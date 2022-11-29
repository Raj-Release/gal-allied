/**
 * 
 */
package com.shaic.claim;

/**
 * @author ntv.vijayar
 *
 */


import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class DMSDocumentViewDetailsPage extends ViewComponent {
	
	@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	private DMSDocumentDTO bean;
	
	private BeanFieldGroup<DMSDocumentDTO> binder;
	
	private String presenterString;
	
	private Panel rrcViewPanel;
	private Window popup;
	private Button btnSubmit;
	private TextField txtIntimationNo;
	
	private TextField txtClaimNo;
	
	public void init(DMSDocumentDTO bean,Window popup)
	{
		this.bean = bean;
		this.popup = popup;
	}
	
	public void initBinder() {
		this.binder = new BeanFieldGroup<DMSDocumentDTO>(DMSDocumentDTO.class);
		this.binder.setItemDataSource(this.bean);
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		//this.binder.setItemDataSource(new DocumentDetailsDTO());
	}
	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
public Component getContent() {
		
		initBinder();
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtIntimationNo.setReadOnly(true);
		txtIntimationNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtClaimNo = binder.buildAndBind("Claim No", "claimNo", TextField.class);
		txtClaimNo.setReadOnly(true);
		txtClaimNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		//this.popup = popup;
		
		dmsDocumentViewListenerTable = dmsDocumentViewListenerTableObj.get();
		dmsDocumentViewListenerTable.init();
		//quantumReductionDetailsListener.initPresenter(SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY);
		//dmsDocumentViewListenerTable.init(this.bean.get);	
		//quantumReductionDetailsListener.setReadOnly(true);
		//quantumReductionDetailsListener.setEnabled(false);
		
	
		//tablePanel.setWidth("1000px");
		Panel tablePanel = new Panel();
		
		tablePanel.setSizeFull();
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo);
		formLayout1.setSpacing(true);
		FormLayout formLayout2 = new FormLayout(txtClaimNo);
		formLayout2.setSpacing(true);
		
		
		HorizontalLayout detailsLayout2 = new HorizontalLayout(formLayout1,formLayout2);
		detailsLayout2.setMargin(true);
		
		HorizontalLayout detailsLayout1 = new HorizontalLayout(dmsDocumentViewListenerTable);
		detailsLayout1.setMargin(true);
		
		VerticalLayout vLayout = new VerticalLayout(detailsLayout2, detailsLayout1);
	
		
		//tablePanel.setContent(detailsLayout1);
	//	tablePanel.setContent(vLayout);
		tablePanel.setContent(new VerticalLayout(new Label("<b>Document Details Table<b>",ContentMode.HTML),dmsDocumentViewListenerTable.getTable()));
		tablePanel.setWidth("100%");
		
		VerticalLayout detailsLayout = new VerticalLayout(detailsLayout2, tablePanel, buildButtonLayout());
		detailsLayout.setSpacing(true);
		detailsLayout.setMargin(true);
		Panel detailsPanel = new Panel();
		detailsPanel.setContent(detailsLayout);
			

		
		rrcViewPanel = new Panel();
		//rrcViewPanel.setSizeFull();
		rrcViewPanel.setHeight("700px");
		rrcViewPanel.setContent(detailsLayout);
		
		
		setTableValues();
		addListener();
		
		setCompositionRoot(rrcViewPanel);
		return rrcViewPanel;
		
	}

	private HorizontalLayout buildButtonLayout()
	{
		btnSubmit = new Button("OK");
		btnSubmit = new Button();
		btnSubmit.setCaption("OK");
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		btnSubmit.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnSubmit.setWidth("-1px");
		btnSubmit.setHeight("-10px");
		btnSubmit.setDisableOnClick(true);
		//Vaadin8-setImmediate() btnSubmit.setImmediate(true);
		HorizontalLayout hBtnLayout = new HorizontalLayout(btnSubmit);
		hBtnLayout.setComponentAlignment(btnSubmit, Alignment.BOTTOM_CENTER);
		hBtnLayout.setSpacing(true);
		hBtnLayout.setMargin(true);
		hBtnLayout.setWidth("100%");
		return hBtnLayout;
	}


	public void addListener() {
		
		btnSubmit.addClickListener(new Button.ClickListener() {
	
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});
	
	}

	private void setTableValues()
	{
		
		if(null != dmsDocumentViewListenerTable)
		{
			List<DMSDocumentDetailsDTO> dmsDocumentDetailsDTOList = this.bean.getDmsDocumentDetailsDTOList();
			if(null != dmsDocumentDetailsDTOList)
			{
				for (DMSDocumentDetailsDTO dmsDocumentDetailsDTO : dmsDocumentDetailsDTOList) {
					dmsDocumentViewListenerTable.addBeanToList(dmsDocumentDetailsDTO);
				}
				
			}
		}
	}
}

