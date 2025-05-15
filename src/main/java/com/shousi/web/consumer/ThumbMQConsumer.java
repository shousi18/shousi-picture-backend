package com.shousi.web.consumer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shousi.web.constant.ThumbMQConstant;
import com.shousi.web.mapper.PictureMapper;
import com.shousi.web.mapper.ThumbMapper;
import com.shousi.web.model.dto.thumb.ThumbEvent;
import com.shousi.web.model.entity.Picture;
import com.shousi.web.model.entity.Thumb;
import com.shousi.web.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
public class ThumbMQConsumer {

    @Resource
    private ThumbMapper thumbMapper;

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private PictureService pictureService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @RabbitListener(queues = ThumbMQConstant.QUEUE_NAME)
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(ThumbEvent thumbEvent) {
        try {
            Long userId = thumbEvent.getUserId();
            Long pictureId = thumbEvent.getPictureId();

            log.info("开始处理点赞事件: userId={}, pictureId={}, type={}",
                    thumbEvent.getUserId(), thumbEvent.getPictureId(), thumbEvent.getType());

            Picture picture = pictureService.getById(pictureId);
            if (picture == null) {
                log.error("图片不存在");
                return;
            }

            if (thumbEvent.getType() == ThumbEvent.EventType.INCR) {
                // 点赞
                log.info("开始处理点赞操作: userId={}, pictureId={}", userId, pictureId);
                processThumbUp(userId, pictureId);
            }else {
                // 取消点赞
                log.info("开始处理取消点赞操作: userId={}, pictureId={}", userId, pictureId);
                processThumbDown(userId, pictureId);
            }

            log.info("处理成功: userId={}, pictureId={}, type={}",
                    thumbEvent.getUserId(), thumbEvent.getPictureId(), thumbEvent.getType());
        } catch (Exception e) {
            log.error("处理点赞事件失败: userId={}, pictureId={}, type={}, error={}, stackTrace={}",
                    thumbEvent.getUserId(), thumbEvent.getPictureId(), thumbEvent.getType(),
                    e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * 处理点赞操作
     * @param userId
     * @param pictureId
     */
    private void processThumbUp(Long userId, Long pictureId) {
        // 检查是否已经点赞，避免重复处理
        LambdaQueryWrapper<Thumb> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Thumb::getUserId, userId)
                .eq(Thumb::getPictureId, pictureId);

        if (thumbMapper.selectCount(queryWrapper) > 0) {
            log.info("用户已经点赞");
            return;
        }

        // 插入点赞记录
        Thumb thumb = new Thumb();
        thumb.setUserId(userId);
        thumb.setPictureId(pictureId);
        thumb.setCreateTime(new Date());
        int rows = thumbMapper.insert(thumb);
        log.info("点赞记录插入结果: rows={}, userId={}, pictureId={}", rows, userId, pictureId);

        if (rows > 0) {
            // 更新图片点赞数
            int updatedRows = pictureMapper.incrementThumbCount(pictureId);
            log.info("图片点赞数更新结果: updatedRows={}, pictureId={}", updatedRows, pictureId);
        }
    }

    /**
     * 处理取消点赞操作
     * @param userId
     * @param pictureId
     */
    private void processThumbDown(Long userId, Long pictureId) {
        // 检查是否已经点赞，避免重复处理
        LambdaQueryWrapper<Thumb> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Thumb::getUserId, userId)
                .eq(Thumb::getPictureId, pictureId);

        if (thumbMapper.selectCount(queryWrapper) == 0) {
            log.info("用户未点赞，无需取消: userId={}, pictureId={}", userId, pictureId);
            return;
        }

        // 取消点赞
        int rows = thumbMapper.delete(queryWrapper);
        log.info("取消点赞结果: rows={}, userId={}, pictureId={}", rows, userId, pictureId);

        if (rows > 0) {
            // 更新图片点赞数
            int updatedRows = pictureMapper.decrementThumbCount(pictureId);
            log.info("图片点赞数更新结果: updatedRows={}, pictureId={}", updatedRows, pictureId);
        }
    }
}
