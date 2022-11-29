package com.shaic.claim.OMPProcessOmpClaimApprover.pages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPNegotiationDetailsDTO;
import com.shaic.claim.OMPProcessNegotiation.pages.OMPProcessNegotiationPageDetailTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimCalculationViewTableDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimDischargeDetailsPage;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorPagePresenter;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimRejectionPage;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewPaymentDetailTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewRRecoverableDetailTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPNewRecoverableTableDto;
import com.shaic.claim.OMPreceiptofdocumentsbillentry.page.OMPUploadDocumentsPage;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.rod.wizard.dto.ReceiptOfDocumentsDTO;
import com.shaic.domain.BankMaster;
import com.shaic.domain.MasterService;
import com.shaic.domain.PolicyNominee;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.omp.OMPClaimService;
import com.shaic.newcode.wizard.dto.NomineeDetailsDto;
import com.shaic.newcode.wizard.dto.NomineeDetailsTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class OMPNewCalculationTableForApprover extends ViewComponent {

	
	
	private Map<OMPClaimCalculationViewTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<OMPClaimCalculationViewTableDTO, HashMap<String, AbstractField<?>>>();
	
	private Map<OMPClaimCalculationViewTableDTO, CheckBox> tableItemchk = new HashMap<OMPClaimCalculationViewTableDTO, CheckBox>();
	private Map<OMPClaimCalculationViewTableDTO, Button> tableItemBtn = new HashMap<OMPClaimCalculationViewTableDTO, Button>();
	
	BeanItemContainer<OMPClaimCalculationViewTableDTO> container = new BeanItemContainer<OMPClaimCalculationViewTableDTO>(OMPClaimCalculationViewTableDTO.class);
	
	private Validator validator;
	
	private Set<String> errorMessages;
	
	//private Button btnAdd;
	
	private Table table;
	
	private Map<String, Object> referenceData;

	private OMPClaimProcessorDTO bean;

	public TextField dummyFieldExpTotal;
	
	private List<OMPClaimCalculationViewTableDTO> deletedList;
	
	private List<OMPClaimProcessorDTO> beanList;
	
	@Inject
	private Instance<OMPProcessNegotiationPageDetailTable> ompProcessNegotiationTableInstance;
	
	private OMPProcessNegotiationPageDetailTable ompProcessNegotiationTableObj;
	@Inject
	private Instance<OMPNewRRecoverableDetailTable> ompNewRRecoverableInstance;
	
	private OMPNewRRecoverableDetailTable ompNewRecoverableTableObj;
	@Inject
	private Instance<OMPNewPaymentDetailTable> ompNewPaymentDetailTableInstance;
	
	private OMPNewPaymentDetailTable ompNewPaymentDetailTableObj;
	
	@Inject
	private Instance<OMPUploadDocumentsPage> uploadDocumentsViemImpl;
	
	private OMPUploadDocumentsPage uploadDocumentView;
	
	private BeanItemContainer<SelectValue> copayContainer ;
	
	private BeanItemContainer<SelectValue> yesOrNoContainer;
	
	private Button recoverableButton;
	
	private Button paymentDetails;
	
	private SelectValue categoryValue =null;

	public TextField dummyField;

	public Button rodButton;
	
	protected Button rejectDetailsBut;
	protected Button disChDetailsBut;
	
	@Inject
	private OMPClaimRejectionPage rejectionPage;
	
	@Inject
	private OMPClaimDischargeDetailsPage disChargePage;
	
	@Inject
	private NomineeDetailsTable nomineeDetailsTable;
	
	@EJB
	private OMPClaimService ompClaimService;
	
	@EJB
	private MasterService masterService;
	
	private CheckBox dichargeChkBox = null;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private List<Long> OMPStatus = new ArrayList<Long>();
	private Boolean isReconsidervalueChanged = false;
	private Boolean chkReconsidervalue= false;
	private CheckBox reConsiderChkBox =  null;
	
	public void init(OMPClaimProcessorDTO bean) {
		//	populateBillDetails(bean);
			this.bean = bean;
			container.removeAllItems();
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.errorMessages = new HashSet<String>();
			/*HorizontalLayout btnLayout = new HorizontalLayout();
			btnLayout.setWidth("100%");
			btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);*/
			
			dummyFieldExpTotal = new TextField();
			dummyField = new TextField();
			deletedList = new ArrayList<OMPClaimCalculationViewTableDTO>();
			beanList = new ArrayList<OMPClaimProcessorDTO>();
			//VerticalLayout layout = new VerticalLayout();
			HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(true);
			//layout.setMargin(true);
			//layout.addComponent(btnLayout);
			
			OMPStatus.add(3181L); // Processor Reject
			OMPStatus.add(3183L); // Approver Reject
			//OMPStatus.add(3182L);// Approver Approved or Submit to FA.
			OMPStatus.add(161L);// Settled Status
			
			initTable();
			table.setWidth("90%");
			table.setHeight("150px");
			table.setPageLength(table.getItemIds().size());
			//table.setSizeFull();
			
			table.addItemClickListener(new ItemClickEvent.ItemClickListener(){ 
			    @Override
			    public void itemClick(ItemClickEvent itemClickEvent) {
			        //System.out.println(itemClickEvent.getItemId().toString());
			    	if(itemClickEvent.getItemId()!=null){
			    		Item item = itemClickEvent.getItem();
			    		//table.setCellStyleGenerator(cellStyleGenerator)
			    		OMPClaimCalculationViewTableDTO itemId = (OMPClaimCalculationViewTableDTO) itemClickEvent.getItemId();
			    		
			    		
			    		if(itemId.getReject()!= null && itemId.getReject().equalsIgnoreCase("Y")){
			    			fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_REJECTION_REMARKS, itemId);
			    		}
			    		if(itemId.getSendforApprover()!=null && itemId.getSendforApprover().equalsIgnoreCase("Y")){
			    			fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_PROCESSOR_REMARKS, itemId);
			    		}
			    	}
			    }
			});
			
			copayContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			SelectValue selectValue = new SelectValue();
			selectValue.setId(1L);
			selectValue.setValue("0");
			SelectValue selectValue2 = new SelectValue();
			selectValue2.setId(2L);
			selectValue2.setValue("30");
			
			copayContainer.addBean(selectValue);
			copayContainer.addBean(selectValue2);
			
			yesOrNoContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			
			SelectValue selectValue3 = new SelectValue();
			selectValue3.setId(1L);
			selectValue3.setValue("Yes");
			SelectValue selectValue4 = new SelectValue();
			selectValue4.setId(2L);
			selectValue4.setValue("No");
			yesOrNoContainer.addBean(selectValue3);
			yesOrNoContainer.addBean(selectValue4);
			layout.addComponent(table);
		//	layout.addComponent(btnAdd);
			layout.setComponentAlignment(table, Alignment.TOP_RIGHT);
			//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
			
			HorizontalLayout horLayout = new HorizontalLayout();
			//horLayout.addComponent(btnLayout);
			horLayout.addComponent(layout);
			horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
			
			Panel tblPanel = new Panel();
			tblPanel.setWidth("90%");
			tblPanel.setHeight("200px");
			tblPanel.setContent(horLayout);
			
			//horLayout.setWidth("100%");
			table.setSortEnabled(Boolean.FALSE);
		/*	Panel tablePanel = new Panel();
			tablePanel.setContent(horLayout);
			tablePanel.setWidth("91%");*/
			setCompositionRoot(tblPanel);
		/*	currencyRateListener();
			dropDownListener();
			//setCompositionRoot(horLayout);
			readOnlyListener();*/
		}

	public class ColorCellStyleGenerator implements CellStyleGenerator {

		@Override
		public String getStyle(Table source, Object itemId, Object propertyId) {
			// TODO Auto-generated method stub
			return "hover";
		}
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
	/*	table.setVisibleColumns(new Object[] {"serialNumber","category", "billAmt", "amtIn", "deduction","totalAmt", "totalAmtInr", "approvedAmt","agreedAmt", "differenceAmt", "expenses","negotiationClaimed",
				"negotiationCapping","negotiationPayable","handlingCharges","totalExp"});*/
		table.setVisibleColumns(new Object[] {"acknumber","rodnumber","classification","subClassification","rodClaimType","docRecivedFrm","lateDocReceivedDate","category","currencyType","currencyrate", "conversionValue","billAmt", 
				"amtIn", "deduction","totalAmt","copay","copayamount","approvedamountaftecopay","afternegotiation","finalApprovedAmtDollor","finalApprovedAmtInr","totalAmtInr", "select","approvedAmt","agreedAmt", "differenceAmt",
				"expenses","handlingCharges","totalExp","negotiationDone","negotiationDetails","uploadDocuments","recoverable","paymentDetails",/*"viewforApprover",*/"sendforApprover","disVoucFlag","dischargeDetails","reject","rejectDetails",
				"notinClaimCount","submit"});
		table.setColumnHeader("acknumber", "Acknowledgement number");
		table.setColumnHeader("rodnumber", "ROD.No");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("subClassification", "Sub Classification");
		table.setColumnHeader("rodClaimType", "Rod Claim Type");
		table.setColumnHeader("docRecivedFrm", "Document Received From");
		table.setColumnHeader("lateDocReceivedDate", "Last Document Received Date");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("currencyType", "Currency Type");
		table.setColumnHeader("currencyrate", "Currency Rate  (in to USD)");
		table.setColumnHeader("conversionValue", "Conversion Value(USD to INR)");
//		table.setColumnHeader("category", "Category");
		table.setColumnHeader("billAmt", "Bill amt (in FC)");
		table.setColumnHeader("amtIn", "Amt in $");
		table.setColumnHeader("deduction", "Deductible as per Policy  ($)");
		table.setColumnHeader("totalAmt", "Total amount in $");
		table.setColumnHeader("copay", "Co-pay %");
		table.setColumnHeader("copayamount", "Co-pay amount");
		table.setColumnHeader("approvedamountaftecopay", "Approved amount after co-pay");
		table.setColumnHeader("afternegotiation", "Amount after Negotiation ");
		table.setColumnHeader("finalApprovedAmtDollor", "Final Approved Amount $");
		table.setColumnHeader("finalApprovedAmtInr", "Final Approved Amount INR");
		table.setColumnHeader("totalAmtInr", "Total amount in INR");
		table.setColumnHeader("select", "Select");
		table.setColumnHeader("approvedAmt", "Approved Amount ($)");
		table.setColumnHeader("agreedAmt", "Agreed Amount ($)");
		table.setColumnHeader("differenceAmt", "Difference Amount ($)");
		table.setColumnHeader("expenses", "Expenses ($)");
		table.setColumnHeader("handlingCharges", "Handling Charges ($)");
		table.setColumnHeader("totalExp", "Total Expense  $");
		table.setColumnHeader("negotiationDone", "Negotiation Done(Y/N)");
		
		
//		table.setColumnHeader("negotiationDetails", "Negotiation Details");
		
		table.removeGeneratedColumn("negotiationDetails");
		addNegotiationDetail();
		
//		table.setColumnHeader("uploadDocuments", "Upload Documents");
		
		table.removeGeneratedColumn("uploadDocuments");
		table.addGeneratedColumn("uploadDocuments", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				Button uploadDetailsButton = new Button("Upload Details");
				uploadDetailsButton.addClickListener(new Button.ClickListener() {
					//OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
					//ReceiptOfDocumentsDTO receiptOfDocumentsDTO = calculationViewTableDTO.getReceiptOfDocumentsDTO();
					@Override
					public void buttonClick(ClickEvent event) {
						Window popup = new com.vaadin.ui.Window();
						uploadDocumentView = uploadDocumentsViemImpl.get();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
					 	//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESSOR_FETCH_UPLOAD_DIC,calculationViewTableDTO);
					 	ReceiptOfDocumentsDTO receiptOfDocumentsDTO = calculationViewTableDTO.getReceiptOfDocumentsDTO();
					 	receiptOfDocumentsDTO.setRodKeyFromPayload(calculationViewTableDTO.getRodKey());
					 	receiptOfDocumentsDTO.setIntimationNo(bean.getIntimationId());
					 	calculationViewTableDTO.setReceiptOfDocumentsDTO(receiptOfDocumentsDTO);
						uploadDocumentView.init(calculationViewTableDTO);
						Panel panel = new Panel(uploadDocumentView);
						VerticalLayout vlaLayout = new VerticalLayout();
						vlaLayout.addComponent(panel);
						vlaLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
						popup.setWidth("65%");
						popup.setHeight("60%");
						popup.setClosable(true);
						popup.setContent(vlaLayout);
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

				});
				uploadDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	uploadDetailsButton.setWidth("150px");
		    	uploadDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
				return uploadDetailsButton;
			}
		});
		table.setColumnHeader("uploadDocuments", "");
//		table.setColumnHeader("recoverable", "Recoverable");
		table.removeGeneratedColumn("recoverable");
		table.addGeneratedColumn("recoverable", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				
				recoverableButton = new Button("Recoverable");
				recoverableButton.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						
						final Window popup = new com.vaadin.ui.Window();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
						HashMap<String, AbstractField<?>> hashMap2 = tableItem.get(calculationViewTableDTO);
						GComboBox clasifi = (GComboBox)hashMap2.get("classification");
						GComboBox docRecivedFrm = (GComboBox)hashMap2.get("docRecivedFrm");
						SelectValue value = (SelectValue) clasifi.getValue();
						SelectValue value2 = (SelectValue) docRecivedFrm.getValue();
						if(value!=null && value.getValue().equalsIgnoreCase("OMP Claim Related") 
								&& bean.getIsCashless()!= null && bean.getIsCashless().equals(Boolean.TRUE)
								&& (calculationViewTableDTO.getDeduction()!=null && calculationViewTableDTO.getDeduction()!=0)
								&& (calculationViewTableDTO.getRodClaimType()!=null
								&& calculationViewTableDTO.getRodClaimType().getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY))
								){
						//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_RECOVERY_FETCH,calculationViewTableDTO);	
						List<OMPNewRecoverableTableDto> ompRecoverableTableList = calculationViewTableDTO.getOmpRecoverableTableList();
						ompNewRecoverableTableObj =  ompNewRRecoverableInstance.get();
						calculationViewTableDTO.setScreenName(SHAConstants.OMP_APPROVER);
						ompNewRecoverableTableObj.init(calculationViewTableDTO);
						ompNewRecoverableTableObj.setCaption("Recoverable Details");
						if(calculationViewTableDTO.getOmpRecoverableTableList() != null){
							ompNewRecoverableTableObj.setTableList(ompRecoverableTableList);
						}
						popup.setCaption("");
						popup.setWidth("75%");
						popup.setHeight("70%");
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
						Button okBtn = new Button("Close");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						okBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								List<OMPNewRecoverableTableDto> ompRecoverableTableList = ompNewRecoverableTableObj.getValues();
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_RECOVERY_FETCH,calculationViewTableDTO);
								if(calculationViewTableDTO.getOmpRecoverableTableList()==null && calculationViewTableDTO.getOmpRecoverableTableList().isEmpty()){
									ompRecoverableTableList = new ArrayList<OMPNewRecoverableTableDto>();
								}
								calculationViewTableDTO.setOmpRecoverableTableList(ompRecoverableTableList);
								popup.close();
							}
						});
						Button saveBtn = new Button("Save");
						saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						saveBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
//								dialog.close();
								//closePopup(popup);
								//popup.close();
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean);
								if(!validatePage(Boolean.TRUE)) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								List<OMPNewRecoverableTableDto> ompRecoverableTableList = ompNewRecoverableTableObj.getValues();
								calculationViewTableDTO.setOmpRecoverableTableList(ompRecoverableTableList);
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROSESS_RECOVERABLE_SAVE,calculationViewTableDTO);
								ompRecoverableTableList = ompNewRecoverableTableObj.getValues();
								calculationViewTableDTO.setOmpRecoverableTableList(ompRecoverableTableList);
								if(!calculationViewTableDTO.getIsReadOnlyRecoverable()){
									Double totalAmtDouble =0d;
									TextField deduction = (TextField)hashMap.get("currencyrate");
									if(deduction!=null){
										deduction.setReadOnly(Boolean.FALSE);
										String value3 = deduction.getValue();
										deduction.setValue(null);
										deduction.setValue(value3);
										deduction.setReadOnly(Boolean.TRUE);
									}
									/*if(approvedamountaftecopay!=null){
										totalAmtDouble = addRecovarableCopaySave(ompRecoverableTableList,totalAmtDouble,approvedamountaftecopay);
										approvedamountaftecopay.setReadOnly(Boolean.FALSE);
										approvedamountaftecopay.setValue(totalAmtDouble.toString());
										approvedamountaftecopay.setReadOnly(Boolean.TRUE);
									}*/
									
								}
								popup.close();
								}
							}


						});
						VerticalLayout vlayout = new VerticalLayout(ompNewRecoverableTableObj);
						HorizontalLayout hLayout = new HorizontalLayout(saveBtn,okBtn);
						hLayout.setSpacing(true);
						vlayout.addComponent(hLayout);
						vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
						popup.setContent(vlayout);
						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}}

				});
				recoverableButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	recoverableButton.setWidth("150px");
		    	recoverableButton.addStyleName(ValoTheme.BUTTON_LINK);
				return recoverableButton;
			}
		});
		table.setColumnHeader("recoverable", "");
