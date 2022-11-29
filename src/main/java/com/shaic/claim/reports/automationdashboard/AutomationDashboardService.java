package com.shaic.claim.reports.automationdashboard;

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
import com.shaic.domain.ClaimsReportService;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.server.VaadinSession;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class AutomationDashboardService extends AbstractDAO<Intimation>{
	@PersistenceContext
	protected EntityManager entityManager;
	protected Date date;

	@Resource
	private UserTransaction utx;

	public AutomationDashboardService() {
		// TODO Auto-generated constructor stub
		super();
	}

	public  Page<AutomationDashboardTableDTO> search(AutomationDashboardFormDTO automationFormDTO,String userName, String passWord, DBCalculationService dbService) {



		try
		{
			VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600);
			utx.setTransactionTimeout(3600);	
			utx.begin();

			date = null != automationFormDTO.getDate() ? automationFormDTO.getDate() : null;

			ClaimsReportService.popinReportLog(entityManager, userName, "Automation Dashboard",new Date(),new Date(),SHAConstants.RPT_BEGIN);
			List<AutomationDashboardTableDTO> resultList = dbService.getAutomationDashboardReportList(new java.sql.Date(date.getTime()));

			Page<AutomationDashboardTableDTO> page = new Page<AutomationDashboardTableDTO>();
			page.setPageItems(resultList);
			ClaimsReportService.popinReportLog(entityManager, userName, "Automation Dashboard",new Date(),new Date(),SHAConstants.RPT_SUCCESS);
			utx.commit();
			return page;

		}
		catch (Exception e) 
		{
			ClaimsReportService.popinReportLog(entityManager, userName, "Automation Dashboard",new Date(),new Date(),SHAConstants.RPT_ERROR);
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
	public Class<Intimation> getDTOClass() {
		// TODO Auto-generated method stub
		return Intimation.class;
	}


}
