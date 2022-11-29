package com.shaic.claim.reports.notAdheringToANHReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.UsertoCPUMappingService;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class SearchNotAdheringToANHReportUI extends ViewComponent {

	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private HorizontalLayout searchfieldLayout;
	private FormLayout cpuFrmLayout;
	private HorizontalLayout buttonLayout;
	private Button searchBtn;
	private Button resetBtn;
	private Button exportBtn;
	private Label dummyLabel;
	private PopupDateField fromDate; 
	private PopupDateField toDate;
	private ComboBox cpuCmb;
	private BeanItemContainer<SelectValue> cpuValueContainer;
	
	private ExcelExport excelExport;
	
	@Inject
	private SearchNotAdheringToANHReportTable notAdheringToANHRTable;
	
	BeanItemContainer<SelectValue> cpucontainer;
	
	@EJB
	private UsertoCPUMappingService  cpuService; 
	
	
	@PostConstruct
	public void init()
	{
		wholeVerticalLayout = new VerticalLayout();
		wholeVerticalLayout.setSizeFull(); 
		wholeVerticalLayout.addComponent(buildSearchPanel());
		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		
		String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		cpuValueContainer = cpuService.getRevisedUserCpuContainer(userId);
		cpuCmb.setContainerDataSource(cpuValueContainer);
		cpuCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cpuCmb.setItemCaptionPropertyId("value");
		
		notAdheringToANHRTable.init("", false, false);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	public void setCPUContainer(BeanItemContainer<SelectValue> cpucontainer){
		cpuValueContainer = cpucontainer;
	}
	private Panel buildSearchPanel(){
		searchVerticalLayout = buildSearchLayout();
		
		searchPanel = new Panel("Not Adhering to ANH-Report");
		searchPanel.setSizeFull();
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
		 absoluteLayout_3.setHeight("100px");
		 
		fromDate = new PopupDateField("From Date");
		fromDate.setDateFormat("dd/MM/yyyy");
		fromDate.setTextFieldEnabled(false);
		fromDate.setRangeEnd(new Date());
		fromDate.setDateOutOfRangeMessage("From Date must be before or equal to current Date");
		
		toDate = new PopupDateField("To Date");
		toDate.setDateFormat("dd/MM/yyyy");
		toDate.setTextFieldEnabled(false);
		toDate.setRangeEnd(new Date());
		toDate.setDateOutOfRangeMessage("To Date must be before or equal to current Date");
		
		FormLayout fromDtFrm = new FormLayout();
		fromDtFrm.addComponent(fromDate);
		FormLayout toDtFrm = new FormLayout();
		toDtFrm.addComponent(toDate);
		
		cpuCmb = new ComboBox("CPU Code");
		cpuFrmLayout = new FormLayout(cpuCmb);
		
		searchfieldLayout = new HorizontalLayout(fromDtFrm,toDtFrm,cpuFrmLayout);
		searchfieldLayout.setSpacing(true);		
		
		searchVerticalLayout.addComponent(searchfieldLayout);
		searchVerticalLayout.setMargin(true);		

		dummyLabel =new Label();
		dummyLabel.setWidth("30px");
		buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
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
				SelectValue cpuCode = (SelectValue)cpuCmb.getValue();
				WeakHashMap<String,Object> filters = new WeakHashMap<String, Object>();
				
				if(validateFields())
					{
						filters.put("fromDate", frmdate);
						filters.put("endDate",endDate);
						filters.put("cpucode",cpuCode);
						
						fireViewEvent(SearchNotAdheringToANHPresenter.SEARCH_BTNN_CLICK, filters);
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

		Label dummyLabel1 =new Label();
		dummyLabel1.setWidth("30px");
		buttonLayout.addComponents(searchBtn,dummyLabel1,resetBtn);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		absoluteLayout_3.addComponent(searchfieldLayout);
		absoluteLayout_3.addComponent(buttonLayout, "top:70.0px;left:190.0px;");
		searchVerticalLayout.addComponent(absoluteLayout_3);
		
		
		
		return searchVerticalLayout;
	}

	@SuppressWarnings("deprecation")
	public boolean validateFields(){
	
		Date frmdate = fromDate.getValue();
		Date endDate = toDate.getValue();
		
		
		if(fromDate != null && toDate != null ){
			
			try {
				fromDate.validate();
				frmdate = fromDate.getValue();
			} catch (Exception e) {
				fromDate.setValue(null);
				showErrorPopup("Please Enter Valid Date");
				return false;
			}
			
			try {
				toDate.validate();
				endDate = toDate.getValue();
			} catch (Exception e) {
				toDate.setValue(null);
				showErrorPopup("Please Enter Valid Date");
				return false;
			}
			
			if((frmdate != null && endDate != null )){
				
				if(frmdate.after(endDate))
				{
					showErrorPopup("From date should not be greater than the To date");
					return false;
				}
				if(endDate.before(frmdate)){
					showErrorPopup("Please Enter Valid To Date");
					return false;
				}
				if(endDate.after(frmdate))
				{
					if(frmdate.getYear() == endDate.getYear() && (endDate.getMonth() - frmdate.getMonth()) > 5)
					{
						showErrorPopup("Date Range should not be Greater than 6 Months");
						return false;	
					}
					if(frmdate.getYear() < endDate.getYear()){
						int prevMonths = 12 - frmdate.getMonth();
						int curMonths = endDate.getMonth()+1;
						if((prevMonths+curMonths)>6){
							showErrorPopup("Date Range should not be Greater than 6 Months");
							return false;							
						}
					}
				 }	
				if(frmdate.before(endDate) || frmdate.compareTo(endDate)>=0)
				{
				  return true;
				}
			}
			else{
			      showErrorPopup("Please Enter Valid From Date / To Date");	
			      return false;
			}
				
		}
		else{
		      showErrorPopup("Please Enter From Date and To Date");	
		}
		
		return false;
			
	}
	
	public void showTable(List<NewIntimationNotAdheringToANHDto> claimList){
		
		if(claimList != null && !claimList.isEmpty()){
			clearSearchTable();
			
			notAdheringToANHRTable.setTableList(claimList);
			searchVerticalLayout.addComponent(notAdheringToANHRTable);
			
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
			Button homeButton = new Button("Not Adhering to ANH Report Home");
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
					fireViewEvent(MenuItemBean.NOT_ADHERING_TO_ANH_REPORT, null);
					
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
				excelExport = new ExcelExport(notAdheringToANHRTable.getTable());
				excelExport.setReportTitle("Not Adhering to ANH Report");
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
		notAdheringToANHRTable.setTableList(new ArrayList<NewIntimationNotAdheringToANHDto>());
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
		cpuCmb.setValue(null);
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
