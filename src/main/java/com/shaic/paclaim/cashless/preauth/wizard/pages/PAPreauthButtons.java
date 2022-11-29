package com.shaic.paclaim.cashless.preauth.wizard.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonListeners;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.shaic.paclaim.cashless.preauth.wizard.wizardfiles.PAPreauthWizardPresenter;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class PAPreauthButtons extends PreauthButtonsUI implements PreauthButtonListeners {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7303039864650778083L;
	private HorizontalLayout buildButtonsHLayout;
	
	private List<String> errorMessages = new ArrayList<String>();  
	
	private PreauthDTO bean;
	
	Map<String, Object> referenceData;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean) {
		this.bean = bean;
		buildButtonsHLayout = buildButtons(bean);
		dynamicFieldsLayout = new VerticalLayout();
		addListener();
		
		
		setCompositionRoot(new VerticalLayout(buildButtonsHLayout, dynamicFieldsLayout));
	}
	
	protected void addListener(){
		query.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 2679764179795985945L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_QUERY_EVENT, null);
			}
		});
		
		suggestRejection.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_SUGGEST_REJECTION_EVENT, null);
			}
		});
		sendForProcessing.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreauthWizardPresenter.PREAUTH_SEND_FOR_PROCESSING_EVENT, null);
			}
		});
	}
	
	public void setReference(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		setReferenceData(referenceData);
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		errorMessages.removeAll(getErrors());
		showOrHideValidation(true);
		String eMsg = "";
		
		if(binder == null) {
			hasError = true;
			eMsg += eMsg + "Please Select Query or Rejection or Send For Processing";
			return !hasError;
		}
		
		
		if (this.binder != null && !this.binder.isValid()) {
		    
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 } 
		showOrHideValidation(false);
		return !hasError;
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
}
