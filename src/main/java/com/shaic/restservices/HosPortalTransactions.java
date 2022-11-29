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
public class HosPortalTransactions {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	DBCalculationService dbCalcService;
	
	@Resource
	private UserTransaction utx;
	
	private static final String NAME_SEPARATOR = "_";
	
	public String doHospitalTransaction(HospitalDocDetails argObj) throws Exception{
		String uploadStatus = "";
		boolean tempStatus = false;
		try{
			
			if(argObj.getDocumentDetails() != null && argObj.getDocumentDetails().size() > 0){
				for(HospitalDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(!StringUtils.isBlank(docObj.getFileContent())){
						String fileName = (StringUtils.isBlank(docObj.getFileName()))?"EMPTY_FILENAME":docObj.getFileName();
						SHAFileUtils.writeStringToFile(fileName, docObj.getFileContent());
						byte[] dbytes = Base64.decodeBase64(docObj.getFileContent());
						String intimationWithUnderScore = argObj.getIntimationNumber();
						intimationWithUnderScore =  intimationWithUnderScore.substring(intimationWithUnderScore.lastIndexOf("/")+1, intimationWithUnderScore.length());
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
						Date date = new Date();
						String currentDate = dateFormat.format(date);//20190701_181026
						String dynamicFileName = "HP"+NAME_SEPARATOR+intimationWithUnderScore+NAME_SEPARATOR+argObj.getRefId()+NAME_SEPARATOR+currentDate+NAME_SEPARATOR+fileName;
						docObj.setDynamicFileName(dynamicFileName);
						WeakHashMap dmsStatus = SHAFileUtils.writeAndSendFileToDMS(dynamicFileName, dbytes);
						boolean isFileUploaded = (boolean) dmsStatus.get("status");
						if(isFileUploaded){
							docObj.setFileUploadInDMS(true);
							docObj.setFileDMSToken(new Long(String.valueOf(dmsStatus.get("fileKey"))));
						}else{
							docObj.setFileUploadInDMS(false);
						}
					}
				}
				for(HospitalDocUploadDetails docObj : argObj.getDocumentDetails()){
					if(docObj.isFileUploadInDMS()){
						tempStatus = true;
					}else{
						tempStatus = false;
						break;
					}
				}
				
				if(tempStatus){
					utx.begin();
					// insert all the details in the table.
					String loginUserId = "ADMIN";
					Date uploadDate = null;
					Intimation intim = getIntimationByNo(argObj.getIntimationNumber());
					HospitalUploadDetails hosDetails = new HospitalUploadDetails();
					if(intim != null){
						hosDetails.setIntimationKey(intim.getKey());
					}else{
						hosDetails.setIntimationKey(Long.parseLong("0"));
					}
					hosDetails.setIntimationNo(argObj.getIntimationNumber());
					hosDetails.setProviderCode(argObj.getProviderCode());
					hosDetails.setClaimedAmount((argObj.getClaimedAmount().equals("") || argObj.getClaimedAmount().equals("null"))?0:Long.parseLong(argObj.getClaimedAmount()));
					hosDetails.setHospitalName(argObj.getHospitalName());
					hosDetails.setFileTypeId(argObj.getFileTypeId());
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
					uploadDate = formatter.parse(argObj.getUploadDate());
					hosDetails.setUploadDate(uploadDate);
					hosDetails.setCreatedBy(loginUserId);
					hosDetails.setCreatedDate(new Date());
					hosDetails.setActiveStatus(1L);
					entityManager.persist(hosDetails);
					
					HospitalUploadDocumentDetails docDetails = null;
					for(HospitalDocUploadDetails reqDocDetails : argObj.getDocumentDetails()){
						docDetails = new HospitalUploadDocumentDetails();
						docDetails.setHospitalUploadDetails(hosDetails);
						docDetails.setDocId(Long.parseLong(reqDocDetails.getDocId()));
						if(intim != null){
							docDetails.setIntimationKey(intim.getKey());
						}else{
							docDetails.setIntimationKey(Long.parseLong("0"));
						}
						docDetails.setIntimationNo(argObj.getIntimationNumber());
						docDetails.setFileTypeId((reqDocDetails.getFileTypeId() == null)?0:Long.parseLong(reqDocDetails.getFileTypeId()));
						docDetails.setFileTypeName(reqDocDetails.getFileTypeName());
//						docDetails.setFileContent(reqDocDetails.getFileContent());
						SHAFileUtils.moveFilesToStatusFolder(true, reqDocDetails.getDynamicFileName());
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
					
					if(intim != null){
						utx.begin();
						System.out.println("Pr invoked");
						Date date=new Date(uploadDate.getTime());
						SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
						String dateText = df2.format(date);
						//System.out.println(dateText);

						// calling the procedure
						Long refId = getPremiaIntimation(intim.getIntimationId()).getGiIntimationId();
						String procStatus = "";
						if(refId != null){
							System.out.println(refId+"---"+intim.getIntimationId()+"---"+hosDetails.getProviderCode()+"---"+hosDetails.getClaimedAmount()+"---"+dateText+"---"+hosDetails.getHospitalName()+"---"+Long.parseLong(hosDetails.getFileTypeId()));
							procStatus = dbCalcService.callHPPushGlx(refId,intim.getIntimationId(),hosDetails.getProviderCode(),hosDetails.getClaimedAmount(),dateText,hosDetails.getHospitalName(),Long.parseLong(hosDetails.getFileTypeId()));
							System.out.println("procStatus : "+procStatus);
						}
						HospitalUploadDetails hosDtls = null;
						hosDtls = getHPUploadDtls(hosDetails.getKey());
						if(!StringUtils.isBlank(procStatus)){
							System.out.println("Procedure returned valid Status");
							if(hosDtls != null){
								hosDtls.setSfBatchStatus(procStatus);
							}
						}else{
							System.out.println("Procedure returned empty Status for intimation "+argObj.getIntimationNumber());
							if(hosDtls != null){
								hosDtls.setSfBatchStatus("E");
							}
						}
						uploadStatus = "Successfully Completed";
						System.out.println("All transactions done properly for I No :"+argObj.getIntimationNumber());
						utx.commit();
					}else{
						System.out.println("Pr not invoked");
					}
				}else{
					for(HospitalDocUploadDetails reqDocDetails : argObj.getDocumentDetails()){
						SHAFileUtils.moveFilesToStatusFolder(false, reqDocDetails.getDynamicFileName());
					}
					/*if(utx != null){
						utx.commit();
					}*/
					uploadStatus = "Some of the file(s) not uploaded in the DMS.";
				}
				
			}
		}catch(Exception exp){
			uploadStatus = exp.getMessage();
			System.out.println("Error occurred while uploading hospital document in transaction.");
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
