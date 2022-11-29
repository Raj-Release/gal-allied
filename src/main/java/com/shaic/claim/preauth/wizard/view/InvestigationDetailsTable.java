package com.shaic.claim.preauth.wizard.view;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.DMSDocumentViewDetailsPage;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.InvestigationService;
import com.shaic.domain.MasterService;
import com.shaic.reimbursement.acknowledgeinvestigationcompleted.ViewInvestigationDetailsUI;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class InvestigationDetailsTable extends
		GBaseTable<InvestigationDetailsTableDTO> {

	private static final long serialVersionUID = 2632021578105738698L;
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "sNo",
			"investigatorName", "investigatorCode", "investigatorContactNo",
			"hospitalName", "investigationTriggerPoints",
			"investigationAssignedDate", "investigationCompletedDateStr",
			"investigationReportReceivedDate", "tat", "status" };*/
	
	@Inject
	private ViewInvestigationDetailsUI viewInvestigationDetailsUI;

	private DMSDocumentViewDetailsPage  dmsDocDetailsViewPage;
	
	private Window popup;
	@EJB
	private HospitalService hospitalService;
	@EJB
	private ClaimService claimService;
	
	@EJB
	private InvestigationService investigationService;
	@EJB
	private MasterService masterService;
	
	
	public void setViewDMSDocViewPage(DMSDocumentViewDetailsPage dmsDocDetailsViewPage){
		this.dmsDocDetailsViewPage = dmsDocDetailsViewPage;
	}
	
	public void setSeviceObjects(HospitalService hospitalService,ClaimService claimService,InvestigationService investigationService,MasterService masterService){
		this.hospitalService = hospitalService;
		this.claimService = claimService;
		this.investigationService = investigationService;
		this.masterService = masterService;
	}
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<InvestigationDetailsTableDTO>(
				InvestigationDetailsTableDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] { "sNo",
			"investigatorName", "investigatorCode", "investigatorContactNo",
			"hospitalName", "investigationTriggerPoints","investigationApprovedDate",
			"investigationAssignedDate", "investigationCompletedDateStr",
			"investigationReportReceivedDate", "tat", "status" };
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setWidth("100%");
		table.setHeight("200px");
		try {
			table.removeGeneratedColumn("viewDetails");
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
									InvestigationDetailsTableDTO investigationDetailsReimbursementTableDTO = (InvestigationDetailsTableDTO) itemId;
									viewInvestigationDetailsUI.setServiceObjects(hospitalService,claimService,investigationService,masterService);									
									viewInvestigationDetailsUI.setViewInvestigationDoc(dmsDocDetailsViewPage);
									viewInvestigationDetailsUI.initView(investigationDetailsReimbursementTableDTO.getInvestigAssignedKey(),investigationDetailsReimbursementTableDTO.getKey());
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
											System.out.println("Close listener called");
										}
									});

									popup.setModal(true);
									UI.getCurrent().addWindow(popup);
								}
							});
							return button;
						}
					});

			table.removeGeneratedColumn("viewInvAssignHistoryDetails");
			table.addGeneratedColumn("viewInvAssignHistoryDetails",
					new Table.ColumnGenerator() {
						private static final long serialVersionUID = 1L;

						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {
							Button button = new Button("Investigation Assigned History");
							button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
							button.addStyleName(ValoTheme.BUTTON_LINK);
							button.addClickListener(new Button.ClickListener() {

								@Override
								public void buttonClick(ClickEvent event) {
									InvestigationDetailsTableDTO investigationDetailsReimbursementTableDTO = (InvestigationDetailsTableDTO) itemId;
									Long invAssignKey = investigationDetailsReimbursementTableDTO.getInvestigAssignedKey();
									viewInvestigationDetailsUI.getInvHistoryTable(invAssignKey);
								}
							});
							return button;
						}
					});
			
		} catch (Exception e) {

		}
	}

	@Override
	public void tableSelectHandler(InvestigationDetailsTableDTO t) {
		// fireViewEvent(SearchEnhancementPresenter.SHOW_ENHANCEMENT_FORM, t);

	}

	@Override
	public String textBundlePrefixString() {
		return "view-investigationdetailstable-";
	}
}
