package com.shaic.claim.omp.newregistration;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.claim.omp.createintimation.OMPCreateIntimationTableDTO;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPNewRegistrationRevisedCarousel extends ViewComponent{
	
	private static final long serialVersionUID = -3075677095610513136L;
	
	private TextField policyNumber;
	private TextField productName;
	
	private TextField insuredName;
	private TextField plan;
	
	private TextField plantxt;
	private TextField placeofVisit;
	private TextField purposeofVisit;
	private TextField placeofVisittxt;
	private TextField dateofDepfromIndia;
	private TextField dateofReturntoIndia ;
	
	private TextField sumInsured;
	private TextField passportNoDateofExpiry;
	
	private TextField insuredAge;

	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;

	
	@PostConstruct
	public void initView() {

	}

	public void init(OMPNewRegistrationSearchDTO ompIntimationDTO, String caption) {

		Panel panel = new Panel(caption);
		panel.setHeight("180px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
		BeanItem<OMPNewRegistrationSearchDTO> item = new BeanItem<OMPNewRegistrationSearchDTO>(ompIntimationDTO);
		
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		
		policyNumber.setReadOnly(false);
		if(ompIntimationDTO.getPolicy()!= null && ompIntimationDTO.getPolicy().getPolicyNumber()!= null){
		policyNumber.setValue(ompIntimationDTO.getPolicy().getPolicyNumber());
		}
		policyNumber.setReadOnly(true);
		
		productName.setReadOnly(false);
		if(ompIntimationDTO.getPolicy() != null && ompIntimationDTO.getPolicy().getProductName()!= null){
		productName.setValue(ompIntimationDTO.getPolicy().getProductName());
		}
		productName.setReadOnly(true);
		
		insuredName.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getInsuredName()!= null){
		insuredName.setValue(ompIntimationDTO.getInsured().getInsuredName());
		}
		insuredName.setReadOnly(true);
		
		plantxt.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getPlan() != null){
			plantxt.setValue(ompIntimationDTO.getInsured().getPlan());
		}else{
			plantxt.setValue("-");
		}
		
		plantxt.setReadOnly(true);
		
		placeofVisit.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getPlaceOfvisit() != null){
		placeofVisit.setValue(ompIntimationDTO.getInsured().getPlaceOfvisit());
		}else{
			placeofVisit.setValue("-");
		}
		placeofVisit.setReadOnly(true);
		
		purposeofVisit.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getPurposeOfvisit() != null){
		purposeofVisit.setValue(ompIntimationDTO.getInsured().getPurposeOfvisit());
		}else{
			purposeofVisit.setValue("-");
		}
		purposeofVisit.setReadOnly(true);
		
		dateofDepfromIndia.setReadOnly(false);
		if(ompIntimationDTO.getInsured()!= null &&ompIntimationDTO.getInsured().getDepartureDate() != null){
		dateofDepfromIndia.setValue(new SimpleDateFormat("dd-MM-yyyy").format(ompIntimationDTO.getInsured().getDepartureDate() ));
		}else{
			dateofDepfromIndia.setValue("-");
		}
		dateofDepfromIndia.setReadOnly(true);
		
		dateofReturntoIndia.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getReturnDate() != null){
		dateofReturntoIndia.setValue(new SimpleDateFormat("dd-MM-yyyy").format(ompIntimationDTO.getInsured().getReturnDate()));
		}else{
			dateofReturntoIndia.setValue("-");
		}
		dateofReturntoIndia.setReadOnly(true);

		sumInsured.setReadOnly(false);
		if(ompIntimationDTO.getPolicy()!= null && ompIntimationDTO.getPolicy().getTotalSumInsured() != null){
		sumInsured.setValue(String.valueOf(ompIntimationDTO.getPolicy().getTotalSumInsured()));
		}else{
		sumInsured.setValue("-");
		}
		sumInsured.setReadOnly(false);
		
		
		String passportNoExp = "";
		if( ompIntimationDTO.getInsured()!= null && ompIntimationDTO.getInsured().getPassportNo()!= null)
		{
			passportNoExp = ompIntimationDTO.getInsured().getPassportNo();
		}
		if(ompIntimationDTO.getInsured()!= null && ompIntimationDTO.getInsured().getPassPortExpiryDate()!= null)
		{
			passportNoExp = !passportNoExp.isEmpty() ? passportNoExp + " - " + new SimpleDateFormat("MM-dd-yyyy").format(ompIntimationDTO.getInsured().getPassPortExpiryDate()):new SimpleDateFormat("dd-MM-yyyy").format(ompIntimationDTO.getInsured().getPassPortExpiryDate());
		}else{
			passportNoDateofExpiry.setReadOnly(false);
			passportNoDateofExpiry.setValue("-");
			passportNoDateofExpiry.setReadOnly(false);
		}
		
		passportNoDateofExpiry.setReadOnly(false);
		passportNoDateofExpiry.setValue(passportNoExp);
		passportNoDateofExpiry.setReadOnly(false);
		
		insuredAge.setReadOnly(false);
		if(ompIntimationDTO.getInsured() != null && ompIntimationDTO.getInsured().getInsuredAge()!= null){
			long insurAge =(new Double( ompIntimationDTO.getInsured().getInsuredAge())).longValue();
		insuredAge.setValue(String.valueOf(insurAge));
		}
		insuredAge.setReadOnly(true);
		
		
		
		setCompositionRoot(panel);
	}

	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setWidth("400px");		
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		FormLayout thirdForm = new FormLayout();
		thirdForm.setSpacing(false);		
			
		policyNumber = new TextField("Policy Number");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("-1px");
		policyNumber.setHeight("20px");
		policyNumber.setReadOnly(true);
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("-1px");
		productName.setHeight("20px");
		productName.setReadOnly(true);
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("-1px");
		insuredName.setHeight("20px");
		insuredName.setReadOnly(true);
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		plantxt = new TextField("Plan");
		//Vaadin8-setImmediate() plantxt.setImmediate(true);
		plantxt.setWidth("-1px");
		plantxt.setHeight("20px");
		plantxt.setReadOnly(true);
		plantxt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		placeofVisit = new TextField("Place of Visit");
		//Vaadin8-setImmediate() placeofVisit.setImmediate(true);
		placeofVisit.setWidth("-1px");
		placeofVisit.setHeight("20px");
		placeofVisit.setReadOnly(true);
		placeofVisit.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		purposeofVisit = new TextField("Purpose of Visit");
		//Vaadin8-setImmediate() purposeofVisit.setImmediate(true);
		purposeofVisit.setWidth("-1px");
		purposeofVisit.setHeight("20px");
		purposeofVisit.setReadOnly(true);
		purposeofVisit.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofDepfromIndia = new TextField("Date of Dep from India");
		//Vaadin8-setImmediate() dateofDepfromIndia.setImmediate(true);
		dateofDepfromIndia.setWidth("-1px");
		dateofDepfromIndia.setHeight("20px");
		dateofDepfromIndia.setReadOnly(true);
		dateofDepfromIndia.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofReturntoIndia = new TextField("Date of Return to India");
		//Vaadin8-setImmediate() dateofReturntoIndia.setImmediate(true);
		dateofReturntoIndia.setWidth("-1px");
		dateofReturntoIndia.setHeight("20px");
		dateofReturntoIndia.setReadOnly(true);
		dateofReturntoIndia.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		sumInsured = new TextField("Sum Insured");
		//Vaadin8-setImmediate() sumInsured.setImmediate(true);
		sumInsured.setWidth("-1px");
		sumInsured.setHeight("20px");
		sumInsured.setReadOnly(true);
		sumInsured.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		passportNoDateofExpiry = new TextField("Passport No. & Date of Expiry");
		//Vaadin8-setImmediate() passportNoDateofExpiry.setImmediate(true);
		passportNoDateofExpiry.setWidth("-1px");
		passportNoDateofExpiry.setHeight("20px");
		passportNoDateofExpiry.setReadOnly(true);
		passportNoDateofExpiry.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		insuredAge = new TextField("Insured Age");
		//Vaadin8-setImmediate() insuredAge.setImmediate(true);
		insuredAge.setWidth("-1px");
		insuredAge.setHeight("20px");
		insuredAge.setReadOnly(true);
		insuredAge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(productName);
		firstForm.addComponent(insuredName);
		firstForm.addComponent(plantxt);
		
		secondForm.addComponent(placeofVisit);
		secondForm.addComponent(purposeofVisit);
		secondForm.addComponent(dateofDepfromIndia);
		secondForm.addComponent(dateofReturntoIndia);
		
		thirdForm.addComponent(sumInsured);
		thirdForm.addComponent(passportNoDateofExpiry);
		thirdForm.addComponent(insuredAge);
		
		HorizontalCarousel formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");

		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");

		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		

		// Only react to arrow keys when focused
		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);
		layout1 = new HorizontalLayout(firstForm, secondForm);
		layout1.setWidth("100%");
		layout1.setSpacing(true);
		layout2 = new HorizontalLayout(thirdForm);
		layout2.setSpacing(true);
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.setHeight("150px");								
		return formsHorizontalLayout;
		
	}
}
