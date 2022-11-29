
package com.shaic.ims.carousel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.MockWebConnection.RawResponseData;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ViewRevisedCashlessTopPanel extends Window {

	private static final long serialVersionUID = 5892717759896227659L;
	@EJB
	private DBCalculationService dbCalculationService;
	
	@PersistenceContext
	protected EntityManager entityManager;

	private TextField claimId;
	private TextField policyNumber;
	private TextField productName;
	private TextField txtPolicyAgeing;
	
	private TextField policyYear;
	private TextField originalSI;
	private TextField netWorkHospitalType;
	private TextField hospitalName;
	
	private TextField hospitalCity;
	private TextField hospitalCode;
	private TextField hospitalStatus;
	private TextArea rawRremarks;
	private TextField claimTypeField;
	private TextField insuredName;
	private TextField insuredDOB;
	
	private TextField preauthAppAmt;
	
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
	private TextField policyPeriod;
	private TextField insuredAge;
	private TextField policyPlan;
	private TextField insuredPatientName;
	
	private TextField diagnosis;
	
	private TextField portabilityPolicy ;

	private	HorizontalLayout formsHorizontalLayout;
	private FormLayout leftForm;
	private FormLayout rightForm;
	
	private TextField noOfDaysPerYear;
	private TextField policyTerm;
	
	private TextField numberOfBeds;
	
	private TextField txtEffectiveFromDate;
	private TextField txtEffectiveToDate;
	
	@PostConstruct
	public void initView() {

	}

	@SuppressWarnings("deprecation")
	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDto,
			String caption) {

		Panel panel = new Panel();
//		panel.setStyleName("policyinfogrid");
		panel.addStyleName("girdBorder");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildFormFields(newIntimationDTO));		
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		binder.bindMemberFields(this);

		admissionDateForCarousel.setReadOnly(false);
		admissionDateForCarousel.setValue(null != claimDto && claimDto.getAdmissionDate() != null ? SHAUtils.formatDate(claimDto
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		admissionDateForCarousel.setReadOnly(true);
		
		txtDischargeDate.setReadOnly(false);
		txtDischargeDate.setValue(null != claimDto ?SHAUtils.formatDate(claimDto.getDischargeDate()):"");
		txtDischargeDate.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		insuredPatientName.setReadOnly(true);
		claimTypeField.setReadOnly(false);

		srCitizenClaim.setReadOnly(false);
		if (newIntimationDTO.getInsuredPatient().getInsuredAge() != null
				&& newIntimationDTO.getInsuredPatient().getInsuredAge() != 0) {
			srCitizenClaim.setValue(Double.valueOf(newIntimationDTO
					.getInsuredPatient().getInsuredAge()) >= 60 ? "YES" : "NO");
		}
		srCitizenClaim.setReadOnly(true);

		claimId.setReadOnly(false);
		claimId.setValue(null != claimDto ?claimDto.getClaimId():"");
		claimId.setReadOnly(true);
		claimId.setWidth("400px");
		if (null != claimDto && null != claimDto.getClaimType()) {
			claimTypeField.setValue(claimDto.getClaimType().getValue() != null ?claimDto.getClaimType().getValue() : "");
		}
		
		claimTypeField.setReadOnly(true);
		
		preauthAppAmt.setReadOnly(false);
		preauthAppAmt.setValue(null != claimDto && claimDto.getCashlessAppAmt() != null ? String.valueOf(claimDto.getCashlessAppAmt()) : "");
		preauthAppAmt.setReadOnly(true);
		insuredAge.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredPatientName.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
			Double insuredSumInsured = 0d;
			if(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())){
				insuredSumInsured = dbCalculationService.getGPAInsuredSumInsured(newIntimationDTO.getInsuredPatient()
								.getInsuredId().toString(), newIntimationDTO.getPolicy().getKey());
				insuredDOB.setVisible(true);
				if(newIntimationDTO.getPaPatientAge() != null) {
					insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
					}
				insuredDOB.setVisible(true);
				if(newIntimationDTO.getPaPatientAge() != null) {
				insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
					insuredAge.setValue(newIntimationDTO.getInsuredPatient().getInsuredAge().toString());
				}
				
					if(newIntimationDTO.getPaPatientDOB() != null) {
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
								!newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)){
						noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
						}
						else if(newIntimationDTO.getInsuredPatient().getHcpDays() != null && 
								 newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
								 && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)){
							 noOfDaysPerYear.setValue(newIntimationDTO.getInsuredPatient().getHcpDays().toString());
						 }
						if(newIntimationDTO.getPolicy().getPolicyTerm() != null){
						policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
						}
					}
					}
				if(newIntimationDTO.getPaPatientName() != null) {
					insuredPatientName.setReadOnly(false);
					insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
					insuredPatientName.setReadOnly(true);
				}
			}else{
				insuredSumInsured = dbCalculationService.getInsuredSumInsured(
						newIntimationDTO.getInsuredPatient()
								.getInsuredId().toString(), newIntimationDTO.getPolicy().getKey(),newIntimationDTO.getInsuredPatient().getLopFlag());
//				insuredDOB.setVisible(false);
				if(null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_GRP_PRODUCT_CODE)){
					policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
				}
				
				if(newIntimationDTO.getPolicy().getPolicyTerm() != null &&  null != newIntimationDTO.getPolicy().getProduct().getCode() 
						&& newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_CODE)){
					policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
				}
			}
			
		// Add for Kavach product policy term cuming in no of days convert into
		// months
		if (newIntimationDTO.getPolicy().getPolicyTerm() != null
				&& null != newIntimationDTO.getPolicy().getProduct().getCode()
				&& (newIntimationDTO
						.getPolicy()
						.getProduct()
						.getCode()
						.equalsIgnoreCase(
								ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)
								|| newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE))) {
			Double valueOfMonth = Double.valueOf(newIntimationDTO.getPolicy()
					.getPolicyTerm()) / 30;
			Long noOfmonth = newIntimationDTO.getPolicy().getPolicyTerm() / 30;
			if (valueOfMonth % noOfmonth == 0) {
				policyTerm.setValue(noOfmonth.toString() + "months");
			}else if(noOfmonth % 2 == 0){
				policyTerm.setValue(noOfmonth.toString() + "months");
			} else {
				if (valueOfMonth.longValue() == 0) {
					policyTerm.setValue("1/2 months");
				} else {
					policyTerm
							.setValue(String.valueOf(valueOfMonth.longValue())
									+ "1/2 months");
				}
			}
		}
			
		if (newIntimationDTO.getPolicy().getPolicyTerm() != null
				&& null != newIntimationDTO.getPolicy().getProduct().getCode()
				&& newIntimationDTO
						.getPolicy()
						.getProduct()
						.getCode()
						.equalsIgnoreCase(
								ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE)) {
			Double valueOfMonth = Double.valueOf(newIntimationDTO.getPolicy()
					.getPolicyTerm()) / 30;
			Long noOfmonth = newIntimationDTO.getPolicy().getPolicyTerm() / 30;
			if (valueOfMonth % noOfmonth == 0) {
				policyTerm.setValue(noOfmonth.toString() + "months");
			} else {
				if (valueOfMonth.longValue() == 0) {
					policyTerm.setValue("1/2 months");
				} else {
					policyTerm
							.setValue(String.valueOf(valueOfMonth.longValue())
									+ "1/2 months");
				}
			}
		}
		
		//added for setting policy term in view top panel screen
		if(newIntimationDTO.getPolicy().getPolicyTerm() != null && !(null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_CORONA_KAVACH_PRODUCT_CODE)
				|| newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.STAR_GRP_COVID_PROD_CODE)
				|| newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(ReferenceTable.RAKSHAK_CORONA_PRODUCT_CODE))){
			policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
		}
			
			
			insuredAge.setReadOnly(true);
			insuredDOB.setReadOnly(true);
			insuredPatientName.setReadOnly(true);
			noOfDaysPerYear.setReadOnly(true);
			policyTerm.setReadOnly(true);
			originalSI.setReadOnly(false);
			newIntimationDTO.setOrginalSI(insuredSumInsured != null ? insuredSumInsured : null);
			double calOrginalSI = newIntimationDTO.getOrginalSI();
