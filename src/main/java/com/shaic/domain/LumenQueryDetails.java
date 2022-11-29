package com.shaic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Stage;

/**
 * @author karthikeyan.r
 * The persistent class for the IMS_CLS_LUMEN_QUERY_DTLS database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_QUERY_DTLS")
@NamedQueries({
	@NamedQuery(name="LumenQueryDetails.findAll", query="SELECT m FROM LumenQueryDetails m"),
	@NamedQuery(name="LumenQueryDetails.findByLumenKeyWithQueryKey", query="SELECT m FROM LumenQueryDetails m where m.lumenRequest.key = :lumenReqKey and m.lumenQuery.key in (:lumenQueryKey)"),
	@NamedQuery(name="LumenQueryDetails.findByQueryDetailKey", query="SELECT m FROM LumenQueryDetails m where m.lumenRequest.key = :lumenReqKey and m.lumenQuery.key = :lumenQueryKey and m.key = :queryDetailKey")
})
public class LumenQueryDetails extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_QUERY_DTLS_GENERATOR", sequenceName = "SEQ_LUMEN_QUERY_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_QUERY_DTLS_GENERATOR")
	@Column(name="LUMEN_QUERY_DTLS_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_QUERY_KEY", nullable=false)
	private LumenQuery lumenQuery;
	
	@Column(name = "QUERY_REMARKS")
	private String queryRemarks;
	
	@Column(name = "REPLY_REMARKS")
	private String replyRemarks;
	
	@Column(name = "REPLIED_BY")
	private String repliedBy;	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLIED_DATE")
	private Date repliedDate;	
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STAGE_ID", nullable= true)
	private Stage stage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID", nullable= true)
	private Status status;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "REPLIED_ROLE")
	private String repliedRole;
	

	public Long getKey() {
		return key;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public LumenQuery getLumenQuery() {
		return lumenQuery;
	}

	public String getQueryRemarks() {
		return queryRemarks;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public String getRepliedBy() {
		return repliedBy;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public Stage getStage() {
		return stage;
	}

	public Status getStatus() {
		return status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	public void setLumenQuery(LumenQuery lumenQuery) {
		this.lumenQuery = lumenQuery;
	}

	public void setQueryRemarks(String queryRemarks) {
		this.queryRemarks = queryRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}

	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRepliedRole() {
		return repliedRole;
	}

	public void setRepliedRole(String repliedRole) {
		this.repliedRole = repliedRole;
	}

}
