package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="MAS_OMBUDSMAN")
@NamedQueries({
	@NamedQuery(name="MasOmbudsman.findAll", query="SELECT m FROM MasOmbudsman m"),
	@NamedQuery(name="MasOmbudsman.findByOmbudsManCode", query="SELECT m FROM MasOmbudsman m where m.ombudsmanCode = :ombudsmanCode"),
	@NamedQuery(name="MasOmbudsman.findBykey", query="SELECT m FROM MasOmbudsman m where m.key = :key")

})
public class MasOmbudsman implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9193690746901801586L;

	@Id
	@Column(name="OMB_KEY")
	private Long key;
	
	
	
//	@Column(name="FK_ORGANIZATION_KEY")
//	private BigDecimal fkOrganizationKey;

//	@Column(name="FK_NAMESPACE_KEY")
//	private BigDecimal fkNamespaceKey;

	@Column(name="OMB_CPU_CODE")
	private String cpuCode;
	
	@Column(name="OMB_CPU_DESC")
	private String cpuCodeDescription;
	
	@Column(name="OMB_OFFICER")
	private String ombOfficer;
	
	@Column(name="OMB_NAME")
	private String ombName;
	
	@Column(name="OMB_ADD01")
	private String ombAddress1;
	
	@Column(name="OMB_ADD02")
	private String ombAddress2;
	
	@Column(name="OMB_ADD03")
	private String ombAddress3;
	
	@Column(name="OMB_ADD04")
	private String ombAddress4;
	
	@Column(name="OMB_TEL")
	private String ombTelephone;
	
	@Column(name="OMB_FAX")
	private String ombFax;
	
	@Column(name="OMB_MAIL")
	private String ombMail;
	
	@Column(name="OMB_CODE")
	private String ombudsmanCode;


	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(String cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getCpuCodeDescription() {
		return cpuCodeDescription;
	}

	public void setCpuCodeDescription(String cpuCodeDescription) {
		this.cpuCodeDescription = cpuCodeDescription;
	}

	public String getOmbOfficer() {
		return ombOfficer;
	}

	public void setOmbOfficer(String ombOfficer) {
		this.ombOfficer = ombOfficer;
	}

	public String getOmbName() {
		return ombName;
	}

	public void setOmbName(String ombName) {
		this.ombName = ombName;
	}

	public String getOmbAddress1() {
		return ombAddress1;
	}

	public void setOmbAddress1(String ombAddress1) {
		this.ombAddress1 = ombAddress1;
	}

	public String getOmbAddress2() {
		return ombAddress2;
	}

	public void setOmbAddress2(String ombAddress2) {
		this.ombAddress2 = ombAddress2;
	}

	public String getOmbAddress3() {
		return ombAddress3;
	}

	public void setOmbAddress3(String ombAddress3) {
		this.ombAddress3 = ombAddress3;
	}

	public String getOmbAddress4() {
		return ombAddress4;
	}

	public void setOmbAddress4(String ombAddress4) {
		this.ombAddress4 = ombAddress4;
	}

	public String getOmbTelephone() {
		return ombTelephone;
	}

	public void setOmbTelephone(String ombTelephone) {
		this.ombTelephone = ombTelephone;
	}

	public String getOmbFax() {
		return ombFax;
	}

	public void setOmbFax(String ombFax) {
		this.ombFax = ombFax;
	}

	public String getOmbMail() {
		return ombMail;
	}

	public void setOmbMail(String ombMail) {
		this.ombMail = ombMail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOmbudsmanCode() {
		return ombudsmanCode;
	}

	public void setOmbudsmanCode(String ombudsmanCode) {
		this.ombudsmanCode = ombudsmanCode;
	}
	
	


}