//			originalSI.setValue(insuredSumInsured != null ? insuredSumInsured.toString() : "0");
			BigDecimal bigDecimal = new BigDecimal(calOrginalSI);
			bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
			originalSI.setValue(bigDecimal.toString());
			originalSI.setReadOnly(true);
		
		
		Map<String, Double> balanceSI2 = new HashMap<String, Double>();
		if(null != claimDto && null != claimDto.getKey()){
			balanceSI2= dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDto.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
		}
		else{
			balanceSI2= dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), 0L, newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
		}
		balanceSI.setReadOnly(false);
		balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
		balanceSI.setReadOnly(true);
		
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

		setAddtionalValuesforCarousel(newIntimationDTO);
		
		setCaption(caption);
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(panel);
		this.setWidth("74%");
		this.setHeight("68%");		
	}
	
	
	public void init(NewIntimationDto newIntimationDTO, ClaimDto claimDTO,
			String caption, String diagnosisStr) {

		Panel panel = new Panel(caption);
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildFormFields(newIntimationDTO));
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

		if (null != claimDTO.getClaimType()
				&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(claimDTO
					.getClaimType().getId())) {
			claimTypeField.setValue(SHAConstants.REIMBURSEMENT_CLAIM_TYPE);
		} else {
			claimTypeField.setValue(SHAConstants.CASHLESS_CLAIM_TYPE);
		}

		claimTypeField.setReadOnly(true);

		if (claimDTO.getCurrencyId() != null) {
			originalSI.setReadOnly(false);
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? newIntimationDTO.getOrginalSI().toString() : "0");
			originalSI.setReadOnly(true);
		}
		if(null != claimDTO && null != claimDTO.getKey()){
		Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
		balanceSI.setReadOnly(false);
		balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
		balanceSI.setReadOnly(true);
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

		setAddtionalValuesforCarousel(newIntimationDTO);
				
		setCaption(caption);
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(panel);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
	}
	
	public void init(PreauthDTO preauthDto,String caption) {
		
		NewIntimationDto newIntimationDTO =preauthDto.getNewIntimationDTO();
		ClaimDto claimDTO =preauthDto.getClaimDTO();
		
		
		Panel panel = new Panel();
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildFormFields(newIntimationDTO));
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
		if (null != claimDTO.getClaimType()
				&& ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY.equals(claimDTO
					.getClaimType().getId())) {
			claimTypeField.setValue("Reimbursement");
		} else {
			claimTypeField.setValue("Cashless");
		}

		claimTypeField.setReadOnly(true);

		if (claimDTO.getCurrencyId() != null) {
			originalSI.setReadOnly(false);
			originalSI.setValue(newIntimationDTO.getOrginalSI() != null ? newIntimationDTO.getOrginalSI().toString() : "0");
			originalSI.setReadOnly(true);
		}
		if(null != claimDTO && null != claimDTO.getKey()){
		Map<String, Double> balanceSI2 = dbCalculationService.getBalanceSI(newIntimationDTO.getPolicy().getKey(), newIntimationDTO.getInsuredPatient().getKey(), claimDTO.getKey(), newIntimationDTO.getOrginalSI(), newIntimationDTO.getKey());
		balanceSI.setReadOnly(false);
		balanceSI.setValue(balanceSI2.containsKey(SHAConstants.TOTAL_BALANCE_SI) ? String.valueOf(balanceSI2.get(SHAConstants.TOTAL_BALANCE_SI))  : "");
		balanceSI.setReadOnly(true);
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

		setCaption(caption);
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(panel);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
	}

	public void init(NewIntimationDto intimationDto) {
		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setStyleName("policyinfogrid");

		CarouselVLayout.addComponent(buildFormFields(intimationDto));
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
		setCaption("Details");
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(CarouselVLayout);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
	}
	
	

	public void init(NewIntimationDto intimationDto, String caption) {

		VerticalLayout CarouselVLayout = new VerticalLayout();
		CarouselVLayout.setStyleName("policyinfogrid");
		CarouselVLayout.addComponent(buildFormFields(intimationDto));
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
		setCaption(caption);
		setModal(true);
		setClosable(true);
		setResizable(true);
		setContent(CarouselVLayout);
		this.setWidth("90%");
		this.setHeight(640, Unit.PIXELS);
	}

	private HorizontalLayout buildFormFields(NewIntimationDto newIntimationDto) {
		leftForm = new FormLayout();
		leftForm.setSpacing(false);
		leftForm.setMargin(true);
		leftForm.setSizeFull();
		
		rightForm = new FormLayout();
		rightForm.setSpacing(false);
		rightForm.setMargin(true);
		rightForm.setSizeFull();
		
		intimationId = new TextField("Intimation No");
		//Vaadin8-setImmediate() intimationId.setImmediate(true);
		intimationId.setWidth("-1px");
		intimationId.setHeight("20px");
		intimationId.setReadOnly(true);
		intimationId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(intimationId);
		
		claimId = new TextField("Claim Number");
		//Vaadin8-setImmediate() claimId.setImmediate(true);
		claimId.setWidth("160px");
		claimId.setHeight("20px");
		claimId.setReadOnly(true);
		claimId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(claimId);
		
		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(insuredPatientName);
				
		insuredName = new TextField("Insured Name");
		//Vaadin8-setImmediate() insuredName.setImmediate(true);
		insuredName.setWidth("200px");
		insuredName.setHeight("20px");
		insuredName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(insuredName);
		
		insuredDOB = new TextField("Insured Date Of Birth");
		insuredDOB.setWidth("200px");
		insuredDOB.setHeight("20px");
		insuredDOB.setVisible(true);
		insuredDOB.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(insuredDOB);
		
		insuredAge = new TextField("Insured Age");
		//Vaadin8-setImmediate() insuredAge.setImmediate(true);
		insuredAge.setWidth("250px");
		insuredAge.setHeight("20px");
		insuredAge.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(insuredAge);
				
		claimTypeField = new TextField("Claim Type");
		//Vaadin8-setImmediate() claimTypeField.setImmediate(true);
		claimTypeField.setWidth("-1px");
		claimTypeField.setHeight("20px");
		claimTypeField.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(claimTypeField);
				
		admissionDateForCarousel = new TextField("Admission Date");
		//Vaadin8-setImmediate() admissionDateForCarousel.setImmediate(true);
		admissionDateForCarousel.setWidth("200px");
		admissionDateForCarousel.setHeight("20px");
		admissionDateForCarousel.setReadOnly(true);
		admissionDateForCarousel.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(admissionDateForCarousel);
				
		txtDischargeDate = new TextField("Discharge Date");
		//Vaadin8-setImmediate() txtDischargeDate.setImmediate(true);
		txtDischargeDate.setWidth("300px");
		txtDischargeDate.setHeight("20px");
		txtDischargeDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(txtDischargeDate);
				
		diagnosis = new TextField("Diagnosis");
		//Vaadin8-setImmediate() diagnosis.setImmediate(true);
		diagnosis.setWidth("300px");
		diagnosis.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(diagnosis);
				
		hospitalTypeName = new TextField("Hospital Type");
		//Vaadin8-setImmediate() hospitalTypeName.setImmediate(true);
		hospitalTypeName.setWidth("-1px");
		hospitalTypeName.setHeight("20px");
		hospitalTypeName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);		
		leftForm.addComponent(hospitalTypeName);
				
		netWorkHospitalType = new TextField("Network Hospital Type");
		//Vaadin8-setImmediate() netWorkHospitalType.setImmediate(true);
		netWorkHospitalType.setWidth("250px");
		netWorkHospitalType.setHeight("20px");
		netWorkHospitalType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(netWorkHospitalType);
				
		hospitalCode = new TextField("Hospital Code");
		//Vaadin8-setImmediate() hospitalCode.setImmediate(true);
		hospitalCode.setWidth("300px");
		hospitalCode.setHeight("20px");
		hospitalCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(hospitalCode);
		
		hospitalStatus = new TextField("Hospital Status");
		hospitalStatus.setWidth("300px");
		hospitalStatus.setHeight("20px");
		hospitalStatus.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(hospitalStatus);
		
		rawRremarks = new TextArea("Remarks");
		rawRremarks.setWidth("300px");
		rawRremarks.setHeight("40px");
		rawRremarks.addStyleName("v-textarea");
		rawRremarks.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		if(null != newIntimationDto && null != newIntimationDto.getHospitalDto() /*&& null != newIntimationDto.getHospitalDto().getHospitalStatus()*/ &&
				null != newIntimationDto.getHospitalDto().getReInstatedBy()) {
			if(newIntimationDto.getHospitalDto().getReInstatedBy().equalsIgnoreCase(SHAConstants.HOSPITAL_SUSPENDED_BY_RAW) /*&&
					newIntimationDto.getHospitalDto().getHospitalStatus().equalsIgnoreCase(SHAConstants.HOSPITAL_DEEMPANNELLED)*/) {
			leftForm.addComponent(rawRremarks);
			}
		}
		
				
		hospitalName = new TextField("Hospital Name");
		//Vaadin8-setImmediate() hospitalName.setImmediate(true);
		hospitalName.setWidth("300px");
		hospitalName.setHeight("20px");
		hospitalName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(hospitalName);
				
		hospitalCity = new TextField("Hospital City");
		//Vaadin8-setImmediate() hospitalCity.setImmediate(true);
		hospitalCity.setWidth("300px");
		hospitalCity.setHeight("20px");
		hospitalCity.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(hospitalCity);
				
		hospitalIrdaCode = new TextField("Hospital IRDA Code");
		//Vaadin8-setImmediate() hospitalIrdaCode.setImmediate(true);
		hospitalIrdaCode.setWidth("300px");
		hospitalIrdaCode.setHeight("20px");
		hospitalIrdaCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		leftForm.addComponent(hospitalIrdaCode);
				
		if(null != newIntimationDto && null != newIntimationDto.getPolicy() && null != newIntimationDto.getPolicy().getProduct() &&
		 null != newIntimationDto.getPolicy().getProduct().getCode() && 
				newIntimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076) ||
				null != newIntimationDto && null != newIntimationDto.getPolicy() && null != newIntimationDto.getPolicy().getProduct() &&
				 null != newIntimationDto.getPolicy().getProduct().getCode() && 
						newIntimationDto.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
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
		leftForm.addComponent(originalSI);
				
