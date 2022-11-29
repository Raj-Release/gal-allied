package com.shaic.claim.fvrdetails.view;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fvrdetailedview.FvrDetailedViewDTO;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.dto.NewFVRGradingDTO;
import com.shaic.domain.FVRGradingDetail;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class ViewFVRMapper {

	private static MapperFacade tableMapper;
	
	static ViewFVRMapper  myObj;


	private static MapperFactory mapperFactory = MappingUtil
			.getMapperFactory(true);

	private static BoundMapperFacade<FieldVisitRequest, ViewFVRFormDTO> fvrRemarksMapper;
	private static BoundMapperFacade<FieldVisitRequest, FvrDetailedViewDTO> fvrDetailsMapper;
	private static ClassMapBuilder<FieldVisitRequest, ViewFVRDTO> classMap = mapperFactory
			.classMap(FieldVisitRequest.class, ViewFVRDTO.class);
	private static ClassMapBuilder<FieldVisitRequest, ViewFVRFormDTO> fvrForm = mapperFactory
			.classMap(FieldVisitRequest.class, ViewFVRFormDTO.class);
	private static ClassMapBuilder<FieldVisitRequest, FvrDetailedViewDTO> fvrDetailedView = mapperFactory
			.classMap(FieldVisitRequest.class, FvrDetailedViewDTO.class);

	public static void getAllMapValues()  {
 
		classMap.field("key", "key");
		classMap.field("representativeName", "representativeName");
		classMap.field("representativeCode", "representativeCode");
		classMap.field("intimation.hospital", "hospitalName");
		classMap.field("hospitalVisitedDate", "hospitalVisitedDate");
		classMap.field("fvrTriggerPoints", "remarks");
		classMap.field("assignedDate", "fVRAssignedDate");
		classMap.field("assignedDate", "fvrassignedDate");
		classMap.field("fvrReceivedDate", "fVRReceivedDate");
		classMap.field("fvrReceivedDate", "fVRreceivedDate");
		classMap.field("claim.intimation.intimationId", "intimationNo");
		classMap.field("allocationTo.value", "allocationTo.value");
		classMap.field("allocationTo.key", "allocationTo.id");
		classMap.field("priority.value", "priority.value");
		classMap.field("priority.key", "priority.id");
		// classMap.field("statusRemarks", "fVRTAT");
		classMap.field("status.processValue", "fVRStatus");
		classMap.field("stage.key", "stageKey");
		classMap.field("status.key", "statusKey");
		fvrForm.field("allocationTo", "allocateTo");
		fvrForm.field("fvrTriggerPoints", "triggerPoints");
		fvrForm.field("claim.key", "claimKey");
		classMap.field("representativeContactNumber", "representativeContactNo");
		classMap.field("patientVerified", "patientVerified");
		classMap.field("gradingRmrks", "gradingRemarks");
		
		fvrDetailedView.field("intimation.hospital", "hospitalName");
		fvrDetailedView.field("allocationTo.value", "allocationTo");
		fvrDetailedView.field("createdBy", "fvrInitiatedBy");
		fvrDetailedView.field("priority.value", "fvrPriority");
		fvrDetailedView.field("createdDate", "fvrInitiatedDateTime");
		fvrDetailedView.field("modifiedBy","modifiedBy");
		fvrDetailedView.field("modifiedDate","modifiedDate");
		fvrDetailedView.field("representativeCode","repCode");
		fvrDetailedView.field("representativeName","repName");
		fvrDetailedView.field("representativeContactNumber","repContactNo");
		fvrDetailedView.field("assignedDate","fvrAssignedDateTime");
		fvrDetailedView.field("fvrReceivedDate","fvrReplyReceivedDateTime");
		fvrDetailedView.field("hospitalVisitedDate","hospitalVisitedDate");
		fvrDetailedView.field("fvrGradingDate","fvrGradedDateTime");
		fvrDetailedView.field("asigneeName.empFirstName","fvrAssignedBy");
		fvrDetailedView.field("gradingRmrks","fvrGradedRemarks");
		fvrDetailedView.field("executiveComments","fvrRemarks");

		classMap.field("status.key", "fvrStatusKey");
		fvrForm.register();
		classMap.register();
		fvrDetailedView.register();
		tableMapper = mapperFactory.getMapperFacade();
		fvrRemarksMapper = mapperFactory.getMapperFacade(
				FieldVisitRequest.class, ViewFVRFormDTO.class);
		fvrDetailsMapper = mapperFactory.getMapperFacade(
				FieldVisitRequest.class, FvrDetailedViewDTO.class);
	}

	public static List<ViewFVRDTO> getViewFvrDto(
			List<FieldVisitRequest> fieldVisitRequestList) {
		List<ViewFVRDTO> mapAsList = tableMapper.mapAsList(fieldVisitRequestList,
				ViewFVRDTO.class);
		return mapAsList;
	}

	public static FieldVisitRequest getFieldVisitRequest(ViewFVRFormDTO bean) {
		FieldVisitRequest dest = fvrRemarksMapper.mapReverse(bean);
		return dest;
	}
	
	public static FvrDetailedViewDTO getFVRDetailView(FieldVisitRequest fieldVisitRequest) {
		FvrDetailedViewDTO dest = fvrDetailsMapper.map(fieldVisitRequest);
		return dest;
	}

	public static List<ViewFVRDTO> getFvrGraddingDetails(
			FieldVisitRequest fieldVisitRequest) {
		ViewFVRDTO viewFVRDTO = null;
		if (fieldVisitRequest != null) {
			List<ViewFVRDTO> viewFVRDTOList = new ArrayList<ViewFVRDTO>();
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("1");
			viewFVRDTO.setCategory("Patient Verified");
			viewFVRDTO.setApplicability("Applicable");
			viewFVRDTO.setStatus(fieldVisitRequest.getPatientVerified());
			viewFVRDTOList.add(viewFVRDTO);
			
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("2");
			viewFVRDTO.setCategory("Diagnosis Verified");
			viewFVRDTO.setApplicability("Applicable");
			viewFVRDTO.setStatus(fieldVisitRequest.getDiagnosisVerfied());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("3");
			viewFVRDTO.setCategory("Room Tariff and Room Category  Verified");
			viewFVRDTO.setApplicability("Applicable");
			viewFVRDTO.setStatus(fieldVisitRequest.getRoomCategoryVerfied());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("4");
			viewFVRDTO.setCategory("Trigger Points focused");
			viewFVRDTO.setApplicability("Not Applicable");
			viewFVRDTO.setStatus(fieldVisitRequest.getTriggerPointsFocused());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("5");
			viewFVRDTO.setCategory("Pre-Existing Disease Verified");
			viewFVRDTO.setApplicability("Applicable");
			viewFVRDTO.setStatus(fieldVisitRequest.getPedVerified());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("6");
			viewFVRDTO.setCategory("Patient Discharged");
			viewFVRDTO.setApplicability("");
			viewFVRDTO.setStatus(fieldVisitRequest.getPatientDischarged());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("7");
			viewFVRDTO.setCategory("Patient not admitted");
			viewFVRDTO.setApplicability("");
			viewFVRDTO.setStatus(fieldVisitRequest.getPatientNotAdmitted());
			viewFVRDTOList.add(viewFVRDTO);
			
			viewFVRDTO = new ViewFVRDTO();
			viewFVRDTO.setSno("8");
			viewFVRDTO.setCategory("Outstanding FVR (Deserves Incentive)");
			viewFVRDTO.setApplicability("");
			viewFVRDTO.setStatus(fieldVisitRequest.getOutstandingFvr());			
			viewFVRDTOList.add(viewFVRDTO);
			
			return viewFVRDTOList;
		}
		return null;

	}
	
	public static List<NewFVRGradingDTO> getFvrGraddingDetailsNew(
			List<FVRGradingDetail> detailList) {
		NewFVRGradingDTO viewFVRDTO = null;
		if (detailList != null && !detailList.isEmpty()) {
			
			List<NewFVRGradingDTO> viewFVRDTOList = new ArrayList<NewFVRGradingDTO>();
			for (FVRGradingDetail fvrGradingDetail : detailList) {
				
				if(fvrGradingDetail.getSegment() != null){
					switch (fvrGradingDetail.getSegment()) {
					
					case SHAConstants.FVR_GRADING_SEGMENT_A:
						viewFVRDTO = new NewFVRGradingDTO();
						if(fvrGradingDetail.getSeqNo() != null){
							viewFVRDTO.setFvrSeqNo(fvrGradingDetail.getSeqNo());	
						}
						viewFVRDTO.setCategory(fvrGradingDetail.getRemarks());
						viewFVRDTO.setSegment(fvrGradingDetail.getSegment());
						if(fvrGradingDetail.getGrading() != null){
							viewFVRDTO.setStatusFlagSegmentA(fvrGradingDetail.getGrading());
						}
						viewFVRDTO.setKey(fvrGradingDetail.getSeqNo());
						viewFVRDTO.setIsEditAB(false);
						viewFVRDTOList.add(viewFVRDTO);
						break;
					case SHAConstants.FVR_GRADING_SEGMENT_B:
						viewFVRDTO = new NewFVRGradingDTO();
						if(fvrGradingDetail.getSeqNo() != null){
						viewFVRDTO.setFvrSeqNo(fvrGradingDetail.getSeqNo());
						}
						viewFVRDTO.setCategory(fvrGradingDetail.getRemarks());
						viewFVRDTO.setSegment(fvrGradingDetail.getSegment());
						viewFVRDTO.setKey(fvrGradingDetail.getSeqNo());
						if(fvrGradingDetail.getGrading() != null){
							viewFVRDTO.setStatusFlag(fvrGradingDetail.getGrading());
						}
						viewFVRDTO.setKey(fvrGradingDetail.getSeqNo());
						viewFVRDTO.setIsEditAB(false);
						viewFVRDTOList.add(viewFVRDTO);
						break;	
						
					case SHAConstants.FVR_GRADING_SEGMENT_C:
						viewFVRDTO = new NewFVRGradingDTO();
						if(fvrGradingDetail.getSeqNo() != null){
						viewFVRDTO.setFvrSeqNo(fvrGradingDetail.getSeqNo());
						}
						viewFVRDTO.setCategory(fvrGradingDetail.getRemarks());
						viewFVRDTO.setSegment(fvrGradingDetail.getSegment());
						viewFVRDTO.setKey(fvrGradingDetail.getSeqNo());
						if(fvrGradingDetail.getGrading() != null){
							viewFVRDTO.setStatusFlagSegmentC(fvrGradingDetail.getGrading());
						}
						viewFVRDTO.setKey(fvrGradingDetail.getSeqNo());
						viewFVRDTO.setIsEditAB(true);
						viewFVRDTOList.add(viewFVRDTO);
						break;
						

					default:
						break;
					}
				}
				
				
			}
			
			return viewFVRDTOList;
		}
		return null;

	}
	
	
	public static ViewFVRMapper getInstance(){
        if(myObj == null){
            myObj = new ViewFVRMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
