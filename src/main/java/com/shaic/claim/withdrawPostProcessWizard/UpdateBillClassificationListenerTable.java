package com.shaic.claim.withdrawPostProcessWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.SearchWithdrawCashLessPostProcessTableDTO;
import com.shaic.claim.cashlessprocess.withdrawpreauthpostprocess.WithDrawPostProcessBillDetailsDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.billclassification.BillClassificationEditUI;
import com.shaic.claim.reimbursement.billclassification.BillClassificationUI;
import com.shaic.claim.reimbursement.billing.benefits.wizard.forms.AddOnBenefitsDataExtractionPage;
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.billing.wizard.BillingProcessButtonsUIForFirstPage;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.BillEntryTableUI;
import com.shaic.claim.rod.wizard.pages.CreateRODDocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.pages.DocumentDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsListenerTable;
import com.shaic.claim.rod.wizard.tables.ReconsiderRODRequestListenerTable.ImmediateFieldFactory;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UpdateBillClassificationListenerTable extends ViewComponent {

	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<WithDrawPostProcessBillDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<WithDrawPostProcessBillDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<WithDrawPostProcessBillDetailsDTO> container = new BeanItemContainer<WithDrawPostProcessBillDetailsDTO>(WithDrawPostProcessBillDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	private Button editBillClassification;
	
	@Inject
	private PreauthDTO bean;
	
	@Inject
	private Instance<UploadedDocumentsListenerTable> uploadDocumentListenerTable;
	
	@Inject
	private Instance<BillingProcessButtonsUIForFirstPage> billingProcessButtonInstance;
	
	
	
	@Inject
	private Instance<BillClassificationEditUI> billClassificationEditUIInstance;
	
	private BillClassificationEditUI billClassificationEditUIObj;
	
	@Inject
	private UploadedDocumentsListenerTable uploadDocumentListenerTableObj;
	
	@Inject
	private BillClassificationUI billClassificationUI;
	
	@Inject
	private Instance<AddOnBenefitsDataExtractionPage> addOnBenifitsPageInstance;
	
	private AddOnBenefitsDataExtractionPage addOnBenefitsPageObj;
	
	@Inject
	private Instance<BillEntryTableUI> billEntryTableInstance;
	
	private BillEntryTableUI billEntryTableObj;
	
	private TextField addmissionDateFld;
	
	private TextField dischargeDateFld;
	
	public Map<String, Object> referenceData;
	
	private List<UploadDocumentDTO> uploadDocsDTO;
	
	private TextField txtStatusOfBills;
	
	private  List<UploadDocumentDTO> billClassifacationUploadDocsDTO;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	//private int iItemValue = 0;
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init() {
		this.bean = bean;
	//	populateBillDetails(bean);
		container.removeAllItems();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		//validator = factory.getValidator();
		//this.errorMessages = new ArrayList<String>();
		/*btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
        txtStatusOfBills = new TextField();
		
		txtStatusOfBills.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		HorizontalLayout btnLayout = new HorizontalLayout(txtStatusOfBills);
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		
		//VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		//layout.setMargin(true);
		//layout.addComponent(btnLayout);
		
		initTable();
		table.setWidth("90%");
		table.setHeight("150px");
		table.setPageLength(table.getItemIds().size());
		table.setCaption("Update Bill Classification");
		//table.setSizeFull();
		
		addEditListener();
		
		layout.addComponent(table);
	//	layout.addComponent(btnAdd);
//		layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(layout);
//		horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		horLayout.setWidth("90%");
		horLayout.setHeight("200px");
		
//		Panel tblPanel = new Panel();
//		tblPanel.setWidth("90%");
//		tblPanel.setHeight("200px");
//		tblPanel.setContent(horLayout);
		
		//horLayout.setWidth("100%");
		
		Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		tablePanel.setWidth("91%");
//		setCompositionRoot(tblPanel);
*/		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		txtStatusOfBills = new TextField();
		
		txtStatusOfBills.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		HorizontalLayout btnLayout = new HorizontalLayout(txtStatusOfBills);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(txtStatusOfBills, Alignment.MIDDLE_RIGHT);
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
//		/layout.addComponent(btnLayout);
		initTable();
		table.setWidth("100%");
		table.setHeight("150px");
		table.setPageLength(table.getItemIds().size());
		addListener();
		layout.addComponent(btnLayout);
		layout.addComponent(table);
		setCompositionRoot(layout);
	}
	
	public void setTableList(final List<WithDrawPostProcessBillDetailsDTO> list) {
		table.removeAllItems();
		for (WithDrawPostProcessBillDetailsDTO withDrawTableDTO : list) {
			table.addItem(withDrawTableDTO);
		}
		table.sort();
	}
	
	public void setBean(PreauthDTO preauth){
		this.bean = preauth;
	}
	
	

	protected void addEditListener() {
		editBillClassification = new Button("EDIT");
		editBillClassification.setStyleName(ValoTheme.BUTTON_LINK);
		editBillClassification.setEnabled(false);
	editBillClassification.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				//BeanItem<ReconsiderRODRequestTableDTO> addItem = container.addItem(new ReconsiderRODRequestTableDTO());
				container.addItem(new UpdateBillClassificationListenerDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setVisibleColumns(new Object[] { "rodNo","docReceivedDate", "modeOfReceipt","billClassification", "approvedAmount","remstatus"});
		table.setColumnHeader("rodNo", "ROD No");
		table.setColumnHeader("docReceivedDate", "DocumentReceivedDate");
		table.setColumnHeader("modeOfReceipt", "ModeOfReceipt");
		table.setColumnHeader("billClassification", "Bill </br>Classification");
		table.setColumnHeader("approvedAmount", "Approved Amount");
		table.setColumnHeader("remstatus", "ROD </br> status ");
//		table.setColumnHeader("select", "Select");
		table.setEditable(true);
//		table.removeGeneratedColumn("viewstatus");
		
		table.removeGeneratedColumn("Update Bill Classification");
		table.addGeneratedColumn("Update Bill Classification",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
		
				 final WithDrawPostProcessBillDetailsDTO uploadDocDTO = (WithDrawPostProcessBillDetailsDTO)itemId;
				Button button = new Button("Update Bill Classification");
//				if(!ReferenceTable.getFinancialApprovalStatus().containsKey(uploadDocDTO.getRemstatusKey()) || uploadDocDTO.getPreauthDto().getPartialHospitalizaionFlag().equals(Boolean.TRUE)){
//				button.setEnabled(true);
//				}
				if(uploadDocDTO.getIsReconsiderationRequest().equals(true) && (ReferenceTable.getFinancialApprovalStatus().containsKey(uploadDocDTO.getRemstatusKey()) 
						|| uploadDocDTO.getRemstatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS))){	
					button.setEnabled(false);
				}
				else {
					button.setEnabled(true);
				}
				if(uploadDocDTO.getIsReconsiderationRequest().equals(true) && (ReferenceTable.getFinancialApprovalStatus().containsKey(uploadDocDTO.getRemstatusKey()) 
						|| uploadDocDTO.getRemstatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS))){	
					uploadDocDTO.getPreauthDto().setHospitalizaionFlag(true);
				}
					
				button.addClickListener(new Button.ClickListener() {
					
				
				public void buttonClick(ClickEvent event) {
//					UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					okBtn.setEnabled(false);
//					uploadDocumentListenerTableObj = uploadDocumentListenerTable.get();
//					// uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING);
//					uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING,
//							null, null);
//					uploadDocumentListenerTableObj.init();
					billClassificationEditUIObj = billClassificationEditUIInstance
							.get();
					billClassificationEditUIObj.setPresenter(SHAConstants.WITHDRAW_CLASSIFICATION);
//					fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.WITHDRAW_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
					billClassificationEditUIObj.initForEdit(uploadDocDTO.getPreauthDto(), billClassifacationUploadDocsDTO,
							uploadDocumentListenerTableObj, okBtn);
					
					billClassificationEditUIObj.setReferenceData(referenceData);

					VerticalLayout layout = new VerticalLayout(
							billClassificationEditUIObj, okBtn);
					layout.setComponentAlignment(okBtn, Alignment.MIDDLE_CENTER);

					final ConfirmDialog dialog = new ConfirmDialog();
					// dialog.setCaption("Alert");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);

					okBtn.addClickListener(new ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							billClassificationEditUIObj.validatePage();
							billClassificationUI.init(bean);
							bean.setIsEditBillClassification(true);

							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setHospitalCashAddonBenefits(
											bean.getAddOnBenefitsHospitalCash());
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setPatientCareAddOnBenefits(
											bean.getAddOnBenefitsPatientCare());
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setHospitalBenefitFlag(
											bean.getAddOnBenefitsHospitalCash() ? "HC"
													: null);
							bean.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO()
									.setPatientCareBenefitFlag(
											bean.getAddOnBenefitsPatientCare() ? "PC"
													: null);
						   setOtherBenefitsValueToDTO(bean);
						   
						  bean.setDocAckKey(bean.getUploadDocDTO().getAckDocKey());
							
							Boolean isStarCare = false;
							if (bean.getNewIntimationDTO() != null
									&& bean.getNewIntimationDTO().getPolicy()
											.getProduct() != null
									&& (bean.getNewIntimationDTO()
											.getPolicy()
											.getProduct()
											.getKey()
											.equals(ReferenceTable.STAR_CARE_FLOATER) || bean
											.getNewIntimationDTO()
											.getPolicy()
											.getProduct()
											.getKey()
											.equals(ReferenceTable.STAR_CARE_INVIDUAL))) {
								isStarCare = true;
							}
							
							addOnBenefitsPageObj = addOnBenifitsPageInstance.get();
							addOnBenefitsPageObj.init(bean
									.getPreauthDataExtractionDetails()
									.getUploadDocumentDTO(), SHAConstants.BILLING,
									isStarCare);
							addOnBenefitsPageObj.getContent();

							Button finalOKButton = new Button("OK");
							finalOKButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

							Label label = new Label("Bill Classification Changed");

							VerticalLayout verti = new VerticalLayout(label,
									finalOKButton);
							verti.setSpacing(true);
							verti.setMargin(true);
							verti.setComponentAlignment(label, Alignment.TOP_CENTER);
							verti.setComponentAlignment(finalOKButton,
									Alignment.BOTTOM_CENTER);
							final Window popup = new com.vaadin.ui.Window();
							popup.setWidth("20%");
							popup.setHeight("10%");
							popup.setContent(verti);
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

							finalOKButton.addClickListener(new ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									popup.close();

								}
							});

							dialog.close();

						}
					});
				}
				});
				return button;
		 }
		});
		
		table.removeGeneratedColumn("Edit Bill Entry");
		table.addGeneratedColumn("Edit Bill Entry",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
		
						/*uploadDocumentListenerTableObj = uploadDocumentListenerTable.get();
						// uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING);
						uploadDocumentListenerTableObj.initPresenter(SHAConstants.BILLING,
								null, null);
						uploadDocumentListenerTableObj.init();*/
		/*		final UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
				final Button button = new Button("Edit Bill Entry");
				if(!ReferenceTable.getFinancialApprovalStatus().containsKey(uploadDocDTO.getRemstatusKey()) || uploadDocDTO.getPreauthDto().getPartialHospitalizaionFlag().equals(Boolean.TRUE)){
					button.setEnabled(true);
				}
				else{
					button.setEnabled(false);
				}
				 button.addClickListener(new Button.ClickListener() {

						public void buttonClick(ClickEvent event) {					
//							UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
							
								fireViewEvent(WithdrawPreauthPostProcessWizardPresenter.BILL_CLASSIFICATION_LOAD_BILL_DETAILS_VALUES, uploadDocDTO);
							}
				 });*/

//				return button;
						
				final Button button = new Button("Edit Bill Entry");
				final WithDrawPostProcessBillDetailsDTO uploadDocDTO = (WithDrawPostProcessBillDetailsDTO)itemId;
				if(uploadDocDTO.getIsReconsiderationRequest().equals(true) && (ReferenceTable.getFinancialApprovalStatus().containsKey(uploadDocDTO.getRemstatusKey()) 
						|| uploadDocDTO.getRemstatusKey().equals(ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS))){	
					button.setEnabled(false);
				}
				else {
					button.setEnabled(true);
				}
//				UploadDocumentDTO uploadDocDTO = (UploadDocumentDTO)itemId;
				button.addClickListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {					
						
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("Update Bill Entry");
						popup.setWidth("75%");

						popup.setHeight("45%");
						
//						popup.setSizeFull();
						uploadDocumentListenerTableObj = uploadDocumentListenerTable.get();
						uploadDocumentListenerTableObj.initPresenter(SHAConstants.WITHDRAW_CASHLESS,
								null, null);
						uploadDocumentListenerTableObj.init();
						if(uploadDocumentListenerTableObj != null) {
							uploadDocumentListenerTableObj.setReferenceData(referenceData);
							
							Integer i = 1;
							uploadDocumentListenerTableObj.setTableInfo(uploadDocDTO.getUploadDocList());
							uploadDocsDTO = uploadDocDTO.getUploadDocList();
//							SectionDetailsTableDTO sectionDTO = this.sectionDetailsListenerTableObj.getValue();
							List<UploadDocumentDTO> uploadList = uploadDocDTO.getUploadDocList();
							if(null !=  uploadList && !uploadList.isEmpty())
							for (UploadDocumentDTO uploadDocLayout : uploadList) {
								if(null != bean.getNewIntimationDTO() && null != bean.getNewIntimationDTO().getPolicy() && null != bean.getNewIntimationDTO().getPolicy().getProduct())
								{
									uploadDocLayout.setProductKey( bean.getNewIntimationDTO().getPolicy()
									.getProduct().getKey());
								}
								/*if(null != bean.getPreauthDataExtractionDetails().getRoomCategory()) {
									uploadDocLayout.setRoomCategory(bean.getPreauthDataExtractionDetails().getRoomCategory());
								}
								if(null != bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey()) {
									uploadDocLayout.setDocREceivedFromId(bean.getPreauthDataExtractionDetails().getDocAckknowledgement().getDocumentReceivedFromId().getKey());
								}
								if(null != bean.getClaimDTO().getClaimSubCoverCode())// && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover() && null != this.bean.getPreauthDataExtractionDetails().getSectionDetailsDTO().getSubCover().getCommonValue())
									uploadDocLayout.setSubCoverCode(bean.getClaimDTO().getClaimSubCoverCode());
								if(null != bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation())
									uploadDocLayout.setDomicillaryFlag(bean.getPreauthDataExtractionDetails().getDomicillaryHospitalisation());*/
								uploadDocLayout.setSeqNo(i);
								uploadDocumentListenerTableObj.addBeanToList(uploadDocLayout);
								i++;
							}
						}
						popup.setContent(uploadDocumentListenerTableObj);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
						popup.setWidth("90%");
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
				});
				return button;
			}
			});
		table.setTableFieldFactory(new ImmediateFieldFactory());
	}
	
