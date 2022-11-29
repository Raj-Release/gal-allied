//package com.shaic.domain.preauth;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//
//import com.shaic.arch.fields.dto.AbstractEntity;
//
//@Entity
//@Table(name="IMS_TMP_INSURED_PED_DETAILS")
//@NamedQueries({
//	@NamedQuery(name = "TmpPED.findAll", query = "SELECT i FROM TmpPED i"),
//	@NamedQuery(name = "TmpPED.findByinsuredKey", query = "select o from TmpPED o where o.key = :insuredKey") })
//public class TmpPED extends AbstractEntity {
//
//	private static final long serialVersionUID = -3150956915053171634L;
//	
//	@Id
//	@Column(name = "INSURED_NUMBER")
//	private Long key;
//	
//	@Column(name = "PED_CODE")
//	private String pedCode;
//	
//	@Column(name = "PED_DESCRIPTION")
//	private String pedDescription;
//	
//	@Override
//	public Long getKey() {
//		return this.key;
//	}
//
//	@Override
//	public void setKey(Long key) {
//		this.key = key;
//	}
//
//	public String getPedCode() {
//		return pedCode;
//	}
//
//	public void setPedCode(String pedCode) {
//		this.pedCode = pedCode;
//	}
//
//	public String getPedDescription() {
//		return pedDescription;
//	}
//
//	public void setPedDescription(String pedDescription) {
//		this.pedDescription = pedDescription;
//	}
//	
//	
//	
//
//}
