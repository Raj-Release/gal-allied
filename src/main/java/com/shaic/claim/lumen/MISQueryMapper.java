package com.shaic.claim.lumen;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.lumen.components.MISQueryReplyDTO;
import com.shaic.domain.LumenQuery;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class MISQueryMapper {

	private static MapperFacade tableMapper;

	static MISQueryMapper  myObj;

	public static void getAllMapValues()  {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<LumenQuery, MISQueryReplyDTO> classMap = mapperFactory.classMap(LumenQuery.class, MISQueryReplyDTO.class);
		classMap.field("lumenRequest", "lumenRequest");
		classMap.field("queryRaisedBy", "queryRaisedBy");
		classMap.field("queryRaisedDate", "queryRaisedDate");
		classMap.field("key", "queryKey");
		classMap.field("queryRaisedRole", "queryRaisedRole");
		classMap.field("repliedBy", "replyReceivedFrom");
		classMap.field("repliedDate", "repliedDate");
		classMap.field("replyRemarks", "misReplyRemarks");
		classMap.register();
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<MISQueryReplyDTO> getLumenQueryDetails(List<LumenQuery> lumenRequestList) {
		List<MISQueryReplyDTO> mapAsList = tableMapper.mapAsList(lumenRequestList, MISQueryReplyDTO.class);
		return mapAsList;
	}

	public static MISQueryMapper getInstance(){
		if(myObj == null){
			myObj = new MISQueryMapper();
			getAllMapValues();
		}
		return myObj;
	}
}
