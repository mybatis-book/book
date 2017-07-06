package tk.mybatis.simple.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import tk.mybatis.simple.model.Country;
import tk.mybatis.simple.model.CountryExample;

public class CountryMapperTest extends BaseMapperTest {

	@Test
	public void testSelectAll() {
		SqlSession sqlSession = getSqlSession();
		try {
			List<Country> countryList = sqlSession.selectList("tk.mybatis.simple.mapper.CountryMapper.selectAll");
			printCountryList(countryList);
		} finally {
			sqlSession.close();
		}
	}

	private void printCountryList(List<Country> countryList) {
		for (Country country : countryList) {
			System.out.printf("%-4d%4s%4s\n", country.getId(), country.getCountryname(), country.getCountrycode());
		}
	}

	@Test
	public void testExample() {
		// 获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			// 获取 CountryMapper 接口
			CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
			//创建 Example 对象
			CountryExample example = new CountryExample();
			//设置排序规则
			example.setOrderByClause("id desc, countryname asc");
			//设置是否 distinct 去重
			example.setDistinct(true);
			//创建条件，只能有一个 createCriteria
			CountryExample.Criteria criteria = example.createCriteria();
			//id >= 1
			criteria.andIdGreaterThanOrEqualTo(1);
			//id < 4
			criteria.andIdLessThan(4);
			//countrycode like '%U%'
			//最容易出错的地方，注意 like 必须自己写上通配符的位置，不可能默认两边加 %，like 可以是任何情况
			criteria.andCountrycodeLike("%U%");
			//or 的情况，可以有多个 or
			CountryExample.Criteria or = example.or();
			//countryname = 中国
			or.andCountrynameEqualTo("中国");
			//执行查询
			List<Country> countryList = countryMapper.selectByExample(example);
			printCountryList(countryList);
		} finally {
			// 不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testUpdateByExampleSelective() {
		// 获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			// 获取 CountryMapper 接口
			CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
			//创建 Example 对象
			CountryExample example = new CountryExample();
			//创建条件，只能有一个 createCriteria
			CountryExample.Criteria criteria = example.createCriteria();
			//更新所有 id > 2 的国家
			criteria.andIdGreaterThan(2);
			//创建一个要设置的对象
			Country country = new Country();
			//将国家名字设置为 China
			country.setCountryname("China");
			//执行查询
			countryMapper.updateByExampleSelective(country, example);
			//在把符合条件的结果输出查看
			printCountryList(countryMapper.selectByExample(example));
		} finally {
			// 不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testDeleteByExample() {
		// 获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			// 获取 CountryMapper 接口
			CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
			//创建 Example 对象
			CountryExample example = new CountryExample();
			//创建条件，只能有一个 createCriteria
			CountryExample.Criteria criteria = example.createCriteria();
			//删除所有 id > 2 的国家
			criteria.andIdGreaterThan(2);
			//执行查询
			countryMapper.deleteByExample(example);
			//使用 countByExample 查询符合条件的数量，因为删除了，所以这里应该是 0
			Assert.assertEquals(0, countryMapper.countByExample(example));
		} finally {
			// 不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
    public void testMapperWithStartPage3() {
        SqlSession sqlSession = getSqlSession();
        CountryMapper countryMapper = sqlSession.getMapper(CountryMapper.class);
        try {
            //获取第1页，10条内容，默认查询总数count
            Map<String, Object> params = new HashMap<String, Object>();
            countryMapper.selectCountries(params);
            List<Country> list1 = (List<Country>) params.get("list1");
            List<Country> list2 = (List<Country>) params.get("list2");
            Assert.assertNotNull(list1);
            Assert.assertNotNull(list2);
        } finally {
            sqlSession.close();
        }
    }
}
