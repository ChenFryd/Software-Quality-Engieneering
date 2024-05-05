package hellocucumber;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class MoodleActuator {
    private WebDriver driver;
    private WebDriverWait wait;


    public void initSession(String webDriver, String path){
        // webDriver = "webdriver.chrome.driver"
        System.setProperty(webDriver, path);

        // new chrome driver object
        this.driver = new ChromeDriver();

        // new web driver wait -> waits until element are loaded (40 sec max)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // launch website -> localhost
        driver.get("http://localhost/?lang=en");

        // maximize the window - some web apps look different in different sizes
        driver.manage().window().maximize();

        System.out.println("Driver setup finished for - " + driver.getTitle());
    }

    public void goToLogin(){
        // locate and click on web element -> login
        driver.findElement(By.linkText("Log in")).click();

    }

    public void enterLoginInfo(String username, String password) {
        // locate the username input box and enter username
        // $x("//*[@id='username']")
        // driver.findElement(By.xpath("//*[@id='username']")).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='username']"))).sendKeys(username);

        // locate the password input box and enter password
        // $x("//*[@name='password' and @type='password']")
        driver.findElement(By.xpath("//*[@name='password' and @type='password']")).sendKeys(password);

        // locate Log in button and press
        // $x("//*[@id='loginbtn']")
        driver.findElement((By.xpath("//*[@id='loginbtn']"))).click();
     //   driver.findElement(By.id("loginbtn")).click();
    }


    public void goToCourse(String courseName){
        // find course -> click on it
        // $x("//*[@class='multiline' and contains(text(),'Demo course')]")[0].click()
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//html/body/div[2]/div[3]/div/div[2]/div/section/div/aside/section/div/div/div[1]/div[2]/div/div/div[1]/div/div/div/div[1]/div/div/a/span[3]/span[2][contains(text(),'" + courseName + "')]"))).click();
    }

    public void generalWelcomeMessage(String welcomeWord) {
        driver.findElement(By.xpath("/html/body/div[2]/div[4]/div/header/div/div[2]/h2[contains(text(),'Hi, " + welcomeWord + "!')]"));
    }


    public void goToCoursePage() {
       driver.findElement(By.linkText("My courses")).click();
    }

    public void goToForum() {
        driver.findElement(By.cssSelector("a[href='http://localhost/mod/forum/view.php?id=2']")).click();

    }
    public void clickSubscribe(String discussionName) {
        WebElement element = driver.findElement(By.xpath("//div[@class='d-inline custom-control custom-switch mb-1']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    public void goToDiscussion(String discussionName) {
        WebElement element = driver.findElement(By.xpath("//a[@aria-label='Grades']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    public void deleteDiscussion() {
        WebElement element = driver.findElement(By.linkText("Delete"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    public void pressContinue() {
        WebElement element = driver.findElement(By.xpath("//form[@method='post' and @action='post.php']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().perform();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getClassName(){
        WebElement element = driver.findElement(By.xpath("//div[@aria-live='polite']"));
        String className = element.getAttribute("class");
        return className;
    }

    public String getConfirmText(){
        WebElement element = driver.findElement(By.xpath("//*[@class='notifications']"));
        String className = element.getAttribute("class");
        return className;
    }

}
