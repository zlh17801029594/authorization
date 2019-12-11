package cn.adcc.authorization.utils;

import cn.adcc.authorization.response.Result;
import cn.adcc.authorization.response.ResultEnum;

public class ResultUtil {
    public static Result base(ResultEnum resultEnum) {
        return new Result().setCode(resultEnum.getCode()).setMsg(resultEnum.getMsg());
    }

    public static Result success() {
        return base(ResultEnum.SUCCESS);
    }

    public static <T> Result<T> success(T t) {
        return new Result<T>().setCode(ResultEnum.SUCCESS.getCode()).setMsg(ResultEnum.SUCCESS.getMsg()).setData(t);
    }

    public static Result failure(String msg) {
        return new Result().setCode(ResultEnum.FAILURE.getCode()).setMsg(msg);
    }

    public static Result failure(String code, String msg) {
        return new Result().setCode(code).setMsg(msg);
    }

    public static <T> Result<T> result(String code, String msg, T t) {
        return new Result<T>().setCode(code).setMsg(msg).setData(t);
    }
}
