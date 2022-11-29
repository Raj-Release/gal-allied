/**
 * 
 */
package com.shaic.claim.rod.wizard.dto;

import java.util.Date;

import com.shaic.arch.fields.dto.SelectValue;

/**
 * @author ntv.vijayar
 *
 */
public class BillEntryDetailsDTO extends ViewBillSummaryRemarksDTO {
	
	private Long key;
	
	private String billNo = "";
	
	private Date billDate;
	
	private Long noOfItems;
	
	private Double billValue;
	
	private Long itemNo;
	
	private Double itemNoForView;
	
	//private RowHeaderMode itemNo;
	
	private String itemName;
	
	private SelectValue classification;
	
	private SelectValue category;
	
	private Double noOfDays;
	
	private Double perDayAmt;
	
	private Double itemValue;
	
	private Boolean billEntryStatus;
	
	private String billEntryStatusFlag ;
	
	private String zonalRemarks;
	
	private Boolean isAlreadyEntered = false;
	
	private Long documentSummaryKey ;
	
	private Long docReceivedFA;
	
	
	/**
	 * 
	 * Fields added for bill Entry popup shown in process claim billing screen.
	 * */
	
	private Double noOfDaysAllowed;
	
	private Double perDayAmtProductBased;
	
	private Double amountAllowableAmount;
	
	private Double nonPayableProductBased = 0d;
	
	private Double nonPayable;
	
	private Double reasonableDeduction;
	
	private Double totalDisallowances;
	
	private Double netPayableAmount;
	
	private String deductibleOrNonPayableReason;
	
	private String medicalRemarks;
	
	//Additional column only for Billing and FA screens
	private SelectValue roomType;
	
	private SelectValue irdaLevel1;
	
	private SelectValue irdaLevel2;
	
	private SelectValue irdaLevel3;
	
	private String billingRemarks;
	
	//Added for hospitalization view
	private String details;
	
	private Double proportionateDeduction;
	
	private String dateOfAdmission;
	
	private String dateOfDischarge;
	
	private String intimationNo;
	
	private String insuredPatientName;
	
	private Long billDetailsKey;
	
	
	//Added for scrc assessment .
	private Double roomRentAndNursingSubTotal;
	
	private Double icuRoomRentAndNursingSubTotal;
	
	
	
	private Double approvedAmountForAssessmentSheet;

	private String presenterString;
	
	private String dtoName;
	
	private String nonPayableRmrksForAssessmentSheet;
	
	private String strRoomType = "";
	
	private Boolean isOthersAvaiable = false;
	
	private Boolean proportionateDeductionappl = false;
	
	private Boolean isproportionateDeductionvisble = false;
	
	private Boolean isproportionateDeductionSelected = false;
	
	public Long getDocReceivedFA() {
		return docReceivedFA;
	}

	public void setDocReceivedFA(Long docReceivedFA) {
		this.docReceivedFA = docReceivedFA;
	}

	public String getPresenterString() {
		return presenterString;
	}

	public void setPresenterString(String presenterString) {
		this.presenterString = presenterString;
	}

	public SelectValue getRoomType() {
		return roomType;
	}

	public void setRoomType(SelectValue roomType) {
		this.roomType = roomType;
	}

	public Double getApprovedAmountForAssessmentSheet() {
		return approvedAmountForAssessmentSheet;
	}

	public void setApprovedAmountForAssessmentSheet(
			Double approvedAmountForAssessmentSheet) {
		this.approvedAmountForAssessmentSheet = approvedAmountForAssessmentSheet;
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

	private String corporateRemarks;

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
		return billDate;
	}

