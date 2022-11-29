package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.upload.DocumentTableDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class DocumentDetailsMapper {
	private static MapperFacade tableMapper;

	static DocumentDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<DocumentDetails, DocumentTableDTO> classMap = mapperFactory.classMap(DocumentDetails.class, DocumentTableDTO.class);
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

	public static List<DocumentTableDTO> getDocumentDetails(List<DocumentDetails> lumenRequestList) {
		List<DocumentTableDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, DocumentTableDTO.class);
		return mapAsList;
	}

	public static DocumentDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new DocumentDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
	
	/*public DocumentDetails getDocumentDetailsObj(DocumentTableDTO documentTableDto) {
		DocumentDetails dest = tableMapper.map(documentTableDto, DocumentDetails.class);
		return dest;
	}*/
}
