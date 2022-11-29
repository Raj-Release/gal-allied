
package com.shaic.ims.carousel;

import java.util.List;
import java.util.Map;

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
import com.shaic.domain.MasUserLimitMapping;
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

public class RevisedCashlessCarousel extends ViewComponent {

	private static final long serialVersionUID = 5892717759896227659L;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@EJB
	private DBCalculationService dbCalculationService;

	private TextField claimId;
	private TextField policyNumber;
	private TextField productName;
	private TextField txtPolicyAgeing;
	
	private TextField policyYear;
	private TextField originalSI;
	private TextField netWorkHospitalType;
	//private Label netWorkHospitalType;
	private TextField hospitalName;
	
	private TextField hospitalCity;
	private TextField hospitalCode;
	private TextField claimTypeField;
	private TextField insuredName;
	
	private TextField balanceSI;
	private TextField admissionDateForCarousel;
	private TextField txtDischargeDate;
	private TextField intimationId;

	private TextField cpuCode;
	private TextField customerId;
	private TextField hospitalTypeName;
	private TextField hospitalIrdaCode;
	
//	private TextField currency;
	private TextField policyType;
	private TextField srCitizenClaim;
	private TextField productType;
	private TextField classOfBusiness;
	
	
	private TextField policyStartDate;
	private TextField insuredAge;
	private TextField policyPlan;
	private TextField insuredPatientName;
	
	private TextField diagnosis;
	
	private TextField portabilityPolicy ;
	
	private TextField noOfBeds;

	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;
	HorizontalLayout layout4;
	HorizontalLayout layout5;

	FormLayout fifthForm;
	
	private TextField gmcMainMemberName;
	//private TextField gmcInsuredType;
	//private TextField gmcEndrosementDate;
	private TextField gmcCompanyName;
	
