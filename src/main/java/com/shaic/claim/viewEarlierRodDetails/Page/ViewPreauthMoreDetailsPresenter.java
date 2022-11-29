package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.intimation.create.dto.PolicyDto;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;

@ViewInterface(PreauthMoreDetailsView.class)
public class ViewPreauthMoreDetailsPresenter extends AbstractMVPPresenter<PreauthMoreDetailsView> {
	

	
	private static final long serialVersionUID = 1L;
	
	
	public static final String SET_HOSPITALIZATION = "set hospital details for amount claimed table in view preauth";
	
	public static final String SET_VALUE = "set sublimit and SI restriction Values";
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	protected Map<String, Object> referenceData = new HashMap<String, Object>();
	
	
	public void getHospitalizationDetails(
			@Observes @CDIEvent(SET_HOSPITALIZATION) final ParameterDTO parameters) {
		PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
		PolicyDto policyDTO = preauthDTO.getPolicyDto();
		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
				,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
		if (sumInsured == 0) {
			sumInsured = policyDTO.getTotalSumInsured();
		}
         Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
		
		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
			try{
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
					hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
							preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
				}else{
					hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
							preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
				}
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			try{
				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
					hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
				}else{
					
					String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
					
					/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
					if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
						policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
					}
					
					hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		referenceData.put("claimDBDetails", hospitalizationDetails);
		view.setHospitalizationDetails(hospitalizationDetails);
		
	}
	
     public void setEditReferenceData(@Observes @CDIEvent(SET_VALUE) final ParameterDTO parameters){
		
		Integer approvedAmount = (Integer) parameters.getPrimaryParameter();
		
		view.setApprovedAmountField(approvedAmount);
		
	}
     
     public Map<Integer, Object> getHospitalizationDetails(PreauthDTO preauthDTO, HospitalService hospitalService,DBCalculationService dbCalculationService) {
 		//PreauthDTO preauthDTO = (PreauthDTO) parameters.getPrimaryParameter();
 		
 		PreauthDTO preauthDto = new PreauthDTO();
 		if(hospitalService==null){
 			hospitalService = new HospitalService();
 		}
 		Hospitals hospitalById = hospitalService.getHospitalById(preauthDTO.getHospitalKey());
 		PolicyDto policyDTO = preauthDTO.getPolicyDto();
 		//String insuredAge = getInsuredAge(policyDTO.getInsuredDob());
 		if(dbCalculationService==null){
 			dbCalculationService = new DBCalculationService();
 		}
 		Double sumInsured = dbCalculationService.getInsuredSumInsured(preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId().toString(), preauthDTO.getPolicyDto().getKey()
 				,preauthDTO.getNewIntimationDTO().getInsuredPatient().getLopFlag());
 		if (sumInsured == 0) {
 			sumInsured = policyDTO.getTotalSumInsured();
 		}
          Map<Integer, Object> hospitalizationDetails = new HashMap<Integer, Object>();
 		
 		if(preauthDTO.getPreauthDataExtractionDetails().getSection() != null){
 			try{
 				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
 					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
 							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
 					hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
 							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
 							preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
 				}else{
 					hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
 							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), 
 							preauthDTO.getNewIntimationDTO().getKey(),preauthDTO.getPreauthDataExtractionDetails().getSection().getId(),"A");
 				}
 			
 			
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}else{
 			try{
 				if(ReferenceTable.getGMCProductList().containsKey(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())){
 					sumInsured = dbCalculationService.getInsuredSumInsuredForGMC(preauthDTO.getNewIntimationDTO().getPolicy().getKey(),
 							preauthDTO.getNewIntimationDTO().getInsuredPatient().getKey(),preauthDTO.getNewIntimationDTO().getPolicy().getSectionCode());
 					hospitalizationDetails = dbCalculationService.getHospitalizationDetailsForGMC(policyDTO.getProduct().getKey(),
 							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,"0");
 				}else{
 					
 					String policyPlan = preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getPolicy().getPolicyPlan() : "0";
					
					/*if(ReferenceTable.MEDI_CLASSIC_BASIC_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey()) || ReferenceTable.MEDI_CLASSIC_GOLD_PRODUCT_KEY.equals(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getKey())) {*/
 					if(preauthDTO.getNewIntimationDTO().getPolicy().getProduct() != null 
							&& (((SHAConstants.PRODUCT_CODE_72.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_87.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
									|| SHAConstants.PRODUCT_CODE_81.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode())
									|| SHAConstants.PROD_PAC_PRD_012.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
							|| ((SHAConstants.PRODUCT_CODE_84.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()) ||
									SHAConstants.PRODUCT_CODE_91.equalsIgnoreCase(preauthDTO.getNewIntimationDTO().getPolicy().getProduct().getCode()))
								&& preauthDTO.getNewIntimationDTO().getPolicy().getProductType().getKey().equals(ReferenceTable.INDIVIDUAL_POLICY)))) {
						policyPlan = preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() != null ? preauthDTO.getNewIntimationDTO().getInsuredPatient().getPolicyPlan() : "0";
					}
 					
 					hospitalizationDetails = dbCalculationService.getHospitalizationDetails(policyDTO.getProduct().getKey(),
 							sumInsured, hospitalById.getCityClass(), preauthDTO.getNewIntimationDTO().getInsuredPatient().getInsuredId(), preauthDTO.getNewIntimationDTO().getKey(),0l,policyPlan);
 				}
 			
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}
 		if(referenceData==null){
 			referenceData = new HashMap<String, Object>();
 		}
 		
// 		return  (Map<String, Object>) referenceData.put("claimDBDetails", hospitalizationDetails);
 		return hospitalizationDetails;
 	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
