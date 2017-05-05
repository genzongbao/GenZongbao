package ydh.user.perm;

import ydh.base.perm.Perm;
import ydh.base.perm.PermOperator;
import ydh.base.perm.PermResource;

@Perm(type="security", name = "权限管理")
public final class SecurityPerm {
	
	@PermResource(name="权限系统")
	public static final class SecuritySys {
		@PermOperator(name = "系统")
		public static final String list = "security";
	}
	
	@PermResource(name="用户管理")
	public static final class User {
		@PermOperator(name = "列表")
		public static final String list = "User:list";
		@PermOperator(name = "分配角色")
		public static final String setRole = "User:setRole";
		@PermOperator(name = "冻结")
		public static final String edit_roles = "User:freeze";
		@PermOperator(name = "解冻")
		public static final String unfreeze = "User:unfreeze";
		@PermOperator(name = "注销")
		public static final String cancellation = "User:cancellation";
	}

	@PermResource(name="角色管理")
	public static final class Role {
		@PermOperator(name = "列表")
		public static final String list = "Role:list";
		@PermOperator(name = "添加")
		public static final String add = "Role:add";
		@PermOperator(name = "修改")
		public static final String amend = "Role:amend";
		@PermOperator(name = "授权")
		public static final String editPermission = "Role:editPermission";
		@PermOperator(name = "删除")
		public static final String delete = "Role:delete";
	}
}
