package appHooks;

import java.io.File;
import java.io.IOException;


import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import cucumber.TestContext;
import driverFactory.DriverManager;
import io.cucumber.java.AfterStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.testng.annotations.BeforeSuite;
import utils.Constants;
import utils.PropertiesFileManager;
import utils.SendTestAutomationReport;


public class ApplicationHook {
	TestContext testContext;
	WebDriver driver;
	Logger log = LogManager.getLogger(ApplicationHook.class);
	//private String browserName=System.getProperty("browser");
	public ApplicationHook(TestContext context){
		testContext=context;
	}

	@Before
	public void setUpWebDriver()
	{
		/*if(browserName==null || browserName.isEmpty()){
			browserName=PropertiesFileManager.getPropertyValue("broswerName");
		}
		 driverfactory=new DriverFactory();
		 driver=driverfactory.init_Driver(browserName);*/
	}
	
	@AfterStep
	public void captureScreenshot(Scenario sc) throws IOException
	{
		driver= testContext.getDriver();
		if(PropertiesFileManager.getPropertyValue("ATTACH_SCREENSHOT_TO_REPORT").equalsIgnoreCase("Yes")){
			if(driver!=null){
			String screenshotName = sc.getName().replace("", "").replace(":", "").replaceAll("[^a-zA-Z0-9]","_");
			File src=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			//FileUtils.copyFile(src,new File(Constants.SCREENSHOTS + screenshotName+ ".png"));
			final byte[] screenshot= ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			sc.attach(screenshot, "image/png", screenshotName);
			}
		}
	}

	@After
	public void tearDown(Scenario sc){
		driver= testContext.getDriver();
		if(driver!=null){
			//sc.log("Log generated using scenario Instance");
			//ExtentCucumberAdapter.getCurrentStep().log(Status.WARNING, "Browser will get closed");
			if(sc.isFailed()) {
				log.info("Test case Failed, Taking screenshot");
				String screenshotName = sc.getName().replace("", "").replace(":", "").replaceAll("[^a-zA-Z0-9]","_");
				File src=((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				//FileUtils.copyFile(src,new File(Constants.SCREENSHOTS + screenshotName+ ".png"));
				final byte[] screenshot= ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				sc.attach(screenshot, "image/png", screenshotName);
				//ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Test Case Failed");
			}else {
				//ExtentCucumberAdapter.addTestStepLog("Test got passed");
				log.info("Test case Passed, Updating Result");
				//ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "Test Case Passed");
			}
		}
		if(driver!=null){
			log.info("Quitting web driver");
			driver.quit();
		}
	}
}
