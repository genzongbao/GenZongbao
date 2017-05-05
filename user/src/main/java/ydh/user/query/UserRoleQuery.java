package ydh.user.query;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryParam;

@Query(from = "USER_ROLE")
public class UserRoleQuery extends PagableQueryCmd{
	
	@QueryParam(fieldName = "USER_ID")
	private Integer userId;
	
	@QueryParam(fieldName = "ROLE_ID")
	private Integer roleId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
