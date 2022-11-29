package com.shaic.claim.policy.search.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.shaic.arch.PremiaConstants;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.domain.Claim;
import com.shaic.domain.OMPClaim;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.BancsDBService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.ims.bpm.claim.IntimationDto;
import com.shaic.restservices.bancs.claimprovision.ClaimProvisionService;
import com.shaic.restservices.bancs.claimprovision.policydetail.PolicyDetailsRequest;
import com.shaic.restservices.bancs.lockPolicy.LockPolicyIntegrationDTO;
import com.shaic.restservices.bancs.lockPolicy.LockPolicyIntegrationService;
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

@Stateless
public class BancsSevice {
	
	private static BancsSevice instance = new BancsSevice();
	
	private final static Logger log = LoggerFactory.getLogger(BancsSevice.class);
	 
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	private BancsDBService searchService;
	 
	public static BancsSevice getInstance() {
		/*if(instance == null){
			instance = new BancsSevice();
		}*/
		return instance;
	}

	public BancsSevice() {
		
	}
	
	
	
	public static Builder getBuilder(String urlString) {
		Builder builder = null;
		try {
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.PREMIA_URL;
			WebResource webResource = client.resource(strPremiaFlag + urlString);
			webResource.accept("application/json");
			
			builder =  webResource.type("application/json");
			builder.accept("application/json");

		} catch (Exception e) {
			e.printStackTrace();

		}
		return builder;
	}
	
	public static Builder getPolicyDetail() {
	//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
		Builder builder = null;
		try {

			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			
			Client client = Client.create(clientConfig);
			String strPremiaFlag = BPMClientContext.BANCS_URL;
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetGPABenefitDetails");

				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;
	}
	
	public static List<BancsRenewedPolicyInformationResponse> getRenewedPolicyDetails(String policyNumber) {

		BancsDBService searchService = new BancsDBService();
		String jsonInString = "";
		List<BancsRenewedPolicyInformationResponse> response = new ArrayList<BancsRenewedPolicyInformationResponse>();
		BancsRenewedPolicyInformationResponse response1 = null;
		Gson gson = new Gson();
	
		try{
			String serviceURL = BPMClientContext.BANCS_GENERIC_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getRenewedPolicyInformation";
			}else{
				serviceURL = serviceURL+"getRenewedPolicyInformation";
			}
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			LockPolicyIntegrationDTO lpolicy=new LockPolicyIntegrationDTO();
			lpolicy.setServiceTransactionId(mapVal.get("tranid"));
			lpolicy.setBusinessChannel(mapVal.get("business"));
			lpolicy.setUserCode(mapVal.get("usercode"));
			lpolicy.setRoleCode(mapVal.get("rolecode"));
			lpolicy.setPolicyNo(policyNumber != null ? policyNumber :""); // "012HFHOSTD401386878000000"
//			lpolicy.setIntimationNo(intimationNumber != null ? intimationNumber :""); //"CLI/2020/151118/02/0300932"
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+lpolicy.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+lpolicy.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+lpolicy.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+lpolicy.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+lpolicy.getPolicyNo()+"\"" 
					+"}";
					
			JSONObject json = new JSONObject(POST_PARAMS);
			log.info(POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			
			boolean isErrorCodeReceived = false;

			
			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					JSONObject jsonObject = new JSONObject(output);
					jsonInString = jsonObject.toString();
					log.info("Response :"+jsonInString);
//					response = gson.fromJson(output, new TypeToken<List<BancsRenewedPolicyInformationResponse>>(){}.getType());
					response1 = gson.fromJson(output,BancsRenewedPolicyInformationResponse.class);
					response.add(response1);

				}
			}
			urlCon.disconnect();
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}
		
