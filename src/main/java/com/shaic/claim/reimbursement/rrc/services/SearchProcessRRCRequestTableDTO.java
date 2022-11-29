/**
 * 
 */
package com.shaic.claim.reimbursement.rrc.services;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shaic.arch.table.AbstractTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;

/**
 * @author ntv.vijayar
 *
 */
public class SearchProcessRRCRequestTableDTO extends AbstractTableDTO  implements Serializable{
	
	
	
	private String intimationNo;
	
	private Long intimationKey;
	
	private String rrcRequestNo;
	
	private Date dateOfRequest;
	
	private String dateOfRequestForTable;
	
	private String requestorId;
	
	private String requestorName;
	
	private String rrcRequestType;
	
	private Long rodKey;
	private Long claimKey;
	
	private PreauthDTO preauthDTO;
	

	
	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getRrcRequestNo() {
		return rrcRequestNo;
	}

	public void setRrcRequestNo(String rrcRequestNo) {
		this.rrcRequestNo = rrcRequestNo;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(Date dateOfRequest) {
		
		if(dateOfRequest !=  null){
			String dateformate =new SimpleDateFormat("dd/MM/yyyy").format(dateOfRequest);
			setDateOfRequestForTable(dateformate);
			this.dateOfRequest = dateOfRequest;
			}
		
		//this.dateOfRequest = dateOfRequest;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getRrcRequestType() {
		return rrcRequestType;
	}

	public void setRrcRequestType(String rrcRequestType) {
		this.rrcRequestType = rrcRequestType;
	}

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public Long getClaimKey() {
		return claimKey;
	}

	public void setClaimKey(Long claimKey) {
		this.claimKey = claimKey;
	}

	public String getDateOfRequestForTable() {
		return dateOfRequestForTable;
	}

	public void setDateOfRequestForTable(String dateOfRequestForTable) {		
		this.dateOfRequestForTable = dateOfRequestForTable;
	}

	public Long getIntimationKey() {
		return intimationKey;
	}

	public void setIntimationKey(Long intimationKey) {
		this.intimationKey = intimationKey;
	}

	public PreauthDTO getPreauthDTO() {
		return preauthDTO;
	}

	public void setPreauthDTO(PreauthDTO preauthDTO) {
		this.preauthDTO = preauthDTO;
	}

}
