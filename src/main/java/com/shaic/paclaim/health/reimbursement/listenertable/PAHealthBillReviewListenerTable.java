/**
 * 
 */
package com.shaic.paclaim.health.reimbursement.listenertable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.components.GComboBox;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.claim.DMSDocumentViewListenerTable;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDetailsPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.paclaim.health.reimbursement.billing.wizard.pages.billreview.PAHealthBillingReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.financial.pages.billreview.PAHealthFinancialReviewPagePresenter;
import com.shaic.paclaim.health.reimbursement.medicalapproval.pages.medicaldecision.PAHealthClaimRequestMedicalDecisionPagePresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class PAHealthBillReviewListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<BillEntryDetailsDTO> container = new BeanItemContainer<BillEntryDetailsDTO>(BillEntryDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private BeanItemContainer<SelectValue> category;
	
	private List<String> errorMessages;
	
	private static Validator validator;
	
	private BeanItemContainer<SelectValue> categoryValues;
	
	private BeanItemContainer<SelectValue> irdaLevel2Values;
	
	private BeanItemContainer<SelectValue> irdaLevel3Values;
	
	private String billNo;
	
	private Date billDate;
	
	private Long noOfItems;
	
	private Double billValue;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	public Double totalNetPayableAmt;
	
	private int iItemValue = 0;
	
	private String presenterString = "";
	
	private Double pdtPerDayAmt ;
	
	private Double pdtICURentAmt;
	
	private Double pdtICCURentAmt;
	
	private Double pdtAmbulanceAmt;
	
	private SelectValue claimType;
	//private UploadDocumentDTO bean;
	private List<BillEntryDetailsDTO> deletedList;
	
	private List ambulanceList;
	private VerticalLayout vLayout ;
	private Button btnDuplicate;
	private String fileName ;
	
	private Button viewUploadedDocument;

	private Window popup;
	
	private UploadDocumentDTO bean;
	final Embedded imageViewer = new Embedded("Uploaded Image");
	private VerticalLayout layout;
	private HorizontalLayout hlayout ;
	
	private Button viewClaimsDMSDocument;

	@Inject
	private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	
	@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	
	private BrowserWindowOpener claimsDMSWindow;
	
	private Button btnDeleteAll;

	private Button btnDeleteSelectedItem;
	
	private List<BillEntryDetailsDTO> deleteSelectedItemList ;
	public TextField dummyFieldForPenalInterest;
	
	private Long productKey;
	
	private String subCoverValue;
	
	private Boolean domicillaryFlag;
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(UploadDocumentDTO bean) {
	//	this.bean = bean;
		populateBillDetails(bean);
		this.bean = bean;
		vLayout = new VerticalLayout();
		ambulanceList = new ArrayList();
		deletedList = new ArrayList<BillEntryDetailsDTO>();
		deleteSelectedItemList =  new ArrayList<BillEntryDetailsDTO>();
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		this.errorMessages = new ArrayList<String>();
		
		this.viewUploadedDocument = new Button("View Document");
		viewUploadedDocument.setStyleName(ValoTheme.BUTTON_LINK);
		
		this.viewClaimsDMSDocument = new Button("View All Documents");
		viewClaimsDMSDocument.setStyleName(ValoTheme.BUTTON_LINK);
		
	
		
		/*claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
		claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
		claimsDMSWindow.extend(viewClaimsDMSDocument);*/
		
		this.btnDeleteSelectedItem = new Button("Delete Selected Rows");
		btnDeleteSelectedItem.setIcon(FontAwesome.TRASH_O);
		btnDeleteSelectedItem.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		
		this.btnDeleteAll = new Button("Delete All Rows");
		btnDeleteAll.setIcon(FontAwesome.TRASH_O);
		btnDeleteAll.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		//HorizontalLayout deleteAllBtnLayout = new HorizontalLayout(btnDeleteAll);
		HorizontalLayout deleteAllBtnLayout = new HorizontalLayout(btnDeleteAll,btnDeleteSelectedItem);
		//deleteAllBtnLayout.setComponentAlignment(btnDeleteAll, Alignment.MIDDLE_RIGHT);
		
		
		
		layout = new VerticalLayout();
		layout.setMargin(true);
		/*layout.addComponent(viewUploadedDocument);
		layout.setComponentAlignment(viewUploadedDocument, Alignment.MIDDLE_RIGHT);*/
		 hlayout = buildButtonLayout();
		 
		 HorizontalLayout hLayout1 = new HorizontalLayout(viewUploadedDocument,viewClaimsDMSDocument);
			
			
		hLayout1.setComponentAlignment(viewUploadedDocument, Alignment.MIDDLE_RIGHT);
		layout.addComponent(hLayout1);
		layout.setComponentAlignment(hLayout1, Alignment.MIDDLE_RIGHT);
		 
		 
		layout.addComponent(hlayout);
		
		//Panel pane = new Panel();
		
		initTable();
		table.setWidth("100%");
		//table.setSizeFull();
		table.setHeight("500px");
		table.setPageLength(table.getItemIds().size());
		
		
		
		layout.addComponent(deleteAllBtnLayout);
		layout.setComponentAlignment(deleteAllBtnLayout, Alignment.MIDDLE_RIGHT);
		
		layout.addComponent(table);
	
		//layout.setHeight("100px");
		layout.setSizeFull();

		setCompositionRoot(layout);
	}
	
	public HorizontalLayout buildButtonLayout()
	{
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		btnDuplicate = new Button("Add Duplicate Row");
		btnDuplicate.setStyleName("link");

	
		//btnDeleteAll.setStyleName(ValoTheme.BUTTON_LINK);
	
 
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnDuplicate,btnAdd);
		btnLayout.setWidth("200%");
		btnLayout.setSpacing(false);
		/*btnLayout.setComponentAlignment(btnFormLayout,Alignment.MIDDLE_RIGHT);
		btnLayout.setComponentAlignment(btnFormLayout1,Alignment.MIDDLE_RIGHT);*/
		btnLayout.setComponentAlignment(btnDuplicate, Alignment.MIDDLE_RIGHT);
		//btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		
		addListener(); 

		
		return btnLayout;

	}
	
	public void setTableList(final List<BillEntryDetailsDTO> list) {
		table.removeAllItems();
		for (final BillEntryDetailsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());	
		//table.setVisibleColumns(new Object[] { "billNo","billDate", "noOfItems", "billValue", "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		if(null != presenterString && !("").equalsIgnoreCase(presenterString) && ((SHAConstants.FINANCIAL).equalsIgnoreCase(presenterString) || (SHAConstants.BILLING).equalsIgnoreCase(presenterString)))
		{
			table.setVisibleColumns(new Object[] { "itemNo", "itemName", "classification", "category","irdaLevel1", "irdaLevel2" , "irdaLevel3" ,"noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason" , "medicalRemarks"});
			table.setColumnHeader("irdaLevel1", "IRDA Level 1");
			table.setColumnHeader("irdaLevel2", "IRDA Level 2");
			table.setColumnHeader("irdaLevel3", "IRDA Level 3");
		}
		else
		{
			table.setVisibleColumns(new Object[] { "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue" , "noOfDaysAllowed" , "perDayAmtProductBased" , "amountAllowableAmount" , "nonPayableProductBased" , "nonPayable" , "reasonableDeduction" , "totalDisallowances" , "netPayableAmount" , "deductibleOrNonPayableReason" , "medicalRemarks"});
		}
		table.setColumnHeader("itemNo", "Item No");
		table.setColumnHeader("itemName", "Item Name");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("category", "Category");

		
		
		table.setColumnHeader("noOfDays", "No of </br> Days Claimed");
		table.setColumnHeader("perDayAmt", "Per Day </br> Amt");
		table.setColumnHeader("itemValue", "Claimed Amount </br> (C) = A*B");
		table.setColumnHeader("noOfDaysAllowed", "No of </br>Days Allowed");
		table.setColumnHeader("perDayAmtProductBased", "Per Day </br> Amt </br> (Product Based)");
		table.setColumnHeader("amountAllowableAmount", "Amount");
		table.setColumnHeader("nonPayableProductBased", "Non payable </br> (Product Based)");
		table.setColumnHeader("nonPayable","Non </br> Payable");
		table.setColumnHeader("reasonableDeduction", "Reasonable </br> Deduction");
		table.setColumnHeader("totalDisallowances", "Total </br> Disallowances");
		table.setColumnHeader("netPayableAmount", "Net Payable </br> Amt");
		table.setColumnHeader("deductibleOrNonPayableReason", "Deductible / </br> Non Payables </br> Reason");
		table.setColumnHeader("medicalRemarks", "Medical </br> Remarks");
				
		table.setEditable(true);
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
			//	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				deleteButton.setIcon(FontAwesome.TRASH_O);
				deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				deleteButton.setWidth("-1px");
				deleteButton.setHeight("-10px");
				deleteButton.setData(itemId);
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;
					public void buttonClick(ClickEvent event) {
						Object currentItemId = event.getButton().getData();
						BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) currentItemId;
						if(null != billEntryDetailsDTO.getKey())
						{
							//billEntryDetailsDTO.setDeletedFlag("Y");
							deletedList.add(billEntryDetailsDTO);
						}
						table.removeItem(currentItemId);
						calculateTotal();
						calculateAllowableAmountTotal();
						summationOfNetPayableAmount();
						summationOfTotalDisallowances();
						
						
					}
				});

				return deleteButton;
			}
		});
		
		table.removeGeneratedColumn("Select");
		table.addGeneratedColumn("Select", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				  BillEntryDetailsDTO tableDTO = (BillEntryDetailsDTO) itemId;
				  CheckBox chkBox = new CheckBox("");
					chkBox.setData(tableDTO);
					addListener(chkBox);
					//compList.add(chkBox);
					//addListener(chkBox);
				return chkBox;
			}
		});
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		table.setFooterVisible(true);
		table.setColumnFooter("category", String.valueOf("Total"));
		

	}
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					Boolean value = (Boolean) event.getProperty().getValue();
					BillEntryDetailsDTO tableDTO = (BillEntryDetailsDTO)chkBox.getData();
					/**
					 * Added for issue#192.
					 * */
					if(null != value && value && null != deleteSelectedItemList)
					{
						deleteSelectedItemList.add(tableDTO);
					}
					else if(null != value && !value && null != deleteSelectedItemList && deleteSelectedItemList.contains(tableDTO))
					{
						deleteSelectedItemList.remove(tableDTO);
					}
				}
			}
		});
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				BeanItem<BillEntryDetailsDTO> addItem = container.addItem(new BillEntryDetailsDTO());
				List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					BillEntryDetailsDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					if(combos != null){
						final TextField txtFld = (TextField) combos.get("itemName");
						txtFld.focus();
					}
					
				}
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
		});
		
		btnDuplicate.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 58520894917977414L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				List<BillEntryDetailsDTO> items = (List<BillEntryDetailsDTO>) table.getItemIds();
				System.out.println("----the items0000"+items);
				if(null != items && !items.isEmpty())
				{
					int iSize = items.size();
					BillEntryDetailsDTO duplicateDTO = items.get(iSize-1);
					container.addItem(populatePreviousRowValues(duplicateDTO));
				}
				
				List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					BillEntryDetailsDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					if(combos != null){
					final TextField txtFld = (TextField) combos.get("itemName");
					txtFld.focus();
					}
					
				}
				//if(container.size()==0){
				
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				manageListeners();
			}
			
			
		});
		
		if(null != btnDeleteAll)
		{
			btnDeleteAll.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 5852089491794014554L;
	
				@Override
				public void buttonClick(ClickEvent event) 
				{
					if(null != table)
					{
						List<BillEntryDetailsDTO> billEntryList = (List<BillEntryDetailsDTO>)table.getItemIds();
						if(null != billEntryList && !billEntryList.isEmpty())
						{
							if(billEntryList.size()>=0)
								{
									showDeleteRowsPopup("All the bills entered in the table will be deleted. Press OK to proceed.", true,billEntryList,false);
								}
						}
						else
						{
							showDeleteRowsPopup("No rows available for deletion.", false,null,false);
						}
					}
					else
					{
						showDeleteRowsPopup("No rows available for deletion.", false,null,false);
					}
				}
			});
		}
		

		if(null != btnDeleteSelectedItem)
		{
			btnDeleteSelectedItem.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 5852089491794014554L;
	
				@Override
				public void buttonClick(ClickEvent event) 
				{
					if(null != deleteSelectedItemList && !deleteSelectedItemList.isEmpty())
					{
						showDeleteRowsPopup("Selected bill will be deleted. Press OK to proceed.", true,deleteSelectedItemList,true);
					}
					else
					{
						showDeleteRowsPopup("No rows available for deletion.", false,null,false);
					}
				}
			});
		}
		
		
		if(null != viewUploadedDocument)
		{

			
			viewUploadedDocument.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 6100598273628582002L;

					
						
						@SuppressWarnings("deprecation")
						public void buttonClick(ClickEvent event) {
							//BrowserWindowOpener opener = null;
						//	Window window = new Window();
							
							if(null != bean.getFileName())
							{
								if(bean.getFileName().endsWith(".JPG") || bean.getFileName().endsWith(".jpg") || bean.getFileName().endsWith(".jpeg"))
								{
									 String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
									
									if(imageUrl != null) {	
										
									//	getUI().getPage().open(imageUrl, "_blank",true);
										getUI().getPage().open(imageUrl, "_blank",1550,650,BorderStyle.NONE);

										
										//viewUploadDocWindow = new BrowserWindowOpener(imageUrl);
										
										//opener.setFeatures("height=693,width=1400");
										//viewUploadDocWindow.setFeatures("height=693,width=1200,resizable");
										//opener.setWindowName("_blank");
										//viewUploadDocWindow.extend(viewUploadedDocument);
										//getUI().getPage().open(imageUrl, "_blank",900,5000,BorderStyle.DEFAULT);
									   
										
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
				                        
				                        //viewUploadDocWindow = new BrowserWindowOpener(r);
										//opener.setWindowName(null);
				                       
				                       // viewUploadDocWindow.setFeatures("height=693,width=1400,resizable");
											//opener.setFeatures("height=300,width=300");

				                     //	opener.setWindowName("_blank");
				                       // viewUploadDocWindow.extend(viewUploadedDocument);
				                     		//getUI().getPage().open(imageUrl, "_blank",900,5000,BorderStyle.DEFAULT);
										//
				                        
				                        //getUI().getPage().open(r, "_blank", false);
				                		getUI().getPage().open(imageUrl, "_blank",1550,650,BorderStyle.NONE);


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
										// viewUploadDocWindow = new BrowserWindowOpener(imageUrl);
										//opener.setWindowName(null);
										
										// viewUploadDocWindow.setFeatures("height=693,width=1400,resizable");

											//opener.setWindowName("_blank");
											 
												//getUI().getPage().open(imageUrl, "_blank",900,5000,BorderStyle.DEFAULT);
										// getUI().getPage().open(imageUrl, "_blank");
										getUI().getPage().open(imageUrl, "_blank",1550,650,BorderStyle.NONE);

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
									 //viewUploadDocWindow = new BrowserWindowOpener(imageUrl);
									//opener.setWindowName(null);
									
										//viewUploadDocWindow.setFeatures("height=693,width=1400,resizable");
										//opener.setFeatures("height=500,width=300");

									//	opener.setWindowName("_blank");
									//	 opener.extend(viewUploadedDocument);
									
									//getUI().getPage().open(imageUrl, "_blank");
									getUI().getPage().open(imageUrl, "_blank",1550,650,BorderStyle.NONE);

									//getUI().getPage().open(imageUrl, "_blank",900,5000,BorderStyle.DEFAULT);
									
									
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
								//viewUploadDocWindow.extend(viewUploadedDocument);
						}
							
							/*if (layout != null
									&& layout.getComponentCount() > 0) {
								
								layout.removeAllComponents();
							}
							
							layout.addComponent(viewUploadedDocument);
							layout.addComponent(hlayout);
							layout.addComponent(table);*/
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
						
						
						BPMClientContext bpmClientContext = new BPMClientContext();
						Map<String,String> tokenInputs = new HashMap<String, String>();
						 tokenInputs.put("intimationNo", bean.getIntimationNo());
						 String intimationNoToken = null;
						  try {
							  intimationNoToken = createJWTTokenForClaimStatusPages(tokenInputs);
						  } catch (NoSuchAlgorithmException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  } catch (ParseException e) {
							  // TODO Auto-generated catch block
							  e.printStackTrace();
						  }
						  tokenInputs = null;
						String url = bpmClientContext.getGalaxyDMSUrl() + intimationNoToken;
						/*Below code commented due to security reason
						String url = bpmClientContext.getGalaxyDMSUrl() + bean.getIntimationNo();*/
					//	getUI().getPage().open(url, "_blank");
						getUI().getPage().open(url, "_blank",1550,650,BorderStyle.NONE);
/*
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
							
							private static final long serialVersionUID = 1L;

							@Override
							public void windowClose(CloseEvent e) {
								System.out.println("Close listener called");
							}
						});

						popup.setModal(true);
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOCUMENT_VIEW_PAGE,dmsDocumentDetailsViewPage);
						
						dmsDocumentViewListenerTable = dmsDocumentViewListenerTableObj.get();
						
						VaadinService.getCurrentRequest().getWrappedSession().setAttribute(SHAConstants.DMS_DOC_TABLE,dmsDocumentViewListenerTable);*/
					//	getSession().setAttribute(SHAConstants.DMS_DOCUMENT_DTO,dmsDTO);
						/*
						claimsDMSWindow = new BrowserWindowOpener(DMSDocumentViewUI.class);
						claimsDMSWindow.setWindowName("_blank");*/
						/*
						claimsDMSWindow.setFeatures("height=693,width=1300,resizable");
						claimsDMSWindow.extend(viewClaimsDMSDocument);*/
						//claimsDMSWindow.markAsDirty();
					//	UI.getCurrent().addWindow(popup);
						
					}
				});
			}
		}
	}
	
	private BillEntryDetailsDTO populatePreviousRowValues(BillEntryDetailsDTO duplicateDTO)
	{
		BillEntryDetailsDTO newDto = new BillEntryDetailsDTO();
		newDto.setItemName(duplicateDTO.getItemName());
		newDto.setClassification(duplicateDTO.getClassification());
		newDto.setCategory(duplicateDTO.getCategory());
		newDto.setIrdaLevel1(duplicateDTO.getIrdaLevel1());
		newDto.setIrdaLevel2(duplicateDTO.getIrdaLevel2());
		newDto.setIrdaLevel3(duplicateDTO.getIrdaLevel3());
		newDto.setNoOfDays(duplicateDTO.getNoOfDays());
		newDto.setPerDayAmt(duplicateDTO.getPerDayAmt());
		newDto.setItemValue(duplicateDTO.getItemValue());
		newDto.setNoOfDaysAllowed(duplicateDTO.getNoOfDaysAllowed());
		newDto.setPerDayAmtProductBased(duplicateDTO.getPerDayAmtProductBased());
		newDto.setAmountAllowableAmount(duplicateDTO.getAmountAllowableAmount());
		newDto.setNonPayableProductBased(duplicateDTO.getNonPayableProductBased());
		newDto.setNonPayable(duplicateDTO.getNonPayable());
		newDto.setReasonableDeduction(duplicateDTO.getReasonableDeduction());
		newDto.setTotalDisallowances(newDto.getTotalDisallowances());
		newDto.setNetPayableAmount(duplicateDTO.getNetPayableAmount());
		newDto.setDeductibleOrNonPayableReason(duplicateDTO.getDeductibleOrNonPayableReason());
		newDto.setMedicalRemarks(duplicateDTO.getMedicalRemarks());
		
		return newDto;
	}
	
	
	
	public void manageListeners() {

		for (BillEntryDetailsDTO billEntryDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(billEntryDetailsDTO);
			
			final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");
			final TextField medicalRemarks = (TextField)combos.get("medicalRemarks");
			enableOrDisableFld(medicalRemarks);
			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);

		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			BillEntryDetailsDTO entryDTO = (BillEntryDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			/*if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(entryDTO);
			}*/
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
			
			
			if ("itemName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
			//field.setConverter(plainIntegerConverter);
				
				//field.focus();
				
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z , .]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemName", field);
				return field;
			}
			
			else if ("itemNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(6);
				field.setData(entryDTO);
			//	field.setConverter(plainIntegerConverter);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemNo", field);

				return field;
			}
			else if ("classification".equals(propertyId)) {
				GComboBox box = new GComboBox();
				
				//box.focus();
				box.setWidth("150px");
				tableRow.put("classification", box);
				box.setData(entryDTO);
				final GComboBox category = (GComboBox) tableRow.get("category");
				addClassificationListener(box,category);
				addBillClassificationValues(box);
				return box;
			}
			else if ("category".equals(propertyId)) {
				GComboBox box = new GComboBox();
				//box.focus();
				box.setWidth("150px");
				tableRow.put("category", box);
				box.setData(entryDTO);
				GComboBox classification = (GComboBox) tableRow.get("classification");
				if(entryDTO.getClassification() != null) {
					extracted(classification);
				}
			//	box.addBlurListener(categoryListener());
				//daysAndAmtValidationListener(box);
				box.addValueChangeListener(categoryListener(box));
				// Need to check with yosuva, why this line was added.
				//if(entryDTO != null && entryDTO.getCategory()==null){
			
				//}
				//addCommonValues(box, "source");
				return box;
			}
			else if ("irdaLevel1".equals(propertyId)) {
				GComboBox box = new GComboBox();
				//box.focus();
				box.setWidth("150px");
				tableRow.put("irdaLevel1", box);
				box.setData(entryDTO);
				
				final GComboBox irdaLevel2 = (GComboBox) tableRow.get("irdaLevel2");
				//final ComboBox irdaLevel3 = (ComboBox) tableRow.get("irdaLevel3");
				addIrdaLevelListener(box,irdaLevel2);
				addIRDALevel1Values(box);

				return box;
			}
			else if ("irdaLevel2".equals(propertyId)) {
				GComboBox box = new GComboBox();
				//box.focus();
				box.setWidth("150px");
				tableRow.put("irdaLevel2", box);
				box.setData(entryDTO);
				
				GComboBox irdaLevel1 = (GComboBox) tableRow.get("irdaLevel1");
				if(entryDTO.getIrdaLevel1() != null) {
//					populateIrdaLevel2Values(irdaLevel1);
					addIrdaLevel2Values(entryDTO.getIrdaLevel1().getId(), box,entryDTO.getIrdaLevel2());
				}
				

				//final GComboBox irdaLevel3 = (GComboBox) tableRow.get("irdaLevel3");
				//addIrdaLevel2Listener(box,irdaLevel3);

//				final ComboBox irdaLevel3 = (ComboBox) tableRow.get("irdaLevel3");
				addIrdaLevel2Listener(box,null);
				/***
				 * @author yosuva.a
				 */
				
				return box;
			}
			else if ("irdaLevel3".equals(propertyId)) {  
				GComboBox box = new GComboBox();
				//box.focus();
				box.setWidth("150px");
				tableRow.put("irdaLevel3", box);
				box.setData(entryDTO);
				/***
				 * @author yosuva.a
				 */
				GComboBox irdaLevel2 = (GComboBox) tableRow.get("irdaLevel2");
				if(entryDTO.getIrdaLevel2() != null){
//					populateIrdaLevel3Values(irdaLevel2);
					addIrdaLevel3Values(entryDTO.getIrdaLevel2().getId(), box,entryDTO.getIrdaLevel3());
				}
				
				return box;
			}
			else if("noOfDays".equals(propertyId)) {
				TextField field = new TextField();
				//field.focus();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setConverter(plainIntegerConverter);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9. ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfDays", field);
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					/*if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()))*/
					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) 
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							|| ("ICCU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setValue(null);
						field.setEnabled(false);
						
					}
				}
				//field.addBlurListener(getNoOfDaysListener(/*"AmountClaimed"*/));
				field.addValueChangeListener(getNoOfDaysListener(field));
				final TextField txt = (TextField) tableRow.get("itemNo");
				generateSlNo(txt);
				return field;
			}
			else if("perDayAmt".equals(propertyId)) {
				TextField field = new TextField();
				//field.focus();
				field.setWidth("80px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				//field.setConverter(plainIntegerConverter);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("perDayAmt", field);
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					/*if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue())
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue())*/
					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) 
							|| ("ICCU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) 
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
							
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
				}
				
				//field.addBlurListener(getPerDayAmtListener(/*"AmountClaimed"*/));
				field.addValueChangeListener(getPerDayAmtListener(field));
				return field;
			}
			else if("itemValue".equals(propertyId)) {
				TextField field = new TextField();
				//field.focus();
				field.setWidth("80px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setConverter(plainIntegerConverter);
				//Added for process claim billing bill entry screen. 
				field.setData(entryDTO);
				field.setEnabled(true);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemValue", field);
				valueChangeLisenerForText(field);
			//	calculateNetPayableAmount(field);
				calculateTotal();
				enableOrDisableFields(field,"itemValue");
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							//|| ("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							)
					{
						field.setValue(null);
						field.setEnabled(false);
					}
					 if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) 
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							|| ("ICCU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
					{
						field.setEnabled(false);
					}
					/*else
					{
						field.setEnabled(true);
					}*/
				}
				return field;
			}
			else if("noOfDaysAllowed".equals(propertyId)) {
				TextField field = new TextField();
				//field.focus();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setConverter(plainIntegerConverter);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9. ,]*$");
				validator.setPreventInvalidTyping(true);
				field.addBlurListener(getNoOfDaysAllowableListener());
				//field.addValueChangeListener(getNoOfDaysAllowableListenerValue());
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))

					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
							
							//|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())))
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						
					}
					/*if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							|| ("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							)
					{
						field.setEnabled(false);
					}
					else
					{
						field.setEnabled(true);
					}*/
				}
				TextField itemValueFld = (TextField) tableRow.get("itemValue");
				if(null != itemValueFld)
				{
					if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
					{
					if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
						//	|| ("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							)
					{
						itemValueFld.setValue(null);
						itemValueFld.setEnabled(false);
					}
					}
				}
				tableRow.put("noOfDaysAllowed", field);
				return field;
			}
			else if("perDayAmtProductBased".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				
				field.setWidth("80px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setConverter(plainIntegerConverter);
				field.setData(entryDTO);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.addBlurListener(getProductBasedPerDayAmtListener());
			//	field.addValueChangeListener(getProductBasedPerDayAmtListenerValue());
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) )
							
							//|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) )
					{
						field.setEnabled(true);
						if(null != entryDTO.getProductBasedRoomRent())
						{
							String roomRent = String.valueOf(entryDTO.getProductBasedRoomRent()); 
							field.setValue(roomRent);
						}
					}
					else
					{
						field.setEnabled(false);
						
					}
					if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							|| ("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							)
					{
						field.setEnabled(false);
					}
					else
					{
						field.setEnabled(true);
					}
				}*/
				//Vaadin8-setImmediate() field.setImmediate(true);
				tableRow.put("perDayAmtProductBased", field);
				return field;
			}
			else if("amountAllowableAmount".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("amountAllowableAmount", field);
				
				//IMSSUPPOR-21035
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					if((("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())))
					{
						field.setEnabled(false);
						Double noOfDays = entryDTO.getNoOfDaysAllowed();
						Double perDayAmt = entryDTO.getPerDayAmtProductBased();
						Double itemValue = entryDTO.getItemValue();
						if(null != noOfDays && null != perDayAmt)
						{
							Double val = noOfDays*perDayAmt;
							entryDTO.setAmountAllowableAmount(val);
							field.setValue(String.valueOf(val));
							 if(0 == perDayAmt && null!= itemValue)
								{
									field.setValue(String.valueOf(itemValue));
								}
						}
					}
					else
					{
						//field.setEnabled(true);
						
					}
				}*/
				
				//final TextField txtFld = (TextField) tableRow.get("perDayAmtProductBased");
				
				valueChangeLisenerForAllowableAmount(field);
				calculateAllowableAmountTotal();
				//calculateNetPayableAmount(field);
				enableOrDisableFields(field,"amountAllowableAmount");
				populateProductBasedPerDayAmt();
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							||("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							)
					{
						field.setEnabled(false);
					}
					else
					{
						//field.setEnabled(true);
					}
				}
				
				//IMSSUPPOR-21035
				/*if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					if((("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())))
					{
						field.setEnabled(false);
						Double noOfDays = entryDTO.getNoOfDaysAllowed();
						Double perDayAmt = entryDTO.getPerDayAmtProductBased();
						if(null != noOfDays && null != perDayAmt)
						{
							Double val = noOfDays*perDayAmt;
							entryDTO.setAmountAllowableAmount(val);
							field.setValue(String.valueOf(val));
						}
					}
					else
					{
						//field.setEnabled(true);
						
					}
				}*/
				
			//	field.addBlurListener(getNoOfDaysListener("AmountEntitled"));
				return field;
			}
			else if("nonPayableProductBased".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				field.addBlurListener(getNonPayablePdtBasedListener());
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("nonPayableProductBased", field);
				return field;
			}

			else if("nonPayable".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setData(entryDTO);
				field.setConverter(plainIntegerConverter);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				field.addBlurListener(getNonPayableListener());
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("Deductibles").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
					{
						field.setEnabled(false);
					}
					else
					{
						field.setEnabled(true);
					}
				}
				tableRow.put("nonPayable", field);
				return field;
			}
			else if("reasonableDeduction".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				
				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(15);
				field.setConverter(plainIntegerConverter);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9,]*$");
				validator.setPreventInvalidTyping(true);
				field.addBlurListener(getResonableDeductionListener());
				tableRow.put("reasonableDeduction", field);
				
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					//if(("ICU Rooms").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()))
					if(("Hospital Discount").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()))
					{
						//field.setValue(null);
						field.setEnabled(false);
					}
					 /*if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue().trim())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue().trim()) )
					{
						field.setEnabled(false);
					}*/
					/*else
					{
						field.setEnabled(true);
					}*/
				}
				
				if(null != entryDTO && (entryDTO.getReasonableDeduction() == null || entryDTO.getReasonableDeduction() <= 0)){
					field.setEnabled(false);
				}
				
				return field;
			}
			else if("totalDisallowances".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				
				field.setWidth("80px");
				field.setNullRepresentation("");
				//field.setMaxLength(10);
				field.setData(entryDTO);
				field.setConverter(plainIntegerConverter);
				field.setEnabled(false);
				populateReasonableDeduction(field);
				valueChangeLisenerForTotalDisallowances(field);
				calculateNetPayableAmount(field);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);

				tableRow.put("totalDisallowances", field);
				return field;
			}
			
			else if ("netPayableAmount".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setEnabled(false);
				//calculateNetPayableAmount(field);
				valueChangeLisenerForNetPayableAmount(field);
				field.setConverter(plainIntegerConverter);
				calculateTotalDisallowances(field);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ,]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("netPayableAmount", field);
				return field;
			}
			else if("deductibleOrNonPayableReason".equals(propertyId)) {
				TextField field = new TextField();
				
				//field.focus();

				field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(1000);
				field.setData(entryDTO);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ . ,]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("deductibleOrNonPayableReason", field);
				calculateNetPayableAmount(field);
				handleEnter(field, null);
				
				/*Panel p = new Panel();
				p.setContent(field);
				p.addActionHandler(field);*/
			//	field.addListener(deductibleRemarksListener(field));
				/*field.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
				field.setTextChangeTimeout(3000);*/
				
				return field;
			}
			
			else if("medicalRemarks".equals(propertyId)) {
				TextField field = new TextField();
				//field.focus();

				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setMaxLength(300);
				field.setData(entryDTO);
//				CSValidator validator = new CSValidator();
//				validator.extend(field);
//				validator.setRegExp("^[a-zA-Z 0-9 @ . ,]*$");
//				validator.setPreventInvalidTyping(true);
				tableRow.put("medicalRemarks", field);
				enableOrDisableFld(field);
				
				if(presenterString != null && (presenterString.equalsIgnoreCase(SHAConstants.BILLING) || presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL))){
					field.setEnabled(false);
				}
				//addMedicalReasonListener(field);
				handleEnterForMedical(field, null);
				
				return field;
			}		
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				if (field instanceof TextField)
					field.setWidth("100%");
				return field;
			}
		}
	}

	private void populateReasonableDeduction(TextField box)
	{
		BillEntryDetailsDTO entryDetailsDTO = (BillEntryDetailsDTO) box.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(entryDetailsDTO);
		 ComboBox categoryBox = (ComboBox) hashMap.get("category");
		 TextField itemValue = (TextField) hashMap.get("itemValue");
		 TextField amountAllowableAmount = (TextField) hashMap.get("amountAllowableAmount");
		 TextField nonPayablePdtBased = (TextField) hashMap.get("nonPayableProductBased");
		 TextField reasonableDeduction = (TextField)hashMap.get("reasonableDeduction");
		 /**
		  * Added for ticket id 4268
		  * */
		 TextField allowedNoDays = (TextField)hashMap.get("noOfDaysAllowed");
		 TextField pdtBasedPerDayAmt = (TextField)hashMap.get("perDayAmtProductBased"); 
		 
		 if(null != categoryBox && null!= categoryBox.getValue())
		 {
			 SelectValue selValue = (SelectValue)categoryBox.getValue();
			
				 if(null != selValue && null != selValue.getValue() && (("ICU Room Rent").equalsIgnoreCase(selValue.getValue().trim()) || ("Room Rent").equalsIgnoreCase(selValue.getValue().trim())
						 || ("Nursing Charges").equalsIgnoreCase(selValue.getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue().trim())
						 || ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue().trim()) 
						 ||  ("ICCU Nursing Charges").equalsIgnoreCase(selValue.getValue().trim())))
			 {
					 
					 Integer reasonableDeductionVal = 0;
					 Integer claimedAmt = 0;
					 Integer amountAllowableAmt = 0;
					 if(null != itemValue)
					 {
						 claimedAmt  = SHAUtils.getIntegerFromStringWithComma(itemValue.getValue());
					 }
					 if(null != amountAllowableAmount )
					 {
						 amountAllowableAmt = SHAUtils.getIntegerFromStringWithComma(amountAllowableAmount.getValue());
						 if(null != amountAllowableAmount )
						 {
							 amountAllowableAmt = SHAUtils.getIntegerFromStringWithComma(amountAllowableAmount.getValue());
							/* Integer noOfDays = 0;
							 Integer pdtBasedAmt = 0;
							 if(null != allowedNoDays)
								  noOfDays = SHAUtils.getIntegerFromStringWithComma(allowedNoDays.getValue());
							 if(null != pdtBasedPerDayAmt)
								  pdtBasedAmt = SHAUtils.getIntegerFromStringWithComma(pdtBasedPerDayAmt.getValue());
								 if(null != noOfDays && null != pdtBasedAmt)
									 amountAllowableAmt = noOfDays*pdtBasedAmt;*/
								 
						 }
					 }
					 if(claimedAmt >= amountAllowableAmt)
					 {
						 reasonableDeductionVal = claimedAmt - amountAllowableAmt;
					 }
					 else
					 {
						 reasonableDeductionVal = 0;
					 }
					 
				/* if(null != itemValue )
				 {
					 reasonableDeductionVal = getDiffOfTwoNumber(Double.parseDouble(SHAUtils.getIntegerFromStringWithComma(itemValue.getValue()).toString()) , reasonableDeductionVal);
	//				 reasonableDeductionVal = ;
					// reasonableDeductionVal -= SHAUtils.getIntegerFromStringWithComma(itemValue.getValue()); 
							 //Double.parseDouble(itemValue.getValue());
				 }
				 if(null != amountAllowableAmount)
				 {
					 reasonableDeductionVal = getDiffOfTwoNumber(Double.parseDouble(SHAUtils.getIntegerFromStringWithComma(amountAllowableAmount.getValue()).toString()) , reasonableDeductionVal);
					// reasonableDeductionVal -= SHAUtils.getIntegerFromStringWithComma(amountAllowableAmount.getValue());
					// reasonableDeductionVal = SHAUtils.getIntegerFromStringWithComma(amountAllowableAmount.getValue()) - reasonableDeductionVal;
					// amountAllowableAmount.setEnabled(false);
				 }
				 if(null != nonPayablePdtBased)
				 {
					 reasonableDeductionVal = getDiffOfTwoNumber(Double.parseDouble(SHAUtils.getIntegerFromStringWithComma(nonPayablePdtBased.getValue()).toString()) , reasonableDeductionVal);
					// reasonableDeductionVal -=SHAUtils.getIntegerFromStringWithComma(nonPayablePdtBased.getValue());
					 //reasonableDeductionVal = SHAUtils.getIntegerFromStringWithComma(nonPayablePdtBased.getValue()) - reasonableDeductionVal;
					// amountAllowableAmount.setEnabled(false);
				 }*/
					 
				//CR20181301 - GALAXYMAIN-12029	 
				 /*if(null != reasonableDeduction)
				 {
					 reasonableDeduction.setValue(String.valueOf(reasonableDeductionVal));
					 if(reasonableDeductionVal <= 0) {
						 reasonableDeduction.setEnabled(false);
					 } else {
						 reasonableDeduction.setEnabled(true);
					 }
					 calculateTotalDisallowances(box);
				 }*/
					 
				//CR20181301 - added for jira GALAXYMAIN-12029.
				if(null != nonPayablePdtBased)
				{
					nonPayablePdtBased.setValue(String.valueOf(reasonableDeductionVal));
					nonPayablePdtBased.setEnabled(false);
					calculateTotalDisallowances(box);
				}
			 }
		 }
		 
	}
	
	
	
	private Double getDiffOfTwoNumber(Double number1 , Double number2)
	{
		Double diff = 0d;
		if(number1 > number2)
		{
			diff = number1 - number2;
		}
		else
		{
			diff = number2 - number1;
		}
		return diff;
	}
	
	private void enableOrDisableFld(TextField box)
	{
		BillEntryDetailsDTO entryDetailsDTO = (BillEntryDetailsDTO) box.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(entryDetailsDTO);
		 ComboBox categoryBox = (ComboBox) hashMap.get("category");
		 TextField itemValue = (TextField) hashMap.get("itemValue");
		 TextField amountAllowableAmount = (TextField) hashMap.get("amountAllowableAmount");
		 TextField txtReasonableDeduction = (TextField) hashMap.get("reasonableDeduction");
		 TextField nonPayable = (TextField) hashMap.get("nonPayable");
		 if(null != categoryBox && null!= categoryBox.getValue())
		 {
			 SelectValue selValue = (SelectValue)categoryBox.getValue();
			 if(null != selValue && null != selValue.getValue() && (("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim())))
					 //|| ("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
					 //)
			 {
				 if(null != itemValue)
				 {
					// itemValue.setReadOnly(true);
					 itemValue.setEnabled(false);
				 }
				 if(null != amountAllowableAmount)
				 {
					 //amountAllowableAmount.setReadOnly(true);
					 amountAllowableAmount.setEnabled(false);
				 }
				/* if(null != txtReasonableDeduction)
				 {
					 if(null != itemValue)
					 {
						 txtReasonableDeduction.setValue(itemValue.getValue());
					 }
				 }*/
				 
				 
			 }
			 else if(("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
			 {
				 if(null != itemValue)
				 {
					// itemValue.setReadOnly(true);
					 itemValue.setEnabled(true);
				 }
			 }
			 else if(("ICU Room Rent").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue()) 
					 ||  ("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue())
					 || ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue()) || ("ICCU  Nursing Charges").equalsIgnoreCase(selValue.getValue()))
			 {
				 if(null != nonPayable)
				 {
					 nonPayable.setValue(null);
					 nonPayable.setEnabled(false);
				 }
			 }
		 }
	}
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("itemNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setValue(String.valueOf(i)); 
					 itemNoFld.setEnabled(false);
				 }
			 }
		 }
		
	}
	

	private void populateProductBasedPerDayAmt()
	{
		
		Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				// TextField claimedNoOfDays = (TextField) hashMap.get("noOfDays");
				 TextField noOfDaysPdtBased  = (TextField)hashMap.get("noOfDaysAllowed");
				 TextField pdtPerDayAmt = (TextField)hashMap.get("perDayAmtProductBased");
				 TextField claimedPerDayAmt = (TextField)hashMap.get("perDayAmt");
				 TextField itemValue = (TextField) hashMap.get("itemValue");
				 TextField allowableAmt = (TextField)hashMap.get("amountAllowableAmount");
				 if(null != pdtPerDayAmt)
				 {
					 
					 if (null != billEntryDetailsDTO.getCategory() && ("ICU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())  )
							 {	
						 		if(null != this.pdtICURentAmt && 0 != this.pdtICURentAmt)
						 		pdtPerDayAmt.setValue(String.valueOf(this.pdtICURentAmt));
						 		else
						 		{
						 			if(null != claimedPerDayAmt)
						 			{
								 	//pdtPerDayAmt.setValue(claimedPerDayAmt.getValue());
						 				if(null != itemValue)
						 				{
						 					allowableAmt.setValue(itemValue.getValue());
						 				}
						 			  pdtPerDayAmt.setValue("0");
						 			}
						 		}
							 }
					 else  if (null != billEntryDetailsDTO.getCategory() && ("ICCU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())  )
					 {	
					 		if(null != this.pdtICCURentAmt && 0 != this.pdtICCURentAmt)
					 		pdtPerDayAmt.setValue(String.valueOf(this.pdtICCURentAmt));
					 		else
					 		{
					 			if(null != claimedPerDayAmt)
					 			{
							 	//pdtPerDayAmt.setValue(claimedPerDayAmt.getValue());
					 				if(null != itemValue)
					 				{
					 					allowableAmt.setValue(itemValue.getValue());
					 				}
					 			  pdtPerDayAmt.setValue("0");
					 			}
					 		}
						 }
					 else if(null != billEntryDetailsDTO.getCategory() &&  ("Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
							{
						 		if(null != this.pdtPerDayAmt && 0 != this.pdtPerDayAmt)
						 			pdtPerDayAmt.setValue(String.valueOf(this.pdtPerDayAmt));
						 		else
						 		{
						 			if(null != claimedPerDayAmt)
						 			{
						 				//pdtPerDayAmt.setValue(claimedPerDayAmt.getValue());
						 				pdtPerDayAmt.setValue("0");
						 			}

						 		}
							}
					 else if(null != billEntryDetailsDTO.getCategory() &&  ("Ambulance Charges").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()))
						{
						 /*if(null != ambulanceList && ambulanceList.isEmpty())
						 {*/
					 		if(null != this.pdtAmbulanceAmt && 0 != this.pdtAmbulanceAmt)
					 		{
					 			allowableAmt.setValue(String.valueOf(this.pdtAmbulanceAmt));
					 			billEntryDetailsDTO.setAmountAllowableAmount(this.pdtAmbulanceAmt);
					 			ambulanceList.add(allowableAmt);
					 		}
					 		else
					 		{
					 			if(null != allowableAmt)
					 				//allowableAmt.setValue(itemValue.getValue());
					 				allowableAmt.setValue("0");
					 		}
						/* }
						 else
						 {
							 if(null != allowableAmt)
								 allowableAmt.setValue("0");
						 }*/
						}
					 if(null != allowableAmt)
					 {
						 Double noOfDays = 0d;
						 Double perDayAmt = 0d;
						 if(null != noOfDaysPdtBased)
						 {
							 String noOfDayStr = (String)noOfDaysPdtBased.getValue();
							 if(null != noOfDayStr && !noOfDayStr.equalsIgnoreCase(""))
							 {
								 if(null != noOfDays)
								 noOfDays = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(noOfDayStr));
							 }
						 }
						 if(null != pdtPerDayAmt )
						 {
							// pdtPerDayAmt.setEnabled(true);
							String pdtPerDayAmtStr = (String) pdtPerDayAmt.getValue();
							if(null != pdtPerDayAmtStr && !pdtPerDayAmtStr.equalsIgnoreCase(""))
							{
								if(null != perDayAmt)
								{
									perDayAmt = Double.valueOf(SHAUtils.getDoubleFromStringWithComma(pdtPerDayAmtStr));
									//pdtPerDayAmt.setEnabled(false);

								}
								
							}
						 }
						 Double amt = noOfDays * perDayAmt;
						 if(null != allowableAmt)
						 {
							 allowableAmt.setValue(String.valueOf(amt));
							 if(null != pdtPerDayAmt &&  null != pdtPerDayAmt.getValue())
							 {
								 Double pdtperDayamt  = SHAUtils.getDoubleFromStringWithComma(pdtPerDayAmt.getValue());
								 if (null != billEntryDetailsDTO.getCategory() && ("ICU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())  && 
										 null != pdtPerDayAmt &&  0 == pdtperDayamt && null != itemValue)
								 {
									 allowableAmt.setValue(itemValue.getValue());
								 }else  if (null != billEntryDetailsDTO.getCategory() && ("ICCU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())  && 
										 null != pdtPerDayAmt &&  0 == pdtperDayamt && null != itemValue)
								 {
									 allowableAmt.setValue(itemValue.getValue());
								 }
							 }
						 }
					 }
						 //if(null != billEntryDetailsDTO.getCategory() && (("ICU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())) || ("Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) )
								 
					 //}
				 }
				
			 }
		 }
		
	}
	
	
	
	 public List<BillEntryDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 
	 @SuppressWarnings("unchecked")
		public void addBillClassificationValues(ComboBox comboBox) {
			BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("billClassification");
			comboBox.setContainerDataSource(billClassificationContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
	 
	 
	 @SuppressWarnings("unchecked")
		public void addIRDALevel1Values(ComboBox comboBox) {
			BeanItemContainer<SelectValue> irdaLevel1Container = (BeanItemContainer<SelectValue>) referenceData
					.get("irdaLevel1");
			comboBox.setContainerDataSource(irdaLevel1Container);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");

		}
		
	 
	 @SuppressWarnings("unused")
		private void addClassificationListener(final ComboBox billClassificationCombo,
				final ComboBox categoryCombo) {
			if (billClassificationCombo != null) {
				billClassificationCombo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						ComboBox component = (ComboBox) event.getComponent();
						
						extracted(component);
						resetFieldsBasedOnClassification(component);
					}

				});
			}

		}
	 
	 
	 private void resetFieldsBasedOnClassification(ComboBox component)
	 {
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			
			TextField noOfDays = (TextField) hashMap.get("noOfDays");
			TextField perDayAmt = (TextField) hashMap.get("perDayAmt");
			TextField itemValue = (TextField) hashMap.get("itemValue");
		//	ComboBox comboBox = (ComboBox) hashMap.get("category");
			if (billEntryDetailsDTO != null) {
				if(billEntryDetailsDTO.getClassification() != null) {
					if(null != component && null != component.getValue())
					{
						SelectValue selValue = (SelectValue)component.getValue();
						if(("Pre-Hospitalization").equals(selValue.getValue()) || ("Post-Hospitalization").equals(selValue.getValue()))
						{
							if(null != noOfDays)
							{
								if(noOfDays.isEnabled())
								{
									noOfDays.setValue(null);
								}
							}
							if(null != perDayAmt)
							{
								if(perDayAmt.isEnabled())
								{
									perDayAmt.setValue(null);
								}
							}
							if(null != itemValue)
							{
								if(!itemValue.isEnabled())
								{
								//itemValue.setValue(null);
								itemValue.setEnabled(true);
								}
							}
						}
					}
				}
			}			
		}
		
	 
	 
	 
	 @SuppressWarnings("unused")
		private void addIrdaLevelListener(final GComboBox irdaLevel1Combo,
				final ComboBox irdaLevel2Combo) {
			if (irdaLevel1Combo != null) {
				irdaLevel1Combo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						GComboBox component = (GComboBox) event.getComponent();
						
						populateIrdaLevel2Values(component);
					}

				});
			}

		}
	 
	 @SuppressWarnings("unused")
		private void addIrdaLevel2Listener(final GComboBox irdaLevel2Combo,
				final ComboBox irdaLevel3Combo) {
			if (irdaLevel2Combo != null) {
				irdaLevel2Combo.addListener(new Listener() {
					private static final long serialVersionUID = -4865225814973226596L;

					@Override
					public void componentEvent(Event event) {
						GComboBox component = (GComboBox) event.getComponent();
						
						populateIrdaLevel3Values(component);
					}

				});
			}

		}
	 
	 

	 
	 private void daysAndAmtValidationListener (final ComboBox component) {
		 
		 if(null != component)
		 {
			 component.addListener(new Listener() {
				
				@Override
				public void componentEvent(Event event) {
					// TODO Auto-generated method stub
					
					BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
					ComboBox comboBox = (ComboBox) hashMap.get("category");
					if (null != billEntryDetailsDTO) {
						if(null != billEntryDetailsDTO.getCategory()) {
							if(null != comboBox && null != comboBox.getValue()) {
								
								SelectValue selValue = (SelectValue)comboBox.getValue();
								HashMap<String, AbstractField<?>> valHashMap = tableItem.get(billEntryDetailsDTO);
								TextField noOfDays = (TextField) valHashMap.get("noOfDays");
								TextField perDayAmt = (TextField) valHashMap.get("perDayAmt");
								TextField itemValue = (TextField) valHashMap.get("itemValue");
								
								TextField noOfDaysAllowed = (TextField) valHashMap.get("noOfDaysAllowed");
								TextField perDayAmtPdtBased = (TextField) valHashMap.get("perDayAmtProductBased");
								TextField amountAllowableAmt = (TextField) valHashMap.get("amountAllowableAmount");
								TextField nonPayable = (TextField) valHashMap.get("nonPayable");
								TextField totalDisallowance = (TextField) valHashMap.get("totalDisallowances");
								TextField reasonableDeduction = (TextField) valHashMap.get("reasonableDeduction");
								//TextField amountAllowableAmt = (TextField) valHashMap.get("amountAllowableAmount");
								
								
								//if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue()))
								/*if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
										
										|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue())
										)*/
									if(("ICU Room Rent").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
											|| ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue()))
											
										//	|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue()) )
								{
									if(null != noOfDays)
									{
										noOfDays.setEnabled(true);
									}
									if (null != perDayAmt)
									{
										perDayAmt.setEnabled(true);
									}
									if(null != noOfDaysAllowed)
									{
										noOfDaysAllowed.setEnabled(true);
									}
									if(null != perDayAmtPdtBased)
									{
										//perDayAmtPdtBased.setEnabled(true);
										perDayAmtPdtBased.setEnabled(false);
										//perDayAmtPdtBased.setValue(pdtPerDayAmt);
									}
									if(null != itemValue)
									{
										//itemValue.setValue(null);
										itemValue.setEnabled(false);
									}
									if(null != amountAllowableAmt)
									{
										Double noDays = 0d;
										Double perDayAmtVal= 0d;
										if(null != noOfDaysAllowed)
										{
											if(null != noOfDaysAllowed.getValue())
												noDays = Double.valueOf(noOfDaysAllowed.getValue());
										}
										if(null != perDayAmtPdtBased && null != perDayAmtPdtBased.getValue())
										{
											perDayAmtVal = Double.valueOf(perDayAmtPdtBased.getValue());
										}
										Double val1 = noDays*perDayAmtVal;
										amountAllowableAmt.setValue(String.valueOf(val1));
									}
									if(null != nonPayable)
									{
										nonPayable.setValue(null);
										nonPayable.setEnabled(false);
									}
									
								}
									else if (("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue()))
									{
										if(null != noOfDaysAllowed)
										{
											noOfDaysAllowed.setEnabled(false);
										}
										if(null != perDayAmtPdtBased)
										{
											perDayAmtPdtBased.setEnabled(false);
										}
										if(null != itemValue)
										{
											itemValue.setEnabled(false);
										}
										if(null != nonPayable)
										{
											nonPayable.setValue(null);
											nonPayable.setEnabled(false);
										}
									}
									
									else if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim())
											|| ("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
									{
										if(("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()) && null != itemValue)
										{
											itemValue.setEnabled(true);
											if(null != totalDisallowance)
											{
												totalDisallowance.setEnabled(false);
											}
											if(null != reasonableDeduction)
											{
												reasonableDeduction.setEnabled(false);
											}
										}
										else if(null != itemValue)
										{
											itemValue.setValue(null);
											itemValue.setEnabled(true);
										}
										if(null != noOfDays)
										{
											noOfDays.setValue(null);
											noOfDays.setEnabled(false);
										}
										if (null != perDayAmt)
										{
											perDayAmt.setEnabled(false);
										}
										
										if(null != amountAllowableAmt)
										{
											/*if(null != amountAllowableAmt.getValue())
											{
												amountAllowableAmt.setValue(null);
											}*/
											amountAllowableAmt.setEnabled(false);
										}
										if(null != nonPayable)
										{
											nonPayable.setEnabled(false);
										}
										
										if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim()))
										{
											if(!reasonableDeduction.isEnabled())
											{
												reasonableDeduction.setEnabled(true);
											}
											/*if(null != itemValue)
											{
												itemValue.setValue(null);
												itemValue.setEnabled(false);
											}*/
										}
									}
									/*else if (("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
									{
										if(null != itemValue)
										{
											itemValue.setEnabled(true);
										}
										
									}*/
								else
								{
									if(null != noOfDays)
									{
										if(!(null != billEntryDetailsDTO.getItemValue() && (billEntryDetailsDTO.getItemValue() > 0)))
										{
											noOfDays.setValue(null);
										}
										noOfDays.setEnabled(false);
										
									}
									if(null != perDayAmt)
									{
										if(!(null != billEntryDetailsDTO.getItemValue() && (billEntryDetailsDTO.getItemValue() > 0)))
										{
											perDayAmt.setValue(null);
										}
										perDayAmt.setEnabled(false);
									}
									if(null != itemValue)
									{
										//itemValue.setValue(null);
										itemValue.setEnabled(true);
									}
									if(null != noOfDaysAllowed)
									{
										noOfDaysAllowed.setEnabled(false);
									}
									if(null != perDayAmtPdtBased)
									{
										perDayAmtPdtBased.setEnabled(false);
									}
									if(null != amountAllowableAmt)
									{
										amountAllowableAmt.setValue(null);
									}
									if(null != nonPayable)
									{
										nonPayable.setEnabled(true);
									//	nonPayable.setValue(null);
									}
									if(null != reasonableDeduction)
									{
										reasonableDeduction.setEnabled(true);
										
											//reasonableDeduction.setValue(null);
									}
								}
							}
						}					
					}
					
				}
			});
		 }
	}
	 
		private void extracted(ComboBox component) {
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			GComboBox comboBox = (GComboBox) hashMap.get("category");
			if (billEntryDetailsDTO != null) {
				if(billEntryDetailsDTO.getClassification() != null) {
					if(comboBox != null) {
						addCategoryValue(billEntryDetailsDTO.getClassification().getId(), comboBox, billEntryDetailsDTO.getCategory());
					}
				}					
			}
		}
		
		
		private void populateIrdaLevel2Values(GComboBox component) {
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			GComboBox irdaLevel2 = (GComboBox) hashMap.get("irdaLevel2");
			//ComboBox irdaLevel3 = (ComboBox) hashMap.get("irdaLevel3");
			if (billEntryDetailsDTO != null) {
				if(billEntryDetailsDTO.getClassification() != null) {
					if(null != irdaLevel2) {
						if(null != billEntryDetailsDTO.getIrdaLevel1())
						addIrdaLevel2Values(billEntryDetailsDTO.getIrdaLevel1().getId(), irdaLevel2, billEntryDetailsDTO.getIrdaLevel2());
					}
					/*if(null != irdaLevel3) {
						addIrdaLevel3Values(billEntryDetailsDTO.getIrdaLevel1().getId(), irdaLevel3, billEntryDetailsDTO.getIrdaLevel3());
					}*/
				}					
			}
		}
		
		private void populateIrdaLevel3Values(GComboBox component) {
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			GComboBox irdaLevel3 = (GComboBox) hashMap.get("irdaLevel3");
			//ComboBox irdaLevel3 = (ComboBox) hashMap.get("irdaLevel3");
			if (billEntryDetailsDTO != null) {
				if(billEntryDetailsDTO.getClassification() != null) {
					if(null != irdaLevel3 && billEntryDetailsDTO.getIrdaLevel2() != null) {
						addIrdaLevel3Values(billEntryDetailsDTO.getIrdaLevel2().getId(), irdaLevel3, billEntryDetailsDTO.getIrdaLevel3());
					}
					/*if(null != irdaLevel3) {
						addIrdaLevel3Values(billEntryDetailsDTO.getIrdaLevel1().getId(), irdaLevel3, billEntryDetailsDTO.getIrdaLevel3());
					}*/ 
				}					
			}
		}
	 
		public void valueChangeLisenerForText(final TextField total){
			
			if(null != total)
			{
				
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					System.out.println("value before ==============================" + event.getProperty().getValue());
					populateProductBasedPerDayAmt();
					calculateTotal();
					populateReasonableDeduction(total);
					calculateNetPayableAmount(total);
					System.out.println("value after ==============================" + event.getProperty().getValue());
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
		
		
		public void valueChangeLisenerForAllowableAmount(final TextField total){
			
			if(null != total)
			{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateAllowableAmountTotal();
					populateReasonableDeduction(total);
					calculateNetPayableAmount(total);
					//calculateTotalAmount(total);
					
				}
			});
			}
		
			
		}
		
/*	 public BlurListener getNoOfDaysListener(final String type) {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					calculateItemValue(componenttype);
				}
			};
			return listener;
		}*/
	 
	 public ValueChangeListener getNoOfDaysListener(final TextField component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//CombBox comp = (ComboBox) event.
				//GComboBox component = (GComboBox) event.
				calculateItemValue(component/*type*/);
				//populateProductPerDayAmt(component);
				
			}
		};
		return listener;
	 }
	 
	 
	/* public BlurListener categoryListener(final String type) {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					GComboBox component = (GComboBox) event.getComponent();
					populateProductPerDayAmt(component);
				}
			};
			return listener;
		}*/
	 
	 public ValueChangeListener categoryListener(final GComboBox component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//CombBox comp = (ComboBox) event.
				//GComboBox component = (GComboBox) event.
			//	populateProductPerDayAmt(component);
				SelectValue selValue = (SelectValue)event.getProperty().getValue();
						/*if(null != selValue)
						{
							Long id = selValue.getId();
							if(ReferenceTable.getPrePostNatalMap().containsKey(id))
							{
								warnMessageForPreAndPostNatal();
							}
						}*/
				populateValuesBasedOnCategoryValues(component);
				
			}
		};
		return listener;
	 }
	 
	/* public ValueChangeListener categoryListener()
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				CombBox comp = (ComboBox) event.
				
			}
		};
	 }
	 */
	 
	/* public ValueChangeListener getNoOfDaysAllowableListenerValue()
	 {
		 ValueChangeListener listener = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Blur Listener  before " + event.getProperty());			
				TextField component = (TextField) event.getProperty();
				calculateAllowableAmountValue(component);
				System.out.println("Blur Listener   " + event.getProperty());
			}
		};
		return listener;
	 }*/
	 
	 public BlurListener getNoOfDaysAllowableListener() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					System.out.println("Blur Listener  before " + event.getSource());			
					TextField component = (TextField) event.getComponent();
					calculateAllowableAmountValue(component);
					System.out.println("Blur Listener   " + event.getSource());
				}
			};
			return listener;
			
		}
	 
	 public ValueChangeListener getPerDayAmtListener(final TextField component)
	 {
		 ValueChangeListener listener =  new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				//CombBox comp = (ComboBox) event.
				//GComboBox component = (GComboBox) event.
				calculateItemValue(component/*type*/);
				//populateProductPerDayAmt(component);
				
			}
		};
		return listener;
	 }
	 
