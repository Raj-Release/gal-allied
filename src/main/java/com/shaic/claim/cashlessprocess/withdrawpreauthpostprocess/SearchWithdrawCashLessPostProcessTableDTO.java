package com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.enhacement.table.PreviousPreAuthTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.withdrawPostProcessWizard.UpdateBillClassificationListenerDTO;
import com.shaic.domain.preauth.Preauth;

public class SearchWithdrawCashLessPostProcessTableDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String intimationNo;

	private String claimNo;

	private String lob;

	private String insuredPatientName;

	private String paPatientName;

	private String diagnosis;

	private String hospitalName;

	private String hospitalAddress;

	private Long hospitalTypeId;

	private String hospitalCity;

	private String middleName;

	private String lastName;

	private String claimStatus;

	private Long policyKey;

	private Long claimKey;

	private Long productKey;

	private List<PreviousPreAuthTableDTO> previousPreAuthTableDTO;

	private Preauth preauth;
	
	private RRCDTO rRCDto;

	private PreauthDTO preauthDto;

	private Boolean isSpecialistReply = false;

	//		private String preAuthKey;

	private Long preAuthKey;
	private String doorApartmentNumber;
	private String plotGateNumber;
	private String buildingName;
	private String streetName;
	private String locality;
	private String district;
	private String state;
	private String country;
	private String pinCode;

	private Boolean isCheifMedicalOfficer = false;
	private Boolean isZonalMedicalHead = false;
	private Boolean isZonalSeniorMedicalApprover = false;
	private Boolean isZonalMedicalApprover = false;
	
	
	private String rodNumber;
	private Date documentReceivedDate;
	private String modeOfReceipt;
	private String billClassification;
	private Double approvedAmount;
	private String status;
	private Long documentSummaryKey;
	
	private List<WithDrawPostProcessBillDetailsDTO> billClassificationTableDTO;
	

	private Long hospitalId;
	//		private HumanTask humanTask;

	public String getPaPatientName() {
		return paPatientName;
	}

	public void setPaPatientName(String paPatientName) {
		this.paPatientName = paPatientName;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Long getHospitalTypeId() {
		return hospitalTypeId;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setHospitalTypeId(Long hospitalTypeId) {
		this.hospitalTypeId = hospitalTypeId;
	}

	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getDoorApartmentNumber() {
		return doorApartmentNumber;
	}

	public void setDoorApartmentNumber(String doorApartmentNumber) {
		this.doorApartmentNumber = doorApartmentNumber;
	}

	public String getPlotGateNumber() {
		return plotGateNumber;
	}

	public void setPlotGateNumber(String plotGateNumber) {
		this.plotGateNumber = plotGateNumber;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getHospitalCity() {
		return hospitalCity;
	}

	public void setHospitalCity(String hospitalCity) {
		this.hospitalCity = hospitalCity;
	}

	public Long getPreAuthKey() {
		return preAuthKey;
	}

	public void setPreAuthKey(Long preAuthKey) {
		this.preAuthKey = preAuthKey;
	}

	/*public HumanTask getHumanTask() {
			return humanTask;
		}

		public void setHumanTask(HumanTask humanTask) {
			this.humanTask = humanTask;
		}
	 */
	public List<PreviousPreAuthTableDTO> getPreviousPreAuthTableDTO() {
		return previousPreAuthTableDTO;
	}

	public void setPreviousPreAuthTableDTO(
			List<PreviousPreAuthTableDTO> previousPreAuthTableDTO) {
		this.previousPreAuthTableDTO = previousPreAuthTableDTO;
	}

	public Preauth getPreauth() {
		return preauth;
	}

	public PreauthDTO getPreauthDto() {
		return preauthDto;
	}

	public void setPreauthDto(PreauthDTO preauthDto) {
		this.preauthDto = preauthDto;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Boolean getIsSpecialistReply() {
		return isSpecialistReply;
	}

	public void setIsSpecialistReply(Boolean isSpecialistReply) {
		this.isSpecialistReply = isSpecialistReply;
	}

	public Boolean getIsCheifMedicalOfficer() {
		return isCheifMedicalOfficer;
	}

	public void setIsCheifMedicalOfficer(Boolean isCheifMedicalOfficer) {
		this.isCheifMedicalOfficer = isCheifMedicalOfficer;
	}

	public Boolean getIsZonalMedicalHead() {
		return isZonalMedicalHead;
	}

	public void setIsZonalMedicalHead(Boolean isZonalMedicalHead) {
		this.isZonalMedicalHead = isZonalMedicalHead;
	}

	public Boolean getIsZonalSeniorMedicalApprover() {
		return isZonalSeniorMedicalApprover;
	}

	public void setIsZonalSeniorMedicalApprover(Boolean isZonalSeniorMedicalApprover) {
		this.isZonalSeniorMedicalApprover = isZonalSeniorMedicalApprover;
	}

	public Boolean getIsZonalMedicalApprover() {
		return isZonalMedicalApprover;
	}

	public void setIsZonalMedicalApprover(Boolean isZonalMedicalApprover) {
		this.isZonalMedicalApprover = isZonalMedicalApprover;
	}

	public Long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public RRCDTO getrRCDto() {
		return rRCDto;
	}

	public void setrRCDto(RRCDTO rRCDto) {
		this.rRCDto = rRCDto;
	}

	public String getRodNumber() {
		return rodNumber;
	}

	public void setRodNumber(String rodNumber) {
		this.rodNumber = rodNumber;
	}

	public Date getDocumentReceivedDate() {
		return documentReceivedDate;
	}

	public void setDocumentReceivedDate(Date documentReceivedDate) {
		this.documentReceivedDate = documentReceivedDate;
	}

	public String getModeOfReceipt() {
		return modeOfReceipt;
	}

	public void setModeOfReceipt(String modeOfReceipt) {
		this.modeOfReceipt = modeOfReceipt;
	}

	public String getBillClassification() {
		return billClassification;
	}

	public void setBillClassification(String billClassification) {
		this.billClassification = billClassification;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Long getDocumentSummaryKey() {
		return documentSummaryKey;
	}

	public void setDocumentSummaryKey(Long documentSummaryKey) {
		this.documentSummaryKey = documentSummaryKey;
	}

	public List<WithDrawPostProcessBillDetailsDTO> getBillClassificationTableDTO() {
		return billClassificationTableDTO;
	}

	public void setBillClassificationTableDTO(
			List<WithDrawPostProcessBillDetailsDTO> billClassificationTableDTO) {
		this.billClassificationTableDTO = billClassificationTableDTO;
	}



	
	/*public String getPreAuthKey() {
			return preAuthKey;
		}

		public void setPreAuthKey(String preAuthKey) {
			this.preAuthKey = preAuthKey;
		}
	 */
}



