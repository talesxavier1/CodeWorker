package com.br.tx.GroovyMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GroovyMessageV1 implements Serializable {

	private static final long serialVersionUID = 1L;
	private Object body;
	private Map<String, Object> headers = new HashMap<>();
	private Map<String, Object> properties = new HashMap<>();

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public String getInternalClassName() {
		return "GroovyMessageV1";
	}
}
