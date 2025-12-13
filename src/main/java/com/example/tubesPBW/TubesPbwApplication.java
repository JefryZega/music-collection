package com.example.tubesPBW;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TubesPbwApplication {

	public static void main(String[] args) {
		SpringApplication.run(TubesPbwApplication.class, args);
	}
}