package com.shaic.arch.table;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author GokulPrasath.A
 *
 */
@Entity
@Table(name = "IMS_CLS_CVC_AUDIT_TEAMS")
@NamedQueries({
@NamedQuery(name = "AuditTeam.findAll", query = "SELECT i FROM AuditTeam i"),
@NamedQuery(name = "AuditTeam.findByAuditKey", query="SELECT i FROM AuditTeam i where i.auditKey = :auditKey"),
@NamedQuery(name = "AuditTeam.findByAuditActiveStatus", query="SELECT i FROM AuditTeam i where i.auditKey = :auditKey and i.activeStatus = 'Y'"),
})
public class AuditTeam extends AbstractEntity{
	@Id
	@SequenceGenerator(name="IMS_AUDIT_TEAM_KEY_GENERATOR", sequenceName = "SEQ_AUDIT_TEAM_ID", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_AUDIT_TEAM_KEY_GENERATOR" ) 
	@Column(name="AUDIT_TEAM_KEY", updatable=false)
	private Long key;

	@Column(name="CVC_AUD_KEY")
	private Long auditKey;
	
	@Column(name="AUDIT_TEAM")
	private String auditTeam;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name="ACTIVE_STATUS")
	private String activeStatus;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
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

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getAuditKey() {
		return auditKey;
	}

	public void setAuditKey(Long auditKey) {
		this.auditKey = auditKey;
	}

	public String getAuditTeam() {
		return auditTeam;
	}

	public void setAuditTeam(String auditTeam) {
		this.auditTeam = auditTeam;
	}
}
