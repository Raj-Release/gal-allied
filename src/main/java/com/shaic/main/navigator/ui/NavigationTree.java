package com.shaic.main.navigator.ui;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.MVPView;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.addon.cdiproperties.Localizer.TextBundleUpdated;
import org.vaadin.addon.cdiproperties.TextBundle;
import org.vaadin.addon.cdiproperties.annotation.TreeProperties;

import com.shaic.arch.SHAUtils;
import com.shaic.main.navigator.domain.MenuItem;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.v7.ui.Tree;
import com.vaadin.v7.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class NavigationTree extends ViewComponent {

	@Inject
	@TreeProperties(nullSelectionAllowed = false, immediate = true, selectable = true)
	private Tree tree;

	@Inject
	private TextBundle tb;

	private final Property.ValueChangeListener listener = new Property.ValueChangeListener() {
		@Override
		public void valueChange(
				final com.vaadin.v7.data.Property.ValueChangeEvent event) {
			
			MenuItem menuItem = (MenuItem) event.getProperty().getValue();
			if (menuItem != null && menuItem.isChild()) {
				
				/*Integer taskNumber = (Integer)getSession().getAttribute(SHAConstants.TOKEN_ID);
				String userName=(String)getSession().getAttribute(BPMClientContext.USERID);
				String passWord=(String)getSession().getAttribute(BPMClientContext.PASSWORD);
				Long workFlowKey = (Long)getSession().getAttribute(SHAConstants.WK_KEY);
				if(taskNumber != null){
				//	 BPMClientContext.setActiveOrDeactiveClaim(userName,passWord, taskNumber, SHAConstants.SYS_RELEASE);
					 getSession().setAttribute(SHAConstants.TOKEN_ID, null);
				}
				if(workFlowKey != null){
					DBCalculationService dbCalService = new DBCalculationService();
					dbCalService.callUnlockProcedure(workFlowKey);
					 getSession().setAttribute(SHAConstants.WK_KEY, null);
				}*/
				VaadinRequest currentRequest = VaadinService.getCurrentRequest();
				SHAUtils.clearSessionObject(currentRequest);
				fireViewEvent((String) menuItem.getCode(), null);
	
			}
		}
	};

	@PostConstruct
	public void init() {
		VerticalLayout layout = new VerticalLayout();
		layout.setCaption("IMS");
		tree.addStyleName("g-tree");
		layout.addComponent(tree);
		setCompositionRoot(layout);
		localize(null);
	}
	

	public void setSelectedView(final Class<? extends MVPView> viewClass) {
        tree.removeValueChangeListener(listener);
        tree.addValueChangeListener(listener);
    }
	
	public void addTreeMenuItem(MenuItem menu, MenuItem parent)
	{
		if (!menu.isAccessFlag())
		{
			return;
		}
		
		Item treeItem = tree.addItem(menu);
		
		if (parent != null)
		{
			tree.setParent(menu, parent);
		}
		if (menu.hasChild())
		{
			tree.setChildrenAllowed(treeItem, true);
			for (MenuItem item : menu.getChildMenus()) 
			{
				addTreeMenuItem(item, menu);
			}
		}
		else
		{
			tree.setChildrenAllowed(menu, false);
		}
	}
	
	public void addTreeMenuItem(MenuItem menu)
	{
		Item treeItem = tree.addItem(menu);
		tree.setChildrenAllowed(menu, false);
		tree.setChildrenAllowed(treeItem, true);		
	}

	public void setValue(final String value) {
		tree.setValue(value);
	}

	public void setupMenu(MenuItem rootMenu)
	{
		addTreeMenuItem(rootMenu, null);
		tree.expandItemsRecursively(rootMenu);
		tree.addValueChangeListener(listener);
	}
	protected void localize(@Observes(notifyObserver = Reception.IF_EXISTS) @TextBundleUpdated final ParameterDTO parameterDto) {
        tree.setItemCaption(MenuItemBean.SEARCH_POLICY,tb.getText("policy-search"));
        tree.setItemCaption(MenuItemBean.SEARCH_INTIMATION, tb.getText("intimation-search"));
    }
	
	public void callCreateIntimation(){
		fireViewEvent(MenuItemBean.SEARCH_POLICY, null);
	}
	
	public void callCreateIntimationWithSearchParameter(String policyNumber, String healthCardNumber){
		fireViewEvent(MenuItemBean.SEARCH_POLICY_PARAMETER, policyNumber,healthCardNumber);
	}
}
