/**
 * 
 */
package com.shaic.arch;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.components.PopUpTextField;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;

/**
 * @author ntv.vijayar
 *
 */
public class TestClass extends ViewComponent {
	
	public void init()
	{

		BillEntryDetailsDTO bto = new BillEntryDetailsDTO();
		HorizontalLayout hLayout = new HorizontalLayout();
		PopUpTextField tfld = new PopUpTextField(bto);
		tfld.initHandler();
		
		PopUpTextField tfld1 = new PopUpTextField(bto);
		tfld1.initHandler();
		hLayout.addComponents(new FormLayout(tfld),new FormLayout(tfld1));
		
		setCompositionRoot(hLayout);
		//setCompositionRoot(compositionRoot);
	}
	
	
	public static void main(String args[])
	{
		//TestClass test = new TestClass();
		//test.init();
		
//		String date = "5/22/2017 11:12:38 AM";
//		
//		Date formatPolicyStartDate = SHAUtils.formatPremiaDate(SHAUtils
//				.formatPremiaDateAsString(new Date(date)));
//		System.out.println(formatPolicyStartDate);
		
		
//		SMSAlertForQueryReplyReceived job = new SMSAlertForQueryReplyReceived();
//		Context context = null;
//		try {
//			job.execute(context);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
/*		try{
			String date2 = "22/5/2017 11:12:38";
			Date formatPolicyStartDate2 = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss").parse(date2);
			System.out.println(formatPolicyStartDate2.toString());
		}catch(Exception exp){
			exp.printStackTrace();
		}*/
/*		Hashids hashids = new Hashids("gSJg5yxNz7wfjhPz", 8);
		String tokenArray = hashids.encode(Long.parseLong("126015"));
		System.out.println(tokenArray);*/
		/*byte[] val = ("LC0000000049"+":"+"@Password12$").getBytes();
		String encodedString = new String(Base64.encodeBase64(val));
		System.out.println(encodedString);*/
		
		/*byte[] val = ("QkEwMDAwMjIxNTAyOnk4MnVyS2VlUjVjUnpGYng=").getBytes();
		String decodedString = new String(Base64.decodeBase64(val));
		System.out.println(decodedString);*/
		
		
		/*byte[] val = ("V0VCU0lURTp5ODJ1cktlZVI1Y1J6RmJ4=").getBytes();
		String decodedString = new String(Base64.decodeBase64(val));
		System.out.println(decodedString);*/
		
		/*byte[] val = ("AGNT_SRCH_001"+":"+"WINTERBOOT123").getBytes();
		String encodedString = new String(Base64.encodeBase64(val));
		System.out.println(encodedString);*/
		
		/*byte[] val = ("GC"+":"+"325D163728C17954EDAA8DC860D1C595").getBytes();
		String encodedString = new String(Base64.encodeBase64(val));
		System.out.println(encodedString);*/
		byte[] val = ("WEBSITE"+":"+"bK8kQQjm2qGLcqKJ").getBytes();
		String encodedString = new String(Base64.encodeBase64(val));
		System.out.println(encodedString);
		
		/*ArrayList<String> letters = new ArrayList<>();  
		letters.add("1231");  
		letters.add("1232");  
		System.out.println(letters);    // [A, B, C, D, E]  
		ArrayList<String>removedLetters = new ArrayList<>();  
		removedLetters.add("1232");  
		removedLetters.add("1233");  
		System.out.println(removedLetters); // [C, D, E]  
		System.out.println(letters.removeAll(removedLetters)); //true  	
		System.out.println(letters);        // [A, B]  
*/		
		try{
			String date2 = "10-05-2019 12:02:45 AM";
			Date formatPolicyStartDate2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").parse(date2);
			System.out.println(formatPolicyStartDate2.toString());
			
			DateFormat dateFormat12 = new SimpleDateFormat("HH:mm:ss");
			String formattedDate=dateFormat12.format(formatPolicyStartDate2);
			System.out.println(formattedDate);
			
			String intimationWithUnderScore = "CLI/2020/151118/0057470";
			intimationWithUnderScore =  intimationWithUnderScore.substring(intimationWithUnderScore.lastIndexOf("/"), intimationWithUnderScore.length());
			System.out.println(intimationWithUnderScore);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			Date date = new Date();
			System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
			
			/*byte[] val = ("AGNT_SRCH_001"+":"+"WINTERBOOT123").getBytes();
			String encodedString = new String(Base64.encodeBase64(val));
			System.out.println(encodedString);*/
			String date3 = "04/09/2019";
			String glxAdmissionDate = date3.replace("/", "-");
			System.out.println("glxAdmissionDate "+glxAdmissionDate);
			System.out.println("admissionDate "+date3);
			
			Date getDate = SHAUtils.formatTimeFromString("12/09/2019");
			System.out.println("getDate : "+getDate);
			
			System.out.println(new Date().toString());

/*			String argClaimNo = "CLI$2016$171124$0284902";
			String temp  = argClaimNo.replaceAll("\\$", "/");
			System.out.println("temp : "+temp);*/
			String tempVal = URLEncoder.encode("CLI/2020/151118/0302007", "UTF-8");
			System.out.println(tempVal);
			System.out.println(URLDecoder.decode(tempVal, "UTF-8"));
			
			List<String> tempList = new ArrayList<String>();
			tempList.add("asdfaf");
			tempList.add("235435");
			System.out.println(tempList.size());
			
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}

}