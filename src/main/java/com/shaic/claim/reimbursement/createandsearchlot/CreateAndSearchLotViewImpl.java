package com.shaic.claim.reimbursement.createandsearchlot;

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
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.paymentprocess.createbatch.search.BatchCpuCountTable;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
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
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class CreateAndSearchLotViewImpl extends AbstractMVPView implements CreateAndSearchLotView{
	
	@Inject
	private CreateAndSearchLotForm lotForm;

	@Inject
	private CreateAndSearchLotListenerTable searchResultTable;
	
	@Inject
	private CreateAndSearchLotTableForExcel tableForExcel;
	
	@Inject
	private BatchCpuCountTable batchCpuCountTable;
	
	private VerticalSplitPanel mainPanel;
	
	private VerticalLayout resultTableLayout;
	
	private Button btnSendToChecker;
	
	private Button btnCancel;
	
	private Button btnSave;
	
	private HorizontalLayout btnLayout;
	
	private Button btnGenerateExcel;
	
	private CheckBox chkBox;
	
	private CheckBox selectAllChkBox;
	
	private VerticalLayout secondLayout = null;
	
	private ExcelExport excelExport;
	
	private List<CreateAndSearchLotTableDTO> finalDataList = null;
	
	final Window popup = new com.vaadin.ui.Window();

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
		buildSecondComponent();
		mainPanel.setSplitPosition(37);
		setHeight("100%");
		setHeight("675px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		lotForm.addSearchListener(this);
		finalDataList = new ArrayList<CreateAndSearchLotTableDTO>();
		CreateAndSearchLotFormDTO searchDTO = lotForm.getSearchDTO();
		searchDTO.setSearchTabType(SHAConstants.NORMAL_SEARCH);
		addListener();
		resetView();
	}
	
	@Override
	public void resetView() {
		//btnGenerateExcel.setEnabled(false);
		//Added for issue 192
		searchResultTable.resetTableDataList();
		lotForm.refresh(); 
		chkBox.setValue(null);
		
	}

	@Override
	public void doSearch() {
		CreateAndSearchLotFormDTO searchDTO = lotForm.getSearchDTO();
		String err=lotForm.validate(searchDTO);
		if(err == null)
		{		
			Pageable pageable = searchResultTable.getPageable();
			searchDTO.setPageable(pageable);
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
			fireViewEvent(CreateAndSearchLotPresenter.CREATE_SEARCH_LOT, searchDTO,userName,passWord);
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
		btnGenerateExcel.setEnabled(false);
		chkBox.setEnabled(false);
		chkBox.setValue(null);
		selectAllChkBox.setValue(null);
		selectAllChkBox.setEnabled(false);
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
				/*IMSSUPPOR-28529*/
				CreateAndSearchLotFormDTO searchDTO = lotForm.getSearchDTO();
				loadDataInWindow(tableRows,searchDTO.getType().getValue(), searchDTO.getVerificationType());
			}else{
				showRecordCount(pageItems);
			}
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
			btnGenerateExcel.setEnabled(false);
			chkBox.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create/Search LOT Home");
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
					//fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null);
					
				}
			});
		}
	}
	
	
	@Override
	public void listForQuick(Page<CreateAndSearchLotTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			btnGenerateExcel.setEnabled(true);
			chkBox.setEnabled(true);
			selectAllChkBox.setEnabled(true);
			Long paymentStatusKey = 0l;
			
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
			if(null != selectAllChkBox && null != selectAllChkBox.getValue())
			{				
			searchResultTable.setValueForSelectAllCheckBox(selectAllChkBox.getValue());
				
			}
			
			List<CreateAndSearchLotTableDTO> tableList = searchResultTable.getTableItems();	
			
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableList) {
				
			searchResultTable.setRowColor(createAndSearchLotTableDTO);
			}
			

			searchResultTable.setHasNextPage(tableRows.isHasNext());
			finalDataList = (List<CreateAndSearchLotTableDTO>)resultList;
			if(null != finalDataList)
			{
				searchResultTable.setTotalNoOfRecords(finalDataList.size());
			}
		}
		else
		{
			btnGenerateExcel.setEnabled(false);
			chkBox.setEnabled(false);
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Create/Search LOT Home");
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
					//fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null);
					
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
		btnGenerateExcel.setEnabled(false);
		//btnGenerateExcel.setDisableOnClick(true);
		//btnGenerateExcel.setEnabled(false);
		chkBox = new CheckBox();
		chkBox.setEnabled(false);
		
		
		selectAllChkBox = new CheckBox("Select All");
		selectAllChkBox.setEnabled(false);
		/*FormLayout formLayout = new FormLayout(chkBox);
		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
		
		
		formLayout.setSpacing(true);
		formLayout1.setSpacing(true);
		
		
		secondLayout = new VerticalLayout();
		secondLayout.addComponent(new HorizontalLayout(formLayout,formLayout1));
		secondLayout.addComponent(searchResultTable);
		HorizontalLayout hLayout = buildButtonsLayout();
		secondLayout.addComponent(hLayout);
		secondLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
		*/
		//secondLayout.addComponent(tableForExcel);
		
		//return secondLayout;
		return buildButtonLayoutBasedOnType(null);
		
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
						/*String err = checkBoxValidate();
						showErrorMessage(err);*/
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
					List<CreateAndSearchLotTableDTO> requestTableList = finalDataList;		
					if(value)
					{
						/*String err = checkBoxValidate();
						showErrorMessage(err);*/
						if(null != requestTableList && !requestTableList.isEmpty())
						{
							for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
								
							createAndSearchLotTableDTO.setCheckBoxStatus("true");						
								 
						}
						} 
						
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}	
					
					/*if(null != finalDataList && !finalDataList.isEmpty())					{  
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : finalDataList) {
						
					createAndSearchLotTableDTO.setCheckBoxStatus(String.valueOf(value));						
						
				}
				} 		*/
					
					
					searchResultTable.setValueForSelectAllCheckBox(value);
					
				}
			}
		});
		
		
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
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
											List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
											if(null != requestTableList && !requestTableList.isEmpty())
											{
												finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
												
													for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {

														if (("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus())) {
															if (createAndSearchLotTableDTO.getDocVerifiedValue() != null) {
																String docVerifiedValue = createAndSearchLotTableDTO.getDocVerifiedValue();
																if (!docVerifiedValue.equalsIgnoreCase("Yes")) {
																	invalid = true;
																}
															} else {
																// invalid
																// =true;
															}
															finalListForProcessing.add(createAndSearchLotTableDTO);

													}
												}
											}
											if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
											{
												//fireViewEvent(CreateAndSearchLotPresenter.GENERATE_LOT_NO_FOR_PAYMENT_PROCESSING, finalListForProcessing, null);
												fireViewEvent(CreateAndSearchLotPresenter.SAVE_PAYMENT_DETAIL, finalListForProcessing, "save");
												//searchResultTable.clearExistingList();
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
		
		
		btnSendToChecker.addClickListener(new ClickListener() {
			
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
											List<CreateAndSearchLotTableDTO> requestTableList = new ArrayList<CreateAndSearchLotTableDTO>();
											
											if(null != selectAllChkBox && null !=selectAllChkBox.getValue() && true == selectAllChkBox.getValue())
											{
												 requestTableList = finalDataList;
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
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
											//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
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
		});
		
		
		
		btnCancel.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetView();
				resetSearchResultTableValues();
				fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null, null);
				
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
					//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
					//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
					List<CreateAndSearchLotTableDTO> requestTableList = finalDataList;
					for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
						
						if(createAndSearchLotTableDTO.getLegalFirstName()!=null && !createAndSearchLotTableDTO.getLegalFirstName().isEmpty()){
							SelectValue payeeName = new SelectValue();
							payeeName.setId(0l);
							payeeName.setValue(createAndSearchLotTableDTO.getLegalFirstName());
							createAndSearchLotTableDTO.setPayeeName(payeeName);
						}
						
						if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
							chkBoxValue = true;
					}
					//if(null != chkBox && chkBox.getValue())
					if(chkBoxValue  || (null != chkBox && null != chkBox.getValue() && chkBox.getValue()) || (null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue()) )
					{
						//getTableDataForReport();
						//secondLayout.addComponent(searchResultTable);
						//tableForExcel.setVisible(false);
						//excelExport = new  ExcelExport(searchResultTable.getTable());
						if(null != selectAllChkBox && null != selectAllChkBox.getValue() && selectAllChkBox.getValue())
						{
							getCompleteTableDataForReport(requestTableList);
						}
						else
						{	
							getTableDataForReport();
						}
						secondLayout.addComponent(tableForExcel);
						tableForExcel.setVisible(false);
						ExcelExport excelExport = new  ExcelExport(tableForExcel.getTable());
						//excelExport.setDisplayTotals(displayTotals);
						//excelExport.
						excelExport.excludeCollapsedColumns();
						excelExport.setDisplayTotals(false);
						//excelExport.set
						excelExport.setReportTitle("Create And Search Lot Data");
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
		
		
	}
	
	private void getTableDataForReport()
	{
		if(null != tableForExcel)
		{
			tableForExcel.removeRow();
			
			//List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableAllItems();
			List<CreateAndSearchLotTableDTO> requestTableList = searchResultTable.getTableItems();
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
	
	private void getCompleteTableDataForReport(List<CreateAndSearchLotTableDTO> requestTableList)
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
			BeanItemContainer<SelectValue> docVerified, BeanItemContainer<SelectValue> paymentMode,
			BeanItemContainer<SelectValue> selectValueContainerForVerificationType) {
		
		lotForm.setDropDownValues(type, cpuCode, claimant, claimType, paymentStatus,product,paymentMode,selectValueContainerForVerificationType);
		searchResultTable.setDropDownValues(cpuCode,docVerified);
		
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
					if(searchResultTable!=null){
	                    searchResultTable.clearObject();
					}
					UI.getCurrent().removeWindow(popup);
				}
				if(null != popUp){
					popUp.close();
				}
				
				

				
			}
		});
	}

	
	@Override
	public void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		// TODO Auto-generated method stub
		searchResultTable.setUpIFSCDetails(viewSearchCriteriaDTO,updatePaymentDetailTableDTO);

	}

	@Override
	public void setPaymentCpu(
			CreateAndSearchLotTableDTO updatePaymentDetailTableDTO) {
		//searchResultTable.setPaymentCpuName(updatePaymentDetailTableDTO);
		
	}

	
	
	@Override
	public void buildResultantTableLayout(String layoutType) {

		resetSearchResultTableValues();
		lotForm.resetFlds();
		searchResultTable.compMap=null;
		chkBox.setValue(null);
		//resetView();
		if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.CREATE_LOT).equalsIgnoreCase(layoutType)){
				searchResultTable.initPresenterString(SHAConstants.CREATE_LOT);
				//searchResultTable.initTable();
				searchResultTable.init(SHAConstants.CREATE_LOT, Boolean.TRUE);
				tableForExcel.setVisibleColumnsForCreateLot();
			}
			else if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.SEARCH_LOT).equalsIgnoreCase(layoutType)){
				searchResultTable.initPresenterString(SHAConstants.SEARCH_LOT);
				//searchResultTable.initTable();
				searchResultTable.init(SHAConstants.SEARCH_LOT, Boolean.TRUE);
				tableForExcel.setVisibleColumnsForSearchLot();
			}
		
		if (null != mainPanel
				&& mainPanel.getComponentCount() > 0) {
			mainPanel.removeAllComponents();
		}
		mainPanel.setFirstComponent(lotForm);
		mainPanel.setSecondComponent(buildButtonLayoutBasedOnType(layoutType));
		mainPanel.getSecondComponent().setVisible(false);
		//mainPanel.addComponent(buildButtonLayoutBasedOnType(layoutType));
		
				
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
			hLayout = buildButtonsLayout();
		}
		else
		{
			hLayout = new HorizontalLayout();
			hLayout.setSpacing(true);
			
			if(null != layoutType && !("").equalsIgnoreCase(layoutType) && (SHAConstants.CREATE_LOT).equalsIgnoreCase(layoutType)){
				hLayout.addComponent(btnSave);
				hLayout.addComponent(btnSendToChecker);
				hLayout.addComponent(btnCancel);
			}
			else
			{
				hLayout.addComponent(btnCancel);
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
	
	

	@Override
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,
			EditPaymentDetailsView editPaymentView) {
		searchResultTable.populatePreviousPaymentDetails(tableDTO,editPaymentView);
		// TODO Auto-generated method stub
		
	}

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

				//fireViewEvent(MenuItemBean.CREATE_OR_SEARCH_LOT, null, null);
				
			}
		});
		
	}
	
	public void setSplitPosition(String searchType){
		
		CreateAndSearchLotFormDTO searchDTO = lotForm.getSearchDTO();
		
		if(SHAConstants.NORMAL_SEARCH.equalsIgnoreCase(searchType)){
			mainPanel.setSplitPosition(37);
			searchDTO.setSearchTabType(SHAConstants.NORMAL_SEARCH);
			mainPanel.removeComponent(buildSecondComponent());
			searchResultTable.resetTable();
			searchResultTable.resetTableDataList();
		}
		else
		{
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
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadDataInWindow(Page<CreateAndSearchLotTableDTO> tableRows,String layoutType, SelectValue vType){
		
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

		HorizontalLayout footerButtons = buildButtonsLayout();
		
		/*Below buttons removed for search lot - IMSSUPPOR-28529*/
		if(layoutType != null && layoutType.equalsIgnoreCase(SHAConstants.SEARCH_LOT)){
            footerButtons.removeComponent(btnSave);
            footerButtons.removeComponent(btnSendToChecker);
            footerButtons.removeComponent(btnCancel);
		}

		addListener();
		
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
				searchResultTable.getPageable().setPageNumber(1);
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
				 if(searchResultTable!=null){
                     searchResultTable.clearObject();
				 }

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
				searchResultTable.setIsSearchBtnClicked(false);
				//System.out.println("Close listener called");
			}
		});
		
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
		// TODO Auto-generated method stub
		
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

public void resetData(){
	if(! searchResultTable.getIsSearchBtnClicked())
	{
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.clearExistingList();searchResultTable.setIsSearchBtnClicked(true);
		if(null != selectAllChkBox){
			selectAllChkBox.setValue(false);
		}
	}
}

@Override
public void setUpPayableDetails(String payableName,CreateAndSearchLotTableDTO tableDTO) {
	searchResultTable.setUpPayableAmt(payableName, tableDTO);
}

}
