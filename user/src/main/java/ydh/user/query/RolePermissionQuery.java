package ydh.user.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;

/**
 * 
 * @author chenkailun
 *
 */
@Query(from = "ROLE_PERMISSION")
public class RolePermissionQuery extends PagableQueryCmd{

	@QueryParam(fieldName = "ROLE_ID")
	private Integer roleId;
	
	@QueryParam(fieldName = "PERMISSION_CODES")
	private String permissionCodes;

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
