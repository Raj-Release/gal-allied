package com.shaic.claim.fvrdetails.view;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.domain.Intimation;
import com.shaic.domain.MasterService;
import com.shaic.domain.ReferenceTable;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(ViewFVRDetailsView.class)
public class ViewFVRDetailsPresenter extends
		AbstractMVPPresenter<ViewFVRDetailsView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SET_REFFER_DATA = "Set_reffer_data";

	public static final String SUBMIT_BUTTON_CLICKED = "The_Submit_Button_Clicked";

	@EJB
	private MasterService masterService;

	@EJB
	private ViewFVRService viewFVRService;

	public void searchClick(
			@Observes @CDIEvent(SET_REFFER_DATA) final ParameterDTO parameters) {

		BeanItemContainer<SelectValue> selectValueContainer = masterService
				.getSelectValueContainer(ReferenceTable.ALLOCATION_TO);
		
		BeanItemContainer<SelectValue> fvrAssignTo = masterService
				.getSelectValueContainer(ReferenceTable.ASSIGN_TO);
		
		BeanItemContainer<SelectValue> fvrPriority = masterService
				.getSelectValueContainer(ReferenceTable.FVR_PRIORITY);
		
		view.list(selectValueContainer,fvrAssignTo,fvrPriority);
	}

	@SuppressWarnings("unused")
	public void submitClicked(
			@Observes @CDIEvent(SUBMIT_BUTTON_CLICKED) final ParameterDTO parameters) {
		ViewFVRFormDTO viewFVRFormDTO = (ViewFVRFormDTO) parameters
				.getPrimaryParameter();
		Object[] secondayParameter = parameters.getSecondaryParameters();
		Intimation intimation = (Intimation) secondayParameter[0];
		Long preauthKey = (Long) secondayParameter[1];
		Long stageKey = (Long) parameters.getSecondaryParameter(2, Long.class);
		Boolean result = viewFVRService.sumbitFVR(viewFVRFormDTO, intimation,preauthKey,stageKey);
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub

	}
}
