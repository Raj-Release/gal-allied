package com.shaic.claim.reimbursement.searchuploaddocumentsforacknotreceived;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class UploadDocumentsForAckNotReceivedPageTable extends GEditableTable<UploadDocumentDTO>{
	
	private static final long serialVersionUID = -5524600977147253320L;
	private String presenterString = "";
	private Window popup;
	final Embedded imageViewer = new Embedded("Uploaded Image");

	public UploadDocumentsForAckNotReceivedPageTable() {
		super(UploadDocumentsForAckNotReceivedPageTable.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] {"referenceNoValue","fileTypeValue","fileName" };*/
	/*public static final Object[] OP_VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/
	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();


	static {
		
		fieldMap.put("referenceNoValue", new TableFieldDTO("referenceNoValue",TextField.class, String.class, false));
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		
	}*/

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void setPreseneterString(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void setVisibleColumns() {
		
		Object[] OP_VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };
		table.setVisibleColumns(OP_VISIBLE_COLUMNS);
		generateColumns();
		table.removeGeneratedColumn("view");
		table.addGeneratedColumn("view", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("View Details");
		    	editButton.setData(itemId);
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

							}
						
						}
						else if(bean.getFileName().endsWith(".PDF"))
						{
							final String imageUrl = SHAFileUtils.viewFileByToken(bean.getDmsDocToken());
							if(null != imageUrl)
							{
								Button saveExcel = new Button();
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
				}
					}		
			    });
		    	editButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		        return editButton;
		      }
		    });
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
		
		popup.setCaption("View Claim Wise RRC History");
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
	
	@Override
	public void initTable() {
	//	errorMessages = new ArrayList<String>();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"referenceNoValue","fileTypeValue","fileName" };
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("300px");
		table.setWidth("100%");
		
		generateColumns();
		
		//table.setPageLength(table.getItemIds().size());
	}

	private void generateColumns() {
		table.removeGeneratedColumn("edit");
		table.addGeneratedColumn("edit", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	final Button editButton = new Button("Edit");
		    	editButton.setData(itemId);
		    	editButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						table.removeItem(currentItemId);
						UploadDocumentDTO uploadDTO = (UploadDocumentDTO) itemId;
						uploadDTO.setIsEdit(true);
							fireViewEvent(UploadDocumentsForAckNotReceivedPagePresenter.EDIT_SEARCH_OR_UPLOADED_UPLOADED_DOCUMENTS, uploadDTO);
						
						
						
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
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	Object currentItemId = event.getButton().getData();
						ConfirmDialog dialog = ConfirmDialog
								.show(getUI(),
										"Confirmation",
										"Do you want to Delete ?",
										"No", "Yes", new ConfirmDialog.Listener() {

											public void onClose(ConfirmDialog dialog) {
												if (!dialog.isConfirmed()) {
													UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) itemId;
													if(null != uploadDocsDTO.getDocDetailsKey())
													{
														//billEntryDetailsDTO.setDeletedFlag("Y");
														deletedList.add(uploadDocsDTO);
													}
													uploadDocsDTO.setIsEdit(false);
													tableSelectHandler(uploadDocsDTO);
												} else {
													// User did not confirm
												}
											}
										});
						
						
			        } 
			    });
		    	deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
		        return deleteButton;
		      }
		    });
	}

	@Override
	public String textBundlePrefixString() {
		return "rod-uploaded-documents-";
	}

	@Override
	protected void newRowAdded() {

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
	 Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
			
			fieldMap.put("referenceNoValue", new TableFieldDTO("referenceNoValue",TextField.class, String.class, false));
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));			
	
		return fieldMap;
	}

	public void validateFields() {
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
		}

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
			fireViewEvent(UploadDocumentsForAckNotReceivedPagePresenter.DELETE_SEARCH_OR_UPLOADED_UPLOADED_DOCUMENTS,t);
				}
	
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
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
