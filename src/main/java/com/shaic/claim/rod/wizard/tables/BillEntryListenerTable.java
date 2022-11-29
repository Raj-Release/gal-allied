/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
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
import com.shaic.claim.reimbursement.billing.pages.billreview.BillingReviewPagePresenter;
import com.shaic.claim.reimbursement.financialapproval.pages.billreview.FinancialReviewPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.processclaimrequest.pages.medicaldecision.ClaimRequestMedicalDecisionPagePresenter;
import com.shaic.claim.reimbursement.medicalapproval.zonalMedicalreview.pages.premedicalprocessing.MedicalApprovalPremedicalProcessingPagePresenter;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.BillEntryDetailsPresenter;
import com.shaic.claims.reibursement.addaditionaldocuments.DetailsPresenter;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.paclaim.rod.enterbilldetails.search.PABillEntryDetailsPresenter;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.event.ShortcutListener;
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
public class BillEntryListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<BillEntryDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<BillEntryDetailsDTO> container = new BeanItemContainer<BillEntryDetailsDTO>(BillEntryDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	//private BeanItemContainer<SelectValue> category;
	
	//private List<String> errorMessages;
	
	//private static Validator validator;
	
	private BeanItemContainer<SelectValue> categoryValues;
	
	//private String billNo;
	
	//private Date billDate;
	
	private Long noOfItems;
	
	//private Double billValue;
	
	private SelectValue claimType;
	
	private Long productKey;
	
	private String subCoverValue;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	//private int iItemValue = 0;
	
	private String presenterString = "";
	
	private Button btnDuplicate;
	
	private  boolean isEmptyRowsCreated = false;
	
	private int iBillEntryTableSize;
	
	private Button viewUploadedDocument;
	
	private Button viewClaimsDMSDocument;

	private Window popup;
	
	private UploadDocumentDTO bean;
	final Embedded imageViewer = new Embedded("Uploaded Image");
	//private VerticalLayout layout;
	private HorizontalLayout hlayout ;
	private VerticalLayout layout;
	
	//private BrowserWindowOpener claimsDMSWindow;
	//private BrowserWindowOpener viewUploadDocWindow;
	
	
	//@Inject
	//private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	//@Inject
	//private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	//private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	private Button btnDeleteAll;
	
	private List<BillEntryDetailsDTO> deletedList;
	
	private Button btnDeleteSelectedItem;
	
	private List<BillEntryDetailsDTO> deleteSelectedItemList ;	
	
	public void initPresenter(String presenterString, int iSize) {
		this.presenterString = presenterString;
		iBillEntryTableSize = iSize;
	}
	
	public void init(UploadDocumentDTO bean) {
		populateBillDetails(bean);
		this.bean = bean;
		container.removeAllItems();
		//ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		deletedList = new ArrayList<BillEntryDetailsDTO>();
		deleteSelectedItemList =  new ArrayList<BillEntryDetailsDTO>();
		//validator = factory.getValidator();
		//this.errorMessages = new ArrayList<String>();
		/*btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		*//*HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);*/
		
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
	//	HorizontalLayout deleteAllBtnLayout = new HorizontalLayout(btnDeleteAll);
		HorizontalLayout deleteAllBtnLayout = new HorizontalLayout(btnDeleteAll,btnDeleteSelectedItem);

		
		hlayout = buildButtonLayout();
		
		layout = new VerticalLayout();
		layout.setMargin(true);
		

//		HorizontalLayout hLayout1 = new HorizontalLayout(viewUploadedDocument/*,viewClaimsDMSDocument*/ );

		HorizontalLayout hLayout1 = new HorizontalLayout(viewUploadedDocument,viewClaimsDMSDocument);
		
	
		hLayout1.setComponentAlignment(viewUploadedDocument, Alignment.MIDDLE_RIGHT);
		layout.addComponent(hLayout1);
		layout.setComponentAlignment(hLayout1, Alignment.MIDDLE_RIGHT);
		/*layout.addComponent(viewUploadedDocument);
		layout.setComponentAlignment(viewUploadedDocument, Alignment.MIDDLE_RIGHT);*/
		
		layout.addComponent(hlayout);
		//layout.setComponentAlignment(hLayout, Alignment.MIDDLE_RIGHT);
		if(bean != null && (bean.getBillEntryDetailList() !=null && bean.getBillEntryDetailList().isEmpty()))
		{
			isEmptyRowsCreated = false;
		}
		
		initTable();
		table.setWidth("100%");
		table.setHeight("470px");
		table.setPageLength(table.getItemIds().size());
		
		//addListener();
		
		layout.addComponent(deleteAllBtnLayout);
		layout.setComponentAlignment(deleteAllBtnLayout, Alignment.MIDDLE_RIGHT);
		
		layout.addComponent(table);
		layout.setWidth("100%");
		layout.setSizeFull();
		//layout.setHeight("70%");
		//layout.setWidth("70%");

		setCompositionRoot(layout);
		
		
		/*VerticalLayout layout = new VerticalLayout();
		//HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
	
		setCompositionRoot();*/
		//setCompositionRoot(horLayout);
	}
	
	public HorizontalLayout buildButtonLayout()
	{
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		btnDuplicate = new Button("Add Duplicate Row");
		btnDuplicate.setStyleName("link");
		
		
		HorizontalLayout btnLayout = new HorizontalLayout(btnDuplicate,btnAdd);
		btnLayout.setWidth("190%");
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
		table.setVisibleColumns(new Object[] { "itemNo", "itemName", "classification", "category","noOfDays", "perDayAmt", "itemValue"});
		table.setColumnHeader("itemNo", "Item</br> No");
		table.setColumnHeader("itemName", "Item Name </br>(As per bill)");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("noOfDays", "No of </br>Days");
		table.setColumnHeader("perDayAmt", "Per Day </br> Amt");
		table.setColumnHeader("itemValue", "Item </br> Value");
		table.setEditable(true);
		table.removeGeneratedColumn("Delete");
		table.addGeneratedColumn("Delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				//final Button deleteButton = new Button("Delete");
				final Button deleteButton = new Button();
				//deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
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
						deletedList.add(billEntryDetailsDTO);
						table.removeItem(currentItemId);
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
		if(!isEmptyRowsCreated)
		{
			createEmptyRowsBasedOnNoOfItems();
		}
	}
	
	public List<BillEntryDetailsDTO> getDeletedBillList()
	{
		return deletedList;
	}
	
	
	private void createEmptyRowsBasedOnNoOfItems()
	{
		for (int i = 0; i < noOfItems-iBillEntryTableSize ; i++) {
			container.addItem(new BillEntryDetailsDTO());
		}
		
//		List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
//		if(null != dtoList && !dtoList.isEmpty())
//		{
//			int iSize = dtoList.size();
//			BillEntryDetailsDTO dto = dtoList.get(iSize-1);
//			HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
//			final TextField txtFld = (TextField) combos.get("itemName");
//			txtFld.focus();
//			
//		}
		
		isEmptyRowsCreated = true;
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
	
	
	private void addListener() {
		btnAdd.focus();
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				//if(container.size()==0){
				//BeanItem<BillEntryDetailsDTO> addItem = container.addItem(new BillEntryDetailsDTO());
				
				addRowInTable();
				/*container.addItem(new BillEntryDetailsDTO());
				List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					BillEntryDetailsDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					if(null != combos)
					{
					final TextField txtFld = (TextField) combos.get("itemName");
					txtFld.focus();
					}

				}*/
				
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				//manageListeners();
			}
		});
		
//		new ShortcutListener("Escape", KeyCode.F6, ModifierKey.CTRL, ModifierKey.SHIFT) {
//			private static final long serialVersionUID = 1832700231438260069L;
//
//			@Override
//            public void handleAction(Object sender, Object target) {
//				container.addItem(new BillEntryDetailsDTO());
//				List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
//				if(null != dtoList && !dtoList.isEmpty())
//				{
//					int iSize = dtoList.size();
//					BillEntryDetailsDTO dto = dtoList.get(iSize-1);
//					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
//					if(null != combos)
//					{
//					final TextField txtFld = (TextField) combos.get("itemName");
//					txtFld.focus();
//					}
//
//				}
//            }
//        }
		
//		btnAdd.setClickShortcut(ShortcutAction.KeyCode.F6, ShortcutAction.ModifierKey.CTRL,ShortcutAction.ModifierKey.SHIFT);
		
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
					HashMap<String, AbstractField<?>> combos = tableItem.get(duplicateDTO);
					final TextField txtFld = (TextField) combos.get("itemName");
					txtFld.focus();
					
				}
				/*List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
				if(null != dtoList && !dtoList.isEmpty())
				{
					int iSize = dtoList.size();
					BillEntryDetailsDTO dto = dtoList.get(iSize-1);
					HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
					final TextField txtFld = (TextField) combos.get("itemName");
					txtFld.focus();
					
				}*/
				
				
				//if(container.size()==0){
				
				//}
				//else{
				//	btnAdd.setVisible(false);
			//	}
				//manageListeners();
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
									//showDeleteRowsPopup("All the bills entered in the table will be deleted. Press OK to proceed.", true);
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
										getUI().getPage().open(imageUrl, "_blank",1550,650,BorderStyle.NONE);
										
									//	getUI().getPage().open(imageUrl, "_blank",true);
										
										
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
												FileInputStream fis = null;
												try {
													File f = new File(url);
													fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												} finally{
													try{
														if(fis != null){
															fis.close();
														}
													}catch (Exception exception){
														exception.printStackTrace();
													}
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
				                                	URLConnection urlConnection = null;
				                                	try {
//				                                		u =  new URL(imageUrl);
//				                                	  is = u.openStream();
				                                		
				                                		u =  new URL(imageUrl);
				                    					urlConnection =  u.openConnection();
				                    					is = urlConnection.getInputStream();
				                                		
				                                	  
//				                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
				                    					byte[]  byteChunk = null;
				                						if(urlConnection.getContentLength() > 25000){
				                							byteChunk = new byte[25000];
				                						} else {
				                							byteChunk = new byte[urlConnection.getContentLength()];
				                						}
				                    					
				                                	  int n;

				                                	  while ( (n = is.read(byteChunk)) > 0 ) {
				                                	    baos.write(byteChunk, 0, n);
				                                	  }
				                                	  byteChunk = null;
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
				                                	  
				                                	  if (null != urlConnection) {
				            		               		  try
				            		               		  {
				            		               			urlConnection.getInputStream().close();
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
				                        
				                       // getUI().getPage().open(r, "_blank", false);
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
												FileInputStream fis = null;
												try {
													File f = new File(url1);
													fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												} finally{
													try{
														if(fis != null){
															fis.close();
														}
													}catch (Exception exception){
														exception.printStackTrace();
													}
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
									//	 getUI().getPage().open(imageUrl, "_blank");
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
												FileInputStream fis = null;
												try {
													File f = new File(url);
													fis = new FileInputStream(f);
													return fis;
												} catch (Exception e) {
													e.printStackTrace();
													return null;
												} finally{
													try{
														if(fis != null){
															fis.close();
														}
													}catch (Exception exception){
														exception.printStackTrace();
													}
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
											FileInputStream fis = null;
											try {
												File f = new File(url);
												fis = new FileInputStream(f);
												return fis;
											} catch (Exception e) {
												e.printStackTrace();
												return null;
											} finally{
												try{
													if(fis != null){
														fis.close();
													}
												}catch (Exception exception){
													exception.printStackTrace();
												}
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
							*//**
							 * 
							 *//*
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
		newDto.setNoOfDays(duplicateDTO.getNoOfDays());
		newDto.setPerDayAmt(duplicateDTO.getPerDayAmt());
		newDto.setItemValue(duplicateDTO.getItemValue());
		return newDto;
	}

	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
		
	
	
	/*public void manageListeners() {

		for (BillEntryDetailsDTO billEntryDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(billEntryDetailsDTO);
			
			final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");
			
			 
//			 daysAndAmtValidationListener(categoryCombo);
			//addClassificationListener(classificationCombo,categoryCombo);
			//calculateItemValue(txtPerDay);
			
			
			

		}
	}*/
	
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
				//tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("itemName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				field.setMaxLength(50);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[a-zA-Z ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemName", field);
				return field;
			}
			
			else if ("itemNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(entryDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemNo", field);

				return field;
			}
			else if ("classification".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("140px");
				//box.setWidth("150px");
				tableRow.put("classification", box);
				box.setData(entryDTO);
				final ComboBox category = (ComboBox) tableRow.get("category");
				addClassificationListener(box,category);
				addBillClassificationValues(box);
				return box;
			}
			else if ("category".equals(propertyId)) {
				GComboBox box = new GComboBox();
				box.setWidth("150px");
				//box.setWidth("150px");
				tableRow.put("category", box);
				entryDTO.setIsAlreadyEntered(true);
				box.setData(entryDTO);
				ComboBox classification = (ComboBox) tableRow.get("classification");
				if(entryDTO.getClassification() != null) {
					extracted(classification);
				}
				
				 daysAndAmtValidationListener(box);

				//box.addListener(valueChangeListenerForCategory(box));
				//box.addValueChangeListener(valueChangeListenerForCategory(box));
				
				return box;
			}
			else if("noOfDays".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setWidth("90px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9.]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("noOfDays", field);
				
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) 
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) 
							|| ("ICCU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) )
							
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						field.setValue(null);
						
					}
				}
				if(null != entryDTO && null != entryDTO.getNoOfDays() )
				{
					field.setValue(String.valueOf(entryDTO.getNoOfDays()));
				}
				
				field.addBlurListener(getNoOfDaysListener());
				final TextField txt = (TextField) tableRow.get("itemNo");
				generateSlNo(txt);
				/*final ComboBox category = (ComboBox) tableRow.get("category");
				daysAndAmtValidationListener(category);*/

				return field;
			}
			else if("perDayAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("90px");
				//field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				field.setData(entryDTO);

				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("perDayAmt", field);
				
				if(null != entryDTO && null != entryDTO.getCategory() && null != entryDTO.getCategory().getValue())
				{
					if(("ICU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue())
							
							|| ("Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) 
							|| ("ICCU Nursing Charges").equalsIgnoreCase(entryDTO.getCategory().getValue()) 
							|| ("ICCU Room Rent").equalsIgnoreCase(entryDTO.getCategory().getValue()) )
					{
						field.setEnabled(true);
					}
					else
					{
						field.setEnabled(false);
						field.setValue(null);
						
					}
				}
				
				if(null != entryDTO && null != entryDTO.getPerDayAmt())
				{
					field.setValue(String.valueOf(entryDTO.getPerDayAmt()));
				}
				
				field.addBlurListener(getPerDayAmtListener());
				return field;
			}
			else if("itemValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				//field.setWidth("110px");
				//field.setWidth("80px");
				field.setNullRepresentation("");
				field.setMaxLength(10);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("itemValue", field);
				valueChangeLisenerForText(field);
				calculateTotal();
				enableOrDisableFields(field);
				
				if(null != entryDTO && null != entryDTO.getItemValue())
				{
					field.setValue(String.valueOf(entryDTO.getItemValue()));
				}
				
				
				//daysAndAmtValidationListener(category);
				
				//field.addListener(getValueChangeListener());
				//field.addValueChangeListener(getValueChangeListener());
				//field.addBlurListener(getItemValueListener());
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
	
	 public List<BillEntryDetailsDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<BillEntryDetailsDTO> itemIds = (List<BillEntryDetailsDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }
	 
	 
	 @SuppressWarnings("unchecked")
		public void addBillClassificationValues(ComboBox comboBox) {
			BeanItemContainer<SelectValue> billClassificationContainer = (BeanItemContainer<SelectValue>) referenceData
					.get("billClassification");
			
			//BeanItemContainer<SelectValue> finalContainer = billClassificationContainer;
			/*for(int i = 0 ; i<billClassificationContainer.size() ; i++)
			 {
				if (("Hospital").equalsIgnoreCase(billClassificationContainer.getIdByIndex(i).getValue()))
				{
					this.cmbDocumentsReceivedFrom.setValue(docReceivedFromRequest.getIdByIndex(i));
				}
			}*/
			
			comboBox.setContainerDataSource(billClassificationContainer);
			comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			comboBox.setItemCaptionPropertyId("value");
			/*if(null != comboBox)
			{
				BillEntryDetailsDTO dto = (BillEntryDetailsDTO) comboBox.getData();
				SelectValue selValue = (SelectValue) comboBox.getValue();
				if(null == selValue)
				{
					dto.setClassification(null);
				}
			}*/

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
								itemValue.setValue(null);
								itemValue.setEnabled(true);
								}
							}
						}
					}
				}
			}			
		}
		
	 
	 
