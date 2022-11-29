package com.shaic.domain;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import oracle.jdbc.OracleTypes;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.shaic.ClaimRemarksAlerts;
import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.MagazineDTO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.fields.dto.SpecialSelectValue;
import com.shaic.arch.table.AuditCategory;
import com.shaic.arch.table.AuditDetails;
import com.shaic.arch.table.AuditMasCategory;
import com.shaic.arch.table.AuditProcessor;
import com.shaic.arch.table.AuditTeam;
import com.shaic.branchmanagerfeedback.BranchManagerCategoryTable;
import com.shaic.claim.ELearnDto;
import com.shaic.claim.MasterGST;
import com.shaic.claim.cvc.auditqueryreplyprocessing.cashless.SearchCVCAuditClsQryFormDTO;
import com.shaic.claim.cvc.postprocess.MasPostClaimCVCCategory;
import com.shaic.claim.pcc.beans.MasUserPCCRoleMappingDetails;
import com.shaic.claim.preauth.view.DiagnosisDTO;
import com.shaic.claim.reimbursement.dto.ExtraEmployeeEffortDTO;
import com.shaic.claim.reimbursement.dto.RRCDTO;
import com.shaic.claim.reimbursement.rrc.services.ProcessRRCRequestMapper;
import com.shaic.claim.rod.searchCriteria.ViewSearchCriteriaTableDTO;
import com.shaic.claim.rod.wizard.dto.DocumentCheckListDTO;
import com.shaic.claim.rod.wizard.dto.SectionDetailsTableDTO;
import com.shaic.claim.viewEarlierRodDetails.ZUAViewQueryHistoryTable;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.fss.MasRack;
import com.shaic.domain.fss.MasShelf;
import com.shaic.domain.fss.MasStorageLocation;
import com.shaic.domain.omp.OMPNegotiationMas;
import com.shaic.domain.preauth.ClaimSubLimit;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.ExclusionDetails;
import com.shaic.domain.preauth.HospitalPackage;
import com.shaic.domain.preauth.IcdBlock;
import com.shaic.domain.preauth.IcdChapter;
import com.shaic.domain.preauth.IcdCode;
import com.shaic.domain.preauth.MasPrivateInvestigator;
import com.shaic.domain.preauth.MasRejectSubCategory;
import com.shaic.domain.preauth.MasterRemarks;
import com.shaic.domain.preauth.PedValidation;
import com.shaic.domain.preauth.PreExistingDisease;
import com.shaic.domain.preauth.PreauthType;
import com.shaic.domain.preauth.ProcedureMaster;
import com.shaic.domain.preauth.ProcedureSpecialityMaster;
import com.shaic.domain.preauth.SpecialityType;
import com.shaic.domain.preauth.Stage;
import com.shaic.domain.preauth.TmpFvR;
import com.shaic.domain.preauth.TreatingDoctorQualification;
import com.shaic.domain.preauth.UserBranchMapping;
import com.shaic.domain.preauth.UserLoginDetails;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.main.navigator.ui.MasMagazineDocument;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class MasterService {

	@PersistenceContext
	protected EntityManager entityManager;

	public MasterService() {
		super();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByValue");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList){
				a_mastersValue = mastersValue;
				break;
			}
		}

		return a_mastersValue;
	}


	public MastersValue getPenalIntrestRate(String penalIntrest) {

		MastersValue a_mastersValue = new MastersValue();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", penalIntrest);
		List<MastersValue> mastersValueList = query.getResultList();
		for (MastersValue mastersValue : mastersValueList)
			a_mastersValue = mastersValue;


		return a_mastersValue;
	}
	public Status getStatusByName(String statusName, String stageName) {
		Stage stage = null;
		Status status = null;
		if (null != stageName && null != statusName) {
			try {

				Query query = entityManager
						.createNamedQuery("Stage.findByStageName");
				query = query.setParameter("stageName",
						"%" + StringUtils.trim(stageName.toLowerCase()) + "%");
				stage = (Stage) query.getResultList().get(0);

				Query stageQuery = entityManager
						.createNamedQuery("Status.findByName");
				query = stageQuery.setParameter("stageId", stage.getKey());
				query.setParameter("statusName",
						"%" + StringUtils.trim(statusName.toLowerCase()) + "%");
				status = (Status) stageQuery.getResultList().get(0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} /*else {

		}*/

		return status;
	}

	public Status getStatusByStageName(String stageName) {
		Stage stage = null;
		Status result = null;
		if (stageName != null) {
			try {

				Query query = entityManager
						.createNamedQuery("Status.findByStageName");
				query = query.setParameter("stageName",
						"%" + StringUtils.trim(stageName.toLowerCase()) + "%");
				result = (Status) query.getResultList().get(0);

				Query stageQuery = entityManager
						.createNamedQuery("Stage.findByName");
				query = stageQuery.setParameter("stageName",
						"%" + StringUtils.trim(stageName.toLowerCase()) + "%");
				stage = (Stage) stageQuery.getResultList().get(0);
				result.setStage(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} /*else {

		}*/

		return result;
	}

	public BeanItemContainer<SelectValue> getStageList(){

		Query query = entityManager
				.createNamedQuery("Stage.findAll");

		List<Stage> stageList = (List<Stage>) query.getResultList();

		BeanItemContainer<SelectValue> stageContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selected = null;
		for (Stage stage : stageList) {
			selected = new SelectValue();
			selected.setId(stage.getKey());
			selected.setValue(stage.getStageName());
			stageContainer.addBean(selected);
		}

		stageContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return stageContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSectionList(Long productKey,Policy policy){

		Query query = entityManager
				.createNamedQuery("ClaimSection.findByProduct");
		query.setParameter("productKey", productKey);

		List<ClaimSection> claimSectionList = (List<ClaimSection>) query.getResultList();

		BeanItemContainer<SelectValue> claimSectionContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selected = null;
		TataPolicy tataPolicy = getTataPolicy(policy.getPolicyNumber());
		if(tataPolicy != null){
			for (ClaimSection claimSection : claimSectionList) { 
				if(null != claimSection.getSectionKey() && (ReferenceTable.getTataTrustSectionKeys().containsKey(claimSection.getSectionKey()))){
					selected = new SelectValue();
					selected.setId(claimSection.getSectionKey());
					selected.setValue(claimSection.getSectionValue());
					selected.setCommonValue(claimSection.getSectionCode());
					claimSectionContainer.addBean(selected);
					selected = null;
				}
			}
		}
		else{
			for (ClaimSection claimSection : claimSectionList) {                
				selected = new SelectValue();
				selected.setId(claimSection.getSectionKey());
				selected.setValue(claimSection.getSectionValue());
				selected.setCommonValue(claimSection.getSectionCode());
				claimSectionContainer.addBean(selected);
				selected = null;
			}
		}
		claimSectionContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return claimSectionContainer;
	}

	public ClaimSection getSectionForView(Long productKey, String sectionCode)
	{
		Query query = entityManager
				.createNamedQuery("ClaimSection.findByProductAndSection");
		query.setParameter("productKey", productKey);
		query.setParameter("sectionCode", sectionCode);

		List<ClaimSection> claimSectionList = (List<ClaimSection>) query.getResultList();

		if(claimSectionList !=null && !claimSectionList.isEmpty())
		{
			return claimSectionList.get(0);
		}

		return null;

	}

	public ClaimSectionSubCover getSubCoverForView(Long coverKey, String subCoverCode)
	{
		Query query = entityManager
				.createNamedQuery("ClaimSectionSubCover.findByCoverKeyAndSubCoverCode");
		query.setParameter("coverKey", coverKey);
		query.setParameter("subCoverCode", subCoverCode);

		List<ClaimSectionSubCover> claimSubCoverList = (List<ClaimSectionSubCover>) query.getResultList();

		if(claimSubCoverList !=null && !claimSubCoverList.isEmpty())
		{
			return claimSubCoverList.get(0);
		}

		return null;

	}


	public ClaimSectionCover getCoverForView(Long sectionKey, String coverCode)
	{
		Query query = entityManager
				.createNamedQuery("ClaimSectionCover.findBySectionKeyAndCoverCode");
		query.setParameter("sectionKey", sectionKey);
		query.setParameter("coverCode", coverCode);

		List<ClaimSectionCover> claimCoverList = (List<ClaimSectionCover>) query.getResultList();

		if(claimCoverList !=null && !claimCoverList.isEmpty())
		{
			return claimCoverList.get(0);
		}

		return null;

	}


	public BeanItemContainer<SelectValue> getCoverList(Long sectionKey){

		Query query = entityManager
				.createNamedQuery("ClaimSectionCover.findBySectionKey").setParameter("sectionKey", sectionKey);

		List<ClaimSectionCover> claimSectionCoverList = (List<ClaimSectionCover>) query.getResultList();

		BeanItemContainer<SelectValue> claimSectionContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selected = null;
		for (ClaimSectionCover claimSection : claimSectionCoverList) {
			selected = new SelectValue();
			selected.setId(claimSection.getCoverKey());
			selected.setValue(claimSection.getCoverValue());
			selected.setCommonValue(claimSection.getCoverCode());
			claimSectionContainer.addBean(selected);
			selected = null;
		}

		claimSectionContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return claimSectionContainer;
	}

	public List<SectionDetailsTableDTO> getSectionDetailsDtoList(Long coverKey){
		List<SectionDetailsTableDTO> resultList = null;
		try{
			Query query = entityManager
					.createNamedQuery("ClaimSectionSubCover.findByCoverKey").setParameter("coverKey", coverKey);

			List<ClaimSectionSubCover> claimSectionSubCoverList = (List<ClaimSectionSubCover>) query.getResultList();

			if(claimSectionSubCoverList != null && !claimSectionSubCoverList.isEmpty()){
				resultList = new ArrayList<SectionDetailsTableDTO>();
				SectionDetailsTableDTO tableDto = null;
				for (ClaimSectionSubCover claimSectionSubCover : claimSectionSubCoverList) {
					tableDto = new SectionDetailsTableDTO();
					tableDto.setSubCover(new SelectValue(claimSectionSubCover.getSubCoverkey(), claimSectionSubCover.getSubCoverValue()));			
					ClaimSectionCover coverObj = getClaimCoverByKey(claimSectionSubCover.getCoverKey());
					tableDto.setCover(new SelectValue(coverObj.getCoverKey(), coverObj.getCoverValue()));
					ClaimSection sectionObj = getClaimSectionByKey(claimSectionSubCover.getSectionKey());
					tableDto.setCover(new SelectValue(sectionObj.getSectionKey(), sectionObj.getSectionValue()));
					resultList.add(tableDto);	
				}	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
	private ClaimSectionCover getClaimCoverByKey(Long coverKey){

		try{
			Query query = entityManager
					.createNamedQuery("ClaimSectionCover.findByCoverKey").setParameter("coverKey", coverKey);
			List<ClaimSectionCover> claimSectionCoverList = (List<ClaimSectionCover>) query.getResultList();
			if(claimSectionCoverList != null && !claimSectionCoverList.isEmpty()){
				return claimSectionCoverList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	private ClaimSection getClaimSectionByKey(Long sectionKey){
		try{
			Query query = entityManager
					.createNamedQuery("ClaimSection.findByKey");
			query.setParameter("sectionKey", sectionKey);

			List<ClaimSection> claimSectionList = (List<ClaimSection>) query.getResultList();

			if(claimSectionList !=null && !claimSectionList.isEmpty())
			{
				return claimSectionList.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}

	public BeanItemContainer<SelectValue> getSubCoverList(Long coverKey){

		Query query = entityManager
				.createNamedQuery("ClaimSectionSubCover.findByCoverKey").setParameter("coverKey", coverKey);

		List<ClaimSectionSubCover> claimSectionSubCoverList = (List<ClaimSectionSubCover>) query.getResultList();

		BeanItemContainer<SelectValue> claimSectionSubContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue selected = null;
		for (ClaimSectionSubCover claimSectionSubCover : claimSectionSubCoverList) {
			selected = new SelectValue();
			selected.setId(claimSectionSubCover.getSubCoverkey());
			selected.setValue(claimSectionSubCover.getSubCoverValue());
			selected.setCommonValue(claimSectionSubCover.getSubCoverCode());
			claimSectionSubContainer.addBean(selected);
			selected = null;
		}

		claimSectionSubContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return claimSectionSubContainer;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<CityTownVillage> getCityTownVillage() {
		Query findAll = entityManager
				.createNamedQuery("CityTownVillage.findAll");

		List<CityTownVillage> cityTownVillageList = (List<CityTownVillage>) findAll
				.getResultList();
		BeanItemContainer<CityTownVillage> cityTownVillageContainer = new BeanItemContainer<CityTownVillage>(
				CityTownVillage.class);
		cityTownVillageContainer.addAll(cityTownVillageList);

		return cityTownVillageContainer;
	}




	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<CityTownVillage> getCityTownVillage(Long a_key) {

		Query query = entityManager
				.createNamedQuery("CityTownVillage.findByStateKey");
		query = query.setParameter("parentKey", a_key);
		List<CityTownVillage> cityTownVillageList = query.getResultList();

		BeanItemContainer<CityTownVillage> cityTownVillageContainer = new BeanItemContainer<CityTownVillage>(
				CityTownVillage.class);
		cityTownVillageContainer.addAll(cityTownVillageList);

		return cityTownVillageContainer;
	}

	public List<CityTownVillage> getCities(Long key){

		Query query = entityManager
				.createNamedQuery("CityTownVillage.findByStateKey");
		query = query.setParameter("parentKey", key);
		List<CityTownVillage> cityTownVillageList = query.getResultList();

		return cityTownVillageList;
	}

	public BeanItemContainer<SelectValue> getCityList(Long key) {

		/*Query findAll = entityManager
				.createNamedQuery("CityTownVillage.findAll");*/
		Query query = entityManager
				.createNamedQuery("CityTownVillage.findByStateKey");
		query = query.setParameter("parentKey", key);
		List<CityTownVillage> cityTownVillageList = query.getResultList();

		List<SelectValue> cities = new ArrayList<SelectValue>();
		SelectValue selected = null;
		for (CityTownVillage city : cityTownVillageList) {
			selected = new SelectValue();
			selected.setId(city.getKey());
			selected.setValue(city.getValue());
			cities.add(selected);
		}
		@SuppressWarnings("deprecation")
		BeanItemContainer<SelectValue> selectedValueContainer = new BeanItemContainer<SelectValue>(
				cities);
		return selectedValueContainer;

	}


	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getcpuLotNo() {

		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByClaimKey");

		List<ClaimPayment> cpuLotList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!cpuLotList.isEmpty()) {

			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (ClaimPayment cpuLotNo : cpuLotList) {
				selectValue = new SelectValue();
				selectValue.setId(cpuLotNo.getKey());
				selectValue.setValue(cpuLotNo.getLotNumber());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getCpuLotNumberForPaymentProcessCpu() {

		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByClaimKey");

		List<ClaimPayment> cpuLotList = query.setMaxResults(20).getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!cpuLotList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (ClaimPayment cpuLotNo : cpuLotList) {
				selectValue = new SelectValue();
				selectValue.setId(cpuLotNo.getKey());
				selectValue.setValue(cpuLotNo.getLotNumber());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}
	
	@SuppressWarnings("unchecked")
	public List<SelectValue> getCpuLotNumberForBasedOnInput(String lotNumber) {

		Query query = entityManager
				.createNamedQuery("ClaimPayment.findByLotNumber");
		query.setParameter("lotNumber","%"+(!lotNumber.isEmpty() ? lotNumber.toLowerCase() : "")+"%");

		List<ClaimPayment> cpuLotList = query.setMaxResults(20).getResultList();
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		if (!cpuLotList.isEmpty()) {
			SelectValue selectValue = null;
			for (ClaimPayment cpuLotNo : cpuLotList) {
				selectValue = new SelectValue();
				selectValue.setId(cpuLotNo.getKey());
				selectValue.setValue(cpuLotNo.getLotNumber());
				selectValueList.add(selectValue);
			}
		}
		return selectValueList;
	}
	
	

	//	@SuppressWarnings({ "unchecked", "unused" })
	//	public BeanItemContainer<Country> getCountry() {
	//		Query findAll = entityManager.createNamedQuery("Country.findAll");
	//		List<Country> countryList = (List<Country>) findAll.getResultList();
	//		BeanItemContainer<Country> countryContainer = new BeanItemContainer<Country>(
	//				Country.class);
	//		countryContainer.addAll(countryList);
	//
	//		return countryContainer;
	//	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<State> getState() {
		Query findAll = entityManager.createNamedQuery("State.findAll");
		List<State> stateList = (List<State>) findAll.getResultList();
		BeanItemContainer<State> stateContainer = new BeanItemContainer<State>(
				State.class);
		stateContainer.addAll(stateList);
		return stateContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getStateListSelectValue() {
		Query findAll = entityManager.createNamedQuery("State.findAll");
		List<State> stateList = (List<State>) findAll.getResultList();
		BeanItemContainer<SelectValue> stateContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!stateList.isEmpty()) {
			for (State state : stateList) {
				SelectValue stateSelectValue = new SelectValue();
				stateSelectValue.setId(state.getKey());
				stateSelectValue.setValue(state.getValue());
				stateContainer.addBean(stateSelectValue);
			}
		}

		//stateContainer.addAll(stateList);
		return stateContainer;
	}



	public List<State> getStateList() {
		Query findAll = entityManager.createNamedQuery("State.findAll");
		@SuppressWarnings("unchecked")
		List<State> stateList = (List<State>) findAll.getResultList();
		return stateList;
	}

	public State getStateByKey(Long key) {
		if(key != null){
			return entityManager.find(State.class, key);
		}else{
			return null;
		}
	}

	public CityTownVillage getCityByKey(Long key) {

		if(key != null){
			return entityManager.find(CityTownVillage.class, key);
		}
		return null;
	}

	public List<State> stateSearch(String q) {
		Query query = entityManager.createNamedQuery("State.findByName");
		query.setParameter("stateName", "%" + q.toLowerCase() + "%");
		return (List<State>) query.getResultList();
	}

	public List<SelectValue> stateSelectSearch(String q) {
		Query query = entityManager.createNamedQuery("State.findByName");
		query.setParameter("stateName", "%" + q.toLowerCase() + "%");
		List<State> resultList = query.getResultList();
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		if (!resultList.isEmpty()) {
			SelectValue selectValue = null;
			for (State state : resultList) {
				selectValue = new SelectValue();
				selectValue.setId(state.getKey());
				selectValue.setValue(state.getValue());
				selectValueList.add(selectValue);
			}
		}
		return selectValueList;
	}

	public BeanItemContainer<SelectValue> searchIcdBlockByChapterKey(
			Long chapterKey) {
		Query query = entityManager
				.createNamedQuery("IcdBlock.findByChapterKey");
		List<IcdBlock> resultList = null;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> icdBlockContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if (chapterKey != null) {
			query.setParameter("chapterKey", chapterKey);
			resultList = query.getResultList();

			if (!resultList.isEmpty()) {
				SelectValue selectValue = null;
				for (IcdBlock icdBlock : resultList) {
					selectValue = new SelectValue();
					selectValue.setId(icdBlock.getKey());
					String value = icdBlock.getValue();
					if(value != null && icdBlock.getDescription() != null){
						value = value +" - "+ icdBlock.getDescription();
					}
					selectValue.setValue(value);
					selectValueList.add(selectValue);
				}
			}
			icdBlockContainer.addAll(selectValueList);
		}

		return icdBlockContainer;
	}

	public BeanItemContainer<SelectValue> searchIcdCodeByBlockKey(Long blockKey) {
		Query query = entityManager.createNamedQuery("IcdCode.findByBlockKey");
		List<IcdCode> resultList;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (blockKey != null) {
			query.setParameter("blockKey", blockKey);
			resultList = query.getResultList();

			if (!resultList.isEmpty()) {
				SelectValue selectValue = null;
				for (IcdCode icdCode : resultList) {
					selectValue = new SelectValue();
					selectValue.setId(icdCode.getKey());
					String value = icdCode.getValue();
					if(value != null && icdCode.getDescription() != null){
						value = value + " - "+ icdCode.getDescription();
					}
					selectValue.setValue(value);
					selectValueList.add(selectValue);
				}
			}
			icdCodeContainer.addAll(selectValueList);
		}
		return icdCodeContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> searchPEDCode(Long blockKey) {
		Query query = entityManager
				.createNamedQuery("PreExistingDisease.findByKey");
		List<PreExistingDisease> resultList;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (blockKey != null) {
			query.setParameter("primaryKey", blockKey);
			resultList = query.getResultList();

			if (!resultList.isEmpty()) {
				SelectValue selectValue = null;
				for (PreExistingDisease pedCode : resultList) {
					selectValue = new SelectValue();
					selectValue.setId(pedCode.getKey());
					selectValue.setValue(pedCode.getValue());
					selectValueList.add(selectValue);
				}
			}
			icdCodeContainer.addAll(selectValueList);
		}
		return icdCodeContainer;
	}

	public String getPEDCode(Long blockKey) {
		Query query = entityManager
				.createNamedQuery("PreExistingDisease.findByKey");
		List<PreExistingDisease> resultList;
		//BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if (blockKey != null) {
			query.setParameter("primaryKey", blockKey);
		}
		resultList = query.getResultList();
		if (resultList != null) {
			return resultList.get(0).getCode();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> searchExclusionDetailsByImpactKey(
			Long impactKey) {
		Query query = entityManager
				.createNamedQuery("ExclusionDetails.findByImpactId");
		List<ExclusionDetails> resultList;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> exclusionContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (impactKey != null) {
			query.setParameter("impactKey", impactKey);
			resultList = query.getResultList();

			if (!resultList.isEmpty()) {
				SelectValue selectValue = null;
				for (ExclusionDetails exclusion : resultList) {
					selectValue = new SelectValue();
					selectValue.setId(exclusion.getKey());
					selectValue.setValue(exclusion.getExclusion());
					selectValueList.add(selectValue);
				}
			}
			exclusionContainer.addAll(selectValueList);
		}
		return exclusionContainer;
	}


	@SuppressWarnings("unchecked")
	public BeanItemContainer<ExclusionDetails> getExclusionDetailsByImpactKey(
			Long impactKey) {
		Query query = entityManager
				.createNamedQuery("ExclusionDetails.findByImpactId");
		List<ExclusionDetails> resultList;
		BeanItemContainer<ExclusionDetails> exclusionContainer = new BeanItemContainer<ExclusionDetails>(
				ExclusionDetails.class);
		if (impactKey != null) {
			query.setParameter("impactKey", impactKey);
			resultList = query.getResultList();

			exclusionContainer.addAll(resultList);
		}
		return exclusionContainer;
	}

	@SuppressWarnings("unchecked")
	public List<ExclusionDetails> getExclusionDetailsList() {
		Query query = entityManager
				.createNamedQuery("ExclusionDetails.findAll");

		List<ExclusionDetails> resultList = query.getResultList();

		return resultList;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<MastersValue> getMasterValue(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<MastersValue> mastersValueContainer = new BeanItemContainer<MastersValue>(
				MastersValue.class);
		mastersValueContainer.addAll(mastersValueList);

		return mastersValueContainer;
	}

	/*@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
			//	if(null != master.getKey() && !(ReferenceTable.HOSP_EXPENSE_WORK_PLACE.equals(master.getKey()) || ReferenceTable.HOSP_EXPENSE_NON_WORK_PLACE.equals(master.getKey())))
				{
					SelectValue selectValue = new SelectValue();
					selectValue.setId(master.getKey().longValue());
					selectValue.setValue(master.getValue());
					selectValueList.add(selectValue);
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}*/

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainer(String a_key) {
		/*// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(master.getKey().longValue());
				selectValue.setValue(master.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}*/

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
		.add(Restrictions.eq("activeStatus", 1))
		.add(Restrictions.eq("code", a_key))
		.addOrder(Order.asc("value"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());


		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerForPackagePdt(String a_key,Long pdtKey) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
				//	if(null != master.getKey() && !(ReferenceTable.HOSP_EXPENSE_WORK_PLACE.equals(master.getKey()) || ReferenceTable.HOSP_EXPENSE_NON_WORK_PLACE.equals(master.getKey())))
				if(!(null != pdtKey && ReferenceTable.PACKAGE_PAC_PRD_003.equals(pdtKey) && (ReferenceTable.NON_ALLOPATHIC_ID).equals(master.getKey().longValue())))
				{
					SelectValue selectValue = new SelectValue();
					selectValue.setId(master.getKey().longValue());
					selectValue.setValue(master.getValue());
					selectValueList.add(selectValue);
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getCoversAndBenefitsContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
				//	if(null != master.getKey() && !(ReferenceTable.HOSP_EXPENSE_WORK_PLACE.equals(master.getKey()) || ReferenceTable.HOSP_EXPENSE_NON_WORK_PLACE.equals(master.getKey())))
				{
					SelectValue selectValue = new SelectValue();
					selectValue.setId(master.getKey().longValue());
					selectValue.setValue(master.getValue());
					selectValueList.add(selectValue);
				}
			}
			selectValueContainer.addAll(selectValueList);
		}
		selectValueContainer.addAll(getCoversContainer());
		return selectValueContainer;
	}


	private List<SelectValue> getCoversContainer()
	{
		Query query = entityManager
				.createNamedQuery("MasPAClaimCover.findByUniqueCoverDesc");


		List<Object> mastersValueList = query.getResultList();

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		if (!mastersValueList.isEmpty()) {


			for (int i = 0 ; i < mastersValueList.size() ; i++)
			{
				String masString =(String)mastersValueList.get(i);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(Long.valueOf(i));
				selectValue.setValue(masString);
				selectValueList.add(selectValue);
			}
			/*for (Object items: mastersValueList) {

				String masString =(String)items;
				SelectValue selectValue = new SelectValue();
				//selectValue.setId(Long.valueOf(i));
				selectValue.setValue(masString);
				selectValueList.add(selectValue);
			}
			 */
			/*for(int i = 0 ; i<= mastersValueList.size() ; i++)
			{
				//MasPAClaimCover mas = mastersValueList.get(i);alueList.get(i);
				String masString =(String) mastersValueList.get(i);
				SelectValue selectValue = new SelectValue();
				selectValue.setId(Long.valueOf(i));
				selectValue.setValue(masString);
				selectValueList.add(selectValue);
			}*/

			/*for (MasPAClaimCover master : mastersValueList) {
			//	if(null != master.getKey() && !(ReferenceTable.HOSP_EXPENSE_WORK_PLACE.equals(master.getKey()) || ReferenceTable.HOSP_EXPENSE_NON_WORK_PLACE.equals(master.getKey())))
				{
					SelectValue selectValue = new SelectValue();
					selectValue.setId(master.getKey().longValue());
					selectValue.setValue(master.getCoverDesc());
					selectValueList.add(selectValue);
				}
			}*/
		}
		return selectValueList;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerForBenefits(String a_key,Long productKey) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
				if(null != master.getKey() && !(ReferenceTable.HOSP_EXPENSE_WORK_PLACE.equals(master.getKey()) || ReferenceTable.HOSP_EXPENSE_NON_WORK_PLACE.equals(master.getKey())
						|| ReferenceTable.HOSPITALIZATION_BENEFITS.equals(master.getKey()) || ReferenceTable.PARTIAL_HOSP_BENEFITS.equals(master.getKey())))
				{
					if(!((null != productKey && ReferenceTable.ACCIDENT_CARE_SCHOOL_STUDENT_PRODUCT_KEY.equals(productKey) || ReferenceTable.ACCIDENT_CARE_COLLEGE_STUDENT_PRODUCT_KEY.equals(productKey))
							&& (ReferenceTable.PPD_BENEFIT_MASTER_VALUE.equals(master.getKey()) || ReferenceTable.TTD_BENEFIT_MASTER_VALUE.equals(master.getKey()))
							))
					{
						SelectValue selectValue = new SelectValue();
						selectValue.setId(master.getKey().longValue());
						selectValue.setValue(master.getValue());
						selectValueList.add(selectValue);
						
						if((null != productKey && ReferenceTable.ACCIDENT_FAMILY_CARE_FLT_KEY.equals(productKey))
								&& !(ReferenceTable.DEATH_BENEFIT_MASTER_VALUE.equals(master.getKey()) || ReferenceTable.PTD_BENEFIT_MASTER_VALUE.equals(master.getKey()))){
							selectValueList.remove(selectValue);
						}
					}

				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public List<State> getStateListForFVR() {

		Query fvrQuery = entityManager.createNamedQuery("TmpFvR.findAll");
		List<TmpFvR> resultList = fvrQuery.getResultList();	

		List<Long> stateKeyList = new ArrayList<Long>();

		for (TmpFvR tmpFvR : resultList) {
			if(tmpFvR.getState() != null && tmpFvR.getState().getKey() != null){
				stateKeyList.add(tmpFvR.getState().getKey());
			}
		}

		Query findAll = entityManager.createNamedQuery("State.findBySpecificList");
		findAll.setParameter("stateKeyList", stateKeyList);
		@SuppressWarnings("unchecked")
		List<State> stateList = (List<State>) findAll.getResultList();
		return stateList;

	}

	/**
	 * Added for create and search lot , payment status dropdown values
	 * */
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getPaymentStatusValue(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				
				if(!(null != master.getKey() && Long.valueOf(master.getKey()).equals(ReferenceTable.PAYMENT_STATUS_HOLD_PENDING)))
				{
					selectValue = new SelectValue();
					selectValue.setId(master.getKey().longValue());
					selectValue.setValue(master.getValue());
				}
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}


	public BeanItemContainer<SelectValue> getExclusionDetails(String a_key) {
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				selectValue = new SelectValue();
				selectValue.setId(master.getKey().longValue());
				selectValue.setValue(master.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getSelectValueContainerForReasonForAdmission(
			NewIntimationDto intiamtionDto) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if (intiamtionDto.getReasonForAdmission() != null
				&& !intiamtionDto.getReasonForAdmission().equals("")) {

			String reasons[] = StringUtils.split(
					intiamtionDto.getReasonForAdmission(), ",");
			if (reasons != null && reasons.length > 0) {
				SelectValue resultSelectValue = null;
				for (int i = 0; i < reasons.length; i++) {
					resultSelectValue = new SelectValue();
					Query query = entityManager
							.createNamedQuery("ReasonForAdmission.findByValue");
					query = query.setParameter("reasons",
							reasons[i]);
					List<ReasonForAdmission> reasonForAdmissionList = query
							.getResultList();
					//List<SelectValue> admissionReasonSelectValueList = new ArrayList<SelectValue>();
					if (reasonForAdmissionList != null
							&& !reasonForAdmissionList.isEmpty()) {
						for (ReasonForAdmission reasonObject : reasonForAdmissionList) {
							resultSelectValue.setId(reasonObject.getKey());
							resultSelectValue.setValue(reasonObject.getValue());
							selectValueContainer.addBean(resultSelectValue);
						}
					}
				}
			}
		}

		return selectValueContainer;
	}

	/**
	 * Method for loading reason for admisison values from Data base
	 * */

	public BeanItemContainer<SelectValue> getSelectValueContainerForReasonForAdmission()
	{
		Query query = entityManager.createNamedQuery("ReasonForAdmission.findAll");
		List<ReasonForAdmission> reasonForAdmissionList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!reasonForAdmissionList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (ReasonForAdmission admissionReason : reasonForAdmissionList) {
				selectValue = new SelectValue();
				selectValue.setId(admissionReason.getKey());
				selectValue.setValue(admissionReason.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerForSpecialist() {
		Query query = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!employeeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey());
				selectValue.setValue(employee.getEmployeeWithNames());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public MastersValue getSpecialistConsulted(Long key){

		Query query = entityManager.createNamedQuery("TmpEmployee.findByKey");
		query.setParameter("primaryKey", key);
		List<TmpEmployee> employeeList = query.getResultList();

		if(employeeList != null && ! employeeList.isEmpty()){
			MastersValue employee = new MastersValue();
			employee.setKey(employeeList.get(0).getKey());
			String employeeName = employeeList.get(0).getEmpId();
			employeeName += employeeList.get(0).getEmpFirstName();
			employee.setValue(employeeName);
			return employee;
		}

		return null;


	}

	public BeanItemContainer<SelectValue> getIcdCodeValue(Long key) {

		Query query = entityManager.createNamedQuery("IcdCode.findByBlockKey")
				.setParameter("blockKey", key);

		List<IcdCode> icdCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdCode icdCode : icdCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getIcdCodeById(Long key) {

		Query query = entityManager.createNamedQuery("IcdCode.findByKey")
				.setParameter("primaryKey", key);

		List<IcdCode> icdCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdCode icdCode : icdCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public SelectValue getIcdCodeByKey(Long key) {

		Query query = entityManager.createNamedQuery("IcdCode.findByKey")
				.setParameter("primaryKey", key);

		IcdCode icdCodeList = (IcdCode) query.getSingleResult();
		SelectValue selectValue = null;
		if (icdCodeList != null) {
			selectValue = new SelectValue();
			selectValue.setId(icdCodeList.getKey());
			selectValue.setValue(icdCodeList.getValue());
			return selectValue;
		}
		return null;
	}

	public BeanItemContainer<SelectValue> getIcdChapterbyKey(Long key) {
		Query query = entityManager.createNamedQuery("IcdChapter.findByKey")
				.setParameter("primaryKey", key);

		List<IcdChapter> icdChapterList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdChapterList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdChapter icdCode : icdChapterList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public SelectValue getIcdChapterbyId(Long key) {
		Query query = entityManager.createNamedQuery("IcdChapter.findByKey")
				.setParameter("primaryKey", key);

		IcdChapter icdChapterList = (IcdChapter) query.getSingleResult();
		SelectValue selectValue = null;
		if (icdChapterList != null) {
			selectValue = new SelectValue();
			selectValue.setId(icdChapterList.getKey());
			selectValue.setValue(icdChapterList.getValue());
			return selectValue;
		}
		return null;
	}

	public BeanItemContainer<SelectValue> getIcdBlockbyKey(Long key) {
		Query query = entityManager.createNamedQuery(
				"IcdBlock.findByChapterKey").setParameter("chapterKey", key);
		List<IcdBlock> icdBlockList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdBlockList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdBlock icdCode : icdBlockList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getIcdBlockbyId(Long key) {
		Query query = entityManager.createNamedQuery("IcdBlock.findByKey")
				.setParameter("primaryKey", key);
		List<IcdBlock> icdBlockList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdBlockList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdBlock icdCode : icdBlockList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public SelectValue getIcdBlock(Long key) {
		Query query = entityManager.createNamedQuery("IcdBlock.findByKey")
				.setParameter("primaryKey", key);
		IcdBlock icdBlockList = (IcdBlock) query.getSingleResult();
		if (icdBlockList != null) {
			SelectValue selectValue = new SelectValue();
			selectValue.setId(icdBlockList.getKey());
			selectValue.setValue(icdBlockList.getValue());
			return selectValue;
		}
		return null;
	}

	public BeanItemContainer<SelectValue> getPedCodebyKey(Long key) {

		Query query = entityManager.createNamedQuery(
				"PreExistingDisease.findByKey").setParameter("primaryKey", key);
		List<PreExistingDisease> pedCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!pedCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (PreExistingDisease icdCode : pedCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public SelectValue getPedCodebyId(Long key) {

		Query query = entityManager.createNamedQuery(
				"PreExistingDisease.findByKey").setParameter("primaryKey", key);
		PreExistingDisease pedCodeList = (PreExistingDisease) query
				.getSingleResult();
		if (pedCodeList != null) {
			SelectValue selectValue = new SelectValue();
			selectValue.setId(pedCodeList.getKey());
			selectValue.setValue(pedCodeList.getValue());
			return selectValue;
		}
		return null;
	}

	public BeanItemContainer<SelectValue> getPedDescription() {

		Query query = entityManager
				.createNamedQuery("PreExistingDisease.findAll");
		List<PreExistingDisease> pedCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!pedCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (PreExistingDisease icdCode : pedCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getICDchapter() {

		Query query = entityManager.createNamedQuery("IcdChapter.findAll");
		List<IcdChapter> pedCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!pedCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdChapter icdCode : pedCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdCode.getKey());
				selectValue.setValue(icdCode.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	//	@SuppressWarnings({ "unchecked", "unused" })
	//	public BeanItemContainer<TmpPolicy> getSearchPolicyDetails() {
	//		Query findAll = entityManager.createNamedQuery("TmpPolicy.findAll");
	//		List<TmpPolicy> policyList = (List<TmpPolicy>) findAll.getResultList();
	//		// BeanContainer<String, TmpPolicy> beans = new BeanContainer<String,
	//		// TmpPolicy>(TmpPolicy.class);
	//		// beans.setBeanIdProperty("polSysId");
	//		BeanItemContainer<TmpPolicy> policyListContainer = new BeanItemContainer<TmpPolicy>(
	//				TmpPolicy.class);
	//		policyListContainer.addAll(policyList);
	//		return policyListContainer;
	//	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<PolicyCondition> getPolicyConditions() {
		Query findAll = entityManager
				.createNamedQuery("PolicyCondition.findAll");
		List<PolicyCondition> policyConditionsList = (List<PolicyCondition>) findAll
				.getResultList();
		BeanItemContainer<PolicyCondition> policyConditionsListContainer = new BeanItemContainer<PolicyCondition>(
				PolicyCondition.class);
		policyConditionsListContainer.addAll(policyConditionsList);
		return policyConditionsListContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<Product> getProduct() {
		Query findAll = entityManager.createNamedQuery("Product.findAll");
		List<Product> productList = (List<Product>) findAll.getResultList();
		BeanItemContainer<Product> productContainer = new BeanItemContainer<Product>(
				Product.class);
		productContainer.addAll(productList);

		return productContainer;
	}

	public Product getProrataForProduct(Long productId)
	{
		//	Product a_mastersValue = new MastersValue();
		//if (a_key != null) {
		Query query = entityManager
				.createNamedQuery("Product.findByKey");
		query = query.setParameter("key", productId);
		List<Product> productValList = query.getResultList();
		Product product = null;
		if(null != productValList && !productValList.isEmpty())
		{
			/*for (Product productValue : productValList)
				{
					product = productValList.get(0);
				}*/
			product = productValList.get(0);
		}

		//}

		return product;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<NetworkHospital> getNetworkHospital() {
		Query findAll = entityManager.createNamedQuery("Country.findAll");
		List<NetworkHospital> networkHospitalList = (List<NetworkHospital>) findAll
				.getResultList();
		BeanItemContainer<NetworkHospital> networkHospitalContainer = new BeanItemContainer<NetworkHospital>(
				NetworkHospital.class);
		networkHospitalContainer.addAll(networkHospitalList);

		return networkHospitalContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<NonNetworkHospital> getNonNetworkHospital() {
		Query findAll = entityManager.createNamedQuery("Country.findAll");
		List<NonNetworkHospital> nonnetworkHospitalList = (List<NonNetworkHospital>) findAll
				.getResultList();
		BeanItemContainer<NonNetworkHospital> nonnetworkHospitalContainer = new BeanItemContainer<NonNetworkHospital>(
				NonNetworkHospital.class);
		nonnetworkHospitalContainer.addAll(nonnetworkHospitalList);

		return nonnetworkHospitalContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<Hospitals> getMasHospitals(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("Hospitals.findByhospitalType");
		query = query.setParameter("parentKey", "" + a_key);
		List<Hospitals> masHospitalsList = query.getResultList();
		BeanItemContainer<Hospitals> masHospitalsContainer = new BeanItemContainer<Hospitals>(
				Hospitals.class);
		masHospitalsContainer.addAll(masHospitalsList);

		return masHospitalsContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList){
				a_mastersValue = mastersValue;
				break;
			}
		}

		return a_mastersValue;
	}

	public MastersValue getMasterForPremia(Long a_key,EntityManager entityManager) {
		this.entityManager = entityManager;
		return getMaster(a_key);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public Product getProductByProductCode(String productCode) {
		Product product = new Product();
		if (productCode != null) {
			Query findAll = entityManager
					.createNamedQuery("Product.findByCode").setParameter(
							"productCode", productCode);
			List<Product> productList = findAll.getResultList();
			for (Product mastersValue : productList) {
				product = mastersValue;
			}
		}
		return product;
	}
	
	public Boolean isTataTrustPolicy(String policyNo){
//		entityManager.merge(claim);
//		entityManager.flush();
		
		String query = "SELECT POLICY_NUMBER FROM  IMS_CLS_TATA_POLICY  WHERE POLICY_NUMBER ='" + policyNo+"'";
		
		Query nativeQuery = entityManager.createNativeQuery(query);
		List<String> policyNumber = (List<String>)nativeQuery.getResultList();
		
		if(policyNumber != null && ! policyNumber.isEmpty()){
			return true;
		}
		return false;

	}


	@SuppressWarnings({ "unchecked", "unused" })
	public Product getProductByProductId(Long productKey) {
		Product product = new Product();
		if (productKey != null) {
			Query findAll = entityManager
					.createNamedQuery("Product.findByKey").setParameter(
							"key", productKey);
			List<Product> productList = findAll.getResultList();
			for (Product mastersValue : productList) {
				product = mastersValue;
			}
		}
		return product;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getRelationshipMasterByRelationshipCode(
			String relationshipCode, Integer key) {
		MastersValue masterValueByRelationshipCode = new MastersValue();
		if (relationshipCode != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByKey");
			query = query.setParameter("parentKey", key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				if (StringUtils.equalsIgnoreCase(mastersValue.getCode(),
						relationshipCode)) {
					masterValueByRelationshipCode = mastersValue;
				}
		}
		return masterValueByRelationshipCode;
	}

	public List<CityTownVillage> citySearch(String q, State state) {
		Query query = entityManager
				.createNamedQuery("CityTownVillage.searchCityByNameAndState");
		query.setParameter("sateKey", state.getKey());
		query.setParameter("cityQuery", "%" + q.toLowerCase() + "%");
		return (List<CityTownVillage>) query.getResultList();
	}

	//	public List<Locality> localitySearch(String q, CityTownVillage city) {
	//		Query query = entityManager
	//				.createNamedQuery("Locality.searchCityByNameAndCity");
	//		query.setParameter("cityKey", city.getKey());
	//		query.setParameter("localityQuery", "%" + q.toLowerCase() + "%");
	//		return (List<Locality>) query.getResultList();
	//	}
	//	
	//	
	//
	//	public List<Locality> localitySearchbyCityKey(Long key) {
	//		Query query = entityManager
	//				.createNamedQuery("Locality.searchAreaByCity");
	//		query.setParameter("cityKey", key);
	//
	//		return (List<Locality>) query.getResultList();
	//	}
	//	
	//	public Locality localitySearchByKey(Long key) {
	//		Query query = entityManager
	//				.createNamedQuery("Locality.searchByKey");
	//		query.setParameter("primaryKey", key);
	//        
	//		return (Locality) query.getSingleResult();
	//	}


	//	public BeanItemContainer<SelectValue> getAreaList(Long key) {
	//
	//		Query query = entityManager
	//				.createNamedQuery("Locality.searchAreaByCity");
	//		query.setParameter("cityKey", key);
	//		List<Locality> area = query.getResultList();
	//
	//		List<SelectValue> cities = new ArrayList<SelectValue>();
	//
	//		for (Locality city : area) {
	//			SelectValue selected = new SelectValue();
	//			selected.setId(city.getKey());
	//			selected.setValue(city.getValue());
	//			cities.add(selected);
	//		}
	//		@SuppressWarnings("deprecation")
	//		BeanItemContainer<SelectValue> selectedValueContainer = new BeanItemContainer<SelectValue>(
	//				cities);
	//		return selectedValueContainer;
	//
	//	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getRelationsShipByValue(String a_value) {
		MastersValue masterValue = null;
		String searchValue = null;

		if (a_value != null) {
			searchValue = StringUtils.trim(a_value);
		}

		BeanItemContainer<MastersValue> relationshipContainer = getMasterValue(ReferenceTable.RELATIONSHIP);

		List<MastersValue> mastersValueList = relationshipContainer
				.getItemIds();

		for (MastersValue a_mastersValue : mastersValueList) {
			if (StringUtils.equalsIgnoreCase(
					StringUtils.trim(a_mastersValue.getValue()), searchValue)) {
				masterValue = a_mastersValue;
			}
		}

		return masterValue;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getKeyByValue(String a_value) {
		MastersValue masterValue = new MastersValue();
		String searchValue = null;

		if (a_value != null) {
			if (a_value.equals("M") || a_value.equals("m"))
				searchValue = "Male";
			else if (a_value.equals("F") || a_value.equals("f"))
				searchValue = "Female";
			else
				searchValue = StringUtils.trim(a_value);
		}
		if(null != searchValue){
			Query query = entityManager
					.createNamedQuery("MastersValue.findByValue");
			query = query.setParameter("parentKey", searchValue.toLowerCase());
			List<MastersValue> mastersValueList = query.getResultList();

			if(mastersValueList != null && !mastersValueList.isEmpty() )
			{
				for (MastersValue a_mastersValue : mastersValueList){
					masterValue = a_mastersValue;
				}
			}

		}
		return masterValue;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getKeyByValueFromPremia(String a_value,EntityManager entityManager) {
		this.entityManager = entityManager;
		return getKeyByValue(a_value);
	}

	public BeanItemContainer<SelectValue> getConversionReasonByValue(String key) {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", key);
		List<MastersValue> mastersValueList = query.getResultList();
		SelectValue selected = null;
		for (int i = 0; i < mastersValueList.size(); i++) {
			selected = new SelectValue();
			selected.setId(mastersValueList.get(i).getKey());
			selected.setValue(mastersValueList.get(i).getValue());

			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				selectValueList);

		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getOMPFileTypes() {
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		Query query = entityManager.createNamedQuery("OMPFileType.findDistinctFileType");
		List<OMPFileType> ompFileTypeList = query.getResultList();
		Map<Long, String> distinctList = new HashMap<Long, String>();
		SelectValue selected = null;
		for (OMPFileType rec : ompFileTypeList) {
			if(distinctList.get(rec.getFileTypeKey()) == null){
				distinctList.put(rec.getFileTypeKey(), rec.getFileTypeDesc());
			}
		}
		for (Map.Entry<Long,String> entry : distinctList.entrySet()) {
//            System.out.println("Key = " + entry.getKey() +", Value = " + entry.getValue()); 
            selected = new SelectValue();
    		selected.setId(entry.getKey());
    		selected.setValue(entry.getValue());
    		selectValueList.add(selected);
		}
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(selectValueList);
		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getOMPDocumentTypes(Long argFileTypKey) {
		Query query = entityManager.createNamedQuery("OMPFileType.findDocByFile");
		query.setParameter("fileTypeKey", argFileTypKey);
		List<OMPFileType> resultList = (List<OMPFileType>) query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if (!resultList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (OMPFileType rec : resultList) {
				selectValue = new SelectValue();
				selectValue.setId(rec.getDocTypeKey());
				selectValue.setValue(rec.getDocTypeDesc());
				selectValueList.add(selectValue);
				selectValue = null;
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getRejectionCategoryByValue() {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey",
				ReferenceTable.REJECTION_CATEGORY);

		List<MastersValue> mastersValueList = query.getResultList();
		SelectValue selected = null;
		for (int i = 0; i < mastersValueList.size(); i++) {
			selected = new SelectValue();
			selected.setId(mastersValueList.get(i).getKey());
			selected.setValue(mastersValueList.get(i).getValue());

			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		selectValueContainer.sort(new Object[] {"id"}, new boolean[] {true});

		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getReimbRejCategoryByValue() {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey",
				ReferenceTable.REIMB_REJECTION_CATEGORY);

		List<MastersValue> mastersValueList = query.getResultList();
		SelectValue selected = null;
		for (int i = 0; i < mastersValueList.size(); i++) {
			selected = new SelectValue();
			selected.setId(mastersValueList.get(i).getKey());
			selected.setValue(mastersValueList.get(i).getValue());

			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		selectValueContainer.sort(new Object[] {"id"}, new boolean[] {true});

		return selectValueContainer;
	}
	
	
	public BeanItemContainer<SelectValue> getSettledRejectionCategory() {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey",
				ReferenceTable.SETTLED_REJECTION_CATEGORY);
		
		List<MastersValue> mastersValueList = query.getResultList();
		SelectValue selected = null;
		for (int i = 0; i < mastersValueList.size(); i++) {
			selected = new SelectValue();
			selected.setId(mastersValueList.get(i).getKey());
			selected.setValue(mastersValueList.get(i).getValue());

			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		selectValueContainer.sort(new Object[] {"id"}, new boolean[] {true});

		return selectValueContainer;
	}
	
	public String getCommonByValue(Long key) {

		String commonValue;

		Query query = entityManager.createNamedQuery("MastersValue.findByKey");
		query = query.setParameter("parentKey", key);

		MastersValue mastersValueList = (MastersValue) query.getSingleResult();
		commonValue = mastersValueList.getValue();

		return commonValue;
	}

	public BeanItemContainer<SelectValue> getSelectValuesForICDChapter() {
		Query query = entityManager.createNamedQuery("IcdChapter.findAll");
		@SuppressWarnings("unchecked")
		List<IcdChapter> icdChapterList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdChapterList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdChapter icdChapter : icdChapterList) {
				selectValue = new SelectValue();
				selectValue.setId(icdChapter.getKey());
				String value = icdChapter.getValue();
				if(value != null && icdChapter.getDescription() != null){
					value = value +" - "+icdChapter.getDescription().trim();
				}
				selectValue.setValue(value);
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getSelectValuesForICDBlock() {
		Query query = entityManager.createNamedQuery("IcdBlock.findAll");
		@SuppressWarnings("unchecked")
		List<IcdBlock> icdBlockList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdBlockList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdBlock icdChapter : icdBlockList) {
				selectValue = new SelectValue();
				selectValue.setId(icdChapter.getKey());
				String value = icdChapter.getValue();
				if(value != null && icdChapter.getDescription() != null){
					value = value +" - "+ icdChapter.getDescription();
				}
				selectValue.setValue(value);
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public String getDiagnosis(List<PedValidation> pedValidationList){

		StringBuffer diagnosisName = new StringBuffer();

		for (PedValidation pedValidation : pedValidationList) {

			Query diagnosis = entityManager.createNamedQuery("Diagnosis.findDiagnosisByKey");
			diagnosis.setParameter("diagnosisKey", pedValidation.getDiagnosisId());
			Diagnosis masters = (Diagnosis) diagnosis.getSingleResult();
			if(masters != null){
				diagnosisName.append(masters.getValue()).append(",");
			}

		}

		return diagnosisName.toString();
	}

	public BeanItemContainer<SelectValue> getSelectValuesForICDCode() {
		Query query = entityManager.createNamedQuery("IcdCode.findAll");
		@SuppressWarnings("unchecked")
		List<IcdCode> icdCodeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!icdCodeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (IcdCode icdChapter : icdCodeList) {
				selectValue = new SelectValue();
				selectValue.setId(icdChapter.getKey());
				String value = icdChapter.getValue();
				if(value != null && icdChapter.getDescription() != null){
					value = value +" - "+ icdChapter.getDescription();
				}
				selectValue.setValue(value);
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	@SuppressWarnings({ "unchecked" })
	public List<SelectValue> getDiagnosisSelctValuesList(String diagnosisString) {
		/*Query findAll = entityManager.createNamedQuery("Diagnosis.findByName")
				.setParameter("diagnosisString",
						"%" + diagnosisString.toLowerCase() + "%");
		List<Diagnosis> diagnosisList = (List<Diagnosis>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (Diagnosis diagnosis : diagnosisList) {
			select = new SelectValue();
			select.setId(diagnosis.getKey());
			select.setValue(diagnosis.getValue());
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);*/


		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(Diagnosis.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(Restrictions.like("value", diagnosisString+"%"))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
														.setMaxResults(20)
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
//		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
//				SelectValue.class);
//		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		//		return selectValueContainer;

		return selectValuesList;
	}

	public PreExistingDisease getValueByKey(Long key) {

		if(null != key)
		{
			Query query =  entityManager.createNamedQuery(
					"PreExistingDisease.findByKey");
			query = query.setParameter("primaryKey", key);
			List<PreExistingDisease> result = query.getResultList();
			if(null != result && !result.isEmpty())
			{
				entityManager.refresh(result.get(0));
				return result.get(0);
			}
		}
		return null;

		/**
		 * 
		 * Below code was commented, sinec findAll.getSingleResult
		 * will lead to no entity found error. Hence revamped it
		 * as per above code.
		 * */
		/*Query findAll = entityManager.createNamedQuery(
				"PreExistingDisease.findByKey").setParameter("primaryKey", key);
		PreExistingDisease result = (PreExistingDisease) findAll
				.getSingleResult();*/
		//return result;

	}

	public BeanItemContainer<SelectValue> getDiagnosisList() {
		System.out.println("---------------- Second Approach Start current time "  + new Date());
		/*
//		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
//		@SuppressWarnings("unchecked")
//		List<Diagnosis> diagnosisList = (List<Diagnosis>) findAll
//				.getResultList();
//		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
//		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
//				SelectValue.class);
//		for (Diagnosis diagnosis : diagnosisList) {
//			SelectValue select = new SelectValue();
//			select.setId(diagnosis.getKey());
//			select.setValue(diagnosis.getValue());
//			selectValuesList.add(select);
//		}
//		selectValueContainer.addAll(selectValuesList);
//		selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
//		return selectValueContainer;

		System.out.println("---------------- Second Approach Start current time for DIAGNOSIS ******************" + System.currentTimeMillis());

		Query findAll = entityManager.createNamedQuery("Diagnosis.findAll");
		@SuppressWarnings("unchecked")
		List<Object[]> diagnosisList = (List<Object[]>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (Object[] diagnosis : diagnosisList) {
			SelectValue select = new SelectValue();
			select.setId((Long)diagnosis[0]);
			select.setValue((String)diagnosis[1]);
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);
		selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time for DIAGNOSIS ******************" + System.currentTimeMillis());

		return selectValueContainer;
		 */
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(Diagnosis.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.addOrder(Order.asc("value"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value"))
														.setMaxResults(20)
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		SHAUtils.setDiagnosisTableList(selectValueContainer);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public String getDiagnosisList(List<DiagnosisDTO> diagnosisDTOList) {
		if (diagnosisDTOList != null) {
			String diagnosisNames = "";
			for (DiagnosisDTO diagnosisDTO : diagnosisDTOList) {
				Query findDiagnosisName = entityManager.createNamedQuery(
						"Diagnosis.findDiagnosisName").setParameter(
								"diagnosisKey", diagnosisDTO.getDiagnosisId());
				List<String> diagnosisList = findDiagnosisName.getResultList();
				for (String diagnosisName : diagnosisList) {
					diagnosisNames = diagnosisNames.toString().replaceFirst(
							diagnosisName + ",", "");
					diagnosisNames = diagnosisNames +(diagnosisName + ", ");
				}
			}

			return diagnosisNames.toString();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public String getDiagnosisList(String diagnosisId,
			EntityManager entitymanager) {
		this.entityManager = entitymanager;
		StringBuffer diagnosisNames = new StringBuffer();
		if (diagnosisId != null) {
			Query findDiagnosisName = entityManager.createNamedQuery(
					"Diagnosis.findDiagnosisName").setParameter("diagnosisKey",
							Long.parseLong(diagnosisId));
			List<String> diagnosisList = findDiagnosisName.getResultList();
			for (String diagnosisName : diagnosisList) {
				diagnosisNames.append(diagnosisName + ",");
			}
			return diagnosisNames.toString();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public String getDiagnosisNames(String diagnosisId) {
		StringBuffer diagnosisNames = new StringBuffer();
		Query findDiagnosisName = entityManager.createNamedQuery(
				"Diagnosis.findDiagnosisName").setParameter("diagnosisKey",
						diagnosisId);
		List<Diagnosis> diagnosisList = findDiagnosisName.getResultList();
		for (Diagnosis diagnosis : diagnosisList) {
			diagnosisNames.append(diagnosis.getValue() + ",");
		}
		return diagnosisNames.toString();

	}

	@SuppressWarnings("unchecked")
	public Boolean getByDiagnosisNames(String diagnosisName) {
		//StringBuffer diagnosisNames = new StringBuffer();
		Query findDiagnosisName = entityManager.createNamedQuery(
				"Diagnosis.findByName").setParameter("diagnosisString",
						diagnosisName.toLowerCase());
		List<Diagnosis> diagnosisList = findDiagnosisName.getResultList();
		if(null!= diagnosisList && !diagnosisList.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Method to retreive the cpu code and its description from db.
	 * */
	public BeanItemContainer<SelectValue> getTmpCpuCodes() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getCpuCode());
			select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}

	public BeanItemContainer<SelectValue> getTmpFvrCpuCode() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getKey());
			select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}

	/*	*//**
	 * Method to retreive the intimation source.
	 * 
	 * */
	/*
	 * 
	 * public BeanItemContainer<SelectValue> getIntimationSource(Integer key){
	 * 
	 * List<SelectValue> selectValueList = new ArrayList<SelectValue>();
	 * 
	 * Query query = entityManager
	 * .createNamedQuery("MastersValue.findByMasterListKey"); query =
	 * query.setParameter("parentKey",key); List<MastersValue> mastersValueList
	 * = query.getResultList(); for (int i = 0; i < mastersValueList.size();
	 * i++) { SelectValue selected = new SelectValue();
	 * selected.setId(mastersValueList.get(i).getKey());
	 * selected.setValue(mastersValueList.get(i).getValue());
	 * 
	 * selectValueList.add(selected); } BeanItemContainer<SelectValue>
	 * selectValueContainer = new BeanItemContainer<SelectValue>(
	 * selectValueList);
	 * 
	 * return selectValueContainer; }
	 */
	public List<InsuredPedDetails> getInsuredPedDetails() {
		Query findAll = entityManager.createNamedQuery("InsuredPedDetails.findAll");
		@SuppressWarnings("unchecked")
		List<InsuredPedDetails> pedList = (List<InsuredPedDetails>) findAll.getResultList();
		return pedList;
	}

	/**
	 * 
	 * Method to retrieve value for type combo box (Type displayed in Preauth
	 * related menus) based on menu value
	 * */

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getType(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager.createNamedQuery("PreauthType.findByKey");
		query = query.setParameter("parentKey", a_key);
		List<PreauthType> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		if (!mastersValueList.isEmpty()) {
			SelectValue selectValue = null;
			for (PreauthType preauthType : mastersValueList) {
				selectValue = new SelectValue();
				// selectValue.setId(preauthType.getStage().getKey().longValue());
				selectValue.setId(preauthType.getKey().longValue());
				selectValue.setValue(preauthType.getPreauthStatus());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public String getSelectedProcedure(Long selectedProcedureId) {
		// Need to implement the named query in the claimsublimit. Java after
		// creating the JPA by lakshmi
		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<ClaimSubLimit> resultSelectedProcedureList = (List<ClaimSubLimit>) findAll
		.getResultList();
		return resultSelectedProcedureList.get(0).toString();
	}

	/**
	 * 
	 * Method to retrieve value for reffered by combo box present in Submit
	 * specialist advise menu
	 * */

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRefferedByDocList() {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		if (!mastersValueList.isEmpty()) {
			SelectValue selectValue = null;
			for (TmpEmployee tmpEmp : mastersValueList) {
				selectValue = new SelectValue();
				// selectValue.setId(preauthType.getStage().getKey().longValue());
				selectValue.setId(tmpEmp.getKey());
				selectValue.setValue(tmpEmp.getEmployeeWithNames());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	public ProcedureMaster getProcedureByKey(Long key) {
		ProcedureMaster procedureMaster = entityManager.find(
				ProcedureMaster.class, key);
		return procedureMaster;
	}


	@SuppressWarnings("unchecked")
	public List<HospitalPackage> getPackageRateByProcedureAndHospitalKey(
			String procedureCode, String hospitalCode,Long roomCategoryId) {
		Query query = entityManager
				.createNamedQuery("HospitalPackage.findByHospitalAndProcedure");
		query.setParameter("procedureCode", procedureCode);
		query.setParameter("hosptialCode", hospitalCode.toLowerCase());
		query.setParameter("roomCategoryId", roomCategoryId);
		try{
			List<HospitalPackage> resultList = (List<HospitalPackage>) query
					.getResultList();
			return resultList;
		}catch(Exception e){

		}
		return null;
	}

	public BeanItemContainer<TmpInvestigation> getInvestigation() {
		Query findAll = entityManager
				.createNamedQuery("TmpInvestigation.findAll");
		@SuppressWarnings("unchecked")
		List<TmpInvestigation> investigationList = (List<TmpInvestigation>) findAll
		.getResultList();
		//List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<TmpInvestigation> selectValueContainer = new BeanItemContainer<TmpInvestigation>(
				TmpInvestigation.class);
		if (!investigationList.isEmpty()) {
			// for (TmpInvestigation investigation : investigationList) {
			// SelectValue selectValue = new SelectValue();
			// //selectValue.setId(preauthType.getStage().getKey().longValue());
			// selectValue.setId(new Long(investigation.getInvestigatorCode()));
			// selectValue.setValue(investigation.getInvestigatorName());
			// selectValueList.add(selectValue);
			// }
			selectValueContainer.addAll(investigationList);
		}
		return selectValueContainer;

	}

	public BeanItemContainer<SelectValue> getInvestigationForPA() {
		Query findAll = entityManager
				.createNamedQuery("TmpInvestigation.findAll");
		@SuppressWarnings("unchecked")
		List<TmpInvestigation> investigationList = (List<TmpInvestigation>) findAll
		.getResultList();
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!investigationList.isEmpty()) {
			for (TmpInvestigation tmpInvestigation : investigationList) {
				SelectValue selValue = new SelectValue();
				selValue.setValue(tmpInvestigation.getInvestigatorName());
				String id = getIdFromString(tmpInvestigation.getInvestigatorCode());
				if(null != id)
					selValue.setId(Long.valueOf(id));
				selectValueList.add(selValue);

			}
			// for (TmpInvestigation investigation : investigationList) {
			// SelectValue selectValue = new SelectValue();
			// //selectValue.setId(preauthType.getStage().getKey().longValue());
			// selectValue.setId(new Long(investigation.getInvestigatorCode()));
			// selectValue.setValue(investigation.getInvestigatorName());
			// selectValueList.add(selectValue);
			// }
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;

	}

	public  String getIdFromString(String InvestigatorId)
	{
		if(null != InvestigatorId)
		{
			String arr[] = InvestigatorId.split("-");
			return arr[1];
		}
		return null;
	}


	public String getDiagnosis(Long key) {
		Diagnosis diagnosis = entityManager.find(Diagnosis.class, key);
		return diagnosis != null ? diagnosis.getValue() : "";
	}

	public String getDiagnosis(Long key, EntityManager entitymanager) {
		this.entityManager = entitymanager;
		Diagnosis diagnosis = entityManager.find(Diagnosis.class, key);
		return diagnosis != null ? diagnosis.getValue() : "";
	}


	/**
	 * Method to retrieve data from MAS_DOCUMENT_COMPONENT table.
	 * 
	 * Used by Acknowledge Document Received Screen.
	 * 
	 * **/


	public List<DocumentCheckListMaster> getDocumentCheckListValues() 
	{//(EntityManager entitymanager) {

		Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findAll");
		List<DocumentCheckListMaster> resultList = (List<DocumentCheckListMaster>) query
				.getResultList();
		return resultList;
	}

	public List<DocumentCheckListMaster> getDocumentCheckListValuesByType(String strType) 
	{//(EntityManager entitymanager) {

		/*Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterType"); findByMasterTypeAndMandatoryRecord*/
		Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterTypeAndMandatoryRecord"); 
		query.setParameter("masterType", strType);
		List<DocumentCheckListMaster> resultList = (List<DocumentCheckListMaster>) query
				.getResultList();


		return resultList;
	}

	public List<DocumentCheckListMaster> getPADocumentCheckListValuesByType(String strType) 
	{//(EntityManager entitymanager) {

		/*Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterType"); findByMasterTypeAndMandatoryRecord*/
		Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterTypeAndMandatoryRecord"); 
		query.setParameter("masterType", strType);
		List<DocumentCheckListMaster> resultList = (List<DocumentCheckListMaster>) query
				.getResultList();


		return resultList;
	}

	public List<DocumentCheckListMaster> getPADocCheckListForBenefit(String strType,String benefitName)
	{
		List<DocumentCheckListMaster> resultList = new ArrayList<DocumentCheckListMaster>();
		try{
			Query query = entityManager
					.createNamedQuery("DocumentCheckListMaster.findMandatoryDocByBenefitType"); 
			query.setParameter("masterType", strType);
			query.setParameter("benefitType",benefitName.toLowerCase());
			resultList = (List<DocumentCheckListMaster>) query
					.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return resultList;
	}	
	public BeanItemContainer<SelectValue> getDocumentCheckListValuesContainer(String strType) 
	{//(EntityManager entitymanager) {

		Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterType");
		/*Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterTypeAndMandatoryRecord"); */
		query.setParameter("masterType", strType);
		List<DocumentCheckListMaster> resultList = (List<DocumentCheckListMaster>) query
				.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!resultList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (DocumentCheckListMaster document : resultList) {
				selectValue = new SelectValue();
				selectValue.setId(document.getKey().longValue());
				selectValue.setValue(document.getValue());
				selectValueList.add(selectValue);
				selectValue = null;
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getOMPDocumentCheckListValuesContainer(String strType) 
	{//(EntityManager entitymanager) {

		Query query = entityManager
				.createNamedQuery("OMPDocumentMaster.findByType");
		/*Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByMasterTypeAndMandatoryRecord"); */
		query.setParameter("type", strType);
		List<OMPDocumentMaster> resultList = (List<OMPDocumentMaster>) query
				.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!resultList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (OMPDocumentMaster document : resultList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(document.getKey().longValue());
				selectValue.setValue(document.getOmpDcfDescription());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getParticularsByBenefit(String lobFlag,String coverValue) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
		.createCriteria(DocumentCheckListMaster.class)
		.add(Restrictions.eq("masterType", lobFlag))
		.add(Restrictions.eq("benefitType", coverValue.toUpperCase()))
		.add(Restrictions.eq("activeStatus", 1l))
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SpecialSelectValue> getRevisedParticularsByBenefit(String lobFlag,String coverValue) {

		Session session = (Session) entityManager.getDelegate();

		Criteria createCriteria = session.createCriteria(DocumentCheckListMaster.class);

		if(coverValue != null && coverValue.equalsIgnoreCase(SHAConstants.HOSPITALIZATION)){
			createCriteria.addOrder(Order.desc("key"));
		}

		@SuppressWarnings("unchecked")
		List<SpecialSelectValue> selectValuesList = createCriteria
		.add(Restrictions.eq("masterType", lobFlag))
		.add(Restrictions.eq("benefitType", coverValue.toUpperCase()))
		.add(Restrictions.eq("activeStatus", 1l))
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value")
				.add(Projections.property("mandatoryDocFlag"),"commonValue"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SpecialSelectValue.class)).list();
		BeanItemContainer<SpecialSelectValue> selectValueContainer = new BeanItemContainer<SpecialSelectValue>(
				SpecialSelectValue.class);
		for (SpecialSelectValue selectValue : selectValuesList) {
			selectValue.setSpecialValue(coverValue);
		}
		selectValueContainer.addAll(selectValuesList);
		if(coverValue != null && coverValue.equalsIgnoreCase(SHAConstants.HOSPITALIZATION)){
			selectValueContainer.sort(new Object[] {"value"}, new boolean[] {false});		
		}
		return selectValueContainer;
	}


	/**
	 * Method to retrieve data from IMS_CLS_ROD_DOCUMENT_LIST table
	 * based on document acknowledge object 
	 * 
	 * */
	public List<RODDocumentCheckList> getRODDocumentListValues(DocAcknowledgement objDocAck)
	{

		Query query = entityManager
				.createNamedQuery("RODDocumentCheckList.findByDocKey");

		query.setParameter("docKey", objDocAck.getKey());
		List<RODDocumentCheckList> rodDocCheckList = query.getResultList();
		List<RODDocumentCheckList> finalRodDocCheckList = new ArrayList<RODDocumentCheckList>();
		for (RODDocumentCheckList rodDocumentCheckList : rodDocCheckList) {
			entityManager.refresh(rodDocumentCheckList);
			finalRodDocCheckList.add(rodDocumentCheckList);
		}
		return finalRodDocCheckList ;

	}


	/**
	 * Method to retrieve data from IMS_CLS_REIMBURSEMENT table
	 * based on document acknowledge object 
	 * 
	 * */
	public List<Reimbursement> getReimbursementDetails(Long claimKey)
	{
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByClaimKey");
		query.setParameter("claimKey", claimKey);
		List<Reimbursement> reimbursementDetails = query.getResultList();
		if(null != reimbursementDetails && !reimbursementDetails.isEmpty())
		{
			for (Reimbursement reimbursement : reimbursementDetails) {
				entityManager.refresh(reimbursement);
			}

		}
		return reimbursementDetails ;
	}

	public Reimbursement getReimbursementDetailsByKey(Long rodKey)
	{
		Query query = entityManager
				.createNamedQuery("Reimbursement.findByKey");
		query.setParameter("primaryKey", rodKey);
		List<Reimbursement> reimbursementDetails = query.getResultList();
		if(null != reimbursementDetails && !reimbursementDetails.isEmpty())
		{
			for (Reimbursement reimbursement : reimbursementDetails) {
				entityManager.refresh(reimbursement);
			}	
			return reimbursementDetails.get(0);
		}
		return null ;
	}

	/**
	 * Method to retrieve data from IMS_CLS_DOC_ACKNOWLEDGEMENT table
	 * based on document acknowledge object 
	 * 
	 * */
	public List<DocAcknowledgement> getDocumentAcknowledgeByClaim(Claim claimKey)
	{
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByClaimKey");
		query.setParameter("claimkey", claimKey.getKey());
		List<DocAcknowledgement> reimbursementDetails = query.getResultList();
		//		List<DocAcknowledgement> docAckList = new ArrayList<DocAcknowledgement>();
		//		for (DocAcknowledgement docAcknowledgement : reimbursementDetails) {
		//			entityManager.refresh(docAcknowledgement);
		//			docAckList.add(docAcknowledgement);
		//		}
		return reimbursementDetails ;
	}

	public List<DocAcknowledgement> getLatestDocumentAcknowledgeByClaim(Claim claimKey)
	{
		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByLatestClaimKey");
		query.setParameter("claimkey", claimKey.getKey());
		List<DocAcknowledgement> reimbursementDetails = query.getResultList();
		List<DocAcknowledgement> docAckList = new ArrayList<DocAcknowledgement>();
		for (DocAcknowledgement docAcknowledgement : reimbursementDetails) {
			entityManager.refresh(docAcknowledgement);
			docAckList.add(docAcknowledgement);
		}
		return docAckList ;
	}

	public List<MasProcedureDaycareMapping> getProcedureDaycareMapping(
			Long productKey) {
		Query query = entityManager
				.createNamedQuery("MasProcedureDaycareMapping.findByProduct");
		query.setParameter("productKey", productKey);
		List<MasProcedureDaycareMapping> resultList = (List<MasProcedureDaycareMapping>) query
				.getResultList();
		return resultList;
	}

	// The InsuredPEDDetails has been deprecated, but the appropriate changes
	// didn't made in this class. So I have changed the code and the old code
	// has been commented below

	/*
	 * @SuppressWarnings({ "unchecked", "unused" }) public
	 * List<InsuredPEDDetails> getInusredPEDList(String insuredKey ) { Query
	 * findAll =
	 * entityManager.createNamedQuery("InsuredPEDDetails.findByInsuredKey");
	 * findAll.setParameter("insuredKey", new Long(insuredKey));
	 * List<InsuredPEDDetails> insuredPEDList = findAll.getResultList(); return
	 * insuredPEDList; }
	 */

	@SuppressWarnings({ "unchecked" })
	public List<InsuredPedDetails> getInusredPEDList(String insuredKey) {
		Query findAll = entityManager
				.createNamedQuery("InsuredPedDetails.findByinsuredKey");
		findAll.setParameter("insuredKey", new Long(insuredKey));
		List<InsuredPedDetails> insuredPEDList = findAll.getResultList();
		return insuredPEDList;
	}

	public SelectValue addReasonForAdmission(String reason) {
		ReasonForAdmission admission = new ReasonForAdmission();
		admission.setValue(reason);
		//		admission.setVersion(1);
		entityManager.persist(admission);
		return new SelectValue(admission.getKey(), admission.getValue());
	}

	public SelectValue addDiagnosis(String query) {
		Diagnosis dia = new Diagnosis();
		dia.setValue(query);
		dia.setActiveStatus(1l);
		dia.setCreatedBy("IMS");
		entityManager.persist(dia);
		entityManager.flush();

		return new SelectValue(dia.getKey(), dia.getValue());
	}
	
   	public SelectValue addProcedure(String query,SelectValue specialityType) {
		ProcedureSpecialityMaster prodMaster = new ProcedureSpecialityMaster();
		prodMaster.setProcedureName(query);
		prodMaster.setSpecialityKey(specialityType.getId());
		prodMaster.setSpecialityName(specialityType.getValue());
		prodMaster.setActiveStatus(1l);
		prodMaster.setCreatedBy("IMS");
		prodMaster.setTransactionFlag("U");
		entityManager.persist(prodMaster);
		entityManager.flush();
		
		return new SelectValue(prodMaster.getKey(),prodMaster.getProcedureName());	
	}

	@SuppressWarnings("unchecked")
	public Boolean searchDiagnosisByValue(String value){

		Query findAll = entityManager
				.createNamedQuery("Diagnosis.findByName");
		findAll.setParameter("diagnosisString", value.toLowerCase());
		List<Diagnosis> diagList = findAll.getResultList();
		if(diagList == null || diagList.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}
	
	public Boolean searchProcedureByValue(String procedureValue){

		Query findAll = entityManager
				.createNamedQuery("ProcedureSpecialityMaster.findByProcedureName");
		findAll.setParameter("procedureName", procedureValue.toLowerCase());
		List<ProcedureSpecialityMaster> procdList = findAll.getResultList();
		if(procdList == null || procdList.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Method added for retreiving select value container for product. Specific
	 * to Policy Search.
	 * */
	public BeanItemContainer<SelectValue> getSelectValueContainerForProduct() {
		Query findAll = entityManager.createNamedQuery("Product.findAll");
		List<Product> productList = (List<Product>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!productList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (Product product : productList) {
				selectValue = new SelectValue();
				selectValue.setId(product.getKey().longValue());
				selectValue.setValue(product.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getSelectValueContainerForProductNameAndCode(String lobType) {
		Query findAll = entityManager.createNamedQuery("Product.findByLineOfBusiness");
		findAll = findAll.setParameter("lineOfBusiness", lobType);
		List<Product> productList = (List<Product>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!productList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (Product product : productList) {
				SelectValue selectValue = new SelectValue();
				selectValue.setId(product.getKey().longValue());
				selectValue.setValue(product.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SpecialSelectValue> getContainerForProduct() {
		Query findAll = entityManager.createNamedQuery("Product.findUnique");
		List<Object> productList = (List<Object>) findAll.getResultList();
		BeanItemContainer<SpecialSelectValue> selectValueContainer = new BeanItemContainer<SpecialSelectValue>(
				SpecialSelectValue.class);
		if (!productList.isEmpty()) {
			List<SpecialSelectValue> selectValueList = new ArrayList<SpecialSelectValue>();
			Long i=0l;
			SpecialSelectValue selectValue = null;
			for (Object product : productList) {

				selectValue = new SpecialSelectValue();

				if(product instanceof Object[]){
					Object[] ObjArray =(Object[]) product;
					Object objCode = ObjArray[0];
					Object objValue =ObjArray[1];
					Object objKey = ObjArray[2];
					if(objCode != null && objValue != null){
						selectValue.setId(Long.valueOf(objKey.toString()));
						selectValue.setValue(objValue.toString());
						selectValue.setSpecialValue(objValue.toString() + " - "+objCode.toString());
					}
					i++;
				}

				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SpecialSelectValue> getContainerForProductByLineOfBusiness(String lobType) {
		Query findAll = entityManager.createNamedQuery("Product.findUniquebyLineOfBusiness");
		findAll = findAll.setParameter("lineOfBusiness", lobType);
		List<Object> productList = (List<Object>) findAll.getResultList();
		BeanItemContainer<SpecialSelectValue> selectValueContainer = new BeanItemContainer<SpecialSelectValue>(
				SpecialSelectValue.class);
		if (!productList.isEmpty()) {
			List<SpecialSelectValue> selectValueList = new ArrayList<SpecialSelectValue>();
			Long i=0l;
			for (Object product : productList) {

				SpecialSelectValue selectValue = new SpecialSelectValue();

				if(product instanceof Object[]){
					Object[] ObjArray =(Object[]) product;
					Object objCode = ObjArray[0];
					Object objValue =ObjArray[1];
					Object objKey = ObjArray[2];
					if(objCode != null && objValue != null){
						selectValue.setId(Long.valueOf(objKey.toString()));
						selectValue.setValue(objValue.toString());
						selectValue.setSpecialValue(objValue.toString() + " - "+objCode.toString());
					}
					i++;
				}

				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	/**
	 * Method to retreive data from VW_HOSPITALS based on key
	 * */
	public Hospitals getHospitalDetails(Long key)
	{
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query = query.setParameter("key", key);

		List<Hospitals> result = (List<Hospitals>) query.getResultList();
		if(result != null && ! result.isEmpty()){
			return result.get(0);
		}

		return null;
	}

	/**
	 * Method to retreive data from MAS_BANK based on key
	 * */
	public BankMaster getBankDetails(String ifscCode)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByIfscCode");
		query = query.setParameter("ifscCode", ifscCode);
		List<BankMaster> masBank = (List<BankMaster>)query.getResultList();
		if(masBank != null && ! masBank.isEmpty()){
			return masBank.get(0);
		}
		return null;
	}

	public BankMaster getBankDetailsByKey(Long key)
	{
		Query query = entityManager.createNamedQuery("BankMaster.findByKey");
		query = query.setParameter("key", key);
		List<BankMaster> masBankList = query.getResultList();

		if(null != masBankList && !masBankList.isEmpty())
		{
			return masBankList.get(0);

		}
		return null;
	}


	/***
	 * Method to retreive data from MAS_BILL_CLASSIFICATION table.
	 * Used in Bill Entry screen. 
	 * */
	public BeanItemContainer<SelectValue> getMasBillClassificationValues() {

		Query findAll = entityManager.createNamedQuery("MasBillClassification.findAll");
		List<MasBillClassification> billClassificationList = (List<MasBillClassification>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!billClassificationList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MasBillClassification classification : billClassificationList) {
				selectValue = new SelectValue();
				selectValue.setId(classification.getKey().longValue());
				selectValue.setValue(classification.getValue());
				selectValueList.add(selectValue);
				selectValue = null;
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getMasBillClassificationValuesForBillEntry() {

		Query findAll = entityManager.createNamedQuery("MasBillClassification.findAll");
		List<MasBillClassification> billClassificationList = (List<MasBillClassification>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!billClassificationList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MasBillClassification classification : billClassificationList) {				
				if(null != classification && !classification.getKey().equals(11l))
				{
					selectValue = new SelectValue();
					selectValue.setId(classification.getKey().longValue());
					selectValue.setValue(classification.getValue());
					selectValueList.add(selectValue);
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	/***
	 * Method to retreive data from MAS_BILL_CLASSIFICATION table.
	 * Used in Bill Entry screen. 
	 * */
	public BeanItemContainer<SelectValue> getMasBillClassificationByValue(String Value) {

		Query findAll = entityManager.createNamedQuery("MasBillClassification.findByValue");
		List<MasBillClassification> billClassificationList = (List<MasBillClassification>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!billClassificationList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue  = null;
			for (MasBillClassification classification : billClassificationList) {
				selectValue = new SelectValue();
				selectValue.setId(classification.getKey().longValue());
				selectValue.setValue(classification.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}


	/***
	 * Method to retreive data from MAS_BILL_CLASSIFICATION table.
	 * Used in Bill Entry screen. 
	 * */
	public BeanItemContainer<SelectValue> getMasBillCategoryValues(Long key,SelectValue claimType) {

		Query query = entityManager.createNamedQuery("MasBillCategory.findByBillClassificationKey");
		query.setParameter("billClassificationkey",key);
		List<MasBillCategory> billCategoryList = (List<MasBillCategory>) query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!billCategoryList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue  = null;
			for (MasBillCategory billCategory : billCategoryList) {
				selectValue = new SelectValue();
				selectValue.setId(billCategory.getKey().longValue());
				selectValue.setValue(billCategory.getValue());
				if(claimType != null && claimType.getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					if(!billCategory.getKey().equals(ReferenceTable.DOMICILIARY_ID)){
						selectValueList.add(selectValue);
					}
				}else{
					selectValueList.add(selectValue);
				}

			}
			selectValueContainer.addAll(selectValueList);

		}

		return selectValueContainer;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getPADocumentType() {

		Query findAll = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		@SuppressWarnings("unchecked")
		List<MastersValue> resultPADocumentResultList = (List<MastersValue>) findAll
		.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(resultPADocumentResultList);		

		return resultContainer;

	}


	public BeanItemContainer<SelectValue> getMasBillCategoryValuesForZonalAndMedical(Long key,SelectValue claimType,Boolean isDomicillary) {

		Query query = entityManager.createNamedQuery("MasBillCategory.findByBillClassificationKey");
		query.setParameter("billClassificationkey",key);
		List<MasBillCategory> billCategoryList = (List<MasBillCategory>) query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!billCategoryList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MasBillCategory billCategory : billCategoryList) {
				selectValue = new SelectValue();
				selectValue.setId(billCategory.getKey().longValue());
				selectValue.setValue(billCategory.getValue());
				if(claimType != null && claimType.getId().equals(ReferenceTable.CASHLESS_CLAIM_TYPE_KEY)){
					if(!billCategory.getKey().equals(ReferenceTable.DOMICILIARY_ID) 
							&& !(null != isDomicillary && isDomicillary && ReferenceTable.getDomicillaryMap().containsKey(billCategory.getKey()))
							){
						selectValueList.add(selectValue);
					}
				}else if(!(null != isDomicillary && isDomicillary && ReferenceTable.getDomicillaryMap().containsKey(billCategory.getKey()))){
					selectValueList.add(selectValue);
				}
				/*else
				{
					selectValueList.add(selectValue);
				}*/

			}
			selectValueContainer.addAll(selectValueList);

		}

		return selectValueContainer;
	}


	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getTmpCpuCodeList() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getKey());
			select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;

	}
	
	//For PCC CPU Code
	
		@SuppressWarnings({ "unchecked"})
		public BeanItemContainer<SelectValue>  getPCCCpuCodeList(String userName,String roleCode) {

			Query query = entityManager.createNamedQuery("MasUserPCCRoleMappingDetails.findByUserRoleKey");
			query.setParameter("userID",userName);
			query.setParameter("roleCode",roleCode);
			
			@SuppressWarnings("unchecked")
			List<MasUserPCCRoleMappingDetails> resultCPUMappingCodeList = (List<MasUserPCCRoleMappingDetails>) query.getResultList();
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			SelectValue select = null;
			for (MasUserPCCRoleMappingDetails oneCpuRoleMap : resultCPUMappingCodeList) {
				
				TmpCPUCode tmpCpuCode = getMasCpuCode(oneCpuRoleMap.getCpuCode());

				if(tmpCpuCode !=null){
				select = new SelectValue();
				select.setId(tmpCpuCode.getKey());
				select.setValue(tmpCpuCode.getCpuCode().toString() + " - " + tmpCpuCode.getDescription());
				selectValuesList.add(select);
				}
				
				/*select = new SelectValue();
				select.setId(oneCpuRoleMap.getKey());
				select.setValue(oneCpuRoleMap.getCpuCode().toString());
				selectValuesList.add(select);*/
			}
			cpuCodeContainer.addAll(selectValuesList);

			cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

			return cpuCodeContainer;

		}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getOfficeCode() {
		//	Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByCpuCode");

		Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findAllOfficeCode");


		List<OrganaizationUnit> resultOfficeCodeList = (List<OrganaizationUnit>) findAll
				.getResultList();
		BeanItemContainer<SelectValue> officeCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (OrganaizationUnit organaizationUnit : resultOfficeCodeList) {
			select = new SelectValue();
			select.setId(Long.valueOf(organaizationUnit.getOrganizationUnitId()));
			select.setValue(organaizationUnit.getOrganizationUnitId());
			officeCodeContainer.addBean(select);
		}
		/*	Iterator officeIterator = resultOfficeCodeList.iterator();
		for (;officeIterator.hasNext();) {

			SelectValue select = new SelectValue();
			Object[] orgObj = (Object[]) officeIterator.next();

			select.setValue((String)orgObj[0]);
			//select.setId(Long.valueOf((String)orgObj[1]));
		 */			//officeCodeContainer.addBean(select);
		//	}

		return officeCodeContainer;


	}



	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getOfficeCodeByCpuCode(String cpuCode) {



		BeanItemContainer<SelectValue> officeCodeContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		if(null != cpuCode)
		{
			Query findAll = entityManager.createNamedQuery("OrganaizationUnit.findByCpuCode");
			findAll.setParameter("cpuCode", cpuCode);

			List<OrganaizationUnit> resultOfficeCodeList = (List<OrganaizationUnit>) findAll
					.getResultList();

			if(resultOfficeCodeList != null && !resultOfficeCodeList.isEmpty()){		

				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue select = null;
				for (OrganaizationUnit organaizationUnit : resultOfficeCodeList) {			
					select = new SelectValue();
					select.setId(Long.valueOf(organaizationUnit.getOrganizationUnitId()));
					select.setValue(organaizationUnit.getOrganizationUnitId());
					selectValueList.add(select);

				}
				officeCodeContainer.addAll(selectValueList);
			}

		}
		return officeCodeContainer;

	}


	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getTmpCpuCodeListWithoutDescription() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getKey());
			select.setValue(cpuCode.getCpuCode().toString());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;

	}
	public List<SelectValue> getTmpCpuCodeListWithoutDescription(EntityManager em){
		entityManager = em;
		BeanItemContainer<SelectValue> cpuItemCont = getTmpCpuCodeListWithoutDescription();
		return cpuItemCont.getItemIds();
	}
	
	public List<MasOmbudsman> getOmbudsmanDetailsByCpuCode(String ombudsManCode, EntityManager em){
		entityManager = em;
		
		return getOmbudsmanDetailsByCpuCode(ombudsManCode);
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getNonKeralaTmpCpuCodeList() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			if(!("Kerala".equalsIgnoreCase(cpuCode.getDescription())))
			{
				select = new SelectValue();
				select.setId(cpuCode.getKey());

				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;

	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getEmpTypeContainer(){
		BeanItemContainer<SelectValue> claimTypeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		try{
			Query findAll = entityManager.createNamedQuery("MastersValue.findByMasterListKeyWithoutOrder");

			findAll.setParameter("parentKey", ReferenceTable.EMP_TYPE);

			List<MastersValue> resultList = (List<MastersValue>) findAll
					.getResultList();
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			SelectValue select = null;
			for (MastersValue empType : resultList) {
				select = new SelectValue(empType.getKey(),empType.getValue());
				selectValuesList.add(select);
			}
			claimTypeContainer.addAll(selectValuesList);

			claimTypeContainer.sort(new Object[] {"value"}, new boolean[] {true});
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return claimTypeContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getEmpContainerByType(String empType){
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if(empType != null){

			try{
				Query findEmpByTypeQuery = entityManager.createNamedQuery("EmpSecUser.findEmpByUserType");
				findEmpByTypeQuery.setParameter("userType", empType.toLowerCase());
				List<EmpSecUser> employeeNameList = (List<EmpSecUser>) findEmpByTypeQuery.getResultList();

				if (!employeeNameList.isEmpty()) {
					List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue = null;
					for (EmpSecUser empUser : employeeNameList) {
						selectValue = new SelectValue();
						selectValue.setId(empUser.getKey().longValue());
						if(null != empUser.getUserId() && !empUser.getUserId().isEmpty())
						{
							selectValue.setValue(empUser.getUserId()+"-"+empUser.getUserName());
						}
						selectValueList.add(selectValue);
					}
					selectValueContainer.addAll(selectValueList);
				}

			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return selectValueContainer;

	}


	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getClaimtypeContainer(){
		Query findAll = entityManager.createNamedQuery("MastersValue.findByMasterListKeyWithoutOrder");

		findAll.setParameter("parentKey", ReferenceTable.CLAIM_TYPE);

		List<MastersValue> resultList = (List<MastersValue>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> claimTypeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue claimType : resultList) {

			if(!claimType.getKey().equals(ReferenceTable.OUT_PATIENT) && !claimType.getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){
				select = new SelectValue(claimType.getKey(),claimType.getValue());
				selectValuesList.add(select);
			}
		}
		claimTypeContainer.addAll(selectValuesList);

		claimTypeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return claimTypeContainer;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getTmpCpuDescriptionList() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			/**
			 * As discussed with lakshmi, cpucode replaced with cpu key
			 */
			SelectValue select = new SelectValue();
			select.setId(cpuCode.getKey());
			select.setValue(cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);
		return cpuCodeContainer;

	}
	
	
	// For Role Based change in Qry Reply Screen to get Emp Ids
	public BeanItemContainer<SelectValue> getAuditQryReplyUser(String userRole, String screenName) {
		
		BeanItemContainer<SelectValue> auditQryRplUserContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		try{
			Query findEmpByTypeQuery = null;
			if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_CLS_QRY_REPLY_SCREEN)) {
				
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findClsQryRplUserByRoleClmProcessType");
				findEmpByTypeQuery.setParameter("clmType", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.MEDICAL.toLowerCase());
			}
			else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN)) {
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findQryRplUserIdByRoleClmProcessType");
				findEmpByTypeQuery.setParameter("clmType1", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmType2", SHAConstants.REIMBURSEMENT.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.MEDICAL.toLowerCase());
				
			}
			else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN)) {
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findQryRplUserIdByRoleClmProcessType");
				findEmpByTypeQuery.setParameter("clmType1", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmType2", SHAConstants.REIMBURSEMENT.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.FA.toLowerCase());
				
			}
			
			if(findEmpByTypeQuery != null)
			{
				findEmpByTypeQuery.setParameter("roleName", userRole.toLowerCase());
				List<MasClmAuditUserMapping> employeeNameList = (List<MasClmAuditUserMapping>) findEmpByTypeQuery.getResultList();
				List<SelectValue> auditQryRplUserList = new ArrayList<SelectValue>();
				if(employeeNameList != null && !employeeNameList.isEmpty()) {
					SelectValue auditQryRplUser = null;
					for (MasClmAuditUserMapping masClmAuditUserMapping : employeeNameList) {
						auditQryRplUser = new SelectValue(new Long(employeeNameList.indexOf(masClmAuditUserMapping)+1), (masClmAuditUserMapping.getUserId()+"-"+masClmAuditUserMapping.getUserName()), masClmAuditUserMapping.getUserId());
						auditQryRplUser.setRoleName(masClmAuditUserMapping.getUserRole());					
						auditQryRplUserContainer.addBean(auditQryRplUser);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
		return auditQryRplUserContainer;
		
		
	}

	public SearchCVCAuditClsQryFormDTO getAuditReplyUserRoleByUserIdForRplScreen(String userId, String screenName) {

		SearchCVCAuditClsQryFormDTO searchFrmDto = new  SearchCVCAuditClsQryFormDTO();
		
		try{
			Query findEmpByTypeQuery = null;
			if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_CLS_QRY_REPLY_SCREEN)) {
				
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findByUserIdClmTypeClmProcessType");
				findEmpByTypeQuery.setParameter("clmType", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.MEDICAL.toLowerCase());
			}
			else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_MEDICAL_QRY_REPLY_SCREEN)) {
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findFaRoleByClmProcessType");
				findEmpByTypeQuery.setParameter("clmType1", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmType2", SHAConstants.REIMBURSEMENT.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.MEDICAL.toLowerCase());
				
			}
			else if(screenName != null && screenName.equalsIgnoreCase(SHAConstants.AUDIT_BILLING_FA_QRY_REPLY_SCREEN)) {
				findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findFaRoleByClmProcessType");
				findEmpByTypeQuery.setParameter("clmType1", SHAConstants.CASHLESS.toLowerCase());
				findEmpByTypeQuery.setParameter("clmType2", SHAConstants.REIMBURSEMENT.toLowerCase());
				findEmpByTypeQuery.setParameter("clmProcessType", SHAConstants.FA.toLowerCase());
				
			}
			
			if(findEmpByTypeQuery != null)
			{
				findEmpByTypeQuery.setParameter("userId", userId.toLowerCase());
				List<MasClmAuditUserMapping> employeeNameList = (List<MasClmAuditUserMapping>) findEmpByTypeQuery.getResultList();
				List<SelectValue> userCpuList = new ArrayList<SelectValue>();
				if(employeeNameList != null && !employeeNameList.isEmpty()) {
					for (MasClmAuditUserMapping masClmAuditUserMapping : employeeNameList) {
						TmpCPUCode zoneName =getMasCpuCode(masClmAuditUserMapping.getCpuCode());
						searchFrmDto.setUsername((masClmAuditUserMapping.getUserId()+"-"+masClmAuditUserMapping.getUserName()));
						searchFrmDto.setUserRoleName(masClmAuditUserMapping.getUserRole());
						SelectValue userCpu = new SelectValue(zoneName.getCpuCode(),zoneName.getCpuCode()+"-"+zoneName.getDescription());
						userCpuList.add(userCpu);
						
					}
					BeanItemContainer<SelectValue> cpuContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
					cpuContainer.addAll(userCpuList);
					searchFrmDto.setCpuListContainer(cpuContainer);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
		return searchFrmDto;
		
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getMasterValueByReference(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			if(a_key.equals(ReferenceTable.HOSPITAL_TYPE)){

				if(! value.getKey().equals(ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID)){
					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);
				}
			}else if(a_key.equals(ReferenceTable.CLAIM_TYPE)){
				if(!value.getKey().equals(ReferenceTable.OUT_PATIENT) && !value.getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}
			else{
				//SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue().toString());
				selectValuesList.add(select);
			}
			select = null;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getMasterValueByReferenceForRRCCategory(String code) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKeyCodewithCondition");
		query = query.setParameter("code", code);
		List<MastersValue> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			if(code.equals(ReferenceTable.HOSPITAL_TYPE)){

				if(! value.getKey().equals(ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID)){
					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);
				}
			}else if(code.equals(ReferenceTable.CLAIM_TYPE)){
				if(!value.getKey().equals(ReferenceTable.OUT_PATIENT) && !value.getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}
			else{
				//SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue().toString());
				selectValuesList.add(select);
			}
			select = null;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getParameterTypeFromMaster(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			if(a_key.equals(ReferenceTable.HOSPITAL_TYPE)){

				if(! value.getKey().equals(ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID)){
					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);
				}
			}else if(a_key.equals(ReferenceTable.CLAIM_TYPE)){
				if(!value.getKey().equals(ReferenceTable.OUT_PATIENT) && !value.getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}
			else{
				//SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue().toString());
				selectValuesList.add(select);
			}
			select = null;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getMasterValueByReferenceForNonAllopathic(String a_key, String treatmentType) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			if(a_key.equals(ReferenceTable.HOSPITAL_TYPE)){

				if(! value.getKey().equals(ReferenceTable.NOT_REGISTERED_HOSPITAL_TYPE_ID)){
					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);
				}
			}else if(a_key.equals(ReferenceTable.CLAIM_TYPE)){
				if(!value.getKey().equals(ReferenceTable.OUT_PATIENT) && !value.getKey().equals(ReferenceTable.HEALTH_CHECK_UP)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}else if(null != treatmentType && treatmentType.equalsIgnoreCase(ReferenceTable.ALLOPATHIC)){
				if(!value.getKey().equals(ReferenceTable.AYURVEDA_KEY)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}else if(null != treatmentType && treatmentType.equalsIgnoreCase(ReferenceTable.NON_ALLOPATHIC)){
				if(value.getKey().equals(ReferenceTable.AYURVEDA_KEY)){

					//SelectValue select = new SelectValue();
					select.setId(value.getKey());
					select.setValue(value.getValue().toString());
					selectValuesList.add(select);

				}
			}
			else{
				//SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getValue().toString());
				selectValuesList.add(select);
			}
			select = null;
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getStatusByStage(Long a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("Status.findByStageKey");
		query = query.setParameter("stageKey", a_key);
		List<Status> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (Status value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getProcessValue());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
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

	@SuppressWarnings({ "unchecked", "unused" })
	public Status getStatusByKey(Long a_key) {
		Status statusObj = null;
		try{
			if(a_key != null){
				Query query = entityManager
						.createNamedQuery("Status.findByKey");
				query = query.setParameter("statusKey", a_key);
				List<Status> mastersValueList = query.getResultList();
				if(mastersValueList != null && !mastersValueList.isEmpty()){
					for (Status value : mastersValueList) {
						entityManager.refresh(value);
						statusObj = value;
					}
				}
				mastersValueList = null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return statusObj;
	}




	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getMasterValueByReferenceForRRCEmployeeCredit(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKeyWithoutOrder");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue().toString());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getProductCodeName() {
		Query findAll = entityManager.createNamedQuery("Product.findAll");
		List<Product> productList = (List<Product>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> productContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (Product value : productList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue().toString());
			selectValuesList.add(select);
		}
		productContainer.addAll(selectValuesList);

		return productContainer;
	}


	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getIRDALevel1Values() {

		Query findAll = entityManager.createNamedQuery("MasIrdaLevel1.findAll");
		@SuppressWarnings("unchecked")
		List<MasIrdaLevel1> irdaLevel1List = (List<MasIrdaLevel1>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> irdaLevel1ValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MasIrdaLevel1 irdaLevel1 : irdaLevel1List) {
			select = new SelectValue();
			select.setId(irdaLevel1.getKey());
			select.setValue(irdaLevel1.getIrdaValue());
			selectValuesList.add(select);
		}
		irdaLevel1ValueContainer.addAll(selectValuesList);
		return irdaLevel1ValueContainer;	
	}

	public SelectValue getIRDALevel1ValueByKey(Long key){

		Query query = entityManager.createNamedQuery("MasIrdaLevel1.findByKey");
		query = query.setParameter("primaryKey", key);

		MasIrdaLevel1 masterValue = (MasIrdaLevel1)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}

		return selectValue;

	}

	public SelectValue getIRDALevel2ValueByKey(Long key){

		Query query = entityManager.createNamedQuery("MasIrdaLevel2.findByKey");
		query = query.setParameter("primaryKey", key);

		MasIrdaLevel2 masterValue = (MasIrdaLevel2)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}

		return selectValue;

	}

	public SelectValue getIRDALevel3ValueByKey(Long key){

		Query query = entityManager.createNamedQuery("MasIrdaLevel3.findByKey");
		query = query.setParameter("primaryKey", key);

		MasIrdaLevel3 masterValue = (MasIrdaLevel3)query.getSingleResult();
		SelectValue selectValue = null;
		if(masterValue != null){
			selectValue = new SelectValue();
			selectValue.setId(masterValue.getKey());
			selectValue.setValue(masterValue.getIrdaValue());
		}

		return selectValue;

	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getIRDALevel2Values(Long irdaLevel1Key) {

		Query query = entityManager.createNamedQuery("MasIrdaLevel2.findByIrdaLevel1Key");
		query = query.setParameter("irdaLevel1Key", irdaLevel1Key);
		@SuppressWarnings("unchecked")
		List<MasIrdaLevel2> irdaLevel2List = (List<MasIrdaLevel2>) query
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> irdaLevel2ValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MasIrdaLevel2 irdaLevel2 : irdaLevel2List) {
			select = new SelectValue();
			select.setId(irdaLevel2.getKey());
			select.setValue(irdaLevel2.getIrdaValue());
			selectValuesList.add(select);
		}
		irdaLevel2ValueContainer.addAll(selectValuesList);
		return irdaLevel2ValueContainer;	
	}


	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getIRDALevel3Values(Long irdaLevel2Key) {

		Query query = entityManager.createNamedQuery("MasIrdaLevel3.findByIrdaLevel2Key");
		query = query.setParameter("irdaLevel2Key", irdaLevel2Key);
		@SuppressWarnings("unchecked")
		List<MasIrdaLevel3> irdaLevel3List = (List<MasIrdaLevel3>) query
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> irdaLevel2ValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MasIrdaLevel3 irdaLevel3 : irdaLevel3List) {
			select = new SelectValue();
			select.setId(irdaLevel3.getKey());
			select.setValue(irdaLevel3.getIrdaValue());
			selectValuesList.add(select);
		}
		irdaLevel2ValueContainer.addAll(selectValuesList);
		return irdaLevel2ValueContainer;	
	}
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMasterByValue(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		a_key = a_key.toLowerCase();
		//		a_key = a_key.substring(0,1).toUpperCase()+a_key.substring(1);
		MastersValue a_mastersValue = null;
		if (a_key != null) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByValue");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}


	public MastersValue getMasterByValueForPremia(String a_key , EntityManager entityManager)
	{
		this.entityManager = entityManager;
		return getMasterByValue(a_key);
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMasterByValueAndMasList(String value,String masListKey) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");

		//		a_key = a_key.substring(0,1).toUpperCase()+a_key.substring(1);
		MastersValue a_mastersValue = null;
		if (value != null) {
			value = value.toLowerCase();
			Query query = entityManager
					.createNamedQuery("MastersValue.findValueByMasListKey");
			query = query.setParameter("parentKey", value);
			query = query.setParameter("masListKey", masListKey);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
	}

	public MastersValue getMasterByValueAndMasListForPremia (String value,String masListKey,EntityManager entityManager) {
		this.entityManager = entityManager;
		return getMasterByValueAndMasList(value, masListKey);
	}

	public Product getProductByCodeAndType(String productCode, String productType){

		productType = productType.toLowerCase();

		Query query = entityManager
				.createNamedQuery("Product.findByProductType");
		query = query.setParameter("productCode", productCode);
		query = query.setParameter("productType", productType);

		List<Product> productList = (List<Product>) query.getResultList();

		if(productList != null && ! productList.isEmpty()){
			return productList.get(0);
		}
		return null;

	}

	public Product getProductByCodeAndTypeForPremia(String productCode, String productType,EntityManager entityManager){
		this.entityManager = entityManager;
		return getProductByCodeAndType(productCode , productType);
	}

	public BidiMap loadEmployeeNameData()
	{
		BidiMap employeeNameMap = new DualHashBidiMap();
		//Map<String,String> employeeNameMap = new HashMap<String, String>();
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		//BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			//List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			StringBuffer strName = null;
			for (TmpEmployee employee : employeeNameList) {
				strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				employeeNameMap.put(strName , employee.getEmpId());
			}

		}
		return employeeNameMap;

	}

	public BeanItemContainer<SelectValue> getEmployeeNameFromMaster()
	{
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			StringBuffer strName = null;
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeNameList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey().longValue());
				strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				selectValue.setValue(strName.toString());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}


	public BeanItemContainer<SelectValue> getClaimStageContainer(){

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		//		try{
		//		
		//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		//		
		//		final CriteriaQuery<Stage> stageCriteriaQuery = builder
		//				.createQuery(Stage.class);
		//
		//		Root<Stage> stageRoot = stageCriteriaQuery
		//				.from(Stage.class);

		//		List<Long> claimStageList = new ArrayList<Long>();
		//		claimStageList.add(ReferenceTable.PROCESS_REJECTION_STAGE);
		//		claimStageList.add(ReferenceTable.PRE_MEDICAL_PRE_AUTH_STAGE);
		//		claimStageList.add(ReferenceTable.PRE_MEDICAL_ENHANCEMENT_STAGE);
		//		claimStageList.add(ReferenceTable.PREAUTH_STAGE);
		//		claimStageList.add(ReferenceTable.ENHANCEMENT_STAGE);
		//		claimStageList.add(ReferenceTable.ENHANCEMENT_DOWNSIZE_STAGE);
		//		claimStageList.add(ReferenceTable.ENHANCEMENT_WITHDRAW_STAGE);
		//		claimStageList.add(ReferenceTable.ACKNOWLEDGE_STAGE_KEY);
		//		claimStageList.add(ReferenceTable.CREATE_ROD_STAGE_KEY);
		//		claimStageList.add(ReferenceTable.BILL_ENTRY_STAGE_KEY);
		//		claimStageList.add(ReferenceTable.ZONAL_REVIEW_STAGE);
		//		claimStageList.add(ReferenceTable.CLAIM_REQUEST_STAGE);
		//		claimStageList.add(ReferenceTable.BILLING_STAGE);
		//		claimStageList.add(ReferenceTable.FINANCIAL_STAGE);
		//		
		//		Predicate stageKeyPredicate = stageRoot.<Long>get("key").in(claimStageList);
		//		
		//		List<Predicate> predicates = new ArrayList<Predicate>();
		//		predicates.add(stageKeyPredicate);
		//		
		//		stageCriteriaQuery.select(stageRoot).where(
		//				builder.and(predicates
		//						.toArray(new Predicate[] {})));
		//
		//		final TypedQuery<Stage> stageQuery = entityManager
		//				.createQuery(stageCriteriaQuery);
		//		List<Stage> stageList = stageQuery.getResultList();
		//		if(stageList != null && !stageList.isEmpty()){
		//			
		//			for(Stage stageObj : stageList){
		//				SelectValue stageSelectValue = new SelectValue();
		//				stageSelectValue.setId(stageObj.getKey());
		//				stageSelectValue.setValue(stageObj.getStageName() );
		//				selectValueContainer.addBean(stageSelectValue);
		//			}			
		//		}
		//			
		//		}
		//		catch(Exception e){
		//			e.printStackTrace();
		//		}
		//		return selectValueContainer;


		List<SelectValue>  clmStageNames = new ArrayList<SelectValue>();

		SelectValue clmStatusValue1 = new SelectValue();
		clmStatusValue1.setValue(SHAConstants.CLOSED_CLAIMS);
		clmStageNames.add(clmStatusValue1);
		SelectValue clmStatusValue2 = new SelectValue();
		clmStatusValue2.setValue(SHAConstants.REJECTED_CLAIMS);
		clmStageNames.add(clmStatusValue2);
		SelectValue clmStatusValue3 = new SelectValue();
		clmStatusValue3.setValue(SHAConstants.CLAIMS_PAID_STATUS);
		clmStageNames.add(clmStatusValue3);
		SelectValue clmStatusValue4 = new SelectValue();
		clmStatusValue4.setValue(SHAConstants.CLAIMS_BILL_RECEIVED_STATUS);
		clmStageNames.add(clmStatusValue4);
		SelectValue clmStatusValue5 = new SelectValue();
		clmStatusValue5.setValue(SHAConstants.CLAIMS_PRE_AUTH_STATUS);
		clmStageNames.add(clmStatusValue5);
		SelectValue clmStatusValue6 = new SelectValue();
		clmStatusValue6.setValue(SHAConstants.BILLING_COMPLETED);
		clmStageNames.add(clmStatusValue6);
		SelectValue clmStatusValue7 = new SelectValue();
		clmStatusValue7.setValue(SHAConstants.MEDICAL_APPROVAL);
		clmStageNames.add(clmStatusValue7);
		SelectValue clmStatusValue8 = new SelectValue();
		clmStatusValue8.setValue(SHAConstants.CLAIM_QUERY);
		clmStageNames.add(clmStatusValue8);

		/*SelectValue selectValue = new SelectValue();
		selectValue.setValue(SHAConstants.CLOSED_CLAIMS);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.REJECTED_CLAIMS);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.CLAIMS_PAID_STAUS);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.CLAIMS_BILL_RECEIVED_STATUS);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.CLAIMS_PRE_AUTH_STATUS);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.BILLING_COMPLETED);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.MEDICAL_APPROVAL);
		clmStageNames.add(selectValue);

		selectValue.setValue(SHAConstants.CLAIM_QUERY);
		clmStageNames.add(selectValue);*/

		selectValueContainer.addAll(clmStageNames);

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getEmployeeLoginNameContainer()
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		try{
			Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");
			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();

			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee employee : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(employee.getKey().longValue());
					if(null != employee.getLoginId() && !("").equalsIgnoreCase(employee.getLoginId()))
					{
						selectValue.setValue(employee.getLoginId());
					}
					selectValueList.add(selectValue);
				}
				selectValueContainer.addAll(selectValueList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}


	public BeanItemContainer<SelectValue> getRevisedEmpLoginNameContainer()
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		try{/*IMSSUPPOR-27254

			Query findAll = entityManager.createNamedQuery("EmpSecUser.findAll");

			List<EmpSecUser> employeeNameList = (List<EmpSecUser>) findAll.getResultList();

			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (EmpSecUser empUser : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(empUser.getKey().longValue());
					if(null != empUser.getUserId() && !empUser.getUserId().isEmpty())
					{
						selectValue.setValue(empUser.getUserId()+"-"+empUser.getUserName());
					}
					selectValueList.add(selectValue);
				}
				selectValueContainer.addAll(selectValueList);
			}
		*/
			Query findAll = entityManager.createNamedQuery("TmpEmployee.findAll");

			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();

			if (employeeNameList != null && !employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee empUser : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(empUser.getKey().longValue());
					if(null != empUser.getEmpId() && !empUser.getEmpId().isEmpty())
					{
						selectValue.setValue(empUser.getEmpId()+"-"+empUser.getEmpFirstName());
					}
					selectValueList.add(selectValue);
				}
				selectValueContainer.addAll(selectValueList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getEmployeeLoginContainer(String loginId)
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		try{
			Query findAll = entityManager.createNamedQuery("TmpEmployee.getEmpByLoginId");
			findAll.setParameter("loginId", loginId.toLowerCase());

			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();

			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee employee : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(Long.valueOf(employee.getKey()));
					if(null != employee.getLoginId() && !("").equalsIgnoreCase(employee.getLoginId()))
					{
						if(employee.getEmpFirstName() != null){
							selectValue.setId(Long.valueOf(employee.getKey()));
							selectValue.setValue(employee.getLoginId() +" - "+ employee.getEmpFirstName());
							selectValueList.add(selectValue);
						}
					}
				}
				selectValueContainer.addAll(selectValueList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}


	public BeanItemContainer<SelectValue> getEmployeeLoginContainer(String loginId,EntityManager entityManager)
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		try{
			Query findAll = entityManager.createNamedQuery("TmpEmployee.getEmpByLoginId");
			findAll.setParameter("loginId", loginId.toLowerCase());

			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();

			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee employee : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(Long.valueOf(employee.getKey()));
					if(null != employee.getLoginId() && !("").equalsIgnoreCase(employee.getLoginId()))
					{
						if(employee.getEmpFirstName() != null){
							selectValue.setId(Long.valueOf(employee.getKey()));
							selectValue.setValue(employee.getLoginId() +" - "+ employee.getEmpFirstName());
							selectValueList.add(selectValue);
						}
					}
				}
				selectValueContainer.addAll(selectValueList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}


	@SuppressWarnings("unchecked")
	public DocAcknowledgement getDocAcknowledgment(Long acknowledgementKey) {

		Query query = entityManager
				.createNamedQuery("DocAcknowledgement.findByKey");
		query.setParameter("ackDocKey", acknowledgementKey);

		List<DocAcknowledgement> reimbursementList = (List<DocAcknowledgement>) query
				.getResultList();

		for (DocAcknowledgement docAcknowledgement : reimbursementList) {
			entityManager.refresh(docAcknowledgement);
		}

		if (reimbursementList.size() > 0) {
			return reimbursementList.get(0);
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public ReimbursementBenefits getReimbursementBenefits(Long reimbBenefitsKey,String benefitsFlag) {

		Query query = entityManager
				.createNamedQuery("ReimbursementBenefits.findByRodKeyAndBenefitsFlag");
		query.setParameter("rodKey", reimbBenefitsKey);
		query.setParameter("benefitsFlag", benefitsFlag);

		List<ReimbursementBenefits> reimbursementList = (List<ReimbursementBenefits>) query
				.getResultList();

		for (ReimbursementBenefits reimbursementBenefits : reimbursementList) {
			entityManager.refresh(reimbursementBenefits);
		}

		if(null != reimbursementList && !reimbursementList.isEmpty())
		{
			return reimbursementList.get(0);
		}

		return null;

	}

	public List<ReimbursementBenefitsDetails> getReimbursementBenefitsDetailsList(Long reimbBenefitsKey)
	{
		Query query = entityManager
				.createNamedQuery("ReimbursementBenefitsDetails.findByBenefitsKey");
		query.setParameter("benefitsKey", reimbBenefitsKey);
		List<ReimbursementBenefitsDetails> reimburesmentBenefitsDetails = query.getResultList();
		return reimburesmentBenefitsDetails;
	}

	public List<ExtraEmployeeEffortDTO> getEmployeeDetailsFromRRCDetails(Long rrcRequestKey)
	{
		List<ExtraEmployeeEffortDTO> employeeEffortDTO = null;
		Query query = entityManager.createNamedQuery("RRCDetails.findByRequestKey");
		query.setParameter("rrcRequestKey", rrcRequestKey);
		List<RRCDetails> rrcDetailsList = query.getResultList();
		if(null != rrcDetailsList && !rrcDetailsList.isEmpty())
		{
			ProcessRRCRequestMapper mapper =  ProcessRRCRequestMapper.getInstance();
			employeeEffortDTO = mapper.getEmployeeListenerTableData(rrcDetailsList);
		}
		return employeeEffortDTO;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getPaymentModeContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				selectValue = new SelectValue();
				selectValue.setId(master.getKey().longValue());
				if((SHAConstants.BANK_TRANSFER).equalsIgnoreCase(master.getValue()))
				{
					selectValue.setValue("NEFT");
				}else
				{
					selectValue.setValue(master.getValue());
				}
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getClaimTypeContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				if(null != master && null != master.getValue())
				{
					if( !((SHAConstants.CLAIM_TYPE_HEALTH_CHECKUP).equalsIgnoreCase(master.getValue()) || (SHAConstants.CLAIM_TYPE_OUT_PATIENT).equalsIgnoreCase(master.getValue())) )
					{
						selectValue = new SelectValue();
						selectValue.setId(master.getKey().longValue());
						selectValue.setValue(master.getValue());
						selectValueList.add(selectValue);
					}
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getVerificationTypeContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				if(null != master && null != master.getValue())
				{
					if( !((SHAConstants.CLAIM_TYPE_HEALTH_CHECKUP).equalsIgnoreCase(master.getValue()) || (SHAConstants.CLAIM_TYPE_OUT_PATIENT).equalsIgnoreCase(master.getValue())) )
					{
						selectValue = new SelectValue();
						selectValue.setId(master.getKey().longValue());
						selectValue.setValue(master.getValue());
						selectValueList.add(selectValue);
					}
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}


	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getPAClaimTypeContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			for (MastersValue master : mastersValueList) {
				if(null != master && null != master.getValue())
				{
					if( !((SHAConstants.CLAIM_TYPE_HEALTH_CHECKUP).equalsIgnoreCase(master.getValue()) || (SHAConstants.CLAIM_TYPE_OUT_PATIENT).equalsIgnoreCase(master.getValue())) )
					{
						SelectValue selectValue = new SelectValue();
						selectValue.setId(master.getKey().longValue());
						selectValue.setValue(master.getValue());
						selectValueList.add(selectValue);
					}
				}
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getTypeContainer(String a_key) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", a_key);
		List<MastersValue> mastersValueList = query.getResultList();

		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (MastersValue master : mastersValueList) {
				selectValue = new SelectValue();
				selectValue.setId(master.getKey().longValue());
				selectValue.setValue(master.getValue());
				/*if(!(SHAConstants.PAYMENT_STATUS_CORRECTION).equalsIgnoreCase(master.getValue()))
				{
					selectValueList.add(selectValue);
				}*/

				selectValueList.add(selectValue);

			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	public String getDocumentURLByToken(Long docToken){

		String fileUrl = "";

		Query query = entityManager
				.createNamedQuery("DocumentDetails.findByDocToken");
		query = query.setParameter("documentToken", docToken);
		List<DocumentDetails> docList = query.getResultList();

		if(docList != null && !docList.isEmpty()){
			fileUrl = docList.get(0).getSfFileName(); 
		}

		return fileUrl;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<MasOmbudsman> getOmbudsmanDetailsByCpuCode(String ombudsManCode) {
		List<MasOmbudsman> ombudsman = new ArrayList<MasOmbudsman>();

		try{
			if(ombudsManCode != null){
				Query findAll = entityManager.createNamedQuery("MasOmbudsman.findByOmbudsManCode").setParameter("ombudsmanCode",ombudsManCode);
				ombudsman = (List<MasOmbudsman>) findAll.getResultList();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ombudsman;
	}


	public TmpEmployee getEmployeeName(String initiatorId)
	{
		if(initiatorId != null){
		  TmpEmployee fvrInitiatorDetail;
			Query findByTransactionKey = entityManager.createNamedQuery(
					"TmpEmployee.getEmpByLoginId").setParameter("loginId", initiatorId.toLowerCase());
			try{
				fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
				return fvrInitiatorDetail;
			}
			catch(Exception e)
			{
				return null;
			}
	  }
	  return null;
	  }

	
	  public MasterRemarks getRemarks(Long refKey) {
		
		MasterRemarks resultObj = null;
		try {  
			Query query = entityManager
		  			.createNamedQuery("MasterRemarks.findByRefKey");
		  	query.setParameter("masRefKey", refKey);
		  	
		  	List<MasterRemarks> resultList = (List<MasterRemarks>)query.getResultList();
		  	
		  	if (resultList != null && ! resultList.isEmpty()) {
		  		resultObj = resultList.get(0);
		  	}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	  	
	  	return resultObj;
	  	
	  }
	  
	  public MasterRemarks getRejSubCategRemarks(Long rejSubCategid) {

		  MasterRemarks resultObj = null;
		  
		  try {
		  	Query query = entityManager
		  			.createNamedQuery("MasterRemarks.findByRejSubCategKey");
		  	query.setParameter("rejSubCategKey", rejSubCategid);
		  	
		  	List<MasterRemarks> resultList = query.getResultList();
		  	
		  	if (resultList != null && ! resultList.isEmpty()) {
		  		resultObj = resultList.get(0);
		  	}
		  }
		  catch(Exception e){
			  
			  e.printStackTrace();
		  }
		  	
		  return resultObj;
		  	
	  }
	  
	  public MasterRemarks getQueryRemarks(Long masterKey) {
		  	Query query = entityManager
		  			.createNamedQuery("MasterRemarks.findByKey");
		  	query.setParameter("masterKey", masterKey);
		  	
		   List resultList = query.getResultList();
		  	
		  	if (resultList != null && ! resultList.isEmpty()) {
		  		return (MasterRemarks) resultList.get(0);
		  	}
		  	
		  	return null;
		  	
	  }
	  
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getUserStatusByMaster() {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", SHAConstants.MASTER_TYPE_CODE);
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);

		return resultContainer;
	}


	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getReallocationUserStatusByMaster() {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", SHAConstants.MASTER_TYPE_CODE_REALLOCATION);
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);

		return resultContainer;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue> getMasterValueByCode(String parameter) {

		Query findAll = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		findAll.setParameter("masterTypeCode",parameter);
		@SuppressWarnings("unchecked")
		List<MastersValue> resultValueResultList = (List<MastersValue>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (MastersValue paDocumentType : resultValueResultList) {
			SelectValue select = new SelectValue();
			select.setId(paDocumentType.getKey());
			if(paDocumentType.getValue()!=null && paDocumentType.getValue().length() > 50){
				String value = paDocumentType.getValue();
				String substring = value.substring(0, 50);
				select.setValue(substring);
			}else{
				select.setValue(paDocumentType.getValue());
			}
			selectValuesList.add(select);
		}
		resultCodeContainer.addAll(selectValuesList);		

		return resultCodeContainer;

	}
	
	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue> getMasterValueByCodeForDecisionChange(String parameter) {

		Query findAll = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		findAll.setParameter("masterTypeCode",parameter);
		@SuppressWarnings("unchecked")
		List<MastersValue> resultValueResultList = (List<MastersValue>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (MastersValue paDocumentType : resultValueResultList) {
			SelectValue select = new SelectValue();
			select.setId(paDocumentType.getKey());
			/*if(paDocumentType.getValue()!=null && paDocumentType.getValue().length() > 50){
				String value = paDocumentType.getValue();
				String substring = value.substring(0, 50);
				select.setValue(substring);
			}else{*/
				select.setValue(paDocumentType.getValue());
			//}
			selectValuesList.add(select);
		}
		resultCodeContainer.addAll(selectValuesList);		

		return resultCodeContainer;

	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue> getMasterValueByCodeAndExcludeKey(String parameter, Long Key) {

		Query findAll = entityManager.createNamedQuery("MastersValue.findByListKey");
		findAll.setParameter("masterTypeCode",parameter);
		findAll.setParameter("parentKey",Key);
		@SuppressWarnings("unchecked")
		List<MastersValue> resultValueResultList = (List<MastersValue>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (MastersValue paDocumentType : resultValueResultList) {
			SelectValue select = new SelectValue();
			select.setId(paDocumentType.getKey());
			if(paDocumentType.getValue()!=null && paDocumentType.getValue().length() > 50){
				String value = paDocumentType.getValue();
				String substring = value.substring(0, 50);
				select.setValue(substring);
			}else{
				select.setValue(paDocumentType.getValue());
			}
			selectValuesList.add(select);
		}
		resultCodeContainer.addAll(selectValuesList);		

		return resultCodeContainer;

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getOmbudsmanDetailsByDesc( ) {
		try{
			List<MasOmbudsman> ombudsman = new ArrayList<MasOmbudsman>();
			Query findAll = entityManager.createNamedQuery("MasOmbudsman.findAll");
			ombudsman = (List<MasOmbudsman>) findAll.getResultList();

			//return ombudsman;

			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			for (MasOmbudsman cpuCode : ombudsman) {
				SelectValue select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getCpuCodeDescription());
				selectValuesList.add(select);
			}
			cpuCodeContainer.addAll(selectValuesList);

			cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

			return cpuCodeContainer;
		}catch(Exception e){
			return null;
		}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public MasOmbudsman getOmbudsmanDetailsByKey(Long key) {
		try{
			List<MasOmbudsman> ombudsman = new ArrayList<MasOmbudsman>();
			if(key != null){
				Query findAll = entityManager.createNamedQuery("MasOmbudsman.findBykey").setParameter("key",key);
				ombudsman = (List<MasOmbudsman>) findAll.getResultList();
				if(ombudsman!=null){
					return ombudsman.get(0);
				}
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	  
	@SuppressWarnings("unchecked")
	public List<SelectValue> getDoctorToReallocateTo(String query, String doctorId) {
		if (doctorId != null && !doctorId.isEmpty()) {
			String query1 = "select m.* from MAS_SEC_USER m inner join MAS_SEC_USER_LIMIT_MAPPING n on m.USER_ID = n.USER_ID inner join MAS_SEC_USER_AUTOALLOCATION o on o.DOCTOR_ID = n.USER_ID inner join IMS_CLS_USER_LOG_DTLS p on p.LOGIN_ID = n.USER_ID and p.USER_KEY in(select max(USER_KEY) from IMS_CLS_USER_LOG_DTLS group by LOGIN_ID) where o.ELIGIBLE_FLAG = 'Y' and p.ACTIVE_STATUS = 1 and Lower(m.USER_ID) like '%"
					+ query.toLowerCase()
					+ "%' and m.USER_ID != '"+doctorId+"' and m.ACTIVE_STATUS = 'Y'";
			Query nativeQuery = entityManager.createNativeQuery(query1);

			List<Object[]> objList = (List<Object[]>) nativeQuery
					.getResultList();
			BeanItemContainer<SelectValue> reallocateValueContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			List<SelectValue> doctorDTOList = new ArrayList<SelectValue>();
			if(objList != null && !objList.isEmpty()){
				for (Object[] obj : objList) {
					SelectValue doctorDTO = new SelectValue();
					BigDecimal id = (BigDecimal)obj[0];
					doctorDTO.setId(id.longValue());
					doctorDTO.setValue((String)obj[2]+"-"+(String)obj[3]);

					doctorDTOList.add(doctorDTO);
				}
			}
			if(!doctorDTOList.isEmpty()){
				reallocateValueContainer.addAll(doctorDTOList);
				return doctorDTOList;
			}
		}
		return null;
	}

	public BeanItemContainer<SelectValue> getEmpListByCPUCode(String cpuCode) {

		BeanItemContainer<SelectValue> empContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if (cpuCode != null) {
			cpuCode = StringUtils.trim(cpuCode);

			Query cpuOrgQuery = entityManager
					.createNamedQuery("OrgToCPUMapping.findEMPForCPU");

			cpuOrgQuery.setParameter("cpuCode", cpuCode);
			List<OrgToCPUMapping> cpuBasedOrgList = (List<OrgToCPUMapping>) cpuOrgQuery
					.getResultList();

			if (cpuBasedOrgList != null) {

				List<String> orgIdList = new ArrayList<String>();

				for (OrgToCPUMapping orgToCPUMapping : cpuBasedOrgList) {
					orgIdList.add(orgToCPUMapping.getOrgId());
				}

				if (orgIdList != null && !orgIdList.isEmpty()) {
					for (String orgId : orgIdList) {

						Query orgEmpQuery = entityManager
								.createNamedQuery("UserToOrgMapping.findUserIdForOrg");

						orgEmpQuery.setParameter("orgId", orgId);
						List<UserToOrgMapping> cpuBasedEmpList = (List<UserToOrgMapping>) orgEmpQuery
								.getResultList();

						if (cpuBasedEmpList != null
								&& !cpuBasedEmpList.isEmpty()) {
							for (UserToOrgMapping orgUserObj : cpuBasedEmpList) {
								SelectValue empSelect = new SelectValue(null,
										orgUserObj.getUserId());
								empContainer.addBean(empSelect);
								empSelect = null;
							}
						}
					}
				}
			}
		}

		return empContainer;
	}

	/*	public BeanItemContainer<SelectValue> getOMPEventCodes() {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			Query query = entityManager.createNamedQuery("OMPEvents.findByOMPEvents");
			List<EventMaster> mastersValueList = query.getResultList();
			for (int i = 0; i < mastersValueList.size(); i++) {
				SelectValue selected = new SelectValue();
				selected.setId(mastersValueList.get(i).getKey());
				selected.setValue(mastersValueList.get(i).getEventType()+"-"+mastersValueList.get(i).getDescription());
				selectValueList.add(selected);
			}
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(selectValueList);
			return selectValueContainer;
		}*/

	public BeanItemContainer<SelectValue> getListMasterValuebyTypeCode(String masterTypeCode) {

		MastersValue a_mastersValue = new MastersValue();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", masterTypeCode);
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);		

		return resultContainer;
	}
	
	public WeakHashMap<Long, BeanItemContainer<SelectValue>> getOMPSubClassificationMap() {

		WeakHashMap<Long, BeanItemContainer<SelectValue>> resultMap = new WeakHashMap<Long, BeanItemContainer<SelectValue>>();
		List<MastersValue> a_mastersValueList = new ArrayList<MastersValue>();

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", SHAConstants.OMP_SUB_CLSIFI_CLM_RELATED);
		List<MastersValue> subClsficList1 = query.getResultList();
		if(subClsficList1 != null && !subClsficList1.isEmpty()){
			a_mastersValueList.addAll(subClsficList1);
		}
		
		BeanItemContainer<SelectValue> clmRelatedSubContainer = getResultContainer(a_mastersValueList);
		resultMap.put(ReferenceTable.OMP_CLAIM_RELATED_CLASSIFICATION_KEY, clmRelatedSubContainer);
		
		a_mastersValueList.clear();
		query = query.setParameter("masterTypeCode", SHAConstants.OMP_SUB_CLSIFI_NEGO);
		List<MastersValue> subClsficList2 = query.getResultList();
		if(subClsficList2 != null && !subClsficList2.isEmpty()){
			a_mastersValueList.addAll(subClsficList2);
		}
		
		BeanItemContainer<SelectValue> negotiatorFeeSubContainer = getResultContainer(a_mastersValueList);
		resultMap.put(ReferenceTable.OMP_NEGOTIATOR_CLASSIFICATION_KEY, negotiatorFeeSubContainer);
		
		a_mastersValueList.clear();
		query = query.setParameter("masterTypeCode", SHAConstants.OMP_SUB_CLSIFI_OTH);
		List<MastersValue> subClsficList3 = query.getResultList();
		if(subClsficList3 != null && !subClsficList3.isEmpty()){
			a_mastersValueList.addAll(subClsficList3);
		}
		
		BeanItemContainer<SelectValue> otherExpSubContainer = getResultContainer(a_mastersValueList);
		resultMap.put(ReferenceTable.OMP_OTHER_EXP_CLASSIFICATION_KEY, otherExpSubContainer);
		

		return resultMap;
	}

	private BeanItemContainer<SelectValue> getResultContainer(List<MastersValue> a_mastersValueList) {
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		for (MastersValue mastersValue : a_mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getValue());
			selectValuesList.add(select);
		}
		resultContainer.addAll(selectValuesList);
		
		return resultContainer;
	}	
	
	
	

	public List<RODDocumentCheckList> getOMPRODDocumentListValues(OMPDocAcknowledgement objDocAck)
	{

		Query query = entityManager
				.createNamedQuery("RODDocumentCheckList.findByDocKey");

		query.setParameter("docKey", objDocAck.getKey());
		List<RODDocumentCheckList> rodDocCheckList = query.getResultList();
		List<RODDocumentCheckList> finalRodDocCheckList = new ArrayList<RODDocumentCheckList>();
		for (RODDocumentCheckList rodDocumentCheckList : rodDocCheckList) {
			entityManager.refresh(rodDocumentCheckList);
			finalRodDocCheckList.add(rodDocumentCheckList);
		}
		return finalRodDocCheckList ;

	}

	public BeanItemContainer<SelectValue> getCountryValue() {
		Query query = entityManager.createNamedQuery("Country.findAll");
		List<Country> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (Country value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}

	public BeanItemContainer<SelectValue> getListMasterEventValue() {

		MastersEvents a_mastersValue = new MastersEvents();

		Query query = entityManager
				.createNamedQuery("MastersEvents.findAll");
		List<MastersEvents> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (MastersEvents mastersValue : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getEventType() + " - " + mastersValue.getEventDescription());
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);		

		return selectValueContainer;
	}

	public MastersEvents getMasterEventValueBykey(Long key) {

		MastersEvents a_mastersValue = new MastersEvents();

		Query query = entityManager
				.createNamedQuery("MastersEvents.findAll");
		query = query.setParameter("findByKey", key);
		List<MastersEvents> mastersValueList = query.getResultList();

		MastersEvents mastersEvents = mastersValueList.get(0);
		return mastersEvents;
	}


	public BeanItemContainer<SelectValue> getListMasterCurrencyValue() {

		Currency a_mastersValue = new Currency();

		Query query = entityManager
				.createNamedQuery("Currency.findAll");
		List<Currency> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (Currency mastersValue : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getCurrencyCode());
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);		

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getNegotiatorName(Long rodkey) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("OMPReimbursement.findByKey");
		query = query.setParameter("primaryKey", rodkey);
		List<OMPReimbursement> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (OMPReimbursement value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getNegotiatorName());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}

	public BeanItemContainer<SelectValue> getOMPContainerForProduct() {
		Query findAll = entityManager.createNamedQuery("Product.findOMPUnique");
		List<Object> productList = (List<Object>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!productList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			Long i=0l;
			for (Object product : productList) {

				SelectValue selectValue = new SelectValue();

				if(product instanceof Object[]){
					Object[] ObjArray =(Object[]) product;
					Object objCode = ObjArray[0];
					Object objValue =ObjArray[1];
					if(objCode != null && objValue != null){

						selectValue.setValue(objValue.toString());
						selectValue.setValue(objValue.toString() + " - "+objCode.toString());
					}
					i++;
				}

				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}


	public BeanItemContainer<SelectValue> getOMPInsuredNameList(List<Insured> listOfItems) {
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		for (int i = 0; i < listOfItems.size(); i++) {
			SelectValue selected = new SelectValue();
			selected.setId(listOfItems.get(i).getKey());
			selected.setValue(listOfItems.get(i).getInsuredName());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(selectValueList);
		return selectValueContainer;
	}


	public SelectValue getCountryValueByValue(String countryName) {
		Query query = entityManager.createNamedQuery("Country.findByName");
		query.setParameter("countryName", countryName);
		List<Country> mastersValueList = query.getResultList();
		SelectValue select = null;
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (Country value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue());
		}
		return select;
	}

	public SelectValue getCountryValueByKey(Long key) {
		Query query = entityManager.createNamedQuery("Country.findByKey");
		query.setParameter("key", key);
		List<Country> mastersValueList = query.getResultList();
		SelectValue select = null;
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (Country value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue());
		}
		return select;
	}

	public BeanItemContainer<SelectValue> getListMasterEventByProduct(Long productKey) {

		MastersEvents a_mastersValue = new MastersEvents();

		Query query = entityManager
				.createNamedQuery("MastersEvents.findByproduct");
		query.setParameter("productKey", productKey);
		List<MastersEvents> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (MastersEvents mastersValue : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getEventCode() + " - " + mastersValue.getEventDescription());
			selectValuesList.add(select);
		}
		selectValueContainer.addAll(selectValuesList);		

		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getNegotiationNamesAll() {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		Query query = entityManager
				.createNamedQuery("OMPNegotiationMas.findByOMPall");
		//			query = query.setParameter("primaryKey", rodkey);
		List<OMPNegotiationMas> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		for (OMPNegotiationMas value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getNeogtiationName());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}


	public MastersEvents getListMasterEventBykey(Long key) {

		MastersEvents a_mastersValue = new MastersEvents();

		Query query = entityManager
				.createNamedQuery("MastersEvents.findByKey");
		query.setParameter("primaryKey", key);
		List<MastersEvents> mastersValueList = query.getResultList();
		if(mastersValueList.size()>0){
			return mastersValueList.get(0);
		}

		return null;
	}

	public Stage getStageByKey(Long stageKey) {
		Stage stage = null;


		Query query = entityManager
				.createNamedQuery("Stage.findByKey");
		query = query.setParameter("stageKey", stageKey);

		List<Stage> stageList = query.getResultList();

		if(stageList != null && !stageList.isEmpty()){
			stage = stageList.get(0);
			entityManager.refresh(stage);

		}
		return stage;
	}	

	public List<MasPaClaimCovers> getCoversListByProductKey(Long productKey) {
		Query query = entityManager.createNamedQuery("MasPaClaimCovers.findByProductKey");
		query = query.setParameter("productKey", productKey);
		@SuppressWarnings("unchecked")
		List<MasPaClaimCovers> coverList = (List<MasPaClaimCovers>) query.getResultList();
		return coverList;
	} 


	public BeanItemContainer<SelectValue> getRackList(List<Long> list) {
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
		.createCriteria(MasRack.class)
		.add(Restrictions.in("storageKey", list))
		.add(Restrictions.eq("activateStatus", SHAConstants.YES_FLAG))
		.addOrder(Order.asc("rackDesc"))
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("rackDesc"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;
	}

	/*public BeanItemContainer<SelectValue> getShelfList(List<Long> list) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
				.createCriteria(MasShelf.class)
				.add(Restrictions.in("rackKey", list))
				.add(Restrictions.eq("activateStatus", SHAConstants.YES_FLAG))
				.addOrder(Order.asc("shelfDesc"))
				.setProjection(
						Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("shelfDesc"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
								.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;
	}*/

	public BeanItemContainer<SelectValue> getStorageLocationList(List<Long> cpuList) {


		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
		.createCriteria(MasStorageLocation.class)
		.add(Restrictions.in("cpu", cpuList))
		.add(Restrictions.eq("activateStatus", SHAConstants.YES_FLAG))
		.addOrder(Order.asc("storageDesc"))
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("storageDesc"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;

		/*Query query = entityManager
				.createNamedQuery("MasStorageLocation.findByLocationByCPU");
		query = query.setParameter("cpuList", cpuList);

		List<MasStorageLocation> mastersValueList = (List<MasStorageLocation>)query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		if(mastersValueList != null && !mastersValueList.isEmpty()){
			for (MasStorageLocation value : mastersValueList) {
				SelectValue select = new SelectValue();
				select.setId(value.getKey());
				select.setValue(value.getStorageDesc());
				selectValuesList.add(select);
			}
			mastersValueContainer.addAll(selectValuesList);
		}
		return mastersValueContainer;*/


	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRackListByLocation(Long a_key) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
		.createCriteria(MasRack.class)
		.add(Restrictions.eq("storageKey", a_key))
		.add(Restrictions.eq("activateStatus", SHAConstants.YES_FLAG))
		/*.addOrder(Order.asc("rackDesc"))*/
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("rackDesc"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;

	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getShelfListByRack(Long a_key) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session
		.createCriteria(MasShelf.class)
		.add(Restrictions.eq("rackKey", a_key))
		.add(Restrictions.eq("activateStatus", SHAConstants.YES_FLAG))
		/*.addOrder(Order.asc("shelfDesc"))*/
		.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("shelfDesc"), "value"))
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SelectValue.class)).list();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;
	}

	public String getDocumentDetailsByDocType(String docType,String intimationNo){

		String docToken = "";

		Query query = entityManager
				.createNamedQuery("DocumentDetails.findByIntimationNoDocType");
		query = query.setParameter("documentType", docType);
		query = query.setParameter("intimationNumber",intimationNo);
		List<DocumentDetails> docList = query.getResultList();

		if(docList != null && !docList.isEmpty()){
			docToken = String.valueOf(docList.get(0).getDocumentToken()); 
		}

		return docToken;
	}

	public Boolean getRestrictionTime(){

		Boolean restricted = Boolean.FALSE;

		MastersValue fromValue = getMaster(ReferenceTable.REPORT_RESTRICTION_TIME_FROM);
		MastersValue toValue = getMaster(ReferenceTable.REPORT_RESTRICTION_TIME_TO);
		MastersValue restriction = getMaster(ReferenceTable.RESTRICTION_FLAG);

		if(fromValue != null && toValue != null && restriction != null){

			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
			try {
				Date from = parser.parse(fromValue.getValue());
				Date to = parser.parse(toValue.getValue());

				Date currentDate = new Date();
				if(currentDate.after(from) && currentDate.before(to)){
					restricted = Boolean.TRUE;
				}


			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return restricted;

	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerByCode(String a_key) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
		.add(Restrictions.eq("code", a_key))
		.addOrder(Order.asc("value"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;
	}
	

	public List<ELearnDto> getELearnContentList(){

		List<ELearnDto> resutlContentList = new ArrayList<ELearnDto>();
		ELearnDto eDocDto;
		Query query = entityManager
				.createNamedQuery("EDocumentsMaster.findAll");

		List<EDocumentsMaster> eContentList = (List<EDocumentsMaster>) query.getResultList();

		for (EDocumentsMaster eContent : eContentList) {
			eDocDto = new ELearnDto();
			eDocDto.setSno(eContentList.indexOf(eContent)+1);
			eDocDto.setDocName(eContent.getDocumentName());
			eDocDto.setDocToken(eContent.getDocToken());
			resutlContentList.add(eDocDto);
		}

		return resutlContentList;
	}

	public Map<String, Object> getSectionDetailsMaster(Long productKey){
		BeanItemContainer<SelectValue> sectionContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> coverContainer= new BeanItemContainer<SelectValue>(SelectValue.class);
		BeanItemContainer<SelectValue> subCoverContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		Map<String, Object> containerMap = new WeakHashMap<String, Object>();

		sectionContainer = getSectionList(ReferenceTable.FHO_PRODUCT_REVISED,null);
		if(sectionContainer != null && sectionContainer.getItemIds() != null && !sectionContainer.getItemIds().isEmpty()){
			coverContainer = getCoverList(sectionContainer.getItemIds().get(0).getId());
			subCoverContainer = getSubCoverListBySecKey(sectionContainer.getItemIds().get(0).getId());
		}			

		containerMap.put("section", sectionContainer);
		containerMap.put("cover", coverContainer);
		containerMap.put("subcover", subCoverContainer);

		return containerMap;		

	}

	public BeanItemContainer<SelectValue> getSubCoverListBySecKey(Long sectionKey){
		BeanItemContainer<SelectValue> claimSubCoverContainer = new BeanItemContainer<SelectValue>(SelectValue.class);

		try{
			Query query = entityManager
					.createNamedQuery("ClaimSectionSubCover.findBySecKey").setParameter("sectionKey", sectionKey);

			List<ClaimSectionSubCover> claimSubCoverList = (List<ClaimSectionSubCover>) query.getResultList();


			SelectValue selected = null;
			for (ClaimSectionSubCover claimSubCover : claimSubCoverList) {
				selected = new SelectValue();
				selected.setId(claimSubCover.getCoverKey());
				selected.setValue(claimSubCover.getSubCoverValue());
				selected.setCommonValue(claimSubCover.getSubCoverCode());
				claimSubCoverContainer.addBean(selected);
				selected = null;
			}

			claimSubCoverContainer.sort(new Object[] {"value"}, new boolean[] {true});
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return claimSubCoverContainer;
	}
	
	
	public MasterGST getGSTByStateId(String poiCode ) {
		
		Query findAllOrg = entityManager.createNamedQuery(
				"OrganaizationUnit.findByUnitId").setParameter("officeCode",
						poiCode);
		
		   List<OrganaizationUnit> orgList = findAllOrg.getResultList();
		if(null != orgList && !orgList.isEmpty()){
			OrganaizationUnit orgObj = orgList.get(0);
		
		if(null != orgObj){			

			Query findAllGst = entityManager.createNamedQuery(
				"MasterGST.findByStateId").setParameter("stateId",
						orgObj.getState().getKey());

		 List<MasterGST> masList = findAllGst.getResultList();
		 if(null != masList && !masList.isEmpty()){
			 
			 MasterGST masObj = masList.get(0);
			 return masObj;
		 }
		}
		}
		return null;
	}
	
	public Boolean getZUAQueryDetails(String policyNumber){
		
		Boolean isZUAQueryAvailable = Boolean.FALSE;
		
		/*Query query = entityManager
				.createNamedQuery("ZUASendQueryTable.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);	
		List<ZUASendQueryTable> zuaQueryList = query.getResultList();
		
		if(zuaQueryList != null && !zuaQueryList.isEmpty()){
			for (ZUASendQueryTable zuaQueryTable : zuaQueryList) {
				
				Query zuaMasterquery = entityManager
						.createNamedQuery("ZUAMasterQueryDetails.findByQueryCode");
				zuaMasterquery = zuaMasterquery.setParameter("queryCode", zuaQueryTable.getQueryCode());	
				List<ZUAMasterQueryDetails> zuaMasterQueryList = zuaMasterquery.getResultList();
				
				if(null != zuaMasterQueryList && !zuaMasterQueryList.isEmpty()){
					
					isZUAQueryAvailable = Boolean.TRUE;
				}
			}
			
		}*/
		
		return isZUAQueryAvailable;
	}
	
	
	
	public List<ZUAViewQueryHistoryTable> getZUAQueryHistoryDetails(String policyNumber){	
		
		Query query = entityManager
				.createNamedQuery("ZUAQueryHistoryTable.findByPolicyNumber");
		query = query.setParameter("policyNumber", policyNumber);	
		List<ZUAViewQueryHistoryTable> zuaQueryList = query.getResultList();
		
		if(zuaQueryList != null && !zuaQueryList.isEmpty()){
			return zuaQueryList;
		}	
				
		return zuaQueryList;
	}
	
	public BeanItemContainer<SelectValue> getEmployeeNameIncludingInactiveEmployees()
	{
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAllEmployees");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			StringBuffer strName = null;
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeNameList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey().longValue());
				strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				selectValue.setValue(strName.toString());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}

	@SuppressWarnings("unchecked")
	public Claim getClaimByIntimationKey(Long intimationKey) {
		Query query = entityManager.createNamedQuery("Claim.findByIntimationKey");
		query.setParameter("intimationKey", intimationKey);		
		List<Claim> resultList = query.getResultList();
		if(!resultList.isEmpty()){
			return resultList.get(0);
		}else{
			return null;
		}		
	}

	public BeanItemContainer<SelectValue> getLumenType() {
		BeanItemContainer<SelectValue> selectValueContainer = getListMasterValuebyTypeCode(ReferenceTable.LUMEN_TYPE);
		return selectValueContainer;
	}

//	public BeanItemContainer<SelectValue> getLumenHospitalErrorTypes() {
//		BeanItemContainer<SelectValue> selectValueContainer = getListMasterValuebyTypeCode(ReferenceTable.LUMEN_ERR_TYPE);
//		return selectValueContainer;
//	}

	public BeanItemContainer<SelectValue> getLumenSource() {
		Stage temp = null;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		Map<String, Long> tempVal = new  HashMap<>();
		
		tempVal.put("pcr-zmr", ReferenceTable.PROCESS_CLAIM_REQUEST_ZONAL_STAGE_KEY);
		tempVal.put("pcr", ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
		tempVal.put("billing", ReferenceTable.BILLING_STAGE);
		tempVal.put("finance", ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
		tempVal.put("ped", ReferenceTable.PED_ENDORSEMENT_STAGE);
		tempVal.put("ssa-r1", ReferenceTable.PROCESS_CLAIM_REQUEST_STAGE_KEY);
		tempVal.put("ssa-r2", ReferenceTable.PROCESS_CLAIM_FINANCIAL_STAGE_KEY);
		tempVal.put("investigation", ReferenceTable.INVESTIGATION_STAGE);		
		tempVal.put("flp-p", ReferenceTable.PROCESS_PRE_MEDICAL);
		tempVal.put("flp-e", ReferenceTable.PRE_MEDICAL_PROCESSING_ENHANCEMENT);
		tempVal.put("prc-p", ReferenceTable.PROCESS_PREAUTH);
		tempVal.put("prc-e", ReferenceTable.PROCESS_ENHANCEMENT_TYPE);		
		tempVal.put("lumen", ReferenceTable.LUMEN_PROCESS);
		for (Map.Entry<String, Long> entry : tempVal.entrySet()) {
			temp = getStageBykey(entry.getValue());
			SelectValue selected = new SelectValue();
			selected.setId(temp.getKey());
			selected.setValue(temp.getStageName());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
		return container;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getLumenEmployeeName() {
		Query query = entityManager.createNamedQuery("TmpEmployee.findAll");
		List<TmpEmployee> employeeList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if (!employeeList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for (TmpEmployee employee : employeeList) {
				selectValue = new SelectValue();
				selectValue.setId(employee.getKey());
				selectValue.setValue(employee.getEmpId()+"-"+employee.getEmpFirstName());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}
	
	public BidiMap loadEmployeeNameDataIncludingInactiveEmployees()
	{
		BidiMap employeeNameMap = new DualHashBidiMap();
		//Map<String,String> employeeNameMap = new HashMap<String, String>();
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findAllEmployees");
		List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
		//BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if (!employeeNameList.isEmpty()) {
			//List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			StringBuffer strName = null;
			for (TmpEmployee employee : employeeNameList) {
				strName = new StringBuffer();
				if(null != employee.getEmpFirstName())
				{
					strName.append(employee.getEmpFirstName());
				}
				if(null != employee.getEmpMiddleName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpMiddleName()); 
					}
					else
					{
						strName.append(employee.getEmpMiddleName());
					}
				}
				if(null != employee.getEmpLastName())
				{
					if(("").equalsIgnoreCase(strName.toString()))
					{
						strName.append(employee.getEmpLastName()); 
					}
					else
					{
						strName.append(employee.getEmpLastName());
					}
				}
				employeeNameMap.put(strName , employee.getEmpId());
			}
			
		}
		return employeeNameMap;
	
	}
	
	
	public BeanItemContainer<SelectValue> getEmployeeLoginNameContainerIncludingInactiveEmployees()
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		try{
			Query findAll = entityManager.createNamedQuery("TmpEmployee.findAllEmployees");
			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
	
			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee employee : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(employee.getKey().longValue());
					if(null != employee.getLoginId() && !("").equalsIgnoreCase(employee.getLoginId()))
					{
						selectValue.setValue(employee.getLoginId());
					}
					selectValueList.add(selectValue);
				}
				selectValueContainer.addAll(selectValueList);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getCommonTmpCpuCodeList() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getCpuCode());
			select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;

	}
	
	
	@SuppressWarnings({ "unchecked"})
	public List<ViewSearchCriteriaTableDTO>  getTmpCpuCodeWithDescriptionList() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAllAsc");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll.getResultList();
		List<ViewSearchCriteriaTableDTO> cpuCodewithDescriptionList = new ArrayList<ViewSearchCriteriaTableDTO>();
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			
			ViewSearchCriteriaTableDTO viewSearchCriteriaDto = new ViewSearchCriteriaTableDTO();
			
			viewSearchCriteriaDto.setCpuCodeWithDescription(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
			cpuCodewithDescriptionList.add(viewSearchCriteriaDto);
		}

		return cpuCodewithDescriptionList;

	}


	public MastersEvents getEventType(String eventDescription) {
		MastersEvents a_mastersValue = new MastersEvents();
		eventDescription = eventDescription.trim();
		if (eventDescription != null) {
			Query query = entityManager
					.createNamedQuery("MastersEvents.findByEventDesc");
			query = query.setParameter("eventCode", eventDescription);
			List<MastersEvents> mastersValueList = query.getResultList();
			for (MastersEvents mastersValue : mastersValueList)
				a_mastersValue = mastersValue;
		}

		return a_mastersValue;
}
	public MastersEvents getEventTypeByKey(Long eventKey) {
		MastersEvents a_mastersValue = new MastersEvents();
			Query query = entityManager
					.createNamedQuery("MastersEvents.findByKey");
			query = query.setParameter("primaryKey", eventKey);
			List<MastersEvents> mastersValueList = query.getResultList();
			for (MastersEvents mastersValue : mastersValueList)
				a_mastersValue = mastersValue;

		return a_mastersValue;
}
  	public BeanItemContainer<SelectValue> getMastersValuebyTypeCodeOnStaatus(String masterTypeCode) {
		
			MastersValue a_mastersValue = new MastersValue();
			
				Query query = entityManager
						.createNamedQuery("MastersValue.findByMasterTypeCodeWithStatus");
				query = query.setParameter("masterTypeCode", masterTypeCode);
				List<MastersValue> mastersValueList = query.getResultList();
				BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);		
				
				return resultContainer;
		}
  	
  	public boolean doGMCPolicyCheckForICR(String argPolicyNumber){
  		Query query = entityManager.createNativeQuery("SELECT * FROM MAS_CLAIM_RATIO c WHERE c.ACTIVE_FLAG = 'Y' AND c.POLICY_NUMBER = '"+argPolicyNumber+"' ");
  		List results = query.getResultList();
  		if(results.size() > 0){
  			return true;
  		}else{
  			return false;
  		}
  	}
  	
  	public BeanItemContainer<SelectValue> getLumenStatusContainer()	{
  		
  		List<Long> statusKeyList = new ArrayList<Long>();
  		statusKeyList.add(ReferenceTable.LUMEN_QUERY);
  		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_APPROVE);
  		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_REJECT);
  		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_REPLY);
  		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_QUERY);
  		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_MIS);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_I_CLOSE);
		statusKeyList.add(ReferenceTable.LUMEN_COORDINATOR_APPROVE);
		statusKeyList.add(ReferenceTable.LUMEN_COORDINATOR_REPLY);
		statusKeyList.add(ReferenceTable.LUMEN_COORDINATOR_QUERY);
		statusKeyList.add(ReferenceTable.LUMEN_COORDINATOR_MIS);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_II_APPROVE);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_II_REJECT);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_II_QUERY);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_II_MIS);
		statusKeyList.add(ReferenceTable.LUMEN_LEVEL_II_CLOSE);
		statusKeyList.add(ReferenceTable.LUMEN_MIS_REPLY);
		statusKeyList.add(ReferenceTable.LUMEN_INITIATOR_REPLY);
    
		List<Status> statusList = getStatusListByKey(statusKeyList);
		
		BeanItemContainer<SelectValue> statusContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		if(statusList != null && !statusList.isEmpty()){
			for (Status status : statusList) {
				SelectValue statusSelect = new SelectValue(status.getKey(),status.getStage().getStageName() + " - " + status.getProcessValue());
				statusContainer.addBean(statusSelect);
			}
		}
    return statusContainer;
  	}
  	
	public List<Status> getStatusListByKey(List<Long> keyList) {
		List<Status> statusObjList = new ArrayList<Status>();
		try{
			if(keyList != null && !keyList.isEmpty()){
				Query query = entityManager
						.createNamedQuery("Status.findByKeys");
				query = query.setParameter("statusKeyList", keyList);
				List<Status> mastersValueList = query.getResultList();
				if(mastersValueList != null && !mastersValueList.isEmpty()){
					for (Status value : mastersValueList) {
						entityManager.refresh(value);
						statusObjList.add(value);
					}
				}
				mastersValueList = null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return statusObjList;
	}

	public BeanItemContainer<SelectValue> getOmpClaimStatusListForReport(){
	
		BeanItemContainer<SelectValue> statusContainer = new BeanItemContainer(SelectValue.class);
		
		SelectValue regSelect = new SelectValue(ReferenceTable.CLAIM_REGISTERED_STATUS, SHAConstants.OMP_REGISTERED);
		SelectValue rejSelect = new SelectValue(ReferenceTable.CLAIM_SUGGEST_REJECTION_STATUS, SHAConstants.OMP_REJECTED_STATUS);
		SelectValue closSelect = new SelectValue(ReferenceTable.CLAIM_CLOSED_STATUS, SHAConstants.OMP_CLOSED_STATUS);
		SelectValue settleSelect = new SelectValue(ReferenceTable.PAYMENT_SETTLED, SHAConstants.OMP_PAID_STATUS);
		statusContainer.addBean(regSelect);
		statusContainer.addBean(rejSelect);
		statusContainer.addBean(closSelect);
		statusContainer.addBean(settleSelect);
		
		return statusContainer;
	}
	public BeanItemContainer<SelectValue> getFinancialYearContainer(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year+1;i>=year-3;i--){
			SelectValue selectValue = new SelectValue();
			Long j = i-1;
			selectValue.setId(j);
			selectValue.setValue(j.intValue()+"-"+i.intValue());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	public BeanItemContainer<SelectValue> getYearContainer(){
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.YEAR, 1);
		int intYear = instance.get(Calendar.YEAR);
		Long year = Long.valueOf(intYear);
		for(Long i= year;i>=SHAConstants.START_YEAR;i--){
			SelectValue selectValue = new SelectValue();
			selectValue.setId(i);
			selectValue.setValue(i.toString());
			selectValueList.add(selectValue);
		}
		container.addAll(selectValueList);
		
		return container;
	}
	
	public TmpEmployee getEmployeeNameWithInactiveUser(String initiatorId)
	{
	  TmpEmployee fvrInitiatorDetail;
		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.getEmpByLoginIdWithInactive").setParameter("loginId", initiatorId.toLowerCase());
		try{
			fvrInitiatorDetail =(TmpEmployee) findByTransactionKey.getSingleResult();
			return fvrInitiatorDetail;
		}
		catch(Exception e)
		{
			return null;
		}
							
	}
	/**
	 * Get activity list
	 * @param masterTypeCode
	 * @return
	 */
	public BeanItemContainer<SelectValue> getActivityList(String masterTypeCode) {
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey", masterTypeCode);
		List<MastersValue> mastersValueList = query.getResultList();
		
		if(!mastersValueList.isEmpty()) {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			SelectValue selectValue = null;
			for(MastersValue master : mastersValueList) {
				selectValue = new SelectValue();
				selectValue.setId(master.getKey().longValue());
				selectValue.setValue(master.getValue());
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}
		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getMasOpinionRoleList() {
		Query findAll = entityManager.createNamedQuery("MasOpinionRole.findAll");
		List<MasOpinionRole> resultRoleList = (List<MasOpinionRole>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> roleContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (MasOpinionRole masRole : resultRoleList) {
			select = new SelectValue();
			select.setId(masRole.getKey());
			select.setValue(masRole.getRoleDescription().toString() /*+ " - " + cpuCode.getDescription()*/);
			select.setCommonValue(masRole.getRoleCode());
			selectValuesList.add(select);
		}
		roleContainer.addAll(selectValuesList);
		roleContainer.sort(new Object[] {"value"}, new boolean[] {true});
		return roleContainer;
	}
	
	public BeanItemContainer<SelectValue> getEmpNameForRole() {
		Query findAll = entityManager.createNamedQuery("TmpEmployee.findByEmpRole");
		List<TmpEmployee> resultRoleEmpList = (List<TmpEmployee>) findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> roleContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (TmpEmployee masRole : resultRoleEmpList) {
			select = new SelectValue();
			select.setId(masRole.getKey());
			select.setValue(masRole.getEmpId().toString() + " - " + masRole.getEmpFirstName());
			select.setCommonValue(masRole.getEmpRole());
			selectValuesList.add(select);
		}
		roleContainer.addAll(selectValuesList);
		roleContainer.sort(new Object[] {"value"}, new boolean[] {true});
		return roleContainer;
	}
	
	//Need to optimize
	public BeanItemContainer<SelectValue> getEmpNameByEmpIds(Set<String> employeeIds) {
		Set<TmpEmployee> employees = null; 
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> empNameContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue employeeNameSelect = null;
		if (employeeIds != null && employeeIds.size() > 0) {
			for (String empId : employeeIds) {
				Query query = entityManager.createNamedQuery("TmpEmployee.getEmployeeName");
				query = query.setParameter("empIds", empId);
				List<TmpEmployee> employeeList =  (List<TmpEmployee>) query.getResultList();
				if (employeeList != null && employeeList.size() > 0) {
					employees = new HashSet<TmpEmployee>();
					employees.addAll(employeeList);
					for (TmpEmployee employee : employees) {
						employeeNameSelect = new SelectValue();
						employeeNameSelect.setId(employee.getKey());
						employeeNameSelect.setValue(employee.getEmpFirstName());
						employeeNameSelect.setCommonValue(employee.getEmpId());
						selectValuesList.add(employeeNameSelect);
					}
				}
			}
		}
		empNameContainer.addAll(selectValuesList);
		empNameContainer.sort(new Object[] {"value"}, new boolean[] {true});
		return empNameContainer;
	}
	
	public String getEmployeeByName(String userName){
		
		String tmpEmployee = null;
		userName = userName.toLowerCase();
		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", userName);
		
		List<TmpEmployee> tmpEmployeeList = query.getResultList();
		
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			for (TmpEmployee tmpEmployeeName : tmpEmployeeList) {
				tmpEmployee = tmpEmployeeName.getEmpFirstName();
			}
		}
		return tmpEmployee;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRawCategory(String categoryType){
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if(categoryType != null){

			try{
				Query findRawcategoryByType = entityManager.createNamedQuery("RawCategory.findByType");
				findRawcategoryByType.setParameter("categoryType",categoryType);
				List<RawCategory> rawCategoryList = (List<RawCategory>) findRawcategoryByType.getResultList();

				if (!rawCategoryList.isEmpty()) {
					List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue = null;
					for (RawCategory rawCategory : rawCategoryList) {
						selectValue = new SelectValue();
						selectValue.setId(rawCategory.getKey().longValue());
						selectValue.setValue(rawCategory.getCategoryDescription());
						selectValueList.add(selectValue);
					}
					selectValueContainer.addAll(selectValueList);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return selectValueContainer;

	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getRawSubCategoryByRawCategoryKey(Long rawCategoryKey){
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if(rawCategoryKey != null){

			try{
				Query findRawSubCategoryByKey = entityManager.createNamedQuery("RawSubCategory.findByRawCategoryKey");
				findRawSubCategoryByKey.setParameter("rawCategoryKey",rawCategoryKey);
				List<RawSubCategory> rawSubCategoryList = (List<RawSubCategory>) findRawSubCategoryByKey.getResultList();

				if (!rawSubCategoryList.isEmpty()) {
					List<SelectValue> selectValueList = new ArrayList<SelectValue>();
					SelectValue selectValue = null;
					for (RawSubCategory rawSubCategory : rawSubCategoryList) {
						selectValue = new SelectValue();
						selectValue.setId(rawSubCategory.getKey().longValue());
						selectValue.setValue(rawSubCategory.getSubCategoryDescription());
						selectValueList.add(selectValue);
					}
					selectValueContainer.addAll(selectValueList);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return selectValueContainer;

	}
	

	public String getOpninonRole(String userName){
		
		String roleName = null;
		
		Query query = entityManager.createNamedQuery("MasOpinionRole.findByKey");
		query.setParameter("roleCodeKey", userName);
		
		List<MasOpinionRole> tmpEmployeeList = query.getResultList();
		
		if (tmpEmployeeList != null && !tmpEmployeeList.isEmpty()) {
			for (MasOpinionRole masOpinionRole : tmpEmployeeList) {
				roleName = masOpinionRole.getRoleDescription();
			}
		}
		return roleName;
	}

public UserBranchMapping getUserBranchAndZone(String userName){
	
	String roleName = null;
	
	Query query = entityManager.createNamedQuery("UserBranchMapping.findbyUserId");
	query.setParameter("userId", userName.toUpperCase());
	
	List<UserBranchMapping> userList = query.getResultList();
	
	if (userList != null && !userList.isEmpty()) {
		return userList.get(0);
	}
	return null;
}
public BeanItemContainer<SelectValue> getSelectValueContainerForBranch() {
	// Query findAll =
	// entityManager.createNamedQuery("CityTownVillage.findAll");
	
	Query findAll = entityManager
			.createNamedQuery("OrganaizationUnit.findAll");
	List<OrganaizationUnit> organizationList = (List<OrganaizationUnit>) findAll.getResultList();	
	BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
			SelectValue.class);
	if (!organizationList.isEmpty()) {
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		for (OrganaizationUnit organizationUnit : organizationList) {
			SelectValue selectValue = new SelectValue();
			selectValue.setId(Long.valueOf(organizationUnit.getOrganizationUnitId()));
			selectValue.setValue(organizationUnit.getOrganizationUnitName());
			selectValueList.add(selectValue);
		}
		selectValueContainer.addAll(selectValueList);
	}

	return selectValueContainer;
}

	public boolean getClsProsAllowedByPolicyNo(String policyNumber){
		boolean allowed = true;
		if(policyNumber != null && !policyNumber.isEmpty()){
			try{
				Query qry = entityManager.createNamedQuery("CashlessDisableMaster.findByPolicyNumber");
				qry.setParameter("policyNumber", policyNumber);
				List<CashlessDisableMaster> clsBlockList = qry.getResultList();
				if(clsBlockList != null && !clsBlockList.isEmpty()){
					allowed = false;
				}
			}
			catch(Exception e){
				e.printStackTrace();
				allowed = true;
			}
		}		
		return allowed;		
	}
	
	public List<SelectValue> getPrivateInvestigatorsZones(){
		
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findByUniqueZone");
		List<Object> listofZones = query.getResultList();
		
		List<SelectValue> selectZonesList = new ArrayList<SelectValue>();
		
		if(listofZones != null && !listofZones.isEmpty()){
			for(int i=0;i<listofZones.size();i++){
				
				Long masterKey = (Long)listofZones.get(i);
				TmpCPUCode zoneName = getMasCpuCode(masterKey);
//				String masterValue = (String)listofZones.get(i);
				if(null != zoneName){
					SelectValue selectValue = new SelectValue();
					selectValue.setId(masterKey);
					selectValue.setValue(zoneName.getDescription());
					selectZonesList.add(selectValue);
				}
			}
		}
		return selectZonesList;
	}
	
public List<SelectValue> getPrivateInvestigatorsZoneNames(){
		
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findByUniqueZoneName");
		List<Object> listofZones = query.getResultList();
		
		List<SelectValue> selectZonesList = new ArrayList<SelectValue>();
		
		if(listofZones != null && !listofZones.isEmpty()){
			for(int i=0;i<listofZones.size();i++){
				
				String masterKey = (String)listofZones.get(i);
				MasInvZone zoneName = getMasInvZoneName(masterKey);
//				String masterValue = (String)listofZones.get(i);
				if(null != zoneName){
					SelectValue selectValue = new SelectValue();
					selectValue.setId(Long.valueOf(zoneName.getKey()));
					selectValue.setValue(zoneName.getZoneName());
					selectZonesList.add(selectValue);
				}
			}
		}
		return selectZonesList;
	}
	
	public BeanItemContainer<MasPrivateInvestigator> getPrivateInvestigationCoordinator(Long selectedZone){
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findByZoneCode");
		query.setParameter("zoneCode", selectedZone);
		List<MasPrivateInvestigator> privateInvestigationList = query.getResultList();
		
		BeanItemContainer<MasPrivateInvestigator> privateInvCordinatorContainer = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		privateInvCordinatorContainer.addAll(privateInvestigationList);
		
		return privateInvCordinatorContainer;
	}
	
	public BeanItemContainer<MasPrivateInvestigator> getPrivateInvestigationCoordinatorName(String selectedZoneName){
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findByZoneName");
		query.setParameter("zoneName", selectedZoneName);
		List<MasPrivateInvestigator> privateInvestigationList = query.getResultList();
		
		BeanItemContainer<MasPrivateInvestigator> privateInvCordinatorContainer = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		privateInvCordinatorContainer.addAll(privateInvestigationList);
		
		return privateInvCordinatorContainer;
	}
	
	public BeanItemContainer<MasPrivateInvestigator> getPrivateInvestigatorNames(String selectedCoordinator) {
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findByCoordinatorCode");
		query.setParameter("coridnatorCode", selectedCoordinator);
		List<MasPrivateInvestigator> privateInvestigationList = query.getResultList();
		
		BeanItemContainer<MasPrivateInvestigator> privateInvestigatorNameContainer = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		privateInvestigatorNameContainer.addAll(privateInvestigationList);
		
		return privateInvestigatorNameContainer;
	}
	
	public BeanItemContainer<MasPrivateInvestigator> getPrivateInvestigatorsList(){
		Query query = entityManager
				.createNamedQuery("MasPrivateInvestigator.findAll");
		List<MasPrivateInvestigator> privateInvestigatorsList = query.getResultList();
		
		BeanItemContainer<MasPrivateInvestigator> privateInvestigators = new BeanItemContainer<MasPrivateInvestigator>(MasPrivateInvestigator.class);
		privateInvestigators.addAll(privateInvestigatorsList);
		
		return privateInvestigators;
	}
	
	public BeanItemContainer<SelectValue> getZoneCodes() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(cpuCode.getCpuCode());
			select.setValue(cpuCode.getDescription());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}
	
	public BeanItemContainer<SelectValue> getInvestigatorZoneNames() {

		Query findAll = entityManager.createNamedQuery("MasInvZone.findAll");
		@SuppressWarnings("unchecked")
		List<MasInvZone> resultCPUCodeList = (List<MasInvZone>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (MasInvZone invZone : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(invZone.getKey());
			select.setValue(invZone.getZoneName());
			selectValuesList.add(select);
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}
	
	public TmpCPUCode getMasCpuCode(Long cpuCode)
	{
		Query  query = entityManager.createNamedQuery("TmpCPUCode.findByCode");
		query = query.setParameter("cpuCode", cpuCode);
		
		List<TmpCPUCode> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty())
		{
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	
	public MasInvZone getMasInvZone(Long key)
	{
		Query  query = entityManager.createNamedQuery("MasInvZone.findByKey");
		query = query.setParameter("key", key);
		
		List<MasInvZone> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty())
		{
			return listOfTmpCodes.get(0);
		}
		return null;
	}
	public MasInvZone getMasInvZoneName(String key)
	{
		Query  query = entityManager.createNamedQuery("MasInvZone.findByZoneName");
		query = query.setParameter("zoneName", key);
		
		List<MasInvZone> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty())
		{
			return listOfTmpCodes.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueCategoryContainer(Long key) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(BranchManagerCategoryTable.class)
		.add(Restrictions.eq("activeStatus", "1"))
		.add(Restrictions.eq("fbMasterKey", key))
		.addOrder(Order.asc("feedbackCategory"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("feedbackCategory"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());


		return selectValueContainer;
	}
	
	public boolean checkSeniorDoctor(String argLoginId){ 
		boolean isSeniorDoc = false;
		Query qry = entityManager.createNamedQuery("MasOpinionSeniorDoctor.findByEmpId");
		qry.setParameter("empId", argLoginId);
		List<MasOpinionSeniorDoctor> empList = qry.getResultList();
		if(empList != null && empList.size() == 1){
			isSeniorDoc = true;
		}
		return isSeniorDoc;
	}
	
	//Magazine CR
/*<<<<<<< HEAD
	public List<ClaimMagazine> getMagazineByUserId(String userId) {
		Query query = entityManager.createNamedQuery("ClaimMagazine.findByUserId");
		query.setParameter("userId", userId.toUpperCase());
=======*/
	public List<ClaimMagazine> getMagazineByUserId(String userId,String magazineCode) {
		Query query = entityManager.createNamedQuery("ClaimMagazine.findByUserIdandCode");
		query.setParameter("userId", userId.toUpperCase());
		query.setParameter("magazineCode",magazineCode);
		List<ClaimMagazine> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList;
		}
		return null;
	}
	public List<MagazineDTO> getMagazineByCode(String magazineCode) {
		Query query = entityManager.createNamedQuery("MasMagazineDocument.findByMagazineCode");
		query.setParameter("magazineCode", magazineCode.toUpperCase());
		List<MagazineDTO> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList;
		}
		return null;
	}
	
	public void persistMagazine(String userId, MagazineDTO argMagDTO) {
		ClaimMagazine clmMag = new ClaimMagazine();
		clmMag.setUserId(userId);
		clmMag.setConfirmationFlag("Y");
		clmMag.setMagazineCode(argMagDTO.getMasMagCode());
		clmMag.setMagazineCategory(argMagDTO.getMasMagCategory());
		clmMag.setMagazineSubCategory(argMagDTO.getMasMagSubCategory());
		clmMag.setSubmitDate((new Timestamp(System.currentTimeMillis())));
		clmMag.setCreatedDate((new Timestamp(System.currentTimeMillis())));
		clmMag.setCreatedBy(userId);
		clmMag.setActiveFlag("Y");
		entityManager.persist(clmMag);
		entityManager.flush();
	}
	
	public List<MasMagazineDocument> getMasMagazine() {
		Query query = entityManager.createNamedQuery("MasMagazineDocument.findAll");
		List<MasMagazineDocument> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList;
		}
		return null;
	}
	public List<MasMagazineDocument> getAllMagazine() {
		Query query = entityManager.createNamedQuery("MasMagazineDocument.findAllMagazine");
		List<MasMagazineDocument> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList;
		}
		return null;
	}
	public Long getMagazineDocumentKey(String magazineCode) {
		Query query = entityManager.createNamedQuery("MasMagazineDocument.findByMagazineCode");
		query.setParameter("magazineCode", magazineCode);
		List<MasMagazineDocument> resultList = query.getResultList();
		if (resultList != null && !resultList.isEmpty()) {
			return resultList.get(0).getDocumentKey();
		}
		return null;
	}
	//Due to performance issue, the below code was commented
	/*public BeanItemContainer<SelectValue> getIcdCodes() {
		Query query = entityManager.createNamedQuery("IcdCode.findAll");
		List<IcdCode> resultList;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> icdCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

			resultList = query.getResultList();

			if (!resultList.isEmpty()) {
				SelectValue selectValue = null;
				for (IcdCode icdCode : resultList) {
					selectValue = new SelectValue();
					selectValue.setId(icdCode.getKey());
					String value = icdCode.getValue();
					if(value != null && icdCode.getDescription() != null){
						value = icdCode.getDescription() + " - "+ value;
					}
					selectValue.setValue(value);
					selectValueList.add(selectValue);
				}
			}
			icdCodeContainer.addAll(selectValueList);

		return icdCodeContainer;
	}*/
	
	public SelectValue getIcdDescriptionKey(Long key) {

		Query query = entityManager.createNamedQuery("IcdCode.findByKey")
				.setParameter("primaryKey", key);

		IcdCode icdCodeList = (IcdCode) query.getSingleResult();
		SelectValue selectValue = null;
		if (icdCodeList != null) {
			selectValue = new SelectValue();
			selectValue.setId(icdCodeList.getKey());
			String value = icdCodeList.getValue();
			if(value != null && icdCodeList.getDescription() != null){
				value = icdCodeList.getDescription() + " - "+ value;
			}
			selectValue.setValue(value);
			selectValue.setCommonValue(icdCodeList.getDescription());
			return selectValue;
		}
		return null;
	}
	
	public BeanItemContainer<SelectValue> getTmpCpuCodesForNegotiation() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			if(cpuCode != null && cpuCode.getNegotiationFlag() != null && cpuCode.getNegotiationFlag().equals(SHAConstants.YES_FLAG)){
				select = new SelectValue();
				select.setId(cpuCode.getCpuCode());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}
	
	public BeanItemContainer<SelectValue> getTmpCpuCodesForNegotiationProcess() {

		Query findAll = entityManager.createNamedQuery("TmpCPUCode.findAll");
		@SuppressWarnings("unchecked")
		List<TmpCPUCode> resultCPUCodeList = (List<TmpCPUCode>) findAll
		.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (TmpCPUCode cpuCode : resultCPUCodeList) {
			if(cpuCode != null && cpuCode.getNegotiationFlag() != null && cpuCode.getNegotiationFlag().equals(SHAConstants.YES_FLAG)){
				select = new SelectValue();
				select.setId(cpuCode.getKey());
				select.setValue(cpuCode.getCpuCode().toString() + " - " + cpuCode.getDescription());
				selectValuesList.add(select);
			}
		}
		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;
	}
	
	public List<MasHospitalUserMapping> getUserMappedHospitals(String userId){
		
		Query findByUserId = entityManager.createNamedQuery("MasHospitalUserMapping.getHospitalsbyUserId");
		findByUserId.setParameter("userId", userId);
		
		List<MasHospitalUserMapping> resultList = (List<MasHospitalUserMapping>) findByUserId.getResultList();
		
		if(resultList != null && !resultList.isEmpty()){
			return resultList;
		}
		
		return null;
	}

	public TataPolicy getTataPolicy(String policyNumber){
		 Query query = entityManager.createNamedQuery("TataPolicy.findByPolicyNumber");
		 query.setParameter("policyNumber", policyNumber);
		    List<TataPolicy> tataPolicy = (List<TataPolicy>)query.getResultList();
		    if(tataPolicy != null && ! tataPolicy.isEmpty()){
		    	return tataPolicy.get(0);
		    }
		    return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<SelectValue> getIcdCodeList(String icdCodeString) {
		Session session = (Session) entityManager.getDelegate();
		Criterion c1 = Restrictions.like("description", "%"+icdCodeString+"%").ignoreCase();
	    Criterion c3 = Restrictions.like("value", "%"+icdCodeString+"%").ignoreCase();
	    Criterion c4 = Restrictions.or(c1, c3);
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(IcdCode.class)
										.add(Restrictions.eq("activeStatus", (long) 1))
										.add(c4)
										.addOrder(Order.asc("description"))
										.setProjection(Projections.projectionList()
														.add(Projections.property("key"), "id")
														.add(Projections.property("value"), "value")
														.add(Projections.property("description"),"commonValue"))
														.setMaxResults(20)
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		for (SelectValue selectValue : selectValuesList) {
			String value = selectValue.getValue();
			
			if(value != null && selectValue.getCommonValue() != null){
				String tmpValue = selectValue.getValue();
				value = selectValue.getCommonValue() + " - "+ value;
				selectValue.setValue(value);
				selectValue.setCommonValue(tmpValue);
			}
		}
//		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
//				SelectValue.class);
//		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());
		//		return selectValueContainer;

		return selectValuesList;
	}

	public BeanItemContainer<SpecialSelectValue> getRoleTypeBYCategory(String roleType) {


		Session session = (Session) entityManager.getDelegate();

		Criteria createCriteria = session.createCriteria(MasRoleLimit.class);

		@SuppressWarnings("unchecked")
		List<SpecialSelectValue> selectValuesList = createCriteria
				.add(Restrictions.eq("roleType", roleType))
						.setProjection(
				Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("roleId"), "value")
				.add(Projections.property("maxAmt"),"specialId")) 
				.setResultTransformer(
						org.hibernate.transform.Transformers
						.aliasToBean(SpecialSelectValue.class)).list();
		BeanItemContainer<SpecialSelectValue> selectValueContainer = new BeanItemContainer<SpecialSelectValue>(
				SpecialSelectValue.class);
		for (SpecialSelectValue selectValue : selectValuesList) {
			selectValue.setSpecialValue(roleType);
		}
		
		selectValueContainer.addAll(selectValuesList);
		
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getRevisedRejectionCategoryByValue(Long productKey) {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(productKey) || ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(productKey)) {
			
//			selectCriteria = selectCriteria.add(Restrictions.like("code", ReferenceTable.REJECTION_CATEGORY+"%"));
			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_CNCR)));
		}
		else if(ReferenceTable.COMPREHENSIVE_27_PRODUCT.equals(productKey) 
				|| ReferenceTable.COMPREHENSIVE_37_PRODUCT.equals(productKey)
				|| ReferenceTable.STAR_FIRST_COMPREHENSVE.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL.equals(productKey)
				|| ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL.equals(productKey)) {

			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_COMPHN)));
		}
		else if(ReferenceTable.STAR_CARDIAC_CARE.equals(productKey)
				|| ReferenceTable.STAR_CARDIAC_CARE_POLICY.equals(productKey)
				|| ReferenceTable.STAR_CARDIAC_CARE_NEW.equals(productKey)) {

			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL),Restrictions.eq("code", ReferenceTable.REJECTION_CARDIAC)));
		}	
		else if(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM.equals(productKey)) {

			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL),Restrictions.eq("code", ReferenceTable.REJECTION_CARDIAC_PLAT)));
		}	
		else if(!ReferenceTable.getGMCProductList().containsKey(productKey)) {
			
			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL)));
		}
		else{
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY));
		}
		
		
		selectValueList = selectCriteria.addOrder(Order.asc("orderKey"))
				.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		return selectValueContainer;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	//R1295
	public BeanItemContainer<SelectValue> getOpinionQueryType()	{
		Stage temp = null;
		List<SelectValue> selectValueList = new ArrayList<SelectValue>();
		Map<Long, String> tempVal = new  HashMap<>();
		tempVal.put(1771L, "New Query");
		tempVal.put(1772L, "Query Reminder");
		for (Map.Entry<Long, String> entry : tempVal.entrySet()) {
			SelectValue selected = new SelectValue();
			selected.setId(entry.getKey());
			selected.setValue(entry.getValue());
			selectValueList.add(selected);
		}
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
		return container;
	}
	
		//OP&HC
		
		public BeanItemContainer<SelectValue> getOPReason()	{
			Query query = entityManager.createNamedQuery("MastersValue.findByMappingCode");
			query = query.setParameter("mappingCode", "OPHC");

			List<MastersValue> mastersValueList = query.getResultList();
			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			for (MastersValue mastersValue : mastersValueList) {
				SelectValue select = new SelectValue();
				select.setId(mastersValue.getKey());
				select.setValue(mastersValue.getValue());
				selectValuesList.add(select);
			}
			selectValueContainer.addAll(selectValuesList);		

			return selectValueContainer;
		}
		
		@SuppressWarnings("unchecked")
		public BeanItemContainer<SelectValue> getOPClaimTypeSelectValueContainer(String a_key) {
			List<String> toBeRemovedList = new ArrayList<String>();
			toBeRemovedList.add("Cashless");
			toBeRemovedList.add("Reimbursement");

			List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
			Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKey");
			query = query.setParameter("parentKey", a_key);
			List<MastersValue> mastersValueList = query.getResultList();
			if (!mastersValueList.isEmpty()) {
				for (MastersValue master : mastersValueList) {
					if(!toBeRemovedList.contains(master.getValue())){
						SelectValue selectValue = new SelectValue();
						selectValue.setId(master.getKey().longValue());
						selectValue.setValue(master.getValue());
						selectValuesList.add(selectValue);
					}
				}
			}

			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
			selectValueContainer.addAll(selectValuesList);
			return selectValueContainer;
		}
		
		public BeanItemContainer<SelectValue> getSelectValueContainerForPIOCode() {
			Query query = entityManager
					.createNamedQuery("OrganaizationUnit.findDistinctBranchCode");
			
			//query = query.setParameter("branchCode", branchCode);
			List<String> organizationList = (List<String>) query.getResultList();	
			BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
					SelectValue.class);
			if (!organizationList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				Long count = 1L;
				for (String organizationUnit : organizationList) {
					SelectValue selectValue = new SelectValue();
					selectValue.setId(count);
					selectValue.setValue(organizationUnit);
					selectValueList.add(selectValue);
					count++;
				}
				selectValueContainer.addAll(selectValueList);
			}

			return selectValueContainer;
		}
		
		
		
		public BeanItemContainer<SelectValue> getOPBillingCategoryTypes() {
			Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
			query = query.setParameter("masterTypeCode", "OPBILLTYPE");
			List<MastersValue> mastersValueList = query.getResultList();
			BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);		
			return resultContainer;
		}
		
		public BeanItemContainer<SelectValue> getOPReceivedStatus() {
			List<SelectValue> selectValueList = new ArrayList<SelectValue>();
			Map<Long, String> tempVal = new  HashMap<Long, String>();
			tempVal.put(1L, "Original");
			tempVal.put(2L, "Photocopy");

			for (Map.Entry<Long, String> entry : tempVal.entrySet()) {
				SelectValue selected = new SelectValue();
				selected.setId(entry.getKey());
				selected.setValue(entry.getValue());
				selectValueList.add(selected);
			}
			BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(selectValueList);
			return container;
		}
	
	public BeanItemContainer<SelectValue> getEnhRejectionCategoryByValue() {

		return getSortedMasterBsedOnMasterTypeCode(ReferenceTable.ENH_REJECTION_CATEGORY);
		
	}

	public BeanItemContainer<SelectValue> getSortedMasterBsedOnMasterTypeCode(String masterTypeCode) {
		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		
		selectCriteria = selectCriteria.add(Restrictions.eq("code", masterTypeCode));
				
		selectValueList = selectCriteria.addOrder(Order.asc("orderKey"))
				.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		return selectValueContainer;
	}
	
	
	public BeanItemContainer<SelectValue> getCVCProcessorValueContainer(Long intimationKey) {

		Connection connection = null;
		CallableStatement cs = null;
		BeanItemContainer<SelectValue> processorContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		ResultSet rset = null;
		try {

			connection = BPMClientContext.getConnection();

			cs = connection.prepareCall("{call PRC_CVC_PROCESSOR_DTLS(?,?)}");		
			cs.setLong(1, intimationKey);
			cs.registerOutParameter(2, OracleTypes.CURSOR, "SYS_REFCURSOR");
			cs.execute();

			rset = (ResultSet) cs.getObject(2);

			if (null != rset) {					
				while (rset.next()) {
					String userDtls = rset.getString("USER_DTL");
					SelectValue employee = new SelectValue(userDtls);
					processorContainer.addBean(employee);
					
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rset != null) {
					rset.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return processorContainer;
	}
	
	public AuditDetails getCVCAuditActionIntimation(Long intimationKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKeyWithPending");
		query.setParameter("intimationKey", intimationKey);
		query.setParameter("remediationStatus", SHAConstants.CVC_PENDING);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	public BeanItemContainer<SelectValue> getCVCErrorCategoryByMaster() {
		Query query = entityManager.createNamedQuery("AuditMasCategory.findAll");
		query = query.setParameter("activeStatus", SHAConstants.YES_FLAG);
		List<AuditMasCategory> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (AuditMasCategory value : mastersValueList) {
			SelectValue select = new SelectValue();
//			select.setId(value.getKey());
			select.setValue(value.getKey().toString());
			select.setCommonValue(value.getCategory());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public List<AuditTeam> getCVCAuditActionTeam(Long auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditTeam.findByAuditActiveStatus");
		query.setParameter("auditKey", auditKey);

		List<AuditTeam> cvcAuditIntimationList = (List<AuditTeam>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	public List<AuditCategory> getCVCAuditActionCategory(Long auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditCategory.findByAuditActiveStatus");
		query.setParameter("auditKey", auditKey);

		List<AuditCategory> cvcAuditIntimationList = (List<AuditCategory>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	public List<AuditProcessor> getCVCAuditActionProcessor(Long auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditProcessor.findByAuditActiveStatus");
		query.setParameter("auditKey", auditKey);

		List<AuditProcessor> cvcAuditIntimationList = (List<AuditProcessor>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList;
		}

		return null;

	}
	
	public AuditMasCategory getCVCMasCategoryValue(Long categoryKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditMasCategory.findByKey");
		query.setParameter("key", categoryKey);

		List<AuditMasCategory> cvcCategoryList = (List<AuditMasCategory>) query.getResultList();

		if(cvcCategoryList !=null && !cvcCategoryList.isEmpty())
		{
			return cvcCategoryList.get(0);
		}

		return null;

	}
	public AuditDetails getCVCAuditActionByintimationKey(Long auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKey");
		query.setParameter("intimationKey", auditKey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();


		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	public BeanItemContainer<SelectValue> getRejSubcategContainer(Long rejCategId) {
		
		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MasRejectSubCategory.class).add(Restrictions.eq("activeStatus", 1));
		
		selectCriteria = selectCriteria.add(Restrictions.eq("masRejCategKey", rejCategId));
				
		selectValueList = selectCriteria.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		return selectValueContainer;
	}

	//CR2019161 - Consent Letter from Insured for Investigation of Reimbursement Claims
	public DocumentCheckListDTO getDocumentCheckListVal(Long categoryKey) 
	{
		DocumentCheckListDTO dto = new DocumentCheckListDTO();
		Query query = entityManager
				.createNamedQuery("DocumentCheckListMaster.findByKey");
		query.setParameter("primaryKey", categoryKey);
		DocumentCheckListMaster masterValue = (DocumentCheckListMaster) query
				.getSingleResult();
			dto.setKey(masterValue.getKey());
			dto.setValue(masterValue.getValue());
			dto.setMandatoryDocFlag("Y");
			dto.setRequiredDocType(masterValue.getRequiredDocType());
			return dto;
	}	
	
	public MastersValue getMasterCodeFlag(String code) {
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCodeFOrBancs");
		query.setParameter("code", code);
		List<MastersValue> masterCodeValues = (List<MastersValue>) query.getResultList();
		if(masterCodeValues !=null && !masterCodeValues.isEmpty())
		{
			return masterCodeValues.get(0);
		}
		return null;
		
	}
	public List<SelectValue> getRelationshipContainerValues() {
		List<SelectValue> list = new ArrayList<SelectValue>();
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MasLegalRelationship.class).add(Restrictions.eq("activeStatus", "Y"));
		list = selectCriteria.setProjection(Projections.projectionList().add(Projections.property("key"),"id")
				.add(Projections.property("realtionshipDesc"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		return list;
		
	}
	/*@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getNatureCauseSelectValueContainer(String a_key,List<String> healthPa) {
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MasUserReference.class)
		.add(Restrictions.eq("activeStatus", 1))
		.add(Restrictions.eq("code", a_key))
		.add(Restrictions.in("masterCode", healthPa))
		.addOrder(Order.asc("value"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		//selectValueContainer.sort(new Object[] {"value"}, new boolean[] {true});
		System.out.println("---------------- Second Approach End current time " + new Date());


		return selectValueContainer;
	}*/
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getNatureCauseLossSelectValueContainer(String mappingCode,String code) {
	Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCodeAndCode");
	query.setParameter("code",code);
	query.setParameter("mappingCode","%"+mappingCode+"%");
	List<MastersValue> resultList = query.getResultList();
	BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
			SelectValue.class);
	if (resultList != null && !resultList.isEmpty()) {
	for (MastersValue masUserReference : resultList) {
		SelectValue value = new SelectValue();
		value.setValue(masUserReference.getValue());
		value.setId(masUserReference.getKey());
		selectValueContainer.addItem(value);
	}
	return selectValueContainer;
	}
	return null;
	
	
}
	
	public SelectValue getMasterValueForNatureCause(Long key) {
		
		Query query = entityManager.createNamedQuery("MastersValue.findByKey");
		query.setParameter("parentKey",key);
		MastersValue resultList = (MastersValue) query.getSingleResult();
		SelectValue masterCode = null;
		if (resultList != null) {
			masterCode = new SelectValue();
				masterCode.setValue(resultList.getValue());
				masterCode.setId(resultList.getKey());
			return masterCode;
			}
			return null;
	}
	
	public BeanItemContainer<SelectValue> getCatastrophicLossList() {
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(CatastropheData.class)
										.add(Restrictions.eq("activeStatus", "Y"))
										.addOrder(Order.asc("catastropheDesc"))
										.setProjection(Projections.projectionList()
										.add(Projections.property("key"), "id")
										.add(Projections.property("catastropheDesc"), "value"))
										.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		return selectValueContainer;	
	
	}
	
	
public SelectValue getCatastropheData(Long key) {
		
		Query query = entityManager.createNamedQuery("CatastropheData.findByKey");
		query.setParameter("key",key);
		CatastropheData resultList = (CatastropheData)query.getSingleResult();
		SelectValue value = null;
		if (resultList != null) {
			value = new SelectValue();
				value.setId(resultList.getKey());
				value.setValue(resultList.getCatastropheDesc());
				
			return value;
			}
			return null;
	}

	public List<LegalHeir> getlegalHeirListByTransactionKey(Long rodKey, EntityManager em) {
		this.entityManager = em;
		
		return getlegalHeirListByTransactionKey(rodKey);
	}
	public List<LegalHeir> getlegalHeirListByTransactionKey(Long rodKey) {
		Query query = entityManager.createNamedQuery("LegalHeir.findByTransactionKey");
		query.setParameter("transacKey", rodKey);
		List<LegalHeir> resultList = query.getResultList();
		if(!resultList.isEmpty() && resultList != null) {
			return resultList;
		}
		return null;
		
	}
	
	public Product getProductBySource(String productCode){

		Product product = null;
		Query query = entityManager.createNamedQuery("Product.findBySourceCode");
		query = query.setParameter("productCode", productCode);	
		List resultList = query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			product = (Product) query.getResultList().get(0);	
		} else {
			System.out.println("This product is not available in Galaxy------------->" + productCode);
		}

		return product;
	}

	public boolean getLegalHeirDocAvailableByIntimation(Long docTypeId, String intimationId) {
		
		MastersValue docTypeMas = getMaster(docTypeId);
		String docToken = getDocumentDetailsByDocType(docTypeMas.getValue(), intimationId);
		boolean available = docToken != null && !docToken.isEmpty() ? true : false;  
		
		return available;
	}
	
	public BeanItemContainer<SelectValue> getRevisedRejectWithdrawCategoryByValue(Long productKey) {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(productKey) || ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(productKey)) {
			
			selectCriteria = selectCriteria.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECT_WITHDRAWAL_REASON_RETAIL),Restrictions.eq("code", ReferenceTable.REJECT_WITHDRAWAL_REASON_RETAIL_CNCR)));
		}
		else{
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECT_WITHDRAWAL_REASON_RETAIL));
		}
		
				
		selectValueList = selectCriteria.addOrder(Order.asc("orderKey"))
				.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		return selectValueContainer;
	}
	
	//added for new product
	public BeanItemContainer<SelectValue> getDiagnosisHospitalCash() {
		Query query = entityManager.createNamedQuery("DiagnosisHospitalDetails.findAll");
//		query = query.setParameter("activeStatus", 1);
		List<DiagnosisHospitalDetails> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (DiagnosisHospitalDetails value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getDiagnosisHospitalKey());
			select.setValue(value.getDiagnosisName());
//			select.setCommonValue(value.getDiagnosisName());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getHospitalCashDueTo() {
		Query query = entityManager.createNamedQuery("MasHospitalCashBenefit.findAll");
//		query = query.setParameter("activeStatus", 1);
		List<MasHospitalCashBenefit> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (MasHospitalCashBenefit value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getBenefitName());
//			select.setCommonValue(value.getDiagnosisName());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
		
	}
	
	/*PCC Flag in FA*/
	public BeanItemContainer<SelectValue> getPCCFlagTypes() {
		
		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", ReferenceTable.PCC_REMARK_FLAG);
		List<MastersValue> mastersValueList = query.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		for (MastersValue mastersValue : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getMappingCode());
			selectValuesList.add(select);
		}
		resultContainer.addAll(selectValuesList);
		return resultContainer;
	}
	
	public SelectValue addQualification(String qualification) {
		TreatingDoctorQualification treatingDoctorQualification = new TreatingDoctorQualification();
		treatingDoctorQualification.setQualification(qualification);
		treatingDoctorQualification.setActiveStatus(1l);
		treatingDoctorQualification.setCreatedBy("IMS");
		treatingDoctorQualification.setCreatedDate(new Date());
		treatingDoctorQualification.setModifiedDate(new Date());
		entityManager.persist(treatingDoctorQualification);
		entityManager.flush();
		
		return new SelectValue(treatingDoctorQualification.getKey(),treatingDoctorQualification.getQualification());	
	}
	
	public Boolean searchQualificationByValue(String qualificationValue){

		Query findAll = entityManager
				.createNamedQuery("TreatingDoctorQualification.findByQualification");
		findAll.setParameter("qualification", qualificationValue.toLowerCase());
		List<TreatingDoctorQualification> procdList = findAll.getResultList();
		if(procdList == null || procdList.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getDoctorQualification() {
		
		Query query = entityManager.createNamedQuery("TreatingDoctorQualification.findAll");
		List<TreatingDoctorQualification> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (TreatingDoctorQualification value : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getQualification());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getQualificationByvalue(String qualifi){

		BeanItemContainer<SelectValue> procedureListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		Query query = entityManager.createNamedQuery("TreatingDoctorQualification.findByQualification")
				.setParameter("qualification", qualifi);
		List<TreatingDoctorQualification> procedureMaster = (List<TreatingDoctorQualification>) query.getResultList();
		if(procedureMaster != null && !procedureMaster.isEmpty()) {
			List<SelectValue> listProcedures = new ArrayList<SelectValue>();
			for (TreatingDoctorQualification TreatingDoctorMaster : procedureMaster) {
				SelectValue selectSpaciality = new SelectValue();
				selectSpaciality.setId(TreatingDoctorMaster.getKey().longValue());
				selectSpaciality.setValue(TreatingDoctorMaster.getQualification());
				listProcedures.add(selectSpaciality);
			}
			procedureListContainer.addAll(listProcedures);
		}
		return procedureListContainer; 
	}
	
	public String getAgentPercentage(String a_key) {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterListKey");
		query = query.setParameter("parentKey",a_key);
		List<MastersValue> mastersValueList = query.getResultList();
		if(null != mastersValueList && !mastersValueList.isEmpty()){
			MastersValue masValue = mastersValueList.get(0);
			String masterValue  = null;
			if(null != masValue){
				masterValue = masValue.getValue();
			}
			return masterValue;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public BeanItemContainer<SelectValue> getNextLOVSpecialityType() {
		
		BeanItemContainer<SelectValue> nextLovBean = new BeanItemContainer<>(SelectValue.class);
		Query findAll = entityManager.createNamedQuery("SpecialityType.findBygetNextLOV");
		List<SpecialityType> specialityLOVList = (List<SpecialityType>) findAll
				.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		SelectValue select = null;
		for (SpecialityType speciality : specialityLOVList) {
			select = new SelectValue();
			select.setId(speciality.getKey());
			select.setValue(speciality.getValue());
			selectValuesList.add(select);
		}
		nextLovBean.addAll(selectValuesList);
		return nextLovBean;
	}

	public BeanItemContainer<SelectValue> getAutoAllocationDocumentType(){
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCodeAutoAllocationType");
		query.setParameter("masterTypeCode", ReferenceTable.AUTO_ALLOCATION_DOCUMENT_TYPE);
		List<MastersValue> mastersValueList = query.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getClaimFlageType(){
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCodeAutoAllocationType");
		query.setParameter("masterTypeCode", ReferenceTable.CLAIM_FLAG_TYPE);
		List<MastersValue> mastersValueList = query.getResultList();
		
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (MastersValue value : mastersValueList) {
			select = new SelectValue();
			select.setId(value.getKey());
			select.setValue(value.getValue());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	

	public BeanItemContainer<SelectValue> getOPPedValues() {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", "DIAGIMPT");
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getOPResultContainer(mastersValueList);		
		return resultContainer;
	}
	
	private BeanItemContainer<SelectValue> getOPResultContainer(List<MastersValue> a_mastersValueList) {
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		for (MastersValue mastersValue : a_mastersValueList) {
			SelectValue select = new SelectValue();
			if(mastersValue.getValue().equalsIgnoreCase(SHAConstants.RELATED) || mastersValue.getValue().equalsIgnoreCase(SHAConstants.NONRELATED)){
				select.setId(mastersValue.getKey());
				select.setValue(mastersValue.getValue());
				selectValuesList.add(select);
			}
		}
		resultContainer.addAll(selectValuesList);
		
		return resultContainer;
	}	


	public BeanItemContainer<SelectValue> getEmployeesContainerIncludingInactiveEmployees(NewIntimationDto newIntimationDto,RRCDTO rrcDTO)
	{
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		try{	
			Query findAll = entityManager.createNamedQuery("TmpEmployee.findAllEmployees");
			List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
			if (!employeeNameList.isEmpty()) {
				List<SelectValue> selectValueList = new ArrayList<SelectValue>();
				SelectValue selectValue = null;
				for (TmpEmployee employee : employeeNameList) {
					selectValue = new SelectValue();
					selectValue.setId(employee.getKey().longValue());
					selectValue.setValue(employee.getEmpId()+"-"+employee.getEmpFirstName());
					selectValueList.add(selectValue);
				}
				selectValueContainer.addAll(selectValueList);
			}
			/*if(newIntimationDto !=null &&
					newIntimationDto.getKey() !=null){
				Query query = entityManager.createNamedQuery("StageInformation.findEmpIdsByIntimationKey");
				query = query.setParameter("intimationkey", newIntimationDto.getKey());
				List<String> employeeIds = (List<String>) query.getResultList();
				if(rrcDTO.getStrUserName() !=null){
					employeeIds.add(rrcDTO.getStrUserName());
				}
				employeeIds.replaceAll(String::toUpperCase);
				if(employeeIds !=null && !employeeIds.isEmpty()){
					Query findAll = entityManager.createNamedQuery("TmpEmployee.findEmpListByEmpIds");
					findAll = findAll.setParameter("empList", employeeIds);
					List<TmpEmployee> employeeNameList = (List<TmpEmployee>) findAll.getResultList();
					
				}
			}*/
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getAuditReplyUserRoles() {
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
				
		mastersValueContainer.addBean(new SelectValue(1L, SHAConstants.CVC_AUDIT_QRY_RPL_ZONAL_USER));
		mastersValueContainer.addBean(new SelectValue(2L, SHAConstants.CVC_AUDIT_QRY_RPL_CLUSTER_HEAD));
		mastersValueContainer.addBean(new SelectValue(3L, SHAConstants.CVC_AUDIT_QRY_RPL_DIVISION_HEAD));
		mastersValueContainer.addBean(new SelectValue(4L, SHAConstants.CVC_AUDIT_QRY_RPL_UNIT_HEAD));
		
		return mastersValueContainer;

	}
	
	public Boolean getCVCAuditQryFlagByIntimationKey(Long intimationKey)
	{
		try{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByKey");
		query.setParameter("intimationKey", intimationKey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();

		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return true;
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getRRCSubCatValues(Long categoryId) {

		Query query = entityManager.createNamedQuery("RRCSubCategory.findSubCatByCategoryId");
		query = query.setParameter("categoryId", categoryId);

		List<RRCSubCategory> rrcSubCategories =  (List<RRCSubCategory>)query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> SubCatValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (RRCSubCategory subCategory : rrcSubCategories) {
			if(subCategory !=null){
				select = new SelectValue();
				select.setId(subCategory.getKey());
				select.setValue(subCategory.getSubCategoryName());
				selectValuesList.add(select);
			}
		}
		SubCatValueContainer.addAll(selectValuesList);
		return SubCatValueContainer;	
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue> getRRCSourceValues(Long subCategoryId) {

		Query query = entityManager.createNamedQuery("RRCCategorySource.findSourceBySubCatId");
		query = query.setParameter("subCategoryId", subCategoryId);

		List<RRCCategorySource> rrcCategoriesSource = (List<RRCCategorySource>) query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> sourceValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		SelectValue select = null;
		for (RRCCategorySource catSource : rrcCategoriesSource) {
			select = new SelectValue();
			select.setId(catSource.getKey());
			select.setValue(catSource.getSourceName());
			selectValuesList.add(select);
		}
		sourceValueContainer.addAll(selectValuesList);
		return sourceValueContainer;	
	}
	
	public MasClmAuditUserMapping getAuditUserByEmpId(String userId) {
	
		MasClmAuditUserMapping clmAuditUser = null;
		try {
			Query findEmpByTypeQuery = entityManager.createNamedQuery("MasClmAuditUserMapping.findByEmpId");
			findEmpByTypeQuery.setParameter("userId", userId.toLowerCase());
			List<MasClmAuditUserMapping> auditUserList = (List<MasClmAuditUserMapping>) findEmpByTypeQuery.getResultList();
			if(auditUserList != null && !auditUserList.isEmpty()) {
				clmAuditUser = auditUserList.get(0); 
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return clmAuditUser;
	}
	
	public AuditDetails getCVCAuditActionByAuditKey(Long auditKey)
	{
		Query query = entityManager
				.createNamedQuery("AuditDetails.findByAuditKey");
		query.setParameter("key", auditKey);

		List<AuditDetails> cvcAuditIntimationList = (List<AuditDetails>) query.getResultList();


		if(cvcAuditIntimationList !=null && !cvcAuditIntimationList.isEmpty())
		{
			return cvcAuditIntimationList.get(0);
		}

		return null;

	}
	
	public List<ClaimRemarksAlerts> getClaimsAlertsByIntitmation(String intitmationNo){
		Query  query = entityManager.createNamedQuery("ClaimRemarksAlerts.findByIntimationnoWithStatus");
		query = query.setParameter("intitmationNo", intitmationNo);
		List<ClaimRemarksAlerts> listOfClaimAlerts = query.getResultList();

		return listOfClaimAlerts;
	}
	
	public List<ClaimRemarksAlerts> getClaimsAlertsByIntitmationCatKey(String intitmationNo,List<Long> catlist){
		Query  query = entityManager.createNamedQuery("ClaimRemarksAlerts.findByfindByIntimationCatKey");
		query = query.setParameter("intitmationNo", intitmationNo);
		query = query.setParameter("catList", catlist);
		List<ClaimRemarksAlerts> listOfClaimAlerts = query.getResultList();

		return listOfClaimAlerts;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerForEmail(String a_key) {
		
		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
		.add(Restrictions.eq("activeStatus", 1))
		.add(Restrictions.eq("code", a_key))
		.addOrder(Order.asc("value"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		List<SelectValue> emailList = new ArrayList<SelectValue>();
		if(selectValuesList != null && !selectValuesList.isEmpty()){
			for (SelectValue selectValue : selectValuesList) {
				if(selectValue != null && selectValue.getValue() != null
						&& selectValue.getValue().equalsIgnoreCase("Email")){
					SelectValue emailSelectValue = new SelectValue();
					emailSelectValue.setId(selectValue.getId());
					emailSelectValue.setValue(selectValue.getValue());
					emailList.add(emailSelectValue);
				}
			}
		}
		selectValueContainer.addAll(emailList);
		System.out.println("---------------- Second Approach End current time " + new Date());
		return selectValueContainer;
	}
	
	//Added for product MED-PRD-083
	public List<MasProcedureDetailsMapping> getProcedureDetailsMapping(
			Long productKey) {
		Query query = entityManager
				.createNamedQuery("MasProcedureDetailsMapping.findByProduct");
		query.setParameter("productKey", productKey);
		List<MasProcedureDetailsMapping> resultList = (List<MasProcedureDetailsMapping>) query
				.getResultList();
		return resultList;
	}
	
	//Added for version
		public List<MasProcedureDetailsMapping> getProcedureDetailsMappingwithVersion(
				Long productKey, Long versionNumber) {
			Query query = entityManager
					.createNamedQuery("MasProcedureDetailsMapping.findByVersion");
			query.setParameter("productKey", productKey);
			query.setParameter("productversionNumber", versionNumber);
			List<MasProcedureDetailsMapping> resultList = (List<MasProcedureDetailsMapping>) query
					.getResultList();
			return resultList;
		}
		

	public MasUser getMasUserByID(String userID){
		Query  query = entityManager.createNamedQuery("MasUser.getById");
		query = query.setParameter("userId", userID.toLowerCase());
		List<MasUser> employeeNameList = query.getResultList();
		if(employeeNameList !=null && !employeeNameList.isEmpty()){
			return employeeNameList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getBehvrHospSelectValueContainer(String a_key) {

		Session session = (Session) entityManager.getDelegate();
		@SuppressWarnings("unchecked")
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
		.add(Restrictions.eq("activeStatus", 1))
		.add(Restrictions.eq("code", a_key))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		System.out.println("---------------- Second Approach End current time " + new Date());


		return selectValueContainer;
	}
	
	public BeanItemContainer<SelectValue> getRevisedRejectionCategoryByValueForCoron(Long productKey ,String policyPlan ) {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		
		if(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)) {
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_INDI));

		}
		else if(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)) {
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_LUMPSUM));
		}
		else if(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)) {

			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJ_CATG_GRP_INDIMEN_93));
		}
		else if(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)) {

			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJ_CATG_GRP_LUMPSUM_93));
		}
		else{
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY));
		}
		
				
		selectValueList = selectCriteria.addOrder(Order.asc("key"))
							.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		Criteria selectCriteria2 = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		List<SelectValue> selectValueList2 = new ArrayList<SelectValue>(); 
		
		if(ReferenceTable.STAR_CANCER_GOLD_PRODUCT_KEY.equals(productKey) || ReferenceTable.STAR_CANCER_PLATINUM_PRODUCT_KEY_IND.equals(productKey)) {
			
			selectCriteria2 = selectCriteria2.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_CNCR)));
		}
		else if(ReferenceTable.COMPREHENSIVE_27_PRODUCT.equals(productKey) 
				|| ReferenceTable.COMPREHENSIVE_37_PRODUCT.equals(productKey)
				|| ReferenceTable.STAR_FIRST_COMPREHENSVE.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL.equals(productKey)
				|| ReferenceTable.STAR_COMPREHENSIVE_IND_PRODUCT_KEY.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_88_PRODUCT_FLOATER.equals(productKey)
				|| ReferenceTable.COMPREHENSIVE_88_PRODUCT_INDIVIDUAL.equals(productKey)) {

			selectCriteria2 = selectCriteria2.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_COMPHN)));
		}
		else if(ReferenceTable.STAR_CARDIAC_CARE.equals(productKey)
				|| ReferenceTable.STAR_CARDIAC_CARE_POLICY.equals(productKey)
				|| ReferenceTable.STAR_CARDIAC_CARE_NEW.equals(productKey)) {

			selectCriteria2 = selectCriteria2.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL),Restrictions.eq("code", ReferenceTable.REJECTION_CARDIAC)));
		}		
		else if(ReferenceTable.STAR_CARDIAC_CARE_PLATIANUM.equals(productKey)) {

			selectCriteria2 = selectCriteria2.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL),Restrictions.eq("code", ReferenceTable.REJECTION_CARDIAC_PLAT)));
		}
		else if(!ReferenceTable.getGMCProductList().containsKey(productKey)) {
			
			selectCriteria2 = selectCriteria2.add(Restrictions.or(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY),Restrictions.eq("code", ReferenceTable.REJECTION_RETAIL)));
		}
		else{
			
			selectCriteria2 = selectCriteria2.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY));
		}
		
		selectValueList2 = selectCriteria2.addOrder(Order.asc("key"))
					.setProjection(Projections.projectionList()
						.add(Projections.property("key"), "id")
						.add(Projections.property("value"), "value"))
						.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		selectValueContainer.addAll(selectValueList2);
		
		return selectValueContainer;
	}

	public BeanItemContainer<SelectValue> getReimbRejCategoryForCoronaFmlProd(Long productKey ,String policyPlan ) {

		List<SelectValue> selectValueList = new ArrayList<SelectValue>(); 
		Session session = (Session) entityManager.getDelegate();
		Criteria selectCriteria = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		
		if(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)) {
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_INDI));

		}
		else if(ReferenceTable.STAR_CORONA_GRP_PRODUCT_KEY_FOR_LUMPSUM.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)) {
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJECTION_CATEGORY_LUMPSUM));
		}
		else if(ReferenceTable.STAR_GRP_COVID_PROD_KEY_INDI.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_INDEMNITY)) {

			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJ_CATG_GRP_INDIMEN_93));
		}
		else if(ReferenceTable.STAR_GRP_COVID_PROD_KEY_LUMSUM.equals(productKey) && policyPlan.equalsIgnoreCase(SHAConstants.POLICY_COVID_GRP_PLAN_LUMPSUM)) {

			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REJ_CATG_GRP_LUMPSUM_93));
		}
		else{
			
			selectCriteria = selectCriteria.add(Restrictions.eq("code", ReferenceTable.REIMB_REJECTION_CATEGORY));
		}
		
				
		selectValueList = selectCriteria.addOrder(Order.asc("key"))
							.setProjection(Projections.projectionList()
								.add(Projections.property("key"), "id")
								.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValueList);
		
		Criteria selectCriteria2 = session.createCriteria(MastersValue.class).add(Restrictions.eq("activeStatus", 1));
		List<SelectValue> selectValueList2 = new ArrayList<SelectValue>(); 
		selectCriteria2 = selectCriteria2.add(Restrictions.eq("code", ReferenceTable.REIMB_REJECTION_CATEGORY));
		selectValueList2 = selectCriteria2.addOrder(Order.asc("key"))
					.setProjection(Projections.projectionList()
						.add(Projections.property("key"), "id")
						.add(Projections.property("value"), "value"))
						.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();
		
		selectValueContainer.addAll(selectValueList2);
		
		return selectValueContainer;
	}
	public BeanItemContainer<SelectValue> getConsulationDetails(Long  productKey) {
		
		BeanItemContainer<SelectValue> consulationdtlsListContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		
		Query findByProdKey = entityManager.createNamedQuery(
				"MasOPProductSection.findByProdKey").setParameter("productKey",
						productKey);
		
		   List<MasOPProductSection> prodSecList = (List<MasOPProductSection> )findByProdKey.getResultList();
		   if(prodSecList != null && !prodSecList.isEmpty()){
			   List<SelectValue> listConsulation = new ArrayList<SelectValue>();
			   for (MasOPProductSection prodSec : prodSecList) {
				   
				   Query findBySecKey = entityManager.createNamedQuery(
							"MasOpClaimSection.findByKey").setParameter("key",
									prodSec.getOpSecKey());
				   List<MasOpClaimSection> secList = (List<MasOpClaimSection> )findBySecKey.getResultList();
				   if(secList != null && !secList.isEmpty()){
					   for (MasOpClaimSection masOpClaimSection : secList) {

						   Boolean COMPREHENSIVE_78_PRODUCT_EXIST = false;
						   if(productKey.equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_FLOATER) || productKey.equals(ReferenceTable.COMPREHENSIVE_78_PRODUCT_INDIVIDUAL) ){
							   COMPREHENSIVE_78_PRODUCT_EXIST = true;
						   }
						SelectValue opCosultation = new SelectValue();
						opCosultation.setId(masOpClaimSection.getKey());
						opCosultation.setValue(masOpClaimSection.getOpDescription());
						opCosultation.setCommonValue(masOpClaimSection.getSectionCode());
						if(masOpClaimSection != null && masOpClaimSection.getKey() != null && masOpClaimSection.getKey().equals(ReferenceTable.OUT_PATIENT_VACCINATION_BENEFIT))
						{
							if(COMPREHENSIVE_78_PRODUCT_EXIST)
							listConsulation.add(opCosultation);
						}
						else{
							listConsulation.add(opCosultation);
						}
					}
				   }
			}
			   consulationdtlsListContainer.addAll(listConsulation);
			   
		   }
		   return consulationdtlsListContainer;
	}

	public BeanItemContainer<SelectValue> getTypeOfAdmissionTypes() {

		Query query = entityManager
				.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", ReferenceTable.ADM_HOME_CARE_TREATMENT_KAVACH);
		List<MastersValue> mastersValueList = query.getResultList();

		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> resultContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);

		for (MastersValue mastersValue : mastersValueList) {
			SelectValue select = new SelectValue();
			select.setId(mastersValue.getKey());
			select.setValue(mastersValue.getValue());
			selectValuesList.add(select);
			//resultContainer.addBean(select);  
		}
		resultContainer.addAll(selectValuesList);
		return resultContainer;
	}

	public TmpEmployee getUserLoginDetail(String loginId){

		Query findByTransactionKey = entityManager.createNamedQuery(
				"TmpEmployee.getEmpByExactLoginId").setParameter("loginId", loginId.toLowerCase());
		List<TmpEmployee> userLoginDetailsList = (List<TmpEmployee>) findByTransactionKey
				.getResultList();

		if(userLoginDetailsList != null && !userLoginDetailsList.isEmpty()){
			return userLoginDetailsList.get(0);
		}

		return null;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getHospitalCodeListByHRMCode(String hrmCode) {
		
		Query query = entityManager
				.createNamedQuery("MasHospitals.findByHRMCode");
		query = query.setParameter("hrmCode", hrmCode);
		//List<Hospitals> masHospitalsList = query.getResultList();

		@SuppressWarnings("unchecked")
		List<MasHospitals> resultHospitalList = (List<MasHospitals>) query.getResultList();

		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> cpuCodeContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if(resultHospitalList != null && !resultHospitalList.isEmpty()){
			SelectValue select = null;
			for (MasHospitals hasHospitals : resultHospitalList) {
				if(hasHospitals != null){

					select = new SelectValue();
					select.setId(hasHospitals.getKey());
					select.setValue(hasHospitals.getHospitalCode() + " - " + hasHospitals.getName());
					selectValuesList.add(select);
				}
			}
		}

		cpuCodeContainer.addAll(selectValuesList);

		cpuCodeContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return cpuCodeContainer;

	}

	public BeanItemContainer<SelectValue> getStopPaymentRequestCategoryTypes() {
		Query query = entityManager.createNamedQuery("MastersValue.findByMasterTypeCode");
		query = query.setParameter("masterTypeCode", "DD_STP_PAY_REINS");
		List<MastersValue> mastersValueList = query.getResultList();
		BeanItemContainer<SelectValue> resultContainer = getResultContainer(mastersValueList);		
		return resultContainer;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public MastersValue getMaster(String masterValue , String masterCode) {
		// Query findAll =
		// entityManager.createNamedQuery("CityTownVillage.findAll");
		MastersValue a_mastersValue = new MastersValue();
		if (masterValue != null && !masterValue.isEmpty() && masterCode != null && !masterCode.isEmpty()) {
			Query query = entityManager
					.createNamedQuery("MastersValue.findByValueAndCode");
			query.setParameter("masterValue", masterValue);
			query.setParameter("masterCode", masterCode);
			List<MastersValue> mastersValueList = query.getResultList();
			for (MastersValue mastersValue : mastersValueList){
				a_mastersValue = mastersValue;
				break;
			}
		}

		return a_mastersValue;
	}
	
	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SpecialSelectValue>  getProductNameCodeList() {

		Query findAll = entityManager.createNamedQuery("Product.findAll");
		@SuppressWarnings("unchecked")
		List<Product> resultCPUCodeList = (List<Product>) findAll.getResultList();
		List<SpecialSelectValue> selectValuesList = new ArrayList<SpecialSelectValue>();
		BeanItemContainer<SpecialSelectValue> productCodeNameContainer = new BeanItemContainer<SpecialSelectValue>(SpecialSelectValue.class);
		SpecialSelectValue select = null;
		for (Product proCodeName : resultCPUCodeList) {
			select = new SpecialSelectValue();
			select.setId(proCodeName.getKey());
			select.setValue(proCodeName.getValue().toString() + " / " + proCodeName.getCode().toString());
			selectValuesList.add(select);
		}
		productCodeNameContainer.addAll(selectValuesList);

		productCodeNameContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return productCodeNameContainer;
	}

	
	public GmcCoorporateBufferLimit getCBBypolicyKey(Long policyKey,Double suminsured){
		Query query = entityManager.createNamedQuery("GmcCoorporateBufferLimit.findBasedOnSI");
		query = query.setParameter("policyKey", policyKey);
		query = query.setParameter("suminsured", suminsured);
		List<GmcCoorporateBufferLimit> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
	
	public CoorporateBuffer getBufferbyinsuerdNo(Long insuredNumber){
		Query query = entityManager.createNamedQuery("CoorporateBuffer.findbyinsuredNumber");
		query = query.setParameter("insuredNumber", insuredNumber);
		List<CoorporateBuffer> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
	
	public CoorporateBuffer getBufferbyMainMember(Long mainMember){
		Query query = entityManager.createNamedQuery("CoorporateBuffer.findbyinsuredMainMemberWithoutBuffer");
		query = query.setParameter("mainMember", mainMember);
		List<CoorporateBuffer> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
	
	public CoorporateBuffer getBufferbyMainMemeberID(Long insuredNumber,String bufferType){
		Query query = entityManager.createNamedQuery("CoorporateBuffer.findbyinsuredMainMember");
		query = query.setParameter("mainMember", insuredNumber);
		query = query.setParameter("bufferType", bufferType);
		List<CoorporateBuffer> bufferLimits = query.getResultList();
		if(bufferLimits !=null && !bufferLimits.isEmpty()){
			return bufferLimits.get(0);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getSelectValueContainerForNEFT(Long masterKey) {
		Query query = entityManager.createNamedQuery("MastersValue.findByKey");
		query.setParameter("parentKey",masterKey);	
		List<MastersValue> resultList = query.getResultList();
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		if (resultList != null && !resultList.isEmpty()) {
			for (MastersValue masUserReference : resultList) {
				SelectValue value = new SelectValue();
				value.setValue(masUserReference.getValue());
				value.setId(masUserReference.getKey());
				selectValueContainer.addItem(value);
			}
			return selectValueContainer;
		}
		return null;


	}
	
	@SuppressWarnings("unchecked")
	public SelectValue getProcedureDescriptionByKey(Long key) {
		Query rodByKey = entityManager.createNamedQuery("ProcedureMaster.findByKey").setParameter("primarykey", key);
		ProcedureMaster procedureMasters = (ProcedureMaster) rodByKey.getSingleResult();
			SelectValue selectValue = null;
			if (procedureMasters != null ) {
				selectValue = new SelectValue();
				selectValue.setId(procedureMasters.getKey());
				String value = procedureMasters.getProcedureName();
				if(procedureMasters.getProcedureCode() != null ){
					value = value + " - "+ procedureMasters.getProcedureCode();
				}else{
					value = value;
				}
				selectValue.setValue(value);
				selectValue.setCommonValue(procedureMasters.getProcedureName());
				return selectValue;
			}
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public BeanItemContainer<SelectValue> getshortOrderMasterValueByShortNO(String a_key) {

		Session session = (Session) entityManager.getDelegate();
		
		List<SelectValue> selectValuesList = session.createCriteria(MastersValue.class)
		.add(Restrictions.eq("activeStatus", 1))
		.add(Restrictions.eq("code", a_key))
		.addOrder(Order.asc("sortNo"))
		.setProjection(Projections.projectionList()
				.add(Projections.property("key"), "id")
				.add(Projections.property("value"), "value"))
				.setResultTransformer(org.hibernate.transform.Transformers.aliasToBean(SelectValue.class)).list();	
		BeanItemContainer<SelectValue> selectValueContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		selectValueContainer.addAll(selectValuesList);
		System.out.println("---------------- Second Approach End current time " + new Date());


		return selectValueContainer;
	}
	
	public TmpEmployee getEmployeeByID(String empId) {

		Query query = entityManager.createNamedQuery("TmpEmployee.findByEmpId");
		query.setParameter("empId", empId.toLowerCase());
		List<TmpEmployee> resultList = (List<TmpEmployee>) query.getResultList();
		if(resultList != null && !resultList.isEmpty()) {
			entityManager.refresh(resultList.get(0));
			return resultList.get(0);
		}
		return null;
	}
	
	public BeanItemContainer<SelectValue> getPostClaimCVCErrorCategoryByMaster() {
		Query query = entityManager.createNamedQuery("MasPostClaimCVCCategory.findAll");
		query = query.setParameter("activeStatus", SHAConstants.YES_FLAG);
		List<MasPostClaimCVCCategory> mastersValueList = query.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> mastersValueContainer = new BeanItemContainer<SelectValue>(SelectValue.class);
		for (MasPostClaimCVCCategory value : mastersValueList) {
			SelectValue select = new SelectValue();
//			select.setId(value.getKey());
			select.setValue(value.getKey().toString());
			select.setCommonValue(value.getCategory());
			selectValuesList.add(select);
		}
		mastersValueContainer.addAll(selectValuesList);

		return mastersValueContainer;
	}
	
	public MasPostClaimCVCCategory getPostClaimCVCMasCategoryValue(Long categoryKey)
	{
		Query query = entityManager
				.createNamedQuery("MasPostClaimCVCCategory.findByKey");
		query.setParameter("key", categoryKey);

		List<MasPostClaimCVCCategory> postClaimcvcCategoryList = (List<MasPostClaimCVCCategory>) query.getResultList();

		if(postClaimcvcCategoryList !=null && !postClaimcvcCategoryList.isEmpty())
		{
			return postClaimcvcCategoryList.get(0);
		}

		return null;

	}

	public BeanItemContainer<SpecialSelectValue> getProductContainerForAudit() {
		Query findAll = entityManager.createNamedQuery("Product.findUnique");
		List<Object> productList = (List<Object>) findAll.getResultList();
		BeanItemContainer<SpecialSelectValue> selectValueContainer = new BeanItemContainer<SpecialSelectValue>(
				SpecialSelectValue.class);
		if (!productList.isEmpty()) {
			List<SpecialSelectValue> selectValueList = new ArrayList<SpecialSelectValue>();
			Long i=0l;
			SpecialSelectValue selectValue = null;
			for (Object product : productList) {

				selectValue = new SpecialSelectValue();

				if(product instanceof Object[]){
					Object[] ObjArray =(Object[]) product;
					Object objCode = ObjArray[0];
					Object objValue =ObjArray[1];
					Object objKey = ObjArray[2];
					if(objCode != null && objValue != null){
						selectValue.setId(Long.valueOf(objKey.toString()));
						selectValue.setValue(objValue.toString());
						selectValue.setSpecialValue(objCode.toString() + " - "+objValue.toString());
					}
					i++;
				}
				
				selectValueList.add(selectValue);
			}
			selectValueContainer.addAll(selectValueList);
		}

		return selectValueContainer;
	}

	@SuppressWarnings({ "unchecked"})
	public BeanItemContainer<SelectValue>  getProductNameContainerForAudit() {

		Query findAll = entityManager.createNamedQuery("Product.findAll");
		@SuppressWarnings("unchecked")
		List<Product> resultCPUCodeList =(List<Product>)findAll.getResultList();
		List<SelectValue> selectValuesList = new ArrayList<SelectValue>();
		BeanItemContainer<SelectValue> productContainer = new BeanItemContainer<SelectValue>(
				SelectValue.class);
		SelectValue select = null;
		for (Product product : resultCPUCodeList) {
			select = new SelectValue();
			select.setId(product.getKey());
			select.setValue(product.getCode()+ " : " + product.getValue());
			selectValuesList.add(select);
		}
		productContainer.addAll(selectValuesList);

		productContainer.sort(new Object[] {"value"}, new boolean[] {true});

		return productContainer;

	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Product> getProductListByProductCode(String productCode) {
		Product product = new Product();
		List<Product> productList =null;
		if (productCode != null) {
			Query findAll = entityManager
					.createNamedQuery("Product.findByCode").setParameter(
							"productCode", productCode);
			productList = findAll.getResultList();
			/*for (Product mastersValue : productList) {
				product = mastersValue;
			}*/
		}
		return productList;
	}
	
	public MasInvZone getMasInvZoneKey(String key)
	{
		Query  query = entityManager.createNamedQuery("MasInvZone.findByKey");
		query = query.setParameter("key", Long.parseLong(key));
		
		List<MasInvZone> listOfTmpCodes = query.getResultList();
		if(null != listOfTmpCodes && !listOfTmpCodes.isEmpty())
		{
			return listOfTmpCodes.get(0);
		}
		return null;
	}
}
