package ydh.config.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "CONFIG")
public class Config {
	
	/** 配置项ID*/
	public static final String CONFIG_ID = "CONFIG_ID";
	@Id
	@Column(name = "CONFIG_ID")
	private String configId;

	/** 配置项值*/
	public static final String CONFIG_VALUE = "CONFIG_VALUE";
	@Column(name = "CONFIG_VALUE")
	private String configValue;

	/** 配置项的描述*/
	public static final String CONFIG_DESCRIPTION = "CONFIG_DESCRIPTION";
	@Column(name = "CONFIG_DESCRIPTION")
	private String configDescription;

	/** 正则表达式*/
	public static final String CONFIG_REGULAR = "CONFIG_REGULAR";
	@Column(name = "CONFIG_REGULAR")
	private String configRegular;

	/** 输入错误提示*/
	public static final String CONFIG_HINT = "CONFIG_HINT";
	@Column(name = "CONFIG_HINT")
	private String configHint;
	
	public String getConfigRegular() {
		return configRegular;
	}

	public void setConfigRegular(String configRegular) {
		this.configRegular = configRegular;
	}

	public String getConfigHint() {
		return configHint;
	}

	public void setConfigHint(String configHint) {
		this.configHint = configHint;
	}


	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigDescription() {
		return configDescription;
	}

	public void setConfigDescription(String configDescription) {
		this.configDescription = configDescription;
	}
	
}
