
package com.shaic.claim.preauth;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PreauthIntimationDetailsCarousel extends ViewComponent {

	private static final long serialVersionUID = 5892717759896227659L;

	@EJB
	private DBCalculationService dbCalculationService;

	private TextField intimationId;
	private TextField claimId;
	private TextField claimTypeField;
	private TextField cpuCode;

//	private TextField currency;
	private TextField originalSI;
	private TextField insuredPatientName;
	private TextField customerId;
	private TextField hospitalName;
	private TextField hospitalCity;
	private TextField hospitalIrdaCode;

	private TextField hospitalTypeName;
	private TextField admissionDateForCarousel;
	private TextField policyNumber;
	private TextField policyType;
	private TextField txtPolicyAgeing;

	private TextField insuredName;
	private TextField srCitizenClaim;
	private TextField productName;
	private TextField productType;

	private TextField classOfBusiness;
	private TextField netWorkHospitalType;

	private TextField policyYear;
	private TextField insuredAge;
	
	private TextField policyPlan;

	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;

	FormLayout fifthForm;

	@PostConstruct
	public void initView() {

	}

	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption) {

		Panel panel = new Panel(caption);
		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);

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

		// buildCarosuelDetials(intimationDto);

		setAddtionalValuesforCarousel(newIntimationDTO);

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

	public void init(NewIntimationDto intimationDto) {
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setStyleName("policyinfogrid");
		CarouselVLayout.setHeight("130px");
		CarouselVLayout.addComponent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				intimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		setAddtionalValuesforCarousel(intimationDto);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(intimationDto.getInsuredPatientName());
		insuredPatientName.setReadOnly(true);
		claimTypeField.setReadOnly(false);

		claimTypeField.setValue(StringUtils.equalsIgnoreCase("Network",
				intimationDto.getHospitalType().getValue()) ? "Cashless"
				: "Reimbursement");
		claimTypeField.setReadOnly(true);

		srCitizenClaim.setReadOnly(false);
		if (intimationDto.getInsuredAge() != null
				&& intimationDto.getInsuredAge() != "") {
			srCitizenClaim.setValue(Integer.valueOf(intimationDto
					.getInsuredAge()) > 60 ? "YES" : "NO");
		}
		srCitizenClaim.setReadOnly(true);
		setCompositionRoot(CarouselVLayout);
	}

	public void init(NewIntimationDto intimationDto, String caption) {

		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setCaption(caption);
		CarouselVLayout.setStyleName("policyinfogrid");
		CarouselVLayout.setHeight("130px");
		CarouselVLayout.addComponent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				intimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		setAddtionalValuesforCarousel(intimationDto);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(intimationDto.getInsuredPatientName());
		insuredPatientName.setReadOnly(true);

		claimTypeField.setReadOnly(false);
		claimTypeField.setValue(StringUtils.equalsIgnoreCase("Network",
				intimationDto.getHospitalType().getValue()) ? "Cashless"
				: "Reimbursement");
		claimTypeField.setReadOnly(true);

		srCitizenClaim.setReadOnly(false);
		if (intimationDto.getInsuredAge() != null
				&& intimationDto.getInsuredAge() != "") {
			srCitizenClaim.setValue(Integer.valueOf(intimationDto
					.getInsuredAge()) > 60 ? "YES" : "NO");
		}
		srCitizenClaim.setReadOnly(true);
		setCompositionRoot(CarouselVLayout);
	}

	private HorizontalCarousel buildCarousel() {
		FormLayout firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setWidth("400px");
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
		fifthForm = new FormLayout();
		fifthForm.setSpacing(false);
		fifthForm.setMargin(false);
		FormLayout sixthForm = new FormLayout();
		sixthForm.setSpacing(false);
		sixthForm.setMargin(false);

		intimationId = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationId.setImmediate(true);
		intimationId.setWidth("-1px");
		intimationId.setHeight("20px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimationId);

		claimId = new TextField("Claim Number");
		//Vaadin8-setImmediate() claimId.setImmediate(true);
		claimId.setWidth("160px");
		claimId.setHeight("20px");
		claimId.setReadOnly(true);
		claimId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(claimId);

		claimTypeField = new TextField("Claim Type");
		//Vaadin8-setImmediate() claimTypeField.setImmediate(true);
		claimTypeField.setWidth("-1px");
		claimTypeField.setHeight("20px");
		claimTypeField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(claimTypeField);

		cpuCode = new TextField("CPU Code");
		//Vaadin8-setImmediate() cpuCode.setImmediate(true);
		cpuCode.setWidth("-1px");
		cpuCode.setHeight("20px");
		cpuCode.setReadOnly(true);
		cpuCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(cpuCode);

		originalSI = new TextField("Original SI");
		//Vaadin8-setImmediate() originalSI.setImmediate(true);
		originalSI.setWidth("-1px");
		originalSI.setHeight("20px");
		originalSI.setReadOnly(true);
		originalSI.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(originalSI);

		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(insuredPatientName);

		customerId = new TextField("Customer Id");
		//Vaadin8-setImmediate() customerId.setImmediate(true);
		customerId.setWidth("-1px");
		customerId.setHeight("20px");
		customerId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(customerId);

		hospitalName = new TextField("Hospital Name");
		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setWidth("300px");
		hospitalName.setHeight("20px");
		hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(hospitalName);
		
		hospitalCity = new TextField("Hospital City");
		//Vaadin8-setImmediate() hospitalCity.setImmediate(true);
		hospitalCity.setWidth("300px");
		hospitalCity.setHeight("20px");
		hospitalCity.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(hospitalCity);
		
		hospitalIrdaCode = new TextField("Hospital IRDA Code");
		//Vaadin8-setImmediate() hospitalIrdaCode.setImmediate(true);
		hospitalIrdaCode.setWidth("300px");
		hospitalIrdaCode.setHeight("20px");
		hospitalIrdaCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(hospitalIrdaCode);
		

		hospitalTypeName = new TextField("Hospital Type");
		//Vaadin8-setImmediate() hospitalTypeName.setImmediate(true);
		hospitalTypeName.setWidth("-1px");
		hospitalTypeName.setHeight("20px");
		hospitalTypeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(hospitalTypeName);

		admissionDateForCarousel = new TextField("Admission Date");
		//Vaadin8-setImmediate() admissionDateForCarousel.setImmediate(true);
		admissionDateForCarousel.setWidth("200px");
		admissionDateForCarousel.setHeight("20px");
		admissionDateForCarousel.setReadOnly(true);
		admissionDateForCarousel.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(admissionDateForCarousel);

		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(policyNumber);

		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(policyType);

		txtPolicyAgeing = new TextField("Policy Ageing");
		//Vaadin8-setImmediate() txtPolicyAgeing.setImmediate(true);
		txtPolicyAgeing.setWidth("180px");
		txtPolicyAgeing.setHeight("20px");
		txtPolicyAgeing.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		fourthForm.addComponent(txtPolicyAgeing);

		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("200px");
		insuredName.setHeight("20px");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(insuredName);

		srCitizenClaim = new TextField("Sr Citizen Claim");
		//Vaadin8-setImmediate() srCitizenClaim.setImmediate(true);
		srCitizenClaim.setWidth("-1px");
		srCitizenClaim.setHeight("20px");
		srCitizenClaim.setReadOnly(true);
		srCitizenClaim.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(srCitizenClaim);

		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("350px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(productName);

		

		classOfBusiness = new TextField("Class Of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("250px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(classOfBusiness);

		netWorkHospitalType = new TextField("Network Hospital Type");
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
		netWorkHospitalType.setHeight("20px");
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(netWorkHospitalType);

		policyYear = new TextField("Policy Year");
		//Vaadin8-setImmediate() policyYear.setImmediate(true);
		policyYear.setWidth("250px");
		policyYear.setHeight("20px");
		policyYear.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(policyYear);

		insuredAge = new TextField("Insured Age");
		//Vaadin8-setImmediate() insuredAge.setImmediate(true);
		insuredAge.setWidth("250px");
		insuredAge.setHeight("20px");
		insuredAge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(insuredAge);
		
		productType = new TextField("Product Type");
		//Vaadin8-setImmediate() productType.setImmediate(true);
		productType.setWidth("250px");
		productType.setHeight("20px");
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(productType);
		
		policyPlan = new TextField("Plan");
		//Vaadin8-setImmediate() policyPlan.setImmediate(true);
		policyPlan.setWidth("300px");
		policyPlan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(policyPlan);

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
		layout1 = new HorizontalLayout(firstForm, secondForm);
//		layout1.setSpacing(true);
		layout2 = new HorizontalLayout(thirdForm,fourthForm);
//		layout2.setSpacing(true);
		layout3 = new HorizontalLayout(fifthForm,sixthForm);
		layout2.setSpacing(true);
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.addComponent(layout3);
		formsHorizontalLayout.setHeight("110px");
     
		return formsHorizontalLayout;
	}

	private void setAddtionalValuesforCarousel(NewIntimationDto newIntimationDTO) {

		/*
		 * if(("Network").equalsIgnoreCase(newIntimationDTO.getHospitalType().
		 * getValue())) { claimType.setReadOnly(false);
		 * claimType.setValue("Cashless"); } else {
		 * claimType.setReadOnly(false); claimType.setValue("Re-imbursement"); }
		 */

		if (null != newIntimationDTO.getCpuCode()) {
			cpuCode.setValue(newIntimationDTO.getCpuCode());
		}
		if (null != newIntimationDTO.getPolicy()) {
			String strCustId = String.valueOf(newIntimationDTO
					.getInsuredPatient().getInsuredId());
			customerId.setValue(strCustId);
			policyNumber.setValue(newIntimationDTO.getPolicy()
					.getPolicyNumber());
			insuredName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
//			insuredName.setValue(newIntimationDTO.getInsuredPatient()
//					.getInsuredName());
		}
		if (null != newIntimationDTO.getHospitalDto()) {
			String hosp = newIntimationDTO.getHospitalDto().getName() != null ? newIntimationDTO
					.getHospitalDto().getName() : "";
			hospitalName.setValue(StringUtils.trim(hosp));
			String City = newIntimationDTO.getHospitalDto().getCity();
			hospitalCity.setValue(City);
			hospitalIrdaCode.setValue(newIntimationDTO.getHospitalDto().getHospitalIrdaCode());
			hospitalIrdaCode.setNullRepresentation("");
		}
		if (null != newIntimationDTO.getHospitalType()) {
			hospitalTypeName.setValue(newIntimationDTO.getHospitalType()
					.getValue());
		}
		if (null != newIntimationDTO.getPolicy().getProduct()) {
			productName.setValue(newIntimationDTO.getPolicy().getProduct()
					.getValue());
		}
		if (null != newIntimationDTO.getPolicy().getProductType()) {
			productType.setValue(newIntimationDTO.getPolicy().getProductType()
					.getValue());
		}
	
		/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey())){*/
		if(newIntimationDTO.getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) || 
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
				 && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
			if(newIntimationDTO.getInsuredPatient().getPolicyPlan() != null){
				policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
			}else{
				policyPlan.setValue("-");
			}
		}
		else{
			if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
				policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
			}else{
				policyPlan.setValue("-");
			}

			if(newIntimationDTO.getPolicy().getProduct() != null 
					&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
							&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)))){
				policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_INDEMNITY);
			}else if(newIntimationDTO.getPolicy().getProduct() != null 
					&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
							&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)))){
				policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_LUMPSUM);
			}else{
				policyPlan.setValue("-");
			}
		}

		if(ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) && newIntimationDTO.getInsuredPatient().getPlan()!= null 
				&&  !newIntimationDTO.getInsuredPatient().getPlan().isEmpty()){
			if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_B)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN);
			}else if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_E)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN);
			}
	    }
		
		if(null != newIntimationDTO.getPolicy().getPolicyYear()){
			policyYear.setValue(newIntimationDTO.getPolicy().getPolicyYear().toString());
		}

