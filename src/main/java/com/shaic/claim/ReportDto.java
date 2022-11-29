package com.shaic.claim;

import java.util.ArrayList;
import java.util.List;

public class ReportDto {
private String claimId;
private List beanList;
private String billAssessmentVersion;

//Added for reminder batch.

private String presenterString = "";

public String getPresenterString() {
	return presenterString;
}
public void setPresenterString(String presenterString) {
	this.presenterString = presenterString;
}
public ReportDto(){
	beanList = new ArrayList();
}
public String getClaimId() {
	return claimId;
}
public void setClaimId(String claimId) {
	this.claimId = claimId;
}
public List getBeanList() {
	return beanList;
}
public void setBeanList(List beanList) {
	this.beanList = beanList;
}
public String getBillAssessmentVersion() {
	return billAssessmentVersion;
}
public void setBillAssessmentVersion(String billAssessmentVersion) {
	this.billAssessmentVersion = billAssessmentVersion;
}

}