	@PostConstruct
	public void initView() {

	}

	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption) {

		Panel panel = new Panel(caption);
		panel.setHeight("140px");
//		panel.setStyleName("policyinfogrid");
		panel.addStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		
		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);
		
		txtDischargeDate.setReadOnly(false);
		txtDischargeDate.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDischargeDate.setReadOnly(true);

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
		if(null != claimDTO && null != claimDTO.getKey()){
			
		if(!isGmcProduct){
			
			Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
			balanceSI.setReadOnly(false);
			balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
			balanceSI.setReadOnly(true);
		}
		else
		{
			Double balanceSI2 = dbCalculationService.getBalanceSIForGMC(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey());
			balanceSI.setReadOnly(false);
			balanceSI.setValue(String.valueOf(balanceSI2));
			balanceSI.setReadOnly(true);
		}
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

		Panel panel = new Panel(caption);
		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		
		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		//gmcInsuredType.setValue(newIntimationDTO.);
		
		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);
		
		txtDischargeDate.setReadOnly(false);
		txtDischargeDate.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDischargeDate.setReadOnly(true);

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
		if(null != claimDTO && null != claimDTO.getKey()){
			
		if(!isGmcProduct){
			
			Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
			balanceSI.setReadOnly(false);
			balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
			balanceSI.setReadOnly(true);
		}
		else
		{
			Double balanceSI2 = dbCalculationService.getBalanceSIForGMC(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey());
			balanceSI.setReadOnly(false);
			balanceSI.setValue(String.valueOf(balanceSI2));
			balanceSI.setReadOnly(true);
		}
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
	
	public void init(PreauthDTO preauthDto,String caption) {
		
		NewIntimationDto newIntimationDTO =preauthDto.getNewIntimationDTO();
		ClaimDto claimDTO =preauthDto.getClaimDTO();
		
		
		Panel panel = new Panel(caption);
		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		
		Boolean isGmcProduct = false;
		if(newIntimationDTO.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		panel.setContent(buildCarousel(isGmcProduct));
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);
		
		txtDischargeDate.setReadOnly(false);
		txtDischargeDate.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDischargeDate.setReadOnly(true);

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
			// JIRA  GALAXYMAIN-13440 FIXED 1.07	
//			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? String.valueOf(newIntimationDTO.getOrginalSI().intValue()) : "0");
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? String.valueOf(newIntimationDTO.getOrginalSI().intValue()) : "0");
			originalSI.setReadOnly(true);
		}
		if(null != claimDTO && null != claimDTO.getKey()){
		/*Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
		balanceSI.setReadOnly(false);
		balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
		balanceSI.setReadOnly(true);*/
			
			if(!isGmcProduct){
				
				Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
				balanceSI.setReadOnly(false);
				balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
				balanceSI.setReadOnly(true);
			}
			else
			{
				Double balanceSI2 = dbCalculationService.getBalanceSIForGMC(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey());
				balanceSI.setReadOnly(false);
				balanceSI.setValue(String.valueOf(balanceSI2));
				balanceSI.setReadOnly(true);
			}
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
								.getPolicy().getPolicyType().getKey().equals(ReferenceTable.PORTABILITY_POLICY_TYPE)?"YES":"NO"	: "":"");*/
		if (newIntimationDTO.getIsPortablity() != null){
			portabilityPolicy
			.setValue(newIntimationDTO.getIsPortablity());
		}
		portabilityPolicy.setReadOnly(true);

		// buildCarosuelDetials(intimationDto);

		setAddtionalValuesforCarousel(newIntimationDTO);
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
		
		CarouselVLayout.addComponent(buildCarousel(isGmcProduct));
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
		
		Boolean isGmcProduct = false;
		if(intimationDto.getPolicy().getProduct() != null && ReferenceTable.getGMCProductList().containsKey(intimationDto.getPolicy().getProduct().getKey())){
			isGmcProduct = true;
		}else{
			isGmcProduct = false;
		}
		
		CarouselVLayout.addComponent(buildCarousel(isGmcProduct));
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

	private HorizontalCarousel buildCarousel(Boolean isGmc) {
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
		FormLayout seventhForm = new FormLayout();
		seventhForm.setSpacing(false);
		seventhForm.setMargin(false);
		FormLayout eigthForm = new FormLayout();
		eigthForm.setSpacing(false);
		eigthForm.setMargin(false);
		
		FormLayout ninthForm = new FormLayout();
		ninthForm.setSpacing(false);
		ninthForm.setMargin(false);

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

		originalSI = new TextField("Original SI");
		//Vaadin8-setImmediate() originalSI.setImmediate(true);
		originalSI.setWidth("-1px");
		originalSI.setHeight("20px");
		originalSI.setReadOnly(true);
		originalSI.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		policyStartDate = new TextField("Policy Inception Date");
		//Vaadin8-setImmediate() policyStartDate.setImmediate(true);
		policyStartDate.setWidth("-1px");
		policyStartDate.setHeight("20px");
		policyStartDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

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
		
		hospitalCode = new TextField("Hospital Code");
		//Vaadin8-setImmediate() hospitalCode.setImmediate(true);
		hospitalCode.setWidth("300px");
		hospitalCode.setHeight("20px");
		hospitalCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

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

		admissionDateForCarousel = new TextField("Admission Date");
		//Vaadin8-setImmediate() admissionDateForCarousel.setImmediate(true);
		admissionDateForCarousel.setWidth("200px");
		admissionDateForCarousel.setHeight("20px");
		admissionDateForCarousel.setReadOnly(true);
		admissionDateForCarousel.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

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

		srCitizenClaim = new TextField("Sr Citizen Claim");
		//Vaadin8-setImmediate() srCitizenClaim.setImmediate(true);
		srCitizenClaim.setWidth("-1px");
		srCitizenClaim.setHeight("20px");
		srCitizenClaim.setReadOnly(true);
		srCitizenClaim.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("380px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		balanceSI = new TextField("Balance SI");
		//Vaadin8-setImmediate() balanceSI.setImmediate(true);
		balanceSI.setWidth("300px");
		balanceSI.setHeight("20px");
		balanceSI.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		txtDischargeDate = new TextField("Discharge Date");
		//Vaadin8-setImmediate() txtDischargeDate.setImmediate(true);
		txtDischargeDate.setWidth("300px");
		txtDischargeDate.setHeight("20px");
		txtDischargeDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		classOfBusiness = new TextField("Class Of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("250px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		netWorkHospitalType = new TextField("Network Hospital Type");
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
		netWorkHospitalType.setHeight("20px");
		//netWorkHospitalType.setStyleName("redcolor");
		//netWorkHospitalType.setStyleName("blink");	
		netWorkHospitalType.setVisible(true);
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyYear = new TextField("Policy Year");
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
		
		diagnosis = new TextField("Diagnosis");
		//Vaadin8-setImmediate() diagnosis.setImmediate(true);
		diagnosis.setWidth("300px");
		diagnosis.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		
		portabilityPolicy = new TextField("Portability Policy");
		//Vaadin8-setImmediate() portabilityPolicy.setImmediate(true);
		portabilityPolicy.setWidth("300px");
		portabilityPolicy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		gmcMainMemberName = new TextField("Main Member Name");
		//Vaadin8-setImmediate() gmcMainMemberName.setImmediate(true);
		gmcMainMemberName.setWidth("200px");
		gmcMainMemberName.setHeight("20px");
		gmcMainMemberName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		/*gmcInsuredType = new TextField("Insured Type");
		//Vaadin8-setImmediate() gmcInsuredType.setImmediate(true);
		gmcInsuredType.setWidth("180px");
		gmcInsuredType.setHeight("20px");
		gmcInsuredType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);	
		
		gmcEndrosementDate = new TextField("Endorsement Date");
		//Vaadin8-setImmediate() gmcEndrosementDate.setImmediate(true);
		gmcEndrosementDate.setWidth("200px");
		gmcEndrosementDate.setHeight("20px");
		gmcEndrosementDate.setReadOnly(true);
		gmcEndrosementDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);*/
		
		gmcCompanyName = new TextField("Company Name");
		//Vaadin8-setImmediate() gmcCompanyName.setImmediate(true);
		gmcCompanyName.setWidth("200px");
		gmcCompanyName.setHeight("20px");
		gmcCompanyName.setReadOnly(true);
		gmcCompanyName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		noOfBeds = new TextField("Number of Beds");
		noOfBeds.setWidth("220px");
		noOfBeds.setHeight("20px");
		noOfBeds.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		firstForm.addComponent(claimId);
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(productName);
//		firstForm.addComponent(txtPolicyAgeing);
		//GLX2020047
		firstForm.addComponent(hospitalName);
		
		secondForm.addComponent(policyYear);
		secondForm.addComponent(originalSI);
		secondForm.addComponent(netWorkHospitalType);
		secondForm.addComponent(diagnosis);
		
		//Commented for GLX2020047
		//thirdForm.addComponent(hospitalName);
		thirdForm.addComponent(hospitalCity);
		thirdForm.addComponent(hospitalCode);
		thirdForm.addComponent(claimTypeField);
		thirdForm.addComponent(noOfBeds);
		
		
		fourthForm.addComponent(balanceSI);
		fourthForm.addComponent(admissionDateForCarousel);
		fourthForm.addComponent(txtDischargeDate);
		if(isGmc){
			fourthForm.addComponent(gmcMainMemberName);	
		}else{
			fourthForm.addComponent(insuredName);
		}
		
		
		if(isGmc){
			//fifthForm.addComponent(gmcInsuredType);
			//fifthForm.addComponent(gmcEndrosementDate);
			fifthForm.addComponent(intimationId);
			fifthForm.addComponent(cpuCode);
			
			sixthForm.addComponent(customerId);
			sixthForm.addComponent(hospitalTypeName);
			sixthForm.addComponent(policyType);
			sixthForm.addComponent(srCitizenClaim);
			
			seventhForm.addComponent(productType);
			seventhForm.addComponent(classOfBusiness);
			seventhForm.addComponent(policyStartDate);
			seventhForm.addComponent(insuredAge);
			
			eigthForm.addComponent(policyPlan);
			eigthForm.addComponent(insuredPatientName);
			eigthForm.addComponent(hospitalIrdaCode);
			eigthForm.addComponent(portabilityPolicy);
			
			ninthForm.addComponent(gmcCompanyName);
			
		}else{
			fifthForm.addComponent(intimationId);
			fifthForm.addComponent(cpuCode);
			fifthForm.addComponent(customerId);
			fifthForm.addComponent(hospitalTypeName);
			
			sixthForm.addComponent(policyType);
			sixthForm.addComponent(srCitizenClaim);
			sixthForm.addComponent(productType);
			sixthForm.addComponent(classOfBusiness);
			
			seventhForm.addComponent(policyStartDate);
			seventhForm.addComponent(insuredAge);
			seventhForm.addComponent(policyPlan);
			seventhForm.addComponent(insuredPatientName);
			
			eigthForm.addComponent(hospitalIrdaCode);
			eigthForm.addComponent(portabilityPolicy);
		}

		
		
		
//		private TextField currency;
		
		

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
		layout1.setWidth("100%");
		layout1.setSpacing(true);
		layout2 = new HorizontalLayout(thirdForm,fourthForm);
//		layout2.setSpacing(true);
		layout3 = new HorizontalLayout(fifthForm,sixthForm);
		layout2.setSpacing(true);
		layout4 = new HorizontalLayout(seventhForm,eigthForm);
		layout5 = new HorizontalLayout(ninthForm);
		formsHorizontalLayout.addComponent(layout1);
		formsHorizontalLayout.addComponent(layout2);
		formsHorizontalLayout.addComponent(layout3);
		formsHorizontalLayout.addComponent(layout4);
		if(isGmc){
		formsHorizontalLayout.addComponent(layout5);
		}
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
		
		
		Hospitals hospCity = getHospCity(newIntimationDTO.getHospitalDto().getKey());

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
			
			gmcMainMemberName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
			
			gmcCompanyName.setReadOnly(false);
			gmcCompanyName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
			gmcCompanyName.setReadOnly(true);
			 if(colorNameForGmc != null){
			    	gmcCompanyName.setStyleName(colorNameForGmc);
			  }
//			insuredName.setValue(newIntimationDTO.getInsuredPatient()
//					.getInsuredName());
		}
		if (null != newIntimationDTO.getHospitalDto()) {
			String hosp = newIntimationDTO.getHospitalName() != null ? newIntimationDTO
					.getHospitalName() : "";
			hospitalName.setValue(StringUtils.trim(hospCity.getName()));
			
			//GLX2020047
			if(hospCity.getMediBuddy() != null && hospCity.getMediBuddy().equals(1)){
				hospitalName.setStyleName("yellow");
			}
		    
			
			
			
			/*String City = newIntimationDTO.getHospitalDto().getCity();*/
			if(hospCity!=null) {
			hospitalCity.setValue(hospCity.getCity()); }
			hospitalIrdaCode.setValue(hospCity.getHospitalIrdaCode());
			hospitalIrdaCode.setNullRepresentation("");
		}
		/*if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getHospitalCode()) {
			hospitalCode.setValue(newIntimationDTO.getIntimaterName());
			hospitalCode.setNullRepresentation("");
		}*/
		if(null != newIntimationDTO.getIntimaterName() && null != newIntimationDTO.getIntimaterName()) {
			hospitalCode.setValue(hospCity.getHospitalCode());
			hospitalCode.setNullRepresentation("");
		}
		if (null != newIntimationDTO.getHospitalType()) {
			hospitalTypeName.setValue(newIntimationDTO.getHospitalType().getValue());
		}
		if (null != newIntimationDTO.getPolicy().getProduct()) {
			productName.setValue(newIntimationDTO.getPolicy().getProduct()
					.getValue() + " - " + newIntimationDTO.getPolicy().getProduct().getCode());
			productName.setWidth("380px");
	
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
			
			/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey())) {
				policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
			}
			else if(ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(newIntimationDTO.getPolicy().getProduct().getKey())) {
				policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan());
			}else {*/
				policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
//			}
		}
		else{
			policyPlan.setValue("-");
		}
		
		if(newIntimationDTO.getPolicy().getProduct() != null 
				&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
					||ReferenceTable.STAR_GRP_COVID_PROD_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
						&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY))){
			policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_INDEMNITY);
		}else if(newIntimationDTO.getPolicy().getProduct() != null 
				&& ((ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
						|| ReferenceTable.STAR_GRP_COVID_PROD_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
						&& newIntimationDTO.getPolicy().getPolicyPlan().equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM))){
			policyPlan.setValue(SHAConstants.STAR_COVID_PLAN_LUMPSUM);
		}
