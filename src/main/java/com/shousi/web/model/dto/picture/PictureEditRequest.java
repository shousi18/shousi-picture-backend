package com.shousi.web.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PictureEditRequest implements Serializable {
  
    /**  
     * id  
     */  
    private Long id;

    /**
     * 空间id
     */
    private Long spaceId;

    /**  
     * 图片名称  
     */  
    private String name;  
  
    /**  
     * 简介  
     */  
    private String introduction;  
  
    /**  
     * 分类  
     */  
    private Long categoryId;
  
    /**  
     * 标签  
     */  
    private List<Long> tagIds;
  
    private static final long serialVersionUID = 1L;  
}
