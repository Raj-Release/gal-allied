package com.shaic.claim.reports.intimatedRiskDetailsReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
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
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class IntimatedRiskDetailsReport extends ViewComponent {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout searchfieldLayout;
	private HorizontalLayout buttonLayout;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private PopupDateField fromDate; 
	private PopupDateField toDate;
	private TextField intimationNoTxt;
	private Label dummyLabel;
	
	private Map<String,Object> filters;
		
	
	private ExcelExport excelExport;
	
	@Inject
	private IntimatedRiskDetailsReportTable intimatedRiskDetailsTable;
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.setSizeFull();
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		intimatedRiskDetailsTable.init("", false, false);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		searchPanel = new Panel("Intimated Risk Details Report");
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
		 absoluteLayout_3.setHeight("160px");

		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
		
		intimationNoTxt = new TextField("Intimation No");
		CSValidator intimationNoValidator = new CSValidator();
		intimationNoValidator.extend(intimationNoTxt);
		intimationNoValidator.setPreventInvalidTyping(true);
		intimationNoValidator.setRegExp("^[a-zA-Z 0-9/]*$");
		
		
		FormLayout fromDtFrm = new FormLayout();
		fromDtFrm.addComponents(fromDate,intimationNoTxt);
		FormLayout toDtFrm = new FormLayout();
		toDtFrm.addComponent(toDate);
		searchfieldLayout = new HorizontalLayout(fromDtFrm,toDtFrm);
		searchfieldLayout.setSpacing(true);
		searchfieldLayout.setMargin(true);
		dummyLabel = new Label();
		dummyLabel.setWidth("30px");
		
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
				
				
				if(validateFields())
					{					
						fireViewEvent(IntimatedRiskDetailsReportPresenter.SEARCH_INTIMATED_RISK_DETAILS_REPORT, filters);
					}
			}
		});
		
		
		resetBtn = new Button("Reset");
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		
		resetBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				resetAllValues();

				if(exportBtn != null){
					buttonLayout.removeComponent(exportBtn);
					buttonLayout.removeComponent(dummyLabel);
					exportBtn = null;
				}
				
			}
		});
		absoluteLayout_3.addComponent(searchfieldLayout);
		buttonLayout = new HorizontalLayout();
		

		Label dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel,resetBtn);
			buttonLayout.setSpacing(true);


		absoluteLayout_3.addComponent(buttonLayout, "top:100.0px;left:190.0px;");


		searchVerticalLayout.addComponent(absoluteLayout_3);

		
		return searchVerticalLayout;
	}

	public boolean validateFields(){
	
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
		
		filters = new HashMap<String, Object>();
		
		filters.put("intimationNo", "%"+intimationNoTxt.getValue()+"%");
		
		if(frmdate != null && endDate != null ){
			
			if(!SHAUtils.validateDate(frmdate) ){
				showErrorPopup("Please Enter Valid From Date");
				return false;
			}
			if(!SHAUtils.validateDate(endDate)){

				showErrorPopup("Please Enter Valid To Date");
				return false;
			}
				
				if(frmdate.before(endDate) || frmdate.compareTo(endDate)<=0)
				{
					filters.put("fromDate", frmdate);
					filters.put("endDate",endDate);
					return true;
				}				
				else
				{
					showErrorPopup("From date should not be greater than the To date");
					filters.remove("fromDate");
					filters.remove("endDate");
					return false;
				}				
		}
		else if(frmdate != null && endDate == null && SHAUtils.validateDate(frmdate)){
			Date currDate = new Date();
			if(frmdate.before(currDate) || frmdate.compareTo(currDate)<=0)
			{
				endDate = currDate;
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}
			else{
				endDate = frmdate;
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}			
		}
		else if(frmdate == null && endDate != null && SHAUtils.validateDate(frmdate)){
			Date currDate = new Date();
			if(endDate.before(currDate) || endDate.compareTo(currDate)<=0)
			{
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}
			else{
				endDate = frmdate;
				filters.put("fromDate", frmdate);
				filters.put("endDate",endDate);
				return true;
			}			
		}
		else if(frmdate == null && endDate == null){
			filters.put("fromDate", frmdate);
			filters.put("endDate",endDate);
		}
		return true;
			
	}
	
	public void showTable(List<IntimatedRiskDetailsReportDto> riskDetailsList){
		
		if(riskDetailsList != null && !riskDetailsList.isEmpty()){			
			clearSearchTable();
			intimatedRiskDetailsTable.setTableList(riskDetailsList);
			searchVerticalLayout.addComponent(intimatedRiskDetailsTable);
			
			if(exportBtn == null){
				addExportButton();
			}
		}
		
		else{			
			
			if(exportBtn != null){
				buttonLayout.removeComponent(exportBtn);
				buttonLayout.removeComponent(dummyLabel);
				exportBtn = null;
			}
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Intimated Risk Details Report Home");
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
					fireViewEvent(MenuItemBean.INTIMATED_RISK_DETAILS_REPORT, null);
					
				}
			});
			
		}
	}

	private void addExportButton(){
		
		exportBtn = new Button("Export to Excel");
		buttonLayout.addComponent(exportBtn);
		buttonLayout.addComponent(dummyLabel);
		
		
		exportBtn.addClickListener(new Button.ClickListener() {
				
			@Override
			public void buttonClick(ClickEvent event) {
				excelExport = new ExcelExport(intimatedRiskDetailsTable.getTable());
				excelExport.setReportTitle("Intimated Risk Details Report");
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
		intimatedRiskDetailsTable.setTableList(new ArrayList<IntimatedRiskDetailsReportDto>());
		if(exportBtn != null){
			buttonLayout.removeComponent(exportBtn);
			buttonLayout.removeComponent(dummyLabel);
			exportBtn = null;
		}
		excelExport = null;
	}
	public void clearSearchFields(){
		fromDate.setValue(null);
		toDate.setValue(null);
		intimationNoTxt.setValue("");
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
