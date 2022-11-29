/**
 * 
 */
package com.shaic.claim.rod.wizard.tables;

import java.util.List;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.DMSDocumentDTO;
import com.shaic.claim.DMSDocumentDetailsDTO;
import com.shaic.claim.DMSDocumentViewListenerTable;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author yosuva.a
 *
 */
@CDIUI
public class DMSDocumentViewUI extends UI {

	/**
	 * 
	 */
	
	
	//private DMSDocumentViewDetailsPage dmsDocumentDetailsViewPage;
	
	private static final long serialVersionUID = 1L;
	private DMSDocumentDTO bean;
	//private Window popup = new Window();
	/*@Inject
	private Instance<DMSDocumentViewListenerTable> dmsDocumentViewListenerTableObj;*/
	private DMSDocumentViewListenerTable dmsDocumentViewListenerTable;
	
	
	
	private BeanFieldGroup<DMSDocumentDTO> binder;
	
	private String presenterString;
	
	private Panel rrcViewPanel;
	
	private Button btnSubmit;
	private TextField txtIntimationNo;
	
	private TextField txtClaimNo;

	@Override
	protected void init(VaadinRequest request) {
		
		//this.bean = (DMSDocumentDTO) request.getAttribute(SHAConstants.DMS_DOCUMENT_DTO);
		this.bean = (DMSDocumentDTO) request.getWrappedSession().getAttribute(SHAConstants.DMS_DOCUMENT_DTO);
		//this.dmsDocumentDetailsViewPage = (DMSDocumentViewDetailsPage) request.getWrappedSession().getAttribute(SHAConstants.DMS_DOCUMENT_VIEW_PAGE);
		this.dmsDocumentViewListenerTable = (DMSDocumentViewListenerTable) request.getWrappedSession().getAttribute(SHAConstants.DMS_DOC_TABLE);
		displayClaimDocs();
		// TODO Auto-generated method stub
		//getContent();
	}

	
	public void init(DMSDocumentDTO bean,Window popup)
	{
		this.bean = bean;
		//this.popup = popup;
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
	
public Component buildDMSLayout() {
		
		initBinder();
		txtIntimationNo = binder.buildAndBind("Intimation No", "intimationNo", TextField.class);
		txtIntimationNo.setReadOnly(true);
		txtIntimationNo.setWidth("110%");
		txtIntimationNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		txtClaimNo = binder.buildAndBind("Claim No", "claimNo", TextField.class);
		txtClaimNo.setReadOnly(true);
		txtClaimNo.setWidth("110%");
		txtClaimNo.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
		//this.popup = popup;
		
		
		dmsDocumentViewListenerTable.init();
		//quantumReductionDetailsListener.initPresenter(SHAConstants.VIEW_CLAIM_WISE_RRC_HISTORY);
		//dmsDocumentViewListenerTable.init(this.bean.get);	
		//quantumReductionDetailsListener.setReadOnly(true);
		//quantumReductionDetailsListener.setEnabled(false);
		
	
		//tablePanel.setWidth("1000px");
		Panel tablePanel = new Panel();
		
		tablePanel.setSizeFull();
		
		FormLayout formLayout1 = new FormLayout(txtIntimationNo);
//		formLayout1.setSpacing(true);
		FormLayout formLayout2 = new FormLayout(txtClaimNo);
//		formLayout2.setSpacing(true);
		
		
		HorizontalLayout detailsLayout2 = new HorizontalLayout(formLayout1,formLayout2);
		detailsLayout2.setMargin(true);
		detailsLayout2.setWidth("100%");
		
		HorizontalLayout detailsLayout1 = new HorizontalLayout(dmsDocumentViewListenerTable);
		detailsLayout1.setMargin(true);
		
		VerticalLayout vLayout = new VerticalLayout(detailsLayout2, detailsLayout1);
	
		
		//tablePanel.setContent(detailsLayout1);
	//	tablePanel.setContent(vLayout);
		tablePanel.setContent(dmsDocumentViewListenerTable);
		tablePanel.setWidth("100%");
		
		VerticalLayout detailsLayout = new VerticalLayout(detailsLayout2, tablePanel /*,buildButtonLayout()*/);
		detailsLayout.setSpacing(true);
		detailsLayout.setMargin(true);
		/*Panel detailsPanel = new Panel();
		detailsPanel.setContent(detailsLayout);*/
			

		
		rrcViewPanel = new Panel();
		//rrcViewPanel.setSizeFull();
		/*rrcViewPanel.setHeight("700px");
		rrcViewPanel.setContent(detailsLayout);*/
		
		
		setTableValues();
		addListener();
		setContent(detailsLayout);
		
		//setContent(rrcViewPanel);
		//setCompositionRoot(rrcViewPanel);
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
		
		/*btnSubmit.addClickListener(new Button.ClickListener() {
	
			private static final long serialVersionUID = 1L;
	
			@Override
			public void buttonClick(ClickEvent event) {
					//binder.commit();
						
						//fireViewEvent(MenuItemBean.SEARCH_RRC_REQUEST,null);
						popup.close();
					
			}
			
		});*/
	
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

	
	
	
	public void displayClaimDocs()
	{
		
		/*dmsDocumentDetailsViewPage.init(bean, popup);
		dmsDocumentDetailsViewPage.getContent();*/
		
		//setContent(dmsDocumentDetailsViewPage);
		Component comp = buildDMSLayout();
		//setContent(comp);
		/*popup = new com.vaadin.ui.Window();

		dmsDocumentDetailsViewPage.init(bean, popup);
		dmsDocumentDetailsViewPage.getContent();

		popup.setCaption("");
		popup.setWidth("75%");
		popup.setHeight("85%");
		//popup.setSizeFull();
		popup.setContent(dmsDocumentDetailsViewPage);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			*//**
			 * 
			 *//*
			private static final long serialVersionUID = 1L;

			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});

		popup.setModal(true);
		UI.getCurrent().addWindow(popup);*/
		
		//addWindow(popup);
		//setContent(popup);
		//UI.getCurrent().addWindow(popup);
		
	}
	

}
