/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.reimbursement.billing.dto.PatientCareDTO;
import com.shaic.claim.rod.wizard.pages.HopsitalCashBenefitDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

/**
 * @author ntv.vijayar
 *
 */
public class UploadDocumentDTO extends AbstractTableDTO {
	
	//File upload component to be injected...
	//private String fileUpload; //This is temp. Later this needs to be changed
	//private File fileUpload;
	//private String strfileUpload = "";
	
	private String sNo = "";
	
	private String updatedOn ="";
	
	private SelectValue fileType ;
	
	private String billNo = "";
	
	private Date billDate;
	
	private Long noOfItems = 0l;
	
	private Double billValue = 0d;
	
	private String fileTypeValue = "";
	
	private String fileName= "";
	
	private String rodNo="";
	
	private Long docREceivedFromId;
	
	private String acknowledgementNo;
	
	private SelectValue valChangedRoomCategory;
	
	private Boolean isSharedAccomotation = false;
	private Boolean isTreatementForPreferred = false;
	private Boolean isRepatriationOfMortal = false;
	private Boolean isCompassionateTravel = false;
	private Boolean isEmergencyDomestic = false;
	
	private SelectValue referenceNo;
	private String referenceNoValue = "";
	
	private Boolean isReceived = false;
	
	private Boolean isIgnored = false;
	
	private Long hospitalizationId;
	
	private SelectValue documentType;
	
	private Date docReceivedDate;
	
	private String remarks;
	
	private SelectValue receivStatus;
	
	private String receivStatusValue;
	
	private Long rodVersion;
	
	private BeanItemContainer<SelectValue> fileTypeContainer;
	
	private String deleted ="N";
	
	private String documentTypeValue;
	
	private Long billAmt;
	private Long deductibleAmt = 0l;
	private Long nonPaybleAmt  = 0l;
	private Long paybleAmt;
	
	private Boolean isPhysicalDoc = false;
	private Long opkey;
	
	private String ackDocumentSource;
	
	public Long getHospitalizationId() {
		return hospitalizationId;
	}

	public void setHospitalizationId(Long hospitalizationId) {
		this.hospitalizationId = hospitalizationId;
	}

	public Long getPreHospitalizationId() {
		return preHospitalizationId;
	}

	public void setPreHospitalizationId(Long preHospitalizationId) {
		this.preHospitalizationId = preHospitalizationId;
	}

	public Long getPostHospitalizationId() {
		return postHospitalizationId;
	}

	public void setPostHospitalizationId(Long postHospitalizationId) {
		this.postHospitalizationId = postHospitalizationId;
	}
	
	public Long getLumpsumId() {
		return lumpsumId;
	}

	public void setLumpsumId(Long lumpsumId) {
		this.lumpsumId = lumpsumId;
	}

	private Long preHospitalizationId;
	
	private Long postHospitalizationId;
	
	private Long lumpsumId;
	
	private Long otherBenefitsId;

	private Long hospitalCashId;
	
	
	/**
	 * Added for enabling category values
	 * based on product in bill entry or bill review
	 * table. 
	 */
	private Long productKey;
	private String subCoverCode;

	public String getSubCoverCode() {
		return subCoverCode;
	}

	public void setSubCoverCode(String subCoverCode) {
		this.subCoverCode = subCoverCode;
	}

	public String getAcknowledgementNo() {
		return acknowledgementNo;
	}

	public void setAcknowledgementNo(String acknowledgementNo) {
		this.acknowledgementNo = acknowledgementNo;
	}

	//private Boolean selectForBillEntry;
	private Boolean selectForBillEntry;
	
	private Boolean status = false;
	
	private List<BillEntryDetailsDTO> billEntryDetailList;
	
	private Long docSummaryKey;
	private Double totalClaimedAmount;
	
	
	//Added for R3.3
	
	private String hospitalizationFlag;
	
	private String preHospitalizationFlag;
	
	private String postHospitalizationFlag;
	
	private String hospitalizationRepeatFlag;
	
	private String partialHospitalizationFlag;
	
	//used for add on benefits feilds.
	
	private Boolean hospitalCashAddonBenefits;
	
	private Boolean patientCareAddOnBenefits;
	
	private Boolean otherBenefit;
	
	private Boolean emergencyMedicalEvaluation;	
	private Boolean compassionateTravel;	
	private Boolean repatriationOfMortalRemains;	
	private Boolean preferredNetworkHospital;	
	private Boolean sharedAccomodation;
	
	
	private String hospitalCashAddonBenefitsFlag;
	
	private String patientCareAddOnBenefitsFlag;
	
	private String otherBenefitFlag;
	
	private String emergencyMedicalEvaluationFlag;
	String compassionateTravelFlag;
	private String repatriationOfMortalRemainsFlag;	
	private String preferredNetworkHospitalFlag;
	String sharedAccomodationFlag;	
	private String hospitalCashNoofDays;
	
	@NotNull(message = "Please Enter hospital cash per day amount")
	@Size(min = 1 , message = "Please Enter hospital cash per day amount")
	private String hospitalCashPerDayAmt;
	
	private String hospitalCashTotalClaimedAmt;
	
	private String patientCareNoofDays;
	
	@NotNull(message = "Please Enter patient care per day amount")
	@Size(min = 1 , message = "Please Enter patient care per day amount")
	private String patientCarePerDayAmt;
	
	private String patientCareTotalClaimedAmt;
	
	private Boolean treatmentPhysiotherapy;
	
	private String treatmentPhysiotherapyFlag;
	
	private Boolean treatmentGovtHospital;
	
	private String treatmentGovtHospFlag;
	
