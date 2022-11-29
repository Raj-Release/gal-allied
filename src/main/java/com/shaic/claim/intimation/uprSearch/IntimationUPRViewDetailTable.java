package com.shaic.claim.intimation.uprSearch;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.dialogs.ConfirmDialog;

import com.ibm.icu.text.SimpleDateFormat;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ViewDetails;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.paymentprocesscpuview.PaymentProcessCpuPageDTO;
import com.shaic.claim.rod.wizard.service.AcknowledgementDocumentsReceivedService;
import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.RodIntimationAndViewDetailsUI;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentModeTrailTable;
import com.shaic.claim.viewEarlierRodDetails.ViewPaymentTrailTableDTO;
import com.shaic.domain.BancsPaymentCancel;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimPaymentCancel;
import com.shaic.domain.ClaimService;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Intimation;
import com.shaic.domain.IntimationService;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.PolicyService;
import com.shaic.domain.Reimbursement;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTable;
import com.shaic.reimbursement.paymentprocess.stoppaymenttracking.validation.StopPaymentTrackingTableDTO;
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
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.BaseTheme;

@SuppressWarnings("serial")
public class IntimationUPRViewDetailTable extends GBaseTable<PaymentProcessCpuPageDTO>{
	
	/*@EJB
	private ClaimService claimService;
	
	@EJB
	private IntimationService intimationService;*/
	
	@Inject
	ViewPaymentModeTrailTable viewPaymentModeTrailTableObj;
	
	@Inject
	private NeftPaymentCancelDetailTable neftCancelTable;

	@Inject
	private ChequePaymentCancelDetailTable chqCancelTable;
	
	@Inject
	private StopPaymentTrackingTable stopPaymentTrackingTable;
	
	@Inject
	private StopPaymentTrackingTable trailForStopPayment;
	
