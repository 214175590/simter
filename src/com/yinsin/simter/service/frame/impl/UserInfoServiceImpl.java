package com.yinsin.simter.service.frame.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yinsin.simter.frame.Argument;
import com.yinsin.simter.modal.base.Pager;
import com.yinsin.simter.modal.frame.UserInfo;
import com.yinsin.simter.service.base.BaseServiceImpl;
import com.yinsin.simter.service.frame.IUserInfoService;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl extends BaseServiceImpl implements IUserInfoService {
    private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);

    public Argument select(Argument arg) {
        try {
            UserInfo entity = (UserInfo) arg.getObj();
            List<UserInfo> dataList = dao.selectList(getMapper(entity, "select"), entity);
            if(dataList != null){
                arg.success().setToRtn("list", dataList);
            } else {
                arg.fail();
            }
        } catch(Exception e){
            logger.error("查询数据失败：" + e.getMessage(), e);
            arg.fail("查询失败");
        }
        return arg;
    }
    
    public Argument selectPaging(Argument arg) {
        try {
            UserInfo entity = (UserInfo) arg.getObj();
            Pager page = arg.getPager();
            List<UserInfo> dataList = dao.selectList(getMapper(entity, "select"), entity, page);
            if(dataList != null){
                arg.success().setToRtn("list", dataList);
            } else {
                arg.fail();
            }
        } catch(Exception e){
            logger.error("分页查询数据失败：" + e.getMessage(), e);
            arg.fail("分页查询失败");
        }
        return arg;
    }
    
    public Argument insert(Argument arg) {
        try {
            UserInfo entity = (UserInfo) arg.getObj();
            int state = dao.insert(getMapper(entity, "insert"), entity);
            if(state != -1){
                arg.success().setObj(entity).setNum(state);
            } else {
                arg.fail();
            }
        } catch(Exception e){
            logger.error("新增数据失败：" + e.getMessage(), e);
            arg.fail("新增数据失败");
        }
        return arg;
    }
    
    public Argument update(Argument arg) {
        try {
            UserInfo entity = (UserInfo) arg.getObj();
            int state = dao.update(getMapper(entity, "updateByPrimaryColumnSelective"), entity);
            if(state != -1){
                arg.success().setObj(entity).setNum(state);
            } else {
                arg.fail();
            }
        } catch(Exception e){
            logger.error("修改数据失败：" + e.getMessage(), e);
            arg.fail("修改数据失败");
        }
        return arg;
    }
    
    public Argument delete(Argument arg) {
        try {
            UserInfo entity = (UserInfo) arg.getObj();
            int state = dao.delete(getMapper(entity, "delete"), entity);
            if(state != -1){
                arg.success().setObj(entity).setNum(state);
            } else {
                arg.fail();
            }
        } catch(Exception e){
            logger.error("修改数据失败：" + e.getMessage(), e);
            arg.fail("修改数据失败");
        }
        return arg;
    }

}