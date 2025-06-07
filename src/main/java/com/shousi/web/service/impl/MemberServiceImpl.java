package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.mapper.MemberMapper;
import com.shousi.web.model.entity.Member;
import com.shousi.web.service.MemberService;
import org.springframework.stereotype.Service;

/**
* @author 86172
* @description 针对表【member(会员表)】的数据库操作Service实现
* @createDate 2025-06-07 13:47:38
*/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
    implements MemberService{

}




