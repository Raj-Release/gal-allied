package com.shaic.paclaim.manageclaim.healthreopenclaim.pageRodLevel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.paclaim.manageclaim.healthreopenclaim.searchRodLevel.PAHealthSearchReOpenClaimRodLevelTableDTO;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
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
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthReOpenRodLevelClaimPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PAHealthReOpenRodLevelClaimTable reOpenClaimTable;
	
	private TextArea txtReOpenRemarks;
	
	private BeanFieldGroup<PAHealthReOpenRodLevelClaimDTO> binder;
	
	private PAHealthReOpenRodLevelClaimDTO bean;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private PAHealthSearchReOpenClaimRodLevelTableDTO searchDTO;
	
	private BeanItemContainer<SelectValue> container;
	
	@PostConstruct
	public void initView() {
		
	}
	
	
	public void init(PAHealthSearchReOpenClaimRodLevelTableDTO searchDTO){
		
		this.searchDTO = searchDTO;
		
		fireViewEvent(PAHealthReOpenRodLevelClaimPresenter.SET_DATA_FIELD, searchDTO);
		
		this.binder = new BeanFieldGroup<PAHealthReOpenRodLevelClaimDTO>(PAHealthReOpenRodLevelClaimDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		reOpenClaimTable.init("Document Details", false, false);
		
		fireViewEvent(PAHealthReOpenRodLevelClaimPresenter.SET_TABLE_DATA, searchDTO);
		
		txtReOpenRemarks =(TextArea) binder.buildAndBind("Re-Open Remarks","reOpenRemarks", TextArea.class);
		txtReOpenRemarks.setMaxLength(200);
		txtReOpenRemarks.setWidth("400px");

		FormLayout formLayout = new FormLayout(txtReOpenRemarks);
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		FormLayout dummyForm = new FormLayout();
		dummyForm.setWidth("50%");
		
		HorizontalLayout formHor = new HorizontalLayout(dummyForm,formLayout);
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainVertical = new VerticalLayout(reOpenClaimTable,formHor,buttonHor);
		mainVertical.setSpacing(true);
		
		mainVertical.setComponentAlignment(formHor, Alignment.BOTTOM_LEFT);
		mainVertical.setComponentAlignment(buttonHor,Alignment.BOTTOM_CENTER);
		
		mandatoryFields.add(txtReOpenRemarks);
		
		showOrHideValidation(false);
		
		addListener();
		
		setCompositionRoot(mainVertical);
		
	}
	
public void addListener(){
		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
				        "No", "Yes", new ConfirmDialog.Listener() {

				            public void onClose(ConfirmDialog dialog) {
				                if (!dialog.isConfirmed()) {
				                	fireViewEvent(MenuItemBean.PA_HOSP_RE_OPEN_CLAIM_ROD_LEVEL, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				dialog.setClosable(false);
			}
		});
		submitBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				if(validatePage()){
					fireViewEvent(PAHealthReOpenRodLevelClaimPresenter.SUBMIT_DATA, searchDTO,bean);
				}
				
			}
		});
		
	}


	
	public void setUpReference(PAHealthReOpenRodLevelClaimDTO bean, BeanItemContainer<SelectValue> reasonForReOpen){
		this.bean = bean;
		this.container = reasonForReOpen;

	}
	
	public boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";		
		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg += errMsg.getFormattedHtmlMessage();
				}
				hasError = true;
			}
		}
		
		if(reOpenClaimTable != null){
			List<ViewDocumentDetailsDTO> values = reOpenClaimTable.getValues();
			Boolean isValid = true;
			for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : values) {
				if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
					
					isValid = false;
					break;
				}
			}
			if(isValid){
				eMsg += "Please Select atlease one ROD for reOpen Claim";
				hasError = true;
			}
		}

		if (hasError) {
			setRequired(true);
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

			hasError = true;
			return !hasError;
		} else {
			
			try {
				this.binder.commit();
				
				if(reOpenClaimTable != null){
					List<ViewDocumentDetailsDTO> values = reOpenClaimTable.getValues();
					this.bean.setDocumentDetails(values);
				}
				
			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}
	
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


	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails) {
		
		reOpenClaimTable.setTableList(listDocumentDetails);
		
	}

}
