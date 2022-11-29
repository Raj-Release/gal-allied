package com.shaic.domain.outpatient;

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
import com.shaic.domain.Insured;

@Entity
@Table(name = "IMS_CLS_OP_HC_DETAILS")
@NamedQueries({
	@NamedQuery(name="OPHCDetails.findAll", query="SELECT r FROM OPHCDetails r"),
	@NamedQuery(name ="OPHCDetails.findByKey",query="SELECT r FROM OPHCDetails r WHERE r.key = :primaryKey"),
	@NamedQuery(name ="OPHCDetails.findByHealthCheckupKey",query="SELECT r FROM OPHCDetails r WHERE r.opHealthCheckup is not null and r.opHealthCheckup.key = :healthCheckupKey"),
})
public class OPHCDetails extends AbstractEntity{

	private static final long serialVersionUID = -797939768188532132L;
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_HC_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_OP_HC_DETAILS_Key", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OP_HC_DETAILS_KEY_GENERATOR" ) 
	@Column(name="OP_HC_DTLS_KEY", updatable=false)
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TREATMENT_DATE")
	private Date treatmentDate;
	
	@Column(name = "REASON_FOR_VISIT")
	private String reasonForVisit;
	
	@JoinColumn(name = "INSURED_KEY", nullable = true, unique = true, insertable = true)
	@OneToOne
	private Insured insured; 
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
		
	}

	public String getReasonForVisit() {
		return reasonForVisit;
	}

	public void setReasonForVisit(String reasonForVisit) {
		this.reasonForVisit = reasonForVisit;
	}

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public Date getTreatmentDate() {
		return treatmentDate;
	}

	public void setTreatmentDate(Date treatmentDate) {
		this.treatmentDate = treatmentDate;
	}

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

}
