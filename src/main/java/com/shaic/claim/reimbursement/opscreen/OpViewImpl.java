package com.shaic.claim.reimbursement.opscreen;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.claim.outpatient.processOPpages.ProcessOPBillDetailsPage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.v7.data.util.BeanItemContainer;
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

public class OpViewImpl extends AbstractMVPView implements OpView{
	
	@Inject
	private OpForm opForm;

	@Inject
	private OpTable searchResultTable;
	
	@Inject 
	private ProcessOPBillDetailsPage processOPBillDetailsPage;
	
	private VerticalSplitPanel mainPanel;
	
	//private CheckBox chkBox;
	
	private Button btnCreateLot;
	
	private HorizontalLayout btnLayout;
	
	private Button btnCancel;
		
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		opForm.init();
		searchResultTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(opForm);
		VerticalLayout vLayout = new VerticalLayout();
		btnLayout = buildButtonsLayout();
		vLayout.addComponents(searchResultTable,btnLayout);
		vLayout.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
		vLayout.setSpacing(true);
		mainPanel.setSecondComponent(vLayout);
		mainPanel.setSplitPosition(27);
		setHeight("650px");
		mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		opForm.addSearchListener(this);		
		resetView();
	}
	
	@Override
	public void resetView() {
	
		opForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		
	/*//	String err=opForm.validate();
		if(err == null)
		{		*/
		OpFormDTO searchDTO = opForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(OpPresenter.OP_SCREEN_VIEW, searchDTO,userName,passWord);
		/*}
		else
		{
			showErrorMessage(err);
		}*/
		
	}
	
	/*private void showErrorMessage(String eMsg) {
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
	}*/

	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
	

		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof VerticalLayout)
			{
				VerticalLayout vLayout = (VerticalLayout)comp;
				Iterator<Component> vCompIter = vLayout.getComponentIterator();
				while(vCompIter.hasNext())
				{
					Component vComp = (Component)vCompIter.next();
					if(vComp instanceof OpTable)
					{
						((OpTable) vComp).removeRow();
					}
				}
			
			
			}
		}
	
		
	}
	
	private HorizontalLayout buildButtonsLayout()
	{
		
		btnCreateLot = new Button();
		btnCreateLot.setCaption("Create Lot");
		//Vaadin8-setImmediate() btnCreateLot.setImmediate(true);
		btnCreateLot.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnCreateLot.setWidth("-1px");
		btnCreateLot.setHeight("-10px");
		//btnSendToChecker.setDisableOnClick(true);

		//Vaadin8-setImmediate() btnCreateLot.setImmediate(true);
		
		
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		btnCancel.addStyleName(ValoTheme.BUTTON_DANGER);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-10px");
		//Vaadin8-setImmediate() btnCancel.setImmediate(true);
		
		/*FormLayout btnFormLayout = new FormLayout(btnSendToChecker);
		FormLayout btnFormLayout1 = new FormLayout(btnCancel);
		btnLayout.addComponent(btnFormLayout);
		btnLayout.addComponent(btnCancel);*/
		btnLayout = new HorizontalLayout();
		btnLayout.setSpacing(true);
		btnLayout.addComponent(btnCreateLot);
		btnLayout.addComponent(btnCancel);
		return btnLayout; 
	}

	@Override
	public void list(Page<OpTableDTO> tableRows) {
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows.getPageItems());
			searchResultTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("OP Home");
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
					fireViewEvent(MenuItemBean.OP_SCREEN, null);
					
				}
			});
		}
	}
	
	
	/*private void addListener()
	{
		
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{
					boolean value = (Boolean) event.getProperty().getValue();
					if(value)
					{
						//btnGenerateExcel.setEnabled(true);
					}
					else
					{
						//btnGenerateExcel.setEnabled(false);
					}
					searchResultTable.setValueForCheckBox(value);
				}
			}
		});
	}*/

	
	@Override
	public void init(BeanItemContainer<SelectValue> type) {
		
		//opForm.setDropDownValues(type);
		
		
	}

	@Override
	public void buildQueryLayout() {
		processOPBillDetailsPage.buildQueryLayout();
	}

	@Override
	public void buildApproveLayout() {
		processOPBillDetailsPage.buildApproveLayout();
	}

	@Override
	public void buildRejectLayout() {
		processOPBillDetailsPage.buildRejectLayout();
	}

}
