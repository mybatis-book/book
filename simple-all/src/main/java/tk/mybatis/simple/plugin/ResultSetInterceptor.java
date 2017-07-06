package tk.mybatis.simple.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({
	@Signature(type = Executor.class, method = "query", args = {
			MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
	})
})
public class ResultSetInterceptor implements Interceptor{

	@SuppressWarnings("rawtypes")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Executor executor = (Executor)invocation.getTarget();
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement)args[0];
		Object parameterObject = args[1];
		RowBounds rowBounds = (RowBounds)args[2];
		ResultHandler resultHandler = (ResultHandler)args[3];
		BoundSql boundSql = ms.getBoundSql(parameterObject);
		CacheKey pageKey = null, countKey = null;
		if(executor instanceof CachingExecutor){
			pageKey = ((CachingExecutor)executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
			countKey = ((CachingExecutor)executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
		} else {
			pageKey = ((BaseExecutor)executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
			countKey = ((BaseExecutor)executor).createCacheKey(ms, parameterObject, rowBounds, boundSql);
		}
		countKey.update("Count");
		BoundSql countSql = new BoundSql(ms.getConfiguration(), "select count(*) from (" + boundSql.getSql() + ") temp", boundSql.getParameterMappings(), parameterObject);
		Object r2 = executor.query(ms, parameterObject, rowBounds, resultHandler, pageKey, boundSql);
		MappedStatement countMs = newMappedStatement(ms, Long.class);
		Object r1 = executor.query(countMs, parameterObject, rowBounds, resultHandler, countKey, countSql);
		System.out.println(r1);
		return r2;
	}
	
	private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);
	
	 /**
     * 根据现有的 ms 创建一个新的，使用新的返回值类型
     *
     * @param ms
     * @param resultType
     * @return
     */
    public MappedStatement newMappedStatement(MappedStatement ms, Class<?> resultType) {
        //下面是新建的过程，考虑效率和复用对象的情况下，这里最后生成的ms可以缓存起来，下次根据 ms.getId() + "_" + getShortName(resultType) 直接返回 ms,省去反复创建的过程
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + "_Count" , ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULTMAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		//通过属性进行配置
	}

}
