package com.shaic.claim.pcc.hrmp;

import javax.ejb.EJB;
import javax.inject.Inject;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.ClaimDto;
import com.shaic.claim.ClaimMapper;
import com.shaic.domain.Intimation;
import com.shaic.ims.carousel.RevisedCarousel;
import com.shaic.main.navigator.ui.MenuPresenter;
import com.shaic.newcode.wizard.domain.NewIntimationMapper;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.VerticalLayout;

/**
 * @author GokulPrasath.A
 *
 */
public class SearchHRMPTable extends GBaseTable<SearchHRMPTableDTO>{
	
	private final static Object[] VISIBLE_COL_ORDER = new Object[]{
		"serialNumber", "intimationNo","referenceNo", "dateOfAdmission","cpuCode",
		"hospitalCode", "hospitalType", "status", "ageing" }; 
	
	@EJB
	private SearchHRMPService hrmService;
	
	@Inject
	private RevisedCarousel preauthIntimationDetailsCarousel;
	
	private VerticalLayout mainLayout;

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
		table.removeAllItems();
		
	}

	@Override
	public void initTable() {
		
		table.setContainerDataSource(new BeanItemContainer<SearchHRMPTableDTO>(
				SearchHRMPTableDTO.class));
		table.setVisibleColumns(VISIBLE_COL_ORDER);
		table.setHeight("335px");
		
	}
	
	 protected void tablesize(){
			table.setPageLength(table.size()+1);
			int length =table.getPageLength();
			if(length>=10){
				table.setPageLength(10);
			}
			
		}

	 @Override
		public void tableSelectHandler(SearchHRMPTableDTO t) {
		// TODO Auto-generated method stub
		 
		 fireViewEvent(MenuPresenter.HRMP_WIZARD, t);
		 //setCompleteLayout(t);
		 
	 }

			
		@Override
		public String textBundlePrefixString() {
		return "search-divisionhead-processing-";
		}
		
		public void setCompleteLayout(SearchHRMPTableDTO t) {
			
			Intimation intimation = hrmService.getIntimationByNo(t.getIntimationNo());
			ClaimMapper claimMapper = ClaimMapper.getInstance();
			
			ClaimDto claimDto = claimMapper.getClaimDto(hrmService.getClaimByIntimation(intimation.getKey()));
			
			NewIntimationDto newIntimationDto = NewIntimationMapper.getInstance()
					.getNewIntimationDto(intimation);
			
			preauthIntimationDetailsCarousel.init(newIntimationDto, claimDto , "Long Stay Claims");
			
			mainLayout = new VerticalLayout(preauthIntimationDetailsCarousel);
			mainLayout.setSpacing(true);
			
			setCompositionRoot(mainLayout);
		}
}
