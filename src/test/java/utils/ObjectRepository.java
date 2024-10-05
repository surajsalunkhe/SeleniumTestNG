package utils;

import org.openqa.selenium.By;

public class ObjectRepository {
    public  static By getLocator(String elementName) {
        String locator = PropertiesFileManager.getPropertyValue(elementName);
        if (locator == null) {
            throw new IllegalArgumentException("Locator for element '" + elementName + "' not found.");
        }
        String[] locatorParts = locator.split(":", 2);
        String locatorType = locatorParts[0];
        String locatorValue = locatorParts[1];

        switch (locatorType.toLowerCase()) {
            case "id":
                return By.id(locatorValue);
            case "name":
                return By.name(locatorValue);
            case "xpath":
                return By.xpath(locatorValue);
            case "css":
                return By.cssSelector(locatorValue);
            case "class":
                return By.className(locatorValue);
            case "linktext":
                return By.linkText(locatorValue);
            case "partiallinktext":
                return By.partialLinkText(locatorValue);
            default:
                throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        }

    }
}
