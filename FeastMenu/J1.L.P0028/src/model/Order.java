
package model;

import java.time.LocalDate;


public class Order {
    private static int count = 0; // Để không phụ thuộc vào Order
    private String customerCode;
    private String menuCode;
    private int numberOfTable;
    private int orderId;
    private LocalDate dateOrder;

    public Order(String customerCode, String menuCode, int numberOfTable, LocalDate dateOrder) {
        this.customerCode = customerCode;
        this.menuCode = menuCode;
        this.numberOfTable = numberOfTable;
        this.dateOrder = dateOrder;
        orderId = ++count; // OrderId lưu lại vì count lấy giá trị gán vô
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Order.count = count;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public int getNumberOfTable() {
        return numberOfTable;
    }

    public void setNumberOfTable(int numberOfTable) {
        this.numberOfTable = numberOfTable;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder = dateOrder;
    }

    @Override
    public String toString() {
        return "Order{" + "customerCode=" + customerCode + ", menuCode=" + menuCode + ", numberOfTable=" + numberOfTable + ", orderId=" + orderId + ", dateOrder=" + dateOrder + '}';
    }
    
    
    
    
    
}
