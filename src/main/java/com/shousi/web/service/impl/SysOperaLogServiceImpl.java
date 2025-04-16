package com.shousi.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shousi.web.model.entity.SysOperaLog;
import com.shousi.web.service.SysOperaLogService;
import com.shousi.web.mapper.SysOperaLogMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 86172
* @description 针对表【sys_opera_log(操作日志记录)】的数据库操作Service实现
* @createDate 2025-04-16 17:31:15
*/
@Service
public class SysOperaLogServiceImpl extends ServiceImpl<SysOperaLogMapper, SysOperaLog>
    implements SysOperaLogService {

    @Resource
    private SysOperaLogMapper sysOperaLogMapper;

    @Override
    @Async
    public void saveSysOperaLog(SysOperaLog sysOperaLog) {
        sysOperaLogMapper.insert(sysOperaLog);
    }
}




