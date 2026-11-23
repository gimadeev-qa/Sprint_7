package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.mapper.ObjectMapperType;
import model.CourierCredentialsModel;
import model.CourierModel;

import static data.CourierData.COURIER_CREATE_PATH;
import static data.CourierData.COURIER_LOGIN_PATH;
import static io.restassured.RestAssured.given;

public final class CourierSteps {

    private CourierSteps() {
    }

    @Step("Создать курьера")
    public static Response createCourier(CourierModel courier) {
        return given()
                .spec(ApiSpecification.request())
                .body(courier, ObjectMapperType.GSON)
                .when()
                .post(COURIER_CREATE_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Авторизоваться курьером")
    public static Response loginCourier(CourierCredentialsModel credentials) {
        return given()
                .spec(ApiSpecification.request())
                .body(credentials, ObjectMapperType.GSON)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Удалить курьера с номером id")
    public static Response deleteCourier(int id) {
        return given()
                .spec(ApiSpecification.request())
                .pathParam("id", id)
                .when()
                .delete(COURIER_CREATE_PATH + "/{id}")
                .then()
                .extract()
                .response();
    }

    @Step("Удалить курьера без id в пути")
    public static Response deleteWithoutId() {
        return given()
                .spec(ApiSpecification.request())
                .when()
                .delete(COURIER_CREATE_PATH + "/")
                .then()
                .extract()
                .response();
    }
}
