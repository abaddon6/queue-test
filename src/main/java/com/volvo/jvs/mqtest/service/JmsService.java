package com.volvo.jvs.mqtest.service;

import java.util.List;

import com.volvo.jvs.mqtest.beans.Result;

public interface JmsService {

	public void sendMessage(String message);
	
	public List<Result> getMessage();
}
