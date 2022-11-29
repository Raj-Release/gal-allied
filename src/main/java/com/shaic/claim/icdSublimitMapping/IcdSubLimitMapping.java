package com.shaic.claim.icdSublimitMapping;

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
import com.shaic.claim.bulkconvertreimb.search.SearchBulkConvertReimbTableDto;
import com.shaic.claim.lumen.create.SearchLumenStatusWiseDto;
import com.shaic.domain.SublimitFunObject;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.v7.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@UIScoped
public class IcdSubLimitMapping extends ViewComponent  {
	
	private VerticalLayout wholeVerticalLayout;
	private Panel searchPanel;
	private VerticalLayout searchVerticalLayout;
	private TextField subLimitFindTxt;
	private VerticalLayout icdTableLayout;
	private Button btnSearch;
	private Button viewBtn;
//	private Button addBtn;
	
	private TextField subLimitNameTxt;
	private ComboBox icdChapterCmb;
	private ComboBox icdBlockCmb;	
	
	private Button submitBtn;
	private Button cancelBtn;
	
	private HorizontalLayout buttonLayout;
	private HorizontalLayout submitBtnLayout;
		
	private BeanFieldGroup<SearchICDSubLimitMappingDto> binder;
	private SearchICDSubLimitMappingDto bean;
	
	private SublimitFunObject currentSublimitObj;
	private VerticalLayout searchResultVLayout;

	private VerticalLayout resultVLayout;
	
	private CheckBox selectAllchk;
	private Label totalRecLbl;
	
	private HorizontalLayout totalRecordSelectlayout;
	
	private FormLayout selectAllFrmLayout;
	
	@Inject
	private IcdSubLimitMappingTable searchResultTable;
	
	public void initView()
	{
		
//		initBinder();
		wholeVerticalLayout = new VerticalLayout(buildSearchLayout());
		searchResultVLayout = new VerticalLayout();
		searchResultVLayout.setSpacing(true);
		selectAllchk = new CheckBox("Select All");
		selectAllchk.addValueChangeListener(getSelectAllValueChangeListener());
		
		totalRecordSelectlayout = new HorizontalLayout();
		totalRecordSelectlayout.setWidth("60%");
		totalRecordSelectlayout.setMargin(false);

		selectAllFrmLayout = new FormLayout();
		selectAllFrmLayout.setMargin(false);
		selectAllFrmLayout.setWidth("60%");
		
		submitBtnLayout = new HorizontalLayout();
		submitBtnLayout.setMargin(true);
		
		searchResultVLayout.removeComponent(totalRecordSelectlayout);
		searchResultVLayout.removeComponent(searchResultTable.getTable());
		searchResultVLayout.removeComponent(submitBtnLayout);		
		
		FormLayout totalRecFrmLayout = new FormLayout();
		totalRecFrmLayout.setMargin(false);
		totalRecLbl = new Label();
		totalRecFrmLayout.addComponent(totalRecLbl);
		totalRecordSelectlayout.addComponent(totalRecFrmLayout);
		totalRecordSelectlayout.setComponentAlignment(totalRecFrmLayout, Alignment.MIDDLE_LEFT);
		
		selectAllFrmLayout.addComponent(selectAllchk);
		totalRecordSelectlayout.addComponent(selectAllFrmLayout);
		totalRecordSelectlayout.setComponentAlignment(selectAllFrmLayout, Alignment.MIDDLE_RIGHT);		
		
		wholeVerticalLayout.addComponent(searchResultVLayout);
		searchResultTable.init("", false, false);
		currentSublimitObj = null;
		
		submitBtn = new Button("Submit");
		submitBtn.setWidth("-1px");
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		//Vaadin8-setImmediate() submitBtn.setImmediate(true);
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					fireViewEvent(IcdSubLimitMappingPresenter.SUBMIT_BTN_CLICK, bean);
				}				
			}
		});
		
		cancelBtn = new Button("Cancel");
		cancelBtn.setWidth("-1px");
		//Vaadin8-setImmediate() cancelBtn.setImmediate(true);

		cancelBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				showCancelLayout();
			}
		});
		submitBtnLayout.removeAllComponents();
		submitBtnLayout.addComponents(submitBtn,cancelBtn);
		submitBtnLayout.setSpacing(true);
		
