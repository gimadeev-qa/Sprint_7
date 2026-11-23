package data;

public final class ExpectedResponses {

    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int CONFLICT = 409;

    public static final String CREATE_MISSING = "Недостаточно данных для создания учетной записи";
    public static final String LOGIN_MISSING = "Недостаточно данных для входа";
    public static final String SEARCH_MISSING = "Недостаточно данных для поиска";
    public static final String DUPLICATE_LOGIN = "Этот логин уже используется. Попробуйте другой.";
    public static final String ACCOUNT_NOT_FOUND = "Учетная запись не найдена";
    public static final String COURIER_NOT_FOUND = "Курьера с таким id нет.";
    public static final String ACCEPT_COURIER_NOT_FOUND = "Курьера с таким id не существует";
    public static final String ACCEPT_ORDER_NOT_FOUND = "Заказа с таким id не существует";
    public static final String TRACK_NOT_FOUND = "Заказ не найден";
    public static final String ROUTE_NOT_FOUND = "Not Found.";

    private ExpectedResponses() {
    }
}
