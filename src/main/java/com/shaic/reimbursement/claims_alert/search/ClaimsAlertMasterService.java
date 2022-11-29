package com.shaic.reimbursement.claims_alert.search;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.ClaimRemarksAlerts;
import com.shaic.ClaimRemarksDocs;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.fileUpload.MultipleUploadDocumentDTO;
import com.shaic.claim.fileUpload.ReferenceDocument;
import com.shaic.claim.preauth.mapper.PreauthMapper;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.FieldVisitRequest;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;

@Stateless
public class ClaimsAlertMasterService {

	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private MasterService masterService;

	PreauthMapper preauthMapper = PreauthMapper.getInstance();

	public ClaimsAlertMasterService() {
		super();
	}


	public List<ClaimRemarksAlerts> getClaimsAlertsByIntitmation(String intitmationNo){
		Query  query = entityManager.createNamedQuery("ClaimRemarksAlerts.findByIntimationno");
		query = query.setParameter("intitmationNo", intitmationNo);
		List<ClaimRemarksAlerts> listOfClaimAlerts = query.getResultList();

		return listOfClaimAlerts;
	}

	public ClaimRemarksAlerts getClaimsAlertsByKey(Long claimsAlertKey){
		Query  query = entityManager.createNamedQuery("ClaimRemarksAlerts.findBykey");
		query = query.setParameter("key", claimsAlertKey);
		List<ClaimRemarksAlerts> listOfTmpCodes = query.getResultList();

		if(listOfTmpCodes != null && !listOfTmpCodes.isEmpty()){
			return listOfTmpCodes.get(0);
		}
		return null;
	}


	public Intimation getIntimationDetails(String intimationKey){
		Query  query = entityManager.createNamedQuery("Intimation.findByIntimationNumber");
		query = query.setParameter("intimationNo", intimationKey);
		List<Intimation> listOfIntimation = query.getResultList();
		if(listOfIntimation !=null &&
				!listOfIntimation.isEmpty()){
			return listOfIntimation.get(0);
		}

		return null;
	}
	
	private Claim getClaimDetailsByIntiNO(String intimationKey){
		Query findByIntimationNo = entityManager.createNamedQuery("Claim.findByIntimationNo")
				.setParameter("intimationNumber",intimationKey);
		Claim claim = (Claim) findByIntimationNo.getSingleResult();
		return claim;
	}

	public void submitClaimsAlertTable(List<ClaimsAlertTableDTO> dto) {

		for (ClaimsAlertTableDTO idaTableDTO : dto) {

			ClaimRemarksAlerts claimRemarksAlerts =null;
			if(idaTableDTO.getClaimsAlertKey() == null) {
				claimRemarksAlerts = new ClaimRemarksAlerts();
				claimRemarksAlerts.setIntitmationNo(idaTableDTO.getIntimationNo());

				if(idaTableDTO.getAlertCategory() != null 
						&& idaTableDTO.getAlertCategory().getId() !=null) {
					MastersValue mastersValue = new MastersValue();
					mastersValue.setKey(idaTableDTO.getAlertCategory().getId());
					claimRemarksAlerts.setAlertCategory(mastersValue);
				}

				if(idaTableDTO.getEnable()){
					claimRemarksAlerts.setActiveStatus(String.valueOf(1));
				}else if(idaTableDTO.getDisable()){
					claimRemarksAlerts.setActiveStatus(String.valueOf(0));
				}

				claimRemarksAlerts.setRemarks(idaTableDTO.getRemarks());
				claimRemarksAlerts.setCreatedBy(idaTableDTO.getUserName());
				claimRemarksAlerts.setCreatedDate(new Date());
				claimRemarksAlerts.setDeleteFlag("N");
				entityManager.persist(claimRemarksAlerts);

				if(idaTableDTO.getDocsDTOs() !=null
						&& !idaTableDTO.getDocsDTOs().isEmpty()){
					for(ClaimsAlertDocsDTO docs:idaTableDTO.getDocsDTOs()){
						if(docs.getKey() == null){
							docs.setClaimsAlertKey(claimRemarksAlerts.getKey());
							updateClaimsDocsDetails(docs,claimRemarksAlerts.getIntitmationNo());
						}
					}
				}
			}else {
				String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
				claimRemarksAlerts = getClaimsAlertsByKey(idaTableDTO.getClaimsAlertKey());
				claimRemarksAlerts.setCreatedBy(idaTableDTO.getCreatedBy());
				claimRemarksAlerts.setCreatedDate(idaTableDTO.getCreatedDate());
				claimRemarksAlerts.setModifyBy(userId);
				claimRemarksAlerts.setModifiedDate(new Date());
				if(idaTableDTO.getAlertCategory() != null 
						&& idaTableDTO.getAlertCategory().getId() !=null) {
					MastersValue mastersValue = new MastersValue();
					mastersValue.setKey(idaTableDTO.getAlertCategory().getId());
					claimRemarksAlerts.setAlertCategory(mastersValue);
				}
				claimRemarksAlerts.setRemarks(idaTableDTO.getRemarks());
				claimRemarksAlerts.setDeleteFlag(idaTableDTO.getDeleteFlag());
				if(idaTableDTO.getEnable()){
					claimRemarksAlerts.setActiveStatus(String.valueOf(1));
				}else if(idaTableDTO.getDisable()){
					claimRemarksAlerts.setActiveStatus(String.valueOf(0));
				}
				entityManager.merge(claimRemarksAlerts);
				if(idaTableDTO.getDocsDTOs() !=null
						&& !idaTableDTO.getDocsDTOs().isEmpty()){
					for(ClaimsAlertDocsDTO docs:idaTableDTO.getDocsDTOs()){
						if(docs.getKey() == null){
							docs.setClaimsAlertKey(claimRemarksAlerts.getKey());
							updateClaimsDocsDetails(docs,claimRemarksAlerts.getIntitmationNo());
						}
					}
				}
			}
			entityManager.flush();
			entityManager.clear();
		}
	}

