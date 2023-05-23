package base;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    private MobileDriver driver;

    public abstract boolean isPageDisplayed();

    public abstract BasePage open();

    public abstract BasePage waitForPageDisplayed();

    public BasePage(MobileDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    public MobileDriver getDriver() {
        return driver;
    }

    public void waitForElementDisplay(MobileElement element) {
        try {
            new WebDriverWait(getDriver(), Const.TIME_OUT_NORMAL_ELEMENT).until(
                    driver -> isElementDisplayed(element));
        } catch (Exception e) {
            // no-opt
        }
    }

    public void waitForElementHide(MobileElement element) {
        try {
            new WebDriverWait(getDriver(), Const.TIME_OUT_MAX_ELEMENT).until(
                    driver -> !isElementDisplayed(element));
        } catch (Exception e) {
            // no-opt
        }
    }

    public boolean isForElementPresent(MobileElement element) {
        return isElementDisplayed(element);
    }

    public boolean isElementDisplayed(MobileElement element) {
        try {
           waitForElementDisplay(element);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public BasePage inputTxtToElement(String txt, MobileElement element) {
        element.clear();
        element.sendKeys(txt);
        return this;
    }
}
