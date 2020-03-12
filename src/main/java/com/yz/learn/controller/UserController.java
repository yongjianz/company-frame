package com.yz.learn.controller;


import com.yz.learn.constants.Constant;
import com.yz.learn.service.UserRoleService;
import com.yz.learn.service.UserService;
import com.yz.learn.utils.DataResult;
import com.yz.learn.vo.req.LoginReqVO;
import com.yz.learn.vo.req.UserAddReqVO;
import com.yz.learn.vo.req.UserPageReqVO;
import com.yz.learn.vo.req.UserRoleOperationReqVO;
import com.yz.learn.vo.resp.LoginRespVO;
import com.yz.learn.vo.resp.UserOwnRoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "用户模块接口")
@RequestMapping("/api")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @ApiOperation("用户登录接口")
    @PostMapping("/user/login")
    public DataResult login(@RequestBody @Valid LoginReqVO vo){
        LoginRespVO loginRespVO = userService.login(vo);
        return DataResult.success(loginRespVO);
    }

    @GetMapping("/user/logout")
    @ApiOperation(value = "用户登出接口")
    public DataResult logout(HttpServletRequest request){

            String accessToken=request.getHeader(Constant.ACCESS_TOKEN);
            String refreshToken=request.getHeader(Constant.REFRESH_TOKEN);
            userService.logout(accessToken,refreshToken);
        return DataResult.success();
    }

    @ApiOperation(value = "分页获取全部用户信息")
    @PostMapping("/users")
    public DataResult getAllUser(@RequestBody  UserPageReqVO vo){
        return DataResult.success(userService.pageInfo(vo));
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/user")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO vo){
        userService.addUser(vo);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取用户拥有角色接口")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId")String userId){
        DataResult<UserOwnRoleRespVO> result=DataResult.success();
        result.setData(userRoleService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles")
    @ApiOperation(value = "保持用户拥有的角色信息接口")
    public DataResult saveUserOwnRole(@RequestBody @Valid UserRoleOperationReqVO vo){
        DataResult result=DataResult.success();
        userService.setUserOwnRole(vo);
        return result;
    }
}
