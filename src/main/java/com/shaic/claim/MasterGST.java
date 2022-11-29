package com.shaic.claim;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="MAS_GST")
@NamedQueries({
	@NamedQuery(name="MasterGST.findByStateId", query="SELECT m FROM MasterGST m WHERE m.stateId = :stateId")
	
})

public class MasterGST extends AbstractEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="GST_KEY")
	private Long key;
	
	@Column(name="GST_NUMBER")
	private String gstNumber;
	
	@Column(name="ARN_NUMBER")
	private String arnNumber;
	
	@Column(name="PRINCIPAL_PLACE_OF_BUSINESS")
	private String placeOfBusiness;
	
	@Column(name="STATE_ID")
	private Long stateId;
	
	@Column(name="STATE_NAME")
	private String stateName;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="CONTACT_NUMBER")
	private String contactNumber;
	

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getArnNumber() {
		return arnNumber;
	}

	public void setArnNumber(String arnNumber) {
		this.arnNumber = arnNumber;
	}

	public String getPlaceOfBusiness() {
		return placeOfBusiness;
	}

	public void setPlaceOfBusiness(String placeOfBusiness) {
		this.placeOfBusiness = placeOfBusiness;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	

}
