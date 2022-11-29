package com.shaic.claim.intimation.create.dto;

import javax.ejb.EJB;

import com.shaic.arch.validation.ValidatorUtils;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;

public class DtoConverter {

	@EJB
	MasterService masterService;

	
	PolicyService policyService = new PolicyService();
	


	public static PolicyDto policyToPolicyDTO(Policy policy) {
		PolicyDto policyDto = new PolicyDto();
		policyDto.setPolicyNumber(!ValidatorUtils.isNull(policy
				.getPolicyNumber()) ? policy.getPolicyNumber() : "");
		//policyDto.setPolicySysId(policy.getPolicySysId());
		policyDto.setProduct(policy.getProduct());
		/*policyDto.setInsuredFirstName(policy.getInsuredFirstName());
		policyDto.setInsuredMiddleName(policy.getInsuredMiddleName());
		policyDto.setInsuredLastName(policy.getInsuredLastName());*/
		//policyDto.setInsuredSumInsured(policy.getInsuredSumInsured());
		//policyDto.setInsuredBalanceSi(policy.getInsuredBalanceSi());
		policyDto.setLobId(policy.getLobId());

		return policyDto;
	}

	/*public static NewIntimationDto intimationToIntimationDTO(
			Intimation intimation, Hospitals hospital) {
		SelectValue selectValue;
		if (intimation == null) {
			return null;
		}
		NewIntimationDto newIntimationDTO = new NewIntimationDto();

		newIntimationDTO.setKey(intimation.getKey());
		
		newIntimationDTO
		.setIntimationId(intimation.getIntimationId() != null ? intimation
				.getIntimationId() : "");
		
		newIntimationDTO.setDateOfIntimation(intimation.getCreatedDate()
				.toString() != null ? intimation.getCreatedDate().toString()
				: "");

		newIntimationDTO.setPolicyNumber(intimation.getPolicy()
				.getPolicyNumber() != null ? intimation.getPolicy()
				.getPolicyNumber() : "");
		
		newIntimationDTO.setPolicyIssueOffice(intimation.getPolicy()
				.getHomeOfficeName() != null ? intimation.getPolicy()
				.getHomeOfficeName() : "");
		
		
		
		newIntimationDTO.setProductName(intimation.getPolicy().getProduct()
				.getValue().toString() != null ? intimation.getPolicy()
				.getProduct().getValue().toString() : "");
		
		newIntimationDTO.setInsuredPatientName(intimation
				.getInsuredPatientName() != null ? intimation
				.getInsuredPatientName() : "");
		
		newIntimationDTO
				.setPatientName(intimation.getIntimatedBy().getValue() != null ? intimation
						.getIntimatedBy().getValue() : "");
		
		newIntimationDTO.setHospitalName(hospital.getName() != null ? hospital
				.getName() : "");
		
		newIntimationDTO.setHospitalCity(hospital.getCity() != null ? hospital
				.getCity() : "");
		
		newIntimationDTO.setHospitalNetwork(hospital.getHospitalType()
				.getValue() != null ? hospital.getHospitalType().getValue()
				: "");
		if(intimation.getAdmissionDate()!=null){
			newIntimationDTO.setAdmissionDate(intimation.getAdmissionDate());
		}
		
		newIntimationDTO
				.setReasonForAdmission(intimation.getAdmissionReason() != null ? intimation
						.getAdmissionReason() : "");
		
		newIntimationDTO
				.setCpuCode(intimation.getCpuCode().getCpuCode() != null ? intimation
						.getCpuCode().getCpuCode() : 0L);
		
		newIntimationDTO
				.setSmCode(intimation.getPolicy().getSmCode() != null ? intimation
						.getPolicy().getSmCode() : "");
		
		newIntimationDTO
				.setSmName(intimation.getPolicy().getSmName() != null ? intimation
						.getPolicy().getSmName() : "");
		
		newIntimationDTO.setAgentBrokerCode(intimation.getPolicy()
				.getAgentCode() != null ? intimation.getPolicy().getAgentCode()
				: "");
		
		newIntimationDTO.setAgentBrokerName(intimation.getPolicy()
				.getAgentName() != null ? intimation.getPolicy().getAgentName()
				: "");
		
		newIntimationDTO
				.setHospitalCode(hospital.getHospitalCode() != null ? hospital
						.getHospitalCode() : "");
		
		newIntimationDTO
				.setAdmissionDate(intimation.getAdmissionDate() != null ? intimation
						.getAdmissionDate() : null);
		newIntimationDTO
				.setReasonForAdmission(intimation.getAdmissionReason() != null ? intimation
						.getAdmissionReason() : "");
		newIntimationDTO.setCpuCode(intimation.getCpuCode().getCpuCode());
		newIntimationDTO.setCreatedDate(intimation.getCreatedDate());
		newIntimationDTO
				.setDoctorName(intimation.getDoctorName() != null ? intimation
						.getDoctorName() : "");
		newIntimationDTO
				.setComments(intimation.getHospitalComments() != null ? intimation
						.getHospitalComments() : "");
		newIntimationDTO.setInsuredPatientName(intimation
				.getInsuredPatientName());
		if (!ValidatorUtils.isNull(intimation.getIntimatedBy())) {
			selectValue = new SelectValue();
			selectValue.setId(intimation.getIntimatedBy().getKey());
			selectValue.setValue(intimation.getIntimatedBy().getValue());
			newIntimationDTO.setIntimatedBy(selectValue);
		}
		newIntimationDTO
				.setIntimaterName(intimation.getIntimaterName() != null ? intimation
						.getIntimaterName() : "");

		
		if (!ValidatorUtils.isNull(intimation.getIntimationMode())) {
			selectValue = new SelectValue();
			selectValue.setId(intimation.getIntimationMode().getKey());
			selectValue.setValue(intimation.getIntimationMode().getValue());
			newIntimationDTO.setModeOfIntimation(selectValue);
		}
		if (!ValidatorUtils.isNull(intimation.getLateintimationReason())) {
			selectValue = new SelectValue();
			selectValue.setId(intimation.getLateintimationReason().getKey());
			selectValue.setValue(intimation.getLateintimationReason()
					.getValue());
			newIntimationDTO.setLateIntimationReason(selectValue);
		}

		newIntimationDTO.setModifiedBy(intimation.getModifiedBy());
		newIntimationDTO.setModifiedDate(intimation.getModifiedDate());

		newIntimationDTO
				.setOfficeCode(intimation.getOfficeCode() != null ? intimation
						.getOfficeCode() : "");
		newIntimationDTO
				.setOfficeCode(intimation.getOfficeCode() != null ? intimation
						.getOfficeCode() : "----------");
		// intimationDto.setPatientNotCovered( intimation.getPatientNotCovered()
		// );
		if (!ValidatorUtils.isNull(intimation.getRoomCategory())) {
			selectValue = new SelectValue();
			selectValue.setId(intimation.getRoomCategory().getKey());
			selectValue.setValue(intimation.getRoomCategory()
					.getValue());
			newIntimationDTO.setRoomCategory(selectValue);
		}
		newIntimationDTO.setStatus(intimation.getStatus() != null ? intimation
				.getStatus() : "");
		newIntimationDTO
				.setStatusDate(intimation.getStatusDate() != null ? intimation
						.getStatusDate() : null);
		// intimationDto.setVersion( intimation.getVersion() );

		newIntimationDTO.setCallerContactNum(intimation
				.getCallerMobileNumber() != null ? intimation
				.getCallerMobileNumber() : "");		

		newIntimationDTO.setProduct(intimation.getPolicy().getProduct());
		
		// MastersValue CPUMaster =
		// master.getMaster(Integer.parseInt(!ValidatorUtils.isNull(intimation.getCpuId())
		// ? intimation.getCpuId().toString(): null));
		// BigDecimal cpuCode=new BigDecimal("");
		// if(! ValidatorUtils.isNull(CPUMaster))
		// {
		// cpuCode = new BigDecimal(CPUMaster.getValue());
		// }
		// intimationDto.setCpuCode(cpuCode);
		//

		// System.out.println(" policyService  >>>>>>>> "+policyService);
		// Hospitals hospital =
		// IntimationService.getVWHospitalByKey(intimation.getHospital());
		if (!ValidatorUtils.isNull(hospital)) {
			if (hospital != null) {
				HospitalMapper hospitalMapper = new HospitalMapper();				
				newIntimationDTO.setHospitalDto(hospitalMapper.getHospitalDTO(hospital));			}
		}

		if (!ValidatorUtils.isNull(intimation.getIntimationMode()))
			newIntimationDTO.setIntimationModeValue(intimation
					.getIntimationMode().getValue() != null ? intimation
					.getIntimationMode().getValue() : "");
		else
			newIntimationDTO.setIntimationModeValue("");

		if (!ValidatorUtils.isNull(intimation.getRoomCategory()))
			newIntimationDTO.setRoomCategoryValue(intimation.getRoomCategory()
					.getValue() != null ? intimation.getRoomCategory()
					.getValue() : "");
		else
			newIntimationDTO.setRoomCategoryValue("");

		if (!ValidatorUtils.isNull(intimation.getIntimatedBy()))
			newIntimationDTO.setIntimatedByValue(intimation.getIntimatedBy()
					.getValue() != null ? intimation.getIntimatedBy()
					.getValue() : "");
		else
			newIntimationDTO.setIntimatedByValue("");

		if (!ValidatorUtils.isNull(intimation.getPolicy())) {
			PolicyDto policyDto = policyToPolicyDTO(intimation.getPolicy());
			newIntimationDTO.setPolicyDto(policyDto);
			newIntimationDTO
					.setInsuredName((intimation.getPolicy()
							.getProposerFirstName() != null ? intimation
							.getPolicy().getProposerFirstName() : "")
							+ " "
							+ (intimation.getPolicy().getProposerMiddleName() != null ? intimation
									.getPolicy().getProposerMiddleName() : "")
							+ " "
							+ ((intimation.getPolicy().getProposerLastName() != null ? intimation
									.getPolicy().getProposerLastName() : "")));
			newIntimationDTO.setHealthCardNumber(intimation.getPolicy()
					.getHealthCardNumber() != null ? intimation.getPolicy()
					.getHealthCardNumber() : "");
		} else {
			newIntimationDTO.setInsuredName("");
			newIntimationDTO.setHealthCardNumber("");
		}

		if (!ValidatorUtils.isNull(intimation.getPolicy().getProduct()))
			newIntimationDTO.setProductName(intimation.getPolicy().getProduct()
					.getValue() != null ? intimation.getPolicy().getProduct()
					.getValue() : "");
		else
			newIntimationDTO.setProductName("");

		return newIntimationDTO;
	}*/
	

	/*public ClaimDtoOld claimToClaimDTO(Claim claim, Hospitals hospital) {
		ClaimDtoOld claimDto = new ClaimDtoOld();
		claimDto.setKey(claim.getKey());
		claimDto.setClaimId(claim.getClaimId());
		claimDto.setCreatedDate(claim.getCreatedDate());
		claimDto.setClaimedAmount(claim.getClaimedAmount());
		claimDto.setProvisionAmount(claim.getProvisionAmount());
		claimDto.setClaimType(claim.getClaimType().getValue());
		claimDto.setClaimStatus(claim.getStatus().getProcessValue());
		claimDto.setCurrencyId(claim.getCurrencyId());

		NewIntimationDto intimationDto = intimationToIntimationDTO(
				claim.getIntimation(), hospital);
		//claimDto.setIntimation(intimationDto);
		claimDto.setNewIntimationDTO(intimationDto);

		return claimDto;
	}*/
	
	
}
