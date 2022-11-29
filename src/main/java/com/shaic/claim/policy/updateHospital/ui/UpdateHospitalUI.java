package com.shaic.claim.policy.updateHospital.ui;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.AbstractMVPView;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.EnhancedFieldGroupFieldFactory;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.newcode.wizard.pages.IntimationDetailsPage;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;

public class UpdateHospitalUI extends AbstractMVPView implements UpdateHospitalView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 44360340582697356L;

	// private UpdateHospitalDTO bean;

	private UpdateHospitalDTO bean;
	
	@EJB
	private UpdateHospitalService updateHospitalService;

	private BeanFieldGroup<UpdateHospitalDTO> binder;
	
//	private List<Locality> areaList;

	private TextField state;

	private TextField hospitalName;

	private TextArea address;

//	private ComboBox area;

	private TextField pinCode;

	private TextField city;

	private TextField contactNo;

	private TextField faxNo;

	private TextField mobileNo;

	private TextField emailId;

	private TextField internalCode;

	private TextField hospitalCode;

	private Button updateBtn;

	private Button cancelBtn;

	private FormLayout firstLayout;

	private FormLayout secondLayout;

	private HorizontalLayout horizonMain;

	private HorizontalLayout buttonLayout;

	private VerticalLayout vertical;

	private IntimationDetailsPage parent;
	
	private List<SelectValue> locality=new ArrayList<SelectValue>();
	
	private List<Component> mandatoryFields = new ArrayList<Component>();
	
	public void initView(IntimationDetailsPage parent, UpdateHospitalDTO updateHospitalDto) {
		
		this.parent = parent;
		
		this.bean = updateHospitalDto;
		this.binder = new BeanFieldGroup<UpdateHospitalDTO>(
				UpdateHospitalDTO.class);
		this.binder.setItemDataSource(this.bean);
		
		binder.setFieldFactory(new EnhancedFieldGroupFieldFactory());
		
		
		state = (TextField) binder.buildAndBind("State", "state",
				TextField.class);
		hospitalName = (TextField) binder.buildAndBind("Hospital Name",
				"hospitalName", TextField.class);
		address = (TextArea) binder.buildAndBind("Address", "address",
				TextArea.class);
//		area = (ComboBox) binder.buildAndBind("Area", "localityId",
//				ComboBox.class);
		pinCode = (TextField) binder.buildAndBind("Pin Code", "pincode",
				TextField.class);
		city = (TextField) binder.buildAndBind("City", "city", TextField.class);
		contactNo = (TextField) binder.buildAndBind("Contact No",
				"phoneNumber", TextField.class);
		faxNo = (TextField) binder.buildAndBind("Fax No", "faxNumber",
				TextField.class);
		mobileNo = (TextField) binder.buildAndBind("Mobile No", "mobileNumber",
				TextField.class);
		emailId = (TextField) binder.buildAndBind("Email ID", "emailId",
				TextField.class);
		internalCode = (TextField) binder.buildAndBind(
				"Hospital Code (Internal)", "hospitalCode", TextField.class);
		hospitalCode = (TextField) binder.buildAndBind("Hospital Code (IRDA)",
				"hospitalCodeIrda", TextField.class);
		updateBtn = new Button("Update");
		cancelBtn = new Button("Cancel");

		firstLayout = new FormLayout(state, hospitalName, address,/* area,*/
				pinCode);
		secondLayout = new FormLayout(city, contactNo, faxNo, mobileNo,
				emailId, internalCode, hospitalCode);

		buttonLayout = new HorizontalLayout(updateBtn, cancelBtn);
		buttonLayout.setSpacing(true);

		addListener();

		horizonMain = new HorizontalLayout(firstLayout, secondLayout);

		vertical = new VerticalLayout(horizonMain, buttonLayout);
		vertical.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		vertical.setSpacing(true);

		horizonMain.setHeight("100%");
		horizonMain.setWidth("100%");
		
//		fireViewEvent(UpdateHospitalPresenter.SET_REFERENCE_DATA, bean.getCityId());
//
//		@SuppressWarnings("deprecation")
//		BeanItemContainer<SelectValue> localityBean=new BeanItemContainer<SelectValue>(locality);
        
//        area.setContainerDataSource(localityBean);
//        area.setItemCaptionMode(ItemCaptionMode.PROPERTY);
//        area.setItemCaptionPropertyId("value");
//		mainPanel = new Panel("Update Details", vertical);
		// mainPanel.setSizeFull();
		this.setWidth(900, Unit.PIXELS);
		this.setHeight(600, Unit.PIXELS);
		setCompositionRoot(vertical);
		mandatoryFields.add(mobileNo);
		mandatoryFields.add(faxNo);

		showOrHideValidation(false);

		
		

	}

	@SuppressWarnings("serial")
	public void addListener() {
		updateBtn.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 2206691737881802049L;

			@Override
			public void buttonClick(ClickEvent event) {
			
				bean.setState(state.getValue());
				bean.setHospitalName(hospitalName.getValue());
				bean.setAddress(address.getValue());
				bean.setPincode(pinCode.getValue());
				bean.setPhoneNumber(contactNo.getValue());
				if (!faxNo.getValue().equals("")) {
					bean.setFaxNumber(faxNo.getValue().replaceAll("\\s", ""));
				} else {
					bean.setFaxNumber(null);
				}
				if(mobileNo.getValue() !=null && !mobileNo.getValue().equals("")) {
					bean.setMobileNumber(mobileNo.getValue());
				} else {
					bean.setMobileNumber(null);
				}
				if (!emailId.getValue().equals("")) {
					bean.setEmailId(emailId.getValue());
				} else {
					bean.setEmailId(null);
				}
				if (!internalCode.getValue().equals("")) {
					bean.setHospitalCode(internalCode.getValue());
				} else {
					bean.setHospitalCode(null);
				}
				if (hospitalCode != null && hospitalCode.getValue() != null && !hospitalCode.getValue().equals("")) {
					bean.setHospitalCodeIrda(hospitalCode.getValue());
				} else {
					bean.setHospitalCodeIrda(null);
				}
				
				parent.callUpdateHospital(bean);
			}
		});
		
		cancelBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				parent.cancelUpdateHospital();
				
			}
		});
	}
	
	private boolean validatePage() {
		Boolean hasError = false;
		showOrHideValidation(true);
		StringBuffer eMsg = new StringBuffer();

		if (!this.binder.isValid()) {

			for (Field<?> field : this.binder.getFields()) {
				ErrorMessage errMsg = ((AbstractField<?>) field)
						.getErrorMessage();
				if (errMsg != null) {
					eMsg.append(errMsg.getFormattedHtmlMessage());
				}
				hasError = true;
			}

		}
		if (mobileNo.getValue() == null) {
			eMsg.append("Please Choose State.</br>");
			hasError = true;
		}
		if (faxNo.getValue() == null) {
			eMsg.append("Please Choose City. </br>");
			hasError = true;
		}


		if (hasError) {
			setRequired(true);
			Label label = new Label(eMsg.toString(), ContentMode.HTML);
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

			hasError = true;
			return !hasError;
		} else {
			try {
				this.binder.commit();

			} catch (CommitException e) {
				e.printStackTrace();
			}
			showOrHideValidation(false);
			return true;
		}
	}


	private void showOrHideValidation(Boolean isVisible) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				if (field != null) {
					field.setRequired(!isVisible);
					field.setValidationVisible(isVisible);
				}
			}
		}
	}

	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void setUpAreaList(List<Locality> area) {
//		
//		this.areaList=area;	
//		for(int i=0;i<areaList.size();i++){
//			SelectValue selected=new SelectValue();
//			selected.setId(areaList.get(i).getKey());
//			selected.setValue(areaList.get(i).getValue());	
//			
//			this.locality.add(selected);
//		}	
//	}

}
