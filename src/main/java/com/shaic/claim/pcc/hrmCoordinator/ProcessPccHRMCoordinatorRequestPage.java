package com.shaic.claim.pcc.hrmCoordinator;

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
import com.shaic.claim.pcc.SearchProcessPCCRequestService;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.views.ProcessingDoctorDetailsTable;
import com.shaic.claim.pcc.views.ZonalMedicalDetailsTable;
import com.shaic.claim.pcc.zonalCoordinator.ProcessPCCZonalCoordinatorRequestPresenter;
import com.shaic.claim.pcc.zonalMedicalHead.ProcessPCCUploadDocumentsGridForm;
import com.shaic.claim.pcc.zonalMedicalHead.ProcessPCCUploadDocumentsPageTable;
import com.shaic.domain.TmpCPUCode;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class ProcessPccHRMCoordinatorRequestPage extends ViewComponent{


	@Inject
	private Instance<ProcessingDoctorDetailsTable> processingDoctorDetailsTableInst;
	private ProcessingDoctorDetailsTable processingDoctorDetailsTable;
	
	@Inject
	private Instance<ZonalMedicalDetailsTable> zonalMedicalDetailsTableInst;
	private ZonalMedicalDetailsTable zonalMedicalDetailsTable;
	
	@Inject
	private ProcessPCCUploadDocumentsPageTableHRM uploadedDocumentsTable;
	
	@Inject
	private ProcessPCCHRMUploadDocumentsGridForm fileUploadUI;
	
	private Panel uploadDocsPanel;
	
	private Panel uploadedDocsPanel;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	private Button btnUpload;
	
	private List<PCCUploadDocumentsDTO> uploadedTblList = new ArrayList<PCCUploadDocumentsDTO>();
	
	
	private Long pccKey;
	
	//End
	
	private PccDTO bean;

	private BeanFieldGroup<PccDTO> binder;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private PccDetailsTableDTO pccDetailsTableDTO;
	
	private String presenterString;
	
	private ComboBox cmbSelectRole;
		
	private TextArea txtAssignNegotioanReamrks;

	private ComboBox cmbSelectUserName;
	
	private FormLayout negotiationLayout;
	
	private FormLayout responceLayout;

	private OptionGroup negotiation;
	
	private TextArea txtRmksforNegotiationZonal;
		
	private FormLayout dummyForm;
	
	private TextField dummytext;
	
	private VerticalLayout mainVLayout;
	
	private VerticalLayout dyanamicVLayout;
	
	private TextField txtNegotiaitedAmmount;
	
	private TextField txtSavedAmount;
	
	private BeanItemContainer<SelectValue> userDetailsContainer;
	
	
	@EJB
	private SearchProcessPCCRequestService pccRequestService;

	@PostConstruct
	protected void initView() {

	}

	public void init(PccDetailsTableDTO pccDetailsTableDTO, String presenterString) {
		
		this.presenterString=presenterString;
		this.pccDetailsTableDTO = pccDetailsTableDTO;
		
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
		
		processingDoctorDetailsTable = processingDoctorDetailsTableInst.get();
		processingDoctorDetailsTable.init("", false, false);
		processingDoctorDetailsTable.setCaption("Processing Doctor Details");
		if(pccDetailsTableDTO !=null){
			List<PccDetailsTableDTO> pccDetailsTableDTOs = new ArrayList<PccDetailsTableDTO>();
			pccDetailsTableDTOs.add(pccDetailsTableDTO);
			processingDoctorDetailsTable.setTableList(pccDetailsTableDTOs);
		}
		
		zonalMedicalDetailsTable = zonalMedicalDetailsTableInst.get();
		zonalMedicalDetailsTable.init("", false, false);
		zonalMedicalDetailsTable.setCaption("Zonal Medical Details");
		if(pccDetailsTableDTO !=null){
			zonalMedicalDetailsTable.setTableList(pccDetailsTableDTO.getZonalDetails());
		}
		
		cmbSelectRole = binder.buildAndBind("Select Role","userRoleAssigned",ComboBox.class);	
		cmbSelectUserName = binder.buildAndBind("Select User Name","userNameAssigned",ComboBox.class);
		
		negotiation = (OptionGroup) binder.buildAndBind("Negotiation sucess", "isNegotiation", OptionGroup.class);
		negotiation.addItems(getReadioButtonOptions());
		negotiation.setItemCaption(true, "Yes");
		negotiation.setItemCaption(false, "No");
		negotiation.setStyleName("horizontal");
			
		txtAssignNegotioanReamrks = binder.buildAndBind("Remarks", "remarksNegotioanforZMH", TextArea.class);
		txtAssignNegotioanReamrks.setMaxLength(1000);
		txtAssignNegotioanReamrks.setWidth("278px");
		txtAssignNegotioanReamrks.setHeight("130px");
		txtAssignNegotioanReamrks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtAssignNegotioanReamrks,null,getUI(),SHAConstants.STP_REMARKS);
		
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
		
		this.pccKey = pccDetailsTableDTO.getPccKey();
		bean=new PccDTO();
		bean.setPccKey(pccKey);
		fileUploadUI.init(new PCCUploadDocumentsDTO(),bean,SHAConstants.PCC_HRM_COORDINATOR_SCREEN);
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
			
        FormLayout appBtnLayout=new FormLayout(negotiation);
        
        dyanamicVLayout = new VerticalLayout();
		dyanamicVLayout.setVisible(false);
		
		mainVLayout = new VerticalLayout(processingDoctorDetailsTable,zonalMedicalDetailsTable,uploadDocsPanel,lblUploadDocuments,uploadedDocsPanel,dummytext,appBtnLayout,dyanamicVLayout);
		mainVLayout.setComponentAlignment(appBtnLayout, Alignment.BOTTOM_LEFT);
		mainVLayout.setComponentAlignment(dyanamicVLayout, Alignment.BOTTOM_CENTER);

		addListener();
		setCompositionRoot(mainVLayout);

	}

	private Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>();
		coordinatorValues.add(true);
		coordinatorValues.add(false);	
		return coordinatorValues;
	}
	

	@SuppressWarnings("deprecation")
	public void addListener() {	
		
		negotiation.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				if(event.getProperty() != null && event.getProperty().getValue() != null) {
					
					if(event.getProperty().getValue().toString() == "true"){
						isChecked = true;
					}

				}
				fireViewEvent(ProcessPCCHrmCoordinatorRequestPresenter.HRM_COORDINATOR_GENERATE_NEGOTIATION_APPLICABLE, isChecked);				
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
							fireViewEvent(ProcessPCCHrmCoordinatorRequestPresenter.PCCCHRM_GENERATE_USER_DETAILS,selectValue.getCommonValue(),cpuCode);
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
	

	public void generateNegotiation(Boolean isChecked) {

		if (dyanamicVLayout != null
				&& dyanamicVLayout.getComponentCount() > 0) {
			dyanamicVLayout.removeAllComponents();
		}
		if(responceLayout !=null 
				&& responceLayout.getComponentCount() >0){
			responceLayout.removeAllComponents();
			this.responceLayout = null;
		}
		
		unbindField(cmbSelectRole);
		unbindField(txtAssignNegotioanReamrks);
		unbindField(txtNegotiaitedAmmount);
		unbindField(txtSavedAmount);
		
		mandatoryFields.add(cmbSelectRole);
		mandatoryFields.add(cmbSelectUserName);
		mandatoryFields.add(txtAssignNegotioanReamrks);
		mandatoryFields.add(txtNegotiaitedAmmount);
		mandatoryFields.add(txtSavedAmount);
		showOrHideValidation(false);
		
		if(isChecked){
			setRolesByActions("ZC_NEGOTATION_YES");
			responceLayout=new FormLayout(cmbSelectRole,cmbSelectUserName,txtNegotiaitedAmmount,txtSavedAmount,txtAssignNegotioanReamrks);
		}else{
			setRolesByActions("ZC_NEGOTATION_NO");
			responceLayout=new FormLayout(cmbSelectRole,txtAssignNegotioanReamrks);
		}
		
		HorizontalLayout hLyout = new HorizontalLayout(dummyForm,responceLayout);
		dyanamicVLayout.addComponent(hLyout);
		dyanamicVLayout.setVisible(true);	

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
		
		if(negotiation != null && negotiation.getValue() == null){
			hasError=true;
			eMsg.append("Select Negotiation option to proceed further </br> ");
		}
		
		if(negotiation != null && negotiation.getValue() != null){

			if(txtAssignNegotioanReamrks != null && txtAssignNegotioanReamrks.getValue() == null){
				hasError=true;
				eMsg.append("Enter the remarks to proceed further </br> ");
			}
		}

		if(negotiation != null && negotiation.getValue() != null){
			
			if( negotiation.getValue().toString() == "true"){
				
				if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() == null){			
					hasError=true;
					eMsg.append("Enter negotiated amount to proceed. </br> ");
				}
				if(txtSavedAmount != null && txtSavedAmount.getValue() == null){			
					hasError=true;
					eMsg.append("Enter saved amount to proceed. </br> ");
				}

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

	@SuppressWarnings("deprecation")
	public PccDTO getvalues() {

		PccDTO pccDTO = binder.getItemDataSource().getBean();
		pccDTO.setPccKey(pccDetailsTableDTO.getPccKey());
		if(cmbSelectRole.getValue() !=null){
			pccDTO.setUserRoleAssigned((SelectValue)cmbSelectRole.getValue());
		}
		if(negotiation != null && negotiation.getValue() != null){
			pccDTO.setIsNegotiation((Boolean)negotiation.getValue());
		}
		if(negotiation != null && negotiation.getValue() != null){
			
			if(negotiation.getValue().toString() == "true"){
				
				if(txtNegotiaitedAmmount != null && txtNegotiaitedAmmount.getValue() != null){	
					String negovalue = txtNegotiaitedAmmount.getValue().replace(",", "");
					pccDTO.setNegotiatioAmount(Long.parseLong(negovalue));
				}
				if(txtSavedAmount != null && txtSavedAmount.getValue() != null){			
					String negovalue = txtSavedAmount.getValue().replace(",", "");
					pccDTO.setSavedAmount(Long.parseLong(negovalue));
				}
				
				if(cmbSelectUserName !=null && cmbSelectUserName.getValue() !=null){
					pccDTO.setUserNameAssigned((SelectValue)cmbSelectUserName.getValue());
				}
			}

		}

		if(txtAssignNegotioanReamrks != null && txtAssignNegotioanReamrks.getValue() != null){
			pccDTO.setRemarksNegotioanforZMH(txtAssignNegotioanReamrks.getValue());
		}

		List<PCCUploadDocumentsDTO> listUploadedDocuments = uploadedDocumentsTable.getValues();
		if(listUploadedDocuments != null && ! listUploadedDocuments.isEmpty()){
			pccDTO.setUploadedFileTableDto(listUploadedDocuments);
			pccDTO.setUploadedNDeletedFileListDto(uploadedDocumentsTable.getdeletedList());
		}
		
		return pccDTO;

	}

	private void setRolesByActions(String action){

		  ArrayList<String> roles;
		BeanItemContainer<SelectValue> userRoles = null;	

		 if(action.equals("ZC_NEGOTATION_YES")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.PCC_COORDINATOR_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.PCC_COORDINATOR_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }else if(action.equals("ZC_NEGOTATION_NO")){
			  roles = new ArrayList<String>();
			  roles.add(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
			  userRoles = pccRequestService.getPCCRoles(roles);
			  SelectValue selectedrole = pccRequestService.getMasRoleSelectValue(SHAConstants.ZONAL_MEDICAL_HEAD_ROLE);
			  addUserRole(userRoles,selectedrole,false);
		  }
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
	
	public void editPCCUploadDocumentDetails(PCCUploadDocumentsDTO dto)
	{
		if(null != this.fileUploadUI){
			fileUploadUI.init(dto,bean,SHAConstants.PCC_HRM_COORDINATOR_SCREEN);
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

	public void getUploadedTableValues(Long pccKey){
		uploadedDocumentsTable.removeRow();
		uploadedDocumentsTable.setTableList(pccRequestService.getUploadDocumentList(pccKey));
		
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

}
