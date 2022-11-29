/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotFormDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.claim.rod.wizard.forms.BankDetailsTableDTO;
import com.shaic.claim.rod.wizard.tables.PreviousAccountDetailsTable;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateBatchViewImpl extends AbstractMVPView implements SearchCreateBatchView{

	
	@Inject
	private SearchCreateBatchForm  searchForm;
	
	@Inject
	private SearchCreateBatchListenerTable searchResultTable;
	
	
	@Inject
	private SearchCreateBatchTableForExcel tableForExcel;
	
	@Inject
	private SearchCreateBatchForsuccessLayout successTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	
	private Button holdOrPendingBtn;
	
	private Button unHoldBtn;
	
	private Button btnCreateBatch;

	
	private Button btnCancel;
	
	private Button btnClose;
	
	private HorizontalLayout btnLayout;
	
	
	private Button btnGenerateExcel;
	
	private CheckBox chkBox;
	
	private Label penalLabel;
	
	private TextField txtPenalIntrest;
	
	private VerticalLayout secondLayout = null;
	
	private ExcelExport excelExport;
	
	private CheckBox selectAllChkBox;
	
	private List<CreateAndSearchLotTableDTO> finalDataList = null;
	
	@Inject
	private SearchCreateBatchTableForPopUp showDetailsTable;
	
	@Inject
	private CreateBatchTableForPopUpForExcel showDetailsExcelTable;
	
	private VerticalLayout showDetailLayout;
	
	private Button btnShowDetailsGenerateExcel;
	
	private TextField txtBatchNoFld;
	
	private HorizontalLayout hShowDetailLayout ;
	
	private List<CreateAndSearchLotTableDTO> requestTableListForShwDetails;
	
	private HorizontalLayout hLayout;
	
	private Window popup;
	
	@Inject
	private UpdateHoldRemarks updateHoldRemarksUI;
	
	@Inject
	private BatchCpuCountTable batchCpuCountTable;
	
	final Window searchPopup = new com.vaadin.ui.Window();
	
	private VerticalLayout SecondVL;
	
	private int iSlNo = 1;
	
	private Button btnSave;
	
	private String menuString;
	
	 @Inject
	 private PreviousAccountDetailsTable previousAccountDetailsTable ;
	 
	 private Window populatePreviousWindowPopup;
	 private VerticalLayout previousPaymentVerticalLayout;
	
//	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init(menuString);
		searchForm.setViewImpl(this);
		searchResultTable.init("",true);
		searchResultTable.addSearchListener(this);
		tableForExcel.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(buildSecondComponent());		
		mainPanel.getSecondComponent().setVisible(false);
		buildShowDetailsLayout();
		mainPanel.setSplitPosition(39);
		setHeight("100%");
		setHeight("675px");
		setCompositionRoot(mainPanel);
		searchForm.addSearchListener(this);
		successTable.init("", false, false);
		finalDataList = new ArrayList<CreateAndSearchLotTableDTO>();
		SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
		searchDTO.setSearchTabType(SHAConstants.NORMAL_SEARCH);
		addListener();
		resetView();
	}
	
	private void buildShowDetailsLayout()
	{
		showDetailLayout = new VerticalLayout();
		hShowDetailLayout =  new HorizontalLayout();
		btnShowDetailsGenerateExcel = new Button();
		btnShowDetailsGenerateExcel.setCaption("Download to Excel");
		//Vaadin8-setImmediate() btnShowDetailsGenerateExcel.setImmediate(true);
		btnShowDetailsGenerateExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnShowDetailsGenerateExcel.setWidth("-1px");
		btnShowDetailsGenerateExcel.setHeight("-10px");
		
		txtBatchNoFld = new TextField();
		txtBatchNoFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtBatchNoFld.setWidth("800px");
		addShowDetailsBtnListener();
		
	}
	
	
	private void addShowDetailsBtnListener()
	{
		btnShowDetailsGenerateExcel.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;		
				
				@Override
				public void buttonClick(ClickEvent event) {
						getTableDataForShowDetailsReport();
						tableForExcel.setVisible(false);
						excelExport = new  ExcelExport(showDetailsExcelTable.getTable());
						excelExport.excludeCollapsedColumns();
						excelExport.setReportTitle("Create Batch Data");
						excelExport.setDisplayTotals(false);
						excelExport.export();
					}
			
		});
	}
	
	
	@Override
	public void resetView() {
		if(searchForm != null) {
			searchForm.resetIntimation();
			searchResultTable.resetTableDataList();
			searchForm.refresh();
			if(null != txtPenalIntrest && null != txtPenalIntrest.getValue())
			{
				txtPenalIntrest.setValue(null);
			}
		iSlNo = 1;
		}	
		
	}
	
	private VerticalLayout buildSecondComponent()
	{
		
		btnGenerateExcel = new Button();
		btnGenerateExcel.setCaption("Download to Excel");
		//Vaadin8-setImmediate() btnGenerateExcel.setImmediate(true);
		btnGenerateExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnGenerateExcel.setWidth("-1px");
		btnGenerateExcel.setHeight("-10px");
		btnGenerateExcel.setEnabled(false);
		chkBox = new CheckBox();
		
		chkBox.setEnabled(false);
		
		selectAllChkBox = new CheckBox("Select All");
		selectAllChkBox.setEnabled(false);
		
		penalLabel = new Label();
		
		FormLayout formLayout = new FormLayout(chkBox);
		formLayout.setMargin(false);
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		formLayout1.setMargin(false);
		
		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);		
	
		
		FormLayout formLayout2 = new FormLayout(selectAllChkBox);
		formLayout2.setMargin(false);
		formLayout2.setSpacing(true);
		
		txtPenalIntrest = new TextField();
		txtPenalIntrest.setCaption("Penal Intrest Rate");
		txtPenalIntrest.setWidth("100px");
		txtPenalIntrest.setEnabled(false);
		CSValidator penalInterestValidator = new CSValidator();
		penalInterestValidator.extend(txtPenalIntrest);
		penalInterestValidator.setRegExp("^[0-9 .]*$");
		penalInterestValidator.setPreventInvalidTyping(true);
		
		
		FormLayout formLayout3 = new FormLayout(txtPenalIntrest);
		formLayout3.setMargin(false);
		formLayout3.setSpacing(true);
		
		secondLayout = new VerticalLayout();
		HorizontalLayout hLayout1 = new HorizontalLayout();	
		hLayout1.addComponents(formLayout,formLayout2,formLayout1,formLayout3);		
		hLayout1.setSpacing(true);
		secondLayout.addComponent(hLayout1);
		
		secondLayout.addComponent(searchResultTable);
		
		hLayout = buildButtonsLayout(SHAConstants.CREATE_BATCH_FRESH);	
		
		return secondLayout;
		
	}
	
	private void addListener()
	{
				
		
		txtPenalIntrest.addValueChangeListener(new ValueChangeListener() {
			
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {	
			
				List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
				txtPenalIntrest.setData(requestTableList);	
				String value = (String)event.getProperty().getValue();
				Double doublevalue = 0d;
				SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();	
				
				if(null != value && !(value.equals("")))
				{
					doublevalue = Double.valueOf(value);
										
					searchDTO.setPenalInterest(doublevalue);	
				}
				TextField  txtPenalInterest = (TextField)event.getProperty();
				
				
				List<CreateAndSearchLotTableDTO> penalInterestList = (List<CreateAndSearchLotTableDTO>) txtPenalInterest.getData();
				
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : penalInterestList) {
					
					Double  faApprovedAmnt = null != createAndSearchLotTableDTO.getFaApprovedAmnt() ? createAndSearchLotTableDTO.getFaApprovedAmnt() : 0d;
					Double interestRate = 0d;
					String legalFlag =  null != createAndSearchLotTableDTO.getClaimDto() && null != createAndSearchLotTableDTO.getClaimDto().getLegalFlag() ? createAndSearchLotTableDTO.getClaimDto().getLegalFlag() : null;
					String docReceivedFrom = null != createAndSearchLotTableDTO.getDocReceivedFrom() ? createAndSearchLotTableDTO.getDocReceivedFrom() : null;
					if((createAndSearchLotTableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) ||
							docReceivedFrom.equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL) ||
							createAndSearchLotTableDTO.getNoofdaysexceeding() < -3 || 
							(null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
									legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG))))
					{
						interestRate = 0d;
					}
					else
					{
					interestRate = null != value ? Double.valueOf(value) : 0d;
					}
					Double noOfExceedingDays1 = null != createAndSearchLotTableDTO.getNoofdaysexceeding() ?  createAndSearchLotTableDTO.getNoofdaysexceeding() : 0d;				
				
					
					Double noOfExceedingDays = Math.abs(noOfExceedingDays1/365);					
				
					
					Double penalInterestAmount = faApprovedAmnt*(interestRate/100)*(noOfExceedingDays);
					
					Double approvedAmnt = 0d;
					if(null != penalInterestAmount)
					{
						
						double decimalNo =  penalInterestAmount*10 % 10;
						int converttoInt = (int) decimalNo;
						
						if(converttoInt >= 5)
						{
							approvedAmnt =  Math.ceil(penalInterestAmount);
						}
						else
						{
							approvedAmnt =Math.floor(penalInterestAmount);	
						}
						
					createAndSearchLotTableDTO.setIntrestAmount(Math.abs(approvedAmnt));
					createAndSearchLotTableDTO.setInterestAmntForCalculation(Math.abs(approvedAmnt));
					
					}
					Double penalTotalamnt = faApprovedAmnt+createAndSearchLotTableDTO.getIntrestAmount();
					
					double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
					int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
					
					if(converttoIntOfApproveAmnt >= 5)
					{
						createAndSearchLotTableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
					}
					
					else
					{
						createAndSearchLotTableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
					}
					createAndSearchLotTableDTO.setIntrestRate(doublevalue);
					
				}
				
				searchResultTable.init(SHAConstants.CREATE_BATCH_TYPE,true);
				
				Page pageItems = new Page<CreateAndSearchLotTableDTO>();
				pageItems.setPageItems(penalInterestList);
				searchResultTable.setTableList(penalInterestList, "");
				searchResultTable.sortTableList();
				
			
				
			}
		});
		
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					searchResultTable.setValueForCheckBox(value);
				}
			}
		});
		
		
		selectAllChkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					List<CreateAndSearchLotTableDTO> requestTableList = finalDataList;		
					if(value)
					{
						if(null != requestTableList && !requestTableList.isEmpty())
						{
							for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
								
							createAndSearchLotTableDTO.setCheckBoxStatus("true");						
								 
						}
						} 
					
					}
					if(!(menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
					searchResultTable.setValueForSelectAllCheckBox(value);
					}
					
				}
			}
		});
		
		final SearchCreateBatchViewImpl searchCreateBatchViewImpl = this;
		
		
		holdOrPendingBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Button value =  event.getButton();
				final Window popup1 = (Window) value.getData();
				chkBox.setValue(null);
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to hold the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {	
										popup = new com.vaadin.ui.Window();
										popup.setCaption("Update Hold Remarks");
										popup.setWidth("88%");
										popup.setHeight("40%");
										
                                        updateHoldRemarksUI.init(searchCreateBatchViewImpl,popup1);
                                        
										popup.setContent(updateHoldRemarksUI);
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
										UI.getCurrent().addWindow(popup);} else {
											// User did not confirm
											dialog.setClosable(false);
											dialog.setStyleName(Reindeer.WINDOW_BLACK);
										}
										
									}
								});
				
								dialog.setClosable(false);
								
			}
		});
		
		
		unHoldBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				chkBox.setValue(null);
				Button value =  event.getButton();
				final Window popup = (Window) value.getData();
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to Unhold the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											
										//	List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
											List<CreateAndSearchLotTableDTO> requestTableList = new ArrayList<CreateAndSearchLotTableDTO>();
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && selectAllChkBox.getValue())
											{
												 requestTableList = finalDataList;
											}
											else
											{												
												requestTableList = searchResultTable.getValues();
											}

											List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
												for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														
														finalListForProcessing.add(createAndSearchLotTableDTO);
													}
																			
											}
													tableForExcel.addBeanToList(requestTableList);
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();	
																			// Confirmed to continue
											fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING, requestTableList, SHAConstants.UNHOLD_SERVICE,popup,menuString,searchDTO);
																		
											}
												
											
											else
											{
												Label label = new Label("Please select a record to hold", ContentMode.HTML);
												label.setStyleName("errMessage");
												VerticalLayout layout = new VerticalLayout();
												layout.setMargin(true);
												layout.addComponent(label);
												ConfirmDialog dialog1 = new ConfirmDialog();
												dialog1.setCaption("Errors");
												dialog1.setClosable(true);
												dialog1.setContent(layout);
												dialog1.setResizable(true);
												dialog1.setModal(true);
												dialog1.show(getUI().getCurrent(), null, true);
											}
											
										} else {
											// User did not confirm
											dialog.setClosable(false);
											dialog.setStyleName(Reindeer.WINDOW_BLACK);
										}
									}
								});				
								dialog.setClosable(false);
			}
		});
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to cancel?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											searchResultTable.resetTableDataList();
											searchResultTable.resetTable();
											
