package com.shaic.claim.corpbuffer.allocation.wizard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.utils.Props;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.ViewDetails.ViewLevels;
import com.shaic.claim.bulkconvertreimb.search.SearchBulkConvertFormDto;
import com.shaic.claim.corpbuffer.allocation.search.AllocateCorpBufferPresenter;
import com.shaic.claim.cvc.auditaction.SearchCVCAuditActionFormDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.carousel.RevisedCashlessCarousel;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.annotations.Theme;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme(Props.THEME_NAME)
public class AllocateCorpBufferWizardViewImpl extends AbstractMVPView implements AllocateCorpBufferWizard {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<RevisedCashlessCarousel> commonCarouselInstance;

	private AllocateCorpBufferDetailDTO bean;
	
	@Inject
	private ViewDetails viewDetails;
	
	@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;
	
	private TextField txtPolicyLabel;
	
	private TextField txtCorpBufferSI;
	
	private TextField txtCorpUtilizedAmt;
	
	private TextField txtdisBufferUtilizedAmt;
	
	private TextField txtpolicywisedisBufferAvlBlnc;
	
	private TextField txtdisBufferApplTo;

	private TextField txtdisBufferIndviSI;

	private TextField txtinsureName ;

	private TextField txtinsureAge ;

	private TextField txtinsurerelationShip;

	private TextField txtmaxdisBufferInsuredLimit;

	private TextField txtdisBufferInsuredLimit;

	private TextField txtdisBufferAvailBalnc;

	private TextField txtmaxwintageBufferLimit;

	private TextField txtwintageBufferLimit;

	private TextField txtwintageBufferAvlBalnc;
	
	private TextField txtwintageBufferAllocated;

	private TextField txtwintageAvlBalnc;

	private TextField txtmaxnacBufferLimit;

	private TextField txtnacBufferLimit;

	private TextField txtnacBufferAvlBalnc;
	
	private TextField txtnacBufferAllocated;

	private TextField txtnacAvlBalnc;

	private Button btnSubmit;
	
	private Button btnCancel;
	
	private String screenName;
	
	private CheckBox chkdisupdateCorpBuffer;
	
	private CheckBox chkwintageupdateCorpBuffer;
	
	private CheckBox chknacupdateCorpBuffer;
	
	String corpBufferAllocatedToClaim;
	
	Double gmcAvailableSI = 0.0;
	
	private TabSheet corpBufferAllocatedTab;
	
	private Button disEdit;
	
	
	private Boolean isdisEditClicked = false;

	private Boolean iswintageEditClicked = false;

	private Boolean isnacbEditClicked = false;
	
	private BeanFieldGroup<AllocateCorpBufferDetailDTO> binder;
	
	
	private TextField txtInsuredLabel;
	
	private TextField txtNoOfTimesSi;
	
	private TextField txtWinBufferIndviSI;
	
	private ComboBox txtWinBufferApplTo;
	
	private TextField txtWintageNoOfTimesSi;
	
	private TextField txtmaxWinBufferInsuredLimit;
	
	private TextField txtwinBufferUtilizedAmt;
	
    private TextField txtnacBufferIndviSI;
	
	private ComboBox txtnacBufferApplTo;
	
	private TextField txtnacNoOfTimesSi;
	
	private TextField txtmaxNacBufferInsuredLimit;
	
	private TextField txtnacBufferUtilizedAmt;
	
	
	@PostConstruct
	public void init() {
		addStyleName("view");	
	}
	
	@Override
	public void resetView() {
	}

