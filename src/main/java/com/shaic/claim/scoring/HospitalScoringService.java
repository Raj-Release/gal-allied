package com.shaic.claim.scoring;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.arch.SHAConstants;
import com.shaic.domain.Claim;
import com.shaic.domain.HospitalCategory;
import com.shaic.arch.SHAUtils;
import com.shaic.domain.HospitalScoring;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.RawCategory;
import com.shaic.domain.RawInvsDetails;
import com.shaic.domain.RawInvsHeaderDetails;
import com.shaic.domain.RawSubCategory;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Reimbursement;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
public class HospitalScoringService {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	private boolean isScoringEnable = false;
	
	private final Integer SCORING_VERSION = 5;
	
	private final Integer OLD_SCORING_VERSION = 4;
	

	public boolean isScoringEnable() {
		return isScoringEnable;
	}

	public void setScoringEnable(boolean isScoringEnable) {
		this.isScoringEnable = isScoringEnable;
	}
	
	// replcing scoringMapValues with boolean flag variables.
	/*Map<String, Object> scoringMapValues = new HashMap<String, Object>();
	
	public Map<String, Object> getScoringMapValues() {
		return this.scoringMapValues;
	}

	public void setScoringMapValues(Map<String, Object> scoringMapValues) {
		this.scoringMapValues = scoringMapValues;
	}*/
	
	private boolean isSeriousDeficiencyMarked = false;
	private boolean isModerateDeficiencyMarked = false;
	
	public boolean isSeriousDeficiencyMarked() {
		return this.isSeriousDeficiencyMarked;
	}

	public void setSeriousDeficiencyMarked(boolean isSeriousDeficiencyMarked) {
		this.isSeriousDeficiencyMarked = isSeriousDeficiencyMarked;
	}

	public boolean isModerateDeficiencyMarked() {
		return this.isModerateDeficiencyMarked;
	}

	public void setModerateDeficiencyMarked(boolean isModerateDeficiencyMarked) {
		this.isModerateDeficiencyMarked = isModerateDeficiencyMarked;
	}

