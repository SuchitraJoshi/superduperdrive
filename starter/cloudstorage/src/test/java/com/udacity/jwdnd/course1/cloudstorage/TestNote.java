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
class TestNote {
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
    public void testCreateNote() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getNotesTab();

        ((JavascriptExecutor)driver).executeScript("showNoteModal()");
        webDriverWait = new WebDriverWait(driver, 2);

        NoteTestPage newNote= new NoteTestPage("new Title","This is the description ");
         fillNoteForm(newNote);

        getNotesTab();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']")).isDisplayed());

    }

    @Test
    public void testUpdateNote() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getNotesTab();
        // Insert Note first
        NoteTestPage newNote= new NoteTestPage("new Title for update test","This is the description ");
        ((JavascriptExecutor)driver).executeScript("showNoteModal()");
        webDriverWait = new WebDriverWait(driver, 2);

        fillNoteForm(newNote);

        //Update the newly inserted note.
        getNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']//preceding-sibling::td/button[text()='Edit']")));
        WebElement updateButton = driver.findElement(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']//preceding-sibling::td/button[text()='Edit']"));
        updateButton.click();
        webDriverWait = new WebDriverWait(driver, 2);
        NoteTestPage updateNote= new NoteTestPage("Updated Title","This description has been updated. ");

        fillNoteForm(updateNote);

        getNotesTab();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+updateNote.getNoteTitle()+"']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//th[text()='"+updateNote.getNoteTitle()+"']")).isDisplayed());

    }

    @Test
    public void testDeleteNote() {
        doSignUp("URL", "Test", "UT", "123");

        doLogIn("UT", "123");

        getNotesTab();
        // Insert Note first
        NoteTestPage newNote= new NoteTestPage("new Title for delete test","This is the description ");
        ((JavascriptExecutor)driver).executeScript("showNoteModal()");
        webDriverWait = new WebDriverWait(driver, 2);

        fillNoteForm(newNote);

        //delete the newly inserted note.
        getNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']//preceding-sibling::td/a[text()='Delete']")));
        WebElement deleteLink = driver.findElement(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']//preceding-sibling::td/a[text()='Delete']"));
        deleteLink.click();
        webDriverWait = new WebDriverWait(driver, 2);
        getNotesTab();
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='"+newNote.getNoteTitle()+"']"));});


    }


    private void getNotesTab(){
        // open homepage
        webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        //find note tab and click
         webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement tabNotes = driver.findElement(By.id("nav-notes-tab"));
        tabNotes.click();
    }

    private void fillNoteForm(NoteTestPage noteTestPage){


        WebElement modalContainer = webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputNoteTitle = modalContainer.findElement(By.id("note-title"));
        inputNoteTitle.click();
        inputNoteTitle.clear();
        inputNoteTitle.sendKeys(noteTestPage.getNoteTitle());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputNoteDesc = modalContainer.findElement(By.id("note-description"));
        inputNoteDesc.click();
        inputNoteDesc.clear();
        inputNoteDesc.sendKeys(noteTestPage.getNoteDescription());

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class=\"btn btn-primary\"]")));
        List<WebElement> btnSubmitNote = modalContainer.findElements(By.xpath("//button[@class=\"btn btn-primary\"]"));
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

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        //Assertions.assertTrue(driver.findElement(By.id("mySpan")).getText().contains("You successfully signed up!"));

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


}
