/**
 * 
 */
package com.shaic.claim;

import java.util.List;

/**
 * @author ntv.vijayar
 *
 */
public class DMSDocumentDTO {
	
	private String intimationNo;
	
	private String claimNo;
	private String docType;
	
	
	
	private List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList;

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public List<DMSDocumentDetailsDTO> getDmsDocumentDetailsDTOList() {
		return dmsDocumentDetailsDTOList;
	}

	public void setDmsDocumentDetailsDTOList(
			List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList) {
		this.dmsDocumentDetailsDTOList = dmsDocumentDetailsDTOList;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

}
