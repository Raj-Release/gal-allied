/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.updatepayment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.paymentprocess.createbatch.search.PendingLotBatchReportDto;
import com.shaic.reimbursement.paymentprocess.createbatch.search.UpdateHoldRemarks;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
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
public class UpdatePaymentDetailViewImpl extends AbstractMVPView implements UpdatePaymentDetailView{

	
	@Inject
	private UpdatePaymentDetailForm  searchForm;
	
	/*@Inject
	private SearchCreateBatchTable searchResultTable;*/
	
	@Inject
	private UpdatePaymentDetailListenerTable searchResultTable;
	
	
	@Inject
	private UpdatePaymentDetailTableForExcel tableForExcel;
	
	@Inject
	private UpdatePaymentDetailForsuccessLayout successTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	
	private Button holdOrPendingBtn;
	
	private Button btnUpdate;
	
	private Button btnSave;

	
	private Button btnCancel;
	
	private HorizontalLayout btnLayout;
	
	
	private Button btnGenerateExcel;
	
	private CheckBox chkBox;
	
	//private TextField txtPenalIntrest;
	
	//private TextField txtTatDays;
	
	private VerticalLayout secondLayout = null;
	
	private ExcelExport excelExport;
	
	private CheckBox selectAllChkBox;
	
	private List<UpdatePaymentDetailTableDTO> finalDataList = null;
	
	
	@Inject
	private UpdatePaymentDetailTableForPopUp showDetailsTable;
	
	private VerticalLayout showDetailLayout;
	
	private Button btnShowDetailsGenerateExcel;
	
	private TextField txtBatchNoFld;
	
	private HorizontalLayout hShowDetailLayout ;
	
	private List<CreateAndSearchLotTableDTO> requestTableListForShwDetails;
	

	//private String penalIntrestRate = "";
	
	private HorizontalLayout hLayout;
	
	private Window popup;
	
	@Inject
	private UpdateHoldRemarks updateHoldRemarksUI;
	
	
	//private Label totalRocordsTxt;
	private int iSlNo = 1;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		//searchResultTable.init("", false, true);
		searchResultTable.init("", true);
		searchResultTable.addSearchListener(this);
		tableForExcel.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(buildSecondComponent());
		buildShowDetailsLayout();
		mainPanel.setSplitPosition(37);
		setHeight("100%");
		setHeight("650px");
		setCompositionRoot(mainPanel);
		
		searchForm.addSearchListener(this);
		
		
		successTable.init("", false, false);

		finalDataList = new ArrayList<UpdatePaymentDetailTableDTO>();
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
					//Boolean chkBoxValue = false;
				//	List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
					//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
					//if(null != chkBox && chkBox.getValue())
					//if(chkBoxValue || (null != chkBox && chkBox.getValue()))
						getTableDataForShowDetailsReport();

