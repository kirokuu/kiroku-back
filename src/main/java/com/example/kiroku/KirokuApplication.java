package com.example.kiroku;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(title = "Kiroku API 문서",
				version = "v1",
				description = "Kiroku 서비스 API 명세서")
)
@SpringBootApplication
public class KirokuApplication {

	public static void main(String[] args) {
		SpringApplication.run(KirokuApplication.class, args);
	}

}
