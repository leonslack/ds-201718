package com.ucab.base.webservices.dtos.response.base;

public class SimpleResponseStructure<T> extends AbstractBaseResponseStructure {

	
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
