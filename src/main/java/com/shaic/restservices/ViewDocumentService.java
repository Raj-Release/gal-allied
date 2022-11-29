package com.shaic.restservices;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAFileUtils;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;

@Path("/entry-claims")
@Stateless
public class ViewDocumentService{

	private final Logger log = LoggerFactory.getLogger(DocumentUploadService.class);

	@SuppressWarnings({ "deprecation", "serial" })
	@POST
	@Path("/doc-view")
	@Consumes(MediaType.APPLICATION_JSON)
	public String checkPolicyData(ViewDocumentData docObj) throws JSONException, IOException {
		JSONObject reqStatus = new JSONObject();
		String binaryData = null;
		byte[] bytes = null;
		final String docUrl = SHAFileUtils.viewFileByToken(docObj.getDocToken());
		if(docUrl != null) {
			Embedded e = new Embedded();
			e.setSizeFull();
			e.setType(Embedded.TYPE_BROWSER);
			StreamResource.StreamSource source = new StreamResource.StreamSource() {
				public InputStream getStream() {
					InputStream is = null;
					URL u = null;
					URLConnection urlConnection = null;
					try {
						u =  new URL(docUrl);
						urlConnection =  u.openConnection();
						is = urlConnection.getInputStream();
					}catch(Exception exp){
						System.out.println("Exception occurred while get stream from dms...");
						exp.printStackTrace();
					}
					return is;
				}
			};
			bytes = IOUtils.toByteArray(source.getStream());
		}
		binaryData = Base64.encodeBase64String(bytes);
		if(binaryData != null && binaryData.length() > 0){
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_SUCCESS);
			reqStatus.put(WSConstants.RES_MESSAGE, binaryData);
			log.info("Document content sent succesfully");
		}else{
			reqStatus.put(WSConstants.REQ_STATUS, WSConstants.STATUS_FAILURE);
			reqStatus.put(WSConstants.RES_MESSAGE, "");
			log.info("Document content is empty");
		}
		return reqStatus.toString();
	}

}
