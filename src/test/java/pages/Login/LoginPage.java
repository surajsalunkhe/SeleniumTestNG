package pages.Login;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utils.ElementUtil;

public class LoginPage {
    WebDriver driver;
    ElementUtil elementutil;
    Logger logger = LogManager.getLogger(LoginPage.class);
    public LoginPage(WebDriver driver){
        this.driver=driver;
        elementutil=new ElementUtil(driver);
        PageFactory.initElements(driver, this);
    }

    public void navigateToSite(String url){
        logger.info("Launching browser and navigating to URL= {}", url);
        driver.navigate().to(url);
    }

}
