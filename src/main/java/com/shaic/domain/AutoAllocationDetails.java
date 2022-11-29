package com.shaic.domain;

import java.sql.Timestamp;

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
@Table(name="IMS_CLS_AUTOALLOCATION_DTLS")
@NamedQueries({
	@NamedQuery(name="AutoAllocationDetails.findAssignedByDoctor", query="SELECT m FROM AutoAllocationDetails m where Lower(m.doctorId) = :doctorId and m.statusFlag IN (:statusList) and m.activeStatus is not null and m.activeStatus = 1 and m.key in (SELECT MAX(n.key) from AutoAllocationDetails n where Lower(n.doctorId) = :doctorId GROUP BY n.intimationNo) order by m.assignedDate asc"),
	@NamedQuery(name="AutoAllocationDetails.findStatusByDoctor", query="SELECT m FROM AutoAllocationDetails m where Lower(m.doctorId) = :doctorId and m.statusFlag = :status and m.activeStatus is not null and m.activeStatus = 1 and m.key in (SELECT MAX(n.key) from AutoAllocationDetails n where Lower(n.doctorId) = :doctorId GROUP BY n.intimationNo) order by m.assignedDate asc"),
	@NamedQuery(name="AutoAllocationDetails.findByIntimation", query="SELECT m FROM AutoAllocationDetails m where m.intimationNo like :intimationNo and m.statusFlag = :status and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="AutoAllocationDetails.findByIntimationDoctor", query="SELECT m FROM AutoAllocationDetails m where m.intimationNo like :intimationNo and Lower(m.doctorId) = :doctorId and m.statusFlag = :status and m.activeStatus is not null and m.activeStatus = 1"),
	@NamedQuery(name="AutoAllocationDetails.findIntimationStatus", query="SELECT m FROM AutoAllocationDetails m where m.intimationNo like :intimationNo and Lower(m.doctorId) = :doctorId and m.activeStatus is not null and m.activeStatus = 1 and m.key in (SELECT MAX(n.key) from AutoAllocationDetails n where Lower(n.doctorId) = :doctorId GROUP BY n.intimationNo)")
})
public class AutoAllocationDetails extends AbstractEntity{

	@Id
	@SequenceGenerator(name="IMS_CLS_AUTOALLOCATION_DTLS_KEY_GENERATOR", sequenceName = "SEQ_AUTO_DTLS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_AUTOALLOCATION_DTLS_KEY_GENERATOR" ) 
	@Column(name="AUTO_DTLS_KEY", updatable=false)
	private Long key;
	
	@Column(name="INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name="DOCTOR_ID")
	private String doctorId;
	
	@Column(name="DOCTOR_NAME")
	private String doctorName;
	
	@Column(name="ASSIGNED_DATE_TIME")
	private Timestamp assignedDate;
	
	@Column(name="COMPLETED_DATE_TIME")
	private Timestamp completedDate;
	
	@Column(name="CLAIMED_AMOUNT")
	private Double claimedAmt;
	
	@Column(name="CPU_CODE")
	private Long cpuCode;
	
	@Column(name="STATUS_FLAG")
	private String statusFlag;
	
	@Column(name="ACTIVE_STATUS")
	private Integer activeStatus;
	
	@Column(name="MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Timestamp modifiedDate;
	
	@Column(name = "COMPLETED_USER_ID")
	private String completedUser;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Double getClaimedAmt() {
		return claimedAmt;
	}

	public void setClaimedAmt(Double claimedAmt) {
		this.claimedAmt = claimedAmt;
	}

	public Long getCpuCode() {
		return cpuCode;
	}

	public void setCpuCode(Long cpuCode) {
		this.cpuCode = cpuCode;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
	}

	public Timestamp getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Timestamp completedDate) {
		this.completedDate = completedDate;
	}

	public String getCompletedUser() {
		return completedUser;
	}

	public void setCompletedUser(String completedUser) {
		this.completedUser = completedUser;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((AutoAllocationDetails) obj).getKey());
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
}