/*	 public BlurListener getPerDayAmtListener(final String type) {
			
			BlurListener listener = new BlurListener() {
				
				*//**
				 * 
				 *//*
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					calculateItemValue(component,type);
					
				}
			};
			return listener;
			
		}*/
	 
	 public ValueChangeListener getProductBasedPerDayAmtListenerValue()
	 {
		 ValueChangeListener listener = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Blur Listener  before " + event.getProperty());			
				TextField component = (TextField) event.getProperty();
				calculateAllowableAmountValue(component);
				System.out.println("Blur Listener   " + event.getProperty());
			}
		};
		return listener;
	 }
	 
	 public BlurListener getProductBasedPerDayAmtListener(/*final String type*/) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					calculateAllowableAmountValue(component);
					
				}
			};
			return listener;
			
		}
	 
		
	 	 public BlurListener getNonPayablePdtBasedListener() {
				
				BlurListener listener = new BlurListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField component = (TextField) event.getComponent();
						populateReasonableDeduction(component);
						calculateTotalDisallowances(component);
						
					}
				};
				return listener;
				
			}
	 	 
	 	 
	 	 public BlurListener getNonPayableListener() {
				
				BlurListener listener = new BlurListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField component = (TextField) event.getComponent();
						calculateTotalDisallowances(component);
					}
				};
				return listener;
				
			}
	 	 
	 	 public BlurListener getResonableDeductionListener() {
				
				BlurListener listener = new BlurListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField component = (TextField) event.getComponent();
						Double txtReasonableDeductionAmt = SHAUtils.getDoubleFromStringWithComma(component.getValue());
						 if(txtReasonableDeductionAmt > 0){
							 validationForPAReasonableDeduct(); 
						 } 
						calculateTotalDisallowances(component);
					}
				};
				return listener;
				
			}
		 
	 
	 	 
	public void valueChangeLisenerForTotalDisallowances(final TextField total){
			
			if(null != total)
			{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					summationOfTotalDisallowances();
					calculateNetPayableAmount(total);
					//calculateTotalAmount(total);
					
				}
			});
			}
		}
	
	public void valueChangeLisenerForNetPayableAmount(final TextField total){
		
		if(null != total)
		{
		total
		.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				summationOfNetPayableAmount();
				/*calculateNetPayableAmount(total);*/
				//calculateTotalAmount(total);
				
			}
		});
		}
	}
	
