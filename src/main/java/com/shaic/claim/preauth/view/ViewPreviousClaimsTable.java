package com.shaic.claim.preauth.view;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.OPClaimMapper;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.outpatient.processOPpages.OPSpecialityDTO;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.outpatient.registerclaim.mapper.OutpatientMapper;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksForPreviousClaims;
import com.shaic.claim.pedrequest.view.ViewPedRequestDetail;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.policy.search.ui.opsearch.OPViewClaimStatus;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
import com.shaic.claim.viewEarlierRodDetails.BillingProcessingTable;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.FinancialApprovalTable;
import com.shaic.claim.viewEarlierRodDetails.ReceiptOfDocumentTable;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationDetailsWindow;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTable;
import com.shaic.claim.viewEarlierRodDetails.ViewPreviousPreauthSummaryTable;
import com.shaic.claim.viewEarlierRodDetails.Table.FieldVisitDetailsTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDiagnosisTable;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewSectionDetailsTable;
import com.shaic.domain.BankMaster;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OPClaim;
import com.shaic.domain.OPIntimation;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpOPClaim;
import com.shaic.domain.ViewTmpOPIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.domain.outpatient.OPDocumentBillEntry;
import com.shaic.domain.outpatient.OPHCHospitalDetails;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OPSpeciality;
import com.shaic.domain.outpatient.OutpatientService;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.domain.PolicyMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreviousClaimsTable extends GBaseTable<PreviousClaimsTableDTO> {
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;

	@Inject
	private ViewPedRequestDetail viewPedRequestDetails;
	
//	@Inject
//	private Instance<RodIntimationDetailsUI> rodIntimationDetailsInstance;
//	
//	private RodIntimationDetailsUI rodIntimationDetailsObj;
	
	@Inject
	private Instance<RodIntimationDetailsWindow> rodIntimationDetailsInstance;
	
	private RodIntimationDetailsWindow rodIntimationDetailsObj;
	
	
	
	@Inject
	private ViewDoctorRemarksForPreviousClaims viewDoctorRemarksUI;
	
	@Inject
	private OPViewClaimStatus opViewClaimStatus;
	
	@EJB 
	private OutpatientService outpatientService;
	
	@EJB
	private MasterService masterService;
	
	
	@EJB
	private HospitalService hospitalService;
	

	
	@EJB
	private AcknowledgementDocumentsReceivedService docAcknowledgementService;
	
	@EJB
	private PolicyService policyService;
	
	@Inject
	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
    @Inject
    private FieldVisitDetailsTable fieldVisitDetailsTableInstance;
    
    @Inject
    private ViewSectionDetailsTable sectionDetailsTableObj;
    
    @Inject
	private ViewPreviousPreauthSummaryTable preauthPreviousDetailsPage;
    
	@Inject
	private ReceiptOfDocumentTable receiptOfDocumentTable;
	
	@Inject
	private BillingProcessingTable billingProcessingTable;
	
	@Inject 
	private ViewDiagnosisTable diagnosisDetailsTable;
	
	@Inject
	private FinancialApprovalTable financialApprovalTable;
	
	@Inject
	private ViewPaymentTable paymentTableObj;
	
    @Inject
	private ViewPolicyDetails viewPolicyDetail;
    
    

	@Inject
	private IntimationService intimationService;

	@EJB
	private CreateRODService createRodService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2409362490913982090L;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"policyNumber",
		"policyYear",
		"claimNumber",
		"claimType",
		"intimationNumber",
		"insuredPatientName",
		"diagnosis",
		"admissionDate",
		"claimStatus",
		"claimAmount",
		"approvedAmount",
		"copayPercentage",
		"hospitalName",
		"pedName",
		"icdCodes",
	};*/
	
	
	/*public static final Object[] VISIBLE_COLUMNS_PA = new Object[] {
		"policyNumber",
		"policyYear",
		"claimNumber",
		"claimType",
		"benefits",
		"intimationNumber",
		"insuredPatientName",
		"patientDOB",
		"parentName",
		"parentDOB",
		"category",		
		"diagnosis",
		"admissionDate",
		"claimStatus",
		"claimAmount",
		"approvedAmount",
		"copayPercentage",
		"hospitalName",
		"pedName",
		"icdCodes",
	};*/

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPAColumnsForPreClaims()
	{
	 Object[] VISIBLE_COLUMNS_PA = new Object[] {
			"policyNumber",
			"policyYear",
			"claimNumber",
			"claimType",
			"benefits",
			"intimationNumber",
			"insuredPatientName",
			"patientDOB",
			"parentName",
			"parentDOB",
			"category",		
			"diagnosis",
			"admissionDate",
			"claimStatus",
			"claimAmount",
			"approvedAmount",
			"copayPercentage",
			"hospitalName",
			"pedName",
			"icdCodes",
		};
		table.setVisibleColumns(VISIBLE_COLUMNS_PA);
		table.setColumnHeader("benefits", "Benefit/Cover");
		
		table.removeGeneratedColumn("ViewClaimStatus");
		table.addGeneratedColumn("ViewClaimStatus",
				new Table.ColumnGenerator() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Button button = new Button("View Claim Status");
						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;
						if(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.OUT_PATIENT)
								||(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
							button.setEnabled(false);
						}
						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								
								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								ViewTmpClaim claim = claimService.getTmpClaimByClaimKey((Long) (previousClaimsTableDTO.getKey()));
								
								
								Map<String, Object> objectMap = new HashMap<String, Object>();
								objectMap.put(SHAConstants.VIEW_CLAIM_HISTORY_REQUEST, viewClaimHistoryRequest);
								objectMap.put(SHAConstants.FIELD_VISIT_DETAILS_TABLE, fieldVisitDetailsTableInstance);
								objectMap.put(SHAConstants.SECTION_DETAILS_TABLE, sectionDetailsTableObj);
								objectMap.put(SHAConstants.VIEW_PREVIOUS_PREAUTH_SUMMARY_TABLE, preauthPreviousDetailsPage);
								objectMap.put(SHAConstants.RECEIPT_OF_DOCUMENTTABLE, receiptOfDocumentTable);
								objectMap.put(SHAConstants.BILLING_PROCESSING_TABLE, billingProcessingTable);
								objectMap.put(SHAConstants.VIEW_DIAGNOSIS_TABLE, diagnosisDetailsTable);
								objectMap.put(SHAConstants.FINANCIAL_APPROVAL_TABLE, financialApprovalTable);
								objectMap.put(SHAConstants.VIEW_PAYMENT_TABLE, paymentTableObj);
								objectMap.put(SHAConstants.VIEW_POLICY_DETAILS, viewPolicyDetail);
								
								VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OBJECT_MAPPER,objectMap);	
								
								viewSearchClaimStatus(claim,objectMap);
							
							}


						});

						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.removeGeneratedColumn("viewDoctorRemarks");
		table.addGeneratedColumn("viewDoctorRemarks",
				new Table.ColumnGenerator() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Button button = new Button("Doctor Internal Note");
						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;

						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {

								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
								ViewTmpClaim claim = claimService.getTmpClaimByClaimKey((Long) (previousClaimsTableDTO.getKey()));
								
								if(claim != null){
									if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
										getDoctorRemarks(claim.getIntimation().getIntimationId());
									}else{
										viewDoctorRemarksFromPremia(claim.getIntimation());
									}

								}
								

							}
						});

						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
		table.removeGeneratedColumn("viewdocument");
		table.addGeneratedColumn("viewdocument", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Document");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						
//						if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
//							getViewDocumentFromPremia(claim);
//						}else{
////						viewPreviousInsurance.showViewPolicySchedule();
//							getViewDocument(claim);
//						}
						
						if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
							
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								
//								getViewDocument(claim);  
								viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());// fetch claim status from galaxy
								
								
							}else{
								getViewDocumentFromPremia(claim);  // fetch claim status from premia end.
							}
							
						}else{
							viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());  //fetch claim status only from galaxy
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Trails");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						Claim claim = claimService.getClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
									&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
								getViewClaimHistory(claim.getIntimation());
							}else {
								Intimation intimation = claim.getIntimation();
								getViewClaimHistoryForOPHealthCheckUp(intimation.getIntimationId());
							}
						}else{
							ViewTmpClaim viewTmpClaim = claimService.getTmpClaimByClaimKey(claimKey);
							if(viewTmpClaim != null && viewTmpClaim.getRecordFlag() != null && viewTmpClaim.getRecordFlag().equalsIgnoreCase("P")){
								viewSpecialistOpinionFromPremia(viewTmpClaim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewSpecialist");
		table.addGeneratedColumn("viewSpecialist", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Specialist Trails");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
										&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
									getSpecialityOpinion(claim.getIntimation().getKey());
								}
							}else{
								viewSpecialistOpinionFromPremia(claim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewPedDetails");
		table.addGeneratedColumn("viewPedDetails", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View PED Details");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
										&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
									getPedRequestDetails(claim.getIntimation().getKey());
								}
							}else{
								viewPedDetailsFromPremia(claim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(
				PreviousClaimsTableDTO.class));
		table.setWidth("100%");
		table.setHeight("300px");
//		table.setPageLength(6);
		
		Object[] VISIBLE_COLUMNS = new Object[] {
			"policyNumber",
			"policyYear",
			"claimNumber",
			"claimType",
			"intimationNumber",
			"insuredPatientName",
			"diagnosis",
			"admissionDate",
			"claimStatus",
			"claimAmount",
			"siRestrication",
			"approvedAmount",
			"copayPercentage",
			"hospitalName",
			"pedName",
			"icdCodes",
			"subLimitApplicable",
			"subLimitName",
			"subLimitAmount",
		};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.removeGeneratedColumn("ViewClaimStatus");
		table.addGeneratedColumn("ViewClaimStatus",
				new Table.ColumnGenerator() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Button button = new Button("View Claim Status");
						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;
						if(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.OUT_PATIENT)
								|| (dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
							button.setEnabled(false);
						}
						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								
								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								ViewTmpClaim claim = claimService.getTmpClaimByClaimKey((Long) (previousClaimsTableDTO.getKey()));
								
								
								Map<String, Object> objectMap = new HashMap<String, Object>();
								objectMap.put(SHAConstants.VIEW_CLAIM_HISTORY_REQUEST, viewClaimHistoryRequest);
								objectMap.put(SHAConstants.FIELD_VISIT_DETAILS_TABLE, fieldVisitDetailsTableInstance);
								objectMap.put(SHAConstants.SECTION_DETAILS_TABLE, sectionDetailsTableObj);
								objectMap.put(SHAConstants.VIEW_PREVIOUS_PREAUTH_SUMMARY_TABLE, preauthPreviousDetailsPage);
								objectMap.put(SHAConstants.RECEIPT_OF_DOCUMENTTABLE, receiptOfDocumentTable);
								objectMap.put(SHAConstants.BILLING_PROCESSING_TABLE, billingProcessingTable);
								objectMap.put(SHAConstants.VIEW_DIAGNOSIS_TABLE, diagnosisDetailsTable);
								objectMap.put(SHAConstants.FINANCIAL_APPROVAL_TABLE, financialApprovalTable);
								objectMap.put(SHAConstants.VIEW_PAYMENT_TABLE, paymentTableObj);
								objectMap.put(SHAConstants.VIEW_POLICY_DETAILS, viewPolicyDetail);
								objectMap.put(SHAConstants.INTIMATION_NUMBER, previousClaimsTableDTO.getIntimationNumber());
								
								VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OBJECT_MAPPER,objectMap);	
								
								viewSearchClaimStatus(claim,objectMap);
							
							}


						});

						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.removeGeneratedColumn("viewDoctorRemarks");
		table.addGeneratedColumn("viewDoctorRemarks",
				new Table.ColumnGenerator() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Button button = new Button("Doctor Internal Note");
						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;

						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {

								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
								ViewTmpClaim claim = claimService.getTmpClaimByClaimKey((Long) (previousClaimsTableDTO.getKey()));
								
								if(claim != null){
									if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
										getDoctorRemarks(claim.getIntimation().getIntimationId());
									}else{
										viewDoctorRemarksFromPremia(claim.getIntimation());
									}

								}
								

							}
						});

						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
		table.removeGeneratedColumn("viewdocument");
		table.addGeneratedColumn("viewdocument", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Document");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						
//						if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
//							getViewDocumentFromPremia(claim);
//						}else{
////						viewPreviousInsurance.showViewPolicySchedule();
//							getViewDocument(claim);
//						}
						
						if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
							
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								
//								getViewDocument(claim);  
								viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());// fetch claim status from galaxy
								
								
							}else{
								getViewDocumentFromPremia(claim);  // fetch claim status from premia end.
							}
							
						}else{
							viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());  //fetch claim status only from galaxy
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Trails");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						Claim claim = claimService.getClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
									&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
								getViewClaimHistory(claim.getIntimation());
							}else {
								Intimation intimation = claim.getIntimation();
								getViewClaimHistoryForOPHealthCheckUp(intimation.getIntimationId());
							}
						}else{
							ViewTmpClaim viewTmpClaim = claimService.getTmpClaimByClaimKey(claimKey);
							if(viewTmpClaim != null && viewTmpClaim.getRecordFlag() != null && viewTmpClaim.getRecordFlag().equalsIgnoreCase("P")){
								viewSpecialistOpinionFromPremia(viewTmpClaim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewSpecialist");
		table.addGeneratedColumn("viewSpecialist", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Specialist Trails");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
										&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
									getSpecialityOpinion(claim.getIntimation().getKey());
								}
							}else{
								viewSpecialistOpinionFromPremia(claim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewPedDetails");
		table.addGeneratedColumn("viewPedDetails", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View PED Details");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpClaim claim = claimService.getTmpClaimByClaimKey(claimKey);
						if(claim != null) {
							if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
								if(claim.getClaimType() != null && ! claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
										&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
									getPedRequestDetails(claim.getIntimation().getKey());
								}
							}else{
								viewPedDetailsFromPremia(claim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		

	}
	
	public void setOPColumnsForPreClaims()
	{
	 Object[] VISIBLE_COLUMNS_OP = new Object[] {
			 	"policyNumber",
				"policyYear",
				"claimNumber",
				"claimType",
				"intimationNumber",
				"insuredPatientName",
				"admissionDate",
				"claimStatus",
				"claimAmount",
				"approvedAmount"
		};
		table.setVisibleColumns(VISIBLE_COLUMNS_OP);
		
		table.removeGeneratedColumn("ViewClaimStatus");
		table.addGeneratedColumn("ViewClaimStatus",
				new Table.ColumnGenerator() {
					/**
			 * 
			 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						Button button = new Button("View Claim Status");
						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;
						if(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.OUT_PATIENT)
								||(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.HEALTH_CHECK_UP))){
							button.setEnabled(false);
						}
						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								
								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								ViewTmpOPClaim claim = claimService.getTmpOPClaimforOPIntimation((Long) (previousClaimsTableDTO.getIntimationKey()));
								
								
								Map<String, Object> objectMap = new HashMap<String, Object>();
								objectMap.put(SHAConstants.VIEW_CLAIM_HISTORY_REQUEST, viewClaimHistoryRequest);
								objectMap.put(SHAConstants.FIELD_VISIT_DETAILS_TABLE, fieldVisitDetailsTableInstance);
								objectMap.put(SHAConstants.SECTION_DETAILS_TABLE, sectionDetailsTableObj);
								objectMap.put(SHAConstants.VIEW_PREVIOUS_PREAUTH_SUMMARY_TABLE, preauthPreviousDetailsPage);
								objectMap.put(SHAConstants.RECEIPT_OF_DOCUMENTTABLE, receiptOfDocumentTable);
								objectMap.put(SHAConstants.BILLING_PROCESSING_TABLE, billingProcessingTable);
								objectMap.put(SHAConstants.VIEW_DIAGNOSIS_TABLE, diagnosisDetailsTable);
								objectMap.put(SHAConstants.FINANCIAL_APPROVAL_TABLE, financialApprovalTable);
								objectMap.put(SHAConstants.VIEW_PAYMENT_TABLE, paymentTableObj);
								objectMap.put(SHAConstants.VIEW_POLICY_DETAILS, viewPolicyDetail);
								
								VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OBJECT_MAPPER,objectMap);	
								
								OPIntimation a_intimation = intimationService.getOPIntimationByKey(previousClaimsTableDTO.getIntimationKey());

								OPClaim a_claim = claimService.getOPClaimforIntimation(a_intimation.getKey());

								if (a_claim != null) {
									viewOPClaimStatusUpdated(a_intimation.getIntimationId());
								}
							
							}


						});

						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
	
		
		
		table.removeGeneratedColumn("viewdocument");
		table.addGeneratedColumn("viewdocument", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Document");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
						ViewTmpOPClaim claim = claimService.getTmpOPClaimforOPIntimation((Long) (previousClaimsTableDTO.getIntimationKey()));
						viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());  //fetch claim status only from galaxy
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("viewTrails");
		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				Button button = new Button("View Trails");
				button.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
//						Claim claim = claimService.getClaimByClaimKey(claimKey);
						ViewTmpOPClaim claim = claimService.getTmpOPClaimforOPIntimation((Long) (previousClaimsTableDTO.getIntimationKey()));
						if(claim != null) {
							if(claim.getClaimType() != null &&  claim.getClaimType().getKey().equals(ReferenceTable.OUT_PATIENT)
									&& ! claim.getClaimType().getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
								getViewClaimHistoryForOPHealthCheckUp(claim.getIntimation().getIntimationId());
							}else {
								/*Intimation intimation = claim.getIntimation();
								getViewClaimHistoryForOPHealthCheckUp(intimation.getIntimationId());*/
							}
						}else{
							ViewTmpClaim viewTmpClaim = claimService.getTmpClaimByClaimKey(claimKey);
							if(viewTmpClaim != null && viewTmpClaim.getRecordFlag() != null && viewTmpClaim.getRecordFlag().equalsIgnoreCase("P")){
								viewSpecialistOpinionFromPremia(viewTmpClaim.getIntimation().getIntimationId());
							}
						}
						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});


		
	}
	
	
	public void viewSearchClaimStatus(ViewTmpClaim claim,Map<String, Object> mapper) {
		
		ViewTmpIntimation intimation = claim.getIntimation();

		if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
			
			if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
					
				showClaimStatus(claim,mapper);                            // fetch claim status from galaxy
				
			}else{
				getViewClaimStatusFromPremia((String)mapper.get(SHAConstants.INTIMATION_NUMBER));    // fetch claim status from premia end.
			}
			
		}else{
			showClaimStatus(claim,mapper);     //fetch claim status only from galaxy
		}

		
	}
	
	public void getViewClaimStatusFromPremia(String intimationNo) {
		VerticalLayout vLayout = new VerticalLayout();

		String strDmsViewURL = BPMClientContext.PREMIA_CLAIM_VIEW_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/

		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+intimationNo));
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setWidth("100%");
		browserFrame.setHeight("150%");
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setWidth("100%");
		vLayout.setHeight("110%");
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
					//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
					popup.close();
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	private void showClaimStatus(ViewTmpClaim claim,Map<String, Object>mapper) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ClaimStatusRegistrationDTO registrationDetails = instance.getRegistrationDetails(claim);
		//ViewTmpIntimation intimation = intimationService.getTmpIntimationByKey(claim.getIntimation().getKey());
		Intimation intimationByKey = intimationService.getIntimationByKey(claim.getIntimation().getKey());
		ViewClaimStatusDTO intimationDetails = instance.getViewClaimStatusDto(intimationByKey);
		Hospitals hospitals = hospitalService.getHospitalById(intimationByKey.getHospital());
		getHospitalDetails(intimationDetails, hospitals);
		intimationDetails.setClaimStatusRegistrionDetails(registrationDetails);
		intimationDetails.setClaimKey(claim.getKey());
		Long claimKey = claim.getKey();
		
		NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationByKey);
		
		PreauthDTO preauthDTO = new PreauthDTO();
		preauthDTO.setNewIntimationDTO(intimationDto);
		Claim claimObj = intimationService.getClaimforIntimation(intimationByKey.getKey());
		if(claimObj!=null){
			preauthDTO.setCrmFlagged(claimObj.getCrcFlag());
			preauthDTO.setVipCustomer(claimObj.getIsVipCustomer());
			preauthDTO.setClaimPriorityLabel(claimObj.getClaimPriorityLabel());
		}
		intimationDetails.setPreauthDTO(preauthDTO);
		
		setPaymentDetails(intimationDetails, claim);
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = docAcknowledgementService.listOfEarlierAckByClaimKey(claimKey,null);
		intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);
		
		if(ReferenceTable.getGMCProductList().containsKey(intimationByKey.getPolicy().getProduct().getKey())){
			 boolean isjiopolicy = false;	
				isjiopolicy = intimationService.getJioPolicyDetails(intimationByKey.getPolicy().getPolicyNumber());
				  
				intimationDetails.setJioPolicy(isjiopolicy);
			      Insured insuredByKey = intimationService.getInsuredByKey(intimationByKey.getInsured().getKey());
			      Insured MainMemberInsured = null;
			      
			      if(insuredByKey.getDependentRiskId() == null){
			    	  MainMemberInsured = insuredByKey;
			      }else{
			    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationByKey.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
			    	  MainMemberInsured = insuredByPolicyAndInsuredId;
			      }
			      
			      if(MainMemberInsured != null){
			    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
			      }		
			}		
		
		rodIntimationDetailsObj.init(intimationDetails,null,mapper);
		popup.setContent(rodIntimationDetailsObj);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		EarlierRodMapper.invalidate(instance);
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = reimbursementService
						.getRimbursementForPayment(claim.getClaimId());
			}
			
			if(reimbursementForPayment != null){
			bean.setBankName(reimbursementForPayment.getBankName());
			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
			bean.setAccountName(reimbursementForPayment.getAccountNumber());
			bean.setBranchName(reimbursementForPayment.getBranchName());
			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
				
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setNeftDate(formatDate);
				}
			 bean.setChequeNumber(null);
			}else{
				if(reimbursementForPayment.getChequeDDDate() != null){
					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
					bean.setChequeDate(formatDate);
				}
			}
			

			return reimbursementForPayment;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	

	public void getViewClaimHistory(Intimation intimation) {
		
		if(intimation != null){

		Boolean result = true;
		
		if (intimation != null) {

		result = viewClaimHistoryRequest.showCashlessAndReimbursementHistory(intimation);
			
			
			if(result){
					Window popup = new com.vaadin.ui.Window();
					popup.setCaption("View History");
					popup.setWidth("75%");
					popup.setHeight("75%");
					popup.setContent(viewClaimHistoryRequest);
					popup.setClosable(true);
					popup.center();
					popup.setResizable(true);
					popup.addCloseListener(new Window.CloseListener() {
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
		
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println("Close listener called");
						}
					});
		
					popup.setModal(true);
					UI.getCurrent().addWindow(popup);
			}else{
					getErrorMessage("History is not available");
			}
		 }
		
		}else{
			getErrorMessage("History is not available");
		}
	}
	
  public void getViewClaimHistoryForOPHealthCheckUp(String intimationNo) {
		
		if(intimationNo != null){
		OPIntimation intimation = intimationService
				.getOPIntimationByNo(intimationNo);
		if (intimation != null) {
			viewClaimHistoryRequest.showOPHealthCheckUpOPClaimHistory(intimation);
			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("View History");
			popup.setWidth("75%");
			popup.setHeight("75%");
			popup.setContent(viewClaimHistoryRequest);
			popup.setClosable(true);
			popup.center();
			popup.setResizable(true);
			popup.addCloseListener(new Window.CloseListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@Override
				public void windowClose(CloseEvent e) {
					System.out.println("Close listener called");
				}
			});

			popup.setModal(true);
			UI.getCurrent().addWindow(popup);
		}
		
		}else{
			getErrorMessage("History is not available");
		}
	}
	
	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void getHospitalDetails(
			ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
		EarlierRodMapper.invalidate(instance);
	}
	
	public void getViewDocument(ViewTmpClaim claim) {

		ViewTmpIntimation intimation = claim.getIntimation();
		String strPolicyNo = intimation.getPolicyNumber();
		Long insuredKey = intimation.getInsured().getKey();
		Insured insuredByKey = intimationService.getInsuredByKey(insuredKey);
		//String strPolicyNo = "P/181219/01/2014/001945";
		getViewDocumentByPolicyNo(strPolicyNo,insuredByKey);

	//	UI.getCurrent().addWindow(viewDocuments);
		
	}
	
	public void getViewDocumentFromPremia(ViewTmpClaim claim){
		
		ViewTmpIntimation intimation2 = claim.getIntimation();
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.CLAIMS_DMS_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		ViewTmpIntimation tmpIntimation = claimService.getTmpIntimationObject(intimation2.getKey());
		Long key = null;
		if(null != tmpIntimation)
		{
			key = tmpIntimation.getKey();
		}
		
		
		//BrowserWindowOpener browserFrame = new BrowserWindowOpener(new ExternalResource(strDmsViewURL+key));
		//browserFrame.
		
		//new StreamReso
		String url = strDmsViewURL+key;
//		ExternalResource ex =  new ExternalResource(strDmsViewURL+key);
//		//ExternalResource ex =  new ExternalResource("http://192.168.1.46:8080/dms/ManageDigitalFileAction.do?method=getHeirarchy&hidIntimationId=896097","text/html");
//	//	ex.setMIMEType("text/html");
//
//		BrowserFrame browserFrame = new BrowserFrame("",
//				ex);
//		browserFrame.setWidth("100%");
//		browserFrame.setHeight("120%");
//		browserFrame.setData(key);
		
//		getUI().getPage().open(url, "_blank");
//		getUI().getPage().open("", "_self");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
		/*Embedded browserFrame = new Embedded("Vaadin web site", new ExternalResource(
				strDmsViewURL+intimation2.getKey()));
		browserFrame.setType(Embedded.TYPE_BROWSER);
		browserFrame.setWidth("100%");
		browserFrame.setHeight("400px");*/
       // addComponent(e);
		
		
		/*vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setWidth("100%");
		vLayout.setHeight("130%");
		popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		//popup.setContent(browserF);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
		
	}
	
	/*private ViewTmpIntimation getTmpIntimationByKey(Long intimationKey)
	{
		Query query = enti
	}*/
	
	public void getViewDocumentByPolicyNo(String strPolicyNo,Insured insured) {
		VerticalLayout vLayout = new VerticalLayout();
		
		String strDmsViewURL = null;
		Policy policyObj = null;
		BrowserFrame browserFrame = null;
		
		if (strPolicyNo != null) {
			policyObj = policyService.getByPolicyNumber(strPolicyNo);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
					strDmsViewURL = BPMClientContext.BANCS_POLICY_DOCUMENT_URL;
					strDmsViewURL = strDmsViewURL.replace("POLICY", strPolicyNo);
					if(ReferenceTable.getGMCProductList().containsKey(policyObj.getProduct().getKey())){
						strDmsViewURL = strDmsViewURL.replace("MEMBER", insured!=null?String.valueOf(insured.getSourceRiskId()!=null?insured.getSourceRiskId():""):"");		
					}else{
						strDmsViewURL = strDmsViewURL.replace("MEMBER", "");
					}
					getUI().getPage().open(strDmsViewURL, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL));
				}else{
					strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
					String dmsToken = intimationService.createDMSToken(strPolicyNo);
					getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
//					browserFrame = new BrowserFrame("",new ExternalResource(strDmsViewURL+dmsToken));
				}
			}
		}
		/*String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));
		
		
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		//browserFrame.setWidth("600px");
		//browserFrame.setHeight("400px");
		browserFrame.setSizeFull();
		vLayout.addComponent(browserFrame);
		
		Button btnSubmit = new Button("OK");
		
		btnSubmit.setCaption("CLOSE");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		vLayout.addComponent(btnSubmit);
		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
		vLayout.setSizeFull();
		final Window popup = new com.vaadin.ui.Window();
		
		popup.setCaption("");
		popup.setWidth("100%");
		popup.setHeight("100%");
		//popup.setSizeFull();
		popup.setContent(vLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

		
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void viewUploadedDocumentDetails(String intimationNo) {

		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = createRodService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}*/

		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimationService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		  tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
		/*Below code commented due to security reason 
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNo;*/
		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
	/*	popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/

	}
	
	public void viewSpecialistOpinionFromPremia(String intimationNo) {

		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = createRodService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}*/

		BPMClientContext bpmClientContext = new BPMClientContext();
		//Bancs Changes Start
		Intimation intimationByNo = intimationService.getIntimationByNo(intimationNo);

		Policy policyObj = null;
		Builder builder = null;
		String url = null;
		
		if(intimationByNo != null){
			if(intimationByNo.getPolicy() != null){
				policyObj = policyService.getByPolicyNumber(intimationByNo.getPolicy().getPolicyNumber() );
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						url = bpmClientContext.getSpecialistBancsUrl() + intimationNo;
					}else{
						url = bpmClientContext.getSpecialistPremiaUrl() + intimationNo;
					}
				}
			}
			
		}
		
		//Bancs Changes End
		//String url = bpmClientContext.getSpecialistPremiaUrl() + intimationNo;
		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
	/*	popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/

	}
	
	
	public void viewPedDetailsFromPremia(String intimationNo) {

		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = createRodService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}*/

		BPMClientContext bpmClientContext = new BPMClientContext();
		//Bancs Changes Start
		Intimation intimationByNo = intimationService.getIntimationByNo(intimationNo);

		Policy policyObj = null;
		Builder builder = null;
		String url = null;
		
		if(intimationByNo != null){
			if(intimationByNo.getPolicy() != null){
				policyObj = policyService.getByPolicyNumber(intimationByNo.getPolicy().getPolicyNumber() );
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						url = bpmClientContext.getPedDetailsForBancsUrl() + intimationNo;
					}else{
						url = bpmClientContext.getPedDetailsForPremiaUrl() + intimationNo;
					}
				}
			}
			
		}
		
		//Bancs Changes End
		//String url = bpmClientContext.getPedDetailsForPremiaUrl() + intimationNo;
		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
	/*	popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/

	}
	
	
	public void viewDoctorRemarksFromPremia(ViewTmpIntimation intimaton) {

		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = createRodService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}*/

		BPMClientContext bpmClientContext = new BPMClientContext();
		
		//Bancs Changes Start
				Intimation intimationByNo = intimationService.getIntimationByNo(intimaton.getIntimationId());

				Policy policyObj = null;
				Builder builder = null;
				String url = null;
				
				if(intimationByNo != null){
					if(intimationByNo.getPolicy() != null){
						policyObj = policyService.getByPolicyNumber(intimationByNo.getPolicy().getPolicyNumber() );
						if (policyObj != null) {
							if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
								url = bpmClientContext.getDoctorRemarksBancsUrl();
							}else{
								url = bpmClientContext.getDoctorRemarksPremiaUrl();
							}
						}
					}
					
				}
				
				//Bancs Changes End
		//String url = bpmClientContext.getDoctorRemarksPremiaUrl();
		
		url = url.replace("%", intimaton.getIntimationId());
		url = url.replace("||", intimaton.getPolicyNumber());
		
		url = url.replace("#", intimaton.getInsured().getHealthCardNumber());
		
		String[] split = intimaton.getIntimationId().split("/");
		String intimationYear = split[1];
		String endNumber = split[3];
		
		url = url.replace("(", endNumber);
		url = url.replace(",", intimationYear);
		
		
