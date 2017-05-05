package ydh.user.entity;

import ydh.cicada.api.Column;
import ydh.cicada.api.Entity;
import ydh.cicada.api.Id;

@Entity(name = "MENUS")
public class Menu {
	/** 菜单ID*/
	@Id(autoincrement = true)
	@Column(name = "MENU_ID")
	private String menuId;

	/** 权限代码*/
	@Column(name = "PERMISSION_CODE")
	private String permissionCode;

	/** 菜单名称*/
	@Column(name = "MENU_NAME")
	private String menuName;

	/** 父菜单名*/
	@Column(name = "PARENT_MENU_ID")
	private String parentMenuId;

	/** 排序*/
	@Column(name = "SORT")
	private Integer sort;

	/** 菜单对应URL地址*/
	@Column(name = "URL")
	private String url;

	/** 菜单所属系统*/
	@Column(name = "SYS_TYPE")
	private String sysType;

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

}
