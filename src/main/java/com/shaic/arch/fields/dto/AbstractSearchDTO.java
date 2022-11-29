package com.shaic.arch.fields.dto;

import java.io.Serializable;

public abstract class AbstractSearchDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int pageNumber;
	
	private Integer pageIndex = 0;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
}