//	private void displayClaimStatus(String intimationNo)
//	{
//		//objViewDetails = viewDetails.get();
//		//queryDetailsObj.init(viewDetails);
//		objViewDetails.viewClaimStatusUpdated(intimationNo);
//	}
//	
//	public void setViewDetailsObj(ViewDetails viewDetails)
//	{
//		objViewDetails = viewDetails;
//	//	initTable();
//	}
//	
//	/*public void setReferenceData(Map<String, Object> referenceData) {
//		this.referenceData = referenceData;
//	}*/
//	
//	
//	
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<WithDrawPostProcessBillDetailsDTO> addItem = container.addItem(new WithDrawPostProcessBillDetailsDTO());
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
	}
	
	public void manageListeners() {
		}
	
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			WithDrawPostProcessBillDetailsDTO entryDTO = (WithDrawPostProcessBillDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				field.setMaxLength(50);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				
				tableRow.put("rodNumber", field);
				return field;
			}
			
			else if ("docReceivedDate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("documentReceivedDate", field);

				return field;
			}
			else if ("modeOfReceipt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("modeOfReceipt", field);
				return field;
			}
			else if ("billClassification".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("billClassification", field);
				return field;
			}
			else if("approvedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
			
				field.setReadOnly(true);
				
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("approvedAmount", field);
				return field;
			}
			else if("remstatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
			
				field.setReadOnly(true);
				
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("status", field);
				return field;
			}
