package tk.mybatis.simple.plugin;

import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * MyBatis Map 类型下划线 Key 转小写驼峰形式
 *
 * @author liuzenghui
 */
@Intercepts(
    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CameHumpInterceptor implements Interceptor {
    
	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        //先执行得到结果，再对结果进行处理
        List<Object> list = (List<Object>) invocation.proceed();
        for(Object object : list){
        	//如果结果是 Map 类型，就对 Map 的 Key 进行转换
            if(object instanceof Map){
                processMap((Map)object);
            } else {
                break;
            }
        }
        return list;
    }

    /**
     * 处理 Map 类型
     *
     * @param map
     */
    private void processMap(Map<String, Object> map) {
        Set<String> keySet = new HashSet<String>(map.keySet());
        for(String key : keySet){
        	//大写开头的会将整个字符串转换为小写，如果包含下划线也会处理为驼峰
        	if((key.charAt(0) >= 'A' && key.charAt(0) <= 'Z') || key.indexOf("_") >= 0){
        		Object value = map.get(key);
        		map.remove(key);
        		map.put(underlineToCamelhump(key), value);
        	}
        }
    }

    /**
     * 将下划线风格替换为驼峰风格
     *
     * @param inputString
     * @return
     */
    public static String underlineToCamelhump(String inputString) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if(c == '_'){
            	if (sb.length() > 0) {
                    nextUpperCase = true;
                }
            } else {
            	if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public Object plugin(Object target) {
    	return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}