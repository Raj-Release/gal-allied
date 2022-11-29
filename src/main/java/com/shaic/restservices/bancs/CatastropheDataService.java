package com.shaic.restservices.bancs;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.domain.CatastropheData;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.restservices.WSConstants;


	@Path("/claims")
	@Stateless
	public class CatastropheDataService {
		
		@Inject
		GLXWBService wbService;
		
		private final Logger log = LoggerFactory.getLogger(CatastropheDataService.class);
		
		@POST
	    @Path("/sendCatastrophicData")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public String checkCatastropheData(CatastropheDataRequest catObj , @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
			JSONObject reqStatus = new JSONObject();
			ObjectMapper mapper = new ObjectMapper();
			log.info("SEND CATASTROPHIE REQ  : "+mapper.writeValueAsString(catObj));
			try{
				boolean validationErrorFlag = false;
				StringBuilder docErrorMsg = new StringBuilder();
				if(!StringUtils.isBlank(authString)){
					if(authString.trim().equals(BPMClientContext.BANCS_EXP_WS_AUTH_KEY)){
						if(StringUtils.isBlank(catObj.getCatReferenceNo())){
							validationErrorFlag = true;
							reqStatus.put(WSConstants.ERROR_CODE, "600");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - CatReferenceNo is Empty or null");
						}else if(!validationErrorFlag && (StringUtils.isBlank(catObj.getCatDescription()))){
							validationErrorFlag = true;
							reqStatus.put(WSConstants.ERROR_CODE, "600");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - CatDescription is Empty or null");
						}else if(!validationErrorFlag && (StringUtils.isBlank(catObj.getStartDate()))){
							validationErrorFlag = true;
							reqStatus.put(WSConstants.ERROR_CODE, "600");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Start Date is Empty or null");
						}else if(!validationErrorFlag && (StringUtils.isBlank(catObj.getEndDate()))){
							validationErrorFlag = true;
							reqStatus.put(WSConstants.ERROR_CODE, "600");
							reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - End Date is Empty or null");
						}
						if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
							Boolean catastropheData = wbService.getCatastropheByRefernceNo(catObj.getCatReferenceNo());
							if(!catastropheData){
								CatastropheData endData = wbService.CatastropheDatas(catObj);
								if(endData != null){
									reqStatus.put(WSConstants.ERROR_CODE, "200");
									reqStatus.put(WSConstants.ERROR_DESCRIPTION, WSConstants.STATUS_SUCCESS);	
								}else{
									reqStatus.put(WSConstants.ERROR_CODE, "600");
									reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+endData);
								}
							}
							else{
								reqStatus.put(WSConstants.ERROR_CODE, "600");
								reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error"+" - "+"CatReferenceNo is Already Exist");
							}

						}
					}else{
						reqStatus.put(WSConstants.ERROR_CODE, "600");
						reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
					}
				}else{
					reqStatus.put(WSConstants.ERROR_CODE, "600");
					reqStatus.put(WSConstants.ERROR_DESCRIPTION, "Error - Authentication Error, Invalid Authorization Key");
				}
			}catch(Exception exp){
				reqStatus.put(WSConstants.ERROR_CODE, "600");
				reqStatus.put(WSConstants.ERROR_DESCRIPTION, exp.getMessage());
				log.error("Exception occured in while fetching Catastrophic details"+exp.getMessage());
			}	
			log.info(reqStatus.toString());
			return reqStatus.toString();
	    }
		

	}