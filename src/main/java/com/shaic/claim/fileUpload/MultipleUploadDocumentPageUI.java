package com.shaic.claim.fileUpload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.processtranslation.search.SearchProcessTranslationTableDTO;
import com.shaic.domain.DocumentDetails;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MultipleUploadDocumentPageUI extends ViewComponent implements Receiver,SucceededListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Upload 	upload ;
	
	@Inject
	private MultipleUploadDocumentTable uploadDocumentTableObj;
	
	private MultipleUploadDocumentDTO bean;
	
	@EJB
	private ReimbursementService reimbursementService;
	
	private SearchProcessTranslationTableDTO fileUploadDTO;

	private Button btnUpload;
	
	private File file;
	
	private VerticalLayout mainVertical;
	
	private Page currentPage;
	
	public void init(String screenName,Long transactionKey,Boolean isView){
		
		this.bean = new MultipleUploadDocumentDTO();
		this.bean.setTransactionKey(transactionKey);
		this.bean.setTransactionName(screenName);
		
		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		//Vaadin8-setImmediate() btnUpload.setImmediate(true);
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
		
		uploadDocumentTableObj.init("Document List", false, false);
		
		
		HorizontalLayout horizontalLayout = new HorizontalLayout(upload,btnUpload);
		mainVertical = new VerticalLayout(horizontalLayout,uploadDocumentTableObj);
		
		if(isView){
//			horizontalLayout.setVisible(false);
			upload.setVisible(false);
			btnUpload.setVisible(false);
		}
		
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
				upload.submitUpload();
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
			//Added for error
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
						MultipleUploadDocumentDTO updateDocument = reimbursementService.updateDocumentDetails(bean);

						if("refer_to_coordinator".equals(bean.getTransactionName()) && !StringUtils.isBlank(bean.getFileToken())){
							reimbursementService.updateDocumentDetailsInDocTable(bean, getFileUploadDTO());
						}
						
						List<MultipleUploadDocumentDTO> values = uploadDocumentTableObj.getValues();
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
						

					}
					else
					{
						Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
					}
					//for clearing heap
					fileAsbyteArray = null;
					fileName = null;
					uploadStatus = null;
			}
			}//need to do
			
			
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
			Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		//Added for error
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
	
	public SearchProcessTranslationTableDTO getFileUploadDTO() {
		return fileUploadDTO;
	}

	public void setFileUploadDTO(SearchProcessTranslationTableDTO fileUploadDTO) {
		this.fileUploadDTO = fileUploadDTO;
	}

}
