package com.shaic.claim.OMPprocessrejection.detailPage;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPViewDetails.view.OMPViewClaimStatusMapper;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.search.OMPReceiptofDocumentsAndBillEntryService;
import com.shaic.claim.claimhistory.view.ompView.OMPEarlierRodMapper;
import com.shaic.claim.claimhistory.view.ompView.ViewOMPClaimHistoryRequest;
import com.shaic.claim.intimation.create.ViewPolicyDetails;
import com.shaic.claim.ompviewroddetails.OMPIntimationAndViewDetailsUI;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTable;
import com.shaic.claim.ompviewroddetails.OMPProcessingDetailsTable;
import com.shaic.claim.ompviewroddetails.OMPRodAndBillEntryDetailTable;
import com.shaic.claim.ompviewroddetails.OMPViewClaimStatusDto;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewDiagnosisTable;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.OMPIntimation;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.domain.omp.OMPIntimationService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinService;
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

public class ViewOMPPreviousClaimsTable extends GBaseTable<PreviousClaimsTableDTO> {
	
	@EJB
	private OMPClaimService claimService;
	
	@EJB
	private OMPReceiptofDocumentsAndBillEntryService reimbursementService;
	
	@Inject
	private OMPIntimationAndViewDetailsUI rodIntimationDetailsInstance;
	
//	private OMPIntimationAndViewDetailsUI rodIntimationDetailsObj;	
	
	@EJB
	private HospitalService hospitalService;
	
//	@EJB
//	private AcknowledgementDocumentsReceivedService docAcknowledgementService;
	
	@EJB
	private PolicyService policyService;
	
//	@Inject
//	private ViewSpecialistOpinionDetails viewSpecialistOpinionDetails;
	
	@Inject
	private ViewOMPClaimHistoryRequest viewClaimHistoryRequest;
	
//    @Inject
//    private FieldVisitDetailsTable fieldVisitDetailsTableInstance;
    
//    @Inject
//    private ViewSectionDetailsTable sectionDetailsTableObj;
//    
//    @Inject
//	private ViewPreviousPreauthSummaryTable preauthPreviousDetailsPage;
    
	@Inject
	private OMPRodAndBillEntryDetailTable receiptOfDocumentTable;
	
	@Inject
	private OMPProcessingDetailsTable billingProcessingTable;
	
	@Inject 
	private ViewDiagnosisTable diagnosisDetailsTable;
	
	@Inject
	private OMPPaymentDetailsTable paymentTableObj;
	
//	@Inject
//	private ViewPaymentTable paymentTableObj;
	
    @Inject
	private ViewPolicyDetails viewPolicyDetail;
    
	@Inject
	private OMPIntimationService intimationService;

	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private IntimationService intimService;
	
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
		"ailmentLoss",
		"admissionDate",
		"claimStatus",
		"claimAmount",
		"approvedAmount",
		"hospitalName"		
	};*/
	
	
//	public static final Object[] VISIBLE_COLUMNS_PA = new Object[] {
//		"policyNumber",
//		"policyYear",
//		"claimNumber",
//		"claimType",
//		"benefits",
//		"intimationNumber",
//		"insuredPatientName",
//		"diagnosis",
//		"admissionDate",
//		"claimStatus",
//		"claimAmount",
//		"approvedAmount",
//		"copayPercentage",
//		"hospitalName",
//		"pedName",
//		"icdCodes",
//	};

	@Override
	public void removeRow() {
		//  Auto-generated method stub
		
	}
	
