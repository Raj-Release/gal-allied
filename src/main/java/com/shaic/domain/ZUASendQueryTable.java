package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="ZUA_SEND_QUERY")
@NamedQueries({
@NamedQuery(name="ZUASendQueryTable.findByPolicyNumber", query="SELECT z FROM ZUASendQueryTable z where z.policyNo = :policyNumber")
})
public class ZUASendQueryTable  extends AbstractEntity {
	
	@Id	
	@Column(name="RSQ_POL_SYS_ID")
	private Long key;
	
	@Column(name="RSQ_POL_NO")
	private String policyNo;
	
	@Column(name="RSQ_QUERY_RAISE_UID")
	private String queryRasiedBy;
	
	@Column(name="RSQ_FLAG")
	private String queryFlag;
	

	@Column(name="RSQ_RAISE_REMARKS")
	private String queryRaiseRemarks;
	
	@Column(name="RSQ_QUERY_CODE")
	private String queryCode;
	
	@Column(name="RSQ_REP_REMARKS")
	private String queryRepRemarks;
	
	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getQueryRasiedBy() {
		return queryRasiedBy;
	}

	public void setQueryRasiedBy(String queryRasiedBy) {
		this.queryRasiedBy = queryRasiedBy;
	}

	public String getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(String queryFlag) {
		this.queryFlag = queryFlag;
	}

	public String getQueryRaiseRemarks() {
		return queryRaiseRemarks;
	}

	public void setQueryRaiseRemarks(String queryRaiseRemarks) {
		this.queryRaiseRemarks = queryRaiseRemarks;
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public String getQueryRepRemarks() {
		return queryRepRemarks;
	}

	public void setQueryRepRemarks(String queryRepRemarks) {
		this.queryRepRemarks = queryRepRemarks;
	}

	
}
