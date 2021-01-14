package tests;

import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import utils.BaseMethods;
import utils.BaseSteps;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static helpers.AttachmentsHelper.*;
import static io.qameta.allure.Allure.step;



public class IssueTests extends BaseMethods{

    private static String REPOSITORY = "https://github.com/sagittMZ/qa_guru";
   // BaseMethods base = new BaseMethods();
//TODO Не забыть спросить почему при подключении селеноида и использовании настроек из TestBase не логинит

    @BeforeAll
     static void setup() {
        Configuration.startMaximized = true;
    }
    @AfterEach
    @Step("Attachments")
    public void afterEach(){
        attachScreenshot("Last screenshot");
        attachPageSource();
        attachAsText("Browser console logs", getConsoleLogs());
        attachVideo();
        closeWebDriver();
    }

    @Test
    @DisplayName("Создание issue с использоавнием чистого селенида")
    @Feature("Issue")
    @Link(url="https://github.com/sagittMZ/qa_guru", name ="Учимсо потихоньку")
    @Owner("s_a_g_i_t_t")
    public void createIssueUsingPureSelenide() {
        githubLogin();
        open(REPOSITORY);
        String issues_name = "Issue was created at " + getCurrentTimeStamp();
        System.out.println("Just wait a little bit");
        $(byAttribute("data-content","Issues")).click();
        $(By.linkText("New issue")).click();
        $("#issue_title").setValue(issues_name);
        $("#issue_body").setValue("Some crazy description" + generateDescription());
        $(byText("assign yourself")).click();
        $(withText("Submit new issue")).click();
        $(byText(issues_name)).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Создание issue с использованием лямбда выражений")
    @Feature("Issue")
    @Link(url="https://github.com/sagittMZ/qa_guru", name ="Учимсо потихоньку")
    @Owner("s_a_g_i_t_t")
    public void createIssueUsingLambda() {
        githubLogin();
        open(REPOSITORY);
        Map<String,String> data = new HashMap<>();
        data.put("CurrentTime",getCurrentTimeStamp());
        step("Go to create new issue", ()->{
            $(byAttribute("data-content","Issues")).click();
            $(By.linkText("New issue")).click();
        });
        step("Fill issues inputs", ()->{
            String issues_name = "Issue was created at " + data.get("CurrentTime");
            $("#issue_title").setValue(issues_name);
            $("#issue_body").setValue("Some crazy description" + generateDescription());
        });
        step("Assigning issue to developer", ()-> {
            $(byText("assign yourself")).click();
        });
        step("Submit new issue", ()-> {
            $(withText("Submit new issue")).click();
        });
        step("Checking the condition 'The new issue is successfully created'", ()-> {
            String issues_name = "Issue was created at " + data.get("CurrentTime");
            $(byAttribute("data-content", "Issues")).click();
            $(byText(issues_name)).shouldBe(Condition.visible);
            System.out.println("Checking the condition 'The new issue is successfully created'");
        });
    }

    @Test
    @DisplayName("Создание issue с использованием аннотаций")
    @Feature("Issue")
    @Link(url="https://github.com/sagittMZ/qa_guru", name ="Учимсо потихоньку")
    @Owner("s_a_g_i_t_t")
    public void createIssueUsingAnnotation() {
        githubLogin();
        open(REPOSITORY);
        BaseSteps steps = new BaseSteps();
        Map<String,String> data = new HashMap<>();
        data.put("CurrentTime",getCurrentTimeStamp());
        data.put("Description",generateDescription());
        String issue_name = "Issue was created at: " + data.get("CurrentTime");
        String description = "Some crazy description: " + data.get("Description");
        steps.goToCreateIssue();
        steps.fillIssuesInputs(issue_name, description);
        steps.assigningIssue();
        steps.submitNewIssue();
        steps.checkTheIssueWasCreated(issue_name);
    }


}
