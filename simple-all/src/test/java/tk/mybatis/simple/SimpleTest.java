package tk.mybatis.simple;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LoggingCache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.junit.Test;

import tk.mybatis.simple.model.Country;

public class SimpleTest {

    @Test
    public void test() throws IOException, SQLException {
    	//使用 log4j 记录日志，前提是已经配置好了 log4j 依赖
        LogFactory.useLog4JLogging();
        //创建配置对象
        final Configuration config = new Configuration();
        //配置 settings 中的部分属性
        config.setCacheEnabled(true);
        config.setLazyLoadingEnabled(false);
        config.setAggressiveLazyLoading(true);
        
        //添加拦截器
        SimpleInterceptor interceptor1 = new SimpleInterceptor("拦截器 1");
        SimpleInterceptor interceptor2 = new SimpleInterceptor("拦截器 2");
        config.addInterceptor(interceptor1);
        config.addInterceptor(interceptor2);

        //创建 DataSource
        UnpooledDataSource dataSource = new UnpooledDataSource();
        dataSource.setDriver("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("");

        //创建 JDBC 事务
        Transaction transaction = new JdbcTransaction(dataSource, null, false);

        //创建 Executor
        //config.newExecutor 会将符合条件的拦截器添加到 Executor 代理链上
        final Executor executor = config.newExecutor(transaction);

        


        //类型处理注册器
        //自己写 TypeHandler 的时候可以参考该注册器中已经存在的大量实现
        final TypeHandlerRegistry registry = config.getTypeHandlerRegistry();

        //================== 下面的步骤相当于解析XML或者解析接口注解方法生成ms =====================

        //创建静态 SqlSource
        //最简单的，相当于从 xml 或接口注解获取 SQL，创建合适的 SqlSource对象
        StaticSqlSource sqlSource = new StaticSqlSource(
        		config, "SELECT * FROM country WHERE id = ?");

        //由于上面的 SQL 有个参数 id，这里需要提供 ParameterMapping(参数映射)
        List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        //通过 ParameterMapping.Builder 创建 ParameterMapping
        parameterMappings.add(new ParameterMapping.Builder(
        		config, "id", registry.getTypeHandler(Long.class)).build());
        ParameterMap.Builder paramBuilder = new ParameterMap.Builder(
        		config, "defaultParameterMap", Country.class, parameterMappings);

        //创建结果映射配置
        @SuppressWarnings("serial")
		ResultMap resultMap = new ResultMap.Builder(config, "defaultResultMap", Country.class,
                new ArrayList<ResultMapping>() {
					{
                        add(new ResultMapping.Builder(config, "id", "id", Long.class).build());
                        add(new ResultMapping.Builder(config, "countryname", "countryname", String.class).build());
                        add(new ResultMapping.Builder(config, "countrycode", "countrycode", registry.getTypeHandler(String.class)
                        ).build());
                    }
                }).build();

        //不设置具体的映射，只是用类型，相当于只配置 resultType="tk.mybatis.sample1.Country"
        //ResultMap resultMap = new ResultMap.Builder(config, "defaultResultMap", Country.class, new ArrayList<ResultMapping>()).build();

        //cache是一个多层代理【装饰模式】的缓存对象，通过一层层使得一个简单的缓存拥有了复杂的功能
        final Cache countryCache =
                new SynchronizedCache(//同步缓存
                        new SerializedCache(//序列化缓存
                                new LoggingCache(//日志缓存
                                        new LruCache(//最少使用缓存
                                                new PerpetualCache("country_cache")//持久缓存
                                        ))));
        //创建 MappedStatement
        MappedStatement.Builder msBuilder = new MappedStatement.Builder(
        		config, "tk.mybatis.simple.SimpleMapper.selectCountry", sqlSource, SqlCommandType.SELECT);
        msBuilder.parameterMap(paramBuilder.build());
        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        resultMaps.add(resultMap);
        //设置返回值的 resultMap
        msBuilder.resultMaps(resultMaps);
        //设置缓存
        msBuilder.cache(countryCache);
        //创建 ms
        MappedStatement ms = msBuilder.build();

        //第一种使用 executor 执行
        List<Country> countries = executor.query(ms, 1L, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);

        for (Country country : countries) {
        	System.out.println(country.getCountryname());
        }

        //第二种，首先添加 m s到 config
        config.addMappedStatement(ms);
        //创建 SqlSession
        SqlSession sqlSession = new DefaultSqlSession(config, executor, false);
        //查询
        Country country = sqlSession.selectOne("selectCountry", 2L);
    	System.out.println(country.getCountryname());

        //第三种 接口方式，创建接口代理工厂类
        MapperProxyFactory<SimpleMapper> mapperProxyFactory = new MapperProxyFactory<SimpleMapper>(SimpleMapper.class);
        //创建代理接口
        SimpleMapper simpleMapper = mapperProxyFactory.newInstance(sqlSession);
        //执行方法
        country = simpleMapper.selectCountry(3L);
    	System.out.println(country.getCountryname());
        //关闭
        sqlSession.close();
    }
}
