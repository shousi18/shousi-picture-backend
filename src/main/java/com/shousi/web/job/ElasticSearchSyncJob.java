package com.shousi.web.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shousi.web.mapper.CategoryMapper;
import com.shousi.web.mapper.PictureCategoryMapper;
import com.shousi.web.mapper.PictureTagMapper;
import com.shousi.web.mapper.TagMapper;
import com.shousi.web.model.entity.Picture;
import com.shousi.web.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@Slf4j
public class ElasticSearchSyncJob implements CommandLineRunner {

    public static final String INDEX_NAME = "picture";

    @Resource
    private PictureService pictureService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PictureCategoryMapper pictureCategoryMapper;

    @Resource
    private PictureTagMapper pictureTagMapper;

    // 记录上次同步时间
    private Date lastSyncTime = new Date();

//    /**
//     * 启动全量同步（也是一种方法） 区别是 @PostConstruct 是 java 提供的注解
//     */
//    @PostConstruct
//    public void fullSync() {
//        fullSyncPictures();
//    }

    /**
     * 启动全量同步
     */
    @Override
    public void run(String... args) {
        fullSyncPictures();
    }

    /**
     * 全量同步图片数据到ElasticSearch
     */
    private void fullSyncPictures() {
        int batchSize = 1000;
        // 分批查询数据库中的数据
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        long total = pictureService.count(queryWrapper);
        // 有余数则加一页
        long pages = total % batchSize == 0 ? total / batchSize : total / batchSize + 1;

        for (int i = 0; i < pages; i++) {
            // 查询当前页的数据
            queryWrapper.last("limit " + i * batchSize + ", " + batchSize);
            // 查询当前页的数据
            List<Picture> pictureList = pictureService.list(queryWrapper);

            if (CollUtil.isEmpty(pictureList)) {
                return;
            }

            List<Picture> pictureWithCategoryTagList = fillCategoryAndTag(pictureList);

            List<IndexQuery> queries = pictureWithCategoryTagList.stream()
                    .map(picture -> new IndexQueryBuilder()
                            .withId(String.valueOf(picture.getId()))
                            .withObject(picture)
                            .build())
                    .collect(Collectors.toList());

            elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME));
            log.info("同步图片数据到ES: 第{}批, 数量{}", i + 1, pictureWithCategoryTagList.size());
        }
    }

    /**
     * 增量同步（十分钟同步一次）
     */
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void incrementalSyncPictures() {
        try {
            log.info("开始执行增量同步任务");
            Date currentTime = new Date();
            incrementalSyncPictures(lastSyncTime, currentTime);
            lastSyncTime = currentTime;
            log.info("增量同步任务结束");
        } catch (Exception e) {
            log.error("增量同步任务异常", e);
        }
    }

    /**
     * 增量同步图片数据到ElasticSearch
     *
     * @param startTime
     * @param endTime
     */
    private void incrementalSyncPictures(Date startTime, Date endTime) {
        // 查询这段时间内更新的未删除数据
        List<Picture> pictures = pictureService.list(new QueryWrapper<Picture>()
                .ge("updateTime", startTime)
                .le("updateTime", endTime)
                .eq("isDelete", 0));

        if (CollUtil.isNotEmpty(pictures)) {
            List<Picture> pictureWithCategoryTagList = fillCategoryAndTag(pictures);
            List<IndexQuery> queries = pictureWithCategoryTagList.stream()
                    .map(picture -> new IndexQueryBuilder()
                            .withId(String.valueOf(picture.getId()))
                            .withObject(picture)
                            .build())
                    .collect(Collectors.toList());
            elasticsearchRestTemplate.bulkIndex(queries, IndexCoordinates.of(INDEX_NAME));
            log.info("增量同步图片数据到ES, 数量: {}", pictures.size());
        }

        // 处理这段时间被删除的图片
        List<Picture> deletePictures = pictureService.list(new QueryWrapper<Picture>()
                .ge("updateTime", startTime)
                .le("updateTime", endTime)
                .eq("isDelete", 1));

        if (CollUtil.isNotEmpty(deletePictures)) {
            deletePictures.forEach(picture -> {
                // 根据图片id删除
                elasticsearchRestTemplate.delete(String.valueOf(picture.getId()), IndexCoordinates.of(INDEX_NAME));
                log.info("删除图片数据到ES, id: {}", picture.getId());
            });
        }
    }

    /**
     * 填充图片分类和标签
     *
     * @param pictures
     * @return
     */
    private List<Picture> fillCategoryAndTag(List<Picture> pictures) {
        // 根据id拿到图片对应的分类名称和图片对应的标签名称
        return pictures.stream()
                .map(picture -> {
                    Long pictureId = picture.getId();
                    picture.setCategoryName(pictureCategoryMapper.selectCategoryName(pictureId));
                    picture.setTagNames(pictureTagMapper.selectTagName(pictureId));
                    return picture;
                })
                .collect(Collectors.toList());
    }
}
