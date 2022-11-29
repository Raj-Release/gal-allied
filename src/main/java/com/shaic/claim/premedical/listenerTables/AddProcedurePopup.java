package com.shaic.claim.premedical.listenerTables;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class AddProcedurePopup extends ViewComponent {
	
	private SpecialityDTO bean;
	
	@EJB
	private MasterService masterService;
	
	private Window popup;
	
	private TextField procedureName;
	
	private Button submitBtn;
	
	private Button cancelBtn;
	
	private ComboBox comb;
	
	@SuppressWarnings("deprecation")
	public void init(final SpecialityDTO bean,ComboBox comb,Window popUp){
		this.bean = bean;
		this.popup = popUp;
		this.comb = comb;
		
		
		final ComboBox cmbAddProcedure = new ComboBox();
		
		procedureName = new TextField("Procedure Name");
		procedureName.setNullRepresentation("");
		
		submitBtn = new Button("Submit");
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
		
		cancelBtn = new Button("Cancel");
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		VerticalLayout procdLayout = new VerticalLayout(procedureName);
		procdLayout.setComponentAlignment(procedureName, Alignment.MIDDLE_CENTER);
		HorizontalLayout btnsLayout = new HorizontalLayout(submitBtn,cancelBtn);
		btnsLayout.setSpacing(true);
		
		final BeanItemContainer<SelectValue> diagnosisContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		cmbAddProcedure.setContainerDataSource(diagnosisContainer);	
		cmbAddProcedure.setFilteringMode(FilteringMode.STARTSWITH);
		cmbAddProcedure.setTextInputAllowed(true);
		cmbAddProcedure.setNullSelectionAllowed(true);
		cmbAddProcedure.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbAddProcedure.setItemCaptionPropertyId("value");
		cmbAddProcedure.setNewItemsAllowed(true);
		
		cmbAddProcedure.addValueChangeListener(new Property.ValueChangeListener() {
	            @Override
	            public void valueChange(ValueChangeEvent event) {
	                
	                // tell the custom container that a value has been selected. This is necessary to ensure that the
	                // selected value is displayed by the ComboBox
	            	SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null)
	            	{
						cmbAddProcedure.select(value);
	            	}
	            }
	        });
		cmbAddProcedure.setNewItemHandler(new NewItemHandler() {
			
			private static final long serialVersionUID = -4453822645147859276L;

			@SuppressWarnings("unused")
			@Override
			public void addNewItem(String newItemCaption) {
				SelectValue newDiagonsisValue = masterService.addProcedure(newItemCaption,bean.getSpecialityType());
				diagnosisContainer.addItem(newDiagonsisValue);
				cmbAddProcedure.addItem(newDiagonsisValue);
//				diagnosisContainer.setSelectedBean(newDiagonsisValue);
				cmbAddProcedure.select(newDiagonsisValue);
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
				
				if(procedureName != null && procedureName.getValue() != null && !procedureName.getValue().isEmpty()){
					Boolean searchProcedureByvalue = masterService.searchProcedureByValue(procedureName.getValue());
					if(searchProcedureByvalue){
						result(!searchProcedureByvalue);
						procedureName.setValue(null);
					} else {
						SelectValue newProcedureValue = masterService.addProcedure(procedureName.getValue().toUpperCase(),bean.getSpecialityType());
					/*	final SuggestingContainer diagnosisContainer = (SuggestingContainer) comb.getContainerDataSource();
						SelectValue st = new SelectValue(newProcedureValue.getId(), newProcedureValue.getValue().toUpperCase());
						diagnosisContainer.setSelectedBean(st);
						comb.select(st);
						comb.setValue(st);*/
						bean.setProcedureValue(newProcedureValue.getValue());
						bean.setAddprocedureId(newProcedureValue.getId());

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