//		table.setColumnHeader("paymentDetails", "Payment Details");
		table.removeGeneratedColumn("paymentDetails");
		table.addGeneratedColumn("paymentDetails", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				paymentDetails = new Button("Payment Details");
				paymentDetails.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						List<NomineeDetailsDto> nomineeDTOList = new ArrayList<NomineeDetailsDto>();
						final Window popup = new com.vaadin.ui.Window();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
						fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_PAYMENT_FETCH,calculationViewTableDTO);	
						List<OMPPaymentDetailsTableDTO> ompPaymentDetailsList = calculationViewTableDTO.getOmpPaymentDetailsList();
						ompNewPaymentDetailTableObj =  ompNewPaymentDetailTableInstance.get();
						ompNewPaymentDetailTableObj.init(calculationViewTableDTO);
						ompNewPaymentDetailTableObj.setCaption("Payment Details");
						if(ompPaymentDetailsList != null){
							ompNewPaymentDetailTableObj.setTableList(ompPaymentDetailsList);
						}
						if(bean.getPatientStatus() != null && bean.getPatientStatus().getValue() != null){
							if(bean.getPatientStatus().getValue().equals("Deceased")){
								nomineeDetailsTable.init("", false, false);
								List<PolicyNominee> nomineeList = ompClaimService.getPolicyInsuredNomineeList(bean.getNewIntimationDto().getInsuredPatient().getKey());
								
								if(nomineeList != null && !nomineeList.isEmpty()){
									BankMaster bankMaster=null;
									for(PolicyNominee rev : nomineeList){
										NomineeDetailsDto newRec = new NomineeDetailsDto(rev);
										if(newRec.getIfscCode() != null){
											
											bankMaster = masterService.getBankDetails(newRec.getIfscCode());
											if(bankMaster != null){
												newRec.setBankName(bankMaster.getBankName());
												newRec.setBankBranchName(bankMaster.getBranchName());
											}
										}
										nomineeDTOList.add(newRec);
									}
								}
								nomineeDetailsTable.setTableList(nomineeDTOList);
								nomineeDetailsTable.generateSelectColumn();
							}
						}
						
						popup.setWidth("75%");
						popup.setHeight("70%");
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
						Button okBtn = new Button("Close");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						okBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								List<OMPPaymentDetailsTableDTO> ompPaymentDetailsTableDTOs = ompNewPaymentDetailTableObj.getValues();
								ompPaymentDetailsTableDTOs = new ArrayList<OMPPaymentDetailsTableDTO>();
								calculationViewTableDTO.setOmpPaymentDetailsList(ompPaymentDetailsTableDTOs);
								popup.close();
							}
						});
						Button saveBtn = new Button("Save");
						saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						saveBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
//								dialog.close();
								//closePopup(popup);
								//popup.close();
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean);
								if(!validatePagepayment(Boolean.TRUE)) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								List<OMPPaymentDetailsTableDTO> ompPaymentDetailsTableDTOs = ompNewPaymentDetailTableObj.getValues();
								calculationViewTableDTO.setOmpPaymentDetailsList(ompPaymentDetailsTableDTOs);
								if(nomineeDetailsTable != null && bean.getPatientStatus() != null && bean.getPatientStatus().getValue() != null){
									if((bean.getPatientStatus().getValue().toString()).equals("Deceased")){
										List<NomineeDetailsDto> listOfNomieez = nomineeDetailsTable.getTableList();
										boolean isNomineeDetailsSelected = false;
										//if(listOfNomieez != null && !listOfNomieez.isEmpty()){
											for(NomineeDetailsDto rec : listOfNomieez){
												if(rec.getSelectedNomineeFlag().equals("Y")){
													calculationViewTableDTO.setNomineeName(rec.getNomineeName());
													isNomineeDetailsSelected = true;
													break;
												}else{
													continue;
												}
											}

											if(!isNomineeDetailsSelected){
												Map<String, String> nomineeDetails  = nomineeDetailsTable.getLegalHeirDetails();
												calculationViewTableDTO.setNomineeName(nomineeDetails.get("FNAME"));
												calculationViewTableDTO.setNomineeAddress(nomineeDetails.get("ADDR"));
											}
										//}
									}
								}
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_PAYMENT_SAVE,calculationViewTableDTO);
								ompPaymentDetailsTableDTOs = ompNewPaymentDetailTableObj.getValues();
								calculationViewTableDTO.setOmpPaymentDetailsList(ompPaymentDetailsTableDTOs);
								popup.close();
								}
							}

						});
						VerticalLayout vlayout = new VerticalLayout(ompNewPaymentDetailTableObj);
						if(bean.getPatientStatus() != null && bean.getPatientStatus().getValue() != null){
							vlayout.addComponent(nomineeDetailsTable);
							FormLayout legaHeirLayout = null;
							if(bean.getPatientStatus().getValue().equals("Deceased")){
								if(nomineeDTOList != null && nomineeDTOList.isEmpty()){
									legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(true);
									nomineeDetailsTable.enableLegalHeir(true);
									if(!StringUtils.isBlank(calculationViewTableDTO.getNomineeName())){
										nomineeDetailsTable.setLegalHeirDetails(calculationViewTableDTO.getNomineeName(),calculationViewTableDTO.getNomineeAddress());
										nomineeDetailsTable.enableLegalHeir(false);
									}
									vlayout.addComponent(legaHeirLayout);
								}else{
									legaHeirLayout = nomineeDetailsTable.getLegalHeirLayout(false);
									nomineeDetailsTable.enableLegalHeir(false);
									vlayout.addComponent(legaHeirLayout);
								}
								vlayout.setSpacing(true);
							}
						}
						HorizontalLayout hLayout = new HorizontalLayout(saveBtn,okBtn);
						hLayout.setSpacing(true);
						vlayout.addComponent(hLayout);
						vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
						popup.setContent(vlayout);
						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}
				
				});
				paymentDetails.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	paymentDetails.setWidth("150px");
		    	paymentDetails.addStyleName(ValoTheme.BUTTON_LINK);
				return paymentDetails;
			}
		});
		table.setColumnHeader("paymentDetails", "");
		
//		table.setColumnHeader("viewforApprover", "View for Approver");
		//table.setColumnHeader("viewforApprover", "View for Approver");
		
//		table.setColumnHeader("sendforApprover", "Send for Approver");
		table.removeGeneratedColumn("sendforApprover");
		table.addGeneratedColumn("sendforApprover", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
 				if(rodDTO != null){
 				
						final CheckBox sendForApprover = new CheckBox();
						if(rodDTO.getSendforApprover()!=null && rodDTO.getSendforApprover().equalsIgnoreCase("Y")){
							sendForApprover.setValue(Boolean.TRUE);
							Button button = tableItemBtn.get(rodDTO);
							if(button!=null){
								button.setEnabled(Boolean.TRUE);
							}
						}else{
							Button button = tableItemBtn.get(rodDTO);
							if(button!=null){
								button.setEnabled(Boolean.FALSE);
							}
						}
						if(rodDTO.getSubmit()!= null && rodDTO.getSubmit().equalsIgnoreCase("Y")){
								sendForApprover.setEnabled(Boolean.FALSE);
						}else{
								sendForApprover.setEnabled(Boolean.TRUE);
						}
						sendForApprover.addValueChangeListener(new Property.ValueChangeListener() {
 							
 							@Override
 							public void valueChange(ValueChangeEvent event) {
								OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
								if(rodDTO.getIsReadOnly()){
									if(rodDTO.getSendforApprover()!=null && rodDTO.getSendforApprover().equalsIgnoreCase("Y")){
										sendForApprover.setValue(Boolean.TRUE);
										Button button = tableItemBtn.get(rodDTO);
										if(button!=null){
											button.setEnabled(Boolean.TRUE);
										}
									}else{
										sendForApprover.setValue(Boolean.FALSE);
									}
								}else{
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(rodDTO);
								Boolean value = (Boolean) event.getProperty().getValue();
								//GComboBox viewforApprover = (GComboBox)hashMap.get("viewforApprover");
								if(rodDTO.getReject()!=null){
									if(rodDTO.getReject().equalsIgnoreCase("Y")){
										sendForApprover.setValue(null);
										rodDTO.setSendforApprover("N");
										Button button = tableItemBtn.get(rodDTO);
										if(button!=null){
											button.setEnabled(Boolean.FALSE);
										}
									}else{
										if(value){
											rodDTO.setSendforApprover("Y");
											Button button = tableItemBtn.get(rodDTO);
											if(button!=null){
												button.setEnabled(Boolean.TRUE);
											}
											if(!bean.getIsCashless() || bean.getNonHospitalisationFlag().equals("Y")){
												if(rodDTO.getDocRecivedFrm() != null && rodDTO.getDocRecivedFrm().getValue().equals("Insured")){
													rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
												}else{
													rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
												}
											}else{
												rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
											}
										}else{
											rodDTO.setSendforApprover("N");
											Button button = tableItemBtn.get(rodDTO);
											if(button!=null){
												button.setEnabled(Boolean.FALSE);
											}
											rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
										}
										fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_PROCESSOR_REMARKS, rodDTO);
									}
								}else{
									if(value){
										rodDTO.setSendforApprover("Y");
										Button button = tableItemBtn.get(rodDTO);
										if(button!=null){
											button.setEnabled(Boolean.TRUE);
										}
										
										if(!bean.getIsCashless() || bean.getNonHospitalisationFlag().equals("Y")){
											if(rodDTO.getDocRecivedFrm() != null && rodDTO.getDocRecivedFrm().getValue().equals("Insured")){
												rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
											}else{
												rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
											}
										}else{
											rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
										}
									}else{
										rodDTO.setSendforApprover("N");
										Button button = tableItemBtn.get(rodDTO);
										if(button!=null){
											button.setEnabled(Boolean.FALSE);
										}
										if(rodDTO.getDisVoucFlag()!= null && rodDTO.getDisVoucFlag().equals("Y")){
											rodDTO.getChkBoxDicharge().setValue(null);
										}
										rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
									}
									fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_PROCESSOR_REMARKS, rodDTO);
								}
								
							}
 							}});
						rodDTO.setApproverApprvChekBox(sendForApprover);
						return sendForApprover;
				}else {
					return null;
				}
		}
		});
		
		table.setColumnHeader("sendforApprover", "Approve");
