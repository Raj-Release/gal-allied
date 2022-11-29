package com.shaic.restservices.pms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.StageIntimation;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.starfax.simulation.PremiaPullService;
import com.shaic.restservices.crm.Error;

@Path("/pms")
//@Stateless
public class PmsHospitalService {

	@EJB
	private PremiaPullService premiaPullService;
	
	@EJB
	private IntimationService intimationService;

	private final Logger log = LoggerFactory.getLogger(PmsHospitalService.class);
	private final String regEx = "[: =]";
	
	@SuppressWarnings("static-access")
	@POST
	@Path("/UpdateHospitalDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateHospitalDetails(PmsHospitalRequest pmsHospitalRequest, @HeaderParam("authorization") String authString) {
		PmsHospitalResponse pmsHospitalResponse = new PmsHospitalResponse();
		List<Error> errors = new ArrayList<Error>();
		String returnMsg = null;
		boolean hasError = false;
		// Replace all Bearer word to empty
		authString = authString.replaceAll("Bearer", "");
		authString = authString.replaceAll("bearer", "");
		authString = authString.replaceAll(regEx, "");
		

		if((authString != null) && (authString.equals(SHAConstants.CRM_WEBSERVICE_AUTH))) {
			log.debug(pmsHospitalRequest.toString());
			
			if((pmsHospitalRequest.getIntimationNo() == null || (pmsHospitalRequest.getIntimationNo() != null && pmsHospitalRequest.getIntimationNo().isEmpty()))){
				hasError = true;
				Error error = new Error();
				error.setError("Intimation number should not be blank");
				errors.add(error);
			}
			
			if((pmsHospitalRequest.getIntimationId() == null || (pmsHospitalRequest.getIntimationId() != null && pmsHospitalRequest.getIntimationId().isEmpty()))){
				hasError = true;
				Error error = new Error();
				error.setError("Intimation Id should not be blank");
				errors.add(error);
			}else{
			   
			}
			
			if((pmsHospitalRequest.getHospitalProviderCode() == null || (pmsHospitalRequest.getHospitalProviderCode() != null && pmsHospitalRequest.getHospitalProviderCode().isEmpty()))){
				hasError = true;
				Error error = new Error();
				error.setError("Provider Code should not be blank");
				errors.add(error);
			}

		} else {
			hasError = true;
			Error error = new Error();
			error.setError("Authorization is invalid");
			errors.add(error);
		}
		// Error availability checking
		pmsHospitalResponse.setErrorYN(hasError ? "Y" : "N");
		pmsHospitalResponse.setErrors(errors);
		if(hasError) {
			pmsHospitalResponse.setResultMsg("Error occurred while processing claim data");
		} else {
			try{
				GalaxyIntimation intimation = intimationService.getGalaxyIntimationByKey(Long.valueOf(pmsHospitalRequest.getIntimationId()));
				   if(intimation == null){
				    	hasError = true;
						Error error = new Error();
						error.setError("Intimation id is not available");
						errors.add(error);
						pmsHospitalResponse.setErrorYN(hasError ? "Y" : "N");
						pmsHospitalResponse.setResultMsg("Hospital Details updated Failure");
				    }
					else{
						intimation.setHospitalCode(pmsHospitalRequest.getHospitalProviderCode());
						Status status = new Status();
						status.setKey(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY);
						intimation.setStatus(status);
						premiaPullService.updateIntimationFromwebService(intimation);
						pmsHospitalResponse.setErrorYN(hasError ? "Y" : "N");
						pmsHospitalResponse.setResultMsg("Hospital Details updated successfully.");
					}

				

			}catch(Exception exp){
				exp.printStackTrace();
			}
		}
		// Convert object to JSON string
        Gson gson = new Gson();
        returnMsg = gson.toJson(pmsHospitalResponse);
        
        return Response.ok(returnMsg, MediaType.APPLICATION_JSON).build();
	}
	
/*	@SuppressWarnings("unchecked")
	private MastersValue getMastersValue(String masterValue, String masterCode) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKeyByCodeAndValue");
		query = query.setParameter("parentKey", masterCode);
		query = query.setParameter("value", masterValue);
		
		List<MastersValue> masterValueList = new ArrayList<MastersValue>();
		masterValueList = query.getResultList();
		if(null != masterValueList && !masterValueList.isEmpty()) {
			return masterValueList.get(0);
		}
		return null;
	}*/
	
	/*@SuppressWarnings("unchecked")
	public Hospitals getHospitalByHospNo(String hospCode) {
		Query query = entityManager.createNamedQuery("Hospitals.findByCode");
		query = query.setParameter("hospitalCode", hospCode.toUpperCase());
		List<Hospitals> hospitalList = query.getResultList();
		if(null != hospitalList && !hospitalList.isEmpty()) {
			return hospitalList.get(0);
		}
		return null;
	}*/

}
