package com.shaic.claim.intimation.viewdetails.search;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.claimhistory.view.ViewClaimHistoryRequest;
import com.shaic.claim.hospitalCommunication.HospitalFormDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class SearchViewDetailIntimationTable extends GBaseTable<NewIntimationDto>{
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	@Inject
	private RodIntimationAndViewDetailsUI rodIntimationAndViewDetails;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@EJB
	private PreauthService preauthService;
	
	@Inject
	private ViewClaimHistoryRequest viewClaimHistoryRequest;
	
	private Long rodKey;

	
	private final static Object COLUM_HEADER_DRAFT_INTIMATION[] = new Object[] {
		"Action","policy.policyNumber", "insuredPatient.healthCardNumber",
			"insuredPatientName", "intimaterName", "hospitalDto.name",
			"modeOfIntimation.value", "intimatedBy.value","dateOfIntimation",
			"status.processValue", "callerContactNum" };
	
	private final static Object COLUM_HEADER_SUBMIT_INTIMATION[] = new Object[] {
		"Action","viewDocument","viewTrails","intimationId", "policy.policyNumber","claimNumber",
			"insuredPatient.healthCardNumber", "insuredPatientName",
			"intimaterName", "hospitalDto.name",
			"modeOfIntimation.value", "intimatedBy.value","dateOfIntimation",
			"status.processValue", "callerContactNum" };
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private ClaimService claimService;
	
	@Inject
	private ViewDetails viewDetails;
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<NewIntimationDto> newIntimationDtoContainer = new BeanItemContainer<NewIntimationDto>(NewIntimationDto.class);
		
newIntimationDtoContainer
		.addNestedContainerProperty("policy.policyNumber");
newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatient.healthCardNumber");
newIntimationDtoContainer
		.addNestedContainerProperty("insuredPatientName");
newIntimationDtoContainer
		.addNestedContainerProperty("intimaterName");
newIntimationDtoContainer
		.addNestedContainerProperty("hospitalDto.name");
newIntimationDtoContainer
		.addNestedContainerProperty("modeOfIntimation.value");
newIntimationDtoContainer
		.addNestedContainerProperty("intimatedBy.value");
newIntimationDtoContainer
		.addNestedContainerProperty("callerContactNum");
newIntimationDtoContainer
		.addNestedContainerProperty("status.processValue");
newIntimationDtoContainer.addNestedContainerProperty("dateOfIntimation");
	
	table.removeGeneratedColumn("dateOfIntimation");
	table.addGeneratedColumn("dateOfIntimation",
		new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source,
					final Object itemId, Object columnId) {
				if(((NewIntimationDto)itemId).getCreatedDate() != null){
			        return  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(((NewIntimationDto)itemId).getCreatedDate()); 
			}else{
			                return "";
			}						 
			}
			});
	
	table.removeGeneratedColumn("Action");
	table.addGeneratedColumn("Action",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button();
					viewIntimationDetailsButton.setData(itemId);
					Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					if (a_claim != null
							&& a_claim.getClaimId() != null)

					{
						viewIntimationDetailsButton
								.setCaption("View Claim Status");
					} else {
						viewIntimationDetailsButton
								.setCaption("View Intimation Details");

					}
					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {

									NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;
									Intimation a_intimation = intimationService
											.getIntimationByKey(newIntimationDto
													.getKey());
									
									Claim a_claim = claimService
											.getClaimforIntimation(newIntimationDto
													.getKey());
									
									if (a_claim != null) {
										rodKey = null;
										viewClaimStatusAndViewDetails(a_intimation.getIntimationId());
									}
									else if (a_claim == null) {
										viewDetails.getViewIntimation(a_intimation.getIntimationId());
									}
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});
	
	table.removeGeneratedColumn("viewDocument");
	table.addGeneratedColumn("viewDocument",
			new Table.ColumnGenerator() {
				@SuppressWarnings("serial")
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View Document Details");
					viewIntimationDetailsButton.setData(itemId);
					final Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {
									Intimation intimation = intimationService.getIntimationByKey(intimationKey);
									viewUploadedDocumentDetails(intimation.getIntimationId());
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});
	
	table.removeGeneratedColumn("viewTrails");
	table.addGeneratedColumn("viewTrails",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View Trails");
					viewIntimationDetailsButton.setData(itemId);
					final Long intimationKey = ((NewIntimationDto) itemId)
							.getKey();

					Claim a_claim = claimService
							.getClaimforIntimation(intimationKey);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(
										ClickEvent event) {
									Intimation intimation = intimationService.getIntimationByKey(intimationKey);
									getViewClaimandViewDetailsHistory(intimation.getIntimationId());
								}
							});
					viewIntimationDetailsButton
							.addStyleName(BaseTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
			});
	
	    table.setColumnHeader("Action", "Action");
	    table.setColumnHeader("viewDocument", "View Document");
		
		table.setContainerDataSource(newIntimationDtoContainer);