	public Map<String,Object> getreferenceDatas(String intimationNo){

		Map<String,Object> containerMap = new HashMap<String,Object>();	

		containerMap.put(SHAConstants.CLAIMS_ALERT_CATEGORY_CONTAINER ,
				masterService.getMasterValueByReference(ReferenceTable.CLAIMS_ALERT_CATEGORY));

		containerMap.put(SHAConstants.INTIMATION_NUMBER ,intimationNo);

		return containerMap;
	}

	private void updateClaimsDocsDetails(ClaimsAlertDocsDTO docs,String intimationNo){

		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		ClaimRemarksDocs remarksDocs =  preauthMapper.getClaimsAlertDocs(docs);
		remarksDocs.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		remarksDocs.setCreatedBy(userId);
		remarksDocs.setActiveStatus("1");
		entityManager.persist(remarksDocs);
		entityManager.flush();
		updateClaimsDocsDetailsInDocTable(remarksDocs,intimationNo);

	}

	private void updateClaimsDocsDetailsInDocTable(ClaimRemarksDocs remarksDocs,String intimationNo){
		Claim claim = getClaimDetailsByIntiNO(intimationNo);

		DocumentDetails  docRec = new DocumentDetails();
		docRec.setClaimNumber(claim.getClaimId());
		docRec.setIntimationNumber(intimationNo);
		docRec.setDocumentType(SHAConstants.REFERDOCS);
		docRec.setFileName(remarksDocs.getFileName());
		docRec.setDocumentToken(remarksDocs.getDocTocken());
		docRec.setDocumentSource(SHAConstants.REFERDOCS);
		docRec.setCreatedBy(remarksDocs.getCreatedBy());
		docRec.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		docRec.setDocReceivedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(docRec);
		entityManager.flush();

	}

	public void deleteClaimsDocsDetails(ClaimsAlertDocsDTO docsDTO){

		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		ClaimRemarksDocs remarksDocs =  preauthMapper.getClaimsAlertDocs(docsDTO);
		remarksDocs.setModifyBy(userId);
		remarksDocs.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		remarksDocs.setActiveStatus("0");
		entityManager.merge(remarksDocs);
		Query findByDocTocken = entityManager.createNamedQuery("DocumentDetails.findByDocToken")
				.setParameter("documentToken", remarksDocs.getDocTocken());
		DocumentDetails documentDetails = (DocumentDetails) findByDocTocken.getSingleResult();
		if(documentDetails !=null){
			documentDetails.setDeletedFlag("Y");
			entityManager.merge(documentDetails);
		}
		entityManager.flush();
	}
	
