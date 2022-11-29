package com.shaic.claim;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.arch.SHAConstants;
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

public class IntimationDetailsCarousel extends ViewComponent {

	private static final long serialVersionUID = 5892717759896227659L;
	
	@EJB
	private DBCalculationService dbCalculationService;

	private TextField claimedAmtTxt;
	private TextField intimationMode;
	private TextField intimateedby;
	private TextField createdDate;
	private TextField intimDate;
	private TextField intimaterName;
	private TextField intimationId;
	
	private TextField nameOfCallAttendee;
	private TextField txtClaimType;
	private TextField srCitizenClaim;
	private TextField cpuCode;
	private TextField proposerName;
	
	private TextField insuredPatientName;
	private TextField customerId;
	private TextField hospitalName;
	private TextField hospitalCity;
	private TextField hospitalIrdaCode;
	private TextField hospitalTypeName;
	private TextField reasonForAdmission;
	
	private TextField policyNumber;
	private TextField insuredName;
	private TextField productName;
	private TextField TypeOfProduct;
	private TextField officeCode;
	private TextField txtPolicyAgeing;
	
	private TextField admissionDate; //admissionDate
	private TextField typeOfAdmission;
	private TextField networkHospitalType;
	
	private TextField policyPlan;
	
	private StringBuilder strPolicyBldr ;
	
	
	private TextField txtLossDateNTime;
	
	private TextField passportNoNExpDate;
	
	private TextField dischargeDt;
	
	private TextField txtPlaceOfLossDelay;
	
	private TextField txtSponsorName;
	
	private TextField txtTPAIntimationNo;
	
	private TextField insuredAge;
	
	private TextField insuredadmissionDate;
	
	private FormLayout firstForm;
	
	private FormLayout secondForm;
	
	private FormLayout thirdForm;
	
	private FormLayout fourthForm;
	
	private FormLayout fifthForm;



	
	@PostConstruct
	public void initView() {
		
	}
	
	public void init(NewIntimationDto intimationDto, String caption) {
		Panel panel = new Panel(caption);
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		//panel.setHeight("130px");
		panel.setContent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(intimationDto);
	    FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        buildCarosuelDetials(intimationDto);
        insuredPatientName.setReadOnly(false);
        insuredPatientName.setValue(intimationDto.getInsuredPatientName());
        insuredPatientName.setReadOnly(true);
        txtClaimType.setReadOnly(false);
        txtClaimType.setValue(StringUtils.equalsIgnoreCase("Network", intimationDto.getHospitalDto().getHospitalType().getValue()) ? "Cashless" : "Reimbursement");
        txtClaimType.setReadOnly(true);
        srCitizenClaim.setReadOnly(false);
        if(intimationDto.getInsuredAge() != null && intimationDto.getInsuredAge() != "") {
        	srCitizenClaim.setValue(Integer.valueOf(intimationDto.getInsuredAge()) > 60 ? "YES" : "NO");
        }
        srCitizenClaim.setReadOnly(true);
        
        setCompositionRoot(panel);
	}
	
	public void init(NewIntimationDto intimationDto, String caption,ClaimDto claimDTO) {
		Panel panel = new Panel(caption);
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		//panel.setHeight("130px");
		panel.setContent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(intimationDto);
	    FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        buildCarosuelDetials(intimationDto);
        insuredPatientName.setReadOnly(false);
        insuredPatientName.setValue(intimationDto.getInsuredPatientName());
        insuredPatientName.setReadOnly(true);
        txtClaimType.setReadOnly(false);
        if (claimDTO != null && claimDTO.getClaimType() != null){
        	txtClaimType.setValue(claimDTO.getClaimType().getValue());
        }
        txtClaimType.setReadOnly(true);
        srCitizenClaim.setReadOnly(false);
        if(intimationDto.getInsuredAge() != null && intimationDto.getInsuredAge() != "") {
        	srCitizenClaim.setValue(Integer.valueOf(intimationDto.getInsuredAge()) > 60 ? "YES" : "NO");
        }
        srCitizenClaim.setReadOnly(true);
        
        setCompositionRoot(panel);
	}
	
