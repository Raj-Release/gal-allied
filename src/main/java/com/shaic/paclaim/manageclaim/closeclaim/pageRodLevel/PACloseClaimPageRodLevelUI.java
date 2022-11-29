package com.shaic.paclaim.manageclaim.closeclaim.pageRodLevel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.IntimationDetailsCarousel;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.manageclaim.closeclaim.searchRodLevel.PASearchCloseClaimTableDTORODLevel;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class PACloseClaimPageRodLevelUI extends ViewComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PACloseClaimDocumentDetailsTableRodLevel documentDetailsTable;
	
	@Inject
	private Instance<IntimationDetailsCarousel> commonCarouselInstance;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private ComboBox cmbReasonForClosing;
	
	private TextArea txtReasonRemarks;
	
	private BeanFieldGroup<ViewDocumentDetailsDTO> binder;
	
	private ViewDocumentDetailsDTO bean;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private NewIntimationDto intimationDto;
	
	private PASearchCloseClaimTableDTORODLevel searchDTO;
	
	@PostConstruct
	public void initView() {
	}

	
	public void init(PASearchCloseClaimTableDTORODLevel searchDTO){
		
		this.bean = new ViewDocumentDetailsDTO();
		this.searchDTO = searchDTO;
		
		this.binder = new BeanFieldGroup<ViewDocumentDetailsDTO>(ViewDocumentDetailsDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		this.intimationDto = searchDTO.getIntimationDto();
		
		IntimationDetailsCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.intimationDto,"Convert Claim type to Reimbursement");
		
		documentDetailsTable.init("Document Details", false, false);
		
		fireViewEvent(PACloseClaimPageRodLevelPresenter.SET_TABLE_DATA, searchDTO);
		
		cmbReasonForClosing =(ComboBox) binder.buildAndBind("Reason for Closing","closingReason", ComboBox.class);
		cmbReasonForClosing.setRequired(true);
		cmbReasonForClosing.setNullSelectionAllowed(false);
		
		txtReasonRemarks = (TextArea) binder.buildAndBind("Remarks for Closing","closingRemarks", TextArea.class);
		txtReasonRemarks.setMaxLength(200);
		txtReasonRemarks.setWidth("400px");
		
		fireViewEvent(PACloseClaimPageRodLevelPresenter.SET_DATA_FIELD, searchDTO);

		FormLayout mainForm = new FormLayout(cmbReasonForClosing,txtReasonRemarks);
		mainForm.setSpacing(true);
		
		HorizontalLayout horLayout = new HorizontalLayout(mainForm);
		horLayout.setComponentAlignment(mainForm, Alignment.MIDDLE_CENTER);
		
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout buttonHor = new HorizontalLayout(submitBtn,cancelBtn);
		buttonHor.setSpacing(true);
		
		VerticalLayout mainHor = new VerticalLayout(documentDetailsTable,horLayout,buttonHor);
		mainHor.setComponentAlignment(buttonHor, Alignment.BOTTOM_CENTER);
		mainHor.setComponentAlignment(horLayout, Alignment.MIDDLE_CENTER);
		mainHor.setSpacing(true);
		mainHor.setMargin(true);
		
		mandatoryFields.add(cmbReasonForClosing);
		mandatoryFields.add(txtReasonRemarks);
		showOrHideValidation(false);
		
		addListener();
		
		setCompositionRoot(mainHor);
	
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
				                	fireViewEvent(MenuItemBean.CLOSE_CLAIM_ROD_LEVEL, true);
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
					fireViewEvent(PACloseClaimPageRodLevelPresenter.SUBMIT_DATA, searchDTO,bean);
				}
				
			}
		});
		
	}

	public void setUpReference(BeanItemContainer<SelectValue> reasonForClosing){
		
		cmbReasonForClosing.setContainerDataSource(reasonForClosing);
		cmbReasonForClosing.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReasonForClosing.setItemCaptionPropertyId("value");
	}


	public void setTableList(List<ViewDocumentDetailsDTO> listDocumentDetails) {
		documentDetailsTable.setTableList(listDocumentDetails);
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
		
		if(documentDetailsTable != null){
			List<ViewDocumentDetailsDTO> values = documentDetailsTable.getValues();
			Boolean isValid = true;
			for (ViewDocumentDetailsDTO viewDocumentDetailsDTO : values) {
				if(viewDocumentDetailsDTO.getCloseClaimStatus() != null && viewDocumentDetailsDTO.getCloseClaimStatus()){
					
					isValid = false;
					break;
				}
			}
			if(isValid){
				eMsg += "Please Select atlease one ROD for Closing Claim";
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
				
				if(documentDetailsTable != null){
					List<ViewDocumentDetailsDTO> values = documentDetailsTable.getValues();
					this.bean.setDocumentDetailsList(values);
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
	
	

}
