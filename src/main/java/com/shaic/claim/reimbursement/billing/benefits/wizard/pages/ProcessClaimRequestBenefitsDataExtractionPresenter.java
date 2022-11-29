/**
 * 
 */
package com.shaic.claim.reimbursement.billing.benefits.wizard.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.claim.reimbursement.billing.benefits.wizard.service.ProcessClaimRequestBenefitsService;
import com.shaic.claim.reimbursement.billing.dto.AddOnBenefitsDTO;
import com.shaic.claim.rod.wizard.dto.DocumentDetailsDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.ims.bpm.claim.DBCalculationService;

/**
 * @author ntv.vijayar
 *
 */
@ViewInterface(ProcessClaimRequestBenefitsDataExtractionView.class)
public class ProcessClaimRequestBenefitsDataExtractionPresenter extends AbstractMVPPresenter<ProcessClaimRequestBenefitsDataExtractionView> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4263979962443839239L;

	public static final String CLAIM_REQUEST_BENEFITS_SETUP_DROPDOWN_VALUES = "claim_request_benefits_setup_dropdown_values";
	
	public static final String CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_HOSPITAL_CASH = "claim_request_benefits_add_on_benefits_hospital_cash";
	
	public static final String CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_PATIENT_CARE= "claim_request_benefits_add_on_benefits_patient_care";
	
	public static final String CLAIM_REQUEST_BENEFITS_HOSPITAL_BENEFITS = "claim_request_billing_hospital_cash_benefits";
	
	public static final String CLAIM_REQUEST_BENEFITS_PATIENT_CARE_BENEFITS = "claim_request_billing_patiet_care_benefits";
	
	public static final String SAVE_PATIENT_CARE_TABLE_VALUES = "save_patient_care_table_values";
	
	public static final String SAVE_HOSPITAL_CASH_TABLE_VALUES = "save_hospital_cash_table_values";
	
	public static final String SAVE_HOSPITAL_CASH_PHC_TABLE_VALUES = "save_hospital_cash_phc_table_values";
	
	public static final String CLAIM_REQUEST_BENEFITS_PATIENT_DAYCARE_DROPDOWN_VALUES = "claim_request_benefits_patient_daycare_dropdown_values";
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService ackDocReceivedService;
	
	@EJB
	private ProcessClaimRequestBenefitsService benefitsService;
	
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	public void setUpDropDownValues(
			@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_SETUP_DROPDOWN_VALUES) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		referenceDataMap.put("docReceivedFrom", masterService
				.getSelectValueContainer(ReferenceTable.ACK_DOC_RECEIVED_FROM));	
		referenceDataMap.put("billClaissificationDetails",validateBillClassification(rodDTO.getClaimDTO().getKey()));
		
		//MED-PRD-076
		referenceDataMap.put("diagnosisHospitalCashContainer", masterService.getDiagnosisHospitalCash());
		referenceDataMap.put("hospitalCashDueTo", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_DUE_TO));
		referenceDataMap.put("patientDayCareValueContainer", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_PATIENT_DAY_CARE));
		
		view.setUpDropDownValues(referenceDataMap);
	
		}
	
	
	public void calculateNoOfDaysForHospitalCash(@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_HOSPITAL_CASH) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()), rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getLopFlag());
		List<Object> addOnBenefitsValues = dbCalculationService.getAddOnBenefitsValues(rodDTO.getDocumentDetails().getRodKey(), 
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(),sumInsured , rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey(),"HC");
		view.setUpHospitalCashValues(addOnBenefitsValues);
	
	}
	
	public void calculateNoOfDaysForPatientCare(@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_ADD_ON_BENEFITS_PATIENT_CARE) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Double sumInsured = dbCalculationService.getInsuredSumInsured(String.valueOf(rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getInsuredId()), rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getKey(),rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getLopFlag());
		List<Object> addOnBenefitsValues = dbCalculationService.getAddOnBenefitsValues(rodDTO.getDocumentDetails().getRodKey(), 
				rodDTO.getClaimDTO().getNewIntimationDto().getInsuredPatient().getKey(),sumInsured , rodDTO.getClaimDTO().getNewIntimationDto().getPolicy().getProduct().getKey(),"PC");
		view.setUpPatientCareValues(addOnBenefitsValues);
	}
	
	public void generateFieldsBasedHospitalCashAddOnBenefits(@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_HOSPITAL_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	public void generateFieldsBasedPatientCareAddOnBenefits(@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_PATIENT_CARE_BENEFITS) final ParameterDTO parameters)
	{
		Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		view.generateFieldsBasedOnPatientCareBenefits(selectedValue);
	}
	
	public DocumentDetailsDTO validateBillClassification(Long claimKey)
	{
		Reimbursement reimbursement = ackDocReceivedService.getLatestReimbursementDetails(claimKey);
		DocumentDetailsDTO docDTO = null;
		if(null != reimbursement)
		{
			DocAcknowledgement docAck= reimbursement.getDocAcknowLedgement();
			docDTO = new DocumentDetailsDTO();
			docDTO.setHospitalizationFlag(docAck.getHospitalisationFlag());
			docDTO.setPartialHospitalizationFlag(docAck.getPartialHospitalisationFlag());
			docDTO.setPreHospitalizationFlag(docAck.getPreHospitalisationFlag());
			docDTO.setPostHospitalizationFlag(docAck.getPostHospitalisationFlag());
			docDTO.setLumpSumAmountFlag(docAck.getLumpsumAmountFlag());
			docDTO.setAddOnBenefitsPatientCareFlag(docAck.getPatientCareFlag());
			docDTO.setAddOnBenefitsHospitalCashFlag(docAck.getHospitalCashFlag());
		}
		
		return docDTO;		
	}
	
	
	public void savePatientCareValues(@Observes @CDIEvent(SAVE_PATIENT_CARE_TABLE_VALUES) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.savePatientCareTableValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	
	public void saveHospitalCashValue(@Observes @CDIEvent(SAVE_HOSPITAL_CASH_TABLE_VALUES) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.saveHospitalCashValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}

	public void saveHospitalCashPhcValue(@Observes @CDIEvent(SAVE_HOSPITAL_CASH_PHC_TABLE_VALUES) final ParameterDTO parameters)
	{
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO)parameters.getPrimaryParameter();
		List<AddOnBenefitsDTO> addOnBenefitsDTO = benefitsService.saveHospitalCashPhcValues(rodDTO);
		view.setBenefitsData(addOnBenefitsDTO);
		//Boolean selectedValue = (Boolean) parameters.getPrimaryParameter();
		//view.generateFieldsBasedOnHospitalCashBenefits(selectedValue);
	}
	public void setUpDropDownValuesForPatientDayCare(
			@Observes @CDIEvent(CLAIM_REQUEST_BENEFITS_PATIENT_DAYCARE_DROPDOWN_VALUES) final ParameterDTO parameters) {
		ReceiptOfDocumentsDTO rodDTO = (ReceiptOfDocumentsDTO) parameters.getPrimaryParameter();
		Map<String, Object> referenceDataMap = new HashMap<String, Object>();
		
		//MED-PRD-076
		referenceDataMap.put("patientDayCareValueContainer", masterService.getSelectValueContainer(ReferenceTable.HOSPITAL_CASH_PATIENT_DAY_CARE));
		view.setUpDropDownValues(referenceDataMap);
	
		}
	
}
