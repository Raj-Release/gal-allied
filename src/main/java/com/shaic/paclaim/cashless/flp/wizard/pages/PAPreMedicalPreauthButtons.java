package com.shaic.paclaim.cashless.flp.wizard.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;

import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonListeners;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.shaic.paclaim.cashless.flp.wizard.wizardFiles.PAPreMedicalPreauthWizardPresenter;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class PAPreMedicalPreauthButtons extends PreauthButtonsUI implements PreauthButtonListeners {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7303039864650778083L;
	private HorizontalLayout buildButtonsHLayout;
	
	private List<String> errorMessages;  
	
	private PreauthDTO bean;
	
	Map<String, Object> referenceData;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean) {
		this.bean = bean;
		errorMessages = new ArrayList<String>();
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
				fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_QUERY_EVENT, null);
			}
		});
		
		suggestRejection.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_SUGGEST_REJECTION_EVENT, null);
			}
		});
		sendForProcessing.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PAPreMedicalPreauthWizardPresenter.PREMEDICAL_SEND_FOR_PROCESSING_EVENT, null);
			}
		});
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		String eMsg = "";
		
		if(this.binder == null) {
			hasError = true;
			errorMessages.add("Query or Suggest for Rejection or Send For Processing to be entered. </br>");
			return !hasError;
		}
		
		if (!this.binder.isValid()) {
		    
		    for (Field<?> field : this.binder.getFields()) {
		    	ErrorMessage errMsg = ((AbstractField<?>)field).getErrorMessage();
		    	if (errMsg != null) {
		    		errorMessages.add(errMsg.getFormattedHtmlMessage());
		    	}
		    	hasError = true;
		    }
		 }  else {
			 try {
				this.binder.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		showOrHideValidation(false);
		return !hasError;
	}
	
	public void setReference(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
		setReferenceData(referenceData);
	}
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
}
