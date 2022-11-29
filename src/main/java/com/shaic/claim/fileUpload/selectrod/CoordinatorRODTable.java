package com.shaic.claim.fileUpload.selectrod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
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
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class CoordinatorRODTable extends ViewComponent {

	private static final long serialVersionUID = -2451354773032502514L;

	private Map<CoordinatorRODTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<CoordinatorRODTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<CoordinatorRODTableDTO> data = new BeanItemContainer<CoordinatorRODTableDTO>(CoordinatorRODTableDTO.class);

	private Table table;
	
	CoordinatorRODTableDTO bean;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	@Inject
	private RodIntimationAndViewDetailsUI rodIntimationAndViewDetails;
	
	@Inject
	private ViewDetails viewDetails;

	public Object[] VISIBLE_COLUMNS = new Object[] {"sno","rodNumber", "billClasification", "rodClaimedAmount", "rodApprovedAmount", "rodStatus", "ClaimStatus" };

	public void init(CoordinatorRODTableDTO bean) {
		this.bean = bean;
		VerticalLayout layout = new VerticalLayout();
		initTable(layout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}

	void initTable(VerticalLayout layout) {
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(1);
		table.setHeight("100px");

		
		table.addGeneratedColumn("ClaimStatus", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button deleteButton = new Button("View Claim Status");
				deleteButton.setData(itemId);
				deleteButton.setStyleName(ValoTheme.BUTTON_LINK);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;
					public void buttonClick(ClickEvent event) {

						CoordinatorRODTableDTO newIntimationDto = (CoordinatorRODTableDTO) itemId;
						Long intimationKey = newIntimationDto.getClaim().getIntimation().getKey();
						Intimation a_intimation = intimationService.getIntimationByKey(intimationKey);
						
						Claim a_claim = claimService.getClaimforIntimation(intimationKey);
						
						if (a_claim != null) {
							viewClaimStatusAndViewDetails(a_intimation.getIntimationId());
						}else if (a_claim == null) {
							viewDetails.getViewIntimation(a_intimation.getIntimationId());
						}
					
						/*
						if (table.getItemIds().size() > 0) {							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												private static final long serialVersionUID = 1L;
												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														table.removeItem(itemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
						}
					*/}
				});
				return deleteButton;
			}			
		});
		
		/*table.addGeneratedColumn("Check", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button deleteButton = new Button("Check");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						if (table.getItemIds().size() > 0) {							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												private static final long serialVersionUID = 1L;
												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														table.removeItem(itemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
						}
					}
				});
				return deleteButton;
			}			
		});*/

		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("billClasification", "Bill Classification");
		table.setColumnHeader("rodClaimedAmount", "Claimed Amount");
		table.setColumnHeader("rodApprovedAmount", "Approved Amount");
		table.setColumnHeader("rodStatus", "ROD Status");	
		
		table.setVisibleColumns(VISIBLE_COLUMNS);
		
		table.setColumnAlignment("sno", Align.CENTER);
		table.setColumnAlignment("rodNumber", Align.CENTER);
		table.setColumnAlignment("billClasification", Align.CENTER);
		table.setColumnAlignment("rodClaimedAmount", Align.CENTER);
		table.setColumnAlignment("rodApprovedAmount", Align.CENTER);
		table.setColumnAlignment("rodStatus", Align.CENTER);
		table.setColumnAlignment("ClaimStatus", Align.CENTER);
