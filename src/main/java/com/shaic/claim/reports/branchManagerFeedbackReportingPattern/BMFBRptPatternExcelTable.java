package com.shaic.claim.reports.branchManagerFeedbackReportingPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.WeakHashMap;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.claim.reports.branchManagerFeedBack.BranchManagerFeedBackReportDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;
/**
 * Part of CR R1238
 * @author Lakshminarayana
 *
 */
public class BMFBRptPatternExcelTable extends GBaseTable<BranchManagerFeedBackReportingPatternDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}
	
	public Object[] getColHeaders(int days){
		
		if(days == 28)
			return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value"};
		else if(days == 29)
			return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value", "d29Value"};
		else if(days == 30)
			return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value", "d29Value", "d30Value"};
		
		return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value", "d29Value", "d30Value", "d31Value"};		
	}

	public Object[] get30DayColHeaders(){
		return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value", "d29Value", "d30Value"};
	}
	public Object[] get29DayColHeaders(){
		return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value", "d29Value"};
	}
	public Object[] get28DayColHeaders(){
		return new Object[] {
				"branchCode", "branchName", "d1Value", "d2Value", "d3Value", "d4Value", "d5Value",
				"d6Value", "d7Value", "d8Value", "d9Value", "d10Value", "d11Value", "d12Value", "d13Value", "d14Value", "d15Value", "d16Value",
				"d17Value", "d18Value", "d19Value", "d20Value", "d21Value", "d22Value", "d23Value", "d24Value", "d25Value", "d26Value", "d27Value",
				"d28Value"};

	}
	public void setReportColHeader(int days){
		table.setVisibleColumns(getColHeaders(days));
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedBackReportingPatternDto>(BranchManagerFeedBackReportingPatternDto.class));
			setReportColHeader(31);
			table.setColumnCollapsingAllowed(false);
			Object[] visibleCols = getColHeaders(31);
			for(int i = 0; i < visibleCols.length; i++){
				if(((String)visibleCols[i]).contains("d") && !("branchCode").equalsIgnoreCase((String)visibleCols[i]) ){
					table.setColumnWidth(visibleCols[i], 30);		
					table.setColumnAlignment(visibleCols[i], Align.CENTER);
				}
			}
			table.setColumnWidth("branchName", 284);
			table.setHeight("250px");	
	}

	public void removeNAColumns(List<String> colmList){
		for (String string : colmList) {
			table.removeGeneratedColumn(string);
		}
	}
	
	@Override
	public void tableSelectHandler(BranchManagerFeedBackReportingPatternDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "bm-reporting-pattern-excel-";
	}
	
	public List<BranchManagerFeedBackReportingPatternDto> getTableList()
	{
		Collection coll = table.getItemIds();
		List<BranchManagerFeedBackReportingPatternDto> list = new ArrayList<BranchManagerFeedBackReportingPatternDto>();
		if (coll != null && coll instanceof List){
			list = (List)coll;
		}
		else{
			if(coll != null){
				list = new ArrayList<BranchManagerFeedBackReportingPatternDto>(coll);
			}
		}
		return list;
	}
}
