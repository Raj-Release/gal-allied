package com.shaic.reimbursement.uploadrodreports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.utils.StarCommonUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.CommonFileUpload;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.viewEarlierRodDetails.EarlierRodDetailsViewImpl;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsReimbursementTableDTO;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.InvestigationDetailsTable;
import com.shaic.reimbursement.draftinvesigation.DraftTriggerPointsToFocusDetailsTableDto;
import com.shaic.reimbursement.draftinvesigation.InvestigatorTriggerPointsTable;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Sizeable;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class UploadInvestigationReportUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Label lblInvestigationDetailsTable;

	private VerticalLayout mainLayout;
	
	private Button btnViewEODEarlierDetails;
	
	private TextField lblAckNumber;
	
	private Window popup;

	@Inject
	private InvestigationDetailsTable investigationDetailsTable;

	@Inject
	private UploadInvestigationReportLayout uploadInvestigationReportLayout;

	private List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTO;
	
	private Label lblUploadInvestigationReport;

	private Long investigationKey = 0l;
	
	private Button submitBtn;
	
	private Button saveBtn;
	
    private Button cancelBtn;
    
    private HorizontalLayout buttonHorLayout;
    private FormLayout form1;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
//	@Inject
//	private UploadedDocumentsUI uploadedDocumentsUI;
	
	@Inject
	private UploadedDocumentsTable uploadedDocumentsTable;
	
	@Inject
	private CommonFileUpload fileUploadUI;
	
	@Inject
	private ViewDetails viewDetails;
	
	@Inject
	private ClaimDto claimDto;
	
	@EJB
	private InvestigationService investigationService;
		
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private ClaimService claimService;
	
	private Table table;
	
	@Inject
	private EarlierRodDetailsViewImpl earlierRodDetailsViewImpl;
	
	private SearchUploadInvesticationTableDTO bean;
	
	private Long rodKey = 0l;
	
	private Long investigationAssignedKey;
	
	private String count = null;
	
	PreauthDTO preauthDTO = null;
	
	private TextArea txtClaimBackgroundDetails ;
	
	private TextArea txtFactsOfCase;
	
	@Inject
	private InvestigatorTriggerPointsTable investigationTriggerPointTable;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;
	
	private TextField investigationCompletionDate;
	
	private TextArea txtInvestigationCompletionRemarks;

	public void init(SearchUploadInvesticationTableDTO bean) {
		this.bean = bean;
		this.investigationKey = bean.getInvestigationKey();
		this.rodKey = bean.getRodKey();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		String assigndKey = wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? String.valueOf(wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY)) : null;
		this.investigationAssignedKey =  assigndKey != null ? Long.valueOf(assigndKey) : null; 
		this.bean.setInvestigationAssignedKey(investigationAssignedKey);
		preauthDTO.setRrcDTO(bean.getRrcDTO());
		fireViewEvent(
				UploadInvestigationReportPresenter.SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS,
				investigationKey,this.investigationAssignedKey,this.rodKey);
	}
	
	public void init(SearchUploadInvesticationTableDTO bean,PreauthDTO preauthDTO) {
		this.bean = bean;
		this.preauthDTO = preauthDTO;
		this.investigationKey = bean.getInvestigationKey();
		Map<String, Object> wrkFlowMap = (Map<String, Object>) bean.getDbOutArray();
		
		String assignedKey = wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY) != null ? String.valueOf(wrkFlowMap.get(SHAConstants.PAYLOAD_RRC_REQUEST_KEY)) : null;
		
		this.investigationAssignedKey =  assignedKey != null ? Long.valueOf(assignedKey) : null; 
		this.rodKey = bean.getRodKey();
		this.bean.setInvestigationAssignedKey(investigationAssignedKey);
		preauthDTO.setRrcDTO(bean.getRrcDTO());
		
		if(SHAConstants.YES_FLAG.equalsIgnoreCase(bean.getPreauthDTO().getNewIntimationDTO().getInsuredDeceasedFlag())) {
			
			SHAUtils.showAlertMessageBox(SHAConstants.INSURED_DECEASED_ALERT);
		}
		
		DBCalculationService dbService = new DBCalculationService();
		Boolean fraudFlag = dbService.getFraudFlag(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId(),
				bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber(),bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode(),
				bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode(),bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode());
		if(fraudFlag)
		{
			if(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getIntimationId());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Intimation "+bean.getPreauthDTO().getNewIntimationDTO().getIntimationId(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Policy "+bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getPolicyNumber(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Hospital "+bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalCode(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Hospital IRDA "+bean.getPreauthDTO().getNewIntimationDTO().getHospitalDto().getHospitalIrdaCode(),"Information");
				}
			}
			if(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode() != null)
			{
				Boolean result = dbService.getFraudAlertValue(bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode());
				if(result)
				{
					SHAUtils.showMessageBoxWithCaption("Suspicious found in Intermediary "+bean.getPreauthDTO().getNewIntimationDTO().getPolicy().getAgentCode(),"Information");
				}
			}
		}
		
		fireViewEvent(
				UploadInvestigationReportPresenter.SET_REFERENCE_FOR_INVESTGATION_DETAILS_TABLE_UPLOAD_REPORTS,
				investigationKey,this.investigationAssignedKey,this.rodKey);
	}

	public void setCompleteLayout(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		
		
		// R1152
		if (!this.bean.getIsGeoSame()) {
			getGeoBasedOnCPU();
		}//CR2019112
		else if(bean.getPreauthDTO().getIsSuspicious()!=null){
			StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
		}
		
		this.investigationDetailsReimbursementTableDTO = investigationDetailsReimbursementTableDTOList;
		
		preauthIntimationDetailsCarousel.init(claimDto.getNewIntimationDto(), claimDto, "Upload Investigation Report");
		
		lblAckNumber = new TextField("Acknowledgement Number");
		lblAckNumber.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		lblAckNumber.setValue(this.count);
		//FormLayout form1 = new FormLayout(lblAckNumber);
		form1 = new FormLayout(lblAckNumber);
	//	form1.setHeight("60px");
		btnViewEODEarlierDetails = new Button("View Earlier ROD Details");
		viewDetails.initView(claimDto.getNewIntimationDto().getIntimationId(),this.rodKey, ViewLevels.UPLOAD_INVESTIGATION_REPORT, false,"Upload Investigation Report");
		
		//HorizontalLayout viewDetailsLayout = new HorizontalLayout(form1, btnViewEODEarlierDetails, viewDetails);
		HorizontalLayout viewDetailsLayout = new HorizontalLayout(commonButtonsLayout(),viewDetails);
		viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);
	//	viewDetailsLayout.setComponentAlignment(viewDetails, Alignment.MIDDLE_RIGHT);
		viewDetailsLayout.setSpacing(true);
		//viewDetailsLayout.setMargin(true);
		viewDetailsLayout.setSizeFull();
		
		lblInvestigationDetailsTable = new Label("<H3>Investigation Details</H3>",ContentMode.HTML);
