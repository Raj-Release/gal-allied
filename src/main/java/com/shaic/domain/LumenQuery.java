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
 * The persistent class for the IMS_CLS_LUMEN_QUERY database table.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name="IMS_CLS_LUMEN_QUERY")
@NamedQueries({
	@NamedQuery(name="LumenQuery.findAll", query="SELECT m FROM LumenQuery m"),
	@NamedQuery(name="LumenQuery.findByLumenKey", query="SELECT m FROM LumenQuery m where m.lumenRequest.key = :lumenReqKey order by m.key asc"),
	@NamedQuery(name="LumenQuery.findByKeyWithDate", query="SELECT m FROM LumenQuery m where m.lumenRequest.key = :lumenReqKey and m.key = :queryKey and m.queryRaisedDate is not null and m.repliedDate is null"),	
	@NamedQuery(name="LumenQuery.findByLumenKeyWithQryKey", query="SELECT m FROM LumenQuery m where m.lumenRequest.key = :lumenReqKey and m.key = :queryKey")
})
public class LumenQuery extends AbstractEntity implements Serializable  {

	@Id
	@SequenceGenerator(name="IMS_CLS_LUMEN_QUERY_GENERATOR", sequenceName = "SEQ_LUMEN_QUERY_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LUMEN_QUERY_GENERATOR")
	@Column(name="LUMEN_QUERY_KEY")
	private Long key;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@Column(name = "QUERY_TYPE")
	private String queryType;
	
	@Column(name = "QUERY_RAISED_BY")
	private String queryRaisedBy;
	
	@Column(name = "QUERY")
	private String query;	
	
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
	
	// new columns
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "QUERY_RAISED_DATE")
	private Date queryRaisedDate;
	
	@Column(name = "QUERY_RAISED_ROLE")
	private String queryRaisedRole;
	
	@Column(name = "REPLIED_BY")
	private String repliedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLIED_DATE")
	private Date repliedDate;
	
	@Column(name = "REPLIED_ROLE")
	private String repliedRole;
	
	@Column(name = "REPLY_REMARKS")
	private String replyRemarks;
	
	public Long getKey() {
		return key;
	}

	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public String getQueryType() {
		return queryType;
	}

	public String getQueryRaisedBy() {
		return queryRaisedBy;
	}

	public String getQuery() {
		return query;
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

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public void setQueryRaisedBy(String queryRaisedBy) {
		this.queryRaisedBy = queryRaisedBy;
	}

	public void setQuery(String query) {
		this.query = query;
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

	public Date getQueryRaisedDate() {
		return queryRaisedDate;
	}

	public void setQueryRaisedDate(Date queryRaisedDate) {
		this.queryRaisedDate = queryRaisedDate;
	}

	public String getQueryRaisedRole() {
		return queryRaisedRole;
	}

	public void setQueryRaisedRole(String queryRaisedRole) {
		this.queryRaisedRole = queryRaisedRole;
	}

	public String getRepliedBy() {
		return repliedBy;
	}

	public void setRepliedBy(String repliedBy) {
		this.repliedBy = repliedBy;
	}

	public Date getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(Date repliedDate) {
		this.repliedDate = repliedDate;
	}

	public String getRepliedRole() {
		return repliedRole;
	}

	public void setRepliedRole(String repliedRole) {
		this.repliedRole = repliedRole;
	}

	public String getReplyRemarks() {
		return replyRemarks;
	}

	public void setReplyRemarks(String replyRemarks) {
		this.replyRemarks = replyRemarks;
	}
	
}
