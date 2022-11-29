package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "MAS_REPORT_CONFIG")
@NamedQueries({
		@NamedQuery(name = "MasReportConfig.findbyName", query = "SELECT i FROM MasReportConfig i where i.reportName = :reportName"),
})
public class MasReportConfig implements Serializable {
	
	@Id
	@Column(name="REPORT_KEY")
	private Long key;
	
	
	@Column(name = "REPORT_NAME")
	private String reportName;
	
	
	@Column(name = "REPORT_DURATION")
	private String reportDuration;
	
	@Column(name = "REMARKS")
	private String remarks;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportDuration() {
		return reportDuration;
	}

	public void setReportDuration(String reportDuration) {
		this.reportDuration = reportDuration;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