/*	public BlurListener addDeductibleRemarksListener(final TextField txtFld)
	{
		BlurListener listener = new BlurListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void blur(BlurEvent event) {

				
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				TextArea txtArea = new TextArea();
				if(null != txtFld)
				{
					txtArea.setValue(txtFld.getValue());
					txtArea.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				}
				txtFld.setValue(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				dialog.show(getUI().getCurrent(), null, true);
				
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
*/	
	
	public  void handleEnter(TextField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcut", ShortcutAction.KeyCode.F8, null) {
	      private static final long serialVersionUID = -2267576464623389044L;
	      @Override
	      public void handleAction( Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	    
	   // handleShortcut(searchField, enterShortCut);
	    handleShortcut(searchField, getShortCutListener(searchField));
	  }
	
	public  void handleShortcut(final TextField textField, final ShortcutListener shortcutListener) {
		//textField.addFocusListener(F);
		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focus(FocusEvent event) {
				//shortcutListener = getShortCutListener(textField);
				//textField.addShortcutListener(getShortCutListener(textField));
				textField.addShortcutListener(shortcutListener);
				
			}
		});
		
	   textField.addBlurListener(new BlurListener() {
		
		@Override
		public void blur(BlurEvent event) {
			
			/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}*/
			
			textField.removeShortcutListener(shortcutListener);
			/*Collection<?> listeners = textField.getListeners(ShortcutListener.class);
			for (Object object : listeners) {
				textField.removeListener(ShortcutListener.class, object);
			}*/
			
		}
	});
	  }
	
	public  void handleEnterForMedical(TextField searchField, final  Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcutForMedical", ShortcutAction.KeyCode.F7, null) {
	      private static final long serialVersionUID = -2267576464623389045L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        ((ShortcutListener) listener).handleAction(sender, target);
	      }
	    };
	   // handleShortcutForMedical(searchField, enterShortCut);
	    handleShortcutForMedical(searchField, getShortCutListenerForMedicalReason(searchField));
	    
	  }
	
	public  void handleShortcutForMedical(final TextField textField, final ShortcutListener shortcutListener) {
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
	
public void addDeductibleRemarksListener(final TextField txtFld){
		
		if(null != txtFld)
		{
			txtFld.addShortcutListener(new ShortcutListener(null,KeyCodes.KEY_F8,null) {
				
				@Override
				 public void handleAction(Object sender, Object target) {
					final TextField txtFld1 = (TextField)sender;
					txtFld1.getId();
					BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
					 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
					 TextField txtItemValue = (TextField) hashMap.get("deductibleOrNonPayableReason");
					
					 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
					 
					if (null != vLayout
							&& vLayout.getComponentCount() > 0) {
						vLayout.removeAllComponents();
					}
					
					TextArea txtArea = new TextArea();
					txtArea.setNullRepresentation("");
					txtArea.setMaxLength(300);
					
					txtArea.setValue(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
					//txtArea.setValue(txtFld.getValue());
					txtArea.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event) {
							TextArea txt = (TextArea)event.getProperty();
							txtFld1.setValue(txt.getValue());
							txtFld1.setDescription(txt.getValue());
							// TODO Auto-generated method stub
							
						}
					});
					
					billEntryDetailsDTO.setDeductibleOrNonPayableReason(txtArea.getValue());
				//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
					txtFld1.setDescription(txtArea.getValue());
					Button okBtn = new Button("OK");
					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
					vLayout.addComponent(txtArea);
					vLayout.addComponent(okBtn);
					vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
					
					
					
					final Window dialog = new Window();
					dialog.setCaption("");
					dialog.setClosable(false);
					dialog.setContent(vLayout);
					dialog.setResizable(false);
					dialog.setModal(true);
					//dialog.show(getUI().getCurrent(), null, true);
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
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
			});
//			txtFld.addTextChangeListener(new TextChangeListener() {
//				
//				@Override
//				public void textChange(TextChangeEvent event) {
//
//					BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
//					 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
//					 TextField txtItemValue = (TextField) hashMap.get("deductibleOrNonPayableReason");
//					
//					 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
//					 
//					if (null != vLayout
//							&& vLayout.getComponentCount() > 0) {
//						vLayout.removeAllComponents();
//					}
//					
//					TextArea txtArea = new TextArea();
//					txtArea.setNullRepresentation("");
//					txtArea.setMaxLength(200);
//					
//					txtArea.setValue(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
//					//txtArea.setValue(txtFld.getValue());
//					txtArea.addValueChangeListener(new ValueChangeListener() {
//						
//						@Override
//						public void valueChange(ValueChangeEvent event) {
//							TextArea txt = (TextArea)event.getProperty();
//							txtFld.setValue(txt.getValue());
//							txtFld.setDescription(txt.getValue());
//							// TODO Auto-generated method stub
//							
//						}
//					});
//					
//					billEntryDetailsDTO.setDeductibleOrNonPayableReason(txtFld.getValue());
//					txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
//					Button okBtn = new Button("OK");
//					okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//					vLayout.addComponent(txtArea);
//					vLayout.addComponent(okBtn);
//					vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
//					
//					
//					
//					final Window dialog = new Window();
//					dialog.setCaption("");
//					dialog.setClosable(false);
//					dialog.setContent(vLayout);
//					dialog.setResizable(false);
//					dialog.setModal(true);
//					//dialog.show(getUI().getCurrent(), null, true);
//					
//					if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
//						dialog.setPositionX(450);
//						dialog.setPositionY(500);
//					}
//					getUI().getCurrent().addWindow(dialog);
//					
//					okBtn.addClickListener(new ClickListener() {
//						private static final long serialVersionUID = 7396240433865727954L;
//
//						@Override
//						public void buttonClick(ClickEvent event) {
//							dialog.close();
//						}
//					});	
//				}
//			});
		}
	}


