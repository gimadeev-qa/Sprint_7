package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Test;

import static data.CourierData.newCourier;
import static data.ExpectedResponses.*;
import static steps.CourierSteps.deleteCourier;
import static steps.CourierSteps.deleteWithoutId;
import static steps.ResponseSteps.checkError;
import static steps.ResponseSteps.checkOk;
import static data.ExpectedResponses.ROUTE_NOT_FOUND;

@Epic("Яндекс.Самокат: API")
@Feature("Удаление курьера")
public class DeleteCourierTest extends BaseApiTest {

    @Test
    @DisplayName("Успешное удаление курьера")
    @Description("Создаём своего курьера, удаляем его и проверяем HTTP 200 и ok=true")
    @Story("Удаление курьера")
    @Step("Сценарий: Успешное удаление курьера")
    public void courierCanBeDeleted() {
        int id = data.prepareCourier(newCourier());

        checkOk(deleteCourier(id), OK);
    }

    @Test
    @DisplayName("Удаление без id")
    @Description("Проверяем HTTP 400 и сообщение")
    @Story("Удаление курьера")
    @Step("Сценарий: Удаление без id")
    public void missingIdIsRejected() {
        checkError(deleteWithoutId(), NOT_FOUND, ROUTE_NOT_FOUND);
    }

    @Test
    @DisplayName("Удаление несуществующего курьера")
    @Description("Получаем заведомо несуществующий id: создаём своего курьера и удаляем до проверяемого запроса")
    @Story("Удаление курьера")
    @Step("Сценарий: Удаление несуществующего курьера")
    public void nonexistentIdIsRejected() {
        int id = data.prepareCourier(newCourier());
        checkOk(deleteCourier(id), OK);

        checkError(deleteCourier(id), NOT_FOUND, COURIER_NOT_FOUND);
    }
}
