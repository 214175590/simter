package com.yinsin.simter.dao.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yinsin.simter.modal.base.Pager;
import com.yinsin.simter.util.CodeUtil;

/**
 * 公共的Dao实现类 提供基本的增删该查操作、减少大量重复的代码
 */
@SuppressWarnings("unchecked")
@Repository("baseDaoImpl")
public class BaseDaoImpl extends SqlSessionDaoSupport implements IBaseDao {

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public SqlSession dao() {
        return getSqlSession();
    }

    private static final String commMapperId = "";

    // 查询单个
    @Override
    public <T> T selectOne(T record) throws Exception {
        if (record == null) {
            throw new Exception("parameter is null.");
        }
        return dao().selectOne(CodeUtil.getSelectMapperId(record.getClass()), record);
    }

    @Override
    public <T> T selectOne(String mapperId, T record) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectOne(mapperId, record);
    }
    
    @Override
    public <T> T selectOne(String mapperId, Integer rowId) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectOne(mapperId, rowId);
    }
    
    @Override
    public <T> T selectOne(String mapperId, String rowId) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectOne(mapperId, rowId);
    }

    @Override
    public Map<String, Object> selectOne(String mapperId, Map<String, Object> params) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectOne(mapperId, params);
    }

    // 查询多个
    @Override
    public <T> List<T> selectList(T record) throws Exception {
        if (record == null) {
            throw new Exception("parameter is null.");
        }
        return dao().selectList(CodeUtil.getSelectMapperId(record.getClass()), record);
    }

    @Override
    public <T> List<T> selectList(String mapperId, T record) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectList(mapperId, record);
    }

    @Override
    public List<Map<String, Object>> selectList(String mapperId, Map<String, Object> params) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        return dao().selectList(mapperId, params);
    }

    // 查询多个带分页
    @Override
    public <T> Page<T> selectList(T record, Pager page) throws Exception {
        if (record == null) {
            throw new Exception("parameter is null.");
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.isCount());
        return (Page<T>) dao().selectList(CodeUtil.getSelectMapperId(record.getClass()), record);
    }

    @Override
    public <T> Page<T> selectList(String mapperId, T record, Pager page) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.isCount());
        return (Page<T>) dao().selectList(mapperId, record);
    }
    
    @Override
    public <T> Page<T> selectList(String mapperId, Map<String, Object> params, Pager page) throws Exception {
        if (mapperId == null || mapperId.length() == 0) {
            throw new Exception("parameter is null.");
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.isCount());
        return (Page<T>) dao().selectList(mapperId, params);
    }

    // 新增
    @Override
    public <T> int insert(T record) throws RuntimeException {
        if (record == null) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().insert(CodeUtil.getInsertMapperId(record.getClass()), record);
    }

    @Override
    public <T> int insert(String mapperId, T record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().insert(mapperId, record);
    }

    @Override
    public int insert(String mapperId, Map<String, Object> params) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().insert(mapperId, params);
    }

    @Override
    public <T> int insertBatch(String mapperId, List<T> record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0 || record == null || record.size() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().insert(mapperId, record);
    }

    // 修改
    @Override
    public <T> int update(T record) throws RuntimeException {
        if (record == null) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().update(CodeUtil.getUpdateMapperId(record.getClass()), record);
    }

    @Override
    public <T> int update(String mapperId, T record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().update(mapperId, record);
    }

    @Override
    public int update(String mapperId, Map<String, Object> params) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().update(mapperId, params);
    }

    @Override
    public <T> int updateBatch(String mapperId, List<T> record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0 || record == null || record.size() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().update(mapperId, record);
    }

    // 删除
    @Override
    public <T> int delete(T record) throws RuntimeException {
        if (record == null) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().delete(CodeUtil.getDeleteMapperId(record.getClass()), record);
    }

    @Override
    public <T> int delete(String mapperId, T record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().delete(mapperId, record);
    }

    @Override
    public int delete(String mapperId, Map<String, Object> params) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().delete(mapperId, params);
    }

    @Override
    public <T> int deleteBatch(String mapperId, List<T> record) throws RuntimeException {
        if (mapperId == null || mapperId.length() == 0 || record == null || record.size() == 0) {
            throw new RuntimeException("parameter is null.");
        }
        return dao().delete(mapperId, record);
    }

    @Override
    public int insert(String sql) throws Exception {
        return dao().insert(commMapperId, sql);
    }

    @Override
    public int update(String sql) throws Exception {
        return dao().update(commMapperId, sql);
    }

    @Override
    public int delete(String sql) throws Exception {
        return dao().delete(commMapperId, sql);
    }

    @Override
    public <T> T selectOne(String sql) throws Exception {
        return dao().selectOne(commMapperId, sql);
    }

    @Override
    public <T> List<T> selectList(String sql) throws Exception {
        return dao().selectList(commMapperId, sql);
    }

    @Override
    public <T> Page<T> selectList(String sql, Pager page) throws Exception {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.isCount());
        return (Page<T>) dao().selectList(commMapperId, sql);
    }
}
