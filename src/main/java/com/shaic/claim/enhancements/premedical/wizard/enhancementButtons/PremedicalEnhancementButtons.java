package com.shaic.claim.enhancements.premedical.wizard.enhancementButtons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.enhancements.premedical.wizard.PremedicalEnhancementWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonListeners;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.shaic.claim.premedical.wizard.PreMedicalPreauthWizardPresenter;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
@Default
public class PremedicalEnhancementButtons extends PreauthButtonsUI implements PreauthButtonListeners{
	private static final long serialVersionUID = 5007742027153988029L;

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
				fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_QUERY_EVENT, null);
			}
		});
		
		suggestRejection.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_SUGGEST_REJECTION_EVENT, null);
			}
		});
		sendForProcessing.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean isStopProcess = SHAUtils.alertMessageForStopProcess(bean.getNewIntimationDTO().getPolicy().getPolicyNumber(),getUI());
				if(!isStopProcess){
					
					if(bean.isInsuredDeleted()){
						alertMessageForPED(SHAConstants.NON_DISCLOSEED_PED_INSURED_MESSAGE.replaceAll("X{4}", bean.getNewIntimationDTO().getInsuredPatient().getInsuredName()));
					}
					
					fireViewEvent(PremedicalEnhancementWizardPresenter.PREMEDICAL_SEND_FOR_PROCESSING_EVENT, null);
				}
			}
		});
		
		holdBtn.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				bean.getPreauthMedicalDecisionDetails().setUserClickAction("Hold");
				fireViewEvent(PremedicalEnhancementWizardPresenter.PRE_MEDICAL_ENHANCEMENT_SEND_FOR_HOLD_EVENT,null);
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
			errorMessages.add( "Please Select Query or Rejection or Send For Processing </br>");
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
		else {
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
	
	public List<String> getErrors()
	{
		return this.errorMessages;
	}
	
	public Boolean alertMessageForPED(String message) {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + message + "</b>",
				ContentMode.HTML);

		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setStyleName("borderLayout");
		layout.setSpacing(true);
		layout.setMargin(true);

		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);

		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();					
			}
		});
		return true;
	}
}
