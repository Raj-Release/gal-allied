package com.shaic.domain.keygenerator;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;

import com.shaic.domain.Insured;
import com.shaic.ims.bpm.claim.DBCalculationService;

public class InsuredCustomKeyGenerator extends SequenceGenerator{
	
	  @Override
	public Serializable generate(SessionImplementor session, Object obj) {
		if (obj != null && obj instanceof Insured) {
			Insured insuredObj = (Insured) obj;
			if (insuredObj.getKey() == null) {
				try {
					DBCalculationService dbCalcService = new DBCalculationService();
					Long sequence2 = dbCalcService
							.generateSequence("SEQ_CLS_POLICY_INSURED_KEY");
					return sequence2;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return insuredObj.getInsuredId();
		}
		return null;
	}

}
