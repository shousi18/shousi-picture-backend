package com.shousi.web.model.dto.picture;

import lombok.Data;

@Data
public class PictureUploadByBatchRequest {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 偏移量（防止每次从第一张图抓）
     */
    private Integer first;

    /**
     * 抓取数量（默认10条）
     */
    private Integer count = 10;

    /**
     * 图片前缀
     */
    private String namePrefix;
}
