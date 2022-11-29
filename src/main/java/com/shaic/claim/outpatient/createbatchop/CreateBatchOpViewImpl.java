package com.shaic.claim.outpatient.createbatchop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotForm;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotFormDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotListenerTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableForExcel;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotView;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.outpatient.OPHealthCheckup;
import com.shaic.domain.outpatient.OPUploadPaymentDetails;
import com.shaic.domain.outpatient.OutpatientService;
import com.shaic.domain.preauth.ProvisionUploadHistory;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.main.navigator.ui.Toolbar;
import com.shaic.reimbursement.paymentprocess.createbatch.search.BatchCpuCountTable;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededListener;

public class CreateBatchOpViewImpl extends AbstractMVPView implements CreateBatchOpView,Receiver,SucceededListener{
	
	@Inject
	private CreateBatchOpForm lotForm;

	@Inject
	private OPCreateBatchTable searchResultTable;
	
	@Inject
	private CreateBatchOpTableForExcel tableForExcel;
	
	@Inject
	private BatchCpuCountTable batchCpuCountTable;
	
	@EJB
	private OutpatientService outPatientService;
	
	private VerticalSplitPanel mainPanel;
	
	private VerticalLayout resultTableLayout;
	
	private Button btnSendToChecker;
	
	private Button btnCancel;
	
	private Button btnSave;
	
	private HorizontalLayout btnLayout;
	
	private Button btnGenerateExcel;
	
	private Button btnUploadDocument;
	
	private CheckBox chkBox;
	
	private CheckBox selectAllChkBox;
	
	private VerticalLayout secondLayout = null;
	
	private ExcelExport excelExport;
	
	private Upload upload;
	
	private File file;
	
	private List<CreateBatchOpTableDTO> finalDataList = null;
	
	final Window popup = new com.vaadin.ui.Window();
	
	@Inject
	private Toolbar toolBar;

	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		lotForm.init();
		lotForm.setViewImpl(this);
		searchResultTable.init(SHAConstants.CREATE_LOT, Boolean.TRUE);
		tableForExcel.init("", false, false);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(lotForm);
//		buildSecondComponent();
		mainPanel.setSecondComponent(buildSecondComponent());
		mainPanel.setSplitPosition(37);
		setHeight("100%");
		setHeight("675px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		lotForm.addSearchListener(this);
		finalDataList = new ArrayList<CreateBatchOpTableDTO>();
		CreateBatchOpDTO searchDTO = lotForm.getSearchDTO();
		addListener();
		resetView();
	}
	
	@Override
	public void resetView() {
		btnGenerateExcel.setEnabled(true);
		//Added for issue 192
		searchResultTable.resetTableDataList();
		lotForm.refresh(); 
//		chkBox.setValue(null);
		
	}

	@Override
	public void doSearch() {
		CreateBatchOpDTO searchDTO = lotForm.getSearchDTO();
		String err=lotForm.validate(searchDTO);
		if(err == null)
		{		
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(CreateBatchOpPresenter.CREATE_SEARCH_OP, searchDTO,userName,passWord);
		}
		else
		{
			showErrorMessage(err);
		}
		
	}
	
