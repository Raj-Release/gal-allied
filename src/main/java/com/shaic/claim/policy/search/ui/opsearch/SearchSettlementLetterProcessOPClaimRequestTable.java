package com.shaic.claim.policy.search.ui.opsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpPresenter;
import com.shaic.claim.outpatient.createbatchop.CreateBatchOpTableDTO;
import com.shaic.claim.outpatient.createbatchop.OPCreateBatchTable.ImmediateFieldFactory;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.Claim;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.themes.BaseTheme;

public class SearchSettlementLetterProcessOPClaimRequestTable extends ViewComponent{
	
	private Map<CreateBatchOpTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<CreateBatchOpTableDTO, HashMap<String, AbstractField<?>>>();
	
	private Table table;
	
	BeanItemContainer<CreateBatchOpTableDTO> data = new BeanItemContainer<CreateBatchOpTableDTO>(CreateBatchOpTableDTO.class);
	
	public List<CreateBatchOpTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	
	private HorizontalLayout fLayout;
	private String presenterString;
	private VerticalLayout tableLayout = null;
	public HashMap<Long,Component> compMap = null;
	
	private Searchable searchable;
	
	private PagerUI pageUI;
	
	public void init() {
		
		
		initTable();
		pageUI = new PagerUI();
			
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new HorizontalLayout();
		fLayout.setWidth("100%");
//		fLayout.setHeight("25px");
		
		
		 tableLayout.addComponent(fLayout);
		 tableLayout.addComponent(table);
		 
		setCompositionRoot(tableLayout);
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table();
		table.setContainerDataSource(data);

		table.setWidth("100%");

			table.removeAllItems();
			
			table.setEditable(true);
			table.setTableFieldFactory(new ImmediateFieldFactory());
			table.setFooterVisible(false);
			table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	
			table.removeGeneratedColumn("Action");
			table.addGeneratedColumn("Action",
					new Table.ColumnGenerator() {
						@Override
						public Object generateCell(final Table source,
								final Object itemId, Object columnId) {

							final Button viewIntimationDetailsButton = new Button();
							viewIntimationDetailsButton.setData(itemId);
							viewIntimationDetailsButton
							.setCaption("Generate Settlement Letter");
							viewIntimationDetailsButton.addClickListener(new Button.ClickListener() {
								
								@Override
								public void buttonClick(ClickEvent event) {
									Long opcheckupkey = ((CreateBatchOpTableDTO) itemId)
											.getOpHealthCheckUpKey();
									fireViewEvent(SearchSettlementLetterProcessOPClaimRequestPresenter.CREATE_SETTLEMENT_LETTER_GENERATE,opcheckupkey);
								}
							});
							
							viewIntimationDetailsButton
									.addStyleName(BaseTheme.BUTTON_LINK);
							return viewIntimationDetailsButton;
						}
					});
			
			table.setVisibleColumns(new Object[] {"Action","intimationNo","claimNo","policyNo","product","paymentStatus","paymentTypeValue",
					"ifscCode","beneficiaryAcntNo","bankName","branchName","typeOfClaim" });
				//table.setColumnHeader("docVerifiedValue", "Doc Verified");
				table.setColumnHeader("intimationNo", "Intimation No");
				table.setColumnHeader("claimNo", "Claim No");
				table.setColumnHeader("policyNo", "Policy No");
				table.setColumnHeader("product", "Product");
				table.setColumnHeader("paymentStatus", "Payment</br>Status");
				table.setColumnHeader("paymentTypeValue", "Payment Type");
				table.setColumnHeader("ifscCode", "IFSC Code");
				table.setColumnHeader("beneficiaryAcntNo", "Beneficiary Account No");
				table.setColumnHeader("branchName", "Branch Name");
				table.setColumnHeader("bankName", "Bank Name");
				table.setColumnHeader("typeOfClaim", "Type Of Claim");
			 table.setColumnHeader("Action", "Action");
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			CreateBatchOpTableDTO initiateDTO = (CreateBatchOpTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
			
			if ("intimationNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intimationNo", field);

				return field;
			}
			if ("claimNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("175px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimNo", field);

				return field;
			}
			else if ("policyNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("160px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("policyNo", field);
			
				return field;
			}
			else if("paymentStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("75px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
//				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentStatus", field);
				return field;
			}
			else if("product".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("100px");
				field.setWidth("100%");
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
//				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("product", field);
				return field;
			}
			else if("cpuCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
//				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("cpuCode", field);
				return field;
			}

			else if("paymentTypeValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
//				field.setEnabled(Boolean.TRUE);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(initiateDTO);
				tableRow.put("paymentTypeValue", field);
				return field;
			}
			else if("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("ifscCode", field);
				return field;
			}
			else if("beneficiaryAcntNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				/*if (presenterString!=null && (SHAConstants.CREATE_LOT).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}*/
			/*	if (presenterString!=null && (initiateDTO.getPaymentType().getId()).equals(ReferenceTable.PAYMENT_MODE_CHEQUE_DD)){
					field.setEnabled(false);
				}*/
				
				tableRow.put("beneficiaryAcntNo", field);
				return field;
			}
			else if("bankName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setReadOnly(true);
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setWidth("100%");
				/*if (presenterString!=null && (SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(presenterString)){
					field.setEnabled(initiateDTO.getIsInsured());
				}else{
					field.setEnabled(Boolean.FALSE);
				}*/
//				field.setEnabled(Boolean.FALSE);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				tableRow.put("bankName", field);
				return field;
			}
			else if("branchName".equals(propertyId)) {
				TextField field = new TextField();
				field.setNullRepresentation("");
				field.setWidth("100%");
				field.setReadOnly(true);
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("branchName", field);
				return field;
			}
			else if("typeOfClaim".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setReadOnly(true);
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("typeOfClaim", field);
				return field;
			}

			else if("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setReadOnly(true);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmt", field);
				return field;
			}
			
		
			/*else if("serialNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
//				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("serialNo", field);
				return field;
			}*/
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField){
					field.setWidth("100%");
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setReadOnly(true);
					
					((TextField) field).setNullRepresentation("");
					
				}
				
				return field;
			}
		}
	}
	
	 @SuppressWarnings("unchecked")
	public List<CreateBatchOpTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<CreateBatchOpTableDTO> itemIds = (List<CreateBatchOpTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
   	
	}
	
	public void addSearchListener(Searchable searchable)
	{
		this.searchable = searchable;
	}
	
	public void resetTableDataList()
	{
		if(null != tableDataList)
		{
			tableDataList.clear();
		}
		if(null != tableDataKeyList)
		{
			tableDataKeyList.clear();
		}
	}
	
	public void clearExistingList(){
		if(this.tableDataList != null){
			this.tableDataList.clear();
		}
		
		if(this.tableDataKeyList != null){
			this.tableDataKeyList.clear();
		}
	}
	
	public List<CreateBatchOpTableDTO> getTableItems()
	{
		return tableDataList;
	}
	
	public Pageable getPageable()
	{
		return this.pageUI.getPageable();
	}
	
	public void resetTable()
	{
		if(null != table){
			List<CreateAndSearchLotTableDTO> tableList = (List<CreateAndSearchLotTableDTO>)table.getItemIds();
			if(null != tableList && !tableList.isEmpty())
			{
				table.removeAllItems();
				if(null != tableLayout) {
					//tableLayout.removeAllComponents();
				}
			}
		}
	//	this.pageUI.resetPage();
	}
	
	public void initPresenterString(String presenterString)
	{
		this.presenterString  = presenterString;
	}
	
	public void setTableList(List<CreateBatchOpTableDTO> tableItems) {
		table.removeAllItems();
		//List<CreateAndSearchLotTableDTO> tableItems = tableRows.getPageItems();
		if(null != tableItems && !tableItems.isEmpty())
		{
			for (CreateBatchOpTableDTO createAndSearchLotTableDTO : tableItems) {
				
				table.addItem(createAndSearchLotTableDTO);
			}
		}
	}
	
	public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);
	}
	
	public void setPage(Page<CreateBatchOpTableDTO> page){
		this.pageUI.setPageDetails(page);
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=5){
			table.setPageLength(5);
		}
		
	}
	
	public void removeRow() {
		table.removeAllItems();
	}
}
