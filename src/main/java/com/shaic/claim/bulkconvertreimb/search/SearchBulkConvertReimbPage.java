package com.shaic.claim.bulkconvertreimb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Searchable;
import com.shaic.domain.Claim;
import com.shaic.domain.ClaimService;
import com.shaic.domain.PreauthService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class SearchBulkConvertReimbPage extends ViewComponent {

	/**
	 * 
	 */
	
	@Inject
	private SearchBulkConvertReimbTable searchResultTable;	
	
	@Inject
	private BatchConvertedTable	searchBulkConvertTable;
	
	//private List<SearchBatchConvertedTableDto> prevBatchList;
	
	@EJB
	private PreauthService preauthService;
	
	private static final long serialVersionUID = 1L;
	
	private FormLayout searchBulkFormLayout;
	
	private TextField intimationNoTxt;
	
	private OptionGroup searchOption;
	
	private TextField crnoTxt;
	
	private Map<String,String> searchfields = new HashMap<String, String>();
	
	private ComboBox cmbType;
	
	private ComboBox cpuCodeCmb;
	
	private Button convertClaimSearchBtn;
	private Button resetBtn;
	
	private Button convertBatchSearchBtn;
	private Button resetBatchBtn;
	
	private BeanFieldGroup<SearchBulkConvertFormDto> binder;
	
	private TabSheet mainTabSheet;
	
	protected VerticalLayout convertVerticalLayout;
	protected VerticalLayout printCoveringLetterLayout;
	
	private VerticalLayout buildConvertClaimSearchLayout;
	
	//private Panel convertClaimPanel;
	
	private Label totalRecLbl;
	
	private VerticalLayout searchTableLayout;

	private Searchable searchable;
	
	private Button convertButton;
	
	private CheckBox selectAllchk;
	
	@EJB
	private ClaimService claimService;
	
	public void addSearchListener(Searchable searable) {
		this.searchable = searable;
	}
	
	public SearchBulkConvertFormDto getSearchDTO() {
		try {
			
			if(cpuCodeCmb.getValue() != null && ((SelectValue)cpuCodeCmb.getValue()).getValue() != null){
				
				this.binder.commit();
				SearchBulkConvertFormDto bean = this.binder.getItemDataSource()
						.getBean();
				return bean;
			}
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@PostConstruct
	public void init()
	{
		initBinder();
		
		Panel convertClaimPanel	= new Panel();
		//Vaadin8-setImmediate() convertClaimPanel.setImmediate(false);
		convertClaimPanel.setWidth("100%");
//		convertClaimPanel.setHeight("50%");
		convertClaimPanel.setCaption("Convert Claim type to Reimbursement (Bulk)");
		convertClaimPanel.addStyleName("panelHeader");
		convertClaimPanel.addStyleName("g-search-panel");
		
		buildConvertClaimSearchLayout = buildConvertClaimLayout();
		convertClaimPanel.setContent(buildConvertClaimSearchLayout);
		setCompositionRoot(convertClaimPanel);
		
	}
		
	
	private void initBinder() {
		this.binder = new BeanFieldGroup<SearchBulkConvertFormDto>(
				SearchBulkConvertFormDto.class);
		this.binder
				.setItemDataSource(new SearchBulkConvertFormDto());
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
	}

	private VerticalLayout buildConvertClaimLayout() {
		
		buildConvertClaimSearchLayout  = new VerticalLayout();
		
		mainTabSheet = new TabSheet();
				
		convertVerticalLayout = buildConvertVerticalLayout();
		
		selectAllchk = new CheckBox("Select All");
		selectAllchk.addValueChangeListener(getSelectAllValueChangeListener());
		searchTableLayout = new VerticalLayout();
		
		HorizontalLayout totalRecordSelectlayout = new HorizontalLayout();
		totalRecordSelectlayout.setWidth("100%");
		FormLayout totalRecFrmLayout = new FormLayout();
		totalRecLbl = new Label();
		totalRecFrmLayout.addComponent(totalRecLbl);
		totalRecordSelectlayout.addComponent(totalRecFrmLayout);
		totalRecordSelectlayout.setComponentAlignment(totalRecFrmLayout, Alignment.MIDDLE_LEFT);
		FormLayout selectAllFrmLayout = new FormLayout();
		selectAllFrmLayout.addComponent(selectAllchk);
		selectAllFrmLayout.setHeight("-1px");
		selectAllFrmLayout.setWidth("-1px");
		totalRecordSelectlayout.addComponent(selectAllFrmLayout);
		totalRecordSelectlayout.setComponentAlignment(selectAllFrmLayout, Alignment.MIDDLE_RIGHT);
		searchTableLayout.addComponent(totalRecordSelectlayout);
		
		searchResultTable.init("", false, false);
		searchTableLayout.addComponent(searchResultTable);
		convertVerticalLayout.addComponent(searchTableLayout);
		convertButton = new Button("Convert");
		convertVerticalLayout.addComponent(convertButton);
		convertVerticalLayout.setComponentAlignment(convertButton, Alignment.BOTTOM_CENTER);
		
		convertButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				boolean isSelected = false;
				List<SearchBulkConvertReimbTableDto> bulkConversionListDto =  searchResultTable.getTableList();
				String errorMessage = "Please Select atleast One Intimation to Convert to Reimbursement"; 
				if(bulkConversionListDto != null && ! bulkConversionListDto.isEmpty()){
					
					for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : bulkConversionListDto) {
						if(searchBulkConvertReimbTableDto.getSelected()){
							isSelected = true;
							break;
						}
						else
							continue;
					}
					
				}
				
				if(isSelected){
					fireViewEvent(SearchBulkConvertReimbPresenter.SUBMIT_CONVERT_BUTTON_CLICK, bulkConversionListDto);	
				}
				else{
					showErrorMsg(errorMessage);
				}				
			}
		});
		
		printCoveringLetterLayout = buildPrintCoveringLetterLayout();
		searchBulkConvertTable.init("", false, false);
		printCoveringLetterLayout.addComponent(searchBulkConvertTable);
			
		mainTabSheet.addTab(convertVerticalLayout);
		mainTabSheet.addTab(printCoveringLetterLayout);
		
		mainTabSheet.getTab(convertVerticalLayout).setClosable(false);
		mainTabSheet.getTab(printCoveringLetterLayout).setClosable(false);
		
		
		mainTabSheet.setStyleName(ValoTheme.TABSHEET_FRAMED);
		mainTabSheet.setSizeFull();
		//Vaadin8-setImmediate() mainTabSheet.setImmediate(true);
		mainTabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
			
			 //Component selected = mainTabSheet.getSelectedTab();
	        public void selectedTabChange(SelectedTabChangeEvent event) {
	        	
	        		            
	        	TabSheet mainTab = (TabSheet)event.getTabSheet();
	        	
	        	//String tabName = mainTab.getCaption();	        			
	        	
	        	Layout selectedTab = (Layout) mainTab.getSelectedTab(); 
	        	
	        	String tabSheet = selectedTab.getCaption();
	        	
	        	if(tabSheet != null && ! tabSheet.isEmpty()){
	        		if((tabSheet.toLowerCase()).equalsIgnoreCase(SHAConstants.PRINT_COVERING_LETTER_TAB)){
	        			if(crnoTxt != null) {
	        				crnoTxt.setValue("");	
	        			}	        			
	        			if(intimationNoTxt != null) {
	        				intimationNoTxt.setValue("");
	        			}
	        			fireViewEvent(SearchBulkConvertReimbPresenter.GET_PREV_CONVERTED_BATCH_LIST, null);
	        		}
	        		else{
	        			searchResultTable.setSearchTableHeader();
	        			searchResultTable.autoGenerateSelectColumn();
	        			resetConversionTabValues();
	        			setTotalRecords("");
	        		}
	        	}	        	
	        }
	    });
		
		buildConvertClaimSearchLayout.addComponent(mainTabSheet);		
		
		return buildConvertClaimSearchLayout;
	}

	private ValueChangeListener getSelectAllValueChangeListener() {
		return new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean selected = (Boolean)event.getProperty().getValue();
								
					List<SearchBulkConvertReimbTableDto> bulkConversionListDto =  searchResultTable.getTableList();
					if(bulkConversionListDto != null && ! bulkConversionListDto.isEmpty()){
						for (SearchBulkConvertReimbTableDto searchBulkConvertReimbTableDto : bulkConversionListDto) {
							//IMSSUPPOR-27155
							Claim claimDetails = claimService.getClaimByKey(searchBulkConvertReimbTableDto.getClaimKey());
							if (null != claimDetails && null != claimDetails.getStatus() && null != claimDetails.getStatus().getKey()
									&& claimDetails.getStatus().getKey().equals(ReferenceTable.CLAIM_CLOSED_STATUS)) {
								searchBulkConvertReimbTableDto.setSelected(Boolean.FALSE);
//								showErrorMsg("Selected claim has been closed.Cannot proceed further.");
							}else{
								searchBulkConvertReimbTableDto.setSelected(selected);
							}
						}
						searchTableLayout.removeComponent(searchResultTable);  
						searchResultTable.init("", false, false);
						repaintSearchConvertTable(bulkConversionListDto);
						searchResultTable.autoGenerateSelectColumn();
						searchTableLayout.addComponent(searchResultTable);
					}			
			}
		};
	}

