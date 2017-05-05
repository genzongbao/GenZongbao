package ydh.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ydh.cicada.dao.JdbcDao;
import ydh.cicada.query.Deleter;
import ydh.cicada.query.QueryObject;
import ydh.user.entity.Role;
import ydh.user.entity.RolePermission;
import ydh.user.entity.UserRole;
import ydh.user.query.RolePermissionQuery;
import ydh.user.query.UserRoleQuery;

/**
 * 
 * @author chenkailun
 *
 */
@Service
public class SecurityService {
	
	@Autowired
	JdbcDao jdbcDao;

	/**
	 * 对用户编辑角色
	 * @param userId
	 * @param roleIds
	 */
	@Transactional
	public void editUsersRole(Integer userId, String roleIds){
		//删除原有的关联关系
		UserRoleQuery userRoleQuery = new UserRoleQuery();
		userRoleQuery.setUserId(userId);
		List<UserRole> userRoleList = jdbcDao.list(UserRole.class, userRoleQuery);
		for (UserRole userRole : userRoleList) {
			jdbcDao.delete(userRole);
		}
		//创建新的关联关系
		//如果角色id字符串 为空（没有任何操作权限） 即不进行操作
		if(roleIds == null){
		}else{
		//前端用逗号分隔
			String[] roleIdList = roleIds.split(",");
			for (String roleId : roleIdList) {
				UserRole userRole = new UserRole();
				userRole.setUserId(userId);
				userRole.setRoleId(Integer.valueOf(roleId));
				jdbcDao.insert(userRole);
			}
		}
	}
	
	/**
	 * 修改角色的权限
	 * @param roleId      角色ID
	 * @param permissions 权限字符串
	 */
	@Transactional
	public void updateRolePermissions(Integer roleId, String[] permissions){
		// 删除原角色的权限
		Deleter.delete(RolePermission.class)
			.cond(RolePermission.ROLE_ID).equ(roleId)
			.exec(jdbcDao);
		
		//创建新的角色权限关联关系
		for (String permissionCode : permissions) {
			RolePermission permission = new RolePermission();
			permission.setRoleId(roleId);
			permission.setPermissionCodes(permissionCode);
			jdbcDao.insert(permission);
		}
	}
	public Role selectRoleByroleName(String roleName){
		return QueryObject.select(Role.class)
				.condition("ROLE_NAME=?", roleName)
				.firstResult(jdbcDao);
				
	}
	public Role selectRoleByIdAndroleName(Integer roleId,String roleName){
		return QueryObject.select(Role.class)
				.condition("ROLE_ID=?", roleId)
				.condition("ROLE_NAME=?", roleName)
				.firstResult(jdbcDao);
	}
	
	/**
	 * 角色删除
	 * @param roleId
	 */
	@Transactional
	public void removeRole(Integer roleId) {
		//删除与操作权限的关联
		RolePermissionQuery query = new RolePermissionQuery();
		query.setRoleId(roleId);
		List<RolePermission> rpList = jdbcDao.list(RolePermission.class, query);
		for (RolePermission rolePermission : rpList) {
			jdbcDao.delete(rolePermission);
		}
		//删除与用户的关联
		UserRoleQuery userRoleQuery = new UserRoleQuery();
		userRoleQuery.setRoleId(roleId);
		List<UserRole> urList = jdbcDao.list(UserRole.class, userRoleQuery);
		for (UserRole userRole : urList) {
			jdbcDao.delete(userRole);
		}
		//删除角色
		jdbcDao.delete(Role.class,roleId);
	}
}
