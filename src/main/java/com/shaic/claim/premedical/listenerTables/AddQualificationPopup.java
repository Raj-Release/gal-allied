package com.shaic.claim.premedical.listenerTables;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.preauth.wizard.dto.TreatingDoctorDTO;
import com.shaic.domain.MasterService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("deprecation")
public class AddQualificationPopup extends ViewComponent{
	
	private TreatingDoctorDTO bean;
	
	@EJB
	private MasterService masterService;
	
	private Window popup;
	
	private TextField qualification;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ComboBox comb;
	
	
	public void init(final TreatingDoctorDTO bean,ComboBox comb,Window popUp){
		this.bean = bean;
		this.popup = popUp;
		this.comb = comb;
		
		
		final ComboBox cmbAddqualification = new ComboBox();
		
		qualification = new TextField("Qualification");
		qualification.setNullRepresentation("");
		
		submitBtn = new Button("Submit");
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn = new Button("Cancel");
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		VerticalLayout procdLayout = new VerticalLayout(qualification);
		procdLayout.setComponentAlignment(qualification, Alignment.MIDDLE_CENTER);
		HorizontalLayout btnsLayout = new HorizontalLayout(submitBtn,cancelBtn);
		btnsLayout.setSpacing(true);
		
		final BeanItemContainer<SelectValue> qualificationContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		cmbAddqualification.setContainerDataSource(qualificationContainer);	
		cmbAddqualification.setFilteringMode(FilteringMode.STARTSWITH);
		cmbAddqualification.setTextInputAllowed(true);
		cmbAddqualification.setNullSelectionAllowed(true);
		cmbAddqualification.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAddqualification.setItemCaptionPropertyId("value");
		cmbAddqualification.setNewItemsAllowed(true);
		
		cmbAddqualification.addValueChangeListener(new Property.ValueChangeListener() {
	            @Override
	            public void valueChange(ValueChangeEvent event) {
	                
	                // tell the custom container that a value has been selected. This is necessary to ensure that the
	                // selected value is displayed by the ComboBox
	            	SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null)
	            	{
						cmbAddqualification.select(value);
	            	}
	            }
	        });
		cmbAddqualification.setNewItemHandler(new NewItemHandler() {
			
			private static final long serialVersionUID = -4453822645147859276L;

			@SuppressWarnings("unused")
			@Override
			public void addNewItem(String newItemCaption) {
				SelectValue newDiagonsisValue = masterService.addQualification(newItemCaption);
				qualificationContainer.addItem(newDiagonsisValue);
				cmbAddqualification.addItem(newDiagonsisValue);
//				diagnosisContainer.setSelectedBean(newDiagonsisValue);
				cmbAddqualification.select(newDiagonsisValue);
			}
		});
		
		VerticalLayout mainLayout = new VerticalLayout(procdLayout,btnsLayout);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setSpacing(true);
		mainLayout.setComponentAlignment(btnsLayout, Alignment.BOTTOM_CENTER);
		
		addListener();
		setCompositionRoot(mainLayout);
		
	}
	
	public void addListener() {
		
		cancelBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
			}
		});
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(qualification != null && qualification.getValue() != null && !qualification.getValue().isEmpty()){
					Boolean searchProcedureByvalue = masterService.searchQualificationByValue(qualification.getValue());
					if(searchProcedureByvalue){
						result(!searchProcedureByvalue);
						qualification.setValue(null);
					} else {
						SelectValue newqualificationValue = masterService.addQualification(qualification.getValue().toUpperCase());
						final BeanItemContainer<SelectValue> qualification = (BeanItemContainer<SelectValue>) comb.getContainerDataSource();
						SelectValue st = new SelectValue(newqualificationValue.getId(), newqualificationValue.getValue().toUpperCase());
						qualification.addBean(st);
						comb.setContainerDataSource(qualification);
						comb.select(st);
						comb.setValue(st);
						//bean.setQualification(newqualificationValue);
						popup.close();
				}
				} else {
					result(true);
				}
				
			}
		});
		
	}
	
	public void result(Boolean value){
		
		Label successLabel = null;

		if(value){
			successLabel = new Label(
					"<b style = 'color: red;'> Please Enter Procedure Name</b>",
					ContentMode.HTML);
		}else{
			 successLabel = new Label(
						"<b style = 'color: green;'>Entered Procedure Name is already available.</b>",
						ContentMode.HTML);
		}

		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel,homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
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
	
			}
		});
		
	}
}
