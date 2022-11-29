package com.shaic.claim.reports.intimationAlternateCPUReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAUtils;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class IntimationAlternateCPUwiseReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout searchfieldLayout;
	private HorizontalLayout buttonLayout;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private Label dunnmyLabel;
	private PopupDateField fromDate; 
	private PopupDateField toDate;
	
	private ExcelExport excelExport;
	
	@Inject
	private IntimationAlternateCPUwiseReportTable intimationAlternateCPUDetailsTable;
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.setSizeFull();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		intimationAlternateCPUDetailsTable.init("", false, false);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Intimation Alternate CPU wise Report");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel"); 
		searchPanel.setContent(searchVerticalLayout);
		
		return searchPanel;
	}
	
	private VerticalLayout buildSearchLayout(){
		AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");
		 searchVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("150px");
		 
		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
		
		
		FormLayout fromDtFrm = new FormLayout();
		fromDtFrm.addComponent(fromDate);
		FormLayout toDtFrm = new FormLayout();
		toDtFrm.addComponent(toDate);
		searchfieldLayout = new HorizontalLayout(fromDtFrm,toDtFrm);
		searchfieldLayout.setSpacing(true);
		searchfieldLayout.setMargin(true);
		
		
		buttonLayout = new HorizontalLayout();
		
		searchBtn = new Button("Search");
		searchBtn.setWidth("-1px");
		searchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchBtn.setDisableOnClick(true);
		searchBtn.addStyleName("hover");
		//Vaadin8-setImmediate() searchBtn.setImmediate(true);
		searchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				((Button)event.getSource()).setEnabled(true);
				Date frmdate = fromDate.getValue();
				Date endDate = toDate.getValue(); 
				
				Map<String,Object> filters = new HashMap<String, Object>();
				
				if(validateFields())
					{
						filters.put("fromDate", frmdate);
						filters.put("endDate",endDate);
					
						fireViewEvent(IntimationAlternateCPUwiseReportPresenter.SEARCH_INTIMATED_RISK_DETAILS_REPORT, filters);
					}
			}
		});
		
		dunnmyLabel= new Label();
		dunnmyLabel.setWidth("30px");
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		//Vaadin8-setImmediate() resetBtn.setImmediate(false);
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetAllValues();

				if(exportBtn != null){
					buttonLayout.removeComponent(exportBtn);
					buttonLayout.removeComponent(dunnmyLabel);
					exportBtn = null;
				}
				
			}
		});
		Label dummyLabel= new Label();
		dummyLabel.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel,resetBtn);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		absoluteLayout_3.addComponent(searchfieldLayout);
		
		absoluteLayout_3.addComponent(buttonLayout, "top:100.0px;left:190.0px;");

		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		
		return searchVerticalLayout;
	}

	public boolean validateFields(){
	
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
		
		if(frmdate != null && endDate != null ){
			
			if(!SHAUtils.validateDate(frmdate) ){
				showErrorPopup("Please Enter Valid Date");
				return false;
			}
			if(!SHAUtils.validateDate(endDate)){

				showErrorPopup("Please Enter Valid Date");
				return false;
			}
				
				if(frmdate.before(endDate) || frmdate.compareTo(endDate)>=0)
				{
				  return true;
				}
				
				if(frmdate.after(endDate))
				{
					showErrorPopup("From date should not be greater than the To date");
					return false;
				}
				if(endDate.before(frmdate)){
					showErrorPopup("Please Enter Valid To Date");
					return false;
				}
				
		}
		else{
		      showErrorPopup("Please Enter From Date and To Date");	
		}
		
		return false;
			
	}
	
	public void showTable(List<IntimationAlternateCPUwiseReportDto> riskDetailsList){
		
		if(riskDetailsList != null && !riskDetailsList.isEmpty()){			
			clearSearchTable();
			intimationAlternateCPUDetailsTable.setTableList(riskDetailsList);
			searchVerticalLayout.addComponent(intimationAlternateCPUDetailsTable);
			
			if(exportBtn == null){
				addExportButton();
			}
		}
		
		else{
			
			
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dunnmyLabel);
				exportBtn = null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Intimation Alternate CPU wise Report Home");
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
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();	
					resetAllValues();
					fireViewEvent(MenuItemBean.INTIMATION_ALTERNATE_CPU_REPORT, null);
					
				}
			});
			
		}
	}

	private void addExportButton(){
		
		exportBtn = new Button("Export to Excel");
		buttonLayout.addComponent(dunnmyLabel);
		buttonLayout.addComponent(exportBtn);
		
		
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(intimationAlternateCPUDetailsTable.getTable());
				excelExport.setReportTitle("Intimation Alternate CPU Wise Report");
				excelExport.setDisplayTotals(false);
				excelExport.export();
				
			}
		});
		
		buttonLayout.addComponent(exportBtn);
	}

	public void resetAllValues(){
		clearSearchFields();
		clearSearchTable();
		
	}
	public void clearSearchTable(){
		intimationAlternateCPUDetailsTable.setTableList(new ArrayList<IntimationAlternateCPUwiseReportDto>());
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dunnmyLabel);
			exportBtn = null;
		}
		excelExport = null;
	}
	public void clearSearchFields(){
		fromDate.setValue(null);
		toDate.setValue(null);
	}
	
	public void showErrorPopup(String eMsg)
	{
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
	searchBtn.setEnabled(true);
	}
	
}
