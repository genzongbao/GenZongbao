package ydh.config.perm;

import ydh.base.perm.Perm;
import ydh.base.perm.PermOperator;
import ydh.base.perm.PermResource;

@Perm(type="sysConfig", name = "系统参数")
public class SysConfigPerm {
	
	@PermResource(name="系统参数设置")
	public static final class SysConfig {
		@PermOperator(name = "列表")
		public static final String list = "SysConfig:list";
		@PermOperator(name = "修改")
		public static final String update = "SysConfig:update";
	}
}
