package cazoo.Pages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import cazoo.util.CarConstants;
public class cazooPage {
	
	public static List<String> readCarRegInputTextFile() throws IOException {
		String regex="([A-Z]{2}[0-9]{2}[A-Z]{3})|(([A-Z]{2}[0-9]{2} [A-Z]{3}))";
		File file = new File(CarConstants.carInputFile);
		BufferedReader br= new BufferedReader(new FileReader(file));
		String st = null;
		
		List<String> l1 = new ArrayList<String>();
		while ((st= br.readLine()) != null) {
	        Pattern p=Pattern.compile(regex);
	        Matcher m = p.matcher(st);
	        
		    while(m.find()) {
		    	l1.add(m.group());
		    }   		
		}
		
	    return l1;
	}

	public static boolean enterCarReg (WebDriver driver,ExtentTest test,List<String> l1) throws IOException, InterruptedException {
		File file1 = new File(CarConstants.carOuputFile);
		BufferedReader br1= new BufferedReader(new FileReader(file1));
		String st1 = null;
		List<String> l2 = new ArrayList<String>();
		while ((st1= br1.readLine()) != null) {
			l2.add(st1);
		}

		boolean name = false,cmodel = false,cyear = false,validRegNo=false;

		for(int i=0;i<l1.size();i++) {
			driver.findElement(By.cssSelector("input[name='vrm']")).clear();	
			driver.findElement(By.cssSelector("input[name='vrm']")).sendKeys(l1.get(i));
			Thread.sleep(2000);
			driver.findElement(By.cssSelector("[data-test-id=valuations2] button[type=submit]")).click();
			Thread.sleep(3000);
			List<WebElement> validReg=driver.findElements(By.cssSelector("div[role=alert]"));
			if(validReg.size()>0) {
				String errorText=driver.findElement(By.cssSelector("div[role=alert] span")).getText();
				test.log(LogStatus.FAIL, "Invalid car registration number: "+l1.get(i));
				test.log(LogStatus.INFO,"Error message :"+errorText);
				System.out.println("Invalid car registration number: "+l1.get(i)+" - Error message: "+errorText);
				takeScreenShot(driver,test);
				validRegNo=false;
				driver.navigate().back();
				Thread.sleep(3000);
				continue;
			}
			Thread.sleep(3000);
			String carName=driver.findElement(By.cssSelector("#__next > div:nth-of-type(1) h1")).getText();
			String text2=driver.findElement(By.cssSelector("#__next > div:nth-of-type(1) h2:nth-of-type(2)")).getText();
			String modelBefore=text2.replace("|","");
			String model=modelBefore.trim().replaceAll(" +"," ");
			String text3=driver.findElement(By.cssSelector("#__next > div:nth-of-type(1) p strong")).getText();
			String yearBefore=text3.replace("|","");
			String year=yearBefore.trim().replaceAll(" +"," ");

			for(String ele:l2) {
				if(ele.contains(carName)) {
					name=true;
					break;
				}else {
					name=false;
				}
			}

			for(String ele:l2) {
				if(ele.contains(model)) {
					cmodel=true;
					break;
				}else {
					cmodel=false;
				}
			}

			for(String ele:l2) {
				if(ele.contains(year)) {
					cyear=true;
					break;
				}else {
					cyear=false;
				}
			}

			Thread.sleep(3000);

			if(name) {
				test.log(LogStatus.PASS, "Registration number:"+l1.get(i)+" CarName: "+carName+" verification passed");
				System.out.println("Registration number:"+l1.get(i)+" CarName: "+carName+" verification passed");
			}else{
				test.log(LogStatus.FAIL, "Registration number:"+l1.get(i)+" CarName: "+carName+" verification failed");
				System.out.println("Registration number:"+l1.get(i)+" CarName: "+carName+" verification failed");
			}
			if(cmodel) {
				test.log(LogStatus.PASS, "Registration number:"+l1.get(i)+" CarModel: "+model+" verification passed");
				System.out.println("Registration number:"+l1.get(i)+" CarModel: "+model+" verification passed");
			}else {
				test.log(LogStatus.FAIL, "Registration number:"+l1.get(i)+" CarModel: "+model+" verification failed");
				System.out.println("Registration number:"+l1.get(i)+" CarModel: "+model+" verification failed");
			}
			if(cyear) {
				test.log(LogStatus.PASS, "Registration number:"+l1.get(i)+" CarYear: "+year+" verification passed");
				System.out.println("Registration number:"+l1.get(i)+" CarYear: "+year+" verification passed");
			}else {
				test.log(LogStatus.FAIL, "Registration number:"+l1.get(i)+" CarYear: "+year+" verification failed");
				System.out.println("Registration number:"+l1.get(i)+" CarYear: "+year+" verification failed");
			}
			takeScreenShot(driver,test);
			driver.navigate().back();
		}

		return name&cmodel&cyear&validRegNo;

	}

	public static void takeScreenShot(WebDriver driver,ExtentTest test) {
		DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date d = new Date();
		String screenshotFile = sdf.format(d).toString().replace(":", "_").replace(" ", "_") + ".png";
		String filePath = CarConstants.reportPath+"screenshots/" + screenshotFile;
		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile,
					new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.log(LogStatus.INFO, test.addScreenCapture(filePath));
	}
	
}
