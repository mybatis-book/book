package tk.mybatis.util;

public class StringUtil {
	
	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static void print(Object parameter){
		System.out.println(parameter);
	}
	
}
