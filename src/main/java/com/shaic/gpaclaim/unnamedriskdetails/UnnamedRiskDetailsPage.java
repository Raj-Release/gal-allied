package com.shaic.gpaclaim.unnamedriskdetails;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class UnnamedRiskDetailsPage extends ViewComponent{

	 private TextField txtIntimationNo;
		
	 private TextField txtPolicyNo;	
	
	 private TextField txtOrganisationName;
	
	 private TextField txtSumInsured;
		
	 private TextField txtParentName;
		
	 private TextField txtDateOfBirth;
		
	 private TextField txtRiskName;	
	 
	 private TextField txtSectionOrclass;
		
	 private ComboBox cmbCategory;
	 
	 private Button btnSubmit;

	 private Button btnCancel;	
	 
	 private UnnamedRiskDetailsPageDTO bean;
	 
	 private BeanFieldGroup<UnnamedRiskDetailsPageDTO> binder;
	 
	 private HorizontalLayout unnamedButtonLayout;
		
	 public void init(UnnamedRiskDetailsPageDTO bean) {
			this.bean = bean;
			unnamedButtonLayout = new HorizontalLayout();
			Panel mainPanel = new Panel("Category Updation");
			mainPanel.addStyleName("panelHeader");
			mainPanel.addStyleName("g-search-panel");
			mainPanel.setContent(getContent());
			setCompositionRoot(mainPanel);		
			mainPanel.setHeight("400px");
		}

		public void initBinder() {
			this.binder = new BeanFieldGroup<UnnamedRiskDetailsPageDTO>(
					UnnamedRiskDetailsPageDTO.class);
			this.binder.setItemDataSource(bean);
			binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		}

		public VerticalLayout getContent() {
			initBinder();
			txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo",
					TextField.class);
			txtIntimationNo.setEnabled(false);
			
			txtPolicyNo = binder.buildAndBind("Policy No", "policyNo",
					TextField.class);
			txtPolicyNo.setEnabled(false);
			
			txtOrganisationName = binder.buildAndBind("Organisation Name",
					"organisationName", TextField.class);
			txtOrganisationName.setEnabled(false);
			
			txtSumInsured = binder.buildAndBind("Sum Insured",
					"sumInsured", TextField.class);
			txtSumInsured.setEnabled(false);
			
			txtParentName = binder.buildAndBind("Parent Name",
					"gpaParentName", TextField.class);
			txtParentName.setEnabled(false);
			
			txtDateOfBirth = binder.buildAndBind("Parent(DOB)",
					"gpaParentDOB", TextField.class);
			txtDateOfBirth.setEnabled(false);
			
			txtRiskName = binder.buildAndBind("Risk Name", "gpaRiskName", TextField.class);	
			txtRiskName.setEnabled(false);
			
			txtSectionOrclass = binder.buildAndBind("Section/Class", "gpaSection",
					TextField.class);
			txtSectionOrclass.setEnabled(false);
			
			cmbCategory = binder.buildAndBind("Category", "gpaCategory",
					ComboBox.class);			
			
			FormLayout leftFormLayout = new FormLayout(txtIntimationNo, txtPolicyNo, txtOrganisationName,txtSumInsured,txtSectionOrclass);
		
			FormLayout rightFormLayout = new FormLayout(txtParentName,txtDateOfBirth,txtRiskName,cmbCategory);
			
			HorizontalLayout fieldLayout = new HorizontalLayout(leftFormLayout,	rightFormLayout);	
			fieldLayout.setSpacing(true);
			fieldLayout.setMargin(true);
			
			
			HorizontalLayout unnamedRiskButtonLayout = buildButtonLayout();		
			
			unnamedButtonLayout.addComponent(unnamedRiskButtonLayout);
			unnamedButtonLayout.setWidth("100%");
			unnamedButtonLayout.setComponentAlignment(unnamedRiskButtonLayout,Alignment.BOTTOM_LEFT);
			unnamedButtonLayout.setSpacing(true);
			unnamedButtonLayout.setMargin(true);;
			
			VerticalLayout unnamedRiskDetailsLayout = new VerticalLayout(fieldLayout,unnamedButtonLayout);
			addListener();
			return unnamedRiskDetailsLayout;
		}
		
		
		private HorizontalLayout buildButtonLayout(){
			
			
			btnSubmit = new Button();
			btnSubmit.setCaption("Submit");
			//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
			btnSubmit.setWidth("-1px");
			btnSubmit.setHeight("-1px");
			btnSubmit.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			btnSubmit.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					
					fireViewEvent(UnnamedRiskDetailsPagePresenter.SUBMIT_BUTTON_CLICK,
							bean);
				}
			});

			btnCancel = new Button();
			btnCancel.setCaption("Cancel");
			//Vaadin8-setImmediate() btnCancel.setImmediate(true);
			btnCancel.setWidth("-1px");
			btnCancel.setHeight("-1px");
			btnCancel.setStyleName(ValoTheme.BUTTON_DANGER);
			
			btnCancel.addClickListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {					
					fireViewEvent(MenuItemBean.GPA_UNNAMED_RISK_DETAILS,
							null);
				}
			});
			HorizontalLayout btnLayout = new HorizontalLayout(
					btnSubmit, btnCancel);
			btnLayout.setSpacing(true);
			return btnLayout;
		}
		
		
		public void setDropDownValues(BeanItemContainer<SelectValue> category) 
		{	
			
			cmbCategory.setContainerDataSource(category);
			cmbCategory.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbCategory.setItemCaptionPropertyId("value");		
			   
			if(null != category){
			 SelectValue defaultCategory = category.getIdByIndex(0);
			 cmbCategory.setValue(defaultCategory);
			}
			       
		}	
		
		@SuppressWarnings("static-access")
		public void buildSuccessLayout() {
			Label successLabel = new Label(
					"<b style = 'color: green;'> Category Updated Successfully</b>",
					ContentMode.HTML);

			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			hLayout.setStyleName("borderLayout");

			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);

			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.GPA_UNNAMED_RISK_DETAILS, null);

				}
			});
		}

		
		private void addListener()
		{
			cmbCategory.addValueChangeListener(new Property.ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue value = (SelectValue) event.getProperty().getValue();
					if(null != value && null != value.getValue())
					{
						String[] splitCategory = value.getValue().split("-");
						String category = splitCategory[0];
						if(null != category){
						bean.setGpaCategory(category);
						}
						
					}
					
				}
			});
		}
}
