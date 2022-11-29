package com.shaic.ims.bpm.claim;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.tomcat.util.codec.binary.Base64;

import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.preauth.WSLockPolicy;

@Stateless
public class BancsDBService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void insertLockTable(Claim claim, String ltype) {
		WSLockPolicy lock = new WSLockPolicy();
		lock.setClaimNumber(claim.getClaimId());
		lock.setIntimationNumber(claim.getIntimation().getIntimationId());
		lock.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber() != null ? claim.getIntimation().getPolicy().getPolicyNumber(): "");
		// lock.setRemarks(remarks);
		lock.setType(ltype);
		entityManager.persist(lock);
	}
	
	public EntityManager getEntityManagerInstance(){
		return entityManager;
	}
	
	public Map<String, String> getAuthAndRequestParam(String argRequestType) {
		Map<String, String> requestContainer = new HashMap<String, String>();
		BancsSevice bancsService = BancsSevice.getInstance();
		BancsHeaderDetails reqDtlObj = bancsService.getRequestHeaderValues(argRequestType);
		if(reqDtlObj != null){
			byte[] val = (reqDtlObj.getWsUserName()+":"+reqDtlObj.getWsPassword()).getBytes();
			String authString = new String(Base64.encodeBase64(val));
			requestContainer.put("authkey", authString);
			requestContainer.put("business", reqDtlObj.getWebserviceBC());
			requestContainer.put("usercode", reqDtlObj.getWebserviceUserCode());
			requestContainer.put("rolecode", reqDtlObj.getWebserviceRoleCode());
			Long id = bancsService.getBancsServiceTransactionalId();
			if(id != null && id.intValue() > 0){
				requestContainer.put("tranid", String.valueOf(id));
			}else{
				requestContainer.put("tranid", String.valueOf(0));
			}
			return requestContainer;
		}else{
			System.out.println("Auth and request parameter going wrong configure bancs user dtls tbl");
			return null;
		}
	}

}


