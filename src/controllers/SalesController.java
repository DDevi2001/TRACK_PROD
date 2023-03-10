package controllers;

import model.Order;

import java.util.HashMap;

public interface SalesController {

    void processQuotation(String customerID, HashMap<String, Integer> customerRequest);
    void processInvoice(String customerID, String orderID, Order order);
    HashMap<String, HashMap<String, Order>> getOrdersDetails();
    HashMap<String, HashMap<String, Integer>> getQuotationRequests();
}
