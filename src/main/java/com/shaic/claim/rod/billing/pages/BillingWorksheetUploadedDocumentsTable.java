/**
 * 
 */
package com.shaic.claim.rod.billing.pages;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalSplitPanel;
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
public class BillingWorksheetUploadedDocumentsTable extends GEditableTable<UploadDocumentDTO> {
	
	//private List<String> errorMessages;
	
	
	public BillingWorksheetUploadedDocumentsTable() {
		super(BillingWorksheetUploadedDocumentsTable.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileName", "billingWorkSheetRemarks","createdBy","createdDate" };*/
	
	public String presenterString;
	
	private Window popup;
	
	final Embedded imageViewer = new Embedded("Uploaded Image");
	HorizontalSplitPanel hsplitPanel = new HorizontalSplitPanel();
	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();

	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billingWorkSheetRemarks", new TableFieldDTO("billingWorkSheetRemarks",TextField.class, String.class, false));
		fieldMap.put("createdBy", new TableFieldDTO("createdBy",TextField.class, String.class, false));
		fieldMap.put("createdDate", new TableFieldDTO("createdDate",DateField.class, Date.class, false));
		
	}*/
	
	public void initPresenter(String presenterString)
	{
		this.presenterString = presenterString;
	}

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	

	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		
		
		
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileName", "billingWorkSheetRemarks","createdBy","createdDate" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("View Details");
		    	editButton.setData(itemId);
		    	editButton.setStyleName(ValoTheme.BUTTON_LINK);
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						UploadDocumentDTO bean = (UploadDocumentDTO)itemId;
						 
						if(null != bean.getFileName())
						{
						if(bean.getFileName().endsWith(".JPG"))
						{
							String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(imageUrl != null) {
								imageViewer.setSource(new ExternalResource(imageUrl));
							    imageViewer.setVisible(true);  
							    imageViewer.setHeight("500px");
							    /*Panel imagePanel = new Panel();
							    imagePanel.setContent(imageViewer);*/
							    showPopup(imageViewer);
							 //   hsplitPanel.setFirstComponent(imagePanel);	
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
							  /*  Panel imagePanel = new Panel();
							    imagePanel.setContent(imageViewer);*/
							   // hsplitPanel.setFirstComponent(imagePanel);	
//								final String url = System.getProperty("jboss.server.data.dir") + "/"
//										+"SampleBill.JPG";
//								imageViewer.setSource(new ExternalResource(url));
//							    imageViewer.setVisible(true);  
//							    imageViewer.setHeight("500px");
//							    Panel imagePanel = new Panel();
//							    imagePanel.setContent(imageViewer);
//							    hsplitPanel.setFirstComponent(imagePanel);	

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
						
			        	/*Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
						fireViewEvent(CreateRODUploadDocumentsPresenter.EDIT_UPLOADED_DOCUMENTS, uploadDTO);*/
						
						
			        }
						else if(bean.getFileName().endsWith(".PDF") || bean.getFileName().endsWith(".pdf"))
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
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
						        r.setMIMEType("application/pdf");
						        e.setSource(r);
					*/
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
//				                     		u =  new URL(imageUrl);
//				                     	  is = u.openStream();
				                     		u =  new URL(imageUrl);
			            					urlConnection =  u.openConnection();
			            					is = urlConnection.getInputStream();
				                     	  
//				                     	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
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
				             r.setMIMEType("application/pdf");
				             r.setStreamSource(source);
							    e.setSource(r);
								showPopup(e);
								//hsplitPanel.setFirstComponent(e);
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
								StreamResource r = new StreamResource(s, bean.getFileName());
						        r.setMIMEType("application/pdf");
						        e.setSource(r);
						        showPopup(e);
								//hsplitPanel.setFirstComponent(e);
								//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
							}
							//Added for dms
						}
						
						else if(bean.getFileName().endsWith(".xlsx") || bean.getFileName().endsWith(".xls"))
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
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
							//	DownloadStream r = new DownloadStream(s.getStream(), "application/x-msexcel", bean.getFileName());
								
							//	ndow w = new Window();
								
								StreamResource r = new StreamResource(s, bean.getFileName());
						      //  r.setMIMEType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
								//r.setCacheTime(1000);
								r.setMIMEType("application/x-msexcel");
						        e.setSource(r);*/
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
//	                                		u =  new URL(imageUrl);
//	                                	  is = u.openStream();
	                                		
	                                		u =  new URL(imageUrl);
			            					urlConnection =  u.openConnection();
			            					is = urlConnection.getInputStream();
	                                	  
//	                                	  byte[] byteChunk = new byte[100000]; // Or whatever size you want to read in at a time.
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
	                        r.setMIMEType("application/x-msexcel");
	                        r.setStreamSource(source);
						    e.setSource(r);

						        showPopup(e);
								//hsplitPanel.setFirstComponent(e);
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
								StreamResource r = new StreamResource(s, bean.getFileName());
						      //  r.setMIMEType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
								r.setMIMEType("application/x-msexcel");
						        e.setSource(r);
						        showPopup(e);
								//hsplitPanel.setFirstComponent(e);
								//Notification.show("Error", "" + "Sorry, the requested file does not exist in the server.", Type.ERROR_MESSAGE);
							}
							//Added for dms
						}
				}
					}		
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
		
		table.removeGeneratedColumn("delete");
		table.addGeneratedColumn("delete", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button deleteButton = new Button("Delete");
		    	deleteButton.setData(itemId);
		    	deleteButton.setIcon(FontAwesome.TRASH_O);
		    	deleteButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        /*	Object currentItemId = event.getButton().getData();
					//	table.removeItem(currentItemId);
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) itemId;
						tableSelectHandler(uploadDocsDTO);*/
						
						Object currentItemId = event.getButton().getData();
						
						UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) currentItemId;
						
						//BillEntryDetailsDTO billEntryDetailsDTO = (BillEntryDetailsDTO) currentItemId;
						if(null != uploadDocsDTO.getRodBillSummaryKey())
						{
							//billEntryDetailsDTO.setDeletedFlag("Y");
							deletedList.add(uploadDocsDTO);
						}
						table.removeItem(currentItemId);
						tableSelectHandler(uploadDocsDTO);
						
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
		
		//table.setPageLength(table.getItemIds().size());
	}

	@Override
	public String textBundlePrefixString() {
		return "billing-worksheet-uploaded-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		
		Map<String, TableFieldDTO> fieldMap = new WeakHashMap<String, TableFieldDTO>();

		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billingWorkSheetRemarks", new TableFieldDTO("billingWorkSheetRemarks",TextField.class, String.class, false));
		fieldMap.put("createdBy", new TableFieldDTO("createdBy",TextField.class, String.class, false));
		fieldMap.put("createdDate", new TableFieldDTO("createdDate",DateField.class, Date.class, false));
			
		return fieldMap;
	}

	public void validateFields() {
		/*Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}*/

	}

	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		
		fireViewEvent(BillingWorksheetUploadDocumentsPresenter.DELETE_BILLING_UPLOADED_DOCUMENTS,t);
		
		/*if((SHAConstants.ZONAL_REVIEW_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
		 
		}
		else if ((SHAConstants.CLAIM_REQUEST_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
			
		}
		else if((SHAConstants.CLAIM_BILLING_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
			
		}
		else if ((SHAConstants.CLAIM_FINANCIAL_BILLING_WORKSHEET).equalsIgnoreCase(this.presenterString))
		{
			
		}*/
		
		//fireViewEvent(CreateRODUploadDocumentsPresenter.DELETE_UPLOADED_DOCUMENTS,t);
		
	}
	
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
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
		popup.setResizable(false);
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
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}
	
	/*
	public void addBeanToList(UploadDocumentDTO dto) {
    	//container.addBean(uploadDocumentsDTO);
	 container.addItem(dto);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
    }*/
}



