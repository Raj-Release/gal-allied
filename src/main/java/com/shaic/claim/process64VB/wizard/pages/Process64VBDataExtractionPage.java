package com.shaic.claim.process64VB.wizard.pages;

import java.util.ArrayList;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.preauth.wizard.dto.SearchPreauthTableDTO;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class Process64VBDataExtractionPage extends ViewComponent implements WizardStep<SearchPreauthTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2853115360263147072L;
	private SearchPreauthTableDTO bean;
	private TextField txtRequested;
	private TextField txttype;
	private TextArea txtRemarks;
	
	private BeanFieldGroup<SearchPreauthTableDTO> binder;
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	private TextArea txtpaymentRemark;
	private TextField txtPaymentStatus;
	private Button btnApprove;
	private Button btnDisapprove;
	private String paymentStatus = null;
	
	@Override
	public void init(SearchPreauthTableDTO bean) {
		String paymentStatus =null;
		this.bean = bean;
		
		initBinder();
		VerticalLayout layout = new VerticalLayout();
		
		txtRequested = (TextField) binder.buildAndBind("Requested By","processVBrequestedBy", TextField.class);
		txttype = (TextField) binder.buildAndBind("Type","processVBtype", TextField.class);
		txtRemarks = (TextArea) binder.buildAndBind("Remarks for Referring 64 VB Complaince","processVBRemarksComplaince", TextArea.class);
		
		txtRequested.setNullRepresentation("");
		txttype.setNullRepresentation("");
		txtRemarks.setNullRepresentation("");
		
		txtPaymentStatus = (TextField) binder.buildAndBind("Payment Status","processVBpayment", TextField.class);
		//txtpaymentRemark = (TextArea) binder.buildAndBind("Remarks","processVBremarks", TextArea.class);
		
		txtPaymentStatus.setNullRepresentation("");
		mandatoryFields.add(txtPaymentStatus);
		FormLayout formLayoutLeft = new FormLayout(txtRequested,txttype,txtPaymentStatus);
		formLayoutLeft.setMargin(Boolean.TRUE);
		formLayoutLeft.setSpacing(Boolean.TRUE);
		formLayoutLeft.setCaption("");
		
		FormLayout formLayoutRight = new FormLayout(txtRemarks);
		formLayoutRight.setMargin(Boolean.TRUE);
		formLayoutRight.setSpacing(Boolean.TRUE);
		formLayoutRight.setCaption("");
		
		txtRequested.setReadOnly(Boolean.TRUE);
		txttype.setReadOnly(Boolean.TRUE);
		txtRemarks.setReadOnly(Boolean.TRUE);
		txtPaymentStatus.setReadOnly(Boolean.TRUE);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);
		/*horizontalLayout.setMargin(Boolean.TRUE);
		horizontalLayout.setSpacing(Boolean.TRUE);
		horizontalLayout.setCaption("");*/
		
		
		
		
		//FormLayout formLayoutLeft1 = new FormLayout(txtPaymentStatus);
		/*
		formLayoutLeft1.setMargin(Boolean.TRUE);
		formLayoutLeft1.setSpacing(Boolean.TRUE);
		formLayoutLeft1.setCaption("");*/
		
		//HorizontalLayout horizontalLayout1 = new HorizontalLayout(formLayoutLeft1);
		HorizontalLayout formLayoutRight1 = new HorizontalLayout();
		
		btnApprove = new Button("Approve");
		btnApprove.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btnDisapprove = new Button("Disapprove");
		btnDisapprove.setStyleName(ValoTheme.BUTTON_PRIMARY);
		addListener();
		
		formLayoutRight1 = new HorizontalLayout(btnApprove, btnDisapprove);
		formLayoutRight1.setSpacing(true);
		formLayoutRight1.setHeight("5px");
		
		
		/*formLayoutRight1.setMargin(Boolean.TRUE);
		formLayoutRight1.setSpacing(Boolean.TRUE);
		formLayoutRight1.setCaption("");*/
		
		/*FormLayout formLayoutRight3 = new FormLayout(txtpaymentRemark);
		formLayoutRight3.setMargin(Boolean.TRUE);
		formLayoutRight3.setSpacing(Boolean.TRUE);
		formLayoutRight3.setCaption("");
		
		HorizontalLayout horizontalLayout2 = new HorizontalLayout(formLayoutRight3);*/
	/*	horizontalLayout2.setMargin(Boolean.TRUE);
		horizontalLayout2.setSpacing(Boolean.TRUE);
		horizontalLayout2.setCaption("");*/

		
		HorizontalLayout horizontalLayout3 = new HorizontalLayout(getSubmitButtonWithListener(new ConfirmDialog()) , getCancelButton(new ConfirmDialog()));
		horizontalLayout3.setMargin(Boolean.TRUE);
		horizontalLayout3.setSpacing(Boolean.TRUE);
		horizontalLayout3.setCaption("");
		
		layout.addComponent(horizontalLayout);
		layout.addComponent(formLayoutRight1);
		layout.setComponentAlignment(formLayoutRight1, Alignment.TOP_CENTER);
		//layout.addComponent(horizontalLayout2);
		layout.addComponent(horizontalLayout3);
		layout.setComponentAlignment(horizontalLayout3, Alignment.BOTTOM_CENTER);
		showOrHideValidation(false);
		setCompositionRoot(layout);
	}
	
	
	
	
	private Button getRemarkSubmitButtonWithListener(final ConfirmDialog dialog) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				String eMsg = "";
				if(txtpaymentRemark != null && txtpaymentRemark.getValue() != null && txtpaymentRemark.getValue().length() > 0) {
					bean.setProcessVBremarks(txtpaymentRemark.getValue());
					dialog.close();
				} else {
					eMsg = "Please Enter Remarks";
					showErrorPopup(eMsg);
				}
			}
		});
		
		return submitButton;
	}
	
	
	
	private Button getSubmitButtonWithListener(final ConfirmDialog dialog) {
		Button submitButton = new Button("Submit");
		submitButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				
					if(!isValid()){
						fireViewEvent(Process64WizardPresenter.PROCESS64_SUBMITTED_EVENT, bean,paymentStatus);
					}
					
					
				/*
				String eMsg = "";
				bean.setStatusKey(ReferenceTable.PREAUTH_REFER_TO_FLP_STATUS);
				bean.setStageKey(ReferenceTable.PREAUTH_STAGE);
				if(remarksfield != null && remarksfield.getValue() != null && remarksfield.getValue().length() > 0) {
					bean.getPreauthMedicalDecisionDetails().setReferToFLPremarks(remarksfield.getValue());
					bean.setIsReferTOFLP(true);
					bean.setDocFilePath(null);
					bean.setDocumentSource(null);
					dialog.close();
				} else {
					eMsg = "Please Enter Remarks";
					showErrorPopup(eMsg);
				}
			*/}
		});
		
		return submitButton;
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
	
	
	private Button getRemarkCancelButton(final ConfirmDialog dialog) {
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
			}
		});
		return cancelButton;
	}
	
	private Button getCancelButton(final ConfirmDialog dialog) {/*
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -5934419771562851393L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				releaseHumanTask();
				fireViewEvent(MenuItemBean.PROCESS_64_VB_COMPLIANCE,
						null);
			}
		});
		return cancelButton;
	*/
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
		cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											dialog.close();
											releaseHumanTask();
											fireViewEvent(MenuItemBean.PROCESS_64_VB_COMPLIANCE,
													null);
										} 
									}
								});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		return cancelButton;
	}
	
	public void buildRemarkLayout() {
		
		initBinder();
		unbindField(txtpaymentRemark);
		txtpaymentRemark = (TextArea) binder.buildAndBind("Remarks","processVBremarks", TextArea.class);
		txtpaymentRemark.setMaxLength(2000);
		txtpaymentRemark.setWidth("400px");
		txtpaymentRemark.setNullRepresentation("");
		txtpaymentRemark.setRequired(Boolean.TRUE);
				
		mandatoryFields= new ArrayList<Component>();
		mandatoryFields.add(txtpaymentRemark);
		showOrHideValidation(false);
		

		final ConfirmDialog dialog = new ConfirmDialog();
		Button submitButtonWithListener = getRemarkSubmitButtonWithListener(dialog);
		
		HorizontalLayout btnLayout = new HorizontalLayout(submitButtonWithListener, getRemarkCancelButton(dialog));
		btnLayout.setWidth("800px");
		btnLayout.setMargin(true);
		btnLayout.setSpacing(true);
		btnLayout.setComponentAlignment(submitButtonWithListener, Alignment.MIDDLE_CENTER);
		showOrHideValidation(false);
		VerticalLayout VLayout = new VerticalLayout(new FormLayout(txtpaymentRemark), btnLayout);
		VLayout.setWidth("800px");
		VLayout.setMargin(true);
		showInPopup(VLayout, dialog);
		
	}
	
	private void showInPopup(Layout layout, ConfirmDialog dialog) {
		dialog.setCaption("");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setHeight("300px");
		
//		submitButton.addClickListener(new ClickListener() {
//			private static final long serialVersionUID = -5934419771562851393L;
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				dialog.close();
//			}
//		});
		
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	private void unbindField(Field<?> field) {
		if (field != null)
		{
			Object propertyId = this.binder.getPropertyId(field);
			if(propertyId != null) {
				this.binder.unbind(field);	
			}
			
		}
	}
	 private void releaseHumanTask(){
			
			Integer existingTaskNumber= (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
	     	String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
	 		String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
	 		Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);

	 		/*if(existingTaskNumber != null){
	 			BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, existingTaskNumber, SHAConstants.SYS_RELEASE);
	 			//getSession().setAttribute(SHAConstants.TOKEN_ID, null);
	 		}*/
	 		
	 		if(wrkFlowKey != null){
	 			DBCalculationService dbService = new DBCalculationService();
	 			dbService.callUnlockProcedure(wrkFlowKey);
	 			getSession().setAttribute(SHAConstants.WK_KEY, null);
	 		}
		}
	
	public void addListener() {
		
		btnApprove.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				paymentStatus = "A";
				buildRemarkLayout();
			}
	});
	
		btnDisapprove.addClickListener(new Button.ClickListener() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
				paymentStatus = "D";
				buildRemarkLayout();
			}
	});
	}
	public void initBinder() {
		this.binder = new BeanFieldGroup<SearchPreauthTableDTO>(
				SearchPreauthTableDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	@Override
	public Component getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAdvance() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
private boolean isValid() {
		

		// TODO Auto-generated method stub
		Boolean hasError = false;
		showOrHideValidation(true);
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			bean.setUsername(userName);
			
			bean.setProcessVBStatus(paymentStatus);
			
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
			
			if(paymentStatus==null){
				eMsg.append("Please Select Approve or Disapprove");
				hasError = true;
				
			}
			
			if(!hasError){
				binder.commit();
			}else{

				setRequired(true);
				Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

				//hasError = true;
				return hasError;
			
			}
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	
		
		
		/*
	
	
	
	
		// TODO Auto-generated method stub
		try {
			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			advocateFeeDTO.setUserName(userName);
			advocateBinder.commit();
			intimationBinder.commit();
			caseDetailBinder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	*/}

		@SuppressWarnings("unused")
		private void setRequired(Boolean isRequired) {
		
			if (!mandatoryFields.isEmpty()) {
				for (int i = 0; i < mandatoryFields.size(); i++) {
					AbstractField<?> field = (AbstractField<?>) mandatoryFields
							.get(i);
					field.setRequired(isRequired);
				}
			}
		}
		
		protected void showOrHideValidation(Boolean isVisible) {
			for (Component component : mandatoryFields) {
				AbstractField<?> field = (AbstractField<?>) component;
				field.setRequired(!isVisible);
				field.setValidationVisible(isVisible);
			}
}

}
