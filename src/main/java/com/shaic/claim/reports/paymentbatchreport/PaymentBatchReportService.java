

package com.shaic.claim.reports.paymentbatchreport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.shaic.domain.BankMaster;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.DocAcknowledgement;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;

@Stateless
public class PaymentBatchReportService extends AbstractDAO<ClaimPayment> {

	@PersistenceContext
	protected EntityManager entityManager;
	
	public PaymentBatchReportService() {
		super();
	}
	
	public  Page<PaymentBatchReportTableDTO> search(PaymentBatchReportFormDTO paymentFormDTO,String userName, String passWord) {
		
		

		List<ClaimPayment> listIntimations = new ArrayList<ClaimPayment>(); 
		try
		{
			Date fromDate = null != paymentFormDTO.getFromDate() ? paymentFormDTO.getFromDate() : null;
			Date toDate = null != paymentFormDTO.getToDate() ? paymentFormDTO.getToDate() : null;
			String lotNoFrom = null != paymentFormDTO.getLotNoFrom() ? paymentFormDTO.getLotNoFrom() : null;
			String lotNoTo = null != paymentFormDTO.getLotNoTo() ? paymentFormDTO.getLotNoTo() : null;
			
			
			final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<ClaimPayment> criteriaQuery = criteriaBuilder.createQuery(ClaimPayment.class);
			
			Root<ClaimPayment> root = criteriaQuery.from(ClaimPayment.class);
			
			List<Predicate> conditionList = new ArrayList<Predicate>();						
			
			Integer fromLotNo = null;
			Integer toLotNo = null;
			
			if(null != lotNoFrom && !lotNoFrom.equals("")){
				
				if(lotNoFrom.contains("LOT")){
					fromLotNo = Integer.parseInt(lotNoFrom.substring(4,11));
				}else{
					fromLotNo = Integer.parseInt(lotNoFrom);
				}
				
				
			}
			
			if(null != lotNoTo && !lotNoTo.equals("")){
				
				if(lotNoTo.contains("LOT")){
					toLotNo = Integer.parseInt(lotNoTo.substring(4,11));
				}else{
					toLotNo = Integer.parseInt(lotNoTo);
				}
				
				
			}
			
//			if((null != lotNoFrom || !lotNoFrom.equals("")) && (null != lotNoTo || lotNoTo.equals("")) && (lotNoFrom.contains("LOT") && lotNoTo.contains("LOT")))
//			{
//				
//				fromLotNo = Integer.parseInt(lotNoFrom.substring(4,11));
//				toLotNo = Integer.parseInt(lotNoTo.substring(4,11));
//				
//			}
//			else
//			{
////				fromLotNo = Integer.parseInt(lotNoFrom);
////				toLotNo = Integer.parseInt(lotNoTo);
//				
//			}
			 
			if(null != fromDate && null != toDate)
			{
				fromDate.setHours(0);
				Predicate condition3 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("batchCreatedDate"), fromDate);
				conditionList.add(condition3);	
//				Calendar c = Calendar.getInstance();
//				c.setTime(toDate);
//				c.add(Calendar.DATE, 1);
//				toDate = c.getTime();
				toDate.setHours(23);
				
				Predicate condition4 = criteriaBuilder.lessThanOrEqualTo(root.<Date>get("batchCreatedDate"), toDate);
				conditionList.add(condition4);
			}
			
			Predicate condition4 = criteriaBuilder.isNotNull(root.<String>get("batchNumber"));
			conditionList.add(condition4);

			if (fromDate == null && toDate == null && lotNoFrom == null && lotNoTo == null) 
			{
				criteriaQuery.select(root);
				
			} 
			else 
			{
				criteriaQuery.select(root).where(criteriaBuilder.and(conditionList.toArray(new Predicate[] {})));
			}
			
			final TypedQuery<ClaimPayment> typedQuery = entityManager.createQuery(criteriaQuery);
			
			SHAUtils.popinReportLog(entityManager, userName, "PaymentBatchReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			
			listIntimations = typedQuery.getResultList();
			
		    List<ClaimPayment> doList = listIntimations;
 
			List<PaymentBatchReportTableDTO> tableDTO = PaymentBatchReportMapper.getInstance().getListOfPaymentBatchTableDTO(doList);
			Product productByProductCode;
			Reimbursement reimbursementObject;
			DocAcknowledgement docAcknowLedgement;
			for (PaymentBatchReportTableDTO paymentBatchReportTableDTO : tableDTO) {
				if(paymentBatchReportTableDTO.getProduct() != null){
					productByProductCode = getProductByProductCode(paymentBatchReportTableDTO.getProduct());
					if(productByProductCode != null){
						paymentBatchReportTableDTO.setProductNameValue(productByProductCode.getValue());
					}
				}
				
				if(paymentBatchReportTableDTO.getChequeDate() != null){
					paymentBatchReportTableDTO.setStrChequeDate(SHAUtils.formatDate(paymentBatchReportTableDTO.getChequeDate()));
				}
				
				if(paymentBatchReportTableDTO.getRodNumber() != null){
					reimbursementObject = getReimbursementObject(paymentBatchReportTableDTO.getRodNumber());
					if(reimbursementObject != null){
						docAcknowLedgement = reimbursementObject.getDocAcknowLedgement();
						if(! (reimbursementObject.getClaim().getClaimType().getKey().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)
								&& docAcknowLedgement.getDocumentReceivedFromId() != null && docAcknowLedgement.getDocumentReceivedFromId().getKey().equals(ReferenceTable.RECEIVED_FROM_HOSPITAL))){
							paymentBatchReportTableDTO.setProviderCode("");
							
						}
					}
				}
			}

			tableDTO = getHospitalDetails(tableDTO);
			
			Double sum = 0d;
			for (PaymentBatchReportTableDTO paymentBatchReportTableDTO : tableDTO) {
				
				paymentBatchReportTableDTO.setServiceTax(0d);
				sum = 0d;
				if(null != paymentBatchReportTableDTO.getApprovedAmt())
				{
					sum = paymentBatchReportTableDTO.getApprovedAmt()+paymentBatchReportTableDTO.getServiceTax();
				}
				paymentBatchReportTableDTO.setSumOfApprovedAndServiceTax(sum);
			}
			List<PaymentBatchReportTableDTO> finalList = new ArrayList<PaymentBatchReportTableDTO>();
			
			if(null != fromLotNo  && null != toLotNo )
			{
				for (PaymentBatchReportTableDTO paymentBatchReportTableDTO : tableDTO) {
					
					if(null != paymentBatchReportTableDTO.getLotNo())
					{
					if(Integer.parseInt(paymentBatchReportTableDTO.getLotNo().substring(4,11)) >= fromLotNo && Integer.parseInt(paymentBatchReportTableDTO.getLotNo().substring(4,11))<= toLotNo)
					{
						finalList.add(paymentBatchReportTableDTO);
					}
					}
				}
			}
			else if(null != fromLotNo)
			{
				for (PaymentBatchReportTableDTO paymentBatchReportTableDTO : tableDTO) {
					
					if(null != paymentBatchReportTableDTO.getLotNo())
					{
					if(Integer.parseInt(paymentBatchReportTableDTO.getLotNo().substring(4,11)) >= fromLotNo)
					{
						finalList.add(paymentBatchReportTableDTO);
					}
					}
				}
			}
			else if(null != toLotNo )
			{
				for (PaymentBatchReportTableDTO paymentBatchReportTableDTO : tableDTO) {
					
					if(null != paymentBatchReportTableDTO.getLotNo())
					{
					if(Integer.parseInt(paymentBatchReportTableDTO.getLotNo().substring(4,11))<= toLotNo)
					{
						finalList.add(paymentBatchReportTableDTO);
					}
					}
				}
			}else{
				finalList.addAll(tableDTO);
			}
			
			
			List<PaymentBatchReportTableDTO> result = new ArrayList<PaymentBatchReportTableDTO>();
//			if(null != finalList && !finalList.isEmpty())
//			{
//				result.addAll(finalList);
//			}
//			else
//			{
//			result.addAll(tableDTO);
//			}
			
			result.addAll(finalList);
			Page<PaymentBatchReportTableDTO> page = new Page<PaymentBatchReportTableDTO>();
			
			page.setPageItems(result);
			page.setIsDbSearch(true);

			SHAUtils.popinReportLog(entityManager, userName, "PaymentBatchReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;
		  }
			catch (Exception e) 
		   {
				SHAUtils.popinReportLog(entityManager, userName, "PaymentBatchReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
		     e.printStackTrace();
		   }
		   return null;	
		}
	
	private List<PaymentBatchReportTableDTO> getHospitalDetails(List<PaymentBatchReportTableDTO> tableDTO)
	{
		BankMaster cityName;
		OrganaizationUnit mailId;
		Calendar faDate;
		Date faApproveDate;
		for(int index = 0; index < tableDTO.size(); index++){
			/*Hospitals hospitalDetail = getHospitalDetail(tableDTO.get(index).getHospitalId());
			if (null != hospitalDetail) {
				
				tableDTO.get(index).setHospitalName(hospitalDetail.getName());
			}*/
	
			
			cityName = getCityName(tableDTO.get(index).getIfscCode());
			if(null != cityName)
			
			{
				//tableDTO.get(index).setCity(cityName.getCity());
				tableDTO.get(index).setPayableAt(cityName.getCity());
				tableDTO.get(index).setBankName(cityName.getBankName());				

			}
			
			mailId = getZonalEmailDetails(tableDTO.get(index).getPioCode());
			if(null != mailId){
				tableDTO.get(index).setZonalMailId(mailId.getEmailId());
			}
			
			  faDate = Calendar.getInstance();
		      faDate.setTime(tableDTO.get(index).getFaApprovedDate());
		      faDate.add(Calendar.DATE, 1);
		      faApproveDate = faDate.getTime();
		      
		      if(null != faApproveDate)
		      {
		    	  tableDTO.get(index).setNextDayOfFaApprovedDate(faApproveDate);
		      }
			
		}
		
		
		return tableDTO;
			
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public Product getProductByProductCode(String productCode) {
		Product product = new Product();
		if (productCode != null) {
			Query findAll = entityManager
					.createNamedQuery("Product.findByCode").setParameter(
							"productCode", productCode);
			List<Product> productList = findAll.getResultList();
			for (Product mastersValue : productList) {
				product = mastersValue;
			}
		}
		return product;
	}
	
	/*private Hospitals getHospitalDetail(Long hospitalId){

		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hospitalId);
		Hospitals hospitalDetail;
		try{
			
		hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
		return hospitalDetail;
		}
		catch(Exception e){
			return null;
		}
	}
	
	  private FieldVisitRequest getFvrDetails(Long intimationKey)
		        {
					
				Query findByIntimationKey = entityManager.createNamedQuery(
						"FieldVisitRequest.findByIntimationKey").setParameter("intimationKey", intimationKey);
				try{
					List<FieldVisitRequest> fvrDetail = (List<FieldVisitRequest>) findByIntimationKey.getResultList();
				
					if(!fvrDetail.isEmpty())
					return fvrDetail.get(fvrDetail.size()-1);
				
				}catch(Exception e)
				{

					
				}
			
				return null;
			
		}*/
		
	  
	  private BankMaster getCityName(String ifscCode)
		{
			Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
			query = query.setParameter("ifscCode", ifscCode);
			List<BankMaster> bankList = query.getResultList();
			if(null != bankList && !bankList.isEmpty())
			{
				return bankList.get(0);
			}
			else
			{
				return null;
			}
			
		}
		
		private OrganaizationUnit getZonalEmailDetails(String pioCode)
		{
			Query query = entityManager.createNamedQuery("OrganaizationUnit.findByUnitId");
			query = query.setParameter("officeCode", pioCode);
			List<OrganaizationUnit> branchList = query.getResultList();
			if(null != branchList && !branchList.isEmpty())
			{
				return branchList.get(0);
			}
			else
			{
				return null;
			}
			
		}
		
		private Reimbursement getReimbursementObject(String rodNo) {
			Query query = entityManager
					.createNamedQuery("Reimbursement.findRodByNumber");
			query = query.setParameter("rodNumber", rodNo);
			List<Reimbursement> reimbursementObjectList = query.getResultList();
			if (null != reimbursementObjectList
					&& !reimbursementObjectList.isEmpty()) {
				entityManager.refresh(reimbursementObjectList.get(0));
				return reimbursementObjectList.get(0);
			}
			return null;
		}


	@Override
	public Class<ClaimPayment> getDTOClass() {
		// TODO Auto-generated method stub
		return ClaimPayment.class;
	}
}
