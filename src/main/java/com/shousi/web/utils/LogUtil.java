package com.shousi.web.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.shousi.web.annotation.Log;
import com.shousi.web.manager.auth.StpKit;
import com.shousi.web.model.entity.SysOperaLog;
import io.netty.handler.codec.http.HttpMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import static com.shousi.web.constant.UserConstant.USER_LOGIN_STATE;

public class LogUtil {

    /**
     * 操作执行之后调用
     * @param sysLog
     * @param proceed
     * @param sysOperaLog
     * @param status
     * @param errorMsg
     */
    public static void afterHandleLog(Log sysLog, Object proceed,
                                      SysOperaLog sysOperaLog, int status,
                                      String errorMsg) {
        if (sysLog.isSaveResponseData()) {
            sysOperaLog.setJsonResult(JSONUtil.toJsonStr(proceed));
        }
        sysOperaLog.setStatus(status);
        sysOperaLog.setErrorMsg(errorMsg);
    }

    /**
     * 操作执行之前调用
     * @param sysLog
     * @param joinPoint
     * @param sysOperaLog
     */
    public static void beforeHandleLog(Log sysLog,
                                       ProceedingJoinPoint joinPoint,
                                       SysOperaLog sysOperaLog) {

        // 设置操作模块名称
        sysOperaLog.setTitle(sysLog.title());
        sysOperaLog.setOperatorType(sysLog.operatorType().name());

        // 获取目标方法信息
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        sysOperaLog.setMethod(method.getDeclaringClass().getName() + "." + method.getName());

        // 获取请求相关参数
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        sysOperaLog.setRequestMethod(request.getMethod());
        sysOperaLog.setOperaUrl(request.getRequestURI());
        sysOperaLog.setOperaIp(LogUtil.getIpAddress(request));

        // 设置请求参数
        if (sysLog.isSaveRequestData()) {
            String requestMethod = sysOperaLog.getRequestMethod();
            if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
                String params = JSONUtil.toJsonStr(joinPoint.getArgs()[0]);
                sysOperaLog.setOperaParam(params);
            }
        }
        sysOperaLog.setOperaId(StpKit.SPACE.getLoginIdAsLong());
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

}