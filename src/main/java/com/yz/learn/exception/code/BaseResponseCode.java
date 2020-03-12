package com.yz.learn.exception.code;

public enum BaseResponseCode implements ResponseCodeInterface {
   /**
   这个要和前段约定好
    code=0：服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。
    code=4010001：（授权异常） 请求要求身份验证。 客户端需要跳转到登录页面重新登录
    code=4010002：(凭证过期) 客户端请求刷新凭证接口
    code=4030001：没有权限禁止访问
    code=400xxxx：系统主动抛出的业务异常
    code=5000001：系统异常
    **/
    SUCCESS(0, "操作成功"),
    SYSTEM_BUSY(5000001,"系统异常"),
    DATA_ERROR(40000001,"数据错误"),
    METHOD_IDENTITY_ERROR(40000002,"参数校验错误"),
    ACCOUNT_PASSWORD_ERROR(40000003,"账户名和密码不匹配"),
    ACCOUNT_NOT_EXIST(40000004,"账户不存在"),
    ACCOUNT_LOCKED(40000004,"账户已被锁定"),
    TOKEN_ERROR(40000005,"token错误"),
    TOKEN_IS_NLL(40000006,"token为空"),
    ACCOUNT_HAS_DELETED_ERROR(400000007,"账号已被删除"),
    TOKEN_PAST_DUE(400000008,"token已过期"),
    SHIRO_AUTHENTICATION_ERROR(40000009,"校验错误"),
    AUTHORIZED_ERROR(40000010,"无权限访问"),
    OPERATION_MENU_PERMISSION_BTN_ERROR(40000011,"权限添加错误,请选择正确的父节点"),
    OPERATION_EROR(40000012,"操作错误"),
    ;

    private final int code;

    private final String msg;

    BaseResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
