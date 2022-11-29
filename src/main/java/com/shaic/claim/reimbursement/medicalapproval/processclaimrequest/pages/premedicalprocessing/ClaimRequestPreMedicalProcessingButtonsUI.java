package com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.premedicalprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.claim.preauth.dto.DiagnosisProcedureTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonListeners;
import com.shaic.claim.preauth.wizard.pages.PreauthButtonsUI;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Field;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
@Alternative
public class ClaimRequestPreMedicalProcessingButtonsUI extends PreauthButtonsUI implements PreauthButtonListeners {
	private static final long serialVersionUID = 4814413087097394190L;

	
	private HorizontalLayout buildButtonsHLayout;
	//private PreauthDTO bean;
	
	private Button submitButton;
	
	Map<String, Object> referenceData;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void initView(PreauthDTO bean) {
		//this.bean = bean;
		errorMessages = new ArrayList<String>();
		submitButton = new Button("Submit");
		buildButtonsHLayout = buildZonalReviewButtons(bean, submitButton);
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
				fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_QUERY_EVENT, null);
			}
		});
		
		suggestRejection.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -1545640032342015257L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_REJECTION_EVENT, null);
			}
		});
		sendForProcessing.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1274221814969702338L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ClaimRequestPremedicalProcessingPagePresenter.ZONAL_REVIEW_SUGGEST_APPROVAL_EVENT, null);
			}
		});
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1076409627764028279L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean hasError = false;
				StringBuffer eMsg = new StringBuffer();
				if(!isValid()) {
					hasError = true;
					List<String> errors = getErrors();
					for (String error : errors) {
						eMsg.append(error).append("</br>");
					}
				}
				
				if(hasError) {
					Label label = new Label(eMsg.toString(), ContentMode.HTML);
				    label.setStyleName("errMessage");
				    VerticalLayout layout = new VerticalLayout();
				    layout.setMargin(true);
				    layout.addComponent(label);
				    
				    ConfirmDialog dialog = new ConfirmDialog();
				    dialog.setCaption("Alert");
				    dialog.setClosable(true);
				    dialog.setContent(layout);
				    dialog.setResizable(false);
				    dialog.setModal(true);
				    dialog.show(getUI().getCurrent(), null, true);
				}
			}
		});
	}
	
	public boolean isValid()
	{
		boolean hasError = false;
		showOrHideValidation(true);
		errorMessages.removeAll(getErrors());
		//StringBuffer eMsg = new StringBuffer();
		
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
	
	
	public void suggestApprovalClick(DiagnosisProcedureTableDTO dto,
			Map<String, Object> medicalDecisionTableValues) {
		setAppropriateValuesToDTOFromProcedure(dto, medicalDecisionTableValues);
		
	}
	
}