	public String validateonClickCat(ClaimsAlertTableDTO tableDTO){
		
		SelectValue value = tableDTO.getAlertCategory();
		Claim claim = getClaimDetailsByIntiNO(tableDTO.getIntimationNo());
		if(value.getId() !=null 
				&& value.getId().equals(SHAConstants.CLAIMS_ALERT_FVR_KEY)){
			Query query = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKeyandStatus")
					.setParameter("claimKey",claim.getKey());
			List<FieldVisitRequest> visitRequest = query.getResultList();
			if(visitRequest == null
					|| visitRequest.isEmpty()){
				return "FVR is not assigned for this intitmation";
			}
		}else if(value.getId() !=null 
				&& value.getId().equals(SHAConstants.CLAIMS_ALERT_ROD_KEY)){
			Query query = entityManager.createNamedQuery("Reimbursement.findLatestRODByClaimKeyStatus")
					.setParameter("claimKey",claim.getKey());
			List<Reimbursement> reimbursements = query.getResultList();
			if(reimbursements != null
					&& !reimbursements.isEmpty()){
				return "ROD has already been created for this Intimation";
			}
		}
		return null;
	}

	public boolean validateoncreate(ClaimsAlertTableDTO tableDTO){

		SelectValue value = tableDTO.getAlertCategory();
		Claim claim = getClaimDetailsByIntiNO(tableDTO.getIntimationNo());
		if(value.getId() !=null 
				&& value.getId().equals(SHAConstants.CLAIMS_ALERT_FVR_KEY)){
			Query query = entityManager.createNamedQuery("FieldVisitRequest.findByClaimKeyandStatus")
					.setParameter("claimKey",claim.getKey());
			List<FieldVisitRequest> visitRequest = query.getResultList();
			if(visitRequest == null
					|| visitRequest.isEmpty()){
				return true;
			}
		}else if(value.getId() !=null 
				&& value.getId().equals(SHAConstants.CLAIMS_ALERT_ROD_KEY)){
			Query query = entityManager.createNamedQuery("Reimbursement.findLatestRODByClaimKeyStatus")
					.setParameter("claimKey",claim.getKey());
			List<Reimbursement> reimbursements = query.getResultList();
			if(reimbursements != null
					&& !reimbursements.isEmpty()){
				return true;
			}
		}
		return false;
	}

	public void deleteClaimsAlertDetails(ClaimsAlertTableDTO idaTableDTO){

		ClaimRemarksAlerts claimRemarksAlerts = new ClaimRemarksAlerts();
		claimRemarksAlerts.setKey(idaTableDTO.getClaimsAlertKey());
		claimRemarksAlerts.setIntitmationNo(idaTableDTO.getIntimationNo());
		if(idaTableDTO.getAlertCategory() != null 
				&& idaTableDTO.getAlertCategory().getId() !=null) {
			MastersValue mastersValue = new MastersValue();
			mastersValue.setKey(idaTableDTO.getAlertCategory().getId());
			claimRemarksAlerts.setAlertCategory(mastersValue);
		}
		if(idaTableDTO.getEnable()){
			claimRemarksAlerts.setActiveStatus(String.valueOf(1));
		}else if(idaTableDTO.getDisable()){
			claimRemarksAlerts.setActiveStatus(String.valueOf(0));
		}

		claimRemarksAlerts.setRemarks(idaTableDTO.getRemarks());
		claimRemarksAlerts.setDeleteFlag("Y");
		claimRemarksAlerts.setCreatedBy(idaTableDTO.getCreatedBy());
		claimRemarksAlerts.setCreatedDate(idaTableDTO.getCreatedDate());
		claimRemarksAlerts.setModifyBy(idaTableDTO.getUserName());
		claimRemarksAlerts.setModifiedDate(new Date());
		entityManager.merge(claimRemarksAlerts);
		if(idaTableDTO.getDocsDTOs() !=null
				&& !idaTableDTO.getDocsDTOs().isEmpty()){
			for(ClaimsAlertDocsDTO docs:idaTableDTO.getDocsDTOs()){
				if(docs.getKey() != null){
					deleteClaimsDocsDetails(docs);
				}
			}
		}
		entityManager.flush();
		entityManager.clear();
	}

}
