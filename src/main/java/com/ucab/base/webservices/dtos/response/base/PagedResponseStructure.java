package com.ucab.base.webservices.dtos.response.base;

import org.springframework.data.domain.Page;

public class PagedResponseStructure<T> extends AbstractBaseResponseStructure {

	
	private Page<T> data;

	public Page<T> getData() {
		return data;
	}

	public void setData(Page<T> data) {
		this.data = data;
	}


}
