package jsfiddle;

import com.codeborne.selenide.*;
import io.github.vinogradoff.testdatabroker.client.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class SaveCodeSnippet {

    TestDataBrokerClient broker = new DefaultTestDataBrokerClient();

    @BeforeAll
    static void login() {
        open("https://jsfiddle.net/user/login/");
        $("#id_username").setValue("brokker");
        $("#id_password").setValue("brokker123").pressEnter();
    }

    @RepeatedTest(3)
    void saveSnippetAndWriteIdToBroker() throws IOException {
        var snippet = "databrokker" + System.currentTimeMillis();
        $("#editor .panel").click();
        actions().sendKeys("Hello " + snippet).perform();
        $("#save").click();
        $("#fork").shouldBe(visible);
        var url = WebDriverRunner.getWebDriver().getCurrentUrl();
        var urlParts = url.split("/");
        var fiddleId = urlParts[urlParts.length - 1];
        System.out.println(fiddleId);
        broker.writeData("jsfiddle.net", "fiddleId", fiddleId);
        open("https://jsfiddle.net");
    }
}
