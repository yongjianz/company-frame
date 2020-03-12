package com.yz.learn.service;

import com.yz.learn.entity.SysRole;
import com.yz.learn.vo.req.RoleAddReqVO;
import com.yz.learn.vo.req.RolePageReqVO;
import com.yz.learn.vo.resp.PageVO;

import java.util.List;

public interface RoleService {

    PageVO<SysRole> pageInfo(RolePageReqVO vo);

    SysRole addRole(RoleAddReqVO vo);

    List<SysRole> selectAll(RolePageReqVO vo);
}
