package com.shaic.paclaim.health.reimbursement.listenertable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDetailsPresenter;
import com.shaic.claim.rod.wizard.tables.BillEntryListenerTable;
import com.shaic.claims.reibursement.addaditionaldocuments.DetailsPresenter;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.shaic.paclaim.reimbursement.medicalapproval.processclaimrequest.pages.dataextraction.PAClaimRequestDataExtractionPagePresenter;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillEntryDetailsPresenter;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PAHealthBillEntryTableUI extends ViewComponent {
	
	private static final long serialVersionUID = 1168621638170269724L;

	@Inject
	private BillEntryListenerTable billEntryTable;
	
	@Inject
	private PAHealthBillReviewListenerTable billReviewTable;
	
	private Panel tablePanel;
	
	private VerticalLayout mainLayout;
	
	private UploadDocumentDTO bean;
	
	private OptionGroup optBillEntryStatus;
	
	private BeanFieldGroup<BillEntryDetailsDTO> binder;
	
	private Window popup;
	
	private TextField txtBillNo;
	
	private DateField txtBillDate;
	
	private TextField txtNoOfItems;
	
	private TextField txtBillValue;
	
	private TextArea txtZonalRemarks;
	
	private TextArea txtCorporateRemarks;
	
	private String presenterString;
	
	final Embedded imageViewer = new Embedded("Uploaded Image");

	private TextArea billingRemarks;
	
	private Map<String, String> classificationValidationMap;
	
	private  Map<String, Object> referenceDataMap;
	
	private TextField txtIntimationNo;
	
	private TextField txtDateOfAdmission;
	
	private TextField txtDateOfDischarge;
	
	private TextField txtInsuredPatientName;
	
	private TextField addmissionDateFld;
	
	private TextField dischargeDateFld;
	
	private int iBillEntryTableSize;
	
	
	private Button viewUploadedDocument;
	
	private Button viewClaimsDMSDocument;
	
	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	private FormLayout viewUploadedDocForm;
	
	private VerticalLayout vLayout ;
	
	/**
	 * Added for enabling save option in 
	 * bill entry popup.
	 * */
	
	//private OptionGroup optSaveBillEntryDetails;
	private Button optSaveBillEntryDetails;
	
	
	//private VerticalLayout viewDocLayout ;
	
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void initPresenter(String presenterString, int iSize)
	{
		this.presenterString = presenterString; 
		iBillEntryTableSize = iSize;
	}
	
	public void initPresenter(String presenterString, TextField addmissionDate , TextField dischargeDate)
	{
		this.presenterString = presenterString;
		addmissionDateFld = addmissionDate;
		dischargeDateFld = dischargeDate;
	}
	
	public void init(UploadDocumentDTO bean, Map<String, Object> referenceDataMap,Window popup,Boolean isRemarksRequired, Boolean isZonalReview, Boolean isBilling, Boolean isFinancial){
		
		this.bean = bean;
		this.popup = popup;
		
		this.binder = new BeanFieldGroup<BillEntryDetailsDTO>(BillEntryDetailsDTO.class);
		this.binder.setItemDataSource(this.bean.getBillEntryDetailsDTO());
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		this.referenceDataMap = referenceDataMap;
		
		//viewDocLayout = new VerticalLayout();
		vLayout = new VerticalLayout();
		
		txtBillNo = (TextField)binder.buildAndBind("Bill No","billNo",TextField.class);
		
		txtBillNo.setValue(this.bean.getBillNo());
		txtBillNo.setEnabled(false);
		CSValidator billNoValidator = new CSValidator();
		billNoValidator.extend(txtBillNo);
		billNoValidator.setRegExp("^[a-zA-Z0-9]*$");
		billNoValidator.setPreventInvalidTyping(true);
		txtBillNo.setMaxLength(20);
		
		txtBillDate = (DateField)binder.buildAndBind("Bill Date","billDate",DateField.class);
		
		txtBillDate.setValue(this.bean.getBillDate());
		txtBillDate.setEnabled(false);
	//	handleEnterForBillDate(txtBillDate,null);
		/*if(getUI().getCurrent().getPage().getWebBrowser().isIE())
		{
			final Window dialog = new Window();
			dialog.setContent(txtBillDate);
			dialog.setPositionX(450);
			dialog.setPositionY(500);
			getUI().getCurrent().addWindow(dialog);
		}*/
		
		txtNoOfItems = (TextField)binder.buildAndBind("No of Items","noOfItems",TextField.class);
		
		txtNoOfItems.setValue(String.valueOf(this.bean.getNoOfItems()));
		txtNoOfItems.setEnabled(false);
		
		txtBillValue = (TextField)binder.buildAndBind("Bill Value","billValue",TextField.class);
		txtBillValue.setNullRepresentation("");
		txtBillValue.setValue(this.bean.getBillValue() != null ? String.valueOf(this.bean.getBillValue()) : "");
		
		txtBillValue.setEnabled(false);
		
		txtIntimationNo = new TextField("Intimation No");
		//txtIntimationNo = (TextField)binder.buildAndBind("Intimation No","intimationNo",TextField.class);
		txtIntimationNo.setEnabled(false);
		if(null != bean.getIntimationNo())
		txtIntimationNo.setValue(bean.getIntimationNo());
		
		
		txtDateOfAdmission = new TextField("Date Of Admission");
		//txtDateOfAdmission = (TextField)binder.buildAndBind("Date Of Admission","dateOfAdmission",TextField.class);
		txtDateOfAdmission.setEnabled(false);
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
		{
			if(null != addmissionDateFld)
				txtDateOfAdmission.setValue(addmissionDateFld.getValue());
		}
		else
		{
			if(null != bean.getDateOfAdmission())
				txtDateOfAdmission.setValue(bean.getDateOfAdmission());
		}
		
		txtDateOfDischarge = new TextField("Date Of Discharge");
		//txtDateOfDischarge = (TextField)binder.buildAndBind("Date Of Discharge","dateOfDischarge",TextField.class);
		txtDateOfDischarge.setEnabled(false);
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
		{
			if(null != dischargeDateFld)
				txtDateOfDischarge.setValue(dischargeDateFld.getValue());
		}
		else
		{
			if(null != bean.getDateOfDischarge())
				txtDateOfDischarge.setValue(bean.getDateOfDischarge());
		}
		
		txtInsuredPatientName = new TextField("Insured Patient Name");
		txtInsuredPatientName.setEnabled(false);
		if(null != bean.getInsuredPatientName())
			txtInsuredPatientName.setValue(bean.getInsuredPatientName());
		
		
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
			 {
				txtBillNo.setEnabled(true);
				txtBillDate.setEnabled(true);
				
			 }
			 
		
		this.viewUploadedDocument = new Button("View Document");
		viewUploadedDocument.setStyleName(ValoTheme.BUTTON_LINK);

		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);

		
		/*FormLayout formLayout1 = new FormLayout(txtBillNo , txtBillDate);
		FormLayout formLayout2 = new FormLayout(txtNoOfItems, txtBillValue);
		
		HorizontalLayout hLayout1 = new HorizontalLayout(formLayout1 , formLayout2);*/
		
		FormLayout txtBillNoForm = new FormLayout(txtBillNo) ;
		txtBillNoForm.setMargin(false);
		FormLayout txtBillDateForm = new FormLayout(txtBillDate);   
		txtBillDateForm.setMargin(false);
		FormLayout txtNoOfItemsForm = new FormLayout(txtNoOfItems);
		txtNoOfItemsForm.setMargin(false);
		FormLayout txtBillValueForm = new FormLayout(txtBillValue);
		txtBillValueForm.setMargin(false);
		
		//FormLayout viewUploadedDocForm = new FormLayout(viewUploadedDocument);
		viewUploadedDocForm = new FormLayout(viewUploadedDocument);
		viewUploadedDocForm.setMargin(false);
		
		FormLayout viewClaimsDMSDocumentForm = new FormLayout(viewClaimsDMSDocument);
		//FormLayout viewClaimsDMSDocumentForm = new FormLayout();
		viewClaimsDMSDocumentForm.setMargin(false);
		
		//HorizontalLayout hLayout = new HorizontalLayout(txtBillNoForm ,txtBillDateForm,txtNoOfItemsForm,txtBillValueForm,viewUploadedDocForm,viewClaimsDMSDocumentForm);
		HorizontalLayout hLayout = new HorizontalLayout(txtBillNoForm ,txtBillDateForm,txtNoOfItemsForm,txtBillValueForm);
		//hLayout.setSpacing(true);\
		hLayout.setSpacing(true);
//		hLayout.setMargin(true);
		FormLayout txtIntimationNoForm = new FormLayout(txtIntimationNo);
		txtIntimationNoForm.setMargin(false);
		FormLayout txtInsuredPatientNameForm = new FormLayout(txtInsuredPatientName);
		txtInsuredPatientNameForm.setMargin(false);
		FormLayout txtDateOfAdmissionForm = new FormLayout(txtDateOfAdmission);
		txtDateOfAdmissionForm.setMargin(false);
		FormLayout txtDateOfDischargeForm = new FormLayout(txtDateOfDischarge);
		txtDateOfDischargeForm.setMargin(false);
		HorizontalLayout hLayout1 = new HorizontalLayout(txtIntimationNoForm,txtInsuredPatientNameForm,txtDateOfAdmissionForm,txtDateOfDischargeForm);		
		hLayout1.setSpacing(true);
//		hLayout1.setMargin(true);
		
		VerticalLayout mainHor = new VerticalLayout(hLayout,hLayout1);
		mainHor.setSpacing(true);
//		mainHor.setHeight("50%");
	
		
		//billEntryTable.addBeanToList(bean);
		
		
		//imageViewer.setWidth("50%");
		//imageViewer.setHeight("500px");
		
		// HorizontalSplitPanel hsplitPanel = new HorizontalSplitPanel();
		 
		  VerticalSplitPanel vsplitPanel = new VerticalSplitPanel();
		  HorizontalLayout hTblLayout = new HorizontalLayout();

		 //Below null check is for bypassing dms
		if(null != bean.getFileName())
		{
		if(bean.getFileName().endsWith(".JPG") || bean.getFileName().endsWith(".jpg") || bean.getFileName().endsWith(".jpeg") )
		{
			String imageUrl = SHAFileUtils.viewFileByToken(this.bean.getDmsDocToken());
			if(imageUrl != null) {
				//ExternalResource external = new ExternalResource(imageUrl);
				
				imageViewer.setSource(new ExternalResource(imageUrl));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("500px");
			    imageViewer.setWidth("70%");
			    imageViewer.setSizeFull();
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    vsplitPanel.setFirstComponent(imagePanel);	
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"SampleBill.JPG";
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				imageViewer.setSource(new StreamResource(s, "SampleBill.jpg"));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("500px");
			    imageViewer.setWidth("70%");
			    imageViewer.setSizeFull();
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    imagePanel.setHeight("700px");
			    vsplitPanel.setFirstComponent(imagePanel);	
//				final String url = System.getProperty("jboss.server.data.dir") + "/"
//						+"SampleBill.JPG";
//				imageViewer.setSource(new ExternalResource(url));
//			    imageViewer.setVisible(true);  
//			    imageViewer.setHeight("500px");
//			    Panel imagePanel = new Panel();
//			    imagePanel.setContent(imageViewer);
//			    hsplitPanel.setFirstComponent(imagePanel);	

			//    hsplitPanel.setFirstComponent(imagePanel);	
				/*final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"SampleBill.JPG";
				imageViewer.setSource(new ExternalResource(url));
			    imageViewer.setVisible(true);  
			    imageViewer.setHeight("500px");
			    Panel imagePanel = new Panel();
			    imagePanel.setContent(imageViewer);
			    hsplitPanel.setFirstComponent(imagePanel);	*/

			}
		
		    
			
		}
		
		/**
		 * Code added for viewing html file.... Added by Vijay. 
		 * */
		else if(bean.getFileName().endsWith(".html") || bean.getFileName().endsWith(".HTML"))// || bean.getGalaxyFileName().endsWith(".PDF") || bean.getGalaxyFileName().endsWith("pdf"))
		{
			final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
			if(null != imageUrl)
			{
				
				 Embedded e = new Embedded();
			        e.setSizeFull();
			        e.setType(Embedded.TYPE_BROWSER);
		         StreamResource.StreamSource source = new StreamResource.StreamSource() {
                        public InputStream getStream() {
                           
                        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        	InputStream is = null;
                        	URL u = null;
                        	try {
                        		u =  new URL(imageUrl);
                        	  is = u.openStream();
                        	  
                        	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
                        	  int n;

                        	  while ( (n = is.read(byteChunk)) > 0 ) {
                        	    baos.write(byteChunk, 0, n);
                        	  }
                        	}
                        	catch (IOException e) {
                        	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                        	  e.printStackTrace ();
                        	  // Perform any other exception handling that's appropriate.
                        	}
                        	finally {
                        	  if (is != null) {
                        		  try
                        		  {
                        			  is.close();
                        		  }
                        		  catch(Exception e)
                        		  {
                        			  e.printStackTrace();
                        		  }
                        		  }
                        	}
                        	return new ByteArrayInputStream(baos.toByteArray());
                        }
                };
                StreamResource r = new StreamResource(source, bean.getFileName());
                r.setMIMEType("text/html");
                r.setStreamSource(source);
                r.setFilename(bean.getFileName());
			    e.setSource(r);
			    vsplitPanel.setFirstComponent(e);
				
				//Button saveExcel = new Button();
				//AdvancedFileDownloader adv = new AdvancedFileDownloader();
				//Resource res = new FileResource(new File(imageUrl));
				/*final ExternalResource res = new ExternalResource(imageUrl);
			
				//res.setMIMEType("application/pdf");
				//FileDownloader fd = new FileDownloader(res);
				//fd.setOverrideContentType(false);
				//fd.setFileDownloadResource(res);
				fd.extend(editButton);
				
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+ bean.getFileName();
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(res);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s, bean.getFileName());
		        r.setMIMEType("application/pdf");
		       // Resource downloadedResources = fd.getFileDownloadResource();
		        
		     //   e.setSource(fd.getFileDownloadResource());
		        e.setSource(r);
		        showPopup(e);*/
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"BILL.PDF";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s,"BILL.PDF");
		        r.setMIMEType("application/pdf");
		        r.setFilename("BILL.PDF");
		        e.setSource(r);
		        vsplitPanel.setFirstComponent(e);
			}
		}
		
		// Code ends here
		

		//else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith(".pdf") || bean.getFileName().endsWith("docx") || bean.getFileName().endsWith("doc"))
		//else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith(".pdf") || )
		else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith("pdf")  || bean.getFileName().endsWith("doc") || bean.getFileName().endsWith("docx"))

		{
			final String imageUrl = SHAFileUtils.viewFileByToken(this.bean.getDmsDocToken());
			if(null != imageUrl)
			{
/*				Button saveExcel = new Button();
				Resource res = new FileResource(new File(imageUrl));
				FileDownloader fd = new FileDownloader(res);
				fd.extend(saveExcel);
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+ bean.getFileName();
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s, bean.getFileName());
		        r.setMIMEType("application/pdf");
		        e.setSource(r);
		        e.setSizeFull();*/
				 Embedded e = new Embedded();
			     e.setSizeFull();
			     e.setType(Embedded.TYPE_BROWSER);
		         StreamResource.StreamSource source = new StreamResource.StreamSource() {
                     public InputStream getStream() {
                        
                     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     	InputStream is = null;
                     	URL u = null;
                     	try {
                     		u =  new URL(imageUrl);
                     		
                     	  is = u.openStream();
                     	  
                     	  
                     	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
                     	  int n;

                     	  while ( (n = is.read(byteChunk)) > 0 ) {
                     	    baos.write(byteChunk, 0, n);
                     	  }
                     	}
                     	catch (IOException e) {
                     	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
                     	  e.printStackTrace ();
                     	  // Perform any other exception handling that's appropriate.
                     	}
                     	finally {
                     	  if (is != null) {
                     		  try
                     		  {
                     			  is.close();
                     		  }
                     		  catch(Exception e)
                     		  {
                     			  e.printStackTrace();
                     		  }
                     		  }
                     	}
                     	return new ByteArrayInputStream(baos.toByteArray());
                     }
             };
             StreamResource r = new StreamResource(source, bean.getFileName());
             if(bean.getFileName().endsWith("docx") || bean.getFileName().endsWith("doc"))
             {
             	r.setMIMEType("application/msword");
             }
             else
             {
             	r.setMIMEType("application/pdf");
             }
             //r.setMIMEType("application/pdf");
             
             r.setFilename(bean.getFileName()+System.currentTimeMillis());
             r.setStreamSource(source);
			 e.setSource(r);
		        vsplitPanel.setFirstComponent(e);
			}
			else
			{
				final String url = System.getProperty("jboss.server.data.dir") + "/"
						+"BILL.PDF";
		        Embedded e = new Embedded();
		        e.setSizeFull();
		        e.setType(Embedded.TYPE_BROWSER);
				StreamResource.StreamSource s = new StreamResource.StreamSource() {
	
					public FileInputStream getStream() {
						try {
							File f = new File(url);
							FileInputStream fis = new FileInputStream(f);
							return fis;
						} catch (Exception e) {
							e.printStackTrace();
							return null;
						}
					}
				};
				StreamResource r = new StreamResource(s, "BILL.PDF");
		        r.setMIMEType("application/pdf");
		        r.setFilename("BILL.PDF");
		        e.setSource(r);
		        e.setSizeFull();
		        vsplitPanel.setFirstComponent(e);
				//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
			}
			//Added for dms
		}
		//Add html code here
		}
	   /* hsplitPanel.addComponent(imagePanel);
	    hsplitPanel.addComponent(billEntryTable);*/

		
		/**
		 * As of now only for zonal review process claim billing is implemented.
		 * Later , the same will be incorporated for remaining screens in billing.
		 * */
		if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) ||  (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
		{
			billReviewTable.initPresenter(this.presenterString);
			billReviewTable.init(bean);
			billReviewTable.setReferenceData(referenceDataMap);
			
			VerticalLayout vLayout = new VerticalLayout(billReviewTable);
			vLayout.setWidth("100%");
			vLayout.setHeight("90%");
			
			//hTblLayout.addComponent(billReviewTable);
			
			if((SHAConstants.BILLING).equalsIgnoreCase(presenterString) ||  (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) )
			{
				vsplitPanel.setSecondComponent(billReviewTable);
			}
			else if ((SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString))
			{
				vsplitPanel.setSecondComponent(vLayout);
			}
			
			//vsplitPanel.setSecondComponent(vLayout);
		}
		else
		{
			billEntryTable.initPresenter(this.presenterString,iBillEntryTableSize);
			billEntryTable.init(bean);
			billEntryTable.setReferenceData(referenceDataMap);
			//hTblLayout.addComponent(billEntryTable);
			
			vsplitPanel.setSecondComponent(billEntryTable);
		}
		
	//	hsplitPanel.setSecondComponent(billEntryTable);
	//	hsplitPanel.setSplitPosition(27);
		hTblLayout.setMargin(true);
		//hTblLayout.setSizeFull();
		hTblLayout.setWidth("300%");
		vsplitPanel.setSplitPosition(50);
		//vsplitPanel.setSizeFull();
		vsplitPanel.setHeight("1100px");
		vsplitPanel.setWidth("100.0%");
		
		
		VerticalLayout verticalLayout = new VerticalLayout();
//		verticalLayout.addComponent(hLayout1);
//		verticalLayout.addComponent(hLayout);
		verticalLayout.addComponent(mainHor);
		verticalLayout.addComponent(vsplitPanel);
		//verticalLayout.addComponent(viewDocLayout);
		//viewDocLayout.setVisible(false);
	/*	verticalLayout.addComponent(imagePanel);
		verticalLayout.addComponent(billEntryTable);*/
		verticalLayout.setHeight("100%");
		
		
		
		
	    
	    
		
		//optBillEntryStatus = (OptionGroup)binder.buildAndBind("Bill Entry / Review Completed","billEntryStatus",OptionGroup.class);
		
		optBillEntryStatus = new OptionGroup("Bill Entry / Review Completed");
		billEntryStatusListener();
		optBillEntryStatus.setRequired(true);
		optBillEntryStatus.addItems(getReadioButtonOptions());
		/*Item item1 = optPaymentMode.addItem(true);
		Item item2 = optPaymentMode.addItem(false);	*/
		optBillEntryStatus.setItemCaption(true, "Yes");
		optBillEntryStatus.setItemCaption(false, "No");
		optBillEntryStatus.setStyleName("horizontal");
		
		optSaveBillEntryDetails = new Button("Save Bill Entry Details");
		billEntrySaveListener();
		optSaveBillEntryDetails.setStyleName("horizontal");
		
		
		
		//optBillEntryStatus.setValue(null);
		//optBillEntryStatus.select(true);
		
		tablePanel = new Panel();
		tablePanel.setContent(verticalLayout);
		tablePanel.setHeight("100%");

		
		FormLayout formLayout = new FormLayout(optBillEntryStatus);
		//Added for enabling save option in bill entry screen
		FormLayout saveFormLayout = new FormLayout(optSaveBillEntryDetails);
		
//		HorizontalLayout billStatusLayout = new HorizontalLayout(formLayout);
		HorizontalLayout billStatusLayout = new HorizontalLayout(formLayout,saveFormLayout);
		billStatusLayout.setComponentAlignment(saveFormLayout, Alignment.BOTTOM_RIGHT);
		billStatusLayout.setComponentAlignment(formLayout, Alignment.BOTTOM_RIGHT);
		//Added for enabling save option in bill entry screen
		
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addComponent(billStatusLayout);
		
		if(isRemarksRequired)
		{
			txtZonalRemarks = (TextArea)binder.buildAndBind("Dr Remarks (Zonal)","zonalRemarks",TextArea.class);
			txtZonalRemarks.setMaxLength(4000);
//			txtZonalRemarks.setWidth("400px");
			txtCorporateRemarks = (TextArea)binder.buildAndBind("Dr Remarks (Corporate)","corporateRemarks",TextArea.class);
			txtCorporateRemarks.setMaxLength(4000);
//			txtCorporateRemarks.setWidth("400px");
			if(isZonalReview)
			{
				txtCorporateRemarks.setEnabled(false);
			}
			else
			{
				txtZonalRemarks.setEnabled(false);
			}
			
			if(bean.getZonalRemarks() != null){
				txtZonalRemarks.setValue(bean.getZonalRemarks());
			}
			if(bean.getCorporateRemarks() != null){
				txtCorporateRemarks.setValue(bean.getCorporateRemarks());
			}
			if(bean.getZonalRemarks() != null && bean.getCorporateRemarks() != null){
				txtCorporateRemarks.setEnabled(false);
			}
			
			if(presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)){
				txtCorporateRemarks.setEnabled(true);
			}
			//FormLayout remarksFormLayout = new FormLayout (txtZonalRemarks,txtCorporateRemarks);
			HorizontalLayout remarksFormLayout = new HorizontalLayout(new FormLayout(txtZonalRemarks) , new FormLayout(txtCorporateRemarks));
			
			if(isBilling || isFinancial) {
				txtZonalRemarks.setEnabled(false);
				txtCorporateRemarks.setEnabled(false);
				if(isBilling) {
					billingRemarks = (TextArea)binder.buildAndBind("Billing Remarks","billingRemarks",TextArea.class);
					billingRemarks.setMaxLength(4000);
					if(this.bean.getBillingRemarks() != null){
						billingRemarks.setValue(this.bean.getBillingRemarks());
					}
				}
				
				if(isFinancial) {
					if(this.bean.getBillingRemarks() != null && ! this.bean.getBillingRemarks().equalsIgnoreCase("")){
						this.bean.getBillEntryDetailsDTO().setBillingRemarks(this.bean.getBillingRemarks());
					}
					billingRemarks = (TextArea)binder.buildAndBind("Billing Remarks","billingRemarks",TextArea.class);
					billingRemarks.setMaxLength(4000);
					billingRemarks.setEnabled(false);
//					billingRemarks = new TextArea("Billing Remarks");
//					billingRemarks.setValue(bean.getBillingRemarks() != null ? bean.getBillingRemarks() :"");
//					billingRemarks.setEnabled(false);
//					billingRemarks.setMaxLength(200);
				
				}
				remarksFormLayout.addComponent(new FormLayout (billingRemarks));
			}
			HorizontalLayout remarksLayout = new HorizontalLayout(remarksFormLayout);
			vLayout.addComponent(remarksLayout);
		}
	
		
		
		mainLayout = new VerticalLayout();
		mainLayout.addComponent(tablePanel);
		
		if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) ||  (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) )
		{
			vsplitPanel.setHeight("800px");
			vsplitPanel.setSplitPosition(70);
			vsplitPanel.setSecondComponent(vLayout);
			mainLayout.addComponent(billReviewTable);
		}
		else
		{
			mainLayout.addComponent(vLayout);
		}
		//mainLayout.addComponent(viewDocLayout);
		setTableValues();
		setCompositionRoot(mainLayout);
		addListener();
		
	}
	
	private void addListener()
	{
		if((SHAConstants.BILLING).equalsIgnoreCase(presenterString) || (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
		{
			if(null != addmissionDateFld)
			{
				addmissionDateFld.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(null != event)
						{
							String value = (String)event.getProperty().getValue();
							txtDateOfAdmission.setValue(value);
						}
					}
				});
			}
			
			
			if(null != dischargeDateFld)
			{
				dischargeDateFld.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						if(null != event)
						{
							String value = (String)event.getProperty().getValue();
							dischargeDateFld.setValue(value);
						}
					}
				});
			}
		}
		
		if(null != viewUploadedDocument)
		{
			
			viewUploadedDocument.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

					
						
						@SuppressWarnings("deprecation")
						public void buttonClick(ClickEvent event) {
							BrowserWindowOpener opener = null;
						//	Window window = new Window();
							
							if(null != bean.getFileName())
							{
								if(bean.getFileName().endsWith(".JPG") || bean.getFileName().endsWith(".jpg") || bean.getFileName().endsWith(".jpeg"))
								{
									 String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
									
									if(imageUrl != null) {	
										
										//window.getUI().getPage().open(imageUrl, "_blank",true);
										
										opener = new BrowserWindowOpener(imageUrl);
										
										opener.setFeatures("height=693,width=1400");
										//opener.setWindowName("_blank");
										opener.extend(viewUploadedDocument);
										
									   
										
									}
									else
									{
										final String url = System.getProperty("jboss.server.data.dir") + "/"
												+"SampleBill.JPG";
										StreamResource.StreamSource s = new StreamResource.StreamSource() {
											public FileInputStream getStream() {
												try {
													File f = new File(url);
													FileInputStream fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												}
											}
										};
										imageViewer.setSource(new StreamResource(s, "SampleBill.jpg"));
									    imageViewer.setVisible(true);  
									    imageViewer.setHeight("500px");
									    showPopup(imageViewer);
									}		
								}

								else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith("pdf")  || bean.getFileName().endsWith("doc") || bean.getFileName().endsWith("docx")/*|| bean.getGalaxyFileName().endsWith(".PDF") || bean.getGalaxyFileName().endsWith("pdf")*/)
								{
									final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
									if(null != imageUrl)
									{
										
										 Embedded e = new Embedded();
									        e.setSizeFull();
									        e.setType(Embedded.TYPE_BROWSER);
								         StreamResource.StreamSource source = new StreamResource.StreamSource() {
				                                public InputStream getStream() {
				                                   
				                                	ByteArrayOutputStream baos = new ByteArrayOutputStream();
				                                	InputStream is = null;
				                                	URL u = null;
				                                	try {
				                                		u =  new URL(imageUrl);
				                                	  is = u.openStream();
				                                	  
				                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
				                                	  int n;

				                                	  while ( (n = is.read(byteChunk)) > 0 ) {
				                                	    baos.write(byteChunk, 0, n);
				                                	  }
				                                	}
				                                	catch (IOException e) {
				                                	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
				                                	  e.printStackTrace ();
				                                	  // Perform any other exception handling that's appropriate.
				                                	}
				                                	finally {
				                                	  if (is != null) {
				                                		  try
				                                		  {
				                                			  is.close();
				                                		  }
				                                		  catch(Exception e)
				                                		  {
				                                			  e.printStackTrace();
				                                		  }
				                                		  }
				                                	}
				                                	return new ByteArrayInputStream(baos.toByteArray());
				                                }
				                        };
				                        StreamResource r = new StreamResource(source, bean.getFileName());
				                        if(bean.getFileName().endsWith("docx") || bean.getFileName().endsWith("doc"))
				                        {
				                        	r.setMIMEType("application/msword");
				                        }
				                        else
				                        {
				                        	r.setMIMEType("application/pdf");
				                        }
				                        r.setFilename(bean.getFileName());
				                        r.setStreamSource(source);
				                        r.setFilename(bean.getFileName());
				                        
				                         opener = new BrowserWindowOpener(r);
										//opener.setWindowName(null);
				                       
				                     	opener.setFeatures("height=693,width=1400");
				                     //	opener.setWindowName("_blank");
				                     	  opener.extend(viewUploadedDocument);
										//
				                        
				                        //getUI().getPage().open(r, "_blank", false);

								}
									else
									{
										final String url1 = System.getProperty("jboss.server.data.dir") + "/"
												+"BILL.PDF";
								        Embedded emb = new Embedded();
								        emb.setSizeFull();
								        emb.setType(Embedded.TYPE_BROWSER);
										StreamResource.StreamSource str = new StreamResource.StreamSource() {
							
											public FileInputStream getStream() {
												try {
													File f = new File(url1);
													FileInputStream fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												}
											}
										};
										StreamResource resource = new StreamResource(str, "BILL.PDF");
										resource.setMIMEType("application/pdf");
										resource.setFilename("BILL.PDF");
										emb.setSource(resource);
										showPopup(emb);
									}
								}
								
								/**
								 * Code added for viewing html file.... Added by Vijay. 
								 * */
								else if(bean.getFileName().endsWith(".html") || bean.getFileName().endsWith(".HTML"))
								{
									final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
									if(null != imageUrl)
									{
										 opener = new BrowserWindowOpener(imageUrl);
										//opener.setWindowName(null);
										
											opener.setFeatures("height=693,width=1400");
											//opener.setWindowName("_blank");
											 opener.extend(viewUploadedDocument);
										
										// getUI().getPage().open(imageUrl, "_blank");
//										
									}
									else
									{
										final String url = System.getProperty("jboss.server.data.dir") + "/"
												+"BILL.PDF";
								        Embedded e = new Embedded();
								        e.setSizeFull();
								        e.setType(Embedded.TYPE_BROWSER);
										StreamResource.StreamSource s = new StreamResource.StreamSource() {
							
											public FileInputStream getStream() {
												try {
													File f = new File(url);
													FileInputStream fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												}
											}
										};
										StreamResource r = new StreamResource(s,"BILL.PDF");
								        r.setMIMEType("application/pdf");
								        r.setFilename("BILL.PDF");
								        e.setSource(r);
								        showPopup(e);
									}
								}

								
							else if(bean.getFileName().endsWith(".xlsx") /*|| bean.getGalaxyFileName().endsWith(".xlsx")*/)
							{
								final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
								if(null != imageUrl)
								{
									 opener = new BrowserWindowOpener(imageUrl);
									//opener.setWindowName(null);
									
										opener.setFeatures("height=693,width=1400");
									//	opener.setWindowName("_blank");
										 opener.extend(viewUploadedDocument);
									
									//getUI().getPage().open(imageUrl, "_blank");
								}
								else
								{
									final String url = System.getProperty("jboss.server.data.dir") + "/"
											+"Amount considered and Decision Table.xlsx";
							        Embedded e = new Embedded();
							        e.setSizeFull();
							        e.setType(Embedded.TYPE_BROWSER);
									StreamResource.StreamSource s = new StreamResource.StreamSource() {
						
										public FileInputStream getStream() {
											try {
												File f = new File(url);
												FileInputStream fis = new FileInputStream(f);
												return fis;
											} catch (Exception e) {
												e.printStackTrace();
												return null;
											}
										}
									};
									StreamResource r = new StreamResource(s, "Amount considered and Decision Table.xlsx");
									r.setMIMEType("application/x-msexcel");
									r.setFilename("Amount considered and Decision Table.xlsx");
							        e.setSource(r);
							        showPopup(e);
								}
							}
						}
							
							/*if (viewUploadedDocForm != null
									&& viewUploadedDocForm.getComponentCount() > 0) {
								viewUploadedDocForm.removeAllComponents();
							}
							viewUploadedDocForm.addComponent(viewUploadedDocument);*/
					}
				});
			
			 
			
		}
		
		if(null != viewClaimsDMSDocument)
		{
			if(null != viewClaimsDMSDocument)
			{
				viewClaimsDMSDocument.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					@SuppressWarnings("deprecation")
					public void buttonClick(ClickEvent event) {

						DMSDocumentDTO dmsDTO = new DMSDocumentDTO();
						dmsDTO.setIntimationNo(bean.getIntimationNo());
						dmsDTO.setClaimNo(bean.getClaimNo());
						dmsDTO.setDmsDocumentDetailsDTOList(bean.getDmsDocumentDTOList());
						popup = new com.vaadin.ui.Window();

						dmsDocumentDetailsViewPage.init(dmsDTO, popup);
						dmsDocumentDetailsViewPage.getContent();

						popup.setCaption("");
						popup.setWidth("75%");
						popup.setHeight("85%");
						//popup.setSizeFull();
						popup.setContent(dmsDocumentDetailsViewPage);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(true);
						popup.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						UI.getCurrent().addWindow(popup);
						
					}
				});
			}
		}
	}
	
	private void setTableValues()
	{
		
		if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
		{
			if(null != billReviewTable)
			{
				List<BillEntryDetailsDTO> billEntryList = this.bean.getBillEntryDetailList();
				if(null != billEntryList && !billEntryList.isEmpty())
				{
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryList) {
						//billEntryDetailsDTO.setBillEntryStatus(false);
						billReviewTable.addBeanToList(billEntryDetailsDTO);
					}
				}
			}
		}
		else
		{
			if(null != billEntryTable)
			{
				List<BillEntryDetailsDTO> billEntryList = this.bean.getBillEntryDetailList();
				if(null != billEntryList && !billEntryList.isEmpty())
				{
					for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryList) {
						//billEntryDetailsDTO.setBillEntryStatus(false);
						billEntryTable.addBeanToList(billEntryDetailsDTO);
					}
				}
			}
		}
	}
	
	public Boolean getZonalRemarks(){
		if(txtCorporateRemarks != null){
			if(txtCorporateRemarks.getValue() != null){
				return true;
			}
		}
		return false;
	}
	
	public Boolean getRemarks(){
		if(txtZonalRemarks != null){
			if(txtZonalRemarks.getValue() != null){
				return true;
			}
		}
		return false;
	}
	
	private void billEntryStatusListener()
	{
		optBillEntryStatus.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
			//public void componentEvent(Event event) {
				if(null != event.getProperty() && null != event.getProperty().getValue())
				{
					if(validateBillEntryTableValues())
					{
						Boolean finalStatus = false;
						Double uploadBillValue = bean.getBillValue();
						 
						Double totalBillValue  = 0d;
						Double totalNetAmt = 0d;
						List<BillEntryDetailsDTO> billEntryDetails = null;
						if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
								|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
						{
							billEntryDetails = billReviewTable.getValues();
							List<BillEntryDetailsDTO> deletedList = billReviewTable.getDeletedBillList();
							if(null != deletedList && !deletedList.isEmpty())
							{
								bean.setDeletedBillList(deletedList);
							}
							totalBillValue = billReviewTable.totalBillValue;
							totalNetAmt = billReviewTable.totalNetPayableAmt;
						}
						else
						{
							List<BillEntryDetailsDTO> deletedList = billEntryTable.getDeletedBillList();
							if(null != deletedList && !deletedList.isEmpty())
							{
								bean.setDeletedBillList(deletedList);
							}
							billEntryDetails = billEntryTable.getValues();
							totalBillValue = billEntryTable.totalBillValue;
						}
						
						
						 //List<BillEntryDetailsDTO> billEntryDetails = billEntryTable.getValues();				 
						 
						 for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetails) {
							 
							 billEntryDetailsDTO.setBillNo(bean.getBillNo());
							 billEntryDetailsDTO.setBillDate(bean.getBillDate());
							 billEntryDetailsDTO.setNoOfItems(bean.getNoOfItems());
							 billEntryDetailsDTO.setBillValue(bean.getBillValue());
							 
							
						}
						 bean.setBillEntryDetailList(billEntryDetails);
						 
						 bean.setNoOfItems(Long.parseLong(String.valueOf(billEntryDetails.size())));
						 
						
						 if("true" == event.getProperty().getValue().toString())
						{
							finalStatus = true;
							//bean.setBillValue(totalBillValue);
							bean.setStatus(finalStatus);
						}
						else
						{
							finalStatus = false;
							//bean.setBillValue(uploadBillValue);
							bean.setStatus(finalStatus);
						}
						 bean.setEmptyRowStatus(true);
		 
						 if(!(uploadBillValue.equals(totalBillValue)))
						 {
							 if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
										|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
							 {
								 buildSuccessLayout("The total value calculated varies from the bill value which is already entered </br> Press OK to update the total value  and cancel to stay back and review",totalBillValue,totalNetAmt);
							 }
							 else
							 {
								 buildSuccessLayout("The total value calculated varies from the bill value which is already entered </br> Press OK to update the total value  and cancel to stay back and review",totalBillValue,null);
							 }
								
						}
						 else
						 {
							 if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
										|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
							 {
								 
								 if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString)){
									 buildSuccessLayout("Bill Entry Verified in Zonal. </br> Click OK to proceed , or cancel to stay back",totalBillValue,totalNetAmt);
								 }
								 else  if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString)){
									 buildSuccessLayout("Bill Entry Verified in Medical. </br> Click OK to proceed , or cancel to stay back",totalBillValue,totalNetAmt);
								 }
								 else  if((SHAConstants.BILLING).equalsIgnoreCase(presenterString)){
									 bean.setBillDate(txtBillDate.getValue());
									 bean.setBillNo(txtBillNo.getValue());
									 buildSuccessLayout("Bill Entry Verified in Billing. </br> Click OK to proceed , or cancel to stay back",totalBillValue,totalNetAmt);
								 }
								 else  if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)){
									 bean.setBillDate(txtBillDate.getValue());
									 bean.setBillNo(txtBillNo.getValue());
									 buildSuccessLayout("Bill Entry Verified in Financial. </br> Click OK to proceed , or cancel to stay back",totalBillValue,totalNetAmt);
								 }
				
							 }
	
							 else
							 {
								 buildSuccessLayout("Bill Entry Values Entered Successfully. </br> Click OK to proceed , or cancel to stay back", totalBillValue,null);
							 }
						 }
					 }
				}

			}
		});
		
	}
	
	
	/**
	 * Added for enabling save option in bill review popup.
	 * 
	 * */
	
	private void billEntrySaveListener()
	{
		optSaveBillEntryDetails.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {	
			//public void componentEvent(Event event) {
				//if(null != event.getProperty() && null != event.getProperty().getValue())
				{
					//Boolean isSave = (Boolean) event.getProperty().getValue();	
					//if(isSave)
					{
						if(validateBillEntryTableValues())
						{
							Boolean finalStatus = false;
							Double uploadBillValue = bean.getBillValue();
							 
							Double totalBillValue  = 0d;
							Double totalNetAmt = 0d;
							List<BillEntryDetailsDTO> billEntryDetails = null;
							if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
									|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
							{
								billEntryDetails = billReviewTable.getValues();
								List<BillEntryDetailsDTO> deletedList = billReviewTable.getDeletedBillList();
								if(null != deletedList && !deletedList.isEmpty())
								{
									bean.setDeletedBillList(deletedList);
								}
								totalBillValue = billReviewTable.totalBillValue;
								totalNetAmt = billReviewTable.totalNetPayableAmt;
							}
							else
							{
								List<BillEntryDetailsDTO> deletedList = billEntryTable.getDeletedBillList();
								if(null != deletedList && !deletedList.isEmpty())
								{
									bean.setDeletedBillList(deletedList);
								}
								billEntryDetails = billEntryTable.getValues();
								totalBillValue = billEntryTable.totalBillValue;
							}
							
							
							 //List<BillEntryDetailsDTO> billEntryDetails = billEntryTable.getValues();				 
							 
							 for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetails) {
								 
								 billEntryDetailsDTO.setBillNo(bean.getBillNo());
								 billEntryDetailsDTO.setBillDate(bean.getBillDate());
								 billEntryDetailsDTO.setNoOfItems(bean.getNoOfItems());
								 billEntryDetailsDTO.setBillValue(bean.getBillValue());
								 
								
							}
							 bean.setBillEntryDetailList(billEntryDetails);
							 
							 bean.setNoOfItems(Long.parseLong(String.valueOf(billEntryDetails.size())));
							 
							
							/* if("true" == event.getProperty().getValue().toString())
							{
								finalStatus = true;
								//bean.setBillValue(totalBillValue);
								bean.setStatus(finalStatus);
							}
							else
							{
								finalStatus = false;
								//bean.setBillValue(uploadBillValue);
								bean.setStatus(finalStatus);
							}*/
							 bean.setEmptyRowStatus(true);
			 
							 /*if(!(uploadBillValue.equals(totalBillValue)))
							 {
								 if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
											|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
								 {
									 buildSuccessLayout("The total value calculated varies from the bill value which is already entered </br> Press OK to update the total value  and cancel to stay back and review",totalBillValue,totalNetAmt);
								 }
								 else
								 {
									 buildSuccessLayout("The total value calculated varies from the bill value which is already entered </br> Press OK to update the total value  and cancel to stay back and review",totalBillValue,null);
								 }
									
							}
							 else*/
							 {
								 if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
											|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))) 
								 {
									 
									 if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString)){
										 buildSuccessLayoutForSave("Bill Entry Values Saved Successfully",totalBillValue,totalNetAmt);
									 }
									 else  if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString)){
										 buildSuccessLayoutForSave("Bill Entry Values Saved Successfully",totalBillValue,totalNetAmt);
									 }
									 else  if((SHAConstants.BILLING).equalsIgnoreCase(presenterString)){
										
										 buildSuccessLayoutForSave("Bill Entry Values Saved Successfully",totalBillValue,totalNetAmt);
									 }
									 else  if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)){
										 
										 buildSuccessLayoutForSave("Bill Entry Values Saved Successfully",totalBillValue,totalNetAmt);
									 }
					
								 }
		
								 else
								 {
									 buildSuccessLayoutForSave("Bill Entry Values Saved Successfully", totalBillValue,null);
								 }
							 }
						 }
				}
			}

		}
	});
		
}
	
	
	private void buildSuccessLayout(String message,final Double totalBillValue,final Double totalNetAmt)
	{
		  	Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			Button cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);
			 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton,cancelButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
			//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
			//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final Window dialog = new Window();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
				dialog.setPositionX(450);
				dialog.setPositionY(500);
				//dialog.setDraggable(true);
				
				
			}
			getUI().getCurrent().addWindow(dialog);
		
