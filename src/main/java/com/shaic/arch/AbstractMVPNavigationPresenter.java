package com.shaic.arch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.addon.cdimvp.AbstractMVPPresenter;
import org.vaadin.addon.cdimvp.MVPView;

@SuppressWarnings("serial")
public abstract class AbstractMVPNavigationPresenter<V extends MVPView> extends AbstractMVPPresenter<V> {

    /**
     * This is really just convenience method used to make view navigation
     * easier
     *
     * view.getUI().getNavigator().navigateTo(SomeMVPNavigationView.NAVIGATION_NAME
     * + makeParametersFragment(parameterMap));
     *
     * @param viewParams
     * @return a String representing the parameters in a URIFragment
     */
    protected String makeParametersFragment(final java.util.Map<String, String> viewParams) {
        final StringBuilder strBuilder = new StringBuilder();
        for (java.util.Map.Entry<String, String> entry : viewParams.entrySet()) {
            try {
                if (strBuilder.length() == 0) {
                    strBuilder.append('/');
                } else {
                    strBuilder.append('&');
                }
                // Always encode the name
                strBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                strBuilder.append('=');
                // Always encode the value
                strBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                Logger.getLogger(getClass().getName()).log(Level.INFO, "makeParamersFragment {0}  added  {1} ==> {2} ", new Object[]{strBuilder, entry.getKey(), entry.getValue()});
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return strBuilder.toString();
    }

}
