package com.shaic.claim.pedquery;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.PedRaisedDetailsDto;
import com.shaic.claim.adviseonped.search.SearchAdviseOnPEDTableDTO;
import com.shaic.claim.pedquery.search.SearchPEDQueryTableDTO;
import com.shaic.claim.pedrequest.approve.PEDRequestDetailsApproveDTO;
import com.shaic.claim.pedrequest.approve.bancspedQuery.BancsSearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDQueryDetailTableDTO;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDQueryMapper;
import com.shaic.claim.pedrequest.approve.bancspedrequest.BancsPEDRequestDetailsApproveDTO;
import com.shaic.claim.pedrequest.approve.search.SearchPEDRequestApproveTableDTO;
import com.shaic.claim.pedrequest.process.PEDRequestDetailsProcessDTO;
import com.shaic.claim.pedrequest.process.search.SearchPEDRequestProcessTableDTO;
import com.shaic.claim.pedrequest.view.ViewPEDTableDTO;
import com.shaic.claim.preauth.wizard.dto.NewInitiatePedEndorsementDTO;
import com.shaic.claim.preauth.wizard.dto.OldPedEndorsementDTO;
import com.shaic.claim.viewPedEndorsement.PedEndorsementMapper;
import com.shaic.cmn.login.ImsUser;
import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasUser;
import com.shaic.domain.MastersValue;
import com.shaic.domain.PedQueryDetailsTableData;
import com.shaic.domain.PedQueryTable;
import com.shaic.domain.Policy;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.Status;
import com.shaic.domain.preauth.NewInitiatePedEndorsement;
import com.shaic.domain.preauth.OldInitiatePedEndorsement;
import com.shaic.domain.preauth.PEDEndormentTrails;
import com.shaic.domain.preauth.PedEndorementHistory;
import com.shaic.domain.preauth.PedEndorsementDetailsHistory;
import com.shaic.domain.preauth.PedQuery;
import com.shaic.domain.preauth.PedSpecialist;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.shaic.newcode.wizard.domain.PEDQueryMapper;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.themes.ValoTheme;
















//import com.shaic.arch.table.Pageable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.sql.SQLException;

@Stateless
public class PEDQueryService extends AbstractDAO<OldInitiatePedEndorsement> {

	@PersistenceContext
	protected EntityManager entityManager;


	public PEDQueryService() {

	}
	
	public enum Outcome {IPEDIPSA}

	@SuppressWarnings("unchecked")
	public Page<OldPedEndorsementDTO> search(Long key) {
		System.out.println("Called one----------------------------------->");
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		//SearchPEDQueryTableDTO formDto = new SearchPEDQueryTableDTO();
		try {
			final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
					.createQuery(OldInitiatePedEndorsement.class);

			Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
					.from(OldInitiatePedEndorsement.class);

			Predicate predicates = builder.equal(searchRoot.get("key"), key);

			criteriaQuery.select(searchRoot).where(builder.and(predicates));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
					.createQuery(criteriaQuery);

			OldInitiatePedEndorsement result = oldInitiatePedQuery
					.getSingleResult();

			Long claimKey = result.getClaim().getKey();
			
//			Query findAllDetails = entityManager
//					.createNamedQuery("Preauth.findByClaimKey").setParameter("claimkey", claimKey);
//			
//			List<Preauth> preauthList = (List<Preauth>) findAllDetails
//					.getResultList();
			
			Predicate preauthQuery = null;
			
//			for (Preauth preauthKey : preauthList) {
//				
				preauthQuery = builder.equal(
						searchRoot.<Preauth> get("claim").<Long> get("key"),
						claimKey);
//			}

			criteriaQuery.select(searchRoot).where(builder.and(preauthQuery));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQueryList = entityManager
					.createQuery(criteriaQuery);

			List<OldInitiatePedEndorsement> resultList = oldInitiatePedQueryList
					.getResultList();

			final List<OldPedEndorsementDTO> resultDto = new ArrayList<OldPedEndorsementDTO>();

			for (int i = 0; i < resultList.size(); i++) {
				OldPedEndorsementDTO details = new OldPedEndorsementDTO();

				if (resultList.get(i).getPedName() != null) {
					details.setPedName(resultList.get(i)
							.getPedName());
				}

				details.setKey(resultList.get(i).getKey());
				details.setPedSuggestionName(resultList.get(i)
						.getPedSuggestion().getValue());
				details.setRepudiationLetterDate(resultList.get(i)
						.getRepudiationLetterDate());
				details.setRemarks(resultList.get(i).getRemarks());
				details.setRequestorId(resultList.get(i).getCreatedBy());
				details.setRequestedDate(SHAUtils.formatDate(resultList.get(i).getCreatedDate()));
				details.setRequestStatus(resultList.get(i).getStatus()
						.getProcessValue());
				
				
                details.setCurrentPED(key);
                
				resultDto.add(details);
			}

			// List<OldPedEndorsementDTO>
			// resultDto=PEDQueryMapper.getPEDQueryTableDTO(resultList);

			for (int i = 0; i < resultDto.size(); i++) {
				//final Long keyList = resultDto.get(i).getKey();

				//final int j = i;

				// Button button = new Button("view Details");
				// button.addClickListener(new Button.ClickListener() {
				// /**
				// *
				// */
				// private static final long serialVersionUID = 1L;
				//
				// @Override
				// public void buttonClick(ClickEvent event) {
				//
				// }
				// });
				// button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				// button.addStyleName(ValoTheme.BUTTON_LINK);
				// resultDto.get(i).setViewDetails(button);
				OptionGroup radio = new OptionGroup();
				radio.addItems("");
				radio.addValueChangeListener(new ValueChangeListener() {

					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(
							com.vaadin.v7.data.Property.ValueChangeEvent event) {

					}
				});
				resultDto.get(i).setSelect(radio);

			}

			Page<OldPedEndorsementDTO> page = new Page<OldPedEndorsementDTO>();
			page.setPageNumber(1);
			page.setPageItems(resultDto);
			// page.setPagesAvailable(pagedList.getPagesAvailable());
			if (page != null) {

				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public Page<BancsPEDQueryDetailTableDTO> searchQuery(Long key) {
		System.out.println("Called one----------------------------------->");
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		try {
			final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
					.createQuery(OldInitiatePedEndorsement.class);

			Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
					.from(OldInitiatePedEndorsement.class);

			Predicate predicates = builder.equal(searchRoot.get("key"), key);

			criteriaQuery.select(searchRoot).where(builder.and(predicates));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
					.createQuery(criteriaQuery);

			OldInitiatePedEndorsement result = oldInitiatePedQuery
					.getSingleResult();

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQueryList = entityManager
					.createQuery(criteriaQuery);

			List<OldInitiatePedEndorsement> resultList = oldInitiatePedQueryList
					.getResultList();
			List<PedQueryDetailsTableData> pedQueryRequestDetails = null;
			List<BancsPEDQueryDetailTableDTO> pedQueryRequestDetailsDtoList = new ArrayList<BancsPEDQueryDetailTableDTO>();

			if(resultList.size() > 0){
				PedQueryTable pedQueryTable = getworkItemID(Long.valueOf(resultList.get(0).getWorkItemID()));
				if(pedQueryTable != null){
					pedQueryRequestDetails = getPedQueryRequestDetails(pedQueryTable.getKey());
					if(pedQueryRequestDetails.size() > 0){
						for(PedQueryDetailsTableData pedQueryDetailsTableData :pedQueryRequestDetails){
							BancsPEDQueryDetailTableDTO  bancsPEDQueryDetailTableDTO= new BancsPEDQueryDetailTableDTO();
							bancsPEDQueryDetailTableDTO.setKey(pedQueryDetailsTableData.getKey());
							bancsPEDQueryDetailTableDTO.setQueryType(pedQueryDetailsTableData.getQueryType());
							bancsPEDQueryDetailTableDTO.setQueryId(pedQueryDetailsTableData.getQueryId());
							bancsPEDQueryDetailTableDTO.setQueryCode(pedQueryDetailsTableData.getQueryCode());
							bancsPEDQueryDetailTableDTO.setQueryDesc(pedQueryDetailsTableData.getQueryDesc());
							bancsPEDQueryDetailTableDTO.setQueryRemarks(pedQueryDetailsTableData.getQueryRemarks());
							bancsPEDQueryDetailTableDTO.setRaisedByRole(pedQueryDetailsTableData.getRaisedByRole());
							bancsPEDQueryDetailTableDTO.setRaisedByUser(pedQueryDetailsTableData.getRaisedByUser());
							bancsPEDQueryDetailTableDTO.setRaisedDate(pedQueryDetailsTableData.getRaisedDate());
							bancsPEDQueryDetailTableDTO.setPedQuery(pedQueryDetailsTableData.getPedQuery());
							pedQueryRequestDetailsDtoList.add(bancsPEDQueryDetailTableDTO);
						}
					}
				}
			}

			Page<BancsPEDQueryDetailTableDTO> page = new Page<BancsPEDQueryDetailTableDTO>();
			page.setPageNumber(1);
			page.setPageItems(pedQueryRequestDetailsDtoList);
			if (page != null) {

				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public List<PEDEndormentTrails> getPedEndorsementTrails(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"PEDEndormentTrails.findByPedKey").setParameter("pedKey", pedKey);
		List<PEDEndormentTrails> initiate = (List<PEDEndormentTrails>) findByKey.getResultList();
		
		return initiate;
		
	}
	
	public List<OldInitiatePedEndorsement> getPedInitiateDetail(Long claimKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByClaim").setParameter("claimKey", claimKey);
		List<OldInitiatePedEndorsement> initiate = (List<OldInitiatePedEndorsement>) findByKey.getResultList();
		
		return initiate;
		
	}
	
	public PedEndorementHistory getPedEndorementHistory(Long pedInitiateKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"PedEndorementHistory.findByKey").setParameter("primaryKey", pedInitiateKey);
		List<PedEndorementHistory> initiate = (List<PedEndorementHistory>) findByKey.getResultList();
		if(initiate != null && ! initiate.isEmpty()){
			return initiate.get(0);
		}
		
		return null;
		
		
	}
	
	public List<PedEndorementHistory> getPedEndorsementHistory(Long pedInitiateKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"PedEndorementHistory.findByPedInitiate").setParameter("pedInitiateKey", pedInitiateKey);
		List<PedEndorementHistory> initiate = (List<PedEndorementHistory>) findByKey.getResultList();
		if(initiate != null && ! initiate.isEmpty()){
			return initiate;
		}
		
		return null;
		
		
	}
	
	public Boolean isPEDInitiated(Long policyKey) {/*
		try {
			Query findAllDetails = entityManager
					.createNamedQuery("OldInitiatePedEndorsement.findByPolicy").setParameter("policyKey", policyKey);
			@SuppressWarnings("unchecked")
			List<OldInitiatePedEndorsement> pedInitiateList = (List<OldInitiatePedEndorsement>) findAllDetails
					.getResultList();
			
			if(pedInitiateList != null && !pedInitiateList.isEmpty()) {
				return true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	*/
		Connection connection = null;
		CallableStatement cs = null;
		Integer output = 0;
		try {
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CHK_PED_EXIST(?, ?, ?)}");
			cs.setLong(1, policyKey);
			cs.setLong(2, 0L);
			cs.registerOutParameter(3, Types.INTEGER, "LN_PED_EXISTS");

			cs.execute();
			if (cs.getObject(3) != null) {
				output = Integer.parseInt(cs.getObject(3).toString());
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return (output.equals(1) ? Boolean.TRUE : Boolean.FALSE);
		
	
	}

	@SuppressWarnings("unused")
	public Page<OldPedEndorsementDTO> pedInitiateDetails(Long claimKey) {

		Query findAllDetails = entityManager
				.createNamedQuery("OldInitiatePedEndorsement.findByClaim").setParameter("claimKey", claimKey);
		@SuppressWarnings("unchecked")
		List<OldInitiatePedEndorsement> pedInitiateList = (List<OldInitiatePedEndorsement>) findAllDetails
				.getResultList();

		List<OldPedEndorsementDTO> resultDto = new ArrayList<OldPedEndorsementDTO>();

		for (int i = 0; i < pedInitiateList.size(); i++) {
			OldPedEndorsementDTO details = new OldPedEndorsementDTO();

			if (pedInitiateList.get(i).getPedName() != null) {
				details.setPedName(pedInitiateList.get(i)
						.getPedName());
			}

			details.setKey(pedInitiateList.get(i).getKey());
			if(pedInitiateList.get(i).getPedSuggestion()!=null){
			details.setPedSuggestionName(pedInitiateList.get(i)
					.getPedSuggestion().getValue());
			}
			details.setRepudiationLetterDate(pedInitiateList.get(i)
					.getRepudiationLetterDate());
			details.setRemarks(pedInitiateList.get(i).getRemarks());
			details.setRequestorId(pedInitiateList.get(i).getCreatedBy());
			if(pedInitiateList.get(i).getCreatedDate() != null){
			Date formatDateTime = SHAUtils.formatTime(pedInitiateList.get(i).getCreatedDate().toString());
			
			details.setRequestedDate(SHAUtils.formatDate(formatDateTime));
			}
			
			details.setRequestStatus(pedInitiateList.get(i).getStatus()
					.getProcessValue());
			
			details.setStatusKey(pedInitiateList.get(i).getStatus().getKey());
			
			if(details.getRequestorId() != null){
				details.setIsReviewer(isUserPedReviewer(details.getRequestorId()));
			}
			
			details.setIntimationNo(pedInitiateList.get(i).getIntimation().getIntimationId());

			resultDto.add(details);
		}

		Page<OldPedEndorsementDTO> page = new Page<OldPedEndorsementDTO>();
		page.setPageNumber(1);
		page.setPageItems(resultDto);

		/*if (page != null) {
			return page;
		}*/

		return page;

	}
	
	/**
	 * R1086 Auto Deletion of Insured 
	 * @param claimKey
	 * @return
	 */
	public boolean getStatusOfInsuredForNonDisclosePed(Long insuredKey) {
	
		if(insuredKey != null){
			try{
				Query findPEDByClmKey = entityManager.createNamedQuery(
						"OldInitiatePedEndorsement.findByInsuredAndpedSuggestion");
				findPEDByClmKey.setParameter("insuredKey", insuredKey);
				findPEDByClmKey.setParameter("pedSuggestionKey", ReferenceTable.PED_SUGGESTION_SUG006);
				List<OldInitiatePedEndorsement> initiatePEDForNonDisclosePedList = (List<OldInitiatePedEndorsement>) findPEDByClmKey.getResultList();
				
				if(initiatePEDForNonDisclosePedList != null && 
						!initiatePEDForNonDisclosePedList.isEmpty() && 
						(initiatePEDForNonDisclosePedList.get(0).getStatus().getKey().intValue() != 44 &&
								initiatePEDForNonDisclosePedList.get(0).getStatus().getKey().intValue() != 254)){
					return true;			
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Returns True if Multiple PED Available excluding Auto Deleted Suggestion Approved case based on Policy Key
	 * otherwise returns false
	 * CR R1156 
	 * @return
	 */
	public boolean getMultiplePEDAvailableNotDeleted(Long policyKey, Long insuredKey){
		
		if(policyKey != null && insuredKey != null){
			try{
				Query findPEDByClmKey = entityManager.createNamedQuery(
						"OldInitiatePedEndorsement.findByPolicy");
				findPEDByClmKey.setParameter("policyKey", policyKey);
				List<OldInitiatePedEndorsement> initiatePEDForNonDisclosePedList = (List<OldInitiatePedEndorsement>) findPEDByClmKey.getResultList();
				
				if(initiatePEDForNonDisclosePedList != null && 
						!initiatePEDForNonDisclosePedList.isEmpty()){
					int pedcount = 0;
					for (OldInitiatePedEndorsement oldInitiatePedEndorsement : initiatePEDForNonDisclosePedList) {
						if(oldInitiatePedEndorsement.getIntimation().getInsured().getKey().equals(insuredKey)
								&& ReferenceTable.PED_SUGGESTION_SUG006.equals(oldInitiatePedEndorsement.getPedSuggestion().getKey())
								&& oldInitiatePedEndorsement.getStatus().getKey().equals(ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA))
							break;
						pedcount++;
					}
					if(initiatePEDForNonDisclosePedList.size() == 1 && pedcount == 0){
						return false;
					}
					if(pedcount <= initiatePEDForNonDisclosePedList.size() /*&& initiatePEDForNonDisclosePedList.size() > 1*/)
						return true;			
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public OldPedEndorsementDTO searchEditableTableList(List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList) {

		// need to be implement
		// primaryKey=18l;

		OldPedEndorsementDTO pedEndorsementDto = new OldPedEndorsementDTO();

		//List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList = getInitiateDetailsPed(primaryKey);

		pedEndorsementDto
				.setNewInitiatePedEndorsementDto(newInitiatePedDtoList);

		return pedEndorsementDto;
	}

	public List<NewInitiatePedEndorsementDTO> getInitiateDetailsPed(
			Long primaryKey) {
		//final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		OldInitiatePedEndorsement oldInitiatePedDetails = getOldInitiatePedEndorsementDetails(primaryKey);

		Long key = oldInitiatePedDetails.getKey();
		
		List<NewInitiatePedEndorsement> resultList = new ArrayList<NewInitiatePedEndorsement>();
		
		resultList = getIntitiatePedEndorsementDetailsForAdviseOnPED(key);

//		final CriteriaQuery<NewInitiatePedEndorsement> criteriaQuery = builder
//				.createQuery(NewInitiatePedEndorsement.class);
//
//		Root<NewInitiatePedEndorsement> searchRoot = criteriaQuery
//				.from(NewInitiatePedEndorsement.class);
//		
//		List<Predicate> listOfPredicates = new ArrayList<Predicate>();
//
//		Predicate predicates = builder.equal(searchRoot
//				.<OldInitiatePedEndorsement> get("oldInitiatePedEndorsement")
//				.<Long> get("key"), key);
//		listOfPredicates.add(predicates);
//		
//		
//		Predicate predicates1 = builder.notEqual(searchRoot.<Long> get("activeStatus"), 0l);
//		listOfPredicates.add(predicates1);
//		
//		criteriaQuery.select(searchRoot).where(builder.and(listOfPredicates.toArray(new Predicate[] {})));
//		
//		final TypedQuery<NewInitiatePedEndorsement> newInitiatePedQuery = entityManager
//				.createQuery(criteriaQuery);
//		List<NewInitiatePedEndorsement> resultList = newInitiatePedQuery
//				.getResultList();
		
		if(resultList != null){
			List<NewInitiatePedEndorsementDTO> newInitiatePedDtoList = PEDQueryMapper.getInstance()
					.getinitiatePed(resultList);
			for (NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO : newInitiatePedDtoList) {
				if(newInitiatePedEndorsementDTO.getDoctorRemarks() == null){
					newInitiatePedEndorsementDTO.setDoctorRemarks("");
				}
			}
			return newInitiatePedDtoList;
		}else{
			return new ArrayList<NewInitiatePedEndorsementDTO>();
		}
		
		
	}
	
public List<NewInitiatePedEndorsement> getIntitiatePedEndorsementDetailsForAdviseOnPED(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"NewInitiatePedEndorsement.findByInitateKey").setParameter(
				"initiateKey", pedKey);

		List<NewInitiatePedEndorsement> newInitiateDetails = (List<NewInitiatePedEndorsement>) findByKey
				.getResultList();
		
		if(newInitiateDetails != null && ! newInitiateDetails.isEmpty()){
			return newInitiateDetails;
		}
		
		return null;
	}

	public OldPedEndorsementDTO getPedDetailsByKey(Long key) {
		OldInitiatePedEndorsement resultList = getOldInitiatePedEndorsementDetails(key);
		OldPedEndorsementDTO resultDto = PEDQueryMapper.getInstance()
				.getPedDetailsDto(resultList);
			
		if (resultDto != null) {
			return resultDto;
		}

		return null;

	}

	public PEDRequestDetailsApproveDTO getPedApproveByKey(Long key) {

		OldInitiatePedEndorsement resultList = getOldInitiatePedEndorsementDetails(key);
		PEDRequestDetailsApproveDTO resultDto = PEDQueryMapper.getInstance()
				.getPedApproveDto(resultList);
		
		
		if (resultDto != null) {
			return resultDto;
		}

		return null;

	}
	
	public BancsPEDRequestDetailsApproveDTO getPedQueryApproveByKey(Long key) {

		OldInitiatePedEndorsement resultList = getOldInitiatePedEndorsementDetails(key);
		BancsPEDRequestDetailsApproveDTO resultDto = BancsPEDQueryMapper.getInstance()
				.getPedApproveDto(resultList);
		
		
		if (resultDto != null) {
			return resultDto;
		}

		return null;

	}

	public OldInitiatePedEndorsement getOldInitiatePedEndorsementDetails(
			Long key
	/* SearchPEDQueryTableDTO formDTO */) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);

		Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);

		Predicate predicates = builder.equal(searchRoot.get("key"), key);

		criteriaQuery.select(searchRoot).where(builder.and(predicates));

		final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		OldInitiatePedEndorsement resultList = oldInitiatePedQuery
				.getSingleResult();
		return resultList;
	}

	public OldPedEndorsementDTO getPedQueryRemarks(Long key) {

		PedQuery results = getPedQueryDetails(key);
		OldPedEndorsementDTO resultDto = PEDQueryMapper.getInstance()
				.getpedQueryRemarks(results);
		if (resultDto != null) {
			return resultDto;
		}
		return null;
	}

	@SuppressWarnings("unused")
	public OldPedEndorsementDTO getSpecialistRemarks(Long key) {

		PedSpecialist results = getSpecialistDetailsByKey(key);
		OldPedEndorsementDTO resultDto = new OldPedEndorsementDTO();
		if(results!=null){
		if(null != results.getReferringReason()){
		resultDto.setReasonforReferring(results.getReferringReason());
		}
		resultDto.setKey(results.getKey());
		}
		/*if (resultDto != null) {
			return resultDto;
		}*/
		return resultDto;
	}

	@SuppressWarnings("unused")
	public Boolean updatedRejectionRemarks(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDTO) {
		Boolean rejectionValue = Boolean.FALSE;
		OldInitiatePedEndorsement intiateDetails = getOldInitiatePedEndorsementDetails(bean
				.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(intiateDetails);
		}
		
		Long version = 0l;
		
		PedEndorsementMapper endormentMapper =  PedEndorsementMapper.getInstance();
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		Status status = new Status();
		status.setKey(ReferenceTable.PED_REJECT);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		intiateDetails.setStage(stage);
		intiateDetails.setStatus(status);

		if (intiateDetails != null) {

			intiateDetails.setRejectionRemarks(bean.getRejectionRemarks());
			intiateDetails.setModifiedBy(strUserName);
			intiateDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			intiateDetails.setProcessType("A");
			if(bean.getIsEditPED())
			{
				intiateDetails = updatePEDInitiateForApprover(intiateDetails, bean);
//				if(intiateDetails.getVersion() != null){
//					version = intiateDetails.getVersion();
//				    version += 1;
//				    intiateDetails.setVersion(version);
//				}
				
			}
			
			entityManager.merge(intiateDetails);
			entityManager.flush();
			//entityManager.refresh(intiateDetails);
			
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(intiateDetails.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(intiateDetails);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						entityManager.flush();
						entityManager.clear();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							entityManager.flush();
							entityManager.clear();
						}
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,intiateDetails,stage,status,version);
			}
			
			//setBPMOutComeForPedApprove(bean, searchDTO, "REJECT");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_REJECT_PED_OUTCOME);
			setDBOutcomeForPEDApprover(searchDTO , bean, "");
			rejectionValue = Boolean.TRUE;
			//return true;
		}
		//return false;
		return rejectionValue;
	}
	
	@SuppressWarnings("unused")
	public Boolean updatedWatchListApproverDetails(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDTO) {
		OldInitiatePedEndorsement intiateDetails = getOldInitiatePedEndorsementDetails(bean
				.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(intiateDetails);
		}
		
		Long version = 0l;
		
		PedEndorsementMapper endormentMapper =  PedEndorsementMapper.getInstance();
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		Status status = new Status();
		status.setKey(ReferenceTable.PED_WATCHLIST_APPROVER);


		if (intiateDetails != null) {

			intiateDetails.setWatchListRemarks(bean.getWatchlistRemarks());
			intiateDetails.setWatchListFlag("Y");
			intiateDetails.setStatus(status);
			intiateDetails.setWatchListDate(new Timestamp(System.currentTimeMillis()));
			intiateDetails.setModifiedBy(strUserName);
			intiateDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			intiateDetails.setProcessType("A");
			if(bean.getIsEditPED())
			{
				intiateDetails = updatePEDInitiateForApprover(intiateDetails, bean);
//				if(intiateDetails.getVersion() != null){
//					version = intiateDetails.getVersion();
//				    version += 1;
//				    intiateDetails.setVersion(version);
//				}
				
			}
			
			entityManager.merge(intiateDetails);
			entityManager.flush();
			//entityManager.refresh(intiateDetails);
			
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(intiateDetails.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(intiateDetails.getStage());
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(intiateDetails);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						entityManager.flush();
						entityManager.clear();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							entityManager.flush();
							entityManager.clear();
						}
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,intiateDetails,intiateDetails.getStage(),intiateDetails.getStatus(),version);
			}
			
			//setBPMOutComeForPedApprove(bean, searchDTO, "WATCHLIST");
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_ADD_TO_WATCH_LIST_OUTCOME);

			setDBOutcomeForPEDApprover(searchDTO, bean , "");

			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	public Boolean updatedEscalateRemarks(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDTO) {
		Boolean approvedValue = Boolean.FALSE;
		OldInitiatePedEndorsement intiateDetails = getOldInitiatePedEndorsementDetails(bean
				.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(intiateDetails);
		}
		
		Long version = 0l;
		
		PedEndorsementMapper endormentMapper =  PedEndorsementMapper.getInstance();
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

//		Status status = new Status();
//		status.setKey(ReferenceTable.PED_ESCALATED);
		
		Status status = getStatusByKey(ReferenceTable.PED_ESCALATED);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		intiateDetails.setStage(stage);
		intiateDetails.setStatus(status);
   
		if (intiateDetails != null) {

			intiateDetails.setEscalateRemarks(bean.getEscalateRemarks());
			intiateDetails.setModifiedBy(strUserName);
			intiateDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			intiateDetails.setProcessType("E");
			if(bean.getIsEditPED())
			{
				intiateDetails = updatePEDInitiateForApprover(intiateDetails, bean);
//				version = intiateDetails.getVersion();
////				version += 1;
//				intiateDetails.setVersion(version);
			}
			
			entityManager.merge(intiateDetails);
			entityManager.flush();
			//entityManager.refresh(intiateDetails);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(intiateDetails.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(intiateDetails);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
						}
						entityManager.flush();
						entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,intiateDetails,stage,status,version);
			}

			//setBPMOutComeForPedApprove(bean, searchDTO, "APPROVE");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS, status.getProcessValue()); 
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_SEND_TO_TL_OUTCOME);
			setDBOutcomeForPEDApprover(searchDTO , bean, "");
			//return true;
			approvedValue = Boolean.TRUE;

		}
		//return false;
		return approvedValue;
	}

	@SuppressWarnings("unused")
	public Boolean updatedApproveRemarks(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDTO) {
		Boolean approvedValue = Boolean.FALSE;
		OldInitiatePedEndorsement intiateDetails = getOldInitiatePedEndorsementDetails(bean
				.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(intiateDetails);
		}
		
		Long version = 0l;
		
		PedEndorsementMapper endormentMapper =  PedEndorsementMapper.getInstance();
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		Status status = new Status();
		status.setKey(ReferenceTable.PED_APPROVED);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		intiateDetails.setStage(stage);
		intiateDetails.setStatus(status);
   
		if (intiateDetails != null) {

			intiateDetails.setApprovalRemarks(bean.getApprovalRemarks());
			intiateDetails.setModifiedBy(strUserName);
			intiateDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			intiateDetails.setProcessType("A");
			if(bean.getIsEditPED())
			{
				intiateDetails = updatePEDInitiateForApprover(intiateDetails, bean);
//				version = intiateDetails.getVersion();
////				version += 1;
//				intiateDetails.setVersion(version);
			}
			
			entityManager.merge(intiateDetails);
			entityManager.flush();
			//entityManager.refresh(intiateDetails);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(intiateDetails.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(intiateDetails);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
						}
						entityManager.flush();
						entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,intiateDetails,stage,status,version);
			}

