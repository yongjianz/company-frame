package com.yz.learn.mapper;

import com.yz.learn.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 通过用户查询关联的角色id集合
     * @param userId
     * @return
     */
    List<String> getRoleIdsByUserId(String userId);
    int removeByUserId(String userId);
    //批量插入用户和角色关联数据
    int batchUserRole(List<SysUserRole> list);
}