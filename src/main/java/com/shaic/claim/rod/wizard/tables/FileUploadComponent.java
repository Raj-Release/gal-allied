/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.apache.commons.io.FileUtils;

import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;

/**
 * @author ntv.vijayar
 *
 */
public class FileUploadComponent  extends CustomField<UploadDocumentDTO> implements Receiver,SucceededListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private Upload upload;
	public Upload upload;
	
	private File file;
	
	private HorizontalLayout fldCompLayout;
	
	private TextField fileNameFld;
	
	private UploadDocumentDTO bean;
	private String dmsDocToken = "";
	
	private String strFileName = "";
	
	public FileUploadComponent()
	{
		
	}

	public FileUploadComponent(UploadDocumentDTO uploadDocDTO) {
	
		/*bean = uploadDocDTO;
		bean.setFileUpload(this);*/
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			if(null != fileAsbyteArray )
			{
				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
				//TO read file after load
				if (flagUploadSuccess.booleanValue())
				{
					String token = "" + uploadStatus.get("fileKey");
					dmsDocToken = token;
					strFileName = (String)uploadStatus.get("fileName");
					System.out.println("the token value-----"+token);
				}
				else
				{
					Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
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
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			//uploadStatus = fileUploadBean.fileUpload(appId,tokenId,template,fileAsbyteArray,filename);
//			System.out.println(" fileUpload ENDS ==>" + uploadStatus);
			return fos;
	}
		
	

	@Override
	protected Component initContent() {
		upload  = new Upload("", this);	
	    upload.addSucceededListener(this);
	    upload.setButtonCaption(null);
	    fldCompLayout = new HorizontalLayout();	    
	    fldCompLayout.setMargin(true);
	    fldCompLayout.addComponent(upload);
		return fldCompLayout;
	}
	
	

	@Override
	public Class<? extends UploadDocumentDTO> getType() {
		// TODO Auto-generated method stub
		return UploadDocumentDTO.class;
	}
	
	/*public void uploadFile(UploadDocumentDTO dto)
	{
		this.bean = dto;
	}*/
	
	public String getDmsDocToken()
	{
		return dmsDocToken;
	}
	
	public String getFileName()
	{
		return strFileName;
	}
	

}
