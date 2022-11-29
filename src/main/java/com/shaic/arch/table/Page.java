package com.shaic.arch.table;

import java.util.ArrayList;
import java.util.List;

//import com.shaic.ims.bpm.claim.corev2.PagedTaskList;

public class Page<E> {

    private int pageNumber;
    private int pagesAvailable;
    private boolean hasNext;
    private Integer totalRecords;
    private Boolean isDbSearch = false;
    private String correctMsg = "No Records found";
    
    private String searchId;
    
    private List totalList ;
    
    

	public Page()
    {
    	super();
    }
    
  /*  public Page(PagedTaskList taskList)
    {
    	hasNext = taskList.getIsNextPageAvailable();
    	this.pageNumber = taskList.getCurrentPage();
    }*/
    
    public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	private List<E> pageItems = new ArrayList<E>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public void setPageItems(List<E> pageItems) {
        this.pageItems = pageItems;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public List<E> getPageItems() {
        return pageItems;
    }

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Boolean getIsDbSearch() {
		return isDbSearch;
	}

	public void setIsDbSearch(Boolean isDbSearch) {
		this.isDbSearch = isDbSearch;
	}

	public List getTotalList() {
		return totalList;
	}

	public void setTotalList(List totalList) {
		this.totalList = totalList;
	}

	public String getCorrectMsg() {
		return correctMsg;
	}

	public void setCorrectMsg(String correctMsg) {
		this.correctMsg = correctMsg;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}



	
}