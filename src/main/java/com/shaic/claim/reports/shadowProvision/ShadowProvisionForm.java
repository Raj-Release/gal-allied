package com.shaic.claim.reports.shadowProvision;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.common.APIService;
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
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShadowProvisionForm extends ViewComponent implements Receiver,SucceededListener{
	
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
	
	@Inject
	private SearchShadowProvisionTable searchShadowProvisonTableObj;
	
	@Inject
	private SearchShowdowDownloadTable searchShadowDownloadTableObj;
	
	public void init(){
		
		TabSheet mainTab = new TabSheet();
		//Vaadin8-setImmediate() mainTab.setImmediate(true);
		
		mainTab.setSizeFull();
		
		mainTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		TabSheet downloadTapSheet = getDownloadTapSheet();
		TabSheet uploadTabSheet = getUploadTabSheet();
		
		mainTab.addTab(downloadTapSheet,"Download",null);
		mainTab.addTab(uploadTabSheet,"Upload",null);
		mainVerticalLayout = new VerticalLayout(mainTab);
		
		Panel mainPanel = new Panel(mainVerticalLayout);
		mainPanel.setCaption("Revision of Provision");
		setCompositionRoot(mainPanel);

		
	}
	
	public Button getExportButtonToExcel(){
		return btnExportToExcel;
	}
	
	public TabSheet getUploadTabSheet(){
		
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

	    HorizontalLayout horizontalLayout = new HorizontalLayout(upload,btnUpload);
		
	    searchShadowDownloadTableObj.init("", false, false);
	   
		
		VerticalLayout mainVertical = new VerticalLayout(horizontalLayout,searchShadowDownloadTableObj);
		firstTabSheet.addComponent(mainVertical);
		
		  btnUpload.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					upload.submitUpload();
				}
			});
		
		return firstTabSheet;

	}
	
	public TabSheet getDownloadTapSheet(){
		
		TabSheet firstTabSheet = new TabSheet();
		firstTabSheet.hideTabs(true);
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		firstTabSheet.setWidth("100%");
		firstTabSheet.setHeight("100%");
		firstTabSheet.setSizeFull();
		//Vaadin8-setImmediate() firstTabSheet.setImmediate(true);
		
	    VerticalLayout dummyVertical = new VerticalLayout();
	    
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
		
		
		searchShadowProvisonTableObj.init("", false, false);
		
		VerticalLayout mainVertical = new VerticalLayout(dummyVertical,currentDateField,btnExportToExcel);	
		mainVertical.setSpacing(true);
		HorizontalLayout mainHor = new HorizontalLayout(dummyVertical1,mainVertical);
		mainHor.setComponentAlignment(mainVertical, Alignment.BOTTOM_CENTER);
		firstTabSheet.addComponent(mainVertical);
		
		addListner();

		return firstTabSheet;

	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		FileInputStream fis = null;
		try {
			if(file.exists() && !file.isDirectory()) {
			fis = new FileInputStream(file);
			org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
			Sheet sheetAt = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheetAt.iterator();
			
			int successCount = 0;
			int errorCount = 0;
			int totalCount = 0;
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			DBCalculationService dbCalculationService = new DBCalculationService();
			String batchNumerForShadowProvision = dbCalculationService.getBatchNumerForShadowProvision();
			
			while (rowIterator.hasNext()){
				try{
					if(file.exists() && !file.isDirectory()) { 
				Row row = rowIterator.next();
				
				if(row.getRowNum() != 0){
					
					Cell intimationNumberCell = row.getCell(0);
					//Cell cpuCell = row.getCell(1);
					//Cell existingProvisionCell = row.getCell(2);
					Cell newProvisionCell = row.getCell(3);
					
					String intimationNumber = intimationNumberCell.getStringCellValue();
//					String cpuCode= cpuCell.getStringCellValue();
					//Double existingProvision = existingProvisionCell.getNumericCellValue();
					Double revisedProvision = newProvisionCell.getNumericCellValue();
					
					Claim claim = claimService.getClaimsByIntimationNumber(intimationNumber);
					
					if(claim != null){
						totalCount++;
						ProvisionUploadHistory provisionHistory = new ProvisionUploadHistory();
						provisionHistory.setClaimKey(claim.getKey());
						provisionHistory.setClaimNumber(claim.getClaimId());
						provisionHistory.setIntimationKey(claim.getIntimation().getKey());
						provisionHistory.setIntimationNumber(claim.getIntimation().getIntimationId());
						provisionHistory.setExistingProvisionAmt(claim.getCurrentProvisionAmount());
						provisionHistory.setCurrentProvisonAmt(revisedProvision);
						provisionHistory.setStatus(claim.getStatus());
						provisionHistory.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						
						List<Reimbursement> previousRODByClaimKey = createRodService.getPreviousRODByClaimKey(claim.getKey());
						Boolean isAnyRodSettled = false;
						for (Reimbursement reimbursement : previousRODByClaimKey) {
							if(reimbursement.getStatus().getKey().equals(ReferenceTable.FINANCIAL_APPROVE_STATUS)){
								isAnyRodSettled = true;
							}
						}
						if( claim.getStatus() != null && !claim.getStatus().getKey().equals(ReferenceTable.INTIMATION_REGISTERED_STATUS)){
							provisionHistory.setRemarks("This is not an Orphan claim");
							errorCount += 1;
							provisionHistory.setUserId(userName);
							provisionHistory.setStatusFlag("F");
						}
						else if((revisedProvision > claim.getCurrentProvisionAmount())){
							provisionHistory.setRemarks("Provision Amount exceeded");
							errorCount += 1;
							provisionHistory.setStatusFlag("F");
						}else if(claim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
							
							provisionHistory.setRemarks("Claim is Closed");
							errorCount += 1;
							provisionHistory.setStatusFlag("F");
							
						}else if(isAnyRodSettled){
							provisionHistory.setRemarks("Rod is already approved in FA");
							errorCount += 1;
							provisionHistory.setStatusFlag("F");
						}
						
						else{
							provisionHistory.setRemarks("SUCCESS");
							provisionHistory.setUserId(userName);
							provisionHistory.setStatusFlag("S");
							claim.setRevisedProvisionAmount(revisedProvision);
							claimService.updateClaimForRevisedProvision(claim);
		
							successCount += 1;
						}
						
						provisionHistory.setBatchNumber(batchNumerForShadowProvision);
						
						claimService.submitProvisionHistory(provisionHistory);
						if(provisionHistory.getStatusFlag() != null && provisionHistory.getStatusFlag().equalsIgnoreCase("S")){
							updateProvisionAmountToPremia(claim);
						}
						
						
					}
				}
					}
				
				}catch(Exception e){
					Notification.show("Error", "" + "Please upload excel with Valid format", Type.ERROR_MESSAGE);
					break;
				} 
			}
			
			
			SearchShadowProvisionDTO resultDto = new SearchShadowProvisionDTO();
		    resultDto.setSuccessCount(successCount+"/"+totalCount);
		    resultDto.setErrorCount(errorCount+"/"+totalCount);
		    resultDto.setBatchNumber(batchNumerForShadowProvision);
		    searchShadowDownloadTableObj.removeRow();
		    //searchShadowDownloadTableObj.setMainVerticalLayout(mainVerticalLayout);
		    searchShadowDownloadTableObj.addBeanToList(resultDto);
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
			this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			if(null != file && (file.getName().endsWith("xlsx") || file.getName().endsWith("xls")))
			{
				fos = new FileOutputStream(file);
			}
			else
			{
				Notification.show("Error", "" + "Please select excel file Only", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {/* comented for error handled
			Notification.show("Error", ""
			+ "Please select the file to be uploaded",
			Type.ERROR_MESSAGE);*/
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
	
	public void addListner(){
		btnExportToExcel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				fireViewEvent(StarFaxSimulatePresenter.SUBMIT_CONVERT_CLAIM, txtCLoseCLaim.getValue().toString());
				DBCalculationService dbcalculationService = new DBCalculationService();
				List<SearchShadowProvisionDTO> downloadForProvisonExcel = dbcalculationService.getDownloadForProvisonExcel(new Date());
				
				//String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
				
				
				searchShadowProvisonTableObj.init("", false, false);
				
				
				mainVerticalLayout.addComponent(searchShadowProvisonTableObj);
				searchShadowProvisonTableObj.setVisible(false);
				
				for (SearchShadowProvisionDTO searchShadowProvisionDTO : downloadForProvisonExcel) {
					searchShadowProvisonTableObj.addBeanToList(searchShadowProvisionDTO);
				}			
				excelExport = new  ExcelExport(searchShadowProvisonTableObj.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setDisplayTotals(false);
//				excelExport.setReportTitle("Revision of Provision");
				excelExport.export();
			
//				fireViewEvent(StarFaxSimulatePresenter.AUTO_CLOSE_CLAIM_PROCESS, txtCLoseCLaim.getValue().toString());

			}
		});
	}
	
	
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

}
