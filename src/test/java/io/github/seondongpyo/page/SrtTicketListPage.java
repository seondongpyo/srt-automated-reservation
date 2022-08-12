package io.github.seondongpyo.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SrtTicketListPage implements SrtTicketSearchablePage {

    public static final String URL = "https://etk.srail.kr/hpg/hra/01/selectScheduleList.do?pageId=TK0101010000";
    public static final String TITLE = "일반승차권 조회 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR";

    @Override
    public SrtTicketListPage search(String departmentStation, String arrivalStation, String date, String time) {
        $("#dptRsStnCdNm").val(departmentStation);
        $("#arvRsStnCdNm").val(arrivalStation);
        $("#dptDt").selectOptionByValue(date);
        $("#dptTm").selectOptionByValue(time);
        $("#search_top_tag > input").click();
        return this;
    }

    public ElementsCollection getSearchResults() {
        return $$("#result-form > fieldset > div.tbl_wrap.th_thead > table > tbody tr")
            .shouldHave(sizeGreaterThanOrEqual(1));
    }
}
