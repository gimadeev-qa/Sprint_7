package data;

import model.OrderModel;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class OrderData {

    public static final String ORDERS_PATH = "/api/v1/orders";
    public static final String ORDER_TRACK_PATH = ORDERS_PATH + "/track";
    public static final String ORDER_CANCEL_PATH = ORDERS_PATH + "/cancel";
    public static final String ORDER_ACCEPT_PATH = ORDERS_PATH + "/accept";
    public static final String ORDER_FINISH_PATH = ORDERS_PATH + "/finish";

    public static final String BLACK = "BLACK";
    public static final String GREY = "GREY";

    public static final int LIST_LIMIT = 5;
    public static final int INVALID_ORDER_NUMBER = 9999991;

    private OrderData() {
    }

    public static OrderModel newOrder(List<String> colors) {
        return new OrderModel(
                "Ivan",
                "Petrov",
                "Moscow, VDNH street, 10",
                "2",
                "+7 912 345 67 89",
                3,
                LocalDate.now().plusDays(2).toString(),
                "Sprint_7_" + UUID.randomUUID(),
                colors
        );
    }
}
