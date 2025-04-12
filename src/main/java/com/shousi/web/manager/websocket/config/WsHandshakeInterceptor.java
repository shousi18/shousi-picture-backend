package com.shousi.web.manager.websocket.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.shousi.web.manager.auth.SpaceUserAuthManager;
import com.shousi.web.manager.auth.constant.SpaceUserPermissionConstant;
import com.shousi.web.model.entity.Picture;
import com.shousi.web.model.entity.SpaceUser;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.SpaceRoleEnum;
import com.shousi.web.model.eums.SpaceUserInviteStatusEnum;
import com.shousi.web.service.PictureService;
import com.shousi.web.service.SpaceUserService;
import com.shousi.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WsHandshakeInterceptor implements HandshakeInterceptor {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    @Resource
    private SpaceUserService spaceUserService;

    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            // 获取请求参数
            // 判断是否有图片id
            String pictureId = servletRequest.getParameter("pictureId");
            if (StrUtil.isBlank(pictureId)) {
                log.error("图片id为空，拒绝握手");
                // 返回false表示拒绝握手
                return false;
            }
            // 判断用户是否登录
            User loginUser = userService.getLoginUser(servletRequest);
            if (ObjectUtil.isEmpty(loginUser)) {
                log.error("用户未登录，拒绝握手");
                return false;
            }
            // 都有的话，判断用户是否有权限
            Picture picture = pictureService.getById(pictureId);
            if (ObjectUtil.isEmpty(picture)) {
                log.error("图片不存在，拒绝握手");
                return false;
            }
            Long spaceId = picture.getSpaceId();
            SpaceUser spaceUser = spaceUserService.lambdaQuery()
                    .eq(SpaceUser::getUserId, loginUser.getId())
                    .eq(SpaceUser::getSpaceId, spaceId)
                    .eq(SpaceUser::getInviteStatus, SpaceUserInviteStatusEnum.AGREE.getValue())
                    .one();
            if (ObjectUtil.isEmpty(spaceUser)) {
                log.error("该图片不在团队空间/用户不在该团队空间，拒绝握手");
                return false;
            }
            List<String> permissionListByRole = spaceUserAuthManager.getPermissionListByRole(spaceUser.getSpaceRole());
            if (!permissionListByRole.contains(SpaceUserPermissionConstant.PICTURE_EDIT)) {
                log.error("用户没有编辑权限，拒绝握手");
                return false;
            }
            // 添加到attributes中，供WebSocketHandler使用
            attributes.put("user", loginUser);
            attributes.put("pictureId", picture.getId());
            attributes.put("userId", loginUser.getId());
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
