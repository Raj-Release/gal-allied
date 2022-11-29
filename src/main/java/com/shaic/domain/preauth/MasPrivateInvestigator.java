package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name= "MAS_PRIVATE_INVESTIGATOR")
@NamedQueries({
	@NamedQuery(name="MasPrivateInvestigator.findAll",query="SELECT i from MasPrivateInvestigator i where i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByInvestigaitonKey",query="SELECT i from MasPrivateInvestigator i where i.privateInvestigationKey = :privateInvestigationKey"),
	@NamedQuery(name="MasPrivateInvestigator.findByZoneCode",query="SELECT i from MasPrivateInvestigator i where i.zoneCode = :zoneCode and i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByZoneName",query="SELECT i from MasPrivateInvestigator i where i.zoneName = :zoneName and i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByUniqueZone",query="SELECT distinct(i.zoneCode) from MasPrivateInvestigator i where i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByUniqueZoneName",query="SELECT distinct(i.zoneName) from MasPrivateInvestigator i where i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByCoordinatorCode",query="SELECT i from MasPrivateInvestigator i where i.coridnatorCode = :coridnatorCode and i.activeStatus='Y'"),
	@NamedQuery(name="MasPrivateInvestigator.findByCoordinatorName",query="SELECT i from MasPrivateInvestigator i where i.cordinatorName = :cordinatorName and i.activeStatus='Y'")
})
public class MasPrivateInvestigator implements Serializable{
	
	@Id
	@SequenceGenerator(name="PRIVATE_INVESTIGATOR_KEY_GENERATOR", sequenceName = "SEQ_PRIVATE_INVS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRIVATE_INVESTIGATOR_KEY_GENERATOR" )
	@Column(name="PRIVATE_INVS_KEY")
	private Long privateInvestigationKey;
	
	@Column(name="INVESTIGATOR_NAME")
	private String investigatorName;
	
	@Column(name="INVESTICATOR_CONSULTANCY")
	private String consultancy;
	
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="CONTACT_PERSON")
	private String contactPerson;
	
	@Column(name="MOBILE_NUMBER_1")
	private Long mobileNumberOne;
	
	@Column(name="MOBILE_NUMBER_2")
	private Long mobileNumberTwo;
	
	@Column(name="ZONE_CODE")
	private Long zoneCode;
	
	@Column(name="ZONE_NAME")
	private String zoneName;
	
	@Column(name="STAR_CORDINATOR_CODE")
	private String coridnatorCode;
	
	@Column(name="STAR_CORDINATOR_NAME")
	private String cordinatorName;
	
	@Column(name="TO_EMAIL_ID")
	private String toEmailId;
	
	@Column(name="CC_EMAIL_ID")
	private String ccEmailId;
	
	@Column(name="PHONE_NUMBER")
	private Long phoneNumber;
	
	@Column(name="ACTIVE_FLAG")
	private String activeStatus;
	
	@Column(name="GENDER")
	private String gender;

	public Long getPrivateInvestigationKey() {
		return privateInvestigationKey;
	}

	public void setPrivateInvestigationKey(Long privateInvestigationKey) {
		this.privateInvestigationKey = privateInvestigationKey;
	}

	public String getInvestigatorName() {
		return investigatorName;
	}

	public void setInvestigatorName(String investigatorName) {
		this.investigatorName = investigatorName;
	}

	public String getConsultancy() {
		return consultancy;
	}

	public void setConsultancy(String consultancy) {
		this.consultancy = consultancy;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Long getMobileNumberOne() {
		return mobileNumberOne;
	}

	public void setMobileNumberOne(Long mobileNumberOne) {
		this.mobileNumberOne = mobileNumberOne;
	}

	public Long getMobileNumberTwo() {
		return mobileNumberTwo;
	}

	public void setMobileNumberTwo(Long mobileNumberTwo) {
		this.mobileNumberTwo = mobileNumberTwo;
	}

	public Long getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(Long zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getCoridnatorCode() {
		return coridnatorCode;
	}

	public void setCoridnatorCode(String coridnatorCode) {
		this.coridnatorCode = coridnatorCode;
	}

	public String getCordinatorName() {
		return cordinatorName;
	}

	public void setCordinatorName(String cordinatorName) {
		this.cordinatorName = cordinatorName;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	public String getCcEmailId() {
		return ccEmailId;
	}

	public void setCcEmailId(String ccEmailId) {
		this.ccEmailId = ccEmailId;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
}
