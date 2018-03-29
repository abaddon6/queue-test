package com.volvo.jvs.mqtest.web;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.volvo.jvs.mqtest.beans.Result;
import com.volvo.jvs.mqtest.service.JmsService;

@Path("/jms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Endpoint {

	@Inject
	private JmsService jmsService;
	
	@GET	
	public Result send(@QueryParam("message") @NotNull Result result) {
		getJmsService().sendMessage(result.getTest());
		return result;
	}
	
	public JmsService getJmsService() {
		return jmsService;
	}
}
