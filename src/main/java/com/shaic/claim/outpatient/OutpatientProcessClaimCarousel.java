
package com.shaic.claim.outpatient;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.Policy;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OutpatientProcessClaimCarousel extends ViewComponent {
	
/*

	private static final long serialVersionUID = 5892717759896227659L;

	@EJB
	private DBCalculationService dbCalculationService;

	private TextField policyNumber;
	private TextField policyType;
	private TextField insuredPatientName;
	
	private TextField insuredPatientID;
	private TextField productName;
	private TextField productType;
	
	private TextField classOfBusiness;
	private TextField pioName;
	private TextField claimType;
	
	private TextField intimationNumber;
	
	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;

	FormLayout fifthForm;

	@PostConstruct
	public void initView() {

	}
	
	public void init(Policy policy, NewIntimationDto dto, ClaimDto claimDto, String pioNameValue) {
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setCaption("Process Claim (OP Check-up)");
		CarouselVLayout.setStyleName("policyinfogrid");
//		CarouselVLayout.setHeight("90px");
		CarouselVLayout.addComponent(buildCarousel());
		policyNumber.setReadOnly(false);
		policyNumber.setValue(policy.getPolicyNumber());
		policyNumber.setReadOnly(true);
		policyType.setReadOnly(false);
		policyType.setValue(policy.getPolicyType() != null ? policy.getPolicyType().getValue() : "");
		policyType.setReadOnly(true);
		productName.setReadOnly(false);
		productName.setValue(policy.getProduct() != null ? policy.getProduct().getValue() : "");
		productName.setReadOnly(true);
		pioName.setReadOnly(false);
		pioName.setValue(pioNameValue);
		pioName.setReadOnly(true);
		productType.setReadOnly(false);
		productType.setValue(policy.getProduct() != null ? policy.getProduct().getProductType() : "");
		productType.setReadOnly(true);
		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(dto.getInsuredPatient().getInsuredName());
		insuredPatientName.setReadOnly(true);
		insuredPatientID.setReadOnly(false);
		insuredPatientID.setValue( String.valueOf(dto.getInsuredPatient().getInsuredId()) );
		insuredPatientID.setReadOnly(true);
		claimType.setReadOnly(false);
		claimType.setValue(claimDto.getClaimType().getValue());
		claimType.setReadOnly(true);
		intimationNumber.setReadOnly(false);
		intimationNumber.setValue(dto.getIntimationId());
		intimationNumber.setReadOnly(true);
		
		classOfBusiness.setReadOnly(false);
		classOfBusiness.setValue("Health");
		classOfBusiness.setReadOnly(true);

		setCompositionRoot(CarouselVLayout);
	}
	
	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setWidth("400px");
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		FormLayout thirdForm = new FormLayout();
		thirdForm.setSpacing(false);
		FormLayout fourthForm = new FormLayout();
		fourthForm.setSpacing(false);
		fifthForm = new FormLayout();
		fifthForm.setSpacing(false);
		FormLayout sixthForm = new FormLayout();
		sixthForm.setSpacing(false);
		
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyNumber);
		
		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyType);
		
		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(insuredPatientName);
		
		insuredPatientID = new TextField("Insured Patient ID No");
		//Vaadin8-setImmediate() insuredPatientID.setImmediate(true);
		insuredPatientID.setWidth("300px");
		insuredPatientID.setHeight("20px");
		insuredPatientID.setReadOnly(true);
		insuredPatientID.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(insuredPatientID);
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("350px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(productName);
		
		productType = new TextField("Product Type");
		//Vaadin8-setImmediate() productType.setImmediate(true);
		productType.setWidth("350px");
		productType.setHeight("20px");
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(productType);
		
		classOfBusiness = new TextField("Class of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("350px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(classOfBusiness);

		pioName = new TextField("PIO Name");
		//Vaadin8-setImmediate() pioName.setImmediate(true);
		pioName.setWidth("-1px");
		pioName.setHeight("20px");
		pioName.setReadOnly(true);
		pioName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(pioName);

		claimType = new TextField("Claim Type");
		//Vaadin8-setImmediate() claimType.setImmediate(true);
		claimType.setWidth("-1px");
		claimType.setHeight("20px");
		claimType.setReadOnly(true);
		claimType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(claimType);
		
		intimationNumber = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationNumber.setImmediate(true);
		intimationNumber.setWidth("-1px");
		intimationNumber.setHeight("20px");
		intimationNumber.setReadOnly(true);
		intimationNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(intimationNumber);

		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		// formsHorizontalLayout.setHeight(115, Unit.PIXELS);

		// Only react to arrow keys when focused
		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);

		layout1 = new HorizontalLayout(firstForm, secondForm, thirdForm);
		layout2 = new HorizontalLayout(fourthForm);

		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
//		formsHorizontalLayout.setHeight("110px");
     
		return formsHorizontalLayout;
	}

	
*/
	

	private static final long serialVersionUID = -6465191662242352039L;
	
	@EJB
	private DBCalculationService dbCalculationService;

	private TextField policyNumber;
	private TextField policyType;
	
	private TextField insuredPatientName;
	private TextField productName;
	
	private TextField pioName;
	
	private TextField policyStartDate;
	private TextField policyEndDate;
	
	HorizontalCarousel formsHorizontalLayout;
	
	private TextField plan;
	private TextField productType;

	HorizontalLayout layout1;
	HorizontalLayout layout2;

	FormLayout fifthForm;

	@PostConstruct
	public void initView() {

	}
	
	public void init(Policy policy, String pioNameValue) {
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setCaption("Register Claim");
		CarouselVLayout.setStyleName("policyinfogrid");
		CarouselVLayout.addComponent(buildCarousel());
		
		policyNumber.setReadOnly(false);
		policyNumber.setValue(policy.getPolicyNumber());
		policyNumber.setReadOnly(true);
		policyType.setReadOnly(false);
		policyType.setValue(policy.getPolicyType() != null ? policy.getPolicyType().getValue() : "");
		policyType.setReadOnly(true);
		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue((policy.getInsured() != null && policy.getInsured().get(0) != null) ? policy.getInsured().get(0).getInsuredName() : "");
		insuredPatientName.setReadOnly(true);
		productName.setReadOnly(false);
		productName.setValue(policy.getProduct() != null ? policy.getProduct().getValue() : "");
		productName.setReadOnly(true);
		pioName.setReadOnly(false);
		pioName.setValue(pioNameValue);
		pioName.setReadOnly(true);
		plan.setReadOnly(false);
		plan.setValue(policy.getPolicyPlan());
		plan.setReadOnly(true);
		plan.setNullRepresentation("");
		
		policyStartDate.setReadOnly(false);
		policyStartDate.setValue(policy.getPolicyFromDate() != null ? SHAUtils.formatDate(policy.getPolicyFromDate()) : "");
		policyStartDate.setReadOnly(true);
		
		policyEndDate.setReadOnly(false);
		policyEndDate.setValue(policy.getPolicyToDate() != null ? SHAUtils.formatDate(policy.getPolicyToDate()) : "");
		policyEndDate.setReadOnly(true);
		
		productType.setReadOnly(false);
		productType.setValue(policy.getProductType() != null && policy.getProductType().getValue() != null ? policy.getProductType().getValue() : "");
		productType.setReadOnly(true);
		
		setCompositionRoot(CarouselVLayout);
	}
	
	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setMargin(false);
		firstForm.setWidth("400px");
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		secondForm.setMargin(false);
		
//		FormLayout thirdForm = new FormLayout();
//		thirdForm.setSpacing(false);
//		thirdForm.setMargin(false);
//		FormLayout fourthForm = new FormLayout();
//		fourthForm.setSpacing(false);
//		fourthForm.setMargin(false);
//		fifthForm = new FormLayout();
//		fifthForm.setSpacing(false);
//		fifthForm.setMargin(false);
//		FormLayout sixthForm = new FormLayout();
//		sixthForm.setMargin(false);
//		sixthForm.setSpacing(false);
		
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyNumber);
		
		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyType);
		
		policyStartDate = new TextField("Policy From Date");
		//Vaadin8-setImmediate() policyStartDate.setImmediate(true);
		policyStartDate.setWidth("180px");
		policyStartDate.setHeight("20px");
		policyStartDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyStartDate);
		
		
		policyEndDate = new TextField("Policy To Date");
		//Vaadin8-setImmediate() policyEndDate.setImmediate(true);
		policyEndDate.setWidth("180px");
		policyEndDate.setHeight("20px");
		policyEndDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyEndDate);
		
		productType = new TextField("Product Type");
		productType.setWidth("180px");
		productType.setHeight("20px");
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(productType);
		
		insuredPatientName = new TextField("Proposer Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(insuredPatientName);
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("350px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(productName);

		pioName = new TextField("PIO Name");
		//Vaadin8-setImmediate() pioName.setImmediate(true);
		pioName.setWidth("-1px");
		pioName.setHeight("20px");
		pioName.setReadOnly(true);
		pioName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(pioName);
		
		plan = new TextField("Plan");
		plan.setWidth("-1px");
		plan.setHeight("20px");
		plan.setReadOnly(true);
		plan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(plan);

		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		// formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		formsHorizontalLayout.setButtonsVisible(false);
		// Only react to arrow keys when focused
//		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);

		layout1 = new HorizontalLayout(firstForm, secondForm);

		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.setHeight("150px");
     
		return formsHorizontalLayout;
	}
}
