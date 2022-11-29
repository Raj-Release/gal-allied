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
public class BranchManagerFeedBackReportingPatternTable extends GBaseTable<BranchManagerFeedBackReportingPatternDto>{
	
	@Override
	public void removeRow() {
		table.removeAllItems();		
		
	}
	
	public Object[] getColHeaders(){
		return new Object[] {
				"branchCode", "branchName", "d1", "d2", "d3", "d4", "d5",
				"d6", "d7", "d8", "d9", "d10", "d11", "d12", "d13", "d14", "d15", "d16",
				"d17", "d18", "d19", "d20", "d21", "d22", "d23", "d24", "d25", "d26", "d27",
				"d28", "d29", "d30", "d31"};
	}

	public void setReportColHeader(){
		table.setVisibleColumns(getColHeaders());
	}
	
	@Override
	public void initTable() {
			setSizeFull();
			table.setContainerDataSource(new BeanItemContainer<BranchManagerFeedBackReportingPatternDto>(BranchManagerFeedBackReportingPatternDto.class));
			setReportColHeader();
			table.setColumnCollapsingAllowed(false);
			Object[] visibleCols = getColHeaders();
			for(int i = 0; i < visibleCols.length; i++){
				if(((String)visibleCols[i]).contains("d") && !("branchCode").equalsIgnoreCase((String)visibleCols[i]) ){
					table.setColumnWidth(visibleCols[i], 30);		
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
	
	public void loadImageColumns(){
		table.removeGeneratedColumn("d1");
		table.addGeneratedColumn("d1", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD1() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
									
				if(tableDto.getD1().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				
				if(tableDto.getD1().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
			Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		
		table.removeGeneratedColumn("d2");
		table.addGeneratedColumn("d2", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD2() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD2().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD2().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d3");
		table.addGeneratedColumn("d3", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD3() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD3().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD3().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d4");
		table.addGeneratedColumn("d4", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD4() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD4().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD4().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d5");
		table.addGeneratedColumn("d5", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD5() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD5().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD5().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d6");
		table.addGeneratedColumn("d6", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD6() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD6().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD6().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;				
			}
		});
		table.removeGeneratedColumn("d7");
		table.addGeneratedColumn("d7", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD7() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD7().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD7().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d8");
		table.addGeneratedColumn("d8", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD8() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD8().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD8().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d9");
		table.addGeneratedColumn("d9", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD9() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD9().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD9().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d10");
		table.addGeneratedColumn("d10", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD10() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD10().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD10().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d11");
		table.addGeneratedColumn("d11", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD11() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD11().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD11().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d12");
		table.addGeneratedColumn("d12", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD12() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD12().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD12().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d13");
		table.addGeneratedColumn("d13", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD13() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD13().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD13().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d14");
		table.addGeneratedColumn("d14", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD14() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD14().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD14().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d15");
		table.addGeneratedColumn("d15", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD15() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				else{
					rptPatternImg = feedbackRptPatternMap.get(0);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);
				ratingBtn.setEnabled(false);
				
				if(tableDto.getD15().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD15().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d16");
		table.addGeneratedColumn("d16", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD16() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD16().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD16().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d17");
		table.addGeneratedColumn("d17", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD17() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD17().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD17().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d18");
		table.addGeneratedColumn("d18", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD1() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD18().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD18().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d19");
		table.addGeneratedColumn("d19", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD19() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD19().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setWidth("25");
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD19().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d20");
		table.addGeneratedColumn("d20", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD20() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD20().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD20().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d21");
		table.addGeneratedColumn("d21", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD21() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD21().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD21().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d22");
		table.addGeneratedColumn("d22", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD22() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD22().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD22().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d23");
		table.addGeneratedColumn("d23", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD23() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD23().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD23().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d24");
		table.addGeneratedColumn("d24", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD24() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD24().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD24().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d25");
		table.addGeneratedColumn("d25", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD25() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD25().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD25().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d26");
		table.addGeneratedColumn("d26", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD26() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD26().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD26().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d27");
		table.addGeneratedColumn("d27", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD27() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD27().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD27().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d28");
		table.addGeneratedColumn("d28", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD28() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD28().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD28().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d29");
		table.addGeneratedColumn("d29", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD29() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD29().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD29().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d30");
		table.addGeneratedColumn("d30", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD30() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD30().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);
					sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD30().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		table.removeGeneratedColumn("d31");
		table.addGeneratedColumn("d31", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(final Table source,
					final Object itemId, Object columnId) {
				Button ratingBtn = new Button();
				BranchManagerFeedBackReportingPatternDto tableDto = (BranchManagerFeedBackReportingPatternDto)itemId;
				WeakHashMap<Integer, String> feedbackRptPatternMap = SHAUtils.getBMFBRptPatternImgMap();
				
				String rptPatternImg = feedbackRptPatternMap.get(0);
				
				if(tableDto.getD31() > 0){
					rptPatternImg = feedbackRptPatternMap.get(1);
				}
				ratingBtn.setIcon(new ThemeResource("images/"+rptPatternImg));
				ratingBtn.addStyleName(BaseTheme.BUTTON_LINK);					
				
				if(tableDto.getD31().intValue() == -1){
					TextField sundayImg = new TextField();
					sundayImg.setValue("");
					sundayImg.setReadOnly(true);sundayImg.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					sundayImg.setWidth("23px");
					return sundayImg;
				}
				if(tableDto.getD31().intValue() == -2){
					rptPatternImg = feedbackRptPatternMap.get(-2);
				}
				Image imgpic = new Image("", new ThemeResource("images/"+rptPatternImg));imgpic.setWidth("23px");return imgpic;
			}
		});
		
		Object[] visibleCols = getColHeaders();
		for(int i = 0; i < visibleCols.length; i++){
			if(((String)visibleCols[i]).contains("d") && !("branchCode").equalsIgnoreCase((String)visibleCols[i])){
				table.setColumnAlignment(visibleCols[i], Align.CENTER);
			}
		}
		
	}
	@Override
	public void tableSelectHandler(BranchManagerFeedBackReportingPatternDto t) {
		
	}

	@Override
	public String textBundlePrefixString() {
		return "bm-reporting-pattern-";
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
