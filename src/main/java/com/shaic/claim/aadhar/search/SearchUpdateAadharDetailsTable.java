package com.shaic.claim.aadhar.search;

import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.table.GBaseTable;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SearchUpdateAadharDetailsTable extends GBaseTable<SearchUpdateAadharTableDTO>{
	
	private final static Object[] NATURAL_COL_ORDER = new Object[]{"serialNumber","intimationNo","policyNo","insuredName","policyStartDate","policyEndDate"};

	@Override
	public void removeRow() {
		table.removeAllItems();
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchUpdateAadharTableDTO>(SearchUpdateAadharTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		
		table.removeGeneratedColumn("updateAadhar");
		table.addGeneratedColumn("updateAadhar",
				new Table.ColumnGenerator() {
					@Override
					public Object generateCell(final Table source,
							final Object itemId, Object columnId) {
						final Button updatePanDetailsButton = new Button("Update Aadhar Details");
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
						updatePanDetailsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
						updatePanDetailsButton.addClickListener(new Button.ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								SearchUpdateAadharTableDTO tableDTO = (SearchUpdateAadharTableDTO) itemId;
								if(tableDTO.getAadharRefNo() != null && !tableDTO.getAadharRefNo().isEmpty()){
									getDuplicateAadharAlert();
								} else {
									updateAadharDetails(tableDTO);
								}
							}

						});
						
						return updatePanDetailsButton;
					}
				});
		

		table.setHeight("331px");
	}

	@Override
	public void tableSelectHandler(SearchUpdateAadharTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateAadharDetails(SearchUpdateAadharTableDTO t){

		
		 VaadinSession session = getSession();
			
			Boolean isActiveHumanTask = SHAUtils.isActiveHumanTask(t.getUsername(),t.getPassword(),t.getTaskNumber(), session);
			try{
					if(! isActiveHumanTask){
					
						SHAUtils.setActiveOrDeactiveClaim(t.getUsername(),t.getPassword(),t.getTaskNumber(),session);
				
						fireViewEvent(MenuPresenter.UPDATE_AADHAR_DETAILS_WIZARD, t);
					}else{
						getErrorMessage("This record is already opened by another user");
					}
			}catch(Exception e){
				
				Integer existingTaskNumber= (Integer)session.getAttribute(SHAConstants.TOKEN_ID);
				SHAUtils.releaseHumanTask(t.getUsername(), t.getPassword(), existingTaskNumber, session);
				
				e.printStackTrace();
			}
		
	
	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "update-aadhar-details-";
	}
	
	public void getErrorMessage(String eMsg){
		
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
	
	public void getDuplicateAadharAlert() {	 
		 
		 Label successLabel = new Label(
					"<b style = 'color: red;'> Aadhar Number is Already Updated. </br> "+ " Edit is Currently Disabled. "+ "</b>",
					ContentMode.HTML);
			Button homeButton = new Button("OK");
			homeButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			VerticalLayout layout = new VerticalLayout(successLabel, homeButton);
			layout.setComponentAlignment(homeButton, Alignment.MIDDLE_CENTER);
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.setStyleName("borderLayout");
	
			final ConfirmDialog dialog = new ConfirmDialog();
			dialog.setClosable(false);
			dialog.setContent(layout);
			dialog.setResizable(false);
			dialog.setModal(true);
			dialog.show(getUI().getCurrent(), null, true);
	
			homeButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 7396240433865727954L;
	
				@Override
				public void buttonClick(ClickEvent event) {
					dialog.close();
					
				}
			});
		}

}
