package com.shaic.restservices;

import java.util.HashMap;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAFileUtils;

@Path("/entry-claims")
@Stateless
public class DocumentUploadService{
	
	private final Logger log = LoggerFactory.getLogger(DocumentUploadService.class);
	
	@SuppressWarnings("unchecked")
	@POST
    @Path("/doc-upload")
	@Consumes(MediaType.APPLICATION_JSON)
    public String checkPolicyData(UploadDocumentData docObj) throws JSONException {
		JSONObject reqStatus = new JSONObject();
		String responseValue = null;
		WeakHashMap<String, Object> dmsResponse = SHAFileUtils.sendFileToDMSServer(docObj.getDocumentFileName(), Base64.decodeBase64(docObj.getDocumentContent()));

		if(String.valueOf(dmsResponse.get("status")).equals("true")){
			responseValue = "documentToken:"+dmsResponse.get("fileKey")+",intimationNumber:"+docObj.getIntimationNumber()+",documentKey:"+docObj.getDocumentKey()+
					",documentFileName:"+docObj.getDocumentFileName()+",docUniqueKey:"+docObj.getDocUniqueKey();
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
			reqStatus.put("documentToken", dmsResponse.get("fileKey"));
			reqStatus.put("intimationNumber", docObj.getIntimationNumber());
			reqStatus.put("documentKey", docObj.getDocumentKey());
			reqStatus.put("documentFileName", docObj.getDocumentFileName());
			reqStatus.put("docUniqueKey", docObj.getDocUniqueKey());			
			log.info("Document uploaded successfully for the intimation");
		}else{
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
			reqStatus.put("documentToken", "");
			reqStatus.put("intimationNumber", "");
			reqStatus.put("documentKey", "");
			reqStatus.put("documentFileName", "");
			reqStatus.put("docUniqueKey", "");
			log.info("Document uploaded failed for the intimation");
		}
		return reqStatus.toString();
	}
	
}
