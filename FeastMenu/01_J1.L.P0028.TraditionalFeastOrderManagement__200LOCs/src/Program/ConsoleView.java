package Program;

import Entity.Customer;
import Entity.FeastMenu;
import Entity.FeastOrder;

import Utilities.InputReader;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ConsoleView {
    private static final String LINE =
            "--------------------------------------------------------------------------------";
    private final NumberFormat moneyFormat = NumberFormat.getIntegerInstance(Locale.US);

    public void showMainMenu() {
        System.out.println("\n===== TRADITIONAL FEAST ORDER MANAGEMENT =====");
        System.out.println("1. Register customers");
        System.out.println("2. Update customer information");
        System.out.println("3. Search for customer information by name");
        System.out.println("4. Display feast menus");
        System.out.println("5. Place a feast order");
        System.out.println("6. Update order information");
        System.out.println("7. Save data to file");
        System.out.println("8. Display Customer or Order lists");
        System.out.println("9. Quit");
    }

    public void showCustomers(List<Customer> customers) {
        if (customers.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }
        System.out.println("Customers information:");
        System.out.println(LINE);
        System.out.printf(
                "%-7s | %-25s | %-10s | %-30s%n", "Code", "Customer Name", "Phone", "Email");
        System.out.println(LINE);
        for (Customer customer : customers) {
            System.out.printf(
                    "%-7s | %-25s | %-10s | %-30s%n",
                    customer.getCode(),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getEmail());
        }
        System.out.println(LINE);
    }

    public void showMenus(List<FeastMenu> menus) {
        if (menus.isEmpty()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }
        System.out.println(LINE);
        for (FeastMenu menu : menus) {
            System.out.printf("Code       : %s%n", menu.getCode());
            System.out.printf("Name       : %s%n", menu.getName());
            System.out.printf("Price      : %s Vnd%n", formatMoney(menu.getPrice()));
            System.out.printf("Ingredients: %s%n", menu.getIngredients());
            System.out.println(LINE);
        }
    }

    public void showOrder(FeastOrder order, Customer customer, FeastMenu menu) {
        System.out.println(LINE);
        System.out.printf("Customer order information [Order ID: %d]%n", order.getOrderId());
        System.out.println(LINE);
        System.out.printf("Code             : %s%n", customer.getCode());
        System.out.printf("Customer name    : %s%n", customer.getName());
        System.out.printf("Phone number     : %s%n", customer.getPhoneNumber());
        System.out.printf("Email            : %s%n", customer.getEmail());
        System.out.println(LINE);
        System.out.printf("Code of Set Menu : %s%n", order.getMenuCode());
        System.out.printf("Set menu name    : %s%n", menu.getName());
        System.out.printf(
                "Event date       : %s%n", order.getEventDate().format(InputReader.DATE_FORMAT));
        System.out.printf("Number of tables : %d%n", order.getNumberOfTables());
        System.out.printf("Price            : %s Vnd%n", formatMoney(order.getMenuPrice()));
        System.out.printf("Ingredients      : %s%n", menu.getIngredients());
        System.out.println(LINE);
        System.out.printf("Total cost       : %s Vnd%n", formatMoney(order.getTotalCost()));
        System.out.println(LINE);
    }

    public void showOrders(List<FeastOrder> orders) {
        if (orders.isEmpty()) {
            System.out.println("No data in the system.");
            return;
        }
        System.out.println(LINE);
        System.out.printf(
                "%-5s | %-10s | %-11s | %-8s | %12s | %6s | %15s%n",
                "ID", "Event date", "Customer", "Set Menu", "Price", "Tables", "Cost");
        System.out.println(LINE);
        for (FeastOrder order : orders) {
            System.out.printf(
                    "%-5d | %-10s | %-11s | %-8s | %12s | %6d | %15s%n",
                    order.getOrderId(),
                    order.getEventDate().format(InputReader.DATE_FORMAT),
                    order.getCustomerCode(),
                    order.getMenuCode(),
                    formatMoney(order.getMenuPrice()),
                    order.getNumberOfTables(),
                    formatMoney(order.getTotalCost()));
        }
        System.out.println(LINE);
    }

    private String formatMoney(BigDecimal value) {
        return moneyFormat.format(value);
    }
}
