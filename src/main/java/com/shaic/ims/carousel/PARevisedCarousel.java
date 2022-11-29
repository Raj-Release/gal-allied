package com.shaic.ims.carousel;

import java.util.List;
import java.util.Map;

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
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.InsuredCover;
import com.shaic.domain.InsuredService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.paclaim.reimbursement.service.PAReimbursementService;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PARevisedCarousel extends ViewComponent {
	private static final long serialVersionUID = 5892717759896227659L;

	@EJB
	private DBCalculationService dbCalculationService;
	
	@EJB
	private PAReimbursementService reimbursementService;
	
	@EJB
	private InsuredService insuredService;
	
	private TextField intimationId;
	private TextField policyNumber;
	private TextField productName;
	private TextField txtPolicyAgeing;
	private TextField policyYear;
	
	private TextField insuredPatientName;
	
	//for CR2019085
	private TextField insuredDOB;
	
	private TextField insuredAge;
	//private TextField diagnosis;
	private TextField claimTypeField;
	private TextField dateOfAdmission;
	private TextField txtDateOfDischarge;
	
	private TextField policyType;
	
	//private TextField hospitalTypeName;
	private TextField hospitalName;
	private TextField hospitalCode;
	private TextField hospitalCity;
	private TextField hospitalIrdaCode;
	
	//private TextField originalSI;
	private TextField insuredName;
	private TextField cpuCode;
	private TextField claimId;
	private TextField cashlessApprovalAmt;

	//private TextField policyType;
	//private TextField customerId;
	//private TextField srCitizenClaim;
	//private TextField productType;
	//private TextField classOfBusiness;
	
//	private TextField admissionDateForCarousel;
	
	//private TextField portabilityPolicy;
	
	//private TextField netWorkHospitalType;
	
	private TextField policyPlan;
	

	HorizontalCarousel formsHorizontalLayout;

	HorizontalLayout layout1;
	HorizontalLayout layout2;
	HorizontalLayout layout3;

	FormLayout fifthForm;
	
	private TextField dateofaccd;
	private TextField dateofdeath;
	private TextField dateofDisablement;
	private TextArea listOfCovers;
	private TextField workPalceAccident;

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

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		txtDateOfDischarge.setReadOnly(false);
		txtDateOfDischarge.setValue(SHAUtils.formatDate(claimDTO
				.getDischargeDate()));
		txtDateOfDischarge.setReadOnly(true);
		
		policyType.setReadOnly(false);
		if(claimDTO.getIncidenceFlagValue() != null) {
		policyType
		.setValue(claimDTO.getIncidenceFlagValue().equalsIgnoreCase("A") ? "Accident":"Death");
		} else {
		policyType.setValue("-"); 
		}
		policyType.setReadOnly(true);
		
		//dateofaccdordeath.setReadOnly(false);
		/*if(claimDTO.getIncidenceDate() !=null) {
		dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getIncidenceDate()));
		}
		else {
			dateofaccdordeath.setValue("-");
				}*/
		
		/*if(null != policyType && null != policyType.getValue()) {
			
			if((SHAConstants.ACCIDENT).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
			}
			else if((SHAConstants.DEATH).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
			}
			else {
				dateofaccdordeath.setValue("-");
					}
		}
		dateofaccdordeath.setReadOnly(true);*/
		dateofaccd.setReadOnly(false);
		dateofdeath.setReadOnly(false);
		dateofDisablement.setReadOnly(false);
		
		if(claimDTO.getAccidentDate() != null){
			dateofaccd.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
		}else{
			dateofaccd.setValue("-");
		}
		
		if(claimDTO.getDeathDate() != null){
			dateofdeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
		}else{
			dateofdeath.setValue("-");
		}
		
		if(claimDTO.getDisablementDate() != null){
			dateofDisablement.setValue(SHAUtils.formatDate(claimDTO.getDisablementDate()));
		}else{
			dateofDisablement.setValue("-");
		}
		
		dateofaccd.setReadOnly(true);
		dateofdeath.setReadOnly(true);
		dateofDisablement.setReadOnly(true);
		listOfCovers.setReadOnly(false);
		workPalceAccident.setReadOnly(false);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			//added for jira IMSSUPPOR-28590
			if(newIntimationDTO.getPaPatientName() != null && !newIntimationDTO.getPaPatientName().isEmpty()){
			insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
			}
			else{
				insuredPatientName.setValue((newIntimationDTO.getInsuredPatientName() != null) ? newIntimationDTO.getInsuredPatientName() : "");
			}
			if(newIntimationDTO.getPaPatientAge() != null) {
				
				insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString() +" Years");
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge() != null){
					Long age = Double.valueOf(newIntimationDTO.getInsuredPatient().getInsuredAge()).longValue();
					insuredAge.setValue(age.toString() + " Years");
				}
			if(newIntimationDTO.getPaPatientDOB() != null &&  newIntimationDTO.getPaPatientDOB().toString()!=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				insuredDOB.setValue(date);
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
				}
		}
		insuredPatientName.setReadOnly(true);

		//for CR2019085
		insuredDOB.setReadOnly(false);
		if( newIntimationDTO.getInsuredPatient()!=null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date); 
			}
		insuredDOB.setReadOnly(true);
		
		
		claimTypeField.setReadOnly(false);
		// claimType.setValue(StringUtils.equalsIgnoreCase("Network",
		// newIntimationDTO.getHospitalDto().getHospitalType().getValue()) ?
		// "Cashless" : "Reimbursement");


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
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
		BeanItem<NewIntimationDto> item = new BeanItem<NewIntimationDto>(
				newIntimationDTO);
		FieldGroup binder = new FieldGroup(item);
		
		Reimbursement reimbursement = reimbursementService.getReimbursementByKey(claimDTO.getRodKeyForDischargeDate());
		
		binder.bindMemberFields(this);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(claimDTO.getAdmissionDate() != null ? SHAUtils.formatDate(claimDTO
				.getAdmissionDate()) : SHAUtils.formatDate(newIntimationDTO
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		txtDateOfDischarge.setReadOnly(false);
		if(null != reimbursement && reimbursement.getDateOfDischarge() != null){
			txtDateOfDischarge.setValue(SHAUtils.formatDate(reimbursement.getDateOfDischarge()));	
		}
		txtDateOfDischarge.setReadOnly(true);
		
		policyType.setReadOnly(false);
		if(claimDTO.getIncidenceFlagValue() != null) {
			policyType
			.setValue(claimDTO.getIncidenceFlagValue().equalsIgnoreCase("A") ? "Accident":"Death");
			} else {
			policyType.setValue("-"); 
			}
		policyType.setReadOnly(true);
		
		//dateofaccdordeath.setReadOnly(false);
		dateofaccd.setReadOnly(false);
		dateofdeath.setReadOnly(false);
		dateofDisablement.setReadOnly(false);
		/*if(claimDTO.getIncidenceDate() !=null) {
			dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getIncidenceDate()));
			} else {
			dateofaccdordeath.setValue("-");
				}*/
		/*if(null != policyType && null != policyType.getValue()) {
			
			if((SHAConstants.ACCIDENT).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
			}
			else if((SHAConstants.DEATH).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
			}
			else {
				dateofaccdordeath.setValue("-");
					}
		}*/
		
		if(claimDTO.getAccidentDate() != null){
			dateofaccd.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
		}else{
			dateofaccd.setValue("-");
		}
		dateofaccd.setReadOnly(true);
		
		if(claimDTO.getDeathDate() != null){
			dateofdeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
		}else{
			dateofdeath.setValue("-");
		}
		dateofdeath.setReadOnly(true);
		
		if(claimDTO.getDisablementDate() != null){
			dateofDisablement.setValue(SHAUtils.formatDate(claimDTO.getDisablementDate()));
		}else{
			dateofDisablement.setValue("-");
		}
		dateofDisablement.setReadOnly(true);
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		insuredDOB.setReadOnly(false);
		if(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()) && 
		newIntimationDTO.getPaPatientDOB() != null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
			insuredDOB.setValue(date);
		}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date);
		}
		
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			   //added for jira IMSSUPPOR-28590
			   if(newIntimationDTO.getPaPatientName() != null && !newIntimationDTO.getPaPatientName().isEmpty()){
				insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
				}
				else{
				insuredPatientName.setValue((newIntimationDTO.getInsuredPatientName() != null) ? newIntimationDTO.getInsuredPatientName() : "");
				}
			   if(newIntimationDTO.getPaPatientAge() != null) {
					insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString() +" Years");
					}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
						Long age = Double.valueOf(newIntimationDTO.getInsuredPatient().getInsuredAge()).longValue();
						insuredAge.setValue(age.toString() + " Years");
					}
			   if(newIntimationDTO.getPaPatientDOB() != null &&  newIntimationDTO.getPaPatientDOB().toString()!=null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				   insuredDOB.setValue(date);
					}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
						String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
						insuredDOB.setValue(date);
					}
		}
		insuredPatientName.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredAge.setReadOnly(true);
		claimTypeField.setReadOnly(false);
		// claimType.setValue(StringUtils.equalsIgnoreCase("Network",
		// newIntimationDTO.getHospitalDto().getHospitalType().getValue()) ?
		// "Cashless" : "Reimbursement");


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

		listOfCovers.setReadOnly(false);

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
	
	public void init(PreauthDTO preauthDto,
			String caption) {
		
		
		NewIntimationDto newIntimationDTO =preauthDto.getNewIntimationDTO();
		ClaimDto claimDTO =preauthDto.getClaimDTO();

		Panel panel = new Panel(caption);
//		panel.setHeight("140px");
		panel.setStyleName("policyinfogrid");
		//Vaadin8-setImmediate() panel.setImmediate(true);
		panel.setContent(buildCarousel());
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
		
		policyType.setReadOnly(false);
		if(newIntimationDTO.getIncidenceFlag() != null) {
			policyType
			.setValue(newIntimationDTO.getIncidenceFlag().equalsIgnoreCase("A") ? "Accident":"Death");
			} else {
			policyType.setValue("-"); 
			}
		policyType.setReadOnly(true);
		
		//dateofaccdordeath.setReadOnly(false);
		dateofaccd.setReadOnly(false);
		dateofdeath.setReadOnly(false);
		dateofDisablement.setReadOnly(false);
		/*if(claimDTO.getIncidenceDate() !=null) {
			dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getIncidenceDate()));
			} else {
		dateofaccdordeath.setValue("-");
			}*/
		
		/*if(null != policyType && null != policyType.getValue()) {
			
			if((SHAConstants.ACCIDENT).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
			}
			else if((SHAConstants.DEATH).equalsIgnoreCase(policyType.getValue()))
			{
				dateofaccdordeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
			}
			else {
				dateofaccdordeath.setValue("-");
					}
		}*/
		if(claimDTO.getAccidentDate() != null){
			dateofaccd.setValue(SHAUtils.formatDate(claimDTO.getAccidentDate()));
		}else{
			dateofaccd.setValue("-");
		}
		dateofaccd.setReadOnly(true);
		
		if(claimDTO.getDeathDate() != null){
			dateofdeath.setValue(SHAUtils.formatDate(claimDTO.getDeathDate()));
		}else{
			dateofdeath.setValue("-");
		}
		dateofdeath.setReadOnly(true);
		
		if(claimDTO.getDisablementDate() != null){
			dateofDisablement.setValue(SHAUtils.formatDate(claimDTO.getDisablementDate()));
		}else{
			dateofDisablement.setValue("-");
		}
		dateofDisablement.setReadOnly(true);
		
		
		cashlessApprovalAmt.setReadOnly(false);
		if(null != claimDTO.getCashlessAppAmt()) {
			cashlessApprovalAmt.setValue(String.valueOf(claimDTO.getCashlessAppAmt().intValue()));
		}
		cashlessApprovalAmt.setReadOnly(true);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(newIntimationDTO.getInsuredPatientName());
		if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
				null != newIntimationDTO.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
			//added for jira IMSSUPPOR-28590
			if(newIntimationDTO.getPaPatientName() != null && !newIntimationDTO.getPaPatientName().isEmpty()){
				insuredPatientName.setValue(newIntimationDTO.getPaPatientName());
				}
			else{
				insuredPatientName.setValue((newIntimationDTO.getInsuredPatientName() != null) ? newIntimationDTO.getInsuredPatientName() : "");
				}
			if(newIntimationDTO.getPaPatientAge() != null) {
				insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString() +" Years");
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
					Long age = Double.valueOf(newIntimationDTO.getInsuredPatient().getInsuredAge()).longValue();
					insuredAge.setValue(age.toString() + " Years");
				}
			if(newIntimationDTO.getPaPatientDOB() != null &&  newIntimationDTO.getPaPatientDOB().toString()!=null) {
				String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				insuredDOB.setValue(date);
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
				}
		}
		insuredDOB.setReadOnly(true);
		insuredAge.setReadOnly(true);
		insuredPatientName.setReadOnly(true);
		
	
		if(newIntimationDTO.getPaPatientDOB() != null) {
			insuredDOB.setReadOnly(false);
		String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
			insuredDOB.setValue(date); 
		}
		if( newIntimationDTO.getInsuredPatient()!=null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
			insuredDOB.setReadOnly(false);
			String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date); 
		}
		
		insuredDOB.setReadOnly(true);
		claimTypeField.setReadOnly(false);
		// claimType.setValue(StringUtils.equalsIgnoreCase("Network",
		// newIntimationDTO.getHospitalDto().getHospitalType().getValue()) ?
		// "Cashless" : "Reimbursement");


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

		
		
		String diagnosisName= "";
		if(preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList() != null && !preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList().isEmpty()){
			List<DiagnosisDetailsTableDTO> diagnosisTableList = preauthDto.getPreauthDataExtractionDetails().getDiagnosisTableList();
			if(null != diagnosisTableList && !diagnosisTableList.isEmpty())
			{
				for (DiagnosisDetailsTableDTO diagnosisDetailsTableDTO : diagnosisTableList) {
					if(null != diagnosisDetailsTableDTO.getDiagnosisName() && null != diagnosisDetailsTableDTO.getDiagnosisName().getValue()){
						diagnosisName+=diagnosisDetailsTableDTO.getDiagnosisName().getValue()+",";
					}
				}
			}
		}
		listOfCovers.setReadOnly(false);
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
		listOfCovers.setReadOnly(false);
		setAddtionalValuesforCarousel(intimationDto);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		policyType.setReadOnly(false);
		if(intimationDto.getIncidenceFlag() != null) {
			policyType
			.setValue(intimationDto.getIncidenceFlag().equalsIgnoreCase("A") ? "Accident":"Death");
			} else {
			policyType.setValue("-"); 
			}
		policyType.setReadOnly(true);
		
		/*dateofaccdordeath.setReadOnly(false);
		if(intimationDto.getAccidentDeathDate() !=null) {
			dateofaccdordeath.setValue(SHAUtils.formatDate(intimationDto.getAccidentDeathDate()));
			} else {
		dateofaccdordeath.setValue("-");
			}*/
		dateofaccd.setReadOnly(false);
		if(intimationDto.getAdmissionDate() !=null) {
			dateofaccd.setValue(SHAUtils.formatDate(intimationDto.getAdmissionDate()));
			} else {
				dateofaccd.setValue("-");
			}
		dateofaccd.setReadOnly(true);
		
		dateofdeath.setReadOnly(false);
		if(intimationDto.getAdmissionDate() !=null) {
			dateofdeath.setValue(SHAUtils.formatDate(intimationDto.getAdmissionDate()));
			} else {
				dateofdeath.setValue("-");
			}
		dateofdeath.setReadOnly(true);
		
		dateofDisablement.setReadOnly(true);

		insuredPatientName.setReadOnly(false);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		insuredPatientName.setValue(intimationDto.getInsuredPatientName());
		
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey()))){
			//added for jira IMSSUPPOR-28590
			if(intimationDto.getPaPatientName() != null && !intimationDto.getPaPatientName().isEmpty()){
				insuredPatientName.setValue(intimationDto.getPaPatientName());
				}
			else{
				insuredPatientName.setValue((intimationDto.getInsuredPatientName() != null) ? intimationDto.getInsuredPatientName() : "");
				}
			if(intimationDto.getPaPatientAge() != null) {
				insuredAge.setValue(intimationDto.getPaPatientAge().toString() +" Years");
				}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredAge() != null){
					Long age = Double.valueOf(intimationDto.getInsuredPatient().getInsuredAge()).longValue();
					insuredAge.setValue(age.toString() + " Years");
				}
			if(intimationDto.getPaPatientDOB() != null &&  intimationDto.getPaPatientDOB().toString()!=null) {
				String date = SHAUtils.formatDate(intimationDto.getPaPatientDOB());
				insuredDOB.setValue(date);
				}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() != null) {
					String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
				}
		}
		insuredPatientName.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredAge.setReadOnly(true);
		
		
		insuredDOB.setReadOnly(false);
		if( intimationDto.getInsuredPatient()!=null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() != null) {
			String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date); 
			}
		insuredDOB.setReadOnly(true);
		claimTypeField.setReadOnly(false);

		claimTypeField.setValue(StringUtils.equalsIgnoreCase("Network",
				intimationDto.getHospitalType().getValue()) ? "Cashless"
				: "Reimbursement");
		claimTypeField.setReadOnly(true);
		
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
		listOfCovers.setReadOnly(false);

		setAddtionalValuesforCarousel(intimationDto);

		dateOfAdmission.setReadOnly(false);
		dateOfAdmission.setValue(SHAUtils.formatDate(intimationDto
				.getAdmissionDate()));
		dateOfAdmission.setReadOnly(true);
		
		policyType.setReadOnly(false);
		if(intimationDto.getIncidenceFlag() != null) {
			policyType
			.setValue(intimationDto.getIncidenceFlag().equalsIgnoreCase("A") ? "Accident":"Death");
			} else {
			policyType.setValue("-"); 
			}
		policyType.setReadOnly(true);
		
		/*dateofaccdordeath.setReadOnly(false);
		if(intimationDto.getAccidentDeathDate() !=null) {
			dateofaccdordeath.setValue(SHAUtils.formatDate(intimationDto.getAccidentDeathDate()));
			} else {
		dateofaccdordeath.setValue("-");
			}*/
		dateofaccd.setReadOnly(false);
		if(intimationDto.getAdmissionDate() !=null) {
			dateofaccd.setValue(SHAUtils.formatDate(intimationDto.getAdmissionDate()));
			} else {
				dateofaccd.setValue("-");
			}
		dateofaccd.setReadOnly(true);
		
		dateofdeath.setReadOnly(false);
		if(intimationDto.getAdmissionDate() !=null) {
			dateofdeath.setValue(SHAUtils.formatDate(intimationDto.getAdmissionDate()));
			} else {
				dateofdeath.setValue("-");
			}
		dateofdeath.setReadOnly(true);

		dateofDisablement.setReadOnly(true);
		insuredDOB.setReadOnly(false);
		insuredAge.setReadOnly(false);
		insuredPatientName.setReadOnly(false);
		insuredPatientName.setValue(intimationDto.getInsuredPatientName());
		
		if(null != intimationDto && null != intimationDto.getPolicy() && null != intimationDto.getPolicy().getProduct() &&
				null != intimationDto.getPolicy().getProduct().getKey() && 
				(ReferenceTable.getGPAProducts().containsKey(intimationDto.getPolicy().getProduct().getKey()))){
			//added for jira IMSSUPPOR-28590
			if(intimationDto.getPaPatientName() != null && !intimationDto.getPaPatientName().isEmpty()){
				insuredPatientName.setValue(intimationDto.getPaPatientName());
				}
			else{
				insuredPatientName.setValue((intimationDto.getInsuredPatientName() != null) ? intimationDto.getInsuredPatientName() : "");
				}
			if(intimationDto.getPaPatientAge() != null) {
				insuredAge.setValue(intimationDto.getPaPatientAge().toString() +" Years");
				}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredAge() != null) {
					Long age = Double.valueOf(intimationDto.getInsuredPatient().getInsuredAge()).longValue();
					insuredAge.setValue(age.toString() + " Years");
				}
			if(intimationDto.getPaPatientDOB() != null &&  intimationDto.getPaPatientDOB().toString()!=null) {
				String date = SHAUtils.formatDate(intimationDto.getPaPatientDOB());
				insuredDOB.setValue(date);
				}else if(intimationDto.getInsuredPatient() != null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() != null) {
					String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setValue(date);
				}
		}
		insuredPatientName.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredAge.setReadOnly(true);
		
		insuredDOB.setReadOnly(false);
		if( intimationDto.getInsuredPatient()!=null && intimationDto.getInsuredPatient().getInsuredDateOfBirth() != null) {
			String date = SHAUtils.formatDate(intimationDto.getInsuredPatient().getInsuredDateOfBirth());
			insuredDOB.setValue(date); 
			}
		insuredDOB.setReadOnly(true);
		
		claimTypeField.setReadOnly(false);
		/*claimTypeField.setValue(StringUtils.equalsIgnoreCase("Network",
				intimationDto.getHospitalType().getValue()) ? "Cashless"
				: "Reimbursement");*/
		/**
		 * The above code is commented , since all these validations
		 * pertaining to health or PA is done while loading data in 
		 * new intimation dto itself. Hence this is not required.
		 * Pls refer IntimationService.getIntimationDto() method for the
		 * above changes.
		 * */
		if(null != intimationDto.getClaimType())
			claimTypeField.setValue(intimationDto.getClaimType().getValue());
		claimTypeField.setReadOnly(true);

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
		fifthForm = new FormLayout();
		fifthForm.setSpacing(false);
		fifthForm.setMargin(false);
		FormLayout sixthForm = new FormLayout();
		sixthForm.setSpacing(false);
		sixthForm.setMargin(false);

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


		insuredPatientName = new TextField("Insured Patient Name");
		//Vaadin8-setImmediate() insuredPatientName.setImmediate(true);
