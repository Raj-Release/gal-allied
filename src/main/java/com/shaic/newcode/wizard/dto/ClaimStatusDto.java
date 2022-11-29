package com.shaic.newcode.wizard.dto;

import com.shaic.claim.ClaimDto;

public class ClaimStatusDto {
	
	private NewIntimationDto newIntimationDto;
	
	private ClaimDto claimDto;
	
	private CashlessDetailsDto cashlessDetailsDto;

	public NewIntimationDto getNewIntimationDto() {
		return newIntimationDto;
	}

	public void setNewIntimationDto(NewIntimationDto newIntimationDto) {
		this.newIntimationDto = newIntimationDto;
	}

	public ClaimDto getClaimDto() {
		return claimDto;
	}

	public void setClaimDto(ClaimDto claimDto) {
		this.claimDto = claimDto;
	}

	public CashlessDetailsDto getCashlessDetailsDto() {
		return cashlessDetailsDto;
	}

	public void setCashlessDetailsDto(CashlessDetailsDto cashlessDetailsDto) {
		this.cashlessDetailsDto = cashlessDetailsDto;
	}	
	
}
