package tests;

import fixtures.TestDataFixture;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseApiTest {

    protected final TestDataFixture data = new TestDataFixture();

    @Before
    @Step("Настроить адрес учебного стенда")
    public void setUpApi() {
        RestAssured.baseURI = System.getProperty(
                "baseUrl", "https://qa-scooter.praktikum-services.ru"
        );
    }

    @After
    @Step("Выполнить очистку после теста")
    public void tearDown() throws Exception {
        data.cleanUp();
    }
}
