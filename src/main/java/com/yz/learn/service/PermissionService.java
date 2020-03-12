package com.yz.learn.service;

import com.yz.learn.entity.SysPermission;
import com.yz.learn.vo.req.PermissionAddReqVO;
import com.yz.learn.vo.resp.PermissionRespNode;

import java.util.List;

public interface PermissionService {

    List<SysPermission> selectAll();

    List<PermissionRespNode> selectAllMenuByTree();

    SysPermission addPermission(PermissionAddReqVO vo);

    List<PermissionRespNode> premissionTreeList(String userId);

    List<PermissionRespNode> selectAllByTree();
}
