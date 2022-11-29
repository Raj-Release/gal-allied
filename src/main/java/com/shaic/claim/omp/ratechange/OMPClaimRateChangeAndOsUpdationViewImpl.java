package com.shaic.claim.omp.ratechange;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.Page;
import com.shaic.arch.table.Pageable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.domain.MenuItemBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.ExternalResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
public class OMPClaimRateChangeAndOsUpdationViewImpl extends AbstractMVPView implements OMPClaimRateChangeAndOsUpdationView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private OMPClaimRateChangeAndOsUpdationUI searchForm;
	@Inject
	private	OMPClaimRateChangeAndOsUpdationDetailTable searchResultTable;
	
//	OMPClaimRateChangeAndOsUpdationFormDto	searchDto = new OMPClaimRateChangeAndOsUpdationFormDto();
	
	private VerticalSplitPanel mainPanel;
	
	private Button closeButton;
	
	private Button submitButton;
	
	private VerticalLayout vLayout;
	
	private Window sub;
	
	private TextField modifyrate;
	
	private Panel panal;

	private ExcelExport excelExport;
	
	private Button btnGenerateExcel;
	
	private Link link ;
	
	@PostConstruct
	protected void initView() {
		addStyleName("view");
		
		setSizeFull();
		searchForm.init();
		searchResultTable.init("", false,false);
		
		mainPanel = new VerticalSplitPanel();
		mainPanel.setFirstComponent(searchForm);
		
		vLayout = new VerticalLayout(searchResultTable);
		vLayout.addComponent(buildSecondComponent());
		addFooterButtons();
		mainPanel.setSecondComponent(vLayout);
		
		mainPanel.setSplitPosition(46);
		setHeight("550px");
	//	mainPanel.setHeight("630px");
		setCompositionRoot(mainPanel);
		searchResultTable.addSearchListener(this);
		searchForm.addSearchListener(this);
		resetView();
	}
	

	private VerticalLayout buildSecondComponent(){
		
		Button livecurrnce ;
		livecurrnce = new Button();
		livecurrnce.setCaption("Live Currency Rate");
		//Vaadin8-setImmediate() livecurrnce.setImmediate(true);
		livecurrnce.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		livecurrnce.setWidth("-1px");
		livecurrnce.setHeight("-10px");
		
		
		btnGenerateExcel = new Button();
		btnGenerateExcel.setCaption("Export to Excel");
		//Vaadin8-setImmediate() btnGenerateExcel.setImmediate(true);
		btnGenerateExcel.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnGenerateExcel.setWidth("-1px");
		btnGenerateExcel.setHeight("-10px");
//		btnGenerateExcel.setEnabled(Boolean.FALSE);
		modifyrate = new TextField();
		modifyrate.setCaption("Modify Rate");
		
		modifyrate.setWidth("100px");
		modifyrate.setEnabled(true);
		
		FormLayout formLayout2 = new FormLayout(modifyrate);
			
//		FormLayout formLayout = new FormLayout(livecurrnce);
//		FormLayout formLayout1 = new FormLayout(btnGenerateExcel);
	
		
		formLayout2.setSpacing(false);
		formLayout2.setMargin(false);
//		formLayout.setSpacing(false);
//		formLayout.setMargin(false);
//		formLayout1.setSpacing(false);
//		formLayout1.setMargin(false);
	
				
	VerticalLayout	secondLayout = new VerticalLayout();
		HorizontalLayout hLayout = new HorizontalLayout(formLayout2);
		hLayout.setSpacing(true);
		hLayout.setMargin(false);
		secondLayout.addComponent(hLayout);
		secondLayout.setSpacing(true);
		secondLayout.setMargin(false);
		secondLayout.addComponent(searchResultTable);
		
//		private void addExportButton(){
			
//			exportBtn = new Button("Export to Excel");
//			dummyLabel.setWidth("30px");
//			buttonLayout.addComponent(dummyLabel);
//			buttonLayout.addComponent(exportBtn);
			
			
			if(btnGenerateExcel != null){
			btnGenerateExcel.addClickListener(new Button.ClickListener() {
					
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					if(searchResultTable.getTable() != null && searchResultTable.getTable().getItemIds() != null && searchResultTable.getTable().getItemIds().size()>0){
					excelExport = new ExcelExport(searchResultTable.getTable());
					excelExport.setReportTitle("Claims Status Report New");
					excelExport.setDisplayTotals(false);
					excelExport.getTotalsRow();
					excelExport.export();
					}
					
				}
			});
			
			hLayout.addComponent(btnGenerateExcel);
			}
			 //R1132
			 link = new Link("Live Currency Rate", new ExternalResource("https://www.oanda.com/"));
			 link.setTargetName("_blank");
			hLayout.addComponent(link);
			
			if(livecurrnce != null){
			livecurrnce.addClickListener(new Button.ClickListener() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					//R1132
					 link = new Link("link", new ExternalResource("https://www.oanda.com/"));
					 link.setTargetName("_blank");
				}
				
			});
//			hLayout.addComponent(livecurrnce);
			}
//		}