			//setBPMOutComeForPedApprove(bean, searchDTO, "APPROVE");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_SEND_TO_APPROVER_OUTCOME);
			setDBOutcomeForPEDApprover(searchDTO , bean, "");
			//return true;
			approvedValue = Boolean.TRUE;

		}
		//return false;
		return approvedValue;
	}

	@SuppressWarnings("unused")
	public Boolean updateQueryRemarksApprover(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDTO) {
		
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		
		Long version = 0l;

//		PedQuery pedQueryDetails = getPedQueryDetails(bean.getKey());
		PedQuery pedQueryDetails = null;
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);


		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		Status status = new Status();
		status.setKey(ReferenceTable.PED_QUERY);

		Query findByInitiateKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter(
				"primaryKey", bean.getKey());
		OldInitiatePedEndorsement pedInitiate = (OldInitiatePedEndorsement) findByInitiateKey
				.getSingleResult();
		
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(pedInitiate);
		}
		
		if (pedInitiate != null) {
			pedInitiate.setStage(stage);
			pedInitiate.setStatus(status);
			pedInitiate.setModifiedBy(strUserName);
			pedInitiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			pedInitiate.setProcessType("A");
			if(bean.getIsEditPED()){
				pedInitiate = updatePEDInitiateForApprover(pedInitiate, bean);
//				if(pedInitiate.getVersion() != null){
//					version = pedInitiate.getVersion();
//					version += 1;
//					pedInitiate.setVersion(version);
//				}
			}
			
			
			entityManager.merge(pedInitiate);
			entityManager.flush();
			//entityManager.refresh(pedInitiate);
			
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){

				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(pedInitiate.getKey());
			}
			
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(pedInitiate);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
						}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,pedInitiate,stage,status,version);
			}
		}
		
		
		
		
		System.out.println("status is reading ---->"+pedInitiate.getStatus().getProcessValue());

		/*if (pedQueryDetails != null) {
			// if (pedQueryDetails.getInitiatorType().equals("A")) {
			pedQueryDetails.setInitiatorType("A");
			pedQueryDetails.setQueryRemarks(bean.getQueryRemarks());
			pedQueryDetails.setModifiedBy(strUserName);
			pedQueryDetails.setStage(stage);
			pedQueryDetails.setStatus(status);
			pedQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(pedQueryDetails);
			entityManager.flush();
//			entityManager.refresh(pedQueryDetails);	
			//entityManager.refresh(pedQueryDetails);
			//setBPMOutComeForPedApprove(bean, searchDTO, "QUERY");
			setDBOutcomeForPEDApprover(searchDTO, bean,SHAConstants.PED_REQUEST_APPROVER_PED_QUERY_OUTCOME);
			return true;
			// }
		}*/
		//else{
			pedQueryDetails=new PedQuery();
			pedQueryDetails.setQueryRemarks(bean.getQueryRemarks());
			if(pedInitiate !=null){
			pedQueryDetails.setNewInitiatePedEndorsement(pedInitiate);
			}
			pedQueryDetails.setActiveStatus(1l);
			pedQueryDetails.setInitiatorType("A");
			pedQueryDetails.setQueryRemarks(bean.getQueryRemarks());
			pedQueryDetails.setStage(stage);
			pedQueryDetails.setStatus(status);
			pedQueryDetails.setCreatedBy(strUserName);
			entityManager.persist(pedQueryDetails);
			entityManager.flush();
			entityManager.clear();
			//setBPMOutComeForPedApprove(bean, searchDTO, "QUERY");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_PED_QUERY_OUTCOME);
			setDBOutcomeForPEDApprover(searchDTO, bean,"");
			return true;
		//}

