package com.shousi.web.model.dto.file;

import lombok.Data;

@Data
public class UploadPictureResult {  
  
    /**  
     * 图片地址  
     */  
    private String url;

    /**
     * 原始图片地址
     */
    private String originUrl;

    /**
     * 缩略图地址
     */
    private String thumbnailUrl;

    /**
     * 图片主色调
     */
    private String picColor;

    /**  
     * 图片名称  
     */  
    private String picName;  
  
    /**  
     * 文件体积  
     */  
    private Long picSize;  
  
    /**  
     * 图片宽度  
     */  
    private int picWidth;  
  
    /**  
     * 图片高度  
     */  
    private int picHeight;  
  
    /**  
     * 图片宽高比  
     */  
    private Double picScale;  
  
    /**  
     * 图片格式  
     */  
    private String picFormat;  
  
}