	private void showErrorMessage(String eMsg) {
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

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		
		
		searchResultTable.resetTable();
		searchResultTable.resetTableDataList();

		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		btnGenerateExcel.setEnabled(true);
		/*chkBox.setEnabled(false);
		chkBox.setValue(null);
		selectAllChkBox.setValue(null);
		selectAllChkBox.setEnabled(false);*/
		lotForm.resetFlds();

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
					if(vComp instanceof CreateAndSearchLotTable)
					{
						((CreateAndSearchLotTable) vComp).removeRow();
					}
				}
			
			
			}
		}
	
		
	}

	@Override
	public void list(Page<CreateBatchOpTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{
			Boolean isRecordExceed = false;
			
			List<CreateBatchOpTableDTO> pageItems = tableRows.getPageItems();
			for (CreateBatchOpTableDTO createAndSearchLotTableDTO : pageItems) {
				if(createAndSearchLotTableDTO.getIsRecordExceed()){
					isRecordExceed = true;
					break;
				}else{
					break;
				}
			}
			
			if(isRecordExceed){
				loadDataInWindow(tableRows);
			}else{
				searchResultTable.setTableList(pageItems, "");
//				showRecordCount(pageItems);
			}
			btnGenerateExcel.setEnabled(true);
			/*	
			btnGenerateExcel.setEnabled(true);
			chkBox.setEnabled(true);
			selectAllChkBox.setEnabled(true);
			
			searchResultTable.setTableList(tableRows.getPageItems(), "");
			
			*//**
			 * The below will add the records selected in each page 
			 * to a global list variable which is present in table.
			 * This is useful if the user wants to send all the records which
			 * ever he has selected in each page for processing.
			 * 
			 * Added for issue 192.
			 * 
			 * *//*

			searchResultTable.setFinalTableList(tableRows.getPageItems());
			
			searchResultTable.tablesize();
			if(null != selectAllChkBox && null != selectAllChkBox.getValue())
			{				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
				
			}
			
			List<CreateAndSearchLotTableDTO> tableList = searchResultTable.getTableItems();	
			
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableList) {
				
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
			}
			

			searchResultTable.setHasNextPage(tableRows.isHasNext());
			finalDataList = (List<CreateAndSearchLotTableDTO>)tableRows.getTotalList();
			if(null != finalDataList)
			{
				searchResultTable.setTotalNoOfRecords(finalDataList.size());
			}
		*/}
		else
		{
			btnGenerateExcel.setEnabled(true);
			chkBox.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create BATCH Home");
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
					lotForm.refresh(); 
					dialog.close();
					//fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null);
					
				}
			});
		}
	}
	
	
	@Override
	public void listForQuick(Page<CreateBatchOpTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			btnGenerateExcel.setEnabled(true);
			chkBox.setEnabled(true);
			selectAllChkBox.setEnabled(true);
			Long paymentStatusKey = 0l;
			
			List<CreateBatchOpTableDTO> resultList = new ArrayList<CreateBatchOpTableDTO>();
			
			List<CreateBatchOpTableDTO> exisitingList = searchResultTable.getValues();
			List<String> existingIntimation = new ArrayList<String>();
			for (CreateBatchOpTableDTO createAndSearchLotTableDTO : exisitingList) {
				existingIntimation.add(createAndSearchLotTableDTO.getIntimationNo());
				paymentStatusKey = createAndSearchLotTableDTO.getPaymentStatusKey();
			}
			resultList.addAll(exisitingList);
			List<CreateBatchOpTableDTO> pageItems = tableRows.getPageItems();
			for (CreateBatchOpTableDTO createAndSearchLotTableDTO : pageItems) {
				
				
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

//			searchResultTable.setFinalTableList(resultList);
			
			searchResultTable.tablesize();

			
			List<CreateBatchOpTableDTO> tableList = searchResultTable.getTableItems();	
			

			searchResultTable.setHasNextPage(tableRows.isHasNext());
			finalDataList = (List<CreateBatchOpTableDTO>)resultList;
		/*	if(null != finalDataList)
			{
				searchResultTable.setTotalNoOfRecords(finalDataList.size());
			}*/
		}
		else
		{
			btnGenerateExcel.setEnabled(false);
			chkBox.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create BATCH Home");
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
					lotForm.refresh(); 
					dialog.close();
					fireViewEvent(MenuItemBean.CREATE_BATCH_OP, null);
					
				}
			});
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
		btnGenerateExcel.setEnabled(true);
		//btnGenerateExcel.setDisableOnClick(true);
		//btnGenerateExcel.setEnabled(false);
		chkBox = new CheckBox();
		chkBox.setEnabled(false);
		
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
		
		btnUploadDocument = new Button();
		btnUploadDocument.setCaption("Upload Document");
		btnUploadDocument.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		selectAllChkBox = new CheckBox("Select All");
		selectAllChkBox.setEnabled(false);
		FormLayout formLayout = new FormLayout(upload,btnUploadDocument);
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		formLayout1.setEnabled(true);
		
		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);
		
		
		secondLayout = new VerticalLayout();
		secondLayout.addComponent(new HorizontalLayout(formLayout,formLayout1));
		secondLayout.addComponent(searchResultTable);
		/*HorizontalLayout hLayout = buildButtonsLayout();
		secondLayout.addComponent(hLayout);
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);*/
		
		//secondLayout.addComponent(tableForExcel);
		
		//return secondLayout;
		return secondLayout;
		
	}
	
	private HorizontalLayout buildButtonsLayout()
	{
		
		btnSave = new Button();
		btnSave.setCaption("Save");
		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSave.setWidth("-1px");
		btnSave.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() btnSave.setImmediate(true);
		
		
		btnSendToChecker = new Button();
		btnSendToChecker.setCaption("Send To Checker");
		//Vaadin8-setImmediate() btnSendToChecker.setImmediate(true);
		btnSendToChecker.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSendToChecker.setWidth("-1px");
		btnSendToChecker.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() btnSendToChecker.setImmediate(true);
		
		
		btnCancel = new Button();
		btnCancel.setCaption("Reset");
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
		btnLayout.addComponent(btnSave);
		btnLayout.addComponent(btnSendToChecker);
		btnLayout.addComponent(btnCancel);
		return btnLayout; 
	}
	
	
	private void addListener()
	{
		
		/*chkBox
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
						String err = checkBoxValidate();
						showErrorMessage(err);
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}
					searchResultTable.setValueForCheckBox(value);
				}
			}
		});*/
		
		
		/*selectAllChkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					List<CreateBatchOpTableDTO> requestTableList = finalDataList;		
					if(value)
					{
						String err = checkBoxValidate();
						showErrorMessage(err);
						if(null != requestTableList && !requestTableList.isEmpty())
						{
							for (CreateBatchOpTableDTO createAndSearchLotTableDTO : requestTableList) {
								
							createAndSearchLotTableDTO.setCheckBoxStatus("true");						
								 
						}
						} 
						
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}	
					
					if(null != finalDataList && !finalDataList.isEmpty())					{  
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : finalDataList) {
						
					createAndSearchLotTableDTO.setCheckBoxStatus(String.valueOf(value));						
						
				}
				} 		
					
					
					searchResultTable.setValueForSelectAllCheckBox(value);
					
				}
			}
		});*/
		
		
		/*selectAllChkBox
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
		});*/
		
		
		/*btnSave.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
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
											List<CreateBatchOpTableDTO> requestTableList = new ArrayList<CreateBatchOpTableDTO>();
											
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && true == selectAllChkBox.getValue())
											{
												 requestTableList = searchResultTable.getTableItems();
												 for (CreateBatchOpTableDTO createAndSearchLotTableDTO : requestTableList) {
														if(createAndSearchLotTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															invalid =true;
														}
														createAndSearchLotTableDTO.setCheckBoxStatus("true");						
															 
													}
											}
											else
											{											
												 requestTableList = searchResultTable.getTableItems();
											}
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<CreateBatchOpTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateBatchOpTableDTO>();
												for (CreateBatchOpTableDTO CreateBatchOpTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(CreateBatchOpTableDTO.getCheckBoxStatus()))
													{
														if(CreateBatchOpTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = CreateBatchOpTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															//invalid =true;
														}
														finalListForProcessing.add(CreateBatchOpTableDTO);
													}
													
											}
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												//fireViewEvent(CreateAndSearchLotPresenter.SAVE_PAYMENT_DETAIL, finalListForProcessing, "save");
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
		});*/
		
		
		/*btnSendToChecker.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(searchResultTable.validatePage())
				{
				
					Button value =  event.getButton();
					final Window popup = (Window) value.getData();
					
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure, you want to create lot for the selected record(s)?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											// Confirmed to continue
											Boolean chkBoxValue = false;
											Boolean invalid =false;
											List<CreateBatchOpTableDTO> requestTableList = new ArrayList<CreateBatchOpTableDTO>();
											
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && true == selectAllChkBox.getValue())
											{
												 requestTableList = finalDataList;
												 for (CreateBatchOpTableDTO createAndSearchLotTableDTO : requestTableList) {
														if(createAndSearchLotTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															invalid =true;
														}
														createAndSearchLotTableDTO.setCheckBoxStatus("true");						
															 
													}
											}
											else
											{											
												 requestTableList = searchResultTable.getTableItems();
											}
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<CreateBatchOpTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateBatchOpTableDTO>();
												for (CreateBatchOpTableDTO createAndSearchLotTableDTO : requestTableList) {
													
													if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
													{
														if(createAndSearchLotTableDTO.getDocVerifiedValue()!=null){
															String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
															if(!docVerifiedValue.equalsIgnoreCase("Yes")){
																invalid =true;
															}
														}else{
															//invalid =true;
														}
														finalListForProcessing.add(createAndSearchLotTableDTO);
													}
													
											}
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty() && !invalid)
											{
												//fireViewEvent(CreateAndSearchLotPresenter.GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING, finalListForProcessing, null);
												fireViewEvent(CreateAndSearchLotPresenter.GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING, requestTableList, null,popup);
											}
											else
											{
												Label label =null;
												label = new Label("Please select a record for lot number generation", ContentMode.HTML);
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
		});*/
		
		
		
		/*btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetView();
				resetSearchResultTableValues();
				fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null, null);
				
			}
		});*/
		
		
		btnGenerateExcel.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Boolean chkBoxValue = false;
					//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
					List<CreateBatchOpTableDTO> requestTableList = searchResultTable.getTableItems();
//					List<CreateBatchOpTableDTO> requestTableList = finalDataList;
					/*for (CreateBatchOpTableDTO createAndSearchLotTableDTO : requestTableList) {
						
						if(createAndSearchLotTableDTO.getLegalFirstName()!=null && !createAndSearchLotTableDTO.getLegalFirstName().isEmpty()){
							SelectValue payeeName = new SelectValue();
							payeeName.setId(0l);
							payeeName.setValue(createAndSearchLotTableDTO.getLegalFirstName());
							createAndSearchLotTableDTO.setPayeeName(payeeName);
						}
						
						if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
							chkBoxValue = true;
					}*/
					//if(null != chkBox && chkBox.getValue())
				/*	if(chkBoxValue  || (null != chkBox && null != chkBox.getValue() && chkBox.getValue()) || (null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue()) )
					{*/
						//getTableDataForReport();
						//secondLayout.addComponent(searchResultTable);
						//tableForExcel.setVisible(false);
						//excelExport = new  ExcelExport(searchResultTable.getTable());
						/*if(null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue())
						{
							getCompleteTableDataForReport(requestTableList);
						}
						else
						{*/	
							getTableDataForReport();
//						}
						secondLayout.addComponent(tableForExcel);
						tableForExcel.setVisible(false);
						ExcelExport excelExport = new  ExcelExport(tableForExcel.getTable());
						//excelExport.setDisplayTotals(displayTotals);
						//excelExport.
						excelExport.excludeCollapsedColumns();
						excelExport.setDisplayTotals(false);
						//excelExport.set
//						excelExport.setReportTitle("OP Details");
						excelExport.export();
					/*}
					
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
					}*/
			}
		});
		
		btnUploadDocument.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					upload.submitUpload();
				}
		});
		
		
	}
	
	
	
	private void getTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			List<CreateBatchOpTableDTO> requestTableList = searchResultTable.getValues();
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				/*for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						
						tableForExcel.addBeanToList(createAndSearchLotTableDTO);
					}
				}*/
				tableForExcel.addBeanToList(requestTableList);
			}	
		}
	}
	
	private void getCompleteTableDataForReport(List<CreateBatchOpTableDTO> requestTableList)
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				/*for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
					{
						
						tableForExcel.addBeanToList(createAndSearchLotTableDTO);
					}
				}*/
				tableForExcel.addFinalListToBean(requestTableList);
			}	
		}
	}
	
	public void lotGenerationValidation()
	{
		
	}
	/*private void getTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			List<SearchRRCRequestTableDTO> requestTableList = searchResultTable.getTableAllItems();
			if(null != requestTableList && !requestTableList.isEmpty())
			{
				for (SearchRRCRequestTableDTO searchRRCRequestTableDTO : requestTableList) {
					if(("true").equalsIgnoreCase(searchRRCRequestTableDTO.getCheckBoxStatus()))
					{
						tableForExcel.addBeanToList(searchRRCRequestTableDTO);
					}
				}
			}	
		}
	}*/

	
	@Override
	public void init(BeanItemContainer<SelectValue> type,
			BeanItemContainer<SelectValue> cpuCode,
			BeanItemContainer<SelectValue> claimant,
			BeanItemContainer<SelectValue> claimType,
			BeanItemContainer<SelectValue> paymentStatus,
			BeanItemContainer<SpecialSelectValue> product,
			BeanItemContainer<SelectValue> docVerified,
			BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> pioCode) {
		
	    lotForm.setDropDownValues(type, cpuCode, claimant, claimType, paymentStatus,product,docVerified,paymentMode,pioCode);
//		searchResultTable.setDropDownValues(cpuCode,docVerified);
		
	}

	@Override
	public void buildSuccessLayout(Map<String, Object> createLotMapper,final Window popUp) {
		final String error = (String)createLotMapper.get(SHAConstants.LOT_CREATION_ERROR);
		
		
		String strLotNo = (String)createLotMapper.get(SHAConstants.LOT_NUMBER);
		String totalNoOfRecords = (String)createLotMapper.get(SHAConstants.TOTAL_NO_LOT_RECORDS);
		String noOfRecordsSentForChecker = (String)createLotMapper.get(SHAConstants.NO_RECORDS_SENT_TO_CHECKER);
		
		String strMessage = null;
		Label successLabel = null;
		if(null != error && !("").equalsIgnoreCase(error) && ("true").equalsIgnoreCase(error))
		{
			strMessage = "Same intimation with hospitalization and any other classification type cannot be in a same lot. Please try again by selecting a valid intimation";
					successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>",
							ContentMode.HTML);
		}
		else
		{
			strMessage = "LOT NO: " + strLotNo +" has been created ";
			String strRecMessage = "Selected "+noOfRecordsSentForChecker +"/"+ totalNoOfRecords+" records have been sent to the checker successfully !!";
			successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>" + "</br>" + "<b style = 'color: green;'>" + strRecMessage + "</b>",
				ContentMode.HTML);
		}
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Create/Search LOT Home");
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

				if(!(null != error && !("").equalsIgnoreCase(error) && ("true").equalsIgnoreCase(error)))
				{
					fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null, null);
				}
				if(null != popUp){
					popUp.close();
				}
				
			}
		});
	}

	
