package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;

public class HospitalCommunicationUpdateMapper {
	
	
	static HospitalCommunicationUpdateMapper myObj;
	
	static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	
	private static BoundMapperFacade<HospitalAcknowledge, HospitalAcknowledgeDTO> hospitalAckMapper;
	private static BoundMapperFacade<Hospitals, HospitalAcknowledgeDTO> hospitalDetailsMapper;
	private static BoundMapperFacade<Claim, HospitalAcknowledgeDTO>claimMapper;
	private static BoundMapperFacade<Preauth, HospitalAcknowledgeDTO>preauthMapper;
	private static BoundMapperFacade<Status, HospitalAcknowledgeDTO>statusMapper;
	
    private static ClassMapBuilder<HospitalAcknowledge, HospitalAcknowledgeDTO> hospitalAckMap = mapperFactory.classMap(HospitalAcknowledge.class,HospitalAcknowledgeDTO.class);
    private static ClassMapBuilder<Hospitals, HospitalAcknowledgeDTO> hospitalDetailsMap = mapperFactory.classMap(Hospitals.class,HospitalAcknowledgeDTO.class);	
    private static ClassMapBuilder<Claim, HospitalAcknowledgeDTO>claimDetailsMap=mapperFactory.classMap(Claim.class, HospitalAcknowledgeDTO.class);
    private static ClassMapBuilder<Preauth, HospitalAcknowledgeDTO>preauthMap=mapperFactory.classMap(Preauth.class, HospitalAcknowledgeDTO.class);
    private static ClassMapBuilder<Status, HospitalAcknowledgeDTO>statusMap=mapperFactory.classMap(Status.class, HospitalAcknowledgeDTO.class);
		
    public static void getAllMapValues()  {
    	hospitalAckMap.field("contactPersonName","hospitalContactName");
    	hospitalAckMap.field("designation", "designation");
    	hospitalAckMap.field("remarks", "remarks");
		
    	hospitalDetailsMap.field("key", "key");
    	hospitalDetailsMap.field("name", "hospitalName");
    	hospitalDetailsMap.field("hospitalCode", "hopitalCode");
    	hospitalDetailsMap.field("address", "address1");
//    	hospitalDetailsMap.field("buildingName", "address2");
//    	hospitalDetailsMap.field("streetName", "address3");
    	hospitalDetailsMap.field("city", "city");
    	hospitalDetailsMap.field("state", "state");
    	hospitalDetailsMap.field("pincode", "pinCode");
    	hospitalDetailsMap.field("phoneNumber", "hospitalPhno");
    	hospitalDetailsMap.field("authorizedRepresentative", "authorizedRepresentative");
    	hospitalDetailsMap.field("representativeName", "nameofRepresentative");
    	hospitalDetailsMap.field("hospitalTypeName", "hospitalCategory");
    	hospitalDetailsMap.field("remark", "hospitalRemarks");
    	
    	claimDetailsMap.field("claimedAmount", "claimedAmount");
    	
    //	preauthMap.field("approvedAmount", "approvedAmount");
    	preauthMap.field("totalApprovalAmount", "approvedAmount");
    	
    	preauthMap.field("remarks", "denialRemarks");
    	preauthMap.field("status.processValue", "claimStatus");
    	
//    	statusMap.field("processValue", "claimStatus");
    	
    	
    	
    	hospitalAckMap.register();
    	hospitalDetailsMap.register();
    	claimDetailsMap.register();
    	preauthMap.register();
    	statusMap.register();
    	
    	
    	hospitalAckMapper=mapperFactory.getMapperFacade(HospitalAcknowledge.class, HospitalAcknowledgeDTO.class);
    	hospitalDetailsMapper=mapperFactory.getMapperFacade(Hospitals.class,HospitalAcknowledgeDTO.class);
    	claimMapper=mapperFactory.getMapperFacade(Claim.class,HospitalAcknowledgeDTO.class);
    	preauthMapper=mapperFactory.getMapperFacade(Preauth.class,HospitalAcknowledgeDTO.class);
    	statusMapper=mapperFactory.getMapperFacade(Status.class,HospitalAcknowledgeDTO.class);
    	
           }
		
	public HospitalAcknowledge getHospitalAcknowledge(HospitalAcknowledgeDTO hospitalAcknowledgeDTO) {
		HospitalAcknowledge dest = hospitalAckMapper.mapReverse(hospitalAcknowledgeDTO);
		return dest;
	}
	
	public HospitalAcknowledgeDTO getHospitalAcknowledgeDTO(HospitalAcknowledge hospitals) {
		HospitalAcknowledgeDTO dest = hospitalAckMapper.map(hospitals);
		return dest;
	}
	
	public Hospitals getHospitalDetails(HospitalAcknowledgeDTO hospitalAcknowledgeDTO) {
		Hospitals dest = hospitalDetailsMapper.mapReverse(hospitalAcknowledgeDTO);
		return dest;
	}
	
	public HospitalAcknowledgeDTO getHospitalDetailsDto(Hospitals hospitals) {
		HospitalAcknowledgeDTO dest = hospitalDetailsMapper.map(hospitals);
		return dest;
	}
	
	public Claim getClaimDetails(HospitalAcknowledgeDTO hospitalAcknowledgeDTO) {
		Claim dest = claimMapper.mapReverse(hospitalAcknowledgeDTO);
		return dest;
	}
	
	public HospitalAcknowledgeDTO getClaimDetailsDto(Claim claim) {
		HospitalAcknowledgeDTO dest = claimMapper.map(claim);
		return dest;
	}
	
	public Preauth getPreauthDetails(HospitalAcknowledgeDTO hospitalAcknowledgeDTO) {
		Preauth dest = preauthMapper.mapReverse(hospitalAcknowledgeDTO);
		return dest;
	}
	
	public HospitalAcknowledgeDTO getPreauthDetailsDto(Preauth preauth) {
		HospitalAcknowledgeDTO dest = preauthMapper.map(preauth);
		return dest;
	}
	
	public Status getStatusDetails(HospitalAcknowledgeDTO hospitalAcknowledgeDTO) {
		Status dest = statusMapper.mapReverse(hospitalAcknowledgeDTO);
		return dest;
	}
	
	public HospitalAcknowledgeDTO getStatusDetailsDto(Status status) {
		HospitalAcknowledgeDTO dest = statusMapper.map(status);
		return dest;
	}
	
	public static HospitalCommunicationUpdateMapper getInstance(){
        if(myObj == null){
            myObj = new HospitalCommunicationUpdateMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
