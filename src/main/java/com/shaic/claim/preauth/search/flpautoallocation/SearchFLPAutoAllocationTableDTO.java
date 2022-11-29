package com.shaic.claim.preauth.search.flpautoallocation;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.premedical.search.ProcessPreMedicalTableDTO;
import com.shaic.claim.process.premedical.enhancement.search.SearchPreMedicalProcessingEnhancementTableDTO;
import com.shaic.claim.registration.ackhoscomm.search.AbstractSearchDTO;
import com.shaic.cmn.login.ImsUser;

public class SearchFLPAutoAllocationTableDTO extends AbstractSearchDTO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private ProcessPreMedicalTableDTO preMedicalTableDTO;
	
	private SearchPreMedicalProcessingEnhancementTableDTO processingEnhancementTableDTO;
	
	private String currentQ;
	
	private ImsUser imsUser;

	public ProcessPreMedicalTableDTO getPreMedicalTableDTO() {
		return preMedicalTableDTO;
	}

	public void setPreMedicalTableDTO(ProcessPreMedicalTableDTO preMedicalTableDTO) {
		this.preMedicalTableDTO = preMedicalTableDTO;
	}

	public SearchPreMedicalProcessingEnhancementTableDTO getProcessingEnhancementTableDTO() {
		return processingEnhancementTableDTO;
	}

	public void setProcessingEnhancementTableDTO(
			SearchPreMedicalProcessingEnhancementTableDTO processingEnhancementTableDTO) {
		this.processingEnhancementTableDTO = processingEnhancementTableDTO;
	}

	public String getCurrentQ() {
		return currentQ;
	}

	public void setCurrentQ(String currentQ) {
		this.currentQ = currentQ;
	}

	public ImsUser getImsUser() {
		return imsUser;
	}

	public void setImsUser(ImsUser imsUser) {
		this.imsUser = imsUser;
	}

}
