/**
 * 
 */
package com.shaic.restservices.bancs.claimprovision.policydetail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class BANCSTestClass {
	
	public static void main(String args[]) {
		try{
			Map<String, Object> jsonValue = new LinkedHashMap<String, Object>();
			JSONObject jsonObj = new JSONObject("{\n   \"errorCode\":0,\n   \"errorDescription\":\"Success\",\n   \"policyDetails\":{\n      \"policyNumber\":\"012HFHOSTD401386879000000\",\n      \"productCode\":\"HFHOSTD4\",\n      \"policyInceptionDate\":\"22/06/2019 11:26:28\",\n      \"policyExpiryDate\":\"21/06/2020 00:00:00\",\n      \"policyProperty\":{\n         \"simpleProperty\":[\n            {\n               \"paramName\":\"Sum Insured Currency\",\n               \"paramValue\":\"INR\"\n            },\n            {\n               \"paramName\":\"Sum Insured Currency Exchange Rate\",\n               \"paramValue\":\"1\"\n            }\n         ],\n         \"multiSetProperty\":[\n            {\n               \"multiSetName\":\"Nominee Detail\",\n               \"multiSetDetail\":[\n                  {\n                     \"sequenceNo\":\"1\",\n                     \"property\":[\n                        {\n                           \"paramName\":\"Serial No\",\n                           \"paramValue\":\"1\"\n                        },\n                        {\n                           \"paramName\":\"Nominee Name\",\n                           \"paramValue\":\"SOMA\"\n                        }\n                     ]\n                  }\n               ]\n            },\n            {\n               \"multiSetName\":\"Cover SI Details\",\n               \"multiSetDetail\":[\n                  {\n                     \"sequenceNo\":\"1\",\n                     \"property\":[\n                        {\n                           \"paramName\":\"covercode\",\n                           \"paramValue\":\"RTAADDLSI\"\n                        },\n                        {\n                           \"paramName\":\"Cover Sum Insured\",\n                           \"paramValue\":\"125000\"\n                        }\n                     ]\n                  },\n                  {\n                     \"sequenceNo\":\"2\",\n                     \"property\":[\n                        {\n                           \"paramName\":\"covercode\",\n                           \"paramValue\":\"ASSTREPROD\"\n                        },\n                        {\n                           \"paramName\":\"Cover Sum Insured\",\n                           \"paramValue\":\"100000\"\n                        }\n                     ]\n                  },\n                  {\n                     \"sequenceNo\":\"3\",\n                     \"property\":[\n                        {\n                           \"paramName\":\"covercode\",\n                           \"paramValue\":\"AUTORESTR1\"\n                        },\n                        {\n                           \"paramName\":\"Cover Sum Insured\",\n                           \"paramValue\":\"0\"\n                        }\n                     ]\n                  }\n               ]\n            }\n         ]\n      },\n      \"proposerDetails\":{\n         \"partyCode\":\"PI19488361\",\n         \"nationality\":\"13\",\n         \"partyAddress\":[\n            {\n               \"addressType\":\"Permanent\",\n               \"address1\":\" Postmaster, Post Office B.C. ROAD (SUB OFFICE), PATNA, BIHAR (BR), India (IN), Pin Code:- 800001\",\n               \"country\":\"INDIA\",\n               \"state\":\"52\",\n               \"city\":\"1316200\",\n               \"addressProperty\":[\n                  {\n                     \"paramName\":\"Secondary Fax\",\n                     \"paramValue\":\"\"\n                  },\n                  {\n                     \"paramName\":\"Location Type\",\n                     \"paramValue\":\"Metro\"\n                  }\n               ]\n            }\n         ]\n      }\n   }\n}");
			jsonValue.put("errorCode", jsonObj.get("errorCode"));
			jsonValue.put("errorDescription", jsonObj.get("errorDescription"));
			
			JSONObject policyDetailObj = jsonObj.getJSONObject("policyDetails");
			if(policyDetailObj != null){
				jsonValue.put("policyNumber", policyDetailObj.get("policyNumber"));
				jsonValue.put("productCode", policyDetailObj.get("productCode"));
				jsonValue.put("policyInceptionDate", policyDetailObj.get("policyInceptionDate"));
				jsonValue.put("policyExpiryDate", policyDetailObj.get("policyExpiryDate"));
				
				JSONObject policyProperty = policyDetailObj.getJSONObject("policyProperty");
				if(policyProperty != null){
					// simpleProperty iteration
					JSONArray simplePropertyArr = policyProperty.getJSONArray("simpleProperty");
					if(simplePropertyArr != null && simplePropertyArr.length() > 0){
						for(int i =0; i < simplePropertyArr.length(); i++){
							jsonValue.put(String.valueOf(simplePropertyArr.getJSONObject(i).get("paramName")), simplePropertyArr.getJSONObject(i).get("paramValue"));
						}
					}
					// multiSetProperty iteration
					JSONArray multiSetPropertyArr = policyProperty.getJSONArray("multiSetProperty");
					if(multiSetPropertyArr != null && multiSetPropertyArr.length() > 0){ //jsonValue.get("multiSetProperty")
						for(int i = 0; i < multiSetPropertyArr.length(); i++){
							// multiSetProperty key and value will be same...... think abt it......
							String multiSetName = String.valueOf(((JSONObject)multiSetPropertyArr.get(i)).get("multiSetName"));//String.valueOf(((JSONObject)((JSONArray)jsonValue.get("multiSetProperty")).get(i)).get("multiSetName"));

							//multiSetDetail iteration
							JSONArray multiSetDetailArr = ((JSONArray)((JSONObject)((JSONArray)multiSetPropertyArr).get(i)).get("multiSetDetail"));
							if(multiSetDetailArr != null && multiSetDetailArr.length() > 0){
								String seqVal = "";
								List<Object> arryValues = null;
								String tmpVal = "";
								for(int k = 0 ; k < multiSetDetailArr.length(); k++){
									JSONObject multiSetDetailArrObj = (JSONObject)multiSetDetailArr.get(k);
									seqVal = multiSetDetailArrObj.getString("sequenceNo");
									JSONArray propertyArr = ((JSONArray)((JSONObject)multiSetDetailArr.get(k)).getJSONArray("property"));
									for(int j = 0; j < propertyArr.length(); j++){
										//JSONArray tempArr = ((JSONArray)jsonValue.get("property"));
										String paramName = String.valueOf(((JSONObject)(propertyArr).get(j)).get("paramName"));
										String paramValue = String.valueOf(((JSONObject)(propertyArr).get(j)).get("paramValue"));
										if(StringUtils.isBlank(paramValue)){
											paramValue = "EMPTY";
										}
										tmpVal += paramName+":"+paramValue;
										if(j < (propertyArr.length()-1)){
											tmpVal += "~";
										}
									}
									if(!tmpVal.equals("")){
										arryValues = new ArrayList<Object>();
										arryValues.add(tmpVal);
										tmpVal = "";
										jsonValue.put(multiSetName+"_"+(seqVal), arryValues);
									}
								}
							}
						}
					}
				}
				
				// proposerDetails iteration
				JSONObject proposerDetails = policyDetailObj.getJSONObject("proposerDetails");
				if(proposerDetails != null){
					jsonValue.put("partyCode", proposerDetails.get("partyCode"));
					jsonValue.put("nationality", proposerDetails.get("nationality"));
					
					// partyAddress iteration
					JSONArray partyAddressArr = proposerDetails.getJSONArray("partyAddress");
					if(partyAddressArr != null && partyAddressArr.length() > 0){
//						JSONArray partyAddressArr = ((JSONArray)((JSONObject)jsonValue.get("proposerDetails")).get("partyAddress"));
						for(int l = 0; l < partyAddressArr.length(); l++){
							JSONObject partyAddressObj = partyAddressArr.getJSONObject(l); //((JSONObject)((JSONArray)partyAddressArr).get(l));
							jsonValue.put("addressType", partyAddressObj.get("addressType"));
							jsonValue.put("address1", partyAddressObj.get("address1"));
							jsonValue.put("country", partyAddressObj.get("country"));
							jsonValue.put("state", partyAddressObj.get("state"));
							jsonValue.put("city", partyAddressObj.get("city"));
							String tmpVal2 = "";
							List<Object> arryValues2 = null;
							// addressProperty iteration
							JSONArray addressPropertyArr = ((JSONArray)((JSONObject)partyAddressArr.get(l)).getJSONArray("addressProperty"));
							if(addressPropertyArr != null){
								for(int j =0; j < addressPropertyArr.length(); j++){
//									JSONArray tempArr2 = ((JSONArray)partyAddressObj.get("addressProperty"));
									String paramName = String.valueOf(((JSONObject)(addressPropertyArr).get(j)).get("paramName"));
									String paramValue = String.valueOf(((JSONObject)(addressPropertyArr).get(j)).get("paramValue"));
									if(StringUtils.isBlank(paramValue)){
										paramValue = "EMPTY";
									}
									tmpVal2 += paramName+":"+paramValue;
									if(j < (addressPropertyArr.length() - 1)){
										tmpVal2 += "~";
									}
								}
								if(!tmpVal2.equals("")){
									arryValues2 = new ArrayList<Object>();
									arryValues2.add(tmpVal2);
									tmpVal2 = "";						
									jsonValue.put(partyAddressObj.get("addressType")+"_"+(l+1), arryValues2);
								}
							}
						}
					}
					
				}
				
			}
			for (Map.Entry<String, Object> entry : jsonValue.entrySet()) {
			    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
			
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}

}
