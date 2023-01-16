package io.github.seondongpyo.page;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static org.assertj.core.api.Assertions.assertThat;

class SrtMainPageTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
    }

    @DisplayName("출발역과 도착역, 날짜, 시간을 선택 후 간편조회한다.")
    @Test
    void search() {
        SrtMainPage mainPage = open(SrtMainPage.URL, SrtMainPage.class);
        mainPage.search("수서", "오송", 2, "2022.08.13", "160000");
        assertThat(title()).isEqualTo(SrtTicketListPage.TITLE);
    }
}
