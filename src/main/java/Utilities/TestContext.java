package Utilities;

public class TestContext {

    private static ThreadLocal<String> token = new ThreadLocal<>();
    private static ThreadLocal<String> userId = new ThreadLocal<>();
    private static ThreadLocal<String> productId = new ThreadLocal<>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String value) {
        token.set(value);
    }

    public static String getUserId() {
        return userId.get();
    }

    public static void setUserId(String value) {
        userId.set(value);
    }

    public static String getProductId() {
        return productId.get();
    }

    public static void setProductId(String value) {
        productId.set(value);
    }

    public static void reset() {
        token.remove();
        userId.remove();
        productId.remove();
    }
}