//											fireViewEvent(MenuItemBean.CREATE_BATCH, null, null);
											
											if(menuString != null && menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1)) {
												
												fireViewEvent(MenuItemBean.PAYMENT_VERIFICATION_LVL1,null);
											}else if(menuString != null && menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
												fireViewEvent(MenuItemBean.PAYMENT_VERIFICATION_LVL2,null);
											}
											else{
												fireViewEvent(MenuItemBean.CREATE_BATCH, null);
											}
											
										} else {
											// User did not confirm
											dialog.setClosable(false);
											dialog.setStyleName(Reindeer.WINDOW_BLACK);
										}
									}
								});				
			}
		});
		
		
		btnCreateBatch.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Button value =  event.getButton();
				final Window popup = (Window) value.getData();
				if(searchResultTable.validatePage())
				{
				
				String err = searchResultTable.isValid();
				if(err.equalsIgnoreCase(""))
				{
				//Boolean chkBoxValue = false;
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to create batch for the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<CreateAndSearchLotTableDTO> requestTableList = new ArrayList<CreateAndSearchLotTableDTO>();
											
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && true == selectAllChkBox.getValue())
											{
												 requestTableList = finalDataList;
												 for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
														
														createAndSearchLotTableDTO.setCheckBoxStatus("true");						
															 
													}
											}
											else
											{												
												requestTableList = searchResultTable.getValues();
											}
											
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
												for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														finalListForProcessing.add(createAndSearchLotTableDTO);
													}
													
											}
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();	
												fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING, requestTableList, SHAConstants.CREATE_BATCH_SERVICE,popup,menuString,searchDTO);
											}
											
											
											else
											{
												Label label = new Label("Please select a record for batch number generation", ContentMode.HTML);
												label.setStyleName("errMessage");
												VerticalLayout layout = new VerticalLayout();
												layout.setMargin(true);
												layout.addComponent(label);
												ConfirmDialog dialog1 = new ConfirmDialog();
												dialog1.setCaption("Errors");
												dialog1.setClosable(true);
												dialog1.setContent(layout);
												dialog1.setResizable(true);
												dialog1.setModal(true);
												dialog1.show(getUI().getCurrent(), null, true);
											}
											
											
										} else {
											// User did not confirm
											dialog.setClosable(false);
											dialog.setStyleName(Reindeer.WINDOW_BLACK);
											dialog.close();
										}
									}
								});
				dialog.setClosable(false);
				}
				else
				{
					showErrorMessage(err);
				}
				
								}
			}
			
					});
		
		
		btnGenerateExcel.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		
				
		
				
				@Override
				public void buttonClick(ClickEvent event) {
					Boolean chkBoxValue = false;
				//	List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getValues();
					List<CreateAndSearchLotTableDTO> requestTableList = finalDataList;
					for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
						
						if(createAndSearchLotTableDTO.getLegalFirstName()!=null && !createAndSearchLotTableDTO.getLegalFirstName().isEmpty()){
							createAndSearchLotTableDTO.setPayeeNameStr(createAndSearchLotTableDTO.getLegalFirstName());
						}
						if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
							chkBoxValue = true;
					}
					if(chkBoxValue  || (null != chkBox && null != chkBox.getValue() && chkBox.getValue()) || (null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue()) )
					{
						if(null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue())
						{
							getCompleteTableDataForReport();
							
						}
						else
						{
							getTableDataForReport();
						}
						secondLayout.addComponent(tableForExcel);
						tableForExcel.setVisible(false);
						excelExport = new  ExcelExport(tableForExcel.getTable());
						excelExport.excludeCollapsedColumns();
						excelExport.setReportTitle("Create Batch Data");
						excelExport.setDisplayTotals(false);
						excelExport.export();
					}
					else
					{
						Label label = new Label("Please select a record for report generation", ContentMode.HTML);
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
						dialog.show(getUI().getCurrent(), null, true);
					}
			}
		});
		
		
		btnSave.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (searchResultTable.validatePageOnSave()) {
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to save the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											Boolean chkBoxValue = false;
											Boolean invalid =false;
											List<CreateAndSearchLotTableDTO> requestTableList = new ArrayList<CreateAndSearchLotTableDTO>();
											
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && true == selectAllChkBox.getValue())
											{
												 requestTableList = searchResultTable.getTableItems();
												 for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
														/*if(createAndSearchLotTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															invalid =true;
														}*/
														createAndSearchLotTableDTO.setCheckBoxStatus("true");						
															 
													}
											}
											else
											{											
												 requestTableList = searchResultTable.getTableItems();
											}
											List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
												for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														/*if(createAndSearchLotTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															//invalid =true;
														}*/
														finalListForProcessing.add(createAndSearchLotTableDTO);
													}
													
											}
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												fireViewEvent(SearchCreateBatchPresenter.SAVE_PAYMENT_DETAILS_FOR_PENAL, finalListForProcessing, "save");
												SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
												String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
												String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
												fireViewEvent(SearchCreateBatchPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
											}
											else
											{
												Label label = new Label("Please select a record to save payment details", ContentMode.HTML);
												label.setStyleName("errMessage");
												VerticalLayout layout = new VerticalLayout();
												layout.setMargin(true);
												layout.addComponent(label);
												ConfirmDialog dialog1 = new ConfirmDialog();
												dialog1.setCaption("Errors");
												dialog1.setClosable(true);
												dialog1.setContent(layout);
												dialog1.setResizable(true);
												dialog1.setModal(true);
												dialog1.show(getUI().getCurrent(), null, true);
											}
											
										} else {
											// User did not confirm
											dialog.setClosable(false);
											//dialog.setStyleName(Reindeer.WINDOW_BLACK);

										}
									}
								});
					dialog.setClosable(false);
				}
			}
		});
		
	}
	
	public void moveToHoldRecords(String holdRemarks,Window popUp) {
		
	//	List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
		List<CreateAndSearchLotTableDTO> requestTableList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && selectAllChkBox.getValue())
		{
			 requestTableList = finalDataList;
		}
		else
		{												
			requestTableList = searchResultTable.getValues();
		}

		List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
				
				if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					
					createAndSearchLotTableDTO.setRemarksForHold(holdRemarks);
					finalListForProcessing.add(createAndSearchLotTableDTO);
				}
										
		}
				tableForExcel.addBeanToList(requestTableList);
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
										// Confirmed to continue
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING, requestTableList, SHAConstants.HOLD_PENDING_SERVICE,popUp,menuString,searchDTO);
									
		}
			
		
		else
		{
			Label label = new Label("Please select a record to hold", ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog1 = new ConfirmDialog();
			dialog1.setCaption("Errors");
			dialog1.setClosable(true);
			dialog1.setContent(layout);
			dialog1.setResizable(true);
			dialog1.setModal(true);
			dialog1.show(getUI().getCurrent(), null, true);
		}
		
		if(popup != null){
			popup.close();
		}
	}
	
	public void submitClosePopUp(){
		if(popup != null){
			popup.close();
		}
	}
	
	private void getTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getValues();
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				tableForExcel.addBeanToList(requestTableList);
			}	
		}
		
	}
	
	private void getTableDataForShowDetailsReport()
	{
		if(null != showDetailsExcelTable)
		{
			showDetailsExcelTable.removeRow();
			
			if(null != requestTableListForShwDetails && !requestTableListForShwDetails.isEmpty())
			{
				showDetailsExcelTable.addBeanToListForShowDetails(requestTableListForShwDetails);
			}	
		}
		
	}
	
	public HorizontalLayout buildButtonsLayout(String layoutType)
	{
		
		btnSave = new Button();
		btnSave.setCaption("Save");
		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.setWidth("-1px");
		btnSave.setHeight("-10px");
		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		
		holdOrPendingBtn = new Button();
		holdOrPendingBtn.setCaption("Move To Hold");
		//Vaadin8-setImmediate() holdOrPendingBtn.setImmediate(true);
		holdOrPendingBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		holdOrPendingBtn.setWidth("-1px");
		holdOrPendingBtn.setHeight("-10px");

		//Vaadin8-setImmediate() holdOrPendingBtn.setImmediate(true);
		
		unHoldBtn = new Button();
		unHoldBtn.setCaption("UnHold/Move To Checker");
		//Vaadin8-setImmediate() unHoldBtn.setImmediate(true);
		unHoldBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		unHoldBtn.setWidth("-1px");
		unHoldBtn.setHeight("-10px");

		//Vaadin8-setImmediate() unHoldBtn.setImmediate(true);
		
		
		btnCreateBatch = new Button();
		
		btnCreateBatch.setCaption("Create Batch");
		
		//Vaadin8-setImmediate() btnCreateBatch.setImmediate(true);
		btnCreateBatch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnCreateBatch.setWidth("-1px");
		btnCreateBatch.setHeight("-10px");

		//Vaadin8-setImmediate() btnCreateBatch.setImmediate(true);
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		btnClose = new Button();
		btnClose.setCaption("Close");
		//Vaadin8-setImmediate() btnClose.setImmediate(true);
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.setWidth("-1px");
		btnClose.setHeight("-10px");
		//Vaadin8-setImmediate() btnClose.setImmediate(true);
		
		btnLayout = new HorizontalLayout();
		btnLayout.setSpacing(true);
		
		if((SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(layoutType))
		btnLayout.addComponent(btnCancel);		
		
		if(menuString != null && (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
			btnLayout.removeAllComponents();
			btnLayout.addComponent(btnClose);
		}
		
		return btnLayout; 
	}

	@Override
	public void doSearch() {
		
		SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
		searchDTO.setMenuString(menuString);
		
		if(menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2)) {
			searchDTO.setBatchType(new SelectValue(1l, SHAConstants.CREATE_BATCH_TYPE));
		}
		
		String err = searchForm.validate(searchDTO);
		if(null == err)
		{
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(SearchCreateBatchPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		}
		else
		{
			showErrorMessage(err);
			
		}
	}
		
		public void showErrorMessage(String eMsg) {
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


	/*@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchCreateBatchTable)
			{
				((SearchCreateBatchTable) comp).removeRow();
			}
		}
	}
	*/
	@Override
	public void resetSearchResultTableValues() {
		
		iSlNo = 1;
		searchResultTable.getPageable().setPageNumber(1);
		List<CreateAndSearchLotTableDTO> resetTableList = searchResultTable.getTableItems();		
		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : resetTableList) {
			
			createAndSearchLotTableDTO.setNoofdaysexceeding(createAndSearchLotTableDTO.getNoOfDaysExceedingforCalculation());
			createAndSearchLotTableDTO.setIntrestAmount(createAndSearchLotTableDTO.getInterestAmntForCalculation());
			fireViewEvent(CreateAndSearchLotPresenter.RESET_INTEREST_RATE, createAndSearchLotTableDTO, null);
			
			txtPenalIntrest.setValue(createAndSearchLotTableDTO.getInterestRateForCalculation().toString());
		}
		searchResultTable.resetTable();	
		searchResultTable.resetTableDataList();
		if(!resetTableList.isEmpty()){
			resetTableList.clear();
		}
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		chkBox.setEnabled(false);
		//searchResultTable.setValue(null);
		chkBox.setValue(null);
		selectAllChkBox.setValue(null);
		selectAllChkBox.setEnabled(false);
		btnGenerateExcel.setEnabled(false);
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				VerticalLayout vLayout = (VerticalLayout)comp;
				Iterator<Component> vCompIter = vLayout.getComponentIterator();
				while(vCompIter.hasNext())
				{
					Component vComp = (Component)vCompIter.next();
					if(vComp instanceof SearchCreateBatchTable)
					{
						((SearchCreateBatchTable) vComp).removeRow();
					}
				}
			
			
			}
		}
	
		
	}
	
	@Override
	public void init(BeanItemContainer<SelectValue> type,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> paymentStatus,
			BeanItemContainer<SelectValue> nonKeralaCpu,
			BeanItemContainer<SelectValue> batchType,
			BeanItemContainer<SelectValue> zoneType,
			BeanItemContainer<SpecialSelectValue> product,
			BeanItemContainer<SelectValue> paymentType,
			BeanItemContainer<SelectValue> penalDueDays,BeanItemContainer<SelectValue> VerificationType,String presenterString) {

		this.menuString = presenterString;
		initView();
		searchForm.setDropDownValues(type, cpuCode, claimant, claimType, paymentStatus,nonKeralaCpu,batchType,zoneType,product,penalDueDays,VerificationType);
		searchResultTable.setDropDownValues(cpuCode,paymentType);
		searchResultTable.paymentVerification(presenterString);
		searchForm.setMenuString(presenterString);
	}
	
	@Override
	public void getPenalInterestRate(CreateAndSearchLotTableDTO tableDto) {
		
		txtPenalIntrest.setValue(tableDto.getIntrestRate().toString());
		SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
		searchDTO.setPenalInterest(tableDto.getIntrestRate());
	}
	
	public void buildCreatePendingBatchLayout(List<PendingLotBatchReportDto> pendingList){
		searchForm.buildCreatePendingBatchLayout(pendingList);
	}
	
	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		searchResultTable.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

	}
	
	@Override
	public void list(Page<CreateAndSearchLotTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			Boolean isRecordExceed = false;
			
			List<CreateAndSearchLotTableDTO> pageItems = tableRows.getPageItems();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : pageItems) {
				if(createAndSearchLotTableDTO.getIsRecordExceed()){
					isRecordExceed = true;
					break;
				}else{
					break;
				}
			}
			if(! isRecordExceed){
				chkBox.setEnabled(true);
				selectAllChkBox.setEnabled(true);
				btnGenerateExcel.setEnabled(true);
				tableRows.getPageItems();		
				List<CreateAndSearchLotTableDTO> listOfRecords = tableRows.getPageItems();
				int pageNumber = tableRows.getPageNumber();
			
				if(listOfRecords.size() < 25)
				{
					int diffNO = ((pageNumber * 25) - 24)- (pageNumber*listOfRecords.size());
					iSlNo = (pageNumber * listOfRecords.size()) + diffNO;
				}
				else
				{
				iSlNo = (pageNumber * listOfRecords.size()) - 24;
				}
				if(null != listOfRecords && !listOfRecords.isEmpty())
				{				
					for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : listOfRecords) {
						createAndSearchLotTableDTO.setSerialNo(iSlNo);
						iSlNo++;
					}
					
				}
				/*searchResultTable.setTableList(listOfRecords,SHAConstants.CREATE_BATCH);
				searchResultTable.sortTableList();*/
				SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
				loadDataInWindow(tableRows, searchDTO.getVerificationType());
	
				/**
				 * The below will add the records selected in each page  
				 * to a global list variable which is present in table.
				 * This is useful if the user wants to send all the records which
				 * ever he has selected in each page for processing.
				 * 
				 * Added for issue 192.
				 * 
				 * */
				
				/*searchResultTable.setFinalTableList(tableRows.getTotalList());
				
				searchResultTable.tablesize();
				
				List<CreateAndSearchLotTableDTO> tableList = searchResultTable.getTableItems();	
				
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableList) {
					
				searchResultTable.setRowColor(createAndSearchLotTableDTO);
				}
				searchResultTable.setHasNextPage(tableRows.isHasNext());
				
				if(null != selectAllChkBox && null != selectAllChkBox.getValue())
				{				
				searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
					
				}
				
				finalDataList = (List<CreateAndSearchLotTableDTO>)tableRows.getTotalList();
				if(null != finalDataList)
				{
					searchResultTable.setTotalNoOfRecords(finalDataList.size());
				}*/
			}else{
				showRecordCount(pageItems);
			}

			
		}
		else
		{
			chkBox.setEnabled(false);
			btnGenerateExcel.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create Batch Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
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
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					//fireViewEvent(MenuItemBean.CREATE_BATCH, null);
					
				}
			});
		}
	}
	
	public void buildHoldPendingLayout(String layoutType,String batchType)
	{
		
		
		if (null != btnLayout
				&& btnLayout.getComponentCount() > 0) {
			btnLayout.removeAllComponents();
		}
		
		SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
		
		if((SHAConstants.HOLD).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType)))
		{	
		
		btnLayout.addComponent(btnSave);
		
		btnLayout.addComponent(unHoldBtn);
		btnLayout.addComponent(btnClose);
	
		}
		else if((SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType))
				|| ((SHAConstants.PAYMENT_STATUS_CORRECTION).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType))))
		{
			btnLayout.addComponent(btnSave);
			btnLayout.addComponent(holdOrPendingBtn);
			btnLayout.addComponent(btnCreateBatch);
			btnLayout.addComponent(btnClose);
		}
		else if(null != searchDTO && null != searchDTO.getSearchTabType() && (SHAConstants.QUICK_SEARCH.equalsIgnoreCase(searchDTO.getSearchTabType()))){
			
			btnLayout.addComponent(btnSave);
			btnLayout.addComponent(btnCreateBatch);
			btnLayout.addComponent(btnCancel);
		}
		btnLayout.setComponentAlignment(btnSave,  Alignment.MIDDLE_RIGHT);
		if(null != menuString && (menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL1) || menuString.equalsIgnoreCase(SHAConstants.PAYMENT_LVL2))) {
			btnLayout.removeComponent(btnSave);
			btnLayout.removeComponent(btnCreateBatch);
			btnLayout.removeComponent(holdOrPendingBtn);
		}
		
		chkBox.setValue(null);
		resetSearchResultTableValues();
	}
	
	@Override
	public void buildSearchBatchLayout(String layout) {
		// TODO Auto-generated method stub
		if (null != btnLayout
				&& btnLayout.getComponentCount() > 0) {
			btnLayout.removeAllComponents();
		}
		if(layout.equals(SHAConstants.SEARCH_BATCH_TYPE))
		{
		btnLayout.addComponent(btnCancel);
		txtPenalIntrest.setVisible(false);
		btnGenerateExcel.setVisible(false);
		chkBox.setVisible(false);
		selectAllChkBox.setVisible(false);
		penalLabel.setVisible(false);
		
		}
		else
		{
			txtPenalIntrest.setVisible(true);
			btnGenerateExcel.setVisible(true);
			chkBox.setVisible(true);
			selectAllChkBox.setVisible(true);
			penalLabel.setVisible(true);
		}
		chkBox.setValue(null);
		resetSearchResultTableValues();
	}
	


	@Override
	public void buildSuccessLayout(Map<String, Object> createLotMapper, final Window popUp,final SearchCreateBatchFormDTO formDTO) {

		String strBatchNo = (String)createLotMapper.get(SHAConstants.BATCH_NUMBER);
		String strRecMessage = null;
		String holdPendingFlag = (String)createLotMapper.get(SHAConstants.HOLD_PENDING_SERVICE);
		if((SHAConstants.N_FLAG).equalsIgnoreCase(holdPendingFlag))
		{
			List<CreateAndSearchLotTableDTO> tableDTO = (List<CreateAndSearchLotTableDTO>)createLotMapper.get(SHAConstants.SUCCESS_TABLE);
			
			successTable.setTableList(tableDTO);
			strRecMessage = "Accounts Batch No "+ strBatchNo +" has been created successfully !!!";
		}
		else
		{
			strRecMessage = "Selected record has been kept on hold !!!";
		}
		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("Create Batch Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout();
		
		if((SHAConstants.N_FLAG).equalsIgnoreCase(holdPendingFlag))
		{
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			layout.addComponent(successLabel);

			layout.addComponent(successTable);

			layout.addComponent(horizontalLayout);
			horizontalLayout.setComponentAlignment(homeButton,  Alignment.MIDDLE_RIGHT);
		}
		else
		{
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			layout = new VerticalLayout(successLabel, horizontalLayout);
			horizontalLayout.setMargin(true);
			horizontalLayout.setComponentAlignment(homeButton,  Alignment.MIDDLE_RIGHT);
		}
		
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
				if(null != formDTO && null != formDTO.getSearchTabType() && (SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(formDTO.getSearchTabType()))){
					fireViewEvent(MenuItemBean.CREATE_BATCH, null, null);
					 //GC
	                if(searchResultTable!=null){
	                        searchResultTable.clearObject();
//	                        searchResultTable = null;
	                }
	                UI.getCurrent().removeWindow(searchPopup);
				}
				else{
					resetSearchResultTableValues();
					searchForm.resetQuickSearchLayout();
				}
				if(null != popUp){
					popUp.close();
				}
				
				

				
			}
		});
		
	}
	
	public void buildSuccessLayoutForHoldBancs(String presenter,final Window popUp){
		String strRecMessage = null;
		
//		strRecMessage = "Accounts Batch No. has been created successfully!!!";
		
		strRecMessage = "Selected Payment records are Submitted Successfully!!!";
		
		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);

		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(hLayout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				/**
				 * Commented as per JIRA 	GALAXYMAIN-13006
				 */
//					fireViewEvent(MenuItemBean.PAYMENT_VERIFICATION_LVL1, null, null);
					dialog.close();
					
					if(null != popUp){
						popUp.close();
					}

						/**
						 *  Added as per JIRA GALAXYMAIN-13006
						 */
						fireViewEvent(SearchCreateBatchPresenter.RELOAD_SEARCH_RESULT_FOR_SUBMIT,null);
					
				
			}
		});	
		
	}
	
	
	@Override
	public void buildSuccessLayoutBancs(String presenter,final Window popUp,Integer size){
		String strRecMessage = null;
		if(presenter.equalsIgnoreCase("Submit"))
		{

//			strRecMessage = "Accounts Batch No. has been created successfully!!!";
			
			strRecMessage = "Selected Payment records are Submitted Successfully!!!";
		}
		else
		{
			strRecMessage = "Selected record has been kept on hold !!!";
		}
		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
/*		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout();
			HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			layout.addComponent(successLabel);


			layout.addComponent(horizontalLayout);
			horizontalLayout.setComponentAlignment(homeButton,  Alignment.MIDDLE_RIGHT);*/
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(hLayout);
		dialog.setResizable(true);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				/**
				 * Commented as per JIRA 	GALAXYMAIN-13006
				 */
//					fireViewEvent(MenuItemBean.PAYMENT_VERIFICATION_LVL1, null, null);
					dialog.close();
					
					if(null != popUp){
						popUp.close();
					}

					/**
					 * GALAXYMAIN-13257 Fixed on 11-02-2020
					 */
					if(size != null && size>1){
						/**
						 *  Added as per JIRA GALAXYMAIN-13006
						 */
						fireViewEvent(SearchCreateBatchPresenter.RELOAD_SEARCH_RESULT_FOR_SUBMIT,null);
					}else{
						UI.getCurrent().removeWindow(searchPopup);
						searchForm.resetIntimation();
					}
				
			}
		});	
		
	}
	
	@Override
	public void showClaimsDMS(String url) {
		getUI().getPage().open(url, "_blank",1550,800,BorderStyle.NONE);

		// TODO Auto-generated method stub
		
	}
	
	private void getCompleteTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			List<CreateAndSearchLotTableDTO> requestTableList = finalDataList;
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				tableForExcel.addFinalListToBean(requestTableList);
			}	
		}
	}

	@Override
	public void showDetails(List<CreateAndSearchLotTableDTO> tableDTOList,String batchNo) {
		
		requestTableListForShwDetails = new ArrayList<CreateAndSearchLotTableDTO>();
		
		requestTableListForShwDetails = tableDTOList;
		
		
		/**/
		if (showDetailLayout != null
				&& showDetailLayout.getComponentCount() > 0) {
			showDetailLayout.removeAllComponents();
		}
		
		if (hShowDetailLayout != null
				&& hShowDetailLayout.getComponentCount() > 0) {
			hShowDetailLayout.removeAllComponents();
		}
		String infoString = "Details of Account Batch No:"+batchNo;
		txtBatchNoFld.setValue(infoString);
		
		FormLayout fLayout = new FormLayout(txtBatchNoFld,btnShowDetailsGenerateExcel);
		hShowDetailLayout.addComponent(fLayout);
		showDetailLayout.addComponent(fLayout);
		showDetailsTable.init("", false, false);
		showDetailsExcelTable.init("", false, false);
		showDetailsTable.setTableData(tableDTOList);
		showDetailLayout.addComponent(showDetailsTable);
		showDetailLayout.addComponent(tableForExcel);	
		showDetailLayout.addComponent(showDetailsExcelTable);
		tableForExcel.setVisible(false);
		showDetailsExcelTable.setVisible(false);
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Batch Details");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(showDetailLayout);
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
				
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			CreateAndSearchLotTableDTO createBatchTableDto) {
		searchResultTable.populatePreviousPaymentDetails(tableDTO,createBatchTableDto);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intrestRateValidation() {	
	
				String err = "Penal Interest Days is more than the actual delay days + 3 Days";
				showErrorMessage(err);
			
		
	}
	@Override
	public void noOfExceedingDaysValidation() {	
	
				String err = "Days should be greater than or equal to -3";
				showErrorMessage(err);
			
		
	}

	@Override
	public void repaintTable(String layout) {
		
		if (null != secondLayout 
				&& secondLayout.getComponentCount() > 0) {
			secondLayout.removeComponent(searchResultTable);
		}
		if(layout.equals(SHAConstants.CREATE_BATCH_TYPE))
		{		
			searchResultTable.init(SHAConstants.CREATE_BATCH_TYPE,true);			
		}
		else
		{
			searchResultTable.init(SHAConstants.SEARCH_BATCH_TYPE,true);		
		}
		secondLayout.addComponent(searchResultTable);
		secondLayout.addComponent(hLayout);		
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
	}

	@Override
	public void resetSlNo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPaymentCpu(
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		searchResultTable.setPaymentCpuName(updatePaymentDetailTableDTO);
		
	}
	
	@Override
	public void buildSuccessLayout(String strRecMessage) {

		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		layout = new VerticalLayout(successLabel, horizontalLayout);
		horizontalLayout.setMargin(true);
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
				//fireViewEvent(MenuItemBean.CREATE_BATCH, null, null);
				
			}
		});
		
	}

	public void setSplitPosition(String searchType){
		
		SearchCreateBatchFormDTO searchDTO = searchForm.getSearchDTO();
		
		if(SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(searchType)){
			mainPanel.setSplitPosition(39);
			searchDTO.setSearchTabType(SHAConstants.NORMAL_SEARCH);
			hLayout.removeAllComponents();
			secondLayout.removeAllComponents();
			mainPanel.removeComponent(secondLayout);
			searchResultTable.resetTable();
			searchResultTable.resetTableDataList();
			//searchResultTable.init(SHAConstants.NORMAL_SEARCH,Boolean.FALSE);
		}
		else
		{
			mainPanel.setSplitPosition(21);
			searchDTO.setSearchTabType(SHAConstants.QUICK_SEARCH);
			VerticalLayout vLayout = buildSecondComponent();
			mainPanel.setSecondComponent(vLayout);
			txtPenalIntrest.setValue(searchDTO.getPenalInterest().toString());
			searchResultTable.resetTable();
			searchResultTable.resetTableDataList();
			searchResultTable.initPresenterString(SHAConstants.QUICK_SEARCH);
			//searchResultTable.init(SHAConstants.CREATE_BATCH, Boolean.TRUE);
			searchResultTable.init(SHAConstants.QUICK_SEARCH, Boolean.TRUE);
			searchForm.addSearchListener(this);
			hLayout.removeAllComponents();
			hLayout = buildButtonsLayout(SHAConstants.CREATE_BATCH_FRESH);	
			secondLayout.addComponent(hLayout);
			secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
			mainPanel.setSecondComponent(secondLayout);
			addListener();
		}
	}
	
	
	@Override
	public void listForQuick(Page<CreateAndSearchLotTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			chkBox.setEnabled(true);
			selectAllChkBox.setEnabled(true);
			btnGenerateExcel.setEnabled(true);
			tableRows.getPageItems();
			Long paymentStatusKey = 0l;

			List<CreateAndSearchLotTableDTO> listOfRecords = tableRows.getPageItems();
			int pageNumber = tableRows.getPageNumber();
		
			if(listOfRecords.size() < 25)
			{
				int diffNO = ((pageNumber * 25) - 24)- (pageNumber*listOfRecords.size());
				iSlNo = (pageNumber * listOfRecords.size()) + diffNO;
			}
			else
			{
			iSlNo = (pageNumber * listOfRecords.size()) - 24;
			}
			if(null != listOfRecords && !listOfRecords.isEmpty())
			{				
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : listOfRecords) {
					createAndSearchLotTableDTO.setSerialNo(iSlNo);
					iSlNo++;
				}
				
			}
			List<CreateAndSearchLotTableDTO> resultList = new ArrayList<CreateAndSearchLotTableDTO>();
			
			List<CreateAndSearchLotTableDTO> exisitingList = searchResultTable.getValues();
			List<String> existingIntimation = new ArrayList<String>();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : exisitingList) {
				existingIntimation.add(createAndSearchLotTableDTO.getIntimationNo());
				paymentStatusKey = createAndSearchLotTableDTO.getPaymentStatusKey();
			}
			resultList.addAll(exisitingList);
			List<CreateAndSearchLotTableDTO> pageItems = tableRows.getPageItems();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : pageItems) {

				
				
				if(null != exisitingList && !exisitingList.isEmpty() && paymentStatusKey != 0l)
				{
					if(paymentStatusKey.equals(createAndSearchLotTableDTO.getPaymentStatusKey())){
						if(!existingIntimation.contains(createAndSearchLotTableDTO.getIntimationNo())){
							resultList.add(createAndSearchLotTableDTO);
						}
					}
					else
					{
						String msg = "";

						if(ReferenceTable.PAYMENT_STATUS_FRESH.equals(paymentStatusKey)){
							msg = "This intimation is correction type";
						}
						else
						{
							msg = "This intimation is Fresh type";
						}
						showErrorMessage(msg);
						break;
					}
				}
				else
				{
					if(!existingIntimation.contains(createAndSearchLotTableDTO.getIntimationNo())){
						resultList.add(createAndSearchLotTableDTO);
					}

				}
			
			}
			
			searchResultTable.resetTable();
			searchResultTable.initPresenterString(SHAConstants.QUICK_SEARCH);
			searchResultTable.init(SHAConstants.QUICK_SEARCH, Boolean.TRUE);
			String vnrStatus = resultList.get(0).getVerifyAccntValue();
			searchResultTable.setvType((vnrStatus != null && vnrStatus.equalsIgnoreCase(SHAConstants.VERIFICATION_NOT_REQUIRED)) ? ReferenceTable.VERIFICATION_NOT_REQUIRED  : vnrStatus);
			
			int i=1;
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : resultList) {
				createAndSearchLotTableDTO.setSerialNo(i);
				createAndSearchLotTableDTO.setPaymentType(new SelectValue(2l,createAndSearchLotTableDTO.getPaymentTypeValue()));
				i++;
			}
			
			searchResultTable.setTableList(resultList, "");
			

			/**
			 * The below will add the records selected in each page  
			 * to a global list variable which is present in table.
			 * This is useful if the user wants to send all the records which
			 * ever he has selected in each page for processing.
			 * 
			 * Added for issue 192.
			 * 
			 * */
			
			searchResultTable.setFinalTableList(resultList);
			
			searchResultTable.tablesize();
			
			List<CreateAndSearchLotTableDTO> tableList = searchResultTable.getTableItems();	
			 
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableList) {
				
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
			}
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			if(null != selectAllChkBox && null != selectAllChkBox.getValue())
			{				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
				
			}
			
			finalDataList = (List<CreateAndSearchLotTableDTO>)resultList;
			if(null != finalDataList)
			{
				searchResultTable.setTotalNoOfRecords(finalDataList.size());
			}
			
		}
		else
		{
			chkBox.setEnabled(false);
			btnGenerateExcel.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create Batch Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
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
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				//	fireViewEvent(MenuItemBean.CREATE_BATCH, null);
					
				}
			});
		}
	}
		
	@Override
	public void resetTableView() {
		searchResultTable.resetTableDataList();
		searchForm.refresh(); 
		if(null != txtPenalIntrest && null != txtPenalIntrest.getValue())
		{
			txtPenalIntrest.setValue(null);
		}
		//fireViewEvent(SearchCreateBatchPresenter.RESET_SERIAL_NO, null,null);
		iSlNo = 1;
		
	}
	
	@Override
	public void setUpPaymentCpuCodeDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		searchResultTable.setUpPaymentCpuCodeDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

	}

	@Override
	public void setUpPayeeNameDetails(
			ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		
		searchResultTable.setUpPayeeNameDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
		
	}
	
	public void showRecordCount(List<CreateAndSearchLotTableDTO> tableDTOList) {
		
		batchCpuCountTable.init("Count For Cpu Wise", false, false);
		batchCpuCountTable.setTableList(tableDTOList);
		
		if(tableDTOList != null && ! tableDTOList.isEmpty()){
			CreateAndSearchLotTableDTO createAndSearchLotTableDTO = tableDTOList.get(0);
			batchCpuCountTable.setTotalCount(createAndSearchLotTableDTO);
		}
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("<b style = 'color: red;'>No of records exceeds 1000 !!!</b>");
		popup.setCaptionAsHtml(true);
		popup.setWidth("40%");
		popup.setHeight("60%");
		popup.setContent(batchCpuCountTable);
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
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void loadDataInWindow(Page<CreateAndSearchLotTableDTO> tableRows, SelectValue vType){
		
		searchPopup.setWidth("100%");
		searchPopup.setHeight("100%");		
		searchPopup.setCaption("Search Result");
		if(SecondVL != null){
			SecondVL.removeAllComponents();
		}
		SecondVL = new VerticalLayout();
		
		penalLabel.setWidth(null);
		
		penalLabel.setCaption("<span style='font-size:medium;font-weight:medium;'>Penal Interest Rate : "+txtPenalIntrest.getValue()+"</span>");
		penalLabel.setCaptionAsHtml(true);
		penalLabel.setCaptionAsHtml(true);
		HorizontalLayout topnestedHL = new HorizontalLayout(chkBox,selectAllChkBox,btnGenerateExcel,penalLabel);
		topnestedHL.setSpacing(true);
		topnestedHL.setMargin(true);
		topnestedHL.setHeight("25px");
		
		SecondVL.addComponent(topnestedHL);
	//	searchResultTable.init(SHAConstants.CREATE_BATCH_TYPE, false);
		if(vType != null){
			searchResultTable.setvType(vType.getValue());
		}
		searchResultTable.setTableList(tableRows.getPageItems(), "");
		searchResultTable.setFinalTableList(tableRows.getPageItems());		
		searchResultTable.tablesize();
		if(null != selectAllChkBox && null != selectAllChkBox.getValue()){				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
		}

		List<CreateAndSearchLotTableDTO> tableList = searchResultTable.getTableItems();	

		for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableList) {
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
		}
		searchResultTable.setHasNextPage(tableRows.isHasNext());
		searchResultTable.setPage(tableRows);
		finalDataList = (List<CreateAndSearchLotTableDTO>)tableRows.getTotalList();
		if(null != finalDataList){
			searchResultTable.setTotalNoOfRecords(finalDataList.size());
		}
		SecondVL.addComponent(searchResultTable);
		
		btnGenerateExcel.setEnabled(true);
		chkBox.setEnabled(true);
		selectAllChkBox.setEnabled(true);
		
		SecondVL.addComponent(btnLayout);
		SecondVL.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);

		// close button in window
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(searchPopup);
			}
		});

		SecondVL.setSpacing(true);
		searchPopup.setContent(SecondVL);
		searchPopup.setClosable(true);
		searchPopup.center();
		searchPopup.setResizable(false);
		searchPopup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				
				/**Search Listener Code is calling twice,So the table list are cleared.
				  To avoid this the below is added.**/
				searchResultTable.setIsSearchBtnClicked(false);
				//searchResultTable.clearExistingList();
			}
		});
		searchPopup.setModal(true);
		
		if(btnCreateBatch != null){
			btnCreateBatch.setData(searchPopup);
		}
		if(holdOrPendingBtn != null){
			holdOrPendingBtn.setData(searchPopup);
		}
		if(unHoldBtn != null){
			unHoldBtn.setData(searchPopup);
		}
		if(searchPopup.isAttached()){
			searchPopup.detach();
			UI.getCurrent().removeWindow(searchPopup);
		
		}
		UI.getCurrent().addWindow(searchPopup);
	}
	
	
	public void resetData(){
		if(! searchResultTable.getIsSearchBtnClicked())
			{
			searchResultTable.getPageable().setPageNumber(1);
			searchResultTable.clearExistingList();
			searchResultTable.setIsSearchBtnClicked(true);
			if(null != selectAllChkBox){
				selectAllChkBox.setValue(false);
			}
		}
	}

	@Override
	public void setUpPayableDetails(String payableName,
			CreateAndSearchLotTableDTO tableDto) {
		searchResultTable.setUpPayableAmt(payableName, tableDto);
		
	}
	
	public void populatePreviousAccntDetails(final CreateAndSearchLotTableDTO createBatchTableDto){
		
		Button bttnOk = new Button("OK");
		//Vaadin8-setImmediate() bttnOk.setImmediate(true);
		bttnOk.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		bttnOk.setWidth("-1px");
		bttnOk.setHeight("-10px");
		//btnOk.setDisableOnClick(true);
		//Vaadin8-setImmediate() bttnOk.setImmediate(true);
		
		Button bttnCancel = new Button("CANCEL");
		//Vaadin8-setImmediate() bttnCancel.setImmediate(true);
		bttnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		bttnCancel.setWidth("-1px");
		bttnCancel.setHeight("-10px");
	//	btnCancel.setDisableOnClick(true);
		//Vaadin8-setImmediate() bttnCancel.setImmediate(true);
		
		HorizontalLayout previousAccntDetailsButtonLayout = new HorizontalLayout(bttnOk,bttnCancel);
		previousPaymentVerticalLayout = new VerticalLayout();
		
		previousAccountDetailsTable.resetTableDataList();
		populatePreviousWindowPopup = new com.vaadin.ui.Window();
		populatePreviousWindowPopup.setWidth("75%");
		populatePreviousWindowPopup.setHeight("90%");
		
		previousAccountDetailsTable.init("Payment details from previous claim", false, false); 
		previousAccountDetailsTable.setPresenterStringForLotAndBatch(SHAConstants.CREATE_BATCH_PAYMENT,createBatchTableDto);
		previousPaymentVerticalLayout.removeAllComponents();
		previousPaymentVerticalLayout.addComponent(previousAccountDetailsTable);
		populatePreviousWindowPopup.setContent(previousPaymentVerticalLayout);	
		previousPaymentVerticalLayout.addComponent(previousAccntDetailsButtonLayout);
		previousPaymentVerticalLayout.setComponentAlignment(previousAccntDetailsButtonLayout, Alignment.TOP_CENTER);				
		
		
		setPreviousAccountDetailsValues(createBatchTableDto);
		populatePreviousWindowPopup.setClosable(true);
		populatePreviousWindowPopup.center();
		populatePreviousWindowPopup.setResizable(true);
		
		populatePreviousWindowPopup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		populatePreviousWindowPopup.setModal(true);
		populatePreviousWindowPopup.setClosable(true);
		
		UI.getCurrent().addWindow(populatePreviousWindowPopup);
		//btnPopulatePreviousAccntDetails.setEnabled(true);
		bttnOk.addClickListener(new ClickListener() {
					
		@Override
		public void buttonClick(ClickEvent event) {
			String err = previousAccountDetailsTable.isValidate();
			if("" == err)
				{
				populatePreviousAcntDtls(createBatchTableDto,SHAConstants.BTN_OK);
					
				}
			}
	});
			
		bttnCancel.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					populatePreviousAcntDtls(createBatchTableDto,SHAConstants.BTN_CANCEL);					
				}
			});
		}
	
	public void setPreviousAccountDetailsValues(CreateAndSearchLotTableDTO bean)
	{
		if(null != previousAccountDetailsTable)
		{
			int rowCount = 1;
			List<PreviousAccountDetailsDTO> previousListTable = bean.getPreviousAccntDetailsList();
			if(null != previousListTable && !previousListTable.isEmpty())
			{				
					for (PreviousAccountDetailsDTO previousAccountDetailsDTO : previousListTable) {
						
						previousAccountDetailsDTO.setChkSelect(false);
						previousAccountDetailsDTO.setChkSelect(null);						
						previousAccountDetailsDTO.setSerialNo(rowCount);
						previousAccountDetailsTable.addBeanToList(previousAccountDetailsDTO);
						rowCount ++ ;
					}
				
			}
			
		}
	}
	
	private void buildDialogBox(String message,final Window populatePreviousWindowPopup,final String btnName,final CreateAndSearchLotTableDTO bean,final PreviousAccountDetailsDTO previousAcntDtlsDto)
	{
		Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		Button cancelBtn = new Button("Cancel");
		cancelBtn.setStyleName(ValoTheme.BUTTON_DANGER);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		
		if(SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnName))
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.addComponent(cancelBtn);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelBtn, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			horizontalLayout.addComponent(homeButton);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
		}
		 
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		getUI().getCurrent().addWindow(dialog);
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(null != populatePreviousWindowPopup)
					populatePreviousWindowPopup.close();
				if(SHAConstants.BTN_OK.equalsIgnoreCase(btnName)){
					fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_POPULATE_PREVIOUS_ACCT_DETAILS,previousAcntDtlsDto,bean);
				}
			}
		});
		if(null != cancelBtn)
		{
			cancelBtn.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					
				}
			});
		}
	}
	
	private void populatePreviousAcntDtls(
			final CreateAndSearchLotTableDTO createBatchTableDto,String btnAction) {
		List<PreviousAccountDetailsDTO> previousAcntDetlsDTO = previousAccountDetailsTable.getTableAllItems();
		if(null != previousAcntDetlsDTO && !previousAcntDetlsDTO.isEmpty()){
			for (PreviousAccountDetailsDTO previousAcntDtlsDto : previousAcntDetlsDTO) {
				if(previousAcntDtlsDto.getChkSelect() != null && previousAcntDtlsDto.getChkSelect()){
					if(null != btnAction && SHAConstants.BTN_OK.equalsIgnoreCase(btnAction)){
						buildDialogBox("Selected Data will be populated in payment details section. Please click OK to proceeed",populatePreviousWindowPopup,SHAConstants.BTN_OK,createBatchTableDto,previousAcntDtlsDto);
						break;
					}
					else
					{
						buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL,createBatchTableDto,previousAcntDtlsDto);
						break;
					}
				}
				else
				{
					if(null != btnAction && SHAConstants.BTN_CANCEL.equalsIgnoreCase(btnAction)){
						buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL,createBatchTableDto,null);
						break;
					}
				}
			}
		}
		else
		{
			buildDialogBox("Are you sure you want to cancel",populatePreviousWindowPopup,SHAConstants.BTN_CANCEL,createBatchTableDto,null);
		}
	}
	
	@Override
	public void prevsAcntDtlsAlert() {

		Label successLabel = new Label("<b style = 'color: red;'>"  + SHAConstants.PREVIOUS_ACNT_DETAILS_ALERT + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setMargin(true);
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
				//fireViewEvent(MenuItemBean.CREATE_BATCH, null, null);
				
			}
		});
		
	}

	@Override
	public void LinkPayeeBankDetails(
			BankDetailsTableDTO viewSearchCriteriaDTO) {
		searchResultTable.linkPayeeBankDetials(viewSearchCriteriaDTO);
		
	}

	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}

}
