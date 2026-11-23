package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.mapper.ObjectMapperType;
import model.OrderModel;

import static data.OrderData.LIST_LIMIT;
import static data.OrderData.ORDER_ACCEPT_PATH;
import static data.OrderData.ORDER_CANCEL_PATH;
import static data.OrderData.ORDER_FINISH_PATH;
import static data.OrderData.ORDER_TRACK_PATH;
import static data.OrderData.ORDERS_PATH;
import static io.restassured.RestAssured.given;

public final class OrderSteps {

    private OrderSteps() {
    }

    @Step("Создать заказ")
    public static Response createOrder(OrderModel order) {
        return given()
                .spec(ApiSpecification.request())
                .body(order, ObjectMapperType.GSON)
                .when()
                .post(ORDERS_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Получить список заказов")
    public static Response getOrders() {
        return given()
                .spec(ApiSpecification.request())
                .queryParam("limit", LIST_LIMIT)
                .queryParam("page", 0)
                .when()
                .get(ORDERS_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Найти заказ по треку с номером track")
    public static Response getByTrack(int track) {
        return given()
                .spec(ApiSpecification.request())
                .queryParam("t", track)
                .when()
                .get(ORDER_TRACK_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Найти заказ без query-параметра t")
    public static Response getWithoutTrack() {
        return given()
                .spec(ApiSpecification.request())
                .when()
                .get(ORDER_TRACK_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Принять заказ  с номером orderId курьером  с номером courierId")
    public static Response accept(int orderId, int courierId) {
        return given()
                .spec(ApiSpecification.request())
                .queryParam("courierId", courierId)
                .pathParam("id", orderId)
                .when()
                .put(ORDER_ACCEPT_PATH + "/{id}")
                .then()
                .extract()
                .response();
    }

    @Step("Принять заказ с номером orderId без courierId")
    public static Response acceptWithoutCourier(int orderId) {
        return given()
                .spec(ApiSpecification.request())
                .pathParam("id", orderId)
                .when()
                .put(ORDER_ACCEPT_PATH + "/{id}")
                .then()
                .extract()
                .response();
    }

    @Step("Принять заказ без id заказа, курьер с номером courierId")
    public static Response acceptWithoutOrder(int courierId) {
        return given()
                .spec(ApiSpecification.request())
                .queryParam("courierId", courierId)
                .when()
                .put(ORDER_ACCEPT_PATH + "/")
                .then()
                .extract()
                .response();
    }

    @Step("Отменить тестовый заказ с треком с номером track")
    public static Response cancel(int track) {
        return given()
                .spec(ApiSpecification.request())
                .queryParam("track", track)
                .when()
                .put(ORDER_CANCEL_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Завершить принятый тестовый заказ с номером orderId")
    public static Response finish(int orderId) {
        return given()
                .spec(ApiSpecification.request())
                .pathParam("id", orderId)
                .when()
                .put(ORDER_FINISH_PATH + "/{id}")
                .then()
                .extract()
                .response();
    }
}
