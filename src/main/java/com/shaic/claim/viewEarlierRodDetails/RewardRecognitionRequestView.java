/**
 * 
 */
package com.shaic.claim.viewEarlierRodDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;










import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.adviseonped.AdviseOnPEDPresenter;
import com.shaic.claim.cashlessprocess.downsize.wizard.DownSizePreauthWizardPresenter;
import com.shaic.claim.cashlessprocess.downsizeRequest.page.DownsizePreauthRequestWizardPresenter;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.fieldVisitPage.FieldVisitPagePresenter;
import com.shaic.claim.fileUpload.fileUploadPresenter;
import com.shaic.claim.hospitalCommunication.AcknowledgeHospitalPresenter;
import com.shaic.claim.negotiation.NegotiationPreauthRequestPresenter;
import com.shaic.claim.pedquery.PEDQueryPresenter;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApprovePresenter;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessPresenter;
import com.shaic.claim.pedrequest.teamlead.PEDRequestDetailsTeamLeadPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.shaic.claim.processRejectionPage.ProcessRejectionPresenter;
import com.shaic.claim.registration.ClaimRegistrationPresenter;
import com.shaic.claim.registration.convertClaimPage.ConvertClaimPagePresenter;
import com.shaic.claim.registration.convertClaimToReimbursement.convertReimbursementPage.ConvertReimbursementPagePresenter;
import com.shaic.claim.reimbursement.billing.wizard.BillingWizardPresenter;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.financialapproval.wizard.FinancialWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.wizard.ClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.wizard.MedicalApprovalZonalReviewWizardPresenter;
import com.shaic.claim.reimbursement.pa.medicalapproval.processclaimrequest.wizard.PAClaimRequestWizardPresenter;
import com.shaic.claim.reimbursement.rrc.services.InitiateRRCRequestWizardPresenter;
import com.shaic.claim.reimbursement.submitSpecialist.SubmitSpecialistAdvisePresenter;
import com.shaic.claim.rod.wizard.pages.AcknowledgeDocReceivedWizardPresenter;
import com.shaic.claim.rod.wizard.pages.BillEntryWizardPresenter;
import com.shaic.claim.rod.wizard.pages.CreateRODWizardPresenter;
import com.shaic.claim.rod.wizard.tables.ExtraEffortEmployeeListenerTable;
import com.shaic.claim.rod.wizard.tables.QuantumReductionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.RRCRequestCategoryListenerTable;
import com.shaic.claim.submitSpecialist.SubmitSpecialistPagePresenter;
import com.shaic.claim.withdrawPostProcessWizard.WithdrawPreauthPostProcessWizardPresenter;
import com.shaic.claim.withdrawWizard.WithdrawPreauthWizardPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.AddAditionalDocumentsPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.paclaim.billing.processclaimbilling.page.PABillingWizardPresenter;
import com.shaic.paclaim.cashless.downsize.wizard.PADownSizePreauthWizardPresenter;
import com.shaic.paclaim.cashless.enhancement.wizard.wizardfiles.PAPreauthEnhancemetWizardPresenter;
import com.shaic.paclaim.cashless.fle.wizard.wizardfiles.PAPremedicalEnhancementWizardPresenter;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.shaic.paclaim.cashless.processdownsize.wizard.PADownsizePreauthRequestWizardPresenter;
import com.shaic.paclaim.cashless.withdraw.wizard.PAWithdrawPreauthWizardPresenter;
import com.shaic.paclaim.convertClaimToReimb.ConvertPAClaimPagePresenter;
import com.shaic.paclaim.convertClaimToReimbursement.convertReimbursementPage.PAConvertReimbursementPagePresenter;
import com.shaic.paclaim.financial.claimapproval.nonhosiptalpage.PAClaimAprNonHosWizardPresenter;
import com.shaic.paclaim.financial.nonhospprocessclaimfinancial.page.PANonHospFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.wizardfiles.PAHealthBillingWizardPresenter;
import com.shaic.paclaim.health.reimbursement.financial.wizard.PAHealthFinancialWizardPresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.wizard.PAHealthClaimRequestWizardPresenter;
import com.shaic.paclaim.processRejectionPage.PAProcessRejectionPresenter;
import com.shaic.paclaim.rod.acknowledgementdocumentreceiver.search.PAAcknowledgeDocumentWizardPresenter;
import com.shaic.paclaim.rod.createrod.search.PACreateRODWizardPresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PAEnterBillDetailsWizardPresenter;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.AcknowledgeInvestigationCompletedPresenter;
import com.shaic.reimbursement.assigninvesigation.AssignInvestigationPresenter;
import com.shaic.reimbursement.draftinvesigation.DraftInvestigationPresenter;
import com.shaic.reimbursement.processi_investigationi_initiated.ProcessInvestigationInitiatedPresenter;
import com.shaic.reimbursement.uploadTranslatedDocument.UploadTranslatedDocumentPresenter;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author ntv.vijayar
 *
 */