						//secondLayout.addComponent(tableForExcel);
						tableForExcel.setVisible(false);
						excelExport = new  ExcelExport(showDetailsTable.getTable());
						//excelExport = new  ExcelExport(searchResultTable.getTable());
						excelExport.excludeCollapsedColumns();
						excelExport.setReportTitle("Update Payment Detail");
						excelExport.setDisplayTotals(false);
						excelExport.export();
						//chkBox.setValue(null); 
					}
			
		});
	}
	
	
	@Override
	public void resetView() {
		searchResultTable.resetTableDataList();
		searchForm.refresh(); 
		/*if(null != txtPenalIntrest && null != txtPenalIntrest.getValue())
		{
			txtPenalIntrest.setValue(null);
		}*/
		//fireViewEvent(SearchCreateBatchPresenter.RESET_SERIAL_NO, null,null);
		iSlNo = 1;
		
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


		//btnGenerateExcel.setDisableOnClick(true);
		//btnGenerateExcel.setEnabled(false);
		chkBox = new CheckBox();
		
		chkBox.setEnabled(false);
		
		selectAllChkBox = new CheckBox("Select All");
		selectAllChkBox.setEnabled(false);
		
		FormLayout formLayout = new FormLayout(chkBox);
		formLayout.setMargin(false);
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		formLayout1.setMargin(false);
		
		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);		
	
		
		FormLayout formLayout2 = new FormLayout(selectAllChkBox);
		formLayout2.setMargin(false);
		formLayout2.setSpacing(true);
		
		//txtPenalIntrest = new TextField();
		//txtPenalIntrest.setCaption("Penal Intrest Rate");
	//	txtPenalIntrest.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		//txtPenalIntrest.setWidth("100px");
		//CSValidator penalInterestValidator = new CSValidator();
		//penalInterestValidator.extend(txtPenalIntrest);
	//	penalInterestValidator.setRegExp("^[0-9 .]*$");
		//penalInterestValidator.setPreventInvalidTyping(true);
		
		
		//FormLayout formLayout3 = new FormLayout(txtPenalIntrest);
		//formLayout3.setMargin(false);
		//formLayout3.setSpacing(true);
		
		/*txtTatDays = new TextField();
		txtTatDays.setCaption("IRDA TAT");
	//	txtTatDays.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtTatDays.setWidth("100px");
		txtTatDays.setEnabled(false);
		
		FormLayout formLayout4 = new FormLayout(txtTatDays);
		formLayout4.setMargin(false);
		formLayout4.setSpacing(true);*/
		
	
		
		secondLayout = new VerticalLayout();
		HorizontalLayout hLayout1 = new HorizontalLayout();	
		hLayout1.addComponents(formLayout,formLayout2,formLayout1);		
	//	hLayout1.addComponents(formLayout,formLayout2,formLayout1,formLayout3,formLayout4);		
		hLayout1.setSpacing(true);
	//	secondLayout.addComponent(new HorizontalLayout(formLayout,formLayout1));
		secondLayout.addComponent(hLayout1);
		
		
		
		secondLayout.addComponent(searchResultTable);
		
		hLayout = buildButtonsLayout(SHAConstants.CREATE_BATCH_FRESH);	
		secondLayout.addComponent(hLayout);
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
		//repaintTable(SHAConstants.CREATE_BATCH_TYPE);
		
		
		//secondLayout.addComponent(tableForExcel);
		
		return secondLayout;
		
	}
	
	private void addListener()
	{
				
		
		
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					if(value)
					{
						//btnGenerateExcel.setEnabled(true);
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}
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
					List<UpdatePaymentDetailTableDTO> requestTableList = finalDataList;		
					if(value)
					{
						//btnGenerateExcel.setEnabled(true);
						if(null != requestTableList && !requestTableList.isEmpty())
						{
							for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
								
							createAndSearchLotTableDTO.setCheckBoxStatus("true");						
								 
						}
						} 
					
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}	
						
					
					searchResultTable.setValueForSelectAllCheckBox(value);
					
				}
			}
		});
		
		final UpdatePaymentDetailViewImpl updatePaymentDetailViewImpl = this;
		
		
		holdOrPendingBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				chkBox.setValue(null);
				//chkBox.setValue(false);
				if(searchResultTable.getTableItems()!=null && searchResultTable.getTableItems().size()>0){
					
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to hold/ pending the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											//Boolean chkBoxValue = false;
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getTableItems();

											List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
												for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														
														finalListForProcessing.add(createAndSearchLotTableDTO);
													}
																			
											}
													tableForExcel.addBeanToList(requestTableList);
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												
																			// Confirmed to continue
											fireViewEvent(UpdatePaymentDetailPresenter.HOLD_PAYMENT_PROCESS, finalListForProcessing, SHAConstants.H_FLAG);
																		
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
			}else
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
				}
		});
		
		
		btnUpdate.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				chkBox.setValue(null);
				if(searchResultTable.getTableItems()!=null && searchResultTable.getTableItems().size()>0){
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to update the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											//Boolean chkBoxValue = false;
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getTableItems();

											List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
											List<UpdatePaymentDetailTableDTO> saveListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
												saveListForProcessing.addAll(requestTableList);
												for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														
														finalListForProcessing.add(createAndSearchLotTableDTO);
														saveListForProcessing.remove(createAndSearchLotTableDTO);
													}
																			
											}
													tableForExcel.addBeanToList(requestTableList);
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												
																			// Confirmed to continue
												fireViewEvent(UpdatePaymentDetailPresenter.UPDATE_PAYMENT_PROCESS, finalListForProcessing, SHAConstants.YES_FLAG);
																		
											}
											else if(null != saveListForProcessing && !saveListForProcessing.isEmpty())
											{
												
																			// Confirmed to continue
												fireViewEvent(UpdatePaymentDetailPresenter.UPDATE_PAYMENT_PROCESS, saveListForProcessing, SHAConstants.N_FLAG);
																		
											}	
											
											else
											{
												Label label = new Label("Please select a record to update", ContentMode.HTML);
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
			}else
			{
				Label label = new Label("Please select a record to update", ContentMode.HTML);
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
			}}
		});
		
		btnSave.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				chkBox.setValue(null);
				//chkBox.setValue(false);
				if(searchResultTable.getTableItems()!=null && searchResultTable.getTableItems().size()>0){
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to save the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											//Boolean chkBoxValue = false;
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getTableItems();

											List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
											List<UpdatePaymentDetailTableDTO> saveListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
												saveListForProcessing.addAll(requestTableList);
												for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														
														finalListForProcessing.add(createAndSearchLotTableDTO);
														saveListForProcessing.remove(createAndSearchLotTableDTO);
													}
																			
											}
													tableForExcel.addBeanToList(requestTableList);
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												
																			// Confirmed to continue
												fireViewEvent(UpdatePaymentDetailPresenter.UPDATE_PAYMENT_PROCESS, finalListForProcessing, SHAConstants.YES_FLAG);
																		
											}
											else if(null != saveListForProcessing && !saveListForProcessing.isEmpty())
											{
												
																			// Confirmed to continue
												fireViewEvent(UpdatePaymentDetailPresenter.UPDATE_PAYMENT_PROCESS, finalListForProcessing, SHAConstants.H_FLAG);
																		
											}	
											
											else
											{
												Label label = new Label("Please select a record to update", ContentMode.HTML);
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
			}else
			{
				Label label = new Label("Please select a record to save", ContentMode.HTML);
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
			}}
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
											//fireViewEvent(MenuItemBean.UPDATE_PAYMENT_DETAILS, null, null);
										} else {
											// User did not confirm
											dialog.setClosable(false);
											dialog.setStyleName(Reindeer.WINDOW_BLACK);
										}
									}
								});				
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
				//	List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
					List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getValues();
					for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
						
						if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
							chkBoxValue = true;
					}
					//if(null != chkBox && chkBox.getValue())
					//if(chkBoxValue || (null != chkBox && chkBox.getValue()))
					if(chkBoxValue  || (null != chkBox && null != chkBox.getValue() && chkBox.getValue()) || (null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue()) )
					{
						//getTableDataForReport();
						//secondLayout.addComponent(searchResultTable);
						//tableForExcel.setVisible(false);
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
						//excelExport = new  ExcelExport(searchResultTable.getTable());
						excelExport.excludeCollapsedColumns();
						excelExport.setReportTitle("Update Payment Detail");
						excelExport.setDisplayTotals(false);
						excelExport.export();
						//chkBox.setValue(null);
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
		
		
	}
	
	public void moveToHoldRecords(String holdRemarks) {
		// Confirmed to continue
		//Boolean chkBoxValue = false;
		//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
		List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getTableItems();

		List<UpdatePaymentDetailTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<UpdatePaymentDetailTableDTO>();
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
				
				/*if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					
					createAndSearchLotTableDTO.setRemarksForHold(holdRemarks);
					finalListForProcessing.add(createAndSearchLotTableDTO);
				}*/
										
		}
				tableForExcel.addBeanToList(requestTableList);
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			
										// Confirmed to continue
		fireViewEvent(UpdatePaymentDetailPresenter.CREATE_BATCH_GENERATE_BATCH_NO_FOR_PAYMENT_PROCESSING, requestTableList, SHAConstants.HOLD_PENDING_SERVICE);
									
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
			List<UpdatePaymentDetailTableDTO> requestTableList = searchResultTable.getValues();
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						tableForExcel.addBeanToList(createAndSearchLotTableDTO);
					}
				}
				
				tableForExcel.addBeanToList(requestTableList);
			}	
		}
		
	}
	
	private void getTableDataForShowDetailsReport()
	{
		if(null != showDetailsTable)
		{
			showDetailsTable.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			//List<CreateAndSearchLotTableDTO> requestTableList = showDetailsTable.getTableItems();
			if(null != requestTableListForShwDetails && !requestTableListForShwDetails.isEmpty())
			{
				/*for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						tableForExcel.addBeanToList(createAndSearchLotTableDTO);
					}
				}*/
				
				showDetailsTable.addBeanToListForShowDetails(requestTableListForShwDetails);
			}	
		}
		
	}
	
	private HorizontalLayout buildButtonsLayout(String layoutType)
	{
		
		holdOrPendingBtn = new Button();
		holdOrPendingBtn.setCaption("Hold / Pending");
		//Vaadin8-setImmediate() holdOrPendingBtn.setImmediate(true);
		holdOrPendingBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		holdOrPendingBtn.setWidth("-1px");
		holdOrPendingBtn.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() holdOrPendingBtn.setImmediate(true);
		
		btnUpdate = new Button();
		btnUpdate.setCaption("Update");
		//Vaadin8-setImmediate() btnUpdate.setImmediate(true);
		btnUpdate.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnUpdate.setWidth("-1px");
		btnUpdate.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() btnUpdate.setImmediate(true);
		
		
		btnSave = new Button();
		btnSave.setCaption("Save");
		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.setWidth("-1px");
		btnSave.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		/*FormLayout btnFormLayout = new FormLayout(btnSendToChecker);
		FormLayout btnFormLayout1 = new FormLayout(btnCancel);
		btnLayout.addComponent(btnFormLayout);
		btnLayout.addComponent(btnCancel);*/
		btnLayout = new HorizontalLayout();
		btnLayout.setSpacing(true);
		
		if((SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(layoutType))
		btnLayout.addComponent(holdOrPendingBtn);
		btnLayout.addComponent(btnUpdate);
		btnLayout.addComponent(btnSave);
		btnLayout.addComponent(btnCancel);				
		
		return btnLayout; 
	}

	@Override
	public void doSearch() {
		
		//chkBox.setValue(null);
		//selectAllChkBox.setValue(null);
		
		String err = searchForm.validate();
		if(null == err)
		{
			
			UpdatePaymentDetailFormDTO searchDTO = searchForm.getSearchDTO();
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(UpdatePaymentDetailPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
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
		List<UpdatePaymentDetailTableDTO> resetTableList = searchResultTable.getTableItems();		
		for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : resetTableList) {
			
			//createAndSearchLotTableDTO.setNoofdaysexceeding(createAndSearchLotTableDTO.getNoOfDaysExceedingforCalculation());
			//createAndSearchLotTableDTO.setIntrestAmount(createAndSearchLotTableDTO.getInterestAmntForCalculation());
			//fireViewEvent(CreateAndSearchLotPresenter.RESET_INTEREST_RATE, createAndSearchLotTableDTO, null);
			
			//txtPenalIntrest.setValue(createAndSearchLotTableDTO.getInterestRateForCalculation().toString());
		}
		searchResultTable.resetTable();	
		resetTableList.clear();
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
					if(vComp instanceof UpdatePaymentDetailTable)
					{
						((UpdatePaymentDetailTable) vComp).removeRow();
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
			BeanItemContainer<SpecialSelectValue> product) {
		
		searchForm.setDropDownValues(type, cpuCode, claimant, claimType, paymentStatus,nonKeralaCpu,batchType,zoneType,product);
		searchResultTable.setDropDownValues(paymentStatus,cpuCode);
		//penalIntrestRate = lotTableDto.getIntrestRate().toString();
	}
	
	@Override
	public void getPenalInterestRate(CreateAndSearchLotTableDTO tableDto) {
		
		//txtPenalIntrest.setValue(tableDto.getIntrestRate().toString());
		//txtTatDays.setValue(BPMClientContext.getIRDATATDays());
		UpdatePaymentDetailFormDTO searchDTO = searchForm.getSearchDTO();
		searchDTO.setPenalInterest(tableDto.getIntrestRate());
	//	searchDTO.setIrdaTAT(Integer.valueOf(BPMClientContext.getIRDATATDays()));
		
	//	fireViewEvent(SearchCreateBatchPresenter.SET_INTEREST_RATE, txtPenalIntrest, txtTatDays);
	}
	
	public void buildCreatePendingBatchLayout(List<PendingLotBatchReportDto> pendingList){
		searchForm.buildCreatePendingBatchLayout(pendingList);
	}
	
	@Override
	public void list(Page<UpdatePaymentDetailTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			chkBox.setEnabled(true);
			selectAllChkBox.setEnabled(true);
			btnGenerateExcel.setEnabled(true);
			tableRows.getPageItems();		
			List<UpdatePaymentDetailTableDTO> listOfRecords = tableRows.getPageItems();
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
				for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : listOfRecords) {
					//createAndSearchLotTableDTO.setSerialNo(iSlNo);
					iSlNo++;
				}
				
			}
			searchResultTable.setTableList(listOfRecords,SHAConstants.CREATE_BATCH_TYPE);
			searchResultTable.sortTableList();

			/**
			 * The below will add the records selected in each page  
			 * to a global list variable which is present in table.
			 * This is useful if the user wants to send all the records which
			 * ever he has selected in each page for processing.
			 * 
			 * Added for issue 192.
			 * 
			 * */
			
			searchResultTable.setFinalTableList(tableRows.getTotalList());
			
			searchResultTable.tablesize();
			
			List<UpdatePaymentDetailTableDTO> tableList = searchResultTable.getTableItems();	
			
			for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : tableList) {
				
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
			}
			searchResultTable.setHasNextPage(tableRows.isHasNext());
			
			if(null != selectAllChkBox && null != selectAllChkBox.getValue())
			{				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
				
			}
			
			finalDataList = (List<UpdatePaymentDetailTableDTO>)tableRows.getTotalList();
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
			Button homeButton = new Button("Update Payment Detail");
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
					//fireViewEvent(MenuItemBean.UPDATE_PAYMENT_DETAILS, null);
					
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
		if((SHAConstants.HOLD).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType)))
		{	
		
		//btnLayout.addComponent(holdOrPendingBtn);
		btnLayout.addComponent(btnUpdate);
		//btnLayout.addComponent(btnCreateBatch);
		btnLayout.addComponent(btnCancel);
		}
		else if((SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType))
				|| (SHAConstants.PAYMENT_STATUS_CORRECTION).equalsIgnoreCase(layoutType) && ((SHAConstants.CREATE_BATCH_TYPE).equals(batchType)))
		{
			
			btnLayout.addComponent(holdOrPendingBtn);
			btnLayout.addComponent(btnSave);
			btnLayout.addComponent(btnCancel);
		}
		else
		{
			
			btnLayout.addComponent(btnCancel);
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
		/*secondLayout.removeComponent(btnGenerateExcel);
		secondLayout.removeComponent(chkBox);
		secondLayout.removeComponent(selectAllChkBox);*/
		//txtPenalIntrest.setVisible(false);
	//	txtTatDays.setVisible(false);
		btnGenerateExcel.setVisible(false);
		chkBox.setVisible(false);
		selectAllChkBox.setVisible(false);
		}
		else
		{
			//txtPenalIntrest.setVisible(true);
		//	txtTatDays.setVisible(true);
			btnGenerateExcel.setVisible(true);
			chkBox.setVisible(true);
			selectAllChkBox.setVisible(true);
		}
		chkBox.setValue(null);
		resetSearchResultTableValues();
	}


	@Override
	public void buildSuccessLayout(String strRecMessage) {

		Label successLabel = new Label("<b style = 'color: green;'>"  + strRecMessage + "</b>",
				ContentMode.HTML);
		
		Button homeButton = new Button("Update Payment Detail");
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

				//fireViewEvent(MenuItemBean.UPDATE_PAYMENT_DETAILS, null, null);
				
			}
		});
		
	}
	
	@Override
	public void showClaimsDMS(String url) {
		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,800,BorderStyle.NONE);

		// TODO Auto-generated method stub
		
	}
	
	private void getCompleteTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			List<UpdatePaymentDetailTableDTO> requestTableList = finalDataList;
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				for (UpdatePaymentDetailTableDTO createAndSearchLotTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						
						tableForExcel.addBeanToList(createAndSearchLotTableDTO);
					}
				}
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
	//	showDetailsTable.setTableList(tableDTOList);
		showDetailsTable.init("", false, false);
		showDetailsTable.setTableData(tableDTOList);
		showDetailLayout.addComponent(showDetailsTable);
		showDetailLayout.addComponent(tableForExcel);	
		tableForExcel.setVisible(false);
		
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
				
				
				
				//System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView) {
		searchResultTable.populatePreviousPaymentDetails(tableDTO,editPaymentView);
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
			//secondLayout.removeAllComponents();
			secondLayout.removeComponent(searchResultTable);
		}
		//secondLayout.removeComponent(searchResultTable);
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
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		searchResultTable.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

	}

	@Override
	public void setPaymentCpu(
			UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO) {
		searchResultTable.setPaymentCpuName(updatePaymentDetailTableDTO);
		
	}

	@Override
	public void buildSuccessLayout(Map<String, Object> createLotMapper) {
		// TODO Auto-generated method stub
		
	}
	
	/*@Override
	public void buildResultantTableLayout(String layoutType) {

		resetSearchResultTableValues();
		searchForm.resetFlds();
		searchResultTable.compMap=null;
		chkBox.setValue(null);
		//resetView();
		if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(layoutType)){
				searchResultTable.setVisiblecolumnForCreateBatch();
			}
			else if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(layoutType)){
				searchResultTable.setVisibleColumnForSearchBatch();
			}
		
		if (null != mainPanel
				&& mainPanel.getComponentCount() > 0) {
			mainPanel.removeAllComponents();
		}
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(buildButtonLayoutBasedOnType(layoutType));
		
				
	}
	
	private VerticalLayout buildButtonLayoutBasedOnType(String layoutType)
	{
		if (null != secondLayout
				&& secondLayout.getComponentCount() > 0) {
			secondLayout.removeAllComponents();
		}
		
		FormLayout formLayout = new FormLayout(chkBox);
		formLayout.setMargin(false);
		
		
		
		
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		formLayout1.setMargin(false);

		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);
		
		FormLayout formLayout2 = new FormLayout(selectAllChkBox);
		formLayout2.setMargin(false);
		formLayout2.setSpacing(true);
		
		
		
		
		secondLayout = new VerticalLayout();
		//secondLayout.addComponent(new HorizontalLayout(formLayout,formLayout1));
		secondLayout.addComponent(new HorizontalLayout(formLayout,formLayout2,formLayout1));
		secondLayout.addComponent(searchResultTable);
		
		//FormLayout fLayout = new FormLayout();
		HorizontalLayout hLayout  = null;
		if(null == layoutType)
		{
			hLayout = buildButtonsLayout(layoutType);
		}
		else
		{
			hLayout = new HorizontalLayout();
			hLayout.setSpacing(true);
			if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(layoutType)
					&& (null != freshOrHoldType && !("").equalsIgnoreCase(freshOrHoldType) && (SHAConstants.CREATE_BATCH_FRESH).equalsIgnoreCase(freshOrHoldType))){
				hLayout.addComponent(btnCreateBatch);
				hLayout.addComponent(btnCancel);
			}
			else
			{
				hLayout.addComponent(holdOrPendingBtn);
				hLayout.addComponent(btnCancel);
			}
			
			//hLayout.setComponentAlignment(fLayout, Alignment.MIDDLE_CENTER);
		}

		//HorizontalLayout hLayout = buildButtonsLayout();
		secondLayout.addComponent(hLayout);
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
		return secondLayout;
	}*/
	

}
