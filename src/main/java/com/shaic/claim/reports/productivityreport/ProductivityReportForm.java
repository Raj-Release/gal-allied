package com.shaic.claim.reports.productivityreport;

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

public class ProductivityReportForm extends SearchComponent<ProductivityReportFormDTO>{
	private PopupDateField dateField;
	private ComboBox cmbClaimType;
	private Button xmlReport;
	private ProductivityReportFormDTO bean;
	//private HorizontalLayout buttonlayout;
	
	@PostConstruct
	public void init() {
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Productivity Report");
		mainPanel.setContent(mainVerticalLayout());
		setCompositionRoot(mainPanel);
	}
	
	
	public VerticalLayout mainVerticalLayout(){
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 mainVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() mainVerticalLayout.setImmediate(false);
		 mainVerticalLayout.setWidth("100.0%");
		 mainVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("149px");
		 
		 
		xmlReport = new Button("Export To Excel");
		mainVerticalLayout = new VerticalLayout();
		
		dateField = new PopupDateField("Date");
		dateField.setDateFormat("dd/MM/yyyy");
		dateField.setTextFieldEnabled(false);
		cmbClaimType = new ComboBox("Claim Type");
		cmbClaimType.setEnabled(false);
		dateField.setValidationVisible(false);
		
		FormLayout formLayoutLeft = new FormLayout(cmbClaimType);
		formLayoutLeft.setSpacing(true);
		FormLayout formLayoutRight = new FormLayout(dateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		absoluteLayout_3.addComponent(fieldLayout);
		absoluteLayout_3.addComponent(xmlReport, "top:80.0px;left:299.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addReportListener();
		return mainVerticalLayout;
	}
	
	private void addReportListener()
	{
		xmlReport.addClickListener(new ClickListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;


				@Override
				public void buttonClick(ClickEvent event) {
					
					
					ProductivityReportFormDTO bean= validate();
					if(bean != null)
					{
//					DailyReportFormDTO hospitalDTO = dailyReportForm.getSearchDTO();
						String userName = (String) getUI().getSession().getAttribute(
								BPMClientContext.USERID);
						String passWord = (String) getUI().getSession().getAttribute(
								BPMClientContext.PASSWORD);
						fireViewEvent(ProductivityReportPresenter.GENERATE_REPORT,
								bean, userName, passWord);
					}
					
					
					//getTableDataForReport();
				
			}
		});
	}
	
	public ProductivityReportFormDTO  validate()
	{
		String err = "";
		bean = new ProductivityReportFormDTO();
		
		if(dateField.getValue() == null)
		{
			err= "Select Date.";
			showErrorMessage(err);	
			return null;
		}
		else
		{
			 bean.setDate(dateField.getValue());
		}
		return bean;
		
	}
	private void showErrorMessage(String eMsg) {
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
	
	
	/*private void initBinder()
	{
		this.binder = new BeanFieldGroup<DailyReportFormDTO>(DailyReportFormDTO.class);
		this.binder.setItemDataSource(new DailyReportFormDTO());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
*/

	public void setDropDownValues(){
		
		BeanItemContainer<SelectValue> clmTypeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue cashless = new SelectValue(null,SHAConstants.CASHLESS_CLAIM_TYPE);
		clmTypeContainer.addBean(cashless);
		
		cmbClaimType.setContainerDataSource(clmTypeContainer);
		cmbClaimType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbClaimType.setItemCaptionPropertyId("value");
		
		cmbClaimType.setValue(clmTypeContainer.getItemIds().get(0));
	}

}
