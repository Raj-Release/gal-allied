package com.shaic.claim.omp.ratechange;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.claim.omp.searchregistration.OMPSearchClaimRegistrationFormDto;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;

@UIScoped
@SuppressWarnings("serial")
public class OMPClaimRateChangeAndOsUpdationUI  extends SearchComponent<OMPClaimRateChangeAndOsUpdationFormDto>{
	
private OMPClaimRateChangeAndOsUpdationFormDto	searchDto;
private OMPClaimRateChangeAndOsUpdationDetailTable	searchTable;

private BeanFieldGroup<OMPClaimRateChangeAndOsUpdationFormDto> binder;
private PopupDateField fromDateField;

@EJB
private MasterService masterService;

private OMPClaimRateChangeAndOsUpdationFormDto bean;

public OMPClaimRateChangeAndOsUpdationFormDto getSearchDTO() {
	bean =null;

//	try {				
//		if(this.binder.isValid()){
//		this.binder.commit();
//		
//		bean = this.binder.getItemDataSource()
//				.getBean();
//		}
//	} catch (CommitException e) {
//		e.printStackTrace();
//	}
	
	if(fromDateField.getValue() != null){
		bean = new OMPClaimRateChangeAndOsUpdationFormDto();
		bean.setIntimationDate(fromDateField.getValue());
	
	
	bean.setUserId(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
	bean.setPassword(UI.getCurrent().getSession().getAttribute(BPMClientContext.PASSWORD).toString());
	}/*else{
		String eMsg ="Please Enter Date Field";
		showErrorPopup(eMsg);
	}*/
	return bean;
}
@PostConstruct
public void init() {
initBinder();
	
	Panel mainPanel = new Panel();
	mainPanel.addStyleName("panelHeader");
	mainPanel.addStyleName("g-search-panel");
	mainPanel.setCaption("OMP CLAIMS RATE CHANGE AND OUTSTANDING UPDATION");
	mainPanel.setContent(mainVerticalLayout());
	setCompositionRoot(mainPanel);
}

private void initBinder()

{
	this.binder = new BeanFieldGroup<OMPClaimRateChangeAndOsUpdationFormDto>(OMPClaimRateChangeAndOsUpdationFormDto.class);
	this.binder.setItemDataSource(new OMPClaimRateChangeAndOsUpdationFormDto());
	this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
}
public VerticalLayout mainVerticalLayout(){

	btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
	btnSearch.setDisableOnClick(true);
	mainVerticalLayout = new VerticalLayout();
	

	fromDateField = binder.buildAndBind(" Date","intimationDate",PopupDateField.class);
	fromDateField.setDateFormat("dd/MM/yyyy");
	FormLayout formLayoutLeft = new FormLayout(fromDateField);
   

	HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft);
	fieldLayout.setMargin(true);
	fieldLayout.setWidth("100%");		
	 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
	 
	 absoluteLayout_3.addComponent(fieldLayout);		
	absoluteLayout_3.addComponent(btnSearch, "top:160.0px;left:220.0px;");
	absoluteLayout_3.addComponent(btnReset, "top:160.0px;left:329.0px;");
	
	
	mainVerticalLayout.addComponent(absoluteLayout_3);
	 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
	 mainVerticalLayout.setWidth("650px");
	 mainVerticalLayout.setMargin(false);		 
	 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
	 absoluteLayout_3.setWidth("100.0%");
	 
	 absoluteLayout_3.setHeight("224px");
	
	addListener();
//	setDropDownValues();
	return mainVerticalLayout;
}

public void setDropDownValues(){
	BeanItemContainer<SpecialSelectValue> productNameValue = masterService.getContainerForProduct();
}

public void setType(BeanItemContainer<SelectValue> parameter, BeanItemContainer<SelectValue> selectValueForPriority, BeanItemContainer<SelectValue> statusByStage, BeanItemContainer<SelectValue> selectValueForUploadedDocument) {
	/*	eventCodeType.setContainerDataSource(parameter);
		eventCodeType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		eventCodeType.setItemCaptionPropertyId("value");
		
		cmbProductCode.setContainerDataSource(selectValueForPriority);
		cmbProductCode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbProductCode.setItemCaptionPropertyId("value");
		
		BeanItemContainer<SelectValue> selectValueForType = SHAUtils.getSelectValueForType();
		
		*/
		
		
	}
private void showErrorPopup(String eMsg) {
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

}
