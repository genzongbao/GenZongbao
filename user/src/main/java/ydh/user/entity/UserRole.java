package ydh.user.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "USER_ROLE")
public class UserRole {
	
	@Id
	@Column(name = "USER_ID")
	private Integer userId;
	
	@Id
	@Column(name = "ROLE_ID")
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
