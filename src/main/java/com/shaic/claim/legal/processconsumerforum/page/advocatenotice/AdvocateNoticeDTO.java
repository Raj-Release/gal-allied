package com.shaic.claim.legal.processconsumerforum.page.advocatenotice;

import java.io.Serializable;

import com.shaic.claim.legal.processconsumerforum.page.consumerforum.CaseDetailsDTO;
import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;

public class AdvocateNoticeDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1657870501626109263L;

	public AdvocateNoticeDTO() {
		
		intimationDetailsDTO = new IntimationDetailsDTO();
		caseDetailsDTO = new CaseDetailsDTO();
		
	}

	private IntimationDetailsDTO intimationDetailsDTO;
	
	private CaseDetailsDTO caseDetailsDTO;
	
	private String userName;

	public IntimationDetailsDTO getIntimationDetailsDTO() {
		return intimationDetailsDTO;
	}

	public void setIntimationDetailsDTO(IntimationDetailsDTO intimationDetailsDTO) {
		this.intimationDetailsDTO = intimationDetailsDTO;
	}

	public CaseDetailsDTO getCaseDetailsDTO() {
		return caseDetailsDTO;
	}

	public void setCaseDetailsDTO(CaseDetailsDTO caseDetailsDTO) {
		this.caseDetailsDTO = caseDetailsDTO;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
