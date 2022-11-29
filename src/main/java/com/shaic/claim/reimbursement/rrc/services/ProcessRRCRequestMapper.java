/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.QuantumReductionDetailsDTO;
import com.shaic.domain.RRCDetails;
import com.shaic.domain.RRCRequest;
import com.shaic.newcode.wizard.domain.MappingUtil;


/**
 * @author ntv.vijayar
 *
 */
public class ProcessRRCRequestMapper {
	
	private static MapperFacade tableMapper;
	
	private static MapperFacade rrcDetailsMapper;
	private static MapperFacade rrcRequestDataMapper;
	
	static ProcessRRCRequestMapper myObj;
	
	private static BoundMapperFacade<RRCDetails, ExtraEmployeeEffortDTO> rrcEmployeeDetailsMapper;
	
	
	public static void getAllMapValues()  {

		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		//This map is used by search process rrc request service, to map rrc request with table dto.
		ClassMapBuilder<RRCRequest, SearchProcessRRCRequestTableDTO> rrcRequestMap = mapperFactory.classMap(RRCRequest.class, SearchProcessRRCRequestTableDTO.class);
		ClassMapBuilder<RRCDetails, ExtraEmployeeEffortDTO> rrcDetailsDataMap  = mapperFactory.classMap(RRCDetails.class, ExtraEmployeeEffortDTO.class);
		ClassMapBuilder<RRCRequest, QuantumReductionDetailsDTO> rrcRequestDataMap  = mapperFactory.classMap(RRCRequest.class, QuantumReductionDetailsDTO.class);
		ClassMapBuilder<RRCDetails, ExtraEmployeeEffortDTO> rrcEmployeeDetailsMap = mapperFactory.classMap(RRCDetails.class,ExtraEmployeeEffortDTO.class);
		/*ClassMapBuilder<Hospitals, SearchPreMedicalProcessingEnhancementTableDTO> classHospMap = mapperFactory.classMap(Hospitals.class, SearchPreMedicalProcessingEnhancementTableDTO.class);
		ClassMapBuilder<TmpStarFaxDetails, SearchPreMedicalProcessingEnhancementTableDTO> classDocMap = mapperFactory.classMap(TmpStarFaxDetails.class, SearchPreMedicalProcessingEnhancementTableDTO.class);*/
		// rrcRequestMap.field(<DO>,<DTO>);
		rrcRequestMap.field("rrcRequestKey", "key");
		rrcRequestMap.field("reimbursement.key", "rodKey");
		rrcRequestMap.field("claim.key", "claimKey");
		rrcRequestMap.field("claim.intimation.intimationId", "intimationNo");
		rrcRequestMap.field("claim.intimation.key", "intimationKey");
		rrcRequestMap.field("rrcRequestNumber", "rrcRequestNo");
		rrcRequestMap.field("rrcInitiatedDate", "dateOfRequest");
		rrcRequestMap.field("requestorID", "requestorId");
		rrcRequestMap.field("requestedTypeId.value", "rrcRequestType");
	
		rrcDetailsDataMap.field("rrcRequest","rrcRequestKey");
		rrcDetailsDataMap.field("employeeId","employeeId");
		rrcDetailsDataMap.field("employeeName","employeeName");
		/*rrcDetailsDataMap.field("creditTypeId","creditType.id");
		rrcDetailsDataMap.field("score","score");
		rrcDetailsDataMap.field("remarks","remarks");*/
		/*rrcDetailsDataMap.field("employeeZone",);
		rrcDetailsDataMap.field("employeeDept",);*/
		
		rrcEmployeeDetailsMap.field("rrcDetailsKey","rrcDetailsKey");
		rrcEmployeeDetailsMap.field("rrcRequest","rrcRequestKey");
		rrcEmployeeDetailsMap.field("employeeId","selEmployeeId.value");
	//	rrcEmployeeDetailsMap.field("employeeId","category.id");

		rrcEmployeeDetailsMap.field("employeeName","selEmployeeName.value");
		//rrcEmployeeDetailsMap.field("employeeName","selEmployeeNameValue");
		rrcEmployeeDetailsMap.field("creditTypeId.key","creditType.id");
		//rrcEmployeeDetailsMap.field("creditTypeId.key","creditTypeValue");
		rrcEmployeeDetailsMap.field("score","score");
	//	rrcEmployeeDetailsMap.field("score","empScore");
		rrcEmployeeDetailsMap.field("remarks","remarks");
		rrcEmployeeDetailsMap.field("contributorTypeId.key","typeOfContributor.id");
		
		rrcRequestDataMap.field("rrcRequestNumber","requestNo");
		rrcRequestDataMap.field("preAuthAmount","preAuthAmount");
		rrcRequestDataMap.field("finalBillAmount","finalBillAmount");
		rrcRequestDataMap.field("settlementAmount","settlementAmount");
		rrcRequestDataMap.field("anhAmount","anhAmount");
		rrcRequestDataMap.field("savedAmount","savedAmount");
		rrcRequestDataMap.field("diagnosis","diagnosis");
		rrcRequestDataMap.field("management","management");
		rrcRequestDataMap.field("significantClinicalId.value","significantClinicalInformationValue");
		rrcRequestDataMap.field("eligiblityTypeId.value","eligiblity");
		rrcRequestDataMap.field("requestorSavedAmount","requestedSavedAmount");
		rrcRequestDataMap.field("requestRemarks","requestRemarks");
		rrcRequestDataMap.field("anh","anhFlag");
		
		rrcRequestMap.register();
		rrcDetailsDataMap.register();
		rrcRequestDataMap.register();
		rrcEmployeeDetailsMap.register();
	
		tableMapper = mapperFactory.getMapperFacade();
		rrcDetailsMapper = mapperFactory.getMapperFacade();
		rrcRequestDataMapper = mapperFactory.getMapperFacade();
		rrcEmployeeDetailsMapper = mapperFactory.getMapperFacade(RRCDetails.class, ExtraEmployeeEffortDTO.class);
		
	}
	
	@SuppressWarnings("unused")
	public  List<SearchProcessRRCRequestTableDTO> getRRCRequestList(
			List<RRCRequest> rrcRequestList) {
		List<SearchProcessRRCRequestTableDTO> mapAsList = tableMapper
				.mapAsList(rrcRequestList,
						SearchProcessRRCRequestTableDTO.class);
		return mapAsList;
	}
	
	public List<ExtraEmployeeEffortDTO> getEmployeeListenerTableData(List<RRCDetails> rrcDetailsList)
	{
		List<ExtraEmployeeEffortDTO> mapAsList = null;
		try
		{
		 mapAsList = rrcDetailsMapper
				.mapAsList(rrcDetailsList,
						ExtraEmployeeEffortDTO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapAsList;
	}
	
	public List<QuantumReductionDetailsDTO> getQuantumReductionDetailsList(List<RRCRequest> rrcDetailsList)
	{
		List<QuantumReductionDetailsDTO> mapAsList = rrcRequestDataMapper
				.mapAsList(rrcDetailsList,
						QuantumReductionDetailsDTO.class);
		return mapAsList;
	}
	
	public RRCDetails getRRCDetailsForEmployee(ExtraEmployeeEffortDTO extraEmployeeEffortDTO)
	{
		RRCDetails rrcDetails = rrcEmployeeDetailsMapper.mapReverse(extraEmployeeEffortDTO);
		return rrcDetails;
	}

	public static ProcessRRCRequestMapper getInstance(){
        if(myObj == null){
            myObj = new ProcessRRCRequestMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
