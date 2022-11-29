package com.shaic.claim.premedical.wizard;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.intimation.CashLessTableDetails;
import com.shaic.claim.intimation.CashlessTable;
import com.shaic.claim.pedrequest.view.ViewDoctorRemarksForPreviousClaims;
import com.shaic.claim.pedrequest.view.ViewPedRequestDetail;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.registration.previousinsurance.view.ViewPreviousInsurance;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.specialistopinion.view.ViewSpecialistOpinionDetails;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.NewIntimationService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.ClaimStatusDto;
import com.sun.jersey.api.client.WebResource.Builder;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreauthPreviousClaimsTable extends GBaseTable<PreviousClaimsTableDTO> {

	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	////private static Window popup;
	
	@Inject
	private Instance<RodIntimationDetailsUI> rodIntimationDetailsInstance;
	
	private RodIntimationDetailsUI rodIntimationDetailsObj;
	
	@Inject
	private ViewDoctorRemarksForPreviousClaims viewDoctorRemarksUI;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService docAcknowledgementService;
	
	@Inject
	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
	
	@Inject
	private ViewPedRequestDetail viewPedRequestDetails;
	
	@EJB
	private PolicyService policyService;

	@Inject
	private CashLessTableDetails cashLessTableDetails;

	@Inject
	private CashlessTable cashlessTable;

	@Inject
	private NewIntimationService newIntimationService;


	@Inject
	private Intimation intimation;
	
	@Inject
	private IntimationService intimationService;
	
	@Inject
	private ClaimStatusDto claimStatusDto;
	
	@Inject
	private CashlessDetailsService cashlessDetailsService;
	
	@Inject
	private ViewPreviousInsurance viewPreviousInsurance;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@EJB
	private CreateRODService createRodService;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2409362490913982090L;
	
	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {
		"claimNumber",
		"intimationNumber",
		"policyNumber",
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

/*	public static final Object[] VISIBLE_COLUMNS_PA = new Object[] {
		
		"claimNumber",		
		"intimationNumber",
		"policyNumber",
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
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(
				PreviousClaimsTableDTO.class));
		table.setWidth("100%");
		table.setPageLength(5);
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"claimNumber",
			"intimationNumber",
			"policyNumber",
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
								viewSearchClaimStatus(claim);

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
								if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
									getDoctorRemarks(claim.getIntimation().getIntimationId());
								}else{
									viewDoctorRemarksFromPremia(claim.getIntimation());
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
								getViewClaimHistoryForOPHealthCheckUp(intimation);
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
	
	
	public void setVisibleColumnForHealth()
	{
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"claimNumber",
			"intimationNumber",
			"policyNumber",
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
	}
	public void setVisiblecolumnForPA()
	{
		 Object[] VISIBLE_COLUMNS_PA = new Object[] {
			
			"claimNumber",		
			"intimationNumber",
			"policyNumber",
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
	
	  public void getViewClaimHistoryForOPHealthCheckUp(String intimationNo) {
			
			if(intimationNo != null){
			Intimation intimation = intimationService
					.getIntimationByNo(intimationNo);
			if (intimation != null) {
				viewClaimHistoryRequest.showOPHealthCheckUpClaimHistory(intimation);
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
			
			
//			url = url + intimaton.getPolicy().getPolicyNumber();

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
	
	  public void getViewClaimHistoryForOPHealthCheckUp(Intimation intimation) {
			

			if (intimation != null) {
				viewClaimHistoryRequest.showOPHealthCheckUpClaimHistory(intimation);
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View History");
				popup.setWidth("75%");
				popup.setHeight("75%");
				popup.setContent(viewClaimHistoryRequest);
				popup.setClosable(true);
				popup.center();
				popup.setResizable(false);
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
			
}

public void getErrorMessage(String eMsg) {

/*	Label label = new Label(eMsg, ContentMode.HTML);
	label.setStyleName("errMessage");
	VerticalLayout layout = new VerticalLayout();
	layout.setMargin(true);
	layout.addComponent(label);

	ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("Error");
	dialog.setClosable(true);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);*/
	HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
	buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
	GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
}
	
	public void getViewDocumentFromPremia(ViewTmpClaim claim){
		
		ViewTmpIntimation intimation2 = claim.getIntimation();
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.CLAIMS_DMS_URL;
		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
		
		
		BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+intimation2.getKey()));
		browserFrame.setWidth("100%");
		browserFrame.setHeight("120%");
		
		String url = strDmsViewURL+intimation2.getKey();
		
