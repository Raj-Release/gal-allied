package com.shaic.reimbursement.paymentprocess.initiateRecovery;

import java.util.List;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class PaymentInitiateRecoverySearchForm extends SearchComponent<PaymentInitiateRecoverySearchFormDTO>{
	
	private TextField txtintimationNumber;
	private ComboBox cmbdocumentReceivedFrom;
	
	private Button resetBtn;
	
	public void init() {
		initBinder();
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Initiate Recovery");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	private void initBinder(){
		this.binder = new BeanFieldGroup<PaymentInitiateRecoverySearchFormDTO>(PaymentInitiateRecoverySearchFormDTO.class);
		this.binder.setItemDataSource(new PaymentInitiateRecoverySearchFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout mainVerticalLayout(){
		mainVerticalLayout = new VerticalLayout();
		
		txtintimationNumber = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		cmbdocumentReceivedFrom = binder.buildAndBind("Document Received From","documentReceivedFrom",ComboBox.class);
		
		
		FormLayout formFirst = new FormLayout(txtintimationNumber);
		formFirst.setSpacing(false);
		formFirst.setMargin(false);
		
		FormLayout formSecond = new FormLayout(cmbdocumentReceivedFrom);
		formSecond.setSpacing(false);
		formSecond.setMargin(false);
		
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formFirst,formSecond);
//		HorizontalLayout fieldLayout = new HorizontalLayout(formSecond);
		
		fieldLayout.setSpacing(true);
		fieldLayout.setMargin(false);
		//fieldLayout.setWidth("100%");
		
		
		AbsoluteLayout search_layout =  new AbsoluteLayout();
		search_layout.addComponent(fieldLayout, "left: 10px; top: 40px;");
//		search_layout.addComponent(fieldLayout, "left: 10px; top: 30px;");
		search_layout.addComponent(btnSearch, "top:150.0px;left:320.0px;");
		search_layout.addComponent(resetBtn, "top:150.0px;left:429.0px;");
		
		mainVerticalLayout.addComponent(search_layout);
		//mainVerticalLayout.setWidth("1100px");
		mainVerticalLayout.setMargin(false);
		mainVerticalLayout.setSpacing(false);
		//mainVerticalLayout.setHeight("200");
		
		search_layout.setWidth("100.0%");
		search_layout.setHeight("210px");
		
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
		
		BeanItemContainer<SelectValue> documentReceivedFrmcontainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue insuredselectVal = new SelectValue();
		insuredselectVal.setId(ReferenceTable.RECEIVED_FROM_INSURED);
		insuredselectVal.setValue(SHAConstants.DOC_RECEIVED_FROM_INSURED);
		SelectValue hospitalselectVal = new SelectValue();
		hospitalselectVal.setId(ReferenceTable.DOC_RECEIVED_TYPE_HOSPITAL);
		hospitalselectVal.setValue(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL);
		documentReceivedFrmcontainer.addBean(hospitalselectVal);
		documentReceivedFrmcontainer.addBean(insuredselectVal);
		cmbdocumentReceivedFrom.setContainerDataSource(documentReceivedFrmcontainer);
		cmbdocumentReceivedFrom.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbdocumentReceivedFrom.setItemCaptionPropertyId("value");
		
	}
	
	
	
	public void resetAlltheValues(){
		txtintimationNumber.setValue("");
		cmbdocumentReceivedFrom.setValue("");
	}
	
	public String validate(PaymentInitiateRecoverySearchFormDTO searchDTO) {

		String err = null;
		
		if(null != searchDTO){
			
			if(txtintimationNumber.getValue() == null || txtintimationNumber.getValue().equalsIgnoreCase(""))		
			{
				return err = "Please Enter Intimation No";
			}
			

			if(null != cmbdocumentReceivedFrom && null == cmbdocumentReceivedFrom.getValue() )
			{
				err = "Please select any one value from Document Received From dropdown";
			}
		
		}
		return err;
	
	}

}