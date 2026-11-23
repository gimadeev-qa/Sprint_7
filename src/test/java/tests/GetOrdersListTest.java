package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.junit.Test;

import static steps.OrderSteps.getOrders;
import static steps.ResponseSteps.checkOrders;

@Epic("Яндекс.Самокат: API")
@Feature("Список заказов")
public class GetOrdersListTest extends BaseApiTest {

    @Test
    @DisplayName("Ответ содержит массив orders")
    @Description("Проверяем HTTP 200 и тип массива; пустой список тоже является корректным списком")
    @Story("Список заказов")
    @Step("Сценарий: Ответ содержит массив orders")
    public void ordersArrayIsReturned() {
        checkOrders(getOrders());
    }
}
