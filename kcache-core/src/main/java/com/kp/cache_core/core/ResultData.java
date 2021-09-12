package com.kp.cache_core.core;

/**
 * description: ResultData <br>
 * date: 2021/9/12 4:40 下午 <br>
 * author: kangpei <br>
 * version: 1.0 <br>
 */
public class ResultData {

    private ResultCode resultCode;
    private String msg;
    private Object data;

    public ResultData(ResultCode resultCode, String msg, Object data) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.data = data;
    }

    public ResultData(Throwable e) {
        this.resultCode = ResultCode.FAIL;
        this.msg = e.getMessage();
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
