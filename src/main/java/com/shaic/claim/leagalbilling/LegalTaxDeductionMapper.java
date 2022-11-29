package com.shaic.claim.leagalbilling;

import java.util.List;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;

import com.shaic.ClaimRemarksDocs;
import com.shaic.arch.SHAConstants;
import com.shaic.newcode.wizard.domain.MappingUtil;
import com.shaic.reimbursement.claims_alert.search.ClaimsAlertDocsDTO;

public class LegalTaxDeductionMapper {
		
	
		
	static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().compilerStrategy(new EclipseJdtCompilerStrategy()).build();

	private static BoundMapperFacade<LegalBillingDTO, LegalTaxDeduction> taxDeductionMap;
		
	private static MapperFacade legalTaxMapper;
	
	private static ClassMapBuilder<LegalBillingDTO, LegalTaxDeduction> legalTaxMap = null;
	
	static LegalTaxDeductionMapper myObj;
	
	public static void getAllMapValues()  {
		
		legalTaxMap = mapperFactory.classMap(LegalBillingDTO.class, LegalTaxDeduction.class);	
	
		legalTaxMap.field("key", "key");
		legalTaxMap.field("intimationKey", "intimationKey");
		legalTaxMap.field("claimKey", "claimKey");
		legalTaxMap.field("rodKey", "rodKey");
		legalTaxMap.field("awardAmount", "awardAmount");
		legalTaxMap.field("cost", "cost");
		legalTaxMap.field("compensation", "compensation");		
		legalTaxMap.field("interestApplicable", "interestApplicable");
		legalTaxMap.field("interestRate", "interestRate");
		legalTaxMap.field("interestfromDate", "interestfromDate");		
		legalTaxMap.field("interesttoDate", "interesttoDate");
		legalTaxMap.field("totalnoofdays", "totalnoofdays");
		legalTaxMap.field("interestCurrentClaim", "interestCurrentClaim");
		legalTaxMap.field("interestOtherClaim", "interestOtherClaim");
		legalTaxMap.field("panNo", "panNo");
		legalTaxMap.field("awardAmountRemark", "awardAmountRemark");
		legalTaxMap.field("costRemark", "costRemark");
		legalTaxMap.field("compensationRemark", "compensationRemark");
		legalTaxMap.field("intrCurrentClaimRemark", "intrCurrentClaimRemark");
		legalTaxMap.field("intrOtherClaimRemark", "intrOtherClaimRemark");
		legalTaxMap.field("intrPayRemark", "intrPayRemark");
		legalTaxMap.field("tdsRemark", "tdsRemark");
		legalTaxMap.field("intrPayTDSRemark", "intrPayTDSRemark");
		legalTaxMap.field("totalPayRemark", "totalPayRemark");
		legalTaxMap.field("activeStatus", "activeStatus");
		legalTaxMap.field("createdBy", "createdBy");
		legalTaxMap.field("createdDate", "createdDate");
		legalTaxMap.field("panDetails", "panFlag");
		legalTaxMap.field("tdsAmount", "tdsAmount");
		legalTaxMap.field("tdsPercentge", "tdsPercentge");

		legalTaxMap.register();
					
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Double>() {
			@Override
			public Double convert(String source, Type<? extends Double> destinationType,
					MappingContext arg2) {
				// TODO Auto-generated method stub
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0.0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Double(source);
			}
		});

		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Long>() {
			@Override
			public Long convert(String source,
					Type<? extends Long> destinationType,
					MappingContext mappingContext) {
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Long(source);
			}			
		});
		
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Integer>() {

			@Override
			public Integer convert(String source,
					Type<? extends Integer> destinationType,
					MappingContext mappingContext) {
				source = source.equals("") || source.isEmpty() || source == "" || source == null ? "0" : source;
				//	source = source.replace("[a-zA-Z\\]", "");
				return new Integer(source);
			}			
		});
		
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<Boolean, String>() {

			@Override
			public String convert(Boolean source, Type<? extends String> destinationType,MappingContext arg2) {				
				if(source !=null && source){
					return SHAConstants.YES_FLAG;
				}
				return SHAConstants.N_FLAG;
			}
		});	
		mapperFactory.getConverterFactory().registerConverter(new CustomConverter<String, Boolean>() {

			@Override
			public Boolean convert(String source, Type<? extends Boolean> destinationType,MappingContext arg2) {				
				if(source !=null && 
						!source.isEmpty() && source.equals(SHAConstants.YES_FLAG)){
					return true;
				}
				return false;
			}
		});	
		legalTaxMapper = mapperFactory.getMapperFacade();
		taxDeductionMap = mapperFactory.getMapperFacade(LegalBillingDTO.class, LegalTaxDeduction.class);
		System.out.println("LegalTaxDeduction MAPPER CREATED -------------------------------> " +taxDeductionMap);
	}
	
	@SuppressWarnings("unused")
	public static LegalTaxDeduction getLegalTaxDeductionFromDTO(LegalBillingDTO legalBillingDTO)
	{
		System.out.println("LegalTaxDeduction MAPPER CREATED -------------------------------> " +taxDeductionMap);
		LegalTaxDeduction legalTaxDeduction = taxDeductionMap.map(legalBillingDTO);	
		return legalTaxDeduction;
	}
	
	@SuppressWarnings("unused")
	public static  List<LegalTaxDeduction> getLegalTaxDeductionFromDTOs(List<LegalBillingDTO> legalBillingDTOs)
	{
		List<LegalTaxDeduction> taxDeductions = legalTaxMapper.mapAsList(legalBillingDTOs, LegalTaxDeduction.class);
		return taxDeductions;
	}
	
	@SuppressWarnings("unused")
	public static LegalBillingDTO getDTOFromTaxDeduction(LegalTaxDeduction taxDeduction)
	{
		LegalBillingDTO billingDTO = taxDeductionMap.mapReverse(taxDeduction);
		return billingDTO;
	}
	
	@SuppressWarnings("unused")
	public static  List<LegalBillingDTO> getPortablityDetails(List<LegalTaxDeduction> taxDeductions)
	{
		List<LegalBillingDTO> mapAsList = legalTaxMapper.mapAsList(taxDeductions, LegalBillingDTO.class);
		return mapAsList;
	}
	
	public static LegalTaxDeductionMapper getInstance(){
        if(myObj == null){
            myObj = new LegalTaxDeductionMapper();
            getAllMapValues();
        }
        return myObj;
	 }

}
