package com.yinsin.simter.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yinsin.simter.dao.base.BaseDaoImpl;
import com.yinsin.utils.BeanUtils;
import com.yinsin.utils.CommonUtils;
import com.yinsin.utils.DbUtils;

public class CodeUtil {

    private static String DAO_PACKAGE_PREFIX = null;
    private static String MODAL_PACKAGE_PREFIX = null;
    
    private static Map<String, IdWorker> IDWORKER_MAP = new HashMap<String, IdWorker>();

    /**
     * 获取类的包路径
     * 
     * @param entity
     * @return
     */
    public static String getClassPackage(Class<?> cla) {
        String pack = "";
        if (cla != null) {
            pack = cla.getPackage().getName();
        }
        return pack;
    }

    /**
     * 获取dao包路径前缀
     * 
     * @return
     */
    public static String getDaoPackagePerfix(Class<?> clasz) {
        if (DAO_PACKAGE_PREFIX == null) {
            String superPack = getClassPackage(BaseDaoImpl.class);
            DAO_PACKAGE_PREFIX = superPack.substring(0, superPack.lastIndexOf(".") + 1);
        }
        String name = clasz.getPackage().getName();
        return DAO_PACKAGE_PREFIX + name.substring(name.lastIndexOf(".") + 1) + ".";
    }

    /**
     * 获取dao包路径前缀
     * 
     * @return
     */
    public static String getModelPackagePerfix() {
        if (MODAL_PACKAGE_PREFIX == null) {
            String superPack = getClassPackage(CodeUtil.class);
            superPack = superPack.substring(0, superPack.lastIndexOf("."));
            MODAL_PACKAGE_PREFIX = superPack + ".modal";
        }
        return MODAL_PACKAGE_PREFIX;
    }

    /**
     * 组装Mybatis执行SQL的MapperId
     * 
     * @param clasz
     * @param mapperMothed
     * @return
     */
    public static String getMapperId(Class<?> clasz, String mapperMothed) {
        String className = clasz.getSimpleName();
        if (!className.endsWith("Mapper")) {
            className += "Mapper";
        }
        return getDaoPackagePerfix(clasz) + className + "." + mapperMothed;
    }

    public static String getInsertMapperId(Class<?> clasz) {
        String className = clasz.getSimpleName();
        if (!className.endsWith("Mapper")) {
            className += "Mapper";
        }
        return getDaoPackagePerfix(clasz) + className + ".insert";
    }

    public static String getUpdateMapperId(Class<?> clasz) {
        String className = clasz.getSimpleName();
        if (!className.endsWith("Mapper")) {
            className += "Mapper";
        }
        return getDaoPackagePerfix(clasz) + className + ".update";
    }

    public static String getDeleteMapperId(Class<?> clasz) {
        String className = clasz.getSimpleName();
        if (!className.endsWith("Mapper")) {
            className += "Mapper";
        }
        return getDaoPackagePerfix(clasz) + className + ".delete";
    }

    public static String getSelectMapperId(Class<?> clasz) {
        String className = clasz.getSimpleName();
        if (!className.endsWith("Mapper")) {
            className += "Mapper";
        }
        return getDaoPackagePerfix(clasz) + className + ".select";
    }

    public static <T> String getSelectSql(Class<?> clas, String conditSql) {
        String table = DbUtils.entityToTable(clas.getSimpleName().replace(".java", ""));
        StringBuilder sql = new StringBuilder("select ");
        StringBuilder column = new StringBuilder();
        Field[] fields = clas.getDeclaredFields();
        for (Field field : fields) {
            column.append(field.getName()).append(",");
        }
        String columnSql = column.toString();
        sql.append(columnSql.substring(0, columnSql.length())).append(" from ").append(table);
        if (conditSql.trim().toLowerCase().startsWith("where")) {
            sql.append(" ");
        } else {
            if (conditSql.trim().toLowerCase().startsWith("and")) {
                sql.append(" where 1 = 1 ");
            } else {
                sql.append(" where ");
            }
        }
        sql.append(conditSql);
        return sql.toString();
    }

    public static <T> String getInsertSql(T t) {
        List<Map<String, Object>> filedList = getFiledsInfo(t);
        String entityName = CommonUtils.objectToString(filedList.get(0).get("obj_name"));
        String table = DbUtils.entityToTable(entityName);
        StringBuilder sql = new StringBuilder("insert into ");
        sql.append(table).append(" (");
        String javaType = "", field = "";
        StringBuilder column = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (Map<String, Object> fieldMap : filedList) {
            if (fieldMap.get("f_value") != null) {
                javaType = CommonUtils.objectToString(fieldMap.get("f_type"));
                field = CommonUtils.objectToString(fieldMap.get("f_name"));
                if (field.equals("id")) {
                    column.append(DbUtils.feildToColumn(field)).append(",");
                    if (javaType.indexOf("String") != -1) {
                        values.append("'").append(fieldMap.get("f_value")).append("',");
                    } else if (javaType.indexOf("Date") != -1) {
                        values.append("STR_TO_DATE(").append(fieldMap.get("f_value")).append(", '%Y-%m-%d %H:%i:%s'),");
                    } else {
                        values.append(fieldMap.get("f_value")).append(",");
                    }
                }
            }
        }
        String columnSql = column.toString();
        String valuesSql = values.toString();
        sql.append(columnSql.substring(0, columnSql.length())).append(") values (");
        sql.append(valuesSql.substring(0, valuesSql.length())).append(")");
        return sql.toString();
    }

    public static <T> List<Map<String, Object>> getFiledsInfo(T t) {
        List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
        Map<String, Object> filedMap = null;
        Field[] fields = t.getClass().getDeclaredFields();
        String entityName = t.getClass().getSimpleName();
        for (int i = 0; i < fields.length; i++) {
            filedMap = new HashMap<String, Object>();

            filedMap.put("obj_name", entityName);
            filedMap.put("f_type", fields[i].getType().toString());
            filedMap.put("f_name", fields[i].getName());
            filedMap.put("f_value", BeanUtils.getFieldValue(t, fields[i].getName()));
            fieldList.add(filedMap);
        }
        return fieldList;
    }
    
    public static synchronized String getRowId(String id){
    	IdWorker worker = IDWORKER_MAP.get(id);
    	if(null == worker){
    		worker = new IdWorker(IDWORKER_MAP.size() + 1);
    		IDWORKER_MAP.put(id, worker);
    	}
    	return id + worker.nextId();
    }
    
    public static void main(String[] args) {
    	for (int i = 0; i < 20; i++) {
    		System.out.println(CodeUtil.getRowId("DX"));
		}
	}

}
