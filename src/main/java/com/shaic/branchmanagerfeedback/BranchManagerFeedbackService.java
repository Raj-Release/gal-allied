package com.shaic.branchmanagerfeedback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.EmpSecUser;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;



@Stateless
public class BranchManagerFeedbackService extends AbstractDAO<BranchManagerFeedbackTable>{
	@Inject
	DBCalculationService dBCalculationService;
	@PersistenceContext
	protected EntityManager entityManager;
	TechnicalDepartmentFeedbackDTO technicalDepartmentFeedbackDTO;
	@Override
	public Class<BranchManagerFeedbackTable> getDTOClass() {
		// TODO Auto-generated method stub
		return BranchManagerFeedbackTable.class;
	}
/*	public Page<BranchManagerFeedbackTable> search(TechnicalDepartmentFeedbackDTO technicalDepartmentFeedbackDTO){
		SelectValue zoneValue = technicalDepartmentFeedbackDTO.getZoneValue() != null && !technicalDepartmentFeedbackDTO.getZoneValue().getValue().isEmpty() ? technicalDepartmentFeedbackDTO.getZoneValue():null;
		SelectValue branchValue = technicalDepartmentFeedbackDTO.getBranchValue() != null && !technicalDepartmentFeedbackDTO.getBranchValue().getValue().isEmpty() ? technicalDepartmentFeedbackDTO.getBranchValue():null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<BranchManagerFeedbackTable> criteriaQuery = criteriaBuilder.createQuery(BranchManagerFeedbackTable.class);
		Root<BranchManagerFeedbackTable> root = criteriaQuery.from(BranchManagerFeedbackTable.class);
		List<Predicate> conditionList = new ArrayList<Predicate>();
		if(zoneValue!=null) {
			Predicate condition1 = criteriaBuilder.like(root.<BranchManagerFeedbackTable>get("managerFeedbackPolicy").<String>get("feedbackZoneCode"), "%"+zoneValue+"%");
			conditionList.add(condition1);
		}
		return null;
		
	}*/
	
	/*public BeanItemContainer<SelectValue>  getZoneValue() {
		return dBCalculationService.getZoneCode();
	}*/
	
