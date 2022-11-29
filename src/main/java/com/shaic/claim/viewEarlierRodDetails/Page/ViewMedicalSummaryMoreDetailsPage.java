package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.view.DiagnosisService;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PedDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDataExtaractionDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.premedical.mapper.PreMedicalMapper;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.MedicalVerificationDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.viewEarlierRodDetails.Table.MoreDetailSpecialityTable;
import com.shaic.claim.viewEarlierRodDetails.Table.MoreDetailsDiagnosisTable;
import com.shaic.claim.viewEarlierRodDetails.Table.MoreDetailsMedicalVerificationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.MoreDetailsProcedureTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewMedicalSummaryPedValidationTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewProcedureTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewTreatementVerificationTable;
import com.shaic.domain.AssignedInvestigatiorDetails;
import com.shaic.domain.ClaimService;
import com.shaic.domain.FieldVisitRequestService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReimbursementQueryService;
import com.shaic.domain.ReimbursementRejectionService;
import com.shaic.domain.SublimitFunObject;
import com.shaic.domain.preauth.ClaimLimit;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewMedicalSummaryMoreDetailsPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PreauthDTO bean;
	
	private BeanFieldGroup<PreauthDataExtaractionDTO> binder;
	
	@Inject
	private Instance<MoreDetailsProcedureTable> viewProcedureListTableInstance;
	@Inject
	private MoreDetailsProcedureTable viewProcedureListTableObj;
	
	/*@Inject
	private Instance<ViewMedicalSummaryPedValidationTable> viewPedValidationTableInstance;
	@Inject
	private ViewMedicalSummaryPedValidationTable viewPedValidationTableObj;*/
	
	private Map<String, Object> referenceData = new HashMap<String, Object>(); 
	
	/*@Inject
	private Instance<MoreDetailsMedicalVerificationTable> medicalVerificationTableInstance;
	@Inject
	private MoreDetailsMedicalVerificationTable medicalVerificationTableObj;*/
	
	@Inject
	private Instance<ViewTreatementVerificationTable> treatementVerificationInstance;
	@Inject
	private ViewTreatementVerificationTable treatementVerification;
	
	@Inject
	private Instance<MoreDetailsDiagnosisTable> viewDiagnosisTableInstance;
	
	@Inject
	private MoreDetailsDiagnosisTable viewDiagnosisTableObj;
	
	@Inject
	private Instance<MoreDetailSpecialityTable> viewSpecialityTableInstance;
	@Inject
	private MoreDetailSpecialityTable viewSpecialityTableObj;
	
	@Inject
	private Instance<ViewProcedureTable> procedureExclusionCheckTable;
	@Inject
	private ViewProcedureTable procedureExclusionCheckTableObj;
	
	@Inject
	private Instance<ViewInvestigationRemarksTable> viewInvestigationReviewRemarksInstance;
	
	private ViewInvestigationRemarksTable viewInvestigationRemarksTable;
	
	@EJB
	private ReimbursementQueryService queryService;
	
	@EJB
	private ReimbursementRejectionService rejectionService;
	
	@EJB
	private FieldVisitRequestService fvrService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private IntimationService intimationService;
	
	private PreauthDTO preauthDto;
	
	private TextField txtStatus;
	
	private TextField txtApprovedAmount;
	
	private TextArea txtMedicalRemarks;
	
	private TextArea txtRemarks;
	
	private TextField txtDoctorNote;
	
	private TextField txtDateOfAdmission;
	
	private TextField ReasonForChangeAdmission;
	
	private TextField txtRoomCategory;
	
	private OptionGroup ventilatorSupportRadio;
	
	private TextField txtSpecify;
	
	private TextField txtDateOfDischarge;
	
	private TextField txtSystemMedicine;
	
	private TextField txtHospitalisationDueTo;
	
	private TextField txtDateOfInjury;
	
	private OptionGroup medicoLegalCase;
	
	private OptionGroup reportedToPolice;
	
	private TextField txtRelapseOfIllness;
	
	private TextField txtReasonForAdmission;
	
	private TextField txtNoOfDays;
	
	private TextField txtNatureOfTreatment;
	
	private TextField  txtFirstConsultaionDate;
	
	private CheckBox chkCorpBuffer;
	
	private TextField txtAutoRestoration;
	
	private TextField txtIllness;
	
	private TextField txtPatientStatus;
	
	private TextField txtCauseOfInjury;
	
	private TextField txtTreatementType;
	
    private CheckBox chkInvestigationReviewed;
	
	private TextField txtInvestigatorName;
	
	private TextField txtInvestigationReviewRemarks;
	
	private TextField txtRelapseIllness;
	
	private TextField txtattachToPreviousClaims;
	
	private TextField txtPreviousClaimsRemarks;
	
	private TextArea txtTimeBasedIcdCodeExclusion;
	
	private TextArea txtDecisionChangeReason;
	
	private TextArea txtDecisionChangeRemarks;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private DiagnosisService diagnosisService;
	
	@EJB
	private PreauthService preauthService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private DBCalculationService dbCalculationService;  
	
	
	@EJB
	private AcknowledgementDocumentsReceivedService documentService;
	
	private TextField txtTypeOfAdmission;
	
	public void init(PreauthDTO preauthDto,Reimbursement reimbursement,List<ProcedureDTO> procedureList,List<SpecialityDTO> speciality,List<MedicalVerificationDTO> medicalVerification,
			List<MedicalVerificationDTO> treatmentVerfication,List<DiagnosisDetailsTableDTO> diagnosisList)
	{
		this.bean = preauthDto;
		
		if(null == this.bean){
			this.bean = new PreauthDTO();
		}
		List<PedDetailsTableDTO> pedValidationList = new ArrayList<PedDetailsTableDTO>();
		if(null != reimbursement)
		{
			PreMedicalMapper premedicalMapper=PreMedicalMapper.getInstance();
//			PreMedicalMapper.getAllMapValues();
			List<ProcedureDTO> procedureMainDTOList = premedicalMapper
					.getProcedureMainDTOList(preauthService
							.findProcedureByPreauthKey(reimbursement.getKey()));
			for (ProcedureDTO procedureDTO : procedureMainDTOList) {
				procedureDTO.setEnableOrDisable(false);
				if (procedureDTO.getSublimitName() != null) {
					ClaimLimit limit = claimService.getClaimLimitByKey(procedureDTO
							.getSublimitName().getLimitId());
					procedureDTO.setSublimitName(getSublimitFunctionObj(limit));
					procedureDTO.setSublimitDesc(limit.getLimitDescription());
					procedureDTO.setSublimitAmount(limit.getMaxPerPolicyPeriod()
							.toString());
    			}
			}
	
		this.bean.getPreauthMedicalProcessingDetails().setProcedureExclusionCheckTableList(procedureMainDTOList);
		pedValidationList = diagnosisService.getPedValidationList(reimbursement.getKey());
		}
		
		NewIntimationDto intimationDto = intimationService.getIntimationDto(reimbursement.getClaim().getIntimation());
		
//		String policyAgeing = dbCalculationService.getPolicyAgeing(bean.getPreauthDataExtractionDetails().getAdmissionDate(), intimationDto.getPolicy().getPolicyNumber());
		String policyAgeing = intimationDto.getPolicyYear();
		
		if(this.bean.getPreauthDataExtractionDetails().getAdmissionDate()!=null){
			Date tempDate = this.bean.getPreauthDataExtractionDetails().getAdmissionDate();
			this.bean.getPreauthDataExtractionDetails().setAdmissionDateStr(SHAUtils.formatDate(tempDate));
		}
		
		if(this.bean.getPreauthDataExtractionDetails().getDischargeDate()!=null){
			Date tempDate =this.bean.getPreauthDataExtractionDetails().getDischargeDate();
			this.bean.getPreauthDataExtractionDetails().setDischargeDateStr(SHAUtils.formatDate(tempDate));
		}
		
		this.binder = new BeanFieldGroup<PreauthDataExtaractionDTO>(
				PreauthDataExtaractionDTO.class);
		this.binder.setItemDataSource(this.bean
				.getPreauthDataExtractionDetails());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		
		txtApprovedAmount = (TextField) binder.buildAndBind("Approved Amt", "totalApprAmt", TextField.class);
		txtStatus = (TextField) binder.buildAndBind("Status", "statusValue", TextField.class);
		
//		txtMedicalRemarks = (TextField) binder.buildAndBind("Medical Remarks", "medicalRemarks", TextField.class);
		txtMedicalRemarks = new TextArea("Medical Remarks");	
		txtMedicalRemarks.setValue(reimbursement.getMedicalRemarks());
		
//		txtRemarks = (TextField) binder.buildAndBind("Remarks", "totalApprAmt", TextField.class);
		txtRemarks = new TextArea("Remarks");
		
//		txtDoctorNote = (TextField) binder.buildAndBind("Doctor Note (Internal remarks)", "totalApprAmt", TextField.class);
		txtDoctorNote = new TextField("Doctor Note (Internal remarks)");
		txtDoctorNote.setValue(reimbursement.getDoctorNote());
		
		txtDateOfAdmission = (TextField) binder.buildAndBind("Date of Admission ", "admissionDateStr", TextField.class);
		txtDateOfAdmission.setValidationVisible(false);
		
		ReasonForChangeAdmission = (TextField) binder.buildAndBind("Reason For Change in DOA", "changeOfDOA", TextField.class);
		
		
			
		txtRoomCategory = new TextField("Room Category");
		txtRoomCategory.setValidationVisible(false);
		
		txtTypeOfAdmission = new TextField("Type Of Admission");
		if( txtTypeOfAdmission != null && reimbursement != null && reimbursement.getTypeOfAdmission() != null ){
			txtTypeOfAdmission.setValue(masterService.getMaster(reimbursement.getTypeOfAdmission()).getValue());
		}else{
			txtTypeOfAdmission.setValue("-");
		}
		ventilatorSupportRadio =new OptionGroup("");
		
		ventilatorSupportRadio =new OptionGroup("");
		
		if(reimbursement !=null && reimbursement.getRoomCategory()!=null && reimbursement.getRoomCategory().getValue()!=null
				&& reimbursement.getRoomCategory().getValue().equalsIgnoreCase("ICU/NICU/ICCU/CCU/PICU")){
			
			System.out.println(String.format("Ventilator Support value in View Details Medical Summary More detailed: [%s]", reimbursement.getVentilatorSupport()));
			ventilatorSupportRadio =new OptionGroup("Ventilator Support");
			ventilatorSupportRadio.addItems(getReadioButtonOptions());
			ventilatorSupportRadio.setItemCaption(true, "Yes");
			ventilatorSupportRadio.setItemCaption(false, "No");
			ventilatorSupportRadio.setStyleName("horizontal");
			//ventilatorSupportRadio.setValue(this.bean.getPreauthDataExtractionDetails().getVentilatorSupport()? true : false);
			if(reimbursement.getVentilatorSupport()!=null){
			ventilatorSupportRadio.setValue(reimbursement.getVentilatorSupport().equalsIgnoreCase("Y")? true : false);
			}
			ventilatorSupportRadio.setReadOnly(true);
			ventilatorSupportRadio.setEnabled(false);
		}
		
//		txtSpecify = (TextField) binder.buildAndBind("Specify", "totalApprAmt", TextField.class);
		txtSpecify = new TextField("Specify");
		
		txtDateOfDischarge = (TextField) binder.buildAndBind("Date of Discharge", "dischargeDateStr", TextField.class);
		
		txtSystemMedicine = (TextField) binder.buildAndBind("System of Medicine", "systemOfMedicine", TextField.class);
		
//		txtHospitalisationDueTo = (TextField) binder.buildAndBind("Hospitalisation Due to", "totalApprAmt", TextField.class);
		txtHospitalisationDueTo = new TextField("Hospitalisation Due to");
		
		txtDateOfInjury = (TextField) binder.buildAndBind("Date of Injury", "injuryDate", TextField.class);
		txtDateOfInjury.setValidationVisible(false);
		
		txtRelapseOfIllness = (TextField) binder.buildAndBind("Relapse of Illness", "relapseIllness", TextField.class);
		

		txtReasonForAdmission = (TextField) binder.buildAndBind("Reason For Admission", "reasonForAdmission", TextField.class);
		txtReasonForAdmission.setValue(reimbursement.getClaim().getIntimation().getAdmissionReason());
		txtNoOfDays = (TextField) binder.buildAndBind("Length of Stay", "noOfDays", TextField.class);
		
//		txtNatureOfTreatment = (TextField) binder.buildAndBind("Nature of Treatment", "totalApprAmt", TextField.class);
		txtNatureOfTreatment = new TextField("Nature of Treatment");

		txtFirstConsultaionDate = (TextField) binder.buildAndBind("1st Consultation Date", "firstConsultantDate", TextField.class);
		txtFirstConsultaionDate.setValidationVisible(false);
		
		chkCorpBuffer = (CheckBox) binder.buildAndBind("Corp Buffer", "corpBuffer", CheckBox.class);
		chkCorpBuffer.setEnabled(false);
		
		txtAutoRestoration = (TextField) binder.buildAndBind("Automatic Restoration", "autoRestoration", TextField.class);

//		txtIllness = (TextField) binder.buildAndBind("Illness", "totalApprAmt", TextField.class);
	
		txtIllness = new TextField("Illness");

		txtPatientStatus = new TextField("Patient Status");
		
		
		
//		txtPatientStatus = (TextField) binder.buildAndBind("Patient Status", "totalApprAmt", TextField.class);
		
//		txtCauseOfInjury = (TextField) binder.buildAndBind("Cause of Injury", "totalApprAmt", TextField.class);
		
		txtCauseOfInjury = new TextField("Cause of Injury");
		if(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury() != null){
		txtCauseOfInjury.setValue(this.bean.getPreauthDataExtractionDetails().getCauseOfInjury().getValue());
		}
		
		txtTimeBasedIcdCodeExclusion = new TextArea("Time based excluded ICD Reason");
		if(reimbursement != null && reimbursement.getReasonForIcdExclusion()!= null){
			txtTimeBasedIcdCodeExclusion.setValue(reimbursement.getReasonForIcdExclusion());
		}
		
		txtDecisionChangeReason = new TextArea("Decision Change Reason");
		if(txtDecisionChangeReason !=null && reimbursement != null && reimbursement.getDecisionChangeReason() != null){
			//MastersValue decisionChangeReason=masterService.getMaster(preauth.getDecisionChangeReason().getValue());
			txtDecisionChangeReason.setValue(reimbursement.getDecisionChangeReason().getValue());
		}
		
		
		txtDecisionChangeRemarks = new TextArea("Decision Change Remarks");
		if(reimbursement != null && reimbursement.getDecisionChangeRemarks()!= null){
			txtDecisionChangeRemarks.setValue(reimbursement.getDecisionChangeRemarks());
		}

		
		medicoLegalCase = (OptionGroup) binder.buildAndBind(
				"Medico Legal Case","medicalLegalCase",OptionGroup.class);
		
		reportedToPolice = (OptionGroup) binder.buildAndBind(
				"Reported to Police",
				"reportedToPolice", OptionGroup.class);
		
		medicoLegalCase.addItems(getReadioButtonOptions());
		medicoLegalCase.setItemCaption(true, "Yes");
		medicoLegalCase.setItemCaption(false, "No");
		medicoLegalCase.setStyleName("horizontal");
		medicoLegalCase.select(false);
		medicoLegalCase.setValue(this.bean.getPreauthDataExtractionDetails().getMedicalLegalCase());
		medicoLegalCase.setReadOnly(true);
		
		reportedToPolice.addItems(getReadioButtonOptions());
		reportedToPolice.setItemCaption(true, "Yes");
		reportedToPolice.setItemCaption(false, "No");
		reportedToPolice.setStyleName("horizontal");
		reportedToPolice.select(false);
		reportedToPolice.setValue(this.bean.getPreauthDataExtractionDetails().getReportedToPolice());
		reportedToPolice.setReadOnly(true);
	    txtTreatementType = new TextField("Treatment Type");
		
		FormLayout firstForm = new FormLayout(txtStatus,txtApprovedAmount,txtMedicalRemarks,txtDateOfAdmission,ReasonForChangeAdmission,txtRoomCategory
				,ventilatorSupportRadio,txtTypeOfAdmission,txtSpecify,txtDateOfDischarge,txtSystemMedicine,txtHospitalisationDueTo,txtDateOfInjury,medicoLegalCase,reportedToPolice,txtTreatementType,txtDecisionChangeReason,txtDecisionChangeRemarks);
		firstForm.setSpacing(true);
		
		
		
		FormLayout secondForm = new FormLayout(txtRemarks,txtDoctorNote,txtRelapseOfIllness,txtReasonForAdmission,txtNoOfDays,txtNatureOfTreatment,txtFirstConsultaionDate,
				chkCorpBuffer,txtAutoRestoration,txtIllness,txtPatientStatus,txtCauseOfInjury,txtTimeBasedIcdCodeExclusion);
		secondForm.setSpacing(true);
	
		
		HorizontalLayout firstHor = new HorizontalLayout(firstForm,secondForm);
		firstHor.setSpacing(true);
		
		if(null != reimbursement){
			setFieldValues(reimbursement);
		}
		
		//this.viewDiagnosisTableObj = viewDiagnosisTableInstance.get();
		this.viewDiagnosisTableObj.init("", false, false);
		this.viewDiagnosisTableObj.setMoreColumn();
		this.viewDiagnosisTableObj.setCaption("Diagnosis Details");
		int serialNumber = 1;
		for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisList) {
			
			if(diagnosisDetailsTableDTO.getIcdChapterKey() != null){
				SelectValue icdChapterbyId = masterService.getIcdChapterbyId(diagnosisDetailsTableDTO.getIcdChapterKey());
				if(icdChapterbyId != null){
				diagnosisDetailsTableDTO.setIcdChapterValue(icdChapterbyId.getValue());
				}
			}
			if(diagnosisDetailsTableDTO.getIcdBlockKey() != null){
				SelectValue icdBlock = masterService.getIcdBlock(diagnosisDetailsTableDTO.getIcdBlockKey());
				if(icdBlock != null){
				diagnosisDetailsTableDTO.setIcdBlockValue(icdBlock.getValue());
				}
			}
			if(diagnosisDetailsTableDTO.getIcdCodeKey() != null){
				SelectValue icdCode = masterService.getIcdCodeByKey(diagnosisDetailsTableDTO.getIcdCodeKey());
				if(icdCode != null){
				diagnosisDetailsTableDTO.setIcdCodeValue(icdCode.getValue());
				}
			}
			diagnosisDetailsTableDTO.setSerialNumber(serialNumber);
			serialNumber ++;
			viewDiagnosisTableObj.addBeanToList(diagnosisDetailsTableDTO);
		}
		
		ViewProcedureTable procedureExclusionCheckTableInstance = procedureExclusionCheckTableObj;
		procedureExclusionCheckTableInstance.init("", false,false);
		//this.procedureExclusionCheckTableObj = procedureExclusionCheckTableInstance;
		this.procedureExclusionCheckTableObj.setCaption("Procedure Exclusion Check");
