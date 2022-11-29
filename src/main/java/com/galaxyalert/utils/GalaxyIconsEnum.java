package com.galaxyalert.utils;

public enum GalaxyIconsEnum {

	OK_ICON("images/tick.png"),
	
	CANCEL_ICON("images/cross.png"),
	
	NO_ICON("images/cross.png"),
	
	YES_ICON("images/tick.png"),
	
	HELP_ICON("images/tick.png");
	
	
	
	String icon;
	 GalaxyIconsEnum(String value) {
         this.icon = value;
	 }
	 public String getIcon() {
			return icon;
		}
	
}
