/**
 * 
 */
package com.shaic.claim.fss.filedetail;

import java.util.Map;

import org.vaadin.teemu.wizards.GWizard;
import org.vaadin.teemu.wizards.WizardStep;

import com.shaic.arch.GMVPView;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.PDIntimationPremia;

/**
 * 
 *
 */
public interface ProcessDataEntryView extends GMVPView,WizardStep<SearchDataEntryTableDTO> {
	
	 void init(SearchDataEntryTableDTO bean, GWizard wizard);

	void setTableValuesToDTO();
	
	void setUpListenerContainer(Map<String, Object> containerMap);
	
	void setPremiaIntimation(PDIntimationPremia intimation);

}
