package utils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;


public class ElementUtil {
	static WebDriver driver;
	JavascriptExecutor js;
	Logger log = LogManager.getLogger(ElementUtil.class);
	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		js = (JavascriptExecutor) driver;
	}

	public void launchUrl(String url) {
		log.info("Navigating to Url="+url);
		driver.get(url);
	}
	public String getTitleOfWebsite(){
		log.info("Website Page Title="+driver.getTitle());
		return driver.getTitle();
	}
	public String getUrlOfWebsite(){
		js=(JavascriptExecutor) driver;
		String url= (String) js.executeScript("return document.URL");
		log.info("Website URl="+url);
		return url;
	}
	public void switchWindow(String windID) {
		driver.switchTo().window(windID);
	}

	public WebElement getElement(By locator) {
		return driver.findElement(locator);
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public void doClickIfPresent(By locator) {
		try {
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			WebElement wb = getElement(locator);
			log.info("Clicking WebElement If present");
			wb.click();
		} catch (Exception e) {
			log.info("Element not found"+e.getStackTrace());
		}
	}

	public void doClick(By locator) {
		try {
			WebElement webElement = getElement(locator);
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(15));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.click();
		}catch (StaleElementReferenceException e){
			log.info("Stale element exception occurred retrying click");
			WebElement webElement = getElement(locator);
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.click();
		}
	}

	public void doSendKeys(By locator, String input) {
		WebElement webElement = getElement(locator);
		try{
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.clear();
			log.info("Entering Text in Field="+input);
			webElement.sendKeys(input);
		}catch (TimeoutException |StaleElementReferenceException|NoSuchElementException e){
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			webElement.clear();
			log.info("Entering Text in Field="+input);
			webElement.sendKeys(input);
		}catch (Exception e){
			log.info("Entering text using JS");
			sendKeysUsingJS(locator,input);
		}

	}

	public void doClear(By locator) {
		WebElement webElement = getElement(locator);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
		log.info("Clearing data in the text field");
		webElement.clear();
	}

	public String getTextOfElement(By locator) {
		try{
		WebElement webElement = getElement(locator);
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return webElement.getText();
		}catch (StaleElementReferenceException e){
			log.info("Stale element exception occured trying again");
			WebElement webElement = getElement(locator);
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return webElement.getText();
		}
	}

	public WebElement waitTillDisplay(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public void waitTillElementIsClickable(By locator){
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForElementToBeClickable(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	public void waitForWebElementToBeClickable(WebElement webElement) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
	}
	public boolean getDisplayStatus(By locator) {
		return waitTillDisplay(locator, 30).isDisplayed();
	}

	public void selectItemInElementList(By locator, String itemName) {
		List<WebElement> filterOption = getElements(locator);
		for (WebElement option : filterOption) {
			log.info("Values from list:"+option.getText());
			if (option.getText().equalsIgnoreCase(itemName)) {
				option.click();
				break;
			}
		}
	}

	public void selectItemWhenContainsInElementList(By locator, String itemName) {
		List<WebElement> filterOption = getElements(locator);
		for (WebElement option : filterOption) {
			log.info("Values from list:"+option.getText());
			if (option.getText().contains(itemName)) {
				option.click();
				break;
			}
		}
	}
	public void clickOnFirstElementFromList(By locator){
		waitTillDisplay(locator,5);
		List<WebElement> filterOption = getElements(locator);
		WebElement element=filterOption.get(0);
		log.info("Clicking on first element from list ="+element.getText());
		element.click();
	}

	public By getLocatorByXpath(String xpathStr){
        return By.xpath(xpathStr);
	}

	public WebElement getWebElement(By locator){
		return driver.findElement(locator);
	}

	public void clickOnSearchResult(By locator){
		List<WebElement> filterOption = getElements(locator);
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		WebElement element=filterOption.get(0);
		try {
			log.info("Clicking on first result using JS executor ="+element.getText());
			safeJavaScriptClick(element);
			Thread.sleep(3000);
		} catch (Exception e) {
			log.info("Unable to click "+ e.getStackTrace());
		}
	}
	public void safeJavaScriptClick(WebElement element){
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				log.info("Clicking on element with using java script executor");
				js.executeScript("arguments[0].click();", element);
			} else {
				log.info("Unable to click on element");

			}
		} catch (StaleElementReferenceException e) {
			log.info("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			log.info("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			log.info("Unable to click on element "+ e.getStackTrace());
		}
	}
	public void safeJSClickUsingLocator(By locator){
		WebElement element=getWebElement(locator);
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				log.info("Clicking on element with using java script executor");
				js.executeScript("arguments[0].click();", element);
			} else {
				log.info("Unable to click on element");

			}
		} catch (StaleElementReferenceException e) {
			log.info("Element is not attached to the page document "+ e.getStackTrace());
		} catch (NoSuchElementException e) {
			log.info("Element was not found in DOM "+ e.getStackTrace());
		} catch (Exception e) {
			log.info("Unable to click on element "+ e.getStackTrace());
		}
	}
	public void scrollWebapage(){
		js.executeScript("window.scrollBy(0,500)", "");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void waitForWebPageLoad(){
		js.executeScript("return document.readyState").equals("complete");
	}
	public void waitForAbsenceOfLoadingMask(By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(java.time.Duration.ofSeconds(30))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(Exception.class);
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
			Boolean status;
			@Override
			public Boolean apply(WebDriver webDriver) {
				try {
					if (webDriver.findElement(locator).isDisplayed())
						status=false;
				} catch (Exception e) {
					status=true;
				}
				return status;
			}
		};
		wait.until(function);
	}
	public void waitForInvisibilityOfElementLocated(By locator){
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void selectElementByVisibleText(By locator, String text){
		Select select=new Select(getElement(locator));
		select.selectByVisibleText(text);
	}

	public boolean isElementisDisplayed(By locator){
		waitTillDisplay(locator,10);
		boolean status=getElement(locator).isDisplayed();
		return status;
	}
	public void sendKeysUsingJS(By locator, String textToSend ){
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "arguments[0].value = arguments[1];";
		js.executeScript(script, driver.findElement(locator), textToSend);
	}

	public void retryElementSendKeys(By locator,String balanceOfPreviousYear){
		retryElementEnter:
		for(int i=0;i<3;i++) {
			try {
				Thread.sleep(2000);
				waitTillElementIsClickable(locator);
				sendKeysUsingJS(locator, balanceOfPreviousYear);
				driver.findElement(locator).sendKeys(Keys.ENTER);
				driver.findElement(locator).sendKeys(Keys.TAB);
				break;
			} catch (ElementNotInteractableException | StaleElementReferenceException e) {
				log.info("ElementNotInteractableException/StaleElementReferenceException  exception occurred trying again");
				break retryElementEnter;
			}catch (InterruptedException e){
                log.info("error occured at wait", e.getMessage());
			}
		}
	}
	public void waitForLoadingMask() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(java.time.Duration.ofSeconds(60))
				.pollingEvery(Duration.ofSeconds(3))
				.ignoring(Exception.class);
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver webDriver) {


				return (driver.findElements(By.xpath("//div[@class='ext-el-mask']")).size() == 0);
			}
		};
		wait.until(function);
	}
	public void waitForLoadingMaskingJQ() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(java.time.Duration.ofSeconds(240))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(Exception.class);
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {
			Boolean status;
			@Override
			public Boolean apply(WebDriver webDriver) {
				try {
					if (webDriver.findElement(By.xpath("//div[@class='ext-el-mask-msg x-mask-loading']/div")).isDisplayed())
						status=false;
				} catch (Exception e) {
					status=true;
				}
				return status;
			}
		};
		wait.until(function);
	}
	public void waitForRetrievingJobData() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(java.time.Duration.ofSeconds(90))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(Exception.class);
		Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver webDriver) {

				return (driver.findElements(By.xpath("//*[text()='Retrieving Job Data...']")).size() == 0);
			}
		};
		wait.until(function);
	}
}

