package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.legal.processconsumerforum.page.advocatefee.SearchProcessAdvocateFeePresenter;
import com.shaic.claim.legal.processconsumerforum.page.advocatenotice.SearchProcessAdvocateNoticePresenter;
import com.shaic.claim.legal.processconsumerforum.page.ombudsman.SearchProcessOmbudsmanPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class SearchProcessConsumerForumTable extends GBaseTable<SearchProcessConsumerForumTableDTO> {
	
	private static final long serialVersionUID = 1L;
	
	public static Window popup;
	
	private String screenName;
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"Select","intimationNumber", "claimType", "cpuName", "policyNumber", "policyIssuingOffice",
			"productName","productType","classOfBussiness" };

	

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchProcessConsumerForumTableDTO>(
				SearchProcessConsumerForumTableDTO.class));
		generaterColumn();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
	}

	@Override
	public void tableSelectHandler(SearchProcessConsumerForumTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public String textBundlePrefixString() {
		return "consumer-forum-";
	}

	public void setWindowObject(Window popup, String screenName) {
		this.popup = popup;
		this.screenName =screenName;
	}
	
	private void generaterColumn(){
		table.removeGeneratedColumn("Select");
		
		table.addGeneratedColumn("Select", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnSelect = new Button("Select");
				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
				final SearchProcessConsumerForumTableDTO viewSearchCriteriaTableDTO = (SearchProcessConsumerForumTableDTO)itemId;
				btnSelect.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						if(screenName.equalsIgnoreCase(SHAConstants.LEGAL_CONSUMER)){
							fireViewEvent(SearchProcessConsumerForumPresenter.POPULATE_FIELD_VALUES,viewSearchCriteriaTableDTO);
						}
						if(screenName.equalsIgnoreCase(SHAConstants.LEGAL_ADVOCATE_NOTICE)){
							fireViewEvent(SearchProcessAdvocateNoticePresenter.POPULATE_FIELD_VALUES_ADVOCATE_NOTICE,viewSearchCriteriaTableDTO);
						}
						if(screenName.equalsIgnoreCase(SHAConstants.LEGAL_ADVOCATE_FEE)){
							fireViewEvent(SearchProcessAdvocateFeePresenter.POPULATE_FIELD_VALUES_ADVOCATE_FEE,viewSearchCriteriaTableDTO);
						}
						if(screenName.equalsIgnoreCase(SHAConstants.LEGAL_OMBUDSMAN)){
							fireViewEvent(SearchProcessOmbudsmanPresenter.POPULATE_FIELD_VALUES_OMBUDSMAN,viewSearchCriteriaTableDTO);
						}
						popup.close();
					}
				});
				return btnSelect;
			}
		});
		
		table.setColumnHeader("Select", "Select");
	}

}