		return  response;
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
				    Builder builder = PremiaConstants.getBuilder(PremiaConstants.UNIQUE_INSTALLMENT_AMOUNT);
					String post = builder.post(String.class, "\""+ policyNumber +"\"");
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
				    Builder builder = PremiaConstants.getBuilder(PremiaConstants.ADJUST_UNIQUE_INSTALLMENT_AMOUNT);
					String post = builder.post(String.class, jsonString);
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
						
			
			public PremPolicyDetails getPolicyDetailsFromPremia(String policyNumber) {
				try {
				    Builder builder = PremiaConstants.getBuilder(PremiaConstants.POLICY_DETAILS);
				    PremPolicyDetails policyDetails = builder.post(new GenericType<PremPolicyDetails>() {}, "\""+policyNumber+ "\"");
					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->" + policyDetails);
					return policyDetails;
				} catch (Exception e) {
					log.info("********PREMIA SERVICE ******* POLICY DETAILS-*********- POLICY NUMBER  --> " + policyNumber + "  RESULT FROM PREMIA END -->  Exception is - " + e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
		public static Builder getPolicyScheduleDetail() {
				//public static List<PremPolicy> getPolicyDetail(PremPolicySearchDTO policy) {
					Builder builder = null;
					try {

						ClientConfig clientConfig = new DefaultClientConfig();
						clientConfig.getClasses().add(JacksonJsonProvider.class);
						
						Client client = Client.create(clientConfig);
						String strPremiaFlag = BPMClientContext.BANCS_URL;
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
						String strPremiaFlag = BPMClientContext.BANCS_URL;
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
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
			WebResource webResourceForLock = client.resource(BPMClientContext.BANCS_HEALTH_CARD_DOCUMENT_VIEW_URL);
			webResourceForLock.accept("application/json");
			Builder builder = webResourceForLock.type("application/json");
			builder.accept("application/json");
			
			return builder;
		}
		
		
		
		public  PremBonusDetails getBonusDetails(String policyNumber) {			
			try{
				Builder builder = getBonusDetailsBuilder();
				PremBonusDetails policyDetails = builder.post(new GenericType<PremBonusDetails>() {}, "\""+policyNumber+ "\"");
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
				String strPremiaFlag = BPMClientContext.BANCS_URL;
				WebResource webResource = client.resource(strPremiaFlag + "GetBonusDetails");
				webResource.accept("application/json");
				
				builder =  webResource.type("application/json");
				builder.accept("application/json");

			} catch (Exception e) {
				e.printStackTrace();

			}
			return builder;
	    	
	    }
		
		@SuppressWarnings("static-access")
		public String provisionAmountBancsUpdate(String policyNo,String intimationNo, String currentProvisionAmount, String jsonRequest) {
			BPMClientContext bpmClientContext = new BPMClientContext();
			String provisonFlag = bpmClientContext.getProvisonFlag();
			String output = null;
			System.out.println("provisonFlag : "+provisonFlag);
			if (provisonFlag != null) {
				if (provisonFlag.equalsIgnoreCase("Y")) {
					DBCalculationService dbService = new DBCalculationService();
					IntimationDto claimObject = dbService.getClaimObject(intimationNo);
					dbService.insertProvision(policyNo,intimationNo, currentProvisionAmount, new Timestamp(System.currentTimeMillis()),claimObject.getIntimationKey(),claimObject.getClaimKey(),claimObject.getClaimNumber());
					/*StarfaxProvisionHistory provision = new StarfaxProvisionHistory();
					provision.setPolicyNumber(policyNo);
					provision.setIntimationNumber(intimationNo);
					provision.setCurrentProvisonAmt(Double.valueOf(currentProvisionAmount));
					provision.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					entityManager.persist(provision);*/
				} else {	
					output = ClaimProvisionService.getInstance().callProvisionService(jsonRequest);
				}
			}
			return output;
		}
		public String lockPolicyUpadte(Claim claim,String Ltype) {
	
			BPMClientContext bpmClientContext = new BPMClientContext();
			String logFlag = bpmClientContext.getLockFlag();
			String response = null;
			if (logFlag != null) {
				if (logFlag.equalsIgnoreCase("Y")) {
					
					DBCalculationService dbService = new DBCalculationService();
					dbService.insertLock(claim.getIntimation().getPolicy().getPolicyNumber(),claim.getIntimation().getIntimationId(),claim.getClaimId(),Ltype);
					//BancsDBService searchService =new  BancsDBService();
					//searchService.insertLockTable(claim,Ltype);

					/*WSLockPolicy lock = new WSLockPolicy();
					lock.setClaimNumber(claim.getClaimId());
					lock.setIntimationNumber(claim.getIntimation().getIntimationId());
					lock.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber() != null ? claim.getIntimation().getPolicy().getPolicyNumber(): null);
					// lock.setRemarks(remarks);
					lock.setType(Ltype);
					entityManager.persist(lock);
				*/
				} else {
					response = LockPolicyIntegrationService.getInstance().viewBancsIntegrationLock(claim.getIntimation().getPolicy().getPolicyNumber(),claim.getIntimation().getIntimationId());
				}
			}
			return response ;
		}
		
		public String OMPLockPolicyUpadte(OMPClaim claim,String Ltype) {
			
			BPMClientContext bpmClientContext = new BPMClientContext();
			String lockFlag = bpmClientContext.getLockFlag();
			String response = null;
			if (lockFlag != null) {
				if (lockFlag.equalsIgnoreCase("Y")) {
					DBCalculationService dbService = new DBCalculationService();
					dbService.insertLock(claim.getIntimation().getPolicy().getPolicyNumber(),claim.getIntimation().getIntimationId(),claim.getClaimId(),Ltype);
					/*WSLockPolicy lock = new WSLockPolicy();
					lock.setClaimNumber(claim.getClaimId());
					lock.setIntimationNumber(claim.getIntimation().getIntimationId());
					lock.setPolicyNo(claim.getIntimation().getPolicy().getPolicyNumber() != null ? claim.getIntimation().getPolicy().getPolicyNumber() :"");
					//lock.setRemarks(remarks);
					lock.setType(Ltype);
					entityManager.persist(lock);*/
				} else {
					response = LockPolicyIntegrationService.getInstance().viewBancsIntegrationLock(claim.getIntimation().getPolicy().getPolicyNumber(),claim.getIntimation().getIntimationId());
				}
			}
			return response ;
		}
		
		public String unLockPolicyUpadte(IntimationDto claim,String Ltype) {
			
			BPMClientContext bpmClientContext = new BPMClientContext();
			String logFlag = bpmClientContext.getUnLockFlag();
			String response = null;
			if (logFlag != null) {
				if (logFlag.equalsIgnoreCase("Y")) {
					DBCalculationService dbService = new DBCalculationService();
					dbService.insertLock(claim.getPolicyNumber(),claim.getIntimationNumber(),claim.getClaimNumber(),Ltype);
					/*WSLockPolicy lock = new WSLockPolicy();
					//lock.setClaimNumber(claim.getClaimId());
					lock.setIntimationNumber(claim.getIntimationNumber());
					lock.setPolicyNo(claim.getPolicyNumber() != null ? claim.getPolicyNumber() :"");
					//lock.setRemarks(remarks);
					lock.setType(Ltype);
					entityManager.persist(lock);*/
				} else {
					response = LockPolicyIntegrationService.getInstance().viewBancsIntegrationUnLock(claim);
				}
			}
			return response ;
		}
		
		public  PremPolicyDetails getPolicyDetailsMethod(String policyNumber) {
			String responseString = "";
			PremPolicyDetails policyDetails = null;
			try {

				String serviceURL =
						BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
				if(!serviceURL.endsWith("/")){ 
					serviceURL = serviceURL+"/"+"getPolicyDetails"; 
				}else{
					serviceURL = serviceURL+"getPolicyDetails"; 
				} 
				System.out.println("Bancs Live - Url :"+serviceURL);
				URL url = new URL(serviceURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST"); 
				conn.setRequestProperty("Content-Type","application/json"); 
				conn.setRequestProperty("Accept", "application/json");
				BancsDBService searchService = new BancsDBService();
				Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
				conn.setRequestProperty("Authorization", mapVal.get("authkey"));

				PolicyDetailsRequest request = new PolicyDetailsRequest();
				request.setBusinessChannel(mapVal.get("business")); 
				request.setUserCode(mapVal.get("usercode"));
				request.setRoleCode(mapVal.get("rolecode"));
				request.setPolicyNumber(policyNumber);

				Gson gson = new Gson(); 
				String requestString = gson.toJson(request);
				log.info("Request To Policy Details: "+requestString);
				System.out.println("Bancs Live - Request:"+requestString);
				conn.setDoOutput(true); 
				
				OutputStream os = conn.getOutputStream();
				os.write(requestString.toString().getBytes()); 
				os.flush(); 
				os.close(); 
				IntegrationLogTable intLog = new IntegrationLogTable();
		        intLog.setOwner("Bancs");
		        intLog.setServiceName("getPolicyDetails");
		        intLog.setUrl(serviceURL);
		        intLog.setRequest(requestString);
		        intLog.setCreatedBy("GALAXY");
		        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		        intLog.setActiveStatus(1l);
				if(conn.getResponseCode() != 200) { 
					intLog.setStatus("Error");
					intLog.setResponse("Connection Error");
					intLog.setRemarks("Connection Error");
					throw new RuntimeException("Request Failed : HTTP Error code : "+conn.getResponseCode()); 
				}else if (conn.getResponseCode() == 200) {
					InputStreamReader in = new InputStreamReader(conn.getInputStream());
					BufferedReader br = new BufferedReader(in);
					
					String output = "";
					while ((output = br.readLine()) != null) { 
						JSONObject jsonObject = new	JSONObject(output); 
						responseString = responseString+jsonObject.toString();
						log.info("Response :"+responseString);	
						String resDtls = output;
						if(resDtls != null && resDtls.length()>3000){
							intLog.setResponse(resDtls.substring(0, 2999));
						}else {
							intLog.setResponse(resDtls);
						}
					} 
					
					Any policyDetailResponse = JsonIterator.deserialize(responseString);
					int checkErrorCode = policyDetailResponse.get("errorCode").toInt();
					if(checkErrorCode==0)
					{
						intLog.setStatus("Success");
						System.out.println("Bancs Live - Response: Success");
					}else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}

					policyDetails = getPolicyDetails(policyDetailResponse);
					try {
						getendorsementDetail(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						exp.printStackTrace();
					}
					try {
						getPolicyCoverDetails(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						exp.printStackTrace();
					}
					try {
						getPolicyNomineeDetails(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						System.out.println("++++++++++++++++++++++++Error in getPolicyNomineeDetails++++++++++++++++++++++++++");
						exp.printStackTrace();
					}
					try {
						getPreviousPolicyDetails(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						System.out.println("++++++++++++++++++++++++Error in getPreviousPolicyDetails++++++++++++++++++++++++++");
						exp.printStackTrace();
					}
					try {
						getPolicyinsuredDetails(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						System.out.println("++++++++++++++++++++++++Error in getPolicyinsuredDetails++++++++++++++++++++++++++");
						exp.printStackTrace();
					}
					try {
						getBankDetails(policyDetailResponse,policyDetails);
					}
					catch (Exception exp) {
						System.out.println("++++++++++++++++++++++++Error in getBankDetails++++++++++++++++++++++++++");
						exp.printStackTrace();
					}
					
					DBCalculationService bancsService = new DBCalculationService();;
					bancsService.insertIntegrationLogTable(intLog);
					conn.disconnect();
					

				}
			}catch (Exception exp) {
				log.error("Exception occurred while calling the provision service" + exp.getMessage());
				exp.printStackTrace();
			}
			return policyDetails;
		}
		
		public PremPolicyDetails getPolicyDetails(Any policyDetailResponse) throws ParseException {
			 System.out.println("**************** IMS_CLS_POLICY **************");
			 PremPolicyDetails newPolicy = new PremPolicyDetails();
			 newPolicy.setPortedYN("N");
			 System.out.println(" policyRelation : - "+policyDetailResponse.get("policyDetails").keys().contains("policyRelation"));
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyRelation")){
				 
				 List<Any> agentDetails = policyDetailResponse.get("policyDetails").get("policyRelation").asList()
						 .stream()
						 .filter(j->j.get("stakeCode").toString().equalsIgnoreCase("AGENT"))
						 .collect(Collectors.toList());
				 if(agentDetails.size()>0) {
					 if(agentDetails.get(0).keys().contains("partyCode")){
						 if(!isNullOrEmpty(agentDetails.get(0).get("partyCode").toString())){
							 System.out.println("Agent Code: "+agentDetails.get(0).get("partyCode").toString());
							 newPolicy.setAgentCode(agentDetails.get(0).get("partyCode").toString());
						 }else{
							 newPolicy.setAgentCode(null);
						 }
					 }
					 
					 if(agentDetails.get(0).keys().contains("partyName")){
						 if(!isNullOrEmpty(agentDetails.get(0).get("partyName").toString())){
							 System.out.println("Agent Name: "+agentDetails.get(0).get("partyName").toString());
							 newPolicy.setAgentName(agentDetails.get(0).get("partyName").toString());
						 }else{
							 newPolicy.setAgentName(null);
						 }
				 }
			 }
			 }
			 System.out.println("officeDetails "+policyDetailResponse.get("policyDetails").keys().contains("officeDetails")+
						" officeAddress "+policyDetailResponse.get("policyDetails").get("officeDetails").keys().contains("officeAddress"));
						 if(policyDetailResponse.get("policyDetails").keys().contains("officeDetails") 
						   && policyDetailResponse.get("policyDetails").get("officeDetails").keys().contains("officeAddress")){
							 System.out.println(" Inside Office Details ");
			 List<Any> officeDetails = policyDetailResponse.get("policyDetails").get("officeDetails").get("officeAddress").asList()
			 .stream()
			 .filter(a-> a.get("addressType").toString().equalsIgnoreCase("Permanent")).collect(Collectors.toList());
			 if(officeDetails.size()>0) {
				 System.out.println("District : "+ officeDetails.get(0).get("district").toString());
			 }
			 }
			 
			System.out.println(" totalPremium key check: -"+policyDetailResponse.get("policyDetails").keys().contains("totalPremium"));
			 double totalPremium = 0.0;
			 if(policyDetailResponse.get("policyDetails").keys().contains("totalPremium")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("totalPremium").toString())){
				 totalPremium = policyDetailResponse.get("policyDetails").get("totalPremium").toDouble();
				 }
			 }
			 System.out.println("Total Premium : "+ totalPremium);

			 if(policyDetailResponse.get("policyDetails").get("officeDetails").keys().contains("officeCode")){
				 System.out.println("OfficeCode : "+ policyDetailResponse.get("policyDetails").get("officeDetails").get("officeCode").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyExpiryDate")){
				 System.out.println("Policy Expiry Date : "+policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString());
			 }

			 if(policyDetailResponse.get("policyDetails").keys().contains("policyNumber")){
				 System.out.println("Policy Number : "+policyDetailResponse.get("policyDetails").get("policyNumber").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyInceptionDate")){
				 System.out.println("Policy Inception Date : "+policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyStatus")){
				 System.out.println("Policy Status : "+policyDetailResponse.get("policyDetails").get("policyStatus").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyDuration")){
				 System.out.println("Policy Duration : "+policyDetailResponse.get("policyDetails").get("policyDuration").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("productCode")){
				 System.out.println("Product Code : "+policyDetailResponse.get("policyDetails").get("productCode").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("productName")){
				 System.out.println("Product Name : "+policyDetailResponse.get("policyDetails").get("productName").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyCode")){
				 System.out.println("Proposer Party Code: "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyCode").toString());
			 }
			 if(policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("dateOfBirth")){
				 System.out.println("Proposer DOB: "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString());
			 }

			 String bonus="";
			 String floaterSumInsured="";
			 String planType="";
			 String pan="";
			 String familySize="";
			 String zone="";
			 String typeofproposal="";
			 String typeofproposalDesr="";

			 Iterator<Any> policyPropertySP = null; 
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyProperty") 
			&& policyDetailResponse.get("policyDetails").get("policyProperty").keys().contains("simpleProperty")){
				 
				 
			   policyPropertySP = policyDetailResponse.get("policyDetails").get("policyProperty").get("simpleProperty").asList().iterator();
			 while(policyPropertySP.hasNext()) {
			 Any policySingleProperty = (Any) policyPropertySP.next();
			 // System.out.println("*****"+any5.get("paramName").toString().toLowerCase());
			 switch(policySingleProperty.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()) {
			 case "bonus":
			 bonus=policySingleProperty.get("paramValue").toString();
			 newPolicy.setCumulativeBonus(bonus);
			 System.out.println("Bonus: "+bonus);
			 break;
			 case "floatersuminsured":
			 floaterSumInsured=policySingleProperty.get("paramValue").toString();
			 newPolicy.setPolicySumInsured(floaterSumInsured);
			 System.out.println("Floater Sum Insured: "+floaterSumInsured);
			 break;
			 case "plantype":
			 planType=policySingleProperty.get("paramValue").toString();
			 if (planType.equalsIgnoreCase("PLANA")) {
				 newPolicy.setPolicyPlan("A");
			 }else if(planType.equalsIgnoreCase("PLANB")) {
				 newPolicy.setPolicyPlan("B");
			 }else if(planType.equalsIgnoreCase("SILVER")) {
				 newPolicy.setPolicyPlan("S");
			 }else if(planType.equalsIgnoreCase("GOLD")) {
				 newPolicy.setPolicyPlan("G");
			 }
			 //Added temp as per Sathish Sir
			 else if(planType.equalsIgnoreCase("Individual")) {
				 newPolicy.setPolType(SHAConstants.PREMIA_POLTYPE_INDIVIDUAL);
			 }else if(planType.equalsIgnoreCase("FLOATER")) {
				 newPolicy.setPolType(SHAConstants.PREMIA_POLTYPE_FLOATER);
			 }
			 
			 System.out.println("Plan Type: "+planType);
			 break;
			 case "familysize":
			 familySize=policySingleProperty.get("paramValue").toString();
			 System.out.println("Family Size: "+familySize);
			 newPolicy.setSchemeType(familySize);
			 break;
			 case "zone":
			 zone=policySingleProperty.get("paramValue").toString();
			 newPolicy.setOfficeCode(zone);
			 System.out.println("Zone: "+zone);
			 break;
			 case "typeofproposal":
				 typeofproposal=policySingleProperty.get("paramValue").toString();
				 if(typeofproposal.equalsIgnoreCase("RNWL")) {
					 typeofproposalDesr="Renewal";
				 }
				 if(typeofproposal.equalsIgnoreCase("FR")) {
					 typeofproposalDesr="Fresh";
				 }
				 if(typeofproposal.equalsIgnoreCase("PP1")) {
					 typeofproposalDesr="Portability";
					 newPolicy.setPortedYN("Y");
				 }
				 newPolicy.setPolicyType(typeofproposalDesr);
			 break;
			 }
			 }
			 }
			 
			 List<Any> proposerPermAddrDetails =null;
			 System.out.println(" proposerDetails "+policyDetailResponse.get("policyDetails").keys().contains("proposerDetails") 
				+" partyAddress "+ policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyAddress"));
			 
			 if(policyDetailResponse.get("policyDetails").keys().contains("proposerDetails") 
				&& policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyAddress")){
				 
				 proposerPermAddrDetails = policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyAddress").asList()
						 .stream()
						 .filter(a-> a.get("addressType").toString().equalsIgnoreCase("Permanent")).collect(Collectors.toList());
				 
				 if(proposerPermAddrDetails.size()>0) {
					 System.out.println(" address1 key check -: "+proposerPermAddrDetails.get(0).keys().contains("address1"));
					 if(proposerPermAddrDetails.get(0).keys().contains("address1")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("address1").toString())){
					System.out.println("Proposer Address1: "+proposerPermAddrDetails.get(0).get("address1").toString());
					 newPolicy.setProposerAddress1(proposerPermAddrDetails.get(0).get("address1").toString());
						 }else{
							 newPolicy.setProposerAddress1(null);
						 }
					 }
					 System.out.println(" address2 key check -: "+proposerPermAddrDetails.get(0).keys().contains("address2"));

					 if(proposerPermAddrDetails.get(0).keys().contains("address2")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("address2").toString())){
					 System.out.println("Proposer Address2: "+proposerPermAddrDetails.get(0).get("address2").toString());
					 newPolicy.setProposerAddress2(proposerPermAddrDetails.get(0).get("address2").toString());
						 }else{
							 newPolicy.setProposerAddress2(null);
						 }
					 }
					 System.out.println(" address3 key check -: "+proposerPermAddrDetails.get(0).keys().contains("address3"));

					 if(proposerPermAddrDetails.get(0).keys().contains("address3")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("address3").toString())){
					System.out.println("Proposer Address3: "+proposerPermAddrDetails.get(0).get("address3").toString());
					 newPolicy.setProposerAddress3(proposerPermAddrDetails.get(0).get("address3").toString());
					 }else{
						 newPolicy.setProposerAddress3(null);	 
					 }
					 }
					 System.out.println(" state key check -: "+proposerPermAddrDetails.get(0).keys().contains("state"));

					 if(proposerPermAddrDetails.get(0).keys().contains("state")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("state").toString())){
					 System.out.println("State: "+proposerPermAddrDetails.get(0).get("state").toString());
					 newPolicy.setState(proposerPermAddrDetails.get(0).get("state").toString());
					 }else{
						 newPolicy.setState(null);
					 }
					 }
					 System.out.println(" district key check -: "+proposerPermAddrDetails.get(0).keys().contains("district"));

					 if(proposerPermAddrDetails.get(0).keys().contains("district")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("district").toString())){
					 System.out.println("SubDistrict: "+proposerPermAddrDetails.get(0).get("district").toString());
					 newPolicy.setSubDistrict(proposerPermAddrDetails.get(0).get("district").toString());
					 }else{
						 newPolicy.setSubDistrict(null);
					 }
					 }
					 System.out.println(" eMailId key check -: "+proposerPermAddrDetails.get(0).keys().contains("eMailId"));

					 if(proposerPermAddrDetails.get(0).keys().contains("eMailId")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("eMailId").toString())){
					 System.out.println("Proposer Email ID: "+ proposerPermAddrDetails.get(0).get("eMailId").toString());
					 newPolicy.setProposerEmail(proposerPermAddrDetails.get(0).get("eMailId").toString());
					 }else{
						 newPolicy.setProposerEmail(null);
					 }
					}
					 
					 System.out.println(" fax key check -: "+proposerPermAddrDetails.get(0).keys().contains("fax"));

					 if(proposerPermAddrDetails.get(0).keys().contains("fax")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("fax").toString())){
						 System.out.println("Proposer Fax no: "+ proposerPermAddrDetails.get(0).get("fax").toString());
					 	newPolicy.setProposerOfficeFaxNo(proposerPermAddrDetails.get(0).get("fax").toString());
					 }else{
						 newPolicy.setProposerOfficeFaxNo(null);
					 }
						 }
					 System.out.println(" mobileNo key check -: "+proposerPermAddrDetails.get(0).keys().contains("mobileNo"));

					 if(proposerPermAddrDetails.get(0).keys().contains("mobileNo")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("mobileNo").toString())){
						 System.out.println("Proposer Mobile No: "+proposerPermAddrDetails.get(0).get("mobileNo").toString());
						 newPolicy.setProposerMobileNo(proposerPermAddrDetails.get(0).get("mobileNo").toString());
						 }else{
							 newPolicy.setProposerMobileNo(null);
						 }
					 }
					 System.out.println(" pincode key check -: "+proposerPermAddrDetails.get(0).keys().contains("pincode"));

					 if(proposerPermAddrDetails.get(0).keys().contains("pincode")){
						 if(!isNullOrEmpty(proposerPermAddrDetails.get(0).get("pincode").toString())){
					 newPolicy.setPinCode(proposerPermAddrDetails.get(0).get("pincode").toString());
						 }else{
							 newPolicy.setPinCode(null);
						 }
					 }
				 }
				 }
			 
		
		String proposerName=null;
		 if(policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("firstName") 
		    && policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("middleName")
		    && policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("lastName")){
		 
			 proposerName = policyDetailResponse.get("policyDetails").get("proposerDetails").get("firstName").toString().replace("null", "")
					 +" "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("middleName").toString().replace("null", "")
					 +" "+ policyDetailResponse.get("policyDetails").get("proposerDetails").get("lastName").toString().replace("null", "");
			 newPolicy.setProposerName(proposerName);
			 System.out.println("Proposer Name: "+ proposerName);
		 }

		 List<Any> proposerOfficeAddrDetails = null;
		 if(policyDetailResponse.get("policyDetails").keys().contains("proposerDetails") 
			&& policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyAddress")){
			 proposerOfficeAddrDetails =
			 policyDetailResponse.get("policyDetails").get("proposerDetails").get(
			 "partyAddress").asList() .stream() .filter(a->
			 a.get("addressType").toString().equalsIgnoreCase("Office")).collect(
			 Collectors.toList());
			 if(proposerOfficeAddrDetails.size()>0) {
				 if(proposerOfficeAddrDetails.get(0).keys().contains("address1")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("address1").toString())){
						 System.out.println("Proposer Office Address1: "+proposerOfficeAddrDetails.get
								 (0).get("address1").toString());
						 newPolicy.setProposerOfficeAddress1(proposerOfficeAddrDetails.get
								 (0).get("address1").toString());
						 
					 }else{
						 newPolicy.setProposerOfficeAddress1(null);
					 }
				 }
				 
				 if(proposerOfficeAddrDetails.get(0).keys().contains("address2")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("address2").toString())){
						 System.out.println("Proposer Office Address2: "+proposerOfficeAddrDetails.get
								 (0).get("address2").toString());
						 newPolicy.setProposerOfficeAddress2(proposerOfficeAddrDetails.get
								 (0).get("address2").toString());
					 }else{
						 newPolicy.setProposerOfficeAddress2(null);
					 }
				 }
				 
				 if(proposerOfficeAddrDetails.get(0).keys().contains("address3")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("address3").toString())){
						 System.out.println("Proposer Office Address3: "+proposerOfficeAddrDetails.get
								 (0).get("address3").toString());
						 newPolicy.setProposerOfficeAddress3(proposerOfficeAddrDetails.get
								 (0).get("address3").toString());
					 }else{
						 newPolicy.setProposerOfficeAddress3(null);
					 }
				 }
				 
				 if(proposerOfficeAddrDetails.get(0).keys().contains("eMailId")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("eMailId").toString())){
						 System.out.println("Proposer Office Email ID: "+proposerOfficeAddrDetails.get
								 (0).get("eMailId").toString());
						 newPolicy.setBaNCSOfficeEmailId(proposerOfficeAddrDetails.get
									(0).get("eMailId").toString());
					 }else{
						 newPolicy.setBaNCSOfficeEmailId(null);
					 }
				 }
			 
				 if(proposerOfficeAddrDetails.get(0).keys().contains("fax")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("fax").toString())){
						 System.out.println("Proposer Office Fax no: "+proposerOfficeAddrDetails.get(0
								 ).get("fax").toString());
						 newPolicy.setBaNCSOfficeFax(proposerOfficeAddrDetails.get
								 (0).get("fax").toString());
					 }else{
						 newPolicy.setBaNCSOfficeFax(null);
					 }
					 }
				 if(proposerOfficeAddrDetails.get(0).keys().contains("phoneNo")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("phoneNo").toString())){
						 System.out.println("Proposer Office Tel No: "+proposerOfficeAddrDetails.get(0
								 ).get("phoneNo").toString());
						 newPolicy.setBaNCSofficeTelPhone(proposerOfficeAddrDetails.get
								 (0).get("phoneNo").toString());
					 }else{
						 newPolicy.setBaNCSofficeTelPhone(null);
					 }
				 }
				 
				 //commented as per new update - need to discuss further.
				 /*if(proposerOfficeAddrDetails.get(0).keys().contains("phoneNo")){
					 if(!isNullOrEmpty(proposerOfficeAddrDetails.get(0).get("phoneNo").toString())){
						 System.out.println("Proposer Tel No: "+proposerOfficeAddrDetails.get(0).get(
								 "phoneNo").toString());
						 newPolicy.setProposerTelNo(proposerOfficeAddrDetails.get(0).get("phoneNo").toString());
					 }else{
						 newPolicy.setProposerTelNo(null);
					 }
				 }*/
			  
			 }
			 }
		 
		 String proposerPanNo=null;
		 
		 if(policyDetailResponse.get("policyDetails").keys().contains("proposerDetails") 
					&& policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyProperty")
					&& policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyProperty").keys().contains("simpleProperty")){
			 
			 proposerPanNo = policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyProperty").get("simpleProperty").asList()
					 .stream().filter(x-> x.get("paramName").toString().equalsIgnoreCase("PAN")).collect(Collectors.toList()).get(0).get("paramValue").toString();
			 System.out.println("Proposer PAN No: "+ proposerPanNo);
		 }
		 
			 System.out.println("Proposer Title: "+policyDetailResponse.get("policyDetails").get("proposerDetails").get("title").toString());
			 System.out.println("Receipt Generation Date: "+policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString());
			 System.out.println("Receipt Number: "+policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptNumber").toString());

			 List<Any> smDetails =null;
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyRelation")){
				 
				 smDetails = policyDetailResponse.get("policyDetails").get("policyRelation").asList()
						 .stream()
						 .filter(j->j.get("stakeCode").toString().equalsIgnoreCase("SM"))
						 .collect(Collectors.toList());
				 if(smDetails.size()>0) {
					 
					 if(smDetails.get(0).keys().contains("partyCode")){
						 if(!isNullOrEmpty(smDetails.get(0).get("partyCode").toString())){
							 System.out.println("SM Code: "+smDetails.get(0).get("partyCode").toString());
							 newPolicy.setSmCode(smDetails.get(0).get("partyCode").toString());
						 }else{
							 newPolicy.setSmCode(null);
						 }
					 }
					 
					 if(smDetails.get(0).keys().contains("partyName")){
						 if(!isNullOrEmpty(smDetails.get(0).get("partyName").toString())){
							 System.out.println("SM Name: "+smDetails.get(0).get("partyName").toString());
							 newPolicy.setSmName(smDetails.get(0).get("partyName").toString());
						 }else{
							 newPolicy.setSmName(null);
						 }
					 }
					 
				 }
			 }

			 Iterator<Any> endorserIterator =null;
			 if(policyDetailResponse.get("policyDetails").keys().contains("endorsementDetails")){
				 endorserIterator = policyDetailResponse.get("policyDetails").get("endorsementDetails").asList().iterator();
			 }
			 while(endorserIterator.hasNext()) {
			 Any endorserDetail = (Any) endorserIterator.next();
			 System.out.println("Endorsement Effective Date: "+endorserDetail.get("endorsementEffectiveDate").toString());
			 System.out.println("Endorsement Number: "+endorserDetail.get("endorsementNumber").toString());
			 System.out.println("Endorsement Premium: "+endorserDetail.get("endorsementPremium").toString());
			 System.out.println("Endorsement Revised Sum Insured: "+endorserDetail.get("endorsementRevisedSumInsured").toString());
			 System.out.println("Endorsement Sum Insured: "+endorserDetail.get("endorsementSumInsured").toString());
			 System.out.println("Type of Endorsement: "+endorserDetail.get("typeofEndorsement").toString());
			 System.out.println("Remarks: "+endorserDetail.get("remarks").toString());
			 if(endorserDetail.keys().contains("endorsementNumber")){
				 if(!isNullOrEmpty(endorserDetail.get("endorsementNumber").toString())){
					 
					 newPolicy.setPolicyEndNo(endorserDetail.get("endorsementNumber").toString());
				 }else{
					 newPolicy.setPolicyEndNo(null);
				 }
			 }
			 }

			 if(policyDetailResponse.get("policyDetails").keys().contains("policyExpiryDate")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString()))
				 {
					 newPolicy.setPolicyEndDate(policyDetailResponse.get("policyDetails").get("policyExpiryDate").toString());
				 }
				 else
				 {
					 newPolicy.setPolicyEndDate(null);
				 }
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyNumber")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyNumber").toString())){
					 newPolicy.setPolicyNo(policyDetailResponse.get("policyDetails").get("policyNumber").toString());
				 }
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyInceptionDate")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString()))
				 {
					 newPolicy.setPolicyStartDate(policyDetailResponse.get("policyDetails").get("policyInceptionDate").toString());
				 }
				 else
				 {
					 newPolicy.setPolicyStartDate(null);
				 }
			 }
			 
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyStatus")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyStatus").toString())){
					 newPolicy.setPolicyStatus(policyDetailResponse.get("policyDetails").get("policyStatus").toString());
				 }
			 }
			 
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyDuration")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyDuration").toString())){
					 newPolicy.setPolicyTerm(policyDetailResponse.get("policyDetails").get("policyDuration").toString());
				 }
			 }

			 //newPolicy.setPolicyType(planType.toString());
			 if(policyDetailResponse.get("policyDetails").keys().contains("proposerDetails") 
						&& policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("partyCode")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyCode").toString())){
					 newPolicy.setProposerCode(policyDetailResponse.get("policyDetails").get("proposerDetails").get("partyCode").toString());
				 }
			 }
			 
			 if(policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("dateOfBirth")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString()))
				 {
					 newPolicy.setProposerDOB(policyDetailResponse.get("policyDetails").get("proposerDetails").get("dateOfBirth").toString());
				 }
				 else
				 {
					 newPolicy.setProposerDOB(null);
				 }
			 }
			 
			 newPolicy.setProposerPanNumber(proposerPanNo);
			 if(policyDetailResponse.get("policyDetails").get("proposerDetails").keys().contains("title")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("proposerDetails").get("title").toString())){
					 newPolicy.setProposerTitle(policyDetailResponse.get("policyDetails").get("proposerDetails").get("title").toString());
				 }
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("productName")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("productName").toString())){
					 newPolicy.setProductName(policyDetailResponse.get("policyDetails").get("productName").toString());
				 }
			 }
			 if(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).keys().contains("receiptGenerationDate")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString()))
				 {
					 newPolicy.setReceiptDate(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptGenerationDate").toString());
				 }
				 else
				 {
					 newPolicy.setReceiptDate(null);
				 }
			 }
			 
			 if(policyDetailResponse.get("policyDetails").keys().contains("policyPaymentInformation") 
						&& policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).keys().contains("receiptNumber")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptNumber").toString())){
					 newPolicy.setReceiptNo(policyDetailResponse.get("policyDetails").get("policyPaymentInformation",0).get("receiptNumber").toString());
				 }
			 }

			 newPolicy.setDistrict(proposerPermAddrDetails.get(0).get("district").toString());
			 if(!isNullOrEmpty(zone)){
				 newPolicy.setPolicyZone(zone);
			 }
			 
			 newPolicy.setProductCode(policyDetailResponse
							.get("policyDetails").get("productCode").toString());
		
			 if(policyDetailResponse.get("policyDetails").keys().contains("officeDetails")
						&& policyDetailResponse.get("policyDetails").get("officeDetails").keys().contains("officeCode")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("officeDetails").get("officeCode").toString())){
					 newPolicy.setOfficeCode(policyDetailResponse.get("policyDetails").get("officeDetails").get("officeCode").toString());
				}
			 }
			 if(policyDetailResponse.get("policyDetails").keys().contains("netPremium")){
				 if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("netPremium").toString())){
					 newPolicy.setGrossPremium(policyDetailResponse.get("policyDetails").get("netPremium").toString());
				 }
			 }
			 
			 if(policyDetailResponse.get("policyDetails").keys().contains("gst")){
					if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("gst").toString())){
						newPolicy.setBaNCSPremiumTax(policyDetailResponse.get("policyDetails").get("gst").toString());
					}
				}
				if(policyDetailResponse.get("policyDetails").keys().contains("stampDuty")){
					if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("stampDuty").toString())){
						newPolicy.setBaNCSStampDuty(policyDetailResponse.get("policyDetails").get("stampDuty").toString());
					}
				}

				if(policyDetailResponse.get("policyDetails").keys().contains("totalPremium")){
					if(!isNullOrEmpty(policyDetailResponse.get("policyDetails").get("totalPremium").toString())){
						newPolicy.setBaNCSTotalPremium(policyDetailResponse.get("policyDetails").get("totalPremium").toString());
					}
				}
			
			newPolicy.setPolicySource("B");
			
			newPolicy.setProposerGender(policyDetailResponse.get("policyDetails")
					.get("proposerDetails").get("gender").toString());	 
			 
			 return newPolicy;
			 }
		
		public  void getPolicyCoverDetails(Any policyDetailResponse,PremPolicyDetails policy) throws ParseException {
			if(policyDetailResponse.keys().contains("policyDetails")
					&& policyDetailResponse.get("policyDetails").keys().contains("policyProperty")
					&& policyDetailResponse.get("policyDetails").get("policyProperty").get("multiSetProperty").keys().contains("policyDetails")) {
			List<Any> policycoverDetail = policyDetailResponse.get("policyDetails").get("policyProperty")
					.get("multiSetProperty").asList().stream()
					.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Cover SI Details"))
					.collect(Collectors.toList());
			if(policycoverDetail.get(0).keys().contains("multiSetDetail")) {
			Iterator<Any> policycoverDetailIterator = policycoverDetail.get(0).get("multiSetDetail").iterator();
			System.out.println(policycoverDetail);
			System.out.println("****************************************************************");
			int l = 0;
			List<PremPolicyCoverDetails> premPolicyCoverDetails = new ArrayList<PremPolicyCoverDetails>();
			while (policycoverDetailIterator.hasNext()) {
				PremPolicyCoverDetails PremPolicyCoverDetails=new PremPolicyCoverDetails();
				Any any5 = (Any) policycoverDetailIterator.next();
				Map<String, Any> collect2 = any5.get("property").asList().stream().collect(Collectors.toMap(
						x -> (x.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()),
						x -> x.get("paramValue")));
				System.out.println(collect2);
				System.out.println(collect2.get("covercode"));
				System.out.println(collect2.get("coversuminsured"));
				PremPolicyCoverDetails.setCoverCode(collect2.get("covercode").toString());
//				PremPolicyCoverDetails.setCoverDescription(collect2.get("CoverDesc").toString());
//				PremPolicyCoverDetails.setRiskId(collect2.get("RiskID").toString());
				PremPolicyCoverDetails.setSumInsured(collect2.get("coversuminsured").toString());
				premPolicyCoverDetails.add(PremPolicyCoverDetails);
			}
			policy.setPremPolicyCoverDetails(premPolicyCoverDetails);
			}
			}
		}
		public void getPolicyinsuredDetails(Any policyDetailResponse,PremPolicyDetails policy) throws ParseException {
			
			if(policyDetailResponse.get("policyDetails").keys().contains("insuredDetails"))
			{
			System.out.println("****************************************************************");
			List<PremInsuredDetails> insuredDetails = new ArrayList<PremInsuredDetails>();
			List<Any> policyInsuredDetail = policyDetailResponse.get("policyDetails").get("insuredDetails").asList().stream()
					.collect(Collectors.toList());
			Iterator<Any> policyInsuredDetaillist = policyInsuredDetail.iterator();
		    while (policyInsuredDetaillist.hasNext()) {
		    	 String ageinyears="";
				 String memberAgeAtEntry="";
				 String coPayPercentage="";
				 String deductible="";
				 String bonusforHealth="";
				 String relationshipwithProposer="";
				 String ped="";
				 String riskSumInsuredinBaseCurrency="";
				 String healthidcardno="";
				 String dateofBirth="";
				 String gender="";
		    	 String insuredName="";
		    	 String partyCode="";
		    	 PremInsuredDetails premInsuredDetails=new PremInsuredDetails();
		    Any policyInsuredDetailData=(Any) policyInsuredDetaillist.next();
		    if(policyInsuredDetailData.keys().contains("memberDetails")){
	       Any insuredMemberDetails = policyInsuredDetailData.get("memberDetails");
	       System.out.println("****************************************************************");
	       if(insuredMemberDetails !=null)
	       {
	    	if(insuredMemberDetails.keys().contains("dateOfBirth")){
	       	dateofBirth = insuredMemberDetails.get("dateOfBirth").toString();
	       	System.out.println("dateOfBirth" +":"+dateofBirth);
	        premInsuredDetails.setDob(dateofBirth);
	        }
	    	if(insuredMemberDetails.keys().contains("gender"))
	        {
	       	gender = insuredMemberDetails.get("gender").toString();
	       	System.out.println("gender" +":"+gender);
	       	premInsuredDetails.setGender(gender);
	        }
	       	 String firstName = insuredMemberDetails.keys().contains("firstName") ? (insuredMemberDetails.get("firstName").toString().replace("null", "")) : "";
	       	 String middleName = insuredMemberDetails.keys().contains("middleName") ? (insuredMemberDetails.get("middleName").toString().replace("null", "")) : "";
	       	 String lastName = insuredMemberDetails.keys().contains("lastName") ? (insuredMemberDetails.get("lastName").toString().replace("null", "")) : "";
	      
	       	 if(firstName != null && !firstName.isEmpty())
	       	 {
	       		 insuredName = insuredName + firstName +" ";
	       	 }
	       	 if(middleName != null && !middleName.isEmpty())
	           {
	           	insuredName = insuredName + middleName +" ";
	           }
	       	 if(lastName != null && !lastName.isEmpty())
	       	 {
	           	insuredName = insuredName + lastName +" "; 
	       	 }
	      	 System.out.println("InsuredName" +":"+insuredName);
	      	 premInsuredDetails.setInsuredName(insuredName);
	      	if(insuredMemberDetails.keys().contains("partyCode")){
	      		partyCode = insuredMemberDetails.get("partyCode").toString();
	      		System.out.println("partyCode" +":"+partyCode);
	      		premInsuredDetails.setBaNCSSourceRiskID(partyCode);
	      	}
	      	if(policyInsuredDetailData.get("memberDetails").keys().contains("partyAddress") && policyInsuredDetailData.get("memberDetails").get("partyAddress").get(0).keys().contains("addressType"))
	      	 {
	      	List<Any> insuredPermAddrDetails = policyInsuredDetailData.get("memberDetails").get("partyAddress").asList()
	      			 .stream()
	      			 .filter(a-> a.get("addressType").toString().equalsIgnoreCase("Permanent")).collect(Collectors.toList());
	        Any partyAddress=insuredMemberDetails.get("partyAddress");
	        if(insuredPermAddrDetails.size()>0) {
	        	
	        	String registeredMobileNo="";
	        	String eMailId="";
	        	String insuredMobileNo="";
	        	String address1="";
	        	String address2="";
	        	String address3="";
	        	String city="";
	        	String pincode="";
	        	String state="";
	        	
	        	if(insuredPermAddrDetails.get(0).keys().contains("mobileNo")){
	        		registeredMobileNo=insuredPermAddrDetails.get(0).get("mobileNo").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("eMailId")){
	        		eMailId =insuredPermAddrDetails.get(0).get("eMailId").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("mobileNo")){
	        		insuredMobileNo=insuredPermAddrDetails.get(0).get("mobileNo").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("address1")){
	        		address1 =insuredPermAddrDetails.get(0).get("address1").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("address2")){
	        		address2=insuredPermAddrDetails.get(0).get("address2").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("address3")){
	        		address3=insuredPermAddrDetails.get(0).get("address3").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("city")){
	        		city=insuredPermAddrDetails.get(0).get("city").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("pincode")){
	        		pincode=insuredPermAddrDetails.get(0).get("pincode").toString();
	        	}
	        	if(insuredPermAddrDetails.get(0).keys().contains("state")){
	        		state=insuredPermAddrDetails.get(0).get("state").toString();
	        	}
	            
	            System.out.println(" registeredMobileNo : "+registeredMobileNo);
	            System.out.println(" eMailId : "+eMailId);
	            System.out.println(" insuredMobileNo : "+insuredMobileNo);
	            System.out.println(" address1 : "+address1);
	            System.out.println(" address2  : "+address2);
	            System.out.println(" address3 : "+address3);
	            System.out.println(" city : "+city);
	            System.out.println(" pincode : "+pincode);
	            System.out.println(" state : "+state);
	            premInsuredDetails.setMobileNumber(registeredMobileNo);
	            //NEED TO VERIFY
	            premInsuredDetails.setContactNumber(registeredMobileNo);
	            premInsuredDetails.setMailId(eMailId);
	            premInsuredDetails.setAddress1(address1);
	            premInsuredDetails.setAddress2(address2);
	            premInsuredDetails.setAddress3(address3);
	            premInsuredDetails.setCity(city);
	            premInsuredDetails.setPinCode(pincode);
	            premInsuredDetails.setState(state);
	        }
	       }
	   	}
	       }
	   	System.out.println("****************************************************************");
	   	if(policyInsuredDetailData.get("riskProperty").keys().contains("simpleProperty")){
	   		List<Any> policyriskDetail = policyInsuredDetailData.get("riskProperty").get("simpleProperty").asList();
	   	   Iterator<Any> riskDetailIterator = policyriskDetail.iterator();
	   	while (riskDetailIterator.hasNext()) {
				Any any5 = (Any) riskDetailIterator.next();
				 String ParamName = any5.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase();
				 switch(ParamName)
				 {
				 case "ageinyears":
					 ParamName = any5.get("paramName").toString();
					  ageinyears=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+ageinyears);
					 premInsuredDetails.setInsuredAge(ageinyears);
		            break;
				 case "memberageatentry":
					 ParamName = any5.get("paramName").toString();
					 memberAgeAtEntry=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+memberAgeAtEntry);
					 premInsuredDetails.setEntryAge(memberAgeAtEntry);
		            break;
				 case "copaypercentage":    
					 ParamName = any5.get("paramName").toString();
					  coPayPercentage=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+coPayPercentage);
					 premInsuredDetails.setCoPay(coPayPercentage);
					 break;
				 case "deductible":    
					 ParamName = any5.get("paramName").toString();
					  deductible=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+deductible);
					 premInsuredDetails.setDeductiableAmt(deductible);
					 break;
				 case "bonusforhealth":    
					 ParamName = any5.get("paramName").toString();
					  bonusforHealth=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+bonusforHealth);
					 premInsuredDetails.setCumulativeBonus(bonusforHealth);
					 break;		 
				 case "relationshipwithproposer":    
					 ParamName = any5.get("paramName").toString();
					  relationshipwithProposer=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+relationshipwithProposer);
					 premInsuredDetails.setRelation(relationshipwithProposer);
					 break;	
					 //DATA PROBLEM
				 /*case "ped":
					 ParamName = any5.get("paramName").toString();
					  ped=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+ped);
					 premInsuredDetails.setPedCoPay(ped);
					 break;*/
				 case "risksuminsuredinbasecurrency":
					 ParamName = any5.get("paramName").toString();
					 riskSumInsuredinBaseCurrency=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+riskSumInsuredinBaseCurrency);
					 premInsuredDetails.setSumInsured(riskSumInsuredinBaseCurrency);
					 break;	
				 case "healthidcardno":
					 ParamName = any5.get("paramName").toString();
					 healthidcardno=any5.get("paramValue").toString();
					 System.out.println(ParamName +":"+healthidcardno);
					 premInsuredDetails.setHealthCardNo(healthidcardno);
					 break;		 
				 }
				 
			}
	   	}
		System.out.println("****************************************************************");
		//premInsuredDetails.setMobileNumber(policy.getProposerMobileNo());
		
		if(policyInsuredDetailData.get("riskProperty").keys().contains("multiSetProperty") && policyInsuredDetailData.get("riskProperty").get("multiSetProperty").get(0).keys().contains("multiSetName"))
		{
		List<Any> policyInsuredriskDetail = policyInsuredDetailData.get("riskProperty").get("multiSetProperty").asList().stream()
				.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("PED Details"))
				.collect(Collectors.toList());
		if(!policyInsuredriskDetail.isEmpty())
		{
			
			if(policyInsuredriskDetail.get(0).keys().contains("multiSetDetail"))
			{
			Iterator<Any> policyriskIterator = policyInsuredriskDetail.get(0).get("multiSetDetail").iterator();
			while(policyriskIterator.hasNext())
			{
				
				Any previousInsuranceDetail = (Any) policyriskIterator.next();
				List<Any> previousInsurancevaluelist=previousInsuranceDetail.get("property").asList();
				Iterator<Any> previousInsurancevalueIterator = previousInsurancevaluelist.iterator();
				PremPEDDetails premPEDDetails = null;
				List<PremPEDDetails> premPEDList = new ArrayList<PremPEDDetails>();
				while(previousInsurancevalueIterator.hasNext())
				{
					String pedDescription ="";
					String pedcode="";
					Any previousInsurePeddata = (Any) previousInsurancevalueIterator.next();
					String ParamName = previousInsurePeddata.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase();
				    switch(ParamName)
				    {
				    case "peddescription":
				    	pedDescription=previousInsurePeddata.get("paramValue").toString();
						 System.out.println("pedDescription: "+pedDescription);
			            break;
				    case "pedicdcode":
				    	pedcode = previousInsurePeddata.get("paramValue").toString();
				    	System.out.println("pedCode: "+pedcode);
				    }
				    
				    premPEDDetails = new PremPEDDetails();
					premPEDDetails.setPedCode(pedcode);
				    premPEDDetails.setPedDescription(pedDescription);
				    premPEDList.add(premPEDDetails);
				 }
				
			    premInsuredDetails.setPedDetails(premPEDList);
			}
			}
		}
		}
		
		System.out.println("*********************Previous Insurance Detail*******************************************");
		
		if(policyInsuredDetailData.get("riskProperty").keys().contains("multiSetProperty") && policyInsuredDetailData.get("riskProperty").get("multiSetProperty").get(0).keys().contains("multiSetName"))
		{
			List<Any> previousInsuranceDetaillist = policyInsuredDetailData.get("riskProperty").get("multiSetProperty").asList().stream()
				.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Previous Insurance Detail"))
				.collect(Collectors.toList());
		if(!previousInsuranceDetaillist.isEmpty())
		{
			
			if(previousInsuranceDetaillist.get(0).keys().contains("multiSetDetail"))
			{
		Iterator<Any> policyriskIterator = previousInsuranceDetaillist.get(0).get("multiSetDetail").iterator();
		while(policyriskIterator.hasNext())
		{
			Any previousInsuranceDetail = (Any) policyriskIterator.next();
			List<Any> previousInsurancevaluelist=previousInsuranceDetail.get("property").asList();
			Iterator<Any> previousInsurancevalueIterator = previousInsurancevaluelist.iterator();
			List<PremPortabilityPrevPolicyDetails> premPortabilityPrevPolicyList = new ArrayList<PremPortabilityPrevPolicyDetails>();
			while(previousInsurancevalueIterator.hasNext())
			{
				String underwritingyear="";
				String cumulativebonusindividual="";
				String cumulativebonusfloater="";
				String firstyearexclusion="";
				String secondyearexclusion="";
				String suminsuredindividual="";
				String suminsuredfloater="";
				String natureofillness="";
				String noofclaims="";
				String detailsofped="";
				String customerid="";
				String policynumber="";
				String dayscoverexclusion="";
				String nameoftheproduct="";
				String policytype="";
				String riskeffectivedate="";
				String riskexpirydate="";
				String nameoftheinsurercompany="";
				PremPortabilityPrevPolicyDetails premPortabilityPrevPolicyDetails = null;
				Any previousInsurancedata = (Any) previousInsurancevalueIterator.next();
				String ParamName = previousInsurancedata.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase();
			    switch(ParamName)
			    {
			    case "underwritingyear":
					 underwritingyear=previousInsurancedata.get("paramValue").toString();
					 System.out.println("underwritingyear: "+underwritingyear);
		            break;
			    case "cumulativebonusindividual":
					 cumulativebonusindividual=previousInsurancedata.get("paramValue").toString();
					 System.out.println("cumulativebonusindividual: "+cumulativebonusindividual);
		            break;
			    case "cumulativebonusfloater":
					 cumulativebonusfloater=previousInsurancedata.get("paramValue").toString();
					 System.out.println("cumulativebonusfloater: "+cumulativebonusfloater);
		            break;
			    case "1styearexclusion":
					 firstyearexclusion=previousInsurancedata.get("paramValue").toString();
					 System.out.println("1styearexclusion: "+firstyearexclusion);
		            break;  
			    case "2ndyearexclusion":
					 secondyearexclusion=previousInsurancedata.get("paramValue").toString();
					 System.out.println("2ndyearexclusion: "+secondyearexclusion);
		            break;  
			    case "suminsuredindividual":
			    	suminsuredindividual=previousInsurancedata.get("paramValue").toString();
					 System.out.println("suminsuredindividual: "+suminsuredindividual);
		            break;   
			    case "suminsuredfloater":
			    	suminsuredfloater=previousInsurancedata.get("paramValue").toString();
					 System.out.println("suminsuredfloater: "+suminsuredfloater);
		            break;
			    case "natureofillnessresultingtheclaim":
			    	natureofillness=previousInsurancedata.get("paramValue").toString();
					 System.out.println("natureofillnessresultingtheclaim: "+natureofillness);
		            break;    
			    case "noofclaims":
			    	noofclaims=previousInsurancedata.get("paramValue").toString();
					 System.out.println("noofclaims: "+noofclaims);
		            break; 
			    case "detailsofped":
			    	detailsofped=previousInsurancedata.get("paramValue").toString();
					 System.out.println("detailsofped: "+detailsofped);
		            break; 
			    case "customerid":
			    	customerid=previousInsurancedata.get("paramValue").toString();
					 System.out.println("customerid: "+customerid);
		            break;
			    case "policynumber":
			    	policynumber=previousInsurancedata.get("paramValue").toString();
					 System.out.println("policynumber: "+policynumber);
		            break; 
			    case "30dayscoverexclusion":
			    	dayscoverexclusion=previousInsurancedata.get("paramValue").toString();
					 System.out.println("30dayscoverexclusion: "+dayscoverexclusion);
		            break;  
			    case "nameoftheproduct":
			    	nameoftheproduct=previousInsurancedata.get("paramValue").toString();
					 System.out.println("nameoftheproduct: "+nameoftheproduct);
		            break;
			    case "policytype":
			    	policytype=previousInsurancedata.get("paramValue").toString();
					 System.out.println("policytype: "+policytype);
		            break; 
			    case "riskeffectivedate":
			    	riskeffectivedate=previousInsurancedata.get("paramValue").toString();
					 System.out.println("riskeffectivedate: "+riskeffectivedate);
		            break; 
			    case "riskexpirydate":
			    	riskexpirydate=previousInsurancedata.get("paramValue").toString();
					 System.out.println("riskexpirydate: "+riskexpirydate);
		            break;
			    case "nameoftheinsurercompany":
					nameoftheinsurercompany=previousInsurancedata.get("paramValue").toString();
					break;
			    }
			    
			    premPortabilityPrevPolicyDetails = new PremPortabilityPrevPolicyDetails();
                premPortabilityPrevPolicyDetails.setUwYear(underwritingyear);
                premPortabilityPrevPolicyDetails.setCummulativeBonus(cumulativebonusindividual);
                premPortabilityPrevPolicyDetails.setExclusion_1stYr(firstyearexclusion);
                premPortabilityPrevPolicyDetails.setExclusion_2ndYr(secondyearexclusion);
                premPortabilityPrevPolicyDetails.setSumInsured(suminsuredindividual);
                premPortabilityPrevPolicyDetails.setNatureofIllness(natureofillness);
                premPortabilityPrevPolicyDetails.setNoofClaims(noofclaims);
                premPortabilityPrevPolicyDetails.setPedDetails(detailsofped);
                premPortabilityPrevPolicyDetails.setCustomerId(customerid);
                premPortabilityPrevPolicyDetails.setPolicyNumber(policynumber);
                premPortabilityPrevPolicyDetails.setWaiver30Days(dayscoverexclusion);
                premPortabilityPrevPolicyDetails.setProductName(nameoftheproduct);
                premPortabilityPrevPolicyDetails.setPolicyType(policytype);
                premPortabilityPrevPolicyDetails.setPolicyFmDt(riskeffectivedate);
                premPortabilityPrevPolicyDetails.setPolicyToDt(riskexpirydate);
                premPortabilityPrevPolicyDetails.setInsurerName(nameoftheinsurercompany);
                premPortabilityPrevPolicyList.add(premPortabilityPrevPolicyDetails);
			}
			premInsuredDetails.setPortabilityPrevPolicyDetails(premPortabilityPrevPolicyList);
		  }
		               
	        }
		    }
		}
		
		insuredDetails.add(premInsuredDetails);
		    }
		    policy.setInsuredDetails(insuredDetails);
		}
		    
		}
		
		public void getPolicyNomineeDetails(Any policyDetailResponse,PremPolicyDetails policy) throws ParseException {

			List<Any> policyPropertyMSDetail = policyDetailResponse.get("policyDetails").get("policyProperty")
					.get("multiSetProperty").asList().stream()
					.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Nominee Detail"))
					.collect(Collectors.toList());

			Iterator<Any> nomineeDetailIterator = policyPropertyMSDetail.get(0).get("multiSetDetail").iterator();
			List<PremInsuredNomineeDetails> properNomineeDetails = new ArrayList<PremInsuredNomineeDetails>();
			System.out.println("****************************************************************");

			int l = 0;
			while (nomineeDetailIterator.hasNext()) {
				Any any5 = (Any) nomineeDetailIterator.next();
				Map<String, Any> collect2 = any5.get("property").asList().stream().collect(Collectors.toMap(
						x -> (x.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase()),
						x -> x.get("paramValue")));
				System.out.println(collect2);	
				PremInsuredNomineeDetails premInsuredNomineeDetails = new PremInsuredNomineeDetails();
				premInsuredNomineeDetails.setAppointeeAge(collect2.get("appointeeage").toString());
		
				premInsuredNomineeDetails.setAppointeeName(collect2.get("appointeename").toString());
				premInsuredNomineeDetails.setAppointeeRelationship(collect2.get("appointeerelationship").toString());
				premInsuredNomineeDetails.setNomineeAge(collect2.get("age").toString());
				premInsuredNomineeDetails.setNomineeDob(collect2.get("dateofbirth").toString());
				premInsuredNomineeDetails.setNomineeName(collect2.get("nomineename").toString());
				premInsuredNomineeDetails.setNomineePercentage(collect2.get("nominee%").toString());
				premInsuredNomineeDetails.setNomineeRelationship(collect2.get("relationshipwithproposer").toString());
				properNomineeDetails.add(premInsuredNomineeDetails);
			}
//			Map<Any, Any> collect = policyDetailResponse.get("policyDetails").get("policyProperty").get("multiSetProperty").asList().stream().
//					filter(a->a.get("multiSetName").toString().equalsIgnoreCase("Nominee Detail")).collect(Collectors.toMap(a->(a.get("multiSetDetail",0).get("property").get("paramName")),(a->a.get("multiSetDetail",0).get("property").get("paramValue"))));
//			System.out.println(collect);
			policy.setProperNomineeDetails(properNomineeDetails);
		}
		
		 public  void getPreviousPolicyDetails(Any policyDetailResponse,PremPolicyDetails policy) throws ParseException {
			 if(policyDetailResponse.get("policyDetails").keys().contains("previousPolicyDetails"))
				{
			 List<Any> policyPropertyMSDetails = policyDetailResponse.get("policyDetails").get("previousPolicyDetails").asList().stream().collect(Collectors.toList());
			 
			 Iterator<Any> policyPropertyMSDetaillist = policyPropertyMSDetails.iterator();
			 List<PremPreviousPolicyDetails> previousPolicyDetails = new ArrayList<PremPreviousPolicyDetails>();
			    while (policyPropertyMSDetaillist.hasNext()) {
			    	Any policyPropertyMSDetail = (Any) policyPropertyMSDetaillist.next();
			 String policyNumber="";
			 String productCode="";
			 String productName="";
			 String policyFromDate="";
			 String policyToDate="";
			 String sumInsured="";
			 String totalPremium="";
			 System.out.println(policyPropertyMSDetail);
			 System.out.println("****************************************************************");
			 
			 PremPreviousPolicyDetails premPreviousPolicyDetails = new PremPreviousPolicyDetails();	 
			 if(policyPropertyMSDetail.keys().contains("policyNumber"))
				{
				 policyNumber = policyPropertyMSDetail.get("policyNumber").toString();
				}
			 if(policyPropertyMSDetail.keys().contains("productCode")){
				 productCode=policyPropertyMSDetail.get("productCode").toString();
			 }
			 if(policyPropertyMSDetail.keys().contains("productName")){
				 productName=policyPropertyMSDetail.get("productName").toString();
			 }
			 if(policyPropertyMSDetail.keys().contains("policyInceptionDate")){
				 policyFromDate=policyPropertyMSDetail.get("policyInceptionDate").toString();
			 }
			 if(policyPropertyMSDetail.keys().contains("policyExpiryDate")){
				 policyToDate=policyPropertyMSDetail.get("policyExpiryDate").toString();
			 }
			 if(policyPropertyMSDetail.keys().contains("sumInsured")){
				 sumInsured=policyPropertyMSDetail.get("sumInsured").toString();
			 }
			 if(policyPropertyMSDetail.keys().contains("totalPremium")){
				 totalPremium=policyPropertyMSDetail.get("totalPremium").toString();
			 }
			 System.out.println("policyNumber : "+policyNumber);
			 System.out.println("productCode : "+productCode);
			 System.out.println("productName : "+productName);
			 System.out.println("policyInceptionDate : "+policyFromDate);
			 System.out.println("policyExpiryDate : "+policyToDate);
			 System.out.println("sumInsured : "+sumInsured);
			 System.out.println("totalPremium :"+totalPremium);
			 if (isNullandEmpty(policyNumber) || isNullandEmpty(productCode) || isNullandEmpty(productName) || isNullandEmpty(policyFromDate) || isNullandEmpty(policyToDate	) || isNullandEmpty(sumInsured) || isNullandEmpty(totalPremium))
				{
			 premPreviousPolicyDetails.setPolicyNo(policyNumber);
			 premPreviousPolicyDetails.setProductCode(productCode);
			 premPreviousPolicyDetails.setProductName(productName);
			 premPreviousPolicyDetails.setPolicyFromDate(policyFromDate);
			 premPreviousPolicyDetails.setPolicyToDate(policyToDate);
			 premPreviousPolicyDetails.setSumInsured(sumInsured);
			 premPreviousPolicyDetails.setPremium(totalPremium);
			 previousPolicyDetails.add(premPreviousPolicyDetails);
			 policy.setPreviousPolicyDetails(previousPolicyDetails);
			 }
				}
				}
			 }
		 
		 private void getendorsementDetail(Any policyDetailResponse,PremPolicyDetails policy) {

			 if(policyDetailResponse.get("policyDetails").keys().contains("endorsementDetails"))
			 {
				 List<Any> endorsementDetailslist = policyDetailResponse.get("policyDetails").get("endorsementDetails").asList();
				 Iterator<Any> endorsementDetailsIterator = endorsementDetailslist.iterator();
				 if(!endorsementDetailslist.isEmpty())
				 {
					 List<PremEndorsementDetails> premEndorsementDetailsList = new ArrayList<PremEndorsementDetails>();
					 while(endorsementDetailsIterator.hasNext())
					 {
						 PremEndorsementDetails premEndorsementDetails = null;
						 String endorsementNumber="";
						 String endorsementEffectiveDate="";
						 String endorsementPremium="";
						 String endorsementSumInsured="";
						 String endorsementRevisedSumInsured="";
						 String typeofEndorsement="";
						 String remarks="";
						 Any endorsementDetailData = (Any) endorsementDetailsIterator.next();
						 if(endorsementDetailData.keys().contains("endorsementNumber"))
						 {
							 endorsementNumber = endorsementDetailData.get("endorsementNumber").toString();
						 }
						 if(endorsementDetailData.keys().contains("endorsementEffectiveDate"))
						 {
							 endorsementEffectiveDate = endorsementDetailData.get("endorsementEffectiveDate").toString();
						 }
						 if(endorsementDetailData.keys().contains("endorsementPremium"))
						 {
							 endorsementPremium = endorsementDetailData.get("endorsementPremium").toString();
						 }
						 if(endorsementDetailData.keys().contains("endorsementSumInsured"))
						 {
							 endorsementSumInsured = endorsementDetailData.get("endorsementSumInsured").toString();
						 }
						 if(endorsementDetailData.keys().contains("endorsementRevisedSumInsured"))
						 {
							 endorsementRevisedSumInsured = endorsementDetailData.get("endorsementRevisedSumInsured").toString();
						 }
						 if(endorsementDetailData.keys().contains("typeofEndorsement"))
						 {
							 typeofEndorsement = endorsementDetailData.get("typeofEndorsement").toString();
						 }
						 if(endorsementDetailData.keys().contains("remarks"))
						 {
							 remarks = endorsementDetailData.get("remarks").toString();
						 }
						 System.out.println("****************************************************************");
						 System.out.println("EndorsementNumber:" + endorsementNumber);
						 System.out.println("EndorsementEffectiveDate:" + endorsementEffectiveDate);
						 System.out.println("EndorsementPremium:" + endorsementPremium);
						 System.out.println("EndorsementSumInsured:" + endorsementSumInsured);
						 System.out.println("EndorsementRevisedSumInsured:" + endorsementRevisedSumInsured);
						 System.out.println("TypeofEndorsement:" + typeofEndorsement);
						 System.out.println("Remarks:" + typeofEndorsement);
						 System.out.println("****************************************************************");
						 if (isNullandEmpty(endorsementNumber) || isNullandEmpty(endorsementEffectiveDate) || isNullandEmpty(endorsementPremium) || isNullandEmpty(endorsementSumInsured) || isNullandEmpty(endorsementRevisedSumInsured) || isNullandEmpty(typeofEndorsement) || isNullandEmpty(remarks))
						 {
							 premEndorsementDetails = new PremEndorsementDetails();
							 premEndorsementDetails.setEndNo(endorsementNumber);
							 premEndorsementDetails.setEndEffFmDt(endorsementEffectiveDate);
							 premEndorsementDetails.setEndPremium(endorsementPremium);
							 premEndorsementDetails.setEndSumInsured(endorsementSumInsured);
							 premEndorsementDetails.setEndRevisedSumInsured(endorsementRevisedSumInsured);
							 premEndorsementDetails.setEndType(typeofEndorsement);
							 premEndorsementDetails.setEndText(remarks);
							 premEndorsementDetailsList.add(premEndorsementDetails);
						 }
					 }
					 policy.setEndorsementDetails(premEndorsementDetailsList);
				 }
			 }
		 }
		 
		 public void getBankDetails(Any policyDetailResponse,PremPolicyDetails policy) throws ParseException {

				if(policyDetailResponse.get("policyDetails").keys().contains("policyProperty"))
				{
					List<Any> policyInsuredDetail = policyDetailResponse.get("policyDetails").get("insuredDetails").asList();
					System.out.println("****************************************************************");
					if(policyInsuredDetail.get(0).keys().contains("memberDetails"))
					{
						Any insuredMemberDetails = policyInsuredDetail.get(0).get("memberDetails");
						if(insuredMemberDetails.get("partyProperty").keys().contains("multiSetProperty") && insuredMemberDetails.get("partyProperty").get("multiSetProperty").get(0).keys().contains("multiSetName"))
						{
							List<Any> policyBankDetail = insuredMemberDetails.get("partyProperty").get("multiSetProperty").asList().stream()
									.filter(a -> a.get("multiSetName").toString().equalsIgnoreCase("Bank Details"))
									.collect(Collectors.toList());
							if(!policyBankDetail.isEmpty())
							{
								Iterator<Any> BankDetailIterator = policyBankDetail.get(0).get("multiSetDetail").iterator();
								List<PremBankDetails> PremBankDetailsList = new ArrayList<PremBankDetails>();
								while(BankDetailIterator.hasNext())
								{
									String nameasperbankac="";
									String bankaccountnumber="";
									String accounttype="";
									String startdate="";
									String enddate="";
									String ifsccode="";
									String preference="";
									String bankname="";
									String branchname="";
									Any previousInsuranceDetail = (Any) BankDetailIterator.next();
									List<Any> previousInsurancevaluelist=previousInsuranceDetail.get("property").asList();
									Iterator<Any> previousInsurancevalueIterator = previousInsurancevaluelist.iterator();
									while(previousInsurancevalueIterator.hasNext())
									{
										Any InsuranceBankdata = (Any) previousInsurancevalueIterator.next();
										String ParamName = InsuranceBankdata.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]", "").toLowerCase();
										switch(ParamName)
										{
										case "nameasperbankac":
											nameasperbankac=InsuranceBankdata.get("paramValue").toString();
											System.out.println("NameAsPerBankAc: "+nameasperbankac);
											break;
										case "bankaccountnumber":
											bankaccountnumber=InsuranceBankdata.get("paramValue").toString();
											System.out.println("BankAccountNumber: "+bankaccountnumber);
											break;
										case "accounttype":
											accounttype=InsuranceBankdata.get("paramValue").toString();
											System.out.println("AccountType: "+accounttype);
											break;
										case "startdate":
											startdate=InsuranceBankdata.get("paramValue").toString();
											System.out.println("StartDate: "+startdate);
											break;
										case "enddate":
											if(!isNullOrEmpty(InsuranceBankdata.get("paramValue").toString())){
												enddate=InsuranceBankdata.get("paramValue").toString();
												System.out.println("EndDate: "+enddate);
											}
											break;
										case "ifsccode":
											ifsccode=InsuranceBankdata.get("paramValue").toString();
											System.out.println("IFSCCode: "+ifsccode);
											break;
										case "preference":
											preference=InsuranceBankdata.get("paramValue").toString();
											System.out.println("Preference: "+preference);
											break;
										case "bankname":
											bankname=InsuranceBankdata.get("paramValue").toString();
											break;
										case "branchname":
											branchname=InsuranceBankdata.get("paramValue").toString();
											break;
										}
									}
									System.out.println("****************************************************************");
									PremBankDetails premBankDetails = new PremBankDetails();
									premBankDetails.setAccountNumber(bankaccountnumber);
									premBankDetails.setNameAsPerBank(nameasperbankac);
									premBankDetails.setAccountType(accounttype);
									premBankDetails.setEffectiveFrom(startdate);
									premBankDetails.setEffectiveTo(enddate);
									premBankDetails.setOthers(preference);
									premBankDetails.setBankName(bankname);
									premBankDetails.setBranchName(branchname);
									PremBankDetailsList.add(premBankDetails);
								}
								policy.setBankDetails(PremBankDetailsList);
							}
						}
					}
				}	
			}
		 
		 public static boolean isNullOrEmpty(String str) {
		        if(str != null && !str.isEmpty() && !str.equalsIgnoreCase("null"))
		        {
		            return false;
		        }
		        else
		        {
		        return true;
		        }
			}
		 
		 public static boolean isNullandEmpty(String str) {
		        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null"))
		        {
		            return false;
		        }
		        else
		        {
		        return true;
		        }
			}
		
		
		public Long getBancsServiceTransactionalId(){
			Long nextVal = 0l;
			Connection connection = null;
			Statement sqlStatement = null;
			ResultSet rs = null;
			try {
				connection = BPMClientContext.getConnection();
				sqlStatement = connection.createStatement();
				rs = sqlStatement.executeQuery("Select SEQ_BaNCS_CMN_TRANS_KEY.NEXTVAL from dual");
				if (rs.next()) {
					int inextVal = rs.getInt(1);
					nextVal = Long.valueOf(inextVal);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
					if (rs != null) {
						rs.close();
					}
					if (sqlStatement != null) {
						sqlStatement.close();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Bancs T Id :"+nextVal);
			return nextVal;
		}
		
		public BancsHeaderDetails getRequestHeaderValues(String argParam){
			/*Query query = entityManager.createNamedQuery("BancsHeaderDetails.findByRequestType");
			query.setParameter("type", argParam);
			List<BancsHeaderDetails> singleResult = (List<BancsHeaderDetails>) query.getResultList();
			if(singleResult != null && ! singleResult.isEmpty()) {
				entityManager.refresh(singleResult.get(0));
				return singleResult.get(0);
			}
			return null;*/		
			
			BancsHeaderDetails dto = null;
			Connection connection = null;
			try {
				connection = BPMClientContext.getConnection();
				if(null != connection) {
					String fetchQuery = "SELECT WS_TRANS_ID_SEQ,WS_BUSSINESS_CHANNEL,WS_USER_CODE,WS_ROLE_CODE,WS_USER_NAME,WS_PASSWORD FROM MAS_BANCS_WS_USER_DTLS WHERE WS_REQUEST_TYPE = ?";
					PreparedStatement preparedStatement = connection.prepareStatement(fetchQuery);
					preparedStatement.setString(1, argParam);
					if(null != preparedStatement) {
						ResultSet rs = preparedStatement.executeQuery();
						if(null != rs) {
							while (rs.next()) {
								dto = new BancsHeaderDetails();
								dto.setWebserviceSeq(rs.getString(1));
								dto.setWebserviceBC(rs.getString(2));
								dto.setWebserviceUserCode(rs.getString(3));
								dto.setWebserviceRoleCode(rs.getString(4));
								dto.setWsUserName(rs.getString(5));
								dto.setWsPassword(rs.getString(6));
							}
						}
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return dto;
		}
		
		public static void main(String[] args) {
			BancsSevice bsObj = BancsSevice.getInstance();
			bsObj.getPolicyDetailsMethod("");
		}
		
		public String getAdjustPolicyInstallment(BancsAdjustPolicyInstallemtRequest request)
		{
			BancsDBService searchService = new BancsDBService();
			String installmentresponse = "";
			BancsAdjustPolicyInstallemtResponse response = null;
			Gson gson = new Gson();

			try{

				String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
				if(!serviceURL.endsWith("/")){
					serviceURL = serviceURL+"/"+"adjustPolicyInstallment";
				}else{
					serviceURL = serviceURL+"adjustPolicyInstallment";
				}
				System.out.println("Bancs Live - Url :"+serviceURL);
				URL url=new URL(serviceURL);
				HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
				urlCon.setRequestMethod("POST");
				urlCon.setRequestProperty("Content-Type", "application/json");
				urlCon.setRequestProperty("Accept", "application/json");
				Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
				urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
				request.setServiceTransactionId(mapVal.get("tranid"));
				request.setBusinessChannel(mapVal.get("business"));
				request.setUserCode(mapVal.get("usercode"));
				request.setRoleCode(mapVal.get("rolecode"));
				ObjectMapper mapper = new ObjectMapper();
				String requestString = mapper.writeValueAsString(request); 
				log.info("Adjust Policy Installment Request : "+requestString);
				System.out.println("Bancs Live - Request:"+requestString);
				urlCon.setDoOutput(true);
				OutputStream os = urlCon.getOutputStream();
				os.write(requestString.toString().getBytes());
				os.flush();
				os.close();
				IntegrationLogTable intLog = new IntegrationLogTable();
		        intLog.setOwner("Bancs");
		        intLog.setServiceName("adjustPolicyInstallment");
		        intLog.setUrl(serviceURL);
		        intLog.setRequest(requestString);
		        intLog.setCreatedBy("GALAXY");
		        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		        intLog.setActiveStatus(1l);
				log.info("Adjust Policy Installment API URL Response Code : "+urlCon.getResponseCode());

				boolean isErrorCodeReceived = false;


				if (urlCon.getResponseCode() != 200) {
					isErrorCodeReceived = true;
					intLog.setStatus("Error");
					intLog.setResponse("Connection Error");
					intLog.setRemarks("Connection Error");
				}else if (urlCon.getResponseCode() == 200) {
					InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
					BufferedReader br = new BufferedReader(in);
					String output = "";
					while ((output = br.readLine()) != null) {
						log.info("Adjust Policy Installment Response :  :"+output);
						response = gson.fromJson(output,BancsAdjustPolicyInstallemtResponse.class);
						String resDtls = output;
						if(resDtls != null && resDtls.length()>3000){
							intLog.setResponse(resDtls.substring(0, 2999));
						}else {
							intLog.setResponse(resDtls);
						}
						if(response.getErrorCode().equals("0"))
						{
							installmentresponse=response.getResult();
							System.out.println("Bancs Live - Response: Success");
							intLog.setStatus("Success");
						}
						else
						{
							log.info("Adjust Policy Installment Error Response :  :"+response.toString());
							System.out.println("Bancs Live - Response: Error");
							intLog.setStatus("Error");
						}
					}
				}
				DBCalculationService bancsService = new DBCalculationService();;
				bancsService.insertIntegrationLogTable(intLog);
				urlCon.disconnect();

			}catch(Exception exp){
				log.error("Exception occurred while calling this service"+exp.getMessage());
				exp.printStackTrace();
			}

			return  installmentresponse;

		}
		
		public String getPolicyInstAmountDetails(String policyNumber)
		{
		BancsDBService searchService = new BancsDBService();
		String installmentresponse = "";
		BancsPolicyInstAmountResponse response = null;
		Gson gson = new Gson();

		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getPolicyInstAmount";
			}else{
				serviceURL = serviceURL+"getPolicyInstAmount";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			LockPolicyIntegrationDTO lpolicy=new LockPolicyIntegrationDTO();
			lpolicy.setServiceTransactionId(mapVal.get("tranid"));
			lpolicy.setBusinessChannel(mapVal.get("business"));
			lpolicy.setUserCode(mapVal.get("usercode"));
			lpolicy.setRoleCode(mapVal.get("rolecode"));
			lpolicy.setPolicyNo(policyNumber != null ? policyNumber :"");
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+lpolicy.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+lpolicy.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+lpolicy.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+lpolicy.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+lpolicy.getPolicyNo()+"\"" 
					+"}";
					
			JSONObject json = new JSONObject(POST_PARAMS);
			log.info("Policy Inst Amount Details Request : "+POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getPolicyInstAmount");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(POST_PARAMS);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			boolean isErrorCodeReceived = false;

			log.info("Policy Inst Amount Details API URL Response Code : "+urlCon.getResponseCode());

			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
				intLog.setStatus("Error");
				intLog.setResponse("Connection Error");
				intLog.setRemarks("Connection Error");
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Policy Inst Amount Details Response :"+output);
					response = gson.fromJson(output,BancsPolicyInstAmountResponse.class);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
		            if(response.getErrorCode().equals("0"))
					{
		            	intLog.setStatus("Success");
		            	installmentresponse=response.getPendingInstallmentAmount();
		            	System.out.println("Bancs Live - Response: Success");
					} else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
			urlCon.disconnect();
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}
		return  installmentresponse;

		}
		
		public List<GetInstallment64VBStatusResponse> getInstallment64VBStatus(String policyNumber)
		{
		BancsDBService searchService = new BancsDBService();
		String installmentresponse = "";
		BancsInstallment64VBStatusResponse response = null;
		List<GetInstallment64VBStatusResponse> getInstallment64VBStatusDetails = new ArrayList<GetInstallment64VBStatusResponse>();
		Gson gson = new Gson();

		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getInstallment64VBStatus";
			}else{
				serviceURL = serviceURL+"getInstallment64VBStatus";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			LockPolicyIntegrationDTO lpolicy=new LockPolicyIntegrationDTO();
			lpolicy.setServiceTransactionId(mapVal.get("tranid"));
			lpolicy.setBusinessChannel(mapVal.get("business"));
			lpolicy.setUserCode(mapVal.get("usercode"));
			lpolicy.setRoleCode(mapVal.get("rolecode"));
			lpolicy.setPolicyNo(policyNumber != null ? policyNumber :"");
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+lpolicy.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+lpolicy.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+lpolicy.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+lpolicy.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+lpolicy.getPolicyNo()+"\"" 
					+"}";
					
			JSONObject json = new JSONObject(POST_PARAMS);
			log.info("Installment 64VBStatus Request : "+POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			
			log.info("Installment 64VBStatus API URL Response Code : "+urlCon.getResponseCode());
			
			boolean isErrorCodeReceived = false;
			
			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Installment 64VBStatus Response :"+output);
					response = gson.fromJson(output,BancsInstallment64VBStatusResponse.class);
		            if(response.getErrorCode().equals("0"))
					{
		            	System.out.println("Bancs Live - Response: Success");
		            	List<BancsInstallmentDetail> installmentList= response.getInstallmentDetails();
		            	if(installmentList !=null && installmentList.size() >0)
		            	{
		            	for (BancsInstallmentDetail installDetails : installmentList) {
		            		GetInstallment64VBStatusResponse details =new GetInstallment64VBStatusResponse();
		            		details.setChqDate(installDetails.getChequeDate());
		            		details.setInstallmentDate(installDetails.getInstallmentDate());
		            		details.setInstallmentNo(installDetails.getInstallmentNo());
		            		details.setInstallmentStatus(installDetails.getInstallmentStatus());
		            		details.setInstAmt(installDetails.getInstallmentAmount());
		            		details.setInstBank(installDetails.getInstBank());
		            		details.setInstNo(installDetails.getInstNo());
		            		details.setInstStatus(installDetails.getInstStatus());
		            		details.setInstType(installDetails.getInstType());
		            		getInstallment64VBStatusDetails.add(details);
		            	}
		            	}
					}else {
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			urlCon.disconnect();
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}
		return getInstallment64VBStatusDetails;

		}
		
		public List<PremPolicyInstallmentDetails> getPolicyInstallmentDetails (String policyNumber)
		{
		BancsDBService searchService = new BancsDBService();
		String installmentresponse = "";
		List<PremPolicyInstallmentDetails> getPolicyInstallmentDetails = new ArrayList<PremPolicyInstallmentDetails>();
		BancsInstallmentDetailsResponse response = null;
		Gson gson = new Gson();

		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
			if(!serviceURL.endsWith("/")){
				serviceURL = serviceURL+"/"+"getInstallmentDetails";
			}else{
				serviceURL = serviceURL+"getInstallmentDetails";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = searchService.getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			LockPolicyIntegrationDTO lpolicy=new LockPolicyIntegrationDTO();
			lpolicy.setServiceTransactionId(mapVal.get("tranid"));
			lpolicy.setBusinessChannel(mapVal.get("business"));
			lpolicy.setUserCode(mapVal.get("usercode"));
			lpolicy.setRoleCode(mapVal.get("rolecode"));
			lpolicy.setPolicyNo(policyNumber != null ? policyNumber :"");
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+lpolicy.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+lpolicy.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+lpolicy.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+lpolicy.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+lpolicy.getPolicyNo()+"\"" 
					+"}";
					
			JSONObject json = new JSONObject(POST_PARAMS);
			log.info("Installment Details Request : "+POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os = urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getInstallmentDetails");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(POST_PARAMS);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			
			boolean isErrorCodeReceived = false;
			log.info("Installment Details API URL Response Code : "+urlCon.getResponseCode());
			
			if (urlCon.getResponseCode() != 200) {
				isErrorCodeReceived = true;
				intLog.setStatus("Error");
				intLog.setResponse("Connection Error");
				intLog.setRemarks("Connection Error");
			}else if (urlCon.getResponseCode() == 200) {
				InputStreamReader in = new InputStreamReader(urlCon.getInputStream());
				BufferedReader br = new BufferedReader(in);
				String output = "";
				while ((output = br.readLine()) != null) {
					log.info("Installment Details Response :"+output);
					response = gson.fromJson(output,BancsInstallmentDetailsResponse.class);
					String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
		            if(response.getErrorCode().equals("0"))
					{
		            	intLog.setStatus("Success");
		            	System.out.println("Bancs Live - Response: Success");
		            	List<InstallmentDetailDTO> installmentList= response.getInstallmentDetails();
		            	if(installmentList !=null && installmentList.size() > 0)
		            	{
		            		for (InstallmentDetailDTO installmentDetails : installmentList) {
		            			PremPolicyInstallmentDetails details = new PremPolicyInstallmentDetails();
		            			details.setInstallmentAmount(installmentDetails.getInstAmount());
		            			details.setInstallmentDueDate(installmentDetails.getInstDueDate());
		            			details.setInstalmentStatus(installmentDetails.getInstStatus());
		            			details.setInstCollectionDate(installmentDetails.getInstCollectionDate());
		            			details.setInstNo(installmentDetails.getInstNumber());
		            			details.setInstScrollDate(installmentDetails.getInstScrollDate());
		            			details.setTaxAmount(installmentDetails.getTaxAmount());
		            			details.setTotalInstAmount(installmentDetails.getTotalInstAmount());
		            			getPolicyInstallmentDetails.add(details);
		            		}
		            	}
					}else {
						intLog.setStatus("Error");
						System.out.println("Bancs Live - Response: Error");
					}
				}
			}
			DBCalculationService bancsService = new DBCalculationService();;
			bancsService.insertIntegrationLogTable(intLog);
			urlCon.disconnect();
			
		}catch(Exception exp){
			log.error("Exception occurred while calling this service"+exp.getMessage());
			exp.printStackTrace();
		}

		return getPolicyInstallmentDetails;

		}
		
		public Boolean isPolicydetailspulled(String policyNo) {
            Boolean isSuccess = false;
            try
            {
                    OutputStream os =null;
                    String serviceURL = BPMClientContext.BANCS_OP_POLICY_PULL_API;
                    String authName = BPMClientContext.API_URL_AUT_NAME;
                    String authPwd = BPMClientContext.API_URL_AUT_PWD;
//        			serviceURL = serviceURL+"getInstallmentDetails";
                    serviceURL = serviceURL+policyNo;
                    URL url = new URL(serviceURL); 
                    log.info("calling claimsApi bancs policy pull Service");
                    byte[] val = (authName+":"+authPwd).getBytes();
        			String authString = new String(Base64.encodeBase64(val));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Authorization", authString);
                    conn.setDoOutput(true);        
                    conn.setDoInput(true);        
                    log.info("Bancs Policy pull API URL Response Code : "+conn.getResponseCode());

                    if (conn.getResponseCode() != 200) {
                    		isSuccess = false;
                            System.out.println("Status: Error - "+conn.getResponseCode() +" ResponseMessage: "+ conn.getResponseMessage());
                            log.error("Status: Error - "+conn.getResponseCode() +" ResponseMessage: "+ conn.getResponseMessage());
                    }else if (conn.getResponseCode() == 200) {
                            InputStreamReader in = new InputStreamReader(conn.getInputStream());
                            BufferedReader br = new BufferedReader(in);
                            String output = "";
                            if ((output = br.readLine()) != null) {
                            	log.info("Response from API"+output);
                            }
                            isSuccess = true;
                    }
            }catch(Exception e)
            {
                    e.printStackTrace();
                    log.error("Exception :" +e.getMessage());
                    isSuccess = false;
            }
            return isSuccess;
            
    }
}
