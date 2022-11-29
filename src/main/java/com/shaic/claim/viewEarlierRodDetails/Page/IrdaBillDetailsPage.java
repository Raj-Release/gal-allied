package com.shaic.claim.viewEarlierRodDetails.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.vaadin.addon.cdimvp.ViewComponent;

import com.shaic.claim.rod.wizard.service.CreateRODService;
import com.shaic.claim.viewEarlierRodDetails.Table.ViewIRDACategoryBillTable;
import com.shaic.claim.viewEarlierRodDetails.dto.HospitalisationDTO;
import com.shaic.domain.Hospitalisation;
import com.shaic.domain.MasIrdaLevel1;
import com.vaadin.ui.Panel;

public class IrdaBillDetailsPage extends ViewComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private ViewIRDACategoryBillTable viewBillDetailsTable;
	
	private Map<String, Object> referenceData = new HashMap<String, Object>();
	
	@EJB
	private CreateRODService billDetailsService;
	
	
	public void init(Long rodKey){
		
		viewBillDetailsTable.init();
		viewBillDetailsTable.setReferenceData(this.referenceData);
		
		List<MasIrdaLevel1> irdaMasterList = billDetailsService.getIrdaLevelOneList();
		
		List<HospitalisationDTO> mastersValue = new ArrayList<HospitalisationDTO>();
		
		int i =1;
		for (MasIrdaLevel1 masIrdaLevel1 : irdaMasterList) {
			
			HospitalisationDTO dto = new HospitalisationDTO();
			dto.setSno(i);
			dto.setIrdaCode(masIrdaLevel1.getIrdaCode().toString());
			dto.setCategory(masIrdaLevel1.getIrdaValue());
			
			mastersValue.add(dto);
			i++;
		}
		
		 List<Hospitalisation> hospitalizationList = billDetailsService.getHospitalisationList(rodKey);
		 
		 
		 
		 
		
         for (HospitalisationDTO hospitalisationDTO : mastersValue) {
        	 
        	 viewBillDetailsTable.addBeanToList(hospitalisationDTO);
			
		}
         
         
         Panel panel = new Panel(viewBillDetailsTable);
         panel.setCaption("View Category Wise Bill Details (IRDA )");
         panel.setStyleName("gridBorder");
         
         setCompositionRoot(panel);
	}
	

}
