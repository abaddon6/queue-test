package com.volvo.jvs.mqtest.web;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class MqTestResourceConfig extends ResourceConfig {

	public MqTestResourceConfig() {
		register(Endpoint.class);
	}
	
}
