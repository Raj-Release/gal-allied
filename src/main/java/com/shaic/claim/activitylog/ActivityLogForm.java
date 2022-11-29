package com.shaic.claim.activitylog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SearchComponent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ActivityLogForm extends SearchComponent<ActivityLogFormDto> {
	private static final long serialVersionUID = 1L;
	
	private TextField empIdNameTextField;
	private TextField intimationNoTextField;
	private PopupDateField activityDate;
	
	@PostConstruct
	public void init() {
		initBinder();
		setCompositionRoot(mainVerticalLayout());
	}
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<ActivityLogFormDto>(ActivityLogFormDto.class);
		this.binder.setItemDataSource(new ActivityLogFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}
	
	public VerticalLayout mainVerticalLayout() {
		mainVerticalLayout = new VerticalLayout();

		empIdNameTextField = binder.buildAndBind("Employee Name", "empIdName", TextField.class);
		empIdNameTextField.setEnabled(true);
		empIdNameTextField.setNullRepresentation("");
		empIdNameTextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intimationNoTextField = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		intimationNoTextField.setEnabled(true);
		intimationNoTextField.setNullRepresentation("");
		intimationNoTextField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		activityDate = binder.buildAndBind("Activity Date", "activityDate", PopupDateField.class);
		activityDate.setWidth("200px");
		activityDate.setDateFormat("dd-MM-yyyy hh:mm a");
		activityDate.setInputPrompt("DD-MM-YYYY HH:MM A");
		activityDate.setLocale(Locale.ENGLISH);
		activityDate.setResolution(Resolution.MINUTE);
		activityDate.setTextFieldEnabled(false);
		
		FormLayout formLayoutLeft = new FormLayout(empIdNameTextField, activityDate);
		FormLayout formLayoutRight = new FormLayout(intimationNoTextField);
		formLayoutLeft.setSpacing(false);
		formLayoutRight.setSpacing(false);
		formLayoutLeft.setMargin(false);
		formLayoutRight.setMargin(false);
		
		HorizontalLayout mainHLayout = new HorizontalLayout(formLayoutLeft, formLayoutRight);
		mainHLayout.setSpacing(false);
		mainHLayout.setMargin(false);
		mainHLayout.setWidth("100%");
		
		mainVerticalLayout.addComponent(mainHLayout);
		//Vaadin8-setImmediate() mainVerticalLayout.setImmediate(true);
		mainVerticalLayout.setWidth("100%");
		mainVerticalLayout.setSpacing(false);
		mainVerticalLayout.setMargin(false);		 

		return mainVerticalLayout;
	}
	
	public void setActivityLogFormValues(String intimationNo, String empId, String empName, Date currentDate, Integer diff) {
		empIdNameTextField.setReadOnly(false);
		intimationNoTextField.setReadOnly(false);
		activityDate.setEnabled(true);
		
		empIdNameTextField.setValue(empId + " - " + empName);
		intimationNoTextField.setValue(intimationNo);
		activityDate.setValue(currentDate);
		
		empIdNameTextField.setReadOnly(true);
		intimationNoTextField.setReadOnly(true);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(currentDate); 
		c.add(Calendar.DATE, -diff);
		activityDate.setRangeStart(c.getTime());
		activityDate.setRangeEnd(currentDate);
	}
	
	public Date getActivityDate() {
		return activityDate.getValue();
	}
	
	public void setActivityDate(Date activityDate) {
		this.activityDate.setValue(activityDate);
		this.activityDate.setEnabled(false);
	}
}
