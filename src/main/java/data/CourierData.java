package data;

import com.github.javafaker.Faker;
import model.CourierModel;

import java.util.UUID;

public final class CourierData {

    private static final Faker USER = new Faker();

    public static final String COURIER_CREATE_PATH = "/api/v1/courier";
    public static final String COURIER_LOGIN_PATH = COURIER_CREATE_PATH + "/login";

    private CourierData() {
    }

    public static CourierModel newCourier() {
        return new CourierModel(
                "student_" + UUID.randomUUID(),
                USER.regexify("[A-Za-z0-9]{12}"),
                USER.name().firstName()
        );
    }
}
