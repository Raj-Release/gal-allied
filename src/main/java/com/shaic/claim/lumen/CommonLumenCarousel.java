package com.shaic.claim.lumen;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CommonLumenCarousel extends ViewComponent{

	private TextField policyNumber;
	private TextField productName;
	private TextField policyType;
	private TextField productType;
	private TextField insuredPatientName;
	private TextField classOfBusiness;
	private TextField insuerdPatientId;
	private TextField pioName;
	private TextField intimationNumber;
	private TextField claimType;
	private TextField insuredName;
	private TextField caseReferenceNumber;

	HorizontalLayout layout1;

	@PostConstruct
	public void initView() {

	}

	public void init(LumenRequestDTO resultDTO, String caption) {
		Panel panel = new Panel(caption);
		panel.setHeight("230px");
		panel.setStyleName("policyinfogrid");
		panel.setContent(buildCarousel());

		enableOrDisableFields(false);		
		if(resultDTO != null ){
			if(resultDTO.getIntimation() != null){
				classOfBusiness.setValue(resultDTO.getIntimation().getLobId().getValue());
				if(resultDTO.getIntimation().getInsured() != null){
					insuerdPatientId.setValue(resultDTO.getIntimation().getInsured().getHealthCardNumber());
					insuredName.setValue(resultDTO.getIntimation().getInsured().getInsuredName());
				}
			}else{
				// added to set data while policy lumen request is selected by user.
				classOfBusiness.setValue("");
				insuerdPatientId.setValue("");
				insuredName.setValue("");
			}
			pioName.setValue(resultDTO.getPolicyIssuingOffice());
			intimationNumber.setValue(resultDTO.getIntimationNumber());
			if(resultDTO.getClaim() != null){
				claimType.setValue(resultDTO.getClaim().getClaimType().getValue());
			}else{
				// added to set data while policy lumen request is selected by user.
				claimType.setValue("");
			}
			if(resultDTO.getPolicy()!= null){
				policyNumber.setValue(resultDTO.getPolicy().getPolicyNumber());
				productName.setValue(resultDTO.getPolicy().getProductName());
				policyType.setValue(resultDTO.getPolicy().getPolicyType().getValue());
				productType.setValue(resultDTO.getPolicy().getProduct().getProductType());
				insuredPatientName.setValue(resultDTO.getPolicy().getProposerFirstName());
			}else{
				policyNumber.setValue(resultDTO.getPolicyNumber());
				productName.setValue(resultDTO.getProductName());
				policyType.setValue("");
				productType.setValue("");
				insuredPatientName.setValue("");
			}
			caseReferenceNumber.setValue(resultDTO.getCaseReferenceNumber());
		}
		enableOrDisableFields(true);
		setCompositionRoot(panel);
	}

	private void enableOrDisableFields(boolean flag){
		policyNumber.setReadOnly(flag);
		productName.setReadOnly(flag);
		policyType.setReadOnly(flag);
		productType.setReadOnly(flag);
		insuredPatientName.setReadOnly(flag);
		classOfBusiness.setReadOnly(flag);
		insuerdPatientId.setReadOnly(flag);
		pioName.setReadOnly(flag);
		intimationNumber.setReadOnly(flag);
		claimType.setReadOnly(flag);
		insuredName.setReadOnly(flag);
		caseReferenceNumber.setReadOnly(flag);
	}

	private HorizontalLayout buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setWidth("400px");		
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		secondForm.setWidth("400px");		

		policyNumber = new TextField("Policy Number");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("-1px");
		policyNumber.setHeight("20px");
		policyNumber.setReadOnly(true);
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth(30, Unit.EM);
		productName.setHeight("20px");
		productName.setReadOnly(true);
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("-1px");
		policyType.setHeight("20px");
		policyType.setReadOnly(true);
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		productType = new TextField("Product Type");
		//Vaadin8-setImmediate() productType.setImmediate(true);
		productType.setWidth(30, Unit.EM);
		productType.setHeight("20px");
		productType.setReadOnly(true);
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredPatientName = new TextField("Proposer Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("-1px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		classOfBusiness = new TextField("Class of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("-1px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.setReadOnly(true);
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuerdPatientId = new TextField("Insured Patient ID No");
		//Vaadin8-setImmediate() insuerdPatientId.setImmediate(true);
		insuerdPatientId.setWidth("-1px");
		insuerdPatientId.setHeight("20px");
		insuerdPatientId.setReadOnly(true);
		insuerdPatientId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		pioName = new TextField("PIO Name");
		//Vaadin8-setImmediate() pioName.setImmediate(true);
		pioName.setWidth("-1px");
		pioName.setHeight("20px");
		pioName.setReadOnly(true);
		pioName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		intimationNumber = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationNumber.setImmediate(true);
		intimationNumber.setWidth("-1px");
		intimationNumber.setHeight("20px");
		intimationNumber.setReadOnly(true);
		intimationNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		claimType = new TextField("Claim Type");
		//Vaadin8-setImmediate() claimType.setImmediate(true);
		claimType.setWidth("-1px");
		claimType.setHeight("20px");
		claimType.setReadOnly(true);
		claimType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth(30, Unit.EM);
		insuredName.setHeight("20px");
		insuredName.setReadOnly(true);
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		caseReferenceNumber = new TextField("Case Reference Number");
		//Vaadin8-setImmediate() caseReferenceNumber.setImmediate(true);
		caseReferenceNumber.setWidth(30, Unit.EM);
		caseReferenceNumber.setHeight("20px");
		caseReferenceNumber.setReadOnly(true);
		caseReferenceNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(policyType);
		firstForm.addComponent(insuredPatientName);
		firstForm.addComponent(insuerdPatientId);
		firstForm.addComponent(intimationNumber);
		firstForm.addComponent(insuredName);

		secondForm.addComponent(productName);
		secondForm.addComponent(productType);
		secondForm.addComponent(classOfBusiness);
		secondForm.addComponent(pioName);
		secondForm.addComponent(claimType);
		secondForm.addComponent(caseReferenceNumber);

		HorizontalLayout formsHorizontalLayout = new HorizontalLayout();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");

		layout1 = new HorizontalLayout(firstForm, secondForm);
		layout1.setWidth("100%");
		layout1.setSpacing(true);
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.setHeight("200px");								
		return formsHorizontalLayout;

	}

}
