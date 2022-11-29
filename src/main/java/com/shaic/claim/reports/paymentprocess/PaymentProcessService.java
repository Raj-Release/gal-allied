package com.shaic.claim.reports.paymentprocess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.FieldVisitRequest;
@Stateless
public class PaymentProcessService extends AbstractDAO<Intimation>{
	
	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	protected EntityManager entityManager;
	
	public PaymentProcessService() {
		super();
	}
	
	public  Page< PaymentProcessTableDTO> search( PaymentProcessFormDTO fvrFormDTO,String userName, String passWord) {
		List<FieldVisitRequest> listIntimations = new ArrayList<FieldVisitRequest>(); 
		try{
		
		Long intimationNo = null; //!= fvrFormDTO.get? fvrFormDTO.getCpuCode().getId() : null;
		Long claimNumber =null;// != fvrFormDTO.getReportType() ? fvrFormDTO.getReportType().getId() : null;
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<FieldVisitRequest> criteriaQuery = criteriaBuilder.createQuery(FieldVisitRequest.class);
		Date fromDate = fvrFormDTO.getFromDate();
		Date toDate = fvrFormDTO.getToDate();
		
		
		Root<FieldVisitRequest> root = criteriaQuery.from(FieldVisitRequest.class);
		
		List<Predicate> conditionList = new ArrayList<Predicate>();
		
		/*if(intimationNo!= null){
		Predicate condition1 = criteriaBuilder.equal(root.<Intimation>get("intimation").<TmpCPUCode>get("cpuCode").<Long>get("key"), intimationNo);
		conditionList.add(condition1);
		}*/
		
		/*if(claimNumber != null){
		Predicate condition2= criteriaBuilder.like(root.<Intimation>get("intimation").<Insured>get("insured").<String>get("healthCardNumber"), "%"+claimNumber+"%");
		//conditionList.add(condition2);
		}*/
		
		if(fromDate != null && toDate != null){
			String fromDateString = new SimpleDateFormat("dd-MM-yyyy").format(fromDate);
			String toDateString = new SimpleDateFormat("dd-MM-yyyy").format(toDate);
			Expression<Date> exp = root.<Date> get("createdDate");
			Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(exp, SHAUtils.getFromDate(fromDateString));
			Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(exp, SHAUtils.getToDate(toDateString));
			conditionList.add(condition6);
			conditionList.add(condition7);
			
		}else if(fromDate != null){
		String fromDateString = new SimpleDateFormat("dd-MM-yyyy").format(fromDate);
		Expression<Date> exp = root.<Date>get("createdDate");
		String pattern = "dd-MM-yyyy";
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    try {
	      Date date = format.parse("12-31-2100");
		String endDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
		Predicate condition6 = criteriaBuilder.greaterThanOrEqualTo(exp, SHAUtils.getFromDate(fromDateString));
		Predicate condition7 = criteriaBuilder.lessThanOrEqualTo(exp, SHAUtils.getToDate(endDate));
		conditionList.add(condition6);
		conditionList.add(condition7);
		}catch(Exception e){
	    	e.printStackTrace();
	    }
	    
	}else if(toDate != null){
		String toDateString = new SimpleDateFormat("dd-MM-yyyy").format(toDate);
		Expression<Date> exp = root.<Date> get("createdDate");
		String pattern = "dd-MM-yyyy";
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
	    try {
	      Date date = format.parse("12-31-2000");
		String startDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
		Predicate condition8 = criteriaBuilder.greaterThanOrEqualTo(exp, SHAUtils.getFromDate(startDate));
		Predicate condition9 = criteriaBuilder.lessThanOrEqualTo(exp, SHAUtils.getToDate(toDateString));

		conditionList.add(condition8);
		conditionList.add(condition9);
	    }
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	
		
			/*List<Long> claimTypeKey = new ArrayList<Long>();
			claimTypeKey.add(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY);
			claimTypeKey.add(ReferenceTable.REIMBURSEMENT_CLAIM_TYPE_KEY);
			Expression<Long> exp = root.<MastersValue> get("claimType")
					.<Long> get("key");
			Predicate condition4 = exp.in(claimTypeKey);
			conditionList.add(condition4);*/

			if (intimationNo == null && claimNumber == null) {
				criteriaQuery.select(root).where(conditionList.toArray(new Predicate[] {}));
			} 
			else {
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			final TypedQuery<FieldVisitRequest> typedQuery = entityManager.createQuery(criteriaQuery);
			int pageNumber = fvrFormDTO.getPageable().getPageNumber();
			int firtResult;
			
			if (pageNumber > 1) {
				firtResult = (pageNumber - 1) * 10;
			} 
			else {
				firtResult = 1;
			}
			
			listIntimations = typedQuery.getResultList();
			if (intimationNo == null && claimNumber == null && listIntimations.size() > 10) {
				listIntimations = typedQuery.setFirstResult(firtResult)
						.setMaxResults(10).getResultList();
			}
			// for(Intimation inti:listIntimations){
			// System.out.println(inti.getIntimationId()+"oooooooooooooooooooooooooo"+inti.getPolicy().getPolicyNumber()+"tttttttttttttt"+inti.getCreatedBy());
			// }
			//List<FieldVisitRequest> doList = listIntimations;
		//	List<PaymentProcessCpuTableDTO> tableDTO = PaymentProcessCputMapper.(doList);
			// tableDTO =
			// SearchAcknowledgementDocumentReceiverMapper.getClaimDTO(getclaimNumber(tableDTO));
			// tableDTO = getclaimNumber(tableDTO);
			//tableDTO = getHospitalDetails(tableDTO);
			List<PaymentProcessTableDTO> result = new ArrayList<PaymentProcessTableDTO>();
			//result.addAll(tableDTO);
			Page<PaymentProcessTableDTO> page = new Page<PaymentProcessTableDTO>();
			fvrFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) {
				fvrFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);

			return page;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	@Override
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}

}