//	 private void daysAndAmtValidationListenerForPreviousRow (final ComboBox component) {
//		 
//		 if(null != component)
//		 {		 
//			 component.addListener(new Listener() {
//				
//				@Override
//				public void componentEvent(Event event) {
//					// TODO Auto-generated method stub
//					daysAndAmtValidationListener(component, event,true);
//				}
//
//	
//			});
//		 }
//	}
	 
     private void daysAndAmtValidationListener (final ComboBox component) {
		 
		 if(null != component)
		 {		 
			 component.addListener(new Listener() {
				
				@Override
				public void componentEvent(Event event) {
					// TODO Auto-generated method stub
					//SelectValue selValue = (SelectValue)component.getValue();
					/*if(null != selValue)
					{
						Long id = selValue.getId();
						if(ReferenceTable.getPrePostNatalMap().containsKey(id))
						{
							warnMessageForPreAndPostNatal();
						}
					}*/
					daysAndAmtValidationListener(component, event);
				}

	
			});
		 }
	}
	 
		private void daysAndAmtValidationListener(final ComboBox component, Event event) {
			//ComboBox cmb = (ComboBox)event.getComponent();
			//SelectValue seltValue = (SelectValue) cmb.getValue(); 
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			GComboBox comboBox = (GComboBox) hashMap.get("category");
			if (null != billEntryDetailsDTO) {
				//if(null != seltValue && null != seltValue.getValue())
				//{
				if(null != billEntryDetailsDTO.getCategory()) {
					if(null != comboBox && null != comboBox.getValue()) {
						
						SelectValue selValue = (SelectValue)comboBox.getValue();
						HashMap<String, AbstractField<?>> valHashMap = tableItem.get(billEntryDetailsDTO);
						TextField noOfDays = (TextField) valHashMap.get("noOfDays");
						TextField perDayAmt = (TextField) valHashMap.get("perDayAmt");
						TextField itemValueFld =  (TextField)  valHashMap.get("itemValue");
						/*if(("ICU Rooms").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
								
								|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue())
								)*/
						if(("ICU Room Rent").equalsIgnoreCase(selValue.getValue()) || ("Room Rent").equalsIgnoreCase(selValue.getValue())
								
								|| ("Nursing Charges").equalsIgnoreCase(selValue.getValue()) || ("ICU Nursing Charges").equalsIgnoreCase(selValue.getValue()) 
								|| ("ICCU Room Rent").equalsIgnoreCase(selValue.getValue())
								|| ("ICCU Nursing Charges").equalsIgnoreCase(selValue.getValue()))
						{
							if(null != noOfDays)
							{
								noOfDays.setEnabled(true);
							}
							if(null != perDayAmt)
								perDayAmt.setEnabled(true);
							if(null != itemValueFld)
								itemValueFld.setEnabled(false);
						}
						else
						{
							if(null != noOfDays)
							{
							noOfDays.setEnabled(false);
							noOfDays.setValue(null);
							}
							if(null != perDayAmt)
							{
							perDayAmt.setEnabled(false);
							perDayAmt.setValue(null);
							}
							if(null != itemValueFld)
							{
								//itemValueFld.setValue(null);
								//Commenting below code, for a major issue. This fix needs to be revisited.
								//if(! billEntryDetailsDTO.getIsAlreadyEntered()){
								itemValueFld.setEnabled(true);
//								itemValueFld.setValue(null);
								//}
							}
						}
						billEntryDetailsDTO.setIsAlreadyEntered(false);
					}
				}
			//}
				/**
				 * Saravanan's change : In a same row,  if the classification value changes, then
				 * the no of days, per day amt and item value boxes will be reset to blank values.
				 * */
				/*else
				{
					HashMap<String, AbstractField<?>> valHashMap = tableItem.get(billEntryDetailsDTO);
					TextField noOfDays = (TextField) valHashMap.get("noOfDays");
					TextField perDayAmt = (TextField) valHashMap.get("perDayAmt");
					TextField itemValue = (TextField) valHashMap.get("itemValue");
					if(null != noOfDays)
					{
						noOfDays.setValue(null);
					}
					if(null != perDayAmt)
					{
						perDayAmt.setValue(null);
					}
					if(null != itemValue)
					{
						itemValue.setValue(null);
						if(!itemValue.isEnabled())
						{
							itemValue.setEnabled(true);
						}
					}
				}*/
				
		}
		}
	 
	 
	 
	 
		private void extracted(ComboBox component) {
			BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) component.getData();
			HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			ComboBox comboBox = (ComboBox) hashMap.get("category");
			if (billEntryDetailsDTO != null) {
				if(billEntryDetailsDTO.getClassification() != null) {
					if(comboBox != null) {
						addCategoryValue(billEntryDetailsDTO.getClassification().getId(), comboBox, billEntryDetailsDTO.getCategory());
						}
				}			
			}
		}
		
		public ValueChangeListener valueChangeListenerForCategory(final ComboBox box)
		{
			
			//if(null != box)
			//{
				ValueChangeListener valChange = new ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						daysAndAmtValidationListener(box);
						
					}
				};
				return valChange;
			//}
			
		}
	 
		public void valueChangeLisenerForText(TextField total){
			
			if(null != total)
			{
			total
			.addValueChangeListener(new com.vaadin.v7.data.Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					calculateTotal();
					
				}
			});
			}
		
			
		}
	 
	 public BlurListener getNoOfDaysListener() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					calculateItemValue(component);
					
				}
			};
			return listener;
			
		}
	 
	 public BlurListener getPerDayAmtListener() {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {
					TextField component = (TextField) event.getComponent();
					calculateItemValue(component);
					
				}
			};
			return listener;
			
		}
		
		 /*public ValueChangeListener getNoOfDaysListener() {
				
				ValueChangeListener listener = new ValueChangeListener() {
					
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					
					

					@Override
					public void valueChange(ValueChangeEvent event) {
						event.getProperty().getValue();
						TextField component = (TextField) event.getComponent();
						calculateItemValue(component);
						
					}

					
				};
				return listener;
				
			}
		 
		 public BlurListener getPerDayAmtListener() {
				
				BlurListener listener = new BlurListener() {
					
					*//**
					 * 
					 *//*
					private static final long serialVersionUID = 1L;

					@Override
					public void blur(BlurEvent event) {
						TextField component = (TextField) event.getComponent();
						calculateItemValue(component);
						
					}
				};
				return listener;
				
			}*/
	 
	
	 
	
	 
	 public void calculateTotal()
	 {
		 List<BillEntryDetailsDTO> itemIconPropertyId = (List<BillEntryDetailsDTO>) table.getItemIds();
		 Double total  = 0d;
		 if(null != itemIconPropertyId && !itemIconPropertyId.isEmpty())
		 {
			 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIconPropertyId) {
				 if(null != billEntryDetailsDTO.getItemValue() )
				 {
					// if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null && !(billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("discount") || billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("deductibles")) )) {
					 if(billEntryDetailsDTO.getCategory() == null || (billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue() != null )) {
						 if(billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("discount") || billEntryDetailsDTO.getCategory() != null && billEntryDetailsDTO.getCategory().getValue().toString().toLowerCase().contains("deductibles"))
						 {
							 total -= billEntryDetailsDTO.getItemValue();
						 }
						 else
						 {
							 total += billEntryDetailsDTO.getItemValue();
						 }
					 }
					 
					 
				 }
		}
		 table.setColumnFooter("itemValue", String.valueOf(total));
		 totalBillValue = total;
		 }
	 }
	 
	 private void calculateItemValue(Component component)
	 {
		 BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		 TextField txtField = (TextField)component;
		 billEntryDetailsDTO = (BillEntryDetailsDTO) txtField.getData();
		 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
		 TextField txtPerDayAmt = (TextField) hashMap.get("perDayAmt");
		 TextField txtItemValue = (TextField) hashMap.get("itemValue");
		 TextField txtNoOfDays = (TextField) hashMap.get("noOfDays");
		/* Integer perDayAmt = 0;
		 Integer noOfDaysVal = 0;*/
		 Double perDayAmt = 0d;
		 Double noOfDaysVal = 0d;
		 
		 if(null != txtNoOfDays && null != txtNoOfDays.getValue() && !("").equalsIgnoreCase(txtNoOfDays.getValue()))
		 {
			// noOfDaysVal =  SHAUtils.getIntegerFromStringWithComma(txtNoOfDays.getValue());
			 noOfDaysVal = SHAUtils.getDoubleFromStringWithComma(txtNoOfDays.getValue());
			 txtItemValue.setEnabled(false);
			 //txtDummyFld.setEnabled(false);
		 }
		 /*else
		 {
			// txtItemValue.setEnabled(true);
			// txtDummyFld.setEnabled(true);
		 }*/
		 
		 if(null != txtPerDayAmt && null != txtPerDayAmt.getValue() && !("").equalsIgnoreCase(txtPerDayAmt.getValue()))
		 {
			// perDayAmt = SHAUtils.getIntegerFromStringWithComma(txtPerDayAmt.getValue());
			 perDayAmt = SHAUtils.getDoubleFromStringWithComma(txtPerDayAmt.getValue());
			 txtItemValue.setEnabled(false);
		 }
		/* else
		 {
			 //txtItemValue.setEnabled(true);
		 }*/
		 
		 Double itemValue = noOfDaysVal * perDayAmt;
	
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
		 txtItemValue.setValue(String.valueOf(itemValue));

		 /*if(null != itemValue)
		 {
			 txtItemValue.setValue(String.valueOf(Math.ceil(itemValue)));
		 }*/
	 }
	 
	 
	 private void enableOrDisableFields(TextField txtField)
	 {
		 Collection<BillEntryDetailsDTO> itemIds = (Collection<BillEntryDetailsDTO>) table.getItemIds();
		 
		 for (BillEntryDetailsDTO billEntryDetailsDTO : itemIds) {
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(billEntryDetailsDTO);
			 if(null != hashMap)
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
			 }
			
		}
	 }
	 
	 
	 public void addCategoryValue(Long billClassificationKey, ComboBox categoryCombo, SelectValue value) {
		 if(this.presenterString.equalsIgnoreCase(SHAConstants.ZONAL_REVIEW)) {
			 fireViewEvent(MedicalApprovalPremedicalProcessingPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo);
		 } else if(this.presenterString.equalsIgnoreCase(SHAConstants.CLAIM_REQUEST)) {
			 fireViewEvent(ClaimRequestMedicalDecisionPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,bean.getIntimationNo());
		 } else if(this.presenterString.equalsIgnoreCase(SHAConstants.BILLING)) {
			 fireViewEvent(BillingReviewPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,bean.getIntimationNo());
		 } else if (this.presenterString.equalsIgnoreCase(SHAConstants.FINANCIAL)) {
			 fireViewEvent(FinancialReviewPagePresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,bean.getIntimationNo());
		 } else if (this.presenterString.equalsIgnoreCase(SHAConstants.ADD_ADDITIONAL_DOCS)) {
			 fireViewEvent(DetailsPresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,bean.getIntimationNo());
		 } else if(this.presenterString.equalsIgnoreCase(SHAConstants.PA_BILL_ENTRY)){
			 fireViewEvent(PABillEntryDetailsPresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,bean.getIntimationNo());
		 }
		 else {
			 fireViewEvent(BillEntryDetailsPresenter.BILL_ENTRY_TABLE_CATEGORY_DROPDOWN_VALUE, billClassificationKey,categoryCombo,claimType,productKey,subCoverValue,bean.getIntimationNo());
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
		
		

		//billNo = uploadDocumentsDTO.getBillNo();
		
		//billDate = uploadDocumentsDTO.getBillDate();
		
		noOfItems = uploadDocumentsDTO.getNoOfItems();
		
		//billValue = uploadDocumentsDTO.getBillValue();
		
		isEmptyRowsCreated = uploadDocumentsDTO.getEmptyRowStatus();
		
		claimType = uploadDocumentsDTO.getClaimType();
		
		productKey = uploadDocumentsDTO.getProductKey();
		
		subCoverValue = uploadDocumentsDTO.getSubCoverCode();

		/*BillEntryDetailsDTO billEntryDetailsDTO = new BillEntryDetailsDTO();
		billEntryDetailsDTO.setBillNo(billNo);
		billEntryDetailsDTO.setBillDate(billDate);
		billEntryDetailsDTO.setNoOfItems(noOfItems);
		billEntryDetailsDTO.setBillValue(billValue);*/
	// container.addItem(billEntryDetailsDTO);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
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
							/*for (BillEntryDetailsDTO billEntryDTO : billEntryList) {
								
								deletedList.add(billEntryDTO);
								//table.removeItem(billEntryDTO);
							}*/
							
							deletedList.addAll(billEntryList);
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
   		/*Label successLabel = new Label(
				"<b style = 'color: red;'>" + SHAConstants.PRE_POST_NATAL_WARN_MESSAGE + "</b>",
				ContentMode.HTML);
   		//final Boolean isClicked = false;
		Button homeButton = new Button("OK");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setStyleName("borderLayout");
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
//		dialog.setCaption("Alert");
		dialog.setClosable(false);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/

		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox("<b style = 'color: red;'>" + SHAConstants.PRE_POST_NATAL_WARN_MESSAGE + "</b>", buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
				//alertMessageForLumpSum();
			}
		});
		return true;
	}
	
	public ShortcutListener addButtonShortCut(){
		int[] modifierArr = new int[]{ModifierKey.CTRL};
		ShortcutListener listener = new ShortcutListener("Add Row", KeyCode.F7, null) {
			private static final long serialVersionUID = 1832700231438260069L;
			int iCount = 0;
			@Override
			public void handleAction(Object sender, Object target) {
				iCount = iCount+1;
				if(iCount == 1){
					addRowInTable();
				}else if(iCount > 1){
					iCount = 0;
				}
			}
		};
		btnAdd.addShortcutListener(listener);
		return listener;
	}
	
	@SuppressWarnings("unchecked")
	private void addRowInTable(){
		container.addItem(new BillEntryDetailsDTO());
		List<BillEntryDetailsDTO> dtoList = (List<BillEntryDetailsDTO>)table.getItemIds();
		if(null != dtoList && !dtoList.isEmpty()){
			int iSize = dtoList.size();
			BillEntryDetailsDTO dto = dtoList.get(iSize-1);
			HashMap<String, AbstractField<?>> combos = tableItem.get(dto);
			if(null != combos) {
				final TextField txtFld = (TextField) combos.get("itemName");
				txtFld.focus();
			}
		}
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
