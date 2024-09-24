package br.com.tx.dbInterface.models;

import java.io.Serializable;

public class ResponseModel<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private T response;
	private String erros;

	public ResponseModel(T response, String erros) {
		this.response = response;
		this.erros = erros;
	}

	public T getResponse() {
		return response;
	}

	public String getErros() {
		return erros;
	}
}
