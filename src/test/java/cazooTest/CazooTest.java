package cazooTest;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import cazoo.Pages.cazooPage;
import cazoo.util.CarConstants;
import cazoo.util.CarTaskExtentManager;

public class CazooTest {
	public ExtentReports extent =CarTaskExtentManager.getInstance("CazooTest");
	public ExtentTest test;
	public ChromeDriver driver;
	
	@Test()
	 public void A1_OpenURL() throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", CarConstants.CHROME_DRIVER_EXE);
		test=extent.startTest("CazooTest");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(CarConstants.Cazoo_URL);
		test.log(LogStatus.INFO, "Cazoo website open");
		// Accept Cookie
		List<WebElement> cookies=driver.findElements(By.cssSelector("[data-test-id=banner-wrapper-with-blocker] div:nth-of-type(2)>div>div>button span"));
		if(cookies.size()>0)
		driver.findElement(By.cssSelector("[data-test-id=banner-wrapper-with-blocker] div:nth-of-type(2)>div>div>button span")).click();
		String carValuationText=driver.findElement(By.cssSelector("[data-test-id='valuations2']>div>div>div>article>div:nth-of-type(1) h2")).getText();
		if(carValuationText.contains("Get a car valuation")) {
			test.log(LogStatus.PASS, "Cazoo website title verification successfull");
			System.out.println("Cazoo website title verification successfull");
		}else {
			test.log(LogStatus.FAIL, "Cazoo website title verification Failed");
		}
		
	 }
	@Test()
	 public void B1_EnterCarReg () throws IOException, InterruptedException {
		List<String> l1=cazooPage.readCarRegInputTextFile();
		boolean result=cazooPage.enterCarReg(driver,test,l1);	    
		Assert.assertTrue(result,"Car details search is successfull");
	}
	
	
	 @AfterTest
		public  void quit(){
		 if(extent!=null){
		 extent.endTest(test);
		 extent.flush();
		 } 
		 if(driver!=null)
    	 driver.close();
		 }

}
