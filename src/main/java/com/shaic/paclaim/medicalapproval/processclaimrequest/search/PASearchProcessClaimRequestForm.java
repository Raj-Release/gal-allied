/**
 * 
 */
package com.shaic.paclaim.medicalapproval.processclaimrequest.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestFormDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narenj
 *
 */
public class PASearchProcessClaimRequestForm extends SearchComponent<SearchProcessClaimRequestFormDTO> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5983258967423553948L;

	//@Inject
	//private PASearchProcessClaimRequestTable searchTable;
	
	@EJB
	private MasterService masterService;
	
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxhospitalType;
	private ComboBox cbxType;
	private ComboBox cbxIntimationSource;
	private ComboBox cbxNetworkHospType;
	private ComboBox cbxTreatmentType;
	private ComboBox cbxSpeciality;
	private ComboBox cmbCpuCode;
	private ComboBox cmbProductName;
	private ComboBox cmbPriority;
	private ComboBox cmbSource;
	private TextField txtClaimedAmountFrom;
	private TextField txtClaimedAmountTo;
	private ComboBox cmbRequestBy;
	
	private BeanItemContainer<SelectValue> networkHospitalType;
	private BeanItemContainer<SelectValue> Speicalityvalue ;
	
	private ComboBox cmbAccidentDeathDropDown;
	
	private SearchProcessClaimRequestFormDTO dto =  new SearchProcessClaimRequestFormDTO();

	private String screenName;
	
	Panel mainPanel = null;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(SearchProcessClaimRequestFormDTO dto, Boolean shouldDoSearch) {
		initBinder(dto);
		
		mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setContent(mainVerticalLayout());
		mainPanel.setHeight("230px");
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
	//	btnSearch.setStyleName("hover");
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		cbxhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
//		cbxType.setEnabled(false);
		
		cbxIntimationSource = binder.buildAndBind("Intimation Source","intimationSource",ComboBox.class);
		
		cbxNetworkHospType = binder.buildAndBind("Network Hosp Type","networkHospType",ComboBox.class);
		
		cbxTreatmentType = binder.buildAndBind("Treatment Type","treatementType",ComboBox.class);
		
		cbxSpeciality = binder.buildAndBind("Speciality","speciality",ComboBox.class);
		
		cmbCpuCode = binder.buildAndBind("CPU Code","cpuCode",ComboBox.class);
		
		cmbProductName = binder.buildAndBind("Product Name/Code","productName",ComboBox.class);
		
		cmbPriority = binder.buildAndBind("Priority","priority",ComboBox.class);
		
		cmbSource = binder.buildAndBind("Source","source",ComboBox.class);
		
		
		cmbAccidentDeathDropDown = (ComboBox)binder.buildAndBind("Accident/Death","accidentDeath",ComboBox.class);
		
		
		cmbRequestBy = binder.buildAndBind("Requested User","requestedBy",ComboBox.class);
		
		txtClaimedAmountFrom = binder.buildAndBind("Claimed Amount From", "claimedAmountFrom", TextField.class);
		txtClaimedAmountFrom.setNullRepresentation("");
		
		CSValidator validator = new CSValidator();
		validator.extend(txtClaimedAmountFrom);
		validator.setRegExp("^[0-9.]*$");
		validator.setPreventInvalidTyping(true);
		
		txtClaimedAmountTo = binder.buildAndBind("Claimed Amount To", "claimedAmountTo", TextField.class);
		txtClaimedAmountTo.setNullRepresentation("");
		
		CSValidator validator1 = new CSValidator();
		validator1.extend(txtClaimedAmountTo);
		validator1.setRegExp("^[0-9.]*$");
		validator1.setPreventInvalidTyping(true);
		
//		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo, txtPolicyNo, cbxhospitalType, cbxTreatmentType,cmbCpuCode,cmbPriority);
//		formLayoutLeft.setMargin(true);
//		FormLayout formLayoutReight = new FormLayout(cbxType, cbxIntimationSource, cbxNetworkHospType, cbxSpeciality,cmbProductName,cmbSource);	
//		formLayoutReight.setMargin(true);
//		
//		FormLayout formLayoutNext = new FormLayout(txtClaimedAmountFrom,txtClaimedAmountTo,cmbRequestBy);
//		formLayoutNext.setMargin(true);
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo,cmbCpuCode,cbxIntimationSource,cbxhospitalType);
		formLayout1.setMargin(false);
		FormLayout formLayout2 = new FormLayout(cmbSource,cmbRequestBy, cmbPriority,cmbProductName);	
		formLayout2.setMargin(false);
		FormLayout formLayout3 = new FormLayout(txtPolicyNo,cbxNetworkHospType,cbxTreatmentType,cbxSpeciality);
		formLayout3.setMargin(false);
		FormLayout formLayout4 = new FormLayout(txtClaimedAmountFrom,txtClaimedAmountTo,cmbAccidentDeathDropDown,cbxType);
		formLayout4.setMargin(false);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayout1,formLayout2,formLayout3,formLayout4);	
		
		cbxhospitalListener();	

		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");		
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 absoluteLayout_3.addComponent(fieldLayout);		
		absoluteLayout_3.addComponent(btnSearch, "top:150.0px;left:260.0px;");
		absoluteLayout_3.addComponent(btnReset, "top:150.0px;left:369.0px;");
		
		
		mainVerticalLayout.addComponent(absoluteLayout_3);
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("1500px");
		// mainVerticalLayout.setHeight("500px");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 
		 absoluteLayout_3.setHeight("180px");
		
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder(SearchProcessClaimRequestFormDTO dto)
	{
		if(dto != null) {
			this.dto = dto;
		} else {
			this.dto = new SearchProcessClaimRequestFormDTO();
		}
		
		this.binder = new BeanFieldGroup<SearchProcessClaimRequestFormDTO>(SearchProcessClaimRequestFormDTO.class);
		this.binder.setItemDataSource(this.dto);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType, BeanItemContainer<SelectValue> typeContainer, BeanItemContainer<SelectValue> productName1, BeanItemContainer<SelectValue> cpuCode, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage,String screenName) {
		
		String mainPanelName = "";
		if(null != screenName && (SHAConstants.PA_MEDICAL_SCREEN.equalsIgnoreCase(screenName))){
			mainPanelName = "Process Claim Request";
		}
		else if(null != screenName && (SHAConstants.PA_MEDICAL_WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName)))
		{
			mainPanelName = "Wait for Input - Non Hospitalisation";
		}
		mainPanel.setCaption(mainPanelName);
		
		cbxIntimationSource.setContainerDataSource(intimationSource);
		cbxIntimationSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxIntimationSource.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getIntimationSource() != null) {
			cbxIntimationSource.setValue(this.dto.getIntimationSource());
		}
		
		
		cbxhospitalType.setContainerDataSource(hospitalType);
		cbxhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxhospitalType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getHospitalType() != null) {
			cbxhospitalType.setValue(this.dto.getHospitalType());
		}
		
		
		cbxTreatmentType.setContainerDataSource(treatementType);
		cbxTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxTreatmentType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getTreatementType() != null) {
			cbxTreatmentType.setValue(this.dto.getTreatementType());
		}
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		BeanItemContainer<SpecialSelectValue> productName = masterService.getContainerForProductByLineOfBusiness(SHAConstants.PA_LOB);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		
		BeanItemContainer<SelectValue> employeeLoginNameContainer = masterService.getEmployeeLoginContainer(userName);
		
		cbxType.setContainerDataSource(selectValueForType);
		cbxType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxType.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getType() != null) {
			cbxType.setValue(this.dto.getType());
		}
		
		cmbCpuCode.setContainerDataSource(cpuCode);
		cmbCpuCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbCpuCode.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getCpuCode() != null) {
			cmbCpuCode.setValue(this.dto.getCpuCode());
		}
		
		cmbProductName.setContainerDataSource(productName);
		cmbProductName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductName.setItemCaptionPropertyId("specialValue");
		if(this.dto != null && this.dto.getProductName() != null) {
			cmbProductName.setValue(this.dto.getProductName());
		}
		
		cmbPriority.setContainerDataSource(selectValueForPriority);
		cmbPriority.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPriority.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getPriority() != null) {
			cmbPriority.setValue(this.dto.getPriority());
		}
		
		cmbSource.setContainerDataSource(statusByStage);
		cmbSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbSource.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getSource() != null) {
			cmbSource.setValue(this.dto.getSource());
		}
		
		cmbRequestBy.setContainerDataSource(employeeLoginNameContainer);
		cmbRequestBy.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRequestBy.setItemCaptionPropertyId("value");
		if(this.dto != null && this.dto.getRequestedBy() != null) {
			cmbRequestBy.setValue(this.dto.getRequestedBy());
		}
		
		SelectValue selectAccident = new SelectValue();
		selectAccident.setId(null);
		selectAccident.setValue(SHAConstants.ACCIDENT);
		
		SelectValue selectDeath = new SelectValue();
		selectDeath.setId(null);
		selectDeath.setValue(SHAConstants.DEATH);
		

		List<SelectValue> selectVallueList = new ArrayList<SelectValue>();		
		selectVallueList.add(selectAccident);
		selectVallueList.add(selectDeath);
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		selectValueContainer.addAll(selectVallueList);
		cmbAccidentDeathDropDown.setContainerDataSource(selectValueContainer);
		cmbAccidentDeathDropDown.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAccidentDeathDropDown.setItemCaptionPropertyId("value");
		
		employeeLoginNameContainer.sort(new Object[] {"value"}, new boolean[] {true});
		
		statusByStage.sort(new Object[] {"value"}, new boolean[] {true});

		this.networkHospitalType = networkHospitalType;
	}	
	private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 4709889307272142361L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				
				
				if(cbxhospitalType.getValue() != null){
					System.out.println("ggggggggggggggggggggggg"+cbxhospitalType.getValue());
					if(ReferenceTable.HOSPITAL_NETWORK.equals(cbxhospitalType.getValue().toString())){
						cbxNetworkHospType.setContainerDataSource(networkHospitalType);
						cbxNetworkHospType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxNetworkHospType.setItemCaptionPropertyId("value");
				}else{
					cbxNetworkHospType.setContainerDataSource(null);
				}
			}else{
				cbxNetworkHospType.setContainerDataSource(null);
				}
			}
			});
		
		cbxTreatmentType.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 3670990192000515195L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cbxTreatmentType.getValue() != null ){
					System.out.println("ggggggggggggggggggggggg"+cbxTreatmentType.getValue());
					if(ReferenceTable.MEDICAL.equals(cbxTreatmentType.getValue().toString())){
					
						fireViewEvent(PASearchProcessClaimRequestPresenter.SPECIALITY, ReferenceTable.MEDICAL_CODE );
						
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}else if(ReferenceTable.SURGICAL.equals(cbxTreatmentType.getValue().toString())){
					
						fireViewEvent(PASearchProcessClaimRequestPresenter.SPECIALITY,ReferenceTable.SURGICAL_CODE );
						
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}
					
				}else{
					cbxSpeciality.setContainerDataSource(null);
				}
			}
		});
		
	cmbSource.addValueChangeListener(new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cmbSource.getValue() != null ) {
					SelectValue value = (SelectValue) cmbSource.getValue();
					if(value != null && ReferenceTable.getReplyReceivedStatus().containsKey(value.getId())) {
						if(cmbRequestBy != null && !cmbRequestBy.getItemIds().isEmpty()) {
							cmbRequestBy.setValue(cmbRequestBy.getItemIds().toArray()[0]);
						}
					} else {
						cmbRequestBy.setValue(null);
					}
				}
			}
		});
	}

	public void setSpecialityCBX(
			BeanItemContainer<SelectValue> specialityValueByReference) {
		Speicalityvalue = specialityValueByReference;
		
	}
}