//		return false;
	}

	/*public void setBPMOutComeForPedApprove(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO humanTask, String outCome) {

			if (humanTask != null) {
				PayloadBOType payload = humanTask.getHumanTask().getPayloadCashless();
				
				if(bean.getIsEditPED()){
					PedReqType pedReq = payload.getPedReq();
					if(pedReq == null){
						pedReq = new PedReqType();
					}
					if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
						pedReq.setStatus(bean.getPedSuggestion().getId().toString());
						payload.setPedReq(pedReq);
					}
					
				}
				
				payload.getPedReq().setResult(outCome);
				payload.getPedReq().setOwnerGroup(outCome);
//				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//				Map<String, String> pedReq = new HashMap<String, String>();
//
//				try {
//
//					if (humanTask.getKey() != null) {
//
//						pedReq.put("key", humanTask.getKey().toString());
//					}
//					//
//					if (humanTask.getIntimationNo() != null) {
//						regIntDetailsReq.put("IntimationNumber",
//								humanTask.getIntimationNo());
//					}
//
//					payload = BPMClientContext.setNodeValue(payload,
//							"RegIntDetails", regIntDetailsReq);
//					payload = BPMClientContext.setNodeValue(payload, "PedReq",
//							pedReq);
//
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				humanTask.getHumanTask().setOutcome(outCome);

//				PedReqType pedReqDetails = new PedReq();
//				pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));
//
//				humanTask.getHumanTask().setPayload(payload);

//				try {
//					System.out
//							.println("----------------------- value from bpm task");
////					BPMClientContext.printPayloadElement(humanTask
////							.getHumanTask().getPayload());
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				SubmitApproverPedReqTask submitPedTask = BPMClientContext.getSubmitPedTask(humanTask.getUsername(),humanTask.getPassword());
				try{
				submitPedTask.execute(humanTask.getUsername(), humanTask.getHumanTask());
				}catch(Exception e){
					e.printStackTrace();
				}

				System.out.println("BPM Executed Successfully");
			}
	}*/

	@SuppressWarnings("unchecked")
	public Boolean sumbitPedQuery(OldPedEndorsementDTO bean,
			SearchPEDQueryTableDTO humanTask) {
		
		String strUserName = humanTask.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		OldInitiatePedEndorsement resultData = PEDQueryMapper.getInstance()
				.getOldInitiatePedEndorsement(bean);

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);

		Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);

		Predicate predicates = builder.equal(searchRoot.<Long> get("key"),
				resultData.getKey());

		criteriaQuery.select(searchRoot).where(builder.and(predicates));

		final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		OldInitiatePedEndorsement existingList = oldInitiatePedQuery
				.getSingleResult();

		PedQuery existingPedQuery = getPedQueryDetails(bean.getKey());
		existingPedQuery.setReplyRemarks(bean.getReplyRemarks());
		
		List<NewInitiatePedEndorsement> newInitiateDetails=null;
		
		List<NewInitiatePedEndorsement> pedInitiateDetails=null;
        
		if(null != bean.getNewInitiatePedEndorsementDto())
		{
		Query findByKey = entityManager.createNamedQuery(
				"NewInitiatePedEndorsement.findByInitateKey").setParameter(
				"initiateKey", existingList.getKey());

		newInitiateDetails = (List<NewInitiatePedEndorsement>) findByKey
				.getResultList();
		
		List<NewInitiatePedEndorsementDTO> newInitiatePedEndorsementDto = bean.getNewInitiatePedEndorsementDto();
		for (NewInitiatePedEndorsementDTO newInitiatePedEndorsementDTO2 : newInitiatePedEndorsementDto) {
			if(newInitiatePedEndorsementDTO2.getICDChapter() != null && newInitiatePedEndorsementDTO2.getICDChapter().getId() != null){
				newInitiatePedEndorsementDTO2.setICDChapterId(newInitiatePedEndorsementDTO2.getICDChapter().getId());
			}
			if(newInitiatePedEndorsementDTO2.getICDCode() != null && newInitiatePedEndorsementDTO2.getICDCode().getId() != null){
				newInitiatePedEndorsementDTO2.setICDCodeId(newInitiatePedEndorsementDTO2.getICDCode().getId());
			}
			if(newInitiatePedEndorsementDTO2.getICDBlock() != null && newInitiatePedEndorsementDTO2.getICDBlock().getId() != null){
				newInitiatePedEndorsementDTO2.setICDBlockId(newInitiatePedEndorsementDTO2.getICDBlock().getId());
			}
			
			if(newInitiatePedEndorsementDTO2.getPedCode() != null && newInitiatePedEndorsementDTO2.getPedCode().getId() != null){
				newInitiatePedEndorsementDTO2.setPedCodeId(newInitiatePedEndorsementDTO2.getPedCode().getId());
			}
		}
		
	    pedInitiateDetails=PEDQueryMapper.getPedInitiateDetailsList(bean.getNewInitiatePedEndorsementDto());
		
		}
		if (newInitiateDetails != null && pedInitiateDetails != null) {
			for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedInitiateDetails) {
				for (NewInitiatePedEndorsement exisitPedInitiate : newInitiateDetails) {
					if (newInitiatePedEndorsement.getKey().equals(exisitPedInitiate.getKey())) {
						newInitiatePedEndorsement.setCreatedDate(exisitPedInitiate.getCreatedDate());
//						newInitiatePedEndorsement.setStatusDate(exisitPedInitiate.getStatusDate());
//						newInitiatePedEndorsement.setActiveStatusDate(exisitPedInitiate.getActiveStatusDate());
					}
				}
			}
		}

		if (existingList != null) {
			Stage stage = new Stage();
			stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

			Status status = new Status();
			status.setKey(ReferenceTable.PED_QUERY_RECEIVED);
			existingList.setStage(stage);
			existingList.setStatus(status);
			existingList.setPedSuggestion(resultData.getPedSuggestion());
			existingList.setPedName(resultData.getPedName());
			existingList.setRemarks(resultData.getRemarks());
			existingList.setRepudiationLetterDate(resultData
					.getRepudiationLetterDate());
			existingList.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			existingList.setModifiedBy(strUserName);
//			existingList.setCreatedBy(resultData.getCreatedBy());

			entityManager.merge(existingList);
			entityManager.flush();
			//entityManager.refresh(existingList);

			if (existingPedQuery != null) {
				existingPedQuery.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				existingPedQuery.setModifiedBy(strUserName);
				existingPedQuery.setStatus(status);
				existingPedQuery.setStage(stage);
				entityManager.merge(existingPedQuery);
				entityManager.flush();
				entityManager.clear();
			}
			
			if(pedInitiateDetails!=null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : pedInitiateDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setModifiedBy(strUserName);
					newInitiatePedEndorsement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(existingList);
					entityManager.merge(newInitiatePedEndorsement);
					entityManager.flush();
					entityManager.clear();
				}
				//entityManager.flush();
			}
			
			setDBOutcomeForPEDQuery(bean, humanTask, SHAConstants.PED_PROCESS_PED_QUERY_REPLY_PROCESSOR);
			return true;
		}
		return false;
	}

	/*public void setBPMOutComeforPedQuery(OldPedEndorsementDTO bean,
			SearchPEDQueryTableDTO humanTask, String outCome) {

		if (humanTask != null) {
			PayloadBOType payload = humanTask.getHumanTask().getPayloadCashless();
			payload.getPedReq().setResult(outCome);
			
			if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
				
				PedReqType pedReq = payload.getPedReq();
				pedReq.setStatus(bean.getPedSuggestion().getId().toString());
				payload.setPedReq(pedReq);
			}
//			Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//			Map<String, String> pedReq = new HashMap<String, String>();
//
//			try {
//
//				if (humanTask.getKey() != null) {
//
//					pedReq.put("key", humanTask.getKey().toString());
//				}
//				//
//				if (humanTask.getIntimationNo() != null) {
//					regIntDetailsReq.put("IntimationNumber",
//							humanTask.getIntimationNo());
//				}
//
//				payload = BPMClientContext.setNodeValue(payload,
//						"RegIntDetails", regIntDetailsReq);
//				payload = BPMClientContext.setNodeValue(payload, "PedReq",
//						pedReq);
//
//			} catch (TransformerException e) {
//				e.printStackTrace();
//			}
			humanTask.getHumanTask().setOutcome(outCome);

//			PedReq pedReqDetails = new PedReq();
//			pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));
//
//			humanTask.getHumanTask().setPayload(payload);
//
//			try {
//				System.out
//						.println("----------------------- value from bpm task");
//				BPMClientContext.printPayloadElement(humanTask
//						.getHumanTask().getPayload());
//			} catch (TransformerException e) {
//				e.printStackTrace();
//			}
//			InvokeHumanTask invokeHT = new InvokeHumanTask();
//			invokeHT.execute(humanTask.getUsername(),humanTask.getPassword(), humanTask.getHumanTask(),
//					null, null, pedReqDetails, null);
			
			SubmitProcessPedQueryTask submitPedQueryTask = BPMClientContext.getSubmitPedQueryTask(humanTask.getUsername(),humanTask.getPassword());
			
			try{
			submitPedQueryTask.execute(humanTask.getUsername(), humanTask.getHumanTask());
			}catch(Exception e){
				e.printStackTrace();
			}

			System.out.println("BPM Executed Successfully");
			
		}
		

	}*/
	
	
	public List<NewInitiatePedEndorsement> getIntitiatePedEndorsementDetails(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"NewInitiatePedEndorsement.findByInitateKey").setParameter(
				"initiateKey", pedKey);

		List<NewInitiatePedEndorsement> newInitiateDetails = (List<NewInitiatePedEndorsement>) findByKey
				.getResultList();
		
		if(newInitiateDetails != null && ! newInitiateDetails.isEmpty()){
			return newInitiateDetails;
		}
		
		return null;
	}

	public boolean getPEDAvailableDetails(Long pedSuggestionKey, Long insuredKey/*, Long pedInitiateKey*/){
		
		boolean pedAvailable = false;
		OldInitiatePedEndorsement pedInitiateObj = getPedDetailsBySuggestionKeyForInsured(pedSuggestionKey, insuredKey); 
		pedAvailable = pedInitiateObj != null ? true : false ;
		return pedAvailable;
	}

	public OldInitiatePedEndorsement getPedDetailsBySuggestionKeyForInsured(Long pedSuggestionKey, Long insuredKey) {
		List<OldInitiatePedEndorsement> pedInitiateDetails = null;
		OldInitiatePedEndorsement pedInitiateObj = null;
		try{
			Query findByIntimateKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByPEDKeyInsuredKey");
			 
			findByIntimateKey.setParameter("pedSuggestionKey", pedSuggestionKey);
			findByIntimateKey.setParameter("insuredKey",insuredKey);
//			findByIntimateKey.setParameter("pedInitiateKey",pedInitiateKey);
	
			pedInitiateDetails = (List<OldInitiatePedEndorsement>) findByIntimateKey
					.getResultList();
					
		}
		catch(Exception e){
			e.printStackTrace();
		}
		if(pedInitiateDetails != null && ! pedInitiateDetails.isEmpty()){
			pedInitiateObj = pedInitiateDetails.get(0);
		}
	
		return pedInitiateObj;
	}
	
	public boolean getPEDAvailableDetailsByPolicyKey(Long suggestionKey, Long policyKey){
		
		boolean pedAvailable = false;
		
		OldInitiatePedEndorsement pedObj = getPedDetailsByPolicyKey(suggestionKey, policyKey);
		
		pedAvailable =  pedObj != null ? true : false;
		
		return pedAvailable;
	
	}
	
	public boolean getPEDAvailableDetailsBySuggestionForPolicy(List<Long> suggestionKeyList, Long policyKey){
		
		boolean pedAvailable = false;
		
		List<OldInitiatePedEndorsement> pedInitiateDetails = null;
		OldInitiatePedEndorsement pedObj = null;
		try{
			Query findByIntimateKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByPedSuggestionForPolicy");
			 
			findByIntimateKey.setParameter("policyKey",policyKey);
			findByIntimateKey.setParameter("pedSuggestionKeyList", suggestionKeyList);
	
			pedInitiateDetails = (List<OldInitiatePedEndorsement>) findByIntimateKey
					.getResultList();
					
			if(pedInitiateDetails != null && ! pedInitiateDetails.isEmpty()){
				pedObj = pedInitiateDetails.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}					
		
		pedAvailable =  pedObj != null ? true : false;
		
		return pedAvailable;
	
	}
	
	public OldInitiatePedEndorsement getPedDetailsByPolicyKey(Long suggestionKey, Long policyKey){
		
		List<OldInitiatePedEndorsement> pedInitiateDetails = null;
		OldInitiatePedEndorsement pedObj = null;
		try{
			Query findByIntimateKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByPolicyAndpedSuggestion");
			 
			findByIntimateKey.setParameter("policyKey",policyKey);
			findByIntimateKey.setParameter("pedSuggestionKey", suggestionKey);
	
			pedInitiateDetails = (List<OldInitiatePedEndorsement>) findByIntimateKey
					.getResultList();
					
			if(pedInitiateDetails != null && ! pedInitiateDetails.isEmpty()){
				pedObj = pedInitiateDetails.get(0);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return pedObj;
	}

	@SuppressWarnings("unchecked")
	public Boolean submitSpecialist(SearchAdviseOnPEDTableDTO searchDTO,
			OldPedEndorsementDTO bean) {
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		Query findByKey = entityManager.createNamedQuery(
				"PedSpecialist.findInitiatePedDetails").setParameter(
				"initiateKey", bean.getKey());
		List<PedSpecialist> infoList = (List<PedSpecialist>) findByKey
				.getResultList();

		List<Long> keysList = new ArrayList<Long>();
		for (PedSpecialist specialist : infoList) {
			keysList.add(specialist.getKey());
		}
		Long maxKey = Collections.max(keysList);

		Query findBymaxKey = entityManager.createNamedQuery(
				"PedSpecialist.findbyKey").setParameter("primaryKey", maxKey);

		PedSpecialist results = (PedSpecialist) findBymaxKey.getSingleResult();

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		Status status = new Status();
		status.setKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
		bean.setStatusKey(ReferenceTable.SPECIALIST_REPLY_RECEIVED);
		Query findByInitiateKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter(
				"primaryKey", bean.getKey());
		OldInitiatePedEndorsement pedInitiate = (OldInitiatePedEndorsement) findByInitiateKey
				.getSingleResult();
		if (pedInitiate != null) {
			pedInitiate.setStage(stage);
			pedInitiate.setStatus(status);
			pedInitiate.setModifiedBy(strUserName);
			pedInitiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(pedInitiate);
			entityManager.flush();
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(pedInitiate.getKey());
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					entityManager.merge(newInitiatePedEndorsement);
					entityManager.flush();
				}
			}

			//entityManager.refresh(pedInitiate);
		}

		if (results != null) {
			results.setStage(stage);
			results.setStatus(status);
			results.setSpecialistRemarks(bean.getSpecialistRemarks());
			results.setModifiedBy(strUserName);
			results.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			if(bean.getFileName() != null && bean.getTokenName() != null){
				results.setFileName(bean.getFileName());
				results.setDocumentToken(bean.getTokenName());
			}
			entityManager.merge(results);
			entityManager.flush();
			entityManager.clear();
			//setBPMoutComeForSpecialist(searchDTO, bean, "APPROVE");
			setDBOutcomeForAdviseOnPED(searchDTO, bean);
			return true;

		}
		return false;

	}

	/*public void setBPMoutComeForSpecialist(
			SearchAdviseOnPEDTableDTO resultTask, OldPedEndorsementDTO bean,
			String outCome) {
		try {
			
		}
		catch(Exception e) {
		}
			
			if (resultTask != null) {
				PayloadBOType payload = resultTask.getHumanTask().getPayloadCashless();
				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
				Map<String, String> pedReq = new HashMap<String, String>();
				
//
//				try {
//
//					if (resultTask.getKey() != null) {
//
//						pedReq.put("key", resultTask.getKey().toString());
//					}
//					//
//					if (resultTask.getIntimationNo() != null) {
//						regIntDetailsReq.put("IntimationNumber",
//								resultTask.getIntimationNo());
//					}
//					payload = BPMClientContext.setNodeValue(payload,
//							"RegIntDetails", regIntDetailsReq);
//					payload = BPMClientContext.setNodeValue(payload, "PedReq",
//							pedReq);
//
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				resultTask.getHumanTask().setOutcome(outCome);

//				PedReq pedReqDetails = new PedReq();
//				pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));

				resultTask.getHumanTask().setPayloadCashless(payload);
//
//				try {
//					System.out
//							.println("----------------------- value from bpm task");
//					BPMClientContext.printPayloadElement(resultTask
//							.getHumanTask().getPayload());
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				
				SubmitAdviseOnPedTask submitPedAdviseTask = BPMClientContext.getSubmitPedAdviseTask(resultTask.getUsername(),resultTask.getPassword());
				
				try{
				submitPedAdviseTask.execute(resultTask.getUsername(), resultTask.getHumanTask());
				}catch(Exception e){
					e.printStackTrace();
				}
//				InvokeHumanTask invokeHT = new InvokeHumanTask();
//				invokeHT.execute(resultTask.getUsername(),resultTask.getPassword(),
//						resultTask.getHumanTask(), null, null, pedReqDetails,
//						null);

				System.out.println("BPM Executed Successfully");
			}
	}*/

	public Boolean submitPedEndorsement(PEDQueryDTO bean, Long preauthKey,
			Intimation intimation, Policy policy, Claim claim, String userName, String passWord,String presenterString) {
		
		PedEndorsementMapper mapper = PedEndorsementMapper.getInstance();
		
		List<ViewPEDTableDTO> viewPedTableDTO = bean.getViewPedTableDTO();

		List<NewInitiatePedEndorsement> newInitiatePedEndorsementList = null;
		
		if(bean.getPedSuggestion() != null 
				&& bean.getPedSuggestion().getId() != null
				&& !(ReferenceTable.PED_SUGGESTION_SUG004.equals(bean.getPedSuggestion().getId()) || ReferenceTable.PED_SUGGESTION_SUG010.equals(bean.getPedSuggestion().getId()))){
			newInitiatePedEndorsementList = mapper.getNewInitiatePedEndorsementList(viewPedTableDTO);
		}	

		if(! bean.getIsEditPED()){
			OldInitiatePedEndorsement initiatePed = mapper
					.getOldInitiatePedEndorsement(bean);
	
	
			if (initiatePed != null) {
	
				initiatePed.setClaim(claim);
				initiatePed.setPolicy(policy);
				initiatePed.setIntimation(intimation);
	
				Status status = new Status();
				status.setKey(ReferenceTable.PED_INITIATE);
				initiatePed.setStatus(status);
				
				if(bean.getIsWatchListReviewer() != null){
					if(bean.getIsWatchListReviewer()){
						if(bean.getIsAddWatchList() != null && bean.getIsAddWatchList()){
							status.setKey(ReferenceTable.PED_WATCHLIST_APPROVER);
							initiatePed.setStatus(status);
						}else{
							status.setKey(ReferenceTable.PED_APPROVED);
							initiatePed.setRemarks(SHAConstants.PED_DIRECT_UW);
							initiatePed.setStatus(status);
						}
					}else{
						if(bean.getIsAddWatchList() != null && bean.getIsAddWatchList()){
							status.setKey(ReferenceTable.PED_WATCHLIST_APPROVER);
							initiatePed.setStatus(status);
						}else if(bean.getIsDiscussed() != null && bean.getIsDiscussed()){
							status.setKey(ReferenceTable.PED_APPROVED);
							initiatePed.setRemarks(SHAConstants.PED_DIRECT_UW);
							initiatePed.setStatus(status);
						}
					}
				}
				
				
				
	
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);
				initiatePed.setStage(stage);
				bean.setStageKey(stage.getKey());
				
				if(preauthKey != null){
					initiatePed.setTransactionKey(preauthKey);
					initiatePed.setInitiateFlag("I");
					
					if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.CASHLESS_STRING)){
						initiatePed.setTransactionFlag("C");
					}	
					
					if(presenterString != null && presenterString.equalsIgnoreCase(SHAConstants.REIMBURSEMENT_SCREEN)){
						initiatePed.setTransactionFlag("R");
					}
				}

				if(presenterString != null && (presenterString.equalsIgnoreCase(SHAConstants.OUTSIDE_PROCESS_SCREEN) || presenterString.equalsIgnoreCase(SHAConstants.PED_INITIATE_OUTSIDE_PROCESS))){
					if(bean.getInsuredName() != null){
						Long insuredKey = bean.getInsuredName().getId();
						initiatePed.setInsuredKey(insuredKey);
						initiatePed.setInitiateFlag("O");
						
					}
					initiatePed.setTransactionFlag("O");
				}
				
				if(bean.getPedEffFromDate() != null){
					initiatePed.setPedEffectiveFromDate(bean.getPedEffFromDate());
				}
				
				String strUserName = SHAUtils.getUserNameForDB(userName);
				initiatePed.setCreatedBy(strUserName);
//	            initiatePed.setVersion(1l);
	            initiatePed.setProcessType("I");
	            initiatePed.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(initiatePed);
	
				entityManager.flush();
				
				//entityManager.refresh(initiatePed);
				
				if (newInitiatePedEndorsementList != null) {
	
					for (NewInitiatePedEndorsement newInitiatePedEndorsement : newInitiatePedEndorsementList) {
	
						if (initiatePed.getKey() != null) {
							newInitiatePedEndorsement
									.setOldInitiatePedEndorsement(initiatePed);
							newInitiatePedEndorsement.setStatus(status);
							newInitiatePedEndorsement.setStage(stage);
//							newInitiatePedEndorsement.setVersion(1l);
							newInitiatePedEndorsement.setDeletedFlag("N");
							// newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiatePed);
							if(newInitiatePedEndorsement.getPedExclusionFlag() != null && newInitiatePedEndorsement.getPedExclusionFlag().equalsIgnoreCase(SHAConstants.YES_FLAG)){
								newInitiatePedEndorsement.setPedExclusionFlag(SHAConstants.YES_FLAG);
							}else{
								newInitiatePedEndorsement.setPedExclusionFlag(SHAConstants.N_FLAG);
							}
							
							entityManager.persist(newInitiatePedEndorsement);
						}
	
						entityManager.flush();
						//entityManager.refresh(newInitiatePedEndorsement);
					}
				}
				if (initiatePed.getKey() != null) {/*
					IntimationType intMsg = new IntimationType();
					ClaimType claimType = new ClaimType();
					HospitalInfoType hospitalInfo = new HospitalInfoType();
					intMsg.setStatus(status.getProcessValue());
					claimType.setClaimType(claim.getClaimType().getValue().toUpperCase());
					intMsg.setIntimationNumber(intimation.getIntimationId());
					intMsg.setIsClaimPending(true);
					intMsg.setIsPolicyValid(false);
					intMsg.setIsBalanceSIAvailable(true);
					hospitalInfo.setHospitalType(intimation.getHospitalType().getValue());
					intMsg.setKey(intimation.getKey());
					
					
	
					PolicyType policyType = new PolicyType();
					policyType.setPolicyId(policy.getPolicyNumber());
	
					PedReqType pedReqDetails = new PedReqType();
					pedReqDetails.setKey(initiatePed.getKey());
					pedReqDetails.setRequestorRole(initiatePed.getCreatedBy());
					if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
						pedReqDetails.setStatus(bean.getPedSuggestion().getId().toString());
					}
					
					Insured insured = intimation.getInsured();
					
					ClassificationType classificationType = new ClassificationType();
					if(claim != null && claim.getIsVipCustomer() != null && claim.getIsVipCustomer().equals(1l)){
						
						classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
					}
					else if(insured != null && insured.getInsuredAge() != null && insured.getInsuredAge()>60){
						classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
					}else{
						classificationType.setPriority(SHAConstants.NORMAL);
					}
				
					classificationType.setType(SHAConstants.TYPE_FRESH);
					
					classificationType.setSource(SHAConstants.NORMAL);
					
					if(bean.getStageKey() != null){
						Stage stage1 = entityManager.find(Stage.class, bean.getStageKey());
						classificationType.setSource(stage1.getStageName());
					}
					
					ClaimRequestType claimRequestType = new ClaimRequestType();
					if(null != intimation.getCpuCode() && null != intimation.getCpuCode().getCpuCode())
						claimRequestType.setCpuCode(String.valueOf(intimation.getCpuCode().getCpuCode()));
					
					ProductInfoType productInfoType = new ProductInfoType();
					productInfoType.setLob(SHAConstants.HEALTH_LOB);
					productInfoType.setLobType(SHAConstants.HEALTH_LOB_FLAG);
					
					PayloadBOType payloadBo = new PayloadBOType();
					payloadBo.setClaim(claimType);
					payloadBo.setIntimation(intMsg);
					payloadBo.setHospitalInfo(hospitalInfo);
					payloadBo.setPedReq(pedReqDetails);
					payloadBo.setPolicy(policyType);
					payloadBo.setClaimRequest(claimRequestType);
					payloadBo.setClassification(classificationType);
					payloadBo.setProductInfo(productInfoType);
					
					BPMClientContext.execute(userName, passWord, payloadBo);    
				*/
					bean.setKey(initiatePed.getKey());
					/*Hospitals hosp = getHospitalByKey(intimation.getHospital());
					initiatePed = getPedInitiate(initiatePed.getKey());
					submitPEDTaskToDB(claim, hosp ,initiatePed, bean);*/	
				}
				return true;
			}
		}else{
			//Long version = 0l;
			OldInitiatePedEndorsement pedInitiate = getPedInitiate(bean.getKey());
			if(pedInitiate != null){
				savePedInitiateHistory(pedInitiate);
			}
			if(pedInitiate != null){
				pedInitiate.setPedName(bean.getPedName());
				if(bean.getPedSuggestion() != null){
					MastersValue suggestion = new MastersValue();
					suggestion.setKey(bean.getPedSuggestion().getId());
					suggestion.setValue(bean.getPedSuggestion().getValue());
					
					pedInitiate.setPedSuggestion(suggestion);
				}
//				if(pedInitiate.getVersion() != null){
//					version += 1;
//					pedInitiate.setVersion(version);
//				}
				if(bean.getIsWatchList()){
					Status status = new Status();
					if(pedInitiate.getStatus() != null && pedInitiate.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_APPROVER)){
						status.setKey(ReferenceTable.PED_WATCHLIST_RELEASE_APPROVER);
						pedInitiate.setStatus(status);
						pedInitiate.setAddWatchListFlag(SHAConstants.N_FLAG);
						
						
						if(bean.getIsWatchListReviewer() != null){
							if(bean.getIsWatchListReviewer()){
									status.setKey(ReferenceTable.PED_APPROVED);
									pedInitiate.setRemarks(SHAConstants.PED_DIRECT_UW);
									pedInitiate.setStatus(status);
								}
							else{
								if(bean.getIsDiscussed() != null && bean.getIsDiscussed()){
									status.setKey(ReferenceTable.PED_APPROVED);
									pedInitiate.setRemarks(SHAConstants.PED_DIRECT_UW);
									pedInitiate.setStatus(status);
									pedInitiate.setUwTlFlag(SHAConstants.YES_FLAG);
									if(bean.getDiscussWith() != null){
										SelectValue value = (SelectValue)bean.getDiscussWith();
										pedInitiate.setUwDiscussWith(getMaster(value.getId()));	
									}
									pedInitiate.setUwSuggestion(bean.getDiscussRemarks());
								}
							}
						
						}
						
					}else if(pedInitiate.getStatus() != null && pedInitiate.getStatus().getKey().equals(ReferenceTable.PED_WATCHLIST_PROCESSOR)){
						status.setKey(ReferenceTable.PED_WATCHLIST_RELEASE_PROCESSOR);
						pedInitiate.setStatus(status);
					}
				}
				
				pedInitiate.setRemarks(bean.getRemarks());
				pedInitiate.setReviewRemarks(bean.getReviewRemarks());
				pedInitiate.setRepudiationLetterDate(bean.getRepudiationLetterDate());
				pedInitiate.setModifiedBy(userName);
				pedInitiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				pedInitiate.setProcessType("I");
				entityManager.merge(pedInitiate);
				entityManager.flush();
				entityManager.clear();
				
//				List<ViewPEDTableDTO> viewPedTableDTO = bean.getViewPedTableDTO();
//
//				List<NewInitiatePedEndorsement> newInitiatePedEndorsementList = mapper.getNewInitiatePedEndorsementList(viewPedTableDTO);
				
				if(newInitiatePedEndorsementList != null && !newInitiatePedEndorsementList.isEmpty()){
				
					for (NewInitiatePedEndorsement newInitiatePedEndorsement : newInitiatePedEndorsementList) {
						
						if(newInitiatePedEndorsement.getKey() != null){
							newInitiatePedEndorsement.setOldInitiatePedEndorsement(pedInitiate);
							newInitiatePedEndorsement.setModifiedBy(bean.getUsername());
							newInitiatePedEndorsement.setStage(pedInitiate.getStage());
							newInitiatePedEndorsement.setStatus(pedInitiate.getStatus());
							newInitiatePedEndorsement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	//						newInitiatePedEndorsement.setVersion(version);
							newInitiatePedEndorsement.setDeletedFlag("N");
							entityManager.merge(newInitiatePedEndorsement);
							entityManager.flush();
							entityManager.clear();
						}else{
							newInitiatePedEndorsement.setOldInitiatePedEndorsement(pedInitiate);
							newInitiatePedEndorsement.setCreatedBy(bean.getUsername());
							newInitiatePedEndorsement.setStage(pedInitiate.getStage());
							newInitiatePedEndorsement.setStatus(pedInitiate.getStatus());
	//						newInitiatePedEndorsement.setVersion(version);
							newInitiatePedEndorsement.setDeletedFlag("N");
							entityManager.persist(newInitiatePedEndorsement);
							entityManager.flush();
							entityManager.clear();
						}
					}
				
					List<ViewPEDTableDTO> deletedDTO = bean.getDeletedDTO();
				
					List<NewInitiatePedEndorsement> newInitiatePedEndorse = mapper.getNewInitiatePedEndorsementList(deletedDTO);
				
					for (NewInitiatePedEndorsement newInitiatePedEndorsement : newInitiatePedEndorse) {
						if(newInitiatePedEndorsement.getKey() != null){
							newInitiatePedEndorsement.setOldInitiatePedEndorsement(pedInitiate);
							newInitiatePedEndorsement.setDeletedFlag("Y");
							newInitiatePedEndorsement.setModifiedBy(bean.getUsername());
							newInitiatePedEndorsement.setModifiedDate(new Timestamp(System.currentTimeMillis()));
	//						newInitiatePedEndorsement.setVersion(version);
							entityManager.merge(newInitiatePedEndorsement);
							entityManager.flush();
							entityManager.clear();
						}
					}
				}
			}
			
			if(bean.getIsWatchList()){
				//callBPMNForWatchList(intimation, pedInitiate, userName, passWord);
				callDBForWatchList(intimation, pedInitiate, userName, passWord);
			}
