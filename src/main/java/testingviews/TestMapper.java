package testingviews;

import java.util.ArrayList;
import java.util.List;

public class TestMapper {

	public static  List<TestTableDTO> getSearchProcessTranslationTableDTO()
	{
		List<TestTableDTO> list = new ArrayList<TestTableDTO>();
		for(int i =1; i <=10; i++)
		{
			TestTableDTO item = new TestTableDTO();
			item.setClaimNo("Claim Nio" + i);
			item.setInsuredPatientName("insuredPatientName");
			item.setIntimationNo("intimationNo" + i);		
			item.setPolicyNo("policyNo" + i);
			item.setLob("lob");
			item.setProductCode("productCode");
			item.setProductName("productName");
			item.setSno(i);
			
			list.add(item);
		}
		return list;
	}
	
}
