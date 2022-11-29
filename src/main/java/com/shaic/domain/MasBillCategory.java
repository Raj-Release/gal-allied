/**
 * 
 */
package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author ntv.vijayar
 *
 */

@Entity
@Table(name="MAS_BILL_CATEGORY")
@NamedQueries( {
@NamedQuery(name="MasBillCategory.findAll", query="SELECT m FROM MasBillCategory m where m.activeStatus is not null and m.activeStatus=1 order by m.value asc"),
@NamedQuery(name = "MasBillCategory.findByKey",query = "select o from MasBillCategory o where o.key = :parentKey"),
@NamedQuery(name = "MasBillCategory.findByBillClassificationKey",query = "select o from MasBillCategory o where o.billClassificationKey = :billClassificationkey and o.activeStatus is not null and o.activeStatus = 1 order by o.value asc"),
@NamedQuery(name = "MasBillCategory.findByBillClassificationKeyOrderByKey",query = "select o from MasBillCategory o where o.billClassificationKey = :billClassificationkey and o.activeStatus is not null and o.activeStatus = 1 order by o.key asc")

})

public class MasBillCategory {
	
	@Id
	@Column(name = "BILL_CATEGORY_KEY")
	private Long key;
	
	@Column(name = "BILL_CLASSIFICATION_KEY")
	private Long billClassificationKey;
	
	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "BILL_TYPE_NUMBER")
	private String billTypeNumber;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	/**
	 * @return the key
	 */
	public Long getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * @return the billClassificationKey
	 */
	public Long getBillClassificationKey() {
		return billClassificationKey;
	}

	/**
	 * @param billClassificationKey the billClassificationKey to set
	 */
	public void setBillClassificationKey(Long billClassificationKey) {
		this.billClassificationKey = billClassificationKey;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the billTypeNumber
	 */
	public String getBillTypeNumber() {
		return billTypeNumber;
	}

	/**
	 * @param billTypeNumber the billTypeNumber to set
	 */
	public void setBillTypeNumber(String billTypeNumber) {
		this.billTypeNumber = billTypeNumber;
	}

	/**
	 * @return the activeStatus
	 */
	public Long getActiveStatus() {
		return activeStatus;
	}

	/**
	 * @param activeStatus the activeStatus to set
	 */
	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	

}
