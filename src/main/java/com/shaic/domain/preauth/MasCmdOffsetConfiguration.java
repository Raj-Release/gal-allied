package com.shaic.domain.preauth;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_SEC_CLUB_MEM_TIME_CONFIG")
@NamedQueries({
	@NamedQuery(name="MasCmdOffsetConfiguration.findOffsetByPriority", query="SELECT m FROM MasCmdOffsetConfiguration m where m.activeStatus is not null and m.activeStatus = 1  and UPPER(m.clubMember) like :clubMemeberType"),
	
})
public class MasCmdOffsetConfiguration {

	@Id
	@Column(name="CLUB_MEMBER_KEY")
	private Long key;
	
	@Column(name="CLUB_MEMBER")
	private String clubMember;
	
	@Column(name="TIME_LAPSE")
	private Long timeLapse;
	
	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getClubMember() {
		return clubMember;
	}

	public void setClubMember(String clubMember) {
		this.clubMember = clubMember;
	}

	public Long getTimeLapse() {
		return timeLapse;
	}

	public void setTimeLapse(Long timeLapse) {
		this.timeLapse = timeLapse;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
}
