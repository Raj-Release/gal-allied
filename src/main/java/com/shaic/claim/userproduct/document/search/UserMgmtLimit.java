package com.shaic.claim.userproduct.document.search;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UserMgmtLimit extends ViewComponent {
	@Inject
	private LimitTable limitTable;
	
	private BeanFieldGroup<UserManagementDTO> binder;
	
	private UserManagementDTO bean;

	private ComboBox cmbRoleCategory;
	
	private ComboBox limit;
	
	private TextField amount;
	
	private  Button add;
	
	@EJB
	private MasterService masterService;
	
	@PostConstruct
	public void initView() {
		
	}
	
	public void initBinder() {
		
	}
	
	@SuppressWarnings("static-access")
	public void init() {
		this.binder = new BeanFieldGroup<UserManagementDTO>(UserManagementDTO.class);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.binder.setItemDataSource(new UserManagementDTO());
		
		BeanItemContainer<SelectValue> roleTypeContainerValue = masterService.getListMasterValuebyTypeCode(SHAConstants.USER_MGMT_ROLE_TYPE);
		cmbRoleCategory = (ComboBox) binder.buildAndBind("Role Category",
				"roleCtegory", ComboBox.class);
		limit = (ComboBox) binder.buildAndBind("Limit",
				"limit", ComboBox.class);
		amount = (TextField) binder.buildAndBind("Amount", "amount", TextField.class);
		amount.setEnabled(false);
		cmbRoleCategory.setContainerDataSource(roleTypeContainerValue);
		cmbRoleCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbRoleCategory.setItemCaptionPropertyId("value");
		cmbRoleCategory.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(cmbRoleCategory.getValue() != null) {
				if(cmbRoleCategory.getValue().toString().equals(SHAConstants.DOCTOR)) {
					BeanItemContainer<SpecialSelectValue> limitContainerValue = masterService.getRoleTypeBYCategory(SHAConstants.MA);
					limit.setRequired(true);
					
					limit.setContainerDataSource(limitContainerValue);
					limit.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					limit.setItemCaptionPropertyId("value");
				}else if(cmbRoleCategory.getValue().toString().equals(SHAConstants.FA)) {
					BeanItemContainer<SpecialSelectValue> limitContainerValue = masterService.getRoleTypeBYCategory(SHAConstants.FA);
					limit.setRequired(true);
					limit.setContainerDataSource(limitContainerValue);
					limit.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					limit.setItemCaptionPropertyId("value");
				}else if(cmbRoleCategory.getValue().toString().equals(SHAConstants.BILLING_AUTO_ALLOCATION)) {
					BeanItemContainer<SpecialSelectValue> limitContainerValue = masterService.getRoleTypeBYCategory(SHAConstants.BILLING_USER_LIMTS);
					limit.setRequired(true);
					limit.setContainerDataSource(limitContainerValue);
					limit.setItemCaptionMode(ItemCaptionMode.PROPERTY);
					limit.setItemCaptionPropertyId("value");
				}
				}
			}
		});
		limit.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2697682747976915503L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				
				SpecialSelectValue value = (SpecialSelectValue) event.getProperty().getValue();
				if(value != null) {
				amount.setValue(value.getSpecialId().toString());
				}
			}
			
		});
		
		
		add = new Button("Add");
		
		add.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		add.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(cmbRoleCategory.getValue() != null  && limit.getValue() == null ) {
					showErrorMessage("Limit is Mandatory");
				}
				if(binder.isValid()) {
					try {
					
						binder.commit();
						bean = binder.getItemDataSource().getBean();
						if(bean.getRoleCtegory() == null || bean.getLimit() == null) {
							if(bean.getRoleCtegory() == null && bean.getLimit() != null) {
								showErrorMessage("You cannot select Limit without Role Category");
							}
							
						}else if(bean.getRoleCtegory() != null && bean.getLimit() != null ) {
						limitTable.addUserLimit(bean);
						}
						binder.setItemDataSource(new UserManagementDTO());
					} catch (CommitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}
				}
							
			}
		});
		
		HorizontalLayout layout = new HorizontalLayout();
		VerticalLayout addlayout = new VerticalLayout();
		Label addDummy = new Label();
		addlayout.addComponents(addDummy,add);
		HorizontalLayout dummy = new HorizontalLayout();
		layout.addComponents(cmbRoleCategory,limit,amount,addlayout);
		layout.setSpacing(true);
		VerticalLayout finallayout = new VerticalLayout();
		finallayout.addComponents(dummy,layout);
		finallayout.setSpacing(true);
		
		finallayout.setMargin(true);
		
		
		setCompositionRoot(finallayout);
		
	}
	public void addListener() {
		cmbRoleCategory.setEnabled(false);
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
