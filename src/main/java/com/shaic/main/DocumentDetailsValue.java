package com.shaic.main;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDetailsValue {
	
	@JsonProperty("docName")
	private String docName;
	
	@JsonProperty("urlId")
	private String urlId;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("ecmUrl")
	private String ecmUrl;
	
	@JsonProperty("serverId")
	private String serverId;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEcmUrl() {
		return ecmUrl;
	}

	public void setEcmUrl(String ecmUrl) {
		this.ecmUrl = ecmUrl;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	

}
