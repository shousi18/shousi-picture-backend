package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.constant.RedisLuaScriptConstant;
import com.shousi.web.constant.ThumbMQConstant;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.model.dto.thumb.DoThumbRequest;
import com.shousi.web.model.dto.thumb.ThumbEvent;
import com.shousi.web.model.entity.Thumb;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.LuaStatusEnum;
import com.shousi.web.service.ThumbService;
import com.shousi.web.mapper.ThumbMapper;
import com.shousi.web.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 86172
 * @description 针对表【thumb(点赞表)】的数据库操作Service实现
 * @createDate 2025-05-15 19:05:51
 */
@Service
@Slf4j
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb>
        implements ThumbService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean doThumb(DoThumbRequest doThumbRequest, User loginUser) {
        if (doThumbRequest == null || doThumbRequest.getPictureId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Long userId = loginUser.getId();
        Long pictureId = doThumbRequest.getPictureId();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(userId);

        try {
            Long result = redisTemplate.execute(
                    RedisLuaScriptConstant.THUMB_SCRIPT_MQ,
                    List.of(userThumbKey),
                    pictureId
            );

            if (result == LuaStatusEnum.FAIL.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已点赞");
            }

            // 构建消息
            ThumbEvent thumbEvent = ThumbEvent.builder()
                    .userId(userId)
                    .pictureId(pictureId)
                    .type(ThumbEvent.EventType.INCR)
                    .eventTime(LocalDateTime.now())
                    .build();

            try {
                rabbitTemplate.convertAndSend(ThumbMQConstant.EXCHANGE_NAME, ThumbMQConstant.ROUTING_KEY, thumbEvent);
                log.info("点赞消息发送成功: userId={}, pictureId={}", userId, pictureId);
            } catch (Exception e) {
                // 发送失败，redis 回滚
                redisTemplate.opsForHash().delete(userThumbKey, pictureId.toString());
                log.error("点赞消息发送失败: userId={}, pictureId={}, 错误: {}, 堆栈: {}",
                        userId, pictureId, e.getMessage(), e.getStackTrace());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "点赞失败，请稍后重试");
            }

            return true;
        } catch (Exception e) {
            log.error("点赞操作失败: userId={}, pictureId={}", userId, pictureId, e);
            // 确保Redis中没有点赞记录
            redisTemplate.opsForHash().delete(userThumbKey, pictureId.toString());
            throw e;
        }
    }

    @Override
    public boolean cancelThumb(DoThumbRequest doThumbRequest, User loginUser) {
        if (doThumbRequest == null || doThumbRequest.getPictureId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        Long userId = loginUser.getId();
        Long pictureId = doThumbRequest.getPictureId();
        String userThumbKey = RedisKeyUtil.getUserThumbKey(userId);
        try {
            Long result = redisTemplate.execute(
                    RedisLuaScriptConstant.UNTHUMB_SCRIPT_MQ,
                    List.of(userThumbKey),
                    pictureId
            );
            if (result == LuaStatusEnum.FAIL.getValue()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未点赞");
            }
            // 构建消息
            ThumbEvent thumbEvent = ThumbEvent.builder()
                    .userId(userId)
                    .pictureId(pictureId)
                    .type(ThumbEvent.EventType.DECR)
                    .eventTime(LocalDateTime.now())
                    .build();
            try {
                rabbitTemplate.convertAndSend(ThumbMQConstant.EXCHANGE_NAME, ThumbMQConstant.ROUTING_KEY, thumbEvent);
                log.info("取消点赞消息发送成功: userId={}, pictureId={}", userId, pictureId);
            } catch (Exception e) {
                // 发送失败，redis 回滚
                redisTemplate.opsForHash().put(userThumbKey, pictureId.toString(), true);
                log.error("取消点赞消息发送失败: userId={}, pictureId={}, 错误: {}, 堆栈: {}",
                        userId, pictureId, e.getMessage(), e.getStackTrace());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "取消点赞失败，请稍后重试");
            }
            return true;
        } catch (Exception e) {
            log.error("取消点赞操作失败: userId={}, pictureId={}", userId, pictureId, e);
            redisTemplate.opsForHash().put(userThumbKey, pictureId.toString(), true);
            throw e;
        }
    }

    @Override
    public boolean hasThumb(Long pictureId, User loginUser) {
        return redisTemplate.opsForHash().hasKey(RedisKeyUtil.getUserThumbKey(loginUser.getId()), pictureId.toString());
    }
}




