package Users;

import datas.ControllerFactory;
import controllers.SalesController;
import model.Order;

import java.util.HashMap;

public class SalesHead extends User {
    private final SalesController salesController;
    public SalesHead(String id, String role) {
        super(id, role);
        this.salesController = ControllerFactory.getController(SalesController.class);
    }

    public HashMap<String, HashMap<String, Integer>> getQuotationRequests() {
        return salesController.getQuotationRequests();
    }
    public void processQuotation(String customerID, HashMap<String, Integer> customerRequest) {
        salesController.processQuotation(customerID, customerRequest);
    }

    public HashMap<String, HashMap<String, Order>> getOrdersDetails() {
        return salesController.getOrdersDetails();
    }

    public void processInvoice(String id, String orderID, Order order) {
        salesController.processInvoice(id, orderID, order);
    }
}
