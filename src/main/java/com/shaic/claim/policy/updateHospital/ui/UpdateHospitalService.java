package com.shaic.claim.policy.updateHospital.ui;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.UpdateHospital;

@Stateless
public class UpdateHospitalService {
	
	@PersistenceContext
	protected EntityManager entityManager;

	public UpdateHospitalService() {
		super();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void submitUpdateHospital(UpdateHospitalDTO updateHospitalDto, UpdateHospital updateHospital) {
		try{
		if (updateHospitalDto != null) {
			if (updateHospital.getKey() != null) {
				entityManager.merge(updateHospital);
			} else { 
				System.out.println("Persist ------------!!!!!!!!!!!!!!!!-------------------------");
				
				entityManager.persist(updateHospital);
				
				System.out.println("Persist Finish ------------!!!!!!!!!!!!!!!!-------------------------");
				
				
			}
			
			entityManager.flush();
			entityManager.clear();
			System.out.println("flush Finish ------------!!!!!!!!!!!!!!!!-------------------------");

			updateHospitalDto.setKey(updateHospital.getKey() != null ? updateHospital
					.getKey().longValue() : null);
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public UpdateHospital searchByKey(Long a_key) {
		UpdateHospital find = entityManager.find(UpdateHospital.class, a_key);
		entityManager.refresh(find);
		return find;
	}
	
	public UpdateHospital searchHospitalByKey(Long a_key) {
		
		Query query = entityManager.createNamedQuery("UpdateHospital.findByKey");
		query.setParameter("primaryKey", a_key);
		
		List<UpdateHospital> find = (List<UpdateHospital>)query.getResultList();
		for (UpdateHospital updateHospital : find) {
			entityManager.refresh(updateHospital);
		}
		if(find != null && !find.isEmpty()){
			return find.get(0);
		}
		return null;
	}

}