public VerticalLayout buildConvertVerticalLayout(){
		
		convertVerticalLayout = new VerticalLayout();
		convertVerticalLayout.setCaption(SHAConstants.CONVERT_CLAIM_REIMB_TAB);
		convertVerticalLayout.setSizeFull();
		convertVerticalLayout.setMargin(true);

		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 //Vaadin8-setImmediate() convertVerticalLayout.setImmediate(false);
		 convertVerticalLayout.setMargin(false);		 
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("100px");

		cpuCodeCmb = binder.buildAndBind("CPU Code", "cpuCode",
				ComboBox.class);
		
		cmbType = binder.buildAndBind("Type","type",ComboBox.class);
		
		FormLayout formLayout1 = new FormLayout(cpuCodeCmb);
		FormLayout formLayout2 = new FormLayout(cmbType);
		HorizontalLayout searchFormLayout = new HorizontalLayout(formLayout1 ,  formLayout2);
		searchFormLayout.setMargin(true);
		searchFormLayout.setSpacing(false);
		absoluteLayout_3.addComponent(searchFormLayout);
		
		convertClaimSearchBtn = new Button();
		convertClaimSearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		convertClaimSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertClaimSearchBtn.setWidth("-1px");
		convertClaimSearchBtn.setHeight("-10px");
		convertClaimSearchBtn.setDisableOnClick(true);
		absoluteLayout_3
		.addComponent(convertClaimSearchBtn, "top:70.0px;left:230.0px;");
		//Vaadin8-setImmediate() convertClaimSearchBtn.setImmediate(true);
		
		resetBtn = new Button();
		resetBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		resetBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBtn.setWidth("-1px");
		resetBtn.setHeight("-1px");
		absoluteLayout_3.addComponent(resetBtn, "top:70.0px;left:339.0px;");
		//Vaadin8-setImmediate() resetBtn.setImmediate(true);
		convertVerticalLayout.addComponent(absoluteLayout_3);
		
		addListener();
		
		return convertVerticalLayout;
	}
	
	public VerticalLayout buildPrintCoveringLetterLayout(){
		
		printCoveringLetterLayout = new VerticalLayout();
		
		printCoveringLetterLayout.setCaption(SHAConstants.PRINT_COVERING_LETTER_TAB);
		printCoveringLetterLayout.setSizeFull();		
		
		searchOption = new OptionGroup();		
		
		Collection<Boolean> searchValues = new ArrayList<Boolean>(2);
		searchValues.add(true);
		searchValues.add(false);

		searchOption.addItems(searchValues);
		searchOption.setItemCaption(true, "CR No Id");
		searchOption.setItemCaption(false, "Intimation No");
		searchOption.setStyleName("horizontal");
		//Vaadin8-setImmediate() searchOption.setImmediate(true);		

		convertBatchSearchBtn = new Button();
		convertBatchSearchBtn.setCaption("Search");
		//Vaadin8-setImmediate() convertBatchSearchBtn.setImmediate(true);
		convertBatchSearchBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		convertBatchSearchBtn.setWidth("-1px");
		convertBatchSearchBtn.setHeight("-10px");
		convertBatchSearchBtn.setDisableOnClick(true);
		//Vaadin8-setImmediate() convertBatchSearchBtn.setImmediate(true);
		convertBatchSearchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(searchfields != null && !searchfields.isEmpty()){
					fireViewEvent(SearchBulkConvertReimbPresenter.SEARCH_BATCH_IINTIMATION_BUTTON_CLICK, searchfields);
				}
				else{
					showErrorMsg("Please Enter value for CR No ID. / Intimation Number for Search");
					convertBatchSearchBtn.setEnabled(true);
				}
			}
				
			});		
		
		resetBatchBtn = new Button();
		resetBatchBtn.setCaption("Reset");
		//Vaadin8-setImmediate() resetBatchBtn.setImmediate(true);
		resetBatchBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		resetBatchBtn.setWidth("-1px");
		resetBatchBtn.setHeight("-10px");
		//Vaadin8-setImmediate() resetBatchBtn.setImmediate(true);
		
		resetBatchBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				resetprintCoveringLetterLayout();
			}
		});
		
		searchOption.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean batchId = (Boolean)event.getProperty().getValue();

				if(batchId){
					buildBatchIdLayout();					
				}
				else{
					buildIntimationNoLayout();					
				}
				
				convertBatchSearchBtn.setData(searchfields);
				
			}
		}
		);		
		
		FormLayout searchOptionFrm = new FormLayout();
		
		searchOptionFrm.addComponent(searchOption);
		printCoveringLetterLayout.addComponent(searchOptionFrm);
		
		searchBulkFormLayout = new FormLayout();
		buildBatchIdLayout();
		buildIntimationNoLayout();		
		printCoveringLetterLayout.addComponent(searchBulkFormLayout);
			
		HorizontalLayout btnLayout = new HorizontalLayout();
		
		btnLayout.addComponent(convertBatchSearchBtn);
		btnLayout.addComponent(resetBatchBtn);
		btnLayout.setSpacing(true);
		
		printCoveringLetterLayout.addComponent(searchBulkFormLayout);
		printCoveringLetterLayout.addComponent(btnLayout);
		printCoveringLetterLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_CENTER);
		
		return printCoveringLetterLayout;		
	}
	
	public void buildBatchIdLayout(){
		
		searchBulkFormLayout.removeAllComponents();
			crnoTxt = new TextField("CR No");
			searchBulkFormLayout.addComponent(crnoTxt);
			crnoTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				searchfields.clear();
				String value = (String)event.getProperty().getValue();
				if(value != null && !value.isEmpty()){
					searchfields.put(SHAConstants.SEARCH_BY_BATCH, value);	
				}
								
			}
		});
		
		searchBulkFormLayout.addComponent(crnoTxt);		
		
	}
	
	public void buildIntimationNoLayout(){
		
		searchBulkFormLayout.removeAllComponents();
		
		intimationNoTxt = new TextField("Intimation No");
		
		searchBulkFormLayout.addComponent(intimationNoTxt);
		intimationNoTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				searchfields.clear();
				String value = (String)event.getProperty().getValue();
				if(value != null && !value.isEmpty()){
					searchfields.put(SHAConstants.SEARCH_BY_INTIMATION,value);	
				}
								
			}
		});
		
		searchBulkFormLayout.addComponent(intimationNoTxt);
	}
	
	public void addListener() {

		convertClaimSearchBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {							
				convertClaimSearchBtn.setEnabled(true);
				SelectValue cpuSelect = (SelectValue)cpuCodeCmb.getValue();
				
				SelectValue typeValueSelect = (SelectValue)cmbType.getValue();
				
				if(cpuSelect != null && cpuSelect.getValue() != null  &&  typeValueSelect != null && typeValueSelect.getValue() != null){
				searchable.doSearch();
				}
				else{
					
					StringBuffer errMsg = new StringBuffer();
					if(cpuSelect == null || cpuSelect.getValue() == null){
						errMsg.append("Please Select atleaset one CPU Code for Search.");
					}
					if(typeValueSelect == null || typeValueSelect.getValue() == null){
						errMsg.append("Please Select atleaset one Type for Search.<br>");
					}					
					showErrorMsg(errMsg.toString());
				}
					
			}
		});
		
		resetBtn.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				resetConversionTabValues();
			}
		});
	}
	
	public void setDropDownValues(BeanItemContainer<SelectValue> cpuParam,BeanItemContainer<SelectValue> typeValueParam){		
		//cpuCodeCmb.setContainerDataSource(parameter);
		cpuCodeCmb.setContainerDataSource(cpuParam);
		cpuCodeCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		//cpuCodeCmb.setItemCaptionPropertyId("description");
		cpuCodeCmb.setItemCaptionPropertyId("value");
		
		cpuParam.sort(new Object[] {"value"}, new boolean[] {true});
		
		cmbType.setContainerDataSource(typeValueParam);
		cmbType.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbType.setItemCaptionPropertyId("value");
		
		typeValueParam.sort(new Object[] {"value"}, new boolean[] {true});
		
	}
	
	public void setPreviousBatchList(List<SearchBatchConvertedTableDto> prevBatchList){
		
		//this.prevBatchList = prevBatchList;
		repaintBulkConvertedTable(prevBatchList);
	}
	
	public void resetConversionTabValues(){
		
		cpuCodeCmb.setValue(null);
		cmbType.setValue(null);
		setTotalRecords("");
		selectAllchk.removeValueChangeListener(getSelectAllValueChangeListener());
		selectAllchk.setValue(false);
		searchResultTable.removeRow();
		selectAllchk.addValueChangeListener(getSelectAllValueChangeListener());
	}
	
	public void resetAlltheValues() 
	{
	}

	/*private void removeTableFromLayout()
	{
		
	}*/

	public void refresh()
	{
		System.out.println("---inside the refresh----");
		resetConversionTabValues();
	}
	
	public void repaintSearchConvertTable(List<SearchBulkConvertReimbTableDto> tableRows){
//		searchTableLayout.removeComponent(searchResultTable);
		searchResultTable.removeRow();
		String totalRecordValue = "<font \"forecolor:'cyan';\">Total No. Records :  "+tableRows.size();
		setTotalRecords(totalRecordValue);
		searchResultTable.setTableList(tableRows);
//		searchTableLayout.addComponent(searchResultTable);
		
	}
	
	public void repaintBulkConvertedTable(List<SearchBatchConvertedTableDto> bulkConvetedlist){
		convertBatchSearchBtn.setEnabled(true);
		printCoveringLetterLayout.removeComponent(searchBulkConvertTable);
		searchBulkConvertTable.initTable();
		searchBulkConvertTable.setTableList(bulkConvetedlist);
		printCoveringLetterLayout.addComponent(searchBulkConvertTable);
		
//		searchBulkConvertTable.setTableList(bulkConvetedlist);
//		convertBatchSearchBtn.setEnabled(true);		
		
	}
	
	public List<SearchBatchConvertedTableDto> getBulkTableList(){
		return searchBulkConvertTable.getTableList();
	}
	
	public void showErrorMsg(String errorMessage) {
		Label label = new Label(errorMessage, ContentMode.HTML);
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
	public void resetprintCoveringLetterLayout(){
		
		if(intimationNoTxt != null){
			intimationNoTxt.setValue("");
		}
		if (crnoTxt != null){
			crnoTxt.setValue("");
		}
		searchBulkConvertTable.removeRow();
	}
	
	public void setTotalRecords(String labelValue){
		totalRecLbl.setContentMode(ContentMode.HTML);
		totalRecLbl.setValue(labelValue);
	}
}
