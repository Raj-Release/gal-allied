package com.shaic.reimbursement.fraudidentification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.jetty.http.HttpStatus.Code;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.AbstractMVPPresenter.ViewInterface;
import org.vaadin.addon.cdimvp.CDIEvent;
import org.vaadin.addon.cdimvp.ParameterDTO;
import org.vaadin.dialogs.ConfirmDialog;

import com.shaic.arch.SHAConstants;
import com.shaic.arch.SHAUtils;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.claim.reports.fraudIdentificationReport.FraudIdentificationReportTable;
import com.shaic.domain.HospitalService;
import com.shaic.domain.Hospitals;
import com.shaic.domain.MasterService;
import com.shaic.domain.Policy;
import com.shaic.domain.PolicyService;
import com.shaic.ims.bpm.claim.DBCalculationService;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.v7.data.util.BeanItemContainer;

@ViewInterface(FraudIdentificationView.class)
public class FraudIdentificationPresenter  extends AbstractMVPPresenter<FraudIdentificationView>{

	public static final String LOAD_PARAMETER_VALUES = "Load Parameter Type";
	public static final String SEARCH_FRAUD_IDENTIFICATION = "Search Fraud Identification";
	public static final String SUBMIT_FRAUD_IDENTIFICATION = "Submit Fraud Identification";
	public static final String INSERT_NEW = "Inser New Record";



	@EJB
	private MasterService masterService;
	
	@EJB
	private HospitalService hospitalService;
	
	@EJB
	private PolicyService policyService;
	
	@Inject
	private Instance<FraudIdentificationReportTable> fraudIdentificationReportTable;
	
	private ExcelExport excelExport;
	
	@EJB
	private FraudIdentificationService service;
	@Override
	public void viewEntered() {
		// TODO Auto-generated method stub
		
	}

	public void loadParametertTypeDownValues(
			@Observes @CDIEvent(LOAD_PARAMETER_VALUES) final ParameterDTO parameters) 
		{
		BeanItemContainer<SelectValue> mastersValueContainer = masterService.getParameterTypeFromMaster(SHAConstants.FRAUD_TYPE);
		view.loadParameterDropDownValues(mastersValueContainer);
		
		}
	
	protected void searchByParameter(
			@Observes @CDIEvent(SEARCH_FRAUD_IDENTIFICATION) final ParameterDTO parameters) {
		
		if (parameters.getPrimaryParameter() != null) {
			String parameterType = (String) parameters.getPrimaryParameter().toString();
			List<FraudIdentificationTableDTO> dtoList = new ArrayList<FraudIdentificationTableDTO>();
			List<FraudIdentification> fraudIdentificationObjList = service.getFraudIdentificationObjList(parameterType);
			int rowCount = 1;
			if (fraudIdentificationObjList !=null && !fraudIdentificationObjList.isEmpty()) {
				
				for (FraudIdentification obj : fraudIdentificationObjList) {
					FraudIdentificationTableDTO dto = new FraudIdentificationTableDTO();
					dto.setSerialNumber(String.valueOf(rowCount));
					dto.setParameterType(obj.getValueType());
					dto.setParameterValue(obj.getValue());
					/*if (obj.getActiveStatus() != null && obj.getActiveStatus().equals(String.valueOf(1)))
						dto.setDisable(Boolean.FALSE);
					else if (obj.getActiveStatus() != null && obj.getActiveStatus().equals(String.valueOf(0)))
						dto.setDisable(Boolean.TRUE);*/
					//Always making Disable false
					dto.setDisable(Boolean.FALSE);
					dto.setEffectiveStartDate(obj.getEffectiveFromDate());
					if(obj.getEffectiveToDate() ==null){
					dto.setEffectiveEndDate(obj.getModifiedDate());
					}
					else{
						dto.setEffectiveEndDate(obj.getEffectiveToDate());
					}
					dto.setUserRemarks(obj.getUserRemark());
					dto.setRecipientTo(obj.getToEmail());
					dto.setRecipientCc(obj.getCcEmail());
					if(obj.getValueType().equalsIgnoreCase("Hospital Code (IRDA)")){
						List<Hospitals> HospitalList=hospitalService.searchHospitalByIrdaCode(obj.getValue());
						if(HospitalList!=null){
						for(Hospitals hospitalDetails : HospitalList){
							dto.setHospitalInternalCode(hospitalDetails.getHospitalCode());
							dto.setHospitalName(hospitalDetails.getName());
							dto.setHospitalAddress(hospitalDetails.getAddress());
							dto.setHospitalCity(hospitalDetails.getCity());
						}
					}
					}
					if(obj.getValueType().equalsIgnoreCase("Hospital Code (Internal)")){
						List<Hospitals> HospitalList=hospitalService.searchHospitalById(obj.getValue());
						if(HospitalList!=null){
						for(Hospitals hospitalDetails : HospitalList){
							dto.setHospitalName(hospitalDetails.getName());
							dto.setHospitalAddress(hospitalDetails.getAddress());
							dto.setHospitalCity(hospitalDetails.getCity());
						}
					}
					}
					if(obj.getValueType().equalsIgnoreCase("Policy No")){
						Policy policyobj=policyService.getByPolicyNumber(obj.getValue());
						if(policyobj!=null){
						dto.setProductName(policyobj.getProductName());
						String dateFormat="dd-MM-yyyy hh:mm:ss";
						dto.setPolicyStartDate(SHAUtils.parseDate(policyobj.getPolicyFromDate(),dateFormat));
						dto.setPolicyEndDate(SHAUtils.parseDate(policyobj.getPolicyToDate(),dateFormat));
						}
					}
					if(obj.getValueType().equalsIgnoreCase("Intermediary Code")){
						DBCalculationService dbService = new DBCalculationService();
						String intermediaryName = dbService.getInermediaryDetails(obj.getValue());
						if(intermediaryName!=null){
						dto.setIntermediaryName(intermediaryName);
						}
					}
					dtoList.add(dto);
					
					rowCount++;
				}
				view.generateTableForFraudIdentificationView(dtoList);
			}else {
				view.buildFailureLayout("No Records found",parameterType);
			}

		}else{
			view.buildFailureLayout("Select Parameter Type",null);
		}
	}
	
	
	protected void submitIDA(
			@Observes @CDIEvent(SUBMIT_FRAUD_IDENTIFICATION) final ParameterDTO parameters) {
		List<FraudIdentificationTableDTO> dto = (List<FraudIdentificationTableDTO>) parameters.getPrimaryParameter();
		service.submitTFraudidentification(dto);
		view.buildSuccessLayout();
	}
	
	protected void inserNewRecord(
			@Observes @CDIEvent(INSERT_NEW) final ParameterDTO parameters) {
		if (parameters.getPrimaryParameter() != null) {
			String parameterType = (String) parameters.getPrimaryParameter().toString();
			int rowCount = 1;
					FraudIdentificationTableDTO dto = new FraudIdentificationTableDTO();
					dto.setSerialNumber(String.valueOf(rowCount));
					dto.setParameterType(parameterType);
					dto.setParameterValue("");	
					dto.setDisable(Boolean.FALSE);
					dto.setEdit(Boolean.FALSE);
					rowCount++;
				   view.insertNewRecord(dto);
			}else{
				view.buildFailureLayout("Select Parameter Type",null);
		}
	}

}
