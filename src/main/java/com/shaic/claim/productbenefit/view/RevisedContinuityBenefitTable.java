package com.shaic.claim.productbenefit.view;

import java.util.List;

import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.domain.PreauthService;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.themes.ValoTheme;

public class RevisedContinuityBenefitTable extends GBaseTable<ContinuityBenefitDTO>{
	private static final long serialVersionUID = 7031963170040209948L;


	private Window popup;

	@Inject
	private ViewPortablityDetails viewPortablityDetails;



	@SuppressWarnings("unused")
	@Override
	public void removeRow() {
		table.removeAllItems();

	}
	private PreauthService preauthService_;

	public void setServices(PreauthService preauthService){
		preauthService_ = preauthService;
	}

	@Override
	public void initTable() {
	table.removeAllItems();
	table.setContainerDataSource(new BeanItemContainer<ContinuityBenefitDTO>(
			ContinuityBenefitDTO.class));
	table.removeGeneratedColumn("viewDetails");
	table.addGeneratedColumn("viewDetails", new Table.ColumnGenerator() {
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

					final ContinuityBenefitDTO tableDto = (ContinuityBenefitDTO) itemId;
//					getPortablityDetails(tableDto);
				//	getPortablityDetails(tableDto.getInsuredName(),tableDto.getCurrentPolicyNo());
					
				} 
		    });
	    	
	    	button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
	    	button.setWidth("150px");
	    	button.addStyleName(ValoTheme.BUTTON_LINK);
	    	return button;
	      }
	});


	 Object[] NATURAL_COL_ORDER = new Object[] {
			 "serialNumber", "waiver30Days", "exclusionYear1Str",
			 "exclusionYear2Str", "PEDwaiver", "policyStrFrmDate", "policyStrToDate", "policyNo",
			 };
	 
	table.setVisibleColumns(NATURAL_COL_ORDER);


	table.setHeight("200px");
	table.setWidth("100%");
	table.setPageLength(table.size()+1);
	table.setColumnHeader("viewDetails", "View Details");

	//table.setPageLength(table.size());
	}

	@Override
	public void tableSelectHandler(ContinuityBenefitDTO t) {
	//TODO:
	}


	public void getPortablityDetails(Long policyKey,Long insuredKey){
		List<ContinuityBenefitDTO> portabilityListdto =	preauthService_.getContinuityBenefitDetails(policyKey, insuredKey);
	if(portabilityListdto != null && !portabilityListdto.isEmpty())
		
	//public void getPortablityDetails(PortablitiyPolicyDTO dto) {
//		viewPortablityDetails.init(dto);

	//	viewPortablityDetails.init(portabilityListdto.get(0));
		popup = new com.vaadin.ui.Window();
		popup.setCaption("Continuity Benefit Details");
		popup.setWidth("60%");
		popup.setHeight("75%");
		popup.setContent(viewPortablityDetails);
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
	}

		@Override
		public String textBundlePrefixString() {
			return "portablity-policy-";
		}
}
