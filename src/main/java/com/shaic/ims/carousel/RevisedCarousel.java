package com.shaic.ims.carousel;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.virkki.carousel.HorizontalCarousel;
import org.vaadin.virkki.carousel.client.widget.gwt.ArrowKeysMode;
import org.vaadin.virkki.carousel.client.widget.gwt.CarouselLoadMode;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Hospitals;
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

public class RevisedCarousel extends ViewComponent {
	private static final long serialVersionUID = 5892717759896227659L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private TextField intimationId;
	private TextField policyNumber;
	private TextField productName;
	private TextField txtPolicyAgeing;
	private TextField policyYear;
	
	private TextField insuredPatientName;
	private TextField insuredDOB;
	private TextField insuredAge;
	private TextField diagnosis;
	private TextField claimTypeField;
	private TextField dateOfAdmission;
	private TextField txtDateOfDischarge;

	private TextField hospitalTypeName;
	private TextField hospitalName;
	private TextField hospitalCode;
	private TextField hospitalCity;
	private TextField hospitalIrdaCode;
	
	private TextField originalSI;
	private TextField insuredName;
	private TextField cpuCode;
	private TextField claimId;
	private TextField cashlessApprovalAmt;

	private TextField policyType;
	private TextField customerId;
	private TextField srCitizenClaim;
	private TextField productType;
	private TextField classOfBusiness;
	
	//GMC product Type field
	private TextField txtMainMemberName;
	private TextField txtInsuredType;
	private TextField endorsementDate;
	
//	private TextField admissionDateForCarousel;
	
	private TextField portabilityPolicy;
	
	private TextField netWorkHospitalType;
	
	private TextField policyPlan;
	

	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;
	HorizontalLayout layout4;

	FormLayout fifthForm;
	
	private TextField gmcMainMemberName;
	private TextField gmcInsuredType;
	private TextField gmcEndrosementDate;
	private TextField gmcCompanyName;
	private TextField noOfDaysPerYear;
	private TextField policyTerm;
	
	private TextField numberOfBeds;
	
	NewIntimationDto intimationDtoForCarousel= new NewIntimationDto();

