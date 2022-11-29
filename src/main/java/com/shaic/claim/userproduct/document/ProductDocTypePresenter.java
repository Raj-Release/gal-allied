package com.shaic.claim.userproduct.document;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;

@ViewInterface(ProductAndDocumentTypeView.class)
public class ProductDocTypePresenter extends AbstractMVPPresenter<ProductAndDocumentTypeView> {
	
	public static final String PRODUCT_DOCTYPE_SUBMIT = "product document type submit";
	
	public static final String USER_CPU_SUBMIT = "user cpu submit";
	
	public static final String DOCTOR_SEARCH_CRITERIA = "searchdoctorcriteria";
	
	public static final String SEARCH_DOCTOR = "search doctor";
	
	@EJB
	private UserProductMappingService updateUserProductMapping;
	
	protected void submitProductDocType(
			@Observes @CDIEvent(PRODUCT_DOCTYPE_SUBMIT) final ParameterDTO parameters){
			
		ProductAndDocumentTypeDTO bean = (ProductAndDocumentTypeDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		List<ProductDocTypeDTO> productList = bean.getProductsList();
		
		List<ApplicableCpuDTO> cpuList = bean.getApplicableCpuList();

		//updateUserProductMapping.updateUserProductMapping(bean,userName);
		
		//updateUserProductMapping.updateUserAutoAllocation(bean,userName);
		//updateUserProductMapping.updateUserCpu(bean,userName);
		
		//updateUserProductMapping.updateEmployee(bean);
		
		
		
		view.submitValues();
	}
	
	protected void submituserCpu(
			@Observes @CDIEvent(USER_CPU_SUBMIT) final ParameterDTO parameters) {
		
		ProductAndDocumentTypeDTO bean = (ProductAndDocumentTypeDTO) parameters.getPrimaryParameter();
		
		String userName=(String)parameters.getSecondaryParameter(0, String.class);
		
		updateUserProductMapping.updateUserCpu(bean,userName);
		view.submitValues();
	}
	

	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

}