public void addMedicalReasonListener(final TextField txtFld){
	
	if(null != txtFld)
	{
		
		txtFld.addShortcutListener(new ShortcutListener(txtFld.getId(),KeyCodes.KEY_F5,null) {
			@Override
			 public void handleAction(Object sender, Object target) {

				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
				 
				
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				TextArea txtArea = new TextArea();
				txtArea.setMaxLength(300);
				txtArea.setNullRepresentation("");
				txtArea.setValue(billEntryDetailsDTO.getMedicalRemarks());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
					}
				});
				
				billEntryDetailsDTO.setMedicalRemarks(txtFld.getValue());
			//	txtFld.setDescription(billEntryDetailsDTO.getMedicalRemarks());
				txtFld.setDescription(txtArea.getValue());

				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
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
		});
//		txtFld.addTextChangeListener(new TextChangeListener() {
//			
//			@Override
//			public void textChange(TextChangeEvent event) {
//
//				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
//				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
//				 
//				
//				if (null != vLayout
//						&& vLayout.getComponentCount() > 0) {
//					vLayout.removeAllComponents();
//				}
//				
//				TextArea txtArea = new TextArea();
//				txtArea.setMaxLength(200);
//				txtArea.setNullRepresentation("");
//				txtArea.setValue(billEntryDetailsDTO.getMedicalRemarks());
//				txtArea.addValueChangeListener(new ValueChangeListener() {
//					
//					@Override
//					public void valueChange(ValueChangeEvent event) {
//						TextArea txt = (TextArea)event.getProperty();
//						txtFld.setValue(txt.getValue());
//						txtFld.setDescription(txt.getValue());
//					}
//				});
//				
//				billEntryDetailsDTO.setMedicalRemarks(txtFld.getValue());
//				txtFld.setDescription(billEntryDetailsDTO.getMedicalRemarks());
//				Button okBtn = new Button("OK");
//				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
//				vLayout.addComponent(txtArea);
//				vLayout.addComponent(okBtn);
//				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
//				final Window dialog = new Window();
//				dialog.setCaption("");
//				dialog.setClosable(false);
//				dialog.setContent(vLayout);
//				dialog.setResizable(false);
//				dialog.setModal(true);
//				//dialog.show(getUI().getCurrent(), null, true);
//				
//				if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
//					dialog.setPositionX(450);
//					dialog.setPositionY(500);
//				}
//				
//				getUI().getCurrent().addWindow(dialog);
//				
//				okBtn.addClickListener(new ClickListener() {
//					private static final long serialVersionUID = 7396240433865727954L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//						dialog.close();
//					}
//				});	
//			}
//		});
	}
}



