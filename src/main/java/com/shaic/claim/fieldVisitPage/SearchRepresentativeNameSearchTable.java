package com.shaic.claim.fieldVisitPage;

import javax.inject.Inject;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.reassignfieldVisitPage.ReAssignFieldVisitPresenter;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class SearchRepresentativeNameSearchTable extends GBaseTable<SearchRepresentativeTableDTO> {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SearchRepresentativeTableDTO searchRepresentationTableDto;
	
	private static final Object[] NATURAL_COL_ORDER = new Object[] {
			"representativeName", "representativeContactNo", "representativeCategory", "totalAllocation", "allocated",
			"canByAllocated" };
	
	private String presenterString;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<SearchRepresentativeTableDTO>(
				SearchRepresentativeTableDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		try{
			table.removeGeneratedColumn("Select");
		}catch(Exception e){
			
		}
		try{
			table.addGeneratedColumn("Select", new Table.ColumnGenerator() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public Object generateCell(final Table source, final Object itemId,
						Object columnId) {					
					Button selectButton = new Button("Select");
					 selectButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
					 selectButton.addStyleName(ValoTheme.BUTTON_LINK);
					 selectButton.addClickListener(new Button.ClickListener() {
						
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							searchRepresentationTableDto = (SearchRepresentativeTableDTO) itemId;
							final String representativeCode = searchRepresentationTableDto.getRepresentativeCode();
							if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.ASSIGN_FVR)){
								fireViewEvent(FieldVisitPagePresenter.RETRIVE_REPRESENTATION, representativeCode);
							}else if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.REASSIGN_FVR)){
								fireViewEvent(ReAssignFieldVisitPresenter.RETRIVE_REPRESENTATION, representativeCode);
							}
						}
					});
					return selectButton;
				}
			});
		}catch(Exception e){
			
		}
		
	}

	@Override
	public void tableSelectHandler(SearchRepresentativeTableDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPresenterString(String presenterString){
		this.presenterString = presenterString;
	}

	@Override
	public String textBundlePrefixString() {
		return "representative-details-";
	}

}
