package com.shaic.claim.preauth;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.vaadin.v7.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.v7.ui.NativeSelect;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class InitiatePEDEndorsement extends Window {

	private static final long serialVersionUID = -9182066388418239095L;

	private VerticalLayout wholeVLayout;
	private VerticalLayout newIntimatPedVLayout;

	@Inject
	private Instance<PEDRequestDetailsList> pedRequestDetailsList;

	@Inject
	private Instance<InitiatePEDEndorsementTable> initiatePEDEndorsementTable;

	public InitiatePEDEndorsement() {
	}
	
	@PostConstruct
	public void init() {
		buildMainLayout();
		setContent(wholeVLayout);
	}

	private VerticalLayout buildMainLayout() {
		wholeVLayout = new VerticalLayout();
		wholeVLayout.setMargin(true);
		wholeVLayout.setSpacing(true);
		
		setCaption("PED Request Details");
		setWidth("1000px");
		setHeight("700px");
		setModal(true);
		setClosable(true); 
		
		PEDRequestDetailsList pedRequestDetailsListInstance = pedRequestDetailsList.get();
		pedRequestDetailsListInstance.init("PED Request Details", false, false);
		wholeVLayout.addComponent(pedRequestDetailsListInstance);
		wholeVLayout.addComponent(bindInitatePedOption());
	

		return wholeVLayout;
	}

	private FormLayout bindInitatePedOption() {
		FormLayout optionFLayout = new FormLayout();		
		OptionGroup intimatePEDOGroup = new OptionGroup();
		intimatePEDOGroup.setCaption("Initiate PED Endorsement");
		intimatePEDOGroup.addItem("Yes");
		intimatePEDOGroup.addItem("No");
		intimatePEDOGroup.setStyleName("inlineStyle");
		addListener(intimatePEDOGroup);
		optionFLayout.addComponent(intimatePEDOGroup);
		return optionFLayout;
	}

	private void addListener(OptionGroup intimatePEDOGroup) {
		intimatePEDOGroup
				.addValueChangeListener(new Property.ValueChangeListener() {

					@Override
					public void valueChange(
							com.vaadin.v7.data.Property.ValueChangeEvent event) {
						if (event.getProperty() != null
								&& event.getProperty().getValue().toString() == "Yes") {
							newIntimatPedVLayout = buildNewIntimatPedVLayout();
							wholeVLayout.addComponent(newIntimatPedVLayout);
						} else {
								if(newIntimatPedVLayout!=null)
								{
									wholeVLayout.removeComponent(newIntimatPedVLayout);
								}
						}
					}

				});
	}

	private VerticalLayout buildNewIntimatPedVLayout() {
		newIntimatPedVLayout = new VerticalLayout();
		//Vaadin8-setImmediate() newIntimatPedVLayout.setImmediate(false);
		newIntimatPedVLayout.setWidth("100.0%");
		newIntimatPedVLayout.setMargin(false);

		newIntimatPedVLayout.addComponent(buildIntimatePEDFLayout());
		
		InitiatePEDEndorsementTable initiatePEDEndorsementTableInstance =initiatePEDEndorsementTable.get();
		initiatePEDEndorsementTableInstance.init("", false);
		newIntimatPedVLayout.addComponent(initiatePEDEndorsementTableInstance);

		NativeButton submitBtn = new NativeButton();
		submitBtn.setCaption("Submit");
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		newIntimatPedVLayout.addComponent(submitBtn);
		newIntimatPedVLayout
				.setComponentAlignment(submitBtn, new Alignment(34));

		return newIntimatPedVLayout;
	}

	private FormLayout buildIntimatePEDFLayout() {
		FormLayout intimatePEDFLayout = new FormLayout();
		intimatePEDFLayout.setSpacing(true);

		NativeSelect txtPEDSuggestion = new NativeSelect();
		txtPEDSuggestion.setCaption("PED Suggestion");
		txtPEDSuggestion.setRequired(true);	
		txtPEDSuggestion.setWidth("155px");
		intimatePEDFLayout.addComponent(txtPEDSuggestion);

		TextField txtNameofPED = new TextField();
		txtNameofPED.setCaption("Name of PED");
		txtNameofPED.setRequired(true);
		txtNameofPED.setNullRepresentation("");
		intimatePEDFLayout.addComponent(txtNameofPED);

		TextField txtRemarks = new TextField();
		txtRemarks.setCaption("Remarks");
		txtRemarks.setRequired(true);
		txtRemarks.setNullRepresentation("");
		intimatePEDFLayout.addComponent(txtRemarks);

		return intimatePEDFLayout;
	}

}