//			else if("select".equals(propertyId)) {
//				CheckBox field = new CheckBox();
//				field.setReadOnly(false);
//				field.setData(entryDTO);
//				//IMSSUPPOR-26556
//				if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.CREATE_ROD)){
//					Boolean select = entryDTO.getSelect();		
//					if((select == null) || (select != null && ! select)){
//						field.setEnabled(false);
//					}
//				}
//				valueChangeListenerForSelect(field);
//				if(null != entryDTO && null != entryDTO.getSelect() && entryDTO.getSelect())
//				{
//					field.setValue(entryDTO.getSelect());
//				}
//				tableRow.put("select", field);
//				
//				return field;
//			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	
	/*private void generateSlNo(TextField txtField)
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("itemNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}*/
	
	 public void addBeanToList(List<WithDrawPostProcessBillDetailsDTO> billEntryDetailsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(billEntryDetailsDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	 public List<WithDrawPostProcessBillDetailsDTO> getValues() {
			List<WithDrawPostProcessBillDetailsDTO> itemIds = (List<WithDrawPostProcessBillDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 

		
	
	 private void setOtherBenefitsValueToDTO(PreauthDTO bean)
		{
			if(null != bean){			
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setOtherBenefit(bean.getOtherBenefitsFlag());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setEmergencyMedicalEvaluation(bean.getEmergencyMedicalEvaluation());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setRepatriationOfMortalRemains(bean.getRepatriationOfMortalRemains());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setCompassionateTravel(bean.getCompassionateTravel());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setPreferredNetworkHospital(bean.getPreferredNetworkHospital());
			
			bean.getPreauthDataExtractionDetails()
			.getUploadDocumentDTO()
			.setSharedAccomodation(bean.getSharedAccomodation());
			}
		}
	 
	 public void loadBillEntryValuesforbill(UploadDocumentDTO uploadDocDTO)
		{

			Window popup = new com.vaadin.ui.Window();
			popup.setCaption("Bill Entry");
			popup.setWidth("75%");
			//popup.setWidth("100%");
			popup.setHeight("85%");
			
			popup.setSizeFull();
			billEntryTableObj = billEntryTableInstance.get();
			presenterString = SHAConstants.WITHDRAW_CASHLESS;
			
			if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
			{
				billEntryTableObj.initPresenter(presenterString, addmissionDateFld, dischargeDateFld);
			}
			else if((SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString))
			{
				billEntryTableObj.initPresenter(presenterString, uploadDocDTO.getiNoOfEmptyRows());
			}
			else if((SHAConstants.PA_BILL_ENTRY).equalsIgnoreCase(presenterString))
			{
				billEntryTableObj.initPresenter(presenterString, uploadDocDTO.getiNoOfEmptyRows());
			}
			else if((SHAConstants.WITHDRAW_CASHLESS).equalsIgnoreCase(presenterString))
			{
				billEntryTableObj.initPresenter(presenterString, addmissionDateFld, dischargeDateFld);
			}
			else
			{
				billEntryTableObj.initPresenter(presenterString);
			}
			 
			Boolean isRemarksRequired = false;
			Boolean isZonalReview = false;
			Boolean isBilling = false;
			Boolean isFinancial = false;
				if (presenterString != null
						&& !presenterString
								.equalsIgnoreCase("")) {
					isRemarksRequired = true;
					if (presenterString
							.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
						isZonalReview = true;
						popup.setCaption("Bill Review");
					} else if (presenterString
							.equalsIgnoreCase(SHAConstants.BILLING)) {
						isBilling = true;
						popup.setCaption("Bill Review");
					} else if (presenterString
							.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
						isFinancial = true;
						popup.setCaption("Bill Review");
					} else if (presenterString
							.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
						popup.setCaption("Bill Review");
					} else if (presenterString
							.equalsIgnoreCase(SHAConstants.ADD_ADDITIONAL_DOCS)) {
						isRemarksRequired = false;
					} else if(presenterString.equalsIgnoreCase(SHAConstants.BILL_ENTRY)){
						isRemarksRequired = false;
					}
					else if (presenterString
							.equalsIgnoreCase(SHAConstants.WITHDRAW_CASHLESS)) {
						isBilling = true;
						popup.setCaption("Bill Review");
					}
					else if(presenterString.equalsIgnoreCase(SHAConstants.PA_BILL_ENTRY)){
						isRemarksRequired = false;}
				}
			

				billEntryTableObj.init((UploadDocumentDTO)uploadDocDTO, referenceData, popup, isRemarksRequired, isZonalReview, isBilling, isFinancial);
				billEntryTableObj.addShortcutListener(billEntryTableObj.setShortcuts());
				
		//billStatus = ((UploadDocumentDTO)itemId).getStatus();
				popup.setContent(billEntryTableObj);
				popup.setClosable(true);
		
				popup.center();
				popup.setResizable(true);
			popup.setWidth("90%");
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
	 
	 
	 
	public void setBillClassificationsDynamically(Boolean isNeededPre, Boolean isNeededPost, Boolean isNeededHosp) {
			BeanItemContainer<SelectValue> object = (BeanItemContainer<SelectValue>) referenceData.get(SHAConstants.ALL_BILL_CLASSIFICATIONS);
			List<SelectValue> itemIds = object.getItemIds();
			BeanItemContainer<SelectValue> newValues = new BeanItemContainer<SelectValue>(SelectValue.class);
			List<SelectValue> selectedValues = new ArrayList<SelectValue>();
			for (SelectValue selectValue : itemIds) {
				if(isNeededPre & selectValue.getId().equals(ReferenceTable.PRE_HOSPITALIZATION) ) {
					selectedValues.add(selectValue);
				}
				if(isNeededPost & selectValue.getId().equals(ReferenceTable.POST_HOSPITALIZATION) ) {
					selectedValues.add(selectValue);
				}
				if(isNeededHosp & selectValue.getId().equals(ReferenceTable.HOSPITALIZATION) ) {
					selectedValues.add(selectValue);
				}
			}
				
			newValues.addAll(selectedValues);
			referenceData.put("billClassification", newValues);
	}
	
	public void setReferenceData(Map<String, Object> reference) {
		 referenceData = reference;
	}
	
	 public void setupCategoryValues(
				BeanItemContainer<SelectValue> categoryValues) {
			billEntryTableObj.setUpCategoryValues(categoryValues);
	}
	 
	 public void updateTable(List<UploadDocumentDTO> dto)
	 {

		 container.removeAllItems();
		 for (UploadDocumentDTO uploadDocumentDTO : dto) {
			 addBeanToList(uploadDocumentDTO);
		}
		
		 
	 }
	 
	 public void addBeanToList(UploadDocumentDTO uploadDocumentsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 
		 if(null != uploadDocumentsDTO.getFileType() && (uploadDocumentsDTO.getFileType().getValue().contains("Bills") || uploadDocumentsDTO.getFileType().getValue().contains("Bill") 
				 || uploadDocumentsDTO.getFileType().getValue().contains("FA")))
		 {
			 container.addItem(uploadDocumentsDTO);
		 }		 
		 updateStatusOfBills(uploadDocumentsDTO);

	    }
	 
	 private void updateStatusOfBills(UploadDocumentDTO uploadDTO)
	 {
		 int i = 0;
		 txtStatusOfBills.setReadOnly(false);
		 if(null != uploadDocsDTO && !uploadDocsDTO.isEmpty())
		 {
			 int iSize = uploadDocsDTO.size();
			 for (UploadDocumentDTO uploadDto : uploadDocsDTO) {
				if(uploadDto.getStatus())
				{
					i++;
				}
			}
			 String txtMsg = i +" of" + " "+ iSize +"  records completed";
				txtStatusOfBills.setValue(txtMsg);
				txtStatusOfBills.setReadOnly(true);
		 }
	 }
	 
	 public void setTableInfo(List<UploadDocumentDTO> uploadDocsDTOList)
		{
			
			for (UploadDocumentDTO uploadDocumentDTO : uploadDocsDTOList) {
				if(null != uploadDocumentDTO.getFileType() && uploadDocumentDTO.getFileType().getId() != null && ReferenceTable.CASHLESS_SETTLEMENT_BILL_KEY.equals(uploadDocumentDTO.getFileType().getId())) {
					for (UploadDocumentDTO uploadDocumentDTOForBill : uploadDocsDTOList) {
						if(null != uploadDocumentDTOForBill.getFileType() && uploadDocumentDTOForBill.getFileType().getId() != null && ReferenceTable.getFinalBillKeys().containsKey(uploadDocumentDTOForBill.getFileType().getId())) {
							uploadDocumentDTO.setDmsDocToken(uploadDocumentDTOForBill.getDmsDocToken());
							uploadDocumentDTO.setFileName(uploadDocumentDTOForBill.getFileName());
							break;
						}
					}
					break;
				}
			}
			
			uploadDocsDTO = new ArrayList<UploadDocumentDTO>();
			if(null != uploadDocsDTOList && !uploadDocsDTOList.isEmpty())
			{
				for (UploadDocumentDTO uploadDocumentsDTO : uploadDocsDTOList) {
					if(null != uploadDocumentsDTO.getFileType() && (uploadDocumentsDTO.getFileType().getValue().contains("Bills") || uploadDocumentsDTO.getFileType().getValue().contains("Bill") ))
					 {
						uploadDocsDTO.add(uploadDocumentsDTO);
					 }
				}
			}
			
		}
	 
	 public void setBillClassificationBillEntries(List<UploadDocumentDTO> uploadDTO){
		 billClassifacationUploadDocsDTO = uploadDTO;
		 uploadDocsDTO = uploadDTO;
	 }
	 
	 public void setBillEntryStatus(UploadDocumentDTO uploadDTO) {
			if (uploadDocumentListenerTableObj != null) {
				List<UploadDocumentDTO> uploadWithDrawDoc = uploadDocumentListenerTableObj
						.getValues();
				List<UploadDocumentDTO> uploadDoc = new ArrayList<UploadDocumentDTO>();
				/*for (WithDrawPostProcessBillDetailsDTO uploadWithDrawDocumentDTO : uploadWithDrawDoc) {
					UploadDocumentDTO uploadedDoc = new UploadDocumentDTO();
					uploadDoc.uploadWithDrawDocumentDTO.getUploadDocList();
				}*/
				List<UploadDocumentDTO> uploadList = new ArrayList<UploadDocumentDTO>();
				for (UploadDocumentDTO uploadDocumentDTO : uploadWithDrawDoc) {
					if (null != uploadDocumentDTO.getFileType()
							&& null != uploadDocumentDTO.getFileType().getValue()) {
						if (uploadDocumentDTO.getFileType().getValue()
								.contains("Bill")) {
							/*
							 * if(uploadDocumentDTO.getBillNo().equalsIgnoreCase(
							 * uploadDTO.getBillNo())) { uploadList.add(uploadDTO);
							 * } else { uploadList.add(uploadDocumentDTO); }
							 */

							/**
							 * Sequence number is an internal parameter maintained
							 * for updating the uploadlistener table. This is
							 * because the row for which the bill is entered should
							 * only get updated. Rest of rows should be the same.
							 * Earlier this was done with bill no. But there are
							 * chance that even bill no can be duplicate. Hence
							 * removed this and added validation based on seq no.
							 * */
							if (uploadDocumentDTO.getSeqNo().equals(
									uploadDTO.getSeqNo())) {
								//uploadList.add(uploadDTO);
							} else {
								uploadList.add(uploadDocumentDTO);
							}

						} else {
							uploadList.add(uploadDocumentDTO);
						}
					}

				}
				uploadList.add(uploadDTO);
				uploadDocumentListenerTableObj.updateTable(uploadList);
			}
		}
		

}