	@Override
	public void initView(AllocateCorpBufferDetailDTO bean) {
		
		setSizeFull();
		this.bean = bean;
		initBinder();
		isdisEditClicked = false;
		iswintageEditClicked = false;
		isnacbEditClicked = false;
		RevisedCashlessCarousel intimationDetailsCarousel = commonCarouselInstance.get();
		intimationDetailsCarousel.init(this.bean.getNewIntimationDto(),this.bean.getClaimDto(), "Allocate Corportate Buffer", bean.getDiagnosis());
		viewDetails.initView(this.bean.getNewIntimationDto().getIntimationId(), ViewLevels.CLOSE_CLAIM, false,"Re-open Claim(Search Based)");
	
//		txtinsureName.setValue(this.bean.getInsureName());
//		txtinsurerelationShip.setValue(this.bean.getInsurerelationShip());
//        txtinsureAge.setValue(this.bean.getInsureAge().toString());		
		corpBufferAllocatedTab = new TabSheet();
		corpBufferAllocatedTab.setStyleName(ValoTheme.TABSHEET_FRAMED);
		corpBufferAllocatedTab.setWidth("47%");
		TabSheet discretionaryBufferTab =  getDiscretionaryBufferTab();
		TabSheet wintageBufferTab =  getWintageBufferTab();
		TabSheet nacBufferTab =  getNACBufferTab();
		corpBufferAllocatedTab.addTab(discretionaryBufferTab, "Discretionary Buffer", null);
		corpBufferAllocatedTab.addTab(wintageBufferTab, "Vintage Buffer", null);
		corpBufferAllocatedTab.addTab(nacBufferTab, "Non Admissible Corporate Buffer (NACB)", null);
//		if(corpBufferAllocatedTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.DISCRETIONARY_BUFFER_TABID)) {
//			corpBufferAllocatedTab.getSelectedTab().setEnabled(false);
//		}
//		corpBufferAllocatedTab.setEnabled(false);
		//		dummyField = new TextField();
//		dummyField.setEnabled(false);
//	    dummyField.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//	    dummyField.setWidth("49px");
//	    dummyField.setHeight("327px");
//	    
//	    editBLayout = new FormLayout(dummyField);	
		if(bean.getIsDisBufferApplicable()){
			discretionaryBufferTab.setEnabled(true);
		}else if(!bean.getIsDisBufferApplicable()){
			discretionaryBufferTab.setEnabled(false);
		}
		if(bean.getIsWintageBufferApplicable()){
			wintageBufferTab.setEnabled(true);
		}else if(!bean.getIsWintageBufferApplicable()){
			wintageBufferTab.setEnabled(false);
		}
		if(bean.getIsNacBufferApplicable()){
			nacBufferTab.setEnabled(true);
		}else if(!bean.getIsNacBufferApplicable()){
			nacBufferTab.setEnabled(false);
		}
		HorizontalLayout buttonLayout = buildCorpBufferButtonLayout();
		HorizontalLayout bufferLayout = new HorizontalLayout(corpBufferAllocatedTab);
//		HorizontalLayout bufferLayout = new HorizontalLayout(corpBufferAllocatedTab,editBLayout);
		VerticalLayout vLayout = new VerticalLayout(viewDetails,bufferLayout,buttonLayout);
		vLayout.setWidth("100%");
		vLayout.setComponentAlignment(viewDetails, Alignment.TOP_RIGHT);		
		vLayout.setComponentAlignment(bufferLayout, Alignment.MIDDLE_CENTER);
		vLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		VerticalLayout mainVertical = new VerticalLayout(intimationDetailsCarousel, vLayout);
		mainVertical.setSpacing(true);
		setHeight("100%");
		addListener();
		setCompositionRoot(mainVertical);
	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildCorpBufferButtonLayout(){
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setEnabled(false);
				
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		
		corpBufferValuesUpdateListener();
		horizontalLayout.addComponents(btnSubmit,btnCancel);
		horizontalLayout.setSpacing(true);
		return horizontalLayout;
	
	}
	
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof com.vaadin.v7.ui.AbstractField) {
				if(c instanceof TextField){
					if (c.getId() !=null && 
							c.getId().equals("BUFFERLIMIT")) {
						TextField field = (TextField) c;
						field.setWidth("81px");
						field.setNullRepresentation("");
						field.setEnabled(false);
						field.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
					} else {
						TextField field = (TextField) c;
						field.setWidth("250px");
						field.setNullRepresentation("");
						field.setReadOnly(readOnly);
						field.addStyleNames(ValoTheme.TEXTFIELD_BORDERLESS,ValoTheme.TEXTFIELD_ALIGN_RIGHT);

					}
				} 
			}
		}
	}
	
	public void corpBufferValuesUpdateListener() {

		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(isvalid()){
					fireViewEvent(AllocateCorpBufferPresenter.SUBMIT_BUTTON_CLICK, getValues());	
					buildSucessLayout();
				}
			}									 			
		});
		
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				Label cancelLabel = new Label("<b style = 'color: black;'>Are you sure you want to cancel ?</b>", ContentMode.HTML);
				Button yesBtn = new Button("Yes");
				Button noBtn = new Button("No");
				yesBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				HorizontalLayout horizontalLayout = new HorizontalLayout(yesBtn, noBtn);
				horizontalLayout.setSpacing(true);
				VerticalLayout layout = new VerticalLayout(cancelLabel, horizontalLayout);
				layout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
				layout.setSpacing(true);
				layout.setMargin(true);
				
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(layout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				
				yesBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();						
						fireViewEvent(MenuItemBean.ALLOCATE_CORP_BUFFER, null);					
					}
				});
				
				noBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});
			}
		});
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<AllocateCorpBufferDetailDTO>(AllocateCorpBufferDetailDTO.class);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private TabSheet getDiscretionaryBufferTab(){

		TabSheet queryCompletedTab = new TabSheet();

		queryCompletedTab.setStyleName("g-search-panel");
		queryCompletedTab.setId(SHAConstants.DISCRETIONARY_BUFFER_TABID);
		queryCompletedTab.setWidth("100%");
		queryCompletedTab.setHeight("100%");
		queryCompletedTab.setSizeFull();

		queryCompletedTab.addComponent(buildDiscretionaryBufferLayout());
		return queryCompletedTab;

	}
	private TabSheet getWintageBufferTab(){

		TabSheet queryPendingTab = new TabSheet();

		queryPendingTab.setStyleName("g-search-panel");
		queryPendingTab.setId(SHAConstants.WINTAGE_BUFFER_TABID);
		queryPendingTab.setWidth("100%");
		queryPendingTab.setHeight("100%");
		queryPendingTab.setSizeFull();
		queryPendingTab.setEnabled(false);
		
		queryPendingTab.addComponent(buildWintageBufferLayout());
		return queryPendingTab;

	}

	private TabSheet getNACBufferTab(){

		TabSheet queryReplyRecievedTab = new TabSheet();
		queryReplyRecievedTab.setId(SHAConstants.NAC_BUFFER_TABID);
		queryReplyRecievedTab.setStyleName("g-search-panel");
		queryReplyRecievedTab.setWidth("100%");
		queryReplyRecievedTab.setHeight("100%");
		queryReplyRecievedTab.setSizeFull();
		
		queryReplyRecievedTab.addComponent(buildNACBufferLayout());
		return queryReplyRecievedTab;

	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildDiscretionaryBufferLayout(){
		txtPolicyLabel = new TextField("<b>Policy Wise </b>");
		txtPolicyLabel.setCaptionAsHtml(true);
		txtCorpBufferSI = (TextField) binder.buildAndBind("Discretionary Buffer SI","disBufferSI", TextField.class);
		txtCorpUtilizedAmt = (TextField) binder.buildAndBind("Discretionary buffer utilised amount","disBufferUtilizedAmt", TextField.class);
		txtpolicywisedisBufferAvlBlnc = (TextField) binder.buildAndBind("Available Balance","policywisedisBufferAvlBlnc", TextField.class);
		txtInsuredLabel = new TextField("<b>Insured Wise </b>");
		txtInsuredLabel.setCaptionAsHtml(true);
		txtinsureName =  new TextField("Employee Name" );
		txtinsureName.setValue(this.bean.getInsureName());
		txtinsurerelationShip = new TextField("Relationship");
		txtinsurerelationShip.setValue(this.bean.getInsurerelationShip());
		txtinsureAge = new TextField("Employee age");
		txtinsureAge.setValue(this.bean.getInsureAge().toString());
		txtdisBufferIndviSI = (TextField) binder.buildAndBind("Floater SI","disBufferIndviSI", TextField.class);
		txtdisBufferApplTo = (TextField) binder.buildAndBind("Discretionary Buffer Limit Applicable To","disBufferApplTo", TextField.class);
		txtNoOfTimesSi = (TextField) binder.buildAndBind("No Of Times(SI)","noOfTimes", TextField.class);
		txtmaxdisBufferInsuredLimit = (TextField) binder.buildAndBind("Maximum Discretionary Buffer Limit for Employee","maxdisBufferInsuredLimit", TextField.class);
		txtdisBufferInsuredLimit = (TextField) binder.buildAndBind("Discretionary Buffer Limit for Employee","disBufferInsuredLimit", TextField.class);
		txtdisBufferInsuredLimit.setId("BUFFERLIMIT");
		txtdisBufferUtilizedAmt = (TextField) binder.buildAndBind("Discretionary Buffer Utilized Amount","discretionaryUtilizedInsured", TextField.class);
		txtdisBufferAvailBalnc = (TextField) binder.buildAndBind("Available Balance","disBufferAvailBalnc", TextField.class);
		chkdisupdateCorpBuffer = (CheckBox) binder.buildAndBind("Update Corp Buffer","disupdateCorpBuffer", CheckBox.class);
		chkdisupdateCorpBuffer.addValueChangeListener(getDBCheckboxListener());
				
//		disEdit = new Button("Edit");
//		if(bean.getMaxdisBufferInsuredLimit() !=null
//				&& bean.getMaxdisBufferInsuredLimit() > 0){
//			disEdit.setEnabled(true);
//		}else{
//			disEdit.setEnabled(false);
//		}
//		txtdisBufferInsuredLimit.addBlurListener(getdisAvlBalncListener());
		
	    
		/*FormLayout formVLayout = new FormLayout(dummyField,disEdit);*/	
		FormLayout formCBLayout = new FormLayout(txtPolicyLabel,txtCorpBufferSI,txtCorpUtilizedAmt,txtpolicywisedisBufferAvlBlnc,txtInsuredLabel
													,txtinsureName,txtinsurerelationShip,txtinsureAge,txtdisBufferIndviSI,txtdisBufferApplTo,txtNoOfTimesSi,
													txtmaxdisBufferInsuredLimit,txtdisBufferInsuredLimit,txtdisBufferUtilizedAmt,txtdisBufferAvailBalnc);
		formCBLayout.setMargin(true);
		formCBLayout.setSpacing(true);
		formCBLayout.addStyleName("layoutDesign");
		formCBLayout.addComponent(chkdisupdateCorpBuffer);
		formCBLayout.setComponentAlignment(chkdisupdateCorpBuffer, Alignment.MIDDLE_CENTER);
		setReadOnly(formCBLayout, true);
		HorizontalLayout hLayout = new HorizontalLayout(formCBLayout);	
		return hLayout;

	}
	
	@SuppressWarnings("deprecation")
	private HorizontalLayout buildWintageBufferLayout(){
		

		txtPolicyLabel = new TextField("<b>Policy Wise</b>");
		txtPolicyLabel.setCaptionAsHtml(true);
		txtmaxwintageBufferLimit = (TextField) binder.buildAndBind("Vintage Buffer SI","maxwintageBufferLimit", TextField.class);
		txtwintageBufferAllocated = (TextField) binder.buildAndBind("Vintage buffer utilised amount","wintageAllocatedLimit", TextField.class);
		txtwintageBufferAvlBalnc = (TextField) binder.buildAndBind("Available Balance","wintageBufferAvlBalnc", TextField.class);
		txtInsuredLabel = new TextField("<b>Insured Wise </b>");
		txtInsuredLabel.setCaptionAsHtml(true);
		txtinsureName =  new TextField("Employee Name" );
		txtinsureName.setValue(this.bean.getInsureName());
		txtinsurerelationShip = new TextField("Relationship");
		txtinsurerelationShip.setValue(this.bean.getInsurerelationShip());
		txtinsureAge = new TextField("Employee age");
		txtinsureAge.setValue(this.bean.getInsureAge().toString());
		txtWinBufferIndviSI = (TextField) binder.buildAndBind("Floater SI","winBufferIndviSI", TextField.class);
		txtWinBufferApplTo = (ComboBox) binder.buildAndBind("Vintage Buffer Limit Applicable To","winBufferApplTo", ComboBox.class);
		 BeanItemContainer<SelectValue> selectValueForVerficationType = SHAUtils.getSelectValueForBufferLimitApplicableTo();
		 txtWinBufferApplTo.setContainerDataSource(selectValueForVerficationType);
		 txtWinBufferApplTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 txtWinBufferApplTo.setItemCaptionPropertyId("value");
		txtWintageNoOfTimesSi = (TextField) binder.buildAndBind("No Of Times(SI)","wintageNoOfTimes", TextField.class);
		txtmaxWinBufferInsuredLimit = (TextField) binder.buildAndBind("Maximum Vintage Buffer Limit for Employee","maxWinBufferInsuredLimit", TextField.class);
		txtwintageBufferLimit = (TextField) binder.buildAndBind("Vintage Buffer Limit for Employee","wintageBufferLimit", TextField.class);
		txtwintageBufferLimit.setId("BUFFERLIMIT");
		txtwinBufferUtilizedAmt = (TextField) binder.buildAndBind("Vintage Buffer Utilized Amount","wintageUtilizedInsured", TextField.class);
		txtwintageAvlBalnc = (TextField) binder.buildAndBind("Available Balance","wintageAvlBalnc", TextField.class);
		chkwintageupdateCorpBuffer = (CheckBox) binder.buildAndBind("Update Corp Buffer","wintageupdateCorpBuffer", CheckBox.class);
		chkwintageupdateCorpBuffer.addValueChangeListener(getWintageCheckboxListener());
				
//		disEdit = new Button("Edit");
//		if(bean.getMaxwintageBufferLimit() !=null
//				&& bean.getMaxwintageBufferLimit() > 0){
//			disEdit.setEnabled(true);
//		}else{
//			disEdit.setEnabled(false);
//		}
//		txtwintageBufferLimit.addBlurListener(getwintageAvlBalncListener());
		
	    
		/*FormLayout formVLayout = new FormLayout(dummyField,disEdit);*/	
		FormLayout formLayoutRight = new FormLayout(txtPolicyLabel,txtmaxwintageBufferLimit,txtwintageBufferAllocated,txtwintageBufferAvlBalnc,txtInsuredLabel
													,txtinsureName,txtinsurerelationShip,txtinsureAge,txtWinBufferIndviSI,txtWinBufferApplTo,txtWintageNoOfTimesSi,
													txtmaxWinBufferInsuredLimit,txtwintageBufferLimit,txtwinBufferUtilizedAmt,txtwintageAvlBalnc);
		formLayoutRight.setMargin(true);
		formLayoutRight.setSpacing(true);
		formLayoutRight.addStyleName("layoutDesign");
		formLayoutRight.addComponent(chkwintageupdateCorpBuffer);
		formLayoutRight.setComponentAlignment(chkwintageupdateCorpBuffer, Alignment.MIDDLE_CENTER);
		setReadOnly(formLayoutRight, true);
		HorizontalLayout hLayout = new HorizontalLayout(formLayoutRight);	
		return hLayout;


	}

	@SuppressWarnings("deprecation")
	private HorizontalLayout buildNACBufferLayout(){
		

		txtPolicyLabel = new TextField("<b>Policy Wise</b>");
		txtInsuredLabel.setCaptionAsHtml(true);
		txtmaxnacBufferLimit = (TextField) binder.buildAndBind("NACB Buffer SI","maxnacBufferLimit", TextField.class);
		txtnacBufferAllocated = (TextField) binder.buildAndBind("NACB buffer utilised amount","nacAllocatedLimit", TextField.class);
		txtnacAvlBalnc = (TextField) binder.buildAndBind("Available Balance","nacAvlBalnc", TextField.class);
		txtInsuredLabel = new TextField("<b>Insured Wise </b> ");
		txtInsuredLabel.setCaptionAsHtml(true);
		txtinsureName =  new TextField("Employee Name" );
		txtinsureName.setValue(this.bean.getInsureName());
		txtinsurerelationShip = new TextField("Relationship");
		txtinsurerelationShip.setValue(this.bean.getInsurerelationShip());
		txtinsureAge = new TextField("Employee age");
		txtinsureAge.setValue(this.bean.getInsureAge().toString());
		txtnacBufferIndviSI = (TextField) binder.buildAndBind("Floater SI","nacbBufferIndviSI", TextField.class);
		txtnacBufferApplTo = (ComboBox) binder.buildAndBind("NACB Buffer Limit Applicable To","nacbBufferApplTo", ComboBox.class);
        BeanItemContainer<SelectValue> selectValueForVerficationType = SHAUtils.getSelectValueForBufferLimitApplicableTo();
        txtnacBufferApplTo.setContainerDataSource(selectValueForVerficationType);
        txtnacBufferApplTo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        txtnacBufferApplTo.setItemCaptionPropertyId("value");
		txtnacNoOfTimesSi = (TextField) binder.buildAndBind("No Of Times(SI)","nacbNoOfTimes", TextField.class);
		txtmaxNacBufferInsuredLimit = (TextField) binder.buildAndBind("Maximum NACB Buffer Limit for Employee","maxNacbBufferInsuredLimit", TextField.class);
		txtnacBufferLimit = (TextField) binder.buildAndBind("NACB Buffer Limit for Employee","nacBufferLimit", TextField.class);
		txtnacBufferLimit.setId("BUFFERLIMIT");
		txtnacBufferUtilizedAmt = (TextField) binder.buildAndBind("NACB Buffer Utilized Amount","nacbUtilizedInsured", TextField.class);
		txtnacBufferAvlBalnc = (TextField) binder.buildAndBind("Available Balance","nacBufferAvlBalnc", TextField.class);
		chknacupdateCorpBuffer = (CheckBox) binder.buildAndBind("Update Corp Buffer","nacupdateCorpBuffer", CheckBox.class);
		chknacupdateCorpBuffer.addValueChangeListener(getNACBCheckboxListener());
				
//		disEdit = new Button("Edit");
//		if(bean.getMaxnacBufferLimit() !=null
//				&& bean.getMaxnacBufferLimit() > 0){
//			disEdit.setEnabled(true);
//		}else{
//			disEdit.setEnabled(false);
//		}
//		txtnacBufferLimit.addBlurListener(getNACAvlBalncListener());
		
	    
		/*FormLayout formVLayout = new FormLayout(dummyField,disEdit);*/	
		FormLayout formLayoutRight = new FormLayout(txtPolicyLabel,txtmaxnacBufferLimit,txtnacBufferAllocated,txtnacAvlBalnc,txtInsuredLabel
													,txtinsureName,txtinsurerelationShip,txtinsureAge,txtnacBufferIndviSI,txtnacBufferApplTo,txtnacNoOfTimesSi,
													txtmaxNacBufferInsuredLimit,txtnacBufferLimit,txtnacBufferUtilizedAmt,txtnacBufferAvlBalnc);
		formLayoutRight.setMargin(true);
		formLayoutRight.setSpacing(true);
		formLayoutRight.addStyleName("layoutDesign");
		formLayoutRight.addComponent(chknacupdateCorpBuffer);
		formLayoutRight.setComponentAlignment(chknacupdateCorpBuffer, Alignment.MIDDLE_CENTER);
		setReadOnly(formLayoutRight, true);
		HorizontalLayout hLayout = new HorizontalLayout(formLayoutRight);	
		return hLayout;

	}
	
	private ValueChangeListener getWintageCheckboxListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CheckBox property = (CheckBox) event.getProperty();
				if(property.getValue() !=null && property.getValue()){
					if(bean.getStageInformation() !=null){
		        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
		        	 }
		        	 if(bean.getWintageBufferAvlBalnc() !=null &&
		        			 bean.getWintageBufferAvlBalnc() <= 0 ){
		        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
		        	 }else{
		        		 btnSubmit.setEnabled(true);
		        		 txtwintageBufferLimit.setEnabled(true);
		        		 iswintageEditClicked = true;
		        		 bean.setIsWinbufferClicked(true);
		        	 }
				}else {
					txtwintageBufferLimit.setEnabled(false);
					btnSubmit.setEnabled(false);
					 bean.setIsWinbufferClicked(false);
				}
			}
		};
		return listener;
	}
	private ValueChangeListener getDBCheckboxListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CheckBox property = (CheckBox) event.getProperty();
				if(property.getValue() !=null && property.getValue()){
					if(bean.getStageInformation() !=null){
		        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
		        	 }
					if(bean.getMaxdisBufferInsuredLimit() != null && bean.getMaxdisBufferInsuredLimit() > 0 &&
							bean.getDisAllocatedLimit() != null && bean.getDisAllocatedLimit() == 0d){
						 btnSubmit.setEnabled(true);
		        		 txtdisBufferInsuredLimit.setEnabled(true);
		        		 isdisEditClicked = true;
		        		 bean.setIsDisbufferClicked(true);
					}
					else if(bean.getMaxdisBufferInsuredLimit() !=null &&
							bean.getMaxdisBufferInsuredLimit() > 0 &&  bean.getDiscretionaryUtilizedInsured() != null
							&& bean.getDiscretionaryUtilizedInsured() > 0 && (bean.getMaxdisBufferInsuredLimit() <= bean.getDiscretionaryUtilizedInsured())){
		        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
		        		 btnSubmit.setEnabled(false);
						}
					else{
		        		 btnSubmit.setEnabled(true);
		        		 txtdisBufferInsuredLimit.setEnabled(true);
		        		 isdisEditClicked = true;
		        		 bean.setIsDisbufferClicked(true);
		        	 }
//					if(bean.getDisBufferApplTo() !=null){
//						if(bean.getDisBufferApplTo().equals("Dependent Only")
//								&& bean.getIsEmployee() !=null && bean.getIsEmployee()){
//							SHAUtils.showErrorMessageBoxWithCaption("Buffer allocation limited to Dependent only", "Error - Message");
//							property.setValue(false);
//						}else if(bean.getDisBufferApplTo().equals("Employee Only")
//								&& bean.getIsDependent() !=null && bean.getIsDependent()){
//							SHAUtils.showErrorMessageBoxWithCaption("Buffer allocation limited to Employee only", "Error - Message");
//							property.setValue(false);
//						}
//					}
				}else {
					txtdisBufferInsuredLimit.setEnabled(false);
					btnSubmit.setEnabled(false);
					bean.setIsDisbufferClicked(false);
				}
			}
		};
		return listener;
	}
	private ValueChangeListener getNACBCheckboxListener() {
		ValueChangeListener listener = new ValueChangeListener() {
			private static final long serialVersionUID = 7342288705079773186L;
			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				CheckBox property = (CheckBox) event.getProperty();
				if(property.getValue() !=null && property.getValue()){
					if(bean.getStageInformation() !=null){
		        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
		        	 }
		        	 if(bean.getNacBufferAvlBalnc() !=null &&
		        			 bean.getNacBufferAvlBalnc() <= 0 ){
		        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
		        	 }else{
		        		 btnSubmit.setEnabled(true);
		        		 txtnacBufferLimit.setEnabled(true);
		        		 isnacbEditClicked = true;
		        		 bean.setIsNacbufferClicked(true);
		        	 }
//					if(bean.getDisBufferApplTo() !=null){
//						if(bean.getDisBufferApplTo().equals("Dependent Only")
//								&& bean.getIsEmployee() !=null && bean.getIsEmployee()){
//							SHAUtils.showErrorMessageBoxWithCaption("Buffer allocation limited to Dependent only", "Error - Message");
//							property.setValue(false);
//						}else if(bean.getDisBufferApplTo().equals("Employee Only")
//								&& bean.getIsDependent() !=null && bean.getIsDependent()){
//							SHAUtils.showErrorMessageBoxWithCaption("Buffer allocation limited to Employee only", "Error - Message");
//							property.setValue(false);
//						}
//					}
				}else {
					txtnacBufferLimit.setEnabled(false);
					btnSubmit.setEnabled(false);
					 bean.setIsNacbufferClicked(false);
				}
			}
		};
		return listener;
	}

	public boolean isvalid(){

		boolean hasError =false;
		StringBuffer eMsg = new StringBuffer();		

		if(isdisEditClicked && txtdisBufferInsuredLimit.getValue() == null){
			hasError = true;
			eMsg.append("Please Enter the Discretionary Buffer Limit. </br>");
		}
		
		if(isdisEditClicked && (chkdisupdateCorpBuffer.getValue() == null
				|| !chkdisupdateCorpBuffer.getValue())){
			hasError = true;
			eMsg.append("Please select Discretionary Buffer Update Corp Buffer. </br>");
		}
		if (isdisEditClicked && txtdisBufferInsuredLimit.getValue() !=null 
				&& bean.getDisBufferAvailBalnc() !=null ) {
			Double bufferInsuredLimit = Double.parseDouble(txtdisBufferInsuredLimit.getValue().replace(",",""));
			Double insuredUtilizedAmount = bean.getDiscretionaryUtilizedInsured();
			if(bufferInsuredLimit < insuredUtilizedAmount){
				hasError = true;
				eMsg.append("The discretionary buffer Allocated Amount cannot be less than discretionary buffer utilized Amount. </br>");
			}
		}
		if (isdisEditClicked && txtdisBufferInsuredLimit.getValue() !=null 
				&& bean.getDisBufferAvailBalnc() != null && bean.getMaxdisBufferInsuredLimit() !=null &&
				bean.getMaxdisBufferInsuredLimit() > 0) {
			Double bufferAllocatedAmount = bean.getMaxdisBufferInsuredLimit();
			Double availablePolicySi = bean.getPolicywisedisBufferAvlBlnc();
			Double bufferInsuredLimit = Double.parseDouble(txtdisBufferInsuredLimit.getValue().replace(",",""));
//			if(bufferInsuredLimit > bean.getDisAllocatedLimit() ){
				if (bufferInsuredLimit > bean.getMaxdisBufferInsuredLimit()) {
					hasError = true;
					eMsg.append("The discretionary buffer limit for Employee is exceeding the maximum discretionary buffer Limit for employee. </br>");
			   }
				else if(bufferInsuredLimit > availablePolicySi){
					hasError = true;
					eMsg.append("The discretionary buffer limit for Employee is exceeding the corporate buffer balance available for policy. </br>");
				}
			
		 } else if(isdisEditClicked && txtdisBufferInsuredLimit.getValue() !=null &&
				 (bean.getMaxdisBufferInsuredLimit() == null ||  bean.getMaxdisBufferInsuredLimit() == 0)){
			Double availablePolicySi = bean.getPolicywisedisBufferAvlBlnc();
			Double bufferInsuredLimit = Double.parseDouble(txtdisBufferInsuredLimit.getValue().replace(",",""));
			if(bufferInsuredLimit > availablePolicySi){
				hasError = true;
				eMsg.append("The discretionary buffer limit for Employee is exceeding the corporate buffer balance available for policy. </br>");
			}
		}
		
		
		if (iswintageEditClicked &&  txtWinBufferApplTo.getValue() == null){
			hasError = true;
			eMsg.append("Please select Vintage Limit applicable to. </br>");
		}
		if(iswintageEditClicked && txtwintageBufferLimit.getValue() == null){
			hasError = true;
			eMsg.append("Please Enter the Vintage Buffer Limit. </br>");
		}
		
		if(iswintageEditClicked && (chkwintageupdateCorpBuffer.getValue() == null
				|| !chkwintageupdateCorpBuffer.getValue())){
			hasError = true;
			eMsg.append("Please select Vintage Buffer Update Corp Buffer. </br>");
		}
		
		if (iswintageEditClicked && txtwintageBufferLimit.getValue() !=null 
				&& bean.getWintageBufferAvlBalnc() !=null) {	

			if(txtwintageBufferAvlBalnc != null && txtwintageBufferAvlBalnc.getValue() != null && !txtwintageBufferAvlBalnc.getValue().isEmpty()){
				Double limitAmt = Double.valueOf(txtwintageBufferLimit.getValue().replace(",", ""));
				Double avlBalnc = Double.valueOf(txtwintageBufferAvlBalnc.getValue().replace(",", ""));
				if (limitAmt > avlBalnc) {
					eMsg.append("The Vintage buffer limit for Employee is exceeding the Vintage buffer balance available for the policy. </br>");
					hasError = true;
				}
		} 
		}
		
		if(isnacbEditClicked && txtnacBufferLimit.getValue() == null){
			hasError = true;
			eMsg.append("Please Enter the NACB Buffer Limit. </br>");
		}
		
		if (isnacbEditClicked &&  txtnacBufferApplTo.getValue() == null){
			hasError = true;
			eMsg.append("Please select NACB Limit applicable to. </br>");
		}
		
		if(isnacbEditClicked && (chknacupdateCorpBuffer.getValue() == null
				|| !chknacupdateCorpBuffer.getValue())){
			hasError = true;
			eMsg.append("Please select NAC Buffer Update Corp Buffer. </br>");
		}
		
		if (isnacbEditClicked && txtnacBufferLimit.getValue() !=null 
				&& bean.getNacBufferAvlBalnc() !=null) {	
			
			Double allocatedAmount = bean.getNacBufferAvlBalnc();
			Double bufferInsuredLimit = Double.parseDouble(txtnacBufferLimit.getValue().replace(",",""));
			if(bufferInsuredLimit > bean.getNacAllocatedLimit()){
				if (bufferInsuredLimit > bean.getNacBufferAvlBalnc()) {
					hasError = true;
					eMsg.append("The NACB buffer limit for Employee is exceeding the NACB buffer balance available for the policy. </br>");
				}
			}				
		} 

		if(hasError) {

			MessageBox.createError()
			.withCaptionCust("Errors").withHtmlMessage(eMsg.toString())
			.withOkButton(ButtonOption.caption("OK")).open();
		} 
		return !hasError;
	}
	
	private void addListener(){
		
//		disEdit.addClickListener(new Button.ClickListener() {
//	        public void buttonClick(ClickEvent event) {
//	        	 if(bean.getStageInformation() !=null){
//	        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
//	        	 }
//	        	 if(bean.getDisBufferAvailBalnc() !=null &&
//	        			 bean.getDisBufferAvailBalnc() <= 0 ){
//	        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
//	        	 }else{
//	        		 btnSubmit.setEnabled(true);
//	        		 txtdisBufferInsuredLimit.setEnabled(true);
//	        		 isdisEditClicked =true;
//	        	 }
//	        } 
//	    });
		
//		wintageEdit.addClickListener(new Button.ClickListener() {
//	        public void buttonClick(ClickEvent event) {
//	        	if(bean.getStageInformation() !=null){
//	        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
//	        	 }
//	        	 if(bean.getWintageBufferAvlBalnc() !=null &&
//	        			 bean.getWintageBufferAvlBalnc() <= 0 ){
//	        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
//	        	 }else{
//	        		 btnSubmit.setEnabled(true);
//	        		 txtwintageBufferLimit.setEnabled(true);
//	        		 iswintageEditClicked = true;
//	        	 }
//	        } 
//	    });
//		
//		nacbEdit.addClickListener(new Button.ClickListener() {
//	        public void buttonClick(ClickEvent event) {
//	        	if(bean.getStageInformation() !=null){
//	        		 SHAUtils.showMessageBox("Claim is in "+bean.getStageInformation(), "Information");
//	        	 }
//	        	 if(bean.getNacBufferAvlBalnc() !=null &&
//	        			 bean.getNacBufferAvlBalnc() <= 0 ){
//	        		 SHAUtils.showMessageBox("Allocated buffer amount utilised fully, can not be change the buffer amount", "Information");
//	        	 }else{
//	        		 btnSubmit.setEnabled(true);
//	        		 txtnacBufferLimit.setEnabled(true);
//	        		 isnacbEditClicked = true;
//	        	 }
//	        } 
//	    });
		
		
		corpBufferAllocatedTab.addSelectedTabChangeListener(event -> {

			if(corpBufferAllocatedTab.getSelectedTab().getId() != null && corpBufferAllocatedTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.DISCRETIONARY_BUFFER_TABID)){
//				if (editBLayout != null
//						&& editBLayout.getComponentCount() > 0) {
//					editBLayout.removeAllComponents();
//				}
//				corpBufferAllocatedTab.getSelectedTab().setEnabled(false);
//			    dummyField.setWidth("49px");
//			    dummyField.setHeight("327px");
//			    editBLayout.addComponent(dummyField);
//			    editBLayout.addComponent(disEdit);
//				addInsuredDetails();
			    
			}else if(corpBufferAllocatedTab.getSelectedTab().getId() != null && corpBufferAllocatedTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.WINTAGE_BUFFER_TABID)){
//				if (editBLayout != null
//						&& editBLayout.getComponentCount() > 0) {
//					editBLayout.removeAllComponents();
//				}
//				
//			    dummyField.setWidth("49px");
//			    dummyField.setHeight("134px");	    
//			    editBLayout.addComponent(dummyField);
//			    editBLayout.addComponent(wintageEdit);
			}else if(corpBufferAllocatedTab.getSelectedTab().getId() != null && corpBufferAllocatedTab.getSelectedTab().getId().equalsIgnoreCase(SHAConstants.NAC_BUFFER_TABID)){
//				if (editBLayout != null
//						&& editBLayout.getComponentCount() > 0) {
//					editBLayout.removeAllComponents();
//				}
				
//			    dummyField.setWidth("49px");
//			    dummyField.setHeight("134px");
//			    editBLayout.addComponent(dummyField);
//			    editBLayout.addComponent(nacbEdit);
			}

		});
	}
	
	public void buildSucessLayout(){
		
		Label successLabel = new Label("<b style = 'color: black;'>Corporate Buffer has been allocated successfully</b>", ContentMode.HTML);
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				if (null != screenName && (SHAConstants.WAIT_FOR_INPUT_SCREEN.equalsIgnoreCase(screenName))) {
					fireViewEvent(MenuItemBean.ALLOCATE_CORP_BUFFER, null);
				}
				else {
					fireViewEvent(MenuItemBean.ALLOCATE_CORP_BUFFER, null);
				}
			}
		});
	}
	
	public AllocateCorpBufferDetailDTO getValues() {
		
		try {
			this.binder.commit();
			AllocateCorpBufferDetailDTO bean = this.binder.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private BlurListener getwintageAvlBalncListener() {
		BlurListener listener = new BlurListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {
				TextField value = (TextField) event.getComponent();
				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
					if(txtwintageBufferAvlBalnc != null && txtwintageBufferAvlBalnc.getValue() != null && !txtwintageBufferAvlBalnc.getValue().isEmpty()){
						Double limitAmt = Double.valueOf(value.getValue().replace(",", ""));
						Double avlBalnc = Double.valueOf(txtwintageBufferAvlBalnc.getValue().replace(",", ""));
						if (limitAmt > avlBalnc) {
							SHAUtils.showMessageBox("The Vintage buffer limit for employee is exceeding the Vintage buffer balance available for policy.", "Information - VINTAGE BUFFER");
							txtwintageBufferLimit.setValue("");
							txtwintageAvlBalnc.setReadOnly(false);
							txtwintageAvlBalnc.setValue(txtwintageBufferAvlBalnc.getValue());
							txtwintageAvlBalnc.setReadOnly(true);
						}
						else{
							Double netAmt = avlBalnc - limitAmt;					
							if(txtwintageAvlBalnc != null){
								txtwintageAvlBalnc.setReadOnly(false);
								txtwintageAvlBalnc.setValue(netAmt.toString());
								txtwintageAvlBalnc.setReadOnly(true);
							}	
						}			
					}				
				}					
			}
		};
		return listener;
	}
	
