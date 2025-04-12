package com.shousi.web.constant;

public interface PictureConstant {

    /**
     * 默认标签
     */
    String DEFAULT_TAG_NAME = "默认";

    /**
     * 默认分类
     */
    String DEFAULT_CATEGORY_NAME = "默认";

    /**
     * 第一级图片大小名称
     */
    String PICTURE_FIRST_STAGE_SIZE_NAME = "100KB";

    /**
     * 第一级图片大小值
     */
    long PICTURE_FIRST_STAGE_SIZE_VALUE = 100 * 1024;

    /**
     * 第二级图片大小名称
     */
    String PICTURE_SECOND_STAGE_SIZE_NAME = "500KB";

    /**
     * 第二级图片大小值
     */
    long PICTURE_SECOND_STAGE_SIZE_VALUE = 500 * 1024;

    /**
     * 第三级图片大小名称
     */
    String PICTURE_THIRD_STAGE_SIZE_NAME = "1MB";

    /**
     * 第三级图片大小值
     */
    long PICTURE_THIRD_STAGE_SIZE_VALUE = 1024 * 1024;

    /**
     * 第四级图片大小名称
     */
    String PICTURE_FOURTH_STAGE_SIZE_NAME = ">1MB";

    /**
     * 公共图库id
     */
    long PUBLIC_PICTURE_LIBRARY_ID = 0L;

    /**
     * 公共编辑图片 key
     */
    String PICTURE_EDIT_KEY = "picture:edit:";

    /**
     * 操作历史记录存储（按图片ID存储最后10个操作）
     */
    String PICTURE_EDIT_HISTORY_KEY = "picture:edit:history:";
    /**
     * 完整状态快照存储
     */
    String PICTURE_SNAPSHOT_KEY = "picture:snapshot:";

}
