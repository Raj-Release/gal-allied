package com.shaic.claim.bedphoto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;

import com.shaic.domain.MasHospitalUserMapping;
import com.shaic.domain.MasterService;


@ViewInterface(SearchBedPhoto.class)
public class SearchBedPhotoPreseneter extends AbstractMVPPresenter<SearchBedPhoto>{
	
	public static final String SEARCH_BUTTON_CLICK = "upload_bed_photo";
	
	@EJB
	private SearchUpdateBedPhotoService searchService;
	
	@EJB
	private MasterService masterService;
	
	@SuppressWarnings({ "deprecation" })
	public void handleSearch(@Observes @CDIEvent(SEARCH_BUTTON_CLICK) final ParameterDTO parameters) {
		
		BedPhotoDTO searchFormDTO = (BedPhotoDTO) parameters.getPrimaryParameter();
		
		List<Long> hospitalKey = new ArrayList<Long>();
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		String passWord=(String)parameters.getSecondaryParameter(1, String.class);
		List<MasHospitalUserMapping> userMapedHospitals = masterService.getUserMappedHospitals(userName.toLowerCase());
		for (MasHospitalUserMapping masHospitalUserMapping : userMapedHospitals) {
			if(masHospitalUserMapping.getHospitalKey() != null){
				hospitalKey.add(masHospitalUserMapping.getHospitalKey());
			}
		}
		
			view.list(searchService.search(searchFormDTO,userName,passWord,hospitalKey));
	}

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