//		insuredPatientName.setWidth("300px");
		insuredPatientName.setHeight("20px");
		insuredPatientName.setReadOnly(true);
		insuredPatientName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		insuredDOB = new TextField("Insured Date Of Birth");
		insuredDOB.setHeight("20px");
		insuredDOB.setReadOnly(true);
		insuredDOB.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
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
		
		policyType = new TextField("Type Accident/ Death");
		//Vaadin8-setImmediate() policyType.setImmediate(true);
		policyType.setWidth("180px");
		policyType.setHeight("20px");
		policyType.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		/*dateofaccdordeath = new TextField("Date of Accident/Death");
		//Vaadin8-setImmediate() dateofaccdordeath.setImmediate(true);
		dateofaccdordeath.setWidth("180px");
		dateofaccdordeath.setHeight("20px");
		dateofaccdordeath.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);*/
		
		dateofaccd = new TextField("Date of Accident");
		//Vaadin8-setImmediate() dateofaccd.setImmediate(true);
		dateofaccd.setWidth("180px");
		dateofaccd.setHeight("20px");
		dateofaccd.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofdeath = new TextField("Date of Death");
		//Vaadin8-setImmediate() dateofdeath.setImmediate(true);
		dateofdeath.setWidth("180px");
		dateofdeath.setHeight("20px");
		dateofdeath.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		dateofDisablement = new TextField("Date of Disablement");
		//Vaadin8-setImmediate() dateofDisablement.setImmediate(true);
		dateofDisablement.setWidth("180px");
		dateofDisablement.setHeight("20px");
		dateofDisablement.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		policyNumber = new TextField("Policy No");
		//Vaadin8-setImmediate() policyNumber.setImmediate(true);
		policyNumber.setWidth("700px");
		policyNumber.setHeight("20px");
		policyNumber.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);

		
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


		productName = new TextField("Product Name");
		//Vaadin8-setImmediate() productName.setImmediate(true);
		productName.setWidth("700px");
		productName.setHeight("20px");
		productName.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);



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
		
		listOfCovers = new TextArea("Table");
		//Vaadin8-setImmediate() listOfCovers.setImmediate(true);
		listOfCovers.setWidth("500px");
		listOfCovers.setHeight("100px");
		listOfCovers.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		workPalceAccident = new TextField("Work Place Accident");
		//Vaadin8-setImmediate() workPalceAccident.setImmediate(true);
		policyPlan.setWidth("300px");
		workPalceAccident.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		
		formsHorizontalLayout = new HorizontalCarousel();
		formsHorizontalLayout.setWidth("100%");
		formsHorizontalLayout.setMouseDragEnabled(false);
		formsHorizontalLayout.setMouseWheelEnabled(false);
		formsHorizontalLayout.setTouchDragEnabled(false);
		// formsHorizontalLayout.setHeight(115, Unit.PIXELS);
		
		firstForm.addComponent(intimationId);
		firstForm.addComponent(policyNumber);
		firstForm.addComponent(productName);
		//GLX2020047
		firstForm.addComponent(hospitalName);
