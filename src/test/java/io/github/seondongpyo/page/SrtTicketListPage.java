package io.github.seondongpyo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SrtTicketListPage implements SrtTicketSearchablePage {

    public static final String URL = "https://etk.srail.kr/hpg/hra/01/selectScheduleList.do?pageId=TK0101010000";
    public static final String TITLE = "일반승차권 조회 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR";

    @Override
    public SrtTicketListPage search(String departmentStation, String arrivalStation, int count, String date, String time) {
        $("#dptRsStnCdNm").val(departmentStation);
        $("#arvRsStnCdNm").val(arrivalStation);
        $("#search-form > fieldset > div.box1 > div > ul > li:nth-child(2) > div.pic_mid_r > div:nth-child(1) > select")
            .selectOptionByValue(String.valueOf(count));
        $("#dptDt").selectOptionByValue(date);
        $("#dptTm").selectOptionByValue(time);
        $("#search_top_tag > input").click();

        try {
            $("#NetFunnel_Loading_Popup").shouldBe(visible).shouldNotBe(visible, Duration.ofMinutes(5));
        } catch (Exception | Error e) {

        }
        return this;
    }

    public ElementsCollection getSearchResults() {
        try {
            return $("#result-form > fieldset > div.tbl_wrap.th_thead > table > tbody")
                .should(visible)
                .findAll("tr");
        } catch (Exception | Error e) {
            return null;
        }
    }

    public SrtReservationPage selectFirstResult() {
        getSearchResults().first().find("td", 6).find("a").as("'예약하기' 버튼").click();
        webdriver().shouldHave(WebDriverConditions.title(SrtReservationPage.TITLE));
        return page(SrtReservationPage.class);
    }
}
