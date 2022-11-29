package com.shaic.domain;

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

import com.shaic.domain.preauth.Stage;


@Entity
@Table(name="IMS_CLSB_PED_UW_QUERY_DTLS")

@NamedQueries({
	
	@NamedQuery(name="PedQueryDetailsTableData.findByKey", query="SELECT o FROM PedQueryDetailsTableData o where o.key =:key"),
	@NamedQuery(name="PedQueryDetailsTableData.findBypedKey", query="SELECT o FROM PedQueryDetailsTableData o where o.pedQuery.key =:key")
	
})

public class PedQueryDetailsTableData {
	
	@Id
	@SequenceGenerator(name="IMS_CLSB_PED_UW_QUERY_DTLS_KEY_GENERATOR", sequenceName = "SEQ_PED_UW_QRYDTLS_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLSB_PED_UW_QUERY_DTLS_KEY_GENERATOR") 
	
	@Column(name = "PED_UW_QRYDTLS_KEY")
	private Long key;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PED_UW_QUERY_KEY")
	private PedQueryTable pedQuery;

	@Column(name = "QUERY_ID")
	private Double queryId;
	
	@Column(name = "QUERY_TYPE")
	private String queryType;

	@Column(name = "QUERY_DESCRIPTION")
	private String queryDesc;
	
	@Column(name = "QUERY_CODE")
	private String queryCode;
	
	@Column(name = "QUERY_REMARKS")
	private String queryRemarks;
	
	@Column(name = "RAISED_BY_USER")
	private String raisedByUser;
	
	@Column(name = "RAISED_BY_ROLE")
	private String raisedByRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RAISED_DATE")
	private Date raisedDate;

	@Column(name = "REPLY_REMARKS")
	private String replyRemarks;
	
	@Column(name = "REPLIED_BY_USER")
	private String repliedByUser;

	@Column(name = "REPLIED_BY_ROLE")
	private String repliedByRole;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLIED_DATE")
	private Date repliedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;	
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STATUS_ID")
	private Status status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STAGE_ID")
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