//		firstForm.addComponent(txtPolicyAgeing);
		firstForm.addComponent(policyYear);
		firstForm.addComponent(dateofaccd);
		
		secondForm.addComponent(insuredPatientName);
		secondForm.addComponent(insuredDOB);
		secondForm.addComponent(insuredAge);
//		
		secondForm.addComponent(claimTypeField);
		secondForm.addComponent(policyType);
		
		//GLX2020047
		//thirdForm.addComponent(hospitalName);
		thirdForm.addComponent(hospitalCode);
		thirdForm.addComponent(hospitalCity);
		thirdForm.addComponent(dateOfAdmission);
		thirdForm.addComponent(txtDateOfDischarge);
		
		fourthForm.addComponent(claimId);
		fourthForm.addComponent(cpuCode);
		fourthForm.addComponent(insuredName);
		fourthForm.addComponent(dateofdeath);
		fourthForm.addComponent(dateofDisablement);
		
		fifthForm.addComponent(listOfCovers);
	//	fifthForm.addComponent(workPalceAccident);
		
		sixthForm.addComponent(workPalceAccident);
		
//		sixthForm.addComponent(policyPlan);
//		sixthForm.addComponent(hospitalIrdaCode);
		
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
		formsHorizontalLayout.setHeight("150px");
     
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
			policyNumber.setValue(newIntimationDTO.getPolicy()
					.getPolicyNumber());
			insuredName.setValue(newIntimationDTO.getPolicy().getProposerFirstName());
		}
		if (null != newIntimationDTO.getHospitalDto()) {
			String hosp = newIntimationDTO.getHospitalDto().getName() != null ? newIntimationDTO
					.getHospitalDto().getName() : "";
			hospitalName.setValue(StringUtils.trim(hosp));
			//GLX2020047
			if(newIntimationDTO.getHospitalDto().getMedibuddyFlag() != null && newIntimationDTO.getHospitalDto().getMedibuddyFlag().equals(1)){
				hospitalName.setStyleName("yellow");
			}
			String City = newIntimationDTO.getHospitalDto().getCity();
			hospitalCity.setValue(City);
			hospitalIrdaCode.setValue(newIntimationDTO.getHospitalDto().getHospitalIrdaCode());
			hospitalIrdaCode.setNullRepresentation("");
		}
		if (null != newIntimationDTO.getHospitalType()) {
		}
		
		if(null != newIntimationDTO.getHospitalDto() && null != newIntimationDTO.getHospitalDto().getHospitalCode()) {
			hospitalCode.setValue(newIntimationDTO.getHospitalDto().getHospitalCode());
			hospitalCode.setNullRepresentation("");
		}
		
		if (null != newIntimationDTO.getPolicy().getProduct()) {
			productName.setValue(newIntimationDTO.getPolicy().getProduct()
					.getValue() + " - " + newIntimationDTO.getPolicy().getProduct().getCode());
		}
		if (null != newIntimationDTO.getPolicy().getProductType()) {
		}
		
		if(newIntimationDTO.getPolicy().getPolicyPlan() != null){
			policyPlan.setValue(newIntimationDTO.getPolicy().getPolicyPlan());
		}else{
			policyPlan.setValue("-");
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
		}
		else
		{
			if(null != newIntimationDTO && null != newIntimationDTO.getPolicy() && null != newIntimationDTO.getPolicy().getProduct() &&
					null != newIntimationDTO.getPolicy().getProduct().getKey() && 
					(ReferenceTable.getGPAProducts().containsKey(newIntimationDTO.getPolicy().getProduct().getKey()))){
				
				if (newIntimationDTO.getParentAge() != null) {
				insured = newIntimationDTO.getParentAge().intValue();
				}
			}
			if(newIntimationDTO.getPaPatientDOB() != null &&  newIntimationDTO.getPaPatientDOB().toString()!=null) {
				insuredDOB.setReadOnly(false);
				String date = SHAUtils.formatDate(newIntimationDTO.getPaPatientDOB());
				insuredDOB.setValue(date);
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth() != null) {
					String date = SHAUtils.formatDate(newIntimationDTO.getInsuredPatient().getInsuredDateOfBirth());
					insuredDOB.setReadOnly(false);
					insuredDOB.setValue(date);
				}
			if(newIntimationDTO.getPaPatientAge() != null) {
				insuredAge.setValue(newIntimationDTO.getPaPatientAge().toString() +" Years");
				}else if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getInsuredAge() != null) {
					insuredAge.setValue(newIntimationDTO.getInsuredPatient().getInsuredAge().toString()+" Years");
				}
