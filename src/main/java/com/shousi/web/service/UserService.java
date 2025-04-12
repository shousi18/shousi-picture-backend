package com.shousi.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shousi.web.model.dto.email.EmailCodeRequest;
import com.shousi.web.model.dto.user.UserQueryRequest;
import com.shousi.web.model.dto.user.UserUpdateEmailRequest;
import com.shousi.web.model.dto.user.UserUpdatePasswordRequest;
import com.shousi.web.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.vo.LoginUserVO;
import com.shousi.web.model.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 86172
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-03-22 23:29:35
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param email         用户邮箱
     * @param code          验证码
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String email, String code, String userPassword, String checkPassword);

    /**
     * 密码加密
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 用户登录
     *
     * @param userAccountOrEmail  用户账户或邮箱
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccountOrEmail, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的已登录用户信息列表
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 通用查询
     *
     * @param userQueryRequest 用户查询参数
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 更新用户头像
     *
     * @param file
     * @param loginUser
     * @return
     */
    String updateUserAvatar(MultipartFile file, User loginUser);

    /**
     * 更新用户密码
     *
     * @param userUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateUserPassword(UserUpdatePasswordRequest userUpdateRequest, User loginUser);

    /**
     * 发送邮箱验证码
     *
     * @param emailCodeRequest
     * @param httpServletRequest
     * @return
     */
    Long getEmailCode(EmailCodeRequest emailCodeRequest, HttpServletRequest httpServletRequest);

    /**
     * 获取图像验证码
     *
     * @return
     */
    Map<String, String> getCaptcha();

    /**
     * 验证图像验证码
     * @param verifyCode
     * @param verifyCodeId
     */
    boolean validateCaptcha(String verifyCode, String verifyCodeId);

    /**
     * 更新用户邮箱
     * @param userUpdateRequest
     * @param currentUser
     * @return
     */
    boolean updateUserEmail(UserUpdateEmailRequest userUpdateRequest, User currentUser);
}
