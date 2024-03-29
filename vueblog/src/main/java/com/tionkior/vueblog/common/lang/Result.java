package com.tionkior.vueblog.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : TionKior
 * @date : 2022/1/19 10:19
 */

@Data
public class Result implements Serializable {

    /**
     * 200 是正常,非 200 表示异常
     */
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data) {
        return success(200, "操作成功", data);
    }

    public static Result success(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
