package com.shaic.arch.table;

import java.io.Serializable;

public class Pageable
  implements Serializable
{

  private static final long serialVersionUID = -8138745053550774237L;

  private int pageNumber;  //current page number

  private int totalPage;  //total number of pages available

  private int pageSize;  //number of records to display per page

  private int totalRecords;  //total number of records
  
  private boolean nextFlag;
  
  private String orderBy;
  
  public Pageable()
  {
    this.pageNumber = 1;
    this.pageSize = 10;
    this.totalPage = 0;
    this.totalRecords = 0;
  }

    public Pageable(int currentPage, int pageSize, boolean nextFlag)
    {
      this.pageNumber = currentPage;
      this.pageSize = pageSize;
      this.nextFlag = nextFlag;
    }

  public Pageable(int currentPage, int totalPage, int pageSize)
  {
    this.pageNumber = currentPage;
    this.totalPage = totalPage;
    this.pageSize = pageSize;
  }

  public int getTotalPage()
  {
    return totalPage;
  }

  public void setTotalPage(int totalPage)
  {
    this.totalPage = totalPage;
  }

  public int getPageSize()
  {
    return pageSize;
  }

  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }

  public void setTotalRecords(int totalRecords)
  {
    this.totalRecords = totalRecords;
  }

  public int getTotalRecords()
  {
    return totalRecords;
  }

    public void first()
      {
            this.pageNumber = 1;
      }

  public int getPageNumber()
  {
    return this.pageNumber;
  }

  public void setPageNumber(int pageNumber)
  {
    this.pageNumber = pageNumber;
  }

  public boolean hasPrevious()
  {
    return !(this.pageNumber <= 1);
  }

  public boolean hasNext()
  {
    return this.nextFlag;
  }

  public void setNextFlag(boolean nextFlag)
  {
      this.nextFlag = nextFlag;
  }
    
    public void next()
      {
        if (this.hasNext())
        {
          this.pageNumber++;
        }
      }

    public void previousOrFirst()
    {
      if (this.pageNumber > 1)
      {
        this.pageNumber--;
      }
      else
      {
        this.pageNumber = 1;
      }
    }

  public Pageable last()
  {
    this.pageNumber = this.totalPage;
    return new Pageable(this.totalPage, totalPage, this.pageSize);
  }

    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    public String getOrderBy()
    {
        return orderBy;
    }

}
