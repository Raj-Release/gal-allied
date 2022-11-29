//package testingviews;
//
//import com.shaic.arch.table.GBaseTable;
//import com.shaic.main.navigator.ui.MenuPresenter;
//import com.vaadin.v7.data.util.BeanItemContainer;
//
//public class TestTable extends
//		GBaseTable<TestTableDTO> {
//	public static final Object[] NATURAL_COL_ORDER = new Object[] { "sno",
//			"intimationNo", "claimNo", "policyNo", "insuredPatientName",
//			"lob", "productCode", "productName" };
//
//	@Override
//	public void removeRow() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void initTable() {
//
//		table.setContainerDataSource(new BeanItemContainer<TestTableDTO>(
//				TestTableDTO.class));
//		table.setVisibleColumns(NATURAL_COL_ORDER);
//
//	}
//
//	@Override
//	public void tableSelectHandler(TestTableDTO t) {
//		fireViewEvent(MenuPresenter.FILE_UPLOAD_TABLE, t);
//
//	}
//
//	@Override
//	public String textBundlePrefixString() {
//		return "search-processtranslation-";
//	}
//
//}