//		this.procedureExclusionCheckTableObj.setReference(referenceData);
		
		List<ProcedureDTO> procedureExclusionCheckTableList = this.bean.getPreauthMedicalProcessingDetails().getProcedureExclusionCheckTableList();
		if(!procedureExclusionCheckTableList.isEmpty()) {
			for (ProcedureDTO procedureDTO : procedureExclusionCheckTableList) {
				procedureDTO.setStatusFlag(true);
				procedureDTO.setPolicyAging(policyAgeing);
				this.procedureExclusionCheckTableObj.addBeanToList(procedureDTO);
			}
		}
		
		//this.viewProcedureListTableObj = viewProcedureListTableInstance.get();
		this.viewProcedureListTableObj.init("", false, false);
		this.viewProcedureListTableObj.setCaption("Procedure List");
		
         for (ProcedureDTO procedureDTO : procedureList) {
        	 this.viewProcedureListTableObj.addBeanToList(procedureDTO);
		}
		
		//this.viewSpecialityTableObj = viewSpecialityTableInstance.get();
		this.viewSpecialityTableObj.init("", false, false);
		this.viewSpecialityTableObj.setCaption("Speciality");
		int i=1;
		
		for (SpecialityDTO specialityDTO : speciality) {
			specialityDTO.setSerialNo(i++);
			this.viewSpecialityTableObj.addBeanToList(specialityDTO);
		}

		if (null != reimbursement && null != reimbursement.getTreatmentType() 
				&& reimbursement.getTreatmentType().getValue().toLowerCase().equals("surgical")) {
			viewSpecialityTableObj.setVisible(false);
		}
         
		//GLX2021092
        //this.viewPedValidationTableObj= this.viewPedValidationTableInstance.get();
       /* this.viewPedValidationTableObj.init("", false, false);
        this.viewPedValidationTableObj.setCaption("PED Validation");
        
       for (PedDetailsTableDTO pedDetailsTableDTO : pedValidationList) {
    	   this.viewPedValidationTableObj.addBeanToList(pedDetailsTableDTO);
		}
		
		//this.medicalVerificationTableObj = medicalVerificationTableInstance.get();
		this.medicalVerificationTableObj.init("", false,false);
		this.medicalVerificationTableObj.setCaption("Medical Verification");
		if (null != medicalVerification) {
			for (MedicalVerificationDTO medicalVerificationDTO : medicalVerification) {
				this.medicalVerificationTableObj
						.addBeanToList(medicalVerificationDTO);
			}
		}*/
        
        chkInvestigationReviewed = new CheckBox("Investigation Report Reviewed");
	    chkInvestigationReviewed.setValue(false);
	    chkInvestigationReviewed.setEnabled(false);
	    
	    if(reimbursement.getInvestigationReportReview() != null && reimbursement.getInvestigationReportReview().equalsIgnoreCase("Y")){
	    	chkInvestigationReviewed.setValue(true);
	    }
	    
	    
	    txtInvestigatorName = new TextField("Investigator Name");
	    txtInvestigatorName.setValue(this.bean.getInvestigatorName());