/*	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		searchResultTable.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

	}*/

	@Override
	public void setPaymentCpu(
			CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
		//searchResultTable.setPaymentCpuName(updatePaymentDetailTableDTO);
		
	}

	
	
	@Override
	public void buildResultantTableLayout(SelectValue layoutType) {

		resetSearchResultTableValues();
		lotForm.resetFlds();
		searchResultTable.compMap=null;
//		chkBox.setValue(null);
		//resetView();
		if(null != layoutType && !("").equals(layoutType) && (SHAConstants.CREATE_BATCH_OP).equals(layoutType)){
				searchResultTable.initPresenterString(SHAConstants.CREATE_BATCH_OP);
				//searchResultTable.initTable();
				searchResultTable.init(SHAConstants.CREATE_BATCH_OP, Boolean.TRUE);
				tableForExcel.setVisibleColumnsForCreateLot();
			}
			else if(null != layoutType && !("").equals(layoutType) && (SHAConstants.SEARCH_BATCH_OP).equals(layoutType)){
				searchResultTable.initPresenterString(SHAConstants.SEARCH_BATCH_OP);
				//searchResultTable.initTable();
				searchResultTable.init(SHAConstants.SEARCH_BATCH_OP, Boolean.TRUE);
				tableForExcel.setVisibleColumnsForSearchLot();
			}
		
		if (null != mainPanel
				&& mainPanel.getComponentCount() > 0) {
			mainPanel.removeAllComponents();
		}
		mainPanel.setFirstComponent(lotForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSecondComponent(buildSecondComponent());
//		mainPanel.getSecondComponent().setVisible(false);
		//mainPanel.addComponent(buildButtonLayoutBasedOnType(layoutType));
		
				
	}
	
	private VerticalLayout buildButtonLayoutBasedOnType(SelectValue layoutType)
	{
		if (null != secondLayout
				&& secondLayout.getComponentCount() > 0) {
			secondLayout.removeAllComponents();
		}
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(false);
		
		
		
		
		FormLayout formLayout1 = new FormLayout();
		formLayout1.setMargin(false);

		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);
		
		FormLayout formLayout2 = new FormLayout();
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
			hLayout = buildButtonsLayout();
		}
		else
		{
			hLayout = new HorizontalLayout();
			hLayout.setSpacing(true);
			
			if(null != layoutType && !("").equals(layoutType) && (SHAConstants.CREATE_LOT).equals(layoutType)){
				hLayout.addComponent(btnSave);
				hLayout.addComponent(btnSendToChecker);
				hLayout.addComponent(btnCancel);
			}
			else
			{
//				hLayout.addComponent(btnCancel);
			}
			
			//hLayout.setComponentAlignment(fLayout, Alignment.MIDDLE_CENTER);
		}

		//HorizontalLayout hLayout = buildButtonsLayout();
		secondLayout.addComponent(hLayout);
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
		return secondLayout;
	}

	@Override
	public void showClaimsDMS(String url) {
		//getUI().getPage().open(url, "_blank");
		getUI().getPage().open(url, "_blank",1550,800,BorderStyle.NONE);

		// TODO Auto-generated method stub
		
	}
	
	

