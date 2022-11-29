/**
 * 
 */
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

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name = "IMS_CLS_REIMBURS_BENEFIT_DTLS")
@NamedQueries({
	@NamedQuery(name = "ReimbursementBenefitsDetails.findAll", query = "SELECT i FROM ReimbursementBenefitsDetails i"),
	@NamedQuery(name = "ReimbursementBenefitsDetails.findByBenefitsKey", query = "SELECT o FROM ReimbursementBenefitsDetails o where o.reimbursementBenefits.key = :benefitsKey"),
	@NamedQuery(name = "ReimbursementBenefitsDetails.findByKey", query = "SELECT o FROM ReimbursementBenefitsDetails o where o.key = :benefitsDetailsKey")
})
public class ReimbursementBenefitsDetails extends AbstractEntity {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 593451890346327177L;

	@Id
	@SequenceGenerator(name="IMS_REIMBURSEMENT_BENEFITS__DETAILS_KEY_GENERATOR", sequenceName = "SEQ_REIMBURS_BENEFITS_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_REIMBURSEMENT_BENEFITS__DETAILS_KEY_GENERATOR" ) 
	@Column(name="CLAIM_BENEFIT_DETAILS_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name="CLAIM_BENEFIT_KEY", nullable=false)
	private ReimbursementBenefits reimbursementBenefits;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENGAGED_FROM")
	private Date engagedFrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENGAGED_TO")
	private Date engagedTo;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public ReimbursementBenefits getReimbursementBenefits() {
		return reimbursementBenefits;
	}

	public void setReimbursementBenefits(ReimbursementBenefits reimbursementBenefits) {
		this.reimbursementBenefits = reimbursementBenefits;
	}

	public Date getEngagedFrom() {
		return engagedFrom;
	}

	public void setEngagedFrom(Date engagedFrom) {
		this.engagedFrom = engagedFrom;
	}

	public Date getEngagedTo() {
		return engagedTo;
	}

	public void setEngagedTo(Date engagedTo) {
		this.engagedTo = engagedTo;
	}
	
	

}
