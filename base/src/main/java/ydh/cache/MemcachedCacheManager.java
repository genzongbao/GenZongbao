package ydh.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.whalin.MemCached.MemCachedClient;

public class MemcachedCacheManager {

	public MemcachedCacheManager() {

	}

	private MemCachedClient memCachedClient;

	public void setMemCachedClient(MemCachedClient memCachedClient) {
		this.memCachedClient = memCachedClient;
	}

	public MemCachedClient getMemCachedClient() {
		return memCachedClient;
	}

	public void put(String key, Object obj) {
		memCachedClient.set(key, obj, 7200);
	}

	public void set(String key, Object value, int expiry) {
		if (StringUtils.isEmpty(key) || value == null) {
			return;
		} else {
			memCachedClient.set(key, value, expiry * 60);
			return;
		}
	}

	public Object get(String key) {
		if (StringUtils.isEmpty(key))
			return Boolean.valueOf(false);
		Object o;
		try {
			o = memCachedClient.get(key);
		} catch (Exception e) {
			o = memCachedClient.get(key);
		}
		return o;
	}

	public Map<String, Object> getMulti(List<Object> keys) {
		if (keys == null || keys.size() == 0) {
			return new HashMap<String, Object>(0);
		} else {
			String strKeys[] = new String[keys.size()];
			strKeys = (String[]) keys.toArray(strKeys);
			return memCachedClient.getMulti(strKeys);
		}
	}

	public Object[] getMulti(String keys[]) {
		if (keys == null || keys.length == 0) {
			return new Object[0];
		} else {
			Map<String, Object> map = memCachedClient.getMulti(keys);
			return map.values().toArray();
		}
	}

	public void delete(String key) {
		if (StringUtils.isEmpty(key)) {
			return;
		} else {
			memCachedClient.delete(key);
			return;
		}
	}

	public boolean exists(String key) {
		if (StringUtils.isEmpty(key))
			return false;
		else
			return memCachedClient.get(key) != null;
	}

	public boolean keyExists(String key) {
		return memCachedClient.keyExists(key);
	}

	public boolean add(String key, Object value, Date expiry) {
		return memCachedClient.add(key, value, expiry);
	}

	public boolean replace(String key, Object value) {
		return memCachedClient.replace(key, value);
	}

	public boolean add(String key, Object value) {
		return memCachedClient.add(key, value);
	}

	public boolean replace(String key, Object value, Date expiry) {
		return memCachedClient.replace(key, value, expiry);
	}

}