/*	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView) {
		searchResultTable.populatePreviousPaymentDetails(tableDTO,editPaymentView);
		// TODO Auto-generated method stub
		
	}*/

	/*@Override
	public void buildPaymentDetailsSuccessLayout(Boolean isSuccess) {

		String strMessage = "";
		if(isSuccess)
		{
			strMessage = "Payment details saved successfully.";
		}
		else
		{
			strMessage = "Error occurred while saving payment data. Please contact administrator";
		}
		
		Label successLabel = new Label("<b style = 'color: green;'>" + strMessage + "</b>",
							ContentMode.HTML);
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Create/Search LOT Home");
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
				{
					//paymentPopup.close();
				}
				
				
				
			}
		});
	
		
	}
*/
	
	private String checkBoxValidate()
	{
		StringBuffer err = new StringBuffer();
		if((null != chkBox && null != chkBox.getValue() && true == chkBox.getValue()) && (null!= selectAllChkBox && null != selectAllChkBox.getValue() && true == selectAllChkBox.getValue() ))
		{
			err.append("Please select any one option");
		}
		return err.toString();
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
				toolBar.opcountTool();
				fireViewEvent(MenuItemBean.CREATE_BATCH_OP, null, null);
				
			}
		});
		
	}
	
	public void setSplitPosition(String searchType){
		
		CreateBatchOpDTO searchDTO = lotForm.getSearchDTO();
		
		if(SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(searchType)){/*
			mainPanel.setSplitPosition(37);
			searchDTO.setSearchTabType(SHAConstants.NORMAL_SEARCH);
			mainPanel.removeComponent(buildSecondComponent());
			searchResultTable.resetTable();
			searchResultTable.resetTableDataList();
		*/}
		else
		{/*
			mainPanel.setSplitPosition(21);
			searchDTO.setSearchTabType(SHAConstants.QUICK_SEARCH);
			VerticalLayout vLayout = buildSecondComponent();
			addListener();
			mainPanel.setSecondComponent(vLayout);
			searchResultTable.resetTable();
			searchResultTable.resetTableDataList();
			searchResultTable.initPresenterString(SHAConstants.QUICK_SEARCH);
			searchResultTable.init(SHAConstants.QUICK_SEARCH, Boolean.TRUE);
			lotForm.addSearchListener(this);
		*/}
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataInWindow(Page<CreateBatchOpTableDTO> tableRows){
		
		popup.setCaption("Search Result");
		popup.setWidth("95%");
		popup.setHeight("95%");		

		btnGenerateExcel.setEnabled(true);
		chkBox.setEnabled(true);
		selectAllChkBox.setEnabled(true);
		VerticalLayout SecondVL = new VerticalLayout();

		HorizontalLayout topnestedHL = new HorizontalLayout(chkBox,selectAllChkBox,btnGenerateExcel);
		topnestedHL.setSpacing(true);
		topnestedHL.setMargin(true);
	//	topnestedHL.setComponentAlignment(chkBox, Alignment.MIDDLE_CENTER);
	//	topnestedHL.setComponentAlignment(selectAllChkBox, Alignment.MIDDLE_CENTER);
		topnestedHL.setHeight("20px");
		
	/*	HorizontalLayout Pl = new HorizontalLayout(penalLabel);
		Pl.setWidth("100%");
		Pl.setHeight("15px");
		Pl.setComponentAlignment(penalLabel, Alignment.MIDDLE_CENTER);
		*/
	//	VerticalLayout topHL = new VerticalLayout(topnestedHL);
		//topHL.setWidth("100%");
//		topHL.setComponentAlignment(Pl, Alignment.TOP_RIGHT);
	//	topHL.setComponentAlignment(topnestedHL, Alignment.MIDDLE_RIGHT);
//		topHL.setComponentAlignment(btnGenerateExcel, Alignment.MIDDLE_LEFT);
		SecondVL.addComponent(topnestedHL);


		searchResultTable.setTableList(tableRows.getPageItems(), "");
	/*	searchResultTable.setFinalTableList(tableRows.getPageItems());		
		searchResultTable.tablesize();
		if(null != selectAllChkBox && null != selectAllChkBox.getValue()){				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
		}*/

		List<CreateBatchOpTableDTO> tableList = searchResultTable.getTableItems();	

		/*for (CreateBatchOpTableDTO createAndSearchLotTableDTO : tableList) {
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
		}*/
		searchResultTable.setHasNextPage(tableRows.isHasNext());
		searchResultTable.setPage(tableRows);
		finalDataList = (List<CreateBatchOpTableDTO>)tableRows.getTotalList();
		/*if(null != finalDataList){
			searchResultTable.setTotalNoOfRecords(finalDataList.size());
		}*/
		SecondVL.addComponent(searchResultTable);

		HorizontalLayout footerButtons = buildButtonsLayout();

//		addListener();
		
		SecondVL.addComponent(footerButtons);
		SecondVL.setComponentAlignment(footerButtons, Alignment.MIDDLE_CENTER);

		// close button in window
		Button btnClose = new Button();
		btnClose.setCaption("Close");
		btnClose.addStyleName(ValoTheme.BUTTON_DANGER);
		btnClose.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().removeWindow(popup);
//				searchResultTable.getPageable().setPageNumber(1);
				searchResultTable.clearExistingList();
			}
		});

