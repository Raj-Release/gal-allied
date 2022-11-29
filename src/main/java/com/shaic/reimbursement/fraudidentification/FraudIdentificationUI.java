package com.shaic.reimbursement.fraudidentification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.reimbursement.rrc.services.SearchRRCRequestTableDTO;
import com.shaic.claim.reports.PolicywiseClaimReportDto;
import com.shaic.claim.reports.fraudIdentificationReport.FraudIdentificationReportTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class FraudIdentificationUI extends ViewComponent{
	


	private static final long serialVersionUID = 1L;
	
	
	private ComboBox parameterValue;
	
	private Button btnSearch;
	
	private Button btnReset;
	
	private Button btnSubmit;
	
	private Button btnCancel;
	
	private Button btnExport;
	
	private Label dummyLabel;

	private VerticalLayout mainLayout;
	
	private VerticalLayout generatedLayout;

	private Panel mainPanel;
	
	@Inject
	private Instance<FraudIdentificationTable> fraudIdentificationTable;
	
	private FraudIdentificationTable fraudIdentificationTableObj;
	
	/*@Inject
	private FraudIdentificationReportTable tableForExcel;
	*/
	private ExcelExport excelExport;
	
	public void init() {
		fireViewEvent(FraudIdentificationPresenter.LOAD_PARAMETER_VALUES, null);
	}
	
	public void initView() {
		mainPanel = new Panel();
		mainLayout = new VerticalLayout();
		generatedLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100.0%");
		mainLayout.setMargin(false);
		
		parameterValue = new ComboBox("Parameter Type");
		//Vaadin8-setImmediate() parameterValue.setImmediate(true);
		parameterValue.setHeight("-1px");
		
		FormLayout mainForm = new FormLayout(parameterValue);
		
		// Search Button
				btnSearch = new Button();
				btnSearch.setCaption("Search");
				btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
				btnSearch.setWidth("-1px");
				btnSearch.setHeight("-1px");
				btnSearch.setTabIndex(3);
				//Vaadin8-setImmediate() btnSearch.setImmediate(true);

				btnSearch.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 5677998363425252239L;

					@Override
					public void buttonClick(ClickEvent event) {
						if(parameterValue.getValue() != null && !parameterValue.isEmpty()) {
							fireViewEvent(FraudIdentificationPresenter.SEARCH_FRAUD_IDENTIFICATION, parameterValue.getValue());
						} else {
							Label label = new Label("Please Select Parameter Type.", ContentMode.HTML);
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
				});
				
			
		
		// Reset Button
				btnReset = new Button();
				btnReset.setCaption("Reset");
				btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
				btnReset.setWidth("-1px");
				btnReset.setHeight("-1px");
				btnReset.setTabIndex(4);
				//Vaadin8-setImmediate() btnReset.setImmediate(true);
				btnReset.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						fireViewEvent(MenuItemBean.FRAUD_IDENTIFICATION,null);
					}
				});
				
				Label mandatoryLabel = new Label("<b style = 'color: red;'>*Seperate the email ids by using semicolon(;)</b>", ContentMode.HTML);
				FormLayout formLabelLayout = new FormLayout(mandatoryLabel);
				
				VerticalLayout addlayout = new VerticalLayout();
				addlayout.setMargin(true);
				
				// Button Layout
				HorizontalLayout btnHLayout = new HorizontalLayout(btnSearch, btnReset);
				btnHLayout.setWidth("100%");
				btnHLayout.setHeight("40px");
				btnHLayout.setMargin(false);
				btnHLayout.setSpacing(true);
				btnHLayout.setComponentAlignment(btnSearch, Alignment.BOTTOM_RIGHT);
				btnHLayout.setComponentAlignment(btnReset, Alignment.BOTTOM_LEFT);
				
				mainLayout.setMargin(true);
				mainLayout.setSpacing(false);
				mainLayout.addComponent(mainForm);
				mainLayout.addComponent(btnHLayout);
				mainLayout.addComponent(addlayout);
				mainLayout.addComponent(generatedLayout);
				
				mainPanel.setWidth("100%");
				mainPanel.setCaption("Fraud Identification");
				mainPanel.addStyleName("panelHeader");
				mainPanel.addStyleName("g-search-panel");
				//Vaadin8-setImmediate() mainPanel.setImmediate(false);
				mainPanel.setContent(mainLayout);
				setCompositionRoot(mainPanel);
			
	}
	

	private void addButonClickListener() {
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean hasError=false;
				if(fraudIdentificationTableObj.getValues()==null || fraudIdentificationTableObj.getValues().isEmpty()){
					hasError=true;
				}
				
				if(validatePage(hasError)){
					fireViewEvent(FraudIdentificationPresenter.SUBMIT_FRAUD_IDENTIFICATION, fraudIdentificationTableObj.getValues());
					
				}
			}
		});
		
		
		
	}
	
	private void addCancelButtonListener() {
		btnCancel.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog dialog = ConfirmDialog
						.show(getUI(),
								"Confirmation",
								"Are you sure you want to cancel ?",
								"No", "Yes", new ConfirmDialog.Listener() {

									public void onClose(ConfirmDialog dialog) {
										if (!dialog.isConfirmed()) {
											fireViewEvent(MenuItemBean.FRAUD_IDENTIFICATION,
													null);
										} else {
											// User did not confirm
										}
									}
								});

				dialog.setClosable(false);
				dialog.setStyleName(Reindeer.WINDOW_BLACK);
			}
		});
	}
	
	/*public void resetAlltheValues(){

		fraudIdentificationReportTableObj.setTableList(new ArrayList<FraudIdentificationTableDTO>());
		if(btnExport != null){
			generatedLayout.removeComponent(btnExport);
			generatedLayout.removeComponent(dummyLabel);
			btnExport = null;
		}
	}*/
	
	private void addExportButton() {
		btnExport = new Button("Export to Excel");
		btnExport.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				/*Boolean chkBoxValue = false;
				List<FraudIdentificationTableDTO> requestTableList = new ArrayList<FraudIdentificationTableDTO>();
				requestTableList = fraudIdentificationTableObj.getValues();
				getTableDataForReport(requestTableList);*/
				
				excelExport = new ExcelExport(fraudIdentificationTableObj.getTable());
				excelExport.excludeCollapsedColumns();
				excelExport.setReportTitle("Fraud Identification Details");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		
		
	}
	public void generateTableForFraudIdentification(List<FraudIdentificationTableDTO> dtoList) {
		generatedLayout.removeAllComponents();
		if(dtoList != null && !dtoList.isEmpty()) {
			if(fraudIdentificationTableObj == null) {
				fraudIdentificationTableObj = fraudIdentificationTable.get();
			}
						
			fraudIdentificationTableObj.init(dtoList.get(0));
			
			
			for (FraudIdentificationTableDTO fraudIdentity : dtoList) {
				fraudIdentificationTableObj.addToList(fraudIdentity);
			}
			fraudIdentificationTableObj.tablesize();
			
			if(btnSubmit == null) {
				btnSubmit = new Button("Submit");
				btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				addButonClickListener();
			}
			if(btnCancel == null) {
				btnCancel = new Button("Cancel");
				addCancelButtonListener();
			}
			if(btnExport == null) {
				btnExport = new Button("Export to Excel");
				addExportButton();
			}
			
			
			generatedLayout.addComponent(fraudIdentificationTableObj);
			generatedLayout.setSpacing(true);
			HorizontalLayout hLayout = new HorizontalLayout(btnSubmit, btnCancel,btnExport);
			hLayout.setSpacing(true);
			generatedLayout.addComponent(hLayout);
			generatedLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
			
		}
		
	}
	
	
	
	private boolean validatePage(Boolean hasError) {
		StringBuffer eMsg = new StringBuffer();
		
		if(hasError){
			eMsg.append("No Records to Submit");
			hasError=true;
		}
		
		if(fraudIdentificationTableObj.getValues() != null && !fraudIdentificationTableObj.getValues().isEmpty()){
			
			List<FraudIdentificationTableDTO> existingEnteriesList = new ArrayList<FraudIdentificationTableDTO>();
			for (FraudIdentificationTableDTO dto : fraudIdentificationTableObj.getValues()){
				
			
				if(!isNewRecord(dto)){
					existingEnteriesList.add(dto);
					
				}
				
			}
			
			
			
			
			for (FraudIdentificationTableDTO component : fraudIdentificationTableObj.getValues()) {
				
				if(component != null && (component.getParameterValue() == null || component.getParameterValue().isEmpty())){
					eMsg.append("Select "+component.getParameterType());
					hasError=true;
					break;
				}
				
				//Duplicate entry validation
				
				Boolean bExistingRecord = false;
				
				if(isNewRecord(component)){
					for(FraudIdentificationTableDTO existingDTO : existingEnteriesList ){
						if(existingDTO.getParameterValue().equalsIgnoreCase(component.getParameterValue())){
							bExistingRecord = true;
						}
					}
				}
				
				
				if(bExistingRecord)
				{
					eMsg.append(component.getParameterValue()+"already exist");
					hasError=true;
					break;
				}
				
				//perform email validation
				
				if(isModifiedRecord(component)){
					
					if (component.getEffectiveStartDate() == null){
						eMsg.append("Please Enter Effective start date");
						hasError=true;
						break;
					}
					
					//For Recipient To
					String recipientTO = component.getRecipientTo();
					if(recipientTO ==null || recipientTO.isEmpty()){
						
						String errorMsg = String.format("Email ID in Recipient To field cannot be empty. Please enter Valid email for "+component.getParameterType()+" : "+component.getParameterValue());
						eMsg.append(errorMsg);
						hasError = true;
						break;
						
					}
					
					if(recipientTO != null && !(recipientTO.isEmpty()))
					{
						String[] listToEmailIDs = recipientTO.split(";");
						Boolean bValidEmail = SHAUtils.ValidateEmailAddresses(listToEmailIDs);
						
						if(!bValidEmail)
						{
							String errorMsg = String.format("Atleast one email ID in the Recipient To field is invalid. Please enter Valid email for "+component.getParameterType()+" : "+component.getParameterValue());
							eMsg.append(errorMsg);
							hasError = true;
							break;
						}
					}
					
					//For Recipient CC
					
					
					String recipientCC = component.getRecipientCc();
					
					if(recipientCC ==null || recipientCC.isEmpty()){
						
						String errorMsg = String.format("Email ID in Recipient CC field cannot be empty. Please enter Valid email for "+component.getParameterType()+" : "+component.getParameterValue());
						eMsg.append(errorMsg);
						hasError = true;
						break;
						
					}

					if(recipientCC != null && !(recipientCC.isEmpty()))
					{
						String[] listCCEmailIDs = recipientCC.split(";");
						Boolean bValidEmail = SHAUtils.ValidateEmailAddresses(listCCEmailIDs);
						
						if(!bValidEmail)
						{
							String errorMsg = String.format("Atleast one email ID in the Recipient CC field is invalid. Please enter Valid email for "+component.getParameterType()+" : "+component.getParameterValue());
							eMsg.append(errorMsg);
							hasError = true;
							break;
						}
					}
					
					if (component.getUserRemarks() == null || component.getUserRemarks().isEmpty()){
						eMsg.append("Please Enter User Remarks");
						hasError=true;
						break;
					}
					
				}
				
			}
			
		}
		
		
		if (hasError) {
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
			return true;
		}
	
	
	public void loadParameterDropDownValues(
			BeanItemContainer<SelectValue> mastersValueContainer) {

		parameterValue.setContainerDataSource(mastersValueContainer);
		parameterValue.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		parameterValue.setItemCaptionPropertyId("value");
	}

	public void generateTableForFraudIdentification(FraudIdentificationTableDTO dto) {
		generatedLayout.removeAllComponents();
		if (fraudIdentificationTableObj == null) {
			fraudIdentificationTableObj = fraudIdentificationTable.get();
		}

		fraudIdentificationTableObj.init(dto);
		fraudIdentificationTableObj.addToList(dto);
		fraudIdentificationTableObj.tablesize();

		if (btnSubmit == null) {
			btnSubmit = new Button("Submit");
			btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			addButonClickListener();
		}
		if (btnCancel == null) {
			btnCancel = new Button("Cancel");
			addCancelButtonListener();
		}
		if (btnExport == null) {
			btnExport = new Button("Export to excel");
			addExportButton();
		}

		generatedLayout.addComponent(fraudIdentificationTableObj);
		generatedLayout.setSpacing(true);
		HorizontalLayout hLayout = new HorizontalLayout(btnSubmit, btnCancel,btnExport);
		hLayout.setSpacing(true);
		generatedLayout.addComponent(hLayout);
		generatedLayout.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);

	}
	
//	private void getTableDataForReport(List<FraudIdentificationTableDTO> finalList) {
//		if (null != tableForExcel) {
//			tableForExcel.removeRow();
//
//			List<FraudIdentificationTableDTO> requestTableList = finalList;
//			if (null != requestTableList && !requestTableList.isEmpty()) {
//				tableForExcel.addBeanToList(requestTableList);
//			}
//
//		}
//	}
	public Boolean isNewRecord(FraudIdentificationTableDTO fdto){
		if (fdto !=null 
				&& (fdto.getNewRecord() == true)) {
			return true;
		}
		
		return false;
				
	}
	
	public Boolean isModifiedRecord(FraudIdentificationTableDTO mdto){
		if (mdto !=null && (mdto.getEdit() == true || isNewRecord(mdto))){
			return true;
		}
		return false;
	}

}
