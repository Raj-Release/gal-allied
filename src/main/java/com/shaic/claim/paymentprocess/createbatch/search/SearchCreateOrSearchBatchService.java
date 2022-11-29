/**
 * 
 */
package com.shaic.claim.paymentprocess.createbatch.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.shaic.arch.table.Page;
/**
 * @author ntv.narenj
 *
 */
@Stateless
public class SearchCreateOrSearchBatchService {

	public  Page<SearchCreateOrSearchBatchTableDTO> search(
			SearchCreateOrSearchBatchFormDTO searchFormDTO,
			String userName, String passWord) {
		
		
		List<SearchCreateOrSearchBatchTableDTO> result = new ArrayList<SearchCreateOrSearchBatchTableDTO>();
		SearchCreateOrSearchBatchTableDTO searchAcknowledgementDocumentReceiverTableDTO = new SearchCreateOrSearchBatchTableDTO();
		searchAcknowledgementDocumentReceiverTableDTO.setIntimationNo("12213");
		searchAcknowledgementDocumentReceiverTableDTO.setClaimNo("45645");
		searchAcknowledgementDocumentReceiverTableDTO.setCpuCode("4564");
		searchAcknowledgementDocumentReceiverTableDTO.setDateOfAdmission(new Date());
		searchAcknowledgementDocumentReceiverTableDTO.setHospitalAddress("tryrty");
		searchAcknowledgementDocumentReceiverTableDTO.setHospitalCity("tytry");
		searchAcknowledgementDocumentReceiverTableDTO.setHospitalName("tytry");
		searchAcknowledgementDocumentReceiverTableDTO.setInsuredPatiendName("yttr");
		searchAcknowledgementDocumentReceiverTableDTO.setPolicyNo("yty");
		searchAcknowledgementDocumentReceiverTableDTO.setReasonForAdmission("tty");
		result.add(searchAcknowledgementDocumentReceiverTableDTO);
		Page<SearchCreateOrSearchBatchTableDTO> page = new Page<SearchCreateOrSearchBatchTableDTO>();
		
		
		page.setPageItems(result);
		
	
		return page;
	}

}
