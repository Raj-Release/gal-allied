package com.shaic.claim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.aadhar.pages.UpdateAadharDetailsPresenter;
import com.shaic.claim.bedphoto.BedPhotoDetailsPresenter;
import com.shaic.claim.pancard.page.UpdatePanCardReportPresenter;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.reimbursement.rod.uploadinvestication.search.SearchUploadInvesticationTableDTO;
import com.shaic.reimbursement.uploadrodreports.UploadInvestigationReportPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class CommonFileUpload extends ViewComponent implements Receiver,SucceededListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Upload upload;
	
	private File file;
	
	private GridLayout gridLayout;
	
	private String screenName;
	
	public static String dataDir = System.getProperty("jboss.server.data.dir");
	
	public static String BYPASS_UPLOAD ;
	
    private Long key;
    
    private Long assignedKey;
    
    private SearchUploadInvesticationTableDTO searchUploadInvTableDto;
    

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		System.out.println("File uploaded" + event.getFilename());
		
		try{
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
				
				fileName = SHAUtils.getOnlyStrings(fileName);
			//	if(hasSpecialChar)
				//{
					//HashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
				WeakHashMap uploadStatus = SHAFileUtils.sendGeneratedFileToDMS(fileName, fileAsbyteArray, file);
					Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
					//TO read file after load
					if (flagUploadSuccess.booleanValue())
					{
						String token = "" + uploadStatus.get("fileKey");

						if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
			        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,fileName,token,SHAConstants.UPLOAD_INVESTGATION_SCREEN,this.searchUploadInvTableDto);
			        	}
						if(this.screenName.equals(ReferenceTable.UPDATE_PANCARD_SCREEN)){
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			        		fireViewEvent(UpdatePanCardReportPresenter.UPLOAD_EVENT, this.key,fileName,token,SHAConstants.UPDATE_PANCARD_SCREEN,userName);
			        	}
						if(this.screenName.equals(ReferenceTable.UPDATE_AADHAR_SCREEN)){
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			        		fireViewEvent(UpdateAadharDetailsPresenter.UPLOAD_EVENT, this.key,fileName,token,SHAConstants.UPDATE_AADHAR_SCREEN,userName);
			        	}
						if(this.screenName.equals(ReferenceTable.UPLOAD_BED_PHOTO)){
							String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
							fireViewEvent(BedPhotoDetailsPresenter.UPLOAD_EVENT, this.key, fileName,token,ReferenceTable.UPLOAD_BED_PHOTO,userName);
						}
						result();
					}
					//for clearing heap
					fileAsbyteArray = null;
					fileName = null;
					uploadStatus = null;
			}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void result(){
        Label successLabel = new Label("<b style = 'color: black;'>File Upload Successfully!!! </b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
//				fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
				
			}
		});
		
	}
	
    
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
        	filename = SHAUtils.getOnlyStrings(filename);
            file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
            fos = new FileOutputStream(file);
//        	if(this.screenName.equals(ReferenceTable.UPLOAD_INVESTIGATION_SCREEN)){
//        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, this.key,filename);
//        	}
        } catch (final java.io.FileNotFoundException e) {
        	Notification.show("Error", ""
        			+ "Please select the file to be uploaded",
        			Type.ERROR_MESSAGE);
        			e.printStackTrace();
        	/*
            new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
                .show(Page.getCurrent());
            return null;
        */}
      //Added for error
		if(fos == null){
		try {
			fos = new FileOutputStream("DUMMY");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        return fos; // Return the output stream to write to
	}
	
	public static Properties readConnectionPropertyFile()
	{
		Properties prop = new Properties();
		InputStream input = null;
		

		try {
			
			input = new FileInputStream(dataDir + "/" + "connection.properties");
			prop.load(input);
			
			BYPASS_UPLOAD = prop.getProperty("bypass_upload");
			return prop;
	  
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	

	
	public void init(final Long key, String screenName,String fileType){
		    this.screenName = screenName;
		    this.key = key;
		  final HorizontalLayout layout = new HorizontalLayout();
		    FormLayout form1 = new FormLayout();
		    Label label1 = new Label("File Upload");
//		    gridLayout = new GridLayout(4,4);
	        upload  = new Upload("", this);	
	        upload.addSucceededListener(this);
	        upload.setReceiver(this);
	        upload.setButtonCaption(null);
//	        layout.setMargin(true);
//	        layout.addComponent(upload);
	        form1.addComponent(label1);
	        form1.addComponent(upload);
	        form1.setSpacing(true);
	        FormLayout form3 = new FormLayout();
	        Button saveBtn = new Button("Upload");
	        saveBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
	        saveBtn.setWidth("-1px");
			saveBtn.setHeight("-10px");
			Label label = new Label();
//			layout.addComponent(saveBtn);
			
			FormLayout form2 = new FormLayout();
			Label label2 = new Label("File Type");
			ComboBox cmbFileType = new ComboBox();
			SelectValue selected = new SelectValue();
			selected.setId(1l);
			selected.setValue(fileType);
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			selectValueContainer.addBean(selected);
			cmbFileType.setContainerDataSource(selectValueContainer);
			cmbFileType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			cmbFileType.setItemCaptionPropertyId("value");
			
			cmbFileType.setValue(selected);
			cmbFileType.setEnabled(false);
			form2.addComponent(label2);
			form2.addComponent(cmbFileType);
			form3.addComponents(label,saveBtn);
			form2.setSpacing(true);
//			layout.addComponent(cmbFileType);
			layout.addComponent(form1);
			layout.addComponent(form2);
			layout.addComponent(form3);
			layout.setSpacing(true);
		//	HorizontalLayout uploadHor = new HorizontalLayout(saveBtn);
		//	uploadHor.setComponentAlignment(saveBtn, Alignment.TOP_RIGHT);
			VerticalLayout vertical = new VerticalLayout(layout);
			//vertical.setComponentAlignment(uploadHor, Alignment.TOP_RIGHT);
//			gridLayout.addComponent(upload,0,0);
//			gridLayout.addComponent(saveBtn,0,1);
			saveBtn.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {	
//					if(null != BYPASS_UPLOAD && !("").equalsIgnoreCase(BYPASS_UPLOAD) && ! ("false").equalsIgnoreCase(BYPASS_UPLOAD))
//					{
					
					upload.submitUpload();
//					}
//					else{
//						
//			        		fireViewEvent(UploadInvestigationReportPresenter.UPLOAD_EVENT, key,null,null);
//			        	}
				}
			});
			
			setCompositionRoot(vertical);

	}

	public Long getAssignedKey() {
		return assignedKey;
	}

	public void setAssignedKey(Long assignedKey) {
		this.assignedKey = assignedKey;
	}

	public SearchUploadInvesticationTableDTO getSearchUploadInvTableDto() {
		return searchUploadInvTableDto;
	}

	public void setSearchUploadInvTableDto(
			SearchUploadInvesticationTableDTO searchUploadInvTableDto) {
		this.searchUploadInvTableDto = searchUploadInvTableDto;
	}
	
}
