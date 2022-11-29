package com.shaic.restservices.bancs.checkendomnt64vbstatus;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.claim.policy.search.ui.PremUnique64VBStatusDetails;
@Path("/status")
@Stateless
public class GetUniqueVB64DummyService {

	

	
private final Logger log = LoggerFactory.getLogger(EndorsementDummyService.class);
	
	@POST
    @Path("/dummy-service-uvb")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<PremUnique64VBStatusDetails>  dummyCall()throws JsonParseException, JsonMappingException, IOException, JSONException{
		Check64VBService checkSts=new Check64VBService();
		List<PremUnique64VBStatusDetails>  respoonse=checkSts.viewGetUnique64VB(null);
		return respoonse;
	}





}
