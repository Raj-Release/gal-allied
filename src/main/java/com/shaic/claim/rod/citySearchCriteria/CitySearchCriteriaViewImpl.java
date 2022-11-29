package com.shaic.claim.rod.citySearchCriteria;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.vaadin.addon.cdimvp.AbstractMVPView;





import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class CitySearchCriteriaViewImpl extends AbstractMVPView implements CitySearchCriteriaView {

	
	@Inject
	private SearchPayableAtTable payableTable;
	
	private Panel panelLayout;
	
	private TextField payableName;
	
	private Button searchPayableAt;
	
	private VerticalLayout mainLayout;
	
	private String presenterString;
	
	public void initView(Window popup) {
		
//		addStyleName("view");
		mainLayout = new VerticalLayout();
		
		payableName = new TextField("Enter Payable At");
		
		searchPayableAt = new Button();
		searchPayableAt.setCaption("Search");
		//Vaadin8-setImmediate() searchPayableAt.setImmediate(true);
		searchPayableAt.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchPayableAt.setWidth("-1px");
		searchPayableAt.setHeight("-10px");
		
		addPayableListner();
		
		FormLayout formLayoutRight = new FormLayout(searchPayableAt);
		FormLayout formLayoutLeft = new FormLayout(payableName);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		payableTable.setWindowObject(popup);
		payableTable.initPresenter(presenterString);
		payableTable.init("",false,false);
		
		
		mainLayout.addComponent(fieldLayout);
		mainLayout.addComponent(payableTable);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setMargin(false);		 
		 
		panelLayout = new Panel();
		panelLayout.setContent(mainLayout);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
		
	}
	
 public void initView(Window popup,CreateAndSearchLotTableDTO initiateDTO) {
		
//		addStyleName("view");
		mainLayout = new VerticalLayout();
		payableName = new TextField("Enter Payable At");
		
		searchPayableAt = new Button();
		searchPayableAt.setCaption("Search");
		//Vaadin8-setImmediate() searchPayableAt.setImmediate(true);
		searchPayableAt.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		searchPayableAt.setWidth("-1px");
		searchPayableAt.setHeight("-10px");
		
		addPayableListner();
		
		FormLayout formLayoutRight = new FormLayout(searchPayableAt);
		FormLayout formLayoutLeft = new FormLayout(payableName);
		
		HorizontalLayout fieldLayout = new HorizontalLayout(formLayoutLeft,formLayoutRight);
		
		fieldLayout.setMargin(true);
		fieldLayout.setWidth("100%");
		
		payableTable.setWindowObject(popup);
		payableTable.initPresenter(presenterString);
		payableTable.init("",false,false);
		payableTable.setLotAndBatchDTO(initiateDTO);
		
		mainLayout.addComponent(fieldLayout);
		mainLayout.addComponent(payableTable);
		//Vaadin8-setImmediate() mainLayout.setImmediate(false);
		mainLayout.setMargin(false);		 
		 
		panelLayout = new Panel();
		panelLayout.setContent(mainLayout);
		panelLayout.setWidth("100%");
		
		setCompositionRoot(panelLayout);
		
	}

	public void addPayableListner() {
	
		searchPayableAt.addClickListener(new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			if(payableName != null && payableName.getValue() != null && !payableName.getValue().isEmpty()){
				fireViewEvent(CitySearchCriteriaPresenter.PAYABLE_SEARCH_CRITERIA, payableName.getValue());
			}else{
				showErrorMessage("Please Enter Valid Payable At");
			}
		}
	});
	
}
	
	private void showErrorMessage(String eMsg) {
		/*Label label = new Label(eMsg, ContentMode.HTML);
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
		dialog.show(getUI().getCurrent(), null, true);*/
		
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		GalaxyAlertBox.createErrorBox(eMsg, buttonsNamewithType);
	}
	
	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTableValues(List<SearchPayableAtTableDTO> list) {
		if(list != null && list.isEmpty()){
			showErrorMessage("Please Enter Valid Payable At");
		}else{
			payableTable.setTableList(list);
		}
				
	}

	public void setPresenterString(String presenterString)
	{
		this.presenterString = presenterString;
	}

	

}