	@PostConstruct
	public void initView() {

	}

	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption) {
		claimDTO.setNewIntimationDto(newIntimationDTO);
		Panel panel = new Panel(caption);
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);

		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct,newIntimationDTO));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		txtDateOfDischarge.setReadOnly(false);
		txtDateOfDischarge.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDateOfDischarge.setReadOnly(true);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			if(newIntimationDTO != null && newIntimationDTO.getPaPatientName() != null) {
			insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
			}else if(newIntimationDTO != null && newIntimationDTO.getInsuredPatientName() != null) {
				insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
			}
		}
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && null != newIntimationDTO.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))) {
			if(newIntimationDTO != null && newIntimationDTO.getPaPatientDOB() != null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				insuredDOB.setValue(date);
			}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
			}
		}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}

		
		//added for new product076
				if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
					  null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
					  null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
					  null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
					if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
					if(newIntimationDTO.getPolicy().getPhcBenefitDays() != null && 
							!newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)) {
					  noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
					}
					else if(newIntimationDTO.getInsuredPatient().getHcpDays() != null && 
							newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
							&& newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))
					{
						noOfDaysPerYear.setValue(newIntimationDTO.getInsuredPatient().getHcpDays().toString());
					}
					if(newIntimationDTO.getPolicy().getPolicyTerm() != null) {
						policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
					 }
				}
				}
				if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
						null != newIntimationDTO.getPolicy().getProduct().getKey() && null != newIntimationDTO.getPaPatientDOB() &&
						(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) &&
						null != newIntimationDTO.getPaPatientAge()) {
					insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
				}
				
				insuredDOB.setReadOnly(true);
				insuredPatientName.setReadOnly(true);
				insuredAge.setReadOnly(true);
				noOfDaysPerYear.setReadOnly(true);
				policyTerm.setReadOnly(true);
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
		String colorNameForGmc = getColorNameForGmc(newIntimationDTO.getColorCode());
	    if(colorNameForGmc != null){
	    	claimId.setStyleName(colorNameForGmc);
	        intimationId.setStyleName(colorNameForGmc);
	    }
	    
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

		if (newIntimationDTO.getOrginalSI() != null) {
			originalSI.setReadOnly(false);
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? String.valueOf(newIntimationDTO.getOrginalSI().intValue()) : "0");
			originalSI.setReadOnly(true);
		}

		policyType.setReadOnly(false);
		policyType
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getValue()
						: "");
		policyType.setReadOnly(true);
		
		portabilityPolicy.setReadOnly(false);
		/*portabilityPolicy
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getKey() !=null ?newIntimationDTO
								.getPolicy().getPolicyType().getKey().equals(ReferenceTable.PORTABILITY_POLICY_TYPE)?"YES":"NO"
						: "":"");*/
		if (newIntimationDTO.getIsPortablity() != null){
			portabilityPolicy
			.setValue(newIntimationDTO.getIsPortablity());
		}
		portabilityPolicy.setReadOnly(true);

		// buildCarosuelDetials(intimationDto);

		setAddtionalValuesforCarousel(newIntimationDTO);
		
		//added for new product076
				if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
						null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
						null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
						null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
					diagnosis.setReadOnly(false);
					diagnosis.setValue(claimDTO.getDiagnosis());
					diagnosis.setReadOnly(true);
					diagnosis.setNullRepresentation("");
				}

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
	
	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption, String diagnosisStr) {
		claimDTO.setNewIntimationDto(newIntimationDTO);
		Panel panel = new Panel(caption);
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		
		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct,newIntimationDTO));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		txtDateOfDischarge.setReadOnly(false);
		txtDateOfDischarge.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDateOfDischarge.setReadOnly(true);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
		if(null != newIntimationDTO.getInsuredPatient() && null != newIntimationDTO.getInsuredPatient().getInsuredAge()) {
			insuredAge.setValue(newIntimationDTO.getInsuredPatient().getInsuredAge().toString());
		}
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			if(newIntimationDTO != null && newIntimationDTO.getPaPatientName() != null) {
			insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
			}else if(newIntimationDTO != null && newIntimationDTO.getInsuredPatientName() != null) {
				insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
			}
		}
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && null != newIntimationDTO.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			if(newIntimationDTO != null && newIntimationDTO.getPaPatientDOB() != null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				insuredDOB.setValue(date);
			}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
			}
		}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}
		//added for new product076
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
			if(newIntimationDTO.getPolicy().getPhcBenefitDays() != null && 
					!newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ) {
				noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
			}
			else if(newIntimationDTO.getInsuredPatient().getHcpDays() != null && 
					newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					&& newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))
			{
				noOfDaysPerYear.setValue(newIntimationDTO.getInsuredPatient().getHcpDays().toString());
			}
			 if(newIntimationDTO.getPolicy().getPolicyTerm() != null) {
				policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
			 }
		  }
		}
		
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && null != newIntimationDTO.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) &&
				null != newIntimationDTO.getPaPatientAge()) {
			insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
		}
		insuredPatientName.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredAge.setReadOnly(true);
		noOfDaysPerYear.setReadOnly(true);
		policyTerm.setReadOnly(true);
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
		String colorNameForGmc = getColorNameForGmc(newIntimationDTO.getColorCode());
	    if(colorNameForGmc != null){
	    	claimId.setStyleName(colorNameForGmc);
	    }
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
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? String.valueOf(newIntimationDTO.getOrginalSI().intValue()) : "0");
			originalSI.setReadOnly(true);
		}

		policyType.setReadOnly(false);
		policyType
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getValue()
						: "");
		policyType.setReadOnly(true);
		
		portabilityPolicy.setReadOnly(false);
		/*portabilityPolicy
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getKey() !=null ?newIntimationDTO
								.getPolicy().getPolicyType().getKey().equals(ReferenceTable.PORTABILITY_POLICY_TYPE)?"YES":"NO"
						: "":"");*/
		if (newIntimationDTO.getIsPortablity() != null){
			portabilityPolicy
			.setValue(newIntimationDTO.getIsPortablity());
		}
		portabilityPolicy.setReadOnly(true);

		// buildCarosuelDetials(intimationDto);

		setAddtionalValuesforCarousel(newIntimationDTO);
		diagnosis.setReadOnly(false);
		diagnosis.setValue(diagnosisStr);
		diagnosis.setReadOnly(true);
		diagnosis.setNullRepresentation("");
		

		//added for new product076
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			diagnosis.setReadOnly(false);
			diagnosis.setValue(diagnosisStr);
			diagnosis.setReadOnly(true);
			diagnosis.setNullRepresentation("");
		}

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
	
	public void init(PreauthDTO preauthDto,
			String caption) {
		
		
		NewIntimationDto newIntimationDTO =preauthDto.getNewIntimationDTO();
		ClaimDto claimDTO =preauthDto.getClaimDTO();

		Panel panel = new Panel(caption);
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		
		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct,newIntimationDTO));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		txtDateOfDischarge.setReadOnly(false);
		txtDateOfDischarge.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDateOfDischarge.setReadOnly(true);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			if(newIntimationDTO != null && newIntimationDTO.getPaPatientName() != null) {
			insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
		}else if(newIntimationDTO != null && newIntimationDTO.getInsuredPatientName() != null) {
			insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		}
		}
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) &&
				null != newIntimationDTO.getPaPatientDOB()){
			String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
			insuredDOB.setValue(date);
		}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}
		
		//added for new product076
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
				 if(newIntimationDTO.getPolicy().getPhcBenefitDays() != null &&
						 !newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)) {
				noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
				 }
				 else if(newIntimationDTO.getInsuredPatient().getHcpDays() != null && 
						 newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						 && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))
				 {
					 noOfDaysPerYear.setValue(newIntimationDTO.getInsuredPatient().getHcpDays().toString());
				 }
			 if(newIntimationDTO.getPolicy().getPolicyTerm() != null) {
				policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
			 }
		  }
		}
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && null != newIntimationDTO.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) &&
				null != newIntimationDTO.getPaPatientAge()) {
			insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
		}
		insuredAge.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		noOfDaysPerYear.setReadOnly(true);
		policyTerm.setReadOnly(true);
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
		String colorNameForGmc = getColorNameForGmc(newIntimationDTO.getColorCode());
	    if(colorNameForGmc != null){
	    	claimId.setStyleName(colorNameForGmc);
	    }
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
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? String.valueOf(newIntimationDTO.getOrginalSI().intValue()) : "0");
			originalSI.setReadOnly(true);
		}

		policyType.setReadOnly(false);
		policyType
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getValue()
						: "");
		policyType.setReadOnly(true);
		
		portabilityPolicy.setReadOnly(false);
		/*portabilityPolicy
				.setValue(newIntimationDTO.getPolicy().getPolicyType() != null ? newIntimationDTO
						.getPolicy().getPolicyType().getKey() !=null ?newIntimationDTO
								.getPolicy().getPolicyType().getKey().equals(ReferenceTable.PORTABILITY_POLICY_TYPE)?"YES":"NO"
						: "":"");*/
		if (newIntimationDTO.getIsPortablity() != null){
			portabilityPolicy
			.setValue(newIntimationDTO.getIsPortablity());
		}
		portabilityPolicy.setReadOnly(true);
		
		
		StringBuffer diagnosisName= new StringBuffer();
		if(preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList() != null && !preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			diagnosis.setReadOnly(false);
			List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList();
			for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
				if(diagnosisDetailsTableDTO.getDiagnosisName().getValue() != null){
					diagnosisName.append(diagnosisDetailsTableDTO.getDiagnosisName().getValue()).append(",");
				}
			}
			diagnosis.setValue(diagnosisName.substring(0, diagnosisName.length()-1));
			diagnosis.setReadOnly(true);
			diagnosis.setNullRepresentation("");
		}

		//added for new product076
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			diagnosis.setReadOnly(false);
			if(diagnosisName.length() > 0){
				diagnosis.setValue(diagnosisName.substring(0, diagnosisName.length()-1));
			}else{
				diagnosis.setValue(preauthDto.getPreauthDataExtractionDetails().getDiagnosis());
			}
			diagnosis.setReadOnly(true);
			diagnosis.setNullRepresentation("");
		}
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
		
		Boolean isGmcProduct = false;
		if(intimationDto.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimationDto.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		CarouselVLayout.addComponent(buildCarousel(isGmcProduct,intimationDto));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				intimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		setAddtionalValuesforCarousel(intimationDto);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		if(intimationDto.getInsuredPatient() != null  && null != intimationDto.getInsuredPatient().getInsuredDateOfBirth()) {
			String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}else if(intimationDto.getPaPatientDOB() != null) {
			String date = SHAUtils.formatDate(intimationDto.getPaPatientDOB());
			insuredDOB.setValue(date);
		}
		if(null != intimationDto.getInsuredPatient() && null != intimationDto.getInsuredPatient().getInsuredAge()) {
			insuredAge.setValue(intimationDto.getInsuredPatient().getInsuredAge().toString());
		}
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey()))){
			if(intimationDto != null && intimationDto.getPaPatientName() != null) {
			insuredPatientName.setValue(intimationDto.getPaPatientName());
			}else if(intimationDto != null && intimationDto.getInsuredPatientName() != null) {
				insuredPatientName.setValue(intimationDto.getInsuredPatientName());
			}
		}
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && null != intimationDto.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey())) &&
				null != intimationDto.getPaPatientDOB()) {
			String date = SHAUtils.formatDate(intimationDto.getPaPatientDOB());
			insuredAge.setValue(date);
		}
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && null != intimationDto.getPaPatientDOB() &&
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey())) &&
				null != intimationDto.getPaPatientAge()) {
			insuredAge.setValue(intimationDto.getPaPatientAge().toString());
		}
		insuredAge.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredPatientName.setReadOnly(true);
		claimTypeField.setReadOnly(false);

		if(intimationDto.getHospitalType()!=null){
			claimTypeField.setValue(StringUtils.equalsIgnoreCase("Network",
					intimationDto.getHospitalType().getValue()) ? "Cashless"
							: "Reimbursement");
			claimTypeField.setReadOnly(true);
		}

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
		
		Boolean isGmcProduct = false;
		if(intimationDto.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimationDto.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		CarouselVLayout.addComponent(buildCarousel(isGmcProduct,intimationDto));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				intimationDto);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);
		setAddtionalValuesforCarousel(intimationDto);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
		if(null != intimationDto.getInsuredPatient() && null != intimationDto.getInsuredPatient().getInsuredAge()) {
			insuredAge.setValue(intimationDto.getInsuredPatient().getInsuredAge().toString());
		}

		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey()))){
			if(intimationDto != null && intimationDto.getPaPatientName() != null) {
			insuredPatientName.setValue(intimationDto.getPaPatientName());
			}else if(intimationDto != null && intimationDto.getInsuredPatientName() != null) {
				insuredPatientName.setValue(intimationDto.getInsuredPatientName());
			}
		}
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey())) &&
				null != intimationDto.getPaPatientDOB()) {
			if(null != intimationDto && intimationDto.getPaPatientDOB() != null) {
				String date = SHAUtils.formatDate(intimationDto.getPaPatientDOB());
				insuredDOB.setValue(date);
			}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
			}
		}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() !=null) {
			String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}
		
		//added for new product076
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getCode() && intimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getCode() && intimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
			if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
				 if(intimationDto.getPolicy().getPhcBenefitDays() != null &&
						 !intimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)) {
				noOfDaysPerYear.setValue(intimationDto.getPolicy().getPhcBenefitDays().toString());
				 }
				 else if(intimationDto.getInsuredPatient().getHcpDays() != null && 
						 intimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						 && intimationDto.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))
				 {
					 noOfDaysPerYear.setValue(intimationDto.getInsuredPatient().getHcpDays().toString());
				 }
			 if(intimationDto.getPolicy().getPolicyTerm() != null) {
				policyTerm.setValue(intimationDto.getPolicy().getPolicyTerm().toString() + "  Year");
			 }
		  }
		}
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey())) &&
				null != intimationDto.getPaPatientAge()) {
			insuredAge.setValue(intimationDto.getPaPatientAge().toString());
		}
		insuredPatientName.setReadOnly(true);
		insuredAge.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		noOfDaysPerYear.setReadOnly(true);
		policyTerm.setReadOnly(true);
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

	private HorizontalCarousel buildCarousel(Boolean isGmc,NewIntimationDto newIntimationDTO) {
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
		intimationId.setWidth("280px");
		intimationId.setHeight("20px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		claimId = new TextField("Claim Number");
		//Vaadin8-setImmediate() claimId.setImmediate(true);
		claimId.setWidth("160px");
		claimId.setHeight("20px");
		claimId.setReadOnly(true);
		claimId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		claimTypeField = new TextField("Claim Type");
		//Vaadin8-setImmediate() claimTypeField.setImmediate(true);
		claimTypeField.setWidth("-1px");
		claimTypeField.setHeight("20px");
		claimTypeField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		cpuCode = new TextField("CPU Code");
		//Vaadin8-setImmediate() cpuCode.setImmediate(true);
		cpuCode.setWidth("-1px");
		cpuCode.setHeight("20px");
		cpuCode.setReadOnly(true);
		cpuCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && 
						newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						|| null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
								null != newIntimationDTO.getPolicy().getProduct().getCode() && 
								newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			originalSI = new TextField("Hospital Cash Amount per day Rs.");
		}
		else{
			originalSI = new TextField("Original SI");
		}
		//Vaadin8-setImmediate() originalSI.setImmediate(true);
		originalSI.setWidth("-1px");
		originalSI.setHeight("20px");
		originalSI.setReadOnly(true);
		originalSI.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
//		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		insuredDOB = new TextField("Insured Date Of Birth");
		insuredDOB.setHeight("20px");
		insuredDOB.setReadOnly(true);
		insuredDOB.addStyleNames(ValoTheme.TEXTFIELD_BORDERLESS);

		customerId = new TextField("Customer Id");
		//Vaadin8-setImmediate() customerId.setImmediate(true);
		customerId.setWidth("-1px");
		customerId.setHeight("20px");
		customerId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		hospitalName = new TextField("Hospital Name");
		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setWidth("300px");
		hospitalName.setHeight("20px");
		hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		hospitalCity = new TextField("Hospital City");
		//Vaadin8-setImmediate() hospitalCity.setImmediate(true);
		hospitalCity.setWidth("300px");
		hospitalCity.setHeight("20px");
		hospitalCity.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		hospitalIrdaCode = new TextField("Hospital IRDA Code");
		//Vaadin8-setImmediate() hospitalIrdaCode.setImmediate(true);
		hospitalIrdaCode.setWidth("300px");
		hospitalIrdaCode.setHeight("20px");
		hospitalIrdaCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		

		hospitalTypeName = new TextField("Hospital Type");
		//Vaadin8-setImmediate() hospitalTypeName.setImmediate(true);
		hospitalTypeName.setWidth("-1px");
		hospitalTypeName.setHeight("20px");
		hospitalTypeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtDateOfDischarge = new TextField("Discharge Date");
		//Vaadin8-setImmediate() txtDateOfDischarge.setImmediate(true);
		txtDateOfDischarge.setWidth("200px");
		txtDateOfDischarge.setHeight("20px");
		txtDateOfDischarge.setReadOnly(true);
		txtDateOfDischarge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		dateOfAdmission = new TextField("Admission Date");
		//Vaadin8-setImmediate() dateOfAdmission.setImmediate(true);
		dateOfAdmission.setWidth("200px");
		dateOfAdmission.setHeight("20px");
		dateOfAdmission.setReadOnly(true);
		dateOfAdmission.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("220px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		portabilityPolicy = new TextField("Portability Policy");
		//Vaadin8-setImmediate() portabilityPolicy.setImmediate(true);
		portabilityPolicy.setWidth("180px");
		portabilityPolicy.setHeight("20px");
		portabilityPolicy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		txtPolicyAgeing = new TextField("Policy Ageing");
		//Vaadin8-setImmediate() txtPolicyAgeing.setImmediate(true);
		txtPolicyAgeing.setWidth("180px");
		txtPolicyAgeing.setHeight("20px");
		txtPolicyAgeing.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("200px");
		insuredName.setHeight("20px");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		//GMP Product Code
		txtMainMemberName = new TextField("Main Member Name");
		//Vaadin8-setImmediate() txtMainMemberName.setImmediate(true);
		txtMainMemberName.setWidth("200px");
		txtMainMemberName.setHeight("20px");
		txtMainMemberName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtInsuredType = new TextField("Main Member Name");
		//Vaadin8-setImmediate() txtInsuredType.setImmediate(true);
		txtInsuredType.setWidth("200px");
		txtInsuredType.setHeight("20px");
		txtInsuredType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		endorsementDate = new TextField("Endorsement Date");
		//Vaadin8-setImmediate() endorsementDate.setImmediate(true);
		endorsementDate.setWidth("200px");
		endorsementDate.setHeight("20px");
		endorsementDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		srCitizenClaim = new TextField("Sr Citizen Claim");
		//Vaadin8-setImmediate() srCitizenClaim.setImmediate(true);
		srCitizenClaim.setWidth("-1px");
		srCitizenClaim.setHeight("20px");
		srCitizenClaim.setReadOnly(true);
		srCitizenClaim.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("360px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		classOfBusiness = new TextField("Class Of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("250px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		netWorkHospitalType = new TextField("Network Hospital Type");
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
		netWorkHospitalType.setHeight("20px");
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyYear = new TextField("Policy Period");
		//Vaadin8-setImmediate() policyYear.setImmediate(true);
		policyYear.setWidth("250px");
		policyYear.setHeight("20px");
		policyYear.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredAge = new TextField("Insured Age");
		//Vaadin8-setImmediate() insuredAge.setImmediate(true);
		insuredAge.setWidth("250px");
		insuredAge.setHeight("20px");
		insuredAge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		productType = new TextField("Product Type");
		//Vaadin8-setImmediate() productType.setImmediate(true);
		productType.setWidth("250px");
		productType.setHeight("20px");
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		policyPlan = new TextField("Plan");
		//Vaadin8-setImmediate() policyPlan.setImmediate(true);
		policyPlan.setWidth("300px");
		policyPlan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		hospitalCode = new TextField("Hospital Code");
		//Vaadin8-setImmediate() hospitalCode.setImmediate(true);
		hospitalCode.setWidth("300px");
		hospitalCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		cashlessApprovalAmt = new TextField("Cashless Approval Amt");
		//Vaadin8-setImmediate() cashlessApprovalAmt.setImmediate(true);
		cashlessApprovalAmt.setWidth("300px");
		cashlessApprovalAmt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		diagnosis = new TextField("Diagnosis");
		//Vaadin8-setImmediate() diagnosis.setImmediate(true);
		diagnosis.setWidth("300px");
		diagnosis.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		gmcMainMemberName = new TextField("Main Member Name");
		//Vaadin8-setImmediate() gmcMainMemberName.setImmediate(true);
		gmcMainMemberName.setWidth("200px");
		gmcMainMemberName.setHeight("20px");
		gmcMainMemberName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		gmcInsuredType = new TextField("Insured Type");
		//Vaadin8-setImmediate() gmcInsuredType.setImmediate(true);
		gmcInsuredType.setWidth("180px");
		gmcInsuredType.setHeight("20px");
		gmcInsuredType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
		
		gmcEndrosementDate = new TextField("Endorsement Date");
		//Vaadin8-setImmediate() gmcEndrosementDate.setImmediate(true);
		gmcEndrosementDate.setWidth("200px");
		gmcEndrosementDate.setHeight("20px");
		gmcEndrosementDate.setReadOnly(true);
		gmcEndrosementDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		gmcCompanyName = new TextField("Company Name");
		//Vaadin8-setImmediate() gmcCompanyName.setImmediate(true);
		gmcCompanyName.setWidth("200px");
		gmcCompanyName.setHeight("20px");
		gmcCompanyName.setReadOnly(true);
		gmcCompanyName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		noOfDaysPerYear = new TextField("Maximum Number of Days Per Year");
		//Vaadin8-setImmediate() gmcCompanyName.setImmediate(true);
		noOfDaysPerYear.setWidth("200px");
		noOfDaysPerYear.setHeight("20px");
		noOfDaysPerYear.setReadOnly(true);
		noOfDaysPerYear.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		policyTerm = new TextField("Policy Term");
		//Vaadin8-setImmediate() cashlessApprovalAmt.setImmediate(true);
		policyTerm.setWidth("300px");
		policyTerm.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		numberOfBeds = new TextField("Number of Beds");
		numberOfBeds.setWidth("220px");
		numberOfBeds.setHeight("20px");

		numberOfBeds.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		// formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		
		firstForm.addComponent(intimationId);
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(productName);
//		firstForm.addComponent(txtPolicyAgeing);
		firstForm.addComponent(policyYear);
		firstForm.addComponent(dateOfAdmission);
		
		secondForm.addComponent(insuredPatientName);
		secondForm.addComponent(insuredDOB);
		secondForm.addComponent(insuredAge);
//		
		secondForm.addComponent(claimTypeField);
		
		secondForm.addComponent(txtDateOfDischarge);
		
		thirdForm.addComponent(hospitalTypeName);
		thirdForm.addComponent(hospitalName);
		thirdForm.addComponent(numberOfBeds);
		thirdForm.addComponent(hospitalCode);
		thirdForm.addComponent(hospitalCity);
		thirdForm.addComponent(diagnosis);
		
		
		fourthForm.addComponent(originalSI);
		if(isGmc){
			fourthForm.addComponent(gmcMainMemberName);	
			fourthForm.addComponent(gmcInsuredType);
			fourthForm.addComponent(gmcEndrosementDate);
			fourthForm.addComponent(cpuCode);
			
			fifthForm.addComponent(claimId);
			fifthForm.addComponent(cashlessApprovalAmt);
			fifthForm.addComponent(policyType);
			fifthForm.addComponent(customerId);
			fifthForm.addComponent(srCitizenClaim);
			
			sixthForm.addComponent(productType);
			sixthForm.addComponent(classOfBusiness);
			sixthForm.addComponent(netWorkHospitalType);
			sixthForm.addComponent(policyPlan);
			sixthForm.addComponent(hospitalIrdaCode);
			
			seventhForm.addComponent(portabilityPolicy);
			seventhForm.addComponent(gmcCompanyName);
			
		}else{
			fourthForm.addComponent(insuredName);
			fourthForm.addComponent(cpuCode);
			fourthForm.addComponent(claimId);
			fourthForm.addComponent(cashlessApprovalAmt);
			
			fifthForm.addComponent(policyType);
			fifthForm.addComponent(customerId);
			fifthForm.addComponent(srCitizenClaim);
			fifthForm.addComponent(productType);
			fifthForm.addComponent(classOfBusiness);
			
			sixthForm.addComponent(netWorkHospitalType);
			sixthForm.addComponent(policyPlan);
			sixthForm.addComponent(hospitalIrdaCode);
			sixthForm.addComponent(portabilityPolicy);
			sixthForm.addComponent(noOfDaysPerYear);
		}
		
		layout4 = new HorizontalLayout(seventhForm); 
		layout4.setSpacing(true);
		//seventhForm.addComponent(numberOfBeds);
		
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getCode() && 
						newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
					|| null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
						null != newIntimationDTO.getPolicy().getProduct().getCode() && 
						newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)) {
			fourthForm.addComponent(insuredName);
			fourthForm.addComponent(cpuCode);
			fourthForm.addComponent(claimId);
			fourthForm.addComponent(policyTerm);
			fourthForm.removeComponent(cashlessApprovalAmt);
			
			fifthForm.addComponent(policyType);
			fifthForm.addComponent(customerId);
			fifthForm.addComponent(srCitizenClaim);
			fifthForm.addComponent(productType);
			fifthForm.addComponent(classOfBusiness);
			
			sixthForm.addComponent(netWorkHospitalType);
			sixthForm.addComponent(policyPlan);
			sixthForm.addComponent(hospitalIrdaCode);
			sixthForm.addComponent(portabilityPolicy);
			sixthForm.addComponent(noOfDaysPerYear);
		}
		
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
//		layout2.setSpacing(true);
		layout3 = new HorizontalLayout(fifthForm,sixthForm);
		layout2.setSpacing(true);
		
			
		
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.addComponent(layout3);
		//formsHorizontalLayout.addComponent(layout4);
		if(isGmc){
			formsHorizontalLayout.addComponent(layout4);	
		}else{
			if(layout4 != null){
				layout4.setVisible(false);	
			}
		}
		
		formsHorizontalLayout.setHeight("170px");
     
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
			
			String colorNameForGmc = getColorNameForGmc(newIntimationDTO.getColorCode());
		    if(colorNameForGmc != null){
		    	policyNumber.setStyleName(colorNameForGmc);
		    }
			
			
			insuredName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
			
			gmcMainMemberName.setValue(newIntimationDTO.getGmcMainMemberName());
			gmcCompanyName.setReadOnly(false);
			gmcCompanyName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
			
//			diagnosis.setValue(newIntimationDTO.getDiagnosis());
			
		    if(colorNameForGmc != null){
		    	gmcCompanyName.setStyleName(colorNameForGmc);
		    }
			
			gmcCompanyName.setReadOnly(true);
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
		
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getHospitalCode()) {
			hospitalCode.setValue(newIntimationDTO.getHospitalDto().getHospitalCode());
			hospitalCode.setNullRepresentation("");
		}
		
		if (null != newIntimationDTO.getPolicy().getProduct()) {
			productName.setValue(newIntimationDTO.getPolicy().getProduct()
					.getValue() + " - " + newIntimationDTO.getPolicy().getProduct().getCode());
		}
		
		String colorNameForGmc = getColorNameForGmc(newIntimationDTO.getColorCode());
	    if(colorNameForGmc != null){
	    	productName.setStyleName(colorNameForGmc);
	    	intimationId.setStyleName(colorNameForGmc);
	    }
		
		if (null != newIntimationDTO.getPolicy().getProductType()) {
			productType.setValue(newIntimationDTO.getPolicy().getProductType()
					.getValue());
		}
		
		
		if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
			
			 if(SHAConstants.PRODUCT_CODE_076.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
					 SHAConstants.GROUP_HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())){
				if(newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLN_B)) {
					policyPlan.setValue("Basic");
				}else if(newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_PLN_E)) {
					policyPlan.setValue("Enhanced");
				}
			}else{
					policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
			}
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
		}
