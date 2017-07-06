package tk.mybatis.springboot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import tk.mybatis.springboot.model.Country;

@Mapper
public interface CountryMapper {
	/**
	 * 查询全部数据
	 * 
	 * @return
	 */
	List<Country> selectAll();
}
