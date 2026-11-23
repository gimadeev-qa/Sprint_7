package fixtures;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CourierCredentialsModel;
import model.CourierModel;
import model.OrderModel;
import org.junit.runners.model.MultipleFailureException;

import java.util.ArrayList;
import java.util.List;

import static data.ExpectedResponses.CREATED;
import static data.ExpectedResponses.NOT_FOUND;
import static data.ExpectedResponses.OK;
import static steps.CourierSteps.createCourier;
import static steps.CourierSteps.deleteCourier;
import static steps.CourierSteps.loginCourier;
import static steps.OrderSteps.cancel;
import static steps.OrderSteps.createOrder;
import static steps.OrderSteps.finish;
import static steps.OrderSteps.getByTrack;
import static steps.ResponseSteps.checkOk;
import static steps.ResponseSteps.checkOrder;
import static steps.ResponseSteps.positiveId;

public class TestDataFixture {

    private final List<CourierModel> couriers = new ArrayList<>();
    private final List<Integer> tracks = new ArrayList<>();
    private final List<Integer> acceptedTracks = new ArrayList<>();

    @Step("Запомнить принятый заказ с треком с номером track")
    public void markAccepted(int track) {
        acceptedTracks.add(track);
    }

    @Step("Запомнить данные курьера для очистки")
    public void rememberCourier(CourierModel courier) {
        couriers.add(courier);
    }

    @Step("Создать курьера и получить его id")
    public int prepareCourier(CourierModel courier) {
        rememberCourier(courier);
        checkOk(createCourier(courier), CREATED);
        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(), courier.getPassword()
        );
        return positiveId(loginCourier(credentials), OK, "id");
    }

    @Step("Создать заказ и сохранить трек для очистки")
    public Response createTrackedOrder(OrderModel order) {
        Response response = createOrder(order);
        Integer track = response.jsonPath().get("track");

        if (track != null && track > 0) {
            tracks.add(track);
        }

        return response;
    }

    @Step("Подготовить заказ и получить его трек")
    public int prepareOrder(OrderModel order) {
        return positiveId(createTrackedOrder(order), CREATED, "track");
    }

    @Step("Очистить все данные текущего теста")
    public void cleanUp() throws Exception {
        List<Throwable> errors = new ArrayList<>();
        for (Integer track : tracks) {
            try {
                cleanupOrder(track);
            } catch (RuntimeException | AssertionError error) {
                errors.add(error);
            }
        }
        for (CourierModel courier : couriers) {
            try {
                cleanupCourier(courier);
            } catch (RuntimeException | AssertionError error) {
                errors.add(error);
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }

    @Step("Очистить заказ с треком с номером track")
    private void cleanupOrder(int track) {
        Response response = getByTrack(track);
        checkOrder(response, track);

        Boolean cancelled = response.jsonPath().get("order.cancelled");
        Boolean finished = response.jsonPath().get("order.finished");

        if (Boolean.TRUE.equals(cancelled) || Boolean.TRUE.equals(finished)) {
            return;
        }

        Integer courierId = response.jsonPath().get("order.courierId");
        Boolean inDelivery = response.jsonPath().get("order.inDelivery");

        boolean accepted = acceptedTracks.contains(track)
                || courierId != null
                || Boolean.TRUE.equals(inDelivery);

        if (accepted) {
            int orderId = positiveId(response, OK, "order.id");
            checkOk(finish(orderId), OK);
        } else {
            checkOk(cancel(track), OK);
        }
    }

    @Step("Удалить своего курьера, если он ещё существует")
    private void cleanupCourier(CourierModel courier) {
        CourierCredentialsModel credentials = new CourierCredentialsModel(
                courier.getLogin(), courier.getPassword()
        );
        Response response = loginCourier(credentials);
        if (response.statusCode() == NOT_FOUND) {
            return;
        }
        int id = positiveId(response, OK, "id");
        checkOk(deleteCourier(id), OK);
    }
}
