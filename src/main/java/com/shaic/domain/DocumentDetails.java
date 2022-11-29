package com.shaic.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;



@Entity
@Table(name="IMS_CLS_DOCUMENT_DETAILS")
@NamedQueries({
@NamedQuery(name="DocumentDetails.findAll", query="SELECT p FROM DocumentDetails p"),
//@NamedQuery(name="DocumentDetails.findByIntimationNo", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber order by p.documentType asc")
@NamedQuery(name="DocumentDetails.findByIntimationNo", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber order by p.docSubmittedDate asc"),
@NamedQuery(name="DocumentDetails.findByIntimationNoWithFlag", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.docSubmittedDate asc"),
//@NamedQuery(name="DocumentDetails.findByIntimationNoOrderByCreatedDate", query="SELECT d.intimationNumber,d.claimNumber,d.documentType,d.sfFileName,d.documentSource,d.documentToken,d.fileName,d.createdDate,d.reimbursementNumber,d.cashlessNumber FROM DocumentDetails d WHERE d.intimationNumber = :intimationNumber and d.key IN (SELECT MIN(p.key) FROM DocumentDetails p where ((p.intimationNumber = :intimationNumber and p.documentType <> 'Lumen' and (p.deletedFlag is null or p.deletedFlag = 'N')) or (p.lumenRequest.key = :lumenRequestKey)) group by p.documentToken) order by d.createdDate asc"),
@NamedQuery(name="DocumentDetails.findByIntimationNoOrderByCreatedDate", query="SELECT d.intimationNumber,d.claimNumber,d.documentType,d.sfFileName,d.documentSource,d.documentToken,d.fileName,d.createdDate,d.reimbursementNumber,d.cashlessNumber FROM DocumentDetails d where ((d.intimationNumber = :intimationNumber and d.documentType <> 'Lumen' and (d.deletedFlag is null or d.deletedFlag = 'N')) or (d.lumenRequest.key = :lumenRequestKey)) order by d.createdDate asc"),
@NamedQuery(name="DocumentDetails.findQueryByIntimationNo" , query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber and lower(p.documentType) like :query"),
@NamedQuery(name="DocumentDetails.findByDocumentToken" , query="SELECT p FROM DocumentDetails p where p.documentToken = :documentToken"),
@NamedQuery(name="DocumentDetails.findByDocToken" , query="SELECT p FROM DocumentDetails p where p.documentToken = :documentToken and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.key asc"),
@NamedQuery(name="DocumentDetails.findByKey" , query="SELECT p FROM DocumentDetails p where p.key = :key"),
@NamedQuery(name="DocumentDetails.findLatestDocumentByIntimationNo", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber order by  p.key desc"),
@NamedQuery(name="DocumentDetails.findQueryByRodNo" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber = :reimbursementNumber and (Lower(p.documentType) in (:billsummary,:billassessment))and lower(p.documentSource) like :financialapproval and (p.deletedFlag is null or p.deletedFlag = 'N')"),
@NamedQuery(name="DocumentDetails.findLatestDocumentByIntimationNoAndApplicationID", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber and p.sfApplicationId = :applicationId order by  p.key desc"),
@NamedQuery(name="DocumentDetails.findByIntimationNoAndDocSource", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber and p.documentSource like :documentSource and (p.deletedFlag is null or p.deletedFlag = 'N') order by p.docSubmittedDate asc"),
@NamedQuery(name="DocumentDetails.findByRodNo" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber like :reimbursementNumber"),
@NamedQuery(name="DocumentDetails.findByRodKey" , query="SELECT p FROM DocumentDetails p where p.rodKey = :rodKey"),
@NamedQuery(name="DocumentDetails.findByDocType" , query="SELECT p FROM DocumentDetails p where p.documentType = :documentType and (p.deletedFlag is null or p.deletedFlag = 'N')"),
@NamedQuery(name="DocumentDetails.findByIntimationNoDocType" , query="SELECT p FROM DocumentDetails p where p.documentType = :documentType and p.intimationNumber = :intimationNumber and (p.deletedFlag is null or p.deletedFlag = 'N')"),
@NamedQuery(name="DocumentDetails.findQueryLetterByRodNumber" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber = :reimbursementNumber and lower(p.documentType) like :ReimbursementQueryLetter order by p.key desc" ),
@NamedQuery(name="DocumentDetails.findRejectionLetterByRodNumber" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber = :reimbursementNumber and lower(p.documentType) like :ReimbursementRejectionLetter order by p.key desc" ),
@NamedQuery(name="DocumentDetails.findByIntimationNoWithDocType", query="SELECT p FROM DocumentDetails p where p.intimationNumber = :intimationNumber and (p.deletedFlag is null or p.deletedFlag = 'N') and p.documentType = :documentType order by p.createdDate Desc"),
@NamedQuery(name="DocumentDetails.findByIntimationNoWithFileNameAndFileType" , query="SELECT p FROM DocumentDetails p where p.fileName = :fileName and p.intimationNumber = :intimationNumber and p.documentType = :documentType and (p.deletedFlag is null or p.deletedFlag = 'N')"),
@NamedQuery(name="DocumentDetails.findBillAssessmentVersions" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber like :reimbursementNumber and (Lower(p.documentType) in (:billsummary,:billassessment))and lower(p.documentSource) like :financialapproval"),
@NamedQuery(name="DocumentDetails.findQueryByRodNoDesc" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber = :reimbursementNumber and (Lower(p.documentType) in (:billsummary,:billassessment))and lower(p.documentSource) like :financialapproval and (p.deletedFlag is null or p.deletedFlag = 'N') order by  p.key desc"),
@NamedQuery(name="DocumentDetails.findRODBillByRodNoDesc" , query="SELECT p FROM DocumentDetails p where p.reimbursementNumber like :reimbursementNumber and (Lower(p.documentType) in (Lower('BillSummaryOtherProducts'),Lower('BillAssessmentSheetSCRC'),Lower('PABillAssessmentSheet'))) order by 1 desc"),
})

public class DocumentDetails extends AbstractEntity{
	
	@Id
	@SequenceGenerator(name="IMS_DOCUMENT_DETAILS_KEY_GENERATOR", sequenceName = "SEQ_DOCUMENT_DETAILS_KEY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_DOCUMENT_DETAILS_KEY_GENERATOR") 
	@Column(name="DOCUMENT_DETAILS_KEY")
	private Long key;
	
	
	@Column(name = "INTIMATION_NUMBER")
	private String intimationNumber;
	
	@Column(name = "CLAIM_NUMBER")
	private String claimNumber;
	
	@Column(name = "REIMBURSEMENT_NUMBER")
	private String reimbursementNumber;
	
	@Column(name = "CASHLESS_NUMBER")
	private String cashlessNumber;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "DOCUMENT_TYPE")
	private String documentType;
	
	@Column(name = "DOCUMENT_URL")
	private String documentUrl;
	
	@Column(name = "DOCUMENT_TOKEN")
	private Long documentToken;
	
	@Column(name = "DOCUMENT_SOURCE")
	private String documentSource;
	
	@Column(name = "SF_APPLICATION_ID")
	private Long sfApplicationId;
	
	@Column(name = "SF_DOCUMENT_ID")
	private Long sfDocumentId;
	
	@Column(name = "SF_FILE_SIZE")
	private Long sfFileSize;
	
	@Column(name = "SF_FILE_NAME")
	private String sfFileName;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_SUBMITTED_DATE")
	private Date docSubmittedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_ACKNOWLEDGEMENT_DATE")
	private Date docAcknowledgementDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "DELETED_FLAG")
	private String deletedFlag;
	
	@Column(name = "PHYSICAL_DOC_STS")
	private String physicalDocumentStatus;
	
	@Column(name = "DOC_TYPE_ID")
	private Long documentTypeId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DOC_RECEIVED_DATE")
	private Date docReceivedDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="LUMEN_REQUEST_KEY", nullable=false)
	private LumenRequest lumenRequest;
	
	@Column(name = "ROD_KEY")
	private Long rodKey;
	
	@Column(name = "DOC_DEL_REMARKS")
	private String documentDeleteRemarks;
	
	@Column(name = "DOC_VERSION")
	private String docVersion;
	
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

	public String getReimbursementNumber() {
		return reimbursementNumber;
	}

	public void setReimbursementNumber(String reimbursementNumber) {
		this.reimbursementNumber = reimbursementNumber;
	}

	public String getCashlessNumber() {
		return cashlessNumber;
	}

	public void setCashlessNumber(String cashlessNumber) {
		this.cashlessNumber = cashlessNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public Long getDocumentToken() {
		return documentToken;
	}

	public void setDocumentToken(Long documentToken) {
		this.documentToken = documentToken;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Long getSfApplicationId() {
		return sfApplicationId;
	}

	public void setSfApplicationId(Long sfApplicationId) {
		this.sfApplicationId = sfApplicationId;
	}

	public Long getSfDocumentId() {
		return sfDocumentId;
	}

	public void setSfDocumentId(Long sfDocumentId) {
		this.sfDocumentId = sfDocumentId;
	}

	public Long getSfFileSize() {
		return sfFileSize;
	}

	public void setSfFileSize(Long sfFileSize) {
		this.sfFileSize = sfFileSize;
	}

	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDocSubmittedDate() {
		return docSubmittedDate;
	}

	public void setDocSubmittedDate(Date docSubmittedDate) {
		this.docSubmittedDate = docSubmittedDate;
	}

	public Date getDocAcknowledgementDate() {
		return docAcknowledgementDate;
	}

	public void setDocAcknowledgementDate(Date docAcknowledgementDate) {
		this.docAcknowledgementDate = docAcknowledgementDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSfFileName() {
		return sfFileName;
	}

	public void setSfFileName(String sfFileName) {
		this.sfFileName = sfFileName;
	}
	
	
	public String getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	public String getPhysicalDocumentStatus() {
		return physicalDocumentStatus;
	}

	public void setPhysicalDocumentStatus(String physicalDocumentStatus) {
		this.physicalDocumentStatus = physicalDocumentStatus;
	}

	public Long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public Date getDocReceivedDate() {
		return docReceivedDate;
	}

	public void setDocReceivedDate(Date docReceivedDate) {
		this.docReceivedDate = docReceivedDate;
	}
	
	public LumenRequest getLumenRequest() {
		return lumenRequest;
	}

	public void setLumenRequest(LumenRequest lumenRequest) {
		this.lumenRequest = lumenRequest;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((DocumentDetails) obj).getKey());
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

	public Long getRodKey() {
		return rodKey;
	}

	public void setRodKey(Long rodKey) {
		this.rodKey = rodKey;
	}

	public String getDocumentDeleteRemarks() {
		return documentDeleteRemarks;
	}

	public void setDocumentDeleteRemarks(String documentDeleteRemarks) {
		this.documentDeleteRemarks = documentDeleteRemarks;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}