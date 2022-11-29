package com.shaic.claim.previousclaim;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.premedical.dto.PreviousClaimsTableDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class ViewPreviousClaimInsuredDataTable extends GBaseTable<PreviousClaimsTableDTO> {

	private static final long serialVersionUID = -7771166917702155656L;
	private static final Object[] NATURAL_COL_ORDER= new Object[]{"policyNumber","claimNumber","customerID","insuredPatientName","claimStatus","claimAmount","amountPayable"};
	
	
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
	
		table.removeAllItems();
		table.setWidth("100%");
<<<<<<< HEAD
		//Vaadin8-setImmediate() //Vaadin8-setImmediate() table.setImmediate(false);
=======
		//Vaadin8-setImmediate() table.setImmediate(false);
>>>>>>> a1b28df37a8ac40ebb73f2c4cd51036c598b388a
        table.setContainerDataSource(new BeanItemContainer<PreviousClaimsTableDTO>(PreviousClaimsTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
//		table.setColumnHeader("policyNumber", "Policy Number");
//		table.setColumnHeader("customerID", "Claim Number");
//		table.setColumnHeader("insuredPatientName", "Insured <br>Patient Name");
//		table.setColumnHeader("claimStatus", "Claim Stutus");
//		table.setColumnHeader("claimAmount", "Claim Amount");
		table.setFooterVisible(true);
		table.setPageLength(table.size() + 6);
		table.setHeight("300px");
		table.removeGeneratedColumn("viewClaimStatus");
		table.addGeneratedColumn("viewClaimStatus",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Claim Status");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {

								
								
								popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								
							
								popup.setClosable(true);
								popup.center();
								popup.setResizable(true);
								popup.addCloseListener(new Window.CloseListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void windowClose(CloseEvent e) {
										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
		table.removeGeneratedColumn("viewDocuments");
		table.addGeneratedColumn("viewDocuments",
				new Table.ColumnGenerator() {

					private static final long serialVersionUID = 1L;

					@Override
					public Object generateCell(Table source, final Object itemId,
							Object columnId) {
						Button button = new Button("View Documents");
						button.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {

								
								
								popup = new com.vaadin.ui.Window();
								popup.setCaption("");
								popup.setWidth("75%");
								popup.setHeight("85%");
								
							
								popup.setClosable(true);
								popup.center();
								popup.setResizable(true);
								popup.addCloseListener(new Window.CloseListener() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 1L;

									@Override
									public void windowClose(CloseEvent e) {
										System.out.println("Close listener called");
									}
								});

								popup.setModal(true);
								UI.getCurrent().addWindow(popup);
							}

						});
						button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				    	button.setWidth("150px");
				    	button.addStyleName(ValoTheme.BUTTON_LINK);
						return button;
					}
				});
		
		
	}
	@Override
	public void tableSelectHandler(PreviousClaimsTableDTO vto) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String textBundlePrefixString() {
		
		return "claim-InsuredOPhistory-";
	}




}
