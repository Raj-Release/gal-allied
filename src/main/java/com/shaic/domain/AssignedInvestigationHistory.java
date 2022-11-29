package com.shaic.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.shaic.domain.preauth.Stage;

@Entity
@Table(name="IMS_CLS_INV_ASSIGNMENT_HIS")
@NamedQueries({
	@NamedQuery(name="AssignedInvestigationHistory.findAll", query="SELECT o FROM AssignedInvestigationHistory o"),
	@NamedQuery(name="AssignedInvestigationHistory.findByAssignHistoryKey", query="SELECT o FROM AssignedInvestigationHistory o where o.key =:key"),
	@NamedQuery(name="AssignedInvestigationHistory.findByAssignKey", query="SELECT o FROM AssignedInvestigationHistory o where o.assignedInv.key =:invAssignkey order by o.key asc"),
	@NamedQuery(name="AssignedInvestigationHistory.findByinvestigatorName",query="SELECT  o FROM AssignedInvestigationHistory o where o.investigatorName =:investigatorName")
})
public class AssignedInvestigationHistory implements Serializable{
	
		@Id
		@Column(name = "INV_ASSIGNMENT_HIS_KEY")
		private Long key;
		     		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "INV_ASSIGNMENT_KEY")
		private AssignedInvestigatiorDetails assignedInv;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "INVESTIGATION_KEY")
		private Investigation investigation;
		               
		@Column(name="INVESTIGATOR_NAME")
		private String investigatorName;
		
		@Column(name="INVESTIGATOR_MOB_NO")
		private String invMobileNo;

		@Column(name="INVESTIGATOR_TEL_NO")
		private String invTelNo;
		
		@Column(name="REMARKS")
		private String reAssignRemarks;
	
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="STAGE_ID", nullable=false)
		private Stage stage;
		
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="STATUS_ID", nullable=false)
		private Status status;
		
		@Column(name="CREATED_BY")
		private String createdBy;

		@Column(name="CREATEDBYNAME")
		private String createdByName;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="CREATED_DATE")
		private Date createdDate;
		
		@Column(name="ASSIGNED_FROM")
		private String assignedFrom;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name="RE_ASSIGNMENT_DT")
		private Date reassignedDate;

		@Transient
		private String reassignDateValue;
		
		@Transient
		private int sNo;
		
		public Long getKey() {
			return key;
		}

		public AssignedInvestigatiorDetails getAssignedInv() {
			return assignedInv;
		}

		public Investigation getInvestigation() {
			return investigation;
		}

		public String getInvestigatorName() {
			return investigatorName;
		}

		public String getInvMobileNo() {
			return invMobileNo != null ? invMobileNo : "";
		}

		public String getInvTelNo() {
			return invTelNo != null ? invTelNo : "";
		}

		public String getReAssignRemarks() {
			return reAssignRemarks != null ? reAssignRemarks : "";
		}

		public Stage getStage() {
			return stage;
		}

		public Status getStatus() {
			return status;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public String getAssignedFrom() {
			return assignedFrom != null ? assignedFrom : "";
		}

		public Date getReassignedDate() {
			return reassignedDate;
		}

		public void setKey(Long key) {
			this.key = key;
		}

		public void setAssignedInv(AssignedInvestigatiorDetails assignedInv) {
			this.assignedInv = assignedInv;
		}

		public void setInvestigation(Investigation investigation) {
			this.investigation = investigation;
		}

		public void setInvestigatorName(String investigatorName) {
			this.investigatorName = investigatorName;
		}

		public void setInvMobileNo(String invMobileNo) {
			this.invMobileNo = invMobileNo;
		}

		public void setInvTelNo(String invTelNo) {
			this.invTelNo = invTelNo;
		}

		public void setReAssignRemarks(String reAssignRemarks) {
			this.reAssignRemarks = reAssignRemarks;
		}

		public void setStage(Stage stage) {
			this.stage = stage;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public void setAssignedFrom(String assignedFrom) {
			this.assignedFrom = assignedFrom;
		}

		public void setReassignedDate(Date reassignedDate) {
			this.reassignedDate = reassignedDate;
		}

		public String getReassignDateValue() {
			
			if(this.createdDate != null){
				reassignDateValue = new SimpleDateFormat("dd/MM/yyyy  HH:mm aa").format(new Timestamp(this.createdDate.getTime()));
			}
			else
			{
				reassignDateValue = "";
			}
			
			return reassignDateValue;
		}

		public void setReassignDateValue(String reassignDateValue) {
			this.reassignDateValue = reassignDateValue;
		}

		public String getCreatedByName() {
			return createdByName;
		}

		public void setCreatedByName(String createdbyname) {
			this.createdByName = createdbyname;
		}

		public int getsNo() {
			return sNo;
		}

		public void setsNo(int sNo) {
			this.sNo = sNo;
		}		
}
