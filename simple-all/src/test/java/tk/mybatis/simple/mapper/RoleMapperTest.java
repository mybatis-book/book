package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.model.SysRole;
import tk.mybatis.simple.plugin.PageRowBounds;
import tk.mybatis.simple.type.Enabled;

public class RoleMapperTest extends BaseMapperTest {

	@Test
	public void testSelectById(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 RoleMapper 接口
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//调用 selectById 方法，查询 id = 1 的角色
			SysRole role = roleMapper.selectById(1l);
			//role 不为空
			Assert.assertNotNull(role);
			//roleName = 管理员
			Assert.assertEquals("管理员", role.getRoleName());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectById2(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 RoleMapper 接口
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//调用 selectById 方法，查询 id = 1 的角色
			SysRole role = roleMapper.selectById2(1l);
			//role 不为空
			Assert.assertNotNull(role);
			//roleName = 管理员
			Assert.assertEquals("管理员", role.getRoleName());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectAll(){
		SqlSession sqlSession = getSqlSession();
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//调用 selectAll 方法查询所有角色
			List<SysRole> roleList = roleMapper.selectAll();
			//结果不为空
			Assert.assertNotNull(roleList);
			//角色数量大于 0 个
			Assert.assertTrue(roleList.size() > 0);
			//验证下划线字段是否映射成功
			Assert.assertNotNull(roleList.get(0).getRoleName());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectAllRoleAndPrivileges(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 RoleMapper 接口
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			List<SysRole> roleList = roleMapper.selectAllRoleAndPrivileges();
			for(SysRole role: roleList){
				System.out.println("角色名：" + role.getRoleName());
				for(SysPrivilege privilege : role.getPrivilegeList()){
					System.out.println("权限名：" + privilege.getPrivilegeName());
				}
			}
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectRoleByUserIdChoose(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 RoleMapper 接口
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//由于数据库数据 enable 都是 1，所以我们给其中一个角色的 enable 赋值为 0
			SysRole role = roleMapper.selectById(2L);
			role.setEnabled(Enabled.disabled);
			roleMapper.updateById(role);
			//获取用户 1 的角色
			List<SysRole> roleList = roleMapper.selectRoleByUserIdChoose(1L);
			for(SysRole r: roleList){
				System.out.println("角色名：" + r.getRoleName());
				if(r.getId().equals(1L)){
					//第一个角色存在权限信息
					Assert.assertNotNull(r.getPrivilegeList());
				} else if(r.getId().equals(2L)){
					//第二个角色的权限为 null
					Assert.assertNull(r.getPrivilegeList());
					continue;
				}
				for(SysPrivilege privilege : r.getPrivilegeList()){
					System.out.println("权限名：" + privilege.getPrivilegeName());
				}
			}
		} finally {
			sqlSession.rollback();
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testUpdateById(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 RoleMapper 接口
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
			//由于数据库数据 enable 都是 1，所以我们给其中一个角色的 enable 赋值为 0
			SysRole role = roleMapper.selectById(2L);
			Assert.assertEquals(Enabled.enabled, role.getEnabled());
			role.setEnabled(Enabled.disabled);
			roleMapper.updateById(role);
		} finally {
			sqlSession.rollback();
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectAllByRowBounds(){
		SqlSession sqlSession = getSqlSession();
		try {
			RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
	        //查询前两个，使用 RowBounds 类型不会查询总数
			RowBounds rowBounds = new RowBounds(0, 1);
			List<SysRole> list = roleMapper.selectAll(rowBounds);
			for(SysRole role : list){
				System.out.println("角色名：" + role.getRoleName());
			}
	        //使用 PageRowBounds 会查询总数
			PageRowBounds pageRowBounds = new PageRowBounds(0, 1);
			list = roleMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysRole role : list){
				System.out.println("角色名：" + role.getRoleName());
			}
			//再次查询
			pageRowBounds = new PageRowBounds(1, 1);
			list = roleMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysRole role : list){
				System.out.println("角色名：" + role.getRoleName());
			}
		} finally {
			sqlSession.close();
		}
	}
	
}
