package com.shaic.ims.bpm.claim;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

public class GetHumanTasks {

//	  public List<HumanTask> getRegistrationTask(String user, String password,RegistrationQF qf,Pageable apage)
//	  {
//		    
//	    System.out.println("BPM Init lookup called");
//	    Context context = BPMClientContext.getInitialContext(user,password);
////	    List<HumanTask> taskList = new ArrayList<HumanTask>();
//	    
//	    PagedTaskList taskList = new PagedTaskList();
//	    ClaimRegTask invoke = null;
//
//	      try {
//	    	  
//	    		invoke = (ClaimRegTask) context.lookup("ClaimRegTask#com.shaic.ims.bpm.claim.servicev2.registration.search.ClaimRegTask");
//	    		PayloadBOType payloadBo = new PayloadBOType();
//	    		
//	    		HospitalInfoType hospitalInfo = new HospitalInfoType();
//	    		hospitalInfo.setHospitalType(qf.getHospitalType());
//	    		
//	    		payloadBo.setHospitalInfo(hospitalInfo);
//	    		
//	    		IntimationType intimationType = new IntimationType();
////	    		intimationType.setIntDate(qf.getIntDate());
//	    		intimationType.setIntimationNumber(qf.getIntimationNumber());
//	    		
//	    		payloadBo.setIntimation(intimationType);
//	    		
//	    		PolicyType policyType = new PolicyType();
//	    		policyType.setPolicyId(qf.getPolicyId());
//	    		
//	    		payloadBo.setPolicy(policyType);
//	    		
//	    		
//			taskList = invoke.getTasks(user,apage,payloadBo);
//			System.out.println("Lookup called and executed at the server side ");
//			
////			System.out.println("tasklist          "+taskList);
////			 for (HumanTask item: taskList)
////		      {
////		        System.out.println("item URL:" + item.getUrl());
////		        System.out.println("item Number:" + item.getNumber());
////		        System.out.println("payload:" + item.getPayload());
////
////		        NodeList payloadNodeList = item.getPayload().getElementsByTagName(("RegIntDetails"));
//////		        NodeList payloadNodeList = item.getPayload().getElementsByTagName(("PedReq"));
////		        System.out.println("payloadNodeList = > " + payloadNodeList);
////		        Node fstnode = payloadNodeList.item(0);
////		        Element fstElmnt = (Element) fstnode;
////		        System.out.println("First Element :: " + fstElmnt.getFirstChild().getNodeName());
////		        System.out.println("First Element Value :: " + fstElmnt.getFirstChild().getNodeValue());
//////		        NodeList tstNmElmntLst = fstElmnt.getElementsByTagName("intimationNumber"); //getting node
//////		        Element tstNmElmnt = (Element) tstNmElmntLst.item(0);
//////		        System.out.println("intimationNumber::" + tstNmElmnt.getFirstChild().getNodeName());
//////		        System.out.println("intimationNumber::" + tstNmElmnt.getFirstChild().getNodeValue());
////		       
////		        printElement(item.getPayload());
////		      }
//			
//			
//			
//			return taskList.getHumanTasks();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return taskList.getHumanTasks();
//	  }
	  public static void printElement(Element element)
			    throws TransformerConfigurationException, TransformerException
			  {
			    TransformerFactory transFactory = TransformerFactory.newInstance();
			    Transformer transformer = transFactory.newTransformer();
			    StringWriter buffer = new StringWriter();
			    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			    transformer.transform(new DOMSource(element), new StreamResult(buffer));
			    String str = buffer.toString();
			    System.out.println("Payload=> " + str);
			  }
	  
//	  public List<HumanTask> getCoveringLetterTask(String user, String password,Pageable apage,RegCovLetterQF regcovletterqf)
//	  {
//		    
//	    System.out.println("BPM Init lookup called");
//	    Context context = BPMClientContext.getInitialContext(user,password);
//	    List<HumanTask> taskList = null;
//	    GenCovLetterRegTask invoke = null;
//
//	      try {
//	    	  
////	    		invoke = (GenCovLetterRegTask) context.lookup("GenCovLetterRegTask#com.shaic.ims.bpm.claim.service.registration.search.GenCovLetterRegTask");
//	    		invoke = (GenCovLetterRegTask) context.lookup("GenCovLetterRegTask#com.shaic.ims.bpm.claim.servicev2.registration.search.GenCovLetterRegTask");
//	    		
////	    		PagedTaskList pagedTask = invoke.getTasks(user, password, apage, regcovletterqf);
//	    		
//			PagedTaskList pagedTask = invoke.getTasks(user,null, null); 
//			
//			taskList = pagedTask.getHumanTasks();
//			
//			System.out.println("Lookup called and executed at the server side ");
//			return taskList;
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//		return taskList;
//	  }
	  
	  
	  
	  
}