//		table.setColumnHeader("reject", "Reject");
		table.removeGeneratedColumn("reject");
		table.addGeneratedColumn("reject", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
				if(rodDTO!= null){
				
						final CheckBox chkBox = new CheckBox();
						if(rodDTO.getIsreject()!=null){
							chkBox.setValue(rodDTO.getIsreject());
						}
						if(rodDTO.getReject()!= null && rodDTO.getReject().equalsIgnoreCase("Y")){
							chkBox.setEnabled(Boolean.FALSE);
						}
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								
								Boolean value = (Boolean) event.getProperty().getValue();
								OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
								if(rodDTO.getIsReadOnly()){
									chkBox.setValue(null);
								}
								else if(value != null){
								   if(value){
									   if(rodDTO.getSendforApprover()!=null){
										   if(rodDTO.getSendforApprover().equalsIgnoreCase("Y")){
											   chkBox.setValue(null);
										   }else{
											   rodDTO.setReject("Y");
											   if(rejectDetailsBut != null){
												   rejectDetailsBut.setEnabled(Boolean.TRUE);
											   }
//											   fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_REJECTION_REMARKS, rodDTO);
										   }
									   }else{
										   rodDTO.setReject("Y");
										   if(rejectDetailsBut != null){
											   rejectDetailsBut.setEnabled(Boolean.TRUE);
										   }
//										   fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_REJECTION_REMARKS, rodDTO);
									   }
									   
								   }else{
									   chkBox.setValue(false);
//									   rodDTO.setCloseClaimStatus(false);
									   if(rejectDetailsBut != null){
										   rejectDetailsBut.setEnabled(Boolean.FALSE);
									   }
									   rodDTO.setReject("N");
//									   fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_PROCESS_SHOW_REJECTION_REMARKS, rodDTO);
//									   rodDTO.setIsSendFrApprover(false);
								   }
								}
								
							}
						});
						rodDTO.setRejectChkBox(chkBox);
						return chkBox;
				}else {
					return null;
				}
		}
		});
		table.setColumnHeader("reject", "Reject");
		
		//CR20181332 - Start
				/*table.removeGeneratedColumn("reconsiderFlag");
				table.addGeneratedColumn("reconsiderFlag", new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source, final Object itemId, Object columnId) {
						OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
						if(rodDTO!= null){
							reConsiderChkBox = new CheckBox();
								if(rodDTO.getReconsiderFlag() != null){
									if(rodDTO.getReconsiderFlag().equals("Y")){
										reConsiderChkBox.setValue(Boolean.TRUE);
									}else{
										reConsiderChkBox.setValue(Boolean.FALSE);
									}
								}else{
									reConsiderChkBox.setValue(Boolean.FALSE);
								}
								reConsiderChkBox.addValueChangeListener(new Property.ValueChangeListener() {
									@Override
									public void valueChange(ValueChangeEvent event) {
										Boolean value = (Boolean) event.getProperty().getValue();
										OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
										if(value != null){
										   if(value){
//											   reConsiderChkBox.setValue(true);
//											   rodDTO.getReConsiderChkBox().setValue(true);
											   rodDTO.setReconsiderFlag("Y");
											   if(OMPStatus.contains(rodDTO.getProcessorStatus())){
												   rodDTO.setIsReadOnly(false);
												   rodDTO.setIsReadOnlyRecoverable(false);
												   isReconsidervalueChanged = true;
												   chkReconsidervalue = true;
												   refreshTable();
												   
												   if(rodDTO.getRejectChkBox() != null){
													   rodDTO.getRejectChkBox().setEnabled(Boolean.TRUE);
													   rodDTO.getRejectChkBox().setValue(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbNegotiationDone() != null){
													   rodDTO.getCmbNegotiationDone().setReadOnly(Boolean.FALSE);
													   rodDTO.getCmbNegotiationDone().setEnabled(Boolean.TRUE);
													   rodDTO.getCmbNegotiationDone().setValue(null);
												   }else{
													   rodDTO.getCmbNegotiationDone().setReadOnly(Boolean.FALSE);
												   }
												   
												   if(rodDTO.getDisVoucFlag() != null){
													   if(rodDTO.getDisVoucFlag().equals("Y")){
														   rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
														   rodDTO.getChkBoxDicharge().setValue(null);
													   }else{
														   rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
													   }
												   }
												   
												   if(rodDTO.getApproverApprvChekBox() != null){
													   rodDTO.getApproverApprvChekBox().setEnabled(Boolean.TRUE);
													   rodDTO.getApproverApprvChekBox().setValue(null);
												   }
												   
												   if(rodDTO.getNotInClaimCountChkBox() != null){
													   rodDTO.getNotInClaimCountChkBox().setEnabled(Boolean.TRUE);
													   rodDTO.getNotInClaimCountChkBox().setValue(null);
												   }
												   
												   if(rodDTO.getCmbClassification() != null){
													   rodDTO.getCmbClassification().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbSubClassification() != null){
													   rodDTO.getCmbSubClassification().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbRODClaimType() != null){
													   rodDTO.getCmbRODClaimType().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbDocReceivFrm() != null){
													   rodDTO.getCmbDocReceivFrm().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbCategory() != null){
													   rodDTO.getCmbCategory().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getCmbCurrencyType() != null){
													   rodDTO.getCmbCurrencyType().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getTxtConversionVal() != null){
													   rodDTO.getTxtConversionVal().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getTxtBillAmt() != null){
													   rodDTO.getTxtBillAmt().setReadOnly(Boolean.FALSE);
												   }
												   if(rodDTO.getTxtDeduction() != null){
													   rodDTO.getTxtDeduction().setReadOnly(Boolean.FALSE);
												   }
												   
											   }
										   }else{
//											   reConsiderChkBox.setValue(false);
//											   rodDTO.getReConsiderChkBox().setValue(false);
											   rodDTO.setReconsiderFlag("N");
											   if(OMPStatus.contains(rodDTO.getProcessorStatus())){
												   rodDTO.setIsReadOnly(true);
												   rodDTO.setIsReadOnlyRecoverable(true);
												   isReconsidervalueChanged = true;
												   chkReconsidervalue = false;
												   refreshTable();
												   
												   if(rodDTO.getRejectChkBox() != null){
													   rodDTO.getRejectChkBox().setEnabled(Boolean.FALSE);
												   }
												   
												   if(rodDTO.getCmbNegotiationDone() != null){
													   rodDTO.getCmbNegotiationDone().setReadOnly(Boolean.TRUE);
													   rodDTO.getCmbNegotiationDone().setEnabled(Boolean.FALSE);
												   }
												   
												   if(rodDTO.getApproverApprvChekBox() != null){
													   rodDTO.getApproverApprvChekBox().setEnabled(Boolean.FALSE);
												   }
												   if(rodDTO.getNotInClaimCountChkBox() != null){
													   rodDTO.getNotInClaimCountChkBox().setEnabled(Boolean.FALSE);
												   }
												   
												   if(rodDTO.getCmbClassification() != null){
													   rodDTO.getCmbClassification().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getCmbSubClassification() != null){
													   rodDTO.getCmbSubClassification().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getCmbRODClaimType() != null){
													   rodDTO.getCmbRODClaimType().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getCmbDocReceivFrm() != null){
													   rodDTO.getCmbDocReceivFrm().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getCmbCategory() != null){
													   rodDTO.getCmbCategory().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getCmbCurrencyType() != null){
													   rodDTO.getCmbCurrencyType().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getTxtConversionVal() != null){
													   rodDTO.getTxtConversionVal().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getTxtBillAmt() != null){
													   rodDTO.getTxtBillAmt().setReadOnly(Boolean.TRUE);
												   }
												   if(rodDTO.getTxtDeduction() != null){
													   rodDTO.getTxtDeduction().setReadOnly(Boolean.TRUE);
												   }
											   }
										   }
										}
									}
								});
								
								if(rodDTO.getProcessorStatus().intValue() == 3182 && rodDTO.getRodPaymentStatus().intValue() == 158){
									if(isReconsidervalueChanged){
										reConsiderChkBox.setValue(chkReconsidervalue);
										isReconsidervalueChanged = false;
									}
									if(chkReconsidervalue){
										reConsiderChkBox.setEnabled(Boolean.TRUE);
									}else{
										reConsiderChkBox.setEnabled(Boolean.FALSE);
									}
								}else{
									if(OMPStatus.contains(rodDTO.getProcessorStatus())){
										if(isReconsidervalueChanged){
											reConsiderChkBox.setValue(chkReconsidervalue);
											isReconsidervalueChanged = false;
										}
										reConsiderChkBox.setEnabled(Boolean.TRUE);
									}else{
										if(isReconsidervalueChanged){
											reConsiderChkBox.setValue(chkReconsidervalue);
											isReconsidervalueChanged = false;
										}
										reConsiderChkBox.setEnabled(Boolean.FALSE);
									}
								//}
								
								if(OMPStatus.contains(rodDTO.getProcessorStatus())){
									if(rodDTO.getReconsiderFlag() != null){
										if(rodDTO.getReconsiderFlag().equals("Y")){
											reConsiderChkBox.setValue(Boolean.TRUE);
										}
										if(rodDTO.getReconsiderFlag().equals("N")){
											reConsiderChkBox.setValue(Boolean.FALSE);
										}
									}else{
										reConsiderChkBox.setValue(Boolean.FALSE);
									}
									reConsiderChkBox.setEnabled(Boolean.TRUE);
								}else{
									reConsiderChkBox.setValue(Boolean.FALSE);
									reConsiderChkBox.setEnabled(Boolean.FALSE);
								}
									
								rodDTO.setReConsiderChkBox(reConsiderChkBox);
								return reConsiderChkBox;
						}else {
							return null;
						}
				
					}
				});
				table.setColumnHeader("reconsiderFlag", "Re-Consider");*/
				
				table.removeGeneratedColumn("rejectDetails");
				table.addGeneratedColumn("rejectDetails", new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
						rejectDetailsBut = new Button("Reject Details");
						rejectDetailsBut.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								
								if(bean.getEventCode() != null){
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Rejection Details");
									popup.setWidth("50%");
									popup.setHeight("45%");
									popup.center();
									popup.setClosable(true);
									popup.setResizable(true);
									rejectionPage.init(rodDTO, bean);
									rejectionPage.setPopupWindow(popup);
									popup.setContent(rejectionPage);
									/*	
								PreauthDTO bean = new PreauthDTO();
								ViewDocumentDetailsDTO tableDto = (ViewDocumentDetailsDTO)itemId;
								bean.setKey(tableDto.getReimbursementKey());
//								uploadDocumentViewImpl.init(bean,popup);
								popup.setCaption("Billing Worksheet");
								popup.setWidth("75%");
								popup.setHeight("90%");
								popup.setClosable(true);
//								popup.setContent(uploadDocumentViewImpl);
								popup.center();
								popup.setResizable(true);*/
									popup.addCloseListener(new Window.CloseListener() {
										private static final long serialVersionUID = 1L;
										@Override
										public void windowClose(CloseEvent e) {
											System.out.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}else{
									showErrorPopup("Please select the event code in Loss Details.");
								}
							}

						});
						rejectDetailsBut.setEnabled(Boolean.FALSE);
						rejectDetailsBut.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						rejectDetailsBut.setWidth("150px");
						rejectDetailsBut.addStyleName(ValoTheme.BUTTON_LINK);
						if(rodDTO.getReject() != null && rodDTO.getIsreject()){
							if(rodDTO.getReject().equalsIgnoreCase("Y")){
								rejectDetailsBut.setEnabled(Boolean.TRUE);
							}else{
								rejectDetailsBut.setEnabled(Boolean.FALSE);
							}
						}else{
							rejectDetailsBut.setEnabled(Boolean.FALSE);
						}
						return rejectDetailsBut;
					}
				});
				table.setColumnHeader("rejectDetails", "Rejection Reason");
				
				table.removeGeneratedColumn("disVoucFlag");
				table.addGeneratedColumn("disVoucFlag", new Table.ColumnGenerator() {
					@Override
					public Object generateCell(Table source, final Object itemId, Object columnId) {
						OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
						if(rodDTO!= null){
								dichargeChkBox = new CheckBox();
								rodDTO.setChkBoxDicharge(dichargeChkBox);
								if(rodDTO.getDisVoucFlag() != null){
									if(rodDTO.getDisVoucFlag().equals("Y")){
										rodDTO.getChkBoxDicharge().setValue(Boolean.TRUE);
									}else{
										rodDTO.getChkBoxDicharge().setValue(Boolean.FALSE);
									}
								}
								dichargeChkBox.addValueChangeListener(new Property.ValueChangeListener() {
									@Override
									public void valueChange(ValueChangeEvent event) {
										Boolean value = (Boolean) event.getProperty().getValue();
										OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
										if(value != null){
										   if(value){
											   rodDTO.setDisVoucFlag("Y");
											   rodDTO.getDisChrgDetailsBut().setEnabled(Boolean.TRUE);
										   }else{
											   rodDTO.setDisVoucFlag("N");
											   rodDTO.getDisChrgDetailsBut().setEnabled(Boolean.FALSE);
										   }
										}
									}
								});
								if(bean.getNonHospitalisationFlag().equals("Y") && !bean.getIsCashless()){
									if(rodDTO.getSendforApprover() != null){
										if(rodDTO.getSendforApprover().equalsIgnoreCase("Y")){
											rodDTO.getChkBoxDicharge().setEnabled(Boolean.TRUE);
										}else{
											rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
										}
									}else{
										rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
									}
								}else{
									// For Cashless and Medical Case's we are disabling the discharge voucher option....
									rodDTO.getChkBoxDicharge().setEnabled(Boolean.FALSE);
									if(rodDTO.getDisChrgDetailsBut() != null){
										rodDTO.getDisChrgDetailsBut().setEnabled(Boolean.FALSE);
									}
								}
								rodDTO.setChkBoxDicharge(dichargeChkBox);
								return dichargeChkBox;
						}else {
							return null;
						}
					}
				});
				table.setColumnHeader("disVoucFlag", "Discharge");
				
				
				table.removeGeneratedColumn("dischargeDetails");
				table.addGeneratedColumn("dischargeDetails", new Table.ColumnGenerator() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
						disChDetailsBut = new Button("Discharge Details");
						disChDetailsBut.addClickListener(new Button.ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								
								if(bean.getEventCode() != null){
									Window popup = new com.vaadin.ui.Window();
									popup.setCaption("Discharge Details");
									popup.setWidth("50%");
									popup.setHeight("45%");
									popup.center();
									popup.setClosable(true);
									popup.setResizable(true);
									fireViewEvent(OMPClaimProcessorPagePresenter.OMP_GET_OMBUDSMAN_DETAILS, bean);
									disChargePage.init(rodDTO, bean);
									disChargePage.setPopupWindow(popup);
									popup.setContent(disChargePage);
									popup.addCloseListener(new Window.CloseListener() {
										private static final long serialVersionUID = 1L;
										@Override
										public void windowClose(CloseEvent e) {
											System.out.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}else{
									showErrorPopup("Please select the event code in Loss Details.");
								}
							}

						});
						disChDetailsBut.setEnabled(Boolean.TRUE);
						disChDetailsBut.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						disChDetailsBut.setWidth("150px");
						disChDetailsBut.addStyleName(ValoTheme.BUTTON_LINK);
						if(rodDTO.getDisVoucFlag() != null){
							if(rodDTO.getDisVoucFlag().equals("Y")){
								disChDetailsBut.setEnabled(Boolean.TRUE);
							}else{
								disChDetailsBut.setEnabled(Boolean.FALSE);
							}
						}else{
							disChDetailsBut.setEnabled(Boolean.FALSE);
						}
						rodDTO.setDisChrgDetailsBut(disChDetailsBut);
						return disChDetailsBut;
					}
				});
				table.setColumnHeader("dischargeDetails", "Discharge Details");
				
				//CR20181332 - End
				
//		table.setColumnHeader("notinClaimCount", "Not in Claim Count");
		table.removeGeneratedColumn("notinClaimCount");
		table.addGeneratedColumn("notinClaimCount", new Table.ColumnGenerator() {
			
			//CheckBox chkbox = new CheckBox();
			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
				if(rodDTO != null){
				
					final CheckBox chkBox = new CheckBox();
					chkBox.setData(rodDTO);
					tableItemchk.put(rodDTO, chkBox);
					if(rodDTO.getNotinClaimCount()!=null && rodDTO.getNotinClaimCount().equalsIgnoreCase("Y")){
						chkBox.setValue(Boolean.TRUE);
						SelectValue selectValue=rodDTO.getClassification();
						if(selectValue!=null && !selectValue.getValue().equals("OMP Claim Related")){
							chkBox.setEnabled(Boolean.FALSE);
						}
					}else{
							chkBox.setValue(Boolean.FALSE);
					}
					 if(rodDTO.getIsReadOnly()){
						 chkBox.setEnabled(Boolean.FALSE);
					 }
					
						/*if(rodDTO.getSendforApprover()!= null && rodDTO.getSendforApprover().equalsIgnoreCase("Y") ||rodDTO.getReject()!= null && rodDTO.getReject().equalsIgnoreCase("Y")){
							chkBox.setEnabled(Boolean.FALSE);
						}*/
						chkBox.addValueChangeListener(new Property.ValueChangeListener() {
							
							@Override
							public void valueChange(ValueChangeEvent event) {
								CheckBox property = (CheckBox) event.getProperty();
								Boolean value = (Boolean) event.getProperty().getValue();
								if(value != null){
									OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								   if(!calculationViewTableDTO.getIsReadOnly()){
										HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
										GComboBox category = (GComboBox)hashMap.get("category");
										if(category!=null & category.getValue()!=null){
											SelectValue value2 = (SelectValue) category.getValue();
											if(value2!=null && value2.getValue()!=null && value2.getValue().equalsIgnoreCase(SHAConstants.OMP_CATEGORY_MAIN_BILL)){
												if(value){
													showErrorPopup("Not in claim count is not allowed for main category");
												}
												 chkBox.setValue(false);
												 calculationViewTableDTO.setIsnotInClaimCount(false);
												 calculationViewTableDTO.setNotinClaimCount("N");
											}else{
												calculationViewTableDTO.setIsnotInClaimCount(value);
												calculationViewTableDTO.setNotinClaimCount("Y");
											}
										}else{
											calculationViewTableDTO.setIsnotInClaimCount(value);
											calculationViewTableDTO.setNotinClaimCount("Y");
										}
								   }/*else{
									   if(calculationViewTableDTO.getNotinClaimCount()!= null && calculationViewTableDTO.getNotinClaimCount().equals("Y")){
										   	 chkBox.setValue(true);
											 calculationViewTableDTO.setIsnotInClaimCount(true);
											 calculationViewTableDTO.setNotinClaimCount("Y");
									   }else if (calculationViewTableDTO.getNotinClaimCount()!= null && calculationViewTableDTO.getNotinClaimCount().equals("N")){
										     chkBox.setValue(false);
											 calculationViewTableDTO.setIsnotInClaimCount(false);
											 calculationViewTableDTO.setNotinClaimCount("N");
									   }
										  
									  // rodDTO.setIsnotInClaimCount(false);
									   //rodDTO.setNotinClaimCount("N");
								   }*/
								}
								
							}
						});
						rodDTO.setNotInClaimCountChkBox(chkBox);
						return chkBox;
				}else {
					return null;
				}
		}
		});
		table.setColumnHeader("notinClaimCount", "Not In Claim Count");
//		table.setColumnHeader("Submit", "Submit");
		table.removeGeneratedColumn("Submit");
		table.addGeneratedColumn("submit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				Button submitForFa = new Button("Submit For FA");
				OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
				submitForFa.setData(calculationViewTableDTO);
				tableItemBtn.put(calculationViewTableDTO, submitForFa);
				submitForFa.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						final Window popup = new com.vaadin.ui.Window();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
						popup.setWidth("25%");
						popup.setHeight("25%");
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
						Button okBtn = new Button("Close");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						okBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								popup.close();
							}
						});
						Button saveBtn = new Button("Save");
						saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						saveBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								
								OMPClaimCalculationViewTableDTO rodDTO = (OMPClaimCalculationViewTableDTO) itemId;
								rodDTO.setSubmit("Y");
								//bean.setRodNumber(rodDTO.getRodnumber());
								//fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_FA_SUBMIT, bean,rodDTO);
								popup.close();
							}
							
						});
						VerticalLayout vlayout = new VerticalLayout();
						HorizontalLayout vlayout1 = new HorizontalLayout(new Label("Are you sure you want to submit the Bill for Payment"));
						HorizontalLayout hLayout = new HorizontalLayout(saveBtn,okBtn);
						hLayout.setSpacing(true);
						vlayout1.setSpacing(true);
						vlayout1.setCaption("");
						vlayout.addComponent(vlayout1);
						vlayout.setComponentAlignment(vlayout1, Alignment.BOTTOM_CENTER);
						vlayout.addComponent(hLayout);
						vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
//						vlayout.setComponentAlignment(saveBtn, Alignment.BOTTOM_CENTER);
						popup.setContent(vlayout);
						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}

				});
				if(calculationViewTableDTO.getSendforApprover()!=null && calculationViewTableDTO.getSendforApprover().equalsIgnoreCase("Y")){
					submitForFa.setEnabled(Boolean.TRUE);
				}else{
					submitForFa.setEnabled(Boolean.FALSE);
				}
				if(calculationViewTableDTO.getSubmit()!= null && calculationViewTableDTO.getSubmit().equalsIgnoreCase("Y")){
					submitForFa.setEnabled(Boolean.FALSE);
				}/*else{
					submitForFa.setEnabled(Boolean.TRUE);
				}*/
				submitForFa.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				submitForFa.setWidth("150px");
				submitForFa.addStyleName(ValoTheme.BUTTON_LINK);
				return submitForFa;
			}
		});
		table.setColumnHeader("submit", "Submit");
		table.setEditable(true);
		table.setFooterVisible(true);
		/*table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO =  (OMPClaimCalculationViewTableDTO) currentItemId;
						deletedList.add(calculationViewTableDTO);
						table.removeItem(currentItemId);
//						setFooter();
					}
					
				});

				return deleteButton;
			}
		});*/
		/*Object currentItemId = table.getItemIds();
		OMPClaimCalculationViewTableDTO calculationViewTableDTO =  (OMPClaimCalculationViewTableDTO) currentItemId;
		deletedList.add(calculationViewTableDTO);*/
		List<OMPClaimCalculationViewTableDTO> itemIds =  (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
		bean.setClaimCalculationViewTable(itemIds);
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}

	private void addNegotiationDetail() {
		table.addGeneratedColumn("negotiationDetails", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(Table source, final Object itemId,
					Object columnId) {
				Button negotitiationDetailButton = new Button("Negotiation Details");
				negotitiationDetailButton.addClickListener(new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						final Window popup = new com.vaadin.ui.Window();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
						HashMap<String, AbstractField<?>> hashMap2 = tableItem.get(calculationViewTableDTO);
						GComboBox negotationEnable = (GComboBox)hashMap2.get("classification");
						GComboBox negotiationDone = (GComboBox)hashMap2.get("negotiationDone");
						GComboBox docRecivedFrm = (GComboBox)hashMap2.get("docRecivedFrm");
						if(negotationEnable!=null && negotationEnable.getValue()!=null && negotiationDone!=null && negotiationDone.getValue()!=null 
								&& docRecivedFrm!=null && docRecivedFrm.getValue()!=null){
						SelectValue value = (SelectValue) negotationEnable.getValue();
						SelectValue value2 = (SelectValue) negotiationDone.getValue();
						SelectValue value3 = (SelectValue) docRecivedFrm.getValue();
						if(value!=null && value.getValue().equalsIgnoreCase("OMP Claim Related") 
								&& value2!=null && value2.getValue().equalsIgnoreCase("Yes")
								&& bean.getIsCashless()!= null && bean.getIsCashless().equals(Boolean.TRUE)
								){
									
						ompProcessNegotiationTableObj =  ompProcessNegotiationTableInstance.get();
						//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_FETCH, calculationViewTableDTO);
						List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = calculationViewTableDTO.getNegotiationDetailsDTOs();
						ompProcessNegotiationTableObj.init(calculationViewTableDTO);
						ompProcessNegotiationTableObj.setCaption("Negotiation Details");
						if(negotiationDetailsDTOs != null && negotiationDetailsDTOs.size() >0){
							ompProcessNegotiationTableObj.setTableList(negotiationDetailsDTOs);
						}
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
						TextField approvedamountaftecopay = (TextField)hashMap.get("approvedamountaftecopay");
						if(approvedamountaftecopay!=null && approvedamountaftecopay.getValue()!=null){
							Double approvedAmtDouble = SHAUtils.getDoubleFromStringWithComma(approvedamountaftecopay.getValue());
							ompProcessNegotiationTableObj.approvedAmt=approvedAmtDouble;
						}
//						popup.setCaption("Billing Worksheet");
						popup.setWidth("75%");
						popup.setHeight("90%");
						popup.setClosable(true);
//						popup.setContent(ompProcessNegotiationTableObj);
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
						Button okBtn = new Button("Close");
						okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						okBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								/*List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
								calculationViewTableDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);*/
								popup.close();
							}
						});
						Button saveBtn = new Button("Save");
						saveBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
						saveBtn.addClickListener(new Button.ClickListener() {
							
							@Override
							public void buttonClick(ClickEvent event) {
//								dialog.close();
								//closePopup(popup);
								//popup.close();
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean);
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO) itemId;
								List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = ompProcessNegotiationTableObj.getValues();
								calculationViewTableDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean,calculationViewTableDTO);
								negotiationDetailsDTOs = ompProcessNegotiationTableObj.getValues();
								calculationViewTableDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
								TextField afternegotiation = (TextField)hashMap.get("afternegotiation");
								if(afternegotiation!=null && calculationViewTableDTO.getAfternegotiation()!=null){
									afternegotiation.setReadOnly(Boolean.FALSE);
									afternegotiation.setValue(calculationViewTableDTO.getAfternegotiation().toString());
									afternegotiation.setReadOnly(Boolean.TRUE);
								}
								
								calculationViewTableDTO.setClaimkey(bean.getClaimDto().getKey());
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_CLAIM_PROCESSOR_FETCH_NEGO, calculationViewTableDTO);
								//List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
								/*for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : dtoList) {
									SelectValue classification2 = ompClaimCalculationViewTableDTO.getClassification();
									if(classification2!=null && classification2.getValue().equalsIgnoreCase("Negotiator Fee")){
									if(!ompClaimCalculationViewTableDTO.getRodKey().equals(calculationViewTableDTO.getRodKey())){
										HashMap<String, AbstractField<?>> hashMapSelect = tableItem.get(ompClaimCalculationViewTableDTO);
										GComboBox select = (GComboBox)hashMapSelect.get("select");
										select.setReadOnly(Boolean.FALSE);
										select.setContainerDataSource(null);
										select.setContainerDataSource(calculationViewTableDTO.getSelectNegoContainer());
										select.setItemCaptionMode(ItemCaptionMode.PROPERTY);
										select.setItemCaptionPropertyId("value");
										select.setValue(ompClaimCalculationViewTableDTO.getSelect());
										select.setReadOnly(ompClaimCalculationViewTableDTO.getIsReadOnly());
									}}
								}*/
								
								popup.close();
							}

						});

						VerticalLayout vlayout = new VerticalLayout(ompProcessNegotiationTableObj);
						HorizontalLayout hLayout = new HorizontalLayout(saveBtn,okBtn);
						hLayout.setSpacing(true);
						vlayout.addComponent(hLayout);
						vlayout.setComponentAlignment(hLayout, Alignment.BOTTOM_CENTER);
