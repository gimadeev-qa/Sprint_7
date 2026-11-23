package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.Collections;

import static data.ExpectedResponses.*;
import static data.OrderData.BLACK;
import static data.OrderData.INVALID_ORDER_NUMBER;
import static data.CourierData.newCourier;
import static data.OrderData.newOrder;
import static steps.CourierSteps.deleteCourier;
import static steps.OrderSteps.accept;
import static steps.OrderSteps.acceptWithoutCourier;
import static steps.OrderSteps.acceptWithoutOrder;
import static steps.OrderSteps.getByTrack;
import static steps.ResponseSteps.checkError;
import static steps.ResponseSteps.checkOk;
import static steps.ResponseSteps.positiveId;
import static data.ExpectedResponses.ROUTE_NOT_FOUND;

@Epic("Яндекс.Самокат: API")
@Feature("Принятие заказа")
public class AcceptOrderTest extends BaseApiTest {

    @Test
    @DisplayName("Курьер принимает заказ")
    @Description("Создаём курьера и заказ, получаем order.id через track; передаём courierId через queryParam.")
    @Story("Принятие заказа")
    @Step("Сценарий: Курьер принимает заказ")
    public void orderCanBeAccepted() {
        int courierId = data.prepareCourier(newCourier());
        int track = data.prepareOrder(newOrder(Collections.singletonList(BLACK)));
        int orderId = positiveId(getByTrack(track), OK, "order.id");

        Response response = accept(orderId, courierId);

        if (response.statusCode() == OK) {
            data.markAccepted(track);
        }

        checkOk(response, OK);
    }

    @Test
    @DisplayName("Принятие без courierId")
    @Description("Создаём заказ; не передаём query-параметр courierId. Ожидаем HTTP 400.")
    @Story("Принятие заказа")
    @Step("Сценарий: Принятие без courierId")
    public void missingCourierIdIsRejected() {
        int track = data.prepareOrder(newOrder(null));
        int orderId = positiveId(getByTrack(track), OK, "order.id");

        checkError(acceptWithoutCourier(orderId), BAD_REQUEST, SEARCH_MISSING);
    }

    @Test
    @DisplayName("Принятие с несуществующим курьером")
    @Description("Создаём и удаляем своего курьера; используем его id для принятия нового заказа.")
    @Story("Принятие заказа")
    @Step("Сценарий: Принятие с несуществующим курьером")
    public void unknownCourierIsRejected() {
        int courierId = data.prepareCourier(newCourier());
        checkOk(deleteCourier(courierId), OK);

        int track = data.prepareOrder(newOrder(null));
        int orderId = positiveId(getByTrack(track), OK, "order.id");

        checkError(accept(orderId, courierId), NOT_FOUND, ACCEPT_COURIER_NOT_FOUND);
    }

    @Test
    @DisplayName("Принятие без id заказа")
    @Description("Создаём своего курьера; отправляем запрос без id заказа в пути.")
    @Story("Принятие заказа")
    @Step("Сценарий: Принятие без id заказа")
    public void missingOrderIdIsRejected() {
        int courierId = data.prepareCourier(newCourier());

        checkError(acceptWithoutOrder(courierId), NOT_FOUND, ROUTE_NOT_FOUND);
    }

    @Test
    @DisplayName("Принятие с несуществующим id заказа")
    @Description("Отрицательный id используется как некорректный идентификатор, а не как id существующего заказа.")
    @Story("Принятие заказа")
    @Step("Сценарий: Принятие с несуществующим id заказа")
    public void unknownOrderIsRejected() {
        int courierId = data.prepareCourier(newCourier());

        checkError(accept(INVALID_ORDER_NUMBER, courierId), NOT_FOUND, ACCEPT_ORDER_NOT_FOUND);
    }
}
