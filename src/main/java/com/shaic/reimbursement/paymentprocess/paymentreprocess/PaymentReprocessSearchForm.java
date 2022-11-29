package com.shaic.reimbursement.paymentprocess.paymentreprocess;

import java.util.List;

import javax.inject.Inject;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("deprecation")
public class PaymentReprocessSearchForm extends SearchComponent<PaymentReprocessSearchFormDTO>{

	private static final long serialVersionUID = 8009759599505456128L;

	private ComboBox cmbtype;
	private TextField txtIntimationNumber;

	private TextField txtUPRId;
	private TextField txtUTRNo;
	private TextField txtCPUCode;
	private TextField txtPaymentCPU;
	private TextField txtClaimType;
	private ComboBox cmbClaimant;
	private ComboBox cmbPaymentMode;
	private ComboBox cmbReprocessType;
	
	private Button resetBtn;
	private PaymentReprocessSearchViewImpl viewImp;
	
	public PaymentReprocessSearchViewImpl getViewImp() {
		return viewImp;
	}

	public void setViewImp(PaymentReprocessSearchViewImpl viewImp) {
		this.viewImp = viewImp;
	}

	@Inject
	public PaymentReprocessDBService dbService;
	
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Payment Re-Process");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}

	private void initBinder(){
		this.binder = new BeanFieldGroup<PaymentReprocessSearchFormDTO>(PaymentReprocessSearchFormDTO.class);
		this.binder.setItemDataSource(new PaymentReprocessSearchFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();
		cmbtype = binder.buildAndBind("Type","type",ComboBox.class);
		txtIntimationNumber = binder.buildAndBind("Intimation No", "intimationNumber", TextField.class);
		txtUPRId = binder.buildAndBind("UPR ID", "uprId", TextField.class);
		txtUTRNo = binder.buildAndBind("UTR NO/Instrument No", "utrNo", TextField.class);
		txtCPUCode = binder.buildAndBind("CPU", "cpuCode", TextField.class);
		txtPaymentCPU = binder.buildAndBind("Payment CPU", "paymentCPU", TextField.class);
		txtClaimType = binder.buildAndBind("Claim Type", "claimType", TextField.class);
		cmbClaimant = binder.buildAndBind("Claimant","claimant",ComboBox.class);
		cmbPaymentMode = binder.buildAndBind("Original Payment Mode","paymentMode",ComboBox.class);
		cmbReprocessType = binder.buildAndBind("Re-Process Type","reprocessType",ComboBox.class);
		
		FormLayout formFirst = new FormLayout(cmbtype);
		formFirst.setSpacing(false);
		formFirst.setMargin(false);
		
		FormLayout formSecond = new FormLayout(txtIntimationNumber, txtCPUCode, cmbClaimant);
		formSecond.setSpacing(false);
		formSecond.setMargin(false);
		
		FormLayout formThird = new FormLayout(txtUPRId, txtPaymentCPU, cmbPaymentMode);
		formThird.setSpacing(false);
		formThird.setMargin(false);
		
		FormLayout formFourth = new FormLayout(txtUTRNo, txtClaimType, cmbReprocessType);
		formFourth.setSpacing(false);
		formFourth.setMargin(false);
		
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout fieldLayout2 = new HorizontalLayout(formFirst);
		HorizontalLayout fieldLayout = new HorizontalLayout(formSecond,formThird,formFourth);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		//fieldLayout.setWidth("100%");
		
		
		AbsoluteLayout search_layout =  new AbsoluteLayout();
		search_layout.addComponent(fieldLayout2, "left: 10px; top: 3px;");
		search_layout.addComponent(fieldLayout, "left: 10px; top: 30px;");
		search_layout.addComponent(btnSearch, "top:150.0px;left:320.0px;");
		search_layout.addComponent(resetBtn, "top:150.0px;left:429.0px;");
		
		mainVerticalLayout.addComponent(search_layout);
		//mainVerticalLayout.setWidth("1100px");
		mainVerticalLayout.setMargin(false);
		mainVerticalLayout.setSpacing(false);
		//mainVerticalLayout.setHeight("200");
		
		search_layout.setWidth("100.0%");
		search_layout.setHeight("240px");
		
		btnSearch.setCaption(SearchComponent.SEARCH_TASK_CAPTION);
		btnSearch.setDisableOnClick(true);
		
		btnSearch.setWidth("-1px");
		btnSearch.setTabIndex(12);
		btnSearch.setHeight("-1px");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		addListener();
		setDropDownValues();
		return mainVerticalLayout;
	}
	
	public void addListener() {
		btnSearch.removeClickListener(this);
		btnSearch.addClickListener(this);

		resetBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				resetAlltheValues();	
			}
		});		
	}
	
	@SuppressWarnings({ "unchecked" })
	public void setDropDownValues(){
		BeanItemContainer<SelectValue> paymentModeContainer = dbService.getPaymentMode();
		cmbPaymentMode.setContainerDataSource(paymentModeContainer);
		cmbPaymentMode.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbPaymentMode.setItemCaptionPropertyId("value");
		List<SelectValue> payModeType = (List<SelectValue>)cmbPaymentMode.getContainerDataSource().getItemIds();
		SelectValue selectedVal = payModeType.get(0);
		cmbPaymentMode.setValue(selectedVal);
		cmbPaymentMode.setReadOnly(true);
		
		BeanItemContainer<SelectValue> claimantContainer = dbService.getClaimant();
		cmbClaimant.setContainerDataSource(claimantContainer);
		cmbClaimant.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimant.setItemCaptionPropertyId("value");
		List<SelectValue> claimantType = (List<SelectValue>)cmbClaimant.getContainerDataSource().getItemIds();
		cmbClaimant.setValue(claimantType.get(0));
		cmbClaimant.setReadOnly(true);
		
		BeanItemContainer<SelectValue> typeContainer = dbService.getMastersValuebyTypeCode("PAYRPTYPE");
		cmbtype.setContainerDataSource(typeContainer);
		cmbtype.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbtype.setItemCaptionPropertyId("value");
		List<SelectValue> type = (List<SelectValue>)cmbtype.getContainerDataSource().getItemIds();
		cmbtype.setValue(type.get(0));
		cmbtype.setReadOnly(true);
		
		BeanItemContainer<SelectValue> rpContainer = dbService.getMastersValuebyTypeCode("PAYRPSUBTYPE");
		cmbReprocessType.setContainerDataSource(rpContainer);
		cmbReprocessType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbReprocessType.setItemCaptionPropertyId("value");
		List<SelectValue> rpTypeList = (List<SelectValue>)cmbReprocessType.getContainerDataSource().getItemIds();
		cmbReprocessType.setValue(rpTypeList.get(0));
	}
	
	public void resetAlltheValues(){
		txtIntimationNumber.setValue("");
		txtUPRId.setValue("");
		txtUTRNo.clear();
		txtCPUCode.clear();
		txtPaymentCPU.clear();
		txtClaimType.clear();
		cmbReprocessType.clear();
		/*if(lumenTable != null){
			if(lumenTable.getPageable()==null)
				lumenTable.init("", false, false);
			lumenTable.getPageable().setPageNumber(1);
			lumenTable.resetTable();
		}*/
	}

	public String validate(PaymentReprocessSearchFormDTO searchDTO) {

		String err = null;
		
		if(null != searchDTO){
			
			if(txtIntimationNumber.getValue() == null || txtIntimationNumber.getValue().equalsIgnoreCase(""))		
			{
				return err = "Please Enter Intimation No";
			}
				if((txtUPRId.getValue()==null || txtUPRId.getValue().isEmpty()) &&
						(txtUTRNo.getValue()==null || txtUTRNo.getValue().isEmpty())){
					return err = "Please Enter UPR ID</br> Or</br>Please Enter UTR NO/Instrument No";
				}

		if(null != cmbReprocessType && null == cmbReprocessType.getValue() )
		{
			err = "Please select any one value from Re-Process Type dropdown";
		}
		
	}
		return err;
	
	}

}
