package com.shaic.claim.reimbursement.searchuploaddocuments;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.shaic.reimburement.addAdditinalDocument.search.SearchAddAdditionalDocumentTable;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class SearchUploadDocumentsViewImpl extends AbstractMVPView implements SearchUploadDocumentsView{

	@Inject
	private SearchUploadDocumentsForm searchForm;
	
	@Inject
	private SearchUploadDocumentsTable searchResultTable;
	
	private BeanItemContainer<SelectValue> dropDownType;
	
	private SearchUploaddocumentsFormDTO searchDTO;
	
private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		mainPanel.setSecondComponent(searchResultTable);
		mainPanel.setSplitPosition(35);
		setHeight("700px");
		//mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	
	
	
	@Override
	public void doSearch() {
		String err=searchForm.validate();
		if(err == null)
		{
		searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);		
		fireViewEvent(SearchUploaddocumentsPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		}
		else
		{
			showErrorMessage(err);
		}
			
	}

	private void showErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchAddAdditionalDocumentTable)
			{
				((SearchAddAdditionalDocumentTable) comp).removeRow();
			}
		}
	
	}

	@Override
	public void resetView() {
		searchForm.refresh(); 
		searchForm.setDropDownValues(this.dropDownType);
		
	}

	@Override
	public void list(Page<SearchUploadDocumentsTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			if(null != searchDTO && (null != searchDTO.getIntimationNo() && !("").equalsIgnoreCase(searchDTO.getIntimationNo())) && (null == searchDTO.getAckNo() || ("").equalsIgnoreCase(searchDTO.getAckNo())))
			{
				fireViewEvent(SearchUploaddocumentsPresenter.SEARCH_INVALID_INTIMATION, searchDTO.getIntimationNo());
			}
			else if(null != searchDTO && (null != searchDTO.getAckNo() && !("").equalsIgnoreCase(searchDTO.getAckNo())) && (null == searchDTO.getIntimationNo() || ("").equalsIgnoreCase(searchDTO.getIntimationNo())))

			{
				fireViewEvent(SearchUploaddocumentsPresenter.SEARCH_INVALID_ACK_NO, searchDTO.getAckNo());
			}
			else if(null != searchDTO && (null != searchDTO.getAckNo() && !("").equalsIgnoreCase(searchDTO.getAckNo())) && (null != searchDTO.getIntimationNo() && !("").equalsIgnoreCase(searchDTO.getIntimationNo())))

			{
				buildSuccessMessageLayout("No records found");
				//fireViewEvent(SearchUploaddocumentsPresenter.SEARCH_INVALID_ACK_NO, searchDTO.getAckNo());
			}
		}
			
	}

	@Override
	public void buildSuccessMessageLayout(String message)
	{
		/*Label successLabel = new Label("<b style = 'color: black;'><br>Acknowledgement is already mapped to ROD. <br> "
				+ "<br> Please use Add Additional Documents menu for uploading documents against the ROD created.<br></b>", ContentMode.HTML);	*/
		Label successLabel = new Label("<b style = 'color: black;'><br>"+message+"</b>", ContentMode.HTML);
		
		Button homeButton = new Button("Search/Upload Documents");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
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
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				fireViewEvent(MenuItemBean.SEARCH_OR_UPLOAD_DOCUMENTS, null);
				
			}
		});
		//searchForm.enableButtons();
	}

	@Override
	public void init(BeanItemContainer<SelectValue> type) {
		this.dropDownType = type;

		searchForm.setDropDownValues(type);

	}
	


}