//	public void setPAColumnsForPreClaims(){
//		table.setVisibleColumns(VISIBLE_COLUMNS_PA);
//		table.setColumnHeader("benefits", "Benefit/Cover");
//		
//		table.removeGeneratedColumn("ViewClaimStatus");
//		table.addGeneratedColumn("ViewClaimStatus",
//				new Table.ColumnGenerator() {
//					/**
//			 * 
//			 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public Object generateCell(final Table source,
//							final Object itemId, Object columnId) {
//						Button button = new Button("View Claim Status");
//						PreviousClaimsTableDTO dto = (PreviousClaimsTableDTO) itemId;
//						if(dto.getClaimTypeKey() != null && dto.getClaimTypeKey().equals(ReferenceTable.OUT_PATIENT)
//								||dto.getClaimTypeKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
//							button.setEnabled(false);
//						}
//						button.addClickListener(new Button.ClickListener() {
//							/**
//					 * 
//					 */
//							private static final long serialVersionUID = 1L;
//
//							public void buttonClick(ClickEvent event) {
//								
//								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
//								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
//								OMPClaim claim = claimService.getClaimByKey((Long) (previousClaimsTableDTO.getKey()));
//								
//								
//								Map<String, Object> objectMap = new HashMap<String, Object>();
////								objectMap.put(SHAConstants.VIEW_CLAIM_HISTORY_REQUEST, viewClaimHistoryRequest);
//								objectMap.put(SHAConstants.RECEIPT_OF_DOCUMENTTABLE, receiptOfDocumentTable);
//								objectMap.put(SHAConstants.BILLING_PROCESSING_TABLE, billingProcessingTable);
//								objectMap.put(SHAConstants.VIEW_PAYMENT_TABLE, paymentTableObj);
//								
//								VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OBJECT_MAPPER,objectMap);	
//								
//								viewSearchClaimStatus(claim,objectMap);
//							
//							}
//
//
//						});
//
//						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//						button.addStyleName(ValoTheme.BUTTON_LINK);
//						return button;
//					}
//				});
//		
//		table.removeGeneratedColumn("viewdocument");
//		table.addGeneratedColumn("viewdocument", new Table.ColumnGenerator() {
//			
//			@Override
//			public Object generateCell(Table source, final Object itemId, Object columnId) {
//				Button button = new Button("View Document");
//				button.addClickListener(new ClickListener() {
//					
//					@Override
//					public void buttonClick(ClickEvent event) {
//						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
//						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
//						OMPClaim claim = claimService.getClaimByKey(claimKey);
//						viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());// fetch claim status from galaxy
//					}
//				});
//				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//				button.addStyleName(ValoTheme.BUTTON_LINK);
//				return button;
//			}
//		});
//		
//		table.removeGeneratedColumn("viewTrails");
//		table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
//			
//			@Override
//			public Object generateCell(Table source, final Object itemId, Object columnId) {
//				Button button = new Button("View Trails");
//				button.addClickListener(new ClickListener() {
//					
//					@Override
//					public void buttonClick(ClickEvent event) {
//						PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
//						Long claimKey = (Long) (previousClaimsTableDTO.getKey());
//						OMPClaim claim = claimService.getClaimByKey(claimKey);
//						if(claim != null) {
//								getViewClaimHistory(claim.getIntimation());
//							
//						}						
//					}
//				});
//				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//				button.addStyleName(ValoTheme.BUTTON_LINK);
//				return button;
//			}
//		});
//	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(
				PreviousClaimsTableDTO.class));
		table.setWidth("100%");
		table.setHeight("400px");
