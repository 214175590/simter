package com.yinsin.simter.service.base;

import com.yinsin.simter.frame.Argument;

public interface IBaseService {

	/**
	 * 查询返回单个对象
	 * 
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectOneSql(Class<?> cla, Argument br) throws Exception;

	/**
	 * 查询返回单个对象
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectOne(String mapperId, Argument br) throws Exception;

	/**
	 * 查询返回单个对象
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectOne(Class<?> cla, Argument br) throws Exception;
	
	/**
	 * 查询返回单个对象
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public <T> Argument selectOne(T t) throws Exception;
	
	/**
	 * 查询返回单个对象
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public <T> Argument selectOne(String mapperId, Integer rowId) throws Exception;
	
	public <T> Argument selectOne(String mapperId, String rowId) throws Exception;

	/**
	 * 查询返回多条记录
	 * 
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectListSql(Class<?> cla, Argument br) throws Exception;

	/**
	 * 查询返回多条记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectList(String mapperId, Argument br) throws Exception;

	/**
	 * 查询返回多条记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectList(Class<?> cla, Argument br) throws Exception;
	
	/**
	 * 查询返回多条记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public <T> Argument selectList(T t) throws Exception;

	/**
	 * 新增记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument insert(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 新增记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument insert(Class<?> cla, Argument br) throws RuntimeException;
	
	/**
	 * 新增记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public <T> Argument insert(T t) throws RuntimeException;

	/**
	 * 批量新增记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument insertBatch(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 修改记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument update(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 修改记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument update(Class<?> cla, Argument br) throws RuntimeException;
	
	/**
	 * 修改记录
	 * 
	 * @param T t
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public <T> Argument update(T t) throws RuntimeException;

	/**
	 * 批量修改记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument updateBatch(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 删除记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument delete(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 删除记录
	 * 
	 * @param Class<?>
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument delete(Class<?> cla, Argument br) throws RuntimeException;
	
	/**
	 * 删除记录
	 * 
	 * @param T t
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public <T> Argument delete(T t) throws RuntimeException;

	/**
	 * 批量删除记录
	 * 
	 * @param mapperId
	 *            指定mapperId
	 * @param Argument
	 * @return Argument
	 * @throws RuntimeException
	 */
	public Argument deleteBatch(String mapperId, Argument br) throws RuntimeException;

	/**
	 * 执行自定义查询SQL语句 reqMap<br>
	 * tableName<br>
	 * orderFeild<br>
	 * data: Map< key, value><br>
	 * 
	 * @param Argument
	 * @return Argument
	 * @throws Exception
	 */
	public Argument selectBySql(Argument br) throws Exception;
}
