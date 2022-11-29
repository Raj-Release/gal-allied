package com.shaic.claim.omp.searchregistration;

import java.io.Serializable;
import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;

public class OMPSearchClaimRegistrationFormDto extends AbstractSearchDTO implements Serializable{
	
	private String intimationNo;
	private SelectValue eventType;
	private String policyNo;
//	private String insuredname;
//	private String claimno;
	private SelectValue productCode;
	private Date intimationDate;
	private String intimationDate1;
	private Date lossnDate;
	private String lossnDate1;

	private String userId;
	
	private String password;
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}
	public SelectValue getEventType() {
		return eventType;
	}
	public void setEventType(SelectValue eventType) {
		this.eventType = eventType;
	}
	public SelectValue getProductCode() {
		return productCode;
	}
	public void setProductCode(SelectValue productCode) {
		this.productCode = productCode;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
		
	public Date getIntimationDate() {
		return intimationDate;
	}

//	public void setIntimationDate(Date intimationDate) {
//		String dateformate = new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);

	public void setIntimationDate(Date intimationDate) {/*
		String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(intimationDate);
>>>>>>> bb697baeb8eb59134bd3d1c41c7a331da54dfe24
		setIntimationDate1(dateformate);
		this.intimationDate = intimationDate;
	*/}
	public String getIntimationDate1() {
		return intimationDate1;
	}
	public void setIntimationDate1(String intimationDate1) {
		this.intimationDate1 = intimationDate1;
	}
	
	
	
	public Date getLossnDate() {
		return lossnDate;
	}
	public void setLossnDate(Date lossnDate) {/*
		String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(lossnDate);
		setLossnDate1(dateformate);
		this.lossnDate = lossnDate;
	*/}
	
	public String getLossnDate1() {
		return lossnDate1;
	}
	public void setLossnDate1(String lossnDate1) {
		this.lossnDate1 = lossnDate1;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
