package com.shousi.web.model.dto.member;

import com.shousi.web.common.PageRequest;
import lombok.Data;

@Data
public class MemberQueryRequest extends PageRequest {

    private String vipCode;

    private Long userId;
}
