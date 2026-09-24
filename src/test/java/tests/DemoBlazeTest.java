package tests;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DemoBlazeTest {

    private WebDriver driver;
    private WebDriverWait wait;

    //  static constants for test inputs
    private static final String BASE_URL = "https://www.demoblaze.com/";
    private static final String TEST_NAME = "Test Student";
    private static final String TEST_COUNTRY = "Sri Lanka";
    private static final String TEST_CITY = "Colombo";
    private static final String TEST_CARD = "4111111111111111";
    private static final String TEST_MONTH = "12";
    private static final String TEST_YEAR = "2027";

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }


     //TC01

    @Test
    public void tc01_homePageSmokeTest() {
        String title = driver.getTitle();
        Assert.assertNotNull(title, "Page title should not be null.");
        Assert.assertFalse(title.trim().isEmpty(), "Page title should not be empty.");

        WebElement brandHeading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("nava"))
        );
        Assert.assertTrue(brandHeading.isDisplayed(), "PRODUCT STORE heading is not displayed.");
        Assert.assertTrue(
                brandHeading.getText().contains("PRODUCT STORE"),
                "Brand heading text does not contain 'PRODUCT STORE'."
        );
    }


     //TC02 - Product Selection

    @Test
    public void tc02_productSelectionTest() {
        WebElement phonesCategory = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Phones"))
        );
        phonesCategory.click();

        WebElement phoneItem = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))
        );
        phoneItem.click();

        WebElement productTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='name']"))
        );
        Assert.assertEquals(
                productTitle.getText(),
                "Samsung galaxy s6",
                "Product title does not match target selection."
        );

        WebElement productPrice = driver.findElement(By.xpath("//h3[@class='price-container']"));
        Assert.assertTrue(productPrice.isDisplayed(), "Price container is not visible.");
        System.out.println("Product Price: " + productPrice.getText());
    }


     //TC03 - Add to Cart


    @Test
    public void tc03_addToCartTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();

        WebElement addToCartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))
        );
        addToCartBtn.click();

        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        System.out.println("Alert message displayed: " + alertText);

        Assert.assertTrue(
                alertText.toLowerCase().contains("added"),
                "Unexpected alert text received: " + alertText
        );
        alert.accept();
    }

 // TC04 - Cart Management

    @Test
    @SuppressWarnings("unchecked")
    public void tc04_cartManagementTest() {
        // Add Item 1: Samsung galaxy s6
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Return Home and Add Item 2: Nokia lumia 1520
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nava"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nokia lumia 1520"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        // Navigate to Cart Page
        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();

        // Wait for 2 cart rows to appear in the table
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody[@id='tbodyid']/tr"), 2));

        List cartRows = driver.findElements(By.xpath("//tbody[@id='tbodyid']/tr"));
        System.out.println("Initial cart row count: " + cartRows.size());
        Assert.assertEquals(cartRows.size(), 2, "Cart should initially contain 2 items.");

        for (int i = 0; i < cartRows.size(); i++) {
            WebElement row = (WebElement) cartRows.get(i);
            String name = row.findElement(By.xpath("./td[2]")).getText();
            String price = row.findElement(By.xpath("./td[3]")).getText();
            System.out.println("Cart Item -> Name: " + name + " | Price: $" + price);
        }

        // Delete Nokia lumia 1520
        WebElement deleteNokiaBtn = driver.findElement(
                By.xpath("//tr[td[contains(text(),'Nokia lumia 1520')]]//a[text()='Delete']")
        );
        deleteNokiaBtn.click();

        // Explicit Synchronization + Type Cast for Java 21+ compiler compatibility
        List remainingRows = (List) (Object) wait.until(
                ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody[@id='tbodyid']/tr"), 1)
        );

        Assert.assertEquals(remainingRows.size(), 1, "Cart should contain 1 item after deletion.");

        WebElement remainingRow = (WebElement) remainingRows.get(0);
        String remainingProductName = remainingRow.findElement(By.xpath("./td[2]")).getText();
        Assert.assertEquals(
                remainingProductName,
                "Samsung galaxy s6",
                "Remaining product in cart is incorrect."
        );

        WebElement totalElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("totalp"))
        );
        System.out.println("Cart Total Price: $" + totalElement.getText());
        Assert.assertFalse(totalElement.getText().isEmpty(), "Cart total should not be empty.");
    }

//TC05 - Checkout Validation

    @Test
    public void tc05_checkoutValidationTest() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Samsung galaxy s6"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("cartur"))).click();
        wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Place Order']"))
        ).click();

        //  A: Negative Validation
        WebElement purchaseBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Purchase']"))
        );
        purchaseBtn.click();

        Alert validationAlert = wait.until(ExpectedConditions.alertIsPresent());
        String validationText = validationAlert.getText();
        System.out.println("Invalid Form Alert Message: " + validationText);
        Assert.assertTrue(
                validationText.toLowerCase().contains("fill out"),
                "Expected mandatory field error alert message."
        );
        validationAlert.accept();

        //  B: Positive Purchase Submission
        driver.findElement(By.id("name")).sendKeys(TEST_NAME);
        driver.findElement(By.id("country")).sendKeys(TEST_COUNTRY);
        driver.findElement(By.id("city")).sendKeys(TEST_CITY);
        driver.findElement(By.id("card")).sendKeys(TEST_CARD);
        driver.findElement(By.id("month")).sendKeys(TEST_MONTH);
        driver.findElement(By.id("year")).sendKeys(TEST_YEAR);

        purchaseBtn.click();

        WebElement successHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'sweet-alert')]//h2"))
        );
        String headerText = successHeader.getText();
        System.out.println("Purchase Header Text: " + headerText);
        Assert.assertTrue(
                headerText.contains("Thank you for your purchase!"),
                "Purchase success header does not match expected output."
        );

        driver.findElement(By.xpath("//button[contains(@class, 'confirm')]")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}