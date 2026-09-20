package controller;

import java.util.ArrayList;
import model.Customer;
import model.FeastMenu;
import util.FileUtils;
import model.Order;

/**
 *
 * @author Admin
 */
public class FeastController {

    private ArrayList<Customer> listCus = new ArrayList<>();
    private ArrayList<FeastMenu> listMenu = new ArrayList<>();
    private ArrayList<Order> listOrder = new ArrayList<>();

    public Customer getCustomerById(String customerCode) { // Phu thuoc class FeastController
        for (Customer cus : listCus) { // Phai dung de truy xuat 
            if (cus.getCustomerCode().equals(customerCode)) {
                return cus;
            }
        }
        return null;
    }

    // Create ham dang ky khach hang
    public void addCustomer(Customer cus) {
        listCus.add(cus);
    }

    public void updateCustomer(Customer newInfoCus) {
        String idCus = newInfoCus.getCustomerCode();
        Customer oldCus = getCustomerById(idCus);
        oldCus.setName(newInfoCus.getName());
        oldCus.setEmail(newInfoCus.getEmail());
        oldCus.setPhoneNumber(newInfoCus.getPhoneNumber());
    }

    public ArrayList<Customer> searchByName(String name) {
        ArrayList<Customer> res = new ArrayList<>();
        for (Customer cus : listCus) { // Lấy từng khách hàng
            if (cus.getName().contains(name)) {
                res.add(cus);
            }
        }
        return res;
    }

    public ArrayList<FeastMenu> getListMenu(String fileName) {
        listMenu = FileUtils.readMenus(fileName);
        return listMenu;
    }
    public ArrayList<FeastMenu> getListMenu() {
        return listMenu;
    

    }
// Để add 1 Order vào thì cusCode và menuCode tồn tại
    public FeastMenu getMenuById(String menuCode){
        for (FeastMenu menu  : listMenu) {
            if (menu.getMenuCode().equals(menuCode)) {
             return menu;
            }
            
        }
        return null;
        
    }
    public void addOrder(Order orderInfo) {
        listOrder.add(orderInfo);
    }
    
// ****
public void updateOrder(Order newInfoOrder){
    int idOrder = newInfoOrder.getOrderId();
    Order oldOrder = getOrderById(idOrder);

    oldOrder.setMenuCode(newInfoOrder.getMenuCode());
    oldOrder.setNumberOfTable(newInfoOrder.getNumberOfTable() );
    oldOrder.setDateOrder(newInfoOrder.getDateOrder());
}

private Order getOrderById(int idOrder) {
    for (Order o : listOrder) {
        if(o.getOrderId() == idOrder)
            return o;
    }
    return null;
}
}
