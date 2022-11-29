package com.shaic.reimbursement.fraudidentification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.domain.gmcautomailer.GmcAutomailerTableDTO;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.reimbursement.topup_policy_master.search.TopUpPolicyMasterTableDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.Grid.HeaderRow;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.PopupDateField;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextArea;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FraudIdentificationTable extends ViewComponent {
	

	private Table table;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PolicyService policyService;
	
	private Map<FraudIdentificationTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<FraudIdentificationTableDTO, HashMap<String, AbstractField<?>>>();

	BeanItemContainer<FraudIdentificationTableDTO> data = new BeanItemContainer<FraudIdentificationTableDTO>(FraudIdentificationTableDTO.class);
	
	private ArrayList<Component> mandatoryFields = new ArrayList<Component>();
	
	private Button btnAdd;
	private Label dummyLabel;
	private PopupDateField effectiveStartDate;
	
	Grid recipientDetailsGrid;
	
	public Object[] VISIBLE_COLUMNS = new Object[] {"parameterType"};
	
	
	public void init(FraudIdentificationTableDTO parameterType) {
		
		
		btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		
			HorizontalLayout addBtnLayout = new HorizontalLayout(btnAdd);
		addBtnLayout.setWidth("100%");
		addBtnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);
		Label mandatoryLabel = new Label("<b style = 'color: red;'>*Seperate the email ids by using semicolon(;)</b>", ContentMode.HTML);
		
		//FormLayout formLabelLayout = new FormLayout(mandatoryLabel);
			VerticalLayout layout = new VerticalLayout();
			layout.addComponent(addBtnLayout);
			layout.addComponent(mandatoryLabel);
			layout.setMargin(true);
			initTable(layout,parameterType);
			table.setWidth("100%");
			table.setHeight("100%");
			table.setPageLength(table.getItemIds().size());
			
			
			addListener(parameterType);
			
			layout.addComponent(table);

			setCompositionRoot(layout);
		}
	
	
	private void addListener(final FraudIdentificationTableDTO parameterType) {	

		btnAdd.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				//data.removeAllItems();
				int serialNumber = 1;
				FraudIdentificationTableDTO fraudIdentificationTableDTO = new FraudIdentificationTableDTO();
				fraudIdentificationTableDTO.setParameterValue("");
				fraudIdentificationTableDTO.setParameterType(parameterType.getParameterType());
				//fraudIdentificationTableDTO.setEdit(true);
				fraudIdentificationTableDTO.setNewRecord(true);
				
				if(table.getItemIds().size() == 0){
					System.out.println(table.getItemIds().size());
					fraudIdentificationTableDTO.setSerialNumber(String.valueOf(serialNumber));
				}else{
					fraudIdentificationTableDTO.setSerialNumber(String.valueOf(table.getItemIds().size()+1));
				}
				
				data.addItem(fraudIdentificationTableDTO);
			}
		});
	}


	void initTable(VerticalLayout layout, FraudIdentificationTableDTO parameterTypeContainer) {
		data.removeAllItems();
		// Create a data source and bind it to a table
		table = new Table("",data);
		table.addStyleName("generateColumnTable");
	
		table.setColumnHeader("serialNumber", "S.No");
		table.setColumnHeader("disable", "Disable");
		table.setColumnHeader("edit", "Edit");
		if(parameterTypeContainer.getParameterType().equalsIgnoreCase("Policy No")){
			table.setColumnHeader("parameterValue", "Policy No");
			table.setColumnHeader("productName", "Product Name");
			table.setColumnHeader("policyStartDate", "Policy Start Date");
			table.setColumnHeader("policyEndDate", "Policy End Date");
			setVisibleColumnForPolicyNumber();
		}else if(parameterTypeContainer.getParameterType().equalsIgnoreCase("Intermediary Code")){
			table.setColumnHeader("parameterValue", "Intermediary Code");
			table.setColumnHeader("intermediaryName", "Intermediary Name");
			setVisibleColumnForIntermediaryCode();
		}else if(parameterTypeContainer.getParameterType().equalsIgnoreCase("Hospital Code (IRDA)")){
			table.setColumnHeader("parameterValue", "Hospital Code (IRDA)");
			setVisibleColumnForHospitalIRDA();
			table.setColumnHeader("hospitalInternalCode", "Hospital Code (Internal)");
			table.setColumnHeader("hospitalName", "Hospital Name");
			table.setColumnHeader("hospitalAddress", "Hospital Address");
			table.setColumnHeader("hospitalCity", "Hospital city");
		}else if(parameterTypeContainer.getParameterType().equalsIgnoreCase("Hospital Code (Internal)")){
			table.setColumnHeader("parameterValue", "Hospital Code (Internal)");
			setVisibleColumnForHospitalInternal();
			table.setColumnHeader("hospitalName", "Hospital Name");
			table.setColumnHeader("hospitalAddress", "Hospital Address");
			table.setColumnHeader("hospitalCity", "Hospital city");
		}
		table.setColumnHeader("effectiveStartDate", "Effective Start date");
		table.setColumnHeader("effectiveEndDate", "Effective End date");
		
		
		/*for (Object object : new Object[] { "serialNumber","disable","edit","parameterValue","hospitalName","hospitalAddress","hospitalCity","effectiveStartDate","effectiveEndtDate","recipientTo","recipientCc","userRemarks"}) {
			recipientDetailsGrid.addColumn(object);
		}
		recipientDetailsGrid.setContainerDataSource(data);
		HeaderRow groupingHeader = recipientDetailsGrid.prependHeaderRow();
		recipientDetailsGrid.getColumn("recipientTo").setHeaderCaption("To");
		recipientDetailsGrid.getColumn("recipientCc").setHeaderCaption("CC");*/
		table.setColumnHeader("recipientTo", "Recipient DetailsTo");
		table.setColumnHeader("recipientCc", "Recipient DetailsCC");
		//groupingHeader.join(groupingHeader.getCell("recipientTo"), groupingHeader.getCell("recipientCc")).setText("Recipient Details");
		table.setColumnHeader("userRemarks", "User Remarks");
		
		table.removeGeneratedColumn("Action");
		table.addGeneratedColumn("Action", new Table.ColumnGenerator() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object generateCell(final Table source, final Object itemId,
					Object columnId) {
				final Button deleteButton = new Button("Delete");
				//deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
				FraudIdentificationTableDTO dto = (FraudIdentificationTableDTO) itemId;
				deleteButton.setData(itemId);
				
				//delete button should be enable only for new records
				if (dto !=null && isNewRecord(dto) ) {
					deleteButton.setEnabled(true);
				} else {
					deleteButton.setEnabled(false);
				}
				
				
				//TODO: Add right condition for Delete button
				
				deleteButton.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						final FraudIdentificationTableDTO currentItemId = (FraudIdentificationTableDTO) event.getButton().getData();
						if (table.getItemIds().size() > 0) {
							
							ConfirmDialog dialog = ConfirmDialog
									.show(getUI(),
											"Confirmation",
											"Do you want to Delete ?",
											"No", "Yes", new ConfirmDialog.Listener() {

												public void onClose(ConfirmDialog dialog) {
													if (!dialog.isConfirmed()) {
														// Confirmed to continue
														
														table.removeItem(currentItemId);
													} else {
														// User did not confirm
													}
												}
											});
							dialog.setClosable(false);
							
						} else {
							HorizontalLayout layout = new HorizontalLayout(
									new Label("One File is Mandatory."));
							layout.setMargin(true);
							final ConfirmDialog dialog = new ConfirmDialog();
							dialog.setCaption("");
							dialog.setClosable(true);
							dialog.setContent(layout);
							dialog.setResizable(false);
							dialog.setModal(true);
							dialog.show(getUI().getCurrent(), null, true);
						}
						
					}
				});
				return deleteButton;
			}
		});		
		table.setColumnHeader("Action", "Action");
		table.setEditable(true);
		table.setTableFieldFactory(new ImmediateFieldFactory());
	
	}
	
	public void setVisibleColumnForHospitalIRDA()
	{
		 Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","disable","edit","parameterValue","hospitalInternalCode","hospitalName","hospitalAddress","hospitalCity","effectiveStartDate","effectiveEndDate","recipientTo","recipientCc","userRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}
	public void setVisibleColumnForHospitalInternal()
	{
		 Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","disable","edit","parameterValue","hospitalName","hospitalAddress","hospitalCity","effectiveStartDate","effectiveEndDate","recipientTo","recipientCc","userRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}
	public void setVisibleColumnForPolicyNumber()
	{
		 Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","disable","edit","parameterValue","productName","policyStartDate","policyEndDate","effectiveStartDate","effectiveEndDate","recipientTo","recipientCc","userRemarks"};
		 table.setVisibleColumns(VISIBLE_COLUMNS);
	}
	public void setVisibleColumnForIntermediaryCode()
	{
		 Object[] VISIBLE_COLUMNS = new Object[] {"serialNumber","disable","edit","parameterValue","intermediaryName","effectiveStartDate","effectiveEndDate","recipientTo","recipientCc","userRemarks"};
		table.setVisibleColumns(VISIBLE_COLUMNS);
	}
	
	protected void tablesize() {
		table.setPageLength(table.size() + 1);
		int length = table.getPageLength();
		if (length >= 7 ) {
			table.setPageLength(7);
		}
	}
	
		
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		
		//https://vaadin.com/docs/v7/framework/components/components-table.html
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			FraudIdentificationTableDTO dto = (FraudIdentificationTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(dto) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(dto, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(dto);
			}
			
			if("serialNumber".equals(propertyId)) {
				TextField box = new TextField();
				box.setWidth("40px");
				box.setNullRepresentation("");
				tableRow.put("serialNumber", box);
				box.setReadOnly(true);
				box.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				return box;
			} else if("disable".equals(propertyId)) {
				CheckBox box = new CheckBox();
				box.setData(dto);
				tableRow.put("disable", box);
				box.setValue(dto.getDisable() != null ? (dto.getDisable() ? true : false ) : false);
				addDisableListener(box);
				return box;
			} else if("edit".equals(propertyId)) {
					CheckBox box = new CheckBox();
					box.setData(dto);
					tableRow.put("edit", box);
					box.setValue(dto.getEdit() != null ? (dto.getEdit()) : false);
					addEditListener(box);
					return box;
					
				}else if("userRemarks".equals(propertyId)) {
					TextArea textarea = new TextArea();
					textarea.setNullRepresentation("");
					textarea.setRequired(true);
					mandatoryFields.add(textarea);
					showOrHideValidation(false);
					textarea.setWidth("200px");
					textarea.setHeight("50px");
					textarea.setMaxLength(500);
					
					addListener(textarea);
					
					SHAUtils.handleShortcutForFraudIdentification(textarea,null,getUI(),SHAConstants.USER_REMARKS);
					textarea.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					textarea.setData(dto);
					tableRow.put("userRemarks", textarea);
					
					return textarea;
					
				} else if("recipientTo".equals(propertyId)) {
					
					TextArea textarea = new TextArea();
					textarea.setNullRepresentation("");
					textarea.setWidth("200px");
					textarea.setHeight("50px");
					textarea.setMaxLength(4000);
					textarea.setNullRepresentation("");
					
					textarea.setRequired(true);
					mandatoryFields.add(textarea);
					showOrHideValidation(false);
					
					CSValidator emailIdValidator = new CSValidator();
					emailIdValidator.extend(textarea);
					addListenerRecipientTo(textarea);
					//emailIdValidator.setRegExp("^[a-zA-Z 0-9 / @ . , - _]*$");
					//emailIdValidator.setPreventInvalidTyping(true);
					SHAUtils.handleShortcutForFraudIdentification(textarea,null,getUI(),SHAConstants.RECIPIENT_DETAILS_TO);
					textarea.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					
					textarea.setData(dto);
					tableRow.put("recipientTo", textarea);
					
					return textarea;
					
				} else if("recipientCc".equals(propertyId)) {
					
					TextArea textarea = new TextArea();
					textarea.setNullRepresentation("");
					textarea.setWidth("200px");
					textarea.setHeight("50px");
					textarea.setMaxLength(4000);
					textarea.setRequired(true);
					mandatoryFields.add(textarea);
					CSValidator emailIdValidator = new CSValidator();
					emailIdValidator.extend(textarea);
					SHAUtils.handleShortcutForFraudIdentification(textarea,null,getUI(),SHAConstants.RECIPIENT_DETAILS_CC);
					textarea.setDescription(SHAConstants.F8_POPUP_FOR_TEXTAREA);
					addListenerRecipientCc(textarea);
					textarea.setData(dto);
					tableRow.put("recipientCc", textarea);
					
					return textarea;
				} 
				else if("parameterValue".equals(propertyId)) {
					
					TextField textfield = new TextField();
					if(dto.getParameterValue() ==null || dto.getParameterValue().equals("")){
						textfield.setEnabled(true);
					}else{
						textfield.setEnabled(false);
					}
					
					textfield.setNullRepresentation("");
					textfield.setWidth("200px");
					textfield.setHeight("50px");
					textfield.setMaxLength(4000);
					
					if(dto.getParameterType().equalsIgnoreCase("Hospital Code (Internal)")){
						textfield.addBlurListener(HospitalCodeListener(dto));
						tableRow.put("hospitalInternalCode", textfield);
					}
					if(dto.getParameterType().equalsIgnoreCase("Hospital Code (IRDA)")){
						textfield.addBlurListener(HospitalCodeIrdaListener(dto));
						tableRow.put("hospitalIrdaCode", textfield);
					}
					if(dto.getParameterType().equalsIgnoreCase("Policy No")){
						textfield.addBlurListener(policyNumberListener(dto));
						tableRow.put("policyNumber", textfield);
					}
					if(dto.getParameterType().equalsIgnoreCase("Intermediary Code")){
						textfield.addBlurListener(intermediaryListener(dto));
						tableRow.put("intermediaryCode", textfield);
					}
					
							
					//We can use the exiting DTO object here
					//No need to copy the DTO object
					
					//textfield.addBlurListener(HospitalCodeListener(dto));
					
					//textfield.addValueChangeListener(getHospitalCodeValueListener(dto));					
					//addHospitalCodeChangeListener(textfield, dto);					
					
					
					textfield.setData(dto);
					textfield.setImmediate(true);
					
					return textfield;
				} 
				else if("effectiveStartDate".equals(propertyId)) {
					PopupDateField effStartDate = new PopupDateField();
				
				
					Date currentDate=new Date();
					effStartDate.setDateFormat("dd/MM/yyyy");
					
					Calendar c = Calendar.getInstance(); 
					c.setTime(currentDate); 
					c.add(Calendar.DATE, -1);
					effStartDate.setRangeStart(c.getTime());
					
					effStartDate.setRangeEnd(currentDate);
					effStartDate.setData(dto);
					tableRow.put("effectiveStartDate",effStartDate);
					
					if(dto.getEdit()==true  || dto.getNewRecord()){
						effStartDate.setEnabled(true);
						effStartDate.setTextFieldEnabled(true);
					}else{
						effStartDate.setEnabled(false);
						effStartDate.setTextFieldEnabled(false);
					}
					
					
					
					return effStartDate;
				} 
				else if("effectiveEndDate".equals(propertyId)) {
					
					PopupDateField effEndDate = new PopupDateField();
					
					Date currentDate=new Date();
					effEndDate.setDateFormat("dd/MM/yyyy");
					
					effEndDate.setRangeStart(currentDate);
					
					/*Calendar c = Calendar.getInstance(); 
					c.setTime(currentDate); 
					//c.add(Calendar.DATE,);
					effEndDate.setRangeEnd(c.getTime());*/
					
					if(dto.getEdit()==true || dto.getNewRecord()==true){
						effEndDate.setEnabled(true);
						effEndDate.setTextFieldEnabled(true);
					}
					else{
						effEndDate.setEnabled(false);
						effEndDate.setTextFieldEnabled(false);
					}
					
					effEndDate.setData(dto);
					tableRow.put("effectiveEndDate",effEndDate);
					
					return effEndDate;
				} 
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField){
					if(dto.getParameterValue() ==null || dto.getParameterValue().equals("")){
						field.setEnabled(true);
					}else{
						field.setEnabled(false);
					}
					field.setWidth("100%");
					((TextField) field).setData((FraudIdentificationTableDTO)itemId);
					((TextField) field).setImmediate(true);
					
					tableRow.put(propertyId.toString(), (AbstractField<?>) field);
					

					//System.out.println("Item Id:"+itemId.toString());
					//System.out.println("property Id:"+propertyId.toString());
					
				}
				return field;
			}
		}
	}
	
	

	
	private BlurListener HospitalCodeListener(FraudIdentificationTableDTO dto) {

		BlurListener liste = new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				TextField hospitalCodeBlurTxtField = (TextField)event.getComponent();
				String hospitalCode = (String) hospitalCodeBlurTxtField.getValue();
				
				if(hospitalCode!=null && !hospitalCode.isEmpty()){

				List<Hospitals> HospitalList=hospitalService.searchHospitalById(hospitalCode);
				if(HospitalList!=null){
					for(Hospitals hospitalDetails : HospitalList){
						dto.setHospitalName(hospitalDetails.getName());
						dto.setHospitalAddress(hospitalDetails.getAddress());
						dto.setHospitalCity(hospitalDetails.getCity());
					}
				}
				/*System.out.println(dto.getHospitalName());
				System.out.println(dto.getHospitalAddress());
				System.out.println(dto.getHospitalCity());*/
				
				HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
				if(hashMap!=null){
					TextField tfHospitalName= (TextField)hashMap.get("hospitalName");
					tfHospitalName.setValue(dto.getHospitalName());
					TextField tfHospitalAddress= (TextField)hashMap.get("hospitalAddress");
					tfHospitalAddress.setValue(dto.getHospitalAddress());
					TextField tfHospitalCity= (TextField)hashMap.get("hospitalCity");
					tfHospitalCity.setValue(dto.getHospitalCity());
					}
				}
			}
		};
		return liste;
	}
	
	private BlurListener HospitalCodeIrdaListener(FraudIdentificationTableDTO dto) {

		BlurListener liste = new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				TextField hospitalCodeBlurTxtField = (TextField)event.getComponent();
				String hospitalIrdaCode = (String) hospitalCodeBlurTxtField.getValue();
				
				if(hospitalIrdaCode!=null && !hospitalIrdaCode.isEmpty()){

					List<Hospitals> HospitalList=hospitalService.searchHospitalByIrdaCode(hospitalIrdaCode);
					if(HospitalList!=null){
						for(Hospitals hospitalDetails : HospitalList){
							dto.setHospitalInternalCode(hospitalDetails.getHospitalCode());
							dto.setHospitalName(hospitalDetails.getName());
							dto.setHospitalAddress(hospitalDetails.getAddress());
							dto.setHospitalCity(hospitalDetails.getCity());
						}
					}
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					if(hashMap!=null){
						TextField tfHospitalCode= (TextField)hashMap.get("hospitalInternalCode");
						tfHospitalCode.setValue(dto.getHospitalInternalCode());
						TextField tfHospitalName= (TextField)hashMap.get("hospitalName");
						tfHospitalName.setValue(dto.getHospitalName());
						TextField tfHospitalAddress= (TextField)hashMap.get("hospitalAddress");
						tfHospitalAddress.setValue(dto.getHospitalAddress());
						TextField tfHospitalCity= (TextField)hashMap.get("hospitalCity");
						tfHospitalCity.setValue(dto.getHospitalCity());
					}
				}
				
				
			}
		};
		return liste;
	}
	
	private BlurListener policyNumberListener(FraudIdentificationTableDTO dto) {

		BlurListener liste = new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				TextField policyNumberBlurTxtField = (TextField)event.getComponent();
				String policyNumber = (String) policyNumberBlurTxtField.getValue();

				if(policyNumber!=null && !policyNumber.isEmpty()){
				
					Policy policyobj=policyService.getByPolicyNumber(policyNumber);
					if(policyobj!=null){
					dto.setProductName(policyobj.getProductName());
					String dateFormat="dd-MM-yyyy hh:mm:ss";
					dto.setPolicyStartDate(SHAUtils.parseDate(policyobj.getPolicyFromDate(),dateFormat));
					dto.setPolicyEndDate(SHAUtils.parseDate(policyobj.getPolicyToDate(),dateFormat));
					}
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					if(hashMap!=null){
						TextField tfproductName= (TextField)hashMap.get("productName");
						tfproductName.setValue(dto.getProductName());
						TextField tfPolicyStartDate= (TextField)hashMap.get("policyStartDate");
						tfPolicyStartDate.setValue(dto.getPolicyStartDate());
						TextField tfPolicyEndDate= (TextField)hashMap.get("policyEndDate");
						tfPolicyEndDate.setValue(dto.getPolicyEndDate());
					}
				}
				
			}
		};
		return liste;
	}
	
	private BlurListener intermediaryListener(FraudIdentificationTableDTO dto) {

		BlurListener liste = new BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				TextField intermediaryBlurTxtField = (TextField)event.getComponent();
				String intermediaryCode = (String) intermediaryBlurTxtField.getValue();

				if(intermediaryCode!=null && !intermediaryCode.isEmpty()){
					DBCalculationService dbService = new DBCalculationService();
					String intermediaryName = dbService.getInermediaryDetails(intermediaryCode);
					if(intermediaryName!=null){
						dto.setIntermediaryName(intermediaryName);
					}

					HashMap<String, AbstractField<?>> hashMap = tableItem.get(dto);
					if(hashMap!=null){
						TextField tfIntermediaryName= (TextField)hashMap.get("intermediaryName");
						tfIntermediaryName.setValue(dto.getIntermediaryName());

					}
				}
			}
		};
		return liste;
	}
	
	
	
	private void addListener(final TextArea txtField)
	{	
		txtField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					FraudIdentificationTableDTO tableDTO = (FraudIdentificationTableDTO)txtField.getData();
					tableDTO.setUserRemarks(value);
				}
			}
		});
	}
	private void addListenerRecipientTo(final TextArea txtField)
	{	
		txtField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					Boolean isEmailValid= true;
					
					if(value!=null){
						String[] emaildIdList = value.split(";");
						for(String email :emaildIdList ){
							if(!email.isEmpty())
							{
								if(!(SHAUtils.isValidEmail(email)))
								{
									isEmailValid = false;
									break;
									
								}
							}
						}
					}
					
					FraudIdentificationTableDTO tableDTO = (FraudIdentificationTableDTO)txtField.getData();
					if (tableDTO !=null && (tableDTO.getParameterValue() == null || "".equalsIgnoreCase(tableDTO.getParameterValue()))){
						if(!isEmailValid){
							showInvalidEmailDialog();
							System.out.println("Email is invalid");
						}
					}

					tableDTO.setRecipientTo(value);
				}
			}
		});
	}
	
	private void addListenerRecipientCc(final TextArea txtField)
	{	
		txtField
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					String value = (String) event.getProperty().getValue();
					Boolean isEmailValid = true;
					
					if(value!=null){
						String[] emaildIdList = value.split(";");
						for(String email :emaildIdList ){
							if(!email.isEmpty())
							{
								if(!SHAUtils.isValidEmail(email))
								{
									isEmailValid = false;
									break;
									
								}
							}
						}
					}
					
					FraudIdentificationTableDTO tableDTO = (FraudIdentificationTableDTO)txtField.getData();
					if (tableDTO !=null && (tableDTO.getParameterValue() == null || "".equalsIgnoreCase(tableDTO.getParameterValue()))){
						if(!isEmailValid){
							showInvalidEmailDialog();
						}
					}
					tableDTO.setRecipientCc(value);
				}
			}
		});
	}

	public void showInvalidEmailDialog(){
		Label label = new Label("Please Enter Valid Email.", ContentMode.HTML);
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
	
	public List<FraudIdentificationTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<FraudIdentificationTableDTO> itemIds = (List<FraudIdentificationTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }


	public void addToList(FraudIdentificationTableDTO idaTableDTO) {
		// TODO Auto-generated method stub
		data.addItem(idaTableDTO);
		
	}
	
	private void addDisableListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					System.out.println("Calling disable value change listener****************** ");
					boolean value = (Boolean) event.getProperty().getValue();
					FraudIdentificationTableDTO tableDTO = (FraudIdentificationTableDTO)chkBox.getData();
					
					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);
					if(hashMap != null){
						
						PopupDateField dfeffectiveStartDate= (PopupDateField)hashMap.get("effectiveStartDate");
						PopupDateField dfeffectiveEndDate= (PopupDateField)hashMap.get("effectiveEndDate");
						TextArea userRemarks = (TextArea)hashMap.get("userRemarks");
						TextArea receipientTo = (TextArea)hashMap.get("recipientTo");
						TextArea receipientCc = (TextArea)hashMap.get("recipientCc");
						CheckBox cbEdit = (CheckBox)hashMap.get("edit");
						
						if(value == true){
							
							//Disable all fields
							
							if(dfeffectiveStartDate!=null){
								dfeffectiveStartDate.setEnabled(false);
								dfeffectiveStartDate.setTextFieldEnabled(false);
							}
							if(dfeffectiveEndDate!=null){
								dfeffectiveEndDate.setTextFieldEnabled(false);
								dfeffectiveEndDate.setEnabled(false);
							}
							if(userRemarks!=null){
								userRemarks.setReadOnly(true);
								userRemarks.setEnabled(false);
							}


							if(receipientTo!=null){
								receipientTo.setReadOnly(true);
								receipientTo.setEnabled(false);
							}


							if(receipientCc!=null){
								receipientCc.setReadOnly(true);
								receipientCc.setEnabled(false);
							}
							
							if(cbEdit !=null ){
							cbEdit.setEnabled(false);
							cbEdit.setValue(false);
							}

						}
						else{
							
							//enable all fields
							
							/*if(dfeffectiveStartDate!=null){
								dfeffectiveStartDate.setEnabled(true);
								dfeffectiveStartDate.setTextFieldEnabled(true);
							}
							if(dfeffectiveEndDate!=null){
								dfeffectiveEndDate.setTextFieldEnabled(true);
								dfeffectiveEndDate.setEnabled(true);
							}
							if(userRemarks!=null){
								userRemarks.setEnabled(true);
							}


							if(receipientTo!=null){
								receipientTo.setEnabled(true);
							}


							if(receipientCc!=null){
								receipientCc.setEnabled(true);
							}*/
							
							if(cbEdit !=null ){
								cbEdit.setEnabled(true);
								}


						}

					}
				}
			}
		});
	}
	private void addEditListener(final CheckBox chkBox)
	{	
		chkBox
		.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(null != event && null != event.getProperty() && null != event.getProperty().getValue())
				{  
					System.out.println("Calling Edit value change listener****************** ");
					boolean value = (Boolean) event.getProperty().getValue();
					FraudIdentificationTableDTO tableDTO = (FraudIdentificationTableDTO)chkBox.getData();
					

					HashMap<String, AbstractField<?>> hashMap = tableItem.get(tableDTO);

					if(hashMap != null){

						PopupDateField dfeffectiveStartDate= (PopupDateField)hashMap.get("effectiveStartDate");
						PopupDateField dfeffectiveEndDate= (PopupDateField)hashMap.get("effectiveEndDate");
						TextArea userRemarks = (TextArea)hashMap.get("userRemarks");
						TextArea receipientTo = (TextArea)hashMap.get("recipientTo");
						TextArea receipientCc = (TextArea)hashMap.get("recipientCc");
						CheckBox cbEdit = (CheckBox)hashMap.get("edit");
						
						if(value == true){
							
							//Enable all fields
							
							if(dfeffectiveStartDate!=null){
								dfeffectiveStartDate.setEnabled(true);
								dfeffectiveStartDate.setTextFieldEnabled(true);
							}
							if(dfeffectiveEndDate!=null){
								dfeffectiveEndDate.setTextFieldEnabled(true);
								dfeffectiveEndDate.setEnabled(true);
							}
							if(userRemarks!=null){
								userRemarks.setReadOnly(false);
								userRemarks.setEnabled(true);
							}


							if(receipientTo!=null){
								receipientTo.setReadOnly(false);
								receipientTo.setEnabled(true);
							}


							if(receipientCc!=null){
								receipientCc.setReadOnly(false);
								receipientCc.setEnabled(true);
							}
							

						}
						else{
							
							//disable all fields
							
							if(dfeffectiveStartDate!=null){
								dfeffectiveStartDate.setEnabled(false);
								dfeffectiveStartDate.setTextFieldEnabled(false);
							}
							if(dfeffectiveEndDate!=null){
								dfeffectiveEndDate.setTextFieldEnabled(false);
								dfeffectiveEndDate.setEnabled(false);
							}
							if(userRemarks!=null){
								userRemarks.setEnabled(false);
							}


							if(receipientTo!=null){
								receipientTo.setEnabled(false);
							}


							if(receipientCc!=null){
								receipientCc.setEnabled(false);
							}
							

						}

					}
					tableDTO.setEdit(value);
				}
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void setRequired(Boolean isRequired) {

		if (!mandatoryFields.isEmpty()) {
			for (int i = 0; i < mandatoryFields.size(); i++) {
				AbstractField<?> field = (AbstractField<?>) mandatoryFields
						.get(i);
				field.setRequired(isRequired);
			}
		}
	}
	
	protected void showOrHideValidation(Boolean isVisible) {
		
		for (Component component : mandatoryFields) {
			AbstractField<?> field = (AbstractField<?>) component;
			field.setRequired(!isVisible);
			field.setValidationVisible(isVisible);
		}
	}
	
	public Table getTable(){
		return table;
	}
	
	public Boolean isNewRecord(FraudIdentificationTableDTO fdto){
		if (fdto !=null 
				&& (fdto.getNewRecord() == true)) {
			return true;
		}
		
		return false;
				
	}
	
}

