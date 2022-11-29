package com.shaic.restservices.bancs.sendpedquery;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.PedQueryTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.GatewayDBService;
import com.shaic.restservices.WSConstants;
import com.shaic.restservices.bancs.GLXWBService;

@Path("/claims")
@Stateless
public class SendPEDQueryService {
	
	@Inject
	GLXWBService wbService;
	
	private final Logger log = LoggerFactory.getLogger(SendPEDQueryService.class);
	
	@POST
    @Path("/sendPEDQuery")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(SendPEDQueryDetails pedObject, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		ObjectMapper mapper = new ObjectMapper();
		log.info("SEND PED QUERY REQ  : "+mapper.writeValueAsString(pedObject));

		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			if(!StringUtils.isBlank(authString)){
				if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
/*					if(StringUtils.isBlank(pedObject.getServiceTransactionId())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Service Transaction ID is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}*/
					if(StringUtils.isBlank(pedObject.getPolicyNumber())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Policy Number is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && StringUtils.isBlank(pedObject.getMemberCode())){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Member Code is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else if(!validationErrorFlag && (pedObject.getWorkItemID() == null)){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - WorkItem ID is Empty or null");
						reqStatus.put(WSConstants.ERROR_CODE, "600");
					}else{
						if(!validationErrorFlag && (pedObject.getQueries() != null && pedObject.getQueries().size() == 0)){
							validationErrorFlag = true;
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - No Complex Type Queries to update");
							reqStatus.put(WSConstants.ERROR_CODE, "600");
						}else{
							if(!validationErrorFlag && (pedObject.getQueries() != null && pedObject.getQueries().size() > 0)){

								for(SendPEDQueryComplexTypeDetails complexTypeDetails : pedObject.getQueries()){
									if(complexTypeDetails.getQueryId() == null){
										docErrorMsg.append(" Query Id is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getQueryType())){
										docErrorMsg.append(" Query Type is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getQueryDescription())){
										docErrorMsg.append(" Query Description Id is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getQueryCode())){
										docErrorMsg.append(" Query Code Name is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getQueryDetails())){
										docErrorMsg.append(" Query Details is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getRaisedByUser())){
										docErrorMsg.append(" Raised By User is Empty or null ");
									}
									if(StringUtils.isBlank(complexTypeDetails.getRaisedByRole())){
										docErrorMsg.append(" Raised By Role is Empty or null ");
									}

									if(!StringUtils.isBlank(docErrorMsg.toString())){
										break;
									}else{
										continue;
									}
								}
							}
						}
					}

					if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
						PedQueryTable uploadStatus = wbService.updateSendPEDQueryService(pedObject);
						PedQueryDetailsTableData queryDetailStatus = wbService.updateSendPEDQueryDetailService(uploadStatus, pedObject);
						if(uploadStatus != null && queryDetailStatus != null){
							reqStatus.put(WSConstants.ERROR_CODE, "200");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_SUCCESS);	
						}else{
							reqStatus.put(WSConstants.ERROR_CODE, "600");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+uploadStatus+"-Deatils-"+queryDetailStatus);
						}
					}
				}else{
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
					reqStatus.put(WSConstants.ERROR_CODE, "600");
				}
			}else{
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
				reqStatus.put(WSConstants.ERROR_CODE, "600");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_FAILURE +"-"+exp.getMessage());
			reqStatus.put(WSConstants.ERROR_CODE, "600");
			reqStatus.put(WSConstants.RES_ERROR, exp.getMessage());
			log.info("Exception occured in Send PED Query Update service"+exp.getMessage());
		}	
		log.info(reqStatus.toString());
		return reqStatus.toString();
    }
}