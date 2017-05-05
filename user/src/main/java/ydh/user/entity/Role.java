package ydh.user.entity;

import java.util.Date;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "ROLES")
public class Role {
	
	/** 角色ID*/
	@Id(autoincrement = true)
	@Column(name = "ROLE_ID")
	private Integer roleId;

	/** 角色名*/
	@Column(name = "ROLE_NAME")
	private String roleName;
	
	@Column(name = "ROLE_CODE")
	private String roleCode;

	/** 角色描述*/
	@Column(name = "ROLE_DESC")
	private String roleDesc;

	/** 创建日期*/
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	/** 权限字符串 */
	private String permissions;

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

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

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
