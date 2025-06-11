package com.shousi.web.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.mapper.MemberMapper;
import com.shousi.web.model.dto.member.MemberQueryRequest;
import com.shousi.web.model.entity.Member;
import com.shousi.web.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 86172
 * @description 针对表【member(会员表)】的数据库操作Service实现
 * @createDate 2025-06-07 13:47:38
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements MemberService {

    @Override
    public List<String> createVipCode() {
        // 创建一批会员码 默认十个
        // 生成会员码 16位 有大小写字母和数字
        List<String> vipCodes = new ArrayList<>();
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Member member = new Member();
            String vipCode = RandomUtil.randomString(16);
            member.setUserId(0L);
            member.setVipCode(vipCode);
            vipCodes.add(vipCode);
            members.add(member);
        }
        try {
            saveBatch(members);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("会员码生成失败");
        }
        return vipCodes;
    }

    @Override
    public Page<Member> listVipCode(MemberQueryRequest memberQueryRequest) {
        String vipCode = memberQueryRequest.getVipCode();
        Long userId = memberQueryRequest.getUserId();
        int current = memberQueryRequest.getCurrent();
        int pageSize = memberQueryRequest.getPageSize();
        LambdaQueryWrapper<Member> query = new LambdaQueryWrapper<>();
        query.like(vipCode != null, Member::getVipCode, vipCode);
        query.eq(userId != null, Member::getUserId, userId);
        return this.page(new Page<>(current, pageSize), query);
    }
}




