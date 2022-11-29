package com.shaic.claim.registration;

import com.shaic.claim.ClaimDto;
import com.shaic.newcode.wizard.dto.SearchTableDTO;

public class SearchClaimCoveringLetterTableDto extends SearchTableDTO{
	
	private ClaimDto claimDto;
	
	//private HumanTask humanTask;
	
	public ClaimDto getClaimDto() {
		return claimDto;
	}
	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}
	/*public HumanTask getHumanTask() {
		return humanTask;
	}
	public void setHumanTask(HumanTask humanTask) {
		this.humanTask = humanTask;
	}*/
	
	

}
