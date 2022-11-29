package com.shaic.claim.leagalbilling;

import java.io.Serializable;

import com.shaic.claim.premedical.dto.PMTableRow;

public class LegalSettlementTabelBean implements Serializable{

	private static final long serialVersionUID = 7968161968596274460L;

	private static PMTableRow intrestApplicableroot = new PMTableRow(0, "", new LegalBaseCell(), 0);
	private static PMTableRow root = new PMTableRow(0, "", new LegalBaseCell(), 0);
	
	private static PMTableRow row1 = new PMTableRow(1, "Award Amount", new LegalBaseCell(), 8);
	private static PMTableRow row2 = new PMTableRow(2, "Cost", new LegalBaseCell(), 9);
	private static PMTableRow row3 = new PMTableRow(3, "Compensation", new LegalBaseCell(), 10);
	private static PMTableRow row4 = new PMTableRow(4, "Interest for current Claim", new LegalBaseCell(), 12);
	private static PMTableRow row5 = new PMTableRow(5, "Total interest paid for previous claims", new LegalBaseCell(), 13);
	private static PMTableRow row6 = new PMTableRow(6, "Total Interest payable", new LegalBaseCell(), 14);
	private static PMTableRow row7 = new PMTableRow(7, "TDS", new LegalBaseCell(), 15);
	private static PMTableRow row8 = new PMTableRow(8, "Interest payable less TDS", new LegalBaseCell(), 16);
	
	static{
		
		intrestApplicableroot.addChild(row1);
		intrestApplicableroot.addChild(row2);
		intrestApplicableroot.addChild(row3);
		intrestApplicableroot.addChild(row4);
		intrestApplicableroot.addChild(row5);
		intrestApplicableroot.addChild(row6);
		intrestApplicableroot.addChild(row7);
		intrestApplicableroot.addChild(row8);
		
		root.addChild(row1);
		root.addChild(row2);
		root.addChild(row3);
	}
	
	public static PMTableRow getPMTableintrestApplicableRows(){
			
		return intrestApplicableroot;
	}
	
	public static PMTableRow getPMTableRows(){
			
		return root;
	}
}
