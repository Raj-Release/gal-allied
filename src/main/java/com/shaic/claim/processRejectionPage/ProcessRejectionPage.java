package com.shaic.claim.processRejectionPage;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.TextBundle;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.claim.processrejection.search.SearchProcessRejectionTableDTO;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.newcode.wizard.dto.ProcessRejectionDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


public class ProcessRejectionPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1678011305032044637L;

	@Inject
	private ProcessRejectionDTO bean;
	
	private TextBundle textBundle;
	
	private BeanFieldGroup<ProcessRejectionDTO> binder;
	
	private TextField currencyName;
	
	private TextField claimedAmt;
	
	private TextField provisionAmt;
	
	private TextField claimNumber;
	
	private TextField registerRemarks;
	
	private TextArea remarks;
	
	private HorizontalLayout mainLayout;
	
	private FormLayout firstForm;
	
	private FormLayout secondForm;
	
	public static Boolean DESICION=false;
	
	@PostConstruct
	public void initView(){
		
	}
	
	public void setReferenceData(ProcessRejectionDTO bean,NewIntimationDto intimationDto){
		
		this.bean=bean;
		this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
		this.binder.setItemDataSource(this.bean);
		
	}
	
	public void initView(SearchProcessRejectionTableDTO searchDTO){
		
		if(searchDTO.getStatusKey() == null){
		fireViewEvent(ProcessRejectionPresenter.SET_DATA,searchDTO);
		}else{
			this.binder= new BeanFieldGroup<ProcessRejectionDTO>(ProcessRejectionDTO.class);
			this.binder.setItemDataSource(searchDTO.getProcessRejectionDTO());
		}
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		currencyName =(TextField) binder.buildAndBind("Currency Name","currencyName", TextField.class);
		claimedAmt =(TextField) binder.buildAndBind("Amount Claimed","claimedAmount", TextField.class);		
		provisionAmt =(TextField) binder.buildAndBind("Provision Amount","provisionAmt", TextField.class);		
		claimNumber =(TextField) binder.buildAndBind("Claim Number","claimNumber", TextField.class);	
		registerRemarks =(TextField) binder.buildAndBind("Registration Remarks","registerRemarks", TextField.class);
		
		firstForm=new FormLayout(currencyName,claimedAmt,provisionAmt,claimNumber,registerRemarks);
		
		remarks=(TextArea) binder.buildAndBind("Suggested Rejection Remarks", "rejectionRemarks", TextArea.class);
		remarks.setReadOnly(true);

		secondForm=new FormLayout(/*suggestion,*/remarks);
		
		setReadOnly(firstForm,true);
		mainLayout=new HorizontalLayout(firstForm,secondForm);
		
		Panel panel=new Panel();
		panel.setCaption("Registration Remarks");
		panel.addStyleName("girdBorder");
		panel.setContent(mainLayout);
		panel.setWidth("60%");
		
		setCompositionRoot(panel);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
			else if (c instanceof ComboBox) {
				ComboBox field = (ComboBox) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
			else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}
		

}
