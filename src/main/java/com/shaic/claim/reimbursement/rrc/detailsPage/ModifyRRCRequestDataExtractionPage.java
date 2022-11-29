/**
 * 
 */
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
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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
public class ModifyRRCRequestDataExtractionPage extends ViewComponent {
	
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
	
	
	@Inject
	private Instance<ExtraEffortEmployeeListenerTable> extraEffortEmployeeListenerTableObjForModify;
	
	private ExtraEffortEmployeeListenerTable extraEffortEmployeeListenerTableForModify;
	
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
	
	private ComboBox cmbModifiedEligiblity;
	
	private TextField txtModifiedSavedAmount;
	
	private TextArea txtModifiedRemarks;
	
	@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcRequestCategoryListenerTableObj;
	
	private RRCRequestCategoryListenerTable rrcRequestCategoryListenerTable;
	
	@Inject
	private Instance<RRCRequestCategoryListenerTable> rrcRequestCategoryListenerTableObjForModify;
	
	private RRCRequestCategoryListenerTable rrcRequestCategoryListenerTableForModify;
	
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
			
		VerticalLayout verticalRRCLayout = new VerticalLayout(detailsPanel , buildReductionDetailsPanel(), buildRRCProcessingDetails() , buildRRCReviewDetails() ,buildRRCModifyDetails() /*,buildButtonLayout()*/);
	
		verticalRRCLayout.setSpacing(true);
		verticalRRCLayout.setMargin(true);
		rrcViewPanel = new Panel();
		
		rrcViewPanel.setSizeFull();
		rrcViewPanel.setContent(verticalRRCLayout);
		
		//loadContainerDataSources();
		setTableValues();
		addListener();
		
		//setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}
	
	private Panel buildReductionDetailsPanel()
	{
		txtClaimType = (TextField)binder.buildAndBind("Claim Type","claimType",TextField.class);
		txtProcessingStage = (TextField)binder.buildAndBind("Processing Stage","processingStage",TextField.class);
		txtClaimType.setReadOnly(true);
		txtProcessingStage.setReadOnly(true);
/*		cmbSignificantClinicalInformation = (ComboBox)binder.buildAndBind("Significant Clinincal Information","significantClinicalInformation",ComboBox.class);*/
/*		cmbSignificantClinicalInformation.setEnabled(false);*/
		txtRemarks = (TextArea)binder.buildAndBind("Remark","remarks",TextArea.class);
//		txtRemarks.setRequired(true);
		txtRemarks.setMaxLength(200);
//		txtRemarks.setRequired(true);
		txtRemarks.setEnabled(false);
		
		FormLayout reductionDetails = new FormLayout(txtClaimType , txtProcessingStage);
		reductionDetails.setWidth("-1px");
		reductionDetails.setSpacing(true);
		
		addStyleForTextfield(reductionDetails);
		
/*		FormLayout clinicalInfo = new FormLayout(cmbSignificantClinicalInformation);*/
		FormLayout remarks = new FormLayout(txtRemarks);
		

		
		quantumReductionListenerTable = quantumReductionListenerTableObj.get();
		quantumReductionListenerTable.init(bean);
		quantumReductionListenerTable.setEnabled(false);

		
		HorizontalLayout infoAndRemarksLayout = new HorizontalLayout( remarks);
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
	
	private Panel buildRRCProcessingDetails()
	{
		txtProcessEligibility = (TextField)binder.buildAndBind("Eligibility","requestRRCElgilibilityValue",TextField.class);
		txtProcessEligibility.setReadOnly(true);
		txtSavedAmount = (TextField)binder.buildAndBind("Saved Amount","requestRRCSavedAmount",TextField.class);
		txtSavedAmount.setReadOnly(true);
		txtFollowUp = (TextField)binder.buildAndBind("Follow Up","followUp",TextField.class);
		txtFollowUp.setReadOnly(true);
		txtProcessRRCRemarks = (TextField)binder.buildAndBind("Remarks","requestEligbilityRRCRemarks",TextField.class);
		txtProcessRRCRemarks.setReadOnly(true);
		txtProcessRRCRemarks.setMaxLength(200);
		
		FormLayout rrcProcessingForm = new FormLayout(txtProcessEligibility,txtSavedAmount,txtFollowUp,txtProcessRRCRemarks);
		rrcProcessingForm.setMargin(true);
		rrcProcessingForm.setSpacing(true);
		
		//rrcProcessingForm.setCaption("RRC Processing Details");
		
		Panel rrcProcessingPanel = new Panel();
		rrcProcessingPanel.setCaption("RRC Processing Details");
		rrcProcessingPanel.setContent(rrcProcessingForm);
		
		return rrcProcessingPanel;		
	}
	
	private Panel buildRRCReviewDetails()
	{
		 cmbEligiblity = (ComboBox)binder.buildAndBind("Eligibility","reviewEligiblity",ComboBox.class);
		/* eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbEligiblity.setContainerDataSource(eligiblity);
		 cmbEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbEligiblity.setItemCaptionPropertyId("value");*/
		
		
		txtSavedAmount = (TextField)binder.buildAndBind("Saved Amount","savedAmount",TextField.class);
		txtSavedAmount.setReadOnly(true);
		txtSavedAmount.setMaxLength(10);
		CSValidator validator = new CSValidator();
		validator.extend(txtSavedAmount);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		
		
		txtEligibilityRemarks = (TextArea)binder.buildAndBind("Remarks","eligibilityRemarks",TextArea.class);
		txtEligibilityRemarks.setReadOnly(true);
		txtEligibilityRemarks.setMaxLength(200);
		txtEligibilityRemarks.setWidth("50%");
		txtEligibilityRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtEligibilityRemarks,null,getUI(),SHAConstants.RRC_REMARK);
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtEligibilityRemarks);
		validator1.setRegExp("^[a-zA-Z 0-9 /]*$");
		validator1.setPreventInvalidTyping(true);
		
