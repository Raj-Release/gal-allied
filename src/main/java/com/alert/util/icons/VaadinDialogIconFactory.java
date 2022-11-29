package com.alert.util.icons;

import com.alert.util.MessageBox;
import com.vaadin.server.ThemeResource;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Label;
import com.vaadin.icons.VaadinIcons;

/**
 * Loads the FontAwesome icon set. The default color of the icons is black.
 * You can customize the color of all icons in the css.
 * The corresponding selectors are "v-label-infoIcon", "v-label-questionIcon", 
 * "v-label-warningIcon", "v-label-errorIcon". This example uses following css:
 * 
 * <pre>
 * .v-label-questionIcon, .v-label-infoIcon {
 * 		color: #0080B0;
 * }
 *
 * .v-label-warningIcon, .v-label-errorIcon {
 *		color: #E00000;
 * }
 * </pre>
 * 
 * @author Dieter Steinwedel
 */
public class VaadinDialogIconFactory implements DialogIconFactory {
	
	private static final long serialVersionUID = 1L;

	private Label createIconLabel(String html, String styleName) {
		Label result = new Label("<span style='font-size:" + MessageBox.getDialogDefaultIconWidth() + ";  line-height: " + MessageBox.getDialogDefaultIconWidth() + ";'>" + html + "</span>", ContentMode.HTML);
		result.addStyleName(styleName + "Icon");
		return result;
	}
	
	@Override
	public Component getQuestionIcon() {
		return createIconLabel(VaadinIcons.QUESTION_CIRCLE.getHtml(), "question");
	}

	@Override
	public Component getInfoIcon() {
		return createIconLabel(VaadinIcons.INFO_CIRCLE.getHtml(), "info");
		//return createIconLabel("<img src='http://192.168.1.36:8080/ims/VAADIN/themes/mytheme/images/info.png'>", "info");
		//return createIconLabel(new ThemeResource("images/info.png").toString(), "info");
	}

	@Override
	public Component getWarningIcon() {
		return createIconLabel(VaadinIcons.EXCLAMATION_CIRCLE.getHtml(), "warning");
	}

	@Override
	public Component getErrorIcon() {
		return createIconLabel(VaadinIcons.CLOSE_CIRCLE.getHtml(), "error");
	}
	
}
