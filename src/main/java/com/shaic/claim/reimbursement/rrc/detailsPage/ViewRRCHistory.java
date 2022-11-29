package com.shaic.claim.reimbursement.rrc.detailsPage;

/**
 * @author ntv.vijayar
 *
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.tables.ExtraEffortEmployeeListenerTable;
import com.shaic.claim.rod.wizard.tables.QuantumReductionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.RRCRequestCategoryListenerTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;

import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author ntv.vijayar
 *
 */
public class ViewRRCHistory extends ViewComponent {
	
	private Panel employeeDetailsPanel;
	
	private Panel policyDetailsPanel;
	
	private Panel hospitalDetailsPanel;
	
	private Panel insuredDetailsPanel;
	
	private Panel quantumReductionDetailsPanel;
	
	private Panel rrcViewPanel;
	//private VerticalSplitPanel rrcViewPanel;
	
	
	private TextField txtEmployeeName;
	
	private	TextField txtEmployeeId;
	
	private TextField txtEmployeeZone;
	
	private TextField txtEmployeeDept;
	
	private TextField txtPolicyNo;
	
	private TextField txtIntimationNo;
	
	private TextField txtProductName;
	
	private TextField txtDuration;
	
	private TextField txtSumInsured;
	
	private TextField txtHospitalName;
	
	private TextField txtHospitalCity;
	
	private TextField txtHospitalZone;
	
	private TextField txtDateOfAdmission;
	
	private TextField txtDateOfDischarge;
	
	private TextField txtInsuredName;
	
	private TextField txtInsuredAge;
	
	private TextField txtSex;
	
	private TextField txtClaimType;
	
	private TextField txtProcessingStage;
	
	@Inject
	private Instance<QuantumReductionDetailsListenerTable> quantumReductionListenerTableObj;
	
	private QuantumReductionDetailsListenerTable quantumReductionListenerTable;
	
	private ComboBox cmbSignificantClinicalInformation;
	
	//private TextField txtRemarks;
	private TextArea txtRemarks;
	
	@Inject
	private Instance<ExtraEffortEmployeeListenerTable> extraEffortEmployeeListenerTableObj;
	
	private ExtraEffortEmployeeListenerTable extraEffortEmployeeListenerTable;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private Button btnProcessRRCRequest;
	
	private Button btnPlaceOnHold;
	
	private String presenterString;
	
	private RRCDTO bean;
	
	private Window popup;
	
	private BeanFieldGroup<RRCDTO> binder;
	
//	private VerticalLayout processRRCRequestLayout;
	
//	private HorizontalLayout placeOnHoldLayout;
	
	private ComboBox cmbEligiblity;
	private TextField txtSavedAmount;
	
	private TextArea txtEligibilityRemarks;
	
	private TextArea txtRequestOnHoldRemarks;
	
	private TextField txtProcessEligibility;
	
	private TextField txtProcessSavedAmount;
	
	private TextField txtFollowUp;
	
	private TextField txtProcessRRCRemarks;
	
	private TextField rrcRequestProcessedDate;
	
	private TextField txtRRCRequestNo;
	
	private TextField txtRequestIntiatedDate;
		
	@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcRequestCategoryListenerTableObj;
	
	private RRCRequestCategoryListenerTable rrcRequestCategoryListenerTable;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String,Object> containerMap;
	
	private BeanItemContainer<SelectValue> eligiblity;
	
	private BeanItemContainer<SelectValue> significantClinicalInformation;
	 
	private BeanItemContainer<SelectValue> significantContainer;
	

	
	
	@PostConstruct
	public void init() {
		
		

	}
	
