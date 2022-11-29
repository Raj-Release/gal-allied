/**
 * 
 */
package com.shaic.restservices;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.shaic.arch.SHAFileUtils;

/**
 * @author ntv.vijayar
 *
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "document-url")
@Path("/document-url")
@Stateless
public class DMSDocumentUrlService {
	
	
	 	@POST
	 	@Path("/documentToken/{documentToken}")
	//	@Path("/documentToken")
	    @Consumes("application/json")
	   //public String getDocumentURL(DocumentURLPojo pojo) {
	 	public String getDocumentURL(@PathParam("documentToken") String documentToken) {
		 
		 //String documentToken = pojo.getDocumentToken();
		 String imageUrl = SHAFileUtils.viewFileByToken(String.valueOf(documentToken));
		 return imageUrl;
				
	 }

}
