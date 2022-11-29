package com.shaic.claim.pcc.zonalMedicalHead;

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
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.pcc.dto.PCCUploadDocumentsDTO;
import com.shaic.claim.pcc.dto.PccDTO;
import com.shaic.claim.pcc.dto.PccDetailsTableDTO;
import com.shaic.claim.reimbursement.searchuploaddocuments.SearchUploadDocumentsPagePresenter;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;

public class ProcessPCCUploadDocumentsGridForm extends ViewComponent implements Receiver,SucceededListener {
	
	private VerticalLayout mainaLayout;
	
	private Panel panel;
	
	private Button btnUpload;
	
	private BeanFieldGroup<PCCUploadDocumentsDTO> binder;
	
	private File file;
	
	private Upload upload;
	
	private String presenterString;
	
	private PCCUploadDocumentsDTO bean;
	
	private PccDTO pccDTO;
	
	private PccDetailsTableDTO pccDetailsTableDTO;
	
	private TextField txtFileUploadRemarks;
	
	private String screenName;
	
	private Long pcckey;

	public static String dataDir = System.getProperty("jboss.server.data.dir");
	
	public static String BYPASS_UPLOAD;

	public void init(PCCUploadDocumentsDTO bean,PccDTO pccDTO, String presenterString) {
		this.bean = bean;
		this.presenterString = presenterString;
		this.pccDTO = pccDTO;
		//this.pccDetailsTableDTO = pccDetailsTableDTO;
		setCompositionRoot(mainLayout());
	}
	
	

	public void initBinder() {
		this.binder = new BeanFieldGroup<PCCUploadDocumentsDTO>(
				PCCUploadDocumentsDTO.class);
		this.binder.setItemDataSource(this.bean);

	}

	private VerticalLayout mainLayout() {
		initBinder();
		readConnectionPropertyFile();

		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		mainaLayout = new VerticalLayout();
		btnUpload = new Button("Upload");
		btnUpload.setWidth("-1px");
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		upload = new Upload("", this);
		upload.setWidth("10%");
		upload.addSucceededListener(this);
		upload.setButtonCaption(null);
		
		
		if((SHAConstants.PCC_ZONAL_MEDICAL_HEAD_SCREEN).equalsIgnoreCase(this.presenterString))
		{
			panel = buildUploadDocLayoutFromZonal();
			//panel.setSpacing(false);
			mainaLayout.addComponent(panel);
			mainaLayout.addComponent(btnUpload);
			mainaLayout.setComponentAlignment(btnUpload, Alignment.MIDDLE_CENTER);
		}
		addListener();

		return mainaLayout;
	}
	
	private Panel buildUploadDocLayoutFromZonal()
	{
		txtFileUploadRemarks = binder.buildAndBind("", "fileUploadRemarks", TextField.class);
		txtFileUploadRemarks.setMaxLength(2000);
		
		GridLayout gridLayout = new GridLayout(2, 2);
		gridLayout.setWidth("70%");
		gridLayout.addComponent(new Label("File Upload"), 0, 0);
		gridLayout.addComponent(upload, 0, 1);

		gridLayout.addComponent(new Label("Remarks"), 1, 0);
		gridLayout.addComponent(txtFileUploadRemarks, 1, 1);
		
		Panel panel = new Panel();
		panel.setContent(gridLayout);
		
		return panel;
	}


	protected void addListener() {

		btnUpload.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				if (isValid()) {
					
					if(null != txtFileUploadRemarks && null != txtFileUploadRemarks.getValue() && !txtFileUploadRemarks.getValue().isEmpty()){
						bean.setFileUploadRemarks(txtFileUploadRemarks.getValue());
					}
						upload.submitUpload();
						
						
					}
			}
		});
	}

	private Boolean isValid() {
		Boolean hasError = false;
		StringBuffer eMsg = new StringBuffer();

			
		/*if(null != txtFileUploadRemarks && null != txtFileUploadRemarks.getValue() && !txtFileUploadRemarks.getValue().isEmpty()){
			System.out.println(String.format("File Upload Remarks[%s]", txtFileUploadRemarks.getValue()));
			bean.setFileUploadRemarks(txtFileUploadRemarks.getValue());
		}*/

		if (hasError) {
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
			label.setStyleName("errMessage");
			VerticalLayout layout = new VerticalLayout();
			layout.setMargin(true);
			layout.addComponent(label);
			ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("Errors");
			dialog.setClosable(true);
			dialog.setContent(layout);
			dialog.setResizable(true);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			hasError = true;
			return !hasError;
		} else {
			return true;
		}
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {
		try {
			//Added for error
			if(file.exists() && !file.isDirectory()) {
				byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);

				String fileName = event.getFilename();
				//String uploadedFileRemarks = event.get;

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

					WeakHashMap uploadStatus = SHAFileUtils.sendGeneratedFileToDMS(fileName, fileAsbyteArray, file);
					Boolean flagUploadSuccess = new Boolean(""+ uploadStatus.get("status"));
					// TO read file after load
					if (flagUploadSuccess.booleanValue()) {
						String token = "" + uploadStatus.get("fileKey");

						if(this.presenterString.equals(SHAConstants.PCC_ZONAL_MEDICAL_HEAD_SCREEN)){
							pcckey = pccDTO.getPccKey();
							String fileUploadRemarks = "";
							if(null != txtFileUploadRemarks && null != txtFileUploadRemarks.getValue() && !txtFileUploadRemarks.getValue().isEmpty()){
								bean.setFileUploadRemarks(txtFileUploadRemarks.getValue());
								 fileUploadRemarks = txtFileUploadRemarks.getValue();
							}
							fireViewEvent(ProcessPCCZonalMedicalHeadRequestPresenter.PCC_UPLOAD_EVENT,pcckey,fileName,token,SHAConstants.PCC_ZONAL_MEDICAL_HEAD_SCREEN,this.pccDTO,fileUploadRemarks);
						}

						result();
						txtFileUploadRemarks.setValue("");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void result(){
        Label successLabel = new Label("<b style = 'color: black;'>File Upload Successfully!!! </b>", ContentMode.HTML);
		
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
				
			}
		});
		
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

		FileOutputStream fos = null;
		try {
			filename = SHAUtils.getOnlyStrings(filename);
			this.file = new File(System.getProperty("jboss.server.data.dir")+ "/" + filename);
			if (null != file) {
				fos = new FileOutputStream(file);
			} else {
				Notification.show("Error", ""+ "Please select the file to be uploaded",
						Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {/* comented for error handled
			Notification.show("Error", ""
			+ "Please select the file to be uploaded",
			Type.ERROR_MESSAGE);*/
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

	public  Properties readConnectionPropertyFile() {
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
	
	public void disableFileUploadComponent()
	{
		if(null != upload)
		{
			upload.setEnabled(false);
			BYPASS_UPLOAD = "true";
		}
	}



	public PccDTO getPccDTO() {
		return pccDTO;
	}

	public void setPccDTO(PccDTO pccDTO) {
		this.pccDTO = pccDTO;
	}

	public Long getPcckey() {
		return pcckey;
	}

	public void setPcckey(Long pcckey) {
		this.pcckey = pcckey;
	}

	public PccDetailsTableDTO getPccDetailsTableDTO() {
		return pccDetailsTableDTO;
	}

	public void setPccDetailsTableDTO(PccDetailsTableDTO pccDetailsTableDTO) {
		this.pccDetailsTableDTO = pccDetailsTableDTO;
	}
	

}
