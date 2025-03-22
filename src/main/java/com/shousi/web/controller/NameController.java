package com.shousi.web.controller;

import com.shousi.web.common.BaseResponse;
import com.shousi.web.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public BaseResponse<String> getName(String name) {
        return ResultUtils.success(name);
    }

}
