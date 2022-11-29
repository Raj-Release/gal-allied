package com.shaic.reimbursement.fraudidentification;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAUtils;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;

@Stateless
public class FraudIdentificationService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	public void submitTFraudidentification(List<FraudIdentificationTableDTO> dto) {

		String userId = (String) VaadinSession.getCurrent().getAttribute(
				BPMClientContext.USERID);

		for (FraudIdentificationTableDTO idaTableDTO : dto) {

			FraudIdentification fraudIdentity = getFraudIdentificationOldList(idaTableDTO);

			if (fraudIdentity == null) {
				fraudIdentity = new FraudIdentification();
				fraudIdentity.setValueType(idaTableDTO.getParameterType());
				fraudIdentity.setValue(idaTableDTO.getParameterValue());
				if (idaTableDTO.getDisable() != null
						&& idaTableDTO.getDisable()) {
					fraudIdentity.setActiveStatus(String.valueOf(0));
				} else {
					fraudIdentity.setActiveStatus(String.valueOf(1));
				}
				fraudIdentity.setCreatedBy(userId);
				fraudIdentity.setCreatedDate(new Date());
				fraudIdentity.setModifyBy(userId);
				fraudIdentity.setModifiedDate(new Date());
				fraudIdentity.setEffectiveFromDate(idaTableDTO.getEffectiveStartDate());
				if(idaTableDTO.getEffectiveEndDate() ==null){
					fraudIdentity.setEffectiveToDate(new Date());
					}
					else{
						fraudIdentity.setEffectiveToDate(idaTableDTO.getEffectiveEndDate());
					}
				fraudIdentity.setCcEmail(idaTableDTO.getRecipientCc());
				fraudIdentity.setToEmail(idaTableDTO.getRecipientTo());
				fraudIdentity.setUserRemark(idaTableDTO.getUserRemarks());
				
				entityManager.persist(fraudIdentity);

			} else {
				fraudIdentity.setModifyBy(userId);
				fraudIdentity.setModifiedDate(new Date());
				fraudIdentity.setValue(idaTableDTO.getParameterValue());
				if (idaTableDTO.getDisable() != null && idaTableDTO.getDisable()) {
					fraudIdentity.setActiveStatus(String.valueOf(0));
				} else {
					fraudIdentity.setActiveStatus(String.valueOf(1));
				}
				fraudIdentity.setEffectiveFromDate(idaTableDTO.getEffectiveStartDate());
				if(idaTableDTO.getEffectiveEndDate() ==null){
				fraudIdentity.setEffectiveToDate(new Date());
				}
				else{
					fraudIdentity.setEffectiveToDate(idaTableDTO.getEffectiveEndDate());
				}
				fraudIdentity.setCcEmail(idaTableDTO.getRecipientCc());
				fraudIdentity.setToEmail(idaTableDTO.getRecipientTo());
				fraudIdentity.setUserRemark(idaTableDTO.getUserRemarks());
				entityManager.merge(fraudIdentity);
			}

			entityManager.flush();
			entityManager.clear();
		}
	}
	
	public List<FraudIdentification> getFraudIdentificationObjList(String parameterType){
		Query  query = entityManager.createNamedQuery("FraudIdentification.findByParameterType");
		query = query.setParameter("valueType", parameterType);
		List<FraudIdentification> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj;
		}
		return null;
	}
	
	public FraudIdentification getFraudIdentificationOldList(FraudIdentificationTableDTO value){
		Query  query = entityManager.createNamedQuery("FraudIdentification.findByParameterValue");
		query = query.setParameter("value", value.getParameterValue());
		query = query.setParameter("valueType", value.getParameterType());
		List<FraudIdentification> listOfObj = query.getResultList();
		
		if(listOfObj != null && !listOfObj.isEmpty()){
			return listOfObj.get(0);
		}
		return null;
	}

}
