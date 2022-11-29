package com.shaic.claim.withdrawPostProcessWizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.WizardStep;

import com.google.gwt.dev.protobuf.UnknownFieldSet.Field;
import com.shaic.arch.CrmFlaggedComponents;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.TmpCPUCode;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.sun.jersey.spi.inject.Errors.ErrorMessage;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;


public class ConvertReimbursementPostProcessPage extends ViewComponent implements WizardStep<WithdrawPreauthPostProcessPageDTO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ConvertClaimDTO bean;
	
	@Inject
	private CrmFlaggedComponents crmFlaggedComponents;

	private BeanFieldGroup<ConvertClaimDTO> binder;

	private TextField claimedAmount;

	private TextField provisionAmount;

	private TextField claimStatus;

	private TextArea remarks;

	private ComboBox conversionReason;

	private FormLayout amountLayout;

	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private FormLayout statusLayout;
	
	private FormLayout crmLayout;

	private HorizontalLayout firstHorizontal;

	private VerticalLayout verticalLayout;

	private VerticalLayout verticalMain;


	@PostConstruct
	public void initView(){

	}

	@Override
	public String getCaption() {
		return "Convert Claim";
	}

	public void initView(ConvertClaimDTO bean,BeanItemContainer<SelectValue> selectValueContainer)
	{
		this.bean=bean;
		if(selectValueContainer != null){
			this.bean.setConversionReasonList(selectValueContainer);
		}

	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<ConvertClaimDTO>(
				ConvertClaimDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	//	public void addListener(){
	//		cancelBtn.addClickListener(new Button.ClickListener() {
	//			/**
	//			 * 
	//			 */
	//			private static final long serialVersionUID = 1L;
	//
	//			@Override
	//			public void buttonClick(ClickEvent event) {
	//				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
	//				        "No", "Yes", new ConfirmDialog.Listener() {
	//
	//				            public void onClose(ConfirmDialog dialog) {
	//				                if (!dialog.isConfirmed()) {
	//				                	
	//				                	VaadinSession session = getSession();
	//									SHAUtils.releaseHumanTask(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getTaskNumber(),session);
	//				                	
	//				                	fireViewEvent(MenuItemBean.CONVERT_CLAIM_OUTSIDE_PROCESS, true);
	//				                } else {
	//				                    dialog.close();
	//				                }
	//				            }
	//				        });
	//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
	//			}
	//		});
	//		convertbutton.addClickListener(new ClickListener() {
	//			
	//			@Override
	//			public void buttonClick(ClickEvent event) {
	//				
	//				Boolean hasError=false;
	//				if(conversionReason.getValue()==null){
	//					hasError=true;
	//				}
	//				if(validatePage(hasError)){
	//				SelectValue selected=new SelectValue();
	//				selected=(SelectValue)conversionReason.getValue();
	//				
	//				bean.setConversionReason(selected);
	//				
	//				fireViewEvent(ConvertReimbursementPostProcessPagePresenter.CONVERSION_REIMBURSEMENT,bean,searchFormDto);
	//				}
	//			}
	//		});
	//		
	//	}

	protected void showOrHideValidation(Boolean isVisible) {
		for (Component component : mandatoryFields) {
			AbstractField<?>  field = (AbstractField<?>)component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}

	private boolean validatePage(Boolean hasError) {
		//Boolean hasError = false;
		showOrHideValidation(true);
		String eMsg = "";
		if (hasError) {
			setRequired(true);
			Label label = new Label("Select Reason For Conversion", ContentMode.HTML);
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
		} 
		showOrHideValidation(false);
		return true;
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	@Override
	public void init(WithdrawPreauthPostProcessPageDTO bean) {
		// TODO Auto-generated method stub

	}

	@Override
	public Component getContent() {
		initBinder();
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
//		if (claimedAmount != null && provisionAmount != null
//				&& claimStatus != null && remarks != null && conversionReason != null
//				) {
//			unbindField(claimedAmount);
//			unbindField(provisionAmount);
//			unbindField(claimStatus);
//			unbindField(remarks);
//			unbindField(conversionReason);
////			specialistFLayout.removeComponent(cmbSpecialistType);
////			specialistFLayout.removeComponent(cmbSpecialistConsulted);
////			specialistFLayout.removeComponent(cmbSpecialistType);
////			specialistFLayout.removeComponent(cmbSpecialistConsulted);
////			specialistFLayout.removeComponent(txtRemarksBySpecialist);
////			mandatoryFields.remove(cmbSpecialistConsulted);
////			mandatoryFields.remove(txtRemarksBySpecialist);
////			mandatoryFields.remove(cmbSpecialistType);
//		}
		claimedAmount = (TextField) binder.buildAndBind("Amount Claimed", "claimedAmount", TextField.class);
		claimedAmount.setReadOnly(true);
		provisionAmount = (TextField) binder.buildAndBind("Provision Amount", "provisionAmount", TextField.class);
		provisionAmount.setReadOnly(true);
		claimStatus = (TextField) binder.buildAndBind("Claim Status", "claimStatus", TextField.class);
		claimStatus.setReadOnly(true);
		remarks=(TextArea) binder.buildAndBind("Remarks", "denialRemarks", TextArea.class);
		remarks.setReadOnly(true);
		remarks.setWidth("200px");
		conversionReason=(ComboBox) binder.buildAndBind("Reason for Conversion", "conversionReason", ComboBox.class);

		conversionReason.setContainerDataSource(this.bean.getConversionReasonList());
		conversionReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		conversionReason.setItemCaptionPropertyId("value");
		conversionReason.setValue(this.bean.getConversionReasonList().getItemIds().get(0));
		mandatoryFields.add(conversionReason);

		showOrHideValidation(false);

		amountLayout=new FormLayout(claimedAmount,provisionAmount,conversionReason);

		statusLayout=new FormLayout(claimStatus,remarks);

		
		crmFlaggedComponents.init(bean.getCrcFlaggedReason(),bean.getCrcFlaggedRemark());
//		crmFlaggedComponents = new FormLayout(claimStatus,remarks);
		crmLayout=new FormLayout(crmFlaggedComponents);
		
		firstHorizontal=new HorizontalLayout(amountLayout,statusLayout,crmLayout);
		firstHorizontal.setSpacing(true);
//		HorizontalLayout hTempLayout = new HorizontalLayout(crmFlaggedComponents);
////		hTempLayout.setComponentAlignment(crmFlaggedLayout, Alignment.BOTTOM_RIGHT);
//		hTempLayout.setSpacing(true);

		//		convertbutton=new Button("Convert to Reimbursement");
		//		cancelBtn=new Button("Cancel");
		//		
		//		convertbutton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		//		convertbutton.setWidth("-1px");
		//		convertbutton.setHeight("-10px");
		//		
		//		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		//		cancelBtn.setWidth("-1px");
		//		cancelBtn.setHeight("-10px");
		//		
		//		buttonHorLayout=new HorizontalLayout(convertbutton,cancelBtn);
		//		buttonHorLayout.setSpacing(true);

		verticalLayout=new VerticalLayout(firstHorizontal/*,crmLayout*/);
		verticalLayout.setSpacing(true);
		//		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalMain=new VerticalLayout(verticalLayout);
		verticalMain.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
		verticalMain.setSizeFull();

		//		addListener();
		//		
		//		setCompositionRoot(verticalMain);
		return verticalMain;
	}

	@Override
	public void setupReferences(Map<String, Object> referenceData) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onAdvance() {
		
		Boolean hasError=false;
		if(conversionReason.getValue()==null){
			hasError=true;
		}
		if(validatePage(hasError)){
		SelectValue selected=new SelectValue();
		selected=(SelectValue)conversionReason.getValue();
		
			bean.setConversionReason(selected);
			hasError = Boolean.TRUE;
		}
		else{
			//return false;
			hasError = Boolean.FALSE;
		}

		return hasError;
	}
	
	
//	private void unbindField(Field<?> field) {
//		if (field != null) {
//			Object propertyId = this.binder.getPropertyId(field);
//			if (field != null && propertyId != null) {
//				this.binder.unbind(field);
//			}
//
//		}
//	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onSave() {
		// TODO Auto-generated method stub
		return false;
	}

}
