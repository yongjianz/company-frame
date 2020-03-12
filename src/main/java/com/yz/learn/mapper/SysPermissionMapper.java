package com.yz.learn.mapper;

import com.yz.learn.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysPermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> selectAll();
}