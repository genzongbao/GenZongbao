package ydh.cicada.dao;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import ydh.cicada.utils.EntityUtils;

public class CicadaRowMapper<T> extends BeanPropertyRowMapper<T> {
	private RowMapper<T> rowMapper;
	
	@SuppressWarnings("unchecked")
	public CicadaRowMapper(Class<T> type) {
		super();
		super.setMappedClass(type);
		if (type == Map.class) {
			this.rowMapper = (RowMapper<T>) new ColumnMapRowMapper() {
				protected String getColumnKey(String columnName) {
					return EntityUtils.toCamel(columnName);
				}
			};
		} else if (isSingleColumn(type)) {
			this.rowMapper = SingleColumnRowMapper.newInstance(type);
		}
	}

	private boolean isSingleColumn(Class<?> entityClass) {
		final Class<?>[] SINGLE_COLUMN_TYPES = {String.class, Long.class, long.class,
				Integer.class, int.class, Short.class, short.class, Byte.class, byte.class,
				Double.class, double.class, Float.class, float.class, 
				BigDecimal.class, Date.class};
		return ArrayUtils.contains(SINGLE_COLUMN_TYPES, entityClass);
	}
	
	@Override  
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initBeanWrapper(BeanWrapper bw) {  
        super.initBeanWrapper(bw);
        for (PropertyDescriptor prop : bw.getPropertyDescriptors()) {
        	Class<?> type = prop.getPropertyType(); 
        	if (type.isEnum()) {
        		bw.registerCustomEditor(type, new EnumEditor((Class<? extends Enum<?>>)type)); 
        	}
        }
    }
	
	@Override
	public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
		if (this.rowMapper != null) {
			return rowMapper.mapRow(rs, rowNumber);
		} else {
			return super.mapRow(rs, rowNumber);
		}
	}
	
	public static class EnumEditor<T extends Enum<T>> extends PropertyEditorSupport {
		
		private Class<T> enumType;
		
		public EnumEditor(Class<T> enumType) {
			this.enumType = enumType;
		}
		
		@Override
	    public void setAsText(final String text) throws IllegalArgumentException {
			if (StringUtils.isEmpty(text)) {
				setValue(null);
			} else {
				T e = Enum.valueOf(enumType, text);
				setValue(e);
			}
	    }

	    @Override  
	    public void setValue(final Object value) {
	    	if (value == null) {
	    		super.setValue(value);
	    	} else if (value instanceof Number) {
	    		T e = null;
	    		int v = ((Number)value).intValue();
	    		for (T t : enumType.getEnumConstants()) {
	    			if (v == t.ordinal()) {
	    				e = t;
	    				break;
	    			}
	    		}
	    		if (e != null) {
		    		super.setValue(e);
	    		} else {
	    			throw new IllegalArgumentException("Unknow enum ("+enumType+") value:" + value);
	    		}
	    	} else if (value instanceof String) {
	    		this.setAsText((String)value);
	    	} else {
	    		throw new IllegalArgumentException("Unknow value type:" + value);
	    	}
	    }  

		@Override  
	    public Object getValue() {
			return super.getValue();
	    }  

	    @Override  
	    public String getAsText() {  
	        return getValue().toString();
	    }  
	}
}