//		livecurrnce.addClickListener(new ClickListener() {
//		 public void buttonClick(ClickEvent event) {
//		 Window liveWindow = new Window();
//		
//		 liveWindow.setCaption("OMP CLAIMS RATE CHANGE AND OUTSTANDING UPDATION");
//		 liveWindow.setWidth("85%");
//		 liveWindow.setHeight("100%");
//		 liveWindow.setModal(true);
//		 liveWindow.setCaption("");
//		 liveWindow.setContent(registrionDetailsPanel());
//		 
//		 liveWindow.setClosable(true);
//		 liveWindow.addCloseListener(new Window.CloseListener() {
//			 	 
//			 private static final long serialVersionUID = 1L;
//			 
//		 @Override
//			public void windowClose(CloseEvent e) {
//				System.out.println("Close listener called");
//			}
//		});
//			UI.getCurrent().addWindow(liveWindow);
//	
//		    }
//
//	
//		});

		
		return secondLayout;
		
		
	}
	 private Panel registrionDetailsPanel(){
		
		 					 
		 Panel main = new Panel(vLayout);
		 main.addStyleName("girdBorder");
		    main.setCaption("");
		    return main;
	
	 }
	
		
	@Override
	public void resetSearchResultTableValues() {
		searchResultTable.resetTable();
		searchResultTable.getPageable().setPageNumber(1);
		Iterator<Component> componentIter = mainPanel.getComponentIterator();
		while(componentIter.hasNext())
		{
			Component comp = (Component)componentIter.next();
			if(comp instanceof OMPClaimRateChangeAndOsUpdationDetailTable)
			{
				((OMPClaimRateChangeAndOsUpdationDetailTable) comp).removeRow();
			}
		}
	
		
	}
	
	@Override
	public void list(Page<OMPClaimRateChangeAndOsUpdationTableDTO> tableRows) {
		
		if(null != tableRows && null != tableRows.getPageItems() && 0!= tableRows.getPageItems().size())
		{	
			searchResultTable.setTableList(tableRows);
			searchResultTable.tablesize();
//			searchResultTable.setHasNextPage(tableRows.isHasNext());  // pagination not required
		}else
		{
			

			Label successLabel = new Label("<b style = 'color: black;'>No Records found.</b>", ContentMode.HTML);			
			Button homeButton = new Button("OMP CLAIMS RATE CHANGE AND OUTSTANDING UPDATION HOME");
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
			
			homeButton.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;

				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					fireViewEvent(MenuItemBean.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION, null);
					
				}
			});
			
		}
//		searchForm.enableButtons();
		
		
	}
	

	@Override
	public void resetView() {
		searchForm.refresh(); 
		
	}
	
public void addFooterButtons(){
		
	HorizontalLayout buttonsLayout = new HorizontalLayout();
		
		closeButton = new Button("Submit");
		closeButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(searchResultTable.getTable() != null &&  searchResultTable.getTable().getContainerDataSource() != null && searchResultTable.getTable().getContainerDataSource().size() > 0){
					closeButton.setData(searchResultTable.getTable());
					
				}
				if(searchResultTable.getTable() != null && searchResultTable.getTable().getItemIds() != null){
				Collection<OMPClaimRateChangeAndOsUpdationTableDTO> itemIds = (Collection<OMPClaimRateChangeAndOsUpdationTableDTO>) searchResultTable.getTable().getItemIds(); 
				if(modifyrate != null && modifyrate.getValue() != null && !modifyrate.equals("")){
					String modifyRate = modifyrate.getValue();
					if(modifyRate != null && !modifyRate.equals("")){
					Double modify = Double.valueOf(modifyRate);
				fireViewEvent(OMPClaimRateChangeAndOsUpdationPresenter.SUBMIT_SEARCH_VIEW,itemIds,modify);
				//fireViewEvent(MenuItemBean.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION,null);	
					}else{
						String eMsg ="Please Enter Modify Rate";
						showErrorPopup(eMsg);
					}
				}
				}
			}
		});
	Button	clearButton = new Button("Clear");
		clearButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(OMPClaimRateChangeAndOsUpdationPresenter.RESET_SEARCH_FROM,null);
				
			}
		});
		
		buttonsLayout.addComponents(closeButton,clearButton);
		buttonsLayout.setSpacing(true);
		VerticalLayout	btnLayout = new VerticalLayout(buttonsLayout);
		btnLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_CENTER);
		
		vLayout.addComponent(btnLayout);
		
	}
	
	@Override
	public void doSearch() {
		OMPClaimRateChangeAndOsUpdationFormDto searchDTO = searchForm.getSearchDTO();
		Pageable pageable = searchResultTable.getPageable();
		if(searchDTO != null){
		searchDTO.setPageable(pageable);
//		btnGenerateExcel.setEnabled(Boolean.TRUE);
		}
		String userName=(String)getUI().getSession().getAttribute(BPMClientContext.USERID);
		String passWord=(String)getUI().getSession().getAttribute(BPMClientContext.PASSWORD);
		if(searchDTO!=null && searchDTO.getIntimationDate()!=null){
			fireViewEvent(OMPClaimRateChangeAndOsUpdationPresenter.SUBMIT_SEARCH, searchDTO,userName,passWord);
		}else{
			String eMsg ="Please Enter Date Field";
			showErrorPopup(eMsg);
		}
		
	}

	
	private void showErrorPopup(String eMsg) {
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
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void buildSuccessLayout( ) {
		// TODO Auto-generated method stub
		
		Label successLabel = new Label("<b style = 'color: green;'>INR Conversion Succesfully Completed !!!!!</b>", ContentMode.HTML);
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		Button homeButton = new Button("Home");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizontalLayout = new HorizontalLayout(homeButton);
		horizontalLayout.setMargin(true);
		
		VerticalLayout layout = new VerticalLayout(successLabel, horizontalLayout);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);
		hLayout.setStyleName("borderLayout");
		
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

//				fireViewEvent(MenuItemBean.OMP_CLAIMS_RATE_CHANGE_AND_OUTSTANDING_UPDATION, null);
				
			}
		});
	}
}
