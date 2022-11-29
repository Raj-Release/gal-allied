package com.shaic.claim.policy.search.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shaic.arch.PremiaConstants;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.policy.search.ui.premia.PremPolicy;
import com.shaic.claim.policy.search.ui.premia.PremPolicySearchDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.preauth.StarfaxProvisionHistory;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.bpm.claim.IntimationDto;
import com.shaic.main.DocumentDetailsApi;
import com.shaic.main.DocumentDetailsValue;
import com.shaic.restservices.bancs.checkendomnt64vbstatus.Check64VBService;
import com.shaic.restservices.bancs.checkendomnt64vbstatus.CheckEndorsementStatusService;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.restservices.bancs.lockPolicy.PolicyLockUnLockResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class PremiaService {
	
	private static final PremiaService instance = new PremiaService();
	
	private final Logger log = LoggerFactory.getLogger(PremiaService.class);
	 
	@EJB
	private PolicyService policyService;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public static PremiaService getInstance() {
		return instance;
	}

	public PremiaService() {
		
	}
	
	

	public Builder getBuilder() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(clientConfig);
		String strPremiaFlag = BPMClientContext.PREMIA_URL;
		WebResource webResource = client.resource(strPremiaFlag + "GetPolicySummary");
		webResource.accept("application/json");
		Builder builder = webResource.type("application/json");
		builder.accept("application/json");
		return builder;
	}
	
	public Builder getBuilderForProvison() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(clientConfig);
		String strPremiaFlag = BPMClientContext.PREMIA_URL;
		WebResource webResource = client.resource(strPremiaFlag + "CreateClaimProvision");
		
		webResource.accept("application/json");
		Builder builder = webResource.type("application/json");
		builder.accept("application/json");
		return builder;
	}
	
	public static Builder getPolicyDetail() {
	//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
		Builder builder = null;
		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag + "GetPolicyDetail");

			webResource.accept("application/json");
			
			builder =  webResource.type("application/json");
			builder.accept("application/json");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return builder;

	}
	
	public static Builder getJetPrivillagePolicyDetail() {
		//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
			Builder builder = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetJPPolicyDetail");

				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;

		}
	
	public static Builder getGpaPolicyDetails() {
		//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
			Builder builder = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetGPAPolicyDetail");

				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;

		}
	
	public static Builder getGpaBenefitDetails() {
		//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
			Builder builder = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetGPABenefitDetails");

				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;
	}
	
	public static Builder getRenewedPolicyDetails() {
		Builder builder = null;
		try{
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag+ "GetRenewedPolicyDetails");
			webResource.accept("application/json");
			builder = webResource.type("application/json");
			builder.accept("application/json");
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return builder;
	}
	@SuppressWarnings({ "unchecked", "null" })
	public static List<DMSDocumentDetailsDTO> getDocumentDetails(String hospitalCode) {
		//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
			Builder builder = null;
			String paramValue = hospitalCode;
			List<DMSDocumentDetailsDTO> docSearch = new ArrayList<DMSDocumentDetailsDTO>();
		
                try{
                	BPMClientContext context = new BPMClientContext();
                	
    				ClientConfig clientConfig = new DefaultClientConfig();
    				clientConfig.getClasses().add(JacksonJsonProvider.class);
    				
    				Client client = Client.create(clientConfig);
    				String strPremiaFlag = BPMClientContext.DOCS_URL;
    				WebResource webResource = client.resource(strPremiaFlag+paramValue);
    				client.setConnectTimeout(context.getAlfrescoTimeOut()); //The connection timeout is the timeout in making the initial connection; i.e. completing the TCP connection handshake
    				client.setReadTimeout(context.getAlfrescoTimeOut()); // if the server fails to send a byte <timeout> seconds after the last byte, a read timeout error will be raised
    				
    				String dmsAPIUrl = context.getDMSRestApiUrl();
    				
//    				String strPremiaFlag = "http://192.168.1.96/shipms/fetchDocumentListPremiaUrl.action?hosCode="+paramValue;
//    				WebResource webResource = client.resource(strPremiaFlag);

    				ClientResponse response = webResource.accept("application/json")
    				        .type("application/json").get(ClientResponse.class);
    				Gson gson = new Gson();
    				DocumentDetailsApi fromJson = gson.fromJson(response.getEntity(String.class),DocumentDetailsApi.class);
    				DMSDocumentDetailsDTO hospitalTariff = null;
    				if(fromJson != null){
    					DocumentDetailsValue[] jsonElement = fromJson.getJsonElement();
    					List<DocumentDetailsValue> detailsValues = Arrays.asList(jsonElement);
    					for (DocumentDetailsValue documentDetailsValue : detailsValues) {
    						//if (!documentDetailsValue.getDescription().contains("Room Tariff")) {
//    						System.out.println(documentDetailsValue.getDescription());
    						DMSDocumentDetailsDTO docVal = new DMSDocumentDetailsDTO();
    						docVal.setDocumentType(SHAConstants.HOSPITAL_DOC);
    						docVal.setFileName(documentDetailsValue.getDescription());
    						String pmsDocumentURL = context.getPmsDocumentURL();
    						pmsDocumentURL = pmsDocumentURL.replace("#", documentDetailsValue.getUrlId());
    						pmsDocumentURL = pmsDocumentURL.replace("+", documentDetailsValue.getEcmUrl());
    						pmsDocumentURL = pmsDocumentURL.replace("*", documentDetailsValue.getDocName());
    						pmsDocumentURL = pmsDocumentURL.replace("$", documentDetailsValue.getServerId());
    						docVal.setFileViewURL(pmsDocumentURL);
    						//docVal.setDmsDocToken(documentDetailsValue.getUrlId());
    						docVal.setDmsRestApiURL(dmsAPIUrl);
    						docSearch.add(docVal);
    						//}
    					}

    					hospitalTariff = getHospitalTariffUrl(hospitalCode);
    					if(hospitalTariff != null){
    						hospitalTariff.setDocumentType(SHAConstants.HOSPITAL_TARIFF);
    						hospitalTariff.setFileName("Room Tariff");
    					}
    				}
    				if(hospitalTariff != null){
    					docSearch.add(hospitalTariff);
    				}
                }catch(Exception e){
                	e.printStackTrace();
                }

			return docSearch;

		}
	
	public static DMSDocumentDetailsDTO getHospitalDiscounttUrl(String hospitalCode) {
		
			DMSDocumentDetailsDTO docSearch = new DMSDocumentDetailsDTO();
		
                try{
                	
    				ClientConfig clientConfig = new DefaultClientConfig();
    				clientConfig.getClasses().add(JacksonJsonProvider.class);
    				
    				Client client = Client.create(clientConfig);
    				String strPremiaFlag = BPMClientContext.HOSP_DISCOUNT_URL+hospitalCode;
//    				String strPremiaFlag = "http://192.168.1.96/shipms/discountPremiaUrl.action?hosCode=HOS-5420#b";
    				
    				BPMClientContext context = new BPMClientContext();
    				String dmsAPIUrl = context.getDMSRestApiUrl();
    				docSearch.setHospitalCode(hospitalCode);
    				docSearch.setDocumentType(SHAConstants.HOSPITAL_DISCOUNT);
    				docSearch.setHosiptalDiscount(strPremiaFlag);
    					
                	
                }catch(Exception e){
                	e.printStackTrace();
                }

			return docSearch;

		}
	
	
    public static Builder get64VBChequeStatusDetails(){
    	
    	Builder builder = null;
		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag + "Check64VBStatus");
			webResource.accept("application/json");
			
			builder =  webResource.type("application/json");
			builder.accept("application/json");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return builder;
    	
    }
			public Builder getLockBuilder(){
			
				ClientConfig clientConfig = new DefaultClientConfig();
				
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResourceForLock = client.resource(strPremiaFlag + "LockPolicy");
//				WebResource webResourceForLock = client.resource("http://192.168.1.237:9410/GalaxyClaims/ClaimsService.svc/REST/LockPolicy");
				webResourceForLock.accept("application/json");
				Builder builder = webResourceForLock.type("application/json");
				builder.accept("application/json");
				
				return builder;
			}
				
			public Builder getUnLockBuilder(){

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResourceForUnLock = client.resource(strPremiaFlag + "UnLockPolicy");
//				WebResource webResourceForUnLock = client.resource("http://192.168.1.237:9410/GalaxyClaims/ClaimsService.svc/REST/UnLockPolicy");
				webResourceForUnLock.accept("application/json");
				Builder builder = webResourceForUnLock.type("application/json");
				builder.accept("application/json");
				return builder;
			}
			
			public Boolean getPolicyLock(Claim claim, String hospitalCode) {
				try {
		
					TempRestDTO policy = new TempRestDTO();
					policy.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber());
					policy.setClaimNo(claim.getClaimId());
					policy.setPolicyEndNo(claim.getIntimation().getPolicy().getEndorsementNumber() != null ? claim.getIntimation().getPolicy().getEndorsementNumber() :"");
					policy.setIntimationNo(claim.getIntimation().getIntimationId());
					policy.setHealthIDCard(claim.getIntimation().getInsured().getHealthCardNumber());
					policy.setRiskSysID(String.valueOf(claim.getIntimation().getInsured().getInsuredId()));
					policy.setReasonForAdmission(claim.getIntimation().getAdmissionReason());
					policy.setAdmissionDate(SHAUtils.formatDate(claim.getIntimation().getAdmissionDate()));
					policy.setHospitalCode(hospitalCode);
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					String post = null;
					
					if (claim.getIntimation().getPolicy().getPolicyNumber() != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(claim.getIntimation().getPolicy().getPolicyNumber());
						if (policyObj != null) {
							 if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								post = BancsSevice.getInstance().lockPolicyUpadte(claim,SHAConstants.BANCS_LOCK);
							}else{
								builder = PremiaService.getInstance().getLockBuilder();
								post = builder.post(String.class, policy);
							}
						}
					}
					
					//Bancs Changes End
					//Builder builder = getLockBuilder();
					//String post = builder.post(String.class, policy);
					
					log.info("-----PREMIA SERVICE------- POLICY LOCK -- INTIMATION NO --> " + claim.getIntimation().getIntimationId() + "  POLICY NO --> " + claim.getIntimation().getPolicy().getPolicyNumber() + "  RESULT FROM PREMIA END -->" + post);
					
					return true;
				} catch (Exception e) {
					log.error("-----PREMIA SERVICE------- POLICY LOCK -- INTIMATION NO --> " + claim.getIntimation().getIntimationId() + "  POLICY NO --> " + claim.getIntimation().getPolicy().getPolicyNumber() + "  RESULT FROM PREMIA END --> Not Working -- Exception is " + e.getMessage());
					e.printStackTrace();
				}
					
					return false;
			}
				
			
			public Boolean UnLockPolicy(String intimationNumber) {
				try {
					
					//Bancs Changes Start
					IntimationDto policyObj = null;
					Builder builder = null;
					String post = null;
					
					if (intimationNumber != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getIntimationObject(intimationNumber);
						if (policyObj != null) {
							 if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								post = BancsSevice.getInstance().unLockPolicyUpadte(policyObj,SHAConstants.BANCS_UN_LOCK);
							}else{
								builder =PremiaService.getInstance().getUnLockBuilder();
								post = builder.post(String.class, "\""+ intimationNumber +"\"");
							}
						}
					}
					
					//Bancs Changes End
					//Builder builder = getUnLockBuilder();
					//String post = builder.post(String.class, "\""+ policyNumber +"\"");
					log.info("-----PREMIA SERVICE------- POLICY UNLOCK -- INTIMATION NO --> " + intimationNumber + "  RESULT FROM PREMIA END -->" + post);
					return true;
			    } catch (Exception e) {
			    	log.error("-----PREMIA SERVICE------- POLICY UNLOCK -- INTIMATION NO --> " + intimationNumber + "  RESULT FROM PREMIA END --> Exception is --> " + e.getMessage());
					e.printStackTrace();

				}
				return false;
			}
			
			public String get64VBStatus(String policyNumber, String intimationNumber) {
				try {
					
					DBCalculationService dbService = new DBCalculationService();
					String status = new String();
					
					Unique64VbstatusDto dto = dbService.getUnique64Details(intimationNumber);
					
					if(dto != null){
						
						List<PremUnique64VBStatusDetails> chequeStatusDetails = null;
						PremUnique64VBStatusDetails premUnique64VBStatusDetail = null;
						Integer installNo = 0;
						//Bancs Changes Start
						Policy policyObj = null;
						Builder uniqueBuilder = null;
						
						if (policyNumber != null) {
							policyObj = dbService.getPolicyObject(policyNumber);
							//policyObj = policyService.getByPolicyNumber(policyNumber);
							if (policyObj != null) {
								if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									chequeStatusDetails = Check64VBService.getInstance().viewGetUnique64VB(policyNumber);
								}else{
									uniqueBuilder = getUniquePolicy64VBStatusDetails();
									chequeStatusDetails= uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");
								}
							}else{
								uniqueBuilder = getUniquePolicy64VBStatusDetails();
								chequeStatusDetails= uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");
							}
						}
						
						//Bancs Changes End
						//Builder uniqueBuilder = getUniquePolicy64VBStatusDetails();
						//chequeStatusDetails= uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");
						
						Date policyFromDate = dto.getPolicyFromDate();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(policyFromDate);
						calendar.add(Calendar.YEAR, 1);
					    Date afterOneYear = calendar.getTime();
					    
					    Date policyToDate = dto.getPolicyToDate();
					    
					    if(dto.getAdmissionDate() != null && dto.getAdmissionDate().before(afterOneYear)){
					    	installNo = 1;
					    }
					    else if(dto.getAdmissionDate() != null && dto.getAdmissionDate().after(afterOneYear) && dto.getAdmissionDate().before(policyToDate)){
					    	installNo = 2;
					    }
						
					    for (PremUnique64VBStatusDetails premUnique64VBStatusDetails : chequeStatusDetails) {
							if(premUnique64VBStatusDetails.getInstallmentNo().equalsIgnoreCase(installNo.toString())){
								premUnique64VBStatusDetail = premUnique64VBStatusDetails;
								break;
							}
						}
					    
					    if(premUnique64VBStatusDetail != null && premUnique64VBStatusDetail.getInstallmentStatus() != null){
					    		status = premUnique64VBStatusDetail.getInstallmentStatus();
					    }
					    
					    return status;
						
						
					}else{
						
						/*Long productKey = dbService.getProductKey(policyNumber);
						log.info("********PREMIA SERVICE *******64 VB STATUS -******** RESULT FROM PREMIA END FOR PRODUCT KEY -->  "+productKey);
						if(productKey != null && productKey.equals(ReferenceTable.GMC_PRODUCT_KEY)){
							log.info("********PREMIA SERVICE *******64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END FOR GMC -->  "+productKey);
							return "R";
						}else{*/
						
						//Bancs Changes Start
						Policy policyObj = null;
						Builder builder = null;
						String post = null;
						
						if (policyNumber != null) {
							policyObj = dbService.getPolicyObject(policyNumber);
							//policyObj = policyService.getByPolicyNumber(policyNumber);
							if (policyObj != null) {
								if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									post = Check64VBService.getInstance().view64vbStatus(policyNumber);
								}else{
									builder = get64VBChequeStatusDetails();
									post = builder.post(String.class, "\""+ policyNumber +"\"");
								}
							}else{
								builder = get64VBChequeStatusDetails();
								post = builder.post(String.class, "\""+ policyNumber +"\"");
							}
						}
						
						//Bancs Changes End
						//	Builder builder = get64VBChequeStatusDetails();
						//String post = builder.post(String.class, "\""+ policyNumber +"\"");
							String replace = post != null?post.replace("\"", ""):"";
							log.info("********PREMIA SERVICE *******64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + replace);
							return replace.equalsIgnoreCase("P") ? "P" : replace;
						//}
					}
				   
//					return "R";
				} catch (Exception e) {
					log.error("********PREMIA SERVICE *******64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + e.getMessage());
					e.printStackTrace();
				}
					return null;

					
			}
			
			public String get64VBStatusForView(String policyNumber, String intimationNumber) {
				try {
					String status = new String();
					
					DBCalculationService dbService = new DBCalculationService();
					
					Unique64VbstatusDto dto = dbService.getUnique64Details(intimationNumber);
					
					if(dto != null){

						List<PremUnique64VBStatusDetails> chequeStatusDetails = null;
						PremUnique64VBStatusDetails premUnique64VBStatusDetail = null;
						Integer installNo = 0;
						//Bancs Changes Start
						Policy policyObj = null;
						Builder uniqueBuilder = null;
						
						if (policyNumber != null) {
							policyObj = dbService.getPolicyObject(policyNumber);
							//policyObj = policyService.getByPolicyNumber(policyNumber);
							if (policyObj != null) {
								if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									chequeStatusDetails = Check64VBService.getInstance().viewGetUnique64VB(policyNumber);
								}else{
									uniqueBuilder = getUniquePolicy64VBStatusDetails();
									chequeStatusDetails = uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");
								}
							}else{
								uniqueBuilder = getUniquePolicy64VBStatusDetails();
								chequeStatusDetails = uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");
							}
						}
						
						//Bancs Changes End
						//Builder uniqueBuilder = getUniquePolicy64VBStatusDetails();
						//chequeStatusDetails= uniqueBuilder.post(new GenericType<List<PremUnique64VBStatusDetails>>() {}, "\""+policyNumber+ "\"");

						Date policyFromDate = dto.getPolicyFromDate();
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(policyFromDate);
						calendar.add(Calendar.YEAR, 1);
						Date afterOneYear = calendar.getTime();

						Date policyToDate = dto.getPolicyToDate();

						if(dto.getAdmissionDate() != null && dto.getAdmissionDate().before(afterOneYear)){
							installNo = 1;
						}
						else if(dto.getAdmissionDate() != null && dto.getAdmissionDate().after(afterOneYear) && dto.getAdmissionDate().before(policyToDate)){
							installNo = 2;
						}

						for (PremUnique64VBStatusDetails premUnique64VBStatusDetails : chequeStatusDetails) {
							if(premUnique64VBStatusDetails.getInstallmentNo().equalsIgnoreCase(installNo.toString())){
								premUnique64VBStatusDetail = premUnique64VBStatusDetails;
								break;
							}
						}

						if(premUnique64VBStatusDetail != null && premUnique64VBStatusDetail.getInstallmentStatus() != null){
								status = premUnique64VBStatusDetail.getInstallmentStatus();
						}

						return status;
					}

					else{
						//Bancs Changes Start
						Policy policyObj = null;
						Builder builder = null;
						String post = null;
						
						if (policyNumber != null) {
							policyObj = dbService.getPolicyObject(policyNumber);
							//policyObj = policyService.getByPolicyNumber(policyNumber);
							if (policyObj != null) {
								if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
									post = Check64VBService.getInstance().view64vbStatus(policyNumber);
								}else{
									builder = get64VBChequeStatusDetails();
									post = builder.post(String.class, "\""+ policyNumber +"\"");
								}
							}else{
								builder = get64VBChequeStatusDetails();
								post = builder.post(String.class, "\""+ policyNumber +"\"");
							}
						}
						
						//Bancs Changes End
						//Builder builder = get64VBChequeStatusDetails();
						//String post = builder.post(String.class, "\""+ policyNumber +"\"");
						String replace = post != null ?post.replace("\"", ""):"";
						log.info("********PREMIA SERVICE *******64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + replace);
						return replace;
					}
					//					return "R";
				} catch (Exception e) {
					log.error("********PREMIA SERVICE *******64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + e.getMessage());
					e.printStackTrace();
				}
				return null;


			}
			
			public Boolean updateProvisionAmount(String input) {
				try {
					
					//Bancs Changes Start
					JSONObject jsonObject = new JSONObject(input);
					String policyNo = jsonObject.getString("PolicyNo");
					String intimationNo = jsonObject.getString("IntimationNo");
					String currentProvisionAmount = jsonObject.getString("ProvisionAmount");
					Policy policyObj = null;
					Builder builder = null;
					String output = null;
					
					if(policyNo != null){
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(policyNo);
						//policyObj = policyService.getByPolicyNumber(policyNo);
						if (policyObj != null) {
							if(policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)){
								output = BancsSevice.getInstance().provisionAmountBancsUpdate(policyNo,intimationNo, currentProvisionAmount, input);
							}else{
								builder = getBuilderForProvison();
								output = builder.post(new GenericType<String>() {}, input);
							}
						}
						
					}
					
					//Bancs Changes End
					//Builder builder = getBuilderForProvison();
					
					log.info("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END -->" + output);
					return true;
			    } catch (Exception e) {
			    	log.error("********PREMIA SERVICE *******PROVISION UPDATE -*********- INPUTS --> " + input + "  RESULT FROM PREMIA END -->" + e.getMessage());
					e.printStackTrace();

				}
				return false;
			}
			
			public Builder getBedsBuilder(){
				
				ClientConfig clientConfig = new DefaultClientConfig();
				
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);
				WebResource webResourceForLock = client.resource(BPMClientContext.BEDS_COUNT);
				webResourceForLock.accept("application/json");
				Builder builder = webResourceForLock.type("application/json");
				builder.accept("application/json");
				
				return builder;
			}
			
			public Boolean updateBedsCount(String bedsCount, String hospitalCode) {
				try {
					Builder builder = getBedsBuilder();
					String output = builder.post(new GenericType<String>() {}, SHAUtils.getBedsInput(bedsCount, hospitalCode));
					log.info("********PREMIA SERVICE *******BED COUNT UPDATE -*********- bed Count --> " + bedsCount + " Hospital Code --> " + hospitalCode +  "  RESULT FROM PREMIA END -->" + output);
					return true;
			    } catch (Exception e) {
			    	log.error("********PREMIA SERVICE *******BED COUNT UPDATE -*********- bed Count --> " + bedsCount + " Hospital Code --> " + hospitalCode +  "  RESULT FROM PREMIA END -->" + e.getMessage());
					e.printStackTrace();

				}
				return false;
			}
			
			public Integer getUniqueInstallmentAmount(String policyNumber) {
				try {
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					String post = null;
					
					if (policyNumber != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(policyNumber);
						//policyObj = policyService.getByPolicyNumber(policyNumber);
						if (policyObj != null) {
							if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								//builder = BancsSevice.getInstance().getBuilder(PremiaConstants.UNIQUE_INSTALLMENT_AMOUNT);
								post = ClaimProvisionService.getInstance().callUniqueInstallmentAmountService(policyNumber);
							}else{
								builder = PremiaConstants.getBuilder(PremiaConstants.UNIQUE_INSTALLMENT_AMOUNT);
								post = builder.post(String.class, "\""+ policyNumber +"\"");
							}
						}else{
							builder = PremiaConstants.getBuilder(PremiaConstants.UNIQUE_INSTALLMENT_AMOUNT);
							post = builder.post(String.class, "\""+ policyNumber +"\"");
						}
					}
					
					//Bancs Changes End
					// Builder builder = PremiaConstants.getBuilder(PremiaConstants.UNIQUE_INSTALLMENT_AMOUNT);
					 
					//	String post = builder.post(String.class, "\""+ policyNumber +"\"");
					System.out.println("----UNIQUE INSTALLMENT AMOUNT----"+ post);
					JSONObject json = new JSONObject(post);
					String  amount = (String) json.get("Result");
					log.info("********PREMIA SERVICE *******UNIQUE INSTALLMENT AMOUNT-*********- POLCIY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + amount);
					return SHAUtils.getIntegerFromStringWithComma(amount);
				} catch (Exception e) {
					log.error("********PREMIA SERVICE *******UNIQUE INSTALLMENT AMOUNT-*********- POLCIY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END --> Exception is" + e.getMessage());
					e.printStackTrace();
				}
				return 0;
			}
			
			public Integer updateAdjustmentAmount(String jsonString) {
				try {
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					JSONObject jsonObject = new JSONObject(jsonString);
					String policyNo = jsonObject.getString("PolicyNo");
					String post = null;
					
					if (policyNo != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(policyNo);
						//policyObj = policyService.getByPolicyNumber(policyNo);
						if (policyObj != null) {
							 if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								//builder = BancsSevice.getInstance().getBuilder(PremiaConstants.ADJUST_UNIQUE_INSTALLMENT_AMOUNT);
							}else{
//								if(policyObj.getProduct() != null && policyObj.getProduct().getKey().equals(ReferenceTable.STAR_UNIQUE_PRODUCT_KEY)){
									builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_UNIQUE_INSTALLMENT_AMOUNT);
//								}else{
//									builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_POLICY_INSTALLMENT);
//								}
								post = builder.post(String.class, jsonString);
							}
						}
					}
					
					//Bancs Changes End
				   // Builder builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_UNIQUE_INSTALLMENT_AMOUNT);
					JSONObject json = new JSONObject(post);
					String  amount = (String) json.get("Result");
					log.info("********PREMIA SERVICE *******UNIQUE ADJUSTED AMOUNT-*********- INPUT  --> " + jsonString + "  RESULT FROM PREMIA END -->" + amount);
					return SHAUtils.getIntegerFromStringWithComma(amount);
				} catch (Exception e) {
					log.error("********PREMIA SERVICE *******UNIQUE ADJUSTED AMOUNT-*********- INPUT  --> " + jsonString + "  RESULT FROM PREMIA END -->" + e.getMessage());
					e.printStackTrace();
				}
				return 0;
			}
			
			public Boolean getEndorsedPolicyStatus(String policyNumber) {
				try {
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					String post = null;
					
					if (policyNumber != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(policyNumber);
						//policyObj = policyService.getByPolicyNumber(policyNumber);
						if (policyObj != null) {
							if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								PolicyLockUnLockResponse response = CheckEndorsementStatusService.getInstance().viewEndoresementStatus(policyNumber);
								post = response.getStatus();
							}else{
								builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_STATUS);
								post = builder.post(String.class, "\""+ policyNumber +"\"");
							}
						}else{
							builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_STATUS);
							post = builder.post(String.class, "\""+ policyNumber +"\"");
						}
					}
					
					//Bancs Changes End
				   // Builder builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_STATUS);
					//String post = builder.post(String.class, "\""+ policyNumber +"\"");
					String replace = post != null? post.replace("\"", ""):"";
					log.info("********PREMIA SERVICE ******* POLICY STATUS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + replace);
					return (post != null && replace.equalsIgnoreCase("C")) ? true : false;
				} catch (Exception e) {
					log.info("********PREMIA SERVICE ******* POLICY STATUS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->  Exception is - " + e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
			
			
			public PremPolicyDetails getPolicyDetailsFromPremia(String policyNumber) {
				try {
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					PremPolicyDetails policyDetails = null;
					
					if (policyNumber != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(policyNumber);
						//policyObj = policyService.getByPolicyNumber(policyNumber);
						if (policyObj != null) {
							if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								//builder = BancsSevice.getInstance().getBuilder(PremiaConstants.POLICY_DETAILS);
							}else{
								builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_DETAILS);
							    policyDetails = builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policyNumber+ "\"");
							}
						}else{
							builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_DETAILS);
						    policyDetails = builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policyNumber+ "\"");
						}
					}
					
					//Bancs Changes End
				   // Builder builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_DETAILS);
					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + policyDetails);
					return policyDetails;
				} catch (Exception e) {
					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->  Exception is - " + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
			
//			public List<PortablitiyPolicyDTO> getPortabilityPolicyDetailsFromPremia(String policyNumber,Long insuredId) {
//				List<PortablitiyPolicyDTO> resultList = new ArrayList<PortablitiyPolicyDTO>();
//				try {
//					PortablitiyPolicyDTO portabilityPolDto = new PortablitiyPolicyDTO(); 
//				    Builder builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_DETAILS);
//				    PremPolicyDetails policyDetails = builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policyNumber+ "\"");
//					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + policyDetails);
//					
//					List<PremInsuredDetails> insuredDetails = policyDetails.getInsuredDetails();
//					
//					if(insuredDetails != null && !insuredDetails.isEmpty()){
//						for (PremInsuredDetails premInsuredDetails : insuredDetails) {
//							
//							if(premInsuredDetails.getRiskSysId().equalsIgnoreCase(insuredId != null ? insuredId.toString() : "")){
//								List<PremPortabilityPrevPolicyDetails> prevPortabilityPolicyDetails = premInsuredDetails.getPrevPortabilityPolicyDetails();
//								if(prevPortabilityPolicyDetails != null && !prevPortabilityPolicyDetails.isEmpty())
//								
//								
//								List<PremPortability> portablitityDetails = premInsuredDetails.getPortablitityDetails();
//								
//							}
//						}
//					}
//					
//					portabilityPolDto.setPolicyNo(policyDetails.getPolicyNo());
////					portabilityPolDto.setPolicyStartDate(policyDetails.getPolicyStartDate());
//					portabilityPolDto.setPolicyFrmDate(policyDetails.getPolicyStartDate());
//					portabilityPolDto.setPolicyToDate(policyDetails.getPolicyEndDate());
//					portabilityPolDto.setPolicyType(policyDetails.getPolicyType());
//					portabilityPolDto.setUnderwritingYear(policyDetails.getPolicyYr());
//					resultList.add(portabilityPolDto);
////					return portabilityPolDto;
//					return resultList;
//				} catch (Exception exception) {
//					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->  Exception is - " + exception.getMessage());
//					exception.printStackTrace();
//				}
//				return null;
//			}
			
			public Boolean getOMPPolicyLock(OMPClaim claim, String hospitalCode) {
				try {
			
					TempRestDTO policy = new TempRestDTO();
					policy.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber());
					policy.setClaimNo(claim.getClaimId());
					policy.setPolicyEndNo(claim.getIntimation().getPolicy().getEndorsementNumber() != null ? claim.getIntimation().getPolicy().getEndorsementNumber() :"");
					policy.setIntimationNo(claim.getIntimation().getIntimationId());
					policy.setHealthIDCard(claim.getIntimation().getInsured().getHealthCardNumber());
					policy.setRiskSysID(String.valueOf(claim.getIntimation().getInsured().getInsuredId()));
//					policy.setReasonForAdmission(claim.getIntimation().getAdmissionReason());
					policy.setAdmissionDate(SHAUtils.formatDate(claim.getIntimation().getAdmissionDate()));
					policy.setHospitalCode(hospitalCode);
					
					//Bancs Changes Start
					Policy policyObj = null;
					Builder builder = null;
					String post = null;
					
					if (claim.getIntimation().getPolicy().getPolicyNumber() != null) {
						DBCalculationService dbService = new DBCalculationService();
						policyObj = dbService.getPolicyObject(claim.getIntimation().getPolicy().getPolicyNumber());
						if (policyObj != null) {
							if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
								post = BancsSevice.getInstance().OMPLockPolicyUpadte(claim,SHAConstants.BANCS_LOCK);
							}else{
								builder = PremiaService.getInstance().getLockBuilder();
								post = builder.post(String.class, policy);
							}
						}
					}
					
					//Bancs Changes End
					//Builder builder = getLockBuilder();
					//String post = builder.post(String.class, policy);
					
					log.info("-----PREMIA SERVICE------- POLICY LOCK -- INTIMATION NO --> " + claim.getIntimation().getIntimationId() + "  POLICY NO --> " + claim.getIntimation().getPolicy().getPolicyNumber() + "  RESULT FROM PREMIA END -->" + post);
					
					return true;
				} catch (Exception e) {
					log.error("-----PREMIA SERVICE------- POLICY LOCK -- INTIMATION NO --> " + claim.getIntimation().getIntimationId() + "  POLICY NO --> " + claim.getIntimation().getPolicy().getPolicyNumber() + "  RESULT FROM PREMIA END --> Not Working -- Exception is " + e.getMessage());
					e.printStackTrace();
				}
					
					return false;
			}
			
			
			public static Builder getPolicyScheduleDetail() {
				//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
					Builder builder = null;
					try {

						ClientConfig clientConfig = new DefaultClientConfig();
						clientConfig.getClasses().add(JacksonJsonProvider.class);
						
						Client client = Client.create(clientConfig);
						String strPremiaFlag = BPMClientContext.PREMIA_URL;
						WebResource webResource = client.resource(strPremiaFlag + "GetPolicySchURL");

						webResource.accept("application/json");
						
						builder =  webResource.type("application/json");
						builder.accept("application/json");

					} catch (Exception e) {
						e.printStackTrace();

					}
					return builder;

				}
			

			public static Builder getOMPPolicyDetail() {
					Builder builder = null;
					try {

						ClientConfig clientConfig = new DefaultClientConfig();
						clientConfig.getClasses().add(JacksonJsonProvider.class);
						
						Client client = Client.create(clientConfig);
						String strPremiaFlag = BPMClientContext.PREMIA_URL;
						WebResource webResource = client.resource(strPremiaFlag + "GetOMPPolicyDetail");


						webResource.accept("application/json");
						
						builder =  webResource.type("application/json");
						builder.accept("application/json");

					} catch (Exception e) {
						e.printStackTrace();

					}
					return builder;
				}	

		public static Builder getGmcPolicyDetails() {
		//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
			Builder builder = null;
			try {
	
				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetGMCPolicyDetail");
	
				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");
	
			} catch (Exception e) {
				e.printStackTrace();
	
			}
			return builder;
	
			}

		public static String getUploadFile(String url,File fileToUpload) {
			String result = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				//clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				//String strPremiaFlag = url;
				WebResource webResource = client.resource(url);
				 

				webResource.accept(MediaType.MULTIPART_FORM_DATA);
				
				FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
		                fileToUpload,
		                MediaType.APPLICATION_OCTET_STREAM_TYPE);
		        fileDataBodyPart.setContentDisposition(
		                FormDataContentDisposition.name("uploadFile")
		                        .fileName(fileToUpload.getName()).build());
			    
			   /* final JSONObject jsonToSend = new JSONObject();
		        jsonToSend.put("character", "Jabba the Hutt");
		        jsonToSend.put("movie", "Return of the Jedi");
		        jsonToSend.put("isGoodGuy", false);*/

		        /* create the MultiPartRequest with:
		         * Text field called "description"
		         * JSON field called "characterProfile"
		         * Text field called "filename"
		         * Binary body part called "file" using fileDataBodyPart
		         */
		        final MultiPart multiPart = new FormDataMultiPart()
		                .field("description", "Picture of Jabba the Hutt", MediaType.TEXT_PLAIN_TYPE)
		                //.field("characterProfile", jsonToSend, MediaType.APPLICATION_JSON_TYPE)
		                .field("filename", fileToUpload.getName(), MediaType.TEXT_PLAIN_TYPE)
		                .bodyPart(fileDataBodyPart);
		        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
			    
			    ClientResponse response = webResource
		                .type("multipart/form-data").post(ClientResponse.class,
		                        multiPart);
			    result = getStringFromInputStream(response.getEntityInputStream());
			    System.out.println("INFO >>> Response from API was: " + result);
		        client.destroy();
			} catch (Exception e) {
				e.printStackTrace();

			}
			return result;
		}	
		
		private static String getStringFromInputStream(InputStream is) {
	        BufferedReader br = null;
	        final StringBuilder sb = new StringBuilder();
	        String line;
	        try {
	            br = new BufferedReader(new InputStreamReader(is));
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return sb.toString();
	    }

		public static DMSDocumentDetailsDTO getHospitalTariffUrl(String hospitalCode) {
			
			DMSDocumentDetailsDTO docSearch = new DMSDocumentDetailsDTO();
		
                try{
                	
    				ClientConfig clientConfig = new DefaultClientConfig();
    				clientConfig.getClasses().add(JacksonJsonProvider.class);
    				
    				Client client = Client.create(clientConfig);
    				String strPremiaFlag = BPMClientContext.HOSP_TARIFF_URL+hospitalCode;
//    				String strPremiaFlag = "http://192.168.1.96/shipms/discountPremiaUrl.action?hosCode=HOS-5420#b";
    				
    				BPMClientContext context = new BPMClientContext();
    				String dmsAPIUrl = context.getDMSRestApiUrl();
    				docSearch.setHospitalCode(hospitalCode);
    				docSearch.setDocumentType(SHAConstants.HOSPITAL_TARIFF);
    				docSearch.setHosiptalDiscount(strPremiaFlag);
    					
                	
                }catch(Exception e){
                	e.printStackTrace();
                }

			return docSearch;

		}
		//CR2017275
		
		public static Builder getUniquePolicy64VBStatusDetails(){
	    	
	    	Builder builder = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetUnique64VBStatus");
				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;
	    	
	    }
		
		
		public static Builder getInsertHospitalBuilder(){
			
			ClientConfig clientConfig = new DefaultClientConfig();
			
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			Client client = Client.create(clientConfig);
			WebResource webResourceForLock = client.resource(BPMClientContext.INSERT_NON_NETWORK_HOSP);
			webResourceForLock.accept("application/json");
			Builder builder = webResourceForLock.type("application/json");
			builder.accept("application/json");
			
			return builder;
		}
		
		public static void insertNonNetworkHospital(InsertHospitalToPMS hospitalDto){
			try{
				
			Builder insertHospitalBuilder = getInsertHospitalBuilder();
			 Gson gson = new Gson();
		     String parameter = gson.toJson(hospitalDto);
			insertHospitalBuilder.post(hospitalDto);
			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public static Builder getHealthCardViewBuilder(){
			
			ClientConfig clientConfig = new DefaultClientConfig();
			
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			Client client = Client.create(clientConfig);
			WebResource webResourceForLock = client.resource(BPMClientContext.HEALTH_CARD_NUMBER_URL);
			webResourceForLock.accept("application/json");
			Builder builder = webResourceForLock.type("application/json");
			builder.accept("application/json");
			
			return builder;
		}
		
		public static String getHealthCardViewDetails(String healthCardNumber) {
			try{
			    Builder builder = getHealthCardViewBuilder();
				String post = builder.post(String.class, "\""+ healthCardNumber +"\"");
				String replace = post.replaceAll("\"", "");
				replace = replace.replace("\\", "");
				return replace;
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		
		public  PremBonusDetails getBonusDetails(String policyNumber) {			
			try{
				//Bancs Changes Start
				Policy policyObj = null;
				Builder builder = null;
				PremBonusDetails policyDetails = null;

				if (policyNumber != null) {
					DBCalculationService dbService = new DBCalculationService();
					policyObj = dbService.getPolicyObject(policyNumber);
					//policyObj = policyService.getByPolicyNumber(policyNumber);
					if (policyObj != null) {
						if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
							//builder = BancsSevice.getInstance().getBonusDetailsBuilder();
						}else{
							builder = getBonusDetailsBuilder();
							policyDetails = builder.post(new GenericType<PremBonusDetails>() {}, "\""+policyNumber+ "\"");
						}
					}else{
						builder = getBonusDetailsBuilder();
						policyDetails = builder.post(new GenericType<PremBonusDetails>() {}, "\""+policyNumber+ "\"");
					}
				}
				
				//Bancs Changes End
				//Builder builder = getBonusDetailsBuilder();
					log.info("********PREMIA SERVICE ******* POLICY BONUS DETAILS DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + policyDetails);
					return policyDetails;
				} catch (Exception e) {
					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->  Exception is - " + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
public static Builder getBonusDetailsBuilder(){
	    	
	    	Builder builder = null;
			try {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				
				Client client = Client.create(clientConfig);
				String strPremiaFlag = BPMClientContext.PREMIA_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetBonusDetails");
				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;
	    	
	    }
public String GetInstallment64VBStatus(String policyNumber) {
	try {
		
		DBCalculationService dbService = new DBCalculationService();
		String status = new String();
		
		
		if(policyNumber != null){

			Policy policyObj = null;
			Builder builder = null;
			String post = null;
			
			List<GetInstallment64VBStatusResponse> getInstallment64VBStatusDetails = null;
			GetInstallment64VBStatusResponse getInstallment64VBStatusDetail = null;
			String bancsInstalmentFlag = "N";
	        BPMClientContext bpmClientContext = new BPMClientContext();
	        bancsInstalmentFlag = bpmClientContext.getBancsInstalmentFlag();
	        
			if (policyNumber != null) {
				policyObj = dbService.getPolicyObject(policyNumber);
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
							&& bancsInstalmentFlag != null && bancsInstalmentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						getInstallment64VBStatusDetails = BancsSevice.getInstance().getInstallment64VBStatus(policyNumber);
					}else{
						builder = getInstallment64VBChequeStatusDetails();
						getInstallment64VBStatusDetails = builder.post(new GenericType<List<GetInstallment64VBStatusResponse>>() {}, "\""+policyNumber+ "\"");
					}
				}else{
					builder = getInstallment64VBChequeStatusDetails();
					getInstallment64VBStatusDetails = builder.post(new GenericType<List<GetInstallment64VBStatusResponse>>() {}, "\""+policyNumber+ "\"");
				}
				for (GetInstallment64VBStatusResponse response : getInstallment64VBStatusDetails) {
					if(response.getInstallmentNo() !=null && !response.getInstallmentNo().equals("")){
						getInstallment64VBStatusDetail = response;
						break;
					}
				}
			    
			    if(getInstallment64VBStatusDetail != null && getInstallment64VBStatusDetail.getInstallmentStatus() != null){
			    		status = getInstallment64VBStatusDetail.getInstallmentStatus();
			    }
			}

				//String replace = post != null?post.replace("\"", ""):"";
				log.info("********PREMIA SERVICE *******INSTALLMENT 64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + status);
				//return replace.equalsIgnoreCase("P") ? "P" : replace;
				return status;
			
		}
	   
	} catch (Exception e) {
		log.error("********PREMIA SERVICE *******INSTALLMENT 64 VB STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + e.getMessage());
		e.printStackTrace();
	}
		return null;

		
}

public static Builder getInstallment64VBChequeStatusDetails(){
	
	Builder builder = null;
	try {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		
		Client client = Client.create(clientConfig);
		String strPremiaFlag = BPMClientContext.PREMIA_URL;
		WebResource webResource = client.resource(strPremiaFlag + "GetInstallment64VBStatus");
		webResource.accept("application/json");
		
		builder =  webResource.type("application/json");
		builder.accept("application/json");

	} catch (Exception e) {
		e.printStackTrace();

	}
	return builder;
	
}

public Integer updateAdjustPolicyInstallment(String jsonString) {
	try {
		//Bancs Changes Start
		Policy policyObj = null;
		Builder builder = null;
		JSONObject jsonObject = new JSONObject(jsonString);
		String policyNo = jsonObject.getString("PolicyNo");
		String post = null;
		String  amount = "";
		String bancsInstalmentFlag = "N";
        BPMClientContext bpmClientContext = new BPMClientContext();
        bancsInstalmentFlag = bpmClientContext.getBancsInstalmentFlag();
		if (policyNo != null) {
			DBCalculationService dbService = new DBCalculationService();
			policyObj = dbService.getPolicyObject(policyNo);
			if (policyObj != null) {
				 if (policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
						 && bancsInstalmentFlag != null && bancsInstalmentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					 BancsAdjustPolicyInstallemtRequest adjustInstalmentRequest = new BancsAdjustPolicyInstallemtRequest();
					 adjustInstalmentRequest.setAdjustmentAmount(jsonObject.getString("AdjustmentAmount"));
					 adjustInstalmentRequest.setIntimationNo(jsonObject.getString("IntimationNo"));
					 adjustInstalmentRequest.setCpuCode(jsonObject.getString("CpuCode"));
					 adjustInstalmentRequest.setPolicyNumber(jsonObject.getString("PolicyNo"));
					 amount = BancsSevice.getInstance().getAdjustPolicyInstallment(adjustInstalmentRequest);
					 log.info("********BANCS SERVICE *******ADJUST POLICY INSTALLMENT AMOUNT-*********- INPUT  --> " + jsonString + "  RESULT FROM PREMIA END -->" + amount);
				}else{
					builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_POLICY_INSTALLMENT);
					post = builder.post(String.class, jsonString);
				}
			}else {
				builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_POLICY_INSTALLMENT);
				post = builder.post(String.class, jsonString);
			}
		}
		
		//Bancs Changes End
	   // Builder builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_UNIQUE_INSTALLMENT_AMOUNT);
		if (policyObj.getPolicySource() != null&& !policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
		JSONObject json = new JSONObject(post);
		  amount = (String) json.get("Result");
		}
		log.info("********PREMIA SERVICE *******ADJUST POLICY INSTALLMENT AMOUNT-*********- INPUT  --> " + jsonString + "  RESULT FROM PREMIA END -->" + amount);
		return SHAUtils.getIntegerFromStringWithComma(amount);
	} catch (Exception e) {
		log.error("********PREMIA SERVICE *******ADJUST POLICY INSTALLMENT AMOUNT-*********- INPUT  --> " + jsonString + "  RESULT FROM PREMIA END -->" + e.getMessage());
		e.printStackTrace();
	}
	return 0;
}

public Integer getPolicyInstallmentAmount(String policyNumber) {
	try {
		Policy policyObj = null;
		Builder builder = null;
		String post = null;
		String  amount = "";
		String bancsInstalmentFlag = "N";
        BPMClientContext bpmClientContext = new BPMClientContext();
        bancsInstalmentFlag = bpmClientContext.getBancsInstalmentFlag();
		if (policyNumber != null) {
			DBCalculationService dbService = new DBCalculationService();
			policyObj = dbService.getPolicyObject(policyNumber);
			if (policyObj != null) {
				if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
						&& bancsInstalmentFlag != null && bancsInstalmentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					amount = BancsSevice.getInstance().getPolicyInstAmountDetails(policyNumber); 
					System.out.println("----POLICY BANCS INSTALLMENT AMOUNT----"+ amount);
				}else{
					builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_INSTALLMENT_AMOUNT);
					post = builder.post(String.class, "\""+ policyNumber +"\"");
				}
			}else{
				builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_INSTALLMENT_AMOUNT);
				post = builder.post(String.class, "\""+ policyNumber +"\"");
			}
		}
		System.out.println("----POLICY INSTALLMENT AMOUNT----"+ post);
		System.out.println("----POLICY BANCS INSTALLMENT AMOUNT----"+ amount);
		if (policyObj.getPolicySource() != null&& !policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)) {
		JSONObject json = new JSONObject(post);
		  amount = (String) json.get("Result");
		}
		log.info("********PREMIA SERVICE *******POLICY INSTALLMENT AMOUNT-*********- POLCIY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + amount);
		return SHAUtils.getIntegerFromStringWithComma(amount);
	} catch (Exception e) {
		log.error("********PREMIA SERVICE *******POLICY INSTALLMENT AMOUNT-*********- POLCIY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END --> Exception is" + e.getMessage());
	}
	return 0;
}

public String getPolicyInstallmentDetails(String policyNumber) {
	try {
		
		DBCalculationService dbService = new DBCalculationService();
		String status = new String();
		
		
		if(policyNumber != null){

			Policy policyObj = null;
			Builder builder = null;
			
			List<PremPolicyInstallmentDetails> getPolicyInstallmentDetails = null;
			PremPolicyInstallmentDetails getPolicyInstallmentDetail = null;
			String bancsInstalmentFlag = "N";
            BPMClientContext bpmClientContext = new BPMClientContext();
            bancsInstalmentFlag = bpmClientContext.getBancsInstalmentFlag();
			if (policyNumber != null) {
				policyObj = dbService.getPolicyObject(policyNumber);
				if (policyObj != null) {
					if (policyObj.getPolicySource() != null&& policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
							&& bancsInstalmentFlag != null && bancsInstalmentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
						getPolicyInstallmentDetails = BancsSevice.getInstance().getPolicyInstallmentDetails(policyNumber);
					}else{
						builder = getPolicyInstalmentDetailsWS();
						getPolicyInstallmentDetails = builder.post(new GenericType<List<PremPolicyInstallmentDetails>>() {}, "\""+policyNumber+ "\"");
					}
				}else{
					builder = getPolicyInstalmentDetailsWS();
					getPolicyInstallmentDetails = builder.post(new GenericType<List<PremPolicyInstallmentDetails>>() {}, "\""+policyNumber+ "\"");
				}
				for (PremPolicyInstallmentDetails response : getPolicyInstallmentDetails) {
					if (policyObj.getPolicySource() != null && policyObj.getPolicySource().equalsIgnoreCase(SHAConstants.BANCS_POLICY)
							&& bancsInstalmentFlag != null && bancsInstalmentFlag.equalsIgnoreCase(SHAConstants.YES_FLAG)) {
					if(response.getInstalmentStatus() !=null &&  
							!(response.getInstalmentStatus().equalsIgnoreCase("RAISED AND PAID"))){
						getPolicyInstallmentDetail = response;
						log.info("********BANCS SERVICE *******POLICY INSTALLMENT DETAILS STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM BANCS END -->" + getPolicyInstallmentDetail.getInstalmentStatus());
						break;
					}
					}else{
						if(response.getInstalmentStatus() !=null && !(response.getInstalmentStatus().equalsIgnoreCase("CO"))){
							getPolicyInstallmentDetail = response;
							log.info("********PREMIA SERVICE *******POLICY INSTALLMENT DETAILS STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + getPolicyInstallmentDetail.getInstalmentStatus());
							break;
						}
					}
						
				}
			    
			    if(getPolicyInstallmentDetail != null && getPolicyInstallmentDetail.getInstallmentDueDate() != null){
			    		status = getPolicyInstallmentDetail.getInstallmentDueDate();
			    }
			}

				log.info("********PREMIA SERVICE *******pOLICY INSTALLMENT DETAILS STATUS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + status);
				return status;
			
		}
	   
	} catch (Exception e) {
		log.error("********PREMIA SERVICE *******POLICY INSTALLMENT DETAILS -*********- POLICY NO --> " + policyNumber + "  RESULT FROM PREMIA END -->" + e.getMessage());
		e.printStackTrace();
	}
		return null;

		
}

public static Builder getPolicyInstalmentDetailsWS(){

	Builder builder = null;
	try {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);

		Client client = Client.create(clientConfig);
		String strPremiaFlag = BPMClientContext.PREMIA_URL;
		WebResource webResource = client.resource(strPremiaFlag + "GetInstallmentDetails");
		webResource.accept("application/json");

		builder =  webResource.type("application/json");
		builder.accept("application/json");

	} catch (Exception e) {
		e.printStackTrace();

	}
	return builder;

}
public static Builder getPolicyScheduleWithoutRiskDetail() {
		Builder builder = null;
		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag + "GetPolicySchURL");

			webResource.accept("application/json");
			
			builder =  webResource.type("application/json");
			builder.accept("application/json");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return builder;

	}

 //GLX2020083 
public String getInsertBonusDetailsResult(String policyNumber) {
	String result ="Success - Records Inserted";
	String output = null;
	try {
		Builder builder = null;
		String post = null;

				if (policyNumber != null) {
					builder = PremiaConstants.getBuilder(PremiaConstants.Insert_Bonus_Details);
					post = builder.post(String.class, policyNumber);
				}
				if(post != null && post.equalsIgnoreCase(result))
				{
					output="Success";
				}
				else{
					output= "Failure";
				}
		log.info("********PREMIA SERVICE *******UNIQUE ADJUSTED AMOUNT-*********- INPUT  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + post);
	} catch (Exception e) {
		log.error("********PREMIA SERVICE *******UNIQUE ADJUSTED AMOUNT-*********- INPUT  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + e.getMessage());
		e.printStackTrace();
	}
	return output;
}
}