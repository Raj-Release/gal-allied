package com.shaic.claim.processdatacorrection.listenertable;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.preauth.wizard.dto.SpecialityDTO;
import com.shaic.claim.processdatacorrection.dto.SpecialityCorrectionDTO;
import com.shaic.claim.processdatacorrection.search.DataCorrectionPresenter;
import com.shaic.domain.MasterService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;

public class AddActualProcedurePopup extends ViewComponent{

	private SpecialityCorrectionDTO bean;

	@EJB
	private MasterService masterService;

	private Window popup;

	private TextField procedureName;

	private Button submitBtn;

	private Button cancelBtn;

	private ComboBox comb;
	
	private String presenterString;

	@SuppressWarnings("deprecation")
	public void init(final SpecialityCorrectionDTO bean,ComboBox comb,Window popUp,String presenterString){
		this.bean = bean;
		this.popup = popUp;
		this.comb = comb;
		this.presenterString = presenterString;
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

		VerticalLayout mainLayout = new VerticalLayout(procdLayout,btnsLayout);
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
						SelectValue newProcedureValue = masterService.addProcedure(procedureName.getValue().toUpperCase(),bean.getActualspecialityType());
						bean.setActualProcedure(newProcedureValue);
						if(presenterString.equalsIgnoreCase("Data Validation")){
							fireViewEvent(DataCorrectionPresenter.ADD_SPECIALITY_PROCEDURE_VALUES,bean.getActualspecialityType().getId(),comb,newProcedureValue);	
						}
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