//		wholeVerticalLayout.addComponent(searchResultTable);
//		wholeVerticalLayout.setComponentAlignment(searchResultTable, Alignment.BOTTOM_CENTER);
		setSizeFull();
		setCompositionRoot(wholeVerticalLayout);
	}

	public void showCancelLayout(){
		
		Label successLabel = new Label("<b style = 'color: black;'>Do you want to cancel ? </b>", ContentMode.HTML);			
		Button sButton = new Button("Yes");
		Button noButton = new Button("No");
		noButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		sButton.setStyleName(ValoTheme.BUTTON_DANGER);
		
		HorizontalLayout cnfrmLayout = new HorizontalLayout(sButton,noButton);
		cnfrmLayout.setSpacing(true);
		VerticalLayout layout = new VerticalLayout(successLabel, cnfrmLayout);
		layout.setComponentAlignment(cnfrmLayout, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		
		final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
		
		sButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				resetSearchForm();
				clearSearchResultFrmLayout();
				fireViewEvent(MenuItemBean.ICD_SUBLIMIT_MAPPING, null);					
			}
		});
		
		noButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();						
			}
		});		
	}
	public VerticalLayout getContent(){
//		searchPanel = buildSearchPanel();
		wholeVerticalLayout.addComponent(searchPanel);
//		wholeVerticalLayout.setComponentAlignment(searchPanel, Alignment.MIDDLE_LEFT);
		searchResultTable.init("", false, false);
		wholeVerticalLayout.addComponent(searchResultTable.getTable());
//		wholeVerticalLayout.setComponentAlignment(searchResultTable, Alignment.BOTTOM_CENTER);
		return wholeVerticalLayout;
	}
	
	private void initBinder()
	{
		if(this.binder != null){
			binder.clear();
		}
		this.binder = new BeanFieldGroup<SearchICDSubLimitMappingDto>(SearchICDSubLimitMappingDto.class);
		
		this.bean = new SearchICDSubLimitMappingDto();
		bean.setSublimitName(currentSublimitObj != null && currentSublimitObj.getName() != null ? currentSublimitObj.getName() : "");
		bean.setSublimitKey(currentSublimitObj != null ? currentSublimitObj.getKey() : null);
		this.binder.setItemDataSource(this.bean);
		this.binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
	}
	
	private VerticalLayout buildSearchLayout(){		
		
		 searchVerticalLayout = new VerticalLayout();
		 //Vaadin8-setImmediate() searchVerticalLayout.setImmediate(false);
		 searchVerticalLayout.setWidth("100.0%");
		 searchVerticalLayout.setMargin(false);
		 
		searchPanel = new Panel("ICD and Sublimit Mapping");
		searchPanel.addStyleName("panelHeader");
		searchPanel.addStyleName("g-search-panel");
		
		searchVerticalLayout.addComponent(searchPanel);
		searchVerticalLayout.addStyleName("g-search-panel");
		 
		 
		 AbsoluteLayout absoluteLayout_3 =  new AbsoluteLayout();
		 //Vaadin8-setImmediate() absoluteLayout_3.setImmediate(false);
		 absoluteLayout_3.setWidth("100.0%");
		 absoluteLayout_3.setHeight("80px");
		 		 
		 buttonLayout = new HorizontalLayout();
		 
		 subLimitFindTxt = new TextField("Sublimit");
		 subLimitFindTxt.setNullRepresentation("");
		 FormLayout srchFrm = new FormLayout(subLimitFindTxt);
		 srchFrm.setMargin(false);
		 subLimitFindTxt.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				String sublimitName = (String)event.getProperty().getValue();
				btnSearch.setData(sublimitName);
				viewBtn.setData(sublimitName);
			}
		});
		 		 
		this.btnSearch = new Button();
		btnSearch.setWidth("-1px");
		btnSearch.setDisableOnClick(true);
		btnSearch.setIcon(FontAwesome.SEARCH);
		btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		//Vaadin8-setImmediate() btnSearch.setImmediate(true);
		
		FormLayout srchBtnFrm = new FormLayout(btnSearch);
		srchBtnFrm.setMargin(false);
		HorizontalLayout frmHLayout = new HorizontalLayout(srchFrm,srchBtnFrm);
		frmHLayout.setMargin(false);
		frmHLayout.setSpacing(true);

		this.viewBtn = new Button("View");
		viewBtn.setWidth("-1px");
		viewBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		//Vaadin8-setImmediate() viewBtn.setImmediate(true);
		
