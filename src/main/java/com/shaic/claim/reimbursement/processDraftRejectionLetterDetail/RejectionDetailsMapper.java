package com.shaic.claim.reimbursement.processDraftRejectionLetterDetail;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.ReimbursementRejectionDetailsDto;
import com.shaic.domain.ReimbursementRejection;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class RejectionDetailsMapper {
	
	private MapperFacade mapper;
	
	public RejectionDetailsMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<ReimbursementRejection, ReimbursementRejectionDetailsDto> classMap = mapperFactory.classMap(ReimbursementRejection.class, ReimbursementRejectionDetailsDto.class);
		
		classMap.field("key", "rejectionKey");
		classMap.field("reimbursement.rodNumber", "rodNo");
		classMap.field("reimbursement.docAcknowLedgement.acknowledgeNumber", "acknowledgementNo");
		classMap.field("reimbursement.docAcknowLedgement.documentReceivedFromId.value", "documentReceivedFrom");
		classMap.field("createdDate", "rejectionDate");
		classMap.field("createdBy", "rejectedByRole");
		classMap.field("status.processValue", "rejectionStatus");
		classMap.register();
		 
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public ReimbursementRejectionDetailsDto getReimbursementRejectionDetailsDto(ReimbursementRejection reimbursementRejection) {
		ReimbursementRejectionDetailsDto dest = mapper.map(reimbursementRejection, ReimbursementRejectionDetailsDto.class);

		String billClassification =  "";
		
		if(reimbursementRejection.getReimbursement() != null ){
			if(reimbursementRejection.getReimbursement().getDocAcknowLedgement() != null)
			{
			  if(reimbursementRejection.getReimbursement().getDocAcknowLedgement().getPreHospitalisationFlag() != null && reimbursementRejection.getReimbursement().getDocAcknowLedgement().getPreHospitalisationFlag().equalsIgnoreCase("y")){
				  billClassification = "Pre-Hospitalisation";  
			  }
			  if(reimbursementRejection.getReimbursement().getDocAcknowLedgement().getHospitalisationFlag() != null && reimbursementRejection.getReimbursement().getDocAcknowLedgement().getHospitalisationFlag().equalsIgnoreCase("y")){
				  billClassification = billClassification.equalsIgnoreCase("") ? "Hospitalisation" : billClassification + ", " +  "Hospitalisation" ;  
			  }
			  if(reimbursementRejection.getReimbursement().getDocAcknowLedgement().getHospitalCashFlag() != null && reimbursementRejection.getReimbursement().getDocAcknowLedgement().getHospitalCashFlag().equalsIgnoreCase("y")){
				  billClassification = billClassification.equalsIgnoreCase("") ? "Hospital cash" : billClassification + ", "  +  "Hospital cash" ;  
			  }
			  if(reimbursementRejection.getReimbursement().getDocAcknowLedgement().getPostHospitalisationFlag() != null && reimbursementRejection.getReimbursement().getDocAcknowLedgement().getPostHospitalisationFlag().equalsIgnoreCase("y")){
				  billClassification = billClassification.equalsIgnoreCase("") ?  "Post-Hospitalisation" : billClassification + ", "  +  "Post-Hospitalisation" ;  
			  }
			}
		}
		
		dest.setBillClassification(billClassification);
		
		return dest;
	}	

}
