package com.shaic.restservices;

/**
 * @author Velmurugan Rajendran
 *
 */
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAConstants;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.ims.bpm.claim.DBCalculationService;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "crm")
@Path("/crm")
@Stateless
public class CRMWebService {

	@PersistenceContext
	protected EntityManager entityManager;

	private final Logger log = LoggerFactory.getLogger(CRMWebService.class);
	private final String regEx = "[: =]";

	@GET
	@Path("/users/{id}")
	public CRMRequest getUserById(@PathParam("id") Integer id) {
		CRMRequest user = new CRMRequest();
		user.setIntimationNumber("aaa");
		user.setCrmFlaggedReason("reason");
		user.setCrmFlaggedRemarks("remarks");
		user.setCrcPriorityCode("code");
		user.setCrcPriorityDesc("description");

		ObjectMapper mapperObj = new ObjectMapper();
		try {
			String jsonStr = mapperObj.writeValueAsString(user);
			System.out.println(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	@POST
	@Path("/flag")
	@Consumes("application/json")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String claimLegal(CRMRequest cRMRequest, @HeaderParam("authorization") String authString) {
		String returnMessage = null;
		authString = authString.replaceAll("Bearer", "");
		authString = authString.replaceAll("bearer", "");
		authString = authString.replaceAll(regEx, "");

		if(authString!=null && authString.equals(SHAConstants.CRM_WEBSERVICE_AUTH)) {
			String intimationNo = cRMRequest.getIntimationNumber();
			String crmFlaggedReason = cRMRequest.getCrmFlaggedReason();
			String crmFlaggedRemarks = cRMRequest.getCrmFlaggedRemarks();
			String crcPriorityCode = cRMRequest.getCrcPriorityCode();
			String crcPriorityDesc = cRMRequest.getCrcPriorityDesc();

			if((null != intimationNo && !("").equalsIgnoreCase(intimationNo))) {
				Claim objClaim = getClaimObject(intimationNo, null);
				if(null != objClaim) {
					DBCalculationService dbCalculationService = new DBCalculationService();
					if(crmFlaggedReason!=null && StringUtils.isNotEmpty(crmFlaggedReason)) {
						if(objClaim.getIntimation() != null && objClaim.getIntimation().getAdmissionReason() != null){
							Map<String, Object> getPriorityEvent =	dbCalculationService.getPriorityFlag(objClaim.getKey(),objClaim.getIntimation().getAdmissionReason());
					         if(getPriorityEvent != null){
					        	 if(getPriorityEvent.containsKey("priorityWeightage")){
					        		 objClaim.setPriorityWeightage((Long) getPriorityEvent.get("priorityWeightage")); 
					        	 }
					        	 if(getPriorityEvent.containsKey("priorityEvent") && !((String) getPriorityEvent.get("priorityEvent")).isEmpty()){
					        		 objClaim.setPriorityEvent((String) getPriorityEvent.get("priorityEvent")); 
					        	 }
					         }
						}
						//CMD weightage limit update in claim table for CR2020190
						if(objClaim.getIntimation() != null && objClaim.getIntimation().getPolicy() != null && objClaim.getIntimation().getPolicy().getKey() != null ){
							String memberType = dbCalculationService.getCMDMemberType(objClaim.getIntimation().getPolicy().getKey());
							if(null != memberType && !memberType.isEmpty()){
								objClaim.setClaimClubMember(memberType);
								Map<String, Object> getPriorityEvent =	dbCalculationService.getPriorityFlag(0l,memberType);
								if(getPriorityEvent != null){
									if(getPriorityEvent.containsKey("priorityWeightage")){
										objClaim.setPriorityWeightage((Long) getPriorityEvent.get("priorityWeightage")); 
									}
									if(getPriorityEvent.containsKey("priorityEvent") && !((String) getPriorityEvent.get("priorityEvent")).isEmpty()){
										objClaim.setPriorityEvent((String) getPriorityEvent.get("priorityEvent")); 
									}
								}
							}
						}
						//weightage limit update in claim table for VSP FLAG is Y in hospital table
						Hospitals hospitalDetailsByKey = getHospitalDetailsByKey(objClaim.getIntimation().getHospital());
						if(null != hospitalDetailsByKey && hospitalDetailsByKey.getFspFlag() != null && hospitalDetailsByKey.getFspFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
							Map<String, Object> getPriorityEvent =	dbCalculationService.getPriorityFlag(0l,"VSP");
							if(getPriorityEvent != null){
								if(getPriorityEvent.containsKey("priorityWeightage")){
									objClaim.setPriorityWeightage((Long) getPriorityEvent.get("priorityWeightage")); 
								}
								if(getPriorityEvent.containsKey("priorityEvent") && ((String) getPriorityEvent.get("priorityEvent")) != null && !(((String) getPriorityEvent.get("priorityEvent")).isEmpty())){
									objClaim.setPriorityEvent((String) getPriorityEvent.get("priorityEvent")); 
								}
							}
						}
						objClaim.setCrcFlag("Y");
						objClaim.setCrcFlaggedReason(crmFlaggedReason);
						objClaim.setCrcFlaggedRemark(crmFlaggedRemarks);
						objClaim.setCrcFlaggedDate(new Date());
						objClaim.setCrcPriorityCode(crcPriorityCode);
						objClaim.setCrcPriorityDesc(crcPriorityDesc);

						try {
							if(null != objClaim.getKey()) {
								log.info("Before updating the claim object");
								entityManager.merge(objClaim);
								entityManager.flush();
								entityManager.clear();
								returnMessage = "Success";
								log.info("claim object updated and commited");
							}
						} catch(Exception e) {
							e.printStackTrace();
							log.info("Error occurred while processing claim. Contact system administrator for further processing");
							returnMessage = "Error occurred while processing claim. Contact system administrator for further processing";
						}
					}
				} else {
					log.info("Intimation number is not found");
					returnMessage = "Intimation number is not found";
				}
			} else {
				log.info("Intimation number is mandatory");
				returnMessage = "Intimation number is mandatory";
			}
		} else {
			log.info("Unable to process,Please check your Authorization");
			returnMessage = "Unable to process, Please check your Authorization";
		}
		return returnMessage;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Claim getClaimObject(String intimationNo , String policyNo) {
		Query query = null;
		if(null != policyNo && !("").equalsIgnoreCase(policyNo)) {
			query = entityManager.createNamedQuery("Claim.findByIntimationNoAndPolicyNo");
			query = query.setParameter("policyNumber", policyNo);
		} else {
			query = entityManager.createNamedQuery("Claim.findByIntimationNo");
		}		
		query = query.setParameter("intimationNumber", intimationNo);

		@SuppressWarnings("unchecked")
		List<Claim> claimList = query.getResultList();
		if(null != claimList && !claimList.isEmpty()) {
			entityManager.refresh(claimList.get(0));
			return claimList.get(0);
		}
		return null;
	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
}
