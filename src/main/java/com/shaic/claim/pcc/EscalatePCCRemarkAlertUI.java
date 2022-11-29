package com.shaic.claim.pcc;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.enhancements.preauth.wizard.PreauthEnhancemetWizardPresenter;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.preauth.PreauthWizardPresenter;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.processdatacorrection.search.SearchProcessDataCorrectionPresenter;
import com.shaic.claim.processrejection.search.SearchProcessRejectionFormDTO;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;

public class EscalatePCCRemarkAlertUI extends ViewComponent{


	private static final long serialVersionUID = 1L;

	private Button btnSubmit;

	private Button btnCancel;

	private VerticalLayout wholeVerticalLayout;

	private FormLayout catFLayout;
	
	private FormLayout generatedLayout;
	
	private FormLayout remarkLayout;

	private HorizontalLayout viewHLayout;
	
	private VerticalLayout verticalLayout;
	
	private Window popupWindow;	
	
	private BeanFieldGroup<PccDetailsTableDTO> binder;
	
	private ComboBox pccCategory;
	
	private ComboBox pccSubCategory;
	
	private ComboBox pccSubCategoryTwo;
	
	private TextArea escalatePCCRemarks;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private BeanItemContainer<SelectValue> pccSubCatContainer;
	
	private PreauthDTO bean;
	
	private String screenName;
	
	@EJB
	private PreauthService preauthService;
	
	public void setPopupWindow(Window popupWindow) {
		this.popupWindow = popupWindow;
	}

	public Window getPopupWindow() {
		return popupWindow;
	}

	@PostConstruct
	public void init() {

	}

	public void initView(PreauthDTO bean,String screenName) {
		this.bean = bean;
		this.screenName = screenName;
		initBinder();
		wholeVerticalLayout = new VerticalLayout(buildLayout());
		setCompositionRoot(wholeVerticalLayout);

	}

