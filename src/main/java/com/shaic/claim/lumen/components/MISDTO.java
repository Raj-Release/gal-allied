package com.shaic.claim.lumen.components;

import com.shaic.arch.table.AbstractTableDTO;

@SuppressWarnings("serial")
public class MISDTO extends AbstractTableDTO{
	private int sno;
	private String queryContent;
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public String getQueryContent() {
		return queryContent;
	}
	public void setQueryContent(String queryContent) {
		this.queryContent = queryContent;
	}
}
