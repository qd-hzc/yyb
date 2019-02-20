package com.jeeplus.core.web;

public class ResultUtil {
    //当正确时返回的值
    public static Result success(Object data){
        Result result = new Result();
        result.setCode("0000");
        result.setMsg("OK");
        result.setData(data);
        return result;
    }

    public static Result success(){
        return success(null);
    }

    //当错误时返回的值
    public static Result error(String code,String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    //当错误时返回的值
    public static Result error(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        result.setCode("0001");
        return result;
    }
}
