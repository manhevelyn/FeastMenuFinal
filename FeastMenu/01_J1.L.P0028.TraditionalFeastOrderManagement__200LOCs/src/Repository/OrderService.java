package Repository;

import Entity.FeastOrder;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class OrderService {
    private final OrderRepository repository;
    private final ArrayList<FeastOrder> orders;

    public OrderService() {
        repository = new OrderRepository();
        orders = loadData();
    }

    private ArrayList<FeastOrder> loadData() {
        try {
            return repository.load();
        } catch (IOException exception) {
            System.out.println("Cannot load order data: " + exception.getMessage());
        } catch (ClassNotFoundException exception) {
            System.out.println("Cannot read order data: incompatible file format.");
        }
        return new ArrayList<FeastOrder>();
    }

    public FeastOrder create(
            String customerCode,
            String menuCode,
            int numberOfTables,
            LocalDate eventDate,
            BigDecimal menuPrice) {
        if (isDuplicate(0, customerCode, menuCode, eventDate)) return null;
        int nextId = 1;
        for (FeastOrder existingOrder : orders) {
            if (existingOrder.getOrderId() >= nextId) nextId = existingOrder.getOrderId() + 1;
        }
        FeastOrder order =
                new FeastOrder(
                        nextId, customerCode, menuCode, numberOfTables, eventDate, menuPrice);
        orders.add(order);
        return order;
    }

    public FeastOrder findById(int orderId) {
        for (FeastOrder order : orders) {
            if (order.getOrderId() == orderId) return order;
        }
        return null;
    }

    public boolean isDuplicate(
            int excludedOrderId, String customerCode, String menuCode, LocalDate eventDate) {
        for (FeastOrder order : orders) {
            if (order.getOrderId() != excludedOrderId
                    && order.getCustomerCode().equalsIgnoreCase(customerCode)
                    && order.getMenuCode().equalsIgnoreCase(menuCode)
                    && order.getEventDate().equals(eventDate)) return true;
        }
        return false;
    }

    public ArrayList<FeastOrder> getSortedOrders() {
        ArrayList<FeastOrder> results = new ArrayList<FeastOrder>(orders);
        for (int first = 0; first < results.size() - 1; first++) {
            for (int second = first + 1; second < results.size(); second++) {
                if (results.get(first).getEventDate().isAfter(results.get(second).getEventDate())) {
                    FeastOrder temporary = results.get(first);
                    results.set(first, results.get(second));
                    results.set(second, temporary);
                }
            }
        }
        return results;
    }

    public void save() throws IOException {
        repository.save(orders);
    }
}