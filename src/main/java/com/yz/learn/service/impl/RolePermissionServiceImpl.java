package com.yz.learn.service.impl;

import com.google.common.collect.Lists;
import com.yz.learn.entity.SysRolePermission;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysRolePermissionMapper;
import com.yz.learn.service.RolePermissionService;
import com.yz.learn.vo.req.RolePermissionOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {
        sysRolePermissionMapper.removeByRoleId(vo.getRoleId());
        if(vo.getPermissionIds()==null||vo.getPermissionIds().isEmpty()){
            return;
        }
        ArrayList<SysRolePermission> list = Lists.newArrayList();
        for (String permissionId:vo.getPermissionIds()) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(UUID.randomUUID().toString());
            sysRolePermission.setRoleId(vo.getRoleId());
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setCreateTime(new Date());
            list.add(sysRolePermission);
        }
        int count = sysRolePermissionMapper.batchRolePermission(list);
        if(count==0)
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
    }
}