//		url = url + intimaton.getPolicy().getPolicyNumber();
		

		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
	/*	popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(dmsDTO, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/

	}



	@Override
	public void tableSelectHandler(PreviousClaimsTableDTO t) {
		
		
			
	}

	@Override
	public String textBundlePrefixString() {
		return "previousclaimtable-preauth-";
	}

	public void setListOfPreviousClaims(BeanItemContainer<PreviousClaimsTableDTO> dto) {
		this.table.setContainerDataSource(dto);
	}
	
	public void getDoctorRemarks(String intimationNo) {

		viewDoctorRemarksUI.init(intimationNo);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Doctor Remarks");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewDoctorRemarksUI);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void setCaption(String strCaption)
	{
		this.table.setCaption(strCaption);
	}
	
	public void getSpecialityOpinion(Long intimationKey) {
		viewSpecialistOpinionDetails.init(intimationKey);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Specialist Opinion");
		popup.setWidth("70%");
		popup.setHeight("40%");
		popup.setResizable(true);
		popup.setContent(viewSpecialistOpinionDetails);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void getPedRequestDetails(Long intimationKey) {

		viewPedRequestDetails.init(intimationKey);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("PED Request Details");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewPedRequestDetails);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
	
	public void viewOPClaimStatusUpdated(String intimationNo) {

		ViewTmpOPClaim claim = null;

		try {
			final ViewTmpOPIntimation intimation = intimationService.searchbyOPIntimationNoFromViewIntimation(intimationNo);
			claim = claimService.getTmpOPClaimforOPIntimation(intimation.getKey());
			if (claim != null) {
				OPClaim claimByKey = claimService.getOPClaimByKey(claim.getKey());
				PolicyDto policyDto = new PolicyMapper().getPolicyDto(claimByKey.getIntimation().getPolicy());
				
				
				OPHealthCheckup opHealthByClaimKey = outpatientService.getOpHealthByClaimKey(claimByKey.getKey());
				List<OPHCHospitalDetails> opHospitalDtlsByKey = outpatientService.getHospitalDetailsByHCKey(opHealthByClaimKey.getKey());
				List<OPDocumentBillEntry> billDtls = outpatientService.getOpBillEntryByOPHealthKey(opHealthByClaimKey.getKey());
				List<OPSpeciality> specialityList = outpatientService.getSpecialityDtlsByHcKey(opHealthByClaimKey.getKey());
				OutpatientMapper mapper = new OutpatientMapper();
//				List<DiagnosisDetailsOPTableDTO> diagnosisListenerTableList = outpatientService.getDiagnosisList(opHealthByClaimKey.getKey());
				OutPatientDTO mainDTO = mapper.getOutpatientDTO(opHealthByClaimKey);
//				mainDTO.setDiagnosisListenerTableList(diagnosisListenerTableList);
				mainDTO.setStrUserName((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID));
				mainDTO.setStrPassword((String)VaadinSession.getCurrent().getAttribute(BPMClientContext.PASSWORD));
				//mainDTO.setHumanTask(tableDTO.getOpHumanTask());
//				Map<String, Integer> healthCheckupFlag = calcService.getHealthCheckupFlag(claimByKey.getIntimation().getPolicy().getKey());
				/*if(healthCheckupFlag.get(SHAConstants.OUTPATIENT_FLAG) == 1) {
					mainDTO.setOutpatientFlag(true);
				}
				
				if(healthCheckupFlag.get(SHAConstants.HEALTH_CHECK_UP_FLAG) == 1) {
					mainDTO.setHealthCheckupFlag(true);
				}*/
				NewIntimationDto newIntimationDto = intimationService.getOPIntimationDto(claimByKey.getIntimation());
				// newIntimationDto.getPolicy().getProduct().getAutoRestoration()
				ClaimDto claimDTO =  OPClaimMapper.getInstance().getClaimDto(claimByKey);
				mainDTO.setNewIntimationDTO(newIntimationDto);
				mainDTO.setClaimDTO(claimDTO);
				mainDTO.setPolicy(claimByKey.getIntimation().getPolicy());
				mainDTO.setClaimKey(claimByKey.getKey());
				mainDTO.setIntimationId(claimByKey.getIntimation().getIntimationId());
				mainDTO.setSelectedHospital(newIntimationDto.getHospitalDto());
				mainDTO.setApprovalRemarks(opHealthByClaimKey.getApprovalRemarks());
				mainDTO.setApprovedAmt(opHealthByClaimKey.getAmountPayable());
				String pioName = "";
				if(claimByKey.getIntimation() != null && claimByKey.getIntimation().getPolicy().getHomeOfficeCode() != null) {
					List<OrganaizationUnit> insuredOfficeNameByDivisionCode = policyService.getInsuredOfficeNameByDivisionCode(claimByKey.getIntimation().getPolicy().getHomeOfficeCode());
					 if(insuredOfficeNameByDivisionCode != null && !insuredOfficeNameByDivisionCode.isEmpty()) {
			    		pioName = insuredOfficeNameByDivisionCode.get(0).getOrganizationUnitName();
			    	  }					        
				}
				Long docReceivedId = ReferenceTable.RECEIVED_FROM_HOSPITAL;
