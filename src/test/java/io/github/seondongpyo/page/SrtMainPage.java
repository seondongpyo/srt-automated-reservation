package io.github.seondongpyo.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;

import static com.codeborne.selenide.Selenide.*;

public class SrtMainPage implements SrtTicketSearchablePage {

    public static final String URL = "https://etk.srail.kr/main.do";
    public static final String TITLE = "승차권 예약/발매 - 국민철도 SR";

    @Override
    public SrtTicketListPage search(String departmentStation, String arrivalStation, String date, String time) {
        $("#dptRsStnCd").selectOption(departmentStation);
        $("#arvRsStnCd").selectOption(arrivalStation);
        SelenideElement departmentDate = $("#search-form > fieldset > div:nth-child(9) > div > input.calendar1");
        executeJavaScript("arguments[0].removeAttribute('readonly')", departmentDate);
        departmentDate.val(date);
        $("#dptTm").selectOptionByValue(time);
        executeJavaScript("selectScheduleList(); return false;");

        webdriver().shouldHave(WebDriverConditions.title(SrtTicketListPage.TITLE));
        return page(SrtTicketListPage.class);
    }
}
