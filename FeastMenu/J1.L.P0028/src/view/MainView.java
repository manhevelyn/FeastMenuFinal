package view;

import controller.FeastController;
import java.time.LocalDate;
import java.util.ArrayList;
import util.Inputter;
import util.Validation;
import model.Customer;
import model.FeastMenu;
import java.util.Comparator;
import model.Order;
// FLOW: chạy tính năng số 4 thì menu mới load vào system

public class MainView {

    public void showMenu() {
        System.out.println("======== FEAST");
        System.out.println("1. Register customer");
        System.out.println("2. Update customer");
        System.out.println("3. Search for customer info by name");
        System.out.println("4. Display feast menus");
        System.out.println("5. Place a feast order");
        System.out.println("6. Update order information");
        System.out.println("7. Save data to file");
        System.out.println("8. Display Customer list");
        System.out.println("9. Display Order lists");
        System.out.println("10. Quit");
    }

    public void showList(ArrayList<Customer> list) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%-7s|%-25s|%-10s|%-30s", "Code", "Customer Name", "Phone", "Email"));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Customer customer : list) {
            System.out.println(customer); // Gọi toString() tự động
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public void run() {
        int choice;
        Inputter input = new Inputter();
        FeastController controller = new FeastController();
        controller.getListMenu("FeastMenu.csv"); // Để nếu ko bấm vào 4 trc
        do {
            showMenu();
            choice = input.inputInt("Enter your choice:");

            switch (choice) {
                case 1:
                    registerCustomer(input, controller);
                    break;
                case 2:
                    updateCustomer(input, controller);
                    break;
                case 3:
                    searchCustomerByName(input, controller);
                    break;
                case 4:
                    readFeastMenu(input, controller);
                    break;
                case 5:
                    placeOrder(input, controller);
                    break;
                case 6:
                    updateOrder(input, controller);
                    break;
                default:
                    throw new AssertionError();

            }
        } while (true);
    }

    private void registerCustomer(Inputter input, FeastController controller) {
        String choice;
        do {
            String customerCode, name, email, phone;
            do {
                customerCode = input.inputString("Enter customer code:");
            } while (!Validation.isValidCustomerCode(customerCode) || controller.getCustomerById(customerCode) != null);

            do {
                name = input.inputString("Enter name:");
            } while (!Validation.isValidName(name));

            do {
                email = input.inputString("Enter email:");
            } while (!Validation.isValidEmail(email));

            do {
                phone = input.inputString("Enter phone:");
            } while (!Validation.isValidPhone(phone));

            // Tạo một đối tượng customer
            Customer cus = new Customer(customerCode, name, email, phone);
            controller.addCustomer(cus);

            choice = input.inputString("Do you want to register a new customer? Yes / No ");
        } while (choice.equalsIgnoreCase("Yes"));
    }

    private void updateCustomer(Inputter input, FeastController controller) {
        String choice;
        do {
            String customerCode = input.inputString("Enter customer code:");
            Customer oldCus = controller.getCustomerById(customerCode);
            if (oldCus == null) {
                System.out.println("Customer doesn't exist");
                return;
            }

            // Cập nhật thông tin khách hàng
            String name = oldCus.getName();
            String email = oldCus.getEmail();
            String phone = oldCus.getPhoneNumber();

            if (input.inputString("Do you want to update name? Yes/No").equalsIgnoreCase("Yes")) {
                do {
                    name = input.inputString("Enter name:");
                } while (!Validation.isValidName(name));
            }

            if (input.inputString("Do you want to update email? Yes/No").equalsIgnoreCase("Yes")) {
                do {
                    email = input.inputString("Enter email:");
                } while (!Validation.isValidEmail(email));
            }

            if (input.inputString("Do you want to update phone number? Yes/No").equalsIgnoreCase("Yes")) {
                do {
                    phone = input.inputString("Enter phone:");
                } while (!Validation.isValidPhone(phone));
            }

            // Cập nhật thông tin khách hàng cũ
            oldCus.setName(name);
            oldCus.setEmail(email);
            oldCus.setPhoneNumber(phone);

            choice = input.inputString("Do you want to update another customer? Yes / No ");
        } while (choice.equalsIgnoreCase("Yes"));
    }

