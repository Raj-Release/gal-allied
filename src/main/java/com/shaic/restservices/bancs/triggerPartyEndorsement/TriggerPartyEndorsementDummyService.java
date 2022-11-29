package com.shaic.restservices.bancs.triggerPartyEndorsement;

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

@Path("/status")
@Stateless
public class TriggerPartyEndorsementDummyService {

private final Logger log = LoggerFactory.getLogger(TriggerPartyEndorsementDummyService.class);
	
	@POST
    @Path("/dummy-triggerPartyEndorsement")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	public String dummyCall()throws JsonParseException, JsonMappingException, IOException, JSONException{
		TriggerPartyEndorsementService checkSts=new TriggerPartyEndorsementService();
		/*String respoonse=checkSts.callTriggerEndorsement();
		return respoonse;*/
		return "";
	}
}
