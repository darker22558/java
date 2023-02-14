package com.geo.integrated.controller.management;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geo.integrated.annotation.OperationLogger;
import com.geo.integrated.common.Constant;
import com.geo.integrated.common.Result;
import com.geo.integrated.entity.SysUser;
import com.geo.integrated.service.SysUserService;
import com.geo.integrated.utils.QiniuOssUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author: whtli
 * @date: 2023/01/14
 * @description: 用户管理控制层
 */
@Api(tags = "SysUserController")
@Slf4j
@RestController
@RequestMapping("/management/system/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private QiniuOssUtils qiniuOssUtils;

    /**
     * 查询用户信息
     *
     * @param username 所获用户名称
     * @param nickname 昵称
     * @param email    邮箱
     * @param pageNum  页码
     * @param pageSize 页内数量
     * @return 用户信息列表
     */
    @ApiOperation("获取用户信息列表")
    @OperationLogger("获取用户信息列表")
    @GetMapping("/getUserList")
    public Result getUserList(@RequestParam(value = "username", defaultValue = "") String username,
                              @RequestParam(value = "nickname", defaultValue = "") String nickname,
                              @RequestParam(value = "email", defaultValue = "") String email,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        queryWrapper.like("nickname", nickname);
        queryWrapper.like("email", email);
        queryWrapper.orderByDesc("create_time");
        // 新建一个分页规则，pageNum代表当前页码，pageSize代表每页数量
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        // 借助Page实现分页查询，借助QueryWrapper实现多参数查询
        IPage<SysUser> pageData = sysUserService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>(2);
        data.put("userList", pageData.getRecords());
        data.put("total", pageData.getTotal());
        if (pageData.getTotal() == 0 && pageData.getRecords().isEmpty()) {
            return Result.success("未查找到相应用户数据", data);
        } else {
            return Result.success("查询成功", data);
        }
    }

    /**
     * 删除用户信息
     *
     * @param id 指定用户信息id
     * @return 删除操作的结果
     */
    @ApiOperation("删除指定用户信息")
    @OperationLogger("删除指定用户信息")
    @DeleteMapping("deleteUserById")
    public Result deleteUserById(@RequestParam Long id) {
        boolean delete = sysUserService.removeById(id);
        if (delete) {
            return Result.success("用户信息删除成功", id);
        } else {
            return Result.fail("用户信息删除失败", id);
        }
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 多个指定用户信息id组成的列表
     * @return 删除操作的结果
     */
    @ApiOperation("批量删除指定用户信息")
    @OperationLogger("批量删除指定用户信息")
    @DeleteMapping("/deleteUserBatchByIds")
    public Result deleteUserBatchByIds(@RequestBody List<Long> ids) {
        boolean delete = sysUserService.removeByIds(ids);
        if (delete) {
            return Result.success("用户信息批量删除成功", ids);
        } else {
            return Result.fail("用户信息批量删除失败", ids);
        }
    }

    /**
     * 新增用户信息
     *
     * @param user 用户信息实体类
     * @return 新增结果
     */
    @ApiOperation("新增用户信息")
    @OperationLogger("新增用户信息")
    @PostMapping("/saveUser")
    public Result saveUser(@RequestBody SysUser user) {
        Result result = sysUserService.saveUserInfo(user);
        return result;
    }

    /**
     * 编辑用户信息
     *
     * @param user 用户信息实体类
     * @return 编辑结果
     */
    @ApiOperation("编辑用户信息")
    @OperationLogger("编辑用户信息")
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody SysUser user) {
        Result result = sysUserService.updateUserInfo(user);
        return result;
    }

    /**
     * 上传头像
     * 借助七牛云工具类
     *
     * @param file 图片文件
     * @return 上传成功后返回的头像地址
     */
    @OperationLogger("向七牛云服务器中上传图片")
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(@RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("请选择图片");
        }
        String fileName = file.getOriginalFilename();
        String fileType = FileUtil.extName(fileName);
        if (Constant.FILE_TYPE_JPG.equals(fileType) || Constant.FILE_TYPE_JPEG.equals(fileType)) {
            String imageUrl = qiniuOssUtils.uploadImage(file);
            return Result.success("头像上传成功", imageUrl);
        } else {
            return Result.fail("图片类型错误，应为.png或.jpg或.jpeg");
        }
    }
}
