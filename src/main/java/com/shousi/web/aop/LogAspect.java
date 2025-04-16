package com.shousi.web.aop;

import com.shousi.web.annotation.Log;
import com.shousi.web.model.entity.SysOperaLog;
import com.shousi.web.service.SysOperaLogService;
import com.shousi.web.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {            // 环绕通知切面类定义

    @Autowired
    private SysOperaLogService sysOperaLogService;

    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) {

        // 构建前置参数
        SysOperaLog sysOperaLog = new SysOperaLog();

        LogUtil.beforeHandleLog(sysLog, joinPoint, sysOperaLog);

        Object proceed = null;
        try {
            // 执行业务方法
            proceed = joinPoint.proceed();

            // 构建响应结果参数
            LogUtil.afterHandleLog(sysLog, proceed, sysOperaLog, 0, null);

        } catch (Throwable e) {
            // 业务方法执行产生异常
            e.printStackTrace();
            LogUtil.afterHandleLog(sysLog, proceed, sysOperaLog, 1, e.getMessage());
            throw new RuntimeException(e);
        }

        // 保存日志数据
        sysOperaLogService.saveSysOperaLog(sysOperaLog);

        // 返回执行结果
        return proceed;
    }
}