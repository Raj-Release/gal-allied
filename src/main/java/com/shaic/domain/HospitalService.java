package com.shaic.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.registration.ackhoscomm.search.SearchAcknowledgeHospitalCommunicationTableDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsDTO;
import com.shaic.claim.registration.updateHospitalDetails.UpdateHospitalDetailsMapper;
import com.shaic.domain.preauth.HospitalAcknowledge;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Stage;
import com.shaic.newcode.wizard.domain.HospitalCommunicationUpdateMapper;
import com.shaic.newcode.wizard.dto.HospitalAcknowledgeDTO;
import com.vaadin.v7.data.util.BeanItemContainer;

@Stateless
public class HospitalService {

	@PersistenceContext
	protected EntityManager entityManager;

	public Hospitals getHospitalById(Long id) {
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", id);
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	public TmpHospital getTmpHospitalById(Long id) {
		Query query = entityManager.createNamedQuery("TmpHospital.findByKey");
		query.setParameter("key", id);
		List<TmpHospital> resultList = (List<TmpHospital>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	public OMPHospitals getOMPHospitalById(Long id) {
		if (id != null) {
			return entityManager.find(OMPHospitals.class, id);
		}
		return null;
	}

	public HospitalDto read(Long id, SelectValue hospitalTypeId) {
		HospitalDto hospital = new HospitalDto();
		hospital.setKey(id);

		if (!hospitalTypeId.getValue().toLowerCase().contains("not")) {
			Hospitals registedHospitals = entityManager.find(Hospitals.class,
					id);
			hospital.setName(registedHospitals.getName());
			hospital.setRegistedHospitals(registedHospitals);
			hospital.setCpuId(registedHospitals.getCpuId());

			hospital.setHospitalType(hospitalTypeId);

			if (registedHospitals.getHospitalType() != null
					&& registedHospitals.getHospitalType().getValue() != null
					&& registedHospitals.getHospitalType().getValue()
							.equalsIgnoreCase("network")) {
				SelectValue networkHospitalType = new SelectValue();
				networkHospitalType.setId(registedHospitals.getHospitalType()
						.getKey());
				networkHospitalType.setValue(registedHospitals
						.getHospitalType().getValue());
				hospital.setNetworkHospitalType(networkHospitalType);
			}
		} else {
			TmpHospital tmpHospital = entityManager.find(TmpHospital.class, id);

			if (tmpHospital != null) {
				hospital.setName(tmpHospital.getHospitalName() != null ? tmpHospital
						.getHospitalName() : "");
				hospital.setNotRegisteredHospitals(tmpHospital);
				hospital.setHospitalType(hospitalTypeId);
			}
		}
		return hospital;
	}

	@SuppressWarnings("unchecked")
	public Hospitals searchbyHospitalKey(Long hosLongKey) {
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByKey").setParameter("key", hosLongKey);
		List<Hospitals> hospitalList = (List<Hospitals>) findByHospitalKey
				.getResultList();
		if (hospitalList.size() > 0) {
			return hospitalList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Hospitals getHospitalAddress(Hospitals a_hospitals) {

		System.out.println("***************start******************");
		System.out.println("Value :" + a_hospitals);

		Query query = entityManager.createNamedQuery("VWHospital.findByKey");
		query = query.setParameter("parentKey", a_hospitals.getKey());
		List<Hospitals> addressList = query.getResultList();

		System.out.println("=================addressList======"
				+ addressList.size());

		Hospitals a_VWHospital = new Hospitals();
		for (Hospitals address : addressList)
			a_VWHospital = address;

		return a_VWHospital;

	}

//	@SuppressWarnings("unchecked")
//	public List<Contact> getHospitalContact(Hospitals a_hospitals) {
//
//		System.out.println("***************start******************");
//		System.out.println("Value :" + a_hospitals);
//
//		Query query = entityManager.createNamedQuery("Contact.findByKey");
//		query = query.setParameter("objectKey", a_hospitals.getKey());
//		query = query.setParameter("objectType", "Hospitals");
//		List<Contact> addressList = query.getResultList();
//
//		System.out.println("=================addressList======"
//				+ addressList.size());
//
//		return addressList;
//
//	}

	public List<UnFreezHospitals> searchHospitalPhoneNo(String a_phoneno,String pinCode, String hospitalCode) {

		System.out.println("***************start******************");
		System.out.println("Value :" + a_phoneno);

		List<UnFreezHospitals> result = new ArrayList<UnFreezHospitals>();
		if (a_phoneno == null && pinCode == null && hospitalCode == null) {
			// do nothing - return empty result
		} else {
			try {

				CriteriaBuilder a_criteriaBuilder = entityManager
						.getCriteriaBuilder();
				CriteriaQuery<UnFreezHospitals> a_criteriaQuery = a_criteriaBuilder
						.createQuery(UnFreezHospitals.class);
				Root<UnFreezHospitals> tmpInsuredRoot = a_criteriaQuery
						.from(UnFreezHospitals.class);
				a_criteriaQuery.select(tmpInsuredRoot);

				List<Predicate> predicates = new ArrayList<Predicate>();
				 if(a_phoneno != null){
				predicates.add(a_criteriaBuilder.like(
						tmpInsuredRoot.<String> get("phoneNumber"), "%"
								+ a_phoneno + "%"));
			     }
				
				if(pinCode != null && ! pinCode.isEmpty()){
					predicates.add(a_criteriaBuilder.equal(
							a_criteriaBuilder.lower(tmpInsuredRoot.<String> get("pincode")),pinCode.toLowerCase()));
					
				}
				
				if(hospitalCode != null && ! hospitalCode.isEmpty()){
					predicates.add(a_criteriaBuilder.equal(
							a_criteriaBuilder.lower(tmpInsuredRoot.<String> get("hospitalCode")),hospitalCode.toLowerCase()));
					
				}

				a_criteriaQuery.select(tmpInsuredRoot).where(
						predicates.toArray(new Predicate[] {}));

				// execute query
				TypedQuery<UnFreezHospitals> typedQueryTmpInsured = entityManager
						.createQuery(a_criteriaQuery);
				result = typedQueryTmpInsured.getResultList();

				System.out.println("result.size() ========= " + result.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public List<Hospitals> hospitalSearch(String q, State state,
			CityTownVillage city, boolean isNetworkHospital) {
		String hType = "network";
		if (!isNetworkHospital) {
			hType = "non-network";
		}

		Query query = entityManager
				.createNamedQuery("Hospitals.searchHospital");
		query.setParameter("stateId", state.getKey());
		query.setParameter("cityId", city.getKey());
		query.setParameter("hType", hType.toLowerCase());
		query.setParameter("qHospital", "%" + q.toLowerCase() + "%");
		return (List<Hospitals>) query.getResultList();
	}

	public List<Hospitals> hospitalNameCriteriaSearch(String q, State state,
			CityTownVillage city /*,Locality area*/) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Hospitals> criteriaQuery = criteriaBuilder
				.createQuery(Hospitals.class);
		Root<Hospitals> hospitalRoot = criteriaQuery.from(Hospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));

		predicates.add(criteriaBuilder.equal(hospitalRoot.get("stateId"), state
				.getKey().toString()));
		if (city != null) {
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("cityId"),
					city.getKey().toString()));
		}
		
		//IMSSUPPOR-26883
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("hospitalTypeName"),
				"Freez"));
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("hospitalTypeName")));
//		if (area != null) {
//			predicates.add(criteriaBuilder.equal(
//					hospitalRoot.get("localityId"), area.getKey().toString()));
//		}
		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<Hospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<Hospitals>) typedQueryHospitals.getResultList();

		// execute query
		// TypedQuery<Hospitals> hospitalQuery =
		// entityManager.createQuery(criteriaQuery);
		// Query query =
		// entityManager.createNamedQuery("Hospitals.searchByHospitalName");
		//
		// if(state != null){
		// query.setParameter("stateId", state.getKey());
		// }
		// if(city != null && city.getKey() != null){
		// query.setParameter("cityId", city != null ? city.getKey() : null);
		// }
		// // if(area != null && area.getKey() != null){
		// query.setParameter("localityId", area != null && area.getKey() !=
		// null ? area.getKey() : null);
		// // }
		//
		// query.setParameter("name", "%" + q.toLowerCase() + "%");
		//
		// System.out.println("result size                     "+query.getResultList().size());
		// System.out.println("result name                     "+((Hospitals)query.getResultList().get(0)).getName());
		// return (List<Hospitals>)query.getResultList();
	}
	
