package com.yz.learn.service.impl;

import com.google.common.collect.Lists;
import com.yz.learn.entity.SysPermission;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysPermissionMapper;
import com.yz.learn.service.PermissionService;
import com.yz.learn.vo.req.PermissionAddReqVO;
import com.yz.learn.vo.resp.PermissionRespNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> result = sysPermissionMapper.selectAll();
        for (SysPermission sysPermission: result){
            SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
            if(parent!=null)
            sysPermission.setPidName(parent.getName());
        }
        return result;
    }

    @Override
    public List<PermissionRespNode> selectAllMenuByTree() {
        List<SysPermission> permissionList = sysPermissionMapper.selectAll();
        List<PermissionRespNode> result = Lists.newArrayList();
        PermissionRespNode respNode = new PermissionRespNode();
        respNode.setId("0");
        respNode.setTitle("默认顶级菜单");
        respNode.setChildren(getTree(permissionList,true));
        result.add(respNode);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo,sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int count=sysPermissionMapper.insertSelective(sysPermission);
        if(count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
        }
        return sysPermission;
    }

    @Override
    public List<PermissionRespNode> premissionTreeList(String userId) {
        return getTree(selectAll(),false);
    }

    @Override
    public List<PermissionRespNode> selectAllByTree() {
        List<SysPermission> all = selectAll();
        return getTree(all,false);
    }

    /**
     * 操作后的菜单类型是目录的时候(1) 父级必须为目录(1)
     * 操作后的菜单类型是菜单的时候(2)，父类必须为目录类型(1)
     * 操作后的菜单类型是按钮的时候(3) 父类必须为菜单类型(2)
     */
    private void verifyForm(SysPermission sysPermission){
            SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
            Integer parentType =parent==null? 1:parent.getType();
            Integer currentType = sysPermission.getType();
             if(parentType.equals(1) && currentType.equals(1)){
                 return;
             }else if(currentType - parentType ==1 && !sysPermission.getPid().equals("0")){
                 return;
             }else {
                 throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
             }

    }

    private List<PermissionRespNode> getTree(List<SysPermission> all,boolean exBtn) {
        List<PermissionRespNode> list = Lists.newArrayList();
        if(all==null||all.isEmpty()){
            return list;
        }
        for (SysPermission sysPermission: all){
            if(sysPermission.getPid().equals("0")){
                PermissionRespNode node = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, node);
                node.setTitle(sysPermission.getName());
                if(exBtn) {
                    node.setChildren(getChildExBtn(node.getId(), all));
                }else{
                    node.setChildren(getChild(node.getId(), all));
                }
                list.add(node);
            }
        }
        return list;
    }

    private List<?> getChild(String id, List<SysPermission> all) {
        List<PermissionRespNode> list = Lists.newArrayList();
        for (SysPermission sysPermission:all) {
            if(sysPermission.getPid().equals(id)){
                PermissionRespNode node = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, node);
                node.setTitle(sysPermission.getName());
                node.setChildren(getChildExBtn(node.getId(), all));
                list.add(node);
            }
        }
        return list;
    }

    private List<PermissionRespNode> getChildExBtn(String id, List<SysPermission> permissionList) {
        List<PermissionRespNode> list = Lists.newArrayList();
        for (SysPermission sysPermission:permissionList) {
            if(sysPermission.getPid().equals(id) && !sysPermission.getType().equals("3")){
                PermissionRespNode node = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, node);
                node.setTitle(sysPermission.getName());
                node.setChildren(getChildExBtn(node.getId(), permissionList));
                list.add(node);
            }
        }
        return list;
    }
}
