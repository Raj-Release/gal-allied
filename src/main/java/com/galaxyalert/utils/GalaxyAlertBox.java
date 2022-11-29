package com.galaxyalert.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alert.util.ButtonOption;
import com.alert.util.ButtonType;
import com.alert.util.MessageBox;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.v7.ui.VerticalLayout;

public class GalaxyAlertBox {

	
	public GalaxyAlertBox(){
		
	}
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createInformationBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createInfo().withCaptionCust("Information")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));*/
		
		}
		
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		msgBox.open();
		return returnButtons;
	}
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createAlertBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createWarning().withCaptionCust("Warning")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {

			getButtonsAndIcons(returnButtons, msgBox, entry);
		
		}
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		msgBox.open();
		return returnButtons;
	}

	
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createErrorBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createError().withCaptionCust("Error")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		*/}
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		msgBox.open();
		return returnButtons;
	}
	
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createWarningBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createWarning().withCaptionCust("Warning")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		*/}
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		msgBox.open();
		return returnButtons;
	}
	
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createConfirmationbox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createQuestion().withCaptionCust("Confirmation")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		TreeMap<String, String> customTreeMap=new TreeMap<String, String>(Collections.reverseOrder());
		customTreeMap.putAll(buttonsKeyWithName);
		for (Map.Entry<String,String> entry : customTreeMap.entrySet())  {
			
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		*/}
		msgBox.open();
		return returnButtons;
	}
	

	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createCustomBox(String caption,
			Object content, HashMap<String, String> buttonsKeyWithName,
			String type) {

		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = null;
		
		
		if (type.equals(GalaxyTypeofMessage.INFORMATION.toString())) {
			msgBox = MessageBox.createInfo();
		} else if (type.equals(GalaxyTypeofMessage.CRITICALALERT.toString())) {
			msgBox = MessageBox.createCritical();
		} else if (type.equals(GalaxyTypeofMessage.ERROR.toString())) {
			msgBox = MessageBox.createError();
		} else if (type.equals(GalaxyTypeofMessage.QUESTION.toString())) {
			msgBox = MessageBox.createQuestion();
		} else if (type.equals(GalaxyTypeofMessage.WARNING.toString())) {
			msgBox = MessageBox.createWarning();
		} else {
			msgBox = MessageBox.createInfo();
		}
		  //msgBox.setButtonAddClosePerDefault(false);
		msgBox.withCaptionCust(caption);
		if (content instanceof VerticalLayout) {
			msgBox.withTableMessage((VerticalLayout) content);
		} else {
			msgBox.withHtmlMessage((String) content);
		}
		/*buttonsKeyWithName.forEach((key, value) -> {
			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					key + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(key);
			msgBox.withButton(buttonType, null, ButtonOption.caption(value)
					.icon(imgIcon));
			returnButtons.put(key, msgBox.getButton(buttonType));
		});*/
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		*/}
		msgBox.open();
		return returnButtons;
	}
	
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createCriticalBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createCritical().withCaptionCust("Critical Alert")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			getButtonsAndIcons(returnButtons, msgBox, entry);
			/*

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		*/}
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		msgBox.open();
		return returnButtons;
	}
	
	
	
	private static void getButtonsAndIcons(
			HashMap<String, Button> returnButtons, MessageBox msgBox,
			Map.Entry<String, String> entry) {
		Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
				entry.getKey() + "_ICON").getIcon());
		ButtonType buttonType = ButtonType.valueOf(entry.getKey());
		msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue()));
		Button button = msgBox.getButton(buttonType);
		button.setIcon(imgIcon);
		returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
	}
	
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Object> createCutomWithCloselBox(String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Object> returnButtons = new HashMap<String, Object>();
		MessageBox msgBox = MessageBox.createCritical().withCaptionCust("Information")
				.withHtmlMessage(content);
		//msgBox.setButtonAddClosePerDefault(false);
		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {
			//getButtonsAndIcons(returnButtons, msgBox, entry);
			

			Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
					entry.getKey() + "_ICON").getIcon());
			ButtonType buttonType = ButtonType.valueOf(entry.getKey());
			msgBox.withButton(buttonType, null, ButtonOption.caption(entry.getValue())
					.icon(imgIcon));
			returnButtons.put(entry.getKey(), msgBox.getButton(buttonType));
		
		}
		/*buttonsKeyWithName
				.forEach((key, value) -> {
					Resource imgIcon = new ThemeResource(GalaxyIconsEnum.valueOf(
							key + "_ICON").getIcon());
					ButtonType buttonType = ButtonType.valueOf(key);
					msgBox.withButton(buttonType, null, ButtonOption.caption(value)
							.icon(imgIcon));
					returnButtons.put(key, msgBox.getButton(buttonType));
				});*/
		returnButtons.put("close", msgBox.getWindow());
		msgBox.open();
		return returnButtons;
	}
	
	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createQuestionCust(String caption, Object content, HashMap<String, String> buttonsKeyWithName,String type) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();

				MessageBox msgBox = null;
				
				
				if (type.equals(GalaxyTypeofMessage.INFORMATION.toString())) {
					msgBox = MessageBox.createInfo();
				} else if (type.equals(GalaxyTypeofMessage.CRITICALALERT.toString())) {
					msgBox = MessageBox.createCritical();
				} else if (type.equals(GalaxyTypeofMessage.ERROR.toString())) {
					msgBox = MessageBox.createError();
				} else if (type.equals(GalaxyTypeofMessage.QUESTION.toString())) {
					msgBox = MessageBox.createQuestion();
				} else if (type.equals(GalaxyTypeofMessage.WARNING.toString())) {
					msgBox = MessageBox.createWarning();
				} else {
					msgBox = MessageBox.createInfo();
				}
				  //msgBox.setButtonAddClosePerDefault(false);
				msgBox.withCaptionCust(caption);
				if (content instanceof VerticalLayout) {
					msgBox.withTableMessage((VerticalLayout) content);
				} else {
					msgBox.withHtmlMessage((String) content);
				}

				TreeMap<String, String> customTreeMap=new TreeMap<String, String>(Collections.reverseOrder());
				customTreeMap.putAll(buttonsKeyWithName);
				for (Map.Entry<String,String> entry : customTreeMap.entrySet())  {
					getButtonsAndIcons(returnButtons, msgBox, entry);
			}
				msgBox.open();
				return returnButtons;
	}
	

	@SuppressWarnings("static-access")
	public static HashMap<String, Button> createInformationBoxWithCaption(String caption,String content,
			HashMap<String, String> buttonsKeyWithName) {
		HashMap<String, Button> returnButtons = new HashMap<String, Button>();
		MessageBox msgBox = MessageBox.createInfo().withCaptionCust(caption)
				.withHtmlMessage(content);

		for (Map.Entry<String,String> entry : buttonsKeyWithName.entrySet())  {			
			getButtonsAndIcons(returnButtons, msgBox, entry);
		}
		msgBox.open();
		return returnButtons;
	}
}
