package cucumber;

import driverFactory.DriverManager;
import objectManager.PageObjectManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class TestContext {
    private PageObjectManager pageObjectManager;
    private DriverManager driverManager;
    Logger logger = LogManager.getLogger(TestContext.class);
    public TestContext(){
        driverManager=new DriverManager();
    }
    public void initializeDriver(String browser) {
        logger.info("Initializing the driver");
        driverManager.setDriver(browser);
        pageObjectManager = new PageObjectManager(driverManager.getDriver());
    }

    public WebDriver getDriver(){
        return  driverManager.getDriver();
    }

    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public void closeDriver() {
        logger.info("Closing the browser");
        driverManager.closeDriver();
    }

}
