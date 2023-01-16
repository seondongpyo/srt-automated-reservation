package io.github.seondongpyo.page;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.WebDriverConditions;

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
        return this;
    }

    public ElementsCollection getSearchResults() {
        return $$("#result-form > fieldset > div.tbl_wrap.th_thead > table > tbody tr")
            .shouldHave(sizeGreaterThanOrEqual(1));
    }

    public SrtReservationPage selectFirstResult() {
        getSearchResults().first().find("td", 6).find("a").as("'예약하기' 버튼").click();
        webdriver().shouldHave(WebDriverConditions.title(SrtReservationPage.TITLE));
        return page(SrtReservationPage.class);
    }
}
