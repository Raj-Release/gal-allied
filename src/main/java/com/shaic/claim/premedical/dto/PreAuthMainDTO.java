package com.shaic.claim.premedical.dto;

import java.io.Serializable;
import java.util.List;

import com.shaic.domain.preauth.Coordinator;
import com.shaic.domain.preauth.Diagnosis;
import com.shaic.domain.preauth.Preauth;
import com.shaic.domain.preauth.Procedure;
import com.shaic.domain.preauth.Speciality;

public class PreAuthMainDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5664583575938495191L;
	
	private Preauth preauth;
	
	private Coordinator coordinator;
	
	private List<Speciality> speciality;
	
	private List<Procedure> procedure;
	
	private List<Procedure> newProcedure;
	
	private List<Diagnosis> diagnosis;

	public Preauth getPreauth() {
		return preauth;
	}

	public void setPreauth(Preauth preauth) {
		this.preauth = preauth;
	}

	public Coordinator getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	public List<Speciality> getSpeciality() {
		return speciality;
	}

	public void setSpeciality(List<Speciality> speciality) {
		this.speciality = speciality;
	}

	public List<Procedure> getProcedure() {
		return procedure;
	}

	public void setProcedure(List<Procedure> procedure) {
		this.procedure = procedure;
	}

	public List<Procedure> getNewProcedure() {
		return newProcedure;
	}

	public void setNewProcedure(List<Procedure> newProcedure) {
		this.newProcedure = newProcedure;
	}

	public List<Diagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(List<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}
	
}