//		lblInvestigationDetailsTable.setStyleName(Reindeer.LABEL_H2);
		investigationDetailsTable.init("", false, false);
		investigationDetailsTable.setSeviceObjects(hospitalService,claimService,investigationService,masterService);
		investigationDetailsTable.setViewDMSDocViewPage(viewDetails.getDmsDocumentDetailsViewPage());
		investigationDetailsTable.setVisibleColumns();
		
		txtClaimBackgroundDetails = new TextArea("Claim Background Details");
		txtClaimBackgroundDetails.setRequired(true);
		txtClaimBackgroundDetails.setValue(bean.getClaimBackgroundDetails());
		txtClaimBackgroundDetails.setReadOnly(true);	
		txtClaimBackgroundDetails.setWidth("500px");
		txtClaimBackgroundDetails.setId("clmBackgroundDetails");
		txtClaimBackgroundDetails.setData(bean);
		handleRemarksPopup(txtClaimBackgroundDetails, null);
		txtClaimBackgroundDetails.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		txtClaimBackgroundDetails.setNullRepresentation("");
		
 
		txtFactsOfCase = new TextArea("Facts Of Case");
		txtFactsOfCase.setRequired(true);
		txtFactsOfCase.setValue(bean.getFactsOfCase());
		txtFactsOfCase.setReadOnly(true);
		txtFactsOfCase.setWidth("500px");
		txtFactsOfCase.setId("invFactsofCase");
		txtFactsOfCase.setData(bean);
		txtFactsOfCase.setNullRepresentation("");
		handleRemarksPopup(txtFactsOfCase, null);
		txtFactsOfCase.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		investigationTriggerPointTable.init("Investigator Trigger Points", false,false);
		Map<String, Object> draftrefData = new HashMap<String, Object>();
		//investigationTriggerPointTable.setReference(draftrefData);
		
		FormLayout bgAndFactsOfCaseFLayout = new FormLayout(txtClaimBackgroundDetails,txtFactsOfCase,investigationTriggerPointTable);
		
		VerticalLayout bgAndFactsOfCaseVLayout = new VerticalLayout(bgAndFactsOfCaseFLayout); 
		
		investigationCompletionDate = new TextField("Date Of Completion");
		String dateformat =new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		investigationCompletionDate.setValue(dateformat);
		investigationCompletionDate.setReadOnly(true);
		
		txtInvestigationCompletionRemarks =  new TextArea("Investigation Completion Remarks");
		txtInvestigationCompletionRemarks.setRequired(true);
		txtInvestigationCompletionRemarks.setReadOnly(false);
		txtInvestigationCompletionRemarks.setWidth("500px");
		txtInvestigationCompletionRemarks.setId("InvComplnRemarks");
		txtInvestigationCompletionRemarks.setData(bean);
		handleRemarksPopup(txtInvestigationCompletionRemarks, null);
		txtInvestigationCompletionRemarks.setDescription(SHAConstants.FVR_TRIGGER_POINTS_F8);
		
		FormLayout dateAndRemarksFLayout = new FormLayout(investigationCompletionDate,txtInvestigationCompletionRemarks);
		VerticalLayout dateAndRemarksVLayout = new VerticalLayout(dateAndRemarksFLayout);
		
		uploadInvestigationReportLayout.init();
		
		
		lblUploadInvestigationReport = new Label("<H3>Upload Investigation Report</H3>",ContentMode.HTML);