	private Long reimbursementKey;
	
	private List<PatientCareDTO> patientCareDTO;
	
	//private String benefitsFlag;
	private String hospitalBenefitFlag;
	
	private String patientCareBenefitFlag;
	
	private Long hospitalBenefitKey;
	
	private Long patientBenefitKey;
	
	private BillEntryDetailsDTO billEntryDetailsDTO;
	
	//private FileUploadComponent fileUpload;
	
	private String dmsDocToken;
	
	private String zonalRemarks;
	
	private String corporateRemarks;
	
	private String billingRemarks;
	
	private Double productBasedRoomRent;
	
	private Double productBasedICURent;
	
	private Double productBasedICCURent;
	
	private Double productBasedAmbulanceAmt;
	
	
	//Added for loading upload listener table. whichever user clicks only that entry will be modified. For this  based on the serial no we do it.
	
	private Integer seqNo;
	
	
	//Added for new amount claimed table.
	
	private Double netAmount;
	
	private Boolean enableOrDisableBtn = true;
	
	private List<BillEntryDetailsDTO> deletedBillList ;
	
	private List<UploadDocumentDTO> deletedDocumentList;
	
	private String billingWorkSheetRemarks;
	
	private String fileUploadRemarks;
	
	private String createdBy;
	
	private Date createdDate;
	
	private List<UploadDocumentDTO> billingWorksheetDeletedList;
	
	private List<UploadDocumentDTO> billingWorkSheetUploadDocumentList;
	
	private Long rodBillSummaryKey;
	
	
	private SelectValue claimType;
	
	
	/***
	 * Created for reconsider ROD request. 
	 */
	private Long hospitalCashReimbursementBenefitsKey;
	
	private Long patientCareReimbursementBenefitsKey;
	
	private List<PatientCareDTO> reconsiderationPatientCareDTOList;
	
	private String dateOfAdmission;
	
	private String dateOfDischarge;
	
	private String intimationNo;
	
	private String insuredPatientName;
	
	private int iNoOfEmptyRows;
	
	private Boolean emptyRowStatus;
	
	private Date admissionDate;
	
	private Date dischargeDate;
	
	private List<DMSDocumentDetailsDTO> dmsDocumentDTOList ;
	
	private String claimNo;
	
	
	private Long rodKey;
	
	private String strUserName;
	
	private Boolean isBillSaved;
	
	private SelectValue roomCategory;
	
	
	private List<UploadDocumentDTO> uploadDocsList;
	
	private List<UploadDocumentDTO> alreadyUploadDocsList;
	
	private String rodOrReferenceNo;

	
	private Boolean isEdit = false;
	
	private String remarksBillEntry;

	/**
	 * Below variables added for
	 * already upload documents table
	 * enhancement.
	 * 
	 * */
	
	private Long ackDocKey;
	
	private Long docDetailsKey;
	
	
	private Boolean isAlreadyUploaded ;
	
	private String claimTypeValue;
	
	private Long dmsToken;
	
	private Boolean domicillaryFlag;
	
	private Boolean physicalDocumentReceived;
	
	private Boolean physicalDocumentIgnored;
	
	private SelectValue particulars;
	
	private List<HopsitalCashBenefitDTO> hopsitalCashBenefitDTO;
	
	private String uploadedBy;
	
	//private Boolean isRodSelectedForAddingDocuments;
	
	//private String documentToken;
	
	
	
//	//private Map buttonMap;
//	
//	public Map getButtonMap() {
//		return buttonMap;
//	}
//
//	public void setButtonMap(Map buttonMap) {
//		this.buttonMap = buttonMap;
//	}

	public Boolean getDomicillaryFlag() {
		return domicillaryFlag;
	}

	public SelectValue getRoomCategory() {
		return roomCategory;
	}

	public void setRoomCategory(SelectValue roomCategory) {
		this.roomCategory = roomCategory;
	}

	public void setDomicillaryFlag(Boolean domicillaryFlag) {
		this.domicillaryFlag = domicillaryFlag;
	}

	public Long getDmsToken() {
		return dmsToken;
	}

	public void setDmsToken(Long dmsToken) {
		this.dmsToken = dmsToken;
	}

	public String getClaimTypeValue() {
		return claimTypeValue;
	}

	public void setClaimTypeValue(String claimTypeValue) {
		this.claimTypeValue = claimTypeValue;
	}

	public Long getDocDetailsKey() {
		return docDetailsKey;
	}

	public String getRodOrReferenceNo() {
		return rodOrReferenceNo;
	}

	public void setRodOrReferenceNo(String rodOrReferenceNo) {
		this.rodOrReferenceNo = rodOrReferenceNo;
	}

	public void setDocDetailsKey(Long docDetailsKey) {
		this.docDetailsKey = docDetailsKey;
	}

	public List<UploadDocumentDTO> getUploadDocsList() {
		return uploadDocsList;
	}

	public void setUploadDocsList(List<UploadDocumentDTO> uploadDocsList) {
		this.uploadDocsList = uploadDocsList;
	}

	public List<UploadDocumentDTO> getDeletedDocumentList() {
		return deletedDocumentList;
	}

	public void setDeletedDocumentList(List<UploadDocumentDTO> deletedDocumentList) {
		this.deletedDocumentList = deletedDocumentList;
	}

	public UploadDocumentDTO ()
	{
		billEntryDetailsDTO = new BillEntryDetailsDTO();
//		buttonMap = new HashMap();
		//fileUpload = new FileUploadComponent();
	}
	
	public Boolean getSelectForBillEntry() {
		return selectForBillEntry;
	}

