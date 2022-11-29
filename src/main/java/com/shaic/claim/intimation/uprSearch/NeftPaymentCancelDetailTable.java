package com.shaic.claim.intimation.uprSearch;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentModeTrailTable;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class NeftPaymentCancelDetailTable extends GBaseTable<ClmPaymentCancelDto>{
	
	@Inject
	private ViewSettlementDetailsTable settlementTable;
	
	private final static Object COLUM_HEADER_CANCEL_UPR_DETAILS[] = new Object[] {
		"serialNumber","rodNumber","remarks","cancelDate"};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<ClmPaymentCancelDto> paymentDtoContainer = new BeanItemContainer<ClmPaymentCancelDto>(ClmPaymentCancelDto.class);
	
	    table.setContainerDataSource(paymentDtoContainer);
	    table.setVisibleColumns(COLUM_HEADER_CANCEL_UPR_DETAILS);
	    tablesize();
		
	}

	@Override
	public void tableSelectHandler(ClmPaymentCancelDto t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "cancelUpr-";
	}
	
	@SuppressWarnings("deprecation")
	public void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(3);
		}
		
	}
	
	public void getErrorMessage(String eMsg) {

		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}
	
	public void generateViewSettlementColumn()
	{
		table.removeGeneratedColumn("Settled Against");
		table.addGeneratedColumn("Settled Against", new Table.ColumnGenerator() {
		
		private static final long serialVersionUID = 1L;

			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				Button button = new Button("Details");
				button.addClickListener(new Button.ClickListener() {
		
					@Override
					public void buttonClick(ClickEvent event) {
						ClmPaymentCancelDto paymentCancelDto = (ClmPaymentCancelDto)itemId;
						fireViewEvent(SearchIntimationUPRDetailPresenter.GET_SETTLEMENT_DETAILS , paymentCancelDto);
					}
				});
				button.setStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.setStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
	}
	
	public void showSettlementDetails(PaymentProcessCpuPageDTO paymentDto){
		settlementTable.init("", false, false);
		settlementTable.setNeftColumnHeaders();
		List<PaymentProcessCpuPageDTO> settlementList = new ArrayList<PaymentProcessCpuPageDTO>();
		settlementList.add(paymentDto);
		settlementTable.setTableList(settlementList);
		
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Settlement Details");
		popup.setWidth("65%");
		popup.setHeight("45%");
		popup.setContent(settlementTable);				
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

}