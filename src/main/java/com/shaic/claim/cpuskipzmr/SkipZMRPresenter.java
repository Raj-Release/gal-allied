package com.shaic.claim.cpuskipzmr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.SHAUtils;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.skipZMR.CPUStageMapping;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.vaadin.server.VaadinSession;


@SuppressWarnings("serial")
@ViewInterface(SkipZMRView.class)
public class SkipZMRPresenter extends AbstractMVPPresenter<SkipZMRView> {

	private static final long serialVersionUID = -8024160721276240363L;
	
	@EJB
	private SkipZMRService skipZMRService;
	
	public static final String SUBMIT_SKIP_ZMR = "submit_skip_zmr";
	public static final String SEARCH_SKIP_ZMR = "search_ship_zmr";

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}
	
	protected void searchByCpuCode(
			@Observes @CDIEvent(SEARCH_SKIP_ZMR) final ParameterDTO parameters) {
		if(parameters.getPrimaryParameter() != null) {
			String userId = (String)VaadinSession.getCurrent().getAttribute(BPMClientContext.USERID);	
			Long cpuCode = (Long) parameters.getPrimaryParameter();
			List<TmpCPUCode> masCpuCode = skipZMRService.getMasCpuCode(cpuCode);
			List<CPUStageMapping> stageMappingByCpuCode = skipZMRService.getStageMappingByCpuCode(cpuCode);
			List<SkipZMRListenerTableDTO> dtoList = new ArrayList<SkipZMRListenerTableDTO>();
			if(masCpuCode != null && !masCpuCode.isEmpty()) {
				for (TmpCPUCode tmpCPUCode : masCpuCode) {
					SkipZMRListenerTableDTO dto = new SkipZMRListenerTableDTO();
					dto.setCpuCode(tmpCPUCode.getCpuCode().toString());
					dto.setCpuName(tmpCPUCode.getDescription());
					if(stageMappingByCpuCode != null && stageMappingByCpuCode.get(0) != null) {
						dto.setKey(stageMappingByCpuCode.get(0).getKey());
					}
					dto.setSkipZMRForCashless((stageMappingByCpuCode != null && !stageMappingByCpuCode.isEmpty()) ? SHAUtils.getBooleanValueForFlag(stageMappingByCpuCode.get(0).getCashlessFlag()) : false);
					dto.setSkipZMRForReimbursement((stageMappingByCpuCode != null && !stageMappingByCpuCode.isEmpty()) ? SHAUtils.getBooleanValueForFlag(stageMappingByCpuCode.get(0).getReimbursementFlag()) : false);
					dto.setUserName(userId);
					dtoList.add(dto);
				}
				
				view.generateTableForCpuCode(dtoList);
			} else {
				view.buildFailureLayout("No Records found");
			}
		} else {
			view.buildFailureLayout("Please choose CPU Code. ");
		}
		
		
		
	}
	
	
	protected void submitSkipZMR(
			@Observes @CDIEvent(SUBMIT_SKIP_ZMR) final ParameterDTO parameters) {
		List<SkipZMRListenerTableDTO> dto = (List<SkipZMRListenerTableDTO>) parameters.getPrimaryParameter();
		skipZMRService.submitSkipZMR(dto);
		view.buildSuccessLayout();
	}

	

}
