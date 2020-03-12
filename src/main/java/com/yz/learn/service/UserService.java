package com.yz.learn.service;

import com.yz.learn.entity.SysUser;
import com.yz.learn.vo.req.LoginReqVO;
import com.yz.learn.vo.req.UserAddReqVO;
import com.yz.learn.vo.req.UserPageReqVO;
import com.yz.learn.vo.req.UserRoleOperationReqVO;
import com.yz.learn.vo.resp.LoginRespVO;
import com.yz.learn.vo.resp.PageVO;

public interface UserService {
    /**
     *用户登录接口
     *@Author: 小霍
     *@UpdateUser:
     *@Version: 0.0.1
     *@param vo
     *@return com.yingxue.lesson.vo.resp.LoginRespVO *@throws
     */
    LoginRespVO login(LoginReqVO vo);

    void logout(String accessToken, String resfreshToken);

    /**
     *分页查询用户信息
     *@param vo
     *@return com.yz.learn.vo.resp.PageVO<com.yz.learn.entity.SysUser> *@throws
     */
    PageVO<SysUser> pageInfo(UserPageReqVO vo);

    void addUser(UserAddReqVO vo);

    void setUserOwnRole(UserRoleOperationReqVO vo);
}
