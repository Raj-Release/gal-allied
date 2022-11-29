package com.shaic.paclaim.registration;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class PreviousPAClaimsTableForClaimRegistration extends GBaseTable<PreviousClaimsTableDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PreviousClaimsTableDTO previousClaimsTableDTO;

	@Override
	public void tableSelectHandler(PreviousClaimsTableDTO t) {		
//		fireViewEvent(ClaimRegistrationPresenter.CLICK_VIEW_CLAIM_STATUS, t);
	}
	
	
	public static final Object[] COLUMN_HEADER = new Object[] {"serialNumber",
		"policyNumber","insuredName","patientName","patientDOB","parentName","parentDOB","category","intimationNumber","admissionDate",
		"hospitalName","claimType","claimNumber","diagnosis",
		"provisionAmount","approvedAmount","claimStatus"
	};

	@Override
	public void removeRow() {
				
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(PreviousClaimsTableDTO.class));
		
		table.setVisibleColumns(COLUMN_HEADER);
		table.setHeight("155px");
		table.setPageLength(table.size()+1);
		table.removeGeneratedColumn("ViewDetails");
		table.addGeneratedColumn("ViewDetails", new Table.ColumnGenerator() {
		      /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		    	Button button = new Button("ViewDetails");
		    	button.addClickListener(new Button.ClickListener() {
			        /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						previousClaimsTableDTO = (PreviousClaimsTableDTO)itemId;
						fireViewEvent(PAClaimRegistrationPresenter.CLICK_VIEW_PA_CLAIM_STATUS, itemId);
			        } 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		
	}
	
	@Override
	public String textBundlePrefixString() {
		return "previousClaims-claimRegistration-";
	}	
}
