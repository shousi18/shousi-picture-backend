package com.shousi.web.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.constant.RedissonKeyConstant;
import com.shousi.web.exception.BusinessException;
import com.shousi.web.exception.ErrorCode;
import com.shousi.web.exception.ThrowUtils;
import com.shousi.web.manager.FileManager;
import com.shousi.web.manager.auth.StpKit;
import com.shousi.web.mapper.UserMapper;
import com.shousi.web.model.dto.email.EmailCodeRequest;
import com.shousi.web.model.dto.file.UploadPictureResult;
import com.shousi.web.model.dto.user.UserQueryRequest;
import com.shousi.web.model.dto.user.UserUpdateEmailRequest;
import com.shousi.web.model.dto.user.UserUpdatePasswordRequest;
import com.shousi.web.model.dto.user.VipCode;
import com.shousi.web.model.entity.User;
import com.shousi.web.model.eums.UserRoleEnum;
import com.shousi.web.model.vo.LoginUserVO;
import com.shousi.web.model.vo.UserVO;
import com.shousi.web.service.UserService;
import com.shousi.web.utils.SendMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.shousi.web.constant.UserConstant.*;
import static com.shousi.web.model.eums.UserRoleEnum.VIP;

/**
 * @author 86172
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-03-22 23:29:35
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private FileManager fileManager;

    @Resource
    private SendMailUtils sendMailUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ResourceLoader resourceLoader;

    @Resource
    private RedissonClient redissonClient;

    // 文件读写锁（确保并发安全）
    private final ReentrantLock fileLock = new ReentrantLock();

    @Override
    public long userRegister(String email, String code, String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(email, code, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 校验验证码
        String verifyCodeKey = String.format(EMAIL_CODE_KEY, email, "register");
        String emailCode = stringRedisTemplate.opsForValue().get(verifyCodeKey);
        if (emailCode == null || !code.equals(emailCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
        }
        // 2. 检查邮箱是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该邮箱已被注册过");
        }
        // 3.检查账号是否重复，使用前缀作为用户账号
        String userAccount = email.substring(0, email.indexOf("@"));
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            // 如果重复，加上随机数
            userAccount = userPassword + RandomUtil.randomNumbers(5);
        }
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userAccount);
        user.setEmail(email);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        // 删除验证码
        stringRedisTemplate.delete(verifyCodeKey);
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccountOrEmail, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccountOrEmail, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱/账号或密码为空");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userPassword", encryptPassword)
                .and(w -> w.eq("userAccount", userAccountOrEmail))
                .or()
                .eq("email", userAccountOrEmail);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 4.记录用户登录态到sa-token中
        StpKit.SPACE.login(user.getId());
        StpKit.SPACE.getSession().set(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }


    @Override
    public String getEncryptPassword(String userPassword) {
        // 盐值，混淆密码
        return DigestUtils.md5DigestAsHex((DEFAULT_SALT + userPassword).getBytes());
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    @Override
    public String updateUserAvatar(MultipartFile file, User loginUser) {
        // 校验
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "上传文件为空");
        Long userId = loginUser.getId();
        String uploadPathPrefix = String.format("/public/%s", userId);
        // 上传头像
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(file, uploadPathPrefix);
        String newUrl = uploadPictureResult.getUrl();
        // 更新用户头像
        loginUser.setUserAvatar(newUrl);
        boolean result = this.updateById(loginUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "更新用户头像失败");
        return newUrl;
    }

    @Override
    public boolean updateUserPassword(UserUpdatePasswordRequest userUpdatePasswordRequest, User currentUser) {
        ThrowUtils.throwIf(userUpdatePasswordRequest == null, ErrorCode.PARAMS_ERROR);
        Long userId = userUpdatePasswordRequest.getId();
        ThrowUtils.throwIf(userId <= 0, ErrorCode.PARAMS_ERROR);
        // 如果当前登录用户不是修改密码的用户，没有权限
        if (!currentUser.getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        User loginUser = this.getById(userId);
        // 判断旧密码是否正确
        String userPassword = loginUser.getUserPassword();
        String oldPassword = userUpdatePasswordRequest.getOldPassword();
        if (!userPassword.equals(getEncryptPassword(oldPassword))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "旧密码输入错误");
        }
        String newPassword = userUpdatePasswordRequest.getNewPassword();
        String checkPassword = userUpdatePasswordRequest.getCheckPassword();
        // 判断新旧密码是否一致
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新旧密码不能一致");
        }
        // 判断两次输入的密码是否一致
        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        loginUser.setUserPassword(getEncryptPassword(newPassword));
        return this.updateById(loginUser);
    }

    @Override
    public Long getEmailCode(EmailCodeRequest emailCodeRequest, HttpServletRequest httpServletRequest) {
        ThrowUtils.throwIf(StrUtil.hasBlank(emailCodeRequest.getEmail(), emailCodeRequest.getType()), ErrorCode.PARAMS_ERROR);
        String email = emailCodeRequest.getEmail();
        String type = emailCodeRequest.getType();
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        // 邮箱不能重复
        if (Boolean.TRUE.equals(this.lambdaQuery().eq(User::getEmail, email).exists())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该邮箱已被注册");
        }
        String emailCodeKey = String.format(EMAIL_CODE_KEY, email, type);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(emailCodeKey))) {
            // 如果有验证码，返回过期时间，不再发送验证码
            return stringRedisTemplate.getExpire(emailCodeKey);
        }
        // 生成随机验证码
        String code = RandomUtil.randomNumbers(6);
        try {
            sendMailUtils.sendMail(email, code);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码失败");
        }
        // 发送验证码成功，将验证码存入redis，设置过期时间：5分钟
        stringRedisTemplate.opsForValue().set(emailCodeKey, code, 5, TimeUnit.MINUTES);
        return stringRedisTemplate.getExpire(emailCodeKey);
    }

    @Override
    public Map<String, String> getCaptcha() {
        // 仅包含数字的字符集
        String characters = "0123456789";
        // 生成 4 位数字验证码
        RandomGenerator randomGenerator = new RandomGenerator(characters, 4);
        // 定义图片的显示大小，并创建验证码对象
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(320, 100, 4, 4);
        shearCaptcha.setGenerator(randomGenerator);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        shearCaptcha.write(outputStream);
        byte[] captchaBytes = outputStream.toByteArray();
        String base64Captcha = Base64.getEncoder().encodeToString(captchaBytes);
        String captchaCode = shearCaptcha.getCode();

        // 使用 Hutool 的 MD5 加密
        String encryptedCaptcha = DigestUtil.md5Hex(captchaCode);

        // 将加密后的验证码和 Base64 编码的图片存储到 Redis 中，设置过期时间为 5 分钟（300 秒）
        stringRedisTemplate.opsForValue().set(CAPTCHA_KEY + encryptedCaptcha, captchaCode, 300, TimeUnit.SECONDS);

        Map<String, String> data = new HashMap<>();
        data.put("base64Captcha", base64Captcha);
        data.put("encryptedCaptcha", encryptedCaptcha);
        return data;
    }

    @Override
    public boolean validateCaptcha(String verifyCode, String verifyCodeId) {
        ThrowUtils.throwIf(StrUtil.hasBlank(verifyCode, verifyCodeId), ErrorCode.PARAMS_ERROR);
        String encryptedCaptcha = DigestUtil.md5Hex(verifyCode);
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(CAPTCHA_KEY + encryptedCaptcha))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码错误");
        }
        if (!encryptedCaptcha.equals(verifyCodeId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码错误");
        }
        return true;
    }

    @Override
    public boolean updateUserEmail(UserUpdateEmailRequest userUpdateEmailRequest, User currentUser) {
        // 校验参数
        ThrowUtils.throwIf(userUpdateEmailRequest == null, ErrorCode.PARAMS_ERROR);
        String newEmail = userUpdateEmailRequest.getNewEmail();
        String code = userUpdateEmailRequest.getCode();
        Long userId = userUpdateEmailRequest.getId();
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);
        if (!currentUser.getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        ThrowUtils.throwIf(StrUtil.hasBlank(newEmail, code), ErrorCode.PARAMS_ERROR);
        if (!newEmail.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        String emailCodeKey = String.format(EMAIL_CODE_KEY, newEmail, "changeEmail");
        if (!code.equals(stringRedisTemplate.opsForValue().get(emailCodeKey)) || !Boolean.TRUE.equals(stringRedisTemplate.hasKey(emailCodeKey))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码错误或已过期");
        }
        if (Boolean.TRUE.equals(this.lambdaQuery().eq(User::getEmail, newEmail).exists())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该邮箱已被注册");
        }
        // 更新邮箱
        User user = new User();
        user.setId(currentUser.getId());
        user.setEmail(newEmail);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        // 删除验证码
        stringRedisTemplate.delete(emailCodeKey);
        return true;
    }

    @Override
    public boolean exchangeMember(String vipCode, User user) {
        // 1. 参数校验
        if (user == null || StrUtil.isBlank(vipCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 读取并校验兑换码
        VipCode targetCode = validateAndMarkVipCode(vipCode);
        // 3. 更新用户信息
        updateUserVipInfo(user, targetCode.getCode());
        return true;
    }

    @Override
    public boolean addUserSignIn(long userId) {
        LocalDate date = LocalDate.now();
        // 年份 + userId
        String key = RedissonKeyConstant.getUserSignInRedisKey(date.getYear(), userId);
        RBitSet signInBitSet = redissonClient.getBitSet(key);
        long offset = date.getDayOfYear();
        // 检查当天是否签到
        if (!signInBitSet.get(offset)) {
            // 没有签到，设置
            try {
                signInBitSet.set(offset, true);
                return true;
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到失败");
            }
        }
        // 已经签到过了
        return false;
    }

    @Override
    public List<Integer> getUserSignInRecord(long userId, Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        String key = RedissonKeyConstant.getUserSignInRedisKey(year, userId);
        RBitSet signInBitSet = redissonClient.getBitSet(key);
        // 加载 BitSet 到内存中，避免发送多次请求
        BitSet bitSet = signInBitSet.asBitSet();
        // 统计签到的日期
        List<Integer> dayList = new ArrayList<>();
        // 从索引0开始查找下一个被设置为1的位
        int index = bitSet.nextSetBit(0);
        while (index >= 0) {
            dayList.add(index);
            // 查找下一个被设置为1的位
            index = bitSet.nextSetBit(index + 1);
        }
        return dayList;
    }

    /**
     * 校验兑换码并标记为已使用
     */
    private VipCode validateAndMarkVipCode(String vipCode) {
        fileLock.lock(); // 加锁保证文件操作原子性
        try {
            // 读取 JSON 文件
            JSONArray jsonArray = readVipCodeFile();

            // 查找匹配的未使用兑换码
            List<VipCode> codes = JSONUtil.toList(jsonArray, VipCode.class);
            VipCode target = codes.stream()
                    .filter(code -> code.getCode().equals(vipCode) && !code.isHasUsed())
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR, "无效的兑换码"));

            // 标记为已使用
            target.setHasUsed(true);

            // 写回文件
            writeVipCodeFile(JSONUtil.parseArray(codes));
            return target;
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * 读取兑换码文件
     */
    private JSONArray readVipCodeFile() {
        try {
            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:biz/vipCode.json");
            String content = FileUtil.readString(resource.getFile(), StandardCharsets.UTF_8);
            return JSONUtil.parseArray(content);
        } catch (IOException e) {
            log.error("读取兑换码文件失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统繁忙");
        }
    }

    /**
     * 写入兑换码文件
     */
    private void writeVipCodeFile(JSONArray jsonArray) {
        try {
            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:biz/vipCode.json");
            FileUtil.writeString(jsonArray.toStringPretty(), resource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("更新兑换码文件失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统繁忙");
        }
    }

    /**
     * 更新用户会员信息
     */
    private void updateUserVipInfo(User user, String usedVipCode) {
        // 计算过期时间（当前时间 + 1 年）
        Date expireTime = DateUtil.offsetMonth(new Date(), 12); // 计算当前时间加 1 年后的时间

        // 构建更新对象
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setVipExpireTime(expireTime); // 设置过期时间
        updateUser.setVipCode(usedVipCode);     // 记录使用的兑换码
        updateUser.setUserRole(VIP.getValue());       // 修改用户角色

        // 执行更新
        boolean updated = this.updateById(updateUser);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "开通会员失败，操作数据库失败");
        }
    }
}




