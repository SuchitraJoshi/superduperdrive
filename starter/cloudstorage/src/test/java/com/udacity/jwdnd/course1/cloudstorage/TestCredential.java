package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestCredential {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    public void beforeEach() {
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("start-maximized");
//        this.driver = new ChromeDriver(options);
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }



    @Test
    public void testCreateCredential() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getCredentialsTab();

        CredentialTestPage newCred= new CredentialTestPage("www.google.com","Randy", "Pearson");
        ((JavascriptExecutor)driver).executeScript("showCredentialModal()");
        webDriverWait = new WebDriverWait(driver, 2);
        fillCredentialForm(newCred);

        getCredentialsTab();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']")).isDisplayed());
        Assertions.assertFalse(driver.findElement(By.xpath("//td[text()='"+newCred.getCredentialUsername()+"']//following-sibling::td"))
                .getText().equals(newCred.getCredentialPassword()));
        //td[text()='aa']//following-sibling::td
    }

    @Test
    public void testUpdateCredential() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getCredentialsTab();
        // Insert Credential first
        CredentialTestPage newCred= new CredentialTestPage("www.yahoo.com","Randy", "Pearson");
        ((JavascriptExecutor)driver).executeScript("showCredentialModal()");
        webDriverWait = new WebDriverWait(driver, 2);
        fillCredentialForm(newCred);

        //Update the newly inserted credential.
        getCredentialsTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']//preceding-sibling::td/button[text()='Edit']")));
        WebElement updateButton = driver.findElement(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']//preceding-sibling::td/button[text()='Edit']"));
        updateButton.click();
        webDriverWait = new WebDriverWait(driver, 2);
        CredentialTestPage updateCred= new CredentialTestPage("www.geeks.com","Greg", "Heffley");

        fillCredentialForm(updateCred);

        getCredentialsTab();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+updateCred.getCredentialUrl()+"']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='"+updateCred.getCredentialUrl()+"']")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.xpath("//td[text()='"+updateCred.getCredentialUsername()+"']")).isDisplayed());
    }

    @Test
    public void testDeleteCredential() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getCredentialsTab();
        // Insert credential first
        CredentialTestPage newCred= new CredentialTestPage("www.hotmail.com","Randy", "Pearson");
        ((JavascriptExecutor)driver).executeScript("showCredentialModal()");
        webDriverWait = new WebDriverWait(driver, 2);
        fillCredentialForm(newCred);

        //delete the newly inserted note.
        getCredentialsTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']//preceding-sibling::td/a[text()='Delete']")));
        WebElement deleteLink = driver.findElement(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']//preceding-sibling::td/a[text()='Delete']"));
        deleteLink.click();
        webDriverWait = new WebDriverWait(driver, 2);
        getCredentialsTab();
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='"+newCred.getCredentialUrl()+"']"));});

    }


    private void getCredentialsTab(){
        // open homepage
        webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        //find note tab and click
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement tabCredentials = driver.findElement(By.id("nav-credentials-tab"));
        tabCredentials.click();
    }

    private void fillCredentialForm(CredentialTestPage credentialTestPage){

        WebElement modalContainer = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputCredUrl = modalContainer.findElement(By.id("credential-url"));
        inputCredUrl.click();
        inputCredUrl.clear();
        inputCredUrl.sendKeys(credentialTestPage.getCredentialUrl());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputCredUsername = modalContainer.findElement(By.id("credential-username"));
        inputCredUsername.click();
        inputCredUsername.clear();
        inputCredUsername.sendKeys(credentialTestPage.getCredentialUsername());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputCredPassword = modalContainer.findElement(By.id("credential-password"));
        inputCredPassword.click();
        inputCredPassword.clear();
        inputCredPassword.sendKeys(credentialTestPage.getCredentialUrl());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cred-submit")));
        List<WebElement> btnSubmitNote = modalContainer.findElements(By.id("cred-submit"));
        btnSubmitNote.get(0).click();
    }



    private void doSignUp(String firstName, String lastName, String userName, String password){
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign-up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depending on the rest of your code.
		*/
       // Assertions.assertTrue(driver.findElement(By.id("mySpan")).getText().contains("You successfully signed up!"));
    }
    private void doLogIn(String userName, String password)
    {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    private String getRandomUrl() {
        return  "www.Google.com";
    }
    private String getRandomUsername() {
        return "myUsername";
    }
    private String getRandomPassword() {
        return "myPassword";
    }




}