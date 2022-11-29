package com.shaic.domain.reimbursement;

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

import com.shaic.arch.fields.dto.AbstractEntity;

@Entity
@Table(name="IMS_CLS_BILL_ITEM_MAPPING")
@NamedQueries({
@NamedQuery(name="BillItemMapping.findAll", query="SELECT i FROM BillItemMapping i"),
@NamedQuery(name="BillItemMapping.findKey", query="SELECT o FROM BillItemMapping o where o.key = :primaryKey"),
@NamedQuery(name="BillItemMapping.findByReimbursementKey", query="SELECT o FROM BillItemMapping o where o.reimbursementKey = :reimbursementKey and o.deleteFlag is not null and o.deleteFlag = 'N'"),
@NamedQuery(name="BillItemMapping.findByRoomIcuRentId", query="SELECT o FROM BillItemMapping o where o.roomIcuRentId = :roomIcuRentId")
})
public class BillItemMapping extends AbstractEntity implements Serializable {

	private static long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_BILL_ITEM_MAPPING_KEY_GENERATOR", sequenceName = "SEQ_BILL_ITEM_MAPPING_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_BILL_ITEM_MAPPING_KEY_GENERATOR" ) 
	@Column(name="BILL_ITEM_MAPPING_KEY")
	private Long key;
	
	@Column(name="REIMBURSEMENT_KEY")
	private Long reimbursementKey;
	
	@Column(name="ROOM_ICU_RENT_ID")
	private Long roomIcuRentId;
	
	@Column(name="NURSING_ID")
	private Long nursingId;
	
	@Column(name="ALLOCATED_DAYS")
	private Double allocatedDays;
	
	@Column(name="UNALLOCATED_DAYS")
	private Double unAllocatedDays;
	
	@Column(name="ALLOWED_DAYS")
	private Double allowedDays;
	
	@Column(name ="DELETED_FLAG")
	private String deleteFlag;

	public Long getKey() {
		return key;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public Long getReimbursementKey() {
		return reimbursementKey;
	}

	public void setReimbursementKey(Long reimbursementKey) {
		this.reimbursementKey = reimbursementKey;
	}

	public Long getRoomIcuRentId() {
		return roomIcuRentId;
	}

	public void setRoomIcuRentId(Long roomIcuRentId) {
		this.roomIcuRentId = roomIcuRentId;
	}

	public Long getNursingId() {
		return nursingId;
	}

	public void setNursingId(Long nursingId) {
		this.nursingId = nursingId;
	}

	public Double getAllocatedDays() {
		return allocatedDays;
	}

	public void setAllocatedDays(Double allocatedDays) {
		this.allocatedDays = allocatedDays;
	}

	public Double getUnAllocatedDays() {
		return unAllocatedDays;
	}

	public void setUnAllocatedDays(Double unAllocatedDays) {
		this.unAllocatedDays = unAllocatedDays;
	}

	public Double getAllowedDays() {
		return allowedDays;
	}

	public void setAllowedDays(Double allowedDays) {
		this.allowedDays = allowedDays;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	
}
