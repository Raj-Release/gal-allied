package com.shaic.restservices.bancs.consumerDetails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.policy.search.ui.BancsSevice;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTableDTO;
import com.shaic.domain.BancsHeaderDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.restservices.bancs.lockPolicy.LockPolicyIntegrationService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.itextpdf.text.log.SysoCounter;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

public class BancsConsumerDetailService {
	
	private static final Logger log = LoggerFactory.getLogger(BancsConsumerDetailService.class);
	
	private static final BancsConsumerDetailService instance = new BancsConsumerDetailService();
	
	public static BancsConsumerDetailService getInstance() {
		return instance;
	}

	public BancsConsumerDetailService() {
		
	}
	
	public List<ZUAViewQueryHistoryTableDTO> getQCZonalAuditQueryDetails(String policyNumber){
		
		String jsonInString = "";
		List<ZUAViewQueryHistoryTableDTO> zUAViewQueryHistoryTableDTOList = new ArrayList<ZUAViewQueryHistoryTableDTO>();
		try{
			
			String serviceURL = BPMClientContext.BANCS_WEB_SERVICE_UW_URL;
			if(!serviceURL.trim().endsWith("/")){
				serviceURL = serviceURL+"/"+"/getQCZonalAuditQueries";
			}else{
				serviceURL = serviceURL.trim()+"getQCZonalAuditQueries";
			}
			System.out.println("Bancs Live - Url :"+serviceURL);
			URL url=new URL(serviceURL);
			HttpURLConnection urlCon=(HttpURLConnection) url.openConnection();
			urlCon.setRequestMethod("POST");
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			Map<String, String> mapVal = getAuthAndRequestParam("ALL");
			urlCon.setRequestProperty("Authorization", mapVal.get("authkey"));
			
			GetQCZonalAuditQueryDTO cgetQCzonal=new GetQCZonalAuditQueryDTO();
			
			cgetQCzonal.setServiceTransactionId(mapVal.get("tranid"));
			cgetQCzonal.setBusinessChannel(mapVal.get("business"));
			cgetQCzonal.setUserCode(mapVal.get("usercode"));
			cgetQCzonal.setRoleCode(mapVal.get("rolecode"));
			cgetQCzonal.setPolicyNo(policyNumber != null? policyNumber :""/*"012HFHOSTD401388137000000"*/); 
			
			
			String POST_PARAMS = "{" 
					+ "\"serviceTransactionId\":"+ "\""+cgetQCzonal.getServiceTransactionId()+"\"," 
					+ "\"businessChannel\":"+ "\""+cgetQCzonal.getBusinessChannel()+"\"," 
					+ "\"userCode\":"+ "\""+cgetQCzonal.getUserCode()+"\"," 
					+ "\"roleCode\":"+ "\""+cgetQCzonal.getRoleCode()+"\"," 
					+ "\"policyNumber\":"+ "\""+cgetQCzonal.getPolicyNo()+"\"" 
					+"}";
			
			JSONObject json=new JSONObject(POST_PARAMS);
			System.out.println("Bancs Live - Request:"+POST_PARAMS);
			log.info(POST_PARAMS);
			urlCon.setDoOutput(true);
			OutputStream os=urlCon.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();
			IntegrationLogTable intLog = new IntegrationLogTable();
	        intLog.setOwner("Bancs");
	        intLog.setServiceName("getQCZonalAuditQueries");
	        intLog.setUrl(serviceURL);
	        intLog.setRequest(POST_PARAMS);
	        intLog.setCreatedBy("GALAXY");
	        intLog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
	        intLog.setActiveStatus(1l);
			
			GetQCZonalAuditQueryResponse response=null;
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
					JSONObject jsonObject = new JSONObject(output);
                    jsonInString = jsonObject.toString();
                    log.info("Response :"+jsonInString);
                    String resDtls = output;
					if(resDtls != null && resDtls.length()>3000){
						intLog.setResponse(resDtls.substring(0, 2999));
					}else {
						intLog.setResponse(resDtls);
					}
					
					//response = mapper.readValue(jsonInString, GetQCZonalAuditQueryResponse.class);
					//ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

					Any any = JsonIterator.deserialize(jsonInString);
					any.get("queries",0).get("multiSetDetail"); // extract out the first name from all items
//					System.out.println(any.get("queries",0).get("multiSetDetail",0).get("property").asList());

					List<Any> asList = any.get("queries",0).get("multiSetDetail",0).get("property").asList();
					Map<Any, Any> collect = asList.stream().collect(Collectors.toMap(x->(x.get("paramName")), x-> x.get("paramValue")));
//					System.out.println(collect);
					
					Iterator<Any> iterator1 = any.get("queries",0).get("multiSetDetail").iterator();
					while (iterator1.hasNext()) {
						ZUAViewQueryHistoryTableDTO zUAViewQueryDetails = new ZUAViewQueryHistoryTableDTO();
						Any any5 = (Any) iterator1.next();
						Map<String, Any> collect2 = any5.get("property").asList().stream().collect(Collectors.toMap(x->(x.get("paramName").toString().replaceAll("[\\-\\+\\.\\^\\:\\,\\ \\(\\)]","").toLowerCase()), x-> x.get("paramValue")));					
						System.out.println(any5.get("sequenceNo").toString());
						System.out.println(collect2);
						collect2.forEach((k,v)->{
							System.out.println("Name : " + k + " Value : " + v);
							       
							if("querycode".equals(k)){
								zUAViewQueryDetails.setQueryCode(v.toString());
							}
							if("querydescription".equals(k)){
								zUAViewQueryDetails.setQueryDescription(v.toString());
							}
							if("querytype".equals(k)){
								zUAViewQueryDetails.setType(v.toString());
							}
							if("serialno".equals(k)){
								zUAViewQueryDetails.setSerialNo(v.toString());
							}
							if("repliedbyuser".equals(k)){
								zUAViewQueryDetails.setRepliedByUser(v.toString());
							}
							if("querydetails".equals(k)){
								zUAViewQueryDetails.setQueryDetails(v.toString());
							}
							if("raisedbyrole".equals(k)){
								zUAViewQueryDetails.setRaisedByRole(v.toString());
							}
							if("raisedbyuser".equals(k)){
								zUAViewQueryDetails.setRaisedByUser(v.toString());
							}
							if("replydateandtime".equals(k)){
								zUAViewQueryDetails.setRepliedDate(v.toString());
							}
							if("queryreply".equals(k)){
								zUAViewQueryDetails.setQueryreply(v.toString());
							}
							if("repliedbyrole".equals(k)){
								zUAViewQueryDetails.setRepliedByRole(v.toString());
							}
							if("dateandtime".equals(k)){
								zUAViewQueryDetails.setDateandTime(v.toString());
							}
							if("querystatus".equals(k)){
								zUAViewQueryDetails.setQuerStatus(v.toString());
							}
						});
						zUAViewQueryHistoryTableDTOList.add(zUAViewQueryDetails);
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
		System.out.println(zUAViewQueryHistoryTableDTOList.size());
		return zUAViewQueryHistoryTableDTOList;
	}

	public static Map<String, String> getAuthAndRequestParam(String argRequestType) {
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
			log.info("Auth and request parameter going wrong configure bancs user dtls tbl");
			return null;
		}
	}

}
