package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="MAS_PRODUCT_SUM_INSURED")
@NamedQuery(name="ProductSumInsured.findAll", query="SELECT i FROM ProductSumInsured i")
public class ProductSumInsured implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645363007758647416L;
	
	@Id
	@Column(name="KEY")
	private Long key;
	
//	@OneToOne
//	@JoinColumn(name="FK_PRODUCT_KEY",nullable=false)
//	private Product product;
//	
//	@Column(name="ACTIVE_STATUS")
//	private Long activeStatus;
//	
//	@Column(name="VERSION")
//	private Long version;
//	
//	@Column(name="SUM_INSURED")
//	private Double sumInsured;
//
//	public ProductSumInsured() {
//	}
//
//	public Long getKey() {
//		return key;
//	}
//
//	public void setKey(Long key) {
//		this.key = key;
//	}
//
//	public Product getProduct() {
//		return product;
//	}
//
//	public void setProduct(Product product) {
//		this.product = product;
//	}
//
//	public Long getActiveStatus() {
//		return activeStatus;
//	}
//
//	public void setActiveStatus(Long activeStatus) {
//		this.activeStatus = activeStatus;
//	}
//
//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}
//
//	public Double getSumInsured() {
//		return sumInsured;
//	}
//
//	public void setSumInsured(Double sumInsured) {
//		this.sumInsured = sumInsured;
//	}
	

}
