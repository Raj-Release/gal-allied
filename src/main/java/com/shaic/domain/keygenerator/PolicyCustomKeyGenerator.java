package com.shaic.domain.keygenerator;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.SequenceGenerator;

import com.shaic.domain.Policy;
import com.shaic.ims.bpm.claim.DBCalculationService;

public class PolicyCustomKeyGenerator extends SequenceGenerator{
	
	  @Override
	public Serializable generate(SessionImplementor session, Object obj) {
		if (obj != null && obj instanceof Policy) {
			Policy policyObj = (Policy) obj;
			if (policyObj.getKey() == null) {
				try {
					DBCalculationService dbCalcService = new DBCalculationService();
					Long sequence2 = dbCalcService
							.generateSequence("SEQ_CLS_POLICY_KEY");
					return sequence2;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return policyObj.getPolicySystemId();
		}
		return null;
	}

}
