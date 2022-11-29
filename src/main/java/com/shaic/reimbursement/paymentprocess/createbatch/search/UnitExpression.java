package com.shaic.reimbursement.paymentprocess.createbatch.search;

import java.io.Serializable;

import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.CriteriaQueryCompiler.RenderingContext;
import org.hibernate.ejb.criteria.expression.function.BasicFunctionExpression;

public class UnitExpression extends BasicFunctionExpression<String> implements Serializable {
	
	public UnitExpression(CriteriaBuilderImpl criteriaBuilder, Class<String> javaType,
		      String functionName) {
		    super(criteriaBuilder, javaType, functionName);
		  }

		  @Override
		  public String render(RenderingContext renderingContext) {
		    return getFunctionName();
		  }
}
