package com.shousi.web.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class TagVO {
    /**
     * id
     */
    private Long id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签使用次数
     */
    private Integer totalNum;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