//	private BlurListener getNACAvlBalncListener() {
//		BlurListener listener = new BlurListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void blur(BlurEvent event) {
//				TextField value = (TextField) event.getComponent();
//				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
//					if(txtnacAvlBalnc != null && txtnacAvlBalnc.getValue() != null && !txtnacAvlBalnc.getValue().isEmpty()){
//						Double limitAmt = Double.valueOf(value.getValue().replace(",", ""));
//						Double avlBalnc = Double.valueOf(txtnacAvlBalnc.getValue().replace(",", ""));
//						if (limitAmt > avlBalnc) {
//							SHAUtils.showMessageBox("The NACB buffer limit for employee is exceeding the buffer Limit for insured.", "Information - WINTAGE BUFFER");
//							txtnacBufferLimit.setValue("");
//							txtnacBufferAvlBalnc.setReadOnly(false);
//							txtnacBufferAvlBalnc.setValue(txtnacAvlBalnc.getValue());
//							txtnacBufferAvlBalnc.setReadOnly(true);
//						}
//						else{
//							Double netAmt = avlBalnc - limitAmt;		
//							if(txtnacBufferAvlBalnc != null){
//								txtnacBufferAvlBalnc.setReadOnly(false);
//								txtnacBufferAvlBalnc.setValue(netAmt.toString());
//								txtnacBufferAvlBalnc.setReadOnly(true);
//							}	
//						}				
//					}				
//				}					
//			}
//		};
//		return listener;
//	}
	