	@SuppressWarnings("unchecked")
	public  List<BranchManagerFeedbackTableDTO>  getFeedbackDetails(TechnicalDepartmentFeedbackDTO searchDTO) {
		
		return dBCalculationService.getManagerFeedbackReport(searchDTO.getFeedbackValue(),searchDTO.getZoneValue(),searchDTO.getBranchValue(),searchDTO.getFeedbackStatusValue(),searchDTO.getFeedbackTypeValue(),
		searchDTO.getFromDate(),searchDTO.getToDate(),entityManager);
		
		
		/*Query findAll = entityManager.createNamedQuery("BranchManagerFeedbackTable.findByFilter");
		findAll.setParameter("feedbackTypeKey",searchDTO.getFeedbackValue().getId());
		findAll.setParameter("feedbackStatusKey", searchDTO.getFeedbackStatusValue().getId());
		String branchCode = getBranchByID(searchDTO.getBranchValue().getId());
		List<BranchManagerFeedbackTableDTO> resultList = new ArrayList<BranchManagerFeedbackTableDTO>();
		if(branchCode!=null){
		findAll.setParameter("branchCode",branchCode );
		List<BranchManagerFeedbackTable> resultOfficeCodeList = (List<BranchManagerFeedbackTable>) findAll
				.getResultList();
		
		BeanItemContainer<SelectValue> branchvalue = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		BranchManagerFeedbackTableDTO branchManagerFeedbackTableDTO = null;
		
		for (BranchManagerFeedbackTable branchManagerFeedbackTable : resultOfficeCodeList) {
			branchManagerFeedbackTableDTO = new BranchManagerFeedbackTableDTO();
			String branchDetails = getBranchDetailsByCode(branchManagerFeedbackTable.getFeedbackBranchCode());
			String EmpDetails = getEmployeeNameById(branchManagerFeedbackTable.getCreatedBy());
			branchManagerFeedbackTableDTO.setBranchDetails(branchDetails +" "+ EmpDetails);
			branchManagerFeedbackTableDTO.setReportedDate(branchManagerFeedbackTable.getCreatedDate()!=null ? new SimpleDateFormat("dd-MMM-YYYY").format(branchManagerFeedbackTable.getCreatedDate()):"");
			branchManagerFeedbackTableDTO.setFeedbackType(branchManagerFeedbackTable.getFeedbackRating().getValue());
			branchManagerFeedbackTableDTO.setFeedbackKey(branchManagerFeedbackTable.getFeedbackType().getKey());
			branchManagerFeedbackTableDTO.setFeedbackStatus(branchManagerFeedbackTable.getFeedbackStatus().getProcessValue());
			branchManagerFeedbackTableDTO.setFeedbackStatusKey(branchManagerFeedbackTable.getFeedbackStatus().getKey());
			branchManagerFeedbackTableDTO.setFeedbackRemarksOverall(branchManagerFeedbackTable.getFeedbackReplyRemarks());
			branchManagerFeedbackTableDTO.setFeedbackRemarksOverall(branchManagerFeedbackTable.getFeedbackRaisedRemarks());
			resultList.add(branchManagerFeedbackTableDTO);
		}
		}
		return resultList;

		*/
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
	public List<BranchManagerFeedbackTableDTO> getTechTeamReply(BranchManagerFeedbackTableDTO searchDTO) {
		
		return null;
		
	}
	public List<BranchManagerFeedbackTableDTO> getPolicyDetailsByParentKey(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByFeedbackKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		List<BranchManagerFeedbackTableDTO> resultList = new ArrayList<BranchManagerFeedbackTableDTO>();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()) {
			BranchManagerFeedbackTableDTO dto=null;
			for (BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable : branchMangerFeedbackDetailsTableList) {
				dto = new BranchManagerFeedbackTableDTO();
				entityManager.refresh(branchMangerFeedbackDetailsTable);
				dto.setDetailKey(branchMangerFeedbackDetailsTable.getKey());
				dto.setIntimationNo(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setPolicyRemarks(branchMangerFeedbackDetailsTable.getDocumentRaisedRemarks());
				resultList.add(dto);
			}
		}
		return resultList;
		
	}
	public List<BranchManagerFeedbackTableDTO> getProposalDetailsByParentKey(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByFeedbackKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		List<BranchManagerFeedbackTableDTO> resultList = new ArrayList<BranchManagerFeedbackTableDTO>();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()) {
			BranchManagerFeedbackTableDTO dto=null;
			for (BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable : branchMangerFeedbackDetailsTableList) {
				dto = new BranchManagerFeedbackTableDTO();
				entityManager.refresh(branchMangerFeedbackDetailsTable);
				dto.setDetailKey(branchMangerFeedbackDetailsTable.getKey());
				dto.setProposalNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setPolicyRemarks(branchMangerFeedbackDetailsTable.getDocumentRaisedRemarks());
				resultList.add(dto);
			}
		}
		return resultList;
		
	}
	public List<BranchManagerFeedbackTableDTO> getClaimDetailsByParentKey(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByFeedbackKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		List<BranchManagerFeedbackTableDTO> resultList = new ArrayList<BranchManagerFeedbackTableDTO>();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()) {
			BranchManagerFeedbackTableDTO dto=null;
			for (BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable : branchMangerFeedbackDetailsTableList) {
				dto = new BranchManagerFeedbackTableDTO();
				entityManager.refresh(branchMangerFeedbackDetailsTable);
				dto.setDetailKey(branchMangerFeedbackDetailsTable.getKey());
				dto.setIntimationNo(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setClaimRemarks(branchMangerFeedbackDetailsTable.getDocumentRaisedRemarks());
				resultList.add(dto);
			}
		}
		return resultList;
		
	}
	public void submitValues(BranchManagerFeedbackTableDTO bean) {		
		
		BranchManagerFeedbackTable branchManagerFeedbackTable = getMainTable(bean.getFeedbackKey());
		BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable =  getchildTable(bean.getFeedBackDtlsKey());
		MastersValue fStatus = new MastersValue();
		
		if(branchManagerFeedbackTable!=null && branchManagerFeedbackTable.getFeedbackType().getKey().equals(ReferenceTable.POLICY_FEEDBACK_TYPE_KEY)) {
		branchManagerFeedbackTable.setFeedbackReplyRemarks(bean.getTechnicalTeamReply());
		fStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
		branchManagerFeedbackTable.setFeedbackStatus(fStatus);
		branchManagerFeedbackTable.setFeedbackRepliedBy(bean.getUsername());
		branchManagerFeedbackTable.setFeedbackRepliedDate(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(branchManagerFeedbackTable);	
		 
		if(branchMangerFeedbackDetailsTable!=null) {
			branchMangerFeedbackDetailsTable.setDocumentReplyRemarks(bean.getTechnicalTeamReply());
			MastersValue childFbStatus = new MastersValue();
			childFbStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
			branchMangerFeedbackDetailsTable.setFeedbackStatus(childFbStatus);
			
			branchMangerFeedbackDetailsTable.setModifiedBy(bean.getUsername());
			branchMangerFeedbackDetailsTable.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
		entityManager.merge(branchMangerFeedbackDetailsTable);
		
		}
	}
		else if(branchManagerFeedbackTable!=null && branchManagerFeedbackTable.getFeedbackType().getKey().equals(ReferenceTable.PROPOSAL_FEEDBACK_TYPE_KEY)) {

			branchManagerFeedbackTable.setFeedbackReplyRemarks(bean.getCorporateMedicalUnderwritingReply());
			fStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
			branchManagerFeedbackTable.setFeedbackStatus(fStatus);
			branchManagerFeedbackTable.setFeedbackRepliedBy(bean.getUsername());
			branchManagerFeedbackTable.setFeedbackRepliedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(branchManagerFeedbackTable);
			
			if(branchMangerFeedbackDetailsTable!=null) {
				MastersValue childFbStatus = new MastersValue();
				childFbStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
				branchMangerFeedbackDetailsTable.setFeedbackStatus(childFbStatus);
				branchMangerFeedbackDetailsTable.setDocumentReplyRemarks(bean.getCorporateMedicalUnderwritingReply());
				branchMangerFeedbackDetailsTable.setModifiedBy(bean.getUsername());
				branchMangerFeedbackDetailsTable.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
			entityManager.merge(branchMangerFeedbackDetailsTable);
		}
			
		}else if(branchManagerFeedbackTable!=null && branchManagerFeedbackTable.getFeedbackType().getKey().equals(ReferenceTable.CLAIM_FEEDBACK_TYPE_KEY)) {

			branchManagerFeedbackTable.setFeedbackReplyRemarks(bean.getClaimsDepartmentReply());
			fStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
			branchManagerFeedbackTable.setFeedbackStatus(fStatus);
			branchManagerFeedbackTable.setFeedbackRepliedBy(bean.getUsername());
			branchManagerFeedbackTable.setFeedbackRepliedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(branchManagerFeedbackTable);
			
			if(branchMangerFeedbackDetailsTable!=null) {
				MastersValue childFbStatus = new MastersValue();
				childFbStatus.setKey(ReferenceTable.FEEDBACK_RESPONDED_KEY);
				branchMangerFeedbackDetailsTable.setFeedbackStatus(childFbStatus);
				branchMangerFeedbackDetailsTable.setDocumentReplyRemarks(bean.getClaimsDepartmentReply());
				branchMangerFeedbackDetailsTable.setModifiedBy(bean.getUsername());
				branchMangerFeedbackDetailsTable.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
			entityManager.merge(branchMangerFeedbackDetailsTable);
		}
			
		}
}
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
	
	public List<BranchManagerFeedbackTableDTO> getViewPolicyDetails(Long feedbackKey) {
		Query findByKey = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByFeedbackKey");
		findByKey.setParameter("feedbackKey", feedbackKey);
		List<BranchMangerFeedbackDetailsTable> branchMangerFeedbackDetailsTableList = (List<BranchMangerFeedbackDetailsTable>) findByKey.getResultList();
		List<BranchManagerFeedbackTableDTO> resultList = new ArrayList<BranchManagerFeedbackTableDTO>();
		if(branchMangerFeedbackDetailsTableList!=null && !branchMangerFeedbackDetailsTableList.isEmpty()) {
			BranchManagerFeedbackTableDTO dto=null;
			for (BranchMangerFeedbackDetailsTable branchMangerFeedbackDetailsTable : branchMangerFeedbackDetailsTableList) {
				dto = new BranchManagerFeedbackTableDTO();
				dto.setDetailKey(branchMangerFeedbackDetailsTable.getKey());
				dto.setIntimationNo(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setClaimNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setPolicyNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setProposalNumber(branchMangerFeedbackDetailsTable.getDocumentNo());
				dto.setFeedBackRemarks(branchMangerFeedbackDetailsTable.getDocumentRaisedRemarks());
				dto.setTechnicalTeamReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setCorpTeamReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setClaimsDeptReply(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				dto.setFeedbackReplyRemarks(branchMangerFeedbackDetailsTable.getDocumentReplyRemarks());
				resultList.add(dto);
			}
		}
		return resultList;
		
	}
}
























