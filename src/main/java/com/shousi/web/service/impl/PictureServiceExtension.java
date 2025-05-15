package com.shousi.web.service.impl;

import com.shousi.web.mapper.PictureMapper;
import com.shousi.web.mapper.ThumbMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class PictureServiceExtension {

    @Resource
    private PictureMapper  pictureMapper;

    /**
     * 增加图片点赞数
     * @param pictureId 图片ID
     * @return 影响行数
     */
    public int incrementThumbCount(Long pictureId) {
        try {
            int rows = pictureMapper.incrementThumbCount(pictureId);
            log.info("SQL执行成功: 增加图片点赞数, pictureId={}, 影响行数={}", pictureId, rows);
            return rows;
        }catch (Exception e) {
            log.error("SQL执行失败: 增加图片点赞数, pictureId={}, 错误: {}, 堆栈: {}",
                    pictureId, e.getMessage(), e.getStackTrace());
            throw e;
        }
    }

    /**
     * 减少图片点赞数
     * @param pictureId 图片ID
     * @return 影响行数
     */
    public int decrementThumbCount(Long pictureId) {
        log.info("开始执行SQL: 减少图片点赞数, pictureId={}", pictureId);
        try {
            int rows = pictureMapper.decrementThumbCount(pictureId);
            log.info("SQL执行成功: 减少图片点赞数, pictureId={}, 影响行数={}", pictureId, rows);
            return rows;
        } catch (Exception e) {
            log.error("SQL执行失败: 减少图片点赞数, pictureId={}, 错误: {}, 堆栈: {}",
                    pictureId, e.getMessage(), e.getStackTrace());
            throw e;
        }
    }
}