//			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {					
					dialog.close();
					popup.close();
					bean.setBillValue(totalBillValue);
					if(null != totalNetAmt)
					{
						bean.setNetAmount(totalNetAmt);
					}
					if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					else if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthBillingReviewPagePresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthFinancialReviewPagePresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					else if(SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)){
						fireViewEvent(DetailsPresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					else if(SHAConstants.PA_BILL_ENTRY.equalsIgnoreCase(presenterString)){
						fireViewEvent(PABillEntryDetailsPresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					
					else {
						fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_COMPLETION_STATUS, bean);
					}
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
			cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					optBillEntryStatus.setValue(null);
					//popup.close();
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
	}
	
	
	
	private void buildSuccessLayoutForSave(String message,final Double totalBillValue,final Double totalNetAmt)
	{
		  	Label successLabel = new Label("<b style = 'color: green;'> "+ message, ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			/*Button cancelButton = new Button("Cancel");
			cancelButton.setStyleName(ValoTheme.BUTTON_DANGER);*/
			 HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
			horizontalLayout.setMargin(true);
			horizontalLayout.setSpacing(true);
			horizontalLayout.setComponentAlignment(homeButton, Alignment.MIDDLE_RIGHT);
			//horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
			//horizontalLayout.setComponentAlignment(homeButton, Alignment.BOTTOM_RIGHT);
			//horizontalLayout.setComponentAlignment(cancelButton, Alignment.BOTTOM_RIGHT);
			
			VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
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
			if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
				dialog.setPositionX(450);
				dialog.setPositionY(500);
				//dialog.setDraggable(true);
				
				
			}
			getUI().getCurrent().addWindow(dialog);
//			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {					
					dialog.close();
					//popup.close();
					bean.setBillValue(totalBillValue);
					if(null != totalNetAmt)
					{
						bean.setNetAmount(totalNetAmt);
					}
					if((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.ZONAL_SAVE_BILL_ENTRY_VALUES, bean);
					}
					else if((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.MA_SAVE_BILL_ENTRY_VALUES, bean);
					}
					else if((SHAConstants.BILLING).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_SAVE_BILL_ENTRY_VALUES, bean);
					}
					else if((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAHealthFinancialReviewPagePresenter.FA_SAVE_BILL_ENTRY_VALUES, bean);
					}
					else if(SHAConstants.ADD_ADDITIONAL_DOCS.equalsIgnoreCase(presenterString)){
						fireViewEvent(DetailsPresenter.ADD_ADDITIONAL_SAVE_BILL_ENTRY_VALUES, bean);
					}
					else if((SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString))
					{
						fireViewEvent(PAClaimRequestDataExtractionPagePresenter.PA_SAVE_BILL_DETAILS_VALUES, bean);
					}
					else {
						fireViewEvent(BillEntryDetailsPresenter.SAVE_BILL_ENTRY_VALUES, bean);
					}
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});
		/*	cancelButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
				//	optSaveBillEntryDetails.setValue(null);
					//popup.close();
					//fireViewEvent(MenuItemBean.SHOW_ACKNOWLEDGEMENT_DOCUMENT_RECEIVER, null);
					
				}
			});*/
	}
	
	private Boolean validateBillEntryTableValues()
	{
		String eMsg = "";
		Boolean hasError = false;
		Double amt = 0d;
		classificationValidationMap = new HashMap<String, String>();
		if(null != this.billEntryTable)
		{
			List<BillEntryDetailsDTO> billEntryDetailsList = null;
			if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
					|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
			{
				billEntryDetailsList = this.billReviewTable.getValues();
			}
			else
			{
				billEntryDetailsList = this.billEntryTable.getValues();
			}
			
			
			if(null != billEntryDetailsList && !billEntryDetailsList.isEmpty())
			{
				for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetailsList) {
					
					if(!(null != billEntryDetailsDTO.getClassification() && !("").equals(billEntryDetailsDTO.getClassification().getValue())))
					{
						hasError = true;
						eMsg += "Please choose bill classification for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
						
					}
					else if(null !=  billEntryDetailsDTO.getClassification())
					{
						String strClassification = billEntryDetailsDTO.getClassification().getValue();
						if(SHAConstants.HOSPITALIZATION.equalsIgnoreCase(strClassification))
						{
							classificationValidationMap.put(SHAConstants.HOSPITALIZATION, strClassification);
						}
						if(SHAConstants.PRE_HOSPITALIZATION.equalsIgnoreCase(strClassification))
						{
							classificationValidationMap.put(SHAConstants.PRE_HOSPITALIZATION, strClassification);
						}
						if(SHAConstants.POST_HOSPITALIZATION.equalsIgnoreCase(strClassification))
						{
							classificationValidationMap.put(SHAConstants.POST_HOSPITALIZATION, strClassification);
						}
					}
					if(null == billEntryDetailsDTO.getItemName() || ("").equals(billEntryDetailsDTO.getItemName()))
					{
						hasError = true;
						eMsg += "Please enter item name for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
					}
					
					if(!(null != billEntryDetailsDTO.getCategory() && !("").equals(billEntryDetailsDTO.getCategory().getValue())))
					{
						hasError = true;
						eMsg += "Please choose bill category for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
					}
					
					if(null != billEntryDetailsDTO.getReasonableDeduction() && billEntryDetailsDTO.getReasonableDeduction() > amt)
					{
						hasError = true;
						eMsg += "Reasanable Deductable Amount should not be greater than Zero in Item"+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						
						break;
					}
					
					if((null != billEntryDetailsDTO.getCategory() && ((SHAConstants.ROOM_RENT).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) || (SHAConstants.ICU_ROOM_RENT).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
							&& (null == billEntryDetailsDTO.getNoOfDays() || null == billEntryDetailsDTO.getPerDayAmt()) ))
					{
						hasError = true;
						eMsg += "Please enter no of days and per day amount , if category is Room Rent or ICU Rooms for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
					}
					
					if((null != billEntryDetailsDTO.getCategory() && ((SHAConstants.ICU_NURSING_CHARGES).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) || (SHAConstants.NURSING_CHARGES).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
							&& (null == billEntryDetailsDTO.getNoOfDays() || null == billEntryDetailsDTO.getPerDayAmt()) ))
					{
						hasError = true;
						eMsg += "Please enter no of days and per day amount , if category is ICU Nursing Charges  or Nursing Charges for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
					}
					
					/*if((null == billEntryDetailsDTO.getItemValue() && ("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && ("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
							&& ("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) )
					{
						hasError = true;
						eMsg += "Please enter item value  </br>";
						break;
					}*/
					
					if(null != presenterString && !("").equalsIgnoreCase(presenterString) && (SHAConstants.BILL_ENTRY).equalsIgnoreCase(presenterString))
					{
						if((null == billEntryDetailsDTO.getItemValue()))/*&& ("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && ("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
							&& ("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) )*/
					{
						hasError = true;
						eMsg += "Please enter item value  for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
						break;
					}
					}
					
					else if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
							|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
					{
						
						if((null != billEntryDetailsDTO.getCategory() && ((SHAConstants.ROOM_RENT).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) || (SHAConstants.ICU_ROOM_RENT).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
								&& (null == billEntryDetailsDTO.getNoOfDaysAllowed() || null == billEntryDetailsDTO.getPerDayAmtProductBased()) ))
						{
							hasError = true;
							eMsg += "Please enter no of days allowed and per day amount , if category is Room Rent or ICU Rooms for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
							break;
						}
						
						if(null == billEntryDetailsDTO.getItemValue() && !((SHAConstants.DEDUCTIBLES).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) || (SHAConstants.DEDUCTIBLES_80_PERCENT).equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())))
				{
					hasError = true;
					eMsg += "Please enter item value  </br>";
					break;
				}
						/*if((null == billEntryDetailsDTO.getAmountAllowableAmount()  && !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
								&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
						{
							hasError = true;
							eMsg += "Please enter entitled amount</br>";
							break;
						}*/
						/*if(null == billEntryDetailsDTO.getNonPayable() && !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
							&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
						{
							hasError = true;
							eMsg += "Please enter Non payable amount</br>";
							break;
						}
						if(null == billEntryDetailsDTO.getReasonableDeduction()&& !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
								&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
						{
							hasError = true;
							eMsg += "Please enter reasonable deduction</br>";
							break;
						}*/
						if ( (null != billEntryDetailsDTO.getReasonableDeduction() || null != billEntryDetailsDTO.getNonPayable()) && ((null == billEntryDetailsDTO.getDeductibleOrNonPayableReason() || ("").equalsIgnoreCase(billEntryDetailsDTO.getDeductibleOrNonPayableReason()))
								&& (("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) || ("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
								|| ("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))))
						{
							hasError = true;
							eMsg += "Please enter deductible or non payable reason</br>";
							break;
						}
						/*if(null == billEntryDetailsDTO.getMedicalRemarks()&& !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
								&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
						{
							hasError = true;
							eMsg += "Please enter medical remarks</br>";
							break;
						}*/
						
						if(null != billEntryDetailsDTO.getItemValue() && null != billEntryDetailsDTO.getTotalDisallowances())
						{
							if(billEntryDetailsDTO.getTotalDisallowances() > billEntryDetailsDTO.getItemValue())
							{
								
								hasError = true;
								
								eMsg += "In Item no. "+billEntryDetailsDTO.getItemNo()+ " ,Total Disallowances should not exceed claimed amount value</br>";
								break;
							}
						}
						
						if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
						{
							
							if(!(null != billEntryDetailsDTO.getIrdaLevel1() && !("").equalsIgnoreCase(billEntryDetailsDTO.getIrdaLevel1().getValue())))
							{
								hasError = true;
								eMsg += "Please select IRDA level1 for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
								break;
							}
							if(!(null != billEntryDetailsDTO.getIrdaLevel2() && !("").equalsIgnoreCase(billEntryDetailsDTO.getIrdaLevel2().getValue())))
							{
								hasError = true;
								eMsg += "Please select IRDA level2 for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
								break;
							}
							
							if(null == billEntryDetailsDTO.getDeductibleOrNonPayableReason() && !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
									&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
							{
								/**
								 * Below code is added as a part of production issues.
								 * 
								 * */
								Double nonPayableAmt = 0d;
								Double nonPayablePdtBasedAmt = 0d;
								Double reasonableDeductionAmt = 0d;
								Double totalAmt = 0d;
								if(null != billEntryDetailsDTO.getNonPayable())
								{
									//nonPayableAmt = 0d;
									nonPayableAmt += billEntryDetailsDTO.getNonPayable();
								}
								if(null != billEntryDetailsDTO.getNonPayableProductBased())
								{
									//nonPayablePdtBasedAmt = 0d;
									nonPayablePdtBasedAmt += billEntryDetailsDTO.getNonPayableProductBased();
								}
								if(null != billEntryDetailsDTO.getReasonableDeduction())
								{
									//reasonableDeductionAmt = 0d;
									reasonableDeductionAmt += billEntryDetailsDTO.getReasonableDeduction();
								}
								if( null != nonPayableAmt && null != nonPayablePdtBasedAmt && null != reasonableDeductionAmt)
								{
									totalAmt = nonPayableAmt + nonPayablePdtBasedAmt + reasonableDeductionAmt;
								}
								if(null == billEntryDetailsDTO.getDeductibleOrNonPayableReason() && (null != totalAmt &&  totalAmt >0))
								{
									hasError = true;
									eMsg += "Please enter deductible or non payable reason</br>";
									break;
								}
								
								
								
							}
							

							
							
//							if(null == billEntryDetailsDTO.getMedicalRemarks()&& !("Deductibles").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()) && !("Deductibles (80%)").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim())
//									&& !("Hospital Discount").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue().trim()))
//							{
//								hasError = true;
//								eMsg += "Please enter medical remarks</br>";
//								break;
//							}
							
							/*if(!(null != billEntryDetailsDTO.getIrdaLevel3() && !("").equalsIgnoreCase(billEntryDetailsDTO.getIrdaLevel3().getValue())))
							{
								hasError = true;
								eMsg += "Please select IRDA level3 for item no "+" " + billEntryDetailsDTO.getItemNo()+"</br>";
								break;
							}*/
						}
						
					}
					
					
					
					
					
				}
			}
			else
			{
				hasError = true;
				eMsg += "Please enter bill details</br>";
			}
			
			/*if(null != this.referenceDataMap)
			{
				BeanItemContainer<SelectValue> classificationValueContainer = (BeanItemContainer<SelectValue>)referenceDataMap.get(SHAConstants.BILL_CLASSIFICATION);
				 for(int i = 0 ; i<classificationValueContainer.size() ; i++)
				 	{
						if(null != classificationValidationMap)
						{
							if((SHAConstants.HOSPITALIZATION).equalsIgnoreCase(classificationValueContainer.getIdByIndex(i).getValue()))
									{
										if(null == classificationValidationMap.get(SHAConstants.HOSPITALIZATION))
										{
											hasError = true;
											eMsg += "Please enter hospitalization bill details</br>";
											break;
										}
									}
							else if((SHAConstants.PRE_HOSPITALIZATION).equalsIgnoreCase(classificationValueContainer.getIdByIndex(i).getValue()))
							{
								if(null == classificationValidationMap.get(SHAConstants.PRE_HOSPITALIZATION))
								{
									hasError = true;
									eMsg += "Please enter pre hospitalization bill details</br>";
									break;
								}
							}
							else if((SHAConstants.POST_HOSPITALIZATION).equalsIgnoreCase(classificationValueContainer.getIdByIndex(i).getValue()))
							{
								if(null == classificationValidationMap.get(SHAConstants.POST_HOSPITALIZATION))
								{
									hasError = true;
									eMsg += "Please enter post hospitalization bill details</br>";
									break;
								}
							}
						}
					}
			}*/
			
			if(null != presenterString && (SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString)){
				if(txtZonalRemarks != null && ("").equalsIgnoreCase(txtZonalRemarks.getValue()) || null == txtZonalRemarks.getValue()){
					hasError = true;
					eMsg += "Please Enter Zonal Remarks </br>";
				}
			}
			
			if(null != presenterString && ((SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString))){
				if(txtCorporateRemarks != null && ("").equalsIgnoreCase(txtCorporateRemarks.getValue()) || null == txtCorporateRemarks.getValue()){
					hasError = true;
					eMsg += "Please Enter Corporate Remarks </br>";
				}
			}
			
			if(null != presenterString && (SHAConstants.BILLING).equalsIgnoreCase(presenterString)){
				if(billingRemarks != null && ("").equalsIgnoreCase(billingRemarks.getValue()) || null == billingRemarks.getValue()){
					hasError = true;
					eMsg += "Please Enter Billing Remarks </br>";
				}
			}
			
			if(hasError)
			{
				Label label = new Label(eMsg, ContentMode.HTML);
				label.setStyleName("errMessage");
				VerticalLayout layout = new VerticalLayout();
				layout.setMargin(true);
				layout.addComponent(label);
				Window dialog = new Window();
				dialog.setCaption("Errors");
				dialog.setClosable(true);
				dialog.setContent(layout);
				dialog.setResizable(true);
				dialog.setModal(true);
				if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
					//dialog.setDraggable(true);
				}
//				dialog.show(getUI().getCurrent(), null, true);
				getUI().getCurrent().addWindow(dialog);
				optBillEntryStatus.setValue(null);
				optBillEntryStatus.setNullSelectionAllowed(true);
				//optBillEntryStatus.setValue("");
				//optBillEntryStatus.setNullSelectionAllowed(true);
				return !hasError;
			}
			else 
			{
				try {
					this.binder.commit();
				} catch (CommitException e) {
					e.printStackTrace();
					return false;
				}
				
				return true;
			}	
		}
		if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.BILLING).equalsIgnoreCase(presenterString))){
			if(billingRemarks != null && ("").equalsIgnoreCase(billingRemarks.getValue())){
				hasError = true;
				eMsg += "Please Enter Billing Remarks</br>";
			}
		}
		return !hasError;
	}
	
	protected Collection<Boolean> getReadioButtonOptions() {
		
		Collection<Boolean> coordinatorValues = new ArrayList<Boolean>(2);
		coordinatorValues.add(true);
		coordinatorValues.add(false);
		return coordinatorValues;
	}
	
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.ZONAL_REVIEW).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)|| (SHAConstants.CLAIM_REQUEST).equalsIgnoreCase(presenterString) || (SHAConstants.PA_PROCESS_CLAIM_REQUEST).equalsIgnoreCase(presenterString)))
		{
			billReviewTable.setUpCategoryValues(categoryValues);
		}
		else
		{
			billEntryTable.setUpCategoryValues(categoryValues);
		}
}

	public String getHospitalizationValues() {
		Integer hospitalizationAmount = 0;
		if(billEntryTable != null) {
			 List<BillEntryDetailsDTO> billEntryDetails = billEntryTable.getValues();
			 for (BillEntryDetailsDTO billEntryDetailsDTO : billEntryDetails) {
				if(billEntryDetailsDTO.getClassification() != null && billEntryDetailsDTO.getClassification().getId().equals(8l)) {
					hospitalizationAmount += billEntryDetailsDTO.getItemValue() != null ? billEntryDetailsDTO.getItemValue().intValue() : 0;
				}
			}
		}
		return String.valueOf(hospitalizationAmount);
	}

	public void setIrdaLevel2Values(
			BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue value) {
	/*	if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)))*/
		{
			billReviewTable.setIrdaLevel2Values(selectValueContainer,cmb,value);
		}
		
	}

	public void setIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		/*if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.BILLING).equalsIgnoreCase(presenterString) 
				|| (SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString)))*/
		{
			billReviewTable.setIrdaLevel3Values(selectValueContainer,cmb,value);
		}
		
	}
	

	private void showPopup(Embedded e)
	{

		/**
		 * On click, the rrc histiory needs to displayed.
		 * */
		popup = new com.vaadin.ui.Window();
		
		
		
		/*viewClaimWiseRRCHistoryPage.init(bean,popup);
		viewClaimWiseRRCHistoryPage.initPresenter(SHAConstants.VIEW_RRC_REQUEST);
		viewClaimWiseRRCHistoryPage.getContent();*/
		
		popup.setCaption("View Uploaded File");
		popup.setWidth("75%");
		popup.setHeight("85%");
		popup.setContent(e);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	
	}
	
	public  void handleEnterForBillDate(DateField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcutForMedical", ShortcutAction.KeyCode.F7, null) {
	      private static final long serialVersionUID = -2267576464623389045L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	   // handleShortcutForMedical(searchField, enterShortCut);
	    handleShortcutForBillDate(searchField, getShortCutListenerForBillDate(searchField));
	    
	  }

	
	public  void handleShortcutForBillDate(final DateField textField, final ShortcutListener shortcutListener) {
		//textField.addFocusListener(F);
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				//textField.addShortcutListener(getShortCutListenerForMedicalReason(textField));
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		
	   textField.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {/*
			Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}
			
		*/
		textField.removeShortcutListener(shortcutListener);	
		}
	});
	  }

	private ShortcutListener getShortCutListenerForBillDate(final DateField dateFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcutForMedical",KeyCodes.KEY_F7,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) dateFld.getData();
				// HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
				// TextField txtItemValue = (TextField) hashMap.get("medicalRemarks");
				
				 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				DateField dateFeild = new DateField();				
				//txtArea.setValue(billEntryDetailsDTO.getMedicalRemarks());
				//txtArea.setValue(txtFld.getValue());
				//dateFeild.addValueChangeListener(listener);
				dateFeild.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						DateField txt = (DateField)event.getProperty();
						dateFld.setValue(txt.getValue());
						//dateFld.setDescription(txt.getValue());
						// TODO Auto-generated method stub
						
					}
				});
				
				//billEntryDetailsDTO.setMedicalRemarks(txtArea.getValue());
			//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				//dateFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(dateFeild);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				
				
				
				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(450);
					dialog.setPositionY(500);
				}
				getUI().getCurrent().addWindow(dialog);
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});	
			}
		};
		
		return listener;
	}
	
}
