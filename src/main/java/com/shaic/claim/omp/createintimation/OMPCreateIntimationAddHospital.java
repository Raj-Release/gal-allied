package com.shaic.claim.omp.createintimation;


import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.MasterService;
import com.shaic.domain.OMPHospitals;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("unused")
public class OMPCreateIntimationAddHospital extends ViewComponent {

	private static final long serialVersionUID = -4863423334536626287L;
	
	private TextField hospitalCode;
	private TextField hospitalName;
	private TextField hospitalCity;
	private ComboBox hospitalCountry;
	private Button submitButton;
	private Window popupWindow;
	Panel panel;
	
	@Inject
	OMPIntimationService ompIntimationService;
	
	@EJB
	private MasterService masterService;
	
	public void initPopupLayout(final Window popUp, final TextField dummy){
		panel = new Panel();
		popupWindow = popUp;

		popUp.setCaption("OMP Hospital Master");
		popUp.setWidth("45%");
		popUp.setHeight("45%");
		popUp.setClosable(true);
		popUp.center();
		popUp.setResizable(false);
		hospitalCode = new TextField("Hospital Code");
		hospitalName = new TextField("Hospital Name");
		hospitalName.setMaxLength(100);
		hospitalCity = new TextField("Hospital City");
		hospitalCity.setMaxLength(100);
//		hospitalCountry = new TextField("Hospital Country");
		hospitalCountry = new ComboBox("Hospital Country");
		BeanItemContainer<SelectValue> country = masterService.getCountryValue();
		hospitalCountry.setContainerDataSource(country);
		hospitalCountry.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		hospitalCountry.setItemCaptionPropertyId("value");
		hospitalCode.setValue(generateHopitalCode());
		hospitalCode.setReadOnly(true);
		
		submitButton = new Button("Submit");
		submitButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1302108657000824335L;
			@Override
			public void buttonClick(ClickEvent event) {
				Map<String,Object> validationResult = doValidation();
				boolean isValidationPassed = (boolean)validationResult.get("flag");
				if(isValidationPassed){
					OMPHospitals  ompHospital = new OMPHospitals();
					ompHospital.setHospitalCode(hospitalCode.getValue());
					ompHospital.setName(hospitalName.getValue());
					ompHospital.setCity(hospitalCity.getValue());
//					ompHospital.setCountry(hospitalCountry.getValue());
					
					if(hospitalCountry!= null && hospitalCountry.getValue()!= null){
						SelectValue value = (SelectValue) hospitalCountry.getValue();
						Long country = value.getId();
								//Long.valueOf(hospitalCountry.getId()).longValue();
						if(country != null){
							ompHospital.setCountryId(country);
						}
					}
					ompIntimationService.doInsertNewOMPHospital(ompHospital);
//					dummy.setValue(null);
					dummy.setValue("dummy");
					popupWindow.close();
				}else{
					showErrorMessage(String.valueOf(validationResult.get("msg")));
				}
			}
		});
		FormLayout addHospitallayout = new FormLayout(hospitalCode,hospitalName,hospitalCity,hospitalCountry);
		VerticalLayout mainVertical = new VerticalLayout();
		mainVertical.addComponent(addHospitallayout);
		mainVertical.addComponent(submitButton);
		mainVertical.setComponentAlignment(submitButton, Alignment.BOTTOM_CENTER);
		mainVertical.setMargin(true);
		mainVertical.setSizeFull();
		panel.setContent(mainVertical);
		mainVertical.setCaption("OMP Hospital Master");
		setCompositionRoot(mainVertical);	    
	}
	
	public Map<String,Object> doValidation(){
		boolean flag = true;
		StringBuilder errorMsg = new StringBuilder();
		Map<String,Object> errorHolder = new HashMap<String, Object>();
		if(StringUtils.isBlank(hospitalCode.getValue())){
			flag = false;
			errorMsg.append("<br> HospitalCode is empty </br>");
		}
		if(StringUtils.isBlank(hospitalName.getValue())){
			flag = false;
			errorMsg.append("<br> HospitalName is empty </br>");
		}
		if(StringUtils.isBlank(hospitalCity.getValue())){
			flag = false;
			errorMsg.append("<br> HospitalCity is empty </br>");
		}
		if(hospitalCountry!= null&& hospitalCountry.getValue()==null){
		flag = false;
			errorMsg.append("<br> HospitalCountry is empty </br>");
		}		
		errorHolder.put("flag", flag);
		errorHolder.put("msg", errorMsg.toString());
		return errorHolder;
	}
	
	
	private void showErrorMessage(String eMsg) {
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
	
	public String generateHopitalCode(){
		String mainString = "OMPHOSP";
		String codeString = "";
		String dbLastCode = ompIntimationService.getMaxOMPHospitalCode();//"OMPHOSP1119999";
		String codeValue = dbLastCode.substring(dbLastCode.lastIndexOf("P")+1);
		int lastCodeInNumber = Integer.parseInt(codeValue);
		int lengthofNumber = String.valueOf(lastCodeInNumber).length();
		int newHospitalCodeNumber = lastCodeInNumber+1;
		int newCodelengthofNumber = String.valueOf(newHospitalCodeNumber).length();
		if(newCodelengthofNumber == 1){
			codeString = "000"+newHospitalCodeNumber;
		}else if(newCodelengthofNumber == 2){
			codeString = "00"+newHospitalCodeNumber;
		}else if(newCodelengthofNumber == 3){
			codeString = "0"+newHospitalCodeNumber;
		}else{
			codeString = ""+newHospitalCodeNumber;
		}
		return mainString+codeString;
	}
	
}

			

				
