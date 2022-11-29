/**
 * 
 */
package com.shaic.claim.edithospitalInfo.search;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.Intimation;
import com.shaic.domain.preauth.Preauth;

/**
 * @author VijayaRagavender
 *
 */
@Stateless
public class SearchEditHospitalDetailsService extends AbstractDAO<Intimation> {

	public SearchEditHospitalDetailsService() {
		super();
	}

	public Page<SearchEditHospitalDetailsTableDTO> search(
			SearchEditHospitalDetailsFormDTO formDTO, String userName, String passWord) {/*
		try 
		{
			String strIntimationNo = "";//formDTO.getIntimationNo();
			String strPolicyNo = "";//formDTO.getPolicyNo();
			Date intimationDate = null;// formDTO.getIntimationDate();
			
			IntimationType intimationType = new IntimationType();
			PolicyType policyType = new PolicyType();
			
			if(null != formDTO.getIntimationNo() && !("").equalsIgnoreCase(formDTO.getIntimationNo()))
			{
				strIntimationNo = formDTO.getIntimationNo();
				intimationType.setIntimationNumber(strIntimationNo);
			}
			
			if(null != formDTO.getPolicyNo() && !("").equalsIgnoreCase(formDTO.getPolicyNo()))
			{
				strPolicyNo = formDTO.getPolicyNo();
				policyType.setPolicyId(strPolicyNo);
				//intimationType.setIntimationNumber(strPolicyNo);
			}
			
			if(null != formDTO.getIntimationDate() )
			{
				//String date = formDTO.getIntimationDate();
				//String date1 = date.replaceAll("-", "/");
				String date1 = SHAUtils.formatIntimationDateValue(formDTO.getIntimationDate());
				
				//SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				
				intimationType.setIntDate(new Date(date1));
			}
			
			PayloadBOType payloadCashless = new PayloadBOType();
			payloadCashless.setIntimation(intimationType);
			payloadCashless.setPolicy(policyType);
			
			
			if(strIntimationNo != null && strIntimationNo.equals("")){
				
				strIntimationNo = null;
			}
			if(strPolicyNo != null && strPolicyNo.equals("")){
				strPolicyNo = null;
			}
			
			HospitalQF hospQF = null;
			if(strPolicyNo != null || strIntimationNo != null)
			{
				
				hospQF = new HospitalQF();
				hospQF.setIntimationNumber(strIntimationNo);
//				hospQF.setIntimationNumber("");
				hospQF.setPolicyId(strPolicyNo);
				///hospQF.setIntDate(intimationDate);
				hospQF.setIntDate(Utilities.changeDatetoString(intimationDate));
			}
			
			Pageable pageable = null;
			List<SearchEditHospitalDetailsTableDTO> searchEditHospitalDetailsTableDTO = new ArrayList<SearchEditHospitalDetailsTableDTO>();
			com.shaic.ims.bpm.claim.servicev2.hms.search.EditHospitalDetailsTask editHospitalDetailsTask = BPMClientContext.getEditHospitalDetailsTask(userName,passWord);
			//PagedTaskList tasks = editHospitalDetailsTask.getTasks(userName, passWord, pageable, hospQF);  //userName="hms1"
			com.shaic.ims.bpm.claim.corev2.PagedTaskList tasks = editHospitalDetailsTask.getTasks(userName, pageable, payloadCashless);  //userName="hms1"
			Map<Long,HumanTask> humanTaskMap = new HashMap<Long, HumanTask>();
			if(null != tasks)
			{
				List<HumanTask> humanTasks = tasks.getHumanTasks();
				List<Long> keys = new ArrayList<Long>();  
				for (HumanTask item: humanTasks)
			    {
					PayloadBOType payload = item.getPayloadCashless();
					if(null != payload)
					{
						IntimationType intimationTypeObj = payload.getIntimation();
						if(null != intimationTypeObj)
						{
							Long keyValue = intimationTypeObj.getKey();
							humanTaskMap.put(keyValue, item);
							keys.add(keyValue);				
						}
					}
					//Map<String, String> valuesFromBPM = BPMClientContext.getMapFromPayload(item.getPayload(), "RegIntDetails");	
					Long keyValue = Long.valueOf(valuesFromBPM.get("key"));
					humanTaskMap.put(keyValue, item);
					keys.add(keyValue);				
			    }
				keys.add(194l);
				
				
		    if(null != keys && 0!= keys.size())
		    {
				List<Intimation> resultList = new ArrayList<Intimation>();	
				final CriteriaBuilder IntimationBuilder = entityManager.getCriteriaBuilder();
				final CriteriaQuery<Intimation> criteriaQuery = IntimationBuilder
						.createQuery(Intimation.class);
				Root<Intimation> searchRootForIntimation = criteriaQuery.from(Intimation.class);
				criteriaQuery.where(searchRootForIntimation.<Long> get("key").in(keys));
				final TypedQuery<Intimation> intimateInfoQuery = entityManager
						.createQuery(criteriaQuery);
				resultList =  intimateInfoQuery.getResultList();
				List<Intimation> pageItemList = resultList;
					searchEditHospitalDetailsTableDTO = SearchEditHospitalDetailsMapper.getInstance().getSearchEditHospitalDetailsTableDTO(pageItemList);
					
					for(SearchEditHospitalDetailsTableDTO objSearchEditHospitalTableDTO : searchEditHospitalDetailsTableDTO)
					{
//						objSearchEditHospitalTableDTO.setHumanTask(humanTaskMap.get(objSearchEditHospitalTableDTO.getKey()));
					}
		    }
		}
		    Page<Intimation> pagedList = super.pagedList(formDTO.getPageable());
			Page<SearchEditHospitalDetailsTableDTO> page = new Page<SearchEditHospitalDetailsTableDTO>();
			page.setPageItems(searchEditHospitalDetailsTableDTO);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	*/
		
	return null;
	}

	public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(java.util.Date date) {
		if(date == null) {
		return null;
		} else {
			
			DatatypeFactory df = null;
			XMLGregorianCalendar xmlGregorianCal = null;
			try {
				df = DatatypeFactory.newInstance();
				GregorianCalendar gregorianCal = new GregorianCalendar();
				gregorianCal.setTimeInMillis(date.getTime());
				xmlGregorianCal = df.newXMLGregorianCalendar(gregorianCal);
			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return xmlGregorianCal;
	
		}
		}
	
	@SuppressWarnings("unchecked")
	public Preauth getEditHospitalDetailsDetailsKey(Long hospitalDetailsKey) {
		
		Query findByKey = entityManager.createNamedQuery("Preauth.findAll");

		List<Preauth> hospitalDetailsList = (List<Preauth>) findByKey
				.getResultList();

		if (!hospitalDetailsList.isEmpty()) {
			return hospitalDetailsList.get(0);

		}
		return null;
	}

	@Override
	public Class<Intimation> getDTOClass() {
		return Intimation.class;
	}

}
