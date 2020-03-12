package com.yz.learn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "视图",description = "负责返回视图")
@Controller
@RequestMapping("/index")
public class IndexController {
    @GetMapping("/404")
    @ApiOperation(value = "跳转404错误页面")
    public String error404(){
        return "error/404";
    }

    @GetMapping("/login")
    @ApiOperation(value = "跳转至登录页面")
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    @ApiOperation(value = "跳转至首页界面")
    public String home(){
        return "home";
    }

    @GetMapping("/main")
    @ApiOperation(value = "跳转默认主页方法")
    public String indexHome(){
        return "main";
    }

    @GetMapping("/menus")
    @ApiOperation(value = "跳转菜单权限页面")
    public String menusList(){
        return "menus/menu";
    }

    @GetMapping("/role")
    @ApiOperation(value = "跳转角色管理页面")
    public String roleList(){
        return "role/role";
    }

    @GetMapping("/dept")
    @ApiOperation(value = "跳转角色管理页面")
    public String deptList(){
        return "depts/dept";
    }

    @GetMapping("/user")
    @ApiOperation(value = "跳转用户管理页面")
    public String userList(){
        return "users/user";
    }
}