	public void setSelectForBillEntry(Boolean selectForBillEntry) {
		this.selectForBillEntry = selectForBillEntry;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	
	
	

	/**
	 * @return the fileType
	 */
	public SelectValue getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(SelectValue fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return the billNo
	 */
	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the billDate
	 */
	public Date getBillDate() {
		
		//String tempDate = new SimpleDateFormat("dd/MM/yyyy").format(billDate);
		
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(Date billDate) {
		
//		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
//		try {
//			this.billDate  = sdf.parse(String.valueOf(billDate));
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		this.billDate = billDate;
	}

	/**
	 * @return the noOfItems
	 */
	public Long getNoOfItems() {
		return noOfItems;
	}

	/**
	 * @param noOfItems the noOfItems to set
	 */
	public void setNoOfItems(Long noOfItems) {
		this.noOfItems = noOfItems;
	}

	

	/**
	 * @return the fileUpload
	 *//*
	public File getFileUpload() {
		return fileUpload;
	}

	*//**
	 * @param fileUpload the fileUpload to set
	 *//*
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}*/

	/**
	 * @return the billValue
	 */
	public Double getBillValue() {
		return billValue;
	}

	/**
	 * @param billValue the billValue to set
	 */
	public void setBillValue(Double billValue) {
		this.billValue = billValue;
	}

	/**
	 * @return the fileTypeValue
	 */
	public String getFileTypeValue() {
		return fileTypeValue;
	}

	/**
	 * @param fileTypeValue the fileTypeValue to set
	 */
	public void setFileTypeValue(String fileTypeValue) {
		this.fileTypeValue = fileTypeValue;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the rodNo
	 */
	public String getRodNo() {
		return rodNo;
	}

	/**
	 * @param rodNo the rodNo to set
	 */
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}

	/**
	 * @return the fileUpload
	 */
	/*public String getFileUpload() {
		return fileUpload;
	}

	*//**
	 * @param fileUpload the fileUpload to set
	 *//*
	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}*/

	/**
	 * @return the billEntryDetailList
	 */
	public List<BillEntryDetailsDTO> getBillEntryDetailList() {
		return billEntryDetailList;
	}

	/**
	 * @param billEntryDetailList the billEntryDetailList to set
	 */
	public void setBillEntryDetailList(List<BillEntryDetailsDTO> billEntryDetailList) {
		this.billEntryDetailList = billEntryDetailList;
	}

	/**
	 * @return the docSummaryKey
	 */
	public Long getDocSummaryKey() {
		return docSummaryKey;
	}

	/**
	 * @param docSummaryKey the docSummaryKey to set
	 */
	public void setDocSummaryKey(Long docSummaryKey) {
		this.docSummaryKey = docSummaryKey;
	}

	/**
	 * @return the totalClaimedAmount
	 */
	public Double getTotalClaimedAmount() {
		return totalClaimedAmount;
	}

	/**
	 * @param totalClaimedAmount the totalClaimedAmount to set
	 */
	public void setTotalClaimedAmount(Double totalClaimedAmount) {
		this.totalClaimedAmount = totalClaimedAmount;
	}
	/*public Boolean getHospitalCashAddonBenefits() {
		return hospitalCashAddonBenefits;
	}



	public void setHospitalCashAddonBenefits(Boolean hospitalCashAddonBenefits) {
		this.hospitalCashAddonBenefits = hospitalCashAddonBenefits;
	}



	public Boolean getPatientCareAddOnBenefits() {
		return patientCareAddOnBenefits;
	}



	public void setPatientCareAddOnBenefits(Boolean patientCareAddOnBenefits) {
		this.patientCareAddOnBenefits = patientCareAddOnBenefits;
	}



	public String getHospitalCashAddonBenefitsFlag() {
		return hospitalCashAddonBenefitsFlag;
	}



	public void setHospitalCashAddonBenefitsFlag(
			String hospitalCashAddonBenefitsFlag) {
		this.hospitalCashAddonBenefitsFlag = hospitalCashAddonBenefitsFlag;
		if(this.hospitalCashAddonBenefitsFlag != null) {
			this.hospitalCashAddonBenefits = this.hospitalCashAddonBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
		
	}

	public String getPatientCareAddOnBenefitsFlag() {
		return patientCareAddOnBenefitsFlag;
	}

	public void setPatientCareAddOnBenefitsFlag(String patientCareAddOnBenefitsFlag) {
		this.patientCareAddOnBenefitsFlag = patientCareAddOnBenefitsFlag;
		if(this.patientCareAddOnBenefitsFlag != null) {
			this.patientCareAddOnBenefits = this.patientCareAddOnBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}


	public String getHospitalCashNoofDays() {
		return hospitalCashNoofDays;
	}



	public void setHospitalCashNoofDays(String hospitalCashNoofDays) {
		this.hospitalCashNoofDays = hospitalCashNoofDays;
	}



	public String getHospitalCashPerDayAmt() {
		return hospitalCashPerDayAmt;
	}



	public void setHospitalCashPerDayAmt(String hospitalCashPerDayAmt) {
		this.hospitalCashPerDayAmt = hospitalCashPerDayAmt;
	}



	public String getHospitalCashTotalClaimedAmt() {
		return hospitalCashTotalClaimedAmt;
	}



	public void setHospitalCashTotalClaimedAmt(String hospitalCashTotalClaimedAmt) {
		this.hospitalCashTotalClaimedAmt = hospitalCashTotalClaimedAmt;
	}



	public String getPatientCareNoofDays() {
		return patientCareNoofDays;
	}



	public void setPatientCareNoofDays(String patientCareNoofDays) {
		this.patientCareNoofDays = patientCareNoofDays;
	}



	public String getPatientCarePerDayAmt() {
		return patientCarePerDayAmt;
	}



	public void setPatientCarePerDayAmt(String patientCarePerDayAmt) {
		this.patientCarePerDayAmt = patientCarePerDayAmt;
	}



	public String getPatientCareTotalClaimedAmt() {
		return patientCareTotalClaimedAmt;
	}



	public void setPatientCareTotalClaimedAmt(String patientCareTotalClaimedAmt) {
		this.patientCareTotalClaimedAmt = patientCareTotalClaimedAmt;
	}



	public Boolean getTreatmentPhysiotherapy() {
		return treatmentPhysiotherapy;
	}



	public void setTreatmentPhysiotherapy(Boolean treatmentPhysiotherapy) {
		this.treatmentPhysiotherapy = treatmentPhysiotherapy;
	}



	public String getTreatmentPhysiotherapyFlag() {
		return treatmentPhysiotherapyFlag;
	}



	public void setTreatmentPhysiotherapyFlag(String treatmentPhysiotherapyFlag) {
		this.treatmentPhysiotherapyFlag = treatmentPhysiotherapyFlag;
	}



	public List<PatientCareDTO> getPatientCareDTO() {
		return patientCareDTO;
	}



	public void setPatientCareDTO(List<PatientCareDTO> patientCareDTO) {
		this.patientCareDTO = patientCareDTO;
	}
*/

	public Boolean getHospitalCashAddonBenefits() {
		return hospitalCashAddonBenefits;
	}

	public void setHospitalCashAddonBenefits(Boolean hospitalCashAddonBenefits) {
		this.hospitalCashAddonBenefits = hospitalCashAddonBenefits;
		this.setHospitalCashAddonBenefitsFlag((this.hospitalCashAddonBenefits != null && this.hospitalCashAddonBenefits) ? "Y" : "N" );
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public Boolean getPatientCareAddOnBenefits() {
		return patientCareAddOnBenefits;
	}

	public void setPatientCareAddOnBenefits(Boolean patientCareAddOnBenefits) {
		this.patientCareAddOnBenefits = patientCareAddOnBenefits;
		this.setPatientCareAddOnBenefitsFlag((this.patientCareAddOnBenefits != null && this.patientCareAddOnBenefits) ? "Y" : "N" );
	}

	public String getHospitalCashAddonBenefitsFlag() {
		return hospitalCashAddonBenefitsFlag;
	}

	public void setHospitalCashAddonBenefitsFlag(
			String hospitalCashAddonBenefitsFlag) {
		this.hospitalCashAddonBenefitsFlag = hospitalCashAddonBenefitsFlag;
		if(this.hospitalCashAddonBenefitsFlag != null) {
			this.hospitalCashAddonBenefits = this.hospitalCashAddonBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getPatientCareAddOnBenefitsFlag() {
		return patientCareAddOnBenefitsFlag;
	}

	public void setPatientCareAddOnBenefitsFlag(String patientCareAddOnBenefitsFlag) {
		this.patientCareAddOnBenefitsFlag = patientCareAddOnBenefitsFlag;
		if(this.patientCareAddOnBenefitsFlag != null) {
			this.patientCareAddOnBenefits = this.patientCareAddOnBenefitsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getHospitalCashNoofDays() {
		return hospitalCashNoofDays;
	}

	public void setHospitalCashNoofDays(String hospitalCashNoofDays) {
		this.hospitalCashNoofDays = hospitalCashNoofDays;
	}

	public String getHospitalCashPerDayAmt() {
		return hospitalCashPerDayAmt;
	}

	public void setHospitalCashPerDayAmt(String hospitalCashPerDayAmt) {
		this.hospitalCashPerDayAmt = hospitalCashPerDayAmt;
	}

	public String getHospitalCashTotalClaimedAmt() {
		return hospitalCashTotalClaimedAmt;
	}

	public void setHospitalCashTotalClaimedAmt(String hospitalCashTotalClaimedAmt) {
		this.hospitalCashTotalClaimedAmt = hospitalCashTotalClaimedAmt;
	}

	public String getPatientCareNoofDays() {
		return patientCareNoofDays;
	}

	public void setPatientCareNoofDays(String patientCareNoofDays) {
		this.patientCareNoofDays = patientCareNoofDays;
	}

	public String getPatientCarePerDayAmt() {
		return patientCarePerDayAmt;
	}

	public void setPatientCarePerDayAmt(String patientCarePerDayAmt) {
		this.patientCarePerDayAmt = patientCarePerDayAmt;
	}

	public String getPatientCareTotalClaimedAmt() {
		return patientCareTotalClaimedAmt;
	}

	public void setPatientCareTotalClaimedAmt(String patientCareTotalClaimedAmt) {
		this.patientCareTotalClaimedAmt = patientCareTotalClaimedAmt;
	}

	public Boolean getTreatmentPhysiotherapy() {
		return treatmentPhysiotherapy;
	}

	public void setTreatmentPhysiotherapy(Boolean treatmentPhysiotherapy) {
		this.treatmentPhysiotherapy = treatmentPhysiotherapy;
		this.setTreatmentPhysiotherapyFlag((this.treatmentPhysiotherapy != null && this.treatmentPhysiotherapy) ? "Y" : "N" );
	}

	public String getTreatmentPhysiotherapyFlag() {
		return treatmentPhysiotherapyFlag;
	}

	public void setTreatmentPhysiotherapyFlag(String treatmentPhysiotherapyFlag) {
		this.treatmentPhysiotherapyFlag = treatmentPhysiotherapyFlag;
		if(this.treatmentPhysiotherapyFlag != null) {
			this.treatmentPhysiotherapy = this.treatmentPhysiotherapyFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public List<PatientCareDTO> getPatientCareDTO() {
		return patientCareDTO;
	}

	public void setPatientCareDTO(List<PatientCareDTO> patientCareDTO) {
		this.patientCareDTO = patientCareDTO;
	}

/*	public Long getReimbursementBenefitsKey() {
		return reimbursementBenefitsKey;
	}

	public void setReimbursementBenefitsKey(Long reimbursementBenefitsKey) {
		this.reimbursementBenefitsKey = reimbursementBenefitsKey;
	}
*/
	

	public String getHospitalBenefitFlag() {
		return hospitalBenefitFlag;
	}

	public void setHospitalBenefitFlag(String hospitalBenefitFlag) {
		this.hospitalBenefitFlag = hospitalBenefitFlag;
	}

	public String getPatientCareBenefitFlag() {
		return patientCareBenefitFlag;
	}

	public void setPatientCareBenefitFlag(String patientCareBenefitFlag) {
		this.patientCareBenefitFlag = patientCareBenefitFlag;
	}

	public Long getHospitalBenefitKey() {
		return hospitalBenefitKey;
	}

	public void setHospitalBenefitKey(Long hospitalBenefitKey) {
		this.hospitalBenefitKey = hospitalBenefitKey;
	}

	public Long getPatientBenefitKey() {
		return patientBenefitKey;
	}

	public void setPatientBenefitKey(Long patientBenefitKey) {
		this.patientBenefitKey = patientBenefitKey;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public BillEntryDetailsDTO getBillEntryDetailsDTO() {
		return billEntryDetailsDTO;
	}

	public void setBillEntryDetailsDTO(BillEntryDetailsDTO billEntryDetailsDTO) {
		this.billEntryDetailsDTO = billEntryDetailsDTO;
	}

	/*public String getStrfileUpload() {
		return strfileUpload;
	}

	public void setStrfileUpload(String strfileUpload) {
		this.strfileUpload = strfileUpload;
	}*/

	

	public String getDmsDocToken() {
		return dmsDocToken;
	}

	public void setDmsDocToken(String dmsDocToken) {
		this.dmsDocToken = dmsDocToken;
	}

	public String getZonalRemarks() {
		return zonalRemarks;
	}

	public void setZonalRemarks(String zonalRemarks) {
		this.zonalRemarks = zonalRemarks;
	}

	public String getCorporateRemarks() {
		return corporateRemarks;
	}

	public void setCorporateRemarks(String corporateRemarks) {
		this.corporateRemarks = corporateRemarks;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Double getProductBasedRoomRent() {
		return productBasedRoomRent;
	}

	public void setProductBasedRoomRent(Double productBasedRoomRent) {
		this.productBasedRoomRent = productBasedRoomRent;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getHospitalizationFlag() {
		return hospitalizationFlag;
	}

	public void setHospitalizationFlag(String hospitalizationFlag) {
		this.hospitalizationFlag = hospitalizationFlag;
	}

	public String getPreHospitalizationFlag() {
		return preHospitalizationFlag;
	}

	public void setPreHospitalizationFlag(String preHospitalizationFlag) {
		this.preHospitalizationFlag = preHospitalizationFlag;
	}

	public String getPostHospitalizationFlag() {
		return postHospitalizationFlag;
	}

	public void setPostHospitalizationFlag(String postHospitalizationFlag) {
		this.postHospitalizationFlag = postHospitalizationFlag;
	}

	public String getHospitalizationRepeatFlag() {
		return hospitalizationRepeatFlag;
	}

	public void setHospitalizationRepeatFlag(String hospitalizationRepeatFlag) {
		this.hospitalizationRepeatFlag = hospitalizationRepeatFlag;
	}

	public String getPartialHospitalizationFlag() {
		return partialHospitalizationFlag;
	}

	public void setPartialHospitalizationFlag(String partialHospitalizationFlag) {
		this.partialHospitalizationFlag = partialHospitalizationFlag;
	}

	public Double getProductBasedICURent() {
		return productBasedICURent;
	}

	public void setProductBasedICURent(Double productBasedICURent) {
		this.productBasedICURent = productBasedICURent;
	}

	public Double getProductBasedICCURent() {
		return productBasedICCURent;
	}

	public void setProductBasedICCURent(Double productBasedICCURent) {
		this.productBasedICCURent = productBasedICCURent;
	}

	public Double getProductBasedAmbulanceAmt() {
		return productBasedAmbulanceAmt;
	}

	public void setProductBasedAmbulanceAmt(Double productBasedAmbulanceAmt) {
		this.productBasedAmbulanceAmt = productBasedAmbulanceAmt;
	}

	public Boolean getEnableOrDisableBtn() {
		return enableOrDisableBtn;
	}

	public void setEnableOrDisableBtn(Boolean enableOrDisableBtn) {
		this.enableOrDisableBtn = enableOrDisableBtn;
	}

	public List<BillEntryDetailsDTO> getDeletedBillList() {
		return deletedBillList;
	}

	public void setDeletedBillList(List<BillEntryDetailsDTO> deletedBillList) {
		this.deletedBillList = deletedBillList;
	}

	public String getBillingWorkSheetRemarks() {
		return billingWorkSheetRemarks;
	}

	public void setBillingWorkSheetRemarks(String billingWorkSheetRemarks) {
		this.billingWorkSheetRemarks = billingWorkSheetRemarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<UploadDocumentDTO> getBillingWorksheetDeletedList() {
		return billingWorksheetDeletedList;
	}

	public void setBillingWorksheetDeletedList(
			List<UploadDocumentDTO> billingWorksheetDeletedList) {
		this.billingWorksheetDeletedList = billingWorksheetDeletedList;
	}

	public Long getRodBillSummaryKey() {
		return rodBillSummaryKey;
	}

	public void setRodBillSummaryKey(Long rodBillSummaryKey) {
		this.rodBillSummaryKey = rodBillSummaryKey;
	}

	public List<UploadDocumentDTO> getBillingWorkSheetUploadDocumentList() {
		return billingWorkSheetUploadDocumentList;
	}

	public void setBillingWorkSheetUploadDocumentList(
			List<UploadDocumentDTO> billingWorkSheetUploadDocumentList) {
		this.billingWorkSheetUploadDocumentList = billingWorkSheetUploadDocumentList;
	}

	public Long getHospitalCashReimbursementBenefitsKey() {
		return hospitalCashReimbursementBenefitsKey;
	}

	public void setHospitalCashReimbursementBenefitsKey(
			Long hospitalCashReimbursementBenefitsKey) {
		this.hospitalCashReimbursementBenefitsKey = hospitalCashReimbursementBenefitsKey;
	}

	public Long getPatientCareReimbursementBenefitsKey() {
		return patientCareReimbursementBenefitsKey;
	}

	public void setPatientCareReimbursementBenefitsKey(
			Long patientCareReimbursementBenefitsKey) {
		this.patientCareReimbursementBenefitsKey = patientCareReimbursementBenefitsKey;
	}

	public List<PatientCareDTO> getReconsiderationPatientCareDTOList() {
		return reconsiderationPatientCareDTOList;
	}

	public void setReconsiderationPatientCareDTOList(
			List<PatientCareDTO> reconsiderationPatientCareDTOList) {
		this.reconsiderationPatientCareDTOList = reconsiderationPatientCareDTOList;
	}

	public Boolean getTreatmentGovtHospital() {
		return treatmentGovtHospital;
	}

	public void setTreatmentGovtHospital(Boolean treatmentGovtHospital) {
		this.treatmentGovtHospital = treatmentGovtHospital;
		this.setTreatmentGovtHospFlag((this.treatmentGovtHospital != null && this.treatmentGovtHospital) ? "Y" : "N" );
	}

	public String getTreatmentGovtHospFlag() {
		return treatmentGovtHospFlag;
	}

	public void setTreatmentGovtHospFlag(String treatmentGovtHospFlag) {
		this.treatmentGovtHospFlag = treatmentGovtHospFlag;
		if(this.treatmentGovtHospFlag != null) {
			this.treatmentGovtHospital = this.treatmentGovtHospFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}

	public int getiNoOfEmptyRows() {
		return iNoOfEmptyRows;
	}

	public void setiNoOfEmptyRows(int iNoOfEmptyRows) {
		this.iNoOfEmptyRows = iNoOfEmptyRows;
	}

	public Boolean getEmptyRowStatus() {
		return emptyRowStatus;
	}

	public void setEmptyRowStatus(Boolean emptyRowStatus) {
		this.emptyRowStatus = emptyRowStatus;
	}

	public SelectValue getClaimType() {
		return claimType;
	}

	public void setClaimType(SelectValue claimType) {
		this.claimType = claimType;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public List<DMSDocumentDetailsDTO> getDmsDocumentDTOList() {
		return dmsDocumentDTOList;
	}

	public void setDmsDocumentDTOList(List<DMSDocumentDetailsDTO> dmsDocumentDTOList) {
		this.dmsDocumentDTOList = dmsDocumentDTOList;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public Boolean getIsBillSaved() {
		return isBillSaved;
	}

	public void setIsBillSaved(Boolean isBillSaved) {
		this.isBillSaved = isBillSaved;
	}

	public Long getAckDocKey() {
		return ackDocKey;
	}

	public void setAckDocKey(Long ackDocKey) {
		this.ackDocKey = ackDocKey;
	}
	
	public List<UploadDocumentDTO> getAlreadyUploadDocsList() {
		return alreadyUploadDocsList;
	}

	public void setAlreadyUploadDocsList(
			List<UploadDocumentDTO> alreadyUploadDocsList) {
		this.alreadyUploadDocsList = alreadyUploadDocsList;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Boolean getIsAlreadyUploaded() {
		return isAlreadyUploaded;
	}

	public void setIsAlreadyUploaded(Boolean isAlreadyUploaded) {
		this.isAlreadyUploaded = isAlreadyUploaded;
	}

	public SelectValue getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(SelectValue referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getReferenceNoValue() {
		return referenceNoValue;
	}

	public void setReferenceNoValue(String referenceNoValue) {
		this.referenceNoValue = referenceNoValue;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getsNo() {
		return sNo;
	}

	public void setsNo(String sNo) {
		this.sNo = sNo;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Long getOtherBenefitsId() {
		return otherBenefitsId;
	}

	public void setOtherBenefitsId(Long otherBenefitsId) {
		this.otherBenefitsId = otherBenefitsId;
	}

	public Boolean getIsSharedAccomotation() {
		return isSharedAccomotation;
	}

	public void setIsSharedAccomotation(Boolean isSharedAccomotation) {
		this.isSharedAccomotation = isSharedAccomotation;
	}

	public Boolean getIsTreatementForPreferred() {
		return isTreatementForPreferred;
	}

	public void setIsTreatementForPreferred(Boolean isTreatementForPreferred) {
		this.isTreatementForPreferred = isTreatementForPreferred;
	}

	public Boolean getIsRepatriationOfMortal() {
		return isRepatriationOfMortal;
	}

	public void setIsRepatriationOfMortal(Boolean isRepatriationOfMortal) {
		this.isRepatriationOfMortal = isRepatriationOfMortal;
	}

	public Boolean getIsCompassionateTravel() {
		return isCompassionateTravel;
	}

	public void setIsCompassionateTravel(Boolean isCompassionateTravel) {
		this.isCompassionateTravel = isCompassionateTravel;
	}

	public Boolean getIsEmergencyDomestic() {
		return isEmergencyDomestic;
	}

	public void setIsEmergencyDomestic(Boolean isEmergencyDomestic) {
		this.isEmergencyDomestic = isEmergencyDomestic;
	}

	public Boolean getOtherBenefit() {
		return otherBenefit;
	}

	public void setOtherBenefit(Boolean otherBenefit) {
		this.otherBenefit = otherBenefit;		
		this.setOtherBenefitFlag((this.otherBenefit != null && this.otherBenefit) ? "Y" : "N");
	}

	public String getOtherBenefitFlag() {
		return otherBenefitFlag;
	}

	public void setOtherBenefitFlag(String otherBenefitFlag) {
		this.otherBenefitFlag = otherBenefitFlag;
		if(this.otherBenefitFlag != null) {
			this.otherBenefit = this.otherBenefitFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Boolean getEmergencyMedicalEvaluation() {
		return emergencyMedicalEvaluation;
	}

	public void setEmergencyMedicalEvaluation(Boolean emergencyMedicalEvaluation) {
		this.emergencyMedicalEvaluation = emergencyMedicalEvaluation;
		this.setEmergencyMedicalEvaluationFlag((this.emergencyMedicalEvaluation != null && this.emergencyMedicalEvaluation) ? "Y" : "N");
	}

	public Boolean getCompassionateTravel() {
		return compassionateTravel;
	}

	public void setCompassionateTravel(Boolean compassionateTravel) {
		this.compassionateTravel = compassionateTravel;
		this.setCompassionateTravelFlag((this.compassionateTravel != null && this.compassionateTravel) ? "Y" : "N");
	}

	public Boolean getRepatriationOfMortalRemains() {
		return repatriationOfMortalRemains;
	}

	public void setRepatriationOfMortalRemains(Boolean repatriationOfMortalRemains) {
		this.repatriationOfMortalRemains = repatriationOfMortalRemains;
		this.setRepatriationOfMortalRemainsFlag((this.repatriationOfMortalRemains != null && this.repatriationOfMortalRemains) ? "Y" : "N");
	}

	public Boolean getPreferredNetworkHospital() {
		return preferredNetworkHospital;
	}

	public void setPreferredNetworkHospital(Boolean preferredNetworkHospital) {
		this.preferredNetworkHospital = preferredNetworkHospital;
		this.setPreferredNetworkHospitalFlag((this.preferredNetworkHospital != null && this.preferredNetworkHospital) ? "Y" : "N");
	}

	public Boolean getSharedAccomodation() {
		return sharedAccomodation;
	}

	public void setSharedAccomodation(Boolean sharedAccomodation) {
		this.sharedAccomodation = sharedAccomodation;
		this.setSharedAccomodationFlag((this.sharedAccomodation != null && this.sharedAccomodation) ? "Y" : "N");
	}

	public String getEmergencyMedicalEvaluationFlag() {
		return emergencyMedicalEvaluationFlag;
	}

	public void setEmergencyMedicalEvaluationFlag(
			String emergencyMedicalEvaluationFlag) {
		this.emergencyMedicalEvaluationFlag = emergencyMedicalEvaluationFlag;
		if(this.repatriationOfMortalRemainsFlag != null) {
			this.repatriationOfMortalRemains = this.repatriationOfMortalRemainsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getCompassionateTravelFlag() {
		return compassionateTravelFlag;
	}

	public void setCompassionateTravelFlag(String compassionateTravelFlag) {
		this.compassionateTravelFlag = compassionateTravelFlag;
		if(this.compassionateTravelFlag != null) {
			this.compassionateTravel = this.compassionateTravelFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getRepatriationOfMortalRemainsFlag() {
		return repatriationOfMortalRemainsFlag;
	}

	public void setRepatriationOfMortalRemainsFlag(
			String repatriationOfMortalRemainsFlag) {
		this.repatriationOfMortalRemainsFlag = repatriationOfMortalRemainsFlag;
		if(this.repatriationOfMortalRemainsFlag != null) {
			this.repatriationOfMortalRemains = this.repatriationOfMortalRemainsFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getPreferredNetworkHospitalFlag() {
		return preferredNetworkHospitalFlag;
	}

	public void setPreferredNetworkHospitalFlag(String preferredNetworkHospitalFlag) {
		this.preferredNetworkHospitalFlag = preferredNetworkHospitalFlag;
		if(this.preferredNetworkHospitalFlag != null) {
			this.preferredNetworkHospital = this.preferredNetworkHospitalFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public String getSharedAccomodationFlag() {
		return sharedAccomodationFlag;
	}

	public void setSharedAccomodationFlag(String sharedAccomodationFlag) {
		this.sharedAccomodationFlag = sharedAccomodationFlag;
		if(this.sharedAccomodationFlag != null) {
			this.sharedAccomodation = this.sharedAccomodationFlag.toLowerCase().equalsIgnoreCase("y") ? true : false;
		}
	}

	public Boolean getPhysicalDocumentReceived() {
		return physicalDocumentReceived;
	}

	public void setPhysicalDocumentReceived(Boolean physicalDocumentReceived) {
		this.physicalDocumentReceived = physicalDocumentReceived;
	}

	public Boolean getPhysicalDocumentIgnored() {
		return physicalDocumentIgnored;
	}

	public void setPhysicalDocumentIgnored(Boolean physicalDocumentIgnored) {
		this.physicalDocumentIgnored = physicalDocumentIgnored;
	}

	public Boolean getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(Boolean isReceived) {
		this.isReceived = isReceived;
	}

	public Boolean getIsIgnored() {
		return isIgnored;
	}

	public void setIsIgnored(Boolean isIgnored) {
		this.isIgnored = isIgnored;
	}

	public SelectValue getDocumentType() {
		return documentType;
	}

	public void setDocumentType(SelectValue documentType) {
		this.documentType = documentType;
	}

	public Date getDocReceivedDate() {
		return docReceivedDate;
	}

	public void setDocReceivedDate(Date docReceivedDate) {
		this.docReceivedDate = docReceivedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public SelectValue getReceivStatus() {
		return receivStatus;
	}

	public void setReceivStatus(SelectValue receivStatus) {
		this.receivStatus = receivStatus;
	}

	public BeanItemContainer<SelectValue> getFileTypeContainer() {
		return fileTypeContainer;
	}

	public void setFileTypeContainer(BeanItemContainer<SelectValue> fileTypeContainer) {
		this.fileTypeContainer = fileTypeContainer;
	}

	public String getReceivStatusValue() {
		return receivStatusValue;
	}

	public void setReceivStatusValue(String receivStatusValue) {
		this.receivStatusValue = receivStatusValue;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getDocumentTypeValue() {
		return documentTypeValue;
	}

	public void setDocumentTypeValue(String documentTypeValue) {
		this.documentTypeValue = documentTypeValue;
	}

	public Long getRodVersion() {
		return rodVersion;
	}

	public void setRodVersion(Long rodVersion) {
		this.rodVersion = rodVersion;
	}

	public Long getDocREceivedFromId() {
		return docREceivedFromId;
	}

	public void setDocREceivedFromId(Long docREceivedFromId) {
		this.docREceivedFromId = docREceivedFromId;
	}

	public SelectValue getValChangedRoomCategory() {
		return valChangedRoomCategory;
	}

	public void setValChangedRoomCategory(SelectValue valChangedRoomCategory) {
		this.valChangedRoomCategory = valChangedRoomCategory;
	}

	public SelectValue getParticulars() {
		return particulars;
	}

	public void setParticulars(SelectValue particulars) {
		this.particulars = particulars;
	}

	public List<HopsitalCashBenefitDTO> getHopsitalCashBenefitDTO() {
		return hopsitalCashBenefitDTO;
	}

	public void setHopsitalCashBenefitDTO(
			List<HopsitalCashBenefitDTO> hopsitalCashBenefitDTO) {
		this.hopsitalCashBenefitDTO = hopsitalCashBenefitDTO;
	}

	public Long getBillAmt() {
		return billAmt;
	}

	public void setBillAmt(Long billAmt) {
		this.billAmt = billAmt;
	}

	public Long getDeductibleAmt() {
		return deductibleAmt;
	}

	public void setDeductibleAmt(Long deductibleAmt) {
		this.deductibleAmt = deductibleAmt;
	}

	public Long getNonPaybleAmt() {
		return nonPaybleAmt;
	}

	public void setNonPaybleAmt(Long nonPaybleAmt) {
		this.nonPaybleAmt = nonPaybleAmt;
	}

	public Long getPaybleAmt() {
		return paybleAmt;
	}

	public void setPaybleAmt(Long paybleAmt) {
		this.paybleAmt = paybleAmt;
	}

	public Boolean getIsPhysicalDoc() {
		return isPhysicalDoc;
	}

	public void setIsPhysicalDoc(Boolean isPhysicalDoc) {
		this.isPhysicalDoc = isPhysicalDoc;
	}

	public Long getOpkey() {
		return opkey;
	}

	public void setOpkey(Long opkey) {
		this.opkey = opkey;
	}

	public Long getHospitalCashId() {
		return hospitalCashId;
	}

	public void setHospitalCashId(Long hospitalCashId) {
		this.hospitalCashId = hospitalCashId;
	}

	public String getFileUploadRemarks() {
		return fileUploadRemarks;
	}

	public void setFileUploadRemarks(String fileUploadRemarks) {
		this.fileUploadRemarks = fileUploadRemarks;
	}

	public String getAckDocumentSource() {
		return ackDocumentSource;
	}

	public void setAckDocumentSource(String ackDocumentSource) {
		this.ackDocumentSource = ackDocumentSource;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getRemarksBillEntry() {
		return remarksBillEntry;
	}

	public void setRemarksBillEntry(String remarksBillEntry) {
		this.remarksBillEntry = remarksBillEntry;
	}
	
	

	/*public Boolean getIsRodSelectedForAddingDocuments() {
		return isRodSelectedForAddingDocuments;
	}

	public void setIsRodSelectedForAddingDocuments(
			Boolean isRodSelectedForAddingDocuments) {
		this.isRodSelectedForAddingDocuments = isRodSelectedForAddingDocuments;
	}
*/
	

	/*public String getDateOfAdmission() {
		return dateOfAdmission;
	}

	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}

	public String getDateOfDischarge() {
		return dateOfDischarge;
	}

	public void setDateOfDischarge(String dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getInsuredPatientName() {
		return insuredPatientName;
	}

	public void setInsuredPatientName(String insuredPatientName) {
		this.insuredPatientName = insuredPatientName;
	}
*/
	
	

	/*public String getToken() {
		return dms;
	}

	public void setToken(String token) {
		this.dms = token;
	}*/
	
	

}