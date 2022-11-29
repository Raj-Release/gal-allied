package com.shaic.restservices.crm;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.mail.Address;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shaic.arch.SHAConstants;
import com.shaic.claim.intimation.create.dto.HospitalDto;
import com.shaic.claim.policy.search.ui.PremPolicyDetails;
import com.shaic.domain.CityTownVillage;
import com.shaic.domain.GalaxyCRMIntimation;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Insured;
import com.shaic.domain.IntimationService;
import com.shaic.domain.MasCpuLimit;
import com.shaic.domain.MastersValue;
import com.shaic.domain.OrganaizationUnit;
import com.shaic.domain.Policy;
import com.shaic.domain.Product;
import com.shaic.domain.ReferenceTable;
import com.shaic.domain.State;
import com.shaic.domain.Status;
import com.shaic.domain.TmpCPUCode;
import com.shaic.domain.TmpHospital;
import com.shaic.domain.preauth.PremiaPreviousClaim;
import com.shaic.domain.preauth.Stage;
import com.shaic.ims.bpm.claim.BPMClientContext;
import com.shaic.newcode.wizard.dto.NewIntimationDto;
import com.shaic.restservices.GatewayDBService;
import com.shaic.starfax.simulation.PremiaPullService;

@Path("/v1")
//@Stateless /crm
public class AddIntimationService {

	@EJB
	private PremiaPullService premiaPullService;

	@EJB
	private IntimationService intimationService;

	@Inject
	private GatewayDBService dbService;

	private final Logger log = LoggerFactory.getLogger(AddIntimationService.class);
//	private final String regEx = "[: =]";

