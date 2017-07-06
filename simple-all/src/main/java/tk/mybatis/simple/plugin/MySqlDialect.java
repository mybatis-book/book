package tk.mybatis.simple.plugin;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.RowBounds;

/**
 * MySql 实现
 * 
 * @author liuzh
 */
@SuppressWarnings("rawtypes")
public class MySqlDialect implements Dialect {

	@Override
	public boolean skip(String msId, Object parameterObject, RowBounds rowBounds) {
		//这里使用 RowBounds 分页，默认没有 RowBounds 参数时，会使用 RowBounds.DEFAULT 作为默认值
		if(rowBounds != RowBounds.DEFAULT){
			return false;
		}
		return true;
	}

	@Override
	public boolean beforeCount(String msId, Object parameterObject, RowBounds rowBounds) {
		//只有使用 PageRowBounds 才能记录总数，否则查询了总数也没用
		if(rowBounds instanceof PageRowBounds){
    		return true;
    	}
		return false;
	}
	
	@Override
	public String getCountSql(BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey countKey) {
		//简单嵌套实现 MySql count 查询
		return "select count(*) from (" + boundSql.getSql() + ") temp";
	}
	
    @Override
    public void afterCount(long count, Object parameterObject, RowBounds rowBounds) {
    	//记录总数，按照 beforeCount 逻辑，只有 PageRowBounds 时才会查询 count，所以这里直接强制转换
    	((PageRowBounds)rowBounds).setTotal(count);
    }
    
    @Override
	public boolean beforePage(String msId, Object parameterObject, RowBounds rowBounds) {
		if(rowBounds != RowBounds.DEFAULT){
			return true;
		}
		return false;
	}
	
	@Override
	public String getPageSql(BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
		//pageKey 会影响缓存，通过固定的 RowBounds 可以保证二级缓存有效
		pageKey.update("RowBounds");
		return boundSql.getSql() + " limit " + rowBounds.getOffset() + "," + rowBounds.getLimit();
	}

	@Override
	public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
		return pageList;
	}
	
	@Override
	public void setProperties(Properties properties) {
		
	}


}
