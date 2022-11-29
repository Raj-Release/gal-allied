package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the MAS_MASTERS_VALUE_T database table.
 * 
 */
@Entity
@Table(name="MAS_REFERENCE_VALUE")
@NamedQueries( {
@NamedQuery(name="MastersValue.findAll", query="SELECT m FROM MastersValue m"),
@NamedQuery(name = "MastersValue.findByMasterListKey",query = "select o from MastersValue o where o.code = :parentKey and o.activeStatus is not null and o.activeStatus = 1 order by o.value asc"),
@NamedQuery(name = "MastersValue.findByKey",query = "select o from MastersValue o where o.key = :parentKey"),
@NamedQuery(name = "MastersValue.findByListKey",query = "select o from MastersValue o where o.code = :masterTypeCode and o.key not in (:parentKey)"),
@NamedQuery(name = "MastersValue.findByValue",query = "select o from MastersValue o where Lower(o.value) = :parentKey"),
@NamedQuery(name = "MastersValue.findValueByMasListKey",query = "select o from MastersValue o where Lower(o.value) = :parentKey and o.code = :masListKey"),
@NamedQuery(name = "MastersValue.findByMasterListKeyWithoutOrder",query = "select o from MastersValue o where o.code = :parentKey and o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name = "MastersValue.findByMasterListKeyByCodeAndValue",query = "select o from MastersValue o where  upper(o.value) = upper(:value) and o.code = :parentKey"),
@NamedQuery(name = "MastersValue.findByMasterTypeCode",query = "select o from MastersValue o where o.code = :masterTypeCode and o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name = "MastersValue.findByMappingCode",query = "select o from MastersValue o where o.mappingCode = :mappingCode"),
@NamedQuery(name = "MastersValue.findByMasterTypeCodeWithStatus",query = "select o from MastersValue o where o.code = :masterTypeCode and o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name = "MastersValue.findByMasterTypeCodeFOrBancs", query = "select o from MastersValue o where o.code = :code and o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name = "MastersValue.findByMasterTypeCodeAndCode", query="SELECT m FROM MastersValue m where  m.code = :code and m.activeStatus = 1 and m.mappingCode like :mappingCode"),
@NamedQuery(name = "MastersValue.findByMasterTypeCodeAutoAllocationType",query = "select o from MastersValue o where o.code = :masterTypeCode and o.activeStatus is not null and o.activeStatus = 1"),
@NamedQuery(name = "MastersValue.findByMasterTypeCodeClaimApplicable",query = "select o from MastersValue o where o.code = :code and Lower(o.value) =:value"),
@NamedQuery(name = "MastersValue.findByValueAndCode",query = "select o from MastersValue o where Lower(o.value) = :masterValue and Lower(o.code) = :masterCode"),

})
public class MastersValue implements Serializable {
	
	private static final long serialVersionUID = -5709582455020907914L;

	@Id
	@Column(name="MASTER_KEY")
	private Long key;


	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;

//	@Column(name="ACTIVE_STATUS_DATE")
//	private Timestamp activeStatusDate;
	
	@Column(name="MASTER_TYPE_CODE")
	private String code;
	
	/*@Column(name="MASTER_CODE")
	private String masterCode;*/
	
	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;
	
	

//	@Column(name="MASTER_TYPE_KEY")
//	private Integer masterListKey;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	@Column(name="MODIFIED_DATE")
	private Timestamp modifiedDate;

	@Column(name="MASTER_VALUE")
	private String value;

//	@Column(name="\"VERSION\"")
//	private Integer version;
	
	@Column(name="MAPPING_CODE")
	private String mappingCode;
	
	@Column(name="OLD_KEY")
	private Long orderKey;
	
	@Column(name="SORT_NO")
	private Long sortNo;

	public MastersValue() {
	}

	public Integer getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

//	public Timestamp getActiveStatusDate() {
//		return this.activeStatusDate;
//	}
//
//	public void setActiveStatusDate(Timestamp activeStatusDate) {
//		this.activeStatusDate = activeStatusDate;
//	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

//	public Integer getMasterListKey() {
//		return this.masterListKey;
//	}
//
//	public void setMasterListKey(Integer masterListKey) {
//		this.masterListKey = masterListKey;
//	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

//	public Integer getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(Integer version) {
//		this.version = version;
//	}
	
	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	
	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	@Override
	public String toString() {
		return this.getValue();
	}

	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
	            return false;
	        } else if (getKey() == null) {
	            return obj == this;
	        } else {
	            return getKey().equals(((MastersValue) obj).getKey());
	        }
	    }

	    @Override
	    public int hashCode() {
	        if (key != null) {
	            return key.hashCode();
	        } else {
	            return super.hashCode();
	        }
	    }
	    

		/*public String getMasterCode() {
			return masterCode;
		}

		public void setMasterCode(String masterCode) {
			this.masterCode = masterCode;
		}*/

}
