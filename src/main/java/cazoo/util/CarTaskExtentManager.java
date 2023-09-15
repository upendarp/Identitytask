package cazoo.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class CarTaskExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance(String fileName) {
		if (extent == null) {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d=new Date();
			String reportPath =CarConstants.reportPath+fileName + "_" + 
				sdf.format(d).toString().substring(4).replace(":", "").replace(" ", "").replace("-", "") + ".html";
 
			extent = new ExtentReports(reportPath, true, DisplayOrder.NEWEST_FIRST);
		
			extent.loadConfig(new File(CarConstants.reportsConfig));
			
			// optional
			extent.assignProject("IdentityE2ETask");
		}
		return extent;
	}
	
}
