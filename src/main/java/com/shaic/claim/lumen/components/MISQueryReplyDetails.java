package com.shaic.claim.lumen.components;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.searchcoordinator.ProcessCoordinatorWizardPresenter;
import com.shaic.claim.lumen.searchlevelone.ProcessLevelOneWizardPresenter;
import com.shaic.claim.lumen.searchleveltwo.ProcessLevelTwoWizardPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class MISQueryReplyDetails extends GBaseTable<MISQueryReplyDTO>{

	private final static Object[] NATURAL_HDCOL_SEARCH_ORDER = new Object[]{"queryRaisedBy","queryRaisedRole","queryRaisedDate","replyReceivedFrom","repliedDate","add"}; 
	
	private String screenName;
	
	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {

		getLayout().setComponentAlignment(getCaptionLayout(), Alignment.TOP_LEFT);		
		table.setContainerDataSource(new BeanItemContainer<MISQueryReplyDTO>(MISQueryReplyDTO.class));
		table.removeGeneratedColumn("add");
		table.addGeneratedColumn("add",
				new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				final Button initiateLumen = new Button();{
					initiateLumen.setCaption("View Details");
					initiateLumen.addClickListener(new Button.ClickListener() {
						public void buttonClick(ClickEvent event) {
							MISQueryReplyDTO resultObj  = (MISQueryReplyDTO) itemId;
							if(getScreenName().equals("ProcessLevel1")){
								fireViewEvent(ProcessLevelOneWizardPresenter.LEVEL_I_MIS_DETAILS, resultObj);	
							}
							
							if(getScreenName().equals("ProcessCoordinator")){
								fireViewEvent(ProcessCoordinatorWizardPresenter.COORDINATOR_MIS_DETAILS, resultObj);	
							}
							
							if(getScreenName().equals("ProcessLevel2")){
								fireViewEvent(ProcessLevelTwoWizardPresenter.LEVEL_II_MIS_DETAILS, resultObj);	
							}
						}
					});
				}
				initiateLumen.addStyleName(BaseTheme.BUTTON_LINK);
				return initiateLumen;
			}
		});
		table.setColumnHeader("add", "");
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
		table.setPageLength(5);
	}

	@Override
	public void tableSelectHandler(MISQueryReplyDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "mis-query-reply-";
	}

	public void setSubmitTableHeader(){
		table.setVisibleColumns(NATURAL_HDCOL_SEARCH_ORDER);
	}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
	}

}
