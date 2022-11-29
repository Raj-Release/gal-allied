package com.shaic.reimbursement.acknowledgeinvestigationcompleted;

import java.util.ArrayList;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InvestigationDetailsTable extends
		GBaseTable<InvestigationDetailsReimbursementTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long investigationKey = 0l;
	
	private Long investigationassignKey = 0l;

	@Inject
	private ViewInvestigationDetailsUI viewInvestigationDetailsUI;

	
	private HospitalService hospitalService;
	
	private ClaimService claimService;
	
	private InvestigationService investigationService;
	
	private MasterService masterService;
	
	private DMSDocumentViewDetailsPage dmsDocDetailsViewPage;
	
	private Window popup;


	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "select","sno",

			"investigatorName", "investigatorCode", "investigatorContactNo",
			"hospitalName", "remarks", "investigationAssignedDate",
			"investigatorStatus" };*/
	
	public static final Object[] VISIBLE_COL_ORDER = new Object[] {"select","sno",
		"investigatorName", "investigatorCode", "investigatorContactNo",
		"hospitalName", "remarks", "investigationAssignedDate","investigationCompletedDate",
		"investigatorStatus"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		table.setContainerDataSource(new BeanItemContainer<InvestigationDetailsReimbursementTableDTO>(
				InvestigationDetailsReimbursementTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "select","sno",

			"investigatorName", "investigatorCode", "investigatorContactNo",
			"hospitalName", "remarks", "investigationAssignedDate",
			"investigatorStatus" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("200px");
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
		final Object selectedRowId = getSelectedRowId(itemIds, investigationassignKey);
		System.out.print(";;;;;;;;;;;;;;;;;; selected id = " + selectedRowId);

		try {
			table.removeGeneratedColumn("viewDetails");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("viewDetails",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							Button button = new Button("View Details");
							button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
							button.addStyleName(ValoTheme.BUTTON_LINK);
							button.addClickListener(new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO = (InvestigationDetailsReimbursementTableDTO) itemId;
									viewInvestigationDetailsUI.setServiceObjects(hospitalService,claimService,investigationService,masterService);									
									viewInvestigationDetailsUI.setViewInvestigationDoc(dmsDocDetailsViewPage);
									viewInvestigationDetailsUI.init(investigationDetailsReimbursementTableDTO);
									popup = new com.vaadin.ui.Window();
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewInvestigationDetailsUI);
									popup.setCaption("View Investigation Details");
									popup.setClosable(true);
									popup.center();
									popup.setResizable(true);
									popup.addCloseListener(new Window.CloseListener() {
										/**
										 * 
										 */
										private static final long serialVersionUID = 1L;

										@Override
										public void windowClose(CloseEvent e) {
											System.out
													.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}
							});
							return button;
						}
					});
		} catch (Exception e) {

		}

	}

	@Override
	public void tableSelectHandler(InvestigationDetailsReimbursementTableDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		return "view-reimbursement-investigationdetailstable-";
	}

	public void setRadioButtonr(Long investigationassignedKey) {
		this.investigationassignKey = investigationassignedKey;
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds());
		final Object selectedRowId = getSelectedRowId(itemIds, investigationassignedKey);
		System.out.print(";;;;;;;;;;;;;;;;;; selected id = " + selectedRowId);
		try {
			table.removeGeneratedColumn("select");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("select", new Table.ColumnGenerator() {

				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {
					InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO = selectedRowId != null ? (InvestigationDetailsReimbursementTableDTO) selectedRowId : null;
					Long key1 = investigationDetailsReimbursementTableDTO != null ? investigationDetailsReimbursementTableDTO
							.getInvestigationAssignedKey() : null;
					investigationDetailsReimbursementTableDTO = itemId != null ? (InvestigationDetailsReimbursementTableDTO) itemId : null;
					Long key2 = investigationDetailsReimbursementTableDTO != null ? investigationDetailsReimbursementTableDTO
							.getInvestigationAssignedKey() : null;

					OptionGroup radioButton = new OptionGroup();
					if (key1 != null && key2 != null && key1.equals(key2)) {
						radioButton.addItem("");
						radioButton.select("");
					} else {
						radioButton.addItem("");
						radioButton.select(false);
					}
					radioButton.setEnabled(false);
					return radioButton;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setVisibleColumns(){
		
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setColumnHeader("investigationCompletedDate", "Investigation Completed Date");
		table.setColumnHeader("viewDetails", "View Details");
		
		try {
			table.removeGeneratedColumn("viewDetails");
		} catch (Exception e) {

		}
		try {
			table.addGeneratedColumn("viewDetails",
					new Table.ColumnGenerator() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							
							Button button = new Button("View Details");
							button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
							button.addStyleName(ValoTheme.BUTTON_LINK);
							button.addClickListener(new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO = (InvestigationDetailsReimbursementTableDTO) itemId;
									viewInvestigationDetailsUI.setServiceObjects(hospitalService,claimService,investigationService,masterService);									
									viewInvestigationDetailsUI.setViewInvestigationDoc(dmsDocDetailsViewPage);
									viewInvestigationDetailsUI.initView(investigationDetailsReimbursementTableDTO.getInvestigationAssignedKey(),investigationDetailsReimbursementTableDTO.getKey());
									popup = new Window();
									popup.setWidth("75%");
									popup.setHeight("75%");
									popup.setContent(viewInvestigationDetailsUI);
									popup.setCaption("View Investigation Details");
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
											System.out
													.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}
							});
							return button;
						}
					});
		} catch (Exception e) {

		}
	}

	private Object getSelectedRowId(ArrayList<Object> ids, Long key) {

		for (Object id : ids) {
			InvestigationDetailsReimbursementTableDTO investigationDetailsReimbursementTableDTO = (InvestigationDetailsReimbursementTableDTO) id;
			Long key1 = investigationDetailsReimbursementTableDTO.getInvestigationAssignedKey();
			if (key1 != null && key1.equals(key)) {
				return id;
			}
		}

		return null;

	}

	public void setSeviceObjects(HospitalService hospitalService,ClaimService claimService,InvestigationService investigationService,MasterService masterService){
		this.hospitalService = hospitalService;
		this.claimService = claimService;
		this.investigationService = investigationService;
		this.masterService = masterService;
	}
	
	public void setViewDMSDocViewPage(DMSDocumentViewDetailsPage dmsDocDetailsViewPage){
		this.dmsDocDetailsViewPage = dmsDocDetailsViewPage;
	}
}
