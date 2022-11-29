package com.shaic.claim.reimbursement.rrc.detailsPage;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.rod.wizard.tables.ExtraEffortEmployeeListenerTable;
import com.shaic.claim.rod.wizard.tables.QuantumReductionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.RRCRequestCategoryListenerTable;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.zybnet.autocomplete.server.AutocompleteField;

/**
 * @author ntv.vijayar
 *
 */
public class ProcessRRCRequestDataExtractionPage extends ViewComponent {
	
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
	
	private VerticalLayout processRRCRequestLayout;
	
	private HorizontalLayout placeOnHoldLayout;
	
	private ComboBox cmbEligiblity;
	private TextField txtSavedAmount;
	
	private TextArea txtEligibilityRemarks;
	
	private TextArea txtRequestOnHoldRemarks;
	
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
	
	public void init(RRCDTO bean) {
		this.bean = bean;
	
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
		detailsPanel.setSizeFull();
		
		processRRCRequestLayout = new VerticalLayout();
		placeOnHoldLayout = new HorizontalLayout();
		
		VerticalLayout verticalRRCLayout = new VerticalLayout(detailsPanel , buildReductionDetailsPanel(), buildRequestTypeButton(), processRRCRequestLayout , placeOnHoldLayout /*,buildButtonLayout()*/);
	
		verticalRRCLayout.setSpacing(true);
		verticalRRCLayout.setMargin(true);
		//verticalRRCLayout.setEnabled(Boolean.FALSE);
		//verticalRRCLayout.setComponentAlignment(buildButtonLayout(), Alignment.MIDDLE_CENTER);
		
		//rrcViewPanel = new VerticalSplitPanel();
		rrcViewPanel = new Panel();
		
	
		rrcViewPanel.setSizeFull();
		rrcViewPanel.setContent(verticalRRCLayout);
		//rrcViewPanel.setFirstComponent(commonButtonsLayout());
		//rrcViewPanel.setSecondComponent(verticalRRCLayout);
		
		
		//loadContainerDataSources();
		setTableValues();
		addListener();
		showOrHideValidation(false);
		//setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}
	
	/*private VerticalLayout commonButtonsLayout()
	{
		TextField rrcRequestNo = new TextField("RRC Request No");
		
		rrcRequestNo.setValue(String.valueOf(this.bean.getRrcDTO().getRrcRequestType()));
		//Vaadin8-setImmediate() rrcRequestNo.setImmediate(true);
		rrcRequestNo.setWidth("250px");
		rrcRequestNo.setHeight("20px");
		rrcRequestNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rrcRequestNo.setReadOnly(true);
		rrcRequestNo.setEnabled(false);
		rrcRequestNo.setNullRepresentation("");
		
		TextField rrcRequestType = new TextField("RRC Request No");
		
		rrcRequestType.setValue(String.valueOf(this.bean.getRrcDTO().getRrcRequestType()));
		//Vaadin8-setImmediate() rrcRequestType.setImmediate(true);
		rrcRequestType.setWidth("250px");
		rrcRequestType.setHeight("20px");
		rrcRequestType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rrcRequestType.setReadOnly(true);
		rrcRequestType.setEnabled(false);
		rrcRequestType.setNullRepresentation("");
		
		FormLayout hLayout = new FormLayout (rrcRequestNo , rrcRequestType);
		VerticalLayout vLayout = new VerticalLayout(hLayout);
		vLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);
		return vLayout;
	}*/
	
	
	
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
				Long sNo =1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				this.bean.setEmployeeEffortList(extraEmployeeList);
			}
		}
		if(null != rrcRequestCategoryListenerTable)
		{
			List<ExtraEmployeeEffortDTO> rrcRequestCategoryList = rrcRequestCategoryListenerTable.getValues();
			if(null != rrcRequestCategoryList && !rrcRequestCategoryList.isEmpty())
			{
				Long sNo =1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcRequestCategoryList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				this.bean.setRrcCategoryList(rrcRequestCategoryList);
			}
		}
	}
	
	private void loadContainerDataSources()
	{
		/* eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbEligiblity.setContainerDataSource(eligiblity);
		 cmbEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbEligiblity.setItemCaptionPropertyId("value");*/
		 
		 significantClinicalInformation = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.SIGNIFICANT_CLINICAL_INFORMATION);
		 cmbSignificantClinicalInformation.setContainerDataSource(significantClinicalInformation);
		 cmbSignificantClinicalInformation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbSignificantClinicalInformation.setItemCaptionPropertyId("value");
		 
		 for(int i = 0 ; i<significantClinicalInformation.size() ; i++)
		 	{
			 if(null != this.bean.getSignificantClinicalInformationValue())
			 {
				if ((this.bean.getSignificantClinicalInformationValue()).equalsIgnoreCase(significantClinicalInformation.getIdByIndex(i).getValue()))
				{
					this.cmbSignificantClinicalInformation.setValue(significantClinicalInformation.getIdByIndex(i));
				}
			 }
			}
		/*if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}*/
	}
	
	private Panel buildReductionDetailsPanel()
	{
		txtClaimType = (TextField)binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtProcessingStage = (TextField)binder.buildAndBind("Processing Stage","processingStage",TextField.class);
		txtClaimType.setReadOnly(true);
		txtProcessingStage.setReadOnly(true);
		/*cmbSignificantClinicalInformation = (ComboBox)binder.buildAndBind("Significant Clinincal Information","significantClinicalInformation",ComboBox.class);
		cmbSignificantClinicalInformation.setEnabled(false);*/
	//	cmbSignificantClinicalInformation.setReadOnly(true);
		txtRemarks = (TextArea)binder.buildAndBind("Remarks","remarks",TextArea.class);
//		txtRemarks.setRequired(true);
		txtRemarks.setMaxLength(200);
		txtRemarks.setReadOnly(true);
		
		FormLayout reductionDetails = new FormLayout(txtClaimType , txtProcessingStage);
		reductionDetails.setWidth("-1px");
		reductionDetails.setSpacing(true);
		
		addStyleForTextfield(reductionDetails);
		/*reductionDetails.setComponentAlignment(txtClaimType, Alignment.TOP_RIGHT);
		reductionDetails.setComponentAlignment(txtProcessingStage, Alignment.MIDDLE_RIGHT);*/
		
		/*FormLayout clinicalInfo = new FormLayout(cmbSignificantClinicalInformation);*/
		FormLayout remarks = new FormLayout(txtRemarks);
		
		//addStyleForTextfield(remarks);
		
		quantumReductionListenerTable = quantumReductionListenerTableObj.get();
		quantumReductionListenerTable.init(this.bean);
		quantumReductionListenerTable.setEnabled(false);
	/*	extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.init(true);*/
		//extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		
		HorizontalLayout infoAndRemarksLayout = new HorizontalLayout(remarks);
		infoAndRemarksLayout.setSpacing(true);
		
		VerticalLayout vReductionLayout = new VerticalLayout(reductionDetails , quantumReductionListenerTable , infoAndRemarksLayout );
		vReductionLayout.setWidth("100%");
		vReductionLayout.setComponentAlignment(reductionDetails, Alignment.TOP_RIGHT);
		vReductionLayout.setSpacing(true);
		vReductionLayout.setMargin(true);
		
		
		quantumReductionDetailsPanel = new Panel();
		quantumReductionDetailsPanel.setContent(vReductionLayout);
		//quantumReductionDetailsPanel.setStyleName("girdBorder");
		
		return quantumReductionDetailsPanel;
		
	}
	
	private Panel  buildEmployeeDetailsLayout()
	{
		txtEmployeeName = (TextField)binder.buildAndBind("Employee Name","employeeName",TextField.class);
		
		//txtEmployeeName.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeId = (TextField)binder.buildAndBind("Employee ID","employeeId",TextField.class);
		//txtEmployeeId.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeZone = (TextField)binder.buildAndBind("Employee Zone","employeeZone",TextField.class);
		//txtEmployeeZone.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtEmployeeDept = (TextField)binder.buildAndBind("Employee Dept","employeeDept",TextField.class);
		//txtEmployeeDept.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		employeeDetailsPanel = new Panel();
		FormLayout empDetails = new FormLayout(txtEmployeeName,txtEmployeeId,txtEmployeeZone,txtEmployeeDept);
		addStyleForTextfield(empDetails);
		empDetails.setSpacing(true);
		empDetails.setMargin(true);
		employeeDetailsPanel.setContent(empDetails);
		
		//employeeDetailsPanel.setStyleName("girdBorder");
		
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
		FormLayout policyDetails = new FormLayout(txtPolicyNo, txtIntimationNo , txtProductName ,/* txtDuration,*/ txtSumInsured);
		
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
	
	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("Submit");
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		/*AbsoluteLayout absoluteLayout_3
		.addComponent(prSearchBtn, "top:100.0px;left:220.0px;");*/
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		
		btnCancel = new Button();
		btnCancel.setCaption("Reset");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
/*		absoluteLayout_3.addComponent(resetBtn, "top:100.0px;left:329.0px;");
*/		//Vaadin8-setImmediate() btnCancel.setImmediate(true);

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit,btnCancel);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_RIGHT);
		hBtnLayout.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}
	
	private HorizontalLayout buildRequestTypeButton()
	{
		
		btnProcessRRCRequest = new Button();
		btnProcessRRCRequest.setCaption("Process RRC Request");
		//Vaadin8-setImmediate() btnProcessRRCRequest.setImmediate(true);
		btnProcessRRCRequest.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnProcessRRCRequest.setWidth("-10px");
		btnProcessRRCRequest.setHeight("-10px");
		//Vaadin8-setImmediate() btnProcessRRCRequest.setImmediate(true);
		
		
		btnPlaceOnHold = new Button();
		btnPlaceOnHold.setCaption("Place on Hold");
		//Vaadin8-setImmediate() btnPlaceOnHold.setImmediate(true);
		btnPlaceOnHold.addStyleName(ValoTheme.BUTTON_DANGER);
		btnPlaceOnHold.setWidth("-10px");
		btnPlaceOnHold.setHeight("-10px");
		//Vaadin8-setImmediate() btnPlaceOnHold.setImmediate(true);

		HorizontalLayout hBtnLayout = new HorizontalLayout(btnProcessRRCRequest,btnPlaceOnHold);
		hBtnLayout.setComponentAlignment(btnProcessRRCRequest, Alignment.MIDDLE_RIGHT);
		hBtnLayout.setComponentAlignment(btnPlaceOnHold, Alignment.MIDDLE_LEFT);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
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
				
				//AutocompleteField<ExtraEmployeeEffortDTO> field, List<ExtraEmployeeEffortDTO> employeeDetailsList);
			
	}
	
	public void addListener() {

	/*	btnSubmit.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
				if(validatePage())
				{
					setTableValuesToDTO();
					if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(presenterString))
					{
						fireViewEvent(ProcessRRCRequestPresenter.SUBMIT_RRC_REQUEST_FOR_PROCESS, bean);
					//}
				}
			}
			
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				//searable.doSearch();
				//resetAlltheValues();
			}
		});*/
		
		btnProcessRRCRequest.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					generateFieldsForRRCRequestProcessing();
			}
			
		});
		
		btnPlaceOnHold.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
					generateFieldsForPlaceOnHold();
			}
			
		});
	}

	public void buildRRCRequestSuccessLayout(String rrcRequestNo) {
		String successMessage = "RRC RequestNo" + " " + rrcRequestNo + " Successfully created !!!";
		Label successLabel = new Label(
				"<b style = 'color: green;'>" + successMessage + "</b>",
				ContentMode.HTML);

		// Label noteLabel = new
		// Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>",
		// ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
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
			//	fireViewEvent(MenuItemBean.PROCESS_CLAIM_REQUEST_ZONAL_REVIEW, null);
				popup.close();
			}
		});	
	}
	
	public void generateFieldsForRRCRequestProcessing()
	{
		
		if (processRRCRequestLayout != null
				&& processRRCRequestLayout.getComponentCount() > 0) {
			processRRCRequestLayout.removeAllComponents();
			unbindField(getListOfFields());
		}
		if (placeOnHoldLayout != null
				&& placeOnHoldLayout.getComponentCount() > 0) {
			placeOnHoldLayout.removeAllComponents();
			unbindField(txtRequestOnHoldRemarks);
			if(null != txtRequestOnHoldRemarks)
				mandatoryFields.remove(txtRequestOnHoldRemarks);
		}
		
		cmbEligiblity = (ComboBox)binder.buildAndBind("Eligibility","eligibility",ComboBox.class);
		 eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbEligiblity.setContainerDataSource(eligiblity);
		 cmbEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbEligiblity.setItemCaptionPropertyId("value");
		
		
		txtSavedAmount = (TextField)binder.buildAndBind("Saved Amount","savedAmount",TextField.class);
		txtSavedAmount.setMaxLength(10);
		txtSavedAmount.setMaxLength(10);
		CSValidator validator = new CSValidator();
		validator.extend(txtSavedAmount);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		addListenerForSavedAmount();
		
		
		txtEligibilityRemarks = (TextArea)binder.buildAndBind("Remarks","eligibilityRemarks",TextArea.class);
		txtEligibilityRemarks.setMaxLength(200);
		txtEligibilityRemarks.setWidth("50%");
		txtEligibilityRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtEligibilityRemarks,null,getUI(),SHAConstants.RRC_REMARK);
