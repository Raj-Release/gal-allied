package com.shaic.claim.policy.search.ui;


import java.util.LinkedHashMap;

import com.shaic.arch.GMVPView;
import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.Page;
import com.shaic.claim.intimation.ViewPreviousIntimation;
import com.shaic.claim.productbenefit.view.ViewProductBenefits;
import com.vaadin.v7.data.util.BeanItemContainer;

public interface NewSearchPolicyView extends GMVPView {
	  public void list(Page<NewSearchPolicyTableDTO> tableRows);
	  //Adding the below method for compatablity with earlier search UI.
		 public void init(BeanItemContainer<SelectValue> searchByContainer ,BeanItemContainer<SelectValue> productNameContainer, 
				  BeanItemContainer<SelectValue> productTypeContainer,BeanItemContainer<SelectValue> policyCodeOrNameContainer) ;
		 public void setPolicyValueMap(LinkedHashMap<String, String> policyValues);
		 public void showProductBenefits(ViewProductBenefits a_viewProductBenefits);
		 public void showPreviousIntimation(ViewPreviousIntimation view);
}
