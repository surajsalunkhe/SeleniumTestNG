package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import utils.Constants;
import utils.PropertiesFileManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utils.Constants.TEST_RESULT_JSON;

@CucumberOptions(
		features = "src/test/resources/features",
		glue = {"stepDef","appHooks"},
		plugin = {"pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"json:target/json-report/cucumber.json",
				"html:target/cucumber-reports.html",
				"rerun:target/failedrerun.txt"},
		tags = "@QA"
)
public class TestRunner extends AbstractTestNGCucumberTests {
	Logger log = LogManager.getLogger(TestRunner.class);
	//WebDriver driver;
	//DriverManager driverManager=new DriverManager();

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}



	@BeforeSuite
	public void clearReportFolder() throws Exception {
		log.info("Inside Clear report in before suite");
		clearFolder();
		//pauseExistingJob();

	}
	public void clearFolder(){
		File myfile = new File(System.getProperty("user.dir")+Constants.REPORT_FOLDER);
		File downloadedReports=new File(System.getProperty("user.dir")+Constants.REPORT_DOWNLOAD_FOLDER);
		try {
			FileUtils.deleteDirectory(myfile);
			FileUtils.deleteDirectory(downloadedReports);
			File reportDownloadFolderCreate = new File(System.getProperty("user.dir")+Constants.REPORT_DOWNLOAD_FOLDER);
			boolean bool = reportDownloadFolderCreate.mkdir();
			if(bool){
				log.info("Folder is created successfully");
			}else{
				log.info("Error Found!");
			}
		} catch (IOException e) {
			log.info("Folder not found for deletion");
		}
	}
	@AfterSuite
	public void updateJiraStatus() {
		if(PropertiesFileManager.getPropertyValue("JiraUpdate").equalsIgnoreCase("Yes")){
			RestAssured.baseURI = PropertiesFileManager.getPropertyValue("JiraAPIURL");
			String jsonFile = System.getProperty("user.dir") + TEST_RESULT_JSON;
			try{
				String userId=PropertiesFileManager.getPropertyValue("JiraUserID");
				String password=PropertiesFileManager.getPropertyValue("JiraPassword");
				String endpoint="/import/execution/cucumber";
				String payload =readCucumberResultsFile(jsonFile);
						Response response = RestAssured.given()
						.header("Content-Type","application/json")
						.auth()
						.preemptive()
						.basic(userId,password)
						.body(payload)
						.when()
						.post(endpoint)
						.then()
						.extract()
						.response();
				int statusCode = response.getStatusCode();
				log.info("After Test Execution Test Result updated in JIRA Status code: " + statusCode);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}else {
			log.info("Jira Updated is disabled, you can enable it from environment.properties file");
		}
	}

	private String readCucumberResultsFile(String jsonFile) throws IOException {
		return new String(Files.readAllBytes((Paths.get(jsonFile))));
	}
}
