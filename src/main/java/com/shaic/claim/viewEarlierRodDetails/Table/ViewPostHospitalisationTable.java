package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.viewEarlierRodDetails.dto.PreHospitalizationDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPostHospitalisationTable extends ViewComponent {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Map<PreHospitalizationDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<PreHospitalizationDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<PreHospitalizationDTO> container = new BeanItemContainer<PreHospitalizationDTO>(PreHospitalizationDTO.class);
	
	private Table table;
	
	private Map<String, Object> referenceData;
	
	
	public void init(){
		container.removeAllItems();
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		initTable();
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		table.setFooterVisible(true);
		layout.addComponent(table);

		setCompositionRoot(layout);
		
		
		
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}
	
	void initTable() {
		// Create a data source and bind it to a table
		table = new Table("", container);
		container.removeAllItems();
		table.addStyleName("generateColumnTable");
		table.setWidth("100%");
		table.setPageLength(table.getItemIds().size());
		
		//table.setVisibleColumns(new Object[] { "details","claimedAmt", "billingNonPayable", "netAmount", "amount", "deductingNonPayable", "payableAmount", "reason" });
		table.setVisibleColumns(new Object[] { "details","claimedAmt", "billingNonPayable", "reasonableDeduction" ,"netAmount", "reason" });

		table.setColumnHeader("details", "Details");
		table.setColumnHeader("claimedAmt", "Claimed Amount(A)");
		//table.setColumnHeader("billingNonPayable", "Non Payable(Entered in Billing)(B)");
		table.setColumnHeader("billingNonPayable", "Non Payable(B)");
		table.setColumnHeader("reasonableDeduction", "Reasonable Deduction (C)");
		table.setColumnHeader("netAmount", "Net Amount(D)");
		/*table.setColumnHeader("amount", "Amount(D)");
		table.setColumnHeader("deductingNonPayable", "Non Payables (Inc Deductibles)(E)");
		table.setColumnHeader("payableAmount", "Payable Amt(F)");*/
		table.setColumnHeader("reason", "Deductible / Non Payables Reason");
		table.setEditable(true);
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		calculateTotal();

	}
	
	
	
	public void setTableList(final List<PreHospitalizationDTO> list) {
		table.removeAllItems();
		for (final PreHospitalizationDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			PreHospitalizationDTO preHospitalizationDTO = (PreHospitalizationDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(preHospitalizationDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(preHospitalizationDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(preHospitalizationDTO);
			}
			
			if("claimedAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("claimedAmt", field);
				return field;
			} else if ("billingNonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("billingNonPayable", field);
				return field;
			}else if ("reasonableDeduction".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("reasonableDeduction", field);
				return field;
			} 
			else if ("netAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("netAmount", field);
				return field;
			} else if("amount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("amount", field);
				return field;
			} else if("deductingNonPayable".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("deductingNonPayable", field);
				return field;
			}
			else if("payableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("payableAmount", field);
				calculateTotal();
				return field;
			}
			else {
				Field<?> field = super.createField(container, itemId,
						propertyId, uiContext);

				if (field instanceof TextField)
					((TextField) field).setNullRepresentation("");
					field.setReadOnly(true);
					field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					field.setWidth("450px");
				return field;
			}
			
			
		}
		
	}
	
	 public void addBeanToList(PreHospitalizationDTO preHospitalizationDTO) {
	    	container.addBean(preHospitalizationDTO);

//	    	data.addItem(pedValidationDTO);
	    }
	 
	 @SuppressWarnings("unchecked")
	private void calculateTotal(){
				
				List<PreHospitalizationDTO> itemIconPropertyId = (List<PreHospitalizationDTO>) table.getItemIds();
			//	Long netAmount =0l;
				Long claimedAmount = 0l;
				Long nonPayableAmount = 0l;
				Long reasonableDeduction = 0l;
				Long netAmount = 0l;
				/*Long amount =0l;
				Long nonPayableAmount =0l;
				Long payableAmount =0l;*/
				for (PreHospitalizationDTO preHospitalizationDTO : itemIconPropertyId) {
					if(preHospitalizationDTO.getNetAmount() != null){
						netAmount += Math.round(preHospitalizationDTO.getNetAmount());
						}
						if(null != preHospitalizationDTO.getClaimedAmt())
						{
							claimedAmount += Math.round(preHospitalizationDTO.getClaimedAmt());
							
						}
						
						if(null != preHospitalizationDTO.getBillingNonPayable())
						{
							nonPayableAmount = Math.round(preHospitalizationDTO.getBillingNonPayable());
							//nonPayableAmount += Long.parseLong(String.valueOf(preHospitalizationDTO.getBillingNonPayable()));
						}
						
						if(null != preHospitalizationDTO.getReasonableDeduction())
						{
							reasonableDeduction += Math.round(preHospitalizationDTO.getReasonableDeduction());
							//reasonableDeduction += Long.parseLong(String.valueOf(preHospitalizationDTO.getReasonableDeduction()));
						}
					
					/*if(preHospitalizationDTO.getAmount() != null){
					amount += preHospitalizationDTO.getAmount();
					}
					if(preHospitalizationDTO.getDeductingNonPayable() != null){
					nonPayableAmount += preHospitalizationDTO.getDeductingNonPayable();
					}
					if(preHospitalizationDTO.getPayableAmount() != null){
					payableAmount += preHospitalizationDTO.getPayableAmount();
					}*/
				}

				table.setColumnFooter("netAmount", String.valueOf(netAmount));
				table.setColumnFooter("claimedAmt", String.valueOf(claimedAmount));
				table.setColumnFooter("billingNonPayable", String.valueOf(nonPayableAmount));
				table.setColumnFooter("reasonableDeduction", String.valueOf(reasonableDeduction));
				/*table.setColumnFooter("amount", String.valueOf(amount));
				table.setColumnFooter("deductingNonPayable", String.valueOf(nonPayableAmount));
				table.setColumnFooter("payableAmount", String.valueOf(payableAmount));*/
				table.setColumnFooter("details", "Total Amount");
				
	 }
	 
	 public List<PreHospitalizationDTO> getValues() {
	    	@SuppressWarnings("unchecked")
			List<PreHospitalizationDTO> itemIds = (List<PreHospitalizationDTO>) this.table.getItemIds() ;
	    	return itemIds;
	    }

}
