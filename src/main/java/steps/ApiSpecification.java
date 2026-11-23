package steps;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiSpecification {

    private ApiSpecification() {
    }

    public static RequestSpecification request() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 60000)))
                .build();
    }
}