	public void init(NewIntimationDto intimationDto) {
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() CarouselVLayout.setImmediate(true);
		CarouselVLayout.setHeight("130px");
		CarouselVLayout.addComponent(buildCarousel());		
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(intimationDto);
	    FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        buildCarosuelDetials(intimationDto);
       
        insuredPatientName.setReadOnly(false);
        insuredPatientName.setValue(intimationDto.getInsuredPatientName());
        insuredPatientName.setReadOnly(true);
        txtClaimType.setReadOnly(false);
        txtClaimType.setValue(StringUtils.equalsIgnoreCase("Network", intimationDto.getHospitalType().getValue()) ? "Cashless" : "Reimbursement");
        txtClaimType.setReadOnly(true);

        
        srCitizenClaim.setReadOnly(false);
        if(intimationDto.getInsuredAge() != null && intimationDto.getInsuredAge() != "") {
        	srCitizenClaim.setValue(Integer.valueOf(intimationDto.getInsuredAge()) > 60 ? "YES" : "NO");
        }
        srCitizenClaim.setReadOnly(true);
//        HorizontalLayout viewHLayout = buildViewDetailsLayout();
//        CarouselVLayout.addComponent(viewHLayout);
//        CarouselVLayout.setComponentAlignment(viewHLayout, Alignment.MIDDLE_RIGHT);
        setCompositionRoot(CarouselVLayout);
	}
	

//	public void init(NewIntimationDto intimationDto,String caption) {
//		VerticalLayout CarouselVLayout = new VerticalLayout();
//		CarouselVLayout.setCaption(caption);
//		CarouselVLayout.setSizeFull();
//		CarouselVLayout.addComponent(buildCarousel());
//		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(intimationDto);
//	    FieldGroup binder = new FieldGroup(item);
//        binder.bindMemberFields(this);
//        
//        buildCarosuelDetials(intimationDto);
//        
//        insuredPatientName.setReadOnly(false);
//        insuredPatientName.setValue(intimationDto.getInsuredPatientName());
//        insuredPatientName.setReadOnly(true);
//        claimType.setReadOnly(false);
//        claimType.setValue(StringUtils.equalsIgnoreCase("Network", intimationDto.getHospitalType().getValue()) ? "Cashless" : "Reimbursement");
//        claimType.setReadOnly(true);
//        srCitizenClaim.setReadOnly(false);
//        if(intimationDto.getInsuredAge() != null && intimationDto.getInsuredAge() != "") {
//        	srCitizenClaim.setValue(Integer.valueOf(intimationDto.getInsuredAge()) > 60 ? "YES" : "NO");
//        }
//        srCitizenClaim.setReadOnly(true);
////        HorizontalLayout viewHLayout = buildViewDetailsLayout();
////        CarouselVLayout.addComponent(viewHLayout);
////        CarouselVLayout.setComponentAlignment(viewHLayout, Alignment.MIDDLE_RIGHT);
//        setCompositionRoot(CarouselVLayout);
//	}

	
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
		FormLayout sixthForm = new FormLayout();
		sixthForm.setSpacing(false);
		sixthForm.setMargin(false);
		FormLayout seventhForm = new FormLayout();
		seventhForm.setSpacing(false);
		seventhForm.setMargin(false);
		
		
		intimationId = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationId.setImmediate(true);
		intimationId.setWidth("-1px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimationId);
		
		intimaterName = new TextField("Intimator Name");
		//Vaadin8-setImmediate() intimaterName.setImmediate(true);
		intimaterName.setWidth("160px");
		intimaterName.setReadOnly(true);
		intimaterName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimaterName);
		
		createdDate = new TextField("Intimation Date");
		//Vaadin8-setImmediate() createdDate.setImmediate(true);
		createdDate.setWidth("-1px");
		createdDate.setReadOnly(true);
		createdDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(createdDate);
		
