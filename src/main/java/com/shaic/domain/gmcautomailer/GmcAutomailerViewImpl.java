package com.shaic.domain.gmcautomailer;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.alert.util.ButtonOption;
import com.alert.util.MessageBox;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.arch.table.Searchable;
import com.shaic.cmn.login.ImsUser;
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
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.themes.ValoTheme;

public class GmcAutomailerViewImpl extends AbstractMVPView implements GmcAutomailerView, Searchable {
	
	private static final long serialVersionUID = 1934939436987293748L;

	@Inject
	private GmcAutomailerForm searchForm;
	
	@Inject
	private GmcAutomailerTable searchResultTable;	
	
	private VerticalSplitPanel mainPanel = new VerticalSplitPanel();
	
	private Button btnSubmit;
	
	private HorizontalLayout btnLayout;
	
	private GmcAutomailerFormDTO searchDTO = null;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false, false);
		searchResultTable.addStyleName((ValoTheme.TABLE_COMPACT));
		searchResultTable.setColumnHeader();
		mainPanel.setFirstComponent(searchForm);
		VerticalLayout vLayout = new VerticalLayout();
		btnLayout = buildButtonsLayout();
		vLayout.addComponents(searchResultTable,btnLayout);		
		vLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		mainPanel.setSecondComponent(vLayout);
		mainPanel.setSplitPosition(30);
		mainPanel.setWidth("100.0%");
		setHeight("100.0%");
		setHeight("600px");
		setCompositionRoot(mainPanel);

		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		addListener();
		resetView();
		
	}
	
	@Override
	public void resetView() {
		System.out.println("---inside the reset view");
		searchForm.refresh(); 
	}

	@Override
	public void list(GmcAutomailerTableDTO tableRows) {
		
		if(null != tableRows)
		{	
			searchResultTable.addBeanToList(tableRows);
			//searchResultTable.setPage(tableRows);
			
			/*for (GmcAutomailerTableDTO tableDto : tableRows) {
				if(tableDto.getColorCode() != null && !tableDto.getColorCode().isEmpty()){
					searchResultTable.setRowColor(tableDto);
				}
			}*/
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("GMC Automailer Home");
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
					fireViewEvent(MenuItemBean.GMC_AUTOMAILER, null);
					
				}
			});
		}
		
	}

	@Override
	public void doSearch() {
		searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		ImsUser imsUser = (ImsUser)getUI().getSession().getAttribute(BPMClientContext.USER_OBJECT);
		
		searchDTO.setImsUser(imsUser);
		if (null != searchDTO.getPolicyNo() && !searchDTO.getPolicyNo().isEmpty() 
				&& null != searchDTO.getBranchCode() && !searchDTO.getBranchCode().isEmpty()) {
			getErrorMessage("Choose only one field.");
		}else {
			fireViewEvent(GmcAutomailerPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName);
		}
	}
	
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof GmcAutomailerTable)
			{
				((GmcAutomailerTable) comp).removeRow();
			}
		}
	}
	
	public HorizontalLayout buildButtonsLayout()
	{
		btnSubmit = new Button();
		btnSubmit.setCaption("Submit");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		
		btnLayout = new HorizontalLayout();
		btnLayout.addComponents(btnSubmit);
		btnLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_LEFT);
		btnLayout.setSpacing(true);
		
		return btnLayout; 
	}
	private void addListener()
	{
		btnSubmit.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

			String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
			if(searchResultTable.getTableList() != null && !searchResultTable.getTableList().isEmpty()) {
			fireViewEvent(GmcAutomailerPresenter.SUBMIT_GMC_AUTOMAILER, searchResultTable.getTableList(),userName); }
			else{
				getErrorMessage("Please provide any Input");
			}
			//showInformation("Email Added Successfully");
			resetView();
			}
		});
	}
	
	public void getErrorMessage(String eMsg){
		
		Label label = new Label(eMsg, ContentMode.HTML);
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
	public void showInformation(String eMsg) {
		MessageBox.create()
		.withCaptionCust("Information").withHtmlMessage(eMsg.toString())
	    .withOkButton(ButtonOption.caption("OK")).open();
	}
}