//						vlayout.setComponentAlignment(saveBtn, Alignment.BOTTOM_CENTER);
						popup.setContent(vlayout);
						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
					}}}

				});
				negotitiationDetailButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				negotitiationDetailButton.setWidth("150px");
				negotitiationDetailButton.addStyleName(ValoTheme.BUTTON_LINK);
				return negotitiationDetailButton;
			}
		});
		table.setColumnHeader("negotiationDetails", "");
	}
	private void closePopup(final Window popup) {
		
		Label successLabel = new Label("<b style = 'color: green;'>Record saved successfully !!!</b>", ContentMode.HTML);
		
		Button homeButton = new Button("Claim Processor Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				popup.close();
//				fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_PROCESSOR, null);
				
			}
		});
	}
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			OMPClaimCalculationViewTableDTO entryDTO = (OMPClaimCalculationViewTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			}else {
				tableRow = tableItem.get(entryDTO);
			}
			
			if ("acknumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(Boolean.TRUE);
				
				field.addValueChangeListener(rodListener());
				tableRow.put("acknumber", field);
				return field;
			}
			else if ("rodnumber".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(Boolean.TRUE);
				
				field.addValueChangeListener(rodListener());
				tableRow.put("rodnumber", field);
				return field;
			}
			
			else if ("classification".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getClassificationContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				bean.setIsOnLoad(Boolean.TRUE);
				field.addValueChangeListener(addListenerForClassification());
				field.setValue(entryDTO.getClassification());
				bean.setIsOnLoad(Boolean.FALSE);
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbClassification(field);
				tableRow.put("classification", field);
				
				return field;
			}
			
			else if ("subClassification".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getSubClassificationContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbSubClassification(field);
				tableRow.put("subClassification", field);

				return field;
			}else if ("rodClaimType".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getRodClaimTypeContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(rodClaimListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbRODClaimType(field);
				tableRow.put("rodClaimType", field);

				return field;
			}
			else if ("docRecivedFrm".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getDocumentRecievedFromContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(documentRevListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbDocReceivFrm(field);
				tableRow.put("docRecivedFrm", field);

				return field;
			}else if("lateDocReceivedDate".equals(propertyId)){
				DateField field = new DateField();
				field.setWidth("250px");
				field.setData(entryDTO);
				field.setDateFormat("dd-MM-yyyy");
				field.addValueChangeListener(lastDocGenDateListener());
				field.setValue(entryDTO.getLateDocReceivedDate());
				Date currentDate = new Date();
				field.setRangeEnd(currentDate);
				field.setReadOnly(entryDTO.getIsReadOnly());
				tableRow.put("lateDocReceivedDate", field);
				return field;
			}
			else if ("category".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				addValuesForValueDropDown(field);
				field.setValue(entryDTO.getCategory());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(categoryListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbCategory(field);
				tableRow.put("category", field);

				return field;
			}
			else if ("currencyType".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				field.setContainerDataSource(bean.getCurrencyValueContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(currencyTypeListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbCurrencyType(field);
				tableRow.put("currencyType", field);

				return field;
			}
			
			else if ("currencyrate".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(currencyRateListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				tableRow.put("currencyrate", field);

				return field;
			}
			else if ("conversionValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.addValueChangeListener(currencyRateListener());
				field.addValueChangeListener(conversionListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setTxtConversionVal(field);
				tableRow.put("conversionValue", field);

				return field;
			}
			else if ("billAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(billAmtListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setTxtBillAmt(field);
				tableRow.put("billAmt", field);

				return field;
			}else if ("amtIn".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				onlyNumber(field);
				field.addValueChangeListener(amtInDollorListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("amtIn", field);

				return field;
			}
			else if ("deduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				onlyNumber(field);
				field.addValueChangeListener(deductionListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("deduction", field);
				entryDTO.setTxtDeduction(field);
				field.setReadOnly(entryDTO.getIsReadOnly());
				return field;
			}else if ("totalAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("totalAmt", field);

				return field;
			}
			
			else if ("copay".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
//				addValuesForValueDropDown(field);
				field.setContainerDataSource(copayContainer);
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.setValue(entryDTO.getCopay());
				field.addValueChangeListener(copayPercentageListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setEnabled(bean.getCopayEnable());
				field.setReadOnly(entryDTO.getIsReadOnly());
				tableRow.put("copay", field);

				return field;
			}
			else if ("copayamount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("copayamount", field);

				return field;
			}
			else if ("approvedamountaftecopay".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(copayApprovedAmtListener());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("approvedamountaftecopay", field);

				return field;
			}
			else if ("afternegotiation".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(afterNegotiationAmtListener());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("afternegotiation", field);

				return field;
			}
			else if ("finalApprovedAmtDollor".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				field.addValueChangeListener(finalApprovedAmtDollorListener());
				tableRow.put("finalApprovedAmtDollor", field);
				return field;
			}
			else if ("finalApprovedAmtInr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				field.setData(entryDTO);
				onlyNumber(field);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("finalApprovedAmtInr", field);
				return field;
			}
			else if ("totalAmtInr".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("250px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				onlyNumber(field);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.addValueChangeListener(totalAmtInrListener());
				field.setReadOnly(entryDTO.getIsReadOnly());
				tableRow.put("totalAmtInr", field);

				return field;
			}
			else if ("select".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
//				addValuesForValueDropDown(field);
				entryDTO.setClaimkey(bean.getClaimDto().getKey());
				addValuesForValueSelect(field,entryDTO);
				field.setContainerDataSource(entryDTO.getSelectNegoContainer());
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				field.addValueChangeListener(selectListener());
				field.setValue(null);
				field.setValue(entryDTO.getSelect());
				field.setReadOnly(entryDTO.getIsReadOnly());
				tableRow.put("select", field);

				return field;
			}
			else if ("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				onlyNumber(field);
				field.setData(entryDTO);
				//field.addValueChangeListener(approvedListener());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("agreedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				//field.addValueChangeListener(agreedListener());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("agreedAmt", field);
				return field;
			}
			else if("differenceAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				//field.addValueChangeListener(differenceListener());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("differenceAmt", field);
				return field;
			}
			else if("expenses".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				CSValidator intialProamtTxtValidator = new CSValidator();
				intialProamtTxtValidator.extend(field);
				intialProamtTxtValidator.setRegExp("^[0-9.]*$");
				intialProamtTxtValidator.setPreventInvalidTyping(true);
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				//field.addValueChangeListener(totalpayableListener());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("expenses", field);
				return field;
			}
			else if("handlingCharges".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				onlyNumber(field);
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.addValueChangeListener(addListenerForHandlingChanrgs());
				field.setReadOnly(Boolean.TRUE);
				tableRow.put("handlingCharges", field);
				return field;
			}			
			else if("totalExp".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				CSValidator intialProamtTxtValidator = new CSValidator();
				intialProamtTxtValidator.extend(field);
				intialProamtTxtValidator.setRegExp("^[0-9.]*$");
				intialProamtTxtValidator.setPreventInvalidTyping(true);
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				tableRow.put("totalExp", field);
				field.setReadOnly(Boolean.TRUE);
				//field.addValueChangeListener(totalAmtInrListener());
//				generateSlNo(field);
				return field;
			}
			else if ("negotiationDone".equals(propertyId)) {
				GComboBox field = new GComboBox();
				field.setWidth("250px");
				//field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(entryDTO);
//				addValuesForValueDropDown(field);
				field.setContainerDataSource(yesOrNoContainer);
				field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				field.setItemCaptionPropertyId("value");
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				//field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.addValueChangeListener(negoDoneListener());
				tableRow.put("negotiationDone", field);
				doclasssificationValidation(field);
				bean.setIsOnLoad(Boolean.TRUE);
				doReadOnly(entryDTO.getClassification(), entryDTO);
				bean.setIsOnLoad(Boolean.FALSE);
				field.setReadOnly(entryDTO.getIsReadOnly());
				entryDTO.setCmbNegotiationDone(field);
				return field;
			}
/*			else if("negotiationDetails".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
				field.setValue(SHAConstants.OMP_NEGO_CAPPING);
				
				tableRow.put("negotiationDetails", field);
				return field;
			}else if("uploadDocuments".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("uploadDocuments", field);
				return field;
			}
			else if("recoverable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("recoverable", field);
				return field;
			}
			else if("paymentDetails".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("paymentDetails", field);
				return field;
			}*/
			
			/*HashMap<String, AbstractField<?>> combos = tableItem.get(entryDTO);
			if(combos!=null){
				GComboBox cmbClassification = (GComboBox) combos.get("classification");
				cmbClassification.setData(entryDTO);
				cmbClassification.setValue(entryDTO.getClassification());
			}*/
			/*else if("sendforApprover".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("sendforApprover", field);
				return field;
			}
//			else if("reject".equals(propertyId)) {
//				TextField field = new TextField();
//				field.setWidth("100px");
//				field.setNullRepresentation("");
//				onlyNumber(field);
//				field.setData(entryDTO);
////				field.addValueChangeListener(totalpayableListener());
//				tableRow.put("reject", field);
//				return field;
//			}
			else if("notinClaimCount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("notinClaimCount", field);
				return field;
			}
			else if("submit".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				onlyNumber(field);
				field.setData(entryDTO);
//				field.addValueChangeListener(totalpayableListener());
				tableRow.put("submit", field);
				return field;
			}*/
			
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
			
		}

		
		
		private void doclasssificationValidation(GComboBox negoDone) {
			OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)negoDone.getData();
			HashMap<String, AbstractField<?>> combos = tableItem.get(calculationViewTableDTO);
			Boolean negogiationEnable = Boolean.FALSE;
			if(combos!=null){
			GComboBox cmbDocumentRec = (GComboBox)combos.get("docRecivedFrm");
			if(cmbDocumentRec!=null && cmbDocumentRec.getValue()!=null && combos!=null){
				SelectValue value2 = (SelectValue) cmbDocumentRec.getValue();
				GComboBox classification = (GComboBox)combos.get("classification");
				if(classification!=null && classification.getValue()!=null){
					SelectValue value = (SelectValue) classification.getValue();
					if(value.getValue().equalsIgnoreCase("OMP Claim Related") 
							
							&& bean.getIsCashless()){
						negogiationEnable = Boolean.TRUE;
					}
				}
				GComboBox negotiationDone = (GComboBox)combos.get("negotiationDone");
				if(negotiationDone!=null){
					negotiationDone.setEnabled(negogiationEnable);
				}
			}
			}
		}



		private void onlyNumber(TextField field) {
			CSValidator validator = new CSValidator();
			validator.extend(field);
			validator.setRegExp("^[0-9.]*$");
			validator.setPreventInvalidTyping(true);
		}
		
	}
	
	/*private HorizontalLayout buildButtonLayout()
	{
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		addAddBtnListener();
		
		return btnLayout;
	}*/
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	public void setTableList(final List<OMPClaimCalculationViewTableDTO> list) {
		table.removeAllItems();
		if(list != null && !list.isEmpty()){
			for (final OMPClaimCalculationViewTableDTO bean : list) {
				table.addItem(bean);
			}
			table.sort();
		}
	}
	
	
	private void addAddBtnListener() {/*
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				
				OMPClaimCalculationViewTableDTO docDTO = new OMPClaimCalculationViewTableDTO();
				List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
				bean.setIsOnLoad(Boolean.TRUE);
				BeanItem<OMPClaimCalculationViewTableDTO> addItem = container.addBean(docDTO);
				bean.setIsOnLoad(Boolean.FALSE);
				if(null != dtoList && !dtoList.isEmpty())
				{ 
					if(dtoList.size()>1){
						dummyField.setValue(null);
						dummyField.setValue("dummy");
						}
					bean.setRodNumber(null);
					bean.setRodKey(null);
					fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_CLAIM_APPROVER_SAVE, bean);
					int iSize = dtoList.size();
					OMPClaimCalculationViewTableDTO dto = dtoList.get(iSize-1);
					dto.setRodnumber(bean.getRodNumber());
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					//final TextField txtFld = (TextField) combos.get("particulars");
					if(combos!=null){
						TextField rodnumberFld = (TextField) combos.get("rodnumber");
						if(rodnumberFld!=null){
						rodnumberFld.setReadOnly(Boolean.FALSE);
						rodnumberFld.setValue(bean.getRodNumber());
						rodnumberFld.setReadOnly(Boolean.TRUE);
						}
						//final GComboBox txtFld = (GComboBox) combos.get("classification");
						//txtFld.focus();
						GComboBox select = (GComboBox)combos.get("select");
						if(select!=null){
						select.setContainerDataSource(null);
						select.setContainerDataSource(docDTO.getSelectNegoContainer());
						select.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						select.setItemCaptionPropertyId("value");
						}
					}
				}
				
		
			}
		});
	*/}
	
/*	private void addListener() {

		if(dummyField!= null){

			dummyField.addValueChangeListener( new ValueChangeListener( ) {
				private static final long serialVersionUID = 1L;

				@Override
		          public void valueChange( ValueChangeEvent event ) {
					TextField eventSelectValue = (TextField) event.getProperty();
					if(eventSelectValue!= null ){
						eventSelectValue.setValue("1");
					}
					
				}
			});}
		}*/
	
		
	
	
	@SuppressWarnings("unchecked")
	public void addValuesForValueDropDown(GComboBox comboBox) {
		
		
		//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
		/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
		 {
			if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
			{
				this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
			}
		}*/
		BeanItemContainer<SelectValue> categoryContainer=null;
		referenceData.clear();
		OMPClaimCalculationViewTableDTO dto = (OMPClaimCalculationViewTableDTO)comboBox.getData();
		HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
		if(combos!=null){
			GComboBox classification = (GComboBox) combos.get("classification");
			if(classification!=null && classification.getValue()!=null){
				SelectValue value = (SelectValue) classification.getValue();
				if(value!=null){
					if(value.getValue()!=null && value.getValue().equals("OMP Other Expenses")){
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_OTHR);
						 categoryContainer = (BeanItemContainer<SelectValue>) referenceData
								.get("category");
					}else if(value.getValue()!=null && value.getValue().equals("OMP Claim Related")){
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_REL);
						 categoryContainer = (BeanItemContainer<SelectValue>) referenceData
								.get("category");
					}else if(value.getValue()!=null && value.getValue().equals("Negotiator Fee")){
						fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_NEGO);
						 categoryContainer = (BeanItemContainer<SelectValue>) referenceData
								.get("category");
					}
				}
			}
			comboBox.setReadOnly(false);
			comboBox.setContainerDataSource(categoryContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void addValuesForValueSelect(GComboBox comboBox, OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		fireViewEvent(OMPClaimProcessorPagePresenter.OMP_CLAIM_PROCESSOR_FETCH_NEGO, calculationViewTableDTO);
		comboBox.setReadOnly(false);
		comboBox.setContainerDataSource(calculationViewTableDTO.getSelectNegoContainer());
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("value");
//		comboBox.setReadOnly(true);

	}
	
	private ValueChangeListener addListenerForClassification()
	{
			ValueChangeListener listener = new ValueChangeListener() {
				
				private static final long serialVersionUID = 7455756225751111662L;
				
				
			
				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox classification = (GComboBox) event.getProperty();
					OMPClaimCalculationViewTableDTO dto = (OMPClaimCalculationViewTableDTO)classification.getData();
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					if(classification!=null && classification.getValue()!=null && combos!=null){
						doClassificationLogics(classification, dto, combos);
						}
					
				}
			};
			return listener;
		}
	
	
	
	
	private void doClassificationLogics(GComboBox classification,
			OMPClaimCalculationViewTableDTO dto,
			HashMap<String, AbstractField<?>> combos) {
		SelectValue value2 = (SelectValue) classification.getValue();
		 GComboBox cmbSubClassification = (GComboBox) combos.get("subClassification");
		 GComboBox cmbDocumentsReceivedFrom = (GComboBox) combos.get("docRecivedFrm");
		 GComboBox cmbCurrencyType = (GComboBox) combos.get("currencyType");
		 TextField txtCurrencyRate = (TextField) combos.get("currencyrate");
		 TextField txtConversionValue = (TextField) combos.get("conversionValue");
		GComboBox cmbCategory = (GComboBox) combos.get("category");
		if(value2!=null && cmbCurrencyType!=null && txtCurrencyRate!=null && txtConversionValue!=null && cmbCategory!=null){
			BeanItemContainer<SelectValue> subClassification2 = new BeanItemContainer<SelectValue>(SelectValue.class);
			if(value2.getValue().equals("OMP Other Expenses")){
				
				if(cmbDocumentsReceivedFrom!=null){
					cmbDocumentsReceivedFrom.setValue(value2);
					cmbDocumentsReceivedFrom.setNullSelectionItemId(null);
				}
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(177L);
				selectValue1.setValue("INR");
				
				cmbCurrencyType.setValue(selectValue1);
				
				txtCurrencyRate.setValue("1");
				txtCurrencyRate.setEnabled(Boolean.FALSE);
				txtConversionValue.setValue("1");
				txtConversionValue.setEnabled(Boolean.FALSE);
				SelectValue category = dto.getCategory();
				referenceData.clear();
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_OTHR);

				setReferenceData(referenceData);
				
				addValuesForValueDropDown(cmbCategory);
				if(category!=null){
					cmbCategory.setValue(category);
				}
				BeanItemContainer<SelectValue> subClassification = bean.getSubClassificationContainer();
				List<SelectValue> itemIds = subClassification.getItemIds();
				List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
				SelectValue defaultValue= null;
				for (SelectValue selectValue : itemIds) {
					if(!selectValue.getValue().equalsIgnoreCase("OMP Claim Related") && !selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
						itemIdsList.add(selectValue);
					}
					if(selectValue.getValue().equalsIgnoreCase("Doctors opinion")){
						defaultValue =selectValue;
					}
				}
				subClassification2.addAll(itemIdsList);
				cmbSubClassification.setReadOnly(false);
				cmbSubClassification.setContainerDataSource(subClassification2);
				cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbSubClassification.setItemCaptionPropertyId("value");
				cmbSubClassification.setValue(defaultValue);
				cmbSubClassification.setReadOnly(dto.getIsReadOnly());
				if(tableItemchk!=null){
					CheckBox checkBox = tableItemchk.get(dto);
					if(checkBox!=null){
						checkBox.setValue(Boolean.TRUE);
						checkBox.setEnabled(Boolean.FALSE);
					}
				}
				doReadOnly(value2, dto);
			}
			
			
			if(value2.getValue().equals("OMP Claim Related")){
				
				if(cmbCurrencyType!=null && cmbCurrencyType.getValue()!=null ){
					
					SelectValue currencyRateSelectValue = (SelectValue) cmbCurrencyType.getValue();
					if(currencyRateSelectValue.getValue().equalsIgnoreCase("USD")){
						txtCurrencyRate.setValue("1");
						txtCurrencyRate.setEnabled(false);
						txtConversionValue.setEnabled(Boolean.TRUE);
					}else if(currencyRateSelectValue.getValue().equalsIgnoreCase("INR")){
						txtCurrencyRate.setValue("1");
						txtConversionValue.setValue("1");
						txtCurrencyRate.setEnabled(Boolean.FALSE);
						txtConversionValue.setEnabled(Boolean.FALSE);
					}
					else{
						//txtCurrencyRate.setValue(null);
						txtCurrencyRate.setEnabled(Boolean.TRUE);
						txtConversionValue.setEnabled(Boolean.TRUE);
					}
				
				}
				
				SelectValue category = dto.getCategory();
				referenceData.clear();
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_REL);

				setReferenceData(referenceData);
				addValuesForValueDropDown(cmbCategory);
				if(category!=null){
					cmbCategory.setValue(category);
				}
				SelectValue defaultValue= null;
				BeanItemContainer<SelectValue> subClassification = bean.getSubClassificationContainer();
				List<SelectValue> itemIds = subClassification.getItemIds();
				List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
				for (SelectValue selectValue : itemIds) {
					if(selectValue.getValue().equalsIgnoreCase("OMP Claim Related")){
						itemIdsList.add(selectValue);
						defaultValue =selectValue;
					}
				}
				subClassification2.addAll(itemIdsList);
				cmbSubClassification.setReadOnly(false);
				cmbSubClassification.setContainerDataSource(subClassification2);
				cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbSubClassification.setItemCaptionPropertyId("value");
				cmbSubClassification.setValue(defaultValue);
				cmbSubClassification.setReadOnly(dto.getIsReadOnly());
				if(tableItemchk!=null){
					CheckBox checkBox = tableItemchk.get(dto);
					if(checkBox!=null){
						checkBox.setValue(Boolean.FALSE);
						checkBox.setEnabled(Boolean.TRUE);
					}
				}
				doReadOnly(value2, dto);
			}
			if(value2.getValue().equals("Negotiator Fee")){
				if(cmbDocumentsReceivedFrom!=null){
					cmbDocumentsReceivedFrom.setValue(value2);
					cmbDocumentsReceivedFrom.setNullSelectionItemId(null);
				}
				//enableDeductiblesRecovered();
				SelectValue selectValue1 = new SelectValue();
				selectValue1.setId(48L);
				selectValue1.setValue("USD");
				cmbCurrencyType.setValue(selectValue1);
				txtCurrencyRate.setValue("1");
				txtCurrencyRate.setEnabled(Boolean.FALSE);
				txtConversionValue.setEnabled(Boolean.TRUE);
				
				SelectValue category = dto.getCategory();
				referenceData.clear();
				fireViewEvent(OMPProcessClaimApproverWizardPresenter.OMP_APPROVER_PARTICULARS, referenceData, SHAConstants.OMPCLM_NEGO);
				setReferenceData(referenceData);
				addValuesForValueDropDown(cmbCategory);
				if(category!=null){
					cmbCategory.setValue(category);
				}
				SelectValue defaultValue= null;
				BeanItemContainer<SelectValue> subClassification = bean.getSubClassificationContainer();
				List<SelectValue> itemIds = subClassification.getItemIds();
				List<SelectValue> itemIdsList = new ArrayList<SelectValue>();
				for (SelectValue selectValue : itemIds) {
					if(selectValue.getValue().equalsIgnoreCase("Negotiator Fee")){
						itemIdsList.add(selectValue);
						defaultValue =selectValue;
					}
				}
				subClassification2.addAll(itemIdsList);
				cmbSubClassification.setContainerDataSource(subClassification2);
				cmbSubClassification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				cmbSubClassification.setItemCaptionPropertyId("value");
				cmbSubClassification.setValue(defaultValue);
				
				dto.setClaimkey(bean.getClaimDto().getKey());
				SelectValue select2 = dto.getSelect();
				fireViewEvent(OMPClaimProcessorPagePresenter.OMP_CLAIM_PROCESSOR_FETCH_NEGO, dto);
				HashMap<String, AbstractField<?>> hashMapSelect = tableItem.get(dto);
				GComboBox select = (GComboBox)hashMapSelect.get("select");
				select.setReadOnly(Boolean.FALSE);
				select.setContainerDataSource(null);
				select.setContainerDataSource(dto.getSelectNegoContainer());
				select.setItemCaptionMode(ItemCaptionMode.PROPERTY);
				select.setItemCaptionPropertyId("value");
				if(select2!=null){
					select.setValue(select2);
				}
				select.setReadOnly(Boolean.TRUE);
				if(tableItemchk!=null){
					CheckBox checkBox = tableItemchk.get(dto);
					if(checkBox!=null){
						checkBox.setValue(Boolean.FALSE);
						checkBox.setEnabled(Boolean.FALSE);
					}
				}
				doReadOnly(value2, dto);
			}
			
			}
	}
	
	
	
	
	private void doReadOnly(
			SelectValue classification,
			OMPClaimCalculationViewTableDTO calculationViewTableDTO) {
		HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
		if(hashMap!= null && classification!=null && classification.getValue()!=null && classification.getValue().equalsIgnoreCase("OMP Claim Related")){
					if(!calculationViewTableDTO.getIsReadOnly()){
						getAllField(hashMap,Boolean.FALSE, Boolean.TRUE);
						if(!bean.getIsOnLoad()){
							List<OMPNegotiationDetailsDTO> negotiationDetailsDTOs = new ArrayList<OMPNegotiationDetailsDTO>();
							calculationViewTableDTO.setNegotiationDetailsDTOs(negotiationDetailsDTOs);
						}
					}
					TextField amtIn = (TextField)hashMap.get("amtIn");
					amtIn.setReadOnly(Boolean.TRUE);
					TextField totalAmt = (TextField)hashMap.get("totalAmt");
					totalAmt.setReadOnly(Boolean.TRUE);
					
					GComboBox negotiationDone = (GComboBox)hashMap.get("negotiationDone");
					negotiationDone.setReadOnly(Boolean.FALSE);
					if(negotiationDone!=null && negotiationDone.getValue()!=null){
						SelectValue value1 = (SelectValue) negotiationDone.getValue();
						negotiationDone.setValue(null);
						negotiationDone.setValue(value1);
					}
					negotiationDone.setReadOnly(calculationViewTableDTO.getIsReadOnly());
					Boolean negotiationEnable = Boolean.FALSE;
					if(bean.getIsCashless()){
						//SelectValue value = (SelectValue) documentRecieved.getValue();
						//if(value!=null && value.getValue()!=null && value.getValue().equals(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
							negotiationEnable = Boolean.TRUE;
						//}
					}
					if(negotiationDone!=null){
						negotiationDone.setEnabled(negotiationEnable);
					}
					Property prop = table.getItem(calculationViewTableDTO).getItemProperty("negotiationDetails");
//					System.out.println(prop);
					TextField deduction = (TextField)hashMap.get("deduction");
					deduction.setReadOnly(calculationViewTableDTO.getIsReadOnly());
					if(bean.getDeductibles()!=null && !bean.getIsOnLoad() && !calculationViewTableDTO.getIsReadOnly()){
						deduction.setValue(bean.getDeductibles().toString());
					}
					//uploadDetailsButton.setEnabled(Boolean.TRUE);
					//recoverableButton.setEnabled(Boolean.TRUE);
					//paymentDetails.setEnabled(Boolean.TRUE);
		}
		if(hashMap!= null && classification!=null && classification.getValue()!=null && classification.getValue().equalsIgnoreCase("OMP Other Expenses")){
			if(!calculationViewTableDTO.getIsReadOnly()){
			//getAllField(hashMap,Boolean.TRUE, Boolean.FALSE);
			getAllField(hashMap,Boolean.TRUE, Boolean.TRUE);
			TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
			totalAmtInr.setReadOnly(Boolean.FALSE);
			TextField deduction = (TextField)hashMap.get("deduction");
			deduction.setReadOnly(Boolean.FALSE);
			deduction.setValue("0");
			deduction.setReadOnly(Boolean.TRUE);
			GComboBox negotiationDone = (GComboBox)hashMap.get("negotiationDone");
			if(negotiationDone!=null){
				negotiationDone.setEnabled(Boolean.FALSE);
			}}
			//negotitiationDetailButton.setEnabled(Boolean.FALSE);
			//uploadDetailsButton.setEnabled(Boolean.FALSE);
			//recoverableButton.setEnabled(Boolean.FALSE);
			//paymentDetails.setEnabled(Boolean.FALSE);
			//chkBox.setValue(Boolean.TRUE);
		}
		if(hashMap!= null && classification!=null && classification.getValue()!=null && classification.getValue().equalsIgnoreCase("Negotiator Fee")){
			if(!calculationViewTableDTO.getIsReadOnly()){
			getAllField(hashMap,Boolean.TRUE, Boolean.TRUE);
			TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
			totalAmtInr.setReadOnly(Boolean.TRUE);
			GComboBox select = (GComboBox)hashMap.get("select");
			select.setReadOnly(Boolean.FALSE);
			TextField deduction = (TextField)hashMap.get("deduction");
			deduction.setReadOnly(Boolean.FALSE);
			deduction.setValue("0");
			deduction.setReadOnly(Boolean.TRUE);
			GComboBox negotiationDone = (GComboBox)hashMap.get("negotiationDone");
			if(negotiationDone!=null){
				negotiationDone.setEnabled(Boolean.FALSE);
			}}
			//negotitiationDetailButton.setEnabled(Boolean.FALSE);
			//uploadDetailsButton.setEnabled(Boolean.FALSE);
			//recoverableButton.setEnabled(Boolean.FALSE);
			//paymentDetails.setEnabled(Boolean.FALSE);
		}
	}
	
	private void getAllField(
			HashMap<String, AbstractField<?>> hashMap, Boolean true1, Boolean false1) {

		TextField billAmt = (TextField)hashMap.get("billAmt");
		billAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			billAmt.setValue(null);
		}
		billAmt.setReadOnly(true1);
		TextField amtIn = (TextField)hashMap.get("amtIn");
		amtIn.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			amtIn.setValue(null);
		}
		amtIn.setReadOnly(true1);
		TextField deduction = (TextField)hashMap.get("deduction");
		deduction.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			deduction.setValue(null);
		}
		deduction.setReadOnly(true1);
		TextField totalAmt = (TextField)hashMap.get("totalAmt");
		totalAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			totalAmt.setValue(null);
		}
		totalAmt.setReadOnly(true1);
		GComboBox copayPercentage = (GComboBox)hashMap.get("copay");
		copayPercentage.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			copayPercentage.setValue(null);
		}
		copayPercentage.setReadOnly(true1);
		TextField copayAmt = (TextField)hashMap.get("copayamount");
		copayAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			copayAmt.setValue(null);
		}
		copayAmt.setReadOnly(Boolean.TRUE);
		TextField approvedamountaftecopay = (TextField)hashMap.get("approvedamountaftecopay");
		approvedamountaftecopay.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			approvedamountaftecopay.setValue(null);
		}
		approvedamountaftecopay.setReadOnly(Boolean.TRUE);
		TextField afternegotiation = (TextField)hashMap.get("afternegotiation");
		afternegotiation.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			afternegotiation.setValue(null);
		}
		afternegotiation.setReadOnly(Boolean.TRUE);
		TextField totalAmtInr = (TextField)hashMap.get("totalAmtInr");
		totalAmtInr.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			totalAmtInr.setValue(null);
		}
		totalAmtInr.setReadOnly(false1);
		GComboBox select = (GComboBox)hashMap.get("select");
		select.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			select.setValue(null);
		}
		select.setReadOnly(false1);
		TextField approvedAmt = (TextField)hashMap.get("approvedAmt");
		approvedAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			approvedAmt.setValue(null);
		}
		approvedAmt.setReadOnly(false1);
		TextField agreedAmt = (TextField)hashMap.get("agreedAmt");
		agreedAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			agreedAmt.setValue(null);
		}
		agreedAmt.setReadOnly(false1);
		TextField differenceAmt = (TextField)hashMap.get("differenceAmt");
		differenceAmt.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			differenceAmt.setValue(null);
		}
		differenceAmt.setReadOnly(false1);
		
		//CR2019041  enable handlingCharges,totalExp,expenses
		TextField expenses = (TextField)hashMap.get("expenses");
		expenses.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			expenses.setValue(null);
		}
	/*	if(expenses!=null && expenses.getValue()!=null){
			expenses.setReadOnly(Boolean.FALSE);
		}else{*/
			expenses.setReadOnly(false1);
