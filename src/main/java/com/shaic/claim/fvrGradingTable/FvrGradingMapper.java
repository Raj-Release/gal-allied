package com.shaic.claim.fvrGradingTable;

import java.util.ArrayList;
import java.util.List;

public class FvrGradingMapper {	

	public static List<FvrGradingDTO> getViewClaimHistoryDTO() {
		List<FvrGradingDTO> fvrGradingDTOList = new ArrayList<FvrGradingDTO>();
		FvrGradingDTO fvrGradingDTO1 = new FvrGradingDTO();
		fvrGradingDTO1.setCategory("Tariff Verified (Hospital / Room )");
		FvrGradingDTO fvrGradingDTO2 = new FvrGradingDTO();
		fvrGradingDTO2.setCategory("Trigger Points attended");
		FvrGradingDTO fvrGradingDTO3 = new FvrGradingDTO();
		fvrGradingDTO3.setCategory("Traced PED");
		FvrGradingDTO fvrGradingDTO4 = new FvrGradingDTO();
		fvrGradingDTO4.setCategory("Total Quantum Reduction Achieved");
		FvrGradingDTO fvrGradingDTO5 = new FvrGradingDTO();
		fvrGradingDTO5.setCategory("Terminated field Visit patient discharged");
		FvrGradingDTO fvrGradingDTO6 = new FvrGradingDTO();
		fvrGradingDTO6.setCategory("None of the above");
		fvrGradingDTOList.add(fvrGradingDTO1);
		fvrGradingDTOList.add(fvrGradingDTO2);
		fvrGradingDTOList.add(fvrGradingDTO3);
		fvrGradingDTOList.add(fvrGradingDTO4);
		fvrGradingDTOList.add(fvrGradingDTO5);
		fvrGradingDTOList.add(fvrGradingDTO6);		
		return fvrGradingDTOList;
	}

}