/*public void addDeductibleRemarksListener(final TextField txtFld){
	
	if(null != txtFld)
	{
		
		txtFld.addFocusListener(new FocusListener() {
			@Override
			public void focus(com.vaadin.event.FieldEvents.FocusEvent event) {


				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
				 TextField txtItemValue = (TextField) hashMap.get("deductibleOrNonPayableReason");
				
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				 final String val1;
				final TextArea txtArea = new TextArea();
				//txtArea.setValue(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						
						// TODO Auto-generated method stub
						
					}
				});
				
				billEntryDetailsDTO.setDeductibleOrNonPayableReason(txtFld.getValue());
				txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());

				
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				final ConfirmDialog dialog = new ConfirmDialog();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//showInPopUp(dialog, textAreaValue, TextFieldValue);
				dialog.show(getUI().getCurrent(), null, true);
				
				//txtArea.setValue(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						
						showInPopUp(dialog, txt.getValue(), txtFld.getValue());
						txtFld.setValue(txt.getValue());
						//txtFld.removeFocusListener(event);
						// TODO Auto-generated method stub
						
					}
				});
				
				//if((!("").equalsIgnoreCase(val1)) && !val.equalsIgnoreCase(val1))
			
				
				okBtn.addClickListener(new ClickListener() {
					private static final long serialVersionUID = 7396240433865727954L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});		
			}

			@Override
			public void focus(com.vaadin.event.FieldEvents.FocusEvent event) {
				// TODO Auto-generated method stub
				
			}

		});
	}
}
*/
			private void showInPopUp(ConfirmDialog dialog , String textAreaValue, String TextFieldValue)
			{
				dialog.close();
				if(!(textAreaValue.equalsIgnoreCase(TextFieldValue)))
				{
					
					dialog.show(getUI().getCurrent(), null, true);
				}
			}
		 
	 	/* public BlurListener getTotalDisallowancesListener() {
				
				BlurListener listener = new BlurListener() {
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField component = (TextField) event.getComponent();
						summationOfTotalDisallowances();
						calculateNetPayableAmount(component);
					}
				};
				return listener;
				
			}*/
	 	 
	 	 
	 	 private void calculateNetPayableAmount(Component component)
	 	 {
	 		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
			 TextField txtField = (TextField)component;
			 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 TextField txtItemValue = (TextField) hashMap.get("itemValue");
			 TextField txtTotalDisallowances = (TextField) hashMap.get("totalDisallowances");
			 TextField txtAmount = (TextField) hashMap.get("netPayableAmount");
			 TextField daysAllowed =(TextField) hashMap.get("noOfDaysAllowed");
			 
			 TextField txtReasonableDeduction = (TextField) hashMap.get("reasonableDeduction");
			 
			 
			 //Added for room rent nursing charges and ICU nursing charges.
			 
			 ComboBox cmbCategory = (ComboBox) hashMap.get("category");
			 
			
			 
			 //Added for amount 
			 
			 Integer totalDisallowances = 0;
			 Integer itemValue = 0;
			 
			 
			 if(null != txtTotalDisallowances && null != txtTotalDisallowances.getValue() && !("").equalsIgnoreCase(txtTotalDisallowances.getValue()))
			 {
				 totalDisallowances =  SHAUtils.getIntegerFromStringWithComma(txtTotalDisallowances.getValue());
				 //txtAmount.setEnabled(false);
				 //txtDummyFld.setEnabled(false);
			 }
			 
			 if(null != txtItemValue && null != txtItemValue.getValue() && !("").equalsIgnoreCase(txtItemValue.getValue()))
			 {
				 itemValue = SHAUtils.getIntegerFromStringWithComma(txtItemValue.getValue());
				
			 }
			 
			 if(null != cmbCategory)
			 {
				 SelectValue selValue = (SelectValue) cmbCategory.getValue();
				 if(null != selValue && null != selValue.getValue() && 
						 (("Nursing Charges").equalsIgnoreCase(selValue.getValue().trim()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue().trim()))
						 )
				 {
					 if(null != txtAmount)
					 {
						 txtAmount.setValue(String.valueOf(itemValue));
						 
					 }
					 if(null != txtReasonableDeduction)
					 {
						 txtReasonableDeduction.setValue(null);
					 }
					 if(null != txtTotalDisallowances)
					 {
						txtTotalDisallowances.setValue(null);
					 }
					 
				 }
				 else
				 {
					 Integer totalValue = 0;
					 
					 if(totalDisallowances > itemValue)
					 {
						 totalValue =  totalDisallowances - itemValue ;
					 }
					 else
					 {
						 totalValue = itemValue -  totalDisallowances ;
					 }
				
					 if(null != txtAmount){
					 txtAmount.setValue(String.valueOf(totalValue));
					 if(selValue != null && selValue.getValue() != null && 
							 (("Room Rent").equalsIgnoreCase(selValue.getValue().trim())) || selValue != null && selValue.getValue() != null &&  (("ICU Room Rent").equalsIgnoreCase(selValue.getValue().trim()))
							 || selValue != null && selValue.getValue() != null && (("ICCU Room Rent").equalsIgnoreCase(selValue.getValue().trim())))
							 {
						 if(daysAllowed != null){
							 if(daysAllowed.getValue() == null){
								 txtAmount.setValue("0");
							 }
						 }else{
							 txtAmount.setValue("0");
						 }
					 }
					
					 }
				 }
			 }
	 	 }
	
	 	public void summationOfNetPayableAmount()
		 {
			 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
			 Double total  = 0d;
			 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
			 {
				 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
					 if(null != billEntryDetailsDTO.getNetPayableAmount())
					 {
						 
						 /**
						  * 
						  * The below code is commented for issue 614.
						  * 
						  * Discount and deductibles will be subtracted from the total
						  * net amount
						  * */
						 //if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null && !(billEntryDetailsDTO.getCategory().getValue().toString().contains("Discount") || billEntryDetailsDTO.getCategory().getValue().toString().contains("Deductibles")) )) {
						 //if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null )) {
							 if (null != billEntryDetailsDTO
									 && null != billEntryDetailsDTO.getCategory() 
									 && null != billEntryDetailsDTO.getCategory().getValue() 
									  
									 &&  (billEntryDetailsDTO.getCategory().getValue().contains("Discount") 
									 || billEntryDetailsDTO.getCategory().getValue().contains("Deductibles")))
							 {
								 total -= billEntryDetailsDTO.getNetPayableAmount();
							 }
							 else
							 {
								 total += billEntryDetailsDTO.getNetPayableAmount();
							 }
						// }
						 /*else 
						 {
							
						 }*/
						
					 }
			}
			 }
				 else
				 {
					 total = 0d;
				 }
				 table.setColumnFooter("netPayableAmount", String.valueOf(total));
				 totalNetPayableAmt = total;
			 
			// totalBillValue = total;
			 //}
		 }
	 	 
	 
	 public void calculateTotal()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getItemValue())
				 {
					 /**
					  * 
					  * The below code is commented for issue 614.
					  * 
					  * Discount and deductibles will be subtracted from the total
					  * net amount
					  * */
					 //if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null && !(billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("discount") || billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("deductibles")) )) {
						 /*if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null )) {
							 if(billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("discount") || billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("deductibles"))*/
					 if (null != billEntryDetailsDTO
							 && null != billEntryDetailsDTO.getCategory() 
							 && null != billEntryDetailsDTO.getCategory().getValue() 
							  
							 &&  (billEntryDetailsDTO.getCategory().getValue().contains("Discount") 
							 || billEntryDetailsDTO.getCategory().getValue().contains("Deductibles")))
							 {
								 total -= billEntryDetailsDTO.getItemValue();
							 }
					 else
					 {
						 total += billEntryDetailsDTO.getItemValue();
					 }
					 }
						 /*else 
						 {
							 
						 }*/
					 
				// }
			}
		 } 
		 else
		 {
			 total = 0d;
		 }
			 table.setColumnFooter("itemValue", String.valueOf(total));
			 totalBillValue = total;
	 }
	 
	 public void calculateAllowableAmountTotal()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getAmountAllowableAmount())
				 {
					 total += billEntryDetailsDTO.getAmountAllowableAmount();
				 }
		}
		 
		 //totalBillValue = total;
		 }
		 else
		 {
			total = 0d;
		 }
		 table.setColumnFooter("amountAllowableAmount", String.valueOf(total));
	 }

	 private void calculateNonPayableAmount(Component component)
	 {
		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		 TextField txtField = (TextField)component;
		 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
		 if(null != hashMap)
		 {
		 TextField txtTotalAmount = (TextField) hashMap.get("totalAmount");
		 TextField txtEntitledAmount = (TextField) hashMap.get("amountEntitledAmount");
		 TextField txtNonPayables = (TextField) hashMap.get("amountEntitledNonPayables");
		 TextField txtNetAmt = (TextField) hashMap.get("amountEntitledNetAmt");
		 //Added for amount 
		 
		 Integer totalAmount = 0;
		 Integer entitledAmount = 0;
		 
		 if(null != txtTotalAmount && null != txtTotalAmount.getValue() && !("").equalsIgnoreCase(txtTotalAmount.getValue()))
		 {
			 totalAmount =  SHAUtils.getIntegerFromStringWithComma(txtTotalAmount.getValue());
			 //txtDummyFld.setEnabled(false);
		 }
		 
		 if(null != txtEntitledAmount && null != txtEntitledAmount.getValue() && !("").equalsIgnoreCase(txtEntitledAmount.getValue()))
		 {
			 entitledAmount = SHAUtils.getIntegerFromStringWithComma(txtEntitledAmount.getValue());
		 }
		 
		 txtNonPayables.setValue(String.valueOf(totalAmount - entitledAmount));
		 
		 Integer nonPayable = 0;
		 
		 if(null != txtNonPayables && null != txtNonPayables.getValue() && !("").equalsIgnoreCase(txtNonPayables.getValue()))
		 {
			 nonPayable = SHAUtils.getIntegerFromStringWithComma(txtNonPayables.getValue());
		 }
		 
		 txtNetAmt.setValue(String.valueOf(nonPayable - entitledAmount));
		 }
	 }
	 
	 private void calculateItemValue(Component component/*,String type*/)
	 {
		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		 TextField txtField = (TextField)component;
		 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
		 
		/* if(("AmountClaimed").equalsIgnoreCase(type))
		 {*/
			 TextField txtPerDayAmt = (TextField) hashMap.get("perDayAmt");
			 TextField txtItemValue = (TextField) hashMap.get("itemValue");
			 TextField txtNoOfDays = (TextField) hashMap.get("noOfDays");
			 TextField txtAllowableNoOfDays = (TextField) hashMap.get("noOfDaysAllowed");
			 TextField txtAmount = (TextField) hashMap.get("amountAllowableAmount");
		
			 //Added for amount 
			 
			/* Integer perDayAmt = 0;
			 Integer noOfDaysVal = 0;*/
			 Double perDayAmt = 0d;
			 Double noOfDaysVal = 0d;
			 
			 
			 if(null != txtAllowableNoOfDays)
			 {	 
				 Integer noOfAllowableDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtAllowableNoOfDays.getValue());
				 Integer noOfDays = SHAUtils.getIntegerFromStringWithComma(txtNoOfDays.getValue());
				 if(noOfAllowableDaysVal > noOfDays)
				 {
					 
					 	Label label = new Label("No of allowable days is greater than the number of days claimed", ContentMode.HTML);
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
					/* HorizontalLayout layout = new HorizontalLayout(
								new Label(""));
						layout.setMargin(true);
						final ConfirmDialog dialog = new ConfirmDialog();
						dialog.setCaption("Errors");
						dialog.setWidth("35%");
						dialog.setClosable(true);
						dialog.setContent(layout);
						dialog.setResizable(false);
						dialog.setModal(true);
						dialog.show(getUI().getCurrent(), null, true);*/
						txtAllowableNoOfDays.setValue(null);
						if(null != txtAmount)
							txtAmount.setEnabled(false);
				 }
			 }
			 
			 
			 if(null != txtNoOfDays && null != txtNoOfDays.getValue() && !("").equalsIgnoreCase(txtNoOfDays.getValue()))
			 {
				/* noOfDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtNoOfDays.getValue());*/
				 noOfDaysVal = SHAUtils.getDoubleFromStringWithComma(txtNoOfDays.getValue());
				 if(null != txtItemValue)
				 {
					 txtItemValue.setEnabled(false);
					txtItemValue.setValue(String.valueOf(noOfDaysVal*perDayAmt));
				 }
				 //txtDummyFld.setEnabled(false);
			 }
			/* else
			 {
				 txtItemValue.setEnabled(true);
				// txtDummyFld.setEnabled(true);
			 }*/
			 
			 if(null != txtPerDayAmt && null != txtPerDayAmt.getValue() && !("").equalsIgnoreCase(txtPerDayAmt.getValue()))
			 {
			//	 perDayAmt = SHAUtils.getIntegerFromStringWithComma(txtPerDayAmt.getValue());
				 perDayAmt = SHAUtils.getDoubleFromStringWithComma(txtPerDayAmt.getValue());
				 if(null != txtItemValue)
				 {
					 txtItemValue.setEnabled(false); 
					txtItemValue.setValue(String.valueOf(noOfDaysVal*perDayAmt)); 
				 }
			 }
			 
			 if(null != billEntryDetailsDTO.getCategory() && (("Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) || 
					 ("ICU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) ||   ("ICU Nursing Charges").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue()) ||
					 ("Nursing Charges").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())
							 ||("ICCU Room Rent").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())
							 || ("ICCU Nursing Charges").equalsIgnoreCase(billEntryDetailsDTO.getCategory().getValue())))
					 
			 {
			 
			 Double itemValue = noOfDaysVal * perDayAmt;
				
			 long lValue = (long) itemValue.longValue();
			 Double decimalVal = itemValue - lValue;
			 if(decimalVal> 0.5)
			 {
				 itemValue = Double.valueOf(String.valueOf(lValue+1));
				 
			 }
			 else
			 {
				 itemValue = Double.valueOf(String.valueOf(lValue));
			 }
			 
			 //txtItemValue.setValue(String.valueOf(noOfDaysVal*perDayAmt));
			 if(null != txtItemValue)
			 txtItemValue.setValue(String.valueOf(itemValue));
			 }
			 /*else
			 {
				 if(null != txtItemValue)
					 txtItemValue.setValue(String.valueOf(itemValue));
			 }*/
			 
			/* else
			 {
				 txtItemValue.setEnabled(true);
			 }*/
			/* if(null != txtItemValue)
			 txtItemValue.setValue(String.valueOf(noOfDaysVal*perDayAmt));*/
		 //}
	 }
	 
	 private void populateProductPerDayAmt(Component component)
	 {
		 BillEntryDetailsDTO billEntryDTO = new BillEntryDetailsDTO();
		 GComboBox cmb = (GComboBox) component;
		 billEntryDTO = (BillEntryDetailsDTO) cmb.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDTO);
		
		 if(null != hashMap && !hashMap.isEmpty())
		 {
			 GComboBox cmbCategory = (GComboBox) hashMap.get("category");
			 TextField noOfDaysAllowed = (TextField)hashMap.get("noOfDaysAllowed");
			 TextField pdtPerDayAmt = (TextField)hashMap.get("perDayAmtProductBased");
			 TextField txtItemValue = (TextField) hashMap.get("itemValue");
			 
			 TextField noOfDays = (TextField) hashMap.get("noOfDays");
			 TextField perDayAmt = (TextField)hashMap.get("perDayAmt");
			 TextField nonPayable = (TextField)hashMap.get("nonPayable");
			 TextField reasonableDeduction = (TextField)hashMap.get("reasonableDeduction");
			 TextField totalDisallowance = (TextField) hashMap.get("totalDisallowances");
			 TextField netPayableAmt = (TextField)hashMap.get("netPayableAmt");
			 
			// if(null != cmbCategory && (("ICU Rooms").equalsIgnoreCase(((SelectValue)(cmbCategory.getPropertyDataSource())).getValue()) || ("Room Rent").equalsIgnoreCase(((SelectValue)(cmbCategory.getPropertyDataSource())).getValue())))
			 if(null != cmbCategory)
			 {
				 SelectValue selValue = (SelectValue) cmbCategory.getValue();
				{
					if(null != selValue && ("ICU Room Rent").equalsIgnoreCase(selValue.getValue())  && null != pdtPerDayAmt && null != noOfDaysAllowed)
					{
						noOfDaysAllowed.setEnabled(true);
						pdtPerDayAmt.setEnabled(true);
						pdtPerDayAmt.setValue(String.valueOf(this.pdtICURentAmt));
					}
					if(null != selValue && ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue())  && null != pdtPerDayAmt && null != noOfDaysAllowed)
					{
						noOfDaysAllowed.setEnabled(true);
						pdtPerDayAmt.setEnabled(true);
						pdtPerDayAmt.setValue(String.valueOf(this.pdtICCURentAmt));
					}
					if(null != selValue && ("Room Rent").equalsIgnoreCase(selValue.getValue()) && null != pdtPerDayAmt && null != noOfDaysAllowed)
					{
						noOfDaysAllowed.setEnabled(true);
						pdtPerDayAmt.setEnabled(true);
						pdtPerDayAmt.setValue(String.valueOf(this.pdtPerDayAmt));
					}
					else
					{
						pdtPerDayAmt.setValue(null);
						pdtPerDayAmt.setEnabled(false);
						noOfDaysAllowed.setEnabled(false);
						noOfDaysAllowed.setValue(null);
						if(txtItemValue != null){
							txtItemValue.setValue(null);
						}
					}
					
					if(null != selValue && ((("deductibles").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim())) || (("deductibles(80%)").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim())) || ("Hospital Discount").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim())))
					{
						if(null != noOfDays)
						{
							noOfDays.setValue(null);
							noOfDays.setEnabled(false);
						}
						if(null != perDayAmt)
						{
							perDayAmt.setValue(null);
							perDayAmt.setEnabled(false);
						}
						if(null != nonPayable)
						{
							nonPayable.setValue(null);
							nonPayable.setEnabled(false);
						}
						if(("Hospital Discount").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim()))
								{
									if(null != reasonableDeduction)
									{
										reasonableDeduction.setValue(null);
										reasonableDeduction.setEnabled(false);
									}
								}
						else if((("deductibles").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim())) || (("deductibles(80%)").equalsIgnoreCase(selValue.getValue().toLowerCase().toString().trim())))
						{
							if(null != reasonableDeduction)
							{
								reasonableDeduction.setValue(null);
								reasonableDeduction.setEnabled(true);
							}
						}
						if(null != totalDisallowance)
						{
							totalDisallowance.setValue(null);
							totalDisallowance.setEnabled(false);
						}
						if(null != netPayableAmt)
						{
							netPayableAmt.setValue(null);
							netPayableAmt.setEnabled(false);
						}
						
						
					}else{
						
						if(null != selValue && ("ICU Room Rent").equalsIgnoreCase(selValue.getValue())  && null != pdtPerDayAmt && null != noOfDaysAllowed)
						{
							noOfDaysAllowed.setEnabled(true);
							pdtPerDayAmt.setEnabled(true);
							if(null != noOfDays){
								noOfDays.setEnabled(true);
							}
							if(null != perDayAmt)
							{
								perDayAmt.setEnabled(true);
							}
							
							if(null != nonPayable)
							{
								nonPayable.setEnabled(true);
							}
							
							pdtPerDayAmt.setValue(String.valueOf(this.pdtICURentAmt));
						}
						if(null != selValue && ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue())  && null != pdtPerDayAmt && null != noOfDaysAllowed)
						{
							noOfDaysAllowed.setEnabled(true);
							pdtPerDayAmt.setEnabled(true);
							if(null != noOfDays){
								noOfDays.setEnabled(true);
							}
							if(null != perDayAmt)
							{
								perDayAmt.setEnabled(true);
							}
							
							if(null != nonPayable)
							{
								nonPayable.setEnabled(true);
							}
							
							pdtPerDayAmt.setValue(String.valueOf(this.pdtICCURentAmt));
						}
						if(null != selValue && ("Room Rent").equalsIgnoreCase(selValue.getValue()) && null != pdtPerDayAmt && null != noOfDaysAllowed)
						{
							noOfDaysAllowed.setEnabled(true);
							
							if(null != noOfDays){
								noOfDays.setEnabled(true);
							}
							if(null != perDayAmt)
							{
								perDayAmt.setEnabled(true);
							}
							
							if(null != nonPayable)
							{
								nonPayable.setEnabled(true);
							}
							pdtPerDayAmt.setEnabled(true);
							pdtPerDayAmt.setValue(String.valueOf(this.pdtPerDayAmt));
						}
						else
						{
							pdtPerDayAmt.setValue(null);
							pdtPerDayAmt.setEnabled(false);
							noOfDaysAllowed.setEnabled(false);
							noOfDaysAllowed.setValue(null);
							if(txtItemValue != null){
								txtItemValue.setValue(null);
							}
						}
						
					}
				}
			 }
		 }
		 
	 }
	 
	 private void populateValuesBasedOnCategoryValues(final ComboBox component)
	 {

			// TODO Auto-generated method stub
			
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			ComboBox comboBox = (ComboBox) hashMap.get("category");
			if (null != billEntryDetailsDTO) {
				if(null != billEntryDetailsDTO.getCategory()) {
					if(null != comboBox && null != comboBox.getValue()) {
						
						SelectValue selValue = (SelectValue)comboBox.getValue();
						HashMap<String, AbstractField<?>> valHashMap = tableItem.get(billEntryDetailsDTO);
						TextField noOfDays = (TextField) valHashMap.get("noOfDays");
						TextField perDayAmt = (TextField) valHashMap.get("perDayAmt");
						TextField itemValue = (TextField) valHashMap.get("itemValue");
						
						TextField noOfDaysAllowed = (TextField) valHashMap.get("noOfDaysAllowed");
						TextField perDayAmtPdtBased = (TextField) valHashMap.get("perDayAmtProductBased");
						TextField amountAllowableAmt = (TextField) valHashMap.get("amountAllowableAmount");
						TextField nonPayable = (TextField) valHashMap.get("nonPayable");
						TextField totalDisallowance = (TextField) valHashMap.get("totalDisallowances");
						TextField reasonableDeduction = (TextField) valHashMap.get("reasonableDeduction");
						//TextField amountAllowableAmt = (TextField) valHashMap.get("amountAllowableAmount");
						
						
						//if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue()))
						/*if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
								
								|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue())
								)*/
							if(("ICU Room Rent").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
									|| ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue()))
									
								//	|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue()) )
						{
							if(null != noOfDays)
							{
								noOfDays.setEnabled(true);
							}
							if (null != perDayAmt)
							{
								perDayAmt.setEnabled(true);
							}
							if(null != noOfDaysAllowed)
							{
								noOfDaysAllowed.setEnabled(true);
							}
							if(null != perDayAmtPdtBased)
							{
								//perDayAmtPdtBased.setEnabled(true);
								perDayAmtPdtBased.setEnabled(false);
								if(("ICU Room Rent").equalsIgnoreCase(selValue.getValue()))
									perDayAmtPdtBased.setValue((String.valueOf(this.pdtICURentAmt)));
								if(("ICCU Room Rent").equalsIgnoreCase(selValue.getValue()))
									perDayAmtPdtBased.setValue((String.valueOf(this.pdtICCURentAmt)));
								if(("Room Rent").equalsIgnoreCase(selValue.getValue()))
										perDayAmtPdtBased.setValue((String.valueOf(this.pdtPerDayAmt)));
							}
							if(null != itemValue)
							{
								//itemValue.setValue(null);
								itemValue.setEnabled(false);
							}
							if(null != nonPayable)
							{
								nonPayable.setValue(null);
								nonPayable.setEnabled(false);
							}
							
						}
							else if (("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue()))
							{
								if(null != noOfDaysAllowed)
								{
									noOfDaysAllowed.setEnabled(false);
								}
								if(null != perDayAmtPdtBased)
								{
									perDayAmtPdtBased.setEnabled(false);
								}
								if(null != itemValue)
								{
									itemValue.setEnabled(false);
								}
								if(null != nonPayable)
								{
									nonPayable.setValue(null);
									nonPayable.setEnabled(false);
								}
							}
							
							else if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim())
									|| ("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
							{
								if(("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()) && null != itemValue)
								{
									itemValue.setEnabled(true);
									if(null != totalDisallowance)
									{
										totalDisallowance.setEnabled(false);
									}
									if(null != reasonableDeduction)
									{
										reasonableDeduction.setEnabled(false);
									}
								}
								else if(null != itemValue)
								{
									itemValue.setValue(null);
									itemValue.setEnabled(true);
								}
								if(null != noOfDays)
								{
									noOfDays.setValue(null);
									noOfDays.setEnabled(false);
								}
								if (null != perDayAmt)
								{
									perDayAmt.setEnabled(false);
								}
								if(null != noOfDaysAllowed)
								{
									noOfDaysAllowed.setEnabled(false);
								}
								
								if(null != amountAllowableAmt)
								{
									/*if(null != amountAllowableAmt.getValue())
									{
										amountAllowableAmt.setValue(null);
									}*/
									amountAllowableAmt.setEnabled(false);
								}
								if(null != nonPayable)
								{
									nonPayable.setEnabled(false);
								}
								
								if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim()))
								{
									if(!reasonableDeduction.isEnabled())
									{
										reasonableDeduction.setEnabled(true);
									}
									/*if(null != itemValue)
									{
										itemValue.setValue(null);
										itemValue.setEnabled(false);
									}*/
								}
							}
							/*else if (("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
							{
								if(null != itemValue)
								{
									itemValue.setEnabled(true);
								}
								
							}*/
						else
						{
							if(null != noOfDays)
							{
								/*if(!(null != billEntryDetailsDTO.getItemValue() && (billEntryDetailsDTO.getItemValue() > 0)))
								{*/
									noOfDays.setValue(null);
								/*}*/
								noOfDays.setEnabled(false);
								
							}
							if(null != perDayAmt)
							{
								/*if(!(null != billEntryDetailsDTO.getItemValue() && (billEntryDetailsDTO.getItemValue() > 0)))
								{*/
									perDayAmt.setValue(null);
								/*}*/
								perDayAmt.setEnabled(false);
							}
							if(null != itemValue)
							{
								//itemValue.setValue(null);
								itemValue.setEnabled(true);
							}
							if(null != noOfDaysAllowed)
							{
								noOfDaysAllowed.setValue(null);
								noOfDaysAllowed.setEnabled(false);
							}
							if(null != perDayAmtPdtBased)
							{
								perDayAmtPdtBased.setValue(null);
								perDayAmtPdtBased.setEnabled(false);
							}
							if(null != amountAllowableAmt)
							{
								amountAllowableAmt.setValue(null);
							}
							if(null != nonPayable)
							{
								nonPayable.setEnabled(true);
							//	nonPayable.setValue(null);
							}
							if(null != reasonableDeduction)
							{
								//reasonableDeduction.setValue(null);
								reasonableDeduction.setEnabled(true);
								
									//reasonableDeduction.setValue(null);
							}
							if(null != totalDisallowance)
							{
								totalDisallowance.setValue(null);
								//totalDisallowance.setEnabled(true);
								
									//reasonableDeduction.setValue(null);
							}
						}
					}
				}					
			}
			
		
	 }
	 
	 
	 private void calculateAllowableAmountValue(Component component)
	 {
		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		 TextField txtField = (TextField)component;
		 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
		 TextField txtProductPerDayAmt = (TextField) hashMap.get("perDayAmtProductBased");
		 TextField txtAllowableNoOfDays = (TextField) hashMap.get("noOfDaysAllowed");
		 TextField txtAmount = (TextField) hashMap.get("amountAllowableAmount");
		 TextField txtNoOfDays = (TextField) hashMap.get("noOfDays");
		 TextField txtClaimedPerDayAmt = (TextField) hashMap.get("perDayAmt");
		 
		 
		 
		 if(null != txtNoOfDays && !("").equalsIgnoreCase(txtNoOfDays.getValue()))
		 {
			 //Integer noOfDaysAllowed =  SHAUtils.getIntegerFromStringWithComma(txtAllowableNoOfDays.getValue());
			 //Integer noOfDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtAllowableNoOfDays.getValue());
			 Double noOfDaysVal = SHAUtils.getDoubleValueFromString(txtAllowableNoOfDays.getValue());
			 Double noOfDays = SHAUtils.getDoubleValueFromString(txtNoOfDays.getValue());
			// Integer noOfDays = SHAUtils.getIntegerFromStringWithComma(txtNoOfDays.getValue());
			 if(noOfDaysVal > noOfDays)
			 {
				 
				 	Label label = new Label("No of allowable days is greater than the number of days claimed", ContentMode.HTML);
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
					
					if(getUI().getCurrent().getPage().getWebBrowser().isIE()){// && ((bean.getFileName() != null && bean.getFileName().endsWith(".PDF")) || (bean.getFileName() != null && bean.getFileName().endsWith(".pdf")))) {
						dialog.setPositionX(550);
						dialog.setPositionY(850);
						//dialog.setDraggable(true);
					}
					
					dialog.show(getUI().getCurrent(), null, true);
				/* HorizontalLayout layout = new HorizontalLayout(
							new Label(""));
					layout.setMargin(true);
					final ConfirmDialog dialog = new ConfirmDialog();
					dialog.setCaption("Errors");
					dialog.setWidth("35%");
					dialog.setClosable(true);
					dialog.setContent(layout);
					dialog.setResizable(false);
					dialog.setModal(true);
					dialog.show(getUI().getCurrent(), null, true);*/
					txtAllowableNoOfDays.setValue(null);
					txtAmount.setEnabled(false);
			 }
			 else
			 {
				 //Integer perDayAmt = 0;
				 Double perDayAmt = 0d;
				/* Integer noOfDaysVal = 0;*/
				 
				 if(null != txtAllowableNoOfDays && null != txtAllowableNoOfDays.getValue() && !("").equalsIgnoreCase(txtAllowableNoOfDays.getValue()))
				 {
					// noOfDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtAllowableNoOfDays.getValue());
					 txtAmount.setEnabled(false);
					 //txtDummyFld.setEnabled(false);
				 }
				 else
				 {
					// txtAmount.setEnabled(true);
					// txtDummyFld.setEnabled(true);
				 }
				 
				 if(null != txtProductPerDayAmt && null != txtProductPerDayAmt.getValue() && !("").equalsIgnoreCase(txtProductPerDayAmt.getValue()))
				 {
					 if(("0").equalsIgnoreCase(txtProductPerDayAmt.getValue()))
					 {
						 if(null != txtClaimedPerDayAmt && null != txtClaimedPerDayAmt.getValue() && !("").equalsIgnoreCase(txtClaimedPerDayAmt.getValue()))
						 {
							// perDayAmt = SHAUtils.getIntegerFromStringWithComma(txtClaimedPerDayAmt.getValue());
							 perDayAmt = SHAUtils.getDoubleFromStringWithComma(txtClaimedPerDayAmt .getValue());
						 }
					 }
					 else
					 {
						 //perDayAmt = SHAUtils.getIntegerFromStringWithComma(txtProductPerDayAmt.getValue());
						 perDayAmt = SHAUtils.getDoubleFromStringWithComma(txtProductPerDayAmt .getValue());
					 }
					 
					 txtAmount.setEnabled(false);
				 }
				 else
				 {
					// txtAmount.setEnabled(true);
				 }
				 
				 Double itemValue = noOfDaysVal*perDayAmt;
				 /**
				  * Math.ceil() wasn't accurate for few datas.
				  * Hence opted for the  below manual calculation.
				  * For datas like 2.5 * 998.1 , math.ceil wasn't accurate.
				  * */
				 long lValue = (long) itemValue.longValue();
				 Double decimalVal = itemValue - lValue;
				 if(decimalVal> 0.5)
				 {
					 itemValue = Double.valueOf(String.valueOf(lValue+1));
					 
				 }
				 else
				 {
					 itemValue = Double.valueOf(String.valueOf(lValue));
				 }
				 
				 //txtItemValue.setValue(String.valueOf(noOfDaysVal*perDayAmt));
				 if(null != txtAmount && null != itemValue )
					 txtAmount.setValue(String.valueOf(itemValue));
				 
				 //txtAmount.setValue(String.valueOf(noOfDaysVal*perDayAmt));
			 }
				 
		 }
		 
		 //Added for amount 
		 
		/* Integer perDayAmt = 0;
		 Integer noOfDaysVal = 0;
		 
		 if(null != txtAllowableNoOfDays && null != txtAllowableNoOfDays.getValue() && !("").equalsIgnoreCase(txtAllowableNoOfDays.getValue()))
		 {
			 noOfDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtAllowableNoOfDays.getValue());
			 txtAmount.setEnabled(false);
			 //txtDummyFld.setEnabled(false);
		 }
		 else
		 {
			 txtAmount.setEnabled(true);
			// txtDummyFld.setEnabled(true);
		 }
		 
		 if(null != txtProductPerDayAmt && null != txtProductPerDayAmt.getValue() && !("").equalsIgnoreCase(txtProductPerDayAmt.getValue()))
		 {
			 perDayAmt = SHAUtils.getIntegerFromStringWithComma(txtProductPerDayAmt.getValue());
			 txtAmount.setEnabled(false);
		 }
		 else
		 {
			 txtAmount.setEnabled(true);
		 }
		 
		 txtAmount.setValue(String.valueOf(noOfDaysVal*perDayAmt));*/
		 
	 }
	 
	 
	 private void calculateTotalDisallowances(Component component)
	 {
		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		 TextField txtField = (TextField)component;
		 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
		 TextField txtNonPayablePdtBased = (TextField) hashMap.get("nonPayableProductBased");
		 TextField txtNonPayable = (TextField) hashMap.get("nonPayable");
		 TextField txtReasonableDeduction = (TextField) hashMap.get("reasonableDeduction");
		 TextField txtAmount = (TextField) hashMap.get("totalDisallowances");
		 TextField txtClaimedAmt = (TextField) hashMap.get("itemValue");
		 //Added for amount 
		 
		 Integer nonPayableProductBased = 0;
		 Integer nonPayableValue = 0;
		 Integer reasonableDeduction = 0;
		 
		 if(null != txtNonPayable && null != txtNonPayable.getValue() && !("").equalsIgnoreCase(txtNonPayable.getValue()))
		 {
			 nonPayableValue =  SHAUtils.getIntegerFromStringWithComma(txtNonPayable.getValue());
			 //txtAmount.setEnabled(false);
			 //txtDummyFld.setEnabled(false);
		 }
		 
		 if(null != txtNonPayablePdtBased && null != txtNonPayablePdtBased.getValue() && !("").equalsIgnoreCase(txtNonPayablePdtBased.getValue()))
		 {
			 nonPayableProductBased = SHAUtils.getIntegerFromStringWithComma(txtNonPayablePdtBased.getValue());
			
		 }
		 
		 if(null != txtReasonableDeduction && null != txtReasonableDeduction.getValue() && !("").equalsIgnoreCase(txtReasonableDeduction.getValue()))
		 {
			 reasonableDeduction = SHAUtils.getIntegerFromStringWithComma(txtReasonableDeduction.getValue());
		 }
		
		 Integer totalValue = nonPayableProductBased + nonPayableValue + reasonableDeduction;
		 if(null != txtAmount)
		 {
			 txtAmount.setValue(String.valueOf(totalValue));
		 }
		 Integer claimedAmt = 0;
		 if(null != txtClaimedAmt)
		 {
			 claimedAmt = SHAUtils.getIntegerFromStringWithComma(txtClaimedAmt.getValue());
		 }
		/* if(totalValue > claimedAmt)
		 {
			 	Label label = new Label("Total Disallowances should not exceed claimed amount value", ContentMode.HTML);
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
				if(null != txtAmount)
				{
					//txtAmount.setValue(null);
					//txtNonPayable.setValue(null);
				}
		 }*/
		 
	 }
	 
	 
	 private void summationOfTotalDisallowances()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getTotalDisallowances())
				 {
					 total += billEntryDetailsDTO.getTotalDisallowances();
				 }
		}
		 //totalBillValue = total;
		 }
		 else
		 {
			 total = 0d;
		 }
		 table.setColumnFooter("totalDisallowances", String.valueOf(total));
	 }
	 
 
	 private void enableOrDisableFields(TextField txtField,String type)
	 {
		 Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		 
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap)
			 {
				 ComboBox cmbBox = (ComboBox)hashMap.get("category");
				 SelectValue selValue = null;
				 if(null != cmbBox )
				 {
					 selValue = (SelectValue)cmbBox.getValue();
				 }
				 if(("itemValue").equalsIgnoreCase(type))
				 {
					 TextField totalItemValueFld = (TextField)hashMap.get("itemValue");
					 
					 if(null != billEntryDetailsDTO.getNoOfDays() && null != billEntryDetailsDTO.getPerDayAmt() && null != billEntryDetailsDTO.getItemValue())
					 {
						 totalItemValueFld.setEnabled(false);
					 }
					 else
					 {
						 totalItemValueFld.setEnabled(true);
					 }
					 if(null != selValue)
					 {
					 if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim()))
							//	|| ("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
					 {
						 totalItemValueFld.setEnabled(false);
					 }
					 }
				 }
				 else if(("amountAllowableAmount").equalsIgnoreCase(type))
				 {
					 TextField totalItemValueFld = (TextField)hashMap.get("amountAllowableAmount");
					 if(null != billEntryDetailsDTO.getNoOfDaysAllowed() && null != billEntryDetailsDTO.getPerDayAmtProductBased() && null != billEntryDetailsDTO.getAmountAllowableAmount())
					 {
						 totalItemValueFld.setEnabled(false);
					 }
					 else
					 {
						// totalItemValueFld.setEnabled(true);
					 }
					 if(null != selValue)
					 {
						 if(("Deductibles").equalsIgnoreCase(selValue.getValue().trim()) || ("Deductibles(80%)").equalsIgnoreCase(selValue.getValue().trim())
									|| ("Hospital Discount").equalsIgnoreCase(selValue.getValue().trim()))
						 {
							 totalItemValueFld.setEnabled(false);
						 }
					 }
				 }
			 }
			
		}
	 }
	 
	 
	 public void addIrdaLevel2Values(Long irdaLevel1Key, GComboBox irdaLevel2Combo, SelectValue value) {
		
		 if(this.presenterString.equalsIgnoreCase(SHAConstants.BILLING)) {
			 fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_BILL_REVIEW_IRDA_LEVEL2_VALUES, irdaLevel1Key,irdaLevel2Combo,value);
		 } else if (this.presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
			 fireViewEvent(PAHealthFinancialReviewPagePresenter.FINANCIAL_BILL_REVIEW_IRDA_LEVEL2_VALUES, irdaLevel1Key,irdaLevel2Combo,value);
		 }
			
		 /*irdaLevel2Combo.setContainerDataSource(irdaLevel2Values);
		 irdaLevel2Combo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 irdaLevel2Combo.setItemCaptionPropertyId("value");
//			/categoryCombo.setNullSelectionAllowed(false);
			if(value != null) {
				irdaLevel2Combo.setValue(value);
			}*/
		}
	 
	 public void addIrdaLevel3Values(Long irdaLevel2Key, GComboBox irdaLevel3Combo, SelectValue value) {
			
		 if(this.presenterString.equalsIgnoreCase(SHAConstants.BILLING)) {
			 fireViewEvent(PAHealthBillingReviewPagePresenter.BILLING_BILL_REVIEW_IRDA_LEVEL3_VALUES, irdaLevel2Key,irdaLevel3Combo,value);
		 } else if (this.presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
			 fireViewEvent(PAHealthFinancialReviewPagePresenter.FINANCIAL_BILL_REVIEW_IRDA_LEVEL3_VALUES, irdaLevel2Key,irdaLevel3Combo,value);
		 }
	 }
			
		/* irdaLevel3Combo.setContainerDataSource(irdaLevel2Values);
		 irdaLevel3Combo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		 irdaLevel3Combo.setItemCaptionPropertyId("value");
//			/categoryCombo.setNullSelectionAllowed(false);
			if(value != null) {
				irdaLevel3Combo.setValue(value);
			}
		}*/
	 
	 
	 public void addCategoryValue(Long billClassificationKey, GComboBox categoryCombo, SelectValue value) {
		 if(this.presenterString.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
			 fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,domicillaryFlag);
		 } else if(this.presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
			 fireViewEvent(PAHealthClaimRequestMedicalDecisionPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,domicillaryFlag);
		 } else if(this.presenterString.equalsIgnoreCase(SHAConstants.BILLING)) {
			 fireViewEvent(PAHealthBillingReviewPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,domicillaryFlag);
		 } else if (this.presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
			 fireViewEvent(PAHealthFinancialReviewPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,domicillaryFlag);
		 }
		  else if(this.presenterString.equalsIgnoreCase(SHAConstants.PA_PROCESS_CLAIM_REQUEST)) {
			 fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType);
		 } 
		 else {
			 fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType);
		 }
			
			categoryCombo.setContainerDataSource(categoryValues);
			categoryCombo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			categoryCombo.setItemCaptionPropertyId("value");
