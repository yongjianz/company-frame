package com.yz.learn.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PermissionRespNode {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "菜单权限名称")
    private String title;
    @ApiModelProperty(value = "接口地址")
    private String url;
    @ApiModelProperty(value = "子菜单权限列表")
    private List<?> children;
    @ApiModelProperty(value = "是否展开 默认不展开(false)")
    private boolean spread=true;
}
