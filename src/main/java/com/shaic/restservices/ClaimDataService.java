package com.shaic.restservices;

import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.domain.GatewayClaimRequest;

@Path("/entry-claims")
@Stateless
public class ClaimDataService {
	
	@Inject
	GatewayDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(ClaimDataService.class);
	
	@POST
    @Path("/policy-claim")
	@Consumes(MediaType.APPLICATION_JSON)
    public String checkPolicyData(ClaimPojo pojoObj) throws JSONException {
		JSONObject reqStatus = new JSONObject();
		String policyNumber = pojoObj.getPolicyNumber();
		try {
			if(StringUtils.isNotBlank(policyNumber)) {
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
				reqStatus.put(WSConstants.RES_MESSAGE, WSConstants.MSG_EMPTY);
				
				boolean isRequestAlreadyMade = dbService.checkRequest(policyNumber);
				if(!isRequestAlreadyMade) {
					GatewayClaimRequest gatewayEntry = new GatewayClaimRequest();
					gatewayEntry.setPolicyNumber(policyNumber);
					gatewayEntry.setCreatedDate(new Date());
					gatewayEntry.setRequestedApplication("GATEWAY");
					gatewayEntry.setReadFlag("N");
					gatewayEntry.setRespondedFlag("N");
					dbService.persistObject(gatewayEntry);
					log.info("Policy request marked for gateway.");
				}
			} else {
				reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
				reqStatus.put(WSConstants.RES_MESSAGE, WSConstants.MSG_INVALID_POLICY);
			}	
		} catch(Exception exp) {
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put(WSConstants.RES_ERROR, exp.getMessage());
			exp.printStackTrace();
		}
       return reqStatus.toString();
    }
}
