package com.shaic.claim.intimation.create;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.shaic.claim.policy.search.ui.IntegrationLogTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ClaimGrievanceAPIService {

	private static final Logger log = LoggerFactory.getLogger(ClaimGrievanceAPIService.class);

	public List<ClaimGrievanceDTO> getGrievanceTrialDataForClaim(String argIntimationNumber, String deptId, String apiName){
		List<ClaimGrievanceDTO> listOfGriev = new ArrayList<ClaimGrievanceDTO>();
		ClaimGrievanceDTO grievData = new ClaimGrievanceDTO();
		IntegrationLogTable logTbl = new IntegrationLogTable();
		try{
			String accessToken = DBCalculationService.getZohoAuthToken();
			BPMClientContext  context = new BPMClientContext();
			String serviceURL = context.getZohoGrievWSUrl();
			serviceURL = serviceURL.replace("dept_id", deptId);
			serviceURL = serviceURL+":"+argIntimationNumber;
			logTbl.setOwner("ZOHO");
			if(apiName.equals("Grievance")){
				logTbl.setServiceName("ZohoGrievance");
			}else if(apiName.equals("Escalation")){
				logTbl.setServiceName("ZohoEscalation");
			}
			logTbl.setUrl(serviceURL);
			logTbl.setRequest(serviceURL);
			log.info("Zoho Grievance Live - Url :"+serviceURL);
			log.info("Zoho Grievance API AccessToken for intimation ("+argIntimationNumber+") :"+accessToken);
			Client client = com.sun.jersey.api.client.Client.create();
			//			client.resource("https://desk.zoho.in/api/v1/tickets/search?limit=1&from=0&departmentId=12655000000058908&customField1=cf_claim_intimation_number:CIR/2023/141137/0202003");
			WebResource webResource = client.resource(serviceURL);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Zoho-oauthtoken "+accessToken)
					.get(ClientResponse.class);
			if(response.getStatus() == 200){
				String output = response.getEntity(String.class);
				logTbl.setResponse(output.substring(0, 2000));
				logTbl.setStatus("Success");
				log.info("Response from ZOHO Grievance API" + output);
				Any data = JsonIterator.deserialize(output);
				if(data != null){
					String id = (data.get("data",0).get("id") == null)?"":data.get("data",0).get("id").toString();
					Any cfData = data.get("data",0).get("cf");
					String cfComments = null;
					if(cfData != null){
						cfComments = (cfData.get("cf_comments") == null)?"":cfData.get("cf_comments").toString();
					}
					//String cfComments = "DateTime:2022-05-28T06:30:59.000Z,Zoho Ticket No:3322435,User ID:Dr.Harish,Source of Escalation:Grievance Department,Grievance Doctor Solution:12655002282187289,Dr. Opinion:Duplicate";
					//parsing logic
					if(!StringUtils.isBlank(cfComments) && apiName.equals("Grievance")){
						String comments[] = cfComments.split(",");
						grievData.setDateTime((comments[0].split(":")[1].equals("null"))?"":comments[0].split("Time:")[1]);
						grievData.setZohoTicNo((comments[1].split(":")[1].equals("null"))?"":comments[1].split(":")[1]);
						grievData.setUserId((comments[2].split(":")[1].equals("null"))?"":comments[2].split(":")[1]);
						grievData.setEscalationSource((comments[3].split(":")[1].equals("null"))?"":comments[3].split(":")[1]);
						grievData.setGrievDoctorSol((comments[4].split(":")[1].equals("null"))?"":comments[4].split(":")[1]);
						grievData.setDrOpinion((comments[5].split(":")[1].equals("null"))?"":comments[5].split(":")[1]);
						log.info("Grievance Trail Data For ("+argIntimationNumber+") "+grievData.toGrievanceString());
						if(!StringUtils.isBlank(id) && !StringUtils.isBlank(grievData.getGrievDoctorSol())){
							getGrievanceDoctorMail(id,grievData.getGrievDoctorSol(),accessToken, grievData, apiName);
						}else{
							log.info("Grievance Mail Trail Data For ("+argIntimationNumber+") is not available due to id/doctorSolution id is null");
						}
						listOfGriev.add(grievData);
					}
					if(!StringUtils.isBlank(cfComments) && apiName.equals("Escalation")){
						String comments[] = cfComments.split(",");
						grievData.setEscTicketCreationDateTime((comments[0].split(":")[1].equals("null"))?"":comments[0].split("creation:")[1]);
						grievData.setEscTicketNo((comments[1].split(":")[1].equals("null"))?"":comments[1].split(":")[1]);
						grievData.setEscSource((comments[2].split(":")[1].equals("null"))?"":comments[2].split(":")[1]);
						grievData.setEscContent((comments[3].split(":")[1].equals("null"))?"":comments[3].split(":")[1]);
						grievData.setEscTicketClosedDateTime((comments[4].split(":")[1].equals("null"))?"":comments[4].split(":")[1]);
						log.info("Grievance Trail Data For ("+argIntimationNumber+") "+grievData.toEscalationString());
						if(!StringUtils.isBlank(id) && !StringUtils.isBlank(grievData.getEscContent())){
							getGrievanceDoctorMail(id,grievData.getEscContent(),accessToken, grievData, apiName);
						}else{
							log.info("Escalation Mail Trail Data For ("+argIntimationNumber+") is not available due to id/doctorSolution id is null");
						}
						listOfGriev.add(grievData);
					}
				}
			}else if(response.getStatus() != 200){
				logTbl.setStatus("Error");
				logTbl.setRemarks("Connection Error/Bad Request");
				log.info("ZOHO Grievance API URL Error Response Code : "+response.getStatus());
			}
			logTbl.setCreatedBy("GALAXY");
			logTbl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			logTbl.setActiveStatus(1L);
			DBCalculationService.insertIntegrationLogTable(logTbl);
		}catch(Exception exp){
			logTbl.setStatus("Error");
			logTbl.setRemarks(exp.getMessage());
			logTbl.setCreatedBy("GALAXY");
			logTbl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			logTbl.setActiveStatus(1L);
			DBCalculationService.insertIntegrationLogTable(logTbl);
			log.info("Exception Occurred While Accessing Zoho Grievance API "+exp.getMessage());
			exp.printStackTrace();
		}
		return listOfGriev;
	}

	private void getGrievanceDoctorMail(String argId, String grievDoctorSol, String accessToken, ClaimGrievanceDTO grievData, String apiName) {
		IntegrationLogTable logTbl = new IntegrationLogTable();
		try{
			BPMClientContext  context = new BPMClientContext();
			String serviceURL = context.getZohoGrievDocWSUrl();
			//			String serviceURL = "https://desk.zoho.in/api/v1/tickets/id/threads/GrievanceDoctorSolution?include=plainText";
			serviceURL = serviceURL.replace("id", argId);
			serviceURL = serviceURL.replace("GrievanceDoctorSolution", grievDoctorSol);
			logTbl.setOwner("ZOHO");
			if(apiName.equals("Grievance")){
				logTbl.setServiceName("ZohoGrievanceMail");
			}else if(apiName.equals("Escalation")){
				logTbl.setServiceName("ZohoEscalationMail");
			}
			logTbl.setUrl(serviceURL);
			logTbl.setRequest(serviceURL);
			log.info("Zoho Grievance Doctor Solution Live - Url :"+serviceURL);
			Client client = com.sun.jersey.api.client.Client.create();
			WebResource webResource = client.resource(serviceURL);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Zoho-oauthtoken "+accessToken)
					.header("Accept", "application/json;charset=UTF-8")
					.get(ClientResponse.class);
			if(response.getStatus() == 200){
				String output = response.getEntity(String.class);
				logTbl.setResponse(output.substring(0, 2000));
				logTbl.setStatus("Success");
				log.info("Response from ZOHO Grievance Doctor Solution API" + output);
				JsonObject convertedObject = new Gson().fromJson(output, JsonObject.class);
				if(convertedObject != null){
					String mailTrailData = convertedObject.get("content").toString();
					//String mailTrailData = "<meta /> <style style=\"display: none;\">div.zm_-8465585266602674939_parse_7243954129022288187 P { margin-top: 0; margin-bottom: 0 }</style>  <div dir=\"ltr\" class=\" zm_-8465585266602674939_parse_7243954129022288187\"> <div style=\"font-family: Calibri, Arial, Helvetica, sans-serif;font-size: 12.0pt;color: rgb(0,0,0);\" class=\"x_556502352elementToProof\"> </div> Dear Mam/Sir, <div><br /> </div> <div>Kindly do the needful.</div> <div><br /> </div> <div>Regards,</div> <div>Jayashree S</div> Chairman Secretariat <div id=\"x_556502352divRplyFwdMsg\" dir=\"ltr\"> <div class=\"x_556502352elementToProof\"></div> </div> <div> <div style=\"direction: ltr;font-family: Tahoma;color: rgb(0,0,0);font-size: 10.0pt;\"><br /> <div style=\"font-family: Times New Roman;color: rgb(0,0,0);font-size: 16.0px;\"> <hr /> <div id=\"x_divRpF19714\" style=\"direction: ltr;\"><font face=\"Tahoma\" size=\"2\" color=\"#000000\"><b>From:</b> Guru Maravanthe [gurudutt171187@gmail.com]<br /> <b>Sent:</b> 29 May 2022 19:52:08<br /> <b>To:</b> claims.payment@starhealth.biz; gro; grievances; GRIEVANCES BLR; complaints IRDA; bimalokpal bengaluru<br /> <b>Cc:</b> A.R. RAMESH; savithree76@gmail.com; cmd; DR.Prakash S (Managing Director); cmdsec<br /> <b>Subject:</b> Re: Intimation No: CIR/2023/141137/0202003 - Incomplete Info on Policy- URGENT<br /> </font><br /> </div> <div></div> <div> <p style=\"color: red;\"><b><i><font face=\"Times New Roman\" size=\"4\">Cyber Security First: </font><font face=\"Times New Roman\">External email – Use caution whilst clicking links or opening any attachment. </font></i></b></p> <div> <div dir=\"auto\">There was no information from your side, either be teleo or thru email, regarding the claim of Ayush sub limit. <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">This was not informed to me while opting for Mediclaim Policy and the same is not in other Company Policies, why can't you give full and correct information while opting for Policy&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">I want 87% of claim to be done as per mediclaim policy and this was the information given to me, hence based on that I have gone through the treatment&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Request to check on this</div> </div> <br /> <div class=\"x_gmail_quote\"> <div dir=\"ltr\" class=\"x_gmail_attr\">On Sat, 28 May 2022, 11:43 Guru Maravanthe, &lt;<a href=\"mailto:gurudutt171187@gmail.com\" target=\"_blank\" rel=\"noreferrer\">gurudutt171187@gmail.com</a>&gt; wrote:<br /> </div> <blockquote class=\"x_gmail_quote\" style=\"margin: 0 0 0 0.8ex;border-left: 1.0px rgb(204,204,204) solid;padding-left: 1.0ex;\"> <div dir=\"auto\">Helo, <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><span style=\"font-size: 12.8px;\">My Concern is&nbsp;</span> <div dir=\"auto\" style=\"font-size: 12.8px;\"><br /> <div dir=\"auto\">Why the Claim is not been settled for the ratio 87% , you need to check on this and as per that total amount of 37,050 to be approved,&nbsp; wherein approved 25k,&nbsp;&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">You have mentioned non payable amount of Rs.19050 - As per policy Ayush sublimit exhausted,&nbsp; mine is the Mediclaim policy of 10 lakh and why only 53% approvEd instead of 87%?&nbsp;</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">(IRDAI) directed all general insurance companies to include AYUSH treatments in their health insurance policies</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Kindly check and do the&nbsp;needful</div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\"><br /> </div> <div dir=\"auto\">Regards&nbsp;</div> <div dir=\"auto\">Gurudutt&nbsp;</div> </div> </div> </div> <br /> <div class=\"x_gmail_quote\"> <div dir=\"ltr\" class=\"x_gmail_attr\">On Fri, 27 May 2022, 23:57 , &lt;<a href=\"mailto:claims.payment@starhealth.biz\" target=\"_blank\" rel=\"noreferrer\">claims.payment@starhealth.biz</a>&gt; wrote:<br /> </div> <blockquote class=\"x_gmail_quote\" style=\"margin: 0 0 0 0.8ex;border-left: 1.0px rgb(204,204,204) solid;padding-left: 1.0ex;\"> <u></u> <div> <p style=\"text-align: center;\"></p> <h2 style=\"text-align: center;\">STAR HEALTH AND ALLIED INSURANCE CO. LTD.</h2> <p></p> <p style=\"text-align: center;\"></p> <h4 style=\"text-align: center;\">No.15, SRI BALAJI COMPLEX,1st FLOOR, WHITES LANE,ROYAPETTAH,CHENNAI-600014. Customer Care Number - 044 6900 6900 / Corporate Customers - 044 43664666, <img alt=\"Whatsapp\" height=\"16\" width=\"16\" />  Chat - +91 9597652225, <a href=\"http://www.Starhealth.in\" target=\"_blank\" rel=\"noreferrer\"> www.Starhealth.in</a></h4> <p></p> <p style=\"text-align: right;\">Date: 27/05/2022</p> <p style=\"text-align: left;\">Claim Int No :CIR/2023/141137/0202003 </p> <p style=\"text-align: left;\">Policy No. :P/141137/01/2022/006590 </p> <p style=\"text-align: left;\">Main Member Name :Mr.GURUDUTT </p> <p style=
					
					if(!StringUtils.isBlank(mailTrailData)){
						mailTrailData = mailTrailData.replaceAll("\\\\r\\\\n","");
						mailTrailData = mailTrailData.replaceAll("\\\\n", "");
						mailTrailData = mailTrailData.substring(1,mailTrailData.length()-1);
						grievData.setGrievDoctorSolMailTrail(mailTrailData);
					}
				}
			}else if(response.getStatus() != 200){
				logTbl.setStatus("Error");
				logTbl.setRemarks("Connection Error/Bad Request");
				log.info("ZOHO Grievance Doctor Solution API URL Error Response Code : "+response.getStatus());
			}
			logTbl.setCreatedBy("GALAXY");
			logTbl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			logTbl.setActiveStatus(1L);
			DBCalculationService.insertIntegrationLogTable(logTbl);
		}catch(Exception exp){
			logTbl.setStatus("Error");
			logTbl.setRemarks(exp.getMessage());
			logTbl.setCreatedBy("GALAXY");
			logTbl.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			logTbl.setActiveStatus(1L);
			DBCalculationService.insertIntegrationLogTable(logTbl);
			log.info("Exception Occurred While Accessing Zoho Grievance Doctor Solution API "+exp.getMessage());
			exp.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ClaimGrievanceAPIService ser = new ClaimGrievanceAPIService();
		List<ClaimGrievanceDTO> data = ser.getGrievanceTrialDataForClaim("CIR/2023/141137/0202003", "", "");
		System.out.println(data.get(0).toString());
	}

}