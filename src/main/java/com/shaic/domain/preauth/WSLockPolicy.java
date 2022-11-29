package com.shaic.domain.preauth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name = "IMS_CLS_WS_LOCK_POLICY")
public class WSLockPolicy implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Id 
		@SequenceGenerator(name="IMS_CLS_LOCK_KEY_GENERATOR", sequenceName = "IMS_MASTXN.SEQ_GLX_WS_LOCK_KEY" , allocationSize = 1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_LOCK_KEY_GENERATOR" )
		@Column(name="GLX_WS_LOCK_KEY", updatable=false)
		private Long key;

		@Column(name = "INTIMATION_NUMBER")
		private String intimationNumber;
		
		@Column(name = "POLICY_NUMBER")
		private String policyNo;
		
		@Column(name = "CLAIM_NUMBER")
		private String claimNumber;
		
		@Column(name = "L_TYPE")
		private String type;
		
		@Column(name = "CREATED_DATE")
		private String createdDate;
		
		@Column(name = "WS_UPD_FLAG")
		private String updateFlag;
		
		@Column(name = "WS_UPD_DATE")
		private String updateDate;
		
		@Column(name = "REMARKS")
		private String remarks;

		public Long getKey() {
			return key;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public String getIntimationNumber() {
			return intimationNumber;
		}

		public void setIntimationNumber(String intimationNumber) {
			this.intimationNumber = intimationNumber;
		}

	
		public String getClaimNumber() {
			return claimNumber;
		}

		public void setClaimNumber(String claimNumber) {
			this.claimNumber = claimNumber;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}

		public String getUpdateFlag() {
			return updateFlag;
		}

		public void setUpdateFlag(String updateFlag) {
			this.updateFlag = updateFlag;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getPolicyNo() {
			return policyNo;
		}

		public void setPolicyNo(String policyNo) {
			this.policyNo = policyNo;
		}
		
		
		
}
		