//		table.setPageLength(6);
		 Object[] VISIBLE_COLUMNS = new Object[] {
			"policyNumber",
			"policyYear",
			"claimNumber",
			"claimType",
			"intimationNumber",
			"insuredPatientName",
			"ailmentLoss",
			"admissionDate",
			"claimStatus",
			"claimAmount",
			"approvedAmount",
			"hospitalName"		
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
						
						button.addClickListener(new Button.ClickListener() {
							/**
					 * 
					 */
							private static final long serialVersionUID = 1L;

							public void buttonClick(ClickEvent event) {
								
								PreviousClaimsTableDTO previousClaimsTableDTO = (PreviousClaimsTableDTO) itemId;
//								rodIntimationDetailsObj = rodIntimationDetailsInstance.get();
								OMPClaim claim = claimService.getClaimByKey((Long) (previousClaimsTableDTO.getKey()));								
								
								Map<String, Object> objectMap = new HashMap<String, Object>();
//								objectMap.put(SHAConstants.VIEW_CLAIM_HISTORY_REQUEST, viewClaimHistoryRequest);
								objectMap.put(SHAConstants.RECEIPT_OF_DOCUMENTTABLE, receiptOfDocumentTable);
								objectMap.put(SHAConstants.BILLING_PROCESSING_TABLE, billingProcessingTable);
								objectMap.put(SHAConstants.VIEW_PAYMENT_TABLE, paymentTableObj);
								
								VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.OBJECT_MAPPER,objectMap);	
								viewSearchClaimStatus(claim,objectMap);
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
						OMPClaim claim = claimService.getClaimByKey(claimKey);
						viewUploadedDocumentDetails(claim.getIntimation().getIntimationId());// fetch claim status from galaxy
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
						OMPClaim claim = claimService.getClaimByKey(claimKey);
						if(claim != null) {
								getViewClaimHistory(claim.getIntimation());
						}						
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});

	}
	
	
	public void viewSearchClaimStatus(OMPClaim claim,Map<String, Object> mapper) {
		
//		OMPIntimation intimation = claim.getIntimation();

		showClaimStatus(claim,mapper);     //fetch claim status only from galaxy
			
	}
	
	private void showClaimStatus(OMPClaim claim,Map<String, Object>mapper) {
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");

		ClaimStatusRegistrationDTO registrationDetails = OMPEarlierRodMapper.getInstance().getRegistrationDetails(claim);
		OMPIntimation intimation = intimationService.getIntimationByKey(claim.getIntimation().getKey());
		ViewClaimStatusDTO intimationDetails = OMPEarlierRodMapper.getInstance().getViewClaimStatusDto(intimation);
		OMPHospitals hospitals = hospitalService.getOMPHospitalById(intimation.getHospital());
		
		if(hospitals != null){		
			getHospitalDetails(intimationDetails, hospitals);
		}
		intimationDetails.setClaimStatusRegistrionDetails(registrationDetails);
		intimationDetails.setClaimKey(claim.getKey());
		Long claimKey = claim.getKey();
		
		setPaymentDetails(intimationDetails, claim);
		
		List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService.listOfEarlierAckByClaimKey(claimKey,null);
		intimationDetails.setReceiptOfDocumentValues(listDocumentDetails);
		OMPViewClaimStatusDto claimStatusDetails = OMPViewClaimStatusMapper.getInstance().getOMPViewClaimStatusDto(intimationDetails);
		rodIntimationDetailsInstance.init(claimStatusDetails,mapper);
		popup.setContent(rodIntimationDetailsInstance);
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
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			OMPClaim claim) {
		ViewTmpClaimPayment reimbursementForPayment = null;
//		try {			
//			
//			ViewTmpReimbursement settledReimbursement = reimbursementService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
//			if(settledReimbursement != null){
//				
//			     reimbursementForPayment = reimbursementService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
//				
//			}else{
//				reimbursementForPayment = reimbursementService
//						.getRimbursementForPayment(claim.getClaimId());
//			}
//			
//			if(reimbursementForPayment != null){
//			bean.setBankName(reimbursementForPayment.getBankName());
//			bean.setTypeOfPayment(reimbursementForPayment.getPaymentType());
//			bean.setAccountName(reimbursementForPayment.getAccountNumber());
//			bean.setBranchName(reimbursementForPayment.getBranchName());
//			bean.setChequeNumber(reimbursementForPayment.getChequeDDNumber());
//			if(reimbursementForPayment.getPaymentType() != null && reimbursementForPayment.getPaymentType().equalsIgnoreCase(SHAConstants.NEFT_TYPE)){
//				
//				if(reimbursementForPayment.getChequeDDDate() != null){
//					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
//					bean.setNeftDate(formatDate);
//				}
//			 bean.setChequeNumber(null);
//			}else{
//				if(reimbursementForPayment.getChequeDDDate() != null){
//					String formatDate = SHAUtils.formatDate(reimbursementForPayment.getChequeDDDate());
//					bean.setChequeDate(formatDate);
//				}
//			}
//			
//			}
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return reimbursementForPayment;

	}
	

	public void getViewClaimHistory(OMPIntimation intimation) {
		
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
			}else{
					getErrorMessage("History is not available");
			}
		 }
		
		}else{
			getErrorMessage("History is not available");
		}
	}
	
  public void getViewClaimHistoryForOPHealthCheckUp(String intimationNo) {}
	
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
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void getHospitalDetails(
			ViewClaimStatusDTO intimationDetails,
			OMPHospitals hospitals) {
		ViewClaimStatusDTO hospitalDetails = OMPEarlierRodMapper.getInstance().gethospitalDetails(hospitals);
		if(hospitalDetails != null){
			intimationDetails.setState(hospitalDetails.getState());
			intimationDetails.setCity(hospitalDetails.getCity());
			intimationDetails.setArea(hospitalDetails.getArea());
			intimationDetails.setHospitalAddress(hospitals.getAddress());
			intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
			intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
			intimationDetails.setHospitalIrdaCode(hospitalDetails.getHospitalIrdaCode());
			intimationDetails.setHospitalInternalCode(hospitalDetails.getHospitalInternalCode());
		}
	}
	
	public void getViewDocument(ViewTmpClaim claim) {

		ViewTmpIntimation intimation = claim.getIntimation();
		String strPolicyNo = intimation.getPolicyNumber();
		getViewDocumentByPolicyNo(strPolicyNo);
		
	}
	
