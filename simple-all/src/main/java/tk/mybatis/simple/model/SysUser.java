package tk.mybatis.simple.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 */
public class SysUser implements Serializable {
	private static final long serialVersionUID = -328602757171077630L;
	/**
	 * 用户ID
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 密码
	 */
	private String userPassword;
	/**
	 * 邮箱
	 */
	private String userEmail;
	/**
	 * 简介
	 */
	private String userInfo;
	/**
	 * 头像
	 */
	private byte[] headImg;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 用户角色
	 */
	private SysRole role;
	
	/**
	 * 用户的角色集合
	 */
	private List<SysRole> roleList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public byte[] getHeadImg() {
		return headImg;
	}

	public void setHeadImg(byte[] headImg) {
		this.headImg = headImg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public List<SysRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}

}
