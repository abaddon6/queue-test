package com.volvo.jvs.mqtest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class MqTestApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		new MqTestApplication()
			.configure(new SpringApplicationBuilder(MqTestApplication.class))
			.run(args);
	}
}
