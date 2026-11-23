package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import model.CourierCredentialsModel;
import model.CourierModel;
import org.junit.Assert;
import org.junit.Test;

import static data.ExpectedResponses.ACCOUNT_NOT_FOUND;
import static data.ExpectedResponses.BAD_REQUEST;
import static data.ExpectedResponses.LOGIN_MISSING;
import static data.ExpectedResponses.NOT_FOUND;
import static data.ExpectedResponses.OK;
import static data.CourierData.newCourier;
import static steps.CourierSteps.loginCourier;
import static steps.ResponseSteps.checkError;
import static steps.ResponseSteps.positiveId;

@Epic("Яндекс.Самокат: API")
@Feature("Логин курьера")
public class LoginCourierTest extends BaseApiTest {

    @Step("Сравнить id из авторизации с id подготовленного курьера")
    private void assertSameCourier(int expected, int actual) {
        Assert.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    @Description("Готовим курьера, проверяем HTTP 200 и совпадение id")
    @Story("Логин курьера")
    @Step("Сценарий: Курьер может авторизоваться")
    public void courierCanLogin() {
        CourierModel courier = newCourier();
        int expectedId = data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );

        int actualId = positiveId(loginCourier(credentials), OK, "id");
        assertSameCourier(expectedId, actualId);
    }

    @Test
    @DisplayName("Логин: Login отсутствует")
    @Description("Проверяем HTTP 400 и точное сообщение об отсутствии данных")
    @Story("Логин курьера")
    @Step("Сценарий: Логин: Login отсутствует")
    public void loginWithoutLoginMissing() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setLogin(null);

        checkError(loginCourier(credentials), BAD_REQUEST, LOGIN_MISSING);
    }

    @Test
    @DisplayName("Логин: Login пустой")
    @Description("Проверяем HTTP 400 и точное сообщение об отсутствии данных")
    @Story("Логин курьера")
    @Step("Сценарий: Логин: Login пустой")
    public void loginWithoutLoginEmpty() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setLogin("");

        checkError(loginCourier(credentials), BAD_REQUEST, LOGIN_MISSING);
    }

    @Test
    @DisplayName("Логин: Password отсутствует")
    @Description("Проверяем HTTP 400 и точное сообщение об отсутствии данных")
    @Story("Логин курьера")
    @Step("Сценарий: Логин: Password отсутствует")
    public void loginWithoutPasswordMissing() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setPassword(null);

        checkError(loginCourier(credentials), BAD_REQUEST, LOGIN_MISSING);
    }

    @Test
    @DisplayName("Логин: Password пустой")
    @Description("Проверяем HTTP 400 и точное сообщение об отсутствии данных")
    @Story("Логин курьера")
    @Step("Сценарий: Логин: Password пустой")
    public void loginWithoutPasswordEmpty() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setPassword("");

        checkError(loginCourier(credentials), BAD_REQUEST, LOGIN_MISSING);
    }

    @Test
    @DisplayName("Авторизация: неверный Login")
    @Description("Меняем только одно поле у данных существующего курьера; ожидаем HTTP 404")
    @Story("Логин курьера")
    @Step("Сценарий: Авторизация: неверный Login")
    public void wrongLoginIsRejected() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setLogin(courier.getLogin() + "_wrong");

        checkError(loginCourier(credentials), NOT_FOUND, ACCOUNT_NOT_FOUND);
    }

    @Test
    @DisplayName("Авторизация: неверный Password")
    @Description("Меняем только одно поле у данных существующего курьера; ожидаем HTTP 404")
    @Story("Логин курьера")
    @Step("Сценарий: Авторизация: неверный Password")
    public void wrongPasswordIsRejected() {
        CourierModel courier = newCourier();
        data.prepareCourier(courier);

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );
        credentials.setPassword(courier.getPassword() + "_wrong");

        checkError(loginCourier(credentials), NOT_FOUND, ACCOUNT_NOT_FOUND);
    }

    @Test
    @DisplayName("Несуществующий пользователь")
    @Description("Генерируем уникальные данные, не регистрируем их и ожидаем HTTP 404")
    @Story("Логин курьера")
    @Step("Сценарий: Несуществующий пользователь")
    public void unknownCourierIsRejected() {
        CourierModel courier = newCourier();

        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(),
                courier.getPassword()
        );

        checkError(loginCourier(credentials), NOT_FOUND, ACCOUNT_NOT_FOUND);
    }
}
