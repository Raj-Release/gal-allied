package com.shaic.claim.reports.billreceivedreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.TmpCPUCode;
@Stateless
public class BillReceivedReportService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	protected Date fromDate;
	protected Date toDate;
	
	
	public BillReceivedReportService() {
		super();
	}
	
	public  Page<BillReceivedReportTableDTO> search(BillReceivedReportFormDTO billFormDTO,String userName, String passWord) {
		
		List<Reimbursement> claimList = new ArrayList<Reimbursement>(); 
		try
		{
			 fromDate = null != billFormDTO.getFromDate() ? billFormDTO.getFromDate() : null;
			 toDate = null != billFormDTO.getToDate() ? billFormDTO.getToDate() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Reimbursement> criteriaQuery = criteriaBuilder.createQuery(Reimbursement.class);
			
			Root<Reimbursement> root = criteriaQuery.from(Reimbursement.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			if(null != fromDate)
			{
				Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition6);
			}
							
							
			if(null!= toDate)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition7);
			}
			
						
			if (null == fromDate  && null == toDate ) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<Reimbursement> typedQuery = entityManager.createQuery(criteriaQuery);
		
			int pageNumber = billFormDTO.getPageable().getPageNumber();
			
			SHAUtils.popinReportLog(entityManager, userName, "BillReceivedStatusReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			
			claimList = typedQuery.getResultList();
			
			List<BillReceivedReportTableDTO> tableDTO = new ArrayList<>();
			
			//for ( Reimbursement reimbursement : claimList) {
				
				
				if(fromDate != null && toDate == null)
				{
					toDate =new Date();
					
				}
				
				if(fromDate == null && toDate != null)
				{
					fromDate = toDate;
					toDate =new Date();
					
				}
				List<TmpCPUCode> cpuList =  getCpuList();
				tableDTO = getBillReceivedStatus(fromDate,toDate,cpuList);
			//}
		    
			//List<BillReceivedReportTableDTO> tableDTO = new ArrayList<>();
			//tableDTO = getBillReceivedStatus();
			
		
			List<BillReceivedReportTableDTO> result = new ArrayList<BillReceivedReportTableDTO>();
			result.addAll(tableDTO);
			Page<BillReceivedReportTableDTO> page = new Page<BillReceivedReportTableDTO>();
			billFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) 
			{
				billFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);
			SHAUtils.popinReportLog(entityManager, userName, "BillReceivedStatusReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);

			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "BillReceivedStatusReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		     e.printStackTrace();
		   }
		   return null;	
		}
	
	
	
	public List<BillReceivedReportTableDTO> getBillReceivedStatus( Date fromDate,Date toDate,List<TmpCPUCode> cpuList)
	{
		Query query;
		List<BillReceivedReportTableDTO> billDetailDTO = new ArrayList<BillReceivedReportTableDTO>();
		if(cpuList != null && !cpuList.isEmpty())
		{
			List<Reimbursement> claimList;
			List<BillReceivedReportTableDTO> cashlessBillDetailDTOList = new ArrayList<BillReceivedReportTableDTO>();
			List<BillReceivedReportTableDTO> reimbBillDetailDTOList = new ArrayList<BillReceivedReportTableDTO>();
			String cpuCode  = null;
			BillReceivedReportTableDTO  billsDTO;
			BillReceivedReportTableDTO billsDTO1;
		for (TmpCPUCode tmpCPUCode : cpuList) 
		{			
					   
			if(fromDate == null && toDate == null)
				{
					query = entityManager.createNamedQuery("Reimbursement.findByStatus");
					query.setParameter("fromStage", ReferenceTable.CLAIM_REQUEST_STAGE);
					query.setParameter("toStage", ReferenceTable.FINANCIAL_STAGE);
					query.setParameter("cpuCode", tmpCPUCode.getCpuCode());
				}
			else
				{
					query = entityManager.createNamedQuery("Reimbursement.findByStatusId");
					query.setParameter("fromStage", ReferenceTable.CLAIM_REQUEST_STAGE);
					query.setParameter("toStage", ReferenceTable.FINANCIAL_STAGE);
					query.setParameter("fromDate", fromDate);
					query.setParameter("endDate", toDate);
					query.setParameter("cpuCode", tmpCPUCode.getCpuCode());
		    
				}
		
		//List<BillReceivedReportTableDTO> billDetailDTO = null;
		
			claimList = (List<Reimbursement>)query.getResultList();		
			
			// BillReceivedReportTableDTO billsDTO = new BillReceivedReportTableDTO();
			if(null != claimList && !claimList.isEmpty())
				{ 

					for(Iterator billDetail = claimList.iterator(); billDetail.hasNext();)
					{ 
						Object[] objectList = (Object[]) billDetail.next();
						BillReceivedReportTableDTO billDTO = new BillReceivedReportTableDTO();
						billDTO.setReimbursementKey((Long)objectList[1]);
						billDTO.setClaimKey((Long)objectList[0]);
						billDTO.setStageKey((Long)objectList[3]);
						billDTO.setStatusKey((Long)objectList[2]);
						billDTO.setCpuCode(String.valueOf((Long)objectList[4]));
						cpuCode = billDTO.getCpuCode();
						billDTO.setTypeOfClaim((String)objectList[5]);
						
						if((SHAConstants.CASHLESS_CLAIM_TYPE).equalsIgnoreCase(billDTO.getTypeOfClaim()))
						{
							cashlessBillDetailDTOList.add(billDTO);
					 					
						}
						else
						{
							reimbBillDetailDTOList.add(billDTO);
					 					
						}				
			}
						
			
			 if(null != cashlessBillDetailDTOList && !cashlessBillDetailDTOList.isEmpty())
				{
				 billsDTO =	getCountDetails(cashlessBillDetailDTOList);
				 billsDTO.setTypeOfClaim("Cashless");
				 billsDTO.setCpuCode(cpuCode);
				 billDetailDTO.add(billsDTO);
				 		
			    }
			 if(null != reimbBillDetailDTOList && !	reimbBillDetailDTOList.isEmpty())
				{
				 billsDTO1 =getCountDetails(reimbBillDetailDTOList);
				 billsDTO1.setTypeOfClaim("Reimbursement");
				 billsDTO1.setCpuCode(cpuCode);
				 billDetailDTO.add(billsDTO1);	
				}			
			}
		}
		
	}
		return billDetailDTO;
}
		
		
		
		
	public  BillReceivedReportTableDTO getCountDetails(List<BillReceivedReportTableDTO> cashlessOrReimbursementBillDetailDTOList )
		{
			Integer iRodCount = 0;
			Long iFinancialCount = 0l;
			Long iMedicalRejectCount = 0l;
			Long iMedicalPendingCount = 0l;
			Long iFinancialPendingCount = 0l;
			Long pendingTotal = 0l;
			Long iBillPendingCount = 0l;
			Long iFinancialRejectCount = 0l;
			Long iChequeNotIssued = 0l;
			Long iCheckSend = 0l;
			Long completeTotal = 0l;
			BillReceivedReportTableDTO objreimbursement = new BillReceivedReportTableDTO();
			for (BillReceivedReportTableDTO reimbursement : cashlessOrReimbursementBillDetailDTOList) 
			{
					/*if((ReferenceTable.CREATE_ROD_STAGE_KEY).equals(reimbursement.getStageKey()) && (ReferenceTable.CREATE_ROD_STATUS_KEY).equals(reimbursement.getStatusKey()))
						{							iRodCount++;

						}*/
						iRodCount = cashlessOrReimbursementBillDetailDTOList.size();			
						
						if((ReferenceTable.FINANCIAL_STAGE).equals(reimbursement.getStageKey()))
						{
							if((ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(reimbursement.getStatusKey()))
						{
							iFinancialCount++;
						}
						else
						{
							if((ReferenceTable.FINANCIAL_REJECT_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.FINANCIAL_QUERY_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.FINANCIAL_REFER_TO_COORDINATOR_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.FINANCIAL_COORDINATOR_REPLY_RECEIVED_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.FINANCIAL_INITIATE_FIELD_REQUEST_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.FINANCIAL_INITIATE_INVESTIGATION_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.FINANCIAL_REFER_TO_BILLING).equals(reimbursement.getStatusKey()) || (ReferenceTable.FINANCIAL_REFER_TO_SPECIALIST).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.FINANCIAL_SPECIALIST_REPLY_RECEIVED_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.FINANCIAL_REFER_TO_MEDICAL_APPROVER).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_QUERY_STATUS).equals(reimbursement.getStatusKey())	|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REDRAFT_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_REJECT_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_QUERY_APPROVE_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.PROCESS_CLAIM_FINANCIAL_DRAFT_REJECT_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_REDRAFT_REJECT_STATUS).equals(reimbursement.getStatusKey())|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_DISAPPROVE_REJECT_STATUS).equals(reimbursement.getStatusKey())
									|| (ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS).equals(reimbursement.getStatusKey()))
							{
							
							iFinancialPendingCount++;
							}
						}
						}
						
						if((ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY.equals(reimbursement.getStageKey())))
						{
							if((ReferenceTable.PROCESS_CLAIM_FINANCIAL_APPROVE_REJECT_STATUS).equals(reimbursement.getStatusKey()))
							{
								iFinancialRejectCount++;
							}
								
			
						}
						if((ReferenceTable.BILLING_STAGE).equals(reimbursement.getStageKey()) && (ReferenceTable.BILLING_SEND_TO_FINANCIAL_APPROVER).equals(reimbursement.getStatusKey()))
						{
							iBillPendingCount++;
						}
						
						
						
						if((ReferenceTable.ZONAL_REVIEW_STAGE).equals(reimbursement.getStageKey()))
						{
								if((ReferenceTable.ZONAL_REVIEW_QUERY_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.ZONAL_REVIEW_REJECTION_STATUS).equals(reimbursement.getStatusKey())
										|| (ReferenceTable.ZONAL_REVIEW_REFER_TO_COORDINATOR_STATUS).equals(reimbursement.getStatusKey()) || (ReferenceTable.ZONAL_REVIEW_COORDINATOR_REPLY_STATUS).equals(reimbursement.getStatusKey()))
								{
									iMedicalPendingCount++;
						      }
						}
						
						if((ReferenceTable.CLAIM_REQUEST_STAGE).equals(reimbursement.getStageKey()) && (ReferenceTable.PROCESS_CLAIM_REQUEST_APPROVE_REJECT_STATUS).equals(reimbursement.getStatusKey()))
						{
							iMedicalRejectCount++;
						}
						
						if((ReferenceTable.FINANCIAL_STAGE).equals(reimbursement.getStageKey()))
							{
							if((ReferenceTable.FINANCIAL_APPROVE_STATUS).equals(reimbursement.getStatusKey()) && !(ReferenceTable.FINANCIAL_SETTLED).equals(reimbursement.getStatusKey()))
						{
							iChequeNotIssued++;
						}
								}
						
						if((ReferenceTable.FINANCIAL_STAGE).equals(reimbursement.getStageKey()) && (ReferenceTable.FINANCIAL_SETTLED).equals(reimbursement.getStatusKey()))
						{
							iCheckSend++;
						}
						
						pendingTotal = iMedicalPendingCount+iFinancialPendingCount+iBillPendingCount+iChequeNotIssued;
						completeTotal = iFinancialRejectCount+	iCheckSend+ iMedicalRejectCount;
		} 
			
			objreimbursement.setTotalBillReceived(iRodCount);
			objreimbursement.setCompleteTotal(completeTotal);
			objreimbursement.setBillingPending(iBillPendingCount);
			objreimbursement.setMedicallyRejected(iMedicalRejectCount);
			objreimbursement.setMedicallyPending(iMedicalPendingCount);
			objreimbursement.setFinanciallyPending(iFinancialPendingCount);
			objreimbursement.setFinanciallyReject(iFinancialRejectCount);
			objreimbursement.setPending(pendingTotal);
			objreimbursement.setClose(0l);
			objreimbursement.setClaimOpen(0l);
			objreimbursement.setChequeNotIssued(iChequeNotIssued);
			objreimbursement.setCheckSend(iCheckSend);
			return objreimbursement;
						
		}
	  
	public List<TmpCPUCode> getCpuList()
	{
		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
				.getResultList();
		
		return resultCPUCodeList;
	}
	

	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}
	

	
	

}