//				mainDTO.setPreviousAccntDetailsList(calcService.getPreviousAccountDetails(claimByKey.getIntimation().getInsured().getInsuredId(),docReceivedId));
				mainDTO.setPioName(pioName);
				mainDTO.setPolicyDto(policyDto);
				
				mainDTO.getDocumentDetails().setDocEmailId(claimByKey.getIntimation().getCallerEmail());
				mainDTO.getDocumentDetails().setReasonforConsultation(claimByKey.getIntimation().getAdmissionReason());
				mainDTO.getDocumentDetails().setDocSubmittedContactNo(claimByKey.getDoctorNo());
				mainDTO.getDocumentDetails().setDocSubmittedName(claimByKey.getIntimation().getDoctorName());
				mainDTO.getDocumentDetails().setRemarksForEmergencyAccident(claimByKey.getRemarksForEmergencyAccident());
				mainDTO.getDocumentDetails().setEmergencyFlag(claimByKey.getEmergencyFlag());
				mainDTO.getDocumentDetails().setAccidentFlag(claimByKey.getAccidentFlag());
				
				List<OPSpecialityDTO> specialityDto = new ArrayList<OPSpecialityDTO>();
				if(specialityList != null && !specialityList.isEmpty()){
					for (OPSpeciality opSpeciality : specialityList) {
						OPSpecialityDTO specList = new OPSpecialityDTO();
						SelectValue spe = new SelectValue();
						Long specId = opSpeciality.getSpecialityTypeId();
						SpecialityType spType = outpatientService.getSpecialityTypeByKey(specId);	
						spe.setId(spType.getKey());
						spe.setValue(spType.getValue());
						specList.setSpecialityType(spe);
						SelectValue ped = new SelectValue();
						if(opSpeciality != null && opSpeciality.getPedRelatedId().equals(1061l)){
							ped.setId(1061l);
							ped.setValue("Not Related");
						} else {
							ped.setId(1062l);
							ped.setValue("Related PED");
						}
						specList.setPed(ped);
						specList.setRemarks(opSpeciality.getRemarks());
						specialityDto.add(specList);
					}
				}
				
				if(specialityDto != null && !specialityDto.isEmpty()){
					mainDTO.setSpecialityDTOList(specialityDto);
				}
				
				if(opHospitalDtlsByKey != null && !opHospitalDtlsByKey.isEmpty()){
					for (OPHCHospitalDetails ophcHospitalDetails : opHospitalDtlsByKey) {
						if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Consultation")){
							mainDTO.getBenefitsAvailedDto().setState(ophcHospitalDetails.getState());
							mainDTO.getBenefitsAvailedDto().setCity(ophcHospitalDetails.getCity());
							mainDTO.getBenefitsAvailedDto().setHospitalName(ophcHospitalDetails.getHospitalName());
							mainDTO.getBenefitsAvailedDto().setHospitalConsulationName(ophcHospitalDetails.getHospitalConsulationName());
							mainDTO.getBenefitsAvailedDto().setHospitalAddress(ophcHospitalDetails.getHospitalAddress());
							mainDTO.getBenefitsAvailedDto().setHospitalContactNumber(ophcHospitalDetails.getHospitalContactNo());
							mainDTO.getBenefitsAvailedDto().setHospitalFaxNo(ophcHospitalDetails.getHospitalFaxNo());
							mainDTO.setConsulation(false);
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Diagnostics")){
							mainDTO.setDiagnosis(true);
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Physiotherapy")){
							mainDTO.setPhysiotherapy(true);
						} else if(ophcHospitalDetails.getBenefitAvailedId().equalsIgnoreCase("Medicine")){
							mainDTO.setMedicine(true);
						} 
					}
				}
				
				List<UploadDocumentDTO> listOfDocs = new ArrayList<UploadDocumentDTO>();
				if(billDtls != null && !billDtls.isEmpty()){
					int i = 1;
					for (OPDocumentBillEntry opDocumentBillEntry : billDtls) {
						UploadDocumentDTO doc = new UploadDocumentDTO();
						Long billType = opDocumentBillEntry.getBillTypeId();
						MastersValue billValue = masterService.getMaster(billType);
						doc.setFileTypeValue(billValue.getValue());
						doc.setBillDate(opDocumentBillEntry.getBillDate());
						doc.setBillNo(opDocumentBillEntry.getBillNumber());
						doc.setBillAmt(opDocumentBillEntry.getClaimedAmount());
						doc.setDeductibleAmt(opDocumentBillEntry.getDeductibleAmt());
						doc.setNonPaybleAmt(opDocumentBillEntry.getNonPayableAmount());
						doc.setBillValue(opDocumentBillEntry.getPayableAmt().doubleValue());
						doc.setRemarks(opDocumentBillEntry.getBillEntryRemarks());
						String fileName = SHAFileUtils.viewFileByToken(opDocumentBillEntry.getDocumentToken());
						doc.setFileName(opDocumentBillEntry.getFileName() != null ? opDocumentBillEntry.getFileName() :"");
						doc.setDmsDocToken(opDocumentBillEntry.getDocumentToken());
						doc.setsNo(String.valueOf(i++));
						listOfDocs.add(doc);
					}
				}
				mainDTO.setUploadedDocsTableList(listOfDocs);
				
				Long paymentMode = opHealthByClaimKey.getPaymentNoteId();
				if(paymentMode != null){
					int i = 1;
					mainDTO.getViewPaymentDto().setSerialNo(i);
					MastersValue paymentModeValue = masterService.getMaster(paymentMode);
					mainDTO.getViewPaymentDto().setPaymentTypeValue(paymentModeValue.getValue());
					if(opHealthByClaimKey.getIfscCode() != null){
						BankMaster bankDtls = masterService.getBankDetails(opHealthByClaimKey.getIfscCode());
						mainDTO.getViewPaymentDto().setBankName(bankDtls.getBankName());
						mainDTO.getViewPaymentDto().setBranchName(bankDtls.getBranchName());
					}
					mainDTO.getViewPaymentDto().setAccontNo(opHealthByClaimKey.getAccountNumber());
					mainDTO.getViewPaymentDto().setIfscCode(opHealthByClaimKey.getIfscCode());
					mainDTO.getViewPaymentDto().setChequeNo(opHealthByClaimKey.getChequeNo());
					mainDTO.getViewPaymentDto().setChequeDate(opHealthByClaimKey.getChequeDate());
					
					String claimType = claimByKey.getClaimType().getValue().toString();
					mainDTO.getViewPaymentDto().setClaimType(claimType);
				}


				opViewClaimStatus.init(mainDTO, null);
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(opViewClaimStatus);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(true);
				popup.addCloseListener(new Window.CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						System.out.println("Close listener called");
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
			}
		} catch (Exception e) {
			//Notification.show("Claim Number is not generated", Notification.TYPE_ERROR_MESSAGE);
			e.printStackTrace();
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	}


}
