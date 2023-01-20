package Users;

import datas.ControllerFactory;
import datas.controllers.SalesController;
import model.Order;

import java.util.HashMap;

public class SalesHead extends User {
    SalesController salesController;
    public SalesHead(String id, String role) {
        super(id, role);
        this.salesController = ControllerFactory.getController(SalesController.class, this.getClass());
    }

//    public void setSalesController(SalesController salesController) {
//        this.salesController = salesController;
//    }
    public HashMap<String, HashMap<String, Integer>> getQuotationRequests() {
        return salesController.getQuotationRequests();
    }
    public void processQuotation(String customerID, HashMap<String, Integer> customerRequest) {
        salesController.processQuotation(customerID, customerRequest);
    }
//    private HashMap<String, HashMap<String, ArrayList<Number>>> process(HashMap<String, ArrayList<Object>> toProcessQuotation) {
//        HashMap<String, HashMap<String, ArrayList<Number>>> quotation = new HashMap<>();
//        for (String productID : toProcessQuotation.keySet()) {
//            quotation.put(productID, new HashMap<>());
//            quotation.get(productID).put(((Product) toProcessQuotation.get(productID).get(0)).getProductName(), new ArrayList<>());
//            quotation.get(productID).get(((Product) toProcessQuotation.get(productID).get(0)).getProductName()).
//                    add((Number) toProcessQuotation.get(productID).get(1));
//            quotation.get(productID).get(((Product) toProcessQuotation.get(productID).get(0)).getProductName()).
//                    add(((Product) toProcessQuotation.get(productID).get(0)).getManufacturingCost() * (Integer) toProcessQuotation.get(productID).get(1));
//        }
//        return quotation;
//    }

    public HashMap<String, Order> getOrdersDetails() {
        return salesController.getOrdersDetails();
    }

    public void processInvoice(String id, Order order) {
        salesController.processInvoice(id, order);
    }
}
