package ydh.utils;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static String toString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T extends Map<String, Object>> T buildModel(T model, boolean success, Object data, String message) {
		setModelAttribute(model, "success", success);
		setModelAttribute(model, "data", data);
		setModelAttribute(model, "message", message);
		return model;
	}
	
	/**
	 * 向map中添加属性
	 * @param model  map
	 * @param key    属性名
	 * @param value  属性值（null表示不添加此属性）
	 * @return  原值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T setModelAttribute(Map<String, Object> model, String key, T value) {
		if (value == null) {
			return (T)model.remove(key);
		} else {
			return (T)model.put(key, value);
		}
	}
}
