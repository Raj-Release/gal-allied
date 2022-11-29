package com.shaic.claim;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.viewEarlierRodDetails.Page.ViewRejectionDetailsPage;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;


public class RejectionDetailsTable extends GBaseTable<ReimbursementRejectionDetailsDto>{
	
		private static final long serialVersionUID = 1L;

		@Inject
		private Instance<ViewRejectionDetailsPage> rejectionDetailPageInstance;
				
		private ViewRejectionDetailsPage rejectionDetailPage;
		////private static Window popup;

		private static final Object[] NATURAL_COL_ORDER = new Object[] {"select","rejectionNo","acknowledgementNo", "rodNo", "documentReceivedFrom", "billClassification", 
				"rejectedByRole", "rejectedDate", "rejectionStatus" };

		@Override
		public void removeRow() {
			// TODO Auto-generated method stub
			table.removeAllItems();
		}
		
		@Override
		public void initTable() {
			
			table.removeAllItems();
			table.setWidth("100%");
			table.setContainerDataSource(new BeanItemContainer<ReimbursementRejectionDetailsDto>(
					ReimbursementRejectionDetailsDto.class));
			table.setVisibleColumns(NATURAL_COL_ORDER);
			table.setPageLength(table.size() + 4);
			table.setHeight("200px");
			
			table.removeGeneratedColumn("select");
			table.addGeneratedColumn("select",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(Table source, final Object itemId,
								Object columnId) {
							return ((ReimbursementRejectionDetailsDto)itemId).getSelect();
						}
						});			
			
			table.removeGeneratedColumn("viewDetails");
			table.addGeneratedColumn("viewDetails",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(Table source, final Object itemId,
								Object columnId) {
							Button button = new Button("View Details");
							button.addClickListener(new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									Window popup = new com.vaadin.ui.Window();
									
									
									popup.setCaption("View Rejection Details");
									popup.setWidth("75%");
									popup.setHeight("85%");
									Long rejectionKey = ((ReimbursementRejectionDetailsDto)itemId).getRejectionKey();
									
									ReimbursementRejectionDetailsDto dto = (ReimbursementRejectionDetailsDto)itemId;
									
									rejectionDetailPage = rejectionDetailPageInstance.get();
									rejectionDetailPage.init(rejectionKey);
									rejectionDetailPage.init(rejectionKey,dto);
									popup.setContent(rejectionDetailPage);
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
//											System.out.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}

							});
							button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					    	button.setWidth("150px");
					    	button.addStyleName(ValoTheme.BUTTON_LINK);
							return button;
						}
			});			
			table.setSelectable(true);
		}

		@Override
		public void tableSelectHandler(ReimbursementRejectionDetailsDto t) {
			table.select(t);
		}

		@Override
		public String textBundlePrefixString() {
			return "claim-rejection-details-";
		}

}
