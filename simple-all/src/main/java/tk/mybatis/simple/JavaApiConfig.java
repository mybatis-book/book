package tk.mybatis.simple;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import tk.mybatis.simple.model.Country;

public class JavaApiConfig {
	
	public static void main(String[] args) throws IOException {
		UnpooledDataSource dataSource = new UnpooledDataSource(
				"com.mysql.jdbc.Driver", 
				"jdbc:mysql://localhost:3306/mybatis", 
				"root", 
				"");
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("Java", transactionFactory, dataSource);
		
		Configuration configuration = new Configuration(environment);
		configuration.getTypeAliasRegistry().registerAliases("tk.mybatis.simple.model");
		configuration.setLogImpl(Log4jImpl.class);
		
		InputStream inputStream = Resources.getResourceAsStream("tk/mybatis/simple/mapper/CountryMapper.xml");
        XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, "tk/mybatis/simple/mapper/CountryMapper.xml", configuration.getSqlFragments());
        mapperParser.parse();
        
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			List<Country> countryList = sqlSession.selectList("selectAll");
			printCountryList(countryList);
		} finally {
			sqlSession.close();
		}
	}
	
	private static void printCountryList(List<Country> countryList){
		for(Country country : countryList){
			System.out.printf("%-4d%4s%4s\n",country.getId(), country.getCountryname(), country.getCountrycode());
		}
	}
	
}
