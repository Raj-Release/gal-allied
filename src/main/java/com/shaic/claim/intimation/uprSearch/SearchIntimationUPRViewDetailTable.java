package com.shaic.claim.intimation.uprSearch;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.ClaimStatusRegistrationDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodMapper;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewClaimStatusDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTableDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class SearchIntimationUPRViewDetailTable extends GBaseTable<PaymentProcessCpuPageDTO>{
	
	private final static Object COLUM_HEADER_SEARCH_INTIMATION_UPR[] = new Object[] {
		"Action","intimationNo","policyNo","rodNumber","paymentCpu","paymentType","approvedAmount",
		"payeeName"};
	
	@Inject
	private IntimationUPRViewDetailTable intimationUPRDetailTable;

	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private RodIntimationAndViewDetailsUI rodIntimationAndViewDetails;
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<PaymentProcessCpuPageDTO> newIntimationDtoContainer = new BeanItemContainer<PaymentProcessCpuPageDTO>(PaymentProcessCpuPageDTO.class);
		
		table.removeGeneratedColumn("Action");
		table.addGeneratedColumn("Action",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						
						final Button viewIntimationDetailsButton = new Button();
						PaymentProcessCpuPageDTO bean = (PaymentProcessCpuPageDTO) itemId;
						viewIntimationDetailsButton.setData(itemId);
						Long intimationKey = ((PaymentProcessCpuPageDTO) itemId)
								.getClaimDto().getNewIntimationDto().getKey();
						
						/*viewDetails.initView(bean.getIntimationNo(), (bean.getReimbursementObj() != null && bean.getReimbursementObj().getKey().intValue() != 0 ? bean.getReimbursementObj().getKey() : 0l), ViewLevels.CLAIM_STATUS,"");
						viewDetails.setDefaultDropView(viewDetails.VIEW_CLAIM_STATUS);*/						
						/*intimationUPRDetailTable.init("", false, false);
						intimationUPRDetailTable.removeRow();
						List<PaymentProcessCpuPageDTO> detailList = new ArrayList<PaymentProcessCpuPageDTO>();
						detailList.add(bean);
						intimationUPRDetailTable.setTableList(detailList);
						intimationUPRDetailTable.tablesize();*/
						viewIntimationDetailsButton
						.setCaption("View Details");
						
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {
										
										viewDetails.initView(bean.getIntimationNo(), (bean.getReimbursementObj() != null && bean.getReimbursementObj().getKey().intValue() != 0 ? bean.getReimbursementObj().getKey() : 0l), ViewLevels.CLAIM_STATUS,"");
										viewDetails.setDefaultDropView(viewDetails.VIEW_CLAIM_STATUS);
										
										intimationUPRDetailTable.init("", false, false);
										intimationUPRDetailTable.removeRow();
										List<PaymentProcessCpuPageDTO> detailList = new ArrayList<PaymentProcessCpuPageDTO>();
										detailList.add(bean);
										intimationUPRDetailTable.setTableList(detailList);
										intimationUPRDetailTable.tablesize();
										 
										FormLayout viewDetailsForm = new FormLayout();
										viewDetailsForm.setWidth("-1px");
										viewDetailsForm.setHeight("-1px");
										viewDetailsForm.setMargin(false);
										viewDetailsForm.setSpacing(true);
										viewDetailsForm.addComponent(viewDetails);
										
										HorizontalLayout clmStatusViewLayout = new HorizontalLayout(viewDetailsForm/*, getClaimStatusLinkBtn(bean)*/); 
										clmStatusViewLayout.setSpacing(true);
										VerticalLayout mainVerticalLayout = new VerticalLayout(clmStatusViewLayout,intimationUPRDetailTable);
										mainVerticalLayout.setComponentAlignment(clmStatusViewLayout, Alignment.MIDDLE_CENTER);
										Window popup = new com.vaadin.ui.Window();
										popup.setCaption("View Details");
										popup.setWidth("75%");
										popup.setHeight("75%");
										popup.setContent(mainVerticalLayout);
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
									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
	
	    table.setColumnHeader("Action", "Action");
	 	
		table.setContainerDataSource(newIntimationDtoContainer);
		table.setVisibleColumns(COLUM_HEADER_SEARCH_INTIMATION_UPR);
		table.setSizeFull();
		
	}
	
	@Override
	public void tableSelectHandler(PaymentProcessCpuPageDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchIntimationUpr-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
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
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(UI.getCurrent(), null, true);
	}

	/*private Button getClaimStatusLinkBtn(PaymentProcessCpuPageDTO paymentDto) {


		final Button viewIntimationDetailsButton = new Button();
		viewIntimationDetailsButton.setData(paymentDto);
		Long intimationKey = paymentDto.getClaimDto().getNewIntimationDto()
				.getKey();

		if (paymentDto.getClaimDto() != null
				&& paymentDto.getClaimDto().getClaimId() != null)

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
						
						if (paymentDto.getClaimDto() != null
								&& paymentDto.getClaimDto().getClaimId() != null) {
//							viewClaimStatusAndViewDetails(paymentDto);
//							viewClaimStatusAndViewDetails(a_intimation.getIntimationId(),paymentDto.getReimbursementObj().getKey());
							fireViewEvent(SearchIntimationUPRDetailPresenter.VIEW_ACTION_CLICK , paymentDto);
							
						}
						else if (paymentDto.getClaimDto() == null
								|| paymentDto.getClaimDto().getClaimId() == null) {
							viewDetails.getViewIntimation(paymentDto.getClaimDto().getNewIntimationDto().getIntimationId());
						}
					}
				});
		
		viewIntimationDetailsButton
				.addStyleName(BaseTheme.BUTTON_LINK);
		return viewIntimationDetailsButton;
	
	}

	public void viewClaimStatusAndViewDetails(String intimationNo, Long rodKey) {

		ViewTmpClaim viewClaim = null;

		try {
			final ViewTmpIntimation viewIntimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);
			// Hospitals hospital =
			// hospitalService.searchbyHospitalKey(intimation
			// .getKey());
			viewClaim = claimService.getTmpClaimforIntimation(viewIntimation.getKey());
			Intimation intimationObj = intimationService
					.getIntimationByKey(viewIntimation
							.getKey());
			
			if (viewClaim != null) {
				EarlierRodMapper instance = EarlierRodMapper.getInstance();
				ClaimStatusRegistrationDTO registrationDetails = instance.getRegistrationDetails(viewClaim);
				 intimationService.getIntimationByKey(intimationObj.getKey());
				ViewClaimStatusDTO intimationDetails = EarlierRodMapper
						.getViewClaimStatusDto(intimationObj);
				Hospitals hospitals = hospitalService
						.getHospitalById(intimationObj.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
				EarlierRodMapper.invalidate(instance);
				intimationDetails.setClaimKey(viewClaim.getKey());
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
				List<ViewDocumentDetailsDTO> listDocumentDetails = ackDocService
						.listOfEarlierAckByClaimKey(viewClaim.getKey(), rodKey);
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, viewClaim);
				
				if(null != intimationObj && null != intimationObj.getPolicy() && 
						null != intimationObj.getPolicy().getProduct() && 
						null != intimationObj.getPolicy().getProduct().getKey() &&
							(ReferenceTable.getGPAProducts().containsKey(intimationObj.getPolicy().getProduct().getKey()))){
					
					//intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					if(intimationObj.getPaPatientName() !=null && !intimationObj.getPaPatientName().isEmpty())
					{
						intimationDetails.setInsuredPatientName(intimationObj.getPaPatientName());
					}else
					{
						intimationDetails.setInsuredPatientName((intimationObj.getInsured() !=null && intimationObj.getInsured().getInsuredName() !=null) ? intimationObj.getInsured().getInsuredName() : "");
					}
				}
				if(null != intimationObj && null != intimationObj.getPolicy() && 
						null != intimationObj.getPolicy().getProduct() && 
						null != intimationObj.getPolicy().getProduct().getKey() &&
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

				DocAcknowledgement docAcknowledgement = ackDocService
						.findAcknowledgment(rodKey);
				if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					rodIntimationAndViewDetails
							.init(intimationDetails, rodKey);
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
				UI.getCurrent().addWindow(popup);
				popup = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
		}

		if (viewClaim == null) {
			getErrorMessage("Claim Number is not generated");
		}

	
	}
	
	public ViewTmpClaimPayment setPaymentDetails(ViewClaimStatusDTO bean,
			ViewTmpClaim claim) {

		try {
			
			ViewTmpClaimPayment reimbursementForPayment = null;
			
			ViewTmpReimbursement settledReimbursement = ackDocService.getLatestViewTmpReimbursementSettlementDetails(claim.getKey());
			if(settledReimbursement != null){
				
			     reimbursementForPayment = ackDocService.getClaimPaymentByRodNumber(settledReimbursement.getRodNumber());
				
			}else{
				reimbursementForPayment = ackDocService
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
		intimationDetails.setHospitalTypeValue(hospitalDetails
				.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		intimationDetails.setHospitalFlag(hospitals.getSuspiciousType());
		EarlierRodMapper.invalidate(instance);
	}
	
	public void showClickActionView(ViewClaimStatusDTO intimationDetails) {
		
		if (intimationDetails.getRodKey() != null) {
			rodIntimationAndViewDetails
					.init(intimationDetails, intimationDetails.getRodKey());
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
			
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}*/
	
	public void setPaymentTrials(List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList) {
		intimationUPRDetailTable.showPayModeTrial(viewPaymentTrailTableList);
	}
	
	public void showPaymentCancelDetailsTable(List<ClmPaymentCancelDto> chqCancelListDto,
			List<ClmPaymentCancelDto> neftCancelListDto) {
		intimationUPRDetailTable.showCancelDetailsTable(chqCancelListDto, neftCancelListDto);
	}
	
	public void showSettlementDetails(PaymentProcessCpuPageDTO paymentDto){
		intimationUPRDetailTable.showSettlementDetails(paymentDto);
	}

	public void showStopPaymentTracking(List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {
		intimationUPRDetailTable.showStopPaymentTracking(viewStopPaymentTrackingTableList);
		
	}
}