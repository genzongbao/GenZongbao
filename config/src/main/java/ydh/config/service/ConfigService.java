package ydh.config.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.QueryObject;
import ydh.config.entity.Config;

@Service
public class ConfigService {
	@Autowired
	private JdbcDao jdbcDao;
	
	@Transactional
	public String queryConfigValue(String configId) {
		Config config = QueryObject.select(Config.class)
			.cond(Config.CONFIG_ID).equ(configId)
			.firstResult(jdbcDao);
		return config == null ? null : config.getConfigValue();
	}
	
	@Transactional
	public List<Config> listConfigs(){
		List<Config> configs = QueryObject.select(Config.class)
				   .list(jdbcDao);
		return configs;
	}

}
