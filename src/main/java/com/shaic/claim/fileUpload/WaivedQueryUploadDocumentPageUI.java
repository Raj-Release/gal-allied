package com.shaic.claim.fileUpload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

public class WaivedQueryUploadDocumentPageUI extends ViewComponent implements Receiver,SucceededListener{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Upload 	upload ;
	
	@Inject
	private WaivedQueryUploadDocumentTable uploadDocumentTableObj;
	
	private MultipleUploadDocumentDTO bean;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	
	private Button btnUpload;
	
	private File file;
	
	private VerticalLayout mainVertical;
	
	private Page currentPage;
	
	private DateField uploadLetterDate;
	
	private TextField noOfPages;
	
	public void init(String screenName,Long transactionKey,Boolean isView){
		
		this.bean = new MultipleUploadDocumentDTO();
		this.bean.setTransactionKey(transactionKey);
		this.bean.setTransactionName(screenName);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
	    
	    uploadLetterDate = new DateField("Letter Date");
	    noOfPages = new TextField();
	    noOfPages.setCaption("No Of Pages");
	    
		uploadDocumentTableObj.init("Document List", false, false);
		uploadDocumentTableObj.setQueryUploadDocumentPageUI(this);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout( );
		horizontalLayout.addComponents(upload,uploadLetterDate,noOfPages,btnUpload);
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		mainVertical = new VerticalLayout(horizontalLayout,uploadDocumentTableObj);
		
		/*if(isView){
//			horizontalLayout.setVisible(false);
			upload.setVisible(false);
			btnUpload.setVisible(false);
		}*/
		
		List <MultipleUploadDocumentDTO> resultList = reimbursementService.getUpdateDocumentDetails(bean.getTransactionKey());
		
		if(resultList != null && ! resultList.isEmpty()){
			
			uploadDocumentTableObj.setTableList(resultList);
			
		}
//		for (MultipleUploadDocumentDTO multipleUploadDocumentDTO : resultList) {
//			uploadDocumentTableObj.addBeanToList(multipleUploadDocumentDTO);
//		}
		
	    mainVertical.setSpacing(true);
	    
	    btnUpload.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				
				if(! bean.getIsEdit()){
					upload.submitUpload();
				}else{
					
					bean.setUploadLetterDate(uploadLetterDate.getValue());
					bean.setNoOfPages(noOfPages.getValue() != null ? ! noOfPages.getValue().isEmpty() ? Integer.valueOf(noOfPages.getValue()) : 0:0);
					reimbursementService.saveEditDocumentValues(bean);
					uploadDocumentTableObj.init("", false, false);
					List <MultipleUploadDocumentDTO> resultList = reimbursementService.getUpdateDocumentDetails(bean.getTransactionKey());
					int sNo = 1;
					for (MultipleUploadDocumentDTO multipleUploadDocumentDTO : resultList) {
						multipleUploadDocumentDTO.setsNo(sNo);
						uploadDocumentTableObj.addBeanToList(multipleUploadDocumentDTO);
						sNo++;
					}
					bean.setIsEdit(false);
					upload.setEnabled(true);
					
					uploadLetterDate.setValue(null);
					noOfPages.setValue("");
				}
				
			}
		});
	    
	    setCompositionRoot(mainVertical);
	}
	
	public void setCurrentPage(Page currentPage){
		this.currentPage = currentPage;
		uploadDocumentTableObj.setCurrentPage(this.currentPage);
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		
		try {
			if(file.exists() && !file.isDirectory()) { 
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			String fileName = event.getFilename();
			if (null != fileAsbyteArray) {
				//file gets uploaded in data directory when code comes here.
				if(null != event && null != event.getFilename() && (event.getFilename().endsWith("jpg") || event.getFilename().endsWith("jpeg") ||
						event.getFilename().endsWith("JPG") || event.getFilename().endsWith("JPEG")))
				{
					/*File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);*/
				}
				

				Pattern p = Pattern.compile("^[a-zA-Z 0-9]*$");
				boolean hasSpecialChar = p.matcher(event.getFilename()).find();
			//	if(hasSpecialChar)
				//{
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
					Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
					//TO read file after load
					if (flagUploadSuccess.booleanValue())
					{
						String token = "" + uploadStatus.get("fileKey");
//						bean.setFileToken(token);
//						this.bean.setFileName(event.getFilename());						
						
						bean.setFileName(fileName);
						bean.setFileToken(token);
						bean.setUploadLetterDate(uploadLetterDate.getValue());
						bean.setNoOfPages(noOfPages.getValue() != null ? ! noOfPages.getValue().isEmpty() ? Integer.valueOf(noOfPages.getValue()) : 0:0);
						
						/*List<MultipleUploadDocumentDTO> values = uploadDocumentTableObj.getValues();
						if(null != values && !values.isEmpty()){
							for (MultipleUploadDocumentDTO multipleUploadDocumentDTO : values) {
								bean.setUploadLetterDate(multipleUploadDocumentDTO.getUploadLetterDate());
								bean.setNoOfPages(multipleUploadDocumentDTO.getNoOfPages());
							}
						}*/
						
						MultipleUploadDocumentDTO updateDocument = reimbursementService.updateWaivedQueryDocumentDetails(bean);
						
						
						uploadDocumentTableObj.init("", false, false);
						List <MultipleUploadDocumentDTO> resultList = reimbursementService.getUpdateDocumentDetails(bean.getTransactionKey());
						int sNo = 1;
						for (MultipleUploadDocumentDTO multipleUploadDocumentDTO : resultList) {
							multipleUploadDocumentDTO.setsNo(sNo);
							uploadDocumentTableObj.addBeanToList(multipleUploadDocumentDTO);
							sNo++;
						}
//						
//						uploadDocumentTableObj.addBeanToList(updateDocument);
						
						uploadLetterDate.setValue(null);
						noOfPages.setValue("");
					}
					else
					{
						Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
					}
			}
		}else {
			Notification.show("Error", StringUtils.EMPTY + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
		}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		
		FileOutputStream fos = null;
		try {
			this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
			if(null != file)
			{
				fos = new FileOutputStream(file);
			}
			else
			{
				Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) { 
		/*	Notification.show("Error", ""
			+ "Please select the file to be uploaded",
			Type.ERROR_MESSAGE);*/
			e.printStackTrace();}
		if(fos == null){
			try {
				fos = new FileOutputStream("DUMMY");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			return fos;
	}
	
	
	public void setEditValues(MultipleUploadDocumentDTO dto){
		if(this.upload != null){
			this.upload.setEnabled(false);
			this.bean = dto;
			if(uploadLetterDate != null){
				uploadLetterDate.setValue(dto.getUploadLetterDate());
			}
			if(noOfPages != null){
				noOfPages.setValue(dto.getNoOfPages() != null ? dto.getNoOfPages().toString() : "");
			}
			
		}
	}
	
	public Boolean IsDocumentUploaded(){
		if(uploadDocumentTableObj != null){
			List<MultipleUploadDocumentDTO> values = uploadDocumentTableObj.getValues();
			if(values != null && !values.isEmpty()){
				return true;
			}
		}
		return false;
	}


}
