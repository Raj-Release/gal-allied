package com.shaic.claim.reports.agentbrokerreport;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.domain.Intimation;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class AgentBrokerReportMapper {
	
	static AgentBrokerReportMapper myObj;
	private static MapperFacade tableMapper;
	private static MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
	//private static ClassMapBuilder<Intimation, AgentBrokerReportTableDTO> classMapForAgentReport = mapperFactory.classMap(Intimation.class, AgentBrokerReportTableDTO.class);
	private static ClassMapBuilder<Intimation, AgentBrokerReportTableDTO> classMapForAgentReport = null;
	
	 public static void getAllMapValues()  {
		 
		classMapForAgentReport = mapperFactory.classMap(Intimation.class, AgentBrokerReportTableDTO.class);
		
		classMapForAgentReport.field("intimationId", "intimationNo");
		classMapForAgentReport.field("policy.policyNumber", "policyNo");
		classMapForAgentReport.field("policy.smCode", "smCode");
		classMapForAgentReport.field("policy.smName", "smName");
		classMapForAgentReport.field("policy.agentCode", "agentBrokerCode");
		classMapForAgentReport.field("policy.agentName", "agentBrokerName");		
		classMapForAgentReport.field("hospital","hospitalId");
		classMapForAgentReport.field("policy.homeOfficeCode","policyIssueOffice");
		
		classMapForAgentReport.register();
		tableMapper = mapperFactory.getMapperFacade();
		
	}
	public static List<AgentBrokerReportTableDTO> getAgentReportTableObjects(List<Intimation> intimationtList)
    {
	List<AgentBrokerReportTableDTO> agentReportTableObjectList = tableMapper.mapAsList(intimationtList, AgentBrokerReportTableDTO.class);
	return agentReportTableObjectList;
    }
	
	public static AgentBrokerReportMapper getInstance(){
        if(myObj == null){
            myObj = new AgentBrokerReportMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