//		lblUploadInvestigationReport.setStyleName(Reindeer.LABEL_H2);
		
		Label lblUploadDocumentsTable = new Label("<H3>Uploaded Documents</H3>",ContentMode.HTML);
		
		uploadedDocumentsTable.init(" ",false,false);
		getUploadedTableValues(investigationAssignedKey);
		
		saveBtn = new Button("Save");
	    submitBtn=new Button("Submit");
		cancelBtn=new Button("Cancel");

		saveBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		saveBtn.setWidth("-1px");
		saveBtn.setHeight("-1px");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(saveBtn,submitBtn,cancelBtn);
	
		buttonHorLayout.setSpacing(true);
		
		fileUploadUI.init(this.rodKey,ReferenceTable.UPLOAD_INVESTIGATION_SCREEN,SHAConstants.CLAIM_VERIFICATION_REPORT);
		fileUploadUI.setSearchUploadInvTableDto(this.bean);
		fileUploadUI.setAssignedKey(investigationAssignedKey);
		
		table = new Table();
		table.setPageLength(table.size()+2);
		table.setHeight("150px");
		table.addContainerProperty("File Upload", CommonFileUpload.class, null);
		table.addContainerProperty("File Type", String.class, null);
		Object addItem = table.addItem();
	    table.getContainerProperty(addItem, "File Upload").setValue(fileUploadUI);
		
		mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel, viewDetailsLayout, lblInvestigationDetailsTable,
				investigationDetailsTable, lblUploadInvestigationReport,  bgAndFactsOfCaseVLayout ,fileUploadUI,lblUploadDocumentsTable,uploadedDocumentsTable,buttonHorLayout,dateAndRemarksVLayout);
		mainLayout.setComponentAlignment(lblUploadDocumentsTable, Alignment.MIDDLE_LEFT);
		mainLayout.setComponentAlignment(buttonHorLayout, Alignment.MIDDLE_CENTER);
//		mainLayout.setComponentAlignment(uploadInvestigationReportLayout,
//				Alignment.MIDDLE_LEFT);
		mainLayout.setSpacing(true);
