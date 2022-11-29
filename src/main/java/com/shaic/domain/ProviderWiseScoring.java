package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name = "MAS_PROVIDER_WISE_SCORING")
@NamedQueries({
	@NamedQuery(name = "ProviderWiseScoring.findBycode", query = "select o from ProviderWiseScoring o where o.code =:code")
})
public class ProviderWiseScoring extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PROVIDER_CODE")
	private String code;
	
	
	@Column(name = "HOSPITAL_NAME")
	private String hospitalName;
	
	@Column(name = "HOSPITAL_TYPE")
	private String hospitalType;
	
	@Column(name = "CLAIM_CNT")
	private Long cliamCount;
	
	@Column(name = "SCORED_CLAIM_CNT")
	private Long scoredClaimCount;
	
	@Column(name = "SCORE_PERC")
	private String scorePercentage;
	
	@Column(name = "FINAL_SCORE")
	private Double finalScore;
	
	@Column(name = "FINAL_SCORE_DEFICIENCY")
	private String finalScoreDeficiency;
	
	
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalType() {
		return hospitalType;
	}

	public void setHospitalType(String hospitalType) {
		this.hospitalType = hospitalType;
	}

	public Long getCliamCount() {
		return cliamCount;
	}

	public void setCliamCount(Long cliamCount) {
		this.cliamCount = cliamCount;
	}

	public Long getScoredClaimCount() {
		return scoredClaimCount;
	}

	public void setScoredClaimCount(Long scoredClaimCount) {
		this.scoredClaimCount = scoredClaimCount;
	}

	
	public String getScorePercentage() {
		return scorePercentage;
	}

	public void setScorePercentage(String scorePercentage) {
		this.scorePercentage = scorePercentage;
	}

	public Double getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(Double finalScore) {
		this.finalScore = finalScore;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	public String getFinalScoreDeficiency() {
		return finalScoreDeficiency;
	}

	public void setFinalScoreDeficiency(String finalScoreDeficiency) {
		this.finalScoreDeficiency = finalScoreDeficiency;
	}

	

}