//			updatePayloadForPedSuggestion(intimation, userName, passWord,
//					pedInitiate);
			
			return true;
			
		}
		return false;
	}
	
/*	public void callBPMNForWatchList(Intimation intimation,OldInitiatePedEndorsement initiate, String userName, String password){
		
		PayloadBOType searchPayload = new PayloadBOType();
		
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimation.getIntimationId());
		searchPayload.setIntimation(intimationType);
		
		ProcessorPedReqWLTask taskProcessorPedReqWatchList = BPMClientContext.getTaskProcessorPedReqWatchList();
		PagedTaskList tasks = taskProcessorPedReqWatchList.getTasks(userName, null, searchPayload);
		
		if(tasks != null && tasks.getHumanTasks() != null && ! tasks.getHumanTasks().isEmpty()){
			for (HumanTask humanTask : tasks.getHumanTasks()) {
				
				if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() != null
						&& humanTask.getPayloadCashless().getPedReq().getKey() != null){
					PedReqType pedReq = humanTask.getPayloadCashless().getPedReq();
					if(pedReq.getKey().equals(initiate.getKey())){
				
						SubmitProcessorPedReqWLTask submitProcessorPedReqWatchList = BPMClientContext.getSubmitProcessorPedReqWatchList();
						try {
							humanTask.setOutcome("WATCHLISTRELEASE");
							submitProcessorPedReqWatchList.execute(userName, humanTask);
							break;
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
					
				}
				
			}
		}
		
		ApproverPedReqWLTask taskApproverPedReqWatchList = BPMClientContext.getTaskApproverPedReqWatchList();
		PagedTaskList pagedTaskList = taskApproverPedReqWatchList.getTasks(userName, null, searchPayload);
		
		if(pagedTaskList != null && pagedTaskList.getHumanTasks() != null && ! pagedTaskList.getHumanTasks().isEmpty()){
			
			for (HumanTask humanTask : pagedTaskList.getHumanTasks()) {
				
				if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() != null
						&& humanTask.getPayloadCashless().getPedReq().getKey() != null){
					PedReqType pedReq = humanTask.getPayloadCashless().getPedReq();
					if(pedReq.getKey().equals(initiate.getKey())){
						SubmitApproverPedReqWLTask submitapproverPedReqTask = BPMClientContext.getSubmitApproverPedReqWatchList();
						try {
							humanTask.setOutcome("WATCHLISTRELEASE");
							submitapproverPedReqTask.execute(userName, humanTask);
							break;
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
					
				}
				
			}
		}
		
	}*/
	
	public void callDBForWatchList(Intimation intimation,OldInitiatePedEndorsement initiate, String userName, String password){
		
		Map<String, Object> mapValues = new WeakHashMap<String, Object>();
		
		//Integer totalRecords = 0; 
		
		//List<Long> keys = new ArrayList<Long>(); 
		
		mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
		
		Object[] setMapValues = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		List<Map<String, Object>> taskProcedure   = dbCalculationService.revisedGetTaskProcedure(setMapValues);
		
		/*mapValues.put(SHAConstants.INTIMATION_NO, intimation.getIntimationId());
		mapValues.put(SHAConstants.CURRENT_Q, SHAConstants.PED_WATCHLIST_CURRENT_QUEUE);
		
		Object[] setMapValues1 = SHAUtils.setRevisedObjArrayForGetTask(mapValues);
		List<Map<String, Object>> taskProcedure1   = dbCalculationService.revisedGetTaskProcedure(setMapValues1);
		taskProcedure.addAll(taskProcedure1);*/
		
		//Map<Long, Object> workFlowMap= new WeakHashMap<Long, Object>();
		
		DBCalculationService dbCalService = new DBCalculationService();
		
		if (null != taskProcedure) {
			for (Map<String, Object> outPutArray : taskProcedure) {
				String outcome = (String)outPutArray.get(SHAConstants.OUTCOME);
				Long pedKey = (Long) outPutArray.get(SHAConstants.PAYLOAD_PED_KEY);
				if(pedKey != null && pedKey.equals(initiate.getKey())){
					if(SHAConstants.PED_REQUEST_PROCESSOR_ADD_TO_WATCH_LIST_OUTCOME.equalsIgnoreCase(outcome))
					{
						outPutArray.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_PROCESSOR_WATCHLIST_RELEASED);
					}
					else if(SHAConstants.PED_REQUEST_APPROVER_ADD_TO_WATCH_LIST_OUTCOME.equalsIgnoreCase(outcome))
					{
						outPutArray.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_APPROVER_WATCHLIST_RELEASED);
					}
					
					
					if(initiate.getAddWatchListFlag() != null){
						outPutArray.put(SHAConstants.PAYLOAD_PED_REFERRED_BY,initiate.getAddWatchListFlag());
					}
					
					if(initiate.getUwTlFlag() != null){
						outPutArray.put(SHAConstants.PAYLOAD_REMINDER_TYPE,initiate.getUwTlFlag());
					}
					
					outPutArray.put(SHAConstants.USER_ID,(userName != null ? userName : "ADMINB"));
					
					Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(outPutArray);
					dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				}
			}
		}	
		
	
		System.out.println("BPM Executed Successfully");
	
	}
	
	
