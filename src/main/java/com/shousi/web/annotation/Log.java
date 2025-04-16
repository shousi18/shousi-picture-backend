package com.shousi.web.annotation;

import com.shousi.web.model.eums.BusinessType;
import com.shousi.web.model.eums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {        // 自定义操作日志记录注解

    /**
     * 模块名称
     *
     * @return
     */
    public String title();

    /**
     * 操作人类别
     *
     * @return
     */
    public OperatorType operatorType() default OperatorType.USER;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     *
     * @return
     */
    public BusinessType businessType();

    /**
     * 是否保存请求的参数
     *
     * @return
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     *
     * @return
     */
    public boolean isSaveResponseData() default true;

}