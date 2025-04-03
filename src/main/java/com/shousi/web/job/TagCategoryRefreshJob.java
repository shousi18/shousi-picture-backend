package com.shousi.web.job;

import cn.hutool.json.JSONUtil;
import com.shousi.web.constant.RedisKeyConstant;
import com.shousi.web.model.vo.CategoryVO;
import com.shousi.web.model.vo.TagVO;
import com.shousi.web.service.CategoryService;
import com.shousi.web.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
@Slf4j
public class TagCategoryRefreshJob {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private TagService tagService;

    @Resource
    private CategoryService categoryService;

    @PostConstruct
    public void init() {
        // 缓存预热
        refreshCache();
    }

    /**
     * 每天凌晨更新列表缓存
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dailyRefreshCache() {
        log.info("开始执行每日缓存刷新任务");
        refreshCache();
    }

    /**
     * 统一的缓存刷新方法
     */
    private void refreshCache() {
        try {
            // 刷新标签缓存
            List<TagVO> tagVOS = tagService.listHotTags();
            stringRedisTemplate.opsForValue().set(
                    RedisKeyConstant.PICTURE_TAG_LIST,
                    JSONUtil.toJsonStr(tagVOS),
                    30, // 设置过期时间为30天
                    TimeUnit.DAYS
            );
            List<CategoryVO> categoryVOS = categoryService.listHotCategories();
            stringRedisTemplate.opsForValue().set(
                    RedisKeyConstant.PICTURE_CATEGORY_LIST,
                    JSONUtil.toJsonStr(categoryVOS),
                    30,
                    TimeUnit.DAYS
            );
        } catch (Exception e) {
            log.error("缓存刷新失败", e);
        }
    }
}
