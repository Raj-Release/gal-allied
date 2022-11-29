package com.shaic.reimbursement.claims_alert.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;

public class ClaimsAlertMasterUI extends ViewComponent{

	private static final long serialVersionUID = 1L;

	private TextField txtIntitmationNo;

	private Button btnSearch;

	private Button btnSubmit;

	private Button btnCancel;

	private VerticalLayout wholeVerticalLayout;

	private VerticalLayout generatedLayout;

	private Panel searchPanel;

	private VerticalLayout searchVerticalLayout;

	private HorizontalLayout viewHLayout;

	@Inject
	private Instance<ClaimsAlertMasterTable> claimsAlertMasterTableInstance;

	private ClaimsAlertMasterTable claimsAlertMasterTableObj;

	@PostConstruct
	public void init() {

	}

	public void initView() {

		wholeVerticalLayout = new VerticalLayout(buildSearchLayout());
		generatedLayout = new VerticalLayout();
		generatedLayout.setSpacing(true);
		wholeVerticalLayout.addComponent(generatedLayout);
		setCompositionRoot(wholeVerticalLayout);

	}

	@SuppressWarnings("deprecation")
	private void addClickListener() {
		btnSearch.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5677998363425252239L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(txtIntitmationNo.getValue() != null && !txtIntitmationNo.isEmpty()) {
					fireViewEvent(ClaimsAlertMasterPresenter.SEARCH_CLAIMS_ALERT, txtIntitmationNo.getValue());
				} else {
					Label label = new Label("Please enter Intimation No.", ContentMode.HTML);
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
				if(claimsAlertMasterTableObj.getValues()==null || claimsAlertMasterTableObj.getValues().isEmpty()){
					hasError=true;
				}
				if(validatePage(hasError)){
					fireViewEvent(ClaimsAlertMasterPresenter.SUBMIT_CLAIMS_ALERT, claimsAlertMasterTableObj.getValues());
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
									fireViewEvent(MenuItemBean.CLAIMS_ALERT_MASTER_SCREEN,null);
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

	public void generateTableForClaimAlert(List<ClaimsAlertTableDTO> alertTableDTOs,Map<String, Object> referenceData) {
		generatedLayout.removeAllComponents();
		if(alertTableDTOs != null && !alertTableDTOs.isEmpty()) {

			if(claimsAlertMasterTableObj == null) {
				claimsAlertMasterTableObj = claimsAlertMasterTableInstance.get();
			}
			claimsAlertMasterTableObj.setReferenceData(referenceData);
			claimsAlertMasterTableObj.init();
			for (ClaimsAlertTableDTO claimsAlertTableDTO : alertTableDTOs) {
				claimsAlertMasterTableObj.addToList(claimsAlertTableDTO);
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
			generatedLayout.addComponent(claimsAlertMasterTableObj);
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

		if(claimsAlertMasterTableObj.getValues() != null && !claimsAlertMasterTableObj.getValues().isEmpty()){
			Set<String> dupilcatecategory = new HashSet<String>();
			for (ClaimsAlertTableDTO component : claimsAlertMasterTableObj.getValues()) {
				if(component != null && component.getAlertCategory() == null){
					eMsg.append("Select Alert Category");
					hasError=true;
					break;
				}else if(component != null && (null == component.getRemarks() || StringUtils.isBlank(component.getRemarks()))){
					eMsg.append("Please Enter Remarks");
					hasError=true;
					break;
				}else if(component != null && component.getEnable() == null && component.getDisable() == null){
					eMsg.append("Select Enable/Disable");
					hasError=true;
					break;
				}else if(component != null && !component.getEnable() && component.getDisable() == null){
					eMsg.append("Select Enable/Disable");
					hasError=true;
					break;
				}
				if(component.getAlertCategory() !=null &&
						dupilcatecategory.contains(component.getAlertCategory().getValue())){
					eMsg.append("Duplicate alert entry for catagory "+component.getAlertCategory().getValue()+" .Kindy delete and proced further");
					hasError=true;
					break;
				}else{
					if(component.getAlertCategory() !=null){
						dupilcatecategory.add(component.getAlertCategory().getValue());
					}
				}
			}
		}

		if (hasError) {
			
			
			Notification.show("Error",eMsg.toString(), Type.ERROR_MESSAGE);
			/*Label label = new Label(eMsg.toString(), ContentMode.HTML);
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
			dialog.show(getUI().getCurrent(), null, true);*/

			hasError = true;
			return !hasError;
		} 
		return true;
	}

	private VerticalLayout buildSearchLayout(){		

		searchVerticalLayout = new VerticalLayout();
		searchVerticalLayout.setWidth("100.0%");

		searchPanel = new Panel("Claim Alert");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");

		searchVerticalLayout.addComponent(searchPanel);
		searchVerticalLayout.addStyleName("g-search-panel");

		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		absoluteLayout_3.setWidth("100.0%");
		absoluteLayout_3.setHeight("80px");

		txtIntitmationNo = new TextField("Intimation No");
		txtIntitmationNo.setNullRepresentation("");
		FormLayout srchFrm = new FormLayout(txtIntitmationNo);
		srchFrm.setMargin(false);

		this.btnSearch = new Button();
		btnSearch.setWidth("-1px");
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		FormLayout srchBtnFrm = new FormLayout(btnSearch);
		srchBtnFrm.setMargin(false);
		HorizontalLayout frmHLayout = new HorizontalLayout(srchFrm,srchBtnFrm);
		frmHLayout.setMargin(false);
		frmHLayout.setSpacing(true);

		addClickListener();
		absoluteLayout_3.addComponent(frmHLayout, "top:10.0px;left:25.0px;");		
		searchVerticalLayout.addComponents(absoluteLayout_3);

		return searchVerticalLayout;
	}

}