//		mainLayout.setMargin(true);
		addListener();
		setTableData();
		setCompositionRoot(mainLayout);
	}
	
	private void addListener(){
		btnViewEODEarlierDetails.addClickListener(new Button.ClickListener() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				earlierRodDetailsViewImpl.init(claimDto.getKey(),bean.getKey());
				popup = new com.vaadin.ui.Window();
				popup.setCaption("");
				popup.setWidth("75%");
				popup.setHeight("75%");
				popup.setContent(earlierRodDetailsViewImpl);
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
				
			}
		});
		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	releaseHumanTask();
				                	fireViewEvent(MenuItemBean.UPLOAD_INVESTIGATION_REPORT, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		saveBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				//if(validatePage())
				{
				
					List<UploadedDocumentsDTO> values = uploadedDocumentsTable.getValues();
					if(null != txtInvestigationCompletionRemarks)
						bean.setInvestigationCompletionRemarks(txtInvestigationCompletionRemarks.getValue());
					if(values != null && ! values.isEmpty()){
						String userId = String.valueOf(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID));
						bean.setUploadedFileTableDto(values);
						bean.setUploadedNDeletedFileListDto(uploadedDocumentsTable.getdeletedList());
						fireViewEvent(UploadInvestigationReportPresenter.SAVE_EVENT, investigationKey,bean);
					}else{
						showErrorPopUp("Please upload atleast one file");
					}
				}

			}
		});

		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage())
				{
					List<UploadedDocumentsDTO> values = uploadedDocumentsTable.getValues();
					if(null != txtInvestigationCompletionRemarks)
						bean.setInvestigationCompletionRemarks(txtInvestigationCompletionRemarks.getValue());
					if(values != null && ! values.isEmpty()){
						String userId = String.valueOf(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID));
						bean.setUsername(userId);
						bean.setUploadedFileTableDto(values);
						bean.setUploadedNDeletedFileListDto(uploadedDocumentsTable.getdeletedList());
						fireViewEvent(UploadInvestigationReportPresenter.SUBMIT_EVENT, investigationAssignedKey, investigationKey,bean);
					}else{
						showErrorPopUp("Please upload atleast one file");
					}
				}

			}
		});
		
	}
	
	private void releaseHumanTask(){
		
		Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

 		if(existingTaskNumber != null){
 			//BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
 			getSession().setAttribute(SHAConstants.TOKEN_ID, null);
 		}
 		
 		if(wrkFlowKey != null){
 			DBCalculationService dbService = new DBCalculationService();
 			dbService.callUnlockProcedure(wrkFlowKey);
 			getSession().setAttribute(SHAConstants.WK_KEY, null);
 		}
	}
	public void showErrorPopUp(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
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
	
	public void setReference(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public void setReferenceForDto(
			List<InvestigationDetailsReimbursementTableDTO> investigationDetailsReimbursementTableDTOList) {
		this.investigationDetailsReimbursementTableDTO = investigationDetailsReimbursementTableDTOList;
	}
	
//	public void setUploadTableValues(Long investigationAssignedKey){
//		uploadedDocumentsTable.removeRow();
//		uploadedDocumentsTable.setTableList(investigationService.getUploadDocumentList(investigationAssignedKey));
//		
//	}
	
	public void getUploadedTableValues(Long investigationAssignedKey){
		uploadedDocumentsTable.removeRow();
		uploadedDocumentsTable.setTableList(investigationService.getUploadDocumentList(investigationAssignedKey));
		
	}

	public void setUploadTableValues(List<UploadedDocumentsDTO> uploadDocDtoList){
//		uploadedDocumentsTable.removeRow();
		List<UploadedDocumentsDTO> existList = new ArrayList<UploadedDocumentsDTO>(); 
		if(uploadedDocumentsTable.getValues() != null){
			existList.addAll(uploadedDocumentsTable.getValues());
		}
		if(uploadDocDtoList != null && !uploadDocDtoList.isEmpty()){
			for (UploadedDocumentsDTO uploadedDocumentsDTO : existList) {
				uploadedDocumentsDTO.setSno(existList.indexOf(uploadedDocumentsDTO)+1);
			}
			existList.addAll(uploadDocDtoList);
		}
		
		uploadedDocumentsTable.setTableList(existList);
		this.bean.setUploadedFileTableDto(existList);
		
	}
	
	public List<UploadedDocumentsDTO> getUploadedTableValues(){
		List<UploadedDocumentsDTO> existList = new ArrayList<UploadedDocumentsDTO>();
		if(uploadedDocumentsTable.getValues() != null){
			existList.addAll(uploadedDocumentsTable.getValues());
		}
		return existList;
	}
	public void setTableData() {
		int sno =1;
		if(investigationDetailsReimbursementTableDTO != null && ! investigationDetailsReimbursementTableDTO.isEmpty()){
			for (InvestigationDetailsReimbursementTableDTO tableDto : investigationDetailsReimbursementTableDTO) {
				investigationDetailsTable.addBeanToList(tableDto);
				tableDto.setSno(sno);
				sno++;
			}
		}
		investigationDetailsTable
				.setTableList(investigationDetailsReimbursementTableDTO);
		investigationDetailsTable.setRadioButtonr(this.investigationAssignedKey);
		
		
		
			if(null != investigationTriggerPointTable)
			{
				List<DraftTriggerPointsToFocusDetailsTableDto> investigatorTriggerPointsList = this.bean.getInvestigatorTriggerPointsList();
				for(int i =0 ; i<investigatorTriggerPointsList.size() ; i++)
				{
					DraftTriggerPointsToFocusDetailsTableDto draftTriggerPointsToFocusDetailsTableDto = investigatorTriggerPointsList.get(i);
					draftTriggerPointsToFocusDetailsTableDto.setSerialNumber(i);
					investigationTriggerPointTable.addBeanToList(draftTriggerPointsToFocusDetailsTableDto);
				}
				//investigationTriggerPoints.setTableList(this.bean.getInvestigatorTriggerPointsList());
			}
		
	}
	
	public void setAcknowledgmentNumber(Long count){
		if(count != null){
		this.count = count.toString();
		}
		
	}
	
	public VerticalLayout commonButtonsLayout()
	{
		Button btnRRC = new Button("RRC");
		btnRRC.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				validateUserForRRCRequestIntiation();
				
			}
			
		});
		
		HorizontalLayout crmIconLayout = SHAUtils.newImageCRM(bean.getPreauthDTO());
		HorizontalLayout crmLayout = new HorizontalLayout(crmIconLayout);
		crmLayout.setSpacing(true);
		crmLayout.setWidth("100%");
		//HorizontalLayout hopitalFlag = SHAUtils.hospitalFlag(bean.getPreauthDTO());
		VerticalLayout hflayout=new VerticalLayout(crmLayout/*, hopitalFlag*/);
		
		HorizontalLayout hLayout = new HorizontalLayout(hflayout,btnRRC,btnViewEODEarlierDetails);
		hLayout.setSpacing(true);
		
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
		crmFlaggedComponents.setWidth("100%");
		
		HorizontalLayout horLayout = new HorizontalLayout(form1,crmFlaggedComponents);
		
		VerticalLayout vLayout = new VerticalLayout(hLayout,horLayout);
		return vLayout;
	}
	
	private void validateUserForRRCRequestIntiation()
	{
		fireViewEvent(UploadInvestigationReportPresenter.VALIDATE_UPLOAD_INVESTIGATION_REPORT_USER_RRC_REQUEST, preauthDTO);//, secondaryParameters);
	}
	
