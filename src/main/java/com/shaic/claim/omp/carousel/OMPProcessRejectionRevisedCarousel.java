package com.shaic.claim.omp.carousel;

import javax.annotation.PostConstruct;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.claim.ClaimDto;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class OMPProcessRejectionRevisedCarousel extends ViewComponent{
	
	
	private TextField intimationno;
	private TextField intimatorName;
	private TextField intimationDate;
	private TextField intimatedBy;
	
	private TextField modeofIntimation;
	private TextField nameofCallAttendee;
	private TextField lossDateTime;
	
	private TextField admissionDate;
	private TextField policyNo;
	private TextField proposerName;
	private TextField insuredName;

	private TextField productName;
	private TextField plan;
	private TextField passportNoDateofExpir;
	private TextField dateofDischarge;
	
	private TextField placeofLossDelay;
	private TextField sponsorName;
	
	
	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;
	
	@PostConstruct
	public void initView() {

	}

	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption) {

		Panel panel = new Panel(caption);
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
/*
		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		dateOfDischarge.setReadOnly(false);
		dateOfDischarge.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		dateOfDischarge.setReadOnly(true);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		insuredPatientName.setReadOnly(true);
		claimTypeField.setReadOnly(false);
		// claimType.setValue(StringUtils.equalsIgnoreCase("Network",
		// newIntimationDTO.getHospitalDto().getHospitalType().getValue()) ?
		// "Cashless" : "Reimbursement");

		srCitizenClaim.setReadOnly(false);
		if (newIntimationDTO.getInsuredPatient().getInsuredAge() != null
				&& newIntimationDTO.getInsuredPatient().getInsuredAge() != 0) {
			srCitizenClaim.setValue(Double.valueOf(newIntimationDTO
					.getInsuredPatient().getInsuredAge()) >= 60 ? "YES" : "NO");
		}
		srCitizenClaim.setReadOnly(true);

		claimId.setReadOnly(false);
		claimId.setValue(claimDTO.getClaimId());
		claimId.setReadOnly(true);
		claimId.setWidth("400px");
		// claimId.setHeight("20px");
		if (null != claimDTO.getClaimType()
				&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(claimDTO
					.getClaimType().getId())) {
			claimTypeField.setValue("Reimbursement");
//			claimTypeField.setValue(newIntimationDTO.getClaimType().getValue());
		} else {
			claimTypeField.setValue("Cashless");
		}

		claimTypeField.setReadOnly(true);

		if (claimDTO.getCurrencyId() != null) {
			originalSI.setReadOnly(false);
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? newIntimationDTO.getOrginalSI().toString() : "0");
			originalSI.setReadOnly(true);
		}

		policyType.setReadOnly(false);
		policyType
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getValue()
						: "");
		policyType.setReadOnly(true);
		
		portabilityPolicy.setReadOnly(false);
		portabilityPolicy
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getKey() !=null ?newIntimationDTO
								.getPolicy().getPolicyType().getKey().equals(ReferenceTable.PORTABILITY_POLICY_TYPE)?"YES":"NO"
						: "":"");
		portabilityPolicy.setReadOnly(true);

		// buildCarosuelDetials(intimationDto);

		setAddtionalValuesforCarousel(newIntimationDTO);*/

		// if(("Network").equalsIgnoreCase(newIntimationDTO.getHospitalType().getValue()))
		// {
		// claimType.setValue("Cashless");
		// }
		// else
		// {
		// claimType.setValue("Re-imbursement");
		// }
		//
		// if(null != newIntimationDTO.getCpuCode()){
		// cpuCode.setValue(newIntimationDTO.getCpuCode());
		// }
		// if(null != newIntimationDTO.getPolicy())
		// {
		// String strCustId = newIntimationDTO.getPolicy().getInsuredId();
		// customerId.setValue(strCustId);
		// policyNumber.setValue(newIntimationDTO.getPolicy().getPolicyNumber());
		// insuredName.setValue(newIntimationDTO.getPolicy().getInsuredFirstName());
		// }
		// if(null != newIntimationDTO.getHospitalDto())
		// {
		// String hosp = newIntimationDTO.getHospitalDto().getName() != null ?
		// newIntimationDTO.getHospitalDto().getName() : "";
		// hospitalName.setValue(StringUtils.trim(hosp));
		// }
		// if( null != newIntimationDTO.getHospitalType())
		// {
		// hospitalTypeName.setValue(newIntimationDTO.getHospitalType().getValue());
		// }
		// if(null != newIntimationDTO.getPolicy().getProduct())
		// {
		// productName.setValue(newIntimationDTO.getPolicy().getProduct().getValue());
		// }
		// if(null != newIntimationDTO.getPolicy().getProductType())
		// {
		// productType.setValue(newIntimationDTO.getPolicy().getProductType().getValue());
		// }
		// claimType.setReadOnly(true);
		// hospitalName.setReadOnly(true);
		// customerId.setReadOnly(true);
		// policyNumber.setReadOnly(true);
		// insuredName.setReadOnly(true);
		// hospitalTypeName.setReadOnly(true);
		// productName.setReadOnly(true);
		// productType.setReadOnly(true);

		setCompositionRoot(panel);
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
		

		intimationno = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationno.setImmediate(true);
		intimationno.setWidth("-1px");
		intimationno.setHeight("20px");
		intimationno.setReadOnly(true);
		intimationno.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
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
		
		lossDateTime = new TextField("Loss Date & Time");
		//Vaadin8-setImmediate() lossDateTime.setImmediate(true);
		lossDateTime.setWidth("-1px");
		lossDateTime.setHeight("20px");
		lossDateTime.setReadOnly(true);
		lossDateTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
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
		
		passportNoDateofExpir = new TextField("Passport No. & Date of Expiry");
		//Vaadin8-setImmediate() passportNoDateofExpir.setImmediate(true);
		passportNoDateofExpir.setWidth("-1px");
		passportNoDateofExpir.setHeight("20px");
		passportNoDateofExpir.setReadOnly(true);
		passportNoDateofExpir.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofDischarge = new TextField("Date of Discharge");
		//Vaadin8-setImmediate() dateofDischarge.setImmediate(true);
		dateofDischarge.setWidth("-1px");
		dateofDischarge.setHeight("20px");
		dateofDischarge.setReadOnly(true);
		dateofDischarge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		placeofLossDelay = new TextField("Place of Loss/Delay");
		//Vaadin8-setImmediate() placeofLossDelay.setImmediate(true);
		placeofLossDelay.setWidth("-1px");
		placeofLossDelay.setHeight("20px");
		placeofLossDelay.setReadOnly(true);
		placeofLossDelay.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		sponsorName = new TextField("Sponsor Name");
		//Vaadin8-setImmediate() sponsorName.setImmediate(true);
		sponsorName.setWidth("-1px");
		sponsorName.setHeight("20px");
		sponsorName.setReadOnly(true);
		sponsorName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		firstForm.addComponent(intimationno);
		firstForm.addComponent(intimatorName);
		firstForm.addComponent(intimationDate);
		firstForm.addComponent(intimatedBy);
		
		secondForm.addComponent(modeofIntimation);
		secondForm.addComponent(nameofCallAttendee);
		secondForm.addComponent(lossDateTime);
		
		
		thirdForm.addComponent(admissionDate);
		thirdForm.addComponent(policyNo);
		thirdForm.addComponent(proposerName);
		thirdForm.addComponent(insuredName);
				
		fourthForm.addComponent(productName);
		fourthForm.addComponent(plan);
		fourthForm.addComponent(passportNoDateofExpir);
		fourthForm.addComponent(dateofDischarge);
	
		fifthForm.addComponent(placeofLossDelay);
		fifthForm.addComponent(sponsorName);
		
		
		// Only react to arrow keys when focused
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

	
}
