package com.shousi.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shousi.web.annotation.AuthCheck;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.common.DeleteRequest;
import com.shousi.web.constant.UserConstant;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.model.dto.email.EmailCodeRequest;
import com.shousi.web.model.dto.user.*;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.vo.LoginUserVO;
import com.shousi.web.model.vo.UserVO;
import com.shousi.web.service.UserService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String email = userRegisterRequest.getEmail();
        String code = userRegisterRequest.getCode();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(StrUtil.hasBlank(email, code, userPassword, checkPassword), ErrorCode.PARAMS_ERROR);
        long result = userService.userRegister(email, code, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccountOrEmail = userLoginRequest.getUserAccountOrEmail();
        String userPassword = userLoginRequest.getUserPassword();
        String verifyCode = userLoginRequest.getVerifyCode();
        String verifyCodeId = userLoginRequest.getVerifyCodeId();
        // 校验参数
        ThrowUtils.throwIf(StrUtil.hasBlank(userAccountOrEmail, userPassword, verifyCode, verifyCodeId), ErrorCode.PARAMS_ERROR);
        // 校验图形验证码
        userService.validateCaptcha(verifyCode, verifyCodeId);
        LoginUserVO loginUserVO = userService.userLogin(userAccountOrEmail, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取防刷验证码
     */
    @GetMapping("/get/captcha")
    public BaseResponse<Map<String, String>> getCaptcha() {
        Map<String, String> captchaData = userService.getCaptcha();
        return ResultUtils.success(captchaData);
    }

    @PostMapping("/get/email_code")
    public BaseResponse<Long> getEmailCode(@RequestBody EmailCodeRequest emailCodeRequest, HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(emailCodeRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StrUtil.hasBlank(emailCodeRequest.getEmail(), emailCodeRequest.getType()), ErrorCode.PARAMS_ERROR);
        Long emailCode = userService.getEmailCode(emailCodeRequest, httpServletRequest);
        return ResultUtils.success(emailCode);
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 创建用户
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update/avatar")
    public BaseResponse<String> updateUserAvatar(@RequestPart("file") MultipartFile file,
                                                 UserUpdateRequest userUpdateRequest,
                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Long userId = userUpdateRequest.getId();
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getById(userId);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_FOUND_ERROR);
        String newUrl = userService.updateUserAvatar(file, loginUser);
        return ResultUtils.success(newUrl);
    }

    @PostMapping("/update/password")
    public BaseResponse<Boolean> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        User currentUser = userService.getLoginUser(request);
        boolean result = userService.updateUserPassword(userUpdateRequest, currentUser);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    @PostMapping("/update/email")
    public BaseResponse<Boolean> updateUserEmail(@RequestBody UserUpdateEmailRequest userUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        User currentUser = userService.getLoginUser(request);
        boolean result = userService.updateUserEmail(userUpdateRequest, currentUser);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

}
