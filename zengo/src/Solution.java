// Liran Bashari- liranbashari1@gmail.com
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
 *  This class will perform an automation test on the following scenarios:
 *  1. Launch the ZenGo website "www.zengo.com"
 *  2. Verify that the site is displayed successfully
 *  3. Get to the menu item “Features” and press “Buy”
 *  4. Verify that you were redirected to https://zengowallet.banxa.com/
 *  5. Verify that ZenGo logo is displayed successfully
 *  6. Get back to Home page
 *  7. Close browser
 */

public class Solution {

    /*
     * This method verifies the page, checks the title and url.
     */
    private static boolean verifyPage(WebDriver driver, String expectedUrl){
        try{
            // Check the url
            if (!(expectedUrl.equals(driver.getCurrentUrl()))){
                System.out.println("Didn't navigate to correct web page");
                return false;
            }
            // Check the title
            String actualTitle = driver.getTitle();
            if (!(actualTitle.equals("ZenGo - Simple & Secure Crypto Wallet App"))){
                System.out.println("Didn't have the expected title");
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("The site is displayed successfully");
        return true;
    }


    /*
     * This method will check the ZenGo's logo and will return "true" if this is the correct logo, else it will return "false".
     */
    private static boolean checkLogo(WebDriver driver){
        try{
            WebElement imageFile = driver.findElement(By.xpath("//*[@id='app']/div/div/img[2]"));
            // Javascript executor to check logo
            Boolean imagePresent  = (Boolean) ((JavascriptExecutor)driver) .executeScript("return arguments[0].complete " +
                    "&& typeof arguments[0].naturalWidth != \"undefined\" " + "&& arguments[0].naturalWidth > 0", imageFile);
            // verify if status is true or not
            if (imagePresent) {
                System.out.println("The logo is displayed correctly");
                return true;
            } else {
                System.out.println("The logo is displayed incorrectly");
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    /*
     * This method will wait until the menu id is available and then will click the button.
     */
    private static void waitUntilLoad(WebDriver driver, String id){
        try {
            // When we are in full screen the menu is different, if is not full screen:
            if (driver.findElement(By.id(id)).isDisplayed()){
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement elem = driver.findElement(By.id(id));
                elem.click();
                wait.until(ExpectedConditions.visibilityOf(elem));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /*
     * This is the main method- the method will test the scenarios described in the assignment,
     *  call a different method that will help me check the scenarios.
     */
    public static void main(String[] args){
        try{
            // Set property
            System.setProperty("webdriver.chrome.driver", "C:/webDriverChrom/chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            System.out.println("Starting web driver:");

            /*
             * 1. Launch the ZenGo website www.zengo.com:
             */
            String url = "https://zengo.com/";
            driver.get(url);
            // Wait until we can see the site and load the page correctly.
            TimeUnit.SECONDS.sleep(2);

            /*
             * 2. Verify that the site is displayed successfully
             */
            if (!(verifyPage(driver, url))){
                System.out.println("The verification of the page has failed!");
                driver.quit();
            }

            /*
             * 3. Get to the menu item “Features” and press “Buy”
             */
            // Wait until the menu will be available
            String menuId = "nav-toggle";
            waitUntilLoad(driver, menuId);
            // Navigate to Buy
            driver.findElement(By.xpath("//*[@id='menu-item-13191']/button")).sendKeys(Keys.RETURN);
            // Click Buy
            TimeUnit.SECONDS.sleep(1);
            driver.findElement(By.xpath("//*[@id='menu-item-10474']/a")).click();

            /*
             * 4. Verify that you were redirected to https://zengowallet.banxa.com/
             */
            String expectedUrl = "https://zengowallet.banxa.com/";
            boolean redirect = false;
            Set<String> tabs = driver.getWindowHandles();
            for (String handle : tabs) {
                driver.switchTo().window(handle);
                if ((expectedUrl.equals(driver.getCurrentUrl()))) {
                   redirect = true;
                }
            }
            if (!redirect){
                System.out.println("Didn't navigate to correct web page");
                driver.quit();
            } else {
                System.out.println("redirected to https://zengowallet.banxa.com/");
            }

            TimeUnit.SECONDS.sleep(5);

            /*
             * 5. Verify that ZenGo logo is displayed successfully
             */
            if(!(checkLogo(driver))){
                System.out.println("The verification of the ZenGo logo has failed!");
                driver.quit();
            }

            /*
             * 6. Get back to Home page
             */
            // Now close the tab and switch back to main screen
            driver.close();
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                if ((url.equals(driver.getCurrentUrl()))) {
                    driver.switchTo().window(handle);
                }
            }
            driver.navigate().to(url);
            TimeUnit.SECONDS.sleep(2);

            /*
             * 7. Close browser
             */
            driver.quit();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
