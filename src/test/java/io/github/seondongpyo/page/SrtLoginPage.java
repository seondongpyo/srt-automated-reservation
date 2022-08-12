package io.github.seondongpyo.page;

import com.codeborne.selenide.WebDriverConditions;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SrtLoginPage {

    public static final String URL = "https://etk.srail.kr/cmc/01/selectLoginForm.do?pageId=TK0701000000";
    public static final String TITLE = "로그인 < SR회원 < 승차권 예약/발매 - 국민철도 SR";

    public SrtMainPage login(String id, String password) {
        $("#srchDvCd3").click();
        $("#srchDvNm03").shouldBe(visible).val(id);
        $("#hmpgPwdCphd03").val(password).submit();
        webdriver().shouldHave(WebDriverConditions.title(SrtMainPage.TITLE));
        return page(SrtMainPage.class);
    }
}
