package cn.van.daijia.system.service;

import cn.van.daijia.model.entity.system.SysPost;
import cn.van.daijia.model.query.system.SysPostQuery;
import cn.van.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPostService extends IService<SysPost> {

    PageVo<SysPost> findPage(Page<SysPost> pageParam, SysPostQuery sysPostQuery);

    void updateStatus(Long id, Integer status);

    List<SysPost> findAll();
}
