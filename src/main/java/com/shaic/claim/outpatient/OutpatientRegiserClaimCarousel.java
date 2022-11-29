
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

public class OutpatientRegiserClaimCarousel extends ViewComponent {

	private static final long serialVersionUID = 5892717759896227659L;

	@EJB
	private DBCalculationService dbCalculationService;

	private TextField policyNumber;
	private TextField policyType;
	
	private TextField insuredPatientName;
	private TextField productName;
	
	private TextField pioName;
	
	private TextField policyStartDate;
	private TextField policyEndDate;
	
	private TextField plan;
	private TextField productType;
	
	HorizontalCarousel formsHorizontalLayout;

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
		insuredPatientName.setValue((policy.getInsured() != null && !policy.getInsured().isEmpty() && policy.getInsured().get(0) != null) ? policy.getInsured().get(0).getInsuredName() : "");
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
		
		productType.setReadOnly(false);
		productType.setValue(policy.getProductType() != null && policy.getProductType().getValue() != null ? policy.getProductType().getValue() : "");
		productType.setReadOnly(true);
		
		policyStartDate.setReadOnly(false);
		policyStartDate.setValue(SHAUtils.formatDate(policy.getPolicyFromDate()));
		policyStartDate.setReadOnly(true);
		
		policyEndDate.setReadOnly(false);
		policyEndDate.setValue(SHAUtils.formatDate(policy.getPolicyToDate()));
		policyEndDate.setReadOnly(true);
		
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
		formsHorizontalLayout.setButtonsVisible(false);
		// formsHorizontalLayout.setHeight(115, Unit.PIXELS);

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
