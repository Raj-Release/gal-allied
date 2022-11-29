package com.shaic.claim.omp.ratechange;

import java.util.Map;

import javax.ejb.EJB;

import com.shaic.arch.table.GBaseTable;
import com.shaic.claim.OMPProcessOmpClaimProcessor.pages.OMPClaimProcessorDTO;
import com.shaic.domain.omp.OMPIntimationService;
import com.vaadin.v7.data.util.BeanItemContainer;

public class OMPViewEndorsementDetailTable extends GBaseTable<OMPClaimProcessorDTO>{
	
	@EJB
	private OMPIntimationService intimationService;
	
	private OMPClaimProcessorDTO bean;
	
	private Map<String, Object> referenceData;
	
	/*public static final Object[] NATURAL_COL_ORDER = new Object[] {"slno","endorsementNo","passedDate","effectiveFromDt","effectiveToDt","endorsementCode","endorsementDescription"};*/


	/*public static final Object[] NATURAL_HCOL_ORDER = new Object[] {"endorsementNo","passedDate","effectiveFromDt","effectiveToDt","endorsementType","endorsementText","endorsementSumInsured","revisedSumInsured","endorsementPremium"};*/
	@Override
	public void removeRow() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void initTable() {
		table.removeAllItems();
		table.setWidth("100%");
		table.setContainerDataSource(new BeanItemContainer<OMPClaimProcessorDTO>(OMPClaimProcessorDTO.class));
		Object[] NATURAL_COL_ORDER = new Object[] {"slno","endorsementNo","passedDate","effectiveFromDt","effectiveToDt","endorsementCode","endorsementDescription"};
		table.setVisibleColumns(NATURAL_COL_ORDER);
		table.setPageLength(table.size() + 4);
		table.setHeight("200px");
		
	}
	
	public void viewPolicyEndose() {
		
		Object[] NATURAL_HCOL_ORDER = new Object[] {"endorsementNo","passedDate","effectiveFromDt","effectiveToDt","endorsementType","endorsementText","endorsementSumInsured","revisedSumInsured","endorsementPremium"};
		table.setVisibleColumns(NATURAL_HCOL_ORDER);
		
	}
	@Override
	public void tableSelectHandler(OMPClaimProcessorDTO t) {
		// TODO Auto-generated method stub
		
	}
	
	public void setReferenceData(Map<String, Object> referenceData) {
		this.referenceData = referenceData;
	}

	@Override
	public String textBundlePrefixString() {
		
		return "ompviewendorsement-details-";
	}
	

}
