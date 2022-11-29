/**
 * 
 */
package com.shaic.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.shaic.arch.fields.dto.AbstractEntity;

/**
 * @author ntv.vijayar
 *
 */
@Entity
@Table(name="IMS_SFX_DOC_METADATA")
@NamedQueries({
@NamedQuery(name="DocUploadToPremia.findAll", query="SELECT p FROM DocUploadToPremia p"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadStatus", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.pfdUpPremiaUploadSts is NULL order by p.pfdUpFfaxSubmitId desc"),
@NamedQuery(name="DocUploadToPremia.findByIntimation", query="SELECT p FROM DocUploadToPremia p where p.pfdUpIntmNo = :intimationNo"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadIndividually", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N'and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is NULL and p.pfdUpIntmNo = :intimationNo"),
@NamedQuery(name="DocUploadToPremia.findByIntimationAndDocType", query="SELECT p FROM DocUploadToPremia p where p.pfdUpIntmNo = :intimationNo and p.pfdUpDocType = :docType and p.galaxyReadFlag = 'Y' order by p.pfdUpReportId desc"),
@NamedQuery(name="DocUploadToPremia.findDocumentByIntimation", query="SELECT p FROM DocUploadToPremia p where p.pfdUpIntmNo = :intimationNo and p.galaxyReadFlag = 'Y' order by p.pfdUpReportId desc"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadWithoutFVR", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is NULL and p.pfdUpDocType <> 'FVR' order by p.pfdUpFfaxSubmitId asc"),
@NamedQuery(name="DocUploadToPremia.findByPendingCount", query="SELECT count(p) FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is NULL and p.pfdUpDocType <> 'FVR' order by p.pfdUpFfaxSubmitId asc"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadWithoutFVRDesc", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is NULL and p.pfdUpDocType <> 'FVR' order by p.pfdUpFfaxSubmitId desc"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadWithFVR", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is NULL and p.pfdUpDocType = 'FVR' order by p.pfdUpFfaxSubmitId asc"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadWithFVRERROR", query="SELECT p FROM DocUploadToPremia p where p.pfdUpFlexYN01 = 'N' and p.fileSyncFlag = 1 and p.pfdUpPremiaUploadSts is not null and p.pfdUpPremiaUploadSts <> 'Success' and p.pfdUpPremiaUploadSts <> 'ERROR' and p.pfdUpDocType = 'FVR' order by p.pfdUpFfaxSubmitId asc"),
@NamedQuery(name="DocUploadToPremia.findByDocUploadWithNoClaim", query="SELECT p FROM DocUploadToPremia p where p.pfdUpPremiaUploadSts = 'NO CLAIM' and p.pfdUpDocType <> 'FVR' order by p.pfdUpFfaxSubmitId desc"),
@NamedQuery(name="DocUploadToPremia.findByIntimationAndAllDocType", query="SELECT p FROM DocUploadToPremia p where p.pfdUpIntmNo = :intimationNo and (p.pfdUpDocType = :docType1 or  p.pfdUpDocType = :docType2) and p.galaxyReadFlag = 'Y' order by p.pfdUpReportId desc"),
@NamedQuery(name="DocUploadToPremia.findByReportId", query="SELECT p FROM DocUploadToPremia p where p.pfdUpReportId = :reportId"),
//@NamedQuery(name="DocUploadToPremia.findAll", query="SELECT p FROM DocUploadToPremia p")
})
public class DocUploadToPremia extends AbstractEntity {
	
	
	@Column(name="PFDUP_INTM_NO")
	private String pfdUpIntmNo;
	
	@Id
	@Column(name = "PFDUP_REPORT_ID")
	private String pfdUpReportId;
	
	@Column(name = "PFDUP_DOC_TYPE")
	private String pfdUpDocType;
	
	//@Temporal(TemporalType.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PFDUP_FFAX_SUBMIT_DT")
	private Date pfdUpFfaxSubmitId;
	
	//@Temporal(TemporalType.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PFDUP_PREMIA_ACK_DT")
	private Date pfdUpPremiaAckDt;
	
	@Column(name = "PFDUP_PREMIA_UPLOAD_STS")
	private String pfdUpPremiaUploadSts;
	
	@Column(name = "PFDUP_UPLOAD_COUNT")
	private Long pfdUploadCount;
	
	@Column(name = "PFDUP_FLEX_01")
	private String pfdUpFlex01;
	
	@Column(name = "PFDUP_FLEX_02")
	private String pfdUpFlex02;
	
	@Column(name = "PFDUP_FLEX_03")
	private String pfdUpFlex03;
	
	@Column(name = "PFDUP_FLEX_YN_01")
	private String pfdUpFlexYN01;
	
	@Column(name = "PFDUP_FLEX_YN_02")
	private String pfdUpFlexYN02;
	
	@Column(name = "PFDUP_FLEX_YN_03")
	private String pfdUpFlexYN03;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PFDUP_FLEX_DT_01")
	private Date pfdUpFlexDt01;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PFDUP_FLEX_DT_02")
	private Date pfdUpFlexDt02;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "PFDUP_FLEX_DT_03")
	private Date pfdUpFlexDt03;
	
	@Column(name = "PFDUP_FILE_NAME")
	private String pfdUpFileName;
	
	@Column(name = "PFDUP_CES_SYS_ID")
	private Long pdfUpCesSysId;
	
	@Column(name = "PFDUP_FFAX_AMT")
	private Long pfdUpFFAXAmt;
	
	@Column(name = "PFDUP_CPU_ESC")
	private String pfdUpCpuEsc;
	
	@Column(name = "PFDUP_FLEX_04")
	private String pfdUpFlex04;
	
	@Column(name = "GAL_READ_FLAG")
	private String galaxyReadFlag;
	
	@Column(name = "FILESYNC_FLAG")
	private Integer fileSyncFlag;
	
	@Column(name = "FROM_CPU")
	private String fromCpu;
	
	@Override
	public Long getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setKey(Long key) {
		// TODO Auto-generated method stub
		
	}

	
	public String getPfdUpReportId() {
		return pfdUpReportId;
	}

	public void setPfdUpReportId(String pfdUpReportId) {
		this.pfdUpReportId = pfdUpReportId;
	}

	public String getPfdUpDocType() {
		return pfdUpDocType;
	}

	public void setPfdUpDocType(String pfdUpDocType) {
		this.pfdUpDocType = pfdUpDocType;
	}

	public Date getPfdUpFfaxSubmitId() {
		return pfdUpFfaxSubmitId;
	}

	public void setPfdUpFfaxSubmitId(Date pfdUpFfaxSubmitId) {
		this.pfdUpFfaxSubmitId = pfdUpFfaxSubmitId;
	}

	public Date getPfdUpPremiaAckDt() {
		return pfdUpPremiaAckDt;
	}

	public void setPfdUpPremiaAckDt(Date pfdUpPremiaAckDt) {
		this.pfdUpPremiaAckDt = pfdUpPremiaAckDt;
	}

	public String getPfdUpPremiaUploadSts() {
		return pfdUpPremiaUploadSts;
	}

	public void setPfdUpPremiaUploadSts(String pfdUpPremiaUploadSts) {
		this.pfdUpPremiaUploadSts = pfdUpPremiaUploadSts;
	}

	public Long getPfdUploadCount() {
		return pfdUploadCount;
	}

	public void setPfdUploadCount(Long pfdUploadCount) {
		this.pfdUploadCount = pfdUploadCount;
	}

	public String getPfdUpFlex01() {
		return pfdUpFlex01;
	}

	public void setPfdUpFlex01(String pfdUpFlex01) {
		this.pfdUpFlex01 = pfdUpFlex01;
	}

	public String getPfdUpFlex02() {
		return pfdUpFlex02;
	}

	public void setPfdUpFlex02(String pfdUpFlex02) {
		this.pfdUpFlex02 = pfdUpFlex02;
	}

	public String getPfdUpFlex03() {
		return pfdUpFlex03;
	}

	public void setPfdUpFlex03(String pfdUpFlex03) {
		this.pfdUpFlex03 = pfdUpFlex03;
	}

	public String getPfdUpFlexYN01() {
		return pfdUpFlexYN01;
	}

	public void setPfdUpFlexYN01(String pfdUpFlexYN01) {
		this.pfdUpFlexYN01 = pfdUpFlexYN01;
	}

	public String getPfdUpFlexYN02() {
		return pfdUpFlexYN02;
	}

	public void setPfdUpFlexYN02(String pfdUpFlexYN02) {
		this.pfdUpFlexYN02 = pfdUpFlexYN02;
	}

	public String getPfdUpFlexYN03() {
		return pfdUpFlexYN03;
	}

	public void setPfdUpFlexYN03(String pfdUpFlexYN03) {
		this.pfdUpFlexYN03 = pfdUpFlexYN03;
	}

	public Date getPfdUpFlexDt01() {
		return pfdUpFlexDt01;
	}

	public void setPfdUpFlexDt01(Date pfdUpFlexDt01) {
		this.pfdUpFlexDt01 = pfdUpFlexDt01;
	}

	public Date getPfdUpFlexDt02() {
		return pfdUpFlexDt02;
	}

	public void setPfdUpFlexDt02(Date pfdUpFlexDt02) {
		this.pfdUpFlexDt02 = pfdUpFlexDt02;
	}

	public Date getPfdUpFlexDt03() {
		return pfdUpFlexDt03;
	}

	public void setPfdUpFlexDt03(Date pfdUpFlexDt03) {
		this.pfdUpFlexDt03 = pfdUpFlexDt03;
	}

	public String getPfdUpFileName() {
		return pfdUpFileName;
	}

	public void setPfdUpFileName(String pfdUpFileName) {
		this.pfdUpFileName = pfdUpFileName;
	}

	public Long getPdfUpCesSysId() {
		return pdfUpCesSysId;
	}

	public void setPdfUpCesSysId(Long pdfUpCesSysId) {
		this.pdfUpCesSysId = pdfUpCesSysId;
	}

	public Long getPfdUpFFAXAmt() {
		return pfdUpFFAXAmt;
	}

	public void setPfdUpFFAXAmt(Long pfdUpFFAXAmt) {
		this.pfdUpFFAXAmt = pfdUpFFAXAmt;
	}

	public String getPfdUpCpuEsc() {
		return pfdUpCpuEsc;
	}

	public void setPfdUpCpuEsc(String pfdUpCpuEsc) {
		this.pfdUpCpuEsc = pfdUpCpuEsc;
	}

	public String getPfdUpFlex04() {
		return pfdUpFlex04;
	}

	public void setPfdUpFlex04(String pfdUpFlex04) {
		this.pfdUpFlex04 = pfdUpFlex04;
	}

	public String getPfdUpIntmNo() {
		return pfdUpIntmNo;
	}

	public void setPfdUpIntmNo(String pfdUpIntmNo) {
		this.pfdUpIntmNo = pfdUpIntmNo;
	}

	public String getGalaxyReadFlag() {
		return galaxyReadFlag;
	}

	public void setGalaxyReadFlag(String galaxyReadFlag) {
		this.galaxyReadFlag = galaxyReadFlag;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        } else if (getPfdUpReportId() == null) {
            return obj == this;
        } else {
            return getPfdUpReportId().equals(((DocUploadToPremia) obj).getPfdUpReportId());
        }
    }

    @Override
    public int hashCode() {
        if (pfdUpReportId != null) {
            return pfdUpReportId.hashCode();
        } else {
            return super.hashCode();
        }
    }

	public Integer getFileSyncFlag() {
		return fileSyncFlag;
	}

	public void setFileSyncFlag(Integer fileSyncFlag) {
		this.fileSyncFlag = fileSyncFlag;
	}

	public String getFromCpu() {
		return fromCpu;
	}

	public void setFromCpu(String fromCpu) {
		this.fromCpu = fromCpu;
	}
	
}
