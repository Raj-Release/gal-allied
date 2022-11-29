package com.shaic.restservices.bancs.claimprovision.policydetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.shaic.restservices.bancs.claimprovision.BankDetails;
import com.shaic.restservices.bancs.claimprovision.BankDetailsResponse;
import com.shaic.restservices.bancs.claimprovision.PartyBankDetails;

public class GetBankDetailsSampleResponseParser {
	public static void main(String[] args) {
		String inputJsonString ="{  \n   \"errorCode\":0,\n   \"errorDescription\":\"Success\",\n   \"policyNumber\":\"012HFHOSTD401388080000000\",\n   \"partyBankDetails\":[  \n      {  \n         \"partyCode\":\"PI19490380\",\n         \"multiSetProperty\":[  \n            {  \n               \"multiSetName\":\"Bank Details\",\n               \"multiSetDetail\":[  \n                  {  \n                     \"sequenceNo\":\"1\",\n                     \"property\":[  \n                        {  \n                           \"paramName\":\"Account Type \",\n                           \"paramValue\":\"SAVINGS\"\n                        },\n                        {  \n                           \"paramName\":\"Bank Account Number\",\n                           \"paramValue\":\"2235425042523\"\n                        },\n                        {  \n                           \"paramName\":\"End Date\",\n                           \"paramValue\":\"\"\n                        },\n                        {  \n                           \"paramName\":\"IFSC Code\",\n                           \"paramValue\":\"3234\"\n                        },\n                        {  \n                           \"paramName\":\"MICR Code\",\n                           \"paramValue\":\"234234\"\n                        },\n                        {  \n                           \"paramName\":\"Name As Per Bank AC\",\n                           \"paramValue\":\"Niteesha\"\n                        },\n                        {  \n                           \"paramName\":\"Preference\",\n                           \"paramValue\":\"PRIMARY\"\n                        },\n                        {  \n                           \"paramName\":\"Start Date\",\n                           \"paramValue\":\"\"\n                        }\n                     ]\n                  },\n\t\t\t\t  {  \n                     \"sequenceNo\":\"2\",\n                     \"property\":[  \n                        {  \n                           \"paramName\":\"Account Type \",\n                           \"paramValue\":\"SAVINGS_KR\"\n                        },\n                        {  \n                           \"paramName\":\"Bank Account Number\",\n                           \"paramValue\":\"2235425042523_KR\"\n                        },\n                        {  \n                           \"paramName\":\"End Date\",\n                           \"paramValue\":\"_KR\"\n                        },\n                        {  \n                           \"paramName\":\"IFSC Code\",\n                           \"paramValue\":\"3234_KR\"\n                        },\n                        {  \n                           \"paramName\":\"MICR Code\",\n                           \"paramValue\":\"234234_KR\"\n                        },\n                        {  \n                           \"paramName\":\"Name As Per Bank AC\",\n                           \"paramValue\":\"Niteesha_KR\"\n                        },\n                        {  \n                           \"paramName\":\"Preference\",\n                           \"paramValue\":\"PRIMARY_KR\"\n                        },\n                        {  \n                           \"paramName\":\"Start Date\",\n                           \"paramValue\":\"KR\"\n                        }\n                     ]\n                  }\n               ]\n            }\n         ]\n      },\n      {  \n         \"partyCode\":\"PI19490574\",\n         \"multiSetProperty\":[  \n            {  \n               \"multiSetName\":\"Bank Details\",\n               \"multiSetDetail\":[  \n                  {  \n                     \"sequenceNo\":\"1\",\n                     \"property\":[  \n                        {  \n                           \"paramName\":\"Account Type \",\n                           \"paramValue\":\"SAVINGS\"\n                        },\n                        {  \n                           \"paramName\":\"Bank Account Number\",\n                           \"paramValue\":\"568945627859\"\n                        },\n                        {  \n                           \"paramName\":\"End Date\",\n                           \"paramValue\":\"\"\n                        },\n                        {  \n                           \"paramName\":\"IFSC Code\",\n                           \"paramValue\":\"KVBL0001672\"\n                        },\n                        {  \n                           \"paramName\":\"MICR Code\",\n                           \"paramValue\":\"5RT566\"\n                        },\n                        {  \n                           \"paramName\":\"Name As Per Bank AC\",\n                           \"paramValue\":\"Bank Test\"\n                        },\n                        {  \n                           \"paramName\":\"Preference\",\n                           \"paramValue\":\"PRIMARY\"\n                        },\n                        {  \n                           \"paramName\":\"Start Date\",\n                           \"paramValue\":\"\"\n                        }\n                     ]\n                  }\n               ]\n            }\n         ]\n      }\n   ]\n}";
		Any root = JsonIterator.deserialize(inputJsonString);
		BankDetailsResponse bdr = new BankDetailsResponse();
//		System.out.println("errorCode : "+root.get("errorCode"));
		bdr.setErrorCode(root.get("errorCode").toInt());
//		System.out.println("errorDescription : "+root.get("errorDescription"));
		bdr.setErrorDescription(root.get("errorDescription").toString());
		
		if(bdr.getErrorCode().intValue() == 0){
//			System.out.println("policyNumber : "+root.get("policyNumber"));
			bdr.setPolicyNumber(root.get("policyNumber").toString());
			//		System.out.println("----------------------------------------------"+root.get("partyBankDetails").size()+"----------------------------");
			//		System.out.println("partyBankDetails : "+root.get("partyBankDetails"));
			List<PartyBankDetails> listOfPbd = new ArrayList<PartyBankDetails>();
			PartyBankDetails pbd = null;
			for (Any element : root.get("partyBankDetails")) {
				pbd = new PartyBankDetails();
//				System.out.println("partyCode : "+element.get("partyCode"));
				pbd.setPartyCode(element.get("partyCode").toString());
				//			System.out.println("----------------------------------------------"+element.get("multiSetProperty").size()+"----------------------------");
				List<BankDetails> listOfBd = new ArrayList<BankDetails>();

				for (Any element2 : element.get("multiSetProperty")) {
//					System.out.println("multiSetName : "+element2.get("multiSetName"));
					BankDetails bd =  null;
					Map<String, Object> temp = new HashMap<String, Object>();
					for (Any element3 : element2.get("multiSetDetail")) {
						for (Any element4 : element3.get("property")) {
							 String paramName = element4.get("paramName").toString();
							 String paramValue = element4.get("paramValue").toString();
//								System.out.println("paramName : "+element4.get("paramName"));
//								System.out.println("paramValue : "+element4.get("paramValue"));
							 temp.put(paramName.trim(), paramValue);
						}
//						System.out.println(temp);
						bd = new BankDetails();
						bd.setSequenceNo(element3.get("sequenceNo").toString());
						for (Map.Entry<String,Object> entry : temp.entrySet())  {
							if(entry.getKey().equals("Account Type")){
								bd.setAccountType(String.valueOf(temp.get("Account Type")));
							}
							if(entry.getKey().equals("Bank Account Number")){
								bd.setAccountNumber(String.valueOf(temp.get("Bank Account Number")));
							}
							if(entry.getKey().equals("End Date")){
								bd.setEndDate(String.valueOf(temp.get("End Date")));
							}
							if(entry.getKey().equals("Start Date")){
								bd.setStartDate(String.valueOf(temp.get("Start Date")));
							}
							if(entry.getKey().equals("IFSC Code")){
								bd.setIfscCode(String.valueOf(temp.get("IFSC Code")));
							}
							if(entry.getKey().equals("MICR Code")){
								bd.setMicrCode(String.valueOf(temp.get("MICR Code")));
							}
							if(entry.getKey().equals("Name As Per Bank AC")){
								bd.setNameAsperBankAccount(String.valueOf(temp.get("Name As Per Bank AC")));
							}
							if(entry.getKey().equals("Preference")){
								bd.setPreference(String.valueOf(temp.get("Preference")));
							}
						}
						listOfBd.add(bd);
					}
				}
//				pbd.setBankDetails(listOfBd);
				listOfPbd.add(pbd);
				bdr.setPartyBankDetails(listOfPbd);
			}
			System.out.println(bdr.toString());
		}else{
			System.out.println("Error returned for the policy number"+root.get("policyNumber"));
		}
		
	}
}
