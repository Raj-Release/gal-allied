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

@Entity
@Table(name="IMS_CLS_COV_DOC_LIST")
@NamedQueries({
	@NamedQuery(name="CoveringLetterDocDetails.findAll", query="SELECT o FROM CoveringLetterDocDetails o"),
	@NamedQuery(name="CoveringLetterDocDetails.findByClaimKey", query="SELECT o FROM CoveringLetterDocDetails o where o.claim.key = :claimKey order by o.key asc")
})
public class CoveringLetterDocDetails extends AbstractEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMS_CLS_COV_DOC_LIST_KEY_GENERATOR", sequenceName = "SEQ_COV_DOC_LIST_KEY"  ,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMS_CLS_COV_DOC_LIST_KEY_GENERATOR" ) 
	@Column(name="DOC_LIST_KEY")
	private Long key;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CLAIM_KEY", nullable=false)
	private Claim claim;
	
	@Column(name="DOCUMENT_TYPE_ID")
	private Long docTypeId;
	
	@Column(name="PROCESS_TYPE")
	private String processType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;	
	
	public Long getKey() {

		return key;
	}
	
	public void setKey(Long key) {

		this.key = key;
	}

	public Claim getClaim() {
		return claim;
	}

	public void setClaim(Claim claim) {
		this.claim = claim;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getKey() == null) {
            return obj == this;
        } else {
            return getKey().equals(((CoveringLetterDocDetails) obj).getKey());
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