//		else{
//			policyPlan.setValue("-");
//		}
		
		
		if(newIntimationDTO.getPolicy().getProduct() != null 
				&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
						SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
						|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())
						|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
						|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
								SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
								&& newIntimationDTO.getPolicy().getProductType() != null 
								&& newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY))) ) {
			policyPlan.setValue(newIntimationDTO.getInsuredPatient().getPolicyPlan() != null ? newIntimationDTO.getInsuredPatient().getPolicyPlan() : "-");
		}
		
		if(ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) && newIntimationDTO.getInsuredPatient().getPlan() != null && !newIntimationDTO.getInsuredPatient().getPlan().isEmpty()){
			if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_B)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN);
			}else if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_E)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN);
			}
	    }

		if(null != newIntimationDTO.getPolicy().getPolicyYear()){

			if(newIntimationDTO.getPolicy().getPolicyFromDate() != null && newIntimationDTO.getPolicy().getPolicyToDate() != null){
				String formatDate = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyFromDate());
				String formatDate2 = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyToDate());
				if(formatDate != null && formatDate2 != null){
					String policyDate = formatDate +" - "+ formatDate2;
					policyYear.setValue(policyDate);
				}
			}
			
		}
		
		/*if(newIntimationDTO.getPolicy().getPolicyFromDate() != null){
			String formatDate = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyFromDate());
			if(formatDate != null ){
				String policyDate = formatDate;
				policyStartDate.setReadOnly(false);
				policyStartDate.setValue(policyDate);
				policyStartDate.setReadOnly(true);
			}
		}*/
		
		if(newIntimationDTO.getPolicyInceptionDate() != null){
			String formatDate = SHAUtils.formatDate(newIntimationDTO.getPolicyInceptionDate());
			if(formatDate != null ){
				String policyDate = formatDate;
				policyStartDate.setReadOnly(false);
				policyStartDate.setValue(policyDate);
				policyStartDate.setReadOnly(true);
			}
		}

