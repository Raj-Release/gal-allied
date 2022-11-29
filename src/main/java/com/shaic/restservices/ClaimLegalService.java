package com.shaic.restservices;

/**
 * @author ntv.vijayar
 *
 */

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
//import org.jboss.resteasy.logging.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Claim;



@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "legal-process")
@Path("/legal-process")
@Stateless
public class ClaimLegalService{
	
	//@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(ClaimLegalService.class);
	
   /* @GET
    @Path("/users/{id}")
    public TestServicePojo getUserById(@PathParam("id") Integer id) {
        
        TestServicePojo user = new TestServicePojo();
        user.setFirstName("Sivakumar");
        user.setLastName("Krishnasamy");
        return user;
    }*/
    
    @POST
    @Path("/claim-legal")
    @Consumes("application/json")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String claimLegal(LegalPojo pojo) {
    	String successMessage = null;
    	String intimationNo = pojo.getIntimationNumber();
    	String policyNo = pojo.getPolicyNumber();
    	String legalFlag = pojo.getLegalFlag();
    	if((null == intimationNo || ("").equalsIgnoreCase(intimationNo)))
    	{
    		log.info("Intimation number is mandatory for legal process");
    		return "Intimation number is mandatory for legal process";
    	}
    	if (null == legalFlag || ("").equalsIgnoreCase(legalFlag))
    	{
    		log.info("Legal flag is mandatory for legal process");
    		return "Legal flag is mandatory for legal process";
    	}
    	if((null != intimationNo && !("").equalsIgnoreCase(intimationNo)) && (null != legalFlag && !("").equalsIgnoreCase(legalFlag)))
    	{
	    	Claim objClaim = getClaimObject(intimationNo, policyNo);
	    	if(null != objClaim)
	    	{
	    		if((SHAConstants.LEGAL_LOCK).equals(legalFlag))
				{
	    			log.info("Before seting Legal flag to Y");
	    			objClaim.setLegalFlag(SHAConstants.YES_FLAG);
	    			log.info("After seting Legal flag to Y");
	    			successMessage = "Claim Locked successfully";
	    			log.info("Claim Locked successfully");
				}
	    		else if((SHAConstants.LEGAL_UNLOCK).equals(legalFlag))
	    		{
	    			log.info("Before seting Legal flag to N");
	    			objClaim.setLegalFlag(SHAConstants.N_FLAG);
	    			log.info("Before seting Legal flag to N");
	    			successMessage = "Claim UnLocked successfully";
	    			log.info("Claim UnLocked successfully");
	    		}
	    		try
	    		{
		    		if(null != objClaim.getKey())
		    		{
		    			log.info("Before updating the claim object");
		    			entityManager.merge(objClaim);
		    			entityManager.flush();
		    			entityManager.clear();
		    			log.info("claim object updated and commited");
		    		}
		    		setBPMTaskStatus(intimationNo, legalFlag);
	    		}
	    		catch(Exception e)
	    		{
	    			e.printStackTrace();
	    			log.error("Error occurred while processing claim. Contact system administrator for further processing",e);
	    			log.info("Error occurred while processing claim. Contact system administrator for further processing");
	    			successMessage = "Error occurred while processing claim. Contact system administrator for further processing";
	    		}
	    	}
    	}
        return successMessage;
    }
    
    /**
     * Close claim and reopen claim task
     * refractoring needs to be implemented
     * in the below method.
     * 
     * */
    private void setBPMTaskStatus(String intimationNo, String legalFlag)
    {/*
		log.info("---Inside BPM task status method---");

    	String userName = BPMClientContext.BPMN_TASK_USER; 
    	String password = BPMClientContext.PASSWORD;
    	PayloadBOType payloadType = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimationNo);
		payloadType.setIntimation(intimationType);
		
		PagedTaskList tasks = null;

		if((SHAConstants.LEGAL_LOCK).equalsIgnoreCase(legalFlag))
		{
			CloseAllClaimTask closeClaimTask = BPMClientContext.getCloseClaimTask(userName,password);
			tasks = closeClaimTask.getTasks(userName, null, payloadType);
			
		
		}
		else if((SHAConstants.LEGAL_UNLOCK).equalsIgnoreCase(legalFlag))
		{
			ReopenAllClaimTask reOpenClaimTask = BPMClientContext.getReOpAllClaimTask(userName, password);
			tasks= reOpenClaimTask.getTasks(userName, null, payloadType);
			log.info("Task unlocked successfully by BPMN Services");
		}
		if(null != tasks)
		{
			List<HumanTask> taskList = tasks.getHumanTasks();
			if(null != taskList && !taskList.isEmpty())
			{
				for (HumanTask humanTask : taskList) {
					SystemActionsHumanTaskService task = BPMClientContext.getActiveAndDeactiveHumanTask(userName, password);
					if((SHAConstants.LEGAL_LOCK).equalsIgnoreCase(legalFlag))
					{
						task .execute(BPMClientContext.BPMN_TASK_USER, humanTask.getNumber(),SHAConstants.SUSPEND_HUMANTASK);
						log.info("Task No  "+humanTask.getNumber()+" locked successfully by BPMN Services");
						//BPMClientContext.setActiveOrDeactiveClaim(userName,password, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
					}
					else if((SHAConstants.LEGAL_UNLOCK).equalsIgnoreCase(legalFlag))
					{
						task .execute(userName, humanTask.getNumber(),SHAConstants.RESUME_HUMANTASK);
						BPMClientContext.setActiveOrDeactiveClaim(userName,password, humanTask.getNumber(), SHAConstants.SYS_RELEASE);
						log.info("Task No  "+humanTask.getNumber()+" unlocked successfully by BPMN Services");
					}
				}
			}
		}
	   */}
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private Claim getClaimObject(String intimationNo , String policyNo)
    {
    	Query query = null;
    	if(null != policyNo && !("").equalsIgnoreCase(policyNo))
    	{
    		query = entityManager.createNamedQuery("Claim.findByIntimationNoAndPolicyNo");
        	query = query.setParameter("policyNumber", policyNo);
    	}
    	else
    	{
    		 query = entityManager.createNamedQuery("Claim.findByIntimationNo");
    	}
    	query = query.setParameter("intimationNumber", intimationNo);
    	List<Claim> claimList = query.getResultList();
    	if(null != claimList && !claimList.isEmpty())
    	{
    		entityManager.refresh(claimList.get(0));
    		return claimList.get(0);
    		//claimList.
    	}
    	return null;
    }
}