public boolean validatePage() {
		
		Boolean hasError = false;
		//showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();
		Boolean isReconsiderationRequest = false;

	
			//ReconsiderRODRequestTableDTO reconsiderDTO = this.bean.getReconsiderRODdto();
					if((null != txtInvestigationCompletionRemarks && (null == txtInvestigationCompletionRemarks.getValue() || (null != txtInvestigationCompletionRemarks.getValue() && txtInvestigationCompletionRemarks.getValue().isEmpty()))))
					{
						hasError = true;
						eMsg.append("Please Enter Investigation CompletionRemarks </br>");
					}

		
		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} 
		return true;
				
	}

public  void handleRemarksPopup(TextArea searchField, final  Listener listener) {
	
    ShortcutListener enterShortCut = new ShortcutListener(
        "ShortcutForRemarks", ShortcutAction.KeyCode.F8, null) {
	
      private static final long serialVersionUID = 1L;
      @Override
      public void handleAction(Object sender, Object target) {
        ((ShortcutListener) listener).handleAction(sender, target);
      }
    };
    handleShortcutForRedraft(searchField, getShortCutListenerForRedraftRemarks(searchField));
    
  }

public  void handleShortcutForRedraft(final TextArea textField, final ShortcutListener shortcutListener) {
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
private ShortcutListener getShortCutListenerForRedraftRemarks(final TextArea txtFld)
{
	ShortcutListener listener =  new ShortcutListener("Remarks",KeyCodes.KEY_F8,null) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void handleAction(Object sender, Object target) {
			SearchUploadInvesticationTableDTO searchUploadInvtableDto = (SearchUploadInvesticationTableDTO) txtFld.getData();
			VerticalLayout vLayout =  new VerticalLayout();
			
			vLayout.setWidth(100.0f,Sizeable.UNITS_PERCENTAGE);
			vLayout.setHeight(Sizeable.SIZE_UNDEFINED,Sizeable.UNITS_PERCENTAGE);
			vLayout.setMargin(true);
			vLayout.setSpacing(true);
			final TextArea txtArea = new TextArea();
//			txtArea.setStyleName("Boldstyle"); 
			txtArea.setValue(txtFld.getValue());
			txtArea.setNullRepresentation("");
			txtArea.setSizeFull();
			txtArea.setWidth("100%");
			txtArea.setMaxLength(4000);

			if(("InvComplnRemarks").equalsIgnoreCase((txtFld.getId()))){
				txtArea.setRows(25);
				txtArea.setReadOnly(false);
			}
			else{
				String remarksValue = "";	
				if(("clmBackgroundDetails").equalsIgnoreCase(txtFld.getId())){
					remarksValue = searchUploadInvtableDto.getClaimBackgroundDetails() != null ? searchUploadInvtableDto.getClaimBackgroundDetails() : "";
	//				txtArea.setRows(searchUploadInvtableDto.getClaimBackgroundDetails() != null ? (searchUploadInvtableDto.getClaimBackgroundDetails().length()/80 >= 25 ? 25 : ((searchUploadInvtableDto.getClaimBackgroundDetails().length()/80)%25)+1) : 25);
				}
				
				if(("invFactsofCase").equalsIgnoreCase(txtFld.getId())){
					remarksValue = searchUploadInvtableDto.getFactsOfCase() != null ? searchUploadInvtableDto.getFactsOfCase() : "";
	//				txtArea.setRows(searchUploadInvtableDto.getFactsOfCase() != null ? (searchUploadInvtableDto.getFactsOfCase().length()/80 >= 25 ? 25 : ((searchUploadInvtableDto.getFactsOfCase().length()/80)%25)+1) : 25);
				}
				
				txtArea.setReadOnly(true);
				
//				String splitArray[] = remarksValue.split("\n");
				String splitArray[] = remarksValue.split("[\n*|.*]");
				
				if(splitArray != null && splitArray.length > 0 && splitArray.length > 25){
					txtArea.setRows(25);
				}
				else{
					txtArea.setRows(splitArray.length);
				}
			}
			
			txtArea.addValueChangeListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					
					if(("InvComplnRemarks").equalsIgnoreCase(txtFld.getId()) || ("InvComplnRemarks").equalsIgnoreCase(txtFld.getId())){
						
						txtFld.setValue(((TextArea)event.getProperty()).getValue());		
						
					}	
				}
			});
			
			Button okBtn = new Button("OK");
			okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			vLayout.addComponent(txtArea);
			vLayout.addComponent(okBtn);
			vLayout.setComponentAlignment(okBtn,Alignment.BOTTOM_CENTER);
			
			final Window dialog = new Window();
			
			String strCaption = "";
			
			if(("InvComplnRemarks").equalsIgnoreCase((txtFld.getId()))){
				strCaption = "Investigation Completion Remarks";
			}
			if(("clmBackgroundDetails").equalsIgnoreCase((txtFld.getId()))){
				strCaption = "Claim Background Details";
			}
			if(("invFactsofCase").equalsIgnoreCase(txtFld.getId())){
				strCaption = "Facts of the Case";
			}
			
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
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getRedraftRemarks());
					dialog.close();
				}
			});
			
			if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
				dialog.setPositionX(450);
				dialog.setPositionY(500);
			}
			getUI().getCurrent().addWindow(dialog);
			okBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
//					TextArea txtArea = (TextArea)dialog.getData();
//					txtArea.setValue(bean.getRedraftRemarks());
					dialog.close();
				}
			});	
		}
	};
	
	return listener;
}
public void getGeoBasedOnCPU() {
	 
	 Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.GEO_PANT_HOSP_ALERT_MESSAGE + "</b>",
				ContentMode.HTML);
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if(bean.getPreauthDTO().getIsSuspicious()!=null){
					StarCommonUtils.showPopup(getUI(), bean.getPreauthDTO().getIsSuspicious(), bean.getPreauthDTO().getClmPrcsInstruction());
				}
				 
			}
		});
	}

}
