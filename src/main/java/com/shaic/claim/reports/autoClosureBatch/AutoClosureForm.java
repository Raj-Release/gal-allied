package com.shaic.claim.reports.autoClosureBatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.claim.common.APIService;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PolicyService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.ReportLog;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.reimbursement.manageclaim.closeclaim.pageClaimLevel.CloseClaimPageDTO;
import com.shaic.reimbursement.queryrejection.generateremainder.search.SearchGenerateReminderTableDTO;
import com.shaic.starfax.simulation.PremiaPullService;
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

public class AutoClosureForm extends ViewComponent implements Receiver,SucceededListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Logger log = LoggerFactory.getLogger(AutoClosureForm.class);

	@EJB
	private ClaimService claimService;
	
	@EJB
	private PolicyService policyService;
	
	@EJB
	private APIService apiService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@EJB
	private IntimationService intimationService;
	
	@EJB
	private PremiaPullService premiaPullService;
	
	@EJB
	private CreateRODService createRodService;

	private Upload upload;
	
	private Button btnUpload;
	
	private PopupDateField currentDateField;
	
	private Button btnExportToExcel;
	
	@EJB
	private PreauthService preauthService;

	private VerticalLayout mainVerticalLayout;
	
	private File file;
	
	
	public void init(){
		
		TabSheet mainTab = new TabSheet();
		//Vaadin8-setImmediate() mainTab.setImmediate(true);
		
		mainTab.setSizeFull();
		
		mainTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		
		TabSheet uploadTabSheet = getUploadTabSheet();
		
		mainTab.addTab(uploadTabSheet,"Upload",null);
		mainVerticalLayout = new VerticalLayout(mainTab);
		
		Panel mainPanel = new Panel(mainVerticalLayout);
		mainPanel.setCaption("Auto Closure Batch");
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
		
		VerticalLayout mainVertical = new VerticalLayout(horizontalLayout);
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
		
		VerticalLayout mainVertical = new VerticalLayout(dummyVertical,currentDateField);	
		mainVertical.setSpacing(true);
		HorizontalLayout mainHor = new HorizontalLayout(dummyVertical1,mainVertical);
		mainHor.setComponentAlignment(mainVertical, Alignment.BOTTOM_CENTER);
		firstTabSheet.addComponent(mainVertical);

		return firstTabSheet;

	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		/*AutoClosureScheduler batchScheduler = new AutoClosureScheduler();
		batchScheduler.submitAutoCloseClaim(file);*/
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		if(file != null){
			if(file.exists() && !file.isDirectory()) {
			FileInputStream fis = null;
			try {
				
				fis = new FileInputStream(file);
				org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);
				Sheet sheetAt = workbook.getSheetAt(0);
				
				Iterator<Row> rowIterator = sheetAt.iterator();
				claimService.popinReportLog(userName, "AUTO_CLOSURE", new Date(),new Date(),SHAConstants.RPT_BEGIN);
				int count = 1;
				while (rowIterator.hasNext()){
					try{
					Row row = rowIterator.next();
					
					if(row.getRowNum() != 0){
						
						
						submityRemainderLetterGeneration(row);

						Cell cellIntimationNumber = row.getCell(0);
						Cell cell = row.getCell(4);
						String intimationNumber = cellIntimationNumber.getStringCellValue();
						
						Date dateCellValue = cell.getDateCellValue();
						Claim objClaim = preauthService.getClaimsByIntimationNumber(intimationNumber);
						
						if(objClaim != null && ! objClaim.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)){
						
							CloseClaimPageDTO bean = new CloseClaimPageDTO();
							bean.setClaimKey(objClaim.getKey());
							bean.setCloseRemarks("");
							bean.setClosedProvisionAmt(objClaim.getCurrentProvisionAmount());
							SelectValue selected = new SelectValue();
							selected.setId(2506l);
							selected.setValue("To Be Considered");
							bean.setReasonId(selected);
							bean.setCloseRemarks("Auto closure");
							bean.setUserName(userName);
							bean.setIntimationNumber(intimationNumber);
							bean.setClosedDate(dateCellValue);
							updateClaimClose(bean,dateCellValue);
							preauthService.updateStageInformation(objClaim, dateCellValue);
							
						    log.info("Auto closure for Intimation number -----------!!!!!!!!!!!!!!!!!!>"+ intimationNumber);
						    log.info("Count ------------> "+ count);
						    count++;
						}

					}

					
					}catch(Exception e){
						Notification.show("Error", "" + "Please upload excel with Valid format", Type.ERROR_MESSAGE);
						break;
					} 
				}
				claimService.popinReportLog(userName, "AUTO_CLOSURE", new Date(),new Date(),SHAConstants.RPT_SUCCESS);

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
		}else {
			Notification.show("Error", StringUtils.EMPTY + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
		}
		}
		
	}

	private void submityRemainderLetterGeneration(Row row) {
		Date createdDate = null;
		Date firstReminderDate = null;
		Date secondReminderDate = null;
		Date finalClosureDate = null;
		String intimationNo = null;
		
		Cell createDateCell = row.getCell(1);
		Cell firstRemDateCell = row.getCell(2);
		Cell secondRemDateCell = row.getCell(3);
		Cell finalClosureDateCell = row.getCell(4);
		Cell intimationNoCell = row.getCell(0);
		
		if(null != createDateCell)
		{
			createdDate = createDateCell.getDateCellValue();
//							createdDate = SHAUtils.formatDateWithoutTime(cell.getDateCellValue().toString());
		}
		
		if(null != firstRemDateCell)
		{
			firstReminderDate = firstRemDateCell.getDateCellValue();
		}
		if(null != secondRemDateCell)
		{
			secondReminderDate = secondRemDateCell.getDateCellValue();
		}
		if(null != finalClosureDateCell)
		{
			finalClosureDate = finalClosureDateCell.getDateCellValue();
		}
		if(null != intimationNoCell)
		{
			intimationNo = intimationNoCell.getStringCellValue();
		}
		uploadReminderLetterToDMS(intimationNo , createdDate , firstReminderDate , secondReminderDate , finalClosureDate );
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
		} catch (FileNotFoundException e) {
//			Notification.show("Error", "" + "Please select excel file Only", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		//Added for error
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
	
	
private void updateClaimClose(CloseClaimPageDTO bean,Date dateCellValue) {
		
		
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(BPMClientContext.BPMN_TASK_USER, BPMClientContext.BPMN_PASSWORD);
		
//----------------------------------------------------Reimbursement Task -----------------------------------------------------------------------------
//		CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask("claimshead", BPMClientContext.BPMN_PASSWORD);
//		
//		PayloadBOType payloadType = new PayloadBOType();
//		IntimationType intimationType = new IntimationType();
//		intimationType.setIntimationNumber(bean.getIntimationNumber());
//		payloadType.setIntimation(intimationType);
//		
//		PagedTaskList tasks = closeClaimTask.getTasks("claimshead", null, payloadType);
//		
//		List<HumanTask> humanTasks = tasks.getHumanTasks();
		
		List<Reimbursement> reimbursementList = new ArrayList<Reimbursement>();
		
		reimbursementList = createRodService.getReimbursementListForClose(bean.getIntimationNumber());

//		for (HumanTask humanTask : humanTasks) {
//			
//			if(humanTask.getPayload() != null && humanTask.getPayload().getRod() != null 
//					&& humanTask.getPayload().getRod().getKey() != null){
//				
//				Long reimbursementKey = humanTask.getPayload().getRod().getKey();
//				Reimbursement reimbursement = createRodService.getReimbursementByKey(reimbursementKey);
//				reimbursementList.add(reimbursement);
//			}
//		}
		
		Boolean isClaimLevel = true;
		
		Boolean anyRodActive = createRodService.isAnyRodActive(bean.getIntimationNumber());
		if(anyRodActive){
			isClaimLevel = false;
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
			
			for (Reimbursement reimbursement : reimbursementList) {
				
				if(! ReferenceTable.getCloseClaimKeys().containsKey(reimbursement.getStatus().getKey())){
				
				dbCalculationService.reimbursementRollBackProc(reimbursement.getKey(),"R");
				
				createRodService.updateReimbursmentForCloseClaim(bean.getUserName(), reimbursement);
				
				Reimbursement hospitalizationRod = reimbursementService.getHospitalizationRod(reimbursement.getClaim().getKey(), reimbursement.getKey());
				
				
				if(reimbursement.getStage().getKey().equals(ReferenceTable.BILLING_STAGE)
						|| reimbursement.getStage().getKey().equals(ReferenceTable.FINANCIAL_STAGE)){
					
					if(reimbursement.getClaim().getClaimType() != null && reimbursement.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
						if(hospitalizationRod == null){
							if(reimbursement.getClaim().getLatestPreauthKey() != null){
//								dbCalculationService.invokeAccumulatorProcedure(reimbursement.getClaim().getLatestPreauthKey());
							}else{
								Preauth latestPreauth = reimbursementService.getLatestPreauthByClaim(reimbursement.getClaim().getKey());
//								dbCalculationService.invokeAccumulatorProcedure(latestPreauth.getKey());
							}
						}else{
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}else{
						if(hospitalizationRod != null){
//							dbCalculationService.invokeReimbursementAccumulatorProcedure(hospitalizationRod.getKey());
						}
					}
				}
				
				dbCalculationService.updateProvisionAmount(reimbursement.getKey(), reimbursement.getClaim().getKey());
				
				if(! isClaimLevel){
					PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());
					createRodService.submitCloseClaimForRodLevel(bean,reimbursement);
				}
				
				}
				
			}
			
			
			
		   if(isClaimLevel){
			   PremiaService.getInstance().UnLockPolicy(bean.getIntimationNumber());
			   createRodService.submitCloseClaim(bean);
		   }

	}

private void uploadReminderLetterToDMS(String intimationNo,Date createdDate,Date remDate1 , Date remDate2, Date finalClosureDate)
{
	Claim objClaim = preauthService.getClaimsByIntimationNumber(intimationNo);

	if(null != objClaim)	
	{
		SearchGenerateReminderTableDTO reminderLetterDto = new SearchGenerateReminderTableDTO();
		ClaimDto claimDTO =  ClaimMapper.getInstance().getClaimDto(objClaim);
		claimDTO.setModifiedDate(createdDate);
		Intimation objIntimation = preauthService.getIntimationByNo(intimationNo);
		NewIntimationDto intimationDto = intimationService.getIntimationDto(objIntimation);
		reminderLetterDto.setClaimDto(claimDTO);
		reminderLetterDto.getClaimDto().setNewIntimationDto(intimationDto);
		//reminderLetterDto.getReimbQueryDto().getReimbursementDto().getClaimDto().setNewIntimationDto(intimationDto);
		
		if(null != remDate1)
		{
			reminderLetterDto.getClaimDto().setReminderCount(1);
			reminderLetterDto.setPresenterString("First Reminder");
			reminderLetterDto.getClaimDto().setFirstReminderDate(remDate1);
			premiaPullService.uploadReminderLetterToDMS(reminderLetterDto);

		}
		if(null != remDate2)
		{
			reminderLetterDto.getClaimDto().setFirstReminderDate(remDate1);
			reminderLetterDto.getClaimDto().setSecondReminderDate(remDate2);
			reminderLetterDto.getClaimDto().setReminderCount(2);
			reminderLetterDto.setPresenterString("Second Reminder");
			premiaPullService.uploadReminderLetterToDMS(reminderLetterDto);

		}
		if(null != finalClosureDate)
		{
			reminderLetterDto.getClaimDto().setSecondReminderDate(remDate2);
			reminderLetterDto.getClaimDto().setThirdReminderDate(finalClosureDate);
			reminderLetterDto.getClaimDto().setReminderCount(3);
			reminderLetterDto.setPresenterString("Third Reminder");
			premiaPullService.uploadReminderLetterToDMS(reminderLetterDto);

		}
		

		
	
	}
	
}

}
