package com.shaic.claim.omp.carousel;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class OMPClaimRegistrationRevisedCarousel  extends ViewComponent{
	
	
	
	private TextField intimationId;
	private TextField intimatorName;
	private TextField intimationDate;
	private TextField intimatedBy;

	private TextField modeofIntimation;
	private TextField nameofCallAttendee;
	private TextField lossDateAndTime;
	private TextField tPAIntimationNo;
	
	private TextField admissionDate;
	private TextField policyNo;
	private TextField proposerName;
	private TextField insuredName;
	
	private TextField productName;
	private TextField plan;
	private TextField passportnoDateofExpiry;
	private TextField dateofDischarge;
	
	private TextField txtPlaceofLossDelay;
	private TextField txtSponsorName;
	
	HorizontalCarousel formsHorizontalLayout;
	

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;
	
	@PostConstruct
	public void initView() {

	}

	public void init(NewIntimationDto newIntimationDTO){
		
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() CarouselVLayout.setImmediate(true);
		CarouselVLayout.setHeight("130px");
		CarouselVLayout.addComponent(buildCarousel());		
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(newIntimationDTO);
	    FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        buildCarosuelDetials(newIntimationDTO);
       
//        insuredPatientName.setReadOnly(false);
//        insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
//        insuredPatientName.setReadOnly(true);
//        txtClaimType.setReadOnly(false);
//        txtClaimType.setValue(StringUtils.equalsIgnoreCase("Network", newIntimationDTO.getHospitalType().getValue()) ? "Cashless" : "Reimbursement");
//        txtClaimType.setReadOnly(true);

        
//        srCitizenClaim.setReadOnly(false);
//        if(intimationDto.getInsuredAge() != null && intimationDto.getInsuredAge() != "") {
//        	srCitizenClaim.setValue(Integer.valueOf(intimationDto.getInsuredAge()) > 60 ? "YES" : "NO");
//        }
//        srCitizenClaim.setReadOnly(true);
//        HorizontalLayout viewHLayout = buildViewDetailsLayout();
//        CarouselVLayout.addComponent(viewHLayout);
//        CarouselVLayout.setComponentAlignment(viewHLayout, Alignment.MIDDLE_RIGHT);
        
   

	setCompositionRoot(CarouselVLayout);
	}

	
	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setMargin(false);
		FormLayout secondForm = new FormLayout();
		secondForm.setSpacing(false);
		secondForm.setMargin(false);
		FormLayout thirdForm = new FormLayout();
		thirdForm.setSpacing(false);
		thirdForm.setMargin(false);
		FormLayout fourthForm = new FormLayout();
		fourthForm.setSpacing(false);
		fourthForm.setMargin(false);
		FormLayout fifthForm = new FormLayout();
		fifthForm.setSpacing(false);
		fifthForm.setMargin(false);
		
		intimationId = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationId.setImmediate(true);
		intimationId.setWidth("-1px");
		intimationId.setHeight("20px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		intimatorName = new TextField("Intimator Name");
		//Vaadin8-setImmediate() intimatorName.setImmediate(true);
		intimatorName.setWidth("-1px");
		intimatorName.setHeight("20px");
		intimatorName.setReadOnly(true);
		intimatorName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intimationDate = new TextField("Intimation Date");
		//Vaadin8-setImmediate() intimationDate.setImmediate(true);
		intimationDate.setWidth("-1px");
		intimationDate.setHeight("20px");
		intimationDate.setReadOnly(true);
		intimationDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		intimatedBy = new TextField("Intimated By");
		//Vaadin8-setImmediate() intimatedBy.setImmediate(true);
		intimatedBy.setWidth("-1px");
		intimatedBy.setHeight("20px");
		intimatedBy.setReadOnly(true);
		intimatedBy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		modeofIntimation = new TextField("Mode of Intimation");
		//Vaadin8-setImmediate() modeofIntimation.setImmediate(true);
		modeofIntimation.setWidth("-1px");
		modeofIntimation.setHeight("20px");
		modeofIntimation.setReadOnly(true);
		modeofIntimation.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		nameofCallAttendee = new TextField("Name of Call Attendee");
		//Vaadin8-setImmediate() nameofCallAttendee.setImmediate(true);
		nameofCallAttendee.setWidth("-1px");
		nameofCallAttendee.setHeight("20px");
		nameofCallAttendee.setReadOnly(true);
		nameofCallAttendee.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		lossDateAndTime = new TextField("Loss Date & Time");
		//Vaadin8-setImmediate() lossDateAndTime.setImmediate(true);
		lossDateAndTime.setWidth("-1px");
		lossDateAndTime.setHeight("20px");
		lossDateAndTime.setReadOnly(true);
		lossDateAndTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		tPAIntimationNo = new TextField("TPA Intimation No");
		//Vaadin8-setImmediate() tPAIntimationNo.setImmediate(true);
		tPAIntimationNo.setWidth("-1px");
		tPAIntimationNo.setHeight("20px");
		tPAIntimationNo.setReadOnly(true);
		tPAIntimationNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		admissionDate = new TextField("Admission Date");
		//Vaadin8-setImmediate() admissionDate.setImmediate(true);
		admissionDate.setWidth("-1px");
		admissionDate.setHeight("20px");
		admissionDate.setReadOnly(true);
		admissionDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyNo = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNo.setImmediate(true);
		policyNo.setWidth("-1px");
		policyNo.setHeight("20px");
		policyNo.setReadOnly(true);
		policyNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		proposerName = new TextField("Proposer Name");
		//Vaadin8-setImmediate() proposerName.setImmediate(true);
		proposerName.setWidth("-1px");
		proposerName.setHeight("20px");
		proposerName.setReadOnly(true);
		proposerName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("-1px");
		insuredName.setHeight("20px");
		insuredName.setReadOnly(true);
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("-1px");
		productName.setHeight("20px");
		productName.setReadOnly(true);
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		plan = new TextField("Plan");
		//Vaadin8-setImmediate() plan.setImmediate(true);
		plan.setWidth("-1px");
		plan.setHeight("20px");
		plan.setReadOnly(true);
		plan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		passportnoDateofExpiry = new TextField("Passport No. & Date of Expiry");
		//Vaadin8-setImmediate() passportnoDateofExpiry.setImmediate(true);
		passportnoDateofExpiry.setWidth("-1px");
		passportnoDateofExpiry.setHeight("20px");
		passportnoDateofExpiry.setReadOnly(true);
		passportnoDateofExpiry.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofDischarge = new TextField("Date of Discharge");
		//Vaadin8-setImmediate() dateofDischarge.setImmediate(true);
		dateofDischarge.setWidth("-1px");
		dateofDischarge.setHeight("20px");
		dateofDischarge.setReadOnly(true);
		dateofDischarge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtPlaceofLossDelay = new TextField("Place of Loss/Delay");
		//Vaadin8-setImmediate() txtPlaceofLossDelay.setImmediate(true);
		txtPlaceofLossDelay.setWidth("-1px");
		txtPlaceofLossDelay.setHeight("20px");
		txtPlaceofLossDelay.setReadOnly(true);
		txtPlaceofLossDelay.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtSponsorName = new TextField("Sponsor Name");
		//Vaadin8-setImmediate() txtSponsorName.setImmediate(true);
		txtSponsorName.setWidth("-1px");
		txtSponsorName.setHeight("20px");
		txtSponsorName.setReadOnly(true);
		txtSponsorName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		firstForm.addComponent(intimationId);
		firstForm.addComponent(intimatorName);
		firstForm.addComponent(intimationDate);
		firstForm.addComponent(intimatedBy);
		
		secondForm.addComponent(modeofIntimation);
		secondForm.addComponent(nameofCallAttendee);
		secondForm.addComponent(lossDateAndTime);
		secondForm.addComponent(tPAIntimationNo);
		
		
		thirdForm.addComponent(admissionDate);
		thirdForm.addComponent(policyNo);
		thirdForm.addComponent(proposerName);
		thirdForm.addComponent(insuredName);
				
		fourthForm.addComponent(productName);
		fourthForm.addComponent(plan);
		fourthForm.addComponent(passportnoDateofExpiry);
		fourthForm.addComponent(dateofDischarge);
	
		fifthForm.addComponent(txtPlaceofLossDelay);
		fifthForm.addComponent(txtSponsorName);
		// Only react to arrow keys when focused
		
		HorizontalCarousel formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");
//		formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		
				formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
				// Fetch children lazily
				formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
				// Transition animations between the children run 500 milliseconds
				formsHorizontalLayout.setTransitionDuration(50);
				layout1 = new HorizontalLayout(firstForm, secondForm);
				layout1.setWidth("100%");
				layout1.setSpacing(true);
				layout2 = new HorizontalLayout(thirdForm,fourthForm);
				layout2.setSpacing(true);
				layout3 = new HorizontalLayout(fifthForm);
				layout3.setSpacing(true);
				formsHorizontalLayout.addComponent(layout1);
				formsHorizontalLayout.addComponent(layout2);
				formsHorizontalLayout.addComponent(layout3);
				formsHorizontalLayout.setHeight("150px");
		     
				return formsHorizontalLayout;
	}
	
	
	private void buildCarosuelDetials(NewIntimationDto newIntimationDTO)
	{
		
		 if(null != newIntimationDTO.getIntimationId())
		 {
			 intimationId.setValue(newIntimationDTO.getIntimationId());
		 }
		 
		 if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
				plan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
		 }
		 
	}
	
	
}
