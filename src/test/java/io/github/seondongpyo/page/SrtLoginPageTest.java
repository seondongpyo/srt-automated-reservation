package io.github.seondongpyo.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.title;
import static org.assertj.core.api.Assertions.assertThat;

class SrtLoginPageTest {

    @DisplayName("SRT 로그인 페이지로 이동한다.")
    @Test
    void loginPage() {
        open(SrtLoginPage.URL, SrtLoginPage.class);
        assertThat(title()).isEqualTo(SrtLoginPage.TITLE);
    }

    @DisplayName("휴대전화 번호와 비밀번호로 로그인을 성공하면 메인 페이지로 이동한다.")
    @Test
    void login() {
        SrtLoginPage loginPage = open(SrtLoginPage.URL, SrtLoginPage.class);
        loginPage.login("", "");
        assertThat(title()).isEqualTo(SrtMainPage.TITLE);
    }
}