//		HorizontalLayout closebutLayout = new HorizontalLayout();
//		closebutLayout.addComponent(btnClose);
//		closebutLayout.setSizeFull();
//		closebutLayout.setComponentAlignment(btnClose, Alignment.MIDDLE_CENTER);
		
		footerButtons.addComponent(btnClose);

		SecondVL.setSpacing(true);
		popup.setContent(SecondVL);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(false);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
				//searchResultTable.clearExistingList();
			}
		});
		popup.setModal(true);
		
		if(btnSendToChecker != null){
			btnSendToChecker.setData(popup);
		}
		
		if(popup.isAttached()){
			popup.detach();
			UI.getCurrent().removeWindow(popup);
		}
		
		UI.getCurrent().addWindow(popup);
	}
	
public void showRecordCount(List<CreateBatchOpTableDTO> tableDTOList) {/*
		
		batchCpuCountTable.init("Count For Cpu Wise", false, false);
	//	batchCpuCountTable.setTableList(tableDTOList);
		if(tableDTOList != null && ! tableDTOList.isEmpty()){
			CreateBatchOpTableDTO createAndSearchLotTableDTO = tableDTOList.get(0);
			//batchCpuCountTable.setTotalCount(createAndSearchLotTableDTO);
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
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				searchResultTable.setIsSearchBtnClicked(false);
				//System.out.println("Close listener called");
			}
		});
		
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
	*/}

@Override
public void setUpPaymentCpuCodeDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,CreateBatchOpTableDTO updatePaymentDetailTableDTO) {/*
	// TODO Auto-generated method stub
	searchResultTable.setUpPaymentCpuCodeDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

*/}

@Override
public void setUpPayeeNameDetails(
		ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
		CreateBatchOpTableDTO updatePaymentDetailTableDTO) {/*
	
	searchResultTable.setUpPayeeNameDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);
	
*/}

public void resetData(){/*
	if(! searchResultTable.getIsSearchBtnClicked())
	{
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.clearExistingList();searchResultTable.setIsSearchBtnClicked(true);
		if(null != selectAllChkBox){
			selectAllChkBox.setValue(false);
		}
	}
*/}

@Override
public void setUpPayableDetails(String payableName,CreateBatchOpTableDTO tableDTO) {/*
	searchResultTable.setUpPayableAmt(payableName, tableDTO);
*/}

@Override
public void populatePreviousPaymentDetails(PreviousAccountDetailsDTO tableDTO,
		EditPaymentDetailsView editPaymentView) {
	// TODO Auto-generated method stub
	
}

@Override
public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,
		CreateBatchOpTableDTO updatePaymentDetailTableDTO) {
	// TODO Auto-generated method stub
	
}

