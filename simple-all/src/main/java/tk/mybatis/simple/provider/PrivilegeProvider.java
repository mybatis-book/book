package tk.mybatis.simple.provider;

import org.apache.ibatis.jdbc.SQL;

import tk.mybatis.simple.model.SysPrivilege;

/**
 * 权限Mapper对应的Provider实现
 */
public class PrivilegeProvider {
	
	public String selectById(final Long id){
		return new SQL(){
			{
				SELECT("id, privilege_name, privilege_url");
				FROM("sys_privilege");
				WHERE("id = #{id}");
			}
		}.toString();
	}
	
	public String selectByPrivilege(final SysPrivilege privilege){
		return new SQL(){
			{
				SELECT("id, privilege_name, privilege_url");
				FROM("sys_privilege");
				//参数不为空的时候才能使用这些条件进行查询，参数 null 时查询所有
				if(privilege != null){
					if(privilege.getId() != null){
						WHERE("id = #{id}");
					}
					if(privilege.getPrivilegeName() != null && privilege.getPrivilegeName().length() > 0){
						//注意 MySql 中的 concat 函数
						WHERE("privilege_name like concat('%',#{privilegeName},'%')");
					}
					if(privilege.getPrivilegeUrl() != null && privilege.getPrivilegeUrl().length() > 0){
						//这里为了举例，直接拼接的字符串
						WHERE("privilege_url like '%" +privilege.getPrivilegeUrl()+ "%'");
					}
				}
			}
		}.toString();
	}
	
	public String selectAll(){
		return "select * from sys_privilege";
	}
	
	public String insert(final SysPrivilege sysPrivilege){
		return new SQL(){
			{
				INSERT_INTO("sys_privilege");
				VALUES("privilege_name", "#{privilegeName}");
				VALUES("privilege_url", "#{privilegeUrl}");
			}
		}.toString();
	}
	
	public String updateById(final SysPrivilege sysPrivilege){
		return new SQL(){
			{
				UPDATE("sys_privilege");
				SET("privilege_name = #{privilegeName}");
				SET("privilege_url = #{privilegeUrl}");
				WHERE("id = #{id}");
			}
		}.toString();
	}
	
	public String deleteById(final Long id){
		return new SQL(){
			{
				DELETE_FROM("sys_privilege");
				WHERE("id = #{id}");
			}
		}.toString();
	}
}
