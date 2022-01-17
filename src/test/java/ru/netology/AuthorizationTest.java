package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.UserInfo.*;

public class AuthorizationTest {
    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    //пользователь с таким логином и паролем существует — активен
    @Test
    public void shouldSendCorrectForm() {
        UserInfo infoUser = createUser("active");
        $("[name='login']").setValue(infoUser.getLogin());
        $("[name='password']").setValue(infoUser.getPassword());
        $(".button").click();
        $(byText("Личный кабинет")).shouldBe(Condition.visible);
    }

    //пользователь с таким логином и паролем существует — заблокирован
    @Test
    public void shouldStatusBlocked() {
        UserInfo infoUser = createUser("blocked");
        $("[name='login']").setValue(infoUser.getLogin());
        $("[name='password']").setValue(infoUser.getPassword());
        $(".button").click();
        $(byText("Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    //пользователя не зарегистрирован
    @Test
    public void shouldNotRegistered() {
        UserInfo withoutInfoUser = withoutUserInfo("active");
        $("[name='login']").setValue(withoutInfoUser.getLogin());
        $("[name='password']").setValue(withoutInfoUser.getPassword());
        $(".button").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    //пользователь зарегистрирован, но ввёл неверный логин
    @Test
    public void shouldWrongLogin() {
        UserInfo infoUser = createUser("active");
        //неправильный логин
        var wrongLogin = randomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(infoUser.getPassword());
        $(".button").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    //пользователь зарегистрирован, но ввёл неверный пароль
    @Test
    public void shouldWrongPassword() {
        UserInfo infoUser = createUser("active");
        var wrongPassword = randomPassword();
        $("[name='login']").setValue(wrongPassword);
        $("[name='password']").setValue(infoUser.getPassword());
        $(".button").click();
        $(byText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}
