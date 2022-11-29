package com.shaic.claim.ompviewroddetails;

import java.util.Date;

public class OMPViewHistoryRodAndBillEntryTableDTO {
	
	
	private static final long serialVersionUID = 1l;
	
	private String typeOfClaim;
	
	private String rodNo;
	
	private String docRecdFrom;
	
	private String rodType;
	
	private String classification;
	
	private String subClassification;
	
	private Date dateAndTime;
	
	private String userId;
	
	private String userName;
	
	private String claimStage;
	
	private String status;
	
	private String userRemarks;
	
	private String getSubClassification(){
		return subClassification ;
	}
	public void setSubClassification(String subClassification){
		this.subClassification = subClassification;
	}
	
	public String getTypeOfClaim(){
		return typeOfClaim ;
	}
	public void setTypeOfClaim(String typeOfClaim){
		this.typeOfClaim = typeOfClaim;
	}
	public String getRodNo(){
		return rodNo;
	}
	public void setRodNo(String rodNo){
		this.rodNo = rodNo;
	}
	public String getDocRecdFrom(){
		return docRecdFrom ;
	}
	public void setDocRecdFrom(String docRecdFrom){
		this.docRecdFrom = docRecdFrom ;
	}
	public String getRodType(){
		return rodType;
	}
	public void setRodtype(String rodType){
		this.rodType = rodType;
	}
	public String getClassification(){
		return classification;
	}
	public void setClassification(String classification){
		this.classification = classification;
	}
	public Date getDateAndTime(){
		return dateAndTime;
	}
	public void setDateAndTime(Date dateAndTime){
		this.dateAndTime = dateAndTime;
	}
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public String getuserName(){
		return userName ;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getClaimStage(){
		return claimStage;
	}
	public void setClaimStage(String claimStage){
		this.claimStage = claimStage;
	}
	public String getStatus(){
		return status ;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getUserRemarks(){
		return userRemarks;
	}
	public void setUserRemarks(String userRemarks){
		this.userRemarks = userRemarks;
	}
	
	
	
	
	

}
