package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.components.MISDocumentDTO;
import com.shaic.domain.LumenQueryDocument;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class MISQueryDocumentDetailsMapper {

	private static MapperFacade tableMapper;

	static MISQueryDocumentDetailsMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenQueryDocument, MISDocumentDTO> classMap = mapperFactory.classMap(LumenQueryDocument.class, MISDocumentDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("lumenQuery", "lumenQuery");
		classMap.field("lumenQueryDetails", "lumenQueryDetails");
		classMap.field("lumenQueryDetails.queryRemarks", "queryRemarks");
		classMap.field("lumenQueryDetails.replyRemarks", "replyRemarks");

		
		//classMap.field("queryRemarks", "uploadedFileType"); // Need FileType column not id column...
		classMap.field("fileName", "uploadedFileName");
		classMap.field("createdDate", "uploadedDate");
		classMap.field("createdBy", "uploadedBy");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<MISDocumentDTO> getLumenQueryDetails(List<LumenQueryDocument> lumenRequestList) {
		List<MISDocumentDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, MISDocumentDTO.class);
		return mapAsList;
	}

	public static MISQueryDocumentDetailsMapper getInstance(){
		if(myObj == null){
			myObj = new MISQueryDocumentDetailsMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
