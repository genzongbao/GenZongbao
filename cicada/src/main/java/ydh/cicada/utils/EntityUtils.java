package ydh.cicada.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;
import ydh.cicada.api.Split;
import ydh.cicada.dao.SplitStrategy;

public class EntityUtils {
	/**
	 * 把结果集中的数据填充到对象中
	 * 
	 * @param rs
	 * @param obj
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public static void fillEntity(ResultSet rs, Object obj) 
			throws SQLException, IllegalAccessException, InvocationTargetException {	
		/**
		 * 想办法知道查询的结果里有多少列,列名是什么
		 * 返回结果集的元数据, 元数据中包含有查询得到的
		 */
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			//获得列名,列的类型等信息
			String columnName = rsmd.getColumnName(i);
			Object columnValue = rs.getObject(columnName);
			//将获得的列名转化为对象对应的成员变量名   注意数据库列名和类对象成员变量的对应关系
			String propertyName = toCamel(columnName);
			if (columnValue != null) {
				//给成员变量赋值
				BeanUtils.setProperty(obj, propertyName, columnValue);
			}
		}
	}

	/**
	 * 把下划线的命名变成驼峰命名方式
	 * 
	 * @param name
	 * @return
	 */
	public static String toCamel(String name) {
		name = name.toLowerCase();
		StringBuilder camel = new StringBuilder(name.length());
		String[] arr = name.split("_");
		camel.append(arr[0]);
		if (arr.length > 1) {
			for (int i = 1; i < arr.length; i++) {
				// 将首字母大写
				camel.append(StringUtils.capitalize(arr[i]));
			}
		}
		return camel.toString();
	}
	
	public static String toUnderscore(String name) {
		StringBuilder underscore = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				underscore.append('_').append(c);
			} else {
				underscore.append(Character.toUpperCase(c));
			}
		}
		return underscore.toString();
	}

	public static String keyOf(Class<?> entityClass, Serializable id) {
		Entity entity = entityClass.getAnnotation(Entity.class);
		String key = entity.name() + ":";
		if (id.getClass().isArray()) {
			Serializable[] ids = (Serializable[])id;
			key += ids[0];
			for (int i = 1; i < ids.length; i++) {
				key += "/";
				key += ids[i];
			}
		} else {
			key += id;
		}
		return key;
	}
	
	public static String keyOf(Object obj) {
		return keyOf(obj.getClass(), idOf(obj));
	}
	
	public static Serializable idOf(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Serializable theId = null;
			Serializable[] ids = null;
			for (Field field : fields) {
				Id id = field.getAnnotation(Id.class);
				if (id != null) {
					if (theId == null) {
						theId = BeanUtils.getProperty(obj, field.getName());
						if (theId == null) return null;
					} else if (ids == null) {
						ids = new Serializable[2];
						ids[0] = theId;
						ids[1] = BeanUtils.getProperty(obj, field.getName());
						if (ids[1] == null) return null;
						theId = ids;
					} else {
						Serializable[] newIds = new Serializable[ids.length + 1];
						System.arraycopy(ids, 0, newIds, 0, ids.length);
						newIds[ids.length] = BeanUtils.getProperty(obj, field.getName());
						if (newIds[ids.length] == null) return null;
						ids = newIds;
						theId = ids;
					}
				}
			}
			return theId;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isAutoincrement(Class<?> entityClass) {
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			Id idAnn = field.getAnnotation(Id.class);
			if (idAnn != null) {
				return idAnn.autoincrement();
			}
		}
		return false;
	}
	
	public static void setId(Object entity, Serializable id) {
		try {
			Field[] fields = entity.getClass().getDeclaredFields();
			Field idField = null;
			Field[] idFields = null;
			for (Field field : fields) {
				Id idAnn = field.getAnnotation(Id.class);
				if (idAnn != null) {
					if (idField == null) {
						idField = field;
					} else if (idFields == null) {
						idFields = new Field[2];
						idFields[0] = idField;
						idFields[1] = field;
					} else {
						Field[] newIdFields = new Field[idFields.length + 1];
						System.arraycopy(idFields, 0, newIdFields, 0, idFields.length);
						newIdFields[idFields.length] = field;
						idFields = newIdFields;
					}
				}
			}
			if (idFields != null) {
				if (! id.getClass().isArray()) {
					throw new IllegalArgumentException("Composite primary key count is not correct");
				}
				if (((Serializable[])id).length != idFields.length) {
					throw new IllegalArgumentException("Composite primary key count is not correct");
				}
				for (int i = 0; i < idFields.length; i++) {
					BeanUtils.setProperty(entity, idFields[i].getName(), ((Serializable[])id)[i]);
				}
			} else if (idField != null) {
				BeanUtils.setProperty(entity, idField.getName(), id);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 取实体对象的字段值
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static Object getProperty(Object bean, String fieldName) {
		try {
			return PropertyUtils.getProperty(bean, fieldName);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public static void copyProperties(Object dest, Object orig) {
		try {
			PropertyUtils.copyProperties(dest, orig);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Object strip(Object param) {
		if (param != null && param instanceof Enum) {
			return ((Enum<?>)param).ordinal();
		} else {
			return param;
		}
	}

	@SuppressWarnings("rawtypes")
	private static Map<Class,SplitStrategy> splitStrategyMap = new HashMap<Class,SplitStrategy>();
	
	@SuppressWarnings("unchecked")
	public static <T> SplitStrategy<T> getSplitStrategy(Class<T> entityClass) {
		SplitStrategy<T> strategy = splitStrategyMap.get(entityClass);
		if (strategy == null) {
			try {
				Split split = entityClass.getAnnotation(Split.class);
				if (split != null) {
					strategy = split.value().newInstance();
					splitStrategyMap.put(entityClass, strategy);
				}
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return strategy;
	}

	public static String tablename(Class<?> entityClass, String entityHashcode) {
		Entity entity  = entityClass.getAnnotation(Entity.class);
		return tablename(entity, entityHashcode);
	}
	
	public static String tablename(Entity entity, String entityHashcode) {
		if (entityHashcode == null) {
			return entity.name();
		} else {
			return entity.name() + "_" + entityHashcode;
		}
	}
	
	public static <T> String tablename(T obj) {
		Entity entity  = obj.getClass().getAnnotation(Entity.class);
		@SuppressWarnings("unchecked")
		SplitStrategy<T> strategy = getSplitStrategy((Class<T>)obj.getClass());
		if (strategy != null) {
			return tablename(entity, strategy.hashcode(obj));
		} else {
			return entity.name();
		}
	}
	
}