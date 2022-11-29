package com.shaic.arch.table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7507325691254648107L;
	
	private int totalRecords;
	
	private int currentPageNum;
	
	private List<Map<String, String>> resultSet;

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	public List<Map<String, String>> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Map<String, String>> resultSet) {
		this.resultSet = resultSet;
	}

}
