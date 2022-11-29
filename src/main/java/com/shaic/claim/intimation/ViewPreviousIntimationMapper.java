package com.shaic.claim.intimation;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.GalaxyCRMIntimation;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.StageIntimation;
import com.shaic.domain.preauth.PremiaPreviousClaim;

public class ViewPreviousIntimationMapper {
	
	private static MapperFacade tableMapper;
	
	public List<ViewPreviousIntimationDto> getIntimationDTO(
			List<Intimation> intimationList) {
		List<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = new ArrayList<ViewPreviousIntimationDto>();		
		for (Intimation intimation : intimationList) {
			if(!intimation.getStatus().getKey().equals(ReferenceTable.VIEW_PREVIOUS_INTIMATION_STATUS_FOR_DRAFT)){
				ViewPreviousIntimationDto viewPreviousIntimationDto = new ViewPreviousIntimationDto();
				viewPreviousIntimationDto.setKey(intimation.getKey());
				viewPreviousIntimationDto.setIntimatinoNo(intimation.getIntimationId());
				viewPreviousIntimationDto.setPolicyNo(intimation.getPolicy().getPolicyNumber());
				viewPreviousIntimationDto.setInsuredPatientName(intimation.getInsured().getInsuredName());
				viewPreviousIntimationDto.setCallerOrIntimatorName(intimation.getIntimaterName());
				viewPreviousIntimationDto.setContactNo(intimation.getCallerLandlineNumber());
				viewPreviousIntimationDto.setCallerAddress(intimation.getCallerAddress());
				viewPreviousIntimationDto.setAttenderContactNo(intimation.getAttendersLandlineNumber());
				viewPreviousIntimationDto.setAttenderMobileNo(intimation.getAttendersMobileNumber());
				viewPreviousIntimationDto.setStatus(intimation.getStatus().getProcessValue());
				viewPreviousIntimationDto.setAdmittedDate(SHAUtils.formatDate(intimation.getAdmissionDate()));
				viewPreviousIntimationDto.setCreatedOn(SHAUtils.formatDate(intimation.getCreatedDate()));
				viewPreviousIntimationDto.setModifiedOn(SHAUtils.formatDate(intimation.getModifiedDate()));
				viewPreviousIntimationDto.setApplicationFlag("G");
				viewPreviousIntimationDto.setHospitalKey(intimation.getHospital());
				viewPreviousIntimationDto.setHospitalCode(null);
				if(intimation.getModifiedDate() == null){
					viewPreviousIntimationDto.setSaveAndSubmittedOn(SHAUtils.formatDate(intimation.getCreatedDate()));
				}else{
					viewPreviousIntimationDto.setSaveAndSubmittedOn(SHAUtils.formatDate(intimation.getModifiedDate()));
				}
				viewPreviousIntimationDtoList.add(viewPreviousIntimationDto);
			}
			
					
		}
			
		return viewPreviousIntimationDtoList;
	}
	
	public List<ViewPreviousIntimationDto> getPremiaPreviousClaimDto(
			List<PremiaPreviousClaim> intimationList) {
		List<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = new ArrayList<ViewPreviousIntimationDto>();		
		for (PremiaPreviousClaim intimation : intimationList) {
				ViewPreviousIntimationDto viewPreviousIntimationDto = new ViewPreviousIntimationDto();
				viewPreviousIntimationDto.setKey(intimation.getPolicySysId());
				viewPreviousIntimationDto.setIntimatinoNo(intimation.getIntimationNumber());
				viewPreviousIntimationDto.setPolicyNo(intimation.getPolicyNumber());
				viewPreviousIntimationDto.setInsuredPatientName(intimation.getInsuredPatientName());
				viewPreviousIntimationDto.setCallerOrIntimatorName(intimation.getIntimatorName());
				viewPreviousIntimationDto.setContactNo(intimation.getContactNo());
				viewPreviousIntimationDto.setCallerAddress(intimation.getAddress1());
				viewPreviousIntimationDto.setAttenderContactNo(intimation.getAttendersMobileNo());
				viewPreviousIntimationDto.setAttenderMobileNo(intimation.getAttendersMobileNo());
				viewPreviousIntimationDto.setStatus(intimation.getStatus());
				viewPreviousIntimationDto.setAdmittedDate(intimation.getAdmittedDate());
				viewPreviousIntimationDto.setCreatedOn(intimation.getCreatedOn());
				viewPreviousIntimationDto.setModifiedOn(intimation.getModifiedOn());
				viewPreviousIntimationDto.setSaveAndSubmittedOn(intimation.getSaveSubmittedOn());
				viewPreviousIntimationDto.setApplicationFlag("P");
				viewPreviousIntimationDto.setHospitalKey(null);
				viewPreviousIntimationDto.setHospitalCode(intimation.getHospitalCode());
				viewPreviousIntimationDtoList.add(viewPreviousIntimationDto);
			
					
		}
			
		return viewPreviousIntimationDtoList;
	}
	
	public List<ViewPreviousIntimationDto> getCRMIntimationDTO(
			List<GalaxyCRMIntimation> intimationList) {
		List<ViewPreviousIntimationDto> viewPreviousIntimationDtoList = new ArrayList<ViewPreviousIntimationDto>();		
		for (GalaxyCRMIntimation intimation : intimationList) {
				ViewPreviousIntimationDto viewPreviousIntimationDto = new ViewPreviousIntimationDto();
				viewPreviousIntimationDto.setKey(intimation.getKey());
				viewPreviousIntimationDto.setIntimatinoNo(intimation.getIntimationNumber());
				viewPreviousIntimationDto.setPolicyNo(intimation.getPolicyNo());
				viewPreviousIntimationDto.setInsuredPatientName(intimation.getInsuredPatientName());
				viewPreviousIntimationDto.setCallerOrIntimatorName(intimation.getIntimatorName());
				viewPreviousIntimationDto.setContactNo(intimation.getIntimatorContactNo());
				//viewPreviousIntimationDto.setCallerAddress(intimation.getCallerAddress());
				viewPreviousIntimationDto.setAttenderContactNo(intimation.getAttenderMobileNo());
				viewPreviousIntimationDto.setAttenderMobileNo(intimation.getAttenderMobileNo());
				//viewPreviousIntimationDto.setStatus(intimation.getStatus().getProcessValue());
				viewPreviousIntimationDto.setAdmittedDate(SHAUtils.formatDate(intimation.getAdmittedDate()));
				viewPreviousIntimationDto.setCreatedOn(SHAUtils.formatDate(intimation.getCreatedDate()));
				//viewPreviousIntimationDto.setModifiedOn(formatDate(intimation.getModifiedDate()));
				viewPreviousIntimationDto.setApplicationFlag("G");
				viewPreviousIntimationDto.setHospitalName(intimation.getHospitalName());
				viewPreviousIntimationDto.setHospitalAddress(intimation.getHospitalAddress());
				viewPreviousIntimationDto.setHospitalType(intimation.getHospitalType());
				viewPreviousIntimationDto.setIsdummy(intimation.getDummyHospFlag());
				viewPreviousIntimationDto.setSaveAndSubmittedOn(SHAUtils.formatDate(intimation.getCreatedDate()));
				viewPreviousIntimationDto.setHospitalCode(null);
				viewPreviousIntimationDtoList.add(viewPreviousIntimationDto);
			}
		return viewPreviousIntimationDtoList;
	}

}
