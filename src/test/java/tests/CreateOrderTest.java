package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import model.OrderModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static data.ExpectedResponses.CREATED;
import static data.OrderData.BLACK;
import static data.OrderData.GREY;
import static data.OrderData.newOrder;
import static steps.ResponseSteps.positiveId;

@RunWith(Parameterized.class)
@Epic("Яндекс.Самокат: API")
@Feature("Создание заказа")
public class CreateOrderTest extends BaseApiTest {

    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[][] parameters() {
        return new Object[][]{
                {Collections.singletonList(BLACK)},
                {Collections.singletonList(GREY)},
                {Arrays.asList(BLACK, GREY)},
                {null}
        };
    }

    @Test
    @DisplayName("Создание заказа с параметризованным цветом")
    @Description("Четыре варианта: BLACK, GREY, оба цвета и отсутствие поля color. Проверяем HTTP 201 и положительный track")
    @Story("Создание заказа")
    @Step("Сценарий: Создание заказа с параметризованным цветом")
    public void orderCanBeCreated() {
        OrderModel order = newOrder(colors);

        positiveId(data.createTrackedOrder(order), CREATED, "track");
    }
}
