package com.shaic.arch;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.table.Searchable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class SearchComponent<T> extends ViewComponent implements ClickListener{
	
	public static final String GET_TASK_CAPTION = "Get Task";
	public static final String SEARCH_TASK_CAPTION = "Search";
	public static final String QUICK_SEARCH_LOT_TASK_CAPTION = "Add to Lot";
	public static final String QUICK_SEARCH_RESET_CAPTION = "Reset";
	public static final String QUICK_SEARCH_BATCH_TASK_CAPTION = "Add to Batch";
	public static final String GENERATE_TASK_CAPTION = "Generate";
	public static final String SUBMIT = "Submit";
	public static final String GENERATE_TASK_REPORT = "Generate Report";
	
	
	protected Searchable searchable;
	protected BeanFieldGroup<T> binder;
	protected Button btnSearch;
	protected Button btnReset;
	protected Button btnQuickSearchLot;
	protected Button btnQuickSearchBatch;
	protected Button btnQuickReset;
	protected Button btnGenerate;
	protected Button btnGetTask;
	protected Button btnGenerateReport;
	
	public SearchComponent() {
		btnSearch = new Button(SEARCH_TASK_CAPTION);
		btnSearch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnReset = new Button("Reset");
		btnReset.addStyleName(ValoTheme.BUTTON_DANGER);
		btnQuickSearchLot = new Button(QUICK_SEARCH_LOT_TASK_CAPTION);
		btnQuickSearchLot.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnQuickSearchBatch = new Button(QUICK_SEARCH_BATCH_TASK_CAPTION);
		btnQuickSearchBatch.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnQuickReset = new Button(QUICK_SEARCH_RESET_CAPTION);
		btnQuickReset.addStyleName(ValoTheme.BUTTON_DANGER);

		btnGenerate = new Button(GENERATE_TASK_CAPTION);
		
		btnGetTask = new Button(GET_TASK_CAPTION);
		btnGetTask.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
		btnGenerateReport= new Button(GENERATE_TASK_REPORT);
		btnGenerateReport.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		
	}
	
	
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		
		if (event.getButton() == btnSearch)
        {
			btnSearch.setEnabled(false);
			searchable.doSearch();
			btnSearch.setEnabled(true);
        }
		else if (event.getButton() == btnReset)
		{
			SHAUtils.resetComponent(mainVerticalLayout);
			searchable.resetSearchResultTableValues();
			
		}
		else if(event.getButton() == btnQuickSearchLot)
		{
			btnQuickSearchLot.setEnabled(false);
			searchable.doSearch();
			btnQuickSearchLot.setEnabled(true);
		}
		else if(event.getButton() == btnQuickSearchBatch)
		{
			btnQuickSearchBatch.setEnabled(false);
			searchable.doSearch();
			btnQuickSearchBatch.setEnabled(true);
		}
		else if(event.getButton() == btnQuickReset)
		{
			SHAUtils.resetComponent(quickVerticallayout);
			searchable.resetSearchResultTableValues();
		}
		else if (event.getButton() == btnGenerate)
        {
			btnGenerate.setEnabled(false);
			searchable.doSearch();
			btnGenerate.setEnabled(true);
        }
		else if (event.getButton() == btnGetTask)
        {
			btnGetTask.setEnabled(false);
			searchable.doSearch();
			btnGetTask.setEnabled(true);
        }
		else if (event.getButton() == btnGenerateReport)
        {
			btnGenerateReport.setEnabled(false);
			searchable.doSearch();
			btnGenerateReport.setEnabled(true);
        }
	}
	
	protected void addListener(){
		btnSearch.removeClickListener(this);
		btnReset.removeClickListener(this);
		btnQuickSearchLot.removeClickListener(this);
		btnQuickSearchBatch.removeClickListener(this);
		btnQuickReset.removeClickListener(this);
		btnGenerate.removeClickListener(this);
		btnGetTask.removeClickListener(this);
		btnGenerateReport.removeClickListener(this);
		
		btnSearch.addClickListener(this);
    	btnReset.addClickListener(this);
    	btnQuickSearchLot.addClickListener(this);
    	btnQuickSearchBatch.addClickListener(this);
		btnQuickReset.addClickListener(this);
		btnGenerate.addClickListener(this);
		btnGetTask.addClickListener(this);
		btnGenerateReport.addClickListener(this);
	}
	
	
	public T getSearchDTO(){
		try {
			this.binder.commit();
			T bean = binder.getItemDataSource().getBean();
			return bean;
		} catch (CommitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected VerticalLayout mainVerticalLayout;
	
	protected VerticalLayout quickVerticallayout;

	public void addSearchListener(Searchable searable)
	{
		
		this.searchable = searable;
	}
	public void refresh()
	{
		System.out.println("---inside the refresh----");
		if(mainVerticalLayout != null) {
			SHAUtils.resetComponent(mainVerticalLayout);
			searchable.resetSearchResultTableValues();
		}
		if (quickVerticallayout != null){
			
			SHAUtils.resetComponent(quickVerticallayout);
			searchable.resetSearchResultTableValues();
		}
	}
	
	public void enableButtons()
	{
		btnReset.setEnabled(true);
		btnSearch.setEnabled(true);
		btnQuickSearchLot.setEnabled(true);
		btnQuickSearchBatch.setEnabled(true);
		btnQuickReset.setEnabled(true);
		btnGenerate.setEnabled(true);
		btnGetTask.setEnabled(true);
		btnGenerateReport.setEnabled(true);
		
		
	}
}