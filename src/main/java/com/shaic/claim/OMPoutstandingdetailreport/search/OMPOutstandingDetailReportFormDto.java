package com.shaic.claim.OMPoutstandingdetailreport.search;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPOutstandingDetailReportFormDto extends AbstractSearchDTO implements Serializable{
	
	
	private String intimationNo;
	private String policyNo;
	private Date fromdate;
	private Date todate;
	private String fromdate1;
	private String todate1;
	
	
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	
	public String getPolicyNo(){
		return policyNo;
	}
	public void setPolicyNo(String policyNo){
		this.policyNo = policyNo;
	}
	
	public String getFromdate1(){
		return fromdate1;
	}
	public void setFromdate1(String fromdate1){
		this.fromdate1 = fromdate1;
	}
	
	public String getTodate1(){
		return todate1;
	}
	public void setTodate1(String todate1){
		this.todate1 = todate1;
	}
	
	public Date getFromdate(){
		return fromdate;
	}
	public void setFromdate(Date fromdate){
			String dateformate = new SimpleDateFormat("dd/MM/yyyy").format(fromdate);
			setFromdate1(dateformate);
			this.fromdate = fromdate;
	
	}
	
	public Date getTodate(){
		return todate;
	}
	public void setTodate(Date todate){
			String dateformate = new SimpleDateFormat("dd/MM/yyyy").format(todate);
			setTodate1(dateformate);
			this.todate = todate;
	
	}
	

}