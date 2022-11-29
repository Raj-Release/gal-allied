package com.shaic.claim.omppaymentletterbulk;

import java.util.ArrayList;
import java.util.List;

public class PrintBulkPaymentResultDto {
	
	
	private List<Long> docTokenList = new ArrayList<Long>();
	private Integer sno; 
	private Integer totalNoofRecords;
	private String print;

	

	

	public List<Long> getDocTokenList() {
		return docTokenList;
	}

	public void setDocTokenList(List<Long> docTokenList) {
		this.docTokenList = docTokenList;
	}

	public Integer getSno() {
		return sno;
	}

	public void setSno(Integer sno) {
		this.sno = sno;
	}

	public String getPrint() {
		return print;
	}

	public void setPrint(String print) {
		this.print = print;
	}

	public Integer getTotalNoofRecords() {
		return totalNoofRecords;
	}

	public void setTotalNoofRecords(Integer totalNoofRecords) {
		this.totalNoofRecords = totalNoofRecords;
	}

	

}
