package com.shaic.claim.pcc.dto;

import com.shaic.arch.table.AbstractTableDTO;

public class PCCUploadedFileDocsDTO extends AbstractTableDTO {
	
	private static final long serialVersionUID = 6516581852249166232L;

	private String fileName;
	
	private Long docToken;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getDocToken() {
		return docToken;
	}

	public void setDocToken(Long docToken) {
		this.docToken = docToken;
	}

	
	
	

}
