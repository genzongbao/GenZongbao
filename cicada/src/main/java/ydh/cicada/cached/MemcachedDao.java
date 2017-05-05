package ydh.cicada.cached;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import ydh.cicada.dao.SplitableDao;
import ydh.cicada.utils.EntityUtils;

import com.whalin.MemCached.MemCachedClient;

public class MemcachedDao extends AbstractCachedDao {
	protected MemCachedClient client;

	public MemcachedDao(SplitableDao dao, MemCachedClient client) {
		super(dao);
		this.client = client;
	}

	public Object cacheGet(String key) {
		return client.get(key);
	}

	public void cacheDelete(String key) {
		client.delete(key);
	}

	public void cacheSet(String key, Object entity, int expiry, TimeUnit unit) {
		if (expiry == 0) {
			long timeTicket = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(expiry, unit);
			client.set(EntityUtils.keyOf(entity), entity, new Date(timeTicket));
		} else {
			client.set(EntityUtils.keyOf(entity), entity);
		}
	}

}