	public void init(RRCDTO bean, Window popup) {
		this.bean = bean;
		this.popup = popup;
	
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<RRCDTO>(RRCDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public Component getContent() {
		
		initBinder();
		this.containerMap = bean.getDataSourcesMap();
		//this.popup = popup;
		
		
		
		HorizontalLayout detailsLayout = new HorizontalLayout(buildEmployeeDetailsLayout() , buildPolicyDetailsLayout() ,buildHospitalDetailsLayout(), buildInsuredDetailsLayout());
		detailsLayout.setSpacing(true);
		detailsLayout.setMargin(true);
		detailsLayout.setEnabled(Boolean.FALSE);
		Panel detailsPanel = new Panel();
		detailsPanel.setContent(detailsLayout);
			HorizontalLayout hLayout = buildRequestFields();
		VerticalLayout verticalRRCLayout = new VerticalLayout(hLayout , detailsPanel , buildReductionDetailsPanel(), buildRRCReviewDetails(),buildButtonLayout() );
		//verticalRRCLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);
	
		verticalRRCLayout.setSpacing(true);
		verticalRRCLayout.setMargin(true);
		rrcViewPanel = new Panel();
		
		rrcViewPanel.setSizeFull();
		rrcViewPanel.setContent(verticalRRCLayout);
		
		//loadContainerDataSources();
		setTableValues();
		addListener();
		
		setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}
	
	private HorizontalLayout buildRequestFields()
	{
		txtRRCRequestNo = (TextField)binder.buildAndBind("RRC Request No","rrcRequestNo",TextField.class);
		txtRequestIntiatedDate = (TextField)binder.buildAndBind("Request Initiated Date","rrcintiatedDate",TextField.class);
		FormLayout fLayout = new FormLayout(txtRRCRequestNo);
		FormLayout fLayout1 = new FormLayout(txtRequestIntiatedDate);
		
		HorizontalLayout hLayout = new HorizontalLayout(fLayout,fLayout1);
		
		
		return hLayout;
	}
	
	private Panel buildReductionDetailsPanel()
	{
		txtClaimType = (TextField)binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtProcessingStage = (TextField)binder.buildAndBind("Processing Stage","processingStage",TextField.class);
		txtClaimType.setReadOnly(true);
		txtProcessingStage.setReadOnly(true);
/*		cmbSignificantClinicalInformation = (ComboBox)binder.buildAndBind("Significant Clinincal Information","significantClinicalInformation",ComboBox.class);*/
		txtRemarks = (TextArea)binder.buildAndBind("Remark","remarks",TextArea.class);
//		txtRemarks.setRequired(true);
		 txtRemarks.setReadOnly(true);
		 txtRemarks.setWidth("60%");

		
		FormLayout reductionDetails = new FormLayout(txtClaimType , txtProcessingStage);
		reductionDetails.setWidth("-1px");
		reductionDetails.setSpacing(true);
		
		addStyleForTextfield(reductionDetails);
		
/*		FormLayout clinicalInfo = new FormLayout(cmbSignificantClinicalInformation);*/
		FormLayout remarks = new FormLayout(txtRemarks);
		

		
		quantumReductionListenerTable = quantumReductionListenerTableObj.get();
		quantumReductionListenerTable.init(bean);

		
		HorizontalLayout infoAndRemarksLayout = new HorizontalLayout(remarks);
		infoAndRemarksLayout.setSpacing(true);
		
//		VerticalLayout vReductionLayout = new VerticalLayout(reductionDetails , quantumReductionListenerTable , infoAndRemarksLayout );
		VerticalLayout vReductionLayout = new VerticalLayout(reductionDetails , quantumReductionListenerTable ,remarks );

		vReductionLayout.setWidth("100%");
		vReductionLayout.setComponentAlignment(reductionDetails, Alignment.TOP_RIGHT);
		vReductionLayout.setSpacing(true);
		vReductionLayout.setMargin(true);
		
		
		quantumReductionDetailsPanel = new Panel();
		quantumReductionDetailsPanel.setContent(vReductionLayout);
		//quantumReductionDetailsPanel.setStyleName("girdBorder");
		
		return quantumReductionDetailsPanel;
		
	}
	
	private Panel buildRRCReviewDetails()
	{
		String panelCaption ="";
		if(bean.getRrcType() !=null
				&& bean.getRrcType().equals("RRC REQUEST")){
			txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","reviewEligiblityValue",TextField.class);
			txtProcessEligibility.setReadOnly(true);
			panelCaption ="RRC Process Details";
		}else if(bean.getRrcType() !=null
				&& bean.getRrcType().equals("RRC REVIEW")){
			txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","reviewEligiblityValue",TextField.class);
			txtProcessEligibility.setReadOnly(true);
			panelCaption ="RRC Review Details";
		}else if(bean.getRrcType() !=null
				&& bean.getRrcType().equals("RRC MODIFY")){
			txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","reviewEligiblityValue",TextField.class);
			txtProcessEligibility.setReadOnly(true);
			panelCaption ="RRC Modify Details";
		}else{
			txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","reviewEligiblityValue",TextField.class);
			txtProcessEligibility.setReadOnly(true);
			panelCaption ="RRC Process Details";
		}
		if(this.bean.getReviewEligiblityValue() == null && this.bean.getEligibility() != null){
			if(bean.getRrcType().equals("RRC REQUEST")
					|| bean.getRrcType().equals("RRC INTIATED")){
				txtProcessEligibility.setReadOnly(false);
				txtProcessEligibility.setValue(this.bean.getEligibility().getValue());
				txtProcessEligibility.setReadOnly(true);
			}
		}
		
		rrcRequestProcessedDate = (TextField)binder.buildAndBind("Request Processed Date","rrcRequestProcessedDate",TextField.class);
		rrcRequestProcessedDate.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rrcRequestProcessedDate.setReadOnly(true);
		
		extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.setReferenceData(this.containerMap);
		extraEffortEmployeeListenerTable.init(false);
		extraEffortEmployeeListenerTable.setEnabled(false);
		
		VerticalLayout vReductionLayout = new VerticalLayout();
		vReductionLayout.setWidth("100%");
		
		
		FormLayout rrcProcessingForm = new FormLayout(txtProcessEligibility,rrcRequestProcessedDate);
		rrcProcessingForm.setMargin(true);
		rrcProcessingForm.setSpacing(true);
		
		FormLayout empform = new FormLayout(extraEffortEmployeeListenerTable);
		empform.setSpacing(true);
		empform.setMargin(true);
		
		vReductionLayout.addComponent(rrcProcessingForm);
		vReductionLayout.addComponent(empform);
		vReductionLayout.setSpacing(true);
		vReductionLayout.setMargin(true);
		
		
		//rrcProcessingForm.setCaption("RRC Processing Details");
		
		Panel rrcProcessingPanel = new Panel();
		rrcProcessingPanel.setCaption(panelCaption);
		rrcProcessingPanel.setContent(vReductionLayout);
		
		return rrcProcessingPanel;		
	}
	
	
	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("OK");
		btnSubmit = new Button();
		btnSubmit.setCaption("OK");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}
	
	
	private void setTableValues()
	{
		/*if(null != extraEffortEmployeeListenerTable)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				extraEffortEmployeeListenerTable.setTableList(extraEmployeeList);
			}
		}*/
		
		if(null != extraEffortEmployeeListenerTable)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				Long sNo = 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					BeanItemContainer<SelectValue> empNameContainer  = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.EMPLOYEE_NAME_LIST);
					 for(int i = 0 ; i<empNameContainer.size() ; i++)
					 	{
						 	/*if(null != extraEmployeeEffortDTO.getSelEmployeeName())
						 	{
								if ((extraEmployeeEffortDTO.getSelEmployeeName().getValue()).equalsIgnoreCase(empNameContainer.getIdByIndex(i).getValue()))
								{
									extraEmployeeEffortDTO.setSelEmployeeName(empNameContainer.getIdByIndex(i));
								}
						 	}*/
						 if(null != extraEmployeeEffortDTO.getSelEmployeeId())
						 	{
								if ((extraEmployeeEffortDTO.getSelEmployeeId().getId()).equals(empNameContainer.getIdByIndex(i).getId()))
								{
									extraEmployeeEffortDTO.setSelEmployeeName(empNameContainer.getIdByIndex(i));
								}
						 	}
						 
						}
					 //extraEffortEmployeeListenerTable.setTableList(extraEmployeeList);
					 extraEffortEmployeeListenerTable.addBeanToList(extraEmployeeEffortDTO);
					 sNo++;
				}
				
				
				
			}
		}
		
		if(null != quantumReductionListenerTable)
		{
			QuantumReductionDetailsDTO quantumReductionDetailsDTO = this.bean.getQuantumReductionDetailsDTO();
			if(null != quantumReductionDetailsDTO)
			{
				quantumReductionListenerTable.addBeanToList(quantumReductionDetailsDTO);
			}
			/*List<QuantumReductionDetailsDTO> quantumReductionDetailsList = this.bean.getQuantumReductionDetailsDTOList();
			if(null != quantumReductionDetailsList && !quantumReductionDetailsList.isEmpty())
			{
				quantumReductionListenerTable.setTableList(quantumReductionDetailsList);
			}*/
		}
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != extraEffortEmployeeListenerTable)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = extraEffortEmployeeListenerTable.getValues();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				Long sNo= 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				this.bean.setEmployeeEffortList(extraEmployeeList);
			}
		}
		
	}
	
	private void loadContainerDataSources()
	{
		 significantClinicalInformation = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.SIGNIFICANT_CLINICAL_INFORMATION);
		 //CR2019204 
		 /* cmbSignificantClinicalInformation.setContainerDataSource(significantClinicalInformation);
		 cmbSignificantClinicalInformation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbSignificantClinicalInformation.setItemCaptionPropertyId("value");*/
		 
		 for(int i = 0 ; i<significantClinicalInformation.size() ; i++)
		 	{
				if (this.bean.getSignificantClinicalInformationValue() != null && (this.bean.getSignificantClinicalInformationValue()).equalsIgnoreCase(significantClinicalInformation.getIdByIndex(i).getValue()))
				{
					this.cmbSignificantClinicalInformation.setValue(significantClinicalInformation.getIdByIndex(i));
				}
			}
		
	}
	
	
	
	private Panel  buildEmployeeDetailsLayout()
	{
		txtEmployeeName = (TextField)binder.buildAndBind("Employee Name","employeeName",TextField.class);
		txtEmployeeId = (TextField)binder.buildAndBind("Employee ID","employeeId",TextField.class);
		txtEmployeeZone = (TextField)binder.buildAndBind("Employee Zone","employeeZone",TextField.class);
		txtEmployeeDept = (TextField)binder.buildAndBind("Employee Dept","employeeDept",TextField.class);
		employeeDetailsPanel = new Panel();
		FormLayout empDetails = new FormLayout(txtEmployeeName,txtEmployeeId,txtEmployeeZone,txtEmployeeDept);
		addStyleForTextfield(empDetails);
		empDetails.setSpacing(true);
		empDetails.setMargin(true);
		employeeDetailsPanel.setContent(empDetails);		
		return employeeDetailsPanel;
	}
	
	private void addStyleForTextfield(FormLayout layout)
	{
		int iLayoutSize = layout.getComponentCount();
		for (int i = 0; i < iLayoutSize; i++) {
			TextField txtFld = (TextField) layout.getComponent(i);
			txtFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			txtFld.setNullRepresentation("");
		}
	}
	
	private Panel buildPolicyDetailsLayout()
	{
		txtPolicyNo = (TextField)binder.buildAndBind("Policy No","policyNo",TextField.class);
		txtIntimationNo = (TextField)binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtProductName = (TextField)binder.buildAndBind("Product Name","productName",TextField.class);
		txtDuration = (TextField)binder.buildAndBind("Duration","duration",TextField.class);
		txtSumInsured = (TextField)binder.buildAndBind("Sum Insured","sumInsured",TextField.class);
		
		policyDetailsPanel = new Panel();
		FormLayout policyDetails = new FormLayout(txtPolicyNo, txtIntimationNo , txtProductName , /*txtDuration,*/ txtSumInsured);
		
		addStyleForTextfield(policyDetails);
		
		policyDetails.setSpacing(true);
		policyDetails.setMargin(true);
		policyDetailsPanel.setContent(policyDetails);
		//policyDetailsPanel.setStyleName("girdBorder");
		
		return policyDetailsPanel;
	}
	
	private Panel buildHospitalDetailsLayout()
	{
		txtHospitalName = (TextField)binder.buildAndBind("Hospital Name","hospitalName",TextField.class);
		txtHospitalCity = (TextField)binder.buildAndBind("Hospital City","hospitalCity",TextField.class);
		txtHospitalZone = (TextField)binder.buildAndBind("Hospital Zone","hospitalZone",TextField.class);
		txtDateOfAdmission = (TextField)binder.buildAndBind("Date Of Admission","dateOfAdmission",TextField.class);
		txtDateOfDischarge = (TextField)binder.buildAndBind("Date Of Discharge","dateOfDischarge",TextField.class);
		
		hospitalDetailsPanel = new Panel();
		FormLayout hospitalDetails = new FormLayout(txtHospitalName, txtHospitalCity , txtHospitalZone , txtDateOfAdmission, txtDateOfDischarge);
		addStyleForTextfield(hospitalDetails);
		
		hospitalDetails.setSpacing(true);
		hospitalDetails.setMargin(true);
		hospitalDetailsPanel.setContent(hospitalDetails);
		//hospitalDetailsPanel.setStyleName("girdBorder");
		return hospitalDetailsPanel;
	}
	
	private Panel buildInsuredDetailsLayout()
	{
		txtInsuredName = (TextField)binder.buildAndBind("Insured Name","insuredName",TextField.class);
		txtInsuredAge = (TextField)binder.buildAndBind("Age","insuredAge",TextField.class);
		txtSex = (TextField)binder.buildAndBind("Sex","sex",TextField.class);
		
		insuredDetailsPanel = new Panel();
		FormLayout insuredDetails = new FormLayout(txtInsuredName, txtInsuredAge , txtSex);
		
		addStyleForTextfield(insuredDetails);
		
		insuredDetails.setSpacing(true);
		insuredDetails.setMargin(true);
		insuredDetailsPanel.setContent(insuredDetails);
		//insuredDetailsPanel.setStyleName("girdBorder");
		return insuredDetailsPanel;
	}

	public void loadRRCRequestDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {

		//reconsiderationRequest = (BeanItemContainer<SelectValue>) referenceDataMap.get("commonValues");
		 cmbSignificantClinicalInformation.setContainerDataSource(mastersValueContainer);
		 cmbSignificantClinicalInformation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbSignificantClinicalInformation.setItemCaptionPropertyId("value");
	}
	
	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		extraEffortEmployeeListenerTable.loadEmployeeMasterData(field , employeeDetailsList);		
	}
	
	public void addListener() {
		
		btnSubmit.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
					//	fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});

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
	



	
	

	
	
	

}
