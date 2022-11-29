package com.shaic.claim.registration.convertClaimPage;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
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

public class ConvertClaimPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ConvertClaimDTO bean;
	
	private BeanFieldGroup<ConvertClaimDTO> binder;
	
	private TextField claimedAmount;
	
	private TextField provisionAmount;
	
	private TextField claimStatus;
	
	private TextArea remarks;
	
	private ComboBox conversionReason;
	
	private Button convertbutton;
	
	private Button cancelBtn;
	
	private FormLayout amountLayout;
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();

	private FormLayout statusLayout;
	
	private HorizontalLayout firstHorizontal;
	
	private VerticalLayout verticalLayout;
	
	private HorizontalLayout buttonHorLayout;
	
	private SearchConvertClaimTableDto searchFormDto;
	
	private VerticalLayout verticalMain;

	
	@PostConstruct
	public void initView(){
		
	}
	
	public void initView(ConvertClaimDTO bean,BeanItemContainer<SelectValue> selectValueContainer,SearchConvertClaimTableDto searchFormDto)
	{
		this.bean=bean;
		this.searchFormDto=searchFormDto;
		this.binder = new BeanFieldGroup<ConvertClaimDTO>(ConvertClaimDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		claimedAmount = (TextField) binder.buildAndBind("Amount Claimed", "claimedAmount", TextField.class);
		claimedAmount.setReadOnly(true);
		provisionAmount = (TextField) binder.buildAndBind("Provision Amount", "provisionAmount", TextField.class);
		provisionAmount.setReadOnly(true);
		claimStatus = (TextField) binder.buildAndBind("Claim Status", "claimStatus", TextField.class);
		claimStatus.setReadOnly(true);
		remarks=(TextArea) binder.buildAndBind("Remarks", "denialRemarks", TextArea.class);
		remarks.setReadOnly(true);
		remarks.setWidth("400px");
		conversionReason=(ComboBox) binder.buildAndBind("Reason for Conversion", "conversionReason", ComboBox.class);
		
		conversionReason.setContainerDataSource(selectValueContainer);
		conversionReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		conversionReason.setItemCaptionPropertyId("value");
		
		mandatoryFields.add(conversionReason);
		
		showOrHideValidation(false);
		
		amountLayout=new FormLayout(claimedAmount,provisionAmount,conversionReason);
		
		statusLayout=new FormLayout(claimStatus,remarks);
		
		firstHorizontal=new HorizontalLayout(amountLayout,statusLayout);
		firstHorizontal.setSpacing(true);
		
		convertbutton=new Button("Convert to Reimbursement");
		cancelBtn=new Button("Cancel");
		
		convertbutton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertbutton.setWidth("-1px");
		convertbutton.setHeight("-10px");
		
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		buttonHorLayout=new HorizontalLayout(convertbutton,cancelBtn);
		buttonHorLayout.setSpacing(true);
		
		verticalLayout=new VerticalLayout(firstHorizontal,buttonHorLayout);
		verticalLayout.setSpacing(true);
		verticalLayout.setComponentAlignment(buttonHorLayout, Alignment.BOTTOM_CENTER);
		verticalMain=new VerticalLayout(verticalLayout);
		verticalMain.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
		verticalMain.setSizeFull();
		
		addListener();
		
		setCompositionRoot(verticalMain);
		
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
				                	
				                	VaadinSession session = getSession();
				                	Long  wrkFlowKey= (Long)getSession().getAttribute(SHAConstants.WK_KEY);
				                	
				                	if(wrkFlowKey != null){
				             			DBCalculationService dbService = new DBCalculationService();
				             			dbService.callUnlockProcedure(wrkFlowKey);
				             			getSession().setAttribute(SHAConstants.WK_KEY, null);
				             		}
									SHAUtils.releaseHumanTask(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getTaskNumber(),session);
				                	
				                	fireViewEvent(MenuItemBean.CONVERT_CLAIM, true);
				                } else {
				                    dialog.close();
				                }
				            }
				        });
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
		convertbutton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				Boolean hasError=false;
				if(conversionReason.getValue()==null){
					hasError=true;
				}
				if(validatePage(hasError)){
				SelectValue selected=new SelectValue();
				selected=(SelectValue)conversionReason.getValue();
				
				bean.setConversionReason(selected);
				
				fireViewEvent(ConvertClaimPagePresenter.CONVERSION_REIMBURSEMENT,bean,searchFormDto);
				}
			}
		});
		
	}
	
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
		
//		if (!this.binder.isValid()) {
//
//			for (Field<?> field : this.binder.getFields()) {
//				ErrorMessage errMsg = ((AbstractField<?>) field)
//						.getErrorMessage();
//				if (errMsg != null) {
//					eMsg += errMsg.getFormattedHtmlMessage();
//				}
//				hasError = true;
//			}
//		}
		
		
		
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

}
