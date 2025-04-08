package com.shousi.web.model.dto.space.analyze;

import lombok.Data;

import java.io.Serializable;

/**
 * 空间使用排名情况分析
 */
@Data
public class SpaceRankAnalyzeRequest implements Serializable {

    /**
     * 排名前 N 的空间
     */
    private Integer topN = 10;

    private static final long serialVersionUID = 1L;
}
