package com.shaic.paclaim.convertClaimToReimb;

import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.arch.view.LoaderPresenter;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimFormDto;
import com.shaic.claim.registration.convertClaim.search.SearchConvertClaimTableDto;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SearchConvertPAClaimViewImpl  extends AbstractMVPView implements SearchConvertPAClaimView, Searchable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private Instance<SearchConvertPAClaimPage> searchFormInstance;
	
	@Inject
	private SearchConvertPAClaimPage searchForm;
	
	@Inject
	private SearchConvertPAClaimTable searchResultTable;	
	
	private VerticalLayout mainLayout = new VerticalLayout();
	
	@PostConstruct
	public void init() {
		
		addStyleName("view");
		setSizeFull();
		searchForm = searchFormInstance.get();
		searchResultTable.init("", false, false);
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));


		mainLayout.addComponent(searchForm);
		mainLayout.addComponent(searchResultTable);

		mainLayout.setWidth("100.0%");
		

		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainLayout);

		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		
		resetView();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		
		searchForm.refresh(); 
	}
	
	@Override
	public void list(Page<SearchConvertClaimTableDto> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Convert Claim Home");
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
					fireViewEvent(LoaderPresenter.LOAD_URL,MenuItemBean.SEARCH_CONVERT_PA_CLAIM);
					
				}
			});
			
		}
		
	}

	@Override
	public void doSearch() {
		SearchConvertClaimFormDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		
		fireViewEvent(SearchConvertPAClaimPresenter.SEARCH_CONVERT_PA_CLAIM_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void init(Map<String,Object> defaultValues) {
		// TODO Auto-generated method stub
		searchForm.setDefaultValues(defaultValues);
		
	}
	@Override
	public void resetSearchResultTableValues() {
		
		searchResultTable.resetTable();
		
		Iterator<Component> componentIter = mainLayout.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof SearchConvertPAClaimTable)
			{
				((SearchConvertPAClaimTable) comp).removeRow();
			}
		}
	}

}
