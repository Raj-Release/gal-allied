/**
 * 
 */
package com.shaic.claim;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAFileUtils;
import com.shaic.claim.reimbursement.rrc.detailsPage.ViewClaimWiseRRCHistoryPage;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.vijayar
 *
 */
public class DMSDocumentViewListenerTable  extends ViewComponent { 
	
	private static final long serialVersionUID = 7802397137014194525L;
	
	private Map<DMSDocumentDetailsDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<DMSDocumentDetailsDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<DMSDocumentDetailsDTO> container = new BeanItemContainer<DMSDocumentDetailsDTO>(DMSDocumentDetailsDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Map<String, Object> referenceData;
	
	private List<String> errorMessages;
	
	private static Validator validator;
		
	private String presenterString = "";
	
	private Window popup;
	
	private DMSDocumentDetailsDTO bean;
	
	@Inject
	private ViewClaimWiseRRCHistoryPage viewClaimWiseRRCHistoryPage;
	
	private Boolean isEnabledFlag = true;
	
	
	final Embedded imageViewer = new Embedded("Uploaded Image");
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(DMSDocumentDetailsDTO bean)
	{
		this.bean = bean;
	}
	
	
	
	public void init(/*QuantumReductionDetailsDTO bean*/) {
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new ArrayList<String>();
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		
		initTable();
		
		table.setPageLength(table.getItemIds().size());
//		table.setCaption("Document Details Table");
		
		addListener();
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(table);
		horLayout.setComponentAlignment(table, Alignment.TOP_RIGHT);
		table.setWidth("100%");
		table.setHeight("400px");
		horLayout.setWidth("100%");
		horLayout.setMargin(true);
		setCompositionRoot(horLayout);
		setSizeFull();
	}
	
	public void setTableList(final List<DMSDocumentDetailsDTO> list) {
		table.removeAllItems();
		for (final DMSDocumentDetailsDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("90%");
		table.setPageLength(table.getItemIds().size());
		table.setVisibleColumns(new Object[] { "slNo", "documentType","cashlessOrReimbursement", "fileName","documentCreatedDateValue" ,"documentSource" });		
		table.setColumnHeader("slNo", "Sl No");
		table.setColumnHeader("documentType", "Document  Type");
		table.setColumnHeader("cashlessOrReimbursement", " Cashless/ROD Number");
		table.setColumnHeader("fileName", "File  Name");
		table.setColumnHeader("documentCreatedDateValue", "Document  Received/Sent Date And Time");
		table.setColumnHeader("documentSource", "Document  Source");
		table.setColumnWidth("documentCreatedDateValue", 230);
		
		table.setEditable(true);
		
		table.removeGeneratedColumn("View Document");
		table.addGeneratedColumn("View Document", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("View Document");
		    	editButton.setData(itemId);
		    	
		    	editButton.setStyleName(ValoTheme.BUTTON_LINK);
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					@SuppressWarnings("deprecation")
					public void buttonClick(ClickEvent event) {
						DMSDocumentDetailsDTO bean = (DMSDocumentDetailsDTO)itemId;
						 
						if(null != bean.getFileName())
						{
							if(bean.getFileName().endsWith(".JPG") || bean.getFileName().endsWith(".jpg") || bean.getFileName().endsWith(".jpeg"))
							{
								 String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
								
								if(imageUrl != null) {
									//getUI().getPage().open(imageUrl, "_blank");
									UI.getCurrent().getPage().open(imageUrl, "_blank");
									//StreamResource st = new StreamResource(, filename)
									//ExternalResource ex = new ExternalResource(imageUrl);
//									imageViewer.setSource(new ExternalResource(imageUrl));
//								    imageViewer.setVisible(true);  
//								    imageViewer.setHeight("500px");
//								    showPopup(imageViewer);	
									
/*									Embedded e = new Embedded();
							        e.setSizeFull();
							        e.setType(Embedded.TYPE_BROWSER);
							        StreamResource.StreamSource source = new StreamResource.StreamSource() {
		                                public InputStream getStream() {
		                                	
		                                	BufferedImage image = new BufferedImage (200, 200,
		                                            BufferedImage.TYPE_INT_RGB);
		                                	ByteArrayOutputStream imagebuffer  = new ByteArrayOutputStream();
		                                	InputStream is = null;
		                                	URL u = null;
		                                	try {
		                                		
		                                		ImageIO.write(image, "JPG", imagebuffer);
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
		                                	return new ByteArrayInputStream(imagebuffer .toByteArray());
		                                }
		                        };
		                        StreamResource r = new StreamResource(source, bean.getFileName());
		                       // r.setMIMEType("application/pdf");
		                        r.setStreamSource(source);
							    e.setSource(r);
							    showPopup(e);*/
							    
							    

									
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

							//else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith("pdf") /*|| bean.getGalaxyFileName().endsWith(".PDF") || bean.getGalaxyFileName().endsWith("pdf")*/)

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
//			                                		u =  new URL(imageUrl);
//			                                	  is = u.openStream();
			                                		
			                                		u =  new URL(imageUrl);
			                    					urlConnection =  u.openConnection();
			                    					is = urlConnection.getInputStream();
			                                	  
//			                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
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
			                        r.setFilename(bean.getFileName()+System.currentTimeMillis());
			                        UI.getCurrent().getPage().open(r, "_blank", false);
//								    e.setSource(r);
//								    showPopup(e);
									
									//Button saveExcel = new Button();
									//AdvancedFileDownloader adv = new AdvancedFileDownloader();
									//Resource res = new FileResource(new File(imageUrl));
									/*final ExternalResource res = new ExternalResource(imageUrl);
								
									//res.setMIMEType("application/pdf");
									FileDownloader fd = new FileDownloader(res);
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
							        showPopup(e);
						*/		}
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
							        showPopup(e);
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
									 getUI().getPage().open(imageUrl, "_blank");
//									 Embedded e = new Embedded();
//								        e.setSizeFull();
//								        e.setType(Embedded.TYPE_BROWSER);
//							         StreamResource.StreamSource source = new StreamResource.StreamSource() {
//			                                public InputStream getStream() {
//			                                   
//			                                	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			                                	InputStream is = null;
//			                                	URL u = null;
//			                                	try {
//			                                		u =  new URL(imageUrl);
//			                                	  is = u.openStream();
//			                                	  
//			                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
//			                                	  int n;
//
//			                                	  while ( (n = is.read(byteChunk)) > 0 ) {
//			                                	    baos.write(byteChunk, 0, n);
//			                                	  }
//			                                	}
//			                                	catch (IOException e) {
//			                                	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
//			                                	  e.printStackTrace ();
//			                                	  // Perform any other exception handling that's appropriate.
//			                                	}
//			                                	finally {
//			                                	  if (is != null) {
//			                                		  try
//			                                		  {
//			                                			  is.close();
//			                                		  }
//			                                		  catch(Exception e)
//			                                		  {
//			                                			  e.printStackTrace();
//			                                		  }
//			                                		  }
//			                                	}
//			                                	return new ByteArrayInputStream(baos.toByteArray());
//			                                }
//			                        };
//			                        StreamResource r = new StreamResource(source, bean.getFileName());
//			                        r.setMIMEType("text/html");
//			                        r.setStreamSource(source);
//			                        r.setFilename(bean.getFileName());
//								    e.setSource(r);
//								    showPopup(e);
									
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
							        showPopup(e);
								}
							}

							
						else if(bean.getFileName().endsWith(".xlsx") /*|| bean.getGalaxyFileName().endsWith(".xlsx")*/)
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
								UI.getCurrent().getPage().open(imageUrl, "_blank");
								/*Button saveExcel = new Button();
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
								r.setMIMEType("application/x-msexcel");
						        e.setSource(r);
						        showPopup(e);*/
								
//								Embedded e = new Embedded();
//						        e.setSizeFull();
//						        e.setType(Embedded.TYPE_BROWSER);
//					         StreamResource.StreamSource source = new StreamResource.StreamSource() {
//	                                public InputStream getStream() {
//	                                   
//	                                	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	                                	InputStream is = null;
//	                                	URL u = null;
//	                                	try {
//	                                		u =  new URL(imageUrl);
//	                                	  is = u.openStream();
//	                                	  
//	                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
//	                                	  int n;
//
//	                                	  while ( (n = is.read(byteChunk)) > 0 ) {
//	                                	    baos.write(byteChunk, 0, n);
//	                                	  }
//	                                	}
//	                                	catch (IOException e) {
//	                                	  System.err.printf ("Failed while reading bytes from %s: %s", u.toExternalForm(), e.getMessage());
//	                                	  e.printStackTrace ();
//	                                	  // Perform any other exception handling that's appropriate.
//	                                	}
//	                                	finally {
//	                                	  if (is != null) {
//	                                		  try
//	                                		  {
//	                                			  is.close();
//	                                		  }
//	                                		  catch(Exception e)
//	                                		  {
//	                                			  e.printStackTrace();
//	                                		  }
//	                                		  }
//	                                	}
//	                                	return new ByteArrayInputStream(baos.toByteArray());
//	                                }
//	                        };
//	                        StreamResource r = new StreamResource(source, bean.getFileName());
//	                        r.setMIMEType("application/x-msexcel");
//	                        r.setFilename(bean.getFileName());
//	                        r.setStreamSource(source);
//	                        r.setFilename(bean.getFileName());
//						    e.setSource(r);
//						    showPopup(e);

								
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
						else if(bean.getFileName().endsWith(".zip"))
						{

							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
								UI.getCurrent().getPage().open(imageUrl, "_blank");							
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
						else
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
								UI.getCurrent().getPage().open(imageUrl, "_blank");							
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
							
					}
				}		
			});
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		  }
		});
		
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
		//table.setFooterVisible(true);
		//table.setColumnFooter("category", String.valueOf("Total"));

	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	private void addListener() {
		btnAdd.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5852089491794014554L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				BeanItem<DMSDocumentDetailsDTO> addItem = container.addItem(new DMSDocumentDetailsDTO());
				
				manageListeners();
			}
		});
	}
	
	
	public List<DMSDocumentDetailsDTO> getValues() {
		@SuppressWarnings("unchecked")
		List<DMSDocumentDetailsDTO> itemIds = new ArrayList<DMSDocumentDetailsDTO>();
		if (this.table != null) {
			itemIds = (List<DMSDocumentDetailsDTO>) this.table.getItemIds();
		}

		return itemIds;
	}
	
	public void manageListeners() {

		for (DMSDocumentDetailsDTO quantumReductionDetailsDTO : tableItem.keySet()) {
			HashMap<String, AbstractField<?>> combos = tableItem.get(quantumReductionDetailsDTO);
			
		/*	final ComboBox classificationCombo = (ComboBox) combos.get("classification");
			final ComboBox categoryCombo = (ComboBox)combos.get("category");
			final TextField txtPerDay = (TextField) combos.get("perDayAmt");*/
		}
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			DMSDocumentDetailsDTO entryDTO = (DMSDocumentDetailsDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				
			if ("slNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("30px");
				//field.setWidth("125px");
				field.setNullRepresentation("");
			//	field.setReadOnly(true);
//				field.setEnabled(true);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setMaxLength(50);
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9 ]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("slNo", field);
				return field;
			}
			
			else if ("documentType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				tableRow.put("documentType", field);

				return field;
			}
			
			else if ("cashlessOrReimbursement".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setMaxLength(15);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(true);
				tableRow.put("cashlessOrReimbursement", field);
				return field;
				}
			else if ("fileName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setMaxLength(15);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(true);
				tableRow.put("fileName", field);
				return field;
				}
			else if ("documentCreatedDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("200px");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				//field.setMaxLength(15);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(true);
				tableRow.put("documentCreatedDateValue", field);
				return field;
				}
			
			
			
			else if ("documentSource".equals(propertyId)) {
				
				TextField field = new TextField();
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setData(entryDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setEnabled(false);
				field.setReadOnly(true);
				final TextField txt = (TextField) tableRow.get("slNo");
				generateSlNo(txt);
				tableRow.put("documentSource", field);
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
		
		Collection<DMSDocumentDetailsDTO> itemIds = (Collection<DMSDocumentDetailsDTO>) table.getItemIds();
		
		int i = 0;
		 for (DMSDocumentDetailsDTO quantumReductionDetailsDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(quantumReductionDetailsDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("slNo");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i)); 
					 //itemNoFld.setEnabled(false);
					 itemNoFld.setReadOnly(true);
				 }
			 }
		 }
		
	}
	
	 public void addBeanToList(DMSDocumentDetailsDTO dmsDocumentDetailsDTO) {
		 container.addItem(dmsDocumentDetailsDTO);
	    }
	
	 public boolean isValid()
		{
			return false;
		}
	 
	 public List<String> getErrors()
		{
			return this.errorMessages;
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

		public Table getTable() {
			return table;
		}

		public void setTable(Table table) {
			this.table = table;
		}	 
}