public class RewardRecognitionRequestView extends ViewComponent {
	
	private Panel employeeDetailsPanel;
	
	private Panel policyDetailsPanel;
	
	private Panel hospitalDetailsPanel;
	
	private Panel insuredDetailsPanel;
	
	private Panel quantumReductionDetailsPanel;
	
	private Panel rrcViewPanel;
	
	
	private TextField txtEmployeeName;
	
	private	TextField txtEmployeeId;
	
	private TextField txtEmployeeZone;
	
	private TextField txtEmployeeDept;
	
	private TextField txtPolicyNo;
	
	private TextField txtIntimationNo;
	
	private TextField txtProductName;
	
	private TextField txtDuration;
	
	private TextField txtSumInsured;
	
	private TextField txtHospitalName;
	
	private TextField txtHospitalCity;
	
	private TextField txtHospitalZone;
	
	private TextField txtDateOfAdmission;
	
	private TextField txtDateOfDischarge;
	
	private TextField txtInsuredName;
	
	private TextField txtInsuredAge;
	
	private TextField txtSex;
	
	private TextField txtClaimType;
	
	private TextField txtProcessingStage;
	
	@Inject
	private Instance<QuantumReductionDetailsListenerTable> quantumReductionListenerTableObj;
	
	private QuantumReductionDetailsListenerTable quantumReductionListenerTable;
	
	private ComboBox cmbSignificantClinicalInformation;
	
	//private TextField txtRemarks;
	private TextArea txtRemarks;
	
	@Inject
	private Instance<ExtraEffortEmployeeListenerTable> extraEffortEmployeeListenerTableObj;
	
	private ExtraEffortEmployeeListenerTable extraEffortEmployeeListenerTable;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private String presenterString;
	
	private PreauthDTO bean;
	
	private Window popup;
	
	private BeanFieldGroup<RRCDTO> binder;
	
	private Map<String, Object> containerMap;
	
	@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcCategoryListenerTableObj;
	
	private RRCRequestCategoryListenerTable rrcCategoryListenerTable;
	
//	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	//private VerticalLayout verticalRRCLayout;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(PreauthDTO preauthDTO, Window popup)
	{
		bean = preauthDTO;
		this.popup = popup;
		this.containerMap = preauthDTO.getRrcDTO().getDataSourcesMap();
		
		if(preauthDTO.getNewIntimationDTO() == null && (preauthDTO.getRrcDTO() != null && preauthDTO.getRrcDTO().getNewIntimationDTO() != null)){
            preauthDTO.setNewIntimationDTO(preauthDTO.getRrcDTO().getNewIntimationDTO());
		}
		
		if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
			DBCalculationService dbCalculationService = new DBCalculationService();
			Double sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
					preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
			if(sumInsured != null){
				bean.getRrcDTO().setSumInsured(sumInsured);
			}
		}

		this.binder = new BeanFieldGroup<RRCDTO>(RRCDTO.class);
		this.binder.setItemDataSource(this.bean.getRrcDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		
		HorizontalLayout detailsLayout = new HorizontalLayout(buildEmployeeDetailsLayout() , buildPolicyDetailsLayout() ,buildHospitalDetailsLayout(), buildInsuredDetailsLayout());
		detailsLayout.setSpacing(true);
		detailsLayout.setMargin(true);
		detailsLayout.setEnabled(Boolean.FALSE);
		Panel detailsPanel = new Panel();
		detailsPanel.setContent(detailsLayout);
		
		VerticalLayout verticalRRCLayout = new VerticalLayout(detailsLayout , buildReductionDetailsPanel(), buildButtonLayout());
	
		verticalRRCLayout.setSpacing(true);
		verticalRRCLayout.setMargin(true);
		//verticalRRCLayout.setComponentAlignment(buildButtonLayout(), Alignment.MIDDLE_CENTER);
		
		rrcViewPanel = new Panel();
		rrcViewPanel.setSizeFull();
		
		rrcViewPanel.setContent(verticalRRCLayout);
		
		//loadContainerDataSources();
		setTableValues();
		addListener();
		
		setCompositionRoot(rrcViewPanel);
		
	}
	