		extraEffortEmployeeListenerTable = extraEffortEmployeeListenerTableObj.get();
		extraEffortEmployeeListenerTable.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTable.setReferenceData(this.containerMap);
		extraEffortEmployeeListenerTable.init(false);
		extraEffortEmployeeListenerTable.setEnabled(false);
		
		rrcRequestCategoryListenerTable = rrcRequestCategoryListenerTableObj.get();
		rrcRequestCategoryListenerTable.initPresenter(this.presenterString);
		rrcRequestCategoryListenerTable.setReferenceData(this.containerMap);
		rrcRequestCategoryListenerTable.init(false);
		rrcRequestCategoryListenerTable.setEnabled(false);
		
		FormLayout layout1 = new FormLayout(cmbEligiblity,txtSavedAmount);
		layout1.setSpacing(true);
		layout1.setMargin(true);
	
		FormLayout layout4 = new FormLayout(rrcRequestCategoryListenerTable);
		layout4.setSpacing(true);
		layout4.setMargin(true);
		
		FormLayout layout5 = new FormLayout(extraEffortEmployeeListenerTable);
		layout5.setSpacing(true);
		layout5.setMargin(true);
		
		FormLayout layout3 = new FormLayout(txtEligibilityRemarks);
		layout3.setSpacing(true);
		layout3.setMargin(true);
		
		VerticalLayout processRRCRequestLayout = new VerticalLayout();
		
		
		processRRCRequestLayout.addComponent(layout1);
		processRRCRequestLayout.addComponent(layout4);
		processRRCRequestLayout.addComponent(layout3);
		processRRCRequestLayout.addComponent(layout5);
		processRRCRequestLayout.setSpacing(true);
		processRRCRequestLayout.setCaption("RRC Review Details");
		processRRCRequestLayout.setMargin(true);
		
		Panel rrcReviewPanel = new Panel();
		rrcReviewPanel.setContent(processRRCRequestLayout);
		rrcReviewPanel.setCaption("RRC Review Details");
		
		
		//setExtraEffortForEmployeeTable();
		
