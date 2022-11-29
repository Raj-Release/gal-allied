package com.shaic.claim.pedrequest.approve.bancspedrequest;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.domain.PedQueryTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;

public class BancsPEDQueryDetailTableDTO extends AbstractSearchDTO implements Serializable {
	
	private Long key;

	private PedQueryTable pedQuery;

	private Double queryId;
	
	private String queryType;

	private String queryDesc;
	
	private String queryCode;
	
	private String queryRemarks;
	
	private String raisedByUser;
	
	private String raisedByRole;
	
	private Date raisedDate;

	private String replyRemarks;
	
	private String repliedByUser;

	private String repliedByRole;
	
	private Date repliedDate;
	
	private Date createdDate;	
	
	private String createdBy;

	private Date modifiedDate;
	
	private String modifiedBy;
	
	private Status status;	

	private Stage stage;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public PedQueryTable getPedQuery() {
		return pedQuery;
	}

	public void setPedQuery(PedQueryTable pedQuery) {
		this.pedQuery = pedQuery;
	}

	public Double getQueryId() {
		return queryId;
	}

	public void setQueryId(Double queryId) {
		this.queryId = queryId;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryDesc() {
		return queryDesc;
	}

	public void setQueryDesc(String queryDesc) {
		this.queryDesc = queryDesc;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public String getRaisedByUser() {
		return raisedByUser;
	}

	public void setRaisedByUser(String raisedByUser) {
		this.raisedByUser = raisedByUser;
	}

	public String getRaisedByRole() {
		return raisedByRole;
	}

	public void setRaisedByRole(String raisedByRole) {
		this.raisedByRole = raisedByRole;
	}

	public Date getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public String getRepliedByUser() {
		return repliedByUser;
	}

	public void setRepliedByUser(String repliedByUser) {
		this.repliedByUser = repliedByUser;
	}

	public String getRepliedByRole() {
		return repliedByRole;
	}

	public void setRepliedByRole(String repliedByRole) {
		this.repliedByRole = repliedByRole;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
