package ydh.user.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

/**
 * @author chenkailun
 */
@Entity(name = "ROLE_PERMISSION")
public class RolePermission {
	
	@Id
	@Column(name = "ROLE_ID")
	private Integer roleId;
	public static final String ROLE_ID = "ROLE_ID";
	
	@Column(name = "PERMISSION_CODES")
	private String permissionCodes;
	public static final String PERMISSION_CODE = "PERMISSION_CODES";

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getPermissionCodes() {
		return permissionCodes;
	}

	public void setPermissionCodes(String permissionCodes) {
		this.permissionCodes = permissionCodes;
	}

}