		intimateedby = new TextField("Intimated By");
		//Vaadin8-setImmediate() intimateedby.setImmediate(true);
		intimateedby.setWidth("-1px");
	//	intimateByyy.setReadOnly(true);
		intimateedby.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimateedby);
		
		
		intimationMode = new TextField("Mode Of Intimation");
		//Vaadin8-setImmediate() intimationMode.setImmediate(true);
		intimationMode.setWidth("-1px");
		intimationMode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(intimationMode);
		
		// Second Column..........
		
		nameOfCallAttendee = new TextField("Name of Call Attendee");
		//Vaadin8-setImmediate() nameOfCallAttendee.setImmediate(true);
		nameOfCallAttendee.setWidth("-1px");
		nameOfCallAttendee.setReadOnly(true);
		nameOfCallAttendee.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(nameOfCallAttendee);
		
		txtClaimType = new TextField("Claim Type");
		
		//Vaadin8-setImmediate() txtClaimType.setImmediate(true);
		txtClaimType.setWidth("-1px");
		txtClaimType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(txtClaimType);
		srCitizenClaim = new TextField("Sr Citizen Claim");
		//Vaadin8-setImmediate() srCitizenClaim.setImmediate(true);
		srCitizenClaim.setWidth("-1px");
		srCitizenClaim.setReadOnly(true);
		srCitizenClaim.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(srCitizenClaim);
		
		cpuCode = new TextField("CPU Code");
		//Vaadin8-setImmediate() cpuCode.setImmediate(true);
		cpuCode.setWidth("-1px");
		cpuCode.setReadOnly(true);
		cpuCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(cpuCode);
		
		proposerName = new TextField("Proposer Name");
		
		//Vaadin8-setImmediate() proposerName.setImmediate(true);
		proposerName.setWidth("300px");
		proposerName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(proposerName);
		
		// Third Column..........
		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(insuredPatientName);
		
		customerId = new TextField("Customer Id");
		
		//Vaadin8-setImmediate() customerId.setImmediate(true);
		customerId.setWidth("-1px");
		customerId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(customerId);
		
		hospitalName = new TextField("Hospital Name");

		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setWidth("180px");
		hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(hospitalName);
		
		hospitalCity = new TextField("Hospital City");
		//Vaadin8-setImmediate() hospitalCity.setImmediate(true);
		hospitalCity.setWidth("180px");
		hospitalCity.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(hospitalCity);
		
		hospitalIrdaCode = new TextField("Hospital IRDA Code");
		//Vaadin8-setImmediate() hospitalIrdaCode.setImmediate(true);
		hospitalIrdaCode.setWidth("180px");
		hospitalIrdaCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(hospitalIrdaCode);
		
		hospitalTypeName = new TextField("Hospital Type");
		//Vaadin8-setImmediate() hospitalTypeName.setImmediate(true);
		hospitalTypeName.setWidth("-1px");
		hospitalTypeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(hospitalTypeName);
		
		reasonForAdmission = new TextField("Reason For Admission");
		//Vaadin8-setImmediate() reasonForAdmission.setImmediate(true);
		reasonForAdmission.setWidth("180px");
		reasonForAdmission.setReadOnly(true);
		reasonForAdmission.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(reasonForAdmission);
		
		
		// Foruth Column
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(policyNumber);
		
		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("300px");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(insuredName);
		

		
		
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("300px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fifthForm.addComponent(productName);
		
		TypeOfProduct = new TextField("Product Type");
		//Vaadin8-setImmediate() TypeOfProduct.setImmediate(true);
		TypeOfProduct.setWidth("300px");
		TypeOfProduct.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(TypeOfProduct);
		
		officeCode = new TextField("Policy Iss Office");
		//Vaadin8-setImmediate() officeCode.setImmediate(true);
		officeCode.setWidth("300px");
		officeCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(officeCode);
		
		txtPolicyAgeing = new TextField("Policy Ageing");
		//Vaadin8-setImmediate() txtPolicyAgeing.setImmediate(true);
		txtPolicyAgeing.setWidth("300px");
		txtPolicyAgeing.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		sixthForm.addComponent(txtPolicyAgeing);
		
		admissionDate = new TextField("Admission Date");
		//Vaadin8-setImmediate() admissionDate.setImmediate(true);
		admissionDate.setWidth("300px");
		admissionDate.setReadOnly(true);
		admissionDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		sixthForm.addComponent(admissionDate);
		
		typeOfAdmission = new TextField("Admission Type");
		//Vaadin8-setImmediate() typeOfAdmission.setImmediate(true);
		typeOfAdmission.setWidth("300px");
		typeOfAdmission.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		seventhForm.addComponent(typeOfAdmission);
		
		
		networkHospitalType = new TextField("Network Hosp Type");
		//Vaadin8-setImmediate() networkHospitalType.setImmediate(true);
		networkHospitalType.setWidth("300px");
		networkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		seventhForm.addComponent(networkHospitalType);
		
		policyPlan = new TextField("Plan");
		//Vaadin8-setImmediate() policyPlan.setImmediate(true);
		policyPlan.setWidth("300px");
		policyPlan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		seventhForm.addComponent(policyPlan);

        
		
		
		HorizontalCarousel formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");
//		formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);

		// Only react to arrow keys when focused
		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);
		
		
		HorizontalLayout layout1 = new HorizontalLayout(firstForm, secondForm);
		
		HorizontalLayout layout2 = new HorizontalLayout(thirdForm, fourthForm);
//		layout1.setSpacing(true);
		HorizontalLayout layout3 = new HorizontalLayout(fifthForm, sixthForm);
		
		HorizontalLayout layout4 = new HorizontalLayout(seventhForm);
//		layout2.setSpacing(true);
		
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.addComponent(layout3);
		formsHorizontalLayout.addComponent(layout4);
		formsHorizontalLayout.setHeight("117px");
		return formsHorizontalLayout;
	}
	
//	private HorizontalLayout buildViewDetailsLayout() {
//		FormLayout viewDetailsForm = new FormLayout();
//		//Vaadin8-setImmediate() viewDetailsForm.setImmediate(true);
//		viewDetailsForm.setWidth("-1px");
//		viewDetailsForm.setHeight("-1px");
//		viewDetailsForm.setMargin(false);
//		viewDetailsForm.setSpacing(true);
//		ComboBox viewDetailsSelect = getViewDetailsNativeSelect();
//		viewDetailsForm.addComponent(viewDetailsSelect);
//		Button goButton = new Button("Go");
////		Button goButton = getGoButton(viewDetailsSelect);
//		HorizontalLayout horizontalLayout1 = new HorizontalLayout(
//				viewDetailsForm, goButton);
////		horizontalLayout1.setSizeUndefined();
//		//Vaadin8-setImmediate() horizontalLayout1.setImmediate(true);
//		horizontalLayout1.setSpacing(true);
//
//		return horizontalLayout1;
//	}

