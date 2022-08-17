package io.github.seondongpyo.page;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class SrtReservationPage {

    public static final String TITLE = "예약하기 < 승차권예약 < 승차권 < 승차권 예약/발매 - 국민철도 SR";

    public void clickPaymentButton() {
        executeJavaScript("settleAmount();");
    }
}
