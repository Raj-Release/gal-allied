/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.detailsPage;

/**
 * @author ntv.vijayar
 *
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.vaadin.addon.cdimvp.ViewComponent;
/*import org.vaadin.dialogs.ConfirmDialog;*/




import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reimbursement.dto.EmployeeMasterDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.services.ReviewRRCRequestService;
import com.shaic.claim.rod.wizard.tables.ExtraEffortEmployeeListenerTable;
import com.shaic.claim.rod.wizard.tables.QuantumReductionDetailsListenerTable;
import com.shaic.claim.rod.wizard.tables.RRCRequestCategoryListenerTable;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.domain.TmpEmployee;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
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
import com.vaadin.v7.ui.Label;
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
public class ViewClaimWiseRRCHistoryPage extends ViewComponent {
	
	/*@PersistenceContext
	protected EntityManager entityManager;*/
	
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
	
	
	/*@Inject
	private Instance<ExtraEffortEmployeeListenerTable> extraEffortEmployeeListenerTableObjForModify;*/
	
	private ExtraEffortEmployeeListenerTable extraEffortEmployeeListenerTableForModify;
	
	private Button btnSubmit;
	
	/*private Button btnCancel;
	
	private Button btnProcessRRCRequest;
	
	private Button btnPlaceOnHold;*/
	
	private String presenterString;
	
	private RRCDTO bean;
	
	private Window popup;
	
	private BeanFieldGroup<RRCDTO> binder;
	
//	private VerticalLayout processRRCRequestLayout;
	
//	private HorizontalLayout placeOnHoldLayout;
	
	private ComboBox cmbEligiblity;
	private TextField txtSavedAmount;
	
	private TextArea txtEligibilityRemarks;
	
	//private TextArea txtRequestOnHoldRemarks;
	
	private TextField txtProcessEligibility;
	
	//private TextField txtProcessSavedAmount;
	
	private TextField txtFollowUp;
	
	private TextField txtProcessRRCRemarks;
	
	/*private ComboBox cmbModifiedEligiblity;
	
	private TextField txtModifiedSavedAmount;
	
	private TextArea txtModifiedRemarks;
	*/
	@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcRequestCategoryListenerTableObj;
	
	private RRCRequestCategoryListenerTable rrcRequestCategoryListenerTable;
	
	/*@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcRequestCategoryListenerTableObjForModify;*/
	
	@EJB
	private ReviewRRCRequestService reviewRRCRequestService;
	
	private RRCRequestCategoryListenerTable rrcRequestCategoryListenerTableForModify;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Map<String,Object> containerMap;
	
	private BeanItemContainer<SelectValue> eligiblity;
	
	private BeanItemContainer<SelectValue> significantClinicalInformation;
	 
	//private BeanItemContainer<SelectValue> significantContainer;
	
	private TextField txtRRCRequestNo;
	
	private TextField txtRequestIntiatedDate;
	
	
	private TextField txtRequestProcessedBy;
	
	private TextField txtRequestProcessedDate;
	
	private TextField txtRequestReviewedBy;
	
	private TextField txtRequestReviewedDate;

	private String rrcRequestNo = null;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	@PostConstruct
	public void init() {
		
		

	}
	
	public void init(RRCDTO bean,Window popup) {
		this.bean = bean;
		this.popup = popup;
	
	}
	
	public void init(RRCDTO bean,Window popup,String rrcRequestNo) {
		this.bean = bean;
		this.popup = popup;
		this.rrcRequestNo = rrcRequestNo;
	
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
	//	detailsLayout.setSizeFull();
		Panel detailsPanel = new Panel();
		detailsPanel.setContent(detailsLayout);
	//	detailsPanel.setWidth("70%");;
			
		HorizontalLayout hLayout = buildRequestFields();
		
		VerticalLayout verticalRRCLayout = new VerticalLayout(hLayout ,detailsPanel , buildReductionDetailsPanel(), buildRRCProcessingDetails() , buildRRCReviewDetails() ,buildButtonLayout() /*,buildButtonLayout()*/);
	
		verticalRRCLayout.setSpacing(true);
		verticalRRCLayout.setMargin(false);
		rrcViewPanel = new Panel();
		
	
		rrcViewPanel.setContent(verticalRRCLayout);
		rrcViewPanel.setSizeFull();
		//loadContainerDataSources();
		setTableValues();
		addListener();
		
		setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}
	