//		this.addBtn = new Button("Add");
//		addBtn.setWidth("-1px");
//		addBtn.setIcon(FontAwesome.PLUS_SQUARE_O);
//		addBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		//Vaadin8-setImmediate() addBtn.setImmediate(true);
				
		Label dummyLabel1 =new Label();
		dummyLabel1.setWidth("30px");
		
		buttonLayout.addComponents(viewBtn,dummyLabel1/*,addBtn*/);
		buttonLayout.setSpacing(true);

		addListener();
		absoluteLayout_3.addComponent(frmHLayout, "top:10.0px;left:25.0px;");
		absoluteLayout_3.addComponent(buttonLayout, "top:45.0px;left:200.0px;");
		
		searchVerticalLayout.addComponents(absoluteLayout_3);
		
		return searchVerticalLayout;
	}
	
	private void addListener(){
		btnSearch.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				String sublimitName = (String) event.getButton().getData();
				
					btnSearch.setEnabled(false);
					fireViewEvent(IcdSubLimitMappingPresenter.SEARCH_ICD_SUBLIMIT_MAPPING,sublimitName);	
			}
		});
		
		viewBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				String sublimitName = (String) event.getButton().getData();
				if(sublimitName != null && !sublimitName.isEmpty() && 
						currentSublimitObj != null && currentSublimitObj.getName() != null && 
						currentSublimitObj.getName().equalsIgnoreCase(sublimitName)){
					fireViewEvent(IcdSubLimitMappingPresenter.VIEW_ICD_SUBLIMIT_MAPPING,sublimitName);
				}	
			}
		});
		
		/*addBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(validateSearchBean()){
					fireViewEvent(IcdSubLimitMappingPresenter.ADD_ICD_SUBLIMIT_MAPPING, bean);
				}
			}
		});*/
		
	}

	public void setIcdChapter(BeanItemContainer<SelectValue> icdChapterContainer){
		
		btnSearch.setEnabled(true);
		
		if(currentSublimitObj != null && currentSublimitObj.getName() != null){
			initBinder();
			searchResultVLayout.removeAllComponents();
			subLimitNameTxt = binder.buildAndBind("Sublimit", "sublimitName", TextField.class);
			icdChapterCmb = binder.buildAndBind("ICD Chapter", "icdChapterSelect", ComboBox.class);
			
			icdChapterCmb.setContainerDataSource(icdChapterContainer);
			icdChapterCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
			icdChapterCmb.setItemCaptionPropertyId("value");
			
			icdChapterCmb.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {

					SelectValue selectedIcdChapter = (SelectValue)event.getProperty().getValue();
					if(selectedIcdChapter != null && selectedIcdChapter.getId() != null)
						fireViewEvent(IcdSubLimitMappingPresenter.GET_ICD_BLOCK_FOR_ICD_CHAPTER,selectedIcdChapter.getId());
				}
			});
			
			icdBlockCmb = binder.buildAndBind("ICD Block", "icdBlockSelect", ComboBox.class);
			icdBlockCmb.setContainerDataSource(new BeanItemContainer<SelectValue>(SelectValue.class));
			
			
			icdBlockCmb.addValueChangeListener(new Property.ValueChangeListener() {
				
				@Override
				public void valueChange(ValueChangeEvent event) {
					SelectValue selectedIcdblock = (SelectValue)event.getProperty().getValue();
					if(selectedIcdblock != null && selectedIcdblock.getId() != null)
						fireViewEvent(IcdSubLimitMappingPresenter.GET_ICD_CODE_FOR_ICD_BLOCK,selectedIcdblock.getId());
				}
			});
			
			FormLayout sublimitFrm = new FormLayout(subLimitNameTxt);
			sublimitFrm.setMargin(false);
			FormLayout icdChapForm = new FormLayout(icdChapterCmb);
			icdChapForm.setMargin(false);
			FormLayout icdBlockForm = new FormLayout(icdBlockCmb);
			icdBlockForm.setMargin(false);
			
			HorizontalLayout icdHLayout = new HorizontalLayout(icdChapForm,icdBlockForm);
			resultVLayout = new VerticalLayout(sublimitFrm,icdHLayout);
			resultVLayout.setSpacing(true);
			resultVLayout.setMargin(false);
						
			searchResultVLayout.addComponents(resultVLayout);
		}		
		
	}
	
	private ValueChangeListener getSelectAllValueChangeListener() {
		return new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean selected = (Boolean)event.getProperty().getValue();
				if(selected != null){				
					List<IcdSublimitMappingDto> icdSublimitMapListDto =  searchResultTable.getTableList();
					if(icdSublimitMapListDto != null && ! icdSublimitMapListDto.isEmpty()){
						for (IcdSublimitMappingDto searchBulkConvertReimbTableDto : icdSublimitMapListDto) {
							searchBulkConvertReimbTableDto.setSelected(selected);
						}
						searchResultVLayout.removeComponent(searchResultTable.getTable());
						searchResultVLayout.removeComponent(submitBtnLayout);
						searchResultTable.init("", false, false);
						repaintSearchResultTable(icdSublimitMapListDto);
						searchResultTable.autoGenerateSelectColumn();
						searchResultVLayout.addComponent(searchResultTable.getTable());
						searchResultVLayout.addComponent(submitBtnLayout);
						searchResultVLayout.setComponentAlignment(submitBtnLayout, Alignment.BOTTOM_CENTER);
					}	
				}	
			}
		};
	}
	
	public void repaintSearchResultTable(List<IcdSublimitMappingDto> tableRows){
		searchResultTable.removeRow();
		searchResultTable.setTableList(tableRows);
		
	}
	public void showSublimitName(SublimitFunObject sublimitObj){
		currentSublimitObj = sublimitObj;
		subLimitFindTxt.setValue(currentSublimitObj != null && currentSublimitObj.getName() != null && !currentSublimitObj.getName().isEmpty() ? currentSublimitObj.getName() : "");
		btnSearch.setEnabled(true);
	}
	
	public void setIcdBlockDropDown(BeanItemContainer<SelectValue> icdChapterContainer) {
		
		icdBlockCmb.setContainerDataSource(icdChapterContainer);
		icdBlockCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdBlockCmb.setItemCaptionPropertyId("value");
	}
	
	public void showResultTable(List<IcdSublimitMappingDto> icdCodeSelectList){
		
		if(icdCodeSelectList != null && !icdCodeSelectList.isEmpty()){
//			if(searchResultTable != null){

				searchResultVLayout.addComponent(totalRecordSelectlayout);				

				searchResultTable.setTableList(icdCodeSelectList);
				searchResultVLayout.addComponent(totalRecordSelectlayout);
				searchResultVLayout.addComponent(searchResultTable.getTable());
				searchResultVLayout.addComponent(submitBtnLayout);
				searchResultVLayout.setComponentAlignment(submitBtnLayout, Alignment.BOTTOM_CENTER);
//			}	
		}
		else{
			showSuccessLayout("No Records found.");
		}
	}
	
	public void showResultTable(BeanItemContainer<SelectValue> icdChapterContainer, BeanItemContainer<SelectValue> icdBlockContainer, List<IcdSublimitMappingDto> icdCodeSelectList, boolean selectAll){
		
	
		if(icdCodeSelectList != null && !icdCodeSelectList.isEmpty()){
			setContainerValues(icdChapterContainer,icdBlockContainer);
			searchResultVLayout.removeComponent(selectAllFrmLayout);
			searchResultVLayout.removeComponent(totalRecordSelectlayout);
			searchResultVLayout.removeComponent(searchResultTable.getTable());
			searchResultVLayout.removeComponent(submitBtnLayout);

			searchResultTable.setTableList(icdCodeSelectList);
			
			FormLayout totalRecFrmLayout = new FormLayout();
			totalRecLbl = new Label();
			totalRecFrmLayout.addComponent(totalRecLbl);
			totalRecFrmLayout.setMargin(false);
			totalRecordSelectlayout.addComponent(totalRecFrmLayout);
			totalRecordSelectlayout.setComponentAlignment(totalRecFrmLayout, Alignment.MIDDLE_LEFT);
			selectAllFrmLayout = new FormLayout();
			selectAllchk.setValue(selectAll);
			selectAllFrmLayout.addComponent(selectAllchk);
			selectAllFrmLayout.setWidth("60%");
			selectAllFrmLayout.setMargin(false);
			totalRecordSelectlayout.addComponent(selectAllFrmLayout);
			totalRecordSelectlayout.setComponentAlignment(selectAllFrmLayout, Alignment.MIDDLE_RIGHT);
			totalRecordSelectlayout.setMargin(false);
			searchResultVLayout.addComponent(totalRecordSelectlayout);
			searchResultVLayout.setMargin(false);
			
			searchResultVLayout.addComponent(searchResultTable.getTable());
			searchResultVLayout.addComponent(submitBtnLayout);
			searchResultVLayout.setComponentAlignment(submitBtnLayout, Alignment.BOTTOM_CENTER);
		}
		else{
			showSuccessLayout("No Records found.");
		}
	}
	
	public void setContainerValues(BeanItemContainer<SelectValue> icdChapterContainer, BeanItemContainer<SelectValue> icdBlockContainer){
	
		setIcdChapter(icdChapterContainer);
		icdChapterCmb.setValue(icdChapterContainer.getItemIds().get(0));
		
		icdBlockCmb.setContainerDataSource(icdBlockContainer);
		icdBlockCmb.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		icdBlockCmb.setItemCaptionPropertyId("value");
		icdBlockCmb.setValue(icdBlockContainer.getItemIds().get(0));
	}
	
	public void showSuccessLayout(String msg)
	{

		Label successLabel = new Label("<b style = 'color: black;'>"+msg+"</b>", ContentMode.HTML);			
		Button homeButton = new Button("Icd Sublimit Mapping Home");
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
		dialog.setStyleName(Reindeer.WINDOW_BLACK);
		
		dialog.show(getUI().getCurrent(), null, true);
		
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				dialog.close();
				resetSearchForm();
				clearSearchResultFrmLayout();
				fireViewEvent(MenuItemBean.ICD_SUBLIMIT_MAPPING, null);					
			}
		});
	
	}
		
	public void showUnchecked(boolean unchecked){
		
//		selectAllchk.removeValueChangeListener(getSelectAllValueChangeListener());
//		selectAllchk.setValue(unchecked);
//		selectAllchk.removeValueChangeListener(getSelectAllValueChangeListener());
//		//Vaadin8-setImmediate() selectAllchk.setImmediate(true);
	}
	
	public void resetSearchForm(){
		
		if(subLimitFindTxt != null){
			subLimitFindTxt.setValue(null);
			currentSublimitObj = null;
		}
		
		if(btnSearch != null){
			btnSearch.setEnabled(true);
		}
		
		if(searchResultTable != null){
			searchResultTable.removeRow();
		}		
		
	}
	
	private void clearSearchResultFrmLayout(){
		resultVLayout.removeAllComponents();
		searchResultVLayout.removeComponent(totalRecordSelectlayout);
		searchResultVLayout.removeComponent(searchResultTable.getTable());
		searchResultVLayout.removeComponent(submitBtnLayout);
	}
	
	public IcdSubLimitMappingTable getSearchResultTable(){
	
		return searchResultTable;
	}
		
	private boolean validateSearchBean(){
		try{
			boolean hasError = false;
			StringBuffer errMsg = new StringBuffer("");
			if(!binder.isValid()){
				
				if(subLimitNameTxt.getValue() == null){
					hasError = true;
					errMsg.append("Pleae Select Sublimit Name<br>");
				}

				
				
				if(searchResultTable != null && searchResultTable.getTableList() != null &&
						!searchResultTable.getTableList().isEmpty() &&
						!searchResultTable.validateSelection()){
					errMsg.append("Please Select atleast one value in the ICD Code Table<br>");
				}
				
				if(icdChapterCmb.getValue() == null){
					hasError = true;
					errMsg.append("Pleae Select ICD Chapter<br>");
				}
				
				if(icdBlockCmb.getValue() == null){
					hasError = true;
					errMsg.append("Pleae Select ICD Block<br>");
				}
				
				if(hasError && errMsg.length()>0){
					showErrorPopup(errMsg.toString());
					return false;
				}
				return false;
			}else{
				
				binder.commit();

				if(icdChapterCmb != null && icdChapterCmb.getValue() == null){
					showErrorPopup("Pleae Select ICD Chapter<br>");
					return false;
				}

				if(icdBlockCmb != null && icdBlockCmb.getValue() == null){
					showErrorPopup("Pleae Select ICD Block<br>");
					return false;
				}
				this.bean = binder.getItemDataSource().getBean();
				this.bean.setIcdCodeSelectList(searchResultTable.getTableList());
				this.bean.setUsername(UI.getCurrent().getSession().getAttribute(BPMClientContext.USERID).toString());
				/*if(searchResultTable != null && searchResultTable.getTableList() != null &&
						!searchResultTable.getTableList().isEmpty() &&
						!searchResultTable.validateSelection()){
					showErrorPopup("Please Select atleast one value in the ICD Code Table");
					return false;
				}*/
				
			}	
			return true;				
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
	return false;
	}
	
	public void showErrorPopup(String eMsg){
		
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
		
}
