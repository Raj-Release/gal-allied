package com.shaic.claim.lumen.querytomis;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.DocumentDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class MISUploadedDocumentTable extends GBaseTable<DocumentAckTableDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","queryRemarks","replyRemarks","fileType","fileName","uploadedDate","remove"}; 
	
	private List<DocumentDetails> listOfUserUploadedDocuments;
	private List<DocumentAckTableDTO> listOfAckTableDocuments;
	
	public List<DocumentDetails> getListOfUserUploadedDocuments() {
		return listOfUserUploadedDocuments;
	}

	public void setListOfUserUploadedDocuments(List<DocumentDetails> listOfUserUploadedDocuments) {
		this.listOfUserUploadedDocuments = listOfUserUploadedDocuments;
	}
	
	public List<DocumentAckTableDTO> getListOfAckTableDocuments() {
		return listOfAckTableDocuments;
	}

	public void setListOfAckTableDocuments(List<DocumentAckTableDTO> listOfAckTableDocuments) {
		this.listOfAckTableDocuments = listOfAckTableDocuments;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<DocumentAckTableDTO>(DocumentAckTableDTO.class));

		table.removeGeneratedColumn("remove");
		table.addGeneratedColumn("remove",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button initiateLumen = new Button();{
					initiateLumen.setCaption("Delete");
					initiateLumen.addClickListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
				    		ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														DocumentAckTableDTO resultObj  = (DocumentAckTableDTO) itemId;
														//System.out.println("Document Details : "+getListOfUserUploadedDocuments());
														//System.out.println("Ack Details : "+getListOfAckTableDocuments());
														fireViewEvent(ProcessMISWizardPresenter.DELETE_MIS_LUMEN_DOCUMENT, resultObj, getListOfUserUploadedDocuments(), getListOfAckTableDocuments());	
													} else {
														// User did not confirm
													}
												}
											});
				    		dialog.setClosable(false);
						}
					});
				}
				initiateLumen.addStyleName(BaseTheme.BUTTON_LINK);
				return initiateLumen;
			}
		});
		table.setColumnHeader("remove", "");
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setWidth("100%");
		table.setPageLength(7);
	}

	@Override
	public void tableSelectHandler(DocumentAckTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-mis-document-upload-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
	}

}
