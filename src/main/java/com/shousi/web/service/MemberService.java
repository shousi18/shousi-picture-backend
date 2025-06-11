package com.shousi.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shousi.web.model.dto.member.MemberQueryRequest;
import com.shousi.web.model.entity.Member;

import java.util.List;

/**
* @author 86172
* @description 针对表【member(会员表)】的数据库操作Service
* @createDate 2025-06-07 13:47:38
*/
public interface MemberService extends IService<Member> {

    /**
     * 创建会员码
     * @return
     */
    List<String> createVipCode();

    /**
     * 列出会员码
     *
     * @return
     */
    Page<Member> listVipCode(MemberQueryRequest memberQueryRequest);
}