/*public Boolean isTaskAvailableInWatchList(Intimation intimation,OldInitiatePedEndorsement initiate, String userName, String password){
		
		PayloadBOType searchPayload = new PayloadBOType();
		
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimation.getIntimationId());
		searchPayload.setIntimation(intimationType);
		
		ProcessorPedReqWLTask taskProcessorPedReqWatchList = BPMClientContext.getTaskProcessorPedReqWatchList();
		PagedTaskList tasks = taskProcessorPedReqWatchList.getTasks(userName, null, searchPayload);
		
		if(tasks != null && tasks.getHumanTasks() != null && ! tasks.getHumanTasks().isEmpty()){
			for (HumanTask humanTask : tasks.getHumanTasks()) {
				
				if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() != null
						&& humanTask.getPayloadCashless().getPedReq().getKey() != null){
					PedReqType pedReq = humanTask.getPayloadCashless().getPedReq();
					if(pedReq.getKey().equals(initiate.getKey())){
						return true;
					}
					
				}
				
			}
		}
		
		ApproverPedReqWLTask taskApproverPedReqWatchList = BPMClientContext.getTaskApproverPedReqWatchList();
		PagedTaskList pagedTaskList = taskApproverPedReqWatchList.getTasks(userName, null, searchPayload);
		
		if(pagedTaskList != null && pagedTaskList.getHumanTasks() != null && ! pagedTaskList.getHumanTasks().isEmpty()){
			
			for (HumanTask humanTask : pagedTaskList.getHumanTasks()) {
				
				if(humanTask.getPayloadCashless() != null && humanTask.getPayloadCashless().getPedReq() != null
						&& humanTask.getPayloadCashless().getPedReq().getKey() != null){
					PedReqType pedReq = humanTask.getPayloadCashless().getPedReq();
					if(pedReq.getKey().equals(initiate.getKey())){
					   return true;
					}
					
				}
				
			}
		}
		
		return false;
		
	}*/

/*
public Boolean isTaskAvailableInWatchListForIntimation(String intimationId){
	
	PayloadBOType searchPayload = new PayloadBOType();
	
	IntimationType intimationType = new IntimationType();
	intimationType.setIntimationNumber(intimationId);
	searchPayload.setIntimation(intimationType);
	
	ProcessorPedReqWLTask taskProcessorPedReqWatchList = BPMClientContext.getTaskProcessorPedReqWatchList();
	PagedTaskList tasks = taskProcessorPedReqWatchList.getTasks(BPMClientContext.BPMN_TASK_USER, null, searchPayload);
	
	if(tasks != null && tasks.getHumanTasks() != null && ! tasks.getHumanTasks().isEmpty()){
		return true;
	}
	
	ApproverPedReqWLTask taskApproverPedReqWatchList = BPMClientContext.getTaskApproverPedReqWatchList();
	PagedTaskList pagedTaskList = taskApproverPedReqWatchList.getTasks(BPMClientContext.BPMN_TASK_USER, null, searchPayload);
	
	if(pagedTaskList != null && pagedTaskList.getHumanTasks() != null && ! pagedTaskList.getHumanTasks().isEmpty()){
		
		return true;
	}
	
	return false;
	
}

	private void updatePayloadForPedSuggestion(Intimation intimation,
			String userName, String passWord,
			OldInitiatePedEndorsement pedInitiate) {
		PayloadBOType payloadBO = new PayloadBOType();
		IntimationType intimationType = new IntimationType();
		intimationType.setIntimationNumber(intimation.getIntimationId());
		payloadBO.setIntimation(intimationType);
		
		String screenName = null;
		
		if(pedInitiate.getStatus().getKey().equals(ReferenceTable.PED_INITIATE) || pedInitiate.getStatus().getKey().equals(ReferenceTable.PED_QUERY_RECEIVED)){
			
			com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask processPedTask = BPMClientContext.getProcessPedTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = processPedTask.getTasks(userName, null, payloadBO);   //userName="pedprocessor1"
			
		}else if(pedInitiate.getStatus().getKey().equals(ReferenceTable.PED_QUERY)){
			
			com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask approvePedTask = BPMClientContext.getApproverPedTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = approvePedTask.getTasks(userName, null, payloadBO);   //UserName="pedapprover1"
			
			ProcessPedQueryTask processPedQueryTask = BPMClientContext
					.getProcessPedQueryTask(userName, passWord); // user Name
																	// and
																	// password
			com.shaic.ims.bpm.claim.corev2.PagedTaskList queryTask = processPedQueryTask.getTasks(userName, null,payloadBO); // ma1 for ped query
			
			//getTask from processor and ped query
		}else if(pedInitiate.getStatus().getKey().equals(ReferenceTable.REFER_TO_SPECIALIST)){
			
			
			//get Task from advise on ped specialist
		}else if(pedInitiate.getStatus().getKey().equals(ReferenceTable.SPECIALIST_REPLY_RECEIVED)){
			
			com.shaic.ims.bpm.claim.servicev2.ped.search.ApproverPedReqTask approvePedTask = BPMClientContext.getApproverPedTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = approvePedTask.getTasks(userName, null, payloadBO);   
			
			com.shaic.ims.bpm.claim.servicev2.ped.search.ProcessorPedReqTask processPedTask = BPMClientContext.getProcessPedTask(userName,passWord);
			com.shaic.ims.bpm.claim.corev2.PagedTaskList processTask = processPedTask.getTasks(userName, null, payloadBO);   
		    
		}
	}*/
	
	public void savePedInitiateHistory(OldInitiatePedEndorsement pedInitiate){
		
		PedEndorementHistory pedHistory = new PedEndorementHistory();
		pedHistory.setPedInitiateKey(pedInitiate.getKey());
		pedHistory.setClaim(pedInitiate.getClaim());
		pedHistory.setIntimation(pedInitiate.getIntimation());
		pedHistory.setTransactionKey(pedInitiate.getTransactionKey());
		pedHistory.setTransactionFlag(pedInitiate.getTransactionFlag());
		pedHistory.setPedInitiateId(pedInitiate.getPedInitiateId());
		pedHistory.setPedSuggestion(pedInitiate.getPedSuggestion());
		pedHistory.setPedName(pedInitiate.getPedName());
		pedHistory.setRemarks(pedInitiate.getRemarks());
		pedHistory.setRejectionRemarks(pedInitiate.getRejectionRemarks());
		pedHistory.setApprovalRemarks(pedInitiate.getApprovalRemarks());
		pedHistory.setProcessorRemarks(pedInitiate.getProcessorRemarks());
		pedHistory.setRepudiationLetterDate(pedInitiate.getRepudiationLetterDate());
		pedHistory.setStage(pedInitiate.getStage());
		pedHistory.setStatus(pedInitiate.getStatus());
		pedHistory.setCreatedBy(pedInitiate.getCreatedBy());
		pedHistory.setCreatedDate(pedInitiate.getCreatedDate());
		pedHistory.setModifiedBy(pedInitiate.getModifiedBy());
		pedHistory.setModifiedDate(pedInitiate.getModifiedDate());
//		pedHistory.setVersion(pedInitiate.getVersion());
		pedHistory.setIntimation(pedInitiate.getIntimation());
		pedHistory.setPolicy(pedInitiate.getpolicy());
		pedHistory.setReviewRemarks(pedInitiate.getReviewRemarks());
		pedHistory.setProcessType(pedInitiate.getProcessType());
		pedHistory.setAddWatchListFlag(pedInitiate.getAddWatchListFlag());
		
		pedHistory.setWatchListRmrks(pedInitiate.getWatchListRmrks());
		
		entityManager.persist(pedHistory);
		entityManager.flush();
		entityManager.clear();
		
		
		List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(pedInitiate.getKey());
		
		savePedInitiateDetails(intitiatePedEndorsementDetails, pedHistory);
	
	    
	}
	
	public void savePedInitiateDetails(List<NewInitiatePedEndorsement> initiateDetails, PedEndorementHistory pedInitiate){
		
		if(initiateDetails != null && !initiateDetails.isEmpty()){
			for (NewInitiatePedEndorsement newInitiatePedEndorsement : initiateDetails) {
				
				PedEndorsementDetailsHistory pedEndorsementDetail = new PedEndorsementDetailsHistory();
				pedEndorsementDetail.setPedCode(newInitiatePedEndorsement.getPedCode());
				pedEndorsementDetail.setPedInitiateDetailsKey(newInitiatePedEndorsement.getKey());
				pedEndorsementDetail.setDescription(newInitiatePedEndorsement.getDescription());
				pedEndorsementDetail.setDeletedFlag(newInitiatePedEndorsement.getDeletedFlag());
				pedEndorsementDetail.setIcdChapterId(newInitiatePedEndorsement.getIcdChapterId());
				pedEndorsementDetail.setIcdBlockId(newInitiatePedEndorsement.getIcdBlockId());
				pedEndorsementDetail.setIcdCodeId(newInitiatePedEndorsement.getIcdCodeId());
				pedEndorsementDetail.setDoctorRemarks(newInitiatePedEndorsement.getDoctorRemarks());
				pedEndorsementDetail.setOthesSpecify(newInitiatePedEndorsement.getOthesSpecify());
				pedEndorsementDetail.setOldInitiatePedEndorsement(pedInitiate);
				pedEndorsementDetail.setActiveStatus(newInitiatePedEndorsement.getActiveStatus());
				pedEndorsementDetail.setSource(newInitiatePedEndorsement.getSource());
				pedEndorsementDetail.setStage(newInitiatePedEndorsement.getStage());
				pedEndorsementDetail.setStatus(newInitiatePedEndorsement.getStatus());
				pedEndorsementDetail.setCreatedBy(newInitiatePedEndorsement.getCreatedBy());
				pedEndorsementDetail.setCreatedDate(newInitiatePedEndorsement.getCreatedDate());
				pedEndorsementDetail.setModifiedBy(newInitiatePedEndorsement.getModifiedBy());
				pedEndorsementDetail.setModifiedDate(newInitiatePedEndorsement.getModifiedDate());
				
				entityManager.persist(pedEndorsementDetail);
				entityManager.flush();
				entityManager.clear();			
			}
		}
		
		
	}

