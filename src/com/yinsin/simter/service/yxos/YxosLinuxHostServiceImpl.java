package com.yinsin.simter.service.yxos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.yinsin.simter.dao.yxos.YxosLinuxHostMapper;
import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.modal.yxos.YxosLinuxHost;
import com.yinsin.simter.service.base.BaseServiceImpl;

/**
 * 此类为[代码工厂]自动生成
 * 
 * @Desc linux主机信息表
 * @Time 2018-02-11 10:12
 * @GeneratedByCodeFactory
 */
@SuppressWarnings("unchecked")
@Service("yxosLinuxHostServiceImpl")
public class YxosLinuxHostServiceImpl extends BaseServiceImpl {
	private final static Logger logger = LoggerFactory.getLogger(YxosLinuxHostServiceImpl.class);

	public Argument selectById(Argument arg) {
		try {
			String id = (String) arg.getReq("id");
			YxosLinuxHost obj = dao.selectOne(getMapper(YxosLinuxHostMapper.class, "selectByPrimaryColumn"), id);
			if (obj != null) {
				arg.success().setObj(obj);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("查询YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("查询数据失败");
		}
		return arg;
	}

	public Argument select(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			List<YxosLinuxHost> dataList = dao.selectList(getMapper(YxosLinuxHostMapper.class, "select"), entity);
			if (dataList != null) {
				arg.success().setDataToRtn(dataList);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("查询YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("查询数据失败");
		}
		return arg;
	}

	public Argument selectPaging(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			Page<YxosLinuxHost> dataList = dao.selectList(getMapper(YxosLinuxHostMapper.class, "select"), entity, arg.getPager());
			if (dataList != null) {
				arg.success().setPage(dataList);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("分页查询YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("分页查询数据失败");
		}
		return arg;
	}

	public Argument insert(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			int state = dao.insert(getMapper(YxosLinuxHostMapper.class, "insert"), entity);
			if (state != -1) {
				arg.success().setObj(entity).setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("新增YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("新增数据失败");
			throw new RuntimeException("新增YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument insertSelective(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			int state = dao.insert(getMapper(YxosLinuxHostMapper.class, "insertSelective"), entity);
			if (state != -1) {
				arg.success().setObj(entity).setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("新增YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("新增数据失败");
			throw new RuntimeException("新增YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument update(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			int state = dao.update(getMapper(YxosLinuxHostMapper.class, "update"), entity);
			if (state != -1) {
				arg.success().setObj(entity).setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("修改YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("修改数据失败");
			throw new RuntimeException("修改YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument updateSelective(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			int state = dao.update(getMapper(YxosLinuxHostMapper.class, "updateSelective"), entity);
			if (state != -1) {
				arg.success().setObj(entity).setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("修改YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("修改数据失败");
			throw new RuntimeException("修改YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument delete(Argument arg) {
		try {
			YxosLinuxHost entity = (YxosLinuxHost) arg.getObj();
			int state = -1;
			if(entity.getId() != null){
				state = dao.delete(getMapper(YxosLinuxHostMapper.class, "delete"), entity);
			} else if(entity.getHost() != null){
				state = dao.delete(getMapper(YxosLinuxHostMapper.class, "deleteByHost"), entity);
			}
			if (state != -1) {
				arg.success().setObj(entity).setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("删除YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("删除数据失败");
			throw new RuntimeException("删除YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument insertBatch(Argument arg) {
		try {
			List<YxosLinuxHost> entityList = (List<YxosLinuxHost>) arg.getObj();
			int state = dao.update(getMapper(YxosLinuxHostMapper.class, "insertBatch"), entityList);
			if (state != -1) {
				arg.success().setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("批量插入YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("批量插入数据失败");
			throw new RuntimeException("批量插入YxosLinuxHost数据失败");
		}
		return arg;
	}

	public Argument deleteBatch(Argument arg) {
		try {
			List<String> idList = (List<String>) arg.getReq("list");
			int state = dao.delete(getMapper(YxosLinuxHostMapper.class, "deleteBatch"), idList);
			if (state != -1) {
				arg.success().setNum(state);
			} else {
				arg.fail();
			}
		} catch (Exception e) {
			logger.error("批量删除YxosLinuxHost数据失败：" + e.getMessage(), e);
			arg.fail("批量删除数据失败");
			throw new RuntimeException("批量删除YxosLinuxHost数据失败");
		}
		return arg;
	}

}