package com.shaic.claim.legal.processconsumerforum.page.ombudsman;

import java.io.Serializable;

import com.shaic.claim.legal.processconsumerforum.page.consumerforum.IntimationDetailsDTO;

public class ConsumerForumDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1921176310533140299L;

	private IntimationDetailsDTO intimationDetailsDTO;
	
	private OmbudsmanDetailsDTO ombudsmanDetailsDTO;
	

	public IntimationDetailsDTO getIntimationDetailsDTO() {
		return intimationDetailsDTO;
	}

	public void setIntimationDetailsDTO(IntimationDetailsDTO intimationDetailsDTO) {
		this.intimationDetailsDTO = intimationDetailsDTO;
	}

	public OmbudsmanDetailsDTO getOmbudsmanDetailsDTO() {
		return ombudsmanDetailsDTO;
	}

	public void setOmbudsmanDetailsDTO(OmbudsmanDetailsDTO ombudsmanDetailsDTO) {
		this.ombudsmanDetailsDTO = ombudsmanDetailsDTO;
	}

}