	/**
	 * @param billDate the billDate to set
	 */
	public void setBillDate(Date billDate) {
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
	 * @return the itemNo
	 */
	/*public Long getItemNo() {
		return itemNo;
	}

	*//**
	 * @param rowHeaderModeIndex the itemNo to set
	 *//*
	public void setItemNo(RowHeaderMode rowHeaderModeIndex) {
		this.itemNo = rowHeaderModeIndex;
	}*/

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the classification
	 */
	public SelectValue getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(SelectValue classification) {
		this.classification = classification;
	}

	public Boolean getIsAlreadyEntered() {
		return isAlreadyEntered;
	}

	public void setIsAlreadyEntered(Boolean isAlreadyEntered) {
		this.isAlreadyEntered = isAlreadyEntered;
	}

	/**
	 * @return the category
	 */
	public SelectValue getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(SelectValue category) {
		this.category = category;
	}

	/**
	 * @return the noOfDays
	 */
	public Double getNoOfDays() {
		return noOfDays;
	}

	/**
	 * @param noOfDays the noOfDays to set
	 */
	public void setNoOfDays(Double noOfDays) {
		this.noOfDays = noOfDays;
	}

	/**
	 * @return the perDayAmt
	 */
	public Double getPerDayAmt() {
		return perDayAmt;
	}

	/**
	 * @param perDayAmt the perDayAmt to set
	 */
	public void setPerDayAmt(Double perDayAmt) {
		this.perDayAmt = perDayAmt;
	}

	/**
	 * @return the itemValue
	 */
	public Double getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(Double itemValue) {
		this.itemValue = itemValue;
	}

	/**
	 * @return the billEntryStatus
	 */
	public Boolean getBillEntryStatus() {
		return billEntryStatus;
	}

	/**
	 * @param billEntryStatus the billEntryStatus to set
	 */
	public void setBillEntryStatus(Boolean billEntryStatus) {
		this.billEntryStatus = billEntryStatus;
		this.billEntryStatusFlag = (null != this.billEntryStatus && billEntryStatus) ? "Y":"N";
	}

	/**
	 * @return the billEntryStatusFlag
	 */
	public String getBillEntryStatusFlag() {
		return billEntryStatusFlag;
	}

	/**
	 * @param billEntryStatusFlag the billEntryStatusFlag to set
	 */
	public void setBillEntryStatusFlag(String billEntryStatusFlag) {
		this.billEntryStatusFlag = billEntryStatusFlag;
		if(null != this.billEntryStatusFlag && this.billEntryStatusFlag.equals("Y"))
			this.billEntryStatus = true;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getMedicalRemarks() {
		return medicalRemarks;
	}

	public void setMedicalRemarks(String medicalRemarks) {
		this.medicalRemarks = medicalRemarks;
	}

	public SelectValue getIrdaLevel1() {
		return irdaLevel1;
	}

	public void setIrdaLevel1(SelectValue irdaLevel1) {
		this.irdaLevel1 = irdaLevel1;
	}

	public SelectValue getIrdaLevel2() {
		return irdaLevel2;
	}

	public void setIrdaLevel2(SelectValue irdaLevel2) {
		this.irdaLevel2 = irdaLevel2;
	}

	public SelectValue getIrdaLevel3() {
		return irdaLevel3;
	}

	public void setIrdaLevel3(SelectValue irdaLevel3) {
		this.irdaLevel3 = irdaLevel3;
	}

	public Double getNoOfDaysAllowed() {
		return noOfDaysAllowed;
	}

	public void setNoOfDaysAllowed(Double noOfDaysAllowed) {
		this.noOfDaysAllowed = noOfDaysAllowed;
	}

	public Double getPerDayAmtProductBased() {
		return perDayAmtProductBased;
	}

	public void setPerDayAmtProductBased(Double perDayAmtProductBased) {
		this.perDayAmtProductBased = perDayAmtProductBased;
	}

	public Double getAmountAllowableAmount() {
		return amountAllowableAmount;
	}

	public void setAmountAllowableAmount(Double amountAllowableAmount) {
		this.amountAllowableAmount = amountAllowableAmount;
	}

	public Double getNonPayableProductBased() {
		return nonPayableProductBased;
	}

	public void setNonPayableProductBased(Double nonPayableProductBased) {
		this.nonPayableProductBased = nonPayableProductBased;
	}

	public Double getNonPayable() {
		return nonPayable;
	}

	public void setNonPayable(Double nonPayable) {
		this.nonPayable = nonPayable;
	}

	public Double getReasonableDeduction() {
		return reasonableDeduction;
	}

	public void setReasonableDeduction(Double reasonableDeduction) {
		this.reasonableDeduction = reasonableDeduction;
	}

	public Double getTotalDisallowances() {
		return totalDisallowances;
	}

	public void setTotalDisallowances(Double totalDisallowances) {
		this.totalDisallowances = totalDisallowances;
	}

	public Double getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(Double netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}

	public String getDeductibleOrNonPayableReason() {
		return deductibleOrNonPayableReason;
	}

	public void setDeductibleOrNonPayableReason(String deductibleOrNonPayableReason) {
		this.deductibleOrNonPayableReason = deductibleOrNonPayableReason;
	}

	public String getBillingRemarks() {
		return billingRemarks;
	}

	public void setBillingRemarks(String billingRemarks) {
		this.billingRemarks = billingRemarks;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Double getProportionateDeduction() {
		return proportionateDeduction;
	}

	public void setProportionateDeduction(Double proportionateDeduction) {
		this.proportionateDeduction = proportionateDeduction;
	}

	public Double getItemNoForView() {
		return itemNoForView;
	}

	public void setItemNoForView(Double itemNoForView) {
		this.itemNoForView = itemNoForView;
	}

	public Long getDocumentSummaryKey() {
		return documentSummaryKey;
	}

	public void setDocumentSummaryKey(Long documentSummaryKey) {
		this.documentSummaryKey = documentSummaryKey;
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

	public Long getBillDetailsKey() {
		return billDetailsKey;
	}

	public void setBillDetailsKey(Long billDetailsKey) {
		this.billDetailsKey = billDetailsKey;
	}

	public Double getRoomRentAndNursingSubTotal() {
		return roomRentAndNursingSubTotal;
	}

	public void setRoomRentAndNursingSubTotal(Double roomRentAndNursingSubTotal) {
		this.roomRentAndNursingSubTotal = roomRentAndNursingSubTotal;
	}

	public Double getIcuRoomRentAndNursingSubTotal() {
		return icuRoomRentAndNursingSubTotal;
	}

	public void setIcuRoomRentAndNursingSubTotal(
			Double icuRoomRentAndNursingSubTotal) {
		this.icuRoomRentAndNursingSubTotal = icuRoomRentAndNursingSubTotal;
	}

	public String getDtoName() {
		return dtoName;
	}

	public void setDtoName(String dtoName) {
		this.dtoName = dtoName;
	}

	public Boolean getIsOthersAvaiable() {
		return isOthersAvaiable;
	}

	public void setIsOthersAvaiable(Boolean isOthersAvaiable) {
		this.isOthersAvaiable = isOthersAvaiable;
	}

	public String getNonPayableRmrksForAssessmentSheet() {
		return nonPayableRmrksForAssessmentSheet;
	}

	public void setNonPayableRmrksForAssessmentSheet(
			String nonPayableRmrksForAssessmentSheet) {
		this.nonPayableRmrksForAssessmentSheet = nonPayableRmrksForAssessmentSheet;
	}

	public String getStrRoomType() {
		return strRoomType;
	}

	public void setStrRoomType(String strRoomType) {
		this.strRoomType = strRoomType;
	}

	public Boolean getProportionateDeductionappl() {
		return proportionateDeductionappl;
	}

	public void setProportionateDeductionappl(Boolean proportionateDeductionappl) {
		this.proportionateDeductionappl = proportionateDeductionappl;
	}

	public Boolean getIsproportionateDeductionvisble() {
		return isproportionateDeductionvisble;
	}

	public void setIsproportionateDeductionvisble(
			Boolean isproportionateDeductionvisble) {
		this.isproportionateDeductionvisble = isproportionateDeductionvisble;
	}

	public Boolean getIsproportionateDeductionSelected() {
		return isproportionateDeductionSelected;
	}

	public void setIsproportionateDeductionSelected(
			Boolean isproportionateDeductionSelected) {
		this.isproportionateDeductionSelected = isproportionateDeductionSelected;
	}

	/*public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
*/
	
}
