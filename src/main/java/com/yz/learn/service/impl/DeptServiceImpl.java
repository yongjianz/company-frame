package com.yz.learn.service.impl;

import com.google.common.collect.Lists;
import com.yz.learn.constants.Constant;
import com.yz.learn.entity.SysDept;
import com.yz.learn.exception.BusinessException;
import com.yz.learn.exception.code.BaseResponseCode;
import com.yz.learn.mapper.SysDeptMapper;
import com.yz.learn.service.DeptService;
import com.yz.learn.service.RedisService;
import com.yz.learn.utils.CodeUtil;
import com.yz.learn.vo.req.DeptAddReqVO;
import com.yz.learn.vo.resp.DeptRespNodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DeptServiceImpl implements DeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public List<SysDept> selectAll() {
        List<SysDept> deptList = sysDeptMapper.selectAll();
        for(SysDept temp : deptList){
            SysDept parent = sysDeptMapper.selectByPrimaryKey(temp.getPid());
            if (parent!=null){
                temp.setPidName(parent.getName());
            }
        }
        return deptList;
    }

    @Override
    public List<DeptRespNodeVO> getTreeList() {
        List<SysDept> all = selectAll();
        ArrayList<DeptRespNodeVO> list = Lists.newArrayList();
        DeptRespNodeVO vo = new DeptRespNodeVO();
        vo.setId("0");
        vo.setSpread(true);
        vo.setTitle("默认顶级部门");
        vo.setChildren(getTree(all));
        list.add(vo);
        return list;

    }

    @Override
    public SysDept addDept(DeptAddReqVO vo) {
        String relationCode;
        long result = redisService.incrby(Constant.DEPT_CODE_KEY,1);
        String deptCode = CodeUtil.deptCode(String.valueOf(result),6,"0");
        SysDept parent = sysDeptMapper.selectByPrimaryKey(vo.getPid());
        if(vo.getPid().equals("0")){
            relationCode = deptCode;
        }else if(parent == null){
            log.error("传入的pid:{}不合法",vo.getPid());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }else{
            relationCode = parent.getRelationCode()+deptCode;
        }
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(vo, sysDept);
        sysDept.setCreateTime(new Date());
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setDeptNo(deptCode);
        sysDept.setRelationCode(relationCode);
        int count = sysDeptMapper.insertSelective(sysDept);
        if(count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_EROR);
        }
        return sysDept;
    }

    private List<?> getTree(List<SysDept> all) {
        ArrayList<DeptRespNodeVO> list = Lists.newArrayList();
        for (SysDept sysDept:all) {
            if(sysDept.getPid().equals("0")){
                DeptRespNodeVO nodeVO = buildDeptRespNodeVO(sysDept);
                nodeVO.setChildren(getChild(nodeVO.getId(),all));
                list.add(nodeVO);
            }
        }
        return list;
    }

    private List<DeptRespNodeVO> getChild(String id, List<SysDept> all) {
        ArrayList<DeptRespNodeVO> list = Lists.newArrayList();
        for(SysDept sysDept:all){
            if(sysDept.getPid().equals(id)){
                DeptRespNodeVO nodeVO = buildDeptRespNodeVO(sysDept);
                nodeVO.setChildren(getChild(nodeVO.getId(),all));
                list.add(nodeVO);
            }
        }
        return list;
    }

    private DeptRespNodeVO buildDeptRespNodeVO(SysDept sysDept) {
        DeptRespNodeVO nodeVO = new DeptRespNodeVO();
        BeanUtils.copyProperties(sysDept, nodeVO);
        nodeVO.setTitle(sysDept.getName());
        nodeVO.setSpread(true);
        return nodeVO;
    }
}
