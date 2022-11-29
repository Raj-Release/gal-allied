package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.claim.premedical.listenerTables.AddDiagnosisPopup;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.reimbursement.paymentprocess.updatepayment.UpdatePaymentDetailTableDTO;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class PaymentCpuCodeSearchTable extends GBaseTable<ViewSearchCriteriaTableDTO>{

	private Window popup;
	
	private ViewSearchCriteriaTableDTO viewSearchCriteriaTableDTO;
	
	private String presenterString;
	
	private EditPaymentDetailsView view;
	
	private UpdatePaymentDetailTableDTO updatePaymentDetailTableDTO;
	
	private CreateAndSearchLotTableDTO createAndSearchLotTableDTO;
	
	@Inject
	private ChangePayeeNamePopUp changePayeeNamePopup;
	
	private Map<ViewSearchCriteriaTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<ViewSearchCriteriaTableDTO, HashMap<String, AbstractField<?>>>();
	
	
	@Override
	public void removeRow() {
		table.removeAllItems();
		
	}
	
	
	public void initPresenter(String presenterString,CreateAndSearchLotTableDTO createAndSearchLotTableDTO, Window popup2)
	{
		this.presenterString = presenterString;
		this.createAndSearchLotTableDTO= createAndSearchLotTableDTO;
		this.popup = popup2;
	}
	
	@Override
	public void initTable() {

		table.setContainerDataSource(new BeanItemContainer<ViewSearchCriteriaTableDTO>(ViewSearchCriteriaTableDTO.class));
		
		 if ((SHAConstants.CREATE_BATCH_PAYMENT_CPU_CODE_CHANGE).equalsIgnoreCase(presenterString) ||
				 (SHAConstants.CREATE_LOT_PAYMENT_CPU_CODE_CHANGE.equalsIgnoreCase(presenterString)))
			{
				table.removeAllItems();
				table.setHeight("400px");
				generaterColumn();
				table.setVisibleColumns(new Object[]{"cpuCodeWithDescription","Select"});
				 
				 
			}
		 
		 else if((SHAConstants.CREATE_BATCH_PAYEE_NAME_CHANGE).equalsIgnoreCase(presenterString) ||
				 (SHAConstants.CREATE_LOT_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString)))
		 {
			 table.removeAllItems();
			 table.setHeight("250px");
			 generaterColumn();
			table.setVisibleColumns(new Object[]{"payeeName","relationship","changePayeeName","Select"});
			
			 
		 }
	}

	@Override
	public void tableSelectHandler(ViewSearchCriteriaTableDTO t) {
		/*fireViewEvent(CreateRODDocumentDetailsPresenter.SETUP_IFSC_DETAILS, t);
		
		popup.close();*/
		
	}
	private void generaterColumn(){
		table.removeGeneratedColumn("Select");
		
		table.addGeneratedColumn("Select", new Table.ColumnGenerator() {
			
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Button btnSelect = new Button("Select");
				btnSelect.setStyleName(ValoTheme.BUTTON_LINK);
				final ViewSearchCriteriaTableDTO viewSearchCriteriaTableDTO = (ViewSearchCriteriaTableDTO)itemId;
				btnSelect.addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
												
						if(SHAConstants.CREATE_BATCH_PAYMENT_CPU_CODE_CHANGE.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(SearchCreateBatchPresenter.SET_UP_PAYMENT_CPU_CODE, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						else if(SHAConstants.CREATE_BATCH_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(SearchCreateBatchPresenter.SET_UP_PAYEE_NAME, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						else if(SHAConstants.CREATE_LOT_PAYMENT_CPU_CODE_CHANGE.equalsIgnoreCase(presenterString)){
							
							fireViewEvent(CreateAndSearchLotPresenter.SET_UP_PAYMENT_CPU_CODE, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						else if(SHAConstants.CREATE_LOT_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString)){
	
							fireViewEvent(CreateAndSearchLotPresenter.SET_UP_PAYEE_NAME_LOT, viewSearchCriteriaTableDTO,createAndSearchLotTableDTO);
						}
						popup.close();
					}
				});
				return btnSelect;
			}
		});
		
		
		table.removeGeneratedColumn("payeeName");
		table.addGeneratedColumn("payeeName", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final ViewSearchCriteriaTableDTO tableDTO = (ViewSearchCriteriaTableDTO)itemId;	
				
				
				com.vaadin.v7.ui.TextField txtPayeeName = new com.vaadin.v7.ui.TextField();
				txtPayeeName.setValue(tableDTO.getPayeeName());
				txtPayeeName.setEnabled(false);
				tableDTO.setTxtPayeeName(txtPayeeName);
				
				txtPayeeName.addValueChangeListener(new ValueChangeListener() {
					
					
					@Override
					public void valueChange(Property.ValueChangeEvent event) {
						
					}
				});
			
				
				return txtPayeeName;
			}
		});
		
		table.removeGeneratedColumn("relationship");
		table.addGeneratedColumn("relationship", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final ViewSearchCriteriaTableDTO tableDTO = (ViewSearchCriteriaTableDTO)itemId;	
				
				com.vaadin.v7.ui.TextField txtRelationShip = new com.vaadin.v7.ui.TextField();
				txtRelationShip.setValue(tableDTO.getRelationship());
				txtRelationShip.setEnabled(false);
	
				return txtRelationShip;
			}
		});
		
		table.removeGeneratedColumn("changePayeeName");
		table.addGeneratedColumn("changePayeeName", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 5936665477260011479L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button changePayeeName = new Button("");
				final ViewSearchCriteriaTableDTO dto = (ViewSearchCriteriaTableDTO) itemId;
				if(null != createAndSearchLotTableDTO && null != createAndSearchLotTableDTO.getClaimType() &&
						SHAConstants.CASHLESS_CLAIM_TYPE.equalsIgnoreCase(createAndSearchLotTableDTO.getClaimType())){
					changePayeeName.setEnabled(false);
				}
				else
				{
					changePayeeName.setEnabled(true);
				}
				changePayeeName.setIcon(FontAwesome.FILE);
				changePayeeName.setData(itemId);
				changePayeeName.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 6100598273628582002L;

					public void buttonClick(ClickEvent event) {
						
						Window popup1 = new com.vaadin.ui.Window();
						popup1.setCaption("Edit Payee Name");
						popup1.setWidth("30%");
						popup1.setHeight("30%");
						
						
						changePayeeNamePopup.init(dto,createAndSearchLotTableDTO,popup1,popup,presenterString);
						popup1.setContent(changePayeeNamePopup);
						popup1.setClosable(true);
						popup1.center();
						popup1.setResizable(false);
						popup1.addCloseListener(new Window.CloseListener() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;
							@Override
							public void windowClose(CloseEvent e) {
								// TODO Auto-generated method stub
								
							}
						});

						popup1.setModal(true);
						UI.getCurrent().addWindow(popup1);
						
						
					}
				});
				// deleteButton.addStyleName(ValoTheme.BUTTON_DANGER);
				return changePayeeName;
			}
		});
		
	}
	

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "view_search_criteria_table-";
	}
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	public void setWindowObject(Window popup){
		this.popup = popup;
	}



}
