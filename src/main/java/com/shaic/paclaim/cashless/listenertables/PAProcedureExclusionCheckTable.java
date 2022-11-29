package com.shaic.paclaim.cashless.listenertables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.shaic.arch.fields.dto.SelectValue;
import com.shaic.arch.table.GRowHandlerTable;
import com.shaic.arch.table.TableFieldDTO;
import com.shaic.claim.preauth.wizard.dto.ProcedureDTO;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.v7.ui.TextField;

//public class ProcedureExclusionCheckTable extends GEditableTable<ProcedureDTO> {
public class PAProcedureExclusionCheckTable extends GRowHandlerTable<ProcedureDTO> {


	private static final long serialVersionUID = 8413428418037609537L;
	
	private List<String> errorMessages;
	
	private Validator validator;

	public PAProcedureExclusionCheckTable() {
		super(ProcedureDTO.class);
		setUp();
		// TODO Auto-generated constructor stub
	}

	public final Object[] NATURAL_COL_ORDER = new Object[] {
			/*"procedureNameValue", "procedureCodeValue", "packageAmount",
			"policyAging", "procedureStatus",
			"exclusionDetails", "remarks"*/
		"procedureNameValue", "procedureCodeValue", "packageRate",
		/*"policyAging",*/ "procedureStatus",
		"exclusionDetails","copay","remarks"
	};

	//public static final Map<String, TableFieldDTO> fieldMap = new HashMap<String, TableFieldDTO>();

	/*static {
		fieldMap.put("procedureNameValue", new TableFieldDTO("procedureNameValue",
				TextField.class, String.class, false));
		fieldMap.put("procedureCodeValue", new TableFieldDTO("procedureCodeValue",
				TextField.class, String.class, false));
		fieldMap.put("packageAmount", new TableFieldDTO("packageAmount",
				TextField.class, String.class, false));
		fieldMap.put("packageRate", new TableFieldDTO("packageRate",
				TextField.class, String.class, false));
		fieldMap.put("policyAging", new TableFieldDTO("policyAgeing",
				TextField.class, String.class, false));
		fieldMap.put("procedureStatus", new TableFieldDTO("procedureStatus",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("exclusionDetails", new TableFieldDTO("exclusionDetails",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("copay", new TableFieldDTO("copay",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextField.class, String.class, true, 100)); 
	}*/

	@Override
	protected void newRowAdded() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Map<String, TableFieldDTO> getFiledMapping() {
		Map<String, TableFieldDTO> fieldMap = new WeakHashMap<String, TableFieldDTO>();
		fieldMap.put("procedureNameValue", new TableFieldDTO("procedureNameValue",
				TextField.class, String.class, false));
		fieldMap.put("procedureCodeValue", new TableFieldDTO("procedureCodeValue",
				TextField.class, String.class, false));
		fieldMap.put("packageAmount", new TableFieldDTO("packageAmount",
				TextField.class, String.class, false));
		fieldMap.put("packageRate", new TableFieldDTO("packageRate",
				TextField.class, String.class, false));
		fieldMap.put("policyAging", new TableFieldDTO("policyAgeing",
				TextField.class, String.class, false));
		fieldMap.put("procedureStatus", new TableFieldDTO("procedureStatus",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("exclusionDetails", new TableFieldDTO("exclusionDetails",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("copay", new TableFieldDTO("copay",
				ComboBox.class, SelectValue.class, false));
		fieldMap.put("remarks", new TableFieldDTO("remarks",
				TextField.class, String.class, true, 100)); 
		return fieldMap;
	}

	@Override
	public void removeRow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTable() {
		this.errorMessages = new ArrayList<String>();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
		table.setContainerDataSource(new BeanItemContainer<ProcedureDTO>(
				ProcedureDTO.class));
		table.setWidth("100%");
		table.setVisibleColumns(NATURAL_COL_ORDER);

	}
	
	
	 public boolean isValid()
		{
			boolean hasError = false;
			errorMessages.removeAll(getErrors());
			@SuppressWarnings("unchecked")
			Collection<ProcedureDTO> itemIds = (Collection<ProcedureDTO>) table.getItemIds();
			for (ProcedureDTO procBean : itemIds) {
				Set<ConstraintViolation<ProcedureDTO>> validate = validator.validate(procBean);
				if(null == procBean.getExclusionDetails())
				{
					hasError = true;
					errorMessages.add("Please Select Exclusion details for procedure in procedure exclusion table");
				}
				if(null == procBean.getProcedureStatus())
				{
					hasError = true;
					errorMessages.add("Please Select procedure status for procedure in procedure exclusion table");
				}
				
				if(null == procBean.getCopay())
				{
					hasError = true;
					errorMessages.add("Please Select Copay for procedure in procedure exclusion table");
				}
				
				if (validate.size() > 0) {
					/*hasError = true;
					for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
						errorMessages.add(constraintViolation.getMessage());
					}*/
					//IMSSUPPOR-31515
					for (ConstraintViolation<ProcedureDTO> constraintViolation : validate) {
						if(constraintViolation.getMessage() != null && !constraintViolation.getMessage().isEmpty() &&
								!constraintViolation.getMessage().equalsIgnoreCase("Please Select Consider For Day Care.")){
							hasError = true;
							errorMessages.add(constraintViolation.getMessage());
						}
					}
				}
			}
			return !hasError;
		}
	 
		public List<String> getErrors()
		{
			return this.errorMessages;
		}

	@Override
	public void tableSelectHandler(ProcedureDTO t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String textBundlePrefixString() {
		// TODO Auto-generated method stub
		return "preauth-procedure-";
	}

	@Override
	public void deleteRow(Object itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProcedureDTO createInstance() {
		ProcedureDTO procedureDTO = new ProcedureDTO();
		procedureDTO.setStatusFlag(true);
		return procedureDTO;
	}

	@Override
	public List<ProcedureDTO> getValues() {
		Collection<ProcedureDTO> coll = (Collection<ProcedureDTO>) table.getItemIds();
		List list;
		if (coll instanceof List){
			list = (List)coll;
		}
		else{
			list = new ArrayList(coll);
		}
		return list;
	}

}
