package com.shaic.restservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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



@Path("/glx-hospital")
@Stateless
public class HospitalDocUploadService{
	
	@Inject
	GatewayDBService dbService;
	
	private final Logger log = LoggerFactory.getLogger(HospitalDocUploadService.class);
	
	@POST
    @Path("/doc-upload")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public String checkPolicyData(HospitalDocDetails hpObject, @HeaderParam("authorization") String authString) throws JsonParseException, JsonMappingException, IOException, JSONException {
		JSONObject reqStatus = new JSONObject();		
		ObjectMapper mapper = new ObjectMapper();
//		log.info("HP REQ : "+mapper.writeValueAsString(hpObject));
		log.info("HP REQ : "+hpObject.getIntimationNumber());
		log.info("HP REQ : "+hpObject.getProviderCode());
		log.info("HP REQ : "+hpObject.getHospitalName());
		log.info("HP REQ : "+hpObject.getClaimedAmount());
		log.info("HP REQ : "+hpObject.getUploadDate());
		log.info("HP REQ : "+hpObject.getFileTypeId());
		if(hpObject.getDocumentDetails() != null && !hpObject.getDocumentDetails().isEmpty()){
			for(HospitalDocUploadDetails rec : hpObject.getDocumentDetails()){
				log.info("HP REQ : "+rec.getDocId());
				log.info("HP REQ : "+rec.getFileName());
				log.info("HP REQ : "+rec.getFileTypeId());
				log.info("HP REQ : "+rec.getFileTypeName());
			}
		}
		
		try{
			boolean validationErrorFlag = false;
			StringBuilder docErrorMsg = new StringBuilder();
			authString =  authString.replace("Bearer=", "");
			if(authString.trim().equals(BPMClientContext.HP_AUTH_KEY)){
				if(hpObject.getIntimationNumber().equals("") || hpObject.getIntimationNumber().equalsIgnoreCase("null")){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Intimation Number is Empty or null");
				}else if(!validationErrorFlag && (hpObject.getFileTypeId().equals("") || hpObject.getFileTypeId().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - FileType Id is Empty or null");
				}else if(!validationErrorFlag && (hpObject.getHospitalName().equals("") || hpObject.getHospitalName().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Hospital Name is Empty or null");
				}else if(!validationErrorFlag && (hpObject.getProviderCode().equals("") || hpObject.getProviderCode().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Provider Code is Empty or null");
				}else if(!validationErrorFlag && (hpObject.getUploadDate().equals("") || hpObject.getUploadDate().equalsIgnoreCase("null"))){
					validationErrorFlag = true;
					reqStatus.put(WSConstants.REQ_STATUS, "Error - Upload Date is Empty or null");
				}else{
					if(!validationErrorFlag && (hpObject.getDocumentDetails() != null && hpObject.getDocumentDetails().size() == 0)){
						validationErrorFlag = true;
						reqStatus.put(WSConstants.REQ_STATUS, "Error - No Documents to upload");
					}else{
						if(!validationErrorFlag && (hpObject.getDocumentDetails() != null && hpObject.getDocumentDetails().size() > 0)){

							for(HospitalDocUploadDetails reqDocDetails : hpObject.getDocumentDetails()){
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
					List<String> successStatus = new ArrayList<String>();
					successStatus.add("Successfully Completed");
					successStatus.add("Successfully Uploaded");
					String uploadStatus = dbService.uploadHospitalDocumentService(hpObject);
					if(successStatus.contains(uploadStatus)){ //uploadStatus.equals("Successfully Uploaded")
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
			exp.printStackTrace();
			log.info("Exception occured in HP doc upl service"+exp.getMessage());
		}	
		return reqStatus.toString();
    }
}
