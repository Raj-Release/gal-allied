package com.shaic.claim.preauth.wizard.dto;

public class NewProcedureDTO  extends ProcedureDTO
{
	private boolean emailFlag;

	public boolean isEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(boolean emailFlag) {
		this.emailFlag = emailFlag;
	}
	
}
