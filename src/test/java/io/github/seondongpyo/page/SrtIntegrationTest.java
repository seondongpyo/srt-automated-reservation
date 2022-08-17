package io.github.seondongpyo.page;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

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
        SrtTicketListPage ticketListPage = mainPage.search("수서", "정읍", "20220827", "100000");
        SrtReservationPage reservationPage = ticketListPage.selectFirstResult();
        reservationPage.clickPaymentButton();
    }
}
