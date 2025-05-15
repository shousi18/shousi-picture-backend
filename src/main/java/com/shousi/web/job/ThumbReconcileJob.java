package com.shousi.web.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shousi.web.constant.ThumbConstant;
import com.shousi.web.constant.ThumbMQConstant;
import com.shousi.web.model.dto.thumb.ThumbEvent;
import com.shousi.web.model.entity.Thumb;
import com.shousi.web.service.ThumbService;
import com.shousi.web.utils.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ThumbReconcileJob {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ThumbService thumbService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 每日2点执行一次
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void reconcile() {
        log.info("开始执行点赞数据同步任务");
        AtomicInteger processedUsers = new AtomicInteger(0);
        AtomicInteger totalEvents = new AtomicInteger(0);

        try {
            // 获取该分片下的所有id
            Set<Long> userIds = new HashSet<>();
            String pattern = ThumbConstant.USER_THUMB_KEY_PREFIX + "*";

            try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(1000).build())) {
                while (cursor.hasNext()) {
                    String key = cursor.next();
                    Long userId = Long.valueOf(key.replace(ThumbConstant.USER_THUMB_KEY_PREFIX, ""));
                    userIds.add(userId);
                }
            }

            int totalUsers = userIds.size();
            log.info("开始点赞对账任务");

            if (totalUsers == 0) {
                log.info("点赞对账任务结束，没有用户需要处理");
                return;
            }

            List<List<Long>> batchUserIds = Lists.partition(Lists.newArrayList(userIds), 100);

            // 异步处理每批用户
            for (List<Long> batchUserIdList : batchUserIds) {
                processUserBatchAsync(batchUserIdList, processedUsers, totalEvents);
            }
            log.info("点赞对账任务结束");
        } catch (Exception e) {
            log.error("点赞对账任务异常", e);
        }
    }


    @Async("taskExecutor")
    public void processUserBatchAsync(List<Long> userIds, AtomicInteger processedUsers, AtomicInteger totalEvents) {
        int batchEvents = 0;
        for (Long userId : userIds) {
            try {
                batchEvents += processUser(userId);
                processedUsers.incrementAndGet();
            } catch (Exception e) {
                log.error("处理用户{}点赞数据异常", userId, e);
            }
        }

        totalEvents.addAndGet(batchEvents);
        log.info("处理用户数 {}，处理了 {} 个点赞事件", userIds.size(), batchEvents);
    }

    /**
     * 处理用户点赞数据
     *
     * @param userId
     * @return
     */
    private int processUser(Long userId) {
        String userThumbKey = RedisKeyUtil.getUserThumbKey(userId);

        // 从redis中获取用户点赞的图片id
        Set<Long> redisPictureIds = redisTemplate.opsForHash().keys(userThumbKey)
                .stream()
                .map(obj -> Long.valueOf(obj.toString()))
                .collect(Collectors.toSet());

        if (redisPictureIds.isEmpty()) {
            return 0;
        }

        // 从数据库中获取用户点赞的图片id
        Set<Long> mysqlPictureIds = Optional.ofNullable(thumbService.lambdaQuery()
                        .eq(Thumb::getUserId, userId)
                        .list()
                ).orElse(Lists.newArrayList())
                .stream()
                .map(Thumb::getPictureId)
                .collect(Collectors.toSet());

        // 计算差异（点赞没有统计上的）
        Set<Long> differenceBlogIds = Sets.difference(redisPictureIds, mysqlPictureIds);
        // 进行补偿
        sendCompensationINCREvent(userId, differenceBlogIds);

        // 计算差异（取消点赞）
        Set<Long> differenceUndoBlogIds = Sets.difference(mysqlPictureIds, redisPictureIds);
        // 进行补偿
        sendCompensationDECREvent(userId, differenceUndoBlogIds);

        return differenceBlogIds.size();
    }

    /**
     * 发送点赞补偿事件
     *
     * @param userId
     * @param pictureIds
     */
    private void sendCompensationINCREvent(Long userId, Set<Long> pictureIds) {
        if (pictureIds.isEmpty()) {
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (Long pictureId : pictureIds) {
            ThumbEvent thumbEvent = ThumbEvent.builder()
                    .userId(userId)
                    .pictureId(pictureId)
                    .type(ThumbEvent.EventType.INCR)
                    .eventTime(LocalDateTime.now())
                    .build();
            try {
                rabbitTemplate.convertAndSend(ThumbMQConstant.EXCHANGE_NAME, ThumbMQConstant.ROUTING_KEY, thumbEvent);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("补偿事件发送失败: userId={}, pictureId={}", userId, pictureId, e);
            }
        }

        if (successCount > 0) {
            log.info("补偿事件发送成功: userId={}, successCount={}, failCount={}", userId, successCount, failCount);
        }
    }

    /**
     * 发送取消点赞补偿事件
     *
     * @param userId
     * @param pictureIds
     */
    private void sendCompensationDECREvent(Long userId, Set<Long> pictureIds) {
        if (pictureIds.isEmpty()) {
            return;
        }

        int successCount = 0;
        int failCount = 0;

        for (Long pictureId : pictureIds) {
            ThumbEvent thumbEvent = ThumbEvent.builder()
                    .userId(userId)
                    .pictureId(pictureId)
                    .type(ThumbEvent.EventType.DECR)
                    .eventTime(LocalDateTime.now())
                    .build();
            try {
                rabbitTemplate.convertAndSend(ThumbMQConstant.EXCHANGE_NAME, ThumbMQConstant.ROUTING_KEY, thumbEvent);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("补偿事件发送失败: userId={}, pictureId={}", userId, pictureId, e);
            }
        }

        if (successCount > 0) {
            log.info("补偿事件发送成功: userId={}, successCount={}, failCount={}", userId, successCount, failCount);
        }
    }
}
