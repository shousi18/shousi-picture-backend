package com.shousi.web.manager.websocket.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.shousi.web.manager.websocket.disruptor.PictureEditEventProducer;
import com.shousi.web.manager.websocket.model.PictureEditRequestMessage;
import com.shousi.web.manager.websocket.model.PictureEditResponseMessage;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.PictureEditActionEnum;
import com.shousi.web.model.eums.PictureEditMessageTypeEnum;
import com.shousi.web.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.shousi.web.constant.PictureConstant.PICTURE_EDIT_KEY;

@Component
public class PictureEditHandler extends TextWebSocketHandler {

    @Resource
    private UserService userService;

    @Resource
    private PictureEditEventProducer pictureEditEventProducer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 每张图片的编辑状态
     * key：图片ID
     * value：正在编辑的用户id
     */
    private static final Map<Long, Long> pictureEditingUsers = new ConcurrentHashMap<>();

    /**
     * 每张图片的编辑会话
     * key：图片ID
     * value：正在编辑的用户会话
     */
    private static final Map<Long, Set<WebSocketSession>> pictureEditSessions = new ConcurrentHashMap<>();

    /**
     * 连接建立成功后调用
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        // 保存会话到集合当中
        User user = (User) session.getAttributes().get("user");
        Long pictureId = (Long) session.getAttributes().get("pictureId");
        pictureEditSessions.putIfAbsent(pictureId, ConcurrentHashMap.newKeySet());
        pictureEditSessions.get(pictureId).add(session);
        // 构造响应
        PictureEditResponseMessage pictureEditResponseMessage = new PictureEditResponseMessage();
        pictureEditResponseMessage.setType(PictureEditMessageTypeEnum.INFO.getValue());
        String message = String.format("用户 %s加入编辑", user.getUserName());
        pictureEditResponseMessage.setMessage(message);
        pictureEditResponseMessage.setUser(userService.getUserVO(user));
        // 广播给所有用户
        broadcastToPicture(pictureId, pictureEditResponseMessage);
    }

    /**
     * 接收到客户端消息时调用
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        // 将消息体解析为 PictureEditRequestMessage
        String payload = message.getPayload();
        PictureEditRequestMessage pictureEditRequestMessage = JSONUtil.toBean(payload, PictureEditRequestMessage.class);
        // 取得公共参数
        User user = (User) session.getAttributes().get("user");
        Long pictureId = (Long) session.getAttributes().get("pictureId");

        // 生产消息
        pictureEditEventProducer.publishEvent(pictureEditRequestMessage, session, user, pictureId);
    }

    /**
     * 连接关闭后调用
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // 取得公共参数
        User user = (User) session.getAttributes().get("user");
        Long pictureId = (Long) session.getAttributes().get("pictureId");
        // 移除当前用户的编辑状态
        handleExitEditMessage(null, session, user, pictureId);

        // 删除会话
        Set<WebSocketSession> webSocketSessions = pictureEditSessions.get(pictureId);
        if (webSocketSessions != null) {
            webSocketSessions.remove(session);
            if (webSocketSessions.isEmpty()) {
                // 删除该图片的整个编辑会话
                pictureEditingUsers.remove(pictureId);
            }
        }

        // 构造响应
        PictureEditResponseMessage pictureEditResponseMessage = new PictureEditResponseMessage();
        pictureEditResponseMessage.setType(PictureEditMessageTypeEnum.INFO.getValue());
        String message = String.format("用户 %s退出编辑", user.getUserName());
        pictureEditResponseMessage.setMessage(message);
        pictureEditResponseMessage.setUser(userService.getUserVO(user));
        // 广播给所有用户
        broadcastToPicture(pictureId, pictureEditResponseMessage);
    }

    /**
     * 用户进入编辑操作
     *
     * @param pictureEditRequestMessage
     * @param session
     * @param user
     * @param pictureId
     */
    public void handleEnterEditMessage(PictureEditRequestMessage pictureEditRequestMessage, WebSocketSession session, User user, Long pictureId) throws Exception {
        // 没有用户进行编辑操作，才能进入编辑
        if (!pictureEditingUsers.containsKey(pictureId)) {
            pictureEditingUsers.put(pictureId, user.getId());
            // 构造响应
            PictureEditResponseMessage pictureEditResponseMessage = new PictureEditResponseMessage();
            pictureEditResponseMessage.setType(PictureEditMessageTypeEnum.ENTER_EDIT.getValue());
            String message = String.format("用户 %s开始编辑图片", user.getUserName());
            pictureEditResponseMessage.setMessage(message);
            pictureEditResponseMessage.setUser(userService.getUserVO(user));
            // 广播给所有人
            broadcastToPicture(pictureId, pictureEditResponseMessage);
        }
    }