//		Second Form
		preauthAppAmt = new TextField("Cashless Approval Amt");
		//Vaadin8-setImmediate() preauthAppAmt.setImmediate(true);
		preauthAppAmt.setWidth("-1px");
		preauthAppAmt.setHeight("20px");
		preauthAppAmt.setReadOnly(true);
		preauthAppAmt.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(preauthAppAmt);
		
		balanceSI = new TextField("Balance SI");
		//Vaadin8-setImmediate() balanceSI.setImmediate(true);
		balanceSI.setWidth("300px");
		balanceSI.setHeight("20px");
		balanceSI.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(balanceSI);
				
		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("180px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(policyNumber);
				
		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("700px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(productName);
				
		policyYear = new TextField("Policy Year");
		//Vaadin8-setImmediate() policyYear.setImmediate(true);
		policyYear.setWidth("250px");
		policyYear.setHeight("20px");
		policyYear.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);		
		rightForm.addComponent(policyYear);
				
		policyPeriod = new TextField("Policy Period");
		//Vaadin8-setImmediate() policyPeriod.setImmediate(true);
		policyPeriod.setWidth("-1px");
		policyPeriod.setHeight("20px");
		policyPeriod.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(policyPeriod);

		txtPolicyAgeing = new TextField("Policy Ageing");
		//Vaadin8-setImmediate() txtPolicyAgeing.setImmediate(true);
		txtPolicyAgeing.setWidth("180px");
		txtPolicyAgeing.setHeight("20px");
		txtPolicyAgeing.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//		rightForm.addComponent(txtPolicyAgeing);
				
		policyType = new TextField("Policy Type");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(policyType);
				
		policyStartDate = new TextField("Policy Inception Date");
		//Vaadin8-setImmediate() policyStartDate.setImmediate(true);
		policyStartDate.setWidth("-1px");
		policyStartDate.setHeight("20px");
		policyStartDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(policyStartDate);
		
		txtEffectiveFromDate = new TextField("Effective Start Date");
		//Vaadin8-setImmediate() policyStartDate.setImmediate(true);
		txtEffectiveFromDate.setWidth("-1px");
		txtEffectiveFromDate.setHeight("20px");
		txtEffectiveFromDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(txtEffectiveFromDate);
		
		txtEffectiveToDate = new TextField("Effective End Date");
		//Vaadin8-setImmediate() policyStartDate.setImmediate(true);
		txtEffectiveToDate.setWidth("-1px");
		txtEffectiveToDate.setHeight("20px");
		txtEffectiveToDate.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(txtEffectiveToDate);
				
		productType = new TextField("Product Type");
		//Vaadin8-setImmediate() productType.setImmediate(true);
		productType.setWidth("250px");
		productType.setHeight("20px");
		productType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(productType);
				
		policyPlan = new TextField("Plan");
		//Vaadin8-setImmediate() policyPlan.setImmediate(true);
		policyPlan.setWidth("300px");
		policyPlan.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);		
		rightForm.addComponent(policyPlan);
				
		classOfBusiness = new TextField("Class Of Business");
		//Vaadin8-setImmediate() classOfBusiness.setImmediate(true);
		classOfBusiness.setWidth("250px");
		classOfBusiness.setHeight("20px");
		classOfBusiness.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);		
		rightForm.addComponent(classOfBusiness);
				
		customerId = new TextField("Customer Id");
		//Vaadin8-setImmediate() customerId.setImmediate(true);
		customerId.setWidth("-1px");
		customerId.setHeight("20px");
		customerId.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(customerId);
				
		cpuCode = new TextField("CPU Code");
		//Vaadin8-setImmediate() cpuCode.setImmediate(true);
		cpuCode.setWidth("-1px");
		cpuCode.setHeight("20px");
		cpuCode.setReadOnly(true);
		cpuCode.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(cpuCode);
				
		portabilityPolicy = new TextField("Portability Policy");
		//Vaadin8-setImmediate() portabilityPolicy.setImmediate(true);
		portabilityPolicy.setWidth("300px");
		portabilityPolicy.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(portabilityPolicy);
				
		srCitizenClaim = new TextField("Sr Citizen Claim");
		//Vaadin8-setImmediate() srCitizenClaim.setImmediate(true);
		srCitizenClaim.setWidth("-1px");
		srCitizenClaim.setHeight("20px");
		srCitizenClaim.setReadOnly(true);
		srCitizenClaim.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(srCitizenClaim);
		
		noOfDaysPerYear = new TextField("Maximum Number of Days Per Year");
		//Vaadin8-setImmediate() gmcCompanyName.setImmediate(true);
		noOfDaysPerYear.setWidth("200px");
		noOfDaysPerYear.setHeight("20px");
		noOfDaysPerYear.setReadOnly(true);
		noOfDaysPerYear.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(noOfDaysPerYear);
		
		policyTerm = new TextField("Policy Term");
		//Vaadin8-setImmediate() cashlessApprovalAmt.setImmediate(true);
		policyTerm.setWidth("300px");
		policyTerm.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(policyTerm);
		
		
		numberOfBeds = new TextField("Number Of Beds");
		numberOfBeds.setWidth("300px");
		numberOfBeds.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		rightForm.addComponent(numberOfBeds);
				
		
		formsHorizontalLayout = new HorizontalLayout();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.addComponents(leftForm,rightForm);
		formsHorizontalLayout.setHeight("100%");
     
		return formsHorizontalLayout;
	}

	private void setAddtionalValuesforCarousel(NewIntimationDto newIntimationDTO) {
		
		diagnosis.setReadOnly(false);
		diagnosis.setValue(newIntimationDTO.getDiagnosis() != null && !newIntimationDTO.getDiagnosis().contains("null") ? newIntimationDTO.getDiagnosis() : "");
		diagnosis.setReadOnly(true);
		
		if (null != newIntimationDTO.getCpuCode()) {
			cpuCode.setReadOnly(false);
			cpuCode.setValue(newIntimationDTO.getCpuCode());
			cpuCode.setReadOnly(true);
		}
		if (null != newIntimationDTO.getPolicy()) {
			String strCustId = String.valueOf(newIntimationDTO
					.getInsuredPatient().getInsuredId());
			customerId.setValue(strCustId);
			policyNumber.setValue(newIntimationDTO.getPolicy()
					.getPolicyNumber());
			insuredName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());

			insuredDOB.setReadOnly(false);
			insuredAge.setReadOnly(false);
			noOfDaysPerYear.setReadOnly(false);
			policyTerm.setReadOnly(false);
			
			if((ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) ) {
				if(newIntimationDTO.getPaPatientDOB() != null &&  newIntimationDTO.getPaPatientDOB().toString()!=null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
					insuredDOB.setValue(date);
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null){
					String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
				}
				if(newIntimationDTO.getPaPatientAge() != null) {
					insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
				}
				
			}
			//added for new product076
			if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				  null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
				  || null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				  null != newIntimationDTO.getPolicy().getProduct().getCode() && newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.GROUP_HOSPITAL_CASH_POLICY)){
				if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() !=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
				insuredDOB.setValue(date);
				if(newIntimationDTO.getPolicy().getPhcBenefitDays() != null &&
						!newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)){
				noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
				}
				else if(newIntimationDTO.getInsuredPatient().getHcpDays() != null && 
						 newIntimationDTO.getPolicy().getProduct().getCode().equalsIgnoreCase(SHAConstants.PRODUCT_CODE_076)
						 && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)){
					 noOfDaysPerYear.setValue(newIntimationDTO.getInsuredPatient().getHcpDays().toString());
				 }
				if(newIntimationDTO.getPolicy().getPolicyTerm() != null){
				policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
				}
			}
			}
			if((ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))) {
				if(newIntimationDTO.getPaPatientName() != null) {
					insuredPatientName.setReadOnly(false);
					insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
					insuredPatientName.setReadOnly(true);
				}
			}
			insuredDOB.setReadOnly(true);
			insuredAge.setReadOnly(true);
			noOfDaysPerYear.setReadOnly(true);
			policyTerm.setReadOnly(true);
		}
		if (null != newIntimationDTO.getHospitalDto()) {
			String hosp = newIntimationDTO.getHospitalDto().getName() != null ? newIntimationDTO
					.getHospitalDto().getName() : "";
					if(newIntimationDTO.getHospitalDto().getFspFlag() !=null && newIntimationDTO.getHospitalDto().getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
						hospitalName.setValue(StringUtils.trim(hosp)+"(VSP)");
					}
					else{
					hospitalName.setValue(StringUtils.trim(hosp));
					}
			
			//GLX2020047
			if(newIntimationDTO.getHospitalDto().getMedibuddyFlag() != null && newIntimationDTO.getHospitalDto().getMedibuddyFlag().equals(1)){
				hospitalName.setStyleName("yellow");
			}
			
			String City = newIntimationDTO.getHospitalDto().getCity();
			hospitalCity.setValue(City != null ? City : "");
			hospitalIrdaCode.setValue(newIntimationDTO.getHospitalDto().getHospitalIrdaCode());
			hospitalIrdaCode.setNullRepresentation("");
		}
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getHospitalCode()) {
			hospitalCode.setValue(newIntimationDTO.getHospitalDto().getHospitalCode());
			hospitalCode.setNullRepresentation("");
		}
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getHospitalStatus()) {
			hospitalStatus.setValue(newIntimationDTO.getHospitalDto().getHospitalStatus());
			hospitalStatus.setNullRepresentation("");
		}
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getRemarks()) {
			rawRremarks.setValue(newIntimationDTO.getHospitalDto().getRemarks());
			rawRremarks.setNullRepresentation("");
		}
		if (null != newIntimationDTO.getHospitalType()) {
			hospitalTypeName.setValue(newIntimationDTO.getHospitalDto().getHospitalType()
					.getValue());
		}
		if (null != newIntimationDTO.getPolicy().getProduct()) {
			productName.setValue(newIntimationDTO.getPolicy().getProduct()
					.getValue() + " - " + newIntimationDTO.getPolicy().getProduct().getCode());
			productName.setWidth("700px");
		}
		if (null != newIntimationDTO.getPolicy().getProductType()) {
			productType.setValue(newIntimationDTO.getPolicy().getProductType()
					.getValue());
		}
		
		//added for support ticket IMSSUPPOR-32501
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
						|| ReferenceTable.STAR_GRP_COVID_PROD_CODE.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) )
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

		if(SHAConstants.PRODUCT_CODE_076.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode())){
			Insured insuredPlan = newIntimationDTO.getInsuredPatient();
			String planType = insuredPlan.getPlan();
			if(!planType.isEmpty()){
				policyPlan.setValue(planType);
			}
		}

		if(((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) ||
				SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()))
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
		
		if(ReferenceTable.HOSPITAL_CASH_POLICY.equalsIgnoreCase(newIntimationDTO.getPolicy().getProduct().getCode()) &&  newIntimationDTO.getInsuredPatient().getPlan() != null 
				&& !newIntimationDTO.getInsuredPatient().getPlan().isEmpty()){
			if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_B)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_BASIC_PLAN);
			}else if(newIntimationDTO.getInsuredPatient().getPlan().equalsIgnoreCase(SHAConstants.STAR_HOSP_CSH_PLAN_E)){
				policyPlan.setValue(SHAConstants.HOSP_CASH_POLICY_ENHN_PLAN);
			}
	    }
		
		if(null != newIntimationDTO.getPolicy().getPolicyYear()){

			policyYear.setValue(String.valueOf(newIntimationDTO.getPolicy().getPolicyYear()));
			
		}
		
			
			if(newIntimationDTO.getPolicyInceptionDate() != null ){
				//String policyDate = formatDate;
				policyStartDate.setReadOnly(false);
				policyStartDate.setValue(SHAUtils.formatDate(newIntimationDTO.getPolicyInceptionDate()));
				policyStartDate.setReadOnly(true);
			}
				
				String formatDate = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyFromDate());
				String policyToDate = SHAUtils.formatDate(newIntimationDTO.getPolicy().getPolicyToDate());
				
				if(policyToDate != null && formatDate != null){
					String periodOfPolicy = formatDate + " - " + policyToDate; 
					policyPeriod.setReadOnly(false);
					policyPeriod.setValue(periodOfPolicy);
					policyPeriod.setReadOnly(true);
				}			
		
		txtPolicyAgeing.setReadOnly(false);
		txtPolicyAgeing.setValue(newIntimationDTO.getPolicyYear() != null ? newIntimationDTO.getPolicyYear() : "");
		txtPolicyAgeing.setReadOnly(true);

		if (null != newIntimationDTO.getPolicy().getProductType()) {
			String classofBusiness = newIntimationDTO.getLineofBusiness() != null ? newIntimationDTO
					.getLineofBusiness() : "";
			classOfBusiness.setValue(classofBusiness);
		}
		
		insuredAge.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredPatientName.setReadOnly(false);
		noOfDaysPerYear.setReadOnly(false);
		policyTerm.setReadOnly(false);
		if(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()) && newIntimationDTO.getPaPatientDOB() != null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
			insuredDOB.setValue(date);
			}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
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
			if(newIntimationDTO.getPolicy().getPhcBenefitDays() != null){
			noOfDaysPerYear.setValue(newIntimationDTO.getPolicy().getPhcBenefitDays().toString());
			}
			if(newIntimationDTO.getPolicy().getPolicyTerm() != null){
			policyTerm.setValue(newIntimationDTO.getPolicy().getPolicyTerm().toString() + "  Year");
			}
		}
		}
		
		if (newIntimationDTO.getInsuredPatient() != null
				&& newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
			
			Integer insuredAgeVal =  newIntimationDTO.getInsuredPatient()
					.getInsuredAge().intValue();
			insuredAge.setReadOnly(false);
			insuredAge.setValue(insuredAgeVal.toString()+" years");
			insuredAge.setReadOnly(true);
		}else if((ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey())) && 
				newIntimationDTO.getPaPatientAge() != null) {
			insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString());
		}
		if(newIntimationDTO.getPaPatientName() != null) {
			insuredPatientName.setReadOnly(false);
			insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
			insuredPatientName.setReadOnly(true);
		}
		
		insuredAge.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredPatientName.setReadOnly(true);
		noOfDaysPerYear.setReadOnly(true);
		policyTerm.setReadOnly(true);

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
			} 
		}
		
		Hospitals hospCity = getHospCity(newIntimationDTO.getHospitalDto().getKey());

		if(hospCity!=null) {
			if(hospCity.getInpatientBeds() != null){
				numberOfBeds.setValue((hospCity.getInpatientBeds()).toString());
			}
			else{
				numberOfBeds.setValue("0"); 
			}
		}
		
		if(newIntimationDTO != null && newIntimationDTO.getPolicy() != null && newIntimationDTO.getPolicy().getProductType() != null && newIntimationDTO.getPolicy().getProductType().getKey()!= null && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.GROUP_POLICY) && newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getEffectiveFromDate() != null ){
			//String policyDate = formatDate;
			txtEffectiveFromDate.setReadOnly(false);
			txtEffectiveFromDate.setValue(SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getEffectiveFromDate()));
			txtEffectiveFromDate.setReadOnly(true);
		}
		
		if(newIntimationDTO != null && newIntimationDTO.getPolicy() != null && newIntimationDTO.getPolicy().getProductType() != null && newIntimationDTO.getPolicy().getProductType().getKey()!= null && newIntimationDTO.getPolicy().getProductType().getKey().equals(ReferenceTable.GROUP_POLICY) && newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getEffectiveToDate() != null ){
			//String policyDate = formatDate;
			txtEffectiveToDate.setReadOnly(false);
			txtEffectiveToDate.setValue(SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getEffectiveToDate()));
			txtEffectiveToDate.setReadOnly(true);
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
		numberOfBeds.setReadOnly(true);
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
