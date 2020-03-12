package com.yz.learn.service.impl;

import com.github.pagehelper.PageHelper;
import com.yz.learn.entity.SysRole;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysRoleMapper;
import com.yz.learn.service.RolePermissionService;
import com.yz.learn.service.RoleService;
import com.yz.learn.utils.PageUtil;
import com.yz.learn.vo.req.RoleAddReqVO;
import com.yz.learn.vo.req.RolePageReqVO;
import com.yz.learn.vo.req.RolePermissionOperationReqVO;
import com.yz.learn.vo.resp.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private RolePermissionService RolePermissionService;

    @Override
    public PageVO<SysRole> pageInfo(RolePageReqVO vo) {
        PageHelper.startPage(vo.getPageSize(), vo.getPageNum());
        List<SysRole> sysRoleList = selectAll(vo);
        return PageUtil.getPageVO(sysRoleList);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRole addRole(RoleAddReqVO vo) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setId(UUID.randomUUID().toString());
        sysRole.setCreateTime(new Date());
        int count = sysRoleMapper.insertSelective(sysRole);
        if(count==0)
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
        RolePermissionOperationReqVO reqVo = new RolePermissionOperationReqVO();
        reqVo.setRoleId(sysRole.getId());
        reqVo.setPermissionIds(vo.getPermissionIds());
        RolePermissionService.addRolePermission(reqVo);
        return sysRole;
    }

    @Override
    public List<SysRole> selectAll(RolePageReqVO vo) {
        return  sysRoleMapper.selectAll(vo);
    }
}
