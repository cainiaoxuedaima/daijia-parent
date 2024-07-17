package cn.van.daijia.system.service;

import cn.van.daijia.model.entity.system.SysOperLog;
import cn.van.daijia.model.query.system.SysOperLogQuery;
import cn.van.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysOperLogService extends IService<SysOperLog> {

    PageVo<SysOperLog> findPage(Page<SysOperLog> pageParam, SysOperLogQuery sysOperLogQuery);

    /**
     * 保存系统日志记录
     */
    void saveSysLog(SysOperLog sysOperLog);
}
