package tk.mybatis.simple.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.model.SysUser;

public interface UserMapper {
	
	/**
	 * 通过 id 查询用户
	 * 
	 * @param id
	 * @return
	 */
	SysUser selectById(Long id);

	/**
	 * 查询全部用户
	 * 
	 * @return
	 */
	List<SysUser> selectAll();
	
	/**
	 * 根据用户 id 获取角色信息
	 * 
	 * @param userId
	 * @return
	 */
	List<SysRole> selectRolesByUserId(Long userId);
	
	/**
	 * 根据用户 id 和 角色的 enabled 状态获取用户的角色
	 * 
	 * @param userId
	 * @param enabled
	 * @return
	 */
	List<SysRole> selectRolesByUserIdAndRoleEnabled(@Param("userId")Long userId, @Param("enabled")Integer enabled);
	
	/**
	 * 根据用户 id 和 角色的 enabled 状态获取用户的角色
	 * 
	 * @param user
	 * @param role
	 * @return
	 */
	List<SysRole> selectRolesByUserAndRole(@Param("user")SysUser user, @Param("role")SysRole role);
	
	/**
	 * 新增用户
	 * 
	 * @param sysUser
	 * @return
	 */
	int insert(SysUser sysUser);
	
	/**
	 * 新增用户 - 使用 useGeneratedKeys 方式
	 * 
	 * @param sysUser
	 * @return
	 */
	int insert2(SysUser sysUser);
	
	/**
	 * 新增用户 - 使用 selectKey 方式
	 * 
	 * @param sysUser
	 * @return
	 */
	int insert3(SysUser sysUser);
	
	/**
	 * 根据主键更新
	 * 
	 * @param sysUser
	 * @return
	 */
	int updateById(SysUser sysUser);
	
	/**
	 * 通过主键删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Long id);
	
	/**
	 * 通过主键删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(SysUser sysUser);
	
	/**
	 * 根据动态条件查询用户信息
	 * 
	 * @param sysUser
	 * @return
	 */
	List<SysUser> selectByUser(SysUser sysUser);
	
	/**
	 * 根据主键更新
	 * 
	 * @param sysUser
	 * @return
	 */
	int updateByIdSelective(SysUser sysUser);
	
	/**
	 * 根据用户 id 或用户名查询
	 * 
	 * @param sysUser
	 * @return
	 */
	SysUser selectByIdOrUserName(SysUser sysUser);
	
	/**
	 * 根据用户 id 集合查询
	 * 
	 * @param idList
	 * @return
	 */
	List<SysUser> selectByIdList(List<Long> idList);
	
	/**
	 * 批量插入用户信息
	 * 
	 * @param userList
	 * @return
	 */
	int insertList(List<SysUser> userList);
	
	/**
	 * 通过 Map 更新列
	 * 
	 * @param map
	 * @return
	 */
	int updateByMap(Map<String, Object> map);
	
	/**
	 * 根据用户 id 获取用户信息和用户的角色信息
	 * 
	 * @param id
	 * @return
	 */
	SysUser selectUserAndRoleById(Long id);
	
	/**
	 * 根据用户 id 获取用户信息和用户的角色信息
	 * 
	 * @param id
	 * @return
	 */
	SysUser selectUserAndRoleById2(Long id);
	
	/**
	 * 根据用户 id 获取用户信息和用户的角色信息，嵌套查询方式
	 * 
	 * @param id
	 * @return
	 */
	SysUser selectUserAndRoleByIdSelect(Long id);
	
	/**
	 * 获取所有的用户以及对应的所有角色
	 * 
	 * @return
	 */
	List<SysUser> selectAllUserAndRoles();
	
	/**
	 * 通过嵌套查询获取指定用户的信息，以及用户的角色和权限信息
	 * 
	 * @param id
	 * @return
	 */
	SysUser selectAllUserAndRolesSelect(Long id);
		
	/**
	 * 使用存储过程查询用户信息
	 * 
	 * @param user
	 * @return
	 */
	void selectUserById(SysUser user);
	
	/**
	 * 使用存储过程分页查询
	 * 
	 * @param userName
	 * @param pageNum
	 * @param pageSize
	 * @param total
	 * @return
	 */
	List<SysUser> selectUserPage(Map<String, Object> params);
	
	/**
	 * 保存用户信息和角色关联信息
	 * 
	 * @param user
	 * @param roleIds
	 * @return
	 */
	int insertUserAndRoles(@Param("user")SysUser user, @Param("roleIds")String roleIds);
	
	/**
	 * 根据用户 id 删除用户和用户的角色信息
	 * 
	 * @param id
	 * @return
	 */
	int deleteUserById(Long id);
}
