/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.medicalapproval.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.cmn.login.ImsUser;


/**
 * @author ntv.narenj
 *
 */
public class PAHealthSearchProcessClaimRequestTableDTO extends AbstractTableDTO  implements Serializable{

	private Long intimationKey;
	private Long policyKey;
	private String policyNumber;
	private String insuredPatientName;
	private Long claimKey;
	private Double requestedAmt;
	private Double claimedAmountAsPerBill;
	private Double balanceSI;
	private Long insuredKey;
	private Long cpuId;
	private Long hospitalNameID;
	private Double sumInsured;
	private String intimationNo;
	private String intimationSource;
	private String cpuName;
	private String productName;
	private String insuredPatiendName;
	private String paPatientName;
	private Long productKey;
	private String hospitalName;
	private String hospitalType;
	private String claimType;
	private String documentReceivedFrom;
	private String treatementType;
	private String speciality;
	private String reasonForAdmission;
	private String originatorID;
	private String originatorName;
	private Long rodKey;	
	private ImsUser imsUser;
	
	private String rodAgeing;
	private String specialistOrQueryOrCoordinatorReqId;
	private String specialistOrQueryOrCoordinatorReqName;
	private PAHealthSearchProcessClaimRequestFormDTO searchDTO;
	private String accidentOrDeath;
	private String status;
	private String ackAgeing;

	private String screenName;
	
	
	public String getIntimationNo() {
		return intimationNo;
	}
	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}	
	public String getInsuredPatiendName() {
		return insuredPatiendName;
	}
	public void setInsuredPatiendName(String insuredPatiendName) {
		this.insuredPatiendName = insuredPatiendName;
	}
	
	public String getIntimationSource() {
		return intimationSource;
	}
	public void setIntimationSource(String intimationSource) {
		this.intimationSource = intimationSource;
	}
	public String getCpuName() {
		return cpuName;
	}
	public void setCpuName(String cpuName) {
		this.cpuName = cpuName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getHospitalType() {
		return hospitalType;
	}
	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}
	
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getTreatementType() {
		return treatementType;
	}
	public void setTreatementType(String treatementType) {
		this.treatementType = treatementType;
	}
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getReasonForAdmission() {
		return reasonForAdmission;
	}
	public void setReasonForAdmission(String reasonForAdmission) {
		this.reasonForAdmission = reasonForAdmission;
	}
	public String getOriginatorID() {
		return originatorID;
	}
	public void setOriginatorID(String originatorID) {
		this.originatorID = originatorID;
	}
	public Long getIntimationKey() {
		return intimationKey;
	}
	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}
	public Long getPolicyKey() {
		return policyKey;
	}
	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
	public String getInsuredPatientName() {
		return insuredPatientName;
	}
	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
	public Long getClaimKey() {
		return claimKey;
	}
	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}
	public Double getRequestedAmt() {
		return requestedAmt;
	}
	public void setRequestedAmt(Double requestedAmt) {
		this.requestedAmt = requestedAmt;
	}
	public Double getBalanceSI() {
		return balanceSI;
	}
	public void setBalanceSI(Double balanceSI) {
		this.balanceSI = balanceSI;
	}
	public Long getInsuredKey() {
		return insuredKey;
	}
	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}
	public Long getCpuId() {
		return cpuId;
	}
	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}
	public Long getHospitalNameID() {
		return hospitalNameID;
	}
	public void setHospitalNameID(Long hospitalNameID) {
		this.hospitalNameID = hospitalNameID;
	}
	public Double getSumInsured() {
		return sumInsured;
	}
	public void setSumInsured(Double sumInsured) {
		this.sumInsured = sumInsured;
	}
	public Long getRodKey() {
		return rodKey;
	}
	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}	
	public String getOriginatorName() {
		return originatorName;
	}
	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public ImsUser getImsUser() {
		return imsUser;
	}
	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}
	public String getRodAgeing() {
		return rodAgeing;
	}
	public void setRodAgeing(String rodAgeing) {
		this.rodAgeing = rodAgeing;
	}
	public String getDocumentReceivedFrom() {
		return documentReceivedFrom;
	}
	public void setDocumentReceivedFrom(String documentReceivedFrom) {
		this.documentReceivedFrom = documentReceivedFrom;
	}
	public Double getClaimedAmountAsPerBill() {
		return claimedAmountAsPerBill;
	}
	public void setClaimedAmountAsPerBill(Double claimedAmountAsPerBill) {
		this.claimedAmountAsPerBill = claimedAmountAsPerBill;
	}
	public String getSpecialistOrQueryOrCoordinatorReqId() {
		return specialistOrQueryOrCoordinatorReqId;
	}
	public void setSpecialistOrQueryOrCoordinatorReqId(
			String specialistOrQueryOrCoordinatorReqId) {
		this.specialistOrQueryOrCoordinatorReqId = specialistOrQueryOrCoordinatorReqId;
	}
	public String getSpecialistOrQueryOrCoordinatorReqName() {
		return specialistOrQueryOrCoordinatorReqName;
	}
	public void setSpecialistOrQueryOrCoordinatorReqName(
			String specialistOrQueryOrCoordinatorReqName) {
		this.specialistOrQueryOrCoordinatorReqName = specialistOrQueryOrCoordinatorReqName;
	}
	public PAHealthSearchProcessClaimRequestFormDTO getSearchDTO() {
		return searchDTO;
	}
	public void setSearchDTO(PAHealthSearchProcessClaimRequestFormDTO searchDTO) {
		this.searchDTO = searchDTO;
	}
	public String getAccidentOrDeath() {
		return accidentOrDeath;
	}
	public void setAccidentOrDeath(String accidentOrDeath) {
		this.accidentOrDeath = accidentOrDeath;
	}
	public String getPaPatientName() {
		return paPatientName;
	}
	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}
	public Long getProductKey() {
		return productKey;
	}
	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAckAgeing() {
		return ackAgeing;
	}
	public void setAckAgeing(String ackAgeing) {
		this.ackAgeing = ackAgeing;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}
