package com.shaic.claim.lumen.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.lumen.LumenDbService;
import com.shaic.claim.lumen.create.LumenRequestDTO;
import com.shaic.claim.lumen.create.LumenSearchResultTableDTO;
import com.shaic.domain.DocumentDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings("serial")
public class LumenUploadDocumentsViewImpl extends AbstractMVPView implements Receiver,SucceededListener,LumenUploadDocumentsView{
	private GridLayout gridLayout;

	private VerticalLayout mainLayout;

	@Inject
	private LumenUploadedDocumentTable documentTable;

	private Button btnUpload;
	
	private ComboBox cmb_Category;

	private Upload 	fileBrowser;
	private File file;

	private FileData fileData;	
	@Inject
	private LumenDbService lumenDbService; 

	private Object dtobean;


	@PostConstruct
	public void initView() {

	}

	public void init(Object resultDTO, String caption) {
		dtobean = resultDTO;
		mainLayout = new VerticalLayout();
		Panel panel = new Panel(caption);
		panel.setHeight("100px");
		panel.setWidth("800px");
		panel.setStyleName("policyinfogrid");
		panel.setContent(buildCarousel());	

		btnUpload = new Button();
		btnUpload.setCaption("Upload");
		btnUpload.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		btnUpload.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				fileBrowser.submitUpload();
			}
		});
		
		VerticalLayout panelLayout = new VerticalLayout(panel);
		VerticalLayout buttonLayout = new VerticalLayout(btnUpload);
		buttonLayout.setComponentAlignment(btnUpload, Alignment.BOTTOM_RIGHT);
		HorizontalLayout fieldLayout = new HorizontalLayout(panelLayout,buttonLayout);

		mainLayout.addComponent(fieldLayout);
		documentTable.init("", false, false);
		mainLayout.addComponent(documentTable);

		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true,true,true,true));
		/*if(resultDTO instanceof LumenSearchResultTableDTO){
			//loadUploadedDocuments(((LumenSearchResultTableDTO)resultDTO).getClaim().getIntimation().getIntimationId());
			loadUploadedDocuments();
		}else if(resultDTO instanceof LumenRequestDTO){
			//loadUploadedDocuments(((LumenRequestDTO)resultDTO).getClaim().getIntimation().getIntimationId());
			loadUploadedDocuments();
		}*/
		
		loadUploadedDocuments();
		setCompositionRoot(mainLayout);
	}

	private GridLayout buildCarousel() {

		gridLayout = new GridLayout(3,2);

		Label uploadLabel = new Label("File Upload"); 
		gridLayout.addComponent(uploadLabel, 0,0);
		gridLayout.setComponentAlignment(uploadLabel, Alignment.MIDDLE_CENTER);

		Label categoryLabel = new Label("File Category");
		gridLayout.addComponent(categoryLabel, 1,0);
		gridLayout.setComponentAlignment(categoryLabel, Alignment.MIDDLE_CENTER);

		fileBrowser = new Upload("", this);	
		fileBrowser.addSucceededListener(this);
		fileBrowser.setButtonCaption(null);

		gridLayout.addComponent(fileBrowser, 0,1);
		gridLayout.setComponentAlignment(fileBrowser, Alignment.MIDDLE_CENTER);

		cmb_Category = new ComboBox();
		gridLayout.addComponent(cmb_Category, 1,1);
		gridLayout.setComponentAlignment(cmb_Category, Alignment.MIDDLE_CENTER);

		gridLayout.setSpacing(true);

		setDropDownValues();
		return gridLayout;
	}

	@SuppressWarnings("unchecked")
	public void setDropDownValues(){
		BeanItemContainer<SelectValue> cmb_Category_values = lumenDbService.getFileCategory();
		cmb_Category.setContainerDataSource(cmb_Category_values);
		cmb_Category.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb_Category.setItemCaptionPropertyId("value");

		List<SelectValue> defaultType = (List<SelectValue>)cmb_Category.getContainerDataSource().getItemIds();
		cmb_Category.setValue(defaultType.get(0));
		cmb_Category.setReadOnly(true);
	}

	@SuppressWarnings("rawtypes")
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
					File convertedFile  = SHAFileUtils.convertImageToPDF(event.getFilename());
					fileName = convertedFile.getName();
					fileAsbyteArray = FileUtils.readFileToByteArray(convertedFile);
				}

				WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
				Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
				//TO read file after load
				if (flagUploadSuccess.booleanValue())
				{
					String token = String.valueOf(uploadStatus.get("fileKey"));
					fileData = new FileData();
					fileData.setFileName(fileName);
					fileData.setToken(token);
					if(dtobean instanceof LumenSearchResultTableDTO){
						DocumentDetails temp = lumenDbService.updateDocumentTable(dtobean,fileData);
						if(temp != null){
							//((LumenSearchResultTableDTO)dtobean).setUserUploadedDocument(lumenDbService.updateDocumentTable(dtobean,fileData));
							((LumenSearchResultTableDTO)dtobean).setUserUploadedDocument(temp);
							((LumenSearchResultTableDTO)dtobean).getListOfUserUploadedDocuments().add(((LumenSearchResultTableDTO)dtobean).getUserUploadedDocument());
						}
					}else if(dtobean instanceof LumenRequestDTO){
						DocumentDetails temp = lumenDbService.updateDocumentTable(dtobean,fileData);
						if(temp != null){
							//((LumenRequestDTO)dtobean).setUserUploadedDocument(lumenDbService.updateDocumentTable(dtobean,fileData));
							((LumenRequestDTO)dtobean).setUserUploadedDocument(temp);
							((LumenRequestDTO)dtobean).getListOfUserUploadedDocuments().add(((LumenRequestDTO)dtobean).getUserUploadedDocument());
						}
					}

					List<DocumentTableDTO> uploadedDocumentsList = null;
					List<DocumentTableDTO> retainedList = new ArrayList<DocumentTableDTO>();

					if(dtobean instanceof LumenSearchResultTableDTO){
						uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenSearchResultTableDTO)dtobean).getListOfUserUploadedDocuments());
					}else if(dtobean instanceof LumenRequestDTO){
						uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenRequestDTO)dtobean).getListOfUserUploadedDocuments());
					}

					documentTable.removeRow();
					
					for(DocumentTableDTO rec : uploadedDocumentsList){
						if(rec.getDeletedFlag().equals("N")){
							retainedList.add(rec);
						}
					}

					Page<DocumentTableDTO> page = new Page<DocumentTableDTO>();
					page.setPageItems(retainedList);
					page.setTotalRecords(retainedList.size());
					page.setTotalList(retainedList);

					documentTable.setTableList(retainedList);
					documentTable.setSubmitTableHeader();
					documentTable.tablesize();
					documentTable.setHasNextPage(page.isHasNext());
				} else {
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
			if(null != file){
				fos = new FileOutputStream(file);
			}else{
				Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
//			Notification.show("Error", "" + "Please select the file to be uploaded", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
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

	public void loadUploadedDocuments(){
		List<DocumentTableDTO> uploadedDocumentsList = null;
		if(dtobean instanceof LumenSearchResultTableDTO){ 
			documentTable.setListOfUserUploadedDocuments(((LumenSearchResultTableDTO)dtobean).getListOfUserUploadedDocuments());
			uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenSearchResultTableDTO)dtobean).getListOfUserUploadedDocuments());
		}else if(dtobean instanceof LumenRequestDTO){
			documentTable.setListOfUserUploadedDocuments(((LumenRequestDTO)dtobean).getListOfUserUploadedDocuments());
			uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenRequestDTO)dtobean).getListOfUserUploadedDocuments());
		}
		for(DocumentTableDTO rec : uploadedDocumentsList){
			if(rec.getDeletedFlag().equals("N")){
				documentTable.addBeanToList(rec);
			}
		}
	}

	public void reloadDocumentPopup(DocumentTableDTO docTblDTO){
		List<DocumentTableDTO> uploadedDocumentsList = null;
		if(dtobean instanceof LumenSearchResultTableDTO){
			uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenSearchResultTableDTO)dtobean).getListOfUserUploadedDocuments());
		}else if(dtobean instanceof LumenRequestDTO){
			uploadedDocumentsList = lumenDbService.getDocumentDetailsFromCache(((LumenRequestDTO)dtobean).getListOfUserUploadedDocuments());
		}
		documentTable.removeRow();
		for(DocumentTableDTO rec : uploadedDocumentsList){
			if(rec.getDeletedFlag().equals("N")){
				documentTable.addBeanToList(rec);
			}
		}
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetSearchResultTableValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub

	}

}
