package com.shaic.claim.reports.paymentpendingdashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.SearchComponent;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;

public class PaymentPendingDashboardForm extends SearchComponent<PaymentPendingDashboardFormDTO>{
	private Label paymentLabel;
	private Button xmlReport;
	private PaymentPendingDashboardFormDTO bean;
	
	@PostConstruct
	public void init() {
		
		Panel mainPanel = new Panel();
		mainPanel.addStyleName("panelHeader");
		mainPanel.addStyleName("g-search-panel");
		mainPanel.setCaption("Payment Pending Dashboard");
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
		 
		paymentLabel= new Label("Payment Pending > 5 days");
		xmlReport = new Button("Export To Excel");
		xmlReport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		mainVerticalLayout = new VerticalLayout();
		
		FormLayout formLayoutRight = new FormLayout(paymentLabel);
		formLayoutRight.setSpacing(true);
		
	
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutRight);
		fieldLayout.setMargin(true);
		fieldLayout.setSpacing(true);
		
		HorizontalLayout buttonhLayout = new HorizontalLayout(xmlReport);
		
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
					
					
					PaymentPendingDashboardFormDTO bean= setDate();
					if(bean != null)
					{
						String userName = (String) getUI().getSession().getAttribute(
								BPMClientContext.USERID);
						String passWord = (String) getUI().getSession().getAttribute(
								BPMClientContext.PASSWORD);
						fireViewEvent(PaymentPendingDashboardPresenter.GENERATE_PAYMENT_PENDING_DASHBOARD_REPORT,
								bean, userName, passWord);
					}
					
			}
		});
	}
	
	public PaymentPendingDashboardFormDTO  setDate()
	{
		bean = new PaymentPendingDashboardFormDTO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = dateFormat.format(new Date());
		System.out.println("Format date:" + formattedDate);
		Date date=null;
		try {
			date=dateFormat.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 bean.setDate(date);
			 System.out.println("DATE:"+date);
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
	

}


