package com.shaic.claim.viewEarlierRodDetails.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.rod.wizard.dto.BillEntryDetailsDTO;
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

public class ViewBillDetailsTable extends ViewComponent {

	
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
		
		table.setVisibleColumns(new Object[] { "sno","rodNumber","fileType","fileName","billNumber","billDate","noOfItems","billValue","itemNo","itemName","classification","category","noOfDays", "perDayAmt", "claimedAmount", "entitlementNoOfDays",
				"entitlementPerDayAmt","amount","deductionNonPayableAmount","payableAmount","reason","medicalRemarks","irdaLevel1","irdaLevel2","irdaLevel3" });
		table.setColumnHeader("sno", "S.No");
		table.setColumnHeader("rodNumber", "ROD No");
		table.setColumnHeader("fileType", "File Type");
		table.setColumnHeader("fileName", "File Name");
		table.setColumnHeader("billNumber", "Bill No");
		table.setColumnHeader("billDate", "Bill Date");
		table.setColumnHeader("noOfItems", "No of Items");
		table.setColumnHeader("billValue", "Bill Value");
		table.setColumnHeader("itemNo", "Item No");
		table.setColumnHeader("itemName", "Item Name");
		table.setColumnHeader("classification", "Classification");
		table.setColumnHeader("category", "Category");
		table.setColumnHeader("noOfDays", "No of Days(A)");
		table.setColumnHeader("perDayAmt", "Per Day Amt(B)");
		table.setColumnHeader("claimedAmount", "Claimed Amount(C)");
		table.setColumnHeader("entitlementNoOfDays", "No of Days(F)");
		table.setColumnHeader("entitlementPerDayAmt", "Per Day Amt(G)");
		table.setColumnHeader("amount", "Amount(H)");
		table.setColumnHeader("deductionNonPayableAmount", "Non Payables (Inc Deductibles)(I)");
		table.setColumnHeader("payableAmount", "Payable Amt(J)");
		table.setColumnHeader("reason", "Deductible / Non Payables Reason");
		table.setColumnHeader("medicalRemarks", "Medical Remarks");
		table.setColumnHeader("irdaLevel1", "IRDALevel 1");
		table.setColumnHeader("irdaLevel2", "IRDALevel 2");
		table.setColumnHeader("irdaLevel3", "IRDALevel 3");
	    
		
		
		table.setColumnWidth("sno", 70);
		table.setColumnWidth("rodNumber", 200);
		table.setColumnWidth("billNumber", 120);
		table.setColumnWidth("billDate", 100);
		table.setColumnWidth("fileType", 180);
		table.setColumnWidth("fileName", 120);
		table.setColumnWidth("noOfItems", 90);
		table.setColumnWidth("billValue", 90);
		table.setColumnWidth("medicalRemarks", 200);
		table.setColumnWidth("irdaLevel1", 220);
		table.setColumnWidth("irdaLevel2", 220);
		table.setColumnWidth("irdaLevel3", 220);
		table.setColumnWidth("itemNo", 70);
		table.setColumnWidth("itemName", 230);
		table.setColumnWidth("classification", 200);
		table.setColumnWidth("category", 280);
		table.setColumnWidth("noOfDays", 100);
		table.setColumnWidth("perDayAmt", 120);
		table.setColumnWidth("claimedAmount", 120);
		table.setColumnWidth("entitlementNoOfDays", 100);
		table.setColumnWidth("entitlementPerDayAmt", 120);
		table.setColumnWidth("amount", 120);
		table.setColumnWidth("deductionNonPayableAmount", 220);
		table.setColumnWidth("payableAmount", 120);
		table.setColumnWidth("reason", 220);
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
					field.setWidth("500px");
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
	 
	 public void clearObjects(){
//		 	SHAUtils.setClearTableItem(tableItem);
			SHAUtils.setClearReferenceData(referenceData);
//			this.container = null;
//			this.tableItem = null;
			if(table!=null){
				table.removeAllItems();
			}
	 }
	
}
