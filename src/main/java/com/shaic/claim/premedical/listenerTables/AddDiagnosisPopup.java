package com.shaic.claim.premedical.listenerTables;

import java.util.HashMap;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.galaxyalert.utils.GalaxyAlertBox;
import com.galaxyalert.utils.GalaxyButtonTypesEnum;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.test.SuggestingContainer;
import com.shaic.claim.preauth.wizard.dto.DiagnosisDetailsTableDTO;
import com.shaic.domain.MasterService;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.v7.ui.AbstractSelect.NewItemHandler;
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

public class AddDiagnosisPopup extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DiagnosisDetailsTableDTO bean;
	
	@EJB
	private MasterService masterService;
	
	private ComboBox cmbDiagnosis;
	
	private TextField txtNewDiagnosis;
	
    private Button submitBtn;
	
	private Button cancelBtn;
	
	private Window popup;
	
	private ComboBox comb;
	
	@SuppressWarnings("deprecation")
	public void init(DiagnosisDetailsTableDTO bean,ComboBox comb,Window popup){
		this.bean = bean;
	    this.comb = comb;
	    this.popup = popup;
//		BeanItemContainer<SelectValue> diagnosisList = masterService.getDiagnosisList();
		
		cmbDiagnosis = new ComboBox();
		
		txtNewDiagnosis = new TextField("New Diagnosis");
		txtNewDiagnosis.setNullRepresentation("");
		
		TextField dummyText = new TextField();
		dummyText.setHeight("25px");
		dummyText.setReadOnly(true);
		dummyText.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		
		TextField dummy1 = new TextField();
		dummy1.setHeight("20px");
		dummy1.setReadOnly(true);
		dummy1.setStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		
		VerticalLayout mainVertical = new VerticalLayout(dummyText,txtNewDiagnosis);
		mainVertical.setComponentAlignment(txtNewDiagnosis, Alignment.MIDDLE_CENTER);
		
		
//		VerticalLayout mainVertical = new VerticalLayout(cmbDiagnosis);
//		mainVertical.setComponentAlignment(cmbDiagnosis, Alignment.MIDDLE_CENTER);
		
//		final SuggestingContainer diagnosisContainer = new SuggestingContainer(masterService);
		final BeanItemContainer<SelectValue> diagnosisContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		cmbDiagnosis.setContainerDataSource(diagnosisContainer);	
		cmbDiagnosis.setFilteringMode(FilteringMode.STARTSWITH);
		cmbDiagnosis.setTextInputAllowed(true);
		cmbDiagnosis.setNullSelectionAllowed(true);
		cmbDiagnosis.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		cmbDiagnosis.setItemCaptionPropertyId("value");
		cmbDiagnosis.setNewItemsAllowed(true);
		
		cmbDiagnosis.addValueChangeListener(new Property.ValueChangeListener() {
	            @Override
	            public void valueChange(ValueChangeEvent event) {
	                
	                // tell the custom container that a value has been selected. This is necessary to ensure that the
	                // selected value is displayed by the ComboBox
	            	SelectValue value = (SelectValue) event.getProperty().getValue();
					if (value != null)
	            	{
						cmbDiagnosis.select(value);
	            	}
	            }
	        });
		
		cmbDiagnosis.setNewItemHandler(new NewItemHandler() {
				
				private static final long serialVersionUID = -4453822645147859276L;

				
				@Override
				public void addNewItem(String newItemCaption) {
					SelectValue newDiagonsisValue = masterService.addDiagnosis(newItemCaption);
					diagnosisContainer.addItem(newDiagonsisValue);
					cmbDiagnosis.addItem(newDiagonsisValue);
//					diagnosisContainer.setSelectedBean(newDiagonsisValue);
					cmbDiagnosis.select(newDiagonsisValue);
				}
			});
		
		submitBtn=new Button("Submit");
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
		
		cancelBtn.addClickListener(new Button.ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
//				ConfirmDialog dialog = ConfirmDialog.show(getUI(),"Confirmation", "Are you sure You want to Cancel ?",
//				        "No", "Yes", new ConfirmDialog.Listener() {
//
//				            public void onClose(ConfirmDialog dialog) {
//				                if (!dialog.isConfirmed()) {
//				                	popup.close();
//				                } else {
//				                    dialog.close();
//				                    popup.close();
//				                }
//				            }
//				        });
//				dialog.setStyleName(Reindeer.WINDOW_BLACK);
				popup.close();
			}
		});
		
		
		submitBtn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
//				SelectValue value = (SelectValue)cmbDiagnosis.getValue();
//				if(value != null){
//				bean.setDiagnosisName(value);
//				SuggestingContainer diagnosisList = (SuggestingContainer) comb.getContainerDataSource();
//     			diagnosisList.addBean(value);
//     			diagnosisList.setSelectedBean(value);
//     			comb.addItem(value);
//				comb.select(value);
				if(txtNewDiagnosis != null && txtNewDiagnosis.getValue() != null && ! txtNewDiagnosis.getValue().equals("")){
				Boolean searchDiagnosisByValue = masterService.searchDiagnosisByValue(txtNewDiagnosis.getValue());
				if(searchDiagnosisByValue){
					result(!searchDiagnosisByValue);
					txtNewDiagnosis.setValue(null);
				}else{
						SelectValue newDiagonsisValue = masterService.addDiagnosis(txtNewDiagnosis.getValue().toUpperCase());
						final SuggestingContainer diagnosisContainer = (SuggestingContainer) comb.getContainerDataSource();
						SelectValue st = new SelectValue(newDiagonsisValue.getId(), newDiagonsisValue.getValue().toUpperCase());
						diagnosisContainer.setSelectedBean(st);
						comb.select(st);
						comb.setValue(st);

						popup.close();
				}
				}
				else{
					result(true);
				}
			}
		});
	}

	public void result(Boolean result) {
		
		//Label successLabel = null;
		String successString=null;
		
		if(result){
       //successLabel = new Label("<b style = 'color: red;'>Please Enter Diagnosis!!! </b>", ContentMode.HTML);
        successString="<b style = 'color: red;'>Please Enter Diagnosis!!! </b>";
		}else{
		//	 successLabel = new Label("<b style = 'color: red;'>Duplicate Diagnosis!!! </b>", ContentMode.HTML);
			 successString="<b style = 'color: red;'>Duplicate Diagnosis!!! </b>";
		}
		
//		Label noteLabel = new Label("<b style = 'color: red;'>  In case of query next step would be </br> viewing the letter and confirming </b>", ContentMode.HTML);
		
		/*Button homeButton = new Button("Ok");
		homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);*/
		/*VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
		layout.setComponentAlignment(homeButton, Alignment.BOTTOM_CENTER);
		layout.setSpacing(true);
		layout.setMargin(true);
		HorizontalLayout hLayout = new HorizontalLayout(layout);
		hLayout.setMargin(true);*/
		
		/*final ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("");
		dialog.setClosable(false);
		dialog.setContent(hLayout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);*/
		HashMap<String, String> buttonsNamewithType = new HashMap<String, String>();
		buttonsNamewithType.put(GalaxyButtonTypesEnum.OK.toString(), "OK");
		HashMap<String, Button> messageBoxButtons = GalaxyAlertBox
				.createAlertBox(successString, buttonsNamewithType);
		Button homeButton = messageBoxButtons.get(GalaxyButtonTypesEnum.OK
				.toString());
		homeButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7396240433865727954L;

			@Override
			public void buttonClick(ClickEvent event) {
				//dialog.close();
//				fireViewEvent(MenuItemBean.PROCESS_PED_QUERY,true);
				
			}
		});
		
	}

}
