package com.shaic.claim.premedical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.selecthospital.ui.HospitalMapper;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.State;
import com.shaic.domain.service.PreMedicalService;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.filter.SimpleStringFilter;
import com.vaadin.v7.data.util.filter.UnsupportedFilterException;

public class CustomLazyContainer extends IndexedContainer {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -464586163849994639L;
	private int subStringSize;
	private  String propertyId;
	private String querySubString;
	private String methodToInvoke;
	private State selectedState;
	private CityTownVillage selectedCity;
//	private Locality selectedArea;
	
	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB 
	private PreMedicalService premedicalService;

	public CustomLazyContainer(int prefixSize, String propertyId, MasterService masterService, String methodToInvoke){
	   this.subStringSize = prefixSize;
	   this.propertyId = "value";
	   this.masterService = masterService;
	   this.methodToInvoke = methodToInvoke;
	} 
	
	public CustomLazyContainer(int prefixSize, String propertyId, MasterService masterService, PreMedicalService premedicalService ,String methodToInvoke){
		   this.subStringSize = prefixSize;
		   this.propertyId = "value";
		   this.masterService = masterService;
		   this.methodToInvoke = methodToInvoke;
		   this.premedicalService = premedicalService;
		} 
	
	public CustomLazyContainer(int prefixSize, String propertyId, HospitalService hospitalService, String methodToInvoke, State state,CityTownVillage city/*,Locality locality*/){
	   this.subStringSize = prefixSize;
	   this.propertyId = "value";
	   this.hospitalService = hospitalService;
	   this.methodToInvoke = methodToInvoke;
	   this.selectedState = state;
	   this.selectedCity = city;
//	   this.selectedArea = locality;		   
	}
	
	
	
	/*public CustomLazyContainer(int prefixSize, String propertyId, String methodToInvoke,MasterService masterService){
		   this.subStringSize = prefixSize;
		   this.propertyId = "value";
		   this.masterService = masterService;
		   this.methodToInvoke = methodToInvoke;
		   invokeContainerFilter();
		} */
	
	public void invokeContainerFilter(String strDiag)
	{
		Object obj = (Object)"value";
		SimpleStringFilter filter = new SimpleStringFilter(obj,strDiag,false,false);
		addContainerFilter(filter);
	}
	
	public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
	   //Check to see if the filter is equal to null
	   if (filter == null) {
	      //remove the items
	      removeAllItems();
	      querySubString = null;
	   } else{
	      //remove the items
	      removeAllItems();
	      //check to see what type of filter we have
	      if (filter instanceof SimpleStringFilter) {
	      //get the query subString from the filter
	      String newFilterString = ((SimpleStringFilter) filter).getFilterString();
	      //check to make sure the substring isn't null and its not equal to the last one
	      if (newFilterString != null || !(newFilterString.equals(querySubString))){
	         //set the sub string
	         querySubString = newFilterString;
	            //check the size on the sub string
	           if(querySubString.length() >= subStringSize){
	              //get the results
	              queryDatabase();
	              //add the filter
	              super.addContainerFilter(filter);
	           } //end if the substring langth is long enough
	        }// end if newFilterString doesnt equal null and newFilterString  doesnt equal last value
	     } // end if and instance of Simple Filter
	  } //end else
	} // end addCoontainerFilter metho
	@SuppressWarnings("unchecked")
	private void queryDatabase() {

	   //Query the database here with the code you want
	   //Store it how ever you want this example uses a list to demonstrate how to get the data to display
		List<SelectValue> selectedValues = new ArrayList<SelectValue>();
		if(methodToInvoke.equalsIgnoreCase("diagnosis")) {
			//premedicalService.saveMasterDiagnosis(querySubString);
			selectedValues = masterService.getDiagnosisSelctValuesList(querySubString);
		
			if(null != selectedValues && !selectedValues.isEmpty())
			{
			for(int i  =0; i<selectedValues.size(); i++) {
				SelectValue selectValue = selectedValues.get(i);
				Item addItem = addItem(selectValue.getId());
				//try
				//{
					this.getContainerProperty(selectValue.getId(), propertyId).setValue(selectValue);
					//);
				//}
				
			}
			}
			/*else
			{
				SelectValue selectValue = premedicalService.saveMasterDiagnosis(querySubString);
				//selectedValues = masterService.getDiagnosisSelctValuesList(querySubString);
				//for(int i  =0; i<selectedValues.size(); i++) {
					//SelectValue selectValue = selectedValues.get(i);
					
					Item addItem = addItem(selectValue.getId());
					this.getContainerProperty(selectValue.getId(), propertyId).setValue(selectValue);
				}*/
			}
		
		else if(methodToInvoke.equalsIgnoreCase("hospital")) {
			List<Hospitals> selectedHospitalValues = hospitalService.hospitalNameCriteriaSearch(querySubString.toLowerCase(), selectedState,
					selectedCity/*, selectedArea*/);
			List<HospitalDto> selectedHospitalDtoValues = new ArrayList<HospitalDto>();
			if(!selectedHospitalValues.isEmpty()){
			
				for(Hospitals hospital : selectedHospitalValues)
				selectedHospitalDtoValues.add(new HospitalMapper().getHospitalDTO(hospital));				
				
			}
			int i = 0;
			   Iterator iterDataList = selectedHospitalDtoValues.iterator();
			   while (iterDataList.hasNext()) {
			      addItem(i);
			      this.getContainerProperty(i, propertyId).setValue(((Hospitals)iterDataList.next()));
			      i++;
		
		        }
			
		}
		
//		arrayList.add("Saravana");
//		arrayList.add("Lakshmi");
//		
//	   List<String> dataList = arrayList;

	   //add the results to the container	
	   
	    //end while iter has next

	}// end queryDataBase method
}
