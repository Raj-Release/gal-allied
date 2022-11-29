package com.shaic.domain.omp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;


@Entity
@Table(name="MAS_OMP_NEGOTIATOR")
@NamedQueries({
	@NamedQuery(name="OMPNegotiationMas.findByOMPall", query="SELECT m FROM OMPNegotiationMas m"),
	@NamedQuery(name="OMPNegotiationMas.findByName", query="SELECT m FROM OMPNegotiationMas m where m.key = :countryName ")
})
public class OMPNegotiationMas extends AbstractEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_OMP_NEGOTIATOR_KEY_GENERATOR", sequenceName = "SEQ_NEGOTIATOR_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_OMP_NEGOTIATOR_KEY_GENERATOR" ) 
	@Column(name="NEGOTIATOR_KEY")
	private Long key;
	
	@Column(name="NEGOTIATOR_NAME")
	private String neogtiationName;
	
	@Column(name="ADDRESS1")
	private String address1;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="PINCODE")
	private Long pinCode;
	
	@Column(name="PHONE")
	private String phone;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="BANK_NAME")
	private String bankName;
	
	@Column(name="BANK_ADDRESS1")
	private String bankAddress1;
	
	@Column(name="BANK_CITY")
	private String bankCity;
	
	@Column(name="BANK_STATE")
	private String bankState;
	
	@Column(name="BANK_COUNTRY")
	private String bankCountry;
	
	@Column(name="BANK_PINCODE")
	private String bankPinCode;
	
	@Column(name="ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name="ABA_ROUTINGG_NUMBER")
	private String abaRoutinggNumber;
	

	@Column(name="SWIFT_CODE")
	private String swiftCode;
	

	@Column(name="NEFT_RTGS_NO")
	private String neftRtgsNo;
	

	@Column(name="NEGOTIATOR_FEES_PCTG")
	private String negotiatorFeesPctg;
	

	@Column(name="NEGOTIATION_EFFECT_FROM")
	private String negotiationEffectFrom;
	

	@Column(name="NEGOTIATION_EFFECT_TO")
	private String negotiationEffectTo;
	
	
	@Column(name="NEGOTIATION_FEE_CAPPING")
	private String negotiationfeeCapping;
	
	@Column(name="NEGOTIATION_FEE_CURRENCY")
	private String negotiationfeeCurrency;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATED_DATE")
	private Date createdDatae;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;


	public Long getKey() {
		return key;
	}


	public void setKey(Long key) {
		this.key = key;
	}


	public String getNeogtiationName() {
		return neogtiationName;
	}


	public void setNeogtiationName(String neogtiationName) {
		this.neogtiationName = neogtiationName;
	}


	public String getAddress1() {
		return address1;
	}


	public void setAddress1(String address1) {
		this.address1 = address1;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Long getPinCode() {
		return pinCode;
	}


	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getBankAddress1() {
		return bankAddress1;
	}


	public void setBankAddress1(String bankAddress1) {
		this.bankAddress1 = bankAddress1;
	}


	public String getBankCity() {
		return bankCity;
	}


	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}


	public String getBankState() {
		return bankState;
	}


	public void setBankState(String bankState) {
		this.bankState = bankState;
	}


	public String getBankCountry() {
		return bankCountry;
	}


	public void setBankCountry(String bankCountry) {
		this.bankCountry = bankCountry;
	}


	public String getBankPinCode() {
		return bankPinCode;
	}


	public void setBankPinCode(String bankPinCode) {
		this.bankPinCode = bankPinCode;
	}


	public String getAccountType() {
		return accountType;
	}


	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}


	public String getAbaRoutinggNumber() {
		return abaRoutinggNumber;
	}


	public void setAbaRoutinggNumber(String abaRoutinggNumber) {
		this.abaRoutinggNumber = abaRoutinggNumber;
	}


	public String getSwiftCode() {
		return swiftCode;
	}


	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}


	public String getNeftRtgsNo() {
		return neftRtgsNo;
	}


	public void setNeftRtgsNo(String neftRtgsNo) {
		this.neftRtgsNo = neftRtgsNo;
	}


	public String getNegotiatorFeesPctg() {
		return negotiatorFeesPctg;
	}


	public void setNegotiatorFeesPctg(String negotiatorFeesPctg) {
		this.negotiatorFeesPctg = negotiatorFeesPctg;
	}


	public String getNegotiationEffectFrom() {
		return negotiationEffectFrom;
	}


	public void setNegotiationEffectFrom(String negotiationEffectFrom) {
		this.negotiationEffectFrom = negotiationEffectFrom;
	}


	public String getNegotiationEffectTo() {
		return negotiationEffectTo;
	}


	public void setNegotiationEffectTo(String negotiationEffectTo) {
		this.negotiationEffectTo = negotiationEffectTo;
	}


	public String getNegotiationfeeCapping() {
		return negotiationfeeCapping;
	}


	public void setNegotiationfeeCapping(String negotiationfeeCapping) {
		this.negotiationfeeCapping = negotiationfeeCapping;
	}


	public String getNegotiationfeeCurrency() {
		return negotiationfeeCurrency;
	}


	public void setNegotiationfeeCurrency(String negotiationfeeCurrency) {
		this.negotiationfeeCurrency = negotiationfeeCurrency;
	}


	public Date getCreatedDatae() {
		return createdDatae;
	}


	public void setCreatedDatae(Date createdDatae) {
		this.createdDatae = createdDatae;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	

}
