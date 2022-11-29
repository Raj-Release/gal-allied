package com.shaic.claim.pcc.zonalMedicalHead;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.CommonFileUpload;
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.processor.ProcessPccProcessorRequestPresenter;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.QueryDetailsTable;
import com.shaic.claim.pcc.views.ReplyDetailsTable;
import com.shaic.claim.pcc.wizard.ProcessPccCoOrdinateRequestPresenter;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsGridForm;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsPageTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.tables.FileUploadComponent;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.preauth.PccRemarks;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsDTO;
import com.shaic.reimbursement.uploadrodreports.UploadedDocumentsTable;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ProcessPccZonalMedicalHeadRequestPage extends ViewComponent {



	@Inject
	private Instance<ProcessingDoctorDetailsTable> processingDoctorDetailsTableInst;
	private ProcessingDoctorDetailsTable processingDoctorDetailsTable;
	
	@Inject
	private Instance<QueryDetailsTable> queryDetailsTableInst;
	private QueryDetailsTable queryDetailsTable;

	@Inject
	private Instance<ReplyDetailsTable> replyDetailsTableInst;
	private ReplyDetailsTable replyDetailsTable;

	
	@Inject
	private ProcessPCCUploadDocumentsPageTable uploadedDocumentsTable;
	
	@Inject
	private ProcessPCCUploadDocumentsGridForm fileUploadUI;
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<PCCUploadDocumentsDTO> uploadedTblList = new ArrayList<PCCUploadDocumentsDTO>();
	
	
	private Long pccKey;
	
	//End
	
	private Button submitBtn;
	
	private Button cancelBtn;

	private PccDTO bean;

	private BeanFieldGroup<PccDTO> binder;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PccDetailsTableDTO pccDetailsTableDTO;
	
	private String presenterString;
	
	
	private ComboBox cmbSelectRole;
	
	private ComboBox cmbSelectUserName;
	
	private TextArea txtAssignReamrks;
	
	private TextArea txtAssignNegotioanReamrks;

	private Boolean isResponse = false;
	
	private Boolean isAssign = false;
	
	//private Boolean isChecked = false;
	
	private Button btnResponse;
	
	private Button btnAssign;
	
	private TextField txtNegotiaitedAmmount;

	private TextField txtSavedAmount;

    private VerticalLayout dyanamicVLayout;
	
	private VerticalLayout mainVLayout;
	
	private FormLayout responseLayout;
	private FormLayout assignLayout;
	private FormLayout negotiationLayout;
	
	private OptionGroup isNegotiation;
	
	private FormLayout dummyForm;
	private TextField dummytext;
	
	private TextField txtNoOfDocUploaded = new TextField();
	
	private int uploadFileCount = 0;
	
	
	private BeanItemContainer<SelectValue> userDetailsContainer;
	
	@EJB
	private ZonalMedicalHeadRequestService zonalMedicalHeadRequestService;
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;
	
	@PostConstruct
	protected void initView() {

	}

	public void init(PccDetailsTableDTO pccDetailsTableDTO, String presenterString) {
		
		this.presenterString=presenterString;
		this.pccDetailsTableDTO = pccDetailsTableDTO;
		//this.bean=bean;
		
		this.binder = new BeanFieldGroup<PccDTO>(PccDTO.class);
		this.binder.setItemDataSource(new PccDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		uploadDocsPanel = new Panel();
		uploadedDocsPanel = new Panel();
		
		dummyForm = new FormLayout();
		dummyForm.setWidth("500px");
		dummyForm.setHeight("45px");
		
		dummytext = new TextField();
		dummytext.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		dummytext.setWidth("-1px");
		dummytext.setHeight("-10px");
		
		
		cmbSelectRole = binder.buildAndBind("Select Role","userRoleAssigned",ComboBox.class);	
		cmbSelectUserName = binder.buildAndBind("Select User Name","userNameAssigned",ComboBox.class);

		txtAssignReamrks = binder.buildAndBind("Remarks", "remarksAssignforZMH", TextArea.class);
		txtAssignReamrks.setMaxLength(4000);
		txtAssignReamrks.setWidth("278px");
		txtAssignReamrks.setHeight("130px");
		txtAssignReamrks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtAssignReamrks,null,getUI(),SHAConstants.STP_REMARKS);
		
		
		txtAssignNegotioanReamrks = binder.buildAndBind("Negotioation Remarks", "remarksNegotioanforZMH", TextArea.class);
		txtAssignNegotioanReamrks.setMaxLength(1000);
		txtAssignNegotioanReamrks.setWidth("278px");
		txtAssignNegotioanReamrks.setHeight("130px");
		txtAssignNegotioanReamrks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtAssignNegotioanReamrks,null,getUI(),SHAConstants.STP_REMARKS);
		
	
		isNegotiation = (OptionGroup) binder.buildAndBind("Is Negotiation sucessful", "isNegotiation", OptionGroup.class);
		isNegotiation.addItems(getReadioButtonOptions());
		isNegotiation.setItemCaption(true, "Yes");
		isNegotiation.setItemCaption(false, "No");
		isNegotiation.setStyleName("horizontal");
		
		txtNegotiaitedAmmount=binder.buildAndBind("Negotiated Amount","negotiatioAmount",TextField.class);
		txtNegotiaitedAmmount.setNullRepresentation("");
		CSValidator txtAmtToNeg = new CSValidator();
		txtAmtToNeg.setRegExp("^[0-9']*$");
		txtAmtToNeg.setPreventInvalidTyping(true);
		txtAmtToNeg.extend(txtNegotiaitedAmmount);
		
		txtSavedAmount = binder.buildAndBind("Saved Amount","savedAmount",TextField.class);
		txtSavedAmount.setNullRepresentation("");
		CSValidator txtSaveAmt = new CSValidator();
		txtSaveAmt.setRegExp("^[0-9']*$");
		txtSaveAmt.setPreventInvalidTyping(true);
		txtSaveAmt.extend(txtSavedAmount);
		txtSavedAmount.addBlurListener(setNegotiationsaveListener());
		
		processingDoctorDetailsTable = processingDoctorDetailsTableInst.get();
		processingDoctorDetailsTable.init("", false, false);
		processingDoctorDetailsTable.setCaption("Processing Doctor Details");
		if(pccDetailsTableDTO !=null){
			List<PccDetailsTableDTO> pccDetailsTableDTOs = new ArrayList<PccDetailsTableDTO>();
			pccDetailsTableDTOs.add(pccDetailsTableDTO);
			processingDoctorDetailsTable.setTableList(pccDetailsTableDTOs);
		}
		
		queryDetailsTable = queryDetailsTableInst.get();
		queryDetailsTable.init("", false, false);
		queryDetailsTable.setCaption("Query Details");
		if(pccDetailsTableDTO.getQueryDetails() !=null 
				&& !pccDetailsTableDTO.getQueryDetails().isEmpty()){
			queryDetailsTable.setTableList(pccDetailsTableDTO.getQueryDetails());
		}
		
		replyDetailsTable = replyDetailsTableInst.get();
		replyDetailsTable.init("", false, false);
		replyDetailsTable.setCaption("Reply Details");
		if(pccDetailsTableDTO.getReplyDetails() !=null 
				&& !pccDetailsTableDTO.getReplyDetails().isEmpty()){
			replyDetailsTable.setTableList(pccDetailsTableDTO.getReplyDetails());
		}
		
		this.pccKey = pccDetailsTableDTO.getPccKey();
		bean=new PccDTO();
		bean.setPccKey(pccKey);
		fileUploadUI.init(new PCCUploadDocumentsDTO(),bean,SHAConstants.PCC_ZONAL_MEDICAL_HEAD_SCREEN);
		fileUploadUI.setPccDTO(bean);
		//fileUploadUI.setPccDetailsTableDTO(pccDetailsTableDTO);
		fileUploadUI.setPcckey(pccKey);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		
		uploadedDocumentsTable.init(" ",false,false);
		getUploadedTableValues(pccKey);
		

		VerticalLayout uploadDocLayout = new VerticalLayout(fileUploadUI);
		uploadDocLayout.setSpacing(true);
		uploadDocLayout.setMargin(true);
		uploadDocLayout.setWidth("100%");
		
		
		Label lblUploadDocuments = new Label("<H3>Uploaded Documents</H3>",ContentMode.HTML);
		
		VerticalLayout uploadedDocLayout = new VerticalLayout();
		uploadedDocLayout.setCaption("Uploaded Documents");
		uploadedDocLayout.setSpacing(true);
		uploadedDocLayout.setMargin(true);
		uploadedDocLayout.setWidth("60%");
		
		uploadedDocLayout.addComponent(uploadedDocumentsTable);

		uploadDocsPanel.setContent(uploadDocLayout);
		uploadedDocsPanel.setContent(uploadedDocLayout);
		
		
		btnResponse=new Button("Response");
		btnAssign=new Button("Assign");
		btnAssign.setEnabled(false);
			
		HorizontalLayout appBtnLayout=new HorizontalLayout(btnResponse/*,btnAssign*/);
		appBtnLayout.setSpacing(true);
		appBtnLayout.setMargin(false);
		
		dyanamicVLayout = new VerticalLayout();
		dyanamicVLayout.setVisible(false);
		
		//uploadDocsPanel.setWidth("70%");
		/*uploadDocMainLayout.addComponent(uploadDocsPanel);
		uploadDocMainLayout.addComponent(uploadedDocsPanel);*/
		mainVLayout = new VerticalLayout(processingDoctorDetailsTable,queryDetailsTable,replyDetailsTable,uploadDocsPanel,lblUploadDocuments,uploadedDocsPanel,dummytext,appBtnLayout,dyanamicVLayout);	
		mainVLayout.setComponentAlignment(appBtnLayout, Alignment.BOTTOM_LEFT);
		mainVLayout.setComponentAlignment(dyanamicVLayout, Alignment.BOTTOM_CENTER);

		addListener();
		setCompositionRoot(mainVLayout);

	}

	private Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	

	@SuppressWarnings("deprecation")
	public void addListener() {
		
	isNegotiation.addValueChangeListener(new Property.ValueChangeListener() {
		
		@Override
		public void valueChange(ValueChangeEvent event) {

			Boolean isChecked = false;
			Boolean isChangesneed = true;
			if(event.getProperty() != null && event.getProperty().getValue() != null) {

				if(event.getProperty().getValue().toString() == "true"){
					isChecked = true;
				}
				
			}
			if(event.getProperty() != null && event.getProperty().getValue() != null) {
				
				fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.ZONAL_MEDICAL_HEAD_GENERATE_NEGOTIATION_APPLICABLE, isChecked);
			}
		}
	});
	btnResponse.addClickListener(new Button.ClickListener() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			isResponse = true;
			isAssign = false;
			fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.ZONAL_MEDICAL_HEAD_GENERATE_RESPONSE_LAYOUT,null);
		}
	});
	
	
	btnAssign.addClickListener(new Button.ClickListener() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			isAssign = true;
			isResponse = false;
			fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.ZONAL_MEDICAL_HEAD_GENERATE_ASSIGN_LAYOUT,null);
		}
	});
	
	cmbSelectRole.addListener(new Listener(){
		@Override
		public void componentEvent(Event event) {
			if (cmbSelectRole.getValue() !=null) {
				SelectValue selectValue = (SelectValue)cmbSelectRole.getValue();
				Long pccKey = pccDetailsTableDTO.getPccKey();
				PCCRequest pccRequest =pccRequestService.getPCCRequestByKey(pccKey);
				Long cpuCode = 0L;
				if(pccRequest!=null){
					TmpCPUCode cpuCodeObject=  pccRequest.getIntimation().getCpuCode();
					if(cpuCodeObject !=null){
						 cpuCode= cpuCodeObject.getCpuCode();
						System.out.println(String.format("Cpu Code in process Page [%s]", cpuCode));
					}
				}
				if(selectValue.getCommonValue() !=null){
						fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.PCCZMH_GENERATE_USER_DETAILS,selectValue.getCommonValue(),cpuCode);
				}
			}
		}			
	});

}
	
	private BlurListener setNegotiationsaveListener(){

		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event){

				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtNegotiaitedAmmount !=null && txtNegotiaitedAmmount.getValue() !=null){
						Integer saveamunt = SHAUtils.getIntegerFromStringWithComma(value.getValue());
						Integer negoamt = SHAUtils.getIntegerFromStringWithComma(txtNegotiaitedAmmount.getValue());
						if(saveamunt > negoamt) {
							String eMsg = "Saved amount is greater than negotiated amount. Please enter a lesser amount in saved amount field";
							SHAUtils.showErrorMessageBoxWithCaption(eMsg, "Error");
							txtSavedAmount.setValue("");
						} 	
					}
				}
			}
		};
		return listener;
	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}

	public Boolean validatePage() {
		
		Boolean hasError=false;
		StringBuffer eMsg = new StringBuffer(); 
		
		if(isResponse){
			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			
			if(txtAssignNegotioanReamrks != null && txtAssignNegotioanReamrks.getValue() == null){
				hasError=true;
				eMsg.append("Enter Negotiation Remarks for Approve to proceed </br> ");
			}
			if(isNegotiation != null && isNegotiation.getValue() == null){
				hasError=true;
				eMsg.append("Select Negotiation to proceed. </br> ");
			}
			
			if(isNegotiation != null && isNegotiation.getValue() != null){
				
				if(isNegotiation.getValue().toString() == "true"){
					
					if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() == null){			
						hasError=true;
						eMsg.append("Enter negotiated amount to proceed. </br> ");
					}
					if(txtSavedAmount != null && txtSavedAmount.getValue() == null){			
						hasError=true;
						eMsg.append("Enter saved amount to proceed. </br> ");
					}
					
					if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
						hasError=true;
						eMsg.append("Select UserName to proceed. </br> ");
					}
				}
			}
		}
		if(isAssign){
			if(cmbSelectRole != null && cmbSelectRole.getValue() == null){			
				hasError=true;
				eMsg.append("Select Role to proceed. </br> ");
			}
			
			if(cmbSelectUserName != null && cmbSelectUserName.getValue() == null){			
				hasError=true;
				eMsg.append("Select UserName to proceed. </br> ");
			}
			
			if(txtAssignReamrks != null && txtAssignReamrks.getValue() == null){
				hasError=true;
				eMsg.append("Enter the Remarks to proceed further </br> ");
			}
		}
		
		if(hasError){
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
			hasError = true;
			return !hasError;
		}
		
		return !hasError;
	}
	
	public void addUserDetails(BeanItemContainer<SelectValue> userDetailsContainer) {

		unbindField(cmbSelectUserName);
		this.userDetailsContainer = userDetailsContainer;
		cmbSelectUserName.setContainerDataSource(this.userDetailsContainer);
		cmbSelectUserName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSelectUserName.setItemCaptionPropertyId("value");
		/*if(isResponse){
			cmbSelectUserName.setValue(pccDetailsTableDTO.getResponceUser());
			cmbSelectUserName.setEnabled(false);
		}*/
	}

	public PccDTO getvalues() {
		
		
			PccDTO pccDTO = binder.getItemDataSource().getBean();
			pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
			pccDTO.setIsAssign(isAssign);
			pccDTO.setIsResponse(isResponse);
			if(cmbSelectRole !=null && cmbSelectRole.getValue() !=null){
				pccDTO.setUserRoleAssigned((SelectValue)cmbSelectRole.getValue());
			}
			if(cmbSelectUserName !=null && cmbSelectUserName.getValue() !=null){
				pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
			}
			
			if(isResponse){
				
				if(txtAssignNegotioanReamrks != null && txtAssignNegotioanReamrks.getValue() != null){
					pccDTO.setRemarksNegotioanforZMH(txtAssignNegotioanReamrks.getValue());
				}
				if(isNegotiation != null && isNegotiation.getValue() != null){
					pccDTO.setIsNegotiation((Boolean)isNegotiation.getValue());
				}
				if(isNegotiation != null && isNegotiation.getValue() != null){
					
					if(isNegotiation.getValue().toString() == "true"){
						
						if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() != null){	
							String negovalue = txtNegotiaitedAmmount.getValue().replace(",", "");
							pccDTO.setNegotiatioAmount(Long.parseLong(negovalue));
						}
						if(txtSavedAmount != null && txtSavedAmount.getValue() != null){			
							String negovalue = txtSavedAmount.getValue().replace(",", "");
							pccDTO.setSavedAmount(Long.parseLong(negovalue));
						}
					}

				}
			}
			if(isAssign){
				if(txtAssignReamrks != null && txtAssignReamrks.getValue() != null){
					pccDTO.setRemarksAssignforZMH(txtAssignReamrks.getValue());
				}
			}
			
			List<PCCUploadDocumentsDTO> listUploadedDocuments = uploadedDocumentsTable.getValues();
				if(listUploadedDocuments != null && ! listUploadedDocuments.isEmpty()){
					pccDTO.setUploadedFileTableDto(listUploadedDocuments);
					pccDTO.setUploadedNDeletedFileListDto(uploadedDocumentsTable.getdeletedList());
					//pccDTO.setFileUploadRemarks;
					System.out.println(String.format("Get values Uploaded Remarks in page ***********[%S]",pccDTO.getFileUploadRemarks()));
					System.out.println(String.format("Get values setUploadTableValues in page ***********[%S]",pccDTO.getUploadedFileTableDto().size()));
				}
			return pccDTO;
	}

	private void addUserRole(BeanItemContainer<SelectValue> userRoles,SelectValue selectedrole,Boolean isEnabel) {

		unbindField(cmbSelectRole);
		cmbSelectRole.setContainerDataSource(userRoles);
		cmbSelectRole.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSelectRole.setItemCaptionPropertyId("value");
		cmbSelectRole.setEnabled(isEnabel);
		
		if(selectedrole !=null){
			cmbSelectRole.setValue(selectedrole);
		}
		
	}

	public void generateResponseLayout() {

		unbindandRemoveComponents();
		
		showOrHideValidation(false);
		negotiationLayout=new FormLayout(isNegotiation);
		HorizontalLayout hLyout = new HorizontalLayout(negotiationLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);		
	}

	public void generateAssignLayout() {

		unbindandRemoveComponents();
		
		mandatoryFields.add(txtAssignReamrks);
		mandatoryFields.add(cmbSelectUserName);
		
		showOrHideValidation(false);
		
		dummyForm.setWidth("500px");
		setRolesByActions("ZMH_ASIGN");
		
		assignLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtAssignReamrks);
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,assignLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);
		
	}

	public void generateNegotiation(Boolean isChecked) {
		
		unbindandRemoveComponents();
		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtAssignNegotioanReamrks);
		mandatoryFields.add(txtNegotiaitedAmmount);
		mandatoryFields.add(txtSavedAmount);
		showOrHideValidation(false);
		
		System.out.println(String.format("Is checked value: [%s]", isChecked));
		if(isChecked){
			setRolesByActions("ZMH_NEGOTATION_YES");
			responseLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtNegotiaitedAmmount,txtSavedAmount,txtAssignNegotioanReamrks);
		}else{
			setRolesByActions("ZMH_NEGOTATION_NO");
			responseLayout=new FormLayout(cmbSelectRole,txtAssignNegotioanReamrks);
			
		}
		
		dummyForm.setWidth("186px");
		negotiationLayout=new FormLayout(isNegotiation);
		
		HorizontalLayout hLyout = new HorizontalLayout(negotiationLayout,dummyForm,responseLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	
		
	}
	
	private void unbindandRemoveComponents(){

		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(assignLayout !=null 
				&& assignLayout.getComponentCount() >0){
			assignLayout.removeAllComponents();
			this.assignLayout = null;
		}	
		if(responseLayout !=null 
				&& responseLayout.getComponentCount() >0){
			responseLayout.removeAllComponents();
			this.responseLayout = null;
		}
		if(negotiationLayout !=null 
				&& negotiationLayout.getComponentCount() >0){
			negotiationLayout.removeAllComponents();
			this.negotiationLayout = null;
		}
	}
	
	
	
	public void editPCCUploadDocumentDetails(PCCUploadDocumentsDTO dto)
	{
		if(null != this.fileUploadUI){
			fileUploadUI.init(dto,bean,SHAConstants.PCC_ZONAL_MEDICAL_HEAD_SCREEN);
		}	
	}
	
	public void deletePCCUploadedDocumentDetails(PCCUploadDocumentsDTO dto) {
		List<PCCUploadDocumentsDTO>	deletedList =  uploadedDocumentsTable.getdeletedList();
			if(deletedList != null){
				for(PCCUploadDocumentsDTO oneDto :deletedList) {
					System.out.println(String.format("oneDto Key : [%s]", oneDto.getKey()));
					System.out.println(String.format("dto Key : [%s]", dto.getKey()));
					//if(oneDto.getKey()== dto.getKey()){
						
						oneDto.setDeletedFlag("Y");
					//}
				}
			}
			bean.setUploadedNDeletedFileListDto(deletedList);
	}

	

	private void setRolesByActions(String action){

		  ArrayList<String> roles;
		BeanItemContainer<SelectValue> userRoles = null;	

		 if(action.equals("ZMH_NEGOTATION_YES")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.PCC_COORDINATOR_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.PCC_COORDINATOR_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("ZMH_NEGOTATION_NO")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.PCC_PROCESSOR_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.PCC_PROCESSOR_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("ZMH_ASIGN")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.ZONAL_COORDINATOR_ROLE);
			  roles.add(SHAConstants.HRM_COORDINATOR_ROLE);
			  roles = pccRequestService.getZMHNegotiationassignedRoles(pccDetailsTableDTO.getPccKey(),roles);
			  if(!roles.isEmpty()){
				  userRoles = pccRequestService.getPCCRoles(roles);
				  addUserRole(userRoles,null,true);
			  }else{
				  userRoles = new BeanItemContainer<SelectValue>(SelectValue.class); 
				  addUserRole(userRoles,null,false);
				  SHAUtils.showMessageBox("Allready assigned to all roles and Negotiation Unsucessful", "PCC - INFORMATION");
			  }		 
		  }
	  }
	
	public void getUploadedTableValues(Long pccKey){
		uploadedDocumentsTable.removeRow();
		uploadedDocumentsTable.setTableList(zonalMedicalHeadRequestService.getUploadDocumentList(pccKey));
		
	}

	public void setUploadTableValues(List<PCCUploadDocumentsDTO> uploadDocDtoList){
		List<PCCUploadDocumentsDTO> existList = new ArrayList<PCCUploadDocumentsDTO>(); 
		
		if(uploadedDocumentsTable.getValues() != null){
			existList.addAll(uploadedDocumentsTable.getValues());
		}
		if(uploadDocDtoList != null && !uploadDocDtoList.isEmpty()){
			existList.addAll(uploadDocDtoList);
		}
		
		uploadedDocumentsTable.setTableList(existList);
		bean.setUploadedFileTableDto(existList);
		
	}
	
	public List<PCCUploadDocumentsDTO> getUploadedTableValues(){
		List<PCCUploadDocumentsDTO> existList = new ArrayList<PCCUploadDocumentsDTO>();
		if(uploadedDocumentsTable.getValues() != null){
			existList.addAll(uploadedDocumentsTable.getValues());
		}
		return existList;
	}



}