//		CSValidator validator1 = new CSValidator();
//		validator1.extend(txtEligibilityRemarks);
//		validator1.setRegExp("/^[ A-Za-z0-9_@./#&+-\n]*$/");
//		validator1.setPreventInvalidTyping(true);
		
		extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.setReferenceData(this.containerMap);
		extraEffortEmployeeListenerTable.init(true);
		
		rrcRequestCategoryListenerTable = rrcRequestCategoryListenerTableObj.get();
		rrcRequestCategoryListenerTable.initPresenter(this.presenterString);
		rrcRequestCategoryListenerTable.setReferenceData(this.containerMap);
		rrcRequestCategoryListenerTable.init(true);
		
		FormLayout layout1 = new FormLayout(cmbEligiblity,txtSavedAmount);
		layout1.setSpacing(true);
		layout1.setMargin(true);
		
		/*FormLayout layout2 = new FormLayout(txtSavedAmount);
		layout2.setSpacing(true);
		layout2.setMargin(true);*/
		
		FormLayout layout3 = new FormLayout(txtEligibilityRemarks);
		layout3.setSpacing(true);
		layout3.setMargin(true);
		
		FormLayout layout4 = new FormLayout(rrcRequestCategoryListenerTable);
		layout4.setSpacing(true);
		layout4.setMargin(true);
		
		FormLayout layout5 = new FormLayout(extraEffortEmployeeListenerTable);
		layout5.setSpacing(true);
		layout5.setMargin(true);
		
		processRRCRequestLayout.addComponent(layout1);
