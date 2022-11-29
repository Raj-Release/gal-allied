package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

/**
 * 
 * @author Lakshminarayana
 *
 */

public class ReimbursementMapper {
	
		private MapperFacade mapper;
		
		public ReimbursementMapper() 
		{
			MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
			ClassMapBuilder<Reimbursement, ReimbursementDto> classMap = mapperFactory.classMap(Reimbursement.class, ReimbursementDto.class);
			
			classMap.field("key","key");
			classMap.field("docAcknowLedgement.key", "docAcknowledgementDto.key");
			classMap.field("claim.key", "claimDto.key");
			classMap.field("rodNumber","rodNumber");
			classMap.field("bankId","bankId");
			classMap.field("paymentModeId","paymentModeId");
			classMap.field("payeeName","payeeName");
			classMap.field("payeeEmailId","payeeEmailId");
			classMap.field("panNumber","panNumber");
			classMap.field("reasonForChange","reasonForChange");
			classMap.field("legalHeirFirstName","legalHeirFirstName");
			classMap.field("legalHeirMiddleName","legalHeirMiddleName");
			classMap.field("legalHeirLastName","legalHeirLastName");
			classMap.field("payableAt","payableAt");
			classMap.field("accountNumber","accountNumber");
			classMap.field("dateOfAdmission","dateOfAdmission");
			classMap.field("doaChangeReason","doaChangeReason");
			classMap.field("roomCategory.key","roomCategory.id");
			classMap.field("roomCategory.value","roomCategory.value");
			classMap.field("numberOfDays","numberOfDays");
			classMap.field("dateOfDischarge","dateOfDischarge");
			classMap.field("stage.key","stageSelectValue.id");
			classMap.field("stage.stageName","stageSelectValue.value");
			classMap.field("status.key","statusSelectValue.id");
			classMap.field("status.processValue","statusSelectValue.value");
			classMap.field("addOnCoversApprovedAmount","addOnCoversApprovedAmount");
			classMap.field("optionalApprovedAmount","optionalApprovedAmount");
			classMap.field("benApprovedAmt","benApprovedAmt");
			classMap.field("claimApprovalAmount","claimApprovalAmount");
			classMap.field("patientStatus.key","patientStatusId");
			classMap.field("dateOfDeath","dateOfDeath");
			classMap.field("deathReason","deathReason");
			classMap.field("nomineeName","nomineeName");
			classMap.field("nomineeAddr","nomineeAddr");
						
			classMap.register();
			 
			 
			 this.mapper = mapperFactory.getMapperFacade();
		}
		
		public Reimbursement getReimbursement(ReimbursementDto reimbursementDto) {
			Reimbursement dest = mapper.map(reimbursementDto, Reimbursement.class);
			
			return dest;
		}
		
		public ReimbursementDto getReimbursementDto(Reimbursement reimbursement) {
			ReimbursementDto dest = mapper.map(reimbursement, ReimbursementDto.class);
			return dest;
		}

}
