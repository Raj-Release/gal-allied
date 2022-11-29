package com.shaic.claim.legal.processconsumerforum.page.consumerforum;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.cdi.UIScoped;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@UIScoped
public class ConsumerFormSearchUI extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Button btnSearch;
	
	private Button btnCancel;
	
	private TextField txtIntimationNumber;
	
	private TextField txtPolicyNumber;


	private VerticalLayout mainLayout;

	private HorizontalLayout horizontalLayout;
	
	public static Window popup;

	private String screenName;
	
	private Boolean isSearch = false;
	
	@Inject
	private SearchProcessConsumerForumTable searchProcessConsumerForumTable;

	public void initBinder() {
		
	}

	public void init() {
		
		
		txtIntimationNumber = new TextField("Intimation Number");
		txtPolicyNumber = new TextField("Policy Number");
		
		btnSearch = new Button("Search");
		btnSearch.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btnCancel = new Button("cancel");
		
		horizontalLayout = new HorizontalLayout(new FormLayout(txtIntimationNumber),
				new FormLayout(txtPolicyNumber));
		
		HorizontalLayout buttonHorizontal = new HorizontalLayout(btnSearch,btnCancel);
		buttonHorizontal.setSpacing(true);
		horizontalLayout.setSpacing(true);
		searchProcessConsumerForumTable.init("", false, false);
		searchProcessConsumerForumTable.setWindowObject(popup ,screenName);
		mainLayout = new VerticalLayout(horizontalLayout, buttonHorizontal,searchProcessConsumerForumTable);
		
		mainLayout.setComponentAlignment(buttonHorizontal, Alignment.MIDDLE_CENTER);
		mainLayout.setMargin(true);
		Panel mainPanel = new Panel();
		mainPanel.setContent(mainLayout);
		setCompositionRoot(mainPanel);
		addListener();
		
	}

	private void addListener() {

		btnSearch.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				fireViewEvent(ConsumerForumPagePresenter.SEARCH_CONSUMER_FORUM,
						txtIntimationNumber.getValue(),
						txtPolicyNumber.getValue());
			}
		});

		btnCancel.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				popup.close();
				
				
			}
		});
	}
	
	public void setTableData(List<SearchProcessConsumerForumTableDTO> tableData){
		if(tableData != null && !tableData.isEmpty()){
			
			searchProcessConsumerForumTable.setTableList(tableData);	
			
		}else{
			getErrorMessage("No Records Found");
		}
		
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
	
	public void setPopup(Window popup,String screenName){
		this.popup = popup;
		this.screenName = screenName;
	}
}
