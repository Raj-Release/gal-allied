package com.shaic.claim.activitylog;

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

import com.shaic.arch.table.AbstractTableDTO;

@Entity
@Table(name="IMS_CLS_DOCTOR_ACTIVITY")
@NamedQueries( {
	@NamedQuery(name="DoctorActivity.findAll", query="SELECT A FROM DoctorActivity A"),
	@NamedQuery(name="DoctorActivity.findByIntimationNo", query="SELECT A FROM DoctorActivity A WHERE A.intimationNo = :intimationNo ORDER BY A.createdDate DESC, A.modifiedDate DESC"),
})
public class DoctorActivity extends AbstractTableDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_DOCTOR_ACTIVITY_KEY_GENERATOR", sequenceName="SEQ_DOC_ACTIVITY_KEY", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_DOCTOR_ACTIVITY_KEY_GENERATOR")
	
	@Column(name= "DOC_ACTIVITY_KEY")
	private Long activityKey;
	
	@Column(name= "INTIMATION_NUMBER")
	private String intimationNo;
	
	@Column(name= "ACTIVITY_USER_ID")
	private String activityUserId;
	
	@Column(name= "ACTIVITY_USER_NAME")
	private String activityUserName;
	
	@Column(name= "ACTIVITY_NAME")
	private String activityName;
	
	@Column(name= "ACTIVITY_DESC")
	private String activityDesc;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "ACTIVITY_DATE")
	private Date activityDate;
	
	@Column(name= "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "CREATED_DATE")
	private Date createdDate;
	
	@Column(name= "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name= "ACTIVE_STATUS")
	private int activeStatus;

	public Long getActivityKey() {
		return activityKey;
	}

	public void setActivityKey(Long activityKey) {
		this.activityKey = activityKey;
	}

	public String getIntimationNo() {
		return intimationNo;
	}

	public void setIntimationNo(String intimationNo) {
		this.intimationNo = intimationNo;
	}

	public String getActivityUserId() {
		return activityUserId;
	}

	public void setActivityUserId(String activityUserId) {
		this.activityUserId = activityUserId;
	}

	public String getActivityUserName() {
		return activityUserName;
	}

	public void setActivityUserName(String activityUserName) {
		this.activityUserName = activityUserName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	@Override
	public String toString() {
		return "DoctorActivity [activityKey=" + activityKey + ", intimationNo="
				+ intimationNo + ", activityUserId=" + activityUserId
				+ ", activityUserName=" + activityUserName + ", activityName="
				+ activityName + ", activityDesc=" + activityDesc
				+ ", activityDate=" + activityDate + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", activeStatus="
				+ activeStatus + "]";
	}
}
