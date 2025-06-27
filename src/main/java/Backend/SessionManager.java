package backend;

public class SessionManager {
    private static int customerId = -1;
    private static String customerName = "";

    public static void setCustomer(int id, String name) {
        customerId = id;
        customerName = name;
    }

    public static int getCustomerId() {
        return customerId;
    }

    public static String getCustomerName() {
        return customerName;
    }

    public static void logout() {
        customerId = -1;
        customerName = "";
    }
}
