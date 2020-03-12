package com.yz.learn.service;

import com.yz.learn.entity.SysUserRole;
import com.yz.learn.vo.req.UserRoleOperationReqVO;
import com.yz.learn.vo.resp.UserOwnRoleRespVO;

import java.util.List;

public interface UserRoleService {

    UserOwnRoleRespVO getUserOwnRole(String userId);

    //根据用户id 删除和该用户关联的角色关联表数据
    int removeByUserId(String userId);
    //批量插入用户和角色关联数据
    int batchUserRole(List<SysUserRole> list);

    void addUserRoleInfo(UserRoleOperationReqVO vo);
}
