package com.shaic.claim.medical.opinion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
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
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpClaimPayment;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.domain.ViewTmpReimbursement;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

public class OpinionValidationTable extends GBaseTable<OpinionValidationTableDTO> {
	
	private static final long serialVersionUID = 1L;
	
	public HashMap<Long, Boolean> compMap = null;
	public HashMap<Long, String> remarksMap = null;
	
	CheckBox chkBoxAccept;
	
	CheckBox chkBoxReject;
	
	ComboBox cmbAction;	
	
	private TextField txtRemarks;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	@Inject
	private RodIntimationAndViewDetailsUI rodIntimationAndViewDetails;
	
	@EJB
	private CreateRODService billDetailsService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private AcknowledgementDocumentsReceivedService reimbursementService;
	
	private Long rodKey;
	
	@Inject
	private ViewDetails viewDetails;

	@Override
	public void removeRow() {
		table.removeAllItems();
		cancelOpinionStatusValue();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<OpinionValidationTableDTO>(OpinionValidationTableDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {
				"serialNumber",
				"intimationNumber",
				"updatedBy",
				"updatedDateTime",
				"updatedRemarks",
				"view",
				"action",
				"remarks"
		};
		generatecolumns();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setHeight("300px");
	}
	
	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7 ) {
			table.setPageLength(7);
		}
	}

	@Override
	public void tableSelectHandler(OpinionValidationTableDTO t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "opinion-validation-";
	}
	
	private void generatecolumns() {
		
		compMap =  new HashMap<Long, Boolean>();
		remarksMap = new HashMap<Long,String>();
		
		table.removeGeneratedColumn("action");
		table.addGeneratedColumn("action", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				cmbAction = new ComboBox();
				cmbAction.setWidth("160px");
				cmbAction.setTabIndex(1);
				cmbAction.setHeight("-1px");
				cmbAction.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbAction.setItemCaptionPropertyId("value");
				cmbAction.setData(tableDTO);
				
				
				SelectValue agree = new SelectValue();
				agree.setId(SHAConstants.OPINION_STATUS_APPROVE);
				agree.setValue("Agree");
				
				SelectValue disagree = new SelectValue();
				disagree.setId(SHAConstants.OPINION_STATUS_REJECT);
				disagree.setValue("Disagree");
					
				BeanItemContainer<SelectValue> sourceData = new BeanItemContainer<SelectValue>(SelectValue.class);
				sourceData.addBean(agree);
				sourceData.addBean(disagree);
					
				cmbAction.setContainerDataSource(sourceData);
				cmbAction.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbAction.setItemCaptionPropertyId("value");
				
				addListener(cmbAction, source);
				return cmbAction;
			}
		});
		
		table.removeGeneratedColumn("remarks");
		table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				txtRemarks = new TextField();
				txtRemarks.setData(tableDTO);
				txtRemarks.setEnabled(false);
				txtRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
				handleTextFieldPopup(txtRemarks,null);
				addTextboxListener(txtRemarks);
				return txtRemarks;
			}
		});
		
		
		/*table.removeGeneratedColumn("approve");
		table.addGeneratedColumn("approve", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)itemId;
				chkBoxAccept = new CheckBox("");
				chkBoxAccept.setData(tableDTO);
				chkBoxAccept.setStyleName("accept");
				addListener(chkBoxAccept);
				return chkBoxAccept;
			}
		});
		
		table.removeGeneratedColumn("reject");
		table.addGeneratedColumn("reject", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) itemId;
				chkBoxReject = new CheckBox("");	
				chkBoxReject.setStyleName("reject");
				chkBoxReject.setData(tableDTO);
				addListener(chkBoxReject);
				return chkBoxReject;
			}
		});*/
		
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {

						final Button viewIntimationDetailsButton = new Button();
						viewIntimationDetailsButton.setData(itemId);
						viewIntimationDetailsButton.setCaption("View Claim Status");
						
						viewIntimationDetailsButton
								.addClickListener(new Button.ClickListener() {
									public void buttonClick(
											ClickEvent event) {

										OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) itemId;
										Intimation intimation = intimationService.searchbyIntimationNo(tableDTO.getIntimationNumber());
										
										Claim a_claim = claimService
												.getClaimforIntimation(intimation
														.getKey());
										
										if (a_claim != null) {
												viewClaimStatusAndViewDetails(intimation.getIntimationId());
										}
										else if (a_claim == null) {
											viewDetails.getViewIntimation(intimation.getIntimationId());
										}
									}
								});
						viewIntimationDetailsButton
								.addStyleName(BaseTheme.BUTTON_LINK);
						return viewIntimationDetailsButton;
					}
				});
		
		
	}
	
	/*private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) chkBox.getData();
					boolean result = false;
					String checkboxName = chkBox.getStyleName();
					if(checkboxName.equalsIgnoreCase("accept")) {
						if (event.getProperty().getValue() == Boolean.TRUE) {
							result = true;
							CheckBox checkBox = findComponentById("reject", tableDTO.getValidationkey());
							if (checkBox != null) {
								checkBox.setValue(false);
							}
							compMap.put(tableDTO.getValidationkey() , result);
						} else {
							compMap.remove(tableDTO.getValidationkey());
						}
					} else {
						if (event.getProperty().getValue() == Boolean.TRUE) {
							result = false;
							CheckBox checkBox = findComponentById("accept", tableDTO.getValidationkey());
							if (checkBox != null) {
								checkBox.setValue(false);
							}
							compMap.put(tableDTO.getValidationkey() , result);
						} else {
							compMap.remove(tableDTO.getValidationkey());
						}
					}
					
				}	
			}		
		});
	}*/
	
	private void addListener(final ComboBox comboBox, final Table source) { 	
		comboBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) comboBox.getData();
				if (null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					String result = event.getProperty().getValue().toString();
					if (result.equalsIgnoreCase("Agree")) {
						compMap.put(tableDTO.getValidationkey() , true);
						TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
						if (txtRemarksField != null) {
							txtRemarksField.setEnabled(true);
							txtRemarksField.setRequired(false);
							txtRemarksField.setRequiredError("");
						}
					} else if (result.equalsIgnoreCase("Disagree")) {
						compMap.put(tableDTO.getValidationkey() , false);
						TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
						if (txtRemarksField != null) {
							txtRemarksField.setRequired(true);
							txtRemarksField.setValidationVisible(true);
							txtRemarksField.setRequiredError("Enter remarks");
							txtRemarksField.setEnabled(true);
						}
					} 					
				} else {
					compMap.remove(tableDTO.getValidationkey());
					TextField txtRemarksField = findComponentById(tableDTO.getValidationkey());
					if (txtRemarksField != null) {
						txtRemarksField.setRequired(false);
						txtRemarksField.setRequiredError("");
						txtRemarksField.setEnabled(false);
						txtRemarksField.setValue("");
					}
				}
			}

				
		});
		
	}
	
	private void addTextboxListener(final TextField textRemarks) { 	
		textRemarks
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO) textRemarks.getData();
				if (null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					String remarks = event.getProperty().getValue().toString();			
					remarksMap.put(tableDTO.getValidationkey(), remarks);
				}
			}		
		});
		
	}
	public HashMap<Long, Boolean> getOpinionStatusValue() {
		return compMap;
	}
	
	public HashMap<Long, String> getOpinionStatusRemarks() {
		return remarksMap;
	}
	
	public HashMap<Long, Boolean> cancelOpinionStatusValue() {
		compMap.clear();
		remarksMap.clear();
		return compMap;
	}
	
	/*public CheckBox findComponentById(String style, Long id) {
		Iterator<Component> iterator = table.iterator();
		Component component = null;
		while (iterator.hasNext()) {
			component = iterator.next();
			if (component instanceof CheckBox) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)((AbstractComponent) component).getData();
				if (component.getStyleName().equalsIgnoreCase(style) && tableDTO.getValidationkey().equals(id)) {
					return (CheckBox) component;
				}
			}
		}
		return null;
	}*/
	
	public TextField findComponentById(Long id) {
		Iterator<Component> iterator = table.iterator();
		Component component = null;
		while (iterator.hasNext()) {
			component = iterator.next();
			if (component instanceof TextField) {
				OpinionValidationTableDTO tableDTO = (OpinionValidationTableDTO)((AbstractComponent) component).getData();
				if (tableDTO.getValidationkey().equals(id)) {
					return (TextField) component;
				}
			}
		}
		return null;
	}
	
	public void viewClaimStatusAndViewDetails(String intimationNo) {

		ViewTmpClaim claim = null;

		try {
			final ViewTmpIntimation viewIntimation = intimationService
					.searchbyIntimationNoFromViewIntimation(intimationNo);
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
						(ReferenceTable.GPA_PRODUCT_KEY.equals(intimation.getPolicy().getProduct().getKey()))){
					
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
					}
				});

				popup.setModal(true);
				UI.getCurrent().addWindow(popup);
				popup = null;
			}
		} catch (Exception e) {
			Notification.show("Claim Number is not generated",
					Notification.TYPE_ERROR_MESSAGE);
		}

		if (claim == null) {
			getErrorMessage("Claim Number is not generated");
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
		dialog.show(getUI().getCurrent(), null, true);
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
		intimationDetails.setHospitalTypeValue(hospitalDetails
				.getHospitalTypeValue());
		intimationDetails.setHospitalIrdaCode(hospitalDetails
				.getHospitalIrdaCode());
		intimationDetails.setHospitalInternalCode(hospitalDetails
				.getHospitalInternalCode());
		intimationDetails.setHospitalCategory(hospitals.getFinalGradeName());
		EarlierRodMapper.invalidate(instance);
	}
	
	private void getTextRemarksField(OpinionValidationTableDTO tableDTO) {
		
	}
	
	@SuppressWarnings("unused")
	public  void handleTextFieldPopup(TextField searchField, final  Listener listener) {

		ShortcutListener enterShortCut = new ShortcutListener(
				"ShortcutForRedraftRemarks", ShortcutAction.KeyCode.F8, null) {

			private static final long serialVersionUID = 1L;
			@Override
			public void handleAction(Object sender, Object target) {
				((ShortcutListener) listener).handleAction(sender, target);
			}
		};
		handleShortcutForRedraft(searchField, getShortCutListenerForRemarks(searchField));

	}

	public  void handleShortcutForRedraft(final TextField textField, final ShortcutListener shortcutListener) {
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				textField.addShortcutListener(shortcutListener);

			}
		});
		textField.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				textField.removeShortcutListener(shortcutListener);

			}
		});
	}
	
	private ShortcutListener getShortCutListenerForRemarks(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("Remarks(Agree/Disagree)",KeyCodes.KEY_F8,null) {

			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void handleAction(Object sender, Object target) {
				VerticalLayout vLayout =  new VerticalLayout();

				vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
				vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
				vLayout.setMargin(true);
				vLayout.setSpacing(true);
				final TextArea txtField = new TextArea();
				txtField.setStyleName("Boldstyle"); 
				txtField.setValue(txtFld.getValue());
				txtField.setNullRepresentation("");
				txtField.setSizeFull();
				txtField.setWidth("100%");
				txtField.setMaxLength(1000);
				txtField.setReadOnly(false);
				txtField.setHeight("400px");
//				txtField.setRows(25);

				txtField.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						txtFld.setValue(((TextArea)event.getProperty()).getValue());
					}
				});
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtField);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);

				final Window dialog = new Window();

				String strCaption = "Remarks(Agree/Disagree)";

				dialog.setCaption(strCaption);

				dialog.setHeight(vLayout.getHeight(), Sizeable.UNITS_PERCENTAGE);
				dialog.setWidth("45%");
				dialog.setClosable(true);

				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};

		return listener;
	}

}