@Override
public void uploadSucceeded(SucceededEvent event) {
	FileInputStream fis = null;
	Boolean hasError = false;
	try {
		if(file.exists() && !file.isDirectory()) {
		fis = new FileInputStream(file);
		org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheetAt = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = sheetAt.iterator();
		StringBuffer eMsg = new StringBuffer();	
		StringBuffer countMsg = new StringBuffer();
		int successCount = 0;
		int errorCount = 0;
		int totalCount = 0;
		Date chequeDt = null;
		String chequeNumber = null;
		Boolean setledClaim = false;
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Long batchNo = dbCalculationService.generateSequence(SHAConstants.OP_BATCH_NO_SEQUENCE_NAME);
		
			while (rowIterator.hasNext()){
				try{
					if(file.exists() && !file.isDirectory()) { 
				Row row = rowIterator.next();
				
					if(row.getRowNum() != 0){
						Boolean isError = false;
						Cell intimationNo = row.getCell(0);
						Cell claimNo = row.getCell(1);
						Cell billAmt = row.getCell(3);
						Cell payableAmt = row.getCell(4);
						Cell payeeName = row.getCell(5);
						Cell chequeNo = row.getCell(6);
						Cell chequeDate = row.getCell(7);
						Cell ifscCode = row.getCell(8);
						Cell bankName = row.getCell(9);
						Cell totBillAmt = row.getCell(10);
						Cell deductionAmt = row.getCell(11);
						Cell totPayableAmt = row.getCell(12);
						Cell remarks = row.getCell(13);
						String remarksVal = remarks.getStringCellValue();
						String intimationNumber = intimationNo.getStringCellValue();
//						OutpatientService opc =  new OutpatientService();
						String settledOrNot = outPatientService.checkSettled(intimationNumber);
						if(settledOrNot == null){
						if(intimationNumber != null){
							String intNo = intimationNumber.replace("/", "");
							if(intNo != null && !intNo.isEmpty()){
								Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
							      Matcher my_match = my_pattern.matcher(intNo);
							      boolean check = my_match.find();
							      if(check){
							    	  isError = true;
							    	  if(eMsg != null && !eMsg.toString().contains("Enter a valid intimation number")){
							    		  eMsg.append("Enter a valid intimation number</br>");
							    	  }
							      	}
							} else if(intimationNumber.isEmpty()) {
								isError = true;
						    	  if(eMsg != null && !eMsg.toString().contains("Enter a valid intimation number")){
						    		  eMsg.append("Enter a valid intimation number</br>");
						    	  }

							}
						}
						String claimNumber = claimNo.getStringCellValue();
						if(claimNumber != null && !claimNumber.isEmpty()){
							String clmNo = claimNumber.replace("/", "");
							if(clmNo != null){
								Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
							      Matcher my_match = my_pattern.matcher(clmNo);
							      boolean checkClm = my_match.find();
							      if(checkClm){
							    	  isError = true;
							    	  if(eMsg != null && !eMsg.toString().contains("Enter a valid claim number")){
							    		  eMsg.append("Enter a valid claim number</br>");
							    	  }
							      	}
							}
						} else if(claimNumber.isEmpty()){
							isError = true;
					    	  if(eMsg != null && !eMsg.toString().contains("Enter a valid claim number")){
					    		  eMsg.append("Enter a valid claim number</br>");
					    	  }
						}
						Double billAmount = 0d;
						if(billAmt.getCellType() == Cell.CELL_TYPE_STRING){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid bill amount")){
					    		  eMsg.append("Enter valid bill amount</br>");
					      	}
						} else if (billAmt.getCellType() == Cell.CELL_TYPE_BLANK){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid bill amount")){
					    		  eMsg.append("Enter valid bill amount</br>");
					      	}
						}else {
							billAmount = billAmt.getNumericCellValue(); 
							if(billAmount == 0d || billAmount < 10 || billAmount > 1000000){
								if(billAmount > 1000000){
									billAmount = 0d;
								}
								if(eMsg != null && !eMsg.toString().contains("Enter valid bill amount")){
										isError = true;
						    		  eMsg.append("Enter valid bill amount</br>");
						      	}
							}
						}
						Double payableAmount = 0d;
						if(payableAmt.getCellType() == Cell.CELL_TYPE_STRING){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Payable Amount")){
					    		  eMsg.append("Enter valid Payable Amount</br>");
					      	}
						} else if (payableAmt.getCellType() == Cell.CELL_TYPE_BLANK){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Payable Amount")){
					    		  eMsg.append("Enter valid Payable Amount</br>");
					      	}
						}else {
							payableAmount = payableAmt.getNumericCellValue();
							if(payableAmount == 0d || payableAmount < 10 || payableAmount > 1000000){
								if(payableAmount > 1000000){
									payableAmount = 0d;
								}
								if(eMsg != null && !eMsg.toString().contains("Enter valid Payable Amount")){
									isError = true;
						    		  eMsg.append("Enter valid Payable Amount</br>");
						      	}
							}
						}
						String payename = payeeName.getStringCellValue(); 
						Pattern payee = Pattern.compile("^[A-Za-z .]+$", Pattern.CASE_INSENSITIVE);
					      Matcher payeName = payee.matcher(payename);
					      boolean payname = payeName.find();
					      if(!payname){
					    	  isError = true;
					    	  if(eMsg != null && !eMsg.toString().contains("Enter valid Payee Name")){
					    		  eMsg.append("Enter valid Payee Name</br>");
					    	  }
					      	}
					      if(payename != null && !payename.isEmpty()){
					    	  payename = payename.replaceAll("\\s", ""); 
					      }
						if(payename == null || payename.isEmpty() || payename.length() < 3 || payename.length() > 40){
							if(payename.length() > 40){
								payename =null;
							}
							if(eMsg != null && !eMsg.toString().contains("Enter valid Payee Name")){
								isError = true;
					    		  eMsg.append("Enter valid Payee Name</br>");
					      	}
						}
						String ifsc = ifscCode.getStringCellValue();
						/*if(ifsc != null && !ifsc.isEmpty()){
							String ifsccode = ifsc.substring(0, 4);
							Pattern ifsc_pattern = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
						      Matcher ifsc_match = ifsc_pattern.matcher(ifsccode);
						      boolean checkIfsc = ifsc_match.find();
						      if(checkIfsc){
						    	  isError = true;
						    	  if(eMsg != null && !eMsg.toString().contains("Enter a Valid Bank code and system should not allow to uploaded")){
						    		  isError = true;
						    		  eMsg.append("Enter a Valid Bank code and system should not allow to uploaded</br>");
						    	  }
						      	}
						      String ifscStr = ifsc.substring(4,5);
						      String ifsclastStr = ifsc.substring(4,ifsc.length()-1);
						      Pattern ifs_pattern = Pattern.compile("\\d");
						      Matcher ifs_match = ifs_pattern.matcher(ifsclastStr);
						      boolean ifscCo = ifsclastStr.matches("^[0-9]*$");
						      boolean checkIfsStr = ifsclastStr.matches("^[0-9]*$");
						      if(!ifscCo || !checkIfsStr){
						    	  isError = true;
						    	  if(eMsg != null && !eMsg.toString().contains("Enter a Valid Bank code and system should not allow to uploaded")){
						    		  eMsg.append("Enter a Valid Bank code and system should not allow to uploaded</br>");
						    	  }
						      }	
						}*/
						String bnkName = bankName.getStringCellValue();
						Pattern bankname = Pattern.compile("^[A-Za-z ]+$", Pattern.CASE_INSENSITIVE);
					     Matcher bnkNm = bankname.matcher(bnkName);
					      boolean notbankName = bnkNm.find();
						if(bnkName == null || bnkName.isEmpty() || bankName.getCellType() == Cell.CELL_TYPE_BLANK 
								|| !notbankName || bnkName.length() < 5 || bnkName.length() > 100){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Bank name")){
					    		  eMsg.append("Enter a Valid Bank name</br>");
					      	}
						}
						Double totalBillAmt = 0d;
						if(totBillAmt.getCellType() == Cell.CELL_TYPE_STRING){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Total Bill Amount")){
					    		  eMsg.append("Enter valid Total Bill Amount</br>");
					      	}
						} else if (totBillAmt.getCellType() == Cell.CELL_TYPE_BLANK){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Total Bill Amount")){
					    		  eMsg.append("Enter valid Total Bill Amount</br>");
					      	}
						}else {
							totalBillAmt = totBillAmt.getNumericCellValue();
							if(totalBillAmt == 0d || totalBillAmt < 10 || totalBillAmt > 1000000){
								if(totalBillAmt > 1000000){
									totalBillAmt =0d;
								}
								if(eMsg != null && !eMsg.toString().contains("Enter valid Total Bill Amount")){
									isError = true;
						    		  eMsg.append("Enter valid Total Bill Amount</br>");
						      	}
							}
						}
						Double deductAmt = 0d;
						if(deductionAmt.getCellType() == Cell.CELL_TYPE_STRING){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Deduction Amount")){
					    		  eMsg.append("Enter valid Deduction Amount</br>");
					      	}
						} else if (deductionAmt.getCellType() == Cell.CELL_TYPE_BLANK){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Deduction Amount")){
					    		  eMsg.append("Enter valid Deduction Amount</br>");
					      	}
						}else {
							deductAmt = deductionAmt.getNumericCellValue();
							if(deductAmt > 1000000){
								deductAmt =0d;
								if(eMsg != null && !eMsg.toString().contains("Enter valid Deduction Amount")){
						    		  eMsg.append("Enter valid Deduction Amount</br>");
						      	}
							}
						}
						Double totalPayableAmt = 0d;
						if(totPayableAmt.getCellType() == Cell.CELL_TYPE_STRING){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Total Payable Amount")){
					    		  eMsg.append("Enter valid Total Payable Amount</br>");
					      	}
						} else if (totPayableAmt.getCellType() == Cell.CELL_TYPE_BLANK){
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter valid Total Payable Amount")){
					    		  eMsg.append("Enter valid Total Payable Amount</br>");
					      	}
						}else {
							totalPayableAmt = totPayableAmt.getNumericCellValue();
							if(totalPayableAmt == 0d || totalPayableAmt < 10 || totalPayableAmt > 1000000){
								if(totalPayableAmt > 1000000){
									totalPayableAmt =0d;
								}
								if(eMsg != null && !eMsg.toString().contains("Enter valid Total Payable Amount")){
									isError = true;
						    		  eMsg.append("Enter valid Total Payable Amount</br>");
						      	}
							}
						}
						
						if(remarksVal == null || remarksVal.isEmpty() || remarksVal.length() < 10 || remarksVal.length() >200){
							if(remarksVal.length() > 200){
								remarksVal = null;
							}
							isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Remarks")){
					    		  eMsg.append("Enter a Valid Remarks</br>");
					      	}
						}
