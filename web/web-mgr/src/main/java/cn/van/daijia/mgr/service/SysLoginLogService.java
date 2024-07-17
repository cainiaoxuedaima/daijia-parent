package cn.van.daijia.mgr.service;

import cn.van.daijia.model.entity.system.SysLoginLog;
import cn.van.daijia.model.query.system.SysLoginLogQuery;
import cn.van.daijia.model.vo.base.PageVo;

public interface SysLoginLogService {

    PageVo<SysLoginLog> findPage(Long page, Long limit, SysLoginLogQuery sysLoginLogQuery);

    /**
     * 记录登录信息
     *
     * @param sysLoginLog
     * @return
     */
    void recordLoginLog(SysLoginLog sysLoginLog);

    SysLoginLog getById(Long id);
}
