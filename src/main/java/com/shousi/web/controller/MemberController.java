package com.shousi.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shousi.web.annotation.AuthCheck;
import com.shousi.web.common.BaseResponse;
import com.shousi.web.constant.UserConstant;
import com.shousi.web.model.dto.member.MemberQueryRequest;
import com.shousi.web.model.entity.Member;
import com.shousi.web.model.entity.User;
import com.shousi.web.service.MemberService;
import com.shousi.web.service.UserService;
import com.shousi.web.utils.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private UserService userService;

    @Resource
    private MemberService memberService;

    /**
     * 创建会员码
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/create_vip_code")
    public BaseResponse<List<String>> createVipCode(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (!userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限");
        }
        List<String> codes = memberService.createVipCode();
        return ResultUtils.success(codes);
    }

    /**
     * 列出会员码
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/list_vip_code")
    public BaseResponse<IPage<Member>> listVipCode(@RequestBody MemberQueryRequest memberQueryRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (!userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限");
        }
        IPage<Member> codes = memberService.listVipCode(memberQueryRequest);
        return ResultUtils.success(codes);
    }
}
