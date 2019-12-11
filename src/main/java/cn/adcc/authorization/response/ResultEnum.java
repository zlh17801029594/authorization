package cn.adcc.authorization.response;

public enum ResultEnum {
    SUCCESS("200", "操作成功"),
    FAILURE("400"),
    UN_AUTHENTICATION("401", "未认证"),
    ACCESS_DENY("403", "权限不足"),
    PAGE_NOT_FOUND("404", "页面不存在"),
    INTERNAL_ERROR("500", "服务端异常"),
    DATABASE_ERROR("600", "数据库异常");

    private String code;
    private String msg;

    ResultEnum(String code) {
        this.code = code;
    }

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