//			insuredAge.setValue(insured.toString()+" years");
		}
		insuredAge.setReadOnly(true);
		insuredDOB.setReadOnly(true);
		insuredPatientName.setReadOnly(true);
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
			} else {
				if (layout2 != null && fifthForm != null) {
				}
			}

		}
		
		if(newIntimationDTO.getInsuredPatient() != null && newIntimationDTO.getInsuredPatient().getKey() != null){
			List<InsuredCover> coverList = insuredService.getInsuredCoverByInsuredKey(newIntimationDTO.getInsuredPatient().getKey());
			if(coverList != null && !coverList.isEmpty()){
				StringBuffer coverValues = new StringBuffer();
				for (InsuredCover insuredCover : coverList) {
					coverValues.append(insuredCover.getCoverCodeDescription()).append(",");
				}
				listOfCovers.setValue(coverValues.toString());
			}
		}
		
		if(null != newIntimationDTO && null != newIntimationDTO.getIntimationId()){
		Map<String, Object> isWorkPlaceAvailable = dbCalculationService.getWorkPlaceStatus(newIntimationDTO.getIntimationId());
		if(null != isWorkPlaceAvailable && null != isWorkPlaceAvailable.get(SHAConstants.WORKPLACE_STATUS)){
			if(SHAConstants.N_FLAG.equalsIgnoreCase(isWorkPlaceAvailable.get(SHAConstants.WORKPLACE_STATUS).toString())){
				workPalceAccident.setValue(SHAConstants.CAPS_NO);
			}
			else
			{
				workPalceAccident.setValue(SHAConstants.CAPS_YES);
			}
		}
		}
		
		claimTypeField.setReadOnly(true);
		hospitalName.setReadOnly(true);
		policyNumber.setReadOnly(true);
		intimationId.setReadOnly(true);
		insuredName.setReadOnly(true);
		productName.setReadOnly(true);
		txtPolicyAgeing.setReadOnly(true);
		policyYear.setReadOnly(true);
		insuredAge.setReadOnly(true);
		hospitalCode.setReadOnly(true);
		cashlessApprovalAmt.setReadOnly(true);
		listOfCovers.setReadOnly(true);
		workPalceAccident.setReadOnly(true);
	}
}
