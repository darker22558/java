package com.geo.integrated.controller.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.SysRole;
import com.geo.integrated.service.SysRoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: whtli
 * @date: 2023/02/14
 * @description: 角色管理控制层
 */
@RestController
@Slf4j
@RequestMapping("/management/system/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 查询角色信息
     *
     * @param name        名称
     * @param description 描述
     * @param flag        唯一标识
     * @param pageNum     页码
     * @param pageSize    页内数量
     * @return 角色信息列表
     */
    @ApiOperation("获取角色信息列表")
    @OperationLogger("获取角色信息列表")
    @GetMapping("/getRoleList")
    public Result getRoleList(@RequestParam(value = "name", defaultValue = "") String name,
                              @RequestParam(value = "description", defaultValue = "") String description,
                              @RequestParam(value = "flag", defaultValue = "") String flag) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.like("description", description);
        queryWrapper.like("flag", flag);
        List<SysRole> sysRoleList = sysRoleService.list(queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("roleList", sysRoleList);
        data.put("total", sysRoleList.size());
        if (sysRoleList.size() == 0) {
            return Result.success("未查找到相应角色数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除角色信息
     *
     * @param id 指定角色信息id
     * @return 删除操作的结果
     */
    @ApiOperation("删除指定角色信息")
    @OperationLogger("删除指定角色信息")
    @DeleteMapping("deleteRoleById")
    public Result deleteRoleById(@RequestParam Long id) {
        boolean delete = sysRoleService.removeById(id);
        if (delete) {
            return Result.success("角色信息删除成功", id);
        } else {
            return Result.fail("角色信息删除失败", id);
        }
    }

    /**
     * 新增或编辑角色信息
     *
     * @param role 角色信息实体类
     * @return 新增或编辑结果
     */
    @ApiOperation("新增或编辑角色信息")
    @OperationLogger("新增或编辑角色信息")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SysRole role) {
        // 创建或更新文献记录
        boolean flag = sysRoleService.saveOrUpdate(role);
        if (flag) {
            return Result.success("角色信息维护成功");
        } else {
            return Result.fail("角色信息维护失败");
        }
    }
}
