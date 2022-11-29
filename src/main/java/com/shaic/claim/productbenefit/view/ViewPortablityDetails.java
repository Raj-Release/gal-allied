package com.shaic.claim.productbenefit.view;

import java.util.Iterator;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPortablityDetails extends ViewComponent{
	
	private TextField txtTbaCode;
	
	private TextField txtPolicyStart;
	
	private TextField txtPeriodElapse;
	
	private TextField txtPolicyTerm;
	
	private TextField txtdateOfBirth;
	
	private TextField txtPedDeclared;
	
	private TextField txtFirstSI;
	
	private TextField txtSecondSI;
	
	private TextField txtThirdSI;
	
	private TextField txtFourthSI;
	
	private TextField txtFirstFloatSI;
	
	private TextField txtSecondFloatSI;
	
	private TextField txtThirdFloatSI;
	
	private TextField txtFourthFloatSI;
	
	private TextField txtFirstChangeSI;
	
	private TextField txtSecondChangeSI;
	
	private TextField txtThirdChangeSI;
	
	private TextField txtFourthChangeSI;
	
	private TextField txtPedIcdCode;
	
	private TextField txtPedDescription;
	
	private TextField txtFamilySize;
	
	private TextField txtRemarks;
	
	private TextField txtRequestID;
	
	private TextField txtMemberEntryDetails;
	
	private BeanFieldGroup<PortablitiyPolicyDTO> binder;
	
	private PortablitiyPolicyDTO bean;
	
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<PortablitiyPolicyDTO>(
				PortablitiyPolicyDTO.class);
		this.binder.setItemDataSource(this.bean);
	}
	
	
	public void init(PortablitiyPolicyDTO bean){
		
		this.bean = bean;
		
		initBinder();
		
		txtTbaCode = (TextField) binder.buildAndBind("TBA code",
				"tbaCode", TextField.class);
		
		txtPolicyStart = (TextField) binder.buildAndBind("Policy start",
				"policyStartDate", TextField.class);
		
		txtPeriodElapse = (TextField) binder.buildAndBind("Period elapsed",
				"periodElapsed", TextField.class);
		
		txtPolicyTerm = (TextField) binder.buildAndBind("policy Term",
				"policyTerm", TextField.class);
		
		txtdateOfBirth = (TextField) binder.buildAndBind("Date of Birth",
				"dateOfBirth", TextField.class);
		
		txtPedDeclared = (TextField) binder.buildAndBind("Ped Declared",
				"pedDeclared", TextField.class);
		
		txtFirstSI = (TextField) binder.buildAndBind("Ist SI",
				"siFist", TextField.class);
		
		txtSecondSI = (TextField) binder.buildAndBind("IInd SI",
				"siSecond", TextField.class);
		
		txtThirdSI = (TextField) binder.buildAndBind("3rd  SI",
				"siThird", TextField.class);
		
		txtFourthSI = (TextField) binder.buildAndBind("4th  SI",
				"siFourth", TextField.class);
		
		txtFirstFloatSI = (TextField) binder.buildAndBind("I st  Float SI",
				"siFirstFloat", TextField.class);
		
		txtSecondFloatSI = (TextField) binder.buildAndBind("II nd  float SI",
				"siSecondFloat", TextField.class);
		
		txtThirdFloatSI = (TextField) binder.buildAndBind("3rd Float SI",
				"siThirdFloat", TextField.class);
		
		txtFourthFloatSI = (TextField) binder.buildAndBind("4th  Float SI",
				"siFourthFloat", TextField.class);
		
		txtFirstChangeSI = (TextField) binder.buildAndBind("I st Change SI",
				"siFirstChange", TextField.class);
		
		txtSecondChangeSI = (TextField) binder.buildAndBind("II nd Change SI",
				"siSecondChange", TextField.class);
		
		txtThirdChangeSI = (TextField) binder.buildAndBind("3rd  Change SI",
				"siThirdChange", TextField.class);
		
		txtFourthChangeSI = (TextField) binder.buildAndBind("4th Change SI",
				"siFourthChange", TextField.class);
		
		txtRemarks = (TextField) binder.buildAndBind("Remarks",
				"remarks", TextField.class);
		
//		txtPedDeclared = (TextField) binder.buildAndBind("PED declared",
//				"pedDeclared", TextField.class);
		
		txtPedIcdCode = (TextField) binder.buildAndBind("PED ICD code",
				"pedIcdCode", TextField.class);
		
		txtPedDescription = (TextField) binder.buildAndBind("PED Description",
				"pedDescription", TextField.class);
		
		txtFamilySize = (TextField) binder.buildAndBind("Family size",
				"familySize", TextField.class);
		
		txtRequestID = (TextField) binder.buildAndBind("Request ID",
				"requestId", TextField.class);
		
		txtMemberEntryDetails = (TextField) binder.buildAndBind("Member entry dt.",
				"memberEntryDate", TextField.class);
		
		FormLayout firstForm = new FormLayout(txtTbaCode,txtPolicyStart,txtPeriodElapse,txtPolicyTerm,txtdateOfBirth,txtPedDeclared);
		firstForm.setSpacing(true);
		firstForm.addStyleName("layoutDesign");
		setReadOnly(firstForm, true);
		
		FormLayout secondForm = new FormLayout(txtFirstSI,txtSecondSI,txtThirdSI,txtFourthSI,txtFirstFloatSI,txtSecondFloatSI);
		secondForm.setSpacing(true);
		secondForm.addStyleName("layoutDesign");
		setReadOnly(secondForm, true);
		
		FormLayout thirdForm = new FormLayout(txtThirdFloatSI,txtFourthFloatSI,txtFirstChangeSI,txtSecondChangeSI,txtThirdChangeSI,txtFourthChangeSI);
		thirdForm.setSpacing(true);
		thirdForm.addStyleName("layoutDesign");
		setReadOnly(thirdForm, true);
		
		FormLayout fourthForm = new FormLayout(txtRemarks,txtPedDeclared,txtPedIcdCode,txtPedDescription,txtFamilySize,txtRequestID,txtMemberEntryDetails);
		fourthForm.setSpacing(true);
		fourthForm.addStyleName("layoutDesign");
		setReadOnly(fourthForm, true);
		
		HorizontalLayout firstHor = new HorizontalLayout(firstForm,secondForm);
		firstHor.setSpacing(true);
		firstHor.setWidth("535px");
		HorizontalLayout secondHor = new HorizontalLayout(thirdForm,fourthForm);
		secondHor.setSpacing(true);
		secondHor.setWidth("535px");
		
		VerticalLayout mainVertical = new VerticalLayout(firstHor,secondHor);
		mainVertical.setSpacing(true);
//		mainVertical.setStyleName("layoutDesign");
		
		setCompositionRoot(mainVertical);

	}
	
	@SuppressWarnings({ "deprecation" })
	private void setReadOnly(FormLayout a_formLayout, boolean readOnly) {
		Iterator<Component> formLayoutLeftComponent = a_formLayout
				.getComponentIterator();
		while (formLayoutLeftComponent.hasNext()) {
			Component c = formLayoutLeftComponent.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setWidth("600px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			} else if (c instanceof TextArea) {
				TextArea field = (TextArea) c;
				field.setWidth("350px");
				field.setNullRepresentation("");
				field.setReadOnly(readOnly);
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}


}
