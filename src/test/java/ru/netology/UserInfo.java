package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserInfo {

    private static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Faker faker = new Faker(new Locale("en"));

    private UserInfo(){

    }

    static void sendRequest(UserAuthorizationInfo userInfo) {
        //сам запрос
        given() //"дано"
                .spec(requestSpecification)  //указываем, какую спецификацию указываем
                .body(userInfo)  //передаём в теде объект, который бёдет преобразован в JSON
                .when() //когда
                .post("/api/system/users")  //на какой путь, относительно BaseUri отправляем запрос
                .then() //"тогда ожидаем"
                .statusCode(200);  //код 200 ОК
    }

    //выводим рандомно логин
    public static String getRandomLogin() {
        String login = faker.name().firstName();
        return login;
    }

    //выводим ранжомно паспорт
    public static String getRandomPassword() {
        String password = faker.internet().password(2,8);
        return password;
    }


    //пользователь, но незарег в системе
    public static UserAuthorizationInfo withoutUserInfo(String status) {
        var infoUser = new UserAuthorizationInfo(getRandomLogin(), getRandomPassword(), status);
        return infoUser;
    }

    //создание пользователя в базе
    public static UserAuthorizationInfo createUser(String status) {
        UserAuthorizationInfo registeredUser = withoutUserInfo(status);
        sendRequest(registeredUser);
        return registeredUser;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAuthorizationInfo {
        private String login;
        private String password;
        private String status;
    }
}
