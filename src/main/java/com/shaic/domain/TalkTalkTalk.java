package com.shaic.domain;

import java.io.Serializable;
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

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.preauth.Preauth;


@Entity
@Table(name="IMS_CLS_TALK_TALK")
@NamedQueries({
	@NamedQuery(name="TalkTalkTalk.findAll", query="SELECT m FROM TalkTalkTalk m"),
	@NamedQuery(name="TalkTalkTalk.findByKey", query="SELECT m FROM TalkTalkTalk m where m.talkKey = :talkKey"),
	@NamedQuery(name="TalkTalkTalk.findByIntimationKey", query = "SELECT m FROM TalkTalkTalk m where m.intimationKey = :intimationKey"),
	@NamedQuery(name="TalkTalkTalk.findByIntimationNumber", query = "SELECT m FROM TalkTalkTalk m where m.intimationNumber = :intimationNumber"),
})
public class TalkTalkTalk extends AbstractEntity implements Serializable  {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1086997314154372927L;

		@Id
		@SequenceGenerator(name="IMS_CLS_TALK_TALK_KEY_GENERATOR", sequenceName = "SEQ_TALK_KEY", allocationSize = 1)
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_TALK_TALK_KEY_GENERATOR" ) 
		
		
		@Column(name="TALK_KEY")
		private Long talkKey;
		
		@Column(name = "INTIMATION_NUMBER")
		private String intimationNumber;
		
		/*@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name="INTIMATION_KEY", nullable=true)
		private Intimation intimation;*/
		
		@Column(name="INTIMATION_KEY")
		private Long intimationKey;
		
		@OneToOne
		@JoinColumn(name="TYPE_OF_COMMUNICATION", nullable=false)
		private MastersValue typeOfCommunication;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "DATE_AND_TIME_OF_CALL")
		private Date dateAndTimeofCall;
		
		@Column(name = "SPOKEN_TO")
		private String spokenTo;
		
		@Column(name = "CONTACT_NUMBER_OF_PERSON")
		private Long contactNumber;
		
		@Column(name = "REMARKS")
		private String remarks;
		
		@Column(name = "PROCESSING_USER_NAME")
		private String processingUserName;
		
		@Column(name = "ACTIVE_STATUS")
		private Long activeStatus;
		
		@Column(name = "CREATED_BY")
		private String createdBy;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "CREATED_DATE")
		private Date createdDate;
		
		@Column(name = "MODIFIED_BY")
		private String modifiedBy;
		
		@Temporal(TemporalType.TIMESTAMP)
		@Column(name = "MODIFIED_DATE")
		private Date modifiedDate;
		
		@Column(name = "REFERENCE_ID")
		private String referenceId;
		
		@Column(name="CONVOX_ID")
		private Long convoxId;
		
		public Long getTalkKey() {
			return talkKey;
		}

		public void setTalkKey(Long talkKey) {
			this.talkKey = talkKey;
		}

		public MastersValue getTypeOfCommunication() {
			return typeOfCommunication;
		}

		public void setTypeOfCommunication(MastersValue typeOfCommunication) {
			this.typeOfCommunication = typeOfCommunication;
		}

		public Date getDateAndTimeofCall() {
			return dateAndTimeofCall;
		}

		public void setDateAndTimeofCall(Date dateAndTimeofCall) {
			this.dateAndTimeofCall = dateAndTimeofCall;
		}

		public String getSpokenTo() {
			return spokenTo;
		}

		public void setSpokenTo(String spokenTo) {
			this.spokenTo = spokenTo;
		}

		public Long getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(Long contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public Long getActiveStatus() {
			return activeStatus;
		}

		public void setActiveStatus(Long activeStatus) {
			this.activeStatus = activeStatus;
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

		@Override
		public Long getKey() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setKey(Long key) {
			// TODO Auto-generated method stub
			
		}

		public String getProcessingUserName() {
			return processingUserName;
		}

		public void setProcessingUserName(String processingUserName) {
			this.processingUserName = processingUserName;
		}

		public String getIntimationNumber() {
			return intimationNumber;
		}

		public void setIntimationNumber(String intimationNumber) {
			this.intimationNumber = intimationNumber;
		}

		public Long getIntimationKey() {
			return intimationKey;
		}

		public void setIntimationKey(Long intimationKey) {
			this.intimationKey = intimationKey;
		}

		public String getReferenceId() {
			return referenceId;
		}

		public void setReferenceId(String referenceId) {
			this.referenceId = referenceId;
		}

		public Long getConvoxId() {
			return convoxId;
		}

		public void setConvoxId(Long convoxId) {
			this.convoxId = convoxId;
		}

		
		
		
}
