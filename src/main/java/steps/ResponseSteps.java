package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;

public final class ResponseSteps {

    private ResponseSteps() {
    }

    @Step("Проверить HTTP status и ok=true")
    public static void checkOk(Response response, int status) {
        response.then()
                .log().ifValidationFails()
                .statusCode(status)
                .body("ok", equalTo(true));
    }

    @Step("Проверить HTTP status и точное сообщение об ошибке")
    public static void checkError(Response response, int status, String message) {
        response.then()
                .log().ifValidationFails()
                .statusCode(status)
                .body("message", equalTo(message));
    }

    @Step("Проверить HTTP status и положительное целое поле field")
    public static int positiveId(Response response, int status, String field) {
        response.then()
                .log().ifValidationFails()
                .statusCode(status)
                .body(field, instanceOf(Integer.class))
                .body(field, greaterThan(0));

        return response.jsonPath().getInt(field);
    }

    @Step("Проверить HTTP 200 и массив orders")
    public static void checkOrders(Response response) {
        response.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("orders", instanceOf(List.class));
    }

    @Step("Проверить HTTP 200, объект order и его трек с номером track")
    public static void checkOrder(Response response, int track) {
        response.then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("order", instanceOf(Map.class))
                .body("order.track", equalTo(track))
                .body("order.id", greaterThan(0));
    }
}
