/**
 * 
 */
package com.shaic.claim.reimbursement.createandsearchlot;

import com.shaic.arch.GMVPView;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;

/**
 * @author ntv.vijayar
 *
 */
public interface EditPaymentDetails  extends GMVPView {
	
	void setUpIFSCDetails(ViewSearchCriteriaTableDTO viewSearchCriteriaDTO,EditPaymentDetailsView view);


}
