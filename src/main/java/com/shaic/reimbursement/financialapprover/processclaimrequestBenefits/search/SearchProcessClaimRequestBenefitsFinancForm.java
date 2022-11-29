/**
 * 
 */
package com.shaic.reimbursement.financialapprover.processclaimrequestBenefits.search;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.reimbursement.medicalapproval.processclaimrequest.search.SearchProcessClaimRequestPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
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
public class SearchProcessClaimRequestBenefitsFinancForm extends SearchComponent<SearchProcessClaimRequestBenefitsFinancFormDTO> {
	
	@Inject
	private SearchProcessClaimRequestBenefitsFinancTable searchTable;
	
	@Inject
	private SearchProcessClaimRequestBenefitsFinancService service;
	
	private TextField txtIntimationNo;
	private TextField txtPolicyNo;
	private ComboBox cbxhospitalType;
	private ComboBox cbxType;
	private ComboBox cbxIntimationSource;
	private ComboBox cbxNetworkHospType;
	private ComboBox cbxTreatmentType;
	private ComboBox cbxSpeciality;
	private BeanItemContainer<SelectValue> networkHospitalType;
	private BeanItemContainer<SelectValue> Speicalityvalue ;
	
	@PostConstruct
	public void init() {
		initBinder();
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Process Claim Request");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	public VerticalLayout mainVerticalLayout(){
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		mainVerticalLayout = new VerticalLayout();
		
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		
		txtPolicyNo = binder.buildAndBind("Policy Number","policyNo",TextField.class);
		
		cbxhospitalType = binder.buildAndBind("Hospital Type","hospitalType",ComboBox.class);
		
		cbxType = binder.buildAndBind("Type","type",ComboBox.class);
		
		cbxIntimationSource = binder.buildAndBind("Intimation Source","intimationSource",ComboBox.class);
		
		cbxNetworkHospType = binder.buildAndBind("Network Hosp Type","networkHospType",ComboBox.class);
		
		cbxTreatmentType = binder.buildAndBind("Treatment Type","treatementType",ComboBox.class);
		
		cbxSpeciality = binder.buildAndBind("Speciality","speciality",ComboBox.class);
		
		FormLayout formLayoutLeft = new FormLayout(txtIntimationNo, txtPolicyNo, cbxhospitalType, cbxTreatmentType);
		FormLayout formLayoutReight = new FormLayout(cbxType, cbxIntimationSource, cbxNetworkHospType, cbxSpeciality);	
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutReight);
		HorizontalLayout buttonlayout = new HorizontalLayout(btnSearch,btnReset);
		buttonlayout.setSpacing(true);
		cbxhospitalListener();
		mainVerticalLayout.addComponent(fieldLayout);
		mainVerticalLayout.setComponentAlignment(fieldLayout, Alignment.BOTTOM_CENTER);
		mainVerticalLayout.addComponent(buttonlayout);
		mainVerticalLayout.setComponentAlignment(buttonlayout, Alignment.MIDDLE_CENTER);
		mainVerticalLayout.setWidth("70%");
		mainVerticalLayout.setHeight("250px");
		mainVerticalLayout.setMargin(true);
		mainVerticalLayout.setSpacing(true);
		addListener();
		
		return mainVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchProcessClaimRequestBenefitsFinancFormDTO>(SearchProcessClaimRequestBenefitsFinancFormDTO.class);
		this.binder.setItemDataSource(new SearchProcessClaimRequestBenefitsFinancFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public void setCBXValue(BeanItemContainer<SelectValue> intimationSource,
			BeanItemContainer<SelectValue> hospitalType,
			BeanItemContainer<SelectValue> networkHospitalType,
			BeanItemContainer<SelectValue> treatementType) {
		cbxIntimationSource.setContainerDataSource(intimationSource);
		cbxIntimationSource.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxIntimationSource.setItemCaptionPropertyId("value");
		
		
		cbxhospitalType.setContainerDataSource(hospitalType);
		cbxhospitalType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxhospitalType.setItemCaptionPropertyId("value");
		
		cbxTreatmentType.setContainerDataSource(treatementType);
		cbxTreatmentType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cbxTreatmentType.setItemCaptionPropertyId("value");
		
		this.networkHospitalType = networkHospitalType;
	}	
	private void cbxhospitalListener(){
		cbxhospitalType.addValueChangeListener(new ValueChangeListener() {
			
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
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
			
				if(cbxTreatmentType.getValue() != null ){
					System.out.println("ggggggggggggggggggggggg"+cbxTreatmentType.getValue());
					if(ReferenceTable.MEDICAL.equals(cbxTreatmentType.getValue().toString())){
						fireViewEvent(SearchProcessClaimRequestPresenter.SPECIALITY, ReferenceTable.MEDICAL_CODE );
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}else if(ReferenceTable.SURGICAL.equals(cbxTreatmentType.getValue().toString())){
						fireViewEvent(SearchProcessClaimRequestPresenter.SPECIALITY, ReferenceTable.SURGICAL_CODE );
						cbxSpeciality.setContainerDataSource(Speicalityvalue);
						cbxSpeciality.setItemCaptionMode(ItemCaptionMode.PROPERTY);
						cbxSpeciality.setItemCaptionPropertyId("value");
					}
					
				}else{
					cbxSpeciality.setContainerDataSource(null);
				}
			}
		});
	}
	
	public void setSpecialityCBX(
			BeanItemContainer<SelectValue> specialityValueByReference) {
		Speicalityvalue = specialityValueByReference;
		
	}
	
}