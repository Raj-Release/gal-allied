package com.shaic.claim.lumen.components;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.domain.MasterService;
import com.shaic.domain.TmpEmployee;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("serial")
public class MISQueryInitiationDetails extends ViewComponent{
	
	private TextField employeeName;
	private TextField employeeId;
	private TextField queryDate;

	HorizontalLayout layout1;
	
	private MISQueryReplyDTO dtoObj;
	
	@Inject
	MasterService masterService;

	@PostConstruct
	public void initView() {

	}

	public void init(MISQueryReplyDTO resultDTO, String caption) {
		this.dtoObj = resultDTO;
		Panel panel = new Panel(caption);
		panel.setHeight("180px");
		panel.setWidth("100%");
		panel.setContent(buildCarousel());

		enableOrDisableFields(false);
		enableOrDisableFields(true);
		setCompositionRoot(panel);
	}

	private void enableOrDisableFields(boolean flag){
		employeeName.setReadOnly(flag);
		employeeId.setReadOnly(flag);
		queryDate.setReadOnly(flag);
	}

	private FormLayout buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(true);
		firstForm.setMargin(true);
		firstForm.setWidth("100%");		

		employeeName = new TextField("Query Raised Employee Name");
		TmpEmployee employeeObj = masterService.getEmployeeNameWithInactiveUser(dtoObj.getQueryRaisedBy());
		employeeName.setValue(employeeObj.getEmpFirstName());
		//Vaadin8-setImmediate() employeeName.setImmediate(true);
		employeeName.setWidth("-1px");
		employeeName.setReadOnly(true);

		employeeId = new TextField("Query Raised Employee ID");
		employeeId.setValue(dtoObj.getQueryRaisedBy());
		//Vaadin8-setImmediate() employeeId.setImmediate(true);
		employeeId.setWidth("-1px");
		employeeId.setReadOnly(true);

		queryDate = new TextField("Query Raised Date");
		if(dtoObj.getQueryRaisedDate() != null){
			queryDate.setValue(DateFormatUtils.format(dtoObj.getQueryRaisedDate(), "MMM, dd, yyyy HH:mm:ss a")); //"yyyy-MM-dd HH:mm:SS"
		}else{
			queryDate.setValue("");
		}
		//Vaadin8-setImmediate() queryDate.setImmediate(true);
		queryDate.setWidth("-1px");
		queryDate.setReadOnly(true);

		firstForm.addComponent(employeeId);
		firstForm.addComponent(employeeName);
		firstForm.addComponent(queryDate);
		
		return firstForm;
	}
	
}
