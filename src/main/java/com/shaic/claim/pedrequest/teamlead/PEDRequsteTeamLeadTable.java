package com.shaic.claim.pedrequest.teamlead;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.claimhistory.view.ViewPEDHistory;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresmentDetailsTable;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndorsementDetails;
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.domain.MasterService;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.CellStyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class PEDRequsteTeamLeadTable extends GBaseTable<OldPedEndorsementDTO> {

public PEDRequsteTeamLeadTable() {
//super(PEDRequestDetailsTable.class);
//setUp();
}

private static final long serialVersionUID = 7031963170040209948L;

@EJB
private PEDQueryService pedService;

@EJB
private MasterService masterService;

@EJB
private PreauthService preauthService;

public SelectPEDRequestListener selectPEDRequestDetailsTable;

@Inject
private ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable;

@Inject
private ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService;

@Inject
private OldPedEndorsementDTO oldPedEndorsementDTO;

@Inject
private ViewPEDHistory viewPedHistory;

private Window popup;

@SuppressWarnings("unused")
 
private Instance<ViewPEDEndorsementDetails> viewDetailsInstance;

/*public static final Object[] NATURAL_COL_ORDER = new Object[] { "select",
	"pedSuggestionName", "pedName", "repudiationLetterDate", "remarks",
	"requestorId", "requestedDate", "requestStatus" };*/

/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
{
fieldMap.put("Select", new TableFieldDTO("select", OptionGroup.class,
		Boolean.class, true));
fieldMap.put("pedSuggestionName", new TableFieldDTO(
		"pedSuggestionName", TextField.class, String.class, false));
fieldMap.put("pedName", new TableFieldDTO("pedName", TextField.class,
		String.class, false));
fieldMap.put("repudiationLetterDate", new TableFieldDTO(
		"repudiationLetterDate", TextField.class, String.class, false));
fieldMap.put("remarks", new TableFieldDTO("remarks", TextField.class,
		String.class, false));
fieldMap.put("requestorId", new TableFieldDTO("requestorId",
		TextField.class, String.class, false));
fieldMap.put("requestedDate", new TableFieldDTO("requestedDate",
		TextField.class, String.class, false));
fieldMap.put("requestStatus", new TableFieldDTO("requestStatus",
		TextField.class, String.class, false));
fieldMap.put("viewDetails", new TableFieldDTO("viewDetails",
		Button.class, String.class, false));
}*/

@Override
public void removeRow() {
// TODO Auto-generated method stub

}

@Override
public void initTable() {
table.removeAllItems();
table.setWidth("100%");
table.setContainerDataSource(new BeanItemContainer<OldPedEndorsementDTO>(
		OldPedEndorsementDTO.class));
 Object[] NATURAL_COL_ORDER = new Object[] {
	"pedSuggestionName", "pedName", "repudiationLetterDate", "remarks",
	"requestorId", "requestedDate", "requestStatus" };
table.setVisibleColumns(NATURAL_COL_ORDER);
//table.setPageLength(table.size());
//table.setHeight("100px");
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
				oldPedEndorsementDTO = (OldPedEndorsementDTO)itemId;
				Long key = oldPedEndorsementDTO.getKey();
	    		ViewPEDEndorsementDetails viewDetailUI = new ViewPEDEndorsementDetails(
	    				pedService, masterService, preauthService,
	    			    viewPEDEndoresmentDetailsTable,
	     				viewPEDEndoresementDetailsService, key);
	    		viewDetailUI.initView(key,oldPedEndorsementDTO.getRequestStatus());
	    		UI.getCurrent().addWindow(viewDetailUI);
	        } 
	    });
    	
    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    	button.addStyleName(ValoTheme.BUTTON_LINK);
    	button.setWidth("150px");
    	return button;
      }
});

table.removeGeneratedColumn("editDetails");
table.addGeneratedColumn("editDetails", new Table.ColumnGenerator() {
      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
      public Object generateCell(final Table source, final Object itemId, Object columnId) {
		
		OldPedEndorsementDTO pedRecord = (OldPedEndorsementDTO)itemId;
		
		if(pedRecord.getKey().equals(pedRecord.getCurrentPED())){
		
	    	final Button button = new Button("Edit Details");
	    	button.addClickListener(new Button.ClickListener() {
		        /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					button.setEnabled(false);
					OldPedEndorsementDTO pedRequestDetailsProcessDTO= (OldPedEndorsementDTO)itemId;
					pedRequestDetailsProcessDTO.setIsEditPED(true);
					fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_EDIT_BUTTON, pedRequestDetailsProcessDTO);
					
		        } 
		    });
	    	
	    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
	    	button.setWidth("150px");
	    	return button;
	    	
		}else{
			return null;
		}
      }
});

table.removeGeneratedColumn("viewTrails");
table.addGeneratedColumn("viewTrails", new Table.ColumnGenerator() {
      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
      public Object generateCell(final Table source, final Object itemId, Object columnId) {
    	Button button = new Button("View Trails");
    	button.addClickListener(new Button.ClickListener() {
	        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				final OldPedEndorsementDTO oldPedEndorsementDTO = (OldPedEndorsementDTO)itemId;
					if (oldPedEndorsementDTO != null) {

						viewPedHistory.init(oldPedEndorsementDTO.getKey());
					   
						popup = new com.vaadin.ui.Window();
						popup.setCaption("View History");
						popup.setWidth("75%");
						popup.setHeight("75%");
						popup.setContent(viewPedHistory);
						popup.setClosable(true);
						popup.center();
						popup.setResizable(false);
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
					}else{
//						getErrorMessage("History is not available");
					}
					
	        } 
	    });
    	
    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
    	button.addStyleName(ValoTheme.BUTTON_LINK);
    	return button;
      }
});


//if(table.size()==1){
//table.setPageLength(table.size()+4);
//}
//else if(table.size()>5){
//	table.setPageLength(table.size()+4);
//}
table.setColumnHeader("viewTrails", "View Trails");
table.setHeight("150px");
table.setColumnHeader("editDetails", "Edit Details");


}

@Override
public void tableSelectHandler(OldPedEndorsementDTO t) {
      fireViewEvent(PEDRequestDetailsTeamLeadPresenter.PED_TL_SET_MODIFIED_DATA, t);
}

@Override
public String textBundlePrefixString() {
return "preauth-old-ped-approve-";
}

public void addListener(SelectPEDRequestListener listener) {
this.selectPEDRequestDetailsTable = listener;
}
public void setRowColor(Long key){
	ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds()); 
	final Object selectedRowId = getSelectedRowId(itemIds , key);
	System.out.print(";;;;;;;;;;;;;;;;;; selected id = "  + selectedRowId);
	

	
	table.setCellStyleGenerator(new CellStyleGenerator() {
		
		@Override
		public String getStyle(Table source, Object itemId, Object propertyId) {
			//table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
			
				  
		
			
		oldPedEndorsementDTO = (OldPedEndorsementDTO)selectedRowId;
			long key1 = oldPedEndorsementDTO.getKey();
			oldPedEndorsementDTO = (OldPedEndorsementDTO)itemId;
			long key2 = oldPedEndorsementDTO.getKey();
			if(key1==key2){
			return "select";
			}else{
				return "none";
			}
		
		}
		
		
	});
	
}

private Object getSelectedRowId( ArrayList<Object> ids,Long key){
	
	for(Object id:ids){
			oldPedEndorsementDTO = (OldPedEndorsementDTO)id;
		Long key1 = oldPedEndorsementDTO.getKey();
		if(key1.equals(key)){
		 return id;
		}
	}
	
	return null;
	
}

}

