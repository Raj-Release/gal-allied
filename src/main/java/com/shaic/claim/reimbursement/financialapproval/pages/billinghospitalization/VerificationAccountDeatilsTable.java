package com.shaic.claim.reimbursement.financialapproval.pages.billinghospitalization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.vaadin.addon.cdimvp.ViewComponent;
import org.vaadin.csvalidation.CSValidator;

import com.shaic.claim.ViewDetails;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.claim.icdSublimitMapping.IcdSublimitMappingDto;
import com.shaic.claim.ompviewroddetails.OMPPaymentDetailsTableDTO;
import com.shaic.claim.preauth.wizard.dto.PreauthDTO;
import com.shaic.claim.reimbursement.createandsearchlot.CreateAndSearchLotTableDTO;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;

public class VerificationAccountDeatilsTable  extends ViewComponent{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Map<VerificationAccountDeatilsTableDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<VerificationAccountDeatilsTableDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<VerificationAccountDeatilsTableDTO> container = new BeanItemContainer<VerificationAccountDeatilsTableDTO>(VerificationAccountDeatilsTableDTO.class);
	
	private Table table;

	private Button btnAdd;
	
	private Set<String> errorMessages;
	
	private static Validator validator;
	
	private Map<String, Object> referenceData;
	
	//This value will be used for validation.
	public Double totalBillValue;
	
	
	private String presenterString = "";
	
	private ViewDetails objViewDetails;

	private PreauthDTO bean;
	
	private CreateAndSearchLotTableDTO lotBean;
	
	private CheckBox selectAllchk;
	
	public HashMap<Long,Component> compMap = null;

	
	public void initPresenter(String presenterString) {
		this.presenterString = presenterString;
	}
	
	public void init(Object  calculationViewTableDTO) {
		if(calculationViewTableDTO instanceof PreauthDTO){
			this.bean=(PreauthDTO) calculationViewTableDTO;
		}else if(calculationViewTableDTO instanceof CreateAndSearchLotTableDTO){
			this.lotBean = (CreateAndSearchLotTableDTO) calculationViewTableDTO;
		}
	//	populateBillDetails(bean);
		container.removeAllItems();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		this.errorMessages = new HashSet<String>();
		/*btnAdd = new Button();
		btnAdd.setStyleName("link");
		btnAdd.setIcon(new ThemeResource("images/addbtn.png"));
		HorizontalLayout btnLayout = new HorizontalLayout(btnAdd);
		btnLayout.setWidth("100%");
		btnLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_RIGHT);*/
		
		selectAllchk = new CheckBox("Select All");
		selectAllchk.addValueChangeListener(getSelectAllValueChangeListener());
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		layout.setHeight("-1px");
		layout.addComponent(selectAllchk);
		
		initTable();
		table.setWidth("89%");
		table.setHeight("400px");
		table.setPageLength(table.getItemIds().size());
		//table.setSizeFull();
		table.setSortDisabled(true);
		
		layout.addComponent(table);
	//	layout.addComponent(btnAdd);
		layout.setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		//layout.setComponentAlignment(btnAdd,Alignment.TOP_LEFT);
		
//		HorizontalLayout horLayout = new HorizontalLayout();
//		horLayout.addComponent(layout);
//		horLayout.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
		Panel tblPanel = new Panel();
		tblPanel.setWidth("95%");
		tblPanel.setHeight("500px");
		tblPanel.setContent(layout);
		
		//horLayout.setWidth("100%");
		
	/*	Panel tablePanel = new Panel();
		tablePanel.setContent(horLayout);
		tablePanel.setWidth("91%");*/
		setCompositionRoot(tblPanel);
		//setCompositionRoot(horLayout);
	}
	
	
	
