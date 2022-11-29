package com.shaic.claim.reimbursement.rrc.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shaic.domain.RRCRequest;

public class RRCRequestRemoveDuplicate {
	
//	public static TreeSet<RRCRequest> getUniqueRecords(List<RRCRequest> rrcRequesetList) {

	public static List<RRCRequest> getUniqueRecords(List<RRCRequest> rrcRequesetList) {/*ReferenceTable.RRC_REQUEST_PROCESS_STATUS
		ReferenceTable.RRC_REQUEST_HOLD_STATUS
		ReferenceTable.RRC_REQUEST_REVIEWED_STATUS*/
		Map<String, RRCRequest> rrcRequestMap = new HashMap<String, RRCRequest>();
		for (RRCRequest rrcRequest : rrcRequesetList) {
			String rrcRequestNumber = rrcRequest.getRrcRequestNumber();
			if (rrcRequestMap.get(rrcRequestNumber) == null)
			{
				rrcRequestMap.put(rrcRequestNumber, rrcRequest);
			}
			else
			{
				if (rrcRequestMap.get(rrcRequestNumber).getStatus().getKey() < rrcRequest.getStatus().getKey())
				{
					rrcRequestMap.put(rrcRequestNumber, rrcRequest);
				}
			}
		}
		
		//TreeSet<RRCRequest> uniqueList = new TreeSet<RRCRequest>();
		List<RRCRequest> uniqueList = new ArrayList<RRCRequest>();
		for (String rrcRequestKey : rrcRequestMap.keySet()) {
			uniqueList.add(rrcRequestMap.get(rrcRequestKey));
		}
		return uniqueList;
	}
	
	public static List<RRCRequest> getRevisedUniqueRecords(List<RRCRequest> rrcRequestList){
		
		
		
		return null;
	}
	
	
	


}