	@POST
	@Path("/intimations") /// AddIntimation
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addIntimation(AddIntimationRequest addIntimationRequest,
			@HeaderParam("authorization") String authString)
			throws JsonGenerationException, JsonMappingException, IOException {
		AddIntimationResponse addIntimationResponse = new AddIntimationResponse();
		List<Error> errors = new ArrayList<Error>();

		Date admitted = null;
		Date createdOn = null;
		Date dischargeDate = null;
		String returnMsg = null;
		boolean hasError = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		// Replace all Bearer word to empty
//		authString = authString.replaceAll("Bearer", "");
//		authString = authString.replaceAll("bearer", "");
//		authString = authString.replaceAll(regEx, "");
		
		  ObjectMapper mapper = new ObjectMapper(); log.info("AI REQ : " +
		  mapper.writeValueAsString(addIntimationRequest));
		 

		if (!StringUtils.isBlank(authString)) {
			String[] arrayOne = authString.split(" ");
			String decodedValue = new String(Base64.decodeBase64(arrayOne[1].getBytes()));
			// if((authString != null) &&
			// (authString.equals(BPMClientContext.CRM_WS_ADD_INTIMATION_KEY))) {
			if (decodedValue.contains(":")) {
				String[] arrayTwo = decodedValue.split(":");
				addIntimationRequest.setUsername(arrayTwo[0]);
				addIntimationRequest.setPassword(arrayTwo[1]);
				if (dbService.validateRequest(addIntimationRequest)) {
					log.debug(addIntimationRequest.toString());

					if (!((addIntimationRequest.getInsuredId() != null)
							&& (!addIntimationRequest.getInsuredId().trim().isEmpty())
							&& (addIntimationRequest.getInsuredId().length() <= 20))) {

						hasError = true;
						Error error = new Error();
						error.setError("InsuredId should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getIntimationMode() != null)
							&& (!addIntimationRequest.getIntimationMode().trim().isEmpty())
							&& (addIntimationRequest.getIntimationMode().length() <= 15))) {
						hasError = true;
						Error error = new Error();
						error.setError("IntimationMode should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getIntimatedBy() != null)
							&& (!addIntimationRequest.getIntimatedBy().trim().isEmpty())
							&& (addIntimationRequest.getIntimatedBy().length() <= 50))) {
						hasError = true;
						Error error = new Error();
						error.setError("IntimatedBy should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getIntimatorName() != null)
							&& (!addIntimationRequest.getIntimatorName().trim().isEmpty())
							&& (addIntimationRequest.getIntimatorName().length() <= 100))) {
						hasError = true;
						Error error = new Error();
						error.setError("IntimatorName should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getPolicyNumber() != null)
							&& (!addIntimationRequest.getPolicyNumber().trim().isEmpty())
							&& (addIntimationRequest.getPolicyNumber().length() <= 60))) {
						hasError = true;
						Error error = new Error();
						error.setError("PolicyNumber should not be blank and should be within the field length");
						errors.add(error);
					}
					/*
					 * if(!((addIntimationRequest.getIntimationNo() != null) &&
					 * (!addIntimationRequest.getIntimationNo().trim().isEmpty()) &&
					 * (addIntimationRequest.getIntimationNo().length() <= 60))) { hasError = true;
					 * Error error = new Error(); error.
					 * setError("IntimationNo should not be blank and should be within the field length"
					 * ); errors.add(error); }
					 */
					if (StringUtils.isBlank(addIntimationRequest.getHospCode())
							&& (StringUtils.isBlank(addIntimationRequest.getHospName())
									|| StringUtils.isBlank(addIntimationRequest.getHospAddress()))) {
						hasError = true;
						Error error = new Error();
						error.setError(
								"HospCode or (Hospital Name, Address, State, City, Contact No and Fax No) has to be provided)");
						errors.add(error);
					}
					/*
					 * if(!((addIntimationRequest.getHospCode() != null) &&
					 * (!addIntimationRequest.getHospCode().trim().isEmpty()) &&
					 * (addIntimationRequest.getHospCode().length() <= 20))) { hasError = true;
					 * Error error = new Error(); error.
					 * setError("HospCode should not be blank and should be within the field length"
					 * ); errors.add(error); }
					 */
					if (!((addIntimationRequest.getAdmissionType() != null)
							&& (!addIntimationRequest.getAdmissionType().trim().isEmpty())
							&& (addIntimationRequest.getAdmissionType().length() <= 50))) {
						hasError = true;
						Error error = new Error();
						error.setError("AdmissionType should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getManagementType() != null)
							&& (!addIntimationRequest.getManagementType().trim().isEmpty())
							&& (addIntimationRequest.getManagementType().length() <= 50))) {
						hasError = true;
						Error error = new Error();
						error.setError("ManagementType should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getReasonForAdmission() != null)
							&& (!addIntimationRequest.getReasonForAdmission().trim().isEmpty())
							&& (addIntimationRequest.getReasonForAdmission().length() <= 200))) {
						hasError = true;
						Error error = new Error();
						error.setError("ReasonForAdmission should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getPolicyYr() != null)
							&& (!addIntimationRequest.getPolicyYr().trim().isEmpty())
							&& (addIntimationRequest.getPolicyYr().length() <= 100))) {
						hasError = true;
						Error error = new Error();
						error.setError("PolicyYr should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getProdCode() != null)
							&& (!addIntimationRequest.getProdCode().trim().isEmpty())
							&& (addIntimationRequest.getProdCode().length() <= 12))) {
						hasError = true;
						Error error = new Error();
						error.setError("ProdCode should not be blank and should be within the field length");
						errors.add(error);
					}
					if (!((addIntimationRequest.getIntimatorContactNo() != null)
							&& (addIntimationRequest.getIntimatorContactNo().length() <= 15))) {
						hasError = true;
						Error error = new Error();
						error.setError("Intimator Contact No should be within the field length");
						errors.add(error);
					}

					if ((addIntimationRequest.getAdmitted() != null)
							&& (!addIntimationRequest.getAdmitted().trim().isEmpty())) {
						try {
							admitted = sdf1.parse(addIntimationRequest.getAdmitted());
						} catch (ParseException e) {
							hasError = true;
							Error error = new Error();
							error.setError(
									"Admitted is invalid date format. Please use valid date format as 'DD/MM/YYYY'");
							errors.add(error);
						}
					} else {
						hasError = true;
						Error error = new Error();
						error.setError("Admitted should not be blank and should be valid date format as 'DD/MM/YYYY'");
						errors.add(error);
					}

					/*
					 * if((addIntimationRequest.getCreatedOn() != null) &&
					 * (!addIntimationRequest.getCreatedOn().trim().isEmpty())) { try { createdOn =
					 * sdf1.parse(addIntimationRequest.getCreatedOn()); } catch (ParseException e) {
					 * hasError = true; Error error = new Error(); error.
					 * setError("Created On is invalid date format. Please use valid date format as 'DD/MM/YYYY'"
					 * ); errors.add(error); } }
					 */
					if ((addIntimationRequest.getDischargeDate() != null)
							&& (!addIntimationRequest.getDischargeDate().trim().isEmpty())) {
						try {
							dischargeDate = sdf1.parse(addIntimationRequest.getDischargeDate());
						} catch (ParseException e) {
							hasError = true;
							Error error = new Error();
							error.setError(
									"Discharge Date is invalid date format. Please use valid date format as 'DD/MM/YYYY'");
							errors.add(error);
						}
					}
					/*
					 * if(!((addIntimationRequest.getRiskID() != null) &&
					 * (!addIntimationRequest.getRiskID().trim().isEmpty()) &&
					 * (addIntimationRequest.getRiskID().length() <= 9))) { hasError = true; Error
					 * error = new Error(); error.
					 * setError("RiskID should not be blank and should be within the field length");
					 * errors.add(error); }
					 */
					if (!((addIntimationRequest.getPioCode() != null)
							&& (!addIntimationRequest.getPioCode().trim().isEmpty())
							&& (addIntimationRequest.getPioCode().length() <= 20))) {
						hasError = true;
						Error error = new Error();
						error.setError("PIO Code should not be blank and should be within the field length");
						errors.add(error);
					}
				} else {
					hasError = true;
					Error error = new Error();
					error.setError("Invalid Username/Password");
					errors.add(error);
				}
			} else {
				hasError = true;
				Error error = new Error();
				error.setError("Authorization is invalid");
				errors.add(error);
			}
		} else {
			hasError = true;
			Error error = new Error();
			error.setError("Authorization is invalid");
			errors.add(error);
		}

		// Error availability checking
		addIntimationResponse.setErrorYN(hasError ? "Y" : "N");
		addIntimationResponse.setErrors(errors);
		if (hasError) {
			addIntimationResponse.setResultMsg("Error occurred while processing claim data");
		} else {
//			StageIntimation stageIntimation = StageIntimationMapper.getInstance().getStageIntimation(addIntimationRequest);
//			stageIntimation.setAdmitted(admitted);
//			stageIntimation.setCreatedOn(createdOn);
//			stageIntimation.setDischargeDate(dischargeDate);

//			  "insuredName": "164474324", 
//			  "intimationMode": "Phone",
//			  "intimatedBy": "HOSPITAL",
//			  "intimatorName": "Steve Jobs",
//			  "intimatorContactNo": "1234567890",
//			  "polNo": "P/700002/01/2016/023680",
//			  "intimationNo": "CLI/2018/111117/0460728",
//			  "hospCode": "HOS-5857",
//			  "hospitalTypeYn": "Y",
//			  "hospComments": "",
//			  "admissionType": "Current",
//			  "managementType": "Surgical",
//			  "reasonForAdmission": "Cold & Fever",
//			  "admitted": "01/03/2018",
//			  "roomCategory": "",
//			  "savedType": "PROCESSED",
//			  "clmProcDivn": "950001",
//			  "patientNameYn": "Y",
//			  "inpatientNo": "",
//			  "createdBy": "1",
//			  "createdOn": "02/03/2018",
//			  "attMobNo": "1234567890",
//			  "policyYr": "3rd Year POLICY",
//			  "accDeathYn": "N",
//			  "hospRequYn": "Y",
//			  "paCategory": "H",
//			  "paPatientName": "",
//			  "paParentName": "",
//			  "paParentDob": "",
//			  "paParentAge": "10",
//			  "prodCode": "MED-PRD-034",
//			  "paPatientDob": "",
//			  "paPatientAge": "20",
//			  "suspiciousMultiple": "0",
//			  "admittedTime": "70:15",
//			  "dischargeDate": "03/03/2018",
//			  "dischargeTime": "19:30",
//			  "riskID": "164474324"

			try {
//				dd/mm/yyyy HH:mm:ss
				// CRM Intimation change Starts
				/*
				 * String statusMsg =
				 * checkIntimationAlreadyCreated(addIntimationRequest.getAdmitted(),
				 * addIntimationRequest.getHospCode(), addIntimationRequest.getPolicyNumber(),
				 * addIntimationRequest.getInsuredId());
				 */
				// CRM Intimation change Ends
				String statusMsg ="DummyHospital";
				if ((StringUtils.isBlank(statusMsg) || statusMsg.equals("DummyHospital"))
						&& !statusMsg.startsWith("Same Details")) {
//					Boolean callWebService = callWebService(addIntimationRequest.getProdCode(), addIntimationRequest.getPolicyNumber(), addIntimationRequest.getInsuredId(),premiaPullService);
					Boolean callWebService=true;
				
					if (callWebService) {
						// CRM Intimation change Starts
						/*
						 * GalaxyIntimation glxIntimation = new GalaxyIntimation();
						 * glxIntimation.setIntimaterName(addIntimationRequest.getIntimatorName());
						 * glxIntimation.setCallerMobileNumber(addIntimationRequest.
						 * getIntimatorContactNo());
						 * glxIntimation.setHospitalComments(addIntimationRequest.getHospComments());
						 * glxIntimation.setAdmissionReason(addIntimationRequest.getReasonForAdmission()
						 * ); // Adding the time value in the admission Date.
						 * if(!StringUtils.isBlank(addIntimationRequest.getAdmitted()) &&
						 * !StringUtils.isBlank(addIntimationRequest.getAdmittedTime())){ String
						 * admittedDate = addIntimationRequest.getAdmitted(); String admittedTime =
						 * addIntimationRequest.getAdmittedTime()+":00"; String cAdmittedDate =
						 * admittedDate+" "+admittedTime; admitted = sdf2.parse(cAdmittedDate);
						 * glxIntimation.setAdmissionDate(admitted); }else{
						 * glxIntimation.setAdmissionDate(admitted); }
						 * glxIntimation.setInpatientNumber(addIntimationRequest.getInpatientNo());
						 * glxIntimation.setCreatedBy(addIntimationRequest.getCreatedBy());
						 * glxIntimation.setCreatedDate(createdOn);
						 * glxIntimation.setAttendersMobileNumber(addIntimationRequest.getAttMobNo());
						 * glxIntimation.setPolicyYear(addIntimationRequest.getPolicyYr());
						 * glxIntimation.setIncidenceFlag(addIntimationRequest.getAccDeathYn());
						 * glxIntimation.setHospitalReqFlag(addIntimationRequest.getHospRequYn());
						 * glxIntimation.setPaCategory(addIntimationRequest.getPaCategory());
						 * glxIntimation.setPaPatientName(addIntimationRequest.getPaPatientName()); //
						 * glxIntimation.setPaParentName(addIntimationRequest.getPaParentName());
						 * glxIntimation.setPaStudentName(addIntimationRequest.getPaParentName());
						 * if(addIntimationRequest.getPaParentDob() != null && !
						 * addIntimationRequest.getPaParentDob().isEmpty()){ Date parentDOB =
						 * sdf1.parse(addIntimationRequest.getPaParentDob());
						 * glxIntimation.setPaParentDOB(parentDOB); }
						 * if(addIntimationRequest.getPaPatientDob() != null && !
						 * addIntimationRequest.getPaPatientDob().isEmpty()){ Date patientDOB =
						 * sdf1.parse(addIntimationRequest.getPaPatientDob());
						 * glxIntimation.setPaStudentDOB(patientDOB); //patientDOB }
						 * 
						 * if(addIntimationRequest.getPaParentAge() != null && !
						 * addIntimationRequest.getPaParentAge().isEmpty()){
						 * glxIntimation.setPaParentAge(Double.parseDouble(addIntimationRequest.
						 * getPaParentAge())); } if(addIntimationRequest.getPaPatientAge() != null && !
						 * addIntimationRequest.getPaPatientAge().isEmpty()){
						 * glxIntimation.setPaStudentAge(Double.parseDouble(addIntimationRequest.
						 * getPaPatientAge())); }
						 * 
						 * // Adding the time value in the discharge Date.
						 * if(!StringUtils.isBlank(addIntimationRequest.getDischargeDate()) &&
						 * !StringUtils.isBlank(addIntimationRequest.getDischargeTime())){ String
						 * dizchargeDate = addIntimationRequest.getDischargeDate(); String dizchargeTime
						 * = addIntimationRequest.getDischargeTime()+":00"; String cDischargeDate =
						 * dizchargeDate+" "+dizchargeTime; dischargeDate = sdf2.parse(cDischargeDate);
						 * glxIntimation.setDateOfDischarge(dischargeDate); }else{
						 * glxIntimation.setDateOfDischarge(dischargeDate); } // Mas Columns
						 * MastersValue intimationMode =
						 * premiaPullService.getMastersValue(addIntimationRequest.getIntimationMode(),
						 * "INTIMODE"); glxIntimation.setIntimationMode(intimationMode); MastersValue
						 * intimatedBy =
						 * premiaPullService.getMastersValue(addIntimationRequest.getIntimatedBy(),
						 * "INTITYPE"); // INTISRCE, INTITYPE glxIntimation.setIntimatedBy(intimatedBy);
						 * MastersValue admissionType =
						 * premiaPullService.getMastersValue(addIntimationRequest.getAdmissionType(),
						 * "ADMTYPE"); glxIntimation.setAdmissionType(admissionType); MastersValue
						 * managementType =
						 * premiaPullService.getMastersValue(addIntimationRequest.getManagementType(),
						 * "TREATMGMT"); glxIntimation.setManagementType(managementType); MastersValue
						 * roomCategory =
						 * premiaPullService.getMastersValue(addIntimationRequest.getRoomCategory(),
						 * "ROOMCTGY"); glxIntimation.setRoomCategory(roomCategory); Hospitals hospital
						 * = null; TmpHospital tmpHos = null;
						 * if(!StringUtils.isBlank(addIntimationRequest.getHospCode())){ hospital =
						 * premiaPullService.getHospitalByHospNo(addIntimationRequest.getHospCode());
						 * if(hospital != null) { glxIntimation.setHospital(hospital.getKey()); //
						 * glxIntimation.setInsuredNumber(Long.valueOf(addIntimationRequest.getRiskID())
						 * );
						 * glxIntimation.setInsuredNumber(Long.valueOf(addIntimationRequest.getInsuredId
						 * ())); glxIntimation.setHospitalCode(addIntimationRequest.getHospCode());
						 * glxIntimation.setDummy("N"); }else{ hasError = true; Error error = new
						 * Error(); error.setError("Hospital Code not available"); errors.add(error); }
						 * }else{ tmpHos = new TmpHospital();
						 * tmpHos.setHospitalName(addIntimationRequest.getHospName());
						 * tmpHos.setAddress(addIntimationRequest.getHospAddress());
						 * if(!StringUtils.isBlank(addIntimationRequest.getHospState())){
						 * tmpHos.setWsState(addIntimationRequest.getHospState());
						 * tmpHos.setState(addIntimationRequest.getHospState()); }else{
						 * tmpHos.setWsState(null); tmpHos.setState(""); } State state =
						 * premiaPullService.getState(addIntimationRequest.getHospState()); if(state !=
						 * null){ tmpHos.setStateId(state.getKey()); }else{ tmpHos.setStateId(0L); }
						 * if(!StringUtils.isBlank(addIntimationRequest.getHospCity())){
						 * tmpHos.setWsCity(addIntimationRequest.getHospCity());
						 * tmpHos.setCity(addIntimationRequest.getHospCity()); }else{
						 * tmpHos.setWsCity(null); tmpHos.setCity(""); } CityTownVillage city =
						 * premiaPullService.getCity(addIntimationRequest.getHospCity()); if(city !=
						 * null){ tmpHos.setCityId(city.getKey()); }else{ tmpHos.setCityId(0L); }
						 * tmpHos.setContactNumber(addIntimationRequest.getHospContactNo());
						 * tmpHos.setFaxNumber(addIntimationRequest.getHospFaxNo());
						 * premiaPullService.insertTmpHospital(tmpHos);
						 * glxIntimation.setHospital(tmpHos.getKey());
						 * 
						 * //
						 * glxIntimation.setInsuredNumber(Long.valueOf(addIntimationRequest.getRiskID())
						 * );
						 * glxIntimation.setInsuredNumber(Long.valueOf(addIntimationRequest.getInsuredId
						 * ())); glxIntimation.setDummy("Y"); }
						 * 
						 * // Network // Non-Network MastersValue hospitalType = new MastersValue();
						 * if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(
						 * addIntimationRequest.getHospitalTypeYn())) {
						 * hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)
						 * ; } else if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(
						 * addIntimationRequest.getHospitalTypeYn())) {
						 * hospitalType.setKey(ReferenceTable.
						 * PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL); } if(hospital != null){
						 * if(hospital.getHospitalType() != null){
						 * if(hospital.getHospitalType().getValue().equals("Network")){
						 * hospitalType.setKey(ReferenceTable.PREMIA_INTIMTION_PROCESS_NETWORK_HOSPITAL)
						 * ; glxIntimation.setHospitalType(hospitalType); } else
						 * if(hospital.getHospitalType().getValue().equals("Non-Network")){
						 * hospitalType.setKey(ReferenceTable.
						 * PREMIA_INTIMTION_PROCESS_NON_NETWORK_HOSPITAL);
						 * glxIntimation.setHospitalType(hospitalType); } } else { hasError = true;
						 * Error error = new Error(); error.setError("Hospital Type is null in Master");
						 * errors.add(error); glxIntimation.setHospitalType(null); } }else{
						 * if(!StringUtils.isBlank(addIntimationRequest.getHospitalType())){
						 * MastersValue hospType = new MastersValue();
						 * if(addIntimationRequest.getHospitalType().equals("Network")){
						 * hospType.setKey(121L); hospType.setValue("Network"); }else
						 * if(addIntimationRequest.getHospitalType().equals("Non-Network")){
						 * hospType.setKey(122L); hospType.setValue("Non-Network"); }
						 * glxIntimation.setHospitalType(hospType); }else{
						 * glxIntimation.setHospitalType(null); } } if(hasError) {
						 * addIntimationResponse.
						 * setResultMsg("Error occurred while processing claim data"); } Policy policy =
						 * null; if(null != addIntimationRequest.getPolicyNumber() &&
						 * !("").equalsIgnoreCase(addIntimationRequest.getPolicyNumber())){ policy =
						 * premiaPullService.getPolicyByPolicyNubember(addIntimationRequest.
						 * getPolicyNumber()); } glxIntimation.setPolicy(policy);
						 * glxIntimation.setOfficeCode(policy.getHomeOfficeCode()); Insured insured =
						 * null; if(null != addIntimationRequest.getInsuredId() &&
						 * !("").equalsIgnoreCase(addIntimationRequest.getInsuredId())){ insured =
						 * premiaPullService.getInsuredByPolicyAndInsuredName(addIntimationRequest.
						 * getPolicyNumber(),
						 * addIntimationRequest.getInsuredId(),addIntimationRequest.getInsuredId()); }
						 * if(insured == null){ insured =
						 * premiaPullService.getInsuredByPolicyAndInsuredNameForDefault(
						 * addIntimationRequest.getPolicyNumber(), addIntimationRequest.getInsuredId());
						 * } glxIntimation.setInsured(insured); setCpuCode(addIntimationRequest, policy,
						 * glxIntimation, hospital);
						 * 
						 * Boolean premiaIntimation =
						 * premiaPullService.isPremiaIntimation(policy.getPolicyNumber());
						 * if(premiaIntimation != null && premiaIntimation){
						 * glxIntimation.setApplicationFlag("P"); }else{
						 * glxIntimation.setApplicationFlag("G"); }
						 * 
						 * if((glxIntimation.getIncidenceFlag() == null) ||
						 * (glxIntimation.getIncidenceFlag() != null &&
						 * glxIntimation.getIncidenceFlag().isEmpty())){
						 * glxIntimation.setProcessClaimType("H"); }else{
						 * glxIntimation.setProcessClaimType("P"); }
						 * 
						 * Status status = null; if(glxIntimation.getDummy().equals("Y")){ status = new
						 * Status(); status.setKey(ReferenceTable.INTIMATION_PENDING_STATUS_KEY); }else{
						 * status = new Status();
						 * status.setKey(ReferenceTable.INTIMATION_SUBMIT_STATUS_KEY); } Stage stage =
						 * new Stage(); stage.setKey(ReferenceTable.INTIMATION_STAGE_KEY);
						 * glxIntimation.setStage(stage); glxIntimation.setStatus(status);
						 * glxIntimation.setActiveStatus("1");
						 * 
						 * glxIntimation.setInsuredPatName(addIntimationRequest.getInsuredPatientName())
						 * ; glxIntimation.setInsuredEmail(addIntimationRequest.getInsuredEmail());
						 * glxIntimation.setDoctorName(addIntimationRequest.getHospitalDoctorName());
						 * glxIntimation.setComments(addIntimationRequest.getComments());
						 * glxIntimation.setHospitalComments(addIntimationRequest.getSuspiciousComments(
						 * )); if(StringUtils.isBlank(addIntimationRequest.getPolicySource())){
						 * glxIntimation.setPolicySource("P"); }else{
						 * glxIntimation.setPolicySource(addIntimationRequest.getPolicySource().
						 * toUpperCase()); }
						 * 
						 * 
						 * if(insured != null){ glxIntimation.setPolicyYear(insured.getPolicyYear()); }
						 * entityManager.persist(glxIntimation); entityManager.flush();
						 * entityManager.clear(); GalaxyIntimation insertIntimationFromwebService =
						 * premiaPullService.insertIntimationFromwebService(glxIntimation);
						 * if(glxIntimation.getDummy().equals("Y")){ NewIntimationDto newIntimationDto =
						 * new NewIntimationDto(); HospitalDto hospitalDto = new HospitalDto();
						 * hospitalDto.setNotRegisteredHospitals(tmpHos);
						 * newIntimationDto.setHospitalDto(hospitalDto);
						 * newIntimationDto.setCreatedBy(insertIntimationFromwebService.getCreatedBy());
						 * intimationService.insertDummyHospitalToPMS(newIntimationDto,
						 * insertIntimationFromwebService); }
						 * if(insertIntimationFromwebService.getIntimationId() != null){
						 * addIntimationResponse.setIntimationNo(insertIntimationFromwebService.
						 * getIntimationId());
						 * addIntimationResponse.setResultMsg("Claim data added successfully"); }
						 */
						Hospitals hospital = premiaPullService.getHospitalByHospNo(addIntimationRequest.getHospCode());
						if(hospital== null) {
							log.info("Invalid Hospital Code");
						}
						
						Policy policy = premiaPullService.getPolicyByPolicyNubember(addIntimationRequest.getPolicyNumber());
						if(policy== null) {
							log.info("Policy Data not available in Galaxy Claim");
						}
						GalaxyCRMIntimation crmIntimation = new GalaxyCRMIntimation();
						crmIntimation.setHospitalType(addIntimationRequest.getHospitalType());
						crmIntimation.setHospitalName(addIntimationRequest.getHospCode());
						crmIntimation.setIntimatedBy(addIntimationRequest.getIntimatedBy());
						crmIntimation.setIntimationMode(addIntimationRequest.getIntimationMode());
						crmIntimation.setIntimatorName(addIntimationRequest.getIntimatorName());
						crmIntimation.setAdmissionType(addIntimationRequest.getAdmissionType());
						crmIntimation.setReasonForAdmission(addIntimationRequest.getReasonForAdmission());
						
						if(!StringUtils.isBlank(addIntimationRequest.getAdmitted()) &&
								!StringUtils.isBlank(addIntimationRequest.getAdmittedTime())){ 
							String admittedDate = addIntimationRequest.getAdmitted(); 
							String admittedTime = addIntimationRequest.getAdmittedTime()+":00"; 
							String cAdmittedDate = admittedDate+" "+admittedTime; 
							admitted = sdf2.parse(cAdmittedDate);
							crmIntimation.setAdmittedDate(admitted); 
						}else{
							crmIntimation.setAdmittedDate(admitted); 
						}
						if(!(StringUtils.isBlank(addIntimationRequest.getAdmittedTime()))){
							crmIntimation.setAdmittedTime(addIntimationRequest.getAdmittedTime()+":00");
						}
						if(!StringUtils.isBlank(addIntimationRequest.getDischargeDate()) &&
								!StringUtils.isBlank(addIntimationRequest.getDischargeTime())){ 
							String dischargedDate = addIntimationRequest.getDischargeDate(); 
							String dischargedTime = addIntimationRequest.getDischargeTime()+":00"; 
							String cdischargedDate = dischargedDate+" "+dischargedTime; 
							dischargeDate = sdf2.parse(cdischargedDate);
							crmIntimation.setDischargeDate(dischargeDate); 
						}else{
							crmIntimation.setDischargeDate(dischargeDate); 
						}
						
						if(!(StringUtils.isBlank(addIntimationRequest.getDischargeTime()))){
							crmIntimation.setDischargeTime(addIntimationRequest.getDischargeTime()+":00");
						}
						crmIntimation.setManagementType(addIntimationRequest.getManagementType());
						crmIntimation.setRoomCategory(addIntimationRequest.getRoomCategory());
						crmIntimation.setInpatientNo(addIntimationRequest.getInpatientNo());
						crmIntimation.setComments(addIntimationRequest.getComments());
						crmIntimation.setHospitalDoctorName(addIntimationRequest.getHospitalDoctorName());
						crmIntimation.setIntimatorContactNo(addIntimationRequest.getIntimatorContactNo());
						crmIntimation.setAttenderMobileNo(addIntimationRequest.getAttMobNo());
						crmIntimation.setPolicyNo(addIntimationRequest.getPolicyNumber());
						crmIntimation.setPolicyYear(addIntimationRequest.getPolicyYr());
						crmIntimation.setHospitalRequired(addIntimationRequest.getHospRequYn());
						crmIntimation.setPaCategory(addIntimationRequest.getPaCategory());
						crmIntimation.setInsuredId(null);
						crmIntimation.setInsuredNumber(addIntimationRequest.getInsuredId());
						crmIntimation.setHospitalCode(addIntimationRequest.getHospCode());
						crmIntimation.setHospitalAddress(addIntimationRequest.getHospAddress());
						crmIntimation.setHospitalCity(addIntimationRequest.getHospCity());
						crmIntimation.setHospitalState(addIntimationRequest.getHospState());
						crmIntimation.setHospitalContactNo(addIntimationRequest.getHospContactNo());
						crmIntimation.setHospitalFaxNo(addIntimationRequest.getHospFaxNo());
						crmIntimation.setCreatedBy(addIntimationRequest.getCreatedBy());
						crmIntimation.setCreatedDate(new Date());
						crmIntimation.setAccDeath(addIntimationRequest.getAccDeathYn());
						crmIntimation.setProdCode(addIntimationRequest.getProdCode());
						crmIntimation.setInsuredPatientName(addIntimationRequest.getInsuredPatientName());
						crmIntimation.setInsuredEmail(addIntimationRequest.getInsuredEmail());
						crmIntimation.setHospitalType(addIntimationRequest.getHospitalType());
						crmIntimation.setSuspiciousComments(addIntimationRequest.getSuspiciousComments());
						crmIntimation.setPolicySource(addIntimationRequest.getPolicySource());
						crmIntimation.setPioCode(addIntimationRequest.getPioCode());
//						crmIntimation.setCpuCode(addIntimationRequest.getc);
						setCRMCpuCode(addIntimationRequest,policy,crmIntimation,hospital);
						Boolean isPremia=premiaPullService.isPremiaIntimation(addIntimationRequest.getPolicyNumber());
						if(isPremia && isPremia!=null) {
							crmIntimation.setIntmFlag("P");							
						}else {
							crmIntimation.setIntmFlag("G");
						}
						if(policy!=null) {
							crmIntimation.setPolicyKey(policy.getKey());
						}
						crmIntimation.setSource(addIntimationRequest.getUsername());
						GalaxyCRMIntimation intimation=premiaPullService.saveCRMIntimation(crmIntimation);
						addIntimationResponse.setErrorYN("N");
						addIntimationResponse.setResultMsg("Claim data added successfully"); 
						addIntimationResponse.setIntimationNo(intimation.getIntimationNumber());
					} else {
						addIntimationResponse.setResultMsg("Intimation is not created due to some error");
						addIntimationResponse.setErrorYN("Y");
					}
				} else {
					addIntimationResponse.setResultMsg(statusMsg);
					addIntimationResponse.setErrorYN("Y");
				}
			} catch (Exception exp) {
				exp.printStackTrace();
				addIntimationResponse.setErrorYN("Y");
				Error error = new Error();
				error.setError(exp.getMessage());
				errors.add(error);
				addIntimationResponse.setErrors(errors);
				addIntimationResponse.setResultMsg("Error occurred while creating intimation");
			}
		}
		// Convert object to JSON string
		Gson gson = new Gson();
		returnMsg = gson.toJson(addIntimationResponse);

		return Response.ok(returnMsg, MediaType.APPLICATION_JSON).build();
	}

