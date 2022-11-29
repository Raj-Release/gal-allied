package com.shaic.claim.reports.automationdashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SearchComponent;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

public class AutomationDashboardForm extends SearchComponent<AutomationDashboardFormDTO>{
	private PopupDateField dateField;
	private Button xmlReport;
	private Button resetBtn;
	private AutomationDashboardFormDTO bean;
	
	@PostConstruct
	public void init() {
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Automation Dashboard");
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
		xmlReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mainVerticalLayout = new VerticalLayout();
		
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		dateField = new PopupDateField("Date");
		dateField.setDateFormat("dd/MM/yyyy");
		 Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, -7);
	        Date reportdate = cal.getTime();  
	    dateField.setRangeStart(reportdate);
		dateField.setRangeEnd(new Date());
		dateField.setTextFieldEnabled(false);
		
		/*LocalDate now = LocalDate.now(ZoneId.systemDefault());

		dateField.setMin(now.minusDays(7));
		dateField.setMax(now);*/
		//dateField.setHelperText("Must be within 60 days from today");

		
		FormLayout formLayoutRight = new FormLayout(dateField);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		
		HorizontalLayout buttonhLayout = new HorizontalLayout(xmlReport,resetBtn);
		
		buttonhLayout.setSpacing(true);
		
		VerticalLayout btnLayout = new VerticalLayout();
		btnLayout.setWidth("70%");
		btnLayout.addComponent(buttonhLayout);
		btnLayout.setComponentAlignment(buttonhLayout,  Alignment.MIDDLE_LEFT);
		absoluteLayout_3.addComponent(fieldLayout);
		
		absoluteLayout_3.addComponent(btnLayout, "top:80.0px;left:299.0px;");
		//absoluteLayout_3.addComponent(resetBtn, "top:80.0px;left:299.0px;");
		mainVerticalLayout.addComponent(absoluteLayout_3);
		
		addReportListener();
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetFields();
			}
		});
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
					
					
					AutomationDashboardFormDTO bean= validate();
					if(bean != null)
					{
//					DailyReportFormDTO hospitalDTO = dailyReportForm.getSearchDTO();
						String userName = (String) getUI().getSession().getAttribute(
								BPMClientContext.USERID);
						String passWord = (String) getUI().getSession().getAttribute(
								BPMClientContext.PASSWORD);
						fireViewEvent(AutomationDashboardPresenter.GENERATE_AUTOMATION_DASHBOARD_REPORT,
								bean, userName, passWord);
					}
					
					
					//getTableDataForReport();
				
			}
		});
	}
	
	public AutomationDashboardFormDTO  validate()
	{
		String err = "";
		bean = new AutomationDashboardFormDTO();
		
		if(dateField.getValue() == null)
		{
			err= "Please Select the  Date.";
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
	
	
	public void resetFields(){		
	dateField.setValue(null);
	//vLayout.removeAllComponents();	
}



}