	private Panel buildReductionDetailsPanel()
	{
		txtClaimType = (TextField)binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtProcessingStage = (TextField)binder.buildAndBind("Processing Stage","processingStage",TextField.class);
		//CR2019204 
/*		cmbSignificantClinicalInformation = (ComboBox)binder.buildAndBind("Significant Clinincal Information","significantClinicalInformation",ComboBox.class);*/		
		txtRemarks = (TextArea)binder.buildAndBind("Remark","remarks",TextArea.class);
		txtRemarks.setRequired(true);
		
		FormLayout reductionDetails = new FormLayout(txtClaimType , txtProcessingStage);
		reductionDetails.setWidth("-1px");
		reductionDetails.setSpacing(true);
		
		addStyleForTextfield(reductionDetails);
		//CR2019204 
	/*	FormLayout clinicalInfo = new FormLayout(cmbSignificantClinicalInformation);*/
		FormLayout remarks = new FormLayout(txtRemarks);
		
		//CR2019204 
		/*addStyleForTextfield(clinicalInfo);*/
		addStyleForTextfield(remarks);

		
		quantumReductionListenerTable = quantumReductionListenerTableObj.get();
		quantumReductionListenerTable.init(this.bean);
		quantumReductionListenerTable.initPresenter(SHAConstants.VIEW_RRC_REQUEST);

		
		HorizontalLayout infoAndRemarksLayout = new HorizontalLayout( remarks);
		infoAndRemarksLayout.setSpacing(true);
		
		VerticalLayout vReductionLayout = new VerticalLayout(reductionDetails , quantumReductionListenerTable , infoAndRemarksLayout );
		vReductionLayout.setWidth("100%");
		vReductionLayout.setComponentAlignment(reductionDetails, Alignment.TOP_RIGHT);
		vReductionLayout.setSpacing(true);
		vReductionLayout.setMargin(true);
//		vReductionLayout.setReadOnly(true);
		
		quantumReductionDetailsPanel = new Panel();
		quantumReductionDetailsPanel.setContent(vReductionLayout);
		quantumReductionDetailsPanel.setStyleName("girdBorder");
		//quantumReductionDetailsPanel.setStyleName("girdBorder");
		
		return quantumReductionDetailsPanel;
		
	}
	
	private Panel buildRRCProcessingDetails()
	{
		txtRequestProcessedBy = (TextField)binder.buildAndBind("Request Processed By","processedBy",TextField.class);
		txtRequestProcessedDate = (TextField)binder.buildAndBind("Request Processed Date","processedDate",TextField.class);
		
		
		txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","requestRRCElgilibilityValue",TextField.class);
		txtProcessEligibility.setReadOnly(true);
		txtSavedAmount = (TextField)binder.buildAndBind("Saved Amount","requestRRCSavedAmount",TextField.class);
		txtSavedAmount.setReadOnly(true);
		txtFollowUp = (TextField)binder.buildAndBind("Follow Up","followUp",TextField.class);
		txtFollowUp.setReadOnly(true);
		txtProcessRRCRemarks = (TextField)binder.buildAndBind("Remarks","requestEligbilityRRCRemarks",TextField.class);
		txtProcessRRCRemarks.setReadOnly(true);
		
		FormLayout fLayout = new FormLayout(txtRequestProcessedBy,txtRequestProcessedDate);
		fLayout.setSpacing(true);
		
		addStyleForTextfield(fLayout);
		
		
		
		
		
		FormLayout rrcProcessingForm = new FormLayout(txtProcessEligibility,txtSavedAmount,txtFollowUp,txtProcessRRCRemarks);
		rrcProcessingForm.setMargin(true);
		rrcProcessingForm.setSpacing(true);
		
		addStyleForTextfield(rrcProcessingForm);
		
		//rrcProcessingForm.setCaption("RRC Processing Details");
		HorizontalLayout hLayout = new HorizontalLayout(rrcProcessingForm, fLayout);
		
		hLayout.setWidth("100%");
		Panel rrcProcessingPanel = new Panel();
		rrcProcessingPanel.setCaption("RRC Processing Details");
		rrcProcessingPanel.setContent(hLayout);
		rrcProcessingPanel.setHeight("200px");
		rrcProcessingPanel.setStyleName("girdBorder");
		
		return rrcProcessingPanel;		
	}
	
