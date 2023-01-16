package io.github.seondongpyo.page;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;

class SrtIntegrationTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @DisplayName("일반실 예약하기")
    @Test
    void reserveStandardRoom() throws IOException {
        List<String> accountInfo = Files.readAllLines(Paths.get("account.txt"));
        String id = accountInfo.get(0);
        String password = accountInfo.get(1);

        SrtLoginPage loginPage = open(SrtLoginPage.URL, SrtLoginPage.class);
        SrtMainPage mainPage = loginPage.login(id, password);
        SrtTicketListPage ticketListPage = mainPage.search("수서", "정읍", 2, "20220909", "000000");

        int count = 1;
        while (true) {
            ElementsCollection searchResults = ticketListPage.getSearchResults();
            Optional<SelenideElement> maybeResult = searchResults.asDynamicIterable()
                .stream()
                .filter(result -> {
                    List<String> times = List.of("08:33", "11:00", "14:10");
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
}
