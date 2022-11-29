package com.shaic.claim.policy.search.ui;

import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.arch.utils.Props;
import com.shaic.claim.intimation.ViewPreviousIntimation;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.v7.ui.themes.Reindeer;
@Theme(Props.THEME_NAME)
public class NewSearchPolicyViewImpl  extends AbstractMVPView implements
NewSearchPolicyView, Searchable {
	
	
	private static final long serialVersionUID = 2645510427614044958L;
	

	@Inject
	private SearchNewPolicyUIForm searchForm;

	@Inject
	private NewSearchPolicyTable searchResultTable;

	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();

	
	@PostConstruct
	protected void initView() {
		try
		{
			addStyleName("view");
			setSizeFull();
			searchForm.init();
			searchResultTable.init("", false, true);
			searchResultTable.setHeight("100.0%");
			searchResultTable.setWidth("100.0%");
			mainPanel.setFirstComponent(searchForm);
			mainPanel.setSecondComponent(searchResultTable);
			mainPanel.setSplitPosition(30);
			mainPanel.setHeight("100.0%");
			mainPanel.setWidth("100.0%");
			setHeight("100.0%");
			setHeight("600px");
			setCompositionRoot(mainPanel);
			searchResultTable.addSearchListener(this);
			searchForm.addSearchListener(this);
			
			resetView();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	 public void init(BeanItemContainer<SelectValue> searchByContainer ,BeanItemContainer<SelectValue> productNameContainer, 
			  BeanItemContainer<SelectValue> productTypeContainer,BeanItemContainer<SelectValue> policyCodeOrNameContainer) 
	{
		searchForm.setSearchByDropDown(searchByContainer);
		searchForm.setProductNameDropDown(productNameContainer);
		searchForm.setProductTypeDropDown(productTypeContainer);
		searchForm.setPolicyIssueOfficeCodeDropDown(policyCodeOrNameContainer);
		// TODO Auto-generated method stub	
	}
	
	/*@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void list(Page<NewSearchPolicyTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			/*searchResultTable.init("", false);
			searchResultTable.setHeight("100.0%");
			searchResultTable.setWidth("100.0%");
			mainPanel.setSecondComponent(searchResultTable);*/
			searchResultTable.setTableList(tableRows.getPageItems());
		}
		else
		{
			ConfirmDialog dialog = ConfirmDialog.show(getUI(), "No Records found ", "Click ok to go to menu",
			        "Cancel", "Ok", new ConfirmDialog.Listener() 
			{
			            /**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						public void onClose(ConfirmDialog dialog) {
			                if (!dialog.isConfirmed()) {
			                    // Confirmed to continue
			                	fireViewEvent(MenuItemBean.CREATE_NEW_INTIMATION, null);
			                } else {
			                    // User did not confirm
			                }
			            }
			        });
			
			dialog.setStyleName(Reindeer.WINDOW_BLACK);
		}
	}

	@Override
	public void doSearch() {
		//// TODO Auto-generated method stub
		NewSearchPolicyFormDTO searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(NewSearchPolicyPresenter.SEARCH_BUTTON_CLICK,
				searchDTO,userName,passWord);		
	}

	@Override
	public void resetView() {
		System.out.println("---tinside the reset view");
		searchForm.refresh();
	}

	@Override
	public void resetSearchResultTableValues() {
		
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof NewSearchPolicyTable)
			{
				((NewSearchPolicyTable) comp).removeRow();
			}
		}
	}

	@Override
	public void setPolicyValueMap(LinkedHashMap<String, String> policyValues) {
		searchResultTable.intializePolicyValueMap(policyValues);
	}

	@Override
	public void showProductBenefits(ViewProductBenefits a_viewProductBenefits) {
		UI.getCurrent().addWindow(a_viewProductBenefits);
		
	}

	@Override
	public void showPreviousIntimation(ViewPreviousIntimation view) {
		UI.getCurrent().addWindow(view);
		
	}
}
