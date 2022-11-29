package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.newcode.wizard.domain.MappingUtil;

public class CoverDetailsTobillDetailsMapper {

	private static MapperFacade tableMapper;
	private static MapperFacade tableMapper1;
	private static MapperFacade tableMapper2;
	
	static CoverDetailsTobillDetailsMapper myObj;
	
	public static void getAllMapValues() {
		
		MapperFactory mapperFactor = MappingUtil.getMapperFactory(true);
		ClassMapBuilder<AddOnCoversTableDTO, BillEntryDetailsDTO> addOnCoversClassMap = mapperFactor.classMap(AddOnCoversTableDTO.class, BillEntryDetailsDTO.class);
		ClassMapBuilder<OptionalCoversDTO, BillEntryDetailsDTO> optionalCoversClassMap = mapperFactor.classMap(OptionalCoversDTO.class, BillEntryDetailsDTO.class);
		ClassMapBuilder<TableBenefitsDTO, BillEntryDetailsDTO> benefitsClassMap = mapperFactor.classMap(TableBenefitsDTO.class, BillEntryDetailsDTO.class);
		
		addOnCoversClassMap.field("addonCovers.value","itemName");
		addOnCoversClassMap.field("billNo", "billNo");
		addOnCoversClassMap.field("billDate", "billDate");
		addOnCoversClassMap.field("billAmount", "itemValue");
		addOnCoversClassMap.field("deduction", "totalDisallowances");
		addOnCoversClassMap.field("approvedAmount", "approvedAmountForAssessmentSheet");
		addOnCoversClassMap.field("remarks", "deductibleOrNonPayableReason");
		
		benefitsClassMap.field("classification.value","itemName");
		benefitsClassMap.field("billNo", "billNo");
		benefitsClassMap.field("billDate", "billDate");
		benefitsClassMap.field("billAmount", "itemValue");
		benefitsClassMap.field("deduction", "totalDisallowances");
		benefitsClassMap.field("approvedAmount", "approvedAmountForAssessmentSheet");
		benefitsClassMap.field("reasonForDeduction", "deductibleOrNonPayableReason");
				
		optionalCoversClassMap.field("optionalCover.value","itemName");
		optionalCoversClassMap.field("billNo", "billNo");
		optionalCoversClassMap.field("billDate", "billDate");
		optionalCoversClassMap.field("totalClaimed", "itemValue");	
		
		// CR2019100 -  Deduction for Medical Extention ROD 
		optionalCoversClassMap.field("deduction", "totalDisallowances");	
		
		optionalCoversClassMap.field("appAmt", "approvedAmountForAssessmentSheet");
		optionalCoversClassMap.field("remarks", "deductibleOrNonPayableReason");
		
		addOnCoversClassMap.register();
		optionalCoversClassMap.register();
		benefitsClassMap.register();
		
		tableMapper = mapperFactor.getMapperFacade();
		tableMapper1 = mapperFactor.getMapperFacade();
		tableMapper2 = mapperFactor.getMapperFacade();
	}
	
	
	public static List<BillEntryDetailsDTO> getaddOnCovresDTO(List<AddOnCoversTableDTO> addOncoversData){
		List<BillEntryDetailsDTO> addOnCoversList = tableMapper.mapAsList(addOncoversData, BillEntryDetailsDTO.class);
		
		if(addOnCoversList != null && !addOnCoversList.isEmpty()){
			for (BillEntryDetailsDTO billEntryDetailsDTO : addOnCoversList) {
				
				for (AddOnCoversTableDTO addOnCoversDTO : addOncoversData) {
					if(addOnCoversDTO.getAddonCovers().getValue().equalsIgnoreCase(billEntryDetailsDTO.getItemName())){
						Double approvedAmountForAssessmentSheet = billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() != null ? billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() : 0d;				
						Double totalDisallowances = billEntryDetailsDTO.getItemValue() != null && billEntryDetailsDTO.getItemValue() != 0 ? billEntryDetailsDTO.getItemValue() - approvedAmountForAssessmentSheet : 0d;
						//billEntryDetailsDTO.setApprovedAmountForAssessmentSheet(approvedAmountForAssessmentSheet);		
						billEntryDetailsDTO.setTotalDisallowances(totalDisallowances);
						break;
					}
				}				
			}
		}
		return addOnCoversList;
		
	}
	
	public static List<BillEntryDetailsDTO> getOptionalCoversDTO(List<OptionalCoversDTO> addOncoversData){
		List<BillEntryDetailsDTO> optionalCoversList = tableMapper1.mapAsList(addOncoversData, BillEntryDetailsDTO.class);
		
		if(optionalCoversList != null && !optionalCoversList.isEmpty()){
			for (BillEntryDetailsDTO billEntryDetailsDTO : optionalCoversList) {
				
				for (OptionalCoversDTO addOnCoversDTO : addOncoversData) {
					if(addOnCoversDTO.getOptionalCover().getValue().equalsIgnoreCase(billEntryDetailsDTO.getItemName())){
						Double totalDisallowances = billEntryDetailsDTO.getItemValue() != null && billEntryDetailsDTO.getItemValue() != 0 ? billEntryDetailsDTO.getItemValue() - (billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() != null ? billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() : 0 ) : 0d;
						billEntryDetailsDTO.setTotalDisallowances(totalDisallowances);
						break;
					}
				}				
			}
		}
		return optionalCoversList;
		
	}
	
	public static List<BillEntryDetailsDTO> getBenefitsDTO(List<TableBenefitsDTO> addOncoversData){
		List<BillEntryDetailsDTO> benefitsList = tableMapper2.mapAsList(addOncoversData, BillEntryDetailsDTO.class);
		
		if(benefitsList != null && !benefitsList.isEmpty()){
			for (BillEntryDetailsDTO billEntryDetailsDTO : benefitsList) {
				
				for (TableBenefitsDTO tableBenefitsDTO : addOncoversData) {
					if(tableBenefitsDTO.getClassification().getValue().equalsIgnoreCase(billEntryDetailsDTO.getItemName())){
						Double approvedAmountForAssessmentSheet = (tableBenefitsDTO.getEligibleAmount() != null && tableBenefitsDTO.getEligibleAmount().intValue() != 0 && billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() != null && tableBenefitsDTO.getEligibleAmount().intValue() < billEntryDetailsDTO.getApprovedAmountForAssessmentSheet() ? tableBenefitsDTO.getEligibleAmount() : billEntryDetailsDTO.getApprovedAmountForAssessmentSheet());
//						Double approvedAmountForAssessmentSheet = (tableBenefitsDTO.getEligibleAmount() != null && tableBenefitsDTO.getEligibleAmount().intValue() != 0 ? tableBenefitsDTO.getEligibleAmount() : billEntryDetailsDTO.getApprovedAmountForAssessmentSheet());				
						Double totalDisallowances = billEntryDetailsDTO.getItemValue() != null && billEntryDetailsDTO.getItemValue() != 0 ? billEntryDetailsDTO.getItemValue() - approvedAmountForAssessmentSheet : 0d;
						billEntryDetailsDTO.setApprovedAmountForAssessmentSheet(approvedAmountForAssessmentSheet);		
						billEntryDetailsDTO.setTotalDisallowances(totalDisallowances);
						break;
					}
				}				
			}
			
		}
		
		return benefitsList;
		
	}
	
	public static CoverDetailsTobillDetailsMapper getInstance(){
        if(myObj == null){
            myObj = new CoverDetailsTobillDetailsMapper();
            getAllMapValues();
        }
        return myObj;
	 }
}