	private HorizontalLayout buildRequestFields()
	{
		txtRRCRequestNo = (TextField)binder.buildAndBind("RRC Request No","rrcRequestNo",TextField.class);
		txtRRCRequestNo.setReadOnly(false);
		txtRRCRequestNo.setNullRepresentation("");
		if(null != rrcRequestNo && !("").equalsIgnoreCase(rrcRequestNo))
		{
			txtRRCRequestNo.setValue(rrcRequestNo);
		}
		txtRRCRequestNo.setReadOnly(true);
		txtRequestIntiatedDate = (TextField)binder.buildAndBind("Request Initiated Date","rrcintiatedDate",TextField.class);
		FormLayout fLayout1 = new FormLayout(txtRRCRequestNo);
		FormLayout fLayout2 = new FormLayout(txtRequestIntiatedDate);
		
		addStyleForTextfield(fLayout1);
		addStyleForTextfield(fLayout2);
		
		HorizontalLayout hLayout = new HorizontalLayout(fLayout1,fLayout2);
		
		hLayout.setEnabled(false);
		return hLayout;
	}
	
	private void addStyleForTextfield(FormLayout layout)
	{
		int iLayoutSize = layout.getComponentCount();
		for (int i = 0; i < iLayoutSize; i++) {
			Component comp = layout.getComponent(i);
			if(comp instanceof TextField)
			{
				TextField txtFld = (TextField)comp;
				txtFld.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				txtFld.setNullRepresentation("");
				txtFld.setReadOnly(true);
			}
			/*else if (comp instanceof ComboBox)
			{
				ComboBox txtFld = (ComboBox)comp;
				//txtFld.setStyleName(ValoTheme.);
				txtFld.setReadOnly(true);

			}*/
			
		}
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
	
	
	private Panel buildRRCReviewDetails()
	{
		
		txtRequestReviewedBy = (TextField)binder.buildAndBind("Request Reviewed By","reviewedBy",TextField.class);
		txtRequestReviewedDate = (TextField)binder.buildAndBind("Request Reviewed Date","reviewedDate",TextField.class);
		
		txtRequestReviewedDate.setValidationVisible(false);
		
		 cmbEligiblity = (ComboBox)binder.buildAndBind("Eligibility","reviewEligiblity",ComboBox.class);
		/* eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbEligiblity.setContainerDataSource(eligiblity);
		 cmbEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbEligiblity.setItemCaptionPropertyId("value");*/		
		
		txtSavedAmount = (TextField)binder.buildAndBind("Saved Amount","savedAmount",TextField.class);
		txtSavedAmount.setReadOnly(true);
		txtEligibilityRemarks = (TextArea)binder.buildAndBind("Remarks","eligibilityRemarks",TextArea.class);
		txtEligibilityRemarks.setReadOnly(true);
		
		extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.setReferenceData(this.containerMap);
		extraEffortEmployeeListenerTable.init(false);
		extraEffortEmployeeListenerTable.setEnabled(false);
		
		rrcRequestCategoryListenerTable = rrcRequestCategoryListenerTableObj.get();
		rrcRequestCategoryListenerTable.initPresenter(this.presenterString);
		rrcRequestCategoryListenerTable.setReferenceData(this.containerMap);
		rrcRequestCategoryListenerTable.init(false);
		/*
		FormLayout fLayout = new FormLayout(txtRequestProcessedBy);
		fLayout.setSpacing(true);
		
		FormLayout fLayout1 = new FormLayout(txtRequestProcessedDate);
		fLayout1.setSpacing(true);
		
		//VerticalLayout processVLayout = new VerticalLayout(fLayout,fLayout1);
		
		VerticalLayout processVLayout = new VerticalLayout(fLayout,fLayout1);*/
		FormLayout fLayout = new FormLayout(txtRequestReviewedBy,txtRequestReviewedDate);
		fLayout.setSpacing(true);
		addStyleForTextfield(fLayout);
		
		FormLayout layout1 = new FormLayout(cmbEligiblity,txtSavedAmount,txtFollowUp,txtEligibilityRemarks);//rrcRequestCategoryListenerTable, extraEffortEmployeeListenerTable);
		layout1.setSpacing(true);
		layout1.setMargin(true);
		
		addStyleForTextfield(layout1);
		
		HorizontalLayout hLayout1 = new HorizontalLayout(rrcRequestCategoryListenerTable);
		hLayout1.setWidth("100%");
		
		HorizontalLayout hLayout2 = new HorizontalLayout(extraEffortEmployeeListenerTable);
		hLayout2.setWidth("100%");
	
		/*FormLayout layout4 = new FormLayout(rrcRequestCategoryListenerTable);
		layout4.setSpacing(true);
		layout4.setMargin(true);
		
		FormLayout layout5 = new FormLayout(extraEffortEmployeeListenerTable);
		layout5.setSpacing(true);
		layout5.setMargin(true);*/
		
		HorizontalLayout hLayout = new HorizontalLayout(layout1 , fLayout);
		hLayout.setWidth("100%");
		
		
		
		VerticalLayout processRRCRequestLayout = new VerticalLayout(hLayout , hLayout1 , hLayout2);
		
		/*processRRCRequestLayout.addComponent(processVLayout);
		processRRCRequestLayout.addComponent(layout1);

		processRRCRequestLayout.addComponent(layout4);
		processRRCRequestLayout.addComponent(layout5);
		processRRCRequestLayout.setSpacing(true);
		processRRCRequestLayout.setCaption("RRC Review Details");
		processRRCRequestLayout.setMargin(true);
		processRRCRequestLayout.setComponentAlignment(processVLayout, Alignment.TOP_RIGHT);
		processRRCRequestLayout.setReadOnly(true);*/
		
		Panel rrcReviewPanel = new Panel();
		rrcReviewPanel.setContent(processRRCRequestLayout);
		rrcReviewPanel.setCaption("RRC Reviewed Details");
		rrcReviewPanel.setStyleName("girdBorder");
		rrcReviewPanel.setSizeFull();
		
		//setExtraEffortForEmployeeTable();
		
		return rrcReviewPanel;
		//this.bean.setRrcStatus(SHAConstants.RRC_STATUS_PROCESS);
		
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
		if(null != quantumReductionListenerTable)
		{
			/*QuantumReductionDetailsDTO quantumReductionDetailsDTO = this.bean.getQuantumReductionDetailsDTO();
			if(null != quantumReductionDetailsDTO)
			{
				quantumReductionListenerTable.addBeanToList(quantumReductionDetailsDTO);
			}*/
			
			
			List<QuantumReductionDetailsDTO> quantumReductionDetailsDTO = this.bean.getQuantumReductionListForClaimWiseRRCHistory();
			if(null != quantumReductionDetailsDTO && !quantumReductionDetailsDTO.isEmpty())
			{
				for (QuantumReductionDetailsDTO quantumReductionDetailsDTO2 : quantumReductionDetailsDTO) {
					if(null != rrcRequestNo && !("").equalsIgnoreCase(rrcRequestNo))
					{
						if(rrcRequestNo.equalsIgnoreCase(quantumReductionDetailsDTO2.getRequestNo()))
						{
							quantumReductionListenerTable.addBeanToList(quantumReductionDetailsDTO2);
							break;
						}
					}
				}
				//quantumReductionListenerTable.addBeanToList(quantumReductionDetailsDTO);
			}
			
			
			/*List<QuantumReductionDetailsDTO> quantumReductionDetailsList = this.bean.getQuantumReductionDetailsDTOList();
			if(null != quantumReductionDetailsList && !quantumReductionDetailsList.isEmpty())
			{
				quantumReductionListenerTable.setTableList(quantumReductionDetailsList);
			}*/
		}
		
		if(null != rrcRequestCategoryListenerTable)
		{
			List<ExtraEmployeeEffortDTO> rrcRequestCategoryList = this.bean.getRrcCategoryList();
			if(null != rrcRequestCategoryList && !rrcRequestCategoryList.isEmpty())
			{
				Long sNo= 1l;
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : rrcRequestCategoryList) {
					extraEmployeeEffortDTO.setSlNo(sNo);
					sNo++;
				}
				rrcRequestCategoryListenerTable.setTableList(rrcRequestCategoryList);
				//this.bean.setRrcCategoryList(rrcRequestCategoryList);
			}
		}
		if(null != extraEffortEmployeeListenerTable)
		{
			RRCRequest rrcdetails = reviewRRCRequestService.getRrcRequestList(rrcRequestNo);
			
			if(rrcdetails != null){
				txtRequestReviewedBy.setReadOnly(false);
				txtRequestReviewedBy.setValue(rrcdetails.getReviewedBy());
				txtRequestReviewedBy.setReadOnly(true);
				
				txtRequestReviewedDate.setReadOnly(false);
				if(rrcdetails.getReviewedDate() != null){
					txtRequestReviewedDate.setValue(rrcdetails.getReviewedDate().toString());
				}else{
					txtRequestReviewedDate.setValue("-");
				}
				
				txtRequestReviewedDate.setReadOnly(true);
				
				txtSavedAmount.setReadOnly(false);
				if(rrcdetails.getSavedAmount() != null){
					txtSavedAmount.setValue(rrcdetails.getSavedAmount().toString());
				}else{
					txtSavedAmount.setValue("-");
				}
				txtSavedAmount.setReadOnly(true);
				
				txtEligibilityRemarks.setReadOnly(false);
				txtEligibilityRemarks.setValue(rrcdetails.getEligibiltyRemarks());
				txtEligibilityRemarks.setReadOnly(true);
			}
			
			TmpEmployee tmpEmployee = reimbursementService.getEmployeeDetails(rrcdetails.getRequestorID());
			if(null != tmpEmployee)
			{
				/*if(!(null != tmpEmployee.getEmpFirstName()))
					tmpEmployee.setEmpFirstName("");
				else if(!(null != tmpEmployee.getEmpMiddleName()))
					tmpEmployee.setEmpMiddleName("");
				else if(!(null != tmpEmployee.getEmpLastName()))
					tmpEmployee.setEmpLastName("");*/
				StringBuffer strName = new StringBuffer();
				if(null != tmpEmployee.getEmpFirstName())
				{
					strName.append(tmpEmployee.getEmpFirstName());
				}
				if(null != tmpEmployee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(tmpEmployee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(tmpEmployee.getEmpMiddleName());
					}
				}
				if(null != tmpEmployee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(tmpEmployee.getEmpLastName()); 
					}
					else
					{
						strName.append(tmpEmployee.getEmpLastName());
					}
				}
					
				//requestRRCDTO.setEmployeeName(tmpEmployee.getEmpFirstName()+tmpEmployee.getEmpMiddleName()+tmpEmployee.getEmpLastName());
				txtEmployeeName.setReadOnly(false);
				txtEmployeeName.setValue(strName.toString());
				txtEmployeeName.setReadOnly(true);
				txtEmployeeId.setReadOnly(false);
				txtEmployeeId.setValue(tmpEmployee.getEmpId());
				txtEmployeeId.setReadOnly(true);
			}
			
			Long rrcKey = rrcdetails.getRrcRequestKey();
			
//			RRCDetails rrcrequest = getEmployeList(rrcKey);
//			ExtraEmployeeEffortDTO extraEmployeeEffortDTO2 = new ExtraEmployeeEffortDTO();
//			if(rrcrequest != null) {
//			extraEmployeeEffortDTO2.setEmployeeId(rrcrequest.getEmployeeId());
//			extraEmployeeEffortDTO2.setEmployeeName(rrcrequest.getEmployeeName());
//			}
			
//			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
			List<ExtraEmployeeEffortDTO> extraEmployeeList = reviewRRCRequestService.getEmployeeDetailsFromRRCDetails(rrcKey);
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
			
			
			/*if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				extraEffortEmployeeListenerTable.setTableList(extraEmployeeList);
			}*/
		}
		