	/*
	 * @SuppressWarnings("unchecked") private MastersValue getMastersValue(String
	 * masterValue, String masterCode) { Query query =
	 * entityManager.createNamedQuery(
	 * "MastersValue.findByMasterListKeyByCodeAndValue"); query =
	 * query.setParameter("parentKey", masterCode); query =
	 * query.setParameter("value", masterValue);
	 * 
	 * List<MastersValue> masterValueList = new ArrayList<MastersValue>();
	 * masterValueList = query.getResultList(); if(null != masterValueList &&
	 * !masterValueList.isEmpty()) { return masterValueList.get(0); } return null; }
	 */

	/*
	 * @SuppressWarnings("unchecked") public Hospitals getHospitalByHospNo(String
	 * hospCode) { Query query =
	 * entityManager.createNamedQuery("Hospitals.findByCode"); query =
	 * query.setParameter("hospitalCode", hospCode.toUpperCase()); List<Hospitals>
	 * hospitalList = query.getResultList(); if(null != hospitalList &&
	 * !hospitalList.isEmpty()) { return hospitalList.get(0); } return null; }
	 */

	public Boolean callWebService(String productCode, String policyNumber, String riskId,
			PremiaPullService premiaPullService) {

		PremPolicyDetails policyDetails = null;
		Boolean isIntegratedPolicy = false;
		String policyServiceType = "OTHERS";
		if (productCode != null) {
			Product productByProductCode = premiaPullService.getProductByProductCode(productCode);
			if (productByProductCode != null && productByProductCode.getProductService() != null) {
				policyServiceType = productByProductCode.getProductService();
			}
		}
		if (policyServiceType.equalsIgnoreCase(SHAConstants.GMC_POL_SERIVICE)) {
			policyDetails = premiaPullService.fetchGmcPolicyDetailsFromPremia(policyNumber, riskId);
			isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, riskId, false);

		} else if (policyServiceType.equalsIgnoreCase(SHAConstants.GPA_POL_SERIVICE)) {
			policyDetails = premiaPullService.fetchGpaPolicyDetailsFromPremia(policyNumber, riskId);
			// policyDetails.setRiskSysId("");
			isIntegratedPolicy = premiaPullService.populateGMCandGPAPolicy(policyDetails, riskId, false);
		} else if (policyServiceType.equalsIgnoreCase(SHAConstants.JET_PRIVILLAGE_POL_SERVICE)) {
			policyDetails = premiaPullService.fetchJetPrivillagePolicyDetailsFromPremia(policyNumber);
			isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails, riskId, false);
		} else {
			policyDetails = premiaPullService.fetchPolicyDetailsFromPremia(policyNumber);
			isIntegratedPolicy = premiaPullService.populatePolicyFromTmpPolicy(policyDetails, riskId, false);
		}

		return isIntegratedPolicy;

	}

	public void setCpuCode(AddIntimationRequest addIntimationRequest, Policy policy, GalaxyIntimation galaxyIntimation,
			Hospitals hospital) {
		TmpCPUCode cpuCode = null;
		if (null != addIntimationRequest.getHospCode() && !("").equalsIgnoreCase(addIntimationRequest.getHospCode())) {
			String hospCode = addIntimationRequest.getHospCode();
//			if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			if (hospital.getHospitalType().getValue().equals("Non-Network")) {
				if (policy.getHomeOfficeCode() != null) {
					OrganaizationUnit branchOffice = premiaPullService
							.getInsuredOfficeNameByDivisionCode(policy.getHomeOfficeCode());
					if (branchOffice != null) {
						String officeCpuCode = branchOffice.getCpuCode();
						if (officeCpuCode != null) {
							cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
						}
					}
				}

//			}else if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			} else if (hospital.getHospitalType().getValue().equals("Network")) {

				if (!StringUtils.isBlank(hospCode)) {
					log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :" + hospCode.toUpperCase());
					if (null != hospital)
						cpuCode = premiaPullService.getCpuDetails(hospital.getCpuId());
				}
			}
		} else {
			String hospCode = SHAConstants.DEFAULT_HOSP_CODE;
			if (null != hospCode) {
				log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :" + hospCode.toUpperCase());
				hospital = this.premiaPullService.getHospitalByHospNo(hospCode.toUpperCase());
				if (null != hospital)
					cpuCode = premiaPullService.getCpuDetails(hospital.getCpuId());
			}
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)) {
			cpuCode = premiaPullService.getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//			if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			if (hospital != null) {
				if (!StringUtils.isBlank(addIntimationRequest.getHospCode())
						&& hospital.getHospitalType().getValue().equals("Non-Network")) {
					if (policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = premiaPullService
								.getInsuredOfficeNameByDivisionCode(addIntimationRequest.getHospCode());
						if (branchOffice != null) {
							String officeCpuCode = branchOffice.getCpuCode();
							if (officeCpuCode != null) {
								MasCpuLimit masCpuLimit = premiaPullService.getMasCpuLimit(Long.valueOf(officeCpuCode),
										SHAConstants.PROCESSING_CPU_CODE_GMC);
								if (masCpuLimit != null) {
									cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
				}
			} else {
				cpuCode = null;
			}
			galaxyIntimation.setCpuCode(cpuCode);
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)) {
			String baayasCpuCodeByPolicy = premiaPullService.getPaayasCpuCodeByPolicy(policy.getPolicyNumber());
			if (baayasCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode);
				}
			}

			String jioCpuCodeByPolicy = premiaPullService.getJioPolicyCpuCode(policy.getPolicyNumber());
			if (jioCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode);
				}
			}

		}

		if (policy != null && (policy.getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY)|| policy.getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))) {
			String kotakCpuCodeByPolicy = premiaPullService.getKotakPolicyCpuCode(policy.getPolicyNumber());
			if (kotakCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode);
				}
			}
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)) {
			cpuCode = premiaPullService.getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
			galaxyIntimation.setCpuCode(cpuCode);
		}
		
		galaxyIntimation.setCpuCode(cpuCode);
	}

	public String checkIntimationAlreadyCreated(String admissionDate, String hospitalCode, String policyNumber,
			String insuredId) {
		String returnMsg = "";
		String glxAdmissionDate = admissionDate.replace("/", "-");
		// System.out.println("glxAdmissionDate "+glxAdmissionDate);
		// System.out.println("admissionDate "+admissionDate);
		if (!StringUtils.isBlank(hospitalCode)) {
			Hospitals hospital = premiaPullService.getHospitalByHospNo(hospitalCode);

			List<GalaxyIntimation> galaxyIntimations = premiaPullService.findDuplicateInitmation(glxAdmissionDate,
					hospital.getKey(), policyNumber, insuredId);
			if (galaxyIntimations != null && !galaxyIntimations.isEmpty()) {
				returnMsg = "Same Details exists in the Intimation - " + galaxyIntimations.get(0).getIntimationId();
			} else {
				List<PremiaPreviousClaim> premiaIntimations = premiaPullService
						.findPremiaIntimationDuplicate(admissionDate, hospitalCode, policyNumber, insuredId);
				if (premiaIntimations != null && !premiaIntimations.isEmpty()) {
					returnMsg = "Same Details exists in the Intimation - "
							+ premiaIntimations.get(0).getIntimationNumber();
				}
			}
		} else {
			returnMsg = "DummyHospital";
		}
		return returnMsg;
	}
	
	public void setCRMCpuCode(AddIntimationRequest addIntimationRequest, Policy policy, GalaxyCRMIntimation galaxyIntimation,
			Hospitals hospital) {
		TmpCPUCode cpuCode = null;
		if (null != addIntimationRequest.getHospCode() && !("").equalsIgnoreCase(addIntimationRequest.getHospCode())) {
			String hospCode = addIntimationRequest.getHospCode();
//			if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			if (hospital!=null && hospital.getHospitalType().getValue().equals("Non-Network")) {
				if (policy!=null && addIntimationRequest.getPioCode() != null) {
					OrganaizationUnit branchOffice = premiaPullService
							.getInsuredOfficeNameByDivisionCode(addIntimationRequest.getPioCode());
					if (branchOffice != null) {
						String officeCpuCode = branchOffice.getCpuCode();
						if (officeCpuCode != null) {
							cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
						}
					}
				}

//			}else if((SHAConstants.PREMIA_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			} else if (hospital!=null && hospital.getHospitalType().getValue().equals("Network")) {

				if (!StringUtils.isBlank(hospCode)) {
					log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :" + hospCode.toUpperCase());
					if (null != hospital)
						cpuCode = premiaPullService.getCpuDetails(hospital.getCpuId());
				}
			}
		} else {
			String hospCode = SHAConstants.DEFAULT_HOSP_CODE;
			if (null != hospCode) {
				log.info("!!!!!!!HOSPITAL CODE !!!!!!!!!!!!!!! :" + hospCode.toUpperCase());
				hospital = this.premiaPullService.getHospitalByHospNo(hospCode.toUpperCase());
				if (null != hospital)
					cpuCode = premiaPullService.getCpuDetails(hospital.getCpuId());
			}
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)) {
			cpuCode = premiaPullService.getCpuDetails(ReferenceTable.GMC_CPU_CODE);
//			if((SHAConstants.PREMIA_NON_NETWORK_HOSPITAL).equalsIgnoreCase(addIntimationRequest.getHospitalTypeYn())) {
			if (hospital != null) {
				if (!StringUtils.isBlank(addIntimationRequest.getPioCode())
						&& hospital.getHospitalType().getValue().equals("Non-Network")) {
					if (policy.getHomeOfficeCode() != null) {
						OrganaizationUnit branchOffice = premiaPullService
								.getInsuredOfficeNameByDivisionCode(addIntimationRequest.getPioCode());
						if (branchOffice != null) {
							String officeCpuCode = branchOffice.getCpuCode();
							if (officeCpuCode != null) {
								MasCpuLimit masCpuLimit = premiaPullService.getMasCpuLimit(Long.valueOf(officeCpuCode),
										SHAConstants.PROCESSING_CPU_CODE_GMC);
								if (masCpuLimit != null) {
									cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(officeCpuCode));
								}
							}
						}
					}
				}
			} else {
				cpuCode = null;
			}
			galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.GMC_PRODUCT_KEY)) {
			String baayasCpuCodeByPolicy = premiaPullService.getPaayasCpuCodeByPolicy(policy.getPolicyNumber());
			if (baayasCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(baayasCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
				}
			}

			String jioCpuCodeByPolicy = premiaPullService.getJioPolicyCpuCode(policy.getPolicyNumber());
			if (jioCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(jioCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
				}
			}

		}

		if (policy != null && (policy.getProduct().getKey().equals(ReferenceTable.STAR_GMC_PRODUCT_KEY) || policy.getProduct().getKey().equals(ReferenceTable.STAR_GMC_NBFC_PRODUCT_KEY))) {
			String kotakCpuCodeByPolicy = premiaPullService.getKotakPolicyCpuCode(policy.getPolicyNumber());
			if (kotakCpuCodeByPolicy != null) {
				cpuCode = premiaPullService.getMasCpuCode(Long.valueOf(kotakCpuCodeByPolicy));
				if (cpuCode != null) {
					galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
				}
			}
		}

		if (policy != null && policy.getProduct().getKey().equals(ReferenceTable.JET_PRIVILEGE_PRODUCT)) {
			cpuCode = premiaPullService.getCpuDetails(ReferenceTable.JET_PRIVILEGE_CPU_CODE);
			galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
		}
		if(cpuCode!=null) {
			galaxyIntimation.setCpuCode(cpuCode.getCpuCode());
		}
	}

}