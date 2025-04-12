package com.shousi.web.model.dto.picture;

import lombok.Data;

@Data
public class PictureEditClearRequest {

    /**
     * 图片id
     */
    private Long pictureId;

    /**
     * 空间id
     */
    private Long spaceId;
}
