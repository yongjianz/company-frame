package com.yz.learn.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPageReqVO extends PageReqVO{

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "账户状态(1.正常 2.锁定 ")
    private Integer status;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
