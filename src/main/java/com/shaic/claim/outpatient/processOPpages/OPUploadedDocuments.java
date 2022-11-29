/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GEditableTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.outpatient.processOP.pages.assessmentsheet.OPClaimAssessmentPagePresenter;
import com.shaic.claim.outpatient.registerclaim.dto.OutPatientDTO;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.claim.rod.wizard.pages.CreateRODUploadDocumentsPresenter;
import com.shaic.claim.rod.wizard.tables.UploadedDocumentsTable;
import com.shaic.paclaim.rod.createrod.search.PACreateRODUploadDocumentsPresenter;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.DateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

/**
 * @author ntv.narasimhaj
 *
 */
public class OPUploadedDocuments extends GEditableTable<UploadDocumentDTO>{
	
	private static final long serialVersionUID = -5524600977147253320L;
	private String presenterString = "";
	private Window popup;
	final Embedded imageViewer = new Embedded("Uploaded Image");
	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	
	private Table approvedAmtTable;
	
	private ProcessOPBillDetailsPage parentObj;
	
	private OutPatientDTO opDTO;
	
	private StringBuilder errMsg = new StringBuilder();

	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}

	public OPUploadedDocuments() {
		super(OPUploadedDocuments.class);
		setUp();
	}

	/*public static final Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/
	/*public static final Object[] OP_VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };*/

/*	public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
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
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		//table.setWidth("80%");
		//Adding the height for procedure table.
		table.setHeight("140px");
		table.setWidth("100%");
		
		generateColumns();
		
		//table.setPageLength(table.getItemIds().size());
	}
	
	public void loadTableColumnsAsPerScreen(String argPresenterString){
		Object[] VISIBLE_COLUMNS =  null;
		if(SHAConstants.OUTPATIENT_FLAG.equalsIgnoreCase(argPresenterString)) {
			VISIBLE_COLUMNS = new Object[] {"fileName","fileTypeValue", "billDate", "billNo", "billAmt",  "deductibleAmt", "nonPaybleAmt", "billValue", "remarks"};
		}else{
			VISIBLE_COLUMNS = new Object[] {"rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue"};
		}
		table.setColumnHeader("billAmt", "Bill Amount");
		table.setColumnHeader("deductibleAmt", "Deductible Amount");
		table.setColumnHeader("nonPaybleAmt", "Non Payable Amount");
		table.setColumnHeader("remarks", "Remarks");
		table.setVisibleColumns(VISIBLE_COLUMNS);
		generateColumns();
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
					 if(SHAConstants.OUTPATIENT_FLAG.equalsIgnoreCase(presenterString)) {
//							fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
							fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_EDIT_UPLOADED_DOCUMENTS, uploadDTO);
							if(parentObj != null && parentObj.getTable() != null){
								parentObj.getTable().setVisible(false);
							}
						opDTO.setApprove(false);
						opDTO.setReject(false);
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
		    	UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) itemId;
		    	if(uploadDocsDTO != null && uploadDocsDTO.getIsPhysicalDoc()){
		    		deleteButton.setEnabled(false);
		    	}
		    	deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
			        	//Object currentItemId = event.getButton().getData();
						/*ConfirmDialog dialog = ConfirmDialog
								.show(getUI(),
										"Confirmation",
										"Do you want to Delete ?",
										"No", "Yes", new ConfirmDialog.Listener() {

											public void onClose(ConfirmDialog dialog) {
												if (!dialog.isConfirmed()) {
													UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) itemId;
													uploadDocsDTO.setIsEdit(false);
													tableSelectHandler(uploadDocsDTO);
													if(null != uploadDocsDTO.getDocSummaryKey())
													{
														//billEntryDetailsDTO.setDeletedFlag("Y");
														deletedList.add(uploadDocsDTO);
													}
												} else {
													// User did not confirm
												}
											}
										});*/
						
						
						HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
						buttonsNamewithType.put(GalaxyButtonTypesEnum.YES.toString(), "Yes");
						buttonsNamewithType.put(GalaxyButtonTypesEnum.NO.toString(), "No");
						HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
								.createConfirmationbox("Do you want to Delete ?", buttonsNamewithType);
						Button yesButton = messageBoxButtons.get(GalaxyButtonTypesEnum.YES
								.toString());
						Button noButton = messageBoxButtons.get(GalaxyButtonTypesEnum.NO
								.toString());
						yesButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {

								UploadDocumentDTO uploadDocsDTO = (UploadDocumentDTO) itemId;
								uploadDocsDTO.setIsEdit(false);
								if(parentObj != null && parentObj.getTable() != null){
									parentObj.getTable().setVisible(false);
								}
								opDTO.setApprove(false);
								opDTO.setReject(false);
								tableSelectHandler(uploadDocsDTO);
								if(null != uploadDocsDTO.getDocSummaryKey())
								{
									//billEntryDetailsDTO.setDeletedFlag("Y");
									deletedList.add(uploadDocsDTO);
									
								}
							
							}
							});
						noButton.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 7396240433865727954L;

							@Override
							public void buttonClick(ClickEvent event) {
								
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
			
			fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
			fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
			fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
			fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
			fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
			fieldMap.put("billAmt", new TableFieldDTO("billAmt", TextField.class,Long.class, false));
			fieldMap.put("deductibleAmt", new TableFieldDTO("deductibleAmt", TextField.class,Long.class, false));
			fieldMap.put("nonPaybleAmt", new TableFieldDTO("nonPaybleAmt", TextField.class,Long.class, false));
			fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
			fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,Long.class, false));
		return fieldMap;
	}

	public void validateFields() {
		errMsg.setLength(0);
		Collection<?> itemIds = table.getItemIds();
		for (Object object : itemIds) {
			UploadDocumentDTO rec = (UploadDocumentDTO) object;
			if(rec.getFileType() == null){
				errMsg.append("Please select the FileType </br>");
			}
			
			if(StringUtils.isBlank(rec.getFileName())){
				errMsg.append("Please fill the FileName </br>");
			}
			
			if(StringUtils.isBlank(rec.getBillNo())){
				errMsg.append("Please fill the Bill No </br>");
			}
			
			if(rec.getBillDate() == null){
				errMsg.append("Please fill the Bill Date </br>");
			}
			
			if(rec.getNoOfItems() == null){
				errMsg.append("Please fill No of Items </br>");
			}
			
			if(rec.getBillValue() == null){
				errMsg.append("Please fill Bill value </br>");
			}
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
		if(SHAConstants.OUTPATIENT_FLAG.equalsIgnoreCase(presenterString)) {
//			fireViewEvent(OPRODAndBillEntryPagePresenter.BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS, t);
			fireViewEvent(OPClaimAssessmentPagePresenter.OP_BILL_ENTRY_DELETE_UPLOADED_DOCUMENTS, t);

		}
		
	}
	
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}
	
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}

	public void setApprovedAmtTable(Table approvedAmtTable) {
		this.approvedAmtTable = approvedAmtTable;
	}

	public void setOpDTO(OutPatientDTO opDTO) {
		this.opDTO = opDTO;
	}

	public void setParentObj(ProcessOPBillDetailsPage parentObj) {
		this.parentObj = parentObj;
	}
	
	/*
	public void addBeanToList(UploadDocumentDTO dto) {
    	//container.addBean(uploadDocumentsDTO);
	 container.addItem(dto);

//    	data.addItem(pedValidationDTO);
    	//manageListeners();
    }*/
}
