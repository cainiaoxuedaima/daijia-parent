package cn.van.daijia.mgr.service.impl;

import cn.van.daijia.mgr.service.SysLoginLogService;
import cn.van.daijia.model.entity.system.SysLoginLog;
import cn.van.daijia.model.query.system.SysLoginLogQuery;
import cn.van.daijia.model.vo.base.PageVo;
import cn.van.daijia.system.client.SysLoginLogFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Autowired
    private SysLoginLogFeignClient sysLoginLogFeignClient;

    @Override
    public PageVo<SysLoginLog> findPage(Long page, Long limit, SysLoginLogQuery sysLoginLogQuery) {
        return sysLoginLogFeignClient.findPage(page, limit, sysLoginLogQuery).getData();
    }

    @Override
    public void recordLoginLog(SysLoginLog sysLoginLog) {
        sysLoginLogFeignClient.recordLoginLog(sysLoginLog);
    }

    @Override
    public SysLoginLog getById(Long id) {
        return sysLoginLogFeignClient.getById(id).getData();
    }
}