//	public void execute(String a_user, String a_password,
//			IntimationMessage intMsg, PreAuthReq preAuthReq,
//			PedReq pedReqDetails) {
//
//		BPMClientContext instance = new BPMClientContext();
//		System.out.println("BPM Init lookup called");
//		Context context = instance.getInitialContext(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD);
//		PedProcess pedProcess = null;
//
//		try {
//			pedProcess = (PedProcess) context
//					.lookup("PedProcess#com.shaic.ims.bpm.claim.servicev2.ped.PedProcess");
//
//			pedProcess.initiate(BPMClientContext.BPMN_TASK_USER,BPMClientContext.BPMN_PASSWORD, intMsg, preAuthReq,
//					pedReqDetails);
//			System.out.println("Look up called at server side");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}

	public OldInitiatePedEndorsement findByKey(Long key) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
				.createQuery(OldInitiatePedEndorsement.class);

		Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
				.from(OldInitiatePedEndorsement.class);

		Predicate predicates = builder.equal(searchRoot.<Long> get("key"), key);

		criteriaQuery.select(searchRoot).where(builder.and(predicates));

		final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
				.createQuery(criteriaQuery);

		OldInitiatePedEndorsement result = oldInitiatePedQuery
				.getSingleResult();

		return result;

	}

	@SuppressWarnings("unchecked")
	public PedSpecialist getSpecialistDetailsByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery(
				"PedSpecialist.findInitiatePedDetails").setParameter(
				"initiateKey", key);
		List<PedSpecialist> initiate = (List<PedSpecialist>) findByKey
				.getResultList();

		List<Long> keysList = new ArrayList<Long>();
		for (PedSpecialist specialist : initiate) {
			keysList.add(specialist.getKey());
		}
		Long maxKey = Collections.max(keysList);
		
		Query findBymaxKey = entityManager.createNamedQuery(
				"PedSpecialist.findbyKey").setParameter("primaryKey", maxKey);
		
		PedSpecialist results=(PedSpecialist)findBymaxKey.getSingleResult();
		
		if(results != null){
			return results;
		}

		return null;

	}
	
	public PedSpecialist getSpecialistforViewDetails(Long key){
		List<PedSpecialist> pedSpecialist=getSpecialistDetailsListByKey(key);
		
		List<PedSpecialist> processedPedSpecialist=new ArrayList<PedSpecialist>();
		
		for (PedSpecialist pedSpecialist2 : pedSpecialist) {
			if(pedSpecialist2.getInitiatorType().equals("P")){
				processedPedSpecialist.add(pedSpecialist2);
			}
		}
		
		if(! processedPedSpecialist.isEmpty()){
			return processedPedSpecialist.get(processedPedSpecialist.size()-1);
		}
		
		return null;
	}
	
	public PedSpecialist getSpecialistforApprover(Long key){
		
        List<PedSpecialist> pedSpecialist=getSpecialistDetailsListByKey(key);
		
		List<PedSpecialist> processedPedSpecialist=new ArrayList<PedSpecialist>();
		
		for (PedSpecialist pedSpecialist2 : pedSpecialist) {
			if(pedSpecialist2.getInitiatorType().equals("A")){
				processedPedSpecialist.add(pedSpecialist2);
			}
		}
		
		if(! processedPedSpecialist.isEmpty()){
			return processedPedSpecialist.get(processedPedSpecialist.size()-1);
		}
		
		return null;
	}

	public Page<PEDRequestDetailsProcessDTO> searchPedProcess(
			SearchPEDRequestProcessTableDTO formDTO) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		try {
			final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
					.createQuery(OldInitiatePedEndorsement.class);

			Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
					.from(OldInitiatePedEndorsement.class);

			Predicate predicates = builder.equal(searchRoot.get("key"),
					formDTO.getKey());

			criteriaQuery.select(searchRoot).where(builder.and(predicates));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
					.createQuery(criteriaQuery);

			OldInitiatePedEndorsement results = oldInitiatePedQuery
					.getSingleResult();

          Long claimKey = results.getClaim().getKey();
			
//			Query findAllDetails = entityManager
//					.createNamedQuery("Preauth.findByClaimKey").setParameter("claimkey", claimKey);
//			
//			List<Preauth> preauthList = (List<Preauth>) findAllDetails
//					.getResultList();
			
			Predicate preauthQuery = null;
			
//			for (Preauth preauthKey : preauthList) {
//				
				preauthQuery = builder.equal(
						searchRoot.<Preauth> get("claim").<Long> get("key"),
						claimKey);
//			}

			criteriaQuery.select(searchRoot).where(builder.and(preauthQuery));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQueryList = entityManager
					.createQuery(criteriaQuery);

			List<OldInitiatePedEndorsement> resultList = oldInitiatePedQueryList
					.getResultList();

			List<PEDRequestDetailsProcessDTO> resultDto = new ArrayList<PEDRequestDetailsProcessDTO>();

			for (int i = 0; i < resultList.size(); i++) {
				PEDRequestDetailsProcessDTO details = new PEDRequestDetailsProcessDTO();
				
				details.setKey(resultList.get(i).getKey());

				details.setNameofPED(resultList.get(i).getPedName());
				details.setPedSuggestionName(resultList.get(i)
						.getPedSuggestion().getValue());
				details.setRepudiationLetterDate(resultList.get(i)
						.getRepudiationLetterDate());
				details.setRemarks(resultList.get(i).getRemarks());
				details.setRequestorId(resultList.get(i).getCreatedBy());
				details.setRequestStatus(resultList.get(i).getStatus()
						.getProcessValue());
				details.setRequestedDate(SHAUtils.formatDate(resultList.get(i).getCreatedDate()));
				details.setCurrentPED(formDTO.getKey());

				resultDto.add(details);

			}

			for (int i = 0; i < resultDto.size(); i++) {
				//final Long keyList = resultDto.get(i).getKey();
				//final int j = i;
//				Button button = new Button("view Details");
//				button.addClickListener(new Button.ClickListener() {
//					/**
//					 * 
//					 */
//					private static final long serialVersionUID = 1L;
//
//					@Override
//					public void buttonClick(ClickEvent event) {
//					}
//				});
//				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
//				button.addStyleName(ValoTheme.BUTTON_LINK);
//				resultDto.get(i).setViewDetails(button);
//
//				resultDto.get(i).setViewDetails(button);
				OptionGroup radio = new OptionGroup();
				radio.addItems("");
				radio.addValueChangeListener(new ValueChangeListener() {

					/**
				 * 
				 */
					private static final long serialVersionUID = 1L;

					@Override
					public void valueChange(
							com.vaadin.v7.data.Property.ValueChangeEvent event) {

						// new
						// PEDQueryViewImpl().showPopupWindow(resultDto.get(j));
					}
				});
				resultDto.get(i).setSelect(radio);
			}

			Page<PEDRequestDetailsProcessDTO> page = new Page<PEDRequestDetailsProcessDTO>();
			page.setPageNumber(1);
			page.setPageItems(resultDto);
			// page.setPagesAvailable(pagedList.getPagesAvailable());
			if (page != null) {

				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OldInitiatePedEndorsement getPedInitiate(Long pedKey){
		
		Query findByKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter("primaryKey", pedKey);
		List<OldInitiatePedEndorsement> initiate = (List<OldInitiatePedEndorsement>) findByKey
				.getResultList();
		
		if(initiate != null && ! initiate.isEmpty()){
			return initiate.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unused")
	public Boolean updateQueryRemarks(PEDRequestDetailsProcessDTO bean,
			SearchPEDRequestProcessTableDTO searchDto) {
		
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		
		Long version = 0l;
		
		Query findByKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter("primaryKey", searchDto.getKey());
		OldInitiatePedEndorsement initiate = (OldInitiatePedEndorsement) findByKey
				.getSingleResult();
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(initiate);
		}
		
		String strUserName = searchDto.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
	
		// PedQuery pedQuery=getPedQueryDetails(bean.getKey());
		PedQuery pedQuery = new PedQuery();

		pedQuery.setQueryRemarks(bean.getQueryRemarks());
		pedQuery.setNewInitiatePedEndorsement(initiate);
		pedQuery.setActiveStatus(1l);
		pedQuery.setInitiatorType("P");
		pedQuery.setCreatedBy(strUserName);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);
		pedQuery.setStage(stage);

		Status status = new Status();
		status.setKey(ReferenceTable.PED_QUERY);
		pedQuery.setStatus(status);
		
		if (pedQuery != null) {
			entityManager.persist(pedQuery);
			entityManager.flush();
			entityManager.clear();
			// return true;
		}
		
		if(initiate!=null){
			initiate.setStage(stage);
			initiate.setStatus(status);
			initiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			initiate.setModifiedBy(strUserName);
			initiate.setProcessType("P");
			if(bean.getIsEditPED()){
				
				initiate = updatePEDInitiate(initiate,bean);
//				if(initiate.getVersion() != null){
//					version = initiate.getVersion();
//					version += 1;
//					initiate.setVersion(version);
//				}
			}
			
			entityManager.merge(initiate);
			entityManager.flush();
			//entityManager.refresh(initiate);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(initiate.getKey());
			}
			
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
		}
		
		if(bean.getIsEditPED()){
			List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
			updateDeletedDiagnosisList(deletedList,initiate,stage,status,version);
		}

		if (pedQuery.getKey() != null) {
			//setBPMOutcome(searchDto, bean, "QUERY");
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_PROCESSOR_PED_QUERY_OUTCOME);
					
			setDBOutcome(searchDto, bean, "");
			System.out.println("Executed successfully");
			return true;
		}

		return false;

	}
	
	public OldInitiatePedEndorsement updatePEDInitiate(OldInitiatePedEndorsement initiate,PEDRequestDetailsProcessDTO bean){
		
		initiate.setPedName(bean.getNameofPED());
		if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
			 MastersValue pedSuggestion = new MastersValue();
			 pedSuggestion.setKey(bean.getPedSuggestion().getId());
			 pedSuggestion.setValue(bean.getPedSuggestion().getValue());
			 initiate.setPedSuggestion(pedSuggestion);
		}
		
		initiate.setRemarks(bean.getRemarks());
		initiate.setRepudiationLetterDate(bean.getRepudiationLetterDate());
		return initiate;
	}
	
public OldInitiatePedEndorsement updatePEDInitiateForApprover(OldInitiatePedEndorsement initiate,PEDRequestDetailsApproveDTO bean){
		
		initiate.setPedName(bean.getPedApprovalName());
		
		if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
			 MastersValue pedSuggestion = new MastersValue();
			 pedSuggestion.setKey(bean.getPedSuggestion().getId());
			 pedSuggestion.setValue(bean.getPedSuggestion().getValue());
			 initiate.setPedSuggestion(pedSuggestion);
		}
		
		initiate.setRemarks(bean.getRemarks());
		initiate.setRepudiationLetterDate(bean.getRepudiationLetterDate());
		return initiate;
	}



/*	public void setBPMOutcome(SearchPEDRequestProcessTableDTO resultTask,
			PEDRequestDetailsProcessDTO searchDto, String outCome) {
			if (resultTask != null) {
				PayloadBOType payload = resultTask.getHumanTask().getPayloadCashless();
				payload.getPedReq().setResult(outCome);
				payload.getPedReq().setOwnerGroup(outCome);
//				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
//				// Map<String, String> preAuthReqMap = new HashMap<String,
//				// String>();
//				Map<String, String> pedReq = new HashMap<String, String>();
//
//				try {
//
//					if (resultTask.getKey() != null) {
//
//						pedReq.put("key", resultTask.getKey().toString());
//					}
//					//
//					if (resultTask.getIntimationNo() != null) {
//						regIntDetailsReq.put("IntimationNumber",
//								resultTask.getIntimationNo());
//					}
//
//					payload = BPMClientContext.setNodeValue(payload,
//							"RegIntDetails", regIntDetailsReq);
//					payload = BPMClientContext.setNodeValue(payload, "PedReq",
//							pedReq);
//
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				resultTask.getHumanTask().setOutcome(outCome);
				
				if(searchDto.getIsEditPED()){
					PedReqType pedReq = payload.getPedReq();
					if(pedReq == null){
						pedReq = new PedReqType();
					}
					if(searchDto.getPedSuggestion() != null && searchDto.getPedSuggestion().getId() != null){
						pedReq.setStatus(searchDto.getPedSuggestion().getId().toString());
						payload.setPedReq(pedReq);
					}
					
				}

//				PedReq pedReqDetails = new PedReq();
//				pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));

				resultTask.getHumanTask().setPayloadCashless(payload);

//				try {
//					System.out
//							.println("----------------------- value from bpm task");
//					BPMClientContext.printPayloadElement(resultTask
//							.getHumanTask().getPayload());
//				} catch (TransformerException e) {
//					e.printStackTrace();
//				}
				
				SubmitProcessorPedReqTask pedProcessorTask = BPMClientContext.getSubmitPedProcessorTask(resultTask.getUsername(),resultTask.getPassword());
				
				try{
				pedProcessorTask.execute(resultTask.getUsername(), resultTask.getHumanTask());
				}catch(Exception e){
					e.printStackTrace();
				}

				System.out.println("BPM Executed Successfully");
			}
	}*/

	public Boolean updatedReviewRemarks(PEDRequestDetailsProcessDTO bean,
			SearchPEDRequestProcessTableDTO searchDto) {
		Boolean reviewValue = Boolean.FALSE;
		PedEndorsementMapper endormentMapper =  PedEndorsementMapper.getInstance();
		
		Long version = 0l;

		//Long key = searchDto.getKey();
		OldInitiatePedEndorsement initiate = findByKey(searchDto.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(initiate);
		}
		//initiate.setRemarks(bean.getRemarks());
		//initiate.setApprovalRemarks(bean.getRemarks());

		System.out.println("Initaiate status" + initiate.getRemarks());

		Status status = new Status();
		status.setKey(ReferenceTable.SEND_TO_APPROVER);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);

		initiate.setProcessorRemarks(bean.getApprovalRemarks());
		initiate.setStage(stage);
		initiate.setStatus(status);

		if (initiate != null) {
			initiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = searchDto.getUsername();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			initiate.setModifiedBy(strUserName);
			initiate.setProcessType("P");
			if(bean.getIsEditPED()){
				
				initiate = updatePEDInitiate(initiate,bean);
//				if(initiate.getVersion() != null){
//					version  = initiate.getVersion();
//					version += 1;
//					initiate.setVersion(version);
//					
//				}
			}
			
			entityManager.merge(initiate);
			entityManager.flush();
			//entityManager.refresh(initiate);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(initiate.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,initiate,stage,status,version);
			}
		
			//setBPMOutcome(searchDto, bean, "APPROVE");
			/**
			 * Will move this constants to enums later
			 * */
			
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_PROCESSOR_SEND_TO_APPROVER_OUTCOME);
			setDBOutcome(searchDto, bean, "");
			

			//return true;
			reviewValue = Boolean.TRUE;
		}
		//return false;
		return reviewValue;
	}
	
	public void updateDeletedDiagnosisList(List<NewInitiatePedEndorsement> deletedList,OldInitiatePedEndorsement initiate,Stage stage, Status status,Long version){
		
		if(deletedList != null){
			for (NewInitiatePedEndorsement newInitiatePedEndorsement : deletedList) {
				newInitiatePedEndorsement.setStage(stage);
				newInitiatePedEndorsement.setStatus(status);
				newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
				newInitiatePedEndorsement.setDeletedFlag("Y");
				if(newInitiatePedEndorsement.getKey() != null){
//					newInitiatePedEndorsement.setVersion(version);
					entityManager.merge(newInitiatePedEndorsement);
					entityManager.flush();
					entityManager.clear();
					}else{
//						entityManager.persist(newInitiatePedEndorsement);
//						entityManager.flush();
				}
			}
		}
	}

	public Boolean specialistDetails(PEDRequestDetailsProcessDTO bean,
			SearchPEDRequestProcessTableDTO searchDTO) {
		Boolean specialistValue = Boolean.FALSE;
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		
		Long version = 0l;

		Query findByKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter("primaryKey", searchDTO.getKey());
		OldInitiatePedEndorsement initiate = (OldInitiatePedEndorsement) findByKey
				.getSingleResult();
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(initiate);
		}

		MastersValue masterValue = new MastersValue();
		masterValue.setKey(bean.getSpecialistType().getId());
		masterValue.setValue(bean.getSpecialistType().getValue());
		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);

		Status status = new Status();
		status.setKey(ReferenceTable.REFER_TO_SPECIALIST);

		Stage stage = new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);
		
		if(initiate!=null){
			initiate.setStage(stage);
			initiate.setStatus(status);
			initiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			
			initiate.setModifiedBy(strUserName);
			initiate.setProcessType("P");
			if(bean.getIsEditPED()){
				
				initiate = updatePEDInitiate(initiate,bean);
//				if(initiate.getVersion() != null){
//					version = initiate.getVersion();
//					version += 1;
//					initiate.setVersion(version);
//				}
			}
			
			entityManager.merge(initiate);
			entityManager.flush();
			//entityManager.refresh(initiate);

			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(initiate.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
					if(bean.getIsEditPED() != null){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
					entityManager.merge(newInitiatePedEndorsement);
					//entityManager.flush();
					}else{
						entityManager.persist(newInitiatePedEndorsement);
						//entityManager.flush();
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,initiate,stage,status,version);
			}
			
			
		}
		PedSpecialist pedSpecialist = new PedSpecialist();
		pedSpecialist.setInitiatePedDetails(initiate);
		pedSpecialist.setSpecialistType(masterValue);
		pedSpecialist.setReferringReason(bean.getReasonforReferring());
		if(bean.getFileName() != null && bean.getTokenName() != null){
			pedSpecialist.setFileName(bean.getFileName());
			pedSpecialist.setDocumentToken(bean.getTokenName());
		}
		pedSpecialist.setStage(stage);
		pedSpecialist.setStatus(status);
		pedSpecialist.setInitiateType("P");
		pedSpecialist.setCreatedBy(strUserName);
		// PedQuery pedQueryList=new PedQuery();
		// pedQueryList.setNewInitiatePedEndorsement();

		if (pedSpecialist != null) {
			entityManager.persist(pedSpecialist);
			entityManager.flush();
			entityManager.clear();
		//	setBPMOutcome(searchDTO, bean, "SPECIALISTPROCESSOR");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDTO.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_PROCESSOR_REFER_TO_SPECIALIST_OUTCOME);
			setDBOutcome(searchDTO, bean, "");

			//return true;
			specialistValue = Boolean.TRUE;
		}

		return specialistValue;
	}

	public Boolean updateWatchlistProcessor(PEDRequestDetailsProcessDTO bean,
			SearchPEDRequestProcessTableDTO searchDto) {
		Boolean watchListValue = Boolean.FALSE;
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		
		Long version = 0l;

		//Long key = searchDto.getKey();
		OldInitiatePedEndorsement initiate = findByKey(searchDto.getKey());
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(initiate);
		}
		//initiate.setRemarks(bean.getRemarks());
		//initiate.setApprovalRemarks(bean.getRemarks());
		
		Status status = new Status();
		status.setKey(ReferenceTable.PED_WATCHLIST_PROCESSOR);

		System.out.println("Initiate status" + initiate.getRemarks());

		initiate.setWatchListRemarks(bean.getWatchListRemarks());
		initiate.setWatchListFlag("Y");
		initiate.setWatchListDate(new Timestamp(System.currentTimeMillis()));