//		table.setColumnAlignment("Check", Align.CENTER);
		
		table.setSelectable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
		/*btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		
		
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;
			@Override
			public void buttonClick(ClickEvent event) {
				List<CoordinatorRODTableDTO> listOfItems = getValues();
				CoordinatorRODTableDTO newItem = new CoordinatorRODTableDTO();
				newItem.setSno(listOfItems.size()+1);
				addBeanToList(newItem);
			}
		});

		layout.addComponent(btnLayout);
		layout.setMargin(true);	*/	
		layout.addComponent(table);
	}

	public void setVisibleColumns() {
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}

	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = 7116790204338353464L;

		@Override
		public Field<?> createField(Container container, Object itemId,	Object propertyId, Component uiContext) {
			CoordinatorRODTableDTO rodTableDTO = (CoordinatorRODTableDTO) itemId;
			TextField rodfield = null;
			TextField classificationfield = null;
			TextField rodClaimAmtfield = null;
			TextField rodApprAmtfield = null;
			TextField rodStatusfield = null;
			TextField snofield = null;
			
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(rodTableDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(rodTableDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(rodTableDTO);
			}
			
			table.setColumnHeader("rodNumber", "ROD No");
			table.setColumnHeader("billClasification", "Bill Classification");
			table.setColumnHeader("rodClaimedAmount", "Claimed Amount");
			table.setColumnHeader("rodApprovedAmount", "Approved Amount");
			table.setColumnHeader("rodStatus", "ROD Status");	

			if("rodNumber".equals(propertyId)) {
				rodfield = new TextField();
				rodfield.setNullRepresentation("");
				rodfield.setData(rodTableDTO);
				rodfield.setWidth("200px");
				rodfield.setReadOnly(true);
				tableRow.put("rodNumber", rodfield);
				return rodfield;
			}if("billClasification".equals(propertyId)) {
				classificationfield = new TextField();
				classificationfield.setNullRepresentation("");
				classificationfield.setData(rodTableDTO);
				classificationfield.setWidth("200px");
				classificationfield.setMaxLength(1000);
				classificationfield.setReadOnly(true);
				tableRow.put("billClasification", classificationfield);
				return classificationfield;
			}if("rodClaimedAmount".equals(propertyId)) {
				rodClaimAmtfield = new TextField();
				rodClaimAmtfield.setNullRepresentation("");
				rodClaimAmtfield.setData(rodTableDTO);
				rodClaimAmtfield.setWidth("90px");
				rodClaimAmtfield.setReadOnly(true);
				tableRow.put("rodClaimedAmount", rodClaimAmtfield);
				return rodClaimAmtfield;
			}if("rodApprovedAmount".equals(propertyId)) {
				rodApprAmtfield = new TextField();
				rodApprAmtfield.setNullRepresentation("");
				rodApprAmtfield.setData(rodTableDTO);
				rodApprAmtfield.setWidth("90px");
				rodApprAmtfield.setReadOnly(true);
				tableRow.put("rodApprovedAmount", rodApprAmtfield);
				return rodApprAmtfield;
			}if("rodStatus".equals(propertyId)) {
				rodStatusfield = new TextField();
				rodStatusfield.setNullRepresentation("");
				rodStatusfield.setData(rodTableDTO);
				rodStatusfield.setWidth("150px");
				rodStatusfield.setReadOnly(true);
				tableRow.put("rodStatus", rodStatusfield);
				return rodStatusfield;
			}else {
				snofield = new TextField();
				snofield.setWidth("50px");
				snofield.setReadOnly(true);
				snofield.setData(rodTableDTO);
				tableRow.put("sno", snofield);
				return snofield;
			}
		}
	}


	public void addBeanToList(CoordinatorRODTableDTO rodTableDTO) {
		data.addBean(rodTableDTO);
	}
	public void addList(List<CoordinatorRODTableDTO> rodTableDTO) {
		for (CoordinatorRODTableDTO rodTableDTO2 : rodTableDTO) {
			data.addBean(rodTableDTO2);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CoordinatorRODTableDTO> getValues() {
		List<CoordinatorRODTableDTO> itemIds = (List<CoordinatorRODTableDTO>) this.table.getItemIds() ;
		return itemIds;
	}

	public void viewClaimStatusAndViewDetails(String intimationNo) {

		ViewTmpClaim claim = null;

		try {
			final ViewTmpIntimation viewIntimation = intimationService.searchbyIntimationNoFromViewIntimation(intimationNo);
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
				Hospitals hospitals = hospitalService.getHospitalById(intimation.getHospital());
				getHospitalDetails(intimationDetails, hospitals);
				intimationDetails
						.setClaimStatusRegistrionDetails(registrationDetails);
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
						.listOfEarlierAckByClaimKey(claim.getKey(), bean.getRodKey());
				intimationDetails
						.setReceiptOfDocumentValues(listDocumentDetails);

				setPaymentDetails(intimationDetails, claim);
				
				if(null != intimation && null != intimation.getPolicy() && 
						null != intimation.getPolicy().getProduct() && 
						null != intimation.getPolicy().getProduct().getKey() &&
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

				DocAcknowledgement docAcknowledgement = reimbursementService.findAcknowledgment(bean.getRodKey());
				
				NewIntimationDto intimationDto = intimationService.getIntimationDto(intimationObj);
				
				PreauthDTO preauthDTO = new PreauthDTO();
				preauthDTO.setNewIntimationDTO(intimationDto);
				Claim claimObj = intimationService.getClaimforIntimation(intimationObj.getKey());
				if(claimObj!=null){
					preauthDTO.setCrmFlagged(claimObj.getCrcFlag());
					preauthDTO.setVipCustomer(claimObj.getIsVipCustomer());
				}
				intimationDetails.setPreauthDTO(preauthDTO);
				
				if (docAcknowledgement != null
						&& docAcknowledgement.getHospitalisationFlag() != null
						&& !docAcknowledgement.getHospitalisationFlag()
								.equalsIgnoreCase("Y")) {
					rodIntimationAndViewDetails.init(intimationDetails, bean.getRodKey());
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
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				popup = null;
				EarlierRodMapper.invalidate(instance);
			}
		} catch (Exception e) {
			Notification.show("Claim Number is not generated", Notification.TYPE_ERROR_MESSAGE);
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
		}
	}
	
	private void getHospitalDetails(ViewClaimStatusDTO intimationDetails, Hospitals hospitals) {
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
		EarlierRodMapper.invalidate(instance);
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