    /**
     * 用户执行编辑操作
     *
     * @param pictureEditRequestMessage
     * @param session
     * @param user
     * @param pictureId
     */
    public void handleEditActionMessage(PictureEditRequestMessage pictureEditRequestMessage, WebSocketSession session, User user, Long pictureId) throws IOException {
        Long editingUserId = pictureEditingUsers.get(pictureId);
        String editAction = pictureEditRequestMessage.getEditAction();
        PictureEditActionEnum pictureEditActionEnum = PictureEditActionEnum.getEnumByValue(editAction);
        if (pictureEditActionEnum == null) {
            return;
        }
        // 确认是当前编辑者
        if (editingUserId != null && editingUserId.equals(user.getId())) {
            PictureEditResponseMessage pictureEditResponseMessage = new PictureEditResponseMessage();
            pictureEditResponseMessage.setType(PictureEditMessageTypeEnum.EDIT_ACTION.getValue());
            String message = String.format("用户 %s执行了%s操作", user.getUserName(), pictureEditActionEnum.getText());
            pictureEditResponseMessage.setMessage(message);
            pictureEditResponseMessage.setEditAction(editAction);
            pictureEditResponseMessage.setUser(userService.getUserVO(user));
            // 广播给除了自己的用户
            broadcastToPicture(pictureId, pictureEditResponseMessage, session);
        }
    }

    /**
     * 用户退出编辑操作
     *
     * @param pictureEditRequestMessage
     * @param session
     * @param user
     * @param pictureId
     */
    public void handleExitEditMessage(PictureEditRequestMessage pictureEditRequestMessage, WebSocketSession session, User user, Long pictureId) throws Exception {
        Long editingUserId = pictureEditingUsers.get(pictureId);
        if (editingUserId != null && editingUserId.equals(user.getId())) {
            // 移除当前用户的编辑状态
            pictureEditingUsers.remove(pictureId);
            // 构造响应
            PictureEditResponseMessage pictureEditResponseMessage = new PictureEditResponseMessage();
            pictureEditResponseMessage.setType(PictureEditMessageTypeEnum.EXIT_EDIT.getValue());
            String message = String.format("用户 %s退出编辑图片", user.getUserName());
            pictureEditResponseMessage.setMessage(message);
            pictureEditResponseMessage.setUser(userService.getUserVO(user));
            // 广播给所有人
            broadcastToPicture(pictureId, pictureEditResponseMessage);
        }
    }

    /**
     * 通用全局广播（排除自身会话）
     *
     * @param pictureId
     * @param pictureEditResponseMessage
     * @param excludeWebSocketSession
     * @throws IOException
     */
    public void broadcastToPicture(Long pictureId, PictureEditResponseMessage pictureEditResponseMessage, WebSocketSession excludeWebSocketSession) throws IOException {
        Set<WebSocketSession> webSocketSessions = pictureEditSessions.get(pictureId);
        // 创建 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        // 配置序列化：将Long类型转为String类型
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance); // 支持long基本类型
        objectMapper.registerModule(module);
        // 序列化为字符串
        String message = objectMapper.writeValueAsString(pictureEditResponseMessage);
        TextMessage textMessage = new TextMessage(message);
        if (CollUtil.isNotEmpty(webSocketSessions)) {
            for (WebSocketSession webSocketSession : webSocketSessions) {
                // 排除自己
                if (webSocketSession.equals(excludeWebSocketSession)) {
                    continue;
                }
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(textMessage);
                }
            }
        }
    }

    /**
     * 通用全局广播
     *
     * @param pictureId
     * @param pictureEditResponseMessage
     * @throws Exception
     */
    private void broadcastToPicture(Long pictureId, PictureEditResponseMessage pictureEditResponseMessage) throws Exception {
        broadcastToPicture(pictureId, pictureEditResponseMessage, null);
    }

}
