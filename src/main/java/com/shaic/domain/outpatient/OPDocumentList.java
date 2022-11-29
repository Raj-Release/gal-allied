package com.shaic.domain.outpatient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.shaic.arch.fields.dto.AbstractEntity;
import com.shaic.domain.MastersValue;

@Entity
@Table(name = "IMS_CLS_OP_DOCUMENT_LIST")
@NamedQueries({

	@NamedQuery(name ="OPDocumentList.findByopHealthCheckupKey",query="SELECT r FROM OPDocumentList r WHERE r.opHealthCheckup.key = :opHealthCheckupKey"),
	@NamedQuery(name ="OPDocumentList.findByHealthCheckupKey",query="SELECT r FROM OPDocumentList r WHERE r.opHealthCheckup is not null and r.opHealthCheckup.key = :healthCheckupKey")

})
public class OPDocumentList extends AbstractEntity {

	private static final long serialVersionUID = -797939768188532132L;
	@Id
	@SequenceGenerator(name="IMS_CLS_OP_DOCUMENT_LIST_KEY_GENERATOR", sequenceName = "SEQ_OP_DOC_LIST_KEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="IMS_CLS_OP_DOCUMENT_LIST_KEY_GENERATOR" ) 
	@Column(name="OP_DOC_LIST_KEY", updatable=false)
	private Long key;
	
	@OneToOne
	@JoinColumn(name="OP_HEALTH_CHECKUP_KEY", nullable=false)
	private OPHealthCheckup opHealthCheckup;
	
	@Column(name = "DOCUMENT_TYPE_ID")
	private Long documentTypeId;
	
	@OneToOne
	@JoinColumn(name="RECEIVED_STATUS_ID")
	private MastersValue receivedStatusId;
	
	@Column(name = "NUMBER_OF_DOCUMENTS")
	private Long numberOfDocuments;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Override
	public Long getKey() {
		return this.key;
	}

	@Override
	public void setKey(Long key) {
		this.key = key;
		
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Long getNumberOfDocuments() {
		return numberOfDocuments;
	}

	public void setNumberOfDocuments(Long numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public OPHealthCheckup getOpHealthCheckup() {
		return opHealthCheckup;
	}

	public void setOpHealthCheckup(OPHealthCheckup opHealthCheckup) {
		this.opHealthCheckup = opHealthCheckup;
	}

	public MastersValue getReceivedStatusId() {
		return receivedStatusId;
	}

	public void setReceivedStatusId(MastersValue receivedStatusId) {
		this.receivedStatusId = receivedStatusId;
	}

}
