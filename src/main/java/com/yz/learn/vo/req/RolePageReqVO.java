package com.yz.learn.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RolePageReqVO extends PageReqVO{

    @ApiModelProperty(value = "角色ID")
    private String roleId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "状态(1:正常0:弃用)")
    private Integer status;
}