//			/categoryCombo.setNullSelectionAllowed(false);
			if(value != null) {
				categoryCombo.setValue(value);
			}
		}
	 
	
	public void setUpCategoryValues(
			BeanItemContainer<SelectValue> categoryValues) {
		this.categoryValues = categoryValues;
		//addCategoryValues();
	}
	
	
	 public void addBeanToList(BillEntryDetailsDTO billEntryDetailsDTO) {
	    	//container.addBean(uploadDocumentsDTO);
		 container.addItem(billEntryDetailsDTO);

//	    	data.addItem(pedValidationDTO);
	    	//manageListeners();
	    }
	
	public void populateBillDetails(UploadDocumentDTO uploadDocumentsDTO) {
    	//container.addBean(uploadDocumentsDTO);
		
		

		billNo = uploadDocumentsDTO.getBillNo();
		
		billDate = uploadDocumentsDTO.getBillDate();
		
		noOfItems = uploadDocumentsDTO.getNoOfItems();
		
		billValue = uploadDocumentsDTO.getBillValue();
		
		pdtPerDayAmt = uploadDocumentsDTO.getProductBasedRoomRent();
		
		pdtICURentAmt = uploadDocumentsDTO.getProductBasedICURent();
		
		pdtICCURentAmt = uploadDocumentsDTO.getProductBasedICCURent();
		
		pdtAmbulanceAmt = uploadDocumentsDTO.getProductBasedAmbulanceAmt();
		
		fileName = uploadDocumentsDTO.getFileName();
		
		claimType = uploadDocumentsDTO.getClaimType();
		
		productKey = uploadDocumentsDTO.getProductKey();
		
		subCoverValue = uploadDocumentsDTO.getSubCoverCode();
		
		if(null != uploadDocumentsDTO.getDomicillaryFlag())
			domicillaryFlag = uploadDocumentsDTO.getDomicillaryFlag();

		/*BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		billEntryDetailsDTO.setBillNo(billNo);
		billEntryDetailsDTO.setBillDate(billDate);
		billEntryDetailsDTO.setNoOfItems(noOfItems);
		billEntryDetailsDTO.setBillValue(billValue);*/
	// container.addItem(billEntryDetailsDTO);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
    }

	public void setIrdaLevel2Values(

			BeanItemContainer<SelectValue> selectValueContainer,GComboBox cmb,SelectValue value) {
		this.irdaLevel2Values = selectValueContainer;



		cmb.setContainerDataSource(irdaLevel2Values);
		cmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb.setItemCaptionPropertyId("value");
		 
		 if(null != value)
		 {
			 cmb.setValue(value);
		 }
		
	}

	public void setIrdaLevel3Values(
			BeanItemContainer<SelectValue> selectValueContainer, GComboBox cmb,
			SelectValue value) {
		this.irdaLevel3Values = selectValueContainer;
		cmb.setContainerDataSource(irdaLevel3Values);
		cmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmb.setItemCaptionPropertyId("value");
		 if(null != value)
		 {
			 cmb.setValue(value);
		 }
		
	}
	
	public List<BillEntryDetailsDTO> getDeletedBillList()
	{
		return deletedList;
	}
	
	StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter() {
		private static final long serialVersionUID = -2154393632039317675L;

		protected java.text.NumberFormat getFormat(Locale locale) {
	        NumberFormat format = super.getFormat(locale);
	        format.setGroupingUsed(false);
	        return format;
	    };
	};
	

	private ShortcutListener getShortCutListener(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcut",KeyCodes.KEY_F8,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
				 TextField txtItemValue = (TextField) hashMap.get("deductibleOrNonPayableReason");
				
				 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				final TextArea txtArea = new TextArea();
				txtArea.setMaxLength(1000);
				//				txtArea.setData(bean);
				txtArea.setValue(txtFld.getValue());
				txtArea.setNullRepresentation("");
				txtArea.setRows(21);
				txtArea.setHeight("30%");
				txtArea.setWidth("100%");
				txtArea.setReadOnly(false);

				txtArea.setValue(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				txtArea.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
					}
				});

				billEntryDetailsDTO.setDeductibleOrNonPayableReason(txtArea.getValue());
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);

				final Window dialog = new Window();
				String strCaption = "Deductible/ Non Payables Reason";
				dialog.setHeight("75%");
				dialog.setWidth("65%");
				dialog.setCaption(strCaption);
				dialog.setClosable(true);
				dialog.setContent(vLayout);
				dialog.setResizable(true);
				dialog.setModal(true);
				dialog.setDraggable(true);
				dialog.setData(txtFld);

				dialog.addCloseListener(new Window.CloseListener() {
					@Override
					public void windowClose(CloseEvent e) {
						dialog.close();
					}
				});

				if(getUI().getCurrent().getPage().getWebBrowser().isIE()) {
					dialog.setPositionX(250);
					dialog.setPositionY(100);
				}
				getUI().getCurrent().addWindow(dialog);
				okBtn.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						dialog.close();
					}
				});
			}
		};
		
		return listener;
	}
	
	private ShortcutListener getShortCutListenerForMedicalReason(final TextField txtFld)
	{
		ShortcutListener listener =  new ShortcutListener("EnterShortcutForMedical",KeyCodes.KEY_F7,null) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) txtFld.getData();
				 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
				 TextField txtItemValue = (TextField) hashMap.get("medicalRemarks");
				
				 //txtFld.setTextChangeEventMode(TextChangeEventMode.TIMEOUT);
				 
				if (null != vLayout
						&& vLayout.getComponentCount() > 0) {
					vLayout.removeAllComponents();
				}
				
				TextArea txtArea = new TextArea();
				txtArea.setNullRepresentation("");
				txtArea.setMaxLength(300);
				
				txtArea.setValue(billEntryDetailsDTO.getMedicalRemarks());
				//txtArea.setValue(txtFld.getValue());
				txtArea.addValueChangeListener(new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						TextArea txt = (TextArea)event.getProperty();
						txtFld.setValue(txt.getValue());
						txtFld.setDescription(txt.getValue());
						// TODO Auto-generated method stub
						
					}
				});
				
				billEntryDetailsDTO.setMedicalRemarks(txtArea.getValue());
			//	txtFld.setDescription(billEntryDetailsDTO.getDeductibleOrNonPayableReason());
				txtFld.setDescription(txtArea.getValue());
				Button okBtn = new Button("OK");
				okBtn.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				vLayout.addComponent(txtArea);
				vLayout.addComponent(okBtn);
				vLayout.setComponentAlignment(okBtn,Alignment.TOP_CENTER);
				
				
				
				final Window dialog = new Window();
				dialog.setCaption("");
				dialog.setClosable(false);
				dialog.setContent(vLayout);
				dialog.setResizable(false);
				dialog.setModal(true);
				//dialog.show(getUI().getCurrent(), null, true);
				
				if(getUI().getCurrent().getPage().getWebBrowser().isIE() && ((null != fileName && fileName.endsWith(".PDF")) || (null != fileName && fileName.endsWith(".pdf")))) {
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
	
	private void showDeleteRowsPopup(String message, final Boolean isConfirmBoxReq,final List<BillEntryDetailsDTO> billEntryList,final Boolean isDeleteSelected)
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
//				dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(null!= isConfirmBoxReq && isConfirmBoxReq && null != isDeleteSelected && !isDeleteSelected)
				{
					if(null != table)
					{
						if(null != billEntryList && !billEntryList.isEmpty())
						{
							for (BillEntryDetailsDTO billEntryDTO : billEntryList) {
								
								deletedList.add(billEntryDTO);
								//table.removeItem(billEntryDTO);
							}
						}
						
						table.removeAllItems();
						
					}
					dialog.close();
				}
				else if(null != isDeleteSelected && isDeleteSelected)
				{
					if(null != table)
					{
						if(null != billEntryList && !billEntryList.isEmpty())
						{
							for (BillEntryDetailsDTO billEntryDTO : billEntryList) {
								
								deletedList.add(billEntryDTO);
								table.removeItem(billEntryDTO);
								//table.removeItem(billEntryDTO);
							}
							deleteSelectedItemList.clear();
						}
					}
					dialog.close();
				}
				else
				{
					dialog.close();
				}
				}
		});
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				
			}
		});
	
	}
	
	public Boolean warnMessageForPreAndPostNatal() {
   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PRE_POST_NATAL_WARN_MESSAGE + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
				//alertMessageForLumpSum();
			}
		});
		return true;
	}
	
	public Boolean validationForPAReasonableDeduct() {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.REASONABLE_DEDUCT_ALERT_MESSAGE + "</b>",
				ContentMode.HTML);
   		final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
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
				//alertMessageForLumpSum();
			}
		});
		return true;
	
		
	}
	
	public String createJWTTokenForClaimStatusPages(Map<String, String> userMap) throws NoSuchAlgorithmException, ParseException{	
		String token = "";	
		try {	
			KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");	
			keyGenerator.initialize(1024);	
		
			KeyPair kp = keyGenerator.genKeyPair();	
			RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();	
			RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();	
		
		
			JWSSigner signer = new RSASSASigner(privateKey);	
		
			JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder();	
			claimsSet.issuer("galaxy");	
			if(userMap.get("intimationNo") != null){
				claimsSet.claim("intimationNo", userMap.get("intimationNo"));	
			}
			if(userMap.get("reimbursementkey") != null){
				claimsSet.claim("reimbursementkey", userMap.get("reimbursementkey"));
			}
			if(userMap.get("acknowledgementNo") !=null){
				claimsSet.claim("acknowledgementNo", userMap.get("acknowledgementNo"));
			}
			if(userMap.get("ompdoc") != null){
				claimsSet.claim("ompdoc", userMap.get("ompdoc"));
			}
			if(userMap.get("cashlessDoc") != null){
				claimsSet.claim("cashlessDoc", userMap.get("cashlessDoc"));
			}
			if(userMap.get("lumenKey") != null){
				claimsSet.claim("lumenKey", userMap.get("lumenKey"));
			}
			claimsSet.expirationTime(new Date(new Date().getTime() + 1000*60*10));	
		
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet.build());	
		
			signedJWT.sign(signer);	
			token = signedJWT.serialize();	
			signedJWT = SignedJWT.parse(token);	
		
			JWSVerifier verifier = new RSASSAVerifier(publicKey);	
				
			return token;	
		} catch (JOSEException ex) {	
				
		}	
		return null;	
		
	 }

}

