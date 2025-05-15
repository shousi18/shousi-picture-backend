package com.shousi.web.service;

import com.shousi.web.model.dto.thumb.DoThumbRequest;
import com.shousi.web.model.entity.Thumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.entity.User;

/**
* @author 86172
* @description 针对表【thumb(点赞表)】的数据库操作Service
* @createDate 2025-05-15 19:05:51
*/
public interface ThumbService extends IService<Thumb> {

    /**
     * 点赞
     * @param doThumbRequest
     * @param loginUser
     * @return
     */
    boolean doThumb(DoThumbRequest doThumbRequest, User loginUser);

    /**
     * 取消点赞
     * @param doThumbRequest
     * @param loginUser
     * @return
     */
    boolean cancelThumb(DoThumbRequest doThumbRequest, User loginUser);

    /**
     * 判断是否点赞
     * @param pictureId
     * @param loginUser
     * @return
     */
    boolean hasThumb(Long pictureId, User loginUser);
}