	public List<HospitalScoringDTO> populateScoringCategory(String argIntimationNumber,String networkTypeID){
		List<HospitalScoringDTO> categoryList = new ArrayList<HospitalScoringDTO>();
		categoryList = getScoringCategories(argIntimationNumber,networkTypeID);
		assignDataForCategory(categoryList, argIntimationNumber);
		
		return categoryList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<HospitalScoringDTO> getScoringCategories(String argIntimationNumber,String networkTypeID){
//		Intimation selectedIntimation  = getIntimationByNo(argIntimationNumber);
		
		List<HospitalScoringDTO> scoringDataList = new ArrayList<HospitalScoringDTO>();
		List<String> distinctCategory = new ArrayList<String>();
		//R1292
		/*List<String> clmTypList = new ArrayList<String>();
		if(selectedIntimation.getClaimType().getKey().intValue() == ReferenceTable.CLAIM_TYPE_CASHLESS_ID){
			clmTypList.add("BOTH");
			clmTypList.add("CASHLESS");
		}else{
			clmTypList.add("BOTH");
		}*/
		// Taking Category
		Query catquery = entityManager.createNamedQuery("HospitalCategory.findDistinctCategory");
		catquery = catquery.setParameter("networkTypeID", networkTypeID);
		List<HospitalCategory> catlist  = catquery.getResultList();
		for(HospitalCategory catRec : catlist){
			if(!distinctCategory.contains(catRec.getCategoryDesc())){
				distinctCategory.add(catRec.getCategoryDesc());
			}	
		}
		
		// Taking SubCategory
		int category_Id = 7;
		int sub_category_Id = 0;
		for(int j = 0; j < distinctCategory.size(); j++){
//		for(String catName : distinctCategory){
			String catName = distinctCategory.get(j).trim();
			Query subCatquery = entityManager.createNamedQuery("HospitalCategory.findByCategory");
			subCatquery = subCatquery.setParameter("categoryDesc", catName);
			subCatquery = subCatquery.setParameter("networkTypeID", networkTypeID);
			List<HospitalCategory> tempQueryList  = subCatquery.getResultList();
			
			HospitalScoringDTO categoryRec = new HospitalScoringDTO();
			categoryRec.setScoringName(catName);
			categoryRec.setScoringValue(null);
			categoryRec.setScoringBooleanValue(null);
			//R1292
			categoryRec.setActualCategoryId(category_Id);
			categoryRec.setActualSubCategoryId(null);
//			if(categoryRec.getActualCategoryId() == 2 || categoryRec.getActualCategoryId() == 3){
			if(categoryRec.getActualCategoryId() == 8){
				categoryRec.setScoringValue("N");
				categoryRec.setScoringBooleanValue(false);
				categoryRec.setOptionVisible(false);
			}else{
				categoryRec.setOptionVisible(true);
			}
			categoryRec.setSubCategoryKey(Long.parseLong((String.valueOf(category_Id))));
			categoryRec.setTextFieldStyleName("tfwbBold");
			categoryRec.setComponentId(category_Id+"_"+sub_category_Id);
			categoryRec.setOptionEnabled(true);
			scoringDataList.add(categoryRec);			
			sub_category_Id++;
			for (HospitalCategory rec : tempQueryList) {
				HospitalScoringDTO subCategoryRec = new HospitalScoringDTO();
				subCategoryRec.setScoringName(rec.getSubCategoryDesc().trim());
				subCategoryRec.setScoringValue(null);
				subCategoryRec.setScoringBooleanValue(null);
				subCategoryRec.setOptionVisible(true);
				subCategoryRec.setSubCategoryKey(rec.getSubCategoryKey());
				subCategoryRec.setTextFieldStyleName("tfwb");
				subCategoryRec.setComponentId(category_Id+"_"+sub_category_Id);
				//R1292
				subCategoryRec.setActualCategoryId(rec.getCategoryKey().intValue());
				subCategoryRec.setActualSubCategoryId(rec.getSubCategoryKey().intValue());
				
				List<Integer> NWEnabledList =  new ArrayList<Integer>();
				NWEnabledList.add(1004); //("ANH Package – For ANH");
				NWEnabledList.add(1006); //("Index Price/SOC/Market Rate  - For NANH");
				
				List<Integer> R3SCatList =  new ArrayList<Integer>();
//				R3SCatList.add("Professional charges");
//				R3SCatList.add("OT consumables");
//				R3SCatList.add("OT Medicines");
//				R3SCatList.add("Implant");
				R3SCatList.add(1007);
				R3SCatList.add(1023);
				R3SCatList.add(1010);
				
				// To be Removed - Start
				/*if(rec.getSubCategoryDesc().trim().equals("ANH Package – For ANH") && selectedIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
					subCategoryRec.setOptionEnabled(true); // Making enabled if it Network Hospital
				}else if(rec.getSubCategoryDesc().trim().equals("ANH Package – For ANH") && selectedIntimation.getHospitalType().getKey() ==  ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID){
					subCategoryRec.setOptionEnabled(false); // Making disable if it NON - Network Hospital
				}else{
					// Nothing to do.....
				}
				
				if(rec.getSubCategoryDesc().trim().equals("Index Price/SOC/Market Rate  - For NANH") && selectedIntimation.getHospitalType().getKey() ==  ReferenceTable.NON_NETWORK_HOSPITAL_TYPE_ID){
					subCategoryRec.setOptionEnabled(true); // Making enabled if it NON - Network Hospital
				}else if(rec.getSubCategoryDesc().trim().equals("Index Price/SOC/Market Rate  - For NANH") && selectedIntimation.getHospitalType().getKey() ==  ReferenceTable.NETWORK_HOSPITAL_TYPE_ID){
					subCategoryRec.setOptionEnabled(false); // Making disable if it Network Hospital
				}else{
					// Nothing to do.....
				}*/
				// To be Removed - End
				
//				if(rec.getSubCategoryDesc().trim().equals("If Violation of ANH/Excess of Index Price ,Justifiable")){
				if(rec.getSubCategoryKey().intValue() == 1006){
					subCategoryRec.setOptionEnabled(false);
				}else if(!NWEnabledList.contains(rec.getSubCategoryKey())){
					if(R3SCatList.contains(rec.getSubCategoryKey().intValue())){
						subCategoryRec.setOptionEnabled(false); // disabling all 3rd hdr sub-categories radio-box
					}else{
						subCategoryRec.setOptionEnabled(true); // enabling all other sub-categories radio-box
					}
				}else{
					// Nothing to do.....
				}
				
				scoringDataList.add(subCategoryRec);	
				sub_category_Id++;
			}
			category_Id++;
			sub_category_Id = 0;
		}
		return scoringDataList;
	}
	
	@SuppressWarnings("unchecked")
	public void assignDataForCategory(List<HospitalScoringDTO> argList, String argIntimationNumber){
		Intimation selectedIntimation  = getIntimationByNo(argIntimationNumber);

		Query scoringquery = entityManager.createNamedQuery("HospitalScoring.findByScoringVersionIntimationKey");
		scoringquery = scoringquery.setParameter("intimationKey", selectedIntimation.getKey());
		scoringquery = scoringquery.setParameter("scoringVersion", SCORING_VERSION);
		List<HospitalScoring> catScorlist  = scoringquery.getResultList();
		List<Integer> listOfResetSc = new ArrayList<Integer>();
		if (!catScorlist.isEmpty()) {
			Map<String, Boolean> categoryFlag = new HashMap<String, Boolean>();
			Map<String, HospitalScoringDTO> categoryRec = new HashMap<String, HospitalScoringDTO>();
			
			for(HospitalScoringDTO dtoRec : argList){
				if(dtoRec.getSubCategoryKey() != null){
					HospitalScoring scoRec = getHospitalScoringBySkey(selectedIntimation.getKey(), dtoRec.getSubCategoryKey());
					/*System.out.println("DTO Val "+dtoRec.getSubCategoryKey().intValue());
					System.out.println("REC Val "+scoRec);
					if(dtoRec != null && scoRec != null){
						boolean isValueResetted = false;
						if(scoRec.getScoringVersion() == null){
							listOfResetSc.add(1004);
							listOfResetSc.add(1006);
							listOfResetSc.add(1007);
							listOfResetSc.add(1010);
							//setting value for 2 and 3 sub category line items for version null i.e first version of HS.
							if(listOfResetSc.contains(dtoRec.getSubCategoryKey().intValue())){
								dtoRec.setScoringValue(null);
								dtoRec.setScoringBooleanValue(null);
								categoryFlag.put(dtoRec.getComponentId(), false);
								isValueResetted = true;
							}
						}
						if(!isValueResetted && dtoRec.getSubCategoryKey().intValue() == scoRec.getSubCategoryKey().intValue()){*/
					if(scoRec != null) {
						if(dtoRec.getSubCategoryKey().intValue() == scoRec.getSubCategoryKey().intValue()){
//							System.out.println("SC "+dtoRec.getSubCategoryKey().intValue());
							dtoRec.setScoringValue(scoRec.getGradeScore());
							if(scoRec.getGradeScore() == null){ // This condition is not possible...... So this is not throwing error.....
								dtoRec.setScoringBooleanValue(null);
								categoryFlag.put(dtoRec.getComponentId(), null);
							}else if(scoRec.getGradeScore().equals("Y")){
								dtoRec.setScoringBooleanValue(true);
								categoryFlag.put(dtoRec.getComponentId(), true);
							}else{
								dtoRec.setScoringBooleanValue(false);
								categoryFlag.put(dtoRec.getComponentId(), false);
							}
							categoryRec.put(dtoRec.getComponentId(), dtoRec);
						}
					} else {
						dtoRec.setScoringBooleanValue(null);
						categoryRec.put(dtoRec.getComponentId(), dtoRec);
					}
					/*}else{
//						System.out.println("SC else "+dtoRec.getSubCategoryKey().intValue());
						// set value for the newly introduced line items and value is set based on the header selection i.e category yes / no value.....
						if(categoryFlag.containsKey("1_0")){
							if(categoryFlag.get("1_0")){
								dtoRec.setScoringValue("Y");
								dtoRec.setScoringBooleanValue(true);
								categoryFlag.put(dtoRec.getComponentId(), true);
							}else{
								dtoRec.setScoringValue(null);
								dtoRec.setScoringBooleanValue(null);
								categoryFlag.put(dtoRec.getComponentId(), false);
							}
						}
						
						if(categoryFlag.containsKey("5_0")){
							if(categoryFlag.get("5_0")){
								dtoRec.setScoringValue("Y");
								dtoRec.setScoringBooleanValue(true);
								categoryFlag.put(dtoRec.getComponentId(), true);
							}else{
								dtoRec.setScoringValue(null);
								dtoRec.setScoringBooleanValue(null);
								categoryFlag.put(dtoRec.getComponentId(), false);
							}
						}
					}*/
//					categoryRec.put(dtoRec.getComponentId(), dtoRec);
				}
			}
			
			if(categoryFlag.containsKey("7_0")){
				if(categoryFlag.get("7_0")){
					((HospitalScoringDTO)categoryRec.get("7_1")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("7_2")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("7_3")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("7_4")).setOptionEnabled(true);
					
					((HospitalScoringDTO)categoryRec.get("8_0")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_1")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_2")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_3")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_4")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_5")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("8_6")).setOptionEnabled(false);
				}else{
					((HospitalScoringDTO)categoryRec.get("7_1")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("7_2")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("7_3")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("7_4")).setOptionEnabled(false);
					
					((HospitalScoringDTO)categoryRec.get("8_0")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_1")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_2")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_3")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_4")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_5")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("8_6")).setOptionEnabled(true);
				}
			}
			
			// Resetting the value of 1023 for version compatabilty....
			/*HospitalScoring scoRecO = getHospitalScoringBySkey(selectedIntimation.getKey(), Long.parseLong(String.valueOf(1023)));
			if(scoRecO == null){
				HospitalScoringDTO dtoRec = categoryRec.get("3_3");//1023
				dtoRec.setScoringValue(null);
				dtoRec.setScoringBooleanValue(null);
				categoryFlag.put(dtoRec.getComponentId(), false);
			}
			
			if(categoryFlag.get("1_0") && categoryFlag.get("1_1") && categoryFlag.get("1_2")&& categoryFlag.get("1_3")){
				((HospitalScoringDTO)categoryRec.get("1_1")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("1_2")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("1_3")).setOptionEnabled(false);
			}else{
				((HospitalScoringDTO)categoryRec.get("1_1")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("1_2")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("1_3")).setOptionEnabled(true);
			}

			if(categoryFlag.get("2_0") && categoryFlag.get("2_1") && categoryFlag.get("2_2")&& categoryFlag.get("2_3")){
				((HospitalScoringDTO)categoryRec.get("2_1")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("2_3")).setOptionEnabled(false);
			}else{
				((HospitalScoringDTO)categoryRec.get("2_1")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("2_3")).setOptionEnabled(true);
			}
			
			if(categoryFlag.get("2_1") != null){
				if(categoryFlag.get("2_1") || !categoryFlag.get("2_1")){
					((HospitalScoringDTO)categoryRec.get("2_1")).setOptionEnabled(true);
//					((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(false);
//					if(categoryFlag.get("2_1")){
					if(categoryRec.get("2_1").getScoringBooleanValue() == null){
						((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(false);
					}else if(categoryRec.get("2_1").getScoringBooleanValue() != null && categoryRec.get("2_1").getScoringBooleanValue()){
						((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(false);
					}else{
						((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(true);
					}
				}

				if(((HospitalScoringDTO)categoryRec.get("2_2")).getScoringBooleanValue() == null || ((HospitalScoringDTO)categoryRec.get("2_2")).getScoringBooleanValue()){
					((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(false);
//					((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(false);
				}else{
					((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(true);
//					((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(true);
				}

			}else{
				if(categoryFlag.get("2_2") || !categoryFlag.get("2_2")){
					((HospitalScoringDTO)categoryRec.get("2_1")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("2_2")).setOptionEnabled(true);
					if(categoryFlag.get("2_2")){
						((HospitalScoringDTO)categoryRec.get("2_3")).setOptionEnabled(false);
					}else{
						((HospitalScoringDTO)categoryRec.get("2_3")).setOptionEnabled(true);
					}

					if(((HospitalScoringDTO)categoryRec.get("2_3")).getScoringBooleanValue() == null || ((HospitalScoringDTO)categoryRec.get("2_3")).getScoringBooleanValue()){
						((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(false);
						((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(false);
						((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(false);
						((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(false);
					}else{
						((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(true);
						((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(true);
						((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(true);
						((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(true);
					}
				}
			}
			
			if(categoryFlag.containsKey("5_0")){ // Reimbursement Case handling
				if(categoryFlag.get("5_0") && categoryFlag.get("5_1") && categoryFlag.get("5_2")&& categoryFlag.get("5_3")){
					((HospitalScoringDTO)categoryRec.get("5_1")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("5_2")).setOptionEnabled(false);
					((HospitalScoringDTO)categoryRec.get("5_3")).setOptionEnabled(false);
				}else{
					((HospitalScoringDTO)categoryRec.get("5_1")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("5_2")).setOptionEnabled(true);
					((HospitalScoringDTO)categoryRec.get("5_3")).setOptionEnabled(true);
				}
			}
			
			if(categoryFlag.get("3_0") && categoryFlag.get("3_1") && categoryFlag.get("3_2") && categoryFlag.get("3_3") && categoryFlag.get("3_4")){
				((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(false);
			}else{
				((HospitalScoringDTO)categoryRec.get("3_1")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("3_2")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("3_3")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("3_4")).setOptionEnabled(true);
			}
			
			
			if(categoryFlag.get("4_0") && categoryFlag.get("4_1") && categoryFlag.get("4_2") && categoryFlag.get("4_3")){
				((HospitalScoringDTO)categoryRec.get("4_1")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("4_2")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("4_3")).setOptionEnabled(false);
//				((HospitalScoringDTO)categoryRec.get("4_4")).setOptionEnabled(false);
			}else{
				((HospitalScoringDTO)categoryRec.get("4_1")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("4_2")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("4_3")).setOptionEnabled(true);
//				((HospitalScoringDTO)categoryRec.get("4_4")).setOptionEnabled(true);
			}
			
			//To Be removed - Start
			if(categoryFlag.get("5_0") && categoryFlag.get("5_1") && categoryFlag.get("5_2") && categoryFlag.get("5_3") && categoryFlag.get("5_4")){
				((HospitalScoringDTO)categoryRec.get("5_1")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("5_2")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("5_3")).setOptionEnabled(false);
				((HospitalScoringDTO)categoryRec.get("5_4")).setOptionEnabled(false);
			}else{
				((HospitalScoringDTO)categoryRec.get("5_1")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("5_2")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("5_3")).setOptionEnabled(true);
				((HospitalScoringDTO)categoryRec.get("5_4")).setOptionEnabled(true);
			}
			//To Be removed - End
			
			for(HospitalScoringDTO dtoRec : argList){
//				if(dtoRec.getComponentId().endsWith("0")){
//					String idStartWith = dtoRec.getComponentId().substring(0, 2);
					categoryFlag.put(dtoRec.getComponentId(), (dtoRec.getScoringValue() == null)?false:true);
//				}
			}

			*/
		}
	}

	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("Intimation.findByIntimationNumber").setParameter("intimationNo", intimationNo);
		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}

	public void saveHospitalScoring(List<HospitalScoringDTO> argList, String argIntimationNumber, String argScreenName, Long argUpdateKey){
		Intimation selectedIntimation  = getIntimationByNo(argIntimationNumber);
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		Claim claim = getClaimByIntimationkey(selectedIntimation.getKey());

//		call to check scoring table is already having the record or not.
		List<HospitalScoring> scoringList = getHospitalScoringByIntimationkey(selectedIntimation.getKey());
		int sizeOfList = scoringList.size();
		if(sizeOfList ==  0){
			for(HospitalScoringDTO rec : argList){
					HospitalScoring newIntimationScoring = new HospitalScoring();
					newIntimationScoring.setIntimationKey(selectedIntimation.getKey());
					newIntimationScoring.setClaimKey(claim.getKey());
					newIntimationScoring.setHospitalKey(selectedIntimation.getHospital());
					Hospitals hospital = getHospitalDetailsByKey(selectedIntimation.getHospital());
					if(hospital != null){
						newIntimationScoring.setHospitalCode(hospital.getHospitalCode());
					}else{
						newIntimationScoring.setHospitalCode(null);
					}
					newIntimationScoring.setSubCategoryKey(rec.getSubCategoryKey());
					newIntimationScoring.setGradeScore(rec.getScoringValue());
					newIntimationScoring.setCreatedBy(loginUserId.toLowerCase());
					newIntimationScoring.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					newIntimationScoring.setScoringVersion(SCORING_VERSION);
					newIntimationScoring.setDeleteFlag(0);
					entityManager.persist(newIntimationScoring);
			}
		}else if(sizeOfList > 0){
			//List<HospitalScoring> scroingList = getIntimationByNo(argIntimationNumber); // pass intimation number....
			boolean isOldVersionData = true;
			if(scoringList.size() ==  argList.size()){
				isOldVersionData = false;
				for(HospitalScoring rec1 : scoringList){
					if(rec1.getScoringVersion() != null && rec1.getScoringVersion().equals(OLD_SCORING_VERSION)){
						isOldVersionData =true;
						break;
					}
				}
			}
			List<Long> listOfUpdatingSC =  new ArrayList<Long>();
			for(HospitalScoringDTO rec : argList){
				HospitalScoring updIntimationScoring = getHospitalScoringBySkey(selectedIntimation.getKey(), rec.getSubCategoryKey());
				if(updIntimationScoring != null){
					updIntimationScoring.setGradeScore(rec.getScoringValue());
					updIntimationScoring.setModifiedBy(loginUserId.toLowerCase());
					updIntimationScoring.setModifiedDate((new Timestamp(System.currentTimeMillis())));
					updIntimationScoring.setScoringVersion(SCORING_VERSION);
					updIntimationScoring.setDeleteFlag(0);
					if(isOldVersionData){
						listOfUpdatingSC.add(updIntimationScoring.getSubCategoryKey());
					}
					entityManager.merge(updIntimationScoring);
				}else{
					HospitalScoring newIntimationScoring = new HospitalScoring();
					newIntimationScoring.setIntimationKey(selectedIntimation.getKey());
					newIntimationScoring.setClaimKey(claim.getKey());
					newIntimationScoring.setHospitalKey(selectedIntimation.getHospital());
					Hospitals hospital = getHospitalDetailsByKey(selectedIntimation.getHospital());
					if(hospital != null){
						newIntimationScoring.setHospitalCode(hospital.getHospitalCode());
					}else{
						newIntimationScoring.setHospitalCode(null);
					}
					newIntimationScoring.setSubCategoryKey(rec.getSubCategoryKey());
					newIntimationScoring.setGradeScore(rec.getScoringValue());
					newIntimationScoring.setCreatedBy(loginUserId.toLowerCase());
					newIntimationScoring.setCreatedDate((new Timestamp(System.currentTimeMillis())));
					newIntimationScoring.setScoringVersion(SCORING_VERSION);
					newIntimationScoring.setDeleteFlag(0);
					if(isOldVersionData){
						listOfUpdatingSC.add(rec.getSubCategoryKey());
					}
					entityManager.persist(newIntimationScoring);
				}
			}
			System.out.println("isOldVersionData : "+isOldVersionData);
			System.out.println("listOfUpdatingSC : "+listOfUpdatingSC);
			//Marking oldVersion Data as Deleted R1292
			if(isOldVersionData && listOfUpdatingSC.size() > 0){
				for(HospitalScoring rec : scoringList){
					if(!listOfUpdatingSC.contains(rec.getSubCategoryKey())){
						rec.setDeleteFlag(1);
						rec.setModifiedBy(loginUserId.toLowerCase());
						rec.setModifiedDate((new Timestamp(System.currentTimeMillis())));
						rec.setScoringVersion(OLD_SCORING_VERSION);
						entityManager.merge(rec);
					}
				}
			}
		}
		
		System.out.println(" Cashless / Reimbursement key :"+argUpdateKey);
		if(!argScreenName.equals("Doctors View")){
			if(argScreenName.equals("Pre-Auth") || argScreenName.equals("Enhancement")){
				Preauth cashlessRec = getPreauthById(argUpdateKey);
				if(null != cashlessRec){
					cashlessRec.setScoringFlag("Y");
					entityManager.merge(cashlessRec);
				}
				setScoringEnable(true);
			}else{
				Reimbursement reimburseRec = getReimbursement(argUpdateKey);
				reimburseRec.setScoringFlag("Y");
				entityManager.merge(reimburseRec);
				setScoringEnable(true);
			}
		}

		entityManager.flush();
		entityManager.clear();
		
		//add logic here 
		DBCalculationService dbCalc = new DBCalculationService();
		Map<String, Object> inputValues = new WeakHashMap<String, Object>();

		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		mapValues.put(SHAConstants.INTIMATION_NO, selectedIntimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q,SHAConstants.SERIOUS_DEFICIENCY_CUURENT_Q );
		//Additional field for stage source 
		String stageSource = "";
		Stage stage = null;
		if(!argScreenName.equals("Doctors View")){
			if(argScreenName.equals("Pre-Auth")) {
				stageSource = SHAConstants.SOURCE_PREAUTH_PROCESS ;
				stage = getStageBykey(ReferenceTable.PREAUTH_STAGE);
			} else if(argScreenName.equals("Enhancement")) {
				stageSource = SHAConstants.SOURCE_ENHANCEMENT_PROCESS ;
				stage = getStageBykey(ReferenceTable.ENHANCEMENT_STAGE);
			} else if(argScreenName.equals("Claim Request")) {
				stageSource = SHAConstants.MA_STAGE_SOURCE ;
				stage = getStageBykey(ReferenceTable.CLAIM_REQUEST_STAGE);
			}
			//As suggested by Sathish/Raja Sir, on 29-Aug-2019
		}else if(argScreenName.equals("Doctors View")){
			stageSource = SHAConstants.SOURCE_PREAUTH_PROCESS ;
			stage = getStageBykey(ReferenceTable.PREAUTH_STAGE);
		}
		System.out.println("screen name "+argScreenName+" Intimation no: "+selectedIntimation.getIntimationId());
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		List<Map<String, Object>> taskProcedure = dbCalc.revisedGetTaskProcedure(setMapValues);
		System.out.println("get task "+ taskProcedure.toString());
		Hospitals hospitalObject = getHospitalDetailsByKey(selectedIntimation.getHospital());
		/*Map<String, Object> sdFlagValues = getScoringMapValues();
		System.out.println("seriousDeficiencyEnabled "+sdFlagValues.get("seriousDeficiencyEnabled").toString()+" Intimation no: "+selectedIntimation.getIntimationId());
		System.out.println("moderateDeficienceEnabled "+sdFlagValues.get("moderateDeficienceEnabled").toString()+" Intimation no: "+selectedIntimation.getIntimationId());*/
		
		
		// Added for a serious production Issue.
		for(HospitalScoringDTO rec : argList){
			if(rec.getSubCategoryKey() != null && rec.getSubCategoryKey().intValue() == 7){
				if(rec.getScoringBooleanValue().booleanValue()){
					setSeriousDeficiencyMarked(true);
					setModerateDeficiencyMarked(false);
				}else{
					setSeriousDeficiencyMarked(false);
					setModerateDeficiencyMarked(true);
				}
			}
		}
		
		System.out.println("seriousDeficiencyEnabled "+isSeriousDeficiencyMarked()+" Intimation no: "+selectedIntimation.getIntimationId());
		System.out.println("moderateDeficienceEnabled "+isModerateDeficiencyMarked()+" Intimation no: "+selectedIntimation.getIntimationId());
		if(taskProcedure.isEmpty() || taskProcedure.size() == 0) {
//			if(Integer.parseInt(sdFlagValues.get("seriousDeficiencyEnabled").toString()) == 1) {
			if(isSeriousDeficiencyMarked()) {
				Object[] submitTaskValues = SHAUtils.getRevisedArrayListForManualHospitalScoringDBCall(selectedIntimation,hospitalObject,claim,stageSource);
				Object[] wrkflowObject = (Object[]) submitTaskValues[0];
				wrkflowObject[SHAConstants.INDEX_OUT_COME] = SHAConstants.ESCALATED_TO_RAW;
				//insert for stage source/ initiated state
				System.out.println("Before submit RAW "+selectedIntimation.getIntimationId());
				submitRAWValues(selectedIntimation,loginUserId, stage);
				System.out.println("After submit RAW "+selectedIntimation.getIntimationId());
				dbCalc.revisedInitiateTaskProcedure(submitTaskValues);
			}

		}else{
//			if(Integer.parseInt(sdFlagValues.get("moderateDeficienceEnabled").toString()) == 1) {
			if(isModerateDeficiencyMarked()) {
				Boolean isModerateDeficiency =true;
				Map<String,Object> resultRow = taskProcedure.get(0);
				resultRow.put(SHAConstants.OUTCOME, SHAConstants.ESCALATION_PULL_BACK);

//				Object[] wrkFlowObject = SHAUtils.getRevisedObjArrayForSubmit(resultRow);
				Object[] wrkFlowObject = SHAUtils.getRevisedObjArrayForHospitalScoringSubmit(resultRow,stageSource);
				System.out.println("Before update RAW for MD "+selectedIntimation.getIntimationId());
				updateRAWValues(selectedIntimation,loginUserId,isModerateDeficiency, stage);
				System.out.println("After update RAW for MD "+selectedIntimation.getIntimationId());
				dbCalc.revisedInitiateTaskProcedure(wrkFlowObject);
//			}else if(Integer.parseInt(sdFlagValues.get("seriousDeficiencyEnabled").toString()) == 1) {
			}else if(isSeriousDeficiencyMarked()) {
				Boolean isModerateDeficiency =false;
				Map<String,Object> resultRow = taskProcedure.get(0);
				resultRow.put(SHAConstants.OUTCOME, SHAConstants.ESCALATED_TO_RAW);
//				Object[] wrkFlowObject = SHAUtils.getRevisedObjArrayForSubmit(resultRow);
				Object[] wrkFlowObject = SHAUtils.getRevisedObjArrayForHospitalScoringSubmit(resultRow,stageSource);
				System.out.println("Before update RAW for SD "+selectedIntimation.getIntimationId());
				updateRAWValues(selectedIntimation,loginUserId,isModerateDeficiency, stage);
				System.out.println("After update RAW for SD "+selectedIntimation.getIntimationId());
				dbCalc.revisedInitiateTaskProcedure(wrkFlowObject);
			}
		}
	}

	public Preauth getPreauthById(Long preauthKey) {
		Query query = entityManager.createNamedQuery("Preauth.findByKey");
		query.setParameter("preauthKey", preauthKey);
		@SuppressWarnings("unchecked")
		List<Preauth> singleResult = (List<Preauth>) query.getResultList();
		if(singleResult != null && ! singleResult.isEmpty()) {
			entityManager.refresh(singleResult.get(0));
			return singleResult.get(0);
		}
		return null;		
	}
	
	@SuppressWarnings("unchecked")
	public Reimbursement getReimbursement(Long rodkey) {
		Query query = entityManager.createNamedQuery("Reimbursement.findByKey");
		query.setParameter("primaryKey", rodkey);
		List<Reimbursement> reimbursementList = (List<Reimbursement>) query.getResultList();
		if (reimbursementList != null && !reimbursementList.isEmpty()) {
			entityManager.refresh(reimbursementList.get(0));
			return reimbursementList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HospitalScoring getHospitalScoringBySkey(Long argIntimationKey, Long argSubCatKey){
		Query scoringCatquery = entityManager.createNamedQuery("HospitalScoring.findScoringByCatKey");
		scoringCatquery = scoringCatquery.setParameter("intimationKey", argIntimationKey);
		scoringCatquery = scoringCatquery.setParameter("subKey", argSubCatKey);
		List<HospitalScoring> catScorlist  = scoringCatquery.getResultList();
		if(catScorlist != null && !catScorlist.isEmpty()){
			return catScorlist.get(0);
		}		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HospitalScoring> getHospitalScoringByIntimationkey(Long argIntimationKey){
		Query scoringquery = entityManager.createNamedQuery("HospitalScoring.findByIntimationKey");
		scoringquery = scoringquery.setParameter("intimationKey", argIntimationKey);
		List<HospitalScoring> catScorSublist  = scoringquery.getResultList();
		return catScorSublist;
	}
	
	public Claim getClaimByIntimationkey(Long argIntimationKey){
		Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationKey");
		findByIntimationKey = findByIntimationKey.setParameter("intimationKey", argIntimationKey);
		Claim claim = (Claim) findByIntimationKey.getSingleResult();
		if(claim != null){
			return claim;
		}
		return null;
	}
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery("Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}
	
	public List<HospitalScoringDTO> getScoringDetails(String argIntimationNumber){
		Intimation selectedIntimation  = getIntimationByNo(argIntimationNumber);
		List<HospitalScoringDTO> categoryList = new ArrayList<HospitalScoringDTO>();
		categoryList = getScoringDetailsFromHS(selectedIntimation.getKey());
		return categoryList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<HospitalScoringDTO> getScoringDetailsFromHS(Long argIntimationKey){
		List<HospitalScoringDTO> scoringDataList = new ArrayList<HospitalScoringDTO>();
		HospitalScoringDTO dtoRec = null;
		Map<Long, HospitalScoringDTO> scoringMapList = new HashMap<Long, HospitalScoringDTO>();
		
		// Taking Data From Transaction Table.....
		Query scoquery = entityManager.createNamedQuery("HospitalScoring.findScoringOrderByKey");
		scoquery = scoquery.setParameter("intimationKey", argIntimationKey);
		List<HospitalScoring> scorlist  = scoquery.getResultList();
		if(scorlist != null && !scorlist.isEmpty()){
			for(HospitalScoring scoRec : scorlist){
				Query catquery = entityManager.createNamedQuery("HospitalCategory.findByKey");
				catquery = catquery.setParameter("cKey", scoRec.getSubCategoryKey());
				catquery = catquery.setParameter("scKey", scoRec.getSubCategoryKey());
				List<HospitalCategory> catRec  = catquery.getResultList();
				if(catRec != null && !catRec.isEmpty()){
					HospitalCategory categoryRec = catRec.get(0); // more than one record will come for header and one record will come for sub category.
					dtoRec = new HospitalScoringDTO();
					dtoRec.setActualCategoryId(categoryRec.getCategoryKey().intValue());
					if(scoRec.getSubCategoryKey() <= 8){
						dtoRec.setActualSubCategoryId(scoRec.getSubCategoryKey().intValue());
						dtoRec.setScoringName(categoryRec.getCategoryDesc());
						dtoRec.setTextFieldStyleName("tfwbBold");
					}else{
						dtoRec.setActualSubCategoryId(scoRec.getSubCategoryKey().intValue());
						dtoRec.setScoringName(categoryRec.getSubCategoryDesc());
						dtoRec.setTextFieldStyleName("tfwb");
					}
					String temp = scoRec.getGradeScore();
					dtoRec.setScoringValue(temp);
					if(temp != null){
						if(temp.equals("Y")){
							dtoRec.setScoringBooleanValue(true);
						}else{
							dtoRec.setScoringBooleanValue(false);
						}
					}else{
						dtoRec.setScoringBooleanValue(null);
					}
					dtoRec.setOptionVisible(true);
				}
				scoringMapList.put(scoRec.getSubCategoryKey(), dtoRec);
			}
			
			List<Long> grp1 = new ArrayList<Long>();
			grp1.add(1L);
			grp1.add(1001L);
			grp1.add(1002L);
			grp1.add(1003L);
			grp1.add(1022L);
			grp1.add(2L);
			grp1.add(1004L);
			grp1.add(1005L);
			grp1.add(1006L);
			grp1.add(3L);
			grp1.add(1007L);
			grp1.add(1008L);
			grp1.add(1009L);
			grp1.add(1010L);
			grp1.add(1023L);
			grp1.add(4L);
			grp1.add(1011L);
			grp1.add(1012L);
			grp1.add(1013L);
			grp1.add(1014L);
			grp1.add(5L);
			grp1.add(1015L);
			grp1.add(1016L);
			grp1.add(1017L);
			grp1.add(1018L);
			grp1.add(6L);
			grp1.add(1019L);
			grp1.add(1020L);
			grp1.add(1021L);
			grp1.add(1024L);
			grp1.add(7L);
			grp1.add(1025L);
			grp1.add(1026L);
			grp1.add(1027L);
			grp1.add(1034L);
			grp1.add(8L);
			grp1.add(1028L);
			grp1.add(1029L);
			grp1.add(1030L);
			grp1.add(1031L);
			grp1.add(1032L);
			grp1.add(1033L);
			grp1.add(1035L);
			grp1.add(1036L);
			grp1.add(1037L);
			grp1.add(1038L);
			grp1.add(1039L);
			grp1.add(1040L);
			grp1.add(1041L);
			grp1.add(1042L);
			grp1.add(1043L);
			grp1.add(1044L);
			grp1.add(1045L);
			grp1.add(1046L);
			grp1.add(1047L);
			
			HospitalScoringDTO temp = null;
			for (Long long1 : grp1) {
				temp = scoringMapList.get(long1);
				if(temp != null){
					scoringDataList.add(temp);
				}
			}
		}
		return scoringDataList;
	}
	
	public void submitRAWValues(Intimation selectedIntimation,String userName, Stage stage) {
		
		RawInvsHeaderDetails getIntimationBasedValues = getRawValuesByIntimation(selectedIntimation.getIntimationId());
		if(getIntimationBasedValues == null) {
		RawInvsHeaderDetails rawHeaderDetails = new RawInvsHeaderDetails();
		rawHeaderDetails.setIntimationNo(selectedIntimation.getIntimationId());
		rawHeaderDetails.setPolicyNumber(selectedIntimation.getPolicy().getPolicyNumber());
		rawHeaderDetails.setClaimType(selectedIntimation.getClaimType().getValue());
		String cpuCode = String.valueOf(selectedIntimation.getOriginalCpuCode());
		rawHeaderDetails.setCpuCode(cpuCode);
		//rawHeaderDetails.setCreatedBy(userName);
		rawHeaderDetails.setCreatedBy(userName);
		rawHeaderDetails.setCreatedDate(new Date());
		//rawHeaderDetails.setHospitalCode(selectedIntimation.getHospital());
		entityManager.persist(rawHeaderDetails);
		System.out.println("SUBMIT: Submit Raw Header"+rawHeaderDetails.toString());
		
		
		RawInvsDetails rawInvsDetaisl = new RawInvsDetails();
		rawInvsDetaisl.setRawInvstigation(rawHeaderDetails);
		//rawInvsDetaisl.setRequestedRemarks(requestedRemarks);
		rawInvsDetaisl.setRequestedBy(userName);
		
		rawInvsDetaisl.setRequestedDate(new Date());
		//rawInvsDetaisl.setRequestedStage(selectedIntimation.getStage().getKey());
		if(stage != null){
			rawInvsDetaisl.setRequestedStage(stage.getKey());
		}
		rawInvsDetaisl.setRequestedStatus(SHAConstants.RAW_STATUS_KEY);
		//rawInvsDetaisl.setCreatedBy(userName);
		//rawInvsDetaisl.setRawCategory();
		
		
		List<HospitalScoring> categoryValue = getCategoryDetails(selectedIntimation.getKey());
		Long categoryKey =7l;
		Long subCategoryKey =1025l;
		for (HospitalScoring hospitalScoring : categoryValue) {
			if(hospitalScoring.getSubCategoryKey()==7 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
				categoryKey =7l;
			}
			if(hospitalScoring.getSubCategoryKey()== 1025 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
				subCategoryKey = 1025l;
			}else if(hospitalScoring.getSubCategoryKey()== 1026 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
				subCategoryKey = 1026l;
			}else if(hospitalScoring.getSubCategoryKey()== 1027 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
				subCategoryKey = 1027l;
			}else if(hospitalScoring.getSubCategoryKey()== 1034 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
				subCategoryKey = 1034l;
			}
		}
		
		rawInvsDetaisl.setRawCategory(getRawCategory(categoryKey));
		rawInvsDetaisl.setRawSubCategory(getSubCategory(subCategoryKey));
		rawInvsDetaisl.setCreatedBy(userName);
		rawInvsDetaisl.setCreatedDate(new Date());
		entityManager.persist(rawInvsDetaisl);
		System.out.println("SUBMIT: Submit Raw Detail"+rawInvsDetaisl.toString());
		}else{
			RawInvsDetails rawInvsDetaisl = new RawInvsDetails();
			rawInvsDetaisl.setRawInvstigation(getIntimationBasedValues);
			//rawInvsDetaisl.setRequestedRemarks(requestedRemarks);
			rawInvsDetaisl.setRequestedBy(userName);
			rawInvsDetaisl.setRequestedDate(new Date());
			rawInvsDetaisl.setCreatedBy(userName);
			rawInvsDetaisl.setCreatedDate(new Date());
			//rawInvsDetaisl.setRequestedStage(selectedIntimation.getStage().getKey());
			if(stage != null){
				rawInvsDetaisl.setRequestedStage(stage.getKey());
			}
			
			rawInvsDetaisl.setRequestedStatus(SHAConstants.RAW_STATUS_KEY);
			List<HospitalScoring> categoryValue = getCategoryDetails(selectedIntimation.getKey());
			Long categoryKey =7l;
			Long subCategoryKey =1025l;
			for (HospitalScoring hospitalScoring : categoryValue) {
				if(hospitalScoring.getSubCategoryKey()==7 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					categoryKey =7l;
				}
				if(hospitalScoring.getSubCategoryKey()== 1025 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1025l;
				}else if(hospitalScoring.getSubCategoryKey()== 1026 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1026l;
				}else if(hospitalScoring.getSubCategoryKey()== 1027 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1027l;
				}else if(hospitalScoring.getSubCategoryKey()== 1034 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1034l;
				}
			}
			
			rawInvsDetaisl.setRawCategory(getRawCategory(categoryKey));
			rawInvsDetaisl.setRawSubCategory(getSubCategory(subCategoryKey));
			//rawInvsDetaisl.setCreatedBy(userName);
			entityManager.persist(rawInvsDetaisl);
			System.out.println("SUBMIT: Submit Raw Detail with Header"+rawInvsDetaisl.toString());
		}
		
		entityManager.flush();
		entityManager.clear();
		
	}
	
	
	public List<HospitalScoring> getCategoryDetails(Long intimationKey){
		
		Query query = entityManager.createNamedQuery("HospitalScoring.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);
		List<HospitalScoring> categoryList = (List<HospitalScoring>) query.getResultList();
		if (categoryList != null && !categoryList.isEmpty()) {
			return  categoryList;
		}
		return null;
	}
	
	public RawCategory getRawCategory(Long categoryKey){
		
		Query query = entityManager.createNamedQuery("RawCategory.findAll");
		query.setParameter("key", categoryKey);
		List<RawCategory> categoryList = (List<RawCategory>) query.getResultList();
		if (categoryList != null && !categoryList.isEmpty()) {
			return  categoryList.get(0);
		}
		return null;
	}
	
	public RawSubCategory getSubCategory(Long subCategoryKey){
		
		Query query = entityManager.createNamedQuery("RawSubCategory.findAll");
		query.setParameter("key", subCategoryKey);
		List<RawSubCategory> categoryList = (List<RawSubCategory>) query.getResultList();
		if (categoryList != null && !categoryList.isEmpty()) {
			return  categoryList.get(0);
		}
		return null;
	}
	
	
	public RawInvsHeaderDetails getRawValuesByIntimation(String intimationNo){
		
		Query query = entityManager.createNamedQuery("RawInvsHeaderDetails.findByIntimationNo");
		query.setParameter("intimationNo", intimationNo);
		List<RawInvsHeaderDetails> categoryList = (List<RawInvsHeaderDetails>) query.getResultList();
		if (categoryList != null && !categoryList.isEmpty()) {
			return  categoryList.get(0);
		}
		return null;
	}

	public void updateRAWValues(Intimation selectedIntimation, String userName,Boolean isModerateDeficiency, Stage stage) {
		RawInvsHeaderDetails getIntimationBasedValues = getRawValuesByIntimation(selectedIntimation.getIntimationId());
		if(getIntimationBasedValues != null) {
			
			List<RawInvsDetails> rawInvsDetaisl = getRAWDetailsByRAWHeader(getIntimationBasedValues.getKey());
			for (RawInvsDetails rawInvsDetails : rawInvsDetaisl) {
				if(rawInvsDetails.getRequestedStatus() == 251) {
					rawInvsDetails.setDeletedFlag(SHAConstants.YES_FLAG);
					rawInvsDetails.setModifiedDate(new Date());
					rawInvsDetails.setModifyby(userName);
					rawInvsDetails.setRequestedStatus(SHAConstants.RAW_PULL_BACK_STATUS_KEY);
					entityManager.merge(rawInvsDetails);
					System.out.println("UPDATE: Update Raw Detail"+rawInvsDetails.toString());
				}
			}
			if(!isModerateDeficiency) {
				RawInvsDetails rawInvsDetails = new RawInvsDetails();
				rawInvsDetails.setRawInvstigation(getIntimationBasedValues);
				//rawInvsDetaisl.setRequestedRemarks(requestedRemarks);
				rawInvsDetails.setRequestedBy(userName);
				rawInvsDetails.setRequestedDate(new Date());
				rawInvsDetails.setCreatedBy(userName);
				rawInvsDetails.setCreatedDate(new Date());
				if(stage != null){
					rawInvsDetails.setRequestedStage(stage.getKey());
				}
				rawInvsDetails.setRequestedStatus(SHAConstants.RAW_STATUS_KEY);
				List<HospitalScoring> categoryValue = getCategoryDetails(selectedIntimation.getKey());
				Long categoryKey =7l;
				Long subCategoryKey =1025l;
				for (HospitalScoring hospitalScoring : categoryValue) {
					if(hospitalScoring.getSubCategoryKey()==7 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
						categoryKey =7l;
					}
					if(hospitalScoring.getSubCategoryKey()== 1025 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
						subCategoryKey = 1025l;
					}else if(hospitalScoring.getSubCategoryKey()== 1026 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
						subCategoryKey = 1026l;
					}else if(hospitalScoring.getSubCategoryKey()== 1027 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
						subCategoryKey = 1027l;
					}else if(hospitalScoring.getSubCategoryKey()== 1034 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
						subCategoryKey = 1034l;
					}
				}
				
				rawInvsDetails.setRawCategory(getRawCategory(categoryKey));
				rawInvsDetails.setRawSubCategory(getSubCategory(subCategoryKey));
				entityManager.persist(rawInvsDetails);
				System.out.println("UPDATE: Insert Raw Detail"+rawInvsDetails.toString());
			}
			
		}else{
			RawInvsHeaderDetails rawHeaderDetails = new RawInvsHeaderDetails();
			rawHeaderDetails.setIntimationNo(selectedIntimation.getIntimationId());
			rawHeaderDetails.setPolicyNumber(selectedIntimation.getPolicy().getPolicyNumber());
			rawHeaderDetails.setClaimType(selectedIntimation.getClaimType().getValue());
			String cpuCode = String.valueOf(selectedIntimation.getOriginalCpuCode());
			rawHeaderDetails.setCpuCode(cpuCode);
			rawHeaderDetails.setCreatedBy(userName);
			rawHeaderDetails.setCreatedDate(new Date());
			//rawHeaderDetails.setHospitalCode(selectedIntimation.getHospital());
			entityManager.persist(rawHeaderDetails);
			System.out.println("UPDATE: Insert Raw Header"+rawHeaderDetails.toString());
			
			
			RawInvsDetails rawInvsDetaisl = new RawInvsDetails();
			rawInvsDetaisl.setRawInvstigation(rawHeaderDetails);
			//rawInvsDetaisl.setRequestedRemarks(requestedRemarks);
			rawInvsDetaisl.setRequestedBy(userName);
			rawInvsDetaisl.setRequestedDate(new Date());
			rawInvsDetaisl.setCreatedDate(new Date());
			if(stage != null){
				rawInvsDetaisl.setRequestedStage(stage.getKey());
			}
			rawInvsDetaisl.setRequestedStatus(SHAConstants.RAW_STATUS_KEY);
			
			
			List<HospitalScoring> categoryValue = getCategoryDetails(selectedIntimation.getKey());
			Long categoryKey =7l;
			Long subCategoryKey =1025l;
			for (HospitalScoring hospitalScoring : categoryValue) {
				if(hospitalScoring.getSubCategoryKey()==7 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					categoryKey =7l;
				}
				if(hospitalScoring.getSubCategoryKey()== 1025 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1025l;
				}else if(hospitalScoring.getSubCategoryKey()== 1026 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1026l;
				}else if(hospitalScoring.getSubCategoryKey()== 1027 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1027l;
				}else if(hospitalScoring.getSubCategoryKey()== 1034 && hospitalScoring.getGradeScore().equals(SHAConstants.YES_FLAG)) {
					subCategoryKey = 1034l;
				}
			}
			
			rawInvsDetaisl.setRawCategory(getRawCategory(categoryKey));
			rawInvsDetaisl.setRawSubCategory(getSubCategory(subCategoryKey));
			rawInvsDetaisl.setCreatedBy(userName);
			entityManager.persist(rawInvsDetaisl);
			System.out.println("UPDATE: Insert Raw detail"+rawInvsDetaisl.toString());
		}
		
		entityManager.flush();
		entityManager.clear();
		
	}
	
	public List<RawInvsDetails> getRAWDetailsByRAWHeader(Long rawInvkey){
		
		Query query = entityManager.createNamedQuery("RawInvsDetails.findByHeaderKey");
		query.setParameter("rawInvkey", rawInvkey);
		List<RawInvsDetails> categoryList = (List<RawInvsDetails>) query.getResultList();
		if (categoryList != null && !categoryList.isEmpty()) {
			return  categoryList;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	public Stage getStageBykey(Long a_key) {
		Stage stageObj = null;
		if(a_key != null){
			Query query = entityManager
					.createNamedQuery("Stage.findByKey");
			query = query.setParameter("stageKey", a_key);
			List<Stage> mastersValueList = query.getResultList();
			if(mastersValueList != null && !mastersValueList.isEmpty()){
				for (Stage value : mastersValueList) {
					entityManager.refresh(value);
					stageObj = value;
				}
			}
		}

		return stageObj;
	}
}
