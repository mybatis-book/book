package tk.mybatis.springboot.service;

import java.util.List;

import tk.mybatis.simple.model.SysUser;

public interface UserService {
	
	/**
	 * 通过 id 查询用户
	 * 
	 * @param id
	 * @return
	 */
	SysUser findById(Long id);
	
	/**
	 * 查询全部用户
	 * 
	 * @return
	 */
	List<SysUser> findAll();
}
