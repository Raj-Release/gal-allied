package com.shaic.restservices.bancs;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.CatastropheData;
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.PedQueryTable;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.Stage;
import com.shaic.restservices.bancs.sendpedquery.SendPEDQueryComplexTypeDetails;
import com.shaic.restservices.bancs.sendpedquery.SendPEDQueryDetails;

@Stateless
public class GLXWBService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final String DEFAULT_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	
		@SuppressWarnings("unchecked")
	public EndorsementNotificationTable EndorsementNotificationData(EndorsementNotificationRequest detailObj , Product endNotifyData) throws ParseException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		EndorsementNotificationTable endData = new EndorsementNotificationTable();
		endData.setPolicySysId(Long.parseLong(detailObj.getPolicyId()));
		endData.setPolicyNumber(detailObj.getPolicyNumber());
		endData.setEndorsementIndex(detailObj.getPolicyEndorsementNumber());
		endData.setRiskId(detailObj.getInsuredId());
		endData.setProductCode(endNotifyData.getCode());
		endData.setGiCreatedOn(convertToDate(detailObj.getEndDate(), "dd-MM-yyyy"));
		entityManager.persist(endData);
		entityManager.flush();
		return endData;
	}
		@SuppressWarnings("unchecked")
		public CatastropheData CatastropheDatas(CatastropheDataRequest catObj) throws ParseException {
			CatastropheData catData = new CatastropheData();
			catData.setCatastropheCode(catObj.getCatReferenceNo());
			catData.setCatastropheDesc(catObj.getCatDescription());
			catData.setActiveStatus("Y");
			catData.setCreatedBy("GALAXY");
			catData.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			catData.setStartDate(convertToDate(catObj.getStartDate(), "dd-MM-yyyy"));
			catData.setEndDate(convertToDate(catObj.getEndDate(), "dd-MM-yyyy"));
			entityManager.persist(catData);
			entityManager.flush();
			return catData;
		}
		
		@SuppressWarnings("unchecked")
		public ClaimReverseFeedTable ClaimReverseFeedData(ClaimReverseFeedData prfObj,String claimUprId) throws ParseException {
			ClaimReverseFeedTable prfData = new ClaimReverseFeedTable();
			prfData.setPrfFieldIndicator(prfObj.getPrfFieldIndicator());
			prfData.setPrfBankAccountNumber(prfObj.getPrfBankAccountNumber());
			if(prfObj.getPrfTotalAmountPaid() != null)
			{
			prfData.setPrfTotalAmountPaid(Double.parseDouble(prfObj.getPrfTotalAmountPaid()));
			}
			if(prfObj.getPrfVoucherDate() != null)
			{
			prfData.setPrfVoucherDate(convertToDate(prfObj.getPrfVoucherDate(), "dd-MM-yyyy"));
			}
			if(prfObj.getPrfChequeDate() != null)
			{
			prfData.setPrfChequeDate(convertToDate(prfObj.getPrfChequeDate(), "dd-MM-yyyy"));
			}
			prfData.setPrfChequeNumber(prfObj.getPrfChequeNumber());
			prfData.setPrfPaymentMethod(prfObj.getPrfPaymentMethod());
			prfData.setPrfNarration(prfObj.getPrfNarration());
			prfData.setPrfPaymentVoucherNumber(prfObj.getPrfPaymentVoucherNumber());
			prfData.setPrfInvoiceNumber(prfObj.getPrfInvoiceNumber());
			if(prfObj.getPrfInvoicePaidAmount() != null)
			{
			prfData.setPrfInvoicePaidAmount(Double.parseDouble(prfObj.getPrfInvoicePaidAmount()));
			}
			prfData.setCheckId(Double.parseDouble(prfObj.getCheckId()));
			if(prfObj.getLastUpdateDate() != null)
			{
			prfData.setLastUpdateDate(convertToDate(prfObj.getLastUpdateDate(), "dd-MM-yyyy"));
			}
			prfData.setEntrySource(prfObj.getEntrySource());
			prfData.setActiveStatus(1l);
			prfData.setCreatedBy("GALAXY");
			prfData.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			prfData.setClaimUprId(claimUprId);
			prfData.setBankRemarks(prfObj.getBankRemarks());
			entityManager.persist(prfData);
			entityManager.flush();
			return prfData;
		}
		
		@SuppressWarnings("unchecked")
		public Boolean getCatastropheByRefernceNo(String catObj){
			Boolean isValue= false;
			Query query = entityManager.createNamedQuery("CatastropheData.findByCatastropheCode");
			query = query.setParameter("catastropheCode", catObj);
			List<CatastropheData> catastropheData = query.getResultList();
			if(null != catastropheData && !catastropheData.isEmpty()){
				return true;
			}
			else{
				return false;
			}
		}
		
		public Product getProductByProductCode(String productCode)
		{
			Query query = entityManager
					.createNamedQuery("Product.findBySourceCode");
			query = query.setParameter("productCode", productCode);
			List<Product> resultList = query.getResultList();
			Product product = null;
			if(null != resultList && !resultList.isEmpty())
			{
					product = resultList.get(0);
			}
			return product;
		}

		public Date convertToDate(String dateValueInString, String dateFormatToBeConverted) throws ParseException{
			SimpleDateFormat  sdf = null;
			SimpleDateFormat  ssdf = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println(dateValueInString);
			Date sDate = ssdf.parse(dateValueInString);
			String dDate = null;
			if(!StringUtils.isBlank(dateValueInString)){
				 sdf = new SimpleDateFormat(dateFormatToBeConverted);
				 dDate = sdf.format(sDate);
			}
			Date finalDate =  sdf.parse(dDate);
			return finalDate;
		}
		
		public PedQueryTable updateSendPEDQueryService(SendPEDQueryDetails pedObject) throws ParseException{
			
			PedQueryTable pedQuery = new PedQueryTable();
			pedQuery.setServiceTransId("0");
			pedQuery.setPolicyNumber(pedObject.getPolicyNumber());
			pedQuery.setMemberCode(pedObject.getMemberCode());
			pedQuery.setClaimNumber(pedObject.getClaimNumber());
			pedQuery.setWorkItemId(Long.parseLong(pedObject.getWorkItemID()));
			pedQuery.setActiveStatus(1l);
			pedQuery.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			pedQuery.setCreatedBy("SYSTEM");
			pedQuery.setStatus(getStatus(ReferenceTable.PED_QUERY_STATUS_KEY));
			pedQuery.setStage(getStage(ReferenceTable.PED_QUERY_STAGE_KEY));
			entityManager.persist(pedQuery);
			entityManager.flush();
			return pedQuery;
		}
		
		public PedQueryDetailsTableData updateSendPEDQueryDetailService(PedQueryTable uploadStatus,SendPEDQueryDetails pedObject) throws Exception {
			List<SendPEDQueryComplexTypeDetails> pedQueries = pedObject.getQueries();
			PedQueryDetailsTableData pedQueryDetails = null;
			if(pedObject.getQueries() != null && ! pedObject.getQueries().isEmpty()){
				for (SendPEDQueryComplexTypeDetails pedDetailsObj : pedQueries) {
					pedQueryDetails = new PedQueryDetailsTableData();
					pedQueryDetails.setPedQuery(uploadStatus);
					pedQueryDetails.setQueryId(Double.parseDouble(pedDetailsObj.getQueryId()));
					pedQueryDetails.setQueryType(pedDetailsObj.getQueryType());
					pedQueryDetails.setQueryDesc(pedDetailsObj.getQueryDescription());
					pedQueryDetails.setQueryCode(pedDetailsObj.getQueryCode());
					pedQueryDetails.setQueryRemarks(pedDetailsObj.getQueryDetails());
					pedQueryDetails.setRaisedByUser(pedDetailsObj.getRaisedByUser());
					pedQueryDetails.setRaisedByRole(pedDetailsObj.getRaisedByRole());
					pedQueryDetails.setRaisedDate(convertToDateTime(pedDetailsObj.getRaisedDate(), DEFAULT_DATE_TIME_FORMAT));
					pedQueryDetails.setCreatedDate(new Timestamp(System.currentTimeMillis()));
					pedQueryDetails.setCreatedBy("SYSTEM");
					pedQueryDetails.setStatus(getStatus(ReferenceTable.PED_QUERY_STATUS_KEY));
					pedQueryDetails.setStage(getStage(ReferenceTable.PED_QUERY_STAGE_KEY));
					entityManager.persist(pedQueryDetails);
					entityManager.flush();
				}
			}
			return pedQueryDetails;
		}
		
		public Status getStatus(Long key){
			Query query = entityManager.createNamedQuery("Status.findByKey");
			query = query.setParameter("statusKey", key);
			List<Status> statusList = query.getResultList();
			if(null != statusList && !statusList.isEmpty()) {
				return statusList.get(0);
			}
			return null;
		}
		
		public Stage getStage(Long stageKey) {
			Query query = entityManager.createNamedQuery("Stage.findByKey");
			query = query.setParameter("stageKey", stageKey);
			List<Stage> stageList = query.getResultList();
			if(null != stageList && !stageList.isEmpty()) {
				return stageList.get(0);
			}
				
			return null;
		}
		
		public Date convertToDateTime(String dateValueInString, String dateFormatToBeConverted) throws ParseException{
			SimpleDateFormat  sdf = null;
			SimpleDateFormat  ssdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			System.out.println(dateValueInString);
			Date sDate = ssdf.parse(dateValueInString);
			String dDate = null;
			if(!StringUtils.isBlank(dateValueInString)){
				 sdf = new SimpleDateFormat(dateFormatToBeConverted);
				 dDate = sdf.format(sDate);
			}
			Date finalDate =  sdf.parse(dDate);
			return finalDate;
		}
}

