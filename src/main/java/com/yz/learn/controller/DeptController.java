package com.yz.learn.controller;

import com.yz.learn.entity.SysDept;
import com.yz.learn.service.DeptService;
import com.yz.learn.utils.DataResult;
import com.yz.learn.vo.req.DeptAddReqVO;
import com.yz.learn.vo.resp.DeptRespNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "组织模块-机构管理")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/depts")
    @ApiOperation(value = "获取机构列表接口")
    public DataResult<List<SysDept>> getDeptAll(){
        return DataResult.success(deptService.selectAll());
    }

    @GetMapping("/dept/tree")
    @ApiOperation(value = "获取机构列表接口")
    public DataResult<List<DeptRespNodeVO>> getTreeList(){
            return DataResult.success(deptService.getTreeList());
    }

    @PostMapping("/dept")
    @ApiOperation(value = "新增部门接口")
    public DataResult<SysDept> addDept(@RequestBody @Valid DeptAddReqVO vo){
        DataResult<SysDept> result=DataResult.success();
        result.setData(deptService.addDept(vo));
        return result;
    }
}
