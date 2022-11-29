package com.shaic.reimbursement.topup_policy_master.search;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.tls.AlertDescription;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

public class TopUpPolicyMasterUI extends ViewComponent {
	


	private static final long serialVersionUID = 1L;
	
	
	
	private TextField txtPolicyNo;
	
	private Button btnSearch;
	
	private Button btnSubmit;
	
	private Button btnCancel;

	private VerticalLayout mainLayout;
	
	private VerticalLayout generatedLayout;

	private Panel mainPanel;
	
	@Inject
	private Instance<TopUpPolicyMasterTable> investigationDirectAssignmentTable;
	
	private TopUpPolicyMasterTable investigationDirectAssignmentTableObj;
	
	
	
	@PostConstruct
	public void init() {

	}
	
	public void initView() {
		mainLayout = new VerticalLayout();
		mainPanel = new Panel();
		generatedLayout = new VerticalLayout();
		mainLayout = new VerticalLayout();
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setWidth("100.0%");
		mainLayout.setMargin(false);
		txtPolicyNo =new TextField("Policy No");
		txtPolicyNo.setRequired(true);
		
		FormLayout cpuForm = new FormLayout(txtPolicyNo);
		
		/*txtPolicyNo.setContainerDataSource();
		txtPolicyNo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtPolicyNo.setItemCaptionPropertyId("value");*/
		
		
		btnSearch = new Button("Search");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		Label dummyLabel1 =new Label();
		dummyLabel1.setWidth("80px");
		
		HorizontalLayout searchButton = new HorizontalLayout();
		searchButton.addComponents(dummyLabel1,btnSearch);
		mainLayout.addStyleName("panelHeader");
		mainLayout.addStyleName("g-search-panel");
		mainLayout.addComponent(cpuForm);
		mainLayout.addComponent(searchButton);
		
		mainLayout.addComponent(generatedLayout);
		
		
	//	mainLayout.setComponentAlignment(btnSearch,Alignment.MIDDLE_CENTER);

		addClickListener();
		mainPanel = new Panel("Policy Alert");
		mainPanel.setWidth("100%");
		mainPanel.setHeight("620px");
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);

	}

	private void addClickListener() {
		btnSearch.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(txtPolicyNo.getValue() != null && !txtPolicyNo.isEmpty()) {
					fireViewEvent(TopUpPolicyMasterPresenter.SEARCH_TOP_UP_POLICY, txtPolicyNo.getValue());
				} else {
					Label label = new Label("Please enter Policy No.", ContentMode.HTML);
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
		
	}
	
	
	private void addButonClickListener() {
		btnSubmit.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				Boolean hasError=false;
				if(investigationDirectAssignmentTableObj.getValues()==null || investigationDirectAssignmentTableObj.getValues().isEmpty()){
					hasError=true;
				}
				
				if(validatePage(hasError)){
					fireViewEvent(TopUpPolicyMasterPresenter.SUBMIT_TOP_UP_POLICY, investigationDirectAssignmentTableObj.getValues());
					
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
											fireViewEvent(MenuItemBean.TOP_UP_POLICY_MASTER_SCREEN,
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

	public void generateTableForPolicyStatus(List<TopUpPolicyMasterTableDTO>policyNo) {
		generatedLayout.removeAllComponents();
		if(policyNo != null && !policyNo.isEmpty()) {
			if(investigationDirectAssignmentTableObj == null) {
				investigationDirectAssignmentTableObj = investigationDirectAssignmentTable.get();
			}
			investigationDirectAssignmentTableObj.init();
//			skipZMRListenerTableObj.setReferenceData( new HashMap<String, Object>());
			for (TopUpPolicyMasterTableDTO tmpPolicyNo : policyNo) {
				investigationDirectAssignmentTableObj.addToList(tmpPolicyNo);
			}
			
			if(btnSubmit == null) {
				btnSubmit = new Button("Submit");
				btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				addButonClickListener();
			}
			if(btnCancel == null) {
				btnCancel = new Button("Cancel");
				addCancelButtonListener();
			}
			
			
			generatedLayout.addComponent(investigationDirectAssignmentTableObj);
			generatedLayout.setSpacing(true);
			HorizontalLayout hLayout = new HorizontalLayout(btnSubmit, btnCancel);
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
		
		if(investigationDirectAssignmentTableObj.getValues() != null && !investigationDirectAssignmentTableObj.getValues().isEmpty()){
			for (TopUpPolicyMasterTableDTO component : investigationDirectAssignmentTableObj.getValues()) {
				if(component != null && component.getEnable() == null && component.getDisable() == null){
					eMsg.append("Select Enable/Disable");
					hasError=true;
					break;
				}else if(component != null && !component.getEnable() && component.getDisable() == null){
					eMsg.append("Select Enable/Disable");
					hasError=true;
					break;
				}
				else if(component != null && (null == component.getRemarks() || StringUtils.isBlank(component.getRemarks()))){
					eMsg.append("Please Enter Remarks");
					hasError=true;
					break;
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
	
	


}