//						if(ifscCode.getCellType() == Cell.CELL_TYPE_BLANK){
							if(chequeNo.getCellType() == Cell.CELL_TYPE_STRING){
							chequeNumber = null;
							isError = true;
								 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque number")){
						    		  eMsg.append("Enter a Valid Cheque number</br>");
						      	}
							}else{
								int numericCellValue = (int) chequeNo.getNumericCellValue();
								chequeNumber = String.valueOf(numericCellValue);
							}
							if(chequeNumber != null){
								if(((chequeNumber.length() > 6 || chequeNumber.length() < 6)) || chequeNumber.equals("0") || chequeNumber == null || chequeNumber.isEmpty() || chequeNumber.trim().isEmpty()){
									 isError = true;
									 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque number")){
							    		  eMsg.append("Enter a Valid Cheque number</br>");
							      	}
								}
							}
//						}
						
						
//						if(ifscCode.getCellType() == Cell.CELL_TYPE_BLANK){
						if(chequeDate.getCellType() == Cell.CELL_TYPE_STRING){
							String stringCellValue = chequeDate.getStringCellValue();
//							 boolean chqDate = stringCellValue.matches("[a-z]*$");
//							Pattern p = Pattern.compile("[a-z]");^[a-z0-9/]+$
							Pattern p = Pattern.compile("^[0-9/]+$");
							 if(p.matcher(stringCellValue).find()){
								 chequeDt = new SimpleDateFormat("dd/MM/yyyy").parse(stringCellValue);
							 } else {
								 chequeDt = null;
							 }
							if(chequeDt != null){
								DataFormatter df = new DataFormatter();
								String stringCellDateValue = df.formatCellValue(chequeDate);
								SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
								if(!stringCellDateValue.equals(sdfDate.format(chequeDt))){
									isError = true;
									 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque Date")){
							    		  eMsg.append("Enter a Valid Cheque Date</br>");
							      	}
								}
							}
						}else{
							chequeDt = chequeDate.getDateCellValue();
							if(chequeDt != null){
							DataFormatter df = new DataFormatter();
							String stringCellValue = df.formatCellValue(chequeDate);
							String dateFor = SHAUtils.getDateWithoutTime(chequeDt);
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							if(!stringCellValue.equals(sdf.format(chequeDt))){
								isError = true;
								 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque Date")){
						    		  eMsg.append("Enter a Valid Cheque Date</br>");
						      	}
							}
							}
							System.out.println();
						}
						if(chequeDt == null || chequeDt.toString().isEmpty()){
							 isError = true;
							 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque Date")){
					    		  eMsg.append("Enter a Valid Cheque Date</br>");
					      	}
						}