//	private BlurListener getdisAvlBalncListener() {
//		BlurListener listener = new BlurListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void blur(BlurEvent event) {
//				TextField value = (TextField) event.getComponent();
//				if(value != null && value.getValue() != null && !value.getValue().isEmpty()){
//					if(bean != null && bean.getDisAvlBalnc() != null && bean.getDisAvlBalnc() > 0){
//						Double bufferAllocatedAmount = bean.getMaxdisBufferInsuredLimit();
//						Double limitAmt = Double.valueOf(value.getValue().replace(",", ""));
//						Double avlBalnc = bean.getMaxdisBufferInsuredLimit();
//
//							if (limitAmt > bean.getMaxdisBufferInsuredLimit()) {
//								SHAUtils.showMessageBox("The discretionary buffer limit for employee is exceeding the buffer Limit for insured.", "Information - DISCRETIONARY BUFFER");
//								txtdisBufferInsuredLimit.setValue("");
//								txtdisBufferAvailBalnc.setReadOnly(false);
//								txtdisBufferAvailBalnc.setValue(bean.getDisAvlBalnc().toString());
//								txtdisBufferAvailBalnc.setReadOnly(true);
//							}
//							else{
//								Double netAmt = avlBalnc - limitAmt;
//
//								if(txtdisBufferAvailBalnc != null){
//									txtdisBufferAvailBalnc.setReadOnly(false);
//									txtdisBufferAvailBalnc.setValue(netAmt.toString());
//									txtdisBufferAvailBalnc.setReadOnly(true);
//								}							
//							}
//						}
//				}					
//			}
//		};
//		return listener;
//	}
	
