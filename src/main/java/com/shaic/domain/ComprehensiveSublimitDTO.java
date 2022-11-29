package com.shaic.domain;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class ComprehensiveSublimitDTO extends AbstractTableDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2011694563880359025L;
	
	private Long limitId;
	
	private String section;
	
	private String subLimitName;
	
	private Double subLimitAmt;
	
	private Double includingCurrentClaimAmt;
	
	private Double includingCurrentClaimBal;
	
	private Double excludingCurrentClaimAmt;
	
	private Double excludingCurrentClaimBal;
	
	private Integer sno;
	
	
	
	public ComprehensiveSublimitDTO() {
		
	}

	public ComprehensiveSublimitDTO(Long limitIdNumber, String name, Double limitAmnt, Double limittotalUtilizationAmnt, Double limitUtilizationAmnt, String secName)
	{
		this.limitId = limitIdNumber;
		this.subLimitName = name;
		this.subLimitAmt = limitAmnt;
		this.excludingCurrentClaimAmt = limitUtilizationAmnt;
		this.includingCurrentClaimAmt = limittotalUtilizationAmnt;
		Double includingCurrentClaimBalance = limitAmnt - limittotalUtilizationAmnt;
		Double excludingCurrentClaimBalance = limitAmnt - limitUtilizationAmnt;
		this.includingCurrentClaimBal = (includingCurrentClaimBalance < 0) ? 0 : includingCurrentClaimBalance;
		this.excludingCurrentClaimBal = (excludingCurrentClaimBalance < 0) ? 0 : excludingCurrentClaimBalance;
		this.section = secName;
		
	}
	
	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}
	
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSubLimitName() {
		return subLimitName;
	}
	public void setSubLimitName(String subLimitName) {
		this.subLimitName = subLimitName;
	}
	public Double getSubLimitAmt() {
		return subLimitAmt;
	}
	public void setSubLimitAmt(Double subLimitAmt) {
		this.subLimitAmt = subLimitAmt;
	}
	public Double getIncludingCurrentClaimAmt() {
		return includingCurrentClaimAmt;
	}
	public void setIncludingCurrentClaimAmt(Double includingCurrentClaimAmt) {
		this.includingCurrentClaimAmt = includingCurrentClaimAmt;
	}
	public Double getIncludingCurrentClaimBal() {
		return includingCurrentClaimBal;
	}
	public void setIncludingCurrentClaimBal(Double includingCurrentClaimBal) {
		this.includingCurrentClaimBal = includingCurrentClaimBal;
	}
	public Double getExcludingCurrentClaimAmt() {
		return excludingCurrentClaimAmt;
	}
	public void setExcludingCurrentClaimAmt(Double excludingCurrentClaimAmt) {
		this.excludingCurrentClaimAmt = excludingCurrentClaimAmt;
	}
	public Double getExcludingCurrentClaimBal() {
		return excludingCurrentClaimBal;
	}
	public void setExcludingCurrentClaimBal(Double excludingCurrentClaimBal) {
		this.excludingCurrentClaimBal = excludingCurrentClaimBal;
	}


	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
	            return false;
	        } else if (getLimitId() == null) {
	            return obj == this;
	        } else {
	            return getLimitId().equals(((ComprehensiveSublimitDTO) obj).getLimitId());
	        }
	    }

	    @Override
	    public int hashCode() {
	        if (limitId != null) {
	            return limitId.hashCode();
	        } else {
	            return super.hashCode();
	        }
	    }

		public Integer getSno() {
			return sno;
		}

		public void setSno(Integer sno) {
			this.sno = sno;
		}

	

}
