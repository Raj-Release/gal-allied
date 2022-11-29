package com.shaic.claim.reports.hospitalintimationstatus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;

public class HospitalIntimationReportStatusTableDTO extends AbstractTableDTO  implements Serializable{
	private Long hospitalNameId;
	private String intimationNo;
	private String policyNo;
	private Date intimationDate;
	private String intimationDateValue;
	private String productCode;
	private String insuredName;
	private String hospitalName;
	private String hospitalAddress;
	private String telephoneNo;
	private String fax;
	private String city;
	private String state;
	private String stage;
	private Long intimationKey;
	
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	
	public Long getHospitalNameId() {
		return hospitalNameId;
	}
	public void setHospitalNameId(Long hospitalNameId) {
		this.hospitalNameId = hospitalNameId;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public Date getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(Date intimationDate) {
				if(intimationDate !=  null){
			String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
			setIntimationDateValue(dateformat);
		    this.intimationDate = intimationDate;
		}
	}
	public String getIntimationDateValue() {
		return intimationDateValue;
	}
	public void setIntimationDateValue(String intimationDateValue) {
		this.intimationDateValue = intimationDateValue;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalAddress() {
		return hospitalAddress;
	}
	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	

}
