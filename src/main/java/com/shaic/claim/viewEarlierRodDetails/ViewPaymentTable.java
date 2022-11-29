package com.shaic.claim.viewEarlierRodDetails;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.paymentprocesscpu.PaymentProcessCpuTableDTO;
import com.shaic.claim.reimbursement.viewPaymentAudit.ViewPaymentAuditDTO;
import com.shaic.claim.reimbursement.viewPaymentAudit.ViewPaymentAuditTable;
import com.shaic.domain.ClaimService;
import com.shaic.domain.PaymentModeTrail;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;


public class ViewPaymentTable extends GBaseTable<PaymentProcessCpuTableDTO>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Inject
//	PaymentProcessCpuPage paymentProcessCpuPage;
	
	@Inject
	private ViewPaymentModeTrailTable viewPaymentModeTrailTableObj;
	
	@EJB
	private ClaimService claimService;
	
	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","rodNo", "claimType","billClassification","docReceivedFrom","rodType","amount","paymentType", 
		"bankName", "accountNumber", "ifscCode","branchName","chequeDateValue","chequeNo","status","paymentmodechange","statusaudit"};
	
	private final static Object[] NATURAL_COL_ORDER_PA = new Object[]{"serialNumber","rodNo", "claimType","benefitCover","docReceivedFrom","rodType","amount", "paymentType", 
		"bankName", "accountNumber", "ifscCode","branchName","chequeDateValue","chequeNo","status"};
	
	@Inject
	private ViewPaymentAuditTable viewPaymentAuditTable;
	
	@EJB
	private DBCalculationService dbCalculationService;
	
	@Override
	public void removeRow() {
		
		table.removeAllItems();
		
	}
	
	public void setPAColumsnForPayment()
	{
		table.setVisibleColumns(NATURAL_COL_ORDER_PA);
		table.setColumnHeader("benefitCover", "Benefit/Cover");
		table.setColumnHeader("docReceivedFrom", "Document Received From");
		table.setColumnHeader("amount", "Amount");
		table.setColumnHeader("rodType", "ROD Type");
	}
	

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PaymentProcessCpuTableDTO>(PaymentProcessCpuTableDTO.class));
		generateColumn();
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size());
		table.setColumnHeader("paymentmodechange", "Payment Mode Change");
		table.setColumnHeader("statusaudit", "Payment Audit Details");
	
	}

	@Override
	public void tableSelectHandler(PaymentProcessCpuTableDTO t) {
		
		
		/***
		 * need to implement webservice for check the 64vb status
		 */
//	fireViewEvent(MenuItemBean.PAYMENT_PROCESS_CPU, t);
		
	}
	
	@SuppressWarnings("deprecation")
	private void generateColumn()
	{

		table.removeGeneratedColumn("paymentmodechange");
		table.addGeneratedColumn("paymentmodechange", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {



				Button button = new Button("View Details");
				button.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						PaymentProcessCpuTableDTO tableDto = (PaymentProcessCpuTableDTO)itemId;

						viewPaymentModeTrailTableObj.init("", false, false);
						setUpdatePaymentDetails(tableDto);
						Window popup = new com.vaadin.ui.Window();
						popup.setCaption("View Payment Mode change");
						popup.setWidth("75%");
						popup.setHeight("75%");
						popup.setContent(viewPaymentModeTrailTableObj);				
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
						popup.addCloseListener(new Window.CloseListener() {
							/**]
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
				button.setWidth("150px");
				button.addStyleName(ValoTheme.BUTTON_LINK);
				return button;
			}
		});
		
		table.removeGeneratedColumn("statusaudit");
		table.addGeneratedColumn("statusaudit", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,Object columnId) {

				PaymentProcessCpuTableDTO tableDto = (PaymentProcessCpuTableDTO)itemId;
				
				if(tableDto.getStatusId() !=null && tableDto.getBancsUprId() !=null 
						&& (tableDto.getStatusId().equals(ReferenceTable.SEND_FOR_PAYMENT_APPROVAL_STATUS)
								|| tableDto.getStatusId().equals(ReferenceTable.PAYMENT_SETTLED_STATUS)) && 
								tableDto.getPolicySource() != null
						&& tableDto.getPolicySource().equals(SHAConstants.BANCS_POLICY)){
					Button button = new Button("Payment Audit Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							PaymentProcessCpuTableDTO tableDto = (PaymentProcessCpuTableDTO)itemId;

							viewPaymentAuditTable.init("", false, false);
							setPaymentAuditDetails(tableDto.getBancsUprId());
							Window popup = new com.vaadin.ui.Window();
							popup.setCaption("Payment Audit Details");
							popup.setWidth("35%");
							popup.setHeight("45%");
							popup.setContent(viewPaymentAuditTable);				
							popup.setClosable(true);
							popup.center();
							popup.setResizable(false);
							popup.addCloseListener(new Window.CloseListener() {
								/**]
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
					//button.setWidth("140px");
					button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
				return "";
			}
		});
}

	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	@Override
	public String textBundlePrefixString() {
		
		return "paymentprocesstable-";
	}
	
	
	
	
	private void setUpdatePaymentDetails(PaymentProcessCpuTableDTO updatePaymentDTO){		
		
		Reimbursement reimbObj = claimService.getReimbursementByRodNo(updatePaymentDTO.getRodNo());
		
		if(null != reimbObj){
			
			List<PaymentModeTrail> payModeTrail = claimService.getPaymentModeTrailByRodKey(reimbObj.getKey());	
			
			List<ViewPaymentTrailTableDTO> viewPaymentTrailTableList = new ArrayList<ViewPaymentTrailTableDTO>();
			
			if(null != payModeTrail && !payModeTrail.isEmpty()){
				
			for (PaymentModeTrail paymentModeTrail : payModeTrail) {
				
				ViewPaymentTrailTableDTO viewPaymentDetailsDto = new ViewPaymentTrailTableDTO();
				
				viewPaymentDetailsDto.setClaimType(updatePaymentDTO.getClaimType());
				viewPaymentDetailsDto.setDateAndTime(paymentModeTrail.getCreatedDate());
				viewPaymentDetailsDto.setClaimStage(paymentModeTrail.getStageId().getStageName());
				viewPaymentDetailsDto.setClaimStatus(paymentModeTrail.getStatusId().getProcessValue());
				viewPaymentDetailsDto.setReasonForChange(paymentModeTrail.getPayModeRemarks());	
				viewPaymentDetailsDto.setUserId(paymentModeTrail.getCreatedBy());
				viewPaymentDetailsDto.setPaymentMode(paymentModeTrail.getPaymentMode());
				viewPaymentTrailTableList.add(viewPaymentDetailsDto);

			}
			    viewPaymentModeTrailTableObj.setTableList(viewPaymentTrailTableList);
			
			}
			
		}
	}

	private void setPaymentAuditDetails(String bancsUprId){		

		List<ViewPaymentAuditDTO> paymentAuditDTOs = dbCalculationService.getPaymentAuditDetails(bancsUprId);
		if(null != paymentAuditDTOs && !paymentAuditDTOs.isEmpty()){
			viewPaymentAuditTable.setTableList(paymentAuditDTOs);
		}
	}
		
}