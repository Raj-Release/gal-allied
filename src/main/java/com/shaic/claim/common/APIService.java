package com.shaic.claim.common;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.taglib.json.util.JSONObject;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.PremiaService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource.Builder;

@Stateless
public class APIService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(APIService.class);
	
	public String updateProvisionAmountToPremia(String input) {
		try {
			
			//Bancs Changes Start
			JSONObject jsonObject = new JSONObject(input);
			String policyNo = jsonObject.getString("PolicyNo");
			String intimationNo = jsonObject.getString("IntimationNo");
			String currentProvisionAmount = jsonObject.getString("ProvisionAmount");
			Policy policyObj = null;
			Builder builder = null;
			String output = null;
			
			if(policyNo != null){
				DBCalculationService dbService = new DBCalculationService();
				policyObj = dbService.getPolicyObject(policyNo);
				if (policyObj != null) {
					if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
						output = BancsSevice.getInstance().provisionAmountBancsUpdate(policyNo,intimationNo, currentProvisionAmount, input);
					}else{
						builder = PremiaService.getInstance().getBuilderForProvison();
						output = builder.post(new GenericType<String>() {}, input);
					}
				}
				
			}
			
			//Bancs Changes End
			
		//	Builder builder = PremiaService.getInstance().getBuilderForProvison();
			log.info("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END -->" + output);
			return output;
		} catch(Exception e) {
			log.error("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END --> Exception is --- > "  + e.getMessage());
			e.printStackTrace();
		}
		
		return "";
		
	
	}

	
	
}
