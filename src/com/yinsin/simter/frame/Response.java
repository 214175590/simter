package com.yinsin.simter.frame;

import java.util.HashMap;
import java.util.Map;

import com.github.pagehelper.Page;
import com.yinsin.simter.frame.consts.CodeContants;
import com.yinsin.simter.modal.base.PageImpl;

/**
 * 统一的结果对象<br>
 * 统一写出到前端的数据包<br>
 * 出入参均以Map容器存储
 * 
 * @author yisin
 */
public class Response {

    private int code = CodeContants.FAILED;

    private String message = CodeContants.FAILED_TEXT;

    private Map<String, Object> rtn = new HashMap<String, Object>();

    public Response fail() {
        this.code = CodeContants.FAILED;
        this.message = CodeContants.FAILED_TEXT;
        return this;
    }

    public Response fail(String msg) {
        this.code = CodeContants.FAILED;
        this.message = msg;
        return this;
    }

    public Response fail(int code, String msg) {
        if (code != 0 && code != CodeContants.SUCCESS) {
            code = CodeContants.FAILED;
        }
        this.code = code;
        this.message = msg;
        return this;
    }

    public Response fail(int code, String msg, Map<String, Object> result) {
        if (code != 0 && code != CodeContants.SUCCESS) {
            code = CodeContants.FAILED;
        }
        this.code = code;
        this.message = msg;
        this.rtn = result;
        return this;
    }

    public Response fail(Map<String, Object> result) {
        this.code = CodeContants.FAILED;
        this.message = CodeContants.FAILED_TEXT;
        this.rtn = result;
        return this;
    }

    public Response success() {
        this.code = CodeContants.SUCCESS;
        this.message = CodeContants.SUCCESS_TEXT;
        return this;
    }

    public Response success(String message) {
        this.code = CodeContants.SUCCESS;
        this.message = message;
        return this;
    }

    public Response success(int code, String msg) {
        if (code != 0 && code != CodeContants.FAILED) {
            code = CodeContants.SUCCESS;
        }
        this.code = code;
        this.message = msg;
        return this;
    }

    public Response success(int code, String msg, Map<String, Object> result) {
        if (code != 0 && code != CodeContants.FAILED) {
            code = CodeContants.SUCCESS;
        }
        this.code = code;
        this.message = CodeContants.SUCCESS_TEXT;
        this.rtn = result;
        return this;
    }

    public Response success(Map<String, Object> result) {
        this.code = CodeContants.SUCCESS;
        this.message = CodeContants.SUCCESS_TEXT;
        this.rtn = result;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Response setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, Object> getRtn() {
        return rtn;
    }

    public Response setRtn(Map<String, Object> result) {
        this.rtn = result;
        return this;
    }

    public Response setRtnAll(Map<String, Object> result) {
        if (this.rtn != null) {
            this.rtn.putAll(result);
        }
        return this;
    }

    /**
     * 判断操作是否成功
     * 
     * @return true 成功，false 失败
     */
    public boolean isSuccess() {
        return this.code != 0 && this.code == CodeContants.SUCCESS;
    }

    /**
     * 未登录或会话超时
     * @return
     */
    public Response loginTimeout() {
        this.code = 8888;
        this.message = "未登录或会话已超时";
        return this;
    }
    
    /**
     * 无权限操作
     * @return
     */
    public Response noPermissions() {
        this.code = 8899;
        this.message = "权限不足";
        return this;
    }
    
    /**
     * 服务器异常
     * @return
     */
    public Response exception() {
        this.code = 8800;
        this.message = "服务器异常";
        return this;
    }
    
    public Response setToRtn(String key, Object value) {
        this.rtn.put(key, value);
        return this;
    }

    public Response setDataToRtn(Object value) {
        this.rtn.put("data", value);
        return this;
    }
    
    public Response setPage(Page<?> value) {
        this.rtn.put("data", new PageImpl(value));
        return this;
    }

}
