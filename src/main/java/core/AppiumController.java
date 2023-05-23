package core;

import base.Const;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.xml.XmlTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class AppiumController {

    static AppiumController instance = new AppiumController();
    private final ThreadLocal<AppiumDriver> driverFactoryThread = new ThreadLocal<>();
    private AppiumDriver driver = null;

    synchronized AppiumDriver getDriver() {
        if (driverFactoryThread.get() == null) {
            try {
                startDefaultServer();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return driverFactoryThread.get();
    }

    synchronized void start(XmlTest xmlTest) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String platformName = xmlTest.getTestParameters().get(AndroidMobileCapabilityType.PLATFORM_NAME);
        URL serverUrl = new URL(xmlTest.getParameter("server"));
        if (platformName.equals("Android")) {
            capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, xmlTest.getTestParameters().get(AndroidMobileCapabilityType.APP_PACKAGE));
            capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, xmlTest.getTestParameters().get(AndroidMobileCapabilityType.APP_ACTIVITY));
            driver = new AndroidDriver(serverUrl, capabilities);
        } else if (platformName.equals("iOS")) {
            driver = new IOSDriver(serverUrl, capabilities);
        } else {
            driver = new AppiumDriver(serverUrl, capabilities);
        }
        driver.manage().timeouts().implicitlyWait(Const.TIME_OUT_MIN_ELEMENT, TimeUnit.SECONDS);
        driverFactoryThread.set(driver);
    }

    void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    private synchronized void startDefaultServer() throws MalformedURLException {
        XmlTest xmlTest = new XmlTest();
        xmlTest.setParameters(defaultAndroidParameters());
        start(xmlTest);
    }


    private Map<String, String> defaultAndroidParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(MobileCapabilityType.PLATFORM_NAME, "Android");
        parameters.put(MobileCapabilityType.DEVICE_NAME, "Pixel_6_Pro");
        parameters.put(MobileCapabilityType.PLATFORM_VERSION, "13");
        parameters.put(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        parameters.put(AndroidMobileCapabilityType.APP_PACKAGE, "io.selendroid.testapp");
        parameters.put(AndroidMobileCapabilityType.APP_ACTIVITY, "io.selendroid.testapp.HomeScreenActivity");
        parameters.put(MobileCapabilityType.APP, "/appfile/android/selendroid-test-app.apk");
        parameters.put("server", "http://127.0.0.1:4723/wd/hub");
        return parameters;
    }

}
