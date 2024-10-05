package objectManager;

import org.openqa.selenium.WebDriver;
import pages.Login.LoginPage;


public class PageObjectManager {
    private WebDriver driver;
    LoginPage loginPage;
    public PageObjectManager(WebDriver driver){
        this.driver=driver;
     }
    public LoginPage getLoginPage(){
        return (loginPage ==null)? loginPage =new LoginPage(driver): loginPage;
    }
}
