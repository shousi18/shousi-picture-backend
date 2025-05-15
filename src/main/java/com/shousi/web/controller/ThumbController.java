package com.shousi.web.controller;

import com.shousi.web.common.BaseResponse;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.model.dto.thumb.DoThumbRequest;
import com.shousi.web.model.entity.User;
import com.shousi.web.service.ThumbService;
import com.shousi.web.service.UserService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/thumb")
public class ThumbController {

    @Resource
    private UserService userService;

    @Resource
    private ThumbService thumbService;

    @PostMapping("/doThumb")
    public BaseResponse<Boolean> doThumb(@RequestBody DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getPictureId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = thumbService.doThumb(doThumbRequest, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/cancelThumb")
    public BaseResponse<Boolean> cancelThumb(@RequestBody DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getPictureId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = thumbService.cancelThumb(doThumbRequest, loginUser);
        return ResultUtils.success(result);
    }

    @GetMapping("/status")
    public BaseResponse<Boolean> hasThumb(HttpServletRequest request, @RequestParam Long pictureId) {
        if (pictureId == null || pictureId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = thumbService.hasThumb(pictureId, loginUser);
        return ResultUtils.success(result);
    }
}