//		getUI().getPage().open("", "_self");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
		
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
	
	
	public void getViewDocument(ViewTmpClaim claim) {

		ViewTmpIntimation intimation = claim.getIntimation();
		String strPolicyNo = intimation.getPolicyNumber();
		Long insuredKey = intimation.getInsured().getKey();
		Insured	insuredByKey = intimationService.getInsuredByKey(insuredKey);
		//String strPolicyNo = "P/181219/01/2014/001945";
		getViewDocumentByPolicyNo(strPolicyNo,insuredByKey);

	//	UI.getCurrent().addWindow(viewDocuments);
		
	}
	
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
	
   public void viewSearchClaimStatus(ViewTmpClaim claim) {
		
		ViewTmpIntimation intimation = claim.getIntimation();

		if(BPMClientContext.CLAIM_STATUS_PREMIA != null && BPMClientContext.CLAIM_STATUS_PREMIA.equalsIgnoreCase(SHAConstants.CLAIMS_VIEW_PLAN_B)){
			
			if(claim != null && claim.getRecordFlag() != null && claim.getRecordFlag().equalsIgnoreCase("G")){
				
				showClaimStatus(claim);                            // fetch claim status from galaxy
				
			}else{
				getViewClaimStatusFromPremia(intimation.getIntimationId());    // fetch claim status from premia end.
			}
			
		}else{
			showClaimStatus(claim);     //fetch claim status only from galaxy
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
	
	private void showClaimStatus(ViewTmpClaim claim) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ClaimStatusRegistrationDTO registrationDetails = instance.getRegistrationDetails(claim);
		//ViewTmpIntimation intimation = intimationService.getTmpIntimationByKey(claim.getIntimation().getKey());
		Intimation intimation = intimationService.getIntimationByKey(claim.getIntimation().getKey());
		ViewClaimStatusDTO intimationDetails = instance.getViewClaimStatusDto(intimation);
		Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
		getHospitalDetails(intimationDetails, hospitals);
		intimationDetails.setClaimStatusRegistrionDetails(registrationDetails);
		intimationDetails.setClaimKey(claim.getKey());
		Long claimKey = claim.getKey();
		
		setPaymentDetails(intimationDetails, claim);
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = docAcknowledgementService.listOfEarlierAckByClaimKey(claimKey,null);
		intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);
		
		 if(ReferenceTable.getGMCProductList().containsKey(intimation.getPolicy().getProduct().getKey())){
			 boolean isjiopolicy = false;	
				isjiopolicy = intimationService.getJioPolicyDetails(intimation.getPolicy().getPolicyNumber());
				  
				intimationDetails.setJioPolicy(isjiopolicy);
			      Insured insuredByKey = intimationService.getInsuredByKey(intimation.getInsured().getKey());
			      Insured MainMemberInsured = null;
			      
			      if(insuredByKey.getDependentRiskId() == null){
			    	  MainMemberInsured = insuredByKey;
			      }else{
			    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimation.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
			    	  MainMemberInsured = insuredByPolicyAndInsuredId;
			      }
			      
			      if(MainMemberInsured != null){
			    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
			      }		
			}
		
		rodIntimationDetailsObj.init(intimationDetails,null);
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
	
	public void viewUploadedDocumentDetails(String intimationNo) {
		
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
	//	getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);

		/*DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		dmsDTO.setClaimNo(claim.getClaimId());
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = createRodService
				.getDocumentDetailsData(intimationNo);
		if (null != dmsDocumentDetailsDTO && !dmsDocumentDetailsDTO.isEmpty()) {
			dmsDTO.setDmsDocumentDetailsDTOList(dmsDocumentDetailsDTO);
		}

		popup = new com.vaadin.ui.Window();

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
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		intimationDetails.setSuspiciousReason(hospitals.getClmPrcsInstruction());
		EarlierRodMapper.invalidate(instance);
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

}
