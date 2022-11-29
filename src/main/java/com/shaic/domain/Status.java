package com.shaic.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.shaic.domain.preauth.Stage;

/**
 * The persistent class for the MAS_STATUS database table. 
 *
 */
@Entity
@Table(name="MAS_STATUS")

@NamedQueries({
	@NamedQuery(name="Status.findByStageName", query="SELECT m FROM Status m WHERE Lower(m.stage.stageName) Like :stageName"),
	@NamedQuery(name="Status.findByStage", query="SELECT m FROM Status m WHERE m.stage.key = :stageId and Lower(m.processValue) Like :status"),
	@NamedQuery(name = "Status.findByStageKey",query= "SELECT o from Status o WHERE o.stage.key = :stageKey and o.activeStatus is not null and o.activeStatus = 1"),
	@NamedQuery(name="Status.findByKey",query="SELECT o from Status o WHERE o.key=:statusKey"),
	@NamedQuery(name="Status.findByKeys",query="SELECT o from Status o WHERE o.key in (:statusKeyList)"),
	@NamedQuery(name="Status.findByName", query="SELECT m FROM Status m WHERE Lower(m.processValue) Like :status and m.activeStatus = 1"),
})
public class Status implements Serializable {
	   
	@Id
	@Column(name = "STATUS_KEY")
	private Long key;
	
	@OneToOne
	@JoinColumn(name = "STAGE_KEY",nullable=false)
	private Stage stage;
	
	@Column(name = "ACTIVE_STATUS")
	private Long activeStatus;


	/*@Column(name="SEQUENCE_NUMBER")
=======
//	@Column(name="SEQUENCE_NUMBER")
	/*@Transient
>>>>>>> cb48332ef255a08cffcca1164ace30af4466a6cb
	private Long sequenceNumber;*/
	
//	@Column(name="VERSION")
//	private Long version;
	
	@Column(name="PROCESS_VALUE")
	private String processValue;
	
	@Column(name="USER_STATUS")
	private String userStatus;
	
	@Column(name="PORTAL_STATUS")
	private String portalStatus;
	
	@Column(name="WEBSITE_STATUS")
	private String websiteStatus;
	
	private static final long serialVersionUID = 1L;
	
	public Status() {
		
	}   
	public Long getKey() {
		return this.key;
	}

	public void setKey(Long key) {
		this.key = key;
	}   
	public Stage getStage() {
		return this.stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}   
	/*public Long getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}   */

	public String getProcessValue() {
		return this.processValue;
	}

	public void setProcessValue(String processValue) {
		this.processValue = processValue;
	}
	public Long getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(Long activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getPortalStatus() {
		return portalStatus;
	}
	public void setPortalStatus(String portalStatus) {
		this.portalStatus = portalStatus;
	}
	public String getWebsiteStatus() {
		return websiteStatus;
	}
	public void setWebsiteStatus(String websiteStatus) {
		this.websiteStatus = websiteStatus;
	}
	
}
