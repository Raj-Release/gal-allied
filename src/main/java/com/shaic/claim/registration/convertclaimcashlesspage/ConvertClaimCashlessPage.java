package com.shaic.claim.registration.convertclaimcashlesspage;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.registration.convertclaimcashless.SearchConverClaimCashlessTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasProductCpuRouting;
import com.shaic.domain.TmpCPUCode;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.newcode.wizard.dto.ConvertClaimDTO;
import com.shaic.starfax.simulation.PremiaPullService;
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

public class ConvertClaimCashlessPage extends ViewComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ConvertClaimDTO bean;
	
	@Inject
	private HospitalService hospitalService;
	
	@Inject
	private ClaimService claimService;
	
	@Inject
	private IntimationService intimationService;
	
	@EJB
	private PremiaPullService premiaPullService;
	
	private BeanFieldGroup<ConvertClaimDTO> binder;
	
	private TextField claimedAmount;
	
	private TextField provisionAmount;
	
	private TextField claimStatus;
	
	private TextField currentCpuCode;
	
	private TextField conversionCPUCode;
	
	private TextField claimType;
	
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
	
	private SearchConverClaimCashlessTableDTO searchFormDto;
	
	private VerticalLayout verticalMain;

	
	@PostConstruct
	public void initView(){
		
	}
	
	public void initView(ConvertClaimDTO bean,BeanItemContainer<SelectValue> selectValueContainer,SearchConverClaimCashlessTableDTO searchFormDto)
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
		claimType = (TextField) binder.buildAndBind("Claim Type", "claimType", TextField.class);
		claimType.setReadOnly(true);
//		remarks=(TextArea) binder.buildAndBind("Remarks", "denialRemarks", TextArea.class);
//		remarks.setReadOnly(true);
		remarks = new TextArea("Remarks");
		remarks.setWidth("400px");
		conversionReason=(ComboBox) binder.buildAndBind("Reason for Conversion", "conversionReason", ComboBox.class);
		conversionReason.setNullSelectionAllowed(false);
		
		Claim claimByClaimKey = claimService.getClaimByClaimKey(this.bean.getKey());
		Intimation intimationByKey = null;
		if (claimByClaimKey != null){
			 intimationByKey = intimationService.getIntimationByKey(claimByClaimKey.getIntimation().getKey());
		}
		
		currentCpuCode = new TextField("CPU Code (Current)");
		
		conversionCPUCode = new  TextField("CPU Code (After Conversion)");
		
		 currentCpuCode.setReadOnly(false);
		 conversionCPUCode.setReadOnly(false);
		 if(intimationByKey != null) {
			 currentCpuCode.setValue(String.valueOf(intimationByKey.getCpuCode().getCpuCode()));
			 Hospitals searchbyHospitalKey = hospitalService.searchbyHospitalKey(intimationByKey.getHospital());
			 TmpCPUCode tmpCPUCode = claimService.getTmpCPUCode(searchbyHospitalKey.getCpuId());
			 conversionCPUCode.setValue(String.valueOf(tmpCPUCode.getCpuCode()));
		 }
		//added for CPU routing //added for support fix IMSSUPPOR-32451
			if(intimationByKey.getPolicy() != null && intimationByKey.getPolicy().getProduct().getKey() != null){
				 Hospitals searchbyHospitalKey = hospitalService.searchbyHospitalKey(intimationByKey.getHospital());
				 TmpCPUCode tmpCPUCode = claimService.getTmpCPUCode(searchbyHospitalKey.getCpuId());
				 MasProductCpuRouting gmcRoutingProduct= premiaPullService.getMasProductForGMCRouting(intimationByKey.getPolicy().getProduct().getKey());
				 if(gmcRoutingProduct != null){
					 if (tmpCPUCode != null && tmpCPUCode.getGmcRoutingCpuCode() != null) {
						 tmpCPUCode = premiaPullService.getMasCpuCode(tmpCPUCode.getGmcRoutingCpuCode());
						 //					 masCpuCode = premiaPullService.getMasCpuCode(Long.valueOf(CpuCode));
						 conversionCPUCode.setValue(String.valueOf(tmpCPUCode.getCpuCode()));

					 }else {
						 conversionCPUCode.setValue(String.valueOf(tmpCPUCode.getCpuCode()));
					 }
				 }
			 }
			//added for CPU routing		
		 
		 currentCpuCode.setReadOnly(true);
		 conversionCPUCode.setReadOnly(true);
		conversionReason.setContainerDataSource(selectValueContainer);
		conversionReason.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		conversionReason.setItemCaptionPropertyId("value");
		
		mandatoryFields.add(conversionReason);
		
		showOrHideValidation(false);
		
		amountLayout=new FormLayout(claimedAmount,provisionAmount,conversionReason,remarks);
		
		statusLayout=new FormLayout(claimStatus,claimType,currentCpuCode,conversionCPUCode);
		
		firstHorizontal=new HorizontalLayout(amountLayout,statusLayout);
		firstHorizontal.setSpacing(true);
		
		convertbutton=new Button("Convert to Cashless");
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
									SHAUtils.releaseHumanTask(searchFormDto.getUsername(), searchFormDto.getPassword(), searchFormDto.getTaskNumber(),session);
				                	
				                	fireViewEvent(MenuItemBean.CONVERT_CLAIM_CASHLESS, true);
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
				
				fireViewEvent(ConvertClaimCashlessPagePresenter.CONVERSION_CASHLESS,bean,searchFormDto);
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
		StringBuffer eMsg = new StringBuffer();
		
		if(conversionReason.getValue()==null){
			
			eMsg.append("Select Reason For Conversion </br>");
			hasError=true;
		}
		
		if(conversionReason.getValue() != null){
			 SelectValue value = (SelectValue) conversionReason.getValue();
				if((value.getValue().equalsIgnoreCase(SHAConstants.CASHLESS_CONV_OTHERS) && ((remarks.getValue()== null)
								||(remarks.getValue() != null && remarks.getValue().equalsIgnoreCase(""))))){
					eMsg.append("Please Enter Remarks");
					hasError=true;
				}

		}
		
		
		if (hasError) {
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