//	private ComboBox getViewDetailsNativeSelect() {
//		ComboBox viewDetailsSelect = new ComboBox("View Details");
//		//Vaadin8-setImmediate() viewDetailsSelect.setImmediate(true);
//		viewDetailsSelect.setWidth("164px");
//		viewDetailsSelect.addItem(VIEW_POLICY);
//		viewDetailsSelect.addItem(VIEW_DOCUMENTS);
//		viewDetailsSelect.addItem(VIEW_CLAIM_HISTORY);
//		viewDetailsSelect.addItem(VIEW_CLAIM_STATUS);
//		viewDetailsSelect.addItem(VIEW_PRODUCT_BENEFITS);
//		viewDetailsSelect.addItem(VIEW_CO_PAY_DETAILS);
//		viewDetailsSelect.addItem(VIEW_MER_DETAILS);
//		viewDetailsSelect.addItem(VIEW_INTIMATION);
//		viewDetailsSelect.addItem(VIEW_HOSPITAL_DETAILS);
//		viewDetailsSelect.addItem(VIEW_PREVIOUS_CLAIM_DETAILS);
//		viewDetailsSelect.addItem(VIEW_BALANCE_SUM_INSURED);
//		return viewDetailsSelect;
//	}
	
	/**
	 * This method populates the text field which would be present
	 * in the register claim details page.
	 * In DTO , the values are populated into object.
	 * When populating an object to a text feild, vaadin throws converter
	 * exception. To overcome the same, the string values or long values
	 * are obtained from the specific object and then set to the 
	 * text fields.
	 * 
	 * */
	private void buildCarosuelDetials(NewIntimationDto newIntimationDTO)
	{
		 if(null != newIntimationDTO.getModeOfIntimation())
		 {
			intimationMode.setValue(newIntimationDTO.getModeOfIntimation().getValue());
		 }
		
		if( null != newIntimationDTO.getIntimatedBy() )
		{
			intimateedby.setValue(newIntimationDTO.getIntimatedBy().toString());
		}
		
//		if(("Network").equalsIgnoreCase(newIntimationDTO.getHospitalType().getValue()))
//		{
//			txtClaimType.setValue("Cashless");
//		}
//		else 
//		{
//			txtClaimType.setValue("Reimbursement");
//		}
		if(newIntimationDTO.getClaimType() != null){
		
			txtClaimType.setValue(newIntimationDTO.getClaimType().getValue());
		}
		
		if(null != newIntimationDTO.getPolicy())
		{
			proposerName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
		}
		
		if(null != newIntimationDTO.getCpuCode()){
			cpuCode.setValue(newIntimationDTO.getCpuCode());
		}
		if(null != newIntimationDTO.getPolicy())
		{	
			String strCustId = String.valueOf(newIntimationDTO.getInsuredPatient().getInsuredId());
			customerId.setValue(strCustId);
			policyNumber.setValue(newIntimationDTO.getPolicy().getPolicyNumber());
			insuredName.setValue(newIntimationDTO.getInsuredPatient().getInsuredName());
			admissionDate.setValue(newIntimationDTO.getAdmissionDate() != null ? new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getAdmissionDate()): "");
	
			/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey())) {*/
			
			if(newIntimationDTO.getPolicy().getProduct() != null 
					&& (SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())  ||
							SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
							|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) 
							|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))) {
				if(newIntimationDTO.getInsuredPatient().getPolicyPlan() != null){
					policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
				}else{
					policyPlan.setValue("-");
				}
			}else if(SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
					SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())){
				if(newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)){
					if(newIntimationDTO.getInsuredPatient().getPolicyPlan() != null){						
						policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
					}
				}else{
					if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
						policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
					}
				}
			}else{
				if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
					policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
				}else{
					policyPlan.setValue("-");
				}
				if(newIntimationDTO.getPolicy().getProduct() != null 
						&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
								|| ReferenceTable.STAR_GRP_COVID_PROD_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
								&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY))){
					policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_INDEMNITY);
				}else if(newIntimationDTO.getPolicy().getProduct() != null 
						&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
								|| ReferenceTable.STAR_GRP_COVID_PROD_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
								&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))){
					policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_LUMPSUM);
				}else{
					policyPlan.setValue("-");
				}
			}
			
			if(ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) && newIntimationDTO.getInsuredPatient().getPlan() != null
				 &&	!newIntimationDTO.getInsuredPatient().getPlan().isEmpty()){
				if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_B)){
					policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN);
				}else if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_E)){
					policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN);
				}
		    }
			             //need to implements
		
			
			/**
			 * StringBuilder object is used to concatenate two string objects.
			 * This is primarily done, to avoid creation of another string object when two 
			 * strings are concatenated , as String objects are immutable. 
			 * Whereas , StringBuilder are mutable. Hence when we append two or more 
			 * strings to the same object, in heap there would be only one string builder object created. 
			 * 
			 * */
			strPolicyBldr = new StringBuilder(); 
			if(newIntimationDTO.getOrganizationUnit() != null){
				strPolicyBldr.append(newIntimationDTO.getOrganizationUnit().getOrganizationUnitName()).append("-").append(newIntimationDTO.getPolicy().getHomeOfficeCode()); 
				officeCode.setValue(strPolicyBldr.toString());
			}
		}
		
		if(null != newIntimationDTO.getHospitalDto())
		{
			String hosp = newIntimationDTO.getHospitalDto().getName() != null ? newIntimationDTO.getHospitalDto().getName() : "";
			hospitalName.setValue(StringUtils.trim(hosp));
			String hospCity = newIntimationDTO.getHospitalDto().getCity();
			hospitalCity.setValue(hospCity);
			hospitalIrdaCode.setValue(newIntimationDTO.getHospitalDto().getHospitalIrdaCode());
		}
		
		if( null != newIntimationDTO.getHospitalType())
		{
			hospitalTypeName.setValue(newIntimationDTO.getHospitalType().getValue());
		}
		if(null != newIntimationDTO.getPolicy().getProduct())
		{
			productName.setValue(newIntimationDTO.getPolicy().getProduct().getValue());
		}
		if(null != newIntimationDTO.getPolicy().getProductType())
		{
			TypeOfProduct.setValue(newIntimationDTO.getPolicy().getProductType().getValue());
		}
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getRegistedHospitals())
		{
			networkHospitalType.setValue(newIntimationDTO.getHospitalDto().getRegistedHospitals().getNetworkHospitalType() != null ? newIntimationDTO.getHospitalDto().getRegistedHospitals().getNetworkHospitalType(): "-"); 
		}
		if(null != newIntimationDTO.getCreatedBy())
		{
			nameOfCallAttendee.setReadOnly(false);
			nameOfCallAttendee.setValue(newIntimationDTO.getCreatedBy());
			nameOfCallAttendee.setReadOnly(true);
		}
		if(null != newIntimationDTO.getAdmissionType())
		{
			
			typeOfAdmission.setValue(newIntimationDTO.getAdmissionType().toString()); 
		}
		
