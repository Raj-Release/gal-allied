package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Field;
import com.vaadin.ui.Panel;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ViewIRDACategoryBillTable extends ViewComponent {
	

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<HospitalisationDTO, HashMap<String, AbstractField<?>>> tableItem = new HashMap<HospitalisationDTO, HashMap<String, AbstractField<?>>>();
	
	BeanItemContainer<HospitalisationDTO> container = new BeanItemContainer<HospitalisationDTO>(HospitalisationDTO.class);

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
		
		Panel panel = new Panel(layout);
		panel.addStyleName("girdBorder");
		panel.setCaption("Amount Claimed Details");

		setCompositionRoot(panel);
		
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
		
		table.setVisibleColumns(new Object[] { "sno","irdaCode","category","noOfDays", "perDayAmt", "claimedAmount", "billingNonPayableAmt", "netAmount", "entitlementNoOfDays",
				"entitlementPerDayAmt","amount","deductionNonPayableAmount","payableAmount" });
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("irdaCode","IRDA Category Code");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("noOfDays", "No of Days(A)");
		table.setColumnHeader("perDayAmt", "Per Day Amt(B)");
		table.setColumnHeader("claimedAmount", "Claimed Amount(C)");
		table.setColumnHeader("billingNonPayableAmt", "Non Payable(Entered in Billing)(D)");
		table.setColumnHeader("netAmount", "Net Amount(E)");
		table.setColumnHeader("entitlementNoOfDays", "No of Days(F)");
		table.setColumnHeader("entitlementPerDayAmt", "Per Day Amt(G)");
		table.setColumnHeader("amount", "Amount(H)");
		table.setColumnHeader("deductionNonPayableAmount", "Non Payables (Inc Deductibles)(I)");
		table.setColumnHeader("payableAmount", "Payable Amt(J)");
	    
		table.setColumnWidth("sno", 70);
		table.setColumnWidth("irdaCode", 140);
		table.setColumnWidth("category", 230);
		table.setColumnWidth("noOfDays", 100);
		table.setColumnWidth("perDayAmt", 100);
		table.setColumnWidth("claimedAmount", 130);
		table.setColumnWidth("billingNonPayableAmt", 200);
		table.setColumnWidth("netAmount", 100);
		table.setColumnWidth("entitlementNoOfDays", 100);
		table.setColumnWidth("entitlementPerDayAmt", 100);
		table.setColumnWidth("amount", 100);
		table.setColumnWidth("deductionNonPayableAmount", 200);
		table.setColumnWidth("payableAmount", 100);
		table.setFooterVisible(true);
		
		table.setEditable(true);
		table.setStyleName("container");
		
//		manageListeners();

		// Use a custom field factory to set the edit fields as immediate.
		// This is used when the table is in editable mode.
		table.setTableFieldFactory(new ImmediateFieldFactory());
		calculateTotal();

	}
	
    public String getColumnHeader(final Object propertyId) {
        final String originalHeader = table.getColumnHeader(propertyId);
        if (originalHeader != null) {
            final String layoutedHeader = originalHeader.replaceAll("\n", "<br />");
            return layoutedHeader;
        }
        return originalHeader;
    }

	
	public void setTableList(final List<HospitalisationDTO> list) {
		table.removeAllItems();
		for (final HospitalisationDTO bean : list) {
			table.addItem(bean);
		}
		table.sort();
	}
	
	public class ImmediateFieldFactory extends DefaultFieldFactory {

	
		private static final long serialVersionUID = 1L;
		
		@Override
		public Field<?> createField(Container container, Object itemId,
				Object propertyId, Component uiContext){
			HospitalisationDTO preHospitalizationDTO = (HospitalisationDTO) itemId;
			Map<String, AbstractField<?>> tableRow = null;

			if (tableItem.get(preHospitalizationDTO) == null) {
				tableRow = new HashMap<String, AbstractField<?>>();
				tableItem.put(preHospitalizationDTO, new HashMap<String, AbstractField<?>>());
			} else {
				tableRow = tableItem.get(preHospitalizationDTO);
			}
			
			if("claimedAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("claimedAmt", field);
				return field;
			} else if ("billingNonPayableAmt".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				field.setData(preHospitalizationDTO);
				tableRow.put("billingNonPayable", field);
				return field;
			} else if ("netAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("netAmount", field);
				return field;
			
			} else if("deductionNonPayableAmount".equals(propertyId)) {
				TextField field = new TextField();
				field.setWidth("100px");
				field.setNullRepresentation("");
				field.setEnabled(false);
				field.setData(preHospitalizationDTO);
				field.setStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
				tableRow.put("deductionNonPayableAmount", field);
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
	
	 public void addBeanToList(HospitalisationDTO preHospitalizationDTO) {
	    	container.addBean(preHospitalizationDTO);

//	    	data.addItem(pedValidationDTO);
	    }
	 
	 @SuppressWarnings("unchecked")
	private void calculateTotal(){
				
				List<HospitalisationDTO> itemIconPropertyId = (List<HospitalisationDTO>) table.getItemIds();
				Long claimedAmount = 0l;
				Long netAmount =0l;
				Long amount =0l;
				Long nonPayableAmount =0l;
				Long payableAmount =0l;
				for (HospitalisationDTO hospitalizationDTO : itemIconPropertyId) {
					if(hospitalizationDTO.getNetAmount() != null){
					netAmount += hospitalizationDTO.getNetAmount();
					}
					if(hospitalizationDTO.getAmount() != null){
					amount += hospitalizationDTO.getAmount();
					}
					if(hospitalizationDTO.getClaimedAmount() != null){
						claimedAmount += hospitalizationDTO.getClaimedAmount();	
					}
					if(hospitalizationDTO.getDeductionNonPayableAmount() != null){
					nonPayableAmount += hospitalizationDTO.getDeductionNonPayableAmount();
					}
					if(hospitalizationDTO.getPayableAmount() != null){
					payableAmount += hospitalizationDTO.getPayableAmount();
					}
				}

				table.setColumnFooter("netAmount", String.valueOf(netAmount));
				table.setColumnFooter("amount", String.valueOf(amount));
				table.setColumnFooter("claimedAmount", String.valueOf(claimedAmount));
				table.setColumnFooter("deductionNonPayableAmount", String.valueOf(nonPayableAmount));
				table.setColumnFooter("payableAmount", String.valueOf(payableAmount));
				table.setColumnFooter("details", "Total Amount");
//				
	 }


}
