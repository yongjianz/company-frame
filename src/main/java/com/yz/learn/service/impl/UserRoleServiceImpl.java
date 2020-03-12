package com.yz.learn.service.impl;


import com.google.common.collect.Lists;
import com.yz.learn.entity.SysRole;
import com.yz.learn.entity.SysUserRole;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysUserRoleMapper;
import com.yz.learn.service.RoleService;
import com.yz.learn.service.UserRoleService;
import com.yz.learn.vo.req.RolePageReqVO;
import com.yz.learn.vo.req.UserRoleOperationReqVO;
import com.yz.learn.vo.resp.UserOwnRoleRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<SysRole> roleList = roleService.selectAll(new RolePageReqVO());
        List<String> roleListByUserId = sysUserRoleMapper.getRoleIdsByUserId(userId);
        UserOwnRoleRespVO result = new UserOwnRoleRespVO();
        result.setAllRole(roleList);
        result.setOwnRoles(roleListByUserId);
        return result;
    }

    @Override
    public int removeByUserId(String userId) {
        return sysUserRoleMapper.removeByUserId(userId);
    }

    @Override
    public int batchUserRole(List<SysUserRole> list) {
        return sysUserRoleMapper.batchUserRole(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRoleInfo(UserRoleOperationReqVO vo) {
        removeByUserId(vo.getUserId());
        ArrayList<SysUserRole> list = Lists.newArrayList();
        for (String roleId:vo.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setCreateTime(new Date());
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setId(UUID.randomUUID().toString());
            list.add(sysUserRole);
        }
        int count = batchUserRole(list);
        if(count==0)
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
    }

}
