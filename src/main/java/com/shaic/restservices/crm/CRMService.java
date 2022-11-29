package com.shaic.restservices.crm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shaic.arch.SHAUtils;
import com.shaic.claim.intimation.ViewPreviousIntimationDto;
import com.shaic.claim.intimation.ViewPreviousIntimationService;
import com.shaic.domain.GalaxyCRMIntimation;
import com.shaic.domain.GalaxyIntimation;
import com.shaic.domain.Hospitals;
import com.shaic.domain.Intimation;
import com.shaic.ims.bpm.claim.BPMClientContext;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class CRMService {

	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	private UserTransaction utx;
	
	@EJB
	private ViewPreviousIntimationService prevIntiService;

	public String getDetailsForPolicy(IntimationDetailsRequest req) throws JSONException {
		JSONArray intimationArray = new JSONArray();
		JSONObject intmObj = null;
		JSONObject response = new JSONObject();
		List<ViewPreviousIntimationDto> intimationDetals = prevIntiService.getPreviousIntimationList(req.getPolicyNumber(), req.getRiskId(), entityManager);
		List<ViewPreviousIntimationDto> premIntimationDetals = prevIntiService.getPreviousPremiaIntimationList(req.getPolicyNumber(), req.getRiskId(), entityManager);
		//Dummy Intimations details list return in the response -03-03-2021
		List<ViewPreviousIntimationDto> dummyIntimationDetals = prevIntiService.getDummyIntimationList(req.getPolicyNumber(), req.getRiskId(), entityManager);
		if(premIntimationDetals != null & !premIntimationDetals.isEmpty()){
			intimationDetals.addAll(premIntimationDetals);
		}
		if(dummyIntimationDetals !=null & !dummyIntimationDetals.isEmpty())
		{
			intimationDetals.addAll(dummyIntimationDetals);
		}
		Collections.sort(intimationDetals, new Comparator<ViewPreviousIntimationDto>() {
		    public int compare(ViewPreviousIntimationDto m1, ViewPreviousIntimationDto m2) {
		    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		    	Date m1Date = null;
		    	Date m2Date = null;
				try {
					m1Date = sdf.parse(m1.getCreatedOn());
					m2Date = sdf.parse(m2.getCreatedOn());
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        return m2Date.compareTo(m1Date);
		    }
		});
       	
		if(intimationDetals != null && !intimationDetals.isEmpty()){
			for (ViewPreviousIntimationDto rec : intimationDetals) {
				intmObj =  new JSONObject();
				intmObj.put("intimationNo", rec.getIntimationNo());
				intmObj.put("policyNo", rec.getPolicyNo());
				intmObj.put("insuredPatientName", (rec.getInsuredPatientName() == null)?"":rec.getInsuredPatientName());
				intmObj.put("intimatorName", rec.getCallerOrIntimatorName());
				Hospitals hospObj =  null;
				if(rec.getHospitalKey() != null){
					Query query = entityManager.createNamedQuery("Hospitals.findByKey");
					query = query.setParameter("key", rec.getHospitalKey());
					List<Hospitals> hospitalList = query.getResultList();
					if(null != hospitalList && !hospitalList.isEmpty()) {
						hospObj = hospitalList.get(0);
					}
				}else if(rec.getHospitalCode() != null){
					Query query = entityManager.createNamedQuery("Hospitals.findByCode");
					query = query.setParameter("hospitalCode", rec.getHospitalCode().toUpperCase());
					List<Hospitals> hospitalList = query.getResultList();
					if(null != hospitalList && !hospitalList.isEmpty()) {
						hospObj = hospitalList.get(0);
					}
				}
				if(hospObj != null){
					intmObj.put("hospitalName", hospObj.getName());
					intmObj.put("hospitalAddress", hospObj.getAddress());
					if(hospObj.getHospitalType() != null){
						intmObj.put("hospitalType", hospObj.getHospitalType().getValue());
					}else{
						intmObj.put("hospitalType", "");
					}
				}
				else if(rec.getIsdummy() !=null && rec.getIsdummy().equalsIgnoreCase("Y"))
				{
					intmObj.put("hospitalName", (rec.getHospitalName() == null)?"":rec.getHospitalName());
					intmObj.put("hospitalAddress", (rec.getHospitalAddress() == null)?"":rec.getHospitalAddress());
					intmObj.put("hospitalType", (rec.getHospitalType() == null)?"":rec.getHospitalType());
				}
				else
				{
					intmObj.put("hospitalName", "");
					intmObj.put("hospitalAddress", "");
					intmObj.put("hospitalType", "");
				}
				intmObj.put("contactNo", rec.getContactNo());
				if(rec.getIsdummy() !=null && rec.getIsdummy().equalsIgnoreCase("Y"))
				{
					intmObj.put("status", "DUMMY_HOSPITAL_PENDING");	
				}
				else
				{
					intmObj.put("status", rec.getStatus());
	
				}
				intmObj.put("admittedDate", rec.getAdmittedDate());
				intmObj.put("createdOn", rec.getCreatedOn());
				intmObj.put("modifiedOn", (rec.getModifiedOn() == null)?"":rec.getModifiedOn());
				intmObj.put("saveSubmittedOn", rec.getSaveAndSubmittedOn());
				intmObj.put("attenderNo", rec.getAttenderMobileNo());
				intimationArray.put(intmObj);
			}
			response.put("status", "Success");
			response.put("resCode", 200);
			response.put("intimationDetails", intimationArray);
		}else{
			response.put("status", "Success");
			response.put("resCode", 200);
			response.put("intimationDetails", intimationArray);
		}
		return response.toString();
	}


	public String getIntimationDetails(ViewIntimationRequest req) throws JSONException{
		JSONArray detailArray = new JSONArray();
		JSONObject detailObj = null;
		JSONObject response = new JSONObject();
		Intimation intmObj = null;
		GalaxyCRMIntimation glxIntmObj = null;
		Query findByKey = entityManager.createNamedQuery("Intimation.findByIntimationNumber").setParameter("intimationNo", req.getIntimationNumber());
		List<Intimation> intimationList = (List<Intimation>) findByKey.getResultList();
		if (!intimationList.isEmpty()) {
			intmObj = intimationList.get(0);
		}
		Query findByKey1 = entityManager.createNamedQuery("GalaxyCRMIntimation.findByIntimationNumber").setParameter("intimationNo", req.getIntimationNumber());
		List<GalaxyCRMIntimation> intimationList1 = (List<GalaxyCRMIntimation>) findByKey1.getResultList();
		if (!intimationList1.isEmpty()) {
			glxIntmObj = intimationList1.get(0);
		}
		if(intmObj != null){
			detailObj =  new JSONObject();
			detailObj.put("insuredId", (intmObj.getInsured() == null) ? "" : String.valueOf(intmObj.getInsured().getInsuredId()));
			detailObj.put("intimationMode", (intmObj.getIntimationMode() == null) ? "" : intmObj.getIntimationMode().getValue());
			detailObj.put("intimatedBy", (intmObj.getIntimationSource() == null) ? "" : intmObj.getIntimatedBy().getValue());
			detailObj.put("intimatorName", (intmObj.getIntimaterName() == null) ? "" : intmObj.getIntimaterName());
			detailObj.put("intimatorContactNo", (intmObj.getCallerMobileNumber() == null) ? "" : intmObj.getCallerMobileNumber());
			detailObj.put("policyNumber", (intmObj.getPolicy() == null) ? "" : intmObj.getPolicy().getPolicyNumber());
			Hospitals hospObj =  null;
				Query query = entityManager.createNamedQuery("Hospitals.findByKey");
				query = query.setParameter("key", intmObj.getHospital());
				List<Hospitals> hospitalList = query.getResultList();
				if(null != hospitalList && !hospitalList.isEmpty()) {
					hospObj = hospitalList.get(0);
				}
			detailObj.put("hospCode", (hospObj.getHospitalCode() == null) ? "" : hospObj.getHospitalCode());
			detailObj.put("hospName", (hospObj.getName() == null) ? "" : hospObj.getName());
			detailObj.put("hospAddress", (hospObj.getAddress() == null) ? "" : hospObj.getAddress());
			detailObj.put("hospCity", (hospObj.getCity() == null) ? "" : hospObj.getCity());
			detailObj.put("hospState", (hospObj.getState() == null) ? "" : hospObj.getState());
			detailObj.put("hospContactNo", (hospObj.getPhoneNumber() == null) ? "" : hospObj.getPhoneNumber());
			detailObj.put("hospFaxNo", (hospObj.getFax() == null) ? "" : hospObj.getFax());
			
			detailObj.put("admissionType", (intmObj.getAdmissionType() == null) ? "" : intmObj.getAdmissionType().getValue());
			detailObj.put("managementType", (intmObj.getManagementType() == null) ? "" : intmObj.getManagementType().getValue());
			detailObj.put("reasonForAdmission", (intmObj.getAdmissionReason() == null) ? "" : intmObj.getAdmissionReason());
			detailObj.put("admitted", (intmObj.getAdmissionDate() == null) ? "" : SHAUtils.formatDate(intmObj.getAdmissionDate()));
			detailObj.put("roomCategory", (glxIntmObj.getRoomCategory() == null) ? "" : glxIntmObj.getRoomCategory());
			detailObj.put("inpatientNo", (intmObj.getInpatientNumber() == null) ? "" : intmObj.getInpatientNumber());
			detailObj.put("createdBy", (intmObj.getCreatedBy() == null) ? "" : intmObj.getCreatedBy());
			detailObj.put("attMobNo", (glxIntmObj.getAttenderMobileNo() == null) ? "" : glxIntmObj.getAttenderMobileNo());
			detailObj.put("policyYr", (intmObj.getPolicyYear() == null) ? "" : intmObj.getPolicyYear());
			detailObj.put("accDeathYn", (glxIntmObj.getAccDeath() == null) ? "" : glxIntmObj.getAccDeath());
			detailObj.put("hospRequYn", (glxIntmObj.getHospitalRequired() == null) ? "N" : glxIntmObj.getHospitalRequired());
			detailObj.put("paCategory", (intmObj.getPaCategory() == null) ? "" : intmObj.getPaCategory());
			detailObj.put("paPatientName", (intmObj.getPaPatientName() == null) ? "" : intmObj.getPaPatientName());
			detailObj.put("paParentName", (intmObj.getPaParentName() == null) ? "" : intmObj.getPaParentName());
			detailObj.put("paParentDob", (intmObj.getPaParentDOB() == null) ? "" : SHAUtils.formatDate(intmObj.getPaParentDOB()));
			detailObj.put("paParentAge", (intmObj.getPaParentAge() == null) ? "" : String.valueOf(intmObj.getPaParentAge().intValue()));
			
			detailObj.put("paPatientAge", (intmObj.getPaPatientAge() == null) ? "" : String.valueOf(intmObj.getPaPatientAge().intValue()));
			detailObj.put("paPatientDob", (intmObj.getPaPatientDOB() == null) ? "" : SHAUtils.formatDate(intmObj.getPaPatientDOB()));
			
			
			detailObj.put("admittedTime", (intmObj.getAdmissionDate() == null) ? "" : getTime(intmObj.getAdmissionDate()));
			detailObj.put("dischargeDate", (intmObj.getDateOfDischarge() == null) ? "" : SHAUtils.formatDate(intmObj.getDateOfDischarge()));
			detailObj.put("dischargeTime", (intmObj.getDateOfDischarge() == null) ? "" : getTime(intmObj.getDateOfDischarge()));
			if(intmObj.getPolicy().getGpaPolicyType() != null && intmObj.getPolicy().getGpaPolicyType().equalsIgnoreCase("un named")){
				detailObj.put("insuredPatientName", (intmObj.getPaPatientName() == null) ? "" : intmObj.getPaPatientName());
			}else{
				detailObj.put("insuredPatientName", (intmObj.getInsured().getInsuredName() == null) ? "" : intmObj.getInsured().getInsuredName());
			}
			detailObj.put("insuredEmail", (glxIntmObj.getInsuredEmail() == null) ? "" : glxIntmObj.getInsuredEmail());
			detailObj.put("hospitalType", (hospObj.getHospitalType() == null) ? "" : hospObj.getHospitalType().getValue());
			detailObj.put("hospitalDoctorName", (glxIntmObj.getHospitalDoctorName() == null) ? "" : glxIntmObj.getHospitalDoctorName());
			detailObj.put("comments", (glxIntmObj.getComments() == null) ? "" : glxIntmObj.getComments());
			detailObj.put("suspiciousComments", (intmObj.getHospitalComments() == null) ? "" : intmObj.getHospitalComments());
			
			detailArray.put(detailObj);
			
			response.put("status", "Success");
			response.put("resCode", 200);
			response.put("intimationDetails", detailArray);
		}else{
			response.put("status", "Success");
			response.put("resCode", 200);
			response.put("intimationDetails", detailArray);
		}
		
		return response.toString();
	}
	
	public String getTime(Date argDate){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = dateFormat.format(argDate);
		return formattedDate;
	}
	
	public String getPolicyBSIDetails(IntimationDetailsRequest req) throws JSONException{
		JSONObject response = new JSONObject();

		Connection connection = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String bsiValue = "";
		try{
			connection = BPMClientContext.getConnection();
			cs = connection.prepareCall("{call PRC_CRM_BALANCE_SI(?, ?, ?)}");
			cs.setString(1, req.getPolicyNumber());
			cs.setString(2, req.getRiskId());
			cs.registerOutParameter(3, Types.VARCHAR);
			cs.execute();

			bsiValue = cs.getString(3);
//			System.out.println("bsiValue "+bsiValue);

			response.put("balanceSumInsured", bsiValue);
			response.put("resCode", 200);
		}catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (connection != null) {
					connection.close();
				}
			}catch(SQLException e){
				e.printStackTrace();
			}

		}
		return response.toString();
	}
}
