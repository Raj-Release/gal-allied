package com.shaic.feedback.managerfeedback;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTable;
import com.shaic.branchmanagerfeedback.BranchManagerFeedbackTableDTO;
import com.shaic.branchmanagerfeedback.BranchMangerFeedbackDetailsTable;
import com.shaic.domain.Claim;
import com.shaic.domain.MasterService;
import com.shaic.domain.MastersValue;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.CashlessWorkFlow;
import com.shaic.domain.preauth.UserBranchMapping;
import com.shaic.feedback.managerfeedback.previousFeedback.BranchManagerPreviousFeedbackTableDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.ConvertClaimMapper;

@Stateless
public class ManagerFeedBackService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@EJB
	private MasterService masterService;

	public Boolean submitManagerFeedbackForm(ManagerFeedBackPolicyTableDto bean) {
		
		if(bean != null){
			BranchManagerFeedbackTable brFeedBack = new BranchManagerFeedbackTable();
			MastersValue feedBackValue = masterService.getMaster(bean.getFeedbackFor());
			/*UserBranchMapping userMapping = masterService.getUserBranchAndZone(bean.getUsername());
			if(null != userMapping){
				brFeedBack.setFeedbackBranchCode(userMapping.getBranchCode());
				brFeedBack.setFeedbackZoneCode(userMapping.getZoneCode());
			}*/
			brFeedBack.setFeedbackBranchCode(bean.getBranch());
			brFeedBack.setFeedbackZoneCode(bean.getZone());
			brFeedBack.setFeedbackType(feedBackValue);
			/*MastersValue feedbackRating = new MastersValue();
			feedbackRating.setKey(null != bean.getRatingfor()? bean.getRatingfor():null);
			brFeedBack.setFeedbackRating(feedbackRating);*/
			brFeedBack.setFeedbackRaisedRemarks(bean.getOverAllRmrks());
			brFeedBack.setActiveFlag(1);
			brFeedBack.setCreatedBy(bean.getUsername());
			brFeedBack.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			brFeedBack.setFeedbackRaisedDate(new Timestamp(System.currentTimeMillis()));
			brFeedBack.setFeedBackRasiedBy(bean.getUsername());
			MastersValue feedBackStatus = new MastersValue();
			feedBackStatus.setKey(ReferenceTable.FEEDBACK_REPORTED_STATUS);
			brFeedBack.setFeedbackStatus(feedBackStatus);
			
			entityManager.persist(brFeedBack);
			entityManager.flush();
			
			List<ManagerFeedBackPolicyTableDto> list = bean.getTableDtoList();
			
			for (ManagerFeedBackPolicyTableDto managerFeedBackPolicyTableDto : list) {
				BranchMangerFeedbackDetailsTable table = new BranchMangerFeedbackDetailsTable();
				if(bean.getFeedbackFor().equals(ReferenceTable.FEEDBACK_TYPE_CLAIM_RETAIL))
				table.setDocumentNo(managerFeedBackPolicyTableDto.getPolicyNumber());
				else if(bean.getFeedbackFor().equals(ReferenceTable.FEEDBACK_TYPE_MER))
					table.setDocumentNo(managerFeedBackPolicyTableDto.getProposalNO());
				else if(bean.getFeedbackFor().equals(ReferenceTable.FEEDBACK_TYPE_CLAIM_GMC))
					table.setDocumentNo(managerFeedBackPolicyTableDto.getIntimationNO());
				
				table.setDocumentRaisedRemarks(managerFeedBackPolicyTableDto.getRemarks());
				
				MastersValue childFeedbackRating = new MastersValue();
				childFeedbackRating.setKey(null != managerFeedBackPolicyTableDto.getFeedBackRating()? managerFeedBackPolicyTableDto.getFeedBackRating().getId():null);
				childFeedbackRating.setValue(null != managerFeedBackPolicyTableDto.getFeedBackRating()? managerFeedBackPolicyTableDto.getFeedBackRating().getValue():null);
				table.setFeedBackRatingId(childFeedbackRating);
				
				MastersValue childfeedBackStatus = new MastersValue();
				childfeedBackStatus.setKey(ReferenceTable.FEEDBACK_REPORTED_STATUS);
				table.setFeedbackStatus(childfeedBackStatus);
				
				table.setFeedbackCategory(null != managerFeedBackPolicyTableDto.getFbCategory() ? managerFeedBackPolicyTableDto.getFbCategory().getId() : null);
				
				table.setActiveFlag(1);
				table.setCreatedBy(bean.getUsername());
				table.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				table.setFeedbackKey(brFeedBack.getKey());
				
				entityManager.persist(table);
				entityManager.flush();
			}
			
		}
		return null;
		
	}

	public void submitBranchManagerReviewDetails(BranchManagerPreviousFeedbackTableDTO bean) {
		
		if(bean != null){
			BranchMangerFeedbackDetailsTable brFeedBack = getFeedbackDetailsByKey(bean.getFeedBackDtlsKey());
			
			if(null != brFeedBack){
				brFeedBack.setFeedBackReviewerRatingId(bean.getRatingfor());
				brFeedBack.setFeedBackReviewFlag(bean.getFeedBackReviewFlag());
				/*MastersValue feedBackStatus = new MastersValue();
				feedBackStatus.setKey(ReferenceTable.FEEDBACK_COMPLETED_STATUS);
				brFeedBack.setFeedbackStatus(feedBackStatus);*/
				brFeedBack.setModifiedBy(bean.getUsername());
				brFeedBack.setReviewReplyDate(new Timestamp(System.currentTimeMillis()));
			}
			
			
			entityManager.merge(brFeedBack);
			entityManager.flush();
			
						
		}
			
	}

	public BranchMangerFeedbackDetailsTable getFeedbackDetailsByKey(Long feedBackDtlsKey){
		 Query query = entityManager.createNamedQuery("BranchMangerFeedbackDetailsTable.findByKey");
		 query.setParameter("primaryKey", feedBackDtlsKey);
		    List<BranchMangerFeedbackDetailsTable> fb_list = (List<BranchMangerFeedbackDetailsTable>)query.getResultList();
		    if(fb_list != null && ! fb_list.isEmpty()){
		    	return fb_list.get(0);
		    }
		    return null;
	}
}
