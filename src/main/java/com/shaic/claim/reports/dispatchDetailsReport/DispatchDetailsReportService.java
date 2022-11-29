package com.shaic.claim.reports.dispatchDetailsReport;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.ReferenceTable;
import com.shaic.ims.bpm.claim.DBCalculationService;


@Stateless
public class DispatchDetailsReportService {

	@PersistenceContext
	protected EntityManager entityManager;

	
	@EJB
	private DBCalculationService dbService;

	public DispatchDetailsReportService() {
		super();
	}

	public Page<DispatchDetailsReportTableDTO> search(DispatchDetailsReportFormDTO searchFormDTO,String userName, String passWord) {

		try
		{			

			String intimationNo = searchFormDTO.getIntimationNo() != null ? searchFormDTO.getIntimationNo() : null;
			SelectValue updateType = searchFormDTO.getUpdateType() != null ? searchFormDTO.getUpdateType() : null;
			String rodNumber = searchFormDTO.getRodNumber() != null ? searchFormDTO.getRodNumber() : null;
			String awsNumber = searchFormDTO.getAwsNumber() != null ? searchFormDTO.getAwsNumber() : null;
			Date fromDate = searchFormDTO.getFromDate() != null ? searchFormDTO.getFromDate() : null;			
			Date todate = searchFormDTO.getToDate() != null ? searchFormDTO.getToDate() : null;
			String batchNumber = null;
			String awbBatchNumber = null;
			Long updateTypeId =  null;
			if(updateType !=null && updateType.getId() !=null && updateType.getId().equals(ReferenceTable.D_BULK_UPLOAD_TYPE_DD)){
				updateTypeId = ReferenceTable.DISPATCH_DETAILS_UPDATE_STATUS;
				batchNumber = searchFormDTO.getBatchNumber() != null ? searchFormDTO.getBatchNumber() : null;
			}else {
				updateTypeId = ReferenceTable.AWSNUMBER_UPDATE_STATUS;
				awbBatchNumber = searchFormDTO.getBatchNumber() != null ? searchFormDTO.getBatchNumber() : null;
			}

			int pageNumber = searchFormDTO.getPageable().getPageNumber();
			ClaimsReportService.popinReportLog(entityManager, userName, "DispatchDetailsReport",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<DispatchDetailsReportTableDTO> resultList = dbService.getDispatchDetailsReportList(updateTypeId,intimationNo,rodNumber,batchNumber,awbBatchNumber,awsNumber,fromDate,todate);
			Page<DispatchDetailsReportTableDTO> page = new Page<DispatchDetailsReportTableDTO>();
			searchFormDTO.getPageable().setPageNumber(pageNumber + 1);
			page.setHasNext(true);
			if (resultList.isEmpty()) {
				searchFormDTO.getPageable().setPageNumber(1);
			}
			page.setPageNumber(pageNumber);
			page.setPageItems(resultList);
			page.setIsDbSearch(false);
			page.setTotalRecords(resultList.size());
			ClaimsReportService.popinReportLog(entityManager, userName, "DispatchDetailsReport",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			return page;

		}
		catch (Exception e) 
		{
			ClaimsReportService.popinReportLog(entityManager, userName, "OpinionValidationReport",new Date(),new Date(),SHAConstants.RPT_ERROR);
			e.printStackTrace();
		}
		return null;	
	}

}