//		initiate.setStage(stage);
//		initiate.setStatus(status);

		if (initiate != null) {
			initiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			String strUserName = searchDto.getUsername();
			strUserName = SHAUtils.getUserNameForDB(strUserName);
			initiate.setModifiedBy(strUserName);
			initiate.setStatus(status);
			initiate.setProcessType("P");
			if(bean.getIsEditPED()){
				
				initiate = updatePEDInitiate(initiate,bean);

			}
			
			entityManager.merge(initiate);
			entityManager.flush();
			//entityManager.refresh(initiate);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(initiate.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(initiate.getStage());
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
					}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,initiate,initiate.getStage(),status,version);
			}
		
			//setBPMOutcome(searchDto, bean, "WATCHLIST");
			Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
			wrkFlowMap.put(SHAConstants.OUTCOME,SHAConstants.PED_REQUEST_PROCESSOR_ADD_TO_WATCH_LIST_OUTCOME);
			setDBOutcome(searchDto, bean, "");


			//return true;
			watchListValue = Boolean.TRUE;
		}
		return watchListValue;
		
	}
	
	public Boolean updateSpecialistDetails(PEDRequestDetailsApproveDTO bean,
			SearchPEDRequestApproveTableDTO searchDto) {
		
		PedEndorsementMapper endormentMapper = PedEndorsementMapper.getInstance();
		
		Long version = 0l;
		
		String strUserName = searchDto.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		
		Query findByKey = entityManager.createNamedQuery(
				"OldInitiatePedEndorsement.findByKey").setParameter("primaryKey", searchDto.getKey());
		OldInitiatePedEndorsement initiate = (OldInitiatePedEndorsement) findByKey
				.getSingleResult();
		
		if(bean.getIsEditPED()){
			savePedInitiateHistory(initiate);
		}

		MastersValue masterValue = new MastersValue();
		masterValue.setKey(bean.getSpecialistType().getId());
		masterValue.setValue(bean.getSpecialistType().getValue());
		
		Status status=new Status();
		status.setKey(ReferenceTable.REFER_TO_SPECIALIST);
		
		Stage stage=new Stage();
		stage.setKey(ReferenceTable.PED_ENDORSEMENT_STAGE);
		
		if(initiate!=null){
			initiate.setStage(stage);
			initiate.setStatus(status);
			initiate.setModifiedBy(strUserName);
			initiate.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			initiate.setProcessType("A");
			if(bean.getIsEditPED()){
				initiate = updatePEDInitiateForApprover(initiate, bean);
//				if(initiate.getVersion() != null){
//					version = initiate.getVersion();
//					version += 1;
//					initiate.setVersion(version);
//				}
			}
			
			entityManager.merge(initiate);
			entityManager.flush();
			//entityManager.refresh(initiate);
			
			List<NewInitiatePedEndorsement> intitiatePedEndorsementDetails = null;
			
			if(bean.getIsEditPED()){
				
				
				intitiatePedEndorsementDetails = endormentMapper.getNewInitiatePedEndorsementList(bean.getPedInitiateDetails());
				
			}else{
				intitiatePedEndorsementDetails = getIntitiatePedEndorsementDetails(initiate.getKey());
			}
			
			if(intitiatePedEndorsementDetails != null){
				for (NewInitiatePedEndorsement newInitiatePedEndorsement : intitiatePedEndorsementDetails) {
					newInitiatePedEndorsement.setStage(stage);
					newInitiatePedEndorsement.setStatus(status);
					newInitiatePedEndorsement.setOldInitiatePedEndorsement(initiate);
					if(bean.getIsEditPED()){
//						newInitiatePedEndorsement.setVersion(version);
					}
					if(newInitiatePedEndorsement.getKey() != null){
						entityManager.merge(newInitiatePedEndorsement);
						//entityManager.flush();
						}else{
							entityManager.persist(newInitiatePedEndorsement);
							//entityManager.flush();
						}
					entityManager.flush();
					entityManager.clear();
				}
			}
			
			if(bean.getIsEditPED()){
				List<NewInitiatePedEndorsement> deletedList = endormentMapper.getNewInitiatePedEndorsementList(bean.getDeletedDiagnosis());
				updateDeletedDiagnosisList(deletedList,initiate,stage,status,version);
			}
			
			
		}

		PedSpecialist pedSpecialist = new PedSpecialist();
		pedSpecialist.setInitiatePedDetails(initiate);
		pedSpecialist.setSpecialistType(masterValue);
		pedSpecialist.setReferringReason(bean.getReasonforReferring());
		pedSpecialist.setInitiatorType("A");
		pedSpecialist.setFileName(bean.getFileName());
		pedSpecialist.setDocumentToken(bean.getTokenName());
		pedSpecialist.setCreatedBy(strUserName);

	
		// PedQuery pedQueryList=new PedQuery();
		// pedQueryList.setNewInitiatePedEndorsement();

		
			
			pedSpecialist.setStage(stage);
			pedSpecialist.setStatus(status);
			entityManager.persist(pedSpecialist);
			entityManager.flush();
			entityManager.clear();
			
			//setBPMOutComeForPedApprove(bean, searchDto, "SPECIALISTAPPROVER");
//			setDBOutcomeForPEDApprover(searchDto , bean, SHAConstants.PED_REQUEST_TEAM_LEAD_REFER_TO_SPECIALIST_OUTCOME);
			setDBOutcomeForPEDApprover(searchDto , bean, "");

//			return true;
		

		return true;
	}
	
	
//	@SuppressWarnings("unchecked")
//	public Preauth getPreauthByKey(Long preauthKey) {
//		Query query = entityManager.createNamedQuery("Preauth.findByKey");
//		query.setParameter("preauthKey", preauthKey);
//		List<Preauth> result = (List<Preauth>) query.getResultList();
//		
//		if(result != null && ! result.isEmpty()){
//			return result.get(0);
//		}
//		
//		return null;
//	}
	
	
	public Page<PEDRequestDetailsApproveDTO> searchPedApproval(
			SearchPEDQueryTableDTO formDTO) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		System.out.println("Called one----------------------------------->");
		try {

			final CriteriaQuery<OldInitiatePedEndorsement> criteriaQuery = builder
					.createQuery(OldInitiatePedEndorsement.class);

			Root<OldInitiatePedEndorsement> searchRoot = criteriaQuery
					.from(OldInitiatePedEndorsement.class);

			Predicate predicates = builder.equal(
					searchRoot.<Preauth> get("preauth").<Long> get("key"),
					formDTO.getKey());

			// Predicate predicates=builder.equal(searchRoot.get("key"),
			// formDTO.getKey());

			criteriaQuery.select(searchRoot).where(builder.and(predicates));

			final TypedQuery<OldInitiatePedEndorsement> oldInitiatePedQuery = entityManager
					.createQuery(criteriaQuery);

			List<OldInitiatePedEndorsement> resultList = oldInitiatePedQuery
					.getResultList();

			// List<PEDRequestDetailsApproveDTO>
			// resultDto=PEDQueryMapper.getPEDQueryApprovalTableDTO(resultList);

			List<PEDRequestDetailsApproveDTO> resultDto = new ArrayList<PEDRequestDetailsApproveDTO>();

			for (int i = 0; i < resultList.size(); i++) {
				PEDRequestDetailsApproveDTO details = new PEDRequestDetailsApproveDTO();

				details.setPedApprovalName(resultList.get(i)
						.getPedName());
				details.setPedSuggestionName(resultList.get(i)
						.getPedSuggestion().getValue());
				details.setRepudiationLetterDate(resultList.get(i)
						.getRepudiationLetterDate());
				details.setRemarks(resultList.get(i).getRemarks());
				details.setRequestorId(resultList.get(i).getCreatedBy());
				details.setRequestedDate(resultList.get(i).getCreatedDate());

				resultDto.add(details);

			}
			// for(OldPedEndorsementDTO key:resultDto){
			// final Long keyList=key.getKey();
			// }
			for (int i = 0; i < resultDto.size(); i++) {
				//final Long keyList = resultDto.get(i).getKey();

				Button button = new Button("view Details");
				button.addClickListener(new Button.ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
					}
				});
				button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
				button.addStyleName(ValoTheme.BUTTON_LINK);
				resultDto.get(i).setViewDetails(button);
			}

			Page<PEDRequestDetailsApproveDTO> page = new Page<PEDRequestDetailsApproveDTO>();
			page.setPageNumber(1);
			page.setPageItems(resultDto);
			// page.setPagesAvailable(pagedList.getPagesAvailable());
			if (page != null) {

				return page;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<OldInitiatePedEndorsement> getDTOClass() {
		return OldInitiatePedEndorsement.class;
	}

	public PedQuery getPedQueryDetails(Long key) {
        try
        {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<PedQuery> criteria = builder
					.createQuery(PedQuery.class);

			Root<PedQuery> root = criteria.from(PedQuery.class);

			Predicate newPredicate = builder.equal(
					root.<OldInitiatePedEndorsement> get(
							"newInitiatePedEndorsement").<Long> get("key"),
					key);

			criteria.select(root).where(builder.and(newPredicate));

			final TypedQuery<PedQuery> oldInitiatePedQuery = entityManager
					.createQuery(criteria);

			List<PedQuery> results = oldInitiatePedQuery.getResultList();
			
			List<Long> keyList=new ArrayList<Long>();
		    if (results != null) {
			for (PedQuery pedquery : results) {
				keyList.add(pedquery.getKey());
		     	}
		     }
		    Long queryKey=Collections.max(keyList);
		    
		    Query findByKey = entityManager.createNamedQuery(
					"PedQuery.findByKey").setParameter("primaryKey", queryKey);
		    
			
			PedQuery pedQueryDetails =(PedQuery)findByKey.getSingleResult();
//			if (results != null && results.size() > 0) {
//				pedQueryDetails = results.get(results.size() - 1);
//				entityManager.refresh(pedQueryDetails);
//			}
			if(pedQueryDetails != null){
			return pedQueryDetails;
			}
        }
        catch(Exception e){
        	
        }
			return null;
	}

	public List<PedQuery> getPedQueryDetailsList(Long key) {
		Query findByKey = entityManager.createNamedQuery(
				"PedQuery.findByPedInitiateKey").setParameter("initiatekey", key);
        	List<PedQuery> results =(List<PedQuery>) findByKey
				.getResultList();
            if(results !=null){
			for (PedQuery pedQuery : results) {
				entityManager.refresh(pedQuery);
			}
			return results;
		}
		return null;
	}
	
	public PedQuery getPedQueryforViewDetails(Long key){
		
		List<PedQuery> pedQuery=getPedQueryDetailsList(key);
		
		List<PedQuery> processedPedQuery=new ArrayList<PedQuery>();
	
		if (pedQuery != null && ! pedQuery.isEmpty()) {
			for (PedQuery pedQuery2 : pedQuery) {
				if (pedQuery2.getInitiatorType() != null) {
					if (pedQuery2.getInitiatorType().equals("P")) {
						processedPedQuery.add(pedQuery2);
					}
					else{
//						processedPedQuery.add(pedQuery2);
					}
				}
			}
		}
		if(! processedPedQuery.isEmpty()){
			return processedPedQuery.get(processedPedQuery.size()-1);
		}
		return null;
	}
	
	public PedQuery getPedQueryforApprovePed(Long key){
		
       List<PedQuery> pedQuery=getPedQueryDetailsList(key);
		
		List<PedQuery> processedPedQuery=new ArrayList<PedQuery>();
		
		if (pedQuery != null && ! pedQuery.isEmpty()) {
			for (PedQuery pedQuery2 : pedQuery) {
				if (pedQuery2.getInitiatorType() != null) {
					if (pedQuery2.getInitiatorType().equals("A")) {
						processedPedQuery.add(pedQuery2);
					}
				}
			}
		}
		if(! processedPedQuery.isEmpty()){
			return processedPedQuery.get(processedPedQuery.size()-1);
		}
		return null;
	}

	public List<PedSpecialist> getSpecialistDetailsListByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery(
				"PedSpecialist.findInitiatePedDetails").setParameter("initiateKey", key);
        	List<PedSpecialist> results =(List<PedSpecialist>) findByKey
				.getResultList();
		//List<PedSpecialist> results = oldInitiatePedQuery.getResultList();
	    for (PedSpecialist pedSpecialist : results) {
			entityManager.refresh(pedSpecialist);
		}
		return results;

	}
	
	public void submitPEDTaskToDB(Claim claimObj, Intimation intimation,/* Hospitals hospitals,	OldInitiatePedEndorsement initiatePed,*/PEDQueryDTO bean)
	{
		
		Hospitals hosp = getHospitalByKey(intimation.getHospital());
		OldInitiatePedEndorsement initiatePed = getPedInitiate(bean.getKey());
		
		Object[] arrayListForDBCall = SHAUtils.getRevisedArrayListForDBCall(claimObj, hosp);
		
		Object[] inputArray = (Object[])arrayListForDBCall[0];
		
		Object[] parameter = new Object[1];
		parameter[0] = inputArray;

		Status pedStatus = getStatusByKey(initiatePed.getStatus().getKey());
		initiatePed.setStatus(pedStatus);
		
	//	PedReqType pedReqDetails = new PedReqType();
		inputArray[SHAConstants.INDEX_PED_KEY] = initiatePed.getKey();
		//inputArray[SHAConstants.INDEX_PED_REQUESTOR_ID] = initiatePed.getCreatedBy();
		inputArray[SHAConstants.INDEX_PED_REQUESTOR_ID] = 0;
		inputArray[SHAConstants.INDEX_PED_REQUESTOR_ROLE] = initiatePed.getCreatedBy();
		
		//Added for R1018
		inputArray[SHAConstants.INDEX_USER_ID] = initiatePed.getCreatedBy();
		
		/**
		 * Field missing in mapper. Need to check with sathish sir on this and then
		 * proceed adding the same.
		 * */
		if(bean.getPedSuggestion() != null && bean.getPedSuggestion().getId() != null){
			inputArray[SHAConstants.INDEX_PED_TYPE] = String.valueOf(bean.getPedSuggestion().getId());
			//pedReqDetails.setStatus(bean.getPedSuggestion().getId().toString());
		}
		
		DBCalculationService dbCalculationService = new DBCalculationService();
		Insured insured = claimObj.getIntimation().getInsured();
		if (claimObj != null && claimObj.getIsVipCustomer() != null
				&& claimObj.getIsVipCustomer().equals(1l)) {

			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.VIP_CUSTOMER;
		//	classificationType.setPriority(SHAConstants.VIP_CUSTOMER);
		} else if (insured != null && insured.getInsuredAge() != null
				&& insured.getInsuredAge() > 60) {
			//classificationType.setPriority(SHAConstants.SENIOR_CITIZEN);
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.SENIOR_CITIZEN;
		} else {
			inputArray[SHAConstants.INDEX_PRIORITY] = SHAConstants.NORMAL;
			//classificationType.setPriority(SHAConstants.NORMAL);
		}
				
		inputArray[SHAConstants.INDEX_RECORD_TYPE] = SHAConstants.TYPE_FRESH;
		if(bean.getStageKey() != null){
			
			Stage stageId = getStageFromKey(bean.getStageKey());//entityManager.find(Stage.class, bean.getStageKey());
			if(null != stageId && ReferenceTable.PREAUTH_STAGE.equals(stageId.getKey()))
			{
				inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_PREAUTH_PROCESS;
				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.PREAUTH_INTIATE_PED;
				//classificationType.setSource(SHAConstants.NORMAL);
			}
			else if(null != stageId && ReferenceTable.ENHANCEMENT_STAGE.equals(stageId.getKey()))
			{
				inputArray[SHAConstants.INDEX_STAGE_SOURCE] = SHAConstants.SOURCE_ENHANCEMENT_PROCESS;
				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.ENHANCEMENT_INTIATE_PED;
				//classificationType.setSource(SHAConstants.NORMAL);
			}
			else
			{
				inputArray[SHAConstants.INDEX_OUT_COME] = SHAConstants.OUTSIDE_INITIATE_PED;
			}
			
		}
		
//		inputArray[SHAConstants.INDEX_PED_INITIATED_DATE] = SHAUtils.parseDate(initiatePed.getCreatedDate());
		
		inputArray[SHAConstants.INDEX_PED_INITIATED_DATE] = initiatePed.getCreatedDate();
		inputArray[SHAConstants.INDEX_PREAUTH_STATUS] = initiatePed.getStatus().getProcessValue();
		
		if(initiatePed.getAddWatchListFlag() != null){
			inputArray[SHAConstants.INDEX_PED_REFERRED_BY] = initiatePed.getAddWatchListFlag();	
		}
		
		if(initiatePed.getUwTlFlag() != null){
			inputArray[SHAConstants.INDEX_REMINDER_TYPE] = initiatePed.getUwTlFlag();
		}
		
		dbCalculationService.revisedInitiateTaskProcedure(parameter);
	
	}
	
	public Status getStatusByKey(Long key) {

		Query findByKey = entityManager.createNamedQuery("Status.findByKey")
				.setParameter("statusKey", key);

		Status status = (Status) findByKey.getSingleResult();
		if (status != null) {
			return status;
		}
		return null;
	}
	
	private Stage getStageFromKey(Long key)
	{
		Query query = entityManager.createNamedQuery("Stage.findByKey");
		query = query.setParameter("stageKey", key);
		List<Stage> stageList = query.getResultList();
		if(null != stageList && !stageList.isEmpty())	
		{
			return stageList.get(0);
		}
		return null;
		
	}
	

	public Hospitals getHospitalByKey(Long hospitalKey){
		
		Query findHospitalElement = entityManager
				.createNamedQuery("Hospitals.findByKey").setParameter("key", hospitalKey);
		
		List<Hospitals> hospital  = (List<Hospitals>) findHospitalElement.getResultList();
		if(hospital != null && ! hospital.isEmpty()){
			return hospital.get(0);
		}
		return null;
	}
	
	public void setDBOutcome(SearchPEDRequestProcessTableDTO resultTask,
			PEDRequestDetailsProcessDTO searchDto, String outCome) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) resultTask.getDbOutArray();