		if(null != rrcRequestCategoryListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> rrcRequestCategoryList = this.bean.getRrcCategoryList();
			if(null != rrcRequestCategoryList && !rrcRequestCategoryList.isEmpty())
			{
				rrcRequestCategoryListenerTableForModify.setTableList(rrcRequestCategoryList);
				//this.bean.setRrcCategoryList(rrcRequestCategoryList);
			}
		}
		if(null != extraEffortEmployeeListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				extraEffortEmployeeListenerTableForModify.setTableList(extraEmployeeList);
			}
		}
		
		
	}
	
	public void setTableValuesToDTO()
	{
		if(null != extraEffortEmployeeListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = extraEffortEmployeeListenerTableForModify.getValues();
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
		if(null != rrcRequestCategoryListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> rrcRequestCategoryList = rrcRequestCategoryListenerTableForModify.getValues();
			if(null != rrcRequestCategoryList && !rrcRequestCategoryList.isEmpty())
			{
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
		//CR2019204 
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
		 
		 eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbEligiblity.setContainerDataSource(eligiblity);
		 cmbEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbEligiblity.setItemCaptionPropertyId("value");
		 
		 for(int i = 0 ; i<eligiblity.size() ; i++)
		 	{
			 if(null != this.bean.getReviewEligiblityValue())
		 		{
					if ((this.bean.getReviewEligiblityValue()).equalsIgnoreCase(eligiblity.getIdByIndex(i).getValue()))
					{
						this.cmbEligiblity.setValue(eligiblity.getIdByIndex(i));
					}
		 		}
			}
		 cmbEligiblity.setReadOnly(true);

		 
		/*if(SHAConstants.ZONAL_REVIEW.equalsIgnoreCase(this.presenterString))
		{
			fireViewEvent(MedicalApprovalZonalReviewWizardPresenter.LOAD_RRC_REQUEST_DROP_DOWN_VALUES, bean);
		}*/
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
	//	employeeDetailsPanel.setSizeFull();
		
		//employeeDetailsPanel.setStyleName("girdBorder");
		
		return employeeDetailsPanel;
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
	//	policyDetailsPanel.setSizeFull();
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
		//hospitalDetailsPanel.setSizeFull();
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
	//	insuredDetailsPanel.setSizeFull();
		//insuredDetailsPanel.setStyleName("girdBorder");
		return insuredDetailsPanel;
	}
	

	/*public RRCDetails getEmployeList(Long rrcKey) {
		if(rrcKey != null) {
		Query findemploye = entityManager.createNamedQuery("RRCDetails.findByRequestKey").setParameter("rrcRequestKey", rrcKey);
		RRCDetails tmpEmploye = (RRCDetails) findemploye.getSingleResult();
		return tmpEmploye; 
		}
		else {
			return null;
		}
		
	}*/
	
	/*public RRCRequest getRrcRequestList(String rrcRequestKey) {
		
		if(rrcRequestKey != null) {
			Query findemploye = entityManager.createNamedQuery("RRCRequest.CountAckByRRCRequestNo").setParameter("rrcRequestNumber", rrcRequestKey);
			List<RRCRequest> tmpEmploye = (List<RRCRequest>) findemploye.getResultList();
			
			if(tmpEmploye != null && ! tmpEmploye.isEmpty()){
				return tmpEmploye.get(0);
			}
			
			return null; 
			}
			else {
				return null;
			}
	}*/

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
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
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
	
	/*private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}*/
	
	
	public boolean validatePage() {
		
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();
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
		
		if (hasError) {
			//setRequired(true);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/
			HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
			buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
			GalaxyAlertBox.createErrorBox(eMsg.toString(), buttonsNamewithType);
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
//			showOrHideValidation(false);
			return true;
		}		
	}


	public void clearObjects(){
		if(quantumReductionListenerTable != null){
			quantumReductionListenerTable.clearObjects();
		}
		if(extraEffortEmployeeListenerTable != null){
			extraEffortEmployeeListenerTable.invalidate();
		}
		if(extraEffortEmployeeListenerTableForModify != null){
			extraEffortEmployeeListenerTableForModify.invalidate();
		}
		if(rrcRequestCategoryListenerTable != null){
			rrcRequestCategoryListenerTable.invalidate();
		}
		if(rrcRequestCategoryListenerTableForModify != null){
			rrcRequestCategoryListenerTableForModify.invalidate();
		}
		
		bean = null;
		binder = null;
		popup = null;
		reviewRRCRequestService = null;
		reimbursementService = null;
		presenterString = null;
		containerMap = null;
	}
	

	
	
	

}
