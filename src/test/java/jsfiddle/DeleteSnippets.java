package jsfiddle;

import com.codeborne.selenide.*;
import io.github.vinogradoff.testdatabroker.client.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class DeleteSnippets {

    TestDataBrokerClient broker = new DefaultTestDataBrokerClient();

    @BeforeAll
    static void loginAndOpenAllSnippets() {
        open("https://jsfiddle.net/user/login/");
        $("#id_username").setValue("brokker");
        $("#id_password").setValue("brokker123").pressEnter();
        open("https://jsfiddle.net/user/fiddles/all/");
    }

    @RepeatedTest(3)
    void readSomeSnippets() throws IOException {
        var fiddleId = broker.readData("jsfiddle.net", "fiddleId");
        System.out.println("id=" + fiddleId);
        $(byText(fiddleId)).shouldBe(visible);
    }

    @RepeatedTest(2)
    void deleteSnippets() throws IOException {
        var fiddleId = broker.claimData("jsfiddle.net", "fiddleId");
        System.out.println("id=" + fiddleId);
        $(byText(fiddleId)).shouldBe(visible)
                .closest("li")
                .$("ul.actions").click();
        $(byText(fiddleId)).shouldBe(visible)
                .closest("li")
                .sibling(0)
                .$(".fiddleActions .delete a").click();
        confirm();
        $(byText(fiddleId)).shouldNotBe(visible);
        refresh();

    }
}
