package com.shaic.restservices.bancs.tiggerEndorsement;

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

import com.shaic.restservices.bancs.checkendomnt64vbstatus.EndorsementDummyService;

@Path("/claims")
@Stateless
public class TriggerEndorsemenDummyService {

private final Logger log = LoggerFactory.getLogger(EndorsementDummyService.class);
	
	@POST
    @Path("/dummy-tgrendo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	public String dummyCall()throws JsonParseException, JsonMappingException, IOException, JSONException{
		TriggerEndorsementService checkSts=new TriggerEndorsementService();
		String respoonse=checkSts.callTriggerEndorsement();
		return respoonse;
	}
}