    private void searchCustomerByName(Inputter input, FeastController controller) {
        String name;
        do {
            name = input.inputString("Enter name:");
        } while (!Validation.isValidName(name));

        ArrayList<Customer> listCus = controller.searchByName(name);
        if (listCus.isEmpty()) { // Kiểm tra danh sách rỗng
            System.out.println("No one matches the search criteria!");
        } else {
            System.out.println("Matching customers are found:");
            showList(listCus);
        }
    }

    private void readFeastMenu(Inputter input, FeastController controller) {
        ArrayList<FeastMenu> listMenu = controller.getListMenu();
        listMenu.sort(
                Comparator.comparingDouble(
                        FeastMenu::getPrice
                )
        );

        showListMenu(listMenu);

    }

    public void showListMenu(ArrayList<FeastMenu> menus) {
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("List of Set Menus for ordering party:");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (FeastMenu menu : menus) {
            System.out.println(menu.toString());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private void placeOrder(Inputter input, FeastController controller) {
        String cusCode;
        String menuCode;
        LocalDate date;
        int numberOfTables;
        do {
            cusCode = input.inputString("Enter customer code:");

        } while (controller.getCustomerById(cusCode) == null || Validation.isValidCustomerCode(cusCode));

        do {
            menuCode = input.inputString("Enter menu code:");
        } while (controller.getMenuById(cusCode) == null);

        do {
            numberOfTables = input.inputInt("Enter number of table:");
        } while (numberOfTables <= 0);
        boolean flag = false;
        // flag = true => do while chạy lần nữa
        // Người dùng nhập một ngày ở tương lai ==> trả về date local ==> giá trị khác null 
        // ==> không chạy vô if else ==> ngắt vòng lặp
        do {
            String date_raw = input.inputString("Enter date(dd/MM/yyyy):");
            date = Validation.isValidDate(date_raw);
            if (date == null) {
                flag = true;
            }
        } while (flag);

        Order o = new Order(cusCode, menuCode, numberOfTables, date);
        controller.addOrder(o);
        showListOrder(o, controller);
    }

    public void showListOrder(Order o, FeastController controller) {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Customer Order Information [Order ID: " + o.getOrderId());
        System.out.println("------------------------------------------------------------------");
        System.out.println(controller.getCustomerById(o.getCustomerCode()).toString());
        System.out.println("------------------------------------------------------------------");
        FeastMenu menu = controller.getMenuById(o.getMenuCode());
        System.out.println(menu.toString());
        System.out.println("------------------------------------------------------------------");
        System.out.println(String.format("Total Cost            : %,.0f Vnd", o.getNumberOfTable() * menu.getPrice()));
        System.out.println("------------------------------------------------------------------");
    }

    private void updateOrder(Inputter input, FeastController controller) {
        int orderId,numberOfTable;
do {
    orderId = input.inputInt("Enter order id: ");
} while (controller.getOrderById(orderId) == null);

Order oldOrder = controller.getOrderById(orderId);
String menuCode,date_raw;

// 1. Cập nhật menu
String choice2 = input.inputString("Do you want to update menu? (y/n): ");
if (choice2.equalsIgnoreCase("y")) {
    do {
        menuCode = input.inputString("Enter menu code: ");
    } while (controller.getMenuById(menuCode) == null);
}

// 2. Cập nhật số lượng bàn
choice2 = input.inputString("Do you want to update number of table? (y/n): ");
if (choice2.equalsIgnoreCase("y")) {
    do {
        numberOfTable = input.inputInt("Enter number of table: ");
    } while (numberOfTable <= 0);
}
    }

}
