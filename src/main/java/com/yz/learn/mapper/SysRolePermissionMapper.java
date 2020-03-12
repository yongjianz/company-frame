package com.yz.learn.mapper;

import com.yz.learn.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    //根据角色id移除和菜单权限关联接口
    int removeByRoleId(String roleId);
    //批量插入角色和菜单权限关联
    int batchRolePermission(List<SysRolePermission> list);
}