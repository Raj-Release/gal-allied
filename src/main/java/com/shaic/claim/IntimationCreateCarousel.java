package com.shaic.claim;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.domain.TmpEmployee;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.reimbursement.service.PASearchProcessClaimRequestService;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class IntimationCreateCarousel extends ViewComponent {

	private static final long serialVersionUID = 5892717759896227659L;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PASearchProcessClaimRequestService claimrequestService;

	private TextField policyNumber; 
	//private TextField insuredName; 
	private TextField productName;  
	private TextField TypeOfProduct; 
	private TextField officeCode; 
	private TextField callerAttendeeName;
	private TextField portabilityPolicy ;

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

		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("220px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(policyNumber);
		
		TypeOfProduct = new TextField("Product Type");
		//Vaadin8-setImmediate() TypeOfProduct.setImmediate(true);
		TypeOfProduct.setWidth("300px");
		TypeOfProduct.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		firstForm.addComponent(TypeOfProduct);
		
//		insuredName = new TextField("Insured Name");
//		//Vaadin8-setImmediate() insuredName.setImmediate(true);
//		insuredName.setWidth("300px");
//		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		secondForm.addComponent(insuredName);

		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("300px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(productName);
		
		officeCode = new TextField("PIO Name");
		//Vaadin8-setImmediate() officeCode.setImmediate(true);
		officeCode.setWidth("300px");
		officeCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		secondForm.addComponent(officeCode);
		
		callerAttendeeName = new TextField("Call Attendee Name");
		//Vaadin8-setImmediate() callerAttendeeName.setImmediate(true);
		callerAttendeeName.setWidth("300px");
		callerAttendeeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		thirdForm.addComponent(callerAttendeeName);
		callerAttendeeName.setNullRepresentation("-");
		
		portabilityPolicy = new TextField("Portability Policy");
		//Vaadin8-setImmediate() portabilityPolicy.setImmediate(true);
		portabilityPolicy.setWidth("300px");
		thirdForm.addComponent(portabilityPolicy);
		portabilityPolicy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		HorizontalCarousel formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setStyleName("policygridinfo");
//		formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		//formsHorizontalLayout.setMouseDragEnabled(false);
		//formsHorizontalLayout.setMouseWheelEnabled(false);
		//formsHorizontalLayout.setTouchDragEnabled(false);

		// Only react to arrow keys when focused
		//formsHorizontalLayout.setArrowKeysMode(ArrowKeysMode.FOCUS);
		// Fetch children lazily
		//formsHorizontalLayout.setLoadMode(CarouselLoadMode.LAZY);
		// Transition animations between the children run 500 milliseconds
		formsHorizontalLayout.setTransitionDuration(50);
		
		
		HorizontalLayout layout1 = new HorizontalLayout(firstForm, secondForm,thirdForm);
		
//		layout1.setSpacing(true);
	
//		layout2.setSpacing(true);
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.setHeight("50px");
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
		

		if(null != newIntimationDTO.getPolicySummary())
		{	
				policyNumber.setValue(newIntimationDTO.getPolicySummary().getPolicyNo());
				//insuredName.setValue(newIntimationDTO.getInsuredPatient() != null ? newIntimationDTO.getInsuredPatient().getInsuredName() : "");
				             //need to implements
			
				
				/**
				 * StringBuilder object is used to concatenate two string objects.
				 * This is primarily done, to avoid creation of another string object when two 
				 * strings are concatenated , as String objects are immutable. 
				 * Whereas , StringBuilder are mutable. Hence when we append two or more 
				 * strings to the same object, in heap there would be only one string builder object created. 
				 * 
				 * */
				StringBuilder strPolicyBldr = new StringBuilder(); 
				if(newIntimationDTO.getOrganizationUnit() != null){
					strPolicyBldr.append(newIntimationDTO.getOrganizationUnit().getOrganizationUnitName()); 
					officeCode.setValue(strPolicyBldr.toString());
				}
			
	
			if(null != newIntimationDTO.getPolicySummary().getProduct())
			{
				productName.setValue(newIntimationDTO.getPolicySummary().getProduct().getValue());
			}
			if(null != newIntimationDTO.getPolicySummary().getProduct().getProductType())
			{
				TypeOfProduct.setValue(newIntimationDTO.getPolicySummary().getProduct().getProductType());
			}
		}else if(null != newIntimationDTO.getPolicy()){
			policyNumber.setValue(newIntimationDTO.getPolicy().getPolicyNumber());
			//insuredName.setValue(newIntimationDTO.getInsuredPatient() != null ? newIntimationDTO.getInsuredPatient().getInsuredName() : "");
			             //need to implements
		
			
			/**
			 * StringBuilder object is used to concatenate two string objects.
			 * This is primarily done, to avoid creation of another string object when two 
			 * strings are concatenated , as String objects are immutable. 
			 * Whereas , StringBuilder are mutable. Hence when we append two or more 
			 * strings to the same object, in heap there would be only one string builder object created. 
			 * 
			 * */
			StringBuilder strPolicyBldr = new StringBuilder(); 
			if(newIntimationDTO.getOrganizationUnit() != null){
				strPolicyBldr.append(newIntimationDTO.getOrganizationUnit().getOrganizationUnitName()); 
				officeCode.setValue(strPolicyBldr.toString());
			}
		

		if(null != newIntimationDTO.getPolicy().getProduct())
		{
			productName.setValue(newIntimationDTO.getPolicy().getProduct().getValue());
		}
		if(null != newIntimationDTO.getPolicy().getProduct().getProductType())
		{
			TypeOfProduct.setValue(newIntimationDTO.getPolicy().getProduct().getProductType());
		}
		}
		
		if(newIntimationDTO.getUsername() != null){
			TmpEmployee employeeName = claimrequestService.getEmployeeName(newIntimationDTO.getUsername().toLowerCase());
			if(employeeName != null){
				callerAttendeeName.setValue(employeeName.getEmpFirstName());
			}
		}

		
		portabilityPolicy.setValue(newIntimationDTO.getIsPortablity());
		policyNumber.setReadOnly(true);
		//insuredName.setReadOnly(true);
		officeCode.setReadOnly(true);
		productName.setReadOnly(true);
		TypeOfProduct.setReadOnly(true);
		callerAttendeeName.setReadOnly(true);
		portabilityPolicy.setReadOnly(true);
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


}
