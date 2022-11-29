package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name="IMS_CLS_PREV_HOSPITALIZATION")
@NamedQueries({
@NamedQuery(name="PreviousClaimedHospitalization.findAll", query="SELECT r FROM PreviousClaimedHospitalization r"),
@NamedQuery(name="PreviousClaimedHospitalization.findByClaimedHistoryKey", query="SELECT r FROM PreviousClaimedHospitalization r WHERE r.previousClaimedHistory is not null and r.previousClaimedHistory.key = :claimedHistoryKey")
})
public class PreviousClaimedHospitalization extends AbstractEntity {
	private static final long serialVersionUID = 421659646016454674L;

	@Id
	@SequenceGenerator(name="IMS_CLS_PREV_HOSPITALIZATION_KEY_GENERATOR", sequenceName = "SEQ_PREV_HOSPITALIZATION_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_PREV_HOSPITALIZATION_KEY_GENERATOR" )
	@Column(name="PREVIOUS_HOSPITALIZATION_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="PREV_CLAIMED_HISTORY_KEY", nullable=false)
	private PreviousClaimedHistory previousClaimedHistory;
	
	@Temporal(TemporalType.DATE)
	@Column(name="HOSPITALIZATION_DATE")
	private Date hospitalizationDate; 
	
	@Column(name="DIAGNOSIS")
	private String diagnosis;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public PreviousClaimedHistory getPreviousClaimedHistory() {
		return previousClaimedHistory;
	}

	public void setPreviousClaimedHistory(
			PreviousClaimedHistory previousClaimedHistory) {
		this.previousClaimedHistory = previousClaimedHistory;
	}

	public Date getHospitalizationDate() {
		return hospitalizationDate;
	}

	public void setHospitalizationDate(Date hospitalizationDate) {
		this.hospitalizationDate = hospitalizationDate;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	
}
