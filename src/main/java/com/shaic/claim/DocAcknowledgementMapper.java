package com.shaic.claim;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.DocAcknowledgement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class DocAcknowledgementMapper {
	
	private MapperFacade mapper;
	
	public DocAcknowledgementMapper() 
	{
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<DocAcknowledgement, DocAcknowledgementDto> classMap = mapperFactory.classMap(DocAcknowledgement.class, DocAcknowledgementDto.class);
		
		classMap.field("key","key");
		classMap.field("claim.key", "claimDto.key");
		classMap.field("claim.status.key", "claimDto.statusId");
		classMap.field("claim.status.processValue", "claimDto.statusName");
		classMap.field("claim.stage.key", "claimDto.stageId");
		classMap.field("claim.stage.stageName", "claimDto.stageName");
		classMap.field("acknowledgeNumber", "acknowledgeNumber");
		classMap.field("rodKey", "rodKey");
		classMap.field("documentReceivedFromId.key", "documentReceivedFrom.id");
		classMap.field("documentReceivedFromId.value", "documentReceivedFrom.value");
		classMap.field("documentReceivedDate", "documentReceivedDate");
		classMap.field("modeOfReceiptId.key", "modeOfReceipt.id");
		
		classMap.register();
		 
		 
		 this.mapper = mapperFactory.getMapperFacade();
	}
	
	public DocAcknowledgement getDocAcknowledgement(DocAcknowledgementDto docAcknowledgementDto) {
		DocAcknowledgement dest = mapper.map(docAcknowledgementDto, DocAcknowledgement.class);
		
		return dest;
	}
	
	public DocAcknowledgementDto getDocAcknowledgementDto(DocAcknowledgement docAcknowledgement) {
		DocAcknowledgementDto dest = mapper.map(docAcknowledgement, DocAcknowledgementDto.class);
		return dest;
	}


}