////		else{
//			policyPlan.setValue("-");
//		}
	
		
		if(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())  ||
				SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) )
				|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
				|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
				|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
				&& newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))) {
			if(newIntimationDTO.getInsuredPatient().getPolicyPlan() != null){
				policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
			}else{
				policyPlan.setValue("-");
			}
		}
		
		if(ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) && newIntimationDTO.getInsuredPatient().getPlan() != null 
				&& !newIntimationDTO.getInsuredPatient().getPlan().isEmpty()){
			if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_B)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN);
			}else if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_E)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN);
			}
	    }

		//In Bancs there is no underwriting year field....sathish suggestion
		//if(null != newIntimationDTO.getPolicy().getPolicyYear()){

			if(newIntimationDTO.getPolicy().getPolicyFromDate() != null && newIntimationDTO.getPolicy().getPolicyToDate() != null){
				String formatDate = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyFromDate());
				String formatDate2 = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyToDate());
				if(formatDate != null && formatDate2 != null){
					String policyDate = formatDate +" - "+ formatDate2;
					policyYear.setValue(policyDate);
				}
			}
			
	//	}

		if (newIntimationDTO.getPolicy().getPolicyNumber() != null
				&& newIntimationDTO.getAdmissionDate() != null) {
//			String policyAgeing = dbCalculationService.getPolicyAgeing(
//					newIntimationDTO.getAdmissionDate(), newIntimationDTO
//							.getPolicy().getPolicyNumber());
			
			String policyAgeing = newIntimationDTO.getPolicyYear();
			txtPolicyAgeing.setValue(policyAgeing);
		}

		if (null != newIntimationDTO.getPolicy().getProductType()) {
			String classofBusiness = newIntimationDTO.getLineofBusiness() != null ? newIntimationDTO
					.getLineofBusiness() : "";
			classOfBusiness.setValue(classofBusiness);

		}

		// if(newIntimationDTO.getPolicy().getp)
		Integer insured = 0;
		insuredAge.setReadOnly(false);
		if (newIntimationDTO.getInsuredPatient() != null
				&& newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
			
			 insured =  newIntimationDTO.getInsuredPatient()
					.getInsuredAge().intValue();
//			String[] age = insuredAge.split(".");
			
			insuredAge.setValue(insured.toString()+" years");
			insuredAge.setReadOnly(true);
		}
		
		else
		{
			if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
					null != newIntimationDTO.getPolicy().getProduct().getKey() && 
					(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
				
				if (newIntimationDTO.getParentAge() != null) {
				insured = newIntimationDTO.getParentAge().intValue();
				}
			
			if(null != newIntimationDTO.getPaPatientAge()) {
			insuredAge.setValue(newIntimationDTO.getPaPatientAge().intValue()+" Years");
			}
			}
			insuredAge.setReadOnly(true);
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
						
				if(null != newIntimationDTO.getHospitalDto().getFinalGradeName()){
					netWorkType	= netWorkType + "("+newIntimationDTO.getHospitalDto().getFinalGradeName()+" Category)";
				}
						
				netWorkHospitalType.setValue(netWorkType);
				if(netWorkType.equals(SHAConstants.AGREED_NETWORK_HOSPITAL_TYPE))
				{
					if(null != newIntimationDTO.getHospitalDto().getFinalGradeName()){
						netWorkType	= netWorkType + "("+newIntimationDTO.getHospitalDto().getFinalGradeName()+" Category)";
					}
					netWorkHospitalType.setValue(netWorkType);					
//					netWorkHospitalType.addStyleName("blink");
				}
			} else {
				if (layout2 != null && fifthForm != null
						&& netWorkHospitalType != null) {
					fifthForm.removeComponent(netWorkHospitalType);
				}
			}

		}
		if(newIntimationDTO.getHospitalDto() !=null){
			Hospitals hospCity = getHospCity(newIntimationDTO.getHospitalDto().getKey());

			if(hospCity!=null) {
				if(hospCity.getInpatientBeds() != null){
					numberOfBeds.setValue((hospCity.getInpatientBeds()).toString());
				}
				else{
					numberOfBeds.setValue("0"); 
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
		portabilityPolicy.setReadOnly(true);
		txtPolicyAgeing.setReadOnly(true);
		classOfBusiness.setReadOnly(true);
		netWorkHospitalType.setReadOnly(true);
		policyYear.setReadOnly(true);
		insuredAge.setReadOnly(true);
		diagnosis.setReadOnly(true);
		hospitalCode.setReadOnly(true);
		cashlessApprovalAmt.setReadOnly(true);
		noOfDaysPerYear.setReadOnly(true);
		policyTerm.setReadOnly(true);
		numberOfBeds.setReadOnly(true);
	}
	
	public void setEnableOrDisableFields(){
		cashlessApprovalAmt.setVisible(false);
	}
	
	public String getColorNameForGmc(String colorCode){
		if(colorCode != null){
			if(colorCode.equals("YELLOW")){
				
				return "yellow";
			}
			else if(colorCode.equals("RED")){
				
				return "red";
			}
			else if(colorCode.equals("GREEN"))
			{
				return "green";
			}
		}
		
		return null;
	
	}
	
	public Hospitals getHospCity(Long hospKey) {

		Query query = entityManager
				.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", hospKey);

		List<Hospitals> tmpEmployeeList = (List<Hospitals>) query.getResultList();

		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			return tmpEmployeeList.get(0);

		}

		return null;

	}

}