		return rrcReviewPanel;
		//this.bean.setRrcStatus(SHAConstants.RRC_STATUS_PROCESS);
		
	}
	
	private Panel buildRRCModifyDetails()
	{
		 cmbModifiedEligiblity = (ComboBox)binder.buildAndBind("Eligibility","rrcModifiedEligbility",ComboBox.class);
		 eligiblity = (BeanItemContainer<SelectValue>) containerMap.get(SHAConstants.RRC_ELIGIBLITY_CONTAINER);
		 cmbModifiedEligiblity.setContainerDataSource(eligiblity);
		 cmbModifiedEligiblity.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbModifiedEligiblity.setItemCaptionPropertyId("value");
		
		 
		 for(int i = 0 ; i<eligiblity.size() ; i++)
		 	{
				 
					 if(null != this.bean.getRrcModifiedEligiblityValue())
					 {
						if ((eligiblity.getIdByIndex(i).getValue()).equalsIgnoreCase(this.bean.getRrcModifiedEligiblityValue()))
						{
							this.cmbModifiedEligiblity.setValue(eligiblity.getIdByIndex(i));
						}
					 }
				 
		 	}

		 
		
		txtModifiedSavedAmount = (TextField)binder.buildAndBind("Saved Amount","rrcModifiedSavedAmount",TextField.class);
		txtModifiedSavedAmount.setMaxLength(10);
		CSValidator validator = new CSValidator();
		validator.extend(txtModifiedSavedAmount);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
		addListenerForSavedAmount();
		
		txtModifiedRemarks = (TextArea)binder.buildAndBind("Remarks","rrcModifiedRemarks",TextArea.class);
		txtModifiedRemarks.setMaxLength(200);
		txtModifiedRemarks.setWidth("50%");
		txtModifiedRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(txtModifiedRemarks,null,getUI(),SHAConstants.RRC_REMARK);
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtModifiedRemarks);
		validator1.setRegExp("^[a-zA-Z 0-9 /]*$");
		validator1.setPreventInvalidTyping(true);
		
		extraEffortEmployeeListenerTableForModify = extraEffortEmployeeListenerTableObjForModify.get();
		extraEffortEmployeeListenerTableForModify.initPresenter(this.presenterString);
		extraEffortEmployeeListenerTableForModify.setReferenceData(this.containerMap);
		extraEffortEmployeeListenerTableForModify.init(true);
	
		
		rrcRequestCategoryListenerTableForModify = rrcRequestCategoryListenerTableObjForModify.get();
		rrcRequestCategoryListenerTableForModify.initPresenter(this.presenterString);
		rrcRequestCategoryListenerTableForModify.setReferenceData(this.containerMap);
		rrcRequestCategoryListenerTableForModify.init(true);
		
		FormLayout layout1 = new FormLayout(cmbModifiedEligiblity,txtModifiedSavedAmount);
		layout1.setSpacing(true);
		layout1.setMargin(true);
		
		FormLayout layout3 = new FormLayout(txtModifiedRemarks);
		layout3.setSpacing(true);
		layout3.setMargin(true);
	
		/*FormLayout layout4 = new FormLayout(rrcRequestCategoryListenerTable);
		layout4.setSpacing(true);
		layout4.setMargin(true);
		
		FormLayout layout5 = new FormLayout(extraEffortEmployeeListenerTable);
		layout5.setSpacing(true);
		layout5.setMargin(true);*/
		
		VerticalLayout processRRCRequestLayout = new VerticalLayout();
		
		
		processRRCRequestLayout.addComponent(layout1);
		processRRCRequestLayout.addComponent(rrcRequestCategoryListenerTableForModify);
		processRRCRequestLayout.addComponent(layout3);
		processRRCRequestLayout.addComponent(extraEffortEmployeeListenerTableForModify);
		processRRCRequestLayout.setSpacing(true);
		processRRCRequestLayout.setCaption("RRC Modification Details");
		processRRCRequestLayout.setMargin(true);
		
		Panel rrcReviewPanel = new Panel();
		rrcReviewPanel.setContent(processRRCRequestLayout);
		rrcReviewPanel.setCaption("RRC Modification Details");
		
		
		//setExtraEffortForEmployeeTable();
		
		return rrcReviewPanel;
		//this.bean.setRrcStatus(SHAConstants.RRC_STATUS_PROCESS);
		

		
	}
	
	public void addListenerForSavedAmount()
	{
		txtModifiedSavedAmount.addValueChangeListener(new ValueChangeListener() {			
			
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
		
		if(null != rrcRequestCategoryListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> rrcRequestCategoryList = this.bean.getRrcCategoryList();
			if(null != rrcRequestCategoryList && !rrcRequestCategoryList.isEmpty())
			{
				rrcRequestCategoryListenerTableForModify.setTableList(rrcRequestCategoryList);
				//this.bean.setRrcCategoryList(rrcRequestCategoryList);
			}
		}
		/*if(null != extraEffortEmployeeListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				extraEffortEmployeeListenerTableForModify.setTableList(extraEmployeeList);
			}
		}*/
		if(null != extraEffortEmployeeListenerTableForModify)
		{
			List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getEmployeeEffortList();
		//	List<ExtraEmployeeEffortDTO> extraEmployeeList = this.bean.getRrcDTO().getEmployeeEffortList();
			if(null != extraEmployeeList && !extraEmployeeList.isEmpty())
			{
				for (ExtraEmployeeEffortDTO extraEmployeeEffortDTO : extraEmployeeList) {
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
					 extraEffortEmployeeListenerTableForModify.addBeanToList(extraEmployeeEffortDTO);
				}
				
				
				
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
				Long sNo =1l;
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
		 cmbSignificantClinicalInformation.setContainerDataSource(significantClinicalInformation);
		 cmbSignificantClinicalInformation.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 cmbSignificantClinicalInformation.setItemCaptionPropertyId("value");
		 
		 for(int i = 0 ; i<significantClinicalInformation.size() ; i++)
		 	{
				if (this.bean.getSignificantClinicalInformationValue() != null && (this.bean.getSignificantClinicalInformationValue()).equalsIgnoreCase(significantClinicalInformation.getIdByIndex(i).getValue()))
				{
					this.cmbSignificantClinicalInformation.setValue(significantClinicalInformation.getIdByIndex(i));
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
	
/*	public void loadEmployeeMasterData(
			AutocompleteField<EmployeeMasterDTO> field,
			List<EmployeeMasterDTO> employeeDetailsList) {
		extraEffortEmployeeListenerTable.loadEmployeeMasterData(field , employeeDetailsList);		
	}*/
	
	public void addListener() {

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
		

		if(null != cmbModifiedEligiblity )
		{
			SelectValue selValue = (SelectValue)cmbModifiedEligiblity.getValue();
			if(!(null != selValue && null != selValue.getValue()))
			{
				hasError = true;
				eMsg.append("Please select Eligibility").append("</br>");
			}
		}
		
		if(!(null != txtModifiedSavedAmount && null != txtModifiedSavedAmount.getValue() ))
		{
			
				hasError = true;
				eMsg.append("Please enter saved amount").append("</br>");
			
		}
		
		if(null != this.extraEffortEmployeeListenerTableForModify)
		{
			Boolean isValid = extraEffortEmployeeListenerTableForModify.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.extraEffortEmployeeListenerTableForModify.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
				}
			}
		}
		
		if(null != this.rrcRequestCategoryListenerTableForModify)
		{
			Boolean isValid = rrcRequestCategoryListenerTableForModify.isValid();
			if (!isValid) {
				hasError = true;
				List<String> errors = this.rrcRequestCategoryListenerTableForModify.getErrors();
				for (String error : errors) {
					eMsg.append(error).append("</br>");
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
//			showOrHideValidation(false);
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
		 extraEffortEmployeeListenerTableForModify.invalidate();
		 rrcRequestCategoryListenerTable.invalidate();
		 rrcRequestCategoryListenerTableForModify.invalidate();
		 SHAUtils.setClearReferenceData(containerMap);
		 eligiblity = null;
		 significantClinicalInformation = null;
		 binder = null;
		 presenterString = null;
	 }

}
