package com.volvo.jvs.mqtest.web;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.stereotype.Component;

import com.volvo.jvs.mqtest.service.JmsService;

@Component
@Path("/jms")
public class Endpoint {

	@Inject
	private JmsService jmsService;
	
	@GET
	public String send(@QueryParam("message") @NotNull String message) {
		getJmsService().sendMessage(message);
		return "Send: " + message;
	}
	
	public JmsService getJmsService() {
		return jmsService;
	}
}
