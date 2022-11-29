package com.shaic.claim.registration.balancesuminsured.view;

import java.util.List;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.outpatient.registerclaim.dto.OPBillDetailsDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

public class BillDetailsTable extends
	GBaseTable<OPBillDetailsDTO> {
	/**
	* 
	*/
	
	private static final long serialVersionUID = 1L;

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","details", "billDateStr","billNumber","claimedAmount","nonPayableAmt","payableAmt","nonPayableReason"};*/

	@Override
	public void removeRow() {
	// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
	//setSizeFull();
	table.setContainerDataSource(new BeanItemContainer<OPBillDetailsDTO>(
			OPBillDetailsDTO.class));
	table.setHeight("180px");
	table.setColumnFooter("details", "Total Amt");
	table.setFooterVisible(true);
	Object[] NATURAL_COL_ORDER = new Object[] {
		"serialNumber","details", "billDateStr","billNumber","claimedAmount","nonPayableAmt","payableAmt","nonPayableReason"};
	table.setVisibleColumns(NATURAL_COL_ORDER);
	//table.setHeight("200px");

	}
public Integer calculateTotal() {
		
		List<OPBillDetailsDTO> itemIconPropertyId = (List<OPBillDetailsDTO>) table.getItemIds();
		Integer claimeAmt = 0;
		Integer nonPayableAnmt = 0;
		Integer payableAmt = 0;
		for (OPBillDetailsDTO dto : itemIconPropertyId) {
		    Integer claimeAmount = SHAUtils.getIntegerFromString(dto.getClaimedAmount()) ;
		    claimeAmt += claimeAmount != null ? claimeAmount : 0;
			Integer nonPayableAmount = SHAUtils.getIntegerFromString(dto.getNonPayableAmt());
			nonPayableAnmt +=  null != nonPayableAmount ? nonPayableAmount : 0;
			Integer payableAmount = SHAUtils.getIntegerFromString(dto.getPayableAmt());
			payableAmt += null != payableAmount ? payableAmount : 0;
		}
		table.setColumnFooter("claimedAmount", String.valueOf(claimeAmt));
		table.setColumnFooter("nonPayableAmt", String.valueOf(nonPayableAnmt));
		table.setColumnFooter("payableAmt", String.valueOf(payableAmt));
		return payableAmt;
	}
	@Override
	public void tableSelectHandler(OPBillDetailsDTO t) {
	// TODO
	// fireViewEvent(MenuItemBean.SHOW_HOSPITAL_ACKNOWLEDGE_FORM,
	// t.getKey());
	}

	@Override
	public String textBundlePrefixString() {
	return "view-billdetails-";
	}

	}

