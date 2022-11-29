package com.shaic.claim.preauth.wizard.pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {
	
	public static void main(String args[]) throws ParseException
	{
		String date = "5/20/2013 6:31:54";
		SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
		Date d = sdf.parse(date);
		System.out.println("----date d"+d);
	}

}
