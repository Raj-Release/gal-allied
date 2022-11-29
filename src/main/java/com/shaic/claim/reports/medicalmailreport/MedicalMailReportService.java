package com.shaic.claim.reports.medicalmailreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import com.shaic.domain.ReimbursementQuery;
import com.shaic.domain.preauth.PreauthQuery;

@Stateless
public class MedicalMailReportService extends AbstractDAO<Intimation>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public MedicalMailReportService() {
		super();
	}
	
	public  Page<MedicalMailReportTableDTO> search(MedicalMailReportFormDTO medicalFormDTO,String userName, String passWord) {
		
		

		List<PreauthQuery> preauthQueryList = new ArrayList<PreauthQuery>(); 
		List<ReimbursementQuery> reimbursementQueryList = new ArrayList<ReimbursementQuery>();
		
		try
		{
			Date fromDate = null != medicalFormDTO.getFromDate() ? medicalFormDTO.getFromDate() : null;
			Date toDate = null != medicalFormDTO.getToDate() ? medicalFormDTO.getToDate() : null;
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();
			final CriteriaQuery<PreauthQuery> criteriaQuery = criteriaBuilder.createQuery(PreauthQuery.class);
			final CriteriaQuery<ReimbursementQuery> criteriaQuery1 = criteriaBuilder1.createQuery(ReimbursementQuery.class);
			
			Root<PreauthQuery> root = criteriaQuery.from(PreauthQuery.class);
			Root<ReimbursementQuery> root1 = criteriaQuery1.from(ReimbursementQuery.class);
			 
			List<Predicate> conditionList = new ArrayList<Predicate>();	
			
			if(null != fromDate && null != toDate)
			{
				Predicate condition1 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createdDate"), fromDate);
				conditionList.add(condition1);	
				Calendar c = Calendar.getInstance();
				c.setTime(toDate);
				c.add(Calendar.DATE, 1);
				toDate = c.getTime();
				Predicate condition2 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createdDate"), toDate);
				conditionList.add(condition2);
			}
			
			
			
			
			if (fromDate == null && toDate == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
				criteriaQuery1.select(root1).where(criteriaBuilder1.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<PreauthQuery> typedQuery = entityManager.createQuery(criteriaQuery);
			final TypedQuery<ReimbursementQuery> typedQuery1 = entityManager.createQuery(criteriaQuery1);
			
			int pageNumber = medicalFormDTO.getPageable().getPageNumber();
			/*int firtResult;
			if (pageNumber > 1) 
			{
				firtResult = (pageNumber - 1) * 10;
			} 
			else 
			{
				firtResult = 1;
			}*/
			
			SHAUtils.popinReportLog(entityManager, userName, "MedicalMailStatusReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			preauthQueryList = typedQuery.getResultList();
			reimbursementQueryList = typedQuery1.getResultList();
			
			
		    List<PreauthQuery> doList = preauthQueryList;
		    List<ReimbursementQuery> doList1 = reimbursementQueryList;		    

			List<MedicalMailReportTableDTO> tableDTO = MedicalMailReportMapper.getInstance().getMedicalMailTableObjects(doList);
			List<MedicalMailReportTableDTO> tableDTO1 = MedicalMailReportMapper.getInstance().getMedicalMailTableObjects1(doList1);
			List<MedicalMailReportTableDTO> queryList = new ArrayList();
			queryList.addAll(tableDTO);
			queryList.addAll(tableDTO1);
				
			List<MedicalMailReportTableDTO> result = new ArrayList<MedicalMailReportTableDTO>();
			result.addAll(queryList);
			Page<MedicalMailReportTableDTO> page = new Page<MedicalMailReportTableDTO>();
			medicalFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (result.isEmpty()) {
				medicalFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(result);
			page.setIsDbSearch(true);

			SHAUtils.popinReportLog(entityManager, userName, "MedicalMailStatusReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "MedicalMailStatusReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
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
