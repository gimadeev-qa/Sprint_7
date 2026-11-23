package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import model.CourierModel;
import org.junit.Test;

import static data.ExpectedResponses.BAD_REQUEST;
import static data.ExpectedResponses.CONFLICT;
import static data.ExpectedResponses.CREATE_MISSING;
import static data.ExpectedResponses.CREATED;
import static data.ExpectedResponses.DUPLICATE_LOGIN;
import static data.CourierData.newCourier;
import static steps.CourierSteps.createCourier;
import static steps.ResponseSteps.checkError;
import static steps.ResponseSteps.checkOk;

@Epic("Яндекс.Самокат: API")
@Feature("Создание курьера")
public class CreateCourierTest extends BaseApiTest {

    @Test
    @DisplayName("Создание курьера с корректными данными")
    @Description("Ожидаем HTTP 201 и ok=true; созданный курьер удаляется после теста")
    @Story("Создание курьера")
    @Step("Сценарий: Создание курьера с корректными данными")
    public void courierCanBeCreated() {
        CourierModel courier = newCourier();
        data.rememberCourier(courier);

        checkOk(createCourier(courier), CREATED);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Создаём курьера, повторяем тот же запрос и проверяем конфликт")
    @Story("Создание курьера")
    @Step("Сценарий: Нельзя создать двух одинаковых курьеров")
    public void duplicateCourierIsRejected() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        checkError(createCourier(courier), CONFLICT, DUPLICATE_LOGIN);
    }

    @Test
    @DisplayName("Занятый логин с другим паролем и именем")
    @Description("Проверяем уникальность именно логина, а не совпадение всех полей")
    @Story("Создание курьера")
    @Step("Сценарий: Занятый логин с другим паролем и именем")
    public void existingLoginWithDifferentDataIsRejected() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierModel other = newCourier();
        other.setLogin(courier.getLogin());
        other.setPassword(courier.getPassword() + "_other");

        checkError(createCourier(other), CONFLICT, DUPLICATE_LOGIN);
    }

    @Test
    @DisplayName("Создание: Login отсутствует")
    @Description("Проверяем HTTP 400 и точный текст ошибки")
    @Story("Создание курьера")
    @Step("Сценарий: Создание: Login отсутствует")
    public void createWithoutLoginMissing() {
        CourierModel courier = newCourier();
        courier.setLogin(null);

        checkError(createCourier(courier), BAD_REQUEST, CREATE_MISSING);
    }

    @Test
    @DisplayName("Создание: Login пустой")
    @Description("Проверяем HTTP 400 и точный текст ошибки")
    @Story("Создание курьера")
    @Step("Сценарий: Создание: Login пустой")
    public void createWithoutLoginEmpty() {
        CourierModel courier = newCourier();
        courier.setLogin("");

        checkError(createCourier(courier), BAD_REQUEST, CREATE_MISSING);
    }

    @Test
    @DisplayName("Создание: Password отсутствует")
    @Description("Проверяем HTTP 400 и точный текст ошибки")
    @Story("Создание курьера")
    @Step("Сценарий: Создание: Password отсутствует")
    public void createWithoutPasswordMissing() {
        CourierModel courier = newCourier();
        courier.setPassword(null);

        checkError(createCourier(courier), BAD_REQUEST, CREATE_MISSING);
    }

    @Test
    @DisplayName("Создание: Password пустой")
    @Description("Проверяем HTTP 400 и точный текст ошибки")
    @Story("Создание курьера")
    @Step("Сценарий: Создание: Password пустой")
    public void createWithoutPasswordEmpty() {
        CourierModel courier = newCourier();
        courier.setPassword("");

        checkError(createCourier(courier), BAD_REQUEST, CREATE_MISSING);
    }

    @Test
    @DisplayName("Создание без необязательного firstName")
    @Description("Отдельно проверяем, что обязательны login и password, а не firstName.")
    @Story("Создание курьера")
    @Step("Сценарий: Создание без необязательного firstName")
    public void createWithoutFirstName() {
        CourierModel courier = newCourier();
        courier.setFirstName(null);
        data.rememberCourier(courier);

        checkOk(createCourier(courier), CREATED);
    }
}