//		if (newIntimationDTO.getPolicy().getPolicyNumber() != null
//				&& newIntimationDTO.getAdmissionDate() != null) {
//			String policyAgeing = dbCalculationService.getPolicyAgeing(
//					newIntimationDTO.getAdmissionDate(), newIntimationDTO
//							.getPolicy().getPolicyNumber());
//			txtPolicyAgeing.setValue(policyAgeing);
//		}
		
		if (newIntimationDTO.getPolicyYear() != null) {
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
						
				if(null != newIntimationDTO.getHospitalDto().getFinalGradeName()){
						netWorkType	= netWorkType + "("+newIntimationDTO.getHospitalDto().getFinalGradeName()+" Category)";
					}	
						
				netWorkHospitalType.setValue(netWorkType);
				
				if(netWorkType.equals(SHAConstants.AGREED_NETWORK_HOSPITAL_TYPE))
				{
				//	String netWorkTypeWithColour=  "<span>"+String.valueOf(netWorkType)+"</span>";
					if(null != newIntimationDTO.getHospitalDto().getFinalGradeName()){
						netWorkType	= netWorkType + "("+newIntimationDTO.getHospitalDto().getFinalGradeName()+ " Category)";
					}
					netWorkHospitalType.setValue(netWorkType);
					//netWorkHospitalType.addStyleName("fontColour");
//					netWorkHospitalType.addStyleName("blink");
					//netWorkHospitalType.setForeground(Color.BLUE);
				}
				
				
			} else {
				if (layout2 != null && fifthForm != null
						&& netWorkHospitalType != null) {
					fifthForm.removeComponent(netWorkHospitalType);
				}
			}

		}
		
		if(hospCity!=null) {
			if(hospCity.getInpatientBeds() != null){
				noOfBeds.setValue((hospCity.getInpatientBeds()).toString());
			}
			else{
				noOfBeds.setValue("0"); 
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
		txtDischargeDate.setReadOnly(true);
		balanceSI.setReadOnly(true);
		hospitalCode.setReadOnly(true);
		noOfBeds.setReadOnly(true);
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

		if (tmpEmployeeList != null) {
			return tmpEmployeeList.get(0);

		}

		return null;

	}
	
	
	
	}