//	    txtInvestigatorName.setValue(reimbursement.getInvestigatorName() != null ? reimbursement.getInvestigatorName().getInvestigatorName() : "");
	    txtInvestigationReviewRemarks = new TextField("Investigation Review Remarks");
	    txtInvestigationReviewRemarks.setValue(reimbursement.getInvestigatorRemarks());
//	    txtInvestigationReviewRemarks.setValue(this.bean.getInvestigationReviewRemarks());
	    
	    this.viewInvestigationRemarksTable = this.viewInvestigationReviewRemarksInstance.get();
		this.viewInvestigationRemarksTable.init(SHAConstants.MEDICAL_SUMMARY);
		
		List<AssignedInvestigatiorDetails> invsReviewRemarksList = reimbursementService.getRepiedInvestigationDetails(this.bean.getClaimKey());
		if(invsReviewRemarksList != null){
			List<AssignedInvestigatiorDetails> invsRepliedRemarksList = new ArrayList<AssignedInvestigatiorDetails>();
			for (AssignedInvestigatiorDetails assignedInvestigatiorDetails : invsReviewRemarksList) {
				if(assignedInvestigatiorDetails.getReportReviewed() != null && assignedInvestigatiorDetails.getReportReviewed().equalsIgnoreCase(SHAConstants.YES_FLAG)){
					invsRepliedRemarksList.add(assignedInvestigatiorDetails);
				}
			}
			this.viewInvestigationRemarksTable.setTableList(invsRepliedRemarksList);
		}
	    
		/*Below Code commented as per BA Team table only injecting
	    FormLayout form1= new FormLayout(chkInvestigationReviewed,txtInvestigatorName,txtInvestigationReviewRemarks);*/
	    
	    FormLayout form1= new FormLayout(viewInvestigationRemarksTable);
	    form1.setSpacing(true);
	    
	    setReadOnly(form1, true);
	    
	    HorizontalLayout investigationHor = new HorizontalLayout(/*medicalVerificationTableObj,*/form1);
	    investigationHor.setSpacing(true);
	    
		if(null == this.treatementVerification){
        //this.treatementVerification = treatementVerificationInstance.get();
        this.treatementVerification.init("", false,false);
        this.treatementVerification.setCaption("Treatment Quality Verification");
		}
		if (null != treatmentVerfication) {
			for (MedicalVerificationDTO medicalVerificationDTO : treatmentVerfication) {
				this.treatementVerification
						.addBeanToList(medicalVerificationDTO);
			}
		}
		
		HorizontalLayout dummyHor = new HorizontalLayout();
		dummyHor.setHeight("100px");
		
		VerticalLayout medicalProcessingVer = new VerticalLayout( /*this.viewPedValidationTableObj,*/this.procedureExclusionCheckTableObj);
		
		Panel medicalProcessingPanel = new Panel(medicalProcessingVer);
		medicalProcessingPanel.setCaption("MedicalProcessing");
		medicalProcessingPanel.setStyleName("gridBorder");
		
		Panel previousClaimsPanel = getPreviousClaimsPanel();
		
		VerticalLayout verticalMain = new VerticalLayout(firstHor,this.viewDiagnosisTableObj,this.viewProcedureListTableObj,this.viewSpecialityTableObj, previousClaimsPanel,medicalProcessingPanel,investigationHor,dummyHor,this.treatementVerification);
		verticalMain.setSpacing(true);
		verticalMain.setMargin(true);
		
		
		Panel mainPanel = new Panel(verticalMain);
		mainPanel.setStyleName("girdBorder");
		mainPanel.setCaption("Medical Summary");
		
		setReadOnly(firstForm, true);
		setReadOnly(secondForm, true);
		
		setCompositionRoot(mainPanel);
	}
	
	public Panel getPreviousClaimsPanel(){
		
		txtRelapseIllness = new TextField("Relapse of Illness");
		txtRelapseIllness.setEnabled(false);
		txtattachToPreviousClaims = new TextField("Attach to Previous Claim");
		txtattachToPreviousClaims.setEnabled(false);
		txtPreviousClaimsRemarks = new TextField("Remarks");
		txtPreviousClaimsRemarks.setEnabled(false);
		
		FormLayout firsForm = new FormLayout(txtRelapseIllness,txtattachToPreviousClaims);
		FormLayout secondForm = new FormLayout(txtPreviousClaimsRemarks);
		
		HorizontalLayout hor = new HorizontalLayout(firsForm,secondForm);
		
		Panel panel = new Panel(hor);
		panel.setCaption("Previous Claim Details");
		panel.setStyleName("gridBorder");
		
		return panel;
		
		
	}
	
	public void setFieldValues(Reimbursement reimbursement){
	    if(null != reimbursement.getApprovedAmount()){
	    	   txtApprovedAmount.setReadOnly(false);
				txtApprovedAmount.setValue(reimbursement.getApprovedAmount().toString());
				txtApprovedAmount.setReadOnly(true);
				}
			if(null != reimbursement.getStatus()){
				txtStatus.setValue(reimbursement.getStatus().getProcessValue());
			}
//			txtMedicalRemarks.setValue(reimbursement.getTreatmentRemarks());
			txtRemarks.setValue(reimbursement.getApprovalRemarks());
			
			if(bean.getRemarks() != null){
				txtRemarks.setValue(bean.getRemarks());
			}
			
			ReasonForChangeAdmission.setValue(reimbursement.getDoaChangeReason());
			if(null != reimbursement.getRoomCategory()){
			txtRoomCategory.setValue(reimbursement.getRoomCategory().getValue());
			}
			txtSystemMedicine.setValue(reimbursement.getSystemOfMedicine());
			if(null != reimbursement.getHopsitaliztionDueto()){
				txtHospitalisationDueTo.setValue(reimbursement.getHopsitaliztionDueto().getValue());
			}
			if(null != reimbursement.getInjuryDate()){
			txtDateOfInjury.setValue(SHAUtils.formatDate(reimbursement.getInjuryDate()));
			}
			txtRelapseOfIllness.setValue(reimbursement.getRelapseRemarks());
			if(null != reimbursement.getNumberOfDays()){
			txtNoOfDays.setValue(reimbursement.getNumberOfDays().toString());
			}
			if(null != reimbursement.getNatureOfTreatment()){
				txtNatureOfTreatment.setValue(reimbursement.getNatureOfTreatment().getValue());
			}
			
			if(null !=reimbursement.getConsultationDate()){
			txtFirstConsultaionDate.setValue(SHAUtils.formatDate(reimbursement.getConsultationDate()));
			}
			if(null != reimbursement.getCorporateBufferFlag()){
				if(1==reimbursement.getCorporateBufferFlag()){
					chkCorpBuffer.setValue(true);
				}
			}
			if(null != reimbursement.getAutomaticRestoration()){
				if(reimbursement.getAutomaticRestoration().equals("Y")){
					txtAutoRestoration.setValue("Yes");
				}
				else
				{
					txtAutoRestoration.setValue("No");
				}
			}
			if(reimbursement.getIllness() != null){
			txtIllness.setValue(reimbursement.getIllness().getValue());
			}
			
			if(reimbursement.getPatientStatus() != null){
				txtPatientStatus.setValue(reimbursement.getPatientStatus().getValue());
			}
		    if(null != reimbursement.getTreatmentType()){
		    txtTreatementType.setValue(reimbursement.getTreatmentType().getValue());
		    }
	}
	
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);

		return coordinatorValues;
	}
	
	private SublimitFunObject getSublimitFunctionObj(ClaimLimit limit) {
		SublimitFunObject obj = new SublimitFunObject();
		obj.setLimitId(limit.getKey());
		obj.setAmount(Double.valueOf(limit.getMaxPerClaimAmount()));
		obj.setName(limit.getLimitName());
		obj.setDescription(limit.getLimitDescription());
		return obj;
	}
	
	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("300px");
				field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
		        field.setNullRepresentation("-");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

	 public void setClearReferenceData(){
	    	SHAUtils.setClearReferenceData(referenceData);
	    	SHAUtils.setClearPreauthDTO(bean);
	    	if(this.viewInvestigationRemarksTable!=null){
	    		this.viewInvestigationRemarksTable=null;
	    	}
	    	this.binder=null;
	 }
	
}