//	public void getViewDocumentFromPremia(ViewTmpClaim claim){
//		
//		ViewTmpIntimation intimation2 = claim.getIntimation();
//		VerticalLayout vLayout = new VerticalLayout();
//		String strDmsViewURL = BPMClientContext.CLAIMS_DMS_URL;
//		/*BrowserFrame browserFrame = new BrowserFrame("View Documents",
//			    new ExternalResource(strDmsViewURL+strPolicyNo));*/
//		
//		ViewTmpIntimation tmpIntimation = claimService.getTmpIntimationObject(intimation2.getKey());
//		Long key = null;
//		if(null != tmpIntimation)
//		{
//			key = tmpIntimation.getKey();
//		}
//		
//		
//		//BrowserWindowOpener browserFrame = new BrowserWindowOpener(new ExternalResource(strDmsViewURL+key));
//		//browserFrame.
//		
//		//new StreamReso
//		String url = strDmsViewURL+key;
////		ExternalResource ex =  new ExternalResource(strDmsViewURL+key);
////		//ExternalResource ex =  new ExternalResource("http://192.168.1.46:8080/dms/ManageDigitalFileAction.do?method=getHeirarchy&hidIntimationId=896097","text/html");
////	//	ex.setMIMEType("text/html");
////
////		BrowserFrame browserFrame = new BrowserFrame("",
////				ex);
////		browserFrame.setWidth("100%");
////		browserFrame.setHeight("120%");
////		browserFrame.setData(key);
//		
////		getUI().getPage().open(url, "_blank");
////		getUI().getPage().open("", "_self");
//		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
//		
//		/*Embedded browserFrame = new Embedded("Vaadin web site", new ExternalResource(
//				strDmsViewURL+intimation2.getKey()));
//		browserFrame.setType(Embedded.TYPE_BROWSER);
//		browserFrame.setWidth("100%");
//		browserFrame.setHeight("400px");*/
//       // addComponent(e);
//		
//		
//		/*vLayout.addComponent(browserFrame);
//		
//		Button btnSubmit = new Button("OK");
//		
//		btnSubmit.setCaption("CLOSE");
//		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
//		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		btnSubmit.setWidth("-1px");
//		btnSubmit.setHeight("-10px");
//		btnSubmit.setDisableOnClick(true);
//		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
//		
//		vLayout.addComponent(btnSubmit);
//		vLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_CENTER);
//		vLayout.setWidth("100%");
//		vLayout.setHeight("130%");
//		popup = new com.vaadin.ui.Window();
//		
//		popup.setCaption("");
//		popup.setWidth("100%");
//		popup.setHeight("100%");
//		//popup.setSizeFull();
//		popup.setContent(vLayout);
//		//popup.setContent(browserF);
//		popup.setClosable(true);
//		popup.center();
//		popup.setResizable(false);
//		
//		btnSubmit.addClickListener(new Button.ClickListener() {
//			
//			private static final long serialVersionUID = 1L;
//	
//			@Override
//			public void buttonClick(ClickEvent event) {
//					//binder.commit();
//						
//						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
//						popup.close();
//					
//			}
//			
//		});
//
//		
//		popup.addCloseListener(new Window.CloseListener() {
//			*//**
//			 * 
//			 *//*
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//
//		popup.setModal(true);
//		UI.getCurrent().addWindow(popup);*/
//		
//	}
	
	/*private ViewTmpIntimation getTmpIntimationByKey(Long intimationKey)
	{
		Query query = enti
	}*/
	
	public void getViewDocumentByPolicyNo(String strPolicyNo) {
		VerticalLayout vLayout = new VerticalLayout();
		String strDmsViewURL = BPMClientContext.DMS_VIEW_URL;
		String dmsToken = intimationService.createDMSToken(strPolicyNo);
		getUI().getPage().open(strDmsViewURL+dmsToken, "_blank",1200,650,BorderStyle.NONE);
		/*BrowserFrame browserFrame = new BrowserFrame("",
			    new ExternalResource(strDmsViewURL+dmsToken));
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
	
	public void viewUploadedDocumentDetails(String intimationNo) {

		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = intimService.createJWTTokenForClaimStatusPages(tokenInputs);
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
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);

	}

	@Override
	public void tableSelectHandler(PreviousClaimsTableDTO t) {
	// nothing to be opened on selecting the table row		
	}

	@Override
	public String textBundlePrefixString() {
		return "previousclaimtable-preauth-";
	}
	public void setListOfPreviousClaims(BeanItemContainer<PreviousClaimsTableDTO> dto) {
		this.table.setContainerDataSource(dto);
	}	
	public void setCaption(String strCaption)
	{
		this.table.setCaption(strCaption);
	}
}
