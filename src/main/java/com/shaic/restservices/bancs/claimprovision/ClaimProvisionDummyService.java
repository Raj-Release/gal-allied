package com.shaic.restservices.bancs.claimprovision;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;

import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.restservices.GLXDocRequest;

@Path("/claims")
@Stateless
public class ClaimProvisionDummyService {

	@POST
    @Path("/dummyClaimProvisionService")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public void callClaimProvisionService(GLXDocRequest detailObj) throws JsonParseException, JsonMappingException, IOException, JSONException {
//		List<PremPolicy> response = ClaimProvisionService.callPolicySummaryService(null);
		BancsSevice.getInstance().getPolicyDetailsMethod("");
//		return response;
	}
}
