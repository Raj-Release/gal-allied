package com.shaic.claim.pedrequest.view;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.scoring.HospitalScoringDetailView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.Table;


public class RevisedViewSeriousDeficiencyTable extends GBaseTable<ViewSeriousDeficiencyDTO>{

	@Inject
	private HospitalScoringDetailView hospitalScoringView;
	
	@Inject
	private ViewSeriousDeficiencyDTO bean;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Object[] NATURAL_COL_ORDER = new Object[] {/* "select",*/
			"serialNumber", "intimationNumber"};

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "deprecation", "serial" })
	@Override
	public void initTable() {
		setSizeFull();
		table.setContainerDataSource(new BeanItemContainer<ViewSeriousDeficiencyDTO>(
				ViewSeriousDeficiencyDTO.class));
		table.setVisibleColumns(NATURAL_COL_ORDER);
		
		table.setColumnWidth("serialNumber", 50);
		table.setColumnWidth("intimationNumber", 250);
		table.setColumnAlignment("intimationNumber", Table.ALIGN_CENTER);

//		table.setColumnWidth("view", 200);
		//table.setColumnExpandRatio("remarks", 20.0f);
		
//		table.setColumnAlignment("remarks", Table.ALIGN_LEFT);
		
		table.setWidth("100%");
		//table.setWidth("70%");
		table.removeGeneratedColumn("View");
		table.addGeneratedColumn("View", new Table.ColumnGenerator() {
			@Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) {
//				VerticalLayout buttonLayout =new VerticalLayout();	
//				ViewSeriousDeficiencyDTO seriousDeficiencyDTO = (ViewSeriousDeficiencyDTO) itemId;
		    	final Button viewHospitalScoringDetailsButton = new Button("View");
		    	viewHospitalScoringDetailsButton.addStyleName(ValoTheme.BUTTON_LINK);
		    	viewHospitalScoringDetailsButton.addClickListener(new Button.ClickListener() {
	    			public void buttonClick(ClickEvent event) {
	    				showScoringView((ViewSeriousDeficiencyDTO) itemId);
	    			} 
		    	});
//		    	buttonLayout.setMargin(Boolean.FALSE);
//		    	buttonLayout.setSpacing(Boolean.FALSE);
		    	return viewHospitalScoringDetailsButton;
			}
		});
		table.setColumnWidth("intimationNumber", 250);
		table.setColumnAlignment("View", Table.ALIGN_CENTER);


	}

	@Override
	public void tableSelectHandler(ViewSeriousDeficiencyDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {

		return "view-seriousdeficiency-";
	}
	
	public void showScoringView(ViewSeriousDeficiencyDTO seriousDeficiencyDTO){
		hospitalScoringView.init(seriousDeficiencyDTO.getIntimationNumber(), false);		
		hospitalScoringView.setScreenName("ViewPage");
		VerticalLayout misLayout = new VerticalLayout(hospitalScoringView);
		Window popup = new com.vaadin.ui.Window();
		popup.setWidth("45%");
//		popup.setHeight("92%");
		popup.setContent(misLayout);
		popup.setClosable(true);
		popup.center();
		popup.setResizable(true);
		popup.addCloseListener(new Window.CloseListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void windowClose(CloseEvent e) {
				System.out.println("Close listener called");
			}
		});
		popup.setModal(true);
		UI.getCurrent().addWindow(popup);
	}
}
