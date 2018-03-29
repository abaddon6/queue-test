package com.volvo.jvs.mqtest.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.google.gson.Gson;

@Provider
public class GsonProvider implements ParamConverterProvider {

	@Context
	private Providers providers;

	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType,
			Type genericType, Annotation[] annotations) {

		// Check whether we can convert the given type with Gson
		final MessageBodyReader<T> mbr = providers.getMessageBodyReader(
				rawType, genericType, annotations,
				MediaType.APPLICATION_JSON_TYPE);
		if (mbr == null || !mbr.isReadable(rawType, genericType, annotations,
				MediaType.APPLICATION_JSON_TYPE)) {
			return null;
		}

		return new ParamConverter<T>() {
			@Override
			public T fromString(String value) {
				return new Gson().fromJson(value, rawType);
			}

			@Override
			public String toString(T value) {
				return new Gson().toJson(value);
			}
		};
	}
}