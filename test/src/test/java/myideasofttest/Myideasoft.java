package myideasofttest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Myideasoft {
    WebDriver driver;
    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get("https://testcase.myideasoft.com/");
        driver.manage().window().maximize();
    }

    @Test
    public void search_product() {
        WebElement searchBox = driver.findElement(By.xpath("//input[@class='auto-complete']"));
        searchBox.sendKeys("ürün");
        WebElement searchButton = driver.findElement(By.xpath("//button[1]"));
        searchButton.click();
        WebElement product = driver.findElement(By.xpath("//a[@title='Ürün']"));
        product.click();
        WebElement productQuantityInput = driver.findElement(By.xpath("//select[@id='qty-input']"));
        String requestedItemQuantity =  "5";
        productQuantityInput.sendKeys(requestedItemQuantity);
        WebElement addToCart = driver.findElement(By.xpath("//a[@class='add-to-cart-button']"));
        addToCart.click();
        WebElement cartConfirmationMessage = driver.findElement(By.xpath("//div[@class='shopping-information-cart']"));
        if (cartConfirmationMessage.isDisplayed()) {
            System.out.println("Product added to cart successfully.");
        } else {
            System.out.println("An error occurred when product adding to cart");
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4)); //https://stackoverflow.com/questions/38327049/check-if-element-is-clickable-in-selenium-java
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".shopping-information-cart")));
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Sepet']")));
        cart.click();
        WebElement cartProductQuantityElement = driver.findElement(By.xpath("//div[@class='cart-item']//input[@data-stocktype='Piece']"));
        String productQuantityText = cartProductQuantityElement.getAttribute("value");
        int productQuantity = Integer.parseInt(productQuantityText.trim());
        int requestedItemQuantityInt = Integer.parseInt(requestedItemQuantity.trim());
        if (productQuantity == requestedItemQuantityInt) {
            System.out.println("Product count is right");
            System.out.println(productQuantity);
        } else {
            System.out.println("Product count is wrong");
        }
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}