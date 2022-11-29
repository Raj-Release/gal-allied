package com.shaic.claim.scoring.ppcoding;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.shaic.domain.Claim;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;

@Stateless
public class PPCodingService {

	@PersistenceContext
	protected EntityManager entityManager;
	
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
	
	@SuppressWarnings("unchecked")
	public List<MasPPCoding> getMasPPCoding(String hospitalType) {
		Query findByKey = entityManager.createNamedQuery("MasPPCoding.findDistinctCategory");
		findByKey = findByKey.setParameter("hospitalType", hospitalType);
		findByKey = findByKey.setParameter("ppVersion", ReferenceTable.PP_VERSION);
		List<MasPPCoding> masPPCodings = (List<MasPPCoding>) findByKey.getResultList();
		if (masPPCodings !=null && !masPPCodings.isEmpty()) {
			return masPPCodings;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<PPCoding> getPPCoding(Long intimationKey) {
		Query findByKey = entityManager.createNamedQuery("PPCoding.findByIntimationKey");
		findByKey = findByKey.setParameter("intimationKey", intimationKey);
		findByKey = findByKey.setParameter("ppVersion", ReferenceTable.PP_VERSION);
		List<PPCoding> ppCodings = (List<PPCoding>) findByKey.getResultList();
		if (ppCodings !=null && !ppCodings.isEmpty()) {
			return ppCodings;
		}
		return null;
	}
	
	public List<PPCodingDTO> populatePPCoding(String hospitalType){
		List<MasPPCoding> codings = getMasPPCoding(hospitalType);
		if(codings !=null){
			List<PPCodingDTO> ppCodingDTOs = new ArrayList<PPCodingDTO>();
			for(MasPPCoding masPPCoding:codings){
				PPCodingDTO codingDTO = new PPCodingDTO();
				codingDTO.setHospitalType(masPPCoding.getHospitalType());
				codingDTO.setPpCode(masPPCoding.getPpCode());
				codingDTO.setPpCodingDesc(masPPCoding.getPpCodingDesc());
				codingDTO.setPpVersion(masPPCoding.getPpVersion());
				ppCodingDTOs.add(codingDTO);
			}
			return ppCodingDTOs;
		}
		return null;
	}
	
	public Map<String,Boolean> getPPCodingValues(Long intimationKey,String hospitalType){
		List<PPCoding> ppCodings = getPPCoding(intimationKey);
		Map<String,Boolean> selectedPPCoding = new LinkedHashMap<String, Boolean>();
		if(ppCodings !=null){
			for(PPCoding ppCoding:ppCodings){
				if(ppCoding.getPpScore()!=null && ppCoding.getPpScore().equals("Y")){
					selectedPPCoding.put(ppCoding.getPpCode(), true);
				}else{
					selectedPPCoding.put(ppCoding.getPpCode(), false);
				}
			}
		}else{
			List<MasPPCoding> codings = getMasPPCoding(hospitalType);
			if(codings !=null){
				for(MasPPCoding masPPCoding:codings){
					selectedPPCoding.put(masPPCoding.getPpCode(), false);
				}
			}
		}
		return selectedPPCoding;
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
	
	public void savePPCoding(String argIntimationNumber, String argScreenName,Map<String,Boolean> selectedPPCoding,Boolean ppCodingSelected,String hospitalType){
		
		Intimation selectedIntimation  = getIntimationByNo(argIntimationNumber);
		String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
		Claim claim = getClaimByIntimationkey(selectedIntimation.getKey());
		Hospitals hospital = getHospitalDetailsByKey(selectedIntimation.getHospital());
		if(ppCodingSelected){
			claim.setPpFlag("Y");
			claim.setPpProtected("Y");
			entityManager.merge(claim);
			
			List<PPCoding> codings =getPPCoding(selectedIntimation.getKey());
			if(codings !=null && !codings.isEmpty()){
				for(PPCoding coding : codings){
					coding.setDeleteFlag(1L);
					coding.setModifiedBy(loginUserId);
					coding.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					coding.setPpStage(argScreenName);
					entityManager.merge(coding);
				}
			}
		}else{
			claim.setPpFlag("Y");
			claim.setPpProtected("N");
			entityManager.merge(claim);
			
			if(selectedPPCoding != null){
				List<PPCoding> codings =getPPCoding(selectedIntimation.getKey());
				if(codings !=null && !codings.isEmpty()){
					for(PPCoding coding : codings){
						if(selectedPPCoding.get(coding.getPpCode()) != null){
							String ppsource = selectedPPCoding.get(coding.getPpCode()) ? "Y" : "N";
							if(! coding.getPpScore().equals(ppsource)){
								coding.setPpScore(ppsource);
								coding.setModifiedBy(loginUserId);
								coding.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								coding.setPpStage(argScreenName);
								entityManager.merge(coding);
							}
						}
					}
				}else{
					List<MasPPCoding>  masPPCodings= getMasPPCoding(hospitalType);
					if(masPPCodings !=null && !masPPCodings.isEmpty()){
						for(MasPPCoding masPPCoding:masPPCodings){
							PPCoding ppCoding = new PPCoding();
							ppCoding.setIntimationKey(selectedIntimation.getKey());
							ppCoding.setClaimKey(claim.getKey());
							ppCoding.setPpCode(masPPCoding.getPpCode());
							ppCoding.setHospitalKey(selectedIntimation.getHospital());
							ppCoding.setHospitalType(hospitalType);
							if(hospital != null){
								ppCoding.setHospitalCode(hospital.getHospitalCode());
							}
							if(selectedPPCoding.get(masPPCoding.getPpCode()) != null){
								String ppsource = selectedPPCoding.get(masPPCoding.getPpCode()) ? "Y" : "N";
								ppCoding.setPpScore(ppsource);
							}
							ppCoding.setPpStage(argScreenName);
							ppCoding.setCreatedBy(loginUserId);
							ppCoding.setCreatedDate(new Timestamp(System.currentTimeMillis()));
							ppCoding.setDeleteFlag(0L);
							ppCoding.setPpVersion(ReferenceTable.PP_VERSION);
							entityManager.persist(ppCoding);
						}
						
					}
				}
			}
		}
		entityManager.flush();
		entityManager.clear();
	}
	
	public Map<String,Boolean> getoldPPCodingValues(Long intimationKey,String hospitalType){
		List<PPCoding> ppCodings = getPPCoding(intimationKey);
		Map<String,Boolean> selectedPPCoding = new LinkedHashMap<String, Boolean>();
		if(ppCodings !=null){
			for(PPCoding ppCoding:ppCodings){
				if(ppCoding.getOldppScore() != null){					
					if(ppCoding.getOldppScore().equals("Y")){
						selectedPPCoding.put(ppCoding.getPpCode(), true);	
					}else{
						selectedPPCoding.put(ppCoding.getPpCode(), false);
					}
				}else if(ppCoding.getPpScore() != null){					
					if(ppCoding.getPpScore().equals("Y")){
						selectedPPCoding.put(ppCoding.getPpCode(), true);	
					}else{
						selectedPPCoding.put(ppCoding.getPpCode(), false);
					}
				}
			}
		}
		
		return selectedPPCoding;
	}
	
	public Claim getClaimsByIntimationNumber(String intimationNo){
		Query findByIntimationKey = entityManager.createNamedQuery("Claim.findByIntimationNumber");
		findByIntimationKey = findByIntimationKey.setParameter("intimationNumber", intimationNo);
		Claim claim = (Claim) findByIntimationKey.getSingleResult();
		if(claim != null){
			return claim;
		}
		return null;
	}

	public void deletePPCoding(String intimationNo){
		Claim claim = getClaimsByIntimationNumber(intimationNo);
		if(claim !=null && claim.getPpFlag()!=null 
				&& claim.getPpFlag().equals("Y")){	
			if(claim.getPpProtected() !=null && claim.getPpProtected().equals("N")){
				List<PPCoding> codings=getPPCoding(claim.getIntimation().getKey());
				if(codings !=null && !codings.isEmpty()){
					String loginUserId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);
					for(PPCoding ppCoding:codings){
						ppCoding.setDeleteFlag(1L);
						ppCoding.setModifiedBy(loginUserId);
						ppCoding.setModifiedDate(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(ppCoding);
					}
				}
			}
			claim.setPpFlag("N");
			claim.setPpProtected(null);
			entityManager.merge(claim);
			entityManager.flush();
			entityManager.clear();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PPCoding> getdeletedPPCoding(Long intimationKey,String ppstage) {
		Query findByKey = entityManager.createNamedQuery("PPCoding.findByppstage");
		findByKey = findByKey.setParameter("intimationKey", intimationKey);
		findByKey = findByKey.setParameter("ppVersion", ReferenceTable.PP_VERSION);
		findByKey = findByKey.setParameter("ppStage", ppstage);
		List<PPCoding> ppCodings = (List<PPCoding>) findByKey.getResultList();
		if (ppCodings !=null && !ppCodings.isEmpty()) {
			return ppCodings;
		}
		return null;
	}
	
	public Map<String,Boolean> getdeletedoldPPCodingValues(Long intimationKey,String hospitalType,String ppstage){
		List<PPCoding> ppCodings = getdeletedPPCoding(intimationKey,ppstage);
		Map<String,Boolean> selectedPPCoding = new LinkedHashMap<String, Boolean>();
		if(ppCodings !=null){
			for(PPCoding ppCoding:ppCodings){
				if(ppCoding.getOldppScore() != null){					
					if(ppCoding.getOldppScore().equals("Y")){
						selectedPPCoding.put(ppCoding.getPpCode(), true);	
					}else{
						selectedPPCoding.put(ppCoding.getPpCode(), false);
					}
				}else if(ppCoding.getPpScore() != null){					
					if(ppCoding.getPpScore().equals("Y")){
						selectedPPCoding.put(ppCoding.getPpCode(), true);	
					}else{
						selectedPPCoding.put(ppCoding.getPpCode(), false);
					}
				}
			}
		}
		
		return selectedPPCoding;
	}
	
}
