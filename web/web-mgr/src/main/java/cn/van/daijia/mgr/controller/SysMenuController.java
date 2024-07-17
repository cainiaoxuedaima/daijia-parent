package cn.van.daijia.mgr.controller;

import cn.van.daijia.common.annotation.Log;
import cn.van.daijia.common.enums.BusinessType;
import cn.van.daijia.common.result.Result;
import cn.van.daijia.mgr.service.SysMenuService;
import cn.van.daijia.model.entity.system.SysMenu;
import cn.van.daijia.model.vo.system.AssginMenuVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @Operation(summary = "获取菜单")
    @GetMapping("findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @Operation(summary = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu permission) {
        sysMenuService.save(permission);
        return Result.ok();
    }

    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @Operation(summary = "修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu permission) {
        sysMenuService.update(permission);
        return Result.ok();
    }

    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @Operation(summary = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.remove(id);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @Operation(summary = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.toAssign(roleId);
        return Result.ok(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.ASSGIN)
    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @Operation(summary = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuVo assginMenuVo) {
        sysMenuService.doAssign(assginMenuVo);
        return Result.ok();
    }
}

