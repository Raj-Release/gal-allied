package com.shaic.claim.pedrequest.process;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.claimhistory.view.ViewPEDHistory;
import com.shaic.claim.pedquery.PEDQueryService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresementDetailsService;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndoresmentDetailsTable;
import com.shaic.claim.pedquery.viewPedDetails.ViewPEDEndorsementDetails;
import com.shaic.claim.pedrequest.SelectPEDRequestListener;
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

public class PEDRequestProcessTable extends GBaseTable<PEDRequestDetailsProcessDTO> {

	public PEDRequestProcessTable() {
//		super(PEDRequestProcessTable.class);
//		setUp();
	}


	private static final long serialVersionUID = 7031963170040209948L;

	@EJB
	private PEDQueryService pedService;

	@EJB
	private MasterService masterService;

	@EJB
	private PreauthService preauthService;
	
	@Inject
	private PEDRequestDetailsProcessDTO pedRequestDetailsProcessDTO;
	
	

	public SelectPEDRequestListener selectPEDRequestDetailsTable;

	@Inject
	private ViewPEDEndoresmentDetailsTable viewPEDEndoresmentDetailsTable;

	@Inject
	private ViewPEDEndoresementDetailsService viewPEDEndoresementDetailsService;
	
	@Inject
	private ViewPEDHistory viewPedHistory;
	
	private Window popup;

	/*@Inject
	private Instance<ViewPEDEndorsementDetails> viewDetailsInstance;*/

	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {
			"pedSuggestionName", "nameofPED", "repudiationLetterDate", "remarks",
			"requestorId", "requestedDate", "requestStatus"};*/
	
	/*public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();
	
	 {
		 
		fieldMap.put("Select", new TableFieldDTO("select", OptionGroup.class, Boolean.class, true));
		fieldMap.put("pedSuggestionName", new TableFieldDTO("pedSuggestionName",
				TextField.class, String.class, false));
		fieldMap.put("nameofPED", new TableFieldDTO("nameofPED",
				TextField.class, String.class, false));
		fieldMap.put("repudiationLetterDate", new TableFieldDTO("repudiationLetterDate",
				TextField.class, String.class, false));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextField.class, String.class, false));
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
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<PEDRequestDetailsProcessDTO>(
				PEDRequestDetailsProcessDTO.class));
		 Object[] NATURAL_COL_ORDER = new Object[] {
			"pedSuggestionName", "nameofPED", "repudiationLetterDate", "remarks",
			"requestorId", "requestedDate", "requestStatus"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
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
						pedRequestDetailsProcessDTO= (PEDRequestDetailsProcessDTO)itemId;
						Long key = pedRequestDetailsProcessDTO.getKey();
			    		ViewPEDEndorsementDetails viewDetailUI = new ViewPEDEndorsementDetails(
			    				pedService, masterService, preauthService,
			    			    viewPEDEndoresmentDetailsTable,
			     				viewPEDEndoresementDetailsService, key);
			    		viewDetailUI.initView(key,pedRequestDetailsProcessDTO.getOldPedEndorsementDTO().getRequestStatus());
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
				
				PEDRequestDetailsProcessDTO pedRecord = (PEDRequestDetailsProcessDTO)itemId;
				
				if(pedRecord.getKey().equals(pedRecord.getCurrentPED())){
				
			    	final Button button = new Button("Edit Details");
			    	button.addClickListener(new Button.ClickListener() {
				        /**
						 * 
						 */
						private static final long serialVersionUID = 1L;
	
						public void buttonClick(ClickEvent event) {
							button.setEnabled(false);
							PEDRequestDetailsProcessDTO pedRequestDetailsProcessDTO= (PEDRequestDetailsProcessDTO)itemId;
							pedRequestDetailsProcessDTO.setIsEditPED(true);
							fireViewEvent(PEDRequestDetailsProcessPresenter.EDIT_BUTTON, pedRequestDetailsProcessDTO);
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
						final PEDRequestDetailsProcessDTO oldPedEndorsementDTO = (PEDRequestDetailsProcessDTO)itemId;
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
//								getErrorMessage("History is not available");
							}
							
			        } 
			    });
		    	
		    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		    	button.addStyleName(ValoTheme.BUTTON_LINK);
		    	return button;
		      }
		});
		

		table.setColumnHeader("viewTrails", "View Trails");
		table.setHeight("150px");
		table.setColumnHeader("editDetails", "Edit Details");

	}

	@Override
	public void tableSelectHandler(PEDRequestDetailsProcessDTO t) {
//		PEDRequestDetailsProcessDTO = (PEDRequestDetailsProcessDTO)itemId;
//		Long key = PEDRequestDetailsProcessDTO.getKey();
//		ViewPEDEndorsementDetails viewDetailUI = new ViewPEDEndorsementDetails(
//				pedService, masterService, preauthService,
//			    viewPEDEndoresmentDetailsTable,
// 				viewPEDEndoresementDetailsService, key);
//		viewDetailUI.initView(key);
//		UI.getCurrent().addWindow(viewDetailUI);
	}

	@Override
	public String textBundlePrefixString() {
		return "preauth-old-ped-endorsement-";
	}

	public void addListener(SelectPEDRequestListener listener) {
		this.selectPEDRequestDetailsTable = listener;
	}
//	@Override
//	protected void newRowAdded() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	protected Map<String, TableFieldDTO> getFiledMapping() {
//		return fieldMap;
//	}
//
//	@Override
//	public void deleteRow(Object itemId) {
//		// TODO Auto-generated method stub
//		
//	}
	public void setRowColor(Long key){
		ArrayList<Object> itemIds = new ArrayList<Object>(table.getItemIds()); 
		final Object selectedRowId = getSelectedRowId(itemIds , key);
		System.out.print(";;;;;;;;;;;;;;;;;; selected id = "  + selectedRowId);
		

		
		table.setCellStyleGenerator(new CellStyleGenerator() {
			
		
			private static final long serialVersionUID = 1L;

			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				//table.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
				
					  
			
				
				pedRequestDetailsProcessDTO = (PEDRequestDetailsProcessDTO)selectedRowId;
				long key1 = pedRequestDetailsProcessDTO.getKey();
				pedRequestDetailsProcessDTO = (PEDRequestDetailsProcessDTO)itemId;
				long key2 = pedRequestDetailsProcessDTO.getKey();
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
			pedRequestDetailsProcessDTO = (PEDRequestDetailsProcessDTO)id;
			Long key1 = pedRequestDetailsProcessDTO.getKey();
			if(key1.equals(key)){
			 return id;
			}
		}
		
		return null;
		
	}
	
	


}
