package com.shaic.reimbursement.claims_alert.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;

public class ClaimsAlertUploadDocumentPageUI extends ViewComponent implements Receiver,SucceededListener{

	private static final long serialVersionUID = 1L;

	private Upload 	upload ;

	@Inject
	private ClaimsAlertUploadDocumentTable uploadDocumentTableObj;

	private ClaimsAlertTableDTO bean;

	@EJB
	private ClaimsAlertMasterService claimsAlertMasterService;

	private Button btnUpload;

	private File file;

	private VerticalLayout mainVertical;

	private Page currentPage;

	private HorizontalLayout uploadhorizontalLayout;

	private ClaimsAlertDocsDTO docsDTO;

	public void init(ClaimsAlertTableDTO alertTableDTO,boolean isView){

		this.bean = alertTableDTO;

		uploadDocumentTableObj.setBean(bean,isView);
		uploadDocumentTableObj.init("Document List", false, false);

		if(isView){
			mainVertical = new VerticalLayout(uploadDocumentTableObj);
		}else{
			btnUpload = new Button();
			btnUpload.setWidth("-1px");
			btnUpload.setIcon(FontAwesome.UPLOAD);
			btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			//btnUpload.setCaption("Upload");
			//btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

			upload  = new Upload("", this);	
			upload.addSucceededListener(this);
			upload.setButtonCaption(null);

			uploadhorizontalLayout = new HorizontalLayout(upload,btnUpload);
			uploadhorizontalLayout.setComponentAlignment(upload, Alignment.MIDDLE_RIGHT);
			uploadhorizontalLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_LEFT);
			mainVertical = new VerticalLayout(uploadhorizontalLayout,uploadDocumentTableObj);
			addListener();
		}

		if(bean !=null
				&& bean.getDocsDTOs() !=null){
			setDocsTablesValue(bean.getDocsDTOs());
		}
		

		mainVertical.setSpacing(true);	    	        
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
					WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
					Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
					//TO read file after load
					if (flagUploadSuccess.booleanValue())
					{
						String token = "" + uploadStatus.get("fileKey");
						if(token !=null
								&& !token.isEmpty()){
							docsDTO = new ClaimsAlertDocsDTO();
							docsDTO.setFileName(fileName);
							docsDTO.setFileToken(token);		
							docsDTO.setDocsFrom(bean.getAlertCategory().getValue());				
							List<ClaimsAlertDocsDTO> values = uploadDocumentTableObj.getValues();					
							int sNo = values.size()+1;
							docsDTO.setsNo(sNo);
							uploadDocumentTableObj.addBeanToList(docsDTO);
							bean.setDocsDTOs(uploadDocumentTableObj.getValues());
						}else{
							Notification.show("Error", "Unsupported File Format", Type.ERROR_MESSAGE);
						}
						
					}
					else{			
						Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
					}
				}
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
			if(null != file){
				fos = new FileOutputStream(file);
			}
			else{
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

	public void setDocsTablesValue(List<ClaimsAlertDocsDTO> alertDocsDTOs){

		if(alertDocsDTOs != null 
				&& !alertDocsDTOs.isEmpty()){
			uploadDocumentTableObj.removeRow();
			int sNo = 1;
			for(ClaimsAlertDocsDTO docsDTO :alertDocsDTOs){
				docsDTO.setsNo(sNo);
				uploadDocumentTableObj.addBeanToList(docsDTO);
				sNo++;
			}
		}	
	}

	private void addListener(){

		btnUpload.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					upload.submitUpload();
				}catch(Exception e){
					e.printStackTrace();
				}	
			}
		});
	}

}
