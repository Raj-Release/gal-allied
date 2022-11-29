package com.shaic.claim.reports.paymentprocess;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
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

public class PaymentProcessViewImpl extends AbstractMVPView implements PaymentProcessView{
	
	@Inject
	private PaymentProcessForm  paymentProcessForm;
	
	@Inject
	private PaymentProcessTable paymentProcessTable;
	
	
	private VerticalSplitPanel mainPanel;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		setSizeFull();
		paymentProcessForm.init();
		paymentProcessTable.init("", false, true);
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(paymentProcessForm);
		mainPanel.setSecondComponent(paymentProcessTable);
		mainPanel.setSplitPosition(42);
		setHeight("650px");
		mainPanel.setHeight("625px");
		setCompositionRoot(mainPanel);
		paymentProcessTable.addSearchListener(this);
		paymentProcessForm.addSearchListener(this);
		resetView();
	}
	
	@Override
	public void resetView() {
		paymentProcessForm.refresh(); 
		
	}

	@Override
	public void doSearch() {
		PaymentProcessFormDTO searchDTO = paymentProcessForm.getSearchDTO();
		Pageable pageable = paymentProcessTable.getPageable();
		searchDTO.setPageable(pageable);
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		fireViewEvent(PaymentProcessPresenter.SEARCH_BUTTON_CLICK, searchDTO,userName,passWord);
		
	}

	@Override
	public void resetSearchResultTableValues() {
		paymentProcessTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof PaymentProcessTable)
			{
				((PaymentProcessTable) comp).removeRow();
			}
		}
	
		
	}

	@Override
	public void list(Page<PaymentProcessTableDTO> tableRows) {
		
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			paymentProcessTable.setTableList(tableRows);
//			paymentProcessTable.tablesize();
			paymentProcessTable.setHasNextPage(tableRows.isHasNext());
		}
		else
		{
			
			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("Payment Process Cpu Home");
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
					fireViewEvent(MenuItemBean.PAYMENT_PROCESS, null);
					
				}
			});
		}
		
		paymentProcessForm.enableButtons();
	}

	@Override
	public void init(BeanItemContainer<SelectValue> cpu,BeanItemContainer<SelectValue> year,
			BeanItemContainer<SelectValue> cpuLotNo, BeanItemContainer<SelectValue> status,BeanItemContainer<SelectValue> branch) 
	{
		paymentProcessForm.setDropDownValues(cpu,year,cpuLotNo,status,branch);
		
	}

	
	

}
