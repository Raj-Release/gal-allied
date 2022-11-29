package com.shaic.claim.preauth.wizard.pages;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PreAuthPreviousQueryDetailsTable extends
		GBaseTable<PreAuthPreviousQueryDetailsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject
	private PreAuthViewQueryDetailsPage preAuthViewQueryDetailsPage;

	@Inject
	private ViewDetails viewDetails;

	@Inject
	private PreAuthPreviousQueryDetailsTableView preAuthPreviousQueryDetailsTableView;

//	@PersistenceContext(unitName = "PERSISTENCE_UNIT_NAME", type = PersistenceContextType.EXTENDED)
//	protected EntityManager entityManager;

	private static final Object[] NATURAL_COL_ORDER = new Object[] { "queryNo",
			"hospitalName", "hospitalCity", "diagnosis", "queryRemarks",
			"queryRaisedRole", "designataion", "queryRaisedDate", "queryStatus" };
	
	private static final Object[] VISIBLE_COLUMN = new Object[] {"sno",
		"acknowledgementNumber", "rodNumber", "documentReceivedFrom", "billClassification",
		"queryRaisedRole", "designataion", "queryRaisedDate", "queryStatus"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<PreAuthPreviousQueryDetailsTableDTO>(
				PreAuthPreviousQueryDetailsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("viewStatus");
		table.setPageLength(3);
		table.removeGeneratedColumn("viewStatus");
		table.addGeneratedColumn("viewStatus", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				Button button = new Button("ViewDetails");
				button.addClickListener(new Button.ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO = (PreAuthPreviousQueryDetailsTableDTO) itemId;
						preAuthPreviousQueryDetailsTableView
								.init(preAuthPreviousQueryDetailsTableDTO);
						Window popup = new com.vaadin.ui.Window();
						popup.setWidth("75%");
						popup.setHeight("85%");
						popup.setContent(preAuthPreviousQueryDetailsTableView);
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

				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
	}
	
	public void setVisibleColumns(){
		table.setVisibleColumns(VISIBLE_COLUMN);
		
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("acknowledgementNumber", "Acknowledgement No");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("documentReceivedFrom", "Document Recieved");
		table.setColumnHeader("billClassification", "Bill Classification");
		table.setColumnHeader("queryRaisedRole", "Query Raised Role");
		table.setColumnHeader("designataion", "Designation");
		table.setColumnHeader("queryRaisedDate", "Query Raised Date");
		table.setColumnHeader("queryStatus", "Query Status");
		
		table.removeGeneratedColumn("viewStatus");
		table.addGeneratedColumn("viewStatus", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				Button button = new Button("ViewDetails");
				button.addClickListener(new Button.ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						PreAuthPreviousQueryDetailsTableDTO preAuthPreviousQueryDetailsTableDTO = (PreAuthPreviousQueryDetailsTableDTO) itemId;
						preAuthPreviousQueryDetailsTableView
								.init(preAuthPreviousQueryDetailsTableDTO);
						Window popup = new com.vaadin.ui.Window();
						popup.setWidth("75%");
						popup.setHeight("85%");
						popup.setContent(preAuthPreviousQueryDetailsTableView);
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

				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		
	}

	@Override
	public void tableSelectHandler(PreAuthPreviousQueryDetailsTableDTO t) {
		// TODO:
	}

	@Override
	public String textBundlePrefixString() {
		return "pre-auth-previous-query-details-";
	}

}
