package com.shaic.claim.fvrgrading.page;

import java.util.List;

import com.shaic.claim.preauth.wizard.dto.FvrGradingDetailsDTO;

public class FvrReportGradingPageDto {

	private String gradingRemarks;
	
	private List<FvrGradingDetailsDTO> fvrGradingDTO;

	public List<FvrGradingDetailsDTO> getFvrGradingDTO() {
		return fvrGradingDTO;
	}

	public void setFvrGradingDTO(List<FvrGradingDetailsDTO> fvrGradingDTO) {
		this.fvrGradingDTO = fvrGradingDTO;
	}

	public String getGradingRemarks() {
		return gradingRemarks;
	}

	public void setGradingRemarks(String gradingRemarks) {
		this.gradingRemarks = gradingRemarks;
	}
	
}
