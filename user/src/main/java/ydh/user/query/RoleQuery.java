package ydh.user.query;

import java.util.Date;

import ydh.cicada.query.PagableQueryCmd;
import ydh.cicada.query.api.Query;
import ydh.cicada.query.api.QueryOperator;
import ydh.cicada.query.api.QueryParam;

@Query(from = "ROLES", orderBy = "ROLES.ROLE_ID ASC")
public class RoleQuery extends PagableQueryCmd{
	
	/** 角色ID*/
	@QueryParam(fieldName = "ROLE_ID")
	private Integer roleId;

	/** 角色名*/
	@QueryParam(fieldName = "ROLE_NAME", op = QueryOperator.LIKE)
	private String roleName;

	/** 创建日期（大于）*/
	@QueryParam(fieldName = "CREATE_DATE", op = QueryOperator.GT_EQU)
	private Date minCreateDate;
	
	/** 创建日期（小于）*/
	@QueryParam(fieldName = "CREATE_DATE", op = QueryOperator.LESS_EQU)
	private Date maxCreateDate;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getMinCreateDate() {
		return minCreateDate;
	}

	public void setMinCreateDate(Date minCreateDate) {
		this.minCreateDate = minCreateDate;
	}

	public Date getMaxCreateDate() {
		return maxCreateDate;
	}

	public void setMaxCreateDate(Date maxCreateDate) {
		this.maxCreateDate = maxCreateDate;
	}
	
}
