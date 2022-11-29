package com.shaic.reimbursement.rod.allowReconsideration.search;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.rod.wizard.dto.ReconsiderRODRequestTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ViewDocumentDetailsDTO;
import com.shaic.reimbursement.manageclaim.reopenclaim.pageClaimLevel.ReopenClaimTableDTO;
import com.shaic.reimbursement.rod.createrod.search.SearchCreateRODTableDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class SearchAllowReconsiderationTable extends GBaseTable<SearchAllowReconsiderationTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo", "claimType","rodNo","policyNo", 
		"cpuCodeandName","rejectionDate", "rejectionReason","rejectionRemarks"}; 

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		// TODO Auto-generated method stub
		table.setContainerDataSource(new BeanItemContainer<SearchAllowReconsiderationTableDTO>(SearchAllowReconsiderationTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
//		table.setHeight("350px");
		table.addStyleName("generateColumnTable");
		table.removeGeneratedColumn("unCheck");
		table.addGeneratedColumn("unCheck", new Table.ColumnGenerator() {			
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CheckBox chkBox = new CheckBox();
				final SearchAllowReconsiderationTableDTO reconsiderDTO = (SearchAllowReconsiderationTableDTO) itemId;
				if(reconsiderDTO != null && reconsiderDTO.getIsRejected()!= null && reconsiderDTO.getIsRejected()) {
					chkBox.setEnabled(true);
				} else {
					chkBox.setEnabled(false);
				}
				chkBox.addValueChangeListener(new Property.ValueChangeListener() {
					
					@Override
					public void valueChange(ValueChangeEvent event) {
						List<SearchAllowReconsiderationTableDTO> items = (List<SearchAllowReconsiderationTableDTO>) table.getItemIds();
						
						boolean value = (Boolean) event.getProperty().getValue();
						if(value){
						reconsiderDTO.setAllowReconsiderFlag("Y");
						fireViewEvent(SearchAllowReconsiderationPresenter.ALLOW_RECONSIDERATION,reconsiderDTO);
						} else {
							fireViewEvent(SearchAllowReconsiderationPresenter.ALLOW_RECONSIDERATION,reconsiderDTO);
						}
					}
				});
//				if(null != reconsiderDTO.getSelect() && reconsiderDTO.getSelect()) 
//				{
//					chkBox.setValue(reconsiderDTO.getSelect());
//				}
				return chkBox;
		}
		});	
		table.setColumnHeader("unCheck", "Uncheck");
	}
	
	public void alertMessage(final SearchAllowReconsiderationTableDTO t, String message) {

   		Label successLabel = new Label(
				"<b style = 'color: red;'>"+ message + "</b>",
				ContentMode.HTML);
   		Button homeButton = new Button("ok");
		homeButton.setData(t);
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");

		final ConfirmDialog dialog = new ConfirmDialog();
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
			}
		});
	}

	@Override
	public void tableSelectHandler(SearchAllowReconsiderationTableDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-allow-reconsider-";
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
//		if(length>=5){
//			table.setPageLength(5);
//		}
	}
	
	 public List<SearchAllowReconsiderationTableDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<SearchAllowReconsiderationTableDTO> itemIds = (List<SearchAllowReconsiderationTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}
