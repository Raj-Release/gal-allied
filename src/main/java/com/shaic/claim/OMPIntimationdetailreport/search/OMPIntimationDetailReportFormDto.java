package com.shaic.claim.OMPIntimationdetailreport.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPIntimationDetailReportFormDto extends AbstractSearchDTO implements Serializable{
	
	
	
	private String intimationNo;
	private String policyNo;
	private Date fromdate;
	private Date todate;
	private String fromDate1;
	private String toDate1;
	
	
	public String getPolicyNo(){
		return policyNo;
	}
	public void setPolicyNo(String policyNo){
		this.policyNo = policyNo;
	}
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public Date getFromdate(){
		return fromdate;
	}
	public void setFromdate(Date fromdate){
		String dateformate = new SimpleDateFormat("dd/MM/yyyy").format(fromdate);
		setFromDate1(dateformate);
		this.fromdate = fromdate;
	}
	
	public Date getTodate(){
		return todate;
	}
	public void setTodate(Date todate){
		String dateformate = new SimpleDateFormat("dd/mm/yyyy").format(fromdate);
		setToDate1(dateformate);
		this.todate = todate;
	}
	public String getFromDate1() {
		return fromDate1;
	}
	public void setFromDate1(String fromDate1) {
		this.fromDate1 = fromDate1;
	}
	public String getToDate1() {
		return toDate1;
	}
	public void setToDate1(String toDate1) {
		this.toDate1 = toDate1;
	}
}
