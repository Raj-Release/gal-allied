package com.shaic.claim.omppaymentletterbulk;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.OMPDocumentDetails;
import com.vaadin.server.VaadinSession;

	
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SearchPrintPaymentBulkService extends AbstractDAO<OMPClaim>{

		@PersistenceContext
		protected EntityManager entityManager;
		
		@Resource
		private UserTransaction utx;
		
		private final Logger log = LoggerFactory.getLogger(SearchPrintPaymentBulkService.class);

		@Override
		public Class<OMPClaim> getDTOClass() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		public List<PrintBulkPaymentResultDto> search(SearchPrintPaymentBulkFormDTO searchFormDTO) {
			
			try {
				
				VaadinSession.getCurrent().getSession().setMaxInactiveInterval(370000);
				utx.setTransactionTimeout(360000);
				utx.begin();
				String option = "";
				
				Date fromDate = searchFormDTO.getFromDate();
				Date toDate = searchFormDTO.getToDate();		
				
				List<PrintBulkPaymentResultDto> resultList =  new ArrayList<PrintBulkPaymentResultDto>();

	
				final CriteriaBuilder builder = entityManager
						.getCriteriaBuilder();
				final CriteriaQuery<OMPDocumentDetails> ompDocumentQuery = builder
						.createQuery(OMPDocumentDetails.class);

				Root<OMPDocumentDetails> ompDocumentRoot = ompDocumentQuery.from(OMPDocumentDetails.class);
				List<Predicate> predicates = new ArrayList<Predicate>();
			//	List<PrintBulkPaymentResultDto> resultList =  new ArrayList<PrintBulkPaymentResultDto>();
			
				if(fromDate != null && toDate != null){
					Expression<Date> fromDateExpression = ompDocumentRoot
							.<Date> get("createdDate");
					Predicate fromDatePredicate = builder
							.greaterThanOrEqualTo(fromDateExpression,
									fromDate);
					predicates.add(fromDatePredicate);

					Expression<Date> toDateExpression = ompDocumentRoot
							.<Date> get("createdDate");
					Calendar c = Calendar.getInstance();
					c.setTime(toDate);
					c.add(Calendar.DATE, 1);
					toDate = c.getTime();
					Predicate toDatePredicate = builder
							.lessThanOrEqualTo(toDateExpression, toDate);
					predicates.add(toDatePredicate);
				}
				
			Predicate cpuPredicate = builder.equal(ompDocumentRoot.<String>get("documentType"), SHAConstants.OMP_PAYMENT_LETTER);
						predicates.add(cpuPredicate);	
				

				ompDocumentQuery.select(ompDocumentRoot).where(
							builder.and(predicates
									.toArray(new Predicate[] {})));

					final TypedQuery<OMPDocumentDetails> ompPaymentQuery = entityManager
							.createQuery(ompDocumentQuery);
					
					List<OMPDocumentDetails> ompDocumentDetailsList = ompPaymentQuery.getResultList();

					
					//List<OMPDocumentDetails> ompDocumentDetailsList = getOMPDocumentDetailsList(fromDate,toDate,SHAConstants.OMP_PAYMENT_LETTER);
				
					
				if(ompDocumentDetailsList != null && !ompDocumentDetailsList.isEmpty()){
					
					PrintBulkPaymentResultDto resultDto = new PrintBulkPaymentResultDto();
					resultDto.setSno(1);
					resultDto.setTotalNoofRecords(ompDocumentDetailsList.size());
					for(OMPDocumentDetails remDetails : ompDocumentDetailsList){						
						resultDto.getDocTokenList().add(remDetails.getDocumentToken());
					}		
					resultList.add(resultDto);					
				}
//			}	
		
				utx.commit();
				return resultList;
			} catch (Exception e) {
				try {
					utx.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				log.error(e.toString());
				e.printStackTrace();
				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhh" + e.getMessage()
						+ "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			}
			return null;
	
		}


		private List<OMPDocumentDetails> getOMPDocumentDetailsList(Date fromDate, Date toDate,
				String documentType) {
			Query  query = entityManager.createNamedQuery("OMPDocumentDetails.findByDocType");
			query = query.setParameter("fromDate", fromDate);
			query = query.setParameter("toDate", toDate);
			query = query.setParameter("documentType", documentType);

			List<OMPDocumentDetails> listOfObj = query.getResultList();
			
			if(listOfObj != null && !listOfObj.isEmpty()){
				return listOfObj;
			}
			return null;
		}	

		private String getDocumentURL(Date fromDate, Date toDate,String documentType) {
			Query  query = entityManager.createNamedQuery("OMPDocumentDetails.findByDocType");
			query = query.setParameter("fromDate", fromDate);
			query = query.setParameter("toDate", toDate);
			query = query.setParameter("documentType", documentType);

			List<OMPDocumentDetails> docList = query.getResultList();
			
			String fileUrl = "";
			if(docList != null && !docList.isEmpty()){
				fileUrl = docList.get(0).getSfFileName(); 
			}

			return fileUrl;
		}	
		
		public String getDocumentURLByToken(Long docToken){

			String fileUrl = "";

			Query query = entityManager
					.createNamedQuery("OMPDocumentDetails.findByDocToken");
			query = query.setParameter("documentToken", docToken);
			List<OMPDocumentDetails> docList = query.getResultList();

			if(docList != null && !docList.isEmpty()){
				fileUrl = docList.get(0).getFileName(); 
			}

			return fileUrl;

		}
}
