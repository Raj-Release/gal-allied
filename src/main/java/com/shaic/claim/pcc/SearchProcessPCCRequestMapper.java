package com.shaic.claim.pcc;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.pcc.beans.PCCQuery;
import com.shaic.claim.pcc.beans.PCCRequest;
import com.shaic.claim.pcc.dto.PCCQueryDetailsTableDTO;
import com.shaic.claim.pcc.dto.PCCReplyDetailsTableDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.pcc.dto.SearchProcessPCCRequestTableDTO;
import com.shaic.claim.pcc.dto.ZonalMedicalDetailsTableDTO;
import com.shaic.claim.processdatacorrectionhistorical.bean.ReimbursementHist;
import com.shaic.domain.Reimbursement;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class SearchProcessPCCRequestMapper {


	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();
	
	private static MapperFacade tableMapper;

	static SearchProcessPCCRequestMapper myObj;

	private static BoundMapperFacade<PCCRequest, SearchProcessPCCRequestTableDTO> processPCCRequestMapper;
	private static BoundMapperFacade<PCCRequest, PccDetailsTableDTO> pccDetailsMapper;
	private static BoundMapperFacade<PCCQuery, PCCQueryDetailsTableDTO> pccQueryDetailsMapper;
	private static BoundMapperFacade<PCCQuery, PCCReplyDetailsTableDTO> pccReplyDetailsMapper;
	private static BoundMapperFacade<PCCQuery, ZonalMedicalDetailsTableDTO> pccZonalDetailsMaper;

	
	private static ClassMapBuilder<PCCRequest, SearchProcessPCCRequestTableDTO> pccRequestMap;
	private static ClassMapBuilder<PCCRequest, PccDetailsTableDTO> pccDetailsMap;
	private static ClassMapBuilder<PCCQuery, PCCQueryDetailsTableDTO> pccQueryDetailsMap;
	private static ClassMapBuilder<PCCQuery, PCCReplyDetailsTableDTO> pccReplyDetailsMap;
	private static ClassMapBuilder<PCCQuery, ZonalMedicalDetailsTableDTO> pccZonalDetailsMap;


	public static void getAllMapValues(){

		pccRequestMap = mapperFactory.classMap(PCCRequest.class, SearchProcessPCCRequestTableDTO.class);
		pccDetailsMap = mapperFactory.classMap(PCCRequest.class, PccDetailsTableDTO.class);
		pccQueryDetailsMap = mapperFactory.classMap(PCCQuery.class, PCCQueryDetailsTableDTO.class);
		pccReplyDetailsMap = mapperFactory.classMap(PCCQuery.class, PCCReplyDetailsTableDTO.class);
		pccZonalDetailsMap = mapperFactory.classMap(PCCQuery.class, ZonalMedicalDetailsTableDTO.class);

		pccRequestMap.field("key", "key");
		pccRequestMap.field("intimationNo", "intimationNo");
		pccRequestMap.field("intimation.insured.insuredName", "insuredPatientName");
		pccRequestMap.field("intimation.hospital", "hospitalId");
		//pccRequestMap.field("intimation.hospital.name", "hospitalName");
		pccRequestMap.field("intimation.policy.productName", "productName");
		pccRequestMap.field("intimation.cpuCode", "cpu");
		pccRequestMap.field("pccCategory.pccDesc", "pccCatagory");
		pccRequestMap.field("intimation.key", "intimationKey");
		pccRequestMap.field("pccSource.value", "pccSource");
		pccRequestMap.field("intimation.createdDate", "dateAndTime1");
		
		pccDetailsMap.field("key", "pccKey");
		pccDetailsMap.field("pccCategory.key", "pccCategory.id");
		pccDetailsMap.field("pccCategory.pccDesc", "pccCategory.value");
		pccDetailsMap.field("subCategory.key", "pccSubCategory1.id");
		pccDetailsMap.field("subCategory.pccSubDesc", "pccSubCategory1.value");
		pccDetailsMap.field("subCategoryTwo.key", "pccSubCategory2.id");
		pccDetailsMap.field("subCategoryTwo.pccSubDesc", "pccSubCategory2.value");
		pccDetailsMap.field("pccDoctorRemarks", "escalatePccRemarks");
		
		pccQueryDetailsMap.field("key", "queryKey");
		pccQueryDetailsMap.field("queryRemarks", "queryRemarks");
		pccQueryDetailsMap.field("roleAssignedBy", "queryRaiseRole");
		pccQueryDetailsMap.field("userAssignedBy", "queryRaiseBy");
		pccQueryDetailsMap.field("createdDate", "queryRaiseDate");
		
		pccReplyDetailsMap.field("key", "queryKey");
		pccReplyDetailsMap.field("queryReplyRemarks", "replyRemarks");
		pccReplyDetailsMap.field("roleAssigned", "replyRole");
		pccReplyDetailsMap.field("repliedBy", "replyGivenBy");
		pccReplyDetailsMap.field("modifiedDate", "repliedDate");
		
		/*pccZonalDetailsMap.field("key", "queryKey");
		pccZonalDetailsMap.field("roleAssignedBy", "medicalIdAndName");
		pccZonalDetailsMap.field("queryRemarks", "remarks");
		pccZonalDetailsMap.field("createdDate", "queryRaiseDate");*/
		
		pccRequestMap.register();
		pccDetailsMap.register();
		pccQueryDetailsMap.register();
		pccReplyDetailsMap.register();
		pccZonalDetailsMap.register();
		processPCCRequestMapper = mapperFactory.getMapperFacade(PCCRequest.class, SearchProcessPCCRequestTableDTO.class);
		pccDetailsMapper = mapperFactory.getMapperFacade(PCCRequest.class, PccDetailsTableDTO.class);
		pccQueryDetailsMapper = mapperFactory.getMapperFacade(PCCQuery.class, PCCQueryDetailsTableDTO.class);
		pccReplyDetailsMapper = mapperFactory.getMapperFacade(PCCQuery.class, PCCReplyDetailsTableDTO.class);
		pccZonalDetailsMaper = mapperFactory.getMapperFacade(PCCQuery.class, ZonalMedicalDetailsTableDTO.class);
		tableMapper = mapperFactory.getMapperFacade();
	}

	public static List<SearchProcessPCCRequestTableDTO> getProcessPCCRequestTableDTOs(List<PCCRequest> pccRequests){
		List<SearchProcessPCCRequestTableDTO> mapAsList = tableMapper.mapAsList(pccRequests, SearchProcessPCCRequestTableDTO.class);
		return mapAsList;
	}
	
	/**************************** PccDetailsTableDTO **********************************/

	public PccDetailsTableDTO getDetailsTableDTO(PCCRequest pccRequest) {
		PccDetailsTableDTO pccTableDTO = pccDetailsMapper.map(pccRequest);
		return pccTableDTO;
	}

	/**************************** PccDetailsTableDTO END **********************************/
	
	/**************************** PCCQueryDetailsTableDTO **********************************/
	public List<PCCQueryDetailsTableDTO> getQueryDetailsTableDTOs(List<PCCQuery> pccQueries){
		List<PCCQueryDetailsTableDTO> mapAsList = tableMapper.mapAsList(pccQueries, PCCQueryDetailsTableDTO.class);
		return mapAsList;
	}
	
	public PCCQueryDetailsTableDTO getQueryDetailsTableDTO(PCCQuery pccQuery) {
		PCCQueryDetailsTableDTO queryDetailsTableDTO = pccQueryDetailsMapper.map(pccQuery);
		return queryDetailsTableDTO;
	}
	
	/**************************** PCCReplyDetailsTableDTO **********************************/
	public List<PCCReplyDetailsTableDTO> getReplyDetailsTableDTOs(List<PCCQuery> pccQueries){
		List<PCCReplyDetailsTableDTO> mapAsList = tableMapper.mapAsList(pccQueries, PCCReplyDetailsTableDTO.class);
		return mapAsList;
	}
	
	public PCCReplyDetailsTableDTO getReplyDetailsTableDTO(PCCQuery pccQuery) {
		PCCReplyDetailsTableDTO replyDetailsTableDTO = pccReplyDetailsMapper.map(pccQuery);
		return replyDetailsTableDTO;
	}
	
	/**************************** PCCReplyDetailsTableDTO END **********************************/
	
	/**************************** ZonalMedicalDetailsTableDTO **********************************/
	public List<ZonalMedicalDetailsTableDTO> getZonalDetailsTableDTOs(List<PCCQuery> pccQueries){
		List<ZonalMedicalDetailsTableDTO> mapAsList = tableMapper.mapAsList(pccQueries, ZonalMedicalDetailsTableDTO.class);
		return mapAsList;
}
	
	
	/**************************** ZonalMedicalDetailsTableDTO END **********************************/

	public static SearchProcessPCCRequestMapper getInstance(){
		if(myObj == null){
			myObj = new SearchProcessPCCRequestMapper();
			getAllMapValues();
		}
		return myObj;
	}
}