package com.shaic.claim.lumen.upload;

import java.util.HashMap;
import java.util.List;







import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.DocumentDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class LumenUploadedDocumentTable extends GBaseTable<DocumentTableDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"serialNumber","fileType","fileName","uploadedDate","uploadedBy","remove"}; 
	
	private List<DocumentDetails> listOfUserUploadedDocuments;
	
	public List<DocumentDetails> getListOfUserUploadedDocuments() {
		return listOfUserUploadedDocuments;
	}

	public void setListOfUserUploadedDocuments(
			List<DocumentDetails> listOfUserUploadedDocuments) {
		this.listOfUserUploadedDocuments = listOfUserUploadedDocuments;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<DocumentTableDTO>(DocumentTableDTO.class));

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
				    		/*ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {
												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														DocumentTableDTO resultObj  = (DocumentTableDTO) itemId;
														fireViewEvent(LumenUploadDocumentViewPresenter.DELETE_LUMEN_DOCUMENT, resultObj, getListOfUserUploadedDocuments());	
													} else {
														// User did not confirm
													}
												}
											});
				    		dialog.setClosable(false);*/
				    		
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
									DocumentTableDTO resultObj  = (DocumentTableDTO) itemId;
									fireViewEvent(LumenUploadDocumentViewPresenter.DELETE_LUMEN_DOCUMENT, resultObj, getListOfUserUploadedDocuments());
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
				}
				initiateLumen.addStyleName(BaseTheme.BUTTON_LINK);
				return initiateLumen;
			}
		});
		table.setColumnHeader("remove", "");
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setSizeFull();
	}

	@Override
	public void tableSelectHandler(DocumentTableDTO t) {
		// TODO Auto-generated method stub
	}

	@Override
	public String textBundlePrefixString() {
		return "lumen-document-upload-";
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
