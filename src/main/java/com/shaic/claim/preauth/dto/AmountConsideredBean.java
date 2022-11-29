package com.shaic.claim.preauth.dto;

import java.io.Serializable;

import com.shaic.claim.premedical.dto.BaseCell;
import com.shaic.claim.premedical.dto.PMTableRow;

public class AmountConsideredBean implements Serializable {

	
	private static final long serialVersionUID = 7507486029910817072L;
	private static PMTableRow root = new PMTableRow(0, "", new BaseCell(), 0);
	private static PMTableRow root1 = new PMTableRow(0,"",new BaseCell(),0); 
	
	private static PMTableRow row1 = new PMTableRow(1, "Amount Considered", new BaseCell(), 17);
	private static PMTableRow row2 = new PMTableRow(2, "Co_Pay", new BaseCell(), 18);
	private static PMTableRow row3 = new PMTableRow(3, "Amount Considered (After Co-Pay)", new BaseCell(), 19);
	
	private static PMTableRow row4 = new PMTableRow(1,"Balance Sum Insured",new BaseCell(),17);
	private static PMTableRow row5 = new PMTableRow(3,"Balance Sum Insured (After Co-Pay)",new BaseCell(),19);
	
	static {
		root.addChild(row1);
		root.addChild(row2);
		root.addChild(row3);
		
		root1.addChild(row4);
		root1.addChild(row2);
		root1.addChild(row5);
		
	}
	
	public static PMTableRow getPMTableRows()
	{
		return root;
	}
	
	public static PMTableRow getPMTTableRowsforSumInsured(){
		return root1;
	}
	

}