	private void addListener() {
		
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(validatePage()){			
					PccDetailsTableDTO pccDetailsTableDTO = binder.getItemDataSource().getBean();
					if(pccDetailsTableDTO.getPccSubCategory1() == null){
						pccDetailsTableDTO.setPccSubCategory1((SelectValue)pccSubCategory.getValue());
					}
					if(pccDetailsTableDTO.getEscalatePccRemarks() == null && escalatePCCRemarks !=null
							&& escalatePCCRemarks.getValue() !=null){
						pccDetailsTableDTO.setEscalatePccRemarks(escalatePCCRemarks.getValue());
					}
					if(pccDetailsTableDTO !=null){
						preauthService.savePCCRequest(bean, pccDetailsTableDTO,screenName);
						SHAUtils.showMessageBox("PCC Initiated Sucessfully", "Information - Escalate PCC ");
						getPopupWindow().close();
					}	
				}
			}
		});	

		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {	
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog dialog) {
								if (!dialog.isConfirmed()) {
									getPopupWindow().close();
								} else {
									// User did not confirm
								}
							}
						});
				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		
		pccCategory.addListener(new Listener(){
			@Override
			public void componentEvent(Event event) {
				if (pccCategory.getValue() !=null) {
					SelectValue selectValue = (SelectValue)pccCategory.getValue();
					if(selectValue.getId() !=null){
						if(screenName !=null && screenName.equals("Process Pre-Auth")){
							fireViewEvent(PreauthWizardPresenter.PREAUTH_PCC_SUB_CAT, selectValue.getId());
						}else if(screenName !=null && screenName.equals("Process Enhancements")){
							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_PCC_SUB_CAT, selectValue.getId());
						}
					}
				}
			}			
		});

		pccSubCategory.addListener(new Listener(){
			@Override
			public void componentEvent(Event event) {
				if (pccSubCategory.getValue() !=null) {
					SelectValue selectValue = (SelectValue)pccSubCategory.getValue();
					if(selectValue.getId() !=null &&
							ReferenceTable.getPCCRemarkNonMandList().contains(selectValue.getId())){
						if(mandatoryFields.contains(escalatePCCRemarks)){
							mandatoryFields.remove(escalatePCCRemarks);
							escalatePCCRemarks.setRequired(false);
							escalatePCCRemarks.setValidationVisible(true);
							unbindField(escalatePCCRemarks);
						}
					}else{
						if(!mandatoryFields.contains(escalatePCCRemarks)){
							mandatoryFields.add(escalatePCCRemarks);
							showOrHideValidation(false);
							bindField(escalatePCCRemarks);
						}	
					}
					if(selectValue.getId() !=null){	
						if(screenName !=null && screenName.equals("Process Pre-Auth")){
							fireViewEvent(PreauthWizardPresenter.PREAUTH_PCC_SUB_CAT_TWO_GENERATE, selectValue.getId());
						}else if(screenName !=null && screenName.equals("Process Enhancements")){
							fireViewEvent(PreauthEnhancemetWizardPresenter.PREAUTH_ENHANCEMENT_PCC_SUB_CAT_TWO_GENERATE, selectValue.getId());
						}
					}
				}
			}			
		});
	}

	private boolean validatePage() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

		if(this.pccCategory != null && (this.pccCategory.getValue() == null || this.pccCategory.getValue().toString().equalsIgnoreCase("")) ){
			pccCategory.setValidationVisible(true);
			hasError = true;
			eMsg.append("Please Select PCC Category. </br>");
		}
		
		if(this.pccSubCategory != null && (this.pccSubCategory.getValue() == null || this.pccSubCategory.getValue().toString().equalsIgnoreCase("")) ){
			pccSubCategory.setValidationVisible(true);
			hasError = true;
			eMsg.append("Please Select PCC SUB Category. </br>");
		}
		
		if(this.pccSubCategoryTwo != null && (this.pccSubCategoryTwo.getValue() == null || this.pccSubCategoryTwo.getValue().toString().equalsIgnoreCase("")) ){
			pccSubCategoryTwo.setValidationVisible(true);
			hasError = true;
			eMsg.append("Please Select PCC SUB Category 2. </br>");
		}
		
		if(this.escalatePCCRemarks != null && pccSubCategory.getValue() != null &&
				(this.escalatePCCRemarks.getValue() == null || this.escalatePCCRemarks.getValue().equalsIgnoreCase("")) ){
			SelectValue selectValue = (SelectValue)pccSubCategory.getValue();
			if(selectValue.getId() !=null &&
					!ReferenceTable.getPCCRemarkNonMandList().contains(selectValue.getId())){
				escalatePCCRemarks.setValidationVisible(true);
				hasError = true;
				eMsg.append("Enter pcc remarks. </br>");
			}
		}	
		
		
		if (hasError) {		
			MessageBox.createError()
	    	.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
	        .withOkButton(ButtonOption.caption("OK")).open();
			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();
			} catch (CommitException e) {
				e.printStackTrace();
			}
		}
		return !hasError;
	}

	@SuppressWarnings("deprecation")
	private VerticalLayout buildLayout(){		
		
		escalatePCCRemarks = binder.buildAndBind("Escalate PCC Remarks", "escalatePccRemarks", TextArea.class);
		escalatePCCRemarks.setMaxLength(200);
		escalatePCCRemarks.setWidth("278px");
		escalatePCCRemarks.setHeight("130px");
		escalatePCCRemarks.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
		SHAUtils.handleTextAreaPopupDetails(escalatePCCRemarks,null,getUI(),SHAConstants.ESCALATE_PCC_REMARKS);
		
		pccCategory = binder.buildAndBind("PCC Category","pccCategory",ComboBox.class);	
		pccSubCategory = binder.buildAndBind("PCC Sub Category","pccSubCategory1",ComboBox.class);
		pccCategory.setNullSelectionAllowed(false);
		pccSubCategory.setNullSelectionAllowed(false);
		
		catFLayout = new FormLayout(pccCategory,pccSubCategory);
		remarkLayout = new FormLayout(escalatePCCRemarks);
		
		mandatoryFields.add(escalatePCCRemarks);
		mandatoryFields.add(pccCategory);
		mandatoryFields.add(pccSubCategory);
		showOrHideValidation(false);
		
		btnSubmit = new Button("Submit");
		btnSubmit.setWidth("-1px");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnCancel = new Button("Cancel");
		btnCancel.setWidth("-1px");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);

		HorizontalLayout frmHLayout = new HorizontalLayout(btnSubmit,btnCancel);
		frmHLayout.setMargin(false);
		frmHLayout.setSpacing(true);
		addpccCategory();
		addListener();	
		
		verticalLayout = new VerticalLayout(catFLayout,remarkLayout,frmHLayout);
		verticalLayout.setComponentAlignment(frmHLayout,Alignment.MIDDLE_CENTER);
		verticalLayout.setWidth("100.0%");

		return verticalLayout;
	}

	private void initBinder()
	{
		this.binder = new BeanFieldGroup<PccDetailsTableDTO>(PccDetailsTableDTO.class);
		this.binder.setItemDataSource(new PccDetailsTableDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private void unbindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.unbind(field);
			}
		}
	}
	
	private void bindField(Field<?> field) {
		if (field != null ) {
			Object propertyId = this.binder.getPropertyId(field);
			if (field!= null && field.isAttached() && propertyId != null) {
				field.setValue(null);
				this.binder.bind(field, propertyId);
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
	
	private void addpccCategory() {
		
		unbindField(pccCategory);
		BeanItemContainer<SelectValue> procedure = preauthService.getPCCCategorys();	
		pccCategory.setContainerDataSource(procedure);
		pccCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		pccCategory.setItemCaptionPropertyId("value");
	}
	
	public void generateFieldsBasedOnSubCatTWO(BeanItemContainer<SelectValue> procedure) {
		if (procedure != null) {
			
			unbindField(pccSubCategoryTwo);
			pccSubCategoryTwo = binder.buildAndBind("PCC Sub Category 2","pccSubCategory2",ComboBox.class);
			catFLayout.addComponent(pccSubCategoryTwo);	
			mandatoryFields.add(pccSubCategoryTwo);
			showOrHideValidation(false);
			pccSubCategoryTwo.setContainerDataSource(procedure);
			pccSubCategoryTwo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			pccSubCategoryTwo.setItemCaptionPropertyId("value");		
		}else {
			if(pccSubCategoryTwo !=null){
				unbindField(pccSubCategoryTwo);
				if(catFLayout != null
						&& catFLayout.getComponentCount() > 0) {
					catFLayout.removeComponent(pccSubCategoryTwo);
					mandatoryFields.remove(pccSubCategoryTwo);
					showOrHideValidation(false);
				}
				pccSubCategoryTwo = null;
			}	
		}
	}
	
	public void addpccSubCategory(BeanItemContainer<SelectValue> pccSubCatContainer) {
		
		this.pccSubCatContainer = pccSubCatContainer;
		pccSubCategory.setContainerDataSource(this.pccSubCatContainer);
		pccSubCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		pccSubCategory.setItemCaptionPropertyId("value");
		
		if(pccSubCategoryTwo !=null){
			unbindField(pccSubCategoryTwo);
			if(catFLayout != null
					&& catFLayout.getComponentCount() > 0) {
				catFLayout.removeComponent(pccSubCategoryTwo);
				mandatoryFields.remove(pccSubCategoryTwo);
				showOrHideValidation(false);
			}
			pccSubCategoryTwo = null;
		}	
	}
	
	
}