	private void setTableValues()
	{
		
		if(null != quantumReductionListenerTable)
		{
			if(this.bean.getRrcDTO() !=null && this.bean.getRrcDTO().getQuantumReductionDetailsDTOList() !=null){
				List<QuantumReductionDetailsDTO> quantumReductionDetailsDTOs = this.bean.getRrcDTO().getQuantumReductionDetailsDTOList();
				for (QuantumReductionDetailsDTO reductionDetailsDTO : quantumReductionDetailsDTOs) {			
					this.quantumReductionListenerTable.addBeanToList(reductionDetailsDTO);
				}
			}else{
				quantumReductionListenerTable.addBeanToList(new QuantumReductionDetailsDTO());
			}
		}
		if(null != extraEffortEmployeeListenerTable)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = extraEffortEmployeeListenerTable.getValues();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{ 
				Long sNo = 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				extraEffortEmployeeListenerTable.setTableList(extraEmployeeList);
			}
			else
			{
				//extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
				Long sNo = 1l;
				ExtraEmployeeEffortDTO extraEmpDTO = bean.getRrcDTO().getEmployeeEffortDTO();
				extraEmpDTO.setSlNo(sNo);
				extraEffortEmployeeListenerTable.addBeanToList(extraEmpDTO);
				
				/*if(null != extraEmpDTO.getSelEmployeeId())
				{
					
					BeanItemContainer<SelectValue> empIdContainer  = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.EMPLOYEE_ID);
					
					
					
					for(int i = 0 ; i<empIdContainer.size() ; i++){
						
						if((this.bean.getRrcDTO().getEmployeeId()).equalsIgnoreCase(empIdContainer.getIdByIndex(i).getValue())){
							extraEmpDTO.setSelEmployeeId(empIdContainer.getIdByIndex(i));
							break;
						}
					}
					
				}
				if(null != extraEmpDTO.getSelEmployeeId())
				{
					//SelectValue selEmpName = new SelectValue();
					BeanItemContainer<SelectValue> empNameContainer  = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.EMPLOYEE_NAME_LIST);
					 for(int i = 0 ; i<empNameContainer.size() ; i++)
					 	{
							if ((extraEmpDTO.getSelEmployeeId().getId()).equals(empNameContainer.getIdByIndex(i).getId()))
							{
								extraEmpDTO.setSelEmployeeName(empNameContainer.getIdByIndex(i));
								break;
							}
						}
				}*/
				
				/*
				 * The below point is kept on hold.
				 * Need to implement once auto complete field is analysed.
				 * */
				//extraEffortEmployeeListenerTable.setTableList(extraEmployeeList);
			}
		}
	}
	
	private void setTableValuesDTO()
	{
		if(null != extraEffortEmployeeListenerTable)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = extraEffortEmployeeListenerTable.getValues();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				Long sNo= 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				
				this.bean.getRrcDTO().setEmployeeEffortList(extraEmployeeList);
			}
		}
		if(null != quantumReductionListenerTable)
		{
			/*
			 * Currently , as per mock up, there is no feasiblity of adding new row in quantum
			 * reduction details table. Hence the below list will have only one record.
			 * But in future, if they would require to add new row, then we would
			 * have list of DTO returned from table. To cater both needs, in DTO we have 
			 * a setter and getter for list as well as for single DTO.
			 * 
			 * Currenlty code implementation is given for one record. If in future, multiple 
			 * records are retreived, then the code commented inside the if block can be uncommented
			 * and used.
			 * */
			List<QuantumReductionDetailsDTO> quantumReductionDetails = quantumReductionListenerTable.getValues();
			if(null != quantumReductionDetails && !quantumReductionDetails.isEmpty())
			{
				//this.bean.getRrcDTO().setQuantumReductionDetailsDTOList(quantumReductionDetails);
				this.bean.getRrcDTO().setQuantumReductionDetailsDTO(quantumReductionDetails.get(0));
			}
		}
		if(null != rrcCategoryListenerTable)
		{
			List<ExtraEmployeeEffortDTO> categoryList = rrcCategoryListenerTable.getValues();
			if(null != categoryList && !categoryList.isEmpty())
			{
				Long sNo= 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : categoryList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				this.bean.getRrcDTO().setRrcCategoryList(categoryList);
			}
		}
	}
	
	private void loadContainerDataSources()
	{
		if(SHAConstants.PROCESS_PRE_MEDICAL.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PreauthWizardPresenter.PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(WithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(DownSizePreauthWizardPresenter.DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(this.presenterString)){
			fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}

		else if(SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(DownsizePreauthRequestWizardPresenter.DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_PED_QUERY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PEDQueryPresenter.PROCESS_PED_QUERY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_REJECTION.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ProcessRejectionPresenter.PROCESS_REJECTION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ConvertClaimPagePresenter.CONVERT_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ConvertReimbursementPagePresenter.CONVERT_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(AcknowledgeHospitalPresenter.ACKNOWLEDGE_HOSPITAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.FIELD_VISIT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(FieldVisitPagePresenter.FIELD_VISIT_REP_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PED_REQUEST_PROCESS.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PEDRequestDetailsProcessPresenter.PROCESS_PED_PROCESS_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PED_REQUEST_APPROVER.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PEDRequestDetailsApprovePresenter.PROCESS_PED_APPROVE_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PED_TEAM_LEAD.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PROCESS_PED_TL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ADVISE_ON_PED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(AdviseOnPEDPresenter.ADVISE_ON_PED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(fileUploadPresenter.COORDINATOR_REPLY_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(UploadTranslatedDocumentPresenter.COORDINATOR_REPLY_FOR_REIMB_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(SubmitSpecialistAdvisePresenter.PROCESS_SUBMIT_SPECIALIST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_INVESTIGATION_INTIATED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ProcessInvestigationInitiatedPresenter.PROCESS_INVESTIGATION_INITIATED_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(AssignInvestigationPresenter.PROCESS_ASSIGN_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ACKNOWLEDGE_INVESTIGATION.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(AcknowledgeInvestigationCompletedPresenter.PROCESS_ACKNOWLEDGE_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(AcknowledgeDocReceivedWizardPresenter.ACK_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(CreateRODWizardPresenter.CREATE_ROD_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		//BillEntryWizardPresenter
		else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(BillEntryWizardPresenter.BILL_ENTRY_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		//AcknowledgeInvestigationCompletedPresenter
		//PEDRequestDetailsProcessPresenter.VALIDATE_PED_PROCESS_USER_RRC_REQUEST
		else if(SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(AddAditionalDocumentsPresenter.ADD_ADDITIONAL_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}

		else if(SHAConstants.PROCESS_UPLOAD_INVESTIGATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(UploadInvestigationReportPresenter.PROCESS_UPLOAD_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}

		else if(SHAConstants.PROCESS_CLAIM_REGISTRATION.equalsIgnoreCase(presenterString))
		{
			fireViewEvent(ClaimRegistrationPresenter.PROCESS_CLAIM_REGISTRATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ClaimRequestWizardPresenter.CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.BILLING.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(BillingWizardPresenter.BILLING_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.FINANCIAL.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(FinancialWizardPresenter.FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(InitiateRRCRequestWizardPresenter.SAVE_INITIATE__RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.DRAFT_INVESTIGATION_INTIATED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(DraftInvestigationPresenter.PROCESS_DRAFT_INVESTIGATION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH_POST_PROCESS.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_REJECTION.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAProcessRejectionPresenter.PROCESS_REJECTION_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}	
		else if(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAAcknowledgeDocumentWizardPresenter.ACK_DOC_REC_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PACreateRODWizardPresenter.CREATE_ROD_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAEnterBillDetailsWizardPresenter.BILL_ENTRY_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAClaimRequestWizardPresenter.CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_CLAIM_REQUEST_HOSP.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAHealthClaimRequestWizardPresenter.CLAIM_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_BILLING.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PABillingWizardPresenter.BILLING_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_BILLING_HOSP.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAHealthBillingWizardPresenter.BILLING_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_PRE_MEDICAL.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAPreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAPreauthWizardPresenter.PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAPremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_ENHANCEMENT.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PADownsizePreauthRequestWizardPresenter.DOWNSZIE_PREAUTH_REQUEST_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PADownSizePreauthWizardPresenter.DOWNSZIE_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAWithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(ConvertPAClaimPagePresenter.CONVERT_PA_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_FINANCIAL_HOSP.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAHealthFinancialWizardPresenter.FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_FINANCIAL.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PANonHospFinancialWizardPresenter.FINANCIAL_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAClaimAprNonHosWizardPresenter.CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}
		else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(PAConvertReimbursementPagePresenter.CONVERT_CLAIM_LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}



	}
	
	private Panel buildReductionDetailsPanel()
	{
		txtClaimType = (TextField)binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtProcessingStage = (TextField)binder.buildAndBind("Processing Stage","processingStage",TextField.class);
		txtProcessingStage.setDescription(bean.getRrcDTO().getProcessingStage());
		txtClaimType.setDescription(bean.getRrcDTO().getClaimType());
		txtClaimType.setReadOnly(true);
		txtProcessingStage.setReadOnly(true);
/*		cmbSignificantClinicalInformation = (ComboBox)binder.buildAndBind("Significant Clinincal Information","significantClinicalInformation",ComboBox.class);*/		
		txtRemarks = (TextArea)binder.buildAndBind("Remarks","remarks",TextArea.class);
		/*txtRemarks.setRequired(true);*/
		txtRemarks.setWidth("60%");
		txtRemarks.setMaxLength(4000);
		txtRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtRemarks,null,getUI(),SHAConstants.RRC_REMARK);
		
		FormLayout reductionDetails = new FormLayout(txtClaimType , txtProcessingStage);
		reductionDetails.setWidth("-1px");
		reductionDetails.setSpacing(true);
		
		addStyleForTextfield(reductionDetails);
		/*reductionDetails.setComponentAlignment(txtClaimType, Alignment.TOP_RIGHT);
		reductionDetails.setComponentAlignment(txtProcessingStage, Alignment.MIDDLE_RIGHT);*/
		
		// Changes Ganesh For CR2019204 
		/*FormLayout clinicalInfo = new FormLayout(cmbSignificantClinicalInformation);*/
		FormLayout remarks = new FormLayout(txtRemarks);
		
		//addStyleForTextfield(remarks);
		
		quantumReductionListenerTable = quantumReductionListenerTableObj.get();
		quantumReductionListenerTable.initPresenter(SHAConstants.RRC_REQUEST_FORM);
		if(null != bean)
		quantumReductionListenerTable.init(bean.getRrcDTO());
		extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.init(true);
		extraEffortEmployeeListenerTable.setReferenceData(this.containerMap);
		//extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		
		rrcCategoryListenerTable = rrcCategoryListenerTableObj.get();
		rrcCategoryListenerTable.initPresenter(this.presenterString);
		rrcCategoryListenerTable.setReferenceData(this.containerMap);
		rrcCategoryListenerTable.init(true);
		
		HorizontalLayout infoAndRemarksLayout = new HorizontalLayout(/*clinicalInfo,*/ remarks);
		infoAndRemarksLayout.setWidth("60%");
		infoAndRemarksLayout.setSpacing(true);
		
		VerticalLayout vReductionLayout = new VerticalLayout(reductionDetails , quantumReductionListenerTable,rrcCategoryListenerTable, remarks, extraEffortEmployeeListenerTable );
		vReductionLayout.setWidth("100%");
		vReductionLayout.setComponentAlignment(reductionDetails, Alignment.TOP_RIGHT);
		vReductionLayout.setSpacing(true);
		vReductionLayout.setMargin(true);
		
		
		quantumReductionDetailsPanel = new Panel();
		quantumReductionDetailsPanel.setContent(vReductionLayout);
		quantumReductionDetailsPanel.setStyleName("girdBorder");
		
		return quantumReductionDetailsPanel;
		
	}
	
	private Panel  buildEmployeeDetailsLayout()
	{
		txtEmployeeName = (TextField)binder.buildAndBind("Employee Name","employeeName",TextField.class);
		
		//txtEmployeeName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeId = (TextField)binder.buildAndBind("Employee ID","employeeId",TextField.class);
		//txtEmployeeId.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeZone = (TextField)binder.buildAndBind("Employee Zone","employeeZone",TextField.class);
		//txtEmployeeZone.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeDept = (TextField)binder.buildAndBind("Employee Dept","employeeDept",TextField.class);
		//txtEmployeeDept.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		employeeDetailsPanel = new Panel();
		FormLayout empDetails = new FormLayout(txtEmployeeName,txtEmployeeId,txtEmployeeZone,txtEmployeeDept);
		addStyleForTextfield(empDetails);
		empDetails.setSpacing(true);
//		empDetails.setMargin(true);
		employeeDetailsPanel.setContent(empDetails);
		
		employeeDetailsPanel.setStyleName("girdBorder");
		
		return employeeDetailsPanel;
	}
	
	private void addStyleForTextfield(FormLayout layout)
	{
		int iLayoutSize = layout.getComponentCount();
		for (int i = 0; i < iLayoutSize; i++) {
			TextField txtFld = (TextField) layout.getComponent(i);
			txtFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			txtFld.setNullRepresentation("");
		}
	}
	
	private Panel buildPolicyDetailsLayout()
	{
		txtPolicyNo = (TextField)binder.buildAndBind("Policy No","policyNo",TextField.class);
		txtIntimationNo = (TextField)binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtProductName = (TextField)binder.buildAndBind("Product Name","productName",TextField.class);
		txtDuration = (TextField)binder.buildAndBind("Duration","duration",TextField.class);
		txtSumInsured = (TextField)binder.buildAndBind("Sum Insured","sumInsured",TextField.class);
		
		policyDetailsPanel = new Panel();
		FormLayout policyDetails = new FormLayout(txtPolicyNo, txtIntimationNo , txtProductName ,/* txtDuration,*/ txtSumInsured);
		
		addStyleForTextfield(policyDetails);
		
		policyDetails.setSpacing(true);
//		policyDetails.setMargin(true);
		policyDetailsPanel.setContent(policyDetails);
		policyDetailsPanel.setStyleName("girdBorder");
		
		return policyDetailsPanel;
	}
	
	private Panel buildHospitalDetailsLayout()
	{
		txtHospitalName = (TextField)binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		txtHospitalCity = (TextField)binder.buildAndBind("Hospital City","hospitalCity",TextField.class);
		txtHospitalZone = (TextField)binder.buildAndBind("Hospital Zone","hospitalZone",TextField.class);
		txtDateOfAdmission = (TextField)binder.buildAndBind("Date Of Admission","dateOfAdmission",TextField.class);
		txtDateOfDischarge = (TextField)binder.buildAndBind("Date Of disCharge","dateOfDischarge",TextField.class);
		
		hospitalDetailsPanel = new Panel();
		FormLayout hospitalDetails = new FormLayout(txtHospitalName, txtHospitalCity , txtHospitalZone , txtDateOfAdmission, txtDateOfDischarge);
		addStyleForTextfield(hospitalDetails);
		
		hospitalDetails.setSpacing(true);
//		hospitalDetails.setMargin(true);
		hospitalDetailsPanel.setContent(hospitalDetails);
		hospitalDetailsPanel.setStyleName("girdBorder");
		return hospitalDetailsPanel;
	}
	
	private Panel buildInsuredDetailsLayout()
	{
		txtInsuredName = (TextField)binder.buildAndBind("Insured Name","insuredName",TextField.class);
		txtInsuredAge = (TextField)binder.buildAndBind("Age","insuredAge",TextField.class);
		txtSex = (TextField)binder.buildAndBind("Sex","sex",TextField.class);
		
		insuredDetailsPanel = new Panel();
		FormLayout insuredDetails = new FormLayout(txtInsuredName, txtInsuredAge , txtSex);
		
		addStyleForTextfield(insuredDetails);
		
		insuredDetails.setSpacing(true);
//		insuredDetails.setMargin(true);
		insuredDetailsPanel.setContent(insuredDetails);
		insuredDetailsPanel.setStyleName("girdBorder");
		return insuredDetailsPanel;
	}
	
	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("Submit");
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		//btnSubmit.setDisableOnClick(true);
		/*AbsoluteLayout absoluteLayout_3
		.addComponent(prSearchBtn, "top:100.0px;left:220.0px;");*/
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		//btnSubmit.s
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
/*		absoluteLayout_3.addComponent(resetBtn, "top:100.0px;left:329.0px;");
*/		//Vaadin8-setImmediate() btnCancel.setImmediate(true);

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
		hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}

	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {

		//reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		// Changes Ganesh For CR2019204 
		/* cmbSignificantClinicalInformation.setContainerDataSource(mastersValueContainer);
		 cmbSignificantClinicalInformation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbSignificantClinicalInformation.setItemCaptionPropertyId("value");*/
	}
	
	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		
		extraEffortEmployeeListenerTable.loadEmployeeMasterData(field , employeeDetailsList);
				
				//AutocompleteField<ExtraEmployeeEffortDTO> field, List<ExtraEmployeeEffortDTO> employeeDetailsList);
			
	}
	
	public void addListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
				if(validatePage())
				{
					setTableValuesDTO();
					
					
					String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
					
					bean.setStrUserName(userName);
					
					 if(SHAConstants.PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_SAVE_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PreauthWizardPresenter.PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
					} 
					 
					else if(SHAConstants.PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_SAVE_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENTSAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(WithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(DownSizePreauthWizardPresenter.DOWNSIZE_PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.NEGOTIATION_PREAUTH_REQUEST_SCREEN.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(NegotiationPreauthRequestPresenter.NEGOTIATION_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(DownsizePreauthRequestWizardPresenter.DOWNSIZE_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_PED_QUERY.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PEDQueryPresenter.PROCESS_PED_QUERY_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_REJECTION.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ProcessRejectionPresenter.PROCESS_REJECTION_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ConvertClaimPagePresenter.CONVERT_CLAIM_SAVE_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ConvertReimbursementPagePresenter.CONVERT_CLAIM_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.ACKNOWLEDGE_HOSPITAL_COMMUNICATION.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(AcknowledgeHospitalPresenter.ACKNOWLEDGE_HOSPITAL_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.FIELD_VISIT.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(FieldVisitPagePresenter.FIELD_VISIT_REP_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PED_REQUEST_PROCESS.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PEDRequestDetailsProcessPresenter.PROCESS_PED_PROCESS_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PED_REQUEST_APPROVER.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PEDRequestDetailsApprovePresenter.PROCESS_PED_APPROVE_SAVE_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.PED_TEAM_LEAD.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PROCESS_PED_TL_SAVE_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.ADVISE_ON_PED.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(AdviseOnPEDPresenter.ADVISE_ON_PED_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_CASHLESS.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(SubmitSpecialistPagePresenter.SUBMIT_SPECIALIST_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.PROCESS_COORDINATOR_REPLY.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(fileUploadPresenter.COORDINATOR_REPLY_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					 
					 
					 
					 else if(SHAConstants.PROCESS_SUBMIT_SPECIALIST_ADVISE_REIMBURSEMENT.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(SubmitSpecialistAdvisePresenter.PROCESS_SUBMIT_SPECIALIST_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					 
					 else if(SHAConstants.PROCESS_COORDINATOR_REPLY_REIMBURSEMENT.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(UploadTranslatedDocumentPresenter.COORDINATOR_REPLY_FOR_REIMB_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PROCESS_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(ProcessInvestigationInitiatedPresenter.PROCESS_INVESTIGATION_INTIATED_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.ASSIGN_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(AssignInvestigationPresenter.PROCESS_ASSIGN_INVESTIGATION_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.ACKNOWLEDGE_INVESTIGATION.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(AcknowledgeInvestigationCompletedPresenter.PROCESS_ACKNOWLEDGE_INVESTIGATION_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(AcknowledgeDocReceivedWizardPresenter.ACK_DOC_REC_SAVE_RRC_REQUEST_VALUES, bean);
						}
						
					 
					 else if(SHAConstants.CREATE_ROD.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(CreateRODWizardPresenter.CREATE_ROD_SAVE_RRC_REQUEST_VALUES, bean);
						}
						
					 
					 else if(SHAConstants.BILL_ENTRY.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(BillEntryWizardPresenter.BILL_ENTRY_SAVE_RRC_REQUEST_VALUES, bean);
						}

					 else if(SHAConstants.ADD_ADDITIONAL_DOCUMENTS.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(AddAditionalDocumentsPresenter.ADD_ADDITIONAL_DOC_REC_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PROCESS_UPLOAD_INVESTIGATION.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(UploadInvestigationReportPresenter.PROCESS_UPLOAD_INVESTIGATION_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PROCESS_CLAIM_REGISTRATION.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(ClaimRegistrationPresenter.PROCESS_CLAIM_REGISTRATION_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.SAVE_RRC_REQUEST_VALUES, bean);
					}
					
					else if(SHAConstants.CLAIM_REQUEST.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ClaimRequestWizardPresenter.SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.BILLING.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(BillingWizardPresenter.SAVE_BILLING_RRC_REQUEST_VALUES, bean);
					}
					else if(SHAConstants.FINANCIAL.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(FinancialWizardPresenter.SAVE_FINANCIAL_RRC_REQUEST_VALUES, bean);
					}
					 
					else if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(InitiateRRCRequestWizardPresenter.SAVE_INITIATE_RRC_REQUEST_VALUES, bean);
					}
					 

					else if(SHAConstants.DRAFT_INVESTIGATION_INTIATED.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(DraftInvestigationPresenter.PROCESS_DRAFT_INVESTIGATION_SAVE_RRC_REQUEST_VALUES, bean);
					}

					else if(SHAConstants.PA_PROCESS_REJECTION.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAProcessRejectionPresenter.PROCESS_REJECTION_SAVE_RRC_REQUEST_VALUES, bean);
					}
					 
					 else if(SHAConstants.PA_ACKNOWLEDGE_DOC_RECEIVED.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAAcknowledgeDocumentWizardPresenter.ACK_DOC_REC_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_CREATE_ROD.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PACreateRODWizardPresenter.CREATE_ROD_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAEnterBillDetailsWizardPresenter.BILL_ENTRY_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_CLAIM_REQUEST.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAClaimRequestWizardPresenter.SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_CLAIM_REQUEST_HOSP.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAHealthClaimRequestWizardPresenter.SAVE_CLAIM_REQUEST_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_BILLING.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PABillingWizardPresenter.SAVE_BILLING_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_BILLING_HOSP.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAHealthBillingWizardPresenter.SAVE_BILLING_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_PRE_MEDICAL.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAPreMedicalPreauthWizardPresenter.PRE_MEDICAL_PRE_AUTH_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_PREAUTH.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAPreauthWizardPresenter.PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PRE_MEDICAL_PROCESSING_ENHANCEMENT.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAPremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_ENHANCEMENT.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAPreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENTSAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_DOWNSIZE_REQUEST_PREAUTH.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PADownsizePreauthRequestWizardPresenter.DOWNSIZE_PREAUTH_REQUEST_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_DOWNSIZE_PREAUTH.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PADownSizePreauthWizardPresenter.DOWNSIZE_PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_WITHDRAW_PREAUTH.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAWithdrawPreauthWizardPresenter.WITHDRAW_PREAUTH_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(ConvertPAClaimPagePresenter.CONVERT_PA_CLAIM_SAVE_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_FINANCIAL_HOSP.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAHealthFinancialWizardPresenter.SAVE_FINANCIAL_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_FINANCIAL.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PANonHospFinancialWizardPresenter.SAVE_FINANCIAL_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_CLAIM_APPROVAL.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAClaimAprNonHosWizardPresenter.SAVE_CLAIM_RRC_REQUEST_VALUES, bean);
						}
					 
					 else if(SHAConstants.PA_PROCESS_CONVERT_CLAIM_SEARCH_BASED.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(PAConvertReimbursementPagePresenter.CONVERT_CLAIM_SAVE_RRC_REQUEST_VALUES, bean);
						}

				}
			}
			
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(presenterString))
				{
					/*ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation", "Are you sure you want to cancel ?",
					        "No", "yes", new ConfirmDialog.Listener() {
						
					            public void onClose(ConfirmDialog dialog) {
					                if (!dialog.isConfirmed()) {
					                    
					                	fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST, null);
					                } else {
					                    // User did not confirm
					                }
					            }
					        });
					
					dialog.setClosable(false);
					dialog.setStyleName(Reindeer.WINDOW_BLACK);*/
					//fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST, bean);
					
					HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
					buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
					buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
					HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
							.createConfirmationbox(SHAConstants.ACK_CLAIMED_AMOUNT_ALERT, buttonsNamewithType);
					Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
							.toString());
					Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
							.toString());
					yesButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							//optDocumentVerified.setValue(null);
						}
						});
					noButton.addClickListener(new ClickListener() {
						private static final long serialVersionUID = 7396240433865727954L;

						@Override
						public void buttonClick(ClickEvent event) {
							fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST, null);
						}
						});
				}
				else
				{
				popup.close();
				}
				//searable.doSearch();
				//resetAlltheValues();
			}
		});
	}

	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		StringBuffer successMessage = new StringBuffer();
		if(null != rrcRequestNo)
		{
		 successMessage.append("RRC RequestNo").append(" ").append(rrcRequestNo).append(" Successfully created !!!");
		}
		else
		{
			successMessage.append("Problem occured during intiating RRC request. Pleae contact administrator");
		}
		/*Label successLabel = new Label(
				"<b style = 'color: green;'>" + successMessage.toString() + "</b>",
				ContentMode.HTML);
*/
		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		/*Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createInformationBox(successMessage.toString() + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
			//	fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW, null);
				if(SHAConstants.INITIATE_RRC_REQUEST.equalsIgnoreCase(presenterString))
				{
					fireViewEvent(MenuItemBean.INITIATE_RRC_REQUEST, bean);
				}
				else
				{
				popup.close();
				}
			}
		});
		
		
	}
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		Boolean errorMsg = Boolean.FALSE;
		StringBuffer eMsg = new StringBuffer();
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if(null != this.quantumReductionListenerTable)
		{
			Boolean isValid = quantumReductionListenerTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.quantumReductionListenerTable.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(null != this.extraEffortEmployeeListenerTable)
		{
			Boolean isValid = extraEffortEmployeeListenerTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.extraEffortEmployeeListenerTable.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		if(null != this.rrcCategoryListenerTable)
		{
			Boolean isValid = rrcCategoryListenerTable.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.rrcCategoryListenerTable.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if (hasError) {
			//setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			btnSubmit.setDisableOnClick(false);
			errorMsg = Boolean.FALSE;
			//return !hasError;
		} 
		else 
		{
			btnSubmit.setDisableOnClick(true);
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
//			showOrHideValidation(false);
			errorMsg = Boolean.TRUE;
			//return true;
		}	
		
		return errorMsg;
	}
	
	 public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 this.rrcCategoryListenerTable.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 this.rrcCategoryListenerTable.setsourceValues(selectValueContainer, source, value);
	 }
	
	public void invalidate(){
		if(extraEffortEmployeeListenerTable != null){
			extraEffortEmployeeListenerTable.invalidate();
		}
		if(rrcCategoryListenerTable != null){
			rrcCategoryListenerTable.invalidate();
		}
		
		if(quantumReductionListenerTable != null){
			quantumReductionListenerTable.clearObjects();
		}
	}
	

}
