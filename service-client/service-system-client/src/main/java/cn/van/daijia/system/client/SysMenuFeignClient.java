package cn.van.daijia.system.client;

import cn.van.daijia.common.result.Result;
import cn.van.daijia.model.entity.system.SysMenu;
import cn.van.daijia.model.vo.system.AssginMenuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "service-system")
public interface SysMenuFeignClient {


    /**
     * 获取菜单
     *
     * @return
     */
    @GetMapping("/sysMenu/findNodes")
    Result<List<SysMenu>> findNodes();

    @PostMapping("/sysMenu/save")
    Result<Boolean> save(@RequestBody SysMenu sysMenu);

    @PutMapping("/sysMenu/update")
    Result<Boolean> update(@RequestBody SysMenu permission);

    @DeleteMapping("/sysMenu/remove/{id}")
    Result<Boolean> remove(@PathVariable Long id);

    /**
     * 根据角色获取菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("/sysMenu/toAssign/{roleId}")
    Result<List<SysMenu>> toAssign(@PathVariable Long roleId);

    /**
     * 给角色分配权限
     *
     * @param assginMenuVo
     * @return
     */
    @PostMapping("/sysMenu/doAssign")
    Result<Boolean> doAssign(@RequestBody AssginMenuVo assginMenuVo);
}

