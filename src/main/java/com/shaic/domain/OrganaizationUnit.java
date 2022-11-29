package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_BRANCH_OFFICE database table.
 * 
 */
@Entity
@Table(name="MAS_BRANCH_OFFICE")
@NamedQueries({
	@NamedQuery(name="OrganaizationUnit.findAll", query="SELECT m FROM OrganaizationUnit m  where m.activeStatus is not null and m.activeStatus = 1 order by m.organizationUnitName asc"),
	@NamedQuery(name="OrganaizationUnit.findByUnitId", query="SELECT m FROM OrganaizationUnit m where m.organizationUnitId = :officeCode"),
	@NamedQuery(name = "OrganaizationUnit.findByOrgUnitId", query = "select o from OrganaizationUnit o where o.organizationUnitId = :parentKey"),
	@NamedQuery(name = "OrganaizationUnit.findByStateAndCity", query = "select o from OrganaizationUnit o where o.state.key = :stateKey and o.cityTownVillage.key = :cityKey"),
	@NamedQuery(name = "OrganaizationUnit.findByBranchode", query = "select o from OrganaizationUnit o where o.organizationUnitId = :parentKey"),
	@NamedQuery(name = "OrganaizationUnit.findByCpuCode", query = "select o from OrganaizationUnit o where o.cpuCode like :cpuCode"),
	@NamedQuery(name = "OrganaizationUnit.findAllOfficeCode", query = "select o from OrganaizationUnit o"),
	@NamedQuery(name = "OrganaizationUnit.findByBasedOnBranchType",query = "select o from OrganaizationUnit o where o.organizationUnitId = :parentKey and o.organizationTypeId = :branchTypeId"),
	@NamedQuery(name = "OrganaizationUnit.findByKey",query = "select o from OrganaizationUnit o where o.key =:branchKey and o.activeStatus = 1"),
	@NamedQuery(name = "OrganaizationUnit.findByCode",query = "select o from OrganaizationUnit o where o.organizationUnitId=:branchCode and o.activeStatus = 1"),
	@NamedQuery(name = "OrganaizationUnit.findDistinctBranchCode",query = "select distinct o.organizationUnitId from OrganaizationUnit o where o.activeStatus = 1 and o.organizationUnitId not in(950000,950001,950002,950003,950004,950005,950006,950007,950008,950009,950010,950011,950012,950013,950014,950015,950016,950017,950018,950019,950025,950026,950027,960000,900009,910000,920000,930000,940000,940004)")
	
})
public class OrganaizationUnit implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9193690746901801586L;

	@Id
	@Column(name="BRANCH_KEY")
	private Long key;
	
//	@Column(name="FK_ORGANIZATION_KEY")
//	private BigDecimal fkOrganizationKey;

//	@Column(name="FK_NAMESPACE_KEY")
//	private BigDecimal fkNamespaceKey;

	

	
	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="BRANCH_CODE")
	private String organizationUnitId;

	@Column(name="BRANCH_NAME")
	private String organizationUnitName;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="PARENT_BRANCH_KEY")
	private Long parentOrgUnitKey;
	
	@Column(name="CPU_CODE")
	private String cpuCode;

//	@Column(name="\"VERSION\"")
//	private BigDecimal version;

	@Column(name="ACTIVE_STATUS")
	private Long activeStatus;

	@Column(name="ZONE_ID")
	private Long zoneId;
	
	@Column(name = "ADDRESS")
	private String address;

	@Column(name="BRANCH_TYPE_ID")
	private Long organizationTypeId;
	
	@JoinColumn(name="STATE_ID")
	@OneToOne
	private State state;
	
	@JoinColumn(name="CITY_ID")
	@OneToOne
	private CityTownVillage cityTownVillage;
	
	@Column(name = "EMAIL_ID")
//	@Transient
	private String emailId;
	
	
	@Column(name = "PINCODE_ID")
	private Long pinCode;
	
	@Column(name="OMB_CODE")
	private String ombudsmanCode;
	
	@Column(name="EMAIL_ID_2")
	private String emailId2;
	
	@Column (name="CONTACT_NUMBER")
	private String contactNumber;
	
//	@JoinColumn(name="COUNTRY_ID")
//	@OneToOne
//	private Country country;
//	
//	@JoinColumn(name="DISTRICT_ID")
//	@OneToOne
//	private District district;
	
//	@JoinColumn(name="LOCALITY_ID")
//	@OneToOne
//	private Locality locality;
	
//	@JoinColumn(name="PINCODE_ID")
//	@OneToOne
//	private Pincode pincode;
	
	

	public Long getKey() {
		return key;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getOrganizationUnitId() {
		return organizationUnitId;
	}

	public void setOrganizationUnitId(String organizationUnitId) {
		this.organizationUnitId = organizationUnitId;
	}

	public String getOrganizationUnitName() {
		return organizationUnitName;
	}

	public void setOrganizationUnitName(String organizationUnitName) {
		this.organizationUnitName = organizationUnitName;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getParentOrgUnitKey() {
		return parentOrgUnitKey;
	}

	public void setParentOrgUnitKey(Long parentOrgUnitKey) {
		this.parentOrgUnitKey = parentOrgUnitKey;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getOrganizationTypeId() {
		return organizationTypeId;
	}

	public void setOrganizationTypeId(Long organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public CityTownVillage getCityTownVillage() {
		return cityTownVillage;
	}

	public void setCityTownVillage(CityTownVillage cityTownVillage) {
		this.cityTownVillage = cityTownVillage;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public String getOmbudsmanCode() {
		return ombudsmanCode;
	}

	public void setOmbudsmanCode(String ombudsmanCode) {
		this.ombudsmanCode = ombudsmanCode;
	}

	public String getEmailId2() {
		return emailId2;
	}

	public void setEmailId2(String emailId2) {
		this.emailId2 = emailId2;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	

//	public District getDistrict() {
//		return district;
//	}
//
//	public void setDistrict(District district) {
//		this.district = district;
//	}

//	public Locality getLocality() {
//		return locality;
//	}
//
//	public void setLocality(Locality locality) {
//		this.locality = locality;
//	}

//	public Pincode getPincode() {
//		return pincode;
//	}
//
//	public void setPincode(Pincode pincode) {
//		this.pincode = pincode;
//	}

	

}