package com.shaic.claim.fss.filedetailsreport;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.AbstractDAO;
import com.shaic.arch.table.Page;
import com.shaic.domain.fss.FileStorage;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless

@TransactionManagement(TransactionManagementType.BEAN)

public class FileDetailsReportService extends AbstractDAO<FileStorage>{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Resource
	private UserTransaction utx;

	public FileDetailsReportService() {
		super();
	}
	
//	public  Page<FVRAssignmentReportTableDTO> search(FVRAssignmentReportFormDTO fvrFormDTO,String userName, String passWord,UsertoCPUMappingService userCPUMapService) {
	public  Page<FileDetailsReportTableDTO> search(FileDetailsReportFormDTO fvrFormDTO,String userName, String passWord, DBCalculationService dbService) {
		
		try
		{
			
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(380000);
			utx.setTransactionTimeout(360000);	
			utx.begin();
			
			Date fromDate = null != fvrFormDTO.getFromDate() ? fvrFormDTO.getFromDate() : null;
			Date toDate = null != fvrFormDTO.getToDate() ? fvrFormDTO.getToDate() : null;
		
			String statusValue = null != fvrFormDTO.getStatusType()? fvrFormDTO.getStatusType().getValue() : null;
			String status="";
			if(statusValue != null && !statusValue.isEmpty()){
				if(statusValue.equalsIgnoreCase(SHAConstants.FILE_CHECK_IN)){
					status = SHAConstants.YES_FLAG;
				}else if(statusValue.equalsIgnoreCase(SHAConstants.FILE_CHECK_OUT)){
					status = SHAConstants.N_FLAG;
				}
			}
			
			int pageNumber = fvrFormDTO.getPageable().getPageNumber();
	
			
			List<FileDetailsReportTableDTO> resultList = dbService.getFileReportList(new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()),status);
				
//				List<FVRAssignmentReportTableDTO> result = new ArrayList<FVRAssignmentReportTableDTO>();
//				
//				result.addAll(tableDTO);
				
				Page<FileDetailsReportTableDTO> page = new Page<FileDetailsReportTableDTO>();
				fvrFormDTO.getPageable().setPageNumber(pageNumber + 1);
				page.setHasNext(true);
				if (resultList.isEmpty()) 
				{
					fvrFormDTO.getPageable().setPageNumber(1);
				}
				page.setPageNumber(pageNumber);
				page.setPageItems(resultList);
				page.setIsDbSearch(true);
				 utx.commit();
				return page;
				
		} 
		catch (Exception e) {
			
			try {
				utx.rollback();
			} catch (IllegalStateException | SecurityException
					| SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Class<FileStorage> getDTOClass() {
		// TODO Auto-generated method stub
		return FileStorage.class;
	}
	
}
		
			
	
		
		

		
