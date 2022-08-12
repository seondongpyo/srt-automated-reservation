package io.github.seondongpyo.page;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

class SrtTicketListPageTest {

    private SrtTicketListPage ticketListPage;

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        ticketListPage = open(SrtTicketListPage.URL, SrtTicketListPage.class);
        ticketListPage.search("수서", "오송", "20220813", "180000");
    }

    @DisplayName("승차권 조회 결과 중 첫 번째 결과의 일반석 승차권 유무를 조회한다.")
    @Test
    void checkFirstSearchResult() {
        ElementsCollection results = ticketListPage.getSearchResults();
        SelenideElement firstResult = results.first();
        assertThat(firstResult.find("td", 6).text()).containsAnyOf("매진", "예약하기", "좌석선택");
    }
}