	private void setSelactAllCheckBox(Boolean value) {
		List<VerificationAccountDeatilsTableDTO> searchTableDTOList = (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
		
		for (VerificationAccountDeatilsTableDTO searchTableDTO : searchTableDTOList) {
					
						if(value)
						{
							searchTableDTO.setVerifiedCheck(true);
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getRodKey());
								chkBox.setValue(value);
							}
						}
						else
						{
							searchTableDTO.setVerifiedCheck(false);
							if(null != compMap && !compMap.isEmpty())
							{
								CheckBox chkBox = (CheckBox) compMap.get(searchTableDTO.getRodKey());
								chkBox.setValue(value);
							}
						}
			}
		
		}

	public void setValueForSelectAllCheckBox(Boolean value)
	{
		List<VerificationAccountDeatilsTableDTO> tableDataList = (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
		
			for (VerificationAccountDeatilsTableDTO searchTabledto : tableDataList){
				
			}
		}

	private ValueChangeListener getSelectAllValueChangeListener() {
		return new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				
				Boolean selected = (Boolean)event.getProperty().getValue();
				if(selected != null){
					List<VerificationAccountDeatilsTableDTO> itemIds = (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
					
					if(null != itemIds && !itemIds.isEmpty())
					{
						for (VerificationAccountDeatilsTableDTO bean : itemIds) {
							if(selected){
								bean.setVerifiedCheck(true);
							}else{
								bean.setVerifiedCheck(false);
							}
						}
					}
				}
				setSelactAllCheckBox(selected);
			}
		};
	}

	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		table.setVisibleColumns(new Object[] { "serialNumber","verifiedCheck","claimNumber","rodNumber","policyNumber","insuredName","payeeName","accountNumber","ifscCode","paidAmount"});
		table.setColumnHeader("serialNumber", "Sl No");
		table.setColumnHeader("verifiedCheck", "");
		table.setColumnHeader("claimNumber", "Intimation Number");
		table.setColumnHeader("rodNumber", "ROD Number");
		table.setColumnHeader("policyNumber", "Policy Number");
		table.setColumnHeader("insuredName", "Insured Name");
		table.setColumnHeader("payeeName", "Payee Name");
		table.setColumnHeader("accountNumber", "Account Number");
		table.setColumnHeader("ifscCode", "IFSC Code");
		table.setColumnHeader("paidAmount", "Paid Amount");


		table.setEditable(true);
		
		@SuppressWarnings("unchecked")
		List<VerificationAccountDeatilsTableDTO> itemIds =  (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
		if(bean!=null){
		bean.setVerificationAccountDeatilsTableDTO(itemIds);
		}
		if(lotBean != null){
			lotBean.setVerificationAccountDeatilsTableDTO(itemIds);
		}
		
		table.setTableFieldFactory(new ImmediateFieldFactory());
	//manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {
		private static final long serialVersionUID = -2192723245525925990L;

		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			VerificationAccountDeatilsTableDTO entryDTO = (VerificationAccountDeatilsTableDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;
			if (tableItem.get(entryDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(entryDTO, new HashMap<String, AbstractField<?>>());
			} 
				tableRow = tableItem.get(entryDTO);
				if ("serialNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("50px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO);
					generateSlNo(field);
					//onlyNumber(field);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setEnabled(false);
					tableRow.put("serialNumber", field);
					return field;
				}	
				else if  ("verifiedCheck".equals(propertyId)) {
					List<VerificationAccountDeatilsTableDTO> itemIds = (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
					CheckBox field = new CheckBox();
						  for (VerificationAccountDeatilsTableDTO dto : itemIds) {
								  if(dto.getVerifiedCheck() == true)
								   {
									  field.setValue(true);;
								   }
								   else if(dto.getVerifiedCheck() == true)
								   {
									   field.setValue(false);
								   }
						}
					  
					field.setData(entryDTO);	
					tableRow.put("verifiedCheck", field);
					if(compMap==null){
						compMap = new HashMap<Long, Component>();
					}
					compMap.put(entryDTO.getRodKey(), field);
					field.setEnabled(true);
					return field;
				}
				
				else if  ("claimNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("175px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getClaimNumber());
//					onlyNumber(field);
//					field.setEnabled(false);
					generateSlNo(field);
					field.setReadOnly(true);
					tableRow.put("claimNumber", field);
					return field;
				}
				else if ("rodNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("200px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getRodNumber());
					generateSlNo(field);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("rodNumber", field);
					return field;
				}
				else if ("policyNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("160px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getPolicyNumber());
					generateSlNo(field);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("policyNumber", field);
					return field;
				}
				else if ("insuredName".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("150px");
					field.setNullRepresentation("");
					field.setData(entryDTO.getInsuredName());
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("insuredName", field);
					return field;
				}
				else if ("payeeName".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("300px");
					field.setNullRepresentation("");
					field.setData(entryDTO.getPayeeName());
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("payeeName", field);
					return field;
				}
				else if ("accountNumber".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("170px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getAccountNumber());
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("accountNumber", field);
					return field;
				}
				else if ("ifscCode".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getIfscCode());
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("ifscCode", field);
					return field;
				}
				else if ("paidAmount".equals(propertyId)) {
					TextField field = new TextField();
					field.setWidth("100px");
					field.setNullRepresentation("");
					field.setEnabled(true);
					field.setData(entryDTO.getPaidAmount());
					generateSlNo(field);
//					onlyNumber(field);
//					field.setEnabled(false);
					field.setReadOnly(true);
					tableRow.put("paidAmount", field);
					return field;
				}
			else {
					Field<?> field = super.createField(container, itemId,
							propertyId, uiContext);
					if (field instanceof TextField)
						field.setWidth("100%");
					return field;
				}
				
				
		}
	}
	
	
	public void setTableList(final List<VerificationAccountDeatilsTableDTO> list) {
		table.removeAllItems();
		for (final VerificationAccountDeatilsTableDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public List<VerificationAccountDeatilsTableDTO> getValues() {
    	@SuppressWarnings("unchecked")
		List<VerificationAccountDeatilsTableDTO> itemIds = (List<VerificationAccountDeatilsTableDTO>) this.table.getItemIds() ;
    	return itemIds;
    }
	
	private void generateSlNo(TextField txtField)
	{
		
		Collection<VerificationAccountDeatilsTableDTO> itemIds = (Collection<VerificationAccountDeatilsTableDTO>) table.getItemIds();
		
		int i = 0;
		 for (VerificationAccountDeatilsTableDTO calculationViewTableDTO : itemIds) {
			 i++;
			 HashMap<String, AbstractField<?>> hashMap = tableItem.get(calculationViewTableDTO);
			 if(null != hashMap && !hashMap.isEmpty())
			 {
				 TextField itemNoFld = (TextField)hashMap.get("serialNumber");
				 if(null != itemNoFld)
				 {
					 itemNoFld.setReadOnly(false);
					 itemNoFld.setValue(String.valueOf(i));
					 itemNoFld.setReadOnly(true);
					 //itemNoFld.setEnabled(false);
				 }
			 }
		 }
	}
	
	
	public Set<String> validateCalculation() {
		Boolean hasError = false;
//		showOrHideValidation(true);
//		errorMessages.removeAll(getErrors());
		List<VerificationAccountDeatilsTableDTO> itemIds = (List<VerificationAccountDeatilsTableDTO>) table.getItemIds();
		
		if(null != itemIds && !itemIds.isEmpty())
		{
			for (VerificationAccountDeatilsTableDTO bean : itemIds) {
				if(bean.getVerifiedCheck() == false){
 					errorMessages.add("Please Verified Checkbox");
 					return errorMessages;
				}
				
			}
		}
			return null;
		
		}

	private void onlyNumber(TextField field) {
		CSValidator validator = new CSValidator();
		validator.extend(field);
		validator.setRegExp("^[0-9]*$");
		validator.setPreventInvalidTyping(true);
	}
	
	public Set<String> getErrors()
		{
			return this.errorMessages;
		}
		
}
