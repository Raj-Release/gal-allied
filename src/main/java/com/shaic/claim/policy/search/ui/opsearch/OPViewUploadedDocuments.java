package com.shaic.claim.policy.search.ui.opsearch;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAFileUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.UploadDocumentDTO;
import com.shaic.reimbursement.uploadrodreports.UploadDocumentPdfPage;
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

public class OPViewUploadedDocuments extends GBaseTable<UploadDocumentDTO>{
	
	@Inject
	private UploadDocumentPdfPage uploadDocumentPdfPage;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		table.removeAllItems();
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(
				UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {
				"sNo","fileName","fileTypeValue", "billDate", "billNo", "billAmt",  "deductibleAmt", "nonPaybleAmt", "billValue", "remarks" };
		
		table.removeGeneratedColumn("fileName");
		table.addGeneratedColumn("fileName",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						UploadDocumentDTO dto = (UploadDocumentDTO)itemId;
                        final String name = dto.getFileName();
                        final String token = dto.getDmsDocToken();
						Button button = new Button(name);
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								
								uploadDocumentPdfPage.init(name,token);
								Window popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								popup.setContent(uploadDocumentPdfPage);
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

						});
//						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setHeight("140px");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
	}

	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "op-uploaded-documents-";
	}
	

	
	/*private static final long serialVersionUID = -5524600977147253320L;
	private String presenterString = "";
	private Window popup;
	final Embedded imageViewer = new Embedded("Uploaded Image");
	private List<UploadDocumentDTO> deletedList = new ArrayList<UploadDocumentDTO>();
	
	private StringBuilder errMsg = new StringBuilder();

	public StringBuilder getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(StringBuilder errMsg) {
		this.errMsg = errMsg;
	}

	public OPViewUploadedDocuments() {
		super(OPViewUploadedDocuments.class);
		setUp();
	}

	public static final Object[] VISIBLE_COLUMNS = new Object[] { "rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };
	public static final Object[] OP_VISIBLE_COLUMNS = new Object[] { "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue" };

	public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	static {
		
		fieldMap.put("rodNo", new TableFieldDTO("rodNo",TextField.class, String.class, false));
		fieldMap.put("fileTypeValue", new TableFieldDTO("fileTypeValue",TextField.class, String.class, false));
		fieldMap.put("fileName", new TableFieldDTO("fileName",TextField.class, String.class, false));
		fieldMap.put("billNo", new TableFieldDTO("billNo", TextField.class,String.class, false));
		fieldMap.put("billDate", new TableFieldDTO("billDate", DateField.class,Date.class, false));
		fieldMap.put("noOfItems", new TableFieldDTO("noOfItems", TextField.class,Long.class, false));
		fieldMap.put("billValue", new TableFieldDTO("billValue", TextField.class,Long.class, false));
	}

	
	@Override
	public void removeRow() {
		table.removeAllItems();
	}
	
	public void setPreseneterString(String presenterString) {
		this.presenterString = presenterString;
	}

	
	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<UploadDocumentDTO>(UploadDocumentDTO.class));
		Object[] VISIBLE_COLUMNS = new Object[] {"rodNo", "fileTypeValue","fileName","billNo", "billDate", "noOfItems", "billValue"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
		table.setHeight("140px");
		table.setWidth("100%");

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

	

	public void setReference(Map<String, Object> referenceData) {
		super.setReference(referenceData);
	}
	

	@Override
	public void deleteRow(Object itemId) {
		this.table.getContainerDataSource().removeItem(itemId);
	}
	
	@Override
	public void tableSelectHandler(UploadDocumentDTO t) {

	}
	
	
	public void removeRow(UploadDocumentDTO uploadDocsDTO) {
		table.removeItem(uploadDocsDTO);
	}
	
	
	public List<UploadDocumentDTO> getDeletedDocumentList()
	{
		return deletedList;
	}*/



}
