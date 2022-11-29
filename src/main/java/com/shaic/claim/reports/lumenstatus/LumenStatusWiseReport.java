package com.shaic.claim.reports.lumenstatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.MultiSelectCPU;
import com.shaic.arch.table.MultiSelectStatus;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class LumenStatusWiseReport extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private PopupDateField fromDate;
	private PopupDateField toDate;
//	private ComboBox lumenStatusCmb;
	
	@Inject
	private MultiSelectStatus lumenStatusCmb;
	
	private ComboBox clmTypeCmb;
	
	@Inject
	private MultiSelectCPU cpuCmbInstance;
	
//	private ComboBox cpuCmb;
	private Button btnSearch;
	private Button btnReset;
	private HorizontalLayout buttonLayout;
	private Button exportBtn; 
	private ExcelExport excelExport;
	private Label dummyLabel;
	private BeanFieldGroup<SearchLumenStatusWiseDto> binder;
	private SearchLumenStatusWiseDto bean;
	
	private BeanItemContainer<SelectValue> cpuContainer;
	private BeanItemContainer<SelectValue> clmTypeContainer;
	private BeanItemContainer<SelectValue> lumenStatusContainer;
	
	@Inject
	private LumenStatusWiseReportTable searchResultTable;
	
	public void init(BeanItemContainer<SelectValue> cpuContainer, BeanItemContainer<SelectValue> lumenStatusContainer, BeanItemContainer<SelectValue> clmTypeContainer)
	{
		initBinder();
		this.cpuContainer = cpuContainer;
		this.lumenStatusContainer = lumenStatusContainer;
		this.clmTypeContainer = clmTypeContainer;
		
		wholeVerticalLayout = new VerticalLayout(buildSearchPanel());
		searchResultTable.init("", false, false);
		wholeVerticalLayout.addComponent(searchResultTable);
		wholeVerticalLayout.setComponentAlignment(searchResultTable, Alignment.BOTTOM_CENTER);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	public VerticalLayout getContent(){
//		searchPanel = buildSearchPanel();
		wholeVerticalLayout.addComponent(searchPanel);
//		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		searchResultTable.init("", false, false);
		wholeVerticalLayout.addComponent(searchResultTable);
		wholeVerticalLayout.setComponentAlignment(searchResultTable, Alignment.BOTTOM_CENTER);
		return wholeVerticalLayout;
	}
	
	private void initBinder()
	{
		this.binder = new BeanFieldGroup<SearchLumenStatusWiseDto>(SearchLumenStatusWiseDto.class);
		
		this.bean = new SearchLumenStatusWiseDto();
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}

	public void setDropDownValues() 
	{
//		lumenStatusCmb.setContainerDataSource(lumenStatusContainer);
//		lumenStatusCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		lumenStatusCmb.setItemCaptionPropertyId("value");
		
		lumenStatusCmb.init("Status",lumenStatusContainer);
		
		clmTypeCmb.setContainerDataSource(clmTypeContainer);
		clmTypeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		clmTypeCmb.setItemCaptionPropertyId("value");

//		cpuCmb.setContainerDataSource(cpuContainer);
//		cpuCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//		cpuCmb.setItemCaptionPropertyId("value");
		
		cpuCmbInstance.init("CPU Office",cpuContainer);
		
		
	}		
	
//	private Panel buildSearchPanel(){
	private VerticalLayout buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
//		return searchPanel;
		return searchVerticalLayout;
	}
	
	private VerticalLayout buildSearchLayout(){		
		
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");
		 searchVerticalLayout.setMargin(false);
		 
		searchPanel = new Panel("Lumen - Status Wise Report");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
//		searchPanel.setContent(searchVerticalLayout);
		
		searchVerticalLayout.addComponent(searchPanel);
		searchVerticalLayout.addStyleName("g-search-panel");
		 
		 
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("40px");
		 
		 dummyLabel =new Label();
		 dummyLabel.setWidth("30px");
		 
		 buttonLayout = new HorizontalLayout();

		fromDate = binder.buildAndBind("From Date", "frmDate", PopupDateField.class);
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setRangeEnd(new Date());
		
		toDate = binder.buildAndBind("To Date", "toDate", PopupDateField.class);
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setRangeEnd(new Date());
		clmTypeCmb = binder.buildAndBind("Claim Type", "claimType", ComboBox.class);
				
//		lumenStatusCmb = binder.buildAndBind("Status", "statusSelect", ComboBox.class);
//		cpuCmb = binder.buildAndBind("CPU Office", "cpuSelect", ComboBox.class);
		
		setDropDownValues();
		
		Label dummyLabel1 =new Label();
		dummyLabel1.setSizeFull();
		Label dummyLabel2 =new Label();
		dummyLabel2.setSizeFull();
		FormLayout leftForm = new FormLayout(fromDate,clmTypeCmb);
		FormLayout rightForm = new FormLayout(toDate);
		HorizontalLayout frmLayout = new HorizontalLayout(leftForm,rightForm);
		HorizontalLayout multiSelectHLayout = new HorizontalLayout(lumenStatusCmb,cpuCmbInstance);
		multiSelectHLayout.setSpacing(true);
		multiSelectHLayout.setMargin(true);
		
		frmLayout.setMargin(true);
		frmLayout.setSpacing(true);
		
		this.btnSearch = new Button("Search");
		btnSearch.setWidth("-1px");
		btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSearch.setDisableOnClick(true);
		btnSearch.addStyleName("hover");
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);

		this.btnReset = new Button("Reset");
		btnReset.setWidth("-1px");
		btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
		//Vaadin8-setImmediate() btnReset.setImmediate(true);
		
		Label dummyLabel3 =new Label();
		dummyLabel3.setWidth("30px");
		
		buttonLayout.addComponents(btnSearch,dummyLabel3,btnReset);
		buttonLayout.setSpacing(true);

		addListener();
		
		searchVerticalLayout.addComponents(frmLayout,multiSelectHLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:0.0px;left:200.0px;");
		
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					btnSearch.setEnabled(false);
//					bean.setCpuCodeList(cpuCmbInstance.getSelectedCpuIds());						
					fireViewEvent(LumenStatusWiseReportPresenter.SEARCH_LUMEN_STATUS_WISE,bean);	
				}				
			}
		});
		
		btnReset.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				fireViewEvent(LumenStatusWiseReportPresenter.RESET_LUMEN_STATUS_REPORT,null);
			}
		});
		
		
	}
	
	public void showResultTable(List<SearchLumenStatusWiseDto> resultListDto){
		
		btnSearch.setEnabled(true);
		if(resultListDto != null && !resultListDto.isEmpty()){
				
		searchResultTable.setTableList(resultListDto);
		
		if(exportBtn == null){
			addExportButton();
		}
		
		}
		else{
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn=null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Search Lumen Status Wise Report Home");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			HorizontalLayout hLayout = new HorizontalLayout(layout);
			hLayout.setMargin(true);
			
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setCaption("");
			dialog.setClosable(false);
			dialog.setContent(hLayout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
			
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					resetSearchForm();
					fireViewEvent(MenuItemBean.LUMEN_STATUS_WISE_REPORT, null);					
				}
			});
		}
		
	}
	
	public void resetSearchForm(){
		if(btnSearch != null){
			btnSearch.setEnabled(true);
		}
		if(fromDate != null){
			this.fromDate.setValue(null);
		}
		if(toDate != null){
			this.toDate.setValue(null);
		}
		if(lumenStatusCmb != null){
//			this.lumenStatusCmb.setValue(null);
			lumenStatusCmb.reset();
		}
		if(cpuCmbInstance != null ){
			this.cpuCmbInstance.reset(); 
		}
//		if(cpuCmb != null){
//			this.cpuCmb.setValue(null);
//		}		
		if(clmTypeCmb != null){
			this.clmTypeCmb.setValue(null);
		}
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		if(searchResultTable != null){
			searchResultTable.removeRow();
		}
		
	}
	public LumenStatusWiseReportTable getSearchResultTable(){
	
		return searchResultTable;
	}
	private boolean validateSearchBean(){
		try{
			boolean hasError = false;
			StringBuffer errMsg = new StringBuffer("");
			if(!binder.isValid()){
				
				if(fromDate.getValue() == null){
					hasError = true;
					errMsg.append("Pleae Enter Valid From Date<br>");
				}
				if(toDate.getValue() == null){
					hasError = true;
					errMsg.append("Pleae Enter Valid To Date<br>");
				}
//				if(lumenStatusCmb.getSelectedCpuIds() == null || lumenStatusCmb.getSelectedCpuIds().isEmpty()){
//					hasError = true;
//					errMsg.append("Please Select Value for Lumen Status<br>");
//				}
								
				if(hasError && errMsg.length()>0){
					showErrorPopup(errMsg.toString());
					return false;
				}
				return false;
			}else{
				
				binder.commit();
				this.bean = binder.getItemDataSource().getBean();
				this.bean.setCpuCodeList(cpuCmbInstance.getSelectedCpuIds());
				this.bean.setStatusList(lumenStatusCmb.getSelectedCpuIds());
				
				if(null != bean.getFrmDate() && null != bean.getToDate() ){
					
					if(bean.getToDate().compareTo(bean.getFrmDate())<0){
							
						hasError = true;
						errMsg.append("From Date should not be greater than To Date<br>");
					}
					else{
						
						Calendar fromday = Calendar.getInstance();
						fromday.setTime(bean.getFrmDate());
						Calendar today = Calendar.getInstance();	
						today.setTime(bean.getToDate());
						
						Calendar currentDate = Calendar.getInstance();
						currentDate.setTime(new Date());
						
						int diffMonths = today.get(Calendar.MONTH) - fromday.get(Calendar.MONTH);
						int diffYrs = today.get(Calendar.YEAR) - fromday.get(Calendar.YEAR);
						
						if(diffMonths > 4 || (diffMonths < 0 && (12 + diffMonths) > 4) || (diffMonths == 0 && diffYrs >= 1) || (diffMonths > 4 && diffYrs >= 1) ){
							hasError = true;
							errMsg.append("Date Range should not be greater than 120 Days<br>");
						}
						else if(diffMonths == 4 || (diffMonths < 0 && (12 + diffMonths) == 4)){
						
							int totalDays = 0;
							
							totalDays = fromday.getActualMaximum(Calendar.DAY_OF_MONTH) - fromday.get(Calendar.DATE) + 1;
							
							fromday.add(Calendar.MONTH, 1);
							
							if(diffMonths > 0){
								for(int d = fromday.get(Calendar.MONTH) ; d < today.get(Calendar.MONTH); d++){
									totalDays += fromday.getActualMaximum(Calendar.DAY_OF_MONTH);
									fromday.add(Calendar.MONTH, 1);
								}
								
								totalDays += (today.get(Calendar.DATE) < today.getActualMaximum(Calendar.DAY_OF_MONTH) ? today.get(Calendar.DATE) : today.getActualMaximum(Calendar.DAY_OF_MONTH));
								
								if(totalDays > 120){
									hasError = true;
									errMsg.append("Date Range should not be greater than 120 Days<br>");
								}
							}
							else{
								
								for(int d = fromday.get(Calendar.MONTH) ; d < 12 ;d++){
									totalDays += fromday.getActualMaximum(Calendar.DAY_OF_MONTH);
									fromday.add(Calendar.MONTH, 1);
								}
								for(int d = 0 ; d < today.get(Calendar.MONTH) ;d++){
									totalDays += fromday.getActualMaximum(Calendar.DAY_OF_MONTH);
									today.add(Calendar.MONTH, 1);
								}
								
								totalDays += (today.get(Calendar.DATE) < today.getActualMaximum(Calendar.DAY_OF_MONTH) ? today.get(Calendar.DATE) : today.getActualMaximum(Calendar.DAY_OF_MONTH));
								
								if(totalDays > 120){
									hasError = true;
									errMsg.append("Date Range should not be greater than 120 Days<br>");
								}
							}
						}
						
					}
						
//						if(toDate.getValue() != null && fromDate.getValue() != null){
//							if(toDate.getValue().compareTo(fromDate.getValue()) < 0){
//								hasError = true;
//								errMsg.append("From Date should not be greater than To Date<br>");
//							}
//						}
//						return true;					
					
//					if(lumenStatusCmb.getSelectedCpuIds() == null || lumenStatusCmb.getSelectedCpuIds().isEmpty()){
//						hasError = true;
//						errMsg.append("Please Select atlease One Lumen Status<br>");
//					}
					
				}	
				
			  else{
					if(null == bean.getFrmDate()){
						hasError = true;
						errMsg.append("Please Select From Date<BR>");
					}
					if(null == bean.getToDate()){
						hasError = true;
						errMsg.append("Please Select To Date<BR>");
					}
//					if(lumenStatusCmb.getSelectedCpuIds() == null || lumenStatusCmb.getSelectedCpuIds().isEmpty()){
//						hasError = true;
//						errMsg.append("Please Select atlease One Lumen Status<br>");
//					}
										
//					if(lumenStatusCmb.getSelectedCpuIds() == null || lumenStatusCmb.getSelectedCpuIds().isEmpty()){
//						hasError = true;
//						errMsg.append("Please Select atlease One Lumen Status<BR>");
//					}
			  }
				
				if(hasError && errMsg.length()>0){
					showErrorPopup(errMsg.toString());
					return false;
				}
				return true;				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
	return false;
	}
	
	private void showErrorPopup(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Errors");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		btnSearch.setEnabled(true);
		
	}
	
	private void addExportButton() {
		exportBtn = new Button("Export to Excel");
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(searchResultTable.getTable());
				excelExport.setReportTitle("Lumen Status Wise Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
			}
		});
		buttonLayout.addComponent(dummyLabel);
		buttonLayout.addComponent(exportBtn);
		
	}	
	
}
