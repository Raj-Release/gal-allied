package com.shaic.restservices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import com.shaic.arch.SHAFileUtils;
import com.shaic.domain.HospitalUploadDetails;
import com.shaic.domain.HospitalUploadDocumentDetails;
import com.shaic.domain.Intimation;
import com.shaic.domain.PremiaIntimationTable;
import com.shaic.ims.bpm.claim.DBCalculationService;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class FVRDocUploadTrancation {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@Resource
	private UserTransaction utx;
	
	private static final String NAME_SEPARATOR = "_";

	
	public String doFVRTransaction(FVRDocDetails argObj) throws Exception{
		String uploadStatus = "";
		boolean tempStatus = false;
		try{
			utx.begin();
			if(argObj.getDocumentDetails() != null && argObj.getDocumentDetails().size() > 0){
				for(FVRDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(!StringUtils.isBlank(docObj.getFileContent())){
						String fileName = (StringUtils.isBlank(docObj.getFileName()))?"EMPTY_FILENAME":docObj.getFileName();
						SHAFileUtils.writeStringToFileFVR(fileName, docObj.getFileContent());
						byte[] dbytes = Base64.decodeBase64(docObj.getFileContent());
						String intimationWithUnderScore = argObj.getIntimationNumber();
						intimationWithUnderScore =  intimationWithUnderScore.substring(intimationWithUnderScore.lastIndexOf("/")+1, intimationWithUnderScore.length());
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
						Date date = new Date();
						String currentDate = dateFormat.format(date);//20190701_181026
						String dynamicFileName = "FVR"+NAME_SEPARATOR+intimationWithUnderScore+NAME_SEPARATOR+argObj.getRefId()+NAME_SEPARATOR+currentDate+NAME_SEPARATOR+fileName;
						docObj.setDynamicFileName(dynamicFileName);
						WeakHashMap dmsStatus = SHAFileUtils.writeAndSendFileToDMSFVR(dynamicFileName, dbytes);
						boolean isFileUploaded = (boolean) dmsStatus.get("status");
						if(isFileUploaded){
							docObj.setFileUploadInDMS(true);
							docObj.setFileDMSToken(new Long(String.valueOf(dmsStatus.get("fileKey"))));
						}else{
							docObj.setFileUploadInDMS(false);
						}
					}
				}
				for(FVRDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(docObj.isFileUploadInDMS()){
						tempStatus = true;
					}else{
						tempStatus = false;
					}
				}
				
				if(tempStatus){
					// insert all the details in the table.
					String loginUserId = "ADMIN";
					Date uploadDate = null;
					Intimation intim = getIntimationByNo(argObj.getIntimationNumber());
					FVRUploadDetails fvrDetails = new FVRUploadDetails();
					if(intim != null){
						fvrDetails.setIntimationKey(intim.getKey());
					}else{
						fvrDetails.setIntimationKey(Long.parseLong("0"));
					}
					fvrDetails.setIntimationNo(argObj.getIntimationNumber());
					fvrDetails.setFvrNo(argObj.getFvrNo());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
					fvrDetails.setFvrRepliedDate(formatter.parse(argObj.getFvrReplyDate()));
					fvrDetails.setFileTypeId(argObj.getFileTypeId());
					uploadDate = formatter.parse(argObj.getUploadDate());
					fvrDetails.setUploadDate(uploadDate);
					fvrDetails.setCreatedBy(loginUserId);
					fvrDetails.setCreatedDate(new Date());
					fvrDetails.setActiveStatus(1L);
					entityManager.persist(fvrDetails);
					
					FVRUploadDocumentDetails docDetails = null;
					for(FVRDocUploadDetails reqDocDetails : argObj.getDocumentDetails()){
						docDetails = new FVRUploadDocumentDetails();
						docDetails.setFvrUploadDetails(fvrDetails);
						docDetails.setDocId(Long.parseLong(reqDocDetails.getDocId()));
						if(intim != null){
							docDetails.setIntimationKey(intim.getKey());
						}else{
							docDetails.setIntimationKey(Long.parseLong("0"));
						}
						docDetails.setIntimationNo(argObj.getIntimationNumber());
						docDetails.setFileTypeId((reqDocDetails.getFileTypeId() == null)?0:Long.parseLong(reqDocDetails.getFileTypeId()));
						docDetails.setFileTypeName(reqDocDetails.getFileTypeName());
					//	docDetails.setFileContent(reqDocDetails.getFileContent());
						SHAFileUtils.moveFilesToStatusFolderFVR(true, reqDocDetails.getDynamicFileName());
						docDetails.setFileName(reqDocDetails.getFileName());
						docDetails.setUploadSource("HP");
						docDetails.setFileServer("EF");
						docDetails.setFileKey((reqDocDetails.getFileDMSToken() == null)?0:reqDocDetails.getFileDMSToken());
						docDetails.setCreatedBy(loginUserId);
						docDetails.setCreatedDate(new Date());
						docDetails.setProcessedFlag("N");
						docDetails.setActiveStatus(1L);
						entityManager.persist(docDetails);
					}
					uploadStatus = "Successfully Uploaded";
					utx.commit();
					
					
				}else{
					for(FVRDocUploadDetails reqDocDetails : argObj.getDocumentDetails()){
						SHAFileUtils.moveFilesToStatusFolderFVR(false, reqDocDetails.getDynamicFileName());
					}
					uploadStatus = "Some of the file(s) not uploaded in the DMS.";
				}
				
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while uploading FVR document in transaction.");
			exp.printStackTrace();
			throw new Exception(uploadStatus);
		}
		return uploadStatus;
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
	
	@SuppressWarnings("unchecked")
	public PremiaIntimationTable getPremiaIntimation(String intimationNo) {
		Query findByKey = entityManager.createNamedQuery("PremiaIntimationTable.findByIntimationNumber").setParameter("intimationNumber", intimationNo);
		List<PremiaIntimationTable> intimationList = (List<PremiaIntimationTable>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			entityManager.refresh(intimationList.get(0));
			return intimationList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HospitalUploadDetails getHPUploadDtls(Long argHPDocKey) {
		Query findByKey = entityManager.createNamedQuery("HospitalUploadDetails.findByKey").setParameter("hpKey", argHPDocKey);
		List<HospitalUploadDetails> hpList = (List<HospitalUploadDetails>) findByKey.getResultList();
		if (!hpList.isEmpty()) {
			entityManager.refresh(hpList.get(0));
			return hpList.get(0);
		}
		return null;
	}
}
