package testingviews;

import java.util.List;

import com.shaic.arch.GMVPView;

public interface TestView extends GMVPView {
	public void list(List<TestTableDTO> tableRows);	

}