//		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);                                       outcome was moved to Prev. calling Method b4 coming here
				if(searchDto.getIsEditPED()){
					wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,String.valueOf(searchDto.getPedSuggestion().getId()));	
				}
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				System.out.println("Submit Executed Successfully");
		 }
	
	public void setDBOutcomeForPEDApprover(SearchPEDRequestApproveTableDTO resultTask,
			PEDRequestDetailsApproveDTO searchDto, String outCome) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) resultTask.getDbOutArray();
		
//		The Out come was decided at the time of submitting the record from (Team Lead / Approver)  
//		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
		
				if(searchDto.getIsEditPED()){
					wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,String.valueOf(searchDto.getPedSuggestion().getId()));	
				}
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				System.out.println("Submit Executed Successfully");
		 }

	
	

	public void setDBOutcomeForPEDQuery(OldPedEndorsementDTO resultTask,
			SearchPEDQueryTableDTO searchDto, String outCome) {
		
		Map<String, Object> wrkFlowMap = (Map<String, Object>) searchDto.getDbOutArray();
		wrkFlowMap.put(SHAConstants.OUTCOME,outCome);
			if(null != resultTask.getPedSuggestion().getId())	
			{
				wrkFlowMap.put(SHAConstants.PAYLOAD_PED_TYPE,String.valueOf(resultTask.getPedSuggestion().getId()));
			}
				
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				System.out.println("Submit Executed Successfully");
		 }
	

	
	public void setDBOutcomeForAdviseOnPED(SearchAdviseOnPEDTableDTO resultTask, OldPedEndorsementDTO bean) {
		
				String outcome = "";
				Map<String, Object> wrkFlowMap = (Map<String, Object>) resultTask.getDbOutArray();
				String previousOutcome = (String) wrkFlowMap.get(SHAConstants.OUTCOME);
				Status pedStatus = getStatusByKey(bean.getStatusKey());
				if (SHAConstants.PED_REQUEST_TEAM_LEAD_REFER_TO_SPECIALIST_OUTCOME.equalsIgnoreCase(previousOutcome))
				{
					outcome = SHAConstants.ADVISE_ON_PED_OUTCOME_FOR_PED_TEAM_LEAD;
				}
				else if(SHAConstants.PED_REQUEST_PROCESSOR_REFER_TO_SPECIALIST_OUTCOME.equalsIgnoreCase(previousOutcome))
				{
					outcome = SHAConstants.ADVISE_ON_PED_OUTCOME_FOR_PED_PROCESSOR;
				}
				else if (SHAConstants.PED_REQUEST_APPROVER_REFER_TO_SPECIALIST_OUTCOME.equalsIgnoreCase(previousOutcome))
				{
					outcome = SHAConstants.ADVISE_ON_PED_OUTCOME_FOR_PED_APPROVER;
				}
				wrkFlowMap.put(SHAConstants.OUTCOME,outcome);
				wrkFlowMap.put(SHAConstants.PAYLOAD_PREAUTH_STATUS, pedStatus.getProcessValue());
				Object[] objArrayForSubmit = SHAUtils.getRevisedObjArrayForSubmit(wrkFlowMap);
				DBCalculationService dbCalService = new DBCalculationService();
				dbCalService.revisedInitiateTaskProcedure(objArrayForSubmit);
				System.out.println("Submit Executed Successfully");
		 }
	
	public Boolean isUserPedReviewer(String userName){
		try {
			Query findAllDetails = entityManager
					.createNamedQuery("MasUser.getByPedReviewer").setParameter("userId", userName.toLowerCase());
			@SuppressWarnings("unchecked")
			List<MasUser> pedInitiateList = (List<MasUser>) findAllDetails
					.getResultList();
			
			if(pedInitiateList != null && !pedInitiateList.isEmpty()) {
				return true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
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

	public List<PedRaisedDetailsDto> getPedRaisedList(Long policyKey, Long insuredKey){
		List<PedRaisedDetailsDto> resultListDto = new ArrayList<PedRaisedDetailsDto>();

		try{
			
			Query findByPolicyKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByPolicy").setParameter(
					"policyKey", policyKey);

			List<OldInitiatePedEndorsement> pedInitiateDetails = (List<OldInitiatePedEndorsement>) findByPolicyKey
					.getResultList();
			
			if(pedInitiateDetails != null && ! pedInitiateDetails.isEmpty()){
				PedRaisedDetailsDto resultDto = null;
				for (OldInitiatePedEndorsement pedInitiatePedEndorsement : pedInitiateDetails) {

					if((pedInitiatePedEndorsement.getIntimation().getInsured().getKey().equals(insuredKey)
							&& ReferenceTable.PED_SUGGESTION_SUG006.equals(pedInitiatePedEndorsement.getPedSuggestion().getKey())
							&& pedInitiatePedEndorsement.getStatus().getKey().equals(ReferenceTable.ENDORSEMENT_APPROVED_BY_PREMIA))
						||	pedInitiatePedEndorsement.getStatus().getKey().equals(ReferenceTable.PED_REJECT))
						continue;
					
					resultDto = new PedRaisedDetailsDto();
					resultDto.setSuggType(pedInitiatePedEndorsement.getPedSuggestion().getMappingCode().replaceFirst("_", " "));
					String[] split = pedInitiatePedEndorsement.getPedSuggestion().getValue().split("-");
					resultDto.setDescription(split.length>0 ? split[1] : "");
					resultDto.setStatusId(pedInitiatePedEndorsement.getStatus().getKey());
					resultDto.setStatusValue(pedInitiatePedEndorsement.getStatus().getProcessValue());
					resultListDto.add(resultDto);
					resultDto = null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resultListDto;
	}
	
	public boolean getPEDAvailableDetailsByICDChapter(WeakHashMap<Integer,Object> inputMap){
		boolean available = false;
		
		try{
			Long pedSuggestionKey = (Long) inputMap.get(1);
			Long diagKey = (Long) inputMap.get(2);
			Long icdChapKey = (Long) inputMap.get(3);
			Long intimationKey = (Long) inputMap.get(4);
			Long insuredKey = (Long) inputMap.get(5);
			
			Query findPedDetails = entityManager.createNamedQuery("NewInitiatePedEndorsement.findByICDChapter");
			findPedDetails.setParameter("pedSuggestionKey", pedSuggestionKey);
			findPedDetails.setParameter("diagKey", diagKey);
			findPedDetails.setParameter("icdChapKey", icdChapKey);
//			findPedDetails.setParameter("intimationKey", intimationKey);
			findPedDetails.setParameter("insuredKey", insuredKey);
			
			List<NewInitiatePedEndorsement> pedInitiateList = (List<NewInitiatePedEndorsement>) findPedDetails.getResultList();
	
			if(pedInitiateList != null && !pedInitiateList.isEmpty()) {		
				available = true;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		return available;
	}
	public List<PedRaisedDetailsDto> getPedRaisedListForAck(Long policyKey)
	{

		List<PedRaisedDetailsDto> resultListDto = new ArrayList<PedRaisedDetailsDto>();

		try{
			
			Query findByPolicyKey = entityManager.createNamedQuery(
					"OldInitiatePedEndorsement.findByPolicy").setParameter(
							"policyKey", policyKey);

			List<OldInitiatePedEndorsement> pedInitiateDetails = (List<OldInitiatePedEndorsement>) findByPolicyKey
					.getResultList();
			
			if(pedInitiateDetails != null && ! pedInitiateDetails.isEmpty()){
				PedRaisedDetailsDto resultDto = null;
				for (OldInitiatePedEndorsement initiatePedEndorsement : pedInitiateDetails) {

					if(ReferenceTable.PED_REJECT.equals(initiatePedEndorsement.getStatus().getKey()))
						continue;
					
					resultDto = new PedRaisedDetailsDto();
					resultDto.setSuggType(initiatePedEndorsement.getPedSuggestion().getMappingCode().replaceFirst("_", " "));
					String[] split = initiatePedEndorsement.getPedSuggestion().getValue().split(" - ");
					resultDto.setDescription(split.length>0 ? split[split.length-1] : "");
					resultDto.setStatusId(initiatePedEndorsement.getStatus().getKey());
					resultDto.setStatusValue(initiatePedEndorsement.getStatus().getProcessValue());
					resultListDto.add(resultDto);
					resultDto = null;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resultListDto;
	
	}
	
	public PedQueryTable getworkItemID(Long workItemId) {

		Query findByKey = entityManager.createNamedQuery("PedQueryTable.findByworkItemID")
				.setParameter("workItemId", workItemId);

		PedQueryTable status = (PedQueryTable) findByKey.getSingleResult();
		if (status != null) {
			return status;
		}
		return null;
	}
	
	public List<PedQueryDetailsTableData> getPedQueryRequestDetails(Long key) {

		Query findByKey = entityManager.createNamedQuery("PedQueryDetailsTableData.findBypedKey")
				.setParameter("key", key);

		List<PedQueryDetailsTableData> status = (List<PedQueryDetailsTableData>) findByKey.getResultList();
		if (status != null) {
			return status;
		}
		return null;
	}

	public Boolean updatedPEDQueryDetails(BancsPEDRequestDetailsApproveDTO bean, BancsSearchPEDRequestApproveTableDTO searchDTO) {		
		String strUserName = searchDTO.getUsername();
		strUserName = SHAUtils.getUserNameForDB(strUserName);
		//String userRole = searchDTO.getUserRole().getEmpName();

		Status status = new Status();
		status.setKey(ReferenceTable.BANCS_PED_QUERY_RECEIVED);
		
		
		for(BancsPEDQueryDetailTableDTO queryDetails : bean.getPedQueryDetailsTableData()){
			 PedQueryDetailsTableData pedQueryDetails = getPEDQueryDetails(queryDetails.getKey());
			 updatePEDQuery(pedQueryDetails.getPedQuery().getKey(),strUserName,status);
			 pedQueryDetails.setRepliedByRole("");
			 pedQueryDetails.setRepliedByUser(strUserName);
			 pedQueryDetails.setRepliedDate(new Timestamp(System.currentTimeMillis()));
			 pedQueryDetails.setReplyRemarks(queryDetails.getReplyRemarks());
			 pedQueryDetails.setModifiedBy(strUserName);
			 pedQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			 pedQueryDetails.setStatus(status);
			 entityManager.merge(pedQueryDetails);
			 entityManager.flush();
		}
		
		return true;

	}

	private void updatePEDQuery(Long key, String strUserName, Status status) {
		PedQueryTable pedQueryDetails = getPEDQuery(key);
		pedQueryDetails.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		pedQueryDetails.setModifiedBy(strUserName);
		pedQueryDetails.setStatus(status);
		entityManager.merge(pedQueryDetails);
		entityManager.flush();
	}

	private PedQueryDetailsTableData getPEDQueryDetails(Long key) {
		Query findByKey = entityManager.createNamedQuery("PedQueryDetailsTableData.findByKey")
				.setParameter("key", key);

		PedQueryDetailsTableData pedQueryDetailsTableData = (PedQueryDetailsTableData) findByKey.getSingleResult();
		if (pedQueryDetailsTableData != null) {
			return pedQueryDetailsTableData;
		}
		return null;
		
	}
	
	private PedQueryTable getPEDQuery(Long key) {
		Query findByKey = entityManager.createNamedQuery("PedQueryTable.findByKey")
				.setParameter("key", key);

		PedQueryTable pedQueryTable = (PedQueryTable) findByKey.getSingleResult();
		if (pedQueryTable != null) {
			return pedQueryTable;
		}
		return null;
		
	}
}


