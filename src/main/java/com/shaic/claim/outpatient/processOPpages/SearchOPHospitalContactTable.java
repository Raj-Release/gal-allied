/**
 * 
 */
package com.shaic.claim.outpatient.processOPpages;

import javax.ejb.EJB;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.outpatient.processOP.wizard.ProcessOPClaimWizardPresenter;
import com.shaic.domain.PolicyService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author ntv.narasimhaj
 *
 */
public class SearchOPHospitalContactTable extends GBaseTable<HospitalDto>{

	@EJB
	private PolicyService policyService;

	private static final long serialVersionUID = 1L;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setWidth("1000px");
        table.setContainerDataSource(new BeanItemContainer<HospitalDto>(HospitalDto.class));
        Object[] NATURAL_COL_ORDER = new Object[] {
    		"name",
    		"address",
    		"phoneNumber",
    		"fax",
    	};
       
        
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.removeGeneratedColumn("select");
		table.addGeneratedColumn("select", new Table.ColumnGenerator() {
		      @Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	  Button btnSelect = new Button("select");
		    	  btnSelect.addStyleName("link");
		    	  btnSelect.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	  HospitalDto selectedInsured = (HospitalDto)itemId;
		    	  btnSelect.addClickListener(new ClickListener() {
		  			@Override
		  			@SuppressWarnings("unchecked")
		  			public void buttonClick(ClickEvent event) {
		  				HospitalDto selectedInsured = (HospitalDto)itemId;
		  				fireViewEvent(ProcessOPClaimWizardPresenter.HOSPITAL_CONTACT_DETAILS, selectedInsured);
		  				
		  			}
		  		});
		    	  
		    	  return btnSelect;
		      }
		});
		
		table.setColumnHeader("select", "Select");
		table.setColumnHeader("name", "Hospital Name");
	    table.setColumnHeader("address", "Hospital Address");
	    table.setColumnHeader("phoneNumber", "Hospital Ph No");
	    table.setColumnHeader("fax", "Hospital Fax");
		
	}
	
	public void setColumnHeader(){
		table.setColumnHeader("select", "");
		table.setColumnHeader("name", "Hospital Name");
	    table.setColumnHeader("address", "Hospital Address");
	    table.setColumnHeader("phoneNumber", "Hospital Ph No");
	    table.setColumnHeader("fax", "Hospital Fax");
	    table.setColumnWidth("select", 100);
	    table.setColumnWidth("name", 350);
	    table.setColumnWidth("address", 350);
	    table.setColumnWidth("phoneNumber", 100);
	    table.setColumnWidth("fax", 100);
	    
	}

	
	public void getErrorMessage(String eMsg) {
		Label label = new Label(eMsg, ContentMode.HTML);
		label.setStyleName("errMessage");
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(label);

		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setCaption("Error");
		dialog.setClosable(true);
		dialog.setContent(layout);
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.show(getUI().getCurrent(), null, true);
	}

	@Override
	public void tableSelectHandler(HospitalDto t) {
		// TODO Auto-generated method stub
		
	}
	
	public void removeAllItems(){
		table.removeAllItems();
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "search-hospital-details-";
	}
	protected void setTablesize(){
		table.setPageLength(table.size());
		int length =table.getPageLength();
		if(length>=7){
			table.setPageLength(7);
		}
		
	}


}
