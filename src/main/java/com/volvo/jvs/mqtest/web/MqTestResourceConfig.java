package com.volvo.jvs.mqtest.web;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/mq-test")
public class MqTestResourceConfig extends ResourceConfig {

	public MqTestResourceConfig() {
		register(Endpoint.class);
		register(GsonProvider.class);
	}
}