	private final static Object COLUM_HEADER_INTIMATION_UPR_DETAILS[] = new Object[] {
		"serialNumber","rodNumber","claimType","paymentPartyMode","cpuCodeWithValue",
		"batchNo","paymentType","bankName","accNumber",
		"ifscCode","bankBranchName","ddNo","ddDateValue",
		"payableAt","approvedAmount","tdsAmount","netAmount",
		"intrestAmount", "billClassification", "docReceivedFrom",
		"rodType","status","paymentModeChange","transacDetails",
		"stopPaymentTracking","reimbursementObj.status.processValue"};
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initTable() {

		BeanItemContainer<PaymentProcessCpuPageDTO> paymentDtoContainer = new BeanItemContainer<PaymentProcessCpuPageDTO>(PaymentProcessCpuPageDTO.class);
		paymentDtoContainer.addNestedContainerProperty("reimbursementObj.status.processValue");
	
		table.removeGeneratedColumn("paymentModeChange");
		table.addGeneratedColumn("paymentModeChange", new Table.ColumnGenerator() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				Button button = new Button("View");
				button.addClickListener(new Button.ClickListener() {
		
					@Override
					public void buttonClick(ClickEvent event) {
						PaymentProcessCpuPageDTO paymentDto = (PaymentProcessCpuPageDTO)itemId;
						PaymentProcessCpuTableDTO tableModeHisDto = paymentDto.getPaymentProcessDto();	
								viewPaymentModeTrailTableObj.init("", false, false);
//								setUpdatePaymentDetails(tableModeHisDto);
								fireViewEvent(SearchIntimationUPRDetailPresenter.GET_PAYMENT_MODE_TRIALS, tableModeHisDto);								
							}			        	
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
	
		table.removeGeneratedColumn("transacDetails");
		table.addGeneratedColumn("transacDetails",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(final Table source,
						final Object itemId, Object columnId) {

					final Button viewIntimationDetailsButton = new Button("View");
					viewIntimationDetailsButton.setData(itemId);

					viewIntimationDetailsButton
							.addClickListener(new Button.ClickListener() {
								public void buttonClick(ClickEvent event) {
									
									PaymentProcessCpuPageDTO paymentDto = (PaymentProcessCpuPageDTO) itemId;

									/*chqCancelTable.init("Details of DD/Cheque cancellation", false, false);
									neftCancelTable.init("Details of NEFT Return", false, false);*/

									fireViewEvent(SearchIntimationUPRDetailPresenter.GET_PAYMENT_CANCEL_DETAILS, paymentDto);
									
								}
							});
					
					viewIntimationDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					viewIntimationDetailsButton.setWidth("150px");
					viewIntimationDetailsButton
							.addStyleName(ValoTheme.BUTTON_LINK);
					return viewIntimationDetailsButton;
				}
		});
		
		table.removeGeneratedColumn("stopPaymentTracking");
		table.addGeneratedColumn("stopPaymentTracking", new Table.ColumnGenerator() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				
				Button button = new Button("View");
				button.addClickListener(new Button.ClickListener() {
		
					@Override
					public void buttonClick(ClickEvent event) {
						PaymentProcessCpuPageDTO paymentDto = (PaymentProcessCpuPageDTO)itemId;
						PaymentProcessCpuTableDTO tableModeHisDto = paymentDto.getPaymentProcessDto();	
						stopPaymentTrackingTable.init("", false, false);
								fireViewEvent(SearchIntimationUPRDetailPresenter.GET_STOP_PAYMENT_TRIALS, tableModeHisDto);								
							}			        	
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.setWidth("150px");
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
	
	    table.setColumnHeader("paymentModeChange", "Payment Mode change");
	    table.setColumnHeader("transacDetails", "Details of<br>DD/Cheque <br>cancellation/<br>NEFT return/<br>Stop payment");
	    table.setColumnHeader("stopPaymentTracking", "Stop Payment <br>Tracking Details");
	    table.setContainerDataSource(paymentDtoContainer);
	    table.setVisibleColumns(COLUM_HEADER_INTIMATION_UPR_DETAILS);
	    table.setPageLength(table.size()+1);
		
	}

	@Override
	public void tableSelectHandler(PaymentProcessCpuPageDTO t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String textBundlePrefixString() {
		return "searchIntimationUpr-";
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

    /*private void setCancelTableValues(PaymentProcessCpuPageDTO paymentDto){
		PaymentProcessCpuPageDTO paymentDto
		List<ClaimPaymentCancel> premiaPaymentCancelList = intimationService.getPremiaPaymentCancelDetailList(paymentDto,SHAConstants.CHEQUE.toLowerCase());
		List<ClmPaymentCancelDto> chqPremiaCancelListDto = getPremiaCancelListDto(premiaPaymentCancelList);

		List<BancsPaymentCancel> bancsChqPaymentCancelList = intimationService.getBancsPaymentCancelDetailList(paymentDto,SHAConstants.CHEQUE.toLowerCase());
		List<ClmPaymentCancelDto> chqBancsCancelListDto = getBancsCancelListDto(bancsChqPaymentCancelList);
		
		List<ClmPaymentCancelDto> chqCancelListDto = new ArrayList<ClmPaymentCancelDto>();
		chqCancelListDto.addAll(chqPremiaCancelListDto);
		chqCancelListDto.addAll(chqBancsCancelListDto);
		
		chqCancelTable.init("Details of DD/Cheque cancellation", false, false);
		chqCancelTable.setTableList(chqCancelListDto);		
		
		List<ClaimPaymentCancel> premiaNeftPaymentCancelList = intimationService.getPremiaPaymentCancelDetailList(paymentDto,SHAConstants.NEFT_TYPE.toLowerCase());
		List<ClmPaymentCancelDto> neftPremiaCancelListDto = getPremiaCancelListDto(premiaNeftPaymentCancelList);
		
		List<BancsPaymentCancel> bancsNeftPaymentCancelList = intimationService.getBancsPaymentCancelDetailList(paymentDto,SHAConstants.NEFT_TYPE.toLowerCase());
		List<ClmPaymentCancelDto> neftBancsCancelListDto = getBancsCancelListDto(bancsNeftPaymentCancelList);
		
		List<ClmPaymentCancelDto> neftCancelListDto = new ArrayList<ClmPaymentCancelDto>();
		neftCancelListDto.addAll(neftPremiaCancelListDto);
		neftCancelListDto.addAll(neftBancsCancelListDto);
		
		neftCancelTable.init("Details of NEFT Return", false, false);
		neftCancelTable.setTableList(neftCancelListDto);
	}*/
	
	public void showPaymentCancelTable(List<ClmPaymentCancelDto> chqCancelListDto,
		List<ClmPaymentCancelDto> neftCancelListDto) {
		
	}
	
	public void showPayModeTrial(List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList) {
		
		viewPaymentModeTrailTableObj.setTableList(viewPaymentTrailTableList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("View Payment Mode change");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(viewPaymentModeTrailTableObj);				
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
	
	public void showCancelDetailsTable(List<ClmPaymentCancelDto> chqCancelListDto,
			List<ClmPaymentCancelDto> neftCancelListDto) {

		chqCancelTable.init("Details of DD/Cheque cancellation", false, false);
		chqCancelTable.generateViewSettlementColumn();
		chqCancelTable.setTableList(chqCancelListDto);
		chqCancelTable.tablesize();
		neftCancelTable.init("Details of NEFT Return", false, false);
		neftCancelTable.generateViewSettlementColumn();
		neftCancelTable.setTableList(neftCancelListDto);
		neftCancelTable.tablesize();
		
		
		VerticalLayout cancelTableLayout = new VerticalLayout(chqCancelTable, neftCancelTable);
		cancelTableLayout.setSpacing(true);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Details of DD/Cheque cancellation/NEFT Return");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(cancelTableLayout);				
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
	
	public void showSettlementDetails(PaymentProcessCpuPageDTO paymentDto) {
		
		if(SHAConstants.NEFT_TYPE.equalsIgnoreCase(paymentDto.getPaymentType())) {
			neftCancelTable.showSettlementDetails(paymentDto);
		}
		else {
			chqCancelTable.showSettlementDetails(paymentDto);
		}	
	}

	public void showStopPaymentTracking(List<StopPaymentTrackingTableDTO> viewStopPaymentTrackingTableList) {

		trailForStopPayment.init("", false, false);
		trailForStopPayment.setTableList(viewStopPaymentTrackingTableList);
		Window popup = new com.vaadin.ui.Window();
		popup.setCaption("Stop Payment Tracking-Detailed View");
		popup.setWidth("75%");
		popup.setHeight("75%");
		popup.setContent(trailForStopPayment);				
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
}