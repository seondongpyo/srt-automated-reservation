package io.github.seondongpyo.page;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

class SrtIntegrationTest {

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        Configuration.browserCapabilities = options;
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void test() throws Exception {
        logic();
    }

    @DisplayName("일반실 예약하기 - 일반승차권 조회 페이지")
    @Test
    void reserve() throws Exception {
        logic();
    }

    private void logic() throws IOException {
        List<String> accountInfo = Files.readAllLines(Paths.get("account.txt"));
        String id = accountInfo.get(0);
        String password = accountInfo.get(1);
        String cardNo1 = accountInfo.get(2);
        String cardNo2 = accountInfo.get(3);
        String cardNo3 = accountInfo.get(4);
        String cardNo4 = accountInfo.get(5);
        String mm = accountInfo.get(6);
        String yy = accountInfo.get(7);
        String cardPassword = accountInfo.get(8);
        String birth = accountInfo.get(9);

        SrtLoginPage loginPage = open(SrtLoginPage.URL, SrtLoginPage.class);
        SrtMainPage mainPage = loginPage.login(id, password);
        SrtTicketListPage ticketListPage = mainPage.gotoTicketListPage();
        ticketListPage = ticketListPage.search("정읍", "수서", 2, "20240211", "100000");

        int count = 1;
        while (true) {
            ElementsCollection searchResults = ticketListPage.getSearchResults();
            if (searchResults == null) {
                refresh();
                ticketListPage = ticketListPage.search("정읍", "수서", 2, "20240211", "100000");
                String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
                System.out.printf("[%s] (%d) 검색 결과가 없으므로 다시 검색%n", currentDateTime, count++);
                continue;
            }

            Optional<SelenideElement> maybeResult = searchResults.asDynamicIterable()
                .stream()
                .filter(result -> {
                    List<String> times = List.of("11:03", "14:03");
                    String time = result.find("td:nth-child(4) > em").text();
                    String buttonText = result.find("td", 6).text();
                    return times.contains(time) && buttonText.contains("예약하기");
                })
                .findFirst();

            if (maybeResult.isPresent()) {
                SelenideElement result = maybeResult.get();
                SelenideElement button = result.find("td:nth-child(7) > a");
                executeJavaScript("arguments[0].click();", button);

                webdriver().shouldHave(WebDriverConditions.title("예약하기 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR"));
                $("#list-form > fieldset > div.tal_c > a.btn_large.btn_blue_dark.val_m.mgr10").shouldHave(visible).click();

                webdriver().shouldHave(WebDriverConditions.title("결제하기 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR"));
                $("#Tk_stlCrCrdNo14_checkbox").setSelected(false);
                $("#stlCrCrdNo11").sendKeys(cardNo1);
                $("#stlCrCrdNo12").sendKeys(cardNo2);
                $("#stlCrCrdNo13").sendKeys(cardNo3);
                $("#stlCrCrdNo14").sendKeys(cardNo4);
                $("#crdVlidTrm1M").selectOptionByValue(mm);
                $("#crdVlidTrm1Y").selectOptionByValue(yy);

                $("#Tk_vanPwd1_checkbox").setSelected(false);
                $("#vanPwd1").sendKeys(cardPassword);
                $("#athnVal1").sendKeys(birth);

                $("#select-form > fieldset > div.issues-area > div.tab.tab3 > ul > li:nth-child(2) > a").click();
                switchTo().alert(Duration.ofSeconds(3)).accept();
                $("#requestIssue1").click();
                switchTo().alert(Duration.ofSeconds(1)).accept();
                break;
            }

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
            System.out.printf("[%s] (%d) 검색 결과 없으므로 새로고침%n", currentDateTime, count++);
            refresh();
        }
    }

    @DisplayName("일반실 예약하기")
    @Test
    void reserveStandardRoom() throws IOException {
        List<String> accountInfo = Files.readAllLines(Paths.get("account.txt"));
        String id = accountInfo.get(0);
        String password = accountInfo.get(1);

        SrtLoginPage loginPage = open(SrtLoginPage.URL, SrtLoginPage.class);
        SrtMainPage mainPage = loginPage.login(id, password);
        SrtTicketListPage ticketListPage = mainPage.search("수서", "정읍", 2, "20240209", "000000");

        int count = 1;
        while (true) {
            ElementsCollection searchResults = ticketListPage.getSearchResults();
            Optional<SelenideElement> maybeResult = searchResults.asDynamicIterable()
                .stream()
                .filter(result -> {
                    List<String> times = List.of("06:40", "08:30");
                    String time = result.find("td:nth-child(4) > em").text();
                    String buttonText = result.find("td", 6).text();
                    return times.contains(time) && buttonText.contains("예약하기");
                })
                .findFirst();

            if (maybeResult.isPresent()) {
                SelenideElement result = maybeResult.get();
                SelenideElement button = result.find("td:nth-child(7) > a");
                executeJavaScript("arguments[0].click();", button);

                webdriver().shouldHave(WebDriverConditions.title("예약하기 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR"));
                $("#list-form > fieldset > div.tal_c > a.btn_large.btn_blue_dark.val_m.mgr10").shouldHave(visible).click();
                break;
            }

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
            System.out.printf("[%s] (%d) 검색 결과 없으므로 새로고침%n", currentDateTime, count++);
            refresh();
        }
    }
    
    @DisplayName("일반실 예약대기")
    @Test
    void waitingForReservation() throws IOException {
        List<String> accountInfo = Files.readAllLines(Paths.get("account.txt"));
        String id = accountInfo.get(0);
        String password = accountInfo.get(1);

        SrtLoginPage loginPage = open(SrtLoginPage.URL, SrtLoginPage.class);
        SrtMainPage mainPage = loginPage.login(id, password);
        SrtTicketListPage ticketListPage = mainPage.search("수서", "정읍", 2, "20240209", "000000");

        int count = 1;
        while (true) {
            ElementsCollection searchResults = ticketListPage.getSearchResults();
            Optional<SelenideElement> maybeResult = searchResults.asDynamicIterable()
                .stream()
                .filter(result -> {
                    List<String> times = List.of("06:40", "08:30");
                    String time = result.find("td:nth-child(4) > em").text();
                    String buttonText = result.find("td", 7).text();
                    return times.contains(time) && (buttonText.contains("신청하기") || buttonText.contains("예약하기"));
                })
                .findFirst();

            if (maybeResult.isPresent()) {
                SelenideElement result = maybeResult.get();
                SelenideElement button = result.find("td:nth-child(8) > a");
                executeJavaScript("arguments[0].click();", button);

                webdriver().shouldHave(WebDriverConditions.title("예약하기 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR"));
                $("#list-form > fieldset > div.tal_c > a.btn_large.btn_blue_dark.val_m.mgr10").shouldHave(visible).click();
                break;
            }

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
            System.out.printf("[%s] (%d) 검색 결과 없으므로 새로고침%n", currentDateTime, count++);
            refresh();
        }
    }
}
