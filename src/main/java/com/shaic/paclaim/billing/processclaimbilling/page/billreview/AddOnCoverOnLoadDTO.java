package com.shaic.paclaim.billing.processclaimbilling.page.billreview;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

public class AddOnCoverOnLoadDTO extends AbstractTableDTO implements Serializable{
	
	private Long additionalCoverKey;
	
	private String coverName;

	public Long getAdditionalCoverKey() {
		return additionalCoverKey;
	}

	public void setAdditionalCoverKey(Long additionalCoverKey) {
		this.additionalCoverKey = additionalCoverKey;
	}

	public String getCoverName() {
		return coverName;
	}

	public void setCoverName(String coverName) {
		this.coverName = coverName;
	}

}