	public List<UnFreezHospitals> hospitalNameCriteriaSearchForPaayas(String q, State state,
			CityTownVillage city /*,Locality area*/) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnFreezHospitals> criteriaQuery = criteriaBuilder
				.createQuery(UnFreezHospitals.class);
		Root<UnFreezHospitals> hospitalRoot = criteriaQuery.from(UnFreezHospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));
		
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("hospitalCode")), "PYS%"));

		predicates.add(criteriaBuilder.equal(hospitalRoot.get("stateId"), state
				.getKey().toString()));
		if (city != null) {
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("cityId"),
					city.getKey().toString()));
		}
		//IMSSUPPOR-26883
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("hospitalTypeName"),
				"Freez"));
		
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("hospitalTypeName")));
//		if (area != null) {
//			predicates.add(criteriaBuilder.equal(
//					hospitalRoot.get("localityId"), area.getKey().toString()));
//		}

		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<UnFreezHospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<UnFreezHospitals>) typedQueryHospitals.getResultList();

		// execute query
		// TypedQuery<Hospitals> hospitalQuery =
		// entityManager.createQuery(criteriaQuery);
		// Query query =
		// entityManager.createNamedQuery("Hospitals.searchByHospitalName");
		//
		// if(state != null){
		// query.setParameter("stateId", state.getKey());
		// }
		// if(city != null && city.getKey() != null){
		// query.setParameter("cityId", city != null ? city.getKey() : null);
		// }
		// // if(area != null && area.getKey() != null){
		// query.setParameter("localityId", area != null && area.getKey() !=
		// null ? area.getKey() : null);
		// // }
		//
		// query.setParameter("name", "%" + q.toLowerCase() + "%");
		//
		// System.out.println("result size                     "+query.getResultList().size());
		// System.out.println("result name                     "+((Hospitals)query.getResultList().get(0)).getName());
		// return (List<Hospitals>)query.getResultList();
	}

	public List<Hospitals> hospitalNameSearch(String q, State state,
			CityTownVillage city/*,Locality area*/) {
		Query query = entityManager
				.createNamedQuery("Hospitals.searchByHospitalName");

		query.setParameter("stateId", state.getKey());
		query.setParameter("cityId", city != null ? city.getKey() : null);
//		query.setParameter("localityId", area != null ? area.getKey() : null);
		query.setParameter("name", "%" + q.toLowerCase() + "%");
		return (List<Hospitals>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Hospitals> searchHospitalById(String hospitalCode) {
		Query query = entityManager.createNamedQuery("Hospitals.findByHospCode");
		query.setParameter("hospitalCode", hospitalCode.toUpperCase());
		return (List<Hospitals>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Hospitals> searchHospitalByIrdaCode(String hospitalIrdaCode) {
		Query query = entityManager.createNamedQuery("Hospitals.findByIrdaCode");
		query.setParameter("hospitalIrdaCode", "%" + hospitalIrdaCode.toUpperCase() + "%");
		return (List<Hospitals>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Hospitals> searchHospitalByName(String hospitalName) {
		Query query = entityManager.createNamedQuery("Hospitals.findByName");
		query.setParameter("name", "%" + hospitalName + "%");
		return (List<Hospitals>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Hospitals> searchHospitalByKey(Long hospitalKey) {
		Query query = entityManager
				.createNamedQuery("Hospitals.findByHospitalKey");
		query.setParameter("key", hospitalKey);
		return (List<Hospitals>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Hospitals> searchAllHospital() {
		Query query = entityManager.createNamedQuery("Hospitals.findAll");
		return (List<Hospitals>) query.getResultList();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Hospitals> getAllHospitals() {
//		Query query = entityManager.createNamedQuery("Hospitals.findAllNameandKey");
//		Object[] hospitalArray = query.getResultList();
//		return hospitalArray;
//	}
	
	public BeanItemContainer<SelectValue> getHospitalContainer(){
		List<Hospitals> searchAllHospital = searchAllHospital();
		
		BeanItemContainer<SelectValue> container = new BeanItemContainer<SelectValue>(SelectValue.class);
		
		for (Hospitals hospitals : searchAllHospital) {
			SelectValue selected = new SelectValue();
			selected.setId(hospitals.getKey());
			selected.setValue(hospitals.getName());
			container.addBean(selected);
		}
		
		return container;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Long createTmpHospital(TmpHospital a_tmpHospital) {

		if (a_tmpHospital != null) {
			entityManager.persist(a_tmpHospital);
			entityManager.flush();
		}
		return a_tmpHospital.getKey();
	}

	public void saveTmpHospital(TmpHospital tmpHospital) {
		entityManager.persist(tmpHospital);
		entityManager.flush();
	}

	// Hospital Acknowledgement
	@SuppressWarnings({ "unchecked", "unused" })
	public HospitalAcknowledge getHospitalAcknowledgementByKey(
			Long acknowledHospitalKey) {

		Query findByKey = entityManager.createNamedQuery(
				"HospitalAcknowledge.findByHospitalKey").setParameter(
				"hospitalKey", acknowledHospitalKey);

		List<HospitalAcknowledge> hospitalList = (List<HospitalAcknowledge>) findByKey
				.getResultList();

		if (!hospitalList.isEmpty()) {
			return hospitalList.get(0);

		}
		return null;
	}

	public Boolean saveHospitalDetails(HospitalAcknowledgeDTO hospitalDto,SearchAcknowledgeHospitalCommunicationTableDTO searchFormDto) {
		if (hospitalDto != null) {
				// For update the data into Hospital Acknowledgement Table

				HospitalAcknowledge hospitals =  HospitalCommunicationUpdateMapper.getInstance()
						.getHospitalAcknowledge(hospitalDto);
				
				Stage stage = new Stage();
				stage.setKey(ReferenceTable.ACKNOWLEDGE_HOSPITAL_STAGE);
				
				Status status = new Status();
				status.setKey(ReferenceTable.ACKNOWLEDGE_HOSPTIAL_STATUS);
				
				Preauth preauth = hospitalDto.getPreauth();
//				preauth.setStatus(status);
//				preauth.setStage(stage);
				
				String username = searchFormDto.getUsername();
				username = SHAUtils.getUserNameForDB(username);
//				preauth.setModifiedBy(username);
//				preauth.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				
//				entityManager.merge(preauth);
//				entityManager.flush();
				
				if(hospitals!=null)
				{
                hospitals.setPreauth(preauth);
                hospitals.setStage(stage);
                hospitals.setStatus(status);
                hospitals.setPolicy(hospitalDto.getPolicy());
                Intimation intimation = entityManager.find(Intimation.class, hospitalDto.getIntimation().getKey());
                hospitalDto.setIntimation(intimation);
                hospitals.setIntimation(hospitalDto.getIntimation());
                hospitals.setActiveStatus(1l);
                
				entityManager.persist(hospitals);
				entityManager.flush();
				
				//setBPMOutCome(hospitalDto, searchFormDto,"APPROVE");
				
				return true;
				}
		}
		return false;

	}
	
    /*  public void setBPMOutCome(HospitalAcknowledgeDTO hospitalDto,SearchAcknowledgeHospitalCommunicationTableDTO humanTask,String outCome){
		
		if (true) {          
			if (humanTask != null) {
				HumanTask humanTaskObj = humanTask.getHumanTask();
				PayloadBOType payloadCashless = humanTaskObj.getPayloadCashless();
				
				PedReqType pedReqType = payloadCashless.getPedReq();
				ClaimType claimType = payloadCashless.getClaim();
				
				//XMLElement payload = humanTask.getHumanTask().getPayload();
				Map<String, String> regIntDetailsReq = new HashMap<String, String>();
				Map<String,String> pedReq=new HashMap<String, String>();
				
			try {
				
				if(humanTask.getKey()!=null){
					
					if(null != pedReqType)
					{
						pedReqType.setKey(humanTask.getKey());
					}
					else
					{
						pedReqType = new PedReqType();
						pedReqType.setKey(humanTask.getKey());
					}
					pedReq.put("key", humanTask.getKey().toString());
					//	
				}
//					
				if(humanTask.getClaimNo()!=null){
					
					if(null != claimType)
					{
						claimType.setClaimId(humanTask.getClaimNo());
					}
					else
					{
						claimType = new ClaimType();
						claimType.setClaimId(humanTask.getClaimNo());
					}
					
					regIntDetailsReq.put("ClaimNumber",humanTask.getClaimNo());
				}
				payloadCashless.setPedReq(pedReqType);
				payloadCashless.setClaim(claimType);
				
				//payload = BPMClientContext.setNodeValue(payload, "RegIntDetails", regIntDetailsReq);
				//payload = BPMClientContext.setNodeValue(payload, "PedReq", pedReq);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			//humanTask.getHumanTask().setOutcome(outCome);
			humanTaskObj.setOutcome(outCome);
			humanTaskObj.setPayloadCashless(payloadCashless);
			
		    PedReq pedReqDetails=new PedReq();
		    pedReqDetails.setKey(Long.valueOf(pedReq.get("key")));
		    
			
			//humanTask.getHumanTask().setPayload(payload);
					
					try {
						BPMClientContext.printPayloadElement(humanTask.getHumanTask().getPayload());
					} catch (TransformerException e) {
						e.printStackTrace();
					}try {
						BPMClientContext.printPayloadElement(humanTask.getHumanTask().getPayload());
					} catch (TransformerException e) {
						
					//InvokeHumanTask invokeHT=new InvokeHumanTask();
					SubmitAckHospitalCommunicationTask submitAckHospitalCommunicationTask = BPMClientContext.getRefractoredAckHospitalCommunicationTask(humanTask.getUsername(),humanTask.getPassword());
					try{
					submitAckHospitalCommunicationTask.execute(humanTask.getUsername(), humanTaskObj);
					}catch(Exception e){
						e.printStackTrace();
					}
					//invokeHT.execute(humanTask.getUsername(),humanTask.getPassword(), humanTask.getHumanTask(), null, null, pedReqDetails, null);
					
					System.out.println("BPM Executed Successfully");
			}	
		   }
	}*/
	
	public Hospitals getHospitalDetailsByKey(Long hospitalKey) {
		Query query = entityManager.createNamedQuery(
				"Hospitals.findByHospitalKey").setParameter("key", hospitalKey);
		Hospitals hospitals = (Hospitals) query.getSingleResult();
		if (hospitals != null) {
			return hospitals;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<UnFreezHospitals> getANHHospitalByName(String name) {
		List<UnFreezHospitals> hospitals = new ArrayList<UnFreezHospitals>();
		Query query = entityManager.createNamedQuery("UnFreezHospitals.findANHByName");

		if (name != null && name != "") {
			query = query.setParameter("name", "%" + name.toUpperCase() + "%");
			query = query.setParameter("hospitalType", "%Non%");

			hospitals = (List<UnFreezHospitals>) query.getResultList();
			if (!hospitals.isEmpty()) {
				return hospitals;
			}
		}
		return hospitals;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getANHHospitalCountCityWise(State state,CityTownVillage city) {
		Long hospitalsCount = null;
		Integer anhCount = null;
		

		if (state != null && city != null) {
			Query query = entityManager.createNamedQuery("UnFreezHospitals.findANHHospitalCount");
			query = query.setParameter("stateId",  state.getKey());
			query = query.setParameter("cityId", city.getKey());
			query = query.setParameter("networkHospitalTypeId", ReferenceTable.ANH_HOSPITAL_TYPE_ID);
			query = query.setParameter("greenChannelHospitalTypeId", ReferenceTable.GREEN_CHANNEL_HOSPITAL_TYPE_ID);

			hospitalsCount = (Long) query.getSingleResult();
		if(hospitalsCount != null){
			anhCount = hospitalsCount.intValue();
		}
		}else if(state != null){
			Query query = entityManager.createNamedQuery("UnFreezHospitals.findANHHospitalCountStateWise");
			query = query.setParameter("stateId",  state.getKey());
			query = query.setParameter("networkHospitalTypeId", ReferenceTable.ANH_HOSPITAL_TYPE_ID);
			query = query.setParameter("greenChannelHospitalTypeId", ReferenceTable.GREEN_CHANNEL_HOSPITAL_TYPE_ID);

			hospitalsCount = (Long) query.getSingleResult();
		if(hospitalsCount != null){
			anhCount = hospitalsCount.intValue();
		}
		
			
		}
		
		return anhCount;
	}
	

	@SuppressWarnings("unchecked")
	public List<UnFreezHospitals> getANHHospitalByCode(String hospitalcode) {
		List<UnFreezHospitals> hospitals = new ArrayList<UnFreezHospitals>();
		Query query = entityManager.createNamedQuery("UnFreezHospitals.findANHByCode");
		if (hospitalcode != null && hospitalcode != "") {
			query = query
					.setParameter("hospitalCode", "%" + hospitalcode + "%");
			query = query.setParameter("hospitalType", "%Non%");
			hospitals = (List<UnFreezHospitals>) query.getResultList();
			if (!hospitals.isEmpty()) {
				return hospitals;
			}
		}
		return hospitals;
	}

	public Boolean UpdateHospitalDetails(UpdateHospitalDetailsDTO bean) {

		UpdateHospital updateDetails =  UpdateHospitalDetailsMapper.getInstance()
				.getUpdateHospital(bean);

		if (updateDetails != null) {
			entityManager.persist(updateDetails);
			entityManager.flush();
			return true;
		}

		return false;
	}
	
	public Boolean changeHospitalDetails(UpdateHospitalDetailsDTO bean) {

		UpdateHospital updateDetails =  UpdateHospitalDetailsMapper.getInstance()
				.getUpdateHospital(bean);

		if (updateDetails != null) {
			
			updateDetails.setHospitalTypeId(bean.getHospitalType().getId());
			entityManager.persist(updateDetails);
			entityManager.flush();
			
			Intimation intimation = bean.getIntimation();
			
			if(intimation != null){
				if(intimation.getKey() != null){
					if(updateDetails.getKey() != null){
					intimation.setHospital(updateDetails.getKey());
					MastersValue type = new MastersValue();
					type.setKey(bean.getHospitalType().getId());
					type.setValue(bean.getHospitalType().getValue());
					intimation.setHospitalType(type);
					entityManager.merge(intimation);
					entityManager.flush();
					}
				}
			}
			return true;
		}

		return false;
	}
	
	private Hospitals getHospitalDetails(String hospitalId)
	{
		Hospitals hospitalDetail;
		Query findByHospitalKey = entityManager.createNamedQuery(
				"Hospitals.findByCode").setParameter("hospitalCode", hospitalId.toUpperCase());
		try{
			hospitalDetail = (Hospitals) findByHospitalKey.getSingleResult();
			return hospitalDetail;
		}
		catch(Exception e)
		{
		
			return null;
		}
	}
	
	
//	@SuppressWarnings("unchecked")
//	public HospitalPackageRatesDto getHospitalPackageRates(String hospitalCode){
//		HospitalPackageRatesDto packageReateDto = new HospitalPackageRatesDto();
//		List<ANHPackageReatesDto> anhPackageListDto = new ArrayList<ANHPackageReatesDto>();
//		Hospitals hosptialObj = getHospitalDetails(hospitalCode);
//		String hospitalDetails = "";
//		int index = 1;
//		
//		try{
//		if(hosptialObj != null){
//			
//			hospitalDetails = ( hosptialObj.getName() != null ? hosptialObj.getName() : "" ) + 
//					(hosptialObj.getCity() != null ? ","+hosptialObj.getCity()+"," : "" ) +
//					(hosptialObj.getHospitalCode() != null ? hosptialObj.getHospitalCode() : "") ;		
//			packageReateDto.setHospitalDetails(hospitalDetails);
//			Query procedureQuery = entityManager.createNamedQuery("ProcedureMaster.findAll");
//			List<ProcedureMaster> procedureObjList = procedureQuery.getResultList();
//				if(procedureObjList != null && !procedureObjList.isEmpty()){
//					for(ProcedureMaster procedureObj : procedureObjList){
//						
//						//HospitalPackage.findByHospitalCodeAndProcedureCode
//						//Query packageQuery = entityManager.createNamedQuery("HospitalPackage.findByHospitalCodeAndProcedure");
//						Query packageQuery = entityManager.createNamedQuery("HospitalPackage.findByHospitalCodeAndProcedureCode");
//						packageQuery.setParameter("hospitalCode", hosptialObj.getHospitalCode().toLowerCase());
//						packageQuery.setParameter("procedureCode", procedureObj.getProcedureCode());
//						
//						List<HospitalPackage> packageRateList =  (List<HospitalPackage>)packageQuery.getResultList();
//						
//						if(packageRateList != null && !packageRateList.isEmpty()){
//							ANHPackageReatesDto anhPackageDto = new ANHPackageReatesDto();
//							anhPackageDto.setSno(String.valueOf(index));
//							anhPackageDto.setProcedureName(procedureObj.getProcedureName() != null ? procedureObj.getProcedureName() : "");
//							for(HospitalPackage hospPackage :packageRateList){
//								anhPackageDto.setPackageRates(hospPackage);
//								
//							}
//							index++;
//							anhPackageListDto.add(anhPackageDto);
//						}
//						
//						
//					}
//				}
//			
//				packageReateDto.setPackageReatesList(anhPackageListDto);
//		}
			
			
			
			
			
			
			
			
//		Query packageQuery = entityManager.createNamedQuery("HospitalPackage.findByHospitalCode");
//		
//		String hospitalDetails = "";
//		try{	
//		packageQuery.setParameter("hospitalCode", hospitalCode);
//		List<HospitalPackage> packageRateList =  (List<HospitalPackage>)packageQuery.getResultList();
//		
//		if(packageRateList != null && !packageRateList.isEmpty()){
//			packageReateDto = new HospitalPackageRatesDto();
//			hospitalDetails = ( hosptialObj.getName() != null ? hosptialObj.getName() : "" ) + 
//					(hosptialObj.getCity() != null ? ","+hosptialObj.getCity()+"," : "" ) +
//					(hosptialObj.getHospitalCode() != null ? hosptialObj.getHospitalCode() : "") ;
//			packageReateDto.setHospitalDetails(hospitalDetails);
//			
//			List<ANHPackageReatesDto> anhPackageListDto = new ArrayList<ANHPackageReatesDto>();
//			
//			for(HospitalPackage anhPackage :packageRateList){				
//				
//				
//				
//				
//				
////				anhPackageDto.setSno(String.valueOf(packageRateList.indexOf(anhPackage)+1));
//								
//				if(anhPackage.getProcedureCode() != null){
//					
//					Query procedureQuery = entityManager.createNamedQuery("ProcedureMaster.findAll");
//					List<ProcedureMaster> procedureObjList = procedureQuery.getResultList();
//						if(procedureObjList != null && !procedureObjList.isEmpty()){
//				
//							ANHPackageReatesDto anhPackageDto = new ANHPackageReatesDto();
//							for(ProcedureMaster procedureObj : procedureObjList){
//							
//								if(!anhPackageListDto.isEmpty() &&  anhPackageListDto.size()>0){
//									
//									for(ANHPackageReatesDto anhPackagesDto :anhPackageListDto){
//										if(anhPackagesDto.getProcedureName().equalsIgnoreCase(procedureObj.getProcedureName())){
//											anhPackagesDto = anhPackagesDto.setPackageRates(anhPackage);	
//										}
//											
//									}
//								}
//								
//								
//								entityManager.refresh(procedureObj);
//								anhPackageDto.setProcedureName(procedureObj.getProcedureName() != null ? procedureObj.getProcedureName() : "");	
//								anhPackageDto = anhPackageDto.setPackageRates(anhPackage);
//								
//							}						
//							anhPackageDto.setSno(String.valueOf(packageRateList.indexOf(anhPackage)+1));
//							anhPackageListDto.add(anhPackageDto);
//						}
//						
//				}
//				
//				
//			}
//			
//			packageReateDto.setPackageReatesList(anhPackageListDto);
//		}
		
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return packageReateDto;
//	}
	
	public TmpCPUCode getTmpCPUCode(Long cpuID) {
		TmpCPUCode tmpCpuCode = null;

		Query findByTmpCpuCode = entityManager.createNamedQuery(
				"TmpCPUCode.findByKey").setParameter("cpuId",
						cpuID);
		try {
			List resultList = findByTmpCpuCode.getResultList();
			
			if(resultList != null && !resultList.isEmpty()) {
				return (TmpCPUCode) resultList.get(0);
			}

		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tmpCpuCode;
	}
	
	public Hospitals getHospitalByKey(Long key){
		Query query = entityManager.createNamedQuery("Hospitals.findByKey");
		query.setParameter("key", key);
		List<Hospitals> resultList = (List<Hospitals>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
		
	}
	
	public List<Hospitals> hospitalNameForRawCriteriaSearch(String q) 
	{
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Hospitals> criteriaQuery = criteriaBuilder
				.createQuery(Hospitals.class);
		Root<Hospitals> hospitalRoot = criteriaQuery.from(Hospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));

		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<Hospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<Hospitals>) typedQueryHospitals.getResultList();

	}
	
	public UnFreezHospitals getUnFreezHospitalById(Long id) {
		Query query = entityManager.createNamedQuery("UnFreezHospitals.findByKey");
		query.setParameter("key", id);
		List<UnFreezHospitals> resultList = (List<UnFreezHospitals>) query.getResultList();
		if(resultList != null && ! resultList.isEmpty()){
			return resultList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UnFreezHospitals> searchUnFreezHospitalByKey(Long hospitalKey) {
		Query query = entityManager
				.createNamedQuery("UnFreezHospitals.findByHospitalKey");
		query.setParameter("key", hospitalKey);
		return (List<UnFreezHospitals>) query.getResultList();
	}
	
	public List<UnFreezHospitals> UnFreezHospitalNameCriteriaSearch(String q, State state,
			CityTownVillage city /*,Locality area*/) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnFreezHospitals> criteriaQuery = criteriaBuilder
				.createQuery(UnFreezHospitals.class);
		Root<UnFreezHospitals> hospitalRoot = criteriaQuery.from(UnFreezHospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));

		predicates.add(criteriaBuilder.equal(hospitalRoot.get("stateId"), state
				.getKey().toString()));
		if (city != null) {
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("cityId"),
					city.getKey().toString()));
		}
		
		//IMSSUPPOR-26883
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("hospitalTypeName"),
				"Freez"));
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("hospitalTypeName")));
//		if (area != null) {
//			predicates.add(criteriaBuilder.equal(
//					hospitalRoot.get("localityId"), area.getKey().toString()));
//		}
		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<UnFreezHospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<UnFreezHospitals>) typedQueryHospitals.getResultList();

		// execute query
		// TypedQuery<Hospitals> hospitalQuery =
		// entityManager.createQuery(criteriaQuery);
		// Query query =
		// entityManager.createNamedQuery("Hospitals.searchByHospitalName");
		//
		// if(state != null){
		// query.setParameter("stateId", state.getKey());
		// }
		// if(city != null && city.getKey() != null){
		// query.setParameter("cityId", city != null ? city.getKey() : null);
		// }
		// // if(area != null && area.getKey() != null){
		// query.setParameter("localityId", area != null && area.getKey() !=
		// null ? area.getKey() : null);
		// // }
		//
		// query.setParameter("name", "%" + q.toLowerCase() + "%");
		//
		// System.out.println("result size                     "+query.getResultList().size());
		// System.out.println("result name                     "+((Hospitals)query.getResultList().get(0)).getName());
		// return (List<Hospitals>)query.getResultList();
	}

	public ProviderWiseScoring getScoringDetails(String code){
		Query query = entityManager.createNamedQuery("ProviderWiseScoring.findBycode");
		query.setParameter("code", code);
		List<ProviderWiseScoring> result = (List<ProviderWiseScoring>) query.getResultList();
				if(result != null && !result.isEmpty()) {
					return result.get(0);
				}
		return null;
		
	}
	
	public List<UnFreezHospitals> getHospitalsDetailsForOP(String q) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnFreezHospitals> criteriaQuery = criteriaBuilder
				.createQuery(UnFreezHospitals.class);
		Root<UnFreezHospitals> hospitalRoot = criteriaQuery.from(UnFreezHospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));
		
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("hospitalTypeName"),
				"Freez"));
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("hospitalTypeName")));

		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<UnFreezHospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<UnFreezHospitals>) typedQueryHospitals.getResultList();

	}
	
	public List<UnFreezHospitals> OPUnFreezHospitalNameCriteriaSearch(String q, State state,
			CityTownVillage city ,Long  hospitalTypeId) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<UnFreezHospitals> criteriaQuery = criteriaBuilder
				.createQuery(UnFreezHospitals.class);
		Root<UnFreezHospitals> hospitalRoot = criteriaQuery.from(UnFreezHospitals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("name")), "%"
						+ q + "%"));

		predicates.add(criteriaBuilder.equal(hospitalRoot.get("stateId"), state
				.getKey().toString()));
		if (city != null) {
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("cityId"),
					city.getKey().toString()));
		}
		
		if (hospitalTypeId != null) {
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("hospitalType").<Long>get("key"),
					hospitalTypeId));
		}
		
		//IMSSUPPOR-26883
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("hospitalTypeName"),
				"Freez"));
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("hospitalTypeName")));

		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<UnFreezHospitals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<UnFreezHospitals>) typedQueryHospitals.getResultList();

	}
	
	public List<UnFreezHospitals> searchOPHospitalPhoneNo(String a_phoneno,String pinCode, String hospitalCode,Long hospitalTypeID) {

		System.out.println("***************start******************");
		System.out.println("Value :" + a_phoneno);

		List<UnFreezHospitals> result = new ArrayList<UnFreezHospitals>();
		if (a_phoneno == null && pinCode == null && hospitalCode == null) {
			// do nothing - return empty result
		} else {
			try {

				CriteriaBuilder a_criteriaBuilder = entityManager
						.getCriteriaBuilder();
				CriteriaQuery<UnFreezHospitals> a_criteriaQuery = a_criteriaBuilder
						.createQuery(UnFreezHospitals.class);
				Root<UnFreezHospitals> tmpInsuredRoot = a_criteriaQuery
						.from(UnFreezHospitals.class);
				a_criteriaQuery.select(tmpInsuredRoot);

				List<Predicate> predicates = new ArrayList<Predicate>();
				 if(a_phoneno != null){
				predicates.add(a_criteriaBuilder.like(
						tmpInsuredRoot.<String> get("phoneNumber"), "%"
								+ a_phoneno + "%"));
			     }
				
				if(pinCode != null && ! pinCode.isEmpty()){
					predicates.add(a_criteriaBuilder.equal(
							a_criteriaBuilder.lower(tmpInsuredRoot.<String> get("pincode")),pinCode.toLowerCase()));
					
				}
				
				if(hospitalCode != null && ! hospitalCode.isEmpty()){
					predicates.add(a_criteriaBuilder.equal(
							a_criteriaBuilder.lower(tmpInsuredRoot.<String> get("hospitalCode")),hospitalCode.toLowerCase()));
					
				}
				if (hospitalTypeID != null) {
					predicates.add(a_criteriaBuilder.equal(tmpInsuredRoot.get("hospitalType").<Long>get("key"),
							hospitalTypeID));
				}

				a_criteriaQuery.select(tmpInsuredRoot).where(
						predicates.toArray(new Predicate[] {}));

				// execute query
				TypedQuery<UnFreezHospitals> typedQueryTmpInsured = entityManager
						.createQuery(a_criteriaQuery);
				result = typedQueryTmpInsured.getResultList();

				System.out.println("result.size() ========= " + result.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return result;

	}
	
	public List<OPLabPortals> OPLabHospitalNameCriteriaSearch(String q, State state,
			CityTownVillage city ,String  hospitalType) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OPLabPortals> criteriaQuery = criteriaBuilder
				.createQuery(OPLabPortals.class);
		Root<OPLabPortals> hospitalRoot = criteriaQuery.from(OPLabPortals.class);
		criteriaQuery.select(hospitalRoot);

		List<Predicate> predicates = new ArrayList<Predicate>();
		q = q != null ? StringUtils.trim(q).toUpperCase() : "";

		System.out.println("Query string                               " + q);
		predicates.add(criteriaBuilder.like(
				criteriaBuilder.upper(hospitalRoot.<String> get("providerName")), "%"
						+ q + "%"));
		if (state != null) {
			String stateName = state.getValue();
//			stateName = stateName != null ? stateName.toUpperCase() : "";
			predicates.add(criteriaBuilder.like(hospitalRoot.get("state"), "%"+stateName+"%"));
		}
	
		if (city != null) {
			String cityName =  city.getValue();
//			cityName = cityName != null ? cityName.toUpperCase() : "";
			predicates.add(criteriaBuilder.like(hospitalRoot.get("city"),
					"%"+cityName+"%"));
		}
		
		if (hospitalType != null) {
//			hospitalType = hospitalType != null ? hospitalType.toUpperCase() : "";
			predicates.add(criteriaBuilder.equal(hospitalRoot.get("providerType"),
					hospitalType));
		}
		
		predicates.add(criteriaBuilder.notEqual(hospitalRoot.get("providerStatus"),
				"Freez"));
		criteriaQuery.orderBy(criteriaBuilder.asc(hospitalRoot.get("providerName")));

		criteriaQuery.select(hospitalRoot).where(
				predicates.toArray(new Predicate[] {}));

		TypedQuery<OPLabPortals> typedQueryHospitals = entityManager
				.createQuery(criteriaQuery);

		return (List<OPLabPortals>) typedQueryHospitals.getResultList();

	}
	
	@SuppressWarnings("unchecked")
	public Intimation getIntimationByNo(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery(
				"Intimation.findByIntimationNumber").setParameter(
				"intimationNo", intimationNo);

		List<Intimation> intimationList = (List<Intimation>) findByKey
				.getResultList();

		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);

		}
		return null;
	}
	
}
