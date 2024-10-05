package stepDef;

import cucumber.TestContext;
import driverFactory.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.Login.LoginPage;
import utils.PropertiesFileManager;

public class StepDefinition {
    TestContext testContext;
    LoginPage loginPage;
    public static String environmentName=null;
    public StepDefinition(TestContext context){
        this.testContext = context;
    }
    @Given("User navigates to URL {string}")
    public void user_navigate_to_url(String url) {
        String testUrl= PropertiesFileManager.getPropertyValue(url);
        loginPage.navigateToSite(testUrl);
    }
    @Given("User launch the browser")
    public void user_launch_the_browser() {
        environmentName= DriverManager.returnEnvironment();
        String browserName=DriverManager.returnBrowserName();
        //DriverManager.setDriver(browserName);
        testContext.initializeDriver(browserName);
        loginPage =testContext.getPageObjectManager().getLoginPage();
    }


    @Then("User close the browser")
    public void user_close_the_browser() {
        testContext.closeDriver();
    }




}
