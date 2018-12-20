package com.yinsin.simter.dao.base;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.yinsin.simter.modal.base.Pager;

public interface IBaseDao {

	// 查询单个的
	public <T> T selectOne(T record) throws Exception;

	public <T> T selectOne(String mapperId, T record) throws Exception;
	
	public <T> T selectOne(String mapperId, Integer rowId) throws Exception;
	
	public <T> T selectOne(String mapperId, String rowId) throws Exception;

	public Map<String, Object> selectOne(String mapperId, Map<String, Object> params) throws Exception;

	// 查询多个的
	public <T> List<T> selectList(T record) throws Exception;

	public <T> List<T> selectList(String mapperId, T record) throws Exception;

	public List<Map<String, Object>> selectList(String mapperId, Map<String, Object> params) throws Exception;

	// 查询多个带分页
	public <T> Page<T> selectList(T record, Pager page) throws Exception;

	public <T> Page<T> selectList(String mapperId, T record, Pager page) throws Exception;
	
	public <T> Page<T> selectList(String mapperId, Map<String, Object> params, Pager page) throws Exception;

	// 插入
	public <T> int insert(T record) throws RuntimeException;

	public <T> int insert(String mapperId, T record) throws RuntimeException;

	public int insert(String mapperId, Map<String, Object> params) throws RuntimeException;

	public <T> int insertBatch(String mapperId, List<T> record) throws RuntimeException;

	// 修改
	public <T> int update(T record) throws RuntimeException;

	public <T> int update(String mapperId, T record) throws RuntimeException;

	public int update(String mapperId, Map<String, Object> params) throws RuntimeException;

	public <T> int updateBatch(String mapperId, List<T> record) throws RuntimeException;

	// 删除
	public <T> int delete(T record) throws RuntimeException;

	public <T> int delete(String mapperId, T record) throws RuntimeException;

	public int delete(String mapperId, Map<String, Object> params) throws RuntimeException;

	public <T> int deleteBatch(String mapperId, List<T> record) throws RuntimeException;
	
	//
	public int insert(String sql) throws Exception;

	public int update(String sql) throws Exception;

	public int delete(String sql) throws Exception;

	public <T> T selectOne(String sql) throws Exception;
	
	public <T> List<T> selectList(String sql) throws Exception;
	
	public <T> Page<T> selectList(String sql, Pager page) throws Exception;

}
