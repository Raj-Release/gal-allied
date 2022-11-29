package com.alert.util;

import java.io.File;

import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;

public class MyMessageBox extends MessageBox {
	
	static String filePath = System.getProperty("jboss.server.data.dir");
	
	 static Embedded e1;
	
	static String logoimg1="error.png";
	static String logoimg2="info.png";
	static String logoimg3="question.png";
	static String logoimg4="warning.png";
	
	static String buttonimg1="cross.png";
	static String buttonimg2="tick.png";
	
	/*
	static Resource res2 = new FileResource(new File(img2));
	static Resource res3 = new FileResource(new File(img3));
	static Resource res4 = new FileResource(new File(img4));*/
	
	public static MessageBox getErorMessageBox(String eMsg,boolean isCritical)
	{
		String filePath = System.getProperty("jboss.server.data.dir");
		String finalLocation = filePath + File.separator+"icon" + File.separator + logoimg1 ;
		String finalLocation1 = filePath + File.separator+"icon" + File.separator + buttonimg2 ;
		File file = new File(finalLocation);
		File file1 = new File(finalLocation1);
		Resource res1 = new FileResource(file);
		Resource res2 = new FileResource(file1);
		
		e1=new Embedded(null, res1);
		if(isCritical){
			MessageBox mbx = MessageBox.createError()
					.withCaption("Errors").withHtmlCriticalErrorMessage(eMsg).withIcon(e1).withCloseButton(ButtonOption.icon(res2));
			
			mbx.setMessageLayoutStyle("criticalRedAlert");
			
			return mbx;
		}
		else{
			return MessageBox.createError()
					.withCaption("Errors").withHtmlErrorMessage(eMsg).withIcon(e1).withCloseButton(ButtonOption.icon(res2));
		}
		        
	}
	public static MessageBox getWarningMessageBox(String eMsg)
	{
		String filePath = System.getProperty("jboss.server.data.dir");
		String finalLocation = filePath + File.separator+"icon" + File.separator + logoimg4 ;
		String finalLocation1 = filePath + File.separator+"icon" + File.separator + buttonimg2 ;
		File file = new File(finalLocation);
		File file1 = new File(finalLocation1);
		
		Resource res2 = new FileResource(file1);
		Resource res1 = new FileResource(file);
		
		e1=new Embedded(null, res1);
		return MessageBox.createWarning()
		    	.withCaption("Warning").withHtmlMessage(eMsg).withIcon(e1).withCloseButton(ButtonOption.icon(res2))/*isCriticalAlert()*/;
		   
	}
	public static MessageBox getConfMessageBox(String eMsg)
	{
		String filePath = System.getProperty("jboss.server.data.dir");
		String finalLocation = filePath + File.separator+"icon" + File.separator + logoimg3 ;
		String finalLocation1 = filePath + File.separator+"icon" + File.separator + buttonimg1 ;
		String finalLocation2 = filePath + File.separator+"icon" + File.separator + buttonimg2 ;
		File file = new File(finalLocation);
		File file1 = new File(finalLocation2);
		File file2 = new File(finalLocation1);
		Resource res1 = new FileResource(file);
		Resource res2 = new FileResource(file1);
		Resource res3 = new FileResource(file2);
		
		e1=new Embedded(null, res1);
		return MessageBox.createQuestion()
		    	.withCaption("Confirmation").withHtmlMessage(eMsg).withIcon(e1).withYesButton(ButtonOption.icon(res2))
		    	.withNoButton(ButtonOption.icon(res3));
		   
	}
	public static MessageBox getInfoMessageBox(String plainTextMessage)
	{
		String filePath = System.getProperty("jboss.server.data.dir");
		String finalLocation = filePath + File.separator+"icon" + File.separator + logoimg2 ;
		File file = new File(finalLocation);
		Resource res1 = new FileResource(file);
		
		e1=new Embedded(null, res1);
		return MessageBox.createInfo()
		    	.withCaption("Information").withHtmlMessage(plainTextMessage).withIcon(e1);
		   
	}
}
