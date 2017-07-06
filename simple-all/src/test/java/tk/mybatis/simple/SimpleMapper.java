package tk.mybatis.simple;

import java.util.List;

import tk.mybatis.simple.model.Country;

public interface SimpleMapper {

	Country selectCountry(Long id);
	
	List<Country> selectAll();
}
