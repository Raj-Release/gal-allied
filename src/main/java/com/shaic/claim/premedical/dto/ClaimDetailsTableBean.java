package com.shaic.claim.premedical.dto;

import java.io.Serializable;

public class ClaimDetailsTableBean implements Serializable{

	private static final long serialVersionUID = -618381680734306025L;
	private static PMTableRow root = new PMTableRow(0, "", new BaseCell(), 0);
	
	private static PMTableRow row1 = new PMTableRow(1, "Room Rent & Nursing charges", new NoOfDaysCell(), 8);
	private static PMTableRow row2 = new PMTableRow(2, "ICU Charges", new NoOfDaysCell(), 9);
	private static PMTableRow row3 = new PMTableRow(3, "ICCU", new NoOfDaysCell(), 22);
	private static PMTableRow row4 = new PMTableRow(4, "OT Charges", new NoOfDaysCell(), 10);
	private static PMTableRow row5 = new PMTableRow(5, "Professional Fees (Surgeon, Anastheist, Consultation charges etc)", new BaseCell(), 11);
	private static PMTableRow row6 = new PMTableRow(6, "Investigation & Diagnostics", new BaseCell(), 12);
	private static PMTableRow row7 = new PMTableRow(7, "Medicines & Consumables", new BaseCell(), 0);
	private static PMTableRow row8 = new PMTableRow(8, "a) Medicines", new BaseCell(), 13);
	private static PMTableRow row9 = new PMTableRow(9, "b) Consumables", new BaseCell(), 23);
	private static PMTableRow row10 = new PMTableRow(10, "c) Implant/Stunt/Valve/Pacemaker/Etc", new BaseCell(), 14);
	private static PMTableRow row11 = new PMTableRow(11, "Ambulance Fees", new BaseCell(), 15);
	private static PMTableRow row12 = new PMTableRow(12, "Package Charges", new BaseCell(), 0);
	private static PMTableRow row13 = new PMTableRow(13, "a) ANH Package", new BaseCell(), 16);
	private static PMTableRow row14 = new PMTableRow(14, "b) Composite Package", new BaseCell(), 17);
	private static PMTableRow row15 = new PMTableRow(15, "c) Other Package", new BaseCell(), 18);
	private static PMTableRow row16 = new PMTableRow(16, "Others", new BaseCell(), 19);
	private static PMTableRow row17 = new PMTableRow(17,"Taxes and Other Cess", new BaseCell(), 20);
	private static PMTableRow row18 = new PMTableRow(18, "Total", new BaseCell(), 100);
	
	//R1006
	private static PMTableRow row19 = new PMTableRow(19,"Discount in Hospital Bill", new BaseCell(), 21);
	
	static{
		row7.addChild(row8);
		row7.addChild(row9);
		row7.addChild(row10);
		
		row12.addChild(row13);
		row12.addChild(row14);
		row12.addChild(row15);
		
		root.addChild(row1);
		root.addChild(row2);
		root.addChild(row3);
		root.addChild(row4);
		root.addChild(row5);
		root.addChild(row6);
		root.addChild(row7);
		root.addChild(row11);
		root.addChild(row12);
		root.addChild(row16);
		root.addChild(row17);
		root.addChild(row18);
		root.addChild(row19);
		
	}
	
	public static PMTableRow getPMTableRows()
	{
		return root;
	}
}