//					}
						OPUploadPaymentDetails opPaymentUploadDocDtls = new OPUploadPaymentDetails();
						opPaymentUploadDocDtls.setIntimationNumber(intimationNumber);
						opPaymentUploadDocDtls.setClaimNumber(claimNumber);
						if(bnkName != null){
							opPaymentUploadDocDtls.setPaymentType("");
						} else {
							opPaymentUploadDocDtls.setPaymentType("");
						}
						opPaymentUploadDocDtls.setBillAmount(billAmount);
						opPaymentUploadDocDtls.setPayableAmount(payableAmount);
						opPaymentUploadDocDtls.setTotBillAmount(totalBillAmt);
						opPaymentUploadDocDtls.setDeductionAmount(deductAmt);
						opPaymentUploadDocDtls.setTotPayableAmount(totalPayableAmt);
						opPaymentUploadDocDtls.setRemarks(remarksVal);
						opPaymentUploadDocDtls.setPayeeName(payename);
						opPaymentUploadDocDtls.setBatchNumber(batchNo.toString());
						
//						Long cqNo = Long.valueOf(chequeNumber.toString());
						//IMSSUPPOR-33298 - trim for space condition
						CreateBatchOpTableDTO uploadOPDetails = new CreateBatchOpTableDTO();
						if((chequeNumber != null && !chequeNumber.trim().isEmpty() && chequeDt != null) || (ifsc != null && !ifsc.isEmpty())){
							if(chequeNumber != null && !chequeNumber.trim().isEmpty() && chequeDt != null){
								uploadOPDetails.setChequeNo(String.valueOf(chequeNumber));
								uploadOPDetails.setChequeDate(chequeDt);
							}
							uploadOPDetails.setIfscCode(ifsc);
							uploadOPDetails.setBatchNumber(batchNo.toString());
						}
						if(!isError){
							
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
							fireViewEvent(CreateBatchOpPresenter.UPDATE_CHEQUE_DETAILS_OP, intimationNumber,uploadOPDetails,userName);
							opPaymentUploadDocDtls.setChequeNo(String.valueOf(chequeNumber));
							opPaymentUploadDocDtls.setChecqueDate(chequeDt);
							opPaymentUploadDocDtls.setUploadStatus("P");
							opPaymentUploadDocDtls.setUploadRemarks("PASS");
							opPaymentUploadDocDtls.setBatchNumber(batchNo.toString());
							opPaymentUploadDocDtls.setCreatedBy(userName);
							opPaymentUploadDocDtls.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							fireViewEvent(CreateBatchOpPresenter.UPDATE_OP_PAYMENT_DETAILS_BATCH,opPaymentUploadDocDtls);
							successCount += 1;
						}
						else{
							/*Notification.show("Error", "" + "Please Upload excel with Payment Details", Type.ERROR_MESSAGE);
							hasError = true;
							break;*/
//							if(!isError){
								String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
								opPaymentUploadDocDtls.setChequeNo(String.valueOf(chequeNumber));
								opPaymentUploadDocDtls.setChecqueDate(chequeDt);
								opPaymentUploadDocDtls.setUploadStatus("E");
								opPaymentUploadDocDtls.setUploadRemarks("ERROR");
								opPaymentUploadDocDtls.setCreatedBy(userName);
								opPaymentUploadDocDtls.setCreatedDate(new Timestamp(System.currentTimeMillis()));
								fireViewEvent(CreateBatchOpPresenter.UPDATE_OP_PAYMENT_DETAILS_BATCH,opPaymentUploadDocDtls);
								errorCount += 1;
							/*} else {
								MessageBox.createError()
						    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
						        .withOkButton(ButtonOption.caption("OK")).open();
							}*/
						}
					} else {
						if(eMsg != null && !eMsg.toString().contains("Claim has already been Settled")){
								setledClaim = true;
								eMsg.append("Claim has already been Settled</br>");
				    	  }
					}
				
					}
				}
				
				}catch(Exception e){
					Notification.show("Error", "" + "Please upload excel with Valid format", Type.ERROR_MESSAGE);
					break;
					/*isError = true;
					 if(eMsg != null && !eMsg.toString().contains("Enter a Valid Cheque Date")){
			    		  eMsg.append("Enter a Valid Cheque Date</br>");
			      	}*/
				} 
			}
			if(successCount > 0 || errorCount > 0){
				countMsg.append("Total Records Upload Success Count :    "+successCount+"</br>");
				countMsg.append("Total Records Failure Count :     "+errorCount+"</br>");
				if(successCount > 0){
					countMsg.append("Batch No:"+batchNo);
				}
			}
			if(eMsg != null && !eMsg.toString().isEmpty()){
				MessageBox.createError()
		    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
		        .withOkButton(ButtonOption.caption("OK")).open();
			}
			if(successCount > 0 || errorCount > 0){
				MessageBox.createInfo()
		    	.withCaptionCust("Information").withHtmlMessage(countMsg.toString())
		        .withOkButton(ButtonOption.caption("OK")).open();
			}
//			if(!hasError){
//				buildSuccessLayout("File Upload Successfully");
//			}
		}else {
			Notification.show("Error", StringUtils.EMPTY + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		if(fis != null){
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

@Override
public OutputStream receiveUpload(String filename, String mimeType) {
	FileOutputStream fos = null;
	try {
		this.file = null;
		this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
		if(null != file && (file.getName().endsWith("xlsx") || file.getName().endsWith("xls")))
		{
			fos = new FileOutputStream(file);
		}
		else
		{
			Notification.show("Error", "" + "Please select excel file Only", Type.ERROR_MESSAGE);
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		}
	if(fos == null){
		try {
			fos = new FileOutputStream("DUMMY");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		return fos;
}

}