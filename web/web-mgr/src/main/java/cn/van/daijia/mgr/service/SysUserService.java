package cn.van.daijia.mgr.service;


import cn.van.daijia.model.entity.system.SysUser;
import cn.van.daijia.model.query.system.SysUserQuery;
import cn.van.daijia.model.vo.base.PageVo;

public interface SysUserService {

    SysUser getById(Long id);

    void save(SysUser sysUser);

    void update(SysUser sysUser);

    void remove(Long id);

    PageVo<SysUser> findPage(Long page, Long limit, SysUserQuery sysUserQuery);

    void updateStatus(Long id, Integer status);


}
