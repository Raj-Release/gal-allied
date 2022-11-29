package com.shaic.restservices;

import javax.ejb.Stateless;
import javax.ws.rs.Path;



@Path("/entry-req")
@Stateless
public class PostTestService{
	
	/*private final Logger log = LoggerFactory.getLogger(PostTestService.class);
	
	@POST
    @Path("/test-req")
	@Consumes(MediaType.APPLICATION_JSON)
    public String checkPolicyData(String testParam) throws JsonParseException, JsonMappingException, IOException, JSONException {
		System.out.println("testParam : "+testParam);
		JSONObject reqStatus = new JSONObject();
		try{
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
		}catch(Exception exp){
			log.info("Exception occurred in query receipt service"+ exp.getMessage());
		}	
		return reqStatus.toString();
    }	*/
}
