package com.shousi.web.service;

import com.shousi.web.model.entity.SysOperaLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86172
* @description 针对表【sys_opera_log(操作日志记录)】的数据库操作Service
* @createDate 2025-04-16 17:31:15
*/
public interface SysOperaLogService extends IService<SysOperaLog> {

    /**
     * 保存操作日志
     * @param sysOperaLog
     */
    void saveSysOperaLog(SysOperaLog sysOperaLog);
}
