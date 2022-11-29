package com.shaic.claim.ompviewroddetails;

import java.util.Date;

public class OMPViewNegotiationDetailsTableDTO {
	
	private static final long serialVersionUID = 1l;
	
	private Long sno;
	
	private String intimationNo;
	
	private String claimNo;
	
	private String eventCode;
	
	private Date negotiationRequestedDate; 
	
	private Date negotiationCompletedDate;
	
	private String nameofNegotiator;
	
	private String approvedAmount;
	
	private String agreedAmount;
	
	private String negotiationRemarks;
	
	
	
	public Long getSno(){
		return sno;
	}
	public void setSno(Long sno){
		this.sno = sno;
	}
	public String getIntimationNo(){
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo){
		this.intimationNo = intimationNo;
	}
	public String getClaimNo(){
		return claimNo;
	}
	public void setClaimNo(String claimNo){
		this.claimNo = claimNo;
	}
	public String getEventCode(){
		return eventCode;
	}
	public void setEventCode(String eventCode){
		this.eventCode = eventCode;
	}
	public Date getNegotiationRequestedDate(){
		return negotiationRequestedDate ;
	}
	public void setNegotiationRequetedDate(Date negotiationRequestedDate){
		this.negotiationRequestedDate = negotiationRequestedDate ;
	}
	public Date getNegotiationCompletedDate(){
		return negotiationCompletedDate;
	}
	public void setNegotiationCompletedDate(Date negotiationCompletedDate){
		this.negotiationCompletedDate = negotiationCompletedDate;
	}
	public String getNameofNegotiator(){
		return nameofNegotiator;
	}
	public void setNameofNegotiator(String nameofNegotiator){
		this.nameofNegotiator = nameofNegotiator;
	}
	public String getApprovedAmount(){
		return approvedAmount;
	}
	public void setApprovedAmount(String approvedAmount){
		this.approvedAmount = approvedAmount;
	}
	public String getAgreedAmount(){
		return agreedAmount;
	}
	public void setAgreedAmount(String agreedAmount){
		this.agreedAmount = agreedAmount;
	}
	public String getNegotiationRematks(){
		return negotiationRemarks ;
	}
	public void setNegotiationRemarks(String negotiationRemarks){
		this.negotiationRemarks = negotiationRemarks;
	}
	
	
}
