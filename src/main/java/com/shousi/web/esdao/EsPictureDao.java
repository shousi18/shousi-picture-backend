package com.shousi.web.esdao;

import com.shousi.web.model.entity.es.EsPicture;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsPictureDao extends ElasticsearchRepository<EsPicture, Long> {

    /**
     * 根据名称或简介模糊查询
     */
    List<EsPicture> findByNameContainingOrIntroductionContaining(String name, String introduction);

    /**
     * 根据审核状态查询图片
     */
    List<EsPicture> findByReviewStatus(Integer reviewStatus);
}
