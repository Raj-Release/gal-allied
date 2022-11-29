package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.PagerListener;
import com.shaic.arch.table.PagerUI;
import com.shaic.arch.table.Searchable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class NewSearchCreateBatchListenerTable extends ViewComponent{
	

	
	private Table table;
	private String presenterString;
	//private Map<String, Object> referenceData;
	private Map<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<CreateAndSearchLotTableDTO, HashMap<String, AbstractField<?>>>();
	private PagerUI pageUI;
	private FormLayout fLayout;
	private Searchable searchable;
	private Label totalRocordsTxt;
	Integer intNoOfDays = 0;
	Double intPenalInterestAmnt = 0d;
	
	public 
	
	/***
	 * Bean object fetch from db
	 */
	BeanItemContainer<CreateAndSearchLotTableDTO> data = new BeanItemContainer<CreateAndSearchLotTableDTO>(CreateAndSearchLotTableDTO.class);
	public HashMap<Long,Component> compMap = null;
	public List<CreateAndSearchLotTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	private VerticalLayout tableLayout = null;
	
	public void init(String presenterString,Boolean showPagerFlag) {
		
		this.presenterString = presenterString;
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		pageUI = new PagerUI();
		
		addListener();		
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
		tableLayout = new VerticalLayout();
		fLayout = new FormLayout();
		totalRocordsTxt = new Label();
		
		// VerticalLayout layout = new VerticalLayout();
		 if(showPagerFlag){
			 tableLayout.addComponent(pageUI);
		     pageUI.addListener(new PagerListener() {
				@Override
				public void changePage() {
					
					searchable.doSearch();
				}
			});
		 }
		
		 tableLayout.addComponent(fLayout);
		 tableLayout.addComponent(table);
		 
		setCompositionRoot(tableLayout);
	}
	
	private void addListener() {
		
	}
	
	public void initPresenterString(String presenterString)
	{
		this.presenterString  = presenterString;
	}
	
	
	public void setReferenceData(Map<String, Object> referenceData) {
		//this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", data);
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setHeight("310px");
		
		generatecolumns();
		
		if((SHAConstants.CREATE_BATCH_TYPE).equalsIgnoreCase(presenterString))
		{
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "serialNo","chkSelect","editpaymentdetails","viewdetails","intimationNo", "claimNo", "policyNo", "rodNo","paymentStatus","product",
				"cpuCode","paymentType","ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","lotNo","approvedAmt","payeeName","payableAt","panNo","providerCode","emailID",
				"zonalMailId","reconsiderationFlag","faApprovedAmnt","lastAckDateValue","faApprovedDateValue","numberofdays","noofdaysexceeding","intrestAmount","penalTotalAmnt",
				"remarks","dbSideRemark" });
			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("editpaymentdetails", "");
			table.setColumnHeader("viewdetails", "");
			table.setColumnHeader("intimationNo", "Intimation No");
			table.setColumnHeader("claimNo", "Claim No");
			table.setColumnHeader("policyNo", "Policy No");
			table.setColumnHeader("rodNo", "ROD No");
			table.setColumnHeader("paymentStatus", "Payment Status");
			table.setColumnHeader("product", "Product");
			table.setColumnHeader("cpuCode", "Cpu Code");
			table.setColumnHeader("ifscCode", "IFSC Code");
			table.setColumnHeader("beneficiaryAcntNo", "Benificiary Acnt No");
			table.setColumnHeader("branchName", "Branch Name");
			table.setColumnHeader("typeOfClaim", "Type Of Claim");
			table.setColumnHeader("lotNo", "Lot No");
			table.setColumnHeader("approvedAmt", "Approved Amnt");
			table.setColumnHeader("payeeName", "Payee Name");
			table.setColumnHeader("payableAt", "Payable City");
			table.setColumnHeader("panNo", "PAN No");
			table.setColumnHeader("providerCode", "Provider Code");
			table.setColumnHeader("emailID","Email ID");
			table.setColumnHeader("zonalMailId", "Zonal Email ID");
			table.setColumnHeader("reconsiderationFlag", "Reconsideration</br>Flag(Y/N)");
			table.setColumnHeader("faApprovedAmnt", "Approved Amnt");
			table.setColumnHeader("lastAckDateValue", "Last<br>Acknowledegement</br>Received Date");
			table.setColumnHeader("faApprovedDateValue", "Date Of<br>Financial</br>Approval");
			table.setColumnHeader("numberofdays", "No Of Days <br>for<br>processing");
			table.setColumnHeader("noofdaysexceeding", "No Of Days<br> Exceeding<br>IRDA TAT<br>(30 Days)</br>");
			table.setColumnHeader("intrestAmount", "Penal Interest<br> Amount");
			table.setColumnHeader("penalTotalAmnt", "Total amount");
	 		table.setColumnHeader("remarks", "Interest Remarks");
	 		table.setColumnHeader("dbSideRemark", "Remarks");
		}
		else if ((SHAConstants.SEARCH_BATCH_TYPE).equalsIgnoreCase(presenterString))
		{
			table.removeAllItems();
			table.setVisibleColumns(new Object[]{"serialNo","chkSelect","accountBatchNo","paymentReqDateValue","userId","typeOfClaim","claimCount","showdetails"});
			
			table.setColumnHeader("serialNo", "S.No");
			table.setColumnHeader("chkSelect", "");
			table.setColumnHeader("accountBatchNo", "Account Batch No");
	 		table.setColumnHeader("paymentReqDateValue", "Payment Req Date");
	 		table.setColumnHeader("userId", "User ID");
	 		table.setColumnHeader("typeOfClaim", "Claim Type");
	 		table.setColumnHeader("claimCount", "Claim CTN");
	 		table.setColumnHeader("showdetails", "Show Details");
		
		}
		else
		{
			table.removeAllItems();
			table.setVisibleColumns(new Object[] { "chkSelect","editpaymentdetails","viewdetails","intimationNo", "claimNo", "policyNo", "rodNo","paymentStatus","product",
					"cpuCode","paymentType","ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","lotNo","approvedAmt","payeeName","payableAt","panNo","providerCode","emailID",
					"zonalMailId","reconsiderationFlag","faApprovedAmnt","lastAckDateValue","faApprovedDateValue","numberofdays","noofdaysexceeding","intrestAmount","penalTotalAmnt",
					"remarks" });
		}
			
		table.setEditable(true);	
		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		
 		table.setFooterVisible(false);
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	
	public void manageListeners() {

		
	}
		
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			CreateAndSearchLotTableDTO initiateDTO = (CreateAndSearchLotTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(initiateDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(initiateDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(initiateDTO);
			}
						
			if("intimationNo".equals(propertyId)) {
				
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intimationNo", field);

				return field;
			} 
			
			else if ("claimNo".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
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
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("policyNo", field);
			
				return field;
			}
			else if ("rodNo".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("rodNo", field);
				
				return field;
			}
			else if("paymentStatus".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setEnabled(false);
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
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
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
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
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
		
			else if("paymentType".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("paymentType", field);
				return field;
			}
			else if("ifscCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
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
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("beneficiaryAcntNo", field);
				return field;
			}
			else if("branchName".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("150px");
				field.setNullRepresentation("");
				field.setEnabled(false);
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
				field.setEnabled(false);
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
			else if("lotNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lotNo", field);
				return field;
			}
			else if("approvedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("approvedAmt", field);
				return field;
			}
			else if("payeeName".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payeeName", field);
				return field;
			}
			else if("payableAt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("payableAt", field);
				return field;
			}
			else if("panNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("120px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("panNo", field);
				return field;
			}
			else if("providerCode".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("providerCode", field);
				return field;
			}
			else if("emailID".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("emailID", field);
				return field;
			}
			else if("zonalMailId".equals(propertyId)) {
				TextField field = new TextField();
				//field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("zonalMailId", field);
				return field;
			}
			else if("reconsiderationFlag".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("reconsiderationFlag", field);
				return field;
			}
			else if("faApprovedAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedAmnt", field);
				return field;
			}else if("lastAckDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("lastAckDateValue", field);
				return field;
			}else if("faApprovedDateValue".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("faApprovedDateValue", field);
				return field;
			}else if("numberofdays".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("70px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("numberofdays", field);
				return field;
			}else if("intrestAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				String legalFlag =  null != initiateDTO.getClaimDto().getLegalFlag() ? initiateDTO.getClaimDto().getLegalFlag() : null;
				
				if(initiateDTO.getReconsiderationFlag()!= null && initiateDTO.getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) 
				{
						if((initiateDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) || (initiateDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
								|| (null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
										legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG))))
						{
							field.setEnabled(false);
						} 
						else
						{
							field.setEnabled(true);
						}
				}
				else
				{
					field.setEnabled(false);
				}
				field.addBlurListener(getInterestAmnt(field));
				field.addBlurListener(totalAmountListener(field));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("intrestAmount", field);
				return field;
			}
			else if("noofdaysexceeding".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);

				if(null != initiateDTO.getNumberofdays())
				{
					if(initiateDTO.getNoofdaysexceeding() >= -3)
					{	
						if(ReferenceTable.CORRECTION_PAYMENT_STATUS_ID.equals(initiateDTO.getPaymentStatusKey()) || (SHAConstants.DOC_RECEIVED_FROM_HOSPITAL).equals(initiateDTO.getDocReceivedFrom()))
					{
						field.setEnabled(false);
					} 
							else
							{
								field.setEnabled(true);
							}
					}
					else
					{					
						field.setEnabled(false);							
						
					}
				}
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				//noOfDaysExceedingListener(field);
				field.addBlurListener(noOfDaysExceedingListener(field));
				tableRow.put("noofdaysexceeding", field);
				return field;
			}
			
			else if("penalTotalAmnt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("penalTotalAmnt", field);
				return field;
			}
			else if("remarks".equals(propertyId)) {
			
				TextField field = new TextField();					
				String legalFlag =  null != initiateDTO.getClaimDto().getLegalFlag() ? initiateDTO.getClaimDto().getLegalFlag() : null;
				
				if(null != legalFlag &&(legalFlag.equalsIgnoreCase(SHAConstants.YES_FLAG) || 
								legalFlag.equalsIgnoreCase(SHAConstants.N_FLAG)))
				{
					field.setEnabled(false);
				} 
				else
				{
					field.setEnabled(true);
				}
			//	field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(true);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				field.setDescription(field.getValue());
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("remarks", field);
				return field;
			}
			else if("claimCount".equals(propertyId)) {
				TextField field = new TextField();
			//	field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				//field.setMaxLength(6);
				field.setData(initiateDTO);
				//field.setValue(String.valueOf(Table.ROW_HEADER_MODE_INDEX));
				//field.setValue(String.valueOf(entryDTO.getItemNo()));
				CSValidator validator = new CSValidator();
				validator.extend(field);
				validator.setRegExp("^[0-9]*$");
				validator.setPreventInvalidTyping(true);
				tableRow.put("claimCount", field);
				//final TextField txt = (TextField) tableRow.get("itemNo");
				//generateSlNo(txt);
				return field;
			}
			else if("serialNo".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("50px");
				field.setNullRepresentation("");
				field.setEnabled(false);
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
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);
				
				if("dbSideRemark".equals(propertyId)){
					CreateAndSearchLotTableDTO dto = (CreateAndSearchLotTableDTO)itemId;
					if (field instanceof TextField){
						TextField txtField = ((TextField) field);
						txtField.setValue(dto.getDbSideRemark());
						txtField.setDescription(dto.getDbSideRemark());
						//dto.setRemarks(dto.getDbSideRemark());
					}
				}

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
	
	
	
	private void generatecolumns(){
		
		//	compList = new ArrayList<Component>();
			compMap = new HashMap<Long, Component>();
			
			table.removeGeneratedColumn("chkSelect");
			table.addGeneratedColumn("chkSelect", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					  CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) itemId;
						CheckBox chkBox = new CheckBox("");
						
						 if(null != tableDataList && !tableDataList.isEmpty())
						  {
							  for (CreateAndSearchLotTableDTO dto : tableDataList) {
								  if(dto.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
								  {
									   if(null != dto.getChkSelect())
									  {
										  chkBox.setValue(dto.getChkSelect());
									  }
									   else if(("true").equalsIgnoreCase(dto.getCheckBoxStatus()))
									   {
										   chkBox.setValue(true);
									   }
									   else if(("false").equalsIgnoreCase(dto.getCheckBoxStatus()))
									   {
										   chkBox.setValue(false);
									   }
									   //compMap.put(dto.getClaimPaymentKey(), chkBox);
								  }
								
							}
						  }
						
						chkBox.setData(tableDTO);
						addListener(chkBox);
						compMap.put(tableDTO.getClaimPaymentKey(), chkBox);
					//	compList.add(chkBox);
						//addListener(chkBox);
					return chkBox;
				}
			});
			
			
			table.removeGeneratedColumn("viewdetails");
			table.addGeneratedColumn("viewdetails", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;
					
					Button button = new Button("View Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
							showClaimsDMSView(tableDTO.getIntimationNo());
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});
			

			table.removeGeneratedColumn("editpaymentdetails");
			table.addGeneratedColumn("editpaymentdetails", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
						
					
					Button button = new Button("Edit Payment Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						//To implement claims dms functionality.
							showEditPaymentDetailsView((CreateAndSearchLotTableDTO)itemId);
							
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});


			table.removeGeneratedColumn("showdetails");
			
			table.addGeneratedColumn("showdetails", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
						
					
					Button button = new Button("Show Details");
					button.addClickListener(new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
						
												
							fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_DETAILS,(CreateAndSearchLotTableDTO)itemId);		    	
					    				
						}
					});
					button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			    	button.setWidth("150px");
			    	button.addStyleName(ValoTheme.BUTTON_LINK);
					return button;
				}
			});
						
			
			table.removeGeneratedColumn("remarks");
			table.addGeneratedColumn("remarks", new Table.ColumnGenerator() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {
						
					final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;	
					
					
					com.vaadin.v7.ui.TextField txtRemarks = new com.vaadin.v7.ui.TextField();
					
					txtRemarks.addValueChangeListener(new ValueChangeListener() {
						
						
						
						@Override
						public void valueChange(Property.ValueChangeEvent event) {	
						
							String value = (String)event.getProperty().getValue();
							
							
							if(null != value && !(value.equals("")))
							{
								
								tableDTO.setRemarks(value);
							}
							else
							{
								tableDTO.setRemarks("");
							}
							
						}
					});
				
					//txtRemarks.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					txtRemarks.setWidth("150px");
					txtRemarks.addStyleName(ValoTheme.BUTTON_LINK);
					if(null != tableDTO.getRemarks())
						txtRemarks.setValue(tableDTO.getRemarks());
					return txtRemarks;
				}
			});
		}
	
	
	
	
	
	 public BlurListener getInterestAmnt(final TextField interstAmnt) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {	
					
					
					
					TextField component = (TextField) event.getComponent();
					String value = null;
					//String[] values = null;
					Number number = null ;
					if(null != component)
					{
						value = component.getValue();
						/**
						 * The below parse done for avoid Number format Exception
						 */
						
						try {
							 number = NumberFormat.getNumberInstance(java.util.Locale.US).parse(value);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
					
					if(null != number)
					{
						intPenalInterestAmnt = number.doubleValue();
					}
				}	
				
					
			};
			return listener;
			
		}
	
	
	 public BlurListener totalAmountListener(final TextField interstAmnt) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {	
					
					
					//String value = (String)event.getComponent().get
					TextField component = (TextField) event.getComponent();
					/*String value = null;
					if(null != component)
					{
						value = component.getValue();
						
					}*/
					
				
					CreateAndSearchLotTableDTO tableDTO = new CreateAndSearchLotTableDTO();
					 TextField txtField = (TextField)component;
					 tableDTO = (CreateAndSearchLotTableDTO) txtField.getData();				
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					//TextField faApproveAmnt = (TextField) hashMap.get("faApprovedAmnt");
					//TextField penalInterestAmt = (TextField) hashMap.get("intrestAmount");
					TextField penalTotalAmnt = (TextField) hashMap.get("penalTotalAmnt");
					Double penalTotalamnt = 0d;
					
					Double faApproveAmount = null != tableDTO.getFaApprovedAmnt() ? tableDTO.getFaApprovedAmnt() : 0d;
					Double interestAmount = null != tableDTO.getIntrestAmount() ? tableDTO.getIntrestAmount() : 0d;
					
						penalTotalamnt = faApproveAmount+interestAmount;
						
						double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
						int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
						
						if(converttoIntOfApproveAmnt >= 5)
						{
							tableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
						}
						
						else
						{
							tableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
						}
						
						if(null != penalTotalAmnt && null != tableDTO.getPenalTotalAmnt())
						{
							penalTotalAmnt.setValue(String.valueOf(tableDTO.getPenalTotalAmnt()));
						}															
					
				}
			};
			return listener;
			
		}
	
	
	 public BlurListener noOfDaysExceedingListener(final TextField txtNoOfDaysExceeding) {
			
			BlurListener listener = new BlurListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void blur(BlurEvent event) {	
					
					
					//String value = (String)event.getComponent().get
					TextField component = (TextField) event.getComponent();
					String value = null;
					if(null != component)
					{
						value = component.getValue();
					}
					
				//	CreateAndSearchLotTableDTO	tableDTO =  (CreateAndSearchLotTableDTO) ((TextField)event.getProperty()).getData();
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO) txtNoOfDaysExceeding.getData();
					//CreateAndSearchLotTableDTO tableDTO = new CreateAndSearchLotTableDTO();
					 TextField txtField = (TextField)component;
					 tableDTO = (CreateAndSearchLotTableDTO) txtField.getData();				
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					TextField penalInterestAmt = (TextField) hashMap.get("intrestAmount");
					TextField penalTotalAmnt = (TextField) hashMap.get("penalTotalAmnt");
					
					Integer diffNoOfDays = 0;
					//Double intNoOfDays = 0d;
					
					if(null != value && !(value.equals("")))
					{								
						 								
						intNoOfDays = Integer.valueOf(value);
						if(intNoOfDays >=-3)
						{
						
						 diffNoOfDays = intNoOfDays - tableDTO.getNoOfDaysExceedingforCalculation();
						
						if(diffNoOfDays<=3)
						{								
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							tableDTO.setNoofdaysexceeding(intNoOfDays);
							
						}
						else
						{
							tableDTO.setNoofdaysexceeding(intNoOfDays);
							tableDTO.setNoOfDiffDays(diffNoOfDays);
							fireViewEvent(SearchCreateBatchPresenter.VALIDATION,null);				
												
						}
						Double  faApprovedAmnt = null != tableDTO.getFaApprovedAmnt() ? tableDTO.getFaApprovedAmnt() : 0d;
						Double interestRate = 0d;
						
						if((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) ||
								tableDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
						{
							interestRate = 0d;
						}
						else
						{
						interestRate = null != tableDTO.getIntrestRate() ? tableDTO.getIntrestRate() : 0d;
						}
						Double noOfExceedingDays1 = null != tableDTO.getNoofdaysexceeding() ?  tableDTO.getNoofdaysexceeding() : 0d;				
					
						
						Double noOfExceedingDays = Math.abs(noOfExceedingDays1/365);					
					
						
						Double penalInterestAmount = faApprovedAmnt*(interestRate/100)*(noOfExceedingDays);
						
						Double penalTotalamnt = 0d;
						Double approvedAmnt = 0d;
						if(null != penalInterestAmount)
						{
							
							double decimalNo =  penalInterestAmount*10 % 10;
							int converttoInt = (int) decimalNo;
							
							if(converttoInt >= 5)
							{
								approvedAmnt =  Math.ceil(penalInterestAmount);
							}
							else
							{
								approvedAmnt =Math.floor(penalInterestAmount);	
							}
							
							if(null != penalInterestAmt && null != approvedAmnt)
							{
								penalInterestAmt.setValue(String.valueOf(Math.abs(approvedAmnt)));
							}
							tableDTO.setIntrestAmount(Math.abs(approvedAmnt));
							tableDTO.setInterestAmntForCalculation(Math.abs(approvedAmnt));
						//	tableDTO.setPenalInterestAmntForCalculation(Math.abs(approvedAmnt));
							
							
						}
						
						 penalTotalamnt = faApprovedAmnt+tableDTO.getIntrestAmount();
						
						double decimalOfApproveAmnt =  penalTotalamnt*10 % 10;
						int converttoIntOfApproveAmnt = (int) decimalOfApproveAmnt;	
						
						if(converttoIntOfApproveAmnt >= 5)
						{
							tableDTO.setPenalTotalAmnt(Math.ceil(penalTotalamnt));
						}
						
						else
						{
							tableDTO.setPenalTotalAmnt(Math.floor(penalTotalamnt));
						}
						
						if(null != penalTotalAmnt && null != tableDTO.getPenalTotalAmnt())
						{
							penalTotalAmnt.setValue(String.valueOf(tableDTO.getPenalTotalAmnt()));
						}
						
							
						}
						
						else
						{
							fireViewEvent(SearchCreateBatchPresenter.NO_OF_EXCEEDING_DAYS_VALIDATION,null);
							if(null != tableDTO.getNoofdaysexceeding())
								txtNoOfDaysExceeding.setValue(String.valueOf(tableDTO.getNoofdaysexceeding()));
						}
						
					}
				}
			};
			return listener;
			
		}
	
	
	private void addListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					
					  
					boolean value = (Boolean) event.getProperty().getValue();
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)chkBox.getData();
					
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}else
					{
						tableDTO.setCheckBoxStatus("false");
						//tableDTO.setChkSelect(false);
					}
					/**
					 * Added for issue#192.
					 * */
					if(null != tableDataList && !tableDataList.isEmpty())
					{
						for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDataList) {
							
							if(createAndSearchLotTableDTO.getClaimPaymentKey().equals(tableDTO.getClaimPaymentKey()))
							{
								if(value)
								{
									createAndSearchLotTableDTO.setCheckBoxStatus("true");
									//tableDTO.setChkSelect(true);
								}
								else
								{
									createAndSearchLotTableDTO.setCheckBoxStatus("false");
									//tableDTO.setChkSelect(false);
								}
							}
						}
					}	
					
				}
			}
		});
	}

	
	public void setValueForCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTabledto.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								if(null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}
						else
						{
							searchTabledto.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
								if(null != chkBox)
									chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		}
	
			
		
	public void setFinalTableList(List<CreateAndSearchLotTableDTO> tableRows) {
		Boolean isListEmpty = false;
//		tableDataList = new ArrayList<CreateAndSearchLotTableDTO>();
	//	List<CreateAndSearchLotTableDTO> dtoList = new ArrayList<CreateAndSearchLotTableDTO>();
		if(null != tableRows && !tableRows.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
				
				/**
				 * When user tries to navigate from forward to previous page.
				 * already added records shouldn't be added to the tableDataList.
				 * Hence another list where keys are stored is used, where if a key is
				 * already existing in that list, then it won't get added in 
				 * the main list.This is done to avoid duplication.
				 * 
				 * */
				
				if(null != tableDataList && !tableDataList.isEmpty() && null != tableDataKeyList && !tableDataKeyList.isEmpty())
				{
					if(!tableDataKeyList.contains(createAndSearchLotTableDTO.getClaimPaymentKey()))
							{
								tableDataList.add(createAndSearchLotTableDTO);
								tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
							}
				}
				else
				{
					isListEmpty = true;
					break;
				}
			}
			/**
			 * 
			 * When first page is painted, table data list would be empty
			 * and hence adding all the records and its keys to data list and
			 * key list
			 * 
			 * */
			if(isListEmpty)
			{
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableRows) {
					tableDataList.add(createAndSearchLotTableDTO);
					tableDataKeyList.add(createAndSearchLotTableDTO.getClaimPaymentKey());
				}
				
			}
			
		}
		// TODO Auto-generated method stub
		
	}
	
	public List<CreateAndSearchLotTableDTO> getTableItems()
	{
		return tableDataList;
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
	
	
	public String isValid()
	{
		
		StringBuffer err = new StringBuffer(); 	
		
		List<CreateAndSearchLotTableDTO> requestTableList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : requestTableList) {
					
				if(null != createAndSearchLotTableDTO.getChkSelect())
				{
				if(true==createAndSearchLotTableDTO.getChkSelect())
				{
					finalListForProcessing.add(createAndSearchLotTableDTO);
				}				
				}
				}
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : finalListForProcessing) {
				
		//if(null != createAndSearchLotTableDTO.getNoOfDiffDays() &&  createAndSearchLotTableDTO.getNoOfDiffDays() >3 && (null ==createAndSearchLotTableDTO.getRemarks() || ("").equalsIgnoreCase(createAndSearchLotTableDTO.getRemarks())))
				if((!( createAndSearchLotTableDTO.getNoOfDaysExceedingforCalculation().equals(createAndSearchLotTableDTO.getNoofdaysexceeding())) || !(createAndSearchLotTableDTO.getInterestAmntForCalculation().equals(createAndSearchLotTableDTO.getIntrestAmount())))
						&& (null ==createAndSearchLotTableDTO.getRemarks() || ("").equalsIgnoreCase(createAndSearchLotTableDTO.getRemarks())))
			{
					 err.append("\nPlease Enter remarks for the Intimation Number:").append(createAndSearchLotTableDTO.getIntimationNo()).append("<br>");
				}
			}
			
		}
		return err.toString();
	}
	

	 public void addBeanToList(CreateAndSearchLotTableDTO createLotDTO) {
	    	data.addBean(createLotDTO);
	 }
	 
	 public void addList(List<CreateAndSearchLotTableDTO> createLotDTO) {
		 for (CreateAndSearchLotTableDTO createandSearchLotDto : createLotDTO) {
			 data.addBean(createandSearchLotDto);
		 }
	 }
	 
	 @SuppressWarnings("unchecked")
	public List<CreateAndSearchLotTableDTO> getValues() {
		 @SuppressWarnings("unchecked")
			List<CreateAndSearchLotTableDTO> itemIds = (List<CreateAndSearchLotTableDTO>) this.table.getItemIds() ;
	    	return itemIds;
    	
	}

	public void setTableList(List<CreateAndSearchLotTableDTO> tableItems,
			String createBatch) {
		table.removeAllItems();
		//List<CreateAndSearchLotTableDTO> tableItems = tableRows.getPageItems();
		if(null != tableItems && !tableItems.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableItems) {
				
				table.addItem(createAndSearchLotTableDTO);
			}
			
			
			
		}
		
		
	}
	
	public void setTotalNoOfRecords(int iSize)
	{
		fLayout.removeAllComponents();
		totalRocordsTxt = new Label(String.valueOf(iSize));
		totalRocordsTxt.setCaption("Total Number Of Records :");
		fLayout.addComponent(totalRocordsTxt);

	}
	
	public void sortTableList()
	{
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	}
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_VIEW_DOCUMENTS,intimationNo);
		//fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
	}
	public void showEditPaymentDetailsView(CreateAndSearchLotTableDTO tableDTO)
	{
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
	}
	
	public Pageable getPageable()
	{
		return this.pageUI.getPageable();
	}
	
	public void setHasNextPage(boolean flag)
	{
		this.pageUI.hasMoreRecords(flag);
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
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

	public void addSearchListener(Searchable searchable)
	{
		this.searchable = searchable;
	}
	
	
	
	public void setRowColor(final CreateAndSearchLotTableDTO dto){
		
		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				
				CreateAndSearchLotTableDTO dto1 = (CreateAndSearchLotTableDTO) itemId;
				if(dto1 != null){
				String colourFlag = null != dto1.getRecStatusFlag() ? dto1.getRecStatusFlag():"";
				//System.out.println("-------No Of times loop executes-----");
				if(colourFlag.equals("E")){
					
					return "yellow";
				}
				else if(colourFlag.equals("F")){
					
					return "red";
				}
				else if(colourFlag.equals("R"))
				{
					return "amber";
				}
				else
				{
					return "none";
				}
			
			}
				else{
					return "none";
				}
			}
	
		});
		
	}
	
		
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,EditPaymentDetailsView editPaymentView) {
		editPaymentView.populatePreviousPaymentDetails(tableDTO);
		
	}


}
