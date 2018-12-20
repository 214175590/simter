package com.yinsin.simter.frame;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.yinsin.simter.frame.consts.CodeContants;
import com.yinsin.simter.modal.base.Pager;
import com.yinsin.utils.CommonUtils;

/**
 * 统一的入参、出参对象<br>
 * 所有的方法都必须使用此对象作为出入参<br>
 * 出入参均以Map容器存储
 * 
 * @author yisin
 */
public class Argument {

	private int code = CodeContants.FAILED;

	private String message = CodeContants.FAILED_TEXT;

	private Integer rowId = 0;

	private int num = 0;

	private Object obj;

	private Pager pager;

	/**
	 * 分页查询后的结果集，类似于List<Object>，属于出参
	 */
	private Page<?> page;

	private String sql;

	/**
	 * 入参
	 */
	private Map<String, Object> req = new HashMap<String, Object>();
	/**
	 * 出参
	 */
	private Map<String, Object> rtn = new HashMap<String, Object>();

	public Argument fail() {
		this.code = CodeContants.FAILED;
		this.message = CodeContants.FAILED_TEXT;
		return this;
	}

	public Argument fail(String msg) {
		this.code = CodeContants.FAILED;
		this.message = msg;
		return this;
	}

	public Argument fail(int code, String msg) {
		if (code != 0 && code != CodeContants.SUCCESS) {
			code = CodeContants.FAILED;
		}
		this.code = code;
		this.message = msg;
		return this;
	}

	public Argument fail(int code, String msg, Map<String, Object> result) {
		if (code != 0 && code != CodeContants.SUCCESS) {
			code = CodeContants.FAILED;
		}
		this.code = code;
		this.message = msg;
		this.rtn = result;
		return this;
	}

	public Argument fail(Map<String, Object> result) {
		this.code = CodeContants.FAILED;
		this.message = CodeContants.FAILED_TEXT;
		this.rtn = result;
		return this;
	}

	public Argument success() {
		this.code = CodeContants.SUCCESS;
		this.message = CodeContants.SUCCESS_TEXT;
		return this;
	}

	public Argument success(String message) {
		this.code = CodeContants.SUCCESS;
		this.message = message;
		return this;
	}

	public Argument success(int code, String msg) {
		if (code != 0 && code != CodeContants.FAILED) {
			code = CodeContants.SUCCESS;
		}
		this.code = code;
		this.message = msg;
		return this;
	}

	public Argument success(int code, String msg, Map<String, Object> result) {
		if (code != 0 && code != CodeContants.FAILED) {
			code = CodeContants.SUCCESS;
		}
		this.code = code;
		this.message = CodeContants.SUCCESS_TEXT;
		this.rtn = result;
		return this;
	}

	public Argument success(Map<String, Object> result) {
		this.code = CodeContants.SUCCESS;
		this.message = CodeContants.SUCCESS_TEXT;
		this.rtn = result;
		return this;
	}

	public int getCode() {
		return code;
	}

	public Argument setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Argument setMessage(String message) {
		this.message = message;
		return this;
	}

	public Integer getRowId() {
		return rowId;
	}

	public Argument setRowId(Integer rowId) {
		this.rowId = rowId;
		return this;
	}

	public int getNum() {
		return num;
	}

	public Argument setNum(int num) {
		this.num = num;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public Argument setObj(Object obj) {
		this.obj = obj;
		return this;
	}

	public Map<String, Object> getReq() {
		return req;
	}

	public Argument setReq(Map<String, Object> req) {
		this.req = req;
		return this;
	}

	public Argument setReqAll(Map<String, Object> req) {
		if (this.req != null) {
			this.req.putAll(req);
		}
		return this;
	}

	public Map<String, Object> getRtn() {
		return rtn;
	}

	public Argument setRtn(Map<String, Object> result) {
		this.rtn = result;
		return this;
	}

	public Argument setRtnAll(Map<String, Object> result) {
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

	public Object getReq(String key) {
		return this.req.get(key);
	}

	public String getStringForReq(String key) {
		return CommonUtils.objectToString(this.getReq(key));
	}

	public int getIntForReq(String key) {
		return CommonUtils.objectToInt(this.getReq(key));
	}

	public float getFloatForReq(String key) {
		return CommonUtils.objectToFloat(this.getReq(key));
	}

	public double getDoubleForReq(String key) {
		return CommonUtils.objectToDouble(this.getReq(key));
	}

	public long getLongForReq(String key) {
		return CommonUtils.objectToLong(this.getReq(key));
	}

	public Object getRtn(String key) {
		return this.rtn.get(key);
	}

	public String getStringForRtn(String key) {
		return CommonUtils.objectToString(this.getRtn(key));
	}

	public int getIntForRtn(String key) {
		return CommonUtils.objectToInt(this.getRtn(key));
	}

	public float getFloatForRtn(String key) {
		return CommonUtils.objectToFloat(this.getRtn(key));
	}

	public long getLongForRtn(String key) {
		return CommonUtils.objectToLong(this.getRtn(key));
	}

	public Argument setToRtn(String key, Object value) {
		this.rtn.put(key, value);
		return this;
	}

	public Argument setToReq(String key, Object value) {
		this.req.put(key, value);
		return this;
	}

	public Argument setDataToRtn(Object value) {
		this.rtn.put("data", value);
		return this;
	}

	public Argument setDataToReq(Object value) {
		this.req.put("data", value);
		return this;
	}

	public Object getDataForRtn() {
		return this.rtn.get("data");
	}

	public Object getDataForReq() {
		return this.req.get("data");
	}

	public Pager getPager() {
		return pager;
	}

	public Argument setPager(JSONObject param) {
		this.pager = new Pager();
		int num = param.getIntValue("_pageIndex");
		int size = param.getIntValue("_pageSize");
		this.pager.setPageNum(num + 1);
		this.pager.setPageSize(size == 0 ? 10 : size);
		this.pager.setCount(true);
		return this;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Page<?> getPage() {
		return page;
	}

	public void setPage(Page<?> page) {
		this.page = page;
	}

}
