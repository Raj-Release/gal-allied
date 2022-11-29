package com.shaic.domain;

import java.io.Serializable;

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

import com.shaic.arch.fields.dto.AbstractEntity;


/**
 * The persistent class for the IMS_CLS_INSURED_NOMINEE database table.
 * 
 */
@Entity
@Table(name="IMS_CLS_INSURED_NOMINEE")

@NamedQueries({
	@NamedQuery(name="NomineeDetails.findAll", query="SELECT o FROM NomineeDetails o"),
	@NamedQuery(name="NomineeDetails.findByInsuredKey", query="SELECT o FROM NomineeDetails o where o.insured.key =:insuredKey")
})

public class NomineeDetails extends AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="IMS_CLS_INSURED_NOMINEE_KEY_GENERATOR", sequenceName = "SEQ_NOMINEE_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_INSURED_NOMINEE_KEY_GENERATOR" ) 
	@Column(name="NOMINEE_KEY")
	private Long key;
	
	@JoinColumn(name = "INSURED_KEY",nullable = false,updatable=false)
	@OneToOne
	private Insured insured;
	
	@Column(name = "NOMINEE_NAME")
	private String nomineeName;
	
	@Column(name="NOMINEE_AGE")
	private Long nomineeAge;
	
	@Column(name = "RELATION_CODE")
	private String relationshipCode;
	
	@JoinColumn(name="RELATION_ID")
	@OneToOne
	private MastersValue relationId;
	
	@Column(name="NOMINEESHAREPERCENT")
	private Long nomineeSharePercent;
	
	@Column(name="DELETED_FLAG")
	private String deletedFlag;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Insured getInsured() {
		return insured;
	}

	public void setInsured(Insured insured) {
		this.insured = insured;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public Long getNomineeAge() {
		return nomineeAge;
	}

	public void setNomineeAge(Long nomineeAge) {
		this.nomineeAge = nomineeAge;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public MastersValue getRelationId() {
		return relationId;
	}

	public void setRelationId(MastersValue relationId) {
		this.relationId = relationId;
	}

	public Long getNomineeSharePercent() {
		return nomineeSharePercent;
	}

	public void setNomineeSharePercent(Long nomineeSharePercent) {
		this.nomineeSharePercent = nomineeSharePercent;
	}

	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

}
