package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.plugin.PageRowBounds;

public class PrivilegeMapperTest extends BaseMapperTest {

	@Test
	public void testSelectById(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 PrivilegeMapper 接口
			PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);
			//调用 selectById 方法，查询 id = 1 的权限
			SysPrivilege privilege = privilegeMapper.selectById(1l);
			//privilege 不为空
			Assert.assertNotNull(privilege);
			//privilegeName = 管理员
			Assert.assertEquals("用户管理", privilege.getPrivilegeName());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectByPrivilege(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 PrivilegeMapper 接口
			PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);
			//查询权限名称包含“维护”二字的数据
			SysPrivilege query = new SysPrivilege();
			query.setPrivilegeName("维护");
			List<SysPrivilege> privilegeList = privilegeMapper.selectByPrivilege(query);
			//两个结果
			Assert.assertEquals(2, privilegeList.size());
			//查询权限地址中包含“es”两个字母的数据
			query = new SysPrivilege();
			query.setPrivilegeUrl("es");
			privilegeList = privilegeMapper.selectByPrivilege(query);
			//两个结果
			Assert.assertEquals(2, privilegeList.size());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectAllByRowBounds(){
		SqlSession sqlSession = getSqlSession();
		try {
			PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);
	        //查询前两个，使用 RowBounds 类型不会查询总数
			RowBounds rowBounds = new RowBounds(0, 2);
			List<SysPrivilege> list = privilegeMapper.selectAll(rowBounds);
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
	        //使用 PageRowBounds 会查询总数
			PageRowBounds pageRowBounds = new PageRowBounds(2, 2);
			list = privilegeMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
			//再次查询
			pageRowBounds = new PageRowBounds(4, 2);
			list = privilegeMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
		} finally {
			sqlSession.close();
		}
		sqlSession = getSqlSession();
		try {
			PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);
			//查询前两个，使用 RowBounds 类型不会查询总数
			RowBounds rowBounds = new RowBounds(0, 2);
			List<SysPrivilege> list = privilegeMapper.selectAll(rowBounds);
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
			//使用 PageRowBounds 会查询总数
			PageRowBounds pageRowBounds = new PageRowBounds(2, 2);
			list = privilegeMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
			//再次查询
			pageRowBounds = new PageRowBounds(4, 2);
			list = privilegeMapper.selectAll(pageRowBounds);
			//获取总数
			System.out.println("查询总数：" + pageRowBounds.getTotal());
			for(SysPrivilege privilege : list){
				System.out.println("权限名：" + privilege.getPrivilegeName());
			}
		} finally {
			sqlSession.close();
		}
	}
	
}
