package kr.co.mgv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootMgvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMgvApplication.class, args);
	}

}
