package com.shaic.claim.bulkconvertreimb.search;

import java.util.List;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

public class SearchBulkConvertReimbTable extends
		GBaseTable<SearchBulkConvertReimbTableDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Object[] VISIBLE_COL_ORDER = new Object[] { "srlNo",
			"intimationNumber", "cpuCode", "intimatedDateValue", "intimatedDays" };

	public static final Object[] VISIBLE_EXCEL_COL_ORDER = new Object[] { "srlNo",
		"crno","subcrno","intimationNumber", "cpuCode", "intimatedDateValue", "intimatedDays","letterDateValue" };
	
	@EJB
	private ClaimService claimService;
	
	@Override
	public void removeRow() {
		table.removeAllItems();

	}
	
	//private List<SearchBulkConvertReimbTableDto> selectedList;

	@Override
	public void initTable() {
		setSizeFull();
		getLayout().removeComponent(getfLayout());
		getLayout().removeComponent(getCaptionLayout());
		table.setContainerDataSource(new BeanItemContainer<SearchBulkConvertReimbTableDto>(
				SearchBulkConvertReimbTableDto.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("300px");
		autoGenerateSelectColumn();
		
		
	}

	public void autoGenerateSelectColumn() {
		table.removeGeneratedColumn("Select");
		table.addGeneratedColumn("Select",
			new Table.ColumnGenerator() {
				@Override
				public Object generateCell(Table source,
						final Object itemId, Object columnId) {
					
					final CheckBox selectChk = new CheckBox();
					
					if((SearchBulkConvertReimbTableDto)itemId != null){
					
						final SearchBulkConvertReimbTableDto letterDto = (SearchBulkConvertReimbTableDto)itemId;
						selectChk.setValue(letterDto.getSelected());
						selectChk.setData(letterDto);						
						selectChk
								.addValueChangeListener(new Property.ValueChangeListener() {
									
									@Override
									public void valueChange(ValueChangeEvent event) {
										SearchBulkConvertReimbTableDto letterDto = (SearchBulkConvertReimbTableDto)selectChk.getData();
									Boolean selectedValue = (Boolean)event.getProperty().getValue();
									//IMSSUPPOR-27155
									Claim claimDetails =null;
									if(letterDto!=null && letterDto.getClaimKey()!=null){
										claimDetails = claimService.getClaimByKey(letterDto.getClaimKey());
									}
									if(!letterDto.getIsOnLoad()){
									if (null != claimDetails && null != claimDetails.getStatus() && null != claimDetails.getStatus().getKey() 
											&& claimDetails.getStatus().getKey()
											.equals(ReferenceTable.CLAIM_CLOSED_STATUS)) {
										letterDto.setSelected(Boolean.FALSE);	
										letterDto.setIsOnLoad(Boolean.TRUE);
										selectChk.setValue(Boolean.FALSE);
										getErrorMessage("Selected claim has been closed.Cannot proceed further.");
									}else{
										letterDto.setIsOnLoad(Boolean.FALSE);
										letterDto.setSelected(selectedValue);
										//Vaadin8-setImmediate() selectChk.setImmediate(true);
									}
									}else{
										letterDto.setIsOnLoad(Boolean.FALSE);
									}
									//letterDto.setSelected(selectedValue);									
									}
								});										
						//Vaadin8-setImmediate() selectChk.setImmediate(true);	
				        return selectChk;
					}
					else{
						return "";
					}
					}
				});
	}
	
	public void setExportExcelTableHeader(){
		table.setVisibleColumns(VISIBLE_EXCEL_COL_ORDER);
		table.removeGeneratedColumn("Select");
	}
	
	public void setSearchTableHeader(){
		table.setVisibleColumns(VISIBLE_COL_ORDER);		
	}
	

	@Override
	public void tableSelectHandler(SearchBulkConvertReimbTableDto t) {
		
//		
//		 VaadinSession session = getSession();
//			
//			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
//			try{
//				if(! isActiveHumanTask){
//				
//					SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
//					fireViewEvent(MenuPresenter.SHOW_CONVERT_CLAIM, t);
//					
//				}else{
//					getErrorMessage("This record is already opened by another user");
//				}
//			}catch(Exception e){
//				
//				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
//				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
//				e.printStackTrace();
//				
//			}
//		
//		
//		// fireViewEvent(MenuPresenter.SHOW_HOSPITAL, t.getKey());
		
	}

	@Override
	public String textBundlePrefixString() {
		return "convertBulkClaimType-";
	}
	
	
public void getErrorMessage(String eMsg){
		
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

public List<SearchBulkConvertReimbTableDto> getTableList(){
	
	return (List<SearchBulkConvertReimbTableDto>)table.getContainerDataSource().getItemIds();
}

}
