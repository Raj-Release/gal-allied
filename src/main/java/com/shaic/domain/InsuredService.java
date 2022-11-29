package com.shaic.domain;

import java.util.ArrayList;
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

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.domain.preauth.GmcMainMemberList;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class InsuredService {

	@PersistenceContext
	protected EntityManager entityManager;

	public BeanItemContainer<Insured> search(
			InsuredSearchBean a_insuredSearchBean, Policy policy) {
		List<Insured> result = new ArrayList<Insured>();
		if (a_insuredSearchBean == null) {
			// do nothing - return empty result
		} else {
			try {

				CriteriaBuilder a_criteriaBuilder = entityManager
						.getCriteriaBuilder();
				CriteriaQuery<Insured> a_criteriaQuery = a_criteriaBuilder
						.createQuery(Insured.class);
				Root<Insured> insuredRoot = a_criteriaQuery
						.from(Insured.class);
				a_criteriaQuery.select(insuredRoot);

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (policy != null && policy.getPolicyNumber() != null) {
					predicates.add(a_criteriaBuilder.like(
							insuredRoot.<Policy>get("policy").<String> get("policyNumber"),
							policy.getPolicyNumber()));
				}

				// where clause
				if (a_insuredSearchBean.getAge() != null) {
					predicates.add(a_criteriaBuilder.equal(
							insuredRoot.<Integer> get("insuredAge"),
							a_insuredSearchBean.getAge()));
				}
				if (a_insuredSearchBean.getInsuredName() != null) {
					predicates.add(a_criteriaBuilder.like(
							a_criteriaBuilder.upper(insuredRoot.<String> get("insuredName")), "%"
									+ StringUtils.trim(a_insuredSearchBean.getInsuredName()).toUpperCase()
									+ "%"));

				}

				if (a_insuredSearchBean.getDateofbirth() != null) {

					predicates.add(a_criteriaBuilder.equal(
							insuredRoot.<Date> get("insuredDateOfBirth"),
							a_insuredSearchBean.getDateofbirth()));
				}

				if (a_insuredSearchBean.getGender() != null) {
//					String genderMaster = (StringUtils.trim(a_insuredSearchBean.getGender()
//							.getValue()).equalsIgnoreCase("Male") ? "M"
//							: StringUtils.trim(a_insuredSearchBean.getGender().getValue())
//									.equalsIgnoreCase("Female") ? "F" : "T");
					
					System.out.println("Gender value  -   Id  : "
							+ a_insuredSearchBean.getGender().getValue()+
							a_insuredSearchBean.getGender().getId());
					
					Predicate genderPredicate =  a_criteriaBuilder.equal(
							insuredRoot.<MastersValue> get("insuredGender").<Long>get("key"),
							a_insuredSearchBean.getGender().getId());
					predicates.add(genderPredicate);
				}
				if (a_insuredSearchBean.getHealthCardNumber() != null && !a_insuredSearchBean.getHealthCardNumber().equals("")) {
					String healthCardNumber = (a_insuredSearchBean.getHealthCardNumber());
					System.out.println("healthCardNumber "
							+ a_insuredSearchBean.getHealthCardNumber());
					predicates.add(a_criteriaBuilder.like(
							insuredRoot.<String> get("healthCardNumber"),
							healthCardNumber));
				}
				
				
				
				if (!predicates.isEmpty()) {
					a_criteriaQuery.select(insuredRoot).where(
							predicates.toArray(new Predicate[] {}));

					// execute query
					TypedQuery<Insured> typedQueryInsured = entityManager
							.createQuery(a_criteriaQuery);

					result = typedQueryInsured.getResultList();

				}
				System.out.println("result.size() ========= " + result.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		BeanItemContainer<Insured> beans = new BeanItemContainer<Insured>(
				Insured.class);
		beans.addAll(result);
		return beans;

	}
	
	
	public List<GmcMainMemberList> searchGMCInsured(
			InsuredSearchBean a_insuredSearchBean, PremPolicy policy) {
		List<GmcMainMemberList> result = new ArrayList<GmcMainMemberList>();
		if (a_insuredSearchBean == null) {
			// do nothing - return empty result
		} else {
			try {
				CriteriaBuilder a_criteriaBuilder = entityManager
						.getCriteriaBuilder();
				CriteriaQuery<GmcMainMemberList> a_criteriaQuery = a_criteriaBuilder
						.createQuery(GmcMainMemberList.class);
				Root<GmcMainMemberList> insuredRoot = a_criteriaQuery
						.from(GmcMainMemberList.class);
				a_criteriaQuery.select(insuredRoot);
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (policy != null && policy.getPolicyNo() != null) {
					predicates.add(a_criteriaBuilder.like(
							insuredRoot.<String> get("policyNumber"),
							policy.getPolicyNo()));
				}
				//IMSSUPPOR-27773 commented for deleted insured to list in create intimation - 12-Mar-2019
				/*predicates.add(a_criteriaBuilder.equal(
						insuredRoot.<Long> get("endorsementNumber"),
					    Long.valueOf(policy.getPolicyEndNoIdx())));*/
				
				if (a_insuredSearchBean.getGmcMainMemberName() != null) {
					predicates.add(a_criteriaBuilder.like(
							a_criteriaBuilder.upper(insuredRoot.<String> get("mainMemberName")), "%"
									+ StringUtils.trim(a_insuredSearchBean.getGmcMainMemberName()).toUpperCase()
									+ "%"));
				}
				
				// where clause
				if (a_insuredSearchBean.getAge() != null) {
					predicates.add(a_criteriaBuilder.equal(
							insuredRoot.<Integer> get("age"),
							a_insuredSearchBean.getAge()));
				}
				if (a_insuredSearchBean.getInsuredName() != null) {
					predicates.add(a_criteriaBuilder.like(
							a_criteriaBuilder.upper(insuredRoot.<String> get("insuredName")), "%"
									+ StringUtils.trim(a_insuredSearchBean.getInsuredName()).toUpperCase()
									+ "%"));
				}

				if (a_insuredSearchBean.getHealthCardNumber() != null && !a_insuredSearchBean.getHealthCardNumber().equals("")) {
					String healthCardNumber = (a_insuredSearchBean.getHealthCardNumber());
					System.out.println("healthCardNumber "
							+ a_insuredSearchBean.getHealthCardNumber());
					predicates.add(a_criteriaBuilder.equal(
							insuredRoot.<String> get("idCardNumber"),
							healthCardNumber));
				}
				
				if (a_insuredSearchBean.getEmployeeId() != null && !a_insuredSearchBean.getEmployeeId().equals("")) {
					String employeeId = (a_insuredSearchBean.getEmployeeId());

					predicates.add(a_criteriaBuilder.like(
							insuredRoot.<String> get("employeeId"),
							employeeId));
				}
	
				if (!predicates.isEmpty()) {
					a_criteriaQuery.select(insuredRoot).where(
							predicates.toArray(new Predicate[] {}));

					// execute query
					TypedQuery<GmcMainMemberList> typedQueryInsured = entityManager
							.createQuery(a_criteriaQuery);

					result = typedQueryInsured.getResultList();

				}
				System.out.println("result.size() ========= " + result.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;

	}
	

	@SuppressWarnings({ "unchecked" })

//	public List<TmpInsured> getInsuredList(String a_key) {
//
//		Query query = entityManager
//				.createNamedQuery("TmpInsured.findByPolicyNo");
//		query = query.setParameter("parentKey", a_key);
//		List<TmpInsured> insuredList = query.getResultList();
//
//		return insuredList;
//	}
	
//	public BeanItemContainer<TmpInsured> getInsuredList(String a_key){
//		 Query query = entityManager.createNamedQuery("TmpInsured.findByPolicyNo");
//		 query = query.setParameter("parentKey", a_key);		        
//		 List<TmpInsured> insuredList  = query.getResultList();		
//		
//		 BeanItemContainer<TmpInsured> beans = new BeanItemContainer<TmpInsured>(TmpInsured.class);
//	     beans.addAll(insuredList);
//	     
//		return beans;
//
//	}
	/**
	 * To get Insured list for create intimation
	 * 
	 * 
	 */
	
	public BeanItemContainer<Insured> getCLSInsuredList(String a_key){
		 Query query = entityManager.createNamedQuery("Insured.findByPolicyNo");
		 query = query.setParameter("policyNumber", a_key);		        
		 List<Insured> insuredList  = query.getResultList();		
		
		 BeanItemContainer<Insured> beans = new BeanItemContainer<Insured>(Insured.class);
	     beans.addAll(insuredList);
	     
		return beans;

	}
	


//	public TmpInsured getInsuredByKey(Long key) {
//		return entityManager.find(TmpInsured.class, key);
//	}
	
	public Insured getInsuredByInsuredKey(Long key) {
		return entityManager.find(Insured.class, key);
	}

//	public TmpInsured getInsured(String policyNumber, String insuredFirstName,
//			Date dateOfBirth) {
//
//		Query query = entityManager
//				.createNamedQuery("TmpInsured.findByPolicyAndInsuredName");
//		query = query.setParameter("policeNo", policyNumber);
//		query = query.setParameter("insuredName", insuredFirstName);
//		query = query.setParameter("dob", dateOfBirth);
//		if (query.getResultList().size() != 0)
//			return (TmpInsured) query.getSingleResult();
//		return null;
//
//	}
	
	/***
	 * IMS_CLS_INSURED is the new table introduced. Hence the below
	 * method will retrieve data from IMS_CLS_INSURED table. Since TmpInsured
	 * is already in use, the above method remains unchanged, as renaming 
	 * this would cause impact in other services.
	 * **/
	
	public Insured getCLSInsured(String policyNumber, String insuredFirstName,
			Date dateOfBirth) {

		Query query = entityManager
				.createNamedQuery("Insured.findByPolicyAndInsuredName");
		query = query.setParameter("policeNo", policyNumber);
		query = query.setParameter("insuredName", insuredFirstName);
		query = query.setParameter("dob", dateOfBirth);
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}
	
//	public TmpInsured getInsured(String key) {
//
//		Query query = entityManager
//				.createNamedQuery("TmpInsured.findByInsured");
//		query = query.setParameter("key", Long.parseLong(key));
//		if (query.getResultList().size() != 0)
//			return (TmpInsured) query.getSingleResult();
//		return null;
//
//	}
	
	
	
	/**
	 * The below method is used to fetch Insured details
	 * from IMS_CLS_INSURED Table. This table is a new table
	 * which was introduced during policy table change requirement.
	 * 
	 * */
	public Insured getCLSInsured(String key) {

		Query query = entityManager
				.createNamedQuery("Insured.findByInsured");
		query = query.setParameter("key", Long.parseLong(key));
		if (query.getResultList().size() != 0)
			return (Insured) query.getSingleResult();
		return null;

	}
	
//	public List<TmpInsured> getInsuredListByPoliycyNo(String a_key){
//		 Query query = entityManager.createNamedQuery("TmpInsured.findByPolicyNo");
//		 query = query.setParameter("parentKey", a_key);		        
//		 List<TmpInsured> insuredList  = query.getResultList();			     
//		return insuredList;
//
//	}
	
	@SuppressWarnings("unchecked")
	public List<Insured> getInsuredByPolicyKey(Long policyKey, Long insuredKey){
		Query query = entityManager.createNamedQuery("Insured.findByPolicyKey");
		 query = query.setParameter("key", policyKey);	
		 query = query.setParameter("insuredKey", insuredKey);
		 List<Insured> insuredList  = (List<Insured>) query.getResultList();			     
		return insuredList;		
	}
	
	
	
	/**
	 * The below method is used to fetch Insured details
	 * from IMS_CLS_INSURED Table. This table is a new table
	 * which was introduced during policy table change requirement.
	 * 
	 * */
	public List<Insured> getInsuredListByPolicyNo(String a_key){
		 Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
		 query = query.setParameter("policykey", Long.valueOf(a_key));		        
		 List<Insured> insuredList  = query.getResultList();			     
		return insuredList;

	}
	
	public Insured getInsuredByPolicyNo(String a_key){
		 Query query = entityManager.createNamedQuery("Insured.findByPolicykey1");
		 query = query.setParameter("policykey", Long.valueOf(a_key));		        
		List<Insured> insuredList = (List<Insured>)query.getResultList();
		
		if(insuredList != null && ! insuredList.isEmpty()){
			return insuredList.get(0);
		}
		return null;

	}
	
	
	
	
	
	/**
	 * The below method is used to fetch Insured details 
	 * from IMS_CLS_INSURED Table. This 
	 * which was introduced during call from another service.
	 * 
	 * */
	public List<Insured> getInsuredListByPolicyNo(String policyNo,EntityManager entityManager){
		this.entityManager = entityManager;
		 Query query = this.entityManager.createNamedQuery("Insured.findByPolicyNo");
		 query = query.setParameter("policyNumber", policyNo);		        
		 List<Insured> insuredList  = query.getResultList();
		/* for (Insured insured : insuredList) {
			entityManager.refresh(insured);
		}*/
		return insuredList;

	}
	
	public List<InsuredPedDetails> getInsuredKeyListByInsuredkey(Long insuredKey){
		 Query query = entityManager.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		 query = query.setParameter("insuredKey", insuredKey);		        
		 List<InsuredPedDetails> insuredList  = query.getResultList();			     
		return insuredList;

	}
	
	public List<Insured> getInsuredListByPolicyNumber(String policyNo) {
		 Query query = this.entityManager.createNamedQuery("Insured.findByPolicyNo");
		 query = query.setParameter("policyNumber", policyNo);		        
		 List<Insured> insuredList  = query.getResultList();
		/* for (Insured insured : insuredList) {
			entityManager.refresh(insured);
		}*/
		return insuredList;

	}
	
	@SuppressWarnings("unchecked")
	public List<InsuredCover> getInsuredCoverByInsuredKey(Long insuredKey){
		Query query = entityManager.createNamedQuery("InsuredCover.findByInsured").setParameter("insuredKey", insuredKey);
		 List<InsuredCover> insuredList  = (List<InsuredCover>) query.getResultList();			     
		return insuredList;		
	}
	
	public void updateAadharCardDetails(Insured insuredDtls) {
		entityManager.merge(insuredDtls);
		entityManager.flush();
		
	}
	
	public BeanItemContainer<Insured>  getInsuredByPolicyNoForOP(String policyNo) {
		 Query query = entityManager.createNamedQuery("Insured.findByPolicyNo");
		 query = query.setParameter("policyNumber", policyNo);		        
		 List<Insured> insuredList  = query.getResultList();
		 List<Insured> insuredResult = new ArrayList<Insured>();
		insuredList = query.getResultList();
		if(null != insuredList && !insuredList.isEmpty()) {
			for (Insured insured : insuredList) {
				if(insured.getLopFlag() == null || SHAConstants.HEALTH_LOB_FLAG.equalsIgnoreCase(insured.getLopFlag())){
					insuredResult.add(insured);
				}
			}
		}
		 BeanItemContainer<Insured> beans = new BeanItemContainer<Insured>(Insured.class);
	     beans.addAll(insuredResult);
		return beans;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
		
	}

}
