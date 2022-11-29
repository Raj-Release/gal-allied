package com.shaic.reimbursement.paymentprocess.createbatch.search;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotPresenter;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.domain.ClaimPayment;
import com.shaic.domain.MasterService;
import com.shaic.domain.reimbursement.ReimbursementService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ChangePayeeNamePopUp extends ViewComponent{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ViewSearchCriteriaTableDTO bean;
	
	private CreateAndSearchLotTableDTO createAndSearchLotTableDTO;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private SearchCreateBatchService batchService;
	
	private TextField txtChangePayeeName;
	
    private Button submitBtn;
	
	private Button cancelBtn;
	
	private Window popup;
	
	private TextField txtPayeeName;
	
	private Window searchPayeeNamePopup;
	
	private String presenterString;
	
	public void init(ViewSearchCriteriaTableDTO bean,CreateAndSearchLotTableDTO createAndSearchLotTableDTO,Window popup,Window payeeNamePopup,String presenterString){
		this.bean = bean;
	    this.txtPayeeName = bean.getTxtPayeeName();
	    this.popup = popup;
	    this.searchPayeeNamePopup = payeeNamePopup;
	    this.createAndSearchLotTableDTO = createAndSearchLotTableDTO;
	    this.presenterString = presenterString;
		
	    txtChangePayeeName = new TextField("Edit Payee Name");
	    txtChangePayeeName.setNullRepresentation("");
		
		TextField dummyText = new TextField();
		dummyText.setHeight("25px");
		dummyText.setReadOnly(true);
		dummyText.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		
		TextField dummy1 = new TextField();
		dummy1.setHeight("20px");
		dummy1.setReadOnly(true);
		dummy1.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		
		VerticalLayout mainVertical = new VerticalLayout(dummyText,txtChangePayeeName);
		mainVertical.setComponentAlignment(txtChangePayeeName, Alignment.MIDDLE_CENTER);
		txtChangePayeeName.setValue(txtPayeeName.getValue());

		/*final BeanItemContainer<SelectValue> diagnosisContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		txtPayeeName.setFilteringMode(FilteringMode.STARTSWITH);
		txtPayeeName.setTextInputAllowed(true);
		txtPayeeName.setNullSelectionAllowed(true);
		txtPayeeName.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		txtPayeeName.setItemCaptionPropertyId("value");
		txtPayeeName.setNewItemsAllowed(true);*/
		
		
		
		submitBtn=new Button("Save");
		cancelBtn=new Button("Cancel");
			
		submitBtn.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		submitBtn.setWidth("-1px");
		submitBtn.setHeight("-10px");
			
		cancelBtn.addStyleName(ValoTheme.BUTTON_DANGER);
		cancelBtn.setWidth("-1px");
		cancelBtn.setHeight("-10px");
		
		HorizontalLayout btnHorizontal = new HorizontalLayout(submitBtn,cancelBtn);
		btnHorizontal.setSpacing(true);
		mainVertical.addComponent(dummy1);
		mainVertical.addComponent(btnHorizontal);
		mainVertical.setSpacing(true);
		mainVertical.setComponentAlignment(btnHorizontal, Alignment.BOTTOM_CENTER);
		
		mainVertical.setSpacing(true);
		addListener();
		setCompositionRoot(mainVertical);
		
	}
	public void addListener(){
		
		
		txtChangePayeeName.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(Property.ValueChangeEvent event) {	
			
				String value = (String)event.getProperty().getValue();
				
				
				if(null != value && !(value.equals("")))
				{
					
					bean.setChangePayeeName(value);
				}
				else
				{
					bean.setChangePayeeName("");
				}
				
				txtChangePayeeName.setWidth("150px");
				txtChangePayeeName.addStyleName(ValoTheme.BUTTON_LINK);
			
			}			
		});
	
		

	/*txtPayeeName.setNewItemHandler(new NewItemHandler() {
			
			private static final long serialVersionUID = -4453822645147859276L;
	
			@SuppressWarnings("unused")
			@Override
			public void addNewItem(String newItemCaption) {
				SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
				diagnosisContainer.addItem(newDiagonsisValue);
				cmbDiagnosis.addItem(newDiagonsisValue);
	//			diagnosisContainer.setSelectedBean(newDiagonsisValue);
				cmbDiagnosis.select(newDiagonsisValue);
			}
		});*/
			
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

				popup.close();
			}
		});
		
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {

				if(txtChangePayeeName != null && txtChangePayeeName.getValue() != null && ! txtChangePayeeName.getValue().equals("")){
				
					if(bean.getPayeeName().contains("."))
					{
						String str = bean.getPayeeName();
						String strPayeeName[] = str.split("\\.");
						String payeeName = strPayeeName[1];

						if(!(txtChangePayeeName.getValue().contains(payeeName)) &&
								!(payeeName.contains(txtChangePayeeName.getValue()))){

							result(Boolean.TRUE);
						}
						else
						{
							if(null != bean.getChangePayeeName()){
								txtPayeeName.setValue(bean.getChangePayeeName());
							}
							
							if(SHAConstants.CREATE_LOT_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString)){
								fireViewEvent(CreateAndSearchLotPresenter.UPDATE_PAYEE_NAME,createAndSearchLotTableDTO,bean);
							}
							else if(SHAConstants.CREATE_BATCH_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString))
							{
								fireViewEvent(SearchCreateBatchPresenter.UPDATE_PAYEE_NAME,createAndSearchLotTableDTO,bean);
							}
							
							searchPayeeNamePopup.close();
						}
					}
					else if(!(txtChangePayeeName.getValue().contains(bean.getPayeeName())) &&
							!(bean.getPayeeName().contains(txtChangePayeeName.getValue())))
					{
						
						result(Boolean.TRUE);
						
					}
					else
					{
						if(null != bean.getChangePayeeName()){
							txtPayeeName.setValue(bean.getChangePayeeName());
						}
						
						if(SHAConstants.CREATE_LOT_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString)){
							fireViewEvent(CreateAndSearchLotPresenter.UPDATE_PAYEE_NAME,createAndSearchLotTableDTO,bean);
						}
						else if(SHAConstants.CREATE_BATCH_PAYEE_NAME_CHANGE.equalsIgnoreCase(presenterString))
						{
							fireViewEvent(SearchCreateBatchPresenter.UPDATE_PAYEE_NAME,createAndSearchLotTableDTO,bean);
						}
						searchPayeeNamePopup.close();
						
					}
				
				}
				
			
				
				popup.close();	
				
			}
		});
	}

	public void result(Boolean result) {
		
		Label successLabel = null;

		
		if(result){
        successLabel = new Label("<b style = 'color: red;'>Enter Valid Payee Name</b>", ContentMode.HTML);
		}
		
		Button homeButton = new Button("Ok");
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
				bean.setChangePayeeName(null);
				dialog.close();
				
			}
		});
		
	}



}
