package tk.mybatis.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.springboot.mapper.CountryMapper;

/**
 * Spring Boot 启动类
 */
@SpringBootApplication
@MapperScan(value = { 
		"tk.mybatis.springboot.mapper", 
		"tk.mybatis.simple.mapper" 
	},
	nameGenerator = MapperNameGenerator.class
)
public class Application implements CommandLineRunner {

	@Autowired
	private CountryMapper countryMapper;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		countryMapper.selectAll();
	}

}