//		if(newIntimationDTO.getPolicy().getPolicyNumber() != null && newIntimationDTO.getAdmissionDate() != null){
//			String policyAgeing = dbCalculationService.getPolicyAgeing(newIntimationDTO.getAdmissionDate(), newIntimationDTO.getPolicy().getPolicyNumber());
//			txtPolicyAgeing.setValue(policyAgeing);
//		}
		
		if(newIntimationDTO.getPolicyYear() != null){
			txtPolicyAgeing.setValue(newIntimationDTO.getPolicyYear());
		}
		
		intimationMode.setReadOnly(true);
		intimateedby.setReadOnly(true);
		txtClaimType.setReadOnly(true);
		hospitalName.setReadOnly(true);
		customerId.setReadOnly(true);
		policyNumber.setReadOnly(true);
		insuredName.setReadOnly(true);
		officeCode.setReadOnly(true);
		hospitalTypeName.setReadOnly(true);
		productName.setReadOnly(true);
		TypeOfProduct.setReadOnly(true);
		networkHospitalType.setReadOnly(true);
		typeOfAdmission.setReadOnly(true);
		txtPolicyAgeing.setReadOnly(true);
		intimationId.setReadOnly(true);
	}
	
	
	

//	private Button getGoButton(final ComboBox viewDetailsSelect) {
//		Button goButton = new Button();
//		goButton.setCaption("Go");
//		//Vaadin8-setImmediate() goButton.setImmediate(true);
//		goButton.addClickListener(new Button.ClickListener() {
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				if (viewDetailsSelect.getValue() != null) {
////					RegistrationBean registrationBean = (RegistrationBean) VaadinSession
////							.getCurrent().getAttribute("registrationBean");
//                    ClaimDto claimDto = new ClaimDto(); 
//					
//					switch (viewDetailsSelect.getValue().toString()) {
//					case VIEW_POLICY:
//						final Intimation intimation = new NewIntimationMapper().getNewIntimation(claimDto.getNewIntimationDto());
//						TmpPolicy tmpPolicy = claimDto.getNewIntimationDto().getTmpPolicy();
////						TmpPolicy tmpPolicy = policyService.getPolicy(intimation.getPolicy().getPolicyNumber());
////						ViewPolicyDetails viewPolicyDetail= new ViewPolicyDetails(policyService, tmpPolicy, masterService);
////						viewPolicyDetail.initView();
////						UI.getCurrent().addWindow(viewPolicyDetail);
//						break;
//					case VIEW_DOCUMENTS:
//						break;
//					case VIEW_CLAIM_HISTORY:
//						break;
//					case VIEW_CLAIM_STATUS:
//						getViewClaimStatus(claimDto);
//						break;
//					case VIEW_PRODUCT_BENEFITS:
//						getViewProductBenefits(registrationBean);
//						break;
//					case VIEW_CO_PAY_DETAILS:
//						break;
//					case VIEW_MER_DETAILS:
//						break;
//					case VIEW_INTIMATION:
//						getViewIntimation(registrationBean);
//						break;
//					case VIEW_HOSPITAL_DETAILS:
//						getViewHospitalDetails(registrationBean);
//						break;
//					case VIEW_PREVIOUS_CLAIM_DETAILS:
//						getViewPreviousClaimDetails(registrationBean);
//						break;
//					case VIEW_BALANCE_SUM_INSURED:
//						getViewBalanceSumInsured(registrationBean);
//						break;
//					}
//					
//				} else {
//					//TODO Error message
//				}
//
//			}
//
//		});
//		return goButton;
//	}
//	
//	private void getViewClaimStatus(ClaimDto claimDto) {
//		final Intimation intimation = intimationService
//				.searchbyIntimationNo(claimDto.getNewIntimationDto().getIntimationId());
//
//		Hospitals hospital = policyService.getVWHospitalByKey(intimation
//				.getHospital());
//
//		IntimationsDto a_intimationDto = DtoConverter
//				.intimationToIntimationDTO(intimation, hospital);
//
//		Claim a_claim = claimService.getClaimforIntimation(intimation.getKey());
//
//		if (a_claim != null) {
//			DtoConverter converter = new DtoConverter();
//			ClaimDtoOld a_claimDto = converter.claimToClaimDTO(a_claim,
//					hospital);
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					a_claimDto, a_intimationDto, intimation.getPolicy()
//							.getStatus() == null);
//			UI.getCurrent().addWindow(intimationStatus);
//		} else {
//			ViewIntimationStatus intimationStatus = new ViewIntimationStatus(
//					a_intimationDto, intimation.getPolicy().getStatus() == null);
//			UI.getCurrent().addWindow(intimationStatus);
//		}
//	}
//
//	private void getViewProductBenefits(RegistrationBean registrationBean) {
//		ViewProductBenefits a_viewProductBenefits = viewProductBenefitInstance
//				.get();
//		a_viewProductBenefits.showValues(registrationBean.getIntimationNo());
//		UI.getCurrent().addWindow(a_viewProductBenefits);
//	}
//
//	private void getViewBalanceSumInsured(RegistrationBean registrationBean) {
//		ViewBalenceSumInsured a_viewBalenceSumInsured = viewBalenceSumInsured
//				.get();
//		a_viewBalenceSumInsured.bindFieldGroup(registrationBean
//				.getIntimationNo());
//		UI.getCurrent().addWindow(a_viewBalenceSumInsured);
//	}
//
//	private void getViewPreviousClaimDetails(RegistrationBean registrationBean) {
//		ViewPreviousClaims a_viewPreviousClaims = viewPreviousClaims.get();
//		a_viewPreviousClaims.showValues(registrationBean.getIntimationNo());
//		UI.getCurrent().addWindow(a_viewPreviousClaims);
//	}
//
//	private void getViewHospitalDetails(RegistrationBean registrationBean) {
//		ViewHospitalDetails a_viewHospitalDetails = viewHospitalDetails.get();
//		a_viewHospitalDetails.showValues(registrationBean.getIntimationNo());
//		UI.getCurrent().addWindow(a_viewHospitalDetails);
//	}
//
//	private void getViewIntimation(RegistrationBean registrationBean) {
//		Intimation intimation = intimationService
//				.searchbyIntimationNo(registrationBean.getIntimationNo());
//		Hospitals hospital = policyService.getVWHospitalByKey(intimation
//				.getHospital());
//		IntimationsDto intimationToIntimationDetailsDTO = DtoConverter
//				.intimationToIntimationDTO(intimation, hospital);
//		ViewIntimation intimationDetails = new ViewIntimation(
//				intimationToIntimationDetailsDTO);
//		UI.getCurrent().addWindow(intimationDetails);
//	}
	
	
	public void initOMPCarousal(NewIntimationDto intimationDto, String caption) {
		Panel panel = new Panel(caption);
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setHeight("-1px");
		//panel.setHeight("130px");
		panel.setContent(buildOMPCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(intimationDto);
	    FieldGroup binder = new FieldGroup(item);
        binder.bindMemberFields(this);
        buildOMPCarosuelDetials(intimationDto);
        insuredPatientName.setReadOnly(false);
        insuredPatientName.setValue(intimationDto.getInsuredPatientName());
        insuredPatientName.setReadOnly(true);

        setCompositionRoot(panel);
	}
	
	private void buildOMPCarosuelDetials(NewIntimationDto newIntimationDTO)
	{
		 if(null != newIntimationDTO.getModeOfIntimation())
		 {
			intimationMode.setValue(newIntimationDTO.getModeOfIntimation().getValue());
		 }else{
			 intimationMode.setValue("-");
		 }
		
		if( null != newIntimationDTO.getIntimatedBy() )
		{
			intimateedby.setValue(newIntimationDTO.getIntimatedBy().toString());
		}else{
			intimateedby.setValue("-");
		}
		
		if(null != newIntimationDTO.getIntimationDate()){
			intimDate.setReadOnly(false);
			intimDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getIntimationDate()));
		}
		
		if(null != newIntimationDTO.getPolicy())
		{
			proposerName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
		}
		
		if(null != newIntimationDTO.getPolicy())
		{	
			policyNumber.setReadOnly(false);
			policyNumber.setValue(newIntimationDTO.getPolicy().getPolicyNumber());
		}

		insuredPatientName.setReadOnly(false);
		if(newIntimationDTO.getInsuredPatientName() != null){
			insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		}else{
			insuredPatientName.setValue("-");
		}
			
		policyPlan.setReadOnly(false);
		if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getPlan() != null){
			policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPlan());
		}else{
			policyPlan.setValue("-");
		}
				
		insuredadmissionDate.setReadOnly(false);
		if(newIntimationDTO.getAdmissionDate() != null){
			insuredadmissionDate.setValue(new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getAdmissionDate()));
			}else{
				insuredadmissionDate.setValue(null);
			}
		insuredadmissionDate.setReadOnly(true);
		if(null != newIntimationDTO.getPolicy().getProduct())
		{
			productName.setReadOnly(false);
			productName.setValue(newIntimationDTO.getPolicy().getProduct().getValue());
		}
		
		if(null != newIntimationDTO.getCreatedBy())
		{
			nameOfCallAttendee.setReadOnly(false);
			nameOfCallAttendee.setValue(newIntimationDTO.getCreatedBy());
			nameOfCallAttendee.setReadOnly(true);
		}
		
		if(null != newIntimationDTO.getLossDateTime())
		{
			txtLossDateNTime.setReadOnly(false);
			txtLossDateNTime.setValue(new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getLossDateTime()));
			txtLossDateNTime.setReadOnly(true);
		}	
		String passportNoExp = "";
		if(newIntimationDTO.getInsuredPatient() != null && null != newIntimationDTO.getInsuredPatient().getPassportNo())
		{
			passportNoExp = newIntimationDTO.getInsuredPatient().getPassportNo();
		}
		if(newIntimationDTO.getInsuredPatient() != null && null != newIntimationDTO.getInsuredPatient().getPassPortExpiryDate())
		{
			passportNoExp = !passportNoExp.isEmpty() ? passportNoExp + " - " + new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getInsuredPatient().getPassPortExpiryDate()):new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getInsuredPatient().getPassPortExpiryDate());
		}
		
		passportNoNExpDate.setReadOnly(false);
		passportNoNExpDate.setValue(passportNoExp);
		passportNoNExpDate.setReadOnly(true);
		
		dischargeDt.setReadOnly(false);	
		dischargeDt.setValue(null != newIntimationDTO.getDischargeDate() ? new SimpleDateFormat("dd-MM-yyyy").format(newIntimationDTO.getDischargeDate()): "");
		dischargeDt.setReadOnly(true);
		
		if(null != newIntimationDTO.getPlaceLossDelay()){
			txtPlaceOfLossDelay.setReadOnly(false);
			txtPlaceOfLossDelay.setValue(newIntimationDTO.getPlaceLossDelay());
			txtPlaceOfLossDelay.setReadOnly(true);
		}
		
		if(null != newIntimationDTO.getSponsorName()){
			txtSponsorName.setReadOnly(false);
			txtSponsorName.setValue(newIntimationDTO.getSponsorName());
			txtSponsorName.setReadOnly(true);
		}
		
		if(null != newIntimationDTO.getIntimaterName()){
			intimaterName.setReadOnly(false);
			intimaterName.setValue(newIntimationDTO.getIntimaterName());
			intimaterName.setReadOnly(true);
		}
			
		insuredAge.setReadOnly(false);
		if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge()!= null){
			long idAge = newIntimationDTO.getInsuredPatient().getInsuredAge().longValue();
		insuredAge.setValue(String.valueOf(idAge));
		}
		insuredAge.setReadOnly(true);
		
		intimationMode.setReadOnly(true);
		intimateedby.setReadOnly(true);
		policyNumber.setReadOnly(true);
		policyPlan.setReadOnly(true);
		insuredPatientName.setReadOnly(true);
		productName.setReadOnly(true);
		intimationId.setReadOnly(true);
		intimDate.setReadOnly(true);
	}


	private HorizontalCarousel buildOMPCarousel() {
		 firstForm = new FormLayout();
		firstForm.setSpacing(false);
		firstForm.setMargin(false);
		secondForm = new FormLayout();
		secondForm.setSpacing(false);
		secondForm.setMargin(false);
		
		thirdForm = new FormLayout();
		thirdForm.setSpacing(false);
		thirdForm.setMargin(false);
		fourthForm = new FormLayout();
		fourthForm.setSpacing(false);
		fourthForm.setMargin(false);
		fifthForm = new FormLayout();
		fifthForm.setSpacing(false);
		fifthForm.setMargin(false);
		FormLayout sixthForm = new FormLayout();
		sixthForm.setSpacing(false);
		sixthForm.setMargin(false);
		FormLayout seventhForm = new FormLayout();
		seventhForm.setSpacing(false);
		seventhForm.setMargin(false);
		
		
		intimationId = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationId.setImmediate(true);
		intimationId.setWidth("-1px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimationId);
		
		intimaterName = new TextField("Intimator Name");
		//Vaadin8-setImmediate() intimaterName.setImmediate(true);
		intimaterName.setWidth("160px");
		intimaterName.setReadOnly(true);
		intimaterName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimaterName);
		
		intimDate = new TextField("Intimation Date");
		//Vaadin8-setImmediate() intimDate.setImmediate(true);
		intimDate.setWidth("-1px");
		intimDate.setReadOnly(true);
		intimDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimDate);
		
		intimateedby = new TextField("Intimated By");
		//Vaadin8-setImmediate() intimateedby.setImmediate(true);
		intimateedby.setWidth("-1px");
	//	intimateByyy.setReadOnly(true);
		intimateedby.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(intimateedby);
		
		
		intimationMode = new TextField("Mode Of Intimation");
		//Vaadin8-setImmediate() intimationMode.setImmediate(true);
		intimationMode.setWidth("-1px");
		intimationMode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(intimationMode);
		
		// Second Column..........
		
		nameOfCallAttendee = new TextField("Name of Call Attendee");
		//Vaadin8-setImmediate() nameOfCallAttendee.setImmediate(true);
		nameOfCallAttendee.setWidth("-1px");
		nameOfCallAttendee.setReadOnly(true);
		nameOfCallAttendee.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(nameOfCallAttendee);
		
		txtLossDateNTime = new TextField("Loss Date & Time");
		//Vaadin8-setImmediate() txtLossDateNTime.setImmediate(true);
		txtLossDateNTime.setWidth("-1px");
		txtLossDateNTime.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(txtLossDateNTime);
		
		insuredadmissionDate = new TextField("Admission Date");
		//Vaadin8-setImmediate() insuredadmissionDate.setImmediate(true);
		insuredadmissionDate.setWidth("300px");
		insuredadmissionDate.setReadOnly(true);
		insuredadmissionDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		insuredadmissionDate.setNullRepresentation("");
		thirdForm.addComponent(insuredadmissionDate);
		
		
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(policyNumber);
		
		proposerName = new TextField("Proposer Name");
		//Vaadin8-setImmediate() proposerName.setImmediate(true);
		proposerName.setWidth("300px");
		proposerName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(proposerName);
		
		// Third Column..........
		insuredPatientName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(insuredPatientName);
				
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("300px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(productName);
		
		policyPlan = new TextField("Plan");
		//Vaadin8-setImmediate() policyPlan.setImmediate(true);
		policyPlan.setWidth("300px");
		policyPlan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(policyPlan);
		
		passportNoNExpDate = new TextField("Passport No. & Date of Expiry");
		//Vaadin8-setImmediate() passportNoNExpDate.setImmediate(true);
		passportNoNExpDate.setWidth("300px");
		passportNoNExpDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(passportNoNExpDate);
		
		dischargeDt = new TextField("Date of Discharge");
		//Vaadin8-setImmediate() dischargeDt.setImmediate(true);
		dischargeDt.setWidth("300px");
		dischargeDt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		fourthForm.addComponent(dischargeDt);
		
		txtPlaceOfLossDelay = new TextField("Place of Loss/Delay");
		//Vaadin8-setImmediate() txtPlaceOfLossDelay.setImmediate(true);
		txtPlaceOfLossDelay.setWidth("300px");
		txtPlaceOfLossDelay.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtPlaceOfLossDelay.setNullRepresentation("");
		fifthForm.addComponent(txtPlaceOfLossDelay);
		
		txtSponsorName = new TextField("Sponsor Name");
		//Vaadin8-setImmediate() txtSponsorName.setImmediate(true);
		txtSponsorName.setWidth("300px");
		txtSponsorName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtSponsorName.setNullRepresentation("");
		fifthForm.addComponent(txtSponsorName);
		
		insuredAge = new TextField("Insured Age");
		//Vaadin8-setImmediate() insuredAge.setImmediate(true);
		insuredAge.setWidth("300px");
		insuredAge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		insuredAge.setNullRepresentation("");
		fifthForm.addComponent(insuredAge);
		
		HorizontalCarousel formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);

		// Only react to arrow keys when focused
		formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);
		
		
		HorizontalLayout layout1 = new HorizontalLayout(firstForm, secondForm);
		
		HorizontalLayout layout2 = new HorizontalLayout(thirdForm, fourthForm);

		HorizontalLayout layout3 = new HorizontalLayout(fifthForm);
		
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.addComponent(layout3);
		formsHorizontalLayout.setHeight("117px");
		return formsHorizontalLayout;
	}

	
	public void addOmpTpaIntimation(NewIntimationDto newIntimationDTO)
	{
				
		txtTPAIntimationNo = new TextField("TPA Intimation No");
		//Vaadin8-setImmediate() txtTPAIntimationNo.setImmediate(true);
		txtTPAIntimationNo.setWidth("300px");
		txtTPAIntimationNo.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtTPAIntimationNo.setNullRepresentation("");
		secondForm.addComponent(txtTPAIntimationNo);
		
		if(null != newIntimationDTO.getTpaIntimationNumber())
		{
			txtTPAIntimationNo.setReadOnly(false);
			txtTPAIntimationNo.setValue(newIntimationDTO.getTpaIntimationNumber());
			txtTPAIntimationNo.setReadOnly(true);
		}
		
	}

	
}
