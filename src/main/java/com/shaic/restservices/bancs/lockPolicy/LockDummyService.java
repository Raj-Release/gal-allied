package com.shaic.restservices.bancs.lockPolicy;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Path("/claims")
@Stateless
public class LockDummyService {

	private final Logger log = LoggerFactory.getLogger(LockDummyService.class);
	
	@POST
    @Path("/dummy-service")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String callConsumingService(String claimNumber) throws JsonParseException, JsonMappingException, IOException, JSONException {
		LockPolicyIntegrationService lop=new LockPolicyIntegrationService();
		String response = lop.viewBancsIntegrationLock(null,null);
		return response;
	}
	
}
