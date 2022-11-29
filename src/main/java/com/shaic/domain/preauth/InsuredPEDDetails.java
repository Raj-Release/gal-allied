package com.shaic.domain.preauth;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Deprecated
@Entity
@Table(name="IMS_CLS_INSURED_PED_DETAILS")
@NamedQueries({
	@NamedQuery(name="InsuredPEDDetails.findAll", query="SELECT m FROM InsuredPEDDetails m "),
	@NamedQuery(name="InsuredPEDDetails.findByInsuredKey", query="SELECT m FROM InsuredPEDDetails m where m.insuredKey = :insuredKey")
})
public class InsuredPEDDetails {

	@Id
	//@Column(name="\"KEY\"")
	@Column(name="INSURED_PED_KEY")
	private Long key;
	
	/*@Column(name="FK_POLICY_KEY")
	private Long policyKey;
	*/
	@Column(name="INSURED_KEY")
	private Long insuredKey;
	
	@Column(name="PED_CODE")
	private String pedCode;
	
	@Column(name="PED_DESCRIPTION")
	private String pedDescription;
	
	/*@Column(name="POLICY_NUMBER")
	private String policyNumber;*/

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	/*public Long getPolicyKey() {
		return policyKey;
	}

	public void setPolicyKey(Long policyKey) {
		this.policyKey = policyKey;
	}
*/
	public Long getInsuredKey() {
		return insuredKey;
	}

	public void setInsuredKey(Long insuredKey) {
		this.insuredKey = insuredKey;
	}

	public String getPedCode() {
		return pedCode;
	}

	public void setPedCode(String pedCode) {
		this.pedCode = pedCode;
	}

	public String getPedDescription() {
		return pedDescription;
	}

	public void setPedDescription(String pedDescription) {
		this.pedDescription = pedDescription;
	}

	/*public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}*/
}
