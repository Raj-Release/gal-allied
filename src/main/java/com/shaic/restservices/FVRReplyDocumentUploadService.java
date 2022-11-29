package com.shaic.restservices;

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
import com.shaic.ims.bpm.claim.BPMClientContext;



@Path("/str-glx-fvr")
@Stateless
public class FVRReplyDocumentUploadService{
	
	@Inject
	GatewayDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(FVRReplyDocumentUploadService.class);
	
	@POST
    @Path("/documentdetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(FVRDocDetails fvrObject, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		ObjectMapper mapper = new ObjectMapper();
	//	log.info("FVR REQ : "+mapper.writeValueAsString(fvrObject));
		log.info("FVR REQ : "+fvrObject.getIntimationNumber());
		log.info("FVR REQ : "+fvrObject.getFvrNo());
		log.info("FVR REQ : "+fvrObject.getFvrReplyDate());
		log.info("FVR REQ : "+fvrObject.getUploadDate());
		log.info("FVR REQ : "+fvrObject.getFileTypeId());
		if(fvrObject.getDocumentDetails() != null && !fvrObject.getDocumentDetails().isEmpty()){
			for(FVRDocUploadDetails rec : fvrObject.getDocumentDetails()){
				log.info("FVR REQ : "+rec.getDocId());
				log.info("FVR REQ : "+rec.getFileName());
				log.info("FVR REQ : "+rec.getFileTypeId());
				log.info("FVR REQ : "+rec.getFileTypeName());
			}
		}
		
		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			authString =  authString.replace("Bearer=", "");
			if(authString.trim().equals(BPMClientContext.FVR_AUTH_KEY)){
				if(fvrObject.getIntimationNumber().equals("") || fvrObject.getIntimationNumber().equalsIgnoreCase("null")){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Intimation Number is Empty or null");
				}else if(!validationErrorFlag && (fvrObject.getFileTypeId().equals("") || fvrObject.getFileTypeId().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - FileType Id is Empty or null");
				}else if(!validationErrorFlag && (fvrObject.getFvrNo().equals("") || fvrObject.getFvrNo().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - FVR No is Empty or null");
				}else if(!validationErrorFlag && (fvrObject.getFvrReplyDate().equals("") || fvrObject.getFvrReplyDate().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - FVR Replied Date is Empty or null");
				}else if(!validationErrorFlag && (fvrObject.getUploadDate().equals("") || fvrObject.getUploadDate().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Upload Date is Empty or null");
				}else{
					if(!validationErrorFlag && (fvrObject.getDocumentDetails() != null && fvrObject.getDocumentDetails().size() == 0)){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.REQ_STATUS, "Error - No Documents to upload");
					}else{
						if(!validationErrorFlag && (fvrObject.getDocumentDetails() != null && fvrObject.getDocumentDetails().size() > 0)){

							for(FVRDocUploadDetails reqDocDetails : fvrObject.getDocumentDetails()){
								if(reqDocDetails.getDocId().equals("") || reqDocDetails.getDocId().equals("null")){
									docErrorMsg.append(" Doc Id is Empty or null ");
								}
								if(reqDocDetails.getFileName().equals("") || reqDocDetails.getFileName().equals("null")){
									docErrorMsg.append(" File Name is Empty or null ");
								}
								if(reqDocDetails.getFileTypeId().equals("") || reqDocDetails.getFileTypeId().equals("null")){
									docErrorMsg.append(" FileType Id is Empty or null ");
								}
								if(reqDocDetails.getFileTypeName().equals("") || reqDocDetails.getFileTypeName().equals("null")){
									docErrorMsg.append(" FileType Name is Empty or null ");
								}
								if(reqDocDetails.getFileContent().equals("") || reqDocDetails.getFileContent().equals("null")){
									docErrorMsg.append(" FileContent is Empty or null ");
								}

								if(!StringUtils.isBlank(docErrorMsg.toString())){
									break;
								}else{
									continue;
								}
							}
							reqStatus.put(WSConstants.REQ_STATUS, "Error - "+docErrorMsg.toString());
						}
					}
				}

				if(!validationErrorFlag && StringUtils.isBlank(docErrorMsg.toString())){
					String uploadStatus = dbService.uploadFVRDocumentService(fvrObject);
					if(uploadStatus.equals("Successfully Uploaded")){
						reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
					}else{
						reqStatus.put(WSConstants.REQ_STATUS, "Error"+" - "+uploadStatus);
					}
				}
			}else{
				reqStatus.put(WSConstants.REQ_STATUS, "Error - Authentication Error, Invalid Authorization Key");
			}
		}catch(Exception exp){
			reqStatus.put(WSConstants.REQ_STATUS, "Error"+" - "+exp.getMessage());
			log.info("Exception occured in FVR doc upl service"+exp.getMessage());
		}	
		return reqStatus.toString();
    }
}