//		}
		
		/*TextField negotiationClaimed = (TextField)hashMap.get("negotiationClaimed");
		negotiationClaimed.setReadOnly(false1);
		TextField negotiationCapping = (TextField)hashMap.get("negotiationCapping");
		negotiationCapping.setReadOnly(false1);*/
	
		TextField handlingCharges = (TextField)hashMap.get("handlingCharges");
		handlingCharges.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			handlingCharges.setValue(null);
		}
		
		if(handlingCharges!=null && handlingCharges.getValue()!=null){
			handlingCharges.setReadOnly(Boolean.FALSE);
		}else{
			handlingCharges.setReadOnly(false1);
		}
		TextField totalExp = (TextField)hashMap.get("totalExp");
		totalExp.setReadOnly(Boolean.FALSE);
		if(!bean.getIsOnLoad()){
			totalExp.setValue(null);
		}
		/*if(totalExp!=null && totalExp.getValue()!=null){
			totalExp.setReadOnly(Boolean.FALSE);
		}else{*/
			totalExp.setReadOnly(false1);
//		}
		//CR2019041
	
	}
	
	 public ValueChangeListener billAmtListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField billAmt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)billAmt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField amtIn = (TextField)hashMap.get("amtIn");
	 				TextField currencyrate = (TextField)hashMap.get("currencyrate");
	 				
	 				Double currencyRate= 0d;
	 				if(currencyrate!=null && currencyrate.getValue()!=null && !currencyrate.getValue().equals("")){
	 					currencyRate = SHAUtils.getDoubleFromStringWithComma(currencyrate.getValue());
	 					Double billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
	 					Double amtInDollor = billAmtDouble * currencyRate;
	 					if(amtIn!=null){
	 						amtIn.setReadOnly(Boolean.FALSE);
	 						amtIn.setValue(amtInDollor.toString());
	 						amtIn.setReadOnly(Boolean.TRUE);
	 					}
	 					
	 				}/*else if(bean.getCurrencyRate()!=null){

	 					currencyRate = bean.getCurrencyRate();
	 					Double billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
	 					Double amtInDollor = billAmtDouble * currencyRate;
	 					if(amtIn!=null){
	 						amtIn.setReadOnly(Boolean.FALSE);
	 						amtIn.setValue(amtInDollor.toString());
	 						amtIn.setReadOnly(Boolean.TRUE);
	 					}
	 					
	 				
	 				}*/
	 				
	 				GComboBox copayamount = (GComboBox)hashMap.get("copay");
	 				if(copayamount!=null){
	 				SelectValue value = (SelectValue) copayamount.getValue();
	 				if(value!=null && value.getValue()!=null){
	 					//copayamount.setReadOnly(Boolean.FALSE);
		 				copayamount.setValue(null);
		 				copayamount.setValue(value);
		 				//copayamount.setReadOnly(Boolean.TRUE);
	 				}}
	 				calculateTotal();
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener amtInDollorListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField amtIn = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)amtIn.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField deduction = (TextField)hashMap.get("deduction");
					TextField totalAmt = (TextField)hashMap.get("totalAmt");
					Double deductionDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(deduction!=null && deduction.getValue()!=null){
						deductionDouble = SHAUtils.getDoubleFromStringWithComma(deduction.getValue());
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						totalAmtDouble = amtinDollorDouble - deductionDouble;
						if(totalAmtDouble < 0){
							totalAmtDouble =0d;
						}
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(totalAmtDouble.toString());
							totalAmt.setReadOnly(Boolean.TRUE);
						}
					}else{
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						if(totalAmt!=null){
							totalAmt.setReadOnly(Boolean.FALSE);
							totalAmt.setValue(amtinDollorDouble.toString());
							totalAmt.setReadOnly(Boolean.TRUE);
						}
					}
					TextField approvedamountaftecopay = (TextField)hashMap.get("approvedamountaftecopay");
					if(approvedamountaftecopay!=null){
						totalAmtDouble = addRecovarableCopay(calculationViewTableDTO.getOmpRecoverableTableList(),totalAmtDouble,approvedamountaftecopay);
						approvedamountaftecopay.setReadOnly(Boolean.FALSE);
						approvedamountaftecopay.setValue(totalAmtDouble.toString());
						approvedamountaftecopay.setReadOnly(Boolean.TRUE);
					}
					GComboBox copayamount = (GComboBox)hashMap.get("copay");
	 				if(copayamount!=null){
	 				SelectValue value = (SelectValue) copayamount.getValue();
	 				if(value!=null && value.getValue()!=null){
	 					copayamount.setReadOnly(Boolean.FALSE);
		 				copayamount.setValue(null);
		 				copayamount.setValue(value);
		 				//copayamount.setReadOnly(Boolean.TRUE);
	 				}}
					calculateTotal();
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener deductionListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					final TextField deduction = (TextField) event.getProperty();
					if(deduction!=null && deduction.getValue()!=null){
						String value = deduction.getValue();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
					if(calculationViewTableDTO.getDeductiblesOriginal()!=null 
							&& Double.compare(calculationViewTableDTO.getDeductiblesOriginal(), SHAUtils.getDoubleFromStringWithComma(value))!=0){
						ConfirmDialog dialog = ConfirmDialog.show(getUI(), "Confirmation",
								"Deductible has been edited ?",
								"No", "Yes", new ConfirmDialog.Listener() {
						
									private static final long serialVersionUID = 1L;
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isCanceled() && !dialog.isConfirmed()) {
											doDeductibles(deduction);
										}else{
											if(deduction!=null){
												deduction.setReadOnly(Boolean.FALSE);
												OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
												deduction.setValue(String.valueOf(calculationViewTableDTO.getDeductiblesOriginal()));
												//deduction.setReadOnly(Boolean.TRUE);
											}
										}
									}
								});
						
						dialog.setStyleName(Reindeer.WINDOW_BLACK);
					}else{
						doDeductibles(deduction);
				}}}

			};
			
			return listener;
		}
	 
	 
	 private void doDeductibles(TextField deduction) {
			OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
			TextField amtIn = (TextField)hashMap.get("amtIn");
			TextField totalAmt = (TextField)hashMap.get("totalAmt");
			Double deductionDouble = 0d;
			Double amtinDollorDouble =0d;
			Double totalAmtDouble =0d;
			if(deduction!=null && deduction.getValue()!=null){
				deductionDouble = SHAUtils.getDoubleFromStringWithComma(deduction.getValue());
				amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
				totalAmtDouble = (amtinDollorDouble - deductionDouble);
				if(totalAmtDouble<0){
					totalAmtDouble =0d;
				}
				if(totalAmt!=null){
					totalAmt.setReadOnly(Boolean.FALSE);
					totalAmt.setValue(totalAmtDouble.toString());
					totalAmt.setReadOnly(Boolean.TRUE);
				}
			}else{
				amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
				totalAmtDouble = amtinDollorDouble;
				if(totalAmt!=null){
					totalAmt.setReadOnly(Boolean.FALSE);
					totalAmt.setValue(amtinDollorDouble.toString());
					totalAmt.setReadOnly(Boolean.TRUE);
				}
			}
			TextField approvedamountaftecopay = (TextField)hashMap.get("approvedamountaftecopay");
			if(approvedamountaftecopay!=null){
				totalAmtDouble = addRecovarableCopay(calculationViewTableDTO.getOmpRecoverableTableList(),totalAmtDouble,approvedamountaftecopay);
				approvedamountaftecopay.setReadOnly(Boolean.FALSE);
				approvedamountaftecopay.setValue(totalAmtDouble.toString());
				approvedamountaftecopay.setReadOnly(Boolean.TRUE);
			}
			
			GComboBox copayamount = (GComboBox)hashMap.get("copay");
			if(copayamount!=null){
			SelectValue value = (SelectValue) copayamount.getValue();
			if(value!=null && value.getValue()!=null){
				copayamount.setReadOnly(Boolean.FALSE);
				copayamount.setValue(null);
				copayamount.setValue(value);
				copayamount.setReadOnly(calculationViewTableDTO.getIsReadOnly());
				//copayamount.setReadOnly(Boolean.TRUE);
				//copayamount.setReadOnly(Boolean.TRUE);
			}}
			calculateTotal();
		}
	 
	 
	 
	 public ValueChangeListener totalAmtInrListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField deduction = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)deduction.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField amtIn = (TextField)hashMap.get("totalAmtInr");
					if(amtIn!=null && amtIn.getValue()!=null){
						Double totalAmt = SHAUtils.getDoubleFromStringWithComma(amtIn.getValue());
						calculateTotal();
					}
				}
			};
			
			return listener;
		}
	 
	 
	 public ValueChangeListener approvedListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField approvedTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)approvedTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField agreedTxt = (TextField)hashMap.get("agreedAmt");
					TextField differenceAmtTxt = (TextField)hashMap.get("differenceAmt");
					Double approvedDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(approvedTxt!=null && approvedTxt.getValue()!=null){
						approvedDouble = SHAUtils.getDoubleFromStringWithComma(approvedTxt.getValue());
						if(agreedTxt!=null){
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(agreedTxt.getValue());
						totalAmtDouble = approvedDouble - amtinDollorDouble;
						
							differenceAmtTxt.setReadOnly(Boolean.FALSE);
							differenceAmtTxt.setValue(totalAmtDouble.toString());
							differenceAmtTxt.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener agreedListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField agreedAmt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)agreedAmt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField approvedTxt1 = (TextField)hashMap.get("approvedAmt");
					TextField differenceAmtTxt = (TextField)hashMap.get("differenceAmt");
					Double approvedDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(agreedAmt!=null && agreedAmt.getValue()!=null){
						approvedDouble = SHAUtils.getDoubleFromStringWithComma(agreedAmt.getValue());
						amtinDollorDouble = SHAUtils.getDoubleFromStringWithComma(approvedTxt1.getValue());
						totalAmtDouble = amtinDollorDouble - approvedDouble;
						if(approvedTxt1!=null && differenceAmtTxt!=null){
							differenceAmtTxt.setReadOnly(Boolean.FALSE);
							differenceAmtTxt.setValue(totalAmtDouble.toString());
							differenceAmtTxt.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener differenceListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField differenceAmtTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)differenceAmtTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField expenses = (TextField)hashMap.get("expenses");
					Double differenceDouble = 0d;
					Double amtinDollorDouble =0d;
					Double totalAmtDouble =0d;
					if(differenceAmtTxt!=null && differenceAmtTxt.getValue()!=null){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(differenceAmtTxt.getValue());
						totalAmtDouble = differenceDouble * SHAUtils.getDoubleFromStringWithComma(SHAConstants.OMP_NEGO_EXP);
						totalAmtDouble = totalAmtDouble / 100;
						if(differenceAmtTxt!=null && expenses!=null){
							expenses.setReadOnly(Boolean.FALSE);
							expenses.setValue(totalAmtDouble.toString());
							expenses.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener totalpayableListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField negoPayableTxt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)negoPayableTxt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField totalExp = (TextField)hashMap.get("totalExp");
					TextField handlingCharges = (TextField)hashMap.get("handlingCharges");
					Double differenceDouble = 0d;
					Double totalAmtDouble =0d;
					Double handlingChargesDouble =0d;
					if(handlingCharges!=null &&negoPayableTxt!=null && negoPayableTxt.getValue()!=null ){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(negoPayableTxt.getValue());
						handlingChargesDouble = SHAUtils.getDoubleFromStringWithComma(handlingCharges.getValue());
						totalAmtDouble = differenceDouble + handlingChargesDouble ;
						if(negoPayableTxt!=null){
							totalExp.setReadOnly(Boolean.FALSE);
							totalExp.setValue(totalAmtDouble.toString());
							totalExp.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener handlingpayableListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField handingText = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)handingText.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField handlingCharge = (TextField)hashMap.get("totalExp");
					TextField totalExp = (TextField)hashMap.get("totalExp");
					Double differenceDouble = 0d;
					Double totalAmtDouble =0d;
					if(handingText!=null && handingText.getValue()!=null && handlingCharge!=null && handlingCharge.getValue()!=null){
						differenceDouble = SHAUtils.getDoubleFromStringWithComma(handingText.getValue());
						totalAmtDouble = differenceDouble + calculationViewTableDTO.getNegotiationPayable() ;
						if(totalExp!=null){
							totalExp.setReadOnly(Boolean.FALSE);
							totalExp.setValue(totalAmtDouble.toString());
							totalExp.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public void calculateTotal() {
			
			List<OMPClaimCalculationViewTableDTO> itemIconPropertyId = (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
			Double billAmt = 0d;
			Double amtInDollor=0d;
			Double deduction = 0d;
			Double totalAmt = 0d;
			Double totalAmtInr = 0d;
			for (OMPClaimCalculationViewTableDTO dto : itemIconPropertyId) {
			    Double billAmtDouble = dto.getBillAmt();
			    billAmt += billAmtDouble != null ? billAmtDouble : 0;
			    
			    Double amtInDollorDouble = dto.getAmtIn();
			    amtInDollor += amtInDollorDouble != null ? amtInDollorDouble : 0;
			    
			    Double deductionDouble = dto.getDeduction();
			    deduction += deductionDouble != null ? deductionDouble : 0;
			    
			    Double totalAmtDouble = dto.getTotalAmt();
			    totalAmt += totalAmtDouble != null ? totalAmtDouble : 0;
			    
			    Double totalAmtINRDouble = dto.getTotalAmtInr();
			    totalAmtInr += totalAmtINRDouble != null ? totalAmtINRDouble : 0;
			    
			}
			table.setColumnFooter("billAmt", String.valueOf(billAmt));
			table.setColumnFooter("amtIn", String.valueOf(amtInDollor));
			table.setColumnFooter("deduction", String.valueOf(deduction));
			table.setColumnFooter("totalAmt", String.valueOf(totalAmt));
			table.setColumnFooter("totalAmtInr", String.valueOf(totalAmtInr));
			bean.setRodProvisionAmt(totalAmt);
		}
	 
	 public ValueChangeListener currencyRateListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField txtCurrencyRate = (TextField) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)txtCurrencyRate.getData();
								Double currencyRate =0d;
								Double billAmtDouble =0d;
								Double amtInDollorDouble =0d;
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								if(hashMap!=null){
								TextField billAmt = (TextField)hashMap.get("billAmt");
								if(billAmt!=null && billAmt.getValue()!=null){
									billAmtDouble = SHAUtils.getDoubleFromStringWithComma(billAmt.getValue());
								}
								if(txtCurrencyRate!=null && txtCurrencyRate.getValue()!=null){
									currencyRate = SHAUtils.getDoubleFromStringWithComma(txtCurrencyRate.getValue());
								}
								TextField amtInDollor = (TextField)hashMap.get("amtIn");
								if(amtInDollor!=null){
									amtInDollorDouble = billAmtDouble * currencyRate;
									amtInDollor.setReadOnly(Boolean.FALSE);
									amtInDollor.setValue(amtInDollorDouble.toString());
									amtInDollor.setReadOnly(Boolean.TRUE);
								}
								}
		 				
						calculateTotal();
					}
				};
			return listener;
		}
	 
	 public ValueChangeListener currencyTypeListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						GComboBox cmbCurrencyType = (GComboBox) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)cmbCurrencyType.getData();
						HashMap<String, AbstractField<?>> combos = tableItem.get(calculationViewTableDTO);
						if(cmbCurrencyType!=null && cmbCurrencyType.getValue()!=null && combos!=null){
							
							BeanItemContainer<SelectValue> currencyValueContainer = calculationViewTableDTO.getCurrencyValueContainer();
							if(currencyValueContainer.size()>1){
								SelectValue idByIndex = currencyValueContainer.getIdByIndex(1);
								currencyValueContainer.removeItem(idByIndex);
							}
							SelectValue currencyRateSelectValue = (SelectValue) cmbCurrencyType.getValue();
							if(!currencyRateSelectValue.getId().equals(107L)){
								currencyValueContainer.addBean(currencyRateSelectValue);
								calculationViewTableDTO.setCurrencyValueContainer(currencyValueContainer);
							}
							
							 TextField txtCurrencyRate = (TextField) combos.get("currencyrate");
							 TextField txtConversionValue = (TextField) combos.get("conversionValue");
							if(txtCurrencyRate!=null && txtConversionValue!=null){
							if(currencyRateSelectValue.getValue().equalsIgnoreCase("USD") ){
								txtCurrencyRate.setValue("1");
								txtCurrencyRate.setEnabled(false);
								txtConversionValue.setEnabled(Boolean.TRUE);
							}else if(currencyRateSelectValue.getValue().equalsIgnoreCase("INR")){
								txtCurrencyRate.setValue("1");
								txtConversionValue.setValue("1");
								txtCurrencyRate.setEnabled(Boolean.FALSE);
								txtConversionValue.setEnabled(Boolean.FALSE);
							}
							else{
								//txtCurrencyRate.setValue(null);
								txtCurrencyRate.setEnabled(Boolean.TRUE);
								txtConversionValue.setEnabled(Boolean.TRUE);
							}
						
						}}
							
					}
				};
			return listener;
		}
	 
	 
	 public ValueChangeListener documentRevListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						GComboBox cmbDocumentRec = (GComboBox) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)cmbDocumentRec.getData();
						HashMap<String, AbstractField<?>> combos = tableItem.get(calculationViewTableDTO);
						Boolean negogiationEnable = Boolean.FALSE;
						if(cmbDocumentRec!=null && cmbDocumentRec.getValue()!=null && combos!=null){
							SelectValue value2 = (SelectValue) cmbDocumentRec.getValue();
							if(value2!= null &&value2.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL) 
									&& bean.getIsCashless()!= null && bean.getIsCashless().equals(Boolean.FALSE)
									&&bean.getNonHospitalisationFlag()!= null && bean.getNonHospitalisationFlag().equalsIgnoreCase("N")){
								/*showErrorPopup("Hospital should not be allowed for reimbursement.");
								cmbDocumentRec.setReadOnly(Boolean.FALSE);
								cmbDocumentRec.setValue(null);*/
							}
							GComboBox classification = (GComboBox)combos.get("classification");
							if(classification!=null && classification.getValue()!=null){
								SelectValue value = (SelectValue) classification.getValue();
								if(value.getValue().equalsIgnoreCase("OMP Claim Related") &&value2.getValue()!= null
										&& bean.getIsCashless()){
									negogiationEnable = Boolean.TRUE;
								}
								/*if(!value2.getValue().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)){
									TextField afternegotiation = (TextField)combos.get("afternegotiation");
									if(afternegotiation!=null){
										afternegotiation.setReadOnly(Boolean.FALSE);
										afternegotiation.setValue(null);
										afternegotiation.setReadOnly(Boolean.TRUE);
									}
									GComboBox negotiationDone = (GComboBox)combos.get("negotiationDone");
									if(negotiationDone!=null){
										negotiationDone.setReadOnly(Boolean.FALSE);
										negotiationDone.setValue(null);
										negotiationDone.setEnabled(negogiationEnable);
									}
								}*/
							}
							GComboBox negotiationDone = (GComboBox)combos.get("negotiationDone");
							if(negotiationDone!=null){
								negotiationDone.setEnabled(negogiationEnable);
							}
						}
							
					}
				};
			return listener;
		}
	 
	 public ValueChangeListener copayPercentageListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox copayPercentage = (GComboBox) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)copayPercentage.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField amtIndollar = (TextField)hashMap.get("totalAmt");
					TextField copayAmt = (TextField)hashMap.get("copayamount");
					TextField approvedamountaftecopay = (TextField)hashMap.get("approvedamountaftecopay");
					Double copayDouble = 0d;
					Double totalAmtDouble =0d;
					Double approvedAmtDouble =0d;
					SelectValue coPayValue = (SelectValue) copayPercentage.getValue();
					if(coPayValue!=null && coPayValue.getValue()!=null && amtIndollar!=null && amtIndollar.getValue()!=null){
						copayDouble = SHAUtils.getDoubleFromStringWithComma(coPayValue.getValue());
						Double amtIndollarDouble = SHAUtils.getDoubleFromStringWithComma(amtIndollar.getValue());
						totalAmtDouble = amtIndollarDouble*copayDouble/100;
						approvedAmtDouble = amtIndollarDouble - totalAmtDouble;
						
						if(copayAmt!=null){
							copayAmt.setReadOnly(Boolean.FALSE);
							copayAmt.setValue(totalAmtDouble.toString());
							copayAmt.setReadOnly(Boolean.TRUE);
						}
						if(approvedamountaftecopay!=null){
							approvedAmtDouble = addRecovarableCopay(calculationViewTableDTO.getOmpRecoverableTableList(),approvedAmtDouble,approvedamountaftecopay);
							approvedamountaftecopay.setReadOnly(Boolean.FALSE);
							approvedamountaftecopay.setValue(approvedAmtDouble.toString());
							approvedamountaftecopay.setReadOnly(Boolean.TRUE);
						}
					}else{
						if(copayAmt!=null){
							copayAmt.setReadOnly(Boolean.FALSE);
							copayAmt.setValue(totalAmtDouble.toString());
							copayAmt.setReadOnly(Boolean.TRUE);
						}
						if(approvedamountaftecopay!=null){
							approvedAmtDouble = SHAUtils.getDoubleFromStringWithComma(amtIndollar.getValue());
							approvedAmtDouble = addRecovarableCopay(calculationViewTableDTO.getOmpRecoverableTableList(),approvedAmtDouble,approvedamountaftecopay);
							approvedamountaftecopay.setReadOnly(Boolean.FALSE);
							approvedamountaftecopay.setValue(approvedAmtDouble.toString());
							approvedamountaftecopay.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
	 public ValueChangeListener selectListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox select = (GComboBox) event.getProperty();
					if(select!=null && select.getValue()==null){
						setNegotiationNull(select);
					}
					
					Boolean selectEnable = null;
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)select.getData();
					List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
					if(dtoList!=null && !dtoList.isEmpty() && select!=null && select.getValue()!=null && selectEnable==null){
						for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : dtoList) {
							selectEnable = Boolean.TRUE;
								if(!ompClaimCalculationViewTableDTO.equals(calculationViewTableDTO)){
									SelectValue value = (SelectValue) select.getValue();
									if(ompClaimCalculationViewTableDTO.getSelect()!=null && value!=null){
										if(ompClaimCalculationViewTableDTO.getSelect().getId().equals(value.getId())){
											showErrorPopup("Negotition is already been selected");
											selectEnable = Boolean.FALSE;
											select.setValue(null);
											//setNegotiationNull(select);
										}
									}
								}
						}
					}
					
					if (selectEnable!=null && selectEnable && select.getValue()!=null) {
						SelectValue value = (SelectValue) select.getValue();
						if (value != null) {
							calculationViewTableDTO.setNegokey(value.getId());
						}
						HashMap<String, AbstractField<?>> hashMap = tableItem
								.get(calculationViewTableDTO);
						fireViewEvent(
								OMPProcessClaimApproverWizardPresenter.OMP_CLAIM_PROCESSOR_FETCH_NEGO_FROM_LIST,
								calculationViewTableDTO);
						TextField approvedAmt = (TextField) hashMap
								.get("approvedAmt");
						TextField agreedAmt = (TextField) hashMap
								.get("agreedAmt");
						TextField differenceAmt = (TextField) hashMap
								.get("differenceAmt");
						TextField expenses = (TextField) hashMap
								.get("expenses");
						TextField handlingCharges = (TextField) hashMap
								.get("handlingCharges");
						TextField totalExp = (TextField) hashMap
								.get("totalExp");
						if (calculationViewTableDTO.getApprovedAmt() != null
								&& approvedAmt != null) {
							approvedAmt.setReadOnly(Boolean.FALSE);
							Double approvedAmtWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getApprovedAmt().toString());
							approvedAmt.setValue(approvedAmtWithComma
									.toString());
							approvedAmt.setReadOnly(Boolean.TRUE);
						}
						if (calculationViewTableDTO.getAgreedAmt() != null
								&& agreedAmt != null) {
							agreedAmt.setReadOnly(Boolean.FALSE);
							Double agrredAmtWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getAgreedAmt().toString());
							agreedAmt.setValue(agrredAmtWithComma.toString());
							agreedAmt.setReadOnly(Boolean.TRUE);
						}
						if (calculationViewTableDTO.getDifferenceAmt() != null
								&& differenceAmt != null) {
							differenceAmt.setReadOnly(Boolean.FALSE);
							Double differenceAmttWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getDifferenceAmt().toString());
							differenceAmt.setValue(differenceAmttWithComma
									.toString());
							differenceAmt.setReadOnly(Boolean.TRUE);
						}
						if (calculationViewTableDTO.getExpenses() != null
								&& expenses != null) {
							expenses.setReadOnly(Boolean.FALSE);
							Double expensesAmttWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getExpenses().toString());
							expenses.setValue(expensesAmttWithComma.toString());
							expenses.setReadOnly(Boolean.TRUE);
						}
						if (calculationViewTableDTO.getTotalExp() != null
								&& totalExp != null) {
							totalExp.setReadOnly(Boolean.FALSE);
							Double totalExpAmttWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getTotalExp().toString());
							totalExp.setValue(totalExpAmttWithComma.toString());
							totalExp.setReadOnly(Boolean.TRUE);
						}
						if (calculationViewTableDTO.getHandlingCharges() != null
								&& handlingCharges != null) {
							handlingCharges.setReadOnly(Boolean.FALSE);
							Double handlingChargesAmttWithComma = SHAUtils
									.getDoubleFromStringWithComma(calculationViewTableDTO
											.getHandlingCharges().toString());
							handlingCharges
									.setValue(handlingChargesAmttWithComma
											.toString());
							//CR2019041 make editable
							if(calculationViewTableDTO.getSendforApprover()==null || calculationViewTableDTO.getSendforApprover().equals("N")){
								handlingCharges.setReadOnly(Boolean.FALSE);
							}else{
								handlingCharges.setReadOnly(Boolean.TRUE);
							}
//							handlingCharges.setReadOnly(Boolean.TRUE);
						}
					}
				}

				private void setNegotiationNull(GComboBox select) {
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)select.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
					TextField approvedAmt = (TextField) hashMap.get("approvedAmt");
					TextField agreedAmt = (TextField) hashMap.get("agreedAmt");
					TextField differenceAmt = (TextField) hashMap.get("differenceAmt");
					TextField expenses = (TextField) hashMap.get("expenses");
					TextField handlingCharges = (TextField) hashMap.get("handlingCharges");
					TextField totalExp = (TextField) hashMap.get("totalExp");

					approvedAmt.setReadOnly(Boolean.FALSE);
					approvedAmt.setValue(null);
					approvedAmt.setReadOnly(Boolean.TRUE);
					
					agreedAmt.setReadOnly(Boolean.FALSE);
					agreedAmt.setValue(null);
					agreedAmt.setReadOnly(Boolean.TRUE);
					
					differenceAmt.setReadOnly(Boolean.FALSE);
					differenceAmt.setValue(null);
					differenceAmt.setReadOnly(Boolean.TRUE);
					
					expenses.setReadOnly(Boolean.FALSE);
					expenses.setValue(null);
					expenses.setReadOnly(Boolean.TRUE);
					
					handlingCharges.setReadOnly(Boolean.FALSE);
					handlingCharges.setValue(null);
					handlingCharges.setReadOnly(Boolean.TRUE);
					
					totalExp.setReadOnly(Boolean.FALSE);
					totalExp.setValue(null);
					totalExp.setReadOnly(Boolean.TRUE);
				}
			};
			
			return listener;
		}
	 
	 	@SuppressWarnings("unchecked")
		public List<OMPClaimCalculationViewTableDTO> getValues() {
			List<OMPClaimCalculationViewTableDTO> itemIds = (List<OMPClaimCalculationViewTableDTO>) this.table.getItemIds() ;
			if(deletedList!=null && !deletedList.isEmpty()){
				for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : deletedList) {
					ompClaimCalculationViewTableDTO.setDeleted("Y");
				}
				deletedList.addAll(itemIds);
				return deletedList;
			}else{
				return itemIds;
			}	
	 	}	 
	 private void showErrorPopup(String eMsg) {
			Label label = new Label(eMsg, ContentMode.HTML);
		    label.setStyleName("errMessage");
		    VerticalLayout layout = new VerticalLayout();
		    layout.setMargin(true);
		    layout.addComponent(label);
		    
		    ConfirmDialog dialog = new ConfirmDialog();
		    dialog.setCaption("Errors");
		    dialog.setClosable(true);
		    dialog.setContent(layout);
		    dialog.setResizable(false);
		    dialog.setModal(true);
		    dialog.show(getUI().getCurrent(), null, true);
		}
	 
	 public ValueChangeListener negoDoneListener(){

			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					GComboBox negoDone = (GComboBox) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)negoDone.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField afternegotiation = (TextField)hashMap.get("afternegotiation");
					if(negoDone.getValue()!=null){
						SelectValue value = (SelectValue) negoDone.getValue();
						if(value!=null ){
							if(value.getValue().equalsIgnoreCase("Yes")){
				 				if(afternegotiation!=null && calculationViewTableDTO.getAfternegotiation()!=null){
				 					afternegotiation.setReadOnly(Boolean.FALSE);
				 					afternegotiation.setValue(calculationViewTableDTO.getAfternegotiation().toString());
				 					afternegotiation.setReadOnly(Boolean.TRUE);
				 				}
							}else{
								afternegotiation.setReadOnly(Boolean.FALSE);
								afternegotiation.setValue(null);
								afternegotiation.setReadOnly(Boolean.TRUE);
							}
						}else{
							afternegotiation.setReadOnly(Boolean.FALSE);
							afternegotiation.setValue(null);
							afternegotiation.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		
	 }
	 
		public Set<String> validateCalculation(OMPClaimProcessorDTO dtoBean) {
//			Boolean hasError = false;
//			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			List<OMPClaimCalculationViewTableDTO> itemIds = (List<OMPClaimCalculationViewTableDTO>) table.getItemIds();
			
			if(null != itemIds && !itemIds.isEmpty())
			{
				Double finalApprovedTotal =0d;
				Double sumInsured = bean.getSumInsured();
				for (OMPClaimCalculationViewTableDTO bean : itemIds) {
					if(bean.getCategory() == null){
	 					errorMessages.add("Please Select Category Type");
//	 					break;
					}
					if(bean.getClassification()== null){
						errorMessages.add("Please Select Classification Type");
//						break;
					}else{
						String classfication = bean.getClassification().getValue();
						doValidation(bean , classfication,dtoBean);
						if(bean.getSendforApprover()!=null && bean.getSendforApprover().equalsIgnoreCase("Y")){
							if(bean.getReject()==null){
								sumInsured = sumInsured - bean.getFinalApprovedAmtDollor();
							}
						}
						if(!bean.getIsReadOnly() && bean.getSendforApprover()!=null && bean.getSendforApprover().equalsIgnoreCase("Y")){
							if(bean.getFinalApprovedAmtDollor()!=null){
								Double finalApprovedAmtDollor = bean.getFinalApprovedAmtDollor();
								finalApprovedTotal = finalApprovedAmtDollor + finalApprovedTotal;
							}
						}
					}
					if(bean.getSubClassification()== null){
						errorMessages.add("Please Select Sub Classification Type");
					}
					if(bean.getDocRecivedFrm()== null){
						errorMessages.add("Please Select Document Recived From");
					}
					////CR20181329
					//IMSSUPPOR-29269
					if(bean.getLateDocReceivedDate() == null && bean.getProcessorStatus()==null){
						errorMessages.add("Please Select Last Document Received Date");
					}else if(!bean.getIsReadOnly()  && bean.getLateDocReceivedDate() == null && bean.getProcessorStatus()!=null && (/*(bean.getProcessorStatus().intValue() == 3181) ||*/ (bean.getProcessorStatus().intValue() == 3180))){
						errorMessages.add("Please Select Last Document Received Date");
					}
					if(bean.getCurrencyType() == null ){
						errorMessages.add("Please Select Currency Type");
					}
					if(bean.getCurrencyrate()== null){
						errorMessages.add("Please Enter Currency Rate (in to USD)");
					}
					if(bean.getViewforApprover()!= null && bean.getSendforApprover()!= null){
						if(bean.getViewforApprover().getValue().equalsIgnoreCase("No") && bean.getSendforApprover().equalsIgnoreCase("Y")){
							errorMessages.add("Please unselect send for approver or select yes to view for approver");
						}
					}
					if(bean.getSendforApprover()!= null && bean.getSendforApprover().equalsIgnoreCase("Y")&& !bean.getIsReadOnly().equals(Boolean.TRUE)){
						if(bean.getOmpPaymentDetailsList()==null|| bean.getOmpPaymentDetailsList().isEmpty()){
							errorMessages.add("Please enter payment details");
						}else{
							if(ompNewPaymentDetailTableObj!=null){
								errorMessages = ompNewPaymentDetailTableObj.validateCalculation(dtoBean);
							}
						}
					}
					
					/*if(bean.getBillAmt() == null){
						errorMessages.add("Please Enter Bill Amount (in FC)");
					}*/
					/*if(bean.getDeduction() ==null){
						errorMessages.add("Please Enter Deduction(Non Payables)");
					}*/
					/*if(bean.getCopay() ==null){
						errorMessages.add("Please Select CoPay %");
					}*/
					
				/*	if(classification!=null && classification.getValue()!=null){
						doValidation(bean, classification.getValue());
					}else{
						if(this.bean!=null && this.bean.getClassification()!=null && this.bean.getClassification().getValue()!=null){
							String classfication = this.bean.getClassification().getValue();
							doValidation(bean , classfication);
						}
					}*/
					//IMSSUPPOR-29202 added for old reject cases
					if(bean.getReject() != null && bean.getReject().equals("Y") && !(bean.getProcessorStatus()!=null && bean.getProcessorStatus().intValue() ==3181 || bean.getProcessorStatus().intValue() ==3183 )){
						if(bean.getRejectionIds() != null){
							HashSet temp = new HashSet<>((Collection) bean.getRejectionIds());
							if(temp.isEmpty()){
								errorMessages.add("Please select rejection categories for the rejected ROD.");
							}
							if(StringUtils.isBlank(bean.getRejectionRemarks())){
								errorMessages.add("Please enter rejection remarks for the rejected ROD.");
							}
						}else{
							errorMessages.add("Please provide rejection details by clicking Reject Details button.");
						}
					}
					
				}
				if(sumInsured!=null){
					if(sumInsured < finalApprovedTotal){
						if(sumInsured<0){
							sumInsured = 0d;
							errorMessages.add("Unable to process the ROD due to insufficient Balance Sum Insured = " + sumInsured);
						}
					}
				}
				
				}else{
//					errorMessages.add("Please Add Bill Details");
				}
				return errorMessages;
			
			}
	 
		private void doValidation(OMPClaimCalculationViewTableDTO bean , String classification,OMPClaimProcessorDTO dtoBean) {
			if(classification.equalsIgnoreCase("OMP Claim Related")){
				if(bean.getBillAmt() == null){
					errorMessages.add("Please Enter BillAmt(in FC)");
					//break;
				}
				if(bean.getConversionValue() == null ){
					errorMessages.add("Please Enter Conversion value (USD to INR)");
				}
				
				if(dtoBean.getIsCashless()!= null && dtoBean.getIsCashless().equals(Boolean.TRUE) 
						&& bean.getNegotiationDone()==null){
					if(bean.getRodClaimType()!=null){
						SelectValue rodClaimType = bean.getRodClaimType();
						if(rodClaimType.getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
							errorMessages.add("Please Select Negotiation Done");
						}
					}
				}
				if(bean.getFinalApprovedAmtDollor()!= null &&  bean.getFinalApprovedAmtDollor()==0){
					if(bean.getSendforApprover() != null &&  bean.getSendforApprover().equalsIgnoreCase("Y")){
						errorMessages.add("Final Approved Amount is Zero. Hence this ROD can not be Approved.");
					}
				}
			}
			if(classification.equalsIgnoreCase("OMP Other Expenses")){
				if(bean.getTotalAmtInr() == null){
					errorMessages.add("Please Enter Total Amount in Inr");
				}else if(bean.getTotalAmtInr()==0){
					if(bean.getSendforApprover() != null &&  bean.getSendforApprover().equalsIgnoreCase("Y")){
						errorMessages.add("Total Amount in Inr is Zero. Hence this ROD can not be Approved.");
					}
					
				}
			}
			if(classification.equalsIgnoreCase("Negotiator Fee")){
				if(bean.getSelect() == null){
					
					errorMessages.add("Please Select Negotiation");
				}
				if(bean.getConversionValue() == null ){
					errorMessages.add("Please Enter Conversion value (USD to INR)");
				}
			}
		}
		
		public Set<String> getErrors()
		{
			return this.errorMessages;
		}
		
		
		public ValueChangeListener copayApprovedAmtListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField copayApprovedAmt = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)copayApprovedAmt.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField afternegotiation = (TextField)hashMap.get("afternegotiation");
					TextField finalApprovedAmtDollor = (TextField)hashMap.get("finalApprovedAmtDollor");
					Double afternegotiationDouble = 0d;
					Double copayApprovedAmtDouble =0d;
					Double finalApprovedAmtDollorDouble =0d;
					
					if(copayApprovedAmt!=null && afternegotiation!=null){
							if(afternegotiation.getValue()!=null){
								afternegotiationDouble = SHAUtils.getDoubleFromStringWithComma(afternegotiation.getValue());
							}
						if(afternegotiation.getValue()!=null && afternegotiationDouble!=0){
							copayApprovedAmtDouble = SHAUtils.getDoubleFromStringWithComma(copayApprovedAmt.getValue());
							Double minOfNegoAndCopay = Math.min(afternegotiationDouble, copayApprovedAmtDouble);
							finalApprovedAmtDollorDouble = Math.min(bean.getSumInsured(),minOfNegoAndCopay);
						}else{
							copayApprovedAmtDouble = SHAUtils.getDoubleFromStringWithComma(copayApprovedAmt.getValue());
							finalApprovedAmtDollorDouble = Math.min(bean.getSumInsured(),copayApprovedAmtDouble);
						}
						if(finalApprovedAmtDollor!=null &&(!calculationViewTableDTO.getIsReadOnly() || !calculationViewTableDTO.getIsReadOnlyRecoverable())){
							finalApprovedAmtDollor.setReadOnly(Boolean.FALSE);
							finalApprovedAmtDollor.setValue(finalApprovedAmtDollorDouble.toString());
							finalApprovedAmtDollor.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
	 
		
		public ValueChangeListener afterNegotiationAmtListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					TextField afternegotiation = (TextField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)afternegotiation.getData();
	 				HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
	 				TextField copayApprovedAmt = (TextField)hashMap.get("approvedamountaftecopay");
					TextField finalApprovedAmtDollor = (TextField)hashMap.get("finalApprovedAmtDollor");
					Double afternegotiationDouble = 0d;
					Double copayApprovedAmtDouble =0d;
					Double finalApprovedAmtDollorDouble =0d;
					
					if(copayApprovedAmt!=null && afternegotiation!=null){
						afternegotiationDouble = SHAUtils.getDoubleFromStringWithComma(afternegotiation.getValue());
						if(afternegotiation.getValue()!=null && afternegotiationDouble!=0){
							copayApprovedAmtDouble = SHAUtils.getDoubleFromStringWithComma(copayApprovedAmt.getValue());
							Double minOfNegoAndCopay = Math.min(afternegotiationDouble, copayApprovedAmtDouble);
							finalApprovedAmtDollorDouble = Math.min(bean.getSumInsured(),minOfNegoAndCopay);
						}else{
							copayApprovedAmtDouble = SHAUtils.getDoubleFromStringWithComma(copayApprovedAmt.getValue());
							finalApprovedAmtDollorDouble = Math.min(bean.getSumInsured(),copayApprovedAmtDouble);
						}
						if(finalApprovedAmtDollor!=null && !calculationViewTableDTO.getIsReadOnly()){
							finalApprovedAmtDollor.setReadOnly(Boolean.FALSE);
							finalApprovedAmtDollor.setValue(finalApprovedAmtDollorDouble.toString());
							finalApprovedAmtDollor.setReadOnly(Boolean.TRUE);
						}
					}
				}
			};
			
			return listener;
		}
		
		public ValueChangeListener conversionListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField txtConversion = (TextField) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)txtConversion.getData();
								Double conversionValue =0d;
								Double finalApprovedAmtDollorDouble =0d;
								Double finalApprovedAmtInrDouble =0d;
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								if(hashMap!=null){
								TextField finalApprovedAmtDollor = (TextField)hashMap.get("finalApprovedAmtDollor");
								if(finalApprovedAmtDollor!=null && finalApprovedAmtDollor.getValue()!=null){
									finalApprovedAmtDollorDouble = SHAUtils.getDoubleFromStringWithComma(finalApprovedAmtDollor.getValue());
								}
								if(txtConversion!=null && txtConversion.getValue()!=null){
									conversionValue = SHAUtils.getDoubleFromStringWithComma(txtConversion.getValue());
								}
								TextField finalApprovedAmtInr = (TextField)hashMap.get("finalApprovedAmtInr");
								if(finalApprovedAmtInr!=null){
									finalApprovedAmtInrDouble = finalApprovedAmtDollorDouble * conversionValue;
									finalApprovedAmtInr.setReadOnly(Boolean.FALSE);
									finalApprovedAmtInr.setValue(finalApprovedAmtInrDouble.toString());
									finalApprovedAmtInr.setReadOnly(Boolean.TRUE);
								}
								}
					}
				};
			return listener;
		}
		
		
		public ValueChangeListener finalApprovedAmtDollorListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextField finalApprovedAmtDollor = (TextField) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)finalApprovedAmtDollor.getData();
								Double conversionValue =0d;
								Double finalApprovedAmtDollorDouble =0d;
								Double finalApprovedAmtInrDouble =0d;
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								if(hashMap!=null){
								TextField conversionValueTxt = (TextField)hashMap.get("conversionValue");
								if(conversionValueTxt!=null && conversionValueTxt.getValue()!=null){
									conversionValue = SHAUtils.getDoubleFromStringWithComma(conversionValueTxt.getValue());
								}
								if(finalApprovedAmtDollor!=null && finalApprovedAmtDollor.getValue()!=null){
									finalApprovedAmtDollorDouble = SHAUtils.getDoubleFromStringWithComma(finalApprovedAmtDollor.getValue());
								}
								TextField finalApprovedAmtInr = (TextField)hashMap.get("finalApprovedAmtInr");
								if(finalApprovedAmtInr!=null){
									finalApprovedAmtInrDouble = finalApprovedAmtDollorDouble * conversionValue;
									finalApprovedAmtInr.setReadOnly(Boolean.FALSE);
									finalApprovedAmtInr.setValue(finalApprovedAmtInrDouble.toString());
									finalApprovedAmtInr.setReadOnly(Boolean.TRUE);
								}
								}
					}
				};
			return listener;
		}
		
		
		
		
		public ValueChangeListener rodListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						final TextField txtRodNo = (TextField) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)txtRodNo.getData();
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
						rodButton = new Button();
						rodButton.setData(txtRodNo);
						rodButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)txtRodNo.getData();
								HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
								//fireViewEvent(OMPClaimProcessorPagePresenter.OMP_PROCESS_NEGOTIATION_SAVE, bean,calculationViewTableDTO);
								TextField afternegotiation = (TextField)hashMap.get("afternegotiation");
								
								
