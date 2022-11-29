package com.shaic.domain.bancs;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.starfax.simulation.PremiaPullService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BaNCSPullService {

private final Logger log = LoggerFactory.getLogger(PremiaPullService.class);
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;
	
	public List<BaNCSIntimationTable> processBaNCSIntimationData(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR BANCS PULL ******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<BaNCSIntimationTable> bancsIntimationList = fetchRecFromBaNCSIntimationTable(batchSize);
			return bancsIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM BANCS TABLE **********************" + e.getMessage());
			return null;
		}
	}
	
	private List<BaNCSIntimationTable> fetchRecFromBaNCSIntimationTable(String batchSize) {
		Query query = entityManager.createNamedQuery("BaNCSIntimationTable.findAll");
		query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
		/**
		 * Will remove once the final integration of batch is over.
		 * */
	//	query = query.setParameter("claimType", "P");

		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<BaNCSIntimationTable> bancsIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR BANCS PULL FOR NON GMC NETWORK ******------> " + (bancsIntimationTableList != null ? String.valueOf(bancsIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return bancsIntimationTableList;
	}
	
	public List<BaNCSIntimationTable> processBaNCSIntimationDataForNonNetwork(String batchSize) {
		try {
			log.info("********* BATCH SIZE FOR BANCS PULL NON-NETWORK******------> " + batchSize != null ? String.valueOf(batchSize) : "NULL");
			List<BaNCSIntimationTable> bancsIntimationList = fetchRecFromBaNCSIntimationTableForNonNetwork(batchSize);
			return bancsIntimationList;
		} catch(Exception e) {
			log.error("********* EXCEPTION OCCURED DURING FETCH FROM PREMIA TABLE **********************" + e.getMessage());
			return null;
		}
	}
	
	private List<BaNCSIntimationTable> fetchRecFromBaNCSIntimationTableForNonNetwork(String batchSize) {
		Query query = entityManager.createNamedQuery("BaNCSIntimationTable.findByNonNetwork");
		query = query.setParameter("savedType", SHAConstants.PREMIA_INTIMATION_STG_PROCESSED_STATUS);
		/**
		 * Will remove once the final integration of batch is over.
		 * */
	//	query = query.setParameter("claimType", "P");

		if(batchSize != null) {
			query.setMaxResults(SHAUtils.getIntegerFromString("20"));
		}
		List<BaNCSIntimationTable> bancsIntimationTableList = query.getResultList();
		log.info("********* COUNT FOR PREMIA PULL FOR NON NETWORK******------> " + (bancsIntimationTableList != null ? String.valueOf(bancsIntimationTableList.size()) : "NO RECORDS TO PULL"));
		return bancsIntimationTableList;
	}
}
