package com.shaic.newcode.wizard.domain;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.domain.Policy;

public class PolicyMapper {

	private MapperFacade mapper;

	public PolicyMapper() {
		MapperFactory mapperFactory = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<Policy, PolicyDto> classMap = mapperFactory.classMap(
				Policy.class, PolicyDto.class);

		classMap.field("key", "key");
		classMap.field("activeStatus", "activeStatus");
		classMap.field("homeOfficeCode", "homeOfficeCode");
		classMap.field("lobId", "lobId");
		classMap.field("homeOfficeCode", "officeCode");
		classMap.field("policyFromDate", "policyFromDate");
		classMap.field("policyNumber", "policyNumber");
		classMap.field("policyStatus", "policyStatus");
		classMap.field("policyToDate", "policyToDate");
		classMap.field("policyPlan", "policyPlan");
		
		classMap.field("proposerFirstName", "proposerFirstName");
		classMap.field("policyStatus", "status");
		classMap.field("totalSumInsured", "totalSumInsured");
		classMap.field("cummulativeBonus", "cummulativeBonus");
		classMap.field("policyType", "policyType");
		classMap.field("productType", "productType");
		classMap.field("rechargeSI", "rechargeSI");
		classMap.field("restoredSI", "restoredSI");
		classMap.field("sumInsuredII", "sectionII");
		classMap.field("copay", "copay");
		classMap.field("stampDuty", "stampDuty");
		classMap.field("stampDuty", "stampDuty");
		classMap.field("gmcPolicyType", "gmcPolicyType");
		classMap.field("linkPolicyNumber", "linkPolicyNumber");
		classMap.field("polEmailId", "polEmailId");
		classMap.field("policyTerm", "policyTerm");
		

		this.mapper = mapperFactory.getMapperFacade();
	}

	public Policy getPolicy(PolicyDto policyDto) {
		Policy dest = mapper.map(policyDto, Policy.class);
		return dest;
	}

	public PolicyDto getPolicyDto(Policy policy) {
		PolicyDto dest = mapper.map(policy, PolicyDto.class);
		return dest;
	}

}
