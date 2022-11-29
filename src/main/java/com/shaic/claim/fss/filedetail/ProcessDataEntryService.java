package com.shaic.claim.fss.filedetail;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shaic.arch.SHAConstants;
import com.shaic.claim.fss.searchfile.SearchDataEntryTableDTO;
import com.shaic.domain.PDIntimationPremia;
import com.shaic.domain.fss.ChequeDetails;
import com.shaic.domain.fss.FileStorage;
import com.shaic.domain.fss.MasClient;
import com.shaic.domain.fss.MasRack;
import com.shaic.domain.fss.MasShelf;
import com.shaic.domain.fss.MasStorageLocation;

@Stateless
public class ProcessDataEntryService {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Logger log = LoggerFactory.getLogger(ProcessDataEntryService.class);

	public ProcessDataEntryService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<ChequeDetails> getChequeDetails(Long fileStorageKey) {

		Query findAll = entityManager.createNamedQuery(
				"ChequeDetails.findChequeByStorageKey").setParameter("fileStorageKey",
						fileStorageKey);
		List<ChequeDetails> chequeDetails = (List<ChequeDetails>) findAll
				.getResultList();

		return chequeDetails;
	}
	
	@SuppressWarnings("unchecked")
	public ChequeDetails getChequeDetailsByKey(Long key) {

		Query findAll = entityManager.createNamedQuery(
				"ChequeDetails.findChequeByKey").setParameter("key",
						key);
		List<ChequeDetails> chequeDetails = (List<ChequeDetails>) findAll
				.getResultList();

		if(chequeDetails != null && !chequeDetails.isEmpty()){
			return chequeDetails.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasClient getClientDetails(String clientName) {

		Query findAll = entityManager.createNamedQuery(
				"MasClient.findByClientName").setParameter("clientName",
						clientName);
		List<MasClient> clientDetails = (List<MasClient>) findAll
				.getResultList();
		if(clientDetails != null && !clientDetails.isEmpty()){
			return clientDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasClient getClient() {

		Query findAll = entityManager.createNamedQuery(
				"MasClient.findByClient");
		List<MasClient> clientDetails = (List<MasClient>) findAll
				.getResultList();
		if(clientDetails != null && !clientDetails.isEmpty()){
			return clientDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasStorageLocation getStorageLocation(Long locationId) {

		Query findAll = entityManager.createNamedQuery(
				"MasStorageLocation.findByLocationKey").setParameter("locationId",
						locationId);
		List<MasStorageLocation> locationDetails = (List<MasStorageLocation>) findAll
				.getResultList();
		if(locationDetails != null && !locationDetails.isEmpty()){
			return locationDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasStorageLocation getStorageLocationByDesc(String storageDesc) {

		Query findAll = entityManager.createNamedQuery(
				"MasStorageLocation.findByLocationByDesc").setParameter("storageDesc",
						storageDesc);
		List<MasStorageLocation> locationDetails = (List<MasStorageLocation>) findAll
				.getResultList();
		if(locationDetails != null && !locationDetails.isEmpty()){
			return locationDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasShelf getShelf(Long shelfId) {

		Query findAll = entityManager.createNamedQuery(
				"MasShelf.findByShelfKey").setParameter("shelfId",
						shelfId);
		List<MasShelf> shelfDetails = (List<MasShelf>) findAll
				.getResultList();
		if(shelfDetails != null && !shelfDetails.isEmpty()){
			return shelfDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasShelf getAdditionalShelf(String shelfName) {

		Query findAll = entityManager.createNamedQuery(
				"MasShelf.findByShelfDesc").setParameter("shelfName",
						shelfName);
		List<MasShelf> shelfDetails = (List<MasShelf>) findAll
				.getResultList();
		if(shelfDetails != null && !shelfDetails.isEmpty()){
			return shelfDetails.get(0);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public MasRack getRack(Long rackId) {

		Query findAll = entityManager.createNamedQuery(
				"MasRack.findByRackKey").setParameter("rackId",
						rackId);
		List<MasRack> rackDetails = (List<MasRack>) findAll
				.getResultList();
		if(rackDetails != null && !rackDetails.isEmpty()){
			return rackDetails.get(0);
		}

		return null;
	}
	
	/*public List<MasStorageLocation> getStorageLocListByCPU(List<Long> cpuList) {

		Query query = entityManager
				.createNamedQuery("MasStorageLocation.findByLocationByCPU");
		query = query.setParameter("cpuList", cpuList);
		List<MasStorageLocation> locList = query.getResultList();
		if (null != locList && !locList.isEmpty()) {
			return locList;
		} else {
			return null;
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public MasRack getRackByDesc(String rackDesc) {

		Query findAll = entityManager.createNamedQuery(
				"MasRack.findByRackDesc").setParameter("rackDesc",
						rackDesc);
		List<MasRack> rackDetails = (List<MasRack>) findAll
				.getResultList();
		if(rackDetails != null && !rackDetails.isEmpty()){
			return rackDetails.get(0);
		}

		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public FileStorage getFileStorageDetail(Long key) {

		Query findAll = entityManager.createNamedQuery(
				"FileStorage.findByStorageKey").setParameter("key",
						key);
		List<FileStorage> fileStorageDetails = (List<FileStorage>) findAll
				.getResultList();
		if(fileStorageDetails != null && !fileStorageDetails.isEmpty()){
			return fileStorageDetails.get(0);
		}

		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	//public String submitProcessRRCRequest(RRCDTO rrcDTO)
	public String submitProcessDataEntry(SearchDataEntryTableDTO dto, String user)
	{
		String msg = "";
		try
		{
			if(user != null && !user.isEmpty()){
				dto.setUsername(user);	
			}
			msg = saveProcessDatatEntry(dto);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	private String saveProcessDatatEntry(SearchDataEntryTableDTO dto) {

		String msg = "";
		if (dto != null && dto.getAddFlag() != null && dto.getAddFlag()
				&& dto.getKey() == null) {
			FileStorage fileStorage = new FileStorage();
			if (dto.getClient() != null) {
				MasClient client = getClientDetails(dto.getClient());
				if (client != null) {
					fileStorage.setClient(client);
				}
			}
			if (dto.getSelectLocation() != null) {
				MasStorageLocation location = getStorageLocation(dto
						.getSelectLocation().getId());
				if (location != null) {
					fileStorage.setStorage(location);
				}
			}

			if (dto.getSelectShelf() != null) {
				MasShelf shelf = getShelf(dto.getSelectShelf().getId());
				if (shelf != null) {
					fileStorage.setShelf(shelf);
				}
			}

			if (dto.getSelectRack() != null) {
				MasRack rack = getRack(dto.getSelectRack().getId());
				if (rack != null) {
					fileStorage.setRack(rack);
				}
			}

			fileStorage.setClaimId(dto.getClaimNo());
			fileStorage.setYear(Integer.valueOf(dto.getYear()));
			fileStorage.setPatientName(dto.getPatientName());
			if (dto.getAlmirahNo() != null && !dto.getAlmirahNo().isEmpty()) {
				fileStorage.setAlmirahNo(dto.getAlmirahNo());
			}
			/*if (dto.getAddlShelfNo() != null) {
					fileStorage.setAddlShelfNo(dto.getAddlShelfNo());
			}*/
			if (dto.getBundleNo() != null) {
				fileStorage.setBundleNo(dto.getBundleNo());
			}
			
			if (dto.getIsCheckInOutStatus() != null) {
				if(dto.getIsCheckInOutStatus())
				fileStorage.setInOutFlag(SHAConstants.YES_FLAG);
				else if(!dto.getIsCheckInOutStatus())
					fileStorage.setInOutFlag(SHAConstants.N_FLAG);
			}
			
			if (dto.getIsRejectStatus() != null) {
				if(dto.getIsRejectStatus())
				fileStorage.setRejectFlag(SHAConstants.YES_FLAG);
				else if(!dto.getIsRejectStatus())
					fileStorage.setRejectFlag(SHAConstants.N_FLAG);
			}
			fileStorage.setCreatedBy(dto.getUsername());
			fileStorage
					.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			fileStorage.setActivateStatus(SHAConstants.YES_FLAG);

			entityManager.persist(fileStorage);
			entityManager.flush();
			msg = SHAConstants.SUCCESS_FLAG;
			if (dto.getChequeList() != null && !dto.getChequeList().isEmpty()) {

				for (ChequeDetailsTableDTO chequeDto : dto.getChequeList()) {
					if(chequeDto.getChequeNo() != null && !chequeDto.getChequeNo().isEmpty()){
						ChequeDetails cheque = new ChequeDetails();
						cheque.setChequeNo(chequeDto.getChequeNo());
						cheque.setFileStorage(fileStorage);
						cheque.setChequeDate(chequeDto.getChequeDate());
						cheque.setBankName(chequeDto.getBankName());
						cheque.setBankBranch(chequeDto.getBankBranch());
						cheque.setCreatedBy(dto.getUsername());
						cheque.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						cheque.setActivateStatus(SHAConstants.YES_FLAG);
						entityManager.persist(cheque);
						entityManager.flush();
						msg = SHAConstants.SUCCESS_FLAG;
					}
					
				}
			}
			return msg;
		} else if (dto != null && dto.getAddFlag() != null && !dto.getAddFlag()
				&& dto.getKey() != null) {
			FileStorage fileStorage = getFileStorageDetail(dto.getKey());
			if (fileStorage != null) {
				if (dto.getClient() != null) {
					MasClient client = getClientDetails(dto.getClient());
					if (client != null) {
						fileStorage.setClient(client);
					}
				}
				if (dto.getSelectLocation() != null) {
					MasStorageLocation location = getStorageLocation(dto
							.getSelectLocation().getId());
					if (location != null) {
						fileStorage.setStorage(location);
					}
				}

				/*if (dto.getAddlShelfNo() != null) {
						fileStorage.setAddlShelfNo(dto.getAddlShelfNo());;
				}*/
				if (dto.getBundleNo() != null) {
					fileStorage.setBundleNo(dto.getBundleNo());
			}

				if (dto.getSelectRack() != null) {
					MasRack rack = getRack(dto.getSelectRack().getId());
					if (rack != null) {
						fileStorage.setRack(rack);
					}
				}

				fileStorage.setClaimId(dto.getClaimNo());
				fileStorage.setYear(Integer.valueOf(dto.getYear()));
				fileStorage.setPatientName(dto.getPatientName());
				if (dto.getAlmirahNo() != null && !dto.getAlmirahNo().isEmpty()) {
					fileStorage.setAlmirahNo(dto.getAlmirahNo());
				}
				if (dto.getSelectShelf() != null) {
					MasShelf shelf = getShelf(dto.getSelectShelf().getId());
					if (shelf != null) {
						fileStorage.setShelf(shelf);
					}
				}
				
				if (dto.getIsCheckInOutStatus() != null) {
					if(dto.getIsCheckInOutStatus())
					fileStorage.setInOutFlag(SHAConstants.YES_FLAG);
					else if(!dto.getIsCheckInOutStatus())
						fileStorage.setInOutFlag(SHAConstants.N_FLAG);
				}
				
				if (dto.getIsRejectStatus() != null) {
					if(dto.getIsRejectStatus())
					fileStorage.setRejectFlag(SHAConstants.YES_FLAG);
					else if(!dto.getIsRejectStatus())
						fileStorage.setRejectFlag(SHAConstants.N_FLAG);
				}
				
				fileStorage.setModifiedBy(dto.getUsername());
				fileStorage.setModifiedDate(new Timestamp(System
						.currentTimeMillis()));
				fileStorage.setActivateStatus(SHAConstants.YES_FLAG);

				entityManager.merge(fileStorage);
				entityManager.flush();
				msg = SHAConstants.SUCCESS_FLAG;

			}
			if (dto.getChequeList() != null && !dto.getChequeList().isEmpty()) {

				for (ChequeDetailsTableDTO chequeDto : dto.getChequeList()) {
					ChequeDetails cheque = getChequeDetailsByKey(chequeDto
							.getKey());
					if (cheque != null) {
						cheque.setChequeNo(chequeDto.getChequeNo());
						cheque.setChequeDate(chequeDto.getChequeDate());
						cheque.setBankName(chequeDto.getBankName());
						cheque.setBankBranch(chequeDto.getBankBranch());
						cheque.setModifiedBy(dto.getUsername());
						cheque.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						cheque.setActivateStatus(SHAConstants.YES_FLAG);
						entityManager.merge(cheque);
						entityManager.flush();
						msg = SHAConstants.SUCCESS_FLAG;
					}else if(cheque == null){
						ChequeDetails chequeNew = new ChequeDetails();
						chequeNew.setChequeNo(chequeDto.getChequeNo());
						chequeNew.setFileStorage(fileStorage);
						chequeNew.setChequeDate(chequeDto.getChequeDate());
						chequeNew.setBankName(chequeDto.getBankName());
						chequeNew.setBankBranch(chequeDto.getBankBranch());
						chequeNew.setCreatedBy(dto.getUsername());
						chequeNew.setCreatedDate(new Timestamp(System
								.currentTimeMillis()));
						chequeNew.setActivateStatus(SHAConstants.YES_FLAG);
						entityManager.persist(chequeNew);
						entityManager.flush();
						msg = SHAConstants.SUCCESS_FLAG;
					}

				}
			}

			if (dto.getDeletedChequeList() != null
					&& !dto.getDeletedChequeList().isEmpty()) {

				for (ChequeDetailsTableDTO chequeDto : dto.getChequeList()) {
					ChequeDetails cheque = getChequeDetailsByKey(chequeDto
							.getKey());
					if (cheque != null) {
						cheque.setModifiedBy(dto.getUsername());
						cheque.setModifiedDate(new Timestamp(System
								.currentTimeMillis()));
						cheque.setActivateStatus(SHAConstants.N_FLAG);
						entityManager.merge(cheque);
						entityManager.flush();
						msg = SHAConstants.SUCCESS_FLAG;
					}

				}
			}
			return msg;
		} else {
			msg = SHAConstants.FAILURE_FLAG;
			return msg;
		}

	}
	
	@SuppressWarnings("unchecked")
	public PDIntimationPremia getClaimDetail(String claimNo, String year) {

		PDIntimationPremia intimation = null;
		Query findAll = entityManager.createNamedQuery(
				"PDIntimationPremia.findByIntmNo").setParameter("claimNo",
						Long.valueOf(claimNo)).setParameter("year", Long.valueOf(year));
		List<PDIntimationPremia> intimationDetails = (List<PDIntimationPremia>) findAll
				.getResultList();
		if(intimationDetails != null && !intimationDetails.isEmpty()){
			entityManager.refresh(intimationDetails.get(0));
			intimation = intimationDetails.get(0);
			if(intimationDetails.size() > 1){
			intimation.setHasRecords(true);
			}
			else{
			intimation.setHasRecords(false);
			}
		}

		return intimation;
	}
}
