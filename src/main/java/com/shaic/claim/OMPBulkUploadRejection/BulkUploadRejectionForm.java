package com.shaic.claim.OMPBulkUploadRejection;

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
import java.util.WeakHashMap;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.common.APIService;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUWisePreauthResultDto;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportDto;
import com.shaic.claim.reports.cpuwisePreauthReport.CPUwisePreauthReportPresenter;
import com.shaic.claim.reports.shadowProvision.ErrorLogTable;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionDTO;
import com.shaic.claim.reports.shadowProvision.SearchShadowProvisionTable;
import com.shaic.claim.reports.shadowProvision.SearchShowdowDownloadTable;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.ProvisionUploadHistory;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;

public class BulkUploadRejectionForm extends ViewComponent implements Receiver,SucceededListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private ClaimService claimService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;
	
	
	@EJB
	private CreateRODService createRodService;
	
	@EJB
	private OMPBulkUploadRejectionService ompBulkUploadRejectionService; 
	
	@Inject
	private ErrorLogTable errorLogTable;

	
	private Upload upload;
	
	private Button btnUpload;
	
	private PopupDateField currentDateField;
	
	private Button btnExportToExcel;
	
	//private Button downloadBtn;
	
	private ExcelExport excelExport;
	
	private VerticalLayout mainVerticalLayout;
	
	private File file;
	
	private List<Component> mandatoryFields = new ArrayList<Component>();

	
	PopupDateField fromDate; 
	PopupDateField toDate; 
	TextField rodNumber;
	TextField podNumber;
	Button searchBtn;
	Button resetBtn;
	VerticalLayout searchVerticalLayout;
	VerticalLayout wholeVerticalLayout;
	Panel searchPanel;
	HorizontalLayout buttonLayout;
	Button exportBtn;
	Label dummyLabel;

	ComboBox cmdUploadType; 
	ComboBox cmdDownloadType; 

	@Inject
	private BulkUploadRejectionTable bulkUploadRejectionTableObj;
	
	public void init(BeanItemContainer<SelectValue> statusContainer){
		
		TabSheet mainTab = new TabSheet();
		//Vaadin8-setImmediate() mainTab.setImmediate(true);
		
		mainTab.setSizeFull();
		
		mainTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		TabSheet downloadTapSheet = getDownloadTapSheet(statusContainer);
		TabSheet uploadTabSheet = getUploadTabSheet(statusContainer);

		mainTab.addTab(downloadTapSheet,"Download",null);
		mainTab.addTab(uploadTabSheet,"Upload",null);
		mainVerticalLayout = new VerticalLayout(mainTab);
		
		Panel mainPanel = new Panel(mainVerticalLayout);
		mainPanel.setCaption("BULK Dispatch Upload & Download");
		setCompositionRoot(mainPanel);

		
	}
	
	public Button getExportButtonToExcel(){
		return btnExportToExcel;
	}
	
	public TabSheet getUploadTabSheet(BeanItemContainer<SelectValue> statusContainer){
		
		TabSheet firstTabSheet = new TabSheet();
		firstTabSheet.hideTabs(true);
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		firstTabSheet.setWidth("100%");
		firstTabSheet.setHeight("100%");
		firstTabSheet.setSizeFull();
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
	    
	    btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		cmdUploadType = new ComboBox("Upload Type");
		cmdUploadType.setContainerDataSource(statusContainer);
		cmdUploadType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdUploadType.setItemCaptionPropertyId("value");
		
		mandatoryFields.add(cmdUploadType);
		
		showOrHideValidation(false);

		
		FormLayout leftFormLayout = new FormLayout(upload);
		leftFormLayout.setMargin(false);
		
		FormLayout rightFormLayout = new FormLayout(cmdUploadType);
		rightFormLayout.setMargin(false);
		
		FormLayout btnFormLayout = new FormLayout(btnUpload);
		btnFormLayout.setMargin(false);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(leftFormLayout,rightFormLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout(horizontalLayout,btnFormLayout);
		verticalLayout.setSpacing(true);

	    bulkUploadRejectionTableObj.init("", false, false);
	   

	    firstTabSheet.addComponent(verticalLayout);
		
		  btnUpload.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					SelectValue uploadTypeSelect = (SelectValue) cmdUploadType.getValue();
					if(uploadTypeSelect != null && uploadTypeSelect.getValue() != null && uploadTypeSelect.getValue() != "") {
						upload.submitUpload();
					} else {
						showErrorPopup("Please select Upload Type");
					}
				}
			});
		
		return firstTabSheet;

	}
	
	public TabSheet getDownloadTapSheet(BeanItemContainer<SelectValue> statusContainer){
		
		TabSheet firstTabSheet = new TabSheet();
		firstTabSheet.hideTabs(true);
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		firstTabSheet.setWidth("100%");
		firstTabSheet.setHeight("100%");
		firstTabSheet.setSizeFull();
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		
/*	    VerticalLayout dummyVertical = new VerticalLayout();
	    
	    TextArea dummyField = new TextArea();
	    dummyField.setEnabled(false);
	    dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	    dummyField.setWidth("1000px");
	    dummyVertical.addComponent(dummyField);
	    dummyVertical.setWidth("1000px");
	    
	    VerticalLayout dummyVertical1 = new VerticalLayout();
	    
	    TextArea dummyField1 = new TextArea();
	    dummyField1.setEnabled(false);
	    dummyField1.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
	    dummyField1.setWidth("1000px");
	    dummyVertical1.addComponent(dummyField);
	    dummyVertical1.setWidth("1000px");
		
		
		currentDateField = new PopupDateField();
		currentDateField.setValue(new Date());
		currentDateField.setEnabled(false);
		
		btnExportToExcel = new Button("Export to Excel");
		
		
		bulkUploadRejectionTableObj.init("", false, false);
		
		VerticalLayout mainVertical = new VerticalLayout(dummyVertical,currentDateField,btnExportToExcel);	
		mainVertical.setSpacing(true);
		HorizontalLayout mainHor = new HorizontalLayout(dummyVertical1,mainVertical);
		mainHor.setComponentAlignment(mainVertical, Alignment.BOTTOM_CENTER);*/
		ExcelExport excelExport;
		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");
		 searchVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("200px");
		 
		 fromDate = new PopupDateField("From Date");
		 fromDate.setDateFormat("dd/MM/yyyy");
		 fromDate.setTextFieldEnabled(false);
			
		 toDate = new PopupDateField("To Date");
		 toDate.setDateFormat("dd/MM/yyyy");
		 toDate.setTextFieldEnabled(false);
		
		
		
		 rodNumber = new TextField("ROD No");
		 //Vaadin8-setImmediate() policyNumber.setImmediate(false);
		 rodNumber.setWidth("180px");
		 rodNumber.setTabIndex(2);
		 rodNumber.setHeight("-1px");
		 
		 podNumber = new TextField("POD No");
		 //Vaadin8-setImmediate() policyNumber.setImmediate(false);
		 podNumber.setWidth("180px");
		 podNumber.setTabIndex(2);
		 podNumber.setHeight("-1px");

		 dummyLabel =new Label();
		 dummyLabel.setWidth("30px");
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();

		cmdDownloadType = new ComboBox("Upload Type");
		cmdDownloadType.setContainerDataSource(statusContainer);
		cmdDownloadType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmdDownloadType.setItemCaptionPropertyId("value");
		
		mandatoryFields.add(cmdDownloadType);
		
		showOrHideValidation(false);


		FormLayout leftForm = new FormLayout(rodNumber,fromDate,cmdDownloadType);
		FormLayout rightForm = new FormLayout(podNumber,toDate);
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);
		frmLayout.setMargin(true);
		frmLayout.setSpacing(true);
		
		
	
		
		
		searchBtn = new Button("Search");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchBtn.setDisableOnClick(true);
		searchBtn.addStyleName("hover");
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		searchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				searchBtn.setEnabled(false);
				Map<String,Object> filter = getSearchFileterValues(); 
				if(!filter.isEmpty()){
					fireViewEvent(OMPBulkUploadRejectionPresenter.SEARCH_BULK_UPLOAD_REJECTION, filter);
				}				
			}
		});
		
		resetBtn = new Button("Reset");
		resetBtn.setWidth("-1px");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();
			}
		});
		
		buttonLayout = new HorizontalLayout();

		Label dummyLabel2 =new Label();
		dummyLabel2.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel2,resetBtn);
			buttonLayout.setSpacing(true);

		absoluteLayout_3.addComponent(frmLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:130.0px;left:200.0px;");

		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		firstTabSheet.addComponent(searchVerticalLayout);
		
//	check	addListner();

		return firstTabSheet;

	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		if(cmdUploadType != null && cmdUploadType.getValue() != null) {
			FileInputStream fis = null;
			try {
				
				fis = new FileInputStream(file);
				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
				Sheet sheetAt = workbook.getSheetAt(0);
				
				Iterator<Row> rowIterator = sheetAt.iterator();
				
				int successCount = 0;
				int errorCount = 0;
				int totalCount = 0;
				
//				DBCalculationService dbCalculationService = new DBCalculationService();
//				String batchNumerForShadowProvision = dbCalculationService.getBatchNumerForShadowProvision();
				
				List <OmpRejectionBulkUpload> ompRejectionBulkUploadList = new ArrayList<OmpRejectionBulkUpload>();
				SelectValue uploadTypeSelect = (SelectValue) cmdUploadType.getValue();
				String uploadType =  uploadTypeSelect.getValue();
				String uploadTypeKey= null;
				Date dateOfDispatchString ;
				String podNumber = null;
				
				if(uploadTypeSelect != null && uploadType.equalsIgnoreCase("discharge voucher")) {
					uploadTypeKey = "D";
				} else if(uploadType != null && uploadType.equalsIgnoreCase("rejection flow")) {
					uploadTypeKey = "R";
				}
				while (rowIterator.hasNext()){
					try{
					Row row = rowIterator.next();
					
					if(row.getRowNum() != 0){
						
						totalCount ++;
						Cell rodNumberCell = row.getCell(0);
						Cell dateOfDispatchCell = row.getCell(1);
						Cell podNumberCell = row.getCell(2);
						Cell modeOfDispatchCell = row.getCell(3);
						Cell remarksCell = row.getCell(4);

						String rodNumber = rodNumberCell.getStringCellValue();
						if(dateOfDispatchCell.getCellType() == Cell.CELL_TYPE_STRING){
							String stringCellValue = dateOfDispatchCell.getStringCellValue();
							dateOfDispatchString = new SimpleDateFormat("dd/MM/yyyy").parse(stringCellValue);
						}else{
							 dateOfDispatchString = dateOfDispatchCell.getDateCellValue();
						}
						if(podNumberCell.getCellType() == Cell.CELL_TYPE_STRING){
							 podNumber = podNumberCell.getStringCellValue();
						}else{
							int numericCellValue = (int) podNumberCell.getNumericCellValue();
							podNumber = String.valueOf(numericCellValue);
						}
						String modeOfDispatch = modeOfDispatchCell.getStringCellValue();
						String remarks = remarksCell.getStringCellValue();
					    
//						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//
//						Date dateOfDispatch = dateOfDispatchString != null ? sdf.parse(dateOfDispatchString) : null;
						
//						String cpuCode= cpuCell.getStringCellValue();
						//Double existingProvision = existingProvisionCell.getNumericCellValue();
						
						OmpRejectionBulkUpload ompRejectionBulkUpload = new OmpRejectionBulkUpload();
						ompRejectionBulkUpload.setRodNumber(rodNumber);
						ompRejectionBulkUpload.setDateOfDispatch(dateOfDispatchString != null ? dateOfDispatchString/*new Timestamp(dateOfDispatchString.getTime())*/ : null);
						ompRejectionBulkUpload.setPodNumber(podNumber);
						ompRejectionBulkUpload.setModeOfDispatch(modeOfDispatch);
						ompRejectionBulkUpload.setRemarks(remarks);
						//get userid
						String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
						ompRejectionBulkUpload.setCreatedBy(userName);
						Date  currentDate = new Date();
						ompRejectionBulkUpload.setCreatedDate(new Timestamp(currentDate.getTime()));
						ompRejectionBulkUpload.setActiveStatus(1);
						ompRejectionBulkUpload.setUploadType(uploadTypeKey);
						
						ompRejectionBulkUploadList.add(ompRejectionBulkUpload);
						successCount ++;
					}
					
					}catch(Exception e){
						errorCount ++;
						Notification.show("Error", "" + "Please upload excel with Valid format", Type.ERROR_MESSAGE);
						break;
					} 
				}
				
				if(!ompRejectionBulkUploadList.isEmpty()){
					ompBulkUploadRejectionService.persistOmpRejectionBulkUploadList(ompRejectionBulkUploadList);
					showSuccessPopup("Successfully Uploaded");
				    cmdUploadType.setValue(null);
				}
				
//				SearchShadowProvisionDTO resultDto = new SearchShadowProvisionDTO();
//			    resultDto.setSuccessCount(successCount+"/"+totalCount);
//			    resultDto.setErrorCount(errorCount+"/"+totalCount);
//			    resultDto.setBatchNumber("");
//			    bulkUploadRejectionTableObj.removeRow();
			    //bulkUploadRejectionTableObj.setMainVerticalLayout(mainVerticalLayout);
//			    bulkUploadRejectionTableObj.addBeanToList(resultDto);
		
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(fis != null){
					try {
						fis.close();
						upload  = new Upload("", this);	
					    upload.addSucceededListener(this);
					    upload.setButtonCaption(null);
					    } catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public void updateProvisionAmountToPremia(Claim claim){
		String strPremiaFlag = BPMClientContext.PREMIA_FLAG;
		if(strPremiaFlag != null && ("true").equalsIgnoreCase(strPremiaFlag)) {
			try {
				Hospitals hospitalDetailsByKey = hospitalService.getHospitalDetailsByKey(claim.getIntimation().getHospital());
				
				String provisionAmtInput = SHAUtils.getProvisionAmtInput(claim, hospitalDetailsByKey.getName(), String.valueOf(claim.getCurrentProvisionAmount().longValue()));
				apiService.updateProvisionAmountToPremia(provisionAmtInput);
			}catch(Exception e){
				
			}
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fos = null;
		try {
			this.file = null;
			if(filename!=null && !(filename.isEmpty()))
			{
			this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			if(null != file && (file.getName().endsWith("xlsx") || file.getName().endsWith("xls")))
			{
				fos = new FileOutputStream(file);
			}
			else
			{
				Notification.show("Error", "" + "Please select excel file Only", Type.ERROR_MESSAGE);
			}
			}else{
				Notification.show("Error", "" + "Please select excel file", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			Notification.show("Error", "" + "Please select excel file Only", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
			return fos;
	}
//*************Check	
/*	public void addListner(){
		btnExportToExcel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				fireViewEvent(StarFaxSimulatePresenter.SUBMIT_CONVERT_CLAIM, txtCLoseCLaim.getValue().toString());
				DBCalculationService dbcalculationService = new DBCalculationService();
				List<OMPBulkUploadRejection> downloadForProvisonExcel = dbcalculationService.getDownloadForProvisonExcel(new Date());
				
				//String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				
				
				bulkUploadRejectionTableObj.init("", false, false);
				
				
				mainVerticalLayout.addComponent(searchShadowProvisonTableObj);
				bulkUploadRejectionTableObj.setVisible(false);
				
				for (OMPBulkUploadRejectionResultDto searchShadowProvisionDTO : downloadForProvisonExcel) {
					bulkUploadRejectionTableObj.addBeanToList(searchShadowProvisionDTO);
				}			
				excelExport = new  ExcelExport(bulkUploadRejectionTableObj.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setDisplayTotals(false);
//				excelExport.setReportTitle("Revision of Provision");
				excelExport.export();
			
//				fireViewEvent(StarFaxSimulatePresenter.AUTO_CLOSE_CLAIM_PROCESS, txtCLoseCLaim.getValue().toString());

			}
		});
	}
	*/
	
	public void exportToErrorLog(List<SearchShadowProvisionDTO> errorLogDetailsForShadow){
		
		errorLogTable.init("", false, false);
		
		mainVerticalLayout.addComponent(errorLogTable);
		errorLogTable.setVisible(false);
		
		errorLogTable.setTableList(errorLogDetailsForShadow);
		
		excelExport = new  ExcelExport(errorLogTable.getTable());
		excelExport.excludeCollapsedColumns();
		excelExport.setDisplayTotals(false);
//		excelExport.setReportTitle("Revision of Provision");
		excelExport.export();
	}

	private Map<String, Object>getSearchFileterValues(){
		WeakHashMap<String, Object> searchfilters = new WeakHashMap<String, Object>();
		Boolean hasError = false;
		Boolean hasTypeError = false;
		StringBuffer errorMessage = new StringBuffer();
				
		if(fromDate.getValue() != null && toDate.getValue() != null){
			if(fromDate.getValue().compareTo(toDate.getValue()) > 0){
				errorMessage.append("<br>From date should not be greater than the To date");
				hasError = true;
			}
			else{
				searchfilters.put("fromDate", fromDate.getValue());	
				searchfilters.put("toDate", toDate.getValue());	
			}
		} else if ((fromDate.getValue() == null && toDate.getValue() != null) || (fromDate.getValue() != null && toDate.getValue() == null)) {
			errorMessage.append("<br>Please Select both From Date and To Date");
			hasError = true;
		}
		
		if(rodNumber.getValue() != null){
			searchfilters.put("rodNumber", rodNumber.getValue());	
		}
		if(podNumber.getValue() != null){
			searchfilters.put("podNumber", podNumber.getValue());
		}
		if(cmdDownloadType.getValue() != null){
			searchfilters.put("uploadType", cmdDownloadType.getValue());
		} else {
			hasTypeError = true;
		}
		if(searchfilters.isEmpty()){
			errorMessage.append("<br>Please Enter Atleast one field value for Search");
			hasError = true;
		}
		
		if(hasError){
			showErrorPopup(errorMessage.toString());
			searchfilters.clear();			
		} else if(hasTypeError) {
			showErrorPopup("<br>Please Enter the Type for Search");
			searchfilters.clear();			
		}
		return searchfilters;
		
	}
	
	public void showErrorPopup(String eMsg)
	{
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
	searchBtn.setEnabled(true);
	}
	
	public void showTableResult(List<OMPBulkUploadRejectionResultDto> bulkUploadRejectionList){
		
		List<OMPBulkUploadRejectionResultDto> resultDtoList = bulkUploadRejectionList;
		
		searchBtn.setEnabled(true);
		if(resultDtoList != null && !resultDtoList.isEmpty()){
			bulkUploadRejectionTableObj.setTableList(resultDtoList);			
			searchVerticalLayout.addComponent(bulkUploadRejectionTableObj);

			
			if(exportBtn == null){
				addExportButton();
			}
		}
		else{
			searchBtn.setEnabled(true);
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn = null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Bulk Upload Rejection Home");
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
					resetAlltheValues();
					fireViewEvent(MenuItemBean.OMP_BULK_UPLOAD_REJECTION, null);
				}
			});	
		}
		
	}
	
	public void resetAlltheValues(){
		fromDate.setValue(null);
		toDate.setValue(null);
		rodNumber.setValue("");
		podNumber.setValue("");
		cmdDownloadType.setValue(null);

		bulkUploadRejectionTableObj.setTableList(new ArrayList<OMPBulkUploadRejectionResultDto>());
		searchBtn.setEnabled(true);
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
	}
	private void addExportButton() {
		exportBtn = new Button("Export to Excel");
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(bulkUploadRejectionTableObj.getTable());
				excelExport.setReportTitle("Bulk Upload Rejection Details");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
	}
	
	public void showSuccessPopup(String eMsg)
	{
	Label label = new Label(eMsg, ContentMode.HTML);
//	label.setStyleName("errMessage");
	VerticalLayout layout = new VerticalLayout();
	layout.setMargin(true);
	layout.addComponent(label);

	ConfirmDialog dialog = new ConfirmDialog();
	dialog.setCaption("Success");
	dialog.setClosable(true);
	dialog.setContent(layout);
	dialog.setResizable(false);
	dialog.setModal(true);
	dialog.show(getUI().getCurrent(), null, true);
	}
	
}