//		if (newIntimationDTO.getPolicy().getPolicyNumber() != null
//				&& newIntimationDTO.getAdmissionDate() != null) {
//			String policyAgeing = dbCalculationService.getPolicyAgeing(
//					newIntimationDTO.getAdmissionDate(), newIntimationDTO
//							.getPolicy().getPolicyNumber());
//			txtPolicyAgeing.setValue(policyAgeing);
//		}
		
		if(newIntimationDTO.getPolicyYear() != null){
			txtPolicyAgeing.setValue(newIntimationDTO.getPolicyYear());
		}

		if (null != newIntimationDTO.getPolicy().getProductType()) {
			String classofBusiness = newIntimationDTO.getLineofBusiness() != null ? newIntimationDTO
					.getLineofBusiness() : "";
			classOfBusiness.setValue(classofBusiness);

		}

		// if(newIntimationDTO.getPolicy().getp)

		if (newIntimationDTO.getInsuredPatient() != null
				&& newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
			
			Integer insured =  newIntimationDTO.getInsuredPatient()
					.getInsuredAge().intValue();
//			String[] age = insuredAge.split(".");
			
			insuredAge.setValue(insured.toString()+" years");
		}

		if (null != newIntimationDTO.getHospitalDto()
				&& null != newIntimationDTO.getHospitalDto()
						.getRegistedHospitals()) {
			if (newIntimationDTO.getHospitalDto().getRegistedHospitals()
					.getNetworkHospitalType() != null) {
				String netWorkType = newIntimationDTO.getHospitalDto()
						.getRegistedHospitals().getNetworkHospitalType() != null ? newIntimationDTO
						.getHospitalDto().getRegistedHospitals()
						.getNetworkHospitalType()
						: "";
				netWorkHospitalType.setValue(netWorkType);
			} else {
				if (layout2 != null && fifthForm != null
						&& netWorkHospitalType != null) {
					fifthForm.removeComponent(netWorkHospitalType);
				}
			}

		}
		
		claimTypeField.setReadOnly(true);
		hospitalName.setReadOnly(true);
		customerId.setReadOnly(true);
		policyNumber.setReadOnly(true);
		intimationId.setReadOnly(true);
		insuredName.setReadOnly(true);
		hospitalTypeName.setReadOnly(true);
		productName.setReadOnly(true);
		productType.setReadOnly(true);
		policyType.setReadOnly(true);
		txtPolicyAgeing.setReadOnly(true);
		classOfBusiness.setReadOnly(true);
		netWorkHospitalType.setReadOnly(true);
		policyYear.setReadOnly(true);
		insuredAge.setReadOnly(true);
	}

}