//								fireViewEvent(MenuItemBean.OMP_PROCESS_OMP_CLAIM_PROCESSOR, null);
								
							}
						});		
		 				
					}
				};
			return listener;
		}
		
		public void enableDisableField(Boolean false1) {
			List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
			for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : dtoList) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(ompClaimCalculationViewTableDTO);
				if(hashMap!=null){
					GComboBox negotiationDone = (GComboBox)hashMap.get("negotiationDone");
					if(negotiationDone!=null){
						negotiationDone.setEnabled(false1);
						if(false1==Boolean.FALSE && yesOrNoContainer!=null && yesOrNoContainer.getItemIds().size()>0){
							negotiationDone.setEnabled(Boolean.TRUE);
							SelectValue selectValue = yesOrNoContainer.getItemIds().get(1);
							negotiationDone.setValue(selectValue);
							negotiationDone.setEnabled(false1);
						}
					}
				}
			}
			
		}
		
		public void enableCopayDisableField(Boolean false1) {
			List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
			for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : dtoList) {
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(ompClaimCalculationViewTableDTO);
				if(hashMap!=null){
					GComboBox negotiationDone = (GComboBox)hashMap.get("copay");
					if(negotiationDone!=null){
						negotiationDone.setValue(null);
						negotiationDone.setEnabled(false1);
					}
				}
			}
		}
		
		
		public boolean validatePage(Boolean true1) {
//			Boolean hasError = false;
//			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			Boolean hasError = false;
			String eMsg = "";	
	/*	if(ompNewPaymentDetailTableObj!= null){
			Set<String> errors = ompNewPaymentDetailTableObj.validateCalculation();
			if(null != errors && !errors.isEmpty()){
				for (String error : errors) {
					eMsg += error + "</br>";
					hasError = true;
//					break;
				}
				
			}
		}*/
		if(ompNewRecoverableTableObj!= null){
			Set<String> recoErrors = ompNewRecoverableTableObj.validateCalculation();
			if(null != recoErrors && !recoErrors.isEmpty()){
				for (String error : recoErrors) {
					eMsg += error + "</br>";
					hasError = true;
//					break;
				}
				
			}
		}
			
				if (hasError) {
					setRequired(true);
					Label label = new Label(eMsg, ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);

					hasError = true;
					return hasError;
				
				}else{
//					errorMessages.add("Please Add Bill Details");
				}
				return false;
			
			}
		public boolean validatePagepayment(Boolean true1) {
//			Boolean hasError = false;
//			showOrHideValidation(true);
			errorMessages.removeAll(getErrors());
			Boolean hasError = false;
			String eMsg = "";	
		if(ompNewPaymentDetailTableObj!= null){
			Set<String> errors = ompNewPaymentDetailTableObj.validateCalculation(bean);
			if(null != errors && !errors.isEmpty()){
				for (String error : errors) {
					eMsg += error + "</br>";
					hasError = true;
//					break;
				}
				
			}
		}
		if(nomineeDetailsTable != null && bean.getPatientStatus() != null && bean.getPatientStatus().getValue() != null){
			if((bean.getPatientStatus().getValue().toString()).equals("Deceased")){
				boolean isNomineeSelected = false;
				List<NomineeDetailsDto> listOfNomieez = nomineeDetailsTable.getTableList();
				//if(listOfNomieez != null && !listOfNomieez.isEmpty()){
				for(NomineeDetailsDto rec : listOfNomieez){
					if(rec.getSelectedNomineeFlag().equals("Y")){
						isNomineeSelected = true;
						break;
					}else{
						continue;
					}
				}

				if(!isNomineeSelected){
					Map<String, String> nomineeDetails  = nomineeDetailsTable.getLegalHeirDetails();
					if(StringUtils.isBlank(nomineeDetails.get("FNAME"))){
						eMsg = "Please provide nominee Details </br>";
						hasError = true;
					}
					if(StringUtils.isBlank(nomineeDetails.get("ADDR"))){
						eMsg = "Please provide nominee Details </br>";
						hasError = true;
					}
				}
				//}
			}
		}

			
				if (hasError) {
					setRequired(true);
					Label label = new Label(eMsg, ContentMode.HTML);
					label.setStyleName("errMessage");
					VerticalLayout layout = new VerticalLayout();
					layout.setMargin(true);
					layout.addComponent(label);

					ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);

					hasError = true;
					return hasError;
				
				}else{
//					errorMessages.add("Please Add Bill Details");
				}
				return false;
			
			}
		@SuppressWarnings("unused")
		private void setRequired(Boolean isRequired) {
		
			if (!mandatoryFields.isEmpty()) {
				for (int i = 0; i < mandatoryFields.size(); i++) {
					AbstractField<?> field = (AbstractField<?>) mandatoryFields
							.get(i);
					field.setRequired(isRequired);
				}
			}
		}
		
		

		public void setClassificationField() {

			List<OMPClaimCalculationViewTableDTO> dtoList = (List<OMPClaimCalculationViewTableDTO>)table.getItemIds();
			for (OMPClaimCalculationViewTableDTO ompClaimCalculationViewTableDTO : dtoList) {
				if(!ompClaimCalculationViewTableDTO.getIsReadOnly()){
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(ompClaimCalculationViewTableDTO);
					if(hashMap!=null){
					GComboBox classification = (GComboBox)hashMap.get("classification");
					if(classification!=null && classification.getValue()!=null){
						SelectValue value = (SelectValue) classification.getValue();
						classification.setValue(null);
						classification.setValue(value);
					}
				}}
			}
		}
		
		public ValueChangeListener categoryListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						GComboBox cmbDocumentRec = (GComboBox) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)cmbDocumentRec.getData();
						HashMap<String, AbstractField<?>> combos = tableItem.get(calculationViewTableDTO);
						SelectValue value2 = (SelectValue) cmbDocumentRec.getValue();
						if(value2!=null && value2.getValue()!=null && value2.getValue().equalsIgnoreCase(SHAConstants.OMP_CATEGORY_MAIN_BILL)){
							if(tableItemchk!=null){
								CheckBox checkBox = tableItemchk.get(calculationViewTableDTO);
								if(checkBox!=null){
									checkBox.setValue(Boolean.FALSE);
								}
							}
					}
				}
			};
		return listener;
	}
		
		public ValueChangeListener rodClaimListener()
		{
		 ValueChangeListener listener = new ValueChangeListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						GComboBox cmbDocumentRec = (GComboBox) event.getProperty();
						OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)cmbDocumentRec.getData();
						HashMap<String, AbstractField<?>> combos = tableItem.get(calculationViewTableDTO);
						if(cmbDocumentRec!=null && cmbDocumentRec.getValue()!=null && combos!=null){
							SelectValue value2 = (SelectValue) cmbDocumentRec.getValue();
								if(value2.getValue()!=null){
									fireViewEvent(OMPClaimProcessorPagePresenter.OMP_CLAIM_PROCESSOR_PAYMENT_CURRENCY,calculationViewTableDTO);
								}
								
							}
							
						}
				};
			return listener;
		}
		
		private Double addRecovarableCopay(
				List<OMPNewRecoverableTableDto> ompRecoverableTableList,
				Double totalAmtDouble,
				TextField approvedamountaftecopay) {
			if(approvedamountaftecopay.getValue()!=null){
				String approvedamountaftecopayStr = approvedamountaftecopay.getValue();
				Double totalAmtDouble1 = SHAUtils.getDoubleFromStringWithComma(approvedamountaftecopayStr);
				if(ompRecoverableTableList!=null && ompRecoverableTableList.size()>0){
					OMPNewRecoverableTableDto ompNewRecoverableTableDto = ompRecoverableTableList.get(0);
					if(ompNewRecoverableTableDto.getSendToAccounts()!=null 
							&& ompNewRecoverableTableDto.getSendToAccounts().equalsIgnoreCase("Y")){
						totalAmtDouble = totalAmtDouble + ompNewRecoverableTableDto.getAmountRecoveredUsd();
					}
				}
			}
			return totalAmtDouble;
		}
		
		private Double addRecovarableCopaySave(
				List<OMPNewRecoverableTableDto> ompRecoverableTableList,
				Double totalAmtDouble,
				TextField approvedamountaftecopay) {
			if(approvedamountaftecopay.getValue()!=null){
				String approvedamountaftecopayStr = approvedamountaftecopay.getValue();
				totalAmtDouble = SHAUtils.getDoubleFromStringWithComma(approvedamountaftecopayStr);
				if(ompRecoverableTableList!=null && ompRecoverableTableList.size()>0){
					OMPNewRecoverableTableDto ompNewRecoverableTableDto = ompRecoverableTableList.get(0);
					if(ompNewRecoverableTableDto.getSendToAccounts()!=null 
							&& ompNewRecoverableTableDto.getSendToAccounts().equalsIgnoreCase("Y")){
						totalAmtDouble = totalAmtDouble + ompNewRecoverableTableDto.getAmountRecoveredUsd();
					}
				}
			}
			return totalAmtDouble;
		}
		
		public ValueChangeListener lastDocGenDateListener(){
			ValueChangeListener listener = new ValueChangeListener() {
				private static final long serialVersionUID = 1L;
				@Override
				public void valueChange( ValueChangeEvent event ) {
					DateField dateLDR = (DateField) event.getProperty();
					OMPClaimCalculationViewTableDTO calculationViewTableDTO = (OMPClaimCalculationViewTableDTO)dateLDR.getData();
					if(dateLDR.getValue() != null){
						calculationViewTableDTO.setLateDocReceivedDate(dateLDR.getValue());
					}
				}
			};
			return listener;
		}
		
		public void refreshTable(){
			table.refreshRowCache(); 
		}

		//CR2019041
		private ValueChangeListener addListenerForHandlingChanrgs()
		{
				ValueChangeListener listener = new ValueChangeListener() {
					
					private static final long serialVersionUID = 7455756225751111662L;
				
					@Override
					public void valueChange(ValueChangeEvent event) {
						 final TextField handlingch = (TextField) event.getProperty();
						if(handlingch!=null && handlingch.getValue()!=null){
						OMPClaimCalculationViewTableDTO dto = (OMPClaimCalculationViewTableDTO)handlingch.getData();
						
						HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
						TextField handling = (TextField)hashMap.get("handlingCharges");
						TextField expecesAmt = (TextField)hashMap.get("expenses");
						TextField totexpecesAmt = (TextField)hashMap.get("totalExp");
						if(handling!=null && handling.getValue()!=null&&  expecesAmt!=null && expecesAmt.getValue()!=null && totexpecesAmt!=null){
							totexpecesAmt.setReadOnly(Boolean.FALSE);
							totexpecesAmt.setValue("0");
							  
							double totexpecesAmtalue =0d;
						/*	double  handlingnumber = Double.parseDouble(handling.getValue());
							double	expecesAmtnumber = Double.parseDouble(expecesAmt.getValue());*/
							double  handlingnumber = (SHAUtils.getDoubleFromStringWithComma(handling.getValue()));
							double	expecesAmtnumber = (SHAUtils.getDoubleFromStringWithComma(expecesAmt.getValue()));
							totexpecesAmtalue =(handlingnumber + expecesAmtnumber);
							
							String totAmt =String.valueOf(totexpecesAmtalue);
							totexpecesAmt.setValue((totAmt));
							totexpecesAmt.setReadOnly(Boolean.TRUE);
							}
						}
					}
				};
				return listener;
			}//CR2019041
}
