package com.yinsin.simter.frame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 * 在xml文件中已经配置
 * @author allen
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {
	public static final Log log = LogFactory.getLog(ApplicationContextUtil.class);
    private  ApplicationContext context; 
    public  ApplicationContextUtil(){
    	log.info("Spring容器BEAN  ApplicationContextUtil已经启动...............");
    }
	public void setApplicationContext(ApplicationContext context) throws BeansException {
             this.context = context;
	}
    public ApplicationContext getContext(){
    	return this.context;
    }
}