//		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.setSizeFull();
		
	}
	
	@SuppressWarnings("deprecation")
	public void setDraftTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_DRAFT_INTIMATION);
		table.removeGeneratedColumn("Action");
		table.removeGeneratedColumn("viewDocument");
		table.removeGeneratedColumn("viewTrails");
	}

	@SuppressWarnings("deprecation")
	public void setSubmitTableHeader(){
		table.setVisibleColumns(COLUM_HEADER_SUBMIT_INTIMATION);
		table.setColumnHeader("Action", "Action");
		 table.setColumnHeader("viewDocument", "View Document");
		 table.setColumnHeader("viewTrails","View Trails");
	}
	
	@Override
	public void tableSelectHandler(NewIntimationDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchIntimation-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}

	@SuppressWarnings("deprecation")
	public void addClaimNumberColum(){
		table.removeGeneratedColumn("claimNumber");
		table.addGeneratedColumn("claimNumber",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						String claimNum = "";

						Claim a_claim = claimService
								.getClaimforIntimation(((NewIntimationDto) itemId)
										.getKey());

						if (a_claim != null && a_claim.getClaimId() != null) {
							claimNum = a_claim.getClaimId();
						}
						Label claimNumber = new Label(claimNum);
						// source.getContainerDataSource().addItem(claimNumber);
						return claimNumber;
					}
				});
	}
	
	@SuppressWarnings("deprecation")
	public void addEditColumn(){
		table.removeGeneratedColumn("Edit Intimation");
		table.addGeneratedColumn("Edit Intimation",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source,
							final Object itemId, Object columnId) {
						final Button viewIntimationDetailsButton = new Button(
								"View / Edit Intimation");

						viewIntimationDetailsButton.setData(itemId);
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										NewIntimationDto newIntimationDto = (NewIntimationDto) event
												.getButton()
												.getData();
										// fireViewEvent(MenuItemBean.EDIT_INTIMATION,
										// newIntimationDto);
										fireViewEvent(
												MenuItemBean.REVISED_EDIT_INTIMATION,
												newIntimationDto);

									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});

	}
	
	@SuppressWarnings("deprecation")
	public void addViewIntimationDetailsColumn(){
		table.removeGeneratedColumn("Action");
		table.addGeneratedColumn("Action",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewIntimationDetailsButton = new Button();
						viewIntimationDetailsButton.setData(itemId);
						Long intimationKey = ((NewIntimationDto) itemId)
								.getKey();

						Claim a_claim = claimService
								.getClaimforIntimation(intimationKey);

						if (a_claim != null
								&& a_claim.getClaimId() != null)

						{
							viewIntimationDetailsButton
									.setCaption("View Claim Status");
						} else {
							viewIntimationDetailsButton
									.setCaption("View Intimation Details");

						}
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {

										NewIntimationDto newIntimationDto = (NewIntimationDto) itemId;
										Intimation a_intimation = intimationService
												.getIntimationByKey(newIntimationDto
														.getKey());
										
										Claim a_claim = claimService
												.getClaimforIntimation(newIntimationDto
														.getKey());
										
										if (a_claim != null) {
												viewDetails.viewClaimStatusUpdated(a_intimation.getIntimationId());
										}
										else if (a_claim == null) {
											viewDetails.getViewIntimation(a_intimation.getIntimationId());
										}
									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
	}
	
	public void viewUploadedDocumentDetails(String intimationNo) {
		
		BPMClientContext bpmClientContext = new BPMClientContext();
		Map<String,String> tokenInputs = new HashMap<String, String>();
		 tokenInputs.put("intimationNo", intimationNo);
		 String intimationNoToken = null;
		  try {
			  intimationNoToken = preauthService.createJWTTokenForClaimStatusPages(tokenInputs);
		  } catch (NoSuchAlgorithmException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (ParseException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
		tokenInputs = null;
		String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
	//	getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);

	/*	DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
		dmsDTO.setIntimationNo(intimationNo);
		Claim claim = claimService.getClaimsByIntimationNumber(intimationNo);
		if(claim != null){
		dmsDTO.setClaimNo(claim.getClaimId());
		}
		List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTO = billDetailsService
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
	
	
	public void getViewClaimandViewDetailsHistory(String intimationNo) {
		
		if(intimationNo != null){
		Intimation intimation = intimationService
				.getIntimationByNo(intimationNo);
		
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
					popup = null;
			}else{
					getErrorMessage("Claim is not available");
			}
		 }
		
		}else{
			getErrorMessage("History is not available");
		}
	}
	
	public void viewClaimStatusAndViewDetails(String intimationNo) {

		ViewTmpClaim claim = null;

		try {
			final ViewTmpIntimation viewIntimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);
			// Hospitals hospital =
			// hospitalService.searchbyHospitalKey(intimation
			// .getKey());
			claim = claimService.getTmpClaimforIntimation(viewIntimation.getKey());
			Intimation intimationObj = intimationService
					.getIntimationByKey(viewIntimation
							.getKey());
			
			if (claim != null) {
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ClaimStatusRegistrationDTO registrationDetails = instance.getRegistrationDetails(claim);
				 Intimation intimation =
				 intimationService.getIntimationByKey(claim.getIntimation().getKey());
				ViewClaimStatusDTO intimationDetails = EarlierRodMapper
						.getViewClaimStatusDto(intimation);
				Hospitals hospitals = hospitalService
						.getHospitalById(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				EarlierRodMapper.invalidate(instance);
				intimationDetails.setClaimKey(claim.getKey());
				String name = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey()).getName()
						: "";
				intimationDetails.setPatientNotCoveredName(name);
				String relationship = null != intimationService
						.getNewBabyByIntimationKey(intimationDetails
								.getIntimationKey()) ? intimationService
						.getNewBabyByIntimationKey(
								intimationDetails.getIntimationKey())
						.getBabyRelationship().getValue() : "";
				intimationDetails.setRelationshipWithInsuredId(relationship);
				List<ViewDocumentDetailsDTO> listDocumentDetails = reimbursementService
						.listOfEarlierAckByClaimKey(claim.getKey(), this.rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, claim);
				
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null != intimation.getPolicy().getProduct().getKey() &&
							(ReferenceTable.getGPAProducts().containsKey(intimation.getPolicy().getProduct().getKey()))){
					
					//intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					if(intimationObj.getPaPatientName() !=null && !intimationObj.getPaPatientName().isEmpty())
					{
						intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					}else
					{
						intimationDetails.setInsuredPatientName((intimationObj.getInsured() !=null && intimationObj.getInsured().getInsuredName() !=null) ? intimationObj.getInsured().getInsuredName() : "");
					}
				}
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null != intimation.getPolicy().getProduct().getKey() &&
						(ReferenceTable.getGMCProductList().containsKey(intimationObj.getPolicy().getProduct().getKey()))){
					 boolean isjiopolicy = false;	
						isjiopolicy = intimationService.getJioPolicyDetails(intimationObj.getPolicy().getPolicyNumber());
						  
						intimationDetails.setJioPolicy(isjiopolicy);
					      Insured insuredByKey = intimationService.getInsuredByKey(intimationObj.getInsured().getKey());
					      Insured MainMemberInsured = null;
					      
					      if(insuredByKey.getDependentRiskId() == null){
					    	  MainMemberInsured = insuredByKey;
					      }else{
					    	  Insured insuredByPolicyAndInsuredId = intimationService.getInsuredByPolicyAndInsuredId(intimationObj.getPolicy().getPolicyNumber(), insuredByKey.getDependentRiskId(), SHAConstants.HEALTH_LOB_FLAG);
					    	  MainMemberInsured = insuredByPolicyAndInsuredId;
					      }
					      
					      if(MainMemberInsured != null){
					    	  intimationDetails.setEmployeeCode(MainMemberInsured.getInsuredEmployeeId());					    	 
					      }							
				}

				DocAcknowledgement docAcknowledgement = reimbursementService
						.findAcknowledgment(this.rodKey);
				
				NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
				
				PreauthDTO preauthDTO = new PreauthDTO();
				preauthDTO.setNewIntimationDTO(intimationDto);
				Claim claimObj = intimationService.getClaimforIntimation(intimationObj.getKey());
				if(claimObj!=null){
					preauthDTO.setCrmFlagged(claimObj.getCrcFlag());
					preauthDTO.setVipCustomer(claimObj.getIsVipCustomer());
					preauthDTO.setClaimPriorityLabel(claimObj.getClaimPriorityLabel());
					preauthDTO.setZohoGrievanceFlag(claimObj.getZohoGrivanceFlag());
				}
				intimationDetails.setPreauthDTO(preauthDTO);
				
				if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					rodIntimationAndViewDetails
							.init(intimationDetails, this.rodKey);
				} else {
					rodIntimationAndViewDetails.init(intimationDetails, null);
				}
				Window popup = new com.vaadin.ui.Window();
				popup.setCaption("View Claim Status");
				popup.setWidth("75%");
				popup.setHeight("85%");
				popup.setContent(rodIntimationAndViewDetails);
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
						if(rodIntimationAndViewDetails != null){
							rodIntimationAndViewDetails.clearObjects();
						}
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				popup = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	
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
	
	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails,
			Hospitals hospitals) {
		EarlierRodMapper instance = EarlierRodMapper.getInstance();
		ViewClaimStatusDTO hospitalDetails = instance.gethospitalDetails(hospitals);
		intimationDetails.setState(hospitalDetails.getState());
		intimationDetails.setCity(hospitalDetails.getCity());
		intimationDetails.setArea(hospitalDetails.getArea());
		intimationDetails.setHospitalAddress(hospitals.getAddress());
		intimationDetails.setHospitalName(hospitalDetails.getHospitalName());
		if(hospitals.getFspFlag() !=null && hospitals.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
			intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue()+"(VSP)");
		}
		else{
			intimationDetails.setHospitalTypeValue(hospitalDetails.getHospitalTypeValue());
		}
		/*intimationDetails.setHospitalTypeValue(hospitalDetails
				.getHospitalTypeValue());*/
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		intimationDetails.setHospitalFlag(hospitals.getSuspiciousType());
		intimationDetails.setSuspiciousReason(hospitals.getClmPrcsInstruction());
		EarlierRodMapper.invalidate(instance);
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
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

}



