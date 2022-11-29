package com.shaic.feedback.managerfeedback.previousFeedback;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTable;
import com.shaic.branchmanagerfeedback.BranchMangerFeedbackDetailsTable;
import com.shaic.domain.EmpSecUser;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class BranchManagerPreviousFeedbackService{
	@Inject
	DBCalculationService dBCalculationService;
	@PersistenceContext
	protected EntityManager entityManager;
	BranchManagerPreviousFeedbackTableDTO branchManagerPreviousFeedbackTableDTO ;
	
	public Class<BranchManagerPreviousFeedbackSearchDTO> getDTOClass() {
		// TODO Auto-generated method stub
		return BranchManagerPreviousFeedbackSearchDTO.class;
	}

	
	@SuppressWarnings("unchecked")
	public  List<BranchManagerPreviousFeedbackTableDTO>  getFeedbackDetails(BranchManagerPreviousFeedbackSearchDTO searchDTO) {
		return dBCalculationService.getManagerPreviousFeedback(searchDTO.getFeedbackValue(),searchDTO.getZoneValue(),searchDTO.getBranchValue(),searchDTO.getFeedbackStatusValue(),searchDTO.getFeedbackTypeValue(),
		searchDTO.getFromDate(),searchDTO.getToDate(),entityManager,searchDTO.getUserName());
	}
	

	
	public String getBranchByID(Long key) {
		Query findByBranchCode = entityManager.createNamedQuery("OrganaizationUnit.findByKey");
		findByBranchCode.setParameter("branchKey", key);
		List<OrganaizationUnit> organizationList = (List<OrganaizationUnit>) findByBranchCode.getResultList();
		if(organizationList!= null && !organizationList.isEmpty()) {
			return organizationList.get(0).getOrganizationUnitId();
		}
		return null;
		
	}
	
	public String getBranchDetailsByCode(String branchCode) {
		Query findByCode = entityManager.createNamedQuery("OrganaizationUnit.findByCode");
		findByCode.setParameter("branchCode", branchCode);
		List<OrganaizationUnit> organizationList = (List<OrganaizationUnit>) findByCode.getResultList();
		StringBuffer stringBuffer = new StringBuffer("");
		if(organizationList!=null && !organizationList.isEmpty()) {
			 stringBuffer.append(organizationList.get(0).getOrganizationUnitId()).append(" - ").append(organizationList.get(0).getOrganizationUnitName());
		}
		return stringBuffer.toString();
	}
	
	private String getEmployeeNameById(String initiatorId)
	{
		List<EmpSecUser> empDetailList;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"EmpSecUser.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
		try{
			empDetailList =(List<EmpSecUser>) findByTransactionKey.getResultList();
			if(empDetailList != null && !empDetailList.isEmpty()){
				entityManager.refresh(empDetailList.get(0));
			}
			
			String empNameWithId = empDetailList.get(0).getEmpId();
			empNameWithId += (empDetailList.get(0).getUserName() != null ? (" - " + empDetailList.get(0).getUserName()) : "");
			
			return empNameWithId;
		}
		catch(Exception e)
		{
			return null;
		}
							
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getTechTeamReply(BranchManagerPreviousFeedbackTableDTO searchDTO) {
		
		return null;
		
	}
	public List<BranchManagerPreviousFeedbackTableDTO> getPolicyDetailsByParentKey(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByFeedbackKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		List<BranchManagerPreviousFeedbackTableDTO> resultList = new ArrayList<BranchManagerPreviousFeedbackTableDTO>();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()) {
			BranchManagerPreviousFeedbackTableDTO dto=null;
			for (BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable : branchMangerFeedbackDetailsTableList) {
				dto = new BranchManagerPreviousFeedbackTableDTO();
				dto.setDetailKey(branchMangerFeedbackDetailsTable.getKey());
				dto.setIntimationNo(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setClaimNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setPolicyNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setProposalNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setFeedBackRemarks(branchMangerFeedbackDetailsTable.getDocumentRaisedRemarks());
				dto.setTechnicalTeamReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setCorpTeamReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setClaimsDeptReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setClaimsDepartmentReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setFeedbackReplyRemarks(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				resultList.add(dto);
			}
		}
		return resultList;
		
	}
	public void submitValues(BranchManagerPreviousFeedbackTableDTO bean) {/*
		BranchManagerFeedbackTable branchManagerFeedbackTable = getMainTable(bean.getFeedbackKey());
		if(branchManagerFeedbackTable!=null) {
		branchManagerFeedbackTable.setFeedbackReplyRemarks(bean.getTechnicalTeamReply());
		Status fStatus = new Status();
		fStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
		branchManagerFeedbackTable.setFeedbackStatus(fStatus);
		entityManager.merge(branchManagerFeedbackTable);
		List<BranchManagerPreviousFeedbackTableDTO> list =  bean.getPolicyList();
		for (BranchManagerPreviousFeedbackTableDTO branchManagerFeedbackTableDTO : list) {
			BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable = getchildTable(bean.getDetailKey());
			if(branchMangerFeedbackDetailsTable!=null) {
				branchMangerFeedbackDetailsTable.setDocumentReplyRemarks(branchManagerFeedbackTableDTO.getTechnicalTeamReply());
			entityManager.merge(branchMangerFeedbackDetailsTable);
		}
		}
		
		
		}
	*/}
	public BranchManagerFeedbackTable getMainTable(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchManagerFeedbackTable.findByKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchManagerFeedbackTable> branchMangerFeedbackDetailsTableList = (List<BranchManagerFeedbackTable>) findByKey.getResultList();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()){
			return branchMangerFeedbackDetailsTableList.get(0);
		}
		return null;
		
	}
	
	public BranchMangerFeedbackDetailsTable getchildTable(Long key){
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByKey");
		findByKey.setParameter("primaryKey", key);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()){
			return branchMangerFeedbackDetailsTableList.get(0);
		}
		return null;
	}
}