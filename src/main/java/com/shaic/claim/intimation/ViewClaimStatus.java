package com.shaic.claim.intimation;

import java.util.Date;
import java.util.Iterator;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.ClaimDto;
import com.shaic.domain.CashlessDetailsService;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.PreviousPolicyService;
import com.shaic.domain.ViewTmpClaim;
import com.shaic.domain.ViewTmpIntimation;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.Property.ReadOnlyException;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewClaimStatus extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Intimation Components

	private TextField txtIntimationNo;

	private TextField txtDateOfIntimation;

	private TextField txtPolicyNumber;

	private TextField txtPolicyIssuingOffice;

	private TextField txtProductName;

	private TextField txtInsuredName;

	private TextField txtPatientName;

	private TextField txtHospitalName;

	private TextField txtCityOfHospital;

	private TextField txtNetworkOrNonNetwork;

	private TextField txtDateOfAdmission;

	private TextField txtFieldVisitDoctorName;

	private TextField txtReasonForAdmission;

	private TextField txtCPUCode;

	private TextField txtSMCode;

	private TextField txtSMName;

	private TextField txtAgentOrBrokerCode;

	private TextField txtAgentOrBrokerName;

	private TextField txtHospitalCode;

	private TextField txtIDCardNo;

	// Registration Details Componenets

	/*private TextField txtClaimNo;

	private TextField txtRegistrationStatus;

	private TextField txtProvisionAmount;

	private TextField txtCashLessOrReimbersment;

	private TextField txtCPUCodeForRegistrationDetails;

	private TextField txtCloseRemarks;

	private TextField txtRemainder;

	private TextField txtRemainderDate;

	// Cashless Details Compoenets

	private TextField txtAilment;

	private TextField txtStatusOfCashLess;

	private TextField txtTotalAuthAmount;

	private BeanFieldGroup<NewIntimationDto> binderForIntimation;

	private BeanFieldGroup<ClaimDto> binderForClaim;

	private BeanFieldGroup<CashlessDetailsDto> binderForCashless;*/

	@EJB
	private HospitalService hospitalService;

	@EJB
	private IntimationService intimationService;

	@EJB
	private PreauthService preAuthService;

	@EJB
	private ClaimService claimService;

	@EJB
	private PreviousPolicyService previousPolicyService;

	@EJB
	private CashlessDetailsService cashlessDetailsService;

	/*@Inject
	private ClaimStatusDto claimStatusDto;*/

	private VerticalLayout mainLayout;

	public void init(String intimationId) {
		mainLayout = new VerticalLayout();
		Label intimationLabel = new Label("Status of Intimation No"
				+ intimationId);
		Panel labelPanel = new Panel();
		labelPanel.setContent(intimationLabel);
		FormLayout intimationFormLayout = getIntimationLayout();
		intimationFormLayout.setMargin(true);
		Panel intimationPanel = new Panel("Intimation Details");
		intimationPanel.setContent(intimationFormLayout);
		intimationPanel.setWidth("750px");
		final ViewTmpIntimation intimation = intimationService
				.searchbyIntimationNoFromViewIntimation(intimationId);
		/*Hospitals hospital = hospitalService.searchbyHospitalKey(intimation
				.getKey());*/
		ViewTmpClaim a_claim = claimService.getTmpClaimforIntimation(intimation.getKey());

		/*CashlessDetailsDto cashlessDetailsDto = cashlessDetailsService
				.getCashlessDetails(intimation.getKey());*/

		if (a_claim != null) {
			ClaimDto a_claimDto = claimService.claimToClaimDTO(a_claim);
			if (a_claimDto != null) {
				/*
				 * claimStatusDto.setClaimDto(a_claimDto);
				 * claimStatusDto.setNewIntimationDto(a_claimDto
				 * .getNewIntimationDto());
				 * claimStatusDto.setCashlessDetailsDto(cashlessDetailsDto);
				 */			
				
				setIntimationLayout(a_claimDto.getNewIntimationDto());
			}
		}
		mainLayout.addComponent(labelPanel);
		mainLayout.addComponent(intimationPanel);		
		mainLayout.setComponentAlignment(intimationPanel, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(labelPanel, Alignment.TOP_CENTER);
		mainLayout.setMargin(true);
		setReadOnly(intimationFormLayout);
		setCompositionRoot(mainLayout);
	}

	public FormLayout getIntimationLayout() {
		
		txtIntimationNo = new TextField("Intimation No");
		txtDateOfIntimation = new TextField("Date Of Intimation");
		txtPolicyNumber = new TextField("Policy-No/Adv Receipt No");
		txtPolicyIssuingOffice = new TextField("Policy Issuing Office");
		txtProductName = new TextField("Product Name");
		txtInsuredName = new TextField("Insured Name");
		txtPatientName = new TextField("Patient Name");
		txtHospitalName = new TextField("Hospital Name");
		txtCityOfHospital = new TextField("City of Hospital");
		txtNetworkOrNonNetwork = new TextField("Network / Non-Network");
		txtDateOfAdmission = new TextField("Date of Admission");
		txtFieldVisitDoctorName = new TextField("Field Visit Doctor Name");
		txtReasonForAdmission = new TextField("Reason For Admission");
		txtCPUCode = new TextField("CPU Code");
		txtSMCode = new TextField("SM Code");
		txtSMName = new TextField("SM Name");
		txtAgentOrBrokerCode = new TextField("Agent / Broker Code");
		txtAgentOrBrokerName = new TextField("Agent / Broker Name");
		txtHospitalCode = new TextField("Hospital Code");
		txtIDCardNo = new TextField("ID Card No.");
		FormLayout intimationLayout = new FormLayout(txtIntimationNo,
				txtDateOfIntimation, txtPolicyNumber, txtPolicyIssuingOffice,
				txtProductName, txtInsuredName, txtPatientName,
				txtHospitalName, txtCityOfHospital, txtNetworkOrNonNetwork,
				txtDateOfAdmission, txtFieldVisitDoctorName,
				txtReasonForAdmission, txtCPUCode, txtSMCode, txtSMName,
				txtAgentOrBrokerCode, txtAgentOrBrokerName, txtHospitalCode,
				txtIDCardNo);
		return intimationLayout;
	}

	public void setIntimationLayout(NewIntimationDto newIntimationDto) {
		if (newIntimationDto != null) {
			txtIntimationNo
					.setValue(newIntimationDto.getIntimationId() != null ? newIntimationDto
							.getIntimationId() : "");

			txtCPUCode
					.setValue(newIntimationDto.getCpuCode() != null ? newIntimationDto
							.getCpuCode() : "");

			txtReasonForAdmission.setValue(newIntimationDto
					.getReasonForAdmission() != null ? newIntimationDto
					.getReasonForAdmission() : "");

			if (newIntimationDto.getCreatedDate() != null) {
				try {
					Date tmpintimationDate = SHAUtils
							.formatTimestamp(newIntimationDto.getCreatedDate()
									.toString());
					txtDateOfIntimation.setValue(SHAUtils
							.formatDate(tmpintimationDate));
				} catch (ReadOnlyException e) {
					e.printStackTrace();
				}
			}

			if (newIntimationDto.getAdmissionDate() != null) {
				try {
					Date tmphospitalAdmissionDate = SHAUtils
							.formatTimestamp(newIntimationDto
									.getAdmissionDate().toString());
					txtDateOfAdmission.setValue(SHAUtils
							.formatDate(tmphospitalAdmissionDate));
				} catch (ReadOnlyException e) {
					e.printStackTrace();
				}
			}

			txtPolicyNumber.setValue(newIntimationDto.getPolicy()
					.getPolicyNumber());
			txtPolicyIssuingOffice
					.setValue((newIntimationDto.getPolicy() != null && newIntimationDto
							.getPolicy().getHomeOfficeCode() != null) ? newIntimationDto
							.getPolicy().getHomeOfficeCode() : "");

			if (newIntimationDto.getInsuredPatient() != null) {
				txtPatientName.setValue(newIntimationDto.getInsuredPatient()
						.getInsuredName() != null ? newIntimationDto
						.getInsuredPatient().getInsuredName() : "");
				txtIDCardNo.setValue(newIntimationDto.getInsuredPatient()
						.getHealthCardNumber() != null ? newIntimationDto
						.getInsuredPatient().getHealthCardNumber() : "");
			}

			if (newIntimationDto.getInsuredPatient() != null) {
				txtInsuredName.setValue(newIntimationDto.getInsuredPatient()
						.getInsuredName() != null ? newIntimationDto
						.getInsuredPatient().getInsuredName() : "");
			}

			if (newIntimationDto.getPolicy() != null
					&& newIntimationDto.getPolicy().getProduct() != null) {
				txtProductName.setValue(newIntimationDto.getPolicy()
						.getProductName() != null ? newIntimationDto
						.getPolicy().getProductName() : "");
				txtAgentOrBrokerCode.setValue(newIntimationDto.getPolicy()
						.getAgentCode() != null ? newIntimationDto.getPolicy()
						.getAgentCode() : "");
				txtAgentOrBrokerName.setValue(newIntimationDto.getPolicy()
						.getAgentName() != null ? newIntimationDto.getPolicy()
						.getAgentName() : "");
				txtSMCode
						.setValue(newIntimationDto.getPolicy().getSmCode() != null ? newIntimationDto
								.getPolicy().getSmCode() : "");
				txtSMName
						.setValue(newIntimationDto.getPolicy().getSmName() != null ? newIntimationDto
								.getPolicy().getSmName() : "");
			}

			if (newIntimationDto.getHospitalDto() != null) {
				txtHospitalName.setValue(newIntimationDto.getHospitalDto()
						.getName() != null ? newIntimationDto.getHospitalDto()
						.getName() : "");
				txtCityOfHospital
						.setValue(newIntimationDto.getHospitalDto() != null ? newIntimationDto
								.getHospitalDto().getCity() : "");
				txtNetworkOrNonNetwork
						.setValue(newIntimationDto.getHospitalDto()
								.getHospitalType() != null ? newIntimationDto
								.getHospitalDto().getHospitalType().getValue()
								: "");

				txtHospitalCode.setValue(newIntimationDto.getHospitalDto()
						.getHospitalCode() != null ? newIntimationDto
						.getHospitalDto().getHospitalCode() : "");
			}

		}
	}

	private void setReadOnly(FormLayout layout) {
		Iterator<Component> i = layout.iterator();
		while (i.hasNext()) {
			Component c = i.next();
			if (c instanceof TextField) {
				TextField field = (TextField) c;
				field.setNullRepresentation("");
				field.setReadOnly(true);
				field.setWidth("500px");
				field.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
			}
		}
	}

}
