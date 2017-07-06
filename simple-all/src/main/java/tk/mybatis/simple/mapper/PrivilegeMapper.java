package tk.mybatis.simple.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;

import tk.mybatis.simple.model.SysPrivilege;
import tk.mybatis.simple.provider.PrivilegeProvider;

@CacheNamespace
public interface PrivilegeMapper {

	@SelectProvider(type = PrivilegeProvider.class, method = "selectById")
	SysPrivilege selectById(Long id);
	
	@SelectProvider(type = PrivilegeProvider.class, method = "selectByPrivilege")
	List<SysPrivilege> selectByPrivilege(SysPrivilege privilege);
	
	@SelectProvider(type = PrivilegeProvider.class, method = "selectAll")
	List<SysPrivilege> selectAll();
	
	//这个方法和上面的方法同名，都会使用 tk.mybatis.simple.mapper.PrivilegeMapper.selectAll 方法
	List<SysPrivilege> selectAll(RowBounds rowBounds);
	
	@InsertProvider(type = PrivilegeProvider.class, method = "insert")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(SysPrivilege sysPrivilege);
	
	//TODO 使用相同的insert方法，但是主键方式不同
	@InsertProvider(type = PrivilegeProvider.class, method = "insert")
	@SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", resultType = Long.class, before = false)
	int insert2(SysPrivilege sysPrivilege);
	
	@UpdateProvider(type = PrivilegeProvider.class, method = "updateById")
	int updateById(SysPrivilege sysPrivilege);
	
	@DeleteProvider(type = PrivilegeProvider.class, method = "deleteById")
	int deleteById(Long id);
	
}
