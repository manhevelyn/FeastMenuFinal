package Program;

import Service.CustomerService;
import Service.MenuService;
import Service.OrderService;

import Entity.Customer;
import Entity.FeastMenu;
import Entity.FeastOrder;

import Utilities.InputReader;
import Utilities.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class FeastController {
    private final InputReader input;
    private final ConsoleView view;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final OrderService orderService;
    private boolean changed;

    public FeastController() {
        input = new InputReader();
        view = new ConsoleView();
        customerService = new CustomerService();
        menuService = new MenuService("feastMenu.csv");
        orderService = new OrderService();
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.showMainMenu();
            int choice = input.readPositiveInt("Select an option: ");
            switch (choice) {
                case 1:
                    registerCustomers();
                    break;
                case 2:
                    updateCustomers();
                    break;
                case 3:
                    searchCustomers();
                    break;
                case 4:
                    view.showMenus(menuService.getSortedMenus());
                    break;
                case 5:
                    placeOrders();
                    break;
                case 6:
                    updateOrders();
                    break;
                case 7:
                    saveData();
                    break;
                case 8:
                    displayLists();
                    break;
                case 9:
                    running = !confirmQuit();
                    break;
                default:
                    System.out.println("Please select an option from 1 to 9.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void registerCustomers() {
        do {
            String code = readNewCustomerCode();
            String name = readName("Customer name: ", false);
            String phone = readPhone("Phone number: ", false);
            String email = readEmail("Email: ", false);
            customerService.add(new Customer(code, name, phone, email));
            changed = true;
            System.out.println("Customer registered successfully.");
        } while (input.readYesNo("Register another customer? (Y/N): "));
    }

    private void updateCustomers() {
        do {
            String code = readNotBlank("Customer code: ", "Customer code cannot be blank.");
            Customer customer = customerService.findByCode(code);
            if (customer == null) {
                System.out.println("This customer does not exist.");
            } else {
                String name = readName("New name (blank to keep): ", true);
                String phone = readPhone("New phone (blank to keep): ", true);
                String email = readEmail("New email (blank to keep): ", true);
                if (!name.isEmpty()) customer.setName(name);
                if (!phone.isEmpty()) customer.setPhoneNumber(phone);
                if (!email.isEmpty()) customer.setEmail(email);
                changed = true;
                System.out.println("Customer information updated successfully.");
            }
        } while (input.readYesNo("Update another customer? (Y/N): "));
    }

    private void searchCustomers() {
        String keyword = readNotBlank("Name or partial name: ", "Search text cannot be blank.");
        ArrayList<Customer> matches = customerService.searchByName(keyword);
        if (matches.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            System.out.println("Matching Customers: " + keyword);
            view.showCustomers(matches);
        }
    }

    private void placeOrders() {
        if (menuService.isEmpty()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }
        do {
            String customerCode = readExistingCustomerCode();
            String menuCode = readExistingMenuCode("Set menu code: ", false);
            int tables = input.readPositiveInt("Number of tables: ");
            LocalDate eventDate = input.readFutureDate("Event date (dd/MM/yyyy): ", false);
            FeastMenu menu = menuService.findByCode(menuCode);
            FeastOrder order =
                    orderService.create(customerCode, menuCode, tables, eventDate, menu.getPrice());
            if (order == null) {
                System.out.println("Duplicate data!");
            } else {
                changed = true;
                view.showOrder(order, customerService.findByCode(customerCode), menu);
            }
        } while (input.readYesNo("Place another order? (Y/N): "));
    }

    private void updateOrders() {
        if (menuService.isEmpty()) {
            System.out.println("Cannot read data from feastMenu.csv. Please check it.");
            return;
        }
        do {
            int orderId = input.readPositiveInt("Order ID: ");
            FeastOrder order = orderService.findById(orderId);
            if (order == null) {
                System.out.println("This Order does not exist.");
            } else if (order.getEventDate().isBefore(LocalDate.now())) {
                System.out.println("An order whose event date has passed cannot be updated.");
            } else {
                updateExistingOrder(order);
            }
        } while (input.readYesNo("Update another order? (Y/N): "));
    }

    private void updateExistingOrder(FeastOrder order) {
        String menuCode = readExistingMenuCode("New set menu code (blank to keep): ", true);
        Integer tables = input.readOptionalPositiveInt("New number of tables (blank to keep): ");
        LocalDate eventDate =
                input.readFutureDate("New event date dd/MM/yyyy (blank to keep): ", true);
        String newMenuCode = menuCode.isEmpty() ? order.getMenuCode() : menuCode;
        LocalDate newEventDate = eventDate == null ? order.getEventDate() : eventDate;
        if (orderService.isDuplicate(
                order.getOrderId(), order.getCustomerCode(), newMenuCode, newEventDate)) {
            System.out.println("Duplicate data!");
            return;
        }
        if (!menuCode.isEmpty()) {
            order.setMenuCode(menuCode);
            order.setMenuPrice(menuService.findByCode(menuCode).getPrice());
        }
        if (tables != null) order.setNumberOfTables(tables);
        if (eventDate != null) order.setEventDate(eventDate);
        changed = true;
        System.out.println("Order information updated successfully.");
    }

    private void displayLists() {
        System.out.println("1. Display customers");
        System.out.println("2. Display orders");
        int choice = input.readPositiveInt("Select a list: ");
        if (choice == 1) view.showCustomers(customerService.getSortedCustomers());
        else if (choice == 2) view.showOrders(orderService.getSortedOrders());
        else System.out.println("Please select 1 or 2.");
    }

    private String readNewCustomerCode() {
        while (true) {
            String code = input.readString("Customer code: ").toUpperCase();
            if (Validator.isCustomerCode(code) && customerService.findByCode(code) == null)
                return code;
            System.out.println("Code must be unique and match C/G/K followed by four digits.");
        }
    }

    private String readExistingCustomerCode() {
        while (true) {
            String code = input.readString("Customer code: ").toUpperCase();
            if (customerService.findByCode(code) != null) return code;
            System.out.println("Customer code has not been registered.");
        }
    }

    private String readExistingMenuCode(String prompt, boolean optional) {
        while (true) {
            String code = input.readString(prompt).toUpperCase();
            if (optional && code.isEmpty()) return code;
            if (menuService.findByCode(code) != null) return code;
            System.out.println("Set menu code does not exist.");
        }
    }

    private String readName(String prompt, boolean optional) {
        while (true) {
            String name = input.readString(prompt);
            if ((optional && name.isEmpty()) || Validator.isName(name)) return name;
            System.out.println("Name must contain 2 to 25 characters.");
        }
    }

    private String readPhone(String prompt, boolean optional) {
        while (true) {
            String phone = input.readString(prompt);
            if ((optional && phone.isEmpty()) || Validator.isPhoneNumber(phone)) return phone;
            System.out.println("Enter a valid 10-digit Vietnamese mobile number.");
        }
    }

    private String readEmail(String prompt, boolean optional) {
        while (true) {
            String email = input.readString(prompt);
            if ((optional && email.isEmpty()) || Validator.isEmail(email)) return email;
            System.out.println("Enter a valid email address.");
        }
    }

    private String readNotBlank(String prompt, String errorMessage) {
        while (true) {
            String value = input.readString(prompt);
            if (!value.isEmpty()) return value;
            System.out.println(errorMessage);
        }
    }

    private boolean saveData() {
        try {
            customerService.save();
            orderService.save();
            changed = false;
            System.out.println("Customer data has been successfully saved to customers.dat.");
            System.out.println(
                    "Order data has been successfully saved to feast_order_service.dat.");
            return true;
        } catch (IOException exception) {
            System.out.println("Unable to save data: " + exception.getMessage());
            return false;
        }
    }

    private boolean confirmQuit() {
        if (!changed) return true;
        if (input.readYesNo("There are unsaved changes. Save before quitting? (Y/N): "))
            return saveData();
        return input.readYesNo("Discard unsaved changes and quit? (Y/N): ");
    }
}
