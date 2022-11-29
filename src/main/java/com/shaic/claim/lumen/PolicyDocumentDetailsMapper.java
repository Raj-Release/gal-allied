package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.policyupload.PolicyDocumentTableDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class PolicyDocumentDetailsMapper {
	private static MapperFacade tableMapper;

	static PolicyDocumentDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<DocumentDetails, PolicyDocumentTableDTO> classMap = mapperFactory.classMap(DocumentDetails.class, PolicyDocumentTableDTO.class);
		classMap.field("documentType", "fileType");
		classMap.field("fileName", "fileName");
		classMap.field("createdDate", "uploadedDate");
		classMap.field("createdBy", "uploadedBy");
		classMap.field("intimationNumber", "intimationNum");
		classMap.field("deletedFlag","deletedFlag");
		classMap.field("sfFileName","sfFileName");		
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<PolicyDocumentTableDTO> getDocumentDetails(List<DocumentDetails> lumenRequestList) {
		List<PolicyDocumentTableDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, PolicyDocumentTableDTO.class);
		return mapAsList;
	}

	public static PolicyDocumentDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new PolicyDocumentDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
