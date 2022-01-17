package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserInfo {

    private final String login;
    private final String password;
    private final String status;

    public UserInfo(String login, String password, String status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    private static Faker faker = new Faker(new Locale("en"));

    //пользователь, но незарегв системе
    public static UserInfo withoutUserInfo(String status) {
        UserInfo infoUser = new UserInfo(randomLogin(), randomPassword(), status);
        return infoUser;
    }

    private static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    static void sendRequest(UserInfo userInfo) {
        //сам запрос
        given() //"дано"
                .spec(requestSpecification)  //указываем, какую спецификацию указываем
                .body(userInfo)  //передаём в теде объект, который бёдет преобразован в JSON
                .when() //когда
                .post("/api/system/users")  //на какой путь, относительно BaseUri отправляем запрос
                .then() //"тогда ожидаем"
                .statusCode(200);  //код 200 ОК
    }

    //создание пользователя в базе
    public static UserInfo createUser(String status) {
        UserInfo registeredUser = withoutUserInfo(status);
        sendRequest(registeredUser);
        return registeredUser;
    }

    public static String randomLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String randomPassword() {
        String password = faker.internet().password();
        return password;
    }
}