/*		processRRCRequestLayout.addComponent(layout2);*/
		processRRCRequestLayout.addComponent(layout4);
		processRRCRequestLayout.addComponent(layout3);
		processRRCRequestLayout.addComponent(layout5);
		
		setExtraEffortForEmployeeTable();
		
		this.bean.setRrcStatus(SHAConstants.RRC_STATUS_PROCESS);
		
		
		
	}
	
	public void addListenerForSavedAmount()
	{
		txtSavedAmount.addValueChangeListener(new ValueChangeListener() {			
			
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
					
				
					String value = (String) event.getProperty().getValue();
					
					
					if(null != value && Double.valueOf(value) > bean.getSumInsured())
					{
						String err = "Saved Amount Should not be greater than SumInsured";
						showErrorMessage(err);
						event.getProperty().setValue(null);
					}
				}
								
			});
			
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
	
	
	private void unbindField(List<Field<?>> field) {
		if(null != field && !field.isEmpty())
		{
			for (Field<?> field2 : field) {
				if (field2 != null ) {
					Object propertyId = this.binder.getPropertyId(field2);
					//if (field2!= null && field2.isAttached() && propertyId != null) {
					if (field2!= null  && propertyId != null) {
						this.binder.unbind(field2);
					}
				}
			}
		}
	}
	
	private List<Field<?>> getListOfFields()
	{
		List<Field<?>>  fieldList = new ArrayList<Field<?>>();
		fieldList.add(cmbEligiblity);
		fieldList.add(txtSavedAmount);
		fieldList.add(txtEligibilityRemarks);
		return fieldList;
	}
	
	private void setExtraEffortForEmployeeTable()
	{
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
		
		if(null != rrcRequestCategoryListenerTable)
		{
			List<ExtraEmployeeEffortDTO> rrcCatagory = this.bean.getRrcCategoryList();
			if(null != rrcCatagory)
			{
				Long sNo= 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcCatagory) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				rrcRequestCategoryListenerTable.setTableList(rrcCatagory);
			}
		}	
	}
	public void generateFieldsForPlaceOnHold()
	{

		if (placeOnHoldLayout != null
				&& placeOnHoldLayout.getComponentCount() > 0) {
			placeOnHoldLayout.removeAllComponents();
		}
		if (processRRCRequestLayout != null
				&& processRRCRequestLayout.getComponentCount() > 0) {
			processRRCRequestLayout.removeAllComponents();
			unbindField(getListOfFields());
		}
		
		unbindField(txtRequestOnHoldRemarks);
		
		txtRequestOnHoldRemarks = (TextArea)binder.buildAndBind("Remarks(Hold)","requestOnHoldRemarks",TextArea.class);
		txtRequestOnHoldRemarks.setRequired(true);
		txtRequestOnHoldRemarks.setMaxLength(200);
		CSValidator validator = new CSValidator();
		validator.extend(txtRequestOnHoldRemarks);
		validator.setRegExp("^[a-zA-Z 0-9 /]*$");
		validator.setPreventInvalidTyping(true);
		
		this.bean.setRrcStatus(SHAConstants.RRC_STATUS_ON_HOLD);
		
		placeOnHoldLayout.addComponent(new FormLayout(txtRequestOnHoldRemarks));
		
		mandatoryFields.add(txtRequestOnHoldRemarks);
		setRequiredAndValidation(txtRequestOnHoldRemarks);
		
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	@SuppressWarnings("unused")
	private void setRequiredAndValidation(Component component) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractField<Field> field = (AbstractField<Field>) component;
		field.setRequired(true);
		field.setValidationVisible(false);
	}
	
	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null  && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
		showOrHideValidation(true);
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}
		}
		
		if((SHAConstants.RRC_STATUS_PROCESS).equalsIgnoreCase(this.bean.getRrcStatus()))
		{
			if(null != cmbEligiblity )
			{
				SelectValue selValue = (SelectValue)cmbEligiblity.getValue();
				if(!(null != selValue && null != selValue.getValue()))
				{
					hasError = true;
					eMsg.append("Please select Eligibility").append("</br>");
				}
			}
			if(!(null != txtSavedAmount && null != txtSavedAmount.getValue() ))
			{
				
					hasError = true;
					eMsg.append("Please enter saved amount").append("</br>");
				
			}
			if(null != this.extraEffortEmployeeListenerTable)
			{
				Boolean isValid = extraEffortEmployeeListenerTable.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.extraEffortEmployeeListenerTable.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
			
			if(null != this.rrcRequestCategoryListenerTable)
			{
				Boolean isValid = rrcRequestCategoryListenerTable.isValid();
				if (!isValid) {
					hasError = true;
					List<String> errors = this.rrcRequestCategoryListenerTable.getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
			}
		}
		
		if (hasError) {
			//setRequired(true);
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
		else 
		{
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}		
	}
	
	 public void setsubCategoryValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox subCategory,SelectValue value){
		 this.rrcRequestCategoryListenerTable.setsubCategoryValues(selectValueContainer, subCategory, value);
	 }
	 
	 public void setsourceValues(BeanItemContainer<SelectValue> selectValueContainer,GComboBox source,SelectValue value){
		 this.rrcRequestCategoryListenerTable.setsourceValues(selectValueContainer, source, value);
	 }
	
	
	 public void invalidate(){
		 quantumReductionListenerTable.clearObjects();
		 extraEffortEmployeeListenerTable.invalidate();
		 rrcRequestCategoryListenerTable.invalidate();
		 SHAUtils.setClearReferenceData(containerMap);
		 eligiblity = null;
		 significantClinicalInformation = null;
		 binder = null;
		 presenterString = null;
		 processRRCRequestLayout.removeAllComponents();
		 processRRCRequestLayout = null;
		 placeOnHoldLayout.removeAllComponents();
		 placeOnHoldLayout = null;
	 }
	
	
	

}
