package com.shaic.claim.OMPReOpenClaim.search;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class OMPReOpenClaimTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	private Long serialNo;
	private String intimationNo;
	private String claimno;
	private String policyno;
	private String insuredPatientName;
	private String hospitalname;
	private String hospitalcity;
	private Date dateofAdmission;
	private String reasonforadmission;
	private String claimstatus;
	
	
	public Long getSerialNo(){
		return serialNo;
	}
	public void setSerialNo(Long serialNo){
		this.serialNo = serialNo;
	}
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getClaimno(){
		return claimno;
	}
	public void setClaimno(String claimno){
		this.claimno = claimno;
	}
	public String getPolicyno(){
		return policyno;
	}
	public void setPolicyno(String policyno){
		this.policyno = policyno;
	}
	public String getInsuredPatientName(){
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName){
		this.insuredPatientName = insuredPatientName;
	}
	
	public String getHospitalname(){
		return hospitalname;
	}
	public void setHospitalname(String hospitalname){
		this.hospitalname = hospitalname;
	}
	
	public String getHospitalcity(){
		return hospitalcity;
	}
	public void setHospitalcity(String hospitalcity){
		this.hospitalcity = hospitalcity;
	}
	public Date getDateofAdmission(){
		return dateofAdmission;
	}
	public void setDateofAdmission(Date dateofAdmission){
		this.dateofAdmission = dateofAdmission;
	}
	public String getReasonforadmission(){
		return reasonforadmission;
	}
	public void setReasonforadmission(String reasonforadmission){
		this.reasonforadmission = reasonforadmission;
	}
	public String getClaimstatus(){
		return claimstatus;
	}
	public void setClaimstatus(String claimstatus){
		this.claimstatus = claimstatus;
	}
	
}
