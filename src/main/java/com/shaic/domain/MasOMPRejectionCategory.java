package com.shaic.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author karthikeyan.r
 * The Master class for the MAS_OMP_PROD_REJECTION database table.
 * 
 */

@Entity
@Table(name="MAS_OMP_PROD_REJECTION")
@NamedQueries( {
@NamedQuery(name = "MasOMPRejectionCategory.findByCode",query = "select o from MasOMPRejectionCategory o where o.productKey = :productKey and o.eventCode = :eventCode"),
@NamedQuery(name = "MasOMPRejectionCategory.findByGen",query = "select o from MasOMPRejectionCategory o where o.eventCode = :eventCode"),
@NamedQuery(name = "MasOMPRejectionCategory.findByRejKey",query = "select o from MasOMPRejectionCategory o where o.key = :rejKey")
})

public class MasOMPRejectionCategory {
	
	/*OMP_PROD_REJ_KEY NOT NULL NUMBER             
	PRODUCT_KEY               NUMBER             
	EVENT_CODE                VARCHAR2(15 CHAR)  
	EVENT_DESC                VARCHAR2(100 CHAR) 
	REJECTION_REASON          VARCHAR2(255 CHAR) 
	ACTIVE_STATUS             NUMBER    */
	
	@Id
	@Column(name = "OMP_PROD_REJ_KEY")
	private Long key;
	
	@Column(name = "PRODUCT_KEY")
	private Long productKey;
	
	@Column(name = "EVENT_CODE")
	private String eventCode;
	
	@Column(name = "EVENT_DESC")
	private String eventDesc;
	
	@Column(name = "REJECTION_REASON")
	private String rejectionReason;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public Long getProductKey() {
		return productKey;
	}

	public void setProductKey(Long productKey) {
		this.productKey = productKey;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Long getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
