package Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FeastOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int orderId;
    private final String customerCode;
    private String menuCode;
    private int numberOfTables;
    private LocalDate eventDate;
    private BigDecimal menuPrice;

    public FeastOrder(
            int orderId,
            String customerCode,
            String menuCode,
            int numberOfTables,
            LocalDate eventDate,
            BigDecimal menuPrice) {
        this.orderId = orderId;
        this.customerCode = customerCode;
        this.menuCode = menuCode;
        this.numberOfTables = numberOfTables;
        this.eventDate = eventDate;
        this.menuPrice = menuPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public int getNumberOfTables() {
        return numberOfTables;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public BigDecimal getTotalCost() {
        return menuPrice.multiply(BigDecimal.valueOf(numberOfTables));
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public void setNumberOfTables(int numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setMenuPrice(BigDecimal menuPrice) {
        this.menuPrice = menuPrice;
    }
}
