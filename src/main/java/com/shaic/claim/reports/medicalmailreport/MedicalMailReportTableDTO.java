package com.shaic.claim.reports.medicalmailreport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class MedicalMailReportTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	private String intimationNo;
	private String cpuCode;
	private String officeCode;
	private String reasonForQuery;
	private Date queryDate;
	private String queryDateValue;
	private String emailStatus;
	private Date mailDate;
	private String mailDateValue;
	private String status;
	private String print;
	private String docType;
	
	
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getReasonForQuery() {
		return reasonForQuery;
	}
	public void setReasonForQuery(String reasonForQuery) {
		this.reasonForQuery = reasonForQuery;
	}
	public Date getQueryDate() {
		return queryDate;
	}
	public void setQueryDate(Date queryDate) {
		
		if(queryDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(queryDate);
			setQueryDateValue(dateformat);
		    this.queryDate = queryDate;
		}
	}
	public String getQueryDateValue() {
		return queryDateValue;
	}
	public void setQueryDateValue(String queryDateValue) {
		this.queryDateValue = queryDateValue;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	public Date getMailDate() {
		return mailDate;
	}
	public void setMailDate(Date mailDate) {
		this.mailDate = mailDate;
	}
	public String getMailDateValue() {
		return mailDateValue;
	}
	public void setMailDateValue(String mailDateValue) {
		this.mailDateValue = mailDateValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrint() {
		return print;
	}
	public void setPrint(String print) {
		this.print = print;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}	


}
