package com.yz.learn.service;

import com.yz.learn.entity.SysDept;
import com.yz.learn.vo.req.DeptAddReqVO;
import com.yz.learn.vo.resp.DeptRespNodeVO;

import java.util.List;

public interface DeptService {
    List<SysDept> selectAll();

    List<DeptRespNodeVO> getTreeList();

    SysDept addDept(DeptAddReqVO vo);
}
