package com.yinsin.simter.service.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.yinsin.simter.dao.base.IBaseDao;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.util.CodeUtil;

@Transactional(readOnly = false)
@Service("baseServiceImpl")
public class BaseServiceImpl implements IBaseService {
    protected static final Logger log = Logger.getLogger(BaseServiceImpl.class);

    @Resource(name = "baseDaoImpl")
    protected IBaseDao dao;

    protected <T> String getMapper(T t, String name) {
        return CodeUtil.getMapperId(t.getClass(), name);
    }

    protected String getMapper(Class<?> clas, String name) {
        return CodeUtil.getMapperId(clas, name);
    }

    @Override
    public Argument selectOneSql(Class<?> cla, Argument br) throws Exception {
        try {
            String sql = CodeUtil.getSelectSql(cla, br.getStringForReq("sql"));
            Object obj = dao.selectOne(sql);
            if (obj == null) {
                br.fail();
            } else {
                br.success().setObj(obj);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument selectOne(String mapperId, Argument br) throws Exception {
        try {
            Object obj = null;
            if (br.getReq() != null) {
                obj = dao.selectOne(mapperId, br.getReq());
            } else if (br.getObj() != null) {
                obj = dao.selectOne(mapperId, br.getObj());
            }
            if (obj == null) {
                br.fail();
            } else {
                br.success().setObj(obj);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument selectOne(Class<?> cla, Argument br) throws Exception {
        return selectOne(CodeUtil.getSelectMapperId(cla), br);
    }

    @Override
    public <T> Argument selectOne(T t) throws Exception {
        Argument br = new Argument();
        try {
            String mapperId = CodeUtil.getSelectMapperId(t.getClass());
            t = dao.selectOne(mapperId, t);
            if (t == null) {
                br.fail();
            } else {
                br.success().setObj(t);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }
    
    @Override
    public <T> Argument selectOne(String mapperId, Integer rowId) throws Exception {
        Argument br = new Argument();
        try {
            T t = dao.selectOne(mapperId, rowId);
            if (t == null) {
                br.fail();
            } else {
                br.success().setObj(t);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }
    
    @Override
    public <T> Argument selectOne(String mapperId, String rowId) throws Exception {
        Argument br = new Argument();
        try {
            T t = dao.selectOne(mapperId, rowId);
            if (t == null) {
                br.fail();
            } else {
                br.success().setObj(t);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument selectListSql(Class<?> cla, Argument br) throws Exception {
        try {
            List<Object> obj = null;
            if (br.getPager() == null) {
                obj = dao.selectList(br.getSql());
            } else {
                obj = dao.selectList(br.getSql(), br.getPager());
            }
            if (obj == null) {
                br.fail();
            } else {
                br.success().setDataToRtn(obj);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument selectList(String mapperId, Argument br) throws Exception {
        try {
            List<Map<String, Object>> list1 = null;
            List<Object> list2 = null;
            Page<Object> page1 = null;
            if (br.getReq() != null && br.getObj() == null) {
                if (br.getPager() == null) {
                    list1 = dao.selectList(mapperId, br.getReq());
                    if (list1 == null) {
                        br.fail();
                    } else {
                        br.success().setDataToRtn(list1);
                    }
                } else {
                	page1 = dao.selectList(mapperId, br.getReq(), br.getPager());
                	if (page1 == null) {
                        br.fail();
                    } else {
                        br.success().setPage(page1);
                    }
                }
            } else if (br.getObj() != null) {
                if (br.getPager() == null) {
                    list2 = dao.selectList(mapperId, br.getObj());
                    if (list2 == null) {
                        br.fail();
                    } else {
                        br.success().setDataToRtn(list2);
                    }
                } else {
                	page1 = dao.selectList(mapperId, br.getObj(), br.getPager());
                	if (page1 == null) {
                        br.fail();
                    } else {
                        br.success().setPage(page1);
                    }
                }
                
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument selectList(Class<?> cla, Argument br) throws Exception {
        return selectList(CodeUtil.getSelectMapperId(cla), br);
    }

    @Override
    public <T> Argument selectList(T t) throws Exception {
        Argument br = new Argument();
        try {
            List<T> list = dao.selectList(CodeUtil.getSelectMapperId(t.getClass()), t);
            if (list == null) {
                br.fail();
            } else {
                br.success().setDataToRtn(list);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument insert(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = -1;
            if (br.getReq() != null) {
                status = dao.insert(mapperId, br.getReq());
            } else if (br.getObj() != null) {
                status = dao.insert(mapperId, br.getObj());
            }
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("新增记录异常：" + e.getMessage());
            throw new RuntimeException("新增记录异常", e);
        }
        return br;
    }

    @Override
    public <T> Argument insert(T t) throws RuntimeException {
        Argument br = new Argument();
        try {
            int status = dao.insert(CodeUtil.getInsertMapperId(t.getClass()), t);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("新增记录异常：" + e.getMessage());
            throw new RuntimeException("新增记录异常", e);
        }
        return br;
    }

    @Override
    public Argument insertBatch(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = dao.insert(mapperId, br.getReq("list"));
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量新增记录异常：" + e.getMessage());
            throw new RuntimeException("批量新增记录异常", e);
        }
        return br;
    }

    @Override
    public Argument update(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = -1;
            if (br.getReq() != null) {
                status = dao.update(mapperId, br.getReq());
            } else if (br.getObj() != null) {
                status = dao.update(mapperId, br.getObj());
            }
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("修改记录异常：" + e.getMessage());
            throw new RuntimeException("修改记录异常", e);
        }
        return br;
    }

    @Override
    public <T> Argument update(T t) throws RuntimeException {
        Argument br = new Argument();
        try {
            int status = dao.update(CodeUtil.getUpdateMapperId(t.getClass()), t);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("修改记录异常：" + e.getMessage());
            throw new RuntimeException("修改记录异常", e);
        }
        return br;
    }

    @Override
    public Argument updateBatch(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = dao.update(mapperId, br.getReq("list"));
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量修改记录异常：" + e.getMessage());
            throw new RuntimeException("批量修改记录异常", e);
        }
        return br;
    }

    @Override
    public Argument delete(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = -1;
            if (br.getReq() != null) {
                status = dao.delete(mapperId, br.getReq());
            } else if (br.getObj() != null) {
                status = dao.delete(mapperId, br.getObj());
            }
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("删除记录异常：" + e.getMessage());
            throw new RuntimeException("删除记录异常", e);
        }
        return br;
    }

    @Override
    public <T> Argument delete(T t) throws RuntimeException {
        Argument br = new Argument();
        try {
            int status = dao.delete(CodeUtil.getInsertMapperId(t.getClass()), t);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量修改记录异常：" + e.getMessage());
            throw new RuntimeException("批量修改记录异常", e);
        }
        return br;
    }

    @Override
    public Argument deleteBatch(String mapperId, Argument br) throws RuntimeException {
        try {
            int status = dao.delete(mapperId, br.getReq("list"));
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量删除记录异常：" + e.getMessage());
            throw new RuntimeException("批量删除记录异常", e);
        }
        return br;
    }

    @Override
    public Argument selectBySql(Argument br) throws Exception {
        try {
            List<Object> obj = null;
            if (br.getPager() == null) {
                obj = dao.selectList(br.getSql());
            } else {
                obj = dao.selectList(br.getSql(), br.getPager());
            }
            if (obj == null) {
                br.fail();
            } else {
                br.success().setDataToRtn(obj);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
            br.fail("查询异常：" + e.getMessage());
        }
        return br;
    }

    @Override
    public Argument insert(Class<?> cla, Argument br) throws RuntimeException {
        try {
            int status = dao.insert(CodeUtil.getInsertMapperId(cla), br);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("新增记录异常：" + e.getMessage());
            throw new RuntimeException("新增记录异常", e);
        }
        return br;
    }

    @Override
    public Argument update(Class<?> cla, Argument br) throws RuntimeException {
        try {
            int status = dao.update(CodeUtil.getInsertMapperId(cla), br);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量修改记录异常：" + e.getMessage());
            throw new RuntimeException("批量修改记录异常", e);
        }
        return br;
    }

    @Override
    public Argument delete(Class<?> cla, Argument br) throws RuntimeException {
        try {
            int status = dao.delete(CodeUtil.getInsertMapperId(cla), br);
            if (status < 0) {
                br.fail();
            } else {
                br.setNum(status);
                br.success();
            }
        } catch (Exception e) {
            br.fail("批量修改记录异常：" + e.getMessage());
            throw new RuntimeException("批量修改记录异常", e);
        }
        return br;
    }

}
