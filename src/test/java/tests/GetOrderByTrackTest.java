package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Test;

import static data.ExpectedResponses.BAD_REQUEST;
import static data.ExpectedResponses.NOT_FOUND;
import static data.ExpectedResponses.SEARCH_MISSING;
import static data.ExpectedResponses.TRACK_NOT_FOUND;
import static data.OrderData.newOrder;
import static data.OrderData.INVALID_ORDER_NUMBER;
import static steps.OrderSteps.getByTrack;
import static steps.OrderSteps.getWithoutTrack;
import static steps.ResponseSteps.checkError;
import static steps.ResponseSteps.checkOrder;

@Epic("Яндекс.Самокат: API")
@Feature("Получение заказа по треку")
public class GetOrderByTrackTest extends BaseApiTest {

    @Test
    @DisplayName("Заказ найден по треку")
    @Description("Создаём заказ и проверяем объект order, положительный id и точное совпадение track")
    @Story("Получение заказа по треку")
    @Step("Сценарий: Заказ найден по треку")
    public void orderCanBeFound() {
        int track = data.prepareOrder(newOrder(null));
        checkOrder(getByTrack(track), track);
    }

    @Test
    @DisplayName("Поиск без трека")
    @Description("Не передаём query-параметр t. Проверяем HTTP 400 и точное сообщение")
    @Story("Получение заказа по треку")
    @Step("Сценарий: Поиск без трека")
    public void missingTrackIsRejected() {
        checkError(getWithoutTrack(), BAD_REQUEST, SEARCH_MISSING);
    }

    @Test
    @DisplayName("Поиск несуществующего трека")
    @Description("Ищем заказ по несуществующему номеру треку, слишком большому для созданных заказов")
    @Story("Получение заказа по треку")
    @Step("Сценарий: Поиск несуществующего трека")
    public void unknownTrackIsRejected() {
        checkError(getByTrack(INVALID_ORDER_NUMBER), NOT_FOUND, TRACK_NOT_FOUND);
    }
}
