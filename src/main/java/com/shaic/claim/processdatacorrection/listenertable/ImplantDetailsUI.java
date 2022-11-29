package com.shaic.claim.processdatacorrection.listenertable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.ImplantDetailsDTO;
import com.shaic.claim.premedical.listenerTables.ImplantTableListener;
import com.shaic.claim.processdatacorrection.dto.ImplantCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.claim.processdatacorrectionpriority.search.DataCorrectionPriorityPresenter;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class ImplantDetailsUI extends ViewComponent{

	private static final long serialVersionUID = 1L;
	
	private TextField txtimplantApplicable;

	private OptionGroup implantApplicable;

	private VerticalLayout implantTableVLayout;

	private HorizontalLayout implantHLayout;

	private FormLayout implantFLayout;

	private VerticalLayout wholeVerticalLayout;
	
	private List<ImplantCorrectionDTO> implantCorrectionDTOs;
	
	private Boolean implantAppli;
	
	@Inject
	private Instance<ImplantCorrectionTabel> implantCorrectionInstance;

	private ImplantCorrectionTabel implantCorrectionObj;
	
	@Inject
	private Instance<ActualImplantCorrectionTabel> actualimplantCorrectionInstance;

	private ActualImplantCorrectionTabel actualimplantCorrectionObj;
	
	private String presenterString;

	@PostConstruct
	public void init() {

	}

	public void initView(Boolean implantAppli,List<ImplantCorrectionDTO> implantCorrectionDTOs,String presenterString) {

		this.implantCorrectionDTOs = implantCorrectionDTOs;
		this.implantAppli = implantAppli;
		this.presenterString= presenterString;
		wholeVerticalLayout = new VerticalLayout(buildImplantLayout());
		implantTableVLayout = new VerticalLayout();
		implantTableVLayout.setSpacing(true);
		wholeVerticalLayout.addComponent(implantTableVLayout);
		if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
			fireViewEvent(DataCorrectionPresenter.ACTUAL_IMPLANT_APPLICABLE_CHANGED, true);
		}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
			fireViewEvent(DataCorrectionPriorityPresenter.ACTUAL_IMPLANT_APPLICABLE_CHANGED_PRIORITY, true);
		}
		setCompositionRoot(wholeVerticalLayout);

	}
	
	@SuppressWarnings("deprecation")
	private VerticalLayout buildImplantLayout(){	
		
		txtimplantApplicable = new TextField("Implant Applicable");
    	txtimplantApplicable.setWidth("300px");
    	txtimplantApplicable.setNullRepresentation("-");
    	if(implantAppli){
    		txtimplantApplicable.setValue("Yes");
    		
    	}else{
    		txtimplantApplicable.setValue("No");
    	}
    	txtimplantApplicable.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
    	txtimplantApplicable.setReadOnly(true);
    	
    	
		
		implantApplicable = new OptionGroup("Implant Applicable");
		implantApplicable.addItems(getReadioButtonOptions());
		implantApplicable.setItemCaption(true, "Yes");
		implantApplicable.setItemCaption(false, "No");
		implantApplicable.setStyleName("horizontal");
		implantApplicable.setValue(true);
		implantApplicable.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Boolean isChecked = false;
				Boolean isChangesneed = true;
				if(event.getProperty() != null && event.getProperty().getValue() != null && event.getProperty().getValue().toString() == "true") {
					isChecked = true;
				}
				if(event.getProperty() != null && event.getProperty().getValue() != null) {
					if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION)){
						fireViewEvent(DataCorrectionPresenter.ACTUAL_IMPLANT_APPLICABLE_CHANGED, isChecked);
					}else if(presenterString.equalsIgnoreCase(SHAConstants.DATA_VALIDATION_PRIORITY)){
						fireViewEvent(DataCorrectionPriorityPresenter.ACTUAL_IMPLANT_APPLICABLE_CHANGED_PRIORITY, isChecked);
					}
				}
			}
		});
		
		FormLayout implantDetails = new FormLayout(txtimplantApplicable);
		FormLayout implantFLayout = new FormLayout(implantApplicable);
		implantDetails.setMargin(true);
		implantFLayout.setMargin(true);
		
		if(implantAppli){
			implantCorrectionObj = implantCorrectionInstance.get();
			implantCorrectionObj.init(presenterString);
			implantCorrectionObj.setWidth("100.0%");
			if(implantCorrectionDTOs !=null && !implantCorrectionDTOs.isEmpty()){
				implantCorrectionObj.addBeansToList(implantCorrectionDTOs);
			}
			return new VerticalLayout(implantDetails,implantCorrectionObj,implantFLayout);
		}else{
			return new VerticalLayout(implantDetails,implantFLayout);
		}
			
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		
		return coordinatorValues;
	}
	
	public void generateFieldsBasedOnImplantApplicable(Boolean ischecked) {
		
		if (ischecked) {
			if (implantTableVLayout != null
					&& implantTableVLayout.getComponentCount() > 0) {
				implantTableVLayout.removeAllComponents();
			}
			this.actualimplantCorrectionObj = null;	
			
			ActualImplantCorrectionTabel actualImplant = actualimplantCorrectionInstance.get();
			actualImplant.init(presenterString);
			this.actualimplantCorrectionObj = actualImplant;
			implantTableVLayout.addComponent(actualimplantCorrectionObj);
			implantTableVLayout.setVisible(true);				
			
		} else {
			if (implantTableVLayout != null
					&& implantTableVLayout.getComponentCount() > 0) {
				implantTableVLayout.removeAllComponents();
			}
			implantTableVLayout.setVisible(false);
			this.actualimplantCorrectionObj = null;
		}
	}
	
	public List<ImplantCorrectionDTO> getvalue(){
		
		if(implantApplicable !=null 
				&& implantApplicable.getValue()!= null && (Boolean)implantApplicable.getValue()){
			if(actualimplantCorrectionObj !=null
					&& actualimplantCorrectionObj.getValues() !=null){
				return actualimplantCorrectionObj.getValues();
			}
		}
		return null;
	}
	
	public boolean isValid(){
		
		boolean hasError = false;
		Boolean imp = (Boolean)implantApplicable.getValue();
		if(actualimplantCorrectionObj !=null
				&& imp){
			return actualimplantCorrectionObj.isValid();
		}
		
		return !hasError;
	}
	
	public List<String> getErrors() {
		return actualimplantCorrectionObj.getErrors();
	}
	
	public void addImplantEdited(ImplantCorrectionDTO implantCorrectionDTO) {
		actualimplantCorrectionObj.addBeanToList(implantCorrectionDTO);
		
	}
	
	public void deleteactualImplant(Long key) {
		implantCorrectionObj.removeImplantEdited(key);
		
	}

	public Boolean getImplantApplicable(){
		if(implantApplicable !=null 
				&& implantApplicable.getValue()!= null ){
			Boolean imp = (Boolean)implantApplicable.getValue();
			return imp;
		}
		return false;
	}

	public List<ImplantCorrectionDTO> getDeletedvalue(){

		if(txtimplantApplicable.getValue() !=null && txtimplantApplicable.getValue().equals("Yes")
				&& implantApplicable.getValue()!= null && !(Boolean)implantApplicable.getValue()){
			if(implantCorrectionObj !=null
					&& implantCorrectionObj.getValues() !=null){
				return implantCorrectionObj.getValues();
			}
		}
		return null;
	}
	
	public void clearObject(){
		
		if(this.implantCorrectionObj != null){
			this.implantCorrectionObj.clearObject();
		}
		this.implantCorrectionObj = null;
		if(this.actualimplantCorrectionObj != null){
			this.actualimplantCorrectionObj.clearObject();
		}
		this.actualimplantCorrectionObj = null;
		presenterString = null;
		if(wholeVerticalLayout != null){
			wholeVerticalLayout.removeAllComponents();
		}
		wholeVerticalLayout = null;
	}
	
}