//	private void addInsuredDetails(){
//		
//		binder.unbind(txtinsureName);
//		binder.unbind(txtinsurerelationShip);
//		binder.unbind(txtinsureAge);
//		txtinsureName = (TextField) binder.buildAndBind("Employee Name","insureName", TextField.class);
//		txtinsurerelationShip = (TextField) binder.buildAndBind("Relationship","insurerelationShip", TextField.class);
//		txtinsureAge = (TextField) binder.buildAndBind("Employee age","insureAge", TextField.class);
//		
//		formCBLayout.removeAllComponents();
//		hLayout.removeAllComponents();
//		
//		formCBLayout =new FormLayout(txtPolicyLabel,txtCorpBufferSI,txtCorpUtilizedAmt,txtpolicywisedisBufferAvlBlnc,txtInsuredLabel
//				,txtinsureName,txtinsurerelationShip,txtinsureAge,txtdisBufferIndviSI,txtdisBufferApplTo,txtNoOfTimesSi,
//				txtmaxdisBufferInsuredLimit,txtdisBufferInsuredLimit,txtdisBufferUtilizedAmt,txtdisBufferAvailBalnc);
//		formCBLayout.setMargin(true);
//		formCBLayout.setSpacing(true);
//		formCBLayout.addStyleName("layoutDesign");
//		formCBLayout.addComponent(chkdisupdateCorpBuffer);
//		formCBLayout.setComponentAlignment(chkdisupdateCorpBuffer, Alignment.MIDDLE_CENTER);
//		setReadOnly(formCBLayout, true);
//		hLayout.addComponent(formCBLayout);	
//	}
	
	}

