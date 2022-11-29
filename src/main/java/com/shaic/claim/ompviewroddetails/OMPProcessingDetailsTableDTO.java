package com.shaic.claim.ompviewroddetails;


public class OMPProcessingDetailsTableDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long sno;
	
	private String rodNo;
	
	private String claimType;
	
	private String eventCode;
	
	private String classification;
	
	private String documentReceivedFrom;
	
	private String rodType;
	
	private String ompProcessApprovedDate;
	
	private String amount;
	
	private String status;
	
	private String typeofPayment;
	
	private String chequeOrTransactionno;
	
	private String chequeOrTransactionnoDate;
	
	private String remarks;
	
	
	public Long getSno(){
		return sno;
	}
	public void setSno(Long sno){
		this.sno = sno;
	}
	public String getRodNo(){
		return rodNo;
	}
	public void setRodNo(String rodNo){
		this.rodNo = rodNo;
	}
	public String getClaimType(){
		return claimType;
	}
	public void setClaimType(String claimType){
		this.claimType = claimType;
	}
	public String getEventCode(){
		return eventCode;
	}
	public void setEventCode(String eventCode){
		this.eventCode = eventCode;
	}
	public String getClassification(){
		return classification;
	}
	public void  setClassification(String classification){
		this.classification = classification;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getchequeOrTransactionnoDate(){
		return chequeOrTransactionnoDate ;
	}
	public void setchequeOrTransactionnoDate(String chequeOrTransactionnoDate){
		this.chequeOrTransactionnoDate = chequeOrTransactionnoDate ;
	}
	public String getDocumentReceivedFrom(){
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom){
		this.documentReceivedFrom = documentReceivedFrom ;
	}
	public String getRodType(){
		return rodType;
	}
	public void setRodType(String rodType){
		this.rodType = rodType;
	}
	public String getOmpProcessApprovedDate(){
		return ompProcessApprovedDate ;
	}
	public void setOmpProcessApprovedDate(String ompProcessApprovedDate){
		this.ompProcessApprovedDate = ompProcessApprovedDate ;
	}
	public String getAmount(){
		return amount;
	}
	public void setAmount(String amount){
		this.amount = amount;
	}
	public String getTypeofPayment(){
		return typeofPayment;
	}
	public void setTypeofPayment(String typeofPayment){
		this.typeofPayment = typeofPayment ;
	}
	public String getchequeOrTransactionno(){
		return chequeOrTransactionno;
	}
	public void setchequeOrTransactionno(String chequeOrTransactionno){
		this.chequeOrTransactionno = chequeOrTransactionno ;
	}
	public String getRemarks(){
		return remarks;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	

}
