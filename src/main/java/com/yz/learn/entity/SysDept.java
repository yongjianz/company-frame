package com.yz.learn.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class SysDept implements Serializable {
    private String id;

    private String deptNo;

    private String name;

    private String pid;

    private Integer status;

    private String relationCode;

    private String deptManagerId;

    private String managerName;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private String pidName;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deptNo=").append(deptNo);
        sb.append(", name=").append(name);
        sb.append(", pid=").append(pid);
        sb.append(", status=").append(status);
        sb.append(", relationCode=").append(relationCode);
        sb.append(", deptManagerId=").append(deptManagerId);
        sb.append(", managerName=").append(managerName);
        sb.append(", phone=").append(phone);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}