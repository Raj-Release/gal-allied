/**
 * 
 */
package com.shaic.reimbursement.paymentprocess.processpaymentreturn.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.shaic.arch.table.Page;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchProcessPaymentReturnsService {

	public  Page<SearchProcessPaymentReturnsTableDTO> search(
			SearchProcessPaymentReturnsFormDTO searchFormDTO,
			String userName, String passWord) {
		
		
		List<SearchProcessPaymentReturnsTableDTO> result = new ArrayList<SearchProcessPaymentReturnsTableDTO>();
		SearchProcessPaymentReturnsTableDTO searchAcknowledgementDocumentReceiverTableDTO = new SearchProcessPaymentReturnsTableDTO();
		searchAcknowledgementDocumentReceiverTableDTO.setIntimationNo("12213");
		searchAcknowledgementDocumentReceiverTableDTO.setClaimNo("45645");
		searchAcknowledgementDocumentReceiverTableDTO.setCpuCode("4564");
		searchAcknowledgementDocumentReceiverTableDTO.setInsuredPatiendName("yttr");
		searchAcknowledgementDocumentReceiverTableDTO.setPolicyNo("yty");
		result.add(searchAcknowledgementDocumentReceiverTableDTO);
		Page<SearchProcessPaymentReturnsTableDTO> page = new Page<SearchProcessPaymentReturnsTableDTO>();
		
		
		page.setPageItems(result);
		page.setIsDbSearch(true);
		
	
		return page;
	}

}
