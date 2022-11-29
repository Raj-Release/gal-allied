/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.processpaymentreturn.search;

import java.io.Serializable;

import com.shaic.arch.table.AbstractTableDTO;

/**
 * @author ntv.narenj
 *
 */
public class SearchProcessPaymentReturnsTableDTO extends AbstractTableDTO  implements Serializable{

	private String intimationNo;
	private String claimNo;
	private String policyNo;
	private String rodNo;
	private String payeeName;
	private String insuredPatiendName;
	private String cpuCode;
	private String modeOfPayment;
	private String cheque_DD_UTRNo;
	private String claimType;
	private String lotNo;
	
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
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;	}
	
	
	public String getCpuCode() {
		return cpuCode;
	}
	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}
	public String getRodNo() {
		return rodNo;
	}
	public void setRodNo(String rodNo) {
		this.rodNo = rodNo;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getCheque_DD_UTRNo() {
		return cheque_DD_UTRNo;
	}
	public void setCheque_DD_UTRNo(String cheque_DD_UTRNo) {
		this.cheque_DD_UTRNo = cheque_DD_UTRNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	/**
	 * @return the modeOfPayment
	 */
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	/**
	 * @param modeOfPayment the modeOfPayment to set
	 */
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public String getInsuredPatiendName() {
		return insuredPatiendName;
	}
	public void setInsuredPatiendName(String insuredPatiendName) {
		this.insuredPatiendName = insuredPatiendName;
	}
}
