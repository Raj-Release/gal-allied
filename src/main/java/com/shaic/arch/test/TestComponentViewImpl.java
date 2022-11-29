package com.shaic.arch.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.vaadin.addon.cdimvp.AbstractMVPView;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.v7.ui.Upload;
import com.vaadin.v7.ui.Upload.Receiver;
import com.vaadin.v7.ui.Upload.SucceededEvent;
import com.vaadin.v7.ui.Upload.SucceededListener;
import com.vaadin.ui.themes.ValoTheme;

public class TestComponentViewImpl extends AbstractMVPView implements TestComponentView, NewItemHandler, ValueChangeListener,Receiver,SucceededListener  {

	private static final long serialVersionUID = -4195936415458592784L;

	private DiagnosisComboBox comboBox;
	
	private File file;
	
	@Inject
	private TestTable testTable;
	
	public TestTable getTestTable() {
		return testTable;
	}

	public void setTestTable(TestTable testTable) {
		this.testTable = testTable;
	}

	@EJB
	private MasterService masterService;
	
	final Embedded imageViewer = new Embedded("Uploaded Image");
	
	
	@Override
	public void testDiagnosisComponent() {
		
	}
	
	private Upload upload;
	
	
	
	private final Button createPageButton = new Button(
			"No result found. Create Page!");
	private final BrowserFrame wikipediaPage = new BrowserFrame();

	@PostConstruct
	protected void init() {
		addStyleName("wrapper");
		CssLayout layout = new CssLayout();
		layout.addStyleName("main-content");
		setCompositionRoot(layout);
		createPageButton.setVisible(false);
		imageViewer.setVisible(false);
		
		Label searchLabel = new Label("Search Wikipedia for");
		searchLabel.setWidth("100%");
		searchLabel.addStyleName("search-label");
		createPageButton.addStyleName("create-page-button");
		createPageButton.setWidth("100%");
		wikipediaPage.addStyleName("wikipedia-page");
		CssLayout header = new CssLayout(searchLabel, createPageButton);
		header.addStyleName("header");
		CssLayout body = new CssLayout(wikipediaPage);
		body.addStyleName("body");
		
		comboBox = new DiagnosisComboBox();
	    //Vaadin8-setImmediate() comboBox.setImmediate(true);
	    comboBox.addValueChangeListener(this);
	    SuggestingContainer container = new SuggestingContainer(masterService);
	    comboBox.setContainerDataSource(container);
	    
        upload  = new Upload("", this);	
        upload.addSucceededListener(this);
        upload.setButtonCaption(null);

        Button saveBtn = new Button("Save");
		
        
        
		saveBtn.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
			//	upload.submitUpload();
				//PremiaService p = new PremiaService();
//				p.getPolicyUnLock(null);
			}
		});
		
		testTable.init("", false, true);
		testTable.setHeight("100.0%");
		testTable.setWidth("100.0%");
		testTable.addStyleName((ValoTheme.TABLE_COMPACT));
		//searchForm.setHeight(600, Unit.PIXELS);

		/*List<TestDO> testListItem = new ArrayList<TestDO>();
		testListItem.add(new TestDO(1l, "Sivakumar", 1000l));
		testListItem.add(new TestDO(1l, "Vijay", 2000l));
		testListItem.add(new TestDO(1l, "Saravanan", 3000l));
		testListItem.add(new TestDO(1l, "Deepak", 4000l));
		testListItem.add(new TestDO(1l, "Satish", 5000l));*/
		
		imageViewer.setWidth("50%");
		imageViewer.setHeight("500px");
        layout.addComponent(upload);
        layout.addComponent(saveBtn);
        layout.addComponent(imageViewer);
		layout.addComponents(header, body, comboBox, testTable);
	}
	
	@Override
	public void resetView() {
		
	}

	@Override
	public void addNewItem(String newItemCaption) {
		SelectValue selectValue = new SelectValue(null, newItemCaption);
		fireViewEvent(TestComponentPresenter.TEST_DIAGNOSIS_ADD_ITEM_COMBO, selectValue);
	}

	public void newItemAdded(SelectValue value) {
//		combobox.addItem(value);
//		combobox.setValue(value);
	}

	@Override
	public void showSearchResult(List<SelectValue> list) {
//		if (list != null && !list.isEmpty()) {
//			for (SelectValue item : list) {
//				combobox.addItem(item);
//			}
//		} else {
//			Notification.show("New Diagnosis need to be added!");
//		}

	}

	@Override
	public void valueChange(ValueChangeEvent event) {
	}

	@Override
	public void uploadSucceeded(SucceededEvent event) {

		try {
			byte[] fileAsbyteArray = FileUtils.readFileToByteArray(file);
			WeakHashMap uploadStatus = SHAFileUtils.sendFileToDMSServer(event.getFilename(), fileAsbyteArray);
			Boolean flagUploadSuccess = new Boolean("" + uploadStatus.get("status"));
			//TO read file after load
			if (flagUploadSuccess.booleanValue())
			{
				String token = "" + uploadStatus.get("fileKey");

				String imageUrl = SHAFileUtils.viewFileByToken(token);
			    imageViewer.setSource(new ExternalResource(imageUrl));
			    imageViewer.setVisible(true);  

//				String imageUrl = SHAFileUtils.viewFileByToken(token);
//			    imageViewer.setSource(new ExternalResource(imageUrl));
			    imageViewer.setVisible(true);  

			}
			else
			{
				Notification.show("Error", "" + uploadStatus.get("message"), Type.ERROR_MESSAGE);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {

//		File file = new File("D:\\software\\project\\DMS_Client_Location\\DMSIntegrationCoreClient\\src\\Chrysanthemum.jpg");
		this.file = new File(System.getProperty("jboss.server.data.dir") +"/" + filename);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			//uploadStatus = fileUploadBean.fileUpload(appId,tokenId,template,fileAsbyteArray,filename);
//			System.out.println(" fileUpload ENDS ==>" + uploadStatus);
			return fos;
		
	
}
	}
