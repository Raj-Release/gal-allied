package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.csvalidation.CSValidator;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.reimbursement.createandsearchlot.EditPaymentDetailsView;
import com.shaic.claim.rod.wizard.dto.PreviousAccountDetailsDTO;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ntv.narenj
 *
 */
public class SearchCreateBatchTable extends GBaseTable<CreateAndSearchLotTableDTO>{

	private Window popup; 
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","chkSelect","editpaymentdetails","viewdetails","intimationNo", "claimNo", "policyNo", 
		"rodNo","paymentStatus","product","cpuCode","paymentType","ifscCode","beneficiaryAcntNo","branchName","typeOfClaim","lotNo","approvedAmt",
		"paymentPartyMode","payeeName","payableAt","panNo","providerCode","emailID","reconsiderationFlag","faApprovedAmnt",
		"lastAckDateValue","faApprovedDateValue","numberofdays","noofdaysexceeding","intrestamount","penalTotalAmnt","remarks"}; 
	
	private final static Object[] SEARCH_BATCH_CIL_ORDER = new Object[]{"serialNumber","chkSelect","accountBatchNo","paymentReqDateValue","userId","typeOfClaim","claimCount","showdetails"};
	
	//public List<Component> compList = null;
	public HashMap<Long,Component> compMap = null;
	public List<CreateAndSearchLotTableDTO> tableDataList = new ArrayList();
	public List<Long> tableDataKeyList = new ArrayList();
	Double approvedAmnt = 0d;
	
	@Inject
	SearchCreateBatchTableForPopUp tableForPopUp;
	

	@Override
	public void removeRow() {
		table.removeAllItems();		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<CreateAndSearchLotTableDTO>(CreateAndSearchLotTableDTO.class));	
		generatecolumns();
		table.setValidationVisible(false);
		table.sort(new  Object[] { "numberofdays" }, new boolean[] { false });
	//	table.sort("numberofdays", false);

	}

	@Override
	public void tableSelectHandler(
			CreateAndSearchLotTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	public void setVisibleColumnForSearchBatch()
	{
		table.setVisibleColumns(SEARCH_BATCH_CIL_ORDER);
	}
	public void setVisiblecolumnForCreateBatch()
	{
		table.setVisibleColumns(NATURAL_COL_ORDER);
	}

	@Override
	public String textBundlePrefixString() {
		
		return "search-create-batch-";
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
		
		
		
		table.removeGeneratedColumn("noofdaysexceeding");
		table.addGeneratedColumn("noofdaysexceeding", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final com.vaadin.v7.ui.TextField txtNoOfDaysExceeding = new com.vaadin.v7.ui.TextField();
				
				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;	
				txtNoOfDaysExceeding.setData(tableDTO);
				
				if(null != tableDTO.getNumberofdays())
				{
					if(tableDTO.getNoofdaysexceeding() >= -3)
					{	
						if((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) || (tableDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)))
					{
						txtNoOfDaysExceeding.setEnabled(false);
					} 
							else
							{
								txtNoOfDaysExceeding.setEnabled(true);
							}
					}
					else
					{					
						txtNoOfDaysExceeding.setEnabled(false);							
						
					}
				}
				
				if(null != tableDTO.getNoofdaysexceeding())
				txtNoOfDaysExceeding.setValue(String.valueOf(tableDTO.getNoofdaysexceeding()));
				//txtNoOfDays.setValue("0");
				txtNoOfDaysExceeding.addValueChangeListener(new ValueChangeListener() {
						
						
						@Override
						public void valueChange(Property.ValueChangeEvent event) {	
					
						
							String value = (String)event.getProperty().getValue();
							
							CreateAndSearchLotTableDTO	tableDTO =  (CreateAndSearchLotTableDTO) ((TextField)event.getProperty()).getData();
							
							Integer intNoOfDays = 0;
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
									
									Double  faApprovedAmnt = null != tableDTO.getFaApprovedAmnt() ? tableDTO.getFaApprovedAmnt() : 0d;
									Double interestRate = 0d;
									
									if((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) ||
											tableDTO.getDocReceivedFrom().equals(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL))
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
									
									Double intrestAmnt = 0d;
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
										
										tableDTO.setIntrestAmount(Math.abs(approvedAmnt));
									//	tableDTO.setPenalInterestAmntForCalculation(Math.abs(approvedAmnt));
										Collection<?> itemIds = table.getItemIds();
										for (Object itemId : itemIds) {
											Property containerProperty = table.getContainerProperty(itemId, "intrestamount");
											if(containerProperty != null) {
												TextField txtField = (TextField) containerProperty.getValue();
												txtField.setReadOnly(false);
												txtField.setValue("hello");
												break;
											}
										}
									}
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
					});
				
			
				//txtNoOfDaysExceeding.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				txtNoOfDaysExceeding.setWidth("150px");
				//txtNoOfDaysExceeding.addStyleName(ValoTheme.BUTTON_LINK);
				CSValidator delayDaysValidator = new CSValidator();
				delayDaysValidator.extend(txtNoOfDaysExceeding);
				delayDaysValidator.setRegExp("^[0-9 .-]*$");
				delayDaysValidator.setPreventInvalidTyping(true);
				//Vaadin8-setImmediate() txtNoOfDaysExceeding.setImmediate(true);
				//txtNoOfDaysExceeding.setValue("");
				
				//delayDaysValidator.
				return txtNoOfDaysExceeding;
			}
		});
		
		table.removeGeneratedColumn("intrestamount");
		table.addGeneratedColumn("intrestamount", new Table.ColumnGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
					
				final CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)itemId;		
				
				com.vaadin.v7.ui.TextField txtIntrestAmount = new com.vaadin.v7.ui.TextField();
				
				if(tableDTO.getReconsiderationFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)) 
				{
						if((tableDTO.getPaymentStatusKey() == ReferenceTable.CORRECTION_PAYMENT_STATUS_ID) || (tableDTO.getDocReceivedFrom().equalsIgnoreCase(SHAConstants.DOC_RECEIVED_FROM_HOSPITAL)))
				{
					txtIntrestAmount.setEnabled(false);
				} 
						else
						{
							txtIntrestAmount.setEnabled(true);
						}
				}
				else
				{
					txtIntrestAmount.setEnabled(false);
				}
				
				txtIntrestAmount.addValueChangeListener(new ValueChangeListener() {
					
					
					@Override
					public void valueChange(Property.ValueChangeEvent event) {	
					
						String value = (String)event.getProperty().getValue();
						
						Double doublevalue = 0d;
						
						if(null != value && !(value.equals("")))
						{
							
							doublevalue = Double.valueOf(value);
							if(doublevalue >= 0)
							{
							tableDTO.setIntrestAmount(doublevalue);
							}
							else
							{						
								//fireViewEvent(SearchCreateBatchPresenter.VALIDATION, doublevalue, null);
							}
						}
						
					}
				});
			
			//	txtIntrestAmount.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				txtIntrestAmount.setWidth("150px");
				txtIntrestAmount.addStyleName(ValoTheme.BUTTON_LINK);
				
				CSValidator intrestAmntValidator = new CSValidator();
				intrestAmntValidator.extend(txtIntrestAmount);
				intrestAmntValidator.setRegExp("^[0-9 .]*$");
				intrestAmntValidator.setPreventInvalidTyping(true);
				
				if(null != tableDTO.getIntrestAmount())
					txtIntrestAmount.setValue(String.valueOf(tableDTO.getIntrestAmount()));
				//Vaadin8-setImmediate() txtIntrestAmount.setImmediate(true);
				return txtIntrestAmount;
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
					
					
				
					
					
					/*boolean value = (Boolean) event.getProperty().getValue();
					CreateAndSearchLotTableDTO tableDTO = (CreateAndSearchLotTableDTO)chkBox.getData();
					if(value)
					{
						tableDTO.setCheckBoxStatus("true");
					}
					else
					{
						tableDTO.setCheckBoxStatus("false");
					}*/
					
				}
			}
		});
	}
	
	/*public void setValueForCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTabledto.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTabledto.getClaimPaymentKey());
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
								chkBox.setValue(value);
							}
							break;
						}
						
					}
			}
		}
		
		if(null != compList && !compList.isEmpty())
		{
			for (Component comp : compList) {
				CheckBox chkBox = (CheckBox) comp;
				chkBox.setValue(value);
			}
		}
		//System.out.println("----------");
	}*/
	
	public void setValueForCheckBox(Boolean value)
	{
		List<CreateAndSearchLotTableDTO> searchTableDTOList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		
		for (CreateAndSearchLotTableDTO searchTableDTO : searchTableDTOList) {
			//for (CreateAndSearchLotTableDTO searchTabledto : tableDataList){
				//if(searchTabledto.getClaimPaymentKey().equals(searchTableDTO.getClaimPaymentKey()))
					{
						if(value)
						{
							searchTableDTO.setCheckBoxStatus("true");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							//break;
						}
						else
						{
							searchTableDTO.setCheckBoxStatus("false");
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
								chkBox.setValue(value);
							}
							//break;
						}
						
					}
			//}
		}
		}
	
	public void setValueForSelectAllCheckBox(Boolean value)
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
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getClaimPaymentKey());
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
	
	public List<CreateAndSearchLotTableDTO> getTableAllItems()
	{
		return (List<CreateAndSearchLotTableDTO>)table.getItemIds();
	}
	
	protected void tablesize(){
		table.setPageLength(table.size()+1);
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}
	
	public void showEditPaymentDetailsView(CreateAndSearchLotTableDTO tableDTO)
	{
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_EDIT_PAYMENT_DETAILS_VIEW,tableDTO);
	}
	
	public void showClaimsDMSView(String intimationNo)
	{
		fireViewEvent(SearchCreateBatchPresenter.CREATE_BATCH_SHOW_VIEW_DOCUMENTS,intimationNo);
		//fireViewEvent(CreateAndSearchLotPresenter.SHOW_VIEW_DOCUMENTS,intimationNo);
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
			/*if(null != dtoList && !dtoList.isEmpty()){
				for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : dtoList) {
					tableDataList.add(createAndSearchLotTableDTO);
				}
			}*/
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
	
	public void populatePreviousPaymentDetails(
			PreviousAccountDetailsDTO tableDTO,EditPaymentDetailsView editPaymentView) {
		editPaymentView.populatePreviousPaymentDetails(tableDTO);
	
		
	}
	
	public String isValid()
	{
		
		StringBuffer err = new StringBuffer(); 
		/*if(null != tableDataList && !tableDataList.isEmpty())
		{
			
			
			
			for (CreateAndSearchLotTableDTO dto : tableDataList) {	
				
				
			if(null != dto.getNoOfDiffDays() &&  dto.getNoOfDiffDays() >= -3 && (null ==dto.getRemarks() || ("").equalsIgnoreCase(dto.getRemarks())))
			{
				 err += "\nPlease Enter remarks for the Intimation Number:" + dto.getIntimationNo() +"<br>";
			}
			
		}
			
	}*/
		
		List<CreateAndSearchLotTableDTO> requestTableList = (List<CreateAndSearchLotTableDTO>) table.getItemIds();
		List<CreateAndSearchLotTableDTO> finalListForProcessing = null;
		if(null != requestTableList && !requestTableList.isEmpty())
		{
			finalListForProcessing = new ArrayList<CreateAndSearchLotTableDTO>();
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : tableDataList) {
								
				if(("true").equalsIgnoreCase(createAndSearchLotTableDTO.getCheckBoxStatus()))
				{
					finalListForProcessing.add(createAndSearchLotTableDTO);
				}				
		
				}
		}
		if(null != finalListForProcessing && !finalListForProcessing.isEmpty())
		{
			for (CreateAndSearchLotTableDTO createAndSearchLotTableDTO : finalListForProcessing) {
				
			
							if(null != createAndSearchLotTableDTO.getNoOfDiffDays() &&  createAndSearchLotTableDTO.getNoOfDiffDays() >= -3 && (null ==createAndSearchLotTableDTO.getRemarks() || ("").equalsIgnoreCase(createAndSearchLotTableDTO.getRemarks())))
				{
					 err.append("\nPlease Enter remarks for the Intimation Number:").append(createAndSearchLotTableDTO.getIntimationNo()).append("<br>");
				}
			}
			
		}
		return err.toString();
	}
	
	//private void generateInterestAmountColumn()
